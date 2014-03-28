joo.classLoader.prepare("package com.adamatomic.Mode",/*
{
	import flash.geom.Point
	
	import org.flixel.**/

	"public class Bot extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		{Embed:{source:"com/adamatomic/Mode/../../../data/bot.png"}}, "protected var",{ ImgBot/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/jet.png"}}, "protected var",{ ImgJet/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/asplode.mp3"}}, "protected var",{ SndExplode/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/hit.mp3"}}, "protected var",{ SndHit/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/jet.mp3"}}, "protected var",{ SndJet/*:Class*/:null},
		
		"protected var",{ _gibs/*:FlxEmitter*/:null},
		"protected var",{ _jets/*:FlxEmitter*/:null},
		"protected var",{ _player/*:Player*/:null},
		"protected var",{ _timer/*:Number*/:NaN},
		"protected var",{ _b/*:Array*/:null},
		"static protected var",{ _cb/*:uint*/ : 0},
		"protected var",{ _shotClock/*:Number*/:NaN},
		
		"public function Bot",function Bot$(xPos/*:int*/,yPos/*:int*/,Bullets/*:Array*/,Gibs/*:FlxEmitter*/,ThePlayer/*:Player*/)
		{
			this.super$5(xPos,yPos);
			this.loadRotatedGraphic(this.ImgBot,32,0);
			this._player = ThePlayer;
			this._b = Bullets;
			this._gibs = Gibs;
			
			this.width = 12;
			this.height = 12;
			this.offset.x = 2;
			this.offset.y = 2;
			this.maxAngular = 120;
			this.angularDrag = 400;
			this.maxThrust = 100;
			this.drag.x = 80;
			this.drag.y = 80;
			
			//Jet effect that shoots out from behind the bot
			this._jets = new org.flixel.FlxEmitter();
			this._jets.setRotation();
			this._jets.gravity = 0;
			this._jets.createSprites(this.ImgJet,15,0,false);

			this.reset(this.x,this.y);
		},
		
		"override public function update",function update()/*:void*/
		{			
			var ot/*:Number*/ = this._timer;
			if((this._timer == 0) && this.onScreen()) org.flixel.FlxG.play(this.SndJet);
			this._timer += org.flixel.FlxG.elapsed;
			if((ot < 8) && (this._timer >= 8))
				this._jets.stop(0.1);

			//Aiming
			var dx/*:Number*/ = this.x-this._player.x;
			var dy/*:Number*/ = this.y-this._player.y;
			var da/*:Number*/ = org.flixel.FlxU.getAngle(dx,dy);
			if(da < 0)
				da += 360;
			var ac/*:Number*/ = this.angle;
			if(ac < 0)
				ac += 360;
			if(da < this.angle)
				this.angularAcceleration = -this.angularDrag;
			else if(da > this.angle)
				this.angularAcceleration = this.angularDrag;
			else
				this.angularAcceleration = 0;

			//Jets
			this.thrust = 0;
			if(this._timer > 9)
				this._timer = 0;
			else if(this._timer < 8)
			{
				this.thrust = 40;
				var v/*:FlxPoint*/ = org.flixel.FlxU.rotatePoint(this.thrust,0,0,0,this.angle);
				this._jets.at(this);
				this._jets.setXSpeed(v.x-30,v.x+30);
				this._jets.setYSpeed(v.y-30,v.y+30);
				if(!this._jets.on)
					this._jets.start(false,0.01,0);
			}

			//Shooting
			if(this.onScreen())
			{
				var os/*:Number*/ = this._shotClock;
				this._shotClock += org.flixel.FlxG.elapsed;
				if((os < 4.0) && (this._shotClock >= 4.0))
				{
					this._shotClock = 0;
					this.shoot();
				}
				else if((os < 3.5) && (this._shotClock >= 3.5))
					this.shoot();
				else if((os < 3.0) && (this._shotClock >= 3.0))
					this.shoot();
			}
			
			this._jets.update();
			this.update$5();
		},
		
		"override public function render",function render()/*:void*/
		{
			this._jets.render();
			this.render$5();
		},
		
		"override public function hurt",function hurt(Damage/*:Number*/)/*:void*/
		{
			org.flixel.FlxG.play(this.SndHit);
			this.flicker(0.2);
			org.flixel.FlxG.score += 10;
			this.hurt$5(Damage);
		},
		
		"override public function kill",function kill()/*:void*/
		{
			if(this.dead)
				return;
			org.flixel.FlxG.play(this.SndExplode);
			this.kill$5();
			this.flicker(-1);
			this._jets.kill();
			this._gibs.at(this);
			this._gibs.start(true,0,20);
			org.flixel.FlxG.score += 200;
		},
		
		"override public function reset",function reset(X/*:Number*/, Y/*:Number*/)/*:void*/
		{
			this.reset$5(X,Y);
			this.thrust = 0;
			this.velocity.x = 0;
			this.velocity.y = 0;
			this.angle = Math.random()*360 - 180;
			this.health = 2;
			this._timer = 0;
			this._shotClock = 0;
		},
		
		"protected function shoot",function shoot()/*:void*/
		{
			var ba/*:FlxPoint*/ = org.flixel.FlxU.rotatePoint(-120,0,0,0,this.angle);
			this._b[com.adamatomic.Mode.Bot._cb].shoot(this.x+this.width/2-2,this.y+this.height/2-2,ba.x,ba.y);
			if(++com.adamatomic.Mode.Bot._cb >= this._b.length) com.adamatomic.Mode.Bot._cb = 0;
		},
	];},[],["org.flixel.FlxSprite","resource:com/adamatomic/Mode/../../../data/bot.png","resource:com/adamatomic/Mode/../../../data/jet.png","resource:com/adamatomic/Mode/../../../data/asplode.mp3","resource:com/adamatomic/Mode/../../../data/hit.mp3","resource:com/adamatomic/Mode/../../../data/jet.mp3","org.flixel.FlxEmitter","org.flixel.FlxG","org.flixel.FlxU","Math"], "0.8.0", "0.8.2-SNAPSHOT"
);