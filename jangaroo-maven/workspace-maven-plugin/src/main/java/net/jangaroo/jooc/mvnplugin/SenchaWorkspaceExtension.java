/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaModuleHelper;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.repository.RepositorySystem;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Singleton
@Named
public class SenchaWorkspaceExtension extends AbstractMavenLifecycleParticipant {

  @Inject
  private ArchiverManager archiverManager;

  @Inject
  private Logger logger;

  @Inject
  private RepositorySystem repositorySystem;

  @Override
  public void afterProjectsRead(MavenSession session) throws MavenExecutionException {
    List<String> goals = session.getGoals();
    if (goals.contains("clean")) {
      cleanWorkspace(session);
      if (goals.size() == 1) {
        return;
      }
    }

    unzipExtFramework(session);

    SenchaConfiguration senchaConfiguration = new SenchaConfiguration();
    senchaConfiguration.setType(SenchaConfiguration.Type.WORKSPACE);
    // todo use search config defaults
    senchaConfiguration.setBuildDir("target/sencha/build");
    senchaConfiguration.setPackagesDir("target/sencha/packages");
    senchaConfiguration.setExtFrameworkDir("target/ext");

    SenchaHelper senchaHelper = new SenchaModuleHelper(session.getTopLevelProject(), senchaConfiguration, new DefaultLog(logger));
    try {
      senchaHelper.createModule();
      senchaHelper.prepareModule();
    } catch (MojoExecutionException e) {
      throw new MavenExecutionException("Error occurred while creating sencha workspace extension",e);
    }
  }

  private void cleanWorkspace(MavenSession session) throws MavenExecutionException {
    cleanDirectory("target/sencha", session);
    cleanDirectory("target/ext", session);
    cleanDirectory(".sencha", session);
    cleanDirectory("workspace.json", session);
  }

  private void cleanDirectory(String path, MavenSession session) throws MavenExecutionException {
    File file = new File(session.getTopLevelProject().getBasedir().getAbsolutePath() + "/" + path);
    if (file.exists()) {
      boolean isDeleted = file.delete();
      if (!isDeleted) {
        throw new MavenExecutionException("Failed to clean sencha workspace file", file);
      }
    }
  }

  private void unzipExtFramework(MavenSession session) throws MavenExecutionException {
    // TODO get version from somewhere else

    Artifact artifact = repositorySystem.createArtifact("com.coremedia.sencha", "ext", "6.0.1.250", "zip");
    ArtifactResolutionRequest artifactResolutionRequest = new ArtifactResolutionRequest();
    artifactResolutionRequest.setLocalRepository(session.getLocalRepository());
    artifactResolutionRequest.setArtifact(artifact);

    ArtifactResolutionResult artifactResolutionResult = repositorySystem.resolve(artifactResolutionRequest);
    if (artifactResolutionResult.getArtifacts().isEmpty()) {
      throw new MavenExecutionException("Could not resolve required artifact \"com.coremedia.sencha:ext:6.0.1.250:zip\"", session.getCurrentProject().getFile());
    }

    File artifactFile = artifactResolutionResult.getArtifacts().iterator().next().getFile();
    File targetDir = new File("target/ext");
    if (!targetDir.exists()) {
      boolean targetDirCreation = targetDir.mkdirs();
      if (!targetDirCreation) {
        throw new MavenExecutionException("Could not create \"target/ext\" directory", session.getCurrentProject().getFile());
      }
    }

    try
    {

      UnArchiver unArchiver;

      unArchiver = archiverManager.getUnArchiver( artifactFile );
      logger.debug( "Found unArchiver by type: " + unArchiver );

      unArchiver.setUseJvmChmod( true );

      unArchiver.setIgnorePermissions( false );

      unArchiver.setSourceFile( artifactFile );

      unArchiver.setDestDirectory( targetDir );

      unArchiver.extract();
    }
    catch ( NoSuchArchiverException e )
    {
      throw new MavenExecutionException( "Unknown archiver type", e );
    }
    catch ( ArchiverException e )
    {
      throw new MavenExecutionException(
              "Error unpacking file: " + artifactFile + " to: " + targetDir + "\r\n" + e.toString(), e );
    }
  }

}
