joo.classLoader.prepare("package org.flixel",/*
{
	import flash.events.MouseEvent*/
	
	/**
	 * A simple button class that calls a function when clicked by the mouse.
	 * Supports labels, highlight states, and parallax scrolling.
	 */
	"public class FlxButton extends org.flixel.FlxGroup",5,function($$private){var as=joo.as,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.events.MouseEvent,org.flixel.FlxG);},
	
		/**
		 * Set this to true if you want this button to function even while the game is paused.
		 */
		"public var",{ pauseProof/*:Boolean*/:false},
		/**
		 * Used for checkbox-style behavior.
		 */
		"protected var",{ _onToggle/*:Boolean*/:false},
		/**
		 * Stores the 'off' or normal button state graphic.
		 */
		"protected var",{ _off/*:FlxSprite*/:null},
		/**
		 * Stores the 'on' or highlighted button state graphic.
		 */
		"protected var",{ _on/*:FlxSprite*/:null},
		/**
		 * Stores the 'off' or normal button state label.
		 */
		"protected var",{ _offT/*:FlxText*/:null},
		/**
		 * Stores the 'on' or highlighted button state label.
		 */
		"protected var",{ _onT/*:FlxText*/:null},
		/**
		 * This function is called when the button is clicked.
		 */
		"protected var",{ _callback/*:Function*/:null},
		/**
		 * Tracks whether or not the button is currently pressed.
		 */
		"protected var",{ _pressed/*:Boolean*/:false},
		/**
		 * Whether or not the button has initialized itself yet.
		 */
		"protected var",{ _initialized/*:Boolean*/:false},
		/**
		 * Helper variable for correcting its members' <code>scrollFactor</code> objects.
		 */
		"protected var",{ _sf/*:FlxPoint*/:null},
		
		/**
		 * Creates a new <code>FlxButton</code> object with a gray background
		 * and a callback function on the UI thread.
		 * 
		 * @param	X			The X position of the button.
		 * @param	Y			The Y position of the button.
		 * @param	Callback	The function to call whenever the button is clicked.
		 */
		"public function FlxButton",function FlxButton$(X/*:int*/,Y/*:int*/,Callback/*:Function*/)
		{
			this.super$5();
			this.x = X;
			this.y = Y;
			this.width = 100;
			this.height = 20;
			this._off = new org.flixel.FlxSprite().createGraphic(this.width,this.height,0xff7f7f7f);
			this._off.solid = false;
			this.add(this._off,true);
			this._on  = new org.flixel.FlxSprite().createGraphic(this.width,this.height,0xffffffff);
			this._on.solid = false;
			this.add(this._on,true);
			this._offT = null;
			this._onT = null;
			this._callback = Callback;
			this._onToggle = false;
			this._pressed = false;
			this._initialized = false;
			this._sf = null;
			this.pauseProof = false;
		},
		
		/**
		 * Set your own image as the button background.
		 * 
		 * @param	Image				A FlxSprite object to use for the button background.
		 * @param	ImageHighlight		A FlxSprite object to use for the button background when highlighted (optional).
		 * 
		 * @return	This FlxButton instance (nice for chaining stuff together, if you're into that).
		 */
		"public function loadGraphic",function loadGraphic(Image/*:FlxSprite*/,ImageHighlight/*:FlxSprite=null*/)/*:FlxButton*/
		{switch(arguments.length){case 0:case 1:ImageHighlight=null;}
			this._off =as( this.replace(this._off,Image),  org.flixel.FlxSprite);
			if(ImageHighlight == null)
			{
				if(this._on != this._off)
					this.remove(this._on);
				this._on = this._off;
			}
			else
				this._on =as( this.replace(this._on,ImageHighlight),  org.flixel.FlxSprite);
			this._on.solid = this._off.solid = false;
			this._off.scrollFactor = this.scrollFactor;
			this._on.scrollFactor = this.scrollFactor;
			this.width = this._off.width;
			this.height = this._off.height;
			this.refreshHulls();
			return this;
		},

		/**
		 * Add a text label to the button.
		 * 
		 * @param	Text				A FlxText object to use to display text on this button (optional).
		 * @param	TextHighlight		A FlxText object that is used when the button is highlighted (optional).
		 * 
		 * @return	This FlxButton instance (nice for chaining stuff together, if you're into that).
		 */
		"public function loadText",function loadText(Text/*:FlxText*/,TextHighlight/*:FlxText=null*/)/*:FlxButton*/
		{switch(arguments.length){case 0:case 1:TextHighlight=null;}
			if(Text != null)
			{
				if(this._offT == null)
				{
					this._offT = Text;
					this.add(this._offT);
				}
				else
					this._offT =as( this.replace(this._offT,Text),  org.flixel.FlxText);
			}
			if(TextHighlight == null)
				this._onT = this._offT;
			else
			{
				if(this._onT == null)
				{
					this._onT = TextHighlight;
					this.add(this._onT);
				}
				else
					this._onT =as( this.replace(this._onT,TextHighlight),  org.flixel.FlxText);
			}
			this._offT.scrollFactor = this.scrollFactor;
			this._onT.scrollFactor = this.scrollFactor;
			return this;
		},
		
		/**
		 * Called by the game loop automatically, handles mouseover and click detection.
		 */
		"override public function update",function update()/*:void*/
		{
			if(!this._initialized)
			{
				if(org.flixel.FlxG.stage != null)
				{
					org.flixel.FlxG.stage.addEventListener(flash.events.MouseEvent.MOUSE_UP, $$bound(this,"onMouseUp"));
					this._initialized = true;
				}
			}
			
			this.update$5();

			this.visibility(false);
			if(this.overlapsPoint(org.flixel.FlxG.mouse.x,org.flixel.FlxG.mouse.y))
			{
				if(!org.flixel.FlxG.mouse.pressed())
					this._pressed = false;
				else if(!this._pressed)
					this._pressed = true;
				this.visibility(!this._pressed);
			}
			if(this._onToggle) this.visibility(this._off.visible);
		},
		
		/**
		 * Use this to toggle checkbox-style behavior.
		 */
		"public function get on",function on$get()/*:Boolean*/
		{
			return this._onToggle;
		},
		
		/**
		 * @private
		 */
		"public function set on",function on$set(On/*:Boolean*/)/*:void*/
		{
			this._onToggle = On;
		},
		
		/**
		 * Called by the game state when state is changed (if this object belongs to the state)
		 */
		"override public function destroy",function destroy()/*:void*/
		{
			if(org.flixel.FlxG.stage != null)
				org.flixel.FlxG.stage.removeEventListener(flash.events.MouseEvent.MOUSE_UP, $$bound(this,"onMouseUp"));
		},
		
		/**
		 * Internal function for handling the visibility of the off and on graphics.
		 * 
		 * @param	On		Whether the button should be on or off.
		 */
		"protected function visibility",function visibility(On/*:Boolean*/)/*:void*/
		{
			if(On)
			{
				this._off.visible = false;
				if(this._offT != null) this._offT.visible = false;
				this._on.visible = true;
				if(this._onT != null) this._onT.visible = true;
			}
			else
			{
				this._on.visible = false;
				if(this._onT != null) this._onT.visible = false;
				this._off.visible = true;
				if(this._offT != null) this._offT.visible = true;
			}
		},
		
		/**
		 * Internal function for handling the actual callback call (for UI thread dependent calls like <code>FlxU.openURL()</code>).
		 */
		"protected function onMouseUp",function onMouseUp(event/*:MouseEvent*/)/*:void*/
		{
			if(!this.exists || !this.visible || !this.active || !org.flixel.FlxG.mouse.justReleased() || (org.flixel.FlxG.pause && !this.pauseProof) || (this._callback == null)) return;
			if(this.overlapsPoint(org.flixel.FlxG.mouse.x,org.flixel.FlxG.mouse.y)) this._callback();
		},
	];},[],["org.flixel.FlxGroup","org.flixel.FlxSprite","org.flixel.FlxText","org.flixel.FlxG","flash.events.MouseEvent"], "0.8.0", "0.8.2-SNAPSHOT"
);