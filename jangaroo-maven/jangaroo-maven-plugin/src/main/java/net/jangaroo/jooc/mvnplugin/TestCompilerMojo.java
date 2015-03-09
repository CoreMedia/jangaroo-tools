package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.utils.CompilerUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;

/**
 * Mojo to compile Jangaroo sources from during the test-compile phase.
 *
 * @goal testCompile
 * @phase test-compile
 * @requiresDependencyResolution test
 * @threadSafe
 */
@SuppressWarnings({"UnusedDeclaration", "UnusedPrivateField"})
public class TestCompilerMojo extends AbstractCompilerMojo {

  /**
   * Output directory for all generated ActionScript3 test files to compile.
   *
   * @parameter expression="${project.build.directory}/generated-test-sources/joo"
   */
  private File generatedTestSourcesDirectory;

  /**
   * Test output directory into whose amd/as3 sub-directory compiled classes are generated.
   *
   * @parameter expression="${project.build.testOutputDirectory}"
   */
  private File testOutputDirectory;

  /**
   * Location of Jangaroo test resources of this module (including compiler output, usually under "joo/") to be added
   * to the webapp. This property is used for <code>war</code> packaging type (actually, all packaging types
   * but <code>jangaroo</code>) as {@link #getOutputDirectory}.
   * Defaults to ${project.build.directory}/jangaroo-test-output/
   *
   * @parameter expression="${project.build.directory}/jangaroo-test-output/"
   */
  private File testPackageSourceDirectory;

  /**
   * Source directory to scan for files to compile.
   *
   * @parameter expression="${project.build.sourceDirectory}"
   */
  private File sourceDirectory;

  /**
   * Source directory to scan for test files to compile.
   *
   * @parameter expression="${project.build.testSourceDirectory}"
   */
  private File testSourceDirectory;

  /**
   * A list of test inclusion filters for the compiler.
   *
   * @parameter
   */

  private Set<String> testIncludes = new HashSet<String>();
  /**
   * A list of test exclusion filters for the compiler.
   *
   * @parameter
   */

  private Set<String> testExcludes = new HashSet<String>();

  /**
   * Temporary output directory for compiled classes to be packaged into a single *.js file.
   *
   * @parameter expression="${project.build.directory}/temp/jangaroo-test-output/classes"
   */
  private File tempTestClassesOutputDirectory;

  /**
   * This parameter specifies the path and name of the output file containing all
   * compiled classes, relative to the testOutputDirectory.
   *
   * @parameter expression="joo/${project.groupId}.${project.artifactId}-test.classes.js"
   */
  private String moduleTestClassesJsFile;

  /**
   * The fully-qualified name of the TestSuite to run.
   *
   * @parameter
   */
  private String testSuite;

  /**
   * Set this to 'true' to bypass unit tests entirely. Its use is NOT RECOMMENDED, especially if you
   * enable it using the "maven.test.skip" property, because maven.test.skip disables both running the
   * tests and compiling the tests. Consider using the skipTests parameter instead.
   *
   * @parameter expression="${maven.test.skip}"
   */
  protected boolean skip;

  /**
   * @component
   */
  @SuppressWarnings("UnusedDeclaration")
  private MavenProjectBuilder mavenProjectBuilder;

  /**
   * @parameter expression="${localRepository}"
   * @required
   */
  @SuppressWarnings("UnusedDeclaration")
  private ArtifactRepository localRepository;

  /**
   * @parameter expression="${project.remoteArtifactRepositories}"
   * @required
   */
  @SuppressWarnings("UnusedDeclaration")
  private List remoteRepositories;

  /**
   * 
   * @return null as API stub generation does not make sense for test sources
   */
  @Override
  protected File getApiOutputDirectory() {
    return null;
  }

  protected List<File> getCompileSourceRoots() {
    return Arrays.asList(testSourceDirectory, generatedTestSourcesDirectory);
  }

  protected File getOutputDirectory() {
    return isJangarooPackaging() ? testOutputDirectory : testPackageSourceDirectory;
  }


  protected File getTempClassesOutputDirectory() {
    return tempTestClassesOutputDirectory;
  }

  @Override
  protected Set<String> getIncludes() {
    return testIncludes;
  }

  @Override
  protected Set<String> getExcludes() {
    return testExcludes;
  }

  public String getModuleClassesJsFileName() {
    return moduleTestClassesJsFile;
  }

  @Override
  protected List<File> getActionScriptClassPath() {
    final List<File> classPath = new ArrayList<File>(getMavenPluginHelper().getActionScriptClassPath(true));
    classPath.add(0, sourceDirectory);
    classPath.add(0, getGeneratedSourcesDirectory());
    return classPath;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if(!skip) {
      super.execute();
      if (testSuite != null) {
        generateTestsAMD();
      }
    }
  }

  private void generateTestsAMD() throws MojoExecutionException {
    File testsAmdFile = new File(testOutputDirectory, "amd/tests.js");
    getLog().info("  generating tests.js AMD file based on Maven dependencies...");
    Writer amdWriter = null;
    try {
      amdWriter = new OutputStreamWriter(new FileOutputStream(testsAmdFile), "UTF-8");
      amdWriter.write(String.format("define(\"as3/__TestSuite\", [\n" +
              "  \"as3/net/jangaroo/joounit/runner/BrowserRunner\",\n" +
              "  \"as3/%s\"\n" +
              "], function(BrowserRunner, TestSuite) {\n" +
              "  return {_: {main: function() {\n" +
              "        return BrowserRunner._.main(TestSuite._);\n" +
              "      }}};\n" +
              "});\n", testSuite.replace('.', '/')));
      Artifact artifact = getProject().getArtifact();
      String amdLibName = GenerateModuleAMDMojo.computeAMDName(artifact.getGroupId(), artifact.getArtifactId());
      String amdTestLibName = amdLibName + "-test";
      amdWriter.write(String.format("define(%s, [\n", CompilerUtils.quote(amdTestLibName)));
      amdWriter.write("  " + CompilerUtils.quote(amdLibName));
      List<String> testDependencies = getTestDependencies(artifact);
      for (String testDependency : testDependencies) {
        amdWriter.write(",\n");
        amdWriter.write("  " + CompilerUtils.quote(testDependency));
      }
      amdWriter.write("\n], function() {});\n");
      amdWriter.write("require([\"run!" + amdTestLibName + "!__TestSuite\"]);\n");
      amdWriter.close();
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to create generated AMD output file.", e);
    } catch (ProjectBuildingException e) {
      throw new MojoExecutionException("Failed to collect dependencies.", e);
    } finally {
      if (amdWriter != null) {
        try {
          amdWriter.close();
        } catch (IOException e) {
          // so what? ignore
        }
      }
    }
  }

  private List<String> getTestDependencies(Artifact artifact) throws ProjectBuildingException {
    MavenProject mp = mavenProjectBuilder.buildFromRepository(artifact, remoteRepositories, localRepository, true);
    List<String> deps = new LinkedList<String>();
    for (Dependency dep : GenerateModuleAMDMojo.getDependencies(mp)) {
      if ("jar".equals(dep.getType()) && dep.getScope().equals("test")) {
        deps.add(GenerateModuleAMDMojo.computeAMDName(dep.getGroupId(), dep.getArtifactId()));
      }
    }
    return deps;
  }

}