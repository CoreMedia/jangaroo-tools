/*
 * Copyright 2008 CoreMedia AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */

package net.jangaroo.test.unit;

import net.jangaroo.test.JooTestCase;
import net.jangaroo.jooc.Jooc;

import java.io.File;

/**
 * Test that all .as files in the error directory produce compiler errors.
 */
public class TestSyntaxErrors extends JooTestCase {
  private static final String ERROR_PACKAGE_PATH = "error";

  /**
   * The number of erroneous .as files. Increase whenever you add a new error file.
   * Specifying this number makes sure that no files are accidentially forgotten.
   */
  private static int ERROR_FILE_COUNT = 4;

  public TestSyntaxErrors(String name) {
    super(name);
  }

  public void testSucceedingCompilation() {
    assertEquals(Jooc.RESULT_CODE_OK, runJooc(new String[]{"package1/TestMethodCall.as"}));
  }

  public void testAllErrorClasses() {
    String baseDirName = prependSourceDir(new String[]{ERROR_PACKAGE_PATH})[0];
    File baseDir = new File(baseDirName);
    int checked = checkAllErrorClasses(baseDir, ERROR_PACKAGE_PATH);
    assertEquals(ERROR_FILE_COUNT, checked);
  }

  private int checkAllErrorClasses(File baseDir, String baseDirName) {
    File[] files = baseDir.listFiles();
    int result = 0;
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      if (file.isDirectory()) {
        result += checkAllErrorClasses(file, baseDirName + "/" + file.getName());
      } else if (file.getName().endsWith(".as")) {
        int resultCode = runJooc(baseDirName + "/" + file.getName());
        assertEquals(Jooc.RESULT_CODE_COMPILATION_FAILED, resultCode);
        result++;
      }
    }
    return result;
  }
}

