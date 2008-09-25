package joox.unit.textui {

import joox.unit.runner.BaseTestRunner;
import joox.unit.framework.TestResult;
import joox.unit.framework.Test;
//import logging.Logger;
import joox.unit.framework.TestSuite;
import joox.unit.util.PrinterWriter;
import joox.unit.textui.ResultPrinter;
import joox.unit.textui.Usage;
import joox.unit.util.JooUtil;

/**
 * Class for an application running test suites with a test based status
 * report.
 */
public class TestRunner extends BaseTestRunner {

  public var mPrinter : *;

  /**
   * The TestRunner is initialized with the given output device. This may be an
   * instance of a ResultPrinter, a PrinterWriter or undefined. For a
   * PrinterWriter the constructor creates a new instance of a standard
   * ResultPrinter with this PrinterWriter. If \a outdev is undefined it creates
   * a ResultPrinter with the SystemWriter.
   * @param outdev Output device
   */
  public function TestRunner(outdev : *) {
    super();
    this.setPrinter(outdev);
  }

  /**
   * Creates an instance of a TestResult to be used for the test run.
   * @return TestResult the new TestResult instance.
   */
  public function createTestResult() : TestResult {
    return new TestResult();
  }

  /**
   * Executes a test run with the given test.
   * @param test The test.
   * @return TestResult The result of the test.
   */
  public function doRun(test : Test) : TestResult {
    var result : TestResult = this.createTestResult();
    result.addListener(this.mPrinter);
    var startTime : Date = new Date();
    //log.log(Logger.LEVEL_DEBUG,"TestRunner#doRun: starting test at "+startTime);
    test.run(result);
    var endTime : Date = new Date();
    //log.log(Logger.LEVEL_DEBUG,"TestRunner#doRun: test finished at "+endTime);
    this.mPrinter.print(result, endTime - startTime);
    return result;
  }

  /**
   * Runs a single test or a suite extracted from a TestCase subclass.
   * @param test The class to test or a test.
   * This static method can be used to start a test run from your program.
   * @return TestResult The result of the test.
   */
  public static function run(test : Object) : TestResult {
    if (test instanceof Function)
      test = new TestSuite(test);
    var runner : TestRunner = new TestRunner();
    return runner.doRun(test);
  }

  //public static var log = new Logger("JooUnitLogger","JooUnit Logger");

  /**
   * Program entry point.
   * @tparam Array<String> args Program arguments.
   * The function will create a TestRunner or the TestRunner given by the
   * preference "TestRunner" and run the tests given by the arguments. The
   * function will exit the program with an error code indicating the type of
   * success.
   */
  public static function main(args : *) : void {
    //log.setLevel(Logger.LEVEL_DEBUG);
    var runner : TestRunner = BaseTestRunner.getPreference("TestRunner");
    if (runner && typeof( runner ) == "function")
      runner = new runner();
    if (!runner)
      runner = new TestRunner();
    try {
      var result : TestResult = runner.start(args);
      JooUtil.quit(
        result.wasSuccessful()
          ? SUCCESS_EXIT
          : FAILURE_EXIT);
    } catch(ex : Error) {
      runner.runFailed(ex.toString());
    }
  }
  /**
   * Run failed.
   * @param msg The failure message.
   * @return Number TestRunner.FAILURE_EXIT.
   */
  public override function runFailed(msg : String) : void {
    //log.log(Logger.LEVEL_ERROR,"TestRunner#runFailed: "+msg);
    JooUtil.getSystemWriter().println(msg);
    JooUtil.quit(EXCEPTION_EXIT);
  }
  /**
   * Set printer.
   * @param outdev Output device
   * @return Number TestRunner.FAILURE_EXIT.
   */
  public function setPrinter(outdev : *) : void {
    if (typeof outdev == "object") {
      if (outdev instanceof PrinterWriter)
        outdev = new ResultPrinter(outdev);
      if (!(outdev instanceof ResultPrinter))
        outdev = new ResultPrinter();
    } else {
      outdev = new ResultPrinter();
    }
    this.mPrinter = outdev;
  }

  private static const USAGE : String = "[JavaScript engine] [TestScript] TestName [TestName2]";
  /**
   * Starts a test run.
   * Analyzes the command line arguments and runs the given test suite. If
   * no argument was given, the function tries to run AllTests.suite().
   * @param args The (optional) arguments as Array or String
   * @return TestResult
   * @throws Usage If an unknown option is used
   */
  public function start(args : Object) : TestResult {
    var testCases : Array = new Array();

    if (typeof args == "undefined")
      args = new Array();
    else if (typeof args == "string")
      args = args.split(/[ ,;]/);

    //log.log(Logger.LEVEL_DEBUG,"TestRunner.main("+args+")");

    var optionsPossible : Boolean = true;
    var i : Number;
    for (i = 0; i < args.length; ++i) {
      args[i] = JooUtil.trimString(args[i]);
      if (optionsPossible && args[i].match(/^-/)) {
        if (args[i] == "--")
          optionsPossible = false;
        else if (args[i] == "-?")
          throw new Usage(USAGE);
        else
          throw new Usage(USAGE + "\nUnknown option \"" + args[i] + "\"");
      }
      if (args[i] != "--")
        testCases.push(args[i]);
    }

    //log.log(Logger.LEVEL_DEBUG,"number of test cases: "+testCases.length);
    var suite : TestSuite;
    if (testCases.length == 0)
      suite = this.getTest("AllTests");
    else if (testCases.length > 1)
    {
      suite = new TestSuite("Start");
      for (i = 0; i < testCases.length; ++i)
        suite.addTestSuite(testCases[i]);
    }
    else
      suite = this.getTest(testCases[0]);

    if (suite)
      return this.doRun(suite);
    else
      return new TestResult();
  }

  /**
   * Exit code, when all tests succeed
   * @type Number
   */
  public static const SUCCESS_EXIT : Number = 0;
  /**
   * Exit code, when at least one test fails with a failure.
   * @type Number
   */
  public static const FAILURE_EXIT : Number = 1;
  /**
   * Exit code, when at least one test fails with an error.
   * @type Number
   */
  public static const EXCEPTION_EXIT : Number = 2;

}
}
