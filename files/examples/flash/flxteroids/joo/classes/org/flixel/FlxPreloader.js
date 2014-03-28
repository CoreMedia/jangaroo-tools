joo.classLoader.prepare("package org.flixel",/*
{
	import flash.display.Bitmap
	import flash.display.BitmapData
	import flash.display.DisplayObject
	import flash.display.MovieClip
	import flash.display.Sprite
	import flash.display.StageAlign
	import flash.display.StageScaleMode
	import flash.events.Event
	import flash.events.MouseEvent
	import flash.net.URLRequest
	import flash.net.navigateToURL
	import flash.text.TextField
	import flash.text.TextFormat
	import flash.utils.getDefinitionByName
	import flash.utils.getTimer*/
	

	/**
	 * This class handles the 8-bit style preloader.
	 */
	"public class FlxPreloader extends flash.display.MovieClip",7,function($$private){var is=joo.is,as=joo.as,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.display.StageScaleMode,flash.events.MouseEvent,org.flixel.FlxG,flash.display.StageAlign,flash.events.Event);},
	
		{Embed:{source:"org/flixel/data/logo.png"}}, "protected var",{ ImgLogo/*:Class*/:null},
		{Embed:{source:"org/flixel/data/logo_corners.png"}}, "protected var",{ ImgLogoCorners/*:Class*/:null},
		{Embed:{source:"org/flixel/data/logo_light.png"}}, "protected var",{ ImgLogoLight/*:Class*/:null},

		/**
		 * @private
		 */
		"protected var",{ _init/*:Boolean*/:false},
		/**
		 * @private
		 */
		"protected var",{ _buffer/*:Sprite*/:null},
		/**
		 * @private
		 */
		"protected var",{ _bmpBar/*:Bitmap*/:null},
		/**
		 * @private
		 */
		"protected var",{ _text/*:TextField*/:null},
		/**
		 * Useful for storing "real" stage width if you're scaling your preloader graphics.
		 */
		"protected var",{ _width/*:uint*/:0},
		/**
		 * Useful for storing "real" stage height if you're scaling your preloader graphics.
		 */
		"protected var",{ _height/*:uint*/:0},
		/**
		 * @private
		 */
		"protected var",{ _logo/*:Bitmap*/:null},
		/**
		 * @private
		 */
		"protected var",{ _logoGlow/*:Bitmap*/:null},
		/**
		 * @private
		 */
		"protected var",{ _min/*:uint*/:0},

		/**
		 * This should always be the name of your main project/document class (e.g. GravityHook).
		 */
		"public var",{ className/*:String*/:null},
		/**
		 * Set this to your game's URL to use built-in site-locking.
		 */
		"public var",{ myURL/*:String*/:null},
		/**
		 * Change this if you want the flixel logo to show for more or less time.  Default value is 0 seconds.
		 */
		"public var",{ minDisplayTime/*:Number*/:NaN},
		
		/**
		 * Constructor
		 */
		"public function FlxPreloader",function FlxPreloader$()
		{this.super$7();
			this.minDisplayTime = 0;
			
			this.stop();
            this.stage.scaleMode = flash.display.StageScaleMode.NO_SCALE;
			this.stage.align = flash.display.StageAlign.TOP_LEFT;
			
			//Check if we are on debug or release mode and set _DEBUG accordingly
            try
            {
                throw new Error("Setting global debug flag...");
            }
            catch(e){if(is(e,Error))
            {
                var re/*:RegExp*/ = /\[.*:[0-9]+\]/;
                org.flixel.FlxG.debug = re.test(e.getStackTrace());
            }else throw e;}
			
			var tmp/*:Bitmap*/;
			if(!org.flixel.FlxG.debug && (this.myURL != null) && (this.root.loaderInfo.url.indexOf(this.myURL) < 0))
			{
				tmp = new flash.display.Bitmap(new flash.display.BitmapData(this.stage.stageWidth,this.stage.stageHeight,true,0xFFFFFFFF));
				this.addChild(tmp);
				
				var fmt/*:TextFormat*/ = new flash.text.TextFormat();
				fmt.color = 0x000000;
				fmt.size = 16;
				fmt.align = "center";
				fmt.bold = true;
				fmt.font = "system";
				
				var txt/*:TextField*/ = new flash.text.TextField();
				txt.width = tmp.width-16;
				txt.height = tmp.height-16;
				txt.y = 8;
				txt.multiline = true;
				txt.wordWrap = true;
				txt.embedFonts = true;
				txt.defaultTextFormat = fmt;
				txt.text = "Hi there!  It looks like somebody copied this game without my permission.  Just click anywhere, or copy-paste this URL into your browser.\n\n"+this.myURL+"\n\nto play the game at my site.  Thanks, and have fun!";
				this.addChild(txt);
				
				txt.addEventListener(flash.events.MouseEvent.CLICK,$$bound(this,"goToMyURL$7"));
				tmp.addEventListener(flash.events.MouseEvent.CLICK,$$bound(this,"goToMyURL$7"));
				return;
			}
			this._init = false;
			this.addEventListener(flash.events.Event.ENTER_FRAME, $$bound(this,"onEnterFrame$7"));
		},
		
		"private function goToMyURL",function goToMyURL(event/*:MouseEvent=null*/)/*:void*/
		{if(arguments.length<1){event=null;}
			flash.net.navigateToURL(new flash.net.URLRequest("http://"+this.myURL));
		},
		
		"private function onEnterFrame",function onEnterFrame(event/*:Event*/)/*:void*/
        {
			if(!this._init)
			{
				if((this.stage.stageWidth <= 0) || (this.stage.stageHeight <= 0))
					return;
				this.create();
				this._init = true;
			}
        	var i/*:int*/;
            this.graphics.clear();
			var time/*:uint*/ = flash.utils.getTimer();
            if((this.framesLoaded >= this.totalFrames) && (time > this._min))
            {
                this.removeEventListener(flash.events.Event.ENTER_FRAME, $$bound(this,"onEnterFrame$7"));
                this.nextFrame();
                var mainClass/*:Class*/ =/* Class*/(flash.utils.getDefinitionByName(this.className));
	            if(mainClass)
	            {
	                var app/*:Object*/ = new mainClass();
	                this.addChild(as(app,  flash.display.DisplayObject));
	            }
                this.removeChild(this._buffer);
            }
            else
			{
				var percent/*:Number*/ = this.root.loaderInfo.bytesLoaded/this.root.loaderInfo.bytesTotal;
				if((this._min > 0) && (percent > time/this._min))
					percent = time/this._min;
            	this.update(percent);
			}
        },
		
		/**
		 * Override this to create your own preloader objects.
		 * Highly recommended you also override update()!
		 */
		"protected function create",function create()/*:void*/
		{
			this._min = 0;
			if(!org.flixel.FlxG.debug)
				this._min = this.minDisplayTime*1000;
			this._buffer = new flash.display.Sprite();
			this._buffer.scaleX = 2;
			this._buffer.scaleY = 2;
			this.addChild(this._buffer);
			this._width = this.stage.stageWidth/this._buffer.scaleX;
			this._height = this.stage.stageHeight/this._buffer.scaleY;
			this._buffer.addChild(new flash.display.Bitmap(new flash.display.BitmapData(this._width,this._height,false,0x00345e)));
			var b/*:Bitmap*/ = new this.ImgLogoLight();
			b.smoothing = true;
			b.width = b.height = this._height;
			b.x = (this._width-b.width)/2;
			this._buffer.addChild(b);
			this._bmpBar = new flash.display.Bitmap(new flash.display.BitmapData(1,7,false,0x5f6aff));
			this._bmpBar.x = 4;
			this._bmpBar.y = this._height-11;
			this._buffer.addChild(this._bmpBar);
			this._text = new flash.text.TextField();
			this._text.defaultTextFormat = new flash.text.TextFormat("system",8,0x5f6aff);
			this._text.embedFonts = true;
			this._text.selectable = false;
			this._text.multiline = false;
			this._text.x = 2;
			this._text.y = this._bmpBar.y - 11;
			this._text.width = 80;
			this._buffer.addChild(this._text);
			this._logo = new this.ImgLogo();
			this._logo.scaleX = this._logo.scaleY = this._height/8;
			this._logo.x = (this._width-this._logo.width)/2;
			this._logo.y = (this._height-this._logo.height)/2;
			this._buffer.addChild(this._logo);
			this._logoGlow = new this.ImgLogo();
			this._logoGlow.smoothing = true;
			this._logoGlow.blendMode = "screen";
			this._logoGlow.scaleX = this._logoGlow.scaleY = this._height/8;
			this._logoGlow.x = (this._width-this._logoGlow.width)/2;
			this._logoGlow.y = (this._height-this._logoGlow.height)/2;
			this._buffer.addChild(this._logoGlow);
			b = new this.ImgLogoCorners();
			b.smoothing = true;
			b.width = this._width;
			b.height = this._height;
			this._buffer.addChild(b);
			b = new flash.display.Bitmap(new flash.display.BitmapData(this._width,this._height,false,0xffffff));
			var i/*:uint*/ = 0;
			var j/*:uint*/ = 0;
			while(i < this._height)
			{
				j = 0;
				while(j < this._width)
					b.bitmapData.setPixel(j++,i,0);
				i+=2;
			}
			b.blendMode = "overlay";
			b.alpha = 0.25;
			this._buffer.addChild(b);
		},
		
		/**
		 * Override this function to manually update the preloader.
		 * 
		 * @param	Percent		How much of the program has loaded.
		 */
		"protected function update",function update(Percent/*:Number*/)/*:void*/
		{
			this._bmpBar.scaleX = Percent*(this._width-8);
			this._text.text = "FLX v"+org.flixel.FlxG.LIBRARY_MAJOR_VERSION+"."+org.flixel.FlxG.LIBRARY_MINOR_VERSION+" "+org.flixel.FlxU.floor(Percent*100)+"%";
			this._text.setTextFormat(this._text.defaultTextFormat);
			if(Percent < 0.1)
			{
				this._logoGlow.alpha = 0;
				this._logo.alpha = 0;
			}
			else if(Percent < 0.15)
			{
				this._logoGlow.alpha = org.flixel.FlxU.random();
				this._logo.alpha = 0;
			}
			else if(Percent < 0.2)
			{
				this._logoGlow.alpha = 0;
				this._logo.alpha = 0;
			}
			else if(Percent < 0.25)
			{
				this._logoGlow.alpha = 0;
				this._logo.alpha = org.flixel.FlxU.random();
			}
			else if(Percent < 0.7)
			{
				this._logoGlow.alpha = (Percent-0.45)/0.45;
				this._logo.alpha = 1;
			}
			else if((Percent > 0.8) && (Percent < 0.9))
			{
				this._logoGlow.alpha = 1-(Percent-0.8)/0.1;
				this._logo.alpha = 0;
			}
			else if(Percent > 0.9)
			{
				this._buffer.alpha = 1-(Percent-0.9)/0.1;
			}
		},
	];},[],["flash.display.MovieClip","resource:org/flixel/data/logo.png","resource:org/flixel/data/logo_corners.png","resource:org/flixel/data/logo_light.png","flash.display.StageScaleMode","flash.display.StageAlign","Error","org.flixel.FlxG","flash.display.Bitmap","flash.display.BitmapData","flash.text.TextFormat","flash.text.TextField","flash.events.MouseEvent","flash.events.Event","flash.net.URLRequest","Class","flash.display.DisplayObject","flash.display.Sprite","org.flixel.FlxU"], "0.8.0", "0.8.1"
);