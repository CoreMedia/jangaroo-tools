package net.jangaroo.jooc.mvnplugin;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.util.Arrays;

/**
 * @goal initialize
 * @phase initialize
 */
public class InitializePathsMojo extends AbstractMojo {
  /**
   * The maven project
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;


  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {


    Resource generatedResources = new Resource();
    generatedResources.setDirectory("target/generated-resources");
    Resource staticJavascript = new Resource();
    staticJavascript.setDirectory("src/main/joo-js");
    project.getBuild().setResources(Arrays.asList(generatedResources, staticJavascript));
    getLog().info("Setting project.build.resources to " + project.getBuild().getResources());


    project.getBuild().setOutputDirectory("target/joo");
    getLog().info("Setting project.build.outputDirectory to target/joo");

    project.getBuild().setSourceDirectory("src/main/joo");
    getLog().info("Setting project.build.sourceDirectory to src/main/joo");

    project.getBuild().setTestSourceDirectory("src/test/joo");
    getLog().info("Setting project.build.testSourceDirectory to src/test/joo");

    project.getBuild().setTestOutputDirectory("target/joo-test");
    getLog().info("Setting project.build.testOutputDirectory to " + project.getBuild().getTestOutputDirectory());

  }
}
