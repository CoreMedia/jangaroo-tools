joo.classLoader.prepare("package flash.events",/* {*/

/**
 * The Event class is used as the base class for the creation of Event objects, which are passed as parameters to event listeners when an event occurs.
 * <p>The properties of the Event class carry basic information about an event, such as the event's type or whether the event's default behavior can be canceled. For many events, such as the events represented by the Event class constants, this basic information is sufficient. Other events, however, may require more detailed information. Events associated with a mouse click, for example, need to include additional information about the location of the click event and whether any keys were pressed during the click event. You can pass such additional information to event listeners by extending the Event class, which is what the MouseEvent class does. ActionScript 3.0 API defines several Event subclasses for common events that require additional information. Events associated with each of the Event subclasses are described in the documentation for each class.</p>
 * <p>The methods of the Event class can be used in event listener functions to affect the behavior of the event object. Some events have an associated default behavior. For example, the <code>doubleClick</code> event has an associated default behavior that highlights the word under the mouse pointer at the time of the event. Your event listener can cancel this behavior by calling the <code>preventDefault()</code> method. You can also make the current event listener the last one to process an event by calling the <code>stopPropagation()</code> or <code>stopImmediatePropagation()</code> method.</p>
 * <p>Other sources of information include:</p>
 * <ul>
 * <li>A useful description about the timing of events, code execution, and rendering at runtime in Ted Patrick's blog entry: <a href="http://www.onflex.org/ted/2005/07/flash-player-mental-model-elastic.php">Flash Player Mental Model - The Elastic Racetrack</a>.</li>
 * <li>A blog entry by Johannes Tacskovics about the timing of frame events, such as ENTER_FRAME, EXIT_FRAME: <a href="http://blog.johannest.com/2009/06/15/the-movieclip-life-cycle-revisited-from-event-added-to-event-removed_from_stage/">The MovieClip Lifecycle</a>.</li>
 * <li>An article by Trevor McCauley about the order of ActionScript operations: <a href="http://www.senocular.com/flash/tutorials/orderofoperations/">Order of Operations in ActionScript</a>.</li>
 * <li>A blog entry by Matt Przybylski on creating custom events: <a href="http://evolve.reintroducing.com/2007/10/23/as3/as3-custom-events/">AS3: Custom Events</a>.</li></ul>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/Event.html#includeExamplesSummary">View the examples</a></p>
 * @see EventDispatcher
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf64a29-7fff.html About events
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf64a29-7ffe.html About the Event class
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf69084-7cdf.html Using events
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf69084-7ce1.html Manually dispatching events
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf69084-7cdb.html Event propagation
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf69084-7cda.html Event priorities
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf69084-7ce0.html Using event subclasses
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WS2db454920e96a9e51e63e3d11c0bf64a29-7fdb.html About keyboard events
 * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e55.html Event objects
 *
 */
"public class Event",1,function($$private){;return[ 
  /**
   * Indicates whether an event is a bubbling event. If the event can bubble, this value is <code>true</code>; otherwise it is <code>false</code>.
   * <p>When an event occurs, it moves through the three phases of the event flow: the capture phase, which flows from the top of the display list hierarchy to the node just before the target node; the target phase, which comprises the target node; and the bubbling phase, which flows from the node subsequent to the target node back up the display list hierarchy.</p>
   * <p>Some events, such as the <code>activate</code> and <code>unload</code> events, do not have a bubbling phase. The <code>bubbles</code> property has a value of <code>false</code> for events that do not have a bubbling phase.</p>
   */
  "public native function get bubbles"/*():Boolean;*/,

  /**
   * Indicates whether the behavior associated with the event can be prevented. If the behavior can be canceled, this value is <code>true</code>; otherwise it is <code>false</code>.
   * @see #preventDefault()
   *
   */
  "public native function get cancelable"/*():Boolean;*/,

  /**
   * The object that is actively processing the Event object with an event listener. For example, if a user clicks an OK button, the current target could be the node containing that button or one of its ancestors that has registered an event listener for that event.
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e55.html Event objects
   *
   */
  "public native function get currentTarget"/*():Object;*/,

  /**
   * The current phase in the event flow. This property can contain the following numeric values:
   * <ul>
   * <li>The capture phase (<code>EventPhase.CAPTURING_PHASE</code>).</li>
   * <li>The target phase (<code>EventPhase.AT_TARGET</code>).</li>
   * <li>The bubbling phase (<code>EventPhase.BUBBLING_PHASE</code>).</li></ul>
   */
  "public native function get eventPhase"/*():uint;*/,

  /**
   * The event target. This property contains the target node. For example, if a user clicks an OK button, the target node is the display list node containing that button.
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e55.html Event objects
   *
   */
  "public native function get target"/*():Object;*/,

  /**
   * The type of event. The type is case-sensitive.
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e55.html Event objects
   *
   */
  "public native function get type"/*():String;*/,

  /**
   * Creates an Event object to pass as a parameter to event listeners.
   * @param type The type of the event, accessible as <code>Event.type</code>.
   * @param bubbles Determines whether the Event object participates in the bubbling stage of the event flow. The default value is <code>false</code>.
   * @param cancelable Determines whether the Event object can be canceled. The default values is <code>false</code>.
   *
   */
  "public function Event",function Event$(type/*:String*/, bubbles/*:Boolean = false*/, cancelable/*:Boolean = false*/) {switch(arguments.length){case 0:case 1:bubbles = false;case 2:cancelable = false;}
    this['type'] = type;
    this['bubbles'] = bubbles;
    this['cancelable'] = cancelable;
  },

  /**
   * Duplicates an instance of an Event subclass.
   * <p>Returns a new Event object that is a copy of the original instance of the Event object. You do not normally call <code>clone()</code>; the EventDispatcher class calls it automatically when you redispatch an event�that is, when you call <code>dispatchEvent(event)</code> from a handler that is handling <code>event</code>.</p>
   * <p>The new Event object includes all the properties of the original.</p>
   * <p>When creating your own custom Event class, you must override the inherited <code>Event.clone()</code> method in order for it to duplicate the properties of your custom class. If you do not set all the properties that you add in your event subclass, those properties will not have the correct values when listeners handle the redispatched event.</p>
   * <p>In this example, <code>PingEvent</code> is a subclass of <code>Event</code> and therefore implements its own version of <code>clone()</code>.</p>
   * <listing>
   *      class PingEvent extends Event {
   *          var URL:String;
   *
   *      public override function clone():Event {
   *               return new PingEvent(type, bubbles, cancelable, URL);
   *         }
   *      }
   *     </listing>
   * @return A new Event object that is identical to the original.
   *
   */
  "public function clone",function clone()/*:Event*/ {
    return new flash.events.Event(this.type, this.bubbles, this.cancelable);
  },

  /**
   * A utility function for implementing the <code>toString()</code> method in custom ActionScript 3.0 Event classes. Overriding the <code>toString()</code> method is recommended, but not required.
   * <pre>     class PingEvent extends Event {
   var URL:String;

   public override function toString():String {
   return formatToString("PingEvent", "type", "bubbles", "cancelable", "eventPhase", "URL");
   }
   }
   </pre>
   * @param className The name of your custom Event class. In the previous example, the <code>className</code> parameter is <code>PingEvent</code>.
   * @param rest The properties of the Event class and the properties that you add in your custom Event class. In the previous example, the <code>...arguments</code> parameter includes <code>type</code>, <code>bubbles</code>, <code>cancelable</code>, <code>eventPhase</code>, and <code>URL</code>.
   *
   * @return The name of your custom Event class and the String value of your <code>...arguments</code> parameter.
   *
   */
  "public function formatToString",function formatToString(className/*:String, ...rest*/)/*:String*/ {var rest=Array.prototype.slice.call(arguments,1);
    var sb/*:Array*/ = ["[", className, " "];
    for (var i/*:uint*/ = 0; i < rest.length; ++i) {
      sb.push(rest[i], "=", this[rest[i]], " ");
    }
    sb.push("]");
    return sb.join("");
  },
  /**
   * Checks whether the <code>preventDefault()</code> method has been called on the event. If the <code>preventDefault()</code> method has been called, returns <code>true</code>; otherwise, returns <code>false</code>.
   * @return If <code>preventDefault()</code> has been called, returns <code>true</code>; otherwise, returns <code>false</code>.
   *
   * @see #preventDefault()
   *
   */
  "public function isDefaultPrevented",function isDefaultPrevented()/*:Boolean*/ {
    return this.defaultPrevented$1;
  },

  /**
   * Cancels an event's default behavior if that behavior can be canceled.
   * <p>Many events have associated behaviors that are carried out by default. For example, if a user types a character into a text field, the default behavior is that the character is displayed in the text field. Because the <code>TextEvent.TEXT_INPUT</code> event's default behavior can be canceled, you can use the <code>preventDefault()</code> method to prevent the character from appearing.</p>
   * <p>An example of a behavior that is not cancelable is the default behavior associated with the <code>Event.REMOVED</code> event, which is generated whenever Flash Player is about to remove a display object from the display list. The default behavior (removing the element) cannot be canceled, so the <code>preventDefault()</code> method has no effect on this default behavior.</p>
   * <p>You can use the <code>Event.cancelable</code> property to check whether you can prevent the default behavior associated with a particular event. If the value of <code>Event.cancelable</code> is <code>true</code>, then <code>preventDefault()</code> can be used to cancel the event; otherwise, <code>preventDefault()</code> has no effect.</p>
   * @see #isDefaultPrevented()
   * @see #cancelable
   *
   */
  "public function preventDefault",function preventDefault()/*:void*/ {
    if (this.cancelable) {
      this.defaultPrevented$1 = true;
    }
  },

  /**
   * Prevents processing of any event listeners in the current node and any subsequent nodes in the event flow. This method takes effect immediately, and it affects event listeners in the current node. In contrast, the <code>stopPropagation()</code> method doesn't take effect until all the event listeners in the current node finish processing.
   * <p><b>Note:</b> This method does not cancel the behavior associated with this event; see <code>preventDefault()</code> for that functionality.</p>
   * @see #stopPropagation()
   * @see #preventDefault()
   *
   */
  "public function stopImmediatePropagation",function stopImmediatePropagation()/*:void*/ {
    this.immediatePropagationStopped$1 = true;
  },

  /**
   * Prevents processing of any event listeners in nodes subsequent to the current node in the event flow. This method does not affect any event listeners in the current node (<code>currentTarget</code>). In contrast, the <code>stopImmediatePropagation()</code> method prevents processing of event listeners in both the current node and subsequent nodes. Additional calls to this method have no effect. This method can be called in any phase of the event flow.
   * <p><b>Note:</b> This method does not cancel the behavior associated with this event; see <code>preventDefault()</code> for that functionality.</p>
   * @see #stopImmediatePropagation()
   * @see #preventDefault()
   *
   */
  "public function stopPropagation",function stopPropagation()/*:void*/ {
    this.propagationStopped$1 = true;
  },

  /**
   * Returns a string containing all the properties of the Event object. The string is in the following format:
   * <p><code>[Event type=<i>value</i> bubbles=<i>value</i> cancelable=<i>value</i>]</code></p>
   * @return A string containing all the properties of the Event object.
   *
   */
  "public function toString",function toString()/*:String*/ {
    return this.formatToString("Event", "type", "bubbles", "cancelable");
  },

  /**
   * The <code>ACTIVATE</code> constant defines the value of the <code>type</code> property of an <code>activate</code> event object.
   * <p><b>Note:</b> This event has neither a "capture phase" nor a "bubble phase", which means that event listeners must be added directly to any potential targets, whether the target is on the display list or not.</p>
   * <p>AIR for TV devices never automatically dispatch this event. You can, however, dispatch it manually.</p>
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
   * <td><code>target</code></td>
   * <td>Any DisplayObject instance with a listener registered for the <code>activate</code> event.</td></tr></table>
   * @see EventDispatcher#event:activate
   * @see #DEACTIVATE
   *
   */
  "public static const",{ ACTIVATE/*:String*/ : "activate"},
  /**
   * The <code>Event.ADDED</code> constant defines the value of the <code>type</code> property of an <code>added</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>true</code></td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>false</code>; there is no default behavior to cancel.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The DisplayObject instance being added to the display list. The <code>target</code> is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.display.DisplayObject#event:added
   * @see #ADDED_TO_STAGE
   * @see #REMOVED
   * @see #REMOVED_FROM_STAGE
   *
   */
  "public static const",{ ADDED/*:String*/ : "added"},
  /**
   * The <code>Event.ADDED_TO_STAGE</code> constant defines the value of the <code>type</code> property of an <code>addedToStage</code> event object.
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
   * <td><code>target</code></td>
   * <td>The DisplayObject instance being added to the on stage display list, either directly or through the addition of a sub tree in which the DisplayObject instance is contained. If the DisplayObject instance is being directly added, the <code>added</code> event occurs before this event.</td></tr></table>
   * @see flash.display.DisplayObject#event:addedToStage
   * @see #ADDED
   * @see #REMOVED
   * @see #REMOVED_FROM_STAGE
   *
   */
  "public static const",{ ADDED_TO_STAGE/*:String*/ : "addedToStage"},
  /**
   * The <code>Event.CANCEL</code> constant defines the value of the <code>type</code> property of a <code>cancel</code> event object.
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
   * <td><code>target</code></td>
   * <td>A reference to the object on which the operation is canceled.</td></tr></table>
   * @see flash.net.FileReference#event:cancel
   *
   */
  "public static const",{ CANCEL/*:String*/ : "cancel"},
  /**
   * The <code>Event.CHANGE</code> constant defines the value of the <code>type</code> property of a <code>change</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>true</code></td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>false</code>; there is no default behavior to cancel.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The object that has had its value modified. The <code>target</code> is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.text.TextField#event:change
   * @see TextEvent#TEXT_INPUT
   *
   */
  "public static const",{ CHANGE/*:String*/ : "change"},
  /**
   * The <code>Event.CLOSE</code> constant defines the value of the <code>type</code> property of a <code>close</code> event object.
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
   * <td><code>target</code></td>
   * <td>The object whose connection has been closed.</td></tr></table>
   * @see flash.net.Socket#event:close
   * @see flash.net.XMLSocket#event:close
   * @see flash.display.NativeWindow#event:close
   *
   */
  "public static const",{ CLOSE/*:String*/ : "close"},
  /**
   * The <code>Event.COMPLETE</code> constant defines the value of the <code>type</code> property of a <code>complete</code> event object.
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
   * <td><code>target</code></td>
   * <td>The network object that has completed loading.</td></tr></table>
   * @see flash.display.LoaderInfo#event:complete
   * @see flash.html.HTMLLoader#event:complete
   * @see flash.media.Sound#event:complete
   * @see flash.net.FileReference#event:complete
   * @see flash.net.URLLoader#event:complete
   * @see flash.net.URLStream#event:complete
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7cfd.html Loading external data
   *
   */
  "public static const",{ COMPLETE/*:String*/ : "complete"},
  /**
   * The <code>Event.CONNECT</code> constant defines the value of the <code>type</code> property of a <code>connect</code> event object.
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
   * <td><code>target</code></td>
   * <td>The Socket or XMLSocket object that has established a network connection.</td></tr></table>
   * @see flash.net.Socket#event:connect
   * @see flash.net.XMLSocket#event:connect
   *
   */
  "public static const",{ CONNECT/*:String*/ : "connect"},
  /**
   * The <code>Event.DEACTIVATE</code> constant defines the value of the <code>type</code> property of a <code>deactivate</code> event object.
   * <p><b>Note:</b> This event has neither a "capture phase" nor a "bubble phase", which means that event listeners must be added directly to any potential targets, whether the target is on the display list or not.</p>
   * <p>AIR for TV devices never automatically dispatch this event. You can, however, dispatch it manually.</p>
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
   * <td><code>target</code></td>
   * <td>Any DisplayObject instance with a listener registered for the <code>deactivate</code> event.</td></tr></table>
   * @see EventDispatcher#event:deactivate
   * @see #ACTIVATE
   *
   */
  "public static const",{ DEACTIVATE/*:String*/ : "deactivate"},
  /**
   * The <code>Event.ENTER_FRAME</code> constant defines the value of the <code>type</code> property of an <code>enterFrame</code> event object.
   * <p><b>Note:</b> This event has neither a "capture phase" nor a "bubble phase", which means that event listeners must be added directly to any potential targets, whether the target is on the display list or not.</p>
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
   * <td><code>target</code></td>
   * <td>Any DisplayObject instance with a listener registered for the <code>enterFrame</code> event.</td></tr></table>
   * @see flash.display.DisplayObject#event:enterFrame
   *
   */
  "public static const",{ ENTER_FRAME/*:String*/ : "enterFrame"},
  /**
   * The <code>Event.EXIT_FRAME</code> constant defines the value of the <code>type</code> property of an <code>exitFrame</code> event object.
   * <p><b>Note:</b> This event has neither a "capture phase" nor a "bubble phase", which means that event listeners must be added directly to any potential targets, whether the target is on the display list or not.</p>
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
   * <td><code>target</code></td>
   * <td>Any DisplayObject instance with a listener registered for the <code>enterFrame</code> event.</td></tr></table>
   * @see flash.display.DisplayObject#event:exitFrame
   *
   */
  "public static const",{ EXIT_FRAME/*:String*/ : "exitFrame"},
  /**
   * The <code>Event.FRAME_CONSTRUCTED</code> constant defines the value of the <code>type</code> property of an <code>frameConstructed</code> event object.
   * <p><b>Note:</b> This event has neither a "capture phase" nor a "bubble phase", which means that event listeners must be added directly to any potential targets, whether the target is on the display list or not.</p>
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
   * <td><code>target</code></td>
   * <td>Any DisplayObject instance with a listener registered for the <code>frameConstructed</code> event.</td></tr></table>
   * @see flash.display.DisplayObject#event:frameConstructed
   *
   */
  "public static const",{ FRAME_CONSTRUCTED/*:String*/ : "frameConstructed"},
  /**
   * The <code>Event.FULL_SCREEN</code> constant defines the value of the <code>type</code> property of a <code>fullScreen</code> event object.
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
   * <td><code>target</code></td>
   * <td>The Stage object.</td></tr></table>
   * @see flash.display.Stage#event:fullScreen
   *
   */
  "public static const",{ FULLSCREEN/*:String*/ : "fullScreen"},
  /**
   * The <code>Event.ID3</code> constant defines the value of the <code>type</code> property of an <code>id3</code> event object.
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
   * <td><code>target</code></td>
   * <td>The Sound object loading the MP3 for which ID3 data is now available. The <code>target</code> is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.media.Sound#event:id3
   *
   */
  "public static const",{ ID3/*:String*/ : "id3"},
  /**
   * The <code>Event.INIT</code> constant defines the value of the <code>type</code> property of an <code>init</code> event object.
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
   * <td><code>target</code></td>
   * <td>The LoaderInfo object associated with the SWF file being loaded.</td></tr></table>
   * @see flash.display.LoaderInfo#event:init
   *
   */
  "public static const",{ INIT/*:String*/ : "init"},
  /**
   * The <code>Event.MOUSE_LEAVE</code> constant defines the value of the <code>type</code> property of a <code>mouseLeave</code> event object.
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
   * <td><code>target</code></td>
   * <td>The Stage object. The <code>target</code> is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.display.Stage#event:mouseLeave
   * @see MouseEvent
   *
   */
  "public static const",{ MOUSE_LEAVE/*:String*/ : "mouseLeave"},
  /**
   * The <code>Event.OPEN</code> constant defines the value of the <code>type</code> property of an <code>open</code> event object.
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
   * <td><code>target</code></td>
   * <td>The network object that has opened a connection.</td></tr></table>
   * @see flash.display.LoaderInfo#event:open
   * @see flash.media.Sound#event:open
   * @see flash.net.FileReference#event:open
   * @see flash.net.URLLoader#event:open
   * @see flash.net.URLStream#event:open
   *
   */
  "public static const",{ OPEN/*:String*/ : "open"},
  /**
   * The <code>Event.REMOVED</code> constant defines the value of the <code>type</code> property of a <code>removed</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>true</code></td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>false</code>; there is no default behavior to cancel.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The DisplayObject instance to be removed from the display list. The <code>target</code> is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.display.DisplayObject#event:removed
   * @see #ADDED
   * @see #ADDED_TO_STAGE
   * @see #REMOVED_FROM_STAGE
   *
   */
  "public static const",{ REMOVED/*:String*/ : "removed"},
  /**
   * The <code>Event.REMOVED_FROM_STAGE</code> constant defines the value of the <code>type</code> property of a <code>removedFromStage</code> event object.
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
   * <td><code>target</code></td>
   * <td>The DisplayObject instance being removed from the on stage display list, either directly or through the removal of a sub tree in which the DisplayObject instance is contained. If the DisplayObject instance is being directly removed, the <code>removed</code> event occurs before this event.</td></tr></table>
   * @see flash.display.DisplayObject#event:removedFromStage
   * @see #ADDED
   * @see #REMOVED
   * @see #ADDED_TO_STAGE
   *
   */
  "public static const",{ REMOVED_FROM_STAGE/*:String*/ : "removedFromStage"},
  /**
   * The <code>Event.RENDER</code> constant defines the value of the <code>type</code> property of a <code>render</code> event object.
   * <p><b>Note:</b> This event has neither a "capture phase" nor a "bubble phase", which means that event listeners must be added directly to any potential targets, whether the target is on the display list or not.</p>
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>false</code></td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>false</code>; the default behavior cannot be canceled.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>Any DisplayObject instance with a listener registered for the <code>render</code> event.</td></tr></table>
   * @see flash.display.DisplayObject#event:render
   * @see flash.display.Stage#invalidate()
   *
   */
  "public static const",{ RENDER/*:String*/ : "render"},
  /**
   * The <code>Event.RESIZE</code> constant defines the value of the <code>type</code> property of a <code>resize</code> event object.
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
   * <td><code>target</code></td>
   * <td>The Stage object.</td></tr></table>
   * @see flash.display.Stage#event:resize
   *
   */
  "public static const",{ RESIZE/*:String*/ : "resize"},
  /**
   * The <code>Event.SCROLL</code> constant defines the value of the <code>type</code> property of a <code>scroll</code> event object.
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
   * <td><code>target</code></td>
   * <td>The TextField object that has been scrolled. The <code>target</code> property is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.text.TextField#event:scroll
   * @see flash.html.HTMLLoader#event:scroll
   *
   */
  "public static const",{ SCROLL/*:String*/ : "scroll"},
  /**
   * The <code>Event.SELECT</code> constant defines the value of the <code>type</code> property of a <code>select</code> event object.
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
   * <td><code>target</code></td>
   * <td>The object on which an item has been selected.</td></tr></table>
   * @see flash.net.FileReference#event:select
   * @see flash.display.NativeMenu#event:select
   * @see flash.display.NativeMenuItem#event:select
   *
   */
  "public static const",{ SELECT/*:String*/ : "select"},
  /**
   * The <code>Event.SOUND_COMPLETE</code> constant defines the value of the <code>type</code> property of a <code>soundComplete</code> event object.
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
   * <td><code>target</code></td>
   * <td>The SoundChannel object in which a sound has finished playing.</td></tr></table>
   * @see flash.media.SoundChannel#event:soundComplete
   *
   */
  "public static const",{ SOUND_COMPLETE/*:String*/ : "soundComplete"},
  /**
   * The <code>Event.TAB_CHILDREN_CHANGE</code> constant defines the value of the <code>type</code> property of a <code>tabChildrenChange</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>true</code></td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>false</code>; there is no default behavior to cancel.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The object whose tabChildren flag has changed. The <code>target</code> is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.display.InteractiveObject#event:tabChildrenChange
   *
   */
  "public static const",{ TAB_CHILDREN_CHANGE/*:String*/ : "tabChildrenChange"},
  /**
   * The <code>Event.TAB_ENABLED_CHANGE</code> constant defines the value of the <code>type</code> property of a <code>tabEnabledChange</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>true</code></td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>false</code>; there is no default behavior to cancel.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The InteractiveObject whose tabEnabled flag has changed. The <code>target</code> is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.display.InteractiveObject#event:tabEnabledChange
   *
   */
  "public static const",{ TAB_ENABLED_CHANGE/*:String*/ : "tabEnabledChange"},
  /**
   * The <code>Event.TAB_INDEX_CHANGE</code> constant defines the value of the <code>type</code> property of a <code>tabIndexChange</code> event object.
   * <p>This event has the following properties:</p>
   * <table>
   * <tr><th>Property</th><th>Value</th></tr>
   * <tr>
   * <td><code>bubbles</code></td>
   * <td><code>true</code></td></tr>
   * <tr>
   * <td><code>cancelable</code></td>
   * <td><code>false</code>; there is no default behavior to cancel.</td></tr>
   * <tr>
   * <td><code>currentTarget</code></td>
   * <td>The object that is actively processing the Event object with an event listener.</td></tr>
   * <tr>
   * <td><code>target</code></td>
   * <td>The object whose tabIndex has changed. The <code>target</code> is not always the object in the display list that registered the event listener. Use the <code>currentTarget</code> property to access the object in the display list that is currently processing the event.</td></tr></table>
   * @see flash.display.InteractiveObject#event:tabIndexChange
   *
   */
  "public static const",{ TAB_INDEX_CHANGE/*:String*/ : "tabIndexChange"},
  /**
   * The <code>Event.UNLOAD</code> constant defines the value of the <code>type</code> property of an <code>unload</code> event object.
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
   * <td><code>target</code></td>
   * <td>The LoaderInfo object associated with the SWF file being unloaded or replaced.</td></tr></table>
   * @see flash.display.LoaderInfo#event:unload
   *
   */
  "public static const",{ UNLOAD/*:String*/ : "unload"},

  // ************************** Jangaroo part **************************

  /**
   * @private
   */
  "public function isPropagationStopped",function isPropagationStopped()/*:Boolean*/ {
    return this.propagationStopped$1;
  },

  /**
   * @private
   */
  "public function isImmediatePropagationStopped",function isImmediatePropagationStopped()/*:Boolean*/ {
    return this.immediatePropagationStopped$1;
  },

  /**
   * @private
   */
  "internal function withTarget",function withTarget(target/*:Object*/)/*:Event*/ {
    var event/*:Event*/ = this.target ? this.clone() : this;
    event['target'] = target;
    return event;
  },

  "private var",{ defaultPrevented/*:Boolean*/ : false},
  "private var",{ propagationStopped/*:Boolean*/:false},
  "private var",{ immediatePropagationStopped/*:Boolean*/:false},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);