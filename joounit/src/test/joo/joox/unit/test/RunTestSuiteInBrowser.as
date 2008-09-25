package joox.unit.test {

import joox.unit.runner.BaseTestRunner;
import joox.unit.htmlui.TestRunner;
import joox.unit.textui.TestRunner;

public class RunTestSuiteInBrowser {

  public static function main(testSuiteName : String) : void {
    BaseTestRunner.setPreference("TestRunner", joox.unit.htmlui.TestRunner);
    joox.unit.textui.TestRunner.main(testSuiteName);
  }
}
}