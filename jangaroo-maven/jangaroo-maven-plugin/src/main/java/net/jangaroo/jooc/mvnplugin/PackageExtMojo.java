package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.CompressorImpl;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProjectHelper;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Packages the Ext JS framework with source maps
 */
@Mojo(name = "package-ext", defaultPhase = LifecyclePhase.PROCESS_RESOURCES, threadSafe = true )
public class PackageExtMojo extends AbstractMojo {

  private static final String FILE_LIST_FILE_NAME = "filenames.txt";

  @Inject
  private MavenProjectHelper helper;

  @Parameter(defaultValue = "${project.build.directory}/unzip", readonly = true)
  private File extFrameworkDir;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    getLog().info("Execute sencha package Ext mojo");
    packageExtAll();
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
    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(classicPackageDir, arguments, getLog(), null);
    senchaCmdExecutor.execute();

    getLog().info(String.format("Compressing Ext to %s", extPackageFile.getPath()));
    compressJsFiles(fileList, extPackageFile);
  }

  /**
   * Renames the overrides directory to overrides-src and concatenates all files therin to overrides/<theme>.js
   */
  private void packageTheme(String theme) throws MojoExecutionException {
    File themeDir = new File(extFrameworkDir, "classic/" + theme);
    File overridesDir = new File(themeDir, "overrides");
    File overridesSrcDir = new File(themeDir, "overrides-src");
    File packageFile = new File(overridesDir, theme + "-overrides.js");
    if (!overridesDir.renameTo(overridesSrcDir)) {
      throw new MojoExecutionException("cannot rename " + overridesDir + " to " + overridesSrcDir);
    }
    if (!overridesDir.mkdir()) {
      throw new MojoExecutionException("cannot create directory " + overridesDir);
    }
    List<File> sources = new ArrayList<>();
    scanSources(overridesSrcDir, sources);
    getLog().info(String.format("Compressing %s to %s", overridesSrcDir.getPath(), packageFile.getPath()));
    try {
      new CompressorImpl().compress(sources, packageFile);
    } catch (IOException e) {
      throw new MojoExecutionException("Exception while packaging JavaScript sources", e);
    }
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
