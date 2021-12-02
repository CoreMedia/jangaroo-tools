package net.jangaroo.jooc.mvnplugin.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergeHelper {
  @SafeVarargs
  private static <S, T> Map<S, T> mergeMaps(MergeOptions mergeOptions, Map<S, T>... maps) {
    Map<S, T> result = new HashMap<>();

    for (Map<S, T> map : maps) {
      mergeMapIntoBaseMap(result, map, mergeOptions);
    }

    return result;
  }

  public static <S, T> void mergeMapIntoBaseMap(Map<S, T> baseMap, Map<S, T> mapToMerge, MergeOptions mergeOptions) {
    for (S key : mapToMerge.keySet()) {
      baseMap.put(key, mergeValues(baseMap.get(key), mapToMerge.get(key), mergeOptions));
    }
  }

  @SafeVarargs
  private static <S> List<S> mergeLists(MergeOptions mergeOptions, List<S>... lists) {
    List<S> result = new ArrayList<>();

    for (List<S> list : lists) {
      mergeListIntoBaseList(result, list, mergeOptions);
    }

    return result;
  }

  private static <S> void mergeListIntoBaseList(List<S> baseList, List<S> listToMerge, MergeOptions mergeOptions) {
    for (int i = 0; i < listToMerge.size(); i++) {
      while (i >= baseList.size()) {
        baseList.add(null);
      }
      baseList.set(i, mergeValues(baseList.get(i), listToMerge.get(i), mergeOptions));
    }
  }

  private static <S, T, U, V> T mergeValues(S value1, T value2, MergeOptions mergeOptions) {
    if (value2 == null) {
      return null;
    }
    if (value2 instanceof Map) {
      if (mergeOptions.mapStrategy == MapStrategy.MERGE && value1 instanceof Map) {
        //noinspection unchecked
        return (T) mergeMaps(mergeOptions, (Map<U, V>) value1, (Map<U, V>) value2);
      } else {
        // calling mergeMaps with a single parameter is like a deep copy
        //noinspection unchecked
        return (T) mergeMaps(mergeOptions, (Map<U, V>) value2);
      }
    } else if (value2 instanceof List) {
      if (!(value1 instanceof List)) {
        // calling mergeLists with a single parameter is like a deep copy
        //noinspection unchecked
        return (T) mergeLists(mergeOptions, (List<U>) value2);
      }
      if (mergeOptions.listStrategy == ListStrategy.MERGE) {
        //noinspection unchecked
        return (T) mergeLists(mergeOptions, (List<U>) value1, (List<U>) value2);
      }
      List<U> list = new ArrayList<>();
      if (mergeOptions.listStrategy == ListStrategy.APPEND) {
        //noinspection unchecked
        list.addAll((List<U>) value1);
      }
      //noinspection unchecked
      list.addAll((List<U>) value2);
      // make sure that a deep copy of every item is provided to avoid modification of inner objects
      //noinspection unchecked
      return (T) mergeLists(mergeOptions, list);
    } else {
      return value2;
    }
  }

  public enum ListStrategy {
    REPLACE,
    MERGE,
    APPEND
  }

  public enum MapStrategy {
    REPLACE,
    MERGE
  }

  public static class MergeOptions {

    public final ListStrategy listStrategy;
    public final MapStrategy mapStrategy;

    public MergeOptions() {
      this.listStrategy = ListStrategy.REPLACE;
      this.mapStrategy = MapStrategy.REPLACE;
    }

    public MergeOptions(ListStrategy listStrategy, MapStrategy mapStrategy) {
      this.listStrategy = listStrategy;
      this.mapStrategy = mapStrategy;
    }
  }
}
