package {
public class RegExp extends Object {
  public native function RegExp(pattern : String = null, options : String = null);

  public native function get global():Boolean;

  public native function get ignoreCase():Boolean;

  public native function get index():Number;

  public native function get lastIndex():Number;

  public native function set lastIndex(v:Number):void;

  public native static function get leftContext():String;

  public native static function get input():String;

  public native static function get lastParen():String;

  public native static function get lastMatch():String;

  public native static function get rightContext():String;

  public native function get multiline():Boolean;

  public native function get source():String;

  public native function get $1():String;

  public native function get $2():String;

  public native function get $3():String;

  public native function get $4():String;

  public native function get $5():String;

  public native function exec(string:String):MatchResult;

  public native function test(string:String):Boolean;
}
}