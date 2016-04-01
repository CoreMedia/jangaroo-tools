package net.jangaroo.jooc.mvnplugin;

import com.google.common.collect.ImmutableList;
import net.jangaroo.jooc.mvnplugin.sencha.EditorPluginDescriptor;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaProfileConfiguration;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.Collections;
import java.util.List;


public abstract class AbstractSenchaMojo extends AbstractMojo implements MavenSenchaConfiguration {

  @Parameter
  private List<String> additionalCssNonBundle;

  @Parameter
  private List<String> additionalJsNonBundle;

  @Parameter
  private List<String> additionalCssBundle;

  @Parameter
  private List<String> additionalJsBundle;

  @Parameter
  private List<String> additionalCssIncludeInBundle;

  @Parameter
  private List<String> additionalJsIncludeInBundle;

  @Parameter
  private List<MavenEditorPluginDescriptor> editorPlugins;

  @Parameter(defaultValue = "classic")
  private String toolkit;

  @Parameter
  private String theme;

  @Parameter
  private MavenSenchaProfileConfigurationProduction production;

  @Parameter
  private MavenSenchaProfileConfigurationTesting testing;

  @Parameter
  private MavenSenchaProfileConfigurationDevelopment development;

  @Parameter(defaultValue = "${project.build.directory}/ext")
  private String extFrameworkDir;

  @Parameter(defaultValue = "${project.build.directory}/packages")
  private String packagesDir;

  @Parameter(defaultValue = "false")
  private boolean scssFromSrc;

  @Parameter(defaultValue = "${project.groupId}:${project.artifactId}")
  private String remotePackagesArtifact;

  @Parameter(defaultValue = "net.jangaroo.com.sencha:ext-js")
  private String extFrameworkArtifact;

  // ***********************************************************************
  // ************************* GETTERS *************************************
  // ***********************************************************************

  @Override
  public String getToolkit() {
    return toolkit;
  }

  @Override
  public String getTheme() {
    return theme;
  }

  @Override
  public SenchaProfileConfiguration getProduction() {
    return production;
  }

  @Override
  public SenchaProfileConfiguration getDevelopment() {
    return development;
  }

  @Override
  public SenchaProfileConfiguration getTesting() {
    return testing;
  }

  @Override
  public String getExtFrameworkDir() {
    return extFrameworkDir;
  }

  @Override
  public String getExtFrameworkArtifact() {
    return extFrameworkArtifact;
  }


  @Override
  public String getPackagesDir() {
    return packagesDir;
  }

  @Override
  public boolean isScssFromSrc() {
    return scssFromSrc;
  }

  @Override
  public String getProfileName() {
    return null;
  }

  @Override
  public List<String> getAdditionalCssNonBundle() {
    return additionalCssNonBundle != null ? ImmutableList.copyOf(additionalCssNonBundle) : Collections.<String>emptyList();
  }

  @Override
  public List<String> getAdditionalJsNonBundle() {
    return additionalJsNonBundle != null ? ImmutableList.copyOf(additionalJsNonBundle) : Collections.<String>emptyList();
  }

  @Override
  public List<String> getAdditionalCssBundle() {
    return additionalCssBundle != null ? ImmutableList.copyOf(additionalCssBundle) : Collections.<String>emptyList();
  }

  @Override
  public List<String> getAdditionalJsBundle() {
    return additionalJsBundle != null ? ImmutableList.copyOf(additionalJsBundle) : Collections.<String>emptyList();
  }

  @Override
  public List<String> getAdditionalCssIncludeInBundle() {
    return additionalCssIncludeInBundle != null ? ImmutableList.copyOf(additionalCssIncludeInBundle) : Collections.<String>emptyList();
  }

  @Override
  public List<String> getAdditionalJsIncludeInBundle() {
    return additionalJsIncludeInBundle != null ? ImmutableList.copyOf(additionalJsIncludeInBundle) : Collections.<String>emptyList();
  }

  @Override
  public List<? extends EditorPluginDescriptor> getEditorPlugins() {
    return editorPlugins != null ? editorPlugins : Collections.<EditorPluginDescriptor>emptyList();
  }

  @Override
  public String getRemotePackagesArtifact() {
    return remotePackagesArtifact;
  }

  // ***********************************************************************
  // ************************* SETTERS *************************************
  // ***********************************************************************

  @Override
  public void setExtFrameworkDir(String extFrameworkDir) {
    this.extFrameworkDir = extFrameworkDir;
  }

  @Override
  public void setPackagesDir(String packagesDir) {
    this.packagesDir = packagesDir;
  }

  @Override
  public void setScssFromSrc(boolean scssFromSrc) {
    this.scssFromSrc = scssFromSrc;
  }

}
