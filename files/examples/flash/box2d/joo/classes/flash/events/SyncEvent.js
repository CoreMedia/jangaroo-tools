joo.classLoader.prepare("package flash.events",/* {*/

/**
 * An SharedObject object representing a remote shared object dispatches a SyncEvent object when the remote shared object has been updated by the server. There is only one type of <code>sync</code> event: <code>SyncEvent.SYNC</code>.
 * @see flash.net.SharedObject
 *
 */
"public class SyncEvent extends flash.events.Event",2,function($$private){;return[ 
  /**
   * An array of objects; each object contains properties that describe the changed members of a remote shared object. The properties of each object are <code>code</code>, <code>name</code>, and <code>oldValue</code>.
   * <p>When you initially connect to a remote shared object that is persistent locally and/or on the server, all the properties of this object are set to empty strings.</p>
   * <p>Otherwise, Flash sets <code>code</code> to <code>"clear"</code>, <code>"success"</code>, <code>"reject"</code>, <code>"change"</code>, or <code>"delete"</code>.</p>
   * <ul>
   * <li>A value of <code>"clear"</code> means either that you have successfully connected to a remote shared object that is not persistent on the server or the client, or that all the properties of the object have been deleted--for example, when the client and server copies of the object are so far out of sync that Flash Player resynchronizes the client object with the server object. In the latter case, <code>SyncEvent.SYNC</code> is dispatched and the "code" value is set to <code>"change"</code>.</li>
   * <li>A value of <code>"success"</code> means the client changed the shared object.</li>
   * <li>A value of <code>"reject"</code> means the client tried unsuccessfully to change the object; instead, another client changed the object.</li>
   * <li>A value of <code>"change"</code> means another client changed the object or the server resynchronized the object.</li>
   * <li>A value of <code>"delete"</code> means the attribute was deleted.</li></ul>
   * <p>The <code>name</code> property contains the name of the property that has been changed.</p>
   * <p>The <code>oldValue</code> property contains the former value of the changed property. This parameter is <code>null</code> unless code has a value of <code>"reject"</code> or <code>"change"</code>.</p>
   * @see flash.net.NetConnection
   * @see flash.net.NetStream
   *
   */
  "public native function get changeList"/*():Array;*/,

  /**
   * @private
   */
  "public native function set changeList"/*(value:Array):void;*/,

  /**
   * Creates an Event object that contains information about <code>sync</code> events. Event objects are passed as parameters to event listeners.
   * @param type The type of the event. Event listeners can access this information through the inherited <code>type</code> property. There is only one type of sync event: <code>SyncEvent.SYNC</code>.
   * @param bubbles Determines whether the Event object participates in the bubbling stage of the event flow. Event listeners can access this information through the inherited <code>bubbles</code> property.
   * @param cancelable Determines whether the Event object can be canceled. Event listeners can access this information through the inherited <code>cancelable</code> property.
   * @param changeList An array of objects that describe the synchronization with the remote SharedObject. Event listeners can access this object through the <code>changeList</code> property.
   *
   * @see #changeList
   *
   */
  "public function SyncEvent",function SyncEvent$(type/*:String*/, bubbles/*:Boolean = false*/, cancelable/*:Boolean = false*/, changeList/*:Array = null*/) {if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){bubbles = false;}cancelable = false;}changeList = null;}
    this.super$2(type, bubbles, cancelable);
    this.changeList = changeList;
  },

  /**
   * Creates a copy of the SyncEvent object and sets the value of each property to match that of the original.
   * @return A new SyncEvent object with property values that match those of the original.
   *
   */
  "override public function clone",function clone()/*:Event*/ {
    return new flash.events.SyncEvent(this.type, this.bubbles, this.cancelable, this.changeList);
  },

  /**
   * Returns a string that contains all the properties of the SyncEvent object. The string is in the following format:
   * <p><code>[SyncEvent type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i> changeList=<i>value</i>]</code></p>
   * @return A string that contains all the properties of the SyncEvent object.
   *
   */
  "override public function toString",function toString()/*:String*/ {
    return this.formatToString("SyncEvent", "type", "bubbles", "cancelable", "changeList");
  },

  /**
   * Defines the value of the <code>type</code> property of a <code>sync</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>false</code></td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>false</code>; there is no default behavior to cancel.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>changeList</code></td>
   * <td>An array with properties that describe the array's status.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The SharedObject instance that has been updated by the server.</td></tr></table>
   * @see flash.net.SharedObject#event:sync
   *
   */
  "public static const",{ SYNC/*:String*/ : "sync"},

];},[],["flash.events.Event"], "0.8.0", "0.8.1"
);