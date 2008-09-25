package joox.unit.util {

import joox.unit.util.SystemWriter;

/**
 * Helper class with static flags.
 */
public class JooUtil {

  /**
   * Retrieve the caller of a function.
   * @param fn The function to examine.
   * @return Function The caller as Function or undefined.
   **/
  public static function getCaller(fn : Function) : Object {
    switch (typeof fn){
      case "undefined":
        return getCaller(getCaller);
      case "function":
        if (fn.caller)
          return fn.caller;
        if (fn.arguments && fn.arguments.caller)
          return fn.arguments.caller;
    }
    return undefined;
  }

  /**
   * Includes a JavaScript file.
   * @param fname The file name.
   * Loads the content of a JavaScript file into a String that has to be
   * evaluated. Works for command line shells WSH, Rhino and SpiderMonkey.
   * @note This function is highly quirky. While WSH works as expected, the
   * Mozilla shells will evaluate the file immediately and add any symbols to
   * the global name space and return just "true". Therefore you have to
   * evaluate the returned string for WSH at global level also. Otherwise the
   * function is not portable.
   * @return String The JavaScript code to be evaluated.
   */
  public static function includeScript(fname : String) : String {
    var ret : String = "true";
    if (isMozillaShell) {
      load(fname);
    } else if (isWSH) {
      var fso : * = new ActiveXObject("Scripting.FileSystemObject");
      var file : * = fso.OpenTextFile(fname, 1);
      ret = file.ReadAll();
      file.Close();
    }
    return ret;
  }
  /**
   * Returns the SystemWriter.
   * Instanciates a SystemWriter depending on the current JavaScript engine.
   * Works for command line shells WSH, Rhino and SpiderMonkey.
   * @return SystemWriter
   */
  public static function getSystemWriter() : SystemWriter {
    if (!mWriter)
      mWriter = new SystemWriter();
    return mWriter;
  }
  /**
   * Quits the JavaScript engine.
   * @param exit The exit code.
   * Stops current JavaScript engine and returns an exit code. Works for
   * command line shells WSH, Rhino and SpiderMonkey.
   */
  public static function quit(exit : Number) : void {
    if (isMozillaShell)
      quit(exit);
    else if (isWSH)
      WScript.Quit(exit);
  }

  /**
   * Trims characters from string.
   * @param chars String with characters to remove.  The character may
   * also be a regular expression character class like "\\s" (which is the
   * default).
   *
   * The function removes the given chararcters \a chars from the beginning an
   * the end from the current string and returns the result. The function will
   * not modify the current string.
   *
   * The function is written as String enhancement and available as new member
   * function of the class String.
   * @return String String without given characters at start or end.
   */
  public static function trimString(str : String, chars : String) : String {
    if (!chars)
      chars = "\\s";
    var re : RegExp = new RegExp("^[" + chars + "]*(.*?)[" + chars + "]*$");
    var s : String = str.replace(re, "$1");
    return s;
  }

  /**
   * The SystemWriter.
   * @type SystemWriter
   * @see #getSystemWriter
   */
  public static var mWriter : SystemWriter = null;
  /**
   * Flag for a browser.
   * @type Boolean
   * The member is true, if the script runs within a browser environment.
   */
  public static var isBrowser : Boolean = window != null;
  /**
   * Flag for Microsoft JScript.
   * @type Boolean
   * The member is true, if the script runs in the Microsoft JScript engine.
   */
  public static var isJScript : Boolean = typeof ScriptEngine != "undefined";
  /**
   * Flag for Microsoft Windows Scripting Host.
   * @type Boolean
   * The member is true, if the script runs in the Microsoft Windows Scripting
   * Host.
   */
  public static var isWSH : Boolean = typeof WScript != "undefined";
  /**
   * Flag for Rhino.
   * @type Boolean
   * The member is true, if the script runs in Rhino of Mozilla.org.
   */
  public static var isRhino : Boolean = typeof importPackage != "undefined";
  /**
   * Flag for Netscape Enterprise Server (iPlanet) engine.
   * @type Boolean
   * The member is true, if the script runs in the iPlanet as SSJS.
   */
  public static var isNSServer : Boolean = typeof Packages != "undefined" && !isRhino;
  /**
   * Flag for a Mozilla JavaScript shell.
   * @type Boolean
   * The member is true, if the script runs in a command line shell of a
   * Mozilla.org script engine (either SpiderMonkey or Rhino).
   */
  public static var isMozillaShell : Boolean = typeof load != "undefined";
  /**
   * Flag for a command line shell.
   * @type Boolean
   * The member is true, if the script runs in a command line shell.
   */
  public static var isShell : Boolean = isMozillaShell || isWSH;
  /**
   * Flag for call stack support.
   * @type Boolean
   * The member is true, if the engine provides call stack info.
   */
  public static var hasCallStackSupport : Boolean = getCaller() !== undefined;
  /**
   * \internal
   */
  public static var hasCompatibleErrorClass : Boolean =
     Error != null && (!isJScript || isJScript && ScriptEngineMajorVersion() >= 6);

}
}
