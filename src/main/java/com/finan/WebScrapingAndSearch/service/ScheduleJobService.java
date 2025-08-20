package com.finan.WebScrapingAndSearch.service;

import com.finan.WebScrapingAndSearch.constant.ApiStatus;
import com.finan.WebScrapingAndSearch.constant.Messages;
import com.finan.WebScrapingAndSearch.exception.ApiException;
import com.finan.WebScrapingAndSearch.job.WebScrapperJob;
import com.finan.WebScrapingAndSearch.model.dto.ScrappedDataDTO;
import com.finan.WebScrapingAndSearch.model.dto.SearchResultDTO;
import com.finan.WebScrapingAndSearch.model.request.ScheduleJobRequest;
import com.finan.WebScrapingAndSearch.model.request.SearchRequest;
import com.finan.WebScrapingAndSearch.model.response.JobStatusResponse;
import com.finan.WebScrapingAndSearch.model.response.ScheduleJobResponse;
import com.finan.WebScrapingAndSearch.model.response.SearchResultResponse;
import com.finan.WebScrapingAndSearch.persistence.mapper.ScrappedDataMapper;
import com.finan.WebScrapingAndSearch.persistence.repository.ScrappedDataRepository;
import com.finan.WebScrapingAndSearch.util.SearchTrie;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleJobService {

  private static final Logger LOGGER = LogManager.getLogger(ScheduleJobService.class);

  private final Scheduler scheduler;
  private final ScrappedDataRepository scrappedDataRepository;
  private final ScrappedDataMapper scrappedDataMapper;

  @Autowired
  public ScheduleJobService(
      Scheduler scheduler,
      ScrappedDataRepository scrappedDataRepository,
      ScrappedDataMapper scrappedDataMapper) {
    this.scheduler = scheduler;
    this.scrappedDataRepository = scrappedDataRepository;
    this.scrappedDataMapper = scrappedDataMapper;
  }

  public ScheduleJobResponse schedule(ScheduleJobRequest request) {
    LOGGER.info("scheduling job");
    ScheduleJobResponse response = new ScheduleJobResponse();

    String jobId = UUID.randomUUID().toString();
    JobDataMap jobDataMap = new JobDataMap();
    jobDataMap.put("request", request);

    JobDetail jobDetail =
        JobBuilder.newJob(WebScrapperJob.class)
            .withIdentity(new JobKey(jobId))
            .usingJobData(jobDataMap)
            .storeDurably()
            .build();

    Trigger trigger =
        TriggerBuilder.newTrigger()
            .withIdentity(new TriggerKey(jobId))
            .startAt(Date.from(request.getSchedule().toInstant(ZoneOffset.UTC)))
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
            .forJob(jobDetail)
            .build();
    response.setJobId(jobId);
    response.setStatus(ApiStatus.SUCCESS.toString());
    response.setScheduledAt(request.getSchedule());
    response.setMessage(Messages.SCRAPING_INITIATED_SUCCESSFULLY);

    try {
      scheduler.scheduleJob(jobDetail, trigger);
    } catch (SchedulerException e) {
      throw new ApiException(e);
    }
    return response;
  }

  public JobStatusResponse status(String jobId) {
    LOGGER.info("fetching status");
    JobStatusResponse jobStatusResponse = new JobStatusResponse();
    try {
      JobKey jobKey = new JobKey(jobId);
      JobDetail jobDetail = scheduler.getJobDetail(jobKey);
      JobDataMap jobDataMap = jobDetail.getJobDataMap();
      ScheduleJobRequest scheduleJobRequest = (ScheduleJobRequest) jobDataMap.get("request");
      long bytes = (long) jobDataMap.get("bytes");
      jobStatusResponse.setDataSize((bytes / (1024.0 * 1024.0)) + " MB");
      jobStatusResponse.setJobId(jobId);
      jobStatusResponse.setUrlsScraped(scheduleJobRequest.getUrls());
      jobStatusResponse.setKeywordsFound(scheduleJobRequest.getKeywords());

      Trigger trigger = scheduler.getTrigger(new TriggerKey(jobId));
      LocalDateTime endDate =
          LocalDateTime.ofInstant(trigger.getEndTime().toInstant(), ZoneOffset.UTC);
      jobStatusResponse.setFinishedAt(endDate);
      jobStatusResponse.setStatus(ApiStatus.SUCCESS.toString());
    } catch (SchedulerException e) {
      throw new ApiException(e);
    }
    return jobStatusResponse;
  }

  public SearchResultResponse search(SearchRequest searchRequest) {
    LOGGER.info("searching for '{}'", searchRequest.getPrefix());
    List<ScrappedDataDTO> scrappedData =
        scrappedDataRepository.findAll().stream().map(scrappedDataMapper::toDto).toList();

    List<SearchResultDTO> results =
        scrappedData.parallelStream()
            .map(
                data -> {
                  SearchTrie searchTrie = SearchTrie.of(data.getKeyWords().toArray(String[]::new));

                  boolean keyWordFound = searchTrie.search(searchRequest.getPrefix());

                  if (keyWordFound) {
                    SearchResultDTO searchResult = new SearchResultDTO();
                    searchResult.setTimestamp(data.getTimeStamp());
                    searchResult.setUrl(data.getUrl());
                    searchResult.setMatchedContent(data.getData());
                    return searchResult;
                  }
                  return null;
                })
            .filter(Objects::nonNull)
            .toList();

    SearchResultResponse response = new SearchResultResponse();
    response.setStatus(ApiStatus.SUCCESS.toString());
    response.setResults(results);
    return response;
  }
}
