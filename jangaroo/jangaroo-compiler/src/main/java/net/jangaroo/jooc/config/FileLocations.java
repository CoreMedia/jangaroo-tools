package net.jangaroo.jooc.config;

import net.jangaroo.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A description of various file paths in the build environment.
 */
public class FileLocations {
  // all paths from which source files are read; used to resolve the package of each file
  private List<File> sourcePath = new ArrayList<File>(); // may contain directories which are source roots
  // the files to compile
  private List<File> sourceFiles = new ArrayList<File>();
  // the class path (including directories and jars) from which referenced classes are loaded
  private List<File> classPath = new ArrayList<File>(); // may contain directories and jar files
  // the directory into which output files are generated
  private File outputDirectory;

  public File findSourceDir(final File file) throws IOException {
    File canonicalFile = file.getCanonicalFile();
    for (File sourceDir : getSourcePath()) {
      if (FileUtils.isParent(sourceDir, canonicalFile)) {
        return sourceDir;
      }
    }
    return null;
  }

  public File getOutputDirectory() {
    return outputDirectory;
  }

  public void setOutputDirectory(File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  public List<File> getSourcePath() {
    return sourcePath;
  }

  public void setSourcePath(final List<File> sourcePath) throws IOException {
    ArrayList<File> canonicalizedSourcePath = new ArrayList<File>();
    for (File file : sourcePath) {
      canonicalizedSourcePath.add(file.getCanonicalFile());
    }
    this.sourcePath = Collections.unmodifiableList(canonicalizedSourcePath);
  }

  public List<File> getClassPath() {
    return classPath;
  }

  public void setClassPath(final List<File> classPath) {
    assert classPath != null;
    this.classPath = Collections.unmodifiableList(classPath);
  }

  public List<File> getSourceFiles() {
    return Collections.unmodifiableList(sourceFiles);
  }

  public void setSourceFiles(List<File> sourceFiles) {
    if (sourceFiles == null) {
      throw new IllegalArgumentException("sourceFiles == null");
    }

    this.sourceFiles = new ArrayList<File>(sourceFiles);
  }

  public void addSourceFile(File source) {
    sourceFiles.add(source);
  }

  public void addSourceFile(String sourcepath) {
    addSourceFile(new File(sourcepath));
  }
}
