package net.jangaroo.jooc.mvnplugin.sencha;

import net.jangaroo.jooc.mvnplugin.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.Configurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.DefaultSenchaCodePackageConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.DefaultSenchaThemePackageConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.MetadataConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.PathConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.RequiresConfigurer;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.SenchaConfigurationConfigurer;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

class SenchaPackageHelper extends AbstractSenchaHelper {

  private SenchaWorkspaceHelper workspaceHelper;

  private final Configurer[] packageConfigurers;
  private final String senchaPath;
  private final String senchaPackagePath;

  public SenchaPackageHelper(MavenProject project, SenchaConfiguration senchaConfiguration, Log log) {
    super(project, senchaConfiguration, log);

    String buildDirectory = project.getBuild().getDirectory();
    this.senchaPath = buildDirectory + File.separator + SenchaUtils.SENCHA_BASE_PATH;

    this.senchaPackagePath = senchaPath + File.separator + SenchaUtils.SENCHA_PACKAGES + File.separator + SenchaUtils.SENCHA_PACKAGES_LOCAL + File.separator + SenchaUtils.LOCAL_PACKAGE_PATH;

    MetadataConfigurer metadataConfigurer = new MetadataConfigurer(project);
    RequiresConfigurer requiresConfigurer = new RequiresConfigurer(project, senchaConfiguration);
    SenchaConfigurationConfigurer senchaConfigurationConfigurer = new SenchaConfigurationConfigurer(project, senchaConfiguration);
    PathConfigurer pathConfigurer = new PathConfigurer(senchaConfiguration);

    Configurer defaultSenchaPackageConfigurer;
    if (!SenchaConfiguration.Type.THEME.equals(senchaConfiguration.getType())) {
      defaultSenchaPackageConfigurer =  DefaultSenchaCodePackageConfigurer.getInstance();
    } else {
      defaultSenchaPackageConfigurer =  DefaultSenchaThemePackageConfigurer.getInstance();
    }

    this.packageConfigurers = new Configurer[] {
            defaultSenchaPackageConfigurer,
            metadataConfigurer,
            requiresConfigurer,
            senchaConfigurationConfigurer,
            pathConfigurer
    };

    SenchaConfiguration workspaceConfiguration = new SenchaConfiguration();
    workspaceConfiguration.setEnabled(true);
    String projectBuildDirRelativeToProjectBaseDir = project.getBasedir().toPath().relativize(Paths.get(project.getBuild().getDirectory())).toString();
    workspaceConfiguration.setBuildDir(projectBuildDirRelativeToProjectBaseDir + File.separator + SenchaUtils.SENCHA_BASE_PATH + File.separator + SenchaUtils.SENCHA_RELATIVE_BUILD_PATH);
    workspaceConfiguration.setType(SenchaConfiguration.Type.WORKSPACE);
    workspaceConfiguration.setPackagesDir(senchaConfiguration.getPackagesDir());
    workspaceConfiguration.setExtFrameworkDir(senchaConfiguration.getExtFrameworkDir());
    workspaceHelper = new SenchaWorkspaceHelper(project, workspaceConfiguration, log);
  }

  @Override
  public void createModule() throws MojoExecutionException {
    if (getSenchaConfiguration().isEnabled()) {
      createTemporaryWorkspaceIfConfigured(false);

      File workingDirectory;
      try {
        workingDirectory = new File(senchaPackagePath).getCanonicalFile();
      } catch (IOException e) {
        throw new MojoExecutionException("could not determine working directory", e);
      }

      File senchaCfg = new File(workingDirectory.getAbsolutePath() + File.separator + SenchaUtils.SENCHA_PACKAGE_CONFIG);
      // make sure senchaCfg does not exist
      if (senchaCfg.exists()) {
        if (!senchaCfg.delete()) {
          throw new MojoExecutionException("could not delete " + SenchaUtils.SENCHA_PACKAGE_CONFIG + " for package");
        }
      }

      Path pathToWorkingDirectory = SenchaUtils.getRelativePathFromWorkspaceToWorkingDir(workingDirectory);

      String line = "sencha generate package"
              + " --name=\"" + getSenchaModuleName() + "\""
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
        } finally {
          if (null != pw) {
            pw.close();
          }
        }
      } else {
        throw new MojoExecutionException("could not find sencha.cfg of package");
      }

      removeTemporaryWorkspaceIfConfigured();
    }
  }

  @Override
  public void prepareModule() throws MojoExecutionException {
    if (getSenchaConfiguration().isEnabled()) {
      File senchaPackageDirectory = new File(senchaPackagePath);

      if (!senchaPackageDirectory.exists()) {
        getLog().info("generating sencha package into: " + senchaPackageDirectory.getPath());
        getLog().debug("created " + senchaPackageDirectory.mkdirs());
      }

      copyFiles(senchaPackagePath);

      File workingDirectory = new File(senchaPackagePath);

      writePackageJson(workingDirectory);
    }
  }

  @Override
  public void packageModule(JarArchiver archiver) throws MojoExecutionException {
    if (getSenchaConfiguration().isEnabled()) {
      if (!getSenchaConfiguration().isSkipBuild()) {
        createTemporaryWorkspaceIfConfigured(true);

        if (getSenchaConfiguration().isScssFromSrc()) {
          // rewrite package.json so the src path is removed in build
          getSenchaConfiguration().setScssFromSrc(false);
          File workingDirectory = new File(senchaPackagePath);
          writePackageJson(workingDirectory);
          getSenchaConfiguration().setScssFromSrc(true);
        }

        File senchaPackageDirectory = new File(senchaPackagePath);

        if (!senchaPackageDirectory.exists()) {
          throw new MojoExecutionException("sencha package directory does not exist: " + senchaPackageDirectory.getPath());
        }

        buildSenchaPackage(senchaPackageDirectory);

        File workspaceDir = SenchaUtils.findClosestSenchaWorkspaceDir(getProject().getBasedir());

        if (null == workspaceDir) {
          throw new MojoExecutionException("could not find sencha workspace directory");
        }

        // Read workspace.json
        String workspaceOutputPath = workspaceDir.getAbsolutePath() + File.separator + getSenchaConfiguration().getBuildDir();
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

        File pkg = new File(workspaceOutputPath + File.separator + getSenchaModuleName() + File.separator + getSenchaModuleName() + SenchaUtils.SENCHA_PKG_EXTENSION);
        if (!pkg.exists()) {
          throw new MojoExecutionException("could not find " + SenchaUtils.SENCHA_PKG_EXTENSION + " for sencha package " + getSenchaModuleName());
        }
        File tempDirectory;
        try {
          tempDirectory = createTempDirectory();
        } catch (IOException e) {
          throw new MojoExecutionException("could not create temporary directory", e);
        }
        tempDirectory.deleteOnExit();
        SenchaUtils.extractZipToDirectory(pkg, tempDirectory);
        try {
          archiver.addDirectory(tempDirectory, SenchaUtils.SENCHA_BASE_PATH + "/");
        } catch (ArchiverException e) {
          throw new MojoExecutionException("could not add package directory to jar", e);
        }

        if (getSenchaConfiguration().isScssFromSrc()) {
          // rewrite package.json so the src path is removed in build
          getSenchaConfiguration().setScssFromSrc(true);
          File workingDirectory = new File(senchaPackagePath);
          writePackageJson(workingDirectory);
          getSenchaConfiguration().setScssFromSrc(false);
        }

        removeTemporaryWorkspaceIfConfigured();
      } else {
        // at least add a package indicator to jar
        try {
          archiver.addFile(new File(senchaPackagePath + File.separator + SenchaUtils.SENCHA_PACKAGE_FILENAME), SenchaUtils.SENCHA_BASE_PATH + "/" + SenchaUtils.SENCHA_PACKAGE_FILENAME);
        } catch (ArchiverException e) {
          throw new MojoExecutionException("could not add package indicator to jar", e);
        }
      }
    }
  }

  @Override
  public void deleteModule() throws MojoExecutionException {
    // TODO
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

  private Map<String, Object> getPackageConfig() throws MojoExecutionException {
    return getConfig(packageConfigurers);
  }

  private static File createTempDirectory() throws IOException {
    final File temp;

    temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

    if(!(temp.delete()))
    {
      throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
    }

    if(!(temp.mkdir()))
    {
      throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
    }

    return (temp);
  }

  private void createTemporaryWorkspaceIfConfigured(boolean extractRemotePackages) throws MojoExecutionException {
    if (getSenchaConfiguration().isTemporaryWorkspace()) {
      // create temporary workspace
      workspaceHelper.deleteModule();
      workspaceHelper.prepareModule();
      workspaceHelper.createModule();
      if (extractRemotePackages) {
        // TODO: determine real target directory
        SenchaUtils.extractRemotePackagesForProject(getProject(), getProject().getBuild().getDirectory() + "/sencha/packages/remote");
      }
    }
  }

  private void removeTemporaryWorkspaceIfConfigured() throws MojoExecutionException {
    if (getSenchaConfiguration().isTemporaryWorkspace()) {
      // remove temporary workspace
      workspaceHelper.deleteModule();
    }
  }
}
