package com.finan.WebScrapingAndSearch.controller;

import com.finan.WebScrapingAndSearch.model.request.ScheduleJobRequest;
import com.finan.WebScrapingAndSearch.model.request.SearchRequest;
import com.finan.WebScrapingAndSearch.model.response.JobStatusResponse;
import com.finan.WebScrapingAndSearch.model.response.ScheduleJobResponse;
import com.finan.WebScrapingAndSearch.model.response.SearchResultResponse;
import com.finan.WebScrapingAndSearch.service.ScheduleJobService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleJobControllerTest {
    @Mock
    private ScheduleJobService scheduleJobService;
    @InjectMocks
    private ScheduleJobController controller;


    @Test
    void testScheduleJob() throws Exception {
        ScheduleJobRequest request = new ScheduleJobRequest();
        ScheduleJobResponse expectedResponse = new ScheduleJobResponse();

        when(scheduleJobService.schedule(request)).thenReturn(expectedResponse);

        ResponseEntity<ScheduleJobResponse> response = controller.scheduleJob(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(scheduleJobService).schedule(request);
    }

    @Test
    void testStatus() {
        String jobId = "job-123";
        JobStatusResponse expectedResponse = new JobStatusResponse();

        when(scheduleJobService.status(jobId)).thenReturn(expectedResponse);

        ResponseEntity<JobStatusResponse> response = controller.status(jobId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(scheduleJobService).status(jobId);
    }

    @Test
    void testSearch() {
        SearchRequest request = new SearchRequest();
        SearchResultResponse expectedResponse = new SearchResultResponse();

        when(scheduleJobService.search(request)).thenReturn(expectedResponse);

        ResponseEntity<SearchResultResponse> response = controller.search(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(scheduleJobService).search(request);
    }


}
