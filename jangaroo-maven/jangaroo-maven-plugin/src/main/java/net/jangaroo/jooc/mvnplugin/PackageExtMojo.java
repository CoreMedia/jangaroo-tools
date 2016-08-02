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
    try {
      new CompressorImpl().compressFileList(fileList, extPackageFile);
    } catch (IOException e) {
      throw new MojoExecutionException("exception while packaging JavaScript sources", e);
    }
  }
}
