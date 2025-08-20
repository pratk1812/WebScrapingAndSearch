package com.finan.WebScrapingAndSearch.model.response;

import java.time.LocalDateTime;
import java.util.List;

public class JobStatusResponse {
  private String status;
  private String jobId;
  private List<String> urlsScraped;
  private List<String> keywordsFound;
  private String dataSize;
  private LocalDateTime finishedAt;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public List<String> getUrlsScraped() {
    return urlsScraped;
  }

  public void setUrlsScraped(List<String> urlsScraped) {
    this.urlsScraped = urlsScraped;
  }

  public List<String> getKeywordsFound() {
    return keywordsFound;
  }

  public void setKeywordsFound(List<String> keywordsFound) {
    this.keywordsFound = keywordsFound;
  }

  public String getDataSize() {
    return dataSize;
  }

  public void setDataSize(String dataSize) {
    this.dataSize = dataSize;
  }

  public LocalDateTime getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(LocalDateTime finishedAt) {
    this.finishedAt = finishedAt;
  }

  @Override
  public String toString() {
    return "JobStatusResponse{"
        + "status='"
        + status
        + '\''
        + ", jobId='"
        + jobId
        + '\''
        + ", urlsScraped="
        + urlsScraped
        + ", keywordsFound="
        + keywordsFound
        + ", dataSize='"
        + dataSize
        + '\''
        + ", finishedAt='"
        + finishedAt
        + '\''
        + '}';
  }
}
