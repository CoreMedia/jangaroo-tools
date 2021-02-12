package net.jangaroo.jooc.config;

import java.util.regex.Pattern;

public class SearchAndReplace {

  public final Pattern search;
  public final String replace;

  public SearchAndReplace(Pattern search, String replace) {
    this.search = search;
    this.replace = replace;
  }
}
