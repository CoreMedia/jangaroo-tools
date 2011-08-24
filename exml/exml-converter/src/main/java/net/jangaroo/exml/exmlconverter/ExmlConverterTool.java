package net.jangaroo.exml.exmlconverter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;

/**
 * A converter for EXML files.
 * Used for adapting EXML files created for Studio 1.1.8 and earlier
 * to the new component naming conventions introduced later on.
 */
public class ExmlConverterTool {
  private String encoding;
  private boolean anyErrors = false;

  private File directory;
  private Properties mappings;

  public ExmlConverterTool(String encoding, File directory, Properties mappings) {
    this.encoding = encoding;
    this.directory = directory;
    this.mappings = mappings;
  }

  public boolean convertAll() {
    for (Iterator<File> i = FileUtils.iterateFiles(directory, new String[]{"exml"}, true); i.hasNext();) {
      convert(i.next());
    }
    return !anyErrors;
  }

  private void convert(File file) {
    System.out.println("Processing " + file + "..."); // NOSONAR this is a commandline tool
    File tempFile = null;
    try {
      tempFile = File.createTempFile(file.getName() + ".", ".temp", file.getParentFile());
      File bakFile = new File(file.getPath() + ".bak");

      new FileConverter(file, tempFile, encoding, mappings).execute();

      if (moveFilesAfterConversion(file, tempFile, bakFile)) {
        anyErrors = true;
        return;
      }
    } catch (ParseException e) {
      System.err.println("... failed due to parse error: " + e.getMessage()); // NOSONAR this is a commandline tool
      anyErrors = true;
      return;
    } catch (Exception e) {
      System.err.println("... failed due to internal error!"); // NOSONAR this is a commandline tool
      e.printStackTrace(); // NOSONAR this is a commandline tool
      anyErrors = true;
      return;
    } finally {
      FileUtils.deleteQuietly(tempFile);
    }
    System.out.println("... done."); // NOSONAR this is a commandline tool
  }

  private boolean moveFilesAfterConversion(File file, File tempFile, File bakFile) {
    FileUtils.deleteQuietly(bakFile);
    if (!file.renameTo(bakFile)) {
      System.err.println("... failed! Could not create backup file " + bakFile + "."); // NOSONAR this is a commandline tool
      return true;
    }
    if (!tempFile.renameTo(file)) {
      System.err.println("... failed! Could not create updated file " + file + "."); // NOSONAR this is a commandline tool
      if (!bakFile.renameTo(file)) {
        System.err.println("(And could not create restore backup file " + bakFile + " to " + file + "."); // NOSONAR this is a commandline tool
      }
      return true;
    }
    return false;
  }
}
