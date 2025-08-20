package com.finan.WebScrapingAndSearch.controller;

import com.finan.WebScrapingAndSearch.model.request.ScheduleJobRequest;
import com.finan.WebScrapingAndSearch.model.request.SearchRequest;
import com.finan.WebScrapingAndSearch.model.response.JobStatusResponse;
import com.finan.WebScrapingAndSearch.model.response.ScheduleJobResponse;
import com.finan.WebScrapingAndSearch.model.response.SearchResultResponse;
import com.finan.WebScrapingAndSearch.service.ScheduleJobService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class ScheduleJobController {

  private static final Logger LOGGER = LogManager.getLogger(ScheduleJobController.class);

  private final ScheduleJobService scheduleJobService;

  @Autowired
  public ScheduleJobController(ScheduleJobService scheduleJobService) {
    this.scheduleJobService = scheduleJobService;
  }

  @PostMapping("/scrape")
  public ResponseEntity<ScheduleJobResponse> scheduleJob(
      @RequestBody ScheduleJobRequest scheduleJobRequest) throws SchedulerException {
    LOGGER.info("Executing scheduleJob");
    ScheduleJobResponse response = scheduleJobService.schedule(scheduleJobRequest);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/status/{jobId}")
  public ResponseEntity<JobStatusResponse> status(@PathVariable String jobId) {
    LOGGER.info("Executing status");
    JobStatusResponse response = scheduleJobService.status(jobId);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/search")
  public ResponseEntity<SearchResultResponse> search(@RequestBody SearchRequest searchRequest) {
    LOGGER.info("Executing search");
    SearchResultResponse response = scheduleJobService.search(searchRequest);
    return ResponseEntity.ok(response);
  }
}
