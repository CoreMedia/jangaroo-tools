package net.jangaroo.jooc.mvnplugin.converter;

import com.google.common.collect.ImmutableList;
import net.jangaroo.jooc.mvnplugin.util.MergeHelper;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JangarooConfig {
  private static final MergeHelper.MergeOptions SENCHA_MERGE_OPTIONS = new MergeHelper.MergeOptions(MergeHelper.ListStrategy.APPEND, MergeHelper.MapStrategy.MERGE);

  private String type;
  private String applicationClass;
  private Map<String, String> appPaths;
  private String theme;
  private Map<String, Object> sencha;
  private Map<String, Object> appManifests;
  private List<String> additionalLocales;
  private List<String> autoLoad;
  private Map<String, Map<String, Object>> command;

  public JangarooConfig() {
  }

  /*public JangarooConfig(String type, String applicationClass, Map<String, String> rootApp, String theme, Map<String, Object> sencha, Map<String, Object> appManifests, List<String> additionalLocales, List<String> autoLoad, Map<String, Map<String, Object>> command) {
    this.type = type;
    this.applicationClass = applicationClass;
    this.appPaths = rootApp;
    this.theme = theme;
    this.sencha = sencha;
    this.appManifests = appManifests;
    this.additionalLocales = additionalLocales;
    this.autoLoad = autoLoad;
    this.command = command;
  }*/

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getApplicationClass() {
    return applicationClass;
  }

  public void setApplicationClass(String applicationClass) {
    this.applicationClass = applicationClass;
  }

  public Map<String, String> getAppPaths() {
    return appPaths;
  }

  public void setAppPaths(Map<String, String> appPaths) {
    this.appPaths = appPaths;
  }

  public void addAppPath(String appDependencyName, String path) {
    if (this.appPaths == null) {
      this.appPaths = new HashMap<>();
    }
    this.appPaths.put(appDependencyName, path);
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = theme;
  }

  public Map<String, Object> getSencha() {
    return sencha;
  }

  public void setSencha(Map<String, Object> sencha) {
    if (sencha == null) {
      this.sencha = null;
      return;
    }
    if (this.sencha == null) {
      this.sencha = new TreeMap<>(new Comparator<String>() {
        private final List<String> ORDER = ImmutableList.of("name", "version", "type", "namespace", "css", "js", "sass");

        @Override
        public int compare(String a, String b) {
          int indexOfA = ORDER.indexOf(a);
          int indexOfB = ORDER.indexOf(b);
          if (indexOfA > -1 && indexOfB > -1) {
            return indexOfA - indexOfB;
          }
          if (indexOfA > -1) {
            return -1;
          }
          if (indexOfB > -1) {
            return 1;
          }
          return a.compareTo(b);
        }
      });
    }
    this.sencha.clear();
    this.addToSencha(sencha);
  }

  public void addToSencha(Map<String, Object> additionalEntries) {
    if (this.sencha == null) {
      setSencha(additionalEntries);
      return;
    }
    MergeHelper.mergeMapIntoBaseMap(this.sencha, additionalEntries, SENCHA_MERGE_OPTIONS);
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
