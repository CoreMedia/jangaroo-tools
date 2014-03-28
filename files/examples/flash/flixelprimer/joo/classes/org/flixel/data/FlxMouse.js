joo.classLoader.prepare("package org.flixel.data",/*
{
	import flash.events.MouseEvent
	
	import org.flixel.FlxPoint
	import org.flixel.FlxSprite
	import org.flixel.FlxU*/
	
	/**
	 * This class helps contain and track the mouse pointer in your game.
	 * Automatically accounts for parallax scrolling, etc.
	 */
	"public class FlxMouse",1,function($$private){;return[
	
		{Embed:{source:"org/flixel/data/cursor.png"}}, "protected var",{ ImgDefaultCursor/*:Class*/:null},
		
		/**
		 * Current X position of the mouse pointer in the game world.
		 */
		"public var",{ x/*:int*/:0},
		/**
		 * Current Y position of the mouse pointer in the game world.
		 */
		"public var",{ y/*:int*/:0},
		/**
		 * Current "delta" value of mouse wheel.  If the wheel was just scrolled up, it will have a positive value.  If it was just scrolled down, it will have a negative value.  If it wasn't just scroll this frame, it will be 0.
		 */
		"public var",{ wheel/*:int*/:0},
		/**
		 * Current X position of the mouse pointer on the screen.
		 */
		"public var",{ screenX/*:int*/:0},
		/**
		 * Current Y position of the mouse pointer on the screen.
		 */
		"public var",{ screenY/*:int*/:0},
		/**
		 * Graphical representation of the mouse pointer.
		 */
		"public var",{ cursor/*:FlxSprite*/:null},
		/**
		 * Helper variable for tracking whether the mouse was just pressed or just released.
		 */
		"protected var",{ _current/*:int*/:0},
		/**
		 * Helper variable for tracking whether the mouse was just pressed or just released.
		 */
		"protected var",{ _last/*:int*/:0},
		/**
		 * Helper for mouse visibility.
		 */
		"protected var",{ _out/*:Boolean*/:false},
		
		/**
		 * Constructor.
		 */
		"public function FlxMouse",function FlxMouse$()
		{
			this.x = 0;
			this.y = 0;
			this.screenX = 0;
			this.screenY = 0;
			this._current = 0;
			this._last = 0;
			this.cursor = null;
			this._out = false;
		},
		
		/**
		 * Either show an existing cursor or load a new one.
		 * 
		 * @param	Graphic		The image you want to use for the cursor.
		 * @param	XOffset		The number of pixels between the mouse's screen position and the graphic's top left corner.
		 * * @param	YOffset		The number of pixels between the mouse's screen position and the graphic's top left corner. 
		 */
		"public function show",function show(Graphic/*:Class=null*/,XOffset/*:int=0*/,YOffset/*:int=0*/)/*:void*/
		{if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){Graphic=null;}XOffset=0;}YOffset=0;}
			this._out = true;
			if(Graphic != null)
				this.load(Graphic,XOffset,YOffset);
			else if(this.cursor != null)
				this.cursor.visible = true;
			else
				this.load(null);
		},
		
		/**
		 * Hides the mouse cursor
		 */
		"public function hide",function hide()/*:void*/
		{
			if(this.cursor != null)
			{
				this.cursor.visible = false;
				this._out = false;
			}
		},
		
		/**
		 * Load a new mouse cursor graphic
		 * 
		 * @param	Graphic		The image you want to use for the cursor.
		 * @param	XOffset		The number of pixels between the mouse's screen position and the graphic's top left corner.
		 * * @param	YOffset		The number of pixels between the mouse's screen position and the graphic's top left corner. 
		 */
		"public function load",function load(Graphic/*:Class*/,XOffset/*:int=0*/,YOffset/*:int=0*/)/*:void*/
		{if(arguments.length<3){if(arguments.length<2){XOffset=0;}YOffset=0;}
			if(Graphic == null)
				Graphic = this.ImgDefaultCursor;
			this.cursor = new org.flixel.FlxSprite(this.screenX,this.screenY,Graphic);
			this.cursor.solid = false;
			this.cursor.offset.x = XOffset;
			this.cursor.offset.y = YOffset;
		},
		
		/**
		 * Unload the current cursor graphic.  If the current cursor is visible,
		 * then the default system cursor is loaded up to replace the old one.
		 */
		"public function unload",function unload()/*:void*/
		{
			if(this.cursor != null)
			{
				if(this.cursor.visible)
					this.load(null);
				else
					this.cursor = null;
			}
		},

		/**
		 * Called by the internal game loop to update the mouse pointer's position in the game world.
		 * Also updates the just pressed/just released flags.
		 * 
		 * @param	X			The current X position of the mouse in the window.
		 * @param	Y			The current Y position of the mouse in the window.
		 * @param	XScroll		The amount the game world has scrolled horizontally.
		 * @param	YScroll		The amount the game world has scrolled vertically.
		 */
		"public function update",function update(X/*:int*/,Y/*:int*/,XScroll/*:Number*/,YScroll/*:Number*/)/*:void*/
		{
			this.screenX = X;
			this.screenY = Y;
			this.x = this.screenX-org.flixel.FlxU.floor(XScroll);
			this.y = this.screenY-org.flixel.FlxU.floor(YScroll);
			if(this.cursor != null)
			{
				this.cursor.x = this.x;
				this.cursor.y = this.y;
			}
			if((this._last == -1) && (this._current == -1))
				this._current = 0;
			else if((this._last == 2) && (this._current == 2))
				this._current = 1;
			this._last = this._current;
		},
		
		/**
		 * Resets the just pressed/just released flags and sets mouse to not pressed.
		 */
		"public function reset",function reset()/*:void*/
		{
			this._current = 0;
			this._last = 0;
		},
		
		/**
		 * Check to see if the mouse is pressed.
		 * 
		 * @return	Whether the mouse is pressed.
		 */
		"public function pressed",function pressed()/*:Boolean*/ { return this._current > 0; },
		
		/**
		 * Check to see if the mouse was just pressed.
		 * 
		 * @return Whether the mouse was just pressed.
		 */
		"public function justPressed",function justPressed()/*:Boolean*/ { return this._current == 2; },
		
		/**
		 * Check to see if the mouse was just released.
		 * 
		 * @return	Whether the mouse was just released.
		 */
		"public function justReleased",function justReleased()/*:Boolean*/ { return this._current == -1; },
		
		/**
		 * Event handler so FlxGame can update the mouse.
		 * 
		 * @param	event	A <code>MouseEvent</code> object.
		 */
		"public function handleMouseDown",function handleMouseDown(event/*:MouseEvent*/)/*:void*/
		{
			if(this._current > 0) this._current = 1;
			else this._current = 2;
		},
		
		/**
		 * Event handler so FlxGame can update the mouse.
		 * 
		 * @param	event	A <code>MouseEvent</code> object.
		 */
		"public function handleMouseUp",function handleMouseUp(event/*:MouseEvent*/)/*:void*/
		{
			if(this._current > 0) this._current = -1;
			else this._current = 0;
		},
		
		/**
		 * Event handler so FlxGame can update the mouse.
		 * 
		 * @param	event	A <code>MouseEvent</code> object.
		 */
		"public function handleMouseOut",function handleMouseOut(event/*:MouseEvent*/)/*:void*/
		{
			if(this.cursor != null)
			{
				this._out = this.cursor.visible;
				this.cursor.visible = false;
			}
		},
		
		/**
		 * Event handler so FlxGame can update the mouse.
		 * 
		 * @param	event	A <code>MouseEvent</code> object.
		 */
		"public function handleMouseOver",function handleMouseOver(event/*:MouseEvent*/)/*:void*/
		{
			if(this.cursor != null)
				this.cursor.visible = this._out;
		},
		
		/**
		 * Event handler so FlxGame can update the mouse.
		 * 
		 * @param	event	A <code>MouseEvent</code> object.
		 */
		"public function handleMouseWheel",function handleMouseWheel(event/*:MouseEvent*/)/*:void*/
		{
			this.wheel = event.delta;
		},
	];},[],["resource:org/flixel/data/cursor.png","org.flixel.FlxSprite","org.flixel.FlxU"], "0.8.0", "0.8.3"
);