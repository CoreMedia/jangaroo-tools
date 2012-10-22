package net.jangaroo.jooc.mvnplugin.test;


import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
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
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Base implementation for a mojo that executes jangaroo based unit tests
 */
public abstract class TestMojoBase extends AbstractMojo {

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
  @SuppressWarnings({"UnusedDeclaration"})
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
  @SuppressWarnings({"UnusedDeclaration"})
  private File testOutputDirectory;

  /**
   * Source directory to scan for test files to compile.
   *
   * @parameter expression="${project.build.testSourceDirectory}"
   */
  @SuppressWarnings({"UnusedDeclaration"})
  private File testSourceDirectory;

  /**
   * Directory where compiles classes (of this module) are placed
   *
   * @parameter expression="${project.build.outputDirectory}"  default-value="${project.build.outputDirectory}"
   */
  @SuppressWarnings({"UnusedDeclaration"})
  private File outputDirectory;


  /**
   * A comma separated list of (actionscript) test classes to execute, e.g. com.mycompany.MyTestSuite. Every
   * test class needs to be either an implementation of
   * flexunit.framework.Test (e.g. flexunit.framework.TestSuite) or needs to provide a static method 'suite' returning
   * a test suite instance.
   * If not set, then the test source folder is scanned for tests
   *
   * @parameter
   */
  @SuppressWarnings({"UnusedDeclaration"})
  private String testClassNames;

  /**
   * Output directory for test results.
   *
   * @parameter expression="${project.build.directory}/surefire-reports/"  default-value="${project.build.directory}/surefire-reports/"
   * @required
   */
  @SuppressWarnings({"UnusedDeclaration"})
  private File testResultOutputDirectory;

  /**
   * Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite
   * convenient on occasion.
   *
   * @parameter expression="${skipTests}"
   */
  @SuppressWarnings({"UnusedDeclaration"})
  private boolean skipTests;


  // ====================



  /**
   * Unpacks all (jangaroo) resources from below /META-INF/resources from all depending artifacts
   */
  protected void unpackResources() throws IOException {

    CountingPrefixRemovingUnArchiver unArchiver = new CountingPrefixRemovingUnArchiver(RESOURCE_DIRECTORY, getLog());

    // 1.) get the files of all transitive dependencies
    List<File> files = new ArrayList<File>();
    Set<Artifact> dependencies = project.getArtifacts();
    for( org.apache.maven.artifact.Artifact d : dependencies ) {

      getLog().debug("Found dependency "+d);
      if( d.getType() == null || d.getType().equalsIgnoreCase("jar") ) {

        File file = resolve(new DefaultArtifact(d.getGroupId(), d.getArtifactId(), d.getClassifier(), d.getType(), d.getVersion()));
        if( file != null ) {
          files.add(file);
        }
      }
    }

    // 2.) copies all resources from this module
    testOutputDirectory.mkdirs();
    // note: plexus' FileUtils.copyDirectory flattens the directory. Thus, commons-io version is used here.
    org.apache.commons.io.FileUtils.copyDirectory(new File(outputDirectory, RESOURCE_DIRECTORY), testOutputDirectory);


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
   * @return The directory where all test classes of this module are stored
   */
  protected File getTestOutputDirectory() {
    return testOutputDirectory;
  }

  /**
   * @return The directory where classes of this module are stored
   */
  protected File getOutputDirectory() {
    return outputDirectory;
  }

  protected MavenProject getProject() {
    return project;
  }

  protected File getTestResultOutputDirectory() {
    return testResultOutputDirectory;
  }

  protected boolean isSkipTests() {
    return skipTests;
  }



  /**
   * Creates placeholder replacements that will be applied to automatically provided test files
   */
  protected Properties createPlaceholders() throws Exception {

    String testClassNamesJSON = "[\""+ StringUtils.join(getTestClassNames().toArray(new String[0]),"\",\"")+"\"]";

    Properties properties = new Properties();
    properties.setProperty("_module_artifact_id", project.getArtifactId());
    properties.setProperty("_module_group_id", project.getGroupId());
    properties.setProperty("_module_version", project.getVersion());
    properties.setProperty("_test_classnames", testClassNamesJSON);

    return properties;
  }


  /**
   * @return The fully qualified actionscript class names of all test classes
   */
  protected List<String> getTestClassNames() throws IOException {

    if( testClassNames != null ) {

      // test classes have been explicitly configured
      List<String> result = new ArrayList<String>();
      StringTokenizer tok = new StringTokenizer(testClassNames, ",");
      while( tok.hasMoreTokens() ) {
        result.add(tok.nextToken().trim());
      }
      return result;
    }
    else {

      // not configured. perform a lookup
      return lookupTestClasses();
    }
  }

  /**
   * Copies a template (encoded as UTF-8) to a file and replaces certain tokens
   * @param source The template stream. will be closed by this method
   */
  protected void writeTemplate(InputStream source, File target, Properties tokens, boolean overwrite) throws IOException{

    // -- 1. read in source
    StringBuilder sourceBuilder = new StringBuilder();

    InputStreamReader reader = new InputStreamReader(source, "UTF-8");
    char[] buffer = new char[1024];
    int length;
    while( (length = reader.read(buffer)) > -1 ) {
      sourceBuilder.append(buffer, 0, length);
    }
    source.close();
    String sourceString = sourceBuilder.toString();

    // -- 2. replace tokens
    for( String name : tokens.stringPropertyNames() ) {
      sourceString = sourceString.replace(name, tokens.getProperty(name));
    }

    // -- 3. write template

    if( overwrite && target.exists() ) {
      target.delete();
    }
    if( !target.exists() ) {
      getLog().info("Writing " + target + " ...");
      FileUtils.fileWrite(target.getAbsolutePath(), sourceString);
    }
    else {
      // Users might add a file to src/test/resources that has precedence
      getLog().info("File " + target + " already exists. Skipping.");
    }
  }


  /**
   * Writes the test result to the file system
   * @param testResultXml The test result xml
   * @throws IOException
   */
  protected void writeTestResult(String testName, String testResultXml) throws IOException {
    File result = new File(getTestResultOutputDirectory(), "TEST-"+testName+".xml");
    org.apache.commons.io.FileUtils.writeStringToFile(result, testResultXml);
  }

  /**
   * Parses the test result from xml into an object
   */
  protected TestResult parseTestResult(String testResultXml) throws ParserConfigurationException, IOException, SAXException {

    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
    StringReader inStream = new StringReader(testResultXml);
    InputSource inSource = new InputSource(inStream);
    Document d = dBuilder.parse(inSource);
    NodeList nl = d.getChildNodes();
    NamedNodeMap namedNodeMap = nl.item(0).getAttributes();


    TestResult result = new TestResult();
    result.setFailures(Integer.parseInt(namedNodeMap.getNamedItem("failures").getNodeValue()));
    result.setErrors(Integer.parseInt(namedNodeMap.getNamedItem("errors").getNodeValue()));
    result.setTests(Integer.parseInt(namedNodeMap.getNamedItem("tests").getNodeValue()));
    result.setTime(Integer.parseInt(namedNodeMap.getNamedItem("time").getNodeValue()));
    result.setName(namedNodeMap.getNamedItem("name").getNodeValue());

    // TODO parse children as well

    return result;

  }


  // ==========================

  /**
   * Looks up all test classes that resists in this module
   * @return The fully qualified class names
   */
  private List<String> lookupTestClasses() throws IOException {

    List<String> classNames = new ArrayList<String>();
    List<String> fileNames = FileUtils.getFileNames(testSourceDirectory, "**/*Test.as", null, false);
    for( String fileName : fileNames ) {

      // cut off ".as" and replace "/" by "."
      String className = fileName.substring(0, fileName.length()-3).replace(File.separatorChar, '.');
      classNames.add(className);
    }

    return classNames;
  }

  /**
   * Resolves an dependency and provides the dependency artifact's file
   * @param artifact The dependency
   * @return The file or null
   */
  private File resolve(org.sonatype.aether.artifact.Artifact artifact) {

    ArtifactRequest request = new ArtifactRequest(artifact, null, null);
    try {

      final List<ArtifactResult> ars = repoSystem.resolveArtifacts(repoSession, Collections.singletonList(request));
      if( ars.size() != 1 ) {
        getLog().warn("Error resolving artifact "+artifact+": "+ars);
        return null;
      }
      else {

        org.sonatype.aether.artifact.Artifact downloadedArtifact = ars.get(0).getArtifact();
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


  // ====================

  /**
   * A test result representation
   */
  protected static class TestResult {

    private String name;
    private int failures;
    private int errors;
    private int tests;
    private int time;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getFailures() {
      return failures;
    }

    public void setFailures(int failures) {
      this.failures = failures;
    }

    public int getErrors() {
      return errors;
    }

    public void setErrors(int errors) {
      this.errors = errors;
    }

    public int getTests() {
      return tests;
    }

    public void setTests(int tests) {
      this.tests = tests;
    }

    public int getTime() {
      return time;
    }

    public void setTime(int time) {
      this.time = time;
    }
  }










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
