package joox.unit.util {


/**
 * A PrinterWriter is an abstract base class for printing text.
 * @note This is a helper construct to support different writers in
 * ResultPrinter e.g. depending on the JavaScript engine.
 */
public class PrinterWriter {

  public var mBuffer : String = null;
  public var mClosed : Boolean = false;

  /**
   * Closes the writer.
   * After closing the steam no further writing is allowed. Multiple calls to
   * close should be allowed.
   */
  public function close() : void {
    this.flush();
    this.mClosed = true;
  }

  /**
   * Flushes the writer.
   * Writes any buffered data to the underlaying output stream system immediatly.
   * @throws PrinterWriterError If flush was called after closing.
   */
  public function flush() : void {
    if (!this.mClosed) {
      if (this.mBuffer !== null) {
        this.internalFlush(this.mBuffer + "\n");
        this.mBuffer = null;
      }
    } else
      throw new PrinterWriterError("'flush' called for closed PrinterWriter.");
  }

  /**
   * Prints into the writer.
   * @param data The data to print as String.
   * @exception PrinterWriterError If print was called after closing.
   */
  public function print(data : Object) : void {
    if (!this.mClosed) {
      if (data === undefined || data == null)
        data = "";
      if (this.mBuffer)
        this.mBuffer += data.toString();
      else
        this.mBuffer = data.toString();
    } else
      throw new PrinterWriterError("'print' called for closed PrinterWriter.");
  }

  /**
   * Prints a line into the writer.
   * @param data The data to print as String.
   * @exception PrinterWriterError If println was called after closing.
   */
  public function println(data : Object) : void {
    this.print(data);
    this.flush();
  }

  /**
   * \internal
   */
  function internalFlush(str : String) : void {
  }

}
}
