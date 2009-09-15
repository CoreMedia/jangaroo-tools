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
      testOutputDirectory.mkdir();
      getLog().info("Unpacking jangaroo dependencies to " + testOutputDirectory);
      unpack();
      copyMainJsAndClasses();
      createHtmlPage();
    } catch (IOException e) {
      throw new MojoExecutionException("Cannot unpack jangaroo dependencies/generate html test page", e);
    } catch (ArchiverException e) {
      throw new MojoExecutionException("Cannot unpack jangaroo dependencies/generate html test page", e);
    } catch (ProjectBuildingException e) {
      throw new MojoExecutionException("Cannot unpack jangaroo dependencies/generate html test page", e);
    }
  }

  private void copyMainJsAndClasses() throws IOException {
    FileUtils.copyFileToDirectory(outputFileName, testOutputDirectory);
    FileUtils.copyDirectory(outputDirectory, new File(testOutputDirectory, "classes"));
  }


  public static List<String> sort(Map<String, List<String>> a) {
    List<String> alreadyOut = new LinkedList<String>();
    while (!a.isEmpty()) {
      String start = a.keySet().iterator().next();
      while (a.get(start) != null && !a.get(start).isEmpty()) {
        for (String s : a.get(start)) {
          if (alreadyOut.contains(s)) {
            a.remove(start);
          } else {
            start = s;
            break;
          }

        }
      }
      a.remove(start);
      alreadyOut.add(start);
    }
    return alreadyOut;
  }


  private void createHtmlPage() throws IOException, MojoExecutionException, ProjectBuildingException {
    List<Artifact> dependencies = project.getTestArtifacts();

    List<Artifact> notJangaroo = new LinkedList<Artifact>();
    for (Artifact dependency : dependencies) {
      if (!"jangaroo".equals(dependency.getType())) {
        notJangaroo.add(dependency);
      }
    }
    dependencies.removeAll(notJangaroo);


    final Map<String, List<String>> artifact2Project = new HashMap<String, List<String>>();

    for (Artifact artifact : dependencies) {
      MavenProject mp = mavenProjectBuilder.buildFromRepository(artifact, remoteRepositories, localRepository, true);
      mp.resolveActiveArtifacts();
      List<String> deps = new LinkedList<String>();

      for (Dependency dep : ((List<Dependency>) mp.getDependencies())) {
        if ("jangaroo".equals(dep.getType())) {
          deps.add(dep.getGroupId() + ":" + dep.getArtifactId());
        }

      }
      artifact2Project.put(artifact.getGroupId() + ":" + artifact.getArtifactId(), deps);
    }
    List<String> depsLineralized = sort(artifact2Project);


    testOutputDirectory.mkdir();
    File f = new File(testOutputDirectory, "tests.html");
    FileWriter fw = null;
    try {
      f.createNewFile();
      fw = new FileWriter(f);
      fw.write("<html>\n" +
              "  <head><title>cap-ui-editor Tests</title></head>\n" +
              "  <body>");
      getLog().info("About to sort");


      for (String dependency : depsLineralized) {
        getLog().debug("Dependency: " + dependency);
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

  public void unpack()
          throws IOException, ArchiverException, MojoExecutionException {
    unarchiver.setOverwrite(false);
    unarchiver.setFileSelectors(new FileSelector[]{new FileSelector() {
      public boolean isSelected(FileInfo fileInfo) throws IOException {
        return !fileInfo.getName().startsWith("META-INF");
      }

    }});
    List dependencies = project.getTestArtifacts();


    for (Iterator iterator = dependencies.iterator(); iterator.hasNext();) {
      Artifact dependency = (Artifact) iterator.next();
      getLog().debug("Dependency: " + dependency.getGroupId() + ":" + dependency.getArtifactId() + "type: " + dependency.getType());
      if (!dependency.isOptional() && Types.JANGAROO_TYPE.equals(dependency.getType())) {
        unarchiver.setDestFile(null);
        unarchiver.setDestDirectory(testOutputDirectory);

        //  getLog().info("Unpack jangaroo dependency [" + dependency.toString() + "]");
        unarchiver.setSourceFile(dependency.getFile());
        unarchiver.extract();
        if (unarchiver.getDestDirectory() == null) {
          throw new MojoExecutionException("God damn I don't know why targetDirectory is null!'" + unarchiver.getDestFile());
        }
      }
    }
  }
}
