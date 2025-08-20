package com.finan.WebScrapingAndSearch.model.request;

public class SearchRequest {
  private String prefix;
  private String limit;

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public String getLimit() {
    return limit;
  }

  public void setLimit(String limit) {
    this.limit = limit;
  }
}
