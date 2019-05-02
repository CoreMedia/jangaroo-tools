package net.jangaroo.jooc;

import net.jangaroo.jooc.api.Compressor;
import net.jangaroo.jooc.api.Packager;
import net.jangaroo.jooc.json.JsonArray;
import net.jangaroo.utils.CompilerUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

public class PackagerImpl implements Packager {

  private static final String DEFAULT_LOCALE = "en";
  private Compressor compressor = new CompressorImpl();

  @Override
  public void doPackage(File sourceDirectory, File overridesDirectory, File localizedOverridesDirectory, File outputDirectory, String outputFilePrefix) throws IOException {

    // package locale-independent sources, plus the sources for the default locale 'en'
    pack(outputDirectory, packageJsFileName(outputFilePrefix),
            sourceDirectory, new File(localizedOverridesDirectory, DEFAULT_LOCALE));

    // package locale-independent overrides
    pack(outputDirectory, overridesJsFilename(outputFilePrefix), overridesDirectory);

    // package locale-specific sources, one file for each non-default locale
    // at this time, the Jangaroo compiler has already written any locale-specific JavaScript into ${package.dir}/locale
    File[] children = localizedOverridesDirectory.listFiles();
    if (children != null) {
      for (File child : children) {
        final String locale = child.getName();
        // the default locale source directory locale/en does not contain any overrides,
        // it contains just classes that have been included in the package js file above
        if (child.isDirectory() && !DEFAULT_LOCALE.equals(locale)) {
          pack(outputDirectory, overridesJsFilename(outputFilePrefix, locale), child);
        }
      }
    }
  }

  private void pack(File outputDirectory, String outputFileName, File... sourceDirectories) throws IOException {
    File outputFile = new File(outputDirectory, outputFileName);
    ArrayList<File> sources = new ArrayList<>();
    ArrayList<String> sourceClasses = new ArrayList<>();
    long timestamp = outputFile.lastModified();
    boolean somethingChanged = false;
    for (File dir : sourceDirectories) {
      somethingChanged |= scanSources(dir, dir, timestamp, sources, sourceClasses);
    }

    final String outputFilePath = outputFile.getAbsolutePath();
    if (somethingChanged && !sources.isEmpty()) {
      System.out.println(String.format("Packing %d js files into %s", sources.size(), outputFilePath));
      //pack(sources, outputFile);

      File inventoryFile = new File(outputDirectory, CompilerUtils.removeExtension(outputFileName) + ".json");
      PrintWriter printWriter = new PrintWriter(inventoryFile);
      printWriter.println(new JsonArray(sourceClasses.toArray()).toString());
      printWriter.close();

    } else if (sources.isEmpty()) {
      if (outputFile.exists()) {
        // sources have been deleted since the previous build
        System.out.println(String.format("No js files found, removing %s", outputFilePath));
        if (!outputFile.delete()) {
          throw new IOException(String.format("failed to delete %s", outputFilePath));
        }
      } else {
        System.out.println(String.format("No js files found, skipping generation of %s", outputFile.getAbsolutePath()));
      }
    } else {
      System.out.println(String.format("Nothing changed, keeping %s", outputFilePath));
    }
  }

  private boolean scanSources(File baseDir, File dir, long timestamp, ArrayList<File> sources, ArrayList<String> sourceClasses) {
    // some js file might have been deleted, check directory timestamps as well
    boolean somethingChanged = dir.lastModified() > timestamp;
    File[] children = dir.listFiles();
    if (children != null) {
      for (File child : children) {
        if (child.isDirectory()) {
          somethingChanged |= scanSources(baseDir, child, timestamp, sources, sourceClasses);
        } else if (child.getName().endsWith(Jooc.OUTPUT_FILE_SUFFIX)) {
          sources.add(child);
          sourceClasses.add(CompilerUtils.qNameFromFile(baseDir, child));
          somethingChanged |= child.lastModified() > timestamp;
        }
      }
    }
    return somethingChanged;
  }

  private void pack(Collection<File> inputFiles, File output) throws IOException {
    compressor.compress(inputFiles, output);
  }

  private String packageJsFileName(String prefix) {
    return packageJsFileName(prefix, "");
  }

  private String packageJsFileName(String prefix, String suffix) {
    return String.format("%s%s.js", prefix, suffix);
  }

  private String overridesJsFilename(String prefix, String locale) {
    return packageJsFileName(prefix, "-overrides" + (locale.isEmpty() ? "" : "-") + locale);
  }

  private String overridesJsFilename(String prefix) {
    return overridesJsFilename(prefix, "");
  }

}
