package net.jangaroo.jooc.mvnplugin.test;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.mojo.javascript.archive.Types;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.components.io.fileselectors.FileInfo;
import org.codehaus.plexus.components.io.fileselectors.FileSelector;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Prepares the Javascript Testenvironment including generation of the HTML page and decompression
 * of jangaroo dependencies.
 * This plugin is executed in the <code>generate-test-resources</code> phase of the jangaroo lifecycle.
 *
 * @requiresDependencyResolution test
 * @goal unpack-jangaroo-test-dependencies
 * @phase generate-test-resources
 */
public class JooGenerateTestResourcesMojo extends AbstractJooTestMojo {

  /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;


  /**
   * Plexus archiver.
   *
   * @component role="org.codehaus.plexus.archiver.UnArchiver" role-hint="zip"
   * @required
   */
  private ZipUnArchiver unarchiver;

  /**
   * @parameter expression="${localRepository}"
   * @required
   */
  private ArtifactRepository localRepository;

  /**
   * @parameter expression="${project.remoteArtifactRepositories}"
   * @required
   */
  private List remoteRepositories;

  /**
   * Output directory for compiled classes.
   *
   * @parameter expression="${project.build.outputDirectory}/classes"
   */
  private File outputDirectory;

  /**
   * This parameter specifies the name of the output file containing all
   * compiled classes.
   *
   * @parameter expression="${project.build.outputDirectory}/${project.artifactId}.js"
   */
  private File outputFileName;


  /**
   * @component
   */
  private MavenProjectBuilder mavenProjectBuilder;


  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      if (isTestAvailable()) {
        testOutputDirectory.mkdir();
        getLog().info("Unpacking jangaroo dependencies to " + testOutputDirectory);
        unpack();
        copyMainJsAndClasses();
        createHtmlPage();
      }
    } catch (IOException e) {
      throw new MojoExecutionException("Cannot unpack jangaroo dependencies/generate html test page", e);
    } catch (ArchiverException e) {
      throw new MojoExecutionException("Cannot unpack jangaroo dependencies/generate html test page", e);
    } catch (ProjectBuildingException e) {
      throw new MojoExecutionException("Cannot unpack jangaroo dependencies/generate html test page", e);
    }
  }

  /**
   * Copies the project's jooc javascript output to the <code>testOutputDirectory</code> since they have to be
   * accessible by the javascript execution environment.
   *
   * @throws IOException if copy fails
   */
  private void copyMainJsAndClasses() throws IOException {
    if (outputFileName.exists()) {
      FileUtils.copyFileToDirectory(outputFileName, testOutputDirectory);
    } else {
      getLog().info("Cannot copy " + outputFileName + " to " + testOutputDirectory + ". It does not exist.");
    }
    if (outputDirectory.exists()) {
      FileUtils.copyDirectory(outputDirectory, new File(testOutputDirectory, "classes"));
    } else {
      getLog().info("Cannot copy " + outputDirectory + " to " + testOutputDirectory + ". It does not exist.");
    }
  }


  /**
   * Linearizes the acyclic, directed graph represented by <code>artifact2directDependencies</code> to a list
   * where every item just needs items that are contained in the list before itself.
   *
   * @param artifact2directDependencies acyclic, directed dependency graph
   * @return linearized dependency list
   */
  public static List<String> sort(Map<String, List<String>> artifact2directDependencies) {

    List<String> alreadyOut = new LinkedList<String>();
    while (!artifact2directDependencies.isEmpty()) {
      String currentDep = goDeep(artifact2directDependencies.keySet().iterator().next(), artifact2directDependencies);
      removeAll(currentDep, artifact2directDependencies);
      alreadyOut.add(currentDep);
    }
    return alreadyOut;
  }

  private static String goDeep(String start, Map<String, List<String>> artifact2directDependencies) {
    while (artifact2directDependencies.get(start) != null && !artifact2directDependencies.get(start).isEmpty()) {
      start = artifact2directDependencies.get(start).iterator().next();
    }
    return start;
  }

  private static void removeAll(String toBeRemoved, Map<String, List<String>> artifact2directDependencies) {
    artifact2directDependencies.remove(toBeRemoved);
    
    for (List<String> strings : artifact2directDependencies.values()) {
      strings.remove(toBeRemoved);
    }
  }

  private String getInternalId(Dependency dep) {
    return dep.getGroupId() + ":" + dep.getArtifactId();
  }

  private String getInternalId(Artifact art) {
    return art.getGroupId() + ":" + art.getArtifactId();
  }


  private void createHtmlPage() throws IOException, ProjectBuildingException {
    List<Artifact> testArtifacts = project.getTestArtifacts();

    List<Artifact> notJangaroo = new LinkedList<Artifact>();
    for (Artifact dependency : testArtifacts) {
      if (!"jangaroo".equals(dependency.getType())) {
        notJangaroo.add(dependency);
      }
    }
    testArtifacts.removeAll(notJangaroo);

    final Map<String, List<String>> artifact2Project = new HashMap<String, List<String>>();

    for (Artifact artifact : testArtifacts) {
      MavenProject mp = mavenProjectBuilder.buildFromRepository(artifact, remoteRepositories, localRepository, true);
      List<String> deps = new LinkedList<String>();
      for (Dependency dep : ((List<Dependency>) mp.getDependencies())) {
        if ("jangaroo".equals(dep.getType())) {
          deps.add(getInternalId(dep));
        }
      }
      artifact2Project.put(getInternalId(artifact), deps);
    }
    getLog().debug("artifact2Project : " + artifact2Project);
    List<String> depsLineralized = sort(artifact2Project);
    getLog().debug("depsLineralized  : " + depsLineralized);

    testOutputDirectory.mkdir();
    File f = new File(testOutputDirectory, "tests.html");
    FileWriter fw = null;
    try {
      f.createNewFile();
      fw = new FileWriter(f);
      fw.write("<html>\n" +
              "  <head><title>" + project.getGroupId() + ":" + project.getArtifactId() + ":" + project.getVersion() + "</title></head>\n" +
              "  <body>");


      for (String dependency : depsLineralized) {
        fw.write("<script type=\"text/javascript\" src=\"" + dependency.split(":")[1] + ".js\"></script>\n");
      }
      fw.write("<script type=\"text/javascript\" src=\"" + project.getArtifact().getArtifactId() + ".js\"></script>\n");
      fw.write("  <script type=\"text/javascript\">\n" +
              "    //with (joo.classLoader=new joo.ClassLoader()) { // disable DynamicClassLoader, as all classes are already there!\n" +
              "    with (joo.classLoader) {\n" +
              "      debug = true;\n" +
              "      urlPrefix=\"classes/\";\n" +
              "      classLoadTimeoutMS = 3000;\n" +
              "      import_(\"flexunit.textui.TestRunner\");\n" +
              "      import_(\"" + testSuiteName + "\");\n" +
              "      import_(\"flexunit.textui.XmlResultPrinter\");\n" +
              "      complete(function(imports) {with(imports){\n" +
              "        var xmlWriter = new XmlResultPrinter();\n" +
              "        TestRunner.run(" + testSuiteName.substring(testSuiteName.lastIndexOf(".") + 1) + ".suite(),function(bla) {\n" +
              "            result = xmlWriter.getXml();\n" +
              "            document.getElementById(\"result\").firstChild.nodeValue = result;\n" +
              "        },xmlWriter);\n" +
              "      }});" +
              "    }\n" +
              "  </script>\n" +
              "  <h1>TestResult</h1>\n" +
              "   <pre id=\"result\">\n" +
              "   </pre>\n" +
              " </body>\n" +
              "</html>");
    } finally {
      if (fw != null) {
        try {
          fw.close();
        } catch (IOException e) {
          getLog().error("don't care. Filehandle still open ... ", e);
        }
      }

    }

  }

  /**
   * Unpacks all jangaroo dependencies (transitively) to <code>testOutputDirectory</code>.
   *
   * @throws IOException       if file copy goes wrong
   * @throws ArchiverException if an archive is corrupt
   */
  public void unpack()
          throws IOException, ArchiverException {
    unarchiver.setOverwrite(false);
    unarchiver.setFileSelectors(new FileSelector[]{new FileSelector() {
      public boolean isSelected(FileInfo fileInfo) throws IOException {
        return !fileInfo.getName().startsWith("META-INF");
      }

    }});

    for (Artifact dependency : ((List<Artifact>) project.getTestArtifacts())) {
      getLog().debug("Dependency: " + getInternalId(dependency) + " type: " + dependency.getType());
      if (!dependency.isOptional() && Types.JANGAROO_TYPE.equals(dependency.getType())) {
        unarchiver.setDestFile(null);
        unarchiver.setDestDirectory(testOutputDirectory);
        unarchiver.setSourceFile(dependency.getFile());
        unarchiver.extract();
      }
    }
  }
}
