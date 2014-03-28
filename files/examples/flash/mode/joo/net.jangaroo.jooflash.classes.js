// class flash.accessibility.Accessibility
joo.classLoader.prepare("package flash.accessibility",
"public final class Accessibility",1,function($$private){;return[
"public static function get active",function(){
return false;
},
"public static function sendEvent",function(source,childID,eventType,nonHTML){switch(arguments.length){case 0:case 1:case 2:case 3:nonHTML=false;}
throw new flash.errors.IllegalOperationError();
},
"public static function updateProperties",function(){
throw new flash.errors.IllegalOperationError();
},
];},["active","sendEvent","updateProperties"],["flash.errors.IllegalOperationError"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.accessibility.AccessibilityImplementation
joo.classLoader.prepare("package flash.accessibility",
"public class AccessibilityImplementation",1,function($$private){;return[
"public var",{errno:0},
"public var",{stub:false},
"public function AccessibilityImplementation",function(){
throw new Error('not implemented');
},
"public function accDoDefaultAction",function(childID){
throw new Error('not implemented');
},
"public function accLocation",function(childID){
throw new Error('not implemented');
},
"public function accSelect",function(operation,childID){
throw new Error('not implemented');
},
"public function get_accDefaultAction",function(childID){
throw new Error('not implemented');
},
"public function get_accFocus",function(){
throw new Error('not implemented');
},
"public function get_accName",function(childID){
throw new Error('not implemented');
},
"public function get_accRole",function(childID){
throw new Error('not implemented');
},
"public function get_accSelection",function(){
throw new Error('not implemented');
},
"public function get_accState",function(childID){
throw new Error('not implemented');
},
"public function get_accValue",function(childID){
throw new Error('not implemented');
},
"public function getChildIDArray",function(){
throw new Error('not implemented');
},
"public function isLabeledBy",function(labelBounds){
throw new Error('not implemented');
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.accessibility.AccessibilityProperties
joo.classLoader.prepare("package flash.accessibility",
"public class AccessibilityProperties",1,function($$private){;return[
"public var",{description:null},
"public var",{forceSimple:false},
"public var",{name:null},
"public var",{noAutoLabeling:false},
"public var",{shortcut:null},
"public var",{silent:false},
"public function AccessibilityProperties",function(){
throw new Error('not implemented');
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.ActionScriptVersion
joo.classLoader.prepare("package flash.display",
"public final class ActionScriptVersion",1,function($$private){;return[
"public static const",{ACTIONSCRIPT2:2},
"public static const",{ACTIONSCRIPT3:3},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.AVM1Movie
joo.classLoader.prepare("package flash.display",
"public class AVM1Movie extends flash.display.DisplayObject",3,function($$private){;return[
];},[],["flash.display.DisplayObject"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.Bitmap
joo.classLoader.prepare("package flash.display",
"public class Bitmap extends flash.display.DisplayObject",3,function($$private){var $$bound=joo.boundMethod;return[
"public function get bitmapData",function(){
return this._bitmapData$3;
},
"public function set bitmapData",function(value){
if(this.listenerAdded$3){
this._bitmapData$3.removeElementChangeListener($$bound(this,"setElement"));
}
this._bitmapData$3=value;
if(this.listenerAdded$3){
this._bitmapData$3.addElementChangeListener($$bound(this,"setElement"));
}
},
"public function get pixelSnapping",function(){
return this._pixelSnapping$3;
},
"public function set pixelSnapping",function(value){
this._pixelSnapping$3=value;
},
"public function get smoothing",function(){
return this._smoothing$3;
},
"public function set smoothing",function(value){
this._smoothing$3=value;
},
"public function Bitmap",function(bitmapData,pixelSnapping,smoothing){switch(arguments.length){case 0:bitmapData=null;case 1:pixelSnapping="auto";case 2:smoothing=false;}
this._bitmapData$3=bitmapData;
this.super$3();
this._pixelSnapping$3=pixelSnapping;
this._smoothing$3=smoothing;
},
"override protected function createElement",function(){
if(!this.listenerAdded$3){
this._bitmapData$3.addElementChangeListener($$bound(this,"setElement"));
}
return this._bitmapData$3.getElement();
},
"override public function get height",function(){
return this._bitmapData$3.height*this.scaleY;
},
"override public function get width",function(){
return this._bitmapData$3.width*this.scaleX;
},
"private var",{_bitmapData:null},
"private var",{listenerAdded:false},
"private var",{_pixelSnapping:null},
"private var",{_smoothing:false},
];},[],["flash.display.DisplayObject"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.BitmapData
joo.classLoader.prepare("package flash.display",
"public class BitmapData implements flash.display.IBitmapDrawable",1,function($$private){var is=joo.is,as=joo.as;return[
"public native function get height",
"public function get rect",function(){
return new flash.geom.Rectangle(0,0,this.width,this.height);
},
"public native function get transparent",
"public native function get width",
"public function BitmapData",function(width,height,transparent,fillColor){switch(arguments.length){case 0:case 1:case 2:transparent=true;case 3:fillColor=0xFFFFFFFF;}this.elementChangeListeners$1=this.elementChangeListeners$1();
this.transparent=transparent;
this.width=width;
this.height=height;
this._alpha$1=transparent?(fillColor>>>24)/0xFF:1;
this._fillColor$1=fillColor&0xFFFFFF;
},
"public function applyFilter",function(sourceBitmapData,sourceRect,destPoint,filter){
throw new Error('not implemented');
},
"public function clone",function(){
throw new Error('not implemented');
},
"public function colorTransform",function(rect,colorTransform){
var context=this.getContext$1();
if(colorTransform.alphaOffset==0
&&colorTransform.redMultiplier>=0&&colorTransform.redMultiplier<=1
&&colorTransform.redMultiplier==colorTransform.greenMultiplier
&&colorTransform.redMultiplier==colorTransform.blueMultiplier
&&colorTransform.redMultiplier==colorTransform.alphaMultiplier){
if(colorTransform.redOffset>=0&&colorTransform.greenOffset>=0&&colorTransform.blueOffset>=0){
context.save();
context.setTransform(1,0,0,1,0,0);
var alpha=1;
if(colorTransform.redMultiplier==1){
context.globalCompositeOperation="lighter";
}else{
context.globalCompositeOperation="source-over";
alpha-=colorTransform.alphaMultiplier;
}
context.fillStyle="rgba("+
[colorTransform.redOffset,colorTransform.greenOffset,colorTransform.blueOffset,
alpha]
.join(",")+")";
context.fillRect(rect.x,rect.y,rect.width,rect.height);
context.restore();
return;
}
}
var input=context.getImageData(rect.x,rect.y,rect.width,rect.height);
var inputData=input.data;
var maps=colorTransform.getComponentMaps();
var i;
for(var m=0;m<4;++m){
var map=maps[m];
if(map){
for(i=inputData.length-4+m;i>=0;i-=4){
inputData[i]=map[inputData[i]];
}
}
}
context.putImageData(input,rect.x,rect.y);
},
"public function compare",function(otherBitmapData){
throw new Error('not implemented');
},
"public function copyChannel",function(sourceBitmapData,sourceRect,destPoint,sourceChannel,destChannel){
throw new Error('not implemented');
},
"public function copyPixels",function(sourceBitmapData,sourceRect,destPoint,
alphaBitmapData,alphaPoint,mergeAlpha){switch(arguments.length){case 0:case 1:case 2:case 3:alphaBitmapData=null;case 4:alphaPoint=null;case 5:mergeAlpha=false;}
var context;
var destRect=new flash.geom.Rectangle(destPoint.x,destPoint.y,sourceRect.width,sourceRect.height);
destRect=destRect.intersection(this.rect);
destRect.width=Math.floor(destRect.width);
destRect.height=Math.floor(destRect.height);
if(destRect.width>0&&destRect.height>0){
var sx=sourceRect.x+(destRect.left-destPoint.x);
var sy=sourceRect.y+(destRect.top-destPoint.y);
if(!sourceBitmapData.isCanvas$1){
if(destRect.equals(this.rect)&&(!this.isCanvas$1||!mergeAlpha)){
this._fillColor$1=sourceBitmapData._fillColor$1;
this._alpha$1=sourceBitmapData._alpha$1;
this.image$1=sourceBitmapData.image$1;
this.imageOffsetX$1=sx+sourceBitmapData.imageOffsetX$1;
this.imageOffsetY$1=sy+sourceBitmapData.imageOffsetY$1;
if(this.elem$1){
this.asDiv();
}
}else{
context=this.getContext$1();
if(sourceBitmapData._alpha$1>0){
context.fillStyle=flash.display.Graphics.toRGBA(sourceBitmapData._fillColor$1,mergeAlpha?sourceBitmapData._alpha$1:1);
context.fillRect(destRect.x,destRect.y,destRect.width,destRect.height);
}
if(sourceBitmapData.image$1){
context.drawImage(sourceBitmapData.image$1,sx+sourceBitmapData.imageOffsetX$1,sy+sourceBitmapData.imageOffsetY$1,
destRect.width,destRect.height,
destRect.left,destRect.top,destRect.width,destRect.height);
}
}
}else{
context=this.getContext$1();
if(mergeAlpha){
context.drawImage(sourceBitmapData.asCanvas$1(),sx,sy,destRect.width,destRect.height,
destRect.x,destRect.y,destRect.width,destRect.height);
}else{
var imageData=sourceBitmapData.getContext$1().getImageData(sx,sy,destRect.width,destRect.height);
context.putImageData(imageData,destRect.x,destRect.y);
}
}
}
},
"public function dispose",function(){
throw new Error('not implemented');
},
"public function draw",function(source,matrix,colorTransform,
blendMode,clipRect,smoothing){switch(arguments.length){case 0:case 1:matrix=null;case 2:colorTransform=null;case 3:blendMode=null;case 4:clipRect=null;case 5:smoothing=false;}
var bitmapData;
if(is(source,flash.display.Bitmap)){
bitmapData=(source).bitmapData;
}else{
bitmapData=as(source,flash.display.BitmapData);
}
var element=bitmapData?
bitmapData.image$1||bitmapData.elem$1||bitmapData.asDiv():(source).getElement();
var context=this.getContext$1();
if(matrix){
context.save();
context.setTransform(matrix.a,matrix.b,matrix.c,matrix.d,matrix.tx,matrix.ty);
}
if(is(element,js.HTMLImageElement)||is(element,js.HTMLCanvasElement)){
context.drawImage(element,0,0);
}else{
if(element.style.backgroundColor){
context.fillStyle=element.style.backgroundColor;
context.fillRect(0,0,source['width'],source['height']);
}
var text=element['textContent'];
if(text){
context.fillStyle=element.style.color;
context.font=element.style.font;
context.textBaseline="top";
context.fillText(text,0,0);
}
}
if(matrix){
context.restore();
}
},
"public function fillRect",function(rect,color){
var alpha=(color>>24&0xFF)/0xFF;
color=color&0xFFFFFF;
if(!this.isCanvas$1&&rect.equals(this.rect)){
this._fillColor$1=color;
this._alpha$1=alpha;
this.image$1=null;
if(this.elem$1){
this.asDiv();
}
return;
}
var context=this.getContext$1();
context.save();
context.setTransform(1,0,0,1,0,0);
if(alpha==0){
context.clearRect(rect.x,rect.y,rect.width,rect.height);
}else{
context.fillStyle="rgba("+
[color>>16&0xFF,color>>8&0xFF,color&0xFF,alpha]
.join(",")+")";
context.globalCompositeOperation="copy";
context.fillRect(rect.x,rect.y,rect.width,rect.height);
}
context.restore();
context.globalCompositeOperation="source-over";
},
"public function floodFill",function(x,y,color){
throw new Error('not implemented');
},
"public function generateFilterRect",function(sourceRect,filter){
throw new Error('not implemented');
},
"public function getColorBoundsRect",function(mask,color,findColor){switch(arguments.length){case 0:case 1:case 2:findColor=true;}
throw new Error('not implemented');
},
"public function getPixel",function(x,y){
if(this.rect.contains(x,y)){
var data=this.getContext$1().getImageData(x,y,1,1).data;
return data[0]<<16|data[1]<<8|data[2];
}
return 0;
},
"public function getPixel32",function(x,y){
if(this.rect.contains(x,y)){
var data=this.getContext$1().getImageData(x,y,1,1).data;
return data[0]<<16|data[1]<<8|data[2]|data[3]<<24;
}
return 0;
},
"public function getPixels",function(rect){
throw new Error('not implemented');
},
"public function hitTest",function(firstPoint,firstAlphaThreshold,secondObject,secondBitmapDataPoint,secondAlphaThreshold){switch(arguments.length){case 0:case 1:case 2:case 3:secondBitmapDataPoint=null;case 4:secondAlphaThreshold=1;}
throw new Error('not implemented');
},
"public function lock",function(){
},
"public function merge",function(sourceBitmapData,sourceRect,destPoint,redMultiplier,greenMultiplier,blueMultiplier,alphaMultiplier){
throw new Error('not implemented');
},
"public function noise",function(randomSeed,low,high,channelOptions,grayScale){switch(arguments.length){case 0:case 1:low=0;case 2:high=255;case 3:channelOptions=7;case 4:grayScale=false;}
throw new Error('not implemented');
},
"public function paletteMap",function(sourceBitmapData,sourceRect,destPoint,redArray,greenArray,blueArray,alphaArray){switch(arguments.length){case 0:case 1:case 2:case 3:redArray=null;case 4:greenArray=null;case 5:blueArray=null;case 6:alphaArray=null;}
throw new Error('not implemented');
},
"public function perlinNoise",function(baseX,baseY,numOctaves,randomSeed,stitch,fractalNoise,channelOptions,grayScale,offsets){switch(arguments.length){case 0:case 1:case 2:case 3:case 4:case 5:case 6:channelOptions=7;case 7:grayScale=false;case 8:offsets=null;}
throw new Error('not implemented');
},
"public function pixelDissolve",function(sourceBitmapData,sourceRect,destPoint,randomSeed,numPixels,fillColor){switch(arguments.length){case 0:case 1:case 2:case 3:randomSeed=0;case 4:numPixels=0;case 5:fillColor=0;}
throw new Error('not implemented');
},
"public function scroll",function(x,y){
throw new Error('not implemented');
},
"public function setPixel",function(x,y,color){
if(this.rect.contains(x,y)){
var context=this.getContext$1();
var imageData=context.createImageData(1,1);
imageData.data[0]=color>>16&0xFF;
imageData.data[1]=color>>8&0xFF;
imageData.data[2]=color&0xFF;
imageData.data[3]=0xFF;
context.putImageData(imageData,x,y);
}
},
"public function setPixel32",function(x,y,color){
if(this.rect.contains(x,y)){
var context=this.getContext$1();
var imageData=context.createImageData(1,1);
imageData.data[0]=color>>16&0xFF;
imageData.data[1]=color>>8&0xFF;
imageData.data[2]=color&0xFF;
imageData.data[3]=color>>24&0xFF;
context.putImageData(imageData,x,y);
}
},
"public function setPixels",function(rect,inputByteArray){
throw new Error('not implemented');
},
"public function threshold",function(sourceBitmapData,sourceRect,destPoint,operation,threshold,color,mask,copySource){switch(arguments.length){case 0:case 1:case 2:case 3:case 4:case 5:color=0;case 6:mask=0xFFFFFFFF;case 7:copySource=false;}
throw new Error('not implemented');
},
"public function unlock",function(changeRect){switch(arguments.length){case 0:changeRect=null;}
},
"public native function set transparent",
"public native function set width",
"public native function set height",
"private const",{elementChangeListeners:function(){return([]);}},
"internal function getElement",function(){
if(!this.elem$1){
return this.asDiv();
}
return this.elem$1;
},
"public static function fromImg",function(img){
var bitmapData=new flash.display.BitmapData(img.width,img.height,true,0);
bitmapData.image$1=img;
return bitmapData;
},
"internal function getImage",function(){
if(this.image$1)
return this.image$1;
var img=new js.Image();
if(this.isCanvas$1){
img.src=(this.elem$1).toDataURL();
}else{
return null;
}
return img;
},
"public function asDiv",function(){
var url;
if(!this.elem$1||this.isCanvas$1){
if(this.isCanvas$1){
url=this.asCanvas$1().toDataURL();
}
this.isCanvas$1=false;
var div=(window.document.createElement("DIV"));
div.style.position="absolute";
div.style.width=this.width+"px";
div.style.height=this.height+"px";
this.changeElement$1(div);
}
if(this.image$1){
url=this.image$1.src;
}
this.elem$1.style.backgroundColor=flash.display.Graphics.toRGBA(this._fillColor$1,this._alpha$1);
this.elem$1.style.backgroundImage=url?"url('"+url+"')":"none";
return this.elem$1;
},
"private function getContext",function(){
return(this.asCanvas$1().getContext("2d"));
},
"private function asCanvas",function(){
if(!this.isCanvas$1){
this.isCanvas$1=true;
var canvas=(window.document.createElement("canvas"));
canvas.width=this.width;
canvas.height=this.height;
canvas.style.position="absolute";
var context=(canvas.getContext("2d"));
if(this._alpha$1>0||!this.transparent){
context.save();
context.fillStyle=flash.display.Graphics.toRGBA(this._fillColor$1,this._alpha$1);
context.fillRect(0,0,this.width,this.height);
context.restore();
}
if(this.image$1){
context.drawImage(this.image$1,this.imageOffsetX$1,this.imageOffsetY$1,this.width,this.height,0,0,this.width,this.height);
this.image$1=null;
}
this.changeElement$1(canvas);
}
return(this.elem$1);
},
"internal function addElementChangeListener",function(listener){
this.elementChangeListeners$1.push(listener);
},
"internal function removeElementChangeListener",function(listener){
var listenerIndex=this.elementChangeListeners$1.indexOf(listener);
if(listenerIndex!==-1){
this.elementChangeListeners$1.slice(listenerIndex,1);
}
},
"private function changeElement",function(elem){
this.elem$1=elem;
for(var i=0;i<this.elementChangeListeners$1.length;i++){
this.elementChangeListeners$1[i](elem);
}
},
"private var",{_fillColor:0},
"private var",{_alpha:NaN},
"private var",{elem:null},
"private var",{isCanvas:false},
"private var",{image:null},
"private var",{imageOffsetX:0},
"private var",{imageOffsetY:0},
];},["fromImg"],["flash.display.IBitmapDrawable","flash.geom.Rectangle","Error","Math","flash.display.Graphics","flash.display.Bitmap","flash.display.DisplayObject","js.HTMLImageElement","js.HTMLCanvasElement","js.Image","js.HTMLElement","js.CanvasRenderingContext2D"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.BitmapDataChannel
joo.classLoader.prepare("package flash.display",
"public final class BitmapDataChannel",1,function($$private){;return[
"public static const",{ALPHA:8},
"public static const",{BLUE:4},
"public static const",{GREEN:2},
"public static const",{RED:1},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.BlendMode
joo.classLoader.prepare("package flash.display",
"public final class BlendMode",1,function($$private){;return[
"public static const",{ADD:"add"},
"public static const",{ALPHA:"alpha"},
"public static const",{DARKEN:"darken"},
"public static const",{DIFFERENCE:"difference"},
"public static const",{ERASE:"erase"},
"public static const",{HARDLIGHT:"hardlight"},
"public static const",{INVERT:"invert"},
"public static const",{LAYER:"layer"},
"public static const",{LIGHTEN:"lighten"},
"public static const",{MULTIPLY:"multiply"},
"public static const",{NORMAL:"normal"},
"public static const",{OVERLAY:"overlay"},
"public static const",{SCREEN:"screen"},
"public static const",{SUBTRACT:"subtract"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.CapsStyle
joo.classLoader.prepare("package flash.display",
"public final class CapsStyle",1,function($$private){;return[
"public static const",{NONE:"none"},
"public static const",{ROUND:"round"},
"public static const",{SQUARE:"square"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.DisplayObject
joo.classLoader.prepare("package flash.display",
{Event:{name:"added",type:"flash.events.Event"}},
{Event:{name:"addedToStage",type:"flash.events.Event"}},
{Event:{name:"enterFrame",type:"flash.events.Event"}},
{Event:{name:"exitFrame",type:"flash.events.Event"}},
{Event:{name:"frameConstructed",type:"flash.events.Event"}},
{Event:{name:"removed",type:"flash.events.Event"}},
{Event:{name:"removedFromStage",type:"flash.events.Event"}},
{Event:{name:"render",type:"flash.events.Event"}},
"public class DisplayObject extends flash.events.EventDispatcher implements flash.display.IBitmapDrawable",2,function($$private){var $$bound=joo.boundMethod,trace=joo.trace;return[function(){joo.classLoader.init(flash.events.KeyboardEvent,flash.events.MouseEvent);},
"public function get accessibilityProperties",function(){
throw new Error('not implemented');
},
"public function set accessibilityProperties",function(value){
throw new Error('not implemented');
},
"public function get alpha",function(){
return this._alpha$2;
},
"public function set alpha",function(value){
this._alpha$2=value;
this.getElement().style.opacity=String(value);
},
"public function get blendMode",function(){
throw new Error('not implemented');
},
"public function set blendMode",function(value){
throw new Error('not implemented');
},
"public function get cacheAsBitmap",function(){
throw new Error('not implemented');
},
"public function set cacheAsBitmap",function(value){
throw new Error('not implemented');
},
"public function get filters",function(){
throw new Error('not implemented');
},
"public function set filters",function(value){
throw new Error('not implemented');
},
"public function get height",function(){
return this.getElement().offsetHeight;
},
"public function set height",function(value){
var style=this.getElement().style;
var oldHeight=this.height;
if(!isNaN(value)){
if(style.paddingTop){
value-=$$private.styleLengthToNumber(style.paddingTop);
}
if(style.paddingBottom){
value-=$$private.styleLengthToNumber(style.paddingBottom);
}
}
style.height=$$private.numberToStyleLength(value);
if(oldHeight&&value){
this.updateScale(this._scaleX$2,value/oldHeight);
}
},
"public function get loaderInfo",function(){
return new flash.display.LoaderInfo();
},
"public function get mask",function(){
throw new Error('not implemented');
},
"public function set mask",function(value){
throw new Error('not implemented');
},
"public function get mouseX",function(){
return this.stage?this.stage.mouseX/this._scaleX$2:NaN;
},
"public function get mouseY",function(){
return this.stage?this.stage.mouseY/this._scaleY$2:NaN;
},
"public native function get name",
"public native function set name",
"public function get opaqueBackground",function(){
throw new Error('not implemented');
},
"public function set opaqueBackground",function(value){
throw new Error('not implemented');
},
"public native function get parent",
"public function get root",function(){
var root=this;
while(root.parent){
root=root.parent;
}
return root;
},
"public function get rotation",function(){
throw new Error('not implemented');
},
"public function set rotation",function(value){
throw new Error('not implemented');
},
"public function get scale9Grid",function(){
throw new Error('not implemented');
},
"public function set scale9Grid",function(value){
throw new Error('not implemented');
},
"public function get scaleX",function(){
return this._scaleX$2;
},
"public function set scaleX",function(value){
var width=this.width;
if(width){
this.width=width*value/this._scaleX$2;
}else{
this.updateScale(value,this._scaleY$2);
}
},
"internal function updateScale",function(scaleX,scaleY){
this._scaleX$2=scaleX;
this._scaleY$2=scaleY;
},
"public function get scaleY",function(){
return this._scaleY$2;
},
"public function set scaleY",function(value){
var height=this.height;
if(height){
this.height=height*value/this._scaleY$2;
}else{
this.updateScale(this._scaleX$2,value);
}
},
"public function get scrollRect",function(){
throw new Error('not implemented');
},
"public function set scrollRect",function(value){
throw new Error('not implemented');
},
"public function get stage",function(){
return this.parent?this.parent.stage:null;
},
"public function get transform",function(){
if(!this._transform$2)
this._transform$2=new flash.geom.Transform(this);
return this._transform$2;
},
"public function set transform",function(value){
this._transform$2=value;
},
"public function get visible",function(){
return this._visible$2;
},
"public function set visible",function(value){
this._visible$2=value;
this.getElement().style.display=this._visible$2?"":"none";
},
"public function get width",function(){
return this.getElement().offsetWidth;
},
"public function set width",function(value){
var style=this.getElement().style;
var oldWidth=this.width;
if(!isNaN(value)){
if(style.paddingLeft){
value-=$$private.styleLengthToNumber(style.paddingLeft);
}
if(style.paddingRight){
value-=$$private.styleLengthToNumber(style.paddingRight);
}
}
style.width=$$private.numberToStyleLength(value);
if(oldWidth&&value){
this.updateScale(value/oldWidth,this._scaleY$2);
}
},
"public function get x",function(){
return this._x$2;
},
"public function set x",function(value){
this._x$2=value||0;
if(this._elem$2){
this._elem$2.style.left=this._x$2+"px";
}
},
"public function get y",function(){
return this._y$2;
},
"public function set y",function(value){
this._y$2=value||0;
if(this._elem$2){
this._elem$2.style.top=this._y$2+"px";
}
},
"public function getBounds",function(targetCoordinateSpace){
throw new Error('not implemented');
},
"public function getRect",function(targetCoordinateSpace){
throw new Error('not implemented');
},
"public function globalToLocal",function(point){
throw new Error('not implemented');
},
"public function hitTestObject",function(obj){
throw new Error('not implemented');
},
"public function hitTestPoint",function(x,y,shapeFlag){switch(arguments.length){case 0:case 1:case 2:shapeFlag=false;}
throw new Error('not implemented');
},
"public function localToGlobal",function(point){
throw new Error('not implemented');
},
"public native function set parent",
"public function broadcastEvent",function(event){
return this.dispatchEvent(event);
},
"private static const",{DOM_EVENT_TO_MOUSE_EVENT:function(){return({
'click':flash.events.MouseEvent.CLICK,
'dblclick':flash.events.MouseEvent.DOUBLE_CLICK,
'mousedown':flash.events.MouseEvent.MOUSE_DOWN,
'mouseup':flash.events.MouseEvent.MOUSE_UP,
'mousemove':flash.events.MouseEvent.MOUSE_MOVE
});}},
"private static const",{DOM_EVENT_TO_KEYBOARD_EVENT:function(){return({
'keydown':flash.events.KeyboardEvent.KEY_DOWN,
'keyup':flash.events.KeyboardEvent.KEY_UP
});}},
"private static const",{FLASH_EVENT_TO_DOM_EVENT:function(){return($$private.merge(
$$private.reverseMapping($$private.DOM_EVENT_TO_MOUSE_EVENT),
$$private.reverseMapping($$private.DOM_EVENT_TO_KEYBOARD_EVENT)));}},
"private var",{_scaleX:1},
"private var",{_scaleY:1},
"private static function merge",function(o1,o2){
var result={};
for(var m in o1){
result[m]=o1[m];
}
for(m in o2){
result[m]=o2[m];
}
return result;
},
"private static function reverseMapping",function(mapping){
var result={};
for(var m in mapping){
result[mapping[m]]=m;
}
return result;
},
"override public function addEventListener",function(type,listener,useCapture,
priority,useWeakReference){switch(arguments.length){case 0:case 1:case 2:useCapture=false;case 3:priority=0;case 4:useWeakReference=false;}
var newEventType=!this.hasEventListener(type);
this.addEventListener$2(type,listener,useCapture,priority,useWeakReference);
var domEventType=$$private.FLASH_EVENT_TO_DOM_EVENT[type];
if(newEventType){
if(domEventType){
this._elem$2.addEventListener(domEventType,$$bound(this,"transformAndDispatch$2"),useCapture);
}
}
},
"override public function removeEventListener",function(type,listener,useCapture){switch(arguments.length){case 0:case 1:case 2:useCapture=false;}
this.removeEventListener$2(type,listener,useCapture);
if(!this.hasEventListener(type)){
var domEventType=$$private.FLASH_EVENT_TO_DOM_EVENT[type];
if(domEventType){
this._elem$2.removeEventListener(domEventType,$$bound(this,"transformAndDispatch$2"),useCapture);
}
}
},
"private function transformAndDispatch",function(event){
var flashEvent;
var type=$$private.DOM_EVENT_TO_MOUSE_EVENT[event.type];
if(type){
flashEvent=new flash.events.MouseEvent(type,true,true,event.pageX-this.stage.x,event.pageY-this.stage.y,null,
event.ctrlKey,event.altKey,event.shiftKey,this.stage.buttonDown);
}else{
type=$$private.DOM_EVENT_TO_KEYBOARD_EVENT[event.type];
if(type){
flashEvent=new flash.events.KeyboardEvent(type,true,true,event['charCode'],event.keyCode||event['which'],0,
event.ctrlKey,event.altKey,event.shiftKey,event.ctrlKey,event.ctrlKey);
}
}
if(!flashEvent){
trace("Unmapped DOM event type "+event.type+" occured, ignoring.");
}
return this.dispatchEvent(flashEvent);
},
"private static function numberToStyleLength",function(value){
return isNaN(value)?"auto":(value+"px");
},
"private static function styleLengthToNumber",function(length){
return length=="auto"?NaN:Number(length.split("px")[0]);
},
"protected function createElement",function(){
var elem=(window.document.createElement(this.getElementName()));
elem.style.position="absolute";
elem.style.width="100%";
elem.style.left=this._x$2+"px";
elem.style.top=this._y$2+"px";
elem.style['MozUserSelect']='none';
elem.style['KhtmlUserSelect']='none';
elem['unselectable']='on';
elem['onselectstart']=function(){return false;};
return elem;
},
"protected function getElementName",function(){
return"div";
},
"public function hasElement",function(){
return! !this._elem$2;
},
"public function getElement",function(){
if(!this._elem$2){
this._elem$2=this.createElement();
}
return this._elem$2;
},
"protected function setElement",function(elem){
elem.style.left=this._x$2+"px";
elem.style.top=this._y$2+"px";
if(this._elem$2){
elem.style.width=this._elem$2.style.width;
elem.style.height=this._elem$2.style.height;
if(this.parent){
this.parent.getElement().replaceChild(elem,this._elem$2);
}
}
this._elem$2=elem;
},
"public function DisplayObject",function(){
this.super$2();
},
"private var",{_elem:null},
"private var",{_x:0,_y:0},
"private var",{_transform:null},
"private var",{_visible:false},
"private var",{_alpha:NaN},
];},[],["flash.events.EventDispatcher","flash.display.IBitmapDrawable","Error","String","flash.display.LoaderInfo","flash.geom.Transform","flash.events.MouseEvent","flash.events.KeyboardEvent","Number","js.HTMLElement"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.DisplayObjectContainer
joo.classLoader.prepare("package flash.display",
"public class DisplayObjectContainer extends flash.display.InteractiveObject",4,function($$private){var as=joo.as,assert=joo.assert;return[function(){joo.classLoader.init(flash.events.Event);},
"public function get mouseChildren",function(){
throw new Error('not implemented');
},
"public function set mouseChildren",function(value){
throw new Error('not implemented');
},
"public function get numChildren",function(){
return this.children$4.length;
},
"public function get tabChildren",function(){
throw new Error('not implemented');
},
"public function set tabChildren",function(value){
throw new Error('not implemented');
},
"public function get textSnapshot",function(){
throw new Error('not implemented');
},
"public function DisplayObjectContainer",function(){
this.children$4=[];
this.super$4();
},
"public function addChild",function(child){
return this.addChildAt(child,this.children$4.length);
},
"public function addChildAt",function(child,index){
var wasInStage=! !child.stage;
this.internalAddChildAt(child,index);
var isInStage=! !this.stage;
if(wasInStage!==isInStage){
child.broadcastEvent(new flash.events.Event(isInStage?flash.events.Event.ADDED_TO_STAGE:flash.events.Event.REMOVED_FROM_STAGE,false,false));
}
return child;
},
"public function internalAddChildAt",function(child,index){
var containerElement=this.getElement();
var childElement=child.getElement();
var oldParent=child.parent;
if(oldParent){
oldParent.removeChild(child);
}else{
}
var refChild=this.children$4[index];
this.children$4.splice(index,0,child);
child.parent=this;
if(refChild){
containerElement.insertBefore(childElement,refChild.getElement());
}else{
containerElement.appendChild(childElement);
}
},
"public function areInaccessibleObjectsUnderPoint",function(point){
throw new Error('not implemented');
},
"public function contains",function(child){
return child===this||this.children$4.some(function(someChild){
var container=as(someChild,flash.display.DisplayObjectContainer);
return container?container.contains(child):someChild===child;
});
},
"public function getChildAt",function(index){
return as(this.children$4[index],flash.display.DisplayObject);
},
"public function getChildByName",function(name){
for(var i=0;i<this.children$4.length;i++){
var child=this.children$4[i];
if(child.name===name){
return child;
}
}
return null;
},
"public function getChildIndex",function(child){
var index=this.children$4.indexOf(child);
if(index==-1){
throw new ArgumentError();
}
return index;
},
"public function getObjectsUnderPoint",function(point){
throw new Error('not implemented');
},
"public function removeChild",function(child){
return this.removeChildAt(this.getChildIndex(child));
},
"public function removeChildAt",function(index){
var containerElement=this.getElement();
var child=this.children$4.splice(index,1)[0];
child.parent=null;
var childElement=child.getElement();
containerElement.removeChild(childElement);
return child;
},
"public function setChildIndex",function(child,index){
this.removeChild(child);
this.addChildAt(child,index);
},
"public function swapChildren",function(child1,child2){
var child1Index=this.children$4.indexOf(child1);
var child2Index=this.children$4.indexOf(child2);
if(child1Index===-1||child2Index===-1){
throw new ArgumentError;
}
this.swapChildrenAt(child1Index,child2Index);
},
"public function swapChildrenAt",function(index1,index2){
if(index1>index2){
this.swapChildrenAt(index2,index1);
}else if(index1<index2){
var containerElement=this.getElement();
var child1=this.children$4[index1];
var child2=this.children$4[index2];
this.children$4.splice(index1,1,child2);
this.children$4.splice(index2,1,child1);
var child1Element=child1.getElement();
var child2Element=child2.getElement();
var refElement=(child2Element.nextSibling);
containerElement.insertBefore(child2Element,child1Element);
if(refElement){
containerElement.insertBefore(child1Element,refElement);
}else{
containerElement.appendChild(child1Element);
}
}
},
"override public function get height",function(){
return this.height$4||this.children$4.length&&((this.children$4[0]).y+(this.children$4[0]).height);
},
"override public function get width",function(){
return this.width$4||this.children$4.length&&((this.children$4[0]).x+(this.children$4[0]).width);
},
"override internal function updateScale",function(scaleX,scaleY){
this.updateScale$4(scaleX,scaleY);
this.children$4.forEach(function(child){
child.updateScale(scaleX,scaleY);
});
},
"override public function broadcastEvent",function(event){
if(this.dispatchEvent(event)){
this.children$4.every(function(child){
return child.broadcastEvent(event);
});
return true;
}
return false;
},
"private var",{children:null},
];},[],["flash.display.InteractiveObject","Error","flash.events.Event","flash.display.DisplayObject","ArgumentError","js.Element"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.FrameLabel
joo.classLoader.prepare("package flash.display",
"public final class FrameLabel",1,function($$private){;return[
"public function get frame",function(){
return this._frame$1;
},
"public function get name",function(){
return this._name$1;
},
"public function FrameLabel",function(){
},
"private var",{_frame:0},
"private var",{_name:null},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.GradientType
joo.classLoader.prepare("package flash.display",
"public final class GradientType",1,function($$private){;return[
"public static const",{LINEAR:"linear"},
"public static const",{RADIAL:"radial"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.Graphics
joo.classLoader.prepare("package flash.display",
"public final class Graphics",1,function($$private){var as=joo.as;return[function(){joo.classLoader.init(flash.geom.Matrix,flash.display.CapsStyle,Math,flash.display.GradientType,flash.display.JointStyle);},
"public function beginBitmapFill",function(bitmap,matrix,repeat,smooth){switch(arguments.length){case 0:case 1:matrix=null;case 2:repeat=true;case 3:smooth=false;}
this._beginFill$1(this.createPattern$1(bitmap,matrix,repeat,smooth));
},
"private function createPattern",function(bitmap,matrix,repeat,smooth){
return this.context$1.createPattern(bitmap.getElement(),repeat?"repeat":"no-repeat");
},
"public function beginFill",function(color,alpha){switch(arguments.length){case 0:case 1:alpha=1.0;}
this._beginFill$1(flash.display.Graphics.toRGBA(color,alpha));
},
"public function beginGradientFill",function(type,colors,alphas,ratios,matrix,spreadMethod,interpolationMethod,focalPointRatio){switch(arguments.length){case 0:case 1:case 2:case 3:case 4:matrix=null;case 5:spreadMethod="pad";case 6:interpolationMethod="rgb";case 7:focalPointRatio=0;}
this._beginFill$1(this.createGradientStyle$1(type,colors,alphas,ratios,
matrix,spreadMethod,interpolationMethod,focalPointRatio));
},
"public function clear",function(){
this.lineStyle();
this.context$1.save();
this.context$1.setTransform(this.scaleX$1,0,0,this.scaleY$1,0,0);
this.context$1.fillStyle="#000000";
this.context$1.clearRect(0,0,this.context$1.canvas.width,this.context$1.canvas.height);
this.context$1.restore();
this.insideFill$1=false;
this.context$1.moveTo(0,0);
this.width=this.height=0;
},
"public function curveTo",function(controlX,controlY,anchorX,anchorY){
this.createSpace$1(Math.max(this.x$1,controlX,anchorX),Math.max(this.y$1,controlY,anchorY));
this.x$1=anchorX;
this.y$1=anchorY;
this.context$1.quadraticCurveTo(controlX,controlY,anchorX,anchorY);
if(!this.insideFill$1){
this.context$1.stroke();
}
},
"public function drawCircle",function(x,y,radius){
this.createSpace$1(x+radius,y+radius);
this.context$1.moveTo(x+radius,y);
this.context$1.arc(x,y,radius,0,2*Math.PI,false);
if(this.insideFill$1){
this.context$1.fill();
}
this.context$1.stroke();
this.context$1.beginPath();
this.context$1.moveTo(x,y);
},
"public function drawEllipse",function(x,y,width,height){
var rx=width/2;
var ry=height/2;
var cx=x+rx;
var cy=y+ry;
this.createSpace$1(x+width,y+height);
this.context$1.beginPath();
this.context$1.moveTo(cx,cy-ry);
this.context$1.bezierCurveTo(cx+($$private.KAPPA*rx),cy-ry,cx+rx,cy-($$private.KAPPA*ry),cx+rx,cy);
this.context$1.bezierCurveTo(cx+rx,cy+($$private.KAPPA*ry),cx+($$private.KAPPA*rx),cy+ry,cx,cy+ry);
this.context$1.bezierCurveTo(cx-($$private.KAPPA*rx),cy+ry,cx-rx,cy+($$private.KAPPA*ry),cx-rx,cy);
this.context$1.bezierCurveTo(cx-rx,cy-($$private.KAPPA*ry),cx-($$private.KAPPA*rx),cy-ry,cx,cy-ry);
if(this.insideFill$1){
this.context$1.fill();
}
this.context$1.stroke();
this.context$1.beginPath();
this.context$1.moveTo(x,y);
},
"public function drawRect",function(x,y,width,height){
this.createSpace$1(x+width,y+height);
if(this.insideFill$1){
this.context$1.fillRect(x,y,width,height);
}
this.context$1.strokeRect(x,y,width,height);
},
"public function drawRoundRect",function(x,y,width,height,ellipseWidth,ellipseHeight){switch(arguments.length){case 0:case 1:case 2:case 3:case 4:case 5:ellipseHeight=NaN;}
this.createSpace$1(x+width,y+height);
if(ellipseHeight==0||ellipseWidth==0){
this.drawRect(x,y,width,height);
return;
}
if(isNaN(ellipseHeight)){
ellipseHeight=ellipseWidth;
}
var x_lw=x+ellipseWidth;
var x_r=x+width;
var x_rw=x_r-ellipseWidth;
var y_tw=y+ellipseHeight;
var y_b=y+height;
var y_bw=y_b-ellipseHeight;
this.context$1.beginPath();
this.context$1.moveTo(x_lw,y);
this.context$1.lineTo(x_rw,y);
this.context$1.quadraticCurveTo(x_r,y,x_r,y_tw);
this.context$1.lineTo(x_r,y_bw);
this.context$1.quadraticCurveTo(x_r,y_b,x_rw,y_b);
this.context$1.lineTo(x_lw,y_b);
this.context$1.quadraticCurveTo(x,y_b,x,y_bw);
this.context$1.lineTo(x,y_tw);
this.context$1.quadraticCurveTo(x,y,x_lw,y);
this.context$1.closePath();
if(this.insideFill$1){
this.context$1.fill();
}
this.context$1.stroke();
},
"public function endFill",function(){
this.context$1.closePath();
this.context$1.fill();
this.context$1.stroke();
this.insideFill$1=false;
},
"public function lineGradientStyle",function(type,colors,alphas,ratios,matrix,spreadMethod,interpolationMethod,focalPointRatio){switch(arguments.length){case 0:case 1:case 2:case 3:case 4:matrix=null;case 5:spreadMethod="pad";case 6:interpolationMethod="rgb";case 7:focalPointRatio=0;}
this.context$1.strokeStyle=this.createGradientStyle$1(type,colors,alphas,ratios,
matrix,spreadMethod,interpolationMethod,focalPointRatio);
},
"public function lineStyle",function(thickness,color,alpha,pixelHinting,scaleMode,caps,joints,miterLimit){switch(arguments.length){case 0:thickness=NaN;case 1:color=0;case 2:alpha=1.0;case 3:pixelHinting=false;case 4:scaleMode="normal";case 5:caps=null;case 6:joints=null;case 7:miterLimit=3;}
if(!isNaN(thickness)){
this.context$1.lineWidth=thickness||1;
}
this.context$1.strokeStyle=flash.display.Graphics.toRGBA(color,alpha);
this.context$1.lineCap=caps||flash.display.CapsStyle.ROUND;
this.context$1.lineJoin=joints||flash.display.JointStyle.ROUND;
this.context$1.miterLimit=miterLimit;
},
"public function lineTo",function(x,y){
this.createSpace$1(Math.max(this.x$1,x),Math.max(this.y$1,y));
this.x$1=x;
this.y$1=y;
this.context$1.lineTo(x,y);
if(!this.insideFill$1){
this.context$1.stroke();
this.context$1.beginPath();
this.context$1.moveTo(x,y);
}
},
"public function moveTo",function(x,y){
this.context$1.beginPath();
this.context$1.moveTo(x,y);
this.x$1=x;
this.y$1=y;
},
"private static const",{PIXEL_CHUNK_SIZE:100},
"private var",{context:null},
"private var",{insideFill:false},
"private var",{x:0},
"private var",{y:0},
"private var",{scaleX:1},
"private var",{scaleY:1},
"internal var",{width:0},
"internal var",{height:0},
"public function Graphics",function(){
var canvas=as(window.document.createElement("canvas"),js.HTMLCanvasElement);
canvas.width=$$private.PIXEL_CHUNK_SIZE;
canvas.height=$$private.PIXEL_CHUNK_SIZE;
canvas.style.position="absolute";
this.context$1=(canvas.getContext("2d"));
this.context$1.beginPath();
this.context$1.moveTo(0,0);
this.context$1.lineCap=flash.display.CapsStyle.ROUND;
this.context$1.lineJoin=flash.display.JointStyle.ROUND;
this.context$1.miterLimit=3;
},
"internal function get canvas",function(){
return this.context$1.canvas;
},
"internal function get renderingContext",function(){
return this.context$1;
},
"internal function updateScale",function(scaleX,scaleY){
this.context$1.setTransform(scaleX,0,0,scaleY,0,0);
var oldWidth=this.width/this.scaleX$1;
var oldHeight=this.height/this.scaleY$1;
this.scaleX$1=scaleX;
this.scaleY$1=scaleY;
this.createSpace$1(oldWidth,oldHeight);
},
"private function createSpace",function(width,height){
width*=this.scaleX$1;
height*=this.scaleY$1;
if(width>this.width||height>this.height){
this.width=Math.max(this.width,width);
this.height=Math.max(this.height,height);
var canvas=this.canvas;
if(this.width>canvas.width||this.height>canvas.height){
var backupStyle={
fillStyle:this.context$1.fillStyle,
lineWidth:this.context$1.lineWidth,
strokeStyle:this.context$1.strokeStyle,
lineCap:this.context$1.lineCap,
lineJoin:this.context$1.lineJoin,
miterLimit:this.context$1.miterLimit
};
if(canvas.width>0&&canvas.height>0){
var imageData=this.context$1.getImageData(0,0,canvas.width,canvas.height);
}
canvas.width=Math.max(canvas.width,width+$$private.PIXEL_CHUNK_SIZE);
canvas.height=Math.max(canvas.height,height+$$private.PIXEL_CHUNK_SIZE);
for(var m in backupStyle){
this.context$1[m]=backupStyle[m];
}
if(imageData){
this.context$1.putImageData(imageData,0,0);
}
this.context$1.beginPath();
this.context$1.moveTo(this.x$1,this.y$1);
}
}
},
"private function _beginFill",function(fillStyle){
this.context$1.beginPath();
this.context$1.fillStyle=fillStyle;
this.insideFill$1=true;
},
"private function createGradientStyle",function(type,colors,alphas,ratios,
matrix,spreadMethod,
interpolationMethod,focalPointRatio){switch(arguments.length){case 0:case 1:case 2:case 3:case 4:matrix=null;case 5:spreadMethod="pad";case 6:interpolationMethod="rgb";case 7:focalPointRatio=0;}
var gradient;
var p0=new flash.geom.Point(0,0);
var p1=new flash.geom.Point(-flash.geom.Matrix.MAGIC_GRADIENT_FACTOR/2,0);
var p2=type==flash.display.GradientType.LINEAR
?new flash.geom.Point(0,-flash.geom.Matrix.MAGIC_GRADIENT_FACTOR/2)
:new flash.geom.Point(flash.geom.Matrix.MAGIC_GRADIENT_FACTOR/2*focalPointRatio,0);
if(matrix){
p0=matrix.transformPoint(p0);
p1=matrix.transformPoint(p1);
p2=matrix.transformPoint(p2);
}
if(type==flash.display.GradientType.LINEAR){
var x1;
var y1;
if(p2.x==p0.x){
x1=p1.x;
y1=p1.y;
}else if(p2.y==p0.y){
x1=p1.x;
y1=p2.x;
}else{
var d=-(p2.x-p0.x)/(p2.y-p0.y);
x1=(p1.x/d+p1.y+d*p0.x-p0.y)/(d+1/d);
y1=d*(x1-p0.x)+p0.y;
}
var x2=p0.x+(p0.x-x1);
var y2=p0.y+(p0.y-y1);
gradient=this.context$1.createLinearGradient(x1,y1,x2,y2);
}else{
var rx=p1.x-p0.x;
var ry=p1.y-p0.y;
var r=rx==0?Math.abs(ry):ry==0?Math.abs(rx):Math.sqrt(rx*rx+ry*ry);
gradient=this.context$1.createRadialGradient(p2.x,p2.y,0,p0.x,p0.y,r);
}
for(var i=0;i<colors.length;++i){
gradient.addColorStop(ratios[i]/255,flash.display.Graphics.toRGBA(colors[i],alphas[i]));
}
return gradient;
},
"public static function toRGBA",function(color,alpha){
var params=[color>>>16&0xFF,color>>>8&0xFF,color&0xFF].join(",");
return alpha<1?["rgba(",params,",",alpha,")"].join("")
:"rgb("+params+")";
},
"private static const",{KAPPA:function(){return(4*((Math.sqrt(2)-1)/3));}},
];},["toRGBA"],["Math","flash.display.CapsStyle","flash.display.JointStyle","js.HTMLCanvasElement","js.CanvasRenderingContext2D","flash.geom.Point","flash.geom.Matrix","flash.display.GradientType"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.IBitmapDrawable
joo.classLoader.prepare("package flash.display",
"public interface IBitmapDrawable",1,function($$private){;return[
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.InteractiveObject
joo.classLoader.prepare("package flash.display",
{Event:{name:"click",type:"flash.events.MouseEvent"}},
{Event:{name:"doubleClick",type:"flash.events.MouseEvent"}},
{Event:{name:"focusIn",type:"flash.events.FocusEvent"}},
{Event:{name:"focusOut",type:"flash.events.FocusEvent"}},
{Event:{name:"keyDown",type:"flash.events.KeyboardEvent"}},
{Event:{name:"keyFocusChange",type:"flash.events.FocusEvent"}},
{Event:{name:"keyUp",type:"flash.events.KeyboardEvent"}},
{Event:{name:"mouseDown",type:"flash.events.MouseEvent"}},
{Event:{name:"mouseFocusChange",type:"flash.events.FocusEvent"}},
{Event:{name:"mouseMove",type:"flash.events.MouseEvent"}},
{Event:{name:"mouseOut",type:"flash.events.MouseEvent"}},
{Event:{name:"mouseOver",type:"flash.events.MouseEvent"}},
{Event:{name:"mouseUp",type:"flash.events.MouseEvent"}},
{Event:{name:"mouseWheel",type:"flash.events.MouseEvent"}},
{Event:{name:"rollOut",type:"flash.events.MouseEvent"}},
{Event:{name:"rollOver",type:"flash.events.MouseEvent"}},
{Event:{name:"tabChildrenChange",type:"flash.events.Event"}},
{Event:{name:"tabEnabledChange",type:"flash.events.Event"}},
{Event:{name:"tabIndexChange",type:"flash.events.Event"}},
{Event:{name:"textInput",type:"flash.events.TextEvent"}},
"public class InteractiveObject extends flash.display.DisplayObject",3,function($$private){;return[
"public native function get accessibilityImplementation",
"public native function set accessibilityImplementation",
"public native function get contextMenu",
"public native function set contextMenu",
"public native function get doubleClickEnabled",
"public native function set doubleClickEnabled",
"public function get focusRect",function(){
return this._focusRect$3;
},
"public function set focusRect",function(value){
this._focusRect$3=value===null?null:Boolean(value);
},
"public native function get mouseEnabled",
"public native function set mouseEnabled",
"public native function get tabEnabled",
"public native function set tabEnabled",
"public native function get tabIndex",
"public native function set tabIndex",
"public function InteractiveObject",function(){this.super$3();
if(this['constructor']===flash.display.InteractiveObject){
throw new ArgumentError();
}
},
"private var",{_focusRect:null},
];},[],["flash.display.DisplayObject","Boolean","ArgumentError"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.InterpolationMethod
joo.classLoader.prepare("package flash.display",
"public final class InterpolationMethod",1,function($$private){;return[
"public static const",{LINEAR_RGB:"linearRGB"},
"public static const",{RGB:"rgb"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.JointStyle
joo.classLoader.prepare("package flash.display",
"public final class JointStyle",1,function($$private){;return[
"public static const",{BEVEL:"bevel"},
"public static const",{MITER:"miter"},
"public static const",{ROUND:"round"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.LineScaleMode
joo.classLoader.prepare("package flash.display",
"public final class LineScaleMode",1,function($$private){;return[
"public static const",{HORIZONTAL:"horizontal"},
"public static const",{NONE:"none"},
"public static const",{NORMAL:"normal"},
"public static const",{VERTICAL:"vertical"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.Loader
joo.classLoader.prepare("package flash.display",
"public class Loader extends flash.display.DisplayObjectContainer",5,function($$private){;return[
"public function get content",function(){
return this.contentLoaderInfo.content;
},
"public native function get contentLoaderInfo",
"public function Loader",function(){this.super$5();
this['contentLoaderInfo']=new flash.display.LoaderInfo(this);
},
"public function close",function(){
},
"public function load",function(request,context){switch(arguments.length){case 0:case 1:context=null;}
this.contentLoaderInfo.setUrl(request.url);
this.contentLoaderInfo.load();
},
"public function loadBytes",function(bytes,context){switch(arguments.length){case 0:case 1:context=null;}
this.contentLoaderInfo.setBytes(bytes);
this.contentLoaderInfo.setUrl("data:image/gif;base64,"+joo.flash.util.Base64.encodeBytes(bytes));
this.contentLoaderInfo.load();
},
"public function unload",function(){
this.contentLoaderInfo.setContent(null);
},
];},[],["flash.display.DisplayObjectContainer","flash.display.LoaderInfo","joo.flash.util.Base64"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.LoaderInfo
joo.classLoader.prepare("package flash.display",
{Event:{name:"complete",type:"flash.events.Event"}},
{Event:{name:"httpStatus",type:"flash.events.HTTPStatusEvent"}},
{Event:{name:"init",type:"flash.events.Event"}},
{Event:{name:"ioError",type:"flash.events.IOErrorEvent"}},
{Event:{name:"open",type:"flash.events.Event"}},
{Event:{name:"progress",type:"flash.events.ProgressEvent"}},
{Event:{name:"unload",type:"flash.events.Event"}},
"public class LoaderInfo extends flash.events.EventDispatcher",2,function($$private){;return[function(){joo.classLoader.init(flash.events.Event);},
"public function get actionScriptVersion",function(){
throw new Error('not implemented');
},
"public function get applicationDomain",function(){
throw new Error('not implemented');
},
"public native function get bytes",
"internal function setBytes",function(value){
this['bytes']=value;
},
"public function get bytesLoaded",function(){
throw new Error('not implemented');
},
"public function get bytesTotal",function(){
throw new Error('not implemented');
},
"public function get childAllowsParent",function(){
throw new Error('not implemented');
},
"public native function get content",
"internal function setContent",function(content){
this['content']=content;
},
"public function get contentType",function(){
throw new Error('not implemented');
},
"public function get frameRate",function(){
throw new Error('not implemented');
},
"public function get height",function(){
return this.content.height;
},
"public native function get loader",
"public function get loaderURL",function(){
throw new Error('not implemented');
},
"public function get parameters",function(){
throw new Error('not implemented');
},
"public function get parentAllowsChild",function(){
throw new Error('not implemented');
},
"public function get sameDomain",function(){
throw new Error('not implemented');
},
"public function get sharedEvents",function(){
throw new Error('not implemented');
},
"public function get swfVersion",function(){
throw new Error('not implemented');
},
"public native function get url",
"internal function setUrl",function(url){
this['url']=url;
},
"public function get width",function(){
return this.content.width;
},
"public static function getLoaderInfoByDefinition",function(object){
throw new Error('not implemented');
},
"public function LoaderInfo",function(loader){switch(arguments.length){case 0:loader=null;}this.super$2();
this['loader']=loader;
},
"internal function load",function(){var this$=this;
var img=new js.Image();
img.addEventListener("load",function(){
this$.setContent(new flash.display.Bitmap(flash.display.BitmapData.fromImg(img)));
this$.dispatchEvent(new flash.events.Event(flash.events.Event.COMPLETE));
},false);
img.src=this.url;
},
];},["getLoaderInfoByDefinition"],["flash.events.EventDispatcher","Error","js.Image","flash.display.Bitmap","flash.display.BitmapData","flash.events.Event"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.MorphShape
joo.classLoader.prepare("package flash.display",
"public final class MorphShape extends flash.display.DisplayObject",3,function($$private){;return[
];},[],["flash.display.DisplayObject"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.MovieClip
joo.classLoader.prepare("package flash.display",
"public dynamic class MovieClip extends flash.display.Sprite",6,function($$private){;return[
"public function get currentFrame",function(){
throw new Error('not implemented');
},
"public function get currentLabel",function(){
throw new Error('not implemented');
},
"public function get currentLabels",function(){
throw new Error('not implemented');
},
"public function get currentScene",function(){
throw new Error('not implemented');
},
"public function get enabled",function(){
throw new Error('not implemented');
},
"public function set enabled",function(value){
throw new Error('not implemented');
},
"public function get framesLoaded",function(){
return-1;
},
"public function get scenes",function(){
throw new Error('not implemented');
},
"public function get totalFrames",function(){
return-1;
},
"public function get trackAsMenu",function(){
throw new Error('not implemented');
},
"public function set trackAsMenu",function(value){
throw new Error('not implemented');
},
"public function MovieClip",function(){this.super$6();
},
"public function gotoAndPlay",function(frame,scene){switch(arguments.length){case 0:case 1:scene=null;}
throw new Error('not implemented');
},
"public function gotoAndStop",function(frame,scene){switch(arguments.length){case 0:case 1:scene=null;}
throw new Error('not implemented');
},
"public function nextFrame",function(){
},
"public function nextScene",function(){
throw new Error('not implemented');
},
"public function play",function(){
throw new Error('not implemented');
},
"public function prevFrame",function(){
},
"public function prevScene",function(){
throw new Error('not implemented');
},
"public function stop",function(){
},
];},[],["flash.display.Sprite","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.PixelSnapping
joo.classLoader.prepare("package flash.display",
"public final class PixelSnapping",1,function($$private){;return[
"public static const",{ALWAYS:"always"},
"public static const",{AUTO:"auto"},
"public static const",{NEVER:"never"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.Scene
joo.classLoader.prepare("package flash.display",
"public final class Scene",1,function($$private){;return[
"public function get labels",function(){
throw new Error('not implemented');
},
"public function get name",function(){
throw new Error('not implemented');
},
"public function get numFrames",function(){
throw new Error('not implemented');
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.Shape
joo.classLoader.prepare("package flash.display",
"public class Shape extends flash.display.DisplayObject",3,function($$private){;return[
"public function get graphics",function(){
return this._graphics$3;
},
"public function Shape",function(){
this.super$3();
this._graphics$3=new flash.display.Graphics();
},
"override public function set transform",function(value){
this.transform$3=value;
var m=value.matrix;
if(m){
this.graphics.renderingContext.setTransform(m.a,m.b,m.c,m.d,m.tx,m.ty);
}
},
"override public function get width",function(){
return this._graphics$3.width;
},
"override public function get height",function(){
return this._graphics$3.height;
},
"override protected function createElement",function(){
return this.graphics.canvas;
},
"private var",{_graphics:null},
];},[],["flash.display.DisplayObject","flash.display.Graphics"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.SimpleButton
joo.classLoader.prepare("package flash.display",
"public class SimpleButton extends flash.display.InteractiveObject",4,function($$private){;return[
"public native function get downState",
"public native function set downState",
"public function get enabled",function(){
return this._enabled$4;
},
"public function set enabled",function(value){
this._enabled$4=value;
this.getElement().disabled=!value;
},
"public native function get hitTestState",
"public native function set hitTestState",
"public native function get overState",
"public native function set overState",
"public native function get soundTransform",
"public native function set soundTransform",
"public native function get trackAsMenu",
"public native function set trackAsMenu",
"public native function get upState",
"public native function set upState",
"public native function get useHandCursor",
"public native function set useHandCursor",
"public function SimpleButton",function(upState,overState,downState,hitTestState){switch(arguments.length){case 0:upState=null;case 1:overState=null;case 2:downState=null;case 3:hitTestState=null;}
this.super$4();
this.upState=upState;
this.overState=overState;
this.downState=downState;
this.hitTestState=hitTestState;
},
"override protected function getElementName",function(){
return"button";
},
"private var",{_enabled:true},
];},[],["flash.display.InteractiveObject"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.SpreadMethod
joo.classLoader.prepare("package flash.display",
"public final class SpreadMethod",1,function($$private){;return[
"public static const",{PAD:"pad"},
"public static const",{REFLECT:"reflect"},
"public static const",{REPEAT:"repeat"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.Sprite
joo.classLoader.prepare("package flash.display",
"public class Sprite extends flash.display.DisplayObjectContainer",5,function($$private){;return[
"public function get buttonMode",function(){
throw new Error('not implemented');
},
"public function set buttonMode",function(value){
throw new Error('not implemented');
},
"public function get dropTarget",function(){
throw new Error('not implemented');
},
"public function get graphics",function(){
if(!this._graphics$5){
this._graphics$5=new flash.display.Graphics();
this._graphics$5.updateScale(this.scaleX,this.scaleY);
var canvas=this._graphics$5.canvas;
var element=this.getElement();
if(element.firstChild){
element.insertBefore(canvas,element.firstChild);
}else{
element.appendChild(canvas);
}
}
return this._graphics$5;
},
"override internal function updateScale",function(scaleX,scaleY){
this.updateScale$5(scaleX,scaleY);
if(this._graphics$5){
this._graphics$5.updateScale(scaleX,scaleY);
}
},
"public function get hitArea",function(){
throw new Error('not implemented');
},
"public function set hitArea",function(value){
throw new Error('not implemented');
},
"public function get soundTransform",function(){
throw new Error('not implemented');
},
"public function set soundTransform",function(value){
throw new Error('not implemented');
},
"public function get useHandCursor",function(){
throw new Error('not implemented');
},
"public function set useHandCursor",function(value){
throw new Error('not implemented');
},
"public function Sprite",function(){
this.super$5();
},
"public function startDrag",function(lockCenter,bounds){switch(arguments.length){case 0:lockCenter=false;case 1:bounds=null;}
throw new Error('not implemented');
},
"public function stopDrag",function(){
throw new Error('not implemented');
},
"override public function set transform",function(value){
this.transform$5=value;
var m=value.matrix;
if(m){
this.graphics.renderingContext.setTransform(m.a,m.b,m.c,m.d,m.tx,m.ty);
}
},
"private var",{_graphics:null},
];},[],["flash.display.DisplayObjectContainer","Error","flash.display.Graphics"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.Stage
joo.classLoader.prepare("package flash.display",
{Event:{name:"fullScreen",type:"flash.events.FullScreenEvent"}},
{Event:{name:"mouseLeave",type:"flash.events.Event"}},
{Event:{name:"resize",type:"flash.events.Event"}},
"public class Stage extends flash.display.DisplayObjectContainer",5,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.display.StageScaleMode,flash.display.StageQuality,flash.display.StageAlign,flash.events.Event,flash.events.TimerEvent);},
"public function get align",function(){
return this._align$5;
},
"public function set align",function(value){
this._align$5=value;
},
"public function get displayState",function(){
throw new Error('not implemented');
},
"public function set displayState",function(value){
throw new Error('not implemented');
},
"public function get focus",function(){
throw new Error('not implemented');
},
"public function set focus",function(value){
throw new Error('not implemented');
},
"public function get frameRate",function(){
return this._frameRate$5;
},
"public function set frameRate",function(value){
this._frameRate$5=Number(value);
if(this.frameTimer$5){
this.frameTimer$5.delay=1000/this._frameRate$5;
}
},
"public function get fullScreenHeight",function(){
throw new Error('not implemented');
},
"public function get fullScreenSourceRect",function(){
throw new Error('not implemented');
},
"public function set fullScreenSourceRect",function(value){
throw new Error('not implemented');
},
"public function get fullScreenWidth",function(){
throw new Error('not implemented');
},
"override public function get height",function(){
return this._stageHeight$5;
},
"override public function set height",function(value){
this.stageHeight=$$int(value);
},
"override public function get mouseChildren",function(){
throw new Error('not implemented');
},
"override public function set mouseChildren",function(value){
throw new Error('not implemented');
},
"override public function get numChildren",function(){
return this.numChildren$5;
},
"public function get quality",function(){
return this._quality$5;
},
"public function set quality",function(value){
this._quality$5=value;
},
"public function get scaleMode",function(){
return this._scaleMode$5;
},
"public function set scaleMode",function(value){
this._scaleMode$5=value;
},
"public native function get showDefaultContextMenu",
"public native function set showDefaultContextMenu",
"public native function get stageFocusRect",
"public native function set stageFocusRect",
"public function get stageHeight",function(){
return this._stageHeight$5;
},
"public function set stageHeight",function(value){
this._stageHeight$5=value;
this.getElement().style.height=value+"px";
},
"public function get stageWidth",function(){
return this._stageWidth$5;
},
"public function set stageWidth",function(value){
this._stageWidth$5=value;
this.getElement().style.width=value+"px";
},
"override public function get tabChildren",function(){
return this.tabChildren$5;
},
"override public function set tabChildren",function(value){
this.tabChildren$5=value;
},
"override public function get textSnapshot",function(){
return this.textSnapshot$5;
},
"override public function get width",function(){
return this._stageWidth$5;
},
"override public function set width",function(value){
this.stageWidth=$$int(value);
},
"override public function addChild",function(child){
return this.addChild$5(child);
},
"override public function addChildAt",function(child,index){
return this.addChildAt$5(child,index);
},
"override public function addEventListener",function(type,listener,useCapture,priority,useWeakReference){switch(arguments.length){case 0:case 1:case 2:useCapture=false;case 3:priority=0;case 4:useWeakReference=false;}
this.addEventListener$5(type,listener,useCapture,priority,useWeakReference);
},
"override public function dispatchEvent",function(event){
return this.dispatchEvent$5(event);
},
"override public function hasEventListener",function(type){
return this.hasEventListener$5(type);
},
"public function invalidate",function(){
throw new Error('not implemented');
},
"public function isFocusInaccessible",function(){
throw new Error('not implemented');
},
"override public function removeChildAt",function(index){
return this.removeChildAt$5(index);
},
"override public function setChildIndex",function(child,index){
this.setChildIndex$5(child,index);
},
"override public function swapChildren",function(child1,child2){
this.swapChildren$5(child1,child2);
},
"override public function willTrigger",function(type){
return this.willTrigger$5(type);
},
"override public function get stage",function(){
return this;
},
"public function Stage",function(id,properties){
this.id$5=id;
if(properties){
for(var m in properties){
this[m]=properties[m];
}
}
this.super$5();this._quality$5=this._quality$5();this._scaleMode$5=this._scaleMode$5();this._align$5=this._align$5();
this.frameTimer$5=new flash.utils.Timer(1000/this._frameRate$5);
this.frameTimer$5.addEventListener(flash.events.TimerEvent.TIMER,$$bound(this,"enterFrame$5"));
this.frameTimer$5.start();
},
"public function set backgroundColor",function(value){
if(typeof value=='string'){
value=String(value).replace(/^#/,"0x");
}
this.getElement().style.backgroundColor=flash.display.Graphics.toRGBA($$uint(value));
},
"override protected function createElement",function(){var this$=this;var this$=this;var this$=this;
var element=(window.document.getElementById(this.id$5));
element.style.position="relative";
element.style.overflow="hidden";
element.setAttribute("tabindex","0");
element.style.margin="0";
element.style.padding="0";
var width=element.getAttribute("width");
if(!width){
width=this.width;
}
element.style.width=width+"px";
var height=element.getAttribute("height");
if(!height){
height=this.height;
}
element.style.height=height+"px";
element.innerHTML="";
element.addEventListener('mousedown',function(){
this$.buttonDown=true;
},true);
element.addEventListener('mouseup',function(){
this$.buttonDown=false;
},true);
element.addEventListener('mousemove',function(e){
this$._mouseX$5=e.clientX;
this$._mouseY$5=e.clientY;
},true);
return element;
},
"override public function get mouseX",function(){
return this._mouseX$5;
},
"override public function get mouseY",function(){
return this._mouseY$5;
},
"private function enterFrame",function(){
this.broadcastEvent(new flash.events.Event(flash.events.Event.ENTER_FRAME,false,false));
},
"private var",{_stageHeight:0},
"private var",{_stageWidth:0},
"private var",{_mouseX:0},
"private var",{_mouseY:0},
"private var",{id:null},
"private var",{_frameRate:30},
"private var",{frameTimer:null},
"private var",{_quality:function(){return(flash.display.StageQuality.HIGH);}},
"private var",{_scaleMode:function(){return(flash.display.StageScaleMode.NO_SCALE);}},
"private var",{_align:function(){return(flash.display.StageAlign.TOP_LEFT);}},
"internal var",{buttonDown:false},
];},[],["flash.display.DisplayObjectContainer","Error","Number","int","flash.utils.Timer","flash.events.TimerEvent","String","flash.display.Graphics","uint","js.HTMLElement","flash.events.Event","flash.display.StageQuality","flash.display.StageScaleMode","flash.display.StageAlign"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.StageAlign
joo.classLoader.prepare("package flash.display",
"public final class StageAlign",1,function($$private){;return[
"public static const",{BOTTOM:"B"},
"public static const",{BOTTOM_LEFT:"BL"},
"public static const",{BOTTOM_RIGHT:"BR"},
"public static const",{LEFT:"L"},
"public static const",{RIGHT:"R"},
"public static const",{TOP:"T"},
"public static const",{TOP_LEFT:"TL"},
"public static const",{TOP_RIGHT:"TR"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.StageDisplayState
joo.classLoader.prepare("package flash.display",
"public final class StageDisplayState",1,function($$private){;return[
"public static const",{FULL_SCREEN:"fullScreen"},
"public static const",{NORMAL:"normal"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.StageQuality
joo.classLoader.prepare("package flash.display",
"public final class StageQuality",1,function($$private){;return[
"public static const",{BEST:"best"},
"public static const",{HIGH:"high"},
"public static const",{LOW:"low"},
"public static const",{MEDIUM:"medium"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.StageScaleMode
joo.classLoader.prepare("package flash.display",
"public final class StageScaleMode",1,function($$private){;return[
"public static const",{EXACT_FIT:"exactFit"},
"public static const",{NO_BORDER:"noBorder"},
"public static const",{NO_SCALE:"noScale"},
"public static const",{SHOW_ALL:"showAll"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.display.SWFVersion
joo.classLoader.prepare("package flash.display",
"public final class SWFVersion",1,function($$private){;return[
"public static const",{FLASH1:1},
"public static const",{FLASH2:2},
"public static const",{FLASH3:3},
"public static const",{FLASH4:4},
"public static const",{FLASH5:5},
"public static const",{FLASH6:6},
"public static const",{FLASH7:7},
"public static const",{FLASH8:8},
"public static const",{FLASH9:9},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.errors.EOFError
joo.classLoader.prepare("package flash.errors",
"public dynamic class EOFError extends flash.errors.IOError",3,function($$private){;return[
"public function EOFError",function(message){switch(arguments.length){case 0:message="";}
this.super$3(message);
},
];},[],["flash.errors.IOError"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.errors.IllegalOperationError
joo.classLoader.prepare("package flash.errors",
"public dynamic class IllegalOperationError extends Error",2,function($$private){;return[
"public function IllegalOperationError",function(message){switch(arguments.length){case 0:message="";}
this.super$2(message);
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.errors.InvalidSWFError
joo.classLoader.prepare("package flash.errors",
"public dynamic class InvalidSWFError extends Error",2,function($$private){;return[
"public function InvalidSWFError",function(message,id){switch(arguments.length){case 0:message="";case 1:id=0;}
this.super$2(message);
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.errors.IOError
joo.classLoader.prepare("package flash.errors",
"public dynamic class IOError extends Error",2,function($$private){;return[
"public function IOError",function(message){switch(arguments.length){case 0:message="";}
this.super$2(message);
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.errors.MemoryError
joo.classLoader.prepare("package flash.errors",
"public dynamic class MemoryError extends Error",2,function($$private){;return[
"public function MemoryError",function(message){switch(arguments.length){case 0:message="";}
this.super$2(message);
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.errors.ScriptTimeoutError
joo.classLoader.prepare("package flash.errors",
"public dynamic class ScriptTimeoutError extends Error",2,function($$private){;return[
"public function ScriptTimeoutError",function(message){switch(arguments.length){case 0:message="";}
this.super$2(message);
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.errors.StackOverflowError
joo.classLoader.prepare("package flash.errors",
"public dynamic class StackOverflowError extends Error",2,function($$private){;return[
"public function StackOverflowError",function(message){switch(arguments.length){case 0:message="";}
this.super$2(message);
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.ActivityEvent
joo.classLoader.prepare("package flash.events",
"public class ActivityEvent extends flash.events.Event",2,function($$private){;return[
"public native function get activating",
"public native function set activating",
"public function ActivityEvent",function(type,bubbles,cancelable,activating){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;case 3:activating=false;}
this.super$2(flash.events.ActivityEvent.ACTIVITY,bubbles,cancelable);
this.activating=activating;
},
"override public function clone",function(){
return new flash.events.ActivityEvent(this.type,this.bubbles,this.cancelable,this.activating);
},
"override public function toString",function(){
return this.formatToString("ActivityEvent","type","bubbles","cancelable","activating");
},
"public static const",{ACTIVITY:"activity"},
];},[],["flash.events.Event"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.AsyncErrorEvent
joo.classLoader.prepare("package flash.events",
"public class AsyncErrorEvent extends flash.events.ErrorEvent",4,function($$private){;return[
"public var",{error:null},
"public function AsyncErrorEvent",function(type,bubbles,cancelable,text,error){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;case 3:text="";case 4:error=null;}
this.super$4(type,bubbles,cancelable,text);
this.error=error;
},
"override public function clone",function(){
return new flash.events.AsyncErrorEvent(this.type,this.bubbles,this.cancelable,this.text,this.error);
},
"override public function toString",function(){
return this.formatToString("AsyncErrorEvent","type","bubbles","cancelable","error");
},
"public static const",{ASYNC_ERROR:"asyncError"},
];},[],["flash.events.ErrorEvent"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.ContextMenuEvent
joo.classLoader.prepare("package flash.events",
"public class ContextMenuEvent extends flash.events.Event",2,function($$private){;return[
"public native function get contextMenuOwner",
"public native function set contextMenuOwner",
"public native function get mouseTarget",
"public native function set mouseTarget",
"public function ContextMenuEvent",function(type,bubbles,cancelable,mouseTarget,contextMenuOwner){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;case 3:mouseTarget=null;case 4:contextMenuOwner=null;}
this.super$2(type,bubbles,cancelable);
this.mouseTarget=mouseTarget;
this.contextMenuOwner=contextMenuOwner;
},
"override public function clone",function(){
return new flash.events.ContextMenuEvent(this.type,this.bubbles,this.cancelable,this.mouseTarget,this.contextMenuOwner);
},
"override public function toString",function(){
return this.formatToString("ContextMenuEvent","type","bubbles","cancelable","mouseTarget","contextMenuOwner");
},
"public static const",{MENU_ITEM_SELECT:"menuItemSelect"},
"public static const",{MENU_SELECT:"menuSelect"},
];},[],["flash.events.Event"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.DataEvent
joo.classLoader.prepare("package flash.events",
"public class DataEvent extends flash.events.TextEvent",3,function($$private){;return[
"public native function get data",
"public native function set data",
"public function DataEvent",function(type,bubbles,cancelable,data){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;case 3:data="";}
this.super$3(type,bubbles,cancelable);
this.data=data;
},
"override public function clone",function(){
return new flash.events.DataEvent(this.type,this.bubbles,this.cancelable,this.data);
},
"override public function toString",function(){
return this.formatToString("DataEvent","type","bubbles","cancelable","data");
},
"public static const",{DATA:"data"},
"public static const",{UPLOAD_COMPLETE_DATA:"uploadCompleteData"},
];},[],["flash.events.TextEvent"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.ErrorEvent
joo.classLoader.prepare("package flash.events",
"public class ErrorEvent extends flash.events.TextEvent",3,function($$private){;return[
"public function ErrorEvent",function(type,bubbles,cancelable,text,id){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;case 3:text="";case 4:id=0;}
this.super$3(type,bubbles,cancelable,text);
this['id']=id;
},
"override public function clone",function(){
return new flash.events.ErrorEvent(this.type,this.bubbles,this.cancelable,this.text,this.id);
},
"override public function toString",function(){
return this.formatToString("ErrorEvent","type","bubbles","cancelable","text");
},
"public static const",{ERROR:"error"},
"protected native function get id",
];},[],["flash.events.TextEvent"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.Event
joo.classLoader.prepare("package flash.events",
"public class Event",1,function($$private){;return[
"public native function get bubbles",
"public native function get cancelable",
"public native function get currentTarget",
"public native function get eventPhase",
"public native function get target",
"public native function get type",
"public function Event",function(type,bubbles,cancelable){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;}
this['type']=type;
this['bubbles']=bubbles;
this['cancelable']=cancelable;
},
"public function clone",function(){
return new flash.events.Event(this.type,this.bubbles,this.cancelable);
},
"public function formatToString",function(className){var rest=Array.prototype.slice.call(arguments,1);
var sb=["[",className," "];
for(var i=0;i<rest.length;++i){
sb.push(rest[i],"=",this[rest[i]]," ");
}
sb.push("]");
return sb.join("");
},
"public function isDefaultPrevented",function(){
return this.defaultPrevented$1;
},
"public function preventDefault",function(){
if(this.cancelable){
this.defaultPrevented$1=true;
}
},
"public function stopImmediatePropagation",function(){
this.immediatePropagationStopped$1=true;
},
"public function stopPropagation",function(){
this.propagationStopped$1=true;
},
"public function toString",function(){
return this.formatToString("Event","type","bubbles","cancelable");
},
"public static const",{ACTIVATE:"activate"},
"public static const",{ADDED:"added"},
"public static const",{ADDED_TO_STAGE:"addedToStage"},
"public static const",{CANCEL:"cancel"},
"public static const",{CHANGE:"change"},
"public static const",{CLOSE:"close"},
"public static const",{COMPLETE:"complete"},
"public static const",{CONNECT:"connect"},
"public static const",{DEACTIVATE:"deactivate"},
"public static const",{ENTER_FRAME:"enterFrame"},
"public static const",{EXIT_FRAME:"exitFrame"},
"public static const",{FRAME_CONSTRUCTED:"frameConstructed"},
"public static const",{FULLSCREEN:"fullScreen"},
"public static const",{ID3:"id3"},
"public static const",{INIT:"init"},
"public static const",{MOUSE_LEAVE:"mouseLeave"},
"public static const",{OPEN:"open"},
"public static const",{REMOVED:"removed"},
"public static const",{REMOVED_FROM_STAGE:"removedFromStage"},
"public static const",{RENDER:"render"},
"public static const",{RESIZE:"resize"},
"public static const",{SCROLL:"scroll"},
"public static const",{SELECT:"select"},
"public static const",{SOUND_COMPLETE:"soundComplete"},
"public static const",{TAB_CHILDREN_CHANGE:"tabChildrenChange"},
"public static const",{TAB_ENABLED_CHANGE:"tabEnabledChange"},
"public static const",{TAB_INDEX_CHANGE:"tabIndexChange"},
"public static const",{UNLOAD:"unload"},
"public function isPropagationStopped",function(){
return this.propagationStopped$1;
},
"public function isImmediatePropagationStopped",function(){
return this.immediatePropagationStopped$1;
},
"internal function withTarget",function(target){
var event=this.target?this.clone():this;
event['target']=target;
return event;
},
"private var",{defaultPrevented:false},
"private var",{propagationStopped:false},
"private var",{immediatePropagationStopped:false},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.EventDispatcher
joo.classLoader.prepare("package flash.events",
{Event:{name:"activate",type:"flash.events.Event"}},
{Event:{name:"deactivate",type:"flash.events.Event"}},
"public class EventDispatcher implements flash.events.IEventDispatcher",1,function($$private){;return[
"public function EventDispatcher",function(target){switch(arguments.length){case 0:target=null;}
this.target$1=target;
this.captureListeners$1={};
this.listeners$1={};
},
"public function addEventListener",function(type,listener,useCapture,priority,useWeakReference){switch(arguments.length){case 0:case 1:case 2:useCapture=false;case 3:priority=0;case 4:useWeakReference=false;}
var listenersByType=useCapture?this.captureListeners$1:this.listeners$1;
if(!(type in listenersByType)){
listenersByType[type]=[listener];
}else{
listenersByType[type].push(listener);
}
},
"public function dispatchEvent",function(event){
event.withTarget(this.target$1||this);
var listeners=this.listeners$1[event.type];
if(listeners){
for(var i=0;i<listeners.length;++i){
if(listeners[i](event)===false){
event.stopPropagation();
event.preventDefault();
}
if(event.isImmediatePropagationStopped()){
break;
}
}
}
return!event.isDefaultPrevented();
},
"public function hasEventListener",function(type){
return this.listeners$1[type]||this.captureListeners$1[type];
},
"public function removeEventListener",function(type,listener,useCapture){switch(arguments.length){case 0:case 1:case 2:useCapture=false;}
var listenersByType=useCapture?this.captureListeners$1:this.listeners$1;
var listeners=listenersByType[type];
if(listeners){
for(var i=0;i<listeners.length;++i){
if(listeners[i]==listener){
if(listeners.length==1){
delete listenersByType[type];
}else{
listeners.splice(i,1);
}
return;
}
}
}
},
"public function willTrigger",function(type){
return this.hasEventListener(type);
},
"public function toString",function(){
return["EventDispatcher[target=",this.target$1,"]"].join("");
},
"private var",{captureListeners:null},
"private var",{listeners:null},
"private var",{target:null},
];},[],["flash.events.IEventDispatcher"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.EventPhase
joo.classLoader.prepare("package flash.events",
"public final class EventPhase",1,function($$private){;return[
"public static const",{AT_TARGET:2},
"public static const",{BUBBLING_PHASE:3},
"public static const",{CAPTURING_PHASE:1},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.FocusEvent
joo.classLoader.prepare("package flash.events",
"public class FocusEvent extends flash.events.Event",2,function($$private){;return[
"public native function get keyCode",
"public native function set keyCode",
"public native function get relatedObject",
"public native function set relatedObject",
"public native function get shiftKey",
"public native function set shiftKey",
"public function FocusEvent",function(type,bubbles,cancelable,relatedObject,shiftKey,keyCode,direction){switch(arguments.length){case 0:case 1:bubbles=true;case 2:cancelable=false;case 3:relatedObject=null;case 4:shiftKey=false;case 5:keyCode=0;case 6:direction="none";}
this.super$2(type,bubbles,cancelable);
this.relatedObject=relatedObject;
this.shiftKey=shiftKey;
this.keyCode=keyCode;
},
"override public function clone",function(){
return new flash.events.FocusEvent(this.type,this.bubbles,this.cancelable,this.relatedObject,this.shiftKey,this.keyCode);
},
"override public function toString",function(){
return this.formatToString("FocusEvent","type","bubbles","cancelable","relatedObject","shiftKey","keyCode");
},
"public static const",{FOCUS_IN:"focusIn"},
"public static const",{FOCUS_OUT:"focusOut"},
"public static const",{KEY_FOCUS_CHANGE:"keyFocusChange"},
"public static const",{MOUSE_FOCUS_CHANGE:"mouseFocusChange"},
];},[],["flash.events.Event"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.FullScreenEvent
joo.classLoader.prepare("package flash.events",
"public class FullScreenEvent extends flash.events.ActivityEvent",3,function($$private){;return[
"public native function get fullScreen",
"public function FullScreenEvent",function(type,bubbles,cancelable,fullScreen){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;case 3:fullScreen=false;}
this.super$3(type,bubbles,cancelable);
this['fullScreen']=fullScreen;
},
"override public function clone",function(){
return new flash.events.FullScreenEvent(this.type,this.bubbles,this.cancelable,this.fullScreen);
},
"override public function toString",function(){
return this.formatToString("FullScreenEvent","type","bubbles","cancelable","fullScreen");
},
"public static const",{FULL_SCREEN:"fullScreen"},
];},[],["flash.events.ActivityEvent"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.HTTPStatusEvent
joo.classLoader.prepare("package flash.events",
"public class HTTPStatusEvent extends flash.events.Event",2,function($$private){;return[
"public native function get status",
"public function HTTPStatusEvent",function(type,bubbles,cancelable,status){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;case 3:status=0;}
this.super$2(type,bubbles,cancelable);
this['status']=status;
},
"override public function clone",function(){
return new flash.events.HTTPStatusEvent(this.type,this.bubbles,this.cancelable,this.status);
},
"override public function toString",function(){
return this.formatToString("HTTPStatusEvent","type","bubbles","cancelable","status");
},
"public static const",{HTTP_STATUS:"httpStatus"},
];},[],["flash.events.Event"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.IEventDispatcher
joo.classLoader.prepare("package flash.events",
"public interface IEventDispatcher",1,function($$private){;return[,,,,,
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.IMEEvent
joo.classLoader.prepare("package flash.events",
"public class IMEEvent extends flash.events.TextEvent",3,function($$private){;return[
"public function IMEEvent",function(type,bubbles,cancelable,text,imeClient){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;case 3:text="";case 4:imeClient=null;}
this.super$3(type,bubbles,cancelable,text);
},
"override public function clone",function(){
return new flash.events.IMEEvent(this.type,this.bubbles,this.cancelable,this.text);
},
"override public function toString",function(){
return this.formatToString("IMEEvent","type","bubbles","cancelable","text");
},
"public static const",{IME_COMPOSITION:"imeComposition"},
];},[],["flash.events.TextEvent"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.IOErrorEvent
joo.classLoader.prepare("package flash.events",
"public class IOErrorEvent extends flash.events.ErrorEvent",4,function($$private){;return[
"public function IOErrorEvent",function(type,bubbles,cancelable,text,id){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;case 3:text="";case 4:id=0;}
this.super$4(type,bubbles,cancelable,text,id);
},
"override public function clone",function(){
return new flash.events.IOErrorEvent(this.type,this.bubbles,this.cancelable,this.text,this.id);
},
"override public function toString",function(){
return this.formatToString("IOErrorEvent","type","bubbles","cancelable","text");
},
"public static const",{IO_ERROR:"ioError"},
];},[],["flash.events.ErrorEvent"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.KeyboardEvent
joo.classLoader.prepare("package flash.events",
"public class KeyboardEvent extends flash.events.Event",2,function($$private){;return[
"public native function get altKey",
"public native function set altKey",
"public native function get charCode",
"public native function set charCode",
"public native function get ctrlKey",
"public native function set ctrlKey",
"public native function get keyCode",
"public native function set keyCode",
"public native function get keyLocation",
"public native function set keyLocation",
"public native function get shiftKey",
"public native function set shiftKey",
"public function KeyboardEvent",function(type,bubbles,cancelable,charCodeValue,keyCodeValue,keyLocationValue,ctrlKeyValue,altKeyValue,shiftKeyValue,controlKeyValue,commandKeyValue){switch(arguments.length){case 0:case 1:bubbles=true;case 2:cancelable=false;case 3:charCodeValue=0;case 4:keyCodeValue=0;case 5:keyLocationValue=0;case 6:ctrlKeyValue=false;case 7:altKeyValue=false;case 8:shiftKeyValue=false;case 9:controlKeyValue=false;case 10:commandKeyValue=false;}
this.super$2(type,bubbles,cancelable);
this.charCode=charCodeValue;
this.keyCode=keyCodeValue;
this.keyLocation=keyLocationValue;
this.ctrlKey=ctrlKeyValue||controlKeyValue||commandKeyValue;
this.altKey=altKeyValue;
this.shiftKey=shiftKeyValue;
},
"override public function clone",function(){
return new flash.events.KeyboardEvent(this.type,this.bubbles,this.cancelable,this.charCode,this.keyCode,this.keyLocation,this.ctrlKey,this.altKey,
this.shiftKey,this.ctrlKey,this.ctrlKey);
},
"override public function toString",function(){
return this.formatToString("KeyboardEvent","type","bubbles","cancelable",
"charCode","keyCode","keyLocation","ctrlKey","altKey","shiftKey","controlKey","commandKey");
},
"public function updateAfterEvent",function(){
},
"public static const",{KEY_DOWN:"keyDown"},
"public static const",{KEY_UP:"keyUp"},
];},[],["flash.events.Event"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.MouseEvent
joo.classLoader.prepare("package flash.events",
"public class MouseEvent extends flash.events.Event",2,function($$private){var as=joo.as;return[
"public native function get altKey",
"public native function set altKey",
"public native function get buttonDown",
"public native function set buttonDown",
"public native function get ctrlKey",
"public native function set ctrlKey",
"public native function get delta",
"public native function set delta",
"public native function get localX",
"public native function set localX",
"public native function get localY",
"public native function set localY",
"public native function get relatedObject",
"public native function set relatedObject",
"public native function get shiftKey",
"public native function set shiftKey",
"public function get stageX",function(){
return(as(this.target,flash.display.DisplayObject)).x+this.localX;
},
"public function get stageY",function(){
return(as(this.target,flash.display.DisplayObject)).y+this.localY;
},
"public function MouseEvent",function(type,bubbles,cancelable,localX,localY,relatedObject,ctrlKey,altKey,shiftKey,buttonDown,delta,commandKey,controlKey,clickCount){switch(arguments.length){case 0:case 1:bubbles=true;case 2:cancelable=false;case 3:localX=NaN;case 4:localY=NaN;case 5:relatedObject=null;case 6:ctrlKey=false;case 7:altKey=false;case 8:shiftKey=false;case 9:buttonDown=false;case 10:delta=0;case 11:commandKey=false;case 12:controlKey=false;case 13:clickCount=0;}
this.super$2(type,bubbles,cancelable);
this.localX=localX;
this.localY=localY;
this.relatedObject=relatedObject;
this.ctrlKey=ctrlKey;
this.altKey=altKey;
this.shiftKey=shiftKey;
this.buttonDown=buttonDown;
this.delta=delta;
},
"override public function clone",function(){
return new flash.events.MouseEvent(this.type,this.bubbles,this.cancelable,this.localX,this.localY,this.relatedObject,this.ctrlKey,this.altKey,this.shiftKey,this.buttonDown,this.delta);
},
"override public function toString",function(){
return this.formatToString("MouseEvent","type","bubbles","cancelable",
"localX","localY","relatedObject","ctrlKey","altKey","shiftKey","buttonDown","delta");
},
"public function updateAfterEvent",function(){
},
"public static const",{CLICK:"click"},
"public static const",{DOUBLE_CLICK:"doubleClick"},
"public static const",{MOUSE_DOWN:"mouseDown"},
"public static const",{MOUSE_MOVE:"mouseMove"},
"public static const",{MOUSE_OUT:"mouseOut"},
"public static const",{MOUSE_OVER:"mouseOver"},
"public static const",{MOUSE_UP:"mouseUp"},
"public static const",{MOUSE_WHEEL:"mouseWheel"},
"public static const",{ROLL_OUT:"rollOut"},
"public static const",{ROLL_OVER:"rollOver"},
];},[],["flash.events.Event","flash.display.DisplayObject"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.NetStatusEvent
joo.classLoader.prepare("package flash.events",
"public class NetStatusEvent extends flash.events.Event",2,function($$private){;return[
"public native function get info",
"public native function set info",
"public function NetStatusEvent",function(type,bubbles,cancelable,info){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;case 3:info=null;}
this.super$2(type,bubbles,cancelable);
this.info=info;
},
"override public function clone",function(){
return new flash.events.NetStatusEvent(this.type,this.bubbles,this.cancelable,this.info);
},
"override public function toString",function(){
return this.formatToString("NetStatusEvent","type","bubbles","cancelable","info");
},
"public static const",{NET_STATUS:"netStatus"},
];},[],["flash.events.Event"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.ProgressEvent
joo.classLoader.prepare("package flash.events",
"public class ProgressEvent extends flash.events.Event",2,function($$private){;return[
"public native function get bytesLoaded",
"public native function set bytesLoaded",
"public native function get bytesTotal",
"public native function set bytesTotal",
"public function ProgressEvent",function(type,bubbles,cancelable,bytesLoaded,bytesTotal){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;case 3:bytesLoaded=0;case 4:bytesTotal=0;}
this.super$2(type,bubbles,cancelable);
this.bytesLoaded=bytesLoaded;
this.bytesTotal=bytesTotal;
},
"override public function clone",function(){
return new flash.events.ProgressEvent(this.type,this.bubbles,this.cancelable,this.bytesLoaded,this.bytesTotal);
},
"override public function toString",function(){
return this.formatToString("TimerEvent","type","bubbles","cancelable","bytesLoaded","bytesTotal");
},
"public static const",{PROGRESS:"progress"},
"public static const",{SOCKET_DATA:"socketData"},
];},[],["flash.events.Event"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.SecurityErrorEvent
joo.classLoader.prepare("package flash.events",
"public class SecurityErrorEvent extends flash.events.ErrorEvent",4,function($$private){;return[
"public function SecurityErrorEvent",function(type,bubbles,cancelable,text,id){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;case 3:text="";case 4:id=0;}
this.super$4(type,bubbles,cancelable,text,id);
},
"override public function clone",function(){
return new flash.events.SecurityErrorEvent(this.type,this.bubbles,this.cancelable,this.text,this.id);
},
"override public function toString",function(){
return this.formatToString("SecurityErrorEvent","type","bubbles","cancelable","text");
},
"public static const",{SECURITY_ERROR:"securityError"},
];},[],["flash.events.ErrorEvent"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.StatusEvent
joo.classLoader.prepare("package flash.events",
"public class StatusEvent extends flash.events.Event",2,function($$private){;return[
"public native function get code",
"public native function set code",
"public native function get level",
"public native function set level",
"public function StatusEvent",function(type,bubbles,cancelable,code,level){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;case 3:code="";case 4:level="";}
this.super$2(type,bubbles,cancelable);
this.code=code;
this.level=level;
},
"override public function clone",function(){
return new flash.events.StatusEvent(this.type,this.bubbles,this.cancelable,this.code,this.level);
},
"override public function toString",function(){
return this.formatToString("StatusEvent","type","bubbles","cancelable","code","level");
},
"public static const",{STATUS:"status"},
];},[],["flash.events.Event"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.SyncEvent
joo.classLoader.prepare("package flash.events",
"public class SyncEvent extends flash.events.Event",2,function($$private){;return[
"public native function get changeList",
"public native function set changeList",
"public function SyncEvent",function(type,bubbles,cancelable,changeList){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;case 3:changeList=null;}
this.super$2(type,bubbles,cancelable);
this.changeList=changeList;
},
"override public function clone",function(){
return new flash.events.SyncEvent(this.type,this.bubbles,this.cancelable,this.changeList);
},
"override public function toString",function(){
return this.formatToString("SyncEvent","type","bubbles","cancelable","changeList");
},
"public static const",{SYNC:"sync"},
];},[],["flash.events.Event"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.TextEvent
joo.classLoader.prepare("package flash.events",
"public class TextEvent extends flash.events.Event",2,function($$private){;return[
"public native function get text",
"public native function set text",
"public function TextEvent",function(type,bubbles,cancelable,text){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;case 3:text="";}
this.super$2(type,bubbles,cancelable);
this.text=text;
},
"override public function clone",function(){
return new flash.events.TextEvent(this.type,this.bubbles,this.cancelable,this.text);
},
"override public function toString",function(){
return this.formatToString("TextEvent","type","bubbles","cancelable","text");
},
"public static const",{LINK:"link"},
"public static const",{TEXT_INPUT:"textInput"},
];},[],["flash.events.Event"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.events.TimerEvent
joo.classLoader.prepare("package flash.events",
"public class TimerEvent extends flash.events.Event",2,function($$private){;return[
"public function TimerEvent",function(type,bubbles,cancelable){switch(arguments.length){case 0:case 1:bubbles=false;case 2:cancelable=false;}
this.super$2(type,bubbles,cancelable);
},
"override public function clone",function(){
return new flash.events.TimerEvent(this.type,this.bubbles,this.cancelable);
},
"override public function toString",function(){
return this.formatToString("TimerEvent","type","bubbles","cancelable");
},
"public function updateAfterEvent",function(){
},
"public static const",{TIMER:"timer"},
"public static const",{TIMER_COMPLETE:"timerComplete"},
];},[],["flash.events.Event"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.external.ExternalInterface
joo.classLoader.prepare("package flash.external",
"public final class ExternalInterface",1,function($$private){;return[
"public static function get available",function(){
throw new Error('not implemented');
},
"public static var",{marshallExceptions:false},
"public static function get objectID",function(){
throw new Error('not implemented');
},
"public static function addCallback",function(functionName,closure){
throw new Error('not implemented');
},
"public static function call",function(functionName){var rest=Array.prototype.slice.call(arguments,1);
throw new Error('not implemented');
},
];},["available","objectID","addCallback","call"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.filters.BevelFilter
joo.classLoader.prepare("package flash.filters",
"public final class BevelFilter extends flash.filters.BitmapFilter",2,function($$private){;return[
"public function get angle",function(){
throw new Error('not implemented');
},
"public function set angle",function(value){
throw new Error('not implemented');
},
"public function get blurX",function(){
throw new Error('not implemented');
},
"public function set blurX",function(value){
throw new Error('not implemented');
},
"public function get blurY",function(){
throw new Error('not implemented');
},
"public function set blurY",function(value){
throw new Error('not implemented');
},
"public function get distance",function(){
throw new Error('not implemented');
},
"public function set distance",function(value){
throw new Error('not implemented');
},
"public function get highlightAlpha",function(){
throw new Error('not implemented');
},
"public function set highlightAlpha",function(value){
throw new Error('not implemented');
},
"public function get highlightColor",function(){
throw new Error('not implemented');
},
"public function set highlightColor",function(value){
throw new Error('not implemented');
},
"public function get knockout",function(){
throw new Error('not implemented');
},
"public function set knockout",function(value){
throw new Error('not implemented');
},
"public function get quality",function(){
throw new Error('not implemented');
},
"public function set quality",function(value){
throw new Error('not implemented');
},
"public function get shadowAlpha",function(){
throw new Error('not implemented');
},
"public function set shadowAlpha",function(value){
throw new Error('not implemented');
},
"public function get shadowColor",function(){
throw new Error('not implemented');
},
"public function set shadowColor",function(value){
throw new Error('not implemented');
},
"public function get strength",function(){
throw new Error('not implemented');
},
"public function set strength",function(value){
throw new Error('not implemented');
},
"public function get type",function(){
throw new Error('not implemented');
},
"public function set type",function(value){
throw new Error('not implemented');
},
"public function BevelFilter",function(distance,angle,highlightColor,highlightAlpha,shadowColor,shadowAlpha,blurX,blurY,strength,quality,type,knockout){switch(arguments.length){case 0:distance=4.0;case 1:angle=45;case 2:highlightColor=0xFFFFFF;case 3:highlightAlpha=1.0;case 4:shadowColor=0x000000;case 5:shadowAlpha=1.0;case 6:blurX=4.0;case 7:blurY=4.0;case 8:strength=1;case 9:quality=1;case 10:type="inner";case 11:knockout=false;}this.super$2();
throw new Error('not implemented');
},
"override public function clone",function(){
throw new Error('not implemented');
},
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.filters.BitmapFilter
joo.classLoader.prepare("package flash.filters",
"public class BitmapFilter",1,function($$private){;return[
"public function clone",function(){
throw new Error('not implemented');
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.filters.BitmapFilterQuality
joo.classLoader.prepare("package flash.filters",
"public final class BitmapFilterQuality",1,function($$private){;return[
"public static const",{HIGH:3},
"public static const",{LOW:1},
"public static const",{MEDIUM:2},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.filters.BitmapFilterType
joo.classLoader.prepare("package flash.filters",
"public final class BitmapFilterType",1,function($$private){;return[
"public static const",{FULL:"full"},
"public static const",{INNER:"inner"},
"public static const",{OUTER:"outer"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.filters.BlurFilter
joo.classLoader.prepare("package flash.filters",
"public final class BlurFilter extends flash.filters.BitmapFilter",2,function($$private){;return[
"public function get blurX",function(){
throw new Error('not implemented');
},
"public function set blurX",function(value){
throw new Error('not implemented');
},
"public function get blurY",function(){
throw new Error('not implemented');
},
"public function set blurY",function(value){
throw new Error('not implemented');
},
"public function get quality",function(){
throw new Error('not implemented');
},
"public function set quality",function(value){
throw new Error('not implemented');
},
"public function BlurFilter",function(blurX,blurY,quality){switch(arguments.length){case 0:blurX=4.0;case 1:blurY=4.0;case 2:quality=1;}this.super$2();
throw new Error('not implemented');
},
"override public function clone",function(){
throw new Error('not implemented');
},
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.filters.ColorMatrixFilter
joo.classLoader.prepare("package flash.filters",
"public final class ColorMatrixFilter extends flash.filters.BitmapFilter",2,function($$private){;return[
"public function get matrix",function(){
throw new Error('not implemented');
},
"public function set matrix",function(value){
throw new Error('not implemented');
},
"public function ColorMatrixFilter",function(matrix){switch(arguments.length){case 0:matrix=null;}this.super$2();
throw new Error('not implemented');
},
"override public function clone",function(){
throw new Error('not implemented');
},
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.filters.ConvolutionFilter
joo.classLoader.prepare("package flash.filters",
"public class ConvolutionFilter extends flash.filters.BitmapFilter",2,function($$private){;return[
"public function get alpha",function(){
throw new Error('not implemented');
},
"public function set alpha",function(value){
throw new Error('not implemented');
},
"public function get bias",function(){
throw new Error('not implemented');
},
"public function set bias",function(value){
throw new Error('not implemented');
},
"public function get clamp",function(){
throw new Error('not implemented');
},
"public function set clamp",function(value){
throw new Error('not implemented');
},
"public function get color",function(){
throw new Error('not implemented');
},
"public function set color",function(value){
throw new Error('not implemented');
},
"public function get divisor",function(){
throw new Error('not implemented');
},
"public function set divisor",function(value){
throw new Error('not implemented');
},
"public function get matrix",function(){
throw new Error('not implemented');
},
"public function set matrix",function(value){
throw new Error('not implemented');
},
"public function get matrixX",function(){
throw new Error('not implemented');
},
"public function set matrixX",function(value){
throw new Error('not implemented');
},
"public function get matrixY",function(){
throw new Error('not implemented');
},
"public function set matrixY",function(value){
throw new Error('not implemented');
},
"public function get preserveAlpha",function(){
throw new Error('not implemented');
},
"public function set preserveAlpha",function(value){
throw new Error('not implemented');
},
"public function ConvolutionFilter",function(matrixX,matrixY,matrix,divisor,bias,preserveAlpha,clamp,color,alpha){switch(arguments.length){case 0:matrixX=0;case 1:matrixY=0;case 2:matrix=null;case 3:divisor=1.0;case 4:bias=0.0;case 5:preserveAlpha=true;case 6:clamp=true;case 7:color=0;case 8:alpha=0.0;}this.super$2();
throw new Error('not implemented');
},
"override public function clone",function(){
throw new Error('not implemented');
},
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.filters.DisplacementMapFilter
joo.classLoader.prepare("package flash.filters",
"public final class DisplacementMapFilter extends flash.filters.BitmapFilter",2,function($$private){;return[
"public function get alpha",function(){
throw new Error('not implemented');
},
"public function set alpha",function(value){
throw new Error('not implemented');
},
"public function get color",function(){
throw new Error('not implemented');
},
"public function set color",function(value){
throw new Error('not implemented');
},
"public function get componentX",function(){
throw new Error('not implemented');
},
"public function set componentX",function(value){
throw new Error('not implemented');
},
"public function get componentY",function(){
throw new Error('not implemented');
},
"public function set componentY",function(value){
throw new Error('not implemented');
},
"public function get mapBitmap",function(){
throw new Error('not implemented');
},
"public function set mapBitmap",function(value){
throw new Error('not implemented');
},
"public function get mapPoint",function(){
throw new Error('not implemented');
},
"public function set mapPoint",function(value){
throw new Error('not implemented');
},
"public function get mode",function(){
throw new Error('not implemented');
},
"public function set mode",function(value){
throw new Error('not implemented');
},
"public function get scaleX",function(){
throw new Error('not implemented');
},
"public function set scaleX",function(value){
throw new Error('not implemented');
},
"public function get scaleY",function(){
throw new Error('not implemented');
},
"public function set scaleY",function(value){
throw new Error('not implemented');
},
"public function DisplacementMapFilter",function(mapBitmap,mapPoint,componentX,componentY,scaleX,scaleY,mode,color,alpha){switch(arguments.length){case 0:mapBitmap=null;case 1:mapPoint=null;case 2:componentX=0;case 3:componentY=0;case 4:scaleX=0.0;case 5:scaleY=0.0;case 6:mode="wrap";case 7:color=0;case 8:alpha=0.0;}this.super$2();
throw new Error('not implemented');
},
"override public function clone",function(){
throw new Error('not implemented');
},
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.filters.DisplacementMapFilterMode
joo.classLoader.prepare("package flash.filters",
"public final class DisplacementMapFilterMode",1,function($$private){;return[
"public static const",{CLAMP:"clamp"},
"public static const",{COLOR:"color"},
"public static const",{IGNORE:"ignore"},
"public static const",{WRAP:"wrap"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.filters.DropShadowFilter
joo.classLoader.prepare("package flash.filters",
"public final class DropShadowFilter extends flash.filters.BitmapFilter",2,function($$private){;return[
"public function get alpha",function(){
throw new Error('not implemented');
},
"public function set alpha",function(value){
throw new Error('not implemented');
},
"public function get angle",function(){
throw new Error('not implemented');
},
"public function set angle",function(value){
throw new Error('not implemented');
},
"public function get blurX",function(){
throw new Error('not implemented');
},
"public function set blurX",function(value){
throw new Error('not implemented');
},
"public function get blurY",function(){
throw new Error('not implemented');
},
"public function set blurY",function(value){
throw new Error('not implemented');
},
"public function get color",function(){
throw new Error('not implemented');
},
"public function set color",function(value){
throw new Error('not implemented');
},
"public function get distance",function(){
throw new Error('not implemented');
},
"public function set distance",function(value){
throw new Error('not implemented');
},
"public function get hideObject",function(){
throw new Error('not implemented');
},
"public function set hideObject",function(value){
throw new Error('not implemented');
},
"public function get inner",function(){
throw new Error('not implemented');
},
"public function set inner",function(value){
throw new Error('not implemented');
},
"public function get knockout",function(){
throw new Error('not implemented');
},
"public function set knockout",function(value){
throw new Error('not implemented');
},
"public function get quality",function(){
throw new Error('not implemented');
},
"public function set quality",function(value){
throw new Error('not implemented');
},
"public function get strength",function(){
throw new Error('not implemented');
},
"public function set strength",function(value){
throw new Error('not implemented');
},
"public function DropShadowFilter",function(distance,angle,color,alpha,blurX,blurY,strength,quality,inner,knockout,hideObject){switch(arguments.length){case 0:distance=4.0;case 1:angle=45;case 2:color=0;case 3:alpha=1.0;case 4:blurX=4.0;case 5:blurY=4.0;case 6:strength=1.0;case 7:quality=1;case 8:inner=false;case 9:knockout=false;case 10:hideObject=false;}this.super$2();
throw new Error('not implemented');
},
"override public function clone",function(){
throw new Error('not implemented');
},
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.filters.GlowFilter
joo.classLoader.prepare("package flash.filters",
"public final class GlowFilter extends flash.filters.BitmapFilter",2,function($$private){;return[
"public function get alpha",function(){
throw new Error('not implemented');
},
"public function set alpha",function(value){
throw new Error('not implemented');
},
"public function get blurX",function(){
throw new Error('not implemented');
},
"public function set blurX",function(value){
throw new Error('not implemented');
},
"public function get blurY",function(){
throw new Error('not implemented');
},
"public function set blurY",function(value){
throw new Error('not implemented');
},
"public function get color",function(){
throw new Error('not implemented');
},
"public function set color",function(value){
throw new Error('not implemented');
},
"public function get inner",function(){
throw new Error('not implemented');
},
"public function set inner",function(value){
throw new Error('not implemented');
},
"public function get knockout",function(){
throw new Error('not implemented');
},
"public function set knockout",function(value){
throw new Error('not implemented');
},
"public function get quality",function(){
throw new Error('not implemented');
},
"public function set quality",function(value){
throw new Error('not implemented');
},
"public function get strength",function(){
throw new Error('not implemented');
},
"public function set strength",function(value){
throw new Error('not implemented');
},
"public function GlowFilter",function(color,alpha,blurX,blurY,strength,quality,inner,knockout){switch(arguments.length){case 0:color=0xFF0000;case 1:alpha=1.0;case 2:blurX=6.0;case 3:blurY=6.0;case 4:strength=2;case 5:quality=1;case 6:inner=false;case 7:knockout=false;}this.super$2();
throw new Error('not implemented');
},
"override public function clone",function(){
throw new Error('not implemented');
},
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.filters.GradientBevelFilter
joo.classLoader.prepare("package flash.filters",
"public final class GradientBevelFilter extends flash.filters.BitmapFilter",2,function($$private){;return[
"public function get alphas",function(){
throw new Error('not implemented');
},
"public function set alphas",function(value){
throw new Error('not implemented');
},
"public function get angle",function(){
throw new Error('not implemented');
},
"public function set angle",function(value){
throw new Error('not implemented');
},
"public function get blurX",function(){
throw new Error('not implemented');
},
"public function set blurX",function(value){
throw new Error('not implemented');
},
"public function get blurY",function(){
throw new Error('not implemented');
},
"public function set blurY",function(value){
throw new Error('not implemented');
},
"public function get colors",function(){
throw new Error('not implemented');
},
"public function set colors",function(value){
throw new Error('not implemented');
},
"public function get distance",function(){
throw new Error('not implemented');
},
"public function set distance",function(value){
throw new Error('not implemented');
},
"public function get knockout",function(){
throw new Error('not implemented');
},
"public function set knockout",function(value){
throw new Error('not implemented');
},
"public function get quality",function(){
throw new Error('not implemented');
},
"public function set quality",function(value){
throw new Error('not implemented');
},
"public function get ratios",function(){
throw new Error('not implemented');
},
"public function set ratios",function(value){
throw new Error('not implemented');
},
"public function get strength",function(){
throw new Error('not implemented');
},
"public function set strength",function(value){
throw new Error('not implemented');
},
"public function get type",function(){
throw new Error('not implemented');
},
"public function set type",function(value){
throw new Error('not implemented');
},
"public function GradientBevelFilter",function(distance,angle,colors,alphas,ratios,blurX,blurY,strength,quality,type,knockout){switch(arguments.length){case 0:distance=4.0;case 1:angle=45;case 2:colors=null;case 3:alphas=null;case 4:ratios=null;case 5:blurX=4.0;case 6:blurY=4.0;case 7:strength=1;case 8:quality=1;case 9:type="inner";case 10:knockout=false;}this.super$2();
throw new Error('not implemented');
},
"override public function clone",function(){
throw new Error('not implemented');
},
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.filters.GradientGlowFilter
joo.classLoader.prepare("package flash.filters",
"public final class GradientGlowFilter extends flash.filters.BitmapFilter",2,function($$private){;return[
"public function get alphas",function(){
throw new Error('not implemented');
},
"public function set alphas",function(value){
throw new Error('not implemented');
},
"public function get angle",function(){
throw new Error('not implemented');
},
"public function set angle",function(value){
throw new Error('not implemented');
},
"public function get blurX",function(){
throw new Error('not implemented');
},
"public function set blurX",function(value){
throw new Error('not implemented');
},
"public function get blurY",function(){
throw new Error('not implemented');
},
"public function set blurY",function(value){
throw new Error('not implemented');
},
"public function get colors",function(){
throw new Error('not implemented');
},
"public function set colors",function(value){
throw new Error('not implemented');
},
"public function get distance",function(){
throw new Error('not implemented');
},
"public function set distance",function(value){
throw new Error('not implemented');
},
"public function get knockout",function(){
throw new Error('not implemented');
},
"public function set knockout",function(value){
throw new Error('not implemented');
},
"public function get quality",function(){
throw new Error('not implemented');
},
"public function set quality",function(value){
throw new Error('not implemented');
},
"public function get ratios",function(){
throw new Error('not implemented');
},
"public function set ratios",function(value){
throw new Error('not implemented');
},
"public function get strength",function(){
throw new Error('not implemented');
},
"public function set strength",function(value){
throw new Error('not implemented');
},
"public function get type",function(){
throw new Error('not implemented');
},
"public function set type",function(value){
throw new Error('not implemented');
},
"public function GradientGlowFilter",function(distance,angle,colors,alphas,ratios,blurX,blurY,strength,quality,type,knockout){switch(arguments.length){case 0:distance=4.0;case 1:angle=45;case 2:colors=null;case 3:alphas=null;case 4:ratios=null;case 5:blurX=4.0;case 6:blurY=4.0;case 7:strength=1;case 8:quality=1;case 9:type="inner";case 10:knockout=false;}this.super$2();
throw new Error('not implemented');
},
"override public function clone",function(){
throw new Error('not implemented');
},
];},[],["flash.filters.BitmapFilter","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.geom.ColorTransform
joo.classLoader.prepare("package flash.geom",
"public class ColorTransform",1,function($$private){;return[
"public var",{alphaMultiplier:NaN},
"public var",{alphaOffset:NaN},
"public var",{blueMultiplier:NaN},
"public var",{blueOffset:NaN},
"public function get color",function(){
return this.redOffset<<16|this.greenOffset<<8||this.blueOffset;
},
"public function set color",function(value){
this.redOffset=value>>16&0xF;
this.greenOffset=value>>8&0xF;
this.blueOffset=value&0xF;
this.redMultiplier=this.greenMultiplier=this.blueMultiplier=1;
},
"public var",{greenMultiplier:NaN},
"public var",{greenOffset:NaN},
"public var",{redMultiplier:NaN},
"public var",{redOffset:NaN},
"public function ColorTransform",function(redMultiplier,greenMultiplier,blueMultiplier,alphaMultiplier,redOffset,greenOffset,blueOffset,alphaOffset){switch(arguments.length){case 0:redMultiplier=1.0;case 1:greenMultiplier=1.0;case 2:blueMultiplier=1.0;case 3:alphaMultiplier=1.0;case 4:redOffset=0;case 5:greenOffset=0;case 6:blueOffset=0;case 7:alphaOffset=0;}
this.redMultiplier=redMultiplier;
this.greenMultiplier=greenMultiplier;
this.blueMultiplier=blueMultiplier;
this.alphaMultiplier=alphaMultiplier;
this.redOffset=redOffset;
this.greenOffset=greenOffset;
this.blueOffset=blueOffset;
this.alphaOffset=alphaOffset;
},
"public function concat",function(second){
this.redMultiplier*=second.redMultiplier;
this.greenMultiplier*=second.greenMultiplier;
this.blueMultiplier*=second.blueMultiplier;
this.alphaMultiplier*=second.alphaMultiplier;
this.redOffset+=second.redOffset;
this.greenOffset+=second.greenOffset;
this.blueOffset+=second.blueOffset;
this.alphaOffset+=second.alphaOffset;
},
"public function toString",function(){
return"[ColorTransform("+[this.redMultiplier,this.greenMultiplier,this.blueMultiplier,this.alphaMultiplier,
this.redOffset,this.greenOffset,this.blueOffset,this.alphaOffset].join(", ")+")]";
},
"private var",{maps:null},
"public function getComponentMaps",function(){
if(!this.maps$1){
var offsets=[this.redOffset,this.greenOffset,this.blueOffset,this.alphaOffset];
var multipliers=[this.redMultiplier,this.greenMultiplier,this.blueMultiplier,this.alphaMultiplier];
this.maps$1=new Array(4);
for(var c=0;c<4;++c){
var offset=offsets[c];
var multiplier=multipliers[c];
var map;
if(offset==0&&multiplier==1){
map=null;
}else{
map=new Array(256);
for(var b=0;b<256;++b){
var val=offset+multiplier*b;
map[b]=val<=0?0:val<=255?val:255;
}
}
this.maps$1[c]=map;
}
}
return this.maps$1;
},
];},[],["Array"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.geom.Matrix
joo.classLoader.prepare("package flash.geom",
"public class Matrix",1,function($$private){;return[
"public var",{a:NaN},
"public var",{b:NaN},
"public var",{c:NaN},
"public var",{d:NaN},
"public var",{tx:NaN},
"public var",{ty:NaN},
"public function Matrix",function(a,b,c,d,tx,ty){switch(arguments.length){case 0:a=1;case 1:b=0;case 2:c=0;case 3:d=1;case 4:tx=0;case 5:ty=0;}
this.a=a;
this.b=b;
this.c=c;
this.d=d;
this.tx=tx;
this.ty=ty;
},
"public function clone",function(){
return new flash.geom.Matrix(this.a,this.b,this.c,this.d,this.tx,this.ty);
},
"public function concat",function(m){
var a=this.a;
var b=this.b;
var c=this.c;
var d=this.d;
var tx=this.tx;
var ty=this.ty;
this.a=m.a*a+m.c*b;
this.b=m.b*a+m.d*b;
this.c=m.a*c+m.c*d;
this.d=m.b*c+m.d*d;
this.tx=m.a*tx+m.c*ty+m.tx;
this.ty=m.b*tx+m.d*ty+m.ty;
},
"public function createBox",function(scaleX,scaleY,rotation,tx,ty){switch(arguments.length){case 0:case 1:case 2:rotation=0;case 3:tx=0;case 4:ty=0;}
if(rotation==0){
this.a=this.d=1;
this.b=this.c=0;
}else{
this.a=Math.cos(rotation);
this.b=Math.sin(rotation);
this.c=-this.b;
this.d=this.a;
}
if(scaleX!=1){
this.a*=scaleX;
this.c*=scaleY;
}
if(scaleY!=1){
this.b*=scaleY;
this.d*=scaleY;
}
this.tx=tx;
this.ty=ty;
},
"public function createGradientBox",function(width,height,rotation,tx,ty){switch(arguments.length){case 0:case 1:case 2:rotation=0;case 3:tx=0;case 4:ty=0;}
this.createBox(width/flash.geom.Matrix.MAGIC_GRADIENT_FACTOR,height/flash.geom.Matrix.MAGIC_GRADIENT_FACTOR,rotation,tx+width/2,ty+height/2);
},
"public function deltaTransformPoint",function(point){
return new flash.geom.Point(this.a*point.x+this.c*point.y,this.b*point.x+this.d*point.y);
},
"public function identity",function(){
this.a=this.d=1;
this.b=this.c=this.tx=this.ty=0;
},
"public function invert",function(){
var a=this.a;
var b=this.b;
var c=this.c;
var d=this.d;
var tx=this.tx;
var ty=this.ty;
var det=a*d-c*b;
this.a=d/det;
this.b=-b/det;
this.c=-c/det;
this.d=a/det;
this.tx=(c*ty-tx*d)/det;
this.ty=(tx*b-a*ty)/det;
},
"public function rotate",function(angle){
if(angle!=0){
var cos=Math.cos(angle);
var sin=Math.sin(angle);
var a=this.a;
var b=this.b;
var c=this.c;
var d=this.d;
var tx=this.tx;
var ty=this.ty;
this.a=a*cos-c*sin;
this.b=a*sin+c*cos;
this.c=b*cos-d*sin;
this.d=b*sin+d*cos;
this.tx=tx*cos-ty*sin;
this.ty=tx*sin+ty*cos;
}
},
"public function scale",function(sx,sy){
if(sx!=1){
this.a*=sx;
this.tx*=sx;
}
if(sy!=1){
this.d*=sy;
this.ty*=sy;
}
},
"public function toString",function(){
return"("+["a="+this.a,"b="+this.b,"c="+this.c,"d="+this.d,"tx="+this.tx,"ty="+this.ty].join(", ")+")";
},
"public function transformPoint",function(point){
return new flash.geom.Point(this.a*point.x+this.c*point.y+this.tx,this.b*point.x+this.d*point.y+this.ty);
},
"public function translate",function(dx,dy){
this.tx+=dx;
this.ty+=dy;
},
"public static const",{MAGIC_GRADIENT_FACTOR:16384/10},
];},[],["Math","flash.geom.Point"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.geom.Point
joo.classLoader.prepare("package flash.geom",
"public class Point",1,function($$private){;return[
"public function get length",function(){
return Math.sqrt(this.x^2+this.y^2);
},
"public var",{x:NaN},
"public var",{y:NaN},
"public function Point",function(x,y){switch(arguments.length){case 0:x=0;case 1:y=0;}
this.x=x;
this.y=y;
},
"public function add",function(v){
return new flash.geom.Point(this.x+v.x,this.y+v.y);
},
"public function clone",function(){
return new flash.geom.Point(this.x,this.y);
},
"public static function distance",function(pt1,pt2){
return Math.sqrt((pt2.x-pt1.x)^2+(pt2.y-pt2.y)^2);
},
"public function equals",function(toCompare){
return this.x==toCompare.x&&this.y==toCompare.y;
},
"public static function interpolate",function(pt1,pt2,f){
throw new Error('not implemented');
},
"public function normalize",function(thickness){
throw new Error('not implemented');
},
"public function offset",function(dx,dy){
this.x+=dx;
this.y+=dy;
},
"public static function polar",function(len,angle){
throw new Error('not implemented');
},
"public function subtract",function(v){
throw new Error('not implemented');
},
"public function toString",function(){
return["(x=",this.x,", y=",this.y,")"].join("");
},
];},["distance","interpolate","polar"],["Math","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.geom.Rectangle
joo.classLoader.prepare("package flash.geom",
"public class Rectangle",1,function($$private){;return[
"public function get bottom",function(){
return this.y+this.height;
},
"public function set bottom",function(value){
this.height=Math.max(value-this.y,0);
},
"public function get bottomRight",function(){
return new flash.geom.Point(this.right,this.bottom);
},
"public function set bottomRight",function(value){
this.right=value.x;
this.bottom=value.y;
},
"public var",{height:NaN},
"public function get left",function(){
return this.x;
},
"public function set left",function(value){
this.width+=this.x-value;
this.x=value;
},
"public function get right",function(){
return this.x+this.width;
},
"public function set right",function(value){
this.width=value-this.x;
},
"public function get size",function(){
return new flash.geom.Point(this.width,this.height);
},
"public function set size",function(value){
this.width=value.x;
this.height=value.y;
},
"public function get top",function(){
return this.y;
},
"public function set top",function(value){
this.height+=this.y-value;
this.y=value;
},
"public function get topLeft",function(){
return new flash.geom.Point(this.x,this.y);
},
"public function set topLeft",function(value){
this.left=value.x;
this.top=value.y;
},
"public var",{width:NaN},
"public var",{x:NaN},
"public var",{y:NaN},
"public function Rectangle",function(x,y,width,height){switch(arguments.length){case 0:x=0;case 1:y=0;case 2:width=0;case 3:height=0;}
this.x=x;
this.y=y;
this.width=width;
this.height=height;
},
"public function clone",function(){
return new flash.geom.Rectangle(this.x,this.y,this.width,this.height);
},
"public function contains",function(x,y){
return this.x<=x&&x<this.right&&this.y<=y&&y<this.bottom;
},
"public function containsPoint",function(point){
return this.contains(point.x,point.y);
},
"public function containsRect",function(rect){
return this.containsPoint(rect.topLeft)&&this.containsPoint(rect.bottomRight);
},
"public function equals",function(toCompare){
return this.x==toCompare.x&&this.y==toCompare.y&&this.width==toCompare.width&&this.height==toCompare.height;
},
"public function inflate",function(dx,dy){
this.width+=dx;
this.height+=dy;
},
"public function inflatePoint",function(point){
this.inflate(point.x,point.y);
},
"public function intersection",function(toIntersect){
var x=Math.max(this.x,toIntersect.x);
var right=Math.min(this.right,toIntersect.right);
if(x<=right){
var y=Math.max(this.y,toIntersect.y);
var bottom=Math.min(this.bottom,toIntersect.bottom);
if(y<=bottom){
return new flash.geom.Rectangle(x,y,right-x,bottom-y);
}
}
return new flash.geom.Rectangle();
},
"public function intersects",function(toIntersect){
return Math.max(this.x,toIntersect.x)<=Math.min(this.right,toIntersect.right)
&&Math.max(this.y,toIntersect.y)<=Math.min(this.bottom,toIntersect.bottom);
},
"public function isEmpty",function(){
return this.x==0&&this.y==0&&this.width==0&&this.height==0;
},
"public function offset",function(dx,dy){
this.x+=dx;
this.y+=dy;
},
"public function offsetPoint",function(point){
this.offset(point.x,point.y);
},
"public function setEmpty",function(){
this.x=this.y=this.width=this.height=0;
},
"public function toString",function(){
return"[Rectangle("+[this.x,this.y,this.width,this.height].join(", ")+")]";
},
"public function union",function(toUnion){
var x=Math.min(this.x,toUnion.x);
var y=Math.min(this.y,toUnion.y);
return new flash.geom.Rectangle(x,y,Math.max(this.right,toUnion.right)-x,Math.max(this.bottom-toUnion.bottom)-y);
},
];},[],["Math","flash.geom.Point"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.geom.Transform
joo.classLoader.prepare("package flash.geom",
"public class Transform",1,function($$private){;return[
"public function get colorTransform",function(){
return this._colorTransform$1;
},
"public function set colorTransform",function(value){
this._colorTransform$1=value;
},
"public function get concatenatedColorTransform",function(){
var concCT=this._colorTransform$1;
var currentDO=this.displayObject$1.parent;
while(currentDO){
concCT.concat(currentDO.transform.colorTransform);
currentDO=currentDO.parent;
}
return this.colorTransform;
},
"public function get concatenatedMatrix",function(){
var concMatrix=this._matrix$1;
var currentDO=this.displayObject$1.parent;
while(currentDO){
concMatrix.concat(currentDO.transform.matrix);
currentDO=currentDO.parent;
}
return concMatrix;
},
"public function get matrix",function(){
return this._matrix$1;
},
"public function set matrix",function(value){
this._matrix$1=value;
this.displayObject$1.transform=this;
},
"public function get pixelBounds",function(){
return new flash.geom.Rectangle(this.displayObject$1.x,this.displayObject$1.y,this.displayObject$1.width,this.displayObject$1.height);
},
"public function Transform",function(displayObject){
this.displayObject$1=displayObject;
},
"private var",{displayObject:null},
"private var",{_colorTransform:null},
"private var",{_matrix:null},
];},[],["flash.geom.Rectangle"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.media.Camera
joo.classLoader.prepare("package flash.media",
{Event:{name:"activity",type:"flash.events.ActivityEvent"}},
{Event:{name:"status",type:"flash.events.StatusEvent"}},
"public final class Camera extends flash.events.EventDispatcher",2,function($$private){;return[
"public function get activityLevel",function(){
throw new Error('not implemented');
},
"public function get bandwidth",function(){
throw new Error('not implemented');
},
"public function get currentFPS",function(){
throw new Error('not implemented');
},
"public function get fps",function(){
throw new Error('not implemented');
},
"public function get height",function(){
throw new Error('not implemented');
},
"public function get index",function(){
throw new Error('not implemented');
},
"public function get keyFrameInterval",function(){
throw new Error('not implemented');
},
"public function get loopback",function(){
throw new Error('not implemented');
},
"public function get motionLevel",function(){
throw new Error('not implemented');
},
"public function get motionTimeout",function(){
throw new Error('not implemented');
},
"public function get muted",function(){
throw new Error('not implemented');
},
"public function get name",function(){
throw new Error('not implemented');
},
"public static function get names",function(){
throw new Error('not implemented');
},
"public function get quality",function(){
throw new Error('not implemented');
},
"public function get width",function(){
throw new Error('not implemented');
},
"public static function getCamera",function(name){switch(arguments.length){case 0:name=null;}
throw new Error('not implemented');
},
"public function setKeyFrameInterval",function(keyFrameInterval){
throw new Error('not implemented');
},
"public function setLoopback",function(compress){switch(arguments.length){case 0:compress=false;}
throw new Error('not implemented');
},
"public function setMode",function(width,height,fps,favorArea){switch(arguments.length){case 0:case 1:case 2:case 3:favorArea=true;}
throw new Error('not implemented');
},
"public function setMotionLevel",function(motionLevel,timeout){switch(arguments.length){case 0:case 1:timeout=2000;}
throw new Error('not implemented');
},
"public function setQuality",function(bandwidth,quality){
throw new Error('not implemented');
},
];},["names","getCamera"],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.media.ID3Info
joo.classLoader.prepare("package flash.media",
"public final dynamic class ID3Info",1,function($$private){;return[
"public var",{album:null},
"public var",{artist:null},
"public var",{comment:null},
"public var",{genre:null},
"public var",{songName:null},
"public var",{track:null},
"public var",{year:null},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.media.Microphone
joo.classLoader.prepare("package flash.media",
{Event:{name:"activity",type:"flash.events.ActivityEvent"}},
{Event:{name:"status",type:"flash.events.StatusEvent"}},
"public final class Microphone extends flash.events.EventDispatcher",2,function($$private){;return[
"public function get activityLevel",function(){
throw new Error('not implemented');
},
"public function get gain",function(){
throw new Error('not implemented');
},
"public function set gain",function(value){
throw new Error('not implemented');
},
"public function get index",function(){
throw new Error('not implemented');
},
"public function get muted",function(){
throw new Error('not implemented');
},
"public function get name",function(){
throw new Error('not implemented');
},
"public static function get names",function(){
throw new Error('not implemented');
},
"public function get rate",function(){
throw new Error('not implemented');
},
"public function set rate",function(value){
throw new Error('not implemented');
},
"public function get silenceLevel",function(){
throw new Error('not implemented');
},
"public function get silenceTimeout",function(){
throw new Error('not implemented');
},
"public function get soundTransform",function(){
throw new Error('not implemented');
},
"public function set soundTransform",function(value){
throw new Error('not implemented');
},
"public function get useEchoSuppression",function(){
throw new Error('not implemented');
},
"public static function getMicrophone",function(index){switch(arguments.length){case 0:index=-1;}
throw new Error('not implemented');
},
"public function setLoopBack",function(state){switch(arguments.length){case 0:state=true;}
throw new Error('not implemented');
},
"public function setSilenceLevel",function(silenceLevel,timeout){switch(arguments.length){case 0:case 1:timeout=-1;}
throw new Error('not implemented');
},
"public function setUseEchoSuppression",function(useEchoSuppression){
throw new Error('not implemented');
},
];},["names","getMicrophone"],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.media.Sound
joo.classLoader.prepare("package flash.media",
{Event:{name:"complete",type:"flash.events.Event"}},
{Event:{name:"id3",type:"flash.events.Event"}},
{Event:{name:"ioError",type:"flash.events.IOErrorEvent"}},
{Event:{name:"open",type:"flash.events.Event"}},
{Event:{name:"progress",type:"flash.events.ProgressEvent"}},
"public class Sound extends flash.events.EventDispatcher",2,function($$private){;return[
"public function get bytesLoaded",function(){
throw new Error('not implemented');
},
"public function get bytesTotal",function(){
throw new Error('not implemented');
},
"public function get id3",function(){
return new flash.media.ID3Info();
},
"public function get isBuffering",function(){
throw new Error('not implemented');
},
"public function get length",function(){
throw new Error('not implemented');
},
"public function get url",function(){
throw new Error('not implemented');
},
"public function Sound",function(stream,context){switch(arguments.length){case 0:stream=null;case 1:context=null;}this.super$2();
if(!this.audio){
this.audio=new js.Audio();
this.audio.addEventListener('error',function(e){
window.alert("error "+e);
},false);
this.load(stream,context);
}
},
"public function close",function(){
throw new Error('not implemented');
},
"public function load",function(stream,context){switch(arguments.length){case 0:case 1:context=null;}
if(stream&&stream.url){
var url=stream.url;
var mp3ExtensionPos=url.indexOf(".mp3");
if(mp3ExtensionPos!==-1&&this.audio.canPlayType("audio/mp3")){
var newExtension=this.audio.canPlayType("audio/ogg")?".ogg":".wav";
url=url.substring(0,mp3ExtensionPos)+newExtension+url.substring(mp3ExtensionPos+4);
}
this.audio.src=url;
this.audio.load();
}
},
"public function play",function(startTime,loops,sndTransform){switch(arguments.length){case 0:startTime=0;case 1:loops=0;case 2:sndTransform=null;}
this.audio['play']();
return null;
},
"protected var",{audio:null},
];},[],["flash.events.EventDispatcher","Error","flash.media.ID3Info","js.Audio"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.media.SoundChannel
joo.classLoader.prepare("package flash.media",
{Event:{name:"soundComplete",type:"flash.events.Event"}},
"public final class SoundChannel extends flash.events.EventDispatcher",2,function($$private){;return[
"public function get leftPeak",function(){
throw new Error('not implemented');
},
"public function get position",function(){
throw new Error('not implemented');
},
"public function get rightPeak",function(){
throw new Error('not implemented');
},
"public function get soundTransform",function(){
throw new Error('not implemented');
},
"public function set soundTransform",function(value){
throw new Error('not implemented');
},
"public function stop",function(){
throw new Error('not implemented');
},
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.media.SoundLoaderContext
joo.classLoader.prepare("package flash.media",
"public class SoundLoaderContext",1,function($$private){;return[
"public var",{bufferTime:1000},
"public var",{checkPolicyFile:false},
"public function SoundLoaderContext",function(bufferTime,checkPolicyFile){switch(arguments.length){case 0:bufferTime=1000;case 1:checkPolicyFile=false;}
throw new Error('not implemented');
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.media.SoundMixer
joo.classLoader.prepare("package flash.media",
"public final class SoundMixer",1,function($$private){;return[
"public static function get bufferTime",function(){
throw new Error('not implemented');
},
"public static function set bufferTime",function(value){
throw new Error('not implemented');
},
"public static function get soundTransform",function(){
throw new Error('not implemented');
},
"public static function set soundTransform",function(value){
throw new Error('not implemented');
},
"public static function areSoundsInaccessible",function(){
throw new Error('not implemented');
},
"public static function computeSpectrum",function(outputArray,FFTMode,stretchFactor){switch(arguments.length){case 0:case 1:FFTMode=false;case 2:stretchFactor=0;}
throw new Error('not implemented');
},
"public static function stopAll",function(){
throw new Error('not implemented');
},
];},["bufferTime","soundTransform","areSoundsInaccessible","computeSpectrum","stopAll"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.media.SoundTransform
joo.classLoader.prepare("package flash.media",
"public final class SoundTransform",1,function($$private){;return[
"public function get leftToLeft",function(){
throw new Error('not implemented');
},
"public function set leftToLeft",function(value){
throw new Error('not implemented');
},
"public function get leftToRight",function(){
throw new Error('not implemented');
},
"public function set leftToRight",function(value){
throw new Error('not implemented');
},
"public function get pan",function(){
return this._pan$1;
},
"public function set pan",function(value){
this._pan$1=value;
},
"public function get rightToLeft",function(){
throw new Error('not implemented');
},
"public function set rightToLeft",function(value){
throw new Error('not implemented');
},
"public function get rightToRight",function(){
throw new Error('not implemented');
},
"public function set rightToRight",function(value){
throw new Error('not implemented');
},
"public function get volume",function(){
return this._volume$1;
},
"public function set volume",function(value){
this._volume$1=value;
},
"public function SoundTransform",function(vol,panning){switch(arguments.length){case 0:vol=1;case 1:panning=0;}
this.volume=vol;
this.pan=panning;
},
"private var",{_volume:NaN},
"private var",{_pan:NaN},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.media.Video
joo.classLoader.prepare("package flash.media",
"public class Video extends flash.display.DisplayObject",3,function($$private){;return[
"public function get deblocking",function(){
throw new Error('not implemented');
},
"public function set deblocking",function(value){
throw new Error('not implemented');
},
"public function get smoothing",function(){
throw new Error('not implemented');
},
"public function set smoothing",function(value){
throw new Error('not implemented');
},
"public function get videoHeight",function(){
throw new Error('not implemented');
},
"public function get videoWidth",function(){
throw new Error('not implemented');
},
"public function Video",function(width,height){switch(arguments.length){case 0:width=320;case 1:height=240;}this.super$3();
throw new Error('not implemented');
},
"public function attachCamera",function(camera){
throw new Error('not implemented');
},
"public function attachNetStream",function(netStream){
throw new Error('not implemented');
},
"public function clear",function(){
throw new Error('not implemented');
},
];},[],["flash.display.DisplayObject","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.FileFilter
joo.classLoader.prepare("package flash.net",
"public final class FileFilter",1,function($$private){;return[
"public function get description",function(){
throw new Error('not implemented');
},
"public function set description",function(value){
throw new Error('not implemented');
},
"public function get extension",function(){
throw new Error('not implemented');
},
"public function set extension",function(value){
throw new Error('not implemented');
},
"public function get macType",function(){
throw new Error('not implemented');
},
"public function set macType",function(value){
throw new Error('not implemented');
},
"public function FileFilter",function(description,extension,macType){switch(arguments.length){case 0:case 1:case 2:macType=null;}
throw new Error('not implemented');
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.FileReference
joo.classLoader.prepare("package flash.net",
{Event:{name:"cancel",type:"flash.events.Event"}},
{Event:{name:"complete",type:"flash.events.Event"}},
{Event:{name:"httpStatus",type:"flash.events.HTTPStatusEvent"}},
{Event:{name:"ioError",type:"flash.events.IOErrorEvent"}},
{Event:{name:"open",type:"flash.events.Event"}},
{Event:{name:"progress",type:"flash.events.ProgressEvent"}},
{Event:{name:"securityError",type:"flash.events.SecurityErrorEvent"}},
{Event:{name:"select",type:"flash.events.Event"}},
{Event:{name:"uploadCompleteData",type:"flash.events.DataEvent"}},
"public class FileReference extends flash.events.EventDispatcher",2,function($$private){;return[
"public function get creationDate",function(){
throw new Error('not implemented');
},
"public function get creator",function(){
throw new Error('not implemented');
},
"public function get modificationDate",function(){
throw new Error('not implemented');
},
"public function get name",function(){
throw new Error('not implemented');
},
"public function get size",function(){
throw new Error('not implemented');
},
"public function get type",function(){
throw new Error('not implemented');
},
"public function FileReference",function(){this.super$2();
throw new Error('not implemented');
},
"public function browse",function(typeFilter){switch(arguments.length){case 0:typeFilter=null;}
throw new Error('not implemented');
},
"public function cancel",function(){
throw new Error('not implemented');
},
"public function download",function(request,defaultFileName){switch(arguments.length){case 0:case 1:defaultFileName=null;}
throw new Error('not implemented');
},
"public function upload",function(request,uploadDataFieldName,testUpload){switch(arguments.length){case 0:case 1:uploadDataFieldName="Filedata";case 2:testUpload=false;}
throw new Error('not implemented');
},
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.FileReferenceList
joo.classLoader.prepare("package flash.net",
{Event:{name:"cancel",type:"flash.events.Event"}},
{Event:{name:"select",type:"flash.events.Event"}},
"public class FileReferenceList extends flash.events.EventDispatcher",2,function($$private){;return[
"public function get fileList",function(){
throw new Error('not implemented');
},
"public function FileReferenceList",function(){this.super$2();
throw new Error('not implemented');
},
"public function browse",function(typeFilter){switch(arguments.length){case 0:typeFilter=null;}
throw new Error('not implemented');
},
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.IDynamicPropertyOutput
joo.classLoader.prepare("package flash.net",
"public interface IDynamicPropertyOutput",1,function($$private){;return[,
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.IDynamicPropertyWriter
joo.classLoader.prepare("package flash.net",
"public interface IDynamicPropertyWriter",1,function($$private){;return[,
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.LocalConnection
joo.classLoader.prepare("package flash.net",
{Event:{name:"asyncError",type:"flash.events.AsyncErrorEvent"}},
{Event:{name:"securityError",type:"flash.events.SecurityErrorEvent"}},
{Event:{name:"status",type:"flash.events.StatusEvent"}},
"public class LocalConnection extends flash.events.EventDispatcher",2,function($$private){;return[
"public function get client",function(){
throw new Error('not implemented');
},
"public function set client",function(value){
throw new Error('not implemented');
},
"public function get domain",function(){
throw new Error('not implemented');
},
"public function LocalConnection",function(){this.super$2();
throw new Error('not implemented');
},
"public function allowDomain",function(){var domains=arguments;
throw new Error('not implemented');
},
"public function allowInsecureDomain",function(){var domains=arguments;
throw new Error('not implemented');
},
"public function close",function(){
throw new Error('not implemented');
},
"public function connect",function(connectionName){
throw new Error('not implemented');
},
"public function send",function(connectionName,methodName){var rest=Array.prototype.slice.call(arguments,2);
throw new Error('not implemented');
},
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.NetConnection
joo.classLoader.prepare("package flash.net",
{Event:{name:"asyncError",type:"flash.events.AsyncErrorEvent"}},
{Event:{name:"ioError",type:"flash.events.IOErrorEvent"}},
{Event:{name:"netStatus",type:"flash.events.NetStatusEvent"}},
{Event:{name:"securityError",type:"flash.events.SecurityErrorEvent"}},
"public class NetConnection extends flash.events.EventDispatcher",2,function($$private){;return[
"public function get client",function(){
throw new Error('not implemented');
},
"public function set client",function(value){
throw new Error('not implemented');
},
"public function get connected",function(){
throw new Error('not implemented');
},
"public function get connectedProxyType",function(){
throw new Error('not implemented');
},
"public static function get defaultObjectEncoding",function(){
throw new Error('not implemented');
},
"public static function set defaultObjectEncoding",function(value){
throw new Error('not implemented');
},
"public function get objectEncoding",function(){
throw new Error('not implemented');
},
"public function set objectEncoding",function(value){
throw new Error('not implemented');
},
"public function get proxyType",function(){
throw new Error('not implemented');
},
"public function set proxyType",function(value){
throw new Error('not implemented');
},
"public function get uri",function(){
throw new Error('not implemented');
},
"public function get usingTLS",function(){
throw new Error('not implemented');
},
"public function NetConnection",function(){this.super$2();
throw new Error('not implemented');
},
"public function addHeader",function(operation,mustUnderstand,param){switch(arguments.length){case 0:case 1:mustUnderstand=false;case 2:param=null;}
throw new Error('not implemented');
},
"public function call",function(command,responder){var rest=Array.prototype.slice.call(arguments,2);
throw new Error('not implemented');
},
"public function close",function(){
throw new Error('not implemented');
},
"public function connect",function(command){var rest=Array.prototype.slice.call(arguments,1);
throw new Error('not implemented');
},
];},["defaultObjectEncoding"],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.NetStream
joo.classLoader.prepare("package flash.net",
{Event:{name:"asyncError",type:"flash.events.AsyncErrorEvent"}},
{Event:{name:"ioError",type:"flash.events.IOErrorEvent"}},
{Event:{name:"netStatus",type:"flash.events.NetStatusEvent"}},
"public class NetStream extends flash.events.EventDispatcher",2,function($$private){;return[
"public function get bufferLength",function(){
throw new Error('not implemented');
},
"public function get bufferTime",function(){
throw new Error('not implemented');
},
"public function set bufferTime",function(value){
throw new Error('not implemented');
},
"public function get bytesLoaded",function(){
throw new Error('not implemented');
},
"public function get bytesTotal",function(){
throw new Error('not implemented');
},
"public function get checkPolicyFile",function(){
throw new Error('not implemented');
},
"public function set checkPolicyFile",function(value){
throw new Error('not implemented');
},
"public function get client",function(){
throw new Error('not implemented');
},
"public function set client",function(value){
throw new Error('not implemented');
},
"public function get currentFPS",function(){
throw new Error('not implemented');
},
"public function get liveDelay",function(){
throw new Error('not implemented');
},
"public function get objectEncoding",function(){
throw new Error('not implemented');
},
"public function get soundTransform",function(){
throw new Error('not implemented');
},
"public function set soundTransform",function(value){
throw new Error('not implemented');
},
"public function get time",function(){
throw new Error('not implemented');
},
"public function NetStream",function(connection,peerID){switch(arguments.length){case 0:case 1:peerID="connectToFMS";}this.super$2();
throw new Error('not implemented');
},
"public function attachAudio",function(microphone){
throw new Error('not implemented');
},
"public function attachCamera",function(theCamera,snapshotMilliseconds){switch(arguments.length){case 0:case 1:snapshotMilliseconds=-1;}
throw new Error('not implemented');
},
"public function close",function(){
throw new Error('not implemented');
},
"public function pause",function(){
throw new Error('not implemented');
},
"public function play",function(){var rest=arguments;
throw new Error('not implemented');
},
"public function publish",function(name,type){switch(arguments.length){case 0:name=null;case 1:type=null;}
throw new Error('not implemented');
},
"public function receiveAudio",function(flag){
throw new Error('not implemented');
},
"public function receiveVideo",function(flag){
throw new Error('not implemented');
},
"public function receiveVideoFPS",function(FPS){
throw new Error('not implemented');
},
"public function resume",function(){
throw new Error('not implemented');
},
"public function seek",function(offset){
throw new Error('not implemented');
},
"public function send",function(handlerName){var rest=Array.prototype.slice.call(arguments,1);
throw new Error('not implemented');
},
"public function togglePause",function(){
throw new Error('not implemented');
},
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.ObjectEncoding
joo.classLoader.prepare("package flash.net",
"public final class ObjectEncoding",1,function($$private){;return[
"public static function get dynamicPropertyWriter",function(){
throw new Error('not implemented');
},
"public static function set dynamicPropertyWriter",function(value){
throw new Error('not implemented');
},
"public static const",{AMF0:0},
"public static const",{AMF3:3},
"public static const",{DEFAULT:3},
];},["dynamicPropertyWriter"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.Responder
joo.classLoader.prepare("package flash.net",
"public class Responder",1,function($$private){;return[
"public function Responder",function(result,status){switch(arguments.length){case 0:case 1:status=null;}
throw new Error('not implemented');
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.SharedObject
joo.classLoader.prepare("package flash.net",
{Event:{name:"asyncError",type:"flash.events.AsyncErrorEvent"}},
{Event:{name:"netStatus",type:"flash.events.NetStatusEvent"}},
{Event:{name:"sync",type:"flash.events.SyncEvent"}},
"public class SharedObject extends flash.events.EventDispatcher",2,function($$private){;return[
"public function get client",function(){
throw new Error('not implemented');
},
"public function set client",function(value){
throw new Error('not implemented');
},
"public function get data",function(){
return this._data$2;
},
"public static function get defaultObjectEncoding",function(){
throw new Error('not implemented');
},
"public static function set defaultObjectEncoding",function(value){
throw new Error('not implemented');
},
"public function set fps",function(value){
throw new Error('not implemented');
},
"public function get objectEncoding",function(){
throw new Error('not implemented');
},
"public function set objectEncoding",function(value){
throw new Error('not implemented');
},
"public function get size",function(){
throw new Error('not implemented');
},
"public function clear",function(){
throw new Error('not implemented');
},
"public function close",function(){
throw new Error('not implemented');
},
"public function connect",function(myConnection,params){switch(arguments.length){case 0:case 1:params=null;}
throw new Error('not implemented');
},
"public function flush",function(minDiskSpace){switch(arguments.length){case 0:minDiskSpace=0;}
throw new Error('not implemented');
},
"public static function getLocal",function(name,localPath,secure){switch(arguments.length){case 0:case 1:localPath=null;case 2:secure=false;}
return new flash.net.SharedObject({});
},
"public static function getRemote",function(name,remotePath,persistence,secure){switch(arguments.length){case 0:case 1:remotePath=null;case 2:persistence=false;case 3:secure=false;}
throw new Error('not implemented');
},
"public function send",function(){var rest=arguments;
throw new Error('not implemented');
},
"public function setDirty",function(propertyName){
throw new Error('not implemented');
},
"public function setProperty",function(propertyName,value){switch(arguments.length){case 0:case 1:value=null;}
throw new Error('not implemented');
},
"public function SharedObject",function(data){this.super$2();
this._data$2=data;
},
"private var",{_data:null},
];},["defaultObjectEncoding","getLocal","getRemote"],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.SharedObjectFlushStatus
joo.classLoader.prepare("package flash.net",
"public final class SharedObjectFlushStatus",1,function($$private){;return[
"public static const",{FLUSHED:"flushed"},
"public static const",{PENDING:"pending"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.Socket
joo.classLoader.prepare("package flash.net",
{Event:{name:"close",type:"flash.events.Event"}},
{Event:{name:"connect",type:"flash.events.Event"}},
{Event:{name:"ioError",type:"flash.events.IOErrorEvent"}},
{Event:{name:"securityError",type:"flash.events.SecurityErrorEvent"}},
{Event:{name:"socketData",type:"flash.events.ProgressEvent"}},
"public class Socket extends flash.events.EventDispatcher implements flash.utils.IDataInput,flash.utils.IDataOutput",2,function($$private){;return[
"public function get bytesAvailable",function(){
throw new Error('not implemented');
},
"public function get connected",function(){
throw new Error('not implemented');
},
"public function get endian",function(){
throw new Error('not implemented');
},
"public function set endian",function(value){
throw new Error('not implemented');
},
"public function get objectEncoding",function(){
throw new Error('not implemented');
},
"public function set objectEncoding",function(value){
throw new Error('not implemented');
},
"public function Socket",function(host,port){switch(arguments.length){case 0:host=null;case 1:port=0;}this.super$2();
throw new Error('not implemented');
},
"public function close",function(){
throw new Error('not implemented');
},
"public function connect",function(host,port){
throw new Error('not implemented');
},
"public function flush",function(){
throw new Error('not implemented');
},
"public function readBoolean",function(){
throw new Error('not implemented');
},
"public function readByte",function(){
throw new Error('not implemented');
},
"public function readBytes",function(bytes,offset,length){switch(arguments.length){case 0:case 1:offset=0;case 2:length=0;}
throw new Error('not implemented');
},
"public function readDouble",function(){
throw new Error('not implemented');
},
"public function readFloat",function(){
throw new Error('not implemented');
},
"public function readInt",function(){
throw new Error('not implemented');
},
"public function readMultiByte",function(length,charSet){
throw new Error('not implemented');
},
"public function readObject",function(){
throw new Error('not implemented');
},
"public function readShort",function(){
throw new Error('not implemented');
},
"public function readUnsignedByte",function(){
throw new Error('not implemented');
},
"public function readUnsignedInt",function(){
throw new Error('not implemented');
},
"public function readUnsignedShort",function(){
throw new Error('not implemented');
},
"public function readUTF",function(){
throw new Error('not implemented');
},
"public function readUTFBytes",function(length){
throw new Error('not implemented');
},
"public function writeBoolean",function(value){
throw new Error('not implemented');
},
"public function writeByte",function(value){
throw new Error('not implemented');
},
"public function writeBytes",function(bytes,offset,length){switch(arguments.length){case 0:case 1:offset=0;case 2:length=0;}
throw new Error('not implemented');
},
"public function writeDouble",function(value){
throw new Error('not implemented');
},
"public function writeFloat",function(value){
throw new Error('not implemented');
},
"public function writeInt",function(value){
throw new Error('not implemented');
},
"public function writeMultiByte",function(value,charSet){
throw new Error('not implemented');
},
"public function writeObject",function(object){
throw new Error('not implemented');
},
"public function writeShort",function(value){
throw new Error('not implemented');
},
"public function writeUnsignedInt",function(value){
throw new Error('not implemented');
},
"public function writeUTF",function(value){
throw new Error('not implemented');
},
"public function writeUTFBytes",function(value){
throw new Error('not implemented');
},
];},[],["flash.events.EventDispatcher","flash.utils.IDataInput","flash.utils.IDataOutput","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.URLLoader
joo.classLoader.prepare("package flash.net",
{Event:{name:"complete",type:"flash.events.Event"}},
{Event:{name:"httpStatus",type:"flash.events.HTTPStatusEvent"}},
{Event:{name:"ioError",type:"flash.events.IOErrorEvent"}},
{Event:{name:"open",type:"flash.events.Event"}},
{Event:{name:"progress",type:"flash.events.ProgressEvent"}},
{Event:{name:"securityError",type:"flash.events.SecurityErrorEvent"}},
"public class URLLoader extends flash.events.EventDispatcher",2,function($$private){var is=joo.is,$$bound=joo.boundMethod,trace=joo.trace;return[function(){joo.classLoader.init(flash.events.Event,js.XMLHttpRequest);},
"public var",{bytesLoaded:0},
"public var",{bytesTotal:0},
"public var",{data:undefined},
"public var",{dataFormat:"text"},
"public function URLLoader",function(request){switch(arguments.length){case 0:request=null;}this.super$2();
if(request){
this.load(request);
}
},
"override public function addEventListener",function(type,listener,useCapture,priority,useWeakReference){switch(arguments.length){case 0:case 1:case 2:useCapture=false;case 3:priority=0;case 4:useWeakReference=false;}
throw new Error('not implemented');
},
"public function close",function(){
this.xmlHttpRequest$2.abort();
},
"public function load",function(request){
try{
this.xmlHttpRequest$2=new js.XMLHttpRequest();
}catch(e){if(is(e,Error)){
throw new Error("Your browser does not support XMLHttpRequest: "+e.message);
}else throw e;}
this.xmlHttpRequest$2.onreadystatechange=$$bound(this,"readyStateChanged$2");
this.xmlHttpRequest$2.open(request.method,request.url,true);
this.xmlHttpRequest$2.send(null);
},
"private function readyStateChanged",function(){
trace("URLLoader: "+this.xmlHttpRequest$2.readyState);
if(this.xmlHttpRequest$2.readyState==js.XMLHttpRequest.DONE){
this.data=this.xmlHttpRequest$2.responseText;
}
var event=this.createEvent$2();
if(event){
this.dispatchEvent(event);
}
},
"private function createEvent",function(){
switch(this.xmlHttpRequest$2.readyState){
case js.XMLHttpRequest.OPENED:return new flash.events.Event(flash.events.Event.OPEN,false,false);
case js.XMLHttpRequest.DONE:return new flash.events.Event(flash.events.Event.COMPLETE,false,false);
}
return null;
},
"private var",{xmlHttpRequest:null},
];},[],["flash.events.EventDispatcher","Error","js.XMLHttpRequest","flash.events.Event"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.URLLoaderDataFormat
joo.classLoader.prepare("package flash.net",
"public final class URLLoaderDataFormat",1,function($$private){;return[
"public static const",{BINARY:"binary"},
"public static const",{TEXT:"text"},
"public static const",{VARIABLES:"variables"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.URLRequest
joo.classLoader.prepare("package flash.net",
"public final class URLRequest",1,function($$private){;return[function(){joo.classLoader.init(flash.net.URLRequestMethod);},
"public native function get contentType",
"public native function set contentType",
"public native function get data",
"public native function set data",
"public native function get digest",
"public native function set digest",
"public native function get method",
"public native function set method",
"public native function get requestHeaders",
"public native function set requestHeaders",
"public native function get url",
"public native function set url",
"public function URLRequest",function(url){switch(arguments.length){case 0:url=null;}
this.method=flash.net.URLRequestMethod.GET;
this.url=url;
},
];},[],["flash.net.URLRequestMethod"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.URLRequestHeader
joo.classLoader.prepare("package flash.net",
"public final class URLRequestHeader",1,function($$private){;return[
"public var",{name:null},
"public var",{value:null},
"public function URLRequestHeader",function(name,value){switch(arguments.length){case 0:name="";case 1:value="";}
this.name=name;
this.value=value;
},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.URLRequestMethod
joo.classLoader.prepare("package flash.net",
"public final class URLRequestMethod",1,function($$private){;return[
"public static const",{GET:"GET"},
"public static const",{POST:"POST"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.URLStream
joo.classLoader.prepare("package flash.net",
{Event:{name:"complete",type:"flash.events.Event"}},
{Event:{name:"httpStatus",type:"flash.events.HTTPStatusEvent"}},
{Event:{name:"ioError",type:"flash.events.IOErrorEvent"}},
{Event:{name:"open",type:"flash.events.Event"}},
{Event:{name:"progress",type:"flash.events.ProgressEvent"}},
{Event:{name:"securityError",type:"flash.events.SecurityErrorEvent"}},
"public class URLStream extends flash.events.EventDispatcher implements flash.utils.IDataInput",2,function($$private){;return[
"public function get bytesAvailable",function(){
throw new Error('not implemented');
},
"public function get connected",function(){
throw new Error('not implemented');
},
"public function get endian",function(){
throw new Error('not implemented');
},
"public function set endian",function(value){
throw new Error('not implemented');
},
"public function get objectEncoding",function(){
throw new Error('not implemented');
},
"public function set objectEncoding",function(value){
throw new Error('not implemented');
},
"public function close",function(){
throw new Error('not implemented');
},
"public function load",function(request){
throw new Error('not implemented');
},
"public function readBoolean",function(){
throw new Error('not implemented');
},
"public function readByte",function(){
throw new Error('not implemented');
},
"public function readBytes",function(bytes,offset,length){switch(arguments.length){case 0:case 1:offset=0;case 2:length=0;}
throw new Error('not implemented');
},
"public function readDouble",function(){
throw new Error('not implemented');
},
"public function readFloat",function(){
throw new Error('not implemented');
},
"public function readInt",function(){
throw new Error('not implemented');
},
"public function readMultiByte",function(length,charSet){
throw new Error('not implemented');
},
"public function readObject",function(){
throw new Error('not implemented');
},
"public function readShort",function(){
throw new Error('not implemented');
},
"public function readUnsignedByte",function(){
throw new Error('not implemented');
},
"public function readUnsignedInt",function(){
throw new Error('not implemented');
},
"public function readUnsignedShort",function(){
throw new Error('not implemented');
},
"public function readUTF",function(){
throw new Error('not implemented');
},
"public function readUTFBytes",function(length){
throw new Error('not implemented');
},
];},[],["flash.events.EventDispatcher","flash.utils.IDataInput","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.URLVariables
joo.classLoader.prepare("package flash.net",
"public dynamic class URLVariables",1,function($$private){;return[
"public function URLVariables",function(source){switch(arguments.length){case 0:source=null;}
throw new Error('not implemented');
},
"public function decode",function(source){
throw new Error('not implemented');
},
"public function toString",function(){
throw new Error('not implemented');
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.net.XMLSocket
joo.classLoader.prepare("package flash.net",
{Event:{name:"close",type:"flash.events.Event"}},
{Event:{name:"connect",type:"flash.events.Event"}},
{Event:{name:"data",type:"flash.events.DataEvent"}},
{Event:{name:"ioError",type:"flash.events.IOErrorEvent"}},
{Event:{name:"securityError",type:"flash.events.SecurityErrorEvent"}},
"public class XMLSocket extends flash.events.EventDispatcher",2,function($$private){;return[
"public function get connected",function(){
throw new Error('not implemented');
},
"public function XMLSocket",function(host,port){switch(arguments.length){case 0:host=null;case 1:port=0;}this.super$2();
throw new Error('not implemented');
},
"public function close",function(){
throw new Error('not implemented');
},
"public function connect",function(host,port){
throw new Error('not implemented');
},
"public function send",function(object){
throw new Error('not implemented');
},
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.printing.PrintJob
joo.classLoader.prepare("package flash.printing",
"public class PrintJob extends flash.events.EventDispatcher",2,function($$private){;return[
"public function get orientation",function(){
throw new Error('not implemented');
},
"public function set orientation",function(value){
throw new Error('not implemented');
},
"public function get pageHeight",function(){
throw new Error('not implemented');
},
"public function get pageWidth",function(){
throw new Error('not implemented');
},
"public function get paperHeight",function(){
throw new Error('not implemented');
},
"public function get paperWidth",function(){
throw new Error('not implemented');
},
"public function PrintJob",function(){this.super$2();
throw new Error('not implemented');
},
"public function addPage",function(sprite,printArea,options,frameNum){switch(arguments.length){case 0:case 1:printArea=null;case 2:options=null;case 3:frameNum=0;}
throw new Error('not implemented');
},
"public function send",function(){
throw new Error('not implemented');
},
"public function start",function(){
throw new Error('not implemented');
},
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.printing.PrintJobOptions
joo.classLoader.prepare("package flash.printing",
"public class PrintJobOptions",1,function($$private){;return[
"public var",{printAsBitmap:false},
"public function PrintJobOptions",function(printAsBitmap){switch(arguments.length){case 0:printAsBitmap=false;}
throw new Error('not implemented');
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.printing.PrintJobOrientation
joo.classLoader.prepare("package flash.printing",
"public final class PrintJobOrientation",1,function($$private){;return[
"public static const",{LANDSCAPE:"landscape"},
"public static const",{PORTRAIT:"portrait"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.system.ApplicationDomain
joo.classLoader.prepare("package flash.system",
"public final class ApplicationDomain",1,function($$private){;return[
"public static function get currentDomain",function(){
throw new Error('not implemented');
},
"public function get parentDomain",function(){
throw new Error('not implemented');
},
"public function ApplicationDomain",function(parentDomain){switch(arguments.length){case 0:parentDomain=null;}
throw new Error('not implemented');
},
"public function getDefinition",function(name){
throw new Error('not implemented');
},
"public function hasDefinition",function(name){
throw new Error('not implemented');
},
];},["currentDomain"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.system.Capabilities
joo.classLoader.prepare("package flash.system",
"public final class Capabilities",1,function($$private){;return[
"public static function get avHardwareDisable",function(){
return false;
},
"public static function get hasAccessibility",function(){
return false;
},
"public static function get hasAudio",function(){
return $$private.audio!==null;
},
"public static function get hasAudioEncoder",function(){
return false;
},
"public static function get hasEmbeddedVideo",function(){
return! !joo.getQualifiedObject('Video');
},
"public static function get hasIME",function(){
return false;
},
"public static function get hasMP3",function(){
return flash.system.Capabilities.hasAudio&&$$private.audio['canPlayType']("audio/mp3");
},
"public static function get hasPrinting",function(){
return false;
},
"public static function get hasScreenBroadcast",function(){
return false;
},
"public static function get hasScreenPlayback",function(){
return false;
},
"public static function get hasStreamingAudio",function(){
return false;
},
"public static function get hasStreamingVideo",function(){
return flash.system.Capabilities.hasEmbeddedVideo;
},
"public static function get hasTLS",function(){
return false;
},
"public static function get hasVideoEncoder",function(){
return false;
},
"public static function get isDebugger",function(){
return joo.debug;
},
"public static function get isEmbeddedInAcrobat",function(){
return false;
},
"public static function get language",function(){
throw new Error('not implemented');
},
"public static function get localFileReadDisable",function(){
return true;
},
"public static function get manufacturer",function(){
return"Jangaroo";
},
"public static function get os",function(){
throw new Error('not implemented');
},
"public static function get pixelAspectRatio",function(){
throw new Error('not implemented');
},
"public static function get playerType",function(){
return"PlugIn";
},
"public static function get screenColor",function(){
throw new Error('not implemented');
},
"public static function get screenDPI",function(){
throw new Error('not implemented');
},
"public static function get screenResolutionX",function(){
throw new Error('not implemented');
},
"public static function get screenResolutionY",function(){
throw new Error('not implemented');
},
"public static function get serverString",function(){
throw new Error('not implemented');
},
"public static function get version",function(){
throw new Error('not implemented');
},
"private static const",{audio:function(){return((function(){
var Audio=joo.getQualifiedObject('Audio');
return Audio?new Audio:null;
})());}},
];},["avHardwareDisable","hasAccessibility","hasAudio","hasAudioEncoder","hasEmbeddedVideo","hasIME","hasMP3","hasPrinting","hasScreenBroadcast","hasScreenPlayback","hasStreamingAudio","hasStreamingVideo","hasTLS","hasVideoEncoder","isDebugger","isEmbeddedInAcrobat","language","localFileReadDisable","manufacturer","os","pixelAspectRatio","playerType","screenColor","screenDPI","screenResolutionX","screenResolutionY","serverString","version"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.system.IME
joo.classLoader.prepare("package flash.system",
{Event:{name:"imeComposition",type:"flash.events.IMEEvent"}},
"public final class IME extends flash.events.EventDispatcher",2,function($$private){;return[
"public static function get conversionMode",function(){
throw new Error('not implemented');
},
"public static function set conversionMode",function(value){
throw new Error('not implemented');
},
"public static function get enabled",function(){
throw new Error('not implemented');
},
"public static function set enabled",function(value){
throw new Error('not implemented');
},
"public static function doConversion",function(){
throw new Error('not implemented');
},
"public static function setCompositionString",function(composition){
throw new Error('not implemented');
},
];},["conversionMode","enabled","doConversion","setCompositionString"],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.system.IMEConversionMode
joo.classLoader.prepare("package flash.system",
"public final class IMEConversionMode",1,function($$private){;return[
"public static const",{ALPHANUMERIC_FULL:"ALPHANUMERIC_FULL"},
"public static const",{ALPHANUMERIC_HALF:"ALPHANUMERIC_HALF"},
"public static const",{CHINESE:"CHINESE"},
"public static const",{JAPANESE_HIRAGANA:"JAPANESE_HIRAGANA"},
"public static const",{JAPANESE_KATAKANA_FULL:"JAPANESE_KATAKANA_FULL"},
"public static const",{JAPANESE_KATAKANA_HALF:"JAPANESE_KATAKANA_HALF"},
"public static const",{KOREAN:"KOREAN"},
"public static const",{UNKNOWN:"UNKNOWN"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.system.LoaderContext
joo.classLoader.prepare("package flash.system",
"public class LoaderContext",1,function($$private){;return[
"public var",{applicationDomain:null},
"public var",{checkPolicyFile:false},
"public var",{securityDomain:null},
"public function LoaderContext",function(checkPolicyFile,applicationDomain,securityDomain){switch(arguments.length){case 0:checkPolicyFile=false;case 1:applicationDomain=null;case 2:securityDomain=null;}
throw new Error('not implemented');
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.system.Security
joo.classLoader.prepare("package flash.system",
"public final class Security",1,function($$private){;return[
"public static function get exactSettings",function(){
throw new Error('not implemented');
},
"public static function set exactSettings",function(value){
throw new Error('not implemented');
},
"public static function get sandboxType",function(){
throw new Error('not implemented');
},
"public static function allowDomain",function(){var domains=arguments;
throw new Error('not implemented');
},
"public static function allowInsecureDomain",function(){var domains=arguments;
throw new Error('not implemented');
},
"public static function loadPolicyFile",function(url){
throw new Error('not implemented');
},
"public static function showSettings",function(panel){switch(arguments.length){case 0:panel="default";}
throw new Error('not implemented');
},
"public static const",{LOCAL_TRUSTED:"localTrusted"},
"public static const",{LOCAL_WITH_FILE:"localWithFile"},
"public static const",{LOCAL_WITH_NETWORK:"localWithNetwork"},
"public static const",{REMOTE:"remote"},
];},["exactSettings","sandboxType","allowDomain","allowInsecureDomain","loadPolicyFile","showSettings"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.system.SecurityDomain
joo.classLoader.prepare("package flash.system",
"public class SecurityDomain",1,function($$private){;return[
"public static function get currentDomain",function(){
throw new Error('not implemented');
},
];},["currentDomain"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.system.SecurityPanel
joo.classLoader.prepare("package flash.system",
"public final class SecurityPanel",1,function($$private){;return[
"public static const",{CAMERA:"camera"},
"public static const",{DEFAULT:"default"},
"public static const",{DISPLAY:"display"},
"public static const",{LOCAL_STORAGE:"localStorage"},
"public static const",{MICROPHONE:"microphone"},
"public static const",{PRIVACY:"privacy"},
"public static const",{SETTINGS_MANAGER:"settingsManager"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.system.System
joo.classLoader.prepare("package flash.system",
"public final class System",1,function($$private){;return[
"public static function get ime",function(){
throw new Error('not implemented');
},
"public static function get totalMemory",function(){
return 1000000;
},
"public static function get useCodePage",function(){
throw new Error('not implemented');
},
"public static function set useCodePage",function(value){
throw new Error('not implemented');
},
"public static function exit",function(code){
throw new Error('not implemented');
},
"public static function gc",function(){
throw new Error('not implemented');
},
"public static function pause",function(){
throw new Error('not implemented');
},
"public static function resume",function(){
throw new Error('not implemented');
},
"public static function setClipboard",function(string){
},
];},["ime","totalMemory","useCodePage","exit","gc","pause","resume","setClipboard"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.AntiAliasType
joo.classLoader.prepare("package flash.text",
"public final class AntiAliasType",1,function($$private){;return[
"public static const",{ADVANCED:"advanced"},
"public static const",{NORMAL:"normal"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.CSMSettings
joo.classLoader.prepare("package flash.text",
"public final class CSMSettings",1,function($$private){;return[
"public var",{fontSize:NaN},
"public var",{insideCutoff:NaN},
"public var",{outsideCutoff:NaN},
"public function CSMSettings",function(fontSize,insideCutoff,outsideCutoff){
throw new Error('not implemented');
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.Font
joo.classLoader.prepare("package flash.text",
"public class Font",1,function($$private){;return[
"public function get fontName",function(){
throw new Error('not implemented');
},
"public function get fontStyle",function(){
throw new Error('not implemented');
},
"public function get fontType",function(){
throw new Error('not implemented');
},
"public static function enumerateFonts",function(enumerateDeviceFonts){switch(arguments.length){case 0:enumerateDeviceFonts=false;}
throw new Error('not implemented');
},
"public function hasGlyphs",function(str){
throw new Error('not implemented');
},
"public static function registerFont",function(font){
throw new Error('not implemented');
},
];},["enumerateFonts","registerFont"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.FontStyle
joo.classLoader.prepare("package flash.text",
"public final class FontStyle",1,function($$private){;return[
"public static const",{BOLD:"bold"},
"public static const",{BOLD_ITALIC:"boldItalic"},
"public static const",{ITALIC:"italic"},
"public static const",{REGULAR:"regular"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.FontType
joo.classLoader.prepare("package flash.text",
"public final class FontType",1,function($$private){;return[
"public static const",{DEVICE:"device"},
"public static const",{EMBEDDED:"embedded"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.GridFitType
joo.classLoader.prepare("package flash.text",
"public final class GridFitType",1,function($$private){;return[
"public static const",{NONE:"none"},
"public static const",{PIXEL:"pixel"},
"public static const",{SUBPIXEL:"subpixel"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.StaticText
joo.classLoader.prepare("package flash.text",
"public final class StaticText extends flash.display.DisplayObject",3,function($$private){;return[
"public function get text",function(){
throw new Error('not implemented');
},
];},[],["flash.display.DisplayObject","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.StyleSheet
joo.classLoader.prepare("package flash.text",
"public dynamic class StyleSheet extends flash.events.EventDispatcher",2,function($$private){;return[
"public function get styleNames",function(){
throw new Error('not implemented');
},
"public function StyleSheet",function(){this.super$2();
throw new Error('not implemented');
},
"public function clear",function(){
throw new Error('not implemented');
},
"public function getStyle",function(styleName){
throw new Error('not implemented');
},
"public function parseCSS",function(CSSText){
throw new Error('not implemented');
},
"public function setStyle",function(styleName,styleObject){
throw new Error('not implemented');
},
"public function transform",function(formatObject){
throw new Error('not implemented');
},
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.TextColorType
joo.classLoader.prepare("package flash.text",
"public final class TextColorType",1,function($$private){;return[
"public static const",{DARK_COLOR:"dark"},
"public static const",{LIGHT_COLOR:"light"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.TextDisplayMode
joo.classLoader.prepare("package flash.text",
"public final class TextDisplayMode",1,function($$private){;return[
"public static const",{CRT:"crt"},
"public static const",{DEFAULT:"default"},
"public static const",{LCD:"lcd"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.TextExtent
joo.classLoader.prepare("package flash.text",
"public class TextExtent",1,function($$private){;return[
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.TextField
joo.classLoader.prepare("package flash.text",
{Event:{name:"change",type:"flash.events.Event"}},
{Event:{name:"link",type:"flash.events.TextEvent"}},
{Event:{name:"scroll",type:"flash.events.Event"}},
{Event:{name:"textInput",type:"flash.events.TextEvent"}},
"public class TextField extends flash.display.InteractiveObject",4,function($$private){;return[function(){joo.classLoader.init(flash.text.TextFormatAlign);},
"public function get alwaysShowSelection",function(){
return this._alwaysShowSelection$4;
},
"public function set alwaysShowSelection",function(value){
this._alwaysShowSelection$4=value;
},
"public function get antiAliasType",function(){
return this._antiAliasType$4;
},
"public function set antiAliasType",function(value){
this._antiAliasType$4=value;
},
"public function get autoSize",function(){
return this._autoSize$4;
},
"public function set autoSize",function(value){
this._autoSize$4=value;
},
"public function get background",function(){
return this._background$4;
},
"public function set background",function(value){
this._background$4=value;
},
"public function get backgroundColor",function(){
return this._backgroundColor$4;
},
"public function set backgroundColor",function(value){
this._backgroundColor$4=value;
$$private.updateElementProperty(this.getElement(),"style.backgroundColor",flash.display.Graphics.toRGBA(value));
},
"public function get border",function(){
return this._border$4;
},
"public function set border",function(value){
this._border$4=value;
$$private.updateElementProperty(this.getElement(),"style.borderWidth",value?"1px":"0");
},
"public function get borderColor",function(){
return this._borderColor$4;
},
"public function set borderColor",function(value){
this._borderColor$4=value;
$$private.updateElementProperty(this.getElement(),"style.borderColor",flash.display.Graphics.toRGBA(value));
},
"public function get bottomScrollV",function(){
return this._bottomScrollV$4;
},
"public function get caretIndex",function(){
return this._caretIndex$4;
},
"public function get condenseWhite",function(){
return this._condenseWhite$4;
},
"public function set condenseWhite",function(value){
this._condenseWhite$4=value;
},
"public function get defaultTextFormat",function(){
return this._defaultTextFormat$4;
},
"public function set defaultTextFormat",function(value){
for(var property in value){
if(value.hasOwnProperty(property)){
var val=value[property];
if(typeof val!=="function"&&val!==null&&val!==""){
this._defaultTextFormat$4[property]=value[property];
}
}
}
if(this.hasElement()){
this.syncTextFormat$4(this.getElement());
}
},
"public function get displayAsPassword",function(){
return this._displayAsPassword$4;
},
"public function set displayAsPassword",function(value){
this._displayAsPassword$4=value;
},
"public function get embedFonts",function(){
return this._embedFonts$4;
},
"public function set embedFonts",function(value){
this._embedFonts$4=value;
},
"public function get gridFitType",function(){
return this._gridFitType$4;
},
"public function set gridFitType",function(value){
this._gridFitType$4=value;
},
"public function get htmlText",function(){
return this._htmlText$4;
},
"public function set htmlText",function(value){
this._htmlText$4=value;
$$private.updateElementProperty(this.getElement(),"innerHTML",value);
},
"public function get length",function(){
return this._length$4;
},
"public function get maxChars",function(){
return this._maxChars$4;
},
"public function set maxChars",function(value){
this._maxChars$4=value;
},
"public function get maxScrollH",function(){
return this._maxScrollH$4;
},
"public function get maxScrollV",function(){
return this._maxScrollV$4;
},
"public function get mouseWheelEnabled",function(){
return this._mouseWheelEnabled$4;
},
"public function set mouseWheelEnabled",function(value){
this._mouseWheelEnabled$4=value;
},
"public function get multiline",function(){
return this._multiline$4;
},
"public function set multiline",function(value){
this._multiline$4=value;
},
"public function get numLines",function(){
return this._lines$4.length;
},
"public function get restrict",function(){
return this._restrict$4;
},
"public function set restrict",function(value){
this._restrict$4=value;
},
"public function get scrollH",function(){
return this._scrollH$4;
},
"public function set scrollH",function(value){
this._scrollH$4=value;
},
"public function get scrollV",function(){
return this._scrollV$4;
},
"public function set scrollV",function(value){
this._scrollV$4=value;
},
"public function get selectable",function(){
return this._selectable$4;
},
"public function set selectable",function(value){
this._selectable$4=value;
},
"public function get selectionBeginIndex",function(){
return this._selectionBeginIndex$4;
},
"public function get selectionEndIndex",function(){
return this._selectionEndIndex$4;
},
"public function get sharpness",function(){
return this._sharpness$4;
},
"public function set sharpness",function(value){
this._sharpness$4=value;
},
"public function get styleSheet",function(){
return this._styleSheet$4;
},
"public function set styleSheet",function(value){
this._styleSheet$4=value;
},
"public function get text",function(){
return this._lines$4.join('\n');
},
"public function set text",function(value){
this._lines$4=value.split('\n');
$$private.updateElementProperty(this.getElement(),"innerHTML",this._lines$4.join('<br />'));
},
"public function get textColor",function(){
return $$uint(this._textFormat$4.color!==null?this._textFormat$4.color:this._defaultTextFormat$4.color);
},
"public function set textColor",function(value){
this._defaultTextFormat$4.color=this._textFormat$4.color=value;
if(this.hasElement()){
$$private.updateElementProperty(this.getElement(),"style.color",flash.display.Graphics.toRGBA(value));
}
},
"public function get textHeight",function(){
return this._textHeight$4;
},
"public function get textWidth",function(){
return this._textWidth$4;
},
"public function get thickness",function(){
return this._thickness$4;
},
"public function set thickness",function(value){
this._thickness$4=value;
},
"public function get type",function(){
return this._type$4;
},
"public function set type",function(value){
this._type$4=value;
},
"public function get useRichTextClipboard",function(){
return this._useRichTextClipboard$4;
},
"public function set useRichTextClipboard",function(value){
this._useRichTextClipboard$4=value;
},
"public function get wordWrap",function(){
return this._wordWrap$4;
},
"public function set wordWrap",function(value){
this._wordWrap$4=value;
},
"public function TextField",function(){
this.super$4();this._defaultTextFormat$4=this._defaultTextFormat$4();this._textFormat$4=this._textFormat$4();
this._lines$4=[""];
},
"public function appendText",function(newText){
this.text=this.text+newText;
},
"public function getCharBoundaries",function(charIndex){
throw new Error('not implemented');
},
"public function getCharIndexAtPoint",function(x,y){
throw new Error('not implemented');
},
"public function getFirstCharInParagraph",function(charIndex){
throw new Error('not implemented');
},
"public function getImageReference",function(id){
throw new Error('not implemented');
},
"public function getLineIndexAtPoint",function(x,y){
throw new Error('not implemented');
},
"public function getLineIndexOfChar",function(charIndex){
throw new Error('not implemented');
},
"public function getLineLength",function(lineIndex){
return this._lines$4[lineIndex].length;
},
"public function getLineMetrics",function(lineIndex){
if(!$$private.lineMetricsContext){
$$private.lineMetricsContext=((window.document.createElement("CANVAS")).getContext("2d"));
}
$$private.lineMetricsContext.font=this.asWebFont$4();
var width=$$private.lineMetricsContext.measureText(this._lines$4[lineIndex]).width;
return new flash.text.TextLineMetrics(0,width,this.getSize$4(),0,0,0);
},
"private function getSize",function(){
return $$int(this._textFormat$4.size!==null?this._textFormat$4.size:this._defaultTextFormat$4.size);
},
"public function getLineOffset",function(lineIndex){
throw new Error('not implemented');
},
"public function getLineText",function(lineIndex){
throw new Error('not implemented');
},
"public function getParagraphLength",function(charIndex){
throw new Error('not implemented');
},
"public function getTextFormat",function(beginIndex,endIndex){switch(arguments.length){case 0:beginIndex=-1;case 1:endIndex=-1;}
throw new Error('not implemented');
},
"public function replaceSelectedText",function(value){
throw new Error('not implemented');
},
"public function replaceText",function(beginIndex,endIndex,newText){
throw new Error('not implemented');
},
"public function setSelection",function(beginIndex,endIndex){
this._selectionBeginIndex$4=beginIndex;
this._selectionEndIndex$4=endIndex;
},
"public function setTextFormat",function(format,beginIndex,endIndex){switch(arguments.length){case 0:case 1:beginIndex=-1;case 2:endIndex=-1;}
this._textFormat$4=format;
if(this.hasElement()){
this.syncTextFormat$4(this.getElement());
}
},
"private function asWebFont",function(){
var webFont=this.getSize$4()+"px ";
switch(this._textFormat$4.font!==null?this._textFormat$4.font:this._defaultTextFormat$4.font){
case"Times New Roman":
webFont+="Times New Roman,serif";break;
case"Arial":
case"Helvetica":
webFont+="Helvetica,Arial,sans-serif";break;
case"system":
webFont+="console,monospace";break;
}
return webFont;
},
"override protected function createElement",function(){
var elem=this.createElement$4();
elem.style.padding="2px";
this.syncTextFormat$4(elem);
return elem;
},
"private function syncTextFormat",function(element){
$$private.updateElementProperty(element,"style.font",this.asWebFont$4());
$$private.updateElementProperty(element,"style.color",flash.display.Graphics.toRGBA(this.textColor));
var bold=this._textFormat$4.bold!==null?this._textFormat$4.bold:this._defaultTextFormat$4.bold;
$$private.updateElementProperty(element,"style.fontWeight",bold?"bold":"normal");
var italic=this._textFormat$4.italic!==null?this._textFormat$4.italic:this._defaultTextFormat$4.italic;
$$private.updateElementProperty(element,"style.fontStyle",italic?"italic":"normal");
var align=this._textFormat$4.align!==null?this._textFormat$4.align:this._defaultTextFormat$4.align;
$$private.updateElementProperty(element,"style.textAlign",align);
},
"override protected function getElementName",function(){
return"span";
},
"private static function updateElementProperty",function(element,propertyPath,value){
var current=element;
var propertyPathArcs=propertyPath.split(".");
var lastIndex=propertyPathArcs.length-1;
for(var i=0;i<lastIndex;++i){
current=current[propertyPathArcs[i]];
}
current[propertyPathArcs[lastIndex]]=value;
},
"private var",{_alwaysShowSelection:false},
"private var",{_antiAliasType:null},
"private var",{_autoSize:null},
"private var",{_background:false},
"private var",{_backgroundColor:0},
"private var",{_border:false},
"private var",{_borderColor:0},
"private var",{_bottomScrollV:0},
"private var",{_caretIndex:0},
"private var",{_condenseWhite:false},
"private var",{_displayAsPassword:false},
"private var",{_embedFonts:false},
"private var",{_gridFitType:null},
"private var",{_htmlText:null},
"private var",{_length:0},
"private var",{_maxChars:0},
"private var",{_maxScrollH:0},
"private var",{_maxScrollV:0},
"private var",{_mouseWheelEnabled:false},
"private var",{_multiline:false},
"private var",{_restrict:null},
"private var",{_scrollH:0},
"private var",{_scrollV:0},
"private var",{_selectable:false},
"private var",{_selectionBeginIndex:0},
"private var",{_selectionEndIndex:0},
"private var",{_sharpness:NaN},
"private var",{_defaultTextFormat:function(){return(new flash.text.TextFormat("Times New Roman",12,0,false,false,false,"","",flash.text.TextFormatAlign.LEFT,0,0,0,0));}},
"private var",{_textFormat:function(){return(new flash.text.TextFormat());}},
"private var",{_lines:null},
"private var",{_styleSheet:null},
"private var",{_textHeight:NaN},
"private var",{_textWidth:NaN},
"private var",{_thickness:NaN},
"private var",{_type:null},
"private var",{_useRichTextClipboard:false},
"private var",{_wordWrap:false},
"private static var",{lineMetricsContext:null},
];},[],["flash.display.InteractiveObject","flash.display.Graphics","uint","Error","js.CanvasRenderingContext2D","js.HTMLCanvasElement","flash.text.TextLineMetrics","int","flash.text.TextFormat","flash.text.TextFormatAlign"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.TextFieldAutoSize
joo.classLoader.prepare("package flash.text",
"public final class TextFieldAutoSize",1,function($$private){;return[
"public static const",{CENTER:"center"},
"public static const",{LEFT:"left"},
"public static const",{NONE:"none"},
"public static const",{RIGHT:"right"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.TextFieldType
joo.classLoader.prepare("package flash.text",
"public final class TextFieldType",1,function($$private){;return[
"public static const",{DYNAMIC:"dynamic"},
"public static const",{INPUT:"input"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.TextFormat
joo.classLoader.prepare("package flash.text",
"public class TextFormat",1,function($$private){;return[
"public function get align",function(){
return this._align$1;
},
"public function set align",function(value){
this._align$1=value;
},
"public function get blockIndent",function(){
return this._blockIndent$1;
},
"public function set blockIndent",function(value){
this._blockIndent$1=value;
},
"public function get bold",function(){
return this._bold$1;
},
"public function set bold",function(value){
this._bold$1=value;
},
"public function get bullet",function(){
return this._bullet$1;
},
"public function set bullet",function(value){
this._bullet$1=value;
},
"public function get color",function(){
return this._color$1;
},
"public function set color",function(value){
this._color$1=value;
},
"public function get font",function(){
return this._font$1;
},
"public function set font",function(value){
this._font$1=value;
},
"public function get indent",function(){
return this._indent$1;
},
"public function set indent",function(value){
this._indent$1=value;
},
"public function get italic",function(){
return this._italic$1;
},
"public function set italic",function(value){
this._italic$1=value;
},
"public function get kerning",function(){
return this._kerning$1;
},
"public function set kerning",function(value){
this._kerning$1=value;
},
"public function get leading",function(){
return this._leading$1;
},
"public function set leading",function(value){
this._leading$1=value;
},
"public function get leftMargin",function(){
return this._leftMargin$1;
},
"public function set leftMargin",function(value){
this._leftMargin$1=value;
},
"public function get letterSpacing",function(){
return this._letterSpacing$1;
},
"public function set letterSpacing",function(value){
this._letterSpacing$1=value;
},
"public function get rightMargin",function(){
return this._rightMargin$1;
},
"public function set rightMargin",function(value){
this._rightMargin$1=value;
},
"public function get size",function(){
return this._size$1;
},
"public function set size",function(value){
this._size$1=value;
},
"public function get tabStops",function(){
return this._tabStops$1;
},
"public function set tabStops",function(value){
this._tabStops$1=value;
},
"public function get target",function(){
return this._target$1;
},
"public function set target",function(value){
this._target$1=value;
},
"public function get underline",function(){
return this._underline$1;
},
"public function set underline",function(value){
this._underline$1=value;
},
"public function get url",function(){
return this._url$1;
},
"public function set url",function(value){
this._url$1=value;
},
"public function TextFormat",function(font,size,color,bold,
italic,underline,url,target,
align,leftMargin,rightMargin,
indent,leading){switch(arguments.length){case 0:font=null;case 1:size=null;case 2:color=null;case 3:bold=null;case 4:italic=null;case 5:underline=null;case 6:url=null;case 7:target=null;case 8:align=null;case 9:leftMargin=null;case 10:rightMargin=null;case 11:indent=null;case 12:leading=null;}
this._font$1=font;
this._size$1=size;
this._color$1=color;
this._bold$1=bold;
this._italic$1=italic;
this._underline$1=underline;
this._url$1=url;
this._target$1=target;
this._align$1=align;
this._leftMargin$1=leftMargin;
this._rightMargin$1=rightMargin;
this._indent$1=indent;
this._leading$1=leading;
},
"private var",{_align:null},
"private var",{_blockIndent:null},
"private var",{_bold:null},
"private var",{_bullet:null},
"private var",{_color:null},
"private var",{_font:null},
"private var",{_indent:null},
"private var",{_italic:null},
"private var",{_kerning:null},
"private var",{_leading:null},
"private var",{_leftMargin:null},
"private var",{_letterSpacing:null},
"private var",{_rightMargin:null},
"private var",{_size:null},
"private var",{_tabStops:null},
"private var",{_target:null},
"private var",{_underline:null},
"private var",{_url:null},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.TextFormatAlign
joo.classLoader.prepare("package flash.text",
"public final class TextFormatAlign",1,function($$private){;return[
"public static const",{CENTER:"center"},
"public static const",{JUSTIFY:"justify"},
"public static const",{LEFT:"left"},
"public static const",{RIGHT:"right"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.TextLineMetrics
joo.classLoader.prepare("package flash.text",
"public class TextLineMetrics",1,function($$private){;return[
"public var",{ascent:NaN},
"public var",{descent:NaN},
"public var",{height:NaN},
"public var",{leading:NaN},
"public var",{width:NaN},
"public var",{x:NaN},
"public function TextLineMetrics",function(x,width,height,ascent,descent,leading){
this.x=x;
this.width=width;
this.height=height;
this.ascent=ascent;
this.descent=descent;
this.leading=leading;
},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.TextRenderer
joo.classLoader.prepare("package flash.text",
"public final class TextRenderer",1,function($$private){;return[
"public static function get displayMode",function(){
return $$private._displayMode;
},
"public static function set displayMode",function(value){
$$private._displayMode=value;
},
"public static function get maxLevel",function(){
return $$private._maxLevel;
},
"public static function set maxLevel",function(value){
$$private._maxLevel=value;
},
"public static function setAdvancedAntiAliasingTable",function(fontName,fontStyle,colorType,advancedAntiAliasingTable){
throw new Error('not implemented');
},
"private static var",{_displayMode:null},
"private static var",{_maxLevel:0},
];},["displayMode","maxLevel","setAdvancedAntiAliasingTable"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.text.TextSnapshot
joo.classLoader.prepare("package flash.text",
"public class TextSnapshot",1,function($$private){;return[
"public function get charCount",function(){
throw new Error('not implemented');
},
"public function findText",function(beginIndex,textToFind,caseSensitive){
throw new Error('not implemented');
},
"public function getSelected",function(beginIndex,endIndex){
throw new Error('not implemented');
},
"public function getSelectedText",function(includeLineEndings){switch(arguments.length){case 0:includeLineEndings=false;}
throw new Error('not implemented');
},
"public function getText",function(beginIndex,endIndex,includeLineEndings){switch(arguments.length){case 0:case 1:case 2:includeLineEndings=false;}
throw new Error('not implemented');
},
"public function getTextRunInfo",function(beginIndex,endIndex){
throw new Error('not implemented');
},
"public function hitTestTextNearPos",function(x,y,maxDistance){switch(arguments.length){case 0:case 1:case 2:maxDistance=0;}
throw new Error('not implemented');
},
"public function setSelectColor",function(hexColor){switch(arguments.length){case 0:hexColor=0xFFFF00;}
throw new Error('not implemented');
},
"public function setSelected",function(beginIndex,endIndex,select){
throw new Error('not implemented');
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.ui.ContextMenu
joo.classLoader.prepare("package flash.ui",
{Event:{name:"menuSelect",type:"flash.events.ContextMenuEvent"}},
"public final class ContextMenu extends flash.events.EventDispatcher",2,function($$private){;return[
"public function get builtInItems",function(){
throw new Error('not implemented');
},
"public function set builtInItems",function(value){
throw new Error('not implemented');
},
"public function get customItems",function(){
throw new Error('not implemented');
},
"public function set customItems",function(value){
throw new Error('not implemented');
},
"public function ContextMenu",function(){this.super$2();
throw new Error('not implemented');
},
"public function hideBuiltInItems",function(){
throw new Error('not implemented');
},
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.ui.ContextMenuBuiltInItems
joo.classLoader.prepare("package flash.ui",
"public final class ContextMenuBuiltInItems",1,function($$private){;return[
"public function get forwardAndBack",function(){
throw new Error('not implemented');
},
"public function set forwardAndBack",function(value){
throw new Error('not implemented');
},
"public function get loop",function(){
throw new Error('not implemented');
},
"public function set loop",function(value){
throw new Error('not implemented');
},
"public function get play",function(){
throw new Error('not implemented');
},
"public function set play",function(value){
throw new Error('not implemented');
},
"public function get print",function(){
throw new Error('not implemented');
},
"public function set print",function(value){
throw new Error('not implemented');
},
"public function get quality",function(){
throw new Error('not implemented');
},
"public function set quality",function(value){
throw new Error('not implemented');
},
"public function get rewind",function(){
throw new Error('not implemented');
},
"public function set rewind",function(value){
throw new Error('not implemented');
},
"public function get save",function(){
throw new Error('not implemented');
},
"public function set save",function(value){
throw new Error('not implemented');
},
"public function get zoom",function(){
throw new Error('not implemented');
},
"public function set zoom",function(value){
throw new Error('not implemented');
},
"public function ContextMenuBuiltInItems",function(){
throw new Error('not implemented');
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.ui.ContextMenuItem
joo.classLoader.prepare("package flash.ui",
{Event:{name:"menuItemSelect",type:"flash.events.ContextMenuEvent"}},
"public final class ContextMenuItem extends flash.events.EventDispatcher",2,function($$private){;return[
"public function get caption",function(){
throw new Error('not implemented');
},
"public function set caption",function(value){
throw new Error('not implemented');
},
"public function get separatorBefore",function(){
throw new Error('not implemented');
},
"public function set separatorBefore",function(value){
throw new Error('not implemented');
},
"public function get visible",function(){
throw new Error('not implemented');
},
"public function set visible",function(value){
throw new Error('not implemented');
},
"public function ContextMenuItem",function(caption,separatorBefore,enabled,visible){switch(arguments.length){case 0:case 1:separatorBefore=false;case 2:enabled=true;case 3:visible=true;}this.super$2();
throw new Error('not implemented');
},
];},[],["flash.events.EventDispatcher","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.ui.Keyboard
joo.classLoader.prepare("package flash.ui",
"public final class Keyboard",1,function($$private){;return[
"public static function get capsLock",function(){
throw new Error('not implemented');
},
"public static function get numLock",function(){
throw new Error('not implemented');
},
"public static function isAccessible",function(){
throw new Error('not implemented');
},
"public static const",{BACKSPACE:8},
"public static const",{CAPS_LOCK:20},
"public static const",{CONTROL:17},
"public static const",{DELETE:46},
"public static const",{DOWN:40},
"public static const",{END:35},
"public static const",{ENTER:13},
"public static const",{ESCAPE:27},
"public static const",{F1:112},
"public static const",{F10:121},
"public static const",{F11:122},
"public static const",{F12:123},
"public static const",{F13:124},
"public static const",{F14:125},
"public static const",{F15:126},
"public static const",{F2:113},
"public static const",{F3:114},
"public static const",{F4:115},
"public static const",{F5:116},
"public static const",{F6:117},
"public static const",{F7:118},
"public static const",{F8:119},
"public static const",{F9:120},
"public static const",{G:71},
"public static const",{HOME:36},
"public static const",{INSERT:45},
"public static const",{LEFT:37},
"public static const",{NUMPAD_0:96},
"public static const",{NUMPAD_1:97},
"public static const",{NUMPAD_2:98},
"public static const",{NUMPAD_3:99},
"public static const",{NUMPAD_4:100},
"public static const",{NUMPAD_5:101},
"public static const",{NUMPAD_6:102},
"public static const",{NUMPAD_7:103},
"public static const",{NUMPAD_8:104},
"public static const",{NUMPAD_9:105},
"public static const",{NUMPAD_ADD:107},
"public static const",{NUMPAD_DECIMAL:110},
"public static const",{NUMPAD_DIVIDE:111},
"public static const",{NUMPAD_ENTER:108},
"public static const",{NUMPAD_MULTIPLY:106},
"public static const",{NUMPAD_SUBTRACT:109},
"public static const",{PAGE_DOWN:34},
"public static const",{PAGE_UP:33},
"public static const",{RIGHT:39},
"public static const",{SHIFT:16},
"public static const",{SPACE:32},
"public static const",{TAB:9},
"public static const",{UP:38},
];},["capsLock","numLock","isAccessible"],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.ui.KeyLocation
joo.classLoader.prepare("package flash.ui",
"public final class KeyLocation",1,function($$private){;return[
"public static const",{LEFT:1},
"public static const",{NUM_PAD:3},
"public static const",{RIGHT:2},
"public static const",{STANDARD:0},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.ui.Mouse
joo.classLoader.prepare("package flash.ui",
"public final class Mouse",1,function($$private){;return[
"public static function hide",function(){
},
"public static function show",function(){
},
];},["hide","show"],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.utils.ByteArray
joo.classLoader.prepare("package flash.utils",
"public class ByteArray implements flash.utils.IDataInput,flash.utils.IDataOutput",1,function($$private){;return[
"public function get bytesAvailable",function(){
throw new Error('not implemented');
},
"public static function get defaultObjectEncoding",function(){
throw new Error('not implemented');
},
"public static function set defaultObjectEncoding",function(value){
throw new Error('not implemented');
},
"public function get endian",function(){
throw new Error('not implemented');
},
"public function set endian",function(value){
throw new Error('not implemented');
},
"public function get length",function(){
while(this._length$1 in this){
++this._length$1;
}
return this._length$1;
},
"public function set length",function(value){
this._length$1=value;
},
"public function get objectEncoding",function(){
throw new Error('not implemented');
},
"public function set objectEncoding",function(value){
throw new Error('not implemented');
},
"public function get position",function(){
throw new Error('not implemented');
},
"public function set position",function(value){
throw new Error('not implemented');
},
"public function ByteArray",function(){
},
"public function compress",function(algorithm){
throw new Error('not implemented');
},
"public function readBoolean",function(){
throw new Error('not implemented');
},
"public function readByte",function(){
throw new Error('not implemented');
},
"public function readBytes",function(bytes,offset,length){switch(arguments.length){case 0:case 1:offset=0;case 2:length=0;}
throw new Error('not implemented');
},
"public function readDouble",function(){
throw new Error('not implemented');
},
"public function readFloat",function(){
throw new Error('not implemented');
},
"public function readInt",function(){
throw new Error('not implemented');
},
"public function readMultiByte",function(length,charSet){
throw new Error('not implemented');
},
"public function readObject",function(){
throw new Error('not implemented');
},
"public function readShort",function(){
throw new Error('not implemented');
},
"public function readUnsignedByte",function(){
throw new Error('not implemented');
},
"public function readUnsignedInt",function(){
throw new Error('not implemented');
},
"public function readUnsignedShort",function(){
throw new Error('not implemented');
},
"public function readUTF",function(){
throw new Error('not implemented');
},
"public function readUTFBytes",function(length){
throw new Error('not implemented');
},
"public function toString",function(){
throw new Error('not implemented');
},
"public function uncompress",function(algorithm){
throw new Error('not implemented');
},
"public function writeBoolean",function(value){
throw new Error('not implemented');
},
"public function writeByte",function(value){
throw new Error('not implemented');
},
"public function writeBytes",function(bytes,offset,length){switch(arguments.length){case 0:case 1:offset=0;case 2:length=0;}
throw new Error('not implemented');
},
"public function writeDouble",function(value){
throw new Error('not implemented');
},
"public function writeFloat",function(value){
throw new Error('not implemented');
},
"public function writeInt",function(value){
throw new Error('not implemented');
},
"public function writeMultiByte",function(value,charSet){
throw new Error('not implemented');
},
"public function writeObject",function(object){
throw new Error('not implemented');
},
"public function writeShort",function(value){
throw new Error('not implemented');
},
"public function writeUnsignedInt",function(value){
throw new Error('not implemented');
},
"public function writeUTF",function(value){
throw new Error('not implemented');
},
"public function writeUTFBytes",function(value){
throw new Error('not implemented');
},
"private var",{_length:0},
];},["defaultObjectEncoding"],["flash.utils.IDataInput","flash.utils.IDataOutput","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.utils.Dictionary
joo.classLoader.prepare("package flash.utils",
"public dynamic class Dictionary",1,function($$private){var trace=joo.trace;return[
"public function Dictionary",function(weakKeys){switch(arguments.length){case 0:weakKeys=false;}
if(weakKeys){
trace("[WARN]","Dictionary with weakKeys not supported by Jangaroo.");
}
},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.utils.Endian
joo.classLoader.prepare("package flash.utils",
"public final class Endian",1,function($$private){;return[
"public static const",{BIG_ENDIAN:"bigEndian"},
"public static const",{LITTLE_ENDIAN:"littleEndian"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.utils.flash_proxy
joo.classLoader.prepare("package flash.utils",
"public namespace flash_proxy","http://www.adobe.com/flash/proxy",[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.utils.IDataInput
joo.classLoader.prepare("package flash.utils",
"public interface IDataInput",1,function($$private){;return[,,,,,,,,,,,,,,,,,,,
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.utils.IDataOutput
joo.classLoader.prepare("package flash.utils",
"public interface IDataOutput",1,function($$private){;return[,,,,,,,,,,,,,,,,
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.utils.IExternalizable
joo.classLoader.prepare("package flash.utils",
"public interface IExternalizable",1,function($$private){;return[,,
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.utils.Proxy
joo.classLoader.prepare("package flash.utils",
"public class Proxy",1,function($$private){;return[
"flash_proxy function callProperty",function(name){var rest=Array.prototype.slice.call(arguments,1);
throw new Error('not implemented');
},
"flash_proxy function deleteProperty",function(name){
throw new Error('not implemented');
},
"flash_proxy function getDescendants",function(name){
throw new Error('not implemented');
},
"flash_proxy function getProperty",function(name){
throw new Error('not implemented');
},
"flash_proxy function hasProperty",function(name){
throw new Error('not implemented');
},
"flash_proxy function isAttribute",function(name){
throw new Error('not implemented');
},
"flash_proxy function nextName",function(index){
throw new Error('not implemented');
},
"flash_proxy function nextNameIndex",function(index){
throw new Error('not implemented');
},
"flash_proxy function nextValue",function(index){
throw new Error('not implemented');
},
"flash_proxy function setProperty",function(name,value){
throw new Error('not implemented');
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.utils.Timer
joo.classLoader.prepare("package flash.utils",
{Event:{name:"timer",type:"flash.events.TimerEvent"}},
{Event:{name:"timerComplete",type:"flash.events.TimerEvent"}},
"public class Timer extends flash.events.EventDispatcher",2,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.events.TimerEvent);},
"public function get currentCount",function(){
return this._currentCount$2;
},
"public function get delay",function(){
return this._delay$2;
},
"public function set delay",function(value){
this._delay$2=value;
if(this.timer$2){
this.stop();
this.start();
}
},
"public function get repeatCount",function(){
return this._repeatCount$2;
},
"public function set repeatCount",function(value){
this._repeatCount$2=value;
this.checkComplete$2();
},
"public function get running",function(){
return this.timer$2!=null;
},
"public function Timer",function(delay,repeatCount){switch(arguments.length){case 0:case 1:repeatCount=0;}this.super$2();
this._delay$2=delay;
this._repeatCount$2=repeatCount;
},
"public function reset",function(){
this.stop();
this._currentCount$2=0;
},
"public function start",function(){
if(!this.timer$2){
this.timer$2=window.setInterval($$bound(this,"tick$2"),this._delay$2);
}
},
"public function stop",function(){
if(this.timer$2){
window.clearInterval(this.timer$2);
this.timer$2=null;
}
},
"private function tick",function(){
if(!this.timer$2){
return;
}
++this._currentCount$2;
try{
this.dispatchEvent(new flash.events.TimerEvent(flash.events.TimerEvent.TIMER));
}finally{
this.checkComplete$2();
}
},
"private function checkComplete",function(){
if(this._repeatCount$2>0&&this._currentCount$2>=this._repeatCount$2){
this.stop();
this.dispatchEvent(new flash.events.TimerEvent(flash.events.TimerEvent.TIMER_COMPLETE));
}
},
"private var",{timer:null},
"private var",{_delay:NaN},
"private var",{_repeatCount:0},
"private var",{_currentCount:0},
];},[],["flash.events.EventDispatcher","flash.events.TimerEvent"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.xml.XMLDocument
joo.classLoader.prepare("package flash.xml",
"public class XMLDocument extends flash.xml.XMLNode",2,function($$private){;return[function(){joo.classLoader.init(flash.xml.XMLNodeType);},
"public var",{docTypeDecl:null},
"public var",{idMap:null},
"public var",{ignoreWhite:false},
"public var",{xmlDecl:null},
"public function XMLDocument",function(source){switch(arguments.length){case 0:source=null;}
this.super$2(flash.xml.XMLNodeType.DOCUMENT_NODE,source);
},
"public function createElement",function(name){
return new flash.xml.XMLNode(flash.xml.XMLNodeType.ELEMENT_NODE,name);
},
"public function createTextNode",function(text){
return new flash.xml.XMLNode(flash.xml.XMLNodeType.ELEMENT_NODE,text);
},
"public function parseXML",function(source){
throw new Error('not implemented');
},
"override public function toString",function(){
throw new Error('not implemented');
},
];},[],["flash.xml.XMLNode","flash.xml.XMLNodeType","Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.xml.XMLNode
joo.classLoader.prepare("package flash.xml",
"public class XMLNode",1,function($$private){;return[
"public function get attributes",function(){
throw new Error('not implemented');
},
"public function set attributes",function(value){
throw new Error('not implemented');
},
"public function get childNodes",function(){
throw new Error('not implemented');
},
"public var",{firstChild:null},
"public var",{lastChild:null},
"public function get localName",function(){
throw new Error('not implemented');
},
"public function get namespaceURI",function(){
throw new Error('not implemented');
},
"public var",{nextSibling:null},
"public var",{nodeName:null},
"public var",{nodeType:0},
"public var",{nodeValue:null},
"public var",{parentNode:null},
"public function get prefix",function(){
throw new Error('not implemented');
},
"public var",{previousSibling:null},
"public function XMLNode",function(type,value){
throw new Error('not implemented');
},
"public function appendChild",function(node){
throw new Error('not implemented');
},
"public function cloneNode",function(deep){
throw new Error('not implemented');
},
"public function getNamespaceForPrefix",function(prefix){
throw new Error('not implemented');
},
"public function getPrefixForNamespace",function(ns){
throw new Error('not implemented');
},
"public function hasChildNodes",function(){
throw new Error('not implemented');
},
"public function insertBefore",function(node,before){
throw new Error('not implemented');
},
"public function removeNode",function(){
throw new Error('not implemented');
},
"public function toString",function(){
throw new Error('not implemented');
},
];},[],["Error"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class flash.xml.XMLNodeType
joo.classLoader.prepare("package flash.xml",
"public final class XMLNodeType",1,function($$private){;return[
"public static const",{ELEMENT_NODE:1},
"public static const",{TEXT_NODE:3},
"internal static const",{DOCUMENT_NODE:0},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);
// class joo.flash.Meta
joo.classLoader.prepare("package joo.flash",
"public class Meta",1,function($$private){var is=joo.is,trace=joo.trace;return[function(){joo.classLoader.init(flash.media.Sound,flash.display.Bitmap);},function()
{
joo.getOrCreatePackage("joo.meta").Embed=joo.flash.Meta.embed;
},
"public static function embed",function(classDeclaration,memberDeclaration,parameters){
var relativeUrl=parameters['source'];
var resource=joo.DynamicClassLoader.INSTANCE.getResource(relativeUrl);
if(is(resource,String)){
memberDeclaration.value=function(){
return new String(resource);
};
return;
}
var EmbedClass;
var superClassDeclaration;
if(resource){
if(is(resource,js.HTMLImageElement)){
superClassDeclaration=flash.display.Bitmap['$class'];
EmbedClass=function(){
var bitmapData=flash.display.BitmapData.fromImg((resource));
superClassDeclaration.constructor_.call(this,bitmapData);
};
}else if(is(resource,js.HTMLAudioElement)){
superClassDeclaration=flash.media.Sound['$class'];
EmbedClass=function(){
this['audio']=(resource);
superClassDeclaration.constructor_.call(this);
};
}
}
if(EmbedClass){
EmbedClass.prototype=new superClassDeclaration.Public();
EmbedClass.toString=function(){
return relativeUrl;
};
memberDeclaration.value=EmbedClass;
}else{
trace("[WARN]","Ignoring unsupported media type of file "+relativeUrl);
}
},
];},["embed"],["joo.DynamicClassLoader","String","js.HTMLImageElement","flash.display.Bitmap","flash.display.BitmapData","js.HTMLAudioElement","flash.media.Sound"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class joo.flash.Run
joo.classLoader.prepare("package joo.flash",
"public class Run",1,function($$private){var trace=joo.trace;return[function(){joo.classLoader.init(joo.DynamicClassLoader,flash.events.Event);},
"public static function main",function(id,primaryDisplayObjectClassName){
var classLoader=joo.DynamicClassLoader.INSTANCE;
classLoader.import_(primaryDisplayObjectClassName);
classLoader.complete(function(){
if(classLoader.debug){
trace("[INFO] Loaded Flash main class "+primaryDisplayObjectClassName+".");
}
var primaryDisplayObjectClass=flash.utils.getDefinitionByName(primaryDisplayObjectClassName);
var cd=primaryDisplayObjectClass['$class'];
var metadata=cd.metadata;
var swf=metadata['SWF'];
var stage=new flash.display.Stage(id,swf);
var displayObject=(new cd.Public());
stage.internalAddChildAt(displayObject,0);
cd.constructor_.call(displayObject);
displayObject.broadcastEvent(new flash.events.Event(flash.events.Event.ADDED_TO_STAGE,false,false));
});
},
];},["main"],["joo.DynamicClassLoader","flash.display.Stage","flash.display.DisplayObject","flash.events.Event"], "0.8.0", "0.8.2-SNAPSHOT"
);
// class joo.flash.util.Base64
joo.classLoader.prepare(
"package joo.flash.util",
"public class Base64",1,function($$private){;return[
"private static const",{keyStr:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="},
"public static function encodeBytes",function(input){
var output=[];
for(var i=0;i<input.length;){
var chr1=input[i++];
var chr2=input[i++];
var chr3=input[i++];
var enc1=chr1>>2;
var enc2=((chr1&3)<<4)|(chr2>>4);
var enc3=((chr2&15)<<2)|(chr3>>6);
var enc4=chr3&63;
if(isNaN(chr2)){
enc3=enc4=64;
}else if(isNaN(chr3)){
enc4=64;
}
output.push($$private.keyStr.charAt(enc1),
$$private.keyStr.charAt(enc2),
$$private.keyStr.charAt(enc3),
$$private.keyStr.charAt(enc4));
}
return output.join("");
},
"public static function encode",function(input){
var output=[];
input=$$private.utf8Encode(input);
for(var i=0;i<input.length;){
var chr1=input.charCodeAt(i++);
var chr2=input.charCodeAt(i++);
var chr3=input.charCodeAt(i++);
var enc1=chr1>>2;
var enc2=((chr1&3)<<4)|(chr2>>4);
var enc3=((chr2&15)<<2)|(chr3>>6);
var enc4=chr3&63;
if(isNaN(chr2)){
enc3=enc4=64;
}else if(isNaN(chr3)){
enc4=64;
}
output.push($$private.keyStr.charAt(enc1),
$$private.keyStr.charAt(enc2),
$$private.keyStr.charAt(enc3),
$$private.keyStr.charAt(enc4));
}
return output.join("");
},
"public static function decode",function(input){
var output=[];
input=input.replace(/[^A-Za-z0-9\+\/\=]/g,"");
for(var i=0;i<input.length;){
var enc1=$$private.keyStr.indexOf(input.charAt(i++));
var enc2=$$private.keyStr.indexOf(input.charAt(i++));
var enc3=$$private.keyStr.indexOf(input.charAt(i++));
var enc4=$$private.keyStr.indexOf(input.charAt(i++));
var chr1=(enc1<<2)|(enc2>>4);
var chr2=((enc2&15)<<4)|(enc3>>2);
var chr3=((enc3&3)<<6)|enc4;
output.push(String.fromCharCode(chr1));
if(enc3!=64){
output.push(String.fromCharCode(chr2));
}
if(enc4!=64){
output.push(String.fromCharCode(chr3));
}
}
return $$private.utf8Decode(output.join(""));
},
"private static function utf8Encode",function(string){
string=string.replace(/\r\n/g,"\n");
var utftext=[];
for(var n=0;n<string.length;n++){
var c=string.charCodeAt(n);
if(c<128){
utftext.push(String.fromCharCode(c));
}else if(c<2048){
utftext.push(String.fromCharCode((c>>6)|192),
String.fromCharCode((c&63)|128));
}else{
utftext.push(String.fromCharCode((c>>12)|224),
String.fromCharCode(((c>>6)&63)|128),
String.fromCharCode((c&63)|128));
}
}
return utftext.join("");
},
"private static function utf8Decode",function(utftext){
var string=[];
for(var i=0;i<utftext.length;){
var c=utftext.charCodeAt(i++);
if(c>=128){
var c2=utftext.charCodeAt(i++);
if(c>191&&c<224){
c=((c&31)<<6)|(c2&63);
}else{
var c3=utftext.charCodeAt(i++);
c=((c&15)<<12)|((c2&63)<<6)|(c3&63);
}
}
string.push(String.fromCharCode(c));
}
return string.join("");
},
];},["encodeBytes","encode","decode"],["String"], "0.8.0", "0.8.2-SNAPSHOT"
);
