joo.classLoader.prepare("package com.chipacabra.Jumper",/* 
{
	import com.chipacabra.Jumper.Player
	import org.flixel.FlxG
	import org.flixel.FlxGroup
	import org.flixel.FlxObject
	import org.flixel.FlxU*/
	/**
	 * ...
	 * @author David Bell
	 */
	"public class Lurker extends com.chipacabra.Jumper.EnemyTemplate",6,function($$private){var as=joo.as,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);}, 
	
		
		
		{Embed:{source : 'com/chipacabra/Jumper/../../../../art/lurkmonsta.png'}},"public var",{ imgLurker/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../sounds/monhurt2.mp3'}},"public var",{ sndHurt/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../sounds/mondead2.mp3'}},"public var",{ sndDead/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../sounds/badshoot.mp3'}},"public var",{ sndShoot/*:Class*/:null},
		
		"protected static const",{ RUN_SPEED/*:int*/ : 30},
		"protected static const",{ GRAVITY/*:int*/ : 300},
		"protected static const",{ HEALTH/*:int*/ : 2},
		"protected static const",{ SPAWNTIME/*:Number*/ : 45},
		"protected static const",{ JUMP_SPEED/*:int*/ : 60},
		"protected static const",{ BURNTIME/*:int*/ : 5},
		"protected static const",{ GUN_DELAY/*:int*/ : 1.5},
		"protected static const",{ BULLET_SPEED/*:int*/ :100},
		
		"protected var",{ _spawntimer/*:Number*/:NaN},
		"protected var",{ _burntimer/*:Number*/:NaN},
		"protected var",{ _playdeathsound/*:Boolean*/:false},
		"protected var",{ _bullets/*:FlxGroup*/:null},
		"protected var",{ _cooldown/*:Number*/:NaN},
		"public function Lurker",function Lurker$(X/*:Number*/, Y/*:Number*/, ThePlayer/*:Player*/, Bullets/*:FlxGroup*/) 
		{
			this.super$6(X, Y, ThePlayer);
			
			this._spawntimer = 0;
			this._burntimer = 0;
			this._playdeathsound = true;
			this._bullets = Bullets;
			this._cooldown = 0;
			
			this.loadGraphic(this.imgLurker, true, true, 16, 17);
			this.addAnimation("walking", [0, 1], 18, true);
			this.addAnimation("burning", [2, 3], 18, true);
			this.addAnimation("wrecked", [4, 5], 18, true);
			this.addAnimation("idle", [0]);
			this.drag.x = com.chipacabra.Jumper.Lurker.RUN_SPEED * 9;
			this.drag.y = com.chipacabra.Jumper.Lurker.JUMP_SPEED * 7;
			this.acceleration.y = com.chipacabra.Jumper.Lurker.GRAVITY;
			this.maxVelocity.x = com.chipacabra.Jumper.Lurker.RUN_SPEED;
			this.maxVelocity.y = com.chipacabra.Jumper.Lurker.JUMP_SPEED;
			this.health = com.chipacabra.Jumper.Lurker.HEALTH;
			this.offset.x = 3;
			this.width = 10;
		},
		"override public function update",function update()/*:void*/ 
		{
			//Animation
			if (!this.velocity.x && !this.velocity.y) { this.play("idle"); }
			else if (this.health < com.chipacabra.Jumper.Lurker.HEALTH) 
			{ 
				if (this.velocity.y == 0) { this.play("wrecked");}
				else {this.play("burning");} 
			}
			else { this.play("walking"); }	
			
			if (this.health>0)
			{
				if (this.velocity.y == 0) { this.acceleration.y = -this.acceleration.y; }
				if (this.x != this._startx)
					{this.acceleration.x = ((this._startx - this.x)); }
				
				var xdistance/*:Number*/ = this._player.x - this.x;
				var ydistance/*:Number*/ = this._player.y - this.y;
				var distancesquared/*:Number*/ = xdistance * xdistance + ydistance * ydistance;
				if (distancesquared < 45000)
				{
					this.shoot$6(this._player);
				}
			}
			
			if (this.health<=0)
			{
				this.maxVelocity.y = com.chipacabra.Jumper.Lurker.JUMP_SPEED * 4;
				this.acceleration.y = com.chipacabra.Jumper.Lurker.GRAVITY*3;
				this.velocity.x = 0;
				this._burntimer += org.flixel.FlxG.elapsed;
				if (this._burntimer >= com.chipacabra.Jumper.Lurker.BURNTIME)
				{
					this.kill$6();
					this.x = -10;
					this.y = -10;
					this.visible = false;
					this.acceleration.y = 0;
				}
				this._spawntimer += org.flixel.FlxG.elapsed;
				if (this._spawntimer >= com.chipacabra.Jumper.Lurker.SPAWNTIME)
				{
					this.reset(this._startx,this._starty);
				}
			}
			this._cooldown += org.flixel.FlxG.elapsed;
			this.update$6();
		},
		"override public function reset",function reset(X/*:Number*/, Y/*:Number*/)/*:void*/ 
		{
			this.reset$6(X, Y);
			this.health = com.chipacabra.Jumper.Lurker.HEALTH;
			this._spawntimer = 0;
			this._burntimer = 0;
			this.acceleration.y = com.chipacabra.Jumper.Lurker.GRAVITY;
			this.maxVelocity.y = com.chipacabra.Jumper.Lurker.JUMP_SPEED;
			this._playdeathsound = true;
		},
		
		"override public function hurt",function hurt(Damage/*:Number*/)/*:void*/ 
		{
			if (this.x > this._player.x)
				this.velocity.x = this.drag.x * 4;
			else
				this.velocity.x = -this.drag.x * 4;
			this.flicker(.5);
			org.flixel.FlxG.play(this.sndHurt,1,false,50);
			this.health -= 1;
			//super.hurt(Damage);
		},
		"private function shoot",function shoot(P/*:Player*/)/*:void*/ 
		{
			// Bullet code will go here
			var bulletX/*:int*/ = this.x;
			var bulletY/*:int*/ = this.y+4;
			
			if (this._cooldown > com.chipacabra.Jumper.Lurker.GUN_DELAY)
			{
				var bullet/*:Bullet*/ =as($$bound( this._bullets,"getFirstAvail"),  com.chipacabra.Jumper.Bullet);
				if (bullet == null)
				{
					bullet= new com.chipacabra.Jumper.Bullet();
					this._bullets.add(bullet);
				}
					if (P.x<this.x)
					{
						bulletX -= bullet.width-8; // nudge it a little to the side so it doesn't emerge from the middle of helmutguy
					}
					else
					{
						bulletX += this.width-8;
					}
					bullet.angleshoot(bulletX, bulletY, com.chipacabra.Jumper.Lurker.BULLET_SPEED, P);
					org.flixel.FlxG.play(this.sndShoot, 1, false,50);
					this._cooldown = 0; // reset the shot clock
			}
		},
		"override public function kill",function kill()/*:void*/ 
		{
			if (this.dead) { return; }
			this.exists = true;
			this.solid = true;
			this.visible = true;
			this.acceleration.y = com.chipacabra.Jumper.Lurker.GRAVITY;
			this.velocity.x = 0;
		},
		"override public function hitBottom",function hitBottom(Contact/*:FlxObject*/, Velocity/*:Number*/)/*:void*/ 
		{
			if (this.health <= 0 && this._playdeathsound)
			{
				org.flixel.FlxG.play(this.sndDead, 1, false, 50);
				this._playdeathsound = false;
			}
			this.hitBottom$6(Contact, Velocity);
		},
		
	];},[],["com.chipacabra.Jumper.EnemyTemplate","resource:com/chipacabra/Jumper/../../../../art/lurkmonsta.png","resource:com/chipacabra/Jumper/../../../../sounds/monhurt2.mp3","resource:com/chipacabra/Jumper/../../../../sounds/mondead2.mp3","resource:com/chipacabra/Jumper/../../../../sounds/badshoot.mp3","org.flixel.FlxG","com.chipacabra.Jumper.Bullet"], "0.8.0", "0.8.2-SNAPSHOT"

);