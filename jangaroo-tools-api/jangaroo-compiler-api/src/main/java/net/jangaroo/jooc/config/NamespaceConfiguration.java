package net.jangaroo.jooc.config;

import java.io.File;

/**
 * A definition of a custom MXML component namespace.
 */
public class NamespaceConfiguration {

  @SuppressWarnings("UnusedDeclaration")
  public NamespaceConfiguration() {
  }

  public NamespaceConfiguration(String uri, File manifest) {
    this.setUri(uri);
    this.setManifest(manifest);
  }

  /**
   * The namespace URI to use for this component package definition.
   *
   * @parameter
   */
  private String uri;

  /**
   * A manifest XML file that contains a component package definition
   * for the given namespace.
   *
   * @parameter expression="${project.build.sourceDirectory}/manifest.xml"
   */
  private File manifest;

  /**
   * The namespace URI to use for this component package definition.
   */
  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  /**
   * A manifest XML file that contains a component package definition
   * for the given namespace.
   */
  public File getManifest() {
    return manifest;
  }

  public void setManifest(File manifest) {
    this.manifest = manifest;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NamespaceConfiguration that = (NamespaceConfiguration) o;

    if (manifest != null ? !manifest.equals(that.manifest) : that.manifest != null) {
      return false;
    }
    return uri != null ? uri.equals(that.uri) : that.uri == null;

  }

  @Override
  public int hashCode() {
    int result = uri != null ? uri.hashCode() : 0;
    result = 31 * result + (manifest != null ? manifest.hashCode() : 0);
    return result;
  }
}
