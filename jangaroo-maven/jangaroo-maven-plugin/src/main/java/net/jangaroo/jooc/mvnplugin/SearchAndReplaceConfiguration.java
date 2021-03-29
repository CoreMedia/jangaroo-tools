package net.jangaroo.jooc.mvnplugin;

import org.apache.maven.plugins.annotations.Parameter;

public class SearchAndReplaceConfiguration {

  @Parameter
  private String search;

  @Parameter
  private String replace;

  public String getSearch() {
    return search;
  }

  public String getReplace() {
    return replace;
  }
}
