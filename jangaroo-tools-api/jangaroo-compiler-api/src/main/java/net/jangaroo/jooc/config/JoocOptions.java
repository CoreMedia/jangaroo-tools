package net.jangaroo.jooc.config;

import java.io.File;

public interface JoocOptions {

  SemicolonInsertionMode getSemicolonInsertionMode();

  DebugMode getDebugMode();

  boolean isEnableAssertions();

  boolean isGenerateApi();

  boolean isGenerateOverrides();

  PublicApiViolationsMode getPublicApiViolationsMode();

  /**
   * If true, the compiler will add an [ExcludeClass] annotation to any
   * API stub whose source class contains neither an [PublicApi] nor an [ExcludeClass]
   * annotation.
   */
  boolean isExcludeClassByDefault();

  boolean isGenerateSourceMaps();

  File getKeepGeneratedActionScriptDirectory();
}
