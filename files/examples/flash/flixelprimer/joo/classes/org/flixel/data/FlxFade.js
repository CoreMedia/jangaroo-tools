joo.classLoader.prepare("package org.flixel.data",/*
{
	import org.flixel.**/
	
	/**
	 * This is a special effects utility class to help FlxGame do the 'fade' effect.
	 */
	"public class FlxFade extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		/**
		 * How long the effect should last.
		 */
		"protected var",{ _delay/*:Number*/:NaN},
		/**
		 * Callback for when the effect is finished.
		 */
		"protected var",{ _complete/*:Function*/:null},
		
		/**
		 * Constructor initializes the fade object
		 */
		"public function FlxFade",function FlxFade$()
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
		 * @param	Duration		How long it should take to fade the screen out
		 * @param	FadeComplete	A function you want to run when the fade finishes
		 * @param	Force			Force the effect to reset
		 */
		"public function start",function start(Color/*:uint=0xff000000*/, Duration/*:Number=1*/, FadeComplete/*:Function=null*/, Force/*:Boolean=false*/)/*:void*/
		{if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){Color=0xff000000;}Duration=1;}FadeComplete=null;}Force=false;}
			if(!Force && this.exists) return;
			this.fill(Color);
			this._delay = Duration;
			this._complete = FadeComplete;
			this.alpha = 0;
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
			this.alpha += org.flixel.FlxG.elapsed/this._delay;
			if(this.alpha >= 1)
			{
				this.alpha = 1;
				if(this._complete != null)
					this._complete();
			}
		},
	];},[],["org.flixel.FlxSprite","org.flixel.FlxG"], "0.8.0", "0.8.3"
);