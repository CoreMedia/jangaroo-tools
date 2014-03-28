joo.classLoader.prepare("package org.flixel.data",/*
{
	import org.flixel.**/
	
	/**
	 * This is a special effects utility class to help FlxGame do the 'flash' effect
	 */
	"public class FlxFlash extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		/**
		 * How long the effect should last.
		 */
		"protected var",{ _delay/*:Number*/:NaN},
		/**
		 * Callback for when the effect is finished.
		 */
		"protected var",{ _complete/*:Function*/:null},
		
		/**
		 * Constructor for this special effect
		 */
		"public function FlxFlash",function FlxFlash$()
		{
			this.super$5();
			this.createGraphic(org.flixel.FlxG.width,org.flixel.FlxG.height,0,true);
			this.scrollFactor.x = 0;
			this.scrollFactor.y = 0;
			this.exists = false;
			this.solid = false;
			this.fixed = true;
		},
		
		/**
		 * Reset and trigger this special effect
		 * 
		 * @param	Color			The color you want to use
		 * @param	Duration		How long it takes for the flash to fade
		 * @param	FlashComplete	A function you want to run when the flash finishes
		 * @param	Force			Force the effect to reset
		 */
		"public function start",function start(Color/*:uint=0xffffffff*/, Duration/*:Number=1*/, FlashComplete/*:Function=null*/, Force/*:Boolean=false*/)/*:void*/
		{if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){Color=0xffffffff;}Duration=1;}FlashComplete=null;}Force=false;}
			if(!Force && this.exists) return;
			this.fill(Color);
			this._delay = Duration;
			this._complete = FlashComplete;
			this.alpha = 1;
			this.exists = true;
		},
		
		/**
		 * Stops and hides this screen effect.
		 */
		"public function stop",function stop()/*:void*/
		{
			this.exists = false;
		},
		
		/**
		 * Updates and/or animates this special effect
		 */
		"override public function update",function update()/*:void*/
		{
			this.alpha -= org.flixel.FlxG.elapsed/this._delay;
			if(this.alpha <= 0)
			{
				this.exists = false;
				if(this._complete != null)
					this._complete();
			}
		},
	];},[],["org.flixel.FlxSprite","org.flixel.FlxG"], "0.8.0", "0.8.3"
);