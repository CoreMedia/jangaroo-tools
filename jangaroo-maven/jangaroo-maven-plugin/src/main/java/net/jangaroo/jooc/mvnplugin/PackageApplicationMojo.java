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
import org.codehaus.plexus.archiver.ArchiveFileFilter;
import org.codehaus.plexus.archiver.ArchiveFilterException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipEntry;
import org.codehaus.plexus.archiver.zip.ZipFile;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.util.FileUtils;
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
import java.util.Collections;
import java.util.HashMap;
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
@SuppressWarnings({"UnusedDeclaration", "ResultOfMethodCallIgnored"})
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
  private MavenProjectBuilder mavenProjectBuilder;

  /**
   * @parameter expression="${localRepository}"
   * @required
   */
  private ArtifactRepository localRepository;

  /**
   * @parameter expression="${project.remoteArtifactRepositories}"
   * @required
   */
  private List remoteRepositories;

  /**
   * Plexus archiver.
   *
   * @component role="org.codehaus.plexus.archiver.UnArchiver" role-hint="zip"
   * @required
   */
  private ZipUnArchiver unarchiver;

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
      concatModuleScripts(new File(webappDirectory, "joo"));
    }
    catch (ArchiverException e) {
      throw new MojoExecutionException("Failed to unpack javascript dependencies", e);
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to create jangaroo-application[-all].js", e);
    } catch (ProjectBuildingException e) {
      throw new MojoExecutionException("Failed to create jangaroo-application[-all].js", e);
    }
  }

  public void unpack(File target)
      throws ArchiverException {

    unarchiver.setOverwrite(false);
    unarchiver.setArchiveFilters(Collections.singletonList(new WarPackageArchiveFilter()));
    Set<Artifact> dependencies = getArtifacts();

    for (Artifact dependency : dependencies) {
      getLog().debug("Dependency: " + dependency.getGroupId() + ":" + dependency.getArtifactId() + "type: " + dependency.getType());
      if (!dependency.isOptional() && Types.JANGAROO_TYPE.equals(dependency.getType())) {
        getLog().debug("Unpacking jangaroo dependency [" + dependency.toString() + "]");
        unarchiver.setSourceFile(dependency.getFile());

        unpack(dependency, target);
      }
    }
  }

  public void unpack(Artifact artifact, File target)
      throws ArchiverException {
    unarchiver.setSourceFile(artifact.getFile());
    if (target.mkdirs()) {
      getLog().debug("created unarchiver target directory " + target);
    }
    unarchiver.setDestDirectory(target);
    unarchiver.setOverwrite(false);
    try {
      unarchiver.extract();
    }
    catch (Exception e) {
      throw new ArchiverException("Failed to extract javascript artifact to " + target, e);
    }

  }


  /**
   * Linearizes the acyclic, directed graph represented by <code>artifact2directDependencies</code> to a list
   * where every item just needs items that are contained in the list before itself.
   *
   * @param artifact2directDependencies acyclic, directed dependency graph
   * @return linearized dependency list
   */
  public static List<String> sort(Map<String, List<String>> artifact2directDependencies) {

    List<String> alreadyOut = new LinkedList<String>();
    while (!artifact2directDependencies.isEmpty()) {
      String currentDep = goDeep(artifact2directDependencies.keySet().iterator().next(), artifact2directDependencies);
      removeAll(currentDep, artifact2directDependencies);
      alreadyOut.add(currentDep);
    }
    return alreadyOut;
  }

  private static String goDeep(String start, Map<String, List<String>> artifact2directDependencies) {
    while (artifact2directDependencies.get(start) != null && !artifact2directDependencies.get(start).isEmpty()) {
      start = artifact2directDependencies.get(start).iterator().next();
    }
    return start;
  }

  private static void removeAll(String toBeRemoved, Map<String, List<String>> artifact2directDependencies) {
    artifact2directDependencies.remove(toBeRemoved);

    for (List<String> strings : artifact2directDependencies.values()) {
      strings.remove(toBeRemoved);
    }
  }

  private static String getInternalId(Dependency dep) {
    return dep.getGroupId() + ":" + dep.getArtifactId();
  }

  private static String getInternalId(Artifact art) {
    return art.getGroupId() + ":" + art.getArtifactId();
  }

  private void copyJangarooOutput(File target) throws IOException {
    File packageSourceDirectory = getPackageSourceDirectory();
    if (!packageSourceDirectory.exists()) {
      getLog().debug("No Jangaroo compiler output directory " + packageSourceDirectory.getAbsolutePath() + ", skipping copy Jangaroo output.");
    } else {
      getLog().info("Copying Jangaroo output from " + packageSourceDirectory + " to " + target);
      FileUtils.copyDirectoryStructureIfModified(packageSourceDirectory, target);
    }
  }

  private void concatModuleScripts(File scriptDirectory) throws IOException, ProjectBuildingException {
    List<Artifact> jooArtifacts = getJangarooDependencies();

    final Map<String, List<String>> artifact2Project = computeDependencyGraph(jooArtifacts);
    getLog().debug("artifact2Project : " + artifact2Project);

    List<String> depsLineralized = sort(artifact2Project);
    getLog().debug("depsLineralized  : " + depsLineralized);

    Writer jangarooApplicationWriter = createJangarooModulesFile(scriptDirectory, "jangaroo-application.js");
    Writer jangarooApplicationAllWriter = createJangarooModulesFile(scriptDirectory, "jangaroo-application-all.js");
    try {
      jangarooApplicationWriter.write("// This file loads all collected JavaScript code from dependent Jangaroo modules.\n\n");
      jangarooApplicationAllWriter.write("// This file contains all collected JavaScript code from dependent Jangaroo modules.\n\n");

      final Map<String, Artifact> internalId2Artifact = artifactByInternalId(jooArtifacts);
      for (String dependency : depsLineralized) {
        Artifact artifact = internalId2Artifact.get(dependency);
        if (artifact != null) { // may be a scope="test" or other kind of dependency not included in getDependencies()
          includeJangarooModuleScript(scriptDirectory, artifact, jangarooApplicationWriter, jangarooApplicationAllWriter);
        }
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
    File jangarooModuleFile = new File(getPackageSourceDirectory(), computeModuleJsFileName(project.getArtifactId()));
    ModuleSource jooModuleSource = jangarooModuleFile.exists()
      ? new FileModuleSource(jangarooModuleFile) : null;
    writeJangarooModuleScript(scriptDirectory, project.getArtifact(), jooModuleSource, jangarooApplicationWriter, jangarooApplicationAllWriter);
  }

  private static String computeModuleJsFileName(String artifactId) {
    return "joo/" + artifactId + ".module.js";
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

  private void writeJangarooModuleScript(File scriptDirectory, Artifact artifact, ModuleSource jooModuleSource, Writer jangarooApplicationWriter, Writer jangarooApplicationAllWriter) throws IOException {
    String fullArtifactName = artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
    String fromMessage = "// FROM " + fullArtifactName + ":\n";
    jangarooApplicationWriter.write(fromMessage);
    jangarooApplicationAllWriter.write(fromMessage);
    if (jooModuleSource == null) {
      getLog().debug("No " + artifact.getArtifactId() + ".module.js in " + fullArtifactName + ".");
      writeModule(scriptDirectory, artifact, jangarooApplicationWriter, jangarooApplicationAllWriter);
    } else {
      getLog().info("Appending " + artifact.getArtifactId() + ".module.js from " + fullArtifactName);
      appendFromInputStream(jangarooApplicationWriter, jooModuleSource.getInputStream());
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
      if (scriptFilename == null) {
        jangarooApplicationAllWriter.write(line + '\n');
      } else {
        File scriptFile = new File(scriptDirectory.getParent(), scriptFilename);
        appendFile(jangarooApplicationAllWriter, scriptFile);
      }
    }
  }

  private void writeModule(File scriptDirectory, Artifact artifact, Writer jangarooApplicationWriter, Writer jangarooApplicationAllWriter) throws IOException {
    writeModule(scriptDirectory, artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
      jangarooApplicationWriter, jangarooApplicationAllWriter);
  }

  protected void writeModule(File scriptDirectory, String groupId, String artifactId, String version, Writer jangarooApplicationWriter, Writer jangarooApplicationAllWriter) throws IOException {
    String fullArtifactName = groupId + ":" + artifactId + ":" + version;
    File classesJsFile = new File(scriptDirectory, groupId + "." + artifactId + ".classes.js");
    if (classesJsFile.exists()) {
      getLog().debug("Creating joo.loadModule(...) code for / appending .classes.js of " + fullArtifactName + ".");
      jangarooApplicationWriter.write("joo.loadModule(\"" + groupId + "\",\"" + artifactId + "\");\n");
      appendFile(jangarooApplicationAllWriter, classesJsFile);
    } else {
      getLog().debug("No file " + classesJsFile.getAbsolutePath() + " in module " + fullArtifactName +".");
    }
  }

  private void appendFile(Writer writer, File file) throws IOException {
    appendFromInputStream(writer, new BOMStripperInputStream(new FileInputStream(file)));
  }

  private void appendFromInputStream(Writer writer, InputStream inputStream) throws IOException {
    IOUtil.copy(inputStream, writer, "UTF-8");
    writer.write('\n'); // file might not end with new-line, better insert one
  }

  private Map<String, Artifact> artifactByInternalId(List<Artifact> jooArtifacts) {
    final Map<String, Artifact> internalId2Artifact = new HashMap<String, Artifact>();
    for (Artifact artifact : jooArtifacts) {
      String internalId = getInternalId(artifact);
      internalId2Artifact.put(internalId, artifact);
    }
    return internalId2Artifact;
  }

  private List<Artifact> getJangarooDependencies() {
    List<Artifact> jooArtifacts = new ArrayList<Artifact>();
    for (Artifact dependency : getArtifacts()) {
      if (Types.JANGAROO_TYPE.equals(dependency.getType())) {
        jooArtifacts.add(dependency);
      }
    }
    return jooArtifacts;
  }

  private Map<String, List<String>> computeDependencyGraph(List<Artifact> artifacts) throws ProjectBuildingException {
    final Map<String, List<String>> artifact2Project = new HashMap<String, List<String>>();
    for (Artifact artifact : artifacts) {
      MavenProject mp = mavenProjectBuilder.buildFromRepository(artifact, remoteRepositories, localRepository, true);
      List<String> deps = new LinkedList<String>();
      for (Dependency dep : getDependencies(mp)) {
        if (Types.JANGAROO_TYPE.equals(dep.getType())) {
          deps.add(getInternalId(dep));
        }
      }
      String internalId = getInternalId(artifact);
      artifact2Project.put(internalId, deps);
    }
    return artifact2Project;
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
  private static class WarPackageArchiveFilter implements ArchiveFileFilter {
    public boolean include(InputStream dataStream, String entryName) throws ArchiveFilterException {
      return !entryName.startsWith("META-INF");
    }
  }
}
