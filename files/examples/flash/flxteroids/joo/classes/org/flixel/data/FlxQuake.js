joo.classLoader.prepare("package org.flixel.data",/*
{
	import org.flixel.FlxG*/
	
	/**
	 * This is a special effects utility class to help FlxGame do the 'quake' or screenshake effect.
	 */
	"public class FlxQuake",1,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		/**
		 * The game's level of zoom.
		 */
		"protected var",{ _zoom/*:uint*/:0},
		/**
		 * The intensity of the quake effect: a percentage of the screen's size.
		 */
		"protected var",{ _intensity/*:Number*/:NaN},
		/**
		 * Set to countdown the quake time.
		 */
		"protected var",{ _timer/*:Number*/:NaN},
		
		/**
		 * The amount of X distortion to apply to the screen.
		 */
		"public var",{ x/*:int*/:0},
		/**
		 * The amount of Y distortion to apply to the screen.
		 */
		"public var",{ y/*:int*/:0},
		
		/**
		 * Constructor.
		 */
		"public function FlxQuake",function FlxQuake$(Zoom/*:uint*/)
		{
			this._zoom = Zoom;
			this.start(0);
		},
		
		/**
		 * Reset and trigger this special effect.
		 * 
		 * @param	Intensity	Percentage of screen size representing the maximum distance that the screen can move during the 'quake'.
		 * @param	Duration	The length in seconds that the "quake" should last.
		 */
		"public function start",function start(Intensity/*:Number=0.05*/,Duration/*:Number=0.5*/)/*:void*/
		{if(arguments.length<2){if(arguments.length<1){Intensity=0.05;}Duration=0.5;}
			this.stop();
			this._intensity = Intensity;
			this._timer = Duration;
		},
		
		/**
		 * Stops this screen effect.
		 */
		"public function stop",function stop()/*:void*/
		{
			this.x = 0;
			this.y = 0;
			this._intensity = 0;
			this._timer = 0;
		},
		
		/**
		 * Updates and/or animates this special effect.
		 */
		"public function update",function update()/*:void*/
		{
			if(this._timer > 0)
			{
				this._timer -= org.flixel.FlxG.elapsed;
				if(this._timer <= 0)
				{
					this._timer = 0;
					this.x = 0;
					this.y = 0;
				}
				else
				{
					this.x = (Math.random()*this._intensity*org.flixel.FlxG.width*2-this._intensity*org.flixel.FlxG.width)*this._zoom;
					this.y = (Math.random()*this._intensity*org.flixel.FlxG.height*2-this._intensity*org.flixel.FlxG.height)*this._zoom;
				}
			}
		},
	];},[],["org.flixel.FlxG","Math"], "0.8.0", "0.8.1"
);