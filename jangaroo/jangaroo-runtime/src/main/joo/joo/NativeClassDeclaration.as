package joo {

[Native(amd)]
public class NativeClassDeclaration {

  public function NativeClassDeclaration(packageName:String, name:String) {
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

  public function toString():String {
    return this.qName;
  }
}
}