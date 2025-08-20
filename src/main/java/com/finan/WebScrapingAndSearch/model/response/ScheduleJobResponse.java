package com.finan.WebScrapingAndSearch.model.response;

import java.time.LocalDateTime;

public class ScheduleJobResponse {
  private String status;
  private String message;
  private String jobId;
  private LocalDateTime scheduledAt;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public LocalDateTime getScheduledAt() {
    return scheduledAt;
  }

  public void setScheduledAt(LocalDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
  }

  @Override
  public String toString() {
    return "ScheduleJobResponse{"
        + "status='"
        + status
        + '\''
        + ", message='"
        + message
        + '\''
        + ", jobId='"
        + jobId
        + '\''
        + ", scheduledAt='"
        + scheduledAt
        + '\''
        + '}';
  }
}
