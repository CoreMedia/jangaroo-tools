package net.jangaroo.jooc.util;

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

/**
 * A utility for finding the strongly connected components of a graph,
 * implementing Kosaraju's algorithm non-recursively.
 */
public class GraphUtil {
  public static <T> Collection<Set<T>> stronglyConnectedComponent(Map<T,? extends Collection<T>> graph) {
    Map<T, Set<T>> reversed = reverse(graph);

    Map<T, Set<T>> sccsByRoot = new HashMap<T, Set<T>>();
    Set<T> assigned = new HashSet<T>();
    for (T root : kosarajuSort(graph)) {
      Deque<T> todo = new LinkedList<T>();
      todo.add(root);
      while (!todo.isEmpty()) {
        T current = todo.removeLast();
        if (assigned.add(current)) {
          Set<T> component = getOrAdd(sccsByRoot, root);
          component.add(current);
          todo.addAll(reversed.get(current));
        }
      }
    }

    return sccsByRoot.values();
  }

  static <T> Map<T, Set<T>> reverse(Map<T,? extends Collection<T>> graph) {
    Map<T, Set<T>> result = new HashMap<T, Set<T>>();
    for (Map.Entry<T, ? extends Collection<T>> entry : graph.entrySet()) {
      T predecessor = entry.getKey();
      getOrAdd(result, predecessor);
      for (T successor : entry.getValue()) {
        Set<T> predecessors = getOrAdd(result, successor);
        predecessors.add(predecessor);
      }
    }
    return result;
  }

  private static <T> Set<T> getOrAdd(Map<T, Set<T>> map, T key) {
    Set<T> predecessors = map.get(key);
    if (predecessors == null) {
      predecessors = new HashSet<T>();
      map.put(key, predecessors);
    }
    return predecessors;
  }

  static <T> Deque<T> kosarajuSort(Map<T, ? extends Collection<T>> graph) {
    Set<T> reached = new HashSet<T>();
    Set<T> processed = new HashSet<T>();
    Deque<T> sorted = new LinkedList<T>();

    Deque<T> todo = new LinkedList<T>(graph.keySet());
    while (!todo.isEmpty()) {
      T current = todo.getLast();
      if (reached.add(current)) {
        // New node to be processed.
        Collection<T> successors = graph.get(current);
        if (successors != null) {
          for (T successor : successors) {
            // Make sure to add only successors not already reached,
            // so that membership in the set reached is an indicator of a
            // backtracking step after all successors have been processed.
            if (!reached.contains(successor)) {
              todo.add(successor);
            }
          }
        }
      } else {
        // All successors have been processed.
        todo.removeLast();
        if (processed.add(current)) {
          // The node
          sorted.addFirst(current);
        }
      }
    }
    return sorted;
  }

  /**
   * Find the shortest path from start to end.
   * @param graph
   * @param start
   * @param end
   * @param <T>
   * @return
   */
  public static <T> List<T> findPath(Map<T, ? extends Collection<T>> graph, T start, T end) {
    // Breadth-first search for the shortest path.
    Map<T, T> predecessors = new HashMap<T, T>();
    List<T> todo = new ArrayList<T>();
    todo.add(start);
    while (!todo.isEmpty()) {
      List<T> nextTodo = new ArrayList<T>();
      for (T current : todo) {
        Collection<T> successors = graph.get(current);
        if (successors != null) {
          for (T successor : successors) {
            if (!predecessors.containsKey(successor)) {
              predecessors.put(successor, current);
              nextTodo.add(successor);
            }
          }
        }
      }

      todo = nextTodo;
    }

    List<T> result = new ArrayList<T>();
    result.add(end);
    T current = end;
    do {
      current = predecessors.get(current);
      if (current == null) {
        return null;
      }
      result.add(current);
    } while (!current.equals(start));
    Collections.reverse(result);
    return result;
  }
}
