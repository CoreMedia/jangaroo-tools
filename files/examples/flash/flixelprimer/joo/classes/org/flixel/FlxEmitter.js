joo.classLoader.prepare("package org.flixel",/*
{
	import org.flixel.data.FlxParticle*/

	/**
	 * <code>FlxEmitter</code> is a lightweight particle emitter.
	 * It can be used for one-time explosions or for
	 * continuous fx like rain and fire.  <code>FlxEmitter</code>
	 * is not optimized or anything; all it does is launch
	 * <code>FlxSprite</code> objects out at set intervals
	 * by setting their positions and velocities accordingly.
	 * It is easy to use and relatively efficient, since it
	 * automatically redelays its sprites and/or kills
	 * them once they've been launched.
	 */
	"public class FlxEmitter extends org.flixel.FlxGroup",5,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		/**
		 * The minimum possible velocity of a particle.
		 * The default value is (-100,-100).
		 */
		"public var",{ minParticleSpeed/*:FlxPoint*/:null},
		/**
		 * The maximum possible velocity of a particle.
		 * The default value is (100,100).
		 */
		"public var",{ maxParticleSpeed/*:FlxPoint*/:null},
		/**
		 * The X and Y drag component of particles launched from the emitter.
		 */
		"public var",{ particleDrag/*:FlxPoint*/:null},
		/**
		 * The minimum possible angular velocity of a particle.  The default value is -360.
		 * NOTE: rotating particles are more expensive to draw than non-rotating ones!
		 */
		"public var",{ minRotation/*:Number*/:NaN},
		/**
		 * The maximum possible angular velocity of a particle.  The default value is 360.
		 * NOTE: rotating particles are more expensive to draw than non-rotating ones!
		 */
		"public var",{ maxRotation/*:Number*/:NaN},
		/**
		 * Sets the <code>acceleration.y</code> member of each particle to this value on launch.
		 */
		"public var",{ gravity/*:Number*/:NaN},
		/**
		 * Determines whether the emitter is currently emitting particles.
		 */
		"public var",{ on/*:Boolean*/:false},
		/**
		 * This variable has different effects depending on what kind of emission it is.
		 * During an explosion, delay controls the lifespan of the particles.
		 * During normal emission, delay controls the time between particle launches.
		 * NOTE: In older builds, polarity (negative numbers) was used to define emitter behavior.
		 * THIS IS NO LONGER THE CASE!  FlxEmitter.start() controls that now!
		 */
		"public var",{ delay/*:Number*/:NaN},
		/**
		 * The number of particles to launch at a time.
		 */
		"public var",{ quantity/*:uint*/:0},
		/**
		 * Checks whether you already fired a particle this frame.
		 */
		"public var",{ justEmitted/*:Boolean*/:false},
		/**
		 * The style of particle emission (all at once, or one at a time).
		 */
		"protected var",{ _explode/*:Boolean*/:false},
		/**
		 * Internal helper for deciding when to launch particles or kill them.
		 */
		"protected var",{ _timer/*:Number*/:NaN},
		/**
		 * Internal marker for where we are in <code>_sprites</code>.
		 */
		"protected var",{ _particle/*:uint*/:0},
		/**
		 * Internal counter for figuring out how many particles to launch.
		 */
		"protected var",{ _counter/*:uint*/:0},
		
		/**
		 * Creates a new <code>FlxEmitter</code> object at a specific position.
		 * Does not automatically generate or attach particles!
		 * 
		 * @param	X			The X position of the emitter.
		 * @param	Y			The Y position of the emitter.
		 */
		"public function FlxEmitter",function FlxEmitter$(X/*:Number=0*/, Y/*:Number=0*/)
		{if(arguments.length<2){if(arguments.length<1){X=0;}Y=0;}
			this.super$5();
			
			this.x = X;
			this.y = Y;
			this.width = 0;
			this.height = 0;
			
			this.minParticleSpeed = new org.flixel.FlxPoint(-100,-100);
			this.maxParticleSpeed = new org.flixel.FlxPoint(100,100);
			this.minRotation = -360;
			this.maxRotation = 360;
			this.gravity = 400;
			this.particleDrag = new org.flixel.FlxPoint();
			this.delay = 0;
			this.quantity = 0;
			this._counter = 0;
			this._explode = true;
			this.exists = false;
			this.on = false;
			this.justEmitted = false;
		},
		
		/**
		 * This function generates a new array of sprites to attach to the emitter.
		 * 
		 * @param	Graphics		If you opted to not pre-configure an array of FlxSprite objects, you can simply pass in a particle image or sprite sheet.
		 * @param	Quantity		The number of particles to generate when using the "create from image" option.
		 * @param	BakedRotations	How many frames of baked rotation to use (boosts performance).  Set to zero to not use baked rotations.
		 * @param	Multiple		Whether the image in the Graphics param is a single particle or a bunch of particles (if it's a bunch, they need to be square!).
		 * @param	Collide			Whether the particles should be flagged as not 'dead' (non-colliding particles are higher performance).  0 means no collisions, 0-1 controls scale of particle's bounding box.
		 * @param	Bounce			Whether the particles should bounce after colliding with things.  0 means no bounce, 1 means full reflection.
		 * 
		 * @return	This FlxEmitter instance (nice for chaining stuff together, if you're into that).
		 */
		"public function createSprites",function createSprites(Graphics/*:Class*/, Quantity/*:uint=50*/, BakedRotations/*:uint=16*/, Multiple/*:Boolean=true*/, Collide/*:Number=0*/, Bounce/*:Number=0*/)/*:FlxEmitter*/
		{if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){Quantity=50;}BakedRotations=16;}Multiple=true;}Collide=0;}Bounce=0;}
			this.members = new Array();
			var r/*:uint*/;
			var s/*:FlxSprite*/;
			var tf/*:uint*/ = 1;
			var sw/*:Number*/;
			var sh/*:Number*/;
			if(Multiple)
			{
				s = new org.flixel.FlxSprite();
				s.loadGraphic(Graphics,true);
				tf = s.frames;
			}
			var i/*:uint*/ = 0;
			while(i < Quantity)
			{
				if((Collide > 0) && (Bounce > 0))
					s =as( new org.flixel.data.FlxParticle(Bounce),  org.flixel.FlxSprite);
				else
					s = new org.flixel.FlxSprite();
				if(Multiple)
				{
					r = $$uint(org.flixel.FlxU.random()*tf);
					if(BakedRotations > 0)
						s.loadRotatedGraphic(Graphics,BakedRotations,r);
					else
					{
						s.loadGraphic(Graphics,true);
						s.frame = r;
					}
				}
				else
				{
					if(BakedRotations > 0)
						s.loadRotatedGraphic(Graphics,BakedRotations);
					else
						s.loadGraphic(Graphics);
				}
				if(Collide > 0)
				{
					sw = s.width;
					sh = s.height;
					s.width *= Collide;
					s.height *= Collide;
					s.offset.x = (sw-s.width)/2;
					s.offset.y = (sh-s.height)/2;
					s.solid = true;
				}
				else
					s.solid = false;
				s.exists = false;
				s.scrollFactor = this.scrollFactor;
				this.add(s);
				i++;
			}
			return this;
		},
		
		/**
		 * A more compact way of setting the width and height of the emitter.
		 * 
		 * @param	Width	The desired width of the emitter (particles are spawned randomly within these dimensions).
		 * @param	Height	The desired height of the emitter.
		 */
		"public function setSize",function setSize(Width/*:uint*/,Height/*:uint*/)/*:void*/
		{
			this.width = Width;
			this.height = Height;
		},
		
		/**
		 * A more compact way of setting the X velocity range of the emitter.
		 * 
		 * @param	Min		The minimum value for this range.
		 * @param	Max		The maximum value for this range.
		 */
		"public function setXSpeed",function setXSpeed(Min/*:Number=0*/,Max/*:Number=0*/)/*:void*/
		{if(arguments.length<2){if(arguments.length<1){Min=0;}Max=0;}
			this.minParticleSpeed.x = Min;
			this.maxParticleSpeed.x = Max;
		},
		
		/**
		 * A more compact way of setting the Y velocity range of the emitter.
		 * 
		 * @param	Min		The minimum value for this range.
		 * @param	Max		The maximum value for this range.
		 */
		"public function setYSpeed",function setYSpeed(Min/*:Number=0*/,Max/*:Number=0*/)/*:void*/
		{if(arguments.length<2){if(arguments.length<1){Min=0;}Max=0;}
			this.minParticleSpeed.y = Min;
			this.maxParticleSpeed.y = Max;
		},
		
		/**
		 * A more compact way of setting the angular velocity constraints of the emitter.
		 * 
		 * @param	Min		The minimum value for this range.
		 * @param	Max		The maximum value for this range.
		 */
		"public function setRotation",function setRotation(Min/*:Number=0*/,Max/*:Number=0*/)/*:void*/
		{if(arguments.length<2){if(arguments.length<1){Min=0;}Max=0;}
			this.minRotation = Min;
			this.maxRotation = Max;
		},
		
		/**
		 * Internal function that actually performs the emitter update (called by update()).
		 */
		"protected function updateEmitter",function updateEmitter()/*:void*/
		{
			if(this._explode)
			{
				this._timer += org.flixel.FlxG.elapsed;
				if((this.delay > 0) && (this._timer > this.delay))
				{
					this.kill();
					return;
				}
				if(this.on)
				{
					this.on = false;
					var i/*:uint*/ = this._particle;
					var l/*:uint*/ = this.members.length;
					if(this.quantity > 0)
						l = this.quantity;
					l += this._particle;
					while(i < l)
					{
						this.emitParticle();
						i++;
					}
				}
				return;
			}
			if(!this.on)
				return;
			this._timer += org.flixel.FlxG.elapsed;
			while((this._timer > this.delay) && ((this.quantity <= 0) || (this._counter < this.quantity)))
			{
				this._timer -= this.delay;
				this.emitParticle();
			}
		},
		
		/**
		 * Internal function that actually goes through and updates all the group members.
		 * Overridden here to remove the position update code normally used by a FlxGroup.
		 */
		"override protected function updateMembers",function updateMembers()/*:void*/
		{
			var o/*:FlxObject*/;
			var i/*:uint*/ = 0;
			var l/*:uint*/ = this.members.length;
			while(i < l)
			{
				o =as( this.members[i++],  org.flixel.FlxObject);
				if((o != null) && o.exists && o.active)
					o.update();
			}
		},
		
		/**
		 * Called automatically by the game loop, decides when to launch particles and when to "die".
		 */
		"override public function update",function update()/*:void*/
		{
			this.justEmitted = false;
			this.update$5();
			this.updateEmitter();
		},
		
		/**
		 * Call this function to start emitting particles.
		 * 
		 * @param	Explode		Whether the particles should all burst out at once.
		 * @param	Delay		You can set the delay (or lifespan) here if you want.
		 * @param	Quantity	How many particles to launch.  Default value is 0, or "all the particles".
		 */
		"public function start",function start(Explode/*:Boolean=true*/,Delay/*:Number=0*/,Quantity/*:uint=0*/)/*:void*/
		{if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){Explode=true;}Delay=0;}Quantity=0;}
			if(this.members.length <= 0)
			{
				org.flixel.FlxG.log("WARNING: there are no sprites loaded in your emitter.\nAdd some to FlxEmitter.members or use FlxEmitter.createSprites().");
				return;
			}
			this._explode = Explode;
			if(!this._explode)
				this._counter = 0;
			if(!this.exists)
				this._particle = 0;
			this.exists = true;
			this.visible = true;
			this.active = true;
			this.dead = false;
			this.on = true;
			this._timer = 0;
			if(this.quantity == 0)
				this.quantity = Quantity;
			else if(Quantity != 0)
				this.quantity = Quantity;
			if(Delay != 0)
				this.delay = Delay;
			if(this.delay < 0)
				this.delay = -this.delay;
			if(this.delay == 0)
			{
				if(Explode)
					this.delay = 3;	//default value for particle explosions
				else
					this.delay = 0.1;//default value for particle streams
			}
		},
		
		/**
		 * This function can be used both internally and externally to emit the next particle.
		 */
		"public function emitParticle",function emitParticle()/*:void*/
		{
			this._counter++;
			var s/*:FlxSprite*/ =as( this.members[this._particle],  org.flixel.FlxSprite);
			s.visible = true;
			s.exists = true;
			s.active = true;
			s.x = this.x - (s.width>>1) + org.flixel.FlxU.random()*this.width;
			s.y = this.y - (s.height>>1) + org.flixel.FlxU.random()*this.height;
			s.velocity.x = this.minParticleSpeed.x;
			if(this.minParticleSpeed.x != this.maxParticleSpeed.x) s.velocity.x += org.flixel.FlxU.random()*(this.maxParticleSpeed.x-this.minParticleSpeed.x);
			s.velocity.y = this.minParticleSpeed.y;
			if(this.minParticleSpeed.y != this.maxParticleSpeed.y) s.velocity.y += org.flixel.FlxU.random()*(this.maxParticleSpeed.y-this.minParticleSpeed.y);
			s.acceleration.y = this.gravity;
			s.angularVelocity = this.minRotation;
			if(this.minRotation != this.maxRotation) s.angularVelocity += org.flixel.FlxU.random()*(this.maxRotation-this.minRotation);
			if(s.angularVelocity != 0) s.angle = org.flixel.FlxU.random()*360-180;
			s.drag.x = this.particleDrag.x;
			s.drag.y = this.particleDrag.y;
			this._particle++;
			if(this._particle >= this.members.length)
				this._particle = 0;
			s.onEmit();
			this.justEmitted = true;
		},
		
		/**
		 * Call this function to stop the emitter without killing it.
		 * 
		 * @param	Delay	How long to wait before killing all the particles.  Set to 'zero' to never kill them.
		 */
		"public function stop",function stop(Delay/*:Number=3*/)/*:void*/
		{if(arguments.length<1){Delay=3;}
			this._explode = true;
			this.delay = Delay;
			if(this.delay < 0)
				this.delay = -Delay;
			this.on = false;
		},
		
		/**
		 * Change the emitter's position to the origin of a <code>FlxObject</code>.
		 * 
		 * @param	Object		The <code>FlxObject</code> that needs to spew particles.
		 */
		"public function at",function at(Object/*:FlxObject*/)/*:void*/
		{
			this.x = Object.x + Object.origin.x;
			this.y = Object.y + Object.origin.y;
		},
		
		/**
		 * Call this function to turn off all the particles and the emitter.
		 */
		"override public function kill",function kill()/*:void*/
		{
			this.kill$5();
			this.on = false;
		},
	];},[],["org.flixel.FlxGroup","org.flixel.FlxPoint","Array","org.flixel.FlxSprite","org.flixel.data.FlxParticle","uint","org.flixel.FlxU","org.flixel.FlxG","org.flixel.FlxObject"], "0.8.0", "0.8.3"
);