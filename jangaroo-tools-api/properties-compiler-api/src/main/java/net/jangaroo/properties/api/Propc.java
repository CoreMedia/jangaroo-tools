package net.jangaroo.properties.api;

import java.io.File;

/**
 * Interface for properties compiler, used by universal Jangaroo IDEA Plugin.
 */
public interface Propc {

  void setConfig(PropertiesCompilerConfiguration config);

  PropertiesCompilerConfiguration getConfig();

  File generate(File propertiesFile);

}
