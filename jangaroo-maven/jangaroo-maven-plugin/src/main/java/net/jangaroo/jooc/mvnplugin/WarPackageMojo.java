package net.jangaroo.jooc.mvnplugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.MavenProjectBuilder;
import org.codehaus.mojo.javascript.archive.Types;
import org.codehaus.plexus.archiver.ArchiveFileFilter;
import org.codehaus.plexus.archiver.ArchiveFilterException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.archiver.zip.ZipFile;
import org.codehaus.plexus.archiver.zip.ZipEntry;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.IOUtil;

import java.io.*;
import java.util.Collections;
import java.util.Set;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * The <code>war-package</code> goal extracts all dependent jangaroo artifacts into
 * the web application to make them accessible from HTML
 * pages during execution of the webapp. It also copies optional Jangaroo compiler
 * output from the current module into the web application.<br/>
 * This goal is NOT bound to the jangaroo lifecycle. It is aimed to be used in conjunction with
 * the <code>war</code> lifecycle and, optionally, the Jangaroo <code>compile</code> goal by defining its execution
 * as shown in the following snippet<br/>
 * <pre>
 * ...
 * &lt;plugin>
 *  &lt;groupId>net.jangaroo&lt;/groupId>
 *  &lt;artifactId>jangaroo-maven-plugin&lt;/artifactId>
 *  &lt;extensions>true&lt;/extensions>
 *  &lt;executions>
 *   &lt;execution>
 *     &lt;id>compile-as-sources&lt;/id>
 *     &lt;phase>compile&lt;/phase>
 *     &lt;goals>
 *      &lt;goal>compile&lt;/goal>
 *     &lt;/goals>
 *   &lt;/execution>
 *   &lt;execution>
 *    &lt;id>war-package&lt;/id>
 *    &lt;goals>
 *     &lt;goal>war-package&lt;/goal>
 *    &lt;/goals>
 *   &lt;/execution>
 *  &lt;/executions>
 * &lt;/plugin>
 * ...
 * </pre>
 *
 * @goal war-package
 * @requiresDependencyResolution runtime
 * @phase compile
 */
@SuppressWarnings({ "ResultOfMethodCallIgnored" })
public class WarPackageMojo
    extends AbstractMojo {


  /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;


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
   * Location of Jangaroo resources of this module (including compiler output, usually under "scripts/") to be added
   * to the webapp. Defaults to ${project.build.directory}/joo/
   *
   * @parameter expression="${project.build.directory}/joo/"
   */
  private File packageSourceDirectory;

  /**
   * The directory where the webapp is built. Default is <code>${project.build.directory}/${project.build.finalName}</code>
   * exactly as the default of the maven-war-plugin.
   *
   * @parameter expression="${project.build.directory}/${project.build.finalName}"
   * @required
   */
  private File webappDirectory;

  /**
   * Plexus archiver.
   *
   * @component role="org.codehaus.plexus.archiver.UnArchiver" role-hint="zip"
   * @required
   */
  private ZipUnArchiver unarchiver;

  /**
   * {@inheritDoc}
   *
   * @see org.apache.maven.plugin.Mojo#execute()
   */
  public void execute()
      throws MojoExecutionException, MojoFailureException {
    webappDirectory.mkdirs();

    try {
      excludeFromWarPackaging();
      unpack(webappDirectory);
      copyJangarooOutput(webappDirectory);
      concatModuleScripts(new File(webappDirectory, "scripts"));
    }
    catch (ArchiverException e) {
      throw new MojoExecutionException("Failed to unpack javascript dependencies", e);
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to create jangaroo-modules.js", e);
    } catch (ProjectBuildingException e) {
      throw new MojoExecutionException("Failed to create jangaroo-modules.js", e);
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
      if (new File(packageSourceDirectory, "jangaroo-module.js").exists()) {
        File copiedJangarooModuleFile = new File(target, "jangaroo-module.js");
        if (copiedJangarooModuleFile.delete()) {
          getLog().info("File " + copiedJangarooModuleFile.getAbsolutePath() + " removed from copy.");
        } else {
          getLog().warn("Copied file " + copiedJangarooModuleFile.getAbsolutePath() + " could not be cleaned up.");
        }
      }
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
          includeJangarooModuleScript(artifact, fw);
        }
      }
      File jangarooModuleFile = new File(packageSourceDirectory, "jangaroo-module.js");
      if (jangarooModuleFile.exists()) {
        writeJangarooModuleScript(project.getArtifact(), new FileInputStream(jangarooModuleFile), fw);
      } else {
        getLog().debug("No jangaroo-module.js file in " + packageSourceDirectory.getAbsolutePath());
      }
    } finally {
      try {
        fw.close();
      } catch (IOException e) {
        getLog().warn("IOException on close ignored.", e);
      }
    }
  }

  private Writer createJangarooModulesFile(File scriptDirectory) throws IOException {
    scriptDirectory.mkdirs();
    File f = new File(scriptDirectory, "jangaroo-modules.js");
    getLog().info("Creating Jangaroo collected code script '" + f.getAbsolutePath() + "'.");
    return new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
  }

  private void includeJangarooModuleScript(Artifact artifact, Writer fw) throws IOException {
    ZipFile zipFile = new ZipFile(artifact.getFile());
    ZipEntry zipEntry = zipFile.getEntry("jangaroo-module.js");
    if (zipEntry != null) {
      InputStream jooModuleInputStream = zipFile.getInputStream(zipEntry);
      writeJangarooModuleScript(artifact, jooModuleInputStream, fw);
    } else {
      getLog().debug("No jangaroo-module.js in " + artifact);
    }
  }

  private void writeJangarooModuleScript(Artifact artifact, InputStream jooModuleInputStream, Writer fw) throws IOException {
    String fullAtifactName = artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
    getLog().info("Appending jangaroo-module.js from " + fullAtifactName);
    fw.write("// FROM " + fullAtifactName
      + ":\n");
    IOUtil.copy(jooModuleInputStream, fw, "UTF-8");
    fw.write('\n'); // file might not end with new-line, better insert one
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

  /**
   * Exclude all artifacts that have been depended with type 'jangaroo'. Since IntelliJ IDEA cannot
   * import jangaroo artifacts we need to import them twice. (a) with type jangaroo and (b) with no type
   * (defaulting to jar). Everything is fine except that these dependencies are included into WEB-INF/lib.
   * By manipulating the configuration of the war plugin we add these artifacts to the packagingExclude
   * property. !!! BAD BAD HACK !!!
   */
  private void excludeFromWarPackaging() {
    getLog().info("excludeFromWarPackaging");
    String pluginGroupId = "org.apache.maven.plugins";
    String pluginArtifactId = "maven-war-plugin";
    if (project.getBuildPlugins() != null) {
      for (Object o : project.getBuildPlugins()) {
        Plugin plugin = (Plugin) o;

        if (pluginGroupId.equals(plugin.getGroupId()) && pluginArtifactId.equals(plugin.getArtifactId())) {
          Xpp3Dom dom = (Xpp3Dom) plugin.getConfiguration();
          if (dom == null) {
            dom = new Xpp3Dom("configuration");
            plugin.setConfiguration(dom);
          }
          Xpp3Dom excludes = dom.getChild("packagingExcludes");
          if (excludes == null) {
            excludes = new Xpp3Dom("packagingExcludes");
            dom.addChild(excludes);
            excludes.setValue("");
          } else if (excludes.getValue().trim().length() > 0) {
            excludes.setValue(excludes.getValue() + ",");
          }

          Set<Artifact> dependencies = getArtifacts();
          getLog().debug("Size of getArtifacts: " + dependencies.size());
          String additionalExcludes = "";
          for (Artifact dependency : dependencies) {
            getLog().debug("Dependency: " + dependency.getGroupId() + ":" + dependency.getArtifactId() + "type: " + dependency.getType());
            if (!dependency.isOptional() && Types.JANGAROO_TYPE.equals(dependency.getType())) {
              getLog().debug("Excluding jangaroo dependency form war plugin [" + dependency.toString() + "]");
              // Add two excludes. The first one is effective when no name clash occurs
              additionalExcludes += "WEB-INF" + File.separator + "lib" + File.separator + dependency.getArtifactId() + "-" + dependency.getVersion() + ".jar,";
              // the second when a name clash occurs (artifact will hav groupId prepended before copying it into the lib dir)
              additionalExcludes += "WEB-INF" + File.separator + "lib" + File.separator + dependency.getGroupId() + "-" + dependency.getArtifactId() + "-" + dependency.getVersion() + ".jar,";
            }
          }
          additionalExcludes += "jangaroo-module.js";
          excludes.setValue(excludes.getValue() + additionalExcludes);
        }
      }
    }
  }

  @SuppressWarnings({ "unchecked" })
  private static List<Dependency> getDependencies(MavenProject mp) {
    return (List<Dependency>) mp.getDependencies();
  }

  @SuppressWarnings({ "unchecked" })
  private Set<Artifact> getArtifacts() {
    return (Set<Artifact>)project.getArtifacts();
  }

  private static class WarPackageArchiveFilter implements ArchiveFileFilter {
    @Override
    public boolean include(InputStream dataStream, String entryName) throws ArchiveFilterException {
      return !entryName.startsWith("META-INF");
    }
  }
}
