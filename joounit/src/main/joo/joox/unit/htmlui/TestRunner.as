package joox.unit.htmlui {

import joox.unit.textui.TestRunner;
import joox.unit.util.HTMLWriterFilter;

/**
 * Class for an application running test suites reporting in HTML.
 */
public class TestRunner extends joox.unit.textui.TestRunner {

  public function TestRunner(outdev : *) {
    super(outdev);
  }

  /**
   * Set printer.
   * The function wraps the PrinterWriter of the new ResultPrinter with a
   * HTMLWriterFilter.
   * @param outdev Output device
   * @return Number TextTestRunner.FAILURE_EXIT.
   */
  override public function setPrinter(outdev : *) : void {
    super.setPrinter(outdev);
    var wrapper : HTMLWriterFilter = new HTMLWriterFilter(this.mPrinter.getWriter());
    this.mPrinter.setWriter(wrapper);
  }

}
}
