joo.classLoader.prepare("package org.flixel.data",/*
{
	import flash.display.Bitmap
	import flash.display.BitmapData
	import flash.display.Sprite
	import flash.text.AntiAliasType
	import flash.text.GridFitType
	import flash.text.TextField
	import flash.text.TextFormat
	
	import org.flixel.FlxG
	import org.flixel.FlxMonitor*/

	/**
	 * Contains all the logic for the developer console.
	 * This class is automatically created by FlxGame.
	 */
	"public class FlxConsole extends flash.display.Sprite",6,function($$private){var trace=joo.trace;return[function(){joo.classLoader.init(flash.text.AntiAliasType,org.flixel.FlxG,flash.text.GridFitType);},
	
		"public var",{ mtrUpdate/*:FlxMonitor*/:null},
		"public var",{ mtrRender/*:FlxMonitor*/:null},
		"public var",{ mtrTotal/*:FlxMonitor*/:null},
		
		/**
		 * @private
		 */
		"protected const",{ MAX_CONSOLE_LINES/*:uint*/ : 256},
		/**
		 * @private
		 */
		"protected var",{ _console/*:Sprite*/:null},
		/**
		 * @private
		 */
		"protected var",{ _text/*:TextField*/:null},
		/**
		 * @private
		 */
		"protected var",{ _fpsDisplay/*:TextField*/:null},
		/**
		 * @private
		 */
		"protected var",{ _extraDisplay/*:TextField*/:null},
		/**
		 * @private
		 */
		"protected var",{ _curFPS/*:uint*/:0},
		/**
		 * @private
		 */
		"protected var",{ _lines/*:Array*/:null},
		/**
		 * @private
		 */
		"protected var",{ _Y/*:Number*/:NaN},
		/**
		 * @private
		 */
		"protected var",{ _YT/*:Number*/:NaN},
		/**
		 * @private
		 */
		"protected var",{ _bx/*:int*/:0},
		/**
		 * @private
		 */
		"protected var",{ _by/*:int*/:0},
		/**
		 * @private
		 */
		"protected var",{ _byt/*:int*/:0},
		
		/**
		 * Constructor
		 * 
		 * @param	X		X position of the console
		 * @param	Y		Y position of the console
		 * @param	Zoom	The game's zoom level
		 */
		"public function FlxConsole",function FlxConsole$(X/*:uint*/,Y/*:uint*/,Zoom/*:uint*/)
		{
			this.super$6();
			
			this.visible = false;
			this.x = X*Zoom;
			this._by = Y*Zoom;
			this._byt = this._by - org.flixel.FlxG.height*Zoom;
			this._YT = this._Y = this.y = this._byt;
			var tmp/*:Bitmap*/ = new flash.display.Bitmap(new flash.display.BitmapData(org.flixel.FlxG.width*Zoom,org.flixel.FlxG.height*Zoom,true,0x7F000000));
			this.addChild(tmp);
			
			this.mtrUpdate = new org.flixel.FlxMonitor(16);
			this.mtrRender = new org.flixel.FlxMonitor(16);
			this.mtrTotal = new org.flixel.FlxMonitor(16);

			this._text = new flash.text.TextField();
			this._text.width = tmp.width;
			this._text.height = tmp.height;
			this._text.multiline = true;
			this._text.wordWrap = true;
			this._text.embedFonts = true;
			this._text.selectable = false;
			this._text.antiAliasType = flash.text.AntiAliasType.NORMAL;
			this._text.gridFitType = flash.text.GridFitType.PIXEL;
			this._text.defaultTextFormat = new flash.text.TextFormat("system",8,0xffffff);
			this.addChild(this._text);

			this._fpsDisplay = new flash.text.TextField();
			this._fpsDisplay.width = 100;
			this._fpsDisplay.x = tmp.width-100;
			this._fpsDisplay.height = 20;
			this._fpsDisplay.multiline = true;
			this._fpsDisplay.wordWrap = true;
			this._fpsDisplay.embedFonts = true;
			this._fpsDisplay.selectable = false;
			this._fpsDisplay.antiAliasType = flash.text.AntiAliasType.NORMAL;
			this._fpsDisplay.gridFitType = flash.text.GridFitType.PIXEL;
			this._fpsDisplay.defaultTextFormat = new flash.text.TextFormat("system",16,0xffffff,true,null,null,null,null,"right");
			this.addChild(this._fpsDisplay);
			
			this._extraDisplay = new flash.text.TextField();
			this._extraDisplay.width = 100;
			this._extraDisplay.x = tmp.width-100;
			this._extraDisplay.height = 64;
			this._extraDisplay.y = 20;
			this._extraDisplay.alpha = 0.5;
			this._extraDisplay.multiline = true;
			this._extraDisplay.wordWrap = true;
			this._extraDisplay.embedFonts = true;
			this._extraDisplay.selectable = false;
			this._extraDisplay.antiAliasType = flash.text.AntiAliasType.NORMAL;
			this._extraDisplay.gridFitType = flash.text.GridFitType.PIXEL;
			this._extraDisplay.defaultTextFormat = new flash.text.TextFormat("system",8,0xffffff,true,null,null,null,null,"right");
			this.addChild(this._extraDisplay);
			
			this._lines = new Array();
		},
		
		/**
		 * Logs data to the developer console
		 * 
		 * @param	Text	The text that you wanted to write to the console
		 */
		"public function log",function log(Text/*:String*/)/*:void*/
		{
			if(Text == null)
				Text = "NULL";
			trace(Text);
			if(org.flixel.FlxG.mobile)
				return;
			this._lines.push(Text);
			if(this._lines.length > this.MAX_CONSOLE_LINES)
			{
				this._lines.shift();
				var newText/*:String*/ = "";
				for(var i/*:uint*/ = 0; i < this._lines.length; i++)
					newText += this._lines[i]+"\n";
				this._text.text = newText;
			}
			else
				this._text.appendText(Text+"\n");
			this._text.scrollV = this._text.height;
		},
		
		/**
		 * Shows/hides the console.
		 */
		"public function toggle",function toggle()/*:void*/
		{
			if(org.flixel.FlxG.mobile)
			{
				this.log("FRAME TIMING DATA:\n=========================\n"+this.printTimingData()+"\n");
				return;
			}
			
			if(this._YT == this._by)
				this._YT = this._byt;
			else
			{
				this._YT = this._by;
				this.visible = true;
			}
		},
		
		/**
		 * Updates and/or animates the dev console.
		 */
		"public function update",function update()/*:void*/
		{
			var total/*:Number*/ = this.mtrTotal.average();
			this._fpsDisplay.text = $$uint(1000/total) + " fps";
			this._extraDisplay.text = this.printTimingData();
			
			if(this._Y < this._YT)
				this._Y += org.flixel.FlxG.height*10*org.flixel.FlxG.elapsed;
			else if(this._Y > this._YT)
				this._Y -= org.flixel.FlxG.height*10*org.flixel.FlxG.elapsed;
			if(this._Y > this._by)
				this._Y = this._by;
			else if(this._Y < this._byt)
			{
				this._Y = this._byt;
				this.visible = false;
			}
			this.y = Math.floor(this._Y);
		},
		
		/**
		 * Returns a string of frame timing data.
		 */
		"protected function printTimingData",function printTimingData()/*:String*/
		{
			var up/*:uint*/ = this.mtrUpdate.average();
			var rn/*:uint*/ = this.mtrRender.average();
			var fx/*:uint*/ = up+rn;
			var tt/*:uint*/ = this.mtrTotal.average();
			return up + "ms update\n" + rn + "ms render\n" + fx + "ms flixel\n" + (tt-fx) + "ms flash\n" + tt + "ms total";
		},
	];},[],["flash.display.Sprite","org.flixel.FlxG","flash.display.Bitmap","flash.display.BitmapData","org.flixel.FlxMonitor","flash.text.TextField","flash.text.AntiAliasType","flash.text.GridFitType","flash.text.TextFormat","Array","uint","Math"], "0.8.0", "0.8.2-SNAPSHOT"
);