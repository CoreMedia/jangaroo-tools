package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.ImmutableList;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaProfileConfiguration;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;

public class MavenSenchaProfileConfiguration implements SenchaProfileConfiguration {

  /**
   * @see SenchaProfileConfiguration#getAdditionalCssNonBundle()
   */
  @Parameter
  private List<String> additionalCssNonBundle;

  /**
   * @see SenchaProfileConfiguration#getAdditionalJsNonBundle()
   */
  @Parameter
  private List<String> additionalJsNonBundle;

  /**
   * @see SenchaProfileConfiguration#getAdditionalCssBundle()
   */
  @Parameter
  private List<String> additionalCssBundle;

  /**
   * @see SenchaProfileConfiguration#getAdditionalJsBundle()
   */
  @Parameter
  private List<String> additionalJsBundle;

  /**
   * @see SenchaProfileConfiguration#getAdditionalCssIncludeInBundle()
   */
  @Parameter
  private List<String> additionalCssIncludeInBundle;

  /**
   * @see SenchaProfileConfiguration#getAdditionalJsIncludeInBundle()
   */
  @Parameter
  private List<String> additionalJsIncludeInBundle;

  /**
   * @see SenchaProfileConfiguration#getEditorPlugins()
   */
  @Parameter
  private List<String> editorPlugins;

  @Override
  public String getProfileName() {
    return null;
  }

  @Override
  public List<String> getAdditionalCssNonBundle() {
    return additionalCssNonBundle != null ? ImmutableList.copyOf(additionalCssNonBundle) : null;
  }

  @Override
  public List<String> getAdditionalJsNonBundle() {
    return additionalJsNonBundle != null ? ImmutableList.copyOf(additionalJsNonBundle) : null;
  }

  @Override
  public List<String> getAdditionalCssBundle() {
    return additionalCssBundle != null ? ImmutableList.copyOf(additionalCssBundle) : null;
  }

  @Override
  public List<String> getAdditionalJsBundle() {
    return additionalJsBundle != null ? ImmutableList.copyOf(additionalJsBundle) : null;
  }

  @Override
  public List<String> getAdditionalCssIncludeInBundle() {
    return additionalCssIncludeInBundle != null ? ImmutableList.copyOf(additionalCssIncludeInBundle) : null;
  }

  @Override
  public List<String> getAdditionalJsIncludeInBundle() {
    return additionalJsIncludeInBundle != null ? ImmutableList.copyOf(additionalJsIncludeInBundle) : null;
  }

  @Override
  public List<String> getEditorPlugins() {
    return editorPlugins;
  }
}
