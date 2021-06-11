package net.jangaroo.jooc.mvnplugin.sencha;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.configbuilder.SenchaConfigBuilder;
import net.jangaroo.jooc.mvnplugin.sencha.executor.SenchaCmdExecutor;
import net.jangaroo.jooc.mvnplugin.util.FileHelper;
import net.jangaroo.jooc.mvnplugin.util.MavenDependencyHelper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static net.jangaroo.jooc.mvnplugin.Type.META_INF_PATH;

public class SenchaUtils {

  public static final String SEPARATOR = "/";

  public static final String APP_DIRECTORY_NAME = "app";

  public static final String TEST_APP_DIRECTORY_NAME = "test-classes"; // for historic reasons...

  public static final String EXT_DIRECTORY_NAME = "ext";

  public static final String PACKAGES_DIRECTORY_NAME = "packages";

  public static final String APPS_DIRECTORY_NAME = "apps";

  public static final String LOCAL_PACKAGES_PATH = SEPARATOR + PACKAGES_DIRECTORY_NAME + SEPARATOR;

  public static final String LOCAL_APPS_PATH = SEPARATOR + APPS_DIRECTORY_NAME + SEPARATOR;

  public static final String APP_TARGET_DIRECTORY = SEPARATOR + APP_DIRECTORY_NAME;

  public static final String APPS_TARGET_DIRECTORY = SEPARATOR + APPS_DIRECTORY_NAME;

  /**
   * The name of the folder of the generated module inside the packages folder of the module.
   * Make sure that the name is not too long to avoid exceeding the max path length in windows.
   * The old path length relative to the target folder was 43 chars:
   * classes\META-INF\resources\joo\classes\com
   *
   * So to avoid compatiblity issues the max length for the path is:
   *
   * 43 - SENCHA_BASE_BATH.length - SENCHA_PACKAGES.length - SENCHA_PACKAGE_LOCAL.length
   *    - SENCHA_CLASS_PATH.length - 4 (Separator)
   */
  public static final String SENCHA_LOCALE_PATH = "locale";
  public static final String DEFAULT_LOCALE = "en";
  public static final String SENCHA_RESOURCES_PATH = "resources";
  public static final String SENCHA_BUNDLED_RESOURCES_PATH = "bundledResources";
  public static final String PRODUCTION_PROFILE = "production";
  public static final String TESTING_PROFILE = "testing";
  public static final String DEVELOPMENT_PROFILE = "development";

  public static final String TOOLKIT_CLASSIC = "classic";


  private static final String SENCHA_CFG = "sencha.cfg";
  public static final String SENCHA_DIRECTORYNAME = ".sencha";
  public static final String SENCHA_WORKSPACE_CONFIG = SENCHA_DIRECTORYNAME + SEPARATOR + "workspace" + SEPARATOR + SENCHA_CFG;
  public static final String SENCHA_PACKAGE_CONFIG = SENCHA_DIRECTORYNAME + SEPARATOR + PACKAGES_DIRECTORY_NAME + SEPARATOR + SENCHA_CFG;
  private static final String SENCHA_APP_CONFIG = SENCHA_DIRECTORYNAME + SEPARATOR + APP_DIRECTORY_NAME + SEPARATOR + SENCHA_CFG;

  public static final String SENCHA_WORKSPACE_FILENAME = "workspace.json";
  public static final String SENCHA_PACKAGE_FILENAME = "package.json";
  public static final String SENCHA_APP_FILENAME = "app.json";
  public static final String PACKAGE_CONFIG_FILENAME = "packageConfig.js";
  public static final String REQUIRED_CLASSES_FILENAME = "requiredClasses.js";
  public static final String DYNAMIC_PACKAGES_FILENAME = "dynamic-packages.json";
  public static final String ADDITIONAL_PACKAGES_PATH = "/additional-packages/*";
  public static final String APP_MANIFEST_FILENAME = "app-manifest.json";
  public static final String APP_MANIFEST_FRAGMENT_FILENAME = "app-manifest-fragment.json";
  public static final String APPS_FILENAME = "apps.json";

  public static final String SENCHA_TEST_APP_TEMPLATE_ARTIFACT_ID = "sencha-test-app-template";
  public static final String SENCHA_APP_TEMPLATE_ARTIFACT_ID = "sencha-app-template";
  public static final String SENCHA_APP_TEMPLATE_GROUP_ID = "net.jangaroo";

  private static final Pattern INTEGER_VERSION_PATTERN = Pattern.compile("[0-9]+");

  public static final String AUTO_CONTENT_COMMENT =
          "DO NOT CHANGE - This file was automatically generated by the jangaroo-maven-plugin";

  public static final Map<String, String> PLACEHOLDERS = ImmutableMap.of( // TODO data structure and location??
          Type.APP, "${app.dir}",
          Type.CODE, "${package.dir}",
          Type.THEME, "${package.dir}",
          Type.WORKSPACE, "${workspace.dir}"
  );

  private static final ObjectMapper objectMapper;
  private static final String DOT_SWC_EXTENSION = '.' + Type.SWC_EXTENSION;
  private static final String REMOTE_PACKAGES_DIR = ".remote-packages";

  static {
    objectMapper = new ObjectMapper();
    objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
  }

  private SenchaUtils() {
    // hide constructor
  }

  public static String getSenchaPackageName(String groupId, String artifactId) {
    return groupId + "__" + artifactId;
  }

  public static String getSenchaPackageName(@Nonnull MavenProject project) {
    return getSenchaPackageName(project.getGroupId(), project.getArtifactId());
  }

  public static String getSenchaVersionForMavenVersion(String version) {
    StringBuilder senchaVersion = new StringBuilder();
    Matcher matcher = INTEGER_VERSION_PATTERN.matcher(version);
    for (int i = 0; i < 4 && matcher.find(); i++) {
      if (i > 0) {
        senchaVersion.append(".");
      }
      senchaVersion.append(matcher.group());
    }
    return senchaVersion.length() == 0 ? null : senchaVersion.toString();
  }

  @Nullable
  public static Dependency getDependencyByRef(@Nonnull MavenProject project, @Nullable String ref) {
    Dependency themeDependency = MavenDependencyHelper.fromKey(ref);
    // verify that provided artifact is under project dependencies
    Set<Artifact> dependencyArtifacts = project.getDependencyArtifacts();
    for (Artifact artifact : dependencyArtifacts) {
      Dependency artifactDependency = MavenDependencyHelper.fromArtifact(artifact);
      if (MavenDependencyHelper.equalsGroupIdAndArtifactId(artifactDependency, themeDependency)) {
        return artifactDependency;
      }
    }
    return null;
  }

  /**
   * Generates an absolute path to the module dir for the given relative path using a placeholder.
   *
   * @param packageType the Maven project's packaging type
   * @param relativePath the path relative to the Sencha module
   * @return path prefixed with a placeholder and a separator to have an absolute path
   */
  public static String absolutizeToModuleWithPlaceholder(String packageType, String relativePath) {
    return absolutizeWithPlaceholder(PLACEHOLDERS.get(packageType), relativePath);
  }

  /**
   * Generates an absolute path to the module dir for the given relative path using a placeholder.
   *
   * @param placeholder the placeholder, e.g. "${workspace.dir}"
   * @param relativePath the path relative to the Sencha module
   * @return path prefixed with a placeholder and a separator to have an absolute path
   */
  public static String absolutizeWithPlaceholder(String placeholder, String relativePath) {
    // make sure only normal slashes are used and no backslashes (e.g. on windows systems)
    String normalizedRelativePath = FilenameUtils.separatorsToUnix(relativePath);
    String result = placeholder;
    if (StringUtils.isNotEmpty(normalizedRelativePath) && !normalizedRelativePath.startsWith(SEPARATOR)) {
      result += SEPARATOR + normalizedRelativePath;
    }
    return result;
  }

  public static ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public static boolean isRequiredSenchaDependency(@Nonnull Dependency dependency, boolean includeTestDependencies) {
    return isRequiredSenchaDependency(dependency, includeTestDependencies, includeTestDependencies);
  }

  public static boolean isRequiredSenchaDependency(@Nonnull Dependency dependency,
                                                   boolean includeTestDependencies,
                                                   boolean includeProvidedDependencies) {
    return isSenchaDependency(dependency)
            && (includeProvidedDependencies || !Artifact.SCOPE_PROVIDED.equals(dependency.getScope()))
            && (includeTestDependencies || !Artifact.SCOPE_TEST.equals(dependency.getScope()));
  }

  public static boolean isSenchaDependency(@Nonnull Dependency dependency) {
    return isSenchaDependency(dependency.getType());
  }

  public static boolean isSenchaDependency(@Nonnull String type) {
    return Type.SWC_EXTENSION.equals(type) || Type.PKG_EXTENSION.equals(type);
  }

  public static boolean doesSenchaAppExist(File directory) {
    File senchaCfg = new File(directory, SenchaUtils.SENCHA_APP_CONFIG);
    return senchaCfg.exists();
  }

  public static String getPackagesPath(MavenProject project) {
    return LOCAL_PACKAGES_PATH + getSenchaPackageName(project);
  }

  public static void generateSenchaAppFromTemplate(File workingDirectory,
                                                   String appName,
                                                   String applicationClass,
                                                   String toolkit,
                                                   Log log,
                                                   String logLevel,
                                                   String senchaJvmArgs) throws MojoExecutionException {
    String templateName = getSenchaPackageName(SENCHA_APP_TEMPLATE_GROUP_ID, SENCHA_APP_TEMPLATE_ARTIFACT_ID) + "/tpl";
    ImmutableMap<String, String> properties = ImmutableMap.of("appName", appName, "applicationClass", applicationClass);
    generateSenchaAppFromTemplate(workingDirectory, appName, toolkit, templateName, properties, log, logLevel, senchaJvmArgs);
  }

  public static void generateSenchaTestAppFromTemplate(File workingDirectory,
                                                       MavenProject project,
                                                       String appName,
                                                       String testSuite,
                                                       String toolkit,
                                                       Log log,
                                                       String logLevel,
                                                       String senchaJvmArgs) throws MojoExecutionException {
    String templateName = getSenchaPackageName(SENCHA_APP_TEMPLATE_GROUP_ID, SENCHA_TEST_APP_TEMPLATE_ARTIFACT_ID) + "/tpl";
    ImmutableMap<String, String> properties = ImmutableMap.of("moduleName", getSenchaPackageName(project), "testSuite", testSuite);
    generateSenchaAppFromTemplate(workingDirectory, appName, toolkit, templateName, properties, log, logLevel, senchaJvmArgs);
  }

  private static void generateSenchaAppFromTemplate(File workingDirectory,
                                                    String appName,
                                                    String toolkit,
                                                    String templateName,
                                                    Map<String, String> properties,
                                                    Log log,
                                                    String logLevel,
                                                    String senchaJvmArgs) throws MojoExecutionException {
    List<String> arguments = new ArrayList<>();
    arguments.add("generate app");
    arguments.add("-ext " + toolkit);
    arguments.add("--template " + templateName);
    arguments.add("--path=\".\"");
    arguments.add("--refresh=false");

    addSwitchFullIfCmd6_5(arguments);

    if (properties != null) {
      for (Map.Entry<String, String> entry: properties.entrySet()) {
        arguments.add(String.format("-D%s=%s",entry.getKey(), entry.getValue()));
      }
    }
    arguments.add(appName);

    SenchaCmdExecutor senchaCmdExecutor = new SenchaCmdExecutor(workingDirectory, StringUtils.join(arguments, ' '), senchaJvmArgs, log, logLevel);
    senchaCmdExecutor.execute();
  }

  /**
   * Special case: Sencha Cmd 6.5 made a breaking change in "generate workspace/app", and to restore
   * the original behavior, we have to specify the new parameter "--full" (which Cmd < 6.5 prompts with an error).
   *
   * @param arguments the arguments list to add "--full" to if Sencha Cmd version is 6.5 or higher
   * @throws MojoExecutionException if Sencha Cmd version cannot be determined
   */
  public static void addSwitchFullIfCmd6_5(List<String> arguments) throws MojoExecutionException {
    try {
      int[] cmdVersion = SenchaCmdExecutor.queryVersion();
      if (cmdVersion == null) {
        throw new MojoExecutionException("No version information found in output of 'sencha switch -l'.");
      }
      // version is 6.5 or above?
      if (cmdVersion[0] > 6 || cmdVersion[0] == 6 && cmdVersion[1] >= 5) {
        // add new command line switch:
        arguments.add("--full");
      }
    } catch (IOException ioe) {
      throw new MojoExecutionException("Could not run 'sencha'. Please install Sencha Cmd >= 6.2.1 or check your PATH environment variable.", ioe);
    }
  }

  public static void createSenchaCfg(Path senchaCfgSource, Path senchaCfgTarget, Map<String, Object> properties)
          throws MojoExecutionException {
    if (Files.exists(senchaCfgSource)) {
      try {
        List<String> senchaCfgTmpContent = Files.readAllLines(senchaCfgSource, Charset.forName("UTF-8"));
        List<String> updatedSenchaCfgContent = updateSenchaCfgContent(senchaCfgTmpContent,
                properties);
        Files.write(senchaCfgTarget, updatedSenchaCfgContent, Charset.forName("UTF-8"));
      } catch (IOException e) {
        throw new MojoExecutionException("Modifying sencha.cfg file failed", e);
      }
    } else {
      throw new MojoExecutionException("Could not find sencha.cfg file in " + senchaCfgSource);
    }
  }

  private static List<String> updateSenchaCfgContent(@Nonnull List<String> currentContent, Map<String, Object> properties) {
    // prepend comment and delete first comment line with usually contains the date time
    if (currentContent.get(0).startsWith("#")) { // if the first
      currentContent.remove(0);
    }
    List<String> newSenchaCfg = new ArrayList<>(currentContent.size());
    newSenchaCfg.add("#");
    newSenchaCfg.add("# " + AUTO_CONTENT_COMMENT);
    newSenchaCfg.add("#");
    newSenchaCfg.add("");
    newSenchaCfg.addAll(currentContent);
    for (Map.Entry<String, Object> property : properties.entrySet()) {
      newSenchaCfg.add(String.format("%s=%s", property.getKey(), property.getValue()));
    }
    return newSenchaCfg;
  }

  public static void configureDefaults(SenchaConfigBuilder configBuilder, String defaultsFileName) throws MojoExecutionException {
    try {
      configBuilder.defaults(defaultsFileName);
    } catch (IOException e) {
      throw new MojoExecutionException("Cannot load " + defaultsFileName, e);
    }
  }

  public static void writeFile(@Nonnull SenchaConfigBuilder configBuilder,
                               @Nonnull String destinationFileDir,
                               @Nonnull String destinationFileName,
                               @Nullable String comment,
                               Log log)
          throws MojoExecutionException {

    String tmpDestFileName = destinationFileName + ".tmp";
    final File tmpDestFile = new File(destinationFileDir, tmpDestFileName);
    final File destFile = new File(destinationFileDir, destinationFileName);
    configBuilder.destFile(tmpDestFile);
    if (comment != null ) {
      configBuilder.destFileComment(comment);
    }
    try {
      configBuilder.buildFile();
    } catch (IOException io) {
      try {
        Files.delete(tmpDestFile.toPath());
      } catch (IOException e) {
        log.warn("Unable to delete temporary file " + tmpDestFile.getAbsolutePath(), e);
      }
      throw new MojoExecutionException(String.format("Writing %s failed", tmpDestFile.getName()), io);
    }
    try {
      Files.move(tmpDestFile.toPath(), destFile.toPath(),
              StandardCopyOption.REPLACE_EXISTING,
              StandardCopyOption.ATOMIC_MOVE);
    } catch (IOException e) {
      throw new MojoExecutionException(String.format("Moving %s to %s failed", tmpDestFile.getName(), destFile.getAbsolutePath()), e);
    }
  }

  /**
   * Unfortunately, we need to implement our own extractor because
   * the maven unarchiver is unable to strip a given path prefix from the entry paths
   */
  public static void extractPkg(File archive, File targetDir) throws MojoExecutionException {
    try (ZipFile zipFile = new ZipFile(archive)) {
      boolean isSwc = archive.getName().endsWith(DOT_SWC_EXTENSION);
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      while (entries.hasMoreElements()) {
        ZipEntry entry = entries.nextElement();
        String targetName = entry.getName();
        boolean isSwcPkgFile = isSwc && targetName.startsWith(Type.SWC_PKG_PATH);
        // extract only package contents
        if (isSwcPkgFile || (!isSwc && !targetName.startsWith(META_INF_PATH))) {
          if (isSwcPkgFile) {
            targetName = targetName.substring(Type.SWC_PKG_PATH.length());
          }
          File target = new File(targetDir, targetName);
          if (entry.isDirectory()) {
            FileHelper.ensureDirectory(target);
          } else {
            FileHelper.ensureDirectory(target.getParentFile());
            try (InputStream in = zipFile.getInputStream(entry);
                 OutputStream out = new FileOutputStream(target)) {
              IOUtils.copy(in, out);
            }
          }
        }
      }
    } catch (IOException e) {
      throw new MojoExecutionException("IO Error while extracting archive", e);
    }
  }

  public static File baseDir(MavenSession mavenSession) {
    File baseDir = mavenSession.getRequest().getMultiModuleProjectDirectory();
    if (baseDir == null) {
      baseDir = new File(mavenSession.getRequest().getBaseDirectory());
    }
    return baseDir;
  }

  public static File remotePackagesDir(MavenSession mavenSession) {
    File currentBaseDir = baseDir(mavenSession);

    for (;;) {
      File remotePackagesDir = new File(currentBaseDir, REMOTE_PACKAGES_DIR);
      File baseParent = currentBaseDir.getParentFile();
      if (remotePackagesDir.exists() || baseParent == null) {
        return remotePackagesDir;
      }
      File pom = new File(baseParent, "pom.xml");
      if (!pom.exists()) {
        // don't go out of maven projects
        return remotePackagesDir;
      }
      currentBaseDir = baseParent;
    }
  }
}
