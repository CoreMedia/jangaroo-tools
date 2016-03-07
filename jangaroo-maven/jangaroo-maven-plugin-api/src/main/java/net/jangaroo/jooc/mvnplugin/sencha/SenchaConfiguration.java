package net.jangaroo.jooc.mvnplugin.sencha;

import net.jangaroo.jooc.mvnplugin.MavenSenchaProfileConfigurationDevelopment;
import net.jangaroo.jooc.mvnplugin.MavenSenchaProfileConfigurationProduction;
import net.jangaroo.jooc.mvnplugin.MavenSenchaProfileConfigurationTesting;

public interface SenchaConfiguration extends SenchaProfileConfiguration {
  boolean isEnabled();

  Type getType();

  boolean isTemporaryWorkspace();

  String getToolkit();

  String getTheme();

  SenchaProfileConfiguration getProduction();

  SenchaProfileConfiguration getDevelopment();

  SenchaProfileConfiguration getTesting();

  String getExtFrameworkDir();

  String getBuildDir();

  String getPackagesDir();

  boolean isSkipBuild();

  boolean isScssFromSrc();

  void setEnabled(boolean enabled);

  void setType(Type type);

  void setTemporaryWorkspace(boolean temporaryWorkspace);

  void setToolkit(String toolkit);

  void setTheme(String theme);

  void setProduction(MavenSenchaProfileConfigurationProduction production);

  void setDevelopment(MavenSenchaProfileConfigurationDevelopment development);

  void setTesting(MavenSenchaProfileConfigurationTesting testing);

  void setExtFrameworkDir(String extFrameworkDir);

  void setBuildDir(String buildDir);

  void setPackagesDir(String packagesDir);

  void setSkipBuild(boolean skipBuild);

  void setScssFromSrc(boolean scssFromSrc);

  public enum Type {
    CODE,
    THEME,
    APP,
    WORKSPACE
  }
}
