joo.classLoader.prepare("package com.chipacabra.Jumper",/* 
{
	import org.flixel.FlxEmitter
	import org.flixel.FlxG
	import org.flixel.FlxObject
	import org.flixel.FlxSprite*/
	
	/**
	 * ...
	 * @author David Bell
	 */
	"public class Enemy extends com.chipacabra.Jumper.EnemyTemplate",6,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);}, 
	
		{Embed:{source : 'com/chipacabra/Jumper/../../../../art/spikemonsta.png'}},"public var",{ Spikemonsta/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../sounds/monhurt2.mp3'}},"public var",{ sndHurt/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../sounds/mondead2.mp3'}},"public var",{ sndDead/*:Class*/:null},
		
		"protected static const",{ RUN_SPEED/*:int*/ : 60},
		"protected static const",{ GRAVITY/*:int*/ :0},
		"protected static const",{ JUMP_SPEED/*:int*/ : 60},
		"protected static const",{ HEALTH/*:int*/ : 1},
		"protected static const",{ SPAWNTIME/*:Number*/ : 30},
		
		//protected var _player:Player;
		"protected var",{ _gibs/*:FlxEmitter*/:null},
		//protected var _startx:Number;
		//protected var _starty:Number;
		"protected var",{ _spawntimer/*:Number*/:NaN},
		
		"public function Enemy",function Enemy$(X/*:Number*/, Y/*:Number*/, ThePlayer/*:Player*/, Gibs/*:FlxEmitter*/) 
		{
			this.super$6(X, Y,ThePlayer);
			
			// These will let us reset the monster later
			//_startx = X;
			//_starty = Y;
			this._spawntimer = 0;
			
			this.loadGraphic(this.Spikemonsta, true, true);  //Set up the graphics
			this.addAnimation("walking", [0, 1], 10, true);
			this.addAnimation("idle", [0]);
			//_player = ThePlayer;
			
			this.drag.x = com.chipacabra.Jumper.Enemy.RUN_SPEED * 7;
			this.drag.y = com.chipacabra.Jumper.Enemy.JUMP_SPEED * 7;
			this.acceleration.y = com.chipacabra.Jumper.Enemy.GRAVITY;
			this.maxVelocity.x = com.chipacabra.Jumper.Enemy.RUN_SPEED;
			this.maxVelocity.y = com.chipacabra.Jumper.Enemy.JUMP_SPEED;
			this.health = com.chipacabra.Jumper.Enemy.HEALTH;
			
			this._gibs = Gibs;
			
		},
		
		"public override function update",function update()/*:void*/
		{
			if (this.dead)
			{
				this._spawntimer += org.flixel.FlxG.elapsed;
				if (this._spawntimer >= com.chipacabra.Jumper.Enemy.SPAWNTIME)
					{
						this.reset(this._startx,this._starty);
					}
			return;
			}
			
			this.acceleration.x = this.acceleration.y = 0; // Coast to 0 when not chasing the player
			
			var xdistance/*:Number*/ = this._player.x - this.x; // distance on x axis to player
			var ydistance/*:Number*/ = this._player.y - this.y; // distance on y axis to player
			var distancesquared/*:Number*/ = xdistance * xdistance + ydistance * ydistance; // absolute distance to player (squared, because there's no need to spend cycles calculating the square root)
			if (distancesquared < 65000) // that's somewhere around 16 tiles
			{
				if (this._player.x < this.x)
				{
					this.facing = org.flixel.FlxSprite.RIGHT; // The sprite is facing the opposite direction than flixel is expecting, so hack it into the right direction
					this.acceleration.x = -this.drag.x;
				}
				else if (this._player.x > this.x)
				{
					this.facing = org.flixel.FlxSprite.LEFT;
					this.acceleration.x = this.drag.x;
				}
				if (this._player.y < this.y) { this.acceleration.y = -this.drag.y; }
				else if (this._player.y > this.y) { this.acceleration.y = this.drag.y;}
			}
			//Animation
			if (!this.velocity.x && !this.velocity.y) { this.play("idle"); }
			else {this.play("walking");}
			
			this.update$6();
		},
		
		"override public function reset",function reset(X/*:Number*/, Y/*:Number*/)/*:void*/ 
		{
			this.reset$6(X, Y);
			this.health = com.chipacabra.Jumper.Enemy.HEALTH;
			this._spawntimer = 0;
		},
		
		"override public function hurt",function hurt(Damage/*:Number*/)/*:void*/ 
		{
			if (this.facing == org.flixel.FlxSprite.RIGHT) // remember, right means facing left
				this.velocity.x = this.drag.x * 4; // Knock him to the right
			else if (this.facing == org.flixel.FlxSprite.LEFT) // Don't really need the if part, but hey.
				this.velocity.x = -this.drag.x * 4;
			this.flicker(.5);
			org.flixel.FlxG.play(this.sndHurt, 1, false,50);
			this.hurt$6(Damage);
		},
		
		"override public function kill",function kill()/*:void*/ 
		{
			if (this.dead) { return; }
			
			if (this._gibs != null)
			{
				this._gibs.at(this);
				this._gibs.start(true, 2.80);
				org.flixel.FlxG.play(this.sndDead, 1, false,50);
			}
			this.kill$6();
			//We need to keep updating for the respawn timer, so set exists back on.
			this.exists = true;
			this.visible = false;
			//Shove it off the map just to avoid any accidents before it respawns
			this.x = -10;
			this.y = -10;
		},
		
	
	];},[],["com.chipacabra.Jumper.EnemyTemplate","resource:com/chipacabra/Jumper/../../../../art/spikemonsta.png","resource:com/chipacabra/Jumper/../../../../sounds/monhurt2.mp3","resource:com/chipacabra/Jumper/../../../../sounds/mondead2.mp3","org.flixel.FlxG"], "0.8.0", "0.8.2-SNAPSHOT"

);