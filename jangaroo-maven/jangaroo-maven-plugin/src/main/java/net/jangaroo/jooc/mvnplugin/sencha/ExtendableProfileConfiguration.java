package net.jangaroo.jooc.mvnplugin.sencha;

import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.List;

public class ExtendableProfileConfiguration implements SenchaProfileConfiguration {

  private List<String> additionalCssNonBundle;
  private List<String> additionalJsNonBundle;
  private List<String> additionalCssIncludeInBundle;
  private List<String> additionalJsIncludeInBundle;
  private List<? extends EditorPluginDescriptor> editorPlugins;

  public ExtendableProfileConfiguration(SenchaProfileConfiguration senchaProfileConfiguration) {
    additionalCssNonBundle = Lists.newArrayList();
    additionalJsNonBundle = Lists.newArrayList();
    additionalCssIncludeInBundle = Lists.newArrayList();
    additionalJsIncludeInBundle = Lists.newArrayList();
    editorPlugins = Lists.newArrayList();

    if (null != senchaProfileConfiguration) {
      additionalCssNonBundle.addAll(senchaProfileConfiguration.getAdditionalCssNonBundle());
      additionalJsNonBundle.addAll(senchaProfileConfiguration.getAdditionalJsNonBundle());
      additionalCssIncludeInBundle.addAll(senchaProfileConfiguration.getAdditionalCssIncludeInBundle());
      additionalJsIncludeInBundle.addAll(senchaProfileConfiguration.getAdditionalJsIncludeInBundle());
      editorPlugins = Lists.newArrayList(senchaProfileConfiguration.getEditorPlugins());
    }
  }

  @Nonnull
  @Override
  public List<String> getAdditionalCssNonBundle() {
    return additionalCssNonBundle;
  }

  @Nonnull
  @Override
  public List<String> getAdditionalJsNonBundle() {
    return additionalJsNonBundle;
  }

  @Nonnull
  @Override
  public List<String> getAdditionalCssIncludeInBundle() {
    return additionalCssIncludeInBundle;
  }

  @Nonnull
  @Override
  public List<String> getAdditionalJsIncludeInBundle() {
    return additionalJsIncludeInBundle;
  }

  @Nonnull
  @Override
  public List<? extends EditorPluginDescriptor> getEditorPlugins() {
    return editorPlugins;
  }

  public void addAdditionalCssNonBundle(String path) {
    additionalCssNonBundle.add(path);
  }

  public void addAdditionalJsNonBundle(String path) {
    additionalJsNonBundle.add(path);
  }

  public void addAdditionalCssIncludeInBundle(String path) {
    additionalCssIncludeInBundle.add(path);
  }

  public void addAdditionalJsIncludeInBundle(String path) {
    additionalJsIncludeInBundle.add(path);
  }
}
