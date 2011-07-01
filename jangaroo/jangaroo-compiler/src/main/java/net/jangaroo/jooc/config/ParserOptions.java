package net.jangaroo.jooc.config;

import net.jangaroo.jooc.config.SemicolonInsertionMode;

public interface ParserOptions {
  SemicolonInsertionMode getSemicolonInsertionMode();

  boolean isVerbose();
}
