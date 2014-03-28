// class org.flixel.data.FlxAnim
joo.classLoader.prepare("package org.flixel.data",
"public class FlxAnim",1,function($$private){;return[

"public var",{name:null},
"public var",{delay:NaN},
"public var",{frames:null},
"public var",{looped:false},
"public function FlxAnim",function(Name,Frames,FrameRate,Looped)
{if(arguments.length<4){if(arguments.length<3){FrameRate=0;}Looped=true;}
this.name=Name;
this.delay=0;
if(FrameRate>0)
this.delay=1.0/FrameRate;
this.frames=Frames;
this.looped=Looped;
},
];},[],[], "0.8.0", "0.8.1"
);
// class org.flixel.data.FlxConsole
joo.classLoader.prepare("package org.flixel.data",
"public class FlxConsole extends flash.display.Sprite",6,function($$private){var trace=joo.trace;return[function(){joo.classLoader.init(flash.text.AntiAliasType,org.flixel.FlxG,flash.text.GridFitType);},

"public var",{mtrUpdate:null},
"public var",{mtrRender:null},
"public var",{mtrTotal:null},
"protected const",{MAX_CONSOLE_LINES:256},
"protected var",{_console:null},
"protected var",{_text:null},
"protected var",{_fpsDisplay:null},
"protected var",{_extraDisplay:null},
"protected var",{_curFPS:0},
"protected var",{_lines:null},
"protected var",{_Y:NaN},
"protected var",{_YT:NaN},
"protected var",{_bx:0},
"protected var",{_by:0},
"protected var",{_byt:0},
"public function FlxConsole",function(X,Y,Zoom)
{
this.super$6();
this.visible=false;
this.x=X*Zoom;
this._by=Y*Zoom;
this._byt=this._by-org.flixel.FlxG.height*Zoom;
this._YT=this._Y=this.y=this._byt;
var tmp=new flash.display.Bitmap(new flash.display.BitmapData(org.flixel.FlxG.width*Zoom,org.flixel.FlxG.height*Zoom,true,0x7F000000));
this.addChild(tmp);
this.mtrUpdate=new org.flixel.FlxMonitor(16);
this.mtrRender=new org.flixel.FlxMonitor(16);
this.mtrTotal=new org.flixel.FlxMonitor(16);
this._text=new flash.text.TextField();
this._text.width=tmp.width;
this._text.height=tmp.height;
this._text.multiline=true;
this._text.wordWrap=true;
this._text.embedFonts=true;
this._text.selectable=false;
this._text.antiAliasType=flash.text.AntiAliasType.NORMAL;
this._text.gridFitType=flash.text.GridFitType.PIXEL;
this._text.defaultTextFormat=new flash.text.TextFormat("system",8,0xffffff);
this.addChild(this._text);
this._fpsDisplay=new flash.text.TextField();
this._fpsDisplay.width=100;
this._fpsDisplay.x=tmp.width-100;
this._fpsDisplay.height=20;
this._fpsDisplay.multiline=true;
this._fpsDisplay.wordWrap=true;
this._fpsDisplay.embedFonts=true;
this._fpsDisplay.selectable=false;
this._fpsDisplay.antiAliasType=flash.text.AntiAliasType.NORMAL;
this._fpsDisplay.gridFitType=flash.text.GridFitType.PIXEL;
this._fpsDisplay.defaultTextFormat=new flash.text.TextFormat("system",16,0xffffff,true,null,null,null,null,"right");
this.addChild(this._fpsDisplay);
this._extraDisplay=new flash.text.TextField();
this._extraDisplay.width=100;
this._extraDisplay.x=tmp.width-100;
this._extraDisplay.height=64;
this._extraDisplay.y=20;
this._extraDisplay.alpha=0.5;
this._extraDisplay.multiline=true;
this._extraDisplay.wordWrap=true;
this._extraDisplay.embedFonts=true;
this._extraDisplay.selectable=false;
this._extraDisplay.antiAliasType=flash.text.AntiAliasType.NORMAL;
this._extraDisplay.gridFitType=flash.text.GridFitType.PIXEL;
this._extraDisplay.defaultTextFormat=new flash.text.TextFormat("system",8,0xffffff,true,null,null,null,null,"right");
this.addChild(this._extraDisplay);
this._lines=new Array();
},
"public function log",function(Text)
{
if(Text==null)
Text="NULL";
trace(Text);
if(org.flixel.FlxG.mobile)
return;
this._lines.push(Text);
if(this._lines.length>this.MAX_CONSOLE_LINES)
{
this._lines.shift();
var newText="";
for(var i=0;i<this._lines.length;i++)
newText+=this._lines[i]+"\n";
this._text.text=newText;
}
else
this._text.appendText(Text+"\n");
this._text.scrollV=this._text.height;
},
"public function toggle",function()
{
if(org.flixel.FlxG.mobile)
{
this.log("FRAME TIMING DATA:\n=========================\n"+this.printTimingData()+"\n");
return;
}
if(this._YT==this._by)
this._YT=this._byt;
else
{
this._YT=this._by;
this.visible=true;
}
},
"public function update",function()
{
var total=this.mtrTotal.average();
this._fpsDisplay.text=$$uint(1000/total)+" fps";
this._extraDisplay.text=this.printTimingData();
if(this._Y<this._YT)
this._Y+=org.flixel.FlxG.height*10*org.flixel.FlxG.elapsed;
else if(this._Y>this._YT)
this._Y-=org.flixel.FlxG.height*10*org.flixel.FlxG.elapsed;
if(this._Y>this._by)
this._Y=this._by;
else if(this._Y<this._byt)
{
this._Y=this._byt;
this.visible=false;
}
this.y=Math.floor(this._Y);
},
"protected function printTimingData",function()
{
var up=this.mtrUpdate.average();
var rn=this.mtrRender.average();
var fx=up+rn;
var tt=this.mtrTotal.average();
return up+"ms update\n"+rn+"ms render\n"+fx+"ms flixel\n"+(tt-fx)+"ms flash\n"+tt+"ms total";
},
];},[],["flash.display.Sprite","org.flixel.FlxG","flash.display.Bitmap","flash.display.BitmapData","org.flixel.FlxMonitor","flash.text.TextField","flash.text.AntiAliasType","flash.text.GridFitType","flash.text.TextFormat","Array","uint","Math"], "0.8.0", "0.8.1"
);
// class org.flixel.data.FlxFade
joo.classLoader.prepare("package org.flixel.data",
"public class FlxFade extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

"protected var",{_delay:NaN},
"protected var",{_complete:null},
"public function FlxFade",function()
{
this.super$5();
this.createGraphic(org.flixel.FlxG.width,org.flixel.FlxG.height,0,true);
this.scrollFactor.x=0;
this.scrollFactor.y=0;
this.exists=false;
this.solid=false;
this.fixed=true;
},
"public function start",function(Color,Duration,FadeComplete,Force)
{if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){Color=0xff000000;}Duration=1;}FadeComplete=null;}Force=false;}
if(!Force&&this.exists)return;
this.fill(Color);
this._delay=Duration;
this._complete=FadeComplete;
this.alpha=0;
this.exists=true;
},
"public function stop",function()
{
this.exists=false;
},
"override public function update",function()
{
this.alpha+=org.flixel.FlxG.elapsed/this._delay;
if(this.alpha>=1)
{
this.alpha=1;
if(this._complete!=null)
this._complete();
}
},
];},[],["org.flixel.FlxSprite","org.flixel.FlxG"], "0.8.0", "0.8.1"
);
// class org.flixel.data.FlxFlash
joo.classLoader.prepare("package org.flixel.data",
"public class FlxFlash extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

"protected var",{_delay:NaN},
"protected var",{_complete:null},
"public function FlxFlash",function()
{
this.super$5();
this.createGraphic(org.flixel.FlxG.width,org.flixel.FlxG.height,0,true);
this.scrollFactor.x=0;
this.scrollFactor.y=0;
this.exists=false;
this.solid=false;
this.fixed=true;
},
"public function start",function(Color,Duration,FlashComplete,Force)
{if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){Color=0xffffffff;}Duration=1;}FlashComplete=null;}Force=false;}
if(!Force&&this.exists)return;
this.fill(Color);
this._delay=Duration;
this._complete=FlashComplete;
this.alpha=1;
this.exists=true;
},
"public function stop",function()
{
this.exists=false;
},
"override public function update",function()
{
this.alpha-=org.flixel.FlxG.elapsed/this._delay;
if(this.alpha<=0)
{
this.exists=false;
if(this._complete!=null)
this._complete();
}
},
];},[],["org.flixel.FlxSprite","org.flixel.FlxG"], "0.8.0", "0.8.1"
);
// class org.flixel.data.FlxGamepad
joo.classLoader.prepare("package org.flixel.data",
"public class FlxGamepad extends org.flixel.data.FlxInput",2,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

"public var",{UP:false},
"public var",{DOWN:false},
"public var",{LEFT:false},
"public var",{RIGHT:false},
"public var",{A:false},
"public var",{B:false},
"public var",{X:false},
"public var",{Y:false},
"public var",{START:false},
"public var",{SELECT:false},
"public var",{L1:false},
"public var",{L2:false},
"public var",{R1:false},
"public var",{R2:false},
"public function FlxGamepad",function()
{
this.super$2();
},
"public function bind",function(Up,Down,Left,Right,
AButton,BButton,XButton,YButton,
StartButton,SelectButton,
L1Button,L2Button,R1Button,R2Button)
{if(arguments.length<14){if(arguments.length<13){if(arguments.length<12){if(arguments.length<11){if(arguments.length<10){if(arguments.length<9){if(arguments.length<8){if(arguments.length<7){if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){Up=null;}Down=null;}Left=null;}Right=null;}AButton=null;}BButton=null;}XButton=null;}YButton=null;}StartButton=null;}SelectButton=null;}L1Button=null;}L2Button=null;}R1Button=null;}R2Button=null;}
if(Up!=null)this.addKey("UP",org.flixel.FlxG.keys._lookup[Up]);
if(Down!=null)this.addKey("DOWN",org.flixel.FlxG.keys._lookup[Down]);
if(Left!=null)this.addKey("LEFT",org.flixel.FlxG.keys._lookup[Left]);
if(Right!=null)this.addKey("RIGHT",org.flixel.FlxG.keys._lookup[Right]);
if(AButton!=null)this.addKey("A",org.flixel.FlxG.keys._lookup[AButton]);
if(BButton!=null)this.addKey("B",org.flixel.FlxG.keys._lookup[BButton]);
if(XButton!=null)this.addKey("X",org.flixel.FlxG.keys._lookup[XButton]);
if(YButton!=null)this.addKey("Y",org.flixel.FlxG.keys._lookup[YButton]);
if(StartButton!=null)this.addKey("START",org.flixel.FlxG.keys._lookup[StartButton]);
if(SelectButton!=null)this.addKey("SELECT",org.flixel.FlxG.keys._lookup[SelectButton]);
if(L1Button!=null)this.addKey("L1",org.flixel.FlxG.keys._lookup[L1Button]);
if(L2Button!=null)this.addKey("L2",org.flixel.FlxG.keys._lookup[L2Button]);
if(R1Button!=null)this.addKey("R1",org.flixel.FlxG.keys._lookup[R1Button]);
if(R2Button!=null)this.addKey("R2",org.flixel.FlxG.keys._lookup[R2Button]);
},
];},[],["org.flixel.data.FlxInput","org.flixel.FlxG"], "0.8.0", "0.8.1"
);
// class org.flixel.data.FlxInput
joo.classLoader.prepare("package org.flixel.data",
"public class FlxInput",1,function($$private){;return[

"internal var",{_lookup:null},
"internal var",{_map:null},
"internal const",{_t:256},
"public function FlxInput",function()
{
this._lookup=new Object();
this._map=new Array(this._t);
},
"public function update",function()
{
var i=0;
while(i<this._t)
{
var o=this._map[i++];
if(o==null)continue;
if((o.last==-1)&&(o.current==-1))o.current=0;
else if((o.last==2)&&(o.current==2))o.current=1;
o.last=o.current;
}
},
"public function reset",function()
{
var i=0;
while(i<this._t)
{
var o=this._map[i++];
if(o==null)continue;
this[o.name]=false;
o.current=0;
o.last=0;
}
},
"public function pressed",function(Key){return this[Key];},
"public function justPressed",function(Key){return this._map[this._lookup[Key]].current==2;},
"public function justReleased",function(Key){return this._map[this._lookup[Key]].current==-1;},
"public function handleKeyDown",function(event)
{
var o=this._map[event.keyCode];
if(o==null)return;
if(o.current>0)o.current=1;
else o.current=2;
this[o.name]=true;
},
"public function handleKeyUp",function(event)
{
var o=this._map[event.keyCode];
if(o==null)return;
if(o.current>0)o.current=-1;
else o.current=0;
this[o.name]=false;
},
"internal function addKey",function(KeyName,KeyCode)
{
this._lookup[KeyName]=KeyCode;
this._map[KeyCode]={name:KeyName,current:0,last:0};
},
];},[],["Object","Array"], "0.8.0", "0.8.1"
);
// class org.flixel.data.FlxKeyboard
joo.classLoader.prepare("package org.flixel.data",
"public class FlxKeyboard extends org.flixel.data.FlxInput",2,function($$private){;return[

"public var",{ESCAPE:false},
"public var",{F1:false},
"public var",{F2:false},
"public var",{F3:false},
"public var",{F4:false},
"public var",{F5:false},
"public var",{F6:false},
"public var",{F7:false},
"public var",{F8:false},
"public var",{F9:false},
"public var",{F10:false},
"public var",{F11:false},
"public var",{F12:false},
"public var",{ONE:false},
"public var",{TWO:false},
"public var",{THREE:false},
"public var",{FOUR:false},
"public var",{FIVE:false},
"public var",{SIX:false},
"public var",{SEVEN:false},
"public var",{EIGHT:false},
"public var",{NINE:false},
"public var",{ZERO:false},
"public var",{NUMPADONE:false},
"public var",{NUMPADTWO:false},
"public var",{NUMPADTHREE:false},
"public var",{NUMPADFOUR:false},
"public var",{NUMPADFIVE:false},
"public var",{NUMPADSIX:false},
"public var",{NUMPADSEVEN:false},
"public var",{NUMPADEIGHT:false},
"public var",{NUMPADNINE:false},
"public var",{NUMPADZERO:false},
"public var",{MINUS:false},
"public var",{NUMPADMINUS:false},
"public var",{PLUS:false},
"public var",{NUMPADPLUS:false},
"public var",{DELETE:false},
"public var",{BACKSPACE:false},
"public var",{Q:false},
"public var",{W:false},
"public var",{E:false},
"public var",{R:false},
"public var",{T:false},
"public var",{Y:false},
"public var",{U:false},
"public var",{I:false},
"public var",{O:false},
"public var",{P:false},
"public var",{LBRACKET:false},
"public var",{RBRACKET:false},
"public var",{BACKSLASH:false},
"public var",{CAPSLOCK:false},
"public var",{A:false},
"public var",{S:false},
"public var",{D:false},
"public var",{F:false},
"public var",{G:false},
"public var",{H:false},
"public var",{J:false},
"public var",{K:false},
"public var",{L:false},
"public var",{SEMICOLON:false},
"public var",{QUOTE:false},
"public var",{ENTER:false},
"public var",{SHIFT:false},
"public var",{Z:false},
"public var",{X:false},
"public var",{C:false},
"public var",{V:false},
"public var",{B:false},
"public var",{N:false},
"public var",{M:false},
"public var",{COMMA:false},
"public var",{PERIOD:false},
"public var",{NUMPADPERIOD:false},
"public var",{SLASH:false},
"public var",{NUMPADSLASH:false},
"public var",{CONTROL:false},
"public var",{ALT:false},
"public var",{SPACE:false},
"public var",{UP:false},
"public var",{DOWN:false},
"public var",{LEFT:false},
"public var",{RIGHT:false},
"public function FlxKeyboard",function()
{this.super$2();
var i;
i=65;
while(i<=90)
this.addKey(String.fromCharCode(i),i++);
i=48;
this.addKey("ZERO",i++);
this.addKey("ONE",i++);
this.addKey("TWO",i++);
this.addKey("THREE",i++);
this.addKey("FOUR",i++);
this.addKey("FIVE",i++);
this.addKey("SIX",i++);
this.addKey("SEVEN",i++);
this.addKey("EIGHT",i++);
this.addKey("NINE",i++);
i=96;
this.addKey("NUMPADZERO",i++);
this.addKey("NUMPADONE",i++);
this.addKey("NUMPADTWO",i++);
this.addKey("NUMPADTHREE",i++);
this.addKey("NUMPADFOUR",i++);
this.addKey("NUMPADFIVE",i++);
this.addKey("NUMPADSIX",i++);
this.addKey("NUMPADSEVEN",i++);
this.addKey("NUMPADEIGHT",i++);
this.addKey("NUMPADNINE",i++);
i=1;
while(i<=12)
this.addKey("F"+i,111+(i++));
this.addKey("ESCAPE",27);
this.addKey("MINUS",189);
this.addKey("NUMPADMINUS",109);
this.addKey("PLUS",187);
this.addKey("NUMPADPLUS",107);
this.addKey("DELETE",46);
this.addKey("BACKSPACE",8);
this.addKey("LBRACKET",219);
this.addKey("RBRACKET",221);
this.addKey("BACKSLASH",220);
this.addKey("CAPSLOCK",20);
this.addKey("SEMICOLON",186);
this.addKey("QUOTE",222);
this.addKey("ENTER",13);
this.addKey("SHIFT",16);
this.addKey("COMMA",188);
this.addKey("PERIOD",190);
this.addKey("NUMPADPERIOD",110);
this.addKey("SLASH",191);
this.addKey("NUMPADSLASH",191);
this.addKey("CONTROL",17);
this.addKey("ALT",18);
this.addKey("SPACE",32);
this.addKey("UP",38);
this.addKey("DOWN",40);
this.addKey("LEFT",37);
this.addKey("RIGHT",39);
},
];},[],["org.flixel.data.FlxInput","String"], "0.8.0", "0.8.1"
);
// class org.flixel.data.FlxKong
joo.classLoader.prepare("package org.flixel.data",
"public class FlxKong extends flash.display.Sprite",6,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.events.Event);},

"public var",{API:undefined},
"public function FlxKong",function()
{this.super$6();
this.API=null;
},
"public function init",function()
{
var paramObj=(this.root.loaderInfo).parameters;
var api_url=paramObj.api_path||"http://www.kongregate.com/flash/API_AS3_Local.swf";
var request=new flash.net.URLRequest(api_url);
var loader=new flash.display.Loader();
loader.contentLoaderInfo.addEventListener(flash.events.Event.COMPLETE,$$bound(this,"APILoaded"));
loader.load(request);
this.addChild(loader);
},
"protected function APILoaded",function(event)
{
this.API=event.target.content;
this.API.services.connect();
},
];},[],["flash.display.Sprite","flash.display.LoaderInfo","flash.net.URLRequest","flash.display.Loader","flash.events.Event"], "0.8.0", "0.8.1"
);
// class org.flixel.data.FlxList
joo.classLoader.prepare("package org.flixel.data",
"public class FlxList",1,function($$private){;return[

"public var",{object:null},
"public var",{next:null},
"public function FlxList",function()
{
this.object=null;
this.next=null;
},
];},[],[], "0.8.0", "0.8.1"
);
// class org.flixel.data.FlxMouse
joo.classLoader.prepare("package org.flixel.data",
"public class FlxMouse",1,function($$private){;return[

{Embed:{source:"org/flixel/data/cursor.png"}},"protected var",{ImgDefaultCursor:null},
"public var",{x:0},
"public var",{y:0},
"public var",{wheel:0},
"public var",{screenX:0},
"public var",{screenY:0},
"public var",{cursor:null},
"protected var",{_current:0},
"protected var",{_last:0},
"protected var",{_out:false},
"public function FlxMouse",function()
{
this.x=0;
this.y=0;
this.screenX=0;
this.screenY=0;
this._current=0;
this._last=0;
this.cursor=null;
this._out=false;
},
"public function show",function(Graphic,XOffset,YOffset)
{if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){Graphic=null;}XOffset=0;}YOffset=0;}
this._out=true;
if(Graphic!=null)
this.load(Graphic,XOffset,YOffset);
else if(this.cursor!=null)
this.cursor.visible=true;
else
this.load(null);
},
"public function hide",function()
{
if(this.cursor!=null)
{
this.cursor.visible=false;
this._out=false;
}
},
"public function load",function(Graphic,XOffset,YOffset)
{if(arguments.length<3){if(arguments.length<2){XOffset=0;}YOffset=0;}
if(Graphic==null)
Graphic=this.ImgDefaultCursor;
this.cursor=new org.flixel.FlxSprite(this.screenX,this.screenY,Graphic);
this.cursor.solid=false;
this.cursor.offset.x=XOffset;
this.cursor.offset.y=YOffset;
},
"public function unload",function()
{
if(this.cursor!=null)
{
if(this.cursor.visible)
this.load(null);
else
this.cursor=null;
}
},
"public function update",function(X,Y,XScroll,YScroll)
{
this.screenX=X;
this.screenY=Y;
this.x=this.screenX-org.flixel.FlxU.floor(XScroll);
this.y=this.screenY-org.flixel.FlxU.floor(YScroll);
if(this.cursor!=null)
{
this.cursor.x=this.x;
this.cursor.y=this.y;
}
if((this._last==-1)&&(this._current==-1))
this._current=0;
else if((this._last==2)&&(this._current==2))
this._current=1;
this._last=this._current;
},
"public function reset",function()
{
this._current=0;
this._last=0;
},
"public function pressed",function(){return this._current>0;},
"public function justPressed",function(){return this._current==2;},
"public function justReleased",function(){return this._current==-1;},
"public function handleMouseDown",function(event)
{
if(this._current>0)this._current=1;
else this._current=2;
},
"public function handleMouseUp",function(event)
{
if(this._current>0)this._current=-1;
else this._current=0;
},
"public function handleMouseOut",function(event)
{
if(this.cursor!=null)
{
this._out=this.cursor.visible;
this.cursor.visible=false;
}
},
"public function handleMouseOver",function(event)
{
if(this.cursor!=null)
this.cursor.visible=this._out;
},
"public function handleMouseWheel",function(event)
{
this.wheel=event.delta;
},
];},[],["resource:org/flixel/data/cursor.png","org.flixel.FlxSprite","org.flixel.FlxU"], "0.8.0", "0.8.1"
);
// class org.flixel.data.FlxPanel
joo.classLoader.prepare("package org.flixel.data",
"public class FlxPanel extends org.flixel.FlxObject",4,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:"org/flixel/data/donate.png"}},"private var",{ImgDonate:null},
{Embed:{source:"org/flixel/data/stumble.png"}},"private var",{ImgStumble:null},
{Embed:{source:"org/flixel/data/digg.png"}},"private var",{ImgDigg:null},
{Embed:{source:"org/flixel/data/reddit.png"}},"private var",{ImgReddit:null},
{Embed:{source:"org/flixel/data/delicious.png"}},"private var",{ImgDelicious:null},
{Embed:{source:"org/flixel/data/twitter.png"}},"private var",{ImgTwitter:null},
{Embed:{source:"org/flixel/data/close.png"}},"private var",{ImgClose:null},
"protected var",{_topBar:null},
"protected var",{_mainBar:null},
"protected var",{_bottomBar:null},
"protected var",{_donate:null},
"protected var",{_stumble:null},
"protected var",{_digg:null},
"protected var",{_reddit:null},
"protected var",{_delicious:null},
"protected var",{_twitter:null},
"protected var",{_close:null},
"protected var",{_caption:null},
"protected var",{_payPalID:null},
"protected var",{_payPalAmount:NaN},
"protected var",{_gameTitle:null},
"protected var",{_gameURL:null},
"protected var",{_initialized:false},
"protected var",{_closed:false},
"protected var",{_ty:NaN},
"protected var",{_s:NaN},
"public function FlxPanel",function()
{
this.super$4();
this.y=-21;
this._ty=this.y;
this._closed=false;
this._initialized=false;
this._topBar=new org.flixel.FlxSprite();
this._topBar.createGraphic(org.flixel.FlxG.width,1,0x7fffffff);
this._topBar.scrollFactor.x=0;
this._topBar.scrollFactor.y=0;
this._mainBar=new org.flixel.FlxSprite();
this._mainBar.createGraphic(org.flixel.FlxG.width,19,0x7f000000);
this._mainBar.scrollFactor.x=0;
this._mainBar.scrollFactor.y=0;
this._bottomBar=new org.flixel.FlxSprite();
this._bottomBar.createGraphic(org.flixel.FlxG.width,1,0x7fffffff);
this._bottomBar.scrollFactor.x=0;
this._bottomBar.scrollFactor.y=0;
this._donate=new org.flixel.FlxButton(3,0,$$bound(this,"onDonate"));
this._donate.loadGraphic(new org.flixel.FlxSprite(0,0,this.ImgDonate$4));
this._donate.scrollFactor.x=0;
this._donate.scrollFactor.y=0;
this._stumble=new org.flixel.FlxButton(org.flixel.FlxG.width/2-6-13-6-13-6,0,$$bound(this,"onStumble"));
this._stumble.loadGraphic(new org.flixel.FlxSprite(0,0,this.ImgStumble$4));
this._stumble.scrollFactor.x=0;
this._stumble.scrollFactor.y=0;
this._digg=new org.flixel.FlxButton(org.flixel.FlxG.width/2-6-13-6,0,$$bound(this,"onDigg"));
this._digg.loadGraphic(new org.flixel.FlxSprite(0,0,this.ImgDigg$4));
this._digg.scrollFactor.x=0;
this._digg.scrollFactor.y=0;
this._reddit=new org.flixel.FlxButton(org.flixel.FlxG.width/2-6,0,$$bound(this,"onReddit"));
this._reddit.loadGraphic(new org.flixel.FlxSprite(0,0,this.ImgReddit$4));
this._reddit.scrollFactor.x=0;
this._reddit.scrollFactor.y=0;
this._delicious=new org.flixel.FlxButton(org.flixel.FlxG.width/2+7+6,0,$$bound(this,"onDelicious"));
this._delicious.loadGraphic(new org.flixel.FlxSprite(0,0,this.ImgDelicious$4));
this._delicious.scrollFactor.x=0;
this._delicious.scrollFactor.y=0;
this._twitter=new org.flixel.FlxButton(org.flixel.FlxG.width/2+7+6+12+6,0,$$bound(this,"onTwitter"));
this._twitter.loadGraphic(new org.flixel.FlxSprite(0,0,this.ImgTwitter$4));
this._twitter.scrollFactor.x=0;
this._twitter.scrollFactor.y=0;
this._caption=new org.flixel.FlxText(org.flixel.FlxG.width/2,0,org.flixel.FlxG.width/2-19,"");
this._caption.alignment="right";
this._caption.scrollFactor.x=0;
this._caption.scrollFactor.y=0;
this._close=new org.flixel.FlxButton(org.flixel.FlxG.width-16,0,$$bound(this,"onClose"));
this._close.loadGraphic(new org.flixel.FlxSprite(0,0,this.ImgClose$4));
this._close.scrollFactor.x=0;
this._close.scrollFactor.y=0;
this.hide();
this.visible=false;
this._s=50;
},
"public function setup",function(PayPalID,PayPalAmount,GameTitle,GameURL,Caption)
{
this._payPalID=PayPalID;
if(this._payPalID.length<=0)this._donate.visible=false;
this._payPalAmount=PayPalAmount;
this._gameTitle=GameTitle;
this._gameURL=GameURL;
this._caption.text=Caption;
this._initialized=true;
},
"override public function update",function()
{
if(!this._initialized)return;
if(this._ty!=this.y)
{
if(this.y<this._ty)
{
this.y+=org.flixel.FlxG.elapsed*this._s;
if(this.y>this._ty)this.y=this._ty;
}
else
{
this.y-=org.flixel.FlxG.elapsed*this._s;
if(this.y<this._ty)this.y=this._ty;
}
this._topBar.y=this.y;
this._mainBar.y=this.y+1;
this._bottomBar.y=this.y+20;
this._donate.reset(this._donate.x,this.y+4);
this._stumble.reset(this._stumble.x,this.y+4);
this._digg.reset(this._digg.x,this.y+4);
this._reddit.reset(this._reddit.x,this.y+4);
this._delicious.reset(this._delicious.x,this.y+5);
this._twitter.reset(this._twitter.x,this.y+4);
this._caption.reset(this._caption.x,this.y+4);
this._close.reset(this._close.x,this.y+4);
}
if((this.y<=-21)||(this.y>=org.flixel.FlxG.height))
this.visible=false;
else
this.visible=true;
if(this.visible)
{
if(this._donate.active)this._donate.update();
if(this._stumble.active)this._stumble.update();
if(this._digg.active)this._digg.update();
if(this._reddit.active)this._reddit.update();
if(this._delicious.active)this._delicious.update();
if(this._twitter.active)this._twitter.update();
if(this._caption.active)this._caption.update();
if(this._close.active)this._close.update();
}
},
"override public function render",function()
{
if(!this._initialized)return;
if(this._topBar.visible)this._topBar.render();
if(this._mainBar.visible)this._mainBar.render();
if(this._bottomBar.visible)this._bottomBar.render();
if(this._donate.visible)this._donate.render();
if(this._stumble.visible)this._stumble.render();
if(this._digg.visible)this._digg.render();
if(this._reddit.visible)this._reddit.render();
if(this._delicious.visible)this._delicious.render();
if(this._twitter.visible)this._twitter.render();
if(this._caption.visible)this._caption.render();
if(this._close.visible)this._close.render();
},
"public function show",function(Top)
{if(arguments.length<1){Top=true;}
if(this._closed)return;
if(!this._initialized)
{
org.flixel.FlxG.log("SUPPORT PANEL ERROR: Uninitialized.\nYou forgot to call FlxGame.setupSupportPanel()\nfrom your game constructor.");
return;
}
if(Top)
{
this.y=-21;
this._ty=-1;
}
else
{
this.y=org.flixel.FlxG.height;
this._ty=org.flixel.FlxG.height-20;
}
this._donate.reset(this._donate.x,this.y+4);
this._stumble.reset(this._stumble.x,this.y+4);
this._digg.reset(this._digg.x,this.y+4);
this._reddit.reset(this._reddit.x,this.y+4);
this._delicious.reset(this._delicious.x,this.y+5);
this._twitter.reset(this._twitter.x,this.y+4);
this._caption.reset(this._caption.x,this.y+4);
this._close.reset(this._close.x,this.y+4);
if(!org.flixel.FlxG.mouse.cursor.visible)
flash.ui.Mouse.show();
this.visible=true;
},
"public function hide",function()
{
if(this.y<0)this._ty=-21;
else this._ty=org.flixel.FlxG.height;
flash.ui.Mouse.hide();
},
"public function onDonate",function()
{
org.flixel.FlxU.openURL("https://www.paypal.com/cgi-bin/webscr?cmd=_xclick&business="+encodeURIComponent(this._payPalID)+"&item_name="+encodeURIComponent(this._gameTitle+" Contribution ("+this._gameURL)+")&currency_code=USD&amount="+this._payPalAmount);
},
"public function onStumble",function()
{
org.flixel.FlxU.openURL("http://www.stumbleupon.com/submit?url="+encodeURIComponent(this._gameURL));
},
"public function onDigg",function()
{
org.flixel.FlxU.openURL("http://digg.com/submit?url="+encodeURIComponent(this._gameURL)+"&title="+encodeURIComponent(this._gameTitle));
},
"public function onReddit",function()
{
org.flixel.FlxU.openURL("http://www.reddit.com/submit?url="+encodeURIComponent(this._gameURL));
},
"public function onDelicious",function()
{
org.flixel.FlxU.openURL("http://delicious.com/save?v=5&amp;noui&amp;jump=close&amp;url="+encodeURIComponent(this._gameURL)+"&amp;title="+encodeURIComponent(this._gameTitle));
},
"public function onTwitter",function()
{
org.flixel.FlxU.openURL("http://twitter.com/home?status=Playing"+encodeURIComponent(" "+this._gameTitle+" - "+this._gameURL));
},
"public function onClose",function()
{
this._closed=true;
this.hide();
},
];},[],["org.flixel.FlxObject","resource:org/flixel/data/donate.png","resource:org/flixel/data/stumble.png","resource:org/flixel/data/digg.png","resource:org/flixel/data/reddit.png","resource:org/flixel/data/delicious.png","resource:org/flixel/data/twitter.png","resource:org/flixel/data/close.png","org.flixel.FlxSprite","org.flixel.FlxG","org.flixel.FlxButton","org.flixel.FlxText","flash.ui.Mouse","org.flixel.FlxU"], "0.8.0", "0.8.1"
);
// class org.flixel.data.FlxParticle
joo.classLoader.prepare("package org.flixel.data",
"public class FlxParticle extends org.flixel.FlxSprite",5,function($$private){;return[

"protected var",{_bounce:NaN},
"public function FlxParticle",function(Bounce)
{
this.super$5();
this._bounce=Bounce;
},
"override public function hitSide",function(Contact,Velocity)
{
this.velocity.x=-this.velocity.x*this._bounce;
if(this.angularVelocity!=0)
this.angularVelocity=-this.angularVelocity*this._bounce;
},
"override public function hitBottom",function(Contact,Velocity)
{
this.onFloor=true;
if(((this.velocity.y>0)?this.velocity.y:-this.velocity.y)>this._bounce*100)
{
this.velocity.y=-this.velocity.y*this._bounce;
if(this.angularVelocity!=0)
this.angularVelocity*=-this._bounce;
}
else
{
this.angularVelocity=0;
this.hitBottom$5(Contact,Velocity);
}
this.velocity.x*=this._bounce;
},
];},[],["org.flixel.FlxSprite"], "0.8.0", "0.8.1"
);
// class org.flixel.data.FlxPause
joo.classLoader.prepare("package org.flixel.data",
"public class FlxPause extends org.flixel.FlxGroup",5,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:"org/flixel/data/key_minus.png"}},"private var",{ImgKeyMinus:null},
{Embed:{source:"org/flixel/data/key_plus.png"}},"private var",{ImgKeyPlus:null},
{Embed:{source:"org/flixel/data/key_0.png"}},"private var",{ImgKey0:null},
{Embed:{source:"org/flixel/data/key_p.png"}},"private var",{ImgKeyP:null},
"public function FlxPause",function()
{
this.super$5();
this.scrollFactor.x=0;
this.scrollFactor.y=0;
var w=80;
var h=92;
this.x=(org.flixel.FlxG.width-w)/2;
this.y=(org.flixel.FlxG.height-h)/2;
var s;
s=new org.flixel.FlxSprite().createGraphic(w,h,0xaa000000,true);
s.solid=false;
this.add(s,true);
(as(this.add(new org.flixel.FlxText(0,0,w,"this game is"),true),org.flixel.FlxText)).alignment="center";
this.add((new org.flixel.FlxText(0,10,w,"PAUSED")).setFormat(null,16,0xffffff,"center"),true);
s=new org.flixel.FlxSprite(4,36,this.ImgKeyP$5);
s.solid=false;
this.add(s,true);
this.add(new org.flixel.FlxText(16,36,w-16,"Pause Game"),true);
s=new org.flixel.FlxSprite(4,50,this.ImgKey0$5);
s.solid=false;
this.add(s,true);
this.add(new org.flixel.FlxText(16,50,w-16,"Mute Sound"),true);
s=new org.flixel.FlxSprite(4,64,this.ImgKeyMinus$5);
s.solid=false;
this.add(s,true);
this.add(new org.flixel.FlxText(16,64,w-16,"Sound Down"),true);
s=new org.flixel.FlxSprite(4,78,this.ImgKeyPlus$5);
s.solid=false;
this.add(s,true);
this.add(new org.flixel.FlxText(16,78,w-16,"Sound Up"),true);
},
];},[],["org.flixel.FlxGroup","resource:org/flixel/data/key_minus.png","resource:org/flixel/data/key_plus.png","resource:org/flixel/data/key_0.png","resource:org/flixel/data/key_p.png","org.flixel.FlxG","org.flixel.FlxSprite","org.flixel.FlxText"], "0.8.0", "0.8.1"
);
// class org.flixel.data.FlxQuake
joo.classLoader.prepare("package org.flixel.data",
"public class FlxQuake",1,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

"protected var",{_zoom:0},
"protected var",{_intensity:NaN},
"protected var",{_timer:NaN},
"public var",{x:0},
"public var",{y:0},
"public function FlxQuake",function(Zoom)
{
this._zoom=Zoom;
this.start(0);
},
"public function start",function(Intensity,Duration)
{if(arguments.length<2){if(arguments.length<1){Intensity=0.05;}Duration=0.5;}
this.stop();
this._intensity=Intensity;
this._timer=Duration;
},
"public function stop",function()
{
this.x=0;
this.y=0;
this._intensity=0;
this._timer=0;
},
"public function update",function()
{
if(this._timer>0)
{
this._timer-=org.flixel.FlxG.elapsed;
if(this._timer<=0)
{
this._timer=0;
this.x=0;
this.y=0;
}
else
{
this.x=(Math.random()*this._intensity*org.flixel.FlxG.width*2-this._intensity*org.flixel.FlxG.width)*this._zoom;
this.y=(Math.random()*this._intensity*org.flixel.FlxG.height*2-this._intensity*org.flixel.FlxG.height)*this._zoom;
}
}
},
];},[],["org.flixel.FlxG","Math"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxButton
joo.classLoader.prepare("package org.flixel",
"public class FlxButton extends org.flixel.FlxGroup",5,function($$private){var as=joo.as,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.events.MouseEvent,org.flixel.FlxG);},

"public var",{pauseProof:false},
"protected var",{_onToggle:false},
"protected var",{_off:null},
"protected var",{_on:null},
"protected var",{_offT:null},
"protected var",{_onT:null},
"protected var",{_callback:null},
"protected var",{_pressed:false},
"protected var",{_initialized:false},
"protected var",{_sf:null},
"public function FlxButton",function(X,Y,Callback)
{
this.super$5();
this.x=X;
this.y=Y;
this.width=100;
this.height=20;
this._off=new org.flixel.FlxSprite().createGraphic(this.width,this.height,0xff7f7f7f);
this._off.solid=false;
this.add(this._off,true);
this._on=new org.flixel.FlxSprite().createGraphic(this.width,this.height,0xffffffff);
this._on.solid=false;
this.add(this._on,true);
this._offT=null;
this._onT=null;
this._callback=Callback;
this._onToggle=false;
this._pressed=false;
this._initialized=false;
this._sf=null;
this.pauseProof=false;
},
"public function loadGraphic",function(Image,ImageHighlight)
{if(arguments.length<2){ImageHighlight=null;}
this._off=as(this.replace(this._off,Image),org.flixel.FlxSprite);
if(ImageHighlight==null)
{
if(this._on!=this._off)
this.remove(this._on);
this._on=this._off;
}
else
this._on=as(this.replace(this._on,ImageHighlight),org.flixel.FlxSprite);
this._on.solid=this._off.solid=false;
this._off.scrollFactor=this.scrollFactor;
this._on.scrollFactor=this.scrollFactor;
this.width=this._off.width;
this.height=this._off.height;
this.refreshHulls();
return this;
},
"public function loadText",function(Text,TextHighlight)
{if(arguments.length<2){TextHighlight=null;}
if(Text!=null)
{
if(this._offT==null)
{
this._offT=Text;
this.add(this._offT);
}
else
this._offT=as(this.replace(this._offT,Text),org.flixel.FlxText);
}
if(TextHighlight==null)
this._onT=this._offT;
else
{
if(this._onT==null)
{
this._onT=TextHighlight;
this.add(this._onT);
}
else
this._onT=as(this.replace(this._onT,TextHighlight),org.flixel.FlxText);
}
this._offT.scrollFactor=this.scrollFactor;
this._onT.scrollFactor=this.scrollFactor;
return this;
},
"override public function update",function()
{
if(!this._initialized)
{
if(org.flixel.FlxG.stage!=null)
{
org.flixel.FlxG.stage.addEventListener(flash.events.MouseEvent.MOUSE_UP,$$bound(this,"onMouseUp"));
this._initialized=true;
}
}
this.update$5();
this.visibility(false);
if(this.overlapsPoint(org.flixel.FlxG.mouse.x,org.flixel.FlxG.mouse.y))
{
if(!org.flixel.FlxG.mouse.pressed())
this._pressed=false;
else if(!this._pressed)
this._pressed=true;
this.visibility(!this._pressed);
}
if(this._onToggle)this.visibility(this._off.visible);
},
"public function get on",function()
{
return this._onToggle;
},
"public function set on",function(On)
{
this._onToggle=On;
},
"override public function destroy",function()
{
if(org.flixel.FlxG.stage!=null)
org.flixel.FlxG.stage.removeEventListener(flash.events.MouseEvent.MOUSE_UP,$$bound(this,"onMouseUp"));
},
"protected function visibility",function(On)
{
if(On)
{
this._off.visible=false;
if(this._offT!=null)this._offT.visible=false;
this._on.visible=true;
if(this._onT!=null)this._onT.visible=true;
}
else
{
this._on.visible=false;
if(this._onT!=null)this._onT.visible=false;
this._off.visible=true;
if(this._offT!=null)this._offT.visible=true;
}
},
"protected function onMouseUp",function(event)
{
if(!this.exists||!this.visible||!this.active||!org.flixel.FlxG.mouse.justReleased()||(org.flixel.FlxG.pause&&!this.pauseProof)||(this._callback==null))return;
if(this.overlapsPoint(org.flixel.FlxG.mouse.x,org.flixel.FlxG.mouse.y))this._callback();
},
];},[],["org.flixel.FlxGroup","org.flixel.FlxSprite","org.flixel.FlxText","org.flixel.FlxG","flash.events.MouseEvent"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxEmitter
joo.classLoader.prepare("package org.flixel",
"public class FlxEmitter extends org.flixel.FlxGroup",5,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxG);},

"public var",{minParticleSpeed:null},
"public var",{maxParticleSpeed:null},
"public var",{particleDrag:null},
"public var",{minRotation:NaN},
"public var",{maxRotation:NaN},
"public var",{gravity:NaN},
"public var",{on:false},
"public var",{delay:NaN},
"public var",{quantity:0},
"public var",{justEmitted:false},
"protected var",{_explode:false},
"protected var",{_timer:NaN},
"protected var",{_particle:0},
"protected var",{_counter:0},
"public function FlxEmitter",function(X,Y)
{if(arguments.length<2){if(arguments.length<1){X=0;}Y=0;}
this.super$5();
this.x=X;
this.y=Y;
this.width=0;
this.height=0;
this.minParticleSpeed=new org.flixel.FlxPoint(-100,-100);
this.maxParticleSpeed=new org.flixel.FlxPoint(100,100);
this.minRotation=-360;
this.maxRotation=360;
this.gravity=400;
this.particleDrag=new org.flixel.FlxPoint();
this.delay=0;
this.quantity=0;
this._counter=0;
this._explode=true;
this.exists=false;
this.on=false;
this.justEmitted=false;
},
"public function createSprites",function(Graphics,Quantity,BakedRotations,Multiple,Collide,Bounce)
{if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){Quantity=50;}BakedRotations=16;}Multiple=true;}Collide=0;}Bounce=0;}
this.members=new Array();
var r;
var s;
var tf=1;
var sw;
var sh;
if(Multiple)
{
s=new org.flixel.FlxSprite();
s.loadGraphic(Graphics,true);
tf=s.frames;
}
var i=0;
while(i<Quantity)
{
if((Collide>0)&&(Bounce>0))
s=as(new org.flixel.data.FlxParticle(Bounce),org.flixel.FlxSprite);
else
s=new org.flixel.FlxSprite();
if(Multiple)
{
r=org.flixel.FlxU.random()*tf;
if(BakedRotations>0)
s.loadRotatedGraphic(Graphics,BakedRotations,r);
else
{
s.loadGraphic(Graphics,true);
s.frame=r;
}
}
else
{
if(BakedRotations>0)
s.loadRotatedGraphic(Graphics,BakedRotations);
else
s.loadGraphic(Graphics);
}
if(Collide>0)
{
sw=s.width;
sh=s.height;
s.width*=Collide;
s.height*=Collide;
s.offset.x=(sw-s.width)/2;
s.offset.y=(sh-s.height)/2;
s.solid=true;
}
else
s.solid=false;
s.exists=false;
s.scrollFactor=this.scrollFactor;
this.add(s);
i++;
}
return this;
},
"public function setSize",function(Width,Height)
{
this.width=Width;
this.height=Height;
},
"public function setXSpeed",function(Min,Max)
{if(arguments.length<2){if(arguments.length<1){Min=0;}Max=0;}
this.minParticleSpeed.x=Min;
this.maxParticleSpeed.x=Max;
},
"public function setYSpeed",function(Min,Max)
{if(arguments.length<2){if(arguments.length<1){Min=0;}Max=0;}
this.minParticleSpeed.y=Min;
this.maxParticleSpeed.y=Max;
},
"public function setRotation",function(Min,Max)
{if(arguments.length<2){if(arguments.length<1){Min=0;}Max=0;}
this.minRotation=Min;
this.maxRotation=Max;
},
"protected function updateEmitter",function()
{
if(this._explode)
{
this._timer+=org.flixel.FlxG.elapsed;
if((this.delay>0)&&(this._timer>this.delay))
{
this.kill();
return;
}
if(this.on)
{
this.on=false;
var i=this._particle;
var l=this.members.length;
if(this.quantity>0)
l=this.quantity;
l+=this._particle;
while(i<l)
{
this.emitParticle();
i++;
}
}
return;
}
if(!this.on)
return;
this._timer+=org.flixel.FlxG.elapsed;
while((this._timer>this.delay)&&((this.quantity<=0)||(this._counter<this.quantity)))
{
this._timer-=this.delay;
this.emitParticle();
}
},
"override protected function updateMembers",function()
{
var o;
var i=0;
var l=this.members.length;
while(i<l)
{
o=as(this.members[i++],org.flixel.FlxObject);
if((o!=null)&&o.exists&&o.active)
o.update();
}
},
"override public function update",function()
{
this.justEmitted=false;
this.update$5();
this.updateEmitter();
},
"public function start",function(Explode,Delay,Quantity)
{if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){Explode=true;}Delay=0;}Quantity=0;}
if(this.members.length<=0)
{
org.flixel.FlxG.log("WARNING: there are no sprites loaded in your emitter.\nAdd some to FlxEmitter.members or use FlxEmitter.createSprites().");
return;
}
this._explode=Explode;
if(!this._explode)
this._counter=0;
if(!this.exists)
this._particle=0;
this.exists=true;
this.visible=true;
this.active=true;
this.dead=false;
this.on=true;
this._timer=0;
if(this.quantity==0)
this.quantity=Quantity;
else if(Quantity!=0)
this.quantity=Quantity;
if(Delay!=0)
this.delay=Delay;
if(this.delay<0)
this.delay=-this.delay;
if(this.delay==0)
{
if(Explode)
this.delay=3;
else
this.delay=0.1;
}
},
"public function emitParticle",function()
{
this._counter++;
var s=as(this.members[this._particle],org.flixel.FlxSprite);
s.visible=true;
s.exists=true;
s.active=true;
s.x=this.x-(s.width>>1)+org.flixel.FlxU.random()*this.width;
s.y=this.y-(s.height>>1)+org.flixel.FlxU.random()*this.height;
s.velocity.x=this.minParticleSpeed.x;
if(this.minParticleSpeed.x!=this.maxParticleSpeed.x)s.velocity.x+=org.flixel.FlxU.random()*(this.maxParticleSpeed.x-this.minParticleSpeed.x);
s.velocity.y=this.minParticleSpeed.y;
if(this.minParticleSpeed.y!=this.maxParticleSpeed.y)s.velocity.y+=org.flixel.FlxU.random()*(this.maxParticleSpeed.y-this.minParticleSpeed.y);
s.acceleration.y=this.gravity;
s.angularVelocity=this.minRotation;
if(this.minRotation!=this.maxRotation)s.angularVelocity+=org.flixel.FlxU.random()*(this.maxRotation-this.minRotation);
if(s.angularVelocity!=0)s.angle=org.flixel.FlxU.random()*360-180;
s.drag.x=this.particleDrag.x;
s.drag.y=this.particleDrag.y;
this._particle++;
if(this._particle>=this.members.length)
this._particle=0;
s.onEmit();
this.justEmitted=true;
},
"public function stop",function(Delay)
{if(arguments.length<1){Delay=3;}
this._explode=true;
this.delay=Delay;
if(this.delay<0)
this.delay=-Delay;
this.on=false;
},
"public function at",function(Object)
{
this.x=Object.x+Object.origin.x;
this.y=Object.y+Object.origin.y;
},
"override public function kill",function()
{
this.kill$5();
this.on=false;
},
];},[],["org.flixel.FlxGroup","org.flixel.FlxPoint","Array","org.flixel.FlxSprite","org.flixel.data.FlxParticle","org.flixel.FlxU","org.flixel.FlxG","org.flixel.FlxObject"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxG
joo.classLoader.prepare("package org.flixel",
"public class FlxG",1,function($$private){var as=joo.as,is=joo.is;return[

"static public var",{LIBRARY_NAME:"flixel"},
"static public var",{LIBRARY_MAJOR_VERSION:2},
"static public var",{LIBRARY_MINOR_VERSION:43},
"static protected var",{_game:null},
"static protected var",{_pause:false},
"static public var",{debug:false},
"static public var",{showBounds:false},
"static public var",{elapsed:NaN},
"static public var",{maxElapsed:NaN},
"static public var",{timeScale:NaN},
"static public var",{width:0},
"static public var",{height:0},
"static public var",{mobile:false},
"static public var",{levels:null},
"static public var",{level:0},
"static public var",{scores:null},
"static public var",{score:0},
"static public var",{saves:null},
"static public var",{save:0},
"static public var",{mouse:null},
"static public var",{keys:null},
"static public var",{gamepads:null},
"static public var",{music:null},
"static public var",{sounds:null},
"static protected var",{_mute:false},
"static protected var",{_volume:NaN},
"static public var",{followTarget:null},
"static public var",{followLead:null},
"static public var",{followLerp:NaN},
"static public var",{followMin:null},
"static public var",{followMax:null},
"static protected var",{_scrollTarget:null},
"static public var",{scroll:null},
"static public var",{buffer:null},
"static protected var",{_cache:null},
"static public var",{kong:null},
"static public var",{panel:null},
"static public var",{quake:null},
"static public var",{flash:null},
"static public var",{fade:null},
"static public function log",function(Data)
{
if((org.flixel.FlxG._game!=null)&&(org.flixel.FlxG._game._console!=null))
org.flixel.FlxG._game._console.log((Data==null)?"ERROR: null object":Data.toString());
},
"static public function get pause",function()
{
return org.flixel.FlxG._pause;
},
"static public function set pause",function(Pause)
{
var op=org.flixel.FlxG._pause;
org.flixel.FlxG._pause=Pause;
if(org.flixel.FlxG._pause!=op)
{
if(org.flixel.FlxG._pause)
{
org.flixel.FlxG._game.pauseGame();
org.flixel.FlxG.pauseSounds();
}
else
{
org.flixel.FlxG._game.unpauseGame();
org.flixel.FlxG.playSounds();
}
}
},
"static public function get framerate",function()
{
return org.flixel.FlxG._game._framerate;
},
"static public function set framerate",function(Framerate)
{
org.flixel.FlxG._game._framerate=Framerate;
if(!org.flixel.FlxG._game._paused&&(org.flixel.FlxG._game.stage!=null))
org.flixel.FlxG._game.stage.frameRate=Framerate;
},
"static public function get frameratePaused",function()
{
return org.flixel.FlxG._game._frameratePaused;
},
"static public function set frameratePaused",function(Framerate)
{
org.flixel.FlxG._game._frameratePaused=Framerate;
if(org.flixel.FlxG._game._paused&&(org.flixel.FlxG._game.stage!=null))
org.flixel.FlxG._game.stage.frameRate=Framerate;
},
"static public function resetInput",function()
{
org.flixel.FlxG.keys.reset();
org.flixel.FlxG.mouse.reset();
var i=0;
var l=org.flixel.FlxG.gamepads.length;
while(i<l)
org.flixel.FlxG.gamepads[i++].reset();
},
"static public function playMusic",function(Music,Volume)
{if(arguments.length<2){Volume=1.0;}
if(org.flixel.FlxG.music==null)
org.flixel.FlxG.music=new org.flixel.FlxSound();
else if(org.flixel.FlxG.music.active)
org.flixel.FlxG.music.stop();
org.flixel.FlxG.music.loadEmbedded(Music,true);
org.flixel.FlxG.music.volume=Volume;
org.flixel.FlxG.music.survive=true;
org.flixel.FlxG.music.play();
},
"static public function play",function(EmbeddedSound,Volume,Looped)
{if(arguments.length<3){if(arguments.length<2){Volume=1.0;}Looped=false;}
var i=0;
var sl=org.flixel.FlxG.sounds.length;
while(i<sl)
{
if(!(as(org.flixel.FlxG.sounds[i],org.flixel.FlxSound)).active)
break;
i++;
}
if(org.flixel.FlxG.sounds[i]==null)
org.flixel.FlxG.sounds[i]=new org.flixel.FlxSound();
var s=org.flixel.FlxG.sounds[i];
s.loadEmbedded(EmbeddedSound,Looped);
s.volume=Volume;
s.play();
return s;
},
"static public function stream",function(URL,Volume,Looped)
{if(arguments.length<3){if(arguments.length<2){Volume=1.0;}Looped=false;}
var i=0;
var sl=org.flixel.FlxG.sounds.length;
while(i<sl)
{
if(!(as(org.flixel.FlxG.sounds[i],org.flixel.FlxSound)).active)
break;
i++;
}
if(org.flixel.FlxG.sounds[i]==null)
org.flixel.FlxG.sounds[i]=new org.flixel.FlxSound();
var s=org.flixel.FlxG.sounds[i];
s.loadStream(URL,Looped);
s.volume=Volume;
s.play();
return s;
},
"static public function get mute",function()
{
return org.flixel.FlxG._mute;
},
"static public function set mute",function(Mute)
{
org.flixel.FlxG._mute=Mute;
org.flixel.FlxG.changeSounds();
},
"static public function getMuteValue",function()
{
if(org.flixel.FlxG._mute)
return 0;
else
return 1;
},
"static public function get volume",function(){return org.flixel.FlxG._volume;},
"static public function set volume",function(Volume)
{
org.flixel.FlxG._volume=Volume;
if(org.flixel.FlxG._volume<0)
org.flixel.FlxG._volume=0;
else if(org.flixel.FlxG._volume>1)
org.flixel.FlxG._volume=1;
org.flixel.FlxG.changeSounds();
},
"static internal function destroySounds",function(ForceDestroy)
{if(arguments.length<1){ForceDestroy=false;}
if(org.flixel.FlxG.sounds==null)
return;
if((org.flixel.FlxG.music!=null)&&(ForceDestroy||!org.flixel.FlxG.music.survive))
org.flixel.FlxG.music.destroy();
var i=0;
var s;
var sl=org.flixel.FlxG.sounds.length;
while(i<sl)
{
s=as(org.flixel.FlxG.sounds[i++],org.flixel.FlxSound);
if((s!=null)&&(ForceDestroy||!s.survive))
s.destroy();
}
},
"static protected function changeSounds",function()
{
if((org.flixel.FlxG.music!=null)&&org.flixel.FlxG.music.active)
org.flixel.FlxG.music.updateTransform();
var i=0;
var s;
var sl=org.flixel.FlxG.sounds.length;
while(i<sl)
{
s=as(org.flixel.FlxG.sounds[i++],org.flixel.FlxSound);
if((s!=null)&&s.active)
s.updateTransform();
}
},
"static internal function updateSounds",function()
{
if((org.flixel.FlxG.music!=null)&&org.flixel.FlxG.music.active)
org.flixel.FlxG.music.update();
var i=0;
var s;
var sl=org.flixel.FlxG.sounds.length;
while(i<sl)
{
s=as(org.flixel.FlxG.sounds[i++],org.flixel.FlxSound);
if((s!=null)&&s.active)
s.update();
}
},
"static protected function pauseSounds",function()
{
if((org.flixel.FlxG.music!=null)&&org.flixel.FlxG.music.active)
org.flixel.FlxG.music.pause();
var i=0;
var s;
var sl=org.flixel.FlxG.sounds.length;
while(i<sl)
{
s=as(org.flixel.FlxG.sounds[i++],org.flixel.FlxSound);
if((s!=null)&&s.active)
s.pause();
}
},
"static protected function playSounds",function()
{
if((org.flixel.FlxG.music!=null)&&org.flixel.FlxG.music.active)
org.flixel.FlxG.music.play();
var i=0;
var s;
var sl=org.flixel.FlxG.sounds.length;
while(i<sl)
{
s=as(org.flixel.FlxG.sounds[i++],org.flixel.FlxSound);
if((s!=null)&&s.active)
s.play();
}
},
"static public function checkBitmapCache",function(Key)
{
return(org.flixel.FlxG._cache[Key]!=undefined)&&(org.flixel.FlxG._cache[Key]!=null);
},
"static public function createBitmap",function(Width,Height,Color,Unique,Key)
{if(arguments.length<5){if(arguments.length<4){Unique=false;}Key=null;}
var key=Key;
if(key==null)
{
key=Width+"x"+Height+":"+Color;
if(Unique&&(org.flixel.FlxG._cache[key]!=undefined)&&(org.flixel.FlxG._cache[key]!=null))
{
var inc=0;
var ukey;
do{ukey=key+inc++;
}while((org.flixel.FlxG._cache[ukey]!=undefined)&&(org.flixel.FlxG._cache[ukey]!=null));
key=ukey;
}
}
if(!org.flixel.FlxG.checkBitmapCache(key))
org.flixel.FlxG._cache[key]=new flash.display.BitmapData(Width,Height,true,Color);
return org.flixel.FlxG._cache[key];
},
"static public function addBitmap",function(Graphic,Reverse,Unique,Key)
{if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){Reverse=false;}Unique=false;}Key=null;}
var needReverse=false;
var key=Key;
if(key==null)
{
key=String(Graphic);
if(Unique&&(org.flixel.FlxG._cache[key]!=undefined)&&(org.flixel.FlxG._cache[key]!=null))
{
var inc=0;
var ukey;
do{ukey=key+inc++;
}while((org.flixel.FlxG._cache[ukey]!=undefined)&&(org.flixel.FlxG._cache[ukey]!=null));
key=ukey;
}
}
if(!org.flixel.FlxG.checkBitmapCache(key))
{
org.flixel.FlxG._cache[key]=(new Graphic).bitmapData;
if(Reverse)needReverse=true;
}
var pixels=org.flixel.FlxG._cache[key];
if(!needReverse&&Reverse&&(pixels.width==(new Graphic).bitmapData.width))
needReverse=true;
if(needReverse)
{
var newPixels=new flash.display.BitmapData(pixels.width<<1,pixels.height,true,0x00000000);
newPixels.draw(pixels);
var mtx=new flash.geom.Matrix();
mtx.scale(-1,1);
mtx.translate(newPixels.width,0);
newPixels.draw(pixels,mtx);
pixels=newPixels;
}
return pixels;
},
"static public function follow",function(Target,Lerp)
{if(arguments.length<2){Lerp=1;}
org.flixel.FlxG.followTarget=Target;
org.flixel.FlxG.followLerp=Lerp;
org.flixel.FlxG._scrollTarget.x=(org.flixel.FlxG.width>>1)-org.flixel.FlxG.followTarget.x-(org.flixel.FlxG.followTarget.width>>1);
org.flixel.FlxG._scrollTarget.y=(org.flixel.FlxG.height>>1)-org.flixel.FlxG.followTarget.y-(org.flixel.FlxG.followTarget.height>>1);
org.flixel.FlxG.scroll.x=org.flixel.FlxG._scrollTarget.x;
org.flixel.FlxG.scroll.y=org.flixel.FlxG._scrollTarget.y;
org.flixel.FlxG.doFollow();
},
"static public function followAdjust",function(LeadX,LeadY)
{if(arguments.length<2){if(arguments.length<1){LeadX=0;}LeadY=0;}
org.flixel.FlxG.followLead=new flash.geom.Point(LeadX,LeadY);
},
"static public function followBounds",function(MinX,MinY,MaxX,MaxY,UpdateWorldBounds)
{if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){MinX=0;}MinY=0;}MaxX=0;}MaxY=0;}UpdateWorldBounds=true;}
org.flixel.FlxG.followMin=new flash.geom.Point(-MinX,-MinY);
org.flixel.FlxG.followMax=new flash.geom.Point(-MaxX+org.flixel.FlxG.width,-MaxY+org.flixel.FlxG.height);
if(org.flixel.FlxG.followMax.x>org.flixel.FlxG.followMin.x)
org.flixel.FlxG.followMax.x=org.flixel.FlxG.followMin.x;
if(org.flixel.FlxG.followMax.y>org.flixel.FlxG.followMin.y)
org.flixel.FlxG.followMax.y=org.flixel.FlxG.followMin.y;
if(UpdateWorldBounds)
org.flixel.FlxU.setWorldBounds(MinX,MinY,MaxX-MinX,MaxY-MinY);
org.flixel.FlxG.doFollow();
},
"static public function get stage",function()
{
if((org.flixel.FlxG._game._state!=null)&&(org.flixel.FlxG._game._state.parent!=null))
return org.flixel.FlxG._game._state.parent.stage;
return null;
},
"static public function get state",function()
{
return org.flixel.FlxG._game._state;
},
"static public function set state",function(State)
{
org.flixel.FlxG._game.switchState(State);
},
"static public function unfollow",function()
{
org.flixel.FlxG.followTarget=null;
org.flixel.FlxG.followLead=null;
org.flixel.FlxG.followLerp=1;
org.flixel.FlxG.followMin=null;
org.flixel.FlxG.followMax=null;
if(org.flixel.FlxG.scroll==null)
org.flixel.FlxG.scroll=new flash.geom.Point();
else
org.flixel.FlxG.scroll.x=org.flixel.FlxG.scroll.y=0;
if(org.flixel.FlxG._scrollTarget==null)
org.flixel.FlxG._scrollTarget=new flash.geom.Point();
else
org.flixel.FlxG._scrollTarget.x=org.flixel.FlxG._scrollTarget.y=0;
},
"static internal function setGameData",function(Game,Width,Height,Zoom)
{
org.flixel.FlxG._game=Game;
org.flixel.FlxG._cache=new Object();
org.flixel.FlxG.width=Width;
org.flixel.FlxG.height=Height;
org.flixel.FlxG._mute=false;
org.flixel.FlxG._volume=0.5;
org.flixel.FlxG.sounds=new Array();
org.flixel.FlxG.mouse=new org.flixel.data.FlxMouse();
org.flixel.FlxG.keys=new org.flixel.data.FlxKeyboard();
org.flixel.FlxG.gamepads=new Array(4);
org.flixel.FlxG.gamepads[0]=new org.flixel.data.FlxGamepad();
org.flixel.FlxG.gamepads[1]=new org.flixel.data.FlxGamepad();
org.flixel.FlxG.gamepads[2]=new org.flixel.data.FlxGamepad();
org.flixel.FlxG.gamepads[3]=new org.flixel.data.FlxGamepad();
org.flixel.FlxG.scroll=null;
org.flixel.FlxG._scrollTarget=null;
org.flixel.FlxG.unfollow();
org.flixel.FlxG.levels=new Array();
org.flixel.FlxG.scores=new Array();
org.flixel.FlxG.level=0;
org.flixel.FlxG.score=0;
org.flixel.FlxG.kong=null;
org.flixel.FlxG.pause=false;
org.flixel.FlxG.timeScale=1.0;
org.flixel.FlxG.framerate=60;
org.flixel.FlxG.frameratePaused=10;
org.flixel.FlxG.maxElapsed=0.0333333;
org.flixel.FlxG.elapsed=0;
org.flixel.FlxG.showBounds=false;
org.flixel.FlxG.mobile=false;
org.flixel.FlxG.panel=new org.flixel.data.FlxPanel();
org.flixel.FlxG.quake=new org.flixel.data.FlxQuake(Zoom);
org.flixel.FlxG.flash=new org.flixel.data.FlxFlash();
org.flixel.FlxG.fade=new org.flixel.data.FlxFade();
org.flixel.FlxU.setWorldBounds(0,0,org.flixel.FlxG.width,org.flixel.FlxG.height);
},
"static internal function doFollow",function()
{
if(org.flixel.FlxG.followTarget!=null)
{
org.flixel.FlxG._scrollTarget.x=(org.flixel.FlxG.width>>1)-org.flixel.FlxG.followTarget.x-(org.flixel.FlxG.followTarget.width>>1);
org.flixel.FlxG._scrollTarget.y=(org.flixel.FlxG.height>>1)-org.flixel.FlxG.followTarget.y-(org.flixel.FlxG.followTarget.height>>1);
if((org.flixel.FlxG.followLead!=null)&&(is(org.flixel.FlxG.followTarget,org.flixel.FlxSprite)))
{
org.flixel.FlxG._scrollTarget.x-=(as(org.flixel.FlxG.followTarget,org.flixel.FlxSprite)).velocity.x*org.flixel.FlxG.followLead.x;
org.flixel.FlxG._scrollTarget.y-=(as(org.flixel.FlxG.followTarget,org.flixel.FlxSprite)).velocity.y*org.flixel.FlxG.followLead.y;
}
org.flixel.FlxG.scroll.x+=(org.flixel.FlxG._scrollTarget.x-org.flixel.FlxG.scroll.x)*org.flixel.FlxG.followLerp*org.flixel.FlxG.elapsed;
org.flixel.FlxG.scroll.y+=(org.flixel.FlxG._scrollTarget.y-org.flixel.FlxG.scroll.y)*org.flixel.FlxG.followLerp*org.flixel.FlxG.elapsed;
if(org.flixel.FlxG.followMin!=null)
{
if(org.flixel.FlxG.scroll.x>org.flixel.FlxG.followMin.x)
org.flixel.FlxG.scroll.x=org.flixel.FlxG.followMin.x;
if(org.flixel.FlxG.scroll.y>org.flixel.FlxG.followMin.y)
org.flixel.FlxG.scroll.y=org.flixel.FlxG.followMin.y;
}
if(org.flixel.FlxG.followMax!=null)
{
if(org.flixel.FlxG.scroll.x<org.flixel.FlxG.followMax.x)
org.flixel.FlxG.scroll.x=org.flixel.FlxG.followMax.x;
if(org.flixel.FlxG.scroll.y<org.flixel.FlxG.followMax.y)
org.flixel.FlxG.scroll.y=org.flixel.FlxG.followMax.y;
}
}
},
"static internal function updateInput",function()
{
org.flixel.FlxG.keys.update();
org.flixel.FlxG.mouse.update(org.flixel.FlxG.state.mouseX,org.flixel.FlxG.state.mouseY,org.flixel.FlxG.scroll.x,org.flixel.FlxG.scroll.y);
var i=0;
var l=org.flixel.FlxG.gamepads.length;
while(i<l)
org.flixel.FlxG.gamepads[i++].update();
},
];},["log","pause","framerate","frameratePaused","resetInput","playMusic","play","stream","mute","getMuteValue","volume","destroySounds","updateSounds","checkBitmapCache","createBitmap","addBitmap","follow","followAdjust","followBounds","stage","state","unfollow","setGameData","doFollow","updateInput"],["org.flixel.FlxSound","flash.display.BitmapData","String","flash.geom.Matrix","flash.geom.Point","org.flixel.FlxU","Object","Array","org.flixel.data.FlxMouse","org.flixel.data.FlxKeyboard","org.flixel.data.FlxGamepad","org.flixel.data.FlxPanel","org.flixel.data.FlxQuake","org.flixel.data.FlxFlash","org.flixel.data.FlxFade","org.flixel.FlxSprite"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxGame
joo.classLoader.prepare("package org.flixel",
"public class FlxGame extends flash.display.Sprite",6,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.display.StageScaleMode,flash.events.KeyboardEvent,flash.text.AntiAliasType,flash.events.MouseEvent,flash.display.StageAlign,org.flixel.FlxG,flash.text.GridFitType,flash.events.Event,org.flixel.FlxState);},

{Embed:{source:"org/flixel/data/nokiafc22.ttf",fontName:"system",embedAsCFF:"false"}},"protected var",{junk:null},
{Embed:{source:"org/flixel/data/beep.mp3"}},"protected var",{SndBeep:null},
{Embed:{source:"org/flixel/data/flixel.mp3"}},"protected var",{SndFlixel:null},
"public var",{useDefaultHotKeys:false},
"public var",{pause:null},
"internal var",{_iState:null},
"internal var",{_created:false},
"internal var",{_state:null},
"internal var",{_screen:null},
"internal var",{_buffer:null},
"internal var",{_zoom:0},
"internal var",{_gameXOffset:0},
"internal var",{_gameYOffset:0},
"internal var",{_frame:null},
"internal var",{_zeroPoint:null},
"internal var",{_elapsed:NaN},
"internal var",{_total:0},
"internal var",{_paused:false},
"internal var",{_framerate:0},
"internal var",{_frameratePaused:0},
"internal var",{_soundTray:null},
"internal var",{_soundTrayTimer:NaN},
"internal var",{_soundTrayBars:null},
"internal var",{_console:null},
"public function FlxGame",function(GameSizeX,GameSizeY,InitialState,Zoom)
{if(arguments.length<4){Zoom=2;}this.super$6();
flash.ui.Mouse.hide();
this._zoom=Zoom;
org.flixel.FlxState.bgColor=0xff000000;
org.flixel.FlxG.setGameData(this,GameSizeX,GameSizeY,Zoom);
this._elapsed=0;
this._total=0;
this.pause=new org.flixel.data.FlxPause();
this._state=null;
this._iState=InitialState;
this._zeroPoint=new flash.geom.Point();
this.useDefaultHotKeys=true;
this._frame=null;
this._gameXOffset=0;
this._gameYOffset=0;
this._paused=false;
this._created=false;
this.addEventListener(flash.events.Event.ENTER_FRAME,$$bound(this,"create"));
},
"protected function addFrame",function(Frame,ScreenOffsetX,ScreenOffsetY)
{
this._frame=Frame;
this._gameXOffset=ScreenOffsetX;
this._gameYOffset=ScreenOffsetY;
return this;
},
"public function showSoundTray",function(Silent)
{if(arguments.length<1){Silent=false;}
if(!Silent)
org.flixel.FlxG.play(this.SndBeep);
this._soundTrayTimer=1;
this._soundTray.y=this._gameYOffset*this._zoom;
this._soundTray.visible=true;
var gv=Math.round(org.flixel.FlxG.volume*10);
if(org.flixel.FlxG.mute)
gv=0;
for(var i=0;i<this._soundTrayBars.length;i++)
{
if(i<gv)this._soundTrayBars[i].alpha=1;
else this._soundTrayBars[i].alpha=0.5;
}
},
"public function switchState",function(State)
{
org.flixel.FlxG.panel.hide();
org.flixel.FlxG.unfollow();
org.flixel.FlxG.resetInput();
org.flixel.FlxG.destroySounds();
org.flixel.FlxG.flash.stop();
org.flixel.FlxG.fade.stop();
org.flixel.FlxG.quake.stop();
this._screen.x=0;
this._screen.y=0;
this._screen.addChild(State);
if(this._state!=null)
{
this._state.destroy();
this._screen.swapChildren(State,this._state);
this._screen.removeChild(this._state);
}
this._state=State;
this._state.scaleX=this._state.scaleY=this._zoom;
this._state.create();
},
"protected function onKeyUp",function(event)
{
if((event.keyCode==192)||(event.keyCode==220))
{
this._console.toggle();
return;
}
if(!org.flixel.FlxG.mobile&&this.useDefaultHotKeys)
{
var c=event.keyCode;
var code=String.fromCharCode(event.charCode);
switch(c)
{
case 48:
case 96:
org.flixel.FlxG.mute=!org.flixel.FlxG.mute;
this.showSoundTray();
return;
case 109:
case 189:
org.flixel.FlxG.mute=false;
org.flixel.FlxG.volume=org.flixel.FlxG.volume-0.1;
this.showSoundTray();
return;
case 107:
case 187:
org.flixel.FlxG.mute=false;
org.flixel.FlxG.volume=org.flixel.FlxG.volume+0.1;
this.showSoundTray();
return;
case 80:
org.flixel.FlxG.pause=!org.flixel.FlxG.pause;
default:break;
}
}
org.flixel.FlxG.keys.handleKeyUp(event);
var i=0;
var l=org.flixel.FlxG.gamepads.length;
while(i<l)
org.flixel.FlxG.gamepads[i++].handleKeyUp(event);
},
"protected function onKeyDown",function(event)
{
org.flixel.FlxG.keys.handleKeyDown(event);
var i=0;
var l=org.flixel.FlxG.gamepads.length;
while(i<l)
org.flixel.FlxG.gamepads[i++].handleKeyDown(event);
},
"protected function onFocus",function(event)
{if(arguments.length<1){event=null;}
if(org.flixel.FlxG.pause)
org.flixel.FlxG.pause=false;
},
"protected function onFocusLost",function(event)
{if(arguments.length<1){event=null;}
org.flixel.FlxG.pause=true;
},
"internal function unpauseGame",function()
{
if(!org.flixel.FlxG.panel.visible)flash.ui.Mouse.hide();
org.flixel.FlxG.resetInput();
this._paused=false;
this.stage.frameRate=this._framerate;
},
"internal function pauseGame",function()
{
if((this.x!=0)||(this.y!=0))
{
this.x=0;
this.y=0;
}
flash.ui.Mouse.show();
this._paused=true;
this.stage.frameRate=this._frameratePaused;
},
"protected function update",function(event)
{
var mark=flash.utils.getTimer();
var i;
var soundPrefs;
var ems=mark-this._total;
this._elapsed=ems/1000;
this._console.mtrTotal.add(ems);
this._total=mark;
org.flixel.FlxG.elapsed=this._elapsed;
if(org.flixel.FlxG.elapsed>org.flixel.FlxG.maxElapsed)
org.flixel.FlxG.elapsed=org.flixel.FlxG.maxElapsed;
org.flixel.FlxG.elapsed*=org.flixel.FlxG.timeScale;
if(this._soundTray!=null)
{
if(this._soundTrayTimer>0)
this._soundTrayTimer-=this._elapsed;
else if(this._soundTray.y>-this._soundTray.height)
{
this._soundTray.y-=this._elapsed*org.flixel.FlxG.height*2;
if(this._soundTray.y<=-this._soundTray.height)
{
this._soundTray.visible=false;
soundPrefs=new org.flixel.FlxSave();
if(soundPrefs.bind("flixel"))
{
if(soundPrefs.data.sound==null)
soundPrefs.data.sound=new Object;
soundPrefs.data.mute=org.flixel.FlxG.mute;
soundPrefs.data.volume=org.flixel.FlxG.volume;
soundPrefs.forceSave();
}
}
}
}
org.flixel.FlxG.panel.update();
if(this._console.visible)
this._console.update();
org.flixel.FlxG.updateInput();
org.flixel.FlxG.updateSounds();
if(this._paused)
this.pause.update();
else
{
org.flixel.FlxG.doFollow();
this._state.update();
if(org.flixel.FlxG.flash.exists)
org.flixel.FlxG.flash.update();
if(org.flixel.FlxG.fade.exists)
org.flixel.FlxG.fade.update();
org.flixel.FlxG.quake.update();
this._screen.x=org.flixel.FlxG.quake.x;
this._screen.y=org.flixel.FlxG.quake.y;
}
var updateMark=flash.utils.getTimer();
this._console.mtrUpdate.add(updateMark-mark);
org.flixel.FlxG.buffer.lock();
this._state.preProcess();
this._state.render();
if(org.flixel.FlxG.flash.exists)
org.flixel.FlxG.flash.render();
if(org.flixel.FlxG.fade.exists)
org.flixel.FlxG.fade.render();
if(org.flixel.FlxG.panel.visible)
org.flixel.FlxG.panel.render();
if(org.flixel.FlxG.mouse.cursor!=null)
{
if(org.flixel.FlxG.mouse.cursor.active)
org.flixel.FlxG.mouse.cursor.update();
if(org.flixel.FlxG.mouse.cursor.visible)
org.flixel.FlxG.mouse.cursor.render();
}
this._state.postProcess();
if(this._paused)
this.pause.render();
org.flixel.FlxG.buffer.unlock();
this._console.mtrRender.add(flash.utils.getTimer()-updateMark);
org.flixel.FlxG.mouse.wheel=0;
},
"internal function create",function(event)
{
if(this.root==null)
return;
var i;
var l;
var soundPrefs;
this.stage.scaleMode=flash.display.StageScaleMode.NO_SCALE;
this.stage.align=flash.display.StageAlign.TOP_LEFT;
this.stage.frameRate=this._framerate;
this._screen=new flash.display.Sprite();
this.addChild(this._screen);
var tmp=new flash.display.Bitmap(new flash.display.BitmapData(org.flixel.FlxG.width,org.flixel.FlxG.height,true,org.flixel.FlxState.bgColor));
tmp.x=this._gameXOffset;
tmp.y=this._gameYOffset;
tmp.scaleX=tmp.scaleY=this._zoom;
this._screen.addChild(tmp);
org.flixel.FlxG.buffer=tmp.bitmapData;
this._console=new org.flixel.data.FlxConsole(this._gameXOffset,this._gameYOffset,this._zoom);
if(!org.flixel.FlxG.mobile)
this.addChild(this._console);
var vstring=org.flixel.FlxG.LIBRARY_NAME+" v"+org.flixel.FlxG.LIBRARY_MAJOR_VERSION+"."+org.flixel.FlxG.LIBRARY_MINOR_VERSION;
if(org.flixel.FlxG.debug)
vstring+=" [debug]";
else
vstring+=" [release]";
var underline="";
i=0;
l=vstring.length+32;
while(i<l)
{
underline+="-";
i++;
}
org.flixel.FlxG.log(vstring);
org.flixel.FlxG.log(underline);
var flxMouse=org.flixel.FlxG.mouse;
this.stage.addEventListener(flash.events.MouseEvent.MOUSE_DOWN,$$bound(flxMouse,"handleMouseDown"));
this.stage.addEventListener(flash.events.MouseEvent.MOUSE_UP,$$bound(flxMouse,"handleMouseUp"));
this.stage.addEventListener(flash.events.KeyboardEvent.KEY_DOWN,$$bound(this,"onKeyDown"));
this.stage.addEventListener(flash.events.KeyboardEvent.KEY_UP,$$bound(this,"onKeyUp"));
if(!org.flixel.FlxG.mobile)
{
this.stage.addEventListener(flash.events.MouseEvent.MOUSE_OUT,$$bound(flxMouse,"handleMouseOut"));
this.stage.addEventListener(flash.events.MouseEvent.MOUSE_OVER,$$bound(flxMouse,"handleMouseOver"));
this.stage.addEventListener(flash.events.MouseEvent.MOUSE_WHEEL,$$bound(flxMouse,"handleMouseWheel"));
this.stage.addEventListener(flash.events.Event.DEACTIVATE,$$bound(this,"onFocusLost"));
this.stage.addEventListener(flash.events.Event.ACTIVATE,$$bound(this,"onFocus"));
this._soundTray=new flash.display.Sprite();
this._soundTray.visible=false;
this._soundTray.scaleX=2;
this._soundTray.scaleY=2;
tmp=new flash.display.Bitmap(new flash.display.BitmapData(80,30,true,0x7F000000));
this._soundTray.x=(this._gameXOffset+org.flixel.FlxG.width/2)*this._zoom-(tmp.width/2)*this._soundTray.scaleX;
this._soundTray.addChild(tmp);
var text=new flash.text.TextField();
text.width=tmp.width;
text.height=tmp.height;
text.multiline=true;
text.wordWrap=true;
text.selectable=false;
text.embedFonts=true;
text.antiAliasType=flash.text.AntiAliasType.NORMAL;
text.gridFitType=flash.text.GridFitType.PIXEL;
text.defaultTextFormat=new flash.text.TextFormat("system",8,0xffffff,null,null,null,null,null,"center");;
this._soundTray.addChild(text);
text.text="VOLUME";
text.y=16;
var bx=10;
var by=14;
this._soundTrayBars=new Array();
i=0;
while(i<10)
{
tmp=new flash.display.Bitmap(new flash.display.BitmapData(4,++i,false,0xffffff));
tmp.x=bx;
tmp.y=by;
this._soundTrayBars.push(this._soundTray.addChild(tmp));
bx+=6;
by--;
}
this.addChild(this._soundTray);
soundPrefs=new org.flixel.FlxSave();
if(soundPrefs.bind("flixel")&&(soundPrefs.data.sound!=null))
{
if(soundPrefs.data.volume!=null)
org.flixel.FlxG.volume=soundPrefs.data.volume;
if(soundPrefs.data.mute!=null)
org.flixel.FlxG.mute=soundPrefs.data.mute;
this.showSoundTray(true);
}
}
if(this._frame!=null)
{
var bmp=new this._frame();
bmp.scaleX=this._zoom;
bmp.scaleY=this._zoom;
this.addChild(bmp);
}
this.switchState(new this._iState());
org.flixel.FlxState.screen.unsafeBind(org.flixel.FlxG.buffer);
this.removeEventListener(flash.events.Event.ENTER_FRAME,$$bound(this,"create"));
this.addEventListener(flash.events.Event.ENTER_FRAME,$$bound(this,"update"));
},
];},[],["flash.display.Sprite","resource:org/flixel/data/nokiafc22.ttf","resource:org/flixel/data/beep.mp3","resource:org/flixel/data/flixel.mp3","flash.ui.Mouse","org.flixel.FlxState","org.flixel.FlxG","org.flixel.data.FlxPause","flash.geom.Point","flash.events.Event","Math","String","org.flixel.FlxSave","Object","flash.display.StageScaleMode","flash.display.StageAlign","flash.display.Bitmap","flash.display.BitmapData","org.flixel.data.FlxConsole","flash.events.MouseEvent","flash.events.KeyboardEvent","flash.text.TextField","flash.text.AntiAliasType","flash.text.GridFitType","flash.text.TextFormat","Array"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxGroup
joo.classLoader.prepare("package org.flixel",
"public class FlxGroup extends org.flixel.FlxObject",4,function($$private){var as=joo.as,$$bound=joo.boundMethod;return[

"static public const",{ASCENDING:-1},
"static public const",{DESCENDING:1},
"public var",{members:null},
"protected var",{_last:null},
"protected var",{_first:false},
"protected var",{_sortIndex:null},
"protected var",{_sortOrder:0},
"public function FlxGroup",function()
{
this.super$4();
this._group=true;
this.solid=false;
this.members=new Array();
this._last=new org.flixel.FlxPoint();
this._first=true;
},
"public function add",function(Object,ShareScroll)
{if(arguments.length<2){ShareScroll=false;}
if(this.members.indexOf(Object)<0)
this.members[this.members.length]=Object;
if(ShareScroll)
Object.scrollFactor=this.scrollFactor;
return Object;
},
"public function replace",function(OldObject,NewObject)
{
var index=this.members.indexOf(OldObject);
if((index<0)||(index>=this.members.length))
return null;
this.members[index]=NewObject;
return NewObject;
},
"public function remove",function(Object,Splice)
{if(arguments.length<2){Splice=false;}
var index=this.members.indexOf(Object);
if((index<0)||(index>=this.members.length))
return null;
if(Splice)
this.members.splice(index,1);
else
this.members[index]=null;
return Object;
},
"public function sort",function(Index,Order)
{if(arguments.length<2){if(arguments.length<1){Index="y";}Order=org.flixel.FlxGroup.ASCENDING;}
this._sortIndex=Index;
this._sortOrder=Order;
this.members.sort($$bound(this,"sortHandler"));
},
"public function getFirstAvail",function()
{
var i=0;
var o;
var ml=this.members.length;
while(i<ml)
{
o=as(this.members[i++],org.flixel.FlxObject);
if((o!=null)&&!o.exists)
return o;
}
return null;
},
"public function getFirstNull",function()
{
var i=0;
var ml=this.members.length;
while(i<ml)
{
if(this.members[i]==null)
return i;
else
i++;
}
return-1;
},
"public function resetFirstAvail",function(X,Y)
{if(arguments.length<2){if(arguments.length<1){X=0;}Y=0;}
var o=this.getFirstAvail();
if(o==null)
return false;
o.reset(X,Y);
return true;
},
"public function getFirstExtant",function()
{
var i=0;
var o;
var ml=this.members.length;
while(i<ml)
{
o=as(this.members[i++],org.flixel.FlxObject);
if((o!=null)&&o.exists)
return o;
}
return null;
},
"public function getFirstAlive",function()
{
var i=0;
var o;
var ml=this.members.length;
while(i<ml)
{
o=as(this.members[i++],org.flixel.FlxObject);
if((o!=null)&&o.exists&&!o.dead)
return o;
}
return null;
},
"public function getFirstDead",function()
{
var i=0;
var o;
var ml=this.members.length;
while(i<ml)
{
o=as(this.members[i++],org.flixel.FlxObject);
if((o!=null)&&o.dead)
return o;
}
return null;
},
"public function countLiving",function()
{
var count=-1;
var i=0;
var o;
var ml=this.members.length;
while(i<ml)
{
o=as(this.members[i++],org.flixel.FlxObject);
if(o!=null)
{
if(count<0)
count=0;
if(o.exists&&!o.dead)
count++;
}
}
return count;
},
"public function countDead",function()
{
var count=-1;
var i=0;
var o;
var ml=this.members.length;
while(i<ml)
{
o=as(this.members[i++],org.flixel.FlxObject);
if(o!=null)
{
if(count<0)
count=0;
if(o.dead)
count++;
}
}
return count;
},
"public function countOnScreen",function()
{
var count=-1;
var i=0;
var o;
var ml=this.members.length;
while(i<ml)
{
o=as(this.members[i++],org.flixel.FlxObject);
if(o!=null)
{
if(count<0)
count=0;
if(o.onScreen())
count++;
}
}
return count;
},
"public function getRandom",function()
{
var c=0;
var o=null;
var l=this.members.length;
var i=$$uint(org.flixel.FlxU.random()*l);
while((o==null)&&(c<this.members.length))
{
o=as(this.members[(++i)%l],org.flixel.FlxObject);
c++;
}
return o;
},
"protected function saveOldPosition",function()
{
if(this._first)
{
this._first=false;
this._last.x=0;
this._last.y=0;
return;
}
this._last.x=this.x;
this._last.y=this.y;
},
"protected function updateMembers",function()
{
var mx;
var my;
var moved=false;
if((this.x!=this._last.x)||(this.y!=this._last.y))
{
moved=true;
mx=this.x-this._last.x;
my=this.y-this._last.y;
}
var i=0;
var o;
var ml=this.members.length;
while(i<ml)
{
o=as(this.members[i++],org.flixel.FlxObject);
if((o!=null)&&o.exists)
{
if(moved)
{
if(o._group)
o.reset(o.x+mx,o.y+my);
else
{
o.x+=mx;
o.y+=my;
}
}
if(o.active)
o.update();
if(moved&&o.solid)
{
o.colHullX.width+=((mx>0)?mx:-mx);
if(mx<0)
o.colHullX.x+=mx;
o.colHullY.x=this.x;
o.colHullY.height+=((my>0)?my:-my);
if(my<0)
o.colHullY.y+=my;
o.colVector.x+=mx;
o.colVector.y+=my;
}
}
}
},
"override public function update",function()
{
this.saveOldPosition();
this.updateMotion();
this.updateMembers();
this.updateFlickering();
},
"protected function renderMembers",function()
{
var i=0;
var o;
var ml=this.members.length;
while(i<ml)
{
o=as(this.members[i++],org.flixel.FlxObject);
if((o!=null)&&o.exists&&o.visible)
o.render();
}
},
"override public function render",function()
{
this.renderMembers();
},
"protected function killMembers",function()
{
var i=0;
var o;
var ml=this.members.length;
while(i<ml)
{
o=as(this.members[i++],org.flixel.FlxObject);
if(o!=null)
o.kill();
}
},
"override public function kill",function()
{
this.killMembers();
this.kill$4();
},
"protected function destroyMembers",function()
{
var i=0;
var o;
var ml=this.members.length;
while(i<ml)
{
o=as(this.members[i++],org.flixel.FlxObject);
if(o!=null)
o.destroy();
}
this.members.length=0;
},
"override public function destroy",function()
{
this.destroyMembers();
this.destroy$4();
},
"override public function reset",function(X,Y)
{
this.saveOldPosition();
this.reset$4(X,Y);
var mx;
var my;
var moved=false;
if((this.x!=this._last.x)||(this.y!=this._last.y))
{
moved=true;
mx=this.x-this._last.x;
my=this.y-this._last.y;
}
var i=0;
var o;
var ml=this.members.length;
while(i<ml)
{
o=as(this.members[i++],org.flixel.FlxObject);
if((o!=null)&&o.exists)
{
if(moved)
{
if(o._group)
o.reset(o.x+mx,o.y+my);
else
{
o.x+=mx;
o.y+=my;
if(this.solid)
{
o.colHullX.width+=((mx>0)?mx:-mx);
if(mx<0)
o.colHullX.x+=mx;
o.colHullY.x=this.x;
o.colHullY.height+=((my>0)?my:-my);
if(my<0)
o.colHullY.y+=my;
o.colVector.x+=mx;
o.colVector.y+=my;
}
}
}
}
}
},
"protected function sortHandler",function(Obj1,Obj2)
{
if(Obj1[this._sortIndex]<Obj2[this._sortIndex])
return this._sortOrder;
else if(Obj1[this._sortIndex]>Obj2[this._sortIndex])
return-this._sortOrder;
return 0;
},
];},[],["org.flixel.FlxObject","Array","org.flixel.FlxPoint","uint","org.flixel.FlxU"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxMonitor
joo.classLoader.prepare("package org.flixel",
"public class FlxMonitor",1,function($$private){;return[

"protected var",{_size:0},
"protected var",{_itr:0},
"protected var",{_data:null},
"public function FlxMonitor",function(Size,Default)
{if(arguments.length<2){Default=0;}
this._size=Size;
if(this._size<=0)
this._size=1;
this._itr=0;
this._data=new Array(this._size);
var i=0;
while(i<this._size)
this._data[i++]=Default;
},
"public function add",function(Data)
{
this._data[this._itr++]=Data;
if(this._itr>=this._size)
this._itr=0;
},
"public function average",function()
{
var sum=0;
var i=0;
while(i<this._size)
sum+=this._data[i++];
return sum/this._size;
},
];},[],["Array"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxObject
joo.classLoader.prepare("package org.flixel",
"public class FlxObject extends org.flixel.FlxRect",3,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxU,org.flixel.FlxG);},

"public var",{exists:false},
"public var",{active:false},
"public var",{visible:false},
"protected var",{_solid:false},
"protected var",{_fixed:false},
"public var",{velocity:null},
"public var",{acceleration:null},
"public var",{drag:null},
"public var",{maxVelocity:null},
"public var",{angle:NaN},
"public var",{angularVelocity:NaN},
"public var",{angularAcceleration:NaN},
"public var",{angularDrag:NaN},
"public var",{maxAngular:NaN},
"public var",{origin:null},
"public var",{thrust:NaN},
"public var",{maxThrust:NaN},
"static protected const",{_pZero:function(){return(new org.flixel.FlxPoint());}},
"public var",{scrollFactor:null},
"protected var",{_flicker:false},
"protected var",{_flickerTimer:NaN},
"public var",{health:NaN},
"public var",{dead:false},
"protected var",{_point:null},
"protected var",{_rect:null},
"protected var",{_flashPoint:null},
"public var",{moves:false},
"public var",{colHullX:null},
"public var",{colHullY:null},
"public var",{colVector:null},
"public var",{colOffsets:null},
"internal var",{_group:false},
"public var",{onFloor:false},
"public var",{collideLeft:false},
"public var",{collideRight:false},
"public var",{collideTop:false},
"public var",{collideBottom:false},
"public function FlxObject",function(X,Y,Width,Height)
{if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){X=0;}Y=0;}Width=0;}Height=0;}
this.super$3(X,Y,Width,Height);
this.exists=true;
this.active=true;
this.visible=true;
this._solid=true;
this._fixed=false;
this.moves=true;
this.collideLeft=true;
this.collideRight=true;
this.collideTop=true;
this.collideBottom=true;
this.origin=new org.flixel.FlxPoint();
this.velocity=new org.flixel.FlxPoint();
this.acceleration=new org.flixel.FlxPoint();
this.drag=new org.flixel.FlxPoint();
this.maxVelocity=new org.flixel.FlxPoint(10000,10000);
this.angle=0;
this.angularVelocity=0;
this.angularAcceleration=0;
this.angularDrag=0;
this.maxAngular=10000;
this.thrust=0;
this.scrollFactor=new org.flixel.FlxPoint(1,1);
this._flicker=false;
this._flickerTimer=-1;
this.health=1;
this.dead=false;
this._point=new org.flixel.FlxPoint();
this._rect=new org.flixel.FlxRect();
this._flashPoint=new flash.geom.Point();
this.colHullX=new org.flixel.FlxRect();
this.colHullY=new org.flixel.FlxRect();
this.colVector=new org.flixel.FlxPoint();
this.colOffsets=new Array(new org.flixel.FlxPoint());
this._group=false;
},
"public function destroy",function()
{
},
"public function get solid",function()
{
return this._solid;
},
"public function set solid",function(Solid)
{
this._solid=Solid;
},
"public function get fixed",function()
{
return this._fixed;
},
"public function set fixed",function(Fixed)
{
this._fixed=Fixed;
},
"public function refreshHulls",function()
{
this.colHullX.x=this.x;
this.colHullX.y=this.y;
this.colHullX.width=this.width;
this.colHullX.height=this.height;
this.colHullY.x=this.x;
this.colHullY.y=this.y;
this.colHullY.width=this.width;
this.colHullY.height=this.height;
},
"protected function updateMotion",function()
{
if(!this.moves)
return;
if(this._solid)
this.refreshHulls();
this.onFloor=false;
var vc;
vc=(org.flixel.FlxU.computeVelocity(this.angularVelocity,this.angularAcceleration,this.angularDrag,this.maxAngular)-this.angularVelocity)/2;
this.angularVelocity+=vc;
this.angle+=this.angularVelocity*org.flixel.FlxG.elapsed;
this.angularVelocity+=vc;
var thrustComponents;
if(this.thrust!=0)
{
thrustComponents=org.flixel.FlxU.rotatePoint(-this.thrust,0,0,0,this.angle);
var maxComponents=org.flixel.FlxU.rotatePoint(-this.maxThrust,0,0,0,this.angle);
var max=((maxComponents.x>0)?maxComponents.x:-maxComponents.x);
if(max>((maxComponents.y>0)?maxComponents.y:-maxComponents.y))
maxComponents.y=max;
else
max=((maxComponents.y>0)?maxComponents.y:-maxComponents.y);
this.maxVelocity.x=this.maxVelocity.y=((max>0)?max:-max);
}
else
thrustComponents=org.flixel.FlxObject._pZero;
vc=(org.flixel.FlxU.computeVelocity(this.velocity.x,this.acceleration.x+thrustComponents.x,this.drag.x,this.maxVelocity.x)-this.velocity.x)/2;
this.velocity.x+=vc;
var xd=this.velocity.x*org.flixel.FlxG.elapsed;
this.velocity.x+=vc;
vc=(org.flixel.FlxU.computeVelocity(this.velocity.y,this.acceleration.y+thrustComponents.y,this.drag.y,this.maxVelocity.y)-this.velocity.y)/2;
this.velocity.y+=vc;
var yd=this.velocity.y*org.flixel.FlxG.elapsed;
this.velocity.y+=vc;
this.x+=xd;
this.y+=yd;
if(!this._solid)
return;
this.colVector.x=xd;
this.colVector.y=yd;
this.colHullX.width+=((this.colVector.x>0)?this.colVector.x:-this.colVector.x);
if(this.colVector.x<0)
this.colHullX.x+=this.colVector.x;
this.colHullY.x=this.x;
this.colHullY.height+=((this.colVector.y>0)?this.colVector.y:-this.colVector.y);
if(this.colVector.y<0)
this.colHullY.y+=this.colVector.y;
},
"protected function updateFlickering",function()
{
if(this.flickering())
{
if(this._flickerTimer>0)
{
this._flickerTimer=this._flickerTimer-org.flixel.FlxG.elapsed;
if(this._flickerTimer==0)
this._flickerTimer=-1;
}
if(this._flickerTimer<0)
this.flicker(-1);
else
{
this._flicker=!this._flicker;
this.visible=!this._flicker;
}
}
},
"public function update",function()
{
this.updateMotion();
this.updateFlickering();
},
"public function render",function()
{
},
"public function overlaps",function(Object)
{
this.getScreenXY(this._point);
var tx=this._point.x;
var ty=this._point.y;
Object.getScreenXY(this._point);
if((this._point.x<=tx-Object.width)||(this._point.x>=tx+this.width)||(this._point.y<=ty-Object.height)||(this._point.y>=ty+this.height))
return false;
return true;
},
"public function overlapsPoint",function(X,Y,PerPixel)
{if(arguments.length<3){PerPixel=false;}
X=X+org.flixel.FlxU.floor(org.flixel.FlxG.scroll.x);
Y=Y+org.flixel.FlxU.floor(org.flixel.FlxG.scroll.y);
this.getScreenXY(this._point);
if((X<=this._point.x)||(X>=this._point.x+this.width)||(Y<=this._point.y)||(Y>=this._point.y+this.height))
return false;
return true;
},
"public function collide",function(Object)
{if(arguments.length<1){Object=null;}
return org.flixel.FlxU.collide(this,((Object==null)?this:Object));
},
"public function preCollide",function(Object)
{
},
"public function hitLeft",function(Contact,Velocity)
{
this.hitSide(Contact,Velocity);
},
"public function hitRight",function(Contact,Velocity)
{
this.hitSide(Contact,Velocity);
},
"public function hitSide",function(Contact,Velocity)
{
if(!this.fixed||(Contact.fixed&&((this.velocity.y!=0)||(this.velocity.x!=0))))
this.velocity.x=Velocity;
},
"public function hitTop",function(Contact,Velocity)
{
if(!this.fixed||(Contact.fixed&&((this.velocity.y!=0)||(this.velocity.x!=0))))
this.velocity.y=Velocity;
},
"public function hitBottom",function(Contact,Velocity)
{
this.onFloor=true;
if(!this.fixed||(Contact.fixed&&((this.velocity.y!=0)||(this.velocity.x!=0))))
this.velocity.y=Velocity;
},
"virtual public function hurt",function(Damage)
{
this.health=this.health-Damage;
if(this.health<=0)
this.kill();
},
"public function kill",function()
{
this.exists=false;
this.dead=true;
},
"public function flicker",function(Duration){if(arguments.length<1){Duration=1;}this._flickerTimer=Duration;if(this._flickerTimer<0){this._flicker=false;this.visible=true;}},
"public function flickering",function(){return this._flickerTimer>=0;},
"public function getScreenXY",function(Point)
{if(arguments.length<1){Point=null;}
if(Point==null)Point=new org.flixel.FlxPoint();
Point.x=org.flixel.FlxU.floor(this.x+org.flixel.FlxU.roundingError)+org.flixel.FlxU.floor(org.flixel.FlxG.scroll.x*this.scrollFactor.x);
Point.y=org.flixel.FlxU.floor(this.y+org.flixel.FlxU.roundingError)+org.flixel.FlxU.floor(org.flixel.FlxG.scroll.y*this.scrollFactor.y);
return Point;
},
"public function onScreen",function()
{
this.getScreenXY(this._point);
if((this._point.x+this.width<0)||(this._point.x>org.flixel.FlxG.width)||(this._point.y+this.height<0)||(this._point.y>org.flixel.FlxG.height))
return false;
return true;
},
"public function reset",function(X,Y)
{
this.x=X;
this.y=Y;
this.exists=true;
this.dead=false;
},
"public function getBoundingColor",function()
{
if(this.solid)
{
if(this.fixed)
return 0x7f00f225;
else
return 0x7fff0012;
}
else
return 0x7f0090e9;
},
];},[],["org.flixel.FlxRect","org.flixel.FlxPoint","flash.geom.Point","Array","org.flixel.FlxU","org.flixel.FlxG"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxPoint
joo.classLoader.prepare("package org.flixel",
"public class FlxPoint",1,function($$private){;return[

"public var",{x:NaN},
"public var",{y:NaN},
"public function FlxPoint",function(X,Y)
{if(arguments.length<2){if(arguments.length<1){X=0;}Y=0;}
this.x=X;
this.y=Y;
},
"public function toString",function()
{
return org.flixel.FlxU.getClassName(this,true);
},
];},[],["org.flixel.FlxU"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxPreloader
joo.classLoader.prepare("package org.flixel",
"public class FlxPreloader extends flash.display.MovieClip",7,function($$private){var is=joo.is,as=joo.as,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.display.StageScaleMode,flash.events.MouseEvent,org.flixel.FlxG,flash.display.StageAlign,flash.events.Event);},

{Embed:{source:"org/flixel/data/logo.png"}},"protected var",{ImgLogo:null},
{Embed:{source:"org/flixel/data/logo_corners.png"}},"protected var",{ImgLogoCorners:null},
{Embed:{source:"org/flixel/data/logo_light.png"}},"protected var",{ImgLogoLight:null},
"protected var",{_init:false},
"protected var",{_buffer:null},
"protected var",{_bmpBar:null},
"protected var",{_text:null},
"protected var",{_width:0},
"protected var",{_height:0},
"protected var",{_logo:null},
"protected var",{_logoGlow:null},
"protected var",{_min:0},
"public var",{className:null},
"public var",{myURL:null},
"public var",{minDisplayTime:NaN},
"public function FlxPreloader",function()
{this.super$7();
this.minDisplayTime=0;
this.stop();
this.stage.scaleMode=flash.display.StageScaleMode.NO_SCALE;
this.stage.align=flash.display.StageAlign.TOP_LEFT;
try
{
throw new Error("Setting global debug flag...");
}
catch(e){if(is(e,Error))
{
var re=/\[.*:[0-9]+\]/;
org.flixel.FlxG.debug=re.test(e.getStackTrace());
}else throw e;}
var tmp;
if(!org.flixel.FlxG.debug&&(this.myURL!=null)&&(this.root.loaderInfo.url.indexOf(this.myURL)<0))
{
tmp=new flash.display.Bitmap(new flash.display.BitmapData(this.stage.stageWidth,this.stage.stageHeight,true,0xFFFFFFFF));
this.addChild(tmp);
var fmt=new flash.text.TextFormat();
fmt.color=0x000000;
fmt.size=16;
fmt.align="center";
fmt.bold=true;
fmt.font="system";
var txt=new flash.text.TextField();
txt.width=tmp.width-16;
txt.height=tmp.height-16;
txt.y=8;
txt.multiline=true;
txt.wordWrap=true;
txt.embedFonts=true;
txt.defaultTextFormat=fmt;
txt.text="Hi there!  It looks like somebody copied this game without my permission.  Just click anywhere, or copy-paste this URL into your browser.\n\n"+this.myURL+"\n\nto play the game at my site.  Thanks, and have fun!";
this.addChild(txt);
txt.addEventListener(flash.events.MouseEvent.CLICK,$$bound(this,"goToMyURL$7"));
tmp.addEventListener(flash.events.MouseEvent.CLICK,$$bound(this,"goToMyURL$7"));
return;
}
this._init=false;
this.addEventListener(flash.events.Event.ENTER_FRAME,$$bound(this,"onEnterFrame$7"));
},
"private function goToMyURL",function(event)
{if(arguments.length<1){event=null;}
flash.net.navigateToURL(new flash.net.URLRequest("http://"+this.myURL));
},
"private function onEnterFrame",function(event)
{
if(!this._init)
{
if((this.stage.stageWidth<=0)||(this.stage.stageHeight<=0))
return;
this.create();
this._init=true;
}
var i;
this.graphics.clear();
var time=flash.utils.getTimer();
if((this.framesLoaded>=this.totalFrames)&&(time>this._min))
{
this.removeEventListener(flash.events.Event.ENTER_FRAME,$$bound(this,"onEnterFrame$7"));
this.nextFrame();
var mainClass=(flash.utils.getDefinitionByName(this.className));
if(mainClass)
{
var app=new mainClass();
this.addChild(as(app,flash.display.DisplayObject));
}
this.removeChild(this._buffer);
}
else
{
var percent=this.root.loaderInfo.bytesLoaded/this.root.loaderInfo.bytesTotal;
if((this._min>0)&&(percent>time/this._min))
percent=time/this._min;
this.update(percent);
}
},
"protected function create",function()
{
this._min=0;
if(!org.flixel.FlxG.debug)
this._min=this.minDisplayTime*1000;
this._buffer=new flash.display.Sprite();
this._buffer.scaleX=2;
this._buffer.scaleY=2;
this.addChild(this._buffer);
this._width=this.stage.stageWidth/this._buffer.scaleX;
this._height=this.stage.stageHeight/this._buffer.scaleY;
this._buffer.addChild(new flash.display.Bitmap(new flash.display.BitmapData(this._width,this._height,false,0x00345e)));
var b=new this.ImgLogoLight();
b.smoothing=true;
b.width=b.height=this._height;
b.x=(this._width-b.width)/2;
this._buffer.addChild(b);
this._bmpBar=new flash.display.Bitmap(new flash.display.BitmapData(1,7,false,0x5f6aff));
this._bmpBar.x=4;
this._bmpBar.y=this._height-11;
this._buffer.addChild(this._bmpBar);
this._text=new flash.text.TextField();
this._text.defaultTextFormat=new flash.text.TextFormat("system",8,0x5f6aff);
this._text.embedFonts=true;
this._text.selectable=false;
this._text.multiline=false;
this._text.x=2;
this._text.y=this._bmpBar.y-11;
this._text.width=80;
this._buffer.addChild(this._text);
this._logo=new this.ImgLogo();
this._logo.scaleX=this._logo.scaleY=this._height/8;
this._logo.x=(this._width-this._logo.width)/2;
this._logo.y=(this._height-this._logo.height)/2;
this._buffer.addChild(this._logo);
this._logoGlow=new this.ImgLogo();
this._logoGlow.smoothing=true;
this._logoGlow.blendMode="screen";
this._logoGlow.scaleX=this._logoGlow.scaleY=this._height/8;
this._logoGlow.x=(this._width-this._logoGlow.width)/2;
this._logoGlow.y=(this._height-this._logoGlow.height)/2;
this._buffer.addChild(this._logoGlow);
b=new this.ImgLogoCorners();
b.smoothing=true;
b.width=this._width;
b.height=this._height;
this._buffer.addChild(b);
b=new flash.display.Bitmap(new flash.display.BitmapData(this._width,this._height,false,0xffffff));
var i=0;
var j=0;
while(i<this._height)
{
j=0;
while(j<this._width)
b.bitmapData.setPixel(j++,i,0);
i+=2;
}
b.blendMode="overlay";
b.alpha=0.25;
this._buffer.addChild(b);
},
"protected function update",function(Percent)
{
this._bmpBar.scaleX=Percent*(this._width-8);
this._text.text="FLX v"+org.flixel.FlxG.LIBRARY_MAJOR_VERSION+"."+org.flixel.FlxG.LIBRARY_MINOR_VERSION+" "+org.flixel.FlxU.floor(Percent*100)+"%";
this._text.setTextFormat(this._text.defaultTextFormat);
if(Percent<0.1)
{
this._logoGlow.alpha=0;
this._logo.alpha=0;
}
else if(Percent<0.15)
{
this._logoGlow.alpha=org.flixel.FlxU.random();
this._logo.alpha=0;
}
else if(Percent<0.2)
{
this._logoGlow.alpha=0;
this._logo.alpha=0;
}
else if(Percent<0.25)
{
this._logoGlow.alpha=0;
this._logo.alpha=org.flixel.FlxU.random();
}
else if(Percent<0.7)
{
this._logoGlow.alpha=(Percent-0.45)/0.45;
this._logo.alpha=1;
}
else if((Percent>0.8)&&(Percent<0.9))
{
this._logoGlow.alpha=1-(Percent-0.8)/0.1;
this._logo.alpha=0;
}
else if(Percent>0.9)
{
this._buffer.alpha=1-(Percent-0.9)/0.1;
}
},
];},[],["flash.display.MovieClip","resource:org/flixel/data/logo.png","resource:org/flixel/data/logo_corners.png","resource:org/flixel/data/logo_light.png","flash.display.StageScaleMode","flash.display.StageAlign","Error","org.flixel.FlxG","flash.display.Bitmap","flash.display.BitmapData","flash.text.TextFormat","flash.text.TextField","flash.events.MouseEvent","flash.events.Event","flash.net.URLRequest","Class","flash.display.DisplayObject","flash.display.Sprite","org.flixel.FlxU"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxQuadTree
joo.classLoader.prepare("package org.flixel",
"public class FlxQuadTree extends org.flixel.FlxRect",3,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxU);},

"static public var",{quadTree:null},
"static public var",{bounds:null},
"static public var",{divisions:0},
"static public const",{A_LIST:0},
"static public const",{B_LIST:1},
"protected var",{_canSubdivide:false},
"protected var",{_headA:null},
"protected var",{_tailA:null},
"protected var",{_headB:null},
"protected var",{_tailB:null},
"static protected var",{_min:0},
"protected var",{_nw:null},
"protected var",{_ne:null},
"protected var",{_se:null},
"protected var",{_sw:null},
"protected var",{_l:NaN},
"protected var",{_r:NaN},
"protected var",{_t:NaN},
"protected var",{_b:NaN},
"protected var",{_hw:NaN},
"protected var",{_hh:NaN},
"protected var",{_mx:NaN},
"protected var",{_my:NaN},
"static protected var",{_o:null},
"static protected var",{_ol:NaN},
"static protected var",{_ot:NaN},
"static protected var",{_or:NaN},
"static protected var",{_ob:NaN},
"static protected var",{_oa:0},
"static protected var",{_oc:null},
"public function FlxQuadTree",function(X,Y,Width,Height,Parent)
{if(arguments.length<5){Parent=null;}
this.super$3(X,Y,Width,Height);
this._headA=this._tailA=new org.flixel.data.FlxList();
this._headB=this._tailB=new org.flixel.data.FlxList();
if(Parent!=null)
{
var itr;
var ot;
if(Parent._headA.object!=null)
{
itr=Parent._headA;
while(itr!=null)
{
if(this._tailA.object!=null)
{
ot=this._tailA;
this._tailA=new org.flixel.data.FlxList();
ot.next=this._tailA;
}
this._tailA.object=itr.object;
itr=itr.next;
}
}
if(Parent._headB.object!=null)
{
itr=Parent._headB;
while(itr!=null)
{
if(this._tailB.object!=null)
{
ot=this._tailB;
this._tailB=new org.flixel.data.FlxList();
ot.next=this._tailB;
}
this._tailB.object=itr.object;
itr=itr.next;
}
}
}
else
org.flixel.FlxQuadTree._min=(this.width+this.height)/(2*org.flixel.FlxQuadTree.divisions);
this._canSubdivide=(this.width>org.flixel.FlxQuadTree._min)||(this.height>org.flixel.FlxQuadTree._min);
this._nw=null;
this._ne=null;
this._se=null;
this._sw=null;
this._l=this.x;
this._r=this.x+this.width;
this._hw=this.width/2;
this._mx=this._l+this._hw;
this._t=this.y;
this._b=this.y+this.height;
this._hh=this.height/2;
this._my=this._t+this._hh;
},
"public function add",function(Object,List)
{
org.flixel.FlxQuadTree._oa=List;
if(Object._group)
{
var i=0;
var m;
var members=(as(Object,org.flixel.FlxGroup)).members;
var l=members.length;
while(i<l)
{
m=as(members[i++],org.flixel.FlxObject);
if((m!=null)&&m.exists)
{
if(m._group)
this.add(m,List);
else if(m.solid)
{
org.flixel.FlxQuadTree._o=m;
org.flixel.FlxQuadTree._ol=org.flixel.FlxQuadTree._o.x;
org.flixel.FlxQuadTree._ot=org.flixel.FlxQuadTree._o.y;
org.flixel.FlxQuadTree._or=org.flixel.FlxQuadTree._o.x+org.flixel.FlxQuadTree._o.width;
org.flixel.FlxQuadTree._ob=org.flixel.FlxQuadTree._o.y+org.flixel.FlxQuadTree._o.height;
this.addObject();
}
}
}
}
if(Object.solid)
{
org.flixel.FlxQuadTree._o=Object;
org.flixel.FlxQuadTree._ol=org.flixel.FlxQuadTree._o.x;
org.flixel.FlxQuadTree._ot=org.flixel.FlxQuadTree._o.y;
org.flixel.FlxQuadTree._or=org.flixel.FlxQuadTree._o.x+org.flixel.FlxQuadTree._o.width;
org.flixel.FlxQuadTree._ob=org.flixel.FlxQuadTree._o.y+org.flixel.FlxQuadTree._o.height;
this.addObject();
}
},
"protected function addObject",function()
{
if(!this._canSubdivide||((this._l>=org.flixel.FlxQuadTree._ol)&&(this._r<=org.flixel.FlxQuadTree._or)&&(this._t>=org.flixel.FlxQuadTree._ot)&&(this._b<=org.flixel.FlxQuadTree._ob)))
{
this.addToList();
return;
}
if((org.flixel.FlxQuadTree._ol>this._l)&&(org.flixel.FlxQuadTree._or<this._mx))
{
if((org.flixel.FlxQuadTree._ot>this._t)&&(org.flixel.FlxQuadTree._ob<this._my))
{
if(this._nw==null)
this._nw=new org.flixel.FlxQuadTree(this._l,this._t,this._hw,this._hh,this);
this._nw.addObject();
return;
}
if((org.flixel.FlxQuadTree._ot>this._my)&&(org.flixel.FlxQuadTree._ob<this._b))
{
if(this._sw==null)
this._sw=new org.flixel.FlxQuadTree(this._l,this._my,this._hw,this._hh,this);
this._sw.addObject();
return;
}
}
if((org.flixel.FlxQuadTree._ol>this._mx)&&(org.flixel.FlxQuadTree._or<this._r))
{
if((org.flixel.FlxQuadTree._ot>this._t)&&(org.flixel.FlxQuadTree._ob<this._my))
{
if(this._ne==null)
this._ne=new org.flixel.FlxQuadTree(this._mx,this._t,this._hw,this._hh,this);
this._ne.addObject();
return;
}
if((org.flixel.FlxQuadTree._ot>this._my)&&(org.flixel.FlxQuadTree._ob<this._b))
{
if(this._se==null)
this._se=new org.flixel.FlxQuadTree(this._mx,this._my,this._hw,this._hh,this);
this._se.addObject();
return;
}
}
if((org.flixel.FlxQuadTree._or>this._l)&&(org.flixel.FlxQuadTree._ol<this._mx)&&(org.flixel.FlxQuadTree._ob>this._t)&&(org.flixel.FlxQuadTree._ot<this._my))
{
if(this._nw==null)
this._nw=new org.flixel.FlxQuadTree(this._l,this._t,this._hw,this._hh,this);
this._nw.addObject();
}
if((org.flixel.FlxQuadTree._or>this._mx)&&(org.flixel.FlxQuadTree._ol<this._r)&&(org.flixel.FlxQuadTree._ob>this._t)&&(org.flixel.FlxQuadTree._ot<this._my))
{
if(this._ne==null)
this._ne=new org.flixel.FlxQuadTree(this._mx,this._t,this._hw,this._hh,this);
this._ne.addObject();
}
if((org.flixel.FlxQuadTree._or>this._mx)&&(org.flixel.FlxQuadTree._ol<this._r)&&(org.flixel.FlxQuadTree._ob>this._my)&&(org.flixel.FlxQuadTree._ot<this._b))
{
if(this._se==null)
this._se=new org.flixel.FlxQuadTree(this._mx,this._my,this._hw,this._hh,this);
this._se.addObject();
}
if((org.flixel.FlxQuadTree._or>this._l)&&(org.flixel.FlxQuadTree._ol<this._mx)&&(org.flixel.FlxQuadTree._ob>this._my)&&(org.flixel.FlxQuadTree._ot<this._b))
{
if(this._sw==null)
this._sw=new org.flixel.FlxQuadTree(this._l,this._my,this._hw,this._hh,this);
this._sw.addObject();
}
},
"protected function addToList",function()
{
var ot;
if(org.flixel.FlxQuadTree._oa==org.flixel.FlxQuadTree.A_LIST)
{
if(this._tailA.object!=null)
{
ot=this._tailA;
this._tailA=new org.flixel.data.FlxList();
ot.next=this._tailA;
}
this._tailA.object=org.flixel.FlxQuadTree._o;
}
else
{
if(this._tailB.object!=null)
{
ot=this._tailB;
this._tailB=new org.flixel.data.FlxList();
ot.next=this._tailB;
}
this._tailB.object=org.flixel.FlxQuadTree._o;
}
if(!this._canSubdivide)
return;
if(this._nw!=null)
this._nw.addToList();
if(this._ne!=null)
this._ne.addToList();
if(this._se!=null)
this._se.addToList();
if(this._sw!=null)
this._sw.addToList();
},
"public function overlap",function(BothLists,Callback)
{if(arguments.length<2){if(arguments.length<1){BothLists=true;}Callback=null;}
org.flixel.FlxQuadTree._oc=Callback;
var c=false;
var itr;
if(BothLists)
{
org.flixel.FlxQuadTree._oa=org.flixel.FlxQuadTree.B_LIST;
if(this._headA.object!=null)
{
itr=this._headA;
while(itr!=null)
{
org.flixel.FlxQuadTree._o=itr.object;
if(org.flixel.FlxQuadTree._o.exists&&org.flixel.FlxQuadTree._o.solid&&this.overlapNode())
c=true;
itr=itr.next;
}
}
org.flixel.FlxQuadTree._oa=org.flixel.FlxQuadTree.A_LIST;
if(this._headB.object!=null)
{
itr=this._headB;
while(itr!=null)
{
org.flixel.FlxQuadTree._o=itr.object;
if(org.flixel.FlxQuadTree._o.exists&&org.flixel.FlxQuadTree._o.solid)
{
if((this._nw!=null)&&this._nw.overlapNode())
c=true;
if((this._ne!=null)&&this._ne.overlapNode())
c=true;
if((this._se!=null)&&this._se.overlapNode())
c=true;
if((this._sw!=null)&&this._sw.overlapNode())
c=true;
}
itr=itr.next;
}
}
}
else
{
if(this._headA.object!=null)
{
itr=this._headA;
while(itr!=null)
{
org.flixel.FlxQuadTree._o=itr.object;
if(org.flixel.FlxQuadTree._o.exists&&org.flixel.FlxQuadTree._o.solid&&this.overlapNode(itr.next))
c=true;
itr=itr.next;
}
}
}
if((this._nw!=null)&&this._nw.overlap(BothLists,org.flixel.FlxQuadTree._oc))
c=true;
if((this._ne!=null)&&this._ne.overlap(BothLists,org.flixel.FlxQuadTree._oc))
c=true;
if((this._se!=null)&&this._se.overlap(BothLists,org.flixel.FlxQuadTree._oc))
c=true;
if((this._sw!=null)&&this._sw.overlap(BothLists,org.flixel.FlxQuadTree._oc))
c=true;
return c;
},
"protected function overlapNode",function(Iterator)
{if(arguments.length<1){Iterator=null;}
var c=false;
var co;
var itr=Iterator;
if(itr==null)
{
if(org.flixel.FlxQuadTree._oa==org.flixel.FlxQuadTree.A_LIST)
itr=this._headA;
else
itr=this._headB;
}
if(itr.object!=null)
{
while(itr!=null)
{
co=itr.object;
if((org.flixel.FlxQuadTree._o===co)||!co.exists||!org.flixel.FlxQuadTree._o.exists||!co.solid||!org.flixel.FlxQuadTree._o.solid||
(org.flixel.FlxQuadTree._o.x+org.flixel.FlxQuadTree._o.width<co.x+org.flixel.FlxU.roundingError)||
(org.flixel.FlxQuadTree._o.x+org.flixel.FlxU.roundingError>co.x+co.width)||
(org.flixel.FlxQuadTree._o.y+org.flixel.FlxQuadTree._o.height<co.y+org.flixel.FlxU.roundingError)||
(org.flixel.FlxQuadTree._o.y+org.flixel.FlxU.roundingError>co.y+co.height))
{
itr=itr.next;
continue;
}
if(org.flixel.FlxQuadTree._oc==null)
{
org.flixel.FlxQuadTree._o.kill();
co.kill();
c=true;
}
else if(org.flixel.FlxQuadTree._oc(org.flixel.FlxQuadTree._o,co))
c=true;
itr=itr.next;
}
}
return c;
},
];},[],["org.flixel.FlxRect","org.flixel.data.FlxList","org.flixel.FlxGroup","org.flixel.FlxObject","org.flixel.FlxU"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxRect
joo.classLoader.prepare("package org.flixel",
"public class FlxRect extends org.flixel.FlxPoint",2,function($$private){;return[

"public var",{width:NaN},
"public var",{height:NaN},
"public function FlxRect",function(X,Y,Width,Height)
{if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){X=0;}Y=0;}Width=0;}Height=0;}
this.super$2(X,Y);
this.width=Width;
this.height=Height;
},
"public function get left",function()
{
return this.x;
},
"public function get right",function()
{
return this.x+this.width;
},
"public function get top",function()
{
return this.y;
},
"public function get bottom",function()
{
return this.y+this.height;
},
];},[],["org.flixel.FlxPoint"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxSave
joo.classLoader.prepare("package org.flixel",
"public class FlxSave extends Object",1,function($$private){var is=joo.is;return[function(){joo.classLoader.init(flash.net.SharedObjectFlushStatus);},

"public var",{data:null},
"public var",{name:null},
"protected var",{_so:null},
"public function FlxSave",function()
{
this.name=null;
this._so=null;
this.data=null;
},
"public function bind",function(Name)
{
this.name=null;
this._so=null;
this.data=null;
this.name=Name;
try
{
this._so=flash.net.SharedObject.getLocal(this.name);
}
catch(e){if(is(e,Error))
{
org.flixel.FlxG.log("WARNING: There was a problem binding to\nthe shared object data from FlxSave.");
this.name=null;
this._so=null;
this.data=null;
return false;
}else throw e;}
this.data=this._so.data;
return true;
},
"public function write",function(FieldName,FieldValue,MinFileSize)
{if(arguments.length<3){MinFileSize=0;}
if(this._so==null)
{
org.flixel.FlxG.log("WARNING: You must call FlxSave.bind()\nbefore calling FlxSave.write().");
return false;
}
this.data[FieldName]=FieldValue;
return this.forceSave(MinFileSize);
},
"public function read",function(FieldName)
{
if(this._so==null)
{
org.flixel.FlxG.log("WARNING: You must call FlxSave.bind()\nbefore calling FlxSave.read().");
return null;
}
return this.data[FieldName];
},
"public function forceSave",function(MinFileSize)
{if(arguments.length<1){MinFileSize=0;}
if(this._so==null)
{
org.flixel.FlxG.log("WARNING: You must call FlxSave.bind()\nbefore calling FlxSave.forceSave().");
return false;
}
var status=null;
try
{
status=this._so.flush(MinFileSize);
}
catch(e){if(is(e,Error))
{
org.flixel.FlxG.log("WARNING: There was a problem flushing\nthe shared object data from FlxSave.");
return false;
}else throw e;}
return status==flash.net.SharedObjectFlushStatus.FLUSHED;
},
"public function erase",function(MinFileSize)
{if(arguments.length<1){MinFileSize=0;}
if(this._so==null)
{
org.flixel.FlxG.log("WARNING: You must call FlxSave.bind()\nbefore calling FlxSave.erase().");
return false;
}
this._so.clear();
return this.forceSave(MinFileSize);
},
];},[],["Object","flash.net.SharedObject","Error","org.flixel.FlxG","flash.net.SharedObjectFlushStatus"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxSound
joo.classLoader.prepare("package org.flixel",
"public class FlxSound extends org.flixel.FlxObject",4,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG,flash.events.Event);},

"public var",{survive:false},
"public var",{playing:false},
"public var",{name:null},
"public var",{artist:null},
"protected var",{_init:false},
"protected var",{_sound:null},
"protected var",{_channel:null},
"protected var",{_transform:null},
"protected var",{_position:NaN},
"protected var",{_volume:NaN},
"protected var",{_volumeAdjust:NaN},
"protected var",{_looped:false},
"protected var",{_core:null},
"protected var",{_radius:NaN},
"protected var",{_pan:false},
"protected var",{_fadeOutTimer:NaN},
"protected var",{_fadeOutTotal:NaN},
"protected var",{_pauseOnFadeOut:false},
"protected var",{_fadeInTimer:NaN},
"protected var",{_fadeInTotal:NaN},
"protected var",{_point2:null},
"public function FlxSound",function()
{
this.super$4();
this._point2=new org.flixel.FlxPoint();
this._transform=new flash.media.SoundTransform();
this.init();
this.fixed=true;
},
"protected function init",function()
{
this._transform.pan=0;
this._sound=null;
this._position=0;
this._volume=1.0;
this._volumeAdjust=1.0;
this._looped=false;
this._core=null;
this._radius=0;
this._pan=false;
this._fadeOutTimer=0;
this._fadeOutTotal=0;
this._pauseOnFadeOut=false;
this._fadeInTimer=0;
this._fadeInTotal=0;
this.active=false;
this.visible=false;
this.solid=false;
this.playing=false;
this.name=null;
this.artist=null;
},
"public function loadEmbedded",function(EmbeddedSound,Looped)
{if(arguments.length<2){Looped=false;}
this.stop();
this.init();
this._sound=new EmbeddedSound();
this._looped=Looped;
this.updateTransform();
this.active=true;
return this;
},
"public function loadStream",function(SoundURL,Looped)
{if(arguments.length<2){Looped=false;}
this.stop();
this.init();
this._sound=new flash.media.Sound();
this._sound.addEventListener(flash.events.Event.ID3,$$bound(this,"gotID3"));
this._sound.load(new flash.net.URLRequest(SoundURL));
this._looped=Looped;
this.updateTransform();
this.active=true;
return this;
},
"public function proximity",function(X,Y,Core,Radius,Pan)
{if(arguments.length<5){Pan=true;}
this.x=X;
this.y=Y;
this._core=Core;
this._radius=Radius;
this._pan=Pan;
return this;
},
"public function play",function()
{
if(this._position<0)
return;
if(this._looped)
{
if(this._position==0)
{
if(this._channel==null)
this._channel=this._sound.play(0,9999,this._transform);
if(this._channel==null)
this.active=false;
}
else
{
this._channel=this._sound.play(this._position,0,this._transform);
if(this._channel==null)
this.active=false;
else
this._channel.addEventListener(flash.events.Event.SOUND_COMPLETE,$$bound(this,"looped"));
}
}
else
{
if(this._position==0)
{
if(this._channel==null)
{
this._channel=this._sound.play(0,0,this._transform);
if(this._channel==null)
this.active=false;
else
this._channel.addEventListener(flash.events.Event.SOUND_COMPLETE,$$bound(this,"stopped"));
}
}
else
{
this._channel=this._sound.play(this._position,0,this._transform);
if(this._channel==null)
this.active=false;
}
}
this.playing=(this._channel!=null);
this._position=0;
},
"public function pause",function()
{
if(this._channel==null)
{
this._position=-1;
return;
}
this._position=this._channel.position;
this._channel.stop();
if(this._looped)
{
while(this._position>=this._sound.length)
this._position-=this._sound.length;
}
this._channel=null;
this.playing=false;
},
"public function stop",function()
{
this._position=0;
if(this._channel!=null)
{
this._channel.stop();
this.stopped();
}
},
"public function fadeOut",function(Seconds,PauseInstead)
{if(arguments.length<2){PauseInstead=false;}
this._pauseOnFadeOut=PauseInstead;
this._fadeInTimer=0;
this._fadeOutTimer=Seconds;
this._fadeOutTotal=this._fadeOutTimer;
},
"public function fadeIn",function(Seconds)
{
this._fadeOutTimer=0;
this._fadeInTimer=Seconds;
this._fadeInTotal=this._fadeInTimer;
this.play();
},
"public function get volume",function()
{
return this._volume;
},
"public function set volume",function(Volume)
{
this._volume=Volume;
if(this._volume<0)
this._volume=0;
else if(this._volume>1)
this._volume=1;
this.updateTransform();
},
"protected function updateSound",function()
{
if(this._position!=0)
return;
var radial=1.0;
var fade=1.0;
if(this._core!=null)
{
var _point=new org.flixel.FlxPoint();
var _point2=new org.flixel.FlxPoint();
this._core.getScreenXY(_point);
this.getScreenXY(_point2);
var dx=_point.x-_point2.x;
var dy=_point.y-_point2.y;
radial=(this._radius-Math.sqrt(dx*dx+dy*dy))/this._radius;
if(radial<0)radial=0;
if(radial>1)radial=1;
if(this._pan)
{
var d=-dx/this._radius;
if(d<-1)d=-1;
else if(d>1)d=1;
this._transform.pan=d;
}
}
if(this._fadeOutTimer>0)
{
this._fadeOutTimer-=org.flixel.FlxG.elapsed;
if(this._fadeOutTimer<=0)
{
if(this._pauseOnFadeOut)
this.pause();
else
this.stop();
}
fade=this._fadeOutTimer/this._fadeOutTotal;
if(fade<0)fade=0;
}
else if(this._fadeInTimer>0)
{
this._fadeInTimer-=org.flixel.FlxG.elapsed;
fade=this._fadeInTimer/this._fadeInTotal;
if(fade<0)fade=0;
fade=1-fade;
}
this._volumeAdjust=radial*fade;
this.updateTransform();
},
"override public function update",function()
{
this.update$4();
this.updateSound();
},
"override public function destroy",function()
{
if(this.active)
this.stop();
},
"internal function updateTransform",function()
{
this._transform.volume=org.flixel.FlxG.getMuteValue()*org.flixel.FlxG.volume*this._volume*this._volumeAdjust;
if(this._channel!=null)
this._channel.soundTransform=this._transform;
},
"protected function looped",function(event)
{if(arguments.length<1){event=null;}
if(this._channel==null)
return;
this._channel.removeEventListener(flash.events.Event.SOUND_COMPLETE,$$bound(this,"looped"));
this._channel=null;
this.play();
},
"protected function stopped",function(event)
{if(arguments.length<1){event=null;}
if(!this._looped)
this._channel.removeEventListener(flash.events.Event.SOUND_COMPLETE,$$bound(this,"stopped"));
else
this._channel.removeEventListener(flash.events.Event.SOUND_COMPLETE,$$bound(this,"looped"));
this._channel=null;
this.active=false;
this.playing=false;
},
"protected function gotID3",function(event)
{if(arguments.length<1){event=null;}
org.flixel.FlxG.log("got ID3 info!");
if(this._sound.id3.songName.length>0)
this.name=this._sound.id3.songName;
if(this._sound.id3.artist.length>0)
this.artist=this._sound.id3.artist;
this._sound.removeEventListener(flash.events.Event.ID3,$$bound(this,"gotID3"));
},
];},[],["org.flixel.FlxObject","org.flixel.FlxPoint","flash.media.SoundTransform","flash.media.Sound","flash.events.Event","flash.net.URLRequest","Math","org.flixel.FlxG"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxSprite
joo.classLoader.prepare("package org.flixel",
"public class FlxSprite extends org.flixel.FlxObject",4,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxU,org.flixel.FlxG);},

"static public const",{LEFT:0},
"static public const",{RIGHT:1},
"static public const",{UP:2},
"static public const",{DOWN:3},
"public var",{offset:null},
"public var",{scale:null},
"public var",{blend:null},
"public var",{antialiasing:false},
"public var",{finished:false},
"public var",{frameWidth:0},
"public var",{frameHeight:0},
"public var",{frames:0},
"protected var",{_animations:null},
"protected var",{_flipped:0},
"protected var",{_curAnim:null},
"protected var",{_curFrame:0},
"protected var",{_caf:0},
"protected var",{_frameTimer:NaN},
"protected var",{_callback:null},
"protected var",{_facing:0},
"protected var",{_bakedRotation:NaN},
"protected var",{_flashRect:null},
"protected var",{_flashRect2:null},
"protected var",{_flashPointZero:null},
"protected var",{_pixels:null},
"protected var",{_colorTransformedPixels:null},
"protected var",{_framePixels:null},
"protected var",{_alpha:NaN},
"protected var",{_color:0},
"protected var",{_ct:null},
"protected var",{_mtx:null},
"protected var",{_bbb:null},
"protected var",{_boundsVisible:false},
"static protected var",{_gfxSprite:null},
"static protected var",{_gfx:null},
"public function FlxSprite",function(X,Y,SimpleGraphic)
{if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){X=0;}Y=0;}SimpleGraphic=null;}
this.super$4();
this.x=X;
this.y=Y;
this._flashRect=new flash.geom.Rectangle();
this._flashRect2=new flash.geom.Rectangle();
this._flashPointZero=new flash.geom.Point();
this.offset=new org.flixel.FlxPoint();
this.scale=new org.flixel.FlxPoint(1,1);
this._alpha=1;
this._color=0x00ffffff;
this.blend=null;
this.antialiasing=false;
this.finished=false;
this._facing=org.flixel.FlxSprite.RIGHT;
this._animations=new Array();
this._flipped=0;
this._curAnim=null;
this._curFrame=0;
this._caf=0;
this._frameTimer=0;
this._mtx=new flash.geom.Matrix();
this._callback=null;
if(org.flixel.FlxSprite._gfxSprite==null)
{
org.flixel.FlxSprite._gfxSprite=new flash.display.Sprite();
org.flixel.FlxSprite._gfx=org.flixel.FlxSprite._gfxSprite.graphics;
}
if(SimpleGraphic==null)
this.createGraphic(8,8);
else
this.loadGraphic(SimpleGraphic);
},
"public function loadGraphic",function(Graphic,Animated,Reverse,Width,Height,Unique)
{if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){Animated=false;}Reverse=false;}Width=0;}Height=0;}Unique=false;}
this._bakedRotation=0;
this._pixels=org.flixel.FlxG.addBitmap(Graphic,Reverse,Unique);
this._colorTransformedPixels=null;
if(Reverse)
this._flipped=this._pixels.width>>1;
else
this._flipped=0;
if(Width==0)
{
if(Animated)
Width=this._pixels.height;
else if(this._flipped>0)
Width=this._pixels.width*0.5;
else
Width=this._pixels.width;
}
this.width=this.frameWidth=Width;
if(Height==0)
{
if(Animated)
Height=this.width;
else
Height=this._pixels.height;
}
this.height=this.frameHeight=Height;
this.resetHelpers();
return this;
},
"public function loadRotatedGraphic",function(Graphic,Rotations,Frame,AntiAliasing,AutoBuffer)
{if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){Rotations=16;}Frame=-1;}AntiAliasing=false;}AutoBuffer=false;}
var rows=Math.sqrt(Rotations);
var brush=org.flixel.FlxG.addBitmap(Graphic);
if(Frame>=0)
{
var full=brush;
brush=new flash.display.BitmapData(full.height,full.height);
var rx=Frame*brush.width;
var ry=0;
var fw=full.width;
if(rx>=fw)
{
ry=$$uint(rx/fw)*brush.height;
rx%=fw;
}
this._flashRect.x=rx;
this._flashRect.y=ry;
this._flashRect.width=brush.width;
this._flashRect.height=brush.height;
brush.copyPixels(full,this._flashRect,this._flashPointZero);
}
var max=brush.width;
if(brush.height>max)
max=brush.height;
if(AutoBuffer)
max*=1.5;
var cols=org.flixel.FlxU.ceil(Rotations/rows);
this.width=max*cols;
this.height=max*rows;
var key=String(Graphic)+":"+Frame+":"+this.width+"x"+this.height;
var skipGen=org.flixel.FlxG.checkBitmapCache(key);
this._pixels=org.flixel.FlxG.createBitmap(this.width,this.height,0,true,key);
this._colorTransformedPixels=null;
this.width=this.frameWidth=this._pixels.width;
this.height=this.frameHeight=this._pixels.height;
this._bakedRotation=360/Rotations;
if(!skipGen)
{
var r=0;
var c;
var ba=0;
var bw2=brush.width*0.5;
var bh2=brush.height*0.5;
var gxc=max*0.5;
var gyc=max*0.5;
while(r<rows)
{
c=0;
while(c<cols)
{
this._mtx.identity();
this._mtx.translate(-bw2,-bh2);
this._mtx.rotate(ba*0.017453293);
this._mtx.translate(max*c+gxc,gyc);
ba+=this._bakedRotation;
this._pixels.draw(brush,this._mtx,null,null,null,AntiAliasing);
c++;
}
gyc+=max;
r++;
}
}
this.frameWidth=this.frameHeight=this.width=this.height=max;
this.resetHelpers();
return this;
},
"public function createGraphic",function(Width,Height,Color,Unique,Key)
{if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){Color=0xffffffff;}Unique=false;}Key=null;}
this._bakedRotation=0;
this._pixels=org.flixel.FlxG.createBitmap(Width,Height,Color,Unique,Key);
this._colorTransformedPixels=null;
this.width=this.frameWidth=this._pixels.width;
this.height=this.frameHeight=this._pixels.height;
this.resetHelpers();
return this;
},
"public function get pixels",function()
{
return this._pixels;
},
"public function set pixels",function(Pixels)
{
this._pixels=Pixels;
this._colorTransformedPixels=null;
this.width=this.frameWidth=this._pixels.width;
this.height=this.frameHeight=this._pixels.height;
this.resetHelpers();
},
"protected function resetHelpers",function()
{
this._boundsVisible=false;
this._flashRect.x=0;
this._flashRect.y=0;
this._flashRect.width=this.frameWidth;
this._flashRect.height=this.frameHeight;
this._flashRect2.x=0;
this._flashRect2.y=0;
this._flashRect2.width=this._pixels.width;
this._flashRect2.height=this._pixels.height;
if((this._framePixels==null)||(this._framePixels.width!=this.width)||(this._framePixels.height!=this.height))
this._framePixels=new flash.display.BitmapData(this.width,this.height);
if((this._bbb==null)||(this._bbb.width!=this.width)||(this._bbb.height!=this.height))
this._bbb=new flash.display.BitmapData(this.width,this.height);
this.origin.x=this.frameWidth*0.5;
this.origin.y=this.frameHeight*0.5;
this._framePixels.copyPixels(this._pixels,this._flashRect,this._flashPointZero);
this.frames=(this._flashRect2.width/this._flashRect.width)*(this._flashRect2.height/this._flashRect.height);
if(this._ct!=null)this._framePixels.colorTransform(this._flashRect,this._ct);
if(org.flixel.FlxG.showBounds)
this.drawBounds();
this._caf=0;
this.refreshHulls();
},
"override public function set solid",function(Solid)
{
var os=this._solid;
this._solid=Solid;
if((os!=this._solid)&&org.flixel.FlxG.showBounds)
this.calcFrame();
},
"override public function set fixed",function(Fixed)
{
var of=this._fixed;
this._fixed=Fixed;
if((of!=this._fixed)&&org.flixel.FlxG.showBounds)
this.calcFrame();
},
"public function get facing",function()
{
return this._facing;
},
"public function set facing",function(Direction)
{
var c=this._facing!=Direction;
this._facing=Direction;
if(c)this.calcFrame();
},
"public function get alpha",function()
{
return this._alpha;
},
"public function set alpha",function(Alpha)
{
if(Alpha>1)Alpha=1;
if(Alpha<0)Alpha=0;
if(Alpha==this._alpha)return;
this._alpha=Alpha;
if((this._alpha!=1)||(this._color!=0x00ffffff))this._ct=new flash.geom.ColorTransform((this._color>>16)*0.00392,(this._color>>8&0xff)*0.00392,(this._color&0xff)*0.00392,this._alpha);
else this._ct=null;
this.calcFrame();
},
"public function get color",function()
{
return this._color;
},
"public function set color",function(Color)
{
Color&=0x00ffffff;
if(this._color==Color)return;
this._color=Color;
if((this._alpha!=1)||(this._color!=0x00ffffff))this._ct=new flash.geom.ColorTransform((this._color>>16)*0.00392,(this._color>>8&0xff)*0.00392,(this._color&0xff)*0.00392,this._alpha);
else this._ct=null;
this.calcFrame();
},
"public function draw",function(Brush,X,Y)
{if(arguments.length<3){if(arguments.length<2){X=0;}Y=0;}
var b=Brush._framePixels;
if(((Brush.angle==0)||(Brush._bakedRotation>0))&&(Brush.scale.x==1)&&(Brush.scale.y==1)&&(Brush.blend==null))
{
this._flashPoint.x=X;
this._flashPoint.y=Y;
this._flashRect2.width=b.width;
this._flashRect2.height=b.height;
this._pixels.copyPixels(b,this._flashRect2,this._flashPoint,null,null,true);
this._flashRect2.width=this._pixels.width;
this._flashRect2.height=this._pixels.height;
this.calcFrame();
return;
}
this._mtx.identity();
this._mtx.translate(-Brush.origin.x,-Brush.origin.y);
this._mtx.scale(Brush.scale.x,Brush.scale.y);
if(Brush.angle!=0)
this._mtx.rotate(Brush.angle*0.017453293);
this._mtx.translate(X+Brush.origin.x,Y+Brush.origin.y);
this._pixels.draw(b,this._mtx,null,Brush.blend,null,Brush.antialiasing);
this._colorTransformedPixels=null;
this.calcFrame();
},
"public function drawLine",function(StartX,StartY,EndX,EndY,Color,Thickness)
{if(arguments.length<6){Thickness=1;}
org.flixel.FlxSprite._gfx.clear();
org.flixel.FlxSprite._gfx.moveTo(StartX,StartY);
org.flixel.FlxSprite._gfx.lineStyle(Thickness,Color);
org.flixel.FlxSprite._gfx.lineTo(EndX,EndY);
this._pixels.draw(org.flixel.FlxSprite._gfxSprite);
this._colorTransformedPixels=null;
this.calcFrame();
},
"public function fill",function(Color)
{
this._pixels.fillRect(this._flashRect2,Color);
this._colorTransformedPixels=null;
if(this._pixels!=this._framePixels)
this.calcFrame();
},
"protected function updateAnimation",function()
{
if(this._bakedRotation)
{
var oc=this._caf;
var ta=this.angle%360;
if(ta<0)
ta+=360;
this._caf=ta/this._bakedRotation;
if(oc!=this._caf)
this.calcFrame();
return;
}
if((this._curAnim!=null)&&(this._curAnim.delay>0)&&(this._curAnim.looped||!this.finished))
{
this._frameTimer+=org.flixel.FlxG.elapsed;
while(this._frameTimer>this._curAnim.delay)
{
this._frameTimer=this._frameTimer-this._curAnim.delay;
if(this._curFrame==this._curAnim.frames.length-1)
{
if(this._curAnim.looped)this._curFrame=0;
this.finished=true;
}
else
this._curFrame++;
this._caf=this._curAnim.frames[this._curFrame];
this.calcFrame();
}
}
},
"override public function update",function()
{
this.updateMotion();
this.updateAnimation();
this.updateFlickering();
},
"protected function renderSprite",function()
{
if(org.flixel.FlxG.showBounds!=this._boundsVisible)
this.calcFrame();
this.getScreenXY(this._point);
this._flashPoint.x=this._point.x;
this._flashPoint.y=this._point.y;
if(((this.angle==0)||(this._bakedRotation>0))&&(this.scale.x==1)&&(this.scale.y==1)&&(this.blend==null))
{
org.flixel.FlxG.buffer.copyPixels(this._framePixels,this._flashRect,this._flashPoint,null,null,true);
return;
}
this._mtx.identity();
this._mtx.translate(-this.origin.x,-this.origin.y);
this._mtx.scale(this.scale.x,this.scale.y);
if(this.angle!=0)
this._mtx.rotate(this.angle*0.017453293);
this._mtx.translate(this._point.x+this.origin.x,this._point.y+this.origin.y);
org.flixel.FlxG.buffer.draw(this._framePixels,this._mtx,null,this.blend,null,this.antialiasing);
},
"override public function render",function()
{
this.renderSprite();
},
"override public function overlapsPoint",function(X,Y,PerPixel)
{if(arguments.length<3){PerPixel=false;}
X=X+org.flixel.FlxU.floor(org.flixel.FlxG.scroll.x);
Y=Y+org.flixel.FlxU.floor(org.flixel.FlxG.scroll.y);
this.getScreenXY(this._point);
if(PerPixel)
return this._framePixels.hitTest(new flash.geom.Point(0,0),0xFF,new flash.geom.Point(X-this._point.x,Y-this._point.y));
else if((X<=this._point.x)||(X>=this._point.x+this.frameWidth)||(Y<=this._point.y)||(Y>=this._point.y+this.frameHeight))
return false;
return true;
},
"virtual public function onEmit",function(){},
"public function addAnimation",function(Name,Frames,FrameRate,Looped)
{if(arguments.length<4){if(arguments.length<3){FrameRate=0;}Looped=true;}
this._animations.push(new org.flixel.data.FlxAnim(Name,Frames,FrameRate,Looped));
},
"public function addAnimationCallback",function(AnimationCallback)
{
this._callback=AnimationCallback;
},
"public function play",function(AnimName,Force)
{if(arguments.length<2){Force=false;}
if(!Force&&(this._curAnim!=null)&&(AnimName==this._curAnim.name)&&(this._curAnim.looped||!this.finished))return;
this._curFrame=0;
this._caf=0;
this._frameTimer=0;
var i=0;
var al=this._animations.length;
while(i<al)
{
if(this._animations[i].name==AnimName)
{
this._curAnim=this._animations[i];
if(this._curAnim.delay<=0)
this.finished=true;
else
this.finished=false;
this._caf=this._curAnim.frames[this._curFrame];
this.calcFrame();
return;
}
i++;
}
},
"public function randomFrame",function()
{
this._curAnim=null;
this._caf=$$int(org.flixel.FlxU.random()*(this._pixels.width/this.frameWidth));
this.calcFrame();
},
"public function get frame",function()
{
return this._caf;
},
"public function set frame",function(Frame)
{
this._curAnim=null;
this._caf=Frame;
this.calcFrame();
},
"override public function getScreenXY",function(Point)
{if(arguments.length<1){Point=null;}
if(Point==null)Point=new org.flixel.FlxPoint();
Point.x=org.flixel.FlxU.floor(this.x+org.flixel.FlxU.roundingError)+org.flixel.FlxU.floor(org.flixel.FlxG.scroll.x*this.scrollFactor.x)-this.offset.x;
Point.y=org.flixel.FlxU.floor(this.y+org.flixel.FlxU.roundingError)+org.flixel.FlxU.floor(org.flixel.FlxG.scroll.y*this.scrollFactor.y)-this.offset.y;
return Point;
},
"protected function calcFrame",function()
{
this._boundsVisible=false;
var rx=this._caf*this.frameWidth;
var ry=0;
var w=this._flipped?this._flipped:this._pixels.width;
if(rx>=w)
{
ry=$$uint(rx/w)*this.frameHeight;
rx%=w;
}
if(this._flipped&&(this._facing==org.flixel.FlxSprite.LEFT))
rx=(this._flipped<<1)-rx-this.frameWidth;
this._flashRect.x=rx;
this._flashRect.y=ry;
if(this._colorTransformedPixels==null)
{
if(this._ct==null)
this._colorTransformedPixels=this._pixels;
else{
this._colorTransformedPixels=new flash.display.BitmapData(this._pixels.width,this._pixels.height,this._pixels.transparent,0);
this._colorTransformedPixels.copyPixels(this._pixels,this._pixels.rect,this._flashPointZero);
this._colorTransformedPixels.colorTransform(this._colorTransformedPixels.rect,this._ct);
}
}
this._framePixels.copyPixels(this._colorTransformedPixels,this._flashRect,this._flashPointZero);
this._flashRect.x=this._flashRect.y=0;
if(org.flixel.FlxG.showBounds)
this.drawBounds();
if(this._callback!=null)this._callback(this._curAnim.name,this._curFrame,this._caf);
},
"protected function drawBounds",function()
{
this._boundsVisible=true;
if((this._bbb==null)||(this._bbb.width!=this.width)||(this._bbb.height!=this.height))
this._bbb=new flash.display.BitmapData(this.width,this.height);
var bbbc=this.getBoundingColor();
this._bbb.fillRect(this._flashRect,0);
var ofrw=this._flashRect.width;
var ofrh=this._flashRect.height;
this._flashRect.width=$$int(this.width);
this._flashRect.height=$$int(this.height);
this._bbb.fillRect(this._flashRect,bbbc);
this._flashRect.width=this._flashRect.width-2;
this._flashRect.height=this._flashRect.height-2;
this._flashRect.x=1;
this._flashRect.y=1;
this._bbb.fillRect(this._flashRect,0);
this._flashRect.width=ofrw;
this._flashRect.height=ofrh;
this._flashRect.x=this._flashRect.y=0;
this._flashPoint.x=$$int(this.offset.x);
this._flashPoint.y=$$int(this.offset.y);
this._framePixels.copyPixels(this._bbb,this._flashRect,this._flashPoint,null,null,true);
},
"internal function unsafeBind",function(Pixels)
{
this._pixels=this._framePixels=Pixels;
this._colorTransformedPixels=null;
},
];},[],["org.flixel.FlxObject","flash.geom.Rectangle","flash.geom.Point","org.flixel.FlxPoint","Array","flash.geom.Matrix","flash.display.Sprite","org.flixel.FlxG","Math","flash.display.BitmapData","uint","org.flixel.FlxU","String","flash.geom.ColorTransform","org.flixel.data.FlxAnim","int"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxState
joo.classLoader.prepare("package org.flixel",
"public class FlxState extends flash.display.Sprite",6,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},

"static public var",{screen:null},
"static public var",{bgColor:0},
"public var",{defaultGroup:null},
"public function FlxState",function()
{
this.super$6();
this.defaultGroup=new org.flixel.FlxGroup();
if(org.flixel.FlxState.screen==null)
{
org.flixel.FlxState.screen=new org.flixel.FlxSprite();
org.flixel.FlxState.screen.createGraphic(org.flixel.FlxG.width,org.flixel.FlxG.height,0,true);
org.flixel.FlxState.screen.origin.x=org.flixel.FlxState.screen.origin.y=0;
org.flixel.FlxState.screen.antialiasing=true;
org.flixel.FlxState.screen.exists=false;
org.flixel.FlxState.screen.solid=false;
org.flixel.FlxState.screen.fixed=true;
}
},
"public function create",function()
{
},
"public function add",function(Core)
{
return this.defaultGroup.add(Core);
},
"public function preProcess",function()
{
org.flixel.FlxState.screen.fill(org.flixel.FlxState.bgColor);
},
"public function update",function()
{
this.defaultGroup.update();
},
"public function collide",function()
{
org.flixel.FlxU.collide(this.defaultGroup,this.defaultGroup);
},
"public function render",function()
{
this.defaultGroup.render();
},
"public function postProcess",function()
{
},
"public function destroy",function()
{
this.defaultGroup.destroy();
},
];},[],["flash.display.Sprite","org.flixel.FlxGroup","org.flixel.FlxSprite","org.flixel.FlxG","org.flixel.FlxU"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxText
joo.classLoader.prepare("package org.flixel",
"public class FlxText extends org.flixel.FlxSprite",5,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxG);},

"protected var",{_tf:null},
"protected var",{_regen:false},
"protected var",{_shadow:0},
"public function FlxText",function(X,Y,Width,Text,EmbeddedFont)
{if(arguments.length<5){if(arguments.length<4){Text=null;}EmbeddedFont=true;}
this.super$5(X,Y);
this.createGraphic(Width,1,0);
if(Text==null)
Text="";
this._tf=new flash.text.TextField();
this._tf.width=Width;
this._tf.embedFonts=EmbeddedFont;
this._tf.selectable=false;
this._tf.sharpness=100;
this._tf.multiline=true;
this._tf.wordWrap=true;
this._tf.text=Text;
var tf=new flash.text.TextFormat("system",8,0xffffff);
this._tf.defaultTextFormat=tf;
this._tf.setTextFormat(tf);
if(Text.length<=0)
this._tf.height=1;
else
this._tf.height=10;
this._regen=true;
this._shadow=0;
this.solid=false;
this.calcFrame();
},
"public function setFormat",function(Font,Size,Color,Alignment,ShadowColor)
{if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){Font=null;}Size=8;}Color=0xffffff;}Alignment=null;}ShadowColor=0;}
if(Font==null)
Font="";
var tf=this.dtfCopy();
tf.font=Font;
tf.size=Size;
tf.color=Color;
tf.align=Alignment;
this._tf.defaultTextFormat=tf;
this._tf.setTextFormat(tf);
this._shadow=ShadowColor;
this._regen=true;
this.calcFrame();
return this;
},
"public function get text",function()
{
return this._tf.text;
},
"public function set text",function(Text)
{
var ot=this._tf.text;
this._tf.text=Text;
if(this._tf.text!=ot)
{
this._regen=true;
this.calcFrame();
}
},
"public function get size",function()
{
return as(this._tf.defaultTextFormat.size,Number);
},
"public function set size",function(Size)
{
var tf=this.dtfCopy();
tf.size=Size;
this._tf.defaultTextFormat=tf;
this._tf.setTextFormat(tf);
this._regen=true;
this.calcFrame();
},
"override public function get color",function()
{
return as(this._tf.defaultTextFormat.color,uint);
},
"override public function set color",function(Color)
{
var tf=this.dtfCopy();
tf.color=Color;
this._tf.defaultTextFormat=tf;
this._tf.setTextFormat(tf);
this._regen=true;
this.calcFrame();
},
"public function get font",function()
{
return this._tf.defaultTextFormat.font;
},
"public function set font",function(Font)
{
var tf=this.dtfCopy();
tf.font=Font;
this._tf.defaultTextFormat=tf;
this._tf.setTextFormat(tf);
this._regen=true;
this.calcFrame();
},
"public function get alignment",function()
{
return this._tf.defaultTextFormat.align;
},
"public function set alignment",function(Alignment)
{
var tf=this.dtfCopy();
tf.align=Alignment;
this._tf.defaultTextFormat=tf;
this._tf.setTextFormat(tf);
this.calcFrame();
},
"public function get shadow",function()
{
return this._shadow;
},
"public function set shadow",function(Color)
{
this._shadow=Color;
this.calcFrame();
},
"override protected function calcFrame",function()
{
if(this._regen)
{
var i=0;
var nl=this._tf.numLines;
this.height=0;
while(i<nl)
this.height+=this._tf.getLineMetrics(i++).height;
this.height+=4;
this._pixels=new flash.display.BitmapData(this.width,this.height,true,0);
this._bbb=new flash.display.BitmapData(this.width,this.height,true,0);
this.frameHeight=this.height;
this._tf.height=this.height*1.2;
this._flashRect.x=0;
this._flashRect.y=0;
this._flashRect.width=this.width;
this._flashRect.height=this.height;
this._regen=false;
}
else
this._pixels.fillRect(this._flashRect,0);
if((this._tf!=null)&&(this._tf.text!=null)&&(this._tf.text.length>0))
{
var tf=this._tf.defaultTextFormat;
var tfa=tf;
this._mtx.identity();
if((tf.align=="center")&&(this._tf.numLines==1))
{
tfa=new flash.text.TextFormat(tf.font,tf.size,tf.color,null,null,null,null,null,"left");
this._tf.setTextFormat(tfa);
this._mtx.translate(Math.floor((this.width-this._tf.getLineMetrics(0).width)/2),0);
}
if(this._shadow>0)
{
this._tf.setTextFormat(new flash.text.TextFormat(tfa.font,tfa.size,this._shadow,null,null,null,null,null,tfa.align));
this._mtx.translate(1,1);
this._pixels.draw(this._tf,this._mtx,this._ct);
this._mtx.translate(-1,-1);
this._tf.setTextFormat(new flash.text.TextFormat(tfa.font,tfa.size,tfa.color,null,null,null,null,null,tfa.align));
}
this._pixels.draw(this._tf,this._mtx,this._ct);
this._tf.setTextFormat(new flash.text.TextFormat(tf.font,tf.size,tf.color,null,null,null,null,null,tf.align));
}
if((this._framePixels==null)||(this._framePixels.width!=this._pixels.width)||(this._framePixels.height!=this._pixels.height))
this._framePixels=new flash.display.BitmapData(this._pixels.width,this._pixels.height,true,0);
this._framePixels.copyPixels(this._pixels,this._flashRect,this._flashPointZero);
if(org.flixel.FlxG.showBounds)
this.drawBounds();
if(this.solid)
this.refreshHulls();
},
"protected function dtfCopy",function()
{
var dtf=this._tf.defaultTextFormat;
return new flash.text.TextFormat(dtf.font,dtf.size,dtf.color,dtf.bold,dtf.italic,dtf.underline,dtf.url,dtf.target,dtf.align);
},
];},[],["org.flixel.FlxSprite","flash.text.TextField","flash.text.TextFormat","Number","uint","flash.display.BitmapData","Math","org.flixel.FlxG"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxTileblock
joo.classLoader.prepare("package org.flixel",
"public class FlxTileblock extends org.flixel.FlxSprite",5,function($$private){;return[

"public function FlxTileblock",function(X,Y,Width,Height)
{
this.super$5(X,Y);
this.createGraphic(Width,Height,0,true);
this.fixed=true;
},
"public function loadTiles",function(TileGraphic,TileWidth,TileHeight,Empties)
{if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){TileWidth=0;}TileHeight=0;}Empties=0;}
if(TileGraphic==null)
return this;
var s=new org.flixel.FlxSprite().loadGraphic(TileGraphic,true,false,TileWidth,TileHeight);
var sw=s.width;
var sh=s.height;
var total=s.frames+Empties;
var regen=false;
if(this.width%s.width!=0)
{
this.width=$$uint(this.width/sw+1)*sw;
regen=true;
}
if(this.height%s.height!=0)
{
this.height=$$uint(this.height/sh+1)*sh;
regen=true;
}
if(regen)
this.createGraphic(this.width,this.height,0,true);
else
this.fill(0);
var r=0;
var c;
var ox;
var oy=0;
var widthInTiles=this.width/sw;
var heightInTiles=this.height/sh;
while(r<heightInTiles)
{
ox=0;
c=0;
while(c<widthInTiles)
{
if(org.flixel.FlxU.random()*total>Empties)
{
s.randomFrame();
this.draw(s,ox,oy);
}
ox+=sw;
c++;
}
oy+=sh;
r++;
}
return this;
},
"override public function loadGraphic",function(Graphic,Animated,Reverse,Width,Height,Unique)
{if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){Animated=false;}Reverse=false;}Width=0;}Height=0;}Unique=false;}
this.loadTiles(Graphic);
return this;
},
];},[],["org.flixel.FlxSprite","uint","org.flixel.FlxU"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxTilemap
joo.classLoader.prepare("package org.flixel",
"public class FlxTilemap extends org.flixel.FlxObject",4,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxG);},

{Embed:{source:"org/flixel/data/autotiles.png"}},"static public var",{ImgAuto:null},
{Embed:{source:"org/flixel/data/autotiles_alt.png"}},"static public var",{ImgAutoAlt:null},
"static public const",{OFF:0},
"static public const",{AUTO:1},
"static public const",{ALT:2},
"public var",{collideIndex:0},
"public var",{startingIndex:0},
"public var",{drawIndex:0},
"public var",{auto:0},
"public var",{refresh:false},
"public var",{widthInTiles:0},
"public var",{heightInTiles:0},
"public var",{totalTiles:0},
"protected var",{_flashRect:null},
"protected var",{_flashRect2:null},
"protected var",{_pixels:null},
"protected var",{_bbPixels:null},
"protected var",{_buffer:null},
"protected var",{_bufferLoc:null},
"protected var",{_bbKey:null},
"protected var",{_data:null},
"protected var",{_rects:null},
"protected var",{_tileWidth:0},
"protected var",{_tileHeight:0},
"protected var",{_block:null},
"protected var",{_callbacks:null},
"protected var",{_screenRows:0},
"protected var",{_screenCols:0},
"protected var",{_boundsVisible:false},
"public function FlxTilemap",function()
{
this.super$4();
this.auto=org.flixel.FlxTilemap.OFF;
this.collideIndex=1;
this.startingIndex=0;
this.drawIndex=1;
this.widthInTiles=0;
this.heightInTiles=0;
this.totalTiles=0;
this._buffer=null;
this._bufferLoc=new org.flixel.FlxPoint();
this._flashRect2=new flash.geom.Rectangle();
this._flashRect=this._flashRect2;
this._data=null;
this._tileWidth=0;
this._tileHeight=0;
this._rects=null;
this._pixels=null;
this._block=new org.flixel.FlxObject();
this._block.width=this._block.height=0;
this._block.fixed=true;
this._callbacks=new Array();
this.fixed=true;
},
"public function loadMap",function(MapData,TileGraphic,TileWidth,TileHeight)
{if(arguments.length<4){if(arguments.length<3){TileWidth=0;}TileHeight=0;}
this.refresh=true;
var cols;
var rows=MapData.split("\n");
this.heightInTiles=rows.length;
this._data=new Array();
var r=0;
var c;
while(r<this.heightInTiles)
{
cols=rows[r++].split(",");
if(cols.length<=1)
{
this.heightInTiles=this.heightInTiles-1;
continue;
}
if(this.widthInTiles==0)
this.widthInTiles=cols.length;
c=0;
while(c<this.widthInTiles)
this._data.push($$uint(cols[c++]));
}
var i;
this.totalTiles=this.widthInTiles*this.heightInTiles;
if(this.auto>org.flixel.FlxTilemap.OFF)
{
this.collideIndex=this.startingIndex=this.drawIndex=1;
i=0;
while(i<this.totalTiles)
this.autoTile(i++);
}
this._pixels=org.flixel.FlxG.addBitmap(TileGraphic);
this._tileWidth=TileWidth;
if(this._tileWidth==0)
this._tileWidth=this._pixels.height;
this._tileHeight=TileHeight;
if(this._tileHeight==0)
this._tileHeight=this._tileWidth;
this._block.width=this._tileWidth;
this._block.height=this._tileHeight;
this.width=this.widthInTiles*this._tileWidth;
this.height=this.heightInTiles*this._tileHeight;
this._rects=new Array(this.totalTiles);
i=0;
while(i<this.totalTiles)
this.updateTile(i++);
var bw=(org.flixel.FlxU.ceil(org.flixel.FlxG.width/this._tileWidth)+1)*this._tileWidth;
var bh=(org.flixel.FlxU.ceil(org.flixel.FlxG.height/this._tileHeight)+1)*this._tileHeight;
this._buffer=new flash.display.BitmapData(bw,bh,true,0);
this._screenRows=Math.ceil(org.flixel.FlxG.height/this._tileHeight)+1;
if(this._screenRows>this.heightInTiles)
this._screenRows=this.heightInTiles;
this._screenCols=Math.ceil(org.flixel.FlxG.width/this._tileWidth)+1;
if(this._screenCols>this.widthInTiles)
this._screenCols=this.widthInTiles;
this._bbKey=String(TileGraphic);
this.generateBoundingTiles();
this.refreshHulls();
this._flashRect.x=0;
this._flashRect.y=0;
this._flashRect.width=this._buffer.width;
this._flashRect.height=this._buffer.height;
return this;
},
"protected function generateBoundingTiles",function()
{
this.refresh=true;
if((this._bbKey==null)||(this._bbKey.length<=0))
return;
var bbc=this.getBoundingColor();
var key=this._bbKey+":BBTILES"+bbc;
var skipGen=org.flixel.FlxG.checkBitmapCache(key);
this._bbPixels=org.flixel.FlxG.createBitmap(this._pixels.width,this._pixels.height,0,true,key);
if(!skipGen)
{
this._flashRect.width=this._pixels.width;
this._flashRect.height=this._pixels.height;
this._flashPoint.x=0;
this._flashPoint.y=0;
this._bbPixels.copyPixels(this._pixels,this._flashRect,this._flashPoint);
this._flashRect.width=this._tileWidth;
this._flashRect.height=this._tileHeight;
var ov=this._solid;
this._solid=false;
bbc=this.getBoundingColor();
key="BBTILESTAMP"+this._tileWidth+"X"+this._tileHeight+bbc;
skipGen=org.flixel.FlxG.checkBitmapCache(key);
var stamp1=org.flixel.FlxG.createBitmap(this._tileWidth,this._tileHeight,0,true,key);
if(!skipGen)
{
stamp1.fillRect(this._flashRect,bbc);
this._flashRect.x=this._flashRect.y=1;
this._flashRect.width=this._flashRect.width-2;
this._flashRect.height=this._flashRect.height-2;
stamp1.fillRect(this._flashRect,0);
this._flashRect.x=this._flashRect.y=0;
this._flashRect.width=this._tileWidth;
this._flashRect.height=this._tileHeight;
}
this._solid=ov;
bbc=this.getBoundingColor();
key="BBTILESTAMP"+this._tileWidth+"X"+this._tileHeight+bbc;
skipGen=org.flixel.FlxG.checkBitmapCache(key);
var stamp2=org.flixel.FlxG.createBitmap(this._tileWidth,this._tileHeight,0,true,key);
if(!skipGen)
{
stamp2.fillRect(this._flashRect,bbc);
this._flashRect.x=this._flashRect.y=1;
this._flashRect.width=this._flashRect.width-2;
this._flashRect.height=this._flashRect.height-2;
stamp2.fillRect(this._flashRect,0);
this._flashRect.x=this._flashRect.y=0;
this._flashRect.width=this._tileWidth;
this._flashRect.height=this._tileHeight;
}
var r=0;
var c;
var i=0;
while(r<this._bbPixels.height)
{
c=0;
while(c<this._bbPixels.width)
{
this._flashPoint.x=c;
this._flashPoint.y=r;
if(i++<this.collideIndex)
this._bbPixels.copyPixels(stamp1,this._flashRect,this._flashPoint,null,null,true);
else
this._bbPixels.copyPixels(stamp2,this._flashRect,this._flashPoint,null,null,true);
c+=this._tileWidth;
}
r+=this._tileHeight;
}
this._flashRect.x=0;
this._flashRect.y=0;
this._flashRect.width=this._buffer.width;
this._flashRect.height=this._buffer.height;
}
},
"protected function renderTilemap",function()
{
this._buffer.fillRect(this._flashRect,0);
var tileBitmap;
if(org.flixel.FlxG.showBounds)
{
tileBitmap=this._bbPixels;
this._boundsVisible=true;
}
else
{
tileBitmap=this._pixels;
this._boundsVisible=false;
}
this.getScreenXY(this._point);
this._flashPoint.x=this._point.x;
this._flashPoint.y=this._point.y;
var tx=Math.floor(-this._flashPoint.x/this._tileWidth);
var ty=Math.floor(-this._flashPoint.y/this._tileHeight);
if(tx<0)tx=0;
if(tx>this.widthInTiles-this._screenCols)tx=this.widthInTiles-this._screenCols;
if(ty<0)ty=0;
if(ty>this.heightInTiles-this._screenRows)ty=this.heightInTiles-this._screenRows;
var ri=ty*this.widthInTiles+tx;
this._flashPoint.y=0;
var r=0;
var c;
var cri;
while(r<this._screenRows)
{
cri=ri;
c=0;
this._flashPoint.x=0;
while(c<this._screenCols)
{
this._flashRect=as(this._rects[cri++],flash.geom.Rectangle);
if(this._flashRect!=null)
this._buffer.copyPixels(tileBitmap,this._flashRect,this._flashPoint,null,null,true);
this._flashPoint.x+=this._tileWidth;
c++;
}
ri+=this.widthInTiles;
this._flashPoint.y+=this._tileHeight;
r++;
}
this._flashRect=this._flashRect2;
this._bufferLoc.x=tx*this._tileWidth;
this._bufferLoc.y=ty*this._tileHeight;
},
"override public function update",function()
{
this.update$4();
this.getScreenXY(this._point);
this._point.x+=this._bufferLoc.x;
this._point.y+=this._bufferLoc.y;
if((this._point.x>0)||(this._point.y>0)||(this._point.x+this._buffer.width<org.flixel.FlxG.width)||(this._point.y+this._buffer.height<org.flixel.FlxG.height))
this.refresh=true;
},
"override public function render",function()
{
if(org.flixel.FlxG.showBounds!=this._boundsVisible)
this.refresh=true;
if(this.refresh)
{
this.renderTilemap();
this.refresh=false;
}
this.getScreenXY(this._point);
this._flashPoint.x=this._point.x+this._bufferLoc.x;
this._flashPoint.y=this._point.y+this._bufferLoc.y;
org.flixel.FlxG.buffer.copyPixels(this._buffer,this._flashRect,this._flashPoint,null,null,true);
},
"override public function set solid",function(Solid)
{
var os=this._solid;
this._solid=Solid;
if(os!=this._solid)
this.generateBoundingTiles();
},
"override public function set fixed",function(Fixed)
{
var of=this._fixed;
this._fixed=Fixed;
if(of!=this._fixed)
this.generateBoundingTiles();
},
"override public function overlaps",function(Core)
{
var d;
var dd;
var blocks=new Array();
var ix=Math.floor((Core.x-this.x)/this._tileWidth);
var iy=Math.floor((Core.y-this.y)/this._tileHeight);
var iw=Math.ceil(Core.width/this._tileWidth)+1;
var ih=Math.ceil(Core.height/this._tileHeight)+1;
var r=0;
var c;
while(r<ih)
{
if(r>=this.heightInTiles)break;
d=(iy+r)*this.widthInTiles+ix;
c=0;
while(c<iw)
{
if(c>=this.widthInTiles)break;
dd=as(this._data[d+c],uint);
if(dd>=this.collideIndex)
blocks.push({x:this.x+(ix+c)*this._tileWidth,y:this.y+(iy+r)*this._tileHeight,data:dd});
c++;
}
r++;
}
var bl=blocks.length;
var hx=false;
var i=0;
while(i<bl)
{
this._block.x=blocks[i].x;
this._block.y=blocks[i++].y;
if(this._block.overlaps(Core))
return true;
}
return false;
},
"override public function overlapsPoint",function(X,Y,PerPixel)
{if(arguments.length<3){PerPixel=false;}
return this.getTile($$uint((X-this.x)/this._tileWidth),$$uint((Y-this.y)/this._tileHeight))>=this.collideIndex;
},
"override public function refreshHulls",function()
{
this.colHullX.x=0;
this.colHullX.y=0;
this.colHullX.width=this._tileWidth;
this.colHullX.height=this._tileHeight;
this.colHullY.x=0;
this.colHullY.y=0;
this.colHullY.width=this._tileWidth;
this.colHullY.height=this._tileHeight;
},
"override public function preCollide",function(Object)
{
this.colHullX.x=0;
this.colHullX.y=0;
this.colHullY.x=0;
this.colHullY.y=0;
var r;
var c;
var rs;
var col=0;
var ix=org.flixel.FlxU.floor((Object.x-this.x)/this._tileWidth);
var iy=org.flixel.FlxU.floor((Object.y-this.y)/this._tileHeight);
var iw=ix+org.flixel.FlxU.ceil(Object.width/this._tileWidth)+1;
var ih=iy+org.flixel.FlxU.ceil(Object.height/this._tileHeight)+1;
if(ix<0)
ix=0;
if(iy<0)
iy=0;
if(iw>this.widthInTiles)
iw=this.widthInTiles;
if(ih>this.heightInTiles)
ih=this.heightInTiles;
rs=iy*this.widthInTiles;
r=iy;
while(r<ih)
{
c=ix;
while(c<iw)
{
if((as(this._data[rs+c],uint))>=this.collideIndex)
this.colOffsets[col++]=new org.flixel.FlxPoint(this.x+c*this._tileWidth,this.y+r*this._tileHeight);
c++;
}
rs+=this.widthInTiles;
r++;
}
if(this.colOffsets.length!=col)
this.colOffsets.length=col;
},
"public function getTile",function(X,Y)
{
return this.getTileByIndex(Y*this.widthInTiles+X);
},
"public function getTileByIndex",function(Index)
{
return as(this._data[Index],uint);
},
"public function setTile",function(X,Y,Tile,UpdateGraphics)
{if(arguments.length<4){UpdateGraphics=true;}
if((X>=this.widthInTiles)||(Y>=this.heightInTiles))
return false;
return this.setTileByIndex(Y*this.widthInTiles+X,Tile,UpdateGraphics);
},
"public function setTileByIndex",function(Index,Tile,UpdateGraphics)
{if(arguments.length<3){UpdateGraphics=true;}
if(Index>=this._data.length)
return false;
var ok=true;
this._data[Index]=Tile;
if(!UpdateGraphics)
return ok;
this.refresh=true;
if(this.auto==org.flixel.FlxTilemap.OFF)
{
this.updateTile(Index);
return ok;
}
var i;
var r=$$int(Index/this.widthInTiles)-1;
var rl=r+3;
var c=Index%this.widthInTiles-1;
var cl=c+3;
while(r<rl)
{
c=cl-3;
while(c<cl)
{
if((r>=0)&&(r<this.heightInTiles)&&(c>=0)&&(c<this.widthInTiles))
{
i=r*this.widthInTiles+c;
this.autoTile(i);
this.updateTile(i);
}
c++;
}
r++;
}
return ok;
},
"public function setCallback",function(Tile,Callback,Range)
{if(arguments.length<3){Range=1;}
org.flixel.FlxG.log("WARNING: FlxTilemap.setCallback()\nhas been temporarily deprecated.");
},
"public function follow",function(Border)
{if(arguments.length<1){Border=0;}
org.flixel.FlxG.followBounds(this.x+Border*this._tileWidth,this.y+Border*this._tileHeight,this.width-Border*this._tileWidth,this.height-Border*this._tileHeight);
},
"public function ray",function(StartX,StartY,EndX,EndY,Result,Resolution)
{if(arguments.length<6){Resolution=1;}
var step=this._tileWidth;
if(this._tileHeight<this._tileWidth)
step=this._tileHeight;
step/=Resolution;
var dx=EndX-StartX;
var dy=EndY-StartY;
var distance=Math.sqrt(dx*dx+dy*dy);
var steps=Math.ceil(distance/step);
var stepX=dx/steps;
var stepY=dy/steps;
var curX=StartX-stepX;
var curY=StartY-stepY;
var tx;
var ty;
var i=0;
while(i<steps)
{
curX+=stepX;
curY+=stepY;
if((curX<0)||(curX>this.width)||(curY<0)||(curY>this.height))
{
i++;
continue;
}
tx=curX/this._tileWidth;
ty=curY/this._tileHeight;
if((as(this._data[ty*this.widthInTiles+tx],uint))>=this.collideIndex)
{
tx*=this._tileWidth;
ty*=this._tileHeight;
var rx=0;
var ry=0;
var q;
var lx=curX-stepX;
var ly=curY-stepY;
q=tx;
if(dx<0)
q+=this._tileWidth;
rx=q;
ry=ly+stepY*((q-lx)/stepX);
if((ry>ty)&&(ry<ty+this._tileHeight))
{
if(Result==null)
Result=new org.flixel.FlxPoint();
Result.x=rx;
Result.y=ry;
return true;
}
q=ty;
if(dy<0)
q+=this._tileHeight;
rx=lx+stepX*((q-ly)/stepY);
ry=q;
if((rx>tx)&&(rx<tx+this._tileWidth))
{
if(Result==null)
Result=new org.flixel.FlxPoint();
Result.x=rx;
Result.y=ry;
return true;
}
return false;
}
i++;
}
return false;
},
"static public function arrayToCSV",function(Data,Width)
{
var r=0;
var c;
var csv;
var Height=Data.length/Width;
while(r<Height)
{
c=0;
while(c<Width)
{
if(c==0)
{
if(r==0)
csv+=Data[0];
else
csv+="\n"+Data[r*Width];
}
else
csv+=", "+Data[r*Width+c];
c++;
}
r++;
}
return csv;
},
"static public function bitmapToCSV",function(bitmapData,Invert,Scale)
{if(arguments.length<3){if(arguments.length<2){Invert=false;}Scale=1;}
if(Scale>1)
{
var bd=bitmapData;
bitmapData=new flash.display.BitmapData(bitmapData.width*Scale,bitmapData.height*Scale);
var mtx=new flash.geom.Matrix();
mtx.scale(Scale,Scale);
bitmapData.draw(bd,mtx);
}
var r=0;
var c;
var p;
var csv;
var w=bitmapData.width;
var h=bitmapData.height;
while(r<h)
{
c=0;
while(c<w)
{
p=bitmapData.getPixel(c,r);
if((Invert&&(p>0))||(!Invert&&(p==0)))
p=1;
else
p=0;
if(c==0)
{
if(r==0)
csv+=p;
else
csv+="\n"+p;
}
else
csv+=", "+p;
c++;
}
r++;
}
return csv;
},
"static public function imageToCSV",function(ImageFile,Invert,Scale)
{if(arguments.length<3){if(arguments.length<2){Invert=false;}Scale=1;}
return org.flixel.FlxTilemap.bitmapToCSV((new ImageFile).bitmapData,Invert,Scale);
},
"protected function autoTile",function(Index)
{
if(this._data[Index]==0)return;
this._data[Index]=0;
if((Index-this.widthInTiles<0)||(this._data[Index-this.widthInTiles]>0))
this._data[Index]+=1;
if((Index%this.widthInTiles>=this.widthInTiles-1)||(this._data[Index+1]>0))
this._data[Index]+=2;
if((Index+this.widthInTiles>=this.totalTiles)||(this._data[Index+this.widthInTiles]>0))
this._data[Index]+=4;
if((Index%this.widthInTiles<=0)||(this._data[Index-1]>0))
this._data[Index]+=8;
if((this.auto==org.flixel.FlxTilemap.ALT)&&(this._data[Index]==15))
{
if((Index%this.widthInTiles>0)&&(Index+this.widthInTiles<this.totalTiles)&&(this._data[Index+this.widthInTiles-1]<=0))
this._data[Index]=1;
if((Index%this.widthInTiles>0)&&(Index-this.widthInTiles>=0)&&(this._data[Index-this.widthInTiles-1]<=0))
this._data[Index]=2;
if((Index%this.widthInTiles<this.widthInTiles-1)&&(Index-this.widthInTiles>=0)&&(this._data[Index-this.widthInTiles+1]<=0))
this._data[Index]=4;
if((Index%this.widthInTiles<this.widthInTiles-1)&&(Index+this.widthInTiles<this.totalTiles)&&(this._data[Index+this.widthInTiles+1]<=0))
this._data[Index]=8;
}
this._data[Index]+=1;
},
"protected function updateTile",function(Index)
{
if(this._data[Index]<this.drawIndex)
{
this._rects[Index]=null;
return;
}
var rx=(this._data[Index]-this.startingIndex)*this._tileWidth;
var ry=0;
if(rx>=this._pixels.width)
{
ry=$$uint(rx/this._pixels.width)*this._tileHeight;
rx%=this._pixels.width;
}
this._rects[Index]=(new flash.geom.Rectangle(rx,ry,this._tileWidth,this._tileHeight));
},
];},["arrayToCSV","bitmapToCSV","imageToCSV"],["org.flixel.FlxObject","resource:org/flixel/data/autotiles.png","resource:org/flixel/data/autotiles_alt.png","org.flixel.FlxPoint","flash.geom.Rectangle","Array","uint","org.flixel.FlxG","org.flixel.FlxU","flash.display.BitmapData","Math","String","int","flash.geom.Matrix"], "0.8.0", "0.8.1"
);
// class org.flixel.FlxU
joo.classLoader.prepare("package org.flixel",
"public class FlxU",1,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Number,Math,org.flixel.FlxG,org.flixel.FlxQuadTree);},

"static internal var",{roundingError:0.0000001},
"static public var",{quadTree:null},
"static public function openURL",function(URL)
{
flash.net.navigateToURL(new flash.net.URLRequest(URL),"_blank");
},
"static public function abs",function(N)
{
return(N>0)?N:-N;
},
"static public function floor",function(N)
{
var n=$$int(N);
return(N>0)?(n):((n!=N)?(n-1):(n));
},
"static public function ceil",function(N)
{
var n=$$int(N);
return(N>0)?((n!=N)?(n+1):(n)):(n);
},
"static public function min",function(N1,N2)
{
return(N1<=N2)?N1:N2;
},
"static public function max",function(N1,N2)
{
return(N1>=N2)?N1:N2;
},
"static public function random",function(Seed)
{if(arguments.length<1){Seed=NaN;}
if(isNaN(Seed))
return Math.random();
else
{
if(Seed==0)
Seed=Number.MIN_VALUE;
if(Seed>=1)
{
if((Seed%1)==0)
Seed/=Math.PI;
Seed%=1;
}
else if(Seed<0)
Seed=(Seed%1)+1;
return((69621*$$int(Seed*0x7FFFFFFF))%0x7FFFFFFF)/0x7FFFFFFF;
}
},
"static public function startProfile",function()
{
return flash.utils.getTimer();
},
"static public function endProfile",function(Start,Name,Log)
{if(arguments.length<3){if(arguments.length<2){Name="Profiler";}Log=true;}
var t=flash.utils.getTimer();
if(Log)
org.flixel.FlxG.log(Name+": "+((t-Start)/1000)+"s");
return t;
},
"static public function rotatePoint",function(X,Y,PivotX,PivotY,Angle,P)
{if(arguments.length<6){P=null;}
var sin=0;
var cos=0;
var radians=Angle*-0.017453293;
while(radians<-3.14159265)
radians+=6.28318531;
while(radians>3.14159265)
radians=radians-6.28318531;
if(radians<0)
{
sin=1.27323954*radians+.405284735*radians*radians;
if(sin<0)
sin=.225*(sin*-sin-sin)+sin;
else
sin=.225*(sin*sin-sin)+sin;
}
else
{
sin=1.27323954*radians-0.405284735*radians*radians;
if(sin<0)
sin=.225*(sin*-sin-sin)+sin;
else
sin=.225*(sin*sin-sin)+sin;
}
radians+=1.57079632;
if(radians>3.14159265)
radians=radians-6.28318531;
if(radians<0)
{
cos=1.27323954*radians+0.405284735*radians*radians;
if(cos<0)
cos=.225*(cos*-cos-cos)+cos;
else
cos=.225*(cos*cos-cos)+cos;
}
else
{
cos=1.27323954*radians-0.405284735*radians*radians;
if(cos<0)
cos=.225*(cos*-cos-cos)+cos;
else
cos=.225*(cos*cos-cos)+cos;
}
var dx=X-PivotX;
var dy=PivotY-Y;
if(P==null)P=new org.flixel.FlxPoint();
P.x=PivotX+cos*dx-sin*dy;
P.y=PivotY-sin*dx-cos*dy;
return P;
},
"static public function getAngle",function(X,Y)
{
var c1=3.14159265/4;
var c2=3*c1;
var ay=(Y<0)?-Y:Y;
var angle=0;
if(X>=0)
angle=c1-c1*((X-ay)/(X+ay));
else
angle=c2-c1*((X+ay)/(ay-X));
return((Y<0)?-angle:angle)*57.2957796;
},
"static public function getColor",function(Red,Green,Blue,Alpha)
{if(arguments.length<4){Alpha=1.0;}
return(((Alpha>1)?Alpha:(Alpha*255))&0xFF)<<24|(Red&0xFF)<<16|(Green&0xFF)<<8|(Blue&0xFF);
},
"static public function getColorHSB",function(Hue,Saturation,Brightness,Alpha)
{if(arguments.length<4){Alpha=1.0;}
var red;
var green;
var blue;
if(Saturation==0.0)
{
red=Brightness;
green=Brightness;
blue=Brightness;
}
else
{
if(Hue==360)
Hue=0;
var slice=Hue/60;
var hf=Hue/60-slice;
var aa=Brightness*(1-Saturation);
var bb=Brightness*(1-Saturation*hf);
var cc=Brightness*(1-Saturation*(1.0-hf));
switch(slice)
{
case 0:red=Brightness;green=cc;blue=aa;break;
case 1:red=bb;green=Brightness;blue=aa;break;
case 2:red=aa;green=Brightness;blue=cc;break;
case 3:red=aa;green=bb;blue=Brightness;break;
case 4:red=cc;green=aa;blue=Brightness;break;
case 5:red=Brightness;green=aa;blue=bb;break;
default:red=0;green=0;blue=0;break;
}
}
return(((Alpha>1)?Alpha:(Alpha*255))&0xFF)<<24|$$uint(red*255)<<16|$$uint(green*255)<<8|$$uint(blue*255);
},
"static public function getRGBA",function(Color,Results)
{if(arguments.length<2){Results=null;}
if(Results==null)
Results=new Array();
Results[0]=(Color>>16)&0xFF;
Results[1]=(Color>>8)&0xFF;
Results[2]=Color&0xFF;
Results[3]=Number((Color>>24)&0xFF)/255;
return Results;
},
"static public function getHSB",function(Color,Results)
{if(arguments.length<2){Results=null;}
if(Results==null)
Results=new Array();
var red=Number((Color>>16)&0xFF)/255;
var green=Number((Color>>8)&0xFF)/255;
var blue=Number((Color)&0xFF)/255;
var m=(red>green)?red:green;
var dmax=(m>blue)?m:blue;
m=(red>green)?green:red;
var dmin=(m>blue)?blue:m;
var range=dmax-dmin;
Results[2]=dmax;
Results[1]=0;
Results[0]=0;
if(dmax!=0)
Results[1]=range/dmax;
if(Results[1]!=0)
{
if(red==dmax)
Results[0]=(green-blue)/range;
else if(green==dmax)
Results[0]=2+(blue-red)/range;
else if(blue==dmax)
Results[0]=4+(red-green)/range;
Results[0]*=60;
if(Results[0]<0)
Results[0]+=360;
}
Results[3]=Number((Color>>24)&0xFF)/255;
return Results;
},
"static public function getClassName",function(Obj,Simple)
{if(arguments.length<2){Simple=false;}
var s=flash.utils.getQualifiedClassName(Obj);
s=s.replace("::",".");
if(Simple)
s=s.substr(s.lastIndexOf(".")+1);
return s;
},
"static public function getClass",function(Name)
{
return as(flash.utils.getDefinitionByName(Name),Class);
},
"static public function computeVelocity",function(Velocity,Acceleration,Drag,Max)
{if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){Acceleration=0;}Drag=0;}Max=10000;}
if(Acceleration!=0)
Velocity+=Acceleration*org.flixel.FlxG.elapsed;
else if(Drag!=0)
{
var d=Drag*org.flixel.FlxG.elapsed;
if(Velocity-d>0)
Velocity=Velocity-d;
else if(Velocity+d<0)
Velocity+=d;
else
Velocity=0;
}
if((Velocity!=0)&&(Max!=10000))
{
if(Velocity>Max)
Velocity=Max;
else if(Velocity<-Max)
Velocity=-Max;
}
return Velocity;
},
"static public function setWorldBounds",function(X,Y,Width,Height,Divisions)
{if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){X=0;}Y=0;}Width=0;}Height=0;}Divisions=3;}
if(org.flixel.FlxQuadTree.bounds==null)
org.flixel.FlxQuadTree.bounds=new org.flixel.FlxRect();
org.flixel.FlxQuadTree.bounds.x=X;
org.flixel.FlxQuadTree.bounds.y=Y;
if(Width>0)
org.flixel.FlxQuadTree.bounds.width=Width;
if(Height>0)
org.flixel.FlxQuadTree.bounds.height=Height;
if(Divisions>0)
org.flixel.FlxQuadTree.divisions=Divisions;
},
"static public function overlap",function(Object1,Object2,Callback)
{if(arguments.length<3){Callback=null;}
if((Object1==null)||!Object1.exists||
(Object2==null)||!Object2.exists)
return false;
org.flixel.FlxU.quadTree=new org.flixel.FlxQuadTree(org.flixel.FlxQuadTree.bounds.x,org.flixel.FlxQuadTree.bounds.y,org.flixel.FlxQuadTree.bounds.width,org.flixel.FlxQuadTree.bounds.height);
org.flixel.FlxU.quadTree.add(Object1,org.flixel.FlxQuadTree.A_LIST);
if(Object1===Object2)
return org.flixel.FlxU.quadTree.overlap(false,Callback);
org.flixel.FlxU.quadTree.add(Object2,org.flixel.FlxQuadTree.B_LIST);
return org.flixel.FlxU.quadTree.overlap(true,Callback);
},
"static public function collide",function(Object1,Object2)
{
if((Object1==null)||!Object1.exists||
(Object2==null)||!Object2.exists)
return false;
org.flixel.FlxU.quadTree=new org.flixel.FlxQuadTree(org.flixel.FlxQuadTree.bounds.x,org.flixel.FlxQuadTree.bounds.y,org.flixel.FlxQuadTree.bounds.width,org.flixel.FlxQuadTree.bounds.height);
org.flixel.FlxU.quadTree.add(Object1,org.flixel.FlxQuadTree.A_LIST);
var match=Object1===Object2;
if(!match)
org.flixel.FlxU.quadTree.add(Object2,org.flixel.FlxQuadTree.B_LIST);
var cx=org.flixel.FlxU.quadTree.overlap(!match,org.flixel.FlxU.solveXCollision);
var cy=org.flixel.FlxU.quadTree.overlap(!match,org.flixel.FlxU.solveYCollision);
return cx||cy;
},
"static public function solveXCollision",function(Object1,Object2)
{
var o1=Object1.colVector.x;
var o2=Object2.colVector.x;
if(o1==o2)
return false;
Object1.preCollide(Object2);
Object2.preCollide(Object1);
var f1;
var f2;
var overlap;
var hit=false;
var p1hn2;
var obj1Stopped=o1==0;
var obj1MoveNeg=o1<0;
var obj1MovePos=o1>0;
var obj2Stopped=o2==0;
var obj2MoveNeg=o2<0;
var obj2MovePos=o2>0;
var i1;
var i2;
var obj1Hull=Object1.colHullX;
var obj2Hull=Object2.colHullX;
var co1=Object1.colOffsets;
var co2=Object2.colOffsets;
var l1=co1.length;
var l2=co2.length;
var ox1;
var oy1;
var ox2;
var oy2;
var r1;
var r2;
var sv1;
var sv2;
p1hn2=((obj1Stopped&&obj2MoveNeg)||(obj1MovePos&&obj2Stopped)||(obj1MovePos&&obj2MoveNeg)||
(obj1MoveNeg&&obj2MoveNeg&&(((o1>0)?o1:-o1)<((o2>0)?o2:-o2)))||
(obj1MovePos&&obj2MovePos&&(((o1>0)?o1:-o1)>((o2>0)?o2:-o2))));
if(p1hn2?(!Object1.collideRight||!Object2.collideLeft):(!Object1.collideLeft||!Object2.collideRight))
return false;
i1=0;
while(i1<l1)
{
ox1=co1[i1].x;
oy1=co1[i1].y;
obj1Hull.x+=ox1;
obj1Hull.y+=oy1;
i2=0;
while(i2<l2)
{
ox2=co2[i2].x;
oy2=co2[i2].y;
obj2Hull.x+=ox2;
obj2Hull.y+=oy2;
if((obj1Hull.x+obj1Hull.width<obj2Hull.x+org.flixel.FlxU.roundingError)||
(obj1Hull.x+org.flixel.FlxU.roundingError>obj2Hull.x+obj2Hull.width)||
(obj1Hull.y+obj1Hull.height<obj2Hull.y+org.flixel.FlxU.roundingError)||
(obj1Hull.y+org.flixel.FlxU.roundingError>obj2Hull.y+obj2Hull.height))
{
obj2Hull.x=obj2Hull.x-ox2;
obj2Hull.y=obj2Hull.y-oy2;
i2++;
continue;
}
if(p1hn2)
{
if(obj1MoveNeg)
r1=obj1Hull.x+Object1.colHullY.width;
else
r1=obj1Hull.x+obj1Hull.width;
if(obj2MoveNeg)
r2=obj2Hull.x;
else
r2=obj2Hull.x+obj2Hull.width-Object2.colHullY.width;
}
else
{
if(obj2MoveNeg)
r1=-obj2Hull.x-Object2.colHullY.width;
else
r1=-obj2Hull.x-obj2Hull.width;
if(obj1MoveNeg)
r2=-obj1Hull.x;
else
r2=-obj1Hull.x-obj1Hull.width+Object1.colHullY.width;
}
overlap=r1-r2;
f1=Object1.fixed;
f2=Object2.fixed;
if(f1&&f2)
{
f1=
f1&&((Object1.colVector.x==0)&&(o1==0));
f2=
f2&&((Object2.colVector.x==0)&&(o2==0));
}
if((overlap==0)||
((!f1&&((overlap>0)?overlap:-overlap)>obj1Hull.width*0.8))||
((!f2&&((overlap>0)?overlap:-overlap)>obj2Hull.width*0.8)))
{
obj2Hull.x=obj2Hull.x-ox2;
obj2Hull.y=obj2Hull.y-oy2;
i2++;
continue;
}
hit=true;
sv1=Object2.velocity.x;
sv2=Object1.velocity.x;
if(!f1&&f2)
{
if(Object1._group)
Object1.reset(Object1.x-overlap,Object1.y);
else
Object1.x=Object1.x-overlap;
}
else if(f1&&!f2)
{
if(Object2._group)
Object2.reset(Object2.x+overlap,Object2.y);
else
Object2.x+=overlap;
}
else if(!f1&&!f2)
{
overlap/=2;
if(Object1._group)
Object1.reset(Object1.x-overlap,Object1.y);
else
Object1.x=Object1.x-overlap;
if(Object2._group)
Object2.reset(Object2.x+overlap,Object2.y);
else
Object2.x+=overlap;
sv1*=0.5;
sv2*=0.5;
}
if(p1hn2)
{
Object1.hitRight(Object2,sv1);
Object2.hitLeft(Object1,sv2);
}
else
{
Object1.hitLeft(Object2,sv1);
Object2.hitRight(Object1,sv2);
}
if(!f1&&(overlap!=0))
{
if(p1hn2)
obj1Hull.width=obj1Hull.width-overlap;
else
{
obj1Hull.x=obj1Hull.x-overlap;
obj1Hull.width+=overlap;
}
Object1.colHullY.x=Object1.colHullY.x-overlap;
}
if(!f2&&(overlap!=0))
{
if(p1hn2)
{
obj2Hull.x+=overlap;
obj2Hull.width=obj2Hull.width-overlap;
}
else
obj2Hull.width+=overlap;
Object2.colHullY.x+=overlap;
}
obj2Hull.x=obj2Hull.x-ox2;
obj2Hull.y=obj2Hull.y-oy2;
i2++;
}
obj1Hull.x=obj1Hull.x-ox1;
obj1Hull.y=obj1Hull.y-oy1;
i1++;
}
return hit;
},
"static public function solveYCollision",function(Object1,Object2)
{
var o1=Object1.colVector.y;
var o2=Object2.colVector.y;
if(o1==o2)
return false;
Object1.preCollide(Object2);
Object2.preCollide(Object1);
var f1;
var f2;
var overlap;
var hit=false;
var p1hn2;
var obj1Stopped=o1==0;
var obj1MoveNeg=o1<0;
var obj1MovePos=o1>0;
var obj2Stopped=o2==0;
var obj2MoveNeg=o2<0;
var obj2MovePos=o2>0;
var i1;
var i2;
var obj1Hull=Object1.colHullY;
var obj2Hull=Object2.colHullY;
var co1=Object1.colOffsets;
var co2=Object2.colOffsets;
var l1=co1.length;
var l2=co2.length;
var ox1;
var oy1;
var ox2;
var oy2;
var r1;
var r2;
var sv1;
var sv2;
p1hn2=((obj1Stopped&&obj2MoveNeg)||(obj1MovePos&&obj2Stopped)||(obj1MovePos&&obj2MoveNeg)||
(obj1MoveNeg&&obj2MoveNeg&&(((o1>0)?o1:-o1)<((o2>0)?o2:-o2)))||
(obj1MovePos&&obj2MovePos&&(((o1>0)?o1:-o1)>((o2>0)?o2:-o2))));
if(p1hn2?(!Object1.collideBottom||!Object2.collideTop):(!Object1.collideTop||!Object2.collideBottom))
return false;
i1=0;
while(i1<l1)
{
ox1=co1[i1].x;
oy1=co1[i1].y;
obj1Hull.x+=ox1;
obj1Hull.y+=oy1;
i2=0;
while(i2<l2)
{
ox2=co2[i2].x;
oy2=co2[i2].y;
obj2Hull.x+=ox2;
obj2Hull.y+=oy2;
if((obj1Hull.x+obj1Hull.width<obj2Hull.x+org.flixel.FlxU.roundingError)||
(obj1Hull.x+org.flixel.FlxU.roundingError>obj2Hull.x+obj2Hull.width)||
(obj1Hull.y+obj1Hull.height<obj2Hull.y+org.flixel.FlxU.roundingError)||
(obj1Hull.y+org.flixel.FlxU.roundingError>obj2Hull.y+obj2Hull.height))
{
obj2Hull.x=obj2Hull.x-ox2;
obj2Hull.y=obj2Hull.y-oy2;
i2++;
continue;
}
if(p1hn2)
{
if(obj1MoveNeg)
r1=obj1Hull.y+Object1.colHullX.height;
else
r1=obj1Hull.y+obj1Hull.height;
if(obj2MoveNeg)
r2=obj2Hull.y;
else
r2=obj2Hull.y+obj2Hull.height-Object2.colHullX.height;
}
else
{
if(obj2MoveNeg)
r1=-obj2Hull.y-Object2.colHullX.height;
else
r1=-obj2Hull.y-obj2Hull.height;
if(obj1MoveNeg)
r2=-obj1Hull.y;
else
r2=-obj1Hull.y-obj1Hull.height+Object1.colHullX.height;
}
overlap=r1-r2;
f1=Object1.fixed;
f2=Object2.fixed;
if(f1&&f2)
{
f1=
f1&&((Object1.colVector.x==0)&&(o1==0));
f2=
f2&&((Object2.colVector.x==0)&&(o2==0));
}
if((overlap==0)||
((!f1&&((overlap>0)?overlap:-overlap)>obj1Hull.height*0.8))||
((!f2&&((overlap>0)?overlap:-overlap)>obj2Hull.height*0.8)))
{
obj2Hull.x=obj2Hull.x-ox2;
obj2Hull.y=obj2Hull.y-oy2;
i2++;
continue;
}
hit=true;
sv1=Object2.velocity.y;
sv2=Object1.velocity.y;
if(!f1&&f2)
{
if(Object1._group)
Object1.reset(Object1.x,Object1.y-overlap);
else
Object1.y=Object1.y-overlap;
}
else if(f1&&!f2)
{
if(Object2._group)
Object2.reset(Object2.x,Object2.y+overlap);
else
Object2.y+=overlap;
}
else if(!f1&&!f2)
{
overlap/=2;
if(Object1._group)
Object1.reset(Object1.x,Object1.y-overlap);
else
Object1.y=Object1.y-overlap;
if(Object2._group)
Object2.reset(Object2.x,Object2.y+overlap);
else
Object2.y+=overlap;
sv1*=0.5;
sv2*=0.5;
}
if(p1hn2)
{
Object1.hitBottom(Object2,sv1);
Object2.hitTop(Object1,sv2);
}
else
{
Object1.hitTop(Object2,sv1);
Object2.hitBottom(Object1,sv2);
}
if(!f1&&(overlap!=0))
{
if(p1hn2)
{
obj1Hull.y=obj1Hull.y-overlap;
if(f2&&Object2.moves)
{
sv1=Object2.colVector.x;
Object1.x+=sv1;
obj1Hull.x+=sv1;
Object1.colHullX.x+=sv1;
}
}
else
{
obj1Hull.y=obj1Hull.y-overlap;
obj1Hull.height+=overlap;
}
}
if(!f2&&(overlap!=0))
{
if(p1hn2)
{
obj2Hull.y+=overlap;
obj2Hull.height=obj2Hull.height-overlap;
}
else
{
obj2Hull.height+=overlap;
if(f1&&Object1.moves)
{
sv2=Object1.colVector.x;
Object2.x+=sv2;
obj2Hull.x+=sv2;
Object2.colHullX.x+=sv2;
}
}
}
obj2Hull.x=obj2Hull.x-ox2;
obj2Hull.y=obj2Hull.y-oy2;
i2++;
}
obj1Hull.x=obj1Hull.x-ox1;
obj1Hull.y=obj1Hull.y-oy1;
i1++;
}
return hit;
},
];},["openURL","abs","floor","ceil","min","max","random","startProfile","endProfile","rotatePoint","getAngle","getColor","getColorHSB","getRGBA","getHSB","getClassName","getClass","computeVelocity","setWorldBounds","overlap","collide","solveXCollision","solveYCollision"],["flash.net.URLRequest","int","Math","Number","org.flixel.FlxG","org.flixel.FlxPoint","uint","Array","Class","org.flixel.FlxQuadTree","org.flixel.FlxRect"], "0.8.0", "0.8.1"
);
