joo.classLoader.prepare("package de.pixelate.flixelprimer",/*
{
	import org.flixel.**/

	"public class Ship extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},
			
		{Embed:{source:"de/pixelate/flixelprimer/../../../../assets/png/Ship.png"}}, "private var",{ ImgShip/*:Class*/:null},

		"public function Ship",function Ship$()/*:void*/
		{
			this.super$5(50, 50, this.ImgShip$5);
		},

		"override public function update",function update()/*:void*/
		{
			this.velocity.x = 0;
			this.velocity.y = 0;

			if(org.flixel.FlxG.keys.LEFT)
			{
				this.velocity.x = -250;
			}
			else if(org.flixel.FlxG.keys.RIGHT)
			{
				this.velocity.x = 250;				
			}

			if(org.flixel.FlxG.keys.UP)
			{
				this.velocity.y = -250;				
			}
			else if(org.flixel.FlxG.keys.DOWN)
			{
				this.velocity.y = 250;
			}
			
			this.update$5();
			
			if(this.x > org.flixel.FlxG.width-this.width-16)
			{
				this.x = org.flixel.FlxG.width-this.width-16;
			}
			else if(this.x < 16)
			{
				this.x = 16;				
			}

			if(this.y > org.flixel.FlxG.height-this.height-16)
			{
				this.y = org.flixel.FlxG.height-this.height-16;
			}
			else if(this.y < 16)
			{
				this.y = 16;				
			}	
		},
		
		"public function getBulletSpawnPosition",function getBulletSpawnPosition()/*:FlxPoint*/
		{
			var p/*: FlxPoint*/ = new org.flixel.FlxPoint(this.x + 36, this.y + 12);
			return p;
		},		
	];},[],["org.flixel.FlxSprite","resource:de/pixelate/flixelprimer/../../../../assets/png/Ship.png","org.flixel.FlxG","org.flixel.FlxPoint"], "0.8.0", "0.8.3"
);