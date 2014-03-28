// class FPS
joo.classLoader.prepare("package",
"public class FPS extends flash.text.TextField",5,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.events.Event);},

"private var",{fs:0},
"private var",{ms:0},
"public function FPS",function()
{this.super$5();
var format=new flash.text.TextFormat();
format.color=0x666666;
format.size=10;
format.bold=true;
format.font='Verdana';
this.textColor=0xcecece;
this.autoSize="left";
this.defaultTextFormat=format;
this.ms$5=flash.utils.getTimer();
this.fs$5=0;
this.addEventListener(flash.events.Event.ENTER_FRAME,$$bound(this,"onEnterFrame$5"));
},
"private function onEnterFrame",function(event)
{
if(flash.utils.getTimer()-1000>this.ms$5)
{
this.ms$5=flash.utils.getTimer();
this.text=this.fs$5.toString();
this.fs$5=0;
}
else
{
++this.fs$5;
}
},
];},[],["flash.text.TextField","flash.text.TextFormat","flash.events.Event"], "0.8.0", "0.8.1"
);
// class Main
joo.classLoader.prepare("package",
{SWF:{backgroundColor:'0x212121',frameRate:'30',width:'384',height:'384'}},
"public class Main extends flash.display.Sprite",6,function($$private){;return[

"private var",{app:null},
"public function Main",function()
{this.super$6();
this.stage.frameRate=44.444;
this.app$6=new ParticleApplication();
this.addChild(this.app$6);
},
];},[],["flash.display.Sprite","ParticleApplication"], "0.8.0", "0.8.1"
);
// class Particle
joo.classLoader.prepare("package",
"public class Particle",1,function($$private){;return[

"public var",{sx:NaN},
"public var",{sy:NaN},
"public var",{vx:NaN},
"public var",{vy:NaN},
"public function Particle",function(sx,sy)
{if(arguments.length<2){if(arguments.length<1){sx=0;}sy=0;}
this.sx=sx;
this.sy=sy;
},
];},[],[], "0.8.0", "0.8.1"
);
// class ParticleApplication
joo.classLoader.prepare("package",
"public class ParticleApplication extends flash.display.Sprite",6,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.display.PixelSnapping,Math,flash.display.GradientType,flash.events.Event);},

"static public const",{WIDTH:384},
"static public const",{HEIGHT:384},
"static private const",{PARTICLE_NUM:100},
"private var",{bitmap:null},
"private var",{particles:null},
"private var",{forceXPhase:NaN},
"private var",{forceYPhase:NaN},
"public function ParticleApplication",function()
{this.super$6();
this.init$6();
},
"private function init",function()
{
var m=new flash.geom.Matrix();
m.createGradientBox(ParticleApplication.WIDTH,ParticleApplication.HEIGHT,Math.PI/2);
this.graphics.beginGradientFill(flash.display.GradientType.LINEAR,[0x212121,0x404040,0x0],[1,1,1],[0,0x84,0xff],m);
this.graphics.drawRect(0,0,ParticleApplication.WIDTH,ParticleApplication.HEIGHT);
this.graphics.endFill();
this.forceXPhase$6=Math.random()*Math.PI;
this.forceYPhase$6=Math.random()*Math.PI;
this.particles$6=new Array();
var particle;
var a;
var r;
for(var i=0;i<$$private.PARTICLE_NUM;i++)
{
a=Math.PI*2/$$private.PARTICLE_NUM*i;
r=(1+i/$$private.PARTICLE_NUM*4)*1;
particle=new Particle(Math.cos(a)*32,Math.sin(a)*32);
particle.vx=Math.sin(-a)*r;
particle.vy=-Math.cos(a)*r;
this.particles$6.push(particle);
}
this.bitmap$6=new flash.display.Bitmap(new flash.display.BitmapData(ParticleApplication.WIDTH,ParticleApplication.HEIGHT,true,0),flash.display.PixelSnapping.AUTO,false);
this.addChild(this.bitmap$6);
this.addEventListener(flash.events.Event.ENTER_FRAME,$$bound(this,"onEnterFrame$6"));
this.addChild(new FPS());
},
"private function onEnterFrame",function(event)
{
this.render$6();
},
"private function render",function()
{
var bitmapData=this.bitmap$6.bitmapData;
bitmapData.colorTransform(bitmapData.rect,new flash.geom.ColorTransform(1,1,1,1,0,0,0,-1));
var p0;
var p1;
var dx;
var dy;
var dd;
var shape=new flash.display.Shape();
shape.graphics.clear();
shape.graphics.lineStyle(0,0xffffff,1);
this.forceXPhase$6+=.0025261;
this.forceYPhase$6+=.000621;
var forceX=1000+Math.sin(this.forceXPhase$6)*500;
var forceY=1000+Math.sin(this.forceYPhase$6)*500;
for(var $1 in this.particles$6)
{p0=this.particles$6[$1];
shape.graphics.moveTo(p0.sx+(ParticleApplication.WIDTH>>1),p0.sy+(ParticleApplication.HEIGHT>>1));
p0.vx-=p0.sx/forceX;
p0.vy-=p0.sy/forceY;
p0.sx+=p0.vx;
p0.sy+=p0.vy;
shape.graphics.lineTo(p0.sx+(ParticleApplication.WIDTH>>1),p0.sy+(ParticleApplication.HEIGHT>>1));
}
bitmapData.draw(shape);
},
];},[],["flash.display.Sprite","flash.geom.Matrix","Math","flash.display.GradientType","Array","Particle","flash.display.Bitmap","flash.display.BitmapData","flash.display.PixelSnapping","flash.events.Event","FPS","flash.geom.ColorTransform","flash.display.Shape"], "0.8.0", "0.8.1"
);
