package net.jangaroo.jooc.api;

import java.io.File;
import java.io.IOException;

public interface Packager2 {

  void doPackage2(String extNamespace,
                  File sourceDirectory, File overridesDirectory, File localizedOverridesDirectory,
                  File outputDirectory, String outputFilePrefix) throws IOException;

}
