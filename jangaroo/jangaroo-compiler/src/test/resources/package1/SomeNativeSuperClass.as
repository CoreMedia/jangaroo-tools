package package1 {

/**
 * This is an example of an API-only class ("native API stub").
 */
[Native(amd = "acme/native", global)]
public class SomeNativeSuperClass {

  [Accessor]
  public native function get baz():String;
  [Accessor]
  public native function set baz(value:String):void;
}
}