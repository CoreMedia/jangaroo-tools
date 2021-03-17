package net.jangaroo.jooc.mvnplugin.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JangarooConfig {
  private String type;
  private String extName;
  private String outputDirectory;
  private String applicationClass;
  private String rootApp;
  private String extNamespace;
  private String extSassNamespace;
  private String theme;
  private String testSuite;
  private Object sencha;
  private Map<String, Object> appManifests;
  private List<String> additionalLocales;
  private List<String> additionalCssIncludeInBundle;
  private List<String> additionalCssNonBundle;
  private List<String> additionalJsIncludeInBundle;
  private List<String> additionalJsNonBundle;
  private Map<String, Object> command;

  public JangarooConfig() {
  }

  public JangarooConfig(String type, String extName, String outputDirectory, String applicationClass, String rootApp, String extNamespace, String theme, String testSuite, Object sencha, Map<String, Object> appManifests, List<String> additionalLocales, List<String> additionalCssIncludeInBundle, List<String> additionalCssNonBundle, List<String> additionalJsIncludeInBundle, List<String> additionalJsNonBundle, Map<String, Object> command) {
    this.type = type;
    this.extName = extName;
    this.outputDirectory = outputDirectory;
    this.applicationClass = applicationClass;
    this.rootApp = rootApp;
    this.extNamespace = extNamespace;
    this.theme = theme;
    this.testSuite = testSuite;
    this.sencha = sencha;
    this.appManifests = appManifests;
    this.additionalLocales = additionalLocales;
    this.additionalCssIncludeInBundle = additionalCssIncludeInBundle;
    this.additionalCssNonBundle = additionalCssNonBundle;
    this.additionalJsIncludeInBundle = additionalJsIncludeInBundle;
    this.additionalJsNonBundle = additionalJsNonBundle;
    this.command = command;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    if (type != null && !type.isEmpty()) {
      this.type = type;
    }
  }

  public String getExtName() {
    return extName;
  }

  public void setExtName(String extName) {
    if (extName != null && !extName.isEmpty()) {
      this.extName = extName;
    }
  }

  public String getOutputDirectory() {
    return outputDirectory;
  }

  public void setOutputDirectory(String outputDirectory) {
    if (outputDirectory != null && !outputDirectory.isEmpty()) {
      this.outputDirectory = outputDirectory;
    }
  }

  public String getApplicationClass() {
    return applicationClass;
  }

  public void setApplicationClass(String applicationClass) {
    if (applicationClass != null && !applicationClass.isEmpty()) {
      this.applicationClass = applicationClass;
    }
  }

  public String getRootApp() {
    return rootApp;
  }

  public void setRootApp(String rootApp) {
    if (rootApp != null && !rootApp.isEmpty()) {
      this.rootApp = rootApp;
    }
  }

  public String getExtNamespace() {
    return extNamespace;
  }

  public void setExtNamespace(String extNamespace) {
    if (extNamespace != null) {
      this.extNamespace = extNamespace;
    }
  }

  public String getExtSassNamespace() {
    return extSassNamespace;
  }

  public void setExtSassNamespace(String extSassNamespace) {
    if (extSassNamespace != null && !extSassNamespace.isEmpty()) {
      this.extSassNamespace = extSassNamespace;
    }
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    if (theme != null && !theme.isEmpty()) {
      this.theme = theme;
    }
  }

  public String getTestSuite() {
    return testSuite;
  }

  public void setTestSuite(String testSuite) {
    if (testSuite != null && !testSuite.isEmpty()) {
      this.testSuite = testSuite;
    }
  }

  public Object getSencha() {
    return sencha;
  }

  public void setSencha(Object sencha) {
    this.sencha = sencha;
  }

  public Map<String, Object> getAppManifests() {
    return appManifests;
  }

  public void addAppManifest(String name, Object manifest) {
    if (this.appManifests == null) {
      this.appManifests = new HashMap<>();
    }
    this.appManifests.put(name, manifest);
  }

  public void setAppManifests(Map<String, Object> appManifests) {
    if (appManifests != null && !appManifests.isEmpty()) {
      this.appManifests = appManifests;
    }
  }

  public List<String> getAdditionalLocales() {
    return additionalLocales;
  }

  public void setAdditionalLocales(List<String> additionalLocales) {
    if (additionalLocales != null && !additionalLocales.isEmpty()) {
      this.additionalLocales = additionalLocales;
    }
  }

  public List<String> getAdditionalCssIncludeInBundle() {
    return additionalCssIncludeInBundle;
  }

  public void setAdditionalCssIncludeInBundle(List<String> additionalCssIncludeInBundle) {
    if (additionalCssIncludeInBundle != null && !additionalCssIncludeInBundle.isEmpty()) {
      this.additionalCssIncludeInBundle = additionalCssIncludeInBundle;
    }
  }

  public List<String> getAdditionalCssNonBundle() {
    return additionalCssNonBundle;
  }

  public void setAdditionalCssNonBundle(List<String> additionalCssNonBundle) {
    if (additionalCssNonBundle != null && !additionalCssNonBundle.isEmpty()) {
      this.additionalCssNonBundle = additionalCssNonBundle;
    }
  }

  public List<String> getAdditionalJsIncludeInBundle() {
    return additionalJsIncludeInBundle;
  }

  public void setAdditionalJsIncludeInBundle(List<String> additionalJsIncludeInBundle) {
    if (additionalJsIncludeInBundle != null && !additionalJsIncludeInBundle.isEmpty()) {
      this.additionalJsIncludeInBundle = additionalJsIncludeInBundle;
    }
  }

  public List<String> getAdditionalJsNonBundle() {
    return additionalJsNonBundle;
  }

  public void setAdditionalJsNonBundle(List<String> additionalJsNonBundle) {
    if (additionalJsNonBundle != null && !additionalJsNonBundle.isEmpty()) {
      this.additionalJsNonBundle = additionalJsNonBundle;
    }
  }

  public Map<String, Object> getCommand() {
    return command;
  }

  public void setCommand(Map<String, Object> command) {
    if (command != null && !command.isEmpty()) {
      this.command = command;
    }
  }
}
