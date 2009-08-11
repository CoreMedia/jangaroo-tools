package {
public class Number extends Object {
  public static native function get MAX_VALUE():Number;

  public static native function get MIN_VALUE():Number;

  public static native function get NaN():Number;

  public static native function get Infinity():Number;

  public static native function get NEGATIVE_INFINITY():Number;

  public static native function get POSITIVE_INFINITY():Number;

  public native function toExponential(fractionalDigits:Number):Number;

  public native function toFixed(fractionalDigits:Number):Number;

  public native function toPrecision(precision:Number):Number;

  public native function toString(radix:Number = 10):String;
}
}