package net.jangaroo.jooc.mvnplugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.javascript.archive.Types;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.components.io.fileselectors.FileInfo;
import org.codehaus.plexus.components.io.fileselectors.FileSelector;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * The <code>war-package</code> goal extracts all dependent jangaroo artifacts into
 * the scripts directory of the web application to make them accessible from HTML
 * pages during execution of the webapp.<br/>
 * This goal is NOT bound to the jangaroo lifecycle. It should be used in conjunction with
 * the <code>war-compile</code> goal in the <code>war</code> lifecycle by defining its execution
 * as shown in the following sniplet<br/>
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
 *      &lt;goal>war-compile&lt;/goal>
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
   * The directory where the webapp is built. Default is <code>${project.build.directory}/${project.build.finalName}</code>
   * exactly as the default of the maven-war-plugin.
   *
   * @parameter expression="${project.build.directory}/${project.build.finalName}"
   * @required
   */
  private File webappDirectory;

  /**
   * The folder below the <code>webappDirectory</code> for unpacking the javascript dependencies.
   *
   * @parameter expression="${scripts}" default-value="scripts"
   */
  private String scriptsDirectory;

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
    File scriptDir = new File(webappDirectory, scriptsDirectory);

    try {
      excludeFromWarPackaging();
      unpack(project, scriptDir);
    }
    catch (ArchiverException e) {
      throw new MojoExecutionException("Failed to unpack javascript dependencies", e);
    }
  }


  public void unpack(MavenProject project, File target)
          throws ArchiverException {

    unarchiver.setOverwrite(false);
    unarchiver.setFileSelectors(new FileSelector[]{new FileSelector() {
      public boolean isSelected(FileInfo fileInfo) throws IOException {
        return !fileInfo.getName().startsWith("META-INF");
      }

    }});
    Set<Artifact> dependencies = project.getArtifacts();

    for (Artifact dependency : dependencies) {
      getLog().debug("Dependency: " + dependency.getGroupId() + ":" + dependency.getArtifactId() + "type: " + dependency.getType());
      if (!dependency.isOptional() && Types.JANGAROO_TYPE.equals(dependency.getType())) {
        getLog().debug("Unpack jangaroo dependency [" + dependency.toString() + "]");
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

          Set dependencies = project.getArtifacts();
          getLog().debug("Size of getArtifacts: " + dependencies.size());
          String additionalExcludes = "";
          for (Iterator iterator = dependencies.iterator(); iterator.hasNext();) {
            Artifact dependency = (Artifact) iterator.next();
            getLog().debug("Dependency: " + dependency.getGroupId() + ":" + dependency.getArtifactId() + "type: " + dependency.getType());
            if (!dependency.isOptional() && Types.JANGAROO_TYPE.equals(dependency.getType())) {
              getLog().info("Excluding jangaroo dependency form war plugin [" + dependency.toString() + "]");
              // Add two excludes. The first one is effective when no nameclash occcurs
              additionalExcludes += "WEB-INF" + File.separator + "lib" + File.separator + dependency.getArtifactId() + "-" + dependency.getVersion() + ".jar,";
              // the second when a nameclash occurs (artifact will hav groupId prepended before copying it into the lib dir)
              additionalExcludes += "WEB-INF" + File.separator + "lib" + File.separator + dependency.getGroupId() + "-" + dependency.getArtifactId() + "-" + dependency.getVersion() + ".jar,";
            }
          }
          excludes.setValue(excludes.getValue() + additionalExcludes);
        }
      }
    }
  }
}
