package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.utils.CompilerUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.plexus.archiver.zip.ZipFile;
import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
   * Name of an optional module file that contains the "body" of this Maven module's AMD descriptor.
   *
   * @parameter expression="${project.build.resources[0].directory}/META-INF/resources/joo/${project.artifactId}.module.js"
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
      amdWriter = createAMDFile(outputDirectory, amdName);
      amdWriter.write(String.format("define(%s, [\n", CompilerUtils.quote(amdName)));
      List<String> dependencies = getDependencies();
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
        getLog().info("  no file " + moduleScriptFile.getPath() + " found to include into AMD file...");
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

  public static String computeAMDName(String groupId, String artifactId) {
    return "lib/" + groupId.replace('.', '/') + "/" + artifactId;
  }

  Writer createAMDFile(File scriptDirectory, String amdName) throws IOException {
    File f = new File(scriptDirectory, "META-INF/resources/amd/" + amdName + ".js");
    //noinspection ResultOfMethodCallIgnored
    if (f.getParentFile().mkdirs()) {
      getLog().debug("created AMD output directory '" + f.getParent() + "'.");
    }
    getLog().info("Creating AMD script '" + f.getAbsolutePath() + "'.");
    return new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
  }

  private List<String> getDependencies() throws ProjectBuildingException {
    return getDependencies(project, getLog(), new String[]{"compile", "runtime"});
  }

  public static List<String> getDependencies(MavenProject mp, Log log,
                                             String[] scopes) throws ProjectBuildingException {
    List<String> deps = new LinkedList<String>();
    for (Artifact dep : getDependencies(mp)) {
      if ("jar".equals(dep.getType()) && ArrayUtils.contains(scopes, dep.getScope())) {
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
      }
    }
    return deps;
  }

  @SuppressWarnings({ "unchecked" })
  public static Set<Artifact> getDependencies(MavenProject mp) {
    return (Set<Artifact>) mp.getDependencyArtifacts();
  }

}
