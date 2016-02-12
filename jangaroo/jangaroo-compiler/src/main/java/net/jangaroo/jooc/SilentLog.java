package net.jangaroo.jooc;

import net.jangaroo.jooc.api.CompileLog;
import net.jangaroo.jooc.api.FilePosition;

class SilentLog implements CompileLog {
  @Override
  public void error(FilePosition position, String msg) {

  }

  @Override
  public void error(String msg) {

  }

  @Override
  public void warning(FilePosition position, String msg) {

  }

  @Override
  public void warning(String msg) {

  }

  @Override
  public boolean hasErrors() {
    return false;
  }
}
