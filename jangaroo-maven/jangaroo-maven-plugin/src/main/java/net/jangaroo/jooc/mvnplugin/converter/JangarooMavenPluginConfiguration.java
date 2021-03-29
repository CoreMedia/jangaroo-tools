package net.jangaroo.jooc.mvnplugin.converter;

import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JangarooMavenPluginConfiguration {
  private String packageType;
  private String theme;
  private String applicationClass;
  private String rootApp;
  private List<String> additionalLocales;
  private List<String> additionalCssNonBundle;
  private List<String> additionalCssIncludeInBundle;
  private List<String> additionalJsIncludeInBundle;
  private List<String> additionalJsNonBundle;
  private String testSuite;
  private String extNamespace;
  private String extSassNamespace;


  public JangarooMavenPluginConfiguration(Model mavenModel) {
    Optional<Plugin> optionalPlugin = mavenModel.getBuild().getPlugins().stream()
            .filter(plugin ->
                    "net.jangaroo".equals(plugin.getGroupId()) && "jangaroo-maven-plugin".equals(plugin.getArtifactId()))
            .findFirst();
    if (optionalPlugin.isPresent()) {
      Xpp3Dom configuration = (Xpp3Dom) optionalPlugin.get().getConfiguration();
      if (configuration != null) {
        packageType = getConfigString(configuration, "packageType") != null ? getConfigString(configuration, "packageType") : "code";
        theme = getConfigString(configuration, "theme");
        applicationClass = getConfigString(configuration, "applicationClass");
        rootApp = getConfigString(configuration, "rootApp");
        testSuite = getConfigString(configuration, "testSuite");
        extNamespace = getConfigString(configuration, "extNamespace");
        extSassNamespace = getConfigString(configuration, "extSassNamespace");
        additionalLocales = getConfigList(configuration, "additionalLocales");
        additionalCssNonBundle = getConfigList(configuration, "additionalCssNonBundle");
        additionalCssIncludeInBundle = getConfigList(configuration, "additionalCssIncludeInBundle");
        additionalJsIncludeInBundle = getConfigList(configuration, "additionalJsIncludeInBundle");
        additionalJsNonBundle = getConfigList(configuration, "additionalJsNonBundle");
      }
    }
  }

  private List<String> getConfigList(Xpp3Dom config, String property) {
    Xpp3Dom child = config.getChild(property);
    if (child != null) {
      List<Xpp3Dom> value = Arrays.asList(child.getChildren());
      if (!value.isEmpty()) {
        return value.stream().map(Xpp3Dom::getValue).collect(Collectors.toList());
      } else {
        return new ArrayList<>();
      }
    } else {
      return null;
    }
  }

  private String getConfigString(Xpp3Dom config, String property) {
    Xpp3Dom child = config.getChild(property);
    //todo is this ok?
    if (child != null) {
      /*Xpp3Dom value = child.getChild("value");
      if (value != null) {
        return value.getValue();
      } else {
        return "";
      }
       */
      return child.getValue();
    } else {
      return null;
    }
  }

  public JangarooMavenPluginConfiguration() {
  }

  public String getPackageType() {
    return packageType;
  }

  public void setPackageType(String packageType) {
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
    if (extNamespace == null) {
      extNamespace = "";
    }
    return extNamespace;
  }

  public void setExtNamespace(String extNamespace) {
    this.extNamespace = extNamespace;
  }

  public String getExtSassNamespace() {
    return extSassNamespace;
  }

  public void setExtSassNamespace(String extSassNamespace) {
    this.extSassNamespace = extSassNamespace;
  }

  public List<String> getAdditionalCssIncludeInBundle() {
    return additionalCssIncludeInBundle;
  }

  public void setAdditionalCssIncludeInBundle(List<String> additionalCssIncludeInBundle) {
    this.additionalCssIncludeInBundle = additionalCssIncludeInBundle;
  }
}
