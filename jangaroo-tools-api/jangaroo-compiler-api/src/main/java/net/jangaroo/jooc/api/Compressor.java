package net.jangaroo.jooc.api;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public interface Compressor {

  /**
   * Compress the given JavaScript source files and write the result to the given output file.
   *
   * En passant, create a source map besides the output file with the same name and suffix '.map'.
   * A matching source map declaration comment for browsers is appended to the output file.
   */
  void compress(Collection<File> sources, File output) throws IOException;

  /**
   * Same as {@link #compress(Collection, File)}, but the list of input file paths is read from
   * a file, line by line.
   *
   * <p>Paths within fileList are interpreted relative to the directory in which the fileList resides.
   */
  void compressFileList(File fileList, File output) throws IOException;
}
