package net.jangaroo.jooc;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.jangaroo.jooc.ast.CompilationUnit;
import net.jangaroo.jooc.ast.FunctionDeclaration;
import net.jangaroo.jooc.util.GraphUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DependencyGraph {
  private Map<String, CompilationUnit> primaryCompilationUnits = new HashMap<String, CompilationUnit>();
  private Multimap<Dependency, Dependency> dependencyGraph = HashMultimap.create();
  private Collection<Set<Dependency>> sccs;
  private Collection<Set<Dependency>> errorSCCs;

  DependencyGraph() throws IOException {}

  void analyze() {
    reduceDependencyGraph();

    // Process each strongly connected component (SCC) of the dependency graph.
    sccs = GraphUtil.stronglyConnectedComponent(dependencyGraph.asMap());
    errorSCCs = new ArrayList<Set<Dependency>>();
    for (Set<Dependency> scc : sccs) {
      // Each SCC must contain at most one initializer.
      List<Dependency> initDependencies = getInitDependencies(scc);

      // We assume that callbacks into a partially initialized class
      // are acceptable if they are triggered by the class initialization itself.
      // Therefore one initializer per cycle is ok. For more than one
      // initialized class, there might be nondeterministic effects
      // as one of the classes has to be initialized first.
      if (initDependencies.size() > 1) {
        errorSCCs.add(scc);
      }

      // Initialized compilation units require all transitive dependencies.
      // Uses dependencies are not enough.
      // Note that the search for transitive dependencies may and often will
      // leave the SCC. However, no cyclic requires chains can be introduced,
      // because the partial order on the SCCs induces a partial order
      // on the initializers.
      if (initDependencies.size() == 1) {
        Dependency initializer = initDependencies.get(0);
        addTransitiveDependenciesAsRequires(dependencyGraph, initializer);
      }
    }
  }

  private void reduceDependencyGraph() {
    for (Collection<Dependency> dependencies : new ArrayList<Collection<Dependency>>(dependencyGraph.asMap().values())) {
      Collection<Dependency> toRemove = new ArrayList<Dependency>();
      for (Dependency dependency : dependencies) {
        if (!primaryCompilationUnits.containsKey(dependency.getCompilationUnitId())) {
          toRemove.add(dependency);
        }
      }
      dependencies.removeAll(toRemove);
    }
  }

  boolean hasErrors() {
    return !errorSCCs.isEmpty();
  }

  private List<Dependency> getInitDependencies(Set<Dependency> scc) {
    List<Dependency> initializingDependencies = new ArrayList<Dependency>();
    for (Dependency dependency : scc) {
      if (dependency.getLevel() == DependencyLevel.INIT) {
        initializingDependencies.add(dependency);
      }
    }
    return initializingDependencies;
  }

  void fillInDependencies(final CompilationUnit compilationUnit) throws IOException {
    primaryCompilationUnits.put(Dependency.getCompilationUnitId(compilationUnit), compilationUnit);

    // Add conceptual dependencies: DYNAMIC -> STATIC -> INIT.
    dependencyGraph.put(new Dependency(compilationUnit, DependencyLevel.DYNAMIC), new Dependency(compilationUnit, DependencyLevel.STATIC));
    dependencyGraph.put(new Dependency(compilationUnit, DependencyLevel.STATIC), new Dependency(compilationUnit, DependencyLevel.INIT));

    // Analyze dependencies in detail.
    // key null: references from static code
    // other keys: references from static methods
    StaticDependencyVisitor visitor = new StaticDependencyVisitor(compilationUnit);
    compilationUnit.visit(visitor);

    // Compute dependencies of the INIT level.
    Set<Dependency> initDependencies = new HashSet<Dependency>();

    // Dependencies directly in initializers.
    Collection<Dependency> nonFunctionDependencies = visitor.getNonFunctionUses().get(null);
    if (nonFunctionDependencies != null) {
      initDependencies.addAll(nonFunctionDependencies);
    }

    // Dependencies in methods called directly or indirectly from initializers.
    Set<FunctionDeclaration> done = new HashSet<FunctionDeclaration>();
    Deque<FunctionDeclaration> todo = new LinkedList<FunctionDeclaration>();
    todo.addAll(visitor.getInternalUses().get(null));
    while (!todo.isEmpty()) {
      FunctionDeclaration ideDeclaration = todo.removeLast();
      if (done.add(ideDeclaration)) {
        if (ideDeclaration.isConstructor()) {
          // Constructor call: typical for singleton pattern.
          // Depend on level DYNAMIC, which ensures that all dependencies apply.
          initDependencies.add(new Dependency(compilationUnit, DependencyLevel.DYNAMIC));
        } else {
          // Add dependencies.
          Collection<Dependency> localDependencies = visitor.getNonFunctionUses().get(ideDeclaration);
          if (localDependencies != null) {
            initDependencies.addAll(localDependencies);
          }
          if (visitor.getInternalUses().containsKey(ideDeclaration)) {
            todo.addAll(visitor.getInternalUses().get(ideDeclaration));
          }
        }
      }
    }

    for (Dependency dependency : initDependencies) {
      dependencyGraph.put(new Dependency(compilationUnit, DependencyLevel.INIT), dependency);
    }

    // Dependencies for level STATIC.
    for (Dependency dependency : visitor.getNonFunctionUses().values()) {
      dependencyGraph.put(new Dependency(compilationUnit, DependencyLevel.STATIC), dependency);
    }

    // Static and init level dependencies have been analysed locally
    // in great detail. Dynamic level dependencies have to be inferred from
    // the dependencies stored in the compilation unit. Required
    // dependencies stored in the compilation unit are used as additional
    // dependencies on the init level to ensure the initialization order.
    for (String dependency : compilationUnit.getDependencies()) {
      dependencyGraph.put(new Dependency(compilationUnit, DependencyLevel.DYNAMIC),
              new Dependency(dependency, DependencyLevel.DYNAMIC));
      boolean isRequired = compilationUnit.isRequiredDependency(dependency);
      if (isRequired) {
        dependencyGraph.put(new Dependency(compilationUnit, DependencyLevel.INIT),
                new Dependency(dependency, DependencyLevel.INIT));
      }
    }
  }

  private void addTransitiveDependenciesAsRequires(Multimap<Dependency, Dependency> dependencyGraph, Dependency initializer) {
    CompilationUnit compilationUnit = primaryCompilationUnits.get(initializer.getCompilationUnitId());
    if (compilationUnit != null) {
      Deque<Dependency> todo = new LinkedList<Dependency>();
      todo.add(initializer);
      Set<Dependency> visited = new HashSet<Dependency>();
      while (!todo.isEmpty()) {
        Dependency dependency = todo.removeLast();
        if (visited.add(dependency)) {
          todo.addAll(dependencyGraph.get(dependency));

          CompilationUnit requiredCompilationUnit = primaryCompilationUnits.get(dependency.getCompilationUnitId());
          if (requiredCompilationUnit != null) {
            compilationUnit.addRequiredDependency(requiredCompilationUnit);
          }
        }
      }
    }
  }

  void writeDependencyGraphToFile(File dependencyGraphFile) throws IOException {
    writeGraphFile(dependencyGraphFile, sccs, Collections.<String>emptySet());
  }

  void writeErrorGraphToFile(File dependencyGraphFile) throws IOException {
    Set<String> allInitializedNames = new HashSet<String>();
    for (Map.Entry<Dependency, Collection<Dependency>> entry : dependencyGraph.asMap().entrySet()) {
      Dependency source = entry.getKey();
      String sourceName = source.toString();
      if (source.getLevel() == DependencyLevel.INIT) {
        allInitializedNames.add(sourceName);
      }
    }

    writeGraphFile(dependencyGraphFile, errorSCCs, allInitializedNames);
  }

  private void writeGraphFile(File dependencyGraphFile, Collection<Set<Dependency>> sccsToExport, Set<String> marked) throws IOException {
    Multimap<String, String> edges = HashMultimap.create();
    for (Map.Entry<Dependency, Collection<Dependency>> entry : dependencyGraph.asMap().entrySet()) {
      String sourceName = entry.getKey().toString();
      Collection<Dependency> dependencies = entry.getValue();
      for (Dependency target : dependencies) {
        edges.put(sourceName, target.toString());
      }
    }

    Set<String> nodes = new HashSet<String>();
    for (Set<Dependency> errorSCC : sccsToExport) {
      for (Dependency dependency : errorSCC) {
        nodes.add(dependency.toString());
      }
    }
    DependencyGraphFile.writeDependencyFile(edges, nodes, marked, dependencyGraphFile);
  }

  String createDependencyError() {
    Set<Dependency> errorSCC = errorSCCs.iterator().next();
    List<Dependency> initializedDependencies = getInitDependencies(errorSCC);

    Dependency dependency1 = initializedDependencies.get(0);
    Dependency dependency2 = initializedDependencies.get(1);

    List<Dependency> path12 = new ArrayList<Dependency>(GraphUtil.findPath(dependencyGraph.asMap(), dependency1, dependency2));
    List<Dependency> path21 = new ArrayList<Dependency>(GraphUtil.findPath(dependencyGraph.asMap(), dependency2, dependency1));
    List<Dependency> cycle = new ArrayList<Dependency>();
    cycle.addAll(path12);
    cycle.addAll(path21.subList(1, path21.size()));

    StringBuilder message = new StringBuilder();
    message.append("The compilation units ");
    message.append(dependency1.getCompilationUnitName());
    message.append(" and ");
    message.append(dependency2.getCompilationUnitName());
    message.append(" contain static initializers");
    message.append(" (for example code blocks or static variables with a complex initializer)");
    message.append(" and the static initializers are mutually dependent: ");
    for (int i = 0; i < cycle.size(); i++) {
      if (i > 0) {
        message.append(" -> ");
      }
      message.append(cycle.get(i));
    }
    message.append(". You can either remove a static initializer or break the dependency cycle");
    message.append(" to make this module compile. (Other dependency cycles might exist, though.)");

    return message.toString();
  }

}
