package net.jangaroo.jooc;

import java.util.ResourceBundle;

public final class JoocProperties {
  private static final String COMPILER_VERSION_KEY = "jooc.compiler.version";
  private static final String RUNTIME_VERSION_KEY = "jooc.runtime.version";

  private static ResourceBundle joocProperties = ResourceBundle.getBundle("net.jangaroo.jooc.jooc");

  // utility class, do not instantiate
  private JoocProperties() {}
  
  public static String getVersion() {
    return joocProperties.getString(COMPILER_VERSION_KEY);
  }

  public static String getRuntimeVersion() {
    return joocProperties.getString(RUNTIME_VERSION_KEY);
  }

}
