package net.jangaroo.jooc.mvnplugin.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;

import java.util.Objects;

public class MavenDependency {

  private String artifactId = StringUtils.EMPTY;

  private String groupId = StringUtils.EMPTY;

  private String type = StringUtils.EMPTY;

  private String scope = StringUtils.EMPTY;

  private MavenDependency() {
    // hide default constructor
  }

  private MavenDependency(String groupId, String artifactId, String scope, String type) {
    this.groupId = StringUtils.defaultString(groupId);
    this.artifactId = StringUtils.defaultString(artifactId);
    this.scope = StringUtils.defaultString(scope);
    this.type = StringUtils.defaultString(type);
  }

  public static MavenDependency fromArtifact(Artifact artifact) {
    return new MavenDependency(
            artifact.getGroupId(), artifact.getArtifactId(), artifact.getScope(), artifact.getType()
    );
  }

  public static MavenDependency fromDependency(Dependency dependency) {
    return new MavenDependency(
            dependency.getGroupId(), dependency.getArtifactId(), dependency.getScope(), dependency.getType()
    );
  }

  public static MavenDependency fromProject(MavenProject project) {
    return new MavenDependency(
            project.getGroupId(), project.getArtifactId(), null, null
    );
  }

  public static MavenDependency fromKey(String key) {
    MavenDependency result = new MavenDependency();
    if (key != null) {
      String[] coordinates = key.split(":");
      result.groupId = getArrayItemSafe(coordinates, 0);
      result.artifactId = getArrayItemSafe(coordinates, 1);
      result.scope = getArrayItemSafe(coordinates, 2);
      result.type = getArrayItemSafe(coordinates, 3);
      return result;
    }
    return result;
  }

  private static String getArrayItemSafe(String[] array, int index) {
    return array.length >= index + 1 ? array[index] : StringUtils.EMPTY;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public String getGroupId() {
    return groupId;
  }

  public String getType() {
    return type;
  }

  public String getScope() {
    return scope;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MavenDependency that = (MavenDependency) o;
    return equalsGroupIdAndArtifactId(o) &&
            Objects.equals(type, that.type) &&
            Objects.equals(scope, that.scope);
  }

  public boolean equalsGroupIdAndArtifactId(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MavenDependency that = (MavenDependency) o;
    return Objects.equals(artifactId, that.artifactId) &&
            Objects.equals(groupId, that.groupId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(artifactId, groupId, type, scope);
  }

  @Override
  public String toString() {
    StringBuilder coordinateBuilder = new StringBuilder();
    coordinateBuilder.append(groupId).append(':').append(artifactId);
    if (StringUtils.isNotBlank(type)) {
      coordinateBuilder.append(':').append(type);
    }
    if (StringUtils.isNotBlank(scope)) {
      coordinateBuilder.append(':').append(scope);
    }
    return coordinateBuilder.toString();
  }
}
