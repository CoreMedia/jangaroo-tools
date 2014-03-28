joo.classLoader.prepare("package org.flixel",/*
{*/
	/**
	 * Stores a 2D floating point coordinate.
	 */
	"public class FlxPoint",1,function($$private){;return[
	
		/**
		 * @default 0
		 */
		"public var",{ x/*:Number*/:NaN},
		/**
		 * @default 0
		 */
		"public var",{ y/*:Number*/:NaN},
		
		/**
		 * Instantiate a new point object.
		 * 
		 * @param	X		The X-coordinate of the point in space.
		 * @param	Y		The Y-coordinate of the point in space.
		 */
		"public function FlxPoint",function FlxPoint$(X/*:Number=0*/, Y/*:Number=0*/)
		{if(arguments.length<2){if(arguments.length<1){X=0;}Y=0;}
			this.x = X;
			this.y = Y;
		},
		
		/**
		 * Convert object to readable string name.  Useful for debugging, save games, etc.
		 */
		"public function toString",function toString()/*:String*/
		{
			return org.flixel.FlxU.getClassName(this,true);
		},
	];},[],["org.flixel.FlxU"], "0.8.0", "0.8.1"
);