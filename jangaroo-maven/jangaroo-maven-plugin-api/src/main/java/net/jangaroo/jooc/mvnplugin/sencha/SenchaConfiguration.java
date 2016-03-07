package net.jangaroo.jooc.mvnplugin.sencha;

public interface SenchaConfiguration extends SenchaProfileConfiguration {
  boolean isEnabled();

  Type getType();

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

  void setToolkit(String toolkit);

  void setTheme(String theme);

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
