package com.finan.WebScrapingAndSearch.job;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.finan.WebScrapingAndSearch.exception.ApiException;
import com.finan.WebScrapingAndSearch.model.dto.ScrappedDataDTO;
import com.finan.WebScrapingAndSearch.model.request.ScheduleJobRequest;
import com.finan.WebScrapingAndSearch.service.ScrappedDataService;
import com.finan.WebScrapingAndSearch.service.WebScrapperService;
import java.io.IOException;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.*;

@ExtendWith(MockitoExtension.class)
class WebScrapperJobTest {
  @InjectMocks private WebScrapperJob webScrapperJob;

  @Mock private ScrappedDataService scrappedDataService;

  @Mock private WebScrapperService webScrapperService;

  @Mock private JobExecutionContext context;

  @Mock private JobDetail jobDetail;

  private JobDataMap jobDataMap;

  @BeforeEach
  void setUp() {
    jobDataMap = new JobDataMap();
    when(context.getJobDetail()).thenReturn(jobDetail);
    when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
  }

  @Test
  void execute_withMixedResults_updatesServiceAndBytes() throws Exception {
    // arrange
    ScheduleJobRequest request = new ScheduleJobRequest();
    request.setUrls(List.of("url1", "url2"));
    request.setKeywords(List.of("match"));
    jobDataMap.put("request", request);

    when(jobDetail.getKey()).thenReturn(new JobKey("myJob"));
    when(webScrapperService.scrape("url1")).thenReturn("this is a match");
    when(webScrapperService.scrape("url2")).thenReturn("");

    // act
    webScrapperJob.execute(context);

    // assert upsertAll
    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<ScrappedDataDTO>> captor = ArgumentCaptor.forClass(List.class);
    verify(scrappedDataService).upsertAll(captor.capture());

    List<ScrappedDataDTO> dtos = captor.getValue();
    assertEquals(2, dtos.size(), "Should process one DTO and one null");

    // first URL produced a DTO
    ScrappedDataDTO dto1 = dtos.get(0);
    assertNotNull(dto1);
    assertEquals("url1", dto1.getUrl());
    assertEquals("this is a match", dto1.getData());
    assertTrue(dto1.getKeyWords().contains("match"));
    assertEquals("DEFAULT.myJob", dto1.getJobId());
    assertNotNull(dto1.getTimeStamp());

    // second URL produced null
    assertNull(dtos.get(1));

    // assert bytes recorded
    long expectedBytes = "this is a match".getBytes().length;
    assertEquals(expectedBytes, jobDataMap.getLong("bytes"));
  }

  @Test
  void execute_whenScrapeThrowsIOException_wrapsInApiException() throws Exception {
    // arrange
    ScheduleJobRequest request = new ScheduleJobRequest();
    request.setUrls(List.of("badUrl"));
    request.setKeywords(List.of("x"));
    jobDataMap.put("request", request);
    when(jobDetail.getKey()).thenReturn(new JobKey("jobErr"));
    when(webScrapperService.scrape("badUrl")).thenThrow(new IOException("network down"));

    // act & assert
    assertThrows(ApiException.class, () -> webScrapperJob.execute(context));
  }

  @Test
  void execute_withNoUrls_callsUpsertWithEmptyListAndZeroBytes() {
    // arrange
    ScheduleJobRequest request = new ScheduleJobRequest();
    request.setUrls(Collections.emptyList());
    request.setKeywords(List.of("irrelevant"));
    jobDataMap.put("request", request);
    when(jobDetail.getKey()).thenReturn(new JobKey("jobEmpty"));

    // act
    webScrapperJob.execute(context);

    // assert
    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<ScrappedDataDTO>> captor = ArgumentCaptor.forClass(List.class);
    verify(scrappedDataService).upsertAll(captor.capture());
    assertTrue(captor.getValue().isEmpty(), "Should pass empty list");

    assertEquals(0L, jobDataMap.getLong("bytes"), "AtomicLong should remain zero for no URLs");
  }
}
