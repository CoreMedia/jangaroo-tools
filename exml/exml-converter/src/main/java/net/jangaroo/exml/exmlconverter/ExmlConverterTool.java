package net.jangaroo.exml.exmlconverter;

import net.jangaroo.exml.ExmlConverter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;

/**
 * A converter for EXML files.
 * Used for adapting EXML files created for Studio 1.1.8 and earlier
 * to the new component naming conventions introduced later on.
 */
public class ExmlConverterTool {
  private final Logger log = LoggerFactory.getLogger(ExmlConverterTool.class);
  
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
    log.info("Processing " + file + "...");
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
      log.error("... failed due to parse error: " + e.getMessage());
      anyErrors = true;
      return;
    } catch (Exception e) {
      log.error("... failed due to internal error!", e); 
      anyErrors = true;
      return;
    } finally {
      FileUtils.deleteQuietly(tempFile);
    }
    log.info("... done.");
  }

  private boolean moveFilesAfterConversion(File file, File tempFile, File bakFile) {
    FileUtils.deleteQuietly(bakFile);
    if (!file.renameTo(bakFile)) {
      log.error("... failed! Could not create backup file " + bakFile + ".");
      return true;
    }
    if (!tempFile.renameTo(file)) {
      log.error("... failed! Could not create updated file " + file + ".");
      if (!bakFile.renameTo(file)) {
        log.error("(And could not create restore backup file " + bakFile + " to " + file + ".");
      }
      return true;
    }
    return false;
  }
}
