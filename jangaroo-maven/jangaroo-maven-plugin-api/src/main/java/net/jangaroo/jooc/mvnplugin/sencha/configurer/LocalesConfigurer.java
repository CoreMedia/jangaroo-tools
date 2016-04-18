package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.MavenSenchaAppConfiguration;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.List;
import java.util.Map;

public class LocalesConfigurer implements Configurer {

  public static final String DEFAULT_LOCALE = "en";

  static final String LOCALES_LABEL = "locales";
  static final String DEFAULT_LOCALE_LABEL = "defaultLocale";

  private List<String> locales;

  public LocalesConfigurer(MavenSenchaAppConfiguration config) {
    locales = config.getLocales();
  }

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    config.put(DEFAULT_LOCALE_LABEL, locales.isEmpty() ? DEFAULT_LOCALE : locales.get(0));
    config.put(LOCALES_LABEL, locales);
  }

}
