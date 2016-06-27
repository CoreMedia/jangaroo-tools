package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.config.NamespaceConfiguration;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

public abstract class AbstractJangarooMojo extends JangarooMojo {

  /**
   * Source directory to scan for files to compile.
   */
  @SuppressWarnings({"UnusedDeclaration", "UnusedPrivateField"})
  @Parameter(defaultValue = "${basedir}/src/main/joo")
  private File sourceDirectory;

  /**
   * A list of custom MXML component namespaces.
   */
  @SuppressWarnings({"UnusedDeclaration", "UnusedPrivateField"})
  @Parameter
  private NamespaceConfiguration[] namespaces;

  public File getSourceDirectory() {
    return sourceDirectory;
  }

  public NamespaceConfiguration[] getNamespaces() {
    return namespaces;
  }
}
