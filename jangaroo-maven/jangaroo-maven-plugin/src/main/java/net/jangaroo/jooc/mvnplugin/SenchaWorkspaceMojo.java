/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaModuleHelper;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Mojo to compile properties files to ActionScript3 files
 *
 * @goal generate-sencha-workspace
 * @phase generate-sources
 * @requiresDependencyResolution
 * @threadSafe
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration", "UnusedPrivateField"})
public class SenchaWorkspaceMojo extends AbstractMojo {

  /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;

  /**
   * The sencha configuration to use.
   *
   * @parameter default-value="${senchaConfiguration}"
   */
  private SenchaConfiguration senchaConfiguration;

  public void execute() throws MojoExecutionException, MojoFailureException {

    // for now:
    SenchaHelper senchaHelper = new SenchaModuleHelper(project, senchaConfiguration, getLog());
    senchaHelper.prepareModule();
    senchaHelper.createModule();
  }

}
