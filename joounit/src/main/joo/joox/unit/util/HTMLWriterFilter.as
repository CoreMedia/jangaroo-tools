package joox.unit.util {

import joox.unit.util.PrinterWriter;
import joox.unit.util.StringWriter;

/**
 * A filter for a PrinterWriter encoding HTML.
 */
public class HTMLWriterFilter extends PrinterWriter {

  public var mWriter : PrinterWriter;

  /**
   * The constructor accepts the writer to wrap.
   * @param writer The writer to filter.
   */
  public function HTMLWriterFilter(writer : PrinterWriter) {
    super();
    this.setWriter(writer);
  }

  /**
   * Returns the wrapped PrinterWriter.
   * @type PrinterWriter
   */
  public function getWriter() : PrinterWriter {
    return this.mWriter;
  }

  /**
   * Sets the PrinterWriter to wrap.
   * If the argument is omitted a StringWriter is created and wrapped.
   * @param writer The writer to filter.
   */
  public function setWriter(writer : PrinterWriter) : void {
    this.mWriter = writer ? writer : new StringWriter();
  }

  /**
   * \internal
   */
  override function internalFlush(str : String) : void {
    str = str.toString();
    str = str.replace(/&/g, "&amp;");
    str = str.replace(/</g, "&lt;");
    str = str.replace(/\"/g, "&quot;");
    str = str.replace(/\n/g, "<br>");
    this.mWriter.internalFlush(str);
  }

}
}
