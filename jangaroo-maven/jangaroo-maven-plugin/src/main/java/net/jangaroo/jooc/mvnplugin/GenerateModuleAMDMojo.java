package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.json.JsonArray;
import net.jangaroo.utils.CompilerUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.plexus.archiver.zip.ZipFile;
import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @goal generate-amd
 * @phase process-classes
 * @threadSafe
 * @requiresDependencyResolution runtime
 */
public class GenerateModuleAMDMojo extends JangarooMojo {

  /**
   * Resource directory whose META-INF/resources/amd/lib/groupId-path/ sub-directory may contain the Maven module's
   * AMD descriptor named artifactId.js.
   *
   * @parameter expression="${project.build.resources[0].directory}"
   */
  @SuppressWarnings("UnusedDeclaration")
  private File resourceDirectory;

  /**
   * Output directory into whose META-INF/resources/amd/ sub-directory the Maven module's AMD descriptor is generated.
   *
   * @parameter expression="${project.build.outputDirectory}"
   */
  @SuppressWarnings("UnusedDeclaration")
  File outputDirectory;

  /**
   * Location of Jangaroo resources of this module.
   * This property is used for <code>war</code> packaging type (actually, all packaging types
   * but <code>jangaroo</code>) as {@link #getOutputDirectory}.
   * Defaults to ${project.build.directory}/jangaroo-output/
   *
   * @parameter expression="${project.build.directory}/jangaroo-output/"
   */
  @SuppressWarnings("UnusedDeclaration")
  private File packageSourceDirectory;

  /**
   * Name of an optional module file that contains the "body" of this Maven module's AMD descriptor.
   *
   * @parameter expression="${project.build.outputDirectory}/META-INF/resources/joo/${project.artifactId}.module.js"
   */
  @SuppressWarnings("UnusedDeclaration")
  private File moduleScriptFile;

  /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  protected MavenProject project;

  /**
   * @component
   */
  @SuppressWarnings("UnusedDeclaration")
  private MavenProjectBuilder mavenProjectBuilder;

  /**
   * @parameter expression="${localRepository}"
   * @required
   */
  @SuppressWarnings("UnusedDeclaration")
  private ArtifactRepository localRepository;

  /**
   * @parameter expression="${project.remoteArtifactRepositories}"
   * @required
   */
  @SuppressWarnings("UnusedDeclaration")
  private List remoteRepositories;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    Artifact artifact = project.getArtifact();
    String amdName = computeAMDName(artifact.getGroupId(), artifact.getArtifactId());
    File sourceAMDFile = new File(resourceDirectory, "META-INF/resources/amd/" + amdName + ".js");
    if (sourceAMDFile.exists()) {
      getLog().info("  existing AMD file " + sourceAMDFile.getPath() + " found, skipping generation.");
      return;
    }
    getLog().info("  no source AMD file " + sourceAMDFile.getPath() + " found, generating one based on Maven dependencies.");
    Writer amdWriter = null;
    try {
      amdWriter = createAMDFile(amdName);
      amdWriter.write(String.format("define(%s, ", CompilerUtils.quote(amdName)));
      List<String> dependencies = getDependencies();
      String amdLibName = amdName + ".lib";
      if (getAMDFile(amdLibName).exists()) {
        dependencies.add("lib!" + amdLibName);
      }
      amdWriter.write(new JsonArray(dependencies.toArray()).toString(2, 0));
      amdWriter.write(", function() {\n");
      if (moduleScriptFile.exists()) {
        getLog().info("  including " + moduleScriptFile.getPath() + " into AMD file...");
        IOUtil.copy(new FileReader(moduleScriptFile), amdWriter);
      } else {
        getLog().info("  no file " + moduleScriptFile.getPath() + " found to include into AMD file.");
      }
      amdWriter.write("});");
      amdWriter.close();
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to create generated AMD output file.", e);
    } catch (ProjectBuildingException e) {
      throw new MojoExecutionException("Failed to collect dependencies.", e);
    } finally {
      if (amdWriter != null) {
        try {
          amdWriter.close();
        } catch (IOException e) {
          // so what? ignore
        }
      }
    }
  }

  @Override
  protected MavenProject getProject() {
    return project;
  }

  protected File getOutputDirectory() {
    return isJangarooPackaging() ? new File(outputDirectory, "META-INF/resources") : packageSourceDirectory;
  }

  public static String computeAMDName(String groupId, String artifactId) {
    return "lib/" + groupId.replace('.', '/') + "/" + artifactId;
  }

  private File getAMDFile(String amdName) {
    return new File(getOutputDirectory(), String.format("amd/%s.js", amdName));
  }

  Writer createAMDFile(String amdName) throws IOException {
    File f = getAMDFile(amdName);
    //noinspection ResultOfMethodCallIgnored
    if (f.getParentFile().mkdirs()) {
      getLog().debug("created AMD output directory '" + f.getParent() + "'.");
    }
    getLog().info("Creating AMD script '" + f.getAbsolutePath() + "'.");
    return new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
  }

  private List<String> getDependencies() throws ProjectBuildingException {
    return getDependencies(project, getLog(), new String[]{"compile", "runtime"},
            mavenProjectBuilder, remoteRepositories, localRepository);
  }

  public static List<String> getDependencies(MavenProject project, Log log,
                                             String[] scopes,
                                             MavenProjectBuilder mavenProjectBuilder,
                                             List remoteRepositories,
                                             ArtifactRepository localRepository) throws ProjectBuildingException {
    Map<String, Artifact> artifactByInternalId = new HashMap<String, Artifact>();
    for (Artifact artifact : getArtifacts(project)) {
      artifactByInternalId.put(getInternalId(artifact), artifact);
    }
    return getDependencies(artifactByInternalId, project, log, scopes, mavenProjectBuilder, remoteRepositories, localRepository);
  }

  private static List<String> getDependencies(Map<String, Artifact> artifactByInternalId, MavenProject project, Log log,
                                             String[] scopes,
                                             MavenProjectBuilder mavenProjectBuilder,
                                             List remoteRepositories,
                                             ArtifactRepository localRepository) throws ProjectBuildingException {
    List<String> deps = new LinkedList<String>();
    for (Artifact dep : getDependentArtifacts(artifactByInternalId, project)) {
      if (ArrayUtils.contains(scopes, dep.getScope())) {
        if ("jar".equals(dep.getType())) {
          String amdName = computeAMDName(dep.getGroupId(), dep.getArtifactId());
          File jarFile = dep.getFile();
          try {
            ZipFile jarZipFile = new ZipFile(jarFile);
            if (jarZipFile.getEntry("META-INF/resources/amd/" + amdName + ".js") != null) {
              log.info("  Adding dependency to AMD module " + amdName + " found in artifact " + jarFile + ".");
              deps.add(amdName);
            } else {
              log.info("  No AMD module " + amdName + " found in META-INF/resoures/amd of dependent artifact JAR " + jarFile + ", dependency skipped.");
            }
          } catch (IOException e) {
            log.warn("Cannot open dependent artifact JAR " + jarFile + ", no dependency generated.");
          }
        } else if ("pom".equals(dep.getType())) {
          // recurse into pom-packaged modules.
          MavenProject subProject = mavenProjectBuilder.buildFromRepository(dep, remoteRepositories, localRepository, true);
          deps.addAll(getDependencies(artifactByInternalId, subProject, log, scopes, mavenProjectBuilder,
                  remoteRepositories, localRepository));
        }
      }
    }
    return deps;
  }

  public static List<Artifact> getDependentArtifacts(Map<String, Artifact> artifactByInternalId, MavenProject project)
          throws ProjectBuildingException {
    List<Dependency> dependencies = getDependencies(project);
    List<Artifact> result = new ArrayList<Artifact>();
    for (Dependency dependency : dependencies) {
      Artifact artifact = artifactByInternalId.get(getInternalId(dependency));
      if (artifact != null) {
        result.add(artifact);
      }
    }
    return result;
  }

  private static String getInternalId(Dependency dep) {
    return dep.getGroupId() + ":" + dep.getArtifactId();
  }

  private static String getInternalId(Artifact art) {
    return art.getGroupId() + ":" + art.getArtifactId();
  }

  @SuppressWarnings("unchecked")
  public static List<Dependency> getDependencies(MavenProject project) {
    return project.getDependencies();
  }

  @SuppressWarnings("unchecked")
  private static Set<Artifact> getArtifacts(MavenProject project) {
    return project.getArtifacts();
  }

}
