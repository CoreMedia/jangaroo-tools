package net.jangaroo.example {

import flexunit.framework.TestSuite;

public class MySuite {

  public function MySuite() {
  }

  public static function suite():TestSuite {
    var fix:HelloWorldTest = new HelloWorldTest();
    var suite:TestSuite = new TestSuite();
    suite.addTestSuite(HelloWorldTest);
    return suite;
  }
}
}