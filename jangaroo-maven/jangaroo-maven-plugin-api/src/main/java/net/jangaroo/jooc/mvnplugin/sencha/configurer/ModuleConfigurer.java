package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaPackageConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleConfigurer implements Configurer {

  static final String TOOLKIT = "toolkit";
  static final String EXTEND = "extend";
  static final String THEME = "theme";
  static final String RESOURCES = "resources";

  static final String RESOURCE_PATH = "path";
  static final String RESOURCE_OUTPUT = "output";

  private MavenProject project;
  private Log log;
  private SenchaConfiguration senchaConfiguration;

  public ModuleConfigurer(MavenProject project, SenchaConfiguration senchaConfiguration, Log log) {
    this.project = project;
    this.log = log;
    this.senchaConfiguration = senchaConfiguration;
  }

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {

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

    if (Type.CODE.equals(senchaConfiguration.getType())
            || Type.THEME.equals(senchaConfiguration.getType())) {
      configureResourcesEntry(config);
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

  private void configureResourcesEntry(Map<String, Object> config) {
    List<Map<String, Object>> resources = new ArrayList<>();
    Map<String, Object> resource = new HashMap<>();

    resource.put(RESOURCE_PATH, SenchaUtils.generateAbsolutePathUsingPlaceholder(senchaConfiguration.getType(), SenchaUtils.SENCHA_RESOURCES_PATH));

    if (senchaConfiguration instanceof SenchaPackageConfiguration) {
      SenchaPackageConfiguration packageConfiguration = (SenchaPackageConfiguration) senchaConfiguration;
      if (Type.CODE.equals(senchaConfiguration.getType()) && packageConfiguration.isShareResources()
              || Type.THEME.equals(senchaConfiguration.getType()) && !packageConfiguration.isIsolateResources()) {
        resource.put(RESOURCE_OUTPUT, "shared");
      }
    }
    resources.add(resource);

    config.put(RESOURCES, resources);
  }

}
