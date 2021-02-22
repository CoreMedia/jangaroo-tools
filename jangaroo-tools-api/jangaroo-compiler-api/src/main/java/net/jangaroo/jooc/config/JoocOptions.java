package net.jangaroo.jooc.config;

import java.io.File;
import java.util.List;

public interface JoocOptions {

  SemicolonInsertionMode getSemicolonInsertionMode();

  DebugMode getDebugMode();

  boolean isSuppressCommentedActionScriptCode();

  boolean isEnableAssertions();

  boolean isGenerateApi();

  boolean isUseEcmaParameterInitializerSemantics();

  boolean isMigrateToTypeScript();

  String getAs3PackagePrefixToRemoveInTypeScript();

  List<SearchAndReplace> getNpmPackageNameReplacers();

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
