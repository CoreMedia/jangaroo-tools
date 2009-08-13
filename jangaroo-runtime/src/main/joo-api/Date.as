/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * The Date class represents date and time information.
 * An instance of the Date class represents a particular point in time for which the properties such as month, day,
 * hours, and seconds can be queried or modified. The Date class lets you retrieve date and time values relative to
 * universal time (Greenwich mean time, now called universal time or UTC) or relative to local time, which is
 * determined by the local time zone setting on the operating system.
 * <p>The methods of the Date class are not static but apply only to the individual Date object specified when the
 * method is called. The Date.UTC() and Date.parse() methods are exceptions; they are static methods.
 * <p>To use the Date class, construct a Date instance using the new operator.
 */
public final dynamic class Date extends Object {

  public native function getDate():Number;



  public native function getDay():Number;



  public native function getFullYear():Number;



  public native function getHours():Number;



  public native function getMilliseconds():Number;



  public native function getMinutes():Number;



  public native function getSeconds():Number;



  public native function getTime():Number;



  public native function getTimezoneOffset():Number;



  public native function getUTCDate():Number;



  public native function getUTCDay():Number;



  public native function getUTCFullYear():Number;



  public native function getUTCHours():Number;



  public native function getUTCMilliseconds():Number;



  public native function getUTCMinutes():Number;



  public native function getUTCMonth():Number;



  public native function getUTCSeconds():Number;



  public native function getYear():Number;



  public static native function parse(date : String):Number;



  public native function setDate(date:Number):Number;



  public native function setFullYear(year:Number, month: Number = 1, date: Number = 1):Number;



  public native function setHours(hour:Number, min: Number = 1, sec: Number = 1, ms : Number = 1):Number;



  public native function setMilliseconds(ms: Number):Number;



  public native function setMinutes(min: Number, sec: Number = 1, ms : Number = 1):Number;



  public native function setMonth(month: Number, date: Number = 1):Number;



  public native function setSeconds(sec: Number, ms : Number = 1):Number;



  public native function setTime(time: Number):Number;



  public native function setUTCDate(date: Number):Number;



  public native function setUTCFullYear(year:Number, month: Number = 1, date: Number = 1):Number;



  public native function setUTCHours(hour:Number, min: Number = 1, sec: Number = 1, ms : Number = 1):Number;



  public native function setUTCMilliseconds(ms: Number):Number;



  public native function setUTCMinutes(min: Number, sec: Number = 1, ms : Number = 1):Number;



  public native function setUTCMonth(month: Number, date: Number = 1):Number;



  public native function setUTCSeconds(sec: Number, ms : Number = 1):Number;



  public native function setYear(year: Number):Number;



  public native function toGMTString():String;



  public native function toLocaleDateString():String;



  public native function toLocaleTimeString():String;



  public native function toDateString():String;



  public native function toTimeString():String;



  public native function toUTCString():String;



  public native static function UTC(year:Number, month:Number, date:Number = 1, hours:Number = 1, seconds:Number = 1, ms:Number = 1):String;

}

}