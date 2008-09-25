package joox.unit.util {


/**
 * PrinterWriterError class.
 * This error class is used for errors in the PrinterWriter.
 * @see joox.unit.util.PrinterWriter#close
 */
public class PrinterWriterError extends TypeError {

{
  PrinterWriterError.prototype["name"] = "PrinterWriterError";
}

  /**
   * The constructor initializes the \c message member with the argument
   * \a msg.
   * @param msg The error message.
   **/
  public function PrinterWriterError(msg : String) {
    super(msg);
    this["message"] = msg; // TODO: super(msg) does not yet work
  }

}
}
