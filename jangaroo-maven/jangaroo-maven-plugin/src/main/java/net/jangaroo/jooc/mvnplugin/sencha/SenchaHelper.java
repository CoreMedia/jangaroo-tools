package net.jangaroo.jooc.mvnplugin.sencha;

import net.jangaroo.jooc.mvnplugin.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.Configurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.DefaultSenchaApplicationConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.DefaultSenchaPackageConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.DefaultSenchaWorkspaceConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.LocalPackagesConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.MetadataConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.PathConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.RequiresConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.SenchaConfigurationConfigurer;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * A set of helper functions for handling sencha package structure.
 */
public class SenchaHelper {

  private final Configurer[] workspaceConfigurers;
  private final Configurer[] packageConfigurers;
  private final Configurer[] appConfigurers;

  private final MavenProject project;
  private final SenchaConfiguration senchaConfiguration;
  private final Log log;

  private final String senchaPackageName;

  private final String senchaPath;
  private final String senchaPackagePath;

  public SenchaHelper(MavenProject project, SenchaConfiguration senchaConfiguration, Log log) {
    this.project = project;
    this.senchaConfiguration = senchaConfiguration;
    this.log = log;

    this.senchaPackageName = SenchaUtils.getSenchaPackageNameForMavenProject(project);

    String buildDirectory = project.getBuild().getDirectory();
    this.senchaPath = buildDirectory + File.separator + SenchaUtils.SENCHA_BASE_PATH;
    this.senchaPackagePath = senchaPath + File.separator + "packages" + File.separator + senchaPackageName;

    MetadataConfigurer metadataConfigurer = new MetadataConfigurer(project);
    RequiresConfigurer requiresConfigurer = new RequiresConfigurer(project);
    SenchaConfigurationConfigurer senchaConfigurationConfigurer = new SenchaConfigurationConfigurer(senchaConfiguration);
    PathConfigurer pathConfigurer = new PathConfigurer(senchaConfiguration);
    LocalPackagesConfigurer localPackagesConfigurer = new LocalPackagesConfigurer(project, senchaConfiguration);

    this.workspaceConfigurers = new Configurer[] {
            DefaultSenchaWorkspaceConfigurer.getInstance(),
            pathConfigurer,
            localPackagesConfigurer
    };
    this.packageConfigurers = new Configurer[] {
            DefaultSenchaPackageConfigurer.getInstance(),
            metadataConfigurer,
            requiresConfigurer,
            senchaConfigurationConfigurer,
            pathConfigurer
    };
    this.appConfigurers = new Configurer[] {
            DefaultSenchaApplicationConfigurer.getInstance(),
            metadataConfigurer,
            requiresConfigurer,
            senchaConfigurationConfigurer,
            pathConfigurer
    };
  }

  public void prepareSenchaModule() throws MojoExecutionException {
    if (senchaConfiguration.isEnabled()) {

      if (senchaConfiguration.getType() == SenchaConfiguration.Type.WORKSPACE) {
        prepareSenchaWorkspace();
      }

      if (senchaConfiguration.getType() == SenchaConfiguration.Type.CODE) {
        prepareSenchaPackage();
      }

      if (senchaConfiguration.getType() == SenchaConfiguration.Type.APP) {
        prepareSenchaApp();
      }
    }
  }

  private void prepareSenchaWorkspace() throws MojoExecutionException {
    File workingDirectory = project.getBasedir();

    if (null == SenchaUtils.findClosestSenchaWorkspaceDir(workingDirectory.getParentFile())) {
      writeWorkspaceJson(workingDirectory);
    } else {
      getLog().info("Skipping preparation of workspace because there already is a workspace in the directory hierarchy");
    }
  }

  private void prepareSenchaPackage() throws MojoExecutionException {
    File senchaPackageDirectory = new File(senchaPackagePath);

    if (!senchaPackageDirectory.exists()) {
      getLog().info("generating sencha package into: " + senchaPackageDirectory.getPath());
      getLog().debug("created " + senchaPackageDirectory.mkdirs());
    }

    copyFilesFromJoo(senchaPackagePath);

    File workingDirectory = new File(senchaPackagePath);

    writePackageJson(workingDirectory);
  }

  private void prepareSenchaApp() throws MojoExecutionException {
    File senchaDirectory = new File(senchaPath);

    if (!senchaDirectory.exists()) {
      getLog().info("generating sencha into: " + senchaDirectory.getPath());
      getLog().debug("created " + senchaDirectory.mkdirs());
    }

    copyFilesFromJoo(senchaPath);

    File workingDirectory = new File(senchaPath);

    writeAppJson(workingDirectory);
  }

  public void generateSenchaModule() throws MojoExecutionException {
    if (senchaConfiguration.isEnabled()) {
      if (SenchaConfiguration.Type.WORKSPACE.equals(senchaConfiguration.getType())) {
        generateSenchaWorkspace();
      }
      if (SenchaConfiguration.Type.CODE.equals(senchaConfiguration.getType())) {
        generateSenchaCodePackage();
      }
      if (SenchaConfiguration.Type.APP.equals(senchaConfiguration.getType())) {
        generateSenchaApp();
      }
    }
  }

  public void packageSenchaModule(JarArchiver archiver) throws MojoExecutionException, ArchiverException {
    if (senchaConfiguration.isEnabled()) {
      if (senchaConfiguration.getType() == SenchaConfiguration.Type.CODE) {
        packageSenchaPackage(archiver);
      }
    }
  }

  private void packageSenchaPackage(JarArchiver archiver) throws MojoExecutionException, ArchiverException {
    File senchaPackageDirectory = new File(senchaPackagePath);

    if (!senchaPackageDirectory.exists()) {
      throw new MojoExecutionException("sencha package directory does not exist: " + senchaPackageDirectory.getPath());
    }

    buildSenchaPackage(senchaPackageDirectory); // move to packageSenchaModule

    File workspaceDir = SenchaUtils.findClosestSenchaWorkspaceDir(project.getFile().getParentFile());

    if (null == workspaceDir) {
      throw new MojoExecutionException("could not find sencha workspace directory");
    }

    // Read workspace.json
    String workspaceOutputPath = workspaceDir.getAbsolutePath() + File.separator + senchaConfiguration.getBuildDir();
    try {
      @SuppressWarnings("unchecked") Map<String, Object> workspaceConfig = (Map<String, Object>) SenchaUtils.getObjectMapper().readValue(new File(workspaceDir.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_WORKSPACE_FILENAME), Map.class);

      // check if custom workspace dir has been set
      Object build = workspaceConfig.get(PathConfigurer.BUILD);
      if (build instanceof Map) {
        build = ((Map) build).get(PathConfigurer.DIR);
      }
      if (build instanceof String) {
        workspaceOutputPath = ((String) build).replace(SenchaUtils.PLACEHOLDERS.get(SenchaConfiguration.Type.WORKSPACE), workspaceDir.getAbsolutePath());
      }
    } catch (IOException e) {
      throw new MojoExecutionException("could not read " + SenchaUtils.SENCHA_WORKSPACE_FILENAME, e);
    }

    File pkg = new File(workspaceOutputPath + File.separator + senchaPackageName + File.separator + senchaPackageName + ".pkg");
    if (!pkg.exists()) {
      throw new MojoExecutionException("could not find pkg for sencha package " + senchaPackageName);
    }
    archiver.addFile(pkg, pkg.getName());
  }

  private void copyFilesFromJoo(String path) throws MojoExecutionException {
    File jangarooResourcesDir = new File(project.getBuild().getDirectory() + "/classes/META-INF/resources");
    File senchaResourcesDir = new File(path + File.separator + SenchaUtils.SENCHA_RELATIVE_RESOURCES_PATH);
    if (jangarooResourcesDir.exists()) {
      if (senchaResourcesDir.exists()) {
        try {
          FileUtils.deleteDirectory(senchaResourcesDir);
        } catch (IOException e) {
          throw new MojoExecutionException("could not clean resources folder in sencha package", e);
        }
      }
      try {
        FileUtils.copyDirectory(jangarooResourcesDir, senchaResourcesDir);
      } catch (IOException e) {
        throw new MojoExecutionException("could not copy classes", e);
      }
    }

    File jangarooClassDir = new File(senchaResourcesDir.getAbsolutePath() + File.separator + "joo/classes");
    if (jangarooClassDir.exists()) {
      File senchaClassDir = new File(path + File.separator + SenchaUtils.SENCHA_RELATIVE_CLASS_PATH);
      if (senchaClassDir.exists()) {
        try {
          FileUtils.deleteDirectory(senchaClassDir);
        } catch (IOException e) {
          throw new MojoExecutionException("could not clean class folder in sencha package", e);
        }
      }
      try {
        FileUtils.moveDirectory(jangarooClassDir, senchaClassDir);
      } catch (IOException e) {
        throw new MojoExecutionException("could not copy classes", e);
      }
    }

    File jangarooOverridesDir = new File(senchaResourcesDir.getAbsolutePath() + File.separator + "joo/overrides");
    if (jangarooOverridesDir.exists()) {
      File senchaOverridesDir = new File(path + File.separator + SenchaUtils.SENCHA_RELATIVE_OVERRIDES_PATH);
      if (senchaOverridesDir.exists()) {
        try {
          FileUtils.deleteDirectory(senchaOverridesDir);
        } catch (IOException e) {
          throw new MojoExecutionException("could not clean overrides folder in sencha package", e);
        }
      }
      try {
        FileUtils.moveDirectory(jangarooOverridesDir, senchaOverridesDir);
      } catch (IOException e) {
        throw new MojoExecutionException("could not copy overrides", e);
      }
    }
  }

  private Log getLog() {
    return log;
  }

  private void generateSenchaWorkspace() throws MojoExecutionException {
    File workingDirectory = project.getBasedir();

    if (null == SenchaUtils.findClosestSenchaWorkspaceDir(workingDirectory.getParentFile())) {

      String line = "sencha generate workspace .";
      CommandLine cmdLine = CommandLine.parse(line);
      DefaultExecutor executor = new DefaultExecutor();
      executor.setWorkingDirectory(workingDirectory);
      executor.setExitValue(0);
      try {
        executor.execute(cmdLine);
      } catch (IOException e) {
        throw new MojoExecutionException("could not execute sencha cmd to generate workspace", e);
      }
    } else {
      getLog().info("Skipping generate workspace because there already is a workspace in the directory hierarchy");
    }
  }

  private void generateSenchaCodePackage() throws MojoExecutionException {

    File workingDirectory = new File(senchaPackagePath);

    String pathToWorkingDirectory = getPathToWorkingDir(workingDirectory);

    File senchaCfg = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_DIRECTORYNAME + "/package/sencha.cfg");
    // make sure senchaCfg does not exist
    if (senchaCfg.exists()) {
      if (!senchaCfg.delete()) {
        throw new MojoExecutionException("could not delete sencha.cfg for package");
      }
    }

    String line = "sencha generate package"
            + " --name=\"" + senchaPackageName + "\""
            + " --namespace=\"\""
            + " --type=\"code\""
            + " " + pathToWorkingDirectory;
    CommandLine cmdLine = CommandLine.parse(line);
    DefaultExecutor executor = new DefaultExecutor();
    executor.setWorkingDirectory(workingDirectory);
    executor.setExitValue(0);
    try {
      executor.execute(cmdLine);
    } catch (IOException e) {
      throw new MojoExecutionException("could not execute sencha cmd to generate package", e);
    }

    // sencha.cfg should be recreated
    // for normal packages skip generating css and slices
    if (senchaCfg.exists()) {
      PrintWriter pw = null;
      try {
        FileWriter fw = new FileWriter(senchaCfg.getAbsoluteFile(), true);
        pw = new PrintWriter(fw);
        pw.println("skip.sass=1");
        pw.println("skip.slice=1");
      } catch (IOException e) {
        throw new MojoExecutionException("could not append skip.sass and skip.slice to sencha config of package");
      }
      finally {
        if (null != pw) {
          pw.close();
        }
      }
    } else {
      throw new MojoExecutionException("could not find sencha.cfg of package");
    }
  }

  private void generateSenchaApp() throws MojoExecutionException {

    File workingDirectory = new File(senchaPath);

    String line = "sencha generate app"
            + " -ext"
            + " -" + senchaConfiguration.getToolkit()
            + " --theme-name=\"" + senchaConfiguration.getTheme() +"\""
            + " --path=\"\""
            + " " + senchaPackageName;
    CommandLine cmdLine = CommandLine.parse(line);
    DefaultExecutor executor = new DefaultExecutor();
    executor.setWorkingDirectory(workingDirectory);
    executor.setExitValue(0);
    try {
      executor.execute(cmdLine);
    } catch (IOException e) {
      throw new MojoExecutionException("could not execute sencha cmd to generate app", e);
    }
  }

  private void writeWorkspaceJson(File workingDirectory) throws MojoExecutionException {
    Map<String, Object> workspaceConfig = getWorkspaceConfig();

    File fWorkspaceJson = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_WORKSPACE_FILENAME);
    try {
      SenchaUtils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(fWorkspaceJson, workspaceConfig);
    } catch (IOException e) {
      throw new MojoExecutionException("could not write " + SenchaUtils.SENCHA_WORKSPACE_FILENAME, e);
    }
  }

  private void writePackageJson(File workingDirectory) throws MojoExecutionException {
    Map<String, Object> packageConfig = getPackageConfig();

    File fAppJson = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_PACKAGE_FILENAME);
    try {
      SenchaUtils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(fAppJson, packageConfig);
    } catch (IOException e) {
      throw new MojoExecutionException("could not write " + SenchaUtils.SENCHA_PACKAGE_FILENAME, e);
    }

  }

  private void writeAppJson(File workingDirectory) throws MojoExecutionException {
    Map<String, Object> appConfig = getAppConfig();

    File fAppJson = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_APP_FILENAME);
    try {
      SenchaUtils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(fAppJson, appConfig);
    } catch (IOException e) {
      throw new MojoExecutionException("could not write " + SenchaUtils.SENCHA_APP_FILENAME, e);
    }

  }

  private String getPathToWorkingDir(File workingDirectory) throws MojoExecutionException {
    File closestSenchaWorkspaceDir = SenchaUtils.findClosestSenchaWorkspaceDir(workingDirectory);
    if (null == closestSenchaWorkspaceDir) {
      throw new MojoExecutionException("could not find sencha workspace above workingDirectory");
    }
    if (!workingDirectory.getAbsolutePath().startsWith(closestSenchaWorkspaceDir.getAbsolutePath())) {
      throw new MojoExecutionException("found sencha workspace directory is not in order above workingDirectory");
    }
    String pathToWorkingDirectory = workingDirectory.getAbsolutePath().replaceFirst("^" + Matcher.quoteReplacement(closestSenchaWorkspaceDir.getAbsolutePath() + File.separator), "");
    pathToWorkingDirectory = pathToWorkingDirectory.replace(File.separator, "/"); // make sure / is used so no additional escaping is needed for cmd line
    return pathToWorkingDirectory;
  }

  private void buildSenchaPackage(File senchaPackageDirectory) throws MojoExecutionException {
    String line = "sencha package build";
    CommandLine cmdLine = CommandLine.parse(line);
    DefaultExecutor executor = new DefaultExecutor();
    executor.setWorkingDirectory(senchaPackageDirectory);
    executor.setExitValue(0);
    try {
      executor.execute(cmdLine);
    } catch (IOException e) {
      throw new MojoExecutionException("could not execute sencha cmd to build package", e);
    }
  }

  private void buildSenchaApp(File senchaAppDirectory) throws MojoExecutionException {
    String line = "sencha package app";
    CommandLine cmdLine = CommandLine.parse(line);
    DefaultExecutor executor = new DefaultExecutor();
    executor.setWorkingDirectory(senchaAppDirectory);
    executor.setExitValue(0);
    try {
      executor.execute(cmdLine);
    } catch (IOException e) {
      throw new MojoExecutionException("could not execute sencha cmd to build app", e);
    }
  }

  private Map<String, Object> getConfig(Configurer[] configurers) throws MojoExecutionException {
    Map<String, Object> config = new LinkedHashMap<String, Object>();

    for (Configurer configurer : configurers) {
      configurer.configure(config);
    }

    return config;
  }

  private Map<String, Object> getWorkspaceConfig() throws MojoExecutionException {
    return getConfig(workspaceConfigurers);
  }

  private Map<String, Object> getPackageConfig() throws MojoExecutionException {
    return getConfig(packageConfigurers);
  }

  private Map<String, Object> getAppConfig() throws MojoExecutionException {
    return getConfig(appConfigurers);
  }
}
