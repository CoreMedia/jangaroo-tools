package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import org.apache.maven.plugin.MojoExecutionException;

import java.util.Map;

public interface Configurer {

  void configure(Map<String, Object> config) throws MojoExecutionException;
}
