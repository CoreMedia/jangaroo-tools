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
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;
import org.codehaus.mojo.javascript.archive.Types;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.components.io.fileselectors.FileInfo;
import org.codehaus.plexus.components.io.fileselectors.FileSelector;

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
public class JooGenerateTestResourcesMojo extends AbstractMojo {

  /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;


  /**
   * Output directory for the janagroo artifact  unarchiver. All jangaroo dependencies will be unpacked into
   * this directory.
   *
   * @parameter expression="${project.build.testOutputDirectory}"  default-value="${project.build.testOutputDirectory}"
   * @required
   */
  private File testOutputDirectory;

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
   * @component
   */
  private MavenProjectBuilder mavenProjectBuilder;


  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      testOutputDirectory.mkdir();
      getLog().info("Unpacking jangaroo dependencies to " + testOutputDirectory);
      unpack();
      createHtmlPage();
    } catch (IOException e) {
      throw new MojoExecutionException("Cannot unpack jangaroo dependencies/generate html test page", e);
    } catch (ArchiverException e) {
      throw new MojoExecutionException("Cannot unpack jangaroo dependencies/generate html test page", e);
    } catch (DependencyTreeBuilderException e) {
      throw new MojoExecutionException("Cannot unpack jangaroo dependencies/generate html test page", e);
    } catch (ProjectBuildingException e) {
      throw new MojoExecutionException("Cannot unpack jangaroo dependencies/generate html test page", e);
    }
  }

  public void log(String s) {
    getLog().info(s);
  }

  public void printIt(Map<String, List<String>> a) {
    for (String s : a.keySet()) {
      String log = "a.put(\"" + s + "\", ";
      if (a.get(s) == null || a.get(s).size() == 0) {
        log += " null);";
      } else {
        log += " Arrays.asList(";
        for (String s1 : a.get(s)) {
          log += "\"" + s1 + "\", ";
        }
        log = log.substring(0, log.length() - 2);
        log += "));";
      }
      log(log);
    }


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


  private void createHtmlPage() throws IOException, DependencyTreeBuilderException, MojoExecutionException, ProjectBuildingException {
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
      getLog().info(mp.getArtifact().getArtifactId());
      for (Dependency dep : ((List<Dependency>) mp.getDependencies())) {
        if ("jangaroo".equals(dep.getType())) {
          deps.add(dep.getGroupId() + ":" + dep.getArtifactId());
        }

      }
      artifact2Project.put(artifact.getGroupId() + ":" + artifact.getArtifactId(), deps);
    }
    printIt(artifact2Project);
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
      fw.write("  <script type=\"text/javascript\">\n" +
              "    //with (joo.classLoader=new joo.ClassLoader()) { // disable DynamicClassLoader, as all classes are already there!\n" +
              "    with (joo.classLoader) {\n" +
              "      debug = true;\n" +
              "      urlPrefix=\"classes/\";\n" +
              "      classLoadTimeoutMS = 3000;\n" +
              "      import_(\"flexunit.textui.TestRunner\");\n" +
              "      import_(\"suite.EditorTestSuite\");\n" +
              "      complete(function(imports) {with(imports){\n" +
              "        TestRunner.run(EditorTestSuite.suite());\n" +
              "      }});\n" +
              "    }\n" +
              "  </script>\n" +
              "  </body>\n" +
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
      @Override
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
