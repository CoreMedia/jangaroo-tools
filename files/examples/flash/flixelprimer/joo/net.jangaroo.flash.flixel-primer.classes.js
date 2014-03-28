// class de.pixelate.flixelprimer.Alien
joo.classLoader.prepare("package de.pixelate.flixelprimer",
"public class Alien extends org.flixel.FlxSprite",5,function($$private){;return[

{Embed:{source:"de/pixelate/flixelprimer/../../../../assets/png/Alien.png"}},"private var",{ImgAlien:null},
"public function Alien",function(x,y)
{
this.super$5(x,y,this.ImgAlien$5);
this.velocity.x=-200;
},
"override public function update",function()
{
this.velocity.y=Math.cos(this.x/50)*50;
this.update$5();
},
];},[],["org.flixel.FlxSprite","resource:de/pixelate/flixelprimer/../../../../assets/png/Alien.png","Math"], "0.8.0", "0.8.3"
);
// class de.pixelate.flixelprimer.Bullet
joo.classLoader.prepare("package de.pixelate.flixelprimer",
"public class Bullet extends org.flixel.FlxSprite",5,function($$private){;return[

"public function Bullet",function(x,y)
{
this.super$5(x,y);
this.createGraphic(16,4,0xFF597137);
this.velocity.x=1000;
},
];},[],["org.flixel.FlxSprite"], "0.8.0", "0.8.3"
);
// class de.pixelate.flixelprimer.PlayState
joo.classLoader.prepare("package de.pixelate.flixelprimer",
"public class PlayState extends org.flixel.FlxState",7,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:"de/pixelate/flixelprimer/../../../../assets/mp3/ExplosionShip.mp3"}},"private var",{SoundExplosionShip:null},
{Embed:{source:"de/pixelate/flixelprimer/../../../../assets/mp3/ExplosionAlien.mp3"}},"private var",{SoundExplosionAlien:null},
{Embed:{source:"de/pixelate/flixelprimer/../../../../assets/mp3/Bullet.mp3"}},"private var",{SoundBullet:null},
"private var",{_ship:null},
"private var",{_aliens:null},
"private var",{_bullets:null},
"private var",{_scoreText:null},
"private var",{_gameOverText:null},
"private var",{_spawnTimer:NaN},
"private var",{_spawnInterval:2.5},
"override public function create",function()
{
org.flixel.FlxG.score=0;
org.flixel.FlxState.bgColor=0xFFABCC7D;
this._ship$7=new de.pixelate.flixelprimer.Ship();
this.add(this._ship$7);
this._aliens$7=new org.flixel.FlxGroup();
this.add(this._aliens$7);
this._bullets$7=new org.flixel.FlxGroup();
this.add(this._bullets$7);
this._scoreText$7=new org.flixel.FlxText(10,8,200,"0");
this._scoreText$7.setFormat(null,32,0xFF597137,"left");
this.add(this._scoreText$7);
this.resetSpawnTimer$7();
this.create$7();
},
"override public function update",function()
{
org.flixel.FlxU.overlap(this._aliens$7,this._bullets$7,$$bound(this,"overlapAlienBullet$7"));
org.flixel.FlxU.overlap(this._aliens$7,this._ship$7,$$bound(this,"overlapAlienShip$7"));
if(org.flixel.FlxG.keys.justPressed("SPACE")&&this._ship$7.dead==false)
{
this.spawnBullet$7(this._ship$7.getBulletSpawnPosition());
}
if(org.flixel.FlxG.keys.ENTER&&this._ship$7.dead)
{
org.flixel.FlxG.state=new de.pixelate.flixelprimer.PlayState();
}
this._spawnTimer$7-=org.flixel.FlxG.elapsed;
if(this._spawnTimer$7<0)
{
this.spawnAlien$7();
this.resetSpawnTimer$7();
}
this.update$7();
},
"private function spawnAlien",function()
{
var x=org.flixel.FlxG.width;
var y=Math.random()*(org.flixel.FlxG.height-100)+50;
this._aliens$7.add(new de.pixelate.flixelprimer.Alien(x,y));
},
"private function spawnBullet",function(p)
{
var bullet=new de.pixelate.flixelprimer.Bullet(p.x,p.y);
this._bullets$7.add(bullet);
org.flixel.FlxG.play(this.SoundBullet$7);
},
"private function resetSpawnTimer",function()
{
this._spawnTimer$7=this._spawnInterval$7;
this._spawnInterval$7*=0.95;
if(this._spawnInterval$7<0.1)
{
this._spawnInterval$7=0.1;
}
},
"private function overlapAlienBullet",function(alien,bullet)
{
var emitter=this.createEmitter$7();
emitter.at(alien);
alien.kill();
bullet.kill();
org.flixel.FlxG.play(this.SoundExplosionAlien$7);
org.flixel.FlxG.score+=1;
this._scoreText$7.text=org.flixel.FlxG.score.toString();
},
"private function overlapAlienShip",function(alien,ship)
{
var emitter=this.createEmitter$7();
emitter.at(ship);
ship.kill();
alien.kill();
org.flixel.FlxG.quake.start(0.02);
org.flixel.FlxG.play(this.SoundExplosionShip$7);
this._gameOverText$7=new org.flixel.FlxText(0,org.flixel.FlxG.height/2,org.flixel.FlxG.width,"GAME OVER\nPRESS ENTER TO PLAY AGAIN");
this._gameOverText$7.setFormat(null,16,0xFF597137,"center");
this.add(this._gameOverText$7);
},
"private function createEmitter",function()
{
var emitter=new org.flixel.FlxEmitter();
emitter.delay=1;
emitter.gravity=0;
emitter.maxRotation=0;
emitter.setXSpeed(-500,500);
emitter.setYSpeed(-500,500);
var particles=10;
for(var i=0;i<particles;i++)
{
var particle=new org.flixel.FlxSprite();
particle.createGraphic(2,2,0xFF597137);
particle.exists=false;
emitter.add(particle);
}
emitter.start();
this.add(emitter);
return emitter;
},
];},[],["org.flixel.FlxState","resource:de/pixelate/flixelprimer/../../../../assets/mp3/ExplosionShip.mp3","resource:de/pixelate/flixelprimer/../../../../assets/mp3/ExplosionAlien.mp3","resource:de/pixelate/flixelprimer/../../../../assets/mp3/Bullet.mp3","org.flixel.FlxG","de.pixelate.flixelprimer.Ship","org.flixel.FlxGroup","org.flixel.FlxText","org.flixel.FlxU","Math","de.pixelate.flixelprimer.Alien","de.pixelate.flixelprimer.Bullet","org.flixel.FlxEmitter","org.flixel.FlxSprite"], "0.8.0", "0.8.3"
);
// class de.pixelate.flixelprimer.Ship
joo.classLoader.prepare("package de.pixelate.flixelprimer",
"public class Ship extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:"de/pixelate/flixelprimer/../../../../assets/png/Ship.png"}},"private var",{ImgShip:null},
"public function Ship",function()
{
this.super$5(50,50,this.ImgShip$5);
},
"override public function update",function()
{
this.velocity.x=0;
this.velocity.y=0;
if(org.flixel.FlxG.keys.LEFT)
{
this.velocity.x=-250;
}
else if(org.flixel.FlxG.keys.RIGHT)
{
this.velocity.x=250;
}
if(org.flixel.FlxG.keys.UP)
{
this.velocity.y=-250;
}
else if(org.flixel.FlxG.keys.DOWN)
{
this.velocity.y=250;
}
this.update$5();
if(this.x>org.flixel.FlxG.width-this.width-16)
{
this.x=org.flixel.FlxG.width-this.width-16;
}
else if(this.x<16)
{
this.x=16;
}
if(this.y>org.flixel.FlxG.height-this.height-16)
{
this.y=org.flixel.FlxG.height-this.height-16;
}
else if(this.y<16)
{
this.y=16;
}
},
"public function getBulletSpawnPosition",function()
{
var p=new org.flixel.FlxPoint(this.x+36,this.y+12);
return p;
},
];},[],["org.flixel.FlxSprite","resource:de/pixelate/flixelprimer/../../../../assets/png/Ship.png","org.flixel.FlxG","org.flixel.FlxPoint"], "0.8.0", "0.8.3"
);
// class Main
joo.classLoader.prepare(
"package",
{SWF:{width:"640",height:"480",backgroundColor:"#ABCC7D"}},
{Frame:{factoryClass:"Preloader"}},
"public class Main extends org.flixel.FlxGame",7,function($$private){;return[function(){joo.classLoader.init(de.pixelate.flixelprimer.PlayState);},

"public function Main",function()
{
this.super$7(640,480,de.pixelate.flixelprimer.PlayState,1);
},
];},[],["org.flixel.FlxGame","de.pixelate.flixelprimer.PlayState"], "0.8.0", "0.8.3"
);
// class Preloader
joo.classLoader.prepare("package",
"public class Preloader extends org.flixel.FlxPreloader",8,function($$private){;return[

"public function Preloader",function()
{
this.className="Main";
this.super$8();
},
];},[],["org.flixel.FlxPreloader"], "0.8.0", "0.8.3"
);
