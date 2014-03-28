joo.classLoader.prepare("package com.adamatomic.Mode",/*
{
	import org.flixel.**/

	"public class MenuState extends org.flixel.FlxState",7,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		{Embed:{source:"com/adamatomic/Mode/../../../data/spawner_gibs.png"}}, "private var",{ ImgGibs/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/cursor.png"}}, "private var",{ ImgCursor/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/menu_hit.mp3"}}, "private var",{ SndHit/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/menu_hit_2.mp3"}}, "private var",{ SndHit2/*:Class*/:null},
		
		"private var",{ _gibs/*:FlxEmitter*/:null},
		"private var",{ _b/*:FlxButton*/:null},
		"private var",{ _t1/*:FlxText*/:null},
		"private var",{ _t2/*:FlxText*/:null},
		"private var",{ _ok/*:Boolean*/:false},
		"private var",{ _ok2/*:Boolean*/:false},
		
		"override public function create",function create()/*:void*/
		{
			var i/*:uint*/;
			var s/*:FlxSprite*/;
			
			this._gibs$7 = new org.flixel.FlxEmitter(org.flixel.FlxG.width/2-50,org.flixel.FlxG.height/2-10);
			this._gibs$7.setSize(100,30);
			this._gibs$7.setYSpeed(-200,-20);
			this._gibs$7.setRotation(-720,720);
			this._gibs$7.gravity = 100;
			this._gibs$7.createSprites(this.ImgGibs$7,1000,32);
			this.add(this._gibs$7);
				
			this._t1$7 = new org.flixel.FlxText(org.flixel.FlxG.width,org.flixel.FlxG.height/3,80,"mo");
			this._t1$7.size = 32;
			this._t1$7.color = 0x3a5c39;
			this._t1$7.antialiasing = true;
			this.add(this._t1$7);

			this._t2$7 = new org.flixel.FlxText(-60,org.flixel.FlxG.height/3,80,"de");
			this._t2$7.size = this._t1$7.size;
			this._t2$7.color = this._t1$7.color;
			this._t2$7.antialiasing = this._t1$7.antialiasing;
			this.add(this._t2$7);
			
			this._ok$7 = false;
			this._ok2$7 = false;
			
			org.flixel.FlxG.mouse.show(this.ImgCursor$7);
			
			//Simple use of flixel save game object
			var save/*:FlxSave*/ = new org.flixel.FlxSave();
			if(save.bind("Mode"))
			{
				if(save.data.plays == null)
					save.data.plays = 0;
				else
					save.data.plays++;
				org.flixel.FlxG.log("Number of plays: "+save.data.plays);
			}
		},

		"override public function update",function update()/*:void*/
		{
			//Slides the text ontot he screen
			var t1m/*:uint*/ = org.flixel.FlxG.width/2-54;
			if(this._t1$7.x > t1m)
			{
				this._t1$7.x -= org.flixel.FlxG.elapsed*org.flixel.FlxG.width;
				if(this._t1$7.x < t1m) this._t1$7.x = t1m;
			}
			var t2m/*:uint*/ = org.flixel.FlxG.width/2+6;
			if(this._t2$7.x < t2m)
			{
				this._t2$7.x += org.flixel.FlxG.elapsed*org.flixel.FlxG.width;
				if(this._t2$7.x > t2m) this._t2$7.x = t2m;
			}
			
			//Check to see if the text is in position
			if(!this._ok$7 && ((this._t1$7.x == t1m) || (this._t2$7.x == t2m)))
			{
				//explosion
				this._ok$7 = true;
				org.flixel.FlxG.play(this.SndHit$7);
				org.flixel.FlxG.flash.start(0xffd8eba2,0.5);
				org.flixel.FlxG.quake.start(0.035,0.5);
				this._t1$7.color = 0xd8eba2;
				this._t2$7.color = 0xd8eba2;
				this._gibs$7.start(true,5);
				this._t1$7.angle = org.flixel.FlxU.random()*40-20;
				this._t2$7.angle = org.flixel.FlxU.random()*40-20;
				
				var t1/*:FlxText*/;
				var t2/*:FlxText*/;
				var b/*:FlxButton*/;
				
				t1 = new org.flixel.FlxText(t1m,org.flixel.FlxG.height/3+39,110,"by Adam Atomic");
				t1.alignment = "center";
				t1.color = 0x3a5c39;
				this.add(t1);
				
				//flixel button
				this.add((new org.flixel.FlxSprite(t1m+1,org.flixel.FlxG.height/3+53)).createGraphic(106,19,0xff131c1b));
				b = new org.flixel.FlxButton(t1m+2,org.flixel.FlxG.height/3+54,$$bound(this,"onFlixel$7"));
				b.loadGraphic((new org.flixel.FlxSprite()).createGraphic(104,15,0xff3a5c39),(new org.flixel.FlxSprite()).createGraphic(104,15,0xff729954));
				t1 = new org.flixel.FlxText(15,1,100,"www.flixel.org");
				t1.color = 0x729954;
				t2 = new org.flixel.FlxText(t1.x,t1.y,t1.width,t1.text);
				t2.color = 0xd8eba2;
				b.loadText(t1,t2);
				this.add(b);
				
				//danny B button
				this.add((new org.flixel.FlxSprite(t1m+1,org.flixel.FlxG.height/3+75)).createGraphic(106,19,0xff131c1b));
				b = new org.flixel.FlxButton(t1m+2,org.flixel.FlxG.height/3+76,$$bound(this,"onDanny$7"));
				b.loadGraphic((new org.flixel.FlxSprite()).createGraphic(104,15,0xff3a5c39),(new org.flixel.FlxSprite()).createGraphic(104,15,0xff729954));
				t1 = new org.flixel.FlxText(8,1,100,"music by danny B");
				t1.color = 0x729954;
				t2 = new org.flixel.FlxText(t1.x,t1.y,t1.width,t1.text);
				t2.color = 0xd8eba2;
				b.loadText(t1,t2);
				this.add(b);
				
				//play button
				this.add((new org.flixel.FlxSprite(t1m+1,org.flixel.FlxG.height/3+137)).createGraphic(106,19,0xff131c1b));
				t1 = new org.flixel.FlxText(t1m,org.flixel.FlxG.height/3+139,110,"PRESS X+C TO PLAY.");
				t1.color = 0x729954;
				t1.alignment = "center";
				this.add(t1);
				this._b$7 = new org.flixel.FlxButton(t1m+2,org.flixel.FlxG.height/3+138,$$bound(this,"onButton$7"));
				this._b$7.loadGraphic((new org.flixel.FlxSprite()).createGraphic(104,15,0xff3a5c39),(new org.flixel.FlxSprite()).createGraphic(104,15,0xff729954));
				t1 = new org.flixel.FlxText(25,1,100,"CLICK HERE");
				t1.color = 0x729954;
				t2 = new org.flixel.FlxText(t1.x,t1.y,t1.width,t1.text);
				t2.color = 0xd8eba2;
				this._b$7.loadText(t1,t2);
				this.add(this._b$7);
			}
			
			//X + C were pressed, fade out and change to play state
			if(this._ok$7 && !this._ok2$7 && org.flixel.FlxG.keys.X && org.flixel.FlxG.keys.C)
			{
				this._ok2$7 = true;
				org.flixel.FlxG.play(this.SndHit2$7);
				org.flixel.FlxG.flash.start(0xffd8eba2,0.5);
				org.flixel.FlxG.fade.start(0xff131c1b,1,$$bound(this,"onFade$7"));
			}

			this.update$7();
		},
		
		"private function onFlixel",function onFlixel()/*:void*/
		{
			org.flixel.FlxU.openURL("http://flixel.org");
		},
		
		"private function onDanny",function onDanny()/*:void*/
		{
			org.flixel.FlxU.openURL("http://dbsoundworks.com");
		},
		
		"private function onButton",function onButton()/*:void*/
		{
			this._b$7.visible = false;
			this._b$7.active = false;
			org.flixel.FlxG.play(this.SndHit2$7);
		},
		
		"private function onFade",function onFade()/*:void*/
		{
			org.flixel.FlxG.state = new com.adamatomic.Mode.PlayState();
			//FlxG.state = new PlayStateTiles();
		},
	];},[],["org.flixel.FlxState","resource:com/adamatomic/Mode/../../../data/spawner_gibs.png","resource:com/adamatomic/Mode/../../../data/cursor.png","resource:com/adamatomic/Mode/../../../data/menu_hit.mp3","resource:com/adamatomic/Mode/../../../data/menu_hit_2.mp3","org.flixel.FlxEmitter","org.flixel.FlxG","org.flixel.FlxText","org.flixel.FlxSave","org.flixel.FlxU","org.flixel.FlxSprite","org.flixel.FlxButton","com.adamatomic.Mode.PlayState"], "0.8.0", "0.8.2-SNAPSHOT"
);