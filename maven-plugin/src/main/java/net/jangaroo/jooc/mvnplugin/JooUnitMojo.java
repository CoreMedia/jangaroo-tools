/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import org.apache.commons.io.output.TeeOutputStream;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.metadata.ResolutionGroup;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProjectBuilder;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.codehaus.plexus.util.IOUtil;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Function;

import java.io.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @goal test
 * @phase test
 */
public class JooUnitMojo extends AbstractRuntimeMojo {

  private JooRunner jooRunner;

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
   * @component
   * @required
   * @readonly
   */
  protected ArtifactMetadataSource artifactMetadataSource;

  /**
   * @component
   * @required
   * @readonly
   */
  protected MavenProjectBuilder projectBuilder;

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
   * @parameter expression="${project.build.directory}/test-js-classes"
   */
  private File testOutputDirectory;

  /**
   * @parameter expression="${joo.testSuite}"
   * @required
   */
  private String testSuite;

  /**
   * Base directory where all reports are written to.
   *
   * @parameter expression="${project.build.directory}/surefire-reports"
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

  static public class XmlCollector extends ScriptableObject {

    public String xmlReport;
    public CountDownLatch completeSignal;

    public XmlCollector(CountDownLatch completeSignal) {
      this.completeSignal = completeSignal;
    }

    public String getClassName() {
      return "XmlCollector";
    }

    public void collectXml(String xml) {
      this.xmlReport = xml;
      completeSignal.countDown();
    }
  }


  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    //Latch that stops the mojo until all tests are complete
    //and the xml report has been written.
    final CountDownLatch completeSignal = new CountDownLatch(1);

    if (!skipExec && !reportsDirectory.isDirectory()) {
      if (!reportsDirectory.mkdirs()) {
        throw new MojoExecutionException("Cannot create report directry "
            + reportsDirectory.toString());
      }
    }
    //create testsuite Name
    final String testSuiteName = testSuite.substring(testSuite.lastIndexOf('.') + 1);

    // create the joo runner that delegates all output to mojo log
    jooRunner = new JooRunner(new JooRunner.TraceOutputHandler() {
      @Override
      public void print(String input) {
        getLog().info(input);
      }

      @Override
      public void println(String input) {
        getLog().info(input);
      }
    });

    //the collector that recieves the xml report of the JooRunnder
    XmlCollector collector = new XmlCollector(completeSignal);
    collector.defineFunctionProperties(new String[]{"collectXml"}, XmlCollector.class, ScriptableObject.DONTENUM);

    // add the collector to the scope
    jooRunner.addInstanceToScope(collector, "collector");

    //retrieve and load the env dependency
    Artifact env_rhino_js = resolveArtifact("thatcher", "env-rhino-js", null, "zip");
    loadArtifactIntoJavaScriptContext(jooRunner, env_rhino_js, false);

    //load the joo runtime
    Artifact runtimeArtifact = resolveRuntimeArtifact();
    loadArtifactIntoJavaScriptContext(jooRunner, runtimeArtifact, false);

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
      loadArtifactIntoJavaScriptContext(jooRunner, a, true);
    }

    // and load joo unit
    loadArtifactIntoJavaScriptContext(jooRunner, jooUnit, true);

    //load the test suite file
    File test = new File(testOutputDirectory, testSuite.replaceAll("\\.", "//") + ".js");
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
      throw new MojoExecutionException("could not read runtime artifact", e);
    }

    // run the test suite with the xml printer
    jooRunner.run(String.format(" with (joo.classLoader) {\n" +
        "debug = true; \n" +
        "      import_(\"flexunit.textui.TestRunner\");\n" +
        "      import_(\"flexunit.textui.XmlResultPrinter\");\n" +
        "      import_(\"%s\");\n" +
        "      complete(function(imports) {with(imports){\n" +
        "        var xmlWriter = new XmlResultPrinter();" +
        "        TestRunner.run(%s.suite(), function(env){collector.collectXml(xmlWriter.getXml())}, xmlWriter);\n" +
        "        " +
        "      }});\n" +
        "    }", testSuite, testSuiteName));


    //wait for the xml report, created by the javaScript writer
    try {
      completeSignal.await();
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
    try {
      final Xpp3Dom dom = Xpp3DomBuilder.build(new StringReader(collector.xmlReport));
      errors += Integer.parseInt(dom.getAttribute("errors"));
      failures += Integer.parseInt(dom.getAttribute("failures"));
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
    }
  }

  private void loadArtifactIntoJavaScriptContext(JooRunner jooRunner, Artifact artifact, boolean joolib) throws MojoExecutionException, MojoFailureException {
    ZipFile zipArtifact = null;
    try {
      zipArtifact = new ZipFile(artifact.getFile(), ZipFile.OPEN_READ);
      Enumeration<? extends ZipEntry> entries = zipArtifact.entries();
      while (entries.hasMoreElements()) {
        ZipEntry zipEntry = entries.nextElement();
        if (!zipEntry.isDirectory() && zipEntry.getName().endsWith(".js")) {
          if(joolib && !zipEntry.getName().startsWith("joo/lib/"))
            continue;
          getLog().debug(String.format("Loading %s into JavaScript context", zipEntry.getName()));
          BufferedReader reader = new BufferedReader(new InputStreamReader(zipArtifact.getInputStream(zipEntry)));
          jooRunner.load(reader, zipEntry.getName());
        }
      }

    } catch (IOException e) {
      throw new MojoExecutionException("could not read runtime artifact", e);
    } finally {
      try {
        zipArtifact.close();
      } catch (IOException e) {
        //
      }
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
}
