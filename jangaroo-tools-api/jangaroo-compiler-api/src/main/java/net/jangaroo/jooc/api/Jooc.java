package net.jangaroo.jooc.api;

import net.jangaroo.jooc.config.JoocConfiguration;

/**
 * Interface for Jangaroo AS3 to JS compiler, used by universal Jangaroo IDEA Plugin.
 */
public interface Jooc {
  int RESULT_CODE_OK = 0;
  int RESULT_CODE_COMPILATION_FAILED = 1;
  int RESULT_CODE_INTERNAL_COMPILER_ERROR = 2;
  int RESULT_CODE_UNRECOGNIZED_OPTION = 3;
  int RESULT_CODE_MISSING_OPTION_ARGUMENT = 4;
  int RESULT_CODE_ILLEGAL_OPTION_VALUE = 5;

  String AS_SUFFIX_NO_DOT = "as";
  String AS_SUFFIX = "." + AS_SUFFIX_NO_DOT;

  String INPUT_FILE_SUFFIX = AS_SUFFIX;
  String OUTPUT_FILE_SUFFIX = ".js";

  void setConfig(JoocConfiguration config);

  void setLog(CompileLog log);

  int run();
}
