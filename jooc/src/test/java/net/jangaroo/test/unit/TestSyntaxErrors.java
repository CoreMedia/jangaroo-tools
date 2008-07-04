package net.jangaroo.test.unit;

import net.jangaroo.test.JsccTestCase;

import java.io.File;

import com.coremedia.jscc.Jscc;

/**
 * Test that all .js2 files in the error directory produce compiler errors.
 */
public class TestSyntaxErrors extends JsccTestCase {
  private static final String ERROR_PACKAGE_PATH = "error";

  /**
   * The number of erroneous .js2 files. Increase whenever you add a new error file.
   * Specifying this number makes sure that no files are accidentially forgotten.
   */
  private static int ERROR_FILE_COUNT = 2;

  public TestSyntaxErrors(String name) {
    super(name);
  }

  public void testSucceedingCompilation() {
    assertEquals(Jscc.RESULT_CODE_OK, runJscc(new String[]{"package1/TestMethodCall.js2"}));
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
      } else if (file.getName().endsWith(".js2")) {
        int resultCode = runJscc(baseDirName + "/" + file.getName());
        assertEquals(Jscc.RESULT_CODE_COMPILATION_FAILED, resultCode);
        result++;
      }
    }
    return result;
  }
}

