joo.classLoader.prepare("package org.flixel",/*
{
	import flash.geom.Point*/
	
	/**
	 * This is the base class for most of the display objects (<code>FlxSprite</code>, <code>FlxText</code>, etc).
	 * It includes some basic attributes about game objects, including retro-style flickering,
	 * basic state information, sizes, scrolling, and basic physics & motion.
	 */
	"public class FlxObject extends org.flixel.FlxRect",3,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxU,org.flixel.FlxG);},
	
		/**
		 * Kind of a global on/off switch for any objects descended from <code>FlxObject</code>.
		 */
		"public var",{ exists/*:Boolean*/:false},
		/**
		 * If an object is not alive, the game loop will not automatically call <code>update()</code> on it.
		 */
		"public var",{ active/*:Boolean*/:false},
		/**
		 * If an object is not visible, the game loop will not automatically call <code>render()</code> on it.
		 */
		"public var",{ visible/*:Boolean*/:false},
		/**
		 * Internal tracker for whether or not the object collides (see <code>solid</code>).
		 */
		"protected var",{ _solid/*:Boolean*/:false},
		/**
		 * Internal tracker for whether an object will move/alter position after a collision (see <code>fixed</code>).
		 */
		"protected var",{ _fixed/*:Boolean*/:false},
		
		/**
		 * The basic speed of this object.
		 */
		"public var",{ velocity/*:FlxPoint*/:null},
		/**
		 * How fast the speed of this object is changing.
		 * Useful for smooth movement and gravity.
		 */
		"public var",{ acceleration/*:FlxPoint*/:null},
		/**
		 * This isn't drag exactly, more like deceleration that is only applied
		 * when acceleration is not affecting the sprite.
		 */
		"public var",{ drag/*:FlxPoint*/:null},
		/**
		 * If you are using <code>acceleration</code>, you can use <code>maxVelocity</code> with it
		 * to cap the speed automatically (very useful!).
		 */
		"public var",{ maxVelocity/*:FlxPoint*/:null},
		/**
		 * Set the angle of a sprite to rotate it.
		 * WARNING: rotating sprites decreases rendering
		 * performance for this sprite by a factor of 10x!
		 */
		"public var",{ angle/*:Number*/:NaN},
		/**
		 * This is how fast you want this sprite to spin.
		 */
		"public var",{ angularVelocity/*:Number*/:NaN},
		/**
		 * How fast the spin speed should change.
		 */
		"public var",{ angularAcceleration/*:Number*/:NaN},
		/**
		 * Like <code>drag</code> but for spinning.
		 */
		"public var",{ angularDrag/*:Number*/:NaN},
		/**
		 * Use in conjunction with <code>angularAcceleration</code> for fluid spin speed control.
		 */
		"public var",{ maxAngular/*:Number*/:NaN},
		/**
		 * WARNING: The origin of the sprite will default to its center.
		 * If you change this, the visuals and the collisions will likely be
		 * pretty out-of-sync if you do any rotation.
		 */
		"public var",{ origin/*:FlxPoint*/:null},
		/**
		 * If you want to do Asteroids style stuff, check out thrust,
		 * instead of directly accessing the object's velocity or acceleration.
		 */
		"public var",{ thrust/*:Number*/:NaN},
		/**
		 * Used to cap <code>thrust</code>, helpful and easy!
		 */
		"public var",{ maxThrust/*:Number*/:NaN},
		/**
		 * A handy "empty point" object
		 */
		"static protected const",{ _pZero/*:FlxPoint*/ :function(){return( new org.flixel.FlxPoint());}},
		
		/**
		 * A point that can store numbers from 0 to 1 (for X and Y independently)
		 * that governs how much this object is affected by the camera subsystem.
		 * 0 means it never moves, like a HUD element or far background graphic.
		 * 1 means it scrolls along a the same speed as the foreground layer.
		 * scrollFactor is initialized as (1,1) by default.
		 */
		"public var",{ scrollFactor/*:FlxPoint*/:null},
		/**
		 * Internal helper used for retro-style flickering.
		 */
		"protected var",{ _flicker/*:Boolean*/:false},
		/**
		 * Internal helper used for retro-style flickering.
		 */
		"protected var",{ _flickerTimer/*:Number*/:NaN},
		/**
		 * Handy for storing health percentage or armor points or whatever.
		 */
		"public var",{ health/*:Number*/:NaN},
		/**
		 * Handy for tracking gameplay or animations.
		 */
		"public var",{ dead/*:Boolean*/:false},
		/**
		 * This is just a pre-allocated x-y point container to be used however you like
		 */
		"protected var",{ _point/*:FlxPoint*/:null},
		/**
		 * This is just a pre-allocated rectangle container to be used however you like
		 */
		"protected var",{ _rect/*:FlxRect*/:null},
		/**
		 * This is a pre-allocated Flash Point object, which is useful for certain Flash graphics API calls
		 */
		"protected var",{ _flashPoint/*:Point*/:null},
		/**
		 * Set this to false if you want to skip the automatic motion/movement stuff (see <code>updateMotion()</code>).
		 * FlxObject and FlxSprite default to true.
		 * FlxText, FlxTileblock, FlxTilemap and FlxSound default to false.
		 */
		"public var",{ moves/*:Boolean*/:false},
		/**
		 * These store a couple of useful numbers for speeding up collision resolution.
		 */
		"public var",{ colHullX/*:FlxRect*/:null},
		/**
		 * These store a couple of useful numbers for speeding up collision resolution.
		 */
		"public var",{ colHullY/*:FlxRect*/:null},
		/**
		 * These store a couple of useful numbers for speeding up collision resolution.
		 */
		"public var",{ colVector/*:FlxPoint*/:null},
		/**
		 * An array of <code>FlxPoint</code> objects.  By default contains a single offset (0,0).
		 */
		"public var",{ colOffsets/*:Array*/:null},
		/**
		 * Dedicated internal flag for whether or not this class is a FlxGroup.
		 */
		"internal var",{ _group/*:Boolean*/:false},
		/**
		 * Flag that indicates whether or not you just hit the floor.
		 * Primarily useful for platformers, this flag is reset during the <code>updateMotion()</code>.
		 */
		"public var",{ onFloor/*:Boolean*/:false},
		/**
		 * Flag for direction collision resolution.
		 */
		"public var",{ collideLeft/*:Boolean*/:false},
		/**
		 * Flag for direction collision resolution.
		 */
		"public var",{ collideRight/*:Boolean*/:false},
		/**
		 * Flag for direction collision resolution.
		 */
		"public var",{ collideTop/*:Boolean*/:false},
		/**
		 * Flag for direction collision resolution.
		 */
		"public var",{ collideBottom/*:Boolean*/:false},
		
		/**
		 * Creates a new <code>FlxObject</code>.
		 * 
		 * @param	X		The X-coordinate of the point in space.
		 * @param	Y		The Y-coordinate of the point in space.
		 * @param	Width	Desired width of the rectangle.
		 * @param	Height	Desired height of the rectangle.
		 */
		"public function FlxObject",function FlxObject$(X/*:Number=0*/, Y/*:Number=0*/, Width/*:Number=0*/, Height/*:Number=0*/)
		{switch(arguments.length){case 0:X=0;case 1:Y=0;case 2:Width=0;case 3:Height=0;}
			this.super$3(X,Y,Width,Height);
			
			this.exists = true;
			this.active = true;
			this.visible = true;
			this._solid = true;
			this._fixed = false;
			this.moves = true;
			
			this.collideLeft = true;
			this.collideRight = true;
			this.collideTop = true;
			this.collideBottom = true;
			
			this.origin = new org.flixel.FlxPoint();

			this.velocity = new org.flixel.FlxPoint();
			this.acceleration = new org.flixel.FlxPoint();
			this.drag = new org.flixel.FlxPoint();
			this.maxVelocity = new org.flixel.FlxPoint(10000,10000);
			
			this.angle = 0;
			this.angularVelocity = 0;
			this.angularAcceleration = 0;
			this.angularDrag = 0;
			this.maxAngular = 10000;
			
			this.thrust = 0;
			
			this.scrollFactor = new org.flixel.FlxPoint(1,1);
			this._flicker = false;
			this._flickerTimer = -1;
			this.health = 1;
			this.dead = false;
			this._point = new org.flixel.FlxPoint();
			this._rect = new org.flixel.FlxRect();
			this._flashPoint = new flash.geom.Point();
			
			this.colHullX = new org.flixel.FlxRect();
			this.colHullY = new org.flixel.FlxRect();
			this.colVector = new org.flixel.FlxPoint();
			this.colOffsets = new Array(new org.flixel.FlxPoint());
			this._group = false;
		},
		
		/**
		 * Called by <code>FlxGroup</code>, commonly when game states are changed.
		 */
		"public function destroy",function destroy()/*:void*/
		{
			//Nothing to destroy yet
		},
		
		/**
		 * Set <code>solid</code> to true if you want to collide this object.
		 */
		"public function get solid",function solid$get()/*:Boolean*/
		{
			return this._solid;
		},
		
		/**
		 * @private
		 */
		"public function set solid",function solid$set(Solid/*:Boolean*/)/*:void*/
		{
			this._solid = Solid;
		},
		
		/**
		 * Set <code>fixed</code> to true if you want the object to stay in place during collisions.
		 * Useful for levels and other environmental objects.
		 */
		"public function get fixed",function fixed$get()/*:Boolean*/
		{
			return this._fixed;
		},
		
		/**
		 * @private
		 */
		"public function set fixed",function fixed$set(Fixed/*:Boolean*/)/*:void*/
		{
			this._fixed = Fixed;
		},
		
		/**
		 * Called by <code>FlxObject.updateMotion()</code> and some constructors to
		 * rebuild the basic collision data for this object.
		 */
		"public function refreshHulls",function refreshHulls()/*:void*/
		{
			this.colHullX.x = this.x;
			this.colHullX.y = this.y;
			this.colHullX.width = this.width;
			this.colHullX.height = this.height;
			this.colHullY.x = this.x;
			this.colHullY.y = this.y;
			this.colHullY.width = this.width;
			this.colHullY.height = this.height;
		},
		
		/**
		 * Internal function for updating the position and speed of this object.
		 * Useful for cases when you need to update this but are buried down in too many supers.
		 */
		"protected function updateMotion",function updateMotion()/*:void*/
		{
			if(!this.moves)
				return;
			
			if(this._solid)
				this.refreshHulls();
			this.onFloor = false;
			var vc/*:Number*/;

			vc = (org.flixel.FlxU.computeVelocity(this.angularVelocity,this.angularAcceleration,this.angularDrag,this.maxAngular) - this.angularVelocity)/2;
			this.angularVelocity += vc; 
			this.angle += this.angularVelocity*org.flixel.FlxG.elapsed;
			this.angularVelocity += vc;
			
			var thrustComponents/*:FlxPoint*/;
			if(this.thrust != 0)
			{
				thrustComponents = org.flixel.FlxU.rotatePoint(-this.thrust,0,0,0,this.angle);
				var maxComponents/*:FlxPoint*/ = org.flixel.FlxU.rotatePoint(-this.maxThrust,0,0,0,this.angle);
				var max/*:Number*/ = ((maxComponents.x>0)?maxComponents.x:-maxComponents.x);
				if(max > ((maxComponents.y>0)?maxComponents.y:-maxComponents.y))
					maxComponents.y = max;
				else
					max = ((maxComponents.y>0)?maxComponents.y:-maxComponents.y);
				this.maxVelocity.x = this.maxVelocity.y = ((max>0)?max:-max);
			}
			else
				thrustComponents = org.flixel.FlxObject._pZero;
			
			vc = (org.flixel.FlxU.computeVelocity(this.velocity.x,this.acceleration.x+thrustComponents.x,this.drag.x,this.maxVelocity.x) - this.velocity.x)/2;
			this.velocity.x += vc;
			var xd/*:Number*/ = this.velocity.x*org.flixel.FlxG.elapsed;
			this.velocity.x += vc;
			
			vc = (org.flixel.FlxU.computeVelocity(this.velocity.y,this.acceleration.y+thrustComponents.y,this.drag.y,this.maxVelocity.y) - this.velocity.y)/2;
			this.velocity.y += vc;
			var yd/*:Number*/ = this.velocity.y*org.flixel.FlxG.elapsed;
			this.velocity.y += vc;
			
			this.x += xd;
			this.y += yd;
			
			//Update collision data with new movement results
			if(!this._solid)
				return;
			this.colVector.x = xd;
			this.colVector.y = yd;
			this.colHullX.width += ((this.colVector.x>0)?this.colVector.x:-this.colVector.x);
			if(this.colVector.x < 0)
				this.colHullX.x += this.colVector.x;
			this.colHullY.x = this.x;
			this.colHullY.height += ((this.colVector.y>0)?this.colVector.y:-this.colVector.y);
			if(this.colVector.y < 0)
				this.colHullY.y += this.colVector.y;
		},
		
		/**
		 * Just updates the retro-style flickering.
		 * Considered update logic rather than rendering because it toggles visibility.
		 */
		"protected function updateFlickering",function updateFlickering()/*:void*/
		{
			if(this.flickering())
			{
				if(this._flickerTimer > 0)
				{
					this._flickerTimer = this._flickerTimer - org.flixel.FlxG.elapsed;
					if(this._flickerTimer == 0)
						this._flickerTimer = -1;
				}
				if(this._flickerTimer < 0)
					this.flicker(-1);
				else
				{
					this._flicker = !this._flicker;
					this.visible = !this._flicker;
				}
			}
		},
		
		/**
		 * Called by the main game loop, handles motion/physics and game logic
		 */
		"public function update",function update()/*:void*/
		{
			this.updateMotion();
			this.updateFlickering();
		},
		
		/**
		 * Override this function to draw graphics (see <code>FlxSprite</code>).
		 */
		"public function render",function render()/*:void*/
		{
			//Objects don't have any visual logic/display of their own.
		},
		
		/**
		 * Checks to see if some <code>FlxObject</code> object overlaps this <code>FlxObject</code> object.
		 * 
		 * @param	Object	The object being tested.
		 * 
		 * @return	Whether or not the two objects overlap.
		 */
		"public function overlaps",function overlaps(Object/*:FlxObject*/)/*:Boolean*/
		{
			this.getScreenXY(this._point);
			var tx/*:Number*/ = this._point.x;
			var ty/*:Number*/ = this._point.y;
			Object.getScreenXY(this._point);
			if((this._point.x <= tx-Object.width) || (this._point.x >= tx+this.width) || (this._point.y <= ty-Object.height) || (this._point.y >= ty+this.height))
				return false;
			return true;
		},
		
		/**
		 * Checks to see if a point in 2D space overlaps this <code>FlxObject</code> object.
		 * 
		 * @param	X			The X coordinate of the point.
		 * @param	Y			The Y coordinate of the point.
		 * @param	PerPixel	Whether or not to use per pixel collision checking (only available in <code>FlxSprite</code> subclass).
		 * 
		 * @return	Whether or not the point overlaps this object.
		 */
		"public function overlapsPoint",function overlapsPoint(X/*:Number*/,Y/*:Number*/,PerPixel/*:Boolean = false*/)/*:Boolean*/
		{switch(arguments.length){case 0:case 1:case 2:PerPixel = false;}
			X = X + org.flixel.FlxU.floor(org.flixel.FlxG.scroll.x);
			Y = Y + org.flixel.FlxU.floor(org.flixel.FlxG.scroll.y);
			this.getScreenXY(this._point);
			if((X <= this._point.x) || (X >= this._point.x+this.width) || (Y <= this._point.y) || (Y >= this._point.y+this.height))
				return false;
			return true;
		},
		
		/**
		 * If you don't want to call <code>FlxU.collide()</code> you can use this instead.
		 * Just calls <code>FlxU.collide(this,Object);</code>.  Will collide against itself
		 * if Object==null.
		 * 
		 * @param	Object		The <FlxObject> you want to collide with.
		 */
		"public function collide",function collide(Object/*:FlxObject=null*/)/*:Boolean*/
		{switch(arguments.length){case 0:Object=null;}
			return org.flixel.FlxU.collide(this,((Object==null)?this:Object));
		},
		
		/**
		 * <code>FlxU.collide()</code> (and thus <code>FlxObject.collide()</code>) call
		 * this function each time two objects are compared to see if they collide.
		 * It doesn't necessarily mean these objects WILL collide, however.
		 * 
		 * @param	Object	The <code>FlxObject</code> you're about to run into.
		 */
		"public function preCollide",function preCollide(Object/*:FlxObject*/)/*:void*/
		{
			//Most objects don't have to do anything here.
		},
		
		/**
		 * Called when this object's left side collides with another <code>FlxObject</code>'s right.
		 * NOTE: by default this function just calls <code>hitSide()</code>.
		 * 
		 * @param	Contact		The <code>FlxObject</code> you just ran into.
		 * @param	Velocity	The suggested new velocity for this object.
		 */
		"public function hitLeft",function hitLeft(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/
		{
			this.hitSide(Contact,Velocity);
		},
		
		/**
		 * Called when this object's right side collides with another <code>FlxObject</code>'s left.
		 * NOTE: by default this function just calls <code>hitSide()</code>.
		 * 
		 * @param	Contact		The <code>FlxObject</code> you just ran into.
		 * @param	Velocity	The suggested new velocity for this object.
		 */
		"public function hitRight",function hitRight(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/
		{
			this.hitSide(Contact,Velocity);
		},
		
		/**
		 * Since most games have identical behavior for running into walls,
		 * you can just override this function instead of overriding both hitLeft and hitRight. 
		 * 
		 * @param	Contact		The <code>FlxObject</code> you just ran into.
		 * @param	Velocity	The suggested new velocity for this object.
		 */
		"public function hitSide",function hitSide(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/
		{
			if(!this.fixed || (Contact.fixed && ((this.velocity.y != 0) || (this.velocity.x != 0))))
				this.velocity.x = Velocity;
		},
		
		/**
		 * Called when this object's top collides with the bottom of another <code>FlxObject</code>.
		 * 
		 * @param	Contact		The <code>FlxObject</code> you just ran into.
		 * @param	Velocity	The suggested new velocity for this object.
		 */
		"public function hitTop",function hitTop(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/
		{
			if(!this.fixed || (Contact.fixed && ((this.velocity.y != 0) || (this.velocity.x != 0))))
				this.velocity.y = Velocity;
		},
		
		/**
		 * Called when this object's bottom edge collides with the top of another <code>FlxObject</code>.
		 * 
		 * @param	Contact		The <code>FlxObject</code> you just ran into.
		 * @param	Velocity	The suggested new velocity for this object.
		 */
		"public function hitBottom",function hitBottom(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/
		{
			this.onFloor = true;
			if(!this.fixed || (Contact.fixed && ((this.velocity.y != 0) || (this.velocity.x != 0))))
				this.velocity.y = Velocity;
		},
		
		/**
		 * Call this function to "damage" (or give health bonus) to this sprite.
		 * 
		 * @param	Damage		How much health to take away (use a negative number to give a health bonus).
		 */
		"virtual public function hurt",function hurt(Damage/*:Number*/)/*:void*/
		{
			this.health = this.health - Damage;
			if(this.health <= 0)
				this.kill();
		},
		
		/**
		 * Call this function to "kill" a sprite so that it no longer 'exists'.
		 */
		"public function kill",function kill()/*:void*/
		{
			this.exists = false;
			this.dead = true;
		},
		
		/**
		 * Tells this object to flicker, retro-style.
		 * 
		 * @param	Duration	How many seconds to flicker for.
		 */
		"public function flicker",function flicker(Duration/*:Number=1*/)/*:void*/ {switch(arguments.length){case 0:Duration=1;} this._flickerTimer = Duration; if(this._flickerTimer < 0) { this._flicker = false; this.visible = true; } },
		
		/**
		 * Check to see if the object is still flickering.
		 * 
		 * @return	Whether the object is flickering or not.
		 */
		"public function flickering",function flickering()/*:Boolean*/ { return this._flickerTimer >= 0; },
		
		/**
		 * Call this function to figure out the on-screen position of the object.
		 * 
		 * @param	P	Takes a <code>Point</code> object and assigns the post-scrolled X and Y values of this object to it.
		 * 
		 * @return	The <code>Point</code> you passed in, or a new <code>Point</code> if you didn't pass one, containing the screen X and Y position of this object.
		 */
		"public function getScreenXY",function getScreenXY(Point/*:FlxPoint=null*/)/*:FlxPoint*/
		{switch(arguments.length){case 0:Point=null;}
			if(Point == null) Point = new org.flixel.FlxPoint();
			Point.x = org.flixel.FlxU.floor(this.x + org.flixel.FlxU.roundingError)+org.flixel.FlxU.floor(org.flixel.FlxG.scroll.x*this.scrollFactor.x);
			Point.y = org.flixel.FlxU.floor(this.y + org.flixel.FlxU.roundingError)+org.flixel.FlxU.floor(org.flixel.FlxG.scroll.y*this.scrollFactor.y);
			return Point;
		},
		
		/**
		 * Check and see if this object is currently on screen.
		 * 
		 * @return	Whether the object is on screen or not.
		 */
		"public function onScreen",function onScreen()/*:Boolean*/
		{
			this.getScreenXY(this._point);
			if((this._point.x + this.width < 0) || (this._point.x > org.flixel.FlxG.width) || (this._point.y + this.height < 0) || (this._point.y > org.flixel.FlxG.height))
				return false;
			return true;
		},
		
		/**
		 * Handy function for reviving game objects.
		 * Resets their existence flags and position, including LAST position.
		 * 
		 * @param	X	The new X position of this object.
		 * @param	Y	The new Y position of this object.
		 */
		"public function reset",function reset(X/*:Number*/,Y/*:Number*/)/*:void*/
		{
			this.x = X;
			this.y = Y;
			this.exists = true;
			this.dead = false;
		},
		
		/**
		 * Returns the appropriate color for the bounding box depending on object state.
		 */
		"public function getBoundingColor",function getBoundingColor()/*:uint*/
		{
			if(this.solid)
			{
				if(this.fixed)
					return 0x7f00f225;
				else
					return 0x7fff0012;
			}
			else
				return 0x7f0090e9;
		},
	];},[],["org.flixel.FlxRect","org.flixel.FlxPoint","flash.geom.Point","Array","org.flixel.FlxU","org.flixel.FlxG"], "0.8.0", "0.8.2-SNAPSHOT"
);