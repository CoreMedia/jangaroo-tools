joo.classLoader.prepare("package org.flixel",/*
{
	import flash.display.BitmapData
	import flash.display.Stage
	import flash.geom.Matrix
	import flash.geom.Point
	
	import org.flixel.data.**/
	
	/**
	 * This is a global helper class full of useful functions for audio,
	 * input, basic info, and the camera system among other things.
	 */
	"public class FlxG",1,function($$private){var as=joo.as,is=joo.is;return[
	
		/**
		 * If you build and maintain your own version of flixel,
		 * you can give it your own name here.  Appears in the console.
		 */
		"static public var",{ LIBRARY_NAME/*:String*/ : "flixel"},
		/**
		 * Assign a major version to your library.
		 * Appears before the decimal in the console.
		 */
		"static public var",{ LIBRARY_MAJOR_VERSION/*:uint*/ : 2},
		/**
		 * Assign a minor version to your library.
		 * Appears after the decimal in the console.
		 */
		"static public var",{ LIBRARY_MINOR_VERSION/*:uint*/ : 43},

		/**
		 * Internal tracker for game object (so we can pause & unpause)
		 */
		"static protected var",{ _game/*:FlxGame*/:null},
		/**
		 * Internal tracker for game pause state.
		 */
		"static protected var",{ _pause/*:Boolean*/:false},
		/**
		 * Whether you are running in Debug or Release mode.
		 * Set automatically by <code>FlxFactory</code> during startup.
		 */
		"static public var",{ debug/*:Boolean*/:false},
		/**
		 * Set <code>showBounds</code> to true to display the bounding boxes of the in-game objects.
		 */
		"static public var",{ showBounds/*:Boolean*/:false},
		
		/**
		 * Represents the amount of time in seconds that passed since last frame.
		 */
		"static public var",{ elapsed/*:Number*/:NaN},
		/**
		 * Essentially locks the framerate to a minimum value - any slower and you'll get slowdown instead of frameskip; default is 1/30th of a second.
		 */
		"static public var",{ maxElapsed/*:Number*/:NaN},
		/**
		 * How fast or slow time should pass in the game; default is 1.0.
		 */
		"static public var",{ timeScale/*:Number*/:NaN},
		/**
		 * The width of the screen in game pixels.
		 */
		"static public var",{ width/*:uint*/:0},
		/**
		 * The height of the screen in game pixels.
		 */
		"static public var",{ height/*:uint*/:0},
		/**
		 * Setting this to true will disable/skip stuff that isn't necessary for mobile platforms like Android. [BETA]
		 */
		"static public var",{ mobile/*:Boolean*/:false}, 
		/**
		 * <code>FlxG.levels</code> and <code>FlxG.scores</code> are generic
		 * global variables that can be used for various cross-state stuff.
		 */
		"static public var",{ levels/*:Array*/:null},
		"static public var",{ level/*:int*/:0},
		"static public var",{ scores/*:Array*/:null},
		"static public var",{ score/*:int*/:0},
		/**
		 * <code>FlxG.saves</code> is a generic bucket for storing
		 * FlxSaves so you can access them whenever you want.
		 */
		"static public var",{ saves/*:Array*/:null}, 
		"static public var",{ save/*:int*/:0},

		/**
		 * A reference to a <code>FlxMouse</code> object.  Important for input!
		 */
		"static public var",{ mouse/*:FlxMouse*/:null},
		/**
		 * A reference to a <code>FlxKeyboard</code> object.  Important for input!
		 */
		"static public var",{ keys/*:FlxKeyboard*/:null},
		/**
		 * An array of <code>FlxGamepad</code> objects.  Important for input!
		 */
		"static public var",{ gamepads/*:Array*/:null},
		
		/**
		 * A handy container for a background music object.
		 */
		"static public var",{ music/*:FlxSound*/:null},
		/**
		 * A list of all the sounds being played in the game.
		 */
		"static public var",{ sounds/*:Array*/:null},
		/**
		 * Internal flag for whether or not the game is muted.
		 */
		"static protected var",{ _mute/*:Boolean*/:false},
		/**
		 * Internal volume level, used for global sound control.
		 */
		"static protected var",{ _volume/*:Number*/:NaN},
		
		/**
		 * Tells the camera to follow this <code>FlxCore</code> object around.
		 */
		"static public var",{ followTarget/*:FlxObject*/:null},
		/**
		 * Used to force the camera to look ahead of the <code>followTarget</code>.
		 */
		"static public var",{ followLead/*:Point*/:null},
		/**
		 * Used to smoothly track the camera as it follows.
		 */
		"static public var",{ followLerp/*:Number*/:NaN},
		/**
		 * Stores the top and left edges of the camera area.
		 */
		"static public var",{ followMin/*:Point*/:null},
		/**
		 * Stores the bottom and right edges of the camera area.
		 */
		"static public var",{ followMax/*:Point*/:null},
		/**
		 * Internal, used to assist camera and scrolling.
		 */
		"static protected var",{ _scrollTarget/*:Point*/:null},
		
		/**
		 * Stores the basic parallax scrolling values.
		 */
		"static public var",{ scroll/*:Point*/:null},
		/**
		 * Reference to the active graphics buffer.
		 * Can also be referenced via <code>FlxState.screen</code>.
		 */
		"static public var",{ buffer/*:BitmapData*/:null},
		/**
		 * Internal storage system to prevent graphics from being used repeatedly in memory.
		 */
		"static protected var",{ _cache/*:Object*/:null},
		
		/**
		 * Access to the Kongregate high scores and achievements API.
		 */
		"static public var",{ kong/*:FlxKong*/:null},
		
		/**
		 * The support panel (twitter, reddit, stumbleupon, paypal, etc) visor thing
		 */
		"static public var",{ panel/*:FlxPanel*/:null},
		/**
		 * A special effect that shakes the screen.  Usage: FlxG.quake.start();
		 */
		"static public var",{ quake/*:FlxQuake*/:null},
		/**
		 * A special effect that flashes a color on the screen.  Usage: FlxG.flash.start();
		 */
		"static public var",{ flash/*:FlxFlash*/:null},
		/**
		 * A special effect that fades a color onto the screen.  Usage: FlxG.fade.start();
		 */
		"static public var",{ fade/*:FlxFade*/:null},
		
		/**
		 * Log data to the developer console.
		 * 
		 * @param	Data		Anything you want to log to the console.
		 */
		"static public function log",function log(Data/*:Object*/)/*:void*/
		{
			if((org.flixel.FlxG._game != null) && (org.flixel.FlxG._game._console != null))
				org.flixel.FlxG._game._console.log((Data == null)?"ERROR: null object":Data.toString());
		},
		
		/**
		 * Set <code>pause</code> to true to pause the game, all sounds, and display the pause popup.
		 */
		"static public function get pause",function pause$get()/*:Boolean*/
		{
			return org.flixel.FlxG._pause;
		},
		
		/**
		 * @private
		 */
		"static public function set pause",function pause$set(Pause/*:Boolean*/)/*:void*/
		{
			var op/*:Boolean*/ = org.flixel.FlxG._pause;
			org.flixel.FlxG._pause = Pause;
			if(org.flixel.FlxG._pause != op)
			{
				if(org.flixel.FlxG._pause)
				{
					org.flixel.FlxG._game.pauseGame();
					org.flixel.FlxG.pauseSounds();
				}
				else
				{
					org.flixel.FlxG._game.unpauseGame();
					org.flixel.FlxG.playSounds();
				}
			}
		},
		
		/**
		 * The game and SWF framerate; default is 60.
		 */
		"static public function get framerate",function framerate$get()/*:uint*/
		{
			return org.flixel.FlxG._game._framerate;
		},
		
		/**
		 * @private
		 */
		"static public function set framerate",function framerate$set(Framerate/*:uint*/)/*:void*/
		{
			org.flixel.FlxG._game._framerate = Framerate;
			if(!org.flixel.FlxG._game._paused && (org.flixel.FlxG._game.stage != null))
				org.flixel.FlxG._game.stage.frameRate = Framerate;
		},
		
		/**
		 * The game and SWF framerate while paused; default is 10.
		 */
		"static public function get frameratePaused",function frameratePaused$get()/*:uint*/
		{
			return org.flixel.FlxG._game._frameratePaused;
		},
		
		/**
		 * @private
		 */
		"static public function set frameratePaused",function frameratePaused$set(Framerate/*:uint*/)/*:void*/
		{
			org.flixel.FlxG._game._frameratePaused = Framerate;
			if(org.flixel.FlxG._game._paused && (org.flixel.FlxG._game.stage != null))
				org.flixel.FlxG._game.stage.frameRate = Framerate;
		},
		
		/**
		 * Reset the input helper objects (useful when changing screens or states)
		 */
		"static public function resetInput",function resetInput()/*:void*/
		{
			org.flixel.FlxG.keys.reset();
			org.flixel.FlxG.mouse.reset();
			var i/*:uint*/ = 0;
			var l/*:uint*/ = org.flixel.FlxG.gamepads.length;
			while(i < l)
				org.flixel.FlxG.gamepads[i++].reset();
		},
		
		/**
		 * Set up and play a looping background soundtrack.
		 * 
		 * @param	Music		The sound file you want to loop in the background.
		 * @param	Volume		How loud the sound should be, from 0 to 1.
		 */
		"static public function playMusic",function playMusic(Music/*:Class*/,Volume/*:Number=1.0*/)/*:void*/
		{if(arguments.length<2){Volume=1.0;}
			if(org.flixel.FlxG.music == null)
				org.flixel.FlxG.music = new org.flixel.FlxSound();
			else if(org.flixel.FlxG.music.active)
				org.flixel.FlxG.music.stop();
			org.flixel.FlxG.music.loadEmbedded(Music,true);
			org.flixel.FlxG.music.volume = Volume;
			org.flixel.FlxG.music.survive = true;
			org.flixel.FlxG.music.play();
		},
		
		/**
		 * Creates a new sound object from an embedded <code>Class</code> object.
		 * 
		 * @param	EmbeddedSound	The sound you want to play.
		 * @param	Volume			How loud to play it (0 to 1).
		 * @param	Looped			Whether or not to loop this sound.
		 * 
		 * @return	A <code>FlxSound</code> object.
		 */
		"static public function play",function play(EmbeddedSound/*:Class*/,Volume/*:Number=1.0*/,Looped/*:Boolean=false*/)/*:FlxSound*/
		{if(arguments.length<3){if(arguments.length<2){Volume=1.0;}Looped=false;}
			var i/*:uint*/ = 0;
			var sl/*:uint*/ = org.flixel.FlxG.sounds.length;
			while(i < sl)
			{
				if(!(as(org.flixel.FlxG.sounds[i],  org.flixel.FlxSound)).active)
					break;
				i++;
			}
			if(org.flixel.FlxG.sounds[i] == null)
				org.flixel.FlxG.sounds[i] = new org.flixel.FlxSound();
			var s/*:FlxSound*/ = org.flixel.FlxG.sounds[i];
			s.loadEmbedded(EmbeddedSound,Looped);
			s.volume = Volume;
			s.play();
			return s;
		},
		
		/**
		 * Creates a new sound object from a URL.
		 * 
		 * @param	EmbeddedSound	The sound you want to play.
		 * @param	Volume			How loud to play it (0 to 1).
		 * @param	Looped			Whether or not to loop this sound.
		 * 
		 * @return	A FlxSound object.
		 */
		"static public function stream",function stream(URL/*:String*/,Volume/*:Number=1.0*/,Looped/*:Boolean=false*/)/*:FlxSound*/
		{if(arguments.length<3){if(arguments.length<2){Volume=1.0;}Looped=false;}
			var i/*:uint*/ = 0;
			var sl/*:uint*/ = org.flixel.FlxG.sounds.length;
			while(i < sl)
			{
				if(!(as(org.flixel.FlxG.sounds[i],  org.flixel.FlxSound)).active)
					break;
				i++;
			}
			if(org.flixel.FlxG.sounds[i] == null)
				org.flixel.FlxG.sounds[i] = new org.flixel.FlxSound();
			var s/*:FlxSound*/ = org.flixel.FlxG.sounds[i];
			s.loadStream(URL,Looped);
			s.volume = Volume;
			s.play();
			return s;
		},
		
		/**
		 * Set <code>mute</code> to true to turn off the sound.
		 * 
		 * @default false
		 */
		"static public function get mute",function mute$get()/*:Boolean*/
		{
			return org.flixel.FlxG._mute;
		},
		
		/**
		 * @private
		 */
		"static public function set mute",function mute$set(Mute/*:Boolean*/)/*:void*/
		{
			org.flixel.FlxG._mute = Mute;
			org.flixel.FlxG.changeSounds();
		},
		
		/**
		 * Get a number that represents the mute state that we can multiply into a sound transform.
		 * 
		 * @return		An unsigned integer - 0 if muted, 1 if not muted.
		 */
		"static public function getMuteValue",function getMuteValue()/*:uint*/
		{
			if(org.flixel.FlxG._mute)
				return 0;
			else
				return 1;
		},
		
		/**
		 * Set <code>volume</code> to a number between 0 and 1 to change the global volume.
		 * 
		 * @default 0.5
		 */
		 "static public function get volume",function volume$get()/*:Number*/ { return org.flixel.FlxG._volume; },
		 
		/**
		 * @private
		 */
		"static public function set volume",function volume$set(Volume/*:Number*/)/*:void*/
		{
			org.flixel.FlxG._volume = Volume;
			if(org.flixel.FlxG._volume < 0)
				org.flixel.FlxG._volume = 0;
			else if(org.flixel.FlxG._volume > 1)
				org.flixel.FlxG._volume = 1;
			org.flixel.FlxG.changeSounds();
		},

		/**
		 * Called by FlxGame on state changes to stop and destroy sounds.
		 * 
		 * @param	ForceDestroy		Kill sounds even if they're flagged <code>survive</code>.
		 */
		"static internal function destroySounds",function destroySounds(ForceDestroy/*:Boolean=false*/)/*:void*/
		{if(arguments.length<1){ForceDestroy=false;}
			if(org.flixel.FlxG.sounds == null)
				return;
			if((org.flixel.FlxG.music != null) && (ForceDestroy || !org.flixel.FlxG.music.survive))
				org.flixel.FlxG.music.destroy();
			var i/*:uint*/ = 0;
			var s/*:FlxSound*/;
			var sl/*:uint*/ = org.flixel.FlxG.sounds.length;
			while(i < sl)
			{
				s =as( org.flixel.FlxG.sounds[i++],  org.flixel.FlxSound);
				if((s != null) && (ForceDestroy || !s.survive))
					s.destroy();
			}
		},

		/**
		 * An internal function that adjust the volume levels and the music channel after a change.
		 */
		"static protected function changeSounds",function changeSounds()/*:void*/
		{
			if((org.flixel.FlxG.music != null) && org.flixel.FlxG.music.active)
				org.flixel.FlxG.music.updateTransform();
			var i/*:uint*/ = 0;
			var s/*:FlxSound*/;
			var sl/*:uint*/ = org.flixel.FlxG.sounds.length;
			while(i < sl)
			{
				s =as( org.flixel.FlxG.sounds[i++],  org.flixel.FlxSound);
				if((s != null) && s.active)
					s.updateTransform();
			}
		},
		
		/**
		 * Called by the game loop to make sure the sounds get updated each frame.
		 */
		"static internal function updateSounds",function updateSounds()/*:void*/
		{
			if((org.flixel.FlxG.music != null) && org.flixel.FlxG.music.active)
				org.flixel.FlxG.music.update();
			var i/*:uint*/ = 0;
			var s/*:FlxSound*/;
			var sl/*:uint*/ = org.flixel.FlxG.sounds.length;
			while(i < sl)
			{
				s =as( org.flixel.FlxG.sounds[i++],  org.flixel.FlxSound);
				if((s != null) && s.active)
					s.update();
			}
		},
		
		/**
		 * Internal helper, pauses all game sounds.
		 */
		"static protected function pauseSounds",function pauseSounds()/*:void*/
		{
			if((org.flixel.FlxG.music != null) && org.flixel.FlxG.music.active)
				org.flixel.FlxG.music.pause();
			var i/*:uint*/ = 0;
			var s/*:FlxSound*/;
			var sl/*:uint*/ = org.flixel.FlxG.sounds.length;
			while(i < sl)
			{
				s =as( org.flixel.FlxG.sounds[i++],  org.flixel.FlxSound);
				if((s != null) && s.active)
					s.pause();
			}
		},
		
		/**
		 * Internal helper, pauses all game sounds.
		 */
		"static protected function playSounds",function playSounds()/*:void*/
		{
			if((org.flixel.FlxG.music != null) && org.flixel.FlxG.music.active)
				org.flixel.FlxG.music.play();
			var i/*:uint*/ = 0;
			var s/*:FlxSound*/;
			var sl/*:uint*/ = org.flixel.FlxG.sounds.length;
			while(i < sl)
			{
				s =as( org.flixel.FlxG.sounds[i++],  org.flixel.FlxSound);
				if((s != null) && s.active)
					s.play();
			}
		},
		
		/**
		 * Check the local bitmap cache to see if a bitmap with this key has been loaded already.
		 *
		 * @param	Key		The string key identifying the bitmap.
		 * 
		 * @return	Whether or not this file can be found in the cache.
		 */
		"static public function checkBitmapCache",function checkBitmapCache(Key/*:String*/)/*:Boolean*/
		{
			return (org.flixel.FlxG._cache[Key] != undefined) && (org.flixel.FlxG._cache[Key] != null);
		},
		
		/**
		 * Generates a new <code>BitmapData</code> object (a colored square) and caches it.
		 * 
		 * @param	Width	How wide the square should be.
		 * @param	Height	How high the square should be.
		 * @param	Color	What color the square should be (0xAARRGGBB)
		 * 
		 * @return	The <code>BitmapData</code> we just created.
		 */
		"static public function createBitmap",function createBitmap(Width/*:uint*/, Height/*:uint*/, Color/*:uint*/, Unique/*:Boolean=false*/, Key/*:String=null*/)/*:BitmapData*/
		{if(arguments.length<5){if(arguments.length<4){Unique=false;}Key=null;}
			var key/*:String*/ = Key;
			if(key == null)
			{
				key = Width+"x"+Height+":"+Color;
				if(Unique && (org.flixel.FlxG._cache[key] != undefined) && (org.flixel.FlxG._cache[key] != null))
				{
					//Generate a unique key
					var inc/*:uint*/ = 0;
					var ukey/*:String*/;
					do { ukey = key + inc++;
					} while((org.flixel.FlxG._cache[ukey] != undefined) && (org.flixel.FlxG._cache[ukey] != null));
					key = ukey;
				}
			}
			if(!org.flixel.FlxG.checkBitmapCache(key))
				org.flixel.FlxG._cache[key] = new flash.display.BitmapData(Width,Height,true,Color);
			return org.flixel.FlxG._cache[key];
		},
		
		/**
		 * Loads a bitmap from a file, caches it, and generates a horizontally flipped version if necessary.
		 * 
		 * @param	Graphic		The image file that you want to load.
		 * @param	Reverse		Whether to generate a flipped version.
		 * 
		 * @return	The <code>BitmapData</code> we just created.
		 */
		"static public function addBitmap",function addBitmap(Graphic/*:Class*/, Reverse/*:Boolean=false*/, Unique/*:Boolean=false*/, Key/*:String=null*/)/*:BitmapData*/
		{if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){Reverse=false;}Unique=false;}Key=null;}
			var needReverse/*:Boolean*/ = false;
			var key/*:String*/ = Key;
			if(key == null)
			{
				key = String(Graphic);
				if(Unique && (org.flixel.FlxG._cache[key] != undefined) && (org.flixel.FlxG._cache[key] != null))
				{
					//Generate a unique key
					var inc/*:uint*/ = 0;
					var ukey/*:String*/;
					do { ukey = key + inc++;
					} while((org.flixel.FlxG._cache[ukey] != undefined) && (org.flixel.FlxG._cache[ukey] != null));
					key = ukey;
				}
			}
			//If there is no data for this key, generate the requested graphic
			if(!org.flixel.FlxG.checkBitmapCache(key))
			{
				org.flixel.FlxG._cache[key] = (new Graphic).bitmapData;
				if(Reverse) needReverse = true;
			}
			var pixels/*:BitmapData*/ = org.flixel.FlxG._cache[key];
			if(!needReverse && Reverse && (pixels.width == (new Graphic).bitmapData.width))
				needReverse = true;
			if(needReverse)
			{
				var newPixels/*:BitmapData*/ = new flash.display.BitmapData(pixels.width<<1,pixels.height,true,0x00000000);
				newPixels.draw(pixels);
				var mtx/*:Matrix*/ = new flash.geom.Matrix();
				mtx.scale(-1,1);
				mtx.translate(newPixels.width,0);
				newPixels.draw(pixels,mtx);
				pixels = newPixels;
			}
			return pixels;
		},

		/**
		 * Tells the camera subsystem what <code>FlxCore</code> object to follow.
		 * 
		 * @param	Target		The object to follow.
		 * @param	Lerp		How much lag the camera should have (can help smooth out the camera movement).
		 */
		"static public function follow",function follow(Target/*:FlxObject*/, Lerp/*:Number=1*/)/*:void*/
		{if(arguments.length<2){Lerp=1;}
			org.flixel.FlxG.followTarget = Target;
			org.flixel.FlxG.followLerp = Lerp;
			org.flixel.FlxG._scrollTarget.x = (org.flixel.FlxG.width>>1)-org.flixel.FlxG.followTarget.x-(org.flixel.FlxG.followTarget.width>>1);
			org.flixel.FlxG._scrollTarget.y = (org.flixel.FlxG.height>>1)-org.flixel.FlxG.followTarget.y-(org.flixel.FlxG.followTarget.height>>1);
			org.flixel.FlxG.scroll.x = org.flixel.FlxG._scrollTarget.x;
			org.flixel.FlxG.scroll.y = org.flixel.FlxG._scrollTarget.y;
			org.flixel.FlxG.doFollow();
		},
		
		/**
		 * Specify an additional camera component - the velocity-based "lead",
		 * or amount the camera should track in front of a sprite.
		 * 
		 * @param	LeadX		Percentage of X velocity to add to the camera's motion.
		 * @param	LeadY		Percentage of Y velocity to add to the camera's motion.
		 */
		"static public function followAdjust",function followAdjust(LeadX/*:Number = 0*/, LeadY/*:Number = 0*/)/*:void*/
		{if(arguments.length<2){if(arguments.length<1){LeadX = 0;}LeadY = 0;}
			org.flixel.FlxG.followLead = new flash.geom.Point(LeadX,LeadY);
		},
		
		/**
		 * Specify the boundaries of the level or where the camera is allowed to move.
		 * 
		 * @param	MinX				The smallest X value of your level (usually 0).
		 * @param	MinY				The smallest Y value of your level (usually 0).
		 * @param	MaxX				The largest X value of your level (usually the level width).
		 * @param	MaxY				The largest Y value of your level (usually the level height).
		 * @param	UpdateWorldBounds	Whether the quad tree's dimensions should be updated to match.
		 */
		"static public function followBounds",function followBounds(MinX/*:int=0*/, MinY/*:int=0*/, MaxX/*:int=0*/, MaxY/*:int=0*/, UpdateWorldBounds/*:Boolean=true*/)/*:void*/
		{if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){MinX=0;}MinY=0;}MaxX=0;}MaxY=0;}UpdateWorldBounds=true;}
			org.flixel.FlxG.followMin = new flash.geom.Point(-MinX,-MinY);
			org.flixel.FlxG.followMax = new flash.geom.Point(-MaxX+org.flixel.FlxG.width,-MaxY+org.flixel.FlxG.height);
			if(org.flixel.FlxG.followMax.x > org.flixel.FlxG.followMin.x)
				org.flixel.FlxG.followMax.x = org.flixel.FlxG.followMin.x;
			if(org.flixel.FlxG.followMax.y > org.flixel.FlxG.followMin.y)
				org.flixel.FlxG.followMax.y = org.flixel.FlxG.followMin.y;
			if(UpdateWorldBounds)
				org.flixel.FlxU.setWorldBounds(MinX,MinY,MaxX-MinX,MaxY-MinY);
			org.flixel.FlxG.doFollow();
		},
		
		/**
		 * Retrieves the Flash stage object (required for event listeners)
		 * 
		 * @return	A Flash <code>MovieClip</code> object.
		 */
		"static public function get stage",function stage$get()/*:Stage*/
		{
			if((org.flixel.FlxG._game._state != null)  && (org.flixel.FlxG._game._state.parent != null))
				return org.flixel.FlxG._game._state.parent.stage;
			return null;
		},
		
		/**
		 * Safely access the current game state.
		 */
		"static public function get state",function state$get()/*:FlxState*/
		{
			return org.flixel.FlxG._game._state;
		},
		
		/**
		 * @private
		 */
		"static public function set state",function state$set(State/*:FlxState*/)/*:void*/
		{
			org.flixel.FlxG._game.switchState(State);
		},
		
		/**
		 * Stops and resets the camera.
		 */
		"static public function unfollow",function unfollow()/*:void*/
		{
			org.flixel.FlxG.followTarget = null;
			org.flixel.FlxG.followLead = null;
			org.flixel.FlxG.followLerp = 1;
			org.flixel.FlxG.followMin = null;
			org.flixel.FlxG.followMax = null;
			if(org.flixel.FlxG.scroll == null)
				org.flixel.FlxG.scroll = new flash.geom.Point();
			else
				org.flixel.FlxG.scroll.x = org.flixel.FlxG.scroll.y = 0;
			if(org.flixel.FlxG._scrollTarget == null)
				org.flixel.FlxG._scrollTarget = new flash.geom.Point();
			else
				org.flixel.FlxG._scrollTarget.x = org.flixel.FlxG._scrollTarget.y = 0;
		},

		/**
		 * Called by <code>FlxGame</code> to set up <code>FlxG</code> during <code>FlxGame</code>'s constructor.
		 */
		"static internal function setGameData",function setGameData(Game/*:FlxGame*/,Width/*:uint*/,Height/*:uint*/,Zoom/*:uint*/)/*:void*/
		{
			org.flixel.FlxG._game = Game;
			org.flixel.FlxG._cache = new Object();
			org.flixel.FlxG.width = Width;
			org.flixel.FlxG.height = Height;
			org.flixel.FlxG._mute = false;
			org.flixel.FlxG._volume = 0.5;
			org.flixel.FlxG.sounds = new Array();
			org.flixel.FlxG.mouse = new org.flixel.data.FlxMouse();
			org.flixel.FlxG.keys = new org.flixel.data.FlxKeyboard();
			org.flixel.FlxG.gamepads = new Array(4);
			org.flixel.FlxG.gamepads[0] = new org.flixel.data.FlxGamepad();
			org.flixel.FlxG.gamepads[1] = new org.flixel.data.FlxGamepad();
			org.flixel.FlxG.gamepads[2] = new org.flixel.data.FlxGamepad();
			org.flixel.FlxG.gamepads[3] = new org.flixel.data.FlxGamepad();
			org.flixel.FlxG.scroll = null;
			org.flixel.FlxG._scrollTarget = null;
			org.flixel.FlxG.unfollow();
			org.flixel.FlxG.levels = new Array();
			org.flixel.FlxG.scores = new Array();
			org.flixel.FlxG.level = 0;
			org.flixel.FlxG.score = 0;
			org.flixel.FlxG.kong = null;
			org.flixel.FlxG.pause = false;
			org.flixel.FlxG.timeScale = 1.0;
			org.flixel.FlxG.framerate = 60;
			org.flixel.FlxG.frameratePaused = 10;
			org.flixel.FlxG.maxElapsed = 0.0333333;
			org.flixel.FlxG.elapsed = 0;
			org.flixel.FlxG.showBounds = false;
			
			org.flixel.FlxG.mobile = false;
			
			org.flixel.FlxG.panel = new org.flixel.data.FlxPanel();
			org.flixel.FlxG.quake = new org.flixel.data.FlxQuake(Zoom);
			org.flixel.FlxG.flash = new org.flixel.data.FlxFlash();
			org.flixel.FlxG.fade = new org.flixel.data.FlxFade();

			org.flixel.FlxU.setWorldBounds(0,0,org.flixel.FlxG.width,org.flixel.FlxG.height);
		},

		/**
		 * Internal function that updates the camera and parallax scrolling.
		 */
		"static internal function doFollow",function doFollow()/*:void*/
		{
			if(org.flixel.FlxG.followTarget != null)
			{
				org.flixel.FlxG._scrollTarget.x = (org.flixel.FlxG.width>>1)-org.flixel.FlxG.followTarget.x-(org.flixel.FlxG.followTarget.width>>1);
				org.flixel.FlxG._scrollTarget.y = (org.flixel.FlxG.height>>1)-org.flixel.FlxG.followTarget.y-(org.flixel.FlxG.followTarget.height>>1);
				if((org.flixel.FlxG.followLead != null) && (is(org.flixel.FlxG.followTarget,  org.flixel.FlxSprite)))
				{
					org.flixel.FlxG._scrollTarget.x -= (as(org.flixel.FlxG.followTarget,  org.flixel.FlxSprite)).velocity.x*org.flixel.FlxG.followLead.x;
					org.flixel.FlxG._scrollTarget.y -= (as(org.flixel.FlxG.followTarget,  org.flixel.FlxSprite)).velocity.y*org.flixel.FlxG.followLead.y;
				}
				org.flixel.FlxG.scroll.x += (org.flixel.FlxG._scrollTarget.x-org.flixel.FlxG.scroll.x)*org.flixel.FlxG.followLerp*org.flixel.FlxG.elapsed;
				org.flixel.FlxG.scroll.y += (org.flixel.FlxG._scrollTarget.y-org.flixel.FlxG.scroll.y)*org.flixel.FlxG.followLerp*org.flixel.FlxG.elapsed;
				
				if(org.flixel.FlxG.followMin != null)
				{
					if(org.flixel.FlxG.scroll.x > org.flixel.FlxG.followMin.x)
						org.flixel.FlxG.scroll.x = org.flixel.FlxG.followMin.x;
					if(org.flixel.FlxG.scroll.y > org.flixel.FlxG.followMin.y)
						org.flixel.FlxG.scroll.y = org.flixel.FlxG.followMin.y;
				}
				
				if(org.flixel.FlxG.followMax != null)
				{
					if(org.flixel.FlxG.scroll.x < org.flixel.FlxG.followMax.x)
						org.flixel.FlxG.scroll.x = org.flixel.FlxG.followMax.x;
					if(org.flixel.FlxG.scroll.y < org.flixel.FlxG.followMax.y)
						org.flixel.FlxG.scroll.y = org.flixel.FlxG.followMax.y;
				}
			}
		},
		
		/**
		 * Calls update on the keyboard and mouse input tracking objects.
		 */
		"static internal function updateInput",function updateInput()/*:void*/
		{
			org.flixel.FlxG.keys.update();
			org.flixel.FlxG.mouse.update(org.flixel.FlxG.state.mouseX,org.flixel.FlxG.state.mouseY,org.flixel.FlxG.scroll.x,org.flixel.FlxG.scroll.y);
			var i/*:uint*/ = 0;
			var l/*:uint*/ = org.flixel.FlxG.gamepads.length;
			while(i < l)
				org.flixel.FlxG.gamepads[i++].update();
		},
	];},["log","pause","framerate","frameratePaused","resetInput","playMusic","play","stream","mute","getMuteValue","volume","destroySounds","updateSounds","checkBitmapCache","createBitmap","addBitmap","follow","followAdjust","followBounds","stage","state","unfollow","setGameData","doFollow","updateInput"],["org.flixel.FlxSound","flash.display.BitmapData","String","flash.geom.Matrix","flash.geom.Point","org.flixel.FlxU","Object","Array","org.flixel.data.FlxMouse","org.flixel.data.FlxKeyboard","org.flixel.data.FlxGamepad","org.flixel.data.FlxPanel","org.flixel.data.FlxQuake","org.flixel.data.FlxFlash","org.flixel.data.FlxFade","org.flixel.FlxSprite"], "0.8.0", "0.8.1"
);