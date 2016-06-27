package net.jangaroo.jooc.mvnplugin.util;

import net.jangaroo.jooc.api.Jooc;
import net.jangaroo.jooc.mvnplugin.Type;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionRequest;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.RepositorySystem;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.jar.Manifest;
import org.codehaus.plexus.archiver.jar.ManifestException;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.compiler.util.scan.InclusionScanException;
import org.codehaus.plexus.compiler.util.scan.SimpleSourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.SourceInclusionScanner;
import org.codehaus.plexus.compiler.util.scan.StaleSourceScanner;
import org.codehaus.plexus.compiler.util.scan.mapping.SourceMapping;
import org.codehaus.plexus.compiler.util.scan.mapping.SuffixMapping;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

  /**
   * Creates a default manifest file for any Jangaroo-packaged Maven project
   * @param project the Maven project with packaging type "jangaroo-app" or "jangaroo-pkg"
   * @return the default manifest file
   * @throws ManifestException
   * @throws IOException
   * @throws ArchiverException
   */
  @Nonnull
  public static File createDefaultManifest(MavenProject project)
      throws ManifestException, IOException, ArchiverException {
    Manifest manifest = new Manifest();
    Manifest.Attribute attr = new Manifest.Attribute("Created-By", "Apache Maven");
    manifest.addConfiguredAttribute(attr);
    attr = new Manifest.Attribute("Implementation-Title", project.getName());
    manifest.addConfiguredAttribute(attr);
    attr = new Manifest.Attribute("Implementation-Version", project.getVersion());
    manifest.addConfiguredAttribute(attr);
    attr = new Manifest.Attribute("Implementation-Vendor-Id", project.getGroupId());
    manifest.addConfiguredAttribute(attr);
    if (project.getOrganization() != null) {
      String vendor = project.getOrganization().getName();
      attr = new Manifest.Attribute("Implementation-Vendor", vendor);
      manifest.addConfiguredAttribute(attr);
    }
    attr = new Manifest.Attribute("Built-By", System.getProperty("user.name"));
    manifest.addConfiguredAttribute(attr);
    attr = new Manifest.Attribute("Class-Path", jangarooDependencies(project));
    manifest.addConfiguredAttribute(attr);

    File mf = File.createTempFile("maven", ".mf");
    mf.deleteOnExit();
    try (PrintWriter writer = new PrintWriter(new FileWriter(mf))) {
      manifest.write(writer);
    }
    return mf;
  }

  @SuppressWarnings({"unchecked"})
  private static String jangarooDependencies(MavenProject project) {
    StringBuilder sb = new StringBuilder();
    Set<Artifact> dependencyArtifacts = project.getDependencyArtifacts();
    for (Artifact artifact : dependencyArtifacts) {
      if (Type.JAR_EXTENSION.equals(artifact.getType())) {
        sb.append(artifact.getArtifactId())
                .append('-')
                .append(artifact.getVersion())
                .append('.')
                .append(Type.JAR_EXTENSION)
                .append(' ');
      }
    }
    return sb.toString();
  }

  public List<File> computeStaleSources(List<File> compileSourceRoots, Set<String> includes, Set<String> excludes, File outputDirectory, String inputFileSuffix, String outputFileSuffix, int staleMillis) throws MojoExecutionException {
    return computeStaleSources(compileSourceRoots, includes, excludes, outputDirectory, inputFileSuffix,
            new SuffixMapping(inputFileSuffix, outputFileSuffix), staleMillis);
  }

  public List<File> computeStalePropertiesSources(List<File> compileSourceRoots, Set<String> includes, Set<String> excludes, File outputDirectory, int staleMillis) throws MojoExecutionException {
    return computeStaleSources(compileSourceRoots, includes, excludes, outputDirectory, Jooc.PROPERTIES_SUFFIX,
            PropertiesSourceMapping.getInstance(), staleMillis);
  }

  private List<File> computeStaleSources(List<File> compileSourceRoots, Set<String> includes, Set<String> excludes, File outputDirectory, String inputFileSuffix, SourceMapping sourceMapping, int staleMillis) throws MojoExecutionException {
    SourceInclusionScanner scanner = createSourceInclusionScanner(includes, excludes, inputFileSuffix, staleMillis);
    scanner.addSourceMapping(sourceMapping);
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
    Set<String> includesOrDefaults = includes.isEmpty() ? Collections.singleton("**/*" + inputFileSuffix) : includes;
    SourceInclusionScanner scanner = staleMillis >= 0
            ? new StaleSourceScanner(staleMillis, includesOrDefaults, excludes)
            : new SimpleSourceInclusionScanner(includesOrDefaults, excludes);

    log.debug("Using source inclusion scanner " + scanner);
    return scanner;
  }

  public List<File> getActionScriptClassPath(boolean includeTestScope) {
    List<File> classPath = new ArrayList<>();
    Collection<Artifact> dependencies = project.getArtifacts();
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

  public static void extractFileTemplate(File targetDirectory,
                                         File templateFile,
                                         ArchiverManager archiverManager) throws MojoExecutionException {
    UnArchiver unArchiver;
    try {
      unArchiver = archiverManager.getUnArchiver(Type.ZIP_EXTENSION);
    } catch (NoSuchArchiverException e) {
      throw new MojoExecutionException("No ZIP UnArchiver?!", e);
    }
    unArchiver.setSourceFile(templateFile);
    unArchiver.setDestDirectory(targetDirectory);
    unArchiver.extract();
  }

  public static File getArtifactFile(ArtifactRepository localRepository,
                                     List<ArtifactRepository> remoteRepositories,
                                     ArtifactResolver artifactResolver,
                                     Artifact artifact) throws MojoExecutionException {

    return getRealArtifact(localRepository, remoteRepositories, artifactResolver, artifact).getFile();
  }

  public static Artifact getArtifact(ArtifactRepository localRepository,
                                     List<ArtifactRepository> remoteRepositories,
                                     ArtifactResolver artifactResolver,
                                     RepositorySystem repositorySystem,
                                     String groupId,
                                     String artifactId,
                                     String version,
                                     String scope,
                                     String type) throws MojoExecutionException {

    Artifact templateArtifact = repositorySystem.createArtifact(groupId, artifactId, version, scope, type);

    return getRealArtifact(localRepository, remoteRepositories, artifactResolver, templateArtifact);
  }

  private static Artifact getRealArtifact(ArtifactRepository localRepository,
                                          List<ArtifactRepository> remoteRepositories,
                                          ArtifactResolver artifactResolver,
                                          Artifact artifact) {
    ArtifactResolutionRequest artifactResolutionRequest = new ArtifactResolutionRequest();
    artifactResolutionRequest.setArtifact(artifact);
    artifactResolutionRequest.setLocalRepository(localRepository);
    artifactResolutionRequest.setRemoteRepositories(remoteRepositories);
    ArtifactResolutionResult result = artifactResolver.resolve(artifactResolutionRequest);

    return result.getArtifacts().iterator().next();
  }

}
