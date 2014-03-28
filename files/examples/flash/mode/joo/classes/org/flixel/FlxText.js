joo.classLoader.prepare("package org.flixel",/*
{
	import flash.display.BitmapData
	import flash.text.TextField
	import flash.text.TextFormat*/
	
	/**
	 * Extends <code>FlxSprite</code> to support rendering text.
	 * Can tint, fade, rotate and scale just like a sprite.
	 * Doesn't really animate though, as far as I know.
	 * Also does nice pixel-perfect centering on pixel fonts
	 * as long as they are only one liners.
	 */
	"public class FlxText extends org.flixel.FlxSprite",5,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		"protected var",{ _tf/*:TextField*/:null},
		"protected var",{ _regen/*:Boolean*/:false},
		"protected var",{ _shadow/*:uint*/:0},
		
		/**
		 * Creates a new <code>FlxText</code> object at the specified position.
		 * 
		 * @param	X				The X position of the text.
		 * @param	Y				The Y position of the text.
		 * @param	Width			The width of the text object (height is determined automatically).
		 * @param	Text			The actual text you would like to display initially.
		 * @param	EmbeddedFont	Whether this text field uses embedded fonts or nto
		 */
		"public function FlxText",function FlxText$(X/*:Number*/, Y/*:Number*/, Width/*:uint*/, Text/*:String=null*/, EmbeddedFont/*:Boolean=true*/)
		{switch(arguments.length){case 0:case 1:case 2:case 3:Text=null;case 4:EmbeddedFont=true;}
			this.super$5(X,Y);
			this.createGraphic(Width,1,0);
			
			if(Text == null)
				Text = "";
			this._tf = new flash.text.TextField();
			this._tf.width = Width;
			this._tf.embedFonts = EmbeddedFont;
			this._tf.selectable = false;
			this._tf.sharpness = 100;
			this._tf.multiline = true;
			this._tf.wordWrap = true;
			this._tf.text = Text;
			var tf/*:TextFormat*/ = new flash.text.TextFormat("system",8,0xffffff);
			this._tf.defaultTextFormat = tf;
			this._tf.setTextFormat(tf);
			if(Text.length <= 0)
				this._tf.height = 1;
			else
				this._tf.height = 10;
			
			this._regen = true;
			this._shadow = 0;
			this.solid = false;
			this.calcFrame();
		},
		
		/**
		 * You can use this if you have a lot of text parameters
		 * to set instead of the individual properties.
		 * 
		 * @param	Font		The name of the font face for the text display.
		 * @param	Size		The size of the font (in pixels essentially).
		 * @param	Color		The color of the text in traditional flash 0xRRGGBB format.
		 * @param	Alignment	A string representing the desired alignment ("left,"right" or "center").
		 * @param	ShadowColor	A uint representing the desired text shadow color in flash 0xRRGGBB format.
		 * 
		 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
		 */
		"public function setFormat",function setFormat(Font/*:String=null*/,Size/*:Number=8*/,Color/*:uint=0xffffff*/,Alignment/*:String=null*/,ShadowColor/*:uint=0*/)/*:FlxText*/
		{switch(arguments.length){case 0:Font=null;case 1:Size=8;case 2:Color=0xffffff;case 3:Alignment=null;case 4:ShadowColor=0;}
			if(Font == null)
				Font = "";
			var tf/*:TextFormat*/ = this.dtfCopy();
			tf.font = Font;
			tf.size = Size;
			tf.color = Color;
			tf.align = Alignment;
			this._tf.defaultTextFormat = tf;
			this._tf.setTextFormat(tf);
			this._shadow = ShadowColor;
			this._regen = true;
			this.calcFrame();
			return this;
		},
		
		/**
		 * The text being displayed.
		 */
		"public function get text",function text$get()/*:String*/
		{
			return this._tf.text;
		},
		
		/**
		 * @private
		 */
		"public function set text",function text$set(Text/*:String*/)/*:void*/
		{
			var ot/*:String*/ = this._tf.text;
			this._tf.text = Text;
			if(this._tf.text != ot)
			{
				this._regen = true;
				this.calcFrame();
			}
		},
		
		/**
		 * The size of the text being displayed.
		 */
		 "public function get size",function size$get()/*:Number*/
		{
			return as( this._tf.defaultTextFormat.size,  Number);
		},
		
		/**
		 * @private
		 */
		"public function set size",function size$set(Size/*:Number*/)/*:void*/
		{
			var tf/*:TextFormat*/ = this.dtfCopy();
			tf.size = Size;
			this._tf.defaultTextFormat = tf;
			this._tf.setTextFormat(tf);
			this._regen = true;
			this.calcFrame();
		},
		
		/**
		 * The color of the text being displayed.
		 */
		"override public function get color",function color$get()/*:uint*/
		{
			return as( this._tf.defaultTextFormat.color,  uint);
		},
		
		/**
		 * @private
		 */
		"override public function set color",function color$set(Color/*:uint*/)/*:void*/
		{
			var tf/*:TextFormat*/ = this.dtfCopy();
			tf.color = Color;
			this._tf.defaultTextFormat = tf;
			this._tf.setTextFormat(tf);
			this._regen = true;
			this.calcFrame();
		},
		
		/**
		 * The font used for this text.
		 */
		"public function get font",function font$get()/*:String*/
		{
			return this._tf.defaultTextFormat.font;
		},
		
		/**
		 * @private
		 */
		"public function set font",function font$set(Font/*:String*/)/*:void*/
		{
			var tf/*:TextFormat*/ = this.dtfCopy();
			tf.font = Font;
			this._tf.defaultTextFormat = tf;
			this._tf.setTextFormat(tf);
			this._regen = true;
			this.calcFrame();
		},
		
		/**
		 * The alignment of the font ("left", "right", or "center").
		 */
		"public function get alignment",function alignment$get()/*:String*/
		{
			return this._tf.defaultTextFormat.align;
		},
		
		/**
		 * @private
		 */
		"public function set alignment",function alignment$set(Alignment/*:String*/)/*:void*/
		{
			var tf/*:TextFormat*/ = this.dtfCopy();
			tf.align = Alignment;
			this._tf.defaultTextFormat = tf;
			this._tf.setTextFormat(tf);
			this.calcFrame();
		},
		
		/**
		 * The alignment of the font ("left", "right", or "center").
		 */
		"public function get shadow",function shadow$get()/*:uint*/
		{
			return this._shadow;
		},
		
		/**
		 * @private
		 */
		"public function set shadow",function shadow$set(Color/*:uint*/)/*:void*/
		{
			this._shadow = Color;
			this.calcFrame();
		},
		
		/**
		 * Internal function to update the current animation frame.
		 */
		"override protected function calcFrame",function calcFrame()/*:void*/
		{
			if(this._regen)
			{
				//Need to generate a new buffer to store the text graphic
				var i/*:uint*/ = 0;
				var nl/*:uint*/ = this._tf.numLines;
				this.height = 0;
				while(i < nl)
					this.height += this._tf.getLineMetrics(i++).height;
				this.height += 4; //account for 2px gutter on top and bottom
				this._pixels = new flash.display.BitmapData(this.width,this.height,true,0);
				this._bbb = new flash.display.BitmapData(this.width,this.height,true,0);
				this.frameHeight = this.height;
				this._tf.height = this.height*1.2;
				this._flashRect.x = 0;
				this._flashRect.y = 0;
				this._flashRect.width = this.width;
				this._flashRect.height = this.height;
				this._regen = false;
			}
			else	//Else just clear the old buffer before redrawing the text
				this._pixels.fillRect(this._flashRect,0);
			
			if((this._tf != null) && (this._tf.text != null) && (this._tf.text.length > 0))
			{
				//Now that we've cleared a buffer, we need to actually render the text to it
				var tf/*:TextFormat*/ = this._tf.defaultTextFormat;
				var tfa/*:TextFormat*/ = tf;
				this._mtx.identity();
				//If it's a single, centered line of text, we center it ourselves so it doesn't blur to hell
				if((tf.align == "center") && (this._tf.numLines == 1))
				{
					tfa = new flash.text.TextFormat(tf.font,tf.size,tf.color,null,null,null,null,null,"left");
					this._tf.setTextFormat(tfa);				
					this._mtx.translate(Math.floor((this.width - this._tf.getLineMetrics(0).width)/2),0);
				}
				//Render a single pixel shadow beneath the text
				if(this._shadow > 0)
				{
					this._tf.setTextFormat(new flash.text.TextFormat(tfa.font,tfa.size,this._shadow,null,null,null,null,null,tfa.align));				
					this._mtx.translate(1,1);
					this._pixels.draw(this._tf,this._mtx,this._ct);
					this._mtx.translate(-1,-1);
					this._tf.setTextFormat(new flash.text.TextFormat(tfa.font,tfa.size,tfa.color,null,null,null,null,null,tfa.align));
				}
				//Actually draw the text onto the buffer
				this._pixels.draw(this._tf,this._mtx,this._ct);
				this._tf.setTextFormat(new flash.text.TextFormat(tf.font,tf.size,tf.color,null,null,null,null,null,tf.align));
			}
			
			//Finally, update the visible pixels
			if((this._framePixels == null) || (this._framePixels.width != this._pixels.width) || (this._framePixels.height != this._pixels.height))
				this._framePixels = new flash.display.BitmapData(this._pixels.width,this._pixels.height,true,0);
			this._framePixels.copyPixels(this._pixels,this._flashRect,this._flashPointZero);
			if(org.flixel.FlxG.showBounds)
				this.drawBounds();
			if(this.solid)
				this.refreshHulls();
		},
		
		/**
		 * A helper function for updating the <code>TextField</code> that we use for rendering.
		 * 
		 * @return	A writable copy of <code>TextField.defaultTextFormat</code>.
		 */
		"protected function dtfCopy",function dtfCopy()/*:TextFormat*/
		{
			var dtf/*:TextFormat*/ = this._tf.defaultTextFormat;
			return new flash.text.TextFormat(dtf.font,dtf.size,dtf.color,dtf.bold,dtf.italic,dtf.underline,dtf.url,dtf.target,dtf.align);
		},
	];},[],["org.flixel.FlxSprite","flash.text.TextField","flash.text.TextFormat","Number","uint","flash.display.BitmapData","Math","org.flixel.FlxG"], "0.8.0", "0.8.2-SNAPSHOT"
);