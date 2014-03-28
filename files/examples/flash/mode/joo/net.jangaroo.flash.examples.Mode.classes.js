// class com.adamatomic.Mode.Bot
joo.classLoader.prepare("package com.adamatomic.Mode",
"public class Bot extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:"com/adamatomic/Mode/../../../data/bot.png"}},"protected var",{ImgBot:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/jet.png"}},"protected var",{ImgJet:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/asplode.mp3"}},"protected var",{SndExplode:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/hit.mp3"}},"protected var",{SndHit:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/jet.mp3"}},"protected var",{SndJet:null},
"protected var",{_gibs:null},
"protected var",{_jets:null},
"protected var",{_player:null},
"protected var",{_timer:NaN},
"protected var",{_b:null},
"static protected var",{_cb:0},
"protected var",{_shotClock:NaN},
"public function Bot",function(xPos,yPos,Bullets,Gibs,ThePlayer)
{
this.super$5(xPos,yPos);
this.loadRotatedGraphic(this.ImgBot,32,0);
this._player=ThePlayer;
this._b=Bullets;
this._gibs=Gibs;
this.width=12;
this.height=12;
this.offset.x=2;
this.offset.y=2;
this.maxAngular=120;
this.angularDrag=400;
this.maxThrust=100;
this.drag.x=80;
this.drag.y=80;
this._jets=new org.flixel.FlxEmitter();
this._jets.setRotation();
this._jets.gravity=0;
this._jets.createSprites(this.ImgJet,15,0,false);
this.reset(this.x,this.y);
},
"override public function update",function()
{
var ot=this._timer;
if((this._timer==0)&&this.onScreen())org.flixel.FlxG.play(this.SndJet);
this._timer+=org.flixel.FlxG.elapsed;
if((ot<8)&&(this._timer>=8))
this._jets.stop(0.1);
var dx=this.x-this._player.x;
var dy=this.y-this._player.y;
var da=org.flixel.FlxU.getAngle(dx,dy);
if(da<0)
da+=360;
var ac=this.angle;
if(ac<0)
ac+=360;
if(da<this.angle)
this.angularAcceleration=-this.angularDrag;
else if(da>this.angle)
this.angularAcceleration=this.angularDrag;
else
this.angularAcceleration=0;
this.thrust=0;
if(this._timer>9)
this._timer=0;
else if(this._timer<8)
{
this.thrust=40;
var v=org.flixel.FlxU.rotatePoint(this.thrust,0,0,0,this.angle);
this._jets.at(this);
this._jets.setXSpeed(v.x-30,v.x+30);
this._jets.setYSpeed(v.y-30,v.y+30);
if(!this._jets.on)
this._jets.start(false,0.01,0);
}
if(this.onScreen())
{
var os=this._shotClock;
this._shotClock+=org.flixel.FlxG.elapsed;
if((os<4.0)&&(this._shotClock>=4.0))
{
this._shotClock=0;
this.shoot();
}
else if((os<3.5)&&(this._shotClock>=3.5))
this.shoot();
else if((os<3.0)&&(this._shotClock>=3.0))
this.shoot();
}
this._jets.update();
this.update$5();
},
"override public function render",function()
{
this._jets.render();
this.render$5();
},
"override public function hurt",function(Damage)
{
org.flixel.FlxG.play(this.SndHit);
this.flicker(0.2);
org.flixel.FlxG.score+=10;
this.hurt$5(Damage);
},
"override public function kill",function()
{
if(this.dead)
return;
org.flixel.FlxG.play(this.SndExplode);
this.kill$5();
this.flicker(-1);
this._jets.kill();
this._gibs.at(this);
this._gibs.start(true,0,20);
org.flixel.FlxG.score+=200;
},
"override public function reset",function(X,Y)
{
this.reset$5(X,Y);
this.thrust=0;
this.velocity.x=0;
this.velocity.y=0;
this.angle=Math.random()*360-180;
this.health=2;
this._timer=0;
this._shotClock=0;
},
"protected function shoot",function()
{
var ba=org.flixel.FlxU.rotatePoint(-120,0,0,0,this.angle);
this._b[com.adamatomic.Mode.Bot._cb].shoot(this.x+this.width/2-2,this.y+this.height/2-2,ba.x,ba.y);
if(++com.adamatomic.Mode.Bot._cb>=this._b.length)com.adamatomic.Mode.Bot._cb=0;
},
];},[],["org.flixel.FlxSprite","resource:com/adamatomic/Mode/../../../data/bot.png","resource:com/adamatomic/Mode/../../../data/jet.png","resource:com/adamatomic/Mode/../../../data/asplode.mp3","resource:com/adamatomic/Mode/../../../data/hit.mp3","resource:com/adamatomic/Mode/../../../data/jet.mp3","org.flixel.FlxEmitter","org.flixel.FlxG","org.flixel.FlxU","Math"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class com.adamatomic.Mode.BotBullet
joo.classLoader.prepare("package com.adamatomic.Mode",
"public class BotBullet extends org.flixel.FlxSprite",5,function($$private){;return[

{Embed:{source:"com/adamatomic/Mode/../../../data/bot_bullet.png"}},"private var",{ImgBullet:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/jump.mp3"}},"private var",{SndHit:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/enemy.mp3"}},"private var",{SndShoot:null},
"public function BotBullet",function()
{
this.super$5();
this.loadGraphic(this.ImgBullet$5,true);
this.addAnimation("idle",[0,1],50);
this.addAnimation("poof",[2,3,4],50,false);
this.exists=false;
},
"override public function update",function()
{
if(this.dead&&this.finished)this.exists=false;
else this.update$5();
},
"override public function hitSide",function(Contact,Velocity){this.kill();},
"override public function hitBottom",function(Contact,Velocity){this.kill();},
"override public function hitTop",function(Contact,Velocity){this.kill();},
"override public function kill",function()
{
if(this.dead)return;
this.velocity.x=0;
this.velocity.y=0;
if(this.onScreen())org.flixel.FlxG.play(this.SndHit$5);
this.dead=true;
this.solid=false;
this.play("poof");
},
"public function shoot",function(X,Y,VelocityX,VelocityY)
{
org.flixel.FlxG.play(this.SndShoot$5,0.5);
this.reset(X,Y);
this.solid=true;
this.velocity.x=VelocityX;
this.velocity.y=VelocityY;
this.play("idle");
},
];},[],["org.flixel.FlxSprite","resource:com/adamatomic/Mode/../../../data/bot_bullet.png","resource:com/adamatomic/Mode/../../../data/jump.mp3","resource:com/adamatomic/Mode/../../../data/enemy.mp3","org.flixel.FlxG"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class com.adamatomic.Mode.Bullet
joo.classLoader.prepare("package com.adamatomic.Mode",
"public class Bullet extends org.flixel.FlxSprite",5,function($$private){;return[

{Embed:{source:"com/adamatomic/Mode/../../../data/bullet.png"}},"private var",{ImgBullet:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/jump.mp3"}},"private var",{SndHit:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/shoot.mp3"}},"private var",{SndShoot:null},
"public function Bullet",function()
{
this.super$5();
this.loadGraphic(this.ImgBullet$5,true);
this.width=6;
this.height=6;
this.offset.x=1;
this.offset.y=1;
this.exists=false;
this.addAnimation("up",[0]);
this.addAnimation("down",[1]);
this.addAnimation("left",[2]);
this.addAnimation("right",[3]);
this.addAnimation("poof",[4,5,6,7],50,false);
},
"override public function update",function()
{
if(this.dead&&this.finished)this.exists=false;
else this.update$5();
},
"override public function render",function()
{
this.render$5();
},
"override public function hitSide",function(Contact,Velocity){this.kill();},
"override public function hitBottom",function(Contact,Velocity){this.kill();},
"override public function hitTop",function(Contact,Velocity){this.kill();},
"override public function kill",function()
{
if(this.dead)return;
this.velocity.x=0;
this.velocity.y=0;
if(this.onScreen())org.flixel.FlxG.play(this.SndHit$5);
this.dead=true;
this.solid=false;
this.play("poof");
},
"public function shoot",function(X,Y,VelocityX,VelocityY)
{
org.flixel.FlxG.play(this.SndShoot$5);
this.reset(X,Y);
this.solid=true;
this.velocity.x=VelocityX;
this.velocity.y=VelocityY;
if(this.velocity.y<0)
this.play("up");
else if(this.velocity.y>0)
this.play("down");
else if(this.velocity.x<0)
this.play("left");
else if(this.velocity.x>0)
this.play("right");
},
];},[],["org.flixel.FlxSprite","resource:com/adamatomic/Mode/../../../data/bullet.png","resource:com/adamatomic/Mode/../../../data/jump.mp3","resource:com/adamatomic/Mode/../../../data/shoot.mp3","org.flixel.FlxG"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class com.adamatomic.Mode.MenuState
joo.classLoader.prepare("package com.adamatomic.Mode",
"public class MenuState extends org.flixel.FlxState",7,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:"com/adamatomic/Mode/../../../data/spawner_gibs.png"}},"private var",{ImgGibs:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/cursor.png"}},"private var",{ImgCursor:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/menu_hit.mp3"}},"private var",{SndHit:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/menu_hit_2.mp3"}},"private var",{SndHit2:null},
"private var",{_gibs:null},
"private var",{_b:null},
"private var",{_t1:null},
"private var",{_t2:null},
"private var",{_ok:false},
"private var",{_ok2:false},
"override public function create",function()
{
var i;
var s;
this._gibs$7=new org.flixel.FlxEmitter(org.flixel.FlxG.width/2-50,org.flixel.FlxG.height/2-10);
this._gibs$7.setSize(100,30);
this._gibs$7.setYSpeed(-200,-20);
this._gibs$7.setRotation(-720,720);
this._gibs$7.gravity=100;
this._gibs$7.createSprites(this.ImgGibs$7,1000,32);
this.add(this._gibs$7);
this._t1$7=new org.flixel.FlxText(org.flixel.FlxG.width,org.flixel.FlxG.height/3,80,"mo");
this._t1$7.size=32;
this._t1$7.color=0x3a5c39;
this._t1$7.antialiasing=true;
this.add(this._t1$7);
this._t2$7=new org.flixel.FlxText(-60,org.flixel.FlxG.height/3,80,"de");
this._t2$7.size=this._t1$7.size;
this._t2$7.color=this._t1$7.color;
this._t2$7.antialiasing=this._t1$7.antialiasing;
this.add(this._t2$7);
this._ok$7=false;
this._ok2$7=false;
org.flixel.FlxG.mouse.show(this.ImgCursor$7);
var save=new org.flixel.FlxSave();
if(save.bind("Mode"))
{
if(save.data.plays==null)
save.data.plays=0;
else
save.data.plays++;
org.flixel.FlxG.log("Number of plays: "+save.data.plays);
}
},
"override public function update",function()
{
var t1m=org.flixel.FlxG.width/2-54;
if(this._t1$7.x>t1m)
{
this._t1$7.x-=org.flixel.FlxG.elapsed*org.flixel.FlxG.width;
if(this._t1$7.x<t1m)this._t1$7.x=t1m;
}
var t2m=org.flixel.FlxG.width/2+6;
if(this._t2$7.x<t2m)
{
this._t2$7.x+=org.flixel.FlxG.elapsed*org.flixel.FlxG.width;
if(this._t2$7.x>t2m)this._t2$7.x=t2m;
}
if(!this._ok$7&&((this._t1$7.x==t1m)||(this._t2$7.x==t2m)))
{
this._ok$7=true;
org.flixel.FlxG.play(this.SndHit$7);
org.flixel.FlxG.flash.start(0xffd8eba2,0.5);
org.flixel.FlxG.quake.start(0.035,0.5);
this._t1$7.color=0xd8eba2;
this._t2$7.color=0xd8eba2;
this._gibs$7.start(true,5);
this._t1$7.angle=org.flixel.FlxU.random()*40-20;
this._t2$7.angle=org.flixel.FlxU.random()*40-20;
var t1;
var t2;
var b;
t1=new org.flixel.FlxText(t1m,org.flixel.FlxG.height/3+39,110,"by Adam Atomic");
t1.alignment="center";
t1.color=0x3a5c39;
this.add(t1);
this.add((new org.flixel.FlxSprite(t1m+1,org.flixel.FlxG.height/3+53)).createGraphic(106,19,0xff131c1b));
b=new org.flixel.FlxButton(t1m+2,org.flixel.FlxG.height/3+54,$$bound(this,"onFlixel$7"));
b.loadGraphic((new org.flixel.FlxSprite()).createGraphic(104,15,0xff3a5c39),(new org.flixel.FlxSprite()).createGraphic(104,15,0xff729954));
t1=new org.flixel.FlxText(15,1,100,"www.flixel.org");
t1.color=0x729954;
t2=new org.flixel.FlxText(t1.x,t1.y,t1.width,t1.text);
t2.color=0xd8eba2;
b.loadText(t1,t2);
this.add(b);
this.add((new org.flixel.FlxSprite(t1m+1,org.flixel.FlxG.height/3+75)).createGraphic(106,19,0xff131c1b));
b=new org.flixel.FlxButton(t1m+2,org.flixel.FlxG.height/3+76,$$bound(this,"onDanny$7"));
b.loadGraphic((new org.flixel.FlxSprite()).createGraphic(104,15,0xff3a5c39),(new org.flixel.FlxSprite()).createGraphic(104,15,0xff729954));
t1=new org.flixel.FlxText(8,1,100,"music by danny B");
t1.color=0x729954;
t2=new org.flixel.FlxText(t1.x,t1.y,t1.width,t1.text);
t2.color=0xd8eba2;
b.loadText(t1,t2);
this.add(b);
this.add((new org.flixel.FlxSprite(t1m+1,org.flixel.FlxG.height/3+137)).createGraphic(106,19,0xff131c1b));
t1=new org.flixel.FlxText(t1m,org.flixel.FlxG.height/3+139,110,"PRESS X+C TO PLAY.");
t1.color=0x729954;
t1.alignment="center";
this.add(t1);
this._b$7=new org.flixel.FlxButton(t1m+2,org.flixel.FlxG.height/3+138,$$bound(this,"onButton$7"));
this._b$7.loadGraphic((new org.flixel.FlxSprite()).createGraphic(104,15,0xff3a5c39),(new org.flixel.FlxSprite()).createGraphic(104,15,0xff729954));
t1=new org.flixel.FlxText(25,1,100,"CLICK HERE");
t1.color=0x729954;
t2=new org.flixel.FlxText(t1.x,t1.y,t1.width,t1.text);
t2.color=0xd8eba2;
this._b$7.loadText(t1,t2);
this.add(this._b$7);
}
if(this._ok$7&&!this._ok2$7&&org.flixel.FlxG.keys.X&&org.flixel.FlxG.keys.C)
{
this._ok2$7=true;
org.flixel.FlxG.play(this.SndHit2$7);
org.flixel.FlxG.flash.start(0xffd8eba2,0.5);
org.flixel.FlxG.fade.start(0xff131c1b,1,$$bound(this,"onFade$7"));
}
this.update$7();
},
"private function onFlixel",function()
{
org.flixel.FlxU.openURL("http://flixel.org");
},
"private function onDanny",function()
{
org.flixel.FlxU.openURL("http://dbsoundworks.com");
},
"private function onButton",function()
{
this._b$7.visible=false;
this._b$7.active=false;
org.flixel.FlxG.play(this.SndHit2$7);
},
"private function onFade",function()
{
org.flixel.FlxG.state=new com.adamatomic.Mode.PlayState();
},
];},[],["org.flixel.FlxState","resource:com/adamatomic/Mode/../../../data/spawner_gibs.png","resource:com/adamatomic/Mode/../../../data/cursor.png","resource:com/adamatomic/Mode/../../../data/menu_hit.mp3","resource:com/adamatomic/Mode/../../../data/menu_hit_2.mp3","org.flixel.FlxEmitter","org.flixel.FlxG","org.flixel.FlxText","org.flixel.FlxSave","org.flixel.FlxU","org.flixel.FlxSprite","org.flixel.FlxButton","com.adamatomic.Mode.PlayState"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class com.adamatomic.Mode.Player
joo.classLoader.prepare("package com.adamatomic.Mode",
"public class Player extends org.flixel.FlxSprite",5,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:"com/adamatomic/Mode/../../../data/spaceman.png"}},"private var",{ImgSpaceman:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/jump.mp3"}},"private var",{SndJump:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/land.mp3"}},"private var",{SndLand:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/asplode.mp3"}},"private var",{SndExplode:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/menu_hit_2.mp3"}},"private var",{SndExplode2:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/hurt.mp3"}},"private var",{SndHurt:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/jam.mp3"}},"private var",{SndJam:null},
"private var",{_jumpPower:0},
"private var",{_bullets:null},
"private var",{_curBullet:0},
"private var",{_bulletVel:0},
"private var",{_up:false},
"private var",{_down:false},
"private var",{_restart:NaN},
"private var",{_gibs:null},
"public function Player",function(X,Y,Bullets,Gibs)
{
this.super$5(X,Y);
this.loadGraphic(this.ImgSpaceman$5,true,true,8);
this._restart$5=0;
this.width=6;
this.height=7;
this.offset.x=1;
this.offset.y=1;
var runSpeed=80;
this.drag.x=runSpeed*8;
this.acceleration.y=420;
this._jumpPower$5=200;
this.maxVelocity.x=runSpeed;
this.maxVelocity.y=this._jumpPower$5;
this.addAnimation("idle",[0]);
this.addAnimation("run",[1,2,3,0],12);
this.addAnimation("jump",[4]);
this.addAnimation("idle_up",[5]);
this.addAnimation("run_up",[6,7,8,5],12);
this.addAnimation("jump_up",[9]);
this.addAnimation("jump_down",[10]);
this._bullets$5=Bullets;
this._curBullet$5=0;
this._bulletVel$5=360;
this._gibs$5=Gibs;
},
"override public function update",function()
{
if(this.dead)
{
this._restart$5+=org.flixel.FlxG.elapsed;
if(this._restart$5>2)
(as(org.flixel.FlxG.state,com.adamatomic.Mode.PlayState)).reload=true;
return;
}
this.acceleration.x=0;
if(org.flixel.FlxG.keys.LEFT)
{
this.facing=org.flixel.FlxSprite.LEFT;
this.acceleration.x-=this.drag.x;
}
else if(org.flixel.FlxG.keys.RIGHT)
{
this.facing=org.flixel.FlxSprite.RIGHT;
this.acceleration.x+=this.drag.x;
}
if(org.flixel.FlxG.keys.justPressed("X")&&!this.velocity.y)
{
this.velocity.y=-this._jumpPower$5;
org.flixel.FlxG.play(this.SndJump$5);
}
this._up$5=false;
this._down$5=false;
if(org.flixel.FlxG.keys.UP)this._up$5=true;
else if(org.flixel.FlxG.keys.DOWN&&this.velocity.y)this._down$5=true;
if(this.velocity.y!=0)
{
if(this._up$5)this.play("jump_up");
else if(this._down$5)this.play("jump_down");
else this.play("jump");
}
else if(this.velocity.x==0)
{
if(this._up$5)this.play("idle_up");
else this.play("idle");
}
else
{
if(this._up$5)this.play("run_up");
else this.play("run");
}
if(!this.flickering()&&org.flixel.FlxG.keys.justPressed("C"))
{
var bXVel=0;
var bYVel=0;
var bX=this.x;
var bY=this.y;
if(this._up$5)
{
bY-=this._bullets$5[this._curBullet$5].height-4;
bYVel=-this._bulletVel$5;
}
else if(this._down$5)
{
bY+=this.height-4;
bYVel=this._bulletVel$5;
this.velocity.y-=36;
}
else if(this.facing==org.flixel.FlxSprite.RIGHT)
{
bX+=this.width-4;
bXVel=this._bulletVel$5;
}
else
{
bX-=this._bullets$5[this._curBullet$5].width-4;
bXVel=-this._bulletVel$5;
}
this._bullets$5[this._curBullet$5].shoot(bX,bY,bXVel,bYVel);
if(++this._curBullet$5>=this._bullets$5.length)
this._curBullet$5=0;
}
this.update$5();
if(this.flickering())
{
if(org.flixel.FlxG.keys.justPressed("C"))
org.flixel.FlxG.play(this.SndJam$5);
}
},
"override public function hitBottom",function(Contact,Velocity)
{
if(this.velocity.y>50)
org.flixel.FlxG.play(this.SndLand$5);
this.onFloor=true;
return this.hitBottom$5(Contact,Velocity);
},
"override public function hurt",function(Damage)
{
Damage=0;
if(this.flickering())
return;
org.flixel.FlxG.play(this.SndHurt$5);
this.flicker(1.3);
if(org.flixel.FlxG.score>1000)org.flixel.FlxG.score-=1000;
if(this.velocity.x>0)
this.velocity.x=-this.maxVelocity.x;
else
this.velocity.x=this.maxVelocity.x;
this.hurt$5(Damage);
},
"override public function kill",function()
{
if(this.dead)
return;
this.solid=false;
org.flixel.FlxG.play(this.SndExplode$5);
org.flixel.FlxG.play(this.SndExplode2$5);
this.kill$5();
this.flicker(-1);
this.exists=true;
this.visible=false;
org.flixel.FlxG.quake.start(0.005,0.35);
org.flixel.FlxG.flash.start(0xffd8eba2,0.35);
if(this._gibs$5!=null)
{
this._gibs$5.at(this);
this._gibs$5.start(true,0,50);
}
},
];},[],["org.flixel.FlxSprite","resource:com/adamatomic/Mode/../../../data/spaceman.png","resource:com/adamatomic/Mode/../../../data/jump.mp3","resource:com/adamatomic/Mode/../../../data/land.mp3","resource:com/adamatomic/Mode/../../../data/asplode.mp3","resource:com/adamatomic/Mode/../../../data/menu_hit_2.mp3","resource:com/adamatomic/Mode/../../../data/hurt.mp3","resource:com/adamatomic/Mode/../../../data/jam.mp3","org.flixel.FlxG","com.adamatomic.Mode.PlayState"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class com.adamatomic.Mode.PlayState
joo.classLoader.prepare("package com.adamatomic.Mode",
"public class PlayState extends org.flixel.FlxState",7,function($$private){var as=joo.as,is=joo.is,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:"com/adamatomic/Mode/../../../data/tech_tiles.png"}},"protected var",{ImgTech:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/dirt_top.png"}},"protected var",{ImgDirtTop:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/dirt.png"}},"protected var",{ImgDirt:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/notch.png"}},"protected var",{ImgNotch:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/mode.mp3"}},"protected var",{SndMode:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/countdown.mp3"}},"protected var",{SndCount:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/gibs.png"}},"private var",{ImgGibs:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/spawner_gibs.png"}},"private var",{ImgSpawnerGibs:null},
"protected var",{_blocks:null},
"protected var",{_decorations:null},
"protected var",{_bullets:null},
"protected var",{_player:null},
"protected var",{_bots:null},
"protected var",{_spawners:null},
"protected var",{_botBullets:null},
"protected var",{_littleGibs:null},
"protected var",{_bigGibs:null},
"protected var",{_objects:null},
"protected var",{_enemies:null},
"protected var",{_score:null},
"protected var",{_score2:null},
"protected var",{_scoreTimer:NaN},
"protected var",{_jamTimer:NaN},
"protected var",{_jamBar:null},
"protected var",{_jamText:null},
"protected var",{_notches:null},
"protected var",{_fading:false},
"public var",{reload:false},
"override public function create",function()
{
org.flixel.FlxG.mouse.hide();
this.reload=false;
this._littleGibs=new org.flixel.FlxEmitter();
this._littleGibs.delay=3;
this._littleGibs.setXSpeed(-150,150);
this._littleGibs.setYSpeed(-200,0);
this._littleGibs.setRotation(-720,-720);
this._littleGibs.createSprites(this.ImgGibs$7,100,10,true,0.5,0.65);
this._bigGibs=new org.flixel.FlxEmitter();
this._bigGibs.setXSpeed(-200,200);
this._bigGibs.setYSpeed(-300,0);
this._bigGibs.setRotation(-720,-720);
this._bigGibs.createSprites(this.ImgSpawnerGibs$7,50,20,true,0.5,0.35);
this._blocks=new org.flixel.FlxGroup();
this._decorations=new org.flixel.FlxGroup();
this._bullets=new org.flixel.FlxGroup();
this._player=new com.adamatomic.Mode.Player(316,300,this._bullets.members,this._littleGibs);
this._bots=new org.flixel.FlxGroup();
this._botBullets=new org.flixel.FlxGroup();
this._spawners=new org.flixel.FlxGroup();
var i;
var r=160;
var b;
b=new org.flixel.FlxTileblock(0,0,640,16);
b.loadGraphic(this.ImgTech);
this._blocks.add(b);
b=new org.flixel.FlxTileblock(0,16,16,640-16);
b.loadGraphic(this.ImgTech);
this._blocks.add(b);
b=new org.flixel.FlxTileblock(640-16,16,16,640-16);
b.loadGraphic(this.ImgTech);
this._blocks.add(b);
b=new org.flixel.FlxTileblock(16,640-24,640-32,8);
b.loadGraphic(this.ImgDirtTop);
this._blocks.add(b);
b=new org.flixel.FlxTileblock(16,640-16,640-32,16);
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
this.add(this._spawners);
this.add(this._littleGibs);
this.add(this._bigGibs);
this.add(this._blocks);
this.add(this._decorations);
this.add(this._bots);
for(i=0;i<50;i++)
this._botBullets.add(new com.adamatomic.Mode.BotBullet());
for(i=0;i<8;i++)
this._bullets.add(new com.adamatomic.Mode.Bullet());
this.add(this._player);
org.flixel.FlxG.follow(this._player,2.5);
org.flixel.FlxG.followAdjust(0.5,0.0);
org.flixel.FlxG.followBounds(0,0,640,640);
this.add(this._botBullets);
this.add(this._bullets);
this._enemies=new org.flixel.FlxGroup();
this._enemies.add(this._botBullets);
this._enemies.add(this._spawners);
this._enemies.add(this._bots);
this._objects=new org.flixel.FlxGroup();
this._objects.add(this._botBullets);
this._objects.add(this._bullets);
this._objects.add(this._bots);
this._objects.add(this._player);
this._objects.add(this._littleGibs);
this._objects.add(this._bigGibs);
var ssf=new org.flixel.FlxPoint(0,0);
this._score=new org.flixel.FlxText(0,0,org.flixel.FlxG.width);
this._score.color=0xd8eba2;
this._score.size=16;
this._score.alignment="center";
this._score.scrollFactor=ssf;
this._score.shadow=0x131c1b;
this.add(this._score);
if(org.flixel.FlxG.scores.length<2)
{
org.flixel.FlxG.scores.push(0);
org.flixel.FlxG.scores.push(0);
}
this._score2=new org.flixel.FlxText(org.flixel.FlxG.width/2,0,org.flixel.FlxG.width/2);
this._score2.color=0xd8eba2;
this._score2.alignment="right";
this._score2.scrollFactor=ssf;
this._score2.shadow=this._score.shadow;
this.add(this._score2);
if(org.flixel.FlxG.score>org.flixel.FlxG.scores[0])
org.flixel.FlxG.scores[0]=org.flixel.FlxG.score;
if(org.flixel.FlxG.scores[0]!=0)
this._score2.text="HIGHEST: "+org.flixel.FlxG.scores[0]+"\nLAST: "+org.flixel.FlxG.score;
org.flixel.FlxG.score=0;
this._scoreTimer=0;
this._notches=new Array();
var tmp;
for(i=0;i<6;i++)
{
tmp=new org.flixel.FlxSprite(4+i*10,4);
tmp.loadGraphic(this.ImgNotch,true);
tmp.scrollFactor.x=tmp.scrollFactor.y=0;
tmp.addAnimation("on",[0]);
tmp.addAnimation("off",[1]);
tmp.moves=false;
tmp.solid=false;
tmp.play("on");
this._notches.push(this.add(tmp));
}
this._jamBar=as(this.add((new org.flixel.FlxSprite(0,org.flixel.FlxG.height-22)).createGraphic(org.flixel.FlxG.width,24,0xff131c1b)),org.flixel.FlxSprite);
this._jamBar.scrollFactor.x=this._jamBar.scrollFactor.y=0;
this._jamBar.visible=false;
this._jamText=new org.flixel.FlxText(0,org.flixel.FlxG.height-22,org.flixel.FlxG.width,"GUN IS JAMMED");
this._jamText.color=0xd8eba2;
this._jamText.size=16;
this._jamText.alignment="center";
this._jamText.scrollFactor=ssf;
this._jamText.visible=false;
this.add(this._jamText);
org.flixel.FlxG.playMusic(this.SndMode);
org.flixel.FlxG.flash.start(0xff131c1b);
this._fading=false;
},
"override public function update",function()
{
var os=org.flixel.FlxG.score;
this.update$7();
org.flixel.FlxU.collide(this._blocks,this._objects);
org.flixel.FlxU.overlap(this._enemies,this._player,$$bound(this,"overlapped"));
org.flixel.FlxU.overlap(this._bullets,this._enemies,$$bound(this,"overlapped"));
if(org.flixel.FlxG.keys.justPressed("C")&&this._player.flickering())
{
this._jamTimer=1;
this._jamBar.visible=true;
this._jamText.visible=true;
}
if(this._jamTimer>0)
{
if(!this._player.flickering())this._jamTimer=0;
this._jamTimer-=org.flixel.FlxG.elapsed;
if(this._jamTimer<0)
{
this._jamBar.visible=false;
this._jamText.visible=false;
}
}
if(!this._fading)
{
if(os!=org.flixel.FlxG.score)this._scoreTimer=2;
this._scoreTimer-=org.flixel.FlxG.elapsed;
if(this._scoreTimer<0)
{
if(org.flixel.FlxG.score>0)
{
org.flixel.FlxG.play(this.SndCount);
if(org.flixel.FlxG.score>100)org.flixel.FlxG.score-=100;
else{org.flixel.FlxG.score=0;this._player.kill();}
this._scoreTimer=1;
if(org.flixel.FlxG.score<600)
org.flixel.FlxG.play(this.SndCount);
if(org.flixel.FlxG.score<500)
org.flixel.FlxG.play(this.SndCount);
if(org.flixel.FlxG.score<400)
org.flixel.FlxG.play(this.SndCount);
if(org.flixel.FlxG.score<300)
org.flixel.FlxG.play(this.SndCount);
if(org.flixel.FlxG.score<200)
org.flixel.FlxG.play(this.SndCount);
}
}
var spawnerCount=this._spawners.countLiving();
if(spawnerCount<=0)
{
this._fading=true;
org.flixel.FlxG.fade.start(0xffd8eba2,3,$$bound(this,"onVictory"));
}
else
{
var l=this._notches.length;
for(var i=0;i<l;i++)
{
if(i<spawnerCount)
this._notches[i].play("on");
else
this._notches[i].play("off");
}
}
}
if(os!=org.flixel.FlxG.score)
{
if(this._player.dead)org.flixel.FlxG.score=0;
this._score.text=org.flixel.FlxG.score.toString();
}
if(this.reload)
org.flixel.FlxG.state=new com.adamatomic.Mode.PlayState();
if(org.flixel.FlxG.keys.justPressed("B"))
org.flixel.FlxG.showBounds=!org.flixel.FlxG.showBounds;
},
"protected function overlapped",function(Object1,Object2)
{
if((is(Object1,com.adamatomic.Mode.BotBullet))||(is(Object1,com.adamatomic.Mode.Bullet)))
Object1.kill();
Object2.hurt(1);
},
"protected function onVictory",function()
{
org.flixel.FlxG.music.stop();
org.flixel.FlxG.state=new com.adamatomic.Mode.VictoryState();
},
"protected function buildRoom",function(RX,RY,Spawners)
{switch(arguments.length){case 0:case 1:case 2:Spawners=false;}
var rw=20;
var sx;
var sy;
if(Spawners)
{
sx=$$uint(2+org.flixel.FlxU.random()*(rw-7));
sy=$$uint(2+org.flixel.FlxU.random()*(rw-7));
}
var numBlocks=$$uint(3+org.flixel.FlxU.random()*4);
if(!Spawners)numBlocks++;
var maxW=10;
var minW=2;
var maxH=8;
var minH=1;
var bx;
var by;
var bw;
var bh;
var check;
for(var i=0;i<numBlocks;i++)
{
check=false;
do
{
bw=$$uint(minW+org.flixel.FlxU.random()*(maxW-minW));
bh=$$uint(minH+org.flixel.FlxU.random()*(maxH-minH));
bx=$$uint(-1+org.flixel.FlxU.random()*(rw+1-bw));
by=$$uint(-1+org.flixel.FlxU.random()*(rw+1-bh));
if(Spawners)
check=((sx>bx+bw)||(sx+3<bx)||(sy>by+bh)||(sy+3<by));
else
check=true;
}while(!check);
var b;
b=new org.flixel.FlxTileblock(RX+bx*8,RY+by*8,bw*8,bh*8);
b.loadTiles(this.ImgTech);
this._blocks.add(b);
if((bw>=4)&&(bh>=5))
{
b=new org.flixel.FlxTileblock(RX+bx*8+8,RY+by*8,bw*8-16,8);
b.loadTiles(this.ImgDirtTop);
this._decorations.add(b);
b=new org.flixel.FlxTileblock(RX+bx*8+8,RY+by*8+8,bw*8-16,bh*8-24);
b.loadTiles(this.ImgDirt);
this._decorations.add(b);
}
}
if(Spawners)
this._spawners.add(new com.adamatomic.Mode.Spawner(RX+sx*8,RY+sy*8,this._bigGibs,this._bots,this._botBullets.members,this._littleGibs,this._player));
},
];},[],["org.flixel.FlxState","resource:com/adamatomic/Mode/../../../data/tech_tiles.png","resource:com/adamatomic/Mode/../../../data/dirt_top.png","resource:com/adamatomic/Mode/../../../data/dirt.png","resource:com/adamatomic/Mode/../../../data/notch.png","resource:com/adamatomic/Mode/../../../data/mode.mp3","resource:com/adamatomic/Mode/../../../data/countdown.mp3","resource:com/adamatomic/Mode/../../../data/gibs.png","resource:com/adamatomic/Mode/../../../data/spawner_gibs.png","org.flixel.FlxG","org.flixel.FlxEmitter","org.flixel.FlxGroup","com.adamatomic.Mode.Player","org.flixel.FlxTileblock","com.adamatomic.Mode.BotBullet","com.adamatomic.Mode.Bullet","org.flixel.FlxPoint","org.flixel.FlxText","Array","org.flixel.FlxSprite","org.flixel.FlxU","com.adamatomic.Mode.VictoryState","uint","com.adamatomic.Mode.Spawner"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class com.adamatomic.Mode.PlayStateTiles
joo.classLoader.prepare("package com.adamatomic.Mode",
"public class PlayStateTiles extends org.flixel.FlxState",7,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:"com/adamatomic/Mode/../../../data/mode.mp3"}},"private var",{SndMode:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/map.txt",mimeType:"application/octet-stream"}},"private var",{TxtMap:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/map2.txt",mimeType:"application/octet-stream"}},"private var",{TxtMap2:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/tiles_all.png"}},"private var",{ImgTiles:null},
"private var",{_tilemap:null},
"private var",{_bullets:null},
"private var",{_player:null},
"override public function create",function()
{
this._tilemap$7=new org.flixel.FlxTilemap();
this._tilemap$7.collideIndex=3;
this._tilemap$7.loadMap(new this.TxtMap$7,this.ImgTiles$7,8);
this._bullets$7=new org.flixel.FlxGroup();
this._player$7=new com.adamatomic.Mode.Player(this._tilemap$7.width/2-4,this._tilemap$7.height/2-4,this._bullets$7.members,null);
for(var i=0;i<8;i++)
this._bullets$7.add(new com.adamatomic.Mode.Bullet());
this.add(this._bullets$7);
this.add(this._player$7);
org.flixel.FlxG.follow(this._player$7,2.5);
org.flixel.FlxG.followAdjust(0.5,0.0);
this._tilemap$7.follow();
this.add(this._tilemap$7);
org.flixel.FlxG.flash.start(0xff131c1b);
var s=org.flixel.FlxG.play(this.SndMode$7,1,true);
s.proximity(320,320,this._player$7,160);
},
"override public function update",function()
{
this.update$7();
this._tilemap$7.collide(this._player$7);
this._tilemap$7.collide(this._bullets$7);
if(org.flixel.FlxG.keys.justPressed("B"))
org.flixel.FlxG.showBounds=!org.flixel.FlxG.showBounds;
},
];},[],["org.flixel.FlxState","resource:com/adamatomic/Mode/../../../data/mode.mp3","resource:com/adamatomic/Mode/../../../data/map.txt","resource:com/adamatomic/Mode/../../../data/map2.txt","resource:com/adamatomic/Mode/../../../data/tiles_all.png","org.flixel.FlxTilemap","org.flixel.FlxGroup","com.adamatomic.Mode.Player","com.adamatomic.Mode.Bullet","org.flixel.FlxG"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class com.adamatomic.Mode.Spawner
joo.classLoader.prepare("package com.adamatomic.Mode",
"public class Spawner extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:"com/adamatomic/Mode/../../../data/spawner.png"}},"private var",{ImgSpawner:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/asplode.mp3"}},"private var",{SndExplode:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/menu_hit_2.mp3"}},"private var",{SndExplode2:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/hit.mp3"}},"private var",{SndHit:null},
"private var",{_timer:NaN},
"private var",{_bots:null},
"private var",{_botBullets:null},
"private var",{_botGibs:null},
"private var",{_gibs:null},
"private var",{_player:null},
"private var",{_open:false},
"public function Spawner",function(X,Y,Gibs,Bots,BotBullets,BotGibs,ThePlayer)
{
this.super$5(X,Y);
this.loadGraphic(this.ImgSpawner$5,true);
this._gibs$5=Gibs;
this._bots$5=Bots;
this._botBullets$5=BotBullets;
this._botGibs$5=BotGibs;
this._player$5=ThePlayer;
this._timer$5=Math.random()*20;
this._open$5=false;
this.health=8;
this.addAnimation("open",[1,2,3,4,5],40,false);
this.addAnimation("close",[4,3,2,1,0],40,false);
this.addAnimation("dead",[6]);
},
"override public function update",function()
{
this._timer$5+=org.flixel.FlxG.elapsed;
var limit=20;
if(this.onScreen())
limit=4;
if(this._timer$5>limit)
{
this._timer$5=0;
this.makeBot();
}
else if(this._timer$5>limit-0.35)
{
if(!this._open$5)
{
this._open$5=true;
this.play("open");
}
}
else if(this._timer$5>1)
{
if(this._open$5)
{
this.play("close");
this._open$5=false;
}
}
this.update$5();
},
"override public function hurt",function(Damage)
{
org.flixel.FlxG.play(this.SndHit$5);
this.flicker(0.2);
org.flixel.FlxG.score+=50;
this.hurt$5(Damage);
},
"override public function kill",function()
{
if(this.dead)
return;
org.flixel.FlxG.play(this.SndExplode$5);
org.flixel.FlxG.play(this.SndExplode2$5);
this.kill$5();
this.active=false;
this.exists=true;
this.solid=false;
this.flicker(-1);
this.play("dead");
org.flixel.FlxG.quake.start(0.005,0.35);
org.flixel.FlxG.flash.start(0xffd8eba2,0.35);
this.makeBot();
this._gibs$5.at(this);
this._gibs$5.start(true,3,0);
org.flixel.FlxG.score+=1000;
},
"protected function makeBot",function()
{
if(this._bots$5.resetFirstAvail(this.x+this.width/2-6,this.y+this.height/2-6))
return;
var bot=new com.adamatomic.Mode.Bot(this.x+this.width/2,this.y+this.height/2,this._botBullets$5,this._botGibs$5,this._player$5);
bot.x-=bot.width/2;
bot.y-=bot.height/2;
this._bots$5.add(bot);
},
];},[],["org.flixel.FlxSprite","resource:com/adamatomic/Mode/../../../data/spawner.png","resource:com/adamatomic/Mode/../../../data/asplode.mp3","resource:com/adamatomic/Mode/../../../data/menu_hit_2.mp3","resource:com/adamatomic/Mode/../../../data/hit.mp3","Math","org.flixel.FlxG","com.adamatomic.Mode.Bot"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class com.adamatomic.Mode.VictoryState
joo.classLoader.prepare("package com.adamatomic.Mode",
"public class VictoryState extends org.flixel.FlxState",7,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:"com/adamatomic/Mode/../../../data/spawner_gibs.png"}},"private var",{ImgGibs:null},
{Embed:{source:"com/adamatomic/Mode/../../../data/menu_hit_2.mp3"}},"private var",{SndMenu:null},
"private var",{_timer:NaN},
"private var",{_fading:false},
"override public function create",function()
{
this._timer$7=0;
this._fading$7=false;
org.flixel.FlxG.flash.start(0xffd8eba2);
var gibs=new org.flixel.FlxEmitter(0,-50);
gibs.setSize(org.flixel.FlxG.width,0);
gibs.setXSpeed();
gibs.setYSpeed(0,100);
gibs.setRotation(-360,360);
gibs.gravity=80;
gibs.createSprites(this.ImgGibs$7,800,32);
this.add(gibs);
gibs.start(false,0.005);
this.add((new org.flixel.FlxText(0,org.flixel.FlxG.height/2-35,org.flixel.FlxG.width,"VICTORY\n\nSCORE: "+org.flixel.FlxG.score)).setFormat(null,16,0xd8eba2,"center"));
},
"override public function update",function()
{
this.update$7();
if(!this._fading$7)
{
this._timer$7+=org.flixel.FlxG.elapsed;
if((this._timer$7>0.35)&&((this._timer$7>10)||org.flixel.FlxG.keys.justPressed("X")||org.flixel.FlxG.keys.justPressed("C")))
{
this._fading$7=true;
org.flixel.FlxG.play(this.SndMenu$7);
org.flixel.FlxG.fade.start(0xff131c1b,2,$$bound(this,"onPlay$7"));
}
}
},
"private function onPlay",function()
{
org.flixel.FlxG.state=new com.adamatomic.Mode.PlayState();
},
];},[],["org.flixel.FlxState","resource:com/adamatomic/Mode/../../../data/spawner_gibs.png","resource:com/adamatomic/Mode/../../../data/menu_hit_2.mp3","org.flixel.FlxG","org.flixel.FlxEmitter","org.flixel.FlxText","com.adamatomic.Mode.PlayState"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class Mode
joo.classLoader.prepare("package",
{SWF:{width:"640",height:"480",backgroundColor:"#000000"}},
{Frame:{factoryClass:"Preloader"}},
"public class Mode extends org.flixel.FlxGame",7,function($$private){;return[function(){joo.classLoader.init(com.adamatomic.Mode.MenuState,org.flixel.FlxState);},

"public function Mode",function()
{
this.super$7(320,240,com.adamatomic.Mode.MenuState);
org.flixel.FlxState.bgColor=0xff131c1b;
this.useDefaultHotKeys=true;
},
];},[],["org.flixel.FlxGame","com.adamatomic.Mode.MenuState","org.flixel.FlxState"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class Preloader
joo.classLoader.prepare("package","public class Preloader extends org.flixel.FlxPreloader",8,function($$private){;return["public function Preloader",function(){
this.className="Mode";
this.super$8();
},];},[],["org.flixel.FlxPreloader"], "0.8.0", "0.8.2-SNAPSHOT");
