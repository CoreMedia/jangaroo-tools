package net.jangaroo.jooc.mvnplugin.sencha;

import net.jangaroo.jooc.mvnplugin.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.Configurer;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A set of helper functions for handling sencha package structure.
 */
abstract class AbstractSenchaHelper implements SenchaHelper {

  private final MavenProject project;
  private final SenchaConfiguration senchaConfiguration;
  private final Log log;

  private final String senchaModuleName;

  public AbstractSenchaHelper(MavenProject project, SenchaConfiguration senchaConfiguration, Log log) {
    this.project = project;
    this.senchaConfiguration = senchaConfiguration;
    this.log = log;

    this.senchaModuleName = SenchaUtils.getSenchaPackageNameForMavenProject(project);
  }

  private void copyFilesFromSrc(String path, String suffix) throws MojoExecutionException {
    File srcDir = new File(project.getBasedir() + File.separator + senchaModuleName + File.separator + suffix);
    File senchaDir = new File(path + File.separator + suffix);
    if (srcDir.exists()) {
      try {
        FileUtils.copyDirectory(srcDir, senchaDir);
      } catch (IOException e) {
        throw new MojoExecutionException("could not copy " + suffix, e);
      }
    }
  }

  protected void copyFilesFromSrc(String path) throws MojoExecutionException {
    copyFilesFromSrc(path, SenchaUtils.SENCHA_RELATIVE_RESOURCES_PATH);
    copyFilesFromSrc(path, SenchaUtils.SENCHA_RELATIVE_OVERRIDES_PATH);
    copyFilesFromSrc(path, SenchaUtils.SENCHA_RELATIVE_RESOURCES_PATH);
    copyFilesFromSrc(path, SenchaUtils.SENCHA_RELATIVE_SASS_PATH);
    copyFilesFromSrc(path, SenchaUtils.SENCHA_RELATIVE_CLASSIC_PATH);
    copyFilesFromSrc(path, SenchaUtils.SENCHA_RELATIVE_MODERN_PATH);
    copyFilesFromSrc(path, SenchaUtils.SENCHA_RELATIVE_PRODUCTION_PATH);
    copyFilesFromSrc(path, SenchaUtils.SENCHA_RELATIVE_TESTING_PATH);
    copyFilesFromSrc(path, SenchaUtils.SENCHA_RELATIVE_DEVELOPMENT_PATH);
  }

  private void copyFilesFromJoo(String path) throws MojoExecutionException {
    File jangarooResourcesDir = new File(project.getBuild().getDirectory() + "/classes/META-INF/resources");
    File senchaResourcesDir = new File(path + File.separator + SenchaUtils.SENCHA_RELATIVE_RESOURCES_PATH);
    if (jangarooResourcesDir.exists()) {
      try {
        FileUtils.copyDirectory(jangarooResourcesDir, senchaResourcesDir);
      } catch (IOException e) {
        throw new MojoExecutionException("could not copy resources", e);
      }
    }

    File jangarooClassDir = new File(senchaResourcesDir.getAbsolutePath() + File.separator + "joo/classes");
    if (jangarooClassDir.exists()) {
      File senchaClassDir = new File(path + File.separator + SenchaUtils.SENCHA_RELATIVE_CLASS_PATH);
      try {
        // FileUtils.move fails if directory already exists
        FileUtils.copyDirectory(jangarooClassDir, senchaClassDir);
        FileUtils.deleteDirectory(jangarooClassDir);
      } catch (IOException e) {
        throw new MojoExecutionException("could not copy classes", e);
      }
    }

    File jangarooOverridesDir = new File(senchaResourcesDir.getAbsolutePath() + File.separator + "joo/overrides");
    if (jangarooOverridesDir.exists()) {
      File senchaOverridesDir = new File(path + File.separator + SenchaUtils.SENCHA_RELATIVE_OVERRIDES_PATH);
      try {
        // FileUtils.move fails if directory already exists
        FileUtils.copyDirectory(jangarooOverridesDir, senchaOverridesDir);
        FileUtils.deleteDirectory(jangarooOverridesDir);
      } catch (IOException e) {
        throw new MojoExecutionException("could not copy overrides", e);
      }
    }
  }

  protected void copyFiles(String path) throws MojoExecutionException {
    copyFilesFromSrc(path);
    copyFilesFromJoo(path);
  }

  protected MavenProject getProject() {
    return project;
  }

  protected SenchaConfiguration getSenchaConfiguration() {
    return senchaConfiguration;
  }

  protected Log getLog() {
    return log;
  }

  protected String getSenchaModuleName() {
    return senchaModuleName;
  }

  protected Map<String, Object> getConfig(Configurer[] configurers) throws MojoExecutionException {
    Map<String, Object> config = new LinkedHashMap<String, Object>();

    for (Configurer configurer : configurers) {
      configurer.configure(config);
    }

    return config;
  }

}
