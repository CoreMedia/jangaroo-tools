// class Alien
joo.classLoader.prepare("package",
"public class Alien extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

"private var",{bullets:null},
"static private var",{bulletIndex:0},
"private var",{shotClock:NaN},
"private var",{originalX:0},
{Embed:{source:"alien.png"}},"private var",{ImgAlien:null},
"public function Alien",function(X,Y,Color,Bullets)
{
this.super$5(X,Y);
this.loadGraphic(this.ImgAlien$5,true);
this.originalX$5=X;
this.color=Color;
this.bullets$5=Bullets;
$$private.bulletIndex=0;
this.restart$5();
this.addAnimation("Default",[0,1,0,2],6+org.flixel.FlxU.random()*4);
this.play("Default");
this.velocity.x=10;
},
"override public function update",function()
{
if(this.x<this.originalX$5-8)
{
this.x=this.originalX$5-8;
this.velocity.x=10;
this.velocity.y++;
}
if(this.x>this.originalX$5+8)
{
this.x=this.originalX$5+8;
this.velocity.x=-10;
}
if(this.y>org.flixel.FlxG.height*0.35)
this.shotClock$5-=org.flixel.FlxG.elapsed;
if(this.shotClock$5<=0)
{
this.restart$5();
var b=this.bullets$5[$$private.bulletIndex];
b.reset(this.x+this.width/2-b.width,this.y);
b.velocity.y=65;
$$private.bulletIndex++;
if($$private.bulletIndex>=this.bullets$5.length)
$$private.bulletIndex=0;
}
this.update$5();
},
"private function restart",function()
{
this.shotClock$5=1+org.flixel.FlxU.random()*10;
},
];},[],["org.flixel.FlxSprite","resource:alien.png","org.flixel.FlxU","org.flixel.FlxG"], "0.8.0", "0.8.1"
);
// class FlxInvaders
joo.classLoader.prepare("package",
{SWF:{width:"640",height:"480",backgroundColor:"#000000"}},
{Frame:{factoryClass:"Preloader"}},
"public class FlxInvaders extends org.flixel.FlxGame",7,function($$private){;return[function(){joo.classLoader.init(PlayState);},

"public function FlxInvaders",function()
{
this.super$7(320,240,PlayState);
},
];},[],["org.flixel.FlxGame","PlayState"], "0.8.0", "0.8.1"
);
// class PlayState
joo.classLoader.prepare("package",
"public class PlayState extends org.flixel.FlxState",7,function($$private){var is=joo.is,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);},

"protected var",{_player:null},
"protected var",{_playerBullets:null},
"protected var",{_aliens:null},
"protected var",{_alienBullets:null},
"protected var",{_shields:null},
"protected var",{_vsShields:null},
"override public function create",function()
{
var i;
if(org.flixel.FlxG.scores.length<=0)
org.flixel.FlxG.scores[0]="WELCOME TO FLX INVADERS";
var s;
this._playerBullets=new org.flixel.FlxGroup();
for(i=0;i<8;i++)
{
s=new org.flixel.FlxSprite(-100,-100);
s.createGraphic(2,8);
s.exists=false;
this._playerBullets.add(s);
}
this.add(this._playerBullets);
this._player=new Ship(this._playerBullets.members);
this.add(this._player);
this._alienBullets=new org.flixel.FlxGroup();
for(i=0;i<64;i++)
{
s=new org.flixel.FlxSprite(-100,-100);
s.createGraphic(2,8);
s.exists=false;
this._alienBullets.add(s);
}
this.add(this._alienBullets);
var a;
this._aliens=new org.flixel.FlxGroup();
var colors=new Array(0xff0000ff,0xff00ffff,0xff00ff00,0xffffff00,0xffff0000);
for(i=0;i<50;i++)
{
a=new Alien(8+(i%10)*32,
24+$$int(i/10)*32,
colors[$$int(i/10)],this._alienBullets.members);
this._aliens.add(a);
}
this.add(this._aliens);
this._shields=new org.flixel.FlxGroup();
for(i=0;i<256;i++)
{
s=new org.flixel.FlxSprite(32+80*$$int(i/64)+(i%8)*2,
org.flixel.FlxG.height-32+($$int((i%64)/8)*2));
s.moves=false;
s.createGraphic(2,2);
this._shields.add(s);
}
this.add(this._shields);
this._vsShields=new org.flixel.FlxGroup();
this._vsShields.add(this._alienBullets);
this._vsShields.add(this._playerBullets);
var t=new org.flixel.FlxText(4,4,org.flixel.FlxG.width-8,org.flixel.FlxG.scores[0]);
t.alignment="center";
this.add(t);
org.flixel.FlxG.mouse.show();
},
"override public function update",function()
{
if(org.flixel.FlxG.mouse.justPressed())
org.flixel.FlxG.mouse.hide();
org.flixel.FlxU.overlap(this._shields,this._vsShields,$$bound(this,"overlapped"));
org.flixel.FlxU.overlap(this._playerBullets,this._aliens);
org.flixel.FlxU.overlap(this._alienBullets,this._player);
this.update$7();
if(!this._player.exists)
{
org.flixel.FlxG.scores[0]="YOU LOST";
org.flixel.FlxG.state=new PlayState();
return;
}
else if(this._aliens.getFirstExtant()==null)
{
org.flixel.FlxG.scores[0]="YOU WON";
org.flixel.FlxG.state=new PlayState();
return;
}
},
"protected function overlapped",function(Object1,Object2)
{
Object1.kill();
if(!(is(Object2,Alien)))
Object2.kill();
},
];},[],["org.flixel.FlxState","org.flixel.FlxG","org.flixel.FlxGroup","org.flixel.FlxSprite","Ship","Array","Alien","int","org.flixel.FlxText","org.flixel.FlxU"], "0.8.0", "0.8.1"
);
// class Preloader
joo.classLoader.prepare("package",
"public class Preloader extends org.flixel.FlxPreloader",8,function($$private){;return[

"public function Preloader",function()
{
this.className="FlxInvaders";
this.super$8();
},
];},[],["org.flixel.FlxPreloader"], "0.8.0", "0.8.1"
);
// class Ship
joo.classLoader.prepare("package",
"public class Ship extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

"private var",{bullets:null},
"private var",{bulletIndex:0},
{Embed:{source:"ship.png"}},"private var",{ImgShip:null},
"public function Ship",function(Bullets)
{
this.super$5(org.flixel.FlxG.width/2-6,org.flixel.FlxG.height-12,this.ImgShip$5);
this.bullets$5=Bullets;
this.bulletIndex$5=0;
},
"override public function update",function()
{
this.velocity.x=0;
if(org.flixel.FlxG.keys.LEFT)
this.velocity.x-=150;
if(org.flixel.FlxG.keys.RIGHT)
this.velocity.x+=150;
this.update$5();
if(this.x>org.flixel.FlxG.width-this.width-4)
this.x=org.flixel.FlxG.width-this.width-4;
if(this.x<4)
this.x=4;
if(org.flixel.FlxG.keys.justPressed("SPACE"))
{
var b=this.bullets$5[this.bulletIndex$5];
b.reset(this.x+this.width/2-b.width,this.y);
b.velocity.y=-240;
this.bulletIndex$5++;
if(this.bulletIndex$5>=this.bullets$5.length)
this.bulletIndex$5=0;
}
},
];},[],["org.flixel.FlxSprite","resource:ship.png","org.flixel.FlxG"], "0.8.0", "0.8.1"
);
