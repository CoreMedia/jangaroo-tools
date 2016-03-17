package net.jangaroo.jooc.mvnplugin.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
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
import java.util.Objects;
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
    Set<File> staleSources = new LinkedHashSet<File>();

    for (File rootFile : compileSourceRoots) {
      if (!rootFile.isDirectory()) {
        continue;
      }

      try {
        log.debug("scanner.getIncludedSources(" + rootFile + ", " + outputDirectory + ")");
        //noinspection unchecked
        staleSources.addAll(scanner.getIncludedSources(rootFile, outputDirectory));
      }
      catch (InclusionScanException e) {
        throw new MojoExecutionException(
          "Error scanning source root: \'" + rootFile.getAbsolutePath() + "\' " + "for stale files to recompile.", e);
      }
    }
    return Collections.unmodifiableList(new ArrayList<File>(staleSources));
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
    List<File> classPath = new ArrayList<File>();
    Collection<Artifact> dependencies = getArtifacts();
    for (Artifact dependency : dependencies) {
      if (log.isDebugEnabled()) {
        log.debug("Dependency: " + dependency.getGroupId() + ":" + dependency.getArtifactId() + " type: " + dependency.getType());
      }
      if (!dependency.isOptional() && ("compile".equals(dependency.getScope()) || includeTestScope && "test".equals(dependency.getScope())) && "jar".equals(dependency.getType())) {
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

  /**
   * Compares two Maven identifiers and returns <code>true</code> if both
   * have the same <em>groupId</em> and <em>artifactId</em>. Packaging, type,
   * version and scope are ignored. The method assumes the default Maven
   * format returned by {@link MavenProject#getId()}, {@link Artifact#getId()} or {@link Dependency#getManagementKey()}.
   *
   * @param id1 maven identifier of the first artifact
   * @param id2 maven identifier of the second artifact
   * @return <code>true</code> if both ids have the same groupId and artifactId otherwise <code>false</code>
   */
  public static boolean hasSameGroupIdAndArtifactId(String id1, String id2) {
    if (Objects.equals(id1, id2)) {
      return true;
    }
    if (id1 == null || id2 == null) {
      return false;
    }

    String[] id1Coordinates = StringUtils.split(id1, ':');
    String[] id2Coordinates = StringUtils.split(id2, ':');
    return (id1Coordinates.length >= 2 && id2Coordinates.length >= 2)
            && Objects.equals(id1Coordinates[0],id2Coordinates[0])   // groupId = 0
            && Objects.equals(id1Coordinates[1],id2Coordinates[1]);  // artifactId = 1
  }
}
