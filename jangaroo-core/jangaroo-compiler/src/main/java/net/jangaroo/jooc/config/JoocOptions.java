package net.jangaroo.jooc.config;

public interface JoocOptions {

  enum SemicolonInsertionMode {
    ERROR, WARN, QUIRKS
  }

  SemicolonInsertionMode getSemicolonInsertionMode();

  boolean isDebug();

  boolean isDebugLines();

  boolean isDebugSource();

  boolean isEnableAssertions();

  boolean isGenerateApi();
}
