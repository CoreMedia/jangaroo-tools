joo.classLoader.prepare("package com.adamatomic.Mode",/*
{
	import org.flixel.**/

	"public class VictoryState extends org.flixel.FlxState",7,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		{Embed:{source:"com/adamatomic/Mode/../../../data/spawner_gibs.png"}}, "private var",{ ImgGibs/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/menu_hit_2.mp3"}}, "private var",{ SndMenu/*:Class*/:null},
		
		"private var",{ _timer/*:Number*/:NaN},
		"private var",{ _fading/*:Boolean*/:false},

		"override public function create",function create()/*:void*/
		{
			this._timer$7 = 0;
			this._fading$7 = false;
			org.flixel.FlxG.flash.start(0xffd8eba2);
			
			//Gibs emitted upon death
			var gibs/*:FlxEmitter*/ = new org.flixel.FlxEmitter(0,-50);
			gibs.setSize(org.flixel.FlxG.width,0);
			gibs.setXSpeed();
			gibs.setYSpeed(0,100);
			gibs.setRotation(-360,360);
			gibs.gravity = 80;
			gibs.createSprites(this.ImgGibs$7,800,32);
			this.add(gibs);
			gibs.start(false,0.005);
			
			this.add((new org.flixel.FlxText(0,org.flixel.FlxG.height/2-35,org.flixel.FlxG.width,"VICTORY\n\nSCORE: "+org.flixel.FlxG.score)).setFormat(null,16,0xd8eba2,"center"));
		},

		"override public function update",function update()/*:void*/
		{
			this.update$7();
			if(!this._fading$7)
			{
				this._timer$7 += org.flixel.FlxG.elapsed;
				if((this._timer$7 > 0.35) && ((this._timer$7 > 10) || org.flixel.FlxG.keys.justPressed("X") || org.flixel.FlxG.keys.justPressed("C")))
				{
					this._fading$7 = true;
					org.flixel.FlxG.play(this.SndMenu$7);
					org.flixel.FlxG.fade.start(0xff131c1b,2,$$bound(this,"onPlay$7"));
				}
			}
		},
		
		"private function onPlay",function onPlay()/*:void*/ 
		{
			org.flixel.FlxG.state = new com.adamatomic.Mode.PlayState();
		},
	];},[],["org.flixel.FlxState","resource:com/adamatomic/Mode/../../../data/spawner_gibs.png","resource:com/adamatomic/Mode/../../../data/menu_hit_2.mp3","org.flixel.FlxG","org.flixel.FlxEmitter","org.flixel.FlxText","com.adamatomic.Mode.PlayState"], "0.8.0", "0.8.2-SNAPSHOT"
);