package net.jangaroo.jooc.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface InputSource {

  /**
   * The file name, i.e. test.as
   *
   * @return the file name
   */
  String getName();

  String getPath();

  String getRelativePath();

  boolean isDirectory();

  List<? extends InputSource> list();

  InputSource getChild(String path);

  List<InputSource> getChildren(String path);

  InputStream getInputStream() throws IOException;

  void close() throws IOException;

  char getFileSeparatorChar();

  InputSource getParent();

  boolean isInSourcePath();
}
