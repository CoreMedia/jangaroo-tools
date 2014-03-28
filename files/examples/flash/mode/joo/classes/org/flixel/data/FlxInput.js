joo.classLoader.prepare("package org.flixel.data",/*
{
	import flash.events.KeyboardEvent*/
	
	"public class FlxInput",1,function($$private){;return[
	
		/**
		 * @private
		 */
		"internal var",{ _lookup/*:Object*/:null},
		/**
		 * @private
		 */
		"internal var",{ _map/*:Array*/:null},
		/**
		 * @private
		 */
		"internal const",{ _t/*:uint*/ : 256},
		
		/**
		 * Constructor
		 */
		"public function FlxInput",function FlxInput$()
		{
			this._lookup = new Object();
			this._map = new Array(this._t);
		},
		
		/**
		 * Updates the key states (for tracking just pressed, just released, etc).
		 */
		"public function update",function update()/*:void*/
		{
			var i/*:uint*/ = 0;
			while(i < this._t)
			{
				var o/*:Object*/ = this._map[i++];
				if(o == null) continue;
				if((o.last == -1) && (o.current == -1)) o.current = 0;
				else if((o.last == 2) && (o.current == 2)) o.current = 1;
				o.last = o.current;
			}
		},
		
		/**
		 * Resets all the keys.
		 */
		"public function reset",function reset()/*:void*/
		{
			var i/*:uint*/ = 0;
			while(i < this._t)
			{
				var o/*:Object*/ = this._map[i++];
				if(o == null) continue;
				this[o.name] = false;
				o.current = 0;
				o.last = 0;
			}
		},
		
		/**
		 * Check to see if this key is pressed.
		 * 
		 * @param	Key		One of the key constants listed above (e.g. "LEFT" or "A").
		 * 
		 * @return	Whether the key is pressed
		 */
		"public function pressed",function pressed(Key/*:String*/)/*:Boolean*/ { return this[Key]; },
		
		/**
		 * Check to see if this key was just pressed.
		 * 
		 * @param	Key		One of the key constants listed above (e.g. "LEFT" or "A").
		 * 
		 * @return	Whether the key was just pressed
		 */
		"public function justPressed",function justPressed(Key/*:String*/)/*:Boolean*/ { return this._map[this._lookup[Key]].current == 2; },
		
		/**
		 * Check to see if this key is just released.
		 * 
		 * @param	Key		One of the key constants listed above (e.g. "LEFT" or "A").
		 * 
		 * @return	Whether the key is just released.
		 */
		"public function justReleased",function justReleased(Key/*:String*/)/*:Boolean*/ { return this._map[this._lookup[Key]].current == -1; },
		
		/**
		 * Event handler so FlxGame can toggle keys.
		 * 
		 * @param	event	A <code>KeyboardEvent</code> object.
		 */
		"public function handleKeyDown",function handleKeyDown(event/*:KeyboardEvent*/)/*:void*/
		{
			var o/*:Object*/ = this._map[event.keyCode];
			if(o == null) return;
			if(o.current > 0) o.current = 1;
			else o.current = 2;
			this[o.name] = true;
		},
		
		/**
		 * Event handler so FlxGame can toggle keys.
		 * 
		 * @param	event	A <code>KeyboardEvent</code> object.
		 */
		"public function handleKeyUp",function handleKeyUp(event/*:KeyboardEvent*/)/*:void*/
		{
			var o/*:Object*/ = this._map[event.keyCode];
			if(o == null) return;
			if(o.current > 0) o.current = -1;
			else o.current = 0;
			this[o.name] = false;
		},
		
		/**
		 * An internal helper function used to build the key array.
		 * 
		 * @param	KeyName		String name of the key (e.g. "LEFT" or "A")
		 * @param	KeyCode		The numeric Flash code for this key.
		 */
		"internal function addKey",function addKey(KeyName/*:String*/,KeyCode/*:uint*/)/*:void*/
		{
			this._lookup[KeyName] = KeyCode;
			this._map[KeyCode] = { name: KeyName, current: 0, last: 0 };
		},
	];},[],["Object","Array"], "0.8.0", "0.8.2-SNAPSHOT"
);