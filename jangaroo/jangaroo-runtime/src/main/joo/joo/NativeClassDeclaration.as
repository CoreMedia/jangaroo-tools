package joo {

[Native(amd)]
public class NativeClassDeclaration {

  public function NativeClassDeclaration(qName:String, constructor_:Function) {
  }

  /**
   * The name of this class declaration (without package).
   */
  public native function get name():String;

  /**
   * The fully qualified name of this class declaration (including package).
   */
  public native function get qName():String;

  /**
   * @deprecated use qName instead
   */
  public native function get fullClassName():String;

  /**
   * @deprecated does nothing; classes are now always auto-inited.
   */
  public native function init():Class;

  public function toString():String {
    return this.qName;
  }
}
}