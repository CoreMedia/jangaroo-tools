package net.jangaroo.jooc.config;

public interface JoocOptions {

  SemicolonInsertionMode getSemicolonInsertionMode();

  DebugMode getDebugMode();

  boolean isEnableAssertions();

  boolean isGenerateApi();
}
