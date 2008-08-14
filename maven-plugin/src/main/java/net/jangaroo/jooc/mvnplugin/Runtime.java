package net.jangaroo.jooc.mvnplugin;

/**
 * Configuration type for the &lt;runtime> parameter of the {@link CopyRuntimeMojo copy-runtime} mojo.
 */
public class Runtime {
  private String groupId = GROUP_ID_JANGAROO;
  private String artifactId = ARTIFACT_ID_JOOC;
  private String classifier = CLASSIFIER_RUNTIME;
  private String version;

  public static final String GROUP_ID_JANGAROO = "net.jangaroo";
  public static final String ARTIFACT_ID_JOOC = "jangaroo-compiler";
  public static final String TYPE_RUNTIME = "zip";
  public static final String CLASSIFIER_RUNTIME = "runtime";

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public void setArtifactId(String artifactId) {
    this.artifactId = artifactId;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getClassifier() {
    return classifier;
  }

  public void setClassifier(String classifier) {
    this.classifier = classifier;
  }
}
