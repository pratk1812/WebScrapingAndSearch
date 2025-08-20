package com.finan.WebScrapingAndSearch.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SearchTrieTest {

  @Test
  void insert() {}

  @Test
  void search() {

    String word = "apple";

    SearchTrie searchTrie = SearchTrie.of(word);

    boolean search = searchTrie.search("app");

    assertTrue(search);
  }

  @Test
  void delete() {

    String[] words = {"apple", "banana"};

    SearchTrie searchTrie = SearchTrie.of(words);

    boolean search = searchTrie.search("app");

    assertTrue(search);

    searchTrie.delete("apple");

    boolean search1 = searchTrie.search("apple");

    assertFalse(search1);
  }
}
