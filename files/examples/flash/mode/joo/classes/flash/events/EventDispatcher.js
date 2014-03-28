joo.classLoader.prepare("package flash.events",/* {*/

/**
 * [broadcast event] Dispatched when the Flash Player or AIR application gains operating system focus and becomes active. This event is a broadcast event, which means that it is dispatched by all EventDispatcher objects with a listener registered for this event. For more information about broadcast events, see the DisplayObject class.
 * @eventType flash.events.Event.ACTIVATE
 */
{Event:{name:"activate", type:"flash.events.Event"}},
/**
 * [broadcast event] Dispatched when the Flash Player or AIR application operating loses system focus and is becoming inactive. This event is a broadcast event, which means that it is dispatched by all EventDispatcher objects with a listener registered for this event. For more information about broadcast events, see the DisplayObject class.
 * @eventType flash.events.Event.DEACTIVATE
 */
{Event:{name:"deactivate", type:"flash.events.Event"}},

/**
 * The EventDispatcher class is the base class for all classes that dispatch events. The EventDispatcher class implements the IEventDispatcher interface and is the base class for the DisplayObject class. The EventDispatcher class allows any object on the display list to be an event target and as such, to use the methods of the IEventDispatcher interface.
 * <p>Event targets are an important part of the Flash<sup>�</sup> Player and Adobe<sup>�</sup> AIR<sup>�</sup> event model. The event target serves as the focal point for how events flow through the display list hierarchy. When an event such as a mouse click or a keypress occurs, Flash Player or the AIR application dispatches an event object into the event flow from the root of the display list. The event object then makes its way through the display list until it reaches the event target, at which point it begins its return trip through the display list. This round-trip journey to the event target is conceptually divided into three phases: the capture phase comprises the journey from the root to the last node before the event target's node, the target phase comprises only the event target node, and the bubbling phase comprises any subsequent nodes encountered on the return trip to the root of the display list.</p>
 * <p>In general, the easiest way for a user-defined class to gain event dispatching capabilities is to extend EventDispatcher. If this is impossible (that is, if the class is already extending another class), you can instead implement the IEventDispatcher interface, create an EventDispatcher member, and write simple hooks to route calls into the aggregated EventDispatcher.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/events/EventDispatcher.html#includeExamplesSummary">View the examples</a></p>
 */
"public class EventDispatcher implements flash.events.IEventDispatcher",1,function($$private){;return[ 
  /**
   * Aggregates an instance of the EventDispatcher class.
   * <p>The EventDispatcher class is generally used as a base class, which means that most developers do not need to use this constructor function. However, advanced developers who are implementing the IEventDispatcher interface need to use this constructor. If you are unable to extend the EventDispatcher class and must instead implement the IEventDispatcher interface, use this constructor to aggregate an instance of the EventDispatcher class.</p>
   * @param target The target object for events dispatched to the EventDispatcher object. This parameter is used when the EventDispatcher instance is aggregated by a class that implements IEventDispatcher; it is necessary so that the containing object can be the target for events. Do not use this parameter in simple cases in which a class extends EventDispatcher.
   *
   */
  "public function EventDispatcher",function EventDispatcher$(target/*:IEventDispatcher = null*/) {switch(arguments.length){case 0:target = null;}
    this.target$1 = target;
    this.captureListeners$1 = {};
    this.listeners$1 = {};
  },

  /**
   * Registers an event listener object with an EventDispatcher object so that the listener receives notification of an event. You can register event listeners on all nodes in the display list for a specific type of event, phase, and priority.
   * <p>After you successfully register an event listener, you cannot change its priority through additional calls to <code>addEventListener()</code>. To change a listener's priority, you must first call <code>removeListener()</code>. Then you can register the listener again with the new priority level.</p>
   * <p>Keep in mind that after the listener is registered, subsequent calls to <code>addEventListener()</code> with a different <code>type</code> or <code>useCapture</code> value result in the creation of a separate listener registration. For example, if you first register a listener with <code>useCapture</code> set to <code>true</code>, it listens only during the capture phase. If you call <code>addEventListener()</code> again using the same listener object, but with <code>useCapture</code> set to <code>false</code>, you have two separate listeners: one that listens during the capture phase and another that listens during the target and bubbling phases.</p>
   * <p>You cannot register an event listener for only the target phase or the bubbling phase. Those phases are coupled during registration because bubbling applies only to the ancestors of the target node.</p>
   * <p>If you no longer need an event listener, remove it by calling <code>removeEventListener()</code>, or memory problems could result. Event listeners are not automatically removed from memory because the garbage collector does not remove the listener as long as the dispatching object exists (unless the <code>useWeakReference</code> parameter is set to <code>true</code>).</p>
   * <p>Copying an EventDispatcher instance does not copy the event listeners attached to it. (If your newly created node needs an event listener, you must attach the listener after creating the node.) However, if you move an EventDispatcher instance, the event listeners attached to it move along with it.</p>
   * <p>If the event listener is being registered on a node while an event is being processed on this node, the event listener is not triggered during the current phase but can be triggered during a later phase in the event flow, such as the bubbling phase.</p>
   * <p>If an event listener is removed from a node while an event is being processed on the node, it is still triggered by the current actions. After it is removed, the event listener is never invoked again (unless registered again for future processing).</p>
   * @param type The type of event.
   * @param listener The listener function that processes the event. This function must accept an Event object as its only parameter and must return nothing, as this example shows:
   * <listing>
   * function(evt:Event):void</listing>
   * <p>The function can have any name.</p>
   * @param useCapture Determines whether the listener works in the capture phase or the target and bubbling phases. If <code>useCapture</code> is set to <code>true</code>, the listener processes the event only during the capture phase and not in the target or bubbling phase. If <code>useCapture</code> is <code>false</code>, the listener processes the event only during the target or bubbling phase. To listen for the event in all three phases, call <code>addEventListener</code> twice, once with <code>useCapture</code> set to <code>true</code>, then again with <code>useCapture</code> set to <code>false</code>.
   * @param priority The priority level of the event listener. The priority is designated by a signed 32-bit integer. The higher the number, the higher the priority. All listeners with priority <i>n</i> are processed before listeners of priority <i>n</i>-1. If two or more listeners share the same priority, they are processed in the order in which they were added. The default priority is 0.
   * @param useWeakReference Determines whether the reference to the listener is strong or weak. A strong reference (the default) prevents your listener from being garbage-collected. A weak reference does not.
   * <p>Class-level member functions are not subject to garbage collection, so you can set <code>useWeakReference</code> to <code>true</code> for class-level member functions without subjecting them to garbage collection. If you set <code>useWeakReference</code> to <code>true</code> for a listener that is a nested inner function, the function will be garbage-collected and no longer persistent. If you create references to the inner function (save it in another variable) then it is not garbage-collected and stays persistent.</p>
   *
   * @throws ArgumentError The <code>listener</code> specified is not a function.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e54.html Event listeners
   *
   */
  "public function addEventListener",function addEventListener(type/*:String*/, listener/*:Function*/, useCapture/*:Boolean = false*/, priority/*:int = 0*/, useWeakReference/*:Boolean = false*/)/*:void*/ {switch(arguments.length){case 0:case 1:case 2:useCapture = false;case 3:priority = 0;case 4:useWeakReference = false;}
    var listenersByType/*:Object*/ = useCapture ? this.captureListeners$1 : this.listeners$1;
    if (!(type in listenersByType)) {
      listenersByType[type] = [ listener ];
    } else {
      listenersByType[type].push(listener);
    }
  },

  /**
   * Dispatches an event into the event flow. The event target is the EventDispatcher object upon which the <code>dispatchEvent()</code> method is called.
   * @param event The Event object that is dispatched into the event flow. If the event is being redispatched, a clone of the event is created automatically. After an event is dispatched, its <code>target</code> property cannot be changed, so you must create a new copy of the event for redispatching to work.
   *
   * @return A value of <code>true</code> if the event was successfully dispatched. A value of <code>false</code> indicates failure or that <code>preventDefault()</code> was called on the event.
   *
   * @throws Error The event dispatch recursion limit has been reached.
   *
   */
  "public function dispatchEvent",function dispatchEvent(event/*:Event*/)/*:Boolean*/ {
    event.withTarget(this.target$1 || this);
    var listeners/*:Array*/ = this.listeners$1[event.type];
    if (listeners) {
      for (var i/*:int*/ = 0; i < listeners.length; ++i) {
        if (listeners[i](event) === false) {
          event.stopPropagation();
          event.preventDefault();
        }
        if (event.isImmediatePropagationStopped()) {
          break;
        }
      }
    }
    return !event.isDefaultPrevented();
  },

  /**
   * Checks whether the EventDispatcher object has any listeners registered for a specific type of event. This allows you to determine where an EventDispatcher object has altered handling of an event type in the event flow hierarchy. To determine whether a specific event type actually triggers an event listener, use <code>willTrigger()</code>.
   * <p>The difference between <code>hasEventListener()</code> and <code>willTrigger()</code> is that <code>hasEventListener()</code> examines only the object to which it belongs, whereas <code>willTrigger()</code> examines the entire event flow for the event specified by the <code>type</code> parameter.</p>
   * <p>When <code>hasEventListener()</code> is called from a LoaderInfo object, only the listeners that the caller can access are considered.</p>
   * @param type The type of event.
   *
   * @return A value of <code>true</code> if a listener of the specified type is registered; <code>false</code> otherwise.
   *
   * @see #willTrigger()
   *
   */
  "public function hasEventListener",function hasEventListener(type/*:String*/)/*:Boolean*/ {
    return this.listeners$1[type] || this.captureListeners$1[type];
  },

  /**
   * Removes a listener from the EventDispatcher object. If there is no matching listener registered with the EventDispatcher object, a call to this method has no effect.
   * @param type The type of event.
   * @param listener The listener object to remove.
   * @param useCapture Specifies whether the listener was registered for the capture phase or the target and bubbling phases. If the listener was registered for both the capture phase and the target and bubbling phases, two calls to <code>removeEventListener()</code> are required to remove both, one call with <code>useCapture()</code> set to <code>true</code>, and another call with <code>useCapture()</code> set to <code>false</code>.
   *
   * @see http://help.adobe.com/en_US/as3/dev/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e54.html Event listeners
   *
   */
  "public function removeEventListener",function removeEventListener(type/*:String*/, listener/*:Function*/, useCapture/*:Boolean = false*/)/*:void*/ {switch(arguments.length){case 0:case 1:case 2:useCapture = false;}
    var listenersByType/*:Object*/ = useCapture ? this.captureListeners$1 : this.listeners$1;
    var listeners/*:Array*/ = listenersByType[type];
    if (listeners) {
      for (var i/*:int*/ = 0; i < listeners.length; ++i) {
        if (listeners[i] == listener) {
          if (listeners.length == 1) {
            delete listenersByType[type];
          } else {
            listeners.splice(i, 1);
          }
          return;
        }
      }
    }
  },

  /**
   * Checks whether an event listener is registered with this EventDispatcher object or any of its ancestors for the specified event type. This method returns <code>true</code> if an event listener is triggered during any phase of the event flow when an event of the specified type is dispatched to this EventDispatcher object or any of its descendants.
   * <p>The difference between the <code>hasEventListener()</code> and the <code>willTrigger()</code> methods is that <code>hasEventListener()</code> examines only the object to which it belongs, whereas the <code>willTrigger()</code> method examines the entire event flow for the event specified by the <code>type</code> parameter.</p>
   * <p>When <code>willTrigger()</code> is called from a LoaderInfo object, only the listeners that the caller can access are considered.</p>
   * @param type The type of event.
   *
   * @return A value of <code>true</code> if a listener of the specified type will be triggered; <code>false</code> otherwise.
   *
   */
  "public function willTrigger",function willTrigger(type/*:String*/)/*:Boolean*/ {
    return this.hasEventListener(type);
  },

  "public function toString",function toString()/*:String*/ {
    return ["EventDispatcher[target=",this.target$1,"]"].join("");
  },

  "private var",{ captureListeners/*:Object*/:null}/*<String,Array>*/,
  "private var",{ listeners/*:Object*/:null}/*<String,Array>*/,
  "private var",{ target/*:IEventDispatcher*/:null},
];},[],["flash.events.IEventDispatcher"], "0.8.0", "0.8.2-SNAPSHOT"
);