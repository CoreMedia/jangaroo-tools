package net.jangaroo.jooc.config;

/**
 * A definition of a custom MXML component namespace.
 */
public class NamespaceConfiguration {

  public NamespaceConfiguration() {
  }

  public NamespaceConfiguration(String uri, String manifest) {
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
   * The file path and name of a manifest XML file that contains a component package definition
   * for the given namespace. The path is relative to the source directory.
   *
   * @parameter expression="manifest.xml"
   */
  private String manifest;

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
  public String getManifest() {
    return manifest;
  }

  public void setManifest(String manifest) {
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
    if (uri != null ? !uri.equals(that.uri) : that.uri != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = uri != null ? uri.hashCode() : 0;
    result = 31 * result + (manifest != null ? manifest.hashCode() : 0);
    return result;
  }
}
