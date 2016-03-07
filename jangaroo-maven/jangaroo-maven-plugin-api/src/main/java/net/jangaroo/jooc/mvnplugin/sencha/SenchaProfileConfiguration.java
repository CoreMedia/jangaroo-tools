package net.jangaroo.jooc.mvnplugin.sencha;

import java.util.List;

public interface SenchaProfileConfiguration {
  String getProfileName();

  List<String> getAdditionalCssNonBundle();

  List<String> getAdditionalJsNonBundle();

  List<String> getAdditionalCssBundle();

  List<String> getAdditionalJsBundle();

  List<String> getAdditionalCssIncludeInBundle();

  List<String> getAdditionalJsIncludeInBundle();

  List<String> getEditorPlugins();
}
