package net.jangaroo.example {

import flexunit.framework.TestSuite;
import net.jangaroo.example.TestClass;

public class MySuite {

  public function MySuite() {
  }

  public static function suite():TestSuite {
      var fix:TestClass = new TestClass();
      var suite:TestSuite = new TestSuite();
      suite.addTestSuite(TestClass);
      return suite;
    }
}
}