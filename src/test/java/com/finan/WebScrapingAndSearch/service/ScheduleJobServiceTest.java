package com.finan.WebScrapingAndSearch.service;

import com.finan.WebScrapingAndSearch.constant.ApiStatus;
import com.finan.WebScrapingAndSearch.constant.Messages;
import com.finan.WebScrapingAndSearch.exception.ApiException;
import com.finan.WebScrapingAndSearch.model.dto.ScrappedDataDTO;
import com.finan.WebScrapingAndSearch.model.dto.SearchResultDTO;
import com.finan.WebScrapingAndSearch.model.request.ScheduleJobRequest;
import com.finan.WebScrapingAndSearch.model.request.SearchRequest;
import com.finan.WebScrapingAndSearch.model.response.JobStatusResponse;
import com.finan.WebScrapingAndSearch.model.response.ScheduleJobResponse;
import com.finan.WebScrapingAndSearch.model.response.SearchResultResponse;
import com.finan.WebScrapingAndSearch.persistence.enitity.ScrappedDataEntity;
import com.finan.WebScrapingAndSearch.persistence.mapper.ScrappedDataMapper;
import com.finan.WebScrapingAndSearch.persistence.repository.ScrappedDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleJobServiceTest {

    @Mock
    private Scheduler scheduler;
    @Mock
    private ScrappedDataRepository scrappedDataRepository;
    @Mock
    private ScrappedDataMapper scrappedDataMapper;
    @InjectMocks
    private ScheduleJobService scheduleJobService;

    @Test
    void testSchedule_success() throws Exception {
        ScheduleJobRequest request = new ScheduleJobRequest();
        request.setSchedule(LocalDateTime.now().plusMinutes(1));

        ScheduleJobResponse response = scheduleJobService.schedule(request);

        assertNotNull(response.getJobId());
        assertEquals(ApiStatus.SUCCESS.toString(), response.getStatus());
        assertEquals(Messages.SCRAPING_INITIATED_SUCCESSFULLY, response.getMessage());
        assertEquals(request.getSchedule(), response.getScheduledAt());

        verify(scheduler).scheduleJob(any(JobDetail.class), any(Trigger.class));
    }

    @Test
    void testSchedule_schedulerException() throws Exception {
        ScheduleJobRequest request = new ScheduleJobRequest();
        request.setSchedule(LocalDateTime.now().plusMinutes(1));

        doThrow(new SchedulerException("fail")).when(scheduler).scheduleJob(any(), any());

        assertThrows(ApiException.class, () -> scheduleJobService.schedule(request));
    }

    @Test
    void testStatus_success() throws Exception {
        String jobId = "job-123";
        ScheduleJobRequest mockRequest = new ScheduleJobRequest();
        mockRequest.setUrls(List.of("https://example.com"));
        mockRequest.setKeywords(List.of("java", "spring"));

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("request", mockRequest);
        jobDataMap.put("bytes", 1048576L); // 1 MB

        JobDetail jobDetail = mock(JobDetail.class);
        when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
        when(scheduler.getJobDetail(new JobKey(jobId))).thenReturn(jobDetail);

        Trigger trigger = mock(Trigger.class);
        Date endDate = Date.from(LocalDateTime.now().plusMinutes(5).toInstant(ZoneOffset.UTC));
        when(trigger.getEndTime()).thenReturn(endDate);
        when(scheduler.getTrigger(new TriggerKey(jobId))).thenReturn(trigger);

        JobStatusResponse response = scheduleJobService.status(jobId);

        assertEquals("1.0 MB", response.getDataSize());
        assertEquals(jobId, response.getJobId());
        assertEquals(mockRequest.getUrls(), response.getUrlsScraped());
        assertEquals(mockRequest.getKeywords(), response.getKeywordsFound());
        assertEquals(ApiStatus.SUCCESS.toString(), response.getStatus());
        assertNotNull(response.getFinishedAt());
    }

    @Test
    void testStatus_schedulerException() throws Exception {
        String jobId = "job-123";
        when(scheduler.getJobDetail(any())).thenThrow(new SchedulerException("fail"));

        assertThrows(ApiException.class, () -> scheduleJobService.status(jobId));
    }

    @Test
    void testSearch_success() {
        SearchRequest request = new SearchRequest();
        request.setPrefix("java");

        ScrappedDataDTO dto = new ScrappedDataDTO();
        dto.setKeyWords(Set.of("java", "spring"));
        dto.setTimeStamp(LocalDateTime.now());
        dto.setUrl("https://example.com");
        dto.setData("Java content");

    ScrappedDataEntity entity = new ScrappedDataEntity();
        when(scrappedDataRepository.findAll()).thenReturn(List.of(entity));
        when(scrappedDataMapper.toDto(entity)).thenReturn(dto);

        SearchResultResponse response = scheduleJobService.search(request);

        assertEquals(ApiStatus.SUCCESS.toString(), response.getStatus());
        assertEquals(1, response.getResults().size());

        SearchResultDTO result = response.getResults().get(0);
        assertEquals(dto.getUrl(), result.getUrl());
        assertEquals(dto.getData(), result.getMatchedContent());
        assertEquals(dto.getTimeStamp(), result.getTimestamp());
    }

    @Test
    void testSearch_noMatch() {
        SearchRequest request = new SearchRequest();
        request.setPrefix("python");

        ScrappedDataDTO dto = new ScrappedDataDTO();
        dto.setKeyWords(Set.of("java", "spring"));
        dto.setTimeStamp(LocalDateTime.now());
        dto.setUrl("https://example.com");
        dto.setData("Java content");

    ScrappedDataEntity entity = new ScrappedDataEntity();
        when(scrappedDataRepository.findAll()).thenReturn(List.of(entity));
        when(scrappedDataMapper.toDto(entity)).thenReturn(dto);

        SearchResultResponse response = scheduleJobService.search(request);

        assertEquals(ApiStatus.SUCCESS.toString(), response.getStatus());
        assertTrue(response.getResults().isEmpty());
    }


}
