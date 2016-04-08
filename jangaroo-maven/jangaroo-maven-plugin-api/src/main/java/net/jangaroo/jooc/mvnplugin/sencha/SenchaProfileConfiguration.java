package net.jangaroo.jooc.mvnplugin.sencha;

import java.util.List;

public interface SenchaProfileConfiguration {

  /**
   * The profile name to use. Can be set to null, so no name is assigned.
   *
   * All profiles used need to have a unique name otherwise profile specific settings might override each other.
   *
   * @return the profile name to use, may be null.
   */
  String getProfileName();

  /**
   * A list of paths to CSS files to include that are not loaded via the class loader (usually in resources folder).
   * "bundle" option will be set to false, "includeInBundle" will be set to false.
   */
  List<String> getAdditionalCssNonBundle();

  /**
   * A list of paths to JS files to include that are not loaded via the class loader (usually in resources folder).
   * "bundle" option will be set to false, "includeInBundle" will be set to false.
   */
  List<String> getAdditionalJsNonBundle();

  /**
   * A list of paths to CSS files to include that are not loaded via the class loader (usually in resources folder).
   * "bundle" option will be set to false, "includeInBundle" will be set to true.
   */
  List<String> getAdditionalCssIncludeInBundle();

  /**
   * A list of paths to JS files to include that are not loaded via the class loader (usually in resources folder).
   * "bundle" option will be set to false, "includeInBundle" will be set to true.
   */
  List<String> getAdditionalJsIncludeInBundle();

  /**
   * Specifies the descriptors of the editorPlugins to be loaded.
   */
  List<? extends EditorPluginDescriptor> getEditorPlugins();
}
