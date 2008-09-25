package joox.unit.util {

import joox.unit.util.JooUtil;

/**
 * CallStack object.
 * The object is extremly system dependent, since its functionality is not
 * within the range of ECMA 262, 3rd edition. It is supported by JScript
 * and SpiderMonkey and was supported in Netscape Enterprise Server 2.x,
 * but not in the newer version 4.x.
 **/
public class CallStack {

  /**
   * The array with the stack.
   * @type Array<String>
   */
  public var mStack : Array;

  /**
   * The object collects the current call stack up to the JavaScript engine.
   * Most engines will not support call stack information with a recursion.
   * Therefore the collection is stopped when the stack has two identical
   * functions in direct sequence.
   * @param Number Maximum recorded stack depth (defaults to 10).
   */
  public function CallStack(depth : Number) {
    this.mStack = null;
    if (JooUtil.hasCallStackSupport)
      this.internalFill(depth);
  }
  /**
   * \internal
   */
  private function internalFill(depth : Number) : void {
    this.mStack = new Array();

    // set stack depth to default
    if (depth == undefined)
      depth = 10;

    ++depth;
    var fn : Function = JooUtil.getCaller(this.internalFill);
    while (fn != null && depth > 0) {
      var s : String = new String(fn);
      --depth;

      // Extract function name and argument list
      var r : RegExp = /function (\w+)([^\{\}]*\))/;
      r.exec(s);
      var f : String = new String(RegExp["$1"]);
      var args : String = new String(RegExp["$2"]);
      this.mStack.push((f + args).replace(/\s/g, ""));

      // Retrieve caller function
      if (fn == JooUtil.getCaller(fn)) {
        // Some interpreter's caller use global objects and may start
        // an endless recursion.
        this.mStack.push("[JavaScript recursion]");
        break;
      } else
        fn = JooUtil.getCaller(fn);
    }

    if (fn == null)
      this.mStack.push("[JavaScript engine]");

    // remove direct calling function CallStack or CallStack_fill
    this.mStack.shift();
  }
  /**
   * Fills the object with the current call stack info.
   * The function collects the current call stack up to the JavaScript engine.
   * Any previous data of the instance is lost.
   * Most engines will not support call stack information with a recursion.
   * Therefore the collection is stopped when the stack has two identical
   * functions in direct sequence.
   * @param depth Maximum recorded stack depth (defaults to 10).
   **/
  public function fill(depth : Number) : void {
    this.mStack = null;
    if (JooUtil.hasCallStackSupport)
      this.internalFill(depth);
  }
  /**
   * Retrieve call stack as array.
   * The function returns the call stack as Array of Strings.
   * @return Array<String> The call stack as array of strings.
   **/
  public function getStack() : Array {
    var a : Array = new Array();
    if (this.mStack != null)
      for (var i : Number = this.mStack.length; i >= 0; i--)
        a[i] = this.mStack[i];
    return a;
  }
  /**
   * Retrieve call stack as string.
   * The function returns the call stack as string. Each stack frame has an
   * own line and is prepended with the call stack depth.
   * @return String The call stack as string.
   **/
  public function toString() : String {
    var s : Array = [];
    if (this.mStack != null)
      for (var i : Number = 1; i <= this.mStack.length; ++i) {
        if (i > 1)
          s.push("\n");
        s.push(i, ": ", this.mStack[i - 1]);
      }
    return s.join("");
  }

}
}
