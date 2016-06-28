package net.jangaroo.jooc.mvnplugin.analyzer;


import net.jangaroo.jooc.mvnplugin.Type;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.analyzer.DefaultProjectDependencyAnalyzer;
import org.apache.maven.shared.dependency.analyzer.ProjectDependencyAnalysis;
import org.apache.maven.shared.dependency.analyzer.ProjectDependencyAnalyzer;
import org.apache.maven.shared.dependency.analyzer.ProjectDependencyAnalyzerException;
import org.codehaus.plexus.component.annotations.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A dependency analyzer that skips modules with the Jangaroo packaging types. The dependencies in the Jangaroo module's
 * POM are declared as JAR packages and as such the <em>maven-dependency-plugin</em> tries to analyse usages
 * of the Java classes of the dependencies. As there are no Java classes inside these JAR dependencies you
 * either have to force "usedDepencencies" by means of the <em>maven-dependency-plugin</em> in the Jangaroo module or
 * you can use this analyzer which reports all Jangaroo JAR dependencies as being used.
 */
@Component(role = ProjectDependencyAnalyzer.class, hint = "skipJangarooAnalyzer")
public class SkipJangarooAnalyzer extends DefaultProjectDependencyAnalyzer {

  @Override
  public ProjectDependencyAnalysis analyze(MavenProject project) throws ProjectDependencyAnalyzerException {
    ProjectDependencyAnalysis analysis = super.analyze(project);

    // TODO - remove after migrating from jangaroo JAR to jangroo SWC or something else
    if (Type.containsJangarooSources(project)) {
      Set<Artifact> fakeUsedDeclaredArtifacts = new HashSet<>();
      fakeUsedDeclaredArtifacts.addAll(analysis.getUnusedDeclaredArtifacts());
      fakeUsedDeclaredArtifacts.addAll(analysis.getUsedUndeclaredArtifacts());
      analysis = new ProjectDependencyAnalysis(fakeUsedDeclaredArtifacts, Collections.<Artifact>emptySet(), Collections.<Artifact>emptySet());
    }
    return analysis;
  }
}
