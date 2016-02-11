package net.jangaroo.jooc.mvnplugin.util;

import net.jangaroo.jooc.json.Json;
import net.jangaroo.jooc.json.JsonArray;
import net.jangaroo.jooc.json.JsonObject;
import net.jangaroo.jooc.mvnplugin.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.SenchaProfileConfiguration;
import net.jangaroo.jooc.mvnplugin.Types;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.zip.ZipEntry;
import org.codehaus.plexus.archiver.zip.ZipFile;
import org.codehaus.plexus.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A set of helper functions for handling sencha package structure.
 */
public class SenchaHelper {

  public static final String SENCHA_BASE_PATH = "/sencha";

  public static final String SENCHA_WORKSPACE_RELATIVE_OUTPUT_PATH = "${workspace.dir}/target/sencha/build";

  public static final String SENCHA_PACKAGE_RELATIVE_OUTPUT_PATH = "${package.dir}/build";
  public static final String SENCHA_PACKAGE_RELATIVE_CLASS_PATH = "${package.dir}/src";
  public static final String SENCHA_PACKAGE_RELATIVE_OVERRIDES_PATH = "${package.dir}/overrides,${package.dir}/${toolkit.name}/overrides";
  public static final String SENCHA_PACKAGE_RELATIVE_RESOURCES_PATH = "${package.dir}/resources";
  public static final String SENCHA_PACKAGE_RELATIVE_RESOURCES_TOOLKIT_PATH = "${toolkit.name}/resources";
  public static final String SENCHA_PACKAGE_RELATIVE_RESOURCES_BUILD_PROFILE_PATH = "${build.id}/resources";

  public static final String SENCHA_DIRECTORYNAME = ".sencha";
  public static final String SENCHA_PACKAGES_DIRECTORYNAME = "packages";
  public static final String SENCHA_WORKSPACE_FILENAME = "workspace.json";
  public static final String SENCHA_PACKAGE_FILENAME = "package.json";
  public static final String SENCHA_APP_FILENAME = "app.json";

  private static final String MAVEN_DEPENDENCY_SCOPE_TEST = "test";

  private static Pattern SENCHA_VERSION_PATTERN = Pattern.compile("^[0-9]+[\\.[0-9]+]{0,3}$");

  private final MavenProject project;
  private final SenchaConfiguration senchaConfiguration;
  private final Log log;

  private final String senchaPackageName;

  private final String senchaWorkspacePath;
  private final String senchaPackagePath;

  public SenchaHelper(MavenProject project, SenchaConfiguration senchaConfiguration, Log log) {
    this.project = project;
    this.senchaConfiguration = senchaConfiguration;
    this.log = log;
    this.senchaPackageName = getSenchaPackageNameForMavenProject(project);
    this.senchaWorkspacePath = project.getBuild().getDirectory() + "/" + SENCHA_BASE_PATH;
    this.senchaPackagePath = senchaWorkspacePath + "/packages/" + senchaPackageName;
  }

  public void executeInitialize() throws MojoExecutionException {
    if (senchaConfiguration.isEnabled()) {
      File senchaPackageDirectory = new File(senchaPackagePath);

      if (!senchaPackageDirectory.exists()) {
        getLog().info("generating sencha package into: " + senchaPackageDirectory.getPath());
        getLog().debug("created " + senchaPackageDirectory.mkdirs());
      }

      if (SenchaConfiguration.Type.WORKSPACE.equals(senchaConfiguration.getType())) {
        createSenchaWorkspace(project.getBasedir(), getAllWorkspacePaths(project));
      }
      if (SenchaConfiguration.Type.CODE.equals(senchaConfiguration.getType())) {
        createSenchaCodePackage(senchaPackageDirectory);
      }
    }
  }

  public void executeInstall() throws MojoExecutionException {
    if (senchaConfiguration.isEnabled()) {
      if (senchaConfiguration.getType() == SenchaConfiguration.Type.CODE) {

        File jangarooResourcesDir = new File(project.getBuild().getDirectory() + "/classes/META-INF/resources");
        File senchaResourcesDir = new File(SENCHA_PACKAGE_RELATIVE_RESOURCES_PATH.replace("${package.dir}", senchaPackagePath));
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
          File senchaClassDir = new File(SENCHA_PACKAGE_RELATIVE_CLASS_PATH.replace("${package.dir}", senchaPackagePath));
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
          File senchaOverridesDir = new File(SENCHA_PACKAGE_RELATIVE_OVERRIDES_PATH.replace("${package.dir}", senchaPackagePath));
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
    }
  }

  public void executePackage(JarArchiver archiver) throws ArchiverException, MojoExecutionException {
    // for now:
    executeInitialize();
    executeInstall();

    if (senchaConfiguration.isEnabled()) {
      if (senchaConfiguration.getType() == SenchaConfiguration.Type.CODE) {

        File senchaPackageDirectory = new File(senchaPackagePath);

        if (!senchaPackageDirectory.exists()) {
          throw new MojoExecutionException("sencha package directory does not exist: " + senchaPackageDirectory.getPath());
        }

        buildSenchaPackage(senchaPackageDirectory); // move to executePackage

        File workspaceDir = findClosestSenchaWorkspaceDir(project.getFile().getParentFile());

        if (null == workspaceDir) {
          throw new MojoExecutionException("could not find sencha workspace directory");
        }

        String workspaceOutputPath = SENCHA_WORKSPACE_RELATIVE_OUTPUT_PATH.replace("${workspace.dir}", workspaceDir.getAbsolutePath());
        File pkg = new File(workspaceOutputPath + "/" + senchaPackageName + "/" + senchaPackageName + ".pkg");
        if (!pkg.exists()) {
          throw new MojoExecutionException("could not find pkg for sencha package " + senchaPackageName);
        }
        archiver.addFile(pkg, pkg.getName());
      }
    }
  }

  private File findClosestSenchaWorkspaceDir(File dir) {
    while (null != dir) {
      String[] list = dir.list(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          return SENCHA_WORKSPACE_FILENAME.equals(name);
        }
      });
      if (list.length > 0) {
        break;
      }
      dir = dir.getParentFile();
    }
    return dir;
  }

  private static String getSenchaPackageName(String groupId, String artifactId) {
    return groupId + "__" + artifactId;
  }

  private static String getSenchaPackageNameForMavenProject(MavenProject project) {
    return getSenchaPackageName(project.getGroupId(), project.getArtifactId());
  }

  private static String getSenchaPackageNameForArtifact(Artifact artifact) {
    return getSenchaPackageName(artifact.getGroupId(), artifact.getArtifactId());
  }

  private static String getSenchaVersionForProject(MavenProject project) {
    String version = project.getVersion().replace("-SNAPSHOT", "");
    if (SENCHA_VERSION_PATTERN.matcher(version).matches()) {
      return version;
    } else {
      return "1.0.0";
    }
  }

  private Log getLog() {
    return log;
  }

  private List<String> getAllWorkspacePaths(MavenProject project) {
    // TODO
    return Collections.emptyList();
  }

  private void createSenchaWorkspace(File workingDirectory, List<String> additionalWorkspacePaths) throws MojoExecutionException {
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

    String workspaceJsonAsString = getWorkspaceJson().toString(4, 4);
    File fWorkspaceJson = new File(workingDirectory.getAbsolutePath() + "/" + SENCHA_WORKSPACE_FILENAME);

    try {
      FileWriter fw = new FileWriter(fWorkspaceJson.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(workspaceJsonAsString);
      bw.close();
    } catch (IOException e) {
      throw new MojoExecutionException("could not write" + SENCHA_WORKSPACE_FILENAME, e);
    }
  }

  private void createSenchaCodePackage(File workingDirectory) throws MojoExecutionException {
    File closestSenchaWorkspaceDir = findClosestSenchaWorkspaceDir(workingDirectory);
    if (null == closestSenchaWorkspaceDir) {
      throw new MojoExecutionException("could not find sencha workspace above workingDirectory");
    }
    if (!workingDirectory.getAbsolutePath().startsWith(closestSenchaWorkspaceDir.getAbsolutePath())) {
      throw new MojoExecutionException("found sencha workspace directory is not in order above workingDirectory");
    }
    String pathToWorkingDirectory = workingDirectory.getAbsolutePath().replaceFirst("^" + Matcher.quoteReplacement(closestSenchaWorkspaceDir.getAbsolutePath() + File.separator), "");
    pathToWorkingDirectory = pathToWorkingDirectory.replace(File.separator, "/"); // make sure / is used so no additional escaping is needed for cmd line

    writePackageJson(workingDirectory);

    writePackageSenchaCfg(workingDirectory, pathToWorkingDirectory);
  }

  private void writePackageJson(File workingDirectory) throws MojoExecutionException {
    String packageJsonAsString = getPackageJson().toString(4, 4);

    File fPackageJson = new File(workingDirectory.getAbsolutePath() + "/" + SENCHA_PACKAGE_FILENAME);
    BufferedWriter bw = null;
    try {
      FileWriter fw = new FileWriter(fPackageJson.getAbsoluteFile());
      bw = new BufferedWriter(fw);
      bw.write(packageJsonAsString);
    } catch (IOException e) {
      throw new MojoExecutionException("could not write package.json", e);
    } finally {
      if (bw != null) {
        try {
          bw.close();
        } catch (IOException ioe2) {
          // just ignore it
        }
      }
    }
  }

  private void writePackageSenchaCfg(File workingDirectory, String pathToWorkingDirectory) throws MojoExecutionException {
    File senchaCfg = new File(workingDirectory.getAbsolutePath() + "/.sencha/package/sencha.cfg");
    // make sure senchaCfg does not exist
    if (senchaCfg.exists()) {
      senchaCfg.delete();
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
      } finally {
        if (pw != null) {
          pw.close();
        }
      }
    } else {
      throw new MojoExecutionException("could not find sencha.cfg of package");
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
      throw new MojoExecutionException("could not execute sencha cmd to generate workspace", e);
    }
  }

  private Json getDependencyJson() throws MojoExecutionException {
    Set<String> dependencies = new HashSet<String>();

    @SuppressWarnings("unchecked") Set<Artifact> dependencyArtifacts = project.getDependencyArtifacts();
    for (Artifact artifact : dependencyArtifacts) {
      String packageName = getSenchaPackageNameForArtifact(artifact);
      if (null != packageName
              && Types.JAVASCRIPT_EXTENSION.equals(artifact.getType())
              && !MAVEN_DEPENDENCY_SCOPE_TEST.equalsIgnoreCase(artifact.getScope())) {
        try {
          ZipFile zipFile = new ZipFile(artifact.getFile());
          ZipEntry zipEntry = zipFile.getEntry(packageName + ".pkg");
          if (zipEntry != null) {
            dependencies.add(packageName);
          }
        } catch (IOException e) {
          throw new MojoExecutionException("could not open artifact jar", e);
        }
        // TODO: remove once everything is ok:
        if ("net.jangaroo__jangaroo-runtime".equals(packageName)
                || "net.jangaroo__ckeditor".equals(packageName)
                || "net.jangaroo__jangaroo-net".equals(packageName)
                || "net.jangaroo__ext-as".equals(packageName)) {
          dependencies.add(packageName);
        }
      }
    }

    return new JsonArray(dependencies.toArray());
  }

  private Json getWorkspaceJson() {

    return new JsonObject(
            "apps", new JsonArray(),
            "build", new JsonObject(
                    "dir", SENCHA_WORKSPACE_RELATIVE_OUTPUT_PATH
            ),
            "packages", new JsonObject(
                    "dir", "${workspace.dir}/target/sencha/packages",
                    "extract", "${workspace.dir}/target/sencha/packages/remote"
            )
    );
  }

  private Json getPackageJson() throws MojoExecutionException {

    String senchaVersion = getSenchaVersionForProject(project);

    return new JsonObject(
            "name", senchaPackageName,
            "version", senchaVersion,
            "compatVersion", senchaVersion,
            "type", "code",
            "local", true,
            "namespace", "",
            "framework", "ext",
            "format", "1",
            "toolkit", senchaConfiguration.getToolkit(),
            "creator", StringUtils.defaultString(project.getOrganization() != null ? project.getOrganization().getName() : ""),
            "summary", StringUtils.defaultString(project.getDescription()),
            "detailedDescription", "",
            "requires", getDependencyJson(),
            "css", getAdditionalResources(senchaConfiguration.getAdditionalCssNonBundle(), senchaConfiguration.getAdditionalCssBundle()),
            "js", getAdditionalResources(senchaConfiguration.getAdditionalJsNonBundle(), senchaConfiguration.getAdditionalJsBundle()),
            "production", getProfileAdditionalResources(senchaConfiguration.getProduction()),
            "development", getProfileAdditionalResources(senchaConfiguration.getDevelopment()),
            "testing", getProfileAdditionalResources(senchaConfiguration.getTesting()),
            "output", SENCHA_PACKAGE_RELATIVE_OUTPUT_PATH,
            "classpath", SENCHA_PACKAGE_RELATIVE_CLASS_PATH,
            "overrides", SENCHA_PACKAGE_RELATIVE_OVERRIDES_PATH,
            "resources", new JsonArray(
            new Json[]{
                    new JsonObject(
                            "path", SENCHA_PACKAGE_RELATIVE_RESOURCES_PATH,
                            "output", "shared"
                    ),
                    new JsonObject(
                            "path", SENCHA_PACKAGE_RELATIVE_RESOURCES_TOOLKIT_PATH
                    ),
                    new JsonObject(
                            "path", SENCHA_PACKAGE_RELATIVE_RESOURCES_BUILD_PROFILE_PATH
                    )
            }
    )
    );
  }

  private Json getAdditionalResources(List<String> resourcesNonBundle, List<String> resourcesBundle) {
    List<Json> resources = new ArrayList<Json>();
    if (null != resourcesNonBundle) {
      for (String resource : resourcesNonBundle) {
        resources.add(new JsonObject(
                "path", resource,
                "bundle", false
        ));
      }
    }
    if (null != resourcesBundle) {
      for (String resource : resourcesBundle) {
        resources.add(new JsonObject(
                "path", resource,
                "bundle", true
        ));
      }
    }
    return new JsonArray(
            resources.toArray()
    );
  }

  private Json getProfileAdditionalResources(SenchaProfileConfiguration senchaProfileConfiguration) {
    if (senchaProfileConfiguration != null) {
      return new JsonObject(
              "css", getAdditionalResources(senchaProfileConfiguration.getAdditionalCssNonBundle(), senchaProfileConfiguration.getAdditionalCssBundle()),
              "js", getAdditionalResources(senchaProfileConfiguration.getAdditionalJsNonBundle(), senchaProfileConfiguration.getAdditionalJsBundle())
      );
    }
    return new JsonObject();
  }
}
