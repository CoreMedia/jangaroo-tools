package net.jangaroo.dependencies;

import com.google.common.base.Charsets;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CyclicRequiredDependencies {

  public static final String PATH_PREFIX = "/target/classes/META-INF/resources/joo/classes/";
  public static final String PATH_SUFFIX = ".js";
  public static final String JANGAROO_PART_MARKER = "============================================== Jangaroo part ==============================================*/";
  public static final String REQUIRES_MARKER = "      requires: [";
  public static final String REQUIRES_PREFIX = "        \"AS3.";

  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      System.out.println("Usage: java -jar ... <BASEDIR> <OUTFILE>");
    } else {
      String baseDir = args[0];
      Collection<File> files = new ArrayList<>();
      fillInCompiledClassFiles(new File(baseDir), files);

      Multimap<String, String> requires = HashMultimap.create();
      Set<String> staticallyInitialized = new HashSet<>();
      for (File file : files) {
        analyzeFile(file, requires, staticallyInitialized);
      }

      File outFile = new File(args[1]);
      DependencyGraphFile.writeDependencyFile(requires, requires.keySet(), staticallyInitialized, outFile);
    }
  }

  private static void fillInCompiledClassFiles(File baseDir, Collection<File> files) {
    for (File file : baseDir.listFiles()) {
      if (file.isDirectory()) {
        fillInCompiledClassFiles(file, files);
      } else {
        String path = file.getPath();
        if (path.contains(PATH_PREFIX) && path.endsWith(PATH_SUFFIX)) {
          files.add(file);
        }
      }
    }
  }

  private static void analyzeFile(File file, Multimap<String, String> requires, Collection<String> staticallyInitialized) throws IOException {
    String path = file.getPath();
    String className = path
            .substring(path.indexOf(PATH_PREFIX) + PATH_PREFIX.length(), path.length() - PATH_SUFFIX.length())
            .replace('/', '.');
    List<String> lines = Files.readLines(file, Charsets.UTF_8);

    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      if (line.contains("HAS_STATIC_CODE")) {
        staticallyInitialized.add(className);
      }
    }

    int jangarooPartStart = lines.indexOf(JANGAROO_PART_MARKER);
    if (jangarooPartStart == -1) {
      return;
    }
    int requiresStart = lines.subList(jangarooPartStart, lines.size()).indexOf(REQUIRES_MARKER);
    if (requiresStart == -1) {
      return;
    }
    int pos = jangarooPartStart + requiresStart + 1;
    while (pos < lines.size()) {
      String line = lines.get(pos);
      if (!line.startsWith(REQUIRES_PREFIX)) {
        break;
      }
      int quotePos = line.indexOf('"', REQUIRES_PREFIX.length());
      String requiredClassName = line.substring(REQUIRES_PREFIX.length(), quotePos);
      if (!className.equals(requiredClassName)) {
        requires.put(className, requiredClassName);
      }
      pos++;
    }
  }
}
