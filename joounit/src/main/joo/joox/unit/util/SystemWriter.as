package joox.unit.util {

import joox.unit.util.PrinterWriter;
import joox.unit.util.JooUtil;

/**
 * The PrinterWriter of the JavaScript engine.
 */
public class SystemWriter extends PrinterWriter {

  /**
   * Closes the writer.
   * Function just flushes the writer. Closing the system writer is not possible.
   */
  public override function close() : void {
    this.flush();
  }
  /**
   * \internal
   */
  override function internalFlush(str : String) : void {
    if (JooUtil.isMozillaShell)
      print(str.substring(0, str.length - 1));
    else if (JooUtil.isBrowser) {
      var p : * /*Element*/ = document.createElement("p");
      p.innerHTML = str;
      document.body.appendChild(p);
    } else if (JooUtil.isWSH)
      WScript.Echo(str.substring(0, str.length - 1));
    /*
     else if( JooUtil.isNSServer )
     write( str );
     */
  }

}
}
