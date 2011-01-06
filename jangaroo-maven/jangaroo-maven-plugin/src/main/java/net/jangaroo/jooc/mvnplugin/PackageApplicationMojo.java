package net.jangaroo.jooc.mvnplugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.mojo.javascript.archive.Types;
import org.codehaus.plexus.archiver.ArchiveFileFilter;
import org.codehaus.plexus.archiver.ArchiveFilterException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipEntry;
import org.codehaus.plexus.archiver.zip.ZipFile;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
   * Location of Jangaroo resources of this module (including compiler output, usually under "joo/") to be added
   * to the webapp. Defaults to ${project.build.directory}/jangaroo-output/
   *
   * @parameter expression="${project.build.directory}/jangaroo-output/"
   */
  private File packageSourceDirectory;

  /**
   * Plexus archiver.
   *
   * @component role="org.codehaus.plexus.archiver.UnArchiver" role-hint="zip"
   * @required
   */
  private ZipUnArchiver unarchiver;

  /**
   * Create the Jangaroo Web app in the given Web app directory.
   * @param webappDirectory the directory where to build the Jangaroo Web app.
   * @throws org.apache.maven.plugin.MojoExecutionException if anything goes wrong
   */
  protected void createWebapp(File webappDirectory) throws MojoExecutionException {
    webappDirectory.mkdirs();

    try {
      unpack(webappDirectory);
      copyJangarooOutput(webappDirectory);
      concatModuleScripts(new File(webappDirectory, "joo"));
    }
    catch (ArchiverException e) {
      throw new MojoExecutionException("Failed to unpack javascript dependencies", e);
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to create jangaroo-application.js", e);
    } catch (ProjectBuildingException e) {
      throw new MojoExecutionException("Failed to create jangaroo-application.js", e);
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
    target.mkdirs();
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

    Writer fw = createJangarooModulesFile(scriptDirectory);
    try {
      fw.write("// This file contains collected JavaScript code from dependent Jangaroo modules.\n\n");

      final Map<String, Artifact> internalId2Artifact = artifactByInternalId(jooArtifacts);
      for (String dependency : depsLineralized) {
        Artifact artifact = internalId2Artifact.get(dependency);
        if (artifact != null) { // may be a scope="test" or other kind of dependency not included in getDependencies()
          includeJangarooModuleScript(scriptDirectory, artifact, fw);
        }
      }
      writeThisJangarooModuleScript(scriptDirectory, fw);
    } finally {
      try {
        fw.close();
      } catch (IOException e) {
        getLog().warn("IOException on close ignored.", e);
      }
    }
  }

  protected void writeThisJangarooModuleScript(File scriptDirectory, Writer fw) throws IOException {
    File jangarooModuleFile = new File(packageSourceDirectory, "joo/" + project.getArtifactId() + ".js");
    FileInputStream jooModuleInputStream = jangarooModuleFile.exists()
      ? new FileInputStream(jangarooModuleFile) : null;
    writeJangarooModuleScript(scriptDirectory, project.getArtifact(), jooModuleInputStream, fw);
  }

  private Writer createJangarooModulesFile(File scriptDirectory) throws IOException {
    //noinspection ResultOfMethodCallIgnored
    scriptDirectory.mkdirs();
    File f = new File(scriptDirectory, "jangaroo-application.js");
    getLog().info("Creating Jangaroo application script '" + f.getAbsolutePath() + "'.");
    return new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
  }

  private void includeJangarooModuleScript(File scriptDirectory, Artifact artifact, Writer fw) throws IOException {
    ZipFile zipFile = new ZipFile(artifact.getFile());
    ZipEntry zipEntry = zipFile.getEntry("joo/" + artifact.getArtifactId() + ".js");
    InputStream jooModuleInputStream = zipEntry != null ? zipFile.getInputStream(zipEntry) : null;
    writeJangarooModuleScript(scriptDirectory, artifact, jooModuleInputStream, fw);
  }

  private void writeJangarooModuleScript(File scriptDirectory, Artifact artifact, InputStream jooModuleInputStream, Writer fw) throws IOException {
    String fullAtifactName = artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
    fw.write("// FROM " + fullAtifactName + ":\n");
    if (jooModuleInputStream == null) {
      getLog().debug("No " + artifact.getArtifactId() + ".js in " + fullAtifactName + ".");
      if (new File(scriptDirectory, artifact.getGroupId() + "." + artifact.getArtifactId() + ".classes.js").exists()) {
        getLog().debug("Creating joo.loadModule(...) code for " + fullAtifactName + ".");
        fw.write("joo.loadModule(\"" + artifact.getGroupId() + "\",\"" + artifact.getArtifactId() + "\");\n");
      }
    } else {
      getLog().info("Appending " + artifact.getArtifactId() + ".js from " + fullAtifactName);
      IOUtil.copy(jooModuleInputStream, fw, "UTF-8");
      fw.write('\n'); // file might not end with new-line, better insert one
    }
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
      if ("jangaroo".equals(dependency.getType())) {
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
        if ("jangaroo".equals(dep.getType())) {
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

  private static class WarPackageArchiveFilter implements ArchiveFileFilter {
    public boolean include(InputStream dataStream, String entryName) throws ArchiveFilterException {
      return !entryName.startsWith("META-INF");
    }
  }
}
