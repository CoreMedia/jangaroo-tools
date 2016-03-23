package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaProfileConfiguration;

public interface MavenSenchaConfiguration extends SenchaConfiguration, SenchaProfileConfiguration {

  /**
   * Returns the coordinates of the local Maven module that contains all remote package dependencies
   * used by the Sencha Cmd build process.
   * Example:
   * <pre>
   * &lt;remotePackagesArtifact>net.jangaroo:remote-packages&lt;/remotePackagesArtifact>
   * </pre>
   * @return the coordinates of the remote packages artifact
   */
  String getRemotePackagesArtifact();

  /**
   * Returns the coordinates of Maven artifact that contains the ExtJs framework.
   * Example:
   * <pre>
   * &lt;extFrameworkArtifact>net.jangaroo.com.sencha:ext-js&lt;/extFrameworkArtifact>
   * </pre>
   * @return the coordinates of the ExtJs framework artifact
   */
  String getExtFrameworkArtifact();

}
