package net.jangaroo.jooc.mvnplugin.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WorkspaceRoot {
  private static final String PACKAGE_JSON_PATH = "/package.json";
  private static final String PNPM_WORKSPACE_PATH = "/pnpm-workspace.yaml";

  private final ObjectMapper objectMapper;
  private final String path;

  public WorkspaceRoot(ObjectMapper objectMapper, String path) {
    this.objectMapper = objectMapper;
    this.path = path;
  }

  private PackageJson readPackageJson(RandomAccessFile randomAccessFile) throws IOException {
    PackageJson packageJson;
    File packageJsonFile = Paths.get(path, PACKAGE_JSON_PATH).toFile();
    if (!packageJsonFile.exists() || randomAccessFile.length() < 1) {
      packageJson = objectMapper.readValue(new BufferedReader(
              new InputStreamReader(getClass().getResourceAsStream("/net/jangaroo/jooc/mvnplugin/package.json"), StandardCharsets.UTF_8))
              .lines()
              .collect(Collectors.joining("\n")), PackageJson.class);
    } else {
      packageJson = objectMapper.readValue(packageJsonFile, PackageJson.class);
    }
    return packageJson;
  }

  public void writePackageJson(List<String> newProjectExtensionWorkspacePaths) throws IOException {
    File packageJsonFile = Paths.get(path + PACKAGE_JSON_PATH).toFile();
    if (!packageJsonFile.exists()) {
      packageJsonFile.createNewFile();
    }
    try (RandomAccessFile randomAccessFile = new RandomAccessFile(packageJsonFile, "rw")) {
      PackageJson packageJson = readPackageJson(randomAccessFile);

      if (!newProjectExtensionWorkspacePaths.isEmpty()) {
        Map<String, Object> coremedia = packageJson.getCoremedia();
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
        packageJson.setCoremedia(coremedia);
      }

      randomAccessFile.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(packageJson).concat("\n").getBytes(StandardCharsets.UTF_8));
    }
  }

  private Set<String> readWorkspacePackages(RandomAccessFile randomAccessFile) throws IOException {
    File pnpmWorkspaceYamlFile = Paths.get(path, PNPM_WORKSPACE_PATH).toFile();
    Set<String> packages;
    if (!pnpmWorkspaceYamlFile.exists() || randomAccessFile.length() < 1) {
       packages = new HashSet<>();
    } else {
      packages = new BufferedReader(new FileReader(pnpmWorkspaceYamlFile)).lines().filter(line -> line.startsWith("  - ")).map(
              line -> line.substring(4).replaceAll("^\"(.*)\"$", "$1")
      ).collect(Collectors.toSet());
    }
    return packages;
  }

  public void writeWorkspacePackages(List<String> newPackages) throws IOException {
    File pnpmWorkspaceYamlFile = Paths.get(path + PNPM_WORKSPACE_PATH).toFile();
    if (!pnpmWorkspaceYamlFile.exists()) {
      pnpmWorkspaceYamlFile.createNewFile();
    }
    try (RandomAccessFile randomAccessFile = new RandomAccessFile(pnpmWorkspaceYamlFile, "rw")) {
      Set<String> packages = readWorkspacePackages(randomAccessFile);
      packages.addAll(newPackages);
      randomAccessFile.write(("packages:\n" + packages.stream().sorted(Comparator.naturalOrder()).map(
              pkg -> "  - \"" + pkg + "\""
      ).collect(Collectors.joining("\n"))).getBytes(StandardCharsets.UTF_8));
    }
  }
}
