joo.classLoader.prepare("package com.chipacabra.Jumper",/* 
{
	import org.flixel.FlxG
	import org.flixel.FlxObject
	import org.flixel.FlxPoint
	import org.flixel.FlxSprite
	import org.flixel.FlxU*/
	
	/**
	 * ...
	 * @author David Bell
	 */
	"public class Bullet extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(Math,org.flixel.FlxG);}, 
	
		{Embed:{source : 'com/chipacabra/Jumper/../../../../art/bullet.png'}},"public var",{ ImgBullet/*:Class*/:null},

		
		
		"public function Bullet",function Bullet$() 
		{
			this.super$5();
			this.loadGraphic(this.ImgBullet, false);
			this.exists = false; // We don't want the bullets to exist anywhere before we call them.
		},
		
		"override public function update",function update()/*:void*/ 
		{
			if (this.dead && this.finished) //Finished refers to animation, only included here in case I add animation later
				this.exists = false;   // Stop paying attention when the bullet dies. 
			if (this.getScreenXY().x < -64 || this.getScreenXY().x > org.flixel.FlxG.width+64) { this.kill();} // If the bullet makes it 64 pixels off the side of the screen, kill it
			else this.update$5();
		},
		
		//We want the bullet to go away when it hits something, not just stop.
		"override public function hitSide",function hitSide(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/ { this.kill(); },
		"override public function hitBottom",function hitBottom(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/ { this.kill(); },
		"override public function hitTop",function hitTop(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/ { this.kill(); },
		
		// We need some sort of function other classes can call that will let us actually fire the bullet. 
		"public function shoot",function shoot(X/*:int*/, Y/*:int*/, VelocityX/*:int*/, VelocityY/*:int*/)/*:void*/
		{
			this.reset(X, Y);  // reset() makes the sprite exist again, at the new location you tell it.
			this.solid = true;
			this.velocity.x = VelocityX;
			this.velocity.y = VelocityY;
		},
		
		"public function angleshoot",function angleshoot(X/*:int*/, Y/*:int*/, Speed/*:int*/, Target/*:FlxPoint*/)/*:void*/
		{
			this.reset(X, Y);
			this.solid = true;
			var dangle/*:Number*/ = org.flixel.FlxU.getAngle(Target.x-(this.x+(this.width/2)), Target.y-(this.y+(this.height/2)));  //This gives angle in degrees
			var rangle/*:Number*/ = dangle * (Math.PI / 180);          //We need it in radians
			this.velocity.x = Math.cos(rangle) * Speed;
			this.velocity.y = Math.sin(rangle) * Speed;
		},
		
	];},[],["org.flixel.FlxSprite","resource:com/chipacabra/Jumper/../../../../art/bullet.png","org.flixel.FlxG","org.flixel.FlxU","Math"], "0.8.0", "0.8.2-SNAPSHOT"

);