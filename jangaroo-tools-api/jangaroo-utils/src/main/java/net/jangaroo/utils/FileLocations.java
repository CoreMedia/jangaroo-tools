package net.jangaroo.utils;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A description of various file paths in the build environment.
 */
public class FileLocations {

  // all paths from which source files are read; used to resolve the package of each file
  private List<File> sourcePath = new ArrayList<>(); // may contain directories which are source roots
  // all paths from which sass source files are read; used to resolve the package of each file
  private Map<String, File> sassSourcePathByType = new HashMap<>(); // may contain directories which are sass source roots
  // the files to compile
  private List<File> sourceFiles = new ArrayList<>();
  // the sass files to compile
  private Map<String, List<File>> sassSourceFilesByType = new HashMap<>();
  // the class path (including directories and SWCs) from which referenced classes are loaded
  private List<File> classPath = new ArrayList<>(); // may contain directories and jar files
  // the compile path (including directories and SWCs) from which referenced classes are loaded. This is the
  // subset of the classPath that may be directly referenced by the sourcePath
  private List<File> compilePath = new ArrayList<>(); // may contain directories and jar files
  // the directory into which output files are generated
  private File outputDirectory;
  // the directory into which sass output files are generated
  private Map<String, File> sassOutputDirectoryByType;

  public File findSourceDir(final File file) throws IOException {
    return CompilerUtils.findSourceDir(getSourcePath(), file);
  }

  public File getOutputDirectory() {
    return outputDirectory;
  }

  @Option(name="-d", metaVar = "DEST_DIR", usage = "destination directory for generated files")
  public void setOutputDirectory(File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  public List<File> getSourcePath() {
    return sourcePath;
  }

  @Option(name="-sourcepath", handler = PathHandler.class, usage = "source root directories, separated by the system dependant path separator character (e.g. ':' on Unix systems, ';' on Windows)")
  public void setSourcePath(final List<File> sourcePath) throws IOException {
    ArrayList<File> canonicalizedSourcePath = new ArrayList<File>();
    for (File file : sourcePath) {
      canonicalizedSourcePath.add(file.getCanonicalFile());
    }
    this.sourcePath = Collections.unmodifiableList(canonicalizedSourcePath);
  }

  public Map<String, File> getSassSourcePathByType() {
    return sassSourcePathByType;
  }

  public void setSassSourcePathByType(Map<String, File> sassSourcePathByType) throws IOException {
    Map<String, File> canonicalizedSourcePathByType = new HashMap<>();
    for (Map.Entry<String, File> entry : sassSourcePathByType.entrySet()) {
      canonicalizedSourcePathByType.put(entry.getKey(), entry.getValue().getCanonicalFile());
    }
    this.sassSourcePathByType = Collections.unmodifiableMap(canonicalizedSourcePathByType);
  }

  public List<File> getClassPath() {
    return classPath;
  }

  @Option(name="-compilepath", handler = PathHandler.class, usage = "source root directories or jangaroo SWCs of dependent classes, separated by the system dependent path separator character (':' on Unix systems, ';' on Windows)")
  public void setCompilePath(final List<File> compilePath) {
    assert compilePath != null;
    this.compilePath = Collections.unmodifiableList(compilePath);
  }

  public List<File> getCompilePath() {
    return compilePath;
  }

  @Option(name="-classpath", handler = PathHandler.class, usage = "source root directories or jangaroo SWCs of dependent classes, separated by the system dependent path separator character (e.g. ':' on Unix systems, ';' on Windows)")
  public void setClassPath(final List<File> classPath) {
    assert classPath != null;
    this.classPath = Collections.unmodifiableList(classPath);
  }

  public List<File> getSourceFiles() {
    return Collections.unmodifiableList(sourceFiles);
  }

  @Argument(metaVar = "SOURCE_FILES", usage = "source files that should be compiled", handler = SourceFilesHandler.class, multiValued = true)
  public void setSourceFiles(List<File> sourceFiles) {
    if (sourceFiles == null) {
      throw new IllegalArgumentException("sourceFiles == null");
    }
    this.sourceFiles = new ArrayList<>(sourceFiles);
  }

  public void addSourceFile(File source) {
    sourceFiles.add(source);
  }

  public void addSourceFile(String sourcepath) {
    addSourceFile(new File(sourcepath));
  }

  public Map<String, List<File>> getSassSourceFilesByType() {
    return Collections.unmodifiableMap(sassSourceFilesByType);
  }

  public void setSassSourceFilesByType(Map<String, List<File>> sassSourceFilesByType) {
    if (sassSourceFilesByType == null) {
      throw new IllegalArgumentException("sassSourceFilesByType == null");
    }
    this.sassSourceFilesByType = new HashMap<>();
    for (Map.Entry<String, List<File>> entry : sassSourceFilesByType.entrySet()) {
      this.sassSourceFilesByType.put(entry.getKey(), new ArrayList<>(entry.getValue()));
    }
  }

  public Map<String, File> getSassOutputDirectoryByType() {
    return Collections.unmodifiableMap(sassOutputDirectoryByType);
  }

  public void setSassOutputDirectoryByType(Map<String, File> sassOutputDirectoryByType) throws IOException {
    Map<String, File> canonicalizedOutputDirectoryByType = new HashMap<>();
    for (Map.Entry<String, File> entry : sassOutputDirectoryByType.entrySet()) {
      canonicalizedOutputDirectoryByType.put(entry.getKey(), entry.getValue().getCanonicalFile());
    }
    this.sassOutputDirectoryByType = Collections.unmodifiableMap(canonicalizedOutputDirectoryByType);
  }

  @Override
  public String toString() {
    return "FileLocations{" +
            "sourcePath=" + sourcePath +
            ", sourceFiles=" + sourceFiles +
            ", classPath=" + classPath +
            ", outputDirectory=" + outputDirectory +
            '}';
  }
}
