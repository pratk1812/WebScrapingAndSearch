package com.finan.WebScrapingAndSearch.persistence.enitity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(
    name = "scrapped_data",
    indexes = {@Index(name = "idx_scrapped_data_job_id", columnList = "job_id")},
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uc_scrapped_url",
          columnNames = {"url"})
    })
public class ScrappedDataEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "job_id", nullable = false)
  private String jobId;

  @Column(name = "url", nullable = false)
  private String url;

  @Column(name = "time_stamp", nullable = false)
  private LocalDateTime timeStamp;

  @Lob
  @Column(name = "data")
  private String data;

  @ElementCollection
  @Column(name = "key_word")
  @CollectionTable(name = "scrapped_data_keyWords", joinColumns = @JoinColumn(name = "owner_id"))
  private Set<String> keyWords = new LinkedHashSet<>();

  public Set<String> getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(Set<String> keyWords) {
    this.keyWords = keyWords;
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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public final boolean equals(Object object) {
    if (!(object instanceof ScrappedDataEntity that)) return false;

    return id.equals(that.id) && jobId.equals(that.jobId) && url.equals(that.url);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + jobId.hashCode();
    result = 31 * result + url.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "ScrappedDataEntity{"
        + "id="
        + id
        + ", jobId='"
        + jobId
        + '\''
        + ", url='"
        + url
        + '\''
        + ", data='"
        + data
        + '\''
        + '}';
  }
}
