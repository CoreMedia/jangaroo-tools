package package1 {

[SomeRuntimeAnnotation]
/**
 * This is an example of a class using an [Embed] annotation.
 */
[SomeRuntimeAnnotation(foo="bar")]
public class UsingEmbed {

  [Embed(source="UsingEmbed.as")]
  public var someText:Class;

  [Embed(source="Interface.as")]
  private static var anotherText:Class;

  [Embed(source="jooley.png")]
  private static var jooley:Class;

  [SomeRuntimeAnnotation]
  public var annotated1;

  [SomeRuntimeAnnotationWithArg("foo")]
  public function annotated2() {
  }

  [SomeRuntimeAnnotationWithNamedArg(foo="bar")]
  public function annotated3() {
  }

  [SomeRuntimeAnnotationWithNamedArg(foo="bar")]
  public function annotated4() {
  }

  /**
   * multiple
   */
  [SomeRuntimeAnnotation(type="bar")]
  /**
   * annotations
   */
  [SomeRuntimeAnnotation(type="baz")]
  public function annotated5() {
  }

  [SomePropertyAnnotation(1)]
  public native function get annotated6():String;

  [SomePropertyAnnotation(2)]
  public native function set annotated6(value:String):void;

}
}