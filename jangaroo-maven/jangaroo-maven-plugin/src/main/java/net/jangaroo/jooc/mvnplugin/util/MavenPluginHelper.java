package net.jangaroo.jooc.mvnplugin.util;

import net.jangaroo.jooc.mvnplugin.Types;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MavenPluginHelper {
  private MavenProject project;
  private Log log;

  public MavenPluginHelper(MavenProject project, Log log) {
    this.project = project;
    this.log = log;
  }

  public List<File> getActionScriptClassPath() {
    List<File> classPath = new ArrayList<File>();
    Collection<Artifact> dependencies = getArtifacts();
    for (Artifact dependency : dependencies) {
      if (log.isDebugEnabled()) {
        log.debug("Dependency: " + dependency.getGroupId() + ":" + dependency.getArtifactId() + "type: " + dependency.getType());
      }
      if (!dependency.isOptional() && Types.JANGAROO_TYPE.equals(dependency.getType())) {
        if (log.isDebugEnabled()) {
          log.debug("adding to classpath: jangaroo dependency [" + dependency.toString() + "]");
        }
        classPath.add(dependency.getFile());
      }
    }
    return classPath;
  }

  @SuppressWarnings({"unchecked"})
  public Set<Artifact> getArtifacts() {
    return (Set<Artifact>) project.getArtifacts();
  }
}
