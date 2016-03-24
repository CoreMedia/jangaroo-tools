package net.jangaroo.jooc.mvnplugin.util;

import net.jangaroo.jooc.mvnplugin.Type;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.SimpleSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MavenPluginHelper {
  private MavenProject project;
  private Log log;

  public MavenPluginHelper(MavenProject project, Log log) {
    this.project = project;
    this.log = log;
  }

  public List<File> computeStaleSources(List<File> compileSourceRoots, Set<String> includes, Set<String> excludes, File outputDirectory, String inputFileSuffix, String outputFileSuffix, int staleMillis) throws MojoExecutionException {
    SourceInclusionScanner scanner = createSourceInclusionScanner(includes, excludes, inputFileSuffix, staleMillis);
    scanner.addSourceMapping(new SuffixMapping(inputFileSuffix, outputFileSuffix));
    log.debug("Searching for");
    Set<File> staleSources = new LinkedHashSet<>();

    for (File rootFile : compileSourceRoots) {
      if (!rootFile.isDirectory()) {
        continue;
      }

      try {
        log.debug("scanner.getIncludedSources(" + rootFile + ", " + outputDirectory + ")");
        //noinspection unchecked
        staleSources.addAll(scanner.getIncludedSources(rootFile, outputDirectory));
      } catch (InclusionScanException e) {
        throw new MojoExecutionException(
                "Error scanning source root: \'" + rootFile.getAbsolutePath() + "\' " + "for stale files to recompile.", e);
      }
    }
    return Collections.unmodifiableList(new ArrayList<>(staleSources));
  }

  private SourceInclusionScanner createSourceInclusionScanner(Set<String> includes, Set<String> excludes, String inputFileSuffix, int staleMillis) {
    SourceInclusionScanner scanner;

    if (staleMillis >= 0 && includes.isEmpty() && excludes.isEmpty()) {
      scanner = new StaleSourceScanner(staleMillis);
    } else {
      Set<String> includesOrDefaults = includes.isEmpty() ? Collections.singleton("**/*" + inputFileSuffix) : includes;
      scanner = staleMillis >= 0 ? new StaleSourceScanner(staleMillis, includesOrDefaults, excludes)
              : new SimpleSourceInclusionScanner(includesOrDefaults, excludes);
    }

    log.debug("Using source inclusion scanner " + scanner);
    return scanner;
  }

  public List<File> getActionScriptClassPath(boolean includeTestScope) {
    List<File> classPath = new ArrayList<>();
    Collection<Artifact> dependencies = getArtifacts();
    for (Artifact dependency : dependencies) {
      if (log.isDebugEnabled()) {
        log.debug("Dependency: " + dependency.getGroupId() + ":" + dependency.getArtifactId() + " type: " + dependency.getType());
      }
      if (!dependency.isOptional()
              && (Artifact.SCOPE_COMPILE.equals(dependency.getScope()) || includeTestScope && Artifact.SCOPE_TEST.equals(dependency.getScope()))
              && Type.JAR_EXTENSION.equals(dependency.getType())) {
        if (log.isDebugEnabled()) {
          log.debug("adding to classpath: compile dependency [" + dependency.toString() + "]");
        }
        classPath.add(dependency.getFile());
      }
    }
    // only for backwards-compatibility, use "src/main/joo-api" directory if it exists:
    File jooApiDir = new File(project.getBasedir(), "src/main/joo-api");
    if (jooApiDir.exists()) {
      classPath.add(0, jooApiDir);
    }
    return classPath;
  }

  @SuppressWarnings({"unchecked"})
  public Set<Artifact> getArtifacts() {
    return project.getArtifacts();
  }

}
