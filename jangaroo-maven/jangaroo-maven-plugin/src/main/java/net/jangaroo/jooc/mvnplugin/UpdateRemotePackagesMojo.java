package net.jangaroo.jooc.mvnplugin;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nullable;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Mojo(name = "update-remote-packages",
        defaultPhase = LifecyclePhase.PROCESS_RESOURCES,
        requiresDependencyResolution = ResolutionScope.RUNTIME,
        threadSafe = true, aggregator = true)
public class UpdateRemotePackagesMojo extends AbstractJangarooMojo {

  public static final String REMOTE_AGGREGATOR_ARTIFACT_ID = "remote-packages";

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    long startTime = System.nanoTime();

    List<MavenProject> projects = getProject().getCollectedProjects();
    MavenProject remoteAggregatorProject = null;

    Set<MavenProject> projectDependencies = new TreeSet<>(new MavenProjectComparator());

    for (MavenProject currentProject: projects) {
      if (Types.SENCHA_TYPES.contains(currentProject.getPackaging())) {
        List<Dependency> currentProjectDependencies = currentProject.getDependencies();
        for (Dependency dependency: currentProjectDependencies) {
          MavenProject projectFromDependency = createProjectFromDependency(dependency);
          if (!projectDependencies.contains(projectFromDependency) && !projects.contains(projectFromDependency)) {
            // add dependency: currentProject.
            projectDependencies.add(projectFromDependency);
          }
        }
      }
      if (isRemoteAggregator(currentProject)) {
        remoteAggregatorProject = currentProject;
      }
    }

    if (remoteAggregatorProject == null) {
      throw new MojoExecutionException("Could not find remote aggregator");
    }


    updatePom(remoteAggregatorProject, createDependencies(Lists.<MavenProject>newArrayList(projectDependencies), remoteAggregatorProject));

    getLog().debug(String.format("Needed %d ns to update remotes for project %s", System.nanoTime() - startTime, getProject()));
  }

  private boolean isRemoteAggregator(MavenProject project) {
    return REMOTE_AGGREGATOR_ARTIFACT_ID.equals(project.getArtifactId());
  }

  private List<Dependency> createDependencies(List<MavenProject> projects, final MavenProject remoteAggregator) {
    return Lists.transform(projects, new Function<MavenProject, Dependency>() {
      @Nullable
      @Override
      public Dependency apply(@Nullable MavenProject mavenProject) {
        return createDependencyFromProject(mavenProject, remoteAggregator);
      }
    });
  }

  private Dependency createDependencyFromProject(MavenProject mavenProject, MavenProject remoteAggregator) {
    Dependency dependency = new Dependency();
    dependency.setArtifactId(mavenProject.getArtifactId());
    dependency.setGroupId(mavenProject.getGroupId());
    dependency.setVersion(mavenProject.getVersion());

    List<Dependency> dependencyList = remoteAggregator.getDependencyManagement().getDependencies();
    for (Dependency dependencyFromList: dependencyList) {
      if (dependencyFromList.getGroupId().equals(dependency.getGroupId())
              && dependencyFromList.getArtifactId().equals(dependency.getArtifactId())
              && dependencyFromList.getVersion().equals(dependency.getVersion())) {
        // should add a warning if version differs
        dependency.setVersion(null);
        break;
      }
    }

    dependency.setScope(Artifact.SCOPE_RUNTIME);
    dependency.setType(Types.JAVASCRIPT_EXTENSION);

    return dependency;
  }

  private MavenProject createProjectFromDependency(Dependency dependency) {
    MavenProject mavenProject = new MavenProject();
    mavenProject.setArtifactId(dependency.getArtifactId());
    mavenProject.setGroupId(dependency.getGroupId());
    mavenProject.setVersion(dependency.getVersion());

    return mavenProject;
  }

  private void updatePom(MavenProject project, List<Dependency> dependencies) throws MojoFailureException {
    Model model = project.getOriginalModel().clone();
    model.setDependencies(dependencies);
    try {
      new MavenXpp3Writer().write(new FileWriter(project.getFile()), model);
    } catch (IOException e) {
      throw new MojoFailureException("could not create plugin config file for " + project.getFile().getAbsolutePath(), e);
    }
  }

  private static class MavenProjectComparator implements Comparator<MavenProject> {

    @Override
    public int compare(MavenProject p1, MavenProject p2) {
      if (!p1.getGroupId().equals(p2.getGroupId())) {
        return p1.getGroupId().compareTo(p2.getGroupId());
      } else if (!p1.getArtifactId().equals(p2.getArtifactId())) {
        return p1.getArtifactId().compareTo(p2.getArtifactId());
      }
      return p1.getVersion().compareTo(p2.getVersion());
    }
  }

}
