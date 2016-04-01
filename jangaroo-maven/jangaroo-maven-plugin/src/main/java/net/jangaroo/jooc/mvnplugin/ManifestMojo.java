/*
 * Copyright (c) 2015, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.exml.config.ExmlConfiguration;
import net.jangaroo.exml.generator.MxmlLibraryManifestGenerator;
import net.jangaroo.exml.model.ConfigClassRegistry;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.util.Collections;

import static net.jangaroo.exml.api.Exmlc.EXML_CONFIG_URI_PREFIX;

/**
 * A Mojo to generate the MXML library manifest.
 */
@SuppressWarnings({"UnusedDeclaration", "UnusedPrivateField"})
@Mojo(name = "generate-manifest", requiresDependencyResolution = ResolutionScope.RUNTIME)
public class ManifestMojo extends AbstractJangarooMojo {

  @Override
  public void execute() throws MojoExecutionException {
    String configClassPackage = getNamespaces()[0].getUri().substring(EXML_CONFIG_URI_PREFIX.length());

    ExmlConfiguration exmlConfiguration = new ExmlConfiguration();
    exmlConfiguration.setConfigClassPackage(configClassPackage);
    exmlConfiguration.setClassPath(getMavenPluginHelper().getActionScriptClassPath(false));
    exmlConfiguration.setOutputDirectory(getSourceDirectory());
    try {
      exmlConfiguration.setSourcePath(Collections.singletonList(getSourceDirectory()));
    } catch (IOException e) {
      throw new MojoExecutionException("could not determine source directory", e);
    }

    try {
      new MxmlLibraryManifestGenerator(new ConfigClassRegistry(exmlConfiguration)).createManifestFile();
    } catch (IOException e) {
      throw new MojoExecutionException("could not generate manifest", e);
    }
  }
}
