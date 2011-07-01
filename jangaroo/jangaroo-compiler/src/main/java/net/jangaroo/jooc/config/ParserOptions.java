package net.jangaroo.jooc.config;

import java.io.File;
import java.util.List;

public interface ParserOptions {
  SemicolonInsertionMode getSemicolonInsertionMode();

  boolean isVerbose();
}
