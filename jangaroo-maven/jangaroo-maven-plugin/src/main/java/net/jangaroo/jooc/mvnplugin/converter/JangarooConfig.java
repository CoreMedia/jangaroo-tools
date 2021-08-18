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
  private Object sencha;
  private Map<String, Object> appManifests;
  private List<String> additionalLocales;
  private List<String> additionalCssIncludeInBundle;
  private List<String> additionalCssNonBundle;
  private List<String> additionalJsIncludeInBundle;
  private List<String> additionalJsNonBundle;
  private List<String> autoLoad;
  private Map<String, Map<String, Object>> command;

  public JangarooConfig() {
  }

  public JangarooConfig(String type, String extName, String outputDirectory, String applicationClass, String rootApp, String extNamespace, String theme, Object sencha, Map<String, Object> appManifests, List<String> additionalLocales, List<String> additionalCssIncludeInBundle, List<String> additionalCssNonBundle, List<String> additionalJsIncludeInBundle, List<String> additionalJsNonBundle, Map<String, Map<String, Object>> command) {
    this.type = type;
    this.extName = extName;
    this.outputDirectory = outputDirectory;
    this.applicationClass = applicationClass;
    this.rootApp = rootApp;
    this.extNamespace = extNamespace;
    this.theme = theme;
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
    this.type = type;
  }

  public String getExtName() {
    return extName;
  }

  public void setExtName(String extName) {
    this.extName = extName;
  }

  public String getOutputDirectory() {
    return outputDirectory;
  }

  public void setOutputDirectory(String outputDirectory) {
    this.outputDirectory = outputDirectory;
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
    this.extSassNamespace = extSassNamespace;
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = theme;
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
    this.appManifests = appManifests;
  }

  public List<String> getAdditionalLocales() {
    return additionalLocales;
  }

  public void setAdditionalLocales(List<String> additionalLocales) {
    this.additionalLocales = additionalLocales;
  }

  public List<String> getAdditionalCssIncludeInBundle() {
    return additionalCssIncludeInBundle;
  }

  public void setAdditionalCssIncludeInBundle(List<String> additionalCssIncludeInBundle) {
    this.additionalCssIncludeInBundle = additionalCssIncludeInBundle;
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

  public List<String> getAutoLoad() {
    return autoLoad;
  }

  public void setAutoLoad(List<String> autoLoad) {
    this.autoLoad = autoLoad;
  }

  public Map<String, Map<String, Object>> getCommand() {
    return command;
  }

  public void setCommand(Map<String, Map<String, Object>> command) {
    this.command = command;
  }
}
