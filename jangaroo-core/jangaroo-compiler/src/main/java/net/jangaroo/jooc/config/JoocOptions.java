package net.jangaroo.jooc.config;

public interface JoocOptions {
  boolean isDebug();

  boolean isDebugLines();

  boolean isDebugSource();

  boolean isEnableAssertions();

  boolean isAllowDuplicateLocalVariables();

  boolean isEnableGuessingMembers();

  boolean isEnableGuessingClasses();

  boolean isEnableGuessingTypeCasts();

  boolean isGenerateApi();
}
