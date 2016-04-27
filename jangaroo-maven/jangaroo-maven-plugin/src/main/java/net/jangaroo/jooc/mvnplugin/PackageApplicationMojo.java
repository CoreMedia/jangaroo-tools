package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.utils.BOMStripperInputStream;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingResult;
import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * An abstract goal to build a Jangaroo application, either for testing or for an actual application.
 * It aggregates all needed resources into a given Web app directory:
 * <ul>
 * <li>extract all dependent jangaroo artifacts</li>
 * <li>optionally add Jangaroo compiler output from the current module</li>
 * <li>concatenate <artifactId>.js from all dependent jangaroo artifacts into jangaroo-application.js in the correct order</li>
 * </ul>
 *
 */
public abstract class PackageApplicationMojo extends AbstractMojo {

  /**
   * The maven project.
   */
  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  protected MavenProject project;

  @SuppressWarnings("UnusedDeclaration")
  @Component
  private ProjectBuilder mavenProjectBuilder;

  @SuppressWarnings("UnusedDeclaration")
  @Parameter(defaultValue = "${session}", required = true, readonly = true)
  private MavenSession session;

  @SuppressWarnings("UnusedDeclaration")
  @Parameter(defaultValue = "${localRepository}", required = true)
  private ArtifactRepository localRepository;

  @SuppressWarnings("UnusedDeclaration")
  @Parameter(defaultValue = "${project.remoteArtifactRepositories}")
  private List<ArtifactRepository> remoteRepositories;

  /**
   * Create the Jangaroo Web app in the given Web app directory.
   * @param webappDirectory the directory where to build the Jangaroo Web app.
   * @throws org.apache.maven.plugin.MojoExecutionException if anything goes wrong
   */
  protected void createWebapp(File webappDirectory) throws MojoExecutionException {
    if (webappDirectory.mkdirs()) {
      getLog().debug("created webapp directory " + webappDirectory);
    }

    try {
      concatModuleScripts(new File(webappDirectory, "joo"));
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to create jangaroo-application[-all].js", e);
    } catch (ProjectBuildingException e) {
      throw new MojoExecutionException("Failed to create jangaroo-application[-all].js", e);
    }
  }

  /**
   * Linearizes the acyclic, directed graph of all dependent artifacts to a list
   * where every item just depends on items that are contained in the list before itself.
   *
   * @return linearized dependency list of artifacts
   */
  private List<Artifact> getLinearizedDependencies() throws ProjectBuildingException {
    final Map<String, Artifact> internalId2Artifact = artifactByInternalId();

    List<String> depthFirstArtifactIds = new ArrayList<String>();
    Set<String> openArtifacts = new HashSet<String>(internalId2Artifact.keySet());
    while (!openArtifacts.isEmpty()) {
      depthFirst(internalId2Artifact, depthFirstArtifactIds, openArtifacts, openArtifacts.iterator().next());
    }

    getLog().debug("linearized dependencies: " + depthFirstArtifactIds);

    // back from internal IDs to Artifacts:
    List<Artifact> depthFirstArtifacts = new ArrayList<Artifact>(depthFirstArtifactIds.size());
    for (String depthFirstArtifactId : depthFirstArtifactIds) {
      depthFirstArtifacts.add(internalId2Artifact.get(depthFirstArtifactId));
    }
    return depthFirstArtifacts;
  }

  private void depthFirst(Map<String, Artifact> internalId2Artifact, List<String> depthFirstArtifactIds,
                          Set<String> openArtifacts, String artifactId) throws ProjectBuildingException {
    if (openArtifacts.remove(artifactId)) {
      // first, my dependencies:
      List<String> dependencies = getDependencies(internalId2Artifact.get(artifactId));
      for (String dependency : dependencies) {
        depthFirst(internalId2Artifact, depthFirstArtifactIds, openArtifacts, dependency);
      }
      // then, my artifact
      depthFirstArtifactIds.add(artifactId);
    }
  }

  private static String getInternalId(Dependency dep) {
    return dep.getGroupId() + ":" + dep.getArtifactId();
  }

  private static String getInternalId(Artifact art) {
    return art.getGroupId() + ":" + art.getArtifactId();
  }

  private static final String JOO_FLUSH_STYLE_SHEETS = "\njoo.flushStyleSheets();\n";

  private void concatModuleScripts(File scriptDirectory) throws IOException, ProjectBuildingException {

    try ( Writer jangarooApplicationWriter = createJangarooModulesFile(scriptDirectory, "jangaroo-application.js")) {

      jangarooApplicationWriter.write("// This file loads all collected JavaScript code from dependent Jangaroo modules.\n\n");

      for (Artifact artifact : getLinearizedDependencies()) {
        includeJangarooModuleScript(artifact, jangarooApplicationWriter);
      }

      jangarooApplicationWriter.write(JOO_FLUSH_STYLE_SHEETS);
    }

  }

  private static String computeModuleJsFileName(String artifactId) {
    return "META-INF/resources/joo/" + artifactId + ".module.js";
  }

  private Writer createJangarooModulesFile(File scriptDirectory, String fileName) throws IOException {
    //noinspection ResultOfMethodCallIgnored
    if (scriptDirectory.mkdirs()) {
      getLog().debug("created script output directory " + scriptDirectory);
    }
    File f = new File(scriptDirectory, fileName);
    getLog().info("Creating Jangaroo application script '" + f.getAbsolutePath() + "'.");
    return new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
  }

  private void includeJangarooModuleScript(Artifact artifact, Writer jangarooApplicationWriter) throws IOException {
    ZipFile zipFile = new ZipFile(artifact.getFile());
    ZipEntry zipEntry = zipFile.getEntry(computeModuleJsFileName(artifact.getArtifactId()));
    ModuleSource jooModuleSource = zipEntry != null ? new ZipEntryModuleSource(zipFile, zipEntry) : null;
    writeJangarooModuleScript(artifact, jooModuleSource, jangarooApplicationWriter);
  }

  private static String fromArtifactMessage(Artifact artifact) {
    return fromArtifactMessage(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion());
  }

  private static String fromArtifactMessage(String groupId, String artifactId, String version) {
    return "// FROM " + fullArtifactName(groupId, artifactId, version) + ":\n";
  }

  private static String fullArtifactName(Artifact artifact) {
    return fullArtifactName(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion());
  }

  private static String fullArtifactName(String groupId, String artifactId, String version) {
    return String.format("%s:%s:%s", groupId, artifactId, version);
  }

  private void writeJangarooModuleScript(Artifact artifact, ModuleSource jooModuleSource, Writer jangarooApplicationWriter) throws IOException {
    String fullArtifactName = fullArtifactName(artifact);
    if (jooModuleSource == null) {
      getLog().debug("No " + artifact.getArtifactId() + ".module.js in " + fullArtifactName + ".");
    } else {
      getLog().info("Appending " + artifact.getArtifactId() + ".module.js from " + fullArtifactName);
      String fromMessage = fromArtifactMessage(artifact);
      jangarooApplicationWriter.write(fromMessage);
      appendFromInputStream(jangarooApplicationWriter, jooModuleSource.getInputStream());
    }
  }

  private void appendFromInputStream(Writer writer, InputStream inputStream) throws IOException {
    IOUtil.copy(inputStream, writer, "UTF-8");
    writer.write('\n'); // file might not end with new-line, better insert one
  }

  private Map<String, Artifact> artifactByInternalId() {
    final Map<String, Artifact> internalId2Artifact = new HashMap<String, Artifact>();
    for (Artifact artifact : getArtifacts()) {
      if (Type.JAR_EXTENSION.equals(artifact.getType())) {
        String internalId = getInternalId(artifact);
        internalId2Artifact.put(internalId, artifact);
      }
    }
    return internalId2Artifact;
  }

  private List<String> getDependencies(Artifact artifact) throws ProjectBuildingException {
    ProjectBuildingRequest projectBuildingRequest = new DefaultProjectBuildingRequest(session.getProjectBuildingRequest());
    projectBuildingRequest.setResolveDependencies(true);
    // validation of dependency artifacts is not really a requirement here
    projectBuildingRequest.setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL);
    projectBuildingRequest.setProcessPlugins(false);

    ProjectBuildingResult projectBuildingResult = mavenProjectBuilder.build(artifact, true, projectBuildingRequest);

    List<String> deps = new LinkedList<String>();
    for (Dependency dep : getDependencies( projectBuildingResult.getProject() )) {
      if (Type.JAR_EXTENSION.equals(dep.getType()) &&
        (Artifact.SCOPE_COMPILE.equals(dep.getScope()) || Artifact.SCOPE_RUNTIME.equals(dep.getScope()))) {
        deps.add(getInternalId(dep));
      }
    }
    return deps;
  }

  @SuppressWarnings({ "unchecked" })
  private static List<Dependency> getDependencies(MavenProject mp) {
    return mp.getDependencies();
  }

  @SuppressWarnings({ "unchecked" })
  protected Set<Artifact> getArtifacts() {
    return project.getArtifacts();
  }

  private interface ModuleSource {
    InputStream getInputStream() throws IOException;
  }

  private static class ZipEntryModuleSource implements ModuleSource {
    private final ZipFile zipFile;
    private final ZipEntry zipEntry;

    private ZipEntryModuleSource(ZipFile zipFile, ZipEntry zipEntry) {
      this.zipFile = zipFile;
      this.zipEntry = zipEntry;
    }

    @Override
    public InputStream getInputStream() throws IOException {
      return new BOMStripperInputStream(zipFile.getInputStream(zipEntry));
    }
  }
}
