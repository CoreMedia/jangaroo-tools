package package1 {

/**
 * This is an example of an API-only class ("native API stub").
 */
[Native]
public class SomeNativeSuperClass {

  [Bindable]
  public native function get baz():String;
  [Bindable]
  public native function set baz(value:String):void;
}
}