package net.jangaroo.jooc;

import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.input.PathInputSource;
import net.jangaroo.jooc.json.JsonArray;
import net.jangaroo.jooc.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyWarningsManager {
  private final List<DependencyWarning> dependencyWarnings;
  private final Map<String, Boolean> compileDependencies;

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
        String key = child.getPath().split("!")[0];
        if (key != null) {
          compileDependencies.putIfAbsent(key, false);
        }
      }
    }
  }

  public List<DependencyWarning> getDependencyWarnings() {
    return dependencyWarnings;
  }

  public String createFileString() {
    List<String> undeclaredDependencies = dependencyWarnings.stream().map(DependencyWarning::getDependency).collect(Collectors.toList());
    return new JsonObject("undeclaredDependencies", new JsonArray(undeclaredDependencies.toArray()),
            "unusedDependencies", new JsonArray(getUnusedDeclaredDependencies().toArray())).toString(2, 0, true);
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

  public List<String> getUnusedDeclaredDependencies() {
    return compileDependencies.entrySet().stream()
            .filter(entry -> !entry.getValue())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
  }

  public static class DependencyWarning {
    private final String dependency;
    private final Set<String> usages;

    public DependencyWarning(String dependency, String usage) {
      this.dependency = dependency.split("!")[0];
      this.usages = new HashSet<>();
      this.usages.add(usage.split("!")[0]);
    }

    public String getDependency() {
      return dependency;
    }

    public Set<String> getUsages() {
      return usages;
    }

    public boolean matches(String dependency) {
      return this.dependency.equals(dependency.split("!")[0]);
    }

    public void addUsage(String usage) {
      this.usages.add(usage);
    }
  }
}
