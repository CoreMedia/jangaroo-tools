joo.classLoader.prepare("package com.adamatomic.Mode",/*
{
	import org.flixel.**/

	"public class Player extends org.flixel.FlxSprite",5,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		{Embed:{source:"com/adamatomic/Mode/../../../data/spaceman.png"}}, "private var",{ ImgSpaceman/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/jump.mp3"}}, "private var",{ SndJump/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/land.mp3"}}, "private var",{ SndLand/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/asplode.mp3"}}, "private var",{ SndExplode/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/menu_hit_2.mp3"}}, "private var",{ SndExplode2/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/hurt.mp3"}}, "private var",{ SndHurt/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/jam.mp3"}}, "private var",{ SndJam/*:Class*/:null},
		
		"private var",{ _jumpPower/*:int*/:0},
		"private var",{ _bullets/*:Array*/:null},
		"private var",{ _curBullet/*:uint*/:0},
		"private var",{ _bulletVel/*:int*/:0},
		"private var",{ _up/*:Boolean*/:false},
		"private var",{ _down/*:Boolean*/:false},
		"private var",{ _restart/*:Number*/:NaN},
		"private var",{ _gibs/*:FlxEmitter*/:null},
		
		"public function Player",function Player$(X/*:int*/,Y/*:int*/,Bullets/*:Array*/,Gibs/*:FlxEmitter*/)
		{
			this.super$5(X,Y);
			this.loadGraphic(this.ImgSpaceman$5,true,true,8);
			this._restart$5 = 0;
			
			//bounding box tweaks
			this.width = 6;
			this.height = 7;
			this.offset.x = 1;
			this.offset.y = 1;
			
			//basic player physics
			var runSpeed/*:uint*/ = 80;
			this.drag.x = runSpeed*8;
			this.acceleration.y = 420;
			this._jumpPower$5 = 200;
			this.maxVelocity.x = runSpeed;
			this.maxVelocity.y = this._jumpPower$5;
			
			//animations
			this.addAnimation("idle", [0]);
			this.addAnimation("run", [1, 2, 3, 0], 12);
			this.addAnimation("jump", [4]);
			this.addAnimation("idle_up", [5]);
			this.addAnimation("run_up", [6, 7, 8, 5], 12);
			this.addAnimation("jump_up", [9]);
			this.addAnimation("jump_down", [10]);
			
			//bullet stuff
			this._bullets$5 = Bullets;
			this._curBullet$5 = 0;
			this._bulletVel$5 = 360;
			
			//Gibs emitted upon death
			this._gibs$5 = Gibs;
		},
		
		"override public function update",function update()/*:void*/
		{
			//game restart timer
			if(this.dead)
			{
				this._restart$5 += org.flixel.FlxG.elapsed;
				if(this._restart$5 > 2)
					(as(org.flixel.FlxG.state,  com.adamatomic.Mode.PlayState)).reload = true;
				return;
			}
			
			//MOVEMENT
			this.acceleration.x = 0;
			if(org.flixel.FlxG.keys.LEFT)
			{
				this.facing = org.flixel.FlxSprite.LEFT;
				this.acceleration.x -= this.drag.x;
			}
			else if(org.flixel.FlxG.keys.RIGHT)
			{
				this.facing = org.flixel.FlxSprite.RIGHT;
				this.acceleration.x += this.drag.x;
			}
			if(org.flixel.FlxG.keys.justPressed("X") && !this.velocity.y)
			{
				this.velocity.y = -this._jumpPower$5;
				org.flixel.FlxG.play(this.SndJump$5);
			}
			
			//AIMING
			this._up$5 = false;
			this._down$5 = false;
			if(org.flixel.FlxG.keys.UP) this._up$5 = true;
			else if(org.flixel.FlxG.keys.DOWN && this.velocity.y) this._down$5 = true;
			
			//ANIMATION
			if(this.velocity.y != 0)
			{
				if(this._up$5) this.play("jump_up");
				else if(this._down$5) this.play("jump_down");
				else this.play("jump");
			}
			else if(this.velocity.x == 0)
			{
				if(this._up$5) this.play("idle_up");
				else this.play("idle");
			}
			else
			{
				if(this._up$5) this.play("run_up");
				else this.play("run");
			}
			
			//SHOOTING
			if(!this.flickering() && org.flixel.FlxG.keys.justPressed("C"))
			{
				var bXVel/*:int*/ = 0;
				var bYVel/*:int*/ = 0;
				var bX/*:int*/ = this.x;
				var bY/*:int*/ = this.y;
				if(this._up$5)
				{
					bY -= this._bullets$5[this._curBullet$5].height - 4;
					bYVel = -this._bulletVel$5;
				}
				else if(this._down$5)
				{
					bY += this.height - 4;
					bYVel = this._bulletVel$5;
					this.velocity.y -= 36;
				}
				else if(this.facing == org.flixel.FlxSprite.RIGHT)
				{
					bX += this.width - 4;
					bXVel = this._bulletVel$5;
				}
				else
				{
					bX -= this._bullets$5[this._curBullet$5].width - 4;
					bXVel = -this._bulletVel$5;
				}
				this._bullets$5[this._curBullet$5].shoot(bX,bY,bXVel,bYVel);
				if(++this._curBullet$5 >= this._bullets$5.length)
					this._curBullet$5 = 0;
			}
				
			//UPDATE POSITION AND ANIMATION
			this.update$5();

			//Jammed, can't fire!
			if(this.flickering())
			{
				if(org.flixel.FlxG.keys.justPressed("C"))
					org.flixel.FlxG.play(this.SndJam$5);
			}
		},
		
		"override public function hitBottom",function hitBottom(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/
		{
			if(this.velocity.y > 50)
				org.flixel.FlxG.play(this.SndLand$5);
			this.onFloor = true;
			return this.hitBottom$5(Contact,Velocity);
		},
		
		"override public function hurt",function hurt(Damage/*:Number*/)/*:void*/
		{
			Damage = 0;
			if(this.flickering())
				return;
			org.flixel.FlxG.play(this.SndHurt$5);
			this.flicker(1.3);
			if(org.flixel.FlxG.score > 1000) org.flixel.FlxG.score -= 1000;
			if(this.velocity.x > 0)
				this.velocity.x = -this.maxVelocity.x;
			else
				this.velocity.x = this.maxVelocity.x;
			this.hurt$5(Damage);
		},
		
		"override public function kill",function kill()/*:void*/
		{
			if(this.dead)
				return;
			this.solid = false;
			org.flixel.FlxG.play(this.SndExplode$5);
			org.flixel.FlxG.play(this.SndExplode2$5);
			this.kill$5();
			this.flicker(-1);
			this.exists = true;
			this.visible = false;
			org.flixel.FlxG.quake.start(0.005,0.35);
			org.flixel.FlxG.flash.start(0xffd8eba2,0.35);
			if(this._gibs$5 != null)
			{
				this._gibs$5.at(this);
				this._gibs$5.start(true,0,50);
			}
		},
	];},[],["org.flixel.FlxSprite","resource:com/adamatomic/Mode/../../../data/spaceman.png","resource:com/adamatomic/Mode/../../../data/jump.mp3","resource:com/adamatomic/Mode/../../../data/land.mp3","resource:com/adamatomic/Mode/../../../data/asplode.mp3","resource:com/adamatomic/Mode/../../../data/menu_hit_2.mp3","resource:com/adamatomic/Mode/../../../data/hurt.mp3","resource:com/adamatomic/Mode/../../../data/jam.mp3","org.flixel.FlxG","com.adamatomic.Mode.PlayState"], "0.8.0", "0.8.2-SNAPSHOT"
);