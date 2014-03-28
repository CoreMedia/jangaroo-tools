// class Asteroid
joo.classLoader.prepare("package",
"public class Asteroid extends WrapSprite",6,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxG);},

"static public var",{group:null},
{Embed:{source:"small.png"}},"private var",{ImgSmall:null},
{Embed:{source:"medium.png"}},"private var",{ImgMedium:null},
{Embed:{source:"large.png"}},"private var",{ImgLarge:null},
"public function Asteroid",function()
{
this.super$6();
this.antialiasing=true;
},
"public function create",function(X,Y,VelocityX,VelocityY,Size)
{if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){X=0;}Y=0;}VelocityX=0;}VelocityY=0;}Size=null;}
this.exists=true;
this.visible=true;
this.active=true;
this.solid=true;
this.loadRotatedGraphic((Size==null)?this.ImgLarge$6:Size,100,-1,true,true);
this.alterBoundingBox();
this.angle=org.flixel.FlxU.random()*360;
if((X!=0)||(Y!=0))
{
this.x=X;
this.y=Y;
this.velocity.x=VelocityX;
this.velocity.y=VelocityY;
return this;
}
var initial_velocity=20;
if(org.flixel.FlxU.random()<0.5)
{
if(org.flixel.FlxU.random()<0.5)
{
this.x=-64+this.offset.x;
this.velocity.x=initial_velocity/2+org.flixel.FlxU.random()*initial_velocity;
}
else
{
this.x=org.flixel.FlxG.width+this.offset.x;
this.velocity.x=-initial_velocity/2-org.flixel.FlxU.random()*initial_velocity;
}
this.y=org.flixel.FlxU.random()*(org.flixel.FlxG.height-this.height);
this.velocity.y=org.flixel.FlxU.random()*initial_velocity*2-initial_velocity;
}
else
{
this.x=org.flixel.FlxU.random()*(org.flixel.FlxG.width-this.width);
this.velocity.x=org.flixel.FlxU.random()*initial_velocity*2-initial_velocity;
if(org.flixel.FlxU.random()<0.5)
{
this.y=-64+this.offset.y;
this.velocity.y=initial_velocity/2+org.flixel.FlxU.random()*initial_velocity;
}
else
{
this.y=org.flixel.FlxG.height+this.offset.y;
this.velocity.y=initial_velocity/2+org.flixel.FlxU.random()*initial_velocity;
}
}
return this;
},
"override public function kill",function()
{
this.kill$6();
if(this.frameWidth<=32)
return;
var initial_velocity=20;
var slot;
var size;
if(this.frameWidth>=64)
{
size=this.ImgMedium$6;
initial_velocity*=2;
}
else
{
size=this.ImgSmall$6;
initial_velocity*=3;
}
var numChunks=2+org.flixel.FlxU.random()*3;
for(var i=0;i<numChunks;i++)
{
var ax=this.x+this.width/2;
var ay=this.y+this.height/2;
var avx=org.flixel.FlxU.random()*initial_velocity*2-initial_velocity;
var avy=org.flixel.FlxU.random()*initial_velocity*2-initial_velocity;
var a=as(Asteroid.group.getFirstAvail(),Asteroid);
if(a==null)
a=as(Asteroid.group.add(new Asteroid()),Asteroid);
a.create(ax,ay,avx,avy,size);
}
},
"override public function hitBottom",function(Contact,Velocity)
{
this.velocity.y=-this.velocity.y;
},
"override public function hitTop",function(Contact,Velocity)
{
this.velocity.y=-this.velocity.y;
},
"override public function hitLeft",function(Contact,Velocity)
{
this.velocity.x=-this.velocity.x;
},
];},[],["WrapSprite","resource:small.png","resource:medium.png","resource:large.png","org.flixel.FlxU","org.flixel.FlxG"], "0.8.0", "0.8.1"
);
// class FlxTeroids
joo.classLoader.prepare("package",
{SWF:{width:"640",height:"480",backgroundColor:"#000000"}},
{Frame:{factoryClass:"Preloader"}},
"public class FlxTeroids extends org.flixel.FlxGame",7,function($$private){;return[function(){joo.classLoader.init(MenuState);},

"public function FlxTeroids",function()
{
this.super$7(320,240,MenuState);
},
];},[],["org.flixel.FlxGame","MenuState"], "0.8.0", "0.8.1"
);
// class MenuState
joo.classLoader.prepare("package",
"public class MenuState extends org.flixel.FlxState",7,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

"override public function create",function()
{
var t;
t=new org.flixel.FlxText(0,org.flixel.FlxG.height/2-10,org.flixel.FlxG.width,"FlxTeroids");
t.size=16;
t.alignment="center";
this.add(t);
t=new org.flixel.FlxText(0,org.flixel.FlxG.height-20,org.flixel.FlxG.width,"click to play");
t.alignment="center";
this.add(t);
org.flixel.FlxG.mouse.show();
},
"override public function update",function()
{
if(org.flixel.FlxG.mouse.justPressed())
org.flixel.FlxG.state=new PlayState();
},
];},[],["org.flixel.FlxState","org.flixel.FlxText","org.flixel.FlxG","PlayState"], "0.8.0", "0.8.1"
);
// class PlayState
joo.classLoader.prepare("package",
"public class PlayState extends org.flixel.FlxState",7,function($$private){;return[function(){joo.classLoader.init(Asteroid,org.flixel.FlxG);},

"protected var",{_ship:null},
"protected var",{_bullets:null},
"protected var",{_asteroids:null},
"protected var",{_timer:NaN},
"override public function create",function()
{
org.flixel.FlxG.mouse.hide();
var i;
this._asteroids=new org.flixel.FlxGroup();
this.add(this._asteroids);
Asteroid.group=this._asteroids;
this.addAsteroid$7();
var s;
this._bullets=new org.flixel.FlxGroup();
for(i=0;i<32;i++)
{
s=new WrapSprite(-100,-100);
s.createGraphic(8,2);
s.width=10;
s.height=10;
s.offset.x=-1;
s.offset.y=-4;
s.exists=false;
this._bullets.add(s);
}
this.add(this._bullets);
this._ship=new Ship(this._bullets.members);
this.add(this._ship);
},
"override public function update",function()
{
this._timer-=org.flixel.FlxG.elapsed;
if(this._timer<=0)
this.addAsteroid$7();
org.flixel.FlxU.overlap(this._bullets,this._asteroids);
org.flixel.FlxU.overlap(this._asteroids,this._ship);
org.flixel.FlxU.collide(this._asteroids,this._asteroids);
this.update$7();
if(!this._ship.exists)
org.flixel.FlxG.state=new PlayState();
},
"private function addAsteroid",function()
{
this._asteroids.add(new Asteroid().create());
this._timer=org.flixel.FlxU.random()*4;
},
];},[],["org.flixel.FlxState","org.flixel.FlxG","org.flixel.FlxGroup","Asteroid","WrapSprite","Ship","org.flixel.FlxU"], "0.8.0", "0.8.1"
);
// class Preloader
joo.classLoader.prepare("package",
"public class Preloader extends org.flixel.FlxPreloader",8,function($$private){;return[

"public function Preloader",function()
{
this.className="FlxTeroids";
this.super$8();
},
];},[],["org.flixel.FlxPreloader"], "0.8.0", "0.8.1"
);
// class Ship
joo.classLoader.prepare("package",
"public class Ship extends WrapSprite",6,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

"private var",{bullets:null},
"private var",{bulletIndex:0},
{Embed:{source:"ship.png"}},"private var",{ImgShip:null},
"public function Ship",function(Bullets)
{
this.super$6(org.flixel.FlxG.width/2-8,org.flixel.FlxG.height/2-8,this.ImgShip$6);
this.bullets$6=Bullets;
this.bulletIndex$6=0;
this.angle=-90;
this.maxThrust=90;
this.antialiasing=true;
},
"override public function update",function()
{
this.angularVelocity=0;
if(org.flixel.FlxG.keys.LEFT)
this.angularVelocity-=240;
if(org.flixel.FlxG.keys.RIGHT)
this.angularVelocity+=240;
this.thrust=0;
if(org.flixel.FlxG.keys.UP)
this.thrust-=this.maxThrust*3;
this.update$6();
if(org.flixel.FlxG.keys.justPressed("SPACE"))
{
var b=this.bullets$6[this.bulletIndex$6];
b.reset(this.x+(this.width-b.width)/2,this.y+(this.height-b.height)/2);
b.angle=this.angle;
b.velocity=org.flixel.FlxU.rotatePoint(150,0,0,0,b.angle);
b.velocity.x+=this.velocity.x;
b.velocity.y+=this.velocity.y;
this.bulletIndex$6++;
if(this.bulletIndex$6>=this.bullets$6.length)
this.bulletIndex$6=0;
}
},
];},[],["WrapSprite","resource:ship.png","org.flixel.FlxG","org.flixel.FlxU"], "0.8.0", "0.8.1"
);
// class WrapSprite
joo.classLoader.prepare("package",
"public class WrapSprite extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

"public function WrapSprite",function(X,Y,Graphic)
{if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){X=0;}Y=0;}Graphic=null;}
this.super$5(X,Y,Graphic);
this.alterBoundingBox();
},
"public function alterBoundingBox",function()
{
this.width=this.width*0.55;
this.height=this.height*0.55;
this.offset.x=(this.frameWidth-this.width)/2;
this.offset.y=(this.frameHeight-this.height)/2;
},
"override public function update",function()
{
this.update$5();
if(this.x<-this.frameWidth+this.offset.x)
this.x=org.flixel.FlxG.width+this.offset.x;
else if(this.x>org.flixel.FlxG.width+this.offset.x)
this.x=-this.frameWidth+this.offset.x;
if(this.y<-this.frameHeight+this.offset.y)
this.y=org.flixel.FlxG.height+this.offset.y;
else if(this.y>org.flixel.FlxG.height+this.offset.y)
this.y=-this.frameHeight+this.offset.y;
},
];},[],["org.flixel.FlxSprite","org.flixel.FlxG"], "0.8.0", "0.8.1"
);
