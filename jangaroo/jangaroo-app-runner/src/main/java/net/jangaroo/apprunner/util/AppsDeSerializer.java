package net.jangaroo.apprunner.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AppsDeSerializer {

  private static final String LOCALES_PROPERTY = "locales";

  private static final String PATH_PROPERTY = "path";
  private static final String PATHS_PROPERTY = "paths";

  private static final String CSS_PROPERTY = "css";
  private static final String JS_PROPERTY = "js";
  private static final String LOAD_ORDER_PROPERTY = "loadOrder";

  private static final List<String> DEFAULT_LOCALES = Collections.singletonList("en");

  public static List<AppInfo> readApps(InputStream appsSource) throws IOException {
    //noinspection unchecked
    return new ObjectMapper().readValue(appsSource, List.class);
  }

  public static void writeApps(OutputStream output, List<AppInfo> apps) throws IOException {
    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(new PrintWriter(output), apps);
  }

  public static List<String> readLocales(InputStream appJsonSource) throws IOException {
    //noinspection unchecked
    Map<String, Object> map = new ObjectMapper().readValue(appJsonSource, Map.class);
    if (map.containsKey(LOCALES_PROPERTY)) {
      //noinspection unchecked
      return (List<String>) map.get(LOCALES_PROPERTY);
    }
    return DEFAULT_LOCALES;
  }

  public static void rewriteBootstrapJsonPaths(InputStream bootstrapJsonSource, OutputStream bootstrapJsonTarget, Map<String, String> pathMapping) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    //noinspection unchecked
    Map<String, Object> bootstrap = objectMapper.readValue(bootstrapJsonSource, Map.class);
    //noinspection unchecked
    Map<String, String> classToPath = (Map<String, String>) bootstrap.get(PATHS_PROPERTY);
    classToPath.forEach((key, value) -> classToPath.put(key, rewritePath(value, pathMapping)));

    Arrays.asList(CSS_PROPERTY, JS_PROPERTY, LOAD_ORDER_PROPERTY).forEach(propertyWithPathObjectList -> {
      if (bootstrap.containsKey(propertyWithPathObjectList)) {
        //noinspection unchecked
        List<Map<String, Object>> pathObjectList = (List<Map<String, Object>>) bootstrap.get(propertyWithPathObjectList);
        pathObjectList.forEach(pathObject -> {
          if (pathObject.containsKey(PATH_PROPERTY)) {
            pathObject.put(PATH_PROPERTY, rewritePath((String) pathObject.get(PATH_PROPERTY), pathMapping));
          }
        });
      }
    });

    objectMapper.writeValue(new PrintWriter(bootstrapJsonTarget), bootstrap);
  }

  private static String rewritePath(String path, Map<String, String> pathMappings) {
    for (String oldPath : pathMappings.keySet()) {
      if (path.startsWith(oldPath)) {
        String newPath = pathMappings.get(oldPath);
        path = newPath + path.substring(oldPath.length());
      }
    }
    return path;
  }

  public static class AppInfo {
    public final String name;
    public final Path path;

    public AppInfo(String name, Path path) {
      this.name = name;
      this.path = path;
    }
  }
}
