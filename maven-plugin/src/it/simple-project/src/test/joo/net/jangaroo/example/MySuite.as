package net.jangaroo.example {

import flexunit.framework.TestSuite;

public class MySuite {

  public function MySuite() {
  }

  public static function suite():TestSuite {
    trace("creating TestSuite...");
    var suite:TestSuite = new TestSuite(HelloWorldTest);
    return suite;
  }
}
}