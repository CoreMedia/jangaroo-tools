package net.jangaroo.jooc.mvnplugin.sencha.configbuilder;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A base class for all builders for Sencha JSON formats.
 */
public class SenchaConfigBuilder<T extends SenchaConfigBuilder> {
  protected Map<String, Object> config = new LinkedHashMap<>();
  private String destFilePath = null;
  private String destFileComment = null;

  @SuppressWarnings("unchecked")
  T nameValue(@Nonnull String name, @Nullable Object value) {
    handleNewValue(config, name, config.get(name), value);
    return (T) this;
  }

  private static void mergeMap(@Nonnull Map<String, Object> baseMap,
                               @Nonnull Map<String, Object> mapWithNewValues) {
    for (Map.Entry<String, Object> entry : mapWithNewValues.entrySet()) {
      String key = entry.getKey();
      handleNewValue(baseMap, key, baseMap.get(key), entry.getValue());
    }
  }

  private static void handleNewValue(@Nonnull Map<String, Object> baseMap,
                                     @Nonnull String key,
                                     @Nullable Object currentValue,
                                     @Nullable Object newValue) {
    boolean isListValue = newValue instanceof List;
    boolean isMapValue = newValue instanceof Map;

    if (currentValue == null || newValue == null || !(isListValue || isMapValue)) {
      baseMap.put(key, newValue);
    } else if (isMapValue) {
      //noinspection unchecked
      addToMapRecursively(baseMap, key, currentValue, (Map<String, Object>) newValue);
    } else {
      addToList(baseMap, key, currentValue, (Collection<?>) newValue);
    }
  }

  private static void addToList(@Nonnull Map<String, Object> baseMap,
                                @Nonnull String key,
                                @Nonnull Object currentValue,
                                @Nonnull Collection<?> additionalValues) {
    if (!(currentValue instanceof List)) {
      String errorMessage = String.format("Expected a list as value for property name %s, but got %s", key, currentValue);
      throw new IllegalArgumentException(errorMessage);
    }
    @SuppressWarnings("unchecked")
    List<Object> currentValueAsList = (List<Object>) currentValue;
    // we need to add the values to the existing list
    List<Object> currentList = new ArrayList<>();
    currentList.addAll(currentValueAsList);
    currentList.addAll(additionalValues);
    baseMap.put(key, currentList);
  }

  private static void addToMapRecursively(@Nonnull Map<String, Object> baseMap,
                                          @Nonnull String key,
                                          @Nonnull Object currentValue,
                                          @Nonnull Map<String, Object> additionalMap) {
    if (!(currentValue instanceof Map)) {
      throw new IllegalArgumentException(String.format("Expected a map as value for property name %s, but got %s", key, currentValue));
    }
    @SuppressWarnings("unchecked")
    Map<String, Object> currentValueAsMap = (Map<String, Object>) currentValue;
    // we need to add the values to the existing list
    Map<String, Object> currentSubMap = new HashMap<>();
    currentSubMap.putAll(currentValueAsMap);
    currentSubMap.putAll(additionalMap);
    baseMap.put(key, currentSubMap);
    mergeMap(currentValueAsMap, additionalMap);
  }

  @SuppressWarnings("unchecked")
  public T namesValues(@Nonnull Map<String, Object> properties) {
    for (Map.Entry<String, Object> entry : properties.entrySet()) {
      nameValue(entry.getKey(), entry.getValue());
    }
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T destFile(String path) {
    this.destFilePath = path;
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T destFileComment(String comment) {
    this.destFileComment = comment;
    return (T) this;
  }

  @Nonnull
  public Map<String, Object> build() {
    return Collections.unmodifiableMap(config);
  }

  /**
   * @return the JSON file containing the Sencha configuration
   * @throws IOException if file could not be written
   */
  public File buildFile() throws IOException {
    if (StringUtils.isBlank(destFilePath)) {
      throw new IllegalStateException("Cannot build file without file path being set.");
    }

    File destFile = new File(destFilePath);
    try (PrintWriter pw = new PrintWriter(new FileWriter(destFile), false)) {
      if (destFileComment != null) {
        pw.println("/**");
        pw.println(" * " + destFileComment);
        pw.println(" */");
      }
      SenchaUtils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(pw, build());
    }
    return destFile;
  }

  @SuppressWarnings("unchecked")
  public T defaults(String jsonFileName) throws IOException {
    return namesValues(readDefaultJson(jsonFileName));
  }

  private Map<String, Object> readDefaultJson(String jsonFileName) throws IOException {
    InputStream inputStream = getClass().getResourceAsStream(jsonFileName);

    //noinspection unchecked
    return (Map<String, Object>) SenchaUtils.getObjectMapper().readValue(inputStream, Map.class);
  }

  <I> T addToList(I item, String ...pathArcs) {
    Map<String, Object> node = config;
    for (int i = 0; i < pathArcs.length - 1; i++) {
      String pathArc = pathArcs[i];
      @SuppressWarnings("unchecked")
      Map<String, Object> nextNode = (Map<String, Object>) node.get(pathArc);
      if (nextNode == null) {
        nextNode = new LinkedHashMap<>();
        node.put(pathArc, nextNode);
      }
      node = nextNode;
    }
    String key = pathArcs[pathArcs.length - 1];
    List<I> newValue = new ArrayList<>();
    @SuppressWarnings("unchecked")
    List<I> value = (List<I>) node.get(key);
    if (value != null && !value.isEmpty()) {
      newValue.addAll(value);
    }
    newValue.add(item);
    node.put(key, Collections.unmodifiableList(newValue));
    //noinspection unchecked
    return (T) this;
  }


}
