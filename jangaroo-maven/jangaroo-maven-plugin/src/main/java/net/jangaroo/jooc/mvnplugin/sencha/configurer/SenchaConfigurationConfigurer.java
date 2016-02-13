package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.SenchaProfileConfiguration;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.LinkedHashMap;
import java.util.Map;

public class SenchaConfigurationConfigurer implements Configurer {

  static final String PRODUCTION = "production";
  static final String TESTING = "testing";
  static final String DEVELOPMENT = "development";
  static final String TOOLKIT = "toolkit";
  static final String EXTENDS = "extends";
  static final String THEME = "theme";

  private SenchaConfiguration senchaConfiguration;

  public SenchaConfigurationConfigurer(SenchaConfiguration senchaConfiguration) {
    this.senchaConfiguration = senchaConfiguration;
  }

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    SenchaProfileConfigurationConfigurer profileConfigurationConfigurer = new SenchaProfileConfigurationConfigurer(senchaConfiguration);
    profileConfigurationConfigurer.configure(config);
    configureAdditionalResourcesForProfile(config, PRODUCTION, senchaConfiguration.getProduction());
    configureAdditionalResourcesForProfile(config, TESTING, senchaConfiguration.getTesting());
    configureAdditionalResourcesForProfile(config, DEVELOPMENT, senchaConfiguration.getDevelopment());

    if (SenchaConfiguration.Type.CODE.equals(senchaConfiguration.getType())) {
      config.put(TOOLKIT, senchaConfiguration.getToolkit());
      config.put(THEME, senchaConfiguration.getTheme());
    }
    if (SenchaConfiguration.Type.THEME.equals(senchaConfiguration.getType())) {
      config.put(TOOLKIT, senchaConfiguration.getToolkit());
      config.put(EXTENDS, senchaConfiguration.getTheme());
    }
    if (SenchaConfiguration.Type.APP.equals(senchaConfiguration.getType())) {
      config.put(THEME, senchaConfiguration.getTheme());
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
