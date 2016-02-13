package net.jangaroo.jooc.mvnplugin.sencha.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public abstract class AbstractJsonInputStreamConfigurer implements Configurer {

  @Override
  public void configure(Map<String, Object> config) throws MojoExecutionException {

    ObjectMapper mapper = new ObjectMapper();
    InputStream inputStream = getInputStream();
    try {
      @SuppressWarnings("unchecked") Map<String, Object> defaultAppConfig = (Map<String, Object>) mapper.readValue(inputStream, Map.class);
      config.putAll(defaultAppConfig);
    } catch (IOException e) {
      throw new MojoExecutionException("could not read json from input stream " + inputStream, e);
    }
  }

  protected abstract InputStream getInputStream();
}
