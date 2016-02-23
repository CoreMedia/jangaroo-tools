/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaModuleHelper;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

@Component(role = AbstractMavenLifecycleParticipant.class, hint = "sencha-workspace-extension")
public class SenchaWorkspaceExtension extends AbstractMavenLifecycleParticipant {

  @Requirement
  private Logger logger;

  @Override
  public void afterProjectsRead(MavenSession session) throws MavenExecutionException {
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

}
