package net.jangaroo.jooc.mvnplugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.ArchiveFileFilter;
import org.codehaus.plexus.archiver.ArchiveFilterException;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

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
 *     &lt;goals>
 *      &lt;goal>compile&lt;/goal>
 *      &lt;goal>war-package&lt;/goal>
 *    &lt;/goals>
 *   &lt;/execution>
 *  &lt;/executions>
 * &lt;/plugin>
 * ...
 * </pre>
 *
 * @goal war-package
 * @requiresDependencyResolution runtime
 * @phase prepare-package
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration", "UnusedPrivateField"})
public class WarPackageMojo extends PackageApplicationMojo {

  /**
   * The directory where the webapp is built. Default is <code>${project.build.directory}/${project.build.finalName}</code>
   * exactly as the default of the maven-war-plugin.
   *
   * @parameter expression="${project.build.directory}/${project.build.finalName}"
   * @required
   */
  private File webappDirectory;
  /**
   * Location of Jangaroo resources of this module (including compiler output, usually under "joo/") to be added
   * to the webapp. Defaults to ${project.build.directory}/jangaroo-output/
   *
   * @parameter expression="${project.build.directory}/jangaroo-output/"
   */
  private File packageSourceDirectory;

  public File getPackageSourceDirectory() {
    return packageSourceDirectory;
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.maven.plugin.Mojo#execute()
   */
  public void execute()
      throws MojoExecutionException, MojoFailureException {

    excludeFromWarPackaging();
    createWebapp(webappDirectory);
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
          excludes.setValue(excludes.getValue() + additionalExcludes);
        }
      }
    }
  }

  private static class WarPackageArchiveFilter implements ArchiveFileFilter {
    public boolean include(InputStream dataStream, String entryName) throws ArchiveFilterException {
      return !entryName.startsWith("META-INF");
    }
  }
}
