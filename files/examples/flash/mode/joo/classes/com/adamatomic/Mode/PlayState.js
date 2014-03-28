joo.classLoader.prepare("package com.adamatomic.Mode",/*
{
	import org.flixel.**/

	"public class PlayState extends org.flixel.FlxState",7,function($$private){var as=joo.as,is=joo.is,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		{Embed:{source:"com/adamatomic/Mode/../../../data/tech_tiles.png"}}, "protected var",{ ImgTech/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/dirt_top.png"}}, "protected var",{ ImgDirtTop/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/dirt.png"}}, "protected var",{ ImgDirt/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/notch.png"}}, "protected var",{ ImgNotch/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/mode.mp3"}}, "protected var",{ SndMode/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/countdown.mp3"}}, "protected var",{ SndCount/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/gibs.png"}}, "private var",{ ImgGibs/*:Class*/:null},
		{Embed:{source:"com/adamatomic/Mode/../../../data/spawner_gibs.png"}}, "private var",{ ImgSpawnerGibs/*:Class*/:null},
		
		//major game objects
		"protected var",{ _blocks/*:FlxGroup*/:null},
		"protected var",{ _decorations/*:FlxGroup*/:null},
		"protected var",{ _bullets/*:FlxGroup*/:null},
		"protected var",{ _player/*:Player*/:null},
		"protected var",{ _bots/*:FlxGroup*/:null},
		"protected var",{ _spawners/*:FlxGroup*/:null},
		"protected var",{ _botBullets/*:FlxGroup*/:null},
		"protected var",{ _littleGibs/*:FlxEmitter*/:null},
		"protected var",{ _bigGibs/*:FlxEmitter*/:null},
		
		//meta groups, to help speed up collisions
		"protected var",{ _objects/*:FlxGroup*/:null},
		"protected var",{ _enemies/*:FlxGroup*/:null},
		
		//HUD
		"protected var",{ _score/*:FlxText*/:null},
		"protected var",{ _score2/*:FlxText*/:null},
		"protected var",{ _scoreTimer/*:Number*/:NaN},
		"protected var",{ _jamTimer/*:Number*/:NaN},
		"protected var",{ _jamBar/*:FlxSprite*/:null},
		"protected var",{ _jamText/*:FlxText*/:null},
		"protected var",{ _notches/*:Array*/:null},
		
		//just to prevent weirdness during level transition
		"protected var",{ _fading/*:Boolean*/:false},
		
		//used to safely reload the playstate after dying
		"public var",{ reload/*:Boolean*/:false},
		
		"override public function create",function create()/*:void*/
		{
			org.flixel.FlxG.mouse.hide();
			this.reload = false;
			
			//get the gibs set up and out of the way
			this._littleGibs = new org.flixel.FlxEmitter();
			this._littleGibs.delay = 3;
			this._littleGibs.setXSpeed(-150,150);
			this._littleGibs.setYSpeed(-200,0);
			this._littleGibs.setRotation(-720,-720);
			this._littleGibs.createSprites(this.ImgGibs$7,100,10,true,0.5,0.65);
			this._bigGibs = new org.flixel.FlxEmitter();
			this._bigGibs.setXSpeed(-200,200);
			this._bigGibs.setYSpeed(-300,0);
			this._bigGibs.setRotation(-720,-720);
			this._bigGibs.createSprites(this.ImgSpawnerGibs$7,50,20,true,0.5,0.35);
			
			//level generation needs to know about the spawners (and thusly the bots, players, etc)
			this._blocks = new org.flixel.FlxGroup();
			this._decorations = new org.flixel.FlxGroup();
			this._bullets = new org.flixel.FlxGroup();
			this._player = new com.adamatomic.Mode.Player(316,300,this._bullets.members,this._littleGibs);
			this._bots = new org.flixel.FlxGroup();
			this._botBullets = new org.flixel.FlxGroup();
			this._spawners = new org.flixel.FlxGroup();
			
			//simple procedural level generation
			var i/*:uint*/;
			var r/*:uint*/ = 160;
			var b/*:FlxTileblock*/;
			
			b = new org.flixel.FlxTileblock(0,0,640,16);
			b.loadGraphic(this.ImgTech);
			this._blocks.add(b);
			
			b = new org.flixel.FlxTileblock(0,16,16,640-16);
			b.loadGraphic(this.ImgTech);
			this._blocks.add(b);
			
			b = new org.flixel.FlxTileblock(640-16,16,16,640-16);
			b.loadGraphic(this.ImgTech);
			this._blocks.add(b);
			
			b = new org.flixel.FlxTileblock(16,640-24,640-32,8);
			b.loadGraphic(this.ImgDirtTop);
			this._blocks.add(b);
			
			b = new org.flixel.FlxTileblock(16,640-16,640-32,16);
			b.loadGraphic(this.ImgDirt);
			this._blocks.add(b);
			
			this.buildRoom(r*0,r*0,true);
			this.buildRoom(r*1,r*0);
			this.buildRoom(r*2,r*0);
			this.buildRoom(r*3,r*0,true);
			this.buildRoom(r*0,r*1,true);
			this.buildRoom(r*1,r*1);
			this.buildRoom(r*2,r*1);
			this.buildRoom(r*3,r*1,true);
			this.buildRoom(r*0,r*2);
			this.buildRoom(r*1,r*2);
			this.buildRoom(r*2,r*2);
			this.buildRoom(r*3,r*2);
			this.buildRoom(r*0,r*3,true);
			this.buildRoom(r*1,r*3);
			this.buildRoom(r*2,r*3);
			this.buildRoom(r*3,r*3,true);
			
			//Add bots and spawners after we add blocks to the state,
			// so that they're drawn on top of the level, and so that
			// the bots are drawn on top of both the blocks + the spawners.
			this.add(this._spawners);
			this.add(this._littleGibs);
			this.add(this._bigGibs);
			this.add(this._blocks);
			this.add(this._decorations);
			this.add(this._bots);
			
			//actually create the bullets now
			for(i = 0; i < 50; i++)
				this._botBullets.add(new com.adamatomic.Mode.BotBullet());
			for(i = 0; i < 8; i++)
				this._bullets.add(new com.adamatomic.Mode.Bullet());

			//add player and set up scrolling camera
			this.add(this._player);
			org.flixel.FlxG.follow(this._player,2.5);
			org.flixel.FlxG.followAdjust(0.5,0.0);
			org.flixel.FlxG.followBounds(0,0,640,640);
			
			//add gibs + bullets to scene here, so they're drawn on top of pretty much everything
			this.add(this._botBullets);
			this.add(this._bullets);
			
			//finally we are going to sort things into a couple of helper groups.
			//we don't add these to the state, we just use them for collisions later!
			this._enemies = new org.flixel.FlxGroup();
			this._enemies.add(this._botBullets);
			this._enemies.add(this._spawners);
			this._enemies.add(this._bots);
			this._objects = new org.flixel.FlxGroup();
			this._objects.add(this._botBullets);
			this._objects.add(this._bullets);
			this._objects.add(this._bots);
			this._objects.add(this._player);
			this._objects.add(this._littleGibs);
			this._objects.add(this._bigGibs);
			
			//HUD - score
			var ssf/*:FlxPoint*/ = new org.flixel.FlxPoint(0,0);
			this._score = new org.flixel.FlxText(0,0,org.flixel.FlxG.width);
			this._score.color = 0xd8eba2;
			this._score.size = 16;
			this._score.alignment = "center";
			this._score.scrollFactor = ssf;
			this._score.shadow = 0x131c1b;
			this.add(this._score);
			if(org.flixel.FlxG.scores.length < 2)
			{
				org.flixel.FlxG.scores.push(0);
				org.flixel.FlxG.scores.push(0);
			}
			
			//HUD - highest and last scores
			this._score2 = new org.flixel.FlxText(org.flixel.FlxG.width/2,0,org.flixel.FlxG.width/2);
			this._score2.color = 0xd8eba2;
			this._score2.alignment = "right";
			this._score2.scrollFactor = ssf;
			this._score2.shadow = this._score.shadow;
			this.add(this._score2);
			if(org.flixel.FlxG.score > org.flixel.FlxG.scores[0])
				org.flixel.FlxG.scores[0] = org.flixel.FlxG.score;
			if(org.flixel.FlxG.scores[0] != 0)
				this._score2.text = "HIGHEST: "+org.flixel.FlxG.scores[0]+"\nLAST: "+org.flixel.FlxG.score;
			org.flixel.FlxG.score = 0;
			this._scoreTimer = 0;
			
			//HUD - the "number of spawns left" icons
			this._notches = new Array();
			var tmp/*:FlxSprite*/;
			for(i = 0; i < 6; i++)
			{
				tmp = new org.flixel.FlxSprite(4+i*10,4);
				tmp.loadGraphic(this.ImgNotch,true);
				tmp.scrollFactor.x = tmp.scrollFactor.y = 0;
				tmp.addAnimation("on",[0]);
				tmp.addAnimation("off",[1]);
				tmp.moves = false;
				tmp.solid = false;
				tmp.play("on");
				this._notches.push(this.add(tmp));
			}
			
			//HUD - the "gun jammed" notification
			this._jamBar =as( this.add((new org.flixel.FlxSprite(0,org.flixel.FlxG.height-22)).createGraphic(org.flixel.FlxG.width,24,0xff131c1b)),  org.flixel.FlxSprite);
			this._jamBar.scrollFactor.x = this._jamBar.scrollFactor.y = 0;
			this._jamBar.visible = false;
			this._jamText = new org.flixel.FlxText(0,org.flixel.FlxG.height-22,org.flixel.FlxG.width,"GUN IS JAMMED");
			this._jamText.color = 0xd8eba2;
			this._jamText.size = 16;
			this._jamText.alignment = "center";
			this._jamText.scrollFactor = ssf;
			this._jamText.visible = false;
			this.add(this._jamText);
			
			org.flixel.FlxG.playMusic(this.SndMode);
			org.flixel.FlxG.flash.start(0xff131c1b);
			this._fading = false;
		},

		"override public function update",function update()/*:void*/
		{
			var os/*:uint*/ = org.flixel.FlxG.score;
			
			this.update$7();
			
			//collisions with environment
			org.flixel.FlxU.collide(this._blocks,this._objects);
			org.flixel.FlxU.overlap(this._enemies,this._player,$$bound(this,"overlapped"));
			org.flixel.FlxU.overlap(this._bullets,this._enemies,$$bound(this,"overlapped"));
			
			//Jammed message
			if(org.flixel.FlxG.keys.justPressed("C") && this._player.flickering())
			{
				this._jamTimer = 1;
				this._jamBar.visible = true;
				this._jamText.visible = true;
			}
			if(this._jamTimer > 0)
			{
				if(!this._player.flickering()) this._jamTimer = 0;
				this._jamTimer -= org.flixel.FlxG.elapsed;
				if(this._jamTimer < 0)
				{
					this._jamBar.visible = false;
					this._jamText.visible = false;
				}
			}

			if(!this._fading)
			{
				//Score + countdown stuffs
				if(os != org.flixel.FlxG.score) this._scoreTimer = 2;
				this._scoreTimer -= org.flixel.FlxG.elapsed;
				if(this._scoreTimer < 0)
				{
					if(org.flixel.FlxG.score > 0) 
					{
						org.flixel.FlxG.play(this.SndCount);
						if(org.flixel.FlxG.score > 100) org.flixel.FlxG.score -= 100;
						else { org.flixel.FlxG.score = 0; this._player.kill(); }
						this._scoreTimer = 1;
						if(org.flixel.FlxG.score < 600)
							org.flixel.FlxG.play(this.SndCount);
						if(org.flixel.FlxG.score < 500)
							org.flixel.FlxG.play(this.SndCount);
						if(org.flixel.FlxG.score < 400)
							org.flixel.FlxG.play(this.SndCount);
						if(org.flixel.FlxG.score < 300)
							org.flixel.FlxG.play(this.SndCount);
						if(org.flixel.FlxG.score < 200)
							org.flixel.FlxG.play(this.SndCount);
					}
				}
			
				//Fade out to victory screen stuffs
				var spawnerCount/*:int*/ = this._spawners.countLiving();
				if(spawnerCount <= 0)
				{
					this._fading = true;
					org.flixel.FlxG.fade.start(0xffd8eba2,3,$$bound(this,"onVictory"));
				}
				else
				{
					var l/*:uint*/ = this._notches.length;
					for(var i/*:uint*/ = 0; i < l; i++)
					{
						if(i < spawnerCount)
							this._notches[i].play("on");
						else
							this._notches[i].play("off");
					}
				}
			}
			
			//actually update score text if it changed
			if(os != org.flixel.FlxG.score)
			{
				if(this._player.dead) org.flixel.FlxG.score = 0;
				this._score.text = org.flixel.FlxG.score.toString();
			}
			
			if(this.reload)
				org.flixel.FlxG.state = new com.adamatomic.Mode.PlayState();
			
			//Toggle the bounding box visibility
			if(org.flixel.FlxG.keys.justPressed("B"))
				org.flixel.FlxG.showBounds = !org.flixel.FlxG.showBounds;
		},

		"protected function overlapped",function overlapped(Object1/*:FlxObject*/,Object2/*:FlxObject*/)/*:void*/
		{
			if((is(Object1,  com.adamatomic.Mode.BotBullet)) || (is(Object1,  com.adamatomic.Mode.Bullet)))
				Object1.kill();
			Object2.hurt(1);
		},
		
		"protected function onVictory",function onVictory()/*:void*/
		{
			org.flixel.FlxG.music.stop();
			org.flixel.FlxG.state = new com.adamatomic.Mode.VictoryState();
		},
		
		//Just plops down a spawner and some blocks - haphazard and crappy atm but functional!
		"protected function buildRoom",function buildRoom(RX/*:uint*/,RY/*:uint*/,Spawners/*:Boolean=false*/)/*:void*/
		{switch(arguments.length){case 0:case 1:case 2:Spawners=false;}
			//first place the spawn point (if necessary)
			var rw/*:uint*/ = 20;
			var sx/*:uint*/;
			var sy/*:uint*/;
			if(Spawners)
			{
				sx = $$uint(2+org.flixel.FlxU.random()*(rw-7));
				sy = $$uint(2+org.flixel.FlxU.random()*(rw-7));
			}
			
			//then place a bunch of blocks
			var numBlocks/*:uint*/ = $$uint(3+org.flixel.FlxU.random()*4);
			if(!Spawners) numBlocks++;
			var maxW/*:uint*/ = 10;
			var minW/*:uint*/ = 2;
			var maxH/*:uint*/ = 8;
			var minH/*:uint*/ = 1;
			var bx/*:uint*/;
			var by/*:uint*/;
			var bw/*:uint*/;
			var bh/*:uint*/;
			var check/*:Boolean*/;
			for(var i/*:uint*/ = 0; i < numBlocks; i++)
			{
				check = false;
				do
				{
					//keep generating different specs if they overlap the spawner
					bw = $$uint(minW + org.flixel.FlxU.random()*(maxW-minW));
					bh = $$uint(minH + org.flixel.FlxU.random()*(maxH-minH));
					bx = $$uint(-1 + org.flixel.FlxU.random()*(rw+1-bw));
					by = $$uint(-1 + org.flixel.FlxU.random()*(rw+1-bh));
					if(Spawners)
						check = ((sx>bx+bw) || (sx+3<bx) || (sy>by+bh) || (sy+3<by));
					else
						check = true;
				} while(!check);
				
				var b/*:FlxTileblock*/;
				
				b = new org.flixel.FlxTileblock(RX+bx*8,RY+by*8,bw*8,bh*8);
				b.loadTiles(this.ImgTech);
				this._blocks.add(b);
				
				//If the block has room, add some non-colliding "dirt" graphics for variety
				if((bw >= 4) && (bh >= 5))
				{
					b = new org.flixel.FlxTileblock(RX+bx*8+8,RY+by*8,bw*8-16,8);
					b.loadTiles(this.ImgDirtTop);
					this._decorations.add(b);
					
					b = new org.flixel.FlxTileblock(RX+bx*8+8,RY+by*8+8,bw*8-16,bh*8-24);
					b.loadTiles(this.ImgDirt);
					this._decorations.add(b);
				}
			}
			
			//Finally actually add the spawner
			if(Spawners)
				this._spawners.add(new com.adamatomic.Mode.Spawner(RX+sx*8,RY+sy*8,this._bigGibs,this._bots,this._botBullets.members,this._littleGibs,this._player));
		},
	];},[],["org.flixel.FlxState","resource:com/adamatomic/Mode/../../../data/tech_tiles.png","resource:com/adamatomic/Mode/../../../data/dirt_top.png","resource:com/adamatomic/Mode/../../../data/dirt.png","resource:com/adamatomic/Mode/../../../data/notch.png","resource:com/adamatomic/Mode/../../../data/mode.mp3","resource:com/adamatomic/Mode/../../../data/countdown.mp3","resource:com/adamatomic/Mode/../../../data/gibs.png","resource:com/adamatomic/Mode/../../../data/spawner_gibs.png","org.flixel.FlxG","org.flixel.FlxEmitter","org.flixel.FlxGroup","com.adamatomic.Mode.Player","org.flixel.FlxTileblock","com.adamatomic.Mode.BotBullet","com.adamatomic.Mode.Bullet","org.flixel.FlxPoint","org.flixel.FlxText","Array","org.flixel.FlxSprite","org.flixel.FlxU","com.adamatomic.Mode.VictoryState","uint","com.adamatomic.Mode.Spawner"], "0.8.0", "0.8.2-SNAPSHOT"
);