joo.classLoader.prepare("package org.flixel.data",/*
{*/
	/**
	 * Just a helper structure for the FlxSprite animation system
	 */
	"public class FlxAnim",1,function($$private){;return[
	
		/**
		 * String name of the animation (e.g. "walk")
		 */
		"public var",{ name/*:String*/:null},
		/**
		 * Seconds between frames (basically the framerate)
		 */
		"public var",{ delay/*:Number*/:NaN},
		/**
		 * A list of frames stored as <code>uint</code> objects
		 */
		"public var",{ frames/*:Array*/:null},
		/**
		 * Whether or not the animation is looped
		 */
		"public var",{ looped/*:Boolean*/:false},
		
		/**
		 * Constructor
		 * 
		 * @param	Name		What this animation should be called (e.g. "run")
		 * @param	Frames		An array of numbers indicating what frames to play in what order (e.g. 1, 2, 3)
		 * @param	FrameRate	The speed in frames per second that the animation should play at (e.g. 40)
		 * @param	Looped		Whether or not the animation is looped or just plays once
		 */
		"public function FlxAnim",function FlxAnim$(Name/*:String*/, Frames/*:Array*/, FrameRate/*:Number=0*/, Looped/*:Boolean=true*/)
		{switch(arguments.length){case 0:case 1:case 2:FrameRate=0;case 3:Looped=true;}
			this.name = Name;
			this.delay = 0;
			if(FrameRate > 0)
				this.delay = 1.0/FrameRate;
			this.frames = Frames;
			this.looped = Looped;
		},
	];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);