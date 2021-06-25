package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.config.JoocConfiguration;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Mojo to compile Jangaroo sources from during the test-compile phase.
 */
@Mojo(name = "testCompile",
        defaultPhase = LifecyclePhase.TEST_COMPILE,
        requiresDependencyResolution = ResolutionScope.TEST,
        threadSafe = true)
public class TestCompilerMojo extends AbstractCompilerMojo {

  /**
   * Output directory for all generated ActionScript3 test files to compile.
   */
  @Parameter(defaultValue = "${project.build.directory}/generated-test-sources/joo")
  private File generatedTestSourcesDirectory;

  /**
   * Test output directory into whose joo/classes sub-directory compiled classes are generated.
   */
  @Parameter(defaultValue = "${project.build.testOutputDirectory}")
  private File testOutputDirectory;

  /**
   * Source directory to scan for test files to compile.
   */
  @Parameter(defaultValue = "${project.build.testSourceDirectory}")
  private File testSourceDirectory;

  /**
   * A list of test inclusion filters for the compiler.
   */
  @Parameter
  private Set<String> testIncludes = new HashSet<>();

  /**
   * A list of test exclusion filters for the compiler.
   */
  @Parameter
  private Set<String> testExcludes = new HashSet<>();

  /**
   * Temporary output directory for compiled classes to be packaged into a single *.js file.
   */
  @Parameter(defaultValue = "${project.build.directory}/temp/jangaroo-test-output/classes")
  private File tempTestClassesOutputDirectory;

  /**
   * Set this to 'true' to bypass unit tests entirely. Its use is NOT RECOMMENDED, especially if you
   * enable it using the "maven.test.skip" property, because maven.test.skip disables both running the
   * tests and compiling the tests. Consider using the skipTests parameter instead.
   */
  @Parameter(defaultValue = "${maven.test.skip}")
  protected boolean skip;

  /**
   * @return null as API stub generation does not make sense for test sources
   */
  @Override
  protected File getApiOutputDirectory() {
    return null;
  }

  @Override
  protected List<File> getCompileSourceRoots() {
    return Arrays.asList(testSourceDirectory, generatedTestSourcesDirectory);
  }


  @Override
  protected File getOutputDirectory() {
    return testOutputDirectory;
  }

  @Override
  protected Set<String> getIncludes() {
    return testIncludes;
  }

  @Override
  protected Set<String> getExcludes() {
    return testExcludes;
  }

  @Override
  protected List<File> getActionScriptCompilePath() {
    List<File> actionScriptCompilePath = getMavenPluginHelper().getActionScriptCompilePath(true);
    actionScriptCompilePath.add(0, getSourceDirectory());
    actionScriptCompilePath.add(0, getGeneratedSourcesDirectory());
    actionScriptCompilePath.add(getCatalogOutputDirectory());
    return actionScriptCompilePath;
  }

  @Override
  protected List<File> getActionScriptClassPath() {
    final List<File> classPath = new ArrayList<>(getMavenPluginHelper().getActionScriptClassPath(true));
    classPath.add(0, getSourceDirectory());
    classPath.add(0, getGeneratedSourcesDirectory());
    classPath.add(getCatalogOutputDirectory());
    return classPath;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!skip) {
      super.execute();
    }
  }

  @Override
  protected boolean findUnusedDependencies(int staleMillis) {
    return false;
  }

  @Override
  protected void printDependencyWarnings(JoocConfiguration joocConfiguration, Map<String, Object> dependencyWarnings) {
    if (joocConfiguration.isFindUnusedDependencies() && dependencyWarnings.get(UNUSED_DEPENDENCIES_KEY) instanceof List) {
      printUnusedDependencyWarnings(joocConfiguration, (List<String>) dependencyWarnings.get(UNUSED_DEPENDENCIES_KEY));
    }
    if (dependencyWarnings.get(UNDECLARED_DEPENDENCIES_KEY) instanceof List) {
      printUndeclaredDependencyWarnings(joocConfiguration, (List<String>) dependencyWarnings.get(UNDECLARED_DEPENDENCIES_KEY), "test-");
    }
  }

  @Override
  protected List<String> createUsedUndeclaredDependencyWarning(Artifact dependency) {
    List<String> lines = new ArrayList<>();
    lines.add("<dependency>");
    lines.add(String.format("  <groupId>%s</groupId>", dependency.getGroupId()));
    lines.add(String.format("  <artifactId>%s</artifactId>", dependency.getArtifactId()));
    lines.add(String.format("  <version>%s</version>", dependency.getVersion()));
    lines.add("  <type>swc</type>");
    lines.add("  <scope>test</scope>");
    lines.add("</dependency>");
    return lines;
  }
}