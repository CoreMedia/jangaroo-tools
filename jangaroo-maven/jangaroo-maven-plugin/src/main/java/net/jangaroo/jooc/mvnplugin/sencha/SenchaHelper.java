package net.jangaroo.jooc.mvnplugin.sencha;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.archiver.jar.JarArchiver;

public interface SenchaHelper {

  void createModule() throws MojoExecutionException;

  void prepareModule() throws MojoExecutionException;

  void packageModule(JarArchiver archiver) throws MojoExecutionException;

  void deleteModule() throws MojoExecutionException;
}
