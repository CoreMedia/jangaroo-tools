joo.classLoader.prepare("package org.flixel",/*
{
	import flash.display.Bitmap
	import flash.display.BitmapData
	import flash.display.Sprite
	import flash.display.StageAlign
	import flash.display.StageScaleMode
	import flash.events.*
	import flash.geom.Point
	import flash.text.AntiAliasType
	import flash.text.GridFitType
	import flash.text.TextField
	import flash.text.TextFormat
	import flash.ui.Mouse
	import flash.utils.Timer
	import flash.utils.getTimer
	
	import org.flixel.data.FlxConsole
	import org.flixel.data.FlxMouse
	import org.flixel.data.FlxPause*/

	/**
	 * FlxGame is the heart of all flixel games, and contains a bunch of basic game loops and things.
	 * It is a long and sloppy file that you shouldn't have to worry about too much!
	 * It is basically only used to create your game object in the first place,
	 * after that FlxG and FlxState have all the useful stuff you actually need.
	 */
	"public class FlxGame extends flash.display.Sprite",6,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.display.StageScaleMode,flash.events.KeyboardEvent,flash.text.AntiAliasType,flash.events.MouseEvent,flash.display.StageAlign,org.flixel.FlxG,flash.text.GridFitType,flash.events.Event,org.flixel.FlxState);},
	
		// NOTE: Flex 4 introduces DefineFont4, which is used by default and does not work in native text fields.
		// Use the embedAsCFF="false" param to switch back to DefineFont4. In earlier Flex 4 SDKs this was cff="false".
		// So if you are using the Flex 3.x SDK compiler, switch the embed statment below to expose the correct version.
		
		//Flex v4.x SDK only (see note above):
		{Embed:{source:"org/flixel/data/nokiafc22.ttf",fontName:"system",embedAsCFF:"false"}}, "protected var",{ junk/*:String*/:null},
		
		//Flex v3.x SDK only (see note above):
		//[Embed(source="data/nokiafc22.ttf",fontFamily="system")] protected var junk:String;
		
		{Embed:{source:"org/flixel/data/beep.mp3"}}, "protected var",{ SndBeep/*:Class*/:null},
		{Embed:{source:"org/flixel/data/flixel.mp3"}}, "protected var",{ SndFlixel/*:Class*/:null},

		/**
		 * Sets 0, -, and + to control the global volume and P to pause.
		 * @default true
		 */
		"public var",{ useDefaultHotKeys/*:Boolean*/:false},
		/**
		 * Displayed whenever the game is paused.
		 * Override with your own <code>FlxLayer</code> for hot custom pause action!
		 * Defaults to <code>data.FlxPause</code>.
		 */
		"public var",{ pause/*:FlxGroup*/:null},
		
		//startup
		"internal var",{ _iState/*:Class*/:null},
		"internal var",{ _created/*:Boolean*/:false},
		
		//basic display stuff
		"internal var",{ _state/*:FlxState*/:null},
		"internal var",{ _screen/*:Sprite*/:null},
		"internal var",{ _buffer/*:Bitmap*/:null},
		"internal var",{ _zoom/*:uint*/:0},
		"internal var",{ _gameXOffset/*:int*/:0},
		"internal var",{ _gameYOffset/*:int*/:0},
		"internal var",{ _frame/*:Class*/:null},
		"internal var",{ _zeroPoint/*:Point*/:null},
		
		//basic update stuff
		"internal var",{ _elapsed/*:Number*/:NaN},
		"internal var",{ _total/*:uint*/:0},
		"internal var",{ _paused/*:Boolean*/:false},
		"internal var",{ _framerate/*:uint*/:0},
		"internal var",{ _frameratePaused/*:uint*/:0},
		
		//Pause screen, sound tray, support panel, dev console, and special effects objects
		"internal var",{ _soundTray/*:Sprite*/:null},
		"internal var",{ _soundTrayTimer/*:Number*/:NaN},
		"internal var",{ _soundTrayBars/*:Array*/:null},
		"internal var",{ _console/*:FlxConsole*/:null},
		
		/**
		 * Game object constructor - sets up the basic properties of your game.
		 * 
		 * @param	GameSizeX		The width of your game in pixels (e.g. 320).
		 * @param	GameSizeY		The height of your game in pixels (e.g. 240).
		 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
		 * @param	Zoom			The level of zoom (e.g. 2 means all pixels are now rendered twice as big).
		 */
		"public function FlxGame",function FlxGame$(GameSizeX/*:uint*/,GameSizeY/*:uint*/,InitialState/*:Class*/,Zoom/*:uint=2*/)
		{if(arguments.length<4){Zoom=2;}this.super$6();
			flash.ui.Mouse.hide();
			
			this._zoom = Zoom;
			org.flixel.FlxState.bgColor = 0xff000000;
			org.flixel.FlxG.setGameData(this,GameSizeX,GameSizeY,Zoom);
			this._elapsed = 0;
			this._total = 0;
			this.pause = new org.flixel.data.FlxPause();
			this._state = null;
			this._iState = InitialState;
			this._zeroPoint = new flash.geom.Point();

			this.useDefaultHotKeys = true;
			
			this._frame = null;
			this._gameXOffset = 0;
			this._gameYOffset = 0;
			
			this._paused = false;
			this._created = false;
			
			this.addEventListener(flash.events.Event.ENTER_FRAME, $$bound(this,"create"));
		},
		
		/**
		 * Adds a frame around your game for presentation purposes (see Canabalt, Gravity Hook).
		 * 
		 * @param	Frame			If you want you can add a little graphical frame to the outside edges of your game.
		 * @param	ScreenOffsetX	Width in pixels of left side of frame.
		 * @param	ScreenOffsetY	Height in pixels of top of frame.
		 * 
		 * @return	This <code>FlxGame</code> instance.
		 */
		"protected function addFrame",function addFrame(Frame/*:Class*/,ScreenOffsetX/*:uint*/,ScreenOffsetY/*:uint*/)/*:FlxGame*/
		{
			this._frame = Frame;
			this._gameXOffset = ScreenOffsetX;
			this._gameYOffset = ScreenOffsetY;
			return this;
		},
		
		/**
		 * Makes the little volume tray slide out.
		 * 
		 * @param	Silent	Whether or not it should beep.
		 */
		"public function showSoundTray",function showSoundTray(Silent/*:Boolean=false*/)/*:void*/
		{if(arguments.length<1){Silent=false;}
			if(!Silent)
				org.flixel.FlxG.play(this.SndBeep);
			this._soundTrayTimer = 1;
			this._soundTray.y = this._gameYOffset*this._zoom;
			this._soundTray.visible = true;
			var gv/*:uint*/ = Math.round(org.flixel.FlxG.volume*10);
			if(org.flixel.FlxG.mute)
				gv = 0;
			for (var i/*:uint*/ = 0; i < this._soundTrayBars.length; i++)
			{
				if(i < gv) this._soundTrayBars[i].alpha = 1;
				else this._soundTrayBars[i].alpha = 0.5;
			}
		},
		
		/**
		 * Switch from one <code>FlxState</code> to another.
		 * Usually called from <code>FlxG</code>.
		 * 
		 * @param	State		The class name of the state you want (e.g. PlayState)
		 */
		"public function switchState",function switchState(State/*:FlxState*/)/*:void*/
		{ 
			//Basic reset stuff
			org.flixel.FlxG.panel.hide();
			org.flixel.FlxG.unfollow();
			org.flixel.FlxG.resetInput();
			org.flixel.FlxG.destroySounds();
			org.flixel.FlxG.flash.stop();
			org.flixel.FlxG.fade.stop();
			org.flixel.FlxG.quake.stop();
			this._screen.x = 0;
			this._screen.y = 0;
			
			//Swap the new state for the old one and dispose of it
			this._screen.addChild(State);
			if(this._state != null)
			{
				this._state.destroy(); //important that it is destroyed while still in the display list
				this._screen.swapChildren(State,this._state);
				this._screen.removeChild(this._state);
			}
			this._state = State;
			this._state.scaleX = this._state.scaleY = this._zoom;
			
			//Finally, create the new state
			this._state.create();
		},

		/**
		 * Internal event handler for input and focus.
		 */
		"protected function onKeyUp",function onKeyUp(event/*:KeyboardEvent*/)/*:void*/
		{
			if((event.keyCode == 192) || (event.keyCode == 220)) //FOR ZE GERMANZ
			{
				this._console.toggle();
				return;
			}
			if(!org.flixel.FlxG.mobile && this.useDefaultHotKeys)
			{
				var c/*:int*/ = event.keyCode;
				var code/*:String*/ = String.fromCharCode(event.charCode);
				switch(c)
				{
					case 48:
					case 96:
						org.flixel.FlxG.mute = !org.flixel.FlxG.mute;
						this.showSoundTray();
						return;
					case 109:
					case 189:
						org.flixel.FlxG.mute = false;
			    		org.flixel.FlxG.volume = org.flixel.FlxG.volume - 0.1;
			    		this.showSoundTray();
						return;
					case 107:
					case 187:
						org.flixel.FlxG.mute = false;
			    		org.flixel.FlxG.volume = org.flixel.FlxG.volume + 0.1;
			    		this.showSoundTray();
						return;
					case 80:
						org.flixel.FlxG.pause = !org.flixel.FlxG.pause;
					default: break;
				}
			}
			org.flixel.FlxG.keys.handleKeyUp(event);
			var i/*:uint*/ = 0;
			var l/*:uint*/ = org.flixel.FlxG.gamepads.length;
			while(i < l)
				org.flixel.FlxG.gamepads[i++].handleKeyUp(event);
		},
		
		/**
		 * Internal event handler for input and focus.
		 */
		"protected function onKeyDown",function onKeyDown(event/*:KeyboardEvent*/)/*:void*/
		{
			org.flixel.FlxG.keys.handleKeyDown(event);
			var i/*:uint*/ = 0;
			var l/*:uint*/ = org.flixel.FlxG.gamepads.length;
			while(i < l)
				org.flixel.FlxG.gamepads[i++].handleKeyDown(event);
		},
		
		/**
		 * Internal event handler for input and focus.
		 */
		"protected function onFocus",function onFocus(event/*:Event=null*/)/*:void*/
		{if(arguments.length<1){event=null;}
			if(org.flixel.FlxG.pause)
				org.flixel.FlxG.pause = false;
		},
		
		/**
		 * Internal event handler for input and focus.
		 */
		"protected function onFocusLost",function onFocusLost(event/*:Event=null*/)/*:void*/
		{if(arguments.length<1){event=null;}
			org.flixel.FlxG.pause = true;
		},
		
		/**
		 * Internal function to help with basic pause game functionality.
		 */
		"internal function unpauseGame",function unpauseGame()/*:void*/
		{
			if(!org.flixel.FlxG.panel.visible) flash.ui.Mouse.hide();
			org.flixel.FlxG.resetInput();
			this._paused = false;
			this.stage.frameRate = this._framerate;
		},
		
		/**
		 * Internal function to help with basic pause game functionality.
		 */
		"internal function pauseGame",function pauseGame()/*:void*/
		{
			if((this.x != 0) || (this.y != 0))
			{
				this.x = 0;
				this.y = 0;
			}
			flash.ui.Mouse.show();
			this._paused = true;
			this.stage.frameRate = this._frameratePaused;
		},
		
		/**
		 * This is the main game loop.  It controls all the updating and rendering.
		 */
		"protected function update",function update(event/*:Event*/)/*:void*/
		{
			var mark/*:uint*/ = flash.utils.getTimer();
			
			var i/*:uint*/;
			var soundPrefs/*:FlxSave*/;

			//Frame timing
			var ems/*:uint*/ = mark-this._total;
			this._elapsed = ems/1000;
			this._console.mtrTotal.add(ems);
			this._total = mark;
			org.flixel.FlxG.elapsed = this._elapsed;
			if(org.flixel.FlxG.elapsed > org.flixel.FlxG.maxElapsed)
				org.flixel.FlxG.elapsed = org.flixel.FlxG.maxElapsed;
			org.flixel.FlxG.elapsed *= org.flixel.FlxG.timeScale;
			
			//Sound tray crap
			if(this._soundTray != null)
			{
				if(this._soundTrayTimer > 0)
					this._soundTrayTimer -= this._elapsed;
				else if(this._soundTray.y > -this._soundTray.height)
				{
					this._soundTray.y -= this._elapsed*org.flixel.FlxG.height*2;
					if(this._soundTray.y <= -this._soundTray.height)
					{
						this._soundTray.visible = false;
						
						//Save sound preferences
						soundPrefs = new org.flixel.FlxSave();
						if(soundPrefs.bind("flixel"))
						{
							if(soundPrefs.data.sound == null)
								soundPrefs.data.sound = new Object;
							soundPrefs.data.mute = org.flixel.FlxG.mute;
							soundPrefs.data.volume = org.flixel.FlxG.volume;
							soundPrefs.forceSave();
						}
					}
				}
			}

			//Animate flixel HUD elements
			org.flixel.FlxG.panel.update();
			if(this._console.visible)
				this._console.update();
			
			//State updating
			org.flixel.FlxG.updateInput();
			org.flixel.FlxG.updateSounds();
			if(this._paused)
				this.pause.update();
			else
			{
				//Update the camera and game state
				org.flixel.FlxG.doFollow();
				this._state.update();
				
				//Update the various special effects
				if(org.flixel.FlxG.flash.exists)
					org.flixel.FlxG.flash.update();
				if(org.flixel.FlxG.fade.exists)
					org.flixel.FlxG.fade.update();
				org.flixel.FlxG.quake.update();
				this._screen.x = org.flixel.FlxG.quake.x;
				this._screen.y = org.flixel.FlxG.quake.y;
			}
			//Keep track of how long it took to update everything
			var updateMark/*:uint*/ = flash.utils.getTimer();
			this._console.mtrUpdate.add(updateMark-mark);
			
			//Render game content, special fx, and overlays
			org.flixel.FlxG.buffer.lock();
			this._state.preProcess();
			this._state.render();
			if(org.flixel.FlxG.flash.exists)
				org.flixel.FlxG.flash.render();
			if(org.flixel.FlxG.fade.exists)
				org.flixel.FlxG.fade.render();
			if(org.flixel.FlxG.panel.visible)
				org.flixel.FlxG.panel.render();
			if(org.flixel.FlxG.mouse.cursor != null)
			{
				if(org.flixel.FlxG.mouse.cursor.active)
					org.flixel.FlxG.mouse.cursor.update();
				if(org.flixel.FlxG.mouse.cursor.visible)
					org.flixel.FlxG.mouse.cursor.render();
			}
			this._state.postProcess();
			if(this._paused)
				this.pause.render();
			org.flixel.FlxG.buffer.unlock();
			//Keep track of how long it took to draw everything
			this._console.mtrRender.add(flash.utils.getTimer()-updateMark);
			//clear mouse wheel delta
			org.flixel.FlxG.mouse.wheel = 0;
		},
		
		/**
		 * Used to instantiate the guts of flixel once we have a valid pointer to the root.
		 */
		"internal function create",function create(event/*:Event*/)/*:void*/
		{
			if(this.root == null)
				return;

			var i/*:uint*/;
			var l/*:uint*/;
			var soundPrefs/*:FlxSave*/;
			
			//Set up the view window and double buffering
			this.stage.scaleMode = flash.display.StageScaleMode.NO_SCALE;
            this.stage.align = flash.display.StageAlign.TOP_LEFT;
            this.stage.frameRate = this._framerate;
            this._screen = new flash.display.Sprite();
            this.addChild(this._screen);
			var tmp/*:Bitmap*/ = new flash.display.Bitmap(new flash.display.BitmapData(org.flixel.FlxG.width,org.flixel.FlxG.height,true,org.flixel.FlxState.bgColor));
			tmp.x = this._gameXOffset;
			tmp.y = this._gameYOffset;
			tmp.scaleX = tmp.scaleY = this._zoom;
			this._screen.addChild(tmp);
			org.flixel.FlxG.buffer = tmp.bitmapData;
			
			//Initialize game console
			this._console = new org.flixel.data.FlxConsole(this._gameXOffset,this._gameYOffset,this._zoom);
			if(!org.flixel.FlxG.mobile)
				this.addChild(this._console);
			var vstring/*:String*/ = org.flixel.FlxG.LIBRARY_NAME+" v"+org.flixel.FlxG.LIBRARY_MAJOR_VERSION+"."+org.flixel.FlxG.LIBRARY_MINOR_VERSION;
			if(org.flixel.FlxG.debug)
				vstring += " [debug]";
			else
				vstring += " [release]";
			var underline/*:String*/ = "";
			i = 0;
			l = vstring.length+32;
			while(i < l)
			{
				underline += "-";
				i++;
			}
			org.flixel.FlxG.log(vstring);
			org.flixel.FlxG.log(underline);
			
			var flxMouse/*:FlxMouse*/ = org.flixel.FlxG.mouse;
			//Add basic input even listeners
			this.stage.addEventListener(flash.events.MouseEvent.MOUSE_DOWN,$$bound( flxMouse,"handleMouseDown"));
			this.stage.addEventListener(flash.events.MouseEvent.MOUSE_UP,$$bound( flxMouse,"handleMouseUp"));
			this.stage.addEventListener(flash.events.KeyboardEvent.KEY_DOWN, $$bound(this,"onKeyDown"));
			this.stage.addEventListener(flash.events.KeyboardEvent.KEY_UP, $$bound(this,"onKeyUp"));
			if(!org.flixel.FlxG.mobile)
			{
				this.stage.addEventListener(flash.events.MouseEvent.MOUSE_OUT,$$bound( flxMouse,"handleMouseOut"));
				this.stage.addEventListener(flash.events.MouseEvent.MOUSE_OVER,$$bound( flxMouse,"handleMouseOver"));
				this.stage.addEventListener(flash.events.MouseEvent.MOUSE_WHEEL,$$bound( flxMouse,"handleMouseWheel"));
				this.stage.addEventListener(flash.events.Event.DEACTIVATE, $$bound(this,"onFocusLost"));
				this.stage.addEventListener(flash.events.Event.ACTIVATE, $$bound(this,"onFocus"));
				
				//Sound Tray popup
				this._soundTray = new flash.display.Sprite();
				this._soundTray.visible = false;
				this._soundTray.scaleX = 2;
				this._soundTray.scaleY = 2;
				tmp = new flash.display.Bitmap(new flash.display.BitmapData(80,30,true,0x7F000000));
				this._soundTray.x = (this._gameXOffset+org.flixel.FlxG.width/2)*this._zoom-(tmp.width/2)*this._soundTray.scaleX;
				this._soundTray.addChild(tmp);
				
				var text/*:TextField*/ = new flash.text.TextField();
				text.width = tmp.width;
				text.height = tmp.height;
				text.multiline = true;
				text.wordWrap = true;
				text.selectable = false;
				text.embedFonts = true;
				text.antiAliasType = flash.text.AntiAliasType.NORMAL;
				text.gridFitType = flash.text.GridFitType.PIXEL;
				text.defaultTextFormat = new flash.text.TextFormat("system",8,0xffffff,null,null,null,null,null,"center");;
				this._soundTray.addChild(text);
				text.text = "VOLUME";
				text.y = 16;
				
				var bx/*:uint*/ = 10;
				var by/*:uint*/ = 14;
				this._soundTrayBars = new Array();
				i = 0;
				while(i < 10)
				{
					tmp = new flash.display.Bitmap(new flash.display.BitmapData(4,++i,false,0xffffff));
					tmp.x = bx;
					tmp.y = by;
					this._soundTrayBars.push(this._soundTray.addChild(tmp));
					bx += 6;
					by--;
				}
				this.addChild(this._soundTray);
				
				//Check for saved sound preference data
				soundPrefs = new org.flixel.FlxSave();
				if(soundPrefs.bind("flixel") && (soundPrefs.data.sound != null))
				{
					if(soundPrefs.data.volume != null)
						org.flixel.FlxG.volume = soundPrefs.data.volume;
					if(soundPrefs.data.mute != null)
						org.flixel.FlxG.mute = soundPrefs.data.mute;
					this.showSoundTray(true);
				}
			}

			//Initialize the decorative frame (optional)
			if(this._frame != null)
			{
				var bmp/*:Bitmap*/ = new this._frame();
				bmp.scaleX = this._zoom;
				bmp.scaleY = this._zoom;
				this.addChild(bmp);
			}
			
			//All set!
			this.switchState(new this._iState());
			org.flixel.FlxState.screen.unsafeBind(org.flixel.FlxG.buffer);
			this.removeEventListener(flash.events.Event.ENTER_FRAME, $$bound(this,"create"));
			this.addEventListener(flash.events.Event.ENTER_FRAME, $$bound(this,"update"));
		},
	];},[],["flash.display.Sprite","resource:org/flixel/data/nokiafc22.ttf","resource:org/flixel/data/beep.mp3","resource:org/flixel/data/flixel.mp3","flash.ui.Mouse","org.flixel.FlxState","org.flixel.FlxG","org.flixel.data.FlxPause","flash.geom.Point","flash.events.Event","Math","String","org.flixel.FlxSave","Object","flash.display.StageScaleMode","flash.display.StageAlign","flash.display.Bitmap","flash.display.BitmapData","org.flixel.data.FlxConsole","flash.events.MouseEvent","flash.events.KeyboardEvent","flash.text.TextField","flash.text.AntiAliasType","flash.text.GridFitType","flash.text.TextFormat","Array"], "0.8.0", "0.8.1"
);