package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.CompressorImpl;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Packages the Ext JS framework with source maps
 */
@Mojo(name = "package-ext", defaultPhase = LifecyclePhase.PROCESS_RESOURCES, threadSafe = true )
public class PackageExtMojo extends AbstractSenchaMojo {

  private static final String FILE_LIST_FILE_NAME = "filenames.txt";

  @Parameter(defaultValue = "${project.build.directory}/unzip", readonly = true)
  private File extFrameworkDir;

  @Override
  public void execute() throws MojoExecutionException {
    getLog().info("Execute sencha package Ext mojo");
    packageExtAll();
    packagePackage("charts");
    packageTheme("theme-neptune");
    packageTheme("theme-triton");
  }

  private void packageExtAll() throws MojoExecutionException {
    File classicPackageDir = new File(extFrameworkDir, "classic/classic");
    File fileList = new File(classicPackageDir, FILE_LIST_FILE_NAME);
    File extBuildDirectory = new File(extFrameworkDir, "build");
    File extPackageFile = new File(extBuildDirectory, "ext-all-rtl-debug-sourcemap.js");

    String arguments = "compile meta"
            + " -filenames"
            + " -tpl \"{0}\" "
            + " -out " + FILE_LIST_FILE_NAME;
    getLog().info("Generating Ext classic file list");
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(classicPackageDir, arguments, getSenchaJvmArgs(), getLog(), null);
    senchaCmdExecutor.execute();

    getLog().info(String.format("Compressing Ext to %s", extPackageFile.getPath()));
    compressJsFiles(fileList, extPackageFile);
  }

  private void packagePackage(String pkg) throws MojoExecutionException {
    File packageDir = new File(extFrameworkDir, "packages/" + pkg);
    File classicDir = new File(packageDir, "classic");
    File packageFile = new File(classicDir, "src/ext-" + pkg + "-sourcemap.js");
    File sourceMapDir = new File(packageDir, "sourcemap");
    try {
      moveToSourceMapDir(packageDir, sourceMapDir);
      moveToSourceMapDir(classicDir, new File(sourceMapDir, "classic"));
    } catch (IOException e) {
      throw new MojoExecutionException("cannot move Ext source files to " + sourceMapDir, e);
    }
    List<File> sources = findAndCompressSources(sourceMapDir, packageFile);
    addDefines(packageFile.toPath(), sources);
  }

  private List<File> findAndCompressSources(File sourceMapDir, File packageFile) throws MojoExecutionException {
    List<File> sources = new ArrayList<>();
    scanSources(sourceMapDir, sources);
    packageFile.getParentFile().mkdirs();
    getLog().info(String.format("Compressing %s to %s", sourceMapDir.getPath(), packageFile.getPath()));
    try {
      new CompressorImpl().compress(sources, packageFile);
    } catch (IOException e) {
      throw new MojoExecutionException("Exception while packaging JavaScript sources.", e);
    }
    return sources;
  }

  private void moveToSourceMapDir(File srcRootDir, File sourceMapDir) throws IOException {
    sourceMapDir.mkdirs();
    moveSubDir(srcRootDir, sourceMapDir, "src");
    moveSubDir(srcRootDir, sourceMapDir, "overrides");
  }

  private void moveSubDir(File srcRootDir, File sourceMapDir, String src) throws IOException {
    File srcDir = new File(srcRootDir, src);
    if (srcDir.exists()) {
      Files.move(srcDir.toPath(), new File(sourceMapDir, src).toPath());
    }
  }

  private void addDefines(Path packageFilePath, List<File> sources) throws MojoExecutionException {
    List<String> defines = new ArrayList<>();
    for (File srcFile : sources) {
      try (BufferedReader reader = new BufferedReader(new FileReader(srcFile))) {
        List<String> fileDefines = reader.lines().filter(line -> line.startsWith("// @define")).collect(Collectors.toList());
        defines.addAll(fileDefines);
      } catch (IOException e) {
        throw new MojoExecutionException("Exception while scanning source file " + srcFile.getPath() + " for @define.", e);
      }
    }
    if (!defines.isEmpty()) {
      try {
        // insert defines before last line to keep //#sourceMappingURL
        List<String> lines = Files.lines(packageFilePath).collect(Collectors.toCollection(ArrayList::new));
        lines.addAll(lines.size() - 1, defines);
        Files.write(packageFilePath, lines);
      } catch (IOException e) {
        throw new MojoExecutionException("Exception while adding @defines to minified package file.", e);
      }
    }
  }

  /**
   * Renames the overrides directory to overrides-src and concatenates all files therin to overrides/<theme>.js
   */
  private void packageTheme(String theme) throws MojoExecutionException {
    File themeDir = new File(extFrameworkDir, "classic/" + theme);
    File overridesDir = new File(themeDir, "overrides");
    File packageFile = new File(overridesDir, theme + "-overrides.js");
    File sourceMapDir = new File(themeDir, "sourcemap");
    try {
      moveToSourceMapDir(themeDir, sourceMapDir);
    } catch (IOException e) {
      throw new MojoExecutionException("cannot move Ext theme sources " + themeDir + " to " + sourceMapDir, e);
    }
    findAndCompressSources(sourceMapDir, packageFile);
  }

  private void compressJsFiles(File fileList, File packageFile) throws MojoExecutionException {
    try {
      new CompressorImpl().compressFileList(fileList, packageFile);
    } catch (IOException e) {
      throw new MojoExecutionException("Exception while packaging JavaScript sources", e);
    }
  }

  private void scanSources(File dir, List<File> sources) {
    File[] children = dir.listFiles();
    if (children != null) {
      for (File child : children) {
        if (child.isDirectory()) {
          scanSources(child, sources);
        } else if (child.getName().endsWith(".js")) {
          sources.add(child);
        }
      }
    }
  }

}
