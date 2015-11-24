package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.config.NamespaceConfiguration;

import java.io.File;

public abstract class AbstractJangarooMojo extends JangarooMojo {
  /**
   * Source directory to scan for files to compile.
   *
   * @parameter expression="${project.build.sourceDirectory}"
   */
  private File sourceDirectory;

  /**
   * A list of custom MXML component namespaces.
   *
   * @parameter
   */

  private NamespaceConfiguration[] namespaces;

  public File getSourceDirectory() {
    return sourceDirectory;
  }

  public NamespaceConfiguration[] getNamespaces() {
    return namespaces;
  }
}
