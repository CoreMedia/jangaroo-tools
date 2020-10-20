package net.jangaroo.apprunner.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

public class AppManifestDeSerializer {

  public static Map<String, Object> readAppManifest(InputStream appManifestSource) throws IOException {
    //noinspection unchecked
    return new ObjectMapper().readValue(appManifestSource, Map.class);
  }

  public static void writeAppManifest(OutputStream output, Map<String, Object> appManifest) throws IOException {
    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(new PrintWriter(output), appManifest);
  }
}
