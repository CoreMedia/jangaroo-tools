joo.classLoader.prepare("package com.adamatomic.Mode",/*
{
	import org.flixel.**/

	"public class BotBullet extends org.flixel.FlxSprite",5,function($$private){;return[
	
		{Embed:{source:"com/adamatomic/Mode/../../../data/bot_bullet.png"}}, "private var",{ ImgBullet/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/jump.mp3"}}, "private var",{ SndHit/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/enemy.mp3"}}, "private var",{ SndShoot/*:Class*/:null},
		
		"public function BotBullet",function BotBullet$()
		{
			this.super$5();
			this.loadGraphic(this.ImgBullet$5,true);
			this.addAnimation("idle",[0, 1], 50);
			this.addAnimation("poof",[2, 3, 4], 50, false);
			this.exists = false;
		},
		
		"override public function update",function update()/*:void*/
		{
			if(this.dead && this.finished) this.exists = false;
			else this.update$5();
		},

		"override public function hitSide",function hitSide(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/ { this.kill(); },
		"override public function hitBottom",function hitBottom(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/ { this.kill(); },
		"override public function hitTop",function hitTop(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/ { this.kill(); },
		"override public function kill",function kill()/*:void*/
		{
			if(this.dead) return;
			this.velocity.x = 0;
			this.velocity.y = 0;
			if(this.onScreen()) org.flixel.FlxG.play(this.SndHit$5);
			this.dead = true;
			this.solid = false;
			this.play("poof");
		},
		
		"public function shoot",function shoot(X/*:int*/, Y/*:int*/, VelocityX/*:int*/, VelocityY/*:int*/)/*:void*/
		{
			org.flixel.FlxG.play(this.SndShoot$5,0.5);
			this.reset(X,Y);
			this.solid = true;
			this.velocity.x = VelocityX;
			this.velocity.y = VelocityY;
			this.play("idle");
		},
	];},[],["org.flixel.FlxSprite","resource:com/adamatomic/Mode/../../../data/bot_bullet.png","resource:com/adamatomic/Mode/../../../data/jump.mp3","resource:com/adamatomic/Mode/../../../data/enemy.mp3","org.flixel.FlxG"], "0.8.0", "0.8.2-SNAPSHOT"
);