package net.jangaroo.jooc;

import com.google.common.collect.ImmutableList;
import net.jangaroo.jooc.api.Compressor;
import net.jangaroo.jooc.api.Packager;
import net.jangaroo.jooc.api.Packager2;
import net.jangaroo.jooc.json.JsonArray;
import net.jangaroo.utils.CompilerUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PackagerImpl implements Packager, Packager2 {

  private static final String DEFAULT_LOCALE = "en";
  private final Compressor compressor = new CompressorImpl();

  @Override
  public void doPackage(File sourceDirectory, File overridesDirectory, File localizedOverridesDirectory,
                        File outputDirectory, String outputFilePrefix) throws IOException {
    doPackage2("",
            sourceDirectory, overridesDirectory, localizedOverridesDirectory,
            outputDirectory, outputFilePrefix);
  }

  @Override
  public void doPackage2(String extNamespace,
                         File sourceDirectory, File overridesDirectory, File localizedOverridesDirectory,
                         File outputDirectory, String outputFilePrefix) throws IOException {

    // package locale-independent sources, plus the sources for the default locale 'en'
    pack(extNamespace, outputDirectory, packageJsFileName(outputFilePrefix),
            sourceDirectory, new File(localizedOverridesDirectory, DEFAULT_LOCALE));

    // package locale-independent overrides
    pack(extNamespace, outputDirectory, overridesJsFilename(outputFilePrefix), overridesDirectory);

    // package locale-specific sources, one file for each non-default locale
    // at this time, the Jangaroo compiler has already written any locale-specific JavaScript into ${package.dir}/locale
    File[] children = localizedOverridesDirectory.listFiles();
    if (children != null) {
      for (File child : children) {
        final String locale = child.getName();
        // the default locale source directory locale/en does not contain any overrides,
        // it contains just classes that have been included in the package js file above
        if (child.isDirectory() && !DEFAULT_LOCALE.equals(locale)) {
          pack(extNamespace, outputDirectory, overridesJsFilename(outputFilePrefix, locale), child);
        }
      }
    }
  }

  private void pack(String extNamespace,
                    File outputDirectory, String outputFileName, File... sourceDirectories) throws IOException {
    File outputFile = new File(outputDirectory, outputFileName);
    ArrayList<File> sources = new ArrayList<>();
    ArrayList<String> sourceClasses = new ArrayList<>();
    long timestamp = outputFile.lastModified();
    boolean somethingChanged = false;
    for (File dir : sourceDirectories) {
      somethingChanged |= scanSources(extNamespace, dir, dir, timestamp, sources, sourceClasses);
    }

    final String outputFilePath = outputFile.getAbsolutePath();
    if (somethingChanged && !sources.isEmpty()) {
      System.out.printf("Packing %d js files into %s%n", sources.size(), outputFilePath);
      String initName = (StringUtils.isNotEmpty(extNamespace) ? extNamespace + "." : "") + "init";
      if (sourceClasses.contains(initName)) {
        sources.add(createAutoLoad(outputDirectory, ImmutableList.of(initName)));
      }
      pack(sources, outputFile);

      File inventoryFile = new File(outputDirectory, CompilerUtils.removeExtension(outputFileName) + ".json");
      PrintWriter printWriter = new PrintWriter(inventoryFile);
      printWriter.println(new JsonArray(sourceClasses.toArray()).toString());
      printWriter.close();

    } else if (sources.isEmpty()) {
      if (outputFile.exists()) {
        // sources have been deleted since the previous build
        System.out.printf("No js files found, removing %s%n", outputFilePath);
        if (!outputFile.delete()) {
          throw new IOException(String.format("failed to delete %s", outputFilePath));
        }
      } else {
        System.out.printf("No js files found, skipping generation of %s%n", outputFile.getAbsolutePath());
      }
    } else {
      System.out.printf("Nothing changed, keeping %s%n", outputFilePath);
    }
  }

  private boolean scanSources(String extNamespace,
                              File baseDir, File dir, long timestamp, ArrayList<File> sources, ArrayList<String> sourceClasses) {
    // some js file might have been deleted, check directory timestamps as well
    boolean somethingChanged = dir.lastModified() > timestamp;
    File[] children = dir.listFiles();
    if (children != null) {
      for (File child : children) {
        if (child.isDirectory()) {
          somethingChanged |= scanSources(extNamespace, baseDir, child, timestamp, sources, sourceClasses);
        } else if (child.getName().endsWith(Jooc.OUTPUT_FILE_SUFFIX)) {
          sources.add(child);
          sourceClasses.add(CompilerUtils.qName(extNamespace, CompilerUtils.qNameFromFile(baseDir, child)));
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

  private static File createAutoLoad(File outputDirectory, List<String> autoLoad) throws IOException {
    File autoLoadFile = new File(outputDirectory, "autoLoad.js");
    try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(autoLoadFile), StandardCharsets.UTF_8)) {
      for (String item : autoLoad) {
        writer.write(item + "();\n");
      }
    }
    return autoLoadFile;
  }
}
