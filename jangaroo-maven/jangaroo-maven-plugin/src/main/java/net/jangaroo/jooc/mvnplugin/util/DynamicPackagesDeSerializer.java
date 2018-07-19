package net.jangaroo.jooc.mvnplugin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jangaroo.jooc.json.JsonArray;

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

  public static void writeDynamicPackages(OutputStream output, Collection<String> dynamicPackages) {
    // cannot pretty-print: new ObjectMapper.writeValue(unitedDynamicPackagesSink, dynamicPackages);
    PrintWriter pw = new PrintWriter(output);
    pw.write(new JsonArray(dynamicPackages.toArray()).toString(2, 0));
    pw.close();
  }
}
