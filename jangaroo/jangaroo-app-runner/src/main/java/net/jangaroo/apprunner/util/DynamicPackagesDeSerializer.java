package net.jangaroo.apprunner.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

public class DynamicPackagesDeSerializer {

  public static List<String> readDynamicPackages(InputStream dynamicPackagesSource) throws IOException {
    //noinspection unchecked
    return new ObjectMapper().readValue(dynamicPackagesSource, List.class);
  }

  public static void writeDynamicPackages(OutputStream output, Collection<String> dynamicPackages) throws IOException {
    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(new PrintWriter(output), dynamicPackages);
  }
}
