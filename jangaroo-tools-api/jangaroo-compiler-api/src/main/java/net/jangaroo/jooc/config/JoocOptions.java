package net.jangaroo.jooc.config;

public interface JoocOptions {

  SemicolonInsertionMode getSemicolonInsertionMode();

  DebugMode getDebugMode();

  boolean isEnableAssertions();

  boolean isGenerateApi();

  PublicApiViolationsMode getPublicApiViolationsMode();

  /**
   * If true, the compiler will add an [ExcludeClass] annotation to any
   * API stub whose source class contains neither an [IncludeClass] nor an [ExcludeClass]
   * annotation.
   */
  boolean isExcludeClassByDefault();
 }
