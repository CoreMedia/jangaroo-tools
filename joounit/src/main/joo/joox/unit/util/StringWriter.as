package joox.unit.util {

import joox.unit.util.PrinterWriter;

/**
 * The PrinterWriter into a String.
 */
public class StringWriter extends PrinterWriter {

  public var mString : String = "";

  /**
   * Returns the written String.
   * The function will close also the stream if it is still open.
   * @type String
   */
  public function getString() : String {
    if (!this.mClosed)
      this.close();
    return this.mString;
  }

  /**
   * \internal
   */
  override function internalFlush(str : String) : void {
    this.mString += str;
  }

}
}
