package net.jangaroo.jooc.config;

public interface JoocOptions {

  SemicolonInsertionMode getSemicolonInsertionMode();

  boolean isDebug();

  boolean isDebugLines();

  boolean isDebugSource();

  boolean isEnableAssertions();

  boolean isGenerateApi();
}
