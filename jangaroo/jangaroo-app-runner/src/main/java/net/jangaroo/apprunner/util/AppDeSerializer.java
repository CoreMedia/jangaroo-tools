package net.jangaroo.apprunner.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AppDeSerializer {
  private static final String LOCALES_PROPERTY = "locales";
  private static final List<String> DEFAULT_LOCALES = Collections.singletonList("en");

  public static List<String> readLocales(InputStream appJsonSource) throws IOException {
    //noinspection unchecked
    Map<String, Object> map = new ObjectMapper().readValue(appJsonSource, Map.class);
    if (map.containsKey(LOCALES_PROPERTY)) {
      //noinspection unchecked
      return (List<String>) map.get(LOCALES_PROPERTY);
    }
    return DEFAULT_LOCALES;
  }
}
