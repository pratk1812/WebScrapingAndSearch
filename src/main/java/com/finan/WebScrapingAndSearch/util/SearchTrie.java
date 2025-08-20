package com.finan.WebScrapingAndSearch.util;

import java.util.ArrayList;
import java.util.List;

public class SearchTrie {
  private static final int SIZE = 36;
  private static final char LETTER_BASE = 'A';
  private static final char DIGIT_BASE = '0';

  private final SearchTrie[] nodes = new SearchTrie[SIZE];
  private boolean isEndOfWord;

  private SearchTrie(boolean isEndOfWord) {
    this.isEndOfWord = isEndOfWord;
  }

  private static void insert(SearchTrie root, String word) {
    SearchTrie current = root;
    for (char c : word.toCharArray()) {

      int index = charToIndex(c);
      if (index < 0) continue;

      if (current.nodes[index] == null) {
        SearchTrie newNode = new SearchTrie(false);
        current.nodes[index] = newNode;
        current = newNode;
      } else {
        current = current.nodes[index];
      }
    }
    current.isEndOfWord = true;
  }

  private static boolean search(SearchTrie root, String word) {
    List<String> array = new ArrayList<>();
    SearchTrie current = root;
    for (char c : word.toCharArray()) {
      int index = charToIndex(c);
      if (index == -1) continue;

      if (current.nodes[index] == null) {
        return false;
      } else {
        array.add(String.valueOf(c));
        current = current.nodes[index];
      }
    }
    return array.size() == word.length();
  }

  private static int charToIndex(char raw) {
    if (Character.isLetter(raw)) {
      char c = Character.toUpperCase(raw);
      int idx = c - LETTER_BASE;
      if (idx >= 0 && idx < 26) return idx;
    } else if (Character.isDigit(raw)) {
      return 26 + (raw - DIGIT_BASE);
    }
    return -1;
  }

  public static void delete(SearchTrie root, String word) {

    List<SearchTrie> nodePath = new ArrayList<>();
    List<Integer> indexPath = new ArrayList<>();

    SearchTrie current = root;
    // nodePath.add(current);

    for (char c : word.toCharArray()) {
      int idx = charToIndex(c);
      if (idx < 0) {
        continue;
      }

      if (current.nodes[idx] == null) {
        return;
      } else {
        nodePath.add(current);
        indexPath.add(idx);
        current = current.nodes[idx];
      }
    }

    if (current.isEndOfWord) current.isEndOfWord = false;

    for (int i = indexPath.size() - 1; i >= 0; i--) {
      SearchTrie parent = nodePath.get(i);
      int childIdx = indexPath.get(i);
      SearchTrie child = parent.nodes[childIdx];

      if (child != null && !child.isEndOfWord && isEmpty(child)) {
        parent.nodes[childIdx] = null;
      } else {
        break;
      }
    }
  }

  private static boolean isEmpty(SearchTrie node) {
    for (SearchTrie child : node.nodes) {
      if (child != null) {
        return false;
      }
    }
    return true;
  }

  public static SearchTrie of(String... words) {
    SearchTrie searchTrie = new SearchTrie(false);
    for (String word : words) {
      searchTrie.insert(word);
    }
    return searchTrie;
  }

  public static SearchTrie of() {
    return new SearchTrie(false);
  }

  public void delete(String word) {
    delete(this, word);
  }

  public void insert(String word) {
    insert(this, word);
  }

  public boolean search(String word) {
    return search(this, word);
  }

  public boolean isEmpty() {
    return isEmpty(this);
  }
}
