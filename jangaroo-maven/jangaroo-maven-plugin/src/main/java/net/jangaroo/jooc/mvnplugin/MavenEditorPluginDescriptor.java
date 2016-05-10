package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.sencha.EditorPluginDescriptor;
import org.apache.maven.plugins.annotations.Parameter;

public class MavenEditorPluginDescriptor implements EditorPluginDescriptor {

  /**
   * @see EditorPluginDescriptor#getMainClass()
   */
  @Parameter
  private String mainClass;

  /**
   * @see EditorPluginDescriptor#getName()
   */
  @Parameter
  private String name;

  /**
   * @see EditorPluginDescriptor#getRequiredLicenseFeature()
   */
  @Parameter
  private String requiredLicenseFeature;

  /**
   * @see EditorPluginDescriptor#getRequiredGroup()
   */
  @Parameter
  private String requiredGroup;

  @Override
  public String getMainClass() {
    return mainClass;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getRequiredLicenseFeature() {
    return requiredLicenseFeature;
  }

  @Override
  public String getRequiredGroup() {
    return requiredGroup;
  }
}
