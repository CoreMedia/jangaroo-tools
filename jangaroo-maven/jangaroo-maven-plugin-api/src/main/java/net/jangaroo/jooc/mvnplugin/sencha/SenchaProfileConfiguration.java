package net.jangaroo.jooc.mvnplugin.sencha;

import javax.annotation.Nonnull;
import java.util.List;

public interface SenchaProfileConfiguration {

  /**
   * A list of paths to CSS files to include that are not loaded via the class loader (usually in resources folder).
   * "bundle" option will be set to false, "includeInBundle" will be set to false.
   */
  @Nonnull
  List<String> getAdditionalCssNonBundle();

  /**
   * A list of paths to JS files to include that are not loaded via the class loader (usually in resources folder).
   * "bundle" option will be set to false, "includeInBundle" will be set to false.
   */
  @Nonnull
  List<String> getAdditionalJsNonBundle();

  /**
   * A list of paths to CSS files to include that are not loaded via the class loader (usually in resources folder).
   * "bundle" option will be set to false, "includeInBundle" will be set to true.
   */
  @Nonnull
  List<String> getAdditionalCssIncludeInBundle();

  /**
   * A list of paths to JS files to include that are not loaded via the class loader (usually in resources folder).
   * "bundle" option will be set to false, "includeInBundle" will be set to true.
   */
  @Nonnull
  List<String> getAdditionalJsIncludeInBundle();

  /**
   * Specifies the descriptors of the editorPlugins to be loaded.
   */
  @Nonnull
  List<? extends EditorPluginDescriptor> getEditorPlugins();
}
