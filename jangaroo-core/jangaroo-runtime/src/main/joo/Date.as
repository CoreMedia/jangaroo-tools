/**
 * API and documentation by Adobe®.
 * Licensed under http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
package {

/**
 * The Date class represents date and time information. An instance of the Date class represents a particular point
 * in time for which the properties such as month, day, hours, and seconds can be queried or modified. The Date
 * class lets you retrieve date and time values relative to universal time (Greenwich mean time, now called universal
 * time or UTC) or relative to local time, which is determined by the local time zone setting on the operating system
 * that is running Flash Player. The methods of the Date class are not static but apply only to the individual Date
 * object specified when the method is called. The <code>Date.UTC()</code> and <code>Date.parse()</code> methods are
 * exceptions; they are static methods.
 * <p>To use the Date class, construct a Date instance using the <code>new</code> operator.</p>
 * <p>ActionScript 3.0 adds several new accessor properties that can be used in place of many Date class methods
 * that access or modify Date instances. ActionScript 3.0 also includes several new variations of the
 * <code>toString()</code> method that are included for ECMA-262 3rd Edition compliance, including:
 * <code>Date.toLocaleString()</code>, <code>Date.toTimeString()</code>, <code>Date.toLocaleTimeString()</code>,
 * <code>Date.toDateString()</code>, and <code>Date.toLocaleDateString()</code>.</p>
 *
 * <p>To compute relative time or time elapsed, see the <code>getTimer()</code> method in the flash.utils package.</p>
 *
 * @example
 * The following example shows various uses of the <code>Date()</code> constructor to assign the
 * following variables:
 * <ul>
 *  <li><code>myDate1</code> calls <code>Date()</code> with no parameters, which sets <code>myDate1</code> to the
 *    current date and time (according to your current system's date and time).</li>
 *  <li><code>myDate2</code> calls <code>Date()</code> with the <code>year</code> (<code>2000</code>), month
 *    (<code>0</code> = January), and <code>day</code> (<code>1</code>) parameters passed to it.</li>
 *  <li><code>myDate3</code> calls <code>Date()</code> with the <code>year</code> (<code>65</code> = 1965),
 *    <code>month</code> (<code>2</code> = March), the <code>day</code> (<code>6</code>), the <code>hour</code>
 *    (<code>9</code>), the <code>minute</code> (<code>30</code>), the <code>second</code> (<code>15</code>)
 *    and the <code>millisecond</code>-+ (<code>0</code>) passed as parameters.</li><li><code>myDate4</code> calls
 *    <code>Date()</code> with the time value representing the number of milliseconds <b>before</b> (since the value
 *    is negative) 0:00:00 GMT January 1, 1970.</li>
 * </ul>
 * <pre>
 * package {
 *     import flash.display.Sprite;
 *
 *     public class DateExample extends Sprite{
 *         public function DateExample() {
 *             var myDate1:Date = new Date();
 *             trace(myDate1); // [NOW]
 *
 *             var myDate2:Date = new Date(2000, 0, 1);
 *             trace(myDate2); // Sat Jan 1 00:00:00 GMT-0800 2000
 *
 *             var myDate3:Date = new Date(65, 2, 6, 9, 30, 15, 0);
 *             trace(myDate3); // Sat Mar 6 09:30:15 GMT-0800 1965
 *
 *             var myDate4:Date = new Date(-14159025000);
 *             trace(myDate4); // Sun Jul 20 19:56:15 GMT-0700 1969
 *         }
 *     }
 * }
 * </pre>
 *
 * @see flash.utils.getTimer
 * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_1.html Working with dates and times
 *
 */
public dynamic class Date extends Object {

  /**
   *   Constructs a new Date object that holds the specified date and time.
   *
   * <p>The <code>Date()</code> constructor takes up to seven parameters (year, month, ..., millisecond) to specify a date
   *  and time to the millisecond. The date that the newly constructed Date object contains depends on the number, and
   *  data type, of arguments passed. </p>
   * <ul>
   *  <li>If you pass no arguments, the Date object is assigned the current date and time.</li>
   *  <li>If you pass one argument of data type Number, the Date object is assigned a time value based on the number of
   *  milliseconds since January 1, 1970 0:00:000 GMT, as specified by the lone argument.</li>
   *  <li>If you pass one argument of data type String, and the string contains a valid date, the Date object  contains a
   *  time value based on that date.</li>
   *  <li>If you pass two or more arguments, the Date object is assigned a time value based on the argument values passed,
   *  which represent the date's year, month, date, hour, minute, second, and milliseconds.</li>
   * </ul>
   * <p>If you pass a string to the Date class constructor, the date can be in a variety of formats, but must at least
   *  include the month, date, and year. For example, <code>Feb 1 2005</code> is valid, but <code>Feb 2005</code> is not.
   *  The following list indicates some of the valid formats:</p>
   * <ul>
   *  <li>Day Month Date Hours:Minutes:Seconds GMT Year (for instance, "Tue Feb 1 00:00:00 GMT-0800 2005", which matches
   *  <code>toString()</code>)</li>
   *  <li>Day Month Date Year Hours:Minutes:Seconds AM/PM (for instance, "Tue Feb 1 2005 12:00:00 AM", which matches
   *  <code>toLocaleString()</code>)</li>
   *  <li>Day Month Date Year (for instance, "Tue Feb 1 2005", which matches <code>toDateString()</code>)</li>
   *  <li>Month/Day/Year (for instance, "02/01/2005")</li>
   *  <li>Month/Year (for instance, "02/2005")</li>
   * </ul>
   *
   * @param yearOrTimevalue If other parameters are specified, this number represents a
   *   year (such as 1965); otherwise, it represents a time value. If the number represents a year, a
   *   value of 0 to 99 indicates 1900 through 1999; otherwise all four digits of the year must be
   *   specified. If the number represents a time value (no other parameters are specified), it is the
   *   number of milliseconds before or after 0:00:00 GMT January 1, 1970; a negative values represents
   *   a time <i>before</i> 0:00:00 GMT January 1, 1970, and a positive value represents a time after.
   * @param month An integer from 0 (January) to 11 (December).
   * @param date (default = <code>1</code>) An integer from 1 to 31.
   * @param hour (default = <code>0</code>) An integer from 0 (midnight) to 23 (11 p.m.).
   * @param minute (default = <code>0</code>) An integer from 0 to 59.
   * @param second (default = <code>0</code>) An integer from 0 to 59.
   * @param millisecond (default = <code>0</code>) An integer from 0 to 999 of milliseconds.
   *
   * @see #getMonth()
   * @see #getDate()
   * @see #getFullYear()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public native function Date(yearOrTimevalue:Number = undefined, month:Number = undefined, date:Number = 1, hour:Number = 0, minute:Number = 0, second:Number = 0, millisecond:Number = 0);

  /**
   * The day of the month (an integer from 1 to 31) specified by a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   * @see #getDate()
   * @see #setDate()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public function get date():Number { return getDate(); }

  /**
   * The day of the month (an integer from 1 to 31) specified by a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   * @see #getDate()
   * @see #setDate()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public function set date(value:Number):void { setDate(value); }

  /**
   * The day of the month (an integer from 1 to 31) of a <code>Date</code> object
   * according to universal time (UTC).
   * @see #getUTCDate()
   * @see setUTCDate()
   */
  public function get dateUTC():Number { return getUTCDate(); } public function set dateUTC(value:Number):void { setUTCDate(value); }

  /**
   * The day of the week (0 for Sunday, 1 for Monday, and so on) specified by this
   * <code>Date</code> according to local time. Local time is determined by the operating
   * system on which Flash Player is running.
   * @see #getDay()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public native function get day():Number

  /**
   * The day of the week (0 for Sunday, 1 for Monday, and so on) of this <code>Date</code> according to universal time (UTC).
   * @see #getUTCDay()
   */
  public native function get dayUTC():Number

  /**
   * The full year (a four-digit number, such as 2000) of a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   * @see #getFullYear()
   * @see #setFullYear()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public function get fullYear():Number { return getFullYear(); }

  /**
   * The full year (a four-digit number, such as 2000) of a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   * @see #getFullYear()
   * @see #setFullYear()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public function set fullYear(value:Number):void { setFullYear(value); }

  /**
   * The four-digit year of a <code>Date</code> object according to universal time (UTC).
   * @see #getUTCFullYear()
   * @see #setUTCFullYear()
   */
  public function get fullYearUTC():Number { return getUTCFullYear(); }

  /**
   * The four-digit year of a <code>Date</code> object according to universal time (UTC).
   * @see #getUTCFullYear()
   * @see #setUTCFullYear()
   */
  public function set fullYearUTC(value:Number):void { setUTCFullYear(value); }

  /**
   * The hour (an integer from 0 to 23) of the day portion of a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   * @see #getHours()
   * @see #setHours()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public function get hours():Number { return getHours(); }

  /**
   * The hour (an integer from 0 to 23) of the day portion of a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   * @see #getHours()
   * @see #setHours()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public function set hours(value:Number):void { setHours(value); }

  /**
   * The hour (an integer from 0 to 23) of the day of a <code>Date</code> object
   * according to universal time (UTC).
   * @see #getUTCHours()
   * @see setUTCHours()
   */
  public function get hoursUTC():Number { return getUTCHours(); }

  /**
   * The hour (an integer from 0 to 23) of the day of a <code>Date</code> object
   * according to universal time (UTC).
   * @see #getUTCHours()
   * @see setUTCHours()
   */
  public function set hoursUTC(value:Number):void { setUTCHours(value); }

  /**
   * The milliseconds (an integer from 0 to 999) portion of a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   * @see #getMilliseconds()
   * @see setMilliseconds()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public function get milliseconds():Number { return getMilliseconds(); }

  /**
   * The milliseconds (an integer from 0 to 999) portion of a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   * @see #getMilliseconds()
   * @see setMilliseconds()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public function set milliseconds(value:Number):void { setMilliseconds(value); }

  /**
   * The milliseconds (an integer from 0 to 999) portion of a <code>Date</code> object
   * according to universal time (UTC).
   * @see #getUTCMilliseconds()
   * @see #setUTCMilliseconds()
   */
  public function get millisecondsUTC():Number { return getUTCMilliseconds(); }

  /**
   * The milliseconds (an integer from 0 to 999) portion of a <code>Date</code> object
   * according to universal time (UTC).
   * @see #getUTCMilliseconds()
   * @see #setUTCMilliseconds()
   */
  public function set millisecondsUTC(value:Number):void { setUTCMilliseconds(value); }

  /**
   * The minutes (an integer from 0 to 59) portion of a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   * @see getMinutes()
   * @see setMinutes()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public function get minutes():Number { return getMinutes(); }

  /**
   * The minutes (an integer from 0 to 59) portion of a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   * @see getMinutes()
   * @see setMinutes()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public function set minutes(value:Number):void { setMinutes(value); }

  /**
   * The minutes (an integer from 0 to 59) portion of a <code>Date</code> object
   * according to universal time (UTC).
   * @see #getUTCMinutes()
   * @see #setUTCMinutes()
   */
  public function get minutesUTC():Number { return getUTCMinutes(); }

  /**
   * The minutes (an integer from 0 to 59) portion of a <code>Date</code> object
   * according to universal time (UTC).
   * @see #getUTCMinutes()
   * @see #setUTCMinutes()
   */
  public function set minutesUTC(value:Number):void { setUTCMinutes(value); }

  /**
   * The month (0 for January, 1 for February, and so on) portion of a <code>
   * Date</code> object according to local time. Local time is determined by the operating system
   * on which Flash Player is running.
   * @see #getMonth()
   * @see #setMonth()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public function get month():Number { return getMonth(); }

  /**
   * The month (0 for January, 1 for February, and so on) portion of a <code>
   * Date</code> object according to local time. Local time is determined by the operating system
   * on which Flash Player is running.
   * @see #getMonth()
   * @see #setMonth()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public function set month(value:Number):void { setMonth(value); }

  /**
   * The month (0 [January] to 11 [December]) portion of a <code>Date</code> object
   * according to universal time (UTC).
   * @see #getUTCMonth()
   * @see #setUTCMonth()
   */
  public function get monthUTC():Number { return getUTCMonth(); }

  /**
   * The month (0 [January] to 11 [December]) portion of a <code>Date</code> object
   * according to universal time (UTC).
   * @see #getUTCMonth()
   * @see #setUTCMonth()
   */
  public function set monthUTC(value:Number):void { setUTCMonth(value); }

  /**
   * The seconds (an integer from 0 to 59) portion of a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   * @see #getSeconds()
   * @see #setSeconds()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public function get seconds():Number { return getSeconds(); }

  /**
   * The seconds (an integer from 0 to 59) portion of a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   * @see #getSeconds()
   * @see #setSeconds()
   * @see http://livedocs.adobe.com/flex/3/html/08_Dates_and_times_3.html Managing calendar dates and times
   */
  public function set seconds(value:Number):void { setSeconds(value); }

  /**
   * The seconds (an integer from 0 to 59) portion of a <code>Date</code> object
   * according to universal time (UTC).
   * @see #getUTCSeconds()
   * @see #setUTCSeconds()
   */
  public function get secondsUTC():Number { return getUTCSeconds(); }

  /**
   * The seconds (an integer from 0 to 59) portion of a <code>Date</code> object
   * according to universal time (UTC).
   * @see #getUTCSeconds()
   * @see #setUTCSeconds()
   */
  public function set secondsUTC(value:Number):void { setUTCSeconds(value); }

  /**
   * The number of milliseconds since midnight January 1, 1970, universal time,
   * for a <code>Date</code> object. Use this method to represent a specific instant in time
   * when comparing two or more <code>Date</code> objects.
   * @see #getTime()
   * @see #setTime()
   */
  public function get time():Number { return getTime(); }

  /**
   * The number of milliseconds since midnight January 1, 1970, universal time,
   * for a <code>Date</code> object. Use this method to represent a specific instant in time
   * when comparing two or more <code>Date</code> objects.
   * @see #getTime()
   * @see #setTime()
   */
  public function set time(value:Number):void { setTime(value); }

  /**
   * The difference, in minutes, between universal time (UTC) and the computer's local time.
   * Specifically, this value is the number of minutes you need to add to the computer's local
   * time to equal UTC. If your computer's time is set later than UTC, the value will be negative.
   * @see getTimezoneOffset()
   */
  public native function get timezoneOffset():Number

  /**
   * Returns the day of the month (an integer from 1 to 31) specified by a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   *
   * @example
   * The following example creates a new Date object <code>someBirthday</code> with parameters
   * <code>year</code> (<code>1974</code>), <code>month</code> (<code>10</code> = November), <code>day</code>
   * (<code>30</code>), <code>hour</code> (<code>1</code>) and <code>minute</code> (<code>20</code>).
   * The <code>getDate()</code> method is then called, which retrieves the day of the month.
   * <pre>
   * package {
   *     import flash.display.Sprite;
   *
   *     public class DateExample extends Sprite {
   *
   *         public function DateExample() {
   *             var someBirthday:Date = new Date(1974, 10, 30, 1, 20);
   *             trace(someBirthday);            // Sat Nov 30 01:20:00 GMT-0800 1974
   *             trace(someBirthday.getDate()); // 30
   *         }
   *     }
   * }
   * </pre>
   *
   * @return Number The day of the month (1 - 31) a <code>Date</code> object represents.
   *
   * @see #getMonth()
   * @see #getFullYear()
   */
  public native function getDate():Number;

  /**
   * Returns the day of the week (0 for Sunday, 1 for Monday, and so on) specified by this
   * <code>Date</code> according to local time. Local time is determined by the operating
   * system on which Flash Player is running.
   *
   * @example
   * The following example creates a new Array object <code>weekDayLabels</code>, with elements
   * <code>[Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday]</code> and a new Date object
   * <code>someBirthday</code> with parameters <code>year</code> (<code>1974</code>), <code>month</code>
   * (<code>10</code> = November), <code>day</code> (<code>30</code>), <code>hour</code> (<code>1</code>)
   * and <code>minute</code> (<code>20</code>).
   * The <code>getDay()</code> method is then called twice, which first shows the day of the month
   * as 6 and then shows the day of the week using <code>weekDayLabels</code>.
   * <pre>
   * var weekDayLabels:Array = new Array("Sunday",
   *                     "Monday",
   *                     "Tuesday",
   *                     "Wednesday",
   *                     "Thursday",
   *                     "Friday",
   *                     "Saturday");
   *
   * var someBirthday:Date = new Date(1974, 10, 30, 1, 20);
   * trace(someBirthday);                       // Sat Nov 30 01:20:00 GMT-0800 1974
   * trace(someBirthday.getDay());            // 6
   * trace(weekDayLabels[someBirthday.getDay()]); // Saturday
   * </pre>
   * @return Number A numeric version of the day of the week (0 - 6) a <code>Date</code> object represents.
   */
  public native function getDay():Number;

  /**
   * Returns the full year (a four-digit number, such as 2000) of a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   *
   * @example
   * The following example creates a new Date object <code>someBirthday</code> with parameters
   * <code>year</code> (<code>1974</code>), <code>month</code> (<code>10</code> = November), <code>day</code>
   * (<code>30</code>), <code>hour</code> (<code>1</code>) and <code>minute</code> (<code>20</code>).
   * The <code>getFullYear()</code> method is then called, which retrieves the four-digit year.
   * <pre>
   * var someBirthday:Date = new Date(1974, 10, 30, 1, 20);
   * trace(someBirthday);           // Sat Nov 30 01:20:00 GMT-0800 1974
   * trace(someBirthday.getFullYear()); // 1974
   * </pre>
   *
   * @return Number The full year a <code>Date</code> object represents.
   */
  public native function getFullYear():Number;

  /**
   * Returns the hour (an integer from 0 to 23) of the day portion of a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   *
   * @example
   * The following example creates a new Date object <code>someBirthday</code> with parameters
   * <code>year</code> (<code>1974</code>), <code>month</code> (<code>10</code> = November), <code>day</code>
   * (<code>30</code>), <code>hour</code> (<code>1</code>) and <code>minute</code> (<code>20</code>).
   * The <code>getHours()</code> and <code>getMinutes()</code> methods are then called, which
   * retrieves the hours and the minutes in 24-hour format.  Finally, a string <code>localTime</code>
   * is created and assigned to the result of a call to the function <code>getUSClockTime()</code>, which, in turn calls
   * <code>getHours()</code> and <code>getMinutes()</code> again, resulting in the time <code>03:05 PM</code>.
   * <pre>
   * var someBirthday:Date = new Date(1974, 10, 30, 15, 5);
   *
   * trace(someBirthday); // Sat Nov 30 15:20:00 GMT-0800 1974
   * trace(someBirthday.getHours() + ":" + someBirthday.getMinutes()); // 15:5
   *
   * var localTime:String = getUSClockTime(someBirthday.getHours(), someBirthday.getMinutes());
   * trace(localTime);    // 03:05 PM
   *
   * function getUSClockTime(hrs:uint, mins:uint):String {
   *     var modifier:String = "PM";
   *     var minLabel:String = doubleDigitFormat(mins);
   *
   *     if(hrs &gt; 12) {
   *         hrs = hrs-12;
   *     } else if(hrs == 0) {
   *         modifier = "AM";
   *         hrs = 12;
   *     } else if(hrs &lt; 12) {
   *         modifier = "AM";
   *     }
   *
   *     return (doubleDigitFormat(hrs) + ":" + minLabel + " " + modifier);
   * }
   *
   * function doubleDigitFormat(num:uint):String {
   *     if(num &lt; 10) {
   *         return ("0" + num);
   *     }
   *     return num;
   * }
   * </pre>
   *
   * @return Number The hour (0 - 23) of the day a <code>Date</code> object represents.
   *
   */
  public native function getHours():Number;

  /**
   * Returns the milliseconds (an integer from 0 to 999) portion of a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   *
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters.
   * The <code>getMilliseconds()</code> method is then called, which retrieves the milliseconds of the
   * Date object <code>now</code> at the time it was created.
   * <pre>
   * var now:Date = new Date();
   * trace(now.getMilliseconds());
   * </pre>
   *
   * @return Number The milliseconds portion of a <code>Date</code> object.
   */
  public native function getMilliseconds():Number;

  /**
   * Returns the minutes (an integer from 0 to 59) portion of a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   *
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters.
   * The <code>getMinutes()</code> method is then called, which retrieves the minutes of the
   * Date object <code>now</code> at the time it was created.
   * <pre>
   * var now:Date = new Date();
   * trace(now);
   * trace(now.getMinutes());
   * </pre>
   *
   * @return Number The minutes portion of a <code>Date</code> object.
   */
  public native function getMinutes():Number;

  /**
   * Returns the month (0 for January, 1 for February, and so on) portion of this <code>
   * Date</code> according to local time. Local time is determined by the operating system
   * on which Flash Player is running.
   *
   * @example
   * The following example creates a new Array object <code>monthLabels</code>, with elements
   * <code>January</code> through <code>December</code> and a new Date object <code>now</code> with no parameters.
   * The <code>getMonth()</code> method is then called twice, which first returns the month number and
   * then the month name of the month the Date object <code>now</code> was created.
   * <pre>
   * var monthLabels:Array = new Array("January",
   *                   "February",
   *                   "March",
   *                   "April",
   *                   "May",
   *                   "June",
   *                   "July",
   *                   "August",
   *                   "September",
   *                   "October",
   *                   "November",
   *                   "December");
   *
   * var now:Date = new Date();
   * trace(now.getMonth());
   * trace(monthLabels[now.getMonth()]);
   * </pre>
   *
   * @return Number The month (0 - 11) portion of a <code>Date</code> object.
   */
  public native function getMonth():Number;

  /**
   * Returns the seconds (an integer from 0 to 59) portion of a <code>Date</code> object
   * according to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   *
   * @example
   * The following example creates a new  Date object <code>now</code> with no parameters.
   * The <code>getSeconds()</code> method is then called, which retrieves the seconds of the
   * Date object <code>now</code> at the time it was created.
   * <pre>
   * var now:Date = new Date();
   * trace(now.getSeconds());
   * </pre>
   *
   * @return Number The seconds (0 to 59) portion of a <code>Date</code> object.
   */
  public native function getSeconds():Number;

  /**
   * Returns the number of milliseconds since midnight January 1, 1970, universal time,
   * for a <code>Date</code> object. Use this method to represent a specific instant in time
   * when comparing two or more <code>Date</code> objects.
   *
   * @example
   * The following example creates a new Date object <code>mlk</code> with parameters
   * <code>year</code> (<code>1929</code>), <code>month</code> (<code>0</code> = January), and
   * <code>day</code> (<code>15</code>). The <code>getTime()</code> method is then called, which
   * retrieves the milliseconds since midnight January 1, 1970, which is negative since the year is
   * set to 1929.
   * <pre>
   * var mlk:Date = new Date(1929, 0, 15);
   * trace(mlk);           // Tue Jan 15 00:00:00 GMT-0800 1929
   * trace(mlk.getTime()); // -1292601600000
   * </pre>
   *
   * The following example creates a new Date object <code>now</code> with no parameters
   * and then uses the following DateMath (created below) class methods to add time to the original Date
   * object <code>now</code> from the time it was created:
   * <ul>
   *   <li><code>addSeconds()</code>: adds 30 seconds to <code>now</code>.</li>
   *   <li><code>addMinutes()</code>: adds 30 minutes to  <code>now</code>.</li>
   *   <li><code>addHours()</code>: adds 6 hours to the Date object <code>now</code>.</li>
   *   <li><code>addDays()</code>: adds 30 days to the Date object <code>now</code>.</li>
   *   <li><code>addWeeks()</code>: adds 4 weeks to <code>now</code>.</li>
   * </ul>
   *
   * <pre>
   * var now:Date = new Date();
   * trace(now);
   * trace(DateMath.addSeconds(now, 30));
   * trace(DateMath.addMinutes(now, 30));
   * trace(DateMath.addHours(now, 6));
   * trace(DateMath.addDays(now, 30));
   * trace(DateMath.addWeeks(now, 4));
   *
   * class DateMath {
   *     public static function addWeeks(date:Date, weeks:Number):Date {
   *         return addDays(date, weeks*7);
   *     }
   *
   *     public static function addDays(date:Date, days:Number):Date {
   *         return addHours(date, days*24);
   *     }
   *
   *     public static function addHours(date:Date, hrs:Number):Date {
   *         return addMinutes(date, hrs*60);
   *     }
   *
   *     public static function addMinutes(date:Date, mins:Number):Date {
   *         return addSeconds(date, mins*60);
   *     }
   *
   *     public static function addSeconds(date:Date, secs:Number):Date {
   *         var mSecs:Number = secs * 1000;
   *         var sum:Number = mSecs + date.getTime();
   *         return new Date(sum);
   *     }
   * }
   * </pre>
   *
   * <b>Note</b>: it's important to use getTime when performing Date arithmetic because it will continue
   *  to work during leap years and doesn't require a bunch of if logic like following pseudo-code:
   * <pre>
   * function addMonths(num:Number):void {
   * 	currentMonth = currentMonth + num;
   * 	if(currentMonth > 12) {
   * 		currentYear++;
   * 		currentMonth = currentMonth - 12;
   * 	}
   * }
   * </pre>
   *
   * @return Number The number of milliseconds since Jan 1, 1970 that a <code>Date</code> object represents.
   */
  public native function getTime():Number;

  /**
   * Returns the difference, in minutes, between universal
   * time (UTC) and the computer's local time.
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters.
   * The <code>getTimezoneOffset()</code> method is then called, which retrieves the difference (in minutes) of the
   * time <code>now</code> was created and Universal Time.  The time zone offset is then converted to hours by
   * dividing the result by 60.
   * <pre>
   * var date:Date = new Date();
   * trace(date.getTimezoneOffset() / 60);
   * </pre>
   *
   * @return Number The minutes you need to add to the computer's local time value to equal UTC. If
   *   your computer's time is set later than UTC, the return value will be negative.
   */
  public native function getTimezoneOffset():Number;

  /**
   * Returns the day of the month (an integer from 1 to 31) of a <code>Date</code> object,
   * according to universal time (UTC).
   *
   * @example
   * The following example creates a new Date object <code>someBirthday</code> with parameters
   * <code>year</code> (<code>1974</code>), <code>month</code> (<code>10</code> = November), <code>day</code>
   *
   * (<code>30</code>), <code>hour</code> (<code>1</code>) and <code>minute</code> (<code>20</code>).
   * The <code>getUTCDate()</code> method is then called, which retrieves the day of the month, according to the UTC.
   * <pre>
   * var someBirthday:Date = new Date(1974, 10, 30, 1, 20);
   * trace(someBirthday);             // Sat Nov 30 01:20:00 GMT-0800 1974
   * trace(someBirthday.getUTCDate()); // 30</pre></div></div></div><a name="getUTCDay()"></a><table class="detailHeader" cellpadding="0" cellspacing="0"><tr><td class="detailHeaderName">getUTCDay</td><td class="detailHeaderParens">()</td><td class="detailHeaderType">method</td><td class="detailHeaderRule">&nbsp;</td></tr></table><div class="detailBody"><code> <a href="statements.html#AS3">AS3</a> function getUTCDay():<a href="Number.html">Number</a></code><p></p><p>
   *
   * @return Number The UTC day of the month (1 to 31) that a <code>Date</code> object represents.
   *
   * @see #getDate()
   */
  public native function getUTCDate():Number;

  /**
   * Returns the day of the week (0 for Sunday, 1 for Monday, and so on) of this <code>Date</code> according to
   * universal time (UTC).
   *
   * @example
   * The following example creates a new Array object <code>weekDayLabels</code>, with elements
   * <code>[Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday]</code> and a new Date object
   * <code>someBirthday</code> with parameters <code>year</code> (<code>1974</code>), <code>month</code>
   * (<code>10</code> = November), <code>day</code> (<code>30</code>), <code>hour</code> (<code>1</code>) and
   * <code>minute</code> (<code>20</code>).
   * The <code>getUTCDay()</code> method is then called twice, which first shows the day of the month
   * as 6 and then shows the day of the week using <code>weekDayLabels</code>, according to the UTC.
   * <pre>
   * var weekDayLabels:Array = new Array("Sunday",
   *                     "Monday",
   *                     "Tuesday",
   *                     "Wednesday",
   *                     "Thursday",
   *                     "Friday",
   *                     "Saturday");
   *
   * var someBirthday:Date = new Date(1974, 10, 30, 1, 20);
   * trace(someBirthday);           // Sat Nov 30 01:20:00 GMT-0800 1974
   * trace(someBirthday.getUTCDay()); // 6
   * trace(weekDayLabels[someBirthday.getUTCDay()]); // Saturday
   * </pre>
   *
   * @return Number The UTC day of the week (0 to 6) that a <code>Date</code> object represents.
   *
   * @see #getDay()
   */

  public native function getUTCDay():Number;

  /**
   * Returns the four-digit year of a <code>Date</code> object according to universal time (UTC).
   *
   * @example
   * The following example creates a new Date object <code>someBirthday</code> with parameters
   * <code>year</code> (<code>1974</code>), <code>month</code> (<code>10</code> = November), <code>day</code>
   * (<code>30</code>), <code>hour</code> (<code>1</code>) and <code>minute</code> (<code>20</code>).
   * The <code>getUTCFullYear()</code> method is then called, which retrieves the four-digit year, according to the UTC.
   * <pre>
   * var someBirthday:Date = new Date(1974, 10, 30, 1, 20);
   * trace(someBirthday);                 // Sat Nov 30 01:20:00 GMT-0800 1974
   * trace(someBirthday.getUTCFullYear()); // 1974
   * </pre>
   *
   * @return Number The UTC four-digit year a <code>Date</code> object represents.
   *
   * @see #getFullYear()
   *
   */
  public native function getUTCFullYear():Number;

  /**
   * Returns the hour (an integer from 0 to 23) of the day of a <code>Date</code> object
   * according to universal time (UTC).
   *
   * @example
   * The following example creates a new Date object <code>someBirthday</code> with parameters
   * <code>year</code> (<code>1974</code>), <code>month</code> (<code>10</code> = November), <code>day</code>
   * (<code>30</code>), <code>hour</code> (<code>1</code>) and <code>minute</code> (<code>20</code>).
   * The <code>getHours()</code> and <code>getMinutes()</code> methods are then called, which retrieves
   * the hours and the minutes in 24-hour format.  Finally, a string <code>localTime</code> is created and
   * assigned to the result of a call to the function <code>getUSClockTime()</code>, which, in turn calls
   * <code>getHours()</code> and <code>getMinutes()</code> again, resulting in the time <code>03:05 PM</code>.
   * Lastly, a String variable <code>utcTime</code> is created in the same manner as <code>localTime</code>,
   * and in this case, the result is the same.
   * <pre>
   * var someBirthday:Date = new Date(1974, 10, 30, 15, 5);
   *
   * trace(someBirthday); // Sat Nov 30 15:20:00 GMT-0800 1974
   * trace(someBirthday.getHours() + ":" + someBirthday.getMinutes()); // 15:5
   *
   * var localTime:String = getUSClockTime(someBirthday.getHours(), someBirthday.getMinutes());
   * trace(localTime);    // 03:05 PM
   *
   * var utcTime:String = getUSClockTime(someBirthday.getUTCHours(), someBirthday.getUTCMinutes());
   * trace(utcTime);      // 11:05 PM
   *
   * function getUSClockTime(hrs:uint, mins:uint):String {
   *     var modifier:String = "PM";
   *     var minLabel:String = doubleDigitFormat(mins);
   *
   *     if(hrs &gt; 12) {
   *         hrs = hrs-12;
   *     } else if(hrs == 0) {
   *         modifier = "AM";
   *         hrs = 12;
   *     } else if(hrs &lt; 12) {
   *         modifier = "AM";
   *     }
   *
   *     return (doubleDigitFormat(hrs) + ":" + minLabel + " " + modifier);
   * }
   *
   * function doubleDigitFormat(num:uint):String {
   *     if(num &lt; 10) {
   *         return ("0" + num);
   *     }
   *     return num;
   * }
   * </pre>
   *
   * @return Number The UTC hour of the day (0 to 23) a <code>Date</code> object represents.
   *
   * @see #getHours()
   */
  public native function getUTCHours():Number;

  /**
   * Returns the milliseconds (an integer from 0 to 999) portion of a <code>Date</code> object
   * according to universal time (UTC).
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters.
   * The <code>getUTCMilliseconds()</code> method is then called, which retrieves the milliseconds of the
   * Date object <code>now</code> at the time it was created, according to the UTC
   * <pre>
   * var now:Date = new Date();
   * trace(now.getUTCMilliseconds());
   * </pre>
   *
   * @return Number The UTC milliseconds portion of a <code>Date</code> object.
   */
  public native function getUTCMilliseconds():Number;

  /**
   * Returns the minutes (an integer from 0 to 59) portion of a <code>Date</code> object
   * according to universal time (UTC).
   *
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters.
   * The <code>getUTCMinutes()</code> method is then called, which retrieves the minutes of the
   * Date object <code>now</code> at the time it was created, according to the UTC.
   *
   * <pre>
   * var now:Date = new Date();
   * trace(now.getUTCMinutes());
   * </pre>
   *
   * @return Number The UTC minutes portion of a <code>Date</code> object.
   */
  public native function getUTCMinutes():Number;

  /**
   * Returns the month (0 [January] to 11 [December]) portion of a <code>Date</code> object
   * according to universal time (UTC).
   *
   * @example
   * The following example creates a new Array object <code>monthLabels</code>, with elements
   * <code>January</code> through <code>December</code> and a new Date object <code>now</code> with no parameters.
   * The <code>getUTCMonth()</code> method is then called twice, which first returns the month number and
   * then the month name of the month the Date object <code>now</code> was created, according to the UTC
   *
   * <pre>
   *
   * var monthLabels:Array = new Array("January",
   *                   "February",
   *                   "March",
   *                   "April",
   *                   "May",
   *                   "June",
   *                   "July",
   *                   "August",
   *                   "September",
   *                   "October",
   *                   "November",
   *                   "December");
   *
   * var now:Date = new Date();
   * trace(now.getMonth());
   * trace(now.getUTCMonth());
   * trace(monthLabels[now.getUTCMonth()]);</pre></div></div></div><a name="getUTCSeconds()"></a><table class="detailHeader" cellpadding="0" cellspacing="0"><tr><td class="detailHeaderName">getUTCSeconds</td><td class="detailHeaderParens">()</td><td class="detailHeaderType">method</td><td class="detailHeaderRule">&nbsp;</td></tr></table><div class="detailBody"><code> <a href="statements.html#AS3">AS3</a> function getUTCSeconds():<a href="Number.html">Number</a></code><p></p><p>
   *
   * @return Number The UTC month portion of a <code>Date</code> object.
   *
   * @see #getMonth()
   */
  public native function getUTCMonth():Number;

  /**
   * Returns the seconds (an integer from 0 to 59) portion of a <code>Date</code> object
   * according to universal time (UTC).
   *
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters.
   * The <code>getUTCSeconds()</code> method is then called, which retrieves the seconds of the
   * Date object <code>now</code> at the time it was created, according to the UTC
   * <pre>
   * var now:Date = new Date();
   * trace(now.getUTCSeconds());
   * </pre>
   *
   * @return Number The UTC seconds portion of a <code>Date</code> object.
   */
  public native function getUTCSeconds():Number;

  /**
   * Converts a string representing a date into a number equaling the number of milliseconds
   * elapsed since January 1, 1970, UTC.
   *
   * @example
   * The following example assigns a date string to <code>dateParsed</code> for November 30, 1974.
   * The <code>Date.parse()</code> method is then called, which converts the date into milliseconds since January 1, 1970.
   * <pre>
   * var dateParsed:String = "Sat Nov 30 1974";
   *
   * var milliseconds:Number = Date.parse(dateParsed);
   * trace(milliseconds); // 155030400000
   * </pre>
   *
   * @param date A string representation of a date, which conforms to the format for the output of
   *   <code>Date.toString()</code>. The date format for the output of <code>Date.toString()</code> is:
   * <pre>
   * Day Mon DD HH:MM:SS TZD YYYY
   * </pre>
   * <p>For example: </p>
   * <pre>
   * Wed Apr 12 15:30:17 GMT-0700 2006
   * </pre>
   * <p>The Time Zone Designation (TZD) is always in the form <code>GMT-HHMM</code> or <code>UTC-HHMM</code> indicating the
   * hour and minute offset relative to Greenwich Mean Time (GMT), which is now also called universal time (UTC).
   * The year month and day terms can be separated by a forward slash (<code>/</code>) or by spaces, but never by a
   * dash (<code>-</code>). Other supported formats include the following (you can include partial representations of these
   * formats; that is, just the month, day, and year):</p>
   *   *  <pre>
   * MM/DD/YYYY HH:MM:SS TZD
   * HH:MM:SS TZD Day Mon/DD/YYYY
   * Mon DD YYYY HH:MM:SS TZD
   * Day Mon DD HH:MM:SS TZD YYYY
   * Day DD Mon HH:MM:SS TZD YYYY
   * Mon/DD/YYYY HH:MM:SS TZD
   * YYYY/MM/DD HH:MM:SS TZD
   * </pre>
   *
   * @return Number A number representing the milliseconds elapsed since January 1, 1970, UTC.
   *
   * @see #toString()
   */
  public static native function parse(date : String):Number;

  /**
   * Sets the day of the month, according to local time, and returns the new time in
   * milliseconds. Local time is determined by the operating system on which Flash Player
   * is running.
   *
   * @example
   * The following example creates a new Date object <code>someBirthday</code> with parameters
   * <code>year</code> (<code>1974</code>), <code>month</code> (<code>10</code> = November), <code>day</code>
   *
   * (<code>30</code>), <code>hour</code> (<code>1</code>) and <code>minute</code> (<code>20</code>).  The
   * method <code>getDate()</code> is then called, which retrieves the day of the month.  Next
   * <code>setDate()</code> is called with the <code>day</code> parameter set to <code>20</code> and
   * then <code>getDate()</code> is called again, which retrieves the newly set day of month.
   * <pre>
   * var someBirthday:Date = new Date(1974, 10, 30, 1, 20);
   * trace(someBirthday);            // Sat Nov 30 01:20:00 GMT-0800 1974
   * trace(someBirthday.getDate()); // 30
   *
   * someBirthday.setDate(20);
   * trace(someBirthday.getDate()); // 20
   * </pre>
   *
   * @param day An integer from 1 to 31.
   *
   * @return Number The new time, in milliseconds.
   */
  public native function setDate(day:Number):Number;

  /**
   * Sets the year, according to local time, and returns the new time in milliseconds. If
   * the <code>month</code> and <code>day</code> parameters are specified,
   * they are set to local time. Local time is determined by the operating system on which
   * Flash Player is running.
   * <p>
   * Calling this method does not modify the other fields of the <code>Date</code> but
   * <code>Date.getUTCDay()</code> and <code>Date.getDay()</code> can report a new value
   * if the day of the week changes as a result of calling this method.
   * </p>
   * @example
   * The following example creates a new Date object <code>someBirthday</code> with parameters
   * <code>year</code> (<code>1974</code>), <code>month</code> (<code>10</code> = November), <code>day</code>
   * (<code>30</code>), <code>hour</code> (<code>1</code>) and <code>minute</code> (<code>20</code>).  The
   * method <code>getFullYear()</code> is then called, which retrieves the four-digit year.
   * Next <code>setFullYear()</code> is called with the <code>year</code> parameter set to
   * <code>2000</code> and then <code>getFullYear()</code> is called again, which retrieves the newly set year.
   * <pre>
   * var someBirthday:Date = new Date(1974, 10, 30, 1, 20);
   * trace(someBirthday);           // Sat Nov 30 01:20:00 GMT-0800 1974
   * trace(someBirthday.getFullYear()); // 1974
   *
   * someBirthday.setFullYear(2000);
   * trace(someBirthday.getFullYear()); // 2000
   * </pre>
   *
   * @param year Number A four-digit number specifying a year. Two-digit numbers do not represent
   *   four-digit years; for example, 99 is not the year 1999, but the year 99.
   * @param month An integer from 0 (January) to 11 (December).
   * @param day A number from 1 to 31.
   *
   * @return Number The new time, in milliseconds.
   *
   * @see #getUTCDay()
   */

  public native function setFullYear(year:Number, month: Number = 1, day: Number = 1):Number;

  /**
   * Sets the hour, according to local time, and returns the new time in milliseconds.
   * Local time is determined by the operating system on which Flash Player is running.
   * @example
   * The following example creates a new Date object <code>someBirthday</code> with parameters
   * <code>year</code> (<code>1974</code>), <code>month</code> (<code>10</code> = November), <code>day</code>
   * (<code>30</code>), <code>hour</code> (<code>1</code>) and <code>minute</code> (<code>20</code>).  The methods
   * <code>getHours()</code> and
   * <code>getMinutes()</code> are then called, which retrieves the hours and minutes.  Next <code>setHours()</code>
   * is called with the <code>hour</code> parameter set to <code>12</code> and then <code>getHours()</code> and
   * <code>getMinutes()</code> are called again, which retrieves the newly set hours and minutes.
   * <pre>
   * var someBirthday:Date = new Date(1974, 10, 30, 15, 20);
   *
   * trace(someBirthday); // Sat Nov 30 15:20:00 GMT-0800 1974
   * trace(someBirthday.getHours() + ":" + someBirthday.getMinutes()); // 15:20
   *
   * someBirthday.setHours(12);
   * trace(someBirthday.getHours() + ":" + someBirthday.getMinutes()); // 12:20
   * </pre>
   *
   * @param hour An integer from 0 (midnight) to 23 (11 p.m.).
   * @param minute An integer from 0 to 59.
   * @param second An integer from 0 to 59.
   * @param millisecond An integer from 0 to 999.
   *
   * @return Number The new time, in milliseconds.
   *
   */
  public native function setHours(hour:Number, minute: Number = 1, second: Number = 1, millisecond : Number = 1):Number;

  /**
   * Sets the milliseconds, according to local time, and returns the new time in
   * milliseconds. Local time is determined by the operating system on which Flash Player
   * is running.
   *
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters.
   * The method <code>getMilliseconds()</code> is then called, which retrieves the milliseconds when
   * <code>now</code> was created.  Then another new Date object <code>before</code> with an additional
   * call to <code>setMilliseconds()</code> with the <code>millisecond</code> parameter set to <code>4</code> and
   * <code>getMilliseconds()</code> is called again, which retrieves the newly set milliseconds.
   *
   * <pre>
   * var now:Date = new Date();
   * trace(now);
   * trace(now.getMilliseconds());
   *
   * var before:Date = new Date(now.setMilliseconds(4));
   * trace(before);
   * trace(before.getMilliseconds());
   * </pre>
   *
   * @param millisecond An integer from 0 to 999.
   *
   * @return Number The new time, in milliseconds.
   */
  public native function setMilliseconds(millisecond: Number):Number;

  /**
   * Sets the minutes, according to local time, and returns the new time in milliseconds.
   * Local time is determined by the operating system on which Flash Player is running.
   *
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters.
   * The method <code>getMinutes()</code> is then called, which retrieves the minutes when
   * <code>now</code> was created.  Then another new Date object <code>before</code> with an additional
   * call to <code>setMinutes()</code> with the <code>minute</code> parameter set to <code>0</code> and
   * <code>getMinutes()</code> is called again, which retrieves the newly set minutes.
   *
   * <pre>
   * var now:Date = new Date();
   * trace(now);
   * trace(now.getMinutes());
   *
   * var before:Date = new Date(now.setMinutes(0));
   * trace(before);
   * trace(before.getMinutes());
   * </pre>
   *
   * @param minute An integer from 0 to 59.
   * @param second An integer from 0 to 59.
   * @param millisecond An integer from 0 to 999.
   *
   * @return Number The new time, in milliseconds.
   */
  public native function setMinutes(minute: Number, second: Number = 1, millisecond : Number = 1):Number;

  /**
   * Sets the month and optionally the day of the month, according to local time, and
   * returns the new time in milliseconds. Local time is determined by the operating
   * system on which Flash Player is running.
   *
   * @example
   * The following example creates a new Array object <code>monthLabels</code>, with elements
   * <code>January</code> through <code>December</code> and a new month object <code>now</code> with no parameters.
   * The method <code>getMonth()</code> is then called, which retrieves the month in which
   * <code>now</code> was created.  Next <code>setMonth()</code> is called with the <code>month</code> parameter set to
   * <code>0</code> and then <code>getMonth()</code> is called again, which retrieves the newly set month..
   * <pre>
   * var monthLabels:Array = new Array("January",
   *                   "February",
   *                   "March",
   *                   "April",
   *                   "May",
   *                   "June",
   *                   "July",
   *                   "August",
   *                   "September",
   *                   "October",
   *                   "November",
   *                   "December");
   *
   * var now:Date = new Date();
   * trace(now.getMonth());
   * trace(monthLabels[now.getMonth()]);
   *
   * now.setMonth(0);
   * trace(now.getMonth());             // 0
   * trace(monthLabels[now.getMonth()]); // January
   * </pre>
   *
   * @param month An integer from 0 (January) to 11 (December).
   * @param day An integer from 1 to 31.
   *
   * @return Number The new time, in milliseconds.
   */
  public native function setMonth(month: Number, day: Number = 1):Number;

  /**
   * Sets the seconds, according to local time, and returns the new time in milliseconds.
   * Local time is determined by the operating system on which Flash Player is running.
   *
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters.
   * The method <code>getseconds()</code> is then called, which retrieves the seconds when
   * <code>now</code> was created.  Then the <code>setSeconds()</code> is called with the <code>second</code>
   * parameter set to <code>0</code> and
   * <code>getSeconds()</code> is called again, which retrieves the newly set seconds.
   * <pre>
   *
   * var now:Date = new Date();
   * trace(now.getSeconds());
   *
   * now.setSeconds(0);
   * trace(now.getSeconds()); // 0
   * </pre>
   *
   * @param second An integer from 0 to 59.
   * @param millisecond An integer from 0 to 999.
   *
   * @return Number The new time, in milliseconds.
   */
  public native function setSeconds(second: Number, millisecond : Number = 1):Number;

  /**
   * Sets the date in milliseconds since midnight on January 1, 1970, and returns the new
   * time in milliseconds.
   *
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters.
   * The <code>setTime()</code> method is then called, with the <code>millisecond</code> parameter set
   * to <code>-1292601600000</code>, which sets the time to <code>Tue Jan 15 00:00:00 GMT-0800 1929</code>.
   * <pre>
   * var now:Date = new Date();
   * trace(now);
   *
   * now.setTime(-1292601600000);
   * trace(now); // Tue Jan 15 00:00:00 GMT-0800 1929
   * </pre>
   *
   * @param millisecond An integer value where 0 is midnight on January 1, universal time (UTC).
   *
   * @return Number The new time, in milliseconds.
   */
  public native function setTime(millisecond: Number):Number;

  /**
   * Sets the day of the month, in universal time (UTC), and returns the new time in
   * milliseconds. Calling this method does not modify the other fields of a <code>Date
   * </code> object, but the <code>Date.getUTCDay()</code> and <code>Date.getDay()</code> methods can report
   * a new value if the day of the week changes as a result of calling this method.
   *
   * @example
   * The following example creates a new Date object <code>someBirthday</code> with parameters
   * <code>year</code> (<code>1974</code>), <code>month</code> (<code>10</code> = November), <code>day</code>
   *
   * (<code>30</code>), <code>hour</code> (<code>1</code>) and <code>minute</code> (<code>20</code>).  The method
   * <code>getUTCDate()</code> is called and correctly returns the day of the month.  Next <code>setUTCDate()</code>
   *
   * is called with the <code>day</code> parameter set to <code>1</code> and a <code>trace()</code> statement
   * confirms the date was correctly set.
   * <pre>
   *
   * var someBirthday:Date = new Date(1974, 10, 30, 1, 20);
   * trace(someBirthday); // Sat Nov 30 01:20:00 GMT-0800 1974
   * trace(someBirthday.getUTCDate()); // 30
   *
   * someBirthday.setUTCDate(1);
   * trace(someBirthday); // Fri Nov 1 01:20:00 GMT-0800 1974
   * </pre>
   *
   * @param day A number; an integer from 1 to 31.
   *
   * @return Number The new time, in milliseconds.
   *
   * @see #getUTCDay()
   */
  public native function setUTCDate(day: Number):Number;

  /**
   * Sets the year, in universal time (UTC), and returns the new time in milliseconds.
   * <p>Optionally, this method can also set the month and day of the month. Calling
   * this method does not modify the other fields, but the <code>Date.getUTCDay()</code> and
   * <code>Date.getDay()</code> methods can report a new value if the day of the week changes as a
   * result of calling this method.
   * </p>
   *
   * @example
   * The following example creates a new Date object <code>someBirthday</code> with parameters
   * <code>year</code> (<code>1974</code>), <code>month</code> (<code>10</code> = November), <code>day</code>
   * (<code>30</code>), <code>hour</code> (<code>1</code>) and <code>minute</code> (<code>20</code>).  The method
   * <code>getUTCFullYear()</code> is called and correctly returns the four-digit year.  Next <code>setUTCFullYear()</code>
   *
   * is called with the <code>year</code> parameter set to <code>1975</code> and a <code>trace()</code> statement
   * confirms the year was correctly set.
   * <pre>
   * var someBirthday:Date = new Date(1974, 10, 30, 1, 20);
   * trace(someBirthday); // Sat Nov 30 01:20:00 GMT-0800 1974
   * trace(someBirthday.getUTCFullYear()); // 1974
   *
   * someBirthday.setUTCFullYear(1975);
   * trace(someBirthday); // Thu Nov 30 01:20:00 GMT-0800 1975
   * </pre>
   *
   * @param year An integer that represents the year specified as a full four-digit year, such as 2000.
   * @param month An integer from 0 (January) to 11 (December).
   * @param day An integer from 1 to 31.
   * @return Number An integer.
   *
   * @see #getUTCDay()
   */
  public native function setUTCFullYear(year:Number, month: Number = 1, day: Number = 1):Number;

  /**
   * Sets the hour, in universal time (UTC), and returns the new time in milliseconds.
   * Optionally, the minutes, seconds, and milliseconds can be specified.
   *
   * @example
   * The following example creates a new Date object <code>someBirthday</code> with parameters
   * <code>year</code> (<code>1974</code>), <code>month</code> (<code>10</code> = November), <code>day</code>
   * (<code>30</code>), <code>hour</code> (<code>1</code>) and <code>minute</code> (<code>20</code>).  The methods
   * <code>getHours()</code>, <code>getMinutes()</code>, <code>getUTCHours()</code>, and <code>getUTCMinutes()</code>
   *
   * are then called, which retrieves the hours and minutes. Next <code>setUTCHours()</code> is called with the
   * <code>hour</code> parameter set to <code>12</code> and then the methods <code>getHours()</code>,
   * <code>getMinutes()</code>, <code>getUTCHours()</code>, and <code>getUTCMinutes()</code> are re-called and
   * correctly display the updated hour.
   * <pre>
   * var someBirthday:Date = new Date(1974, 10, 30, 15, 20);
   *
   * trace(someBirthday); // Sat Nov 30 15:20:00 GMT-0800 1974
   * trace(someBirthday.getHours() + ":" + someBirthday.getMinutes());     // 15:20
   * trace(someBirthday.getUTCHours() + ":" + someBirthday.getUTCMinutes()); // 23:20
   *
   * someBirthday.setUTCHours(12);
   * trace(someBirthday.getHours() + ":" + someBirthday.getMinutes());     // 4:20
   * trace(someBirthday.getUTCHours() + ":" + someBirthday.getUTCMinutes()); // 12:20
   * </pre>
   *
   * @param hour An integer from 0 (midnight) to 23 (11 p.m.).
   * @param minute An integer from 0 to 59.
   * @param second An integer from 0 to 59.
   * @param millisecond An integer from 0 to 999.
   *
   * @return Number The new time, in milliseconds.
   */
  public native function setUTCHours(hour:Number, minute: Number = 1, second: Number = 1, millisecond : Number = 1):Number;

  /**
   * Sets the milliseconds, in universal time (UTC), and returns the new time in milliseconds.
   *
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters.
   * The method <code>getUTCMilliseconds()</code> is then called, which retrieves the UTCMilliseconds when
   * <code>now</code> was created.  Then another new Date object <code>before</code> with an additional
   * call to <code>setUTCMilliseconds()</code> with the <code>millisecond</code> parameter set to <code>4</code> and
   * <code>getUTCMilliseconds()</code> is called again, which retrieves the newly set milliseconds.
   * <pre>
   * var now:Date = new Date();
   * trace(now);
   * trace(now.getUTCMilliseconds());
   *
   * var before:Date = new Date(now.setUTCMilliseconds(4));
   * trace(before);
   * trace(before.getUTCMilliseconds());
   * </pre>
   *
   * @param millisecond An integer from 0 to 999.
   *
   * @return Number The new time, in milliseconds.
   */
  public native function setUTCMilliseconds(millisecond: Number):Number;

  /**
   * Sets the minutes, in universal time (UTC), and returns the new time in milliseconds.
   * Optionally, you can specify the seconds and milliseconds.
   *
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters.
   * The method <code>getUTCMinutes()</code> is then called, which retrieves the UTCMinutes when
   * <code>now</code> was created.  Then another new Date object <code>before</code> with an additional
   * call to <code>setUTCMinutes()</code> with the <code>minute</code> parameter set to <code>0</code> and
   * <code>getUTCMinutes()</code> is called again, which retrieves the newly set minutes.
   *
   * <pre>
   * var now:Date = new Date();
   * trace(now);
   * trace(now.getUTCMinutes());
   *
   * var before:Date = new Date(now.setUTCMinutes(0));
   * trace(before);
   * trace(before.getUTCMinutes());
   * </pre>
   *
   * @param minute An integer from 0 to 59.
   * @param second An integer from 0 to 59.
   * @param millisecond An integer from 0 to 999.
   *
   * @return Number The new time, in milliseconds.
   */
  public native function setUTCMinutes(minute: Number, second: Number = 1, millisecond : Number = 1):Number;

  /**
   * Sets the month, and optionally the day, in universal time(UTC) and returns the new
   * time in milliseconds. Calling this method does not modify the other fields, but the
   * <code>Date.getUTCDay()</code> and <code>Date.getDay()</code> methods might report a new
   * value if the day of the week changes as a result of calling this method.
   *
   * @example
   * The following example creates a new Array object <code>UTCMonthLabels</code>, with elements
   * <code>January</code> through <code>December</code> and a new UTCMonth object <code>now</code> with no parameters.
   * The method <code>getUTCMonth()</code> is then called, which retrieves the UTCMonth in which
   * <code>now</code> was created.  Next <code>setUTCMonth()</code> is called with the <code>month</code> parameter set to
   * <code>0</code> and then <code>getUTCMonth()</code> is called again, which retrieves the newly set month..
   * <pre>
   * var UTCMonthLabels:Array = new Array("January",
   *                   "February",
   *                   "March",
   *                   "April",
   *                   "May",
   *                   "June",
   *                   "July",
   *                   "August",
   *                   "September",
   *                   "October",
   *                   "November",
   *                   "December");
   *
   * var now:Date = new Date();
   * trace(now.getUTCMonth());
   * trace(UTCMonthLabels[now.getUTCMonth()]);
   *
   * now.setUTCUTCMonth(0);
   * trace(now.getUTCMonth());              // 0
   * trace(UTCMonthLabels[now.getUTCMonth()]); // January
   * </pre>
   *
   * @param month An integer from 0 (January) to 11 (December).
   * @param day An integer from 1 to 31.
   *
   * @return Number The new time, in milliseconds.
   *
   * @see #getDay()
   */
  public native function setUTCMonth(month: Number, day: Number = 1):Number;

  /**
   * Sets the seconds, and optionally the milliseconds, in universal time (UTC) and
   * returns the new time in milliseconds.
   *
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters.
   * The method <code>getUTCSeconds()</code> is then called, which retrieves the seconds when
   * <code>now</code> was created.  Then the <code>setUTCSeconds()</code> is called with the <code>second</code>
   *
   * parameter set to <code>0</code> and <code>getUTCSeconds()</code> is called again, which retrieves the
   * newly set seconds.
   * <pre>
   *
   * var now:Date = new Date();
   * trace(now.getUTCSeconds());
   *
   * now.setUTCSeconds(0);
   * trace(now.getUTCSeconds()); // 0
   * </pre>
   *
   * @param second An integer from 0 to 59.
   * @param millisecond An integer from 0 to 999.
   *
   * @return Number The new time, in milliseconds.
   */
  public native function setUTCSeconds(second: Number, millisecond : Number = 0):Number;

  /**
   * Returns a string representation of the day and date only, and does not include the time or timezone.
   * Contrast with the following methods:
   * <ul>
   *   <li><code>Date.toTimeString()</code>, which returns only the time and timezone</li>
   *   <li><code>Date.toString()</code>, which returns not only the day and date, but also the time and timezone.</li>
   * </ul>
   *
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters
   * and then the following methods are called within a <code>trace()</code> statement
   * <ul>
   *   <li><code>toString</code>: displays all parameters for <code>now</code> at the time <code>now</code> was
   *     created.</li>
   *   <li><code>toDateString()</code>: displays the <code>day</code>, <code>month</code>, and <code>year</code>
   *     parameters for the time <code>now</code> was created.</li>
   * </ul>
   * <pre>
   * var now:Date = new Date();
   * trace(now);
   * trace(now.toDateString());
   * </pre>
   *
   * @return String The string representation of day and date only.
   *
   * @see #toString()
   */
  public native function toDateString():String;

  /**
   * Returns a String representation of the day and date only, and does not include the time or timezone.
   * This method returns the same value as <code>Date.toDateString</code>.
   * Contrast with the following methods:
   * <ul>
   *   <li><code>Date.toTimeString()</code>, which returns only the time and timezone</li>
   *   <li><code>Date.toString()</code>, which returns not only the day and date, but also the time and timezone.</li>
   * </ul>
   * @return String The <code>String</code> representation of day and date only.
   *
   * @see #toDateString()
   * @see #toTimeString()
   * @see #toString()
   */
  public native function toLocaleDateString():String;

  /**
   * Returns a String representation of the day, date, time, given in local time.
   * Contrast with the <code>Date.toString()</code> method, which returns the same information (plus the timezone)
   * with the year listed at the end of the string.
   *
   * @return String A string representation of a <code>Date</code> object in the local timezone.
   */
  public native function toLocaleString():String;

  /**
   * Returns a String representation of the time only, and does not include the day, date, year, or timezone.
   * Contrast with the <code>Date.toTimeString()</code> method, which returns the time and timezone.
   *
   * @return String The string representation of time and timezone only.
   *
   * @see #toTimeString()
   */
  public native function toLocaleTimeString():String;

  /**
   * Returns a String representation of the day, date, time, and timezone.
   * The date format for the output is:
   * <pre>
   * Day Mon Date HH:MM:SS TZD YYYY
   * </pre>
   * <p>For example:</p>
   * <pre>
   * Wed Apr 12 15:30:17 GMT-0700 2006
   * </pre>
   *
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters
   * and then <code>toString</code> is called within a <code>trace()</code> statement, which
   * displays all parameters for <code>now</code> at the time <code>now</code> was created.
   * <pre>
   * var now:Date = new Date();
   * trace(now);
   * </pre>
   *
   * @return String The string representation of a <code>Date</code> object.
   */
  public native function toString():String;

  /**
   * Returns a String representation of the time and timezone only, and does not include the day and date.
   * Contrast with the <code>Date.toDateString()</code> method, which returns only the day and date.
   *
   * @return String The string representation of time and timezone only.
   *
   * @see #toDateString()
   */
  public native function toTimeString():String;

  /**
   * Returns a String representation of the day, date, and time in universal time (UTC).
   * For example, the date February 1, 2005 is returned as <code>Tue Feb 1 00:00:00 2005 UTC</code>.
   *
   * @return String The string representation of a <code>Date</code> object in UTC time.
   *
   * @see #toString()
   */
  public native function toUTCString():String;


  /**
   * Returns the number of milliseconds between midnight on January 1, 1970, universal time,
   * and the time specified in the parameters. This method uses universal time, whereas the
   * <code>Date</code> constructor uses local time.
   * <p>This method is useful if you want to pass a UTC date to the Date class constructor.
   * Because the Date class constructor accepts the millisecond offset as an argument, you
   * can use the Date.UTC() method to convert your UTC date into the corresponding millisecond
   * offset, and send that offset as an argument to the Date class constructor:</p>
   *
   * @example
   * The following example creates a new Date object <code>someBirthday</code> with parameters
   * <code>year</code> (<code>1974</code>), <code>month</code> (<code>10</code> = November), <code>day</code>
   * (<code>30</code>), <code>hour</code> (<code>1</code>) and <code>minute</code> (<code>20</code>) using local
   * time.  Then a call to <code>UTC()</code> within a <code>setTime()</code> method resets the same parameters
   * to universal time.
   * <pre>
   * var someBirthday:Date = new Date(1974, 10, 30, 15, 20);
   * trace(someBirthday.toString());
   *
   * someBirthday.setTime(Date.UTC(1974, 10, 30, 15, 20));
   * trace(someBirthday.toString());
   * </pre>
   *
   * @param year A four-digit integer that represents the year (for example, 2000).
   * @param month An integer from 0 (January) to 11 (December).
   * @param date (default = <code>1</code>)<code></code> &mdash; An integer from 1 to 31.
   * @param hour (default = <code>0</code>)<code></code> &mdash; An integer from 0 (midnight) to 23 (11 p.m.).
   * @param minute (default = <code>0</code>)<code></code> &mdash; An integer from 0 to 59.
   * @param second (default = <code>0</code>)<code></code> &mdash; An integer from 0 to 59.
   * @param millisecond (default = <code>0</code>)<code></code> &mdash; An integer from 0 to 999.
   *
   * @return Number The number of milliseconds since January 1, 1970 and the specified date and time.
   */
  public native static function UTC(year:Number, month:Number, date:Number = 1, hour:Number = 0, minute:Number = 0, second:Number = 0, millisecond:Number = 0):String;

  /**
   * Returns the number of milliseconds since midnight January 1, 1970, universal time,
   * for a <code>Date</code> object.
   *
   * @example
   * The following example creates a new Date object <code>now</code> with no parameters
   * The <code>getTime()</code> method is then called, which retrieves the number of milliseconds between
   * the time <code>now</code> was created and midnight on
   * January 1, 1970, and then <code>valueOf()</code> is called, which retrieves the same thing.
   * <pre>
   * var now:Date = new Date();
   * trace(now.getTime());
   * trace(now.valueOf());
   * </pre>
   *
   * @return Number The number of milliseconds since January 1, 1970 that a <code>Date</code> object represents.
   */
  public native function valueOf():Number;

  /**
   * @deprecated
   * @return Number the year since 1900
   */
  public native function getYear():Number;

  /**
   * @deprecated
   * @param year the year since 1900
   * @return Number
   */
  public native function setYear(year: Number):Number;


  /**
   * JavaScript only.
   * @return String
   */
  public native function toGMTString():String;

}

}