package net.jangaroo.jooc.api;

import java.io.File;
import java.io.IOException;

public interface Packager {

  void doPackage(File sourceDirectory, File overridesDirectory, File localizedOverridesDirectory,
                 File outputDirectory, String outputFilePrefix) throws IOException;

}
