joo.classLoader.prepare("package org.flixel.data",/*
{
	import org.flixel.**/

	/**
	 * This is the default flixel pause screen.
	 * It can be overridden with your own <code>FlxLayer</code> object.
	 */
	"public class FlxPause extends org.flixel.FlxGroup",5,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		{Embed:{source:"org/flixel/data/key_minus.png"}}, "private var",{ ImgKeyMinus/*:Class*/:null},
		{Embed:{source:"org/flixel/data/key_plus.png"}}, "private var",{ ImgKeyPlus/*:Class*/:null},
		{Embed:{source:"org/flixel/data/key_0.png"}}, "private var",{ ImgKey0/*:Class*/:null},
		{Embed:{source:"org/flixel/data/key_p.png"}}, "private var",{ ImgKeyP/*:Class*/:null},

		/**
		 * Constructor.
		 */
		"public function FlxPause",function FlxPause$()
		{
			this.super$5();
			this.scrollFactor.x = 0;
			this.scrollFactor.y = 0;
			var w/*:uint*/ = 80;
			var h/*:uint*/ = 92;
			this.x = (org.flixel.FlxG.width-w)/2;
			this.y = (org.flixel.FlxG.height-h)/2;
			
			var s/*:FlxSprite*/;
			s = new org.flixel.FlxSprite().createGraphic(w,h,0xaa000000,true);
			s.solid = false;
			this.add(s,true);
			
			(as(this.add(new org.flixel.FlxText(0,0,w,"this game is"),true),  org.flixel.FlxText)).alignment = "center";
			this.add((new org.flixel.FlxText(0,10,w,"PAUSED")).setFormat(null,16,0xffffff,"center"),true);
			
			s = new org.flixel.FlxSprite(4,36,this.ImgKeyP$5);
			s.solid = false;
			this.add(s,true);
			
			this.add(new org.flixel.FlxText(16,36,w-16,"Pause Game"),true);
			
			s = new org.flixel.FlxSprite(4,50,this.ImgKey0$5);
			s.solid = false;
			this.add(s,true);
			
			this.add(new org.flixel.FlxText(16,50,w-16,"Mute Sound"),true);
			
			s = new org.flixel.FlxSprite(4,64,this.ImgKeyMinus$5);
			s.solid = false;
			this.add(s,true);
			
			this.add(new org.flixel.FlxText(16,64,w-16,"Sound Down"),true);
			
			s = new org.flixel.FlxSprite(4,78,this.ImgKeyPlus$5);
			s.solid = false;
			this.add(s,true);
			
			this.add(new org.flixel.FlxText(16,78,w-16,"Sound Up"),true);
		},
	];},[],["org.flixel.FlxGroup","resource:org/flixel/data/key_minus.png","resource:org/flixel/data/key_plus.png","resource:org/flixel/data/key_0.png","resource:org/flixel/data/key_p.png","org.flixel.FlxG","org.flixel.FlxSprite","org.flixel.FlxText"], "0.8.0", "0.8.3"
);