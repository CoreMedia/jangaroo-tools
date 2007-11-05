/*
 *   Copyright (c) 2003 CoreMedia AG, Hamburg. All rights reserved.
 */

package com.coremedia.jscc.test;

import com.coremedia.jscc.Jscc;
import junit.framework.TestCase;

import java.io.File;

public abstract class JsccTestCase extends TestCase {

  public JsccTestCase(String name) {
    super(name);
  }

  protected boolean debug = false;
  protected boolean enableAssertions = false;
  protected String sourceDir = null;
  protected String destinationDir = null;

  protected String getProperty(String name, String defaultValue) {
    return System.getProperty(name, defaultValue);
  }

  protected void setUp() throws Exception {
    sourceDir = getProperty("com.coremedia.jscc.test.source", ".");
    destinationDir = getProperty("com.coremedia.jscc.test.destination", null);
  }

  protected String[] concat(String[] arr1, String[] arr2) {
    String[] result = new String[arr1.length + arr2.length];
    System.arraycopy(arr1, 0, result, 0, arr1.length);
    System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
    return result;
  }

  protected String[] concat(String s, String[] arr2) {
    return concat(new String[]{s}, arr2);
  }

  protected String toString(String[] arr) {
    String result = "";
    for (int i = 0; i < arr.length; i++) {
      if (i > 0)
        result += " ";
      String s = arr[i];
      result += s;
    }
    return result;
  }

  protected String[] copy(String[] arr) {
    String[] result = new String[arr.length];
    System.arraycopy(arr, 0, result, 0, arr.length);
    return result;
  }

  protected String[] prependSourceDir(String[] fileNames) {
    String[] result = copy(fileNames);
    for (int i = 0; i < result.length; i++) {
      String s = result[i];
      result[i] = (new File(sourceDir, s)).getPath();
    }
    return result;
  }

  protected int runJscc(String[] fileNames) {
    String[] args = prependSourceDir(fileNames);
    if (debug) args = concat("-g", args);
    if (enableAssertions) args = concat("-ea", args);
    if (destinationDir != null) args = concat(new String[]{"-d", destinationDir}, args);
    Jscc compiler = new Jscc();
    System.out.println("jscc " + toString(args));
    return compiler.run(args);
  }

  protected int runJscc(String filename) {
    return runJscc(new String[]{filename});
  }

  protected void expectCompileSuccess(String fileName) {
    compile(new String[]{fileName});
  }

  protected void expectCompileResult(int resultCode, String[] fileNames) {
    int actualResult = runJscc(fileNames);
    if (resultCode != actualResult) {
      fail("unexpected compilation result: expected " + resultCode + " (" + Jscc.getResultCodeDescription(resultCode) + "), " +
              "found " + actualResult + " (" + Jscc.getResultCodeDescription(actualResult) + ")");
    }
  }

  protected void compile(String[] fileNames) {
    expectCompileResult(Jscc.RESULT_CODE_OK, fileNames);
  }

  protected void compile(String fileName) {
    compile(new String[] { fileName });
  }

}
