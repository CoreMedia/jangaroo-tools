joo.classLoader.prepare("package de.pixelate.flixelprimer",/*
{
	import org.flixel.**/

	"public class PlayState extends org.flixel.FlxState",7,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);},
			
		{Embed:{source:"de/pixelate/flixelprimer/../../../../assets/mp3/ExplosionShip.mp3"}}, "private var",{ SoundExplosionShip/*:Class*/:null},
		{Embed:{source:"de/pixelate/flixelprimer/../../../../assets/mp3/ExplosionAlien.mp3"}}, "private var",{ SoundExplosionAlien/*:Class*/:null},
		{Embed:{source:"de/pixelate/flixelprimer/../../../../assets/mp3/Bullet.mp3"}}, "private var",{ SoundBullet/*:Class*/:null},

		"private var",{ _ship/*: Ship*/:null},
		"private var",{ _aliens/*: FlxGroup*/:null},
		"private var",{ _bullets/*: FlxGroup*/:null},		
		"private var",{ _scoreText/*: FlxText*/:null},
		"private var",{ _gameOverText/*: FlxText*/:null},
		"private var",{ _spawnTimer/*: Number*/:NaN},
		"private var",{ _spawnInterval/*: Number*/ : 2.5},
		
		"override public function create",function create()/*:void*/
		{
			org.flixel.FlxG.score = 0;
			
			org.flixel.FlxState.bgColor = 0xFFABCC7D;
			
			this._ship$7 = new de.pixelate.flixelprimer.Ship();			
			this.add(this._ship$7);
			
			this._aliens$7 = new org.flixel.FlxGroup();
			this.add(this._aliens$7);
			
			this._bullets$7 = new org.flixel.FlxGroup();
			this.add(this._bullets$7);

			this._scoreText$7 = new org.flixel.FlxText(10, 8, 200, "0");
			this._scoreText$7.setFormat(null, 32, 0xFF597137, "left");
			this.add(this._scoreText$7);
			
			this.resetSpawnTimer$7();
						
			this.create$7();
		},

		"override public function update",function update()/*:void*/
		{			
			org.flixel.FlxU.overlap(this._aliens$7, this._bullets$7, $$bound(this,"overlapAlienBullet$7"));
			org.flixel.FlxU.overlap(this._aliens$7, this._ship$7, $$bound(this,"overlapAlienShip$7"));
			
			if(org.flixel.FlxG.keys.justPressed("SPACE") && this._ship$7.dead == false)
			{
				this.spawnBullet$7(this._ship$7.getBulletSpawnPosition());					
			}

			if(org.flixel.FlxG.keys.ENTER && this._ship$7.dead)
			{				
				org.flixel.FlxG.state = new de.pixelate.flixelprimer.PlayState();
			}
			
			this._spawnTimer$7 -= org.flixel.FlxG.elapsed;
			
			if(this._spawnTimer$7 < 0)
			{
				this.spawnAlien$7();
				this.resetSpawnTimer$7();
			}
						
			this.update$7();
		},
		
		"private function spawnAlien",function spawnAlien()/*:void*/
		{
			var x/*: Number*/ = org.flixel.FlxG.width;
			var y/*: Number*/ = Math.random() * (org.flixel.FlxG.height - 100) + 50;
			this._aliens$7.add(new de.pixelate.flixelprimer.Alien(x, y));
		},	
		
		"private function spawnBullet",function spawnBullet(p/*: FlxPoint*/)/*:void*/
		{
			var bullet/*: Bullet*/ = new de.pixelate.flixelprimer.Bullet(p.x, p.y);
			this._bullets$7.add(bullet);
			org.flixel.FlxG.play(this.SoundBullet$7);
		},	
		
		"private function resetSpawnTimer",function resetSpawnTimer()/*:void*/
		{
			this._spawnTimer$7 = this._spawnInterval$7;			
			this._spawnInterval$7 *= 0.95;
			if(this._spawnInterval$7 < 0.1)
			{
				this._spawnInterval$7 = 0.1;
			}
		},
		
		"private function overlapAlienBullet",function overlapAlienBullet(alien/*: Alien*/, bullet/*: Bullet*/)/*:void*/
		{
			var emitter/*:FlxEmitter*/ = this.createEmitter$7();
			emitter.at(alien);		
			alien.kill();
			bullet.kill();	
			org.flixel.FlxG.play(this.SoundExplosionAlien$7);	
			org.flixel.FlxG.score += 1;
			this._scoreText$7.text = org.flixel.FlxG.score.toString();					
		},
		
		"private function overlapAlienShip",function overlapAlienShip(alien/*: Alien*/, ship/*: Ship*/)/*:void*/
		{
			var emitter/*:FlxEmitter*/ = this.createEmitter$7();
			emitter.at(ship);			
			ship.kill();
			alien.kill();
			org.flixel.FlxG.quake.start(0.02);
			org.flixel.FlxG.play(this.SoundExplosionShip$7);	
			
			this._gameOverText$7 = new org.flixel.FlxText(0, org.flixel.FlxG.height / 2, org.flixel.FlxG.width, "GAME OVER\nPRESS ENTER TO PLAY AGAIN");					
			this._gameOverText$7.setFormat(null, 16, 0xFF597137, "center");
			this.add(this._gameOverText$7);
		},
				
		"private function createEmitter",function createEmitter()/*:FlxEmitter*/
		{
			var emitter/*:FlxEmitter*/ = new org.flixel.FlxEmitter();
			emitter.delay = 1;
			emitter.gravity = 0;
			emitter.maxRotation = 0;
			emitter.setXSpeed(-500, 500);
			emitter.setYSpeed(-500, 500);		
			var particles/*: int*/ = 10;
			for(var i/*: int*/ = 0; i < particles; i++)
			{
				var particle/*:FlxSprite*/ = new org.flixel.FlxSprite();
				particle.createGraphic(2, 2, 0xFF597137);
				particle.exists = false;
				emitter.add(particle);
			}
			emitter.start();
			this.add(emitter);
			return emitter;		
		},
	];},[],["org.flixel.FlxState","resource:de/pixelate/flixelprimer/../../../../assets/mp3/ExplosionShip.mp3","resource:de/pixelate/flixelprimer/../../../../assets/mp3/ExplosionAlien.mp3","resource:de/pixelate/flixelprimer/../../../../assets/mp3/Bullet.mp3","org.flixel.FlxG","de.pixelate.flixelprimer.Ship","org.flixel.FlxGroup","org.flixel.FlxText","org.flixel.FlxU","Math","de.pixelate.flixelprimer.Alien","de.pixelate.flixelprimer.Bullet","org.flixel.FlxEmitter","org.flixel.FlxSprite"], "0.8.0", "0.8.3"
);