package com.coremedia.studio.tools.exmlconverter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Iterator;

/**
 * The main class of a converter for EXML files.
 * Used for adapting EXML files created for Studio 1.1.8 and earlier
 * to the new component naming conventions introduced later on.
 */
public class ExmlConverterTool {
  private String[] args;
  private String encoding;
  private boolean anyErrors;

  public ExmlConverterTool(String[] args) {
    this.args = args;
  }

  public static void main(String[] args) {
    new ExmlConverterTool(args).run();
  }

  void exit(int code) {
    System.exit(code);
  }

  void run() {
    if (args.length < 1 || args.length > 2) {
      System.out.println("Usage: java -jar exml-converter.jar PATH [ENCODING]");
      System.out.println("Convert EXML files created for Studio 1.1.8 or earlier to the new");
      System.out.println("component naming conventions. Before converting, please make sure");
      System.out.println("that all files to be converted compile cleanly with the original");
      System.out.println("Studio release.");
      System.out.println("");
      System.out.println("Backup files are created as *.exml.bak next to the original files.");
      System.out.println("");
      System.out.println("PATH        an EXML file to convert; if a directory is given, all");
      System.out.println("            *.exml files in that directory are converted, including");
      System.out.println("            files in subdirectories");
      System.out.println("ENCODING    the optional character encoding of the EXML files;");
      System.out.println("            defaults to UTF-8");
      exit(-1);
      // Never reached. (Except in tests.)
      return;
    }
    String path = args[0];
    encoding = args.length <= 1 ? "UTF-8" : args[1];

    File file = new File(path);
    if (!file.exists()) {
      System.out.println("The file or directory " + path + " does not exist.");
      System.out.println("Please specify an existing path.");
      exit(-2);
    }
    anyErrors = false;
    if (file.isFile()) {
      convert(file);
    } else {
      convertAll(file);
    }
    if (anyErrors) {
      System.out.println("Some files could not be processed due to errors.");
      exit(-3);
    }
  }

  private void convertAll(File directory) {
    for (Iterator<File> i = FileUtils.iterateFiles(directory, new String[]{"exml"}, true); i.hasNext();) {
      convert(i.next());
    }
  }

  private void convert(File file) {
    System.out.println("Processing " + file + "...");
    File tempFile = null;
    try {
      tempFile = File.createTempFile(file.getName() + ".", ".temp", file.getParentFile());
      File bakFile = new File(file.getPath() + ".bak");

      new FileConverter(file, tempFile, encoding).execute();

      if (moveFilesAfterConversion(file, tempFile, bakFile)) {
        anyErrors = true;
        return;
      }
    } catch (ParseException e) {
      System.out.println("... failed due to parse error: " + e.getMessage());
      anyErrors = true;
      return;
    } catch (Exception e) {
      System.out.println("... failed due to internal error!");
      e.printStackTrace();
      anyErrors = true;
      return;
    } finally {
      FileUtils.deleteQuietly(tempFile);
    }
    System.out.println("... done.");
  }

  private boolean moveFilesAfterConversion(File file, File tempFile, File bakFile) {
    FileUtils.deleteQuietly(bakFile);
    if (!file.renameTo(bakFile)) {
      System.out.println("... failed! Could not create backup file " + bakFile + ".");
      return true;
    }
    if (!tempFile.renameTo(file)) {
      System.out.println("... failed! Could not create updated file " + file + ".");
      if (!bakFile.renameTo(file)) {
        System.out.println("(And could not create restore backup file " + bakFile + " to " + file + ".");
      }
      return true;
    }
    return false;
  }
}
