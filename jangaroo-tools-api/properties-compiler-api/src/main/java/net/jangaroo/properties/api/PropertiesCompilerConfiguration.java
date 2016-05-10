package net.jangaroo.properties.api;

import net.jangaroo.utils.FileLocations;
import org.kohsuke.args4j.Option;

import java.io.File;

/**
 * Jangaroo compiler configuration
 */
public class PropertiesCompilerConfiguration extends FileLocations {

  private File apiOutputDirectory;
  private String defaultLocale = "en";

  public File getApiOutputDirectory() {
    return apiOutputDirectory;
  }

  @Option(name="-api", aliases = "--apiDir", usage = "destination directory where to generate ActionScript API stubs")
  public void setApiOutputDirectory(final File apiOutputDirectory) {
    this.apiOutputDirectory = apiOutputDirectory;
  }

  public boolean isGenerateApi() {
    return getApiOutputDirectory() != null;
  }

  public String getDefaultLocale() {
    return defaultLocale;
  }

  @Option(name="-defaultLocale", usage = "the locale of the properties file w/o explicit locale suffix. Default default is 'EN'")
  public void setDefaultLocale(String defaultLocale) {
    this.defaultLocale = defaultLocale;
  }
}
