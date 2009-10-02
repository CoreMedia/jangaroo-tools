package net.jangaroo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * This one does nothing and is just here to allow a lifecycle definition.
 * @goal initialize
 */
public class DummyMojo extends AbstractMojo {
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    throw new MojoFailureException("Do not use this Mojo. It does nothing and just worksaround maven's inability to define a lifecycle without a plugin");
  }
}
