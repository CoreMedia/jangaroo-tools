/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaModuleHelper;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.jar.JarArchiver;

import java.io.File;

/**
 * Mojo to compile properties files to ActionScript3 files
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration", "UnusedPrivateField"})
@Mojo(name = "generate-sencha-module",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.RUNTIME,
        threadSafe = true, aggregator = true)
public class SenchaModuleMojo extends AbstractMojo {

  /**
   * The Maven session
   */
  @Parameter(defaultValue = "${session}", readonly = true)
  private MavenSession mavenSession;

  /**
   * The maven project
   */
  @Parameter(defaultValue = "${project}")
  private MavenProject project;

  /**
   * Plexus archiver.
   */
  @Component(role = org.codehaus.plexus.archiver.Archiver.class, hint = "jar")
  private JarArchiver archiver;

  /**
   * The archive configuration to use.
   * See <a href="http://maven.apache.org/shared/maven-archiver/index.html">Maven Archiver Reference</a>.
   */
  @Parameter
  private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

  /**
   * The sencha configuration to use.
   */
  @Parameter(property = "senchaConfiguration")
  private MavenSenchaConfiguration senchaConfiguration;

  public void execute() throws MojoExecutionException, MojoFailureException {

    Build build = project.getBuild();
    File jarFile = new File(build.getDirectory(), build.getFinalName() + "." + Types.JAVASCRIPT_EXTENSION);
    MavenArchiver mavenArchiver = new MavenArchiver();
    mavenArchiver.setArchiver(archiver);
    mavenArchiver.setOutputFile(jarFile);

    // for now:
    SenchaHelper senchaHelper = new SenchaModuleHelper(project, senchaConfiguration, getLog());
    senchaHelper.createModule();
    senchaHelper.prepareModule();
    senchaHelper.packageModule(archiver);

    try {
      mavenArchiver.createArchive(mavenSession, project, archive);
    } catch (Exception e) {
      throw new MojoExecutionException("Failed to create the javascript archive", e);
    }

    project.getArtifact().setFile(jarFile);
  }

}
