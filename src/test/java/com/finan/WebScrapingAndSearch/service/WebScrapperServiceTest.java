package com.finan.WebScrapingAndSearch.service;

import com.finan.WebScrapingAndSearch.persistence.enitity.ScrappedDataEntity;
import com.finan.WebScrapingAndSearch.persistence.repository.ScrappedDataRepository;
import com.finan.WebScrapingAndSearch.util.SearchTrie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WebScrapperServiceTest {

  @InjectMocks
  private WebScrapperService service;

  @Test
  void scrape() {
    String url = "https://www.scrapingcourse.com/ecommerce/";

    String data = service.scrape("NA", url);

    SearchTrie searchTrie = SearchTrie.of("search", "option");

    assertTrue(data.contains("Sort by popularity"));
  }
}
