joo.classLoader.prepare("package org.flixel",/*
{*/
	/**
	 * Stores a rectangle.
	 */
	"public class FlxRect extends org.flixel.FlxPoint",2,function($$private){;return[
	
		/**
		 * @default 0
		 */
		"public var",{ width/*:Number*/:NaN},
		/**
		 * @default 0
		 */
		"public var",{ height/*:Number*/:NaN},
		
		/**
		 * Instantiate a new rectangle.
		 * 
		 * @param	X		The X-coordinate of the point in space.
		 * @param	Y		The Y-coordinate of the point in space.
		 * @param	Width	Desired width of the rectangle.
		 * @param	Height	Desired height of the rectangle.
		 */
		"public function FlxRect",function FlxRect$(X/*:Number=0*/, Y/*:Number=0*/, Width/*:Number=0*/, Height/*:Number=0*/)
		{if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){X=0;}Y=0;}Width=0;}Height=0;}
			this.super$2(X,Y);
			this.width = Width;
			this.height = Height;
		},
		
		/**
		 * The X coordinate of the left side of the rectangle.  Read-only.
		 */
		"public function get left",function left$get()/*:Number*/
		{
			return this.x;
		},
		
		/**
		 * The X coordinate of the right side of the rectangle.  Read-only.
		 */
		"public function get right",function right$get()/*:Number*/
		{
			return this.x + this.width;
		},
		
		/**
		 * The Y coordinate of the top of the rectangle.  Read-only.
		 */
		"public function get top",function top$get()/*:Number*/
		{
			return this.y;
		},
		
		/**
		 * The Y coordinate of the bottom of the rectangle.  Read-only.
		 */
		"public function get bottom",function bottom$get()/*:Number*/
		{
			return this.y + this.height;
		},
	];},[],["org.flixel.FlxPoint"], "0.8.0", "0.8.3"
);