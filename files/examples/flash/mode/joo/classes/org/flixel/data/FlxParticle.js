joo.classLoader.prepare("package org.flixel.data",/*
{
	import org.flixel.**/
	
	"public class FlxParticle extends org.flixel.FlxSprite",5,function($$private){;return[
	
		"protected var",{ _bounce/*:Number*/:NaN},
		
		"public function FlxParticle",function FlxParticle$(Bounce/*:Number*/)
		{
			this.super$5();
			this._bounce = Bounce;
		},
		
		"override public function hitSide",function hitSide(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/
		{
			this.velocity.x = -this.velocity.x * this._bounce;
			if(this.angularVelocity != 0)
				this.angularVelocity = -this.angularVelocity * this._bounce;
		},
		
		"override public function hitBottom",function hitBottom(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/
		{
			this.onFloor = true;
			if(((this.velocity.y > 0)?this.velocity.y:-this.velocity.y) > this._bounce*100)
			{
				this.velocity.y = -this.velocity.y * this._bounce;
				if(this.angularVelocity != 0)
					this.angularVelocity *= -this._bounce;
			}
			else
			{
				this.angularVelocity = 0;
				this.hitBottom$5(Contact,Velocity);
			}
			this.velocity.x *= this._bounce;
		},
	];},[],["org.flixel.FlxSprite"], "0.8.0", "0.8.2-SNAPSHOT"
);