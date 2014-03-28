joo.classLoader.prepare("package com.adamatomic.Mode",/*
{
	import org.flixel.**/

	"public class Spawner extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		{Embed:{source:"com/adamatomic/Mode/../../../data/spawner.png"}}, "private var",{ ImgSpawner/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/asplode.mp3"}}, "private var",{ SndExplode/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/menu_hit_2.mp3"}}, "private var",{ SndExplode2/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/hit.mp3"}}, "private var",{ SndHit/*:Class*/:null},
		
		"private var",{ _timer/*:Number*/:NaN},
		"private var",{ _bots/*:FlxGroup*/:null},
		"private var",{ _botBullets/*:Array*/:null},
		"private var",{ _botGibs/*:FlxEmitter*/:null},
		"private var",{ _gibs/*:FlxEmitter*/:null},
		"private var",{ _player/*:Player*/:null},
		"private var",{ _open/*:Boolean*/:false},
		
		"public function Spawner",function Spawner$(X/*:int*/, Y/*:int*/,Gibs/*:FlxEmitter*/,Bots/*:FlxGroup*/,BotBullets/*:Array*/,BotGibs/*:FlxEmitter*/,ThePlayer/*:Player*/)
		{
			this.super$5(X,Y);
			this.loadGraphic(this.ImgSpawner$5,true);
			this._gibs$5 = Gibs;
			this._bots$5 = Bots;
			this._botBullets$5 = BotBullets;
			this._botGibs$5 = BotGibs;
			this._player$5 = ThePlayer;
			this._timer$5 = Math.random()*20;
			this._open$5 = false;
			this.health = 8;

			this.addAnimation("open", [1, 2, 3, 4, 5], 40, false);
			this.addAnimation("close", [4, 3, 2, 1, 0], 40, false);
			this.addAnimation("dead", [6]);
		},
		
		"override public function update",function update()/*:void*/
		{
			this._timer$5 += org.flixel.FlxG.elapsed;
			var limit/*:uint*/ = 20;
			if(this.onScreen())
				limit = 4;
			if(this._timer$5 > limit)
			{
				this._timer$5 = 0;
				this.makeBot();
			}
			else if(this._timer$5 > limit - 0.35)
			{
				if(!this._open$5)
				{
					this._open$5 = true;
					this.play("open");
				}
			}
			else if(this._timer$5 > 1)
			{
				if(this._open$5)
				{
					this.play("close");
					this._open$5 = false;
				}
			}
				
			this.update$5();
		},
		
		"override public function hurt",function hurt(Damage/*:Number*/)/*:void*/
		{
			org.flixel.FlxG.play(this.SndHit$5);
			this.flicker(0.2);
			org.flixel.FlxG.score += 50;
			this.hurt$5(Damage);
		},
		
		"override public function kill",function kill()/*:void*/
		{
			if(this.dead)
				return;
			org.flixel.FlxG.play(this.SndExplode$5);
			org.flixel.FlxG.play(this.SndExplode2$5);
			this.kill$5();
			this.active = false;
			this.exists = true;
			this.solid = false;
			this.flicker(-1);
			this.play("dead");
			org.flixel.FlxG.quake.start(0.005,0.35);
			org.flixel.FlxG.flash.start(0xffd8eba2,0.35);
			this.makeBot();
			this._gibs$5.at(this);
			this._gibs$5.start(true,3,0);
			org.flixel.FlxG.score += 1000;
		},
		
		"protected function makeBot",function makeBot()/*:void*/
		{
			//Try to recycle a dead bot
			if(this._bots$5.resetFirstAvail(this.x + this.width/2 - 6, this.y + this.height/2 - 6))
				return;
			
			//If there weren't any non-existent ones to respawn, just add a new one instead
			var bot/*:Bot*/ = new com.adamatomic.Mode.Bot(this.x + this.width/2, this.y + this.height/2, this._botBullets$5, this._botGibs$5, this._player$5);
			bot.x -= bot.width/2;
			bot.y -= bot.height/2;
			this._bots$5.add(bot);
		},
	];},[],["org.flixel.FlxSprite","resource:com/adamatomic/Mode/../../../data/spawner.png","resource:com/adamatomic/Mode/../../../data/asplode.mp3","resource:com/adamatomic/Mode/../../../data/menu_hit_2.mp3","resource:com/adamatomic/Mode/../../../data/hit.mp3","Math","org.flixel.FlxG","com.adamatomic.Mode.Bot"], "0.8.0", "0.8.2-SNAPSHOT"
);