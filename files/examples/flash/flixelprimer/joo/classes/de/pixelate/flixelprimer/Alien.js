joo.classLoader.prepare("package de.pixelate.flixelprimer",/*
{
	import org.flixel.**/

	"public class Alien extends org.flixel.FlxSprite",5,function($$private){;return[
			
		{Embed:{source:"de/pixelate/flixelprimer/../../../../assets/png/Alien.png"}}, "private var",{ ImgAlien/*:Class*/:null},

		"public function Alien",function Alien$(x/*: Number*/, y/*: Number*/)/*:void*/
		{
			this.super$5(x, y, this.ImgAlien$5);
			this.velocity.x = -200;
		},

		"override public function update",function update()/*:void*/
		{			
	    	this.velocity.y = Math.cos(this.x / 50) * 50;
			this.update$5();			
		},	
	];},[],["org.flixel.FlxSprite","resource:de/pixelate/flixelprimer/../../../../assets/png/Alien.png","Math"], "0.8.0", "0.8.3"
);