package net.jangaroo.jooc.mvnplugin.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;

public class MavenDependencyHelper {

  private MavenDependencyHelper() {
    // hide default constructor
  }

  public static Dependency createDependency(String groupId, String artifactId, String type, String version) {
    Dependency dependency = new Dependency();
    dependency.setArtifactId(artifactId);
    dependency.setGroupId(groupId);
    dependency.setType(type);
    dependency.setVersion(version);
    return dependency;
  }

  public static Dependency fromArtifact(Artifact artifact) {
    Dependency dependency = createDependency(
            artifact.getGroupId(), artifact.getArtifactId(), artifact.getType(), artifact.getVersion()
    );
    dependency.setScope(artifact.getScope());
    return dependency;
  }

  public static Dependency fromProject(MavenProject project) {
    return createDependency(
            project.getGroupId(), project.getArtifactId(), null, null
    );
  }

  public static Dependency fromKey(String key) {
    Dependency result = new Dependency();
    if (key != null) {
      String[] coordinates = key.split(":");
      result.setGroupId( getArrayItemSafe(coordinates, 0) );
      result.setArtifactId( getArrayItemSafe(coordinates, 1) );
      result.setScope( getArrayItemSafe(coordinates, 2) );
      result.setType( getArrayItemSafe(coordinates, 3) );
      return result;
    }
    return result;
  }

  private static String getArrayItemSafe(String[] array, int index) {
    return array.length >= index + 1 ? array[index] : StringUtils.EMPTY;
  }



  public static boolean equalsGroupIdAndArtifactId(Dependency dependency1, Dependency dependency2) {
    return Objects.equals(dependency1, dependency2)
            || dependency1 != null && dependency2 != null
            && Objects.equals(dependency1.getArtifactId(), dependency2.getArtifactId())
            && Objects.equals(dependency1.getGroupId(), dependency2.getGroupId());

  }

  public static boolean contains(Collection<Dependency> dependencies, final Dependency dependency) {
    return Iterables.tryFind(dependencies, new Predicate<Dependency>() {
      @Override
      public boolean apply(@Nullable Dependency input) {
        return MavenDependencyHelper.equalsGroupIdAndArtifactId(input, dependency);
      }
    }).isPresent();
  }

  public static boolean remove(Collection<Dependency> dependencies, final Dependency dependency) {
    return Iterables.removeIf(dependencies, new Predicate<Dependency>() {
      @Override
      public boolean apply(@Nullable Dependency input) {
        return MavenDependencyHelper.equalsGroupIdAndArtifactId(input, dependency);
      }
    });
  }

}
