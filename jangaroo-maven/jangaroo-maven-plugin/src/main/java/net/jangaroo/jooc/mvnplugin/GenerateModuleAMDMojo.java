package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.utils.BOMStripperInputStream;
import net.jangaroo.utils.CompilerUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.plexus.archiver.zip.ZipEntry;
import org.codehaus.plexus.archiver.zip.ZipFile;
import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @goal generate-amd
 * @phase generate-resources
 * @threadSafe
 * @requiresDependencyResolution runtime
 */
public class GenerateModuleAMDMojo extends AbstractMojo {

  /**
   * Output directory into whose META-INF/resources/amd/ sub-directory the Maven module's AMD descriptor is generated.
   *
   * @parameter expression="${project.build.outputDirectory}"
   */
  private File outputDirectory;

  /**
   * Name of an optional module file that contains the "body" of this Maven module's AMD descriptor.
   *
   * @parameter expression="${project.build.resources[0].directory}/META-INF/resources/joo/${project.artifactId}.module.js"
   */
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
    Writer amdWriter = null;
    try {
      amdWriter = createAMDFile(outputDirectory, amdName);
      amdWriter.write(String.format("define(%s, [\n", CompilerUtils.quote(amdName)));
      List<String> dependencies = getDependencies(artifact);
      for (String dependency : dependencies) {
        amdWriter.write("  " + CompilerUtils.quote(dependency));
        amdWriter.write(",\n");
      }
      amdWriter.write("  " + CompilerUtils.quote("lib!" + amdName + ".lib"));
      amdWriter.write("], function() {\n");
      if (moduleScriptFile.exists()) {
        getLog().info("  including " + moduleScriptFile.getPath() + " into AMD file...");
        IOUtil.copy(new FileReader(moduleScriptFile), amdWriter);
      } else {
        getLog().info("  not file " + moduleScriptFile.getPath() + " found to include into AMD file...");
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

  static String computeAMDName(String groupId, String artifactId) {
    return "lib/" + groupId.replace('.', '/') + "/" + artifactId;
  }

  private Writer createAMDFile(File scriptDirectory, String amdName) throws IOException {
    File f = new File(scriptDirectory, "META-INF/resources/amd/" + amdName + ".js");
    //noinspection ResultOfMethodCallIgnored
    if (f.getParentFile().mkdirs()) {
      getLog().debug("created AMD output directory '" + f.getParent() + "'.");
    }
    getLog().info("Creating AMD script '" + f.getAbsolutePath() + "'.");
    return new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
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
      getLog().debug("No file " + classesJsFile.getAbsolutePath() + " in module " + fullArtifactName(groupId, artifactId, version) + ".");
    }
  }

  private void appendFile(Writer writer, File file) throws IOException {
    appendFromInputStream(writer, new BOMStripperInputStream(new FileInputStream(file)));
  }

  private void appendFromInputStream(Writer writer, InputStream inputStream) throws IOException {
    IOUtil.copy(inputStream, writer, "UTF-8");
    writer.write('\n'); // file might not end with new-line, better insert one
  }

  private List<String> getDependencies(Artifact artifact) throws ProjectBuildingException {
    MavenProject mp = mavenProjectBuilder.buildFromRepository(artifact, remoteRepositories, localRepository, true);
    List<String> deps = new LinkedList<String>();
    for (Dependency dep : getDependencies(mp)) {
      if ("jar".equals(dep.getType()) &&
        (dep.getScope().equals("compile") || dep.getScope().equals("runtime"))) {
        deps.add(computeAMDName(dep.getGroupId(), dep.getArtifactId()));
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
