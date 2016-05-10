package net.jangaroo.jooc.mvnplugin.sencha.configbuilder;

import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A base class for all builders for Sencha JSON formats.
 */
public class SenchaConfigBuilder<T extends SenchaConfigBuilder> {
  protected Map<String, Object> config = new LinkedHashMap<>();

  @SuppressWarnings("unchecked")
  T nameValue(String name, Object value) {
    config.put(name, value);
    return (T) this;
  }

  public Map<String, Object> build() {
    return Collections.unmodifiableMap(config);
  }

  public T defaults(String jsonFileName) throws IOException {
    config.putAll(readDefaultJson(jsonFileName));
    return (T) this;
  }

  private Map<String, Object> readDefaultJson(String jsonFileName) throws IOException {
    InputStream inputStream = getClass().getResourceAsStream(jsonFileName);

    @SuppressWarnings("unchecked")
    Map<String, Object> defaultAppConfig = (Map<String, Object>) SenchaUtils.getObjectMapper().readValue(inputStream, Map.class);
    return defaultAppConfig;
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
