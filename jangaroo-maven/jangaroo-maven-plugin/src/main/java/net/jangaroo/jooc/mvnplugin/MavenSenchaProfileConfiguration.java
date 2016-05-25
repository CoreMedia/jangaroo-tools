package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.ImmutableList;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaProfileConfiguration;
import org.apache.maven.plugins.annotations.Parameter;

import javax.annotation.Nonnull;
import java.util.Collections;
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
   * @see SenchaProfileConfiguration#getRequiredClasses()
   */
  @Parameter
  private List<String> requiredClasses;

  public void setAdditionalCssIncludeInBundle(List<String> additionalCssIncludeInBundle) {
    this.additionalCssIncludeInBundle = additionalCssIncludeInBundle;
  }

  public void setAdditionalCssNonBundle(List<String> additionalCssNonBundle) {
    this.additionalCssNonBundle = additionalCssNonBundle;
  }

  public void setAdditionalJsIncludeInBundle(List<String> additionalJsIncludeInBundle) {
    this.additionalJsIncludeInBundle = additionalJsIncludeInBundle;
  }

  public void setAdditionalJsNonBundle(List<String> additionalJsNonBundle) {
    this.additionalJsNonBundle = additionalJsNonBundle;
  }

  public void setRequiredClasses(List<String> requiredClasses) {
    this.requiredClasses = requiredClasses;
  }

  @Nonnull
  @Override
  public List<String> getAdditionalCssNonBundle() {
    return additionalCssNonBundle != null ? ImmutableList.copyOf(additionalCssNonBundle) : Collections.<String>emptyList();
  }

  @Nonnull
  @Override
  public List<String> getAdditionalJsNonBundle() {
    return additionalJsNonBundle != null ? ImmutableList.copyOf(additionalJsNonBundle) : Collections.<String>emptyList();
  }

  @Nonnull
  @Override
  public List<String> getAdditionalCssIncludeInBundle() {
    return additionalCssIncludeInBundle != null ? ImmutableList.copyOf(additionalCssIncludeInBundle) : Collections.<String>emptyList();
  }

  @Nonnull
  @Override
  public List<String> getAdditionalJsIncludeInBundle() {
    return additionalJsIncludeInBundle != null ? ImmutableList.copyOf(additionalJsIncludeInBundle) : Collections.<String>emptyList();
  }

  @Nonnull
  @Override
  public List<String> getRequiredClasses() {
    return requiredClasses == null ? Collections.<String>emptyList() : requiredClasses;
  }
}
