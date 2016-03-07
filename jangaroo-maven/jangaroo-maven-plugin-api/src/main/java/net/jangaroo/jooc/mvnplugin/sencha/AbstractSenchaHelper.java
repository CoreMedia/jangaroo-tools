package net.jangaroo.jooc.mvnplugin.sencha;

import net.jangaroo.jooc.mvnplugin.sencha.configurer.Configurer;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
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
    File srcDir = new File(project.getBasedir() + File.separator + SenchaUtils.SENCHA_BASE_PATH + File.separator + suffix);
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

  protected void addRegisterEditorPluginsResource(String modulePath) throws MojoExecutionException {
    addRegisterEditorPluginsResource(modulePath, getSenchaConfiguration());
    if (null != getSenchaConfiguration().getProduction()) {
      addRegisterEditorPluginsResource(modulePath, getSenchaConfiguration().getProduction());
    }
    if (null != getSenchaConfiguration().getTesting()) {
      addRegisterEditorPluginsResource(modulePath, getSenchaConfiguration().getTesting());
    }
    if (null != getSenchaConfiguration().getDevelopment()) {
      addRegisterEditorPluginsResource(modulePath, getSenchaConfiguration().getDevelopment());
    }
  }

  protected void addRegisterEditorPluginsResource(String modulePath, SenchaProfileConfiguration senchaProfileConfiguration) throws MojoExecutionException {
    List<String> editorPlugins = senchaProfileConfiguration.getEditorPlugins();
    if (null != editorPlugins && !editorPlugins.isEmpty()) {
      String profileFolder = "";
      if (null != senchaProfileConfiguration.getProfileName()) {
        profileFolder = senchaProfileConfiguration.getProfileName() + SenchaUtils.SEPARATOR;
      }
      File resource = new File(modulePath + File.separator + SenchaUtils.SENCHA_RELATIVE_RESOURCES_PATH + File.separator + profileFolder + SenchaUtils.EDITOR_PLUGIN_RESOURCE_FILENAME);
      if (resource.exists()) {
        getLog().warn("resource file for editor plugins already exists, deleting...");
        if (!resource.delete()) {
          throw new MojoExecutionException("Could not delete resource file for editor plugins");
        }
      }
      PrintWriter pw = null;
      try {
        FileWriter fw = new FileWriter(resource, true);
        pw = new PrintWriter(fw);
        for (String editorPlugin : editorPlugins) {
          String editorPluginName = getPluginName(editorPlugin);
          String editorPluginCompiledClassName = "AS3." + editorPlugin;
          pw.println("Ext.require(\"" + editorPluginCompiledClassName + "\");");
          pw.println("coremediaEditorPlugins.push({\n"
                  + "name: \"" + editorPluginName + "\",\n"
                  + "mainClass: \"" + editorPlugin + "\"\n"
                  + "});");
        }
      } catch (IOException e) {
        throw new MojoExecutionException("could not append skip.sass and skip.slice to sencha config of package");
      } finally {
        if (null != pw) {
          pw.close();
        }
      }
    }
  }

  private String getPluginName(String editorPlugin) {
    String editorPluginName = "No name";
    String[] parts = editorPlugin.split("\\.");
    if (parts.length > 0) {
      editorPluginName = parts[parts.length - 1];
    }
    return editorPluginName;
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
