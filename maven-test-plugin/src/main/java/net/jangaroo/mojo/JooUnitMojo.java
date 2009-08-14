/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.mojo;

import org.apache.commons.io.FilenameUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.metadata.ResolutionGroup;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.jangaroo.util.JooRunner;

/**
 * @goal test
 * @phase test
 */
public class JooUnitMojo extends AbstractMojo {

  private JooRunner jooRunner;

  /**
   * @component
   * @required
   * @readonly
   */
  protected org.apache.maven.artifact.factory.ArtifactFactory artifactFactory;

  /**
   * @component
   * @required
   * @readonly
   */
  protected org.apache.maven.artifact.resolver.ArtifactResolver resolver;

  /**
   * @parameter expression="${project.remoteArtifactRepositories}"
   * @required
   * @readonly
   */
  private java.util.List remoteRepositories;

  /**
   * @parameter expression="${localRepository}"
   * @required
   * @readonly
   */
  protected ArtifactRepository localRepository;

  /**
   * @parameter expression="${plugin.artifacts}"
   * @required
   * @readonly
   */
  private List<Artifact> pluginArtifacts;

  /**
   * The Maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   * @description "The current Maven project"
   */
  private MavenProject project;


  /**
   * Set this to 'true' to bypass unit tests entirely. Its use is NOT RECOMMENDED, but quite
   * convenient on occasion.
   *
   * @parameter expression="${maven.test.skip}"
   */
  private boolean skip;

  /**
   * Set this to 'true' to bypass unit tests execution, but still load them to ensure the
   * syntax. Its use is NOT RECOMMENDED, but quite convenient on occasion.
   *
   * @parameter expression="${maven.test.skip.exec}"
   */
  private boolean skipExec;

  /**
   * Set this to true to ignore a failure during testing. Its use is NOT RECOMMENDED, but
   * quite convenient on occasion.
   *
   * @parameter expression="${maven.test.failure.ignore}"
   */
  private boolean testFailureIgnore;

  /**
   * Set the timeout in minutes
   *
   * @parameter expression=2
   */
  private long jooUnitTimeout;

  /**
   * @component
   * @required
   * @readonly
   */
  protected ArtifactMetadataSource artifactMetadataSource;


  /**
   * List of Remote Repositories used by the resolver
   *
   * @parameter expression="${project.remoteArtifactRepositories}"
   * @readonly
   * @required
   */
  protected java.util.List remoteRepos;

  /**
   * Output directory for compiled classes.
   *
   * @parameter expression="${project.build.directory}/joo/classes"
   */
  private File outputDirectory;

  /**
   * Output directory for compiled classes.
   *
   * @parameter expression="${project.build.directory}/joo-test/classes"
   */
  private File testOutputDirectory;

  /**
   * @parameter expression="JooTestSuite"
   * @optional
   */
  private String testSuite;

  /**
   * Base directory where all reports are written to.
   *
   * @parameter expression="${project.build.directory}/surefire-reports"
   * @required
   */
  private File reportsDirectory;

  /**
   * Option to print summary of test suites or just print the test cases that have errors.
   *
   * @parameter expression="${surefire.printSummary}" default-value="true"
   */
  private boolean printSummary;


  protected File getOutputDirectory() {
    return testOutputDirectory;
  }

  /**
   * Collects the XMl from the FlexUnit run and releases the complete latch.
   */
  public class XmlCollector {

    public String xmlReport;
    public CountDownLatch completeSignal;

    public XmlCollector(CountDownLatch completeSignal) {
      this.completeSignal = completeSignal;
    }

    public String getClassName() {
      return "XmlCollector";
    }

    public void collectXml(String xml) {
      getLog().debug("Collecting XML report...");
      this.xmlReport = xml;
      completeSignal.countDown();
    }
  }

  public class DynamicScriptLoader {
    private JooRunner runner;
    private File[] scriptDirectories;

    public DynamicScriptLoader(JooRunner runner, File[] scriptDirectories) {
      this.runner = runner;
      this.scriptDirectories = scriptDirectories;
    }

    public String getClassName() {
      return "DynamicScriptLoader";
    }

    public void loadScript(String url) {
      getLog().debug("DynamicClassLoader requested " + url);
      boolean loaded = false;
      for (File dir : scriptDirectories) {
        File script = new File(dir, url);
        if (script.canRead()) {
          try {
            FileReader reader = new FileReader(script);
            getLog().debug("DynamicClassLoader is loading script from  " + script.getPath());
            runner.load(reader, url);
            loaded = true;
            break;
          } catch (FileNotFoundException e) {
            getLog().error(e);
            throw new RuntimeException(e);
          } catch (IOException e) {
            getLog().error(e);
            throw new RuntimeException(e);
          }
        }
      }
      if (!loaded) {
        getLog().error("Could not load script: " + url);
        throw new RuntimeException("Could not load script: " + url);
      }
    }
  }


  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    if (!skipExec && !reportsDirectory.exists() && !reportsDirectory.isDirectory()) {
      if (!reportsDirectory.mkdirs()) {
        throw new MojoExecutionException("Cannot create report directry "
                + reportsDirectory.toString());
      }
    }
    File test = new File(testOutputDirectory, testSuite.replaceAll("\\.", "//") + ".js");
    if (test.exists()) {
      //create testsuite Name
      final String testSuiteName = testSuite.substring(testSuite.lastIndexOf('.') + 1);

      // create the joo runner that delegates all output to mojo log
      jooRunner = new JooRunner(new JooRunner.TraceOutputHandler() {
        public void print(String input) {
          getLog().info(input);
        }

        public void println(String input) {
          getLog().info(input);
        }
      });

      //retrieve and load the env dependency
      Artifact env_rhino_js = resolveArtifact("thatcher", "env-rhino-js", null, "zip");
      loadArtifactIntoJavaScriptContext(jooRunner, env_rhino_js, false);

      //load the joo runtime
      /*Artifact runtimeArtifact = resolveRuntimeArtifact();
      loadRuntimeArtifact(jooRunner, runtimeArtifact);
        */
      //retrieve joo unit
      Artifact jooUnit = resolveArtifact("net.jangaroo", "joounit", "lib", "zip");

      if (jooUnit == null) {
        throw new MojoExecutionException(String.format("no jooUnit found"));
      }

      //retrieve all joo unit dependencies
      Set<Artifact> jooUnitDependencies;
      try {
        jooUnitDependencies = resolveTransitively(jooUnit, remoteRepos);
      } catch (Exception e) {
        throw new MojoExecutionException(String.format("could not resolve %s transitively", jooUnit), e);
      }

      //... and load them
      for (Artifact a : jooUnitDependencies) {
        if ("lib".equals(a.getClassifier()) && "zip".equals(a.getType())) {
          loadArtifactIntoJavaScriptContext(jooRunner, a, true);
        }
      }

      // and load joo unit
      loadArtifactIntoJavaScriptContext(jooRunner, jooUnit, true);

      // load all artefacts of this projects that not have already been loaded
      /* Set<Artifact> projectArtifacts = project.getDependencyArtifacts();

      for (Artifact art : projectArtifacts) {
        if (!jooUnitDependencies.contains(art) && "lib".equals(art.getClassifier()) && "zip".equals(art.getType())) {
          loadArtifactIntoJavaScriptContext(jooRunner, art, true);
        }
      }*/

      StringBuffer imports = new StringBuffer();

      //load all classes into javascript context
      /*Collection<File> files = FileUtils.listFiles(outputDirectory, FileFilterUtils.suffixFileFilter("js"), FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter("joo")));
      imports.append(loadJavaScriptClass(files, outputDirectory));     */

      //load all test classes into javascript context
      /*Collection<File> files = FileUtils.listFiles(testOutputDirectory, new String[]{"js"}, true);
      imports.append(loadJavaScriptClass(files, testOutputDirectory));  */

      //load the test suite file

      FileReader testReader = null;
      try {
        testReader = new FileReader(test);
      } catch (FileNotFoundException e) {
        throw new MojoExecutionException("could not read test suite", e);
      }

      // and load it
      try {
        getLog().debug(String.format("Loading test suite %s into JavaScript Context", testSuite));
        jooRunner.load(testReader, testSuite);
      } catch (IOException e) {
        throw new MojoExecutionException(String.format("Could no load test suite %s into JavaScript Context", testSuite), e);
      }
      imports.append(String.format("      import_(\"%s\");\n", testSuite));

      DynamicScriptLoader loader = new DynamicScriptLoader(jooRunner, new File[]{outputDirectory, testOutputDirectory});
      jooRunner.addInstanceToScope(loader, "loader");

      //Latch that stops the mojo until all tests are complete
      //and the xml report has been written.
      final CountDownLatch completeSignal = new CountDownLatch(1);

      //the collector that recieves the xml report of the JooRunnder
      XmlCollector collector = new XmlCollector(completeSignal);

      // add the collector to the scope
      jooRunner.addInstanceToScope(collector, "collector");

      // run the test suite with the xml printer
      jooRunner.run(String.format("" +
              "with (joo.classLoader) {\n" +
              "urlPrefix = \"\";\n" +
              "loadScript = function(url){loader.loadScript(url);};\n" +
              "js.XMLHttpRequest = XMLHttpRequest;\n" +
              "debug = true; \n" +
              "      import_(\"flexunit.textui.TestRunner\");\n" +
              "      import_(\"flexunit.textui.XmlResultPrinter\");\n" +
              imports.toString() +
              "      complete(function(imports) {with(imports){\n" +
              "        trace(\"start complete....\");" +
              "        var xmlWriter = new XmlResultPrinter();\n" +
              "        TestRunner.run(%s.suite(), function(env){trace(\"Tests completed...\");collector.collectXml(xmlWriter.getXml());}, xmlWriter);\n" +
              "        " +
              "      }});\n" +
              "    }", testSuiteName));


      //wait for the xml report, created by the javaScript writer
      try {
        if (!completeSignal.await(jooUnitTimeout, TimeUnit.MINUTES)) {
          throw new MojoExecutionException("Testrun timeout");
        }
      } catch (InterruptedException e) {
        throw new MojoExecutionException("unknown error", e);
      }

      //write the report
      final File file = new File(reportsDirectory, "TEST-" + testSuiteName + ".xml");
      Writer writer = null;
      try {
        writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write(collector.xmlReport);
        writer.close();
      } catch (final IOException e) {
        throw new MojoExecutionException("Cannot create file " + file.getName(), e);
      } finally {
        IOUtil.close(writer);
      }

      //evaluate the result

      int errors = 0;
      int failures = 0;
      int tests = 0;
      long time = 0;
      try {
        final Xpp3Dom dom = Xpp3DomBuilder.build(new StringReader(collector.xmlReport));
        errors += Integer.parseInt(dom.getAttribute("errors"));
        failures += Integer.parseInt(dom.getAttribute("failures"));
        tests += Integer.parseInt(dom.getAttribute("tests"));
        time += Long.parseLong(dom.getAttribute("time"));
      } catch (XmlPullParserException e) {
        throw new MojoExecutionException("Cannot parse report of test suite " + testSuite, e);
      } catch (IOException e) {
        throw new MojoExecutionException("Cannot read report of test suite " + testSuite, e);
      }
      if (errors + failures > 0) {
        final String msg = "There have been "
                + errors
                + " errors and "
                + failures
                + " failures testing JavaScript";
        if (testFailureIgnore) {
          getLog().error(msg);
        } else {
          throw new MojoFailureException(msg);
        }
      } else if (printSummary) {
        getLog().info(" ");
        getLog().info("Jangaroo Test results:");
        getLog().info(String.format("%s tests have been executed. That took %s ms.", tests, time));
        getLog().info(" ");
      }
    } else {
      getLog().error("No TestSuite exists skipping tests");
    }
  }

  private String loadJavaScriptClass(Collection<File> files, File baseDirectory) throws MojoExecutionException {
    FileReader testReader = null;
    StringBuffer imports = new StringBuffer();
    for (File testFile : files) {
      try {
        imports.append(String.format("      import_(\"%s\");\n", getQualifiedClassname(baseDirectory, testFile)));
        testReader = new FileReader(testFile);
        getLog().info(String.format("Loading class %s into JavaScript Context", getRelativePath(baseDirectory, testFile)));
        jooRunner.load(testReader, testFile.getName());
      } catch (FileNotFoundException e) {
        throw new MojoExecutionException("could not read JavaScript class", e);
      } catch (IOException e) {
        throw new MojoExecutionException("could not read JavaScript class", e);
      }
    }
    return imports.toString();
  }

  private String getQualifiedClassname(File baseDirectory, File file) {
    String name = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(".js"));
    name = FilenameUtils.separatorsToUnix(name).replaceAll("/", "\\.");
    name = name.substring(name.indexOf(baseDirectory.getName()) + baseDirectory.getName().length() + 1, name.length());
    return name;
  }

  private String getRelativePath(File baseDirectory, File file) {
    return file.getPath().substring(file.getPath().indexOf(baseDirectory.getName()) + baseDirectory.getName().length() + 1, file.getPath().length());
  }

  private void loadRuntimeArtifact(JooRunner jooRunner, Artifact runtimeArtifact) throws MojoExecutionException {
    ZipFile zipArtifact = null;
    try {
      zipArtifact = new ZipFile(runtimeArtifact.getFile(), ZipFile.OPEN_READ);
      ZipEntry zipEntry = zipArtifact.getEntry("joo-debug.js");
      if (zipEntry != null) {
        getLog().debug(String.format("Loading %s into JavaScript context", zipEntry.getName()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(zipArtifact.getInputStream(zipEntry)));
        jooRunner.load(reader, zipEntry.getName());
      } else {
        throw new IOException();
      }
    } catch (IOException e) {
      throw new MojoExecutionException("could not read runtime artifact", e);
    } finally {
      try {
        if (zipArtifact != null) {
          zipArtifact.close();
        }
      } catch (IOException e) {
        //
      }
    }
  }

  private void loadArtifactIntoJavaScriptContext(JooRunner jooRunner, Artifact artifact, boolean joolib) throws MojoExecutionException, MojoFailureException {
    ZipFile zipArtifact = null;
    boolean loaded = false;
    try {
      zipArtifact = new ZipFile(artifact.getFile(), ZipFile.OPEN_READ);
      Enumeration<? extends ZipEntry> entries = zipArtifact.entries();
      while (entries.hasMoreElements()) {
        ZipEntry zipEntry = entries.nextElement();
        if (!zipEntry.isDirectory() && zipEntry.getName().endsWith(".js")) {
          if (joolib && !zipEntry.getName().startsWith("joo/lib/")) {
            continue;
          }
          getLog().debug(String.format("Loading %s into JavaScript context", zipEntry.getName()));
          BufferedReader reader = new BufferedReader(new InputStreamReader(zipArtifact.getInputStream(zipEntry)));
          jooRunner.load(reader, zipEntry.getName());
          loaded = true;
        }
      }

    } catch (IOException e) {
      throw new MojoExecutionException("could not read artifact", e);
    } finally {
      try {
        zipArtifact.close();
      } catch (IOException e) {
        //
      }
    }
    if (!loaded) {
      throw new MojoExecutionException("could not read artifact: " + artifact);
    }
  }

  private Set<Artifact> resolveTransitively(Artifact artifact, final List<ArtifactRepository> repositories)
          throws Exception {

    ResolutionGroup resolutionGroup = artifactMetadataSource.retrieve(artifact,
            this.localRepository, repositories);
    repositories.addAll(resolutionGroup.getResolutionRepositories());

    Set<Artifact> dependencies = resolutionGroup.getArtifacts();
    dependencies.add(artifact);
    ArtifactResolutionResult resolution =
            this.resolver.resolveTransitively(
                    dependencies,
                    artifact, Collections.EMPTY_MAP,
                    this.localRepository,
                    repositories,
                    this.artifactMetadataSource, null,
                    Collections.EMPTY_LIST);

    Set<Artifact> result = resolution.getArtifacts();
    if (result == null || result.isEmpty()) {
      throw new ArtifactNotFoundException("Artifact not found.",
              artifact);
    }
    return result;
  }

  protected Artifact resolveArtifact(String groupId, String artifactId, String classifier, String type)  throws MojoFailureException, MojoExecutionException {
    Artifact result = null;
    for (Artifact pluginArtifact : pluginArtifacts) {
        getLog().debug("Inspecting pluginArtifact: " + pluginArtifact);
        if (groupId.equals(pluginArtifact.getGroupId()) &&
          artifactId.equals(pluginArtifact.getArtifactId()) &&
          (classifier == null || classifier.equals(pluginArtifact.getClassifier())) &&
          type.equals(pluginArtifact.getType())) {

          getLog().debug("Selected pluginArtifact: " + pluginArtifact);
          result = pluginArtifact;
          break;
        }
      }
    if (result == null) {
      throw new MojoExecutionException(String.format(
        "Cannot find any version of the required artifact among the plugin artifacts: %s:%s:%s:%s",
        groupId, artifactId, classifier, type));
    }

    getLog().debug("Using Jangaroo artifact: " + result);

    try {
      resolver.resolve( result, remoteRepositories, localRepository );
    } catch (Exception e) {
      throw new MojoExecutionException("Cannot resolve artifact", e);
    }

    return result;
  }
}
