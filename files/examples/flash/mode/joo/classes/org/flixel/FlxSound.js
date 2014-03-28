joo.classLoader.prepare("package org.flixel",/*
{
	import flash.events.Event
	import flash.media.Sound
	import flash.media.SoundChannel
	import flash.media.SoundTransform
	import flash.net.URLRequest*/
	
	/**
	 * This is the universal flixel sound object, used for streaming, music, and sound effects.
	 */
	"public class FlxSound extends org.flixel.FlxObject",4,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG,flash.events.Event);},
	
		/**
		 * Whether or not this sound should be automatically destroyed when you switch states.
		 */
		"public var",{ survive/*:Boolean*/:false},
		/**
		 * Whether the sound is currently playing or not.
		 */
		"public var",{ playing/*:Boolean*/:false},
		/**
		 * The ID3 song name.  Defaults to null.  Currently only works for streamed sounds.
		 */
		"public var",{ name/*:String*/:null},
		/**
		 * The ID3 artist name.  Defaults to null.  Currently only works for streamed sounds.
		 */
		"public var",{ artist/*:String*/:null},
		
		"protected var",{ _init/*:Boolean*/:false},
		"protected var",{ _sound/*:Sound*/:null},
		"protected var",{ _channel/*:SoundChannel*/:null},
		"protected var",{ _transform/*:SoundTransform*/:null},
		"protected var",{ _position/*:Number*/:NaN},
		"protected var",{ _volume/*:Number*/:NaN},
		"protected var",{ _volumeAdjust/*:Number*/:NaN},
		"protected var",{ _looped/*:Boolean*/:false},
		"protected var",{ _core/*:FlxObject*/:null},
		"protected var",{ _radius/*:Number*/:NaN},
		"protected var",{ _pan/*:Boolean*/:false},
		"protected var",{ _fadeOutTimer/*:Number*/:NaN},
		"protected var",{ _fadeOutTotal/*:Number*/:NaN},
		"protected var",{ _pauseOnFadeOut/*:Boolean*/:false},
		"protected var",{ _fadeInTimer/*:Number*/:NaN},
		"protected var",{ _fadeInTotal/*:Number*/:NaN},
		"protected var",{ _point2/*:FlxPoint*/:null},
		
		/**
		 * The FlxSound constructor gets all the variables initialized, but NOT ready to play a sound yet.
		 */
		"public function FlxSound",function FlxSound$()
		{
			this.super$4();
			this._point2 = new org.flixel.FlxPoint();
			this._transform = new flash.media.SoundTransform();
			this.init();
			this.fixed = true; //no movement usually
		},
		
		/**
		 * An internal function for clearing all the variables used by sounds.
		 */
		"protected function init",function init()/*:void*/
		{
			this._transform.pan = 0;
			this._sound = null;
			this._position = 0;
			this._volume = 1.0;
			this._volumeAdjust = 1.0;
			this._looped = false;
			this._core = null;
			this._radius = 0;
			this._pan = false;
			this._fadeOutTimer = 0;
			this._fadeOutTotal = 0;
			this._pauseOnFadeOut = false;
			this._fadeInTimer = 0;
			this._fadeInTotal = 0;
			this.active = false;
			this.visible = false;
			this.solid = false;
			this.playing = false;
			this.name = null;
			this.artist = null;
		},
		
		/**
		 * One of two main setup functions for sounds, this function loads a sound from an embedded MP3.
		 * 
		 * @param	EmbeddedSound	An embedded Class object representing an MP3 file.
		 * @param	Looped			Whether or not this sound should loop endlessly.
		 * 
		 * @return	This <code>FlxSound</code> instance (nice for chaining stuff together, if you're into that).
		 */
		"public function loadEmbedded",function loadEmbedded(EmbeddedSound/*:Class*/, Looped/*:Boolean=false*/)/*:FlxSound*/
		{switch(arguments.length){case 0:case 1:Looped=false;}
			this.stop();
			this.init();
			this._sound = new EmbeddedSound();
			//NOTE: can't pull ID3 info from embedded sound currently
			this._looped = Looped;
			this.updateTransform();
			this.active = true;
			return this;
		},
		
		/**
		 * One of two main setup functions for sounds, this function loads a sound from a URL.
		 * 
		 * @param	EmbeddedSound	A string representing the URL of the MP3 file you want to play.
		 * @param	Looped			Whether or not this sound should loop endlessly.
		 * 
		 * @return	This <code>FlxSound</code> instance (nice for chaining stuff together, if you're into that).
		 */
		"public function loadStream",function loadStream(SoundURL/*:String*/, Looped/*:Boolean=false*/)/*:FlxSound*/
		{switch(arguments.length){case 0:case 1:Looped=false;}
			this.stop();
			this.init();
			this._sound = new flash.media.Sound();
			this._sound.addEventListener(flash.events.Event.ID3, $$bound(this,"gotID3"));
			this._sound.load(new flash.net.URLRequest(SoundURL));
			this._looped = Looped;
			this.updateTransform();
			this.active = true;
			return this;
		},
		
		/**
		 * Call this function if you want this sound's volume to change
		 * based on distance from a particular FlxCore object.
		 * 
		 * @param	X		The X position of the sound.
		 * @param	Y		The Y position of the sound.
		 * @param	Core	The object you want to track.
		 * @param	Radius	The maximum distance this sound can travel.
		 * 
		 * @return	This FlxSound instance (nice for chaining stuff together, if you're into that).
		 */
		"public function proximity",function proximity(X/*:Number*/,Y/*:Number*/,Core/*:FlxObject*/,Radius/*:Number*/,Pan/*:Boolean=true*/)/*:FlxSound*/
		{switch(arguments.length){case 0:case 1:case 2:case 3:case 4:Pan=true;}
			this.x = X;
			this.y = Y;
			this._core = Core;
			this._radius = Radius;
			this._pan = Pan;
			return this;
		},
		
		/**
		 * Call this function to play the sound.
		 */
		"public function play",function play(Skip/*:Number=0*/)/*:void*/  // Added SKip parameter, time in ms to skip ahead
		{switch(arguments.length){case 0:Skip=0;}
			if(this._position < 0)
				return;
			if(this._looped)
			{
				if(this._position == 0)
				{
					if(this._channel == null)
						this._channel = this._sound.play(Skip,9999,this._transform);  // Use Skip here
					if(this._channel == null)
						this.active = false;
				}
				else
				{
					this._channel = this._sound.play(this._position,0,this._transform);
					if(this._channel == null)
						this.active = false;
					else
						this._channel.addEventListener(flash.events.Event.SOUND_COMPLETE, $$bound(this,"looped"));
				}
			}
			else
			{
				if(this._position == 0)
				{
					if(this._channel == null)
					{
						this._channel = this._sound.play(Skip,0,this._transform);  // Use Skip here, too
						if(this._channel == null)
							this.active = false;
						else
							this._channel.addEventListener(flash.events.Event.SOUND_COMPLETE, $$bound(this,"stopped"));
					}
				}
				else
				{
					this._channel = this._sound.play(this._position,0,this._transform);
					if(this._channel == null)
						this.active = false;
				}
			}
			this.playing = (this._channel != null);
			this._position = 0;
		},
		
		/**
		 * Call this function to pause this sound.
		 */
		"public function pause",function pause()/*:void*/
		{
			if(this._channel == null)
			{
				this._position = -1;
				return;
			}
			this._position = this._channel.position;
			this._channel.stop();
			if(this._looped)
			{
				while(this._position >= this._sound.length)
					this._position -= this._sound.length;
			}
			this._channel = null;
			this.playing = false;
		},
		
		/**
		 * Call this function to stop this sound.
		 */
		"public function stop",function stop()/*:void*/
		{
			this._position = 0;
			if(this._channel != null)
			{
				this._channel.stop();
				this.stopped();
			}
		},
		
		/**
		 * Call this function to make this sound fade out over a certain time interval.
		 * 
		 * @param	Seconds			The amount of time the fade out operation should take.
		 * @param	PauseInstead	Tells the sound to pause on fadeout, instead of stopping.
		 */
		"public function fadeOut",function fadeOut(Seconds/*:Number*/,PauseInstead/*:Boolean=false*/)/*:void*/
		{switch(arguments.length){case 0:case 1:PauseInstead=false;}
			this._pauseOnFadeOut = PauseInstead;
			this._fadeInTimer = 0;
			this._fadeOutTimer = Seconds;
			this._fadeOutTotal = this._fadeOutTimer;
		},
		
		/**
		 * Call this function to make a sound fade in over a certain
		 * time interval (calls <code>play()</code> automatically).
		 * 
		 * @param	Seconds		The amount of time the fade-in operation should take.
		 */
		"public function fadeIn",function fadeIn(Seconds/*:Number*/)/*:void*/
		{
			this._fadeOutTimer = 0;
			this._fadeInTimer = Seconds;
			this._fadeInTotal = this._fadeInTimer;
			this.play();
		},
		
		/**
		 * Set <code>volume</code> to a value between 0 and 1 to change how this sound is.
		 */
		"public function get volume",function volume$get()/*:Number*/
		{
			return this._volume;
		},
		
		/**
		 * @private
		 */
		"public function set volume",function volume$set(Volume/*:Number*/)/*:void*/
		{
			this._volume = Volume;
			if(this._volume < 0)
				this._volume = 0;
			else if(this._volume > 1)
				this._volume = 1;
			this.updateTransform();
		},
		
		/**
		 * Internal function that performs the actual logical updates to the sound object.
		 * Doesn't do much except optional proximity and fade calculations.
		 */
		"protected function updateSound",function updateSound()/*:void*/
		{
			if(this._position != 0)
				return;
			
			var radial/*:Number*/ = 1.0;
			var fade/*:Number*/ = 1.0;
			
			//Distance-based volume control
			if(this._core != null)
			{
				var _point/*:FlxPoint*/ = new org.flixel.FlxPoint();
				var _point2/*:FlxPoint*/ = new org.flixel.FlxPoint();
				this._core.getScreenXY(_point);
				this.getScreenXY(_point2);
				var dx/*:Number*/ = _point.x - _point2.x;
				var dy/*:Number*/ = _point.y - _point2.y;
				radial = (this._radius - Math.sqrt(dx*dx + dy*dy))/this._radius;
				if(radial < 0) radial = 0;
				if(radial > 1) radial = 1;
				
				if(this._pan)
				{
					var d/*:Number*/ = -dx/this._radius;
					if(d < -1) d = -1;
					else if(d > 1) d = 1;
					this._transform.pan = d;
				}
			}
			
			//Cross-fading volume control
			if(this._fadeOutTimer > 0)
			{
				this._fadeOutTimer -= org.flixel.FlxG.elapsed;
				if(this._fadeOutTimer <= 0)
				{
					if(this._pauseOnFadeOut)
						this.pause();
					else
						this.stop();
				}
				fade = this._fadeOutTimer/this._fadeOutTotal;
				if(fade < 0) fade = 0;
			}
			else if(this._fadeInTimer > 0)
			{
				this._fadeInTimer -= org.flixel.FlxG.elapsed;
				fade = this._fadeInTimer/this._fadeInTotal;
				if(fade < 0) fade = 0;
				fade = 1 - fade;
			}
			
			this._volumeAdjust = radial*fade;
			this.updateTransform();
		},

		/**
		 * The basic game loop update function.  Just calls <code>updateSound()</code>.
		 */
		"override public function update",function update()/*:void*/
		{
			this.update$4();
			this.updateSound();			
		},
		
		/**
		 * The basic class destructor, stops the music and removes any leftover events.
		 */
		"override public function destroy",function destroy()/*:void*/
		{
			if(this.active)
				this.stop();
		},
		
		/**
		 * An internal function used to help organize and change the volume of the sound.
		 */
		"internal function updateTransform",function updateTransform()/*:void*/
		{
			this._transform.volume = org.flixel.FlxG.getMuteValue()*org.flixel.FlxG.volume*this._volume*this._volumeAdjust;
			if(this._channel != null)
				this._channel.soundTransform = this._transform;
		},
		
		/**
		 * An internal helper function used to help Flash resume playing a looped sound.
		 * 
		 * @param	event		An <code>Event</code> object.
		 */
		"protected function looped",function looped(event/*:Event=null*/)/*:void*/
		{switch(arguments.length){case 0:event=null;}
		    if (this._channel == null)
		    	return;
	        this._channel.removeEventListener(flash.events.Event.SOUND_COMPLETE,$$bound(this,"looped"));
	        this._channel = null;
			this.play();
		},

		/**
		 * An internal helper function used to help Flash clean up and re-use finished sounds.
		 * 
		 * @param	event		An <code>Event</code> object.
		 */
		"protected function stopped",function stopped(event/*:Event=null*/)/*:void*/
		{switch(arguments.length){case 0:event=null;}
			if(!this._looped)
	        	this._channel.removeEventListener(flash.events.Event.SOUND_COMPLETE,$$bound(this,"stopped"));
	        else
	        	this._channel.removeEventListener(flash.events.Event.SOUND_COMPLETE,$$bound(this,"looped"));
	        this._channel = null;
	        this.active = false;
			this.playing = false;
		},
		
		/**
		 * Internal event handler for ID3 info (i.e. fetching the song name).
		 * 
		 * @param	event	An <code>Event</code> object.
		 */
		"protected function gotID3",function gotID3(event/*:Event=null*/)/*:void*/
		{switch(arguments.length){case 0:event=null;}
			org.flixel.FlxG.log("got ID3 info!");
			if(this._sound.id3.songName.length > 0)
				this.name = this._sound.id3.songName;
			if(this._sound.id3.artist.length > 0)
				this.artist = this._sound.id3.artist;
			this._sound.removeEventListener(flash.events.Event.ID3, $$bound(this,"gotID3"));
		},
	];},[],["org.flixel.FlxObject","org.flixel.FlxPoint","flash.media.SoundTransform","flash.media.Sound","flash.events.Event","flash.net.URLRequest","Math","org.flixel.FlxG"], "0.8.0", "0.8.2-SNAPSHOT"
);