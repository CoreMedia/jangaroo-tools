package net.jangaroo.exml.exmlconverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * Converts a single EXML file.
 */
public class FileConverter extends Converter {
  private File source;
  private File target;
  private String encoding;

  public FileConverter(File source, File target, String encoding) {
    this.source = source;
    this.target = target;
    this.encoding = encoding;
  }

  @Override
  protected Writer createWriter() throws UnsupportedEncodingException, FileNotFoundException {
    return new OutputStreamWriter(new FileOutputStream(target), encoding);
  }

  @Override
  protected Reader createReader() throws UnsupportedEncodingException, FileNotFoundException {
    return new InputStreamReader(new FileInputStream(source), encoding);
  }
}
