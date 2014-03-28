joo.classLoader.prepare("package com.adamatomic.Mode",/*
{
	import org.flixel.**/

	"public class PlayStateTiles extends org.flixel.FlxState",7,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		{Embed:{source:"com/adamatomic/Mode/../../../data/mode.mp3"}}, "private var",{ SndMode/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/map.txt",mimeType:"application/octet-stream"}}, "private var",{ TxtMap/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/map2.txt",mimeType:"application/octet-stream"}}, "private var",{ TxtMap2/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/tiles_all.png"}}, "private var",{ ImgTiles/*:Class*/:null},
		
		//major game objects
		"private var",{ _tilemap/*:FlxTilemap*/:null},
		"private var",{ _bullets/*:FlxGroup*/:null},
		"private var",{ _player/*:Player*/:null},
		
		"override public function create",function create()/*:void*/
		{
			//create tilemap
			this._tilemap$7 = new org.flixel.FlxTilemap();
			this._tilemap$7.collideIndex = 3;
			this._tilemap$7.loadMap(new this.TxtMap$7,this.ImgTiles$7,8);
			//_tilemap.loadMap(new TxtMap2,ImgTiles,8); //This is an alternate tiny map
			
			//create player and bullets
			this._bullets$7 = new org.flixel.FlxGroup();
			this._player$7 = new com.adamatomic.Mode.Player(this._tilemap$7.width/2-4,this._tilemap$7.height/2-4,this._bullets$7.members,null);
			for(var i/*:uint*/ = 0; i < 8; i++)
				this._bullets$7.add(new com.adamatomic.Mode.Bullet());
			this.add(this._bullets$7);
			
			//add player and set up camera
			this.add(this._player$7);
			org.flixel.FlxG.follow(this._player$7,2.5);
			org.flixel.FlxG.followAdjust(0.5,0.0);
			this._tilemap$7.follow();	//Set the followBounds to the map dimensions
			
			//Uncomment these lines if you want to center TxtMap2
			//var fx:uint = _tilemap.width/2 - FlxG.width/2;
			//var fy:uint = _tilemap.height/2 - FlxG.height/2;
			//FlxG.followBounds(fx,fy,fx,fy);
			
			//add tilemap last so it is in front, looks neat
			this.add(this._tilemap$7);
			
			//fade in
			org.flixel.FlxG.flash.start(0xff131c1b);
			
			//The music in this mode is positional - it fades out toward the edges of the level
			var s/*:FlxSound*/ = org.flixel.FlxG.play(this.SndMode$7,1,true);
			s.proximity(320,320,this._player$7,160);
		},

		"override public function update",function update()/*:void*/
		{
			this.update$7();
			this._tilemap$7.collide(this._player$7);
			this._tilemap$7.collide(this._bullets$7);
			
			//Toggle the bounding box visibility
			if(org.flixel.FlxG.keys.justPressed("B"))
				org.flixel.FlxG.showBounds = !org.flixel.FlxG.showBounds;
		},
	];},[],["org.flixel.FlxState","resource:com/adamatomic/Mode/../../../data/mode.mp3","resource:com/adamatomic/Mode/../../../data/map.txt","resource:com/adamatomic/Mode/../../../data/map2.txt","resource:com/adamatomic/Mode/../../../data/tiles_all.png","org.flixel.FlxTilemap","org.flixel.FlxGroup","com.adamatomic.Mode.Player","com.adamatomic.Mode.Bullet","org.flixel.FlxG"], "0.8.0", "0.8.2-SNAPSHOT"
);