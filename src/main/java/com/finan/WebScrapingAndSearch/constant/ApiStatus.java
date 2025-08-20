package com.finan.WebScrapingAndSearch.constant;

public enum ApiStatus {
  SUCCESS("success"),
  CREATED("created"),
  ERROR("error"),
  NOT_FOUND("not found"),
  UNAUTHORIZED("unauthorized"),
  BAD_REQUEST("bad request");

  private final String value;

  ApiStatus(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
