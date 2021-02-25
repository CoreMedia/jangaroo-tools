package net.jangaroo.jooc.api;

import net.jangaroo.jooc.config.JoocConfiguration;

/**
 * Interface for Jangaroo AS3 to JS compiler, used by universal Jangaroo IDEA Plugin.
 */
public interface Jooc {

  String AS_SUFFIX_NO_DOT = "as";
  String AS_SUFFIX = "." + AS_SUFFIX_NO_DOT;

  String MXML_SUFFIX_NO_DOT = "mxml";
  String MXML_SUFFIX = "." + MXML_SUFFIX_NO_DOT;

  String PROPERTIES_SUFFIX_NO_DOT = "properties";
  String PROPERTIES_SUFFIX = "." + PROPERTIES_SUFFIX_NO_DOT;

  String SCSS_SUFFIX_NO_DOT = "scss";
  String SCSS_SUFFIX = "." + SCSS_SUFFIX_NO_DOT;

  String INPUT_FILE_SUFFIX = AS_SUFFIX;
  String OUTPUT_FILE_SUFFIX = ".js";

  void setConfig(JoocConfiguration config);

  void setLog(CompileLog log);

  CompilationResult run();
}
