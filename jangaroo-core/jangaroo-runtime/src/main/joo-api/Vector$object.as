package {

public final dynamic class Vector$object {

  public native function Vector$object(length:uint = 0, fixed:Boolean = false):*;

  public native function reverse():Vector$object;

  public native function unshift(... rest):uint;

  public native function set length(value:uint):void;

  public native function indexOf(value:Object, from:Number = 0):Number;

  public native function pop():*;

  public native function slice(start:Number = 0, end:Number = 2147483647):Vector$object;

  public native function concat(... rest):Vector$object;

  public native function get fixed():Boolean;

  public native function push(... rest):uint;

  public native function every(checker:Function, thisObj:Object = null):Boolean;

  public native function map(mapper:Function, thisObj:Object = null):*;

  public native function sort(comparefn:*):Vector$object;

  public native function shift():*;

  public native function get length():uint;

  public native function set fixed(f:Boolean):void;

  public native function join(separator:String = ","):String;

  public native function lastIndexOf(value:Object, from:Number = 2147483647):Number;

  public native function toString():String;

  public native function toLocaleString():String;

  public native function forEach(eacher:Function, thisObj:Object = null):void;

  public native function some(checker:*, thisObj:Object = null):Boolean;

  public native function splice(start:Number, deleteCount:Number, ... rest):Vector$object;

  public native function filter(checker:Function, thisObj:Object = null):Vector$object;

}
}