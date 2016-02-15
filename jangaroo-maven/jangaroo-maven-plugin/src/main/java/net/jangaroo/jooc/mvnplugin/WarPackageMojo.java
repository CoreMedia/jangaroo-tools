package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaHelper;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

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
 *
 * @goal war-package
 * @requiresDependencyResolution runtime
 * @phase prepare-package
 * @threadSafe
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration", "UnusedPrivateField"})
public class WarPackageMojo extends PackageApplicationMojo {

  /**
   * Location of Jangaroo resources of this module (including compiler output, usually under "joo/") to be added
   * to the webapp. Defaults to ${project.build.directory}/jangaroo-output/
   *
   * @parameter expression="${project.build.directory}/jangaroo-output/"
   */
  private File packageSourceDirectory;

  /**
   * @parameter expression="${project.resources}"
   */
  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  private List<Resource> resources;

  /**
   * The sencha configuration to use.
   *
   * @parameter default-value="${senchaConfiguration}"
   */
  private SenchaConfiguration senchaConfiguration;
  
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
    SenchaHelper senchaHelper = new SenchaHelper(project, senchaConfiguration, getLog());
    senchaHelper.prepareSenchaModule();
    senchaHelper.generateSenchaModule();
  }

}
