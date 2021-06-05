package net.jangaroo.jooc.mvnplugin.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RootPackageJson {
  private PackageJson packageJson;
  private final String path;
  private final ObjectMapper objectMapper;

  public RootPackageJson(ObjectMapper objectMapper, String path) {
    this.objectMapper = objectMapper;
    this.path = path;
  }

  private PackageJson readPackageJson(RandomAccessFile randomAccessFile) throws IOException {
    this.packageJson = new PackageJson();
    File packageJsonFile = Paths.get(path, "/package.json").toFile();
    PackageJson parsedPackageJson;
    if (!packageJsonFile.exists() || randomAccessFile.length() < 1) {
      parsedPackageJson = objectMapper.readValue(new BufferedReader(
              new InputStreamReader(getClass().getResourceAsStream("/net/jangaroo/jooc/mvnplugin/package.json"), StandardCharsets.UTF_8))
              .lines()
              .collect(Collectors.joining("\n")), PackageJson.class);
    } else {
      parsedPackageJson = objectMapper.readValue(packageJsonFile, PackageJson.class);
    }
    if (parsedPackageJson != null) {
      this.packageJson = parsedPackageJson;
    }
    return this.packageJson;
  }

  public void writePackageJson(List<String> newWorkspaces, List<String> newProjectExtensionWorkspacePaths) throws IOException {
    File packageJsonFile = Paths.get(path + "/package.json").toFile();
    if (!packageJsonFile.exists()) {
      packageJsonFile.createNewFile();
    }
    try (RandomAccessFile randomAccessFile = new RandomAccessFile(packageJsonFile, "rw")) {
      readPackageJson(randomAccessFile);
      newWorkspaces.forEach(this.packageJson::addWorkspace);
      if (this.packageJson.getWorkspaces() != null) {
        this.packageJson.getWorkspaces().sort(Comparator.naturalOrder());
      }

      if (!newProjectExtensionWorkspacePaths.isEmpty()) {
        Map<String, Object> coremedia = this.packageJson.getCoremedia();
        if (coremedia == null) {
          coremedia = new LinkedHashMap<>();
        }
        @SuppressWarnings("unchecked") List<String> projectExtensionWorkspacePaths = (List<String>) coremedia.get("projectExtensionWorkspacePaths");
        if (projectExtensionWorkspacePaths == null) {
          projectExtensionWorkspacePaths = new ArrayList<>();
        }
        for (String newProjectExtensionWorkspacePath : newProjectExtensionWorkspacePaths) {
          if (!projectExtensionWorkspacePaths.contains(newProjectExtensionWorkspacePath)) {
            projectExtensionWorkspacePaths.add(newProjectExtensionWorkspacePath);
          }
        }
        projectExtensionWorkspacePaths.sort(Comparator.naturalOrder());
        coremedia.put("projectExtensionWorkspacePaths", projectExtensionWorkspacePaths);
        this.packageJson.setCoremedia(coremedia);
      }

      randomAccessFile.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(packageJson).concat("\n").getBytes(StandardCharsets.UTF_8));
    }
  }
}
