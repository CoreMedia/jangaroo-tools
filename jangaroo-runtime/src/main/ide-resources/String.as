package{
public class String extends Object {
  public native function get length():Number;

  public native function anchor(nameAttribute:String):String;

  public native function big():String;

  public native function blink():String;

  public native function bold():String;

  public native function charAt(pos:Number):String;

  public native function charCodeAt(pos:Number):Number;

  public native function concat(...strings):String;

  public native function fixed():String;

  public native function fontcolor(color:String):String;

  public native function fontsize(size:Number):String;

  public native static function fromCharCode(... chars):String;

  public native function indexOf(searchString:String, position:Number = null):Number;

  public native function italics():String;

  public native function lastIndexOf(searchString:String, position:Number = null):Number;

  public native function link(href:String):String;

  public native function localeCompare(that:String):Boolean;

  public native function match(regexp:RegExp):MatchResult;

  public native function replace(searchValue:*, replaceValueOrMapperFunction:*, options:String = null):String;

  public native function search(regexp:RegExp):Boolean;

  public native function slice(start:Number, end:Number):String;

  public native function small():String;

  public native function split(separator:String, limit:Number = null):Array;

  public native function strike():String;

  public native function sub():String;

  public native function substr(start:Number, length:Number = null):String;

  public native function substring(start:Number, end:Number = null):String;

  public native function sup():String;

  public native function toLowerCase():String;

  public native function toLocaleLowerCase():String;

  public native function toLocaleUpperCase():String;

  public native function toUpperCase():String;
}
}