package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaProfileConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nonnull;
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
  private Log log;
  private SenchaConfiguration senchaConfiguration;

  public SenchaConfigurationConfigurer(MavenProject project, SenchaConfiguration senchaConfiguration, Log log) {
    this.project = project;
    this.log = log;
    this.senchaConfiguration = senchaConfiguration;
  }

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    SenchaProfileConfigurationConfigurer profileConfigurationConfigurer = new SenchaProfileConfigurationConfigurer(senchaConfiguration);
    profileConfigurationConfigurer.configure(config);
    configureAdditionalResourcesForProfile(config, PRODUCTION, senchaConfiguration.getProduction());
    configureAdditionalResourcesForProfile(config, TESTING, senchaConfiguration.getTesting());
    configureAdditionalResourcesForProfile(config, DEVELOPMENT, senchaConfiguration.getDevelopment());

    String themeAttribute = THEME;
    if (Type.CODE.equals(senchaConfiguration.getType())) {
      config.put(TOOLKIT, senchaConfiguration.getToolkit());
    }
    if (Type.THEME.equals(senchaConfiguration.getType())) {
      config.put(TOOLKIT, senchaConfiguration.getToolkit());
      themeAttribute = EXTEND;
    }

    String themePackageName = getThemePackageName();
    if (StringUtils.isNotBlank(themePackageName)) {
      config.put(themeAttribute, themePackageName);
    }
  }

  @Nonnull
  private String getThemePackageName() {
    Dependency themeDependency = SenchaUtils.getThemeDependency(senchaConfiguration.getTheme(), project);
    String themePackageName;
    if (themeDependency == null) {
      themePackageName = StringUtils.defaultString(senchaConfiguration.getTheme());
      // print a warning if a theme was set but it could not be found in the list of dependencies
      if (senchaConfiguration.getTheme() != null) {
        log.warn(String.format("Could not identify theme dependency. Using theme  \"%s\" from configuration instead.",
                themePackageName));
      }
    } else {
      themePackageName = SenchaUtils.getSenchaPackageName(themeDependency.getGroupId(), themeDependency.getArtifactId());
      log.info(String.format("Setting theme to \"%s\"", themePackageName));
    }
    return themePackageName;
  }

  private void configureAdditionalResourcesForProfile(Map<String, Object> config, String profileName, SenchaProfileConfiguration senchaProfileConfiguration) throws MojoExecutionException {
    if (senchaProfileConfiguration != null) {
      LinkedHashMap<String, Object> profileConfig = new LinkedHashMap<>();
      config.put(profileName, profileConfig);
      SenchaProfileConfigurationConfigurer profileConfigurationConfigurer = new SenchaProfileConfigurationConfigurer(senchaProfileConfiguration);
      profileConfigurationConfigurer.configure(profileConfig);
    }
  }

}
