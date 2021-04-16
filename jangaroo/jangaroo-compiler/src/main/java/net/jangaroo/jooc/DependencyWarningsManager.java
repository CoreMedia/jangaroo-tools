package net.jangaroo.jooc;

import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.input.PathInputSource;
import net.jangaroo.jooc.input.ZipEntryInputSource;
import net.jangaroo.jooc.input.ZipFileInputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DependencyWarningsManager {
  private List<DependencyWarning> dependencyWarnings;
  private Map<String, Boolean> compileDependencies;

  public DependencyWarningsManager() {
    this.dependencyWarnings = new ArrayList<>();
    this.compileDependencies = new HashMap<>();
  }

  public void loadInputSource(InputSource inputSource) {
    getCompileDependencies((PathInputSource) inputSource);
  }

  private void getCompileDependencies(PathInputSource classPathInputSource) {
    for (InputSource child : classPathInputSource.getChildren("")) {
      if (child.isInCompilePath()) {
        String key = convertInputSourceToDependency(child);
        if (key != null) {
          compileDependencies.putIfAbsent(key, false);
        }
      }
    }
  }

  public String convertInputSourceToDependency(InputSource inputSource) {
    ZipFile zipFile;
    if (inputSource instanceof ZipFileInputSource) {
      zipFile = ((ZipFileInputSource) inputSource).getZipFile();
    } else if (inputSource instanceof ZipEntryInputSource) {
      zipFile = ((ZipEntryInputSource) inputSource).getZipFileInputSource().getZipFile();
    } else {
      return inputSource.getPath();
    }
    return handleZipFile(zipFile);
  }

  private String handleZipFile(ZipFile zipFile) {
    CompileDependency compileDependency = new CompileDependency();
    Enumeration<? extends ZipEntry> entries = zipFile.entries();
    ZipEntry zipEntry = null;
    while (entries.hasMoreElements()) {
      ZipEntry nextElement = entries.nextElement();
      if (nextElement.getName().contains("META-INF/maven") && nextElement.getName().contains("pom.properties")) {
        zipEntry = nextElement;
      }
    }
    if (zipEntry != null) {
      try (InputStream inputStream = zipFile.getInputStream(zipEntry);
           InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        while (bufferedReader.ready()) {
          line = bufferedReader.readLine();
          setCompileDependency(compileDependency, line);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return compileDependency.createString();
  }

  private void setCompileDependency(CompileDependency dependency, String line) {
    if (line.contains("artifactId")) {
      dependency.setArtifactId(line.replace("artifactId=", ""));
    } else if (line.contains("groupId")) {
      dependency.setGroupId(line.replace("groupId=", ""));
    } else if (line.contains("version")) {
      dependency.setVersion(line.replace("version=", ""));
    }
    // only change something if the line contains the correct key, hence no final else branch
  }

  public void addDependencyWarning(String compileDependency, String usageName) {
    Optional<DependencyWarning> warning = this.dependencyWarnings.stream()
            .filter(dependencyWarning -> dependencyWarning.matches(compileDependency))
            .findFirst();
    if (warning.isPresent()) {
      warning.get().addUsage(usageName);
    } else {
      dependencyWarnings.add(new DependencyWarning(compileDependency, usageName));
    }

  }

  public void updateUsedCompileDependencies(List<String> usedCompileDependencies) {
    for (String usedCompileDependency : usedCompileDependencies) {
      if (compileDependencies.containsKey(usedCompileDependency)) {
        compileDependencies.put(usedCompileDependency, true);
      } else if (compileDependencies.keySet().stream().anyMatch(usedCompileDependency::contains)) {
        compileDependencies.keySet().stream()
                .filter(usedCompileDependency::contains)
                .findFirst()
                .ifPresent(dependency -> compileDependencies.put(dependency, true));
      } else {
        compileDependencies.put(usedCompileDependency, true);
      }
    }
  }

  public List<DependencyWarning> getDependencyWarnings() {
    return dependencyWarnings;
  }

  public List<String> getUnusedDeclaredDependencies() {
    return compileDependencies.entrySet().stream()
            .filter(entry -> !entry.getValue())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
  }

  public class CompileDependency {
    private String artifactId;
    private String groupId;
    private String version;

    public CompileDependency() {
    }

    public CompileDependency(String dependency) {
      String[] firstSplit = dependency.split("__");
      if (firstSplit.length == 2) {
        setGroupId(firstSplit[0]);
        String[] finalSplit = firstSplit[1].split(":");
        if (finalSplit.length == 2) {
          setArtifactId(finalSplit[0]);
          setVersion(finalSplit[1]);
        }
      }
    }

    public boolean matches(CompileDependency anotherDependency) {
      return artifactId.equals(anotherDependency.getGroupId()) &&
              groupId.equals(anotherDependency.getArtifactId()) &&
              version.equals(anotherDependency.getVersion());
    }

    public String createString() {
      return groupId + "__" + artifactId + ":" + version;
    }

    public String getArtifactId() {
      return artifactId;
    }

    public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
    }

    public String getGroupId() {
      return groupId;
    }

    public void setGroupId(String groupId) {
      this.groupId = groupId;
    }

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }
  }

  public class DependencyWarning {
    private final String dependency;
    private final Set<String> usages;

    public DependencyWarning(String dependency, String usage) {
      this.dependency = dependency;
      this.usages = new HashSet<>();
      this.usages.add(usage);
    }


    public String createUsedUndeclaredDependencyWarning(boolean isTestCompile) {
      CompileDependency compileDependency = new CompileDependency(dependency);
      if (compileDependency.getArtifactId() == null || compileDependency.getGroupId() == null || compileDependency.getVersion() == null) {
        return dependency;
      }
      StringJoiner stringJoiner = new StringJoiner("\n");
      stringJoiner.add("<dependency>");
      stringJoiner.add(String.format(" <groupId>%s</groupId>", compileDependency.getGroupId()));
      stringJoiner.add(String.format(" <artifactId>%s</artifactId>", compileDependency.getArtifactId()));
      stringJoiner.add(String.format(" <version>%s</version>", compileDependency.getVersion()));

      stringJoiner.add(" <type>swc</type>");
      if (isTestCompile) {
        stringJoiner.add(" <scope>test</scope>");
      }
      stringJoiner.add("</dependency>");
      return stringJoiner.toString();
    }

    public boolean warningOnlyInTests() {
      return usages.stream().allMatch(usage -> usage.contains("Test"));
    }

    public String getDependency() {
      return dependency;
    }

    public Set<String> getUsages() {
      return usages;
    }

    public boolean matches(String dependency) {
      return this.dependency.equals(dependency);
    }

    public void addUsage(String usage) {
      this.usages.add(usage);
    }
  }
}
