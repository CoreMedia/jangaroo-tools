package net.jangaroo.apprunner.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppsDeSerializer {

  private static final String PATH_PROPERTY = "path";
  private static final String PATHS_PROPERTY = "paths";

  private static final String CSS_PROPERTY = "css";
  private static final String JS_PROPERTY = "js";
  private static final String LOAD_ORDER_PROPERTY = "loadOrder";

  public static List<AppInfo> readApps(InputStream appsSource, Path rootPath) throws IOException {
    @SuppressWarnings("unchecked") List<Map<String, Object>> appMaps = new ObjectMapper().readValue(appsSource, List.class);
    //noinspection unchecked
    return appMaps.stream().map(appMap -> new AppInfo(
            (String) appMap.get("name"),
            rootPath.resolve((String) appMap.get("path")),
            (List<String>) appMap.get("locales"))
    ).collect(Collectors.toList());
  }

  public static void writeApps(OutputStream output, Path rootPath, List<AppInfo> apps) throws IOException {
    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(
            new PrintWriter(output),
            apps.stream().map(appInfo -> {
              Map<String, Object> appMap = new HashMap<>();
              appMap.put("name", appInfo.name);
              appMap.put("path", rootPath.relativize(appInfo.path).toString().replace('\\', '/'));
              appMap.put("locales", appInfo.locales);
              return appMap;
            }).collect(Collectors.toList())
    );
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
    public final List<String> locales;

    public AppInfo(String name, Path path, List<String> locales) {
      this.name = name;
      this.path = path;
      this.locales = locales;
    }
  }
}
