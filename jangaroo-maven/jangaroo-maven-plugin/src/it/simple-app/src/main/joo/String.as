package {
[Native]
public class String {
  public native function substr(a:int, b:int):String;
  public native function replace(pattern:*, repl:Object, options:String = null):String;
}
}