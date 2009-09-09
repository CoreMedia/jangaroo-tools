package net.jangaroo.jooc.mvnplugin.test;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.javascript.archive.Types;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.components.io.fileselectors.FileInfo;
import org.codehaus.plexus.components.io.fileselectors.FileSelector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
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
   * Output directory for compiled classes.
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
    }
  }

  private void createHtmlPage() throws IOException {
    List dependencies = project.getTestArtifacts();
    Artifact jangRuntime = null;
    for (Object dependency : dependencies) {

      if ((((Artifact) dependency).getArtifactId().equals("jangaroo-runtime")) &&
              (((Artifact) dependency).getType().equals("jangaroo"))) {
        getLog().info("found runtime");
        jangRuntime = (Artifact) dependency;
        break;
      }
    }
    if (jangRuntime != null) {
      dependencies.remove(jangRuntime);
      dependencies.add(0, jangRuntime);
      getLog().info("moved runtime");
    }

    testOutputDirectory.mkdir();
    File f = new File(testOutputDirectory, "tests.html");
    FileWriter fw = null;
    try {
      f.createNewFile();
      fw = new FileWriter(f);
      fw.write("<html>\n" +
              "  <head><title>cap-ui-editor Tests</title></head>\n" +
              "  <body>");

      for (Iterator iterator = dependencies.iterator(); iterator.hasNext();) {
        Artifact dependency = (Artifact) iterator.next();
        getLog().debug("Dependency: " + dependency.getGroupId() + ":" + dependency.getArtifactId() + "type: " + dependency.getType());
        if (!dependency.isOptional() && Types.JANGAROO_TYPE.equals(dependency.getType())) {
          getLog().debug("Unpack jangaroo dependency [" + dependency.toString() + "]");
          fw.write("<script type=\"text/javascript\" src=\"" + dependency.getArtifactId() + ".js\"></script>\n");
        }
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

        getLog().info("Unpack jangaroo dependency [" + dependency.toString() + "]");
        unarchiver.setSourceFile(dependency.getFile());
        unarchiver.extract();
        if (unarchiver.getDestDirectory() == null) {
          throw new MojoExecutionException("God damn I don't know why targetDirectory is null!'" + unarchiver.getDestFile());
        }
      }
    }
  }
}
