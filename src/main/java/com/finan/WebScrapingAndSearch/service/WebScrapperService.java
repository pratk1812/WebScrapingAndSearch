package com.finan.WebScrapingAndSearch.service;

import com.finan.WebScrapingAndSearch.exception.ApiException;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class WebScrapperService {
  private static final Logger LOGGER = LogManager.getLogger(WebScrapperService.class);

  private static final String USER_AGENT =
"""
Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36""";

  public String scrape(String jobId, String url) {
    LOGGER.info("scrapping : {}", url);
    Document doc;
    try {
      doc =
          Jsoup.connect(url)
              .userAgent(USER_AGENT)
              .referrer("http://google.com")
              .timeout(5000)
              .get();
    } catch (IOException e) {
      throw new ApiException(e);
    }
    return doc.body().text();
  }
}
