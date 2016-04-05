package net.jangaroo.jooc;

import net.jangaroo.jooc.api.FilePosition;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Factory;

class FilePositionMatcher extends CustomTypeSafeMatcher<FilePosition> {

  private final int line;
  private final int column;

  FilePositionMatcher(int line, int column) {
    super(String.format("file posisition %s:%s", line, column));
    this.line = line;
    this.column = column;
  }

  @Override
  protected boolean matchesSafely(FilePosition filePosition) {
    return filePosition.getLine() == line && filePosition.getColumn() == column;
  }

  @Factory
  static FilePositionMatcher matchesPosition(int line, int column) {
    return new FilePositionMatcher(line, column);
  }
}
