joo.classLoader.prepare("package com.chipacabra.Jumper",/*
{
	import org.flixel.FlxEmitter
	import org.flixel.FlxG
	import org.flixel.FlxGroup
	import org.flixel.FlxObject
	import org.flixel.FlxSprite*/
	
	/**
	 * ...
	 * @author David Bell
	 **/
	"public class Player extends org.flixel.FlxSprite",5,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxG);}, 
	
		{Embed:{source : 'com/chipacabra/Jumper/../../../../art/lizardhead3.png'}},"public var",{ lizardhead/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../sounds/death.mp3'}},"public var",{ sndDeath/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../sounds/jump.mp3'}},"public var",{ sndJump/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../sounds/shoot2.mp3'}},"public var",{ sndShoot/*:Class*/:null}, 
		
		"protected static const",{ RUN_SPEED/*:int*/ : 90},
		"protected static const",{ GRAVITY/*:int*/ :620},
		"protected static const",{ JUMP_SPEED/*:int*/ : 250},
		"protected static const",{ BULLET_SPEED/*:int*/ : 200},
		"protected static const",{ GUN_DELAY/*:Number*/ : .4},
		
		
		"protected var",{ _gibs/*:FlxEmitter*/:null},
		"protected var",{ _bullets/*:FlxGroup*/:null},
		"protected var",{ _blt/*:Bullet*/:null},
		"protected var",{ _cooldown/*:Number*/:NaN},
		"protected var",{ _parent/*:**/:undefined},
		"protected var",{ _onladder/*:Boolean*/:false},
		
		"private var",{ _jump/*:Number*/:NaN},
		"private var",{ _canDJump/*:Boolean*/:false},
		"private var",{ _xgridleft/*:int*/:0},
		"private var",{ _xgridright/*:int*/:0},
		"private var",{ _ygrid/*:int*/:0},
		
		"public var",{ climbing/*:Boolean*/:false},
		
		"public function Player",function Player$(X/*:int*/,Y/*:int*/,Parent/*:**/,Gibs/*:FlxEmitter*/,Bullets/*:FlxGroup*/)/*:void*/ // X,Y: Starting coordinates
		{
			this.super$5(X, Y);
			
			this._bullets = Bullets;
			
			this.loadGraphic(this.lizardhead, true, true,16,20);  //Set up the graphics
			this.addAnimation("walking", [0,1, 2,3], 12, true);
			this.addAnimation("idle", [3]);
			this.addAnimation("jump", [2]);
			
			this.drag.x = com.chipacabra.Jumper.Player.RUN_SPEED * 8;
			this.drag.y = com.chipacabra.Jumper.Player.RUN_SPEED*8;
			this.acceleration.y = com.chipacabra.Jumper.Player.GRAVITY;
			this.maxVelocity.x = com.chipacabra.Jumper.Player.RUN_SPEED;
			this.maxVelocity.y = com.chipacabra.Jumper.Player.JUMP_SPEED;
			this.height = 16;
			this.offset.y = 4;
			this.width = 12;
			this.offset.x = 3;
			
			this._cooldown = com.chipacabra.Jumper.Player.GUN_DELAY; // Initialize the cooldown so that helmutguy can shoot right away.
			this._gibs = Gibs;
			this._parent = Parent;  // This is so we can look at properties of the playstate's tilemaps
			this._jump$5 = 0;
			this._onladder = false;
			
			this.climbing = false; // just to make sure it never gets caught undefined. That would be embarassing.
			
		},
		
		"public override function update",function update()/*:void*/
		{
			
			this.acceleration.x = 0; //Reset to 0 when no button is pushed
			
			if (this.climbing) this.acceleration.y = 0;  // Stop falling if you're climbing a ladder
			else this.acceleration.y = com.chipacabra.Jumper.Player.GRAVITY;
			
			if (org.flixel.FlxG.keys.LEFT)
			{
				this.facing = org.flixel.FlxSprite.LEFT; 
				this.acceleration.x = -this.drag.x;
			}
			else if (org.flixel.FlxG.keys.RIGHT)
			{
				this.facing = org.flixel.FlxSprite.RIGHT;
				this.acceleration.x = this.drag.x;				
			}
			
			// Climbing
			if (org.flixel.FlxG.keys.UP)
			{
				if (this._onladder) 
				{
					this.climbing = true;
					this._canDJump$5 = true;
				}
				if (this.climbing && (this._parent.ladders.getTile(this._xgridleft$5, this._ygrid$5-1))) this.velocity.y = -com.chipacabra.Jumper.Player.RUN_SPEED;
			}
			if (org.flixel.FlxG.keys.DOWN) 
			{
				if (this._onladder) 
				{
					this.climbing = true;
					this._canDJump$5 = true;
				}
				if (this.climbing) this.velocity.y = com.chipacabra.Jumper.Player.RUN_SPEED;
			}
			
			if (org.flixel.FlxG.keys.justPressed("C"))
			{
				if (this.climbing)
				{
					this._jump$5 = 0;
					this.climbing = false;
					org.flixel.FlxG.play(this.sndJump, 1, false, 50);
				}
				if (!this.velocity.y)
					org.flixel.FlxG.play(this.sndJump, 1, false, 50);
			}
			
			if (org.flixel.FlxG.keys.justPressed("C") && (this.velocity.y > 0) && this._canDJump$5==true)
			{
				org.flixel.FlxG.play(this.sndJump, 1, false, 50);
				this._jump$5 = 0;
				this._canDJump$5 = false;
			}
			
            if((this._jump$5 >= 0) && (org.flixel.FlxG.keys.C)) //You can also use space or any other key you want
            {
                this.climbing = false;
				this._jump$5 += org.flixel.FlxG.elapsed;
                if(this._jump$5 > 0.25) this._jump$5 = -1; //You can't jump for more than 0.25 seconds
            }
            else this._jump$5 = -1;
 
            if (this._jump$5 > 0)
            {
                if(this._jump$5 < 0.035)   // this number is how long before a short slow jump shifts to a faster, high jump
                    this.velocity.y = -.6 * this.maxVelocity.y; //This is the minimum height of the jump
					
				else 
					this.velocity.y = -.8 * this.maxVelocity.y;
            }
			//Shooting
			if (org.flixel.FlxG.keys.X)
			{
				this.shoot$5();  //Let's put the shooting code in its own function to keep things organized
			}
			//Animation

			if (this.velocity.x > 0 || this.velocity.x <0 ) { this.play("walking"); }
			else if (!this.velocity.x) { this.play("idle"); }
			if (this.velocity.y<0) { this.play("jump"); }
			
			this._cooldown += org.flixel.FlxG.elapsed;
			
			// Don't let helmuguy walk off the edge of the map
			if (this.x <= 0)
			this.x = 0;
			if ((this.x+this.width) > this._parent.map.width)
			this.x = this._parent.map.width - this.width;
			
			this._xgridleft$5 = $$int((this.x +3) / 16);   // Convert pixel positions to grid positions. int and floor are functionally the same, 
			this._xgridright$5 = $$int((this.x+this.width - 3) / 16);
			this._ygrid$5 = $$int((this.y+this.height-1) / 16);   // but I hear int is faster so let's go with that.
			
			if (this._parent.ladders.getTile(this._xgridleft$5,this._ygrid$5)&&this._parent.ladders.getTile(this._xgridright$5,this._ygrid$5)) {this._onladder = true;}
			else 
			{
				this._onladder = false;
				this.climbing = false;
			}
			if (this.climbing)
				{
					this.collideTop = false;
					this.collideBottom = false;
				}
			else
				{
					this.collideTop = true;
					this.collideBottom = true;
				}
			
			this.update$5();
		},
		
		"private function shoot",function shoot()/*:void*/ 
		{
			// Prepare some variables to pass on to the bullet
			var bulletX/*:int*/ = this.x;
			var bulletY/*:int*/ = this.y+4;
			var bXVeloc/*:int*/ = 0;
			var bYVeloc/*:int*/ = 0;
			
			if (this._cooldown >= com.chipacabra.Jumper.Player.GUN_DELAY)
			{
			this._blt =as( this._bullets.getFirstAvail(),  com.chipacabra.Jumper.Bullet);	
			
			if (this._blt)
			{
					if (this.facing == org.flixel.FlxSprite.LEFT)
					{
						bulletX -= this._blt.width-8; // nudge it a little to the side so it doesn't emerge from the middle of helmutguy
						bXVeloc = -com.chipacabra.Jumper.Player.BULLET_SPEED;
					}
					else
					{
						bulletX += this.width-8;
						bXVeloc = com.chipacabra.Jumper.Player.BULLET_SPEED;
					}
					this._blt.shoot(bulletX, bulletY, bXVeloc, bYVeloc);
					org.flixel.FlxG.play(this.sndShoot, 1, false,50);
					this._cooldown = 0; // reset the shot clock
				}
			}
		},
		
		"override public function overlaps",function overlaps(Object/*:FlxObject*/)/*:Boolean*/ 
		{
			if (!(Object.dead))
				return this.overlaps$5(Object);
			else
				return false;
		},
		
		"override public function hitBottom",function hitBottom(Contact/*:FlxObject*/, Velocity/*:Number*/)/*:void*/ 
		{
            if (!org.flixel.FlxG.keys.C) // Don't let the player jump until he lets go of the button
			this._jump$5 = 0;
			this._canDJump$5 = true;  // Reset the double jump flag
            this.hitBottom$5(Contact, Velocity);
        },
		
		"override public function kill",function kill()/*:void*/
		{
			if (this.dead) { return; }
			//solid = false;
			//super.kill();
			//exists = false;
			//visible = false;
			org.flixel.FlxG.quake.start(0.005, .35);
			org.flixel.FlxG.flash.start(0xffDB3624, .35);
			if (this._gibs != null)
			{
				this._gibs.at(this);
				this._gibs.start(true, 2.80);
			}
			org.flixel.FlxG.play(this.sndDeath, 1, false,50);
		},
	];},[],["org.flixel.FlxSprite","resource:com/chipacabra/Jumper/../../../../art/lizardhead3.png","resource:com/chipacabra/Jumper/../../../../sounds/death.mp3","resource:com/chipacabra/Jumper/../../../../sounds/jump.mp3","resource:com/chipacabra/Jumper/../../../../sounds/shoot2.mp3","org.flixel.FlxG","int","com.chipacabra.Jumper.Bullet"], "0.8.0", "0.8.2-SNAPSHOT"
);