// class com.chipacabra.Jumper.Bullet
joo.classLoader.prepare("package com.chipacabra.Jumper",
"public class Bullet extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(Math,org.flixel.FlxG);},

{Embed:{source:'com/chipacabra/Jumper/../../../../art/bullet.png'}},"public var",{ImgBullet:null},
"public function Bullet",function()
{
this.super$5();
this.loadGraphic(this.ImgBullet,false);
this.exists=false;
},
"override public function update",function()
{
if(this.dead&&this.finished)
this.exists=false;
if(this.getScreenXY().x<-64||this.getScreenXY().x>org.flixel.FlxG.width+64){this.kill();}
else this.update$5();
},
"override public function hitSide",function(Contact,Velocity){this.kill();},
"override public function hitBottom",function(Contact,Velocity){this.kill();},
"override public function hitTop",function(Contact,Velocity){this.kill();},
"public function shoot",function(X,Y,VelocityX,VelocityY)
{
this.reset(X,Y);
this.solid=true;
this.velocity.x=VelocityX;
this.velocity.y=VelocityY;
},
"public function angleshoot",function(X,Y,Speed,Target)
{
this.reset(X,Y);
this.solid=true;
var dangle=org.flixel.FlxU.getAngle(Target.x-(this.x+(this.width/2)),Target.y-(this.y+(this.height/2)));
var rangle=dangle*(Math.PI/180);
this.velocity.x=Math.cos(rangle)*Speed;
this.velocity.y=Math.sin(rangle)*Speed;
},
];},[],["org.flixel.FlxSprite","resource:com/chipacabra/Jumper/../../../../art/bullet.png","org.flixel.FlxG","org.flixel.FlxU","Math"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class com.chipacabra.Jumper.Coin
joo.classLoader.prepare("package com.chipacabra.Jumper",
"public class Coin extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:'com/chipacabra/Jumper/../../../../art/coinspin.png'}},"public var",{imgCoin:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../sounds/coin.mp3'}},"public var",{sndCoin:null},
"public function Coin",function(X,Y,SimpleGraphic)
{switch(arguments.length){case 0:X=0;case 1:Y=0;case 2:SimpleGraphic=null;}
this.super$5(X,Y);
this.loadGraphic(this.imgCoin,true,false);
this.addAnimation("spinning",[0,1,2,3,4,5],10,true);
this.play("spinning");
},
"override public function kill",function()
{
this.kill$5();
org.flixel.FlxG.play(this.sndCoin,3,false,50);
org.flixel.FlxG.score++;
},
];},[],["org.flixel.FlxSprite","resource:com/chipacabra/Jumper/../../../../art/coinspin.png","resource:com/chipacabra/Jumper/../../../../sounds/coin.mp3","org.flixel.FlxG"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class com.chipacabra.Jumper.Enemy
joo.classLoader.prepare("package com.chipacabra.Jumper",
"public class Enemy extends com.chipacabra.Jumper.EnemyTemplate",6,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:'com/chipacabra/Jumper/../../../../art/spikemonsta.png'}},"public var",{Spikemonsta:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../sounds/monhurt2.mp3'}},"public var",{sndHurt:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../sounds/mondead2.mp3'}},"public var",{sndDead:null},
"protected static const",{RUN_SPEED:60},
"protected static const",{GRAVITY:0},
"protected static const",{JUMP_SPEED:60},
"protected static const",{HEALTH:1},
"protected static const",{SPAWNTIME:30},
"protected var",{_gibs:null},
"protected var",{_spawntimer:NaN},
"public function Enemy",function(X,Y,ThePlayer,Gibs)
{
this.super$6(X,Y,ThePlayer);
this._spawntimer=0;
this.loadGraphic(this.Spikemonsta,true,true);
this.addAnimation("walking",[0,1],10,true);
this.addAnimation("idle",[0]);
this.drag.x=com.chipacabra.Jumper.Enemy.RUN_SPEED*7;
this.drag.y=com.chipacabra.Jumper.Enemy.JUMP_SPEED*7;
this.acceleration.y=com.chipacabra.Jumper.Enemy.GRAVITY;
this.maxVelocity.x=com.chipacabra.Jumper.Enemy.RUN_SPEED;
this.maxVelocity.y=com.chipacabra.Jumper.Enemy.JUMP_SPEED;
this.health=com.chipacabra.Jumper.Enemy.HEALTH;
this._gibs=Gibs;
},
"public override function update",function()
{
if(this.dead)
{
this._spawntimer+=org.flixel.FlxG.elapsed;
if(this._spawntimer>=com.chipacabra.Jumper.Enemy.SPAWNTIME)
{
this.reset(this._startx,this._starty);
}
return;
}
this.acceleration.x=this.acceleration.y=0;
var xdistance=this._player.x-this.x;
var ydistance=this._player.y-this.y;
var distancesquared=xdistance*xdistance+ydistance*ydistance;
if(distancesquared<65000)
{
if(this._player.x<this.x)
{
this.facing=org.flixel.FlxSprite.RIGHT;
this.acceleration.x=-this.drag.x;
}
else if(this._player.x>this.x)
{
this.facing=org.flixel.FlxSprite.LEFT;
this.acceleration.x=this.drag.x;
}
if(this._player.y<this.y){this.acceleration.y=-this.drag.y;}
else if(this._player.y>this.y){this.acceleration.y=this.drag.y;}
}
if(!this.velocity.x&&!this.velocity.y){this.play("idle");}
else{this.play("walking");}
this.update$6();
},
"override public function reset",function(X,Y)
{
this.reset$6(X,Y);
this.health=com.chipacabra.Jumper.Enemy.HEALTH;
this._spawntimer=0;
},
"override public function hurt",function(Damage)
{
if(this.facing==org.flixel.FlxSprite.RIGHT)
this.velocity.x=this.drag.x*4;
else if(this.facing==org.flixel.FlxSprite.LEFT)
this.velocity.x=-this.drag.x*4;
this.flicker(.5);
org.flixel.FlxG.play(this.sndHurt,1,false,50);
this.hurt$6(Damage);
},
"override public function kill",function()
{
if(this.dead){return;}
if(this._gibs!=null)
{
this._gibs.at(this);
this._gibs.start(true,2.80);
org.flixel.FlxG.play(this.sndDead,1,false,50);
}
this.kill$6();
this.exists=true;
this.visible=false;
this.x=-10;
this.y=-10;
},
];},[],["com.chipacabra.Jumper.EnemyTemplate","resource:com/chipacabra/Jumper/../../../../art/spikemonsta.png","resource:com/chipacabra/Jumper/../../../../sounds/monhurt2.mp3","resource:com/chipacabra/Jumper/../../../../sounds/mondead2.mp3","org.flixel.FlxG"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class com.chipacabra.Jumper.EnemyTemplate
joo.classLoader.prepare("package com.chipacabra.Jumper",
"public class EnemyTemplate extends org.flixel.FlxSprite",5,function($$private){;return[

"protected var",{_player:null},
"protected var",{_startx:NaN},
"protected var",{_starty:NaN},
"public function EnemyTemplate",function(X,Y,ThePlayer)
{
this.super$5(X,Y);
this._startx=X;
this._starty=Y;
this._player=ThePlayer;
},
];},[],["org.flixel.FlxSprite"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class com.chipacabra.Jumper.Lurker
joo.classLoader.prepare("package com.chipacabra.Jumper",
"public class Lurker extends com.chipacabra.Jumper.EnemyTemplate",6,function($$private){var as=joo.as,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:'com/chipacabra/Jumper/../../../../art/lurkmonsta.png'}},"public var",{imgLurker:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../sounds/monhurt2.mp3'}},"public var",{sndHurt:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../sounds/mondead2.mp3'}},"public var",{sndDead:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../sounds/badshoot.mp3'}},"public var",{sndShoot:null},
"protected static const",{RUN_SPEED:30},
"protected static const",{GRAVITY:300},
"protected static const",{HEALTH:2},
"protected static const",{SPAWNTIME:45},
"protected static const",{JUMP_SPEED:60},
"protected static const",{BURNTIME:5},
"protected static const",{GUN_DELAY:1.5},
"protected static const",{BULLET_SPEED:100},
"protected var",{_spawntimer:NaN},
"protected var",{_burntimer:NaN},
"protected var",{_playdeathsound:false},
"protected var",{_bullets:null},
"protected var",{_cooldown:NaN},
"public function Lurker",function(X,Y,ThePlayer,Bullets)
{
this.super$6(X,Y,ThePlayer);
this._spawntimer=0;
this._burntimer=0;
this._playdeathsound=true;
this._bullets=Bullets;
this._cooldown=0;
this.loadGraphic(this.imgLurker,true,true,16,17);
this.addAnimation("walking",[0,1],18,true);
this.addAnimation("burning",[2,3],18,true);
this.addAnimation("wrecked",[4,5],18,true);
this.addAnimation("idle",[0]);
this.drag.x=com.chipacabra.Jumper.Lurker.RUN_SPEED*9;
this.drag.y=com.chipacabra.Jumper.Lurker.JUMP_SPEED*7;
this.acceleration.y=com.chipacabra.Jumper.Lurker.GRAVITY;
this.maxVelocity.x=com.chipacabra.Jumper.Lurker.RUN_SPEED;
this.maxVelocity.y=com.chipacabra.Jumper.Lurker.JUMP_SPEED;
this.health=com.chipacabra.Jumper.Lurker.HEALTH;
this.offset.x=3;
this.width=10;
},
"override public function update",function()
{
if(!this.velocity.x&&!this.velocity.y){this.play("idle");}
else if(this.health<com.chipacabra.Jumper.Lurker.HEALTH)
{
if(this.velocity.y==0){this.play("wrecked");}
else{this.play("burning");}
}
else{this.play("walking");}
if(this.health>0)
{
if(this.velocity.y==0){this.acceleration.y=-this.acceleration.y;}
if(this.x!=this._startx)
{this.acceleration.x=((this._startx-this.x));}
var xdistance=this._player.x-this.x;
var ydistance=this._player.y-this.y;
var distancesquared=xdistance*xdistance+ydistance*ydistance;
if(distancesquared<45000)
{
this.shoot$6(this._player);
}
}
if(this.health<=0)
{
this.maxVelocity.y=com.chipacabra.Jumper.Lurker.JUMP_SPEED*4;
this.acceleration.y=com.chipacabra.Jumper.Lurker.GRAVITY*3;
this.velocity.x=0;
this._burntimer+=org.flixel.FlxG.elapsed;
if(this._burntimer>=com.chipacabra.Jumper.Lurker.BURNTIME)
{
this.kill$6();
this.x=-10;
this.y=-10;
this.visible=false;
this.acceleration.y=0;
}
this._spawntimer+=org.flixel.FlxG.elapsed;
if(this._spawntimer>=com.chipacabra.Jumper.Lurker.SPAWNTIME)
{
this.reset(this._startx,this._starty);
}
}
this._cooldown+=org.flixel.FlxG.elapsed;
this.update$6();
},
"override public function reset",function(X,Y)
{
this.reset$6(X,Y);
this.health=com.chipacabra.Jumper.Lurker.HEALTH;
this._spawntimer=0;
this._burntimer=0;
this.acceleration.y=com.chipacabra.Jumper.Lurker.GRAVITY;
this.maxVelocity.y=com.chipacabra.Jumper.Lurker.JUMP_SPEED;
this._playdeathsound=true;
},
"override public function hurt",function(Damage)
{
if(this.x>this._player.x)
this.velocity.x=this.drag.x*4;
else
this.velocity.x=-this.drag.x*4;
this.flicker(.5);
org.flixel.FlxG.play(this.sndHurt,1,false,50);
this.health-=1;
},
"private function shoot",function(P)
{
var bulletX=this.x;
var bulletY=this.y+4;
if(this._cooldown>com.chipacabra.Jumper.Lurker.GUN_DELAY)
{
var bullet=as($$bound(this._bullets,"getFirstAvail"),com.chipacabra.Jumper.Bullet);
if(bullet==null)
{
bullet=new com.chipacabra.Jumper.Bullet();
this._bullets.add(bullet);
}
if(P.x<this.x)
{
bulletX-=bullet.width-8;
}
else
{
bulletX+=this.width-8;
}
bullet.angleshoot(bulletX,bulletY,com.chipacabra.Jumper.Lurker.BULLET_SPEED,P);
org.flixel.FlxG.play(this.sndShoot,1,false,50);
this._cooldown=0;
}
},
"override public function kill",function()
{
if(this.dead){return;}
this.exists=true;
this.solid=true;
this.visible=true;
this.acceleration.y=com.chipacabra.Jumper.Lurker.GRAVITY;
this.velocity.x=0;
},
"override public function hitBottom",function(Contact,Velocity)
{
if(this.health<=0&&this._playdeathsound)
{
org.flixel.FlxG.play(this.sndDead,1,false,50);
this._playdeathsound=false;
}
this.hitBottom$6(Contact,Velocity);
},
];},[],["com.chipacabra.Jumper.EnemyTemplate","resource:com/chipacabra/Jumper/../../../../art/lurkmonsta.png","resource:com/chipacabra/Jumper/../../../../sounds/monhurt2.mp3","resource:com/chipacabra/Jumper/../../../../sounds/mondead2.mp3","resource:com/chipacabra/Jumper/../../../../sounds/badshoot.mp3","org.flixel.FlxG","com.chipacabra.Jumper.Bullet"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class com.chipacabra.Jumper.MenuState
joo.classLoader.prepare("package com.chipacabra.Jumper",
"public class MenuState extends org.flixel.FlxState",7,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:'com/chipacabra/Jumper/../../../../art/pointer.png'}},"public var",{imgPoint:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../sounds/coin.mp3'}},"public var",{sndClick:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../sounds/menu.mp3'}},"public var",{sndPoint:null},
"static public const",{OPTIONS:3},
"static public const",{TEXT_SPEED:200},
"private var",{_text1:null},
"private var",{_text2:null},
"private var",{_text3:null},
"private var",{_text4:null},
"private var",{_text5:null},
"private var",{_pointer:null},
"private var",{_option:0},
"override public function create",function()
{
this._text1$7=new org.flixel.FlxText(-220,org.flixel.FlxG.height/4,320,"Project");
this._text1$7.size=40;
this._text1$7.color=0xFFFF00;
this._text1$7.antialiasing=true;
this.add(this._text1$7);
this._text2$7=new org.flixel.FlxText(org.flixel.FlxG.width-50,org.flixel.FlxG.height/2.5,320,"Jumper");
this._text2$7.size=this._text1$7.size;
this._text2$7.color=this._text1$7.color;
this._text2$7.antialiasing=this._text1$7.antialiasing;
this.add(this._text2$7);
this._text3$7=new org.flixel.FlxText(org.flixel.FlxG.width*2/3,org.flixel.FlxG.height*2/3,150,"Play");
this._text4$7=new org.flixel.FlxText(org.flixel.FlxG.width*2/3,org.flixel.FlxG.height*2/3+30,150,"Visit NIWID");
this._text5$7=new org.flixel.FlxText(org.flixel.FlxG.width*2/3,org.flixel.FlxG.height*2/3+60,150,"Visit flixel.org");
this._text3$7.color=this._text4$7.color=this._text5$7.color=0xAAFFFF00;
this._text3$7.size=this._text4$7.size=this._text5$7.size=16;
this._text3$7.antialiasing=this._text4$7.antialiasing=this._text5$7.antialiasing=true;
this.add(this._text3$7);
this.add(this._text4$7);
this.add(this._text5$7);
this._pointer$7=new org.flixel.FlxSprite();
this._pointer$7.loadGraphic(this.imgPoint);
this._pointer$7.x=this._text3$7.x-this._pointer$7.width-10;
this.add(this._pointer$7);
this._option$7=0;
this.create$7();
},
"override public function update",function()
{
if(this._text1$7.x<org.flixel.FlxG.width/5)this._text1$7.velocity.x=com.chipacabra.Jumper.MenuState.TEXT_SPEED;
else this._text1$7.velocity.x=0;
if(this._text2$7.x>org.flixel.FlxG.width/2.5)this._text2$7.velocity.x=-com.chipacabra.Jumper.MenuState.TEXT_SPEED;
else this._text2$7.velocity.x=0;
switch(this._option$7)
{
case 0:
this._pointer$7.y=this._text3$7.y;
break;
case 1:
this._pointer$7.y=this._text4$7.y;
break;
case 2:
this._pointer$7.y=this._text5$7.y;
break;
}
if(org.flixel.FlxG.keys.justPressed("UP"))
{
this._option$7=(this._option$7+com.chipacabra.Jumper.MenuState.OPTIONS-1)%com.chipacabra.Jumper.MenuState.OPTIONS;
org.flixel.FlxG.play(this.sndPoint,1,false,50);
}
if(org.flixel.FlxG.keys.justPressed("DOWN"))
{
this._option$7=(this._option$7+com.chipacabra.Jumper.MenuState.OPTIONS+1)%com.chipacabra.Jumper.MenuState.OPTIONS;
org.flixel.FlxG.play(this.sndPoint,1,false,50);
}
if(org.flixel.FlxG.keys.justPressed("SPACE")||org.flixel.FlxG.keys.justPressed("ENTER"))
switch(this._option$7)
{
case 0:
org.flixel.FlxG.fade.start(0xFF969867,1,$$bound(this,"startGame$7"));
org.flixel.FlxG.play(this.sndClick,1,false,50);
break;
case 1:
this.onURL$7();
break;
case 2:
this.onFlixel$7();
break;
}
this.update$7();
},
"private function onFlixel",function()
{
org.flixel.FlxU.openURL("http://flixel.org");
},
"private function onURL",function()
{
org.flixel.FlxU.openURL("http://chipacabra.blogspot.com");
},
"private function startGame",function()
{
org.flixel.FlxG.state=new com.chipacabra.Jumper.PlayState;
},
];},[],["org.flixel.FlxState","resource:com/chipacabra/Jumper/../../../../art/pointer.png","resource:com/chipacabra/Jumper/../../../../sounds/coin.mp3","resource:com/chipacabra/Jumper/../../../../sounds/menu.mp3","org.flixel.FlxText","org.flixel.FlxG","org.flixel.FlxSprite","org.flixel.FlxU","com.chipacabra.Jumper.PlayState"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class com.chipacabra.Jumper.Player
joo.classLoader.prepare("package com.chipacabra.Jumper",
"public class Player extends org.flixel.FlxSprite",5,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:'com/chipacabra/Jumper/../../../../art/lizardhead3.png'}},"public var",{lizardhead:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../sounds/death.mp3'}},"public var",{sndDeath:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../sounds/jump.mp3'}},"public var",{sndJump:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../sounds/shoot2.mp3'}},"public var",{sndShoot:null},
"protected static const",{RUN_SPEED:90},
"protected static const",{GRAVITY:620},
"protected static const",{JUMP_SPEED:250},
"protected static const",{BULLET_SPEED:200},
"protected static const",{GUN_DELAY:.4},
"protected var",{_gibs:null},
"protected var",{_bullets:null},
"protected var",{_blt:null},
"protected var",{_cooldown:NaN},
"protected var",{_parent:undefined},
"protected var",{_onladder:false},
"private var",{_jump:NaN},
"private var",{_canDJump:false},
"private var",{_xgridleft:0},
"private var",{_xgridright:0},
"private var",{_ygrid:0},
"public var",{climbing:false},
"public function Player",function(X,Y,Parent,Gibs,Bullets)
{
this.super$5(X,Y);
this._bullets=Bullets;
this.loadGraphic(this.lizardhead,true,true,16,20);
this.addAnimation("walking",[0,1,2,3],12,true);
this.addAnimation("idle",[3]);
this.addAnimation("jump",[2]);
this.drag.x=com.chipacabra.Jumper.Player.RUN_SPEED*8;
this.drag.y=com.chipacabra.Jumper.Player.RUN_SPEED*8;
this.acceleration.y=com.chipacabra.Jumper.Player.GRAVITY;
this.maxVelocity.x=com.chipacabra.Jumper.Player.RUN_SPEED;
this.maxVelocity.y=com.chipacabra.Jumper.Player.JUMP_SPEED;
this.height=16;
this.offset.y=4;
this.width=12;
this.offset.x=3;
this._cooldown=com.chipacabra.Jumper.Player.GUN_DELAY;
this._gibs=Gibs;
this._parent=Parent;
this._jump$5=0;
this._onladder=false;
this.climbing=false;
},
"public override function update",function()
{
this.acceleration.x=0;
if(this.climbing)this.acceleration.y=0;
else this.acceleration.y=com.chipacabra.Jumper.Player.GRAVITY;
if(org.flixel.FlxG.keys.LEFT)
{
this.facing=org.flixel.FlxSprite.LEFT;
this.acceleration.x=-this.drag.x;
}
else if(org.flixel.FlxG.keys.RIGHT)
{
this.facing=org.flixel.FlxSprite.RIGHT;
this.acceleration.x=this.drag.x;
}
if(org.flixel.FlxG.keys.UP)
{
if(this._onladder)
{
this.climbing=true;
this._canDJump$5=true;
}
if(this.climbing&&(this._parent.ladders.getTile(this._xgridleft$5,this._ygrid$5-1)))this.velocity.y=-com.chipacabra.Jumper.Player.RUN_SPEED;
}
if(org.flixel.FlxG.keys.DOWN)
{
if(this._onladder)
{
this.climbing=true;
this._canDJump$5=true;
}
if(this.climbing)this.velocity.y=com.chipacabra.Jumper.Player.RUN_SPEED;
}
if(org.flixel.FlxG.keys.justPressed("C"))
{
if(this.climbing)
{
this._jump$5=0;
this.climbing=false;
org.flixel.FlxG.play(this.sndJump,1,false,50);
}
if(!this.velocity.y)
org.flixel.FlxG.play(this.sndJump,1,false,50);
}
if(org.flixel.FlxG.keys.justPressed("C")&&(this.velocity.y>0)&&this._canDJump$5==true)
{
org.flixel.FlxG.play(this.sndJump,1,false,50);
this._jump$5=0;
this._canDJump$5=false;
}
if((this._jump$5>=0)&&(org.flixel.FlxG.keys.C))
{
this.climbing=false;
this._jump$5+=org.flixel.FlxG.elapsed;
if(this._jump$5>0.25)this._jump$5=-1;
}
else this._jump$5=-1;
if(this._jump$5>0)
{
if(this._jump$5<0.035)
this.velocity.y=-.6*this.maxVelocity.y;
else
this.velocity.y=-.8*this.maxVelocity.y;
}
if(org.flixel.FlxG.keys.X)
{
this.shoot$5();
}
if(this.velocity.x>0||this.velocity.x<0){this.play("walking");}
else if(!this.velocity.x){this.play("idle");}
if(this.velocity.y<0){this.play("jump");}
this._cooldown+=org.flixel.FlxG.elapsed;
if(this.x<=0)
this.x=0;
if((this.x+this.width)>this._parent.map.width)
this.x=this._parent.map.width-this.width;
this._xgridleft$5=$$int((this.x+3)/16);
this._xgridright$5=$$int((this.x+this.width-3)/16);
this._ygrid$5=$$int((this.y+this.height-1)/16);
if(this._parent.ladders.getTile(this._xgridleft$5,this._ygrid$5)&&this._parent.ladders.getTile(this._xgridright$5,this._ygrid$5)){this._onladder=true;}
else
{
this._onladder=false;
this.climbing=false;
}
if(this.climbing)
{
this.collideTop=false;
this.collideBottom=false;
}
else
{
this.collideTop=true;
this.collideBottom=true;
}
this.update$5();
},
"private function shoot",function()
{
var bulletX=this.x;
var bulletY=this.y+4;
var bXVeloc=0;
var bYVeloc=0;
if(this._cooldown>=com.chipacabra.Jumper.Player.GUN_DELAY)
{
this._blt=as(this._bullets.getFirstAvail(),com.chipacabra.Jumper.Bullet);
if(this._blt)
{
if(this.facing==org.flixel.FlxSprite.LEFT)
{
bulletX-=this._blt.width-8;
bXVeloc=-com.chipacabra.Jumper.Player.BULLET_SPEED;
}
else
{
bulletX+=this.width-8;
bXVeloc=com.chipacabra.Jumper.Player.BULLET_SPEED;
}
this._blt.shoot(bulletX,bulletY,bXVeloc,bYVeloc);
org.flixel.FlxG.play(this.sndShoot,1,false,50);
this._cooldown=0;
}
}
},
"override public function overlaps",function(Object)
{
if(!(Object.dead))
return this.overlaps$5(Object);
else
return false;
},
"override public function hitBottom",function(Contact,Velocity)
{
if(!org.flixel.FlxG.keys.C)
this._jump$5=0;
this._canDJump$5=true;
this.hitBottom$5(Contact,Velocity);
},
"override public function kill",function()
{
if(this.dead){return;}
this.kill$5();
org.flixel.FlxG.quake.start(0.005,.35);
org.flixel.FlxG.flash.start(0xffDB3624,.35);
if(this._gibs!=null)
{
this._gibs.at(this);
this._gibs.start(true,2.80);
}
org.flixel.FlxG.play(this.sndDeath,1,false,50);
},
];},[],["org.flixel.FlxSprite","resource:com/chipacabra/Jumper/../../../../art/lizardhead3.png","resource:com/chipacabra/Jumper/../../../../sounds/death.mp3","resource:com/chipacabra/Jumper/../../../../sounds/jump.mp3","resource:com/chipacabra/Jumper/../../../../sounds/shoot2.mp3","org.flixel.FlxG","int","com.chipacabra.Jumper.Bullet"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class com.chipacabra.Jumper.PlayState
joo.classLoader.prepare("package com.chipacabra.Jumper",
"public class PlayState extends org.flixel.FlxState",7,function($$private){var is=joo.is,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(com.chipacabra.Jumper.Coin,org.flixel.FlxG,com.chipacabra.Jumper.Lurker,com.chipacabra.Jumper.Enemy);},

{Embed:{source:'com/chipacabra/Jumper/../../../../levels/mapCSV_Group1_Map1.csv',mimeType:'application/octet-stream'}},"public var",{levelMap:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../levels/mapCSV_Group1_Map1back.csv',mimeType:'application/octet-stream'}},"public var",{backgroundMap:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../levels/mapCSV_Group1_Ladders.csv',mimeType:'application/octet-stream'}},"public var",{laddersMap:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../data/monstacoords.csv',mimeType:'application/octet-stream'}},"public var",{monstaList:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../data/lurkcoords.csv',mimeType:'application/octet-stream'}},"public var",{lurkList:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../data/coins.csv',mimeType:'application/octet-stream'}},"public var",{coinList:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../art/area02_level_tiles2.png'}},"public var",{levelTiles:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../art/lizgibs.png'}},"public var",{imgLizGibs:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../art/spikegibs.png'}},"public var",{imgSpikeGibs:null},
{Embed:{source:'com/chipacabra/Jumper/../../../../music/ScrollingSpace[1].mp3'}},"public var",{bgMusic:null},
"public var",{map:function(){return(new org.flixel.FlxTilemap);}},
"public var",{background:function(){return(new org.flixel.FlxTilemap);}},
"public var",{ladders:function(){return(new org.flixel.FlxTilemap);}},
"public var",{player:null},
"protected var",{_gibs:null},
"protected var",{_mongibs:null},
"protected var",{_bullets:null},
"protected var",{_badbullets:null},
"protected var",{_restart:false},
"protected var",{_text1:null},
"protected var",{_enemies:null},
"protected var",{_coins:null},
"protected var",{_score:null},
"override public function create",function()
{
this._restart=false;
this._text1=new org.flixel.FlxText(30,30,400,"Press R to Restart");
this._text1.visible=false;
this._text1.size=40;
this._text1.color=0xFFFF0000;
this._text1.antialiasing=true;
this._text1.scrollFactor.x=this._text1.scrollFactor.y=0;
this._gibs=new org.flixel.FlxEmitter();
this._gibs.delay=3;
this._gibs.setXSpeed(-150,150);
this._gibs.setYSpeed(-200,0);
this._gibs.setRotation(-720,720);
this._gibs.createSprites(this.imgLizGibs,25,15,true,.5,0.65);
this._mongibs=new org.flixel.FlxEmitter();
this._mongibs.delay=3;
this._mongibs.setXSpeed(-150,150);
this._mongibs.setYSpeed(-200,0);
this._mongibs.setRotation(-720,720);
this._mongibs.createSprites(this.imgSpikeGibs,25,15,true,.5,.65);
this._bullets=new org.flixel.FlxGroup;
this._badbullets=new org.flixel.FlxGroup;
this.add(this.background.loadMap(new this.backgroundMap,this.levelTiles,16,16));
this.background.scrollFactor.x=this.background.scrollFactor.y=.5;
this.add(this.map.loadMap(new this.levelMap,this.levelTiles,16,16));
this.add(this.ladders.loadMap(new this.laddersMap,this.levelTiles,16,16));
org.flixel.FlxU.setWorldBounds(0,0,this.map.width,this.map.height);
this.add(this.player=new com.chipacabra.Jumper.Player(112,92,this,this._gibs,this._bullets));
org.flixel.FlxG.follow(this.player,1);
org.flixel.FlxG.followAdjust(0,0);
org.flixel.FlxG.followBounds(0,0,1600,800);
this._enemies=new org.flixel.FlxGroup;
this.placeMonsters$7(new this.monstaList,com.chipacabra.Jumper.Enemy);
this.placeMonsters$7(new this.lurkList,com.chipacabra.Jumper.Lurker);
this._coins=new org.flixel.FlxGroup;
this.placeCoins$7(new this.coinList,com.chipacabra.Jumper.Coin);
this.add(this._coins);
this.add(this._enemies);
org.flixel.FlxG.score=0;
this.create$7();
for(var i=0;i<4;i++)
this._bullets.add(new com.chipacabra.Jumper.Bullet());
this.add(this._badbullets);
this.add(this._bullets);
this.add(this._gibs);
this.add(this._mongibs);
var ssf=new org.flixel.FlxPoint(0,0);
this._score=new org.flixel.FlxText(0,0,org.flixel.FlxG.width);
this._score.color=0xFFFF00;
this._score.size=16;
this._score.alignment="center";
this._score.scrollFactor=ssf;
this._score.shadow=0x131c1b;
this.add(this._score);
this.add(this._text1);
org.flixel.FlxG.playMusic(this.bgMusic,.5);
},
"override public function update",function()
{
this.update$7();
this.player.collide(this.map);
this._enemies.collide(this.map);
this._gibs.collide(this.map);
this._bullets.collide(this.map);
this._badbullets.collide(this.map);
this._score.text='$'+org.flixel.FlxG.score.toString();
if(org.flixel.FlxG.keys.justPressed("B"))
{
org.flixel.FlxG.showBounds=!org.flixel.FlxG.showBounds;
}
if(this.player.dead)
{
this._text1.visible=true;
if(org.flixel.FlxG.keys.justPressed("R"))this._restart=true;
}
org.flixel.FlxU.overlap(this.player,this._enemies,$$bound(this,"hitPlayer$7"));
org.flixel.FlxU.overlap(this._bullets,this._enemies,$$bound(this,"hitmonster$7"));
org.flixel.FlxU.overlap(this.player,this._coins,$$bound(this,"collectCoin$7"));
org.flixel.FlxU.overlap(this.player,this._badbullets,$$bound(this,"hitPlayer$7"));
if(this._restart)org.flixel.FlxG.state=new com.chipacabra.Jumper.PlayState;
},
"private function collectCoin",function(P,C)
{
C.kill();
},
"private function hitPlayer",function(P,Monster)
{
if(is(Monster,com.chipacabra.Jumper.Bullet))
Monster.kill();
if(Monster.health>0)
P.hurt(1);
},
"private function hitmonster",function(Blt,Monster)
{
if(Monster.dead){return;}
if(Monster.health>0)
{
Blt.kill();
Monster.hurt(1);
}
},
"private function placeMonsters",function(MonsterData,Monster)
{
var coords;
var entities=MonsterData.split("\n");
for(var j=0;j<entities.length;j++)
{
coords=entities[j].split(",");
if(Monster==com.chipacabra.Jumper.Enemy)
{this._enemies.add(new Monster($$uint(coords[0]),$$uint(coords[1]),this.player,this._mongibs));}
else if(Monster==com.chipacabra.Jumper.Lurker)
{this._enemies.add(new Monster($$uint(coords[0]),$$uint(coords[1]),this.player,this._badbullets));}
else if(Monster!=null)
{this._enemies.add(new Monster($$uint(coords[0]),$$uint(coords[1]),this.player));}
}
},
"private function placeCoins",function(CoinData,Sparkle)
{
var coords;
var entities=CoinData.split("\n");
for(var j=0;j<entities.length;j++)
{
coords=entities[j].split(",");
if(Sparkle==com.chipacabra.Jumper.Coin)
{this._coins.add(new com.chipacabra.Jumper.Coin($$uint(coords[0]),$$uint(coords[1])));}
}
},
"public function PlayState",function PlayState$(){this.super$7();this.map=this.map();this.background=this.background();this.ladders=this.ladders();}];},[],["org.flixel.FlxState","resource:com/chipacabra/Jumper/../../../../levels/mapCSV_Group1_Map1.csv","resource:com/chipacabra/Jumper/../../../../levels/mapCSV_Group1_Map1back.csv","resource:com/chipacabra/Jumper/../../../../levels/mapCSV_Group1_Ladders.csv","resource:com/chipacabra/Jumper/../../../../data/monstacoords.csv","resource:com/chipacabra/Jumper/../../../../data/lurkcoords.csv","resource:com/chipacabra/Jumper/../../../../data/coins.csv","resource:com/chipacabra/Jumper/../../../../art/area02_level_tiles2.png","resource:com/chipacabra/Jumper/../../../../art/lizgibs.png","resource:com/chipacabra/Jumper/../../../../art/spikegibs.png","resource:com/chipacabra/Jumper/../../../../music/ScrollingSpace[1].mp3","org.flixel.FlxTilemap","org.flixel.FlxText","org.flixel.FlxEmitter","org.flixel.FlxGroup","org.flixel.FlxU","com.chipacabra.Jumper.Player","org.flixel.FlxG","com.chipacabra.Jumper.Enemy","com.chipacabra.Jumper.Lurker","com.chipacabra.Jumper.Coin","com.chipacabra.Jumper.Bullet","org.flixel.FlxPoint","uint"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class Jumper
joo.classLoader.prepare("package",
{SWF:{width:"640",height:"480",backgroundColor:"#000000"}},
{Frame:{factoryClass:"Preloader"}},
"public class Jumper extends org.flixel.FlxGame",7,function($$private){;return[function(){joo.classLoader.init(com.chipacabra.Jumper.MenuState,org.flixel.FlxState);},

"public function Jumper",function()
{
this.super$7(640,480,com.chipacabra.Jumper.MenuState,1);
org.flixel.FlxState.bgColor=0xFF101414;
},
];},[],["org.flixel.FlxGame","com.chipacabra.Jumper.MenuState","org.flixel.FlxState"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class Preloader
joo.classLoader.prepare("package",
"public class Preloader extends org.flixel.FlxPreloader",8,function($$private){;return[

"public function Preloader",function()
{
this.className="Jumper";
this.super$8();
},
];},[],["org.flixel.FlxPreloader"], "0.8.0", "0.8.2-SNAPSHOT"
);
