package net.jangaroo.jooc.mvnplugin.sencha;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.archiver.jar.JarArchiver;

public interface SenchaHelper {

  void prepareModule() throws MojoExecutionException;

  void generateModule() throws MojoExecutionException;

  void packageModule(JarArchiver archiver) throws MojoExecutionException;
}
