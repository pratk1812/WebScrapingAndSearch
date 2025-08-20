package com.finan.WebScrapingAndSearch.model.dto;

import java.time.LocalDateTime;

public class SearchResultDTO {
    private String url;
    private String matchedContent;
    private LocalDateTime timestamp;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMatchedContent() {
        return matchedContent;
    }

    public void setMatchedContent(String matchedContent) {
        this.matchedContent = matchedContent;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "SearchResultDTO{" +
                "url='" + url + '\'' +
                ", matchedContent='" + matchedContent + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
