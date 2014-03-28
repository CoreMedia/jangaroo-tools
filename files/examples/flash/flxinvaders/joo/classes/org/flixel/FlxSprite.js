joo.classLoader.prepare("package org.flixel",/*
{
	import flash.display.Bitmap
	import flash.display.BitmapData
	import flash.display.Graphics
	import flash.display.Sprite
	import flash.geom.ColorTransform
	import flash.geom.Matrix
	import flash.geom.Point
	import flash.geom.Rectangle
	
	import org.flixel.data.FlxAnim*/
	
	/**
	* The main "game object" class, handles basic physics and animation.
	*/
	"public class FlxSprite extends org.flixel.FlxObject",4,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxU,org.flixel.FlxG);},
	
		/**
		 * Useful for controlling flipped animations and checking player orientation.
		 */
		"static public const",{ LEFT/*:uint*/ : 0},
		/**
		 * Useful for controlling flipped animations and checking player orientation.
		 */
		"static public const",{ RIGHT/*:uint*/ : 1},
		/**
		 * Useful for checking player orientation.
		 */
		"static public const",{ UP/*:uint*/ : 2},
		/**
		 * Useful for checking player orientation.
		 */
		"static public const",{ DOWN/*:uint*/ : 3},
		 
		/**
		* If you changed the size of your sprite object to shrink the bounding box,
		* you might need to offset the new bounding box from the top-left corner of the sprite.
		*/
		"public var",{ offset/*:FlxPoint*/:null},
		
		/**
		 * Change the size of your sprite's graphic.
		 * NOTE: Scale doesn't currently affect collisions automatically,
		 * you will need to adjust the width, height and offset manually.
		 * WARNING: scaling sprites decreases rendering performance for this sprite by a factor of 10x!
		 */
		"public var",{ scale/*:FlxPoint*/:null},
		/**
		 * Blending modes, just like Photoshop!
		 * E.g. "multiply", "screen", etc.
		 * @default null
		 */
		"public var",{ blend/*:String*/:null},
		/**
		 * Controls whether the object is smoothed when rotated, affects performance.
		 * @default false
		 */
		"public var",{ antialiasing/*:Boolean*/:false},
		/**
		 * Whether the current animation has finished its first (or only) loop.
		 */
		"public var",{ finished/*:Boolean*/:false},
		/**
		 * The width of the actual graphic or image being displayed (not necessarily the game object/bounding box).
		 * NOTE: Edit at your own risk!!  This is intended to be read-only.
		 */
		"public var",{ frameWidth/*:uint*/:0},
		/**
		 * The height of the actual graphic or image being displayed (not necessarily the game object/bounding box).
		 * NOTE: Edit at your own risk!!  This is intended to be read-only.
		 */
		"public var",{ frameHeight/*:uint*/:0},
		/**
		 * The total number of frames in this image (assumes each row is full).
		 */
		"public var",{ frames/*:uint*/:0},
		
		//Animation helpers
		"protected var",{ _animations/*:Array*/:null},
		"protected var",{ _flipped/*:uint*/:0},
		"protected var",{ _curAnim/*:FlxAnim*/:null},
		"protected var",{ _curFrame/*:uint*/:0},
		"protected var",{ _caf/*:uint*/:0},
		"protected var",{ _frameTimer/*:Number*/:NaN},
		"protected var",{ _callback/*:Function*/:null},
		"protected var",{ _facing/*:uint*/:0},
		"protected var",{ _bakedRotation/*:Number*/:NaN},
		
		//Various rendering helpers
		"protected var",{ _flashRect/*:Rectangle*/:null},
		"protected var",{ _flashRect2/*:Rectangle*/:null},
		"protected var",{ _flashPointZero/*:Point*/:null},
		"protected var",{ _pixels/*:BitmapData*/:null},
		"protected var",{ _colorTransformedPixels/*:BitmapData*/:null},
		"protected var",{ _framePixels/*:BitmapData*/:null},
		"protected var",{ _alpha/*:Number*/:NaN},
		"protected var",{ _color/*:uint*/:0},
		"protected var",{ _ct/*:ColorTransform*/:null},
		"protected var",{ _mtx/*:Matrix*/:null},
		"protected var",{ _bbb/*:BitmapData*/:null},
		"protected var",{ _boundsVisible/*:Boolean*/:false},
		"static protected var",{ _gfxSprite/*:Sprite*/:null},
		"static protected var",{ _gfx/*:Graphics*/:null},
		
		/**
		 * Creates a white 8x8 square <code>FlxSprite</code> at the specified position.
		 * Optionally can load a simple, one-frame graphic instead.
		 * 
		 * @param	X				The initial X position of the sprite.
		 * @param	Y				The initial Y position of the sprite.
		 * @param	SimpleGraphic	The graphic you want to display (OPTIONAL - for simple stuff only, do NOT use for animated images!).
		 */
		"public function FlxSprite",function FlxSprite$(X/*:Number=0*/,Y/*:Number=0*/,SimpleGraphic/*:Class=null*/)
		{if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){X=0;}Y=0;}SimpleGraphic=null;}
			this.super$4();
			this.x = X;
			this.y = Y;

			this._flashRect = new flash.geom.Rectangle();
			this._flashRect2 = new flash.geom.Rectangle();
			this._flashPointZero = new flash.geom.Point();
			this.offset = new org.flixel.FlxPoint();
			
			this.scale = new org.flixel.FlxPoint(1,1);
			this._alpha = 1;
			this._color = 0x00ffffff;
			this.blend = null;
			this.antialiasing = false;
			
			this.finished = false;
			this._facing = org.flixel.FlxSprite.RIGHT;
			this._animations = new Array();
			this._flipped = 0;
			this._curAnim = null;
			this._curFrame = 0;
			this._caf = 0;
			this._frameTimer = 0;

			this._mtx = new flash.geom.Matrix();
			this._callback = null;
			if(org.flixel.FlxSprite._gfxSprite == null)
			{
				org.flixel.FlxSprite._gfxSprite = new flash.display.Sprite();
				org.flixel.FlxSprite._gfx = org.flixel.FlxSprite._gfxSprite.graphics;
			}
			
			if(SimpleGraphic == null)
				this.createGraphic(8,8);
			else
				this.loadGraphic(SimpleGraphic);
		},
		
		/**
		 * Load an image from an embedded graphic file.
		 * 
		 * @param	Graphic		The image you want to use.
		 * @param	Animated	Whether the Graphic parameter is a single sprite or a row of sprites.
		 * @param	Reverse		Whether you need this class to generate horizontally flipped versions of the animation frames.
		 * @param	Width		OPTIONAL - Specify the width of your sprite (helps FlxSprite figure out what to do with non-square sprites or sprite sheets).
		 * @param	Height		OPTIONAL - Specify the height of your sprite (helps FlxSprite figure out what to do with non-square sprites or sprite sheets).
		 * @param	Unique		Whether the graphic should be a unique instance in the graphics cache.
		 * 
		 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
		 */
		"public function loadGraphic",function loadGraphic(Graphic/*:Class*/,Animated/*:Boolean=false*/,Reverse/*:Boolean=false*/,Width/*:uint=0*/,Height/*:uint=0*/,Unique/*:Boolean=false*/)/*:FlxSprite*/
		{if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){Animated=false;}Reverse=false;}Width=0;}Height=0;}Unique=false;}
			this._bakedRotation = 0;
			this._pixels = org.flixel.FlxG.addBitmap(Graphic,Reverse,Unique);
			this._colorTransformedPixels = null;
			if(Reverse)
				this._flipped = this._pixels.width>>1;
			else
				this._flipped = 0;
			if(Width == 0)
			{
				if(Animated)
					Width = this._pixels.height;
				else if(this._flipped > 0)
					Width = this._pixels.width*0.5;
				else
					Width = this._pixels.width;
			}
			this.width = this.frameWidth = Width;
			if(Height == 0)
			{
				if(Animated)
					Height = this.width;
				else
					Height = this._pixels.height;
			}
			this.height = this.frameHeight = Height;
			this.resetHelpers();
			return this;
		},
		
		/**
		 * Create a pre-rotated sprite sheet from a simple sprite.
		 * This can make a huge difference in graphical performance!
		 * 
		 * @param	Graphic			The image you want to rotate & stamp.
		 * @param	Frames			The number of frames you want to use (more == smoother rotations).
		 * @param	Offset			Use this to select a specific frame to draw from the graphic.
		 * @param	AntiAliasing	Whether to use high quality rotations when creating the graphic.
		 * @param	AutoBuffer		Whether to automatically increase the image size to accomodate rotated corners.
		 * 
		 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
		 */
		"public function loadRotatedGraphic",function loadRotatedGraphic(Graphic/*:Class*/, Rotations/*:uint=16*/, Frame/*:int=-1*/, AntiAliasing/*:Boolean=false*/, AutoBuffer/*:Boolean=false*/)/*:FlxSprite*/
		{if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){Rotations=16;}Frame=-1;}AntiAliasing=false;}AutoBuffer=false;}
			//Create the brush and canvas
			var rows/*:uint*/ = Math.sqrt(Rotations);
			var brush/*:BitmapData*/ = org.flixel.FlxG.addBitmap(Graphic);
			if(Frame >= 0)
			{
				//Using just a segment of the graphic - find the right bit here
				var full/*:BitmapData*/ = brush;
				brush = new flash.display.BitmapData(full.height,full.height);
				var rx/*:uint*/ = Frame*brush.width;
				var ry/*:uint*/ = 0;
				var fw/*:uint*/ = full.width;
				if(rx >= fw)
				{
					ry = $$uint(rx/fw)*brush.height;
					rx %= fw;
				}
				this._flashRect.x = rx;
				this._flashRect.y = ry;
				this._flashRect.width = brush.width;
				this._flashRect.height = brush.height;
				brush.copyPixels(full,this._flashRect,this._flashPointZero);
			}
			
			var max/*:uint*/ = brush.width;
			if(brush.height > max)
				max = brush.height;
			if(AutoBuffer)
				max *= 1.5;
			var cols/*:uint*/ = org.flixel.FlxU.ceil(Rotations/rows);
			this.width = max*cols;
			this.height = max*rows;
			var key/*:String*/ = String(Graphic) + ":" + Frame + ":" + this.width + "x" + this.height;
			var skipGen/*:Boolean*/ = org.flixel.FlxG.checkBitmapCache(key);
			this._pixels = org.flixel.FlxG.createBitmap(this.width, this.height, 0, true, key);
			this._colorTransformedPixels = null;
			this.width = this.frameWidth = this._pixels.width;
			this.height = this.frameHeight = this._pixels.height;
			this._bakedRotation = 360/Rotations;
			
			//Generate a new sheet if necessary, then fix up the width & height
			if(!skipGen)
			{
				var r/*:uint*/ = 0;
				var c/*:uint*/;
				var ba/*:Number*/ = 0;
				var bw2/*:uint*/ = brush.width*0.5;
				var bh2/*:uint*/ = brush.height*0.5;
				var gxc/*:uint*/ = max*0.5;
				var gyc/*:uint*/ = max*0.5;
				while(r < rows)
				{
					c = 0;
					while(c < cols)
					{
						this._mtx.identity();
						this._mtx.translate(-bw2,-bh2);
						this._mtx.rotate(ba*0.017453293);
						this._mtx.translate(max*c+gxc, gyc);
						ba += this._bakedRotation;
						this._pixels.draw(brush,this._mtx,null,null,null,AntiAliasing);
						c++;
					}
					gyc += max;
					r++;
				}
			}
			this.frameWidth = this.frameHeight = this.width = this.height = max;
			this.resetHelpers();
			return this;
		},
		
		/**
		 * This function creates a flat colored square image dynamically.
		 * 
		 * @param	Width		The width of the sprite you want to generate.
		 * @param	Height		The height of the sprite you want to generate.
		 * @param	Color		Specifies the color of the generated block.
		 * @param	Unique		Whether the graphic should be a unique instance in the graphics cache.
		 * @param	Key			Optional parameter - specify a string key to identify this graphic in the cache.  Trumps Unique flag.
		 * 
		 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
		 */
		"public function createGraphic",function createGraphic(Width/*:uint*/,Height/*:uint*/,Color/*:uint=0xffffffff*/,Unique/*:Boolean=false*/,Key/*:String=null*/)/*:FlxSprite*/
		{if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){Color=0xffffffff;}Unique=false;}Key=null;}
			this._bakedRotation = 0;
			this._pixels = org.flixel.FlxG.createBitmap(Width,Height,Color,Unique,Key);
			this._colorTransformedPixels = null;
			this.width = this.frameWidth = this._pixels.width;
			this.height = this.frameHeight = this._pixels.height;
			this.resetHelpers();
			return this;
		},
		
		/**
		 * Set <code>pixels</code> to any <code>BitmapData</code> object.
		 * Automatically adjust graphic size and render helpers.
		 */
		"public function get pixels",function pixels$get()/*:BitmapData*/
		{
			return this._pixels;
		},
		
		/**
		 * @private
		 */
		"public function set pixels",function pixels$set(Pixels/*:BitmapData*/)/*:void*/
		{
			this._pixels = Pixels;
			this._colorTransformedPixels = null;
			this.width = this.frameWidth = this._pixels.width;
			this.height = this.frameHeight = this._pixels.height;
			this.resetHelpers();
		},
		
		/**
		 * Resets some important variables for sprite optimization and rendering.
		 */
		"protected function resetHelpers",function resetHelpers()/*:void*/
		{
			this._boundsVisible = false;
			this._flashRect.x = 0;
			this._flashRect.y = 0;
			this._flashRect.width = this.frameWidth;
			this._flashRect.height = this.frameHeight;
			this._flashRect2.x = 0;
			this._flashRect2.y = 0;
			this._flashRect2.width = this._pixels.width;
			this._flashRect2.height = this._pixels.height;
			if((this._framePixels == null) || (this._framePixels.width != this.width) || (this._framePixels.height != this.height))
				this._framePixels = new flash.display.BitmapData(this.width,this.height);
			if((this._bbb == null) || (this._bbb.width != this.width) || (this._bbb.height != this.height))
				this._bbb = new flash.display.BitmapData(this.width,this.height);
			this.origin.x = this.frameWidth*0.5;
			this.origin.y = this.frameHeight*0.5;
			this._framePixels.copyPixels(this._pixels,this._flashRect,this._flashPointZero);
			this.frames = (this._flashRect2.width / this._flashRect.width) * (this._flashRect2.height / this._flashRect.height);
			if(this._ct != null) this._framePixels.colorTransform(this._flashRect,this._ct);
			if(org.flixel.FlxG.showBounds)
				this.drawBounds();
			this._caf = 0;
			this.refreshHulls();
		},
		
		/**
		 * @private
		 */
		"override public function set solid",function solid$set(Solid/*:Boolean*/)/*:void*/
		{
			var os/*:Boolean*/ = this._solid;
			this._solid = Solid;
			if((os != this._solid) && org.flixel.FlxG.showBounds)
				this.calcFrame();
		},
		
		/**
		 * @private
		 */
		"override public function set fixed",function fixed$set(Fixed/*:Boolean*/)/*:void*/
		{
			var of/*:Boolean*/ = this._fixed;
			this._fixed = Fixed;
			if((of != this._fixed) && org.flixel.FlxG.showBounds)
				this.calcFrame();
		},
		
		/**
		 * Set <code>facing</code> using <code>FlxSprite.LEFT</code>,<code>RIGHT</code>,
		 * <code>UP</code>, and <code>DOWN</code> to take advantage of
		 * flipped sprites and/or just track player orientation more easily.
		 */
		"public function get facing",function facing$get()/*:uint*/
		{
			return this._facing;
		},
		
		/**
		 * @private
		 */
		"public function set facing",function facing$set(Direction/*:uint*/)/*:void*/
		{
			var c/*:Boolean*/ = this._facing != Direction;
			this._facing = Direction;
			if(c) this.calcFrame();
		},
		
		/**
		 * Set <code>alpha</code> to a number between 0 and 1 to change the opacity of the sprite.
		 */
		"public function get alpha",function alpha$get()/*:Number*/
		{
			return this._alpha;
		},
		
		/**
		 * @private
		 */
		"public function set alpha",function alpha$set(Alpha/*:Number*/)/*:void*/
		{
			if(Alpha > 1) Alpha = 1;
			if(Alpha < 0) Alpha = 0;
			if(Alpha == this._alpha) return;
			this._alpha = Alpha;
			if((this._alpha != 1) || (this._color != 0x00ffffff)) this._ct = new flash.geom.ColorTransform((this._color>>16)*0.00392,(this._color>>8&0xff)*0.00392,(this._color&0xff)*0.00392,this._alpha);
			else this._ct = null;
			this.calcFrame();
		},
		
		/**
		 * Set <code>color</code> to a number in this format: 0xRRGGBB.
		 * <code>color</code> IGNORES ALPHA.  To change the opacity use <code>alpha</code>.
		 * Tints the whole sprite to be this color (similar to OpenGL vertex colors).
		 */
		"public function get color",function color$get()/*:uint*/
		{
			return this._color;
		},
		
		/**
		 * @private
		 */
		"public function set color",function color$set(Color/*:uint*/)/*:void*/
		{
			Color &= 0x00ffffff;
			if(this._color == Color) return;
			this._color = Color;
			if((this._alpha != 1) || (this._color != 0x00ffffff)) this._ct = new flash.geom.ColorTransform((this._color>>16)*0.00392,(this._color>>8&0xff)*0.00392,(this._color&0xff)*0.00392,this._alpha);
			else this._ct = null;
			this.calcFrame();
		},
		

		/**
		 * This function draws or stamps one <code>FlxSprite</code> onto another.
		 * This function is NOT intended to replace <code>render()</code>!
		 * 
		 * @param	Brush		The image you want to use as a brush or stamp or pen or whatever.
		 * @param	X			The X coordinate of the brush's top left corner on this sprite.
		 * @param	Y			They Y coordinate of the brush's top left corner on this sprite.
		 */
		"public function draw",function draw(Brush/*:FlxSprite*/,X/*:int=0*/,Y/*:int=0*/)/*:void*/
		{if(arguments.length<3){if(arguments.length<2){X=0;}Y=0;}
			var b/*:BitmapData*/ = Brush._framePixels;
			
			//Simple draw
			if(((Brush.angle == 0) || (Brush._bakedRotation > 0)) && (Brush.scale.x == 1) && (Brush.scale.y == 1) && (Brush.blend == null))
			{
				this._flashPoint.x = X;
				this._flashPoint.y = Y;
				this._flashRect2.width = b.width;
				this._flashRect2.height = b.height;
				this._pixels.copyPixels(b,this._flashRect2,this._flashPoint,null,null,true);
				this._flashRect2.width = this._pixels.width;
				this._flashRect2.height = this._pixels.height;
				this.calcFrame();
				return;
			}

			//Advanced draw
			this._mtx.identity();
			this._mtx.translate(-Brush.origin.x,-Brush.origin.y);
			this._mtx.scale(Brush.scale.x,Brush.scale.y);
			if(Brush.angle != 0)
				this._mtx.rotate(Brush.angle * 0.017453293);
			this._mtx.translate(X+Brush.origin.x,Y+Brush.origin.y);
			this._pixels.draw(b,this._mtx,null,Brush.blend,null,Brush.antialiasing);
			this._colorTransformedPixels = null;
			this.calcFrame();
		},
		
		/**
		 * This function draws a line on this sprite from position X1,Y1
		 * to position X2,Y2 with the specified color.
		 * 
		 * @param	StartX		X coordinate of the line's start point.
		 * @param	StartY		Y coordinate of the line's start point.
		 * @param	EndX		X coordinate of the line's end point.
		 * @param	EndY		Y coordinate of the line's end point.
		 * @param	Color		The line's color.
		 * @param	Thickness	How thick the line is in pixels (default value is 1).
		 */
		"public function drawLine",function drawLine(StartX/*:Number*/,StartY/*:Number*/,EndX/*:Number*/,EndY/*:Number*/,Color/*:uint*/,Thickness/*:uint=1*/)/*:void*/
		{if(arguments.length<6){Thickness=1;}
			//Draw line
			org.flixel.FlxSprite._gfx.clear();
			org.flixel.FlxSprite._gfx.moveTo(StartX,StartY);
			org.flixel.FlxSprite._gfx.lineStyle(Thickness,Color);
			org.flixel.FlxSprite._gfx.lineTo(EndX,EndY);
			
			//Cache line to bitmap
			this._pixels.draw(org.flixel.FlxSprite._gfxSprite);
			this._colorTransformedPixels = null;
			this.calcFrame();
		},
		
		/**
		 * Fills this sprite's graphic with a specific color.
		 * 
		 * @param	Color		The color with which to fill the graphic, format 0xAARRGGBB.
		 */
		"public function fill",function fill(Color/*:uint*/)/*:void*/
		{
			this._pixels.fillRect(this._flashRect2,Color);
			this._colorTransformedPixels = null;
			if(this._pixels != this._framePixels)
				this.calcFrame();
		},
		
		/**
		 * Internal function for updating the sprite's animation.
		 * Useful for cases when you need to update this but are buried down in too many supers.
		 * This function is called automatically by <code>FlxSprite.update()</code>.
		 */
		"protected function updateAnimation",function updateAnimation()/*:void*/
		{
			if(this._bakedRotation)
			{
				var oc/*:uint*/ = this._caf;
				var ta/*:int*/ = this.angle%360;
				if(ta < 0)
					ta += 360;
				this._caf = ta/this._bakedRotation;
				if(oc != this._caf)
					this.calcFrame();
				return;
			}
			if((this._curAnim != null) && (this._curAnim.delay > 0) && (this._curAnim.looped || !this.finished))
			{
				this._frameTimer += org.flixel.FlxG.elapsed;
				while(this._frameTimer > this._curAnim.delay)
				{
					this._frameTimer = this._frameTimer - this._curAnim.delay;
					if(this._curFrame == this._curAnim.frames.length-1)
					{
						if(this._curAnim.looped) this._curFrame = 0;
						this.finished = true;
					}
					else
						this._curFrame++;
					this._caf = this._curAnim.frames[this._curFrame];
					this.calcFrame();
				}
			}
		},
		
		/**
		 * Main game loop update function.  Override this to create your own sprite logic!
		 * Just don't forget to call super.update() or any of the helper functions.
		 */
		"override public function update",function update()/*:void*/
		{
			this.updateMotion();
			this.updateAnimation();
			this.updateFlickering();
		},
		
		/**
		 * Internal function that performs the actual sprite rendering, called by render().
		 */
		"protected function renderSprite",function renderSprite()/*:void*/
		{
			if(org.flixel.FlxG.showBounds != this._boundsVisible)
				this.calcFrame();
			
			this.getScreenXY(this._point);
			this._flashPoint.x = this._point.x;
			this._flashPoint.y = this._point.y;
			
			//Simple render
			if(((this.angle == 0) || (this._bakedRotation > 0)) && (this.scale.x == 1) && (this.scale.y == 1) && (this.blend == null))
			{
				org.flixel.FlxG.buffer.copyPixels(this._framePixels,this._flashRect,this._flashPoint,null,null,true);
				return;
			}
			
			//Advanced render
			this._mtx.identity();
			this._mtx.translate(-this.origin.x,-this.origin.y);
			this._mtx.scale(this.scale.x,this.scale.y);
			if(this.angle != 0)
				this._mtx.rotate(this.angle * 0.017453293);
			this._mtx.translate(this._point.x+this.origin.x,this._point.y+this.origin.y);
			org.flixel.FlxG.buffer.draw(this._framePixels,this._mtx,null,this.blend,null,this.antialiasing);
		},
		
		/**
		 * Called by game loop, updates then blits or renders current frame of animation to the screen
		 */
		"override public function render",function render()/*:void*/
		{
			this.renderSprite();
		},
		
		/**
		 * Checks to see if a point in 2D space overlaps this FlxCore object.
		 * 
		 * @param	X			The X coordinate of the point.
		 * @param	Y			The Y coordinate of the point.
		 * @param	PerPixel	Whether or not to use per pixel collision checking.
		 * 
		 * @return	Whether or not the point overlaps this object.
		 */
		"override public function overlapsPoint",function overlapsPoint(X/*:Number*/,Y/*:Number*/,PerPixel/*:Boolean = false*/)/*:Boolean*/
		{if(arguments.length<3){PerPixel = false;}
			X = X + org.flixel.FlxU.floor(org.flixel.FlxG.scroll.x);
			Y = Y + org.flixel.FlxU.floor(org.flixel.FlxG.scroll.y);
			this.getScreenXY(this._point);
			if(PerPixel)
				return this._framePixels.hitTest(new flash.geom.Point(0,0),0xFF,new flash.geom.Point(X-this._point.x,Y-this._point.y));
			else if((X <= this._point.x) || (X >= this._point.x+this.frameWidth) || (Y <= this._point.y) || (Y >= this._point.y+this.frameHeight))
				return false;
			return true;
		},
		
		/**
		 * Triggered whenever this sprite is launched by a <code>FlxEmitter</code>.
		 */
		"virtual public function onEmit",function onEmit()/*:void*/ { },
		
		/**
		 * Adds a new animation to the sprite.
		 * 
		 * @param	Name		What this animation should be called (e.g. "run").
		 * @param	Frames		An array of numbers indicating what frames to play in what order (e.g. 1, 2, 3).
		 * @param	FrameRate	The speed in frames per second that the animation should play at (e.g. 40 fps).
		 * @param	Looped		Whether or not the animation is looped or just plays once.
		 */
		"public function addAnimation",function addAnimation(Name/*:String*/, Frames/*:Array*/, FrameRate/*:Number=0*/, Looped/*:Boolean=true*/)/*:void*/
		{if(arguments.length<4){if(arguments.length<3){FrameRate=0;}Looped=true;}
			this._animations.push(new org.flixel.data.FlxAnim(Name,Frames,FrameRate,Looped));
		},
		
		/**
		 * Pass in a function to be called whenever this sprite's animation changes.
		 * 
		 * @param	AnimationCallback		A function that has 3 parameters: a string name, a uint frame number, and a uint frame index.
		 */
		"public function addAnimationCallback",function addAnimationCallback(AnimationCallback/*:Function*/)/*:void*/
		{
			this._callback = AnimationCallback;
		},
		
		/**
		 * Plays an existing animation (e.g. "run").
		 * If you call an animation that is already playing it will be ignored.
		 * 
		 * @param	AnimName	The string name of the animation you want to play.
		 * @param	Force		Whether to force the animation to restart.
		 */
		"public function play",function play(AnimName/*:String*/,Force/*:Boolean=false*/)/*:void*/
		{if(arguments.length<2){Force=false;}
			if(!Force && (this._curAnim != null) && (AnimName == this._curAnim.name) && (this._curAnim.looped || !this.finished)) return;
			this._curFrame = 0;
			this._caf = 0;
			this._frameTimer = 0;
			var i/*:uint*/ = 0;
			var al/*:uint*/ = this._animations.length;
			while(i < al)
			{
				if(this._animations[i].name == AnimName)
				{
					this._curAnim = this._animations[i];
					if(this._curAnim.delay <= 0)
						this.finished = true;
					else
						this.finished = false;
					this._caf = this._curAnim.frames[this._curFrame];
					this.calcFrame();
					return;
				}
				i++;
			}
		},

		/**
		 * Tell the sprite to change to a random frame of animation
		 * Useful for instantiating particles or other weird things.
		 */
		"public function randomFrame",function randomFrame()/*:void*/
		{
			this._curAnim = null;
			this._caf = $$int(org.flixel.FlxU.random()*(this._pixels.width/this.frameWidth));
			this.calcFrame();
		},
		
		/**
		 * Tell the sprite to change to a specific frame of animation.
		 * 
		 * @param	Frame	The frame you want to display.
		 */
		"public function get frame",function frame$get()/*:uint*/
		{
			return this._caf;
		},
		
		/**
		 * @private
		 */
		"public function set frame",function frame$set(Frame/*:uint*/)/*:void*/
		{
			this._curAnim = null;
			this._caf = Frame;
			this.calcFrame();
		},
		
		/**
		 * Call this function to figure out the on-screen position of the object.
		 * 
		 * @param	P	Takes a <code>Point</code> object and assigns the post-scrolled X and Y values of this object to it.
		 * 
		 * @return	The <code>Point</code> you passed in, or a new <code>Point</code> if you didn't pass one, containing the screen X and Y position of this object.
		 */
		"override public function getScreenXY",function getScreenXY(Point/*:FlxPoint=null*/)/*:FlxPoint*/
		{if(arguments.length<1){Point=null;}
			if(Point == null) Point = new org.flixel.FlxPoint();
			Point.x = org.flixel.FlxU.floor(this.x + org.flixel.FlxU.roundingError)+org.flixel.FlxU.floor(org.flixel.FlxG.scroll.x*this.scrollFactor.x) - this.offset.x;
			Point.y = org.flixel.FlxU.floor(this.y + org.flixel.FlxU.roundingError)+org.flixel.FlxU.floor(org.flixel.FlxG.scroll.y*this.scrollFactor.y) - this.offset.y;
			return Point;
		},
		
		/**
		 * Internal function to update the current animation frame.
		 */
		"protected function calcFrame",function calcFrame()/*:void*/
		{
			this._boundsVisible = false;
			var rx/*:uint*/ = this._caf*this.frameWidth;
			var ry/*:uint*/ = 0;

			//Handle sprite sheets
			var w/*:uint*/ = this._flipped?this._flipped:this._pixels.width;
			if(rx >= w)
			{
				ry = $$uint(rx/w)*this.frameHeight;
				rx %= w;
			}
			
			//handle reversed sprites
			if(this._flipped && (this._facing == org.flixel.FlxSprite.LEFT))
				rx = (this._flipped<<1)-rx-this.frameWidth;
			
			//Update display bitmap
			this._flashRect.x = rx;
			this._flashRect.y = ry;
			if (this._colorTransformedPixels == null)
			{
				if(this._ct == null)
					this._colorTransformedPixels = this._pixels;
				else {
					this._colorTransformedPixels = new flash.display.BitmapData(this._pixels.width, this._pixels.height, this._pixels.transparent, 0);
					this._colorTransformedPixels.copyPixels(this._pixels, this._pixels.rect, this._flashPointZero);
					this._colorTransformedPixels.colorTransform(this._colorTransformedPixels.rect, this._ct);
				}
			}
			this._framePixels.copyPixels(this._colorTransformedPixels,this._flashRect,this._flashPointZero);
			this._flashRect.x = this._flashRect.y = 0;
			if(org.flixel.FlxG.showBounds)
				this.drawBounds();
			if(this._callback != null) this._callback(this._curAnim.name,this._curFrame,this._caf);
		},
		
		"protected function drawBounds",function drawBounds()/*:void*/
		{
			this._boundsVisible = true;
			if((this._bbb == null) || (this._bbb.width != this.width) || (this._bbb.height != this.height))
				this._bbb = new flash.display.BitmapData(this.width,this.height);
			var bbbc/*:uint*/ = this.getBoundingColor();
			this._bbb.fillRect(this._flashRect,0);
			var ofrw/*:uint*/ = this._flashRect.width;
			var ofrh/*:uint*/ = this._flashRect.height;
			this._flashRect.width = $$int(this.width);
			this._flashRect.height = $$int(this.height);
			this._bbb.fillRect(this._flashRect,bbbc);
			this._flashRect.width = this._flashRect.width - 2;
			this._flashRect.height = this._flashRect.height - 2;
			this._flashRect.x = 1;
			this._flashRect.y = 1;
			this._bbb.fillRect(this._flashRect,0);
			this._flashRect.width = ofrw;
			this._flashRect.height = ofrh;
			this._flashRect.x = this._flashRect.y = 0;
			this._flashPoint.x = $$int(this.offset.x);
			this._flashPoint.y = $$int(this.offset.y);
			this._framePixels.copyPixels(this._bbb,this._flashRect,this._flashPoint,null,null,true);
		},
		
		/**
		 * Internal function, currently only used to quickly update FlxState.screen for post-processing.
		 * Potentially super-unsafe, since it doesn't call <code>resetHelpers()</code>!
		 * 
		 * @param	Pixels		The <code>BitmapData</code> object you want to point at.
		 */
		"internal function unsafeBind",function unsafeBind(Pixels/*:BitmapData*/)/*:void*/
		{
			this._pixels = this._framePixels = Pixels;
			this._colorTransformedPixels = null;
		},
	];},[],["org.flixel.FlxObject","flash.geom.Rectangle","flash.geom.Point","org.flixel.FlxPoint","Array","flash.geom.Matrix","flash.display.Sprite","org.flixel.FlxG","Math","flash.display.BitmapData","uint","org.flixel.FlxU","String","flash.geom.ColorTransform","org.flixel.data.FlxAnim","int"], "0.8.0", "0.8.1"
);