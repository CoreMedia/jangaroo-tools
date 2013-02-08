package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.utils.BOMStripperInputStream;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.plexus.archiver.zip.ZipEntry;
import org.codehaus.plexus.archiver.zip.ZipFile;
import org.codehaus.plexus.util.IOUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An abstract goal to build a Jangaroo application, either for testing or for an actual application.
 * It aggregates all needed resources into a given Web app directory:
 * <ul>
 * <li>extract all dependent jangaroo artifacts</li>
 * <li>optionally add Jangaroo compiler output from the current module</li>
 * <li>concatenate <artifactId>.js from all dependent jangaroo artifacts into jangaroo-application.js in the correct order</li>
 * </ul>
 *
 * @requiresDependencyResolution runtime
 */
public abstract class PackageApplicationMojo extends AbstractMojo {

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

  public abstract File getPackageSourceDirectory();

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
      unpack(webappDirectory);
      copyJangarooOutput(webappDirectory);
//      concatModuleScripts(new File(webappDirectory, "joo"));
    }
    catch (ArchiverException e) {
      throw new MojoExecutionException("Failed to unpack javascript dependencies", e);
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to create jangaroo-application[-all].js", e);
//    } catch (ProjectBuildingException e) {
//      throw new MojoExecutionException("Failed to create jangaroo-application[-all].js", e);
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

  private void concatModuleScripts(File scriptDirectory) throws IOException, ProjectBuildingException {
    Writer jangarooApplicationWriter = createJangarooModulesFile(scriptDirectory, "jangaroo-application.js");
    Writer jangarooApplicationAllWriter = createJangarooModulesFile(scriptDirectory, "jangaroo-application-all.js");
    try {
      jangarooApplicationWriter.write("// This file loads all collected JavaScript code from dependent Jangaroo modules.\n\n");
      jangarooApplicationAllWriter.write("// This file contains all collected JavaScript code from dependent Jangaroo modules.\n\n");

      for (Artifact artifact : getLinearizedDependencies()) {
        includeJangarooModuleScript(scriptDirectory, artifact, jangarooApplicationWriter, jangarooApplicationAllWriter);
      }
      writeThisJangarooModuleScript(scriptDirectory, jangarooApplicationWriter, jangarooApplicationAllWriter);
    } finally {
      try {
        jangarooApplicationWriter.close();
        jangarooApplicationAllWriter.close();
      } catch (IOException e) {
        getLog().warn("IOException on close ignored.", e);
      }
    }
  }

  protected void writeThisJangarooModuleScript(File scriptDirectory, Writer jangarooApplicationWriter, Writer jangarooApplicationAllWriter) throws IOException {
    ModuleSource jooModuleSource = null;
    File packageSourceDirectory = getPackageSourceDirectory();
    if (packageSourceDirectory != null) {
      File jangarooModuleFile = new File(packageSourceDirectory, computeModuleJsFileName(project.getArtifactId()));
      if (jangarooModuleFile.exists()) {
        jooModuleSource = new FileModuleSource(jangarooModuleFile);
      }
    }
    writeJangarooModuleScript(scriptDirectory, project.getArtifact(), jooModuleSource, jangarooApplicationWriter, jangarooApplicationAllWriter);
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

  private void includeJangarooModuleScript(File scriptDirectory, Artifact artifact, Writer jangarooApplicationWriter, Writer jangarooApplicationAllWriter) throws IOException {
    ZipFile zipFile = new ZipFile(artifact.getFile());
    ZipEntry zipEntry = zipFile.getEntry(computeModuleJsFileName(artifact.getArtifactId()));
    ModuleSource jooModuleSource = zipEntry != null ? new ZipEntryModuleSource(zipFile, zipEntry) : null;
    writeJangarooModuleScript(scriptDirectory, artifact, jooModuleSource, jangarooApplicationWriter, jangarooApplicationAllWriter);
  }

  private static final Pattern LOAD_SCRIPT_CODE_PATTERN = Pattern.compile("\\s*joo\\.loadScript\\(['\"]([^'\"]+)['\"]\\s*[,)].*");
  private static final Pattern LOAD_MODULE_CODE_PATTERN = Pattern.compile("\\s*joo\\.loadModule\\(['\"]([^'\"]+)['\"]\\s*,\\s*['\"]([^'\"]+)['\"].*");

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

  private void writeJangarooModuleScript(File scriptDirectory, Artifact artifact, ModuleSource jooModuleSource, Writer jangarooApplicationWriter, Writer jangarooApplicationAllWriter) throws IOException {
    String fullArtifactName = fullArtifactName(artifact);
    if (jooModuleSource == null) {
      getLog().debug("No " + artifact.getArtifactId() + ".module.js in " + fullArtifactName + ".");
      writeModule(scriptDirectory, artifact, jangarooApplicationWriter, jangarooApplicationAllWriter);
    } else {
      getLog().info("Appending " + artifact.getArtifactId() + ".module.js from " + fullArtifactName);
      String fromMessage = fromArtifactMessage(artifact);
      jangarooApplicationWriter.write(fromMessage);
      appendFromInputStream(jangarooApplicationWriter, jooModuleSource.getInputStream());
      jangarooApplicationAllWriter.write(fromMessage);
      writeModuleWithInlineScripts(scriptDirectory, jooModuleSource, jangarooApplicationAllWriter);
    }
  }

  private void writeModuleWithInlineScripts(File scriptDirectory, ModuleSource jooModuleSource, Writer jangarooApplicationAllWriter) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jooModuleSource.getInputStream(), "UTF-8"));
    while (true) {
      String line = bufferedReader.readLine();
      if (line == null) {
        break;
      }
      String scriptFilename = null;
      Matcher loadModuleMatcher = LOAD_MODULE_CODE_PATTERN.matcher(line);
      if (loadModuleMatcher.matches()) {
        String groupId = loadModuleMatcher.group(1);
        String artifactId = loadModuleMatcher.group(2);
        scriptFilename = "joo/" + groupId + "." + artifactId + ".classes.js";
        getLog().debug(" found loadModule: " + groupId + " / " + artifactId);
      } else {
        Matcher loadScriptMatcher = LOAD_SCRIPT_CODE_PATTERN.matcher(line);
        if (loadScriptMatcher.matches()) {
          scriptFilename = loadScriptMatcher.group(1);
          getLog().debug(" found loadScript: " + scriptFilename);
        }
      }
      File scriptFile = null;
      if (scriptFilename != null) {
        scriptFile = new File(scriptDirectory.getParent(), scriptFilename);
        if (!scriptFile.exists()) {
          scriptFile = null;
        }
      }
      if (scriptFile == null) {
        jangarooApplicationAllWriter.write(line + '\n');
      } else {
        appendFile(jangarooApplicationAllWriter, scriptFile);
      }
    }
  }

  private void writeModule(File scriptDirectory, Artifact artifact, Writer jangarooApplicationWriter, Writer jangarooApplicationAllWriter) throws IOException {
    writeModule(scriptDirectory, artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
      jangarooApplicationWriter, jangarooApplicationAllWriter);
  }

  protected void writeModule(File scriptDirectory, String groupId, String artifactId, String version, Writer jangarooApplicationWriter, Writer jangarooApplicationAllWriter) throws IOException {
    File classesJsFile = new File(scriptDirectory, groupId + "." + artifactId + ".classes.js");
    if (classesJsFile.exists()) {
      getLog().debug("Creating joo.loadModule(...) code for / appending .classes.js of " + fullArtifactName(groupId, artifactId, version) + ".");
      jangarooApplicationWriter.write(fromArtifactMessage(groupId, artifactId, version));
      jangarooApplicationWriter.write("joo.loadModule(\"" + groupId + "\",\"" + artifactId + "\");\n");
      jangarooApplicationAllWriter.write(fromArtifactMessage(groupId, artifactId, version));
      appendFile(jangarooApplicationAllWriter, classesJsFile);
    } else {
      getLog().debug("No file " + classesJsFile.getAbsolutePath() + " in module " + fullArtifactName(groupId, artifactId, version) +".");
    }
  }

  private void appendFile(Writer writer, File file) throws IOException {
    appendFromInputStream(writer, new BOMStripperInputStream(new FileInputStream(file)));
  }

  private void appendFromInputStream(Writer writer, InputStream inputStream) throws IOException {
    IOUtil.copy(inputStream, writer, "UTF-8");
    writer.write('\n'); // file might not end with new-line, better insert one
  }

  private Map<String, Artifact> artifactByInternalId() {
    final Map<String, Artifact> internalId2Artifact = new HashMap<String, Artifact>();
    for (Artifact artifact : getArtifacts()) {
      if ("jar".equals(artifact.getType())) {
        String internalId = getInternalId(artifact);
        internalId2Artifact.put(internalId, artifact);
      }
    }
    return internalId2Artifact;
  }

  private List<String> getDependencies(Artifact artifact) throws ProjectBuildingException {
    MavenProject mp = mavenProjectBuilder.buildFromRepository(artifact, remoteRepositories, localRepository, true);
    List<String> deps = new LinkedList<String>();
    for (Dependency dep : getDependencies(mp)) {
      if ("jar".equals(dep.getType()) &&
        (dep.getScope().equals("compile") || dep.getScope().equals("runtime"))) {
        deps.add(getInternalId(dep));
      }
    }
    return deps;
  }

  @SuppressWarnings({ "unchecked" })
  private static List<Dependency> getDependencies(MavenProject mp) {
    return (List<Dependency>) mp.getDependencies();
  }

  @SuppressWarnings({ "unchecked" })
  protected Set<Artifact> getArtifacts() {
    return (Set<Artifact>)project.getArtifacts();
  }

  private static interface ModuleSource {
    InputStream getInputStream() throws IOException;
  }

  private static class FileModuleSource implements ModuleSource {
    private final File file;

    private FileModuleSource(File file) {
      this.file = file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
      return new BOMStripperInputStream(new FileInputStream(file));
    }
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
