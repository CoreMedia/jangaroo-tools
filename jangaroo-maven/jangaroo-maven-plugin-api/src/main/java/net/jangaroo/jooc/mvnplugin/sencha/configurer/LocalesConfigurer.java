package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import net.jangaroo.jooc.mvnplugin.MavenSenchaAppConfiguration;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.List;
import java.util.Map;

public class LocalesConfigurer implements Configurer {

  static final String LOCALES = "locales";
  static final String DEFAULT_LOCALE = "defaultLocale";

  private List<String> locales;

  public LocalesConfigurer(MavenSenchaAppConfiguration config) {
    locales = config.getLocales();
  }

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {
    config.put(DEFAULT_LOCALE, locales.isEmpty() ? "en" : locales.get(0));
    config.put(LOCALES, locales);
  }

}
