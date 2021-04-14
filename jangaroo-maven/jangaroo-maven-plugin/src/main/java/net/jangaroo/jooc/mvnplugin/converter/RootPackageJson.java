package net.jangaroo.jooc.mvnplugin.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class RootPackageJson {
  private PackageJson packageJson;
  private final String path;
  private final ObjectMapper objectMapper;

  public RootPackageJson(ObjectMapper objectMapper, String path) {
    this.objectMapper = objectMapper;
    this.path = path;
    readPackageJson();
  }

  public void addWorkspace(String workspace) {
    this.packageJson.addWorkspace(workspace);
  }

  public PackageJson readPackageJson() {
    this.packageJson = new PackageJson();
    File packageJsonFile = Paths.get(path + "/package.json").toFile();
    if (packageJsonFile.exists()) {
      try {
        PackageJson parsedPackageJson = objectMapper.readValue(packageJsonFile, PackageJson.class);
        if (parsedPackageJson != null) {
          this.packageJson = parsedPackageJson;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      Map<String, String> engines = new TreeMap<>();
      engines.put("node", "^14.16.1");
      engines.put("npm", "^7.8.0");
      Map<String, String> devDependencies = new TreeMap<>();
      devDependencies.put("@typescript-eslint/eslint-plugin", "^4.19.0");
      devDependencies.put("@typescript-eslint/parser", "^4.19.0");
      devDependencies.put("eslint", "^7.22.0");
      devDependencies.put("eslint-plugin-import", "^2.22.1");
      devDependencies.put("eslint-plugin-jsdoc", "^32.3.0");
      devDependencies.put("eslint-plugin-padding", "^0.0.4");
      devDependencies.put("lerna", "^3.0.0");
      Map<String, String> scripts = new LinkedHashMap<>();
      scripts.put("lerna", "lerna");
      this.packageJson = new PackageJson("studio-client-workspace",
              null, null, "1.0.0-SNAPSHOT",
              "MIT",
              true,
              engines,
              new TreeMap<>(),
              devDependencies,
              scripts,
              new ArrayList<>(),
              new LinkedHashMap<>());
    }

    return this.packageJson;
  }

  public void writePackageJson() {
    try {
      if (this.packageJson.getWorkspaces() != null) {
        this.packageJson.getWorkspaces().sort(Comparator.naturalOrder());
      }
      File packageJsonFile = Paths.get(path + "/package.json").toFile();
      if (packageJsonFile.exists()) {
        packageJsonFile.delete();
      }
      FileUtils.write(packageJsonFile, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(packageJson).concat("\n"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
