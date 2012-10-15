package net.jangaroo.jooc.mvnplugin.test;


import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.resolution.ArtifactRequest;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Executes unit tests using PhantomJS
 *
 * @requiresDependencyResolution test
 * @goal run-test-phantomjs
 * @phase test
 */
public class PhantomJsTestMojo extends AbstractMojo {

  public static enum Mode {
    direct,
    html
  }

  private static final String RESOURCE_DIRECTORY = "META-INF/resources/";

  /**
   * @parameter default-value="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;

  /**
   * @component role="org.codehaus.plexus.archiver.UnArchiver" roleHint="jar"
   */
  //private UnArchiver archiver;

  /**
   * The entry point to Aether, i.e. the component doing all the work.
   *
   * @component
   */
  @SuppressWarnings({"UnusedDeclaration"})
  private RepositorySystem repoSystem;

  /**
   * The current repository/network configuration of Maven.
   *
   * @parameter default-value="${repositorySystemSession}"
   * @readonly
   */
  private RepositorySystemSession repoSession;

  /**
   * Source directory to scan for files to compile.
   *
   * @parameter expression="${project.build.testSourceDirectory}"
   */
  //private File testSourceDirectory;

  /**
   * Directory where all test resources including the compiled tests itself are placed
   *
   * @parameter expression="${project.build.testOutputDirectory}"  default-value="${project.build.testOutputDirectory}"
   */
  protected File testOutputDirectory;

  /**
   * Directory where compiles classes (of this module) are placed
   *
   * @parameter expression="${project.build.outputDirectory}"  default-value="${project.build.outputDirectory}"
   */
  protected File outputDirectory;

  /**
   * Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite
   * convenient on occasion.
   *
   * @parameter expression="${skipTests}"
   */
  private boolean skipTests;

  /**
   * Output directory for test results.
   *
   * @parameter expression="${project.build.directory}/surefire-reports/"  default-value="${project.build.directory}/surefire-reports/"
   * @required
   */
  private File testResultOutputDirectory;


  /**
   * The (actionscript) test's class name to run, e.g. com.mycompany.MyTestSuite. Needs to be an implementation of
   * flexunit.framework.Test (e.g. flexunit.framework.TestSuite)
   *
   *
   * @parameter
   * @required
   */
  private String test;

  /**
   * Time in milliseconds to wait for the test to finish. Default is 30000ms.
   *
   * @parameter
   */
  private int timeout = 30000;

  /**
   * Location of the PhantomJS executable
   * @parameter expression="${phantomjs.executable}" default-value="phantomjs"
   * @required
   */
  private String executable;

  /**
   * Additional arguments to be passed to phantomjs. Expected to be a JSON object, i.e.
   * <code>
   * {loglevel:'all'}
   * </code>
   *
   * @parameter expression="${phantomjs.args}" default-value="{loglevel:'all'}"
   */
  private String args;


  /**
   * The execution mode: Either directly ("direct") or embedded into a html page ("html")
   * @parameter default-value="direct"
   */
  private Mode mode;


  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    try {

      unpackResources();
      runTests();
    }
    catch (MojoFailureException e) {
      throw e;
    }
    catch (MojoExecutionException e) {
      throw e;
    }
    catch (Exception e) {
      throw new MojoExecutionException(e.toString(), e);
    }
  }

  // ===========================================

  private void runTests() throws Exception {

    if( skipTests ) {
      getLog().info("Skipping tests");
      return;
    }

    String bootstrapScript = mode == Mode.direct ? "joo/phantomjs-joounit-runner.js" : "joo/phantomjs-joounit-page-runner.js";

    PhantomJsTestRunner runner = new PhantomJsTestRunner(executable, testOutputDirectory, bootstrapScript, test, args, timeout, getLog());
    getLog().info("trying to run phantomjs first: " + runner.toString());
    if (runner.isTestAvailable() && runner.canRun()) {

      boolean exitCode = runner.execute();
      String testResultXml = runner.getTestResult();
      writeResultToFile(test, testResultXml);
      evalTestOutput(testResultXml);
      if (!exitCode) {
        throw new MojoFailureException("There are test failures");
      }
    }
  }

  private void writeResultToFile(String testSuite, String testResultXml) throws IOException {
    File result = new File(testResultOutputDirectory, "TEST-"+testSuite+".xml");
    FileUtils.writeStringToFile(result, testResultXml);
  }

  private void evalTestOutput(String testResultXml) throws ParserConfigurationException, IOException, SAXException, MojoFailureException {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
    StringReader inStream = new StringReader(testResultXml);
    InputSource inSource = new InputSource(inStream);
    Document d = dBuilder.parse(inSource);
    NodeList nl = d.getChildNodes();
    NamedNodeMap namedNodeMap = nl.item(0).getAttributes();
    final String failures = namedNodeMap.getNamedItem("failures").getNodeValue();
    final String errors = namedNodeMap.getNamedItem("errors").getNodeValue();
    final String tests = namedNodeMap.getNamedItem("tests").getNodeValue();
    final String time = namedNodeMap.getNamedItem("time").getNodeValue();
    final String name = namedNodeMap.getNamedItem("name").getNodeValue();
    getLog().info(name + " tests run: " + tests + ", Failures: " + failures + ", Errors: " + errors + ", time: " + time + " ms");
    if (Integer.parseInt(errors) > 0 || Integer.parseInt(failures) > 0) {
      getLog().info(testResultXml);
      throw new MojoFailureException("There are test failures");
    }
  }


  /**
   * Unpacks all (jangaroo) resources from below /META-INF/resources from all depending artifacts
   */
  private void unpackResources() throws IOException {

    CountingPrefixRemovingUnArchiver unArchiver = new CountingPrefixRemovingUnArchiver(RESOURCE_DIRECTORY, getLog());

    // 1.) get the files of all transitive dependencies
    List<File> files = new ArrayList<File>();
    Set<org.apache.maven.artifact.Artifact> dependencies = project.getArtifacts();
    for( org.apache.maven.artifact.Artifact d : dependencies ) {

      getLog().debug("Found dependency "+d);
      if( d.getType() == null || d.getType().equalsIgnoreCase("jar") ) {

        File file = resolve(new DefaultArtifact(d.getGroupId(), d.getArtifactId(), d.getClassifier(), d.getType(), d.getVersion()));
        if( file != null ) {
          files.add(file);
        }
      }
    }

    // 2.) extract all resources from this module
    testOutputDirectory.mkdirs();
    FileUtils.copyDirectory(new File(outputDirectory, RESOURCE_DIRECTORY), testOutputDirectory);


    // 3.) extract all resources from all dependencies (represented by the files)
    for( File file : files ) {

      unArchiver.setSourceFile(file);
      try {

        unArchiver.extract(RESOURCE_DIRECTORY, testOutputDirectory);
        int count = unArchiver.getCount();
        if( count > 0 ) {
          getLog().info("Extracted "+count+" resources from "+file.getPath()+" (from below "+RESOURCE_DIRECTORY+") to "+testOutputDirectory);
        }
        else {
          getLog().debug("No resources found in " + file.getPath() + " below " + RESOURCE_DIRECTORY);
        }
      }
      catch (ArchiverException e1) {
        getLog().warn("Error extracting resources from " + file.getAbsolutePath(), e1);
      }
    }
  }

  /**
   * Resolves an dependency and provides the dependency artifact's file
   * @param artifact The dependency
   * @return The file or null
   */
  private File resolve(Artifact artifact) {

    ArtifactRequest request = new ArtifactRequest(artifact, null, null);
    try {

      final List<ArtifactResult> ars = repoSystem.resolveArtifacts(repoSession, Collections.singletonList(request));
      if( ars.size() != 1 ) {
        getLog().warn("Error resolving artifact "+artifact+": "+ars);
        return null;
      }
      else {

        Artifact downloadedArtifact = ars.get(0).getArtifact();
        File file = downloadedArtifact.getFile();
        getLog().debug("Resolved artifact " + artifact + " to " + file.getAbsolutePath());

        return file;
      }
    }
    catch(ArtifactResolutionException e) {
      getLog().warn("Error resolving artifact "+artifact, e);
      return null;
    }
  }


  // =======

  /**
   * An extension of the ZipUnArchiver that removes a prefix from the path of the files to be extracted. It
   * also counts the number of extracted files.
   */
  private static class CountingPrefixRemovingUnArchiver extends ZipUnArchiver {

    private final String prefix;
    private final Log log;
    private int count;

    public CountingPrefixRemovingUnArchiver(String prefix, Log log) {
      this.prefix = prefix;
      this.log = log;
    }

    public int getCount() {
      return count;
    }

    @Override
    protected void execute(String path, File outputDirectory) throws ArchiverException {
      count = 0;
      super.execute(path, outputDirectory);
    }

    @Override
    protected void execute() throws ArchiverException {
      count = 0;
      super.execute();
    }

    @Override
    protected void extractFile(File srcF, File dir, InputStream compressedInputStream, String entryName, Date entryDate, boolean isDirectory, Integer mode) throws IOException, ArchiverException {

      String adjustedEntryName = entryName;
      if( adjustedEntryName.startsWith(prefix) ) {
        adjustedEntryName = adjustedEntryName.substring(prefix.length());
      }
      if( adjustedEntryName.length() == 0 ) {
        // it's the directory that is denoted by the prefix
        return;
      }
      if( log.isDebugEnabled() ) {
        log.debug("Extracting "+adjustedEntryName);
      }
      super.extractFile(srcF, dir, compressedInputStream, adjustedEntryName, entryDate, isDirectory, mode);
    }
  }
}
