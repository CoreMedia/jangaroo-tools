package net.jangaroo.jooc.mvnplugin.converter;

import java.util.List;

public class JangarooMavenPluginConfiguration {
  private PackageType packageType;
  private String theme;
  private String applicationClass;
  private String rootApp;
  private List<String> additionalLocales;
  private List<String> additionalCssNonBundle;
  private List<String> additionalJsIncludeInBundle;
  private List<String> additionalJsNonBundle;
  private String testSuite;
  private String extNamespace;

  public PackageType getPackageType() {
    return packageType;
  }

  public void setPackageType(PackageType packageType) {
    this.packageType = packageType;
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = theme;
  }

  public String getApplicationClass() {
    return applicationClass;
  }

  public void setApplicationClass(String applicationClass) {
    this.applicationClass = applicationClass;
  }

  public String getRootApp() {
    return rootApp;
  }

  public void setRootApp(String rootApp) {
    this.rootApp = rootApp;
  }

  public List<String> getAdditionalLocales() {
    return additionalLocales;
  }

  public void setAdditionalLocales(List<String> additionalLocales) {
    this.additionalLocales = additionalLocales;
  }

  public List<String> getAdditionalCssNonBundle() {
    return additionalCssNonBundle;
  }

  public void setAdditionalCssNonBundle(List<String> additionalCssNonBundle) {
    this.additionalCssNonBundle = additionalCssNonBundle;
  }

  public List<String> getAdditionalJsIncludeInBundle() {
    return additionalJsIncludeInBundle;
  }

  public void setAdditionalJsIncludeInBundle(List<String> additionalJsIncludeInBundle) {
    this.additionalJsIncludeInBundle = additionalJsIncludeInBundle;
  }

  public List<String> getAdditionalJsNonBundle() {
    return additionalJsNonBundle;
  }

  public void setAdditionalJsNonBundle(List<String> additionalJsNonBundle) {
    this.additionalJsNonBundle = additionalJsNonBundle;
  }

  public String getTestSuite() {
    return testSuite;
  }

  public void setTestSuite(String testSuite) {
    this.testSuite = testSuite;
  }

  public String getExtNamespace() {
    return extNamespace;
  }

  public void setExtNamespace(String extNamespace) {
    this.extNamespace = extNamespace;
  }

  public enum PackageType {
    CODE, THEME
  }
}
