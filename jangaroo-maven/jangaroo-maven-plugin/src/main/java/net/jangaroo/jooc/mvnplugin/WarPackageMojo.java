package net.jangaroo.jooc.mvnplugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.IOException;
import java.io.Writer;

/**
 * Like <code>generate-amd</code>, the <code>war-package</code> goal generates an AMD module with all
 * Maven dependencies to other Jangaroo modules as AMD dependencies, but also a bootstrap AMD module called
 * <code>jangaroo-application</code> that requires this module. 
 * This goal is NOT bound to the jangaroo lifecycle. It is aimed to be used in conjunction with
 * the <code>war</code> lifecycle by defining its execution as shown in the following snippet<br/>
 * <pre>
 * ...
 * &lt;plugin>
 *  &lt;groupId>net.jangaroo&lt;/groupId>
 *  &lt;artifactId>jangaroo-maven-plugin&lt;/artifactId>
 *  &lt;extensions>true&lt;/extensions>
 *  &lt;executions>
 *   &lt;execution>
 *     &lt;goals>
 *      &lt;goal>war-package&lt;/goal>
 *    &lt;/goals>
 *   &lt;/execution>
 *  &lt;/executions>
 * &lt;/plugin>
 * ...
 * </pre>
 *
 * @goal war-package
 * @phase prepare-package
 * @requiresDependencyResolution runtime
 * @threadSafe
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "UnusedDeclaration", "UnusedPrivateField"})
public class WarPackageMojo extends GenerateModuleAMDMojo {

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    super.execute();
    Writer amdWriter = null;
    try {
      amdWriter = createAMDFile("jangaroo-application");
      String projectAMD = computeAMDName(project.getGroupId(), project.getArtifactId());
      amdWriter.write("define(\"jangaroo-application\", [\"" + projectAMD + "\"], function() {});");
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to create bootstrap AMD script output file.", e);
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
}
