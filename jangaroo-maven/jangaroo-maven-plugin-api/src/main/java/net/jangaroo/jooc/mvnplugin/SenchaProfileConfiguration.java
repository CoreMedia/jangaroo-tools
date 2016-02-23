package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.ImmutableList;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;

public class SenchaProfileConfiguration {

  /**
   * A list of paths to CSS files to include that are not loaded via the class loader (usually in resources folder).
   * Bundle option will be set to false.
   */
  @Parameter
  private List<String> additionalCssNonBundle;

  /**
   * A list of paths to JS files to include that are not loaded via the class loader (usually in resources folder).
   * Bundle option will be set to false.
   */
  @Parameter
  private List<String> additionalJsNonBundle;

  /**
   * A list of paths to CSS files to include that are not loaded via the class loader (usually in resources folder).
   * Bundle option will be set to true.
   */
  @Parameter
  private List<String> additionalCssBundle;

  /**
   * A list of paths to JS files to include that are not loaded via the class loader (usually in resources folder).
   * Bundle option will be set to true.
   */
  @Parameter
  private List<String> additionalJsBundle;


  public List<String> getAdditionalCssNonBundle() {
    return additionalCssNonBundle != null ? ImmutableList.copyOf(additionalCssNonBundle) : null;
  }

  public List<String> getAdditionalJsNonBundle() {
    return additionalJsNonBundle != null ? ImmutableList.copyOf(additionalJsNonBundle) : null;
  }

  public List<String> getAdditionalCssBundle() {
    return additionalCssBundle != null ? ImmutableList.copyOf(additionalCssBundle) : null;
  }

  public List<String> getAdditionalJsBundle() {
    return additionalJsBundle != null ? ImmutableList.copyOf(additionalJsBundle) : null;
  }
}
