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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
   * Source directory to scan for test files to compile.
   *
   * @parameter expression="${project.build.testSourceDirectory}"
   */
  private File testSourceDirectory;

  /**
   * Directory where compiles classes (of this module) are placed
   *
   * @parameter expression="${project.build.outputDirectory}"  default-value="${project.build.outputDirectory}"
   */
  protected File outputDirectory;


  /**
   * The (actionscript) test's class name to run, e.g. com.mycompany.MyTestSuite. Needs to be an implementation of
   * flexunit.framework.Test (e.g. flexunit.framework.TestSuite) or needs to provide a static method 'suite' that
   * returns a test suite. If not set, then the test source folder is scanned for tests
   *
   * @parameter
   */
  private String testClass;

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
   *
   * @return The name of the test class
   */
  protected String getTestClassName() {
    return testClass;
  }

  protected File getTestOutputDirectory() {
    return testOutputDirectory;
  }

  protected File getOutputDirectory() {
    return outputDirectory;
  }

  protected MavenProject getProject() {
    return project;
  }

  /**
   * Creates placeholder replacements that will be applied to automatically provided test files
   */
  protected Properties createPlaceholders() throws Exception {

    String testClassNames =  getTestClassName() != null ?
            "[\""+getTestClassName()+"\"]" : // explicit test name
            "[\""+ StringUtils.join(lookupTestClasses().toArray(new String[0]),"\",\"")+"\"]"; // scan for tests

    Properties properties = new Properties();
    properties.setProperty("_module_artifact_id", project.getArtifactId());
    properties.setProperty("_module_group_id", project.getGroupId());
    properties.setProperty("_module_version", project.getVersion());
    properties.setProperty("_test_classnames", testClassNames);

    return properties;
  }

  // ============

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
   * Looks up all test classes that resists in this module
   * @return The fully qualified class names
   */
  protected List<String> lookupTestClasses() throws IOException {

    List<String> classNames = new ArrayList<String>();
    List<String> fileNames = FileUtils.getFileNames(testSourceDirectory, "**/*Test.as", null, false);
    for( String fileName : fileNames ) {

      // cut off ".as" and replace "/" by "."
      String className = fileName.substring(0, fileName.length()-3).replace(File.separatorChar, '.');
      classNames.add(className);
    }

    return classNames;
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
