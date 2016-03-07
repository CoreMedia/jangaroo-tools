package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaModuleHelper;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.util.List;

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
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration", "UnusedPrivateField"})
@Mojo(  name = "war-package",
        defaultPhase = LifecyclePhase.PREPARE_PACKAGE,
        requiresDependencyResolution = ResolutionScope.RUNTIME,
        threadSafe = true)
public class WarPackageMojo extends PackageApplicationMojo {

  /**
   * Location of Jangaroo resources of this module (including compiler output, usually under "joo/") to be added
   * to the webapp. Defaults to ${project.build.directory}/jangaroo-output/
   */
  @Parameter(defaultValue = "${project.build.directory}/jangaroo-output/")
  private File packageSourceDirectory;

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  @Parameter(defaultValue = "${project.resources}")
  private List<Resource> resources;

  @Parameter(defaultValue = "${senchaConfiguration}")
  private MavenSenchaConfiguration senchaConfiguration;

  public File getPackageSourceDirectory() {
    return resources.isEmpty() ? null : new File(resources.get(0).getDirectory());
  }

  /**
   * {@inheritDoc}
   *
   * @see org.apache.maven.plugin.Mojo#execute()
   */
  public void execute()
      throws MojoExecutionException, MojoFailureException {
    createWebapp(packageSourceDirectory);

    // for now:
    SenchaHelper senchaHelper = new SenchaModuleHelper(project, senchaConfiguration, getLog());
    senchaHelper.createModule();
    senchaHelper.prepareModule();
  }

}
