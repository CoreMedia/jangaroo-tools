/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.metadata.ResolutionGroup;
import org.apache.maven.project.MavenProject;

import java.util.List;
import java.util.Enumeration;
import java.util.Set;
import java.util.Collections;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.io.*;

/**
 *
 */
public abstract class AbstractRuntimeMojo extends AbstractMojo {
  /**
   * @component
   * @required
   * @readonly
   */
  protected org.apache.maven.artifact.factory.ArtifactFactory artifactFactory;

  /**
   * @component
   * @required
   * @readonly
   */
  protected org.apache.maven.artifact.resolver.ArtifactResolver resolver;

  /**
   * @parameter expression="${project.remoteArtifactRepositories}"
   * @required
   * @readonly
   */
  private java.util.List remoteRepositories;

  /**
   * @parameter expression="${localRepository}"
   * @required
   * @readonly
   */
  protected ArtifactRepository localRepository;

  /**
   * @parameter expression="${plugin.artifacts}"
   * @required
   * @readonly
   */
  private List<Artifact> pluginArtifacts;

  /**
   * By default, the plugin will take the Jangaroo runtime from its own jangaroo-compiler dependency.
   * If necessary, this parameter may be set to override which artifact will be used as the source for the
   * Jangaroo runtime.
   * @parameter alias="runtime"
   */
  private Runtime runtime;

  protected Artifact resolveArtifact(String groupId, String artifactId, String classifier, String type)  throws MojoFailureException, MojoExecutionException {
    Artifact result = null;
    for (Artifact pluginArtifact : pluginArtifacts) {
        getLog().debug("Inspecting pluginArtifact: " + pluginArtifact);
        if (groupId.equals(pluginArtifact.getGroupId()) &&
          artifactId.equals(pluginArtifact.getArtifactId()) &&
          (classifier == null || classifier.equals(pluginArtifact.getClassifier())) &&
          type.equals(pluginArtifact.getType())) {

          getLog().debug("Selected pluginArtifact: " + pluginArtifact);
          result = pluginArtifact;
          break;
        }
      }
    if (result == null) {
      throw new MojoExecutionException(String.format(
        "Cannot find any version of the required artifact among the plugin artifacts: %s:%s:%s:%s",
        groupId, artifactId, classifier, type));
    }

    getLog().debug("Using Jangaroo artifact: " + result);

    try {
      resolver.resolve( result, remoteRepositories, localRepository );
    } catch (Exception e) {
      throw new MojoExecutionException("Cannot resolve artifact", e);
    }

    return result;
  }


  protected Artifact resolveRuntimeArtifact() throws MojoFailureException, MojoExecutionException {
    Artifact result = null;

    // Use runtime, if configured.
    if (runtime != null) {
      if (runtime.getVersion() == null)
        throw new MojoExecutionException("<runtime>/<version> is not configured");

      result = artifactFactory.createArtifactWithClassifier(
        runtime.getGroupId(), runtime.getArtifactId(), runtime.getVersion(),
        Runtime.TYPE_RUNTIME, runtime.getClassifier());
    }

    if (result == null) {
      for (Artifact pluginArtifact : pluginArtifacts) {
        getLog().debug("Inspecting pluginArtifact: " + pluginArtifact);
        if (Runtime.GROUP_ID_JANGAROO.equals(pluginArtifact.getGroupId()) &&
          Runtime.ARTIFACT_ID_JOOC.equals(pluginArtifact.getArtifactId()) &&
          Runtime.CLASSIFIER_RUNTIME.equals(pluginArtifact.getClassifier()) &&
          Runtime.TYPE_RUNTIME.equals(pluginArtifact.getType())) {

          getLog().debug("Selected Jangaroo runtime pluginArtifact: " + pluginArtifact);
          result = pluginArtifact;
          break;
        }
      }
    }

    if (result == null) {
      throw new MojoExecutionException(String.format(
        "Cannot find any version of the required runtime artifact among the plugin artifacts: %s:%s:%s:%s",
        Runtime.GROUP_ID_JANGAROO, Runtime.ARTIFACT_ID_JOOC, Runtime.CLASSIFIER_RUNTIME, Runtime.TYPE_RUNTIME));
    }

    getLog().debug("Using Jangaroo runtime artifact: " + result);

    try {
      resolver.resolve( result, remoteRepositories, localRepository );
    } catch (Exception e) {
      throw new MojoExecutionException("Cannot resolve runtime artifact", e);
    }

    return result;
  }
}
