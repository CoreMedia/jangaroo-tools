package net.jangaroo.jooc.mvnplugin.sencha;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;

public interface SenchaHelper {

  void createModule() throws MojoExecutionException;

  void prepareModule() throws MojoExecutionException;

  File packageModule() throws MojoExecutionException;

}
