package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaProfileConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.util.LinkedHashMap;
import java.util.Map;

public class SenchaConfigurationConfigurer implements Configurer {

  static final String PRODUCTION = "production";
  static final String TESTING = "testing";
  static final String DEVELOPMENT = "development";
  static final String TOOLKIT = "toolkit";
  static final String EXTEND = "extend";
  static final String THEME = "theme";

  private MavenProject project;
  private SenchaConfiguration senchaConfiguration;

  public SenchaConfigurationConfigurer(MavenProject project, SenchaConfiguration senchaConfiguration) {
    this.project = project;
    this.senchaConfiguration = senchaConfiguration;
  }

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    SenchaProfileConfigurationConfigurer profileConfigurationConfigurer = new SenchaProfileConfigurationConfigurer(senchaConfiguration);
    profileConfigurationConfigurer.configure(config);
    configureAdditionalResourcesForProfile(config, PRODUCTION, senchaConfiguration.getProduction());
    configureAdditionalResourcesForProfile(config, TESTING, senchaConfiguration.getTesting());
    configureAdditionalResourcesForProfile(config, DEVELOPMENT, senchaConfiguration.getDevelopment());

    String themePackageName = SenchaUtils.getSenchaPackageNameForTheme(senchaConfiguration.getTheme(), project);
    if (SenchaConfiguration.Type.CODE.equals(senchaConfiguration.getType())) {
      config.put(TOOLKIT, senchaConfiguration.getToolkit());
      config.put(THEME, themePackageName);
    }
    if (SenchaConfiguration.Type.THEME.equals(senchaConfiguration.getType())) {
      config.put(TOOLKIT, senchaConfiguration.getToolkit());
      config.put(EXTEND, themePackageName);
    }
    if (SenchaConfiguration.Type.APP.equals(senchaConfiguration.getType())) {
      config.put(THEME, themePackageName);
    }
  }


  private void configureAdditionalResourcesForProfile(Map<String, Object> config, String profileName, SenchaProfileConfiguration senchaProfileConfiguration) throws MojoExecutionException {
    if (senchaProfileConfiguration != null) {
      LinkedHashMap<String, Object> profileConfig = new LinkedHashMap<String, Object>();
      config.put(profileName, profileConfig);
      SenchaProfileConfigurationConfigurer profileConfigurationConfigurer = new SenchaProfileConfigurationConfigurer(senchaProfileConfiguration);
      profileConfigurationConfigurer.configure(profileConfig);
    }
  }

}
