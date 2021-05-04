package net.jangaroo.jooc.mvnplugin.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
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

  public void writePackageJson(List<String> newWorkspaces) throws IOException {
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
      randomAccessFile.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(packageJson).concat("\n").getBytes(StandardCharsets.UTF_8));
    }
  }
}
