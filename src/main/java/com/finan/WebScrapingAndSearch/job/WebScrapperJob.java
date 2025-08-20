package com.finan.WebScrapingAndSearch.job;

import com.finan.WebScrapingAndSearch.constant.Messages;
import com.finan.WebScrapingAndSearch.exception.ApiException;
import com.finan.WebScrapingAndSearch.model.dto.ScrappedDataDTO;
import com.finan.WebScrapingAndSearch.model.request.ScheduleJobRequest;
import com.finan.WebScrapingAndSearch.service.ScrappedDataService;
import com.finan.WebScrapingAndSearch.service.WebScrapperService;
import com.finan.WebScrapingAndSearch.util.SearchTrie;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebScrapperJob implements Job {
  private static final Logger LOGGER = LogManager.getLogger(WebScrapperJob.class);

  @Autowired private ScrappedDataService scrappedDataService;

  @Autowired private WebScrapperService webScrapperService;

  @Override
  public void execute(JobExecutionContext context) {
    LOGGER.info("Staring job execution");
    try {
      ScheduleJobRequest request =
          (ScheduleJobRequest) context.getJobDetail().getJobDataMap().get("request");
      List<String> urls = request.getUrls();
      List<String> keyWords = request.getKeywords();
      String jobId = context.getJobDetail().getKey().toString();
      AtomicLong bytes = new AtomicLong(0L);

      List<ScrappedDataDTO> scrappedDataDTOS =
          urls.stream()
              .map(
                  url ->
                      CompletableFuture.supplyAsync(
                          () -> {
                            final SearchTrie searchTrie =
                                SearchTrie.of(keyWords.toArray(String[]::new));

                            String data;
                            try {
                              data = webScrapperService.scrape(url);
                              bytes.addAndGet(data.getBytes().length);
                            } catch (IOException e) {
                              LOGGER.error(Messages.JOB_EXECUTION_FAILED);
                              throw new ApiException(e);
                            }

                            if (data.isEmpty()) return null;

                            ScrappedDataDTO scrappedDataDTO = new ScrappedDataDTO();
                            String[] words = data.split(" ");
                            for (int i = 0; i < words.length && !searchTrie.isEmpty(); i++) {
                              boolean found = searchTrie.search(words[i]);
                              if (found) {
                                scrappedDataDTO.getKeyWords().add(words[i]);
                                searchTrie.delete(words[i]);
                              }
                            }

                            scrappedDataDTO.setJobId(jobId);
                            scrappedDataDTO.setTimeStamp(LocalDateTime.now());
                            scrappedDataDTO.setData(data);
                            scrappedDataDTO.setUrl(url);

                            return scrappedDataDTO;
                          }))
              .map(CompletableFuture::join)
              .toList();

      scrappedDataService.upsertAll(scrappedDataDTOS);
      context.getJobDetail().getJobDataMap().put("bytes", bytes.get());
    } catch (Exception e) {
      LOGGER.error(Messages.JOB_EXECUTION_FAILED);
      throw new ApiException(e);
    }
    LOGGER.info("Job finished");
  }
}
