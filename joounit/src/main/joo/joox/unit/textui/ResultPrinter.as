package joox.unit.textui {

import joox.unit.framework.Test;
import joox.unit.framework.AssertionFailedError;
import joox.unit.textui.TestRunner;
//import logging.Logger;
import joox.unit.util.PrinterWriter;
import joox.unit.framework.TestResult;
import joox.unit.framework.TestFailure;
import joox.unit.util.JooUtil;
import joox.unit.framework.TestSuite;
import joox.unit.framework.TestListener;

/**
 * Class to print the result of a TextTestRunner.
 */
public class ResultPrinter implements TestListener {

  public var mColumn : Number;
  public var mWriter : PrinterWriter;

  /**
   * Initialization of the ResultPrinter. If no \a writer is provided the
   * instance uses the SystemWriter.
   * @param writer The writer for the report.
   */
  public function ResultPrinter(writer : PrinterWriter) {
    this.setWriter(writer);
    this.mColumn = 0;
  }

  /**
   * Implementation of TestListener.
   * @param test The test that had an error.
   * @param except The thrown error.
   */
  public function addError(test : Test, except : Error) : void {
    this.getWriter().print("E");
  }

  /**
   * Implementation of TestListener.
   * @param test The test that had a failure.
   * @param afe The thrown failure.
   */
  public function addFailure(test : Test, afe : AssertionFailedError) : void {
    //if (test) TestRunner.log.log(Logger.LEVEL_DEBUG,"Failure of Test "+test.getName()+": "+afe);
    this.getWriter().print("F");
  }

  /**
   * Returns the elapsed time in seconds as String.
   * @param runTime The elapsed time in ms.
   * @return String
   */
  public function elapsedTimeAsString(runTime : Number) : String {
    return new String(runTime / 1000);
  }

  /**
   * Implementation of TestListener.
   * @param test The test that ends.
   */
  public function endTest(test : Test) : void {
  }

  /**
   * Returns the associated writer to this instance.
   * @return PrinterWriter
   */
  public function getWriter() : PrinterWriter {
    return this.mWriter;
  }

  /**
   * Print the complete test result.
   * @param result The complete test result.
   * @param runTime The elapsed time in ms.
   */
  public function print(result : TestResult, runTime : Number) : void {
    this.printHeader(runTime);
    this.printErrors(result);
    this.printFailures(result);
    this.printFooter(result);
  }

  /**
   * Print a defect of the test result.
   * @param defect The defect to print.
   * @param count The counter for this defect type.
   */
  public function printDefect(defect : TestFailure, count : Number) : void {
    this.printDefectHeader(defect, count);
    this.printDefectTrace(defect);
    this.getWriter().println();
  }

  /**
   * \internal
   */
  private function printDefectHeader(defect : TestFailure, count : Number) : void {
    this.getWriter().print(count + ") " + defect.toString());
  }

  /**
   * Print the defects of a special type of the test result.
   * @param array The array with the defects (TestFailure).
   * @param type The type of the defects.
   */
  public function printDefects(array : Array, type : String) : void {
    if (array.length == 0)
      return;
    if (array.length == 1)
      this.getWriter().println("There was 1 " + type + ":");
    else
      this.getWriter().println(
        "There were " + array.length + " " + type + "s:");
    for (var i : Number = 0; i < array.length;)
      this.printDefect(array[i], ++i);
  }

  /**
   * \internal
   */
  private function printDefectTrace(defect : TestFailure) : void {
    if (defect["getCallStack"])
      this.getWriter().print(defect["getCallStack"]().toString());
  }

  /**
   * Print the errors of the test result.
   * @param result The complete test result.
   */
  public function printErrors(result : TestResult) : void {
    this.printDefects(result.mErrors, "error");
  }

  /**
   * Print the failures of the test result.
   * @param result The complete test result.
   */
  public function printFailures(result : TestResult) : void {
    this.printDefects(result.mFailures, "failure");
  }

  /**
   * Print the footer of the test result.
   * @param result The complete test result.
   */
  public function printFooter(result : TestResult) : void {
    var writer : PrinterWriter = this.getWriter();
    if (result.wasSuccessful()) {
      var count : Number = result.runCount();
      writer.println();
      writer.print("OK");
      writer.println(" (" + count + " test" + ( count == 1 ? "" : "s" ) + ")");
    } else {
      writer.println();
      writer.println("FAILURES!!!");
      writer.println("Tests run: " + result.runCount()
        + ", Failures: " + result.failureCount()
        + ", Errors: " + result.errorCount());
    }
    writer.println();
  }

  /**
   * Print the header of the test result.
   * @param runTime The elapsed time in ms.
   */
  public function printHeader(runTime : Number) : void {
    var writer : PrinterWriter = this.getWriter();
    writer.println();
    writer.println("Time: " + this.elapsedTimeAsString(runTime));
  }

  /**
   * Sets the PrinterWriter.
   * @note This is an enhancement to JUnit 3.8
   * @param writer The writer for the report.
   * Initialization of the ResultPrinter. If no \a writer is provided the
   * instance uses the SystemWriter.
   */
  public function setWriter(writer : PrinterWriter) : void {
    this.mWriter = writer ? writer : JooUtil.getSystemWriter();
  }

  /**
   * Implementation of TestListener.
   * @param test The test that starts.
   */
  public function startTest(test : Test) : void {
    if (!(test instanceof TestSuite)) {
      this.getWriter().print(".");
      if (this.mColumn++ >= 39) {
        this.getWriter().println();
        this.mColumn = 0;
      }
    }
  }

}
}
