package com.finan.WebScrapingAndSearch.model.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ScrappedDataDTO {

  private Long id;
  private String jobId;
  private String url;
  private LocalDateTime timeStamp;
  private String data;
  private Set<String> keyWords = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public LocalDateTime getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(LocalDateTime timeStamp) {
    this.timeStamp = timeStamp;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Set<String> getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(Set<String> keyWords) {
    this.keyWords = keyWords;
  }

  @Override
  public String toString() {
    return "ScrappedDataDTO{"
        + "id="
        + id
        + ", jobId='"
        + jobId
        + '\''
        + ", url='"
        + url
        + '\''
        + ", timeStamp="
        + timeStamp
        + ", data='"
        + data
        + '\''
        + ", keyWords="
        + keyWords
        + '}';
  }
}
