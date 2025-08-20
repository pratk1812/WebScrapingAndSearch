package com.finan.WebScrapingAndSearch.service;

import static org.junit.jupiter.api.Assertions.*;

import com.finan.WebScrapingAndSearch.util.SearchTrie;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WebScrapperServiceTest {

  @InjectMocks private WebScrapperService service;

  @Test
  void scrape() throws IOException {
    String url = "https://www.scrapingcourse.com/ecommerce/";

    String data = service.scrape(url);

    SearchTrie searchTrie = SearchTrie.of("search", "option");

    assertTrue(data.contains("Sort by popularity"));
  }
}
