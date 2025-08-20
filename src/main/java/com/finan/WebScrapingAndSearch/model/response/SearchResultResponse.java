package com.finan.WebScrapingAndSearch.model.response;

import com.finan.WebScrapingAndSearch.model.dto.SearchResultDTO;
import java.util.List;

public class SearchResultResponse {
  private String status;
  private List<SearchResultDTO> results;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<SearchResultDTO> getResults() {
    return results;
  }

  public void setResults(List<SearchResultDTO> results) {
    this.results = results;
  }

  @Override
  public String toString() {
    return "SearchResultResponse{" + "status='" + status + '\'' + ", results=" + results + '}';
  }
}
