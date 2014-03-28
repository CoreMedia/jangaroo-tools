// class CanvasTest
joo.classLoader.prepare("package",
"public class CanvasTest",1,function($$private){var as=joo.as;return[
"public static function main",function(id){
var canvasElement=as(window.document.getElementById(id),js.HTMLCanvasElement);
canvasElement.width=100;
canvasElement.height=100;
var context=as(canvasElement.getContext("2d"),js.CanvasRenderingContext2D);
context.fillRect(10,10,90,90);
var imageData=context.getImageData(0,0,100,100);
canvasElement.width=200;
canvasElement.height=200;
context.putImageData(imageData,0,0);
},
];},["main"],["js.HTMLCanvasElement","js.CanvasRenderingContext2D"], "0.8.0", "0.8.1"
);
// class General.FpsCounter
joo.classLoader.prepare(
"package General",
"public class FpsCounter extends flash.display.Sprite",6,function($$private){;return[function(){joo.classLoader.init(flash.system.System);},
"public function FpsCounter",function(){this.super$6();
this.textBox$6=new flash.text.TextField();
this.textBox$6.text="...";
this.textBox$6.textColor=0xaa1144;
this.textBox$6.selectable=false;
this.textBox2$6=new flash.text.TextField();
this.textBox2$6.text="...";
this.textBox2$6.width=150;
this.textBox2$6.textColor=0xaa1144;
this.textBox2$6.selectable=false;
this.textBox2$6.y=15;
this.textBox3$6=new flash.text.TextField();
this.textBox3$6.text="...";
this.textBox3$6.textColor=0xaa1144;
this.textBox3$6.selectable=false;
this.textBox3$6.y=30;
this.oldT$6=flash.utils.getTimer();
this.addChild(this.textBox$6);
this.addChild(this.textBox2$6);
this.addChild(this.textBox3$6);
},
"public function update",function(){
var newT=flash.utils.getTimer();
var f1=newT-this.oldT$6;
this.mfpsCount$6+=f1;
if(this.avgCount$6<1){
this.textBox$6.text=String(Math.round(1000/(this.mfpsCount$6/30))+" fps average");
this.avgCount$6=30;
this.mfpsCount$6=0;
}
this.avgCount$6--;
this.oldT$6=flash.utils.getTimer();
this.textBox3$6.text=Math.round(flash.system.System.totalMemory/(1024*1024))+" MB used";
},
"public function updatePhys",function(oldT2){
var newT=flash.utils.getTimer();
var f1=newT-oldT2;
this.mfpsCount2$6+=f1;
if(this.avgCount2$6<1){
this.textBox2$6.text=String("Physics step: "+Math.round(this.mfpsCount2$6/30)+" ms ("+Math.round(1000/(this.mfpsCount2$6/30))+" fps)");
this.avgCount2$6=30;
this.mfpsCount2$6=0;
}
this.avgCount2$6--;
},
"public function updateEnd",function(){
},
"private var",{textBox:null},
"private var",{textBox2:null},
"private var",{textBox3:null},
"private var",{mfpsCount:0},
"private var",{mfpsCount2:0},
"private var",{avgCount:30},
"private var",{avgCount2:30},
"private var",{oldT:0},
];},[],["flash.display.Sprite","flash.text.TextField","String","Math","flash.system.System"], "0.8.0", "0.8.1"
);
// class General.FRateLimiter
joo.classLoader.prepare(
"package General",
"public class FRateLimiter",1,function($$private){;return[
"static public function limitFrame",function(maxFPS){
var fTime=1000/maxFPS;
while(Math.abs($$private.newT-$$private.oldT)<fTime){
$$private.newT=flash.utils.getTimer();
}
$$private.oldT=flash.utils.getTimer();
},
"private static var",{oldT:function(){return(flash.utils.getTimer());}},
"private static var",{newT:function(){return($$private.oldT);}},
];},["limitFrame"],["Math"], "0.8.0", "0.8.1"
);
// class General.Input
joo.classLoader.prepare(
"package General",
"public class Input",1,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.events.KeyboardEvent,flash.events.MouseEvent,flash.events.Event);},
"public function Input",function(stageMc){
General.Input.m_stageMc=stageMc;
General.Input.ascii=new Array(222);
this.fillAscii$1();
$$private.keyState=new Array(222);
$$private.keyArr=new Array();
for(var i=0;i<222;i++){
$$private.keyState[i]=new int(0);
if(General.Input.ascii[i]!=undefined){
$$private.keyArr.push(i);
}
}
$$private.bufferSize=5;
$$private.keyBuffer=new Array($$private.bufferSize);
for(var j=0;j<$$private.bufferSize;j++){
$$private.keyBuffer[j]=new Array(0,0);
}
stageMc.stage.addEventListener(flash.events.KeyboardEvent.KEY_DOWN,$$bound(this,"keyPress"),false,0,true);
stageMc.stage.addEventListener(flash.events.KeyboardEvent.KEY_UP,$$bound(this,"keyRelease"),false,0,true);
stageMc.stage.addEventListener(flash.events.MouseEvent.MOUSE_DOWN,$$bound(this,"mousePress"),false,0,true);
stageMc.stage.addEventListener(flash.events.MouseEvent.CLICK,$$bound(this,"mouseRelease"),false,0,true);
stageMc.stage.addEventListener(flash.events.MouseEvent.MOUSE_MOVE,$$bound(this,"mouseMove"),false,0,true);
stageMc.stage.addEventListener(flash.events.Event.MOUSE_LEAVE,$$bound(this,"mouseLeave"),false,0,true);
General.Input.mouse.graphics.lineStyle(0.1,0,100);
General.Input.mouse.graphics.moveTo(0,0);
General.Input.mouse.graphics.lineTo(0,0.1);
},
"static public function update",function(){
for(var i=0;i<$$private.keyArr.length;i++){
if($$private.keyState[$$private.keyArr[i]]!=0){
$$private.keyState[$$private.keyArr[i]]++;
}
}
for(var j=0;j<$$private.bufferSize;j++){
$$private.keyBuffer[j][1]++;
}
General.Input.mouseReleased=false;
General.Input.mousePressed=false;
General.Input.mouseOver=false;
},
"public function mousePress",function(e){
General.Input.mousePressed=true;
General.Input.mouseDown=true;
General.Input.mouseDragX=0;
General.Input.mouseDragY=0;
},
"public function mouseRelease",function(e){
General.Input.mouseDown=false;
General.Input.mouseReleased=true;
},
"public function mouseLeave",function(e){
General.Input.mouseReleased=General.Input.mouseDown;
General.Input.mouseDown=false;
},
"public function mouseMove",function(e){
if(General.Input.mouseDown!=e.buttonDown){
General.Input.mouseDown=e.buttonDown;
General.Input.mouseReleased=!e.buttonDown;
General.Input.mousePressed=e.buttonDown;
General.Input.mouseDragX=0;
General.Input.mouseDragY=0;
}
General.Input.mouseX=e.stageX-General.Input.m_stageMc.x;
General.Input.mouseY=e.stageY-General.Input.m_stageMc.y;
General.Input.mouseOffsetX=General.Input.mouseX-General.Input.mouse.x;
General.Input.mouseOffsetY=General.Input.mouseY-General.Input.mouse.y;
if(General.Input.mouseDown){
General.Input.mouseDragX+=General.Input.mouseOffsetX;
General.Input.mouseDragY+=General.Input.mouseOffsetY;
}
General.Input.mouse.x=General.Input.mouseX;
General.Input.mouse.y=General.Input.mouseY;
},
"static public function getKeyHold",function(k){
return Math.max(0,$$private.keyState[k]);
},
"static public function isKeyDown",function(k){
return($$private.keyState[k]>0);
},
"static public function isKeyPressed",function(k){
General.Input.timeSinceLastKey=0;
return($$private.keyState[k]==1);
},
"static public function isKeyReleased",function(k){
return($$private.keyState[k]==-1);
},
"static public function isKeyInBuffer",function(k,i,t){
return($$private.keyBuffer[i][0]==k&&$$private.keyBuffer[i][1]<=t);
},
"public function keyPress",function(e){
$$private.keyState[e.keyCode]=Math.max($$private.keyState[e.keyCode],1);
General.Input.lastKey=e.keyCode;
},
"public function keyRelease",function(e){
$$private.keyState[e.keyCode]=-1;
for(var i=$$private.bufferSize-1;i>0;i--){
$$private.keyBuffer[i]=$$private.keyBuffer[i-1];
}
$$private.keyBuffer[0]=[e.keyCode,0];
},
"static public function getKeyString",function(k){
return General.Input.ascii[k];
},
"private function fillAscii",function(){
General.Input.ascii[65]="A";
General.Input.ascii[66]="B";
General.Input.ascii[67]="C";
General.Input.ascii[68]="D";
General.Input.ascii[69]="E";
General.Input.ascii[70]="F";
General.Input.ascii[71]="G";
General.Input.ascii[72]="H";
General.Input.ascii[73]="I";
General.Input.ascii[74]="J";
General.Input.ascii[75]="K";
General.Input.ascii[76]="L";
General.Input.ascii[77]="M";
General.Input.ascii[78]="N";
General.Input.ascii[79]="O";
General.Input.ascii[80]="P";
General.Input.ascii[81]="Q";
General.Input.ascii[82]="R";
General.Input.ascii[83]="S";
General.Input.ascii[84]="T";
General.Input.ascii[85]="U";
General.Input.ascii[86]="V";
General.Input.ascii[87]="W";
General.Input.ascii[88]="X";
General.Input.ascii[89]="Y";
General.Input.ascii[90]="Z";
General.Input.ascii[48]="0";
General.Input.ascii[49]="1";
General.Input.ascii[50]="2";
General.Input.ascii[51]="3";
General.Input.ascii[52]="4";
General.Input.ascii[53]="5";
General.Input.ascii[54]="6";
General.Input.ascii[55]="7";
General.Input.ascii[56]="8";
General.Input.ascii[57]="9";
General.Input.ascii[32]="Spacebar";
General.Input.ascii[17]="Ctrl";
General.Input.ascii[16]="Shift";
General.Input.ascii[192]="~";
General.Input.ascii[38]="up";
General.Input.ascii[40]="down";
General.Input.ascii[37]="left";
General.Input.ascii[39]="right";
General.Input.ascii[96]="Numpad 0";
General.Input.ascii[97]="Numpad 1";
General.Input.ascii[98]="Numpad 2";
General.Input.ascii[99]="Numpad 3";
General.Input.ascii[100]="Numpad 4";
General.Input.ascii[101]="Numpad 5";
General.Input.ascii[102]="Numpad 6";
General.Input.ascii[103]="Numpad 7";
General.Input.ascii[104]="Numpad 8";
General.Input.ascii[105]="Numpad 9";
General.Input.ascii[111]="Numpad /";
General.Input.ascii[106]="Numpad *";
General.Input.ascii[109]="Numpad -";
General.Input.ascii[107]="Numpad +";
General.Input.ascii[110]="Numpad .";
General.Input.ascii[45]="Insert";
General.Input.ascii[46]="Delete";
General.Input.ascii[33]="Page Up";
General.Input.ascii[34]="Page Down";
General.Input.ascii[35]="End";
General.Input.ascii[36]="Home";
General.Input.ascii[112]="F1";
General.Input.ascii[113]="F2";
General.Input.ascii[114]="F3";
General.Input.ascii[115]="F4";
General.Input.ascii[116]="F5";
General.Input.ascii[117]="F6";
General.Input.ascii[118]="F7";
General.Input.ascii[119]="F8";
General.Input.ascii[188]=",";
General.Input.ascii[190]=".";
General.Input.ascii[186]=";";
General.Input.ascii[222]="'";
General.Input.ascii[219]="[";
General.Input.ascii[221]="]";
General.Input.ascii[189]="-";
General.Input.ascii[187]="+";
General.Input.ascii[220]="\\";
General.Input.ascii[191]="/";
General.Input.ascii[9]="TAB";
General.Input.ascii[8]="Backspace";
},
"static public var",{ascii:null},
"static private var",{keyState:null},
"static private var",{keyArr:null},
"static private var",{keyBuffer:null},
"static private var",{bufferSize:0},
"static public var",{lastKey:0},
"static public var",{timeSinceLastKey:0},
"static public var",{mouseDown:false},
"static public var",{mouseReleased:false},
"static public var",{mousePressed:false},
"static public var",{mouseOver:false},
"static public var",{mouseX:0},
"static public var",{mouseY:0},
"static public var",{mouseOffsetX:0},
"static public var",{mouseOffsetY:0},
"static public var",{mouseDragX:0},
"static public var",{mouseDragY:0},
"static public var",{mouse:function(){return(new flash.display.Sprite());}},
"static public var",{m_stageMc:null},
];},["update","getKeyHold","isKeyDown","isKeyPressed","isKeyReleased","isKeyInBuffer","getKeyString"],["Array","int","flash.events.KeyboardEvent","flash.events.MouseEvent","flash.events.Event","Math","flash.display.Sprite"], "0.8.0", "0.8.1"
);
// class Main
joo.classLoader.prepare(
"package",
{SWF:{width:'640',height:'360',backgroundColor:'#292C2C',frameRate:'30'}},
"public class Main extends flash.display.MovieClip",7,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.text.TextFormatAlign,TestBed.TestBuoyancy,TestBed.TestRagdoll,TestBed.TestCompound,TestBed.TestStack,TestBed.TestBridge,TestBed.TestCCD,TestBed.TestTheoJansen,TestBed.TestBreakable,flash.events.Event,TestBed.TestSensor,TestBed.TestOneSidedPlatform,TestBed.TestRaycast,TestBed.TestCrankGearsPulley);},
"public function Main",function(){this.super$7();
this.addEventListener(flash.events.Event.ENTER_FRAME,$$bound(this,"update"),false,0,true);
Main.m_fpsCounter.x=7;
Main.m_fpsCounter.y=5;
this.addChildAt(Main.m_fpsCounter,0);
Main.m_sprite=new flash.display.Sprite();
this.addChild(Main.m_sprite);
this.m_input=new General.Input(Main.m_sprite);
var instructions_text=new flash.text.TextField();
var instructions_text_format=new flash.text.TextFormat("Arial",16,0xffffff,false,false,false);
instructions_text_format.align=flash.text.TextFormatAlign.RIGHT;
instructions_text.defaultTextFormat=instructions_text_format;
instructions_text.x=140;
instructions_text.y=4.5;
instructions_text.width=495;
instructions_text.height=61;
instructions_text.text="Box2DFlashAS3 2.1a\n'Left'/'Right' arrows to go to previous/next example. \n'R' to reset.";
this.addChild(instructions_text);
Main.m_aboutText=new flash.text.TextField();
var m_aboutTextFormat=new flash.text.TextFormat("Arial",16,0x00CCFF,true,false,false);
m_aboutTextFormat.align=flash.text.TextFormatAlign.RIGHT;
Main.m_aboutText.defaultTextFormat=m_aboutTextFormat;
Main.m_aboutText.x=334;
Main.m_aboutText.y=71;
Main.m_aboutText.width=300;
Main.m_aboutText.height=30;
this.addChild(Main.m_aboutText);
instructions_text.mouseEnabled=false;
Main.m_aboutText.mouseEnabled=false;
},
"public function update",function(e){
Main.m_sprite.graphics.clear();
if(General.Input.isKeyPressed(39)){
this.m_currId++;
Main.m_currTest=null;
}
else if(General.Input.isKeyPressed(37)){
this.m_currId--;
Main.m_currTest=null;
}
else if(General.Input.isKeyPressed(82)){
Main.m_currTest=null;
}
var tests=[
TestBed.TestRagdoll,
TestBed.TestCompound,
TestBed.TestCrankGearsPulley,
TestBed.TestBridge,
TestBed.TestStack,
TestBed.TestCCD,
TestBed.TestTheoJansen,
TestBed.TestBuoyancy,
TestBed.TestOneSidedPlatform,
TestBed.TestBreakable,
TestBed.TestRaycast,
TestBed.TestSensor,
null
];
tests.length-=1;
var testCount=tests.length;
this.m_currId=(this.m_currId+testCount)%testCount;
if(!Main.m_currTest){
switch(this.m_currId){
default:
Main.m_currTest=new tests[this.m_currId]();
}
}
Main.m_currTest.Update();
General.Input.update();
Main.m_fpsCounter.update();
General.FRateLimiter.limitFrame(30);
},
"static public var",{m_fpsCounter:function(){return(new General.FpsCounter());}},
"public var",{m_currId:0},
"static public var",{m_currTest:null},
"static public var",{m_sprite:null},
"static public var",{m_aboutText:null},
"public var",{m_input:null},
];},[],["flash.display.MovieClip","flash.events.Event","flash.display.Sprite","General.Input","flash.text.TextField","flash.text.TextFormat","flash.text.TextFormatAlign","TestBed.TestRagdoll","TestBed.TestCompound","TestBed.TestCrankGearsPulley","TestBed.TestBridge","TestBed.TestStack","TestBed.TestCCD","TestBed.TestTheoJansen","TestBed.TestBuoyancy","TestBed.TestOneSidedPlatform","TestBed.TestBreakable","TestBed.TestRaycast","TestBed.TestSensor","General.FRateLimiter","General.FpsCounter"], "0.8.0", "0.8.1"
);
// class TestBed.Test
joo.classLoader.prepare(
"package TestBed",
"public class Test",1,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main,General.Input,Box2D.Dynamics.b2DebugDraw);},
"public function Test",function(){this.mousePVec$1=this.mousePVec$1();
this.m_sprite=Main.m_sprite;
var worldAABB=new Box2D.Collision.b2AABB();
worldAABB.lowerBound.Set(-1000.0,-1000.0);
worldAABB.upperBound.Set(1000.0,1000.0);
var gravity=new Box2D.Common.Math.b2Vec2(0.0,10.0);
var doSleep=true;
this.m_world=new Box2D.Dynamics.b2World(gravity,doSleep);
this.m_world.SetWarmStarting(true);
var dbgDraw=new Box2D.Dynamics.b2DebugDraw();
dbgDraw.SetSprite(this.m_sprite);
dbgDraw.SetDrawScale(30.0);
dbgDraw.SetFillAlpha(0.3);
dbgDraw.SetLineThickness(1.0);
dbgDraw.SetFlags(Box2D.Dynamics.b2DebugDraw.e_shapeBit|Box2D.Dynamics.b2DebugDraw.e_jointBit);
this.m_world.SetDebugDraw(dbgDraw);
var wall=new Box2D.Collision.Shapes.b2PolygonShape();
var wallBd=new Box2D.Dynamics.b2BodyDef();
var wallB;
wallBd.position.Set(-95/this.m_physScale,360/this.m_physScale/2);
wall.SetAsBox(100/this.m_physScale,400/this.m_physScale/2);
wallB=this.m_world.CreateBody(wallBd);
wallB.CreateFixture2(wall,0.0);
wallBd.position.Set((640+95)/this.m_physScale,360/this.m_physScale/2);
wallB=this.m_world.CreateBody(wallBd);
wallB.CreateFixture2(wall,0.0);
wallBd.position.Set(640/this.m_physScale/2,-95/this.m_physScale);
wall.SetAsBox(680/this.m_physScale/2,100/this.m_physScale);
wallB=this.m_world.CreateBody(wallBd);
wallB.CreateFixture2(wall,0.0);
wallBd.position.Set(640/this.m_physScale/2,(360+95)/this.m_physScale);
wallB=this.m_world.CreateBody(wallBd);
wallB.CreateFixture2(wall,0.0);
},
"public function Update",function(){
this.UpdateMouseWorld();
this.MouseDestroy();
this.MouseDrag();
var physStart=flash.utils.getTimer();
this.m_world.Step(this.m_timeStep,this.m_velocityIterations,this.m_positionIterations);
Main.m_fpsCounter.updatePhys(physStart);
this.m_world.DrawDebugData();
},
"public var",{m_world:null},
"public var",{m_bomb:null},
"public var",{m_mouseJoint:null},
"public var",{m_velocityIterations:10},
"public var",{m_positionIterations:10},
"public var",{m_timeStep:1.0/30.0},
"public var",{m_physScale:30},
"static public var",{mouseXWorldPhys:NaN},
"static public var",{mouseYWorldPhys:NaN},
"static public var",{mouseXWorld:NaN},
"static public var",{mouseYWorld:NaN},
"public var",{m_sprite:null},
"public function UpdateMouseWorld",function(){
TestBed.Test.mouseXWorldPhys=(General.Input.mouseX)/this.m_physScale;
TestBed.Test.mouseYWorldPhys=(General.Input.mouseY)/this.m_physScale;
TestBed.Test.mouseXWorld=(General.Input.mouseX);
TestBed.Test.mouseYWorld=(General.Input.mouseY);
},
"public function MouseDrag",function(){
if(General.Input.mouseDown&&!this.m_mouseJoint){
var body=this.GetBodyAtMouse();
if(body)
{
var md=new Box2D.Dynamics.Joints.b2MouseJointDef();
md.bodyA=this.m_world.GetGroundBody();
md.bodyB=body;
md.target.Set(TestBed.Test.mouseXWorldPhys,TestBed.Test.mouseYWorldPhys);
md.collideConnected=true;
md.maxForce=300.0*body.GetMass();
this.m_mouseJoint=as(this.m_world.CreateJoint(md),Box2D.Dynamics.Joints.b2MouseJoint);
body.SetAwake(true);
}
}
if(!General.Input.mouseDown){
if(this.m_mouseJoint)
{
this.m_world.DestroyJoint(this.m_mouseJoint);
this.m_mouseJoint=null;
}
}
if(this.m_mouseJoint)
{
var p2=new Box2D.Common.Math.b2Vec2(TestBed.Test.mouseXWorldPhys,TestBed.Test.mouseYWorldPhys);
this.m_mouseJoint.SetTarget(p2);
}
},
"public function MouseDestroy",function(){
if(!General.Input.mouseDown&&General.Input.isKeyPressed(68)){
var body=this.GetBodyAtMouse(true);
if(body)
{
this.m_world.DestroyBody(body);
return;
}
}
},
"private var",{mousePVec:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public function GetBodyAtMouse",function(includeStatic){var this$=this;if(arguments.length<1){includeStatic=false;}
this.mousePVec$1.Set(TestBed.Test.mouseXWorldPhys,TestBed.Test.mouseYWorldPhys);
var aabb=new Box2D.Collision.b2AABB();
aabb.lowerBound.Set(TestBed.Test.mouseXWorldPhys-0.001,TestBed.Test.mouseYWorldPhys-0.001);
aabb.upperBound.Set(TestBed.Test.mouseXWorldPhys+0.001,TestBed.Test.mouseYWorldPhys+0.001);
var body=null;
var fixture;
function GetBodyCallback(fixture)
{
var shape=fixture.GetShape();
if(fixture.GetBody().GetType()!=Box2D.Dynamics.b2Body.b2_staticBody||includeStatic)
{
var inside=shape.TestPoint(fixture.GetBody().GetTransform(),this$.mousePVec$1);
if(inside)
{
body=fixture.GetBody();
return false;
}
}
return true;
}
this.m_world.QueryAABB(GetBodyCallback,aabb);
return body;
},
];},[],["Main","Box2D.Collision.b2AABB","Box2D.Common.Math.b2Vec2","Box2D.Dynamics.b2World","Box2D.Dynamics.b2DebugDraw","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.b2BodyDef","General.Input","Box2D.Dynamics.Joints.b2MouseJointDef","Box2D.Dynamics.Joints.b2MouseJoint","Box2D.Dynamics.b2Body"], "0.8.0", "0.8.1"
);
// class TestBed.TestBreakable
joo.classLoader.prepare(
"package TestBed",
"public class TestBreakable extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main,Math);},
"public function TestBreakable",function(){this.super$2();this.m_velocity=this.m_velocity();this.m_shape1=this.m_shape1();this.m_shape2=this.m_shape2();
Main.m_aboutText.text="Breakable";
this.m_world.SetContactListener(new TestBed.TestBreakable.ContactListener(this));
var ground=this.m_world.GetGroundBody();
{
var bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd.position.Set(5.0,5.0);
bd.angle=0.25*Math.PI;
this.m_body1=this.m_world.CreateBody(bd);
this.m_shape1.SetAsOrientedBox(0.5,0.5,new Box2D.Common.Math.b2Vec2(-0.5,0.0));
this.m_piece1=this.m_body1.CreateFixture2(this.m_shape1,1.0);
this.m_shape2.SetAsOrientedBox(0.5,0.5,new Box2D.Common.Math.b2Vec2(0.5,0.0));
this.m_piece2=this.m_body1.CreateFixture2(this.m_shape2,1.0);
}
this.m_break=false;
this.m_broke=false;
},
"public function Break",function()
{var this$=this;
this.m_body1.SetLinearVelocity(this.m_velocity);
this.m_body1.SetAngularVelocity(this.m_angularVelocity);
this.m_body1.Split(function(fixture){
return fixture!=this$.m_piece1;
});
},
"override public function Update",function()
{
this.Update$2();
if(this.m_break)
{
this.Break();
this.m_broke=true;
this.m_break=false;
}
if(this.m_broke==false)
{
this.m_velocity=this.m_body1.GetLinearVelocity();
this.m_angularVelocity=this.m_body1.GetAngularVelocity();
}
},
"public var",{m_body1:null},
"public var",{m_velocity:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{m_angularVelocity:NaN},
"public var",{m_shape1:function(){return(new Box2D.Collision.Shapes.b2PolygonShape());}},
"public var",{m_shape2:function(){return(new Box2D.Collision.Shapes.b2PolygonShape());}},
"public var",{m_piece1:null},
"public var",{m_piece2:null},
"public var",{m_broke:false},
"public var",{m_break:false},

"class ContactListener extends Box2D.Dynamics.b2EmptyContactListener",2,function($$private){;return[

"private var",{test:null},
"public function ContactListener",function(test)
{this.super$2();
this.test$2=test;
},
"override public function PostSolve",function(contact,impulse)
{
if(this.test$2.m_broke)
{
return;
}
var count=contact.GetManifold().m_pointCount;
var maxImpulse=0.0;
for(var i=0;i<count;i++)
{
maxImpulse=Box2D.Common.Math.b2Math.Max(maxImpulse,impulse.normalImpulses[i]);
}
if(maxImpulse>50)
{
this.test$2.m_break=true;
}
},
];},[],];},[],["TestBed.Test","Main","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Math","Box2D.Common.Math.b2Vec2","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.b2EmptyContactListener","Box2D.Common.Math.b2Math"], "0.8.0", "0.8.1"
);
// class TestBed.TestBridge
joo.classLoader.prepare(
"package TestBed",
"public class TestBridge extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main,Math);},
"public function TestBridge",function(){this.super$2();
Main.m_aboutText.text="Bridge";
var ground=this.m_world.GetGroundBody();
var i;
var anchor=new Box2D.Common.Math.b2Vec2();
var body;
{
var sd=new Box2D.Collision.Shapes.b2PolygonShape();
var fixtureDef=new Box2D.Dynamics.b2FixtureDef();
sd.SetAsBox(24/this.m_physScale,5/this.m_physScale);
fixtureDef.shape=sd;
fixtureDef.density=20.0;
fixtureDef.friction=0.2;
var bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
var jd=new Box2D.Dynamics.Joints.b2RevoluteJointDef();var numPlanks=10;
jd.lowerAngle=-15/(180/Math.PI);
jd.upperAngle=15/(180/Math.PI);
jd.enableLimit=true;
var prevBody=ground;
for(i=0;i<numPlanks;++i)
{
bd.position.Set((100+22+44*i)/this.m_physScale,250/this.m_physScale);
body=this.m_world.CreateBody(bd);
body.CreateFixture(fixtureDef);
anchor.Set((100+44*i)/this.m_physScale,250/this.m_physScale);
jd.Initialize(prevBody,body,anchor);
this.m_world.CreateJoint(jd);
prevBody=body;
}
anchor.Set((100+44*numPlanks)/this.m_physScale,250/this.m_physScale);
jd.Initialize(prevBody,ground,anchor);
this.m_world.CreateJoint(jd);
}
for(i=0;i<5;i++){
var bodyDef=new Box2D.Dynamics.b2BodyDef();
bodyDef.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
var boxShape=new Box2D.Collision.Shapes.b2PolygonShape();
fixtureDef.shape=boxShape;
fixtureDef.density=1.0;
fixtureDef.friction=0.3;
fixtureDef.restitution=0.1;
boxShape.SetAsBox((Math.random()*5+10)/this.m_physScale,(Math.random()*5+10)/this.m_physScale);
bodyDef.position.Set((Math.random()*400+120)/this.m_physScale,(Math.random()*150+50)/this.m_physScale);
bodyDef.angle=Math.random()*Math.PI;
body=this.m_world.CreateBody(bodyDef);
body.CreateFixture(fixtureDef);
}
for(i=0;i<5;i++){
var bodyDefC=new Box2D.Dynamics.b2BodyDef();
bodyDefC.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
var circShape=new Box2D.Collision.Shapes.b2CircleShape((Math.random()*5+10)/this.m_physScale);
fixtureDef.shape=circShape;
fixtureDef.density=1.0;
fixtureDef.friction=0.3;
fixtureDef.restitution=0.1;
bodyDefC.position.Set((Math.random()*400+120)/this.m_physScale,(Math.random()*150+50)/this.m_physScale);
bodyDefC.angle=Math.random()*Math.PI;
body=this.m_world.CreateBody(bodyDefC);
body.CreateFixture(fixtureDef);
}
var j;
for(i=0;i<15;i++){
var bodyDefP=new Box2D.Dynamics.b2BodyDef();
bodyDefP.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
var polyShape=new Box2D.Collision.Shapes.b2PolygonShape();
var vertices=new Array();
var vertexCount;
if(Math.random()>0.66){
vertexCount=4;
for(j=0;j<vertexCount;++j)
{
vertices[j]=new Box2D.Common.Math.b2Vec2();
}
vertices[0].Set((-10-Math.random()*10)/this.m_physScale,(10+Math.random()*10)/this.m_physScale);
vertices[1].Set((-5-Math.random()*10)/this.m_physScale,(-10-Math.random()*10)/this.m_physScale);
vertices[2].Set((5+Math.random()*10)/this.m_physScale,(-10-Math.random()*10)/this.m_physScale);
vertices[3].Set((10+Math.random()*10)/this.m_physScale,(10+Math.random()*10)/this.m_physScale);
}
else if(Math.random()>0.5){
vertexCount=5;
for(j=0;j<vertexCount;++j)
{
vertices[j]=new Box2D.Common.Math.b2Vec2();
}
vertices[0].Set(0,(10+Math.random()*10)/this.m_physScale);
vertices[2].Set((-5-Math.random()*10)/this.m_physScale,(-10-Math.random()*10)/this.m_physScale);
vertices[3].Set((5+Math.random()*10)/this.m_physScale,(-10-Math.random()*10)/this.m_physScale);
vertices[1].Set((vertices[0].x+vertices[2].x),(vertices[0].y+vertices[2].y));
vertices[1].Multiply(Math.random()/2+0.8);
vertices[4].Set((vertices[3].x+vertices[0].x),(vertices[3].y+vertices[0].y));
vertices[4].Multiply(Math.random()/2+0.8);
}
else{
vertexCount=3;
for(j=0;j<vertexCount;++j)
{
vertices[j]=new Box2D.Common.Math.b2Vec2();
}
vertices[0].Set(0,(10+Math.random()*10)/this.m_physScale);
vertices[1].Set((-5-Math.random()*10)/this.m_physScale,(-10-Math.random()*10)/this.m_physScale);
vertices[2].Set((5+Math.random()*10)/this.m_physScale,(-10-Math.random()*10)/this.m_physScale);
}
polyShape.SetAsArray(vertices,vertexCount);
fixtureDef.shape=polyShape;
fixtureDef.density=1.0;
fixtureDef.friction=0.3;
fixtureDef.restitution=0.1;
bodyDefP.position.Set((Math.random()*400+120)/this.m_physScale,(Math.random()*150+50)/this.m_physScale);
bodyDefP.angle=Math.random()*Math.PI;
body=this.m_world.CreateBody(bodyDefP);
body.CreateFixture(fixtureDef);
}
},
];},[],["TestBed.Test","Main","Box2D.Common.Math.b2Vec2","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.b2FixtureDef","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Box2D.Dynamics.Joints.b2RevoluteJointDef","Math","Box2D.Collision.Shapes.b2CircleShape","Array"], "0.8.0", "0.8.1"
);
// class TestBed.TestBuoyancy
joo.classLoader.prepare(
"package TestBed",
"public class TestBuoyancy extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Main,Box2D.Dynamics.b2Body,Math);},
"private var",{m_bodies:function(){return(new Array());}},
"private var",{m_controller:null},
"public function TestBuoyancy",function(){this.super$2();this.m_bodies$2=this.m_bodies$2();
var bc=new Box2D.Dynamics.Controllers.b2BuoyancyController();
this.m_controller$2=bc;
bc.normal.Set(0,-1);
bc.offset=-200/this.m_physScale;
bc.density=2.0;
bc.linearDrag=5;
bc.angularDrag=2;
var ground=this.m_world.GetGroundBody();
var i;
var anchor=new Box2D.Common.Math.b2Vec2();
var body;
var fd;
for(i=0;i<5;i++){
var bodyDef=new Box2D.Dynamics.b2BodyDef();
bodyDef.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
var boxDef=new Box2D.Collision.Shapes.b2PolygonShape();
fd=new Box2D.Dynamics.b2FixtureDef();
fd.shape=boxDef;
fd.density=1.0;
fd.friction=0.3;
fd.restitution=0.1;
boxDef.SetAsBox((Math.random()*5+10)/this.m_physScale,(Math.random()*5+10)/this.m_physScale);
bodyDef.position.Set((Math.random()*400+120)/this.m_physScale,(Math.random()*150+50)/this.m_physScale);
bodyDef.angle=Math.random()*Math.PI;
body=this.m_world.CreateBody(bodyDef);
body.CreateFixture(fd);
this.m_bodies$2.push(body);
}
for(i=0;i<5;i++){
var bodyDefC=new Box2D.Dynamics.b2BodyDef();
bodyDefC.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
var circDef=new Box2D.Collision.Shapes.b2CircleShape((Math.random()*5+10)/this.m_physScale);
fd=new Box2D.Dynamics.b2FixtureDef();
fd.shape=circDef;
fd.density=1.0;
fd.friction=0.3;
fd.restitution=0.1;
bodyDefC.position.Set((Math.random()*400+120)/this.m_physScale,(Math.random()*150+50)/this.m_physScale);
bodyDefC.angle=Math.random()*Math.PI;
body=this.m_world.CreateBody(bodyDefC);
body.CreateFixture(fd);
this.m_bodies$2.push(body);
}
for(i=0;i<15;i++){
var bodyDefP=new Box2D.Dynamics.b2BodyDef();
bodyDefP.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
var polyDef=new Box2D.Collision.Shapes.b2PolygonShape();
if(Math.random()>0.66){
polyDef.SetAsArray([
new Box2D.Common.Math.b2Vec2((-10-Math.random()*10)/this.m_physScale,(10+Math.random()*10)/this.m_physScale),
new Box2D.Common.Math.b2Vec2((-5-Math.random()*10)/this.m_physScale,(-10-Math.random()*10)/this.m_physScale),
new Box2D.Common.Math.b2Vec2((5+Math.random()*10)/this.m_physScale,(-10-Math.random()*10)/this.m_physScale),
new Box2D.Common.Math.b2Vec2((10+Math.random()*10)/this.m_physScale,(10+Math.random()*10)/this.m_physScale)
]);
}
else if(Math.random()>0.5)
{
var array=[];
array[0]=new Box2D.Common.Math.b2Vec2(0,(10+Math.random()*10)/this.m_physScale);
array[2]=new Box2D.Common.Math.b2Vec2((-5-Math.random()*10)/this.m_physScale,(-10-Math.random()*10)/this.m_physScale);
array[3]=new Box2D.Common.Math.b2Vec2((5+Math.random()*10)/this.m_physScale,(-10-Math.random()*10)/this.m_physScale);
array[1]=new Box2D.Common.Math.b2Vec2((array[0].x+array[2].x),(array[0].y+array[2].y));
array[1].Multiply(Math.random()/2+0.8);
array[4]=new Box2D.Common.Math.b2Vec2((array[3].x+array[0].x),(array[3].y+array[0].y));
array[4].Multiply(Math.random()/2+0.8);
polyDef.SetAsArray(array);
}
else
{
polyDef.SetAsArray([
new Box2D.Common.Math.b2Vec2(0,(10+Math.random()*10)/this.m_physScale),
new Box2D.Common.Math.b2Vec2((-5-Math.random()*10)/this.m_physScale,(-10-Math.random()*10)/this.m_physScale),
new Box2D.Common.Math.b2Vec2((5+Math.random()*10)/this.m_physScale,(-10-Math.random()*10)/this.m_physScale)
]);
}
fd=new Box2D.Dynamics.b2FixtureDef();
fd.shape=polyDef;
fd.density=1.0;
fd.friction=0.3;
fd.restitution=0.1;
bodyDefP.position.Set((Math.random()*400+120)/this.m_physScale,(Math.random()*150+50)/this.m_physScale);
bodyDefP.angle=Math.random()*Math.PI;
body=this.m_world.CreateBody(bodyDefP);
body.CreateFixture(fd);
this.m_bodies$2.push(body);
}
boxDef.SetAsBox(40/this.m_physScale,10/this.m_physScale);
fd=new Box2D.Dynamics.b2FixtureDef();
fd.shape=boxDef;
fd.density=3.0;
bodyDef.position.Set(50/this.m_physScale,300/this.m_physScale);
bodyDef.angle=0;
body=this.m_world.CreateBody(bodyDef);
body.CreateFixture(fd);
this.m_bodies$2.push(body);
bodyDef.position.Set(300/this.m_physScale,300/this.m_physScale);
body=this.m_world.CreateBody(bodyDef);
circDef=new Box2D.Collision.Shapes.b2CircleShape(7/this.m_physScale);
fd=new Box2D.Dynamics.b2FixtureDef();
fd.shape=circDef;
fd.density=2;
circDef.m_p.Set(30/this.m_physScale,0/this.m_physScale);
body.CreateFixture(fd);
circDef.m_p.Set(-30/this.m_physScale,0/this.m_physScale);
body.CreateFixture(fd);
circDef.m_p.Set(0/this.m_physScale,30/this.m_physScale);
body.CreateFixture(fd);
circDef.m_p.Set(0/this.m_physScale,-30/this.m_physScale);
body.CreateFixture(fd);
fd=new Box2D.Dynamics.b2FixtureDef();
fd.shape=boxDef;
fd.density=2.0;
boxDef.SetAsBox(30/this.m_physScale,2/this.m_physScale);
body.CreateFixture(fd);
fd.density=2.0;
boxDef.SetAsBox(2/this.m_physScale,30/this.m_physScale);
body.CreateFixture(fd);
this.m_bodies$2.push(body);
for(var $1 in this.m_bodies$2){body=this.m_bodies$2[$1];
this.m_controller$2.AddBody(body);}
this.m_world.AddController(this.m_controller$2);
Main.m_aboutText.text="Buoyancy";
},
"public override function Update",function(){
this.Update$2();
this.m_sprite.graphics.lineStyle(1,0x0000ff,1);
this.m_sprite.graphics.moveTo(5,200);
this.m_sprite.graphics.lineTo(635,200);
this.m_sprite.graphics.lineStyle();
this.m_sprite.graphics.beginFill(0x0000ff,0.2);
this.m_sprite.graphics.moveTo(5,200);
this.m_sprite.graphics.lineTo(635,200);
this.m_sprite.graphics.lineTo(635,355);
this.m_sprite.graphics.lineTo(5,355);
this.m_sprite.graphics.endFill();
},
];},[],["TestBed.Test","Array","Box2D.Dynamics.Controllers.b2BuoyancyController","Box2D.Common.Math.b2Vec2","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.b2FixtureDef","Math","Box2D.Collision.Shapes.b2CircleShape","Main"], "0.8.0", "0.8.1"
);
// class TestBed.TestCCD
joo.classLoader.prepare(
"package TestBed",
"public class TestCCD extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main);},
"public function TestCCD",function(){this.super$2();
Main.m_aboutText.text="Continuous Collision Detection";
this.m_world.SetContinuousPhysics(true);
var bd;
var body;
var fixtureDef=new Box2D.Dynamics.b2FixtureDef();
fixtureDef.density=4.0;
fixtureDef.restitution=1.4;
{
bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd.bullet=true;
bd.position.Set(150.0/this.m_physScale,100.0/this.m_physScale);
body=this.m_world.CreateBody(bd);
var sd_bottom=new Box2D.Collision.Shapes.b2PolygonShape();
sd_bottom.SetAsBox(45.0/this.m_physScale,4.5/this.m_physScale);
fixtureDef.shape=sd_bottom;
body.CreateFixture(fixtureDef);
var sd_left=new Box2D.Collision.Shapes.b2PolygonShape();
sd_left.SetAsOrientedBox(4.5/this.m_physScale,81.0/this.m_physScale,new Box2D.Common.Math.b2Vec2(-43.5/this.m_physScale,-70.5/this.m_physScale),-0.2);
fixtureDef.shape=sd_left;
body.CreateFixture(fixtureDef);
var sd_right=new Box2D.Collision.Shapes.b2PolygonShape();
sd_right.SetAsOrientedBox(4.5/this.m_physScale,81.0/this.m_physScale,new Box2D.Common.Math.b2Vec2(43.5/this.m_physScale,-70.5/this.m_physScale),0.2);
fixtureDef.shape=sd_right;
body.CreateFixture(fixtureDef);
}
for(var i=0;i<5;i++){
var cd=new Box2D.Collision.Shapes.b2CircleShape((Math.random()*10+5)/this.m_physScale);
fixtureDef.shape=cd;
fixtureDef.friction=0.3;
fixtureDef.density=1.0;
fixtureDef.restitution=1.1;
bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd.bullet=true;
bd.position.Set((Math.random()*300+250)/this.m_physScale,(Math.random()*320+20)/this.m_physScale);
body=this.m_world.CreateBody(bd);
body.CreateFixture(fixtureDef);
}
},
];},[],["TestBed.Test","Main","Box2D.Dynamics.b2FixtureDef","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Common.Math.b2Vec2","Box2D.Collision.Shapes.b2CircleShape","Math"], "0.8.0", "0.8.1"
);
// class TestBed.TestCompound
joo.classLoader.prepare(
"package TestBed",
"public class TestCompound extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main,Math);},
"public function TestCompound",function(){this.super$2();
Main.m_aboutText.text="Compound Shapes";
var bd;
var body;
var i;
var x;
{
var cd1=new Box2D.Collision.Shapes.b2CircleShape();
cd1.SetRadius(15.0/this.m_physScale);
cd1.SetLocalPosition(new Box2D.Common.Math.b2Vec2(-15.0/this.m_physScale,15.0/this.m_physScale));
var cd2=new Box2D.Collision.Shapes.b2CircleShape();
cd2.SetRadius(15.0/this.m_physScale);
cd2.SetLocalPosition(new Box2D.Common.Math.b2Vec2(15.0/this.m_physScale,15.0/this.m_physScale));
bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
for(i=0;i<5;++i)
{
x=320.0+Box2D.Common.Math.b2Math.RandomRange(-3.0,3.0);
bd.position.Set((x+150.0)/this.m_physScale,(31.5+75.0*-i+300.0)/this.m_physScale);
bd.angle=Box2D.Common.Math.b2Math.RandomRange(-Math.PI,Math.PI);
body=this.m_world.CreateBody(bd);
body.CreateFixture2(cd1,2.0);
body.CreateFixture2(cd2,0.0);
}
}
{
var pd1=new Box2D.Collision.Shapes.b2PolygonShape();
pd1.SetAsBox(7.5/this.m_physScale,15.0/this.m_physScale);
var pd2=new Box2D.Collision.Shapes.b2PolygonShape();
pd2.SetAsOrientedBox(7.5/this.m_physScale,15.0/this.m_physScale,new Box2D.Common.Math.b2Vec2(0.0,-15.0/this.m_physScale),0.5*Math.PI);
bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
for(i=0;i<5;++i)
{
x=320.0+Box2D.Common.Math.b2Math.RandomRange(-3.0,3.0);
bd.position.Set((x-150.0)/this.m_physScale,(31.5+75.0*-i+300)/this.m_physScale);
bd.angle=Box2D.Common.Math.b2Math.RandomRange(-Math.PI,Math.PI);
body=this.m_world.CreateBody(bd);
body.CreateFixture2(pd1,2.0);
body.CreateFixture2(pd2,2.0);
}
}
{
var xf1=new Box2D.Common.Math.b2Transform();
xf1.R.Set(0.3524*Math.PI);
xf1.position=Box2D.Common.Math.b2Math.MulMV(xf1.R,new Box2D.Common.Math.b2Vec2(1.0,0.0));
var sd1=new Box2D.Collision.Shapes.b2PolygonShape();
sd1.SetAsArray([
Box2D.Common.Math.b2Math.MulX(xf1,new Box2D.Common.Math.b2Vec2(-30.0/this.m_physScale,0.0)),
Box2D.Common.Math.b2Math.MulX(xf1,new Box2D.Common.Math.b2Vec2(30.0/this.m_physScale,0.0)),
Box2D.Common.Math.b2Math.MulX(xf1,new Box2D.Common.Math.b2Vec2(0.0,15.0/this.m_physScale)),
]);
var xf2=new Box2D.Common.Math.b2Transform();
xf2.R.Set(-0.3524*Math.PI);
xf2.position=Box2D.Common.Math.b2Math.MulMV(xf2.R,new Box2D.Common.Math.b2Vec2(-30.0/this.m_physScale,0.0));
var sd2=new Box2D.Collision.Shapes.b2PolygonShape();
sd2.SetAsArray([
Box2D.Common.Math.b2Math.MulX(xf2,new Box2D.Common.Math.b2Vec2(-30.0/this.m_physScale,0.0)),
Box2D.Common.Math.b2Math.MulX(xf2,new Box2D.Common.Math.b2Vec2(30.0/this.m_physScale,0.0)),
Box2D.Common.Math.b2Math.MulX(xf2,new Box2D.Common.Math.b2Vec2(0.0,15.0/this.m_physScale)),
]);
bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd.fixedRotation=true;
for(i=0;i<5;++i)
{
x=320.0+Box2D.Common.Math.b2Math.RandomRange(-3.0,3.0);
bd.position.Set(x/this.m_physScale,(-61.5+55.0*-i+300)/this.m_physScale);
bd.angle=0.0;
body=this.m_world.CreateBody(bd);
body.CreateFixture2(sd1,2.0);
body.CreateFixture2(sd2,2.0);
}
}
{
var sd_bottom=new Box2D.Collision.Shapes.b2PolygonShape();
sd_bottom.SetAsBox(45.0/this.m_physScale,4.5/this.m_physScale);
var sd_left=new Box2D.Collision.Shapes.b2PolygonShape();
sd_left.SetAsOrientedBox(4.5/this.m_physScale,81.0/this.m_physScale,new Box2D.Common.Math.b2Vec2(-43.5/this.m_physScale,-70.5/this.m_physScale),-0.2);
var sd_right=new Box2D.Collision.Shapes.b2PolygonShape();
sd_right.SetAsOrientedBox(4.5/this.m_physScale,81.0/this.m_physScale,new Box2D.Common.Math.b2Vec2(43.5/this.m_physScale,-70.5/this.m_physScale),0.2);
bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd.position.Set(320.0/this.m_physScale,300.0/this.m_physScale);
body=this.m_world.CreateBody(bd);
body.CreateFixture2(sd_bottom,4.0);
body.CreateFixture2(sd_left,4.0);
body.CreateFixture2(sd_right,4.0);
}
},
];},[],["TestBed.Test","Main","Box2D.Collision.Shapes.b2CircleShape","Box2D.Common.Math.b2Vec2","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Box2D.Common.Math.b2Math","Math","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Common.Math.b2Transform"], "0.8.0", "0.8.1"
);
// class TestBed.TestCrankGearsPulley
joo.classLoader.prepare(
"package TestBed",
"public class TestCrankGearsPulley extends TestBed.Test",2,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main,Math);},
"public function TestCrankGearsPulley",function(){this.super$2();
Main.m_aboutText.text="Joints";
var ground=this.m_world.GetGroundBody();
var body;
var circleBody;
var sd;
var bd;
var fixtureDef=new Box2D.Dynamics.b2FixtureDef();
{
sd=new Box2D.Collision.Shapes.b2PolygonShape();
sd.SetAsBox(7.5/this.m_physScale,30.0/this.m_physScale);
fixtureDef.shape=sd;
fixtureDef.density=1.0;
var rjd=new Box2D.Dynamics.Joints.b2RevoluteJointDef();
var prevBody=ground;
bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd.position.Set(100.0/this.m_physScale,(360.0-105.0)/this.m_physScale);
body=this.m_world.CreateBody(bd);
body.CreateFixture(fixtureDef);
rjd.Initialize(prevBody,body,new Box2D.Common.Math.b2Vec2(100.0/this.m_physScale,(360.0-75.0)/this.m_physScale));
rjd.motorSpeed=1.0*-Math.PI;
rjd.maxMotorTorque=5000.0;
rjd.enableMotor=true;
this.m_joint1$2=as(this.m_world.CreateJoint(rjd),Box2D.Dynamics.Joints.b2RevoluteJoint);
prevBody=body;
sd=new Box2D.Collision.Shapes.b2PolygonShape;
sd.SetAsBox(7.5/this.m_physScale,60.0/this.m_physScale);
fixtureDef.shape=sd;
bd.position.Set(100.0/this.m_physScale,(360.0-195.0)/this.m_physScale);
body=this.m_world.CreateBody(bd);
body.CreateFixture(fixtureDef);
rjd.Initialize(prevBody,body,new Box2D.Common.Math.b2Vec2(100.0/this.m_physScale,(360.0-135.0)/this.m_physScale));
rjd.enableMotor=false;
this.m_world.CreateJoint(rjd);
prevBody=body;
sd=new Box2D.Collision.Shapes.b2PolygonShape();
sd.SetAsBox(22.5/this.m_physScale,22.5/this.m_physScale);
fixtureDef.shape=sd;
bd.position.Set(100.0/this.m_physScale,(360.0-255.0)/this.m_physScale);
body=this.m_world.CreateBody(bd);
body.CreateFixture(fixtureDef);
rjd.Initialize(prevBody,body,new Box2D.Common.Math.b2Vec2(100.0/this.m_physScale,(360.0-255.0)/this.m_physScale));
this.m_world.CreateJoint(rjd);
var pjd=new Box2D.Dynamics.Joints.b2PrismaticJointDef();
pjd.Initialize(ground,body,new Box2D.Common.Math.b2Vec2(100.0/this.m_physScale,(360.0-255.0)/this.m_physScale),new Box2D.Common.Math.b2Vec2(0.0,1.0));
pjd.maxMotorForce=500.0;
pjd.enableMotor=true;
this.m_joint2$2=as(this.m_world.CreateJoint(pjd),Box2D.Dynamics.Joints.b2PrismaticJoint);
sd=new Box2D.Collision.Shapes.b2PolygonShape();
sd.SetAsBox(22.5/this.m_physScale,22.5/this.m_physScale);
fixtureDef.shape=sd;
fixtureDef.density=2.0;
bd.position.Set(100.0/this.m_physScale,(360.0-345.0)/this.m_physScale);
body=this.m_world.CreateBody(bd);
body.CreateFixture(fixtureDef);
}
var circle1=new Box2D.Collision.Shapes.b2CircleShape(25/this.m_physScale);
fixtureDef.shape=circle1;
fixtureDef.density=5.0;
var bd1=new Box2D.Dynamics.b2BodyDef();
bd1.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd1.position.Set(200/this.m_physScale,360/2/this.m_physScale);
var body1=this.m_world.CreateBody(bd1);
body1.CreateFixture(fixtureDef);
var jd1=new Box2D.Dynamics.Joints.b2RevoluteJointDef();
jd1.Initialize(ground,body1,bd1.position);
this.m_gJoint1=as(this.m_world.CreateJoint(jd1),Box2D.Dynamics.Joints.b2RevoluteJoint);
var circle2=new Box2D.Collision.Shapes.b2CircleShape(50/this.m_physScale);
fixtureDef.shape=circle2;
fixtureDef.density=5.0;
var bd2=new Box2D.Dynamics.b2BodyDef();
bd2.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd2.position.Set(275/this.m_physScale,360/2/this.m_physScale);
var body2=this.m_world.CreateBody(bd2);
body2.CreateFixture(fixtureDef);
var jd2=new Box2D.Dynamics.Joints.b2RevoluteJointDef();
jd2.Initialize(ground,body2,bd2.position);
this.m_gJoint2=as(this.m_world.CreateJoint(jd2),Box2D.Dynamics.Joints.b2RevoluteJoint);
var box=new Box2D.Collision.Shapes.b2PolygonShape();
box.SetAsBox(10/this.m_physScale,100/this.m_physScale);
fixtureDef.shape=box;
fixtureDef.density=5.0;
var bd3=new Box2D.Dynamics.b2BodyDef();
bd3.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd3.position.Set(335/this.m_physScale,360/2/this.m_physScale);
var body3=this.m_world.CreateBody(bd3);
body3.CreateFixture(fixtureDef);
var jd3=new Box2D.Dynamics.Joints.b2PrismaticJointDef();
jd3.Initialize(ground,body3,bd3.position,new Box2D.Common.Math.b2Vec2(0,1));
jd3.lowerTranslation=-25.0/this.m_physScale;
jd3.upperTranslation=100.0/this.m_physScale;
jd3.enableLimit=true;
this.m_gJoint3=as(this.m_world.CreateJoint(jd3),Box2D.Dynamics.Joints.b2PrismaticJoint);
var jd4=new Box2D.Dynamics.Joints.b2GearJointDef();
jd4.bodyA=body1;
jd4.bodyB=body2;
jd4.joint1=this.m_gJoint1;
jd4.joint2=this.m_gJoint2;
jd4.ratio=circle2.GetRadius()/circle1.GetRadius();
this.m_gJoint4=as(this.m_world.CreateJoint(jd4),Box2D.Dynamics.Joints.b2GearJoint);
var jd5=new Box2D.Dynamics.Joints.b2GearJointDef();
jd5.bodyA=body2;
jd5.bodyB=body3;
jd5.joint1=this.m_gJoint2;
jd5.joint2=this.m_gJoint3;
jd5.ratio=-1.0/circle2.GetRadius();
this.m_gJoint5=as(this.m_world.CreateJoint(jd5),Box2D.Dynamics.Joints.b2GearJoint);
sd=new Box2D.Collision.Shapes.b2PolygonShape();
sd.SetAsBox(50/this.m_physScale,20/this.m_physScale);
fixtureDef.shape=sd;
fixtureDef.density=5.0;
bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd.position.Set(480/this.m_physScale,200/this.m_physScale);
body2=this.m_world.CreateBody(bd);
body2.CreateFixture(fixtureDef);
var pulleyDef=new Box2D.Dynamics.Joints.b2PulleyJointDef();
var anchor1=new Box2D.Common.Math.b2Vec2(335/this.m_physScale,180/this.m_physScale);
var anchor2=new Box2D.Common.Math.b2Vec2(480/this.m_physScale,180/this.m_physScale);
var groundAnchor1=new Box2D.Common.Math.b2Vec2(335/this.m_physScale,50/this.m_physScale);
var groundAnchor2=new Box2D.Common.Math.b2Vec2(480/this.m_physScale,50/this.m_physScale);
pulleyDef.Initialize(body3,body2,groundAnchor1,groundAnchor2,anchor1,anchor2,2.0);
pulleyDef.maxLengthA=200/this.m_physScale;
pulleyDef.maxLengthB=150/this.m_physScale;as(
this.m_world.CreateJoint(pulleyDef),Box2D.Dynamics.Joints.b2PulleyJoint);
var circ=new Box2D.Collision.Shapes.b2CircleShape(40/this.m_physScale);
fixtureDef.shape=circ;
fixtureDef.friction=0.3;
fixtureDef.restitution=0.3;
fixtureDef.density=5.0;
bd.position.Set(485/this.m_physScale,100/this.m_physScale);
body1=circleBody=this.m_world.CreateBody(bd);
body1.CreateFixture(fixtureDef);
{
sd=new Box2D.Collision.Shapes.b2PolygonShape();
sd.SetAsBox(7.5/this.m_physScale,30.0/this.m_physScale);
fixtureDef.shape=sd;
fixtureDef.density=1.0;
bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd.position.Set(500/this.m_physScale,500/2/this.m_physScale);
body=this.m_world.CreateBody(bd);
body.CreateFixture(fixtureDef);
var ljd=new Box2D.Dynamics.Joints.b2LineJointDef();
ljd.Initialize(ground,body,body.GetPosition(),new Box2D.Common.Math.b2Vec2(0.4,0.6));
ljd.lowerTranslation=-1;
ljd.upperTranslation=1;
ljd.enableLimit=true;
ljd.maxMotorForce=1;
ljd.motorSpeed=0;
ljd.enableMotor=true;
this.m_world.CreateJoint(ljd);
}
{
var fjd=new Box2D.Dynamics.Joints.b2FrictionJointDef();
fjd.Initialize(circleBody,this.m_world.GetGroundBody(),circleBody.GetPosition());
fjd.collideConnected=true;
fjd.maxForce=200;
this.m_world.CreateJoint(fjd);
}
if(false)
{
var wjd=new Box2D.Dynamics.Joints.b2WeldJointDef();
wjd.Initialize(circleBody,body,circleBody.GetPosition());
this.m_world.CreateJoint(wjd);
}
},
"private var",{m_joint1:null},
"private var",{m_joint2:null},
"public var",{m_gJoint1:null},
"public var",{m_gJoint2:null},
"public var",{m_gJoint3:null},
"public var",{m_gJoint4:null},
"public var",{m_gJoint5:null},
];},[],["TestBed.Test","Main","Box2D.Dynamics.b2FixtureDef","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.Joints.b2RevoluteJointDef","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Box2D.Common.Math.b2Vec2","Math","Box2D.Dynamics.Joints.b2RevoluteJoint","Box2D.Dynamics.Joints.b2PrismaticJointDef","Box2D.Dynamics.Joints.b2PrismaticJoint","Box2D.Collision.Shapes.b2CircleShape","Box2D.Dynamics.Joints.b2GearJointDef","Box2D.Dynamics.Joints.b2GearJoint","Box2D.Dynamics.Joints.b2PulleyJointDef","Box2D.Dynamics.Joints.b2PulleyJoint","Box2D.Dynamics.Joints.b2LineJointDef","Box2D.Dynamics.Joints.b2FrictionJointDef","Box2D.Dynamics.Joints.b2WeldJointDef"], "0.8.0", "0.8.1"
);
// class TestBed.TestOneSidedPlatform
joo.classLoader.prepare(
"package TestBed",
"public class TestOneSidedPlatform extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main);},
"public function TestOneSidedPlatform",function(){this.super$2();
Main.m_aboutText.text="One Sided Platform\n"+
"Press: (c) create a shape, (d) destroy a shape.";
var bd;
var body;
{
bd=new Box2D.Dynamics.b2BodyDef();
bd.position.Set(10.0,10.0);
body=this.m_world.CreateBody(bd);
var polygon=Box2D.Collision.Shapes.b2PolygonShape.AsBox(3.0,0.5);
this.m_platform=body.CreateFixture2(polygon,0.0);
this.m_bottom=bd.position.y+0.5;
this.m_top=bd.position.y-0.5;
}
{
bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd.position.Set(10.0,12.0);
body=this.m_world.CreateBody(bd);
this.m_radius=0.5;
var circle=new Box2D.Collision.Shapes.b2CircleShape(this.m_radius);
this.m_character=body.CreateFixture2(circle,1.0);
this.m_state=$$private.e_unknown;
}
this.m_world.SetContactListener(new TestBed.TestOneSidedPlatform.ContactListener(this));
},
"static private var",{e_unknown:0},
"static private var",{e_above:1},
"static private var",{e_below:2},
"public var",{m_radius:NaN},
"public var",{m_top:NaN},
"public var",{m_bottom:NaN},
"public var",{m_state:0},
"public var",{m_platform:null},
"public var",{m_character:null},

"class ContactListener extends Box2D.Dynamics.b2EmptyContactListener",2,function($$private){;return[

"private var",{test:null},
"public function ContactListener",function(test)
{this.super$2();
this.test$2=test;
},
"override public function PreSolve",function(contact,oldManifold)
{
var fixtureA=contact.GetFixtureA();
var fixtureB=contact.GetFixtureB();
if(fixtureA!=this.test$2.m_platform&&fixtureA!=this.test$2.m_character)
return;
if(fixtureB!=this.test$2.m_platform&&fixtureB!=this.test$2.m_character)
return;
var position=this.test$2.m_character.GetBody().GetPosition();
if(position.y>this.test$2.m_top)
contact.SetEnabled(false);
},
];},[],];},[],["TestBed.Test","Main","Box2D.Dynamics.b2BodyDef","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.b2Body","Box2D.Collision.Shapes.b2CircleShape","Box2D.Dynamics.b2EmptyContactListener"], "0.8.0", "0.8.1"
);
// class TestBed.TestRagdoll
joo.classLoader.prepare(
"package TestBed",
"public class TestRagdoll extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main,Math);},
"public function TestRagdoll",function(){this.super$2();
Main.m_aboutText.text="Ragdolls";
var circ;
var box;
var bd=new Box2D.Dynamics.b2BodyDef();
var jd=new Box2D.Dynamics.Joints.b2RevoluteJointDef();
var fixtureDef=new Box2D.Dynamics.b2FixtureDef();
for(var i=0;i<2;i++){
var startX=70+Math.random()*20+480*i;
var startY=20+Math.random()*50;
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
circ=new Box2D.Collision.Shapes.b2CircleShape(12.5/this.m_physScale);
fixtureDef.shape=circ;
fixtureDef.density=1.0;
fixtureDef.friction=0.4;
fixtureDef.restitution=0.3;
bd.position.Set(startX/this.m_physScale,startY/this.m_physScale);
var head=this.m_world.CreateBody(bd);
head.CreateFixture(fixtureDef);
head.ApplyLinearImpulse(new Box2D.Common.Math.b2Vec2(Math.random()*100-50,Math.random()*100-50),head.GetWorldCenter());
box=new Box2D.Collision.Shapes.b2PolygonShape();
box.SetAsBox(15/this.m_physScale,10/this.m_physScale);
fixtureDef.shape=box;
fixtureDef.density=1.0;
fixtureDef.friction=0.4;
fixtureDef.restitution=0.1;
bd.position.Set(startX/this.m_physScale,(startY+28)/this.m_physScale);
var torso1=this.m_world.CreateBody(bd);
torso1.CreateFixture(fixtureDef);
box=new Box2D.Collision.Shapes.b2PolygonShape();
box.SetAsBox(15/this.m_physScale,10/this.m_physScale);
fixtureDef.shape=box;
bd.position.Set(startX/this.m_physScale,(startY+43)/this.m_physScale);
var torso2=this.m_world.CreateBody(bd);
torso2.CreateFixture(fixtureDef);
box.SetAsBox(15/this.m_physScale,10/this.m_physScale);
fixtureDef.shape=box;
bd.position.Set(startX/this.m_physScale,(startY+58)/this.m_physScale);
var torso3=this.m_world.CreateBody(bd);
torso3.CreateFixture(fixtureDef);
fixtureDef.density=1.0;
fixtureDef.friction=0.4;
fixtureDef.restitution=0.1;
box=new Box2D.Collision.Shapes.b2PolygonShape();
box.SetAsBox(18/this.m_physScale,6.5/this.m_physScale);
fixtureDef.shape=box;
bd.position.Set((startX-30)/this.m_physScale,(startY+20)/this.m_physScale);
var upperArmL=this.m_world.CreateBody(bd);
upperArmL.CreateFixture(fixtureDef);
box=new Box2D.Collision.Shapes.b2PolygonShape();
box.SetAsBox(18/this.m_physScale,6.5/this.m_physScale);
fixtureDef.shape=box;
bd.position.Set((startX+30)/this.m_physScale,(startY+20)/this.m_physScale);
var upperArmR=this.m_world.CreateBody(bd);
upperArmR.CreateFixture(fixtureDef);
fixtureDef.density=1.0;
fixtureDef.friction=0.4;
fixtureDef.restitution=0.1;
box=new Box2D.Collision.Shapes.b2PolygonShape();
box.SetAsBox(17/this.m_physScale,6/this.m_physScale);
fixtureDef.shape=box;
bd.position.Set((startX-57)/this.m_physScale,(startY+20)/this.m_physScale);
var lowerArmL=this.m_world.CreateBody(bd);
lowerArmL.CreateFixture(fixtureDef);
box=new Box2D.Collision.Shapes.b2PolygonShape();
box.SetAsBox(17/this.m_physScale,6/this.m_physScale);
fixtureDef.shape=box;
bd.position.Set((startX+57)/this.m_physScale,(startY+20)/this.m_physScale);
var lowerArmR=this.m_world.CreateBody(bd);
lowerArmR.CreateFixture(fixtureDef);
fixtureDef.density=1.0;
fixtureDef.friction=0.4;
fixtureDef.restitution=0.1;
box=new Box2D.Collision.Shapes.b2PolygonShape();
box.SetAsBox(7.5/this.m_physScale,22/this.m_physScale);
fixtureDef.shape=box;
bd.position.Set((startX-8)/this.m_physScale,(startY+85)/this.m_physScale);
var upperLegL=this.m_world.CreateBody(bd);
upperLegL.CreateFixture(fixtureDef);
box=new Box2D.Collision.Shapes.b2PolygonShape();
box.SetAsBox(7.5/this.m_physScale,22/this.m_physScale);
fixtureDef.shape=box;
bd.position.Set((startX+8)/this.m_physScale,(startY+85)/this.m_physScale);
var upperLegR=this.m_world.CreateBody(bd);
upperLegR.CreateFixture(fixtureDef);
fixtureDef.density=1.0;
fixtureDef.friction=0.4;
fixtureDef.restitution=0.1;
box=new Box2D.Collision.Shapes.b2PolygonShape();
box.SetAsBox(6/this.m_physScale,20/this.m_physScale);
fixtureDef.shape=box;
bd.position.Set((startX-8)/this.m_physScale,(startY+120)/this.m_physScale);
var lowerLegL=this.m_world.CreateBody(bd);
lowerLegL.CreateFixture(fixtureDef);
box=new Box2D.Collision.Shapes.b2PolygonShape();
box.SetAsBox(6/this.m_physScale,20/this.m_physScale);
fixtureDef.shape=box;
bd.position.Set((startX+8)/this.m_physScale,(startY+120)/this.m_physScale);
var lowerLegR=this.m_world.CreateBody(bd);
lowerLegR.CreateFixture(fixtureDef);
jd.enableLimit=true;
jd.lowerAngle=-40/(180/Math.PI);
jd.upperAngle=40/(180/Math.PI);
jd.Initialize(torso1,head,new Box2D.Common.Math.b2Vec2(startX/this.m_physScale,(startY+15)/this.m_physScale));
this.m_world.CreateJoint(jd);
jd.lowerAngle=-85/(180/Math.PI);
jd.upperAngle=130/(180/Math.PI);
jd.Initialize(torso1,upperArmL,new Box2D.Common.Math.b2Vec2((startX-18)/this.m_physScale,(startY+20)/this.m_physScale));
this.m_world.CreateJoint(jd);
jd.lowerAngle=-130/(180/Math.PI);
jd.upperAngle=85/(180/Math.PI);
jd.Initialize(torso1,upperArmR,new Box2D.Common.Math.b2Vec2((startX+18)/this.m_physScale,(startY+20)/this.m_physScale));
this.m_world.CreateJoint(jd);
jd.lowerAngle=-130/(180/Math.PI);
jd.upperAngle=10/(180/Math.PI);
jd.Initialize(upperArmL,lowerArmL,new Box2D.Common.Math.b2Vec2((startX-45)/this.m_physScale,(startY+20)/this.m_physScale));
this.m_world.CreateJoint(jd);
jd.lowerAngle=-10/(180/Math.PI);
jd.upperAngle=130/(180/Math.PI);
jd.Initialize(upperArmR,lowerArmR,new Box2D.Common.Math.b2Vec2((startX+45)/this.m_physScale,(startY+20)/this.m_physScale));
this.m_world.CreateJoint(jd);
jd.lowerAngle=-15/(180/Math.PI);
jd.upperAngle=15/(180/Math.PI);
jd.Initialize(torso1,torso2,new Box2D.Common.Math.b2Vec2(startX/this.m_physScale,(startY+35)/this.m_physScale));
this.m_world.CreateJoint(jd);
jd.Initialize(torso2,torso3,new Box2D.Common.Math.b2Vec2(startX/this.m_physScale,(startY+50)/this.m_physScale));
this.m_world.CreateJoint(jd);
jd.lowerAngle=-25/(180/Math.PI);
jd.upperAngle=45/(180/Math.PI);
jd.Initialize(torso3,upperLegL,new Box2D.Common.Math.b2Vec2((startX-8)/this.m_physScale,(startY+72)/this.m_physScale));
this.m_world.CreateJoint(jd);
jd.lowerAngle=-45/(180/Math.PI);
jd.upperAngle=25/(180/Math.PI);
jd.Initialize(torso3,upperLegR,new Box2D.Common.Math.b2Vec2((startX+8)/this.m_physScale,(startY+72)/this.m_physScale));
this.m_world.CreateJoint(jd);
jd.lowerAngle=-25/(180/Math.PI);
jd.upperAngle=115/(180/Math.PI);
jd.Initialize(upperLegL,lowerLegL,new Box2D.Common.Math.b2Vec2((startX-8)/this.m_physScale,(startY+105)/this.m_physScale));
this.m_world.CreateJoint(jd);
jd.lowerAngle=-115/(180/Math.PI);
jd.upperAngle=25/(180/Math.PI);
jd.Initialize(upperLegR,lowerLegR,new Box2D.Common.Math.b2Vec2((startX+8)/this.m_physScale,(startY+105)/this.m_physScale));
this.m_world.CreateJoint(jd);
}
bd.type=Box2D.Dynamics.b2Body.b2_staticBody;
fixtureDef.density=0.0;
fixtureDef.friction=0.4;
fixtureDef.restitution=0.3;
for(var j=1;j<=10;j++){
box=new Box2D.Collision.Shapes.b2PolygonShape();
box.SetAsBox((10*j)/this.m_physScale,10/this.m_physScale);
fixtureDef.shape=box;
bd.position.Set((10*j)/this.m_physScale,(150+20*j)/this.m_physScale);
head=this.m_world.CreateBody(bd);
head.CreateFixture(fixtureDef);
}
for(var k=1;k<=10;k++){
box=new Box2D.Collision.Shapes.b2PolygonShape();
box.SetAsBox((10*k)/this.m_physScale,10/this.m_physScale);
fixtureDef.shape=box;
bd.position.Set((640-10*k)/this.m_physScale,(150+20*k)/this.m_physScale);
head=this.m_world.CreateBody(bd);
head.CreateFixture(fixtureDef);
}
box=new Box2D.Collision.Shapes.b2PolygonShape();
box.SetAsBox(30/this.m_physScale,40/this.m_physScale);
fixtureDef.shape=box;
bd.position.Set(320/this.m_physScale,320/this.m_physScale);
head=this.m_world.CreateBody(bd);
head.CreateFixture(fixtureDef);
},
];},[],["TestBed.Test","Main","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.Joints.b2RevoluteJointDef","Box2D.Dynamics.b2FixtureDef","Math","Box2D.Dynamics.b2Body","Box2D.Collision.Shapes.b2CircleShape","Box2D.Common.Math.b2Vec2","Box2D.Collision.Shapes.b2PolygonShape"], "0.8.0", "0.8.1"
);
// class TestBed.TestRaycast
joo.classLoader.prepare(
"package TestBed",
"public class TestRaycast extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main,Math);},
"public var",{laser:null},
"public function TestRaycast",function(){this.super$2();
Main.m_aboutText.text="Raycast";
this.m_world.SetGravity(new Box2D.Common.Math.b2Vec2(0,0));
var ground=this.m_world.GetGroundBody();
var box=new Box2D.Collision.Shapes.b2PolygonShape();
box.SetAsBox(30/this.m_physScale,4/this.m_physScale);
var fd=new Box2D.Dynamics.b2FixtureDef();
fd.shape=box;
fd.density=4;
fd.friction=0.4;
fd.restitution=0.3;
fd.userData="laser";
var bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd.position.Set(320/this.m_physScale,150/this.m_physScale);
bd.position.Set(40/this.m_physScale,150/this.m_physScale);
this.laser=this.m_world.CreateBody(bd);
this.laser.CreateFixture(fd);
this.laser.SetAngle(0.5);
this.laser.SetAngle(Math.PI);
var circle=new Box2D.Collision.Shapes.b2CircleShape(30/this.m_physScale);
fd.shape=circle;
fd.density=4;
fd.friction=0.4;
fd.restitution=0.3;
fd.userData="circle";
bd.position.Set(100/this.m_physScale,100/this.m_physScale);
var body=this.m_world.CreateBody(bd);
body.CreateFixture(fd);
},
"public override function Update",function(){
this.Update$2();
var p1=this.laser.GetWorldPoint(new Box2D.Common.Math.b2Vec2(30.1/this.m_physScale,0));
var p2=this.laser.GetWorldPoint(new Box2D.Common.Math.b2Vec2(130.1/this.m_physScale,0));
var f=this.m_world.RayCastOne(p1,p2);
var lambda=1;
if(f)
{
var input=new Box2D.Collision.b2RayCastInput(p1,p2);
var output=new Box2D.Collision.b2RayCastOutput();
f.RayCast(output,input);
lambda=output.fraction;
}
this.m_sprite.graphics.lineStyle(1,0xff0000,1);
this.m_sprite.graphics.moveTo(p1.x*this.m_physScale,p1.y*this.m_physScale);
this.m_sprite.graphics.lineTo((p2.x*lambda+(1-lambda)*p1.x)*this.m_physScale,
(p2.y*lambda+(1-lambda)*p1.y)*this.m_physScale);
},
];},[],["TestBed.Test","Main","Box2D.Common.Math.b2Vec2","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.b2FixtureDef","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Math","Box2D.Collision.Shapes.b2CircleShape","Box2D.Collision.b2RayCastInput","Box2D.Collision.b2RayCastOutput"], "0.8.0", "0.8.1"
);
// class TestBed.TestSensor
joo.classLoader.prepare(
"package TestBed",
"public class TestSensor extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Main,Box2D.Dynamics.b2Body);},
"public function TestSensor",function(){this.super$2();
var fd=new Box2D.Dynamics.b2FixtureDef();
fd.shape=new Box2D.Collision.Shapes.b2CircleShape(.5);
fd.density=1.0;
var bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
for(var i=0;i<8;i++)
{
bd.position.Set(4+i*1.5,5);
var body=this.m_world.CreateBody(bd);
body.CreateFixture(fd);
}
bd.type=Box2D.Dynamics.b2Body.b2_staticBody;
bd.position.Set(10,6);
var sensorBody=this.m_world.CreateBody(bd);
var fixture=sensorBody.CreateFixture2(new Box2D.Collision.Shapes.b2CircleShape(2),0.0);
fixture.SetSensor(true);
var controller=new TestBed.TestSensor.MyController();
controller.position.SetV(bd.position);
controller.SetBodyIterable(new Box2D.Dynamics.Controllers.b2TouchingBodyIterable(sensorBody));
this.m_world.AddController(controller);
Main.m_aboutText.text="Sensor";
},

"internal class MyController extends Box2D.Dynamics.Controllers.b2Controller",2,function($$private){;return[

"public var",{position:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{strength:1.0},
"override public function Step",function(step)
{
var iterator=this.m_bodyIterable.GetIterator();
while(iterator.HasNext())
{
var body=iterator.Next();
var d=Box2D.Common.Math.b2Math.SubtractVV(this.position,body.GetPosition());
d.Multiply(this.strength);
body.ApplyForce(d,body.GetPosition());
}
},
"public function MyController",function MyController$(){this.super$2();this.position=this.position();}];},[],];},[],["TestBed.Test","Box2D.Dynamics.b2FixtureDef","Box2D.Collision.Shapes.b2CircleShape","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Box2D.Dynamics.Controllers.b2TouchingBodyIterable","Main","Box2D.Dynamics.Controllers.b2Controller","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Math"], "0.8.0", "0.8.1"
);
// class TestBed.TestStack
joo.classLoader.prepare(
"package TestBed",
"public class TestStack extends TestBed.Test",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main);},
"public function TestStack",function(){this.super$2();
Main.m_aboutText.text="Stacked Boxes";
var fd=new Box2D.Dynamics.b2FixtureDef();
var sd=new Box2D.Collision.Shapes.b2PolygonShape();
var bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
var b;
fd.density=1.0;
fd.friction=0.5;
fd.restitution=0.1;
fd.shape=sd;
var i;
for(i=0;i<10;i++){
sd.SetAsBox((10)/this.m_physScale,(10)/this.m_physScale);
bd.position.Set((640/2+100)/this.m_physScale,(360-5-i*25)/this.m_physScale);
b=this.m_world.CreateBody(bd);
b.CreateFixture(fd);
}
for(i=0;i<10;i++){
sd.SetAsBox((10)/this.m_physScale,(10)/this.m_physScale);
bd.position.Set((640/2-0+Math.random()*0.02-0.01)/this.m_physScale,(360-5-i*25)/this.m_physScale);
b=this.m_world.CreateBody(bd);
b.CreateFixture(fd);
}
for(i=0;i<10;i++){
sd.SetAsBox((10)/this.m_physScale,(10)/this.m_physScale);
bd.position.Set((640/2+200+Math.random()*0.02-0.01)/this.m_physScale,(360-5-i*25)/this.m_physScale);
b=this.m_world.CreateBody(bd);
b.CreateFixture(fd);
}
var vxs=[new Box2D.Common.Math.b2Vec2(0,0),
new Box2D.Common.Math.b2Vec2(0,-100/this.m_physScale),
new Box2D.Common.Math.b2Vec2(200/this.m_physScale,0)];
sd.SetAsArray(vxs,vxs.length);
fd.density=0;
bd.type=Box2D.Dynamics.b2Body.b2_staticBody;
bd.userData="ramp";
bd.position.Set(0,360/this.m_physScale);
b=this.m_world.CreateBody(bd);
b.CreateFixture(fd);
var cd=new Box2D.Collision.Shapes.b2CircleShape();
cd.m_radius=40/this.m_physScale;
fd.density=2;
fd.restitution=0.2;
fd.friction=0.5;
fd.shape=cd;
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd.userData="ball";
bd.position.Set(50/this.m_physScale,100/this.m_physScale);
b=this.m_world.CreateBody(bd);
b.CreateFixture(fd);
},
];},[],["TestBed.Test","Main","Box2D.Dynamics.b2FixtureDef","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Math","Box2D.Common.Math.b2Vec2","Box2D.Collision.Shapes.b2CircleShape"], "0.8.0", "0.8.1"
);
// class TestBed.TestTheoJansen
joo.classLoader.prepare(
"package TestBed",
"public class TestTheoJansen extends TestBed.Test",2,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Main,Math);},
"public function TestTheoJansen",function(){this.super$2();this.m_offset$2=this.m_offset$2();
Main.m_aboutText.text="Theo Jansen Walker";
this.tScale$2=this.m_physScale*2;
this.m_offset$2.Set(120.0/this.m_physScale,250/this.m_physScale);
this.m_motorSpeed$2=-2.0;
this.m_motorOn$2=true;
var pivot=new Box2D.Common.Math.b2Vec2(0.0,-24.0/this.tScale$2);
var pd;
var cd;
var fd;
var bd;
var body;
for(var i=0;i<40;++i)
{
cd=new Box2D.Collision.Shapes.b2CircleShape(7.5/this.tScale$2);
bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd.position.Set((Math.random()*620+10)/this.m_physScale,350/this.m_physScale);
body=this.m_world.CreateBody(bd);
body.CreateFixture2(cd,1.0);
}
{
pd=new Box2D.Collision.Shapes.b2PolygonShape();
pd.SetAsBox(75/this.tScale$2,30/this.tScale$2);
fd=new Box2D.Dynamics.b2FixtureDef();
fd.shape=pd;
fd.density=1.0;
fd.filter.groupIndex=-1;
bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd.position=Box2D.Common.Math.b2Math.AddVV(pivot,this.m_offset$2);
this.m_chassis$2=this.m_world.CreateBody(bd);
this.m_chassis$2.CreateFixture(fd);
}
{
cd=new Box2D.Collision.Shapes.b2CircleShape(48/this.tScale$2);
fd=new Box2D.Dynamics.b2FixtureDef();
fd.shape=cd;
fd.density=1.0;
fd.filter.groupIndex=-1;
bd=new Box2D.Dynamics.b2BodyDef();
bd.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd.position=Box2D.Common.Math.b2Math.AddVV(pivot,this.m_offset$2);
this.m_wheel$2=this.m_world.CreateBody(bd);
this.m_wheel$2.CreateFixture(fd);
}
{
var jd=new Box2D.Dynamics.Joints.b2RevoluteJointDef();
var po=pivot.Copy();
po.Add(this.m_offset$2);
jd.Initialize(this.m_wheel$2,this.m_chassis$2,po);
jd.collideConnected=false;
jd.motorSpeed=this.m_motorSpeed$2;
jd.maxMotorTorque=400.0;
jd.enableMotor=this.m_motorOn$2;
this.m_motorJoint$2=as(this.m_world.CreateJoint(jd),Box2D.Dynamics.Joints.b2RevoluteJoint);
}
var wheelAnchor;
wheelAnchor=new Box2D.Common.Math.b2Vec2(0.0,24.0/this.tScale$2);
wheelAnchor.Add(pivot);
this.CreateLeg$2(-1.0,wheelAnchor);
this.CreateLeg$2(1.0,wheelAnchor);
this.m_wheel$2.SetPositionAndAngle(this.m_wheel$2.GetPosition(),120.0*Math.PI/180.0);
this.CreateLeg$2(-1.0,wheelAnchor);
this.CreateLeg$2(1.0,wheelAnchor);
this.m_wheel$2.SetPositionAndAngle(this.m_wheel$2.GetPosition(),-120.0*Math.PI/180.0);
this.CreateLeg$2(-1.0,wheelAnchor);
this.CreateLeg$2(1.0,wheelAnchor);
},
"private function CreateLeg",function(s,wheelAnchor){
var p1=new Box2D.Common.Math.b2Vec2(162*s/this.tScale$2,183/this.tScale$2);
var p2=new Box2D.Common.Math.b2Vec2(216*s/this.tScale$2,36/this.tScale$2);
var p3=new Box2D.Common.Math.b2Vec2(129*s/this.tScale$2,57/this.tScale$2);
var p4=new Box2D.Common.Math.b2Vec2(93*s/this.tScale$2,-24/this.tScale$2);
var p5=new Box2D.Common.Math.b2Vec2(180*s/this.tScale$2,-45/this.tScale$2);
var p6=new Box2D.Common.Math.b2Vec2(75*s/this.tScale$2,-111/this.tScale$2);
var sd1=new Box2D.Collision.Shapes.b2PolygonShape();
var sd2=new Box2D.Collision.Shapes.b2PolygonShape();
var fd1=new Box2D.Dynamics.b2FixtureDef();
var fd2=new Box2D.Dynamics.b2FixtureDef();
fd1.shape=sd1;
fd2.shape=sd2;
fd1.filter.groupIndex=-1;
fd2.filter.groupIndex=-1;
fd1.density=1.0;
fd2.density=1.0;
if(s>0.0)
{
sd1.SetAsArray([p3,p2,p1]);
sd2.SetAsArray([
Box2D.Common.Math.b2Math.SubtractVV(p6,p4),
Box2D.Common.Math.b2Math.SubtractVV(p5,p4),
new Box2D.Common.Math.b2Vec2()
]);
}
else
{
sd1.SetAsArray([p2,p3,p1]);
sd2.SetAsArray([
Box2D.Common.Math.b2Math.SubtractVV(p5,p4),
Box2D.Common.Math.b2Math.SubtractVV(p6,p4),
new Box2D.Common.Math.b2Vec2()
]);
}
var bd1=new Box2D.Dynamics.b2BodyDef();
bd1.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
var bd2=new Box2D.Dynamics.b2BodyDef();
bd2.type=Box2D.Dynamics.b2Body.b2_dynamicBody;
bd1.position.SetV(this.m_offset$2);
bd2.position=Box2D.Common.Math.b2Math.AddVV(p4,this.m_offset$2);
bd1.angularDamping=10.0;
bd2.angularDamping=10.0;
var body1=this.m_world.CreateBody(bd1);
var body2=this.m_world.CreateBody(bd2);
body1.CreateFixture(fd1);
body2.CreateFixture(fd2);
var djd=new Box2D.Dynamics.Joints.b2DistanceJointDef();
djd.dampingRatio=0.5;
djd.frequencyHz=10.0;
djd.Initialize(body1,body2,Box2D.Common.Math.b2Math.AddVV(p2,this.m_offset$2),Box2D.Common.Math.b2Math.AddVV(p5,this.m_offset$2));
this.m_world.CreateJoint(djd);
djd.Initialize(body1,body2,Box2D.Common.Math.b2Math.AddVV(p3,this.m_offset$2),Box2D.Common.Math.b2Math.AddVV(p4,this.m_offset$2));
this.m_world.CreateJoint(djd);
djd.Initialize(body1,this.m_wheel$2,Box2D.Common.Math.b2Math.AddVV(p3,this.m_offset$2),Box2D.Common.Math.b2Math.AddVV(wheelAnchor,this.m_offset$2));
this.m_world.CreateJoint(djd);
djd.Initialize(body2,this.m_wheel$2,Box2D.Common.Math.b2Math.AddVV(p6,this.m_offset$2),Box2D.Common.Math.b2Math.AddVV(wheelAnchor,this.m_offset$2));
this.m_world.CreateJoint(djd);
var rjd=new Box2D.Dynamics.Joints.b2RevoluteJointDef();
rjd.Initialize(body2,this.m_chassis$2,Box2D.Common.Math.b2Math.AddVV(p4,this.m_offset$2));
this.m_world.CreateJoint(rjd);
},
"public override function Update",function(){
if(General.Input.isKeyPressed(65)){
this.m_chassis$2.SetAwake(true);
this.m_motorJoint$2.SetMotorSpeed(-this.m_motorSpeed$2);
}
if(General.Input.isKeyPressed(83)){
this.m_chassis$2.SetAwake(true);
this.m_motorJoint$2.SetMotorSpeed(0.0);
}
if(General.Input.isKeyPressed(68)){
this.m_chassis$2.SetAwake(true);
this.m_motorJoint$2.SetMotorSpeed(this.m_motorSpeed$2);
}
if(General.Input.isKeyPressed(77)){
this.m_chassis$2.SetAwake(true);
this.m_motorJoint$2.EnableMotor(!this.m_motorJoint$2.IsMotorEnabled());
}
this.Update$2();
},
"private var",{tScale:NaN},
"private var",{m_offset:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_chassis:null},
"private var",{m_wheel:null},
"private var",{m_motorJoint:null},
"private var",{m_motorOn:true},
"private var",{m_motorSpeed:NaN},
];},[],["TestBed.Test","Main","Box2D.Common.Math.b2Vec2","Box2D.Collision.Shapes.b2CircleShape","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Math","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.b2FixtureDef","Box2D.Common.Math.b2Math","Box2D.Dynamics.Joints.b2RevoluteJointDef","Box2D.Dynamics.Joints.b2RevoluteJoint","Box2D.Dynamics.Joints.b2DistanceJointDef","General.Input"], "0.8.0", "0.8.1"
);
