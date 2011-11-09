package net.jangaroo.jooc.api;

import java.io.File;
import java.util.Map;

/**
 * The result of a Jooc compilation run.
 */
public interface CompilationResult {

  int RESULT_CODE_OK = 0;
  int RESULT_CODE_COMPILATION_FAILED = 1;
  int RESULT_CODE_INTERNAL_COMPILER_ERROR = 2;
  int RESULT_CODE_UNRECOGNIZED_OPTION = 3;
  int RESULT_CODE_MISSING_OPTION_ARGUMENT = 4;
  int RESULT_CODE_ILLEGAL_OPTION_VALUE = 5;

  /**
   * Return the result code of the last compilation run.
   * This is one of the <codeRESULT_</code>... constants defined in this class.
   * @return the compilation result code
   */
  int getResultCode();

  /**
   * Return a map from source (input) files to successfully generated output files.
   * @return the output file map
   */
  Map<File, File> getOutputFileMap();
}
