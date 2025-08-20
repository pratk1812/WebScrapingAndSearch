package com.finan.WebScrapingAndSearch.model.request;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleJobRequest {
    private List<String> urls;
    private List<String> keywords;
    private LocalDateTime schedule;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public LocalDateTime getSchedule() {
        return schedule;
    }

    public void setSchedule(LocalDateTime schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "ScheduleJobRequest{" +
                "urls=" + urls +
                ", keywords=" + keywords +
                ", schedule=" + schedule +
                '}';
    }
}
