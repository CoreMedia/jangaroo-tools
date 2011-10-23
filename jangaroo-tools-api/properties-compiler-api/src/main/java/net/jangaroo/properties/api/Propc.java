package net.jangaroo.properties.api;

import net.jangaroo.utils.FileLocations;

import java.io.File;

/**
 * Interface for properties compiler, used by universal Jangaroo IDEA Plugin.
 */
public interface Propc {

  void setConfig(FileLocations config);

  FileLocations getConfig();

  File generate(File propertiesFile);

}
