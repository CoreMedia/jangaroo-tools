package net.jangaroo.jooc;

import java.io.IOException;
import java.io.InputStream;

public interface InputSource {

  String getName();
  String getRelativePath();
  InputStream getInputStream() throws IOException;
  void close() throws IOException;
  char getFileSeparatorChar();
}
