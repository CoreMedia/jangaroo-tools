package org.codehaus.mojo.javascript.archive;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.logging.AbstractLogEnabled;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

/**
 * A component to handle javascript dependencies.
 *
 * @author <a href="mailto:nicolas.deloof@gmail.com">nicolas De Loof</a>
 * @plexus.component role="org.codehaus.mojo.javascript.archive.JavascriptArtifactManager"
 */
public class JavascriptArtifactManager
        extends AbstractLogEnabled {

  /**
   * @plexus.requirement
   */
  private ArtifactResolver artifactResolver;

  /**
   * Plexus un-archiver.
   *
   * @plexus.requirement role-hint="jangaroo"
   */
  private UnArchiver archiver;

  public JavascriptArtifactManager() {
    super();
  }

  public void unpack(MavenProject project, String scope, File target, boolean useArtifactId)
          throws ArchiverException {
    archiver.setOverwrite(false);

    Set dependencies = project.getArtifacts();

    for (Iterator iterator = dependencies.iterator(); iterator.hasNext();) {
      Artifact dependency = (Artifact) iterator.next();
      getLogger().error("Dependency: " + dependency.getGroupId() + ":" + dependency.getArtifactId() + "type: " + dependency.getType());
      if (!dependency.isOptional() && Types.JANGAROO_TYPE.equals(dependency.getType())) {
        getLogger().info("Unpack javascript dependency [" + dependency.toString() + "]");
        archiver.setSourceFile(dependency.getFile());

        File dest = target;
        if (useArtifactId) {
          dest = new File(target, dependency.getArtifactId());
        }
        unpack(dependency, dest);
        /*artifactResolver.resolveTransitively()
        for (Object artifact : dependency.getDependencyTrail()) {
          getLogger().error("DependencyTrail: " + ((Artifact)artifact).getGroupId() + ":" + ((Artifact)artifact).getArtifactId() + "type: " + ((Artifact)artifact).getType());
        } */
      }
    }
  }

  /**
   *
   */
  public void unpack(Artifact artifact, File target)
          throws ArchiverException {
    archiver.setSourceFile(artifact.getFile());
    target.mkdirs();
    archiver.setDestDirectory(target);
    archiver.setOverwrite(false);
    try {
      archiver.extract();
    }
    catch (Exception e) {
      throw new ArchiverException("Failed to extract javascript artifact to " + target, e);
    }

  }
}