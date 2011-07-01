package net.jangaroo.jooc.config;

import java.io.File;
import java.util.List;

public interface JoocOptions {

  SemicolonInsertionMode getSemicolonInsertionMode();

  boolean isDebug();

  boolean isDebugLines();

  boolean isDebugSource();

  boolean isEnableAssertions();

  boolean isGenerateApi();
}
