package net.jangaroo.jooc;

import net.jangaroo.jooc.api.CompilationResult;

import java.io.File;
import java.util.Collections;
import java.util.Map;

public class CompilationResultImpl implements CompilationResult {

  private int resultCode;
  private Map<File,File> outputFileMap;

  public CompilationResultImpl(int resultCode, Map<File, File> outputFileMap) {
    this.resultCode = resultCode;
    this.outputFileMap = outputFileMap;
  }

  public CompilationResultImpl(int resultCode) {
    this(resultCode, Collections.<File, File>emptyMap());
  }

  @Override
  public int getResultCode() {
    return resultCode;
  }

  @Override
  public Map<File, File> getOutputFileMap() {
    return outputFileMap;
  }
}
