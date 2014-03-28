joo.classLoader.prepare("package com.chipacabra.Jumper",/*
{
	import org.flixel.**/
 
	"public class PlayState extends org.flixel.FlxState",7,function($$private){var is=joo.is,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(com.chipacabra.Jumper.Coin,org.flixel.FlxG,com.chipacabra.Jumper.Lurker,com.chipacabra.Jumper.Enemy);},
	
		{Embed:{source : 'com/chipacabra/Jumper/../../../../levels/mapCSV_Group1_Map1.csv', mimeType : 'application/octet-stream'}},"public var",{ levelMap/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../levels/mapCSV_Group1_Map1back.csv', mimeType : 'application/octet-stream'}},"public var",{ backgroundMap/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../levels/mapCSV_Group1_Ladders.csv', mimeType : 'application/octet-stream'}},"public var",{ laddersMap/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../data/monstacoords.csv', mimeType : 'application/octet-stream'}},"public var",{ monstaList/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../data/lurkcoords.csv', mimeType : 'application/octet-stream'}},"public var",{ lurkList/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../data/coins.csv', mimeType : 'application/octet-stream'}},"public var",{ coinList/*:Class*/:null},
		
		{Embed:{source : 'com/chipacabra/Jumper/../../../../art/area02_level_tiles2.png'}},"public var",{ levelTiles/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../art/lizgibs.png'}},"public var",{ imgLizGibs/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../art/spikegibs.png'}},"public var",{ imgSpikeGibs/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../music/ScrollingSpace[1].mp3'}},"public var",{ bgMusic/*:Class*/:null},

		
		"public var",{ map/*:FlxTilemap*/ :function(){return( new org.flixel.FlxTilemap);}},
		"public var",{ background/*:FlxTilemap*/ :function(){return( new org.flixel.FlxTilemap);}},
		"public var",{ ladders/*:FlxTilemap*/ :function(){return( new org.flixel.FlxTilemap);}},
		"public var",{ player/*:Player*/:null},
		//public var skelmonsta:Enemy;

		
		"protected var",{ _gibs/*:FlxEmitter*/:null},
		"protected var",{ _mongibs/*:FlxEmitter*/:null},
		"protected var",{ _bullets/*:FlxGroup*/:null},
		"protected var",{ _badbullets/*:FlxGroup*/:null},
		"protected var",{ _restart/*:Boolean*/:false},
		"protected var",{ _text1/*:FlxText*/:null},
		"protected var",{ _enemies/*:FlxGroup*/:null},
		"protected var",{ _coins/*:FlxGroup*/:null},
		"protected var",{ _score/*:FlxText*/:null},
		
		"override public function create",function create()/*:void*/
		{
			this._restart = false;
			// Set up the game over text
			this._text1 = new org.flixel.FlxText(30, 30, 400, "Press R to Restart");
			this._text1.visible = false;
			this._text1.size = 40;
			this._text1.color = 0xFFFF0000;
			this._text1.antialiasing = true;
			this._text1.scrollFactor.x = this._text1.scrollFactor.y = 0;
			
			// Set up the gibs
			this._gibs= new org.flixel.FlxEmitter();
			this._gibs.delay = 3;
			this._gibs.setXSpeed( -150, 150);
			this._gibs.setYSpeed( -200, 0);
			this._gibs.setRotation( -720, 720);
			this._gibs.createSprites(this.imgLizGibs, 25, 15, true, .5, 0.65);
			
			this._mongibs = new org.flixel.FlxEmitter();
			this._mongibs.delay = 3;
			this._mongibs.setXSpeed( -150, 150);
			this._mongibs.setYSpeed( -200, 0);
			this._mongibs.setRotation( -720, 720);
			this._mongibs.createSprites(this.imgSpikeGibs, 25, 15, true, .5, .65);
			
			// Create the actual group of bullets here
			this._bullets = new org.flixel.FlxGroup;
			this._badbullets = new org.flixel.FlxGroup;
			
			this.add(this.background.loadMap(new this.backgroundMap, this.levelTiles, 16, 16));
			this.background.scrollFactor.x = this.background.scrollFactor.y = .5;
			
			this.add(this.map.loadMap(new this.levelMap, this.levelTiles, 16, 16));
			this.add(this.ladders.loadMap(new this.laddersMap, this.levelTiles, 16, 16));
			
			org.flixel.FlxU.setWorldBounds(0, 0, this.map.width, this.map.height);
			
			this.add(this.player = new com.chipacabra.Jumper.Player(112, 92,this,this._gibs,this._bullets));
			
			org.flixel.FlxG.follow(this.player,1); // Attach the camera to the player. The number is how much to lag the camera to smooth things out
			org.flixel.FlxG.followAdjust(0,0); // Adjust the camera speed with this
			org.flixel.FlxG.followBounds(0, 0, 1600, 800);
			
			//add(skelmonsta = new Enemy(1260, 640, player, _mongibs));// I used DAME to find the coordinates I want.
			
			// Set up the enemies here
			this._enemies = new org.flixel.FlxGroup;
			this.placeMonsters$7(new this.monstaList, com.chipacabra.Jumper.Enemy);
			this.placeMonsters$7(new this.lurkList, com.chipacabra.Jumper.Lurker);
			
			this._coins = new org.flixel.FlxGroup;
			this.placeCoins$7(new this.coinList, com.chipacabra.Jumper.Coin);
			
			this.add(this._coins);
			this.add(this._enemies);
			
			org.flixel.FlxG.score = 0;
			
			this.create$7();
			
			// Set up the individual bullets
			for (var i/*:uint*/ = 0; i < 4; i++)    // Allow 4 bullets at a time
				this._bullets.add(new com.chipacabra.Jumper.Bullet());
			this.add(this._badbullets);
			this.add(this._bullets); 
			this.add(this._gibs);
			this.add(this._mongibs);
			
			
			//HUD - score
			var ssf/*:FlxPoint*/ = new org.flixel.FlxPoint(0,0);
			this._score = new org.flixel.FlxText(0,0,org.flixel.FlxG.width);
			this._score.color = 0xFFFF00;
			this._score.size = 16;
			this._score.alignment = "center";
			this._score.scrollFactor = ssf;
			this._score.shadow = 0x131c1b;
			this.add(this._score);
			
			this.add(this._text1); // Add last so it goes on top, you know the drill.
			
			org.flixel.FlxG.playMusic(this.bgMusic, .5);
		},
		
		"override public function update",function update()/*:void*/ 
		{
			this.update$7();
			this.player.collide(this.map);
			this._enemies.collide(this.map);
			this._gibs.collide(this.map);
			this._bullets.collide(this.map);
			this._badbullets.collide(this.map);
			
			this._score.text = '$' + org.flixel.FlxG.score.toString();
			
			if (org.flixel.FlxG.keys.justPressed("B"))
			{
				org.flixel.FlxG.showBounds = !org.flixel.FlxG.showBounds;
			}
			if (this.player.dead)
			{
				this._text1.visible = true;
				if (org.flixel.FlxG.keys.justPressed("R")) this._restart = true;
			}
			
			//Check for impact!
/*			if (player.overlaps(_enemies))
			{
				player.kill(); // This should probably be more interesting
			}*/
			
			org.flixel.FlxU.overlap(this.player, this._enemies, $$bound(this,"hitPlayer$7"));
			org.flixel.FlxU.overlap(this._bullets, this._enemies, $$bound(this,"hitmonster$7"));
			org.flixel.FlxU.overlap(this.player, this._coins, $$bound(this,"collectCoin$7"));
			org.flixel.FlxU.overlap(this.player, this._badbullets, $$bound(this,"hitPlayer$7"));
			
			if (this._restart) org.flixel.FlxG.state = new com.chipacabra.Jumper.PlayState;
			
		},
		
		"private function collectCoin",function collectCoin(P/*:FlxObject*/, C/*:FlxObject*/)/*:void*/ 
		{
			C.kill();
		},
		
		"private function hitPlayer",function hitPlayer(P/*:FlxObject*/,Monster/*:FlxObject*/)/*:void*/ 
		{
			if (is(Monster,  com.chipacabra.Jumper.Bullet))
				Monster.kill();
			if (Monster.health >0)
				P.hurt(1); // This should still be more interesting
		},
		
		"private function hitmonster",function hitmonster(Blt/*:FlxObject*/, Monster/*:FlxObject*/)/*:void*/ 
		{
			if (Monster.dead) { return;}  // Just in case
			if (Monster.health > 0) 
			{
				Blt.kill();
				Monster.hurt(1);
			}
		},
		
		"private function placeMonsters",function placeMonsters(MonsterData/*:String*/, Monster/*:Class*/)/*:void*/
		{
			var coords/*:Array*/;
			var entities/*:Array*/ = MonsterData.split("\n");   // Each line becomes an entry in the array of strings
			for (var j/*:int*/ = 0; j < entities.length; j++) 
			{
				coords = entities[j].split(",");  //Split each line into two coordinates
				if (Monster == com.chipacabra.Jumper.Enemy)
				{this._enemies.add(new Monster($$uint(coords[0]), $$uint(coords[1]), this.player, this._mongibs)); }
				else if (Monster == com.chipacabra.Jumper.Lurker)
				{ this._enemies.add(new Monster($$uint(coords[0]), $$uint(coords[1]), this.player, this._badbullets));}
				else if (Monster!=null)
				{ this._enemies.add(new Monster($$uint(coords[0]), $$uint(coords[1]), this.player));}
			}
		},
		
		"private function placeCoins",function placeCoins(CoinData/*:String*/, Sparkle/*:Class*/)/*:void*/ 
		{
			var coords/*:Array*/;
			var entities/*:Array*/ = CoinData.split("\n");   // Each line becomes an entry in the array of strings
			for (var j/*:int*/ = 0; j < entities.length; j++) 
			{
				coords = entities[j].split(",");  //Split each line into two coordinates
				if (Sparkle == com.chipacabra.Jumper.Coin)
				{this._coins.add(new com.chipacabra.Jumper.Coin($$uint(coords[0]), $$uint(coords[1]))); }
			}
		},
	"public function PlayState",function PlayState$(){this.super$7();this.map=this.map();this.background=this.background();this.ladders=this.ladders();}];},[],["org.flixel.FlxState","resource:com/chipacabra/Jumper/../../../../levels/mapCSV_Group1_Map1.csv","resource:com/chipacabra/Jumper/../../../../levels/mapCSV_Group1_Map1back.csv","resource:com/chipacabra/Jumper/../../../../levels/mapCSV_Group1_Ladders.csv","resource:com/chipacabra/Jumper/../../../../data/monstacoords.csv","resource:com/chipacabra/Jumper/../../../../data/lurkcoords.csv","resource:com/chipacabra/Jumper/../../../../data/coins.csv","resource:com/chipacabra/Jumper/../../../../art/area02_level_tiles2.png","resource:com/chipacabra/Jumper/../../../../art/lizgibs.png","resource:com/chipacabra/Jumper/../../../../art/spikegibs.png","resource:com/chipacabra/Jumper/../../../../music/ScrollingSpace[1].mp3","org.flixel.FlxTilemap","org.flixel.FlxText","org.flixel.FlxEmitter","org.flixel.FlxGroup","org.flixel.FlxU","com.chipacabra.Jumper.Player","org.flixel.FlxG","com.chipacabra.Jumper.Enemy","com.chipacabra.Jumper.Lurker","com.chipacabra.Jumper.Coin","com.chipacabra.Jumper.Bullet","org.flixel.FlxPoint","uint"], "0.8.0", "0.8.2-SNAPSHOT"
);