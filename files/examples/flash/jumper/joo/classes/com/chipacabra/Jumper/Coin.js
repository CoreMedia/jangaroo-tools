joo.classLoader.prepare("package com.chipacabra.Jumper",/* 
{
	import org.flixel.FlxG
	import org.flixel.FlxSprite*/
	
	/**
	 * ...
	 * @author David Bell
	 */
	"public class Coin extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);}, 
	
		{Embed:{source : 'com/chipacabra/Jumper/../../../../art/coinspin.png'}},"public var",{ imgCoin/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../sounds/coin.mp3'}},"public var",{ sndCoin/*:Class*/:null},
		"public function Coin",function Coin$(X/*:Number = 0*/, Y/*:Number = 0*/, SimpleGraphic/*:Class = null*/) 
		{switch(arguments.length){case 0:X = 0;case 1:Y = 0;case 2:SimpleGraphic = null;}
			this.super$5(X, Y);
			
			this.loadGraphic(this.imgCoin, true, false);
			this.addAnimation("spinning", [0, 1, 2, 3, 4, 5], 10, true);
			this.play("spinning");
		},
		
		"override public function kill",function kill()/*:void*/ 
		{
			this.kill$5();
			org.flixel.FlxG.play(this.sndCoin, 3, false, 50);
			org.flixel.FlxG.score++;
		},
	];},[],["org.flixel.FlxSprite","resource:com/chipacabra/Jumper/../../../../art/coinspin.png","resource:com/chipacabra/Jumper/../../../../sounds/coin.mp3","org.flixel.FlxG"], "0.8.0", "0.8.2-SNAPSHOT"

);