// class caurina.transitions.AuxFunctions
joo.classLoader.prepare("package caurina.transitions",
"public class AuxFunctions",1,function($$private){;return[
"public static function numberToR",function(p_num){
return(p_num&0xff0000)>>16;
},
"public static function numberToG",function(p_num){
return(p_num&0xff00)>>8;
},
"public static function numberToB",function(p_num){
return(p_num&0xff);
},
"public static function isInArray",function(p_string,p_array){
var l=p_array.length;
for(var i=0;i<l;i++){
if(p_array[i]==p_string)return true;
}
return false;
},
"public static function getObjectLength",function(p_object){
var totalProperties=0;
for(var pName in p_object)totalProperties++;
return totalProperties;
},
"public static function concatObjects",function(){var args=Array.prototype.slice.call(arguments);
var finalObject={};
var currentObject;
for(var i=0;i<args.length;i++){
currentObject=args[i];
for(var prop in currentObject){
if(currentObject[prop]==null){
delete finalObject[prop];
}else{
finalObject[prop]=currentObject[prop];
}
}
}
return finalObject;
},
];},["numberToR","numberToG","numberToB","isInArray","getObjectLength","concatObjects"],[], "0.8.0", "0.8.4"
);
// class caurina.transitions.Equations
joo.classLoader.prepare(
"package caurina.transitions",
"public class Equations",1,function($$private){var trace=joo.trace;return[function(){joo.classLoader.init(Number,Math);},
"public function Equations",function(){
trace("Equations is a static class and should not be instantiated.");
},
"public static function init",function(){
caurina.transitions.Tweener.registerTransition("easenone",caurina.transitions.Equations.easeNone);
caurina.transitions.Tweener.registerTransition("linear",caurina.transitions.Equations.easeNone);
caurina.transitions.Tweener.registerTransition("easeinquad",caurina.transitions.Equations.easeInQuad);
caurina.transitions.Tweener.registerTransition("easeoutquad",caurina.transitions.Equations.easeOutQuad);
caurina.transitions.Tweener.registerTransition("easeinoutquad",caurina.transitions.Equations.easeInOutQuad);
caurina.transitions.Tweener.registerTransition("easeoutinquad",caurina.transitions.Equations.easeOutInQuad);
caurina.transitions.Tweener.registerTransition("easeincubic",caurina.transitions.Equations.easeInCubic);
caurina.transitions.Tweener.registerTransition("easeoutcubic",caurina.transitions.Equations.easeOutCubic);
caurina.transitions.Tweener.registerTransition("easeinoutcubic",caurina.transitions.Equations.easeInOutCubic);
caurina.transitions.Tweener.registerTransition("easeoutincubic",caurina.transitions.Equations.easeOutInCubic);
caurina.transitions.Tweener.registerTransition("easeinquart",caurina.transitions.Equations.easeInQuart);
caurina.transitions.Tweener.registerTransition("easeoutquart",caurina.transitions.Equations.easeOutQuart);
caurina.transitions.Tweener.registerTransition("easeinoutquart",caurina.transitions.Equations.easeInOutQuart);
caurina.transitions.Tweener.registerTransition("easeoutinquart",caurina.transitions.Equations.easeOutInQuart);
caurina.transitions.Tweener.registerTransition("easeinquint",caurina.transitions.Equations.easeInQuint);
caurina.transitions.Tweener.registerTransition("easeoutquint",caurina.transitions.Equations.easeOutQuint);
caurina.transitions.Tweener.registerTransition("easeinoutquint",caurina.transitions.Equations.easeInOutQuint);
caurina.transitions.Tweener.registerTransition("easeoutinquint",caurina.transitions.Equations.easeOutInQuint);
caurina.transitions.Tweener.registerTransition("easeinsine",caurina.transitions.Equations.easeInSine);
caurina.transitions.Tweener.registerTransition("easeoutsine",caurina.transitions.Equations.easeOutSine);
caurina.transitions.Tweener.registerTransition("easeinoutsine",caurina.transitions.Equations.easeInOutSine);
caurina.transitions.Tweener.registerTransition("easeoutinsine",caurina.transitions.Equations.easeOutInSine);
caurina.transitions.Tweener.registerTransition("easeincirc",caurina.transitions.Equations.easeInCirc);
caurina.transitions.Tweener.registerTransition("easeoutcirc",caurina.transitions.Equations.easeOutCirc);
caurina.transitions.Tweener.registerTransition("easeinoutcirc",caurina.transitions.Equations.easeInOutCirc);
caurina.transitions.Tweener.registerTransition("easeoutincirc",caurina.transitions.Equations.easeOutInCirc);
caurina.transitions.Tweener.registerTransition("easeinexpo",caurina.transitions.Equations.easeInExpo);
caurina.transitions.Tweener.registerTransition("easeoutexpo",caurina.transitions.Equations.easeOutExpo);
caurina.transitions.Tweener.registerTransition("easeinoutexpo",caurina.transitions.Equations.easeInOutExpo);
caurina.transitions.Tweener.registerTransition("easeoutinexpo",caurina.transitions.Equations.easeOutInExpo);
caurina.transitions.Tweener.registerTransition("easeinelastic",caurina.transitions.Equations.easeInElastic);
caurina.transitions.Tweener.registerTransition("easeoutelastic",caurina.transitions.Equations.easeOutElastic);
caurina.transitions.Tweener.registerTransition("easeinoutelastic",caurina.transitions.Equations.easeInOutElastic);
caurina.transitions.Tweener.registerTransition("easeoutinelastic",caurina.transitions.Equations.easeOutInElastic);
caurina.transitions.Tweener.registerTransition("easeinback",caurina.transitions.Equations.easeInBack);
caurina.transitions.Tweener.registerTransition("easeoutback",caurina.transitions.Equations.easeOutBack);
caurina.transitions.Tweener.registerTransition("easeinoutback",caurina.transitions.Equations.easeInOutBack);
caurina.transitions.Tweener.registerTransition("easeoutinback",caurina.transitions.Equations.easeOutInBack);
caurina.transitions.Tweener.registerTransition("easeinbounce",caurina.transitions.Equations.easeInBounce);
caurina.transitions.Tweener.registerTransition("easeoutbounce",caurina.transitions.Equations.easeOutBounce);
caurina.transitions.Tweener.registerTransition("easeinoutbounce",caurina.transitions.Equations.easeInOutBounce);
caurina.transitions.Tweener.registerTransition("easeoutinbounce",caurina.transitions.Equations.easeOutInBounce);
},
"public static function easeNone",function(t,b,c,d){
return c*t/d+b;
},
"public static function easeInQuad",function(t,b,c,d){
return c*(t/=d)*t+b;
},
"public static function easeOutQuad",function(t,b,c,d){
return-c*(t/=d)*(t-2)+b;
},
"public static function easeInOutQuad",function(t,b,c,d){
if((t/=d/2)<1)return c/2*t*t+b;
return-c/2*((--t)*(t-2)-1)+b;
},
"public static function easeOutInQuad",function(t,b,c,d){
if(t<d/2)return caurina.transitions.Equations.easeOutQuad(t*2,b,c/2,d);
return caurina.transitions.Equations.easeInQuad((t*2)-d,b+c/2,c/2,d);
},
"public static function easeInCubic",function(t,b,c,d){
return c*(t/=d)*t*t+b;
},
"public static function easeOutCubic",function(t,b,c,d){
return c*((t=t/d-1)*t*t+1)+b;
},
"public static function easeInOutCubic",function(t,b,c,d){
if((t/=d/2)<1)return c/2*t*t*t+b;
return c/2*((t-=2)*t*t+2)+b;
},
"public static function easeOutInCubic",function(t,b,c,d){
if(t<d/2)return caurina.transitions.Equations.easeOutCubic(t*2,b,c/2,d);
return caurina.transitions.Equations.easeInCubic((t*2)-d,b+c/2,c/2,d);
},
"public static function easeInQuart",function(t,b,c,d){
return c*(t/=d)*t*t*t+b;
},
"public static function easeOutQuart",function(t,b,c,d){
return-c*((t=t/d-1)*t*t*t-1)+b;
},
"public static function easeInOutQuart",function(t,b,c,d){
if((t/=d/2)<1)return c/2*t*t*t*t+b;
return-c/2*((t-=2)*t*t*t-2)+b;
},
"public static function easeOutInQuart",function(t,b,c,d){
if(t<d/2)return caurina.transitions.Equations.easeOutQuart(t*2,b,c/2,d);
return caurina.transitions.Equations.easeInQuart((t*2)-d,b+c/2,c/2,d);
},
"public static function easeInQuint",function(t,b,c,d){
return c*(t/=d)*t*t*t*t+b;
},
"public static function easeOutQuint",function(t,b,c,d){
return c*((t=t/d-1)*t*t*t*t+1)+b;
},
"public static function easeInOutQuint",function(t,b,c,d){
if((t/=d/2)<1)return c/2*t*t*t*t*t+b;
return c/2*((t-=2)*t*t*t*t+2)+b;
},
"public static function easeOutInQuint",function(t,b,c,d){
if(t<d/2)return caurina.transitions.Equations.easeOutQuint(t*2,b,c/2,d);
return caurina.transitions.Equations.easeInQuint((t*2)-d,b+c/2,c/2,d);
},
"public static function easeInSine",function(t,b,c,d){
return-c*Math.cos(t/d*(Math.PI/2))+c+b;
},
"public static function easeOutSine",function(t,b,c,d){
return c*Math.sin(t/d*(Math.PI/2))+b;
},
"public static function easeInOutSine",function(t,b,c,d){
return-c/2*(Math.cos(Math.PI*t/d)-1)+b;
},
"public static function easeOutInSine",function(t,b,c,d){
if(t<d/2)return caurina.transitions.Equations.easeOutSine(t*2,b,c/2,d);
return caurina.transitions.Equations.easeInSine((t*2)-d,b+c/2,c/2,d);
},
"public static function easeInExpo",function(t,b,c,d){
return(t==0)?b:c*Math.pow(2,10*(t/d-1))+b-c*0.001;
},
"public static function easeOutExpo",function(t,b,c,d){
return(t==d)?b+c:c*1.001*(-Math.pow(2,-10*t/d)+1)+b;
},
"public static function easeInOutExpo",function(t,b,c,d){
if(t==0)return b;
if(t==d)return b+c;
if((t/=d/2)<1)return c/2*Math.pow(2,10*(t-1))+b-c*0.0005;
return c/2*1.0005*(-Math.pow(2,-10*--t)+2)+b;
},
"public static function easeOutInExpo",function(t,b,c,d){
if(t<d/2)return caurina.transitions.Equations.easeOutExpo(t*2,b,c/2,d);
return caurina.transitions.Equations.easeInExpo((t*2)-d,b+c/2,c/2,d);
},
"public static function easeInCirc",function(t,b,c,d){
return-c*(Math.sqrt(1-(t/=d)*t)-1)+b;
},
"public static function easeOutCirc",function(t,b,c,d){
return c*Math.sqrt(1-(t=t/d-1)*t)+b;
},
"public static function easeInOutCirc",function(t,b,c,d){
if((t/=d/2)<1)return-c/2*(Math.sqrt(1-t*t)-1)+b;
return c/2*(Math.sqrt(1-(t-=2)*t)+1)+b;
},
"public static function easeOutInCirc",function(t,b,c,d){
if(t<d/2)return caurina.transitions.Equations.easeOutCirc(t*2,b,c/2,d);
return caurina.transitions.Equations.easeInCirc((t*2)-d,b+c/2,c/2,d);
},
"public static function easeInElastic",function(t,b,c,d,a,p){if(arguments.length<6){if(arguments.length<5){a=Number.NaN;}p=Number.NaN;}
if(t==0)return b;if((t/=d)==1)return b+c;if(!p)p=d*.3;
var s;
if(!a||a<Math.abs(c)){a=c;s=p/4;}
else s=p/(2*Math.PI)*Math.asin(c/a);
return-(a*Math.pow(2,10*(t-=1))*Math.sin((t*d-s)*(2*Math.PI)/p))+b;
},
"public static function easeOutElastic",function(t,b,c,d,a,p){if(arguments.length<6){if(arguments.length<5){a=Number.NaN;}p=Number.NaN;}
if(t==0)return b;if((t/=d)==1)return b+c;if(!p)p=d*.3;
var s;
if(!a||a<Math.abs(c)){a=c;s=p/4;}
else s=p/(2*Math.PI)*Math.asin(c/a);
return(a*Math.pow(2,-10*t)*Math.sin((t*d-s)*(2*Math.PI)/p)+c+b);
},
"public static function easeInOutElastic",function(t,b,c,d,a,p){if(arguments.length<6){if(arguments.length<5){a=Number.NaN;}p=Number.NaN;}
if(t==0)return b;if((t/=d/2)==2)return b+c;if(!p)p=d*(.3*1.5);
var s;
if(!a||a<Math.abs(c)){a=c;s=p/4;}
else s=p/(2*Math.PI)*Math.asin(c/a);
if(t<1)return-.5*(a*Math.pow(2,10*(t-=1))*Math.sin((t*d-s)*(2*Math.PI)/p))+b;
return a*Math.pow(2,-10*(t-=1))*Math.sin((t*d-s)*(2*Math.PI)/p)*.5+c+b;
},
"public static function easeOutInElastic",function(t,b,c,d,a,p){if(arguments.length<6){if(arguments.length<5){a=Number.NaN;}p=Number.NaN;}
if(t<d/2)return caurina.transitions.Equations.easeOutElastic(t*2,b,c/2,d,a,p);
return caurina.transitions.Equations.easeInElastic((t*2)-d,b+c/2,c/2,d,a,p);
},
"public static function easeInBack",function(t,b,c,d,s){if(arguments.length<5){s=Number.NaN;}
if(!s)s=1.70158;
return c*(t/=d)*t*((s+1)*t-s)+b;
},
"public static function easeOutBack",function(t,b,c,d,s){if(arguments.length<5){s=Number.NaN;}
if(!s)s=1.70158;
return c*((t=t/d-1)*t*((s+1)*t+s)+1)+b;
},
"public static function easeInOutBack",function(t,b,c,d,s){if(arguments.length<5){s=Number.NaN;}
if(!s)s=1.70158;
if((t/=d/2)<1)return c/2*(t*t*(((s*=(1.525))+1)*t-s))+b;
return c/2*((t-=2)*t*(((s*=(1.525))+1)*t+s)+2)+b;
},
"public static function easeOutInBack",function(t,b,c,d,s){if(arguments.length<5){s=Number.NaN;}
if(t<d/2)return caurina.transitions.Equations.easeOutBack(t*2,b,c/2,d,s);
return caurina.transitions.Equations.easeInBack((t*2)-d,b+c/2,c/2,d,s);
},
"public static function easeInBounce",function(t,b,c,d){
return c-caurina.transitions.Equations.easeOutBounce(d-t,0,c,d)+b;
},
"public static function easeOutBounce",function(t,b,c,d){
if((t/=d)<(1/2.75)){
return c*(7.5625*t*t)+b;
}else if(t<(2/2.75)){
return c*(7.5625*(t-=(1.5/2.75))*t+.75)+b;
}else if(t<(2.5/2.75)){
return c*(7.5625*(t-=(2.25/2.75))*t+.9375)+b;
}else{
return c*(7.5625*(t-=(2.625/2.75))*t+.984375)+b;
}
},
"public static function easeInOutBounce",function(t,b,c,d){
if(t<d/2)return caurina.transitions.Equations.easeInBounce(t*2,0,c,d)*.5+b;
else return caurina.transitions.Equations.easeOutBounce(t*2-d,0,c,d)*.5+c*.5+b;
},
"public static function easeOutInBounce",function(t,b,c,d){
if(t<d/2)return caurina.transitions.Equations.easeOutBounce(t*2,b,c/2,d);
return caurina.transitions.Equations.easeInBounce((t*2)-d,b+c/2,c/2,d);
},
];},["init","easeNone","easeInQuad","easeOutQuad","easeInOutQuad","easeOutInQuad","easeInCubic","easeOutCubic","easeInOutCubic","easeOutInCubic","easeInQuart","easeOutQuart","easeInOutQuart","easeOutInQuart","easeInQuint","easeOutQuint","easeInOutQuint","easeOutInQuint","easeInSine","easeOutSine","easeInOutSine","easeOutInSine","easeInExpo","easeOutExpo","easeInOutExpo","easeOutInExpo","easeInCirc","easeOutCirc","easeInOutCirc","easeOutInCirc","easeInElastic","easeOutElastic","easeInOutElastic","easeOutInElastic","easeInBack","easeOutBack","easeInOutBack","easeOutInBack","easeInBounce","easeOutBounce","easeInOutBounce","easeOutInBounce"],["caurina.transitions.Tweener","Math","Number"], "0.8.0", "0.8.4"
);
// class caurina.transitions.PropertyInfoObj
joo.classLoader.prepare("package caurina.transitions",
"public class PropertyInfoObj",1,function($$private){;return[
"public var",{valueStart:NaN},
"public var",{valueComplete:NaN},
"public var",{hasModifier:false},
"public var",{modifierFunction:null},
"public var",{modifierParameters:null},
"function PropertyInfoObj",function(p_valueStart,p_valueComplete,p_modifierFunction,p_modifierParameters){
this.valueStart=p_valueStart;
this.valueComplete=p_valueComplete;
this.hasModifier=Boolean(p_modifierFunction);
this.modifierFunction=p_modifierFunction;
this.modifierParameters=p_modifierParameters;
},
"public function clone",function(){
var nProperty=new caurina.transitions.PropertyInfoObj(this.valueStart,this.valueComplete,this.modifierFunction,this.modifierParameters);
return nProperty;
},
"public function toString",function(){
var returnStr="\n[PropertyInfoObj ";
returnStr+="valueStart:"+String(this.valueStart);
returnStr+=", ";
returnStr+="valueComplete:"+String(this.valueComplete);
returnStr+=", ";
returnStr+="modifierFunction:"+String(this.modifierFunction);
returnStr+=", ";
returnStr+="modifierParameters:"+String(this.modifierParameters);
returnStr+="]\n";
return returnStr;
},
];},[],["Boolean","String"], "0.8.0", "0.8.4"
);
// class caurina.transitions.SpecialPropertiesDefault
joo.classLoader.prepare("package caurina.transitions",
"public class SpecialPropertiesDefault",1,function($$private){var is=joo.is,trace=joo.trace;return[function(){joo.classLoader.init(flash.filters.BlurFilter);},
"public function SpecialPropertiesDefault",function(){
trace("SpecialProperties is a static class and should not be instantiated.");
},
"public static function init",function(){
caurina.transitions.Tweener.registerSpecialProperty("_frame",caurina.transitions.SpecialPropertiesDefault.frame_get,caurina.transitions.SpecialPropertiesDefault.frame_set);
caurina.transitions.Tweener.registerSpecialProperty("_sound_volume",caurina.transitions.SpecialPropertiesDefault._sound_volume_get,caurina.transitions.SpecialPropertiesDefault._sound_volume_set);
caurina.transitions.Tweener.registerSpecialProperty("_sound_pan",caurina.transitions.SpecialPropertiesDefault._sound_pan_get,caurina.transitions.SpecialPropertiesDefault._sound_pan_set);
caurina.transitions.Tweener.registerSpecialProperty("_color_ra",caurina.transitions.SpecialPropertiesDefault._color_property_get,caurina.transitions.SpecialPropertiesDefault._color_property_set,["redMultiplier"]);
caurina.transitions.Tweener.registerSpecialProperty("_color_rb",caurina.transitions.SpecialPropertiesDefault._color_property_get,caurina.transitions.SpecialPropertiesDefault._color_property_set,["redOffset"]);
caurina.transitions.Tweener.registerSpecialProperty("_color_ga",caurina.transitions.SpecialPropertiesDefault._color_property_get,caurina.transitions.SpecialPropertiesDefault._color_property_set,["greenMultiplier"]);
caurina.transitions.Tweener.registerSpecialProperty("_color_gb",caurina.transitions.SpecialPropertiesDefault._color_property_get,caurina.transitions.SpecialPropertiesDefault._color_property_set,["greenOffset"]);
caurina.transitions.Tweener.registerSpecialProperty("_color_ba",caurina.transitions.SpecialPropertiesDefault._color_property_get,caurina.transitions.SpecialPropertiesDefault._color_property_set,["blueMultiplier"]);
caurina.transitions.Tweener.registerSpecialProperty("_color_bb",caurina.transitions.SpecialPropertiesDefault._color_property_get,caurina.transitions.SpecialPropertiesDefault._color_property_set,["blueOffset"]);
caurina.transitions.Tweener.registerSpecialProperty("_color_aa",caurina.transitions.SpecialPropertiesDefault._color_property_get,caurina.transitions.SpecialPropertiesDefault._color_property_set,["alphaMultiplier"]);
caurina.transitions.Tweener.registerSpecialProperty("_color_ab",caurina.transitions.SpecialPropertiesDefault._color_property_get,caurina.transitions.SpecialPropertiesDefault._color_property_set,["alphaOffset"]);
caurina.transitions.Tweener.registerSpecialProperty("_autoAlpha",caurina.transitions.SpecialPropertiesDefault._autoAlpha_get,caurina.transitions.SpecialPropertiesDefault._autoAlpha_set);
caurina.transitions.Tweener.registerSpecialPropertySplitter("_color",caurina.transitions.SpecialPropertiesDefault._color_splitter);
caurina.transitions.Tweener.registerSpecialPropertySplitter("_colorTransform",caurina.transitions.SpecialPropertiesDefault._colorTransform_splitter);
caurina.transitions.Tweener.registerSpecialPropertySplitter("_scale",caurina.transitions.SpecialPropertiesDefault._scale_splitter);
caurina.transitions.Tweener.registerSpecialProperty("_blur_blurX",caurina.transitions.SpecialPropertiesDefault._filter_property_get,caurina.transitions.SpecialPropertiesDefault._filter_property_set,[flash.filters.BlurFilter,"blurX"]);
caurina.transitions.Tweener.registerSpecialProperty("_blur_blurY",caurina.transitions.SpecialPropertiesDefault._filter_property_get,caurina.transitions.SpecialPropertiesDefault._filter_property_set,[flash.filters.BlurFilter,"blurY"]);
caurina.transitions.Tweener.registerSpecialProperty("_blur_quality",caurina.transitions.SpecialPropertiesDefault._filter_property_get,caurina.transitions.SpecialPropertiesDefault._filter_property_set,[flash.filters.BlurFilter,"quality"]);
caurina.transitions.Tweener.registerSpecialPropertySplitter("_filter",caurina.transitions.SpecialPropertiesDefault._filter_splitter);
caurina.transitions.Tweener.registerSpecialPropertyModifier("_bezier",caurina.transitions.SpecialPropertiesDefault._bezier_modifier,caurina.transitions.SpecialPropertiesDefault._bezier_get);
},
"public static function _color_splitter",function(p_value,p_parameters){
var nArray=new Array();
if(p_value==null){
nArray.push({name:"_color_ra",value:1});
nArray.push({name:"_color_rb",value:0});
nArray.push({name:"_color_ga",value:1});
nArray.push({name:"_color_gb",value:0});
nArray.push({name:"_color_ba",value:1});
nArray.push({name:"_color_bb",value:0});
}else{
nArray.push({name:"_color_ra",value:0});
nArray.push({name:"_color_rb",value:caurina.transitions.AuxFunctions.numberToR(p_value)});
nArray.push({name:"_color_ga",value:0});
nArray.push({name:"_color_gb",value:caurina.transitions.AuxFunctions.numberToG(p_value)});
nArray.push({name:"_color_ba",value:0});
nArray.push({name:"_color_bb",value:caurina.transitions.AuxFunctions.numberToB(p_value)});
}
return nArray;
},
"public static function _colorTransform_splitter",function(p_value,p_parameters){
var nArray=new Array();
if(p_value==null){
nArray.push({name:"_color_ra",value:1});
nArray.push({name:"_color_rb",value:0});
nArray.push({name:"_color_ga",value:1});
nArray.push({name:"_color_gb",value:0});
nArray.push({name:"_color_ba",value:1});
nArray.push({name:"_color_bb",value:0});
}else{
if(p_value.ra!=undefined)nArray.push({name:"_color_ra",value:p_value.ra});
if(p_value.rb!=undefined)nArray.push({name:"_color_rb",value:p_value.rb});
if(p_value.ga!=undefined)nArray.push({name:"_color_ba",value:p_value.ba});
if(p_value.gb!=undefined)nArray.push({name:"_color_bb",value:p_value.bb});
if(p_value.ba!=undefined)nArray.push({name:"_color_ga",value:p_value.ga});
if(p_value.bb!=undefined)nArray.push({name:"_color_gb",value:p_value.gb});
if(p_value.aa!=undefined)nArray.push({name:"_color_aa",value:p_value.aa});
if(p_value.ab!=undefined)nArray.push({name:"_color_ab",value:p_value.ab});
}
return nArray;
},
"public static function _scale_splitter",function(p_value,p_parameters){
var nArray=new Array();
nArray.push({name:"scaleX",value:p_value});
nArray.push({name:"scaleY",value:p_value});
return nArray;
},
"public static function _filter_splitter",function(p_value,p_parameters){
var nArray=new Array();
if(is(p_value,flash.filters.BlurFilter)){
nArray.push({name:"_blur_blurX",value:(p_value).blurX});
nArray.push({name:"_blur_blurY",value:(p_value).blurY});
nArray.push({name:"_blur_quality",value:(p_value).quality});
}else{
trace("??");
}
return nArray;
},
"public static function frame_get",function(p_obj){
return p_obj.currentFrame;
},
"public static function frame_set",function(p_obj,p_value){
p_obj.gotoAndStop(Math.round(p_value));
},
"public static function _sound_volume_get",function(p_obj){
return p_obj.soundTransform.volume;
},
"public static function _sound_volume_set",function(p_obj,p_value){
var sndTransform=p_obj.soundTransform;
sndTransform.volume=p_value;
p_obj.soundTransform=sndTransform;
},
"public static function _sound_pan_get",function(p_obj){
return p_obj.soundTransform.pan;
},
"public static function _sound_pan_set",function(p_obj,p_value){
var sndTransform=p_obj.soundTransform;
sndTransform.pan=p_value;
p_obj.soundTransform=sndTransform;
},
"public static function _color_property_get",function(p_obj,p_parameters){
return p_obj.transform.colorTransform[p_parameters[0]];
},
"public static function _color_property_set",function(p_obj,p_value,p_parameters){
var tf=p_obj.transform.colorTransform;
tf[p_parameters[0]]=p_value;
p_obj.transform.colorTransform=tf;
},
"public static function _autoAlpha_get",function(p_obj){
return p_obj.alpha;
},
"public static function _autoAlpha_set",function(p_obj,p_value){
p_obj.alpha=p_value;
p_obj.visible=p_value>0;
},
"public static function _filter_property_get",function(p_obj,p_parameters){
var f=p_obj.filters;
var i;
var filterClass=p_parameters[0];
var propertyName=p_parameters[1];
for(i=0;i<f.length;i++){
if(is(f[i],flash.filters.BlurFilter)&&filterClass==flash.filters.BlurFilter)return(f[i][propertyName]);
}
var defaultValues;
switch(filterClass){
case flash.filters.BlurFilter:
defaultValues={blurX:0,blurY:0,quality:NaN};
break;
}
return defaultValues[propertyName];
},
"public static function _filter_property_set",function(p_obj,p_value,p_parameters){
var f=p_obj.filters;
var i;
var filterClass=p_parameters[0];
var propertyName=p_parameters[1];
for(i=0;i<f.length;i++){
if(is(f[i],flash.filters.BlurFilter)&&filterClass==flash.filters.BlurFilter){
f[i][propertyName]=p_value;
p_obj.filters=f;
return;
}
}
if(f==null)f=new Array();
var fi;
switch(filterClass){
case flash.filters.BlurFilter:
fi=new flash.filters.BlurFilter(0,0);
break;
}
fi[propertyName]=p_value;
f.push(fi);
p_obj.filters=f;
},
"public static function _bezier_modifier",function(p_obj){
var mList=[];
var pList;
if(is(p_obj,Array)){
pList=p_obj;
}else{
pList=[p_obj];
}
var i;
var istr;
var mListObj={};
for(i=0;i<pList.length;i++){
for(istr in pList[i]){
if(mListObj[istr]==undefined)mListObj[istr]=[];
mListObj[istr].push(pList[i][istr]);
}
}
for(istr in mListObj){
mList.push({name:istr,parameters:mListObj[istr]});
}
return mList;
},
"public static function _bezier_get",function(b,e,t,p){
if(p.length==1){
return b+t*(2*(1-t)*(p[0]-b)+t*(e-b));
}else{
var ip=Math.floor(t*p.length);
var it=(t-(ip*(1/p.length)))*p.length;
var p1,p2;
if(ip==0){
p1=b;
p2=(p[0]+p[1])/2;
}else if(ip==p.length-1){
p1=(p[ip-1]+p[ip])/2;
p2=e;
}else{
p1=(p[ip-1]+p[ip])/2;
p2=(p[ip]+p[ip+1])/2;
}
return p1+it*(2*(1-it)*(p[ip]-p1)+it*(p2-p1));
}
},
];},["init","_color_splitter","_colorTransform_splitter","_scale_splitter","_filter_splitter","frame_get","frame_set","_sound_volume_get","_sound_volume_set","_sound_pan_get","_sound_pan_set","_color_property_get","_color_property_set","_autoAlpha_get","_autoAlpha_set","_filter_property_get","_filter_property_set","_bezier_modifier","_bezier_get"],["caurina.transitions.Tweener","flash.filters.BlurFilter","Array","caurina.transitions.AuxFunctions","Math"], "0.8.0", "0.8.4"
);
// class caurina.transitions.SpecialProperty
joo.classLoader.prepare("package caurina.transitions",
"public class SpecialProperty",1,function($$private){;return[
"public var",{getValue:null},
"public var",{setValue:null},
"public var",{parameters:null},
"public function SpecialProperty",function(p_getFunction,p_setFunction,p_parameters){if(arguments.length<3){p_parameters=null;}
this.getValue=p_getFunction;
this.setValue=p_setFunction;
this.parameters=p_parameters;
},
"public function toString",function(){
var value="";
value+="[SpecialProperty ";
value+="getValue:"+String(this.getValue);
value+=", ";
value+="setValue:"+String(this.setValue);
value+=", ";
value+="parameters:"+String(this.parameters);
value+="]";
return value;
},
];},[],["String"], "0.8.0", "0.8.4"
);
// class caurina.transitions.SpecialPropertyModifier
joo.classLoader.prepare("package caurina.transitions",
"public class SpecialPropertyModifier",1,function($$private){;return[
"public var",{modifyValues:null},
"public var",{getValue:null},
"public function SpecialPropertyModifier",function(p_modifyFunction,p_getFunction){
this.modifyValues=p_modifyFunction;
this.getValue=p_getFunction;
},
"public function toString",function(){
var value="";
value+="[SpecialPropertyModifier ";
value+="modifyValues:"+String(this.modifyValues);
value+=", ";
value+="getValue:"+String(this.getValue);
value+="]";
return value;
},
];},[],["String"], "0.8.0", "0.8.4"
);
// class caurina.transitions.SpecialPropertySplitter
joo.classLoader.prepare("package caurina.transitions",
"public class SpecialPropertySplitter",1,function($$private){;return[
"public var",{parameters:null},
"public var",{splitValues:null},
"public function SpecialPropertySplitter",function(p_splitFunction,p_parameters){
this.splitValues=p_splitFunction;
},
"public function toString",function(){
var value="";
value+="[SpecialPropertySplitter ";
value+="splitValues:"+String(this.splitValues);
value+=", ";
value+="parameters:"+String(this.parameters);
value+="]";
return value;
},
];},[],["String"], "0.8.0", "0.8.4"
);
// class caurina.transitions.Tweener
joo.classLoader.prepare(
"package caurina.transitions",
"public class Tweener",1,function($$private){var is=joo.is,trace=joo.trace;return[function(){joo.classLoader.init(flash.events.Event);},
"private static var",{__tweener_controller__:null},
"private static var",{_engineExists:false},
"private static var",{_inited:false},
"private static var",{_currentTime:NaN},
"private static var",{_tweenList:null},
"private static var",{_timeScale:1},
"private static var",{_transitionList:null},
"private static var",{_specialPropertyList:null},
"private static var",{_specialPropertyModifierList:null},
"private static var",{_specialPropertySplitterList:null},
"public function Tweener",function(){
trace("Tweener is a static class and should not be instantiated.");
},
"public static function addTween",function(p_arg1,p_arg2){if(arguments.length<2){if(arguments.length<1){p_arg1=null;}p_arg2=null;}
if(arguments.length<2||arguments[0]==undefined)return false;
var rScopes=new Array();
var i,j,istr,jstr;
if(is(arguments[0],Array)){
for(i=0;i<arguments[0].length;i++)rScopes.push(arguments[0][i]);
}else{
for(i=0;i<arguments.length-1;i++)rScopes.push(arguments[i]);
}
var p_obj=caurina.transitions.TweenListObj.makePropertiesChain(arguments[arguments.length-1]);
if(!$$private._inited)caurina.transitions.Tweener.init();
if(!$$private._engineExists||!Boolean($$private.__tweener_controller__))$$private.startEngine();
var rTime=(isNaN(p_obj.time)?0:p_obj.time);
var rDelay=(isNaN(p_obj.delay)?0:p_obj.delay);
var rProperties=new Array();
var restrictedWords={time:true,delay:true,useFrames:true,skipUpdates:true,transition:true,onStart:true,onUpdate:true,onComplete:true,onOverwrite:true,rounded:true,onStartParams:true,onUpdateParams:true,onCompleteParams:true,onOverwriteParams:true};
var modifiedProperties=new Object();
for(istr in p_obj){
if(!restrictedWords[istr]){
if($$private._specialPropertySplitterList[istr]){
var splitProperties=$$private._specialPropertySplitterList[istr].splitValues(p_obj[istr],$$private._specialPropertySplitterList[istr].parameters);
for(i=0;i<splitProperties.length;i++){
rProperties[splitProperties[i].name]={valueStart:undefined,valueComplete:splitProperties[i].value};
}
}else if($$private._specialPropertyModifierList[istr]!=undefined){
var tempModifiedProperties=$$private._specialPropertyModifierList[istr].modifyValues(p_obj[istr]);
for(i=0;i<tempModifiedProperties.length;i++){
modifiedProperties[tempModifiedProperties[i].name]={modifierParameters:tempModifiedProperties[i].parameters,modifierFunction:$$private._specialPropertyModifierList[istr].getValue};
}
}else{
rProperties[istr]={valueStart:undefined,valueComplete:p_obj[istr]};
}
}
}
for(istr in modifiedProperties){
if(rProperties[istr]!=undefined){
rProperties[istr].modifierParameters=modifiedProperties[istr].modifierParameters;
rProperties[istr].modifierFunction=modifiedProperties[istr].modifierFunction;
}
}
var rTransition;
if(typeof p_obj.transition=="string"){
var trans=p_obj.transition.toLowerCase();
rTransition=$$private._transitionList[trans];
}else{
rTransition=p_obj.transition;
}
if(!Boolean(rTransition))rTransition=$$private._transitionList["easeoutexpo"];
var nProperties;
var nTween;
var myT;
for(i=0;i<rScopes.length;i++){
nProperties=new Object();
for(istr in rProperties){
nProperties[istr]=new caurina.transitions.PropertyInfoObj(rProperties[istr].valueStart,rProperties[istr].valueComplete,rProperties[istr].modifierFunction,rProperties[istr].modifierParameters);
}
nTween=new caurina.transitions.TweenListObj(
rScopes[i],
$$private._currentTime+((rDelay*1000)/$$private._timeScale),
$$private._currentTime+(((rDelay*1000)+(rTime*1000))/$$private._timeScale),
(p_obj.useFrames==true),
rTransition
);
nTween.properties=nProperties;
nTween.onStart=p_obj.onStart;
nTween.onUpdate=p_obj.onUpdate;
nTween.onComplete=p_obj.onComplete;
nTween.onOverwrite=p_obj.onOverwrite;
nTween.onError=p_obj.onError;
nTween.onStartParams=p_obj.onStartParams;
nTween.onUpdateParams=p_obj.onUpdateParams;
nTween.onCompleteParams=p_obj.onCompleteParams;
nTween.onOverwriteParams=p_obj.onOverwriteParams;
nTween.rounded=p_obj.rounded;
nTween.skipUpdates=p_obj.skipUpdates;
caurina.transitions.Tweener.removeTweensByTime(nTween.scope,nTween.properties,nTween.timeStart,nTween.timeComplete);
$$private._tweenList.push(nTween);
if(rTime==0&&rDelay==0){
myT=$$private._tweenList.length-1;
$$private.updateTweenByIndex(myT);
caurina.transitions.Tweener.removeTweenByIndex(myT);
}
}
return true;
},
"public static function addCaller",function(p_arg1,p_arg2){if(arguments.length<2){if(arguments.length<1){p_arg1=null;}p_arg2=null;}
if(arguments.length<2||arguments[0]==undefined)return false;
var rScopes=new Array();
var i,j;
if(is(arguments[0],Array)){
for(i=0;i<arguments[0].length;i++)rScopes.push(arguments[0][i]);
}else{
for(i=0;i<arguments.length-1;i++)rScopes.push(arguments[i]);
}
var p_obj=arguments[arguments.length-1];
if(!$$private._inited)caurina.transitions.Tweener.init();
if(!$$private._engineExists||!Boolean($$private.__tweener_controller__))$$private.startEngine();
var rTime=(isNaN(p_obj.time)?0:p_obj.time);
var rDelay=(isNaN(p_obj.delay)?0:p_obj.delay);
var rTransition;
if(typeof p_obj.transition=="string"){
var trans=p_obj.transition.toLowerCase();
rTransition=$$private._transitionList[trans];
}else{
rTransition=p_obj.transition;
}
if(!Boolean(rTransition))rTransition=$$private._transitionList["easeoutexpo"];
var nTween;
var myT;
for(i=0;i<rScopes.length;i++){
nTween=new caurina.transitions.TweenListObj(
rScopes[i],
$$private._currentTime+((rDelay*1000)/$$private._timeScale),
$$private._currentTime+(((rDelay*1000)+(rTime*1000))/$$private._timeScale),
(p_obj.useFrames==true),
rTransition
);
nTween.properties=null;
nTween.onStart=p_obj.onStart;
nTween.onUpdate=p_obj.onUpdate;
nTween.onComplete=p_obj.onComplete;
nTween.onOverwrite=p_obj.onOverwrite;
nTween.onStartParams=p_obj.onStartParams;
nTween.onUpdateParams=p_obj.onUpdateParams;
nTween.onCompleteParams=p_obj.onCompleteParams;
nTween.onOverwriteParams=p_obj.onOverwriteParams;
nTween.isCaller=true;
nTween.count=p_obj.count;
nTween.waitFrames=p_obj.waitFrames;
$$private._tweenList.push(nTween);
if(rTime==0&&rDelay==0){
myT=$$private._tweenList.length-1;
$$private.updateTweenByIndex(myT);
caurina.transitions.Tweener.removeTweenByIndex(myT);
}
}
return true;
},
"public static function removeTweensByTime",function(p_scope,p_properties,p_timeStart,p_timeComplete){
var removed=false;
var removedLocally;
var i;
var tl=$$private._tweenList.length;
var pName;
for(i=0;i<tl;i++){
if(Boolean($$private._tweenList[i])&&p_scope==$$private._tweenList[i].scope){
if(p_timeComplete>$$private._tweenList[i].timeStart&&p_timeStart<$$private._tweenList[i].timeComplete){
removedLocally=false;
for(pName in $$private._tweenList[i].properties){
if(Boolean(p_properties[pName])){
if(Boolean($$private._tweenList[i].onOverwrite)){
try{
$$private._tweenList[i].onOverwrite.apply($$private._tweenList[i].scope,$$private._tweenList[i].onOverwriteParams);
}catch(e){if(is(e,Error)){
$$private.handleError($$private._tweenList[i],e,"onOverwrite");
}else throw e;}
}
$$private._tweenList[i].properties[pName]=undefined;
delete $$private._tweenList[i].properties[pName];
removedLocally=true;
removed=true;
}
}
if(removedLocally){
if(caurina.transitions.AuxFunctions.getObjectLength($$private._tweenList[i].properties)==0)caurina.transitions.Tweener.removeTweenByIndex(i);
}
}
}
}
return removed;
},
"public static function removeTweens",function(p_scope){var args=Array.prototype.slice.call(arguments,1);
var properties=new Array();
var i;
for(i=0;i<args.length;i++){
if(typeof(args[i])=="string"&&!caurina.transitions.AuxFunctions.isInArray(args[i],properties))properties.push(args[i]);
}
return $$private.affectTweens(caurina.transitions.Tweener.removeTweenByIndex,p_scope,properties);
},
"public static function removeAllTweens",function(){
if(!Boolean($$private._tweenList))return false;
var removed=false;
var i;
for(i=0;i<$$private._tweenList.length;i++){
caurina.transitions.Tweener.removeTweenByIndex(i);
removed=true;
}
return removed;
},
"public static function pauseTweens",function(p_scope){var args=Array.prototype.slice.call(arguments,1);
var properties=new Array();
var i;
for(i=0;i<args.length;i++){
if(typeof(args[i])=="string"&&!caurina.transitions.AuxFunctions.isInArray(args[i],properties))properties.push(args[i]);
}
return $$private.affectTweens(caurina.transitions.Tweener.pauseTweenByIndex,p_scope,properties);
},
"public static function pauseAllTweens",function(){
if(!Boolean($$private._tweenList))return false;
var paused=false;
var i;
for(i=0;i<$$private._tweenList.length;i++){
caurina.transitions.Tweener.pauseTweenByIndex(i);
paused=true;
}
return paused;
},
"public static function resumeTweens",function(p_scope){var args=Array.prototype.slice.call(arguments,1);
var properties=new Array();
var i;
for(i=0;i<args.length;i++){
if(typeof(args[i])=="string"&&!caurina.transitions.AuxFunctions.isInArray(args[i],properties))properties.push(args[i]);
}
return $$private.affectTweens(caurina.transitions.Tweener.resumeTweenByIndex,p_scope,properties);
},
"public static function resumeAllTweens",function(){
if(!Boolean($$private._tweenList))return false;
var resumed=false;
var i;
for(i=0;i<$$private._tweenList.length;i++){
caurina.transitions.Tweener.resumeTweenByIndex(i);
resumed=true;
}
return resumed;
},
"private static function affectTweens",function(p_affectFunction,p_scope,p_properties){
var affected=false;
var i;
if(!Boolean($$private._tweenList))return false;
for(i=0;i<$$private._tweenList.length;i++){
if($$private._tweenList[i]&&$$private._tweenList[i].scope==p_scope){
if(p_properties.length==0){
p_affectFunction(i);
affected=true;
}else{
var affectedProperties=new Array();
var j;
for(j=0;j<p_properties.length;j++){
if(Boolean($$private._tweenList[i].properties[p_properties[j]])){
affectedProperties.push(p_properties[j]);
}
}
if(affectedProperties.length>0){
var objectProperties=caurina.transitions.AuxFunctions.getObjectLength($$private._tweenList[i].properties);
if(objectProperties==affectedProperties.length){
p_affectFunction(i);
affected=true;
}else{
var slicedTweenIndex=caurina.transitions.Tweener.splitTweens(i,affectedProperties);
p_affectFunction(slicedTweenIndex);
affected=true;
}
}
}
}
}
return affected;
},
"public static function splitTweens",function(p_tween,p_properties){
var originalTween=$$private._tweenList[p_tween];
var newTween=originalTween.clone(false);
var i;
var pName;
for(i=0;i<p_properties.length;i++){
pName=p_properties[i];
if(Boolean(originalTween.properties[pName])){
originalTween.properties[pName]=undefined;
delete originalTween.properties[pName];
}
}
var found;
for(pName in newTween.properties){
found=false;
for(i=0;i<p_properties.length;i++){
if(p_properties[i]==pName){
found=true;
break;
}
}
if(!found){
newTween.properties[pName]=undefined;
delete newTween.properties[pName];
}
}
$$private._tweenList.push(newTween);
return($$private._tweenList.length-1);
},
"private static function updateTweens",function(){
if($$private._tweenList.length==0)return false;
var i;
for(i=0;i<$$private._tweenList.length;i++){
if($$private._tweenList[i]==undefined||!$$private._tweenList[i].isPaused){
if(!$$private.updateTweenByIndex(i))caurina.transitions.Tweener.removeTweenByIndex(i);
if($$private._tweenList[i]==null){
caurina.transitions.Tweener.removeTweenByIndex(i,true);
i--;
}
}
}
return true;
},
"public static function removeTweenByIndex",function(i,p_finalRemoval){if(arguments.length<2){p_finalRemoval=false;}
$$private._tweenList[i]=null;
if(p_finalRemoval)$$private._tweenList.splice(i,1);
return true;
},
"public static function pauseTweenByIndex",function(p_tween){
var tTweening=$$private._tweenList[p_tween];
if(tTweening==null||tTweening.isPaused)return false;
tTweening.timePaused=$$private._currentTime;
tTweening.isPaused=true;
return true;
},
"public static function resumeTweenByIndex",function(p_tween){
var tTweening=$$private._tweenList[p_tween];
if(tTweening==null||!tTweening.isPaused)return false;
tTweening.timeStart+=$$private._currentTime-tTweening.timePaused;
tTweening.timeComplete+=$$private._currentTime-tTweening.timePaused;
tTweening.timePaused=undefined;
tTweening.isPaused=false;
return true;
},
"private static function updateTweenByIndex",function(i){
var tTweening=$$private._tweenList[i];
if(tTweening==null||!Boolean(tTweening.scope))return false;
var isOver=false;
var mustUpdate;
var nv;
var t;
var b;
var c;
var d;
var pName;
var tScope;
var tProperty;
if($$private._currentTime>=tTweening.timeStart){
tScope=tTweening.scope;
if(tTweening.isCaller){
do{
t=((tTweening.timeComplete-tTweening.timeStart)/tTweening.count)*(tTweening.timesCalled+1);
b=tTweening.timeStart;
c=tTweening.timeComplete-tTweening.timeStart;
d=tTweening.timeComplete-tTweening.timeStart;
nv=tTweening.transition(t,b,c,d);
if($$private._currentTime>=nv){
if(Boolean(tTweening.onUpdate)){
try{
tTweening.onUpdate.apply(tScope,tTweening.onUpdateParams);
}catch(e){if(is(e,Error)){
$$private.handleError(tTweening,e,"onUpdate");
}else throw e;}
}
tTweening.timesCalled++;
if(tTweening.timesCalled>=tTweening.count){
isOver=true;
break;
}
if(tTweening.waitFrames)break;
}
}while($$private._currentTime>=nv);
}else{
mustUpdate=tTweening.skipUpdates<1||!tTweening.skipUpdates||tTweening.updatesSkipped>=tTweening.skipUpdates;
if($$private._currentTime>=tTweening.timeComplete){
isOver=true;
mustUpdate=true;
}
if(!tTweening.hasStarted){
if(Boolean(tTweening.onStart)){
try{
tTweening.onStart.apply(tScope,tTweening.onStartParams);
}catch(e){if(is(e,Error)){
$$private.handleError(tTweening,e,"onStart");
}else throw e;}
}
for(pName in tTweening.properties){
var pv=$$private.getPropertyValue(tScope,pName);
tTweening.properties[pName].valueStart=isNaN(pv)?tTweening.properties[pName].valueComplete:pv;
}
mustUpdate=true;
tTweening.hasStarted=true;
}
if(mustUpdate){
for(pName in tTweening.properties){
tProperty=tTweening.properties[pName];
if(isOver){
nv=tProperty.valueComplete;
}else{
if(tProperty.hasModifier){
t=$$private._currentTime-tTweening.timeStart;
d=tTweening.timeComplete-tTweening.timeStart;
nv=tTweening.transition(t,0,1,d);
nv=tProperty.modifierFunction(tProperty.valueStart,tProperty.valueComplete,nv,tProperty.modifierParameters);
}else{
t=$$private._currentTime-tTweening.timeStart;
b=tProperty.valueStart;
c=tProperty.valueComplete-tProperty.valueStart;
d=tTweening.timeComplete-tTweening.timeStart;
nv=tTweening.transition(t,b,c,d);
}
}
if(tTweening.rounded)nv=Math.round(nv);
$$private.setPropertyValue(tScope,pName,nv);
}
tTweening.updatesSkipped=0;
if(Boolean(tTweening.onUpdate)){
try{
tTweening.onUpdate.apply(tScope,tTweening.onUpdateParams);
}catch(e){if(is(e,Error)){
$$private.handleError(tTweening,e,"onUpdate");
}else throw e;}
}
}else{
tTweening.updatesSkipped++;
}
}
if(isOver&&Boolean(tTweening.onComplete)){
try{
tTweening.onComplete.apply(tScope,tTweening.onCompleteParams);
}catch(e){if(is(e,Error)){
$$private.handleError(tTweening,e,"onComplete");
}else throw e;}
}
return(!isOver);
}
return(true);
},
"public static function init",function(p_object){if(arguments.length<1){p_object=null;}
$$private._inited=true;
$$private._transitionList=new Object();
caurina.transitions.Equations.init();
$$private._specialPropertyList=new Object();
$$private._specialPropertyModifierList=new Object();
$$private._specialPropertySplitterList=new Object();
caurina.transitions.SpecialPropertiesDefault.init();
},
"public static function registerTransition",function(p_name,p_function){
if(!$$private._inited)caurina.transitions.Tweener.init();
$$private._transitionList[p_name]=p_function;
},
"public static function registerSpecialProperty",function(p_name,p_getFunction,p_setFunction,p_parameters){if(arguments.length<4){p_parameters=null;}
if(!$$private._inited)caurina.transitions.Tweener.init();
var sp=new caurina.transitions.SpecialProperty(p_getFunction,p_setFunction,p_parameters);
$$private._specialPropertyList[p_name]=sp;
},
"public static function registerSpecialPropertyModifier",function(p_name,p_modifyFunction,p_getFunction){
if(!$$private._inited)caurina.transitions.Tweener.init();
var spm=new caurina.transitions.SpecialPropertyModifier(p_modifyFunction,p_getFunction);
$$private._specialPropertyModifierList[p_name]=spm;
},
"public static function registerSpecialPropertySplitter",function(p_name,p_splitFunction,p_parameters){if(arguments.length<3){p_parameters=null;}
if(!$$private._inited)caurina.transitions.Tweener.init();
var sps=new caurina.transitions.SpecialPropertySplitter(p_splitFunction,p_parameters);
$$private._specialPropertySplitterList[p_name]=sps;
},
"private static function startEngine",function(){
$$private._engineExists=true;
$$private._tweenList=new Array();
$$private.__tweener_controller__=new flash.display.MovieClip();
$$private.__tweener_controller__.addEventListener(flash.events.Event.ENTER_FRAME,caurina.transitions.Tweener.onEnterFrame);
caurina.transitions.Tweener.updateTime();
},
"private static function stopEngine",function(){
$$private._engineExists=false;
$$private._tweenList=null;
$$private._currentTime=0;
$$private.__tweener_controller__.removeEventListener(flash.events.Event.ENTER_FRAME,caurina.transitions.Tweener.onEnterFrame);
$$private.__tweener_controller__=null;
},
"private static function getPropertyValue",function(p_obj,p_prop){
if($$private._specialPropertyList[p_prop]!=undefined){
if(Boolean($$private._specialPropertyList[p_prop].parameters)){
return $$private._specialPropertyList[p_prop].getValue(p_obj,$$private._specialPropertyList[p_prop].parameters);
}else{
return $$private._specialPropertyList[p_prop].getValue(p_obj);
}
}else{
return p_obj[p_prop];
}
},
"private static function setPropertyValue",function(p_obj,p_prop,p_value){
if($$private._specialPropertyList[p_prop]!=undefined){
if(Boolean($$private._specialPropertyList[p_prop].parameters)){
$$private._specialPropertyList[p_prop].setValue(p_obj,p_value,$$private._specialPropertyList[p_prop].parameters);
}else{
$$private._specialPropertyList[p_prop].setValue(p_obj,p_value);
}
}else{
p_obj[p_prop]=p_value;
}
},
"public static function updateTime",function(){
$$private._currentTime=flash.utils.getTimer();
},
"public static function onEnterFrame",function(e){
caurina.transitions.Tweener.updateTime();
var hasUpdated=false;
hasUpdated=$$private.updateTweens();
if(!hasUpdated)$$private.stopEngine();
},
"public static function setTimeScale",function(p_time){
var i;
if(isNaN(p_time))p_time=1;
if(p_time<0.00001)p_time=0.00001;
if(p_time!=$$private._timeScale){
if($$private._tweenList!=null){
for(i=0;i<$$private._tweenList.length;i++){
$$private._tweenList[i].timeStart=$$private._currentTime-(($$private._currentTime-$$private._tweenList[i].timeStart)*$$private._timeScale/p_time);
$$private._tweenList[i].timeComplete=$$private._currentTime-(($$private._currentTime-$$private._tweenList[i].timeComplete)*$$private._timeScale/p_time);
if($$private._tweenList[i].timePaused!=undefined)$$private._tweenList[i].timePaused=$$private._currentTime-(($$private._currentTime-$$private._tweenList[i].timePaused)*$$private._timeScale/p_time);
}
}
$$private._timeScale=p_time;
}
},
"public static function isTweening",function(p_scope){
if(!Boolean($$private._tweenList))return false;
var i;
for(i=0;i<$$private._tweenList.length;i++){
if($$private._tweenList[i].scope==p_scope){
return true;
}
}
return false;
},
"public static function getTweens",function(p_scope){
if(!Boolean($$private._tweenList))return[];
var i;
var pName;
var tList=new Array();
for(i=0;i<$$private._tweenList.length;i++){
if($$private._tweenList[i].scope==p_scope){
for(pName in $$private._tweenList[i].properties)tList.push(pName);
}
}
return tList;
},
"public static function getTweenCount",function(p_scope){
if(!Boolean($$private._tweenList))return 0;
var i;
var c=0;
for(i=0;i<$$private._tweenList.length;i++){
if($$private._tweenList[i].scope==p_scope){
c+=caurina.transitions.AuxFunctions.getObjectLength($$private._tweenList[i].properties);
}
}
return c;
},
"private static function handleError",function(pTweening,pError,pCallBackName){
if(Boolean(pTweening.onError)&&(is(pTweening.onError,Function))){
try{
pTweening.onError.apply(pTweening.scope,[pTweening.scope,pError]);
}catch(metaError){if(is(metaError,Error)){
trace("## [Tweener] Error:",pTweening.scope,"raised an error while executing the 'onError' handler. Original error:\n",pError.stack,"\nonError error:",metaError.getStackTrace());
}else throw metaError;}
}else{
if(!Boolean(pTweening.onError)){
trace("## [Tweener] Error: :",pTweening.scope,"raised an error while executing the'"+pCallBackName+"'handler. \n",pError.stack);
}
}
},
"public static function getVersion",function(){
return"AS3 1.26.62";
},
"public static function debug_getList",function(){
var ttl="";
var i,k;
for(i=0;i<$$private._tweenList.length;i++){
ttl+="["+i+"] ::\n";
for(k=0;k<$$private._tweenList[i].properties.length;k++){
ttl+="  "+$$private._tweenList[i].properties[k].name+" -> "+$$private._tweenList[i].properties[k].valueComplete+"\n";
}
}
return ttl;
},
];},["addTween","addCaller","removeTweensByTime","removeTweens","removeAllTweens","pauseTweens","pauseAllTweens","resumeTweens","resumeAllTweens","splitTweens","removeTweenByIndex","pauseTweenByIndex","resumeTweenByIndex","init","registerTransition","registerSpecialProperty","registerSpecialPropertyModifier","registerSpecialPropertySplitter","updateTime","onEnterFrame","setTimeScale","isTweening","getTweens","getTweenCount","getVersion","debug_getList"],["Array","caurina.transitions.TweenListObj","Boolean","Object","caurina.transitions.PropertyInfoObj","Error","caurina.transitions.AuxFunctions","Math","caurina.transitions.Equations","caurina.transitions.SpecialPropertiesDefault","caurina.transitions.SpecialProperty","caurina.transitions.SpecialPropertyModifier","caurina.transitions.SpecialPropertySplitter","flash.display.MovieClip","flash.events.Event","Function"], "0.8.0", "0.8.4"
);
// class caurina.transitions.TweenListObj
joo.classLoader.prepare("package caurina.transitions",
"public class TweenListObj",1,function($$private){var is=joo.is;return[
"public var",{scope:null},
"public var",{properties:null},
"public var",{auxProperties:null},
"public var",{timeStart:NaN},
"public var",{timeComplete:NaN},
"public var",{useFrames:false},
"public var",{transition:null},
"public var",{onStart:null},
"public var",{onUpdate:null},
"public var",{onComplete:null},
"public var",{onOverwrite:null},
"public var",{onError:null},
"public var",{onStartParams:null},
"public var",{onUpdateParams:null},
"public var",{onCompleteParams:null},
"public var",{onOverwriteParams:null},
"public var",{rounded:false},
"public var",{isPaused:false},
"public var",{timePaused:NaN},
"public var",{isCaller:false},
"public var",{count:NaN},
"public var",{timesCalled:NaN},
"public var",{waitFrames:false},
"public var",{skipUpdates:NaN},
"public var",{updatesSkipped:NaN},
"public var",{hasStarted:false},
"function TweenListObj",function(p_scope,p_timeStart,p_timeComplete,p_useFrames,p_transition){
this.scope=p_scope;
this.timeStart=p_timeStart;
this.timeComplete=p_timeComplete;
this.useFrames=p_useFrames;
this.transition=p_transition;
this.auxProperties=new Object();
this.properties=new Object();
this.isPaused=false;
this.timePaused=undefined;
this.isCaller=false;
this.updatesSkipped=0;
this.timesCalled=0;
this.skipUpdates=0;
this.hasStarted=false;
},
"public function clone",function(omitEvents){
var nTween=new caurina.transitions.TweenListObj(this.scope,this.timeStart,this.timeComplete,this.useFrames,this.transition);
nTween.properties=new Array();
for(var pName in this.properties){
nTween.properties[pName]=this.properties[pName].clone();
}
nTween.skipUpdates=this.skipUpdates;
nTween.updatesSkipped=this.updatesSkipped;
if(!omitEvents){
nTween.onStart=this.onStart;
nTween.onUpdate=this.onUpdate;
nTween.onComplete=this.onComplete;
nTween.onOverwrite=this.onOverwrite;
nTween.onError=this.onError;
nTween.onStartParams=this.onStartParams;
nTween.onUpdateParams=this.onUpdateParams;
nTween.onCompleteParams=this.onCompleteParams;
nTween.onOverwriteParams=this.onOverwriteParams;
}
nTween.rounded=this.rounded;
nTween.isPaused=this.isPaused;
nTween.timePaused=this.timePaused;
nTween.isCaller=this.isCaller;
nTween.count=this.count;
nTween.timesCalled=this.timesCalled;
nTween.waitFrames=this.waitFrames;
nTween.hasStarted=this.hasStarted;
return nTween;
},
"public function toString",function(){
var returnStr="\n[TweenListObj ";
returnStr+="scope:"+String(this.scope);
returnStr+=", properties:";
for(var i=0;i<this.properties.length;i++){
if(i>0)returnStr+=",";
returnStr+="[name:"+this.properties[i].name;
returnStr+=",valueStart:"+this.properties[i].valueStart;
returnStr+=",valueComplete:"+this.properties[i].valueComplete;
returnStr+="]";
}
returnStr+=", timeStart:"+String(this.timeStart);
returnStr+=", timeComplete:"+String(this.timeComplete);
returnStr+=", useFrames:"+String(this.useFrames);
returnStr+=", transition:"+String(this.transition);
if(this.skipUpdates)returnStr+=", skipUpdates:"+String(this.skipUpdates);
if(this.updatesSkipped)returnStr+=", updatesSkipped:"+String(this.updatesSkipped);
if(Boolean(this.onStart))returnStr+=", onStart:"+String(this.onStart);
if(Boolean(this.onUpdate))returnStr+=", onUpdate:"+String(this.onUpdate);
if(Boolean(this.onComplete))returnStr+=", onComplete:"+String(this.onComplete);
if(Boolean(this.onOverwrite))returnStr+=", onOverwrite:"+String(this.onOverwrite);
if(Boolean(this.onError))returnStr+=", onError:"+String(this.onError);
if(this.onStartParams)returnStr+=", onStartParams:"+String(this.onStartParams);
if(this.onUpdateParams)returnStr+=", onUpdateParams:"+String(this.onUpdateParams);
if(this.onCompleteParams)returnStr+=", onCompleteParams:"+String(this.onCompleteParams);
if(this.onOverwriteParams)returnStr+=", onOverwriteParams:"+String(this.onOverwriteParams);
if(this.rounded)returnStr+=", rounded:"+String(this.rounded);
if(this.isPaused)returnStr+=", isPaused:"+String(this.isPaused);
if(this.timePaused)returnStr+=", timePaused:"+String(this.timePaused);
if(this.isCaller)returnStr+=", isCaller:"+String(this.isCaller);
if(this.count)returnStr+=", count:"+String(this.count);
if(this.timesCalled)returnStr+=", timesCalled:"+String(this.timesCalled);
if(this.waitFrames)returnStr+=", waitFrames:"+String(this.waitFrames);
if(this.hasStarted)returnStr+=", hasStarted:"+String(this.hasStarted);
returnStr+="]\n";
return returnStr;
},
"public static function makePropertiesChain",function(p_obj){
var baseObject=p_obj.base;
if(baseObject){
var chainedObject={};
var chain;
if(is(baseObject,Array)){
chain=[];
for(var k=0;k<baseObject.length;k++)chain.push(baseObject[k]);
}else{
chain=[baseObject];
}
chain.push(p_obj);
var currChainObj;
var len=chain.length;
for(var i=0;i<len;i++){
if(chain[i]["base"]){
currChainObj=caurina.transitions.AuxFunctions.concatObjects(caurina.transitions.TweenListObj.makePropertiesChain(chain[i]["base"]),chain[i]);
}else{
currChainObj=chain[i];
}
chainedObject=caurina.transitions.AuxFunctions.concatObjects(chainedObject,currChainObj);
}
if(chainedObject["base"]){
delete chainedObject["base"];
}
return chainedObject;
}else{
return p_obj;
}
},
];},["makePropertiesChain"],["Object","Array","String","Boolean","caurina.transitions.AuxFunctions"], "0.8.0", "0.8.4"
);
// class charts.Area
joo.classLoader.prepare("package charts",
"public class Area extends charts.Line",8,function($$private){var as=joo.as,is=joo.is;return[
"private var",{fill_colour:NaN},
"private var",{area_base:NaN},
"public function Area",function(json){
this.super$8(json);
var fill;
if(json.fill)
fill=json.fill;
else
fill=json.colour;
this.fill_colour$8=string.Utils.get_colour(fill);
},
"public override function resize",function(sc){
var right_axis=false;
if(this.props.has('axis'))
if(this.props.get('axis')=='right')
right_axis=true;
this.area_base$8=sc.get_y_bottom(right_axis);
this.resize$8(sc);
},
"protected override function draw",function(){
this.graphics.clear();
this.fill_area$8();
this.draw_line();
},
"private function fill_area",function(){
var last;
var first=true;
var tmp;
for(var i=0;i<this.numChildren;i++){
tmp=as(this.getChildAt(i),flash.display.Sprite);
if(is(tmp,charts.series.Element)){
var e=as(tmp,charts.series.Element);
if(first)
{
first=false;
if(this.props.get('loop'))
{
this.graphics.moveTo(e.x,e.y);
}
else
{
this.graphics.moveTo(e.x,this.area_base$8);
}
this.graphics.lineStyle(0,0,0);
this.graphics.beginFill(this.fill_colour$8,this.props.get('fill-alpha'));
if(!this.props.get('loop'))
this.graphics.lineTo(e.x,e.y);
}
else
{
this.graphics.lineTo(e.x,e.y);
last=e;
}
}
}
if(last!=null){
if(!this.props.get('loop')){
this.graphics.lineTo(last.x,this.area_base$8);
}
}
this.graphics.endFill();
},
];},[],["charts.Line","string.Utils","flash.display.Sprite","charts.series.Element"], "0.8.0", "0.8.4"
);
// class charts.Arrow
joo.classLoader.prepare("package charts",
"public class Arrow extends charts.Base",7,function($$private){;return[
"private var",{style:null},
"public function Arrow",function(json)
{this.super$7();
this.style$7={
start:[],
end:[],
colour:'#808080',
alpha:0.5,
'barb-length':20
};
object_helper.merge_2(json,this.style$7);
this.style$7.colour=string.Utils.get_colour(this.style$7.colour);
},
"public override function resize",function(sc){
this.graphics.clear();
this.graphics.lineStyle(1,this.style$7.colour,1);
this.graphics.moveTo(
sc.get_x_from_val(this.style$7.start.x),
sc.get_y_from_val(this.style$7.start.y));
var x=sc.get_x_from_val(this.style$7.end.x);
var y=sc.get_y_from_val(this.style$7.end.y);
this.graphics.lineTo(x,y);
var angle=Math.atan2(
sc.get_y_from_val(this.style$7.start.y)-y,
sc.get_x_from_val(this.style$7.start.x)-x
);
var barb_length=this.style$7['barb-length'];
var barb_angle=0.34;
var a=x+(barb_length*Math.cos(angle-barb_angle));
var b=y+(barb_length*Math.sin(angle-barb_angle));
var c=x+(barb_length*Math.cos(angle+barb_angle));
var d=y+(barb_length*Math.sin(angle+barb_angle));
this.graphics.moveTo(x,y);
this.graphics.lineTo(a,b);
this.graphics.moveTo(x,y);
this.graphics.lineTo(c,d);
},
];},[],["charts.Base","object_helper","string.Utils","Math"], "0.8.0", "0.8.4"
);
// class charts.Bar
joo.classLoader.prepare("package charts",
"public class Bar extends charts.BarBase",8,function($$private){;return[
"public function Bar",function(json,group){
this.super$8(json,group);
},
"protected override function get_element",function(index,value){
return new charts.series.bars.Bar(index,this.get_element_helper_prop(value),this.group);
},
];},[],["charts.BarBase","charts.series.bars.Bar"], "0.8.0", "0.8.4"
);
// class charts.Bar3D
joo.classLoader.prepare("package charts",
"public class Bar3D extends charts.BarBase",8,function($$private){;return[
"public function Bar3D",function(json,group){
this.super$8(json,group);
},
"protected override function get_element",function(index,value){
return new charts.series.bars.Bar3D(index,this.get_element_helper_prop(value),this.group);
},
];},[],["charts.BarBase","charts.series.bars.Bar3D"], "0.8.0", "0.8.4"
);
// class charts.BarBase
joo.classLoader.prepare("package charts",
"public class BarBase extends charts.Base",7,function($$private){var as=joo.as,is=joo.is;return[function(){joo.classLoader.init(Number);},

"protected var",{group:NaN},
"protected var",{props:null},
"protected var",{on_show:null},
"public function BarBase",function(json,group)
{this.super$7();
var root=new Properties({
values:[],
colour:'#3030d0',
text:'',
'font-size':12,
tip:'#val#<br>#x_label#',
alpha:0.6,
'on-click':false,
'axis':'left'
});
this.props=new Properties(json,root);
this.on_show=this.get_on_show(json['on-show']);
this.colour=this.props.get_colour('colour');
this.key=this.props.get('text');
this.font_size=this.props.get('font-size');
this.props.set('tip',this.props.get('tip').replace('#key#',this.key));
this.group=group;
this.values=this.props.get('values');
this.add_values();
},
"protected function get_on_show",function(json){
var on_show_root=new Properties({
type:"none",
cascade:3,
delay:0
});
return new Properties(json,on_show_root);
},
"public override function resize",function(sc){
for(var i=0;i<this.numChildren;i++)
{
var e=as(this.getChildAt(i),charts.series.Element);
e.resize(sc);
}
},
"public override function get_max_x",function(){
var max_index=Number.MIN_VALUE;
for(var i=0;i<this.numChildren;i++){
var e=as(this.getChildAt(i),charts.series.Element);
max_index=Math.max(max_index,e.index);
}
return max_index;
},
"public override function get_min_x",function(){
return 0;
},
"protected function get_element_helper_prop",function(value){
var default_style=new Properties({
colour:this.props.get('colour'),
tip:this.props.get('tip'),
alpha:this.props.get('alpha'),
'on-click':this.props.get('on-click'),
axis:this.props.get('axis'),
'on-show':this.on_show
});
var s;
if(is(value,Number))
s=new Properties({'top':value},default_style);
else
s=new Properties(value,default_style);
return s;
},
"public override function closest",function(x,y){
var shortest=Number.MAX_VALUE;
var ex=null;
for(var i=0;i<this.numChildren;i++)
{
var e=as(this.getChildAt(i),charts.series.Element);
e.is_tip=false;
if((x>e.x)&&(x<e.x+e.width))
{
shortest=Math.min(Math.abs(x-e.x),Math.abs(x-(e.x+e.width)));
ex=e;
break;
}
else
{
var d1=Math.abs(x-e.x);
var d2=Math.abs(x-(e.x+e.width));
var min=Math.min(d1,d2);
if(min<shortest)
{
shortest=min;
ex=e;
}
}
}
var dy=Math.abs(y-ex.y);
return{element:ex,distance_x:shortest,distance_y:dy};
},
"public override function die",function(){
this.die$7();
this.props.die();
},
];},[],["charts.Base","Properties","charts.series.Element","Number","Math"], "0.8.0", "0.8.4"
);
// class charts.BarCylinder
joo.classLoader.prepare("package charts",
"public class BarCylinder extends charts.BarBase",8,function($$private){;return[
"public function BarCylinder",function(json,group){
this.super$8(json,group);
},
"protected override function get_element",function(index,value){
return new charts.series.bars.Cylinder(index,this.get_element_helper_prop(value),this.group);
},
];},[],["charts.BarBase","charts.series.bars.Cylinder"], "0.8.0", "0.8.4"
);
// class charts.BarCylinderOutline
joo.classLoader.prepare("package charts",
"public class BarCylinderOutline extends charts.BarBase",8,function($$private){;return[
"public function BarCylinderOutline",function(json,group){
this.super$8(json,group);
},
"protected override function get_element",function(index,value){
return new charts.series.bars.CylinderOutline(index,this.get_element_helper_prop(value),this.group);
},
];},[],["charts.BarBase","charts.series.bars.CylinderOutline"], "0.8.0", "0.8.4"
);
// class charts.BarDome
joo.classLoader.prepare("package charts",
"public class BarDome extends charts.BarBase",8,function($$private){;return[
"public function BarDome",function(json,group){
this.super$8(json,group);
},
"protected override function get_element",function(index,value){
return new charts.series.bars.Dome(index,this.get_element_helper_prop(value),this.group);
},
];},[],["charts.BarBase","charts.series.bars.Dome"], "0.8.0", "0.8.4"
);
// class charts.BarFade
joo.classLoader.prepare("package charts",
"public class BarFade extends charts.BarBase",8,function($$private){;return[
"public function BarFade",function(json,group){
this.super$8(json,group);
},
"protected override function get_element",function(index,value){
return new charts.Elements.PointBarFade(index,value,this.colour,this.group);
},
];},[],["charts.BarBase","charts.Elements.PointBarFade"], "0.8.0", "0.8.4"
);
// class charts.BarGlass
joo.classLoader.prepare("package charts",
"public class BarGlass extends charts.BarBase",8,function($$private){;return[
"public function BarGlass",function(json,group){
this.super$8(json,group);
},
"protected override function get_element",function(index,value){
return new charts.series.bars.Glass(index,this.get_element_helper_prop(value),this.group);
},
];},[],["charts.BarBase","charts.series.bars.Glass"], "0.8.0", "0.8.4"
);
// class charts.BarOutline
joo.classLoader.prepare("package charts",
"public class BarOutline extends charts.BarBase",8,function($$private){;return[
"private var",{outline_colour:NaN},
"protected var",{style:null},
"public function BarOutline",function(json,group){
this.style={
'outline-colour':"#000000"
};
object_helper.merge_2(json,this.style);
this.super$8(json,group);
},
"protected override function get_element",function(index,value){
var root=new Properties({
'outline-colour':this.style['outline-colour']
});
var default_style=this.get_element_helper_prop(value);
default_style.set_parent(root);
return new charts.series.bars.Outline(index,default_style,this.group);
},
];},[],["charts.BarBase","object_helper","Properties","charts.series.bars.Outline"], "0.8.0", "0.8.4"
);
// class charts.BarPlastic
joo.classLoader.prepare("package charts",
"public class BarPlastic extends charts.BarBase",8,function($$private){;return[
"public function BarPlastic",function(json,group){
this.super$8(json,group);
},
"protected override function get_element",function(index,value){
return new charts.series.bars.Plastic(index,this.get_element_helper_prop(value),this.group);
},
];},[],["charts.BarBase","charts.series.bars.Plastic"], "0.8.0", "0.8.4"
);
// class charts.BarPlasticFlat
joo.classLoader.prepare("package charts",
"public class BarPlasticFlat extends charts.BarBase",8,function($$private){;return[
"public function BarPlasticFlat",function(json,group){
this.super$8(json,group);
},
"protected override function get_element",function(index,value){
return new charts.series.bars.PlasticFlat(index,this.get_element_helper_prop(value),this.group);
},
];},[],["charts.BarBase","charts.series.bars.PlasticFlat"], "0.8.0", "0.8.4"
);
// class charts.BarRound
joo.classLoader.prepare("package charts",
"public class BarRound extends charts.BarBase",8,function($$private){;return[
"public function BarRound",function(json,group){
this.super$8(json,group);
},
"protected override function get_element",function(index,value){
return new charts.series.bars.Round(index,this.get_element_helper_prop(value),this.group);
},
];},[],["charts.BarBase","charts.series.bars.Round"], "0.8.0", "0.8.4"
);
// class charts.BarRound3D
joo.classLoader.prepare("package charts",
"public class BarRound3D extends charts.BarBase",8,function($$private){;return[
"public function BarRound3D",function(json,group){
this.super$8(json,group);
},
"protected override function get_element",function(index,value){
return new charts.series.bars.Round3D(index,this.get_element_helper_prop(value),this.group);
},
];},[],["charts.BarBase","charts.series.bars.Round3D"], "0.8.0", "0.8.4"
);
// class charts.BarRoundGlass
joo.classLoader.prepare("package charts",
"public class BarRoundGlass extends charts.BarBase",8,function($$private){;return[
"public function BarRoundGlass",function(json,group){
this.super$8(json,group);
},
"protected override function get_element",function(index,value){
return new charts.series.bars.RoundGlass(index,this.get_element_helper_prop(value),this.group);
},
];},[],["charts.BarBase","charts.series.bars.RoundGlass"], "0.8.0", "0.8.4"
);
// class charts.BarSketch
joo.classLoader.prepare("package charts",
"public class BarSketch extends charts.BarBase",8,function($$private){;return[
"private var",{outline_colour:NaN},
"private var",{offset:NaN},
"protected var",{style:null},
"public function BarSketch",function(json,group){
this.style={
'outline-colour':"#000000",
offset:6
};
object_helper.merge_2(json,this.style);
this.super$8(this.style,group);
},
"protected override function get_element",function(index,value){
var root=new Properties({
'outline-colour':this.style['outline-colour'],
offset:this.style.offset
});
var default_style=this.get_element_helper_prop(value);
default_style.set_parent(root);
return new charts.series.bars.Sketch(index,default_style,this.group);
},
];},[],["charts.BarBase","object_helper","Properties","charts.series.bars.Sketch"], "0.8.0", "0.8.4"
);
// class charts.BarStack
joo.classLoader.prepare("package charts",
"public class BarStack extends charts.BarBase",8,function($$private){var is=joo.is,as=joo.as;return[
"public function BarStack",function(json,num,group){
this.super$8({},0);
var root=new Properties({
values:[],
keys:[],
colours:['#FF0000','#00FF00'],
text:'',
'font-size':12,
tip:'#x_label# : #val#<br>Total: #total#',
alpha:0.6,
'on-click':false,
'axis':'left'
});
this.props=new Properties(json,root);
this.on_show=this.get_on_show(json['on-show']);
this.group=group;
this.values=json.values;
this.add_values();
},
"public override function get_keys",function(){
var tmp=[];
for(var $1 in this.props.get('keys')){var o=this.props.get('keys')[$1];
if(o.text&&o['font-size']&&o.colour){
o.colour=string.Utils.get_colour(o.colour);
tmp.push(o);
}
}
return tmp;
},
"protected override function get_element",function(index,value){
var default_style=new Properties({
colours:this.props.get('colours'),
tip:this.props.get('tip'),
alpha:this.props.get('alpha'),
'on-click':this.props.get('on-click'),
axis:this.props.get('axis'),
'on-show':this.on_show,
values:value
});
return new charts.series.bars.StackCollection(index,default_style,this.group);
},
"protected override function get_all_at_this_x_pos",function(x){
var tmp=new Array();
var p;
var e;
for(var i=0;i<this.numChildren;i++){
if(is(this.getChildAt(i),charts.series.Element)){
e=as(this.getChildAt(i),charts.series.bars.StackCollection);
p=e.get_mid_point();
if(p.x==x){
var children=e.get_children();
for(var $1 in children){var child=children[$1];
tmp.push(child);}
}
}
}
return tmp;
},
];},[],["charts.BarBase","Properties","string.Utils","charts.series.bars.StackCollection","Array","charts.series.Element"], "0.8.0", "0.8.4"
);
// class charts.Base
joo.classLoader.prepare("package charts",
"public class Base extends flash.display.Sprite",6,function($$private){var is=joo.is,as=joo.as;return[function(){joo.classLoader.init(Number);},
"protected var",{key:null},
"protected var",{font_size:NaN},
"public var",{colour:NaN},
"public var",{line_width:NaN},
"public var",{circle_size:NaN},
"public var",{values:null},
"protected var",{axis:NaN},
"public function Base",function()
{this.super$6();},
"public function get_colour",function(){
return this.colour;
},
"public function get_keys",function(){
var tmp=[];
if((this.font_size>0)&&(this.key!=''))
tmp.push({'text':this.key,'font-size':this.font_size,'colour':this.get_colour()});
return tmp;
},
"protected function which_axis_am_i_attached_to",function(data,i){
if(data['show_y2']!=undefined)
if(data['show_y2']!='false')
if(data['y2_lines']!=undefined)
{
var tmp=data.y2_lines.split(",");
var pos=tmp.indexOf(i.toString());
if(pos==-1)
return 1;
else
return 2;
}
return 1;
},
"public function get_max_x",function(){
var max=Number.MIN_VALUE;
for(var i=0;i<this.numChildren;i++){
if(is(this.getChildAt(i),charts.series.Element)){
var e=as(this.getChildAt(i),charts.series.Element);
max=Math.max(max,e.get_x());
}
}
return max;
},
"public function get_min_x",function(){
var min=Number.MAX_VALUE;
for(var i=0;i<this.numChildren;i++){
if(is(this.getChildAt(i),charts.series.Element)){
var e=as(this.getChildAt(i),charts.series.Element);
min=Math.min(min,e.get_x());
}
}
return min;
},
"public function resize",function(sc){},
"public function closest",function(x,y){
var shortest=Number.MAX_VALUE;
var closest=null;
var dx;
for(var i=0;i<this.numChildren;i++){
if(is(this.getChildAt(i),charts.series.Element)){
var e=as(this.getChildAt(i),charts.series.Element);
e.set_tip(false);
dx=Math.abs(x-e.x);
if(dx<shortest){
shortest=dx;
closest=e;
}
}
}
var dy=0;
if(closest)
dy=Math.abs(y-closest.y);
return{element:closest,distance_x:shortest,distance_y:dy};
},
"public function closest_2",function(x,y){
var x=this.closest_x$6(x);
var tmp=this.get_all_at_this_x_pos(x);
var closest=this.get_closest_y$6(tmp,y);
var dy=Math.abs(y-closest.y);
return closest;
},
"private function closest_x",function(x){
var closest=Number.MAX_VALUE;
var p;
var x_pos;
var dx;
for(var i=0;i<this.numChildren;i++){
if(is(this.getChildAt(i),charts.series.Element)){
var e=as(this.getChildAt(i),charts.series.Element);
p=e.get_mid_point();
dx=Math.abs(x-p.x);
if(dx<closest){
closest=dx;
x_pos=p.x;
}
}
}
return x_pos;
},
"protected function get_all_at_this_x_pos",function(x){
var tmp=new Array();
var p;
var e;
for(var i=0;i<this.numChildren;i++){
if(is(this.getChildAt(i),charts.series.Element)){
e=as(this.getChildAt(i),charts.series.Element);
p=e.get_mid_point();
if(p.x==x)
tmp.push(e);
}
}
return tmp;
},
"private function get_closest_y",function(elements,y){
var y_min=Number.MAX_VALUE;
var dy;
var closest=new Array();
var p;
var e;
for(var $1 in elements){e=elements[$1];
p=e.get_mid_point();
dy=Math.abs(y-p.y);
y_min=Math.min(dy,y_min);
}
for(var $2 in elements){e=elements[$2];
p=e.get_mid_point();
dy=Math.abs(y-p.y);
if(dy==y_min)
closest.push(e);
}
return closest;
},
"public function mouse_proximity",function(x,y){
var closest=Number.MAX_VALUE;
var p;
var i;
var e;
var mouse=new flash.geom.Point(x,y);
for(i=0;i<this.numChildren;i++){
if(is(this.getChildAt(i),charts.series.Element)){
e=as(this.getChildAt(i),charts.series.Element);
closest=Math.min(flash.geom.Point.distance(e.get_mid_point(),mouse),closest);
}
}
var close=[];
for(i=0;i<this.numChildren;i++){
if(is(this.getChildAt(i),charts.series.Element)){
e=as(this.getChildAt(i),charts.series.Element);
if(flash.geom.Point.distance(e.get_mid_point(),mouse)==closest)
close.push(e);
}
}
return close;
},
"public function mouse_out",function(){
for(var i=0;i<this.numChildren;i++){
if(is(this.getChildAt(i),charts.series.Element)){
var e=as(this.getChildAt(i),charts.series.Element);
e.set_tip(false);
}
}
},
"protected function get_element",function(index,value){
return null;
},
"public function add_values",function(){
var index=0;
for(var $1 in this.values)
{var val=this.values[$1];
var tmp;
if(val!=null)
{
tmp=this.get_element(index,val);
if(tmp.line_mask!=null)
this.addChild(tmp.line_mask);
this.addChild(tmp);
}
index++;
}
},
"public function tooltip_replace_labels",function(labels){
for(var i=0;i<this.numChildren;i++){
if(is(this.getChildAt(i),charts.series.Element)){
var e=as(this.getChildAt(i),charts.series.Element);
e.tooltip_replace_labels(labels);
}
}
},
"public function die",function(){
for(var i=0;i<this.numChildren;i++)
if(is(this.getChildAt(i),charts.series.Element)){
var e=as(this.getChildAt(i),charts.series.Element);
e.die();
}
while(this.numChildren>0)
this.removeChildAt(0);
},
];},[],["flash.display.Sprite","Number","charts.series.Element","Math","Array","flash.geom.Point"], "0.8.0", "0.8.4"
);
// class charts.Candle
joo.classLoader.prepare("package charts",
"public class Candle extends charts.BarBase",8,function($$private){;return[
"private var",{negative_colour:NaN},
"public function Candle",function(json,group){
this.super$8(json,group);
tr.aces('---');
tr.ace_json(json);
tr.aces('neg',this.props.has('negative-colour'),this.props.get_colour('negative-colour'));
},
"protected override function get_element",function(index,value){
var default_style=this.get_element_helper_prop(value);
if(this.props.has('negative-colour'))
default_style.set('negative-colour',this.props.get('negative-colour'));
return new charts.series.bars.ECandle(index,default_style,this.group);
},
];},[],["charts.BarBase","tr","charts.series.bars.ECandle"], "0.8.0", "0.8.4"
);
// class charts.Elements.PointBarFade
joo.classLoader.prepare("package charts.Elements",
"public class PointBarFade extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[

"public function PointBarFade",function(index,value,colour,group)
{
var p=new Properties(value);
this.super$8(index,p,group);
},
"public override function resize",function(sc){
var h=this.resize_helper(as(sc,ScreenCoords));
this.graphics.clear();
this.graphics.beginFill(this.colour,1.0);
this.graphics.moveTo(0,0);
this.graphics.lineTo(h.width,0);
this.graphics.lineTo(h.width,h.height);
this.graphics.lineTo(0,h.height);
this.graphics.lineTo(0,0);
this.graphics.endFill();
},
];},[],["charts.series.bars.Base","Properties","ScreenCoords"], "0.8.0", "0.8.4"
);
// class charts.Factory
joo.classLoader.prepare("package charts",
"public class Factory",1,function($$private){var as=joo.as;return[

"private var",{attach_right:null},
"public static function MakeChart",function(json)
{
var collection=new charts.ObjectCollection();
var bar_group=0;
var name='';
var c=1;
var elements=as(json['elements'],Array);
for(var i=0;i<elements.length;i++)
{
switch(elements[i]['type']){
case'bar':
collection.add(new charts.Bar(elements[i],bar_group));
bar_group++;
break;
case'line':
collection.add(new charts.Line(elements[i]));
break;
case'area':
collection.add(new charts.Area(elements[i]));
break;
case'pie':
collection.add(new charts.Pie(elements[i]));
break;
case'hbar':
collection.add(new charts.HBar(elements[i]));
bar_group++;
break;
case'bar_stack':
collection.add(new charts.BarStack(elements[i],c,bar_group));
bar_group++;
break;
case'scatter':
collection.add(new charts.Scatter(elements[i]));
break;
case'scatter_line':
collection.add(new charts.ScatterLine(elements[i]));
break;
case'bar_sketch':
collection.add(new charts.BarSketch(elements[i],bar_group));
bar_group++;
break;
case'bar_glass':
collection.add(new charts.BarGlass(elements[i],bar_group));
bar_group++;
break;
case'bar_cylinder':
collection.add(new charts.BarCylinder(elements[i],bar_group));
bar_group++;
break;
case'bar_cylinder_outline':
collection.add(new charts.BarCylinderOutline(elements[i],bar_group));
bar_group++;
break;
case'bar_dome':
collection.add(new charts.BarDome(elements[i],bar_group));
bar_group++;
break;
case'bar_round':
collection.add(new charts.BarRound(elements[i],bar_group));
bar_group++;
break;
case'bar_round_glass':
collection.add(new charts.BarRoundGlass(elements[i],bar_group));
bar_group++;
break;
case'bar_round3d':
collection.add(new charts.BarRound3D(elements[i],bar_group));
bar_group++;
break;
case'bar_fade':
collection.add(new charts.BarFade(elements[i],bar_group));
bar_group++;
break;
case'bar_3d':
collection.add(new charts.Bar3D(elements[i],bar_group));
bar_group++;
break;
case'bar_filled':
collection.add(new charts.BarOutline(elements[i],bar_group));
bar_group++;
break;
case'bar_plastic':
collection.add(new charts.BarPlastic(elements[i],bar_group));
bar_group++;
break;
case'bar_plastic_flat':
collection.add(new charts.BarPlasticFlat(elements[i],bar_group));
bar_group++;
break;
case'shape':
collection.add(new charts.Shape(elements[i]));
break;
case'candle':
collection.add(new charts.Candle(elements[i],bar_group));
bar_group++;
break;
case'tags':
collection.add(new charts.Tags(elements[i]));
break;
case'arrow':
collection.add(new charts.Arrow(elements[i]));
break;
}
}
var y2=false;
var y2lines;
collection.groups=bar_group;
return collection;
},
];},["MakeChart"],["charts.ObjectCollection","Array","charts.Bar","charts.Line","charts.Area","charts.Pie","charts.HBar","charts.BarStack","charts.Scatter","charts.ScatterLine","charts.BarSketch","charts.BarGlass","charts.BarCylinder","charts.BarCylinderOutline","charts.BarDome","charts.BarRound","charts.BarRoundGlass","charts.BarRound3D","charts.BarFade","charts.Bar3D","charts.BarOutline","charts.BarPlastic","charts.BarPlasticFlat","charts.Shape","charts.Candle","charts.Tags","charts.Arrow"], "0.8.0", "0.8.4"
);
// class charts.HBar
joo.classLoader.prepare("package charts",
"public class HBar extends charts.Base",7,function($$private){var is=joo.is,as=joo.as;return[
"protected var",{group:NaN},
"protected var",{style:null},
"public function HBar",function(json){this.super$7();
this.style={
values:[],
colour:'#3030d0',
text:'',
'font-size':12,
tip:'#val#'
};
object_helper.merge_2(json,this.style);
this.colour=string.Utils.get_colour(this.style.colour);
this.key=json.text;
this.font_size=json['font-size'];
this.group=0;
this.values=json['values'];
this.style['on-click']=json['on-click'];
this.add_values();
},
"protected override function get_element",function(index,value){
var default_style={
colour:this.style.colour,
tip:this.style.tip,
'on-click':this.style['on-click']
};
if(is(value,Number))
default_style.top=value;
else
object_helper.merge_2(value,default_style);
if(is(default_style.colour,String))
default_style.colour=string.Utils.get_colour(default_style.colour);
return new charts.series.bars.Horizontal(index,default_style,this.group);
},
"public override function resize",function(sc){
for(var i=0;i<this.numChildren;i++)
{
var p=as(this.getChildAt(i),charts.series.bars.Horizontal);
p.resize(sc);
}
},
"public override function get_max_x",function(){
var x=0;
for(var i=0;i<this.numChildren;i++)
if(is(this.getChildAt(i),charts.series.bars.Horizontal)){
var h=as(this.getChildAt(i),charts.series.bars.Horizontal);
x=Math.max(x,h.get_max_x());
}
return x;
},
"public override function get_min_x",function(){
return 0;
},
];},[],["charts.Base","object_helper","string.Utils","Number","String","charts.series.bars.Horizontal","Math"], "0.8.0", "0.8.4"
);
// class charts.Line
joo.classLoader.prepare("package charts",
"public class Line extends charts.Base",7,function($$private){var is=joo.is,as=joo.as,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.display.BlendMode);},

"protected var",{props:null},
"private var",{dot_style:null},
"private var",{on_show:null},
"private var",{line_style:null},
"private var",{on_show_timer:null},
"private var",{on_show_start:false},
"public function Line",function(json){this.super$7();
var root=new Properties({
values:[],
width:2,
colour:'#3030d0',
text:'',
'font-size':12,
tip:'#val#',
loop:false,
axis:'left'
});
this.props=new Properties(json,root);
this.line_style$7=new charts.LineStyle(json['line-style']);
this.dot_style$7=new charts.series.dots.DefaultDotProperties(json['dot-style'],this.props.get('colour'),this.props.get('axis'));
var on_show_root=new Properties({
type:"none",
cascade:0.5,
delay:0
});
this.on_show$7=new Properties(json['on-show'],on_show_root);
this.on_show_start$7=true;
this.key=this.props.get('text');
this.font_size=this.props.get('font-size');
this.values=this.props.get('values');
this.add_values();
this.blendMode=flash.display.BlendMode.LAYER;
},
"protected override function get_element",function(index,value){
if(is(value,Number))
value={value:value};
var tmp=new Properties(value,this.dot_style$7);
tmp.set('tip',tmp.get('tip').replace('#key#',this.key));
tmp.set('on-show',this.on_show$7);
return charts.series.dots.dot_factory.make(index,tmp);
},
"public override function resize",function(sc){
this.x=this.y=0;
this.move_dots(sc);
if(this.on_show_start$7)
this.start_on_show_timer$7();
else
this.draw();
},
"private function start_on_show_timer",function(){
this.on_show_start$7=false;
this.on_show_timer$7=new flash.utils.Timer(1000/60);
this.on_show_timer$7.addEventListener("timer",$$bound(this,"animationManager"));
this.on_show_timer$7.start();
},
"protected function animationManager",function(eventArgs){
this.draw();
if(!this.still_animating$7()){
tr.ace('Line.as : on show animation stop');
this.on_show_timer$7.stop();
}
},
"private function still_animating",function(){
var i;
var tmp;
for(i=0;i<this.numChildren;i++){
tmp=as(this.getChildAt(i),flash.display.Sprite);
if(is(tmp,charts.series.dots.PointDotBase))
{
var e=as(tmp,charts.series.dots.PointDotBase);
if(e.is_tweening())
return true;
}
}
return false;
},
"protected function draw",function(){
this.graphics.clear();
this.draw_line();
},
"protected function draw_line",function(){
this.graphics.lineStyle(this.props.get_colour('width'),this.props.get_colour('colour'));
if(this.line_style$7.style!='solid')
this.dash_line();
else
this.solid_line();
},
"public function move_dots",function(sc){
var i;
var tmp;
for(i=0;i<this.numChildren;i++){
tmp=as(this.getChildAt(i),flash.display.Sprite);
if(is(tmp,charts.series.Element))
{
var e=as(tmp,charts.series.Element);
e.resize(sc);
}
}
},
"public function solid_line",function(){
var first=true;
var i;
var tmp;
var x;
var y;
for(i=0;i<this.numChildren;i++){
tmp=as(this.getChildAt(i),flash.display.Sprite);
if(is(tmp,charts.series.Element))
{
var e=as(tmp,charts.series.Element);
if(first)
{
this.graphics.moveTo(e.x,e.y);
x=e.x;
y=e.y;
first=false;
}
else
this.graphics.lineTo(e.x,e.y);
}
}
if(this.props.get('loop')){
this.graphics.lineTo(x,y);
}
},
"public function dash_line",function(){
var first=true;
var prev_x=0;
var prev_y=0;
var on_len_left=0;
var off_len_left=0;
var on_len=this.line_style$7.on;
var off_len=this.line_style$7.off;
var now_on=true;
for(var i=0;i<this.numChildren;i++){
var tmp=as(this.getChildAt(i),flash.display.Sprite);
if(is(tmp,charts.series.Element))
{
var e=as(tmp,charts.series.Element);
if(first)
{
this.graphics.moveTo(e.x,e.y);
on_len_left=on_len;
off_len_left=off_len;
now_on=true;
first=false;
prev_x=e.x;
prev_y=e.y;
var x_tmp_1=prev_x;
var x_tmp_2;
var y_tmp_1=prev_y;
var y_tmp_2;
}
else{
var part_len=Math.sqrt((e.x-prev_x)*(e.x-prev_x)+(e.y-prev_y)*(e.y-prev_y));
var sinus=((e.y-prev_y)/part_len);
var cosinus=((e.x-prev_x)/part_len);
var part_len_left=part_len;
var inside_part=true;
while(inside_part){
if(now_on){
if(on_len_left<part_len_left){
x_tmp_2=x_tmp_1+on_len_left*cosinus;
y_tmp_2=y_tmp_1+on_len_left*sinus;
x_tmp_1=x_tmp_2;
y_tmp_1=y_tmp_2;
part_len_left=part_len_left-on_len_left;
now_on=false;
off_len_left=off_len;
}else{
x_tmp_2=e.x;
y_tmp_2=e.y;
x_tmp_1=x_tmp_2;
y_tmp_1=y_tmp_2;
on_len_left=on_len_left-part_len_left;
inside_part=false;
}
this.graphics.lineTo(x_tmp_2,y_tmp_2);
}else{
if(off_len_left<part_len_left){
x_tmp_2=x_tmp_1+off_len_left*cosinus;
y_tmp_2=y_tmp_1+off_len_left*sinus;
x_tmp_1=x_tmp_2;
y_tmp_1=y_tmp_2;
part_len_left=part_len_left-off_len_left;
now_on=true;
on_len_left=on_len;
}else{
x_tmp_2=e.x;
y_tmp_2=e.y;
x_tmp_1=x_tmp_2;
y_tmp_1=y_tmp_2;
off_len_left=off_len_left-part_len_left;
inside_part=false;
}
this.graphics.moveTo(x_tmp_2,y_tmp_2);
}
}
}
prev_x=e.x;
prev_y=e.y;
}
}
},
"public override function get_colour",function(){
return this.props.get_colour('colour');
},
];},[],["charts.Base","Properties","charts.LineStyle","charts.series.dots.DefaultDotProperties","flash.display.BlendMode","Number","charts.series.dots.dot_factory","flash.utils.Timer","tr","flash.display.Sprite","charts.series.dots.PointDotBase","charts.series.Element","Math"], "0.8.0", "0.8.4"
);
// class charts.LineBase
joo.classLoader.prepare("package charts",
"public class LineBase extends charts.Base",7,function($$private){var is=joo.is,as=joo.as;return[

"protected var",{style:null},
"public function LineBase",function(){this.super$7();},
"protected override function get_element",function(index,value){
var tmp;
if(is(value,Number))
tmp=new Properties({value:value},this.style['--dot-style']);
else
tmp=new Properties(value,this.style['--dot-style']);
return charts.series.dots.dot_factory.make(index,tmp);
},
"public override function resize",function(sc){
this.x=this.y=0;
this.graphics.clear();
this.graphics.lineStyle(this.style.width,this.style.colour);
if(this.style['line-style'].style!='solid')
this.dash_line(sc);
else
this.solid_line(sc);
},
"public function solid_line",function(sc){
var first=true;
var i;
var tmp;
var x;
var y;
for(i=0;i<this.numChildren;i++){
tmp=as(this.getChildAt(i),flash.display.Sprite);
if(is(tmp,charts.series.Element))
{
var e=as(tmp,charts.series.Element);
e.resize(sc);
if(first)
{
this.graphics.moveTo(e.x,e.y);
x=e.x;
y=e.y;
first=false;
}
else
this.graphics.lineTo(e.x,e.y);
}
}
if(this.style.loop){
this.graphics.lineTo(x,y);
}
},
"public function dash_line",function(sc){
var first=true;
var prev_x=0;
var prev_y=0;
var on_len_left=0;
var off_len_left=0;
var on_len=this.style['line-style'].on;
var off_len=this.style['line-style'].off;
var now_on=true;
for(var i=0;i<this.numChildren;i++){
var tmp=as(this.getChildAt(i),flash.display.Sprite);
if(is(tmp,charts.series.Element))
{
var e=as(tmp,charts.series.Element);
e.resize(sc);
if(first)
{
this.graphics.moveTo(e.x,e.y);
on_len_left=on_len;
off_len_left=off_len;
now_on=true;
first=false;
prev_x=e.x;
prev_y=e.y;
var x_tmp_1=prev_x;
var x_tmp_2;
var y_tmp_1=prev_y;
var y_tmp_2;
}
else{
var part_len=Math.sqrt((e.x-prev_x)*(e.x-prev_x)+(e.y-prev_y)*(e.y-prev_y));
var sinus=((e.y-prev_y)/part_len);
var cosinus=((e.x-prev_x)/part_len);
var part_len_left=part_len;
var inside_part=true;
while(inside_part){
if(now_on){
if(on_len_left<part_len_left){
x_tmp_2=x_tmp_1+on_len_left*cosinus;
y_tmp_2=y_tmp_1+on_len_left*sinus;
x_tmp_1=x_tmp_2;
y_tmp_1=y_tmp_2;
part_len_left=part_len_left-on_len_left;
now_on=false;
off_len_left=off_len;
}else{
x_tmp_2=e.x;
y_tmp_2=e.y;
x_tmp_1=x_tmp_2;
y_tmp_1=y_tmp_2;
on_len_left=on_len_left-part_len_left;
inside_part=false;
}
this.graphics.lineTo(x_tmp_2,y_tmp_2);
}else{
if(off_len_left<part_len_left){
x_tmp_2=x_tmp_1+off_len_left*cosinus;
y_tmp_2=y_tmp_1+off_len_left*sinus;
x_tmp_1=x_tmp_2;
y_tmp_1=y_tmp_2;
part_len_left=part_len_left-off_len_left;
now_on=true;
on_len_left=on_len;
}else{
x_tmp_2=e.x;
y_tmp_2=e.y;
x_tmp_1=x_tmp_2;
y_tmp_1=y_tmp_2;
off_len_left=off_len_left-part_len_left;
inside_part=false;
}
this.graphics.moveTo(x_tmp_2,y_tmp_2);
}
}
}
prev_x=e.x;
prev_y=e.y;
}
}
},
"protected function merge_us_with_value_object",function(value){
var default_style={
'dot-size':this.style['dot-size'],
colour:this.style.colour,
'halo-size':this.style['halo-size'],
tip:this.style.tip,
'on-click':this.style['on-click'],
'axis':this.style.axis
};
if(is(value,Number))
default_style.value=value;
else
object_helper.merge_2(value,default_style);
if(is(default_style.colour,String))
default_style.colour=string.Utils.get_colour(default_style.colour);
default_style.tip=default_style.tip.replace('#key#',this.style.text);
return default_style;
},
"public override function get_colour",function(){
return this.style.colour;
},
];},[],["charts.Base","Number","Properties","charts.series.dots.dot_factory","flash.display.Sprite","charts.series.Element","Math","object_helper","String","string.Utils"], "0.8.0", "0.8.4"
);
// class charts.LineDot
joo.classLoader.prepare("package charts",
"public class LineDot extends charts.LineBase",8,function($$private){;return[function(){joo.classLoader.init(flash.display.BlendMode);},

"public function LineDot",function(json)
{this.super$8();
this.style={
values:[],
width:2,
colour:'#3030d0',
text:'',
'dot-size':5,
'halo-size':2,
'font-size':12,
tip:'#val#',
'line-style':new charts.LineStyle(json['line-style'])
};
object_helper.merge_2(json,this.style);
this.style.colour=string.Utils.get_colour(this.style.colour);
this.key=this.style.text;
this.font_size=this.style['font-size'];
this.values=this.style['values'];
this.add_values();
this.blendMode=flash.display.BlendMode.LAYER;
},
];},[],["charts.LineBase","charts.LineStyle","object_helper","string.Utils","flash.display.BlendMode"], "0.8.0", "0.8.4"
);
// class charts.LineHollow
joo.classLoader.prepare("package charts",
"public class LineHollow extends charts.LineBase",8,function($$private){;return[function(){joo.classLoader.init(flash.display.BlendMode);},

"public function LineHollow",function(json)
{this.super$8();
this.blendMode=flash.display.BlendMode.LAYER;
this.style={
values:[],
width:2,
colour:'#80a033',
text:'',
'font-size':10,
'dot-size':6,
'halo-size':2,
tip:'#val#',
'line-style':new charts.LineStyle(json['line-style']),
'axis':'left'
};
this.style=object_helper.merge(json,this.style);
this.style.colour=string.Utils.get_colour(this.style.colour);
this.values=this.style.values;
this.key=this.style.text;
this.font_size=this.style['font-size'];
this.add_values();
},
];},[],["charts.LineBase","flash.display.BlendMode","charts.LineStyle","object_helper","string.Utils"], "0.8.0", "0.8.4"
);
// class charts.LineStyle
joo.classLoader.prepare("package charts",
"public class LineStyle extends Object",1,function($$private){;return[

"public var",{style:null},
"public var",{on:NaN},
"public var",{off:NaN},
"public function LineStyle",function(json){
this.style='solid';
this.on=1;
this.off=5;
object_helper.merge_2(json,this);
},
];},[],["Object","object_helper"], "0.8.0", "0.8.4"
);
// class charts.ObjectCollection
joo.classLoader.prepare("package charts",
"public class ObjectCollection",1,function($$private){var is=joo.is;return[function(){joo.classLoader.init(Number);},

"public var",{sets:null},
"public var",{groups:NaN},
"public function ObjectCollection",function(){
this.sets=new Array();
},
"public function add",function(set){
this.sets.push(set);
},
"public function get_max_x",function(){
var max=Number.MIN_VALUE;
for(var $1 in this.sets){var o=this.sets[$1];
max=Math.max(max,o.get_max_x());}
return max;
},
"public function get_min_x",function(){
var min=Number.MAX_VALUE;
for(var $1 in this.sets){var o=this.sets[$1];
min=Math.min(min,o.get_min_x());}
return min;
},
"public function resize",function(sc){
for(var $1 in this.sets){var o=this.sets[$1];
o.resize(sc);}
},
"public function tooltip_replace_labels",function(labels){
for(var $1 in this.sets){var o=this.sets[$1];
o.tooltip_replace_labels(labels);}
},
"public function mouse_out",function(){
for(var $1 in this.sets){var s=this.sets[$1];
s.mouse_out();}
},
"private function closest",function(x,y){
var o;
var s;
var closest=new Array();
for(var $1 in this.sets){s=this.sets[$1];
closest.push(s.closest(x,y));}
var min=Number.MAX_VALUE;
for(var $2 in closest){o=closest[$2];
min=Math.min(min,o.distance_x);}
var xx={element:null,distance_x:Number.MAX_VALUE,distance_y:Number.MAX_VALUE};
for(var $3 in closest){o=closest[$3];
if(o.distance_x==min)
{
if(o.distance_y<xx.distance_y)
xx=o;
}
}
if(xx.element)
xx.element.set_tip(true);
return xx.element;
},
"public function mouse_move",function(x,y){
var e=null;
if(!e)
{
e=this.closest$1(x,y);
}
return e;
},
"public function closest_2",function(x,y){
var e;
var s;
var p;
var closest=new Array();
for(var $2 in this.sets){s=this.sets[$2];
var tmp=s.closest_2(x,y);
for(var $1 in tmp){e=tmp[$1];
closest.push(e);}
}
var min_x=Number.MAX_VALUE;
for(var $3 in closest){e=closest[$3];
p=e.get_mid_point();
min_x=Math.min(min_x,Math.abs(x-p.x));
}
var good_x=new Array();
for(var $4 in closest){e=closest[$4];
p=e.get_mid_point();
if(Math.abs(x-p.x)==min_x)
good_x.push(e);
}
var min_y=Number.MAX_VALUE;
for(var $5 in good_x){e=good_x[$5];
p=e.get_mid_point();
min_y=Math.min(min_y,Math.abs(y-p.y));
}
var good_x_and_y=new Array();
for(var $6 in good_x){e=good_x[$6];
p=e.get_mid_point();
if(Math.abs(y-p.y)==min_y)
good_x_and_y.push(e);
}
return good_x_and_y;
},
"public function mouse_move_proximity",function(x,y){
var e;
var s;
var p;
var closest=new Array();
for(var $2 in this.sets){s=this.sets[$2];
var tmp=s.mouse_proximity(x,y);
for(var $1 in tmp){e=tmp[$1];
closest.push(e);}
}
var min_dist=Number.MAX_VALUE;
var mouse=new flash.geom.Point(x,y);
for(var $3 in closest){e=closest[$3];
min_dist=Math.min(flash.geom.Point.distance(e.get_mid_point(),mouse),min_dist);
}
var close=[];
for(var $4 in closest){e=closest[$4];
if(flash.geom.Point.distance(e.get_mid_point(),mouse)==min_dist)
close.push(e);
}
return close;
},
"public function has_pie",function(){
if(this.sets.length>0&&(is(this.sets[0],charts.Pie)))
return true;
else
return false;
},
"public function die",function(){
for(var $1 in this.sets){var o=this.sets[$1];
o.die();}
},
];},[],["Array","Number","Math","flash.geom.Point","charts.Pie"], "0.8.0", "0.8.4"
);
// class charts.Pie
joo.classLoader.prepare("package charts",
"public class Pie extends charts.Base",7,function($$private){var is=joo.is,as=joo.as,trace=joo.trace;return[

"private var",{labels:null},
"private var",{links:null},
"private var",{colours:null},
"private var",{gradientFill:'true'},
"private var",{border_width:1},
"private var",{label_line:NaN},
"private var",{easing:null},
"public var",{style:null},
"public var",{total_value:0},
"private var",{props:null},
"public function Pie",function(json)
{this.super$7();
this.labels$7=new Array();
this.links$7=new Array();
this.colours$7=new Array();
this.style={
colours:["#900000","#009000"]
};
object_helper.merge_2(json,this.style);
for(var $1 in this.style.colours){var colour=this.style.colours[$1];
this.colours$7.push(string.Utils.get_colour(colour));}
this.props$7=new charts.series.pies.DefaultPieProperties(json);
this.label_line$7=10;
this.values=json.values;
this.add_values();
},
"public override function add_values",function(){
var g=global.Global.getInstance();
var total=0;
var slice_start=this.props$7.get('start-angle');
var i;
var val;
for(var $1 in this.values){val=this.values[$1];
if(is(val,Number))
total+=val;
else
total+=val.value;
}
this.total_value=total;
i=0;
for(var $2 in this.values){val=this.values[$2];
var value=is(val,Number)?as(val,Number):val.value;
var slice_angle=value*360/total;
if(slice_angle>=0)
{
var t=this.props$7.get('tip').replace('#total#',NumberUtils.formatNumber(this.total_value));
t=t.replace('#percent#',NumberUtils.formatNumber(value/this.total_value*100)+'%');
this.addChild(
this.add_slice$7(
i,
slice_start,
slice_angle,
val,
t,
this.colours$7[(i%this.colours$7.length)]
)
);
}
i++;
slice_start+=slice_angle;
}
},
"private function add_slice",function(index,start,angle,value,tip,colour){
var calculated_stuff=new Properties(
{
colour:colour,
tip:tip,
start:start,
angle:angle
},
this.props$7);
var tmp={};
if(is(value,Number))
tmp.value=value;
else
tmp=value;
var p=new Properties(tmp,calculated_stuff);
if(!p.has('label'))
p.set('label',p.get('value').toString());
return new charts.series.pies.PieSliceContainer(index,p);
},
"public override function closest",function(x,y){
return{Element:null,distance_x:0,distance_y:0};
},
"public override function resize",function(sc){
var radius=this.style.radius;
if(isNaN(radius)){
radius=(Math.min(sc.width,sc.height)/2.0);
var offsets={top:0,right:0,bottom:0,left:0};
trace("sc.width, sc.height, radius",sc.width,sc.height,radius);
var i;
var sliceContainer;
for(i=0;i<this.numChildren;i++){
sliceContainer=as(this.getChildAt(i),charts.series.pies.PieSliceContainer);
var pie_offsets=sliceContainer.get_radius_offsets();
for(var key in offsets){
if(pie_offsets[key]>offsets[key]){
offsets[key]=pie_offsets[key];
}
}
}
var vRadius=radius;
vRadius-=Math.max(offsets.top,offsets.bottom);
if((vRadius+offsets.left)>(sc.width/2))
{
vRadius=(sc.width/2)-offsets.left;
}
if((vRadius+offsets.right)>(sc.width/2))
{
vRadius=(sc.width/2)-offsets.right;
}
radius=Math.max(vRadius,10);
}
var rightTopTicAngle=720;
var rightTopTicIdx=-1;
var rightBottomTicAngle=-720;
var rightBottomTicIdx=-1;
var leftTopTicAngle=720;
var leftTopTicIdx=-1;
var leftBottomTicAngle=-720;
var leftBottomTicIdx=-1;
for(i=0;i<this.numChildren;i++)
{
sliceContainer=as(this.getChildAt(i),charts.series.pies.PieSliceContainer);
sliceContainer.pie_resize(sc,radius);
var ticAngle=sliceContainer.getTicAngle();
if(ticAngle>=270)
{
if((ticAngle<rightTopTicAngle)||(rightTopTicAngle<=90))
{
rightTopTicAngle=ticAngle;
rightTopTicIdx=i;
}
if((rightBottomTicAngle<0)||
((rightBottomTicAngle>90)&&(rightBottomTicAngle<ticAngle)))
{
rightBottomTicAngle=ticAngle;
rightBottomTicIdx=i;
}
}
else if(ticAngle<=90)
{
if((ticAngle>rightBottomTicAngle)||(rightBottomTicAngle>90))
{
rightBottomTicAngle=ticAngle;
rightBottomTicIdx=i;
}
if((rightTopTicAngle>360)||
((rightTopTicAngle<=90)&&(ticAngle<rightBottomTicAngle)))
{
rightTopTicAngle=ticAngle;
rightTopTicIdx=i;
}
}
else if(ticAngle<=180)
{
if((leftBottomTicAngle<0)||(ticAngle<leftBottomTicAngle))
{
leftBottomTicAngle=ticAngle;
leftBottomTicIdx=i;
}
if((leftTopTicAngle>360)||(leftTopTicAngle<ticAngle))
{
leftTopTicAngle=ticAngle;
leftTopTicIdx=i;
}
}
else
{
if((leftTopTicAngle>360)||(ticAngle>leftTopTicAngle))
{
leftTopTicAngle=ticAngle;
leftTopTicIdx=i;
}
if((leftBottomTicAngle<0)||(leftBottomTicAngle>ticAngle))
{
leftBottomTicAngle=ticAngle;
leftBottomTicIdx=i;
}
}
}
var childIdx=rightTopTicIdx;
var yVal=sc.top;
var bDone=false;
while((childIdx>=0)&&(!bDone))
{
sliceContainer=as(this.getChildAt(childIdx),charts.series.pies.PieSliceContainer);
ticAngle=sliceContainer.getTicAngle();
if((ticAngle>=270)||(ticAngle<=90))
{
yVal=sliceContainer.moveLabelDown(sc,yVal);
childIdx++;
if(childIdx>=this.numChildren)childIdx=0;
bDone=(childIdx==rightTopTicIdx);
}
else
{
bDone=true;
}
}
childIdx=rightBottomTicIdx;
yVal=sc.bottom;
bDone=false;
while((childIdx>=0)&&(!bDone))
{
sliceContainer=as(this.getChildAt(childIdx),charts.series.pies.PieSliceContainer);
ticAngle=sliceContainer.getTicAngle();
if((ticAngle>=270)||(ticAngle<=90))
{
yVal=sliceContainer.moveLabelUp(sc,yVal);
childIdx--;
if(childIdx<0)childIdx=this.numChildren-1;
bDone=(childIdx==rightBottomTicIdx);
}
else
{
bDone=true;
}
}
childIdx=leftBottomTicIdx;
yVal=sc.bottom;
bDone=false;
while((childIdx>=0)&&(!bDone))
{
sliceContainer=as(this.getChildAt(childIdx),charts.series.pies.PieSliceContainer);
ticAngle=sliceContainer.getTicAngle();
if((ticAngle>90)&&(ticAngle<270))
{
yVal=sliceContainer.moveLabelUp(sc,yVal);
childIdx++;
if(childIdx>=this.numChildren)childIdx=0;
bDone=(childIdx==leftBottomTicIdx);
}
else
{
bDone=true;
}
}
childIdx=leftTopTicIdx;
yVal=sc.top;
bDone=false;
while((childIdx>=0)&&(!bDone))
{
sliceContainer=as(this.getChildAt(childIdx),charts.series.pies.PieSliceContainer);
ticAngle=sliceContainer.getTicAngle();
if((ticAngle>90)&&(ticAngle<270))
{
yVal=sliceContainer.moveLabelDown(sc,yVal);
childIdx--;
if(childIdx<0)childIdx=this.numChildren-1;
bDone=(childIdx==leftTopTicIdx);
}
else
{
bDone=true;
}
}
},
"public override function toString",function(){
return"Pie with "+this.numChildren+" children";
},
];},[],["charts.Base","Array","object_helper","string.Utils","charts.series.pies.DefaultPieProperties","global.Global","Number","NumberUtils","Properties","charts.series.pies.PieSliceContainer","Math"], "0.8.0", "0.8.4"
);
// class charts.Scatter
joo.classLoader.prepare("package charts",
"public class Scatter extends charts.ScatterBase",8,function($$private){;return[

"public function Scatter",function(json)
{
this.super$8(json);
this.style={
values:[],
width:2,
colour:'#3030d0',
text:'',
'font-size':12,
tip:'[#x#,#y#] #size#',
axis:'left'
};
var tmp=json['dot-style'];
object_helper.merge_2(json,this.style);
this.default_style=new charts.series.dots.DefaultDotProperties(
json['dot-style'],this.style.colour,this.style.axis);
this.line_width=this.style.width;
this.colour=string.Utils.get_colour(this.style.colour);
this.key=this.style.text;
this.font_size=this.style['font-size'];
this.circle_size=this.style['dot-size'];
this.values=this.style.values;
this.add_values();
},
];},[],["charts.ScatterBase","object_helper","charts.series.dots.DefaultDotProperties","string.Utils"], "0.8.0", "0.8.4"
);
// class charts.ScatterBase
joo.classLoader.prepare("package charts",
"public class ScatterBase extends charts.Base",7,function($$private){var is=joo.is,as=joo.as;return[
"protected var",{props:null},
"protected var",{style:null},
"private var",{on_show:null},
"private var",{dot_style:null},
"protected var",{default_style:null},
"public function ScatterBase",function(json){this.super$7();
var root=new Properties({
colour:'#3030d0',
text:'',
'font-size':12,
tip:'#val#',
axis:'left'
});
this.props=new Properties(json,root);
this.dot_style$7=new charts.series.dots.DefaultDotProperties(json['dot-style'],this.props.get('colour'),this.props.get('axis'));
var on_show_root=new Properties({
type:"explode",
cascade:0,
delay:0.3
});
this.on_show$7=new Properties(json['on-show'],on_show_root);
},
"protected override function get_element",function(index,value){
var default_style={
width:this.style.width,
colour:this.style.colour,
tip:this.style.tip,
'dot-size':10
};
object_helper.merge_2(this.style['dot-style'],default_style);
object_helper.merge_2(value,default_style);
if(is(default_style.colour,String))
default_style.colour=string.Utils.get_colour(default_style.colour);
var tmp=new Properties(value,this.dot_style$7);
tmp.set('on-show',this.on_show$7);
return charts.series.dots.dot_factory.make(index,tmp);
},
"public override function resize",function(sc){
var tmp;
for(var i=0;i<this.numChildren;i++){
tmp=as(this.getChildAt(i),flash.display.Sprite);
if(is(tmp,charts.series.Element))
{
var e=as(tmp,charts.series.Element);
e.resize(sc);
}
}
},
];},[],["charts.Base","Properties","charts.series.dots.DefaultDotProperties","object_helper","String","string.Utils","charts.series.dots.dot_factory","flash.display.Sprite","charts.series.Element"], "0.8.0", "0.8.4"
);
// class charts.ScatterLine
joo.classLoader.prepare("package charts",
"public class ScatterLine extends charts.ScatterBase",8,function($$private){var as=joo.as,is=joo.is;return[function(){joo.classLoader.init(flash.display.BlendMode);},

"public var",{stepgraph:0},
"public static const",{STEP_HORIZONTAL:1},
"public static const",{STEP_VERTICAL:2},
"public function ScatterLine",function(json)
{
this.super$8(json);
this.blendMode=flash.display.BlendMode.LAYER;
this.style={
values:[],
width:2,
colour:'#3030d0',
text:'',
'font-size':12,
stepgraph:0,
axis:'left'
};
var tmp=json['dot-style'];
object_helper.merge_2(json,this.style);
this.default_style=new charts.series.dots.DefaultDotProperties(
json['dot-style'],this.style.colour,this.style.axis);
this.style.colour=string.Utils.get_colour(this.style.colour);
this.line_width=this.style.width;
this.colour=this.style.colour;
this.key=this.style.text;
this.font_size=this.style['font-size'];
switch(this.style['stepgraph']){
case'horizontal':
this.stepgraph=charts.ScatterLine.STEP_HORIZONTAL;
break;
case'vertical':
this.stepgraph=charts.ScatterLine.STEP_VERTICAL;
break;
}
this.values=this.style.values;
this.add_values();
},
"public override function resize",function(sc){
this.resize$8(sc);
this.graphics.clear();
this.graphics.lineStyle(this.style.width,this.style.colour);
this.solid_line(sc);
},
"public function solid_line",function(sc){
var first=true;
var last_x=0;
var last_y=0;
var areaClosed=true;
var isArea=false;
var areaBaseX=NaN;
var areaBaseY=NaN;
var areaColour=this.colour;
var areaAlpha=0.4;
var areaStyle=this.style['area-style'];
if(areaStyle!=null)
{
isArea=true;
if(areaStyle.x!=null)
{
areaBaseX=areaStyle.x;
}
if(areaStyle.y!=null)
{
areaBaseY=areaStyle.y;
}
if(areaStyle.colour!=null)
{
areaColour=string.Utils.get_colour(areaStyle.colour);
}
if(areaStyle.alpha!=null)
{
areaAlpha=areaStyle.alpha;
}
if(!isNaN(areaBaseX))
{
areaBaseX=sc.get_x_from_val(areaBaseX);
}
if(!isNaN(areaBaseY))
{
areaBaseY=sc.get_y_from_val(areaBaseY);
}
}
for(var i=0;i<this.numChildren;i++){
var tmp=as(this.getChildAt(i),flash.display.Sprite);
if(is(tmp,charts.series.Element))
{
var e=as(tmp,charts.series.Element);
e.resize(sc);
if(!e.visible)
{
if((isArea)&&(i>0))
{
areaX=isNaN(areaBaseX)?last_x:areaBaseX;
areaY=isNaN(areaBaseY)?last_y:areaBaseY;
this.graphics.lineStyle(0,areaColour,0);
this.graphics.lineTo(areaX,areaY);
this.graphics.endFill();
areaClosed=true;
}
first=true;
}
else if(first)
{
if(isArea)
{
var areaX=isNaN(areaBaseX)?e.x:areaBaseX;
var areaY=isNaN(areaBaseY)?e.y:areaBaseY;
this.graphics.beginFill(areaColour,areaAlpha);
this.graphics.lineStyle(0,areaColour,0);
this.graphics.moveTo(areaX,areaY);
this.graphics.lineTo(e.x,e.y);
areaClosed=false;
this.graphics.lineStyle(this.style.width,this.style.colour,1.0);
}
else
{
this.graphics.moveTo(e.x,e.y);
}
first=false;
}
else
{
if(this.stepgraph==charts.ScatterLine.STEP_HORIZONTAL)
this.graphics.lineTo(e.x,last_y);
else if(this.stepgraph==charts.ScatterLine.STEP_VERTICAL)
this.graphics.lineTo(last_x,e.y);
this.graphics.lineTo(e.x,e.y);
}
last_x=e.x;
last_y=e.y;
}
}
if(isArea&&!areaClosed)
{
areaX=isNaN(areaBaseX)?last_x:areaBaseX;
areaY=isNaN(areaBaseY)?last_y:areaBaseY;
this.graphics.lineStyle(0,areaColour,0);
this.graphics.lineTo(areaX,areaY);
this.graphics.endFill();
}
},
];},[],["charts.ScatterBase","flash.display.BlendMode","object_helper","charts.series.dots.DefaultDotProperties","string.Utils","flash.display.Sprite","charts.series.Element"], "0.8.0", "0.8.4"
);
// class charts.series.bars.Bar
joo.classLoader.prepare("package charts.series.bars",
"public class Bar extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[
"public function Bar",function(index,props,group){
this.super$8(index,props,group);
},
"public override function resize",function(sc){
var h=this.resize_helper(as(sc,ScreenCoords));
this.graphics.clear();
this.graphics.beginFill(this.colour,1.0);
this.graphics.moveTo(0,0);
this.graphics.lineTo(h.width,0);
this.graphics.lineTo(h.width,h.height);
this.graphics.lineTo(0,h.height);
this.graphics.lineTo(0,0);
this.graphics.endFill();
},
];},[],["charts.series.bars.Base","ScreenCoords"], "0.8.0", "0.8.4"
);
// class charts.series.bars.Bar3D
joo.classLoader.prepare("package charts.series.bars",
"public class Bar3D extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Math);},
"public function Bar3D",function(index,props,group){
this.super$8(index,props,group);
var dropShadow=new flash.filters.DropShadowFilter();
dropShadow.blurX=5;
dropShadow.blurY=5;
dropShadow.distance=3;
dropShadow.angle=45;
dropShadow.quality=2;
dropShadow.alpha=0.4;
this.filters=[dropShadow];
},
"public override function resize",function(sc){
var h=this.resize_helper(as(sc,ScreenCoords));
this.graphics.clear();
this.draw_top$8(h.width,h.height);
this.draw_front$8(h.width,h.height);
this.draw_side$8(h.width,h.height);
},
"private function draw_top",function(w,h){
this.graphics.lineStyle(0,0,0);
var lighter=charts.series.bars.Bar3D.Lighten(this.colour);
var colors=[this.colour,lighter];
var alphas=[1,1];
var ratios=[0,255];
var matrix=new flash.geom.Matrix();
matrix.createGradientBox(w+12,12,(270/180)*Math.PI);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
var y=0;
if(h<0)
y=h;
this.graphics.moveTo(0,y);
this.graphics.lineTo(w,y);
this.graphics.lineTo(w-12,y+12);
this.graphics.lineTo(-12,y+12);
this.graphics.endFill();
},
"private function draw_front",function(w,h){
var rad=7;
var lighter=charts.series.bars.Bar3D.Lighten(this.colour);
var colors=[lighter,this.colour];
var alphas=[1,1];
var ratios=[0,127];
var matrix=new flash.geom.Matrix();
matrix.createGradientBox(w-12,h+12,(90/180)*Math.PI);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
this.graphics.moveTo(-12,12);
this.graphics.lineTo(-12,h+12);
this.graphics.lineTo(w-12,h+12);
this.graphics.lineTo(w-12,12);
this.graphics.endFill();
},
"private function draw_side",function(w,h){
var rad=7;
var lighter=charts.series.bars.Bar3D.Lighten(this.colour);
var colors=[this.colour,lighter];
var alphas=[1,1];
var ratios=[0,255];
var matrix=new flash.geom.Matrix();
matrix.createGradientBox(w,h+12,(270/180)*Math.PI);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
this.graphics.lineStyle(0,0,0);
this.graphics.moveTo(w,0);
this.graphics.lineTo(w,h);
this.graphics.lineTo(w-12,h+12);
this.graphics.lineTo(w-12,12);
this.graphics.endFill();
},
"public static function Lighten",function(col){
var rgb=col;
var red=(rgb&16711680)>>16;
var green=(rgb&65280)>>8;
var blue=rgb&255;
var p=2;
red+=red/p;
if(red>255)
red=255;
green+=green/p;
if(green>255)
green=255;
blue+=blue/p;
if(blue>255)
blue=255;
return red<<16|green<<8|blue;
},
];},["Lighten"],["charts.series.bars.Base","flash.filters.DropShadowFilter","ScreenCoords","flash.geom.Matrix","Math"], "0.8.0", "0.8.4"
);
// class charts.series.bars.Base
joo.classLoader.prepare("package charts.series.bars",
"public class Base extends charts.series.Element",7,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(caurina.transitions.Equations,Number,flash.events.MouseEvent);},

"protected var",{tip_pos:null},
"protected var",{colour:NaN},
"protected var",{group:NaN},
"protected var",{top:NaN},
"protected var",{bottom:NaN},
"protected var",{mouse_out_alpha:NaN},
"private var",{on_show_animate:false},
"protected var",{on_show:null},
"public function Base",function(index,props,group)
{
this.super$7();
this.index=index;
this.parse_value(props);
this.colour=props.get_colour('colour');
this.tooltip=this.replace_magic_values(props.get('tip'));
this.group=group;
this.visible=true;
this.on_show_animate$7=true;
this.on_show=props.get('on-show');
this.mouse_out_alpha=props.get('alpha');
this.alpha=this.mouse_out_alpha;
this.addEventListener(flash.events.MouseEvent.MOUSE_OVER,$$bound(this,"mouseOver"));
this.addEventListener(flash.events.MouseEvent.MOUSE_OUT,$$bound(this,"mouseOut"));
if(props.has('on-click'))
if(props.get('on-click')!=false)
this.set_on_click(props.get('on-click'));
if(props.has('axis'))
if(props.get('axis')=='right')
this.right_axis=true;
},
"protected function parse_value",function(props){
if(!props.has('bottom')){
props.set('bottom',Number.MIN_VALUE);
}
this.top=props.get('top');
this.bottom=props.get('bottom');
},
"protected function replace_magic_values",function(t){
t=t.replace('#top#',NumberUtils.formatNumber(this.top));
t=t.replace('#bottom#',NumberUtils.formatNumber(this.bottom));
t=t.replace('#val#',NumberUtils.formatNumber(this.top-this.bottom));
return t;
},
"public override function get_mid_point",function(){
return new flash.geom.Point(this.x+(this.width/2),this.y);
},
"public override function mouseOver",function(event){
this.is_tip=true;
caurina.transitions.Tweener.addTween(this,{alpha:1,time:0.6,transition:caurina.transitions.Equations.easeOutCirc});
},
"public override function mouseOut",function(event){
this.is_tip=false;
caurina.transitions.Tweener.addTween(this,{alpha:this.mouse_out_alpha,time:0.8,transition:caurina.transitions.Equations.easeOutElastic});
},
"public override function resize",function(sc){},
"public override function get_tip_pos",function(){
return{x:this.tip_pos.x,y:this.tip_pos.y};
},
"protected function resize_helper",function(sc){
var tmp=sc.get_bar_coords(this.index,this.group);
var bar_top=sc.get_y_from_val(this.top,this.right_axis);
var bar_bottom;
if(this.bottom==Number.MIN_VALUE)
bar_bottom=sc.get_y_bottom(this.right_axis);
else
bar_bottom=sc.get_y_from_val(this.bottom,this.right_axis);
var top;
var height;
var upside_down=false;
if(bar_bottom<bar_top){
top=bar_bottom;
upside_down=true;
}
else
{
top=bar_top;
}
height=Math.abs(bar_bottom-bar_top);
this.tip_pos=new flash.geom.Point(tmp.x+(tmp.width/2),top);
if(this.on_show_animate$7)
this.first_show(tmp.x,top,tmp.width,height);
else{
this.y=top;
this.x=tmp.x;
}
return{width:tmp.width,top:top,height:height,upside_down:upside_down};
},
"protected function first_show",function(x,y,width,height){
this.on_show_animate$7=false;
caurina.transitions.Tweener.removeTweens(this);
var d=x/this.stage.stageWidth;
d*=this.on_show.get('cascade');
d+=this.on_show.get('delay');
switch(this.on_show.get('type')){
case'pop-up':
this.x=x;
this.y=this.stage.stageHeight+this.height+3;
caurina.transitions.Tweener.addTween(this,{y:y,time:1,delay:d,transition:caurina.transitions.Equations.easeOutBounce});
break;
case'drop':
this.x=x;
this.y=-height-10;
caurina.transitions.Tweener.addTween(this,{y:y,time:1,delay:d,transition:caurina.transitions.Equations.easeOutBounce});
break;
case'fade-in':
this.x=x;
this.y=y;
this.alpha=0;
caurina.transitions.Tweener.addTween(this,{alpha:this.mouse_out_alpha,time:1.2,delay:d,transition:caurina.transitions.Equations.easeOutQuad});
break;
case'grow-down':
this.x=x;
this.y=y;
this.scaleY=0.01;
caurina.transitions.Tweener.addTween(this,{scaleY:1,time:1.2,delay:d,transition:caurina.transitions.Equations.easeOutQuad});
break;
case'grow-up':
this.x=x;
this.y=y+height;
this.scaleY=0.01;
caurina.transitions.Tweener.addTween(this,{scaleY:1,time:1.2,delay:d,transition:caurina.transitions.Equations.easeOutQuad});
caurina.transitions.Tweener.addTween(this,{y:y,time:1.2,delay:d,transition:caurina.transitions.Equations.easeOutQuad});
break;
case'pop':
this.y=this.top;
this.alpha=0.2;
caurina.transitions.Tweener.addTween(this,{alpha:this.mouse_out_alpha,time:0.7,delay:d,transition:caurina.transitions.Equations.easeOutQuad});
this.x=x+(width/2);
this.y=y+(height/2);
this.width=3;
this.height=3;
caurina.transitions.Tweener.addTween(this,{x:x,y:y,width:width,height:height,time:1.2,delay:d,transition:caurina.transitions.Equations.easeOutElastic});
break;
default:
this.y=y;
this.x=x;
}
},
];},[],["charts.series.Element","flash.events.MouseEvent","Number","NumberUtils","flash.geom.Point","caurina.transitions.Tweener","caurina.transitions.Equations","Math"], "0.8.0", "0.8.4"
);
// class charts.series.bars.Cylinder
joo.classLoader.prepare("package charts.series.bars",
"public class Cylinder extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Math);},

"public function Cylinder",function(index,props,group){
this.super$8(index,props,group);
var dropShadow=new flash.filters.DropShadowFilter();
dropShadow.blurX=5;
dropShadow.blurY=5;
dropShadow.distance=3;
dropShadow.angle=45;
dropShadow.quality=2;
dropShadow.alpha=0.4;
this.filters=[dropShadow];
},
"public override function resize",function(sc){
this.graphics.clear();
var h=this.resize_helper(as(sc,ScreenCoords));
this.bg$8(h.width,h.height,h.upside_down);
this.glass$8(h.width,h.height,h.upside_down);
},
"private function bg",function(w,h,upside_down){
var rad=w/3;
if(rad>(w/2))
rad=w/2;
this.graphics.lineStyle(0,0,0);
var bgcolors=charts.series.bars.Cylinder.GetColours(this.colour);
var bgalphas=[1,1];
var bgratios=[0,255];
var bgmatrix=new flash.geom.Matrix();
var xRadius;
var yRadius;
var x;
var y;
bgmatrix.createGradientBox(w,h,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
x=w/2;
y=h;
xRadius=w/2;
yRadius=rad/2;
this.halfEllipse(x,y,xRadius,yRadius,100);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.moveTo(0,0);
this.graphics.lineTo(0,h);
this.graphics.lineTo(w,h);
this.graphics.lineTo(w,0);
this.graphics.beginFill(this.colour,1);
x=w/2;
y=0;
xRadius=w/2;
yRadius=rad/2;
this.Ellipse(x,y,xRadius,yRadius,100);
this.graphics.endFill();
},
"private function glass",function(w,h,upside_down){
this.graphics.lineStyle(0,0,0);
var colors=[0xFFFFFF,0xFFFFFF];
var alphas=[0,0.5];
var ratios=[150,255];
var xRadius;
var yRadius;
var x;
var y;
var matrix=new flash.geom.Matrix();
matrix.createGradientBox(this.width,this.height,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
var rad=w/3;
x=w/2;
y=h;
xRadius=w/2;
yRadius=rad/2;
this.halfEllipse(x,y,xRadius,yRadius,100);
this.graphics.moveTo(0,0);
this.graphics.lineTo(0,h);
this.graphics.lineTo(w,h);
this.graphics.lineTo(w,0);
this.graphics.beginFill(this.colour,1);
x=w/2;
y=0;
xRadius=w/2;
yRadius=rad/2;
this.Ellipse(x,y,xRadius,yRadius,100);
this.graphics.beginGradientFill('linear',colors,alphas,[25,255],matrix,'pad');
x=w/2;
y=0;
xRadius=w/2;
yRadius=rad/2;
this.Ellipse(x,y,xRadius,yRadius,100);
this.graphics.endFill();
},
"public static function GetColours",function(col){
var rgb=col;
var red=(rgb&16711680)>>16;
var green=(rgb&65280)>>8;
var blue=rgb&255;
var shift=2;
var basecolor=col;
var highlight=col;
var bgred=(rgb&16711680)>>16;
var bggreen=(rgb&65280)>>8;
var bgblue=rgb&255;
var hired=(rgb&16711680)>>16;
var higreen=(rgb&65280)>>8;
var hiblue=rgb&255;
if(red+red/shift>255||green+green/shift>255||blue+blue/shift>255)
{
bgred=red-red/shift;
bggreen=green-green/shift;
bgblue=blue-blue/shift;
}
hired=bgred+red/shift;
hiblue=bgblue+blue/shift;
higreen=bggreen+green/shift;
basecolor=bgred<<16|bggreen<<8|bgblue;
highlight=hired<<16|higreen<<8|hiblue;
return[highlight,basecolor];
},
"public static function magicTrigFunctionX",function(pointRatio){
return Math.cos(pointRatio*2*Math.PI);
},
"public static function magicTrigFunctionY",function(pointRatio){
return Math.sin(pointRatio*2*Math.PI);
},
"public function Ellipse",function(centerX,centerY,xRadius,yRadius,sides){
this.graphics.moveTo(centerX+xRadius,centerY);
for(var i=0;i<=sides;i++){
var pointRatio=i/sides;
var xSteps=charts.series.bars.Cylinder.magicTrigFunctionX(pointRatio);
var ySteps=charts.series.bars.Cylinder.magicTrigFunctionY(pointRatio);
var pointX=centerX+xSteps*xRadius;
var pointY=centerY+ySteps*yRadius;
this.graphics.lineTo(pointX,pointY);
}
return 1;
},
"public function halfEllipse",function(centerX,centerY,xRadius,yRadius,sides){
this.graphics.moveTo(centerX+xRadius,centerY);
for(var i=0;i<=sides/2;i++){
var pointRatio=i/sides;
var xSteps=charts.series.bars.Cylinder.magicTrigFunctionX(pointRatio);
var ySteps=charts.series.bars.Cylinder.magicTrigFunctionY(pointRatio);
var pointX=centerX+xSteps*xRadius;
var pointY=centerY+ySteps*yRadius;
this.graphics.lineTo(pointX,pointY);
}
return 1;
},
];},["GetColours","magicTrigFunctionX","magicTrigFunctionY"],["charts.series.bars.Base","flash.filters.DropShadowFilter","ScreenCoords","flash.geom.Matrix","Math"], "0.8.0", "0.8.4"
);
// class charts.series.bars.CylinderOutline
joo.classLoader.prepare("package charts.series.bars",
"public class CylinderOutline extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Math);},
"public function CylinderOutline",function(index,props,group){
this.super$8(index,props,group);
var dropShadow=new flash.filters.DropShadowFilter();
dropShadow.blurX=5;
dropShadow.blurY=5;
dropShadow.distance=3;
dropShadow.angle=45;
dropShadow.quality=2;
dropShadow.alpha=0.4;
this.filters=[dropShadow];
},
"public override function resize",function(sc){
this.graphics.clear();
var h=this.resize_helper(as(sc,ScreenCoords));
this.bg$8(h.width,h.height,h.upside_down);
this.glass$8(h.width,h.height,h.upside_down);
},
"private function bg",function(w,h,upside_down){
var rad=w/3;
if(rad>(w/2))
rad=w/2;
this.graphics.lineStyle(0,0,0);
var bgcolors=charts.series.bars.CylinderOutline.GetColours(this.colour);
var bgalphas=[1,1];
var bgratios=[0,255];
var bgmatrix=new flash.geom.Matrix();
var xRadius;
var yRadius;
var x;
var y;
bgmatrix.createGradientBox(w,h,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
x=w/2;
y=h;
xRadius=w/2;
yRadius=rad/2;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.moveTo(0,0);
this.graphics.lineTo(0,h);
this.graphics.lineTo(w,h);
this.graphics.lineTo(w,0);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
x=w/2;
y=0;
xRadius=w/2;
yRadius=rad/2;
this.Ellipse(x,y,xRadius,yRadius,100);
this.graphics.endFill();
},
"private function glass",function(w,h,upside_down){
this.graphics.lineStyle(0,0,0);
var bgcolors=charts.series.bars.CylinderOutline.GetColours(this.colour);
var bgalphas=[1,1];
var bgratios=[0,255];
var bgmatrix=new flash.geom.Matrix();
var colors=[0xFFFFFF,0xFFFFFF];
var alphas=[0,0.5];
var ratios=[150,255];
var xRadius;
var yRadius;
var x;
var y;
var matrix=new flash.geom.Matrix();
matrix.createGradientBox(this.width,this.height,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
var rad=w/3;
x=w/2;
y=h;
xRadius=w/2-(0.025*w);
yRadius=rad/2-(0.025*w);
this.halfEllipse(x,y,xRadius,yRadius,100,false);
this.graphics.moveTo(0+(0.025*w),0+(0.025*w));
this.graphics.lineTo(0+(0.025*w),h);
this.graphics.lineTo(w-(0.025*w),h);
this.graphics.lineTo(w-(0.025*w),0+(0.025*w));
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
x=w/2;
y=0;
xRadius=w/2;
yRadius=rad/2;
this.Ellipse(x,y,xRadius,yRadius,100);
this.graphics.beginGradientFill('linear',colors,alphas,[25,255],matrix,'pad');
x=w/2;
y=0;
xRadius=w/2-(0.025*w);
yRadius=rad/2-(0.025*w);
this.Ellipse(x,y,xRadius,yRadius,100);
this.graphics.endFill();
},
"public static function GetColours",function(col){
var rgb=col;
var red=(rgb&16711680)>>16;
var green=(rgb&65280)>>8;
var blue=rgb&255;
var shift=2;
var basecolor=col;
var highlight=col;
var bgred=(rgb&16711680)>>16;
var bggreen=(rgb&65280)>>8;
var bgblue=rgb&255;
var hired=(rgb&16711680)>>16;
var higreen=(rgb&65280)>>8;
var hiblue=rgb&255;
if(red+red/shift>255||green+green/shift>255||blue+blue/shift>255)
{
bgred=red-red/shift;
bggreen=green-green/shift;
bgblue=blue-blue/shift;
}
hired=bgred+red/shift;
hiblue=bgblue+blue/shift;
higreen=bggreen+green/shift;
basecolor=bgred<<16|bggreen<<8|bgblue;
highlight=hired<<16|higreen<<8|hiblue;
return[highlight,basecolor];
},
"public static function magicTrigFunctionX",function(pointRatio){
return Math.cos(pointRatio*2*Math.PI);
},
"public static function magicTrigFunctionY",function(pointRatio){
return Math.sin(pointRatio*2*Math.PI);
},
"public function Ellipse",function(centerX,centerY,xRadius,yRadius,sides){
this.graphics.moveTo(centerX+xRadius,centerY);
for(var i=0;i<=sides;i++){
var pointRatio=i/sides;
var xSteps=charts.series.bars.CylinderOutline.magicTrigFunctionX(pointRatio);
var ySteps=charts.series.bars.CylinderOutline.magicTrigFunctionY(pointRatio);
var pointX=centerX+xSteps*xRadius;
var pointY=centerY+ySteps*yRadius;
this.graphics.lineTo(pointX,pointY);
}
return 1;
},
"public function halfEllipse",function(centerX,centerY,xRadius,yRadius,sides,top){
var loopStart;
var loopEnd;
if(top==true)
{
loopStart=sides/2;
loopEnd=sides;
}
else
{
loopStart=0;
loopEnd=sides/2;
}
this.graphics.moveTo(centerX+xRadius,centerY);
for(var i=loopStart;i<=loopEnd;i++){
var pointRatio=i/sides;
var xSteps=charts.series.bars.CylinderOutline.magicTrigFunctionX(pointRatio);
var ySteps=charts.series.bars.CylinderOutline.magicTrigFunctionY(pointRatio);
var pointX=centerX+xSteps*xRadius;
var pointY=centerY+ySteps*yRadius;
this.graphics.lineTo(pointX,pointY);
}
return 1;
},
];},["GetColours","magicTrigFunctionX","magicTrigFunctionY"],["charts.series.bars.Base","flash.filters.DropShadowFilter","ScreenCoords","flash.geom.Matrix","Math"], "0.8.0", "0.8.4"
);
// class charts.series.bars.Dome
joo.classLoader.prepare("package charts.series.bars",
"public class Dome extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Math);},

"public function Dome",function(index,props,group){
this.super$8(index,props,group);
var dropShadow=new flash.filters.DropShadowFilter();
dropShadow.blurX=5;
dropShadow.blurY=5;
dropShadow.distance=3;
dropShadow.angle=45;
dropShadow.quality=2;
dropShadow.alpha=0.4;
this.filters=[dropShadow];
},
"public override function resize",function(sc){
this.graphics.clear();
var h=this.resize_helper(as(sc,ScreenCoords));
this.bg$8(h.width,h.height,h.upside_down);
this.glass$8(h.width,h.height,h.upside_down);
},
"private function bg",function(w,h,upside_down){
var rad=w/3;
if(rad>(w/2))
rad=w/2;
this.graphics.lineStyle(0,0,0);
var bgcolors=charts.series.bars.Dome.GetColours(this.colour);
var bgalphas=[1,1];
var bgratios=[0,255];
var bgmatrix=new flash.geom.Matrix();
var xRadius;
var yRadius;
var x;
var y;
bgmatrix.createGradientBox(w,h,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
if(!upside_down&&h>0)
{
if(h>=w/2)
{
x=w/2;
y=h;
xRadius=w/2;
yRadius=rad/2;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.moveTo(0,w/2);
this.graphics.lineTo(0,h);
this.graphics.lineTo(w,h);
this.graphics.lineTo(w,w/2);
x=w/2;
y=w/2;
xRadius=w/2;
yRadius=xRadius;
this.halfEllipse(x,y,xRadius,yRadius,100,true);
}
else
{
x=w/2;
y=h;
xRadius=w/2;
yRadius=rad/2;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
x=w/2;
y=h;
xRadius=w/2;
yRadius=h;
this.halfEllipse(x,y,xRadius,yRadius,100,true);
}
}
else
{
if(h>=w/2)
{
x=w/2;
y=0;
xRadius=w/2;
yRadius=rad/2;
this.halfEllipse(x,y,xRadius,yRadius,100,true);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.moveTo(0,0);
this.graphics.lineTo(0,h-w/2);
this.graphics.lineTo(w,h-w/2);
this.graphics.lineTo(w,0);
x=w/2;
y=h-w/2;
xRadius=w/2;
yRadius=xRadius;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
}
else
{
if(h>0)
{
x=w/2;
y=0;
xRadius=w/2;
yRadius=rad/2;
this.halfEllipse(x,y,xRadius,yRadius,100,true);
x=w/2;
y=0;
xRadius=w/2;
yRadius=h;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
}
else
{
x=w/2;
y=h;
xRadius=w/2;
yRadius=rad/4;
this.Ellipse(x,y,xRadius,yRadius,100);
}
}
}
this.graphics.endFill();
},
"private function glass",function(w,h,upside_down){
this.graphics.lineStyle(0,0,0);
var bgcolors=charts.series.bars.Dome.GetColours(this.colour);
var bgalphas=[1,1];
var bgratios=[0,255];
var bgmatrix=new flash.geom.Matrix();
bgmatrix.createGradientBox(w,h,(180/180)*Math.PI);
var colors=[0xFFFFFF,0xFFFFFF];
var alphas=[0,0.75];
var ratios=[100,255];
var xRadius;
var yRadius;
var x;
var y;
var matrix=new flash.geom.Matrix();
matrix.createGradientBox(this.width,this.height,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
var rad=w/3;
if(!upside_down&&h>0)
{
if(h>=w/2)
{
x=w/2;
y=h;
xRadius=w/2-(0.05*w);
yRadius=rad/2-(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,false);
this.graphics.moveTo(0+(0.05*w),w/2);
this.graphics.lineTo(0+(0.05*w),h);
this.graphics.lineTo(w-(0.05*w),h);
this.graphics.lineTo(w-(0.05*w),w/2);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
x=w/2;
y=w/2;
xRadius=w/2;
yRadius=xRadius;
this.halfEllipse(x,y,xRadius,yRadius,100,true);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
x=w/3;
y=w/2;
xRadius=w/3-(0.05*w);
yRadius=xRadius+(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,true);
}
else
{
x=w/2;
y=h;
xRadius=w/2-(0.05*w);
yRadius=rad/2-(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,false);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
x=w/3;
y=h;
xRadius=w/3-(0.05*w);
yRadius=h-2.5*(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,true);
}
}
else
{
if(h>=w/2)
{
x=w/2;
y=0;
xRadius=w/2-(0.05*w);
yRadius=rad/2-(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,true);
this.graphics.moveTo(0+(0.05*w),0);
this.graphics.lineTo(0+(0.05*w),h-w/2);
this.graphics.lineTo(w-(0.05*w),h-w/2);
this.graphics.lineTo(w-(0.05*w),0);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
x=w/2;
y=h-w/2;
xRadius=w/2;
yRadius=xRadius;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
x=w/3;
y=h-w/2;
xRadius=w/3-(0.05*w);
yRadius=xRadius+(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,false);
}
else
{
if(h>0)
{
x=w/2;
y=0;
xRadius=w/2-(0.05*w);
yRadius=rad/2-(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,true);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
x=w/3;
y=0;
xRadius=w/3-(0.05*w);
yRadius=h-2.5*(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,false);
}
else
{
x=w/2;
y=h;
xRadius=w/2-(0.05*w);
yRadius=rad/4-(0.05*w);
this.Ellipse(x,y,xRadius,yRadius,100);
}
}
}
this.graphics.endFill();
},
"public static function GetColours",function(col){
var rgb=col;
var red=(rgb&16711680)>>16;
var green=(rgb&65280)>>8;
var blue=rgb&255;
var shift=2;
var basecolor=col;
var highlight=col;
var bgred=(rgb&16711680)>>16;
var bggreen=(rgb&65280)>>8;
var bgblue=rgb&255;
var hired=(rgb&16711680)>>16;
var higreen=(rgb&65280)>>8;
var hiblue=rgb&255;
if(red+red/shift>255||green+green/shift>255||blue+blue/shift>255)
{
bgred=red-red/shift;
bggreen=green-green/shift;
bgblue=blue-blue/shift;
}
hired=bgred+red/shift;
hiblue=bgblue+blue/shift;
higreen=bggreen+green/shift;
basecolor=bgred<<16|bggreen<<8|bgblue;
highlight=hired<<16|higreen<<8|hiblue;
return[highlight,basecolor];
},
"public static function magicTrigFunctionX",function(pointRatio){
return Math.cos(pointRatio*2*Math.PI);
},
"public static function magicTrigFunctionY",function(pointRatio){
return Math.sin(pointRatio*2*Math.PI);
},
"public function Ellipse",function(centerX,centerY,xRadius,yRadius,sides){
this.graphics.moveTo(centerX+xRadius,centerY);
for(var i=0;i<=sides;i++){
var pointRatio=i/sides;
var xSteps=charts.series.bars.Dome.magicTrigFunctionX(pointRatio);
var ySteps=charts.series.bars.Dome.magicTrigFunctionY(pointRatio);
var pointX=centerX+xSteps*xRadius;
var pointY=centerY+ySteps*yRadius;
this.graphics.lineTo(pointX,pointY);
}
return 1;
},
"public function halfEllipse",function(centerX,centerY,xRadius,yRadius,sides,top){
var loopStart;
var loopEnd;
if(top==true)
{
loopStart=sides/2;
loopEnd=sides;
}
else
{
loopStart=0;
loopEnd=sides/2;
}
this.graphics.moveTo(centerX+xRadius,centerY);
for(var i=loopStart;i<=loopEnd;i++){
var pointRatio=i/sides;
var xSteps=charts.series.bars.Dome.magicTrigFunctionX(pointRatio);
var ySteps=charts.series.bars.Dome.magicTrigFunctionY(pointRatio);
var pointX=centerX+xSteps*xRadius;
var pointY=centerY+ySteps*yRadius;
this.graphics.lineTo(pointX,pointY);
}
return 1;
},
];},["GetColours","magicTrigFunctionX","magicTrigFunctionY"],["charts.series.bars.Base","flash.filters.DropShadowFilter","ScreenCoords","flash.geom.Matrix","Math"], "0.8.0", "0.8.4"
);
// class charts.series.bars.ECandle
joo.classLoader.prepare("package charts.series.bars",
"public class ECandle extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[
"protected var",{high:NaN},
"protected var",{low:NaN},
"protected var",{negative_colour:NaN},
"public function ECandle",function(index,props,group){
this.super$8(index,props,group);
tr.aces(props.has('negative-colour'),props.get_colour('negative-colour'));
if(props.has('negative-colour'))
this.negative_colour=props.get_colour('negative-colour');
else
this.negative_colour=this.colour;
},
"protected override function parse_value",function(props){
this.parse_value$8(props);
this.high=props.get('high');
this.low=props.get('low');
},
"protected override function replace_magic_values",function(t){
t=this.replace_magic_values$8(t);
t=t.replace('#high#',NumberUtils.formatNumber(this.high));
t=t.replace('#open#',NumberUtils.formatNumber(this.top));
t=t.replace('#close#',NumberUtils.formatNumber(this.bottom));
t=t.replace('#low#',NumberUtils.formatNumber(this.low));
return t;
},
"public override function resize",function(sc){
var h=this.resize_helper(as(sc,ScreenCoords));
var tmp=sc.get_y_from_val(Math.max(this.top,this.bottom),this.right_axis);
var bar_high=sc.get_y_from_val(this.high,this.right_axis)-tmp;
var bar_top=0;
var bar_bottom=sc.get_y_from_val(this.bottom,this.right_axis)-tmp;
var bar_low=sc.get_y_from_val(this.low,this.right_axis)-tmp;
this.tip_pos=new flash.geom.Point(this.x+(h.width/2),this.y);
var mid=h.width/2;
this.graphics.clear();
var c=this.colour;
if(h.upside_down)
c=this.negative_colour;
this.top_line$8(c,mid,bar_high);
if(this.top==this.bottom)
this.draw_doji$8(c,h.width,bar_top);
else
this.draw_box$8(c,bar_top,h.height,h.width,h.upside_down);
this.bottom_line$8(c,mid,h.height,bar_low);
this.tip_pos=new flash.geom.Point(
this.x+(h.width/2),
this.y+bar_high);
},
"private function top_line",function(colour,mid,height){
this.graphics.beginFill(colour,1.0);
this.graphics.moveTo(mid-1,0);
this.graphics.lineTo(mid+1,0);
this.graphics.lineTo(mid+1,height);
this.graphics.lineTo(mid-1,height);
this.graphics.endFill();
},
"private function bottom_line",function(colour,mid,top,bottom){
this.graphics.beginFill(colour,1.0);
this.graphics.moveTo(mid-1,top);
this.graphics.lineTo(mid+1,top);
this.graphics.lineTo(mid+1,bottom);
this.graphics.lineTo(mid-1,bottom);
this.graphics.endFill();
},
"private function draw_doji",function(colour,width,pos){
this.graphics.beginFill(colour,1.0);
this.graphics.moveTo(0,pos-1);
this.graphics.lineTo(width,pos-1);
this.graphics.lineTo(width,pos+1);
this.graphics.lineTo(0,pos+1);
this.graphics.endFill();
},
"private function draw_box",function(colour,top,bottom,width,upside_down){
this.graphics.beginFill(colour,1.0);
this.graphics.moveTo(0,top);
this.graphics.lineTo(width,top);
this.graphics.lineTo(width,bottom);
this.graphics.lineTo(0,bottom);
this.graphics.lineTo(0,top);
if(upside_down){
this.graphics.moveTo(2,top+2);
this.graphics.lineTo(width-2,top+2);
this.graphics.lineTo(width-2,bottom-2);
this.graphics.lineTo(2,bottom-2);
this.graphics.lineTo(2,top+2);
}
this.graphics.endFill();
if(upside_down){
this.graphics.lineStyle(0,0,0);
this.graphics.beginFill(0,0);
this.graphics.moveTo(2,top-2);
this.graphics.lineTo(width-2,top-2);
this.graphics.lineTo(width-2,bottom-2);
this.graphics.lineTo(2,bottom-2);
this.graphics.endFill();
}
},
];},[],["charts.series.bars.Base","tr","NumberUtils","ScreenCoords","Math","flash.geom.Point"], "0.8.0", "0.8.4"
);
// class charts.series.bars.Glass
joo.classLoader.prepare("package charts.series.bars",
"public class Glass extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Math);},

"public function Glass",function(index,props,group){
this.super$8(index,props,group);
var dropShadow=new flash.filters.DropShadowFilter();
dropShadow.blurX=5;
dropShadow.blurY=5;
dropShadow.distance=3;
dropShadow.angle=45;
dropShadow.quality=2;
dropShadow.alpha=0.4;
this.filters=[dropShadow];
},
"public override function resize",function(sc){
this.graphics.clear();
var h=this.resize_helper(as(sc,ScreenCoords));
if(h.height==0)
return;
this.bg$8(h.width,h.height,h.upside_down);
this.glass$8(h.width,h.height,h.upside_down);
},
"private function bg",function(w,h,upside_down){
var rad=7;
if(rad>(w/2))
rad=w/2;
this.graphics.lineStyle(0,0,0);
this.graphics.beginFill(this.colour,1);
if(!upside_down)
{
this.graphics.moveTo(0+rad,0);
this.graphics.lineTo(w-rad,0);
this.graphics.curveTo(w,0,w,rad);
this.graphics.lineTo(w,h);
this.graphics.lineTo(0,h);
this.graphics.lineTo(0,0+rad);
this.graphics.curveTo(0,0,0+rad,0);
}
else
{
this.graphics.moveTo(0,0);
this.graphics.lineTo(w,0);
this.graphics.lineTo(w,h-rad);
this.graphics.curveTo(w,h,w-rad,h);
this.graphics.lineTo(rad,h);
this.graphics.curveTo(0,h,0,h-rad);
this.graphics.lineTo(0,0);
}
this.graphics.endFill();
},
"private function glass",function(w,h,upside_down){
var x=2;
var y=x;
var width=(w/2)-x;
if(upside_down)
y-=x;
h-=x;
this.graphics.lineStyle(0,0,0);
var colors=[0xFFFFFF,0xFFFFFF];
var alphas=[0.3,0.7];
var ratios=[0,255];
var matrix=new flash.geom.Matrix();
matrix.createGradientBox(width,this.height,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
var rad=4;
var w=width;
if(!upside_down)
{
this.graphics.moveTo(x+rad,y);
this.graphics.lineTo(x+w,y);
this.graphics.lineTo(x+w,y+h);
this.graphics.lineTo(x,y+h);
this.graphics.lineTo(x,y+rad);
this.graphics.curveTo(x,y,x+rad,y);
}
else
{
this.graphics.moveTo(x,y);
this.graphics.lineTo(x+w,y);
this.graphics.lineTo(x+w,y+h);
this.graphics.lineTo(x+rad,y+h);
this.graphics.curveTo(x,y+h,x,y+h-rad);
}
this.graphics.endFill();
},
];},[],["charts.series.bars.Base","flash.filters.DropShadowFilter","ScreenCoords","flash.geom.Matrix","Math"], "0.8.0", "0.8.4"
);
// class charts.series.bars.Horizontal
joo.classLoader.prepare("package charts.series.bars",
"public class Horizontal extends charts.series.Element",7,function($$private){var as=joo.as,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(caurina.transitions.Equations,flash.events.MouseEvent);},

"private var",{right:NaN},
"private var",{left:NaN},
"public var",{colour:NaN},
"protected var",{group:NaN},
"public function Horizontal",function(index,style,group)
{
this.super$7();
this.index=index;
this.left$7=style.left?style.left:0;
this.right$7=style.right?style.right:0;
this.colour=style.colour;
this.group=group;
this.visible=true;
this.alpha=0.5;
this.tooltip=this.replace_magic_values(style.tip);
this.addEventListener(flash.events.MouseEvent.MOUSE_OVER,$$bound(this,"mouseOver"));
this.addEventListener(flash.events.MouseEvent.MOUSE_OUT,$$bound(this,"mouseOut"));
},
"protected function replace_magic_values",function(t){
t=t.replace('#right#',NumberUtils.formatNumber(this.right$7));
t=t.replace('#left#',NumberUtils.formatNumber(this.left$7));
t=t.replace('#val#',NumberUtils.formatNumber(this.right$7-this.left$7));
return t;
},
"public override function mouseOver",function(event){
caurina.transitions.Tweener.addTween(this,{alpha:1,time:0.6,transition:caurina.transitions.Equations.easeOutCirc});
},
"public override function mouseOut",function(event){
caurina.transitions.Tweener.addTween(this,{alpha:0.5,time:0.8,transition:caurina.transitions.Equations.easeOutElastic});
},
"public override function resize",function(sc){
var sc2=as(sc,ScreenCoords);
var tmp=sc2.get_horiz_bar_coords(this.index,this.group);
var left=sc.get_x_from_val(this.left$7);
var right=sc.get_x_from_val(this.right$7);
var width=right-left;
this.graphics.clear();
this.graphics.beginFill(this.colour,1.0);
this.graphics.drawRect(0,0,width,tmp.width);
this.graphics.endFill();
this.x=left;
this.y=tmp.y;
},
"public override function get_mid_point",function(){
return new flash.geom.Point(this.x+(this.width/2),this.y);
},
"public override function get_tip_pos",function(){
return{x:this.x+this.width-20,y:this.y};
},
"public function get_max_x",function(){
return this.right$7;
},
];},[],["charts.series.Element","flash.events.MouseEvent","NumberUtils","caurina.transitions.Tweener","caurina.transitions.Equations","ScreenCoords","flash.geom.Point"], "0.8.0", "0.8.4"
);
// class charts.series.bars.Outline
joo.classLoader.prepare("package charts.series.bars",
"public class Outline extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[
"private var",{outline:NaN},
"public function Outline",function(index,props,group){
this.super$8(index,props,group);
this.outline$8=props.get_colour('outline-colour');
},
"public override function resize",function(sc){
var h=this.resize_helper(as(sc,ScreenCoords));
this.graphics.clear();
this.graphics.lineStyle(1,this.outline$8,1);
this.graphics.beginFill(this.colour,1.0);
this.graphics.moveTo(0,0);
this.graphics.lineTo(h.width,0);
this.graphics.lineTo(h.width,h.height);
this.graphics.lineTo(0,h.height);
this.graphics.lineTo(0,0);
this.graphics.endFill();
},
];},[],["charts.series.bars.Base","ScreenCoords"], "0.8.0", "0.8.4"
);
// class charts.series.bars.Plastic
joo.classLoader.prepare("package charts.series.bars",
"public class Plastic extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Math);},

"public function Plastic",function(index,props,group){
this.super$8(index,props,group);
var dropShadow=new flash.filters.DropShadowFilter();
dropShadow.blurX=5;
dropShadow.blurY=5;
dropShadow.distance=3;
dropShadow.angle=45;
dropShadow.quality=2;
dropShadow.alpha=0.4;
this.filters=[dropShadow];
},
"public override function resize",function(sc){
this.graphics.clear();
var h=this.resize_helper(as(sc,ScreenCoords));
this.bg$8(h.width,h.height,h.upside_down);
this.glass$8(h.width,h.height,h.upside_down);
},
"private function bg",function(w,h,upside_down){
var rad=w/3;
if(rad>(w/2))
rad=w/2;
this.graphics.lineStyle(0,0,0);
var allcolors=charts.series.bars.Plastic.GetColours(this.colour);
var lowlight=allcolors[2];
var highlight=allcolors[0];
var bgcolors=[allcolors[1],allcolors[2],allcolors[2]];
var bgalphas=[1,1,1];
var bgratios=[0,115,255];
var bgmatrix=new flash.geom.Matrix();
var xRadius;
var yRadius;
var x;
var y;
var bevel=0.02*w;
var div=3;
bgmatrix.createGradientBox(w,h,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
if(h>0||h<0)
{
this.graphics.beginFill(0x000000,1);
this.graphics.drawRoundRect(0,0,w,h,w/div,w/div);
this.graphics.beginFill(highlight,1);
this.graphics.drawRoundRect(0+bevel,0+bevel,w-2*bevel,h-2*bevel,w/div-2*bevel,w/div-2*bevel);
bevel=bevel*3;
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.drawRoundRect(0+bevel,0+bevel,w-2*bevel,h-2*bevel,w/div-2*bevel,w/div-2*bevel);
}
else
{
this.graphics.beginFill(0x000000,1);
this.graphics.drawRoundRect(0,0-2*bevel,w,h+4*bevel,w/div,w/div);
this.graphics.beginFill(highlight,1);
this.graphics.drawRoundRect(0+bevel,0-2*bevel+bevel,w-2*bevel,h+4*bevel-2*bevel,w/div-2*bevel,w/div-2*bevel);
bevel=bevel*3;
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.drawRoundRect(0+bevel,0-2*bevel+bevel,w-2*bevel,h+4*bevel-2*bevel,w/div-2*bevel,w/div-2*bevel);
}
this.graphics.endFill();
},
"private function glass",function(w,h,upside_down){
this.graphics.lineStyle(0,0,0);
var bgcolors=charts.series.bars.Plastic.GetColours(this.colour);
var bgmatrix=new flash.geom.Matrix();
var bgalphas=[1,1];
var bgratios=[0,255];
var colors=[0xFFFFFF,0xFFFFFF,0xFFFFFF];
var alphas=[0,0.05,0.75];
var ratios=[0,123,255];
var xRadius;
var yRadius;
var x;
var y;
var matrix=new flash.geom.Matrix();
var bevel=0.02*w;
var div=3;
matrix.createGradientBox(this.width,this.height,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
var rad=w/3;
if(h>0||h<0)
{
this.graphics.drawRoundRect(0+bevel,0+bevel,w-2*bevel,h-2*bevel,w/div-2*bevel,w/div-2*bevel);
}
else
{
this.graphics.drawRoundRect(0+bevel,0-2*bevel+bevel,w-2*bevel,h+4*bevel-2*bevel,w/div-2*bevel,w/div-2*bevel);
}
this.graphics.endFill();
},
"public static function GetColours",function(col){
var rgb=col;
var red=(rgb&16711680)>>16;
var green=(rgb&65280)>>8;
var blue=rgb&255;
var shift=0.15;
var loshift=1.75;
var basecolor=col;
var lowlight=col;
var highlight=col;
var bgred=(rgb&16711680)>>16;
var bggreen=(rgb&65280)>>8;
var bgblue=rgb&255;
var lored=(rgb&16711680)>>16;
var logreen=(rgb&65280)>>8;
var loblue=rgb&255;
var hired=(rgb&16711680)>>16;
var higreen=(rgb&65280)>>8;
var hiblue=rgb&255;
if(red+red*shift<255&&red-loshift*red*shift>0)
{
bgred=red;
}
else
{
if(red+red*shift<255)
{
bgred=red+red/shift;
}
else
{
bgred=red-loshift*red*shift;
}
}
if(blue+blue*shift<255&&blue-loshift*blue*shift>0)
{
bgblue=blue;
}
else
{
if(blue+blue*shift<255)
{
bgblue=blue+blue*shift;
}
else
{
bgblue=blue-loshift*blue*shift;
}
}
if(green+green*shift<255&&green-loshift*green*shift>0)
{
bggreen=green;
}
else
{
if(green+green*shift<255)
{
bggreen=green+green*shift;
}
else
{
bggreen=green-loshift*green*shift;
}
}
hired=bgred+red*shift;
lored=bgred-loshift*(red*shift);
hiblue=bgblue+blue*shift;
loblue=bgblue-loshift*(blue*shift);
higreen=bggreen+green*shift;
logreen=bggreen-loshift*(green*shift);
basecolor=bgred<<16|bggreen<<8|bgblue;
highlight=hired<<16|higreen<<8|hiblue;
lowlight=lored<<16|logreen<<8|loblue;
return[highlight,basecolor,lowlight];
},
"public static function magicTrigFunctionX",function(pointRatio){
return Math.cos(pointRatio*2*Math.PI);
},
"public static function magicTrigFunctionY",function(pointRatio){
return Math.sin(pointRatio*2*Math.PI);
},
"public function Ellipse",function(centerX,centerY,xRadius,yRadius,sides){
this.graphics.moveTo(centerX+xRadius,centerY);
for(var i=0;i<=sides;i++){
var pointRatio=i/sides;
var xSteps=charts.series.bars.Plastic.magicTrigFunctionX(pointRatio);
var ySteps=charts.series.bars.Plastic.magicTrigFunctionY(pointRatio);
var pointX=centerX+xSteps*xRadius;
var pointY=centerY+ySteps*yRadius;
this.graphics.lineTo(pointX,pointY);
}
return 1;
},
];},["GetColours","magicTrigFunctionX","magicTrigFunctionY"],["charts.series.bars.Base","flash.filters.DropShadowFilter","ScreenCoords","flash.geom.Matrix","Math"], "0.8.0", "0.8.4"
);
// class charts.series.bars.PlasticFlat
joo.classLoader.prepare("package charts.series.bars",
"public class PlasticFlat extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Math);},

"public function PlasticFlat",function(index,props,group){
this.super$8(index,props,group);
var dropShadow=new flash.filters.DropShadowFilter();
dropShadow.blurX=5;
dropShadow.blurY=5;
dropShadow.distance=3;
dropShadow.angle=45;
dropShadow.quality=2;
dropShadow.alpha=0.4;
this.filters=[dropShadow];
},
"public override function resize",function(sc){
this.graphics.clear();
var h=this.resize_helper(as(sc,ScreenCoords));
this.bg$8(h.width,h.height,h.upside_down);
this.glass$8(h.width,h.height,h.upside_down);
},
"private function bg",function(w,h,upside_down){
var rad=w/3;
if(rad>(w/2))
rad=w/2;
this.graphics.lineStyle(0,0,0);
var allcolors=charts.series.bars.PlasticFlat.GetColours(this.colour);
var lowlight=allcolors[2];
var highlight=allcolors[0];
var bgcolors=[allcolors[1],allcolors[2],allcolors[2]];
var bgalphas=[1,1,1];
var bgratios=[0,115,255];
var bgmatrix=new flash.geom.Matrix();
var xRadius;
var yRadius;
var x;
var y;
var bevel=0.02*w;
var div=3;
bgmatrix.createGradientBox(w,h,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
if(h>0||h<0)
{
this.graphics.beginFill(0x000000,1);
this.graphics.drawRoundRect(0,0,w,h,w/div,w/div);
this.graphics.beginFill(highlight,1);
this.graphics.drawRoundRect(0+bevel,0+bevel,w-2*bevel,h-2*bevel,w/div-2*bevel,w/div-2*bevel);
bevel=bevel*3;
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.drawRoundRect(0+bevel,0+bevel,w-2*bevel,h-2*bevel,w/div-2*bevel,w/div-2*bevel);
}
else
{
this.graphics.beginFill(0x000000,1);
this.graphics.drawRoundRect(0,0-2*bevel,w,h+4*bevel,w/div,w/div);
this.graphics.beginFill(highlight,1);
this.graphics.drawRoundRect(0+bevel,0-2*bevel+bevel,w-2*bevel,h+4*bevel-2*bevel,w/div-2*bevel,w/div-2*bevel);
bevel=bevel*3;
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.drawRoundRect(0+bevel,0-2*bevel+bevel,w-2*bevel,h+4*bevel-2*bevel,w/div-2*bevel,w/div-2*bevel);
}
this.graphics.endFill();
},
"private function glass",function(w,h,upside_down){
this.graphics.lineStyle(0,0,0);
var allcolors=charts.series.bars.PlasticFlat.GetColours(this.colour);
var lowlight=allcolors[2];
var highlight=allcolors[0];
var bgcolors=[allcolors[1],allcolors[2],allcolors[2]];
var bgalphas=[1,1,1];
var bgratios=[0,115,255];
var bgmatrix=new flash.geom.Matrix();
var colors=[0xFFFFFF,0xFFFFFF,0xFFFFFF];
var alphas=[0,0.05,0.75];
var ratios=[0,123,255];
var xRadius;
var yRadius;
var x;
var y;
var matrix=new flash.geom.Matrix();
var bevel=0.02*w;
var div=3;
bgmatrix.createGradientBox(w,h,(180/180)*Math.PI);
matrix.createGradientBox(this.width,this.height,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
var rad=w/3;
if(h>0&&!upside_down)
{
this.graphics.drawRoundRect(0+bevel,0+bevel,w-2*bevel,h-2*bevel,w/div-2*bevel,w/div-2*bevel);
this.graphics.beginFill(0x000000,1);
this.graphics.drawRect(0,h-h/2,w,h/2);
this.graphics.beginFill(highlight,1);
this.graphics.drawRect(0+bevel,h-h/2,w-2*bevel,h/2-bevel);
bevel=bevel*3;
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.drawRect(0+bevel,h-h/2,w-2*bevel,h/2-bevel);
bevel=bevel/3;
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
this.graphics.drawRect(0+bevel,h-h/2,w-2*bevel,h/2-bevel);
}
else if(h>0)
{
this.graphics.drawRoundRect(0+bevel,0+bevel,w-2*bevel,h-2*bevel,w/div-2*bevel,w/div-2*bevel);
this.graphics.beginFill(0x000000,1);
this.graphics.drawRect(0,0,w,h/2);
this.graphics.beginFill(highlight,1);
this.graphics.drawRect(0+bevel,0+bevel,w-2*bevel,h/2-bevel);
bevel=bevel*3;
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.drawRect(0+bevel,0+bevel,w-2*bevel,h/2-bevel);
bevel=bevel/3;
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
this.graphics.drawRect(0+bevel,0+bevel,w-2*bevel,h/2-bevel);
}
else
{
this.graphics.drawRoundRect(0+bevel,0-2*bevel+bevel,w-2*bevel,h+4*bevel-2*bevel,w/div-2*bevel,w/div-2*bevel);
}
this.graphics.endFill();
},
"public static function GetColours",function(col){
var rgb=col;
var red=(rgb&16711680)>>16;
var green=(rgb&65280)>>8;
var blue=rgb&255;
var shift=0.15;
var loshift=1.75;
var basecolor=col;
var lowlight=col;
var highlight=col;
var bgred=(rgb&16711680)>>16;
var bggreen=(rgb&65280)>>8;
var bgblue=rgb&255;
var lored=(rgb&16711680)>>16;
var logreen=(rgb&65280)>>8;
var loblue=rgb&255;
var hired=(rgb&16711680)>>16;
var higreen=(rgb&65280)>>8;
var hiblue=rgb&255;
if(red+red*shift<255&&red-loshift*red*shift>0)
{
bgred=red;
}
else
{
if(red+red*shift<255)
{
bgred=red+red/shift;
}
else
{
bgred=red-loshift*red*shift;
}
}
if(blue+blue*shift<255&&blue-loshift*blue*shift>0)
{
bgblue=blue;
}
else
{
if(blue+blue*shift<255)
{
bgblue=blue+blue*shift;
}
else
{
bgblue=blue-loshift*blue*shift;
}
}
if(green+green*shift<255&&green-loshift*green*shift>0)
{
bggreen=green;
}
else
{
if(green+green*shift<255)
{
bggreen=green+green*shift;
}
else
{
bggreen=green-loshift*green*shift;
}
}
hired=bgred+red*shift;
lored=bgred-loshift*(red*shift);
hiblue=bgblue+blue*shift;
loblue=bgblue-loshift*(blue*shift);
higreen=bggreen+green*shift;
logreen=bggreen-loshift*(green*shift);
basecolor=bgred<<16|bggreen<<8|bgblue;
highlight=hired<<16|higreen<<8|hiblue;
lowlight=lored<<16|logreen<<8|loblue;
return[highlight,basecolor,lowlight];
},
"public static function magicTrigFunctionX",function(pointRatio){
return Math.cos(pointRatio*2*Math.PI);
},
"public static function magicTrigFunctionY",function(pointRatio){
return Math.sin(pointRatio*2*Math.PI);
},
"public function Ellipse",function(centerX,centerY,xRadius,yRadius,sides){
this.graphics.moveTo(centerX+xRadius,centerY);
for(var i=0;i<=sides;i++){
var pointRatio=i/sides;
var xSteps=charts.series.bars.PlasticFlat.magicTrigFunctionX(pointRatio);
var ySteps=charts.series.bars.PlasticFlat.magicTrigFunctionY(pointRatio);
var pointX=centerX+xSteps*xRadius;
var pointY=centerY+ySteps*yRadius;
this.graphics.lineTo(pointX,pointY);
}
return 1;
},
];},["GetColours","magicTrigFunctionX","magicTrigFunctionY"],["charts.series.bars.Base","flash.filters.DropShadowFilter","ScreenCoords","flash.geom.Matrix","Math"], "0.8.0", "0.8.4"
);
// class charts.series.bars.Round
joo.classLoader.prepare("package charts.series.bars",
"public class Round extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Math);},

"public function Round",function(index,props,group){
this.super$8(index,props,group);
var dropShadow=new flash.filters.DropShadowFilter();
dropShadow.blurX=5;
dropShadow.blurY=5;
dropShadow.distance=3;
dropShadow.angle=45;
dropShadow.quality=2;
dropShadow.alpha=0.4;
this.filters=[dropShadow];
},
"public override function resize",function(sc){
this.graphics.clear();
var h=this.resize_helper(as(sc,ScreenCoords));
this.bg$8(h.width,h.height,h.upside_down);
this.glass$8(h.width,h.height,h.upside_down);
},
"private function bg",function(w,h,upside_down){
var rad=w/3;
if(rad>(w/2))
rad=w/2;
this.graphics.lineStyle(0,0,0);
var bgcolors=charts.series.bars.Round.GetColours(this.colour);
var bgalphas=[1,1];
var bgratios=[0,255];
var bgmatrix=new flash.geom.Matrix();
var xRadius;
var yRadius;
var x;
var y;
bgmatrix.createGradientBox(w,h,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
if(h>0)
{
if(h>=w)
{
x=w/2;
y=h-w/2;
xRadius=w/2;
yRadius=xRadius;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.moveTo(0,w/2);
this.graphics.lineTo(0,h-w/2);
this.graphics.lineTo(w,h-w/2);
this.graphics.lineTo(w,w/2);
x=w/2;
y=w/2;
xRadius=w/2;
yRadius=xRadius;
this.halfEllipse(x,y,xRadius,yRadius,100,true);
}
else
{
x=w/2;
y=h/2;
xRadius=w/2;
yRadius=h/2;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
x=w/2;
y=h/2;
xRadius=w/2;
yRadius=h/2;
this.halfEllipse(x,y,xRadius,yRadius,100,true);
}
}
else
{
{
x=w/2;
y=h;
xRadius=w/2;
yRadius=rad/4;
this.Ellipse(x,y,xRadius,yRadius,100);
}
}
this.graphics.endFill();
},
"private function glass",function(w,h,upside_down){
this.graphics.lineStyle(0,0,0);
var bgcolors=charts.series.bars.Round.GetColours(this.colour);
var bgalphas=[1,1];
var bgratios=[0,255];
var bgmatrix=new flash.geom.Matrix();
bgmatrix.createGradientBox(w,h,(180/180)*Math.PI);
var colors=[0xFFFFFF,0xFFFFFF];
var alphas=[0,0.75];
var ratios=[100,255];
var xRadius;
var yRadius;
var x;
var y;
var matrix=new flash.geom.Matrix();
matrix.createGradientBox(this.width,this.height,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
var rad=w/3;
if(h>0)
{
if(h>=w)
{
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
x=w/3;
y=h-w/2;
xRadius=w/3-(0.05*w);
yRadius=xRadius+(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,false);
this.graphics.moveTo(0+(0.05*w),w/2);
this.graphics.lineTo(0+(0.05*w),h-w/2);
this.graphics.lineTo(w-(0.05*w),h-w/2);
this.graphics.lineTo(w-(0.05*w),w/2);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
x=w/3;
y=w/2;
xRadius=w/3-(0.05*w);
yRadius=xRadius+(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,true);
}
else
{
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
x=w/3;
y=h/2;
xRadius=w/3-(0.05*w);
yRadius=h/2-3*(0.05*w);
this.Ellipse(x,y,xRadius,yRadius,100);
}
}
else
{
x=w/2;
y=h;
xRadius=w/2-(0.05*w);
yRadius=rad/4-(0.05*w);
this.Ellipse(x,y,xRadius,yRadius,100);
}
this.graphics.endFill();
},
"public static function GetColours",function(col){
var rgb=col;
var red=(rgb&16711680)>>16;
var green=(rgb&65280)>>8;
var blue=rgb&255;
var shift=2;
var basecolor=col;
var highlight=col;
var bgred=(rgb&16711680)>>16;
var bggreen=(rgb&65280)>>8;
var bgblue=rgb&255;
var hired=(rgb&16711680)>>16;
var higreen=(rgb&65280)>>8;
var hiblue=rgb&255;
if(red+red/shift>255||green+green/shift>255||blue+blue/shift>255)
{
bgred=red-red/shift;
bggreen=green-green/shift;
bgblue=blue-blue/shift;
}
hired=bgred+red/shift;
hiblue=bgblue+blue/shift;
higreen=bggreen+green/shift;
basecolor=bgred<<16|bggreen<<8|bgblue;
highlight=hired<<16|higreen<<8|hiblue;
return[highlight,basecolor];
},
"public static function magicTrigFunctionX",function(pointRatio){
return Math.cos(pointRatio*2*Math.PI);
},
"public static function magicTrigFunctionY",function(pointRatio){
return Math.sin(pointRatio*2*Math.PI);
},
"public function Ellipse",function(centerX,centerY,xRadius,yRadius,sides){
this.graphics.moveTo(centerX+xRadius,centerY);
for(var i=0;i<=sides;i++){
var pointRatio=i/sides;
var xSteps=charts.series.bars.Round.magicTrigFunctionX(pointRatio);
var ySteps=charts.series.bars.Round.magicTrigFunctionY(pointRatio);
var pointX=centerX+xSteps*xRadius;
var pointY=centerY+ySteps*yRadius;
this.graphics.lineTo(pointX,pointY);
}
return 1;
},
"public function halfEllipse",function(centerX,centerY,xRadius,yRadius,sides,top){
var loopStart;
var loopEnd;
if(top==true)
{
loopStart=sides/2;
loopEnd=sides;
}
else
{
loopStart=0;
loopEnd=sides/2;
}
this.graphics.moveTo(centerX+xRadius,centerY);
for(var i=loopStart;i<=loopEnd;i++){
var pointRatio=i/sides;
var xSteps=charts.series.bars.Round.magicTrigFunctionX(pointRatio);
var ySteps=charts.series.bars.Round.magicTrigFunctionY(pointRatio);
var pointX=centerX+xSteps*xRadius;
var pointY=centerY+ySteps*yRadius;
this.graphics.lineTo(pointX,pointY);
}
return 1;
},
];},["GetColours","magicTrigFunctionX","magicTrigFunctionY"],["charts.series.bars.Base","flash.filters.DropShadowFilter","ScreenCoords","flash.geom.Matrix","Math"], "0.8.0", "0.8.4"
);
// class charts.series.bars.Round3D
joo.classLoader.prepare("package charts.series.bars",
"public class Round3D extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Math);},

"public function Round3D",function(index,props,group){
this.super$8(index,props,group);
var dropShadow=new flash.filters.DropShadowFilter();
dropShadow.blurX=5;
dropShadow.blurY=5;
dropShadow.distance=3;
dropShadow.angle=45;
dropShadow.quality=2;
dropShadow.alpha=0.4;
this.filters=[dropShadow];
},
"public override function resize",function(sc){
this.graphics.clear();
var h=this.resize_helper(as(sc,ScreenCoords));
this.bg$8(h.width,h.height,h.upside_down);
this.glass$8(h.width,h.height,h.upside_down);
},
"private function bg",function(w,h,upside_down){
var rad=w/3;
if(rad>(w/2))
rad=w/2;
this.graphics.lineStyle(0,0,0);
var bgcolors=charts.series.bars.Round3D.GetColours(this.colour);
var bgalphas=[1,1];
var bgratios=[0,255];
var bgmatrix=new flash.geom.Matrix();
var xRadius;
var yRadius;
var x;
var y;
bgmatrix.createGradientBox(w,h,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
if(!upside_down&&h>0)
{
if(h>=w/2)
{
x=w/2;
y=h;
xRadius=w/2;
yRadius=rad/2;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.moveTo(0,w/2);
this.graphics.lineTo(0,h);
this.graphics.lineTo(w,h);
this.graphics.lineTo(w,w/2);
x=w/2;
y=w/2;
xRadius=w/2;
yRadius=xRadius;
this.halfEllipse(x,y,xRadius,yRadius,100,true);
}
else
{
x=w/2;
y=h;
xRadius=w/2;
yRadius=rad/2;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
x=w/2;
y=h;
xRadius=w/2;
yRadius=h;
this.halfEllipse(x,y,xRadius,yRadius,100,true);
}
}
else
{
if(h>=w/2)
{
x=w/2;
y=0;
xRadius=w/2;
yRadius=rad/2;
this.halfEllipse(x,y,xRadius,yRadius,100,true);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.moveTo(0,0);
this.graphics.lineTo(0,h-w/2);
this.graphics.lineTo(w,h-w/2);
this.graphics.lineTo(w,0);
x=w/2;
y=h-w/2;
xRadius=w/2;
yRadius=xRadius;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
}
else
{
if(h>0)
{
x=w/2;
y=0;
xRadius=w/2;
yRadius=rad/2;
this.halfEllipse(x,y,xRadius,yRadius,100,true);
x=w/2;
y=0;
xRadius=w/2;
yRadius=h;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
}
else
{
x=w/2;
y=h;
xRadius=w/2;
yRadius=rad/4;
this.Ellipse(x,y,xRadius,yRadius,100);
}
}
}
this.graphics.endFill();
},
"private function glass",function(w,h,upside_down){
this.graphics.lineStyle(0,0,0);
var bgcolors=charts.series.bars.Round3D.GetColours(this.colour);
var bgalphas=[1,1];
var bgratios=[0,255];
var bgmatrix=new flash.geom.Matrix();
bgmatrix.createGradientBox(w,h,(180/180)*Math.PI);
var colors=[0xFFFFFF,0xFFFFFF];
var alphas=[0,0.75];
var ratios=[100,255];
var xRadius;
var yRadius;
var x;
var y;
var matrix=new flash.geom.Matrix();
matrix.createGradientBox(this.width,this.height,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
var rad=w/3;
if(!upside_down&&h>0)
{
if(h>=w/2)
{
x=w/2;
y=h;
xRadius=w/2-(0.05*w);
yRadius=rad/2-(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,false);
this.graphics.moveTo(0+(0.05*w),w/2);
this.graphics.lineTo(0+(0.05*w),h);
this.graphics.lineTo(w-(0.05*w),h);
this.graphics.lineTo(w-(0.05*w),w/2);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
x=w/2;
y=w/2;
xRadius=w/2;
yRadius=xRadius;
this.halfEllipse(x,y,xRadius,yRadius,100,true);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
x=w/3;
y=w/2;
xRadius=w/3-(0.05*w);
yRadius=xRadius+(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,true);
}
else
{
x=w/2;
y=h;
xRadius=w/2-(0.05*w);
yRadius=rad/2-(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,false);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
x=w/3;
y=h;
xRadius=w/3-(0.05*w);
yRadius=h-2.5*(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,true);
}
}
else
{
if(h>=w/2)
{
this.graphics.moveTo(0+(0.05*w),0);
this.graphics.lineTo(0+(0.05*w),h-w/2);
this.graphics.lineTo(w-(0.05*w),h-w/2);
this.graphics.lineTo(w-(0.05*w),0);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
x=w/2;
y=h-w/2;
xRadius=w/2;
yRadius=xRadius;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
x=w/3;
y=h-w/2;
xRadius=w/3-(0.05*w);
yRadius=xRadius+(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,false);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
x=w/2;
y=0;
xRadius=w/2;
yRadius=rad/2;
this.Ellipse(x,y,xRadius,yRadius,100);
this.graphics.beginGradientFill('linear',colors,[0.1,0.7],[0,255],matrix,'pad');
x=w/2;
y=0;
xRadius=w/2-(0.05*w);
yRadius=rad/2-(0.05*w);
this.Ellipse(x,y,xRadius,yRadius,100);
}
else
{
if(h>0)
{
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
x=w/3;
y=0;
xRadius=w/3-(0.05*w);
yRadius=h-2.5*(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,false);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
x=w/2;
y=0;
xRadius=w/2;
yRadius=rad/2;
this.Ellipse(x,y,xRadius,yRadius,100);
this.graphics.beginGradientFill('linear',colors,[0.1,0.67],[0,255],matrix,'pad');
x=w/2;
y=0;
xRadius=w/2-(0.05*w);
yRadius=rad/2-(0.05*w);
this.Ellipse(x,y,xRadius,yRadius,100);
}
else
{
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
x=w/2;
y=0;
xRadius=w/2;
yRadius=rad/2;
this.Ellipse(x,y,xRadius,yRadius,100);
this.graphics.beginGradientFill('linear',colors,[0.1,0.7],[0,255],matrix,'pad');
x=w/2;
y=0;
xRadius=w/2-(0.05*w);
yRadius=rad/2-(0.05*w);
this.Ellipse(x,y,xRadius,yRadius,100);
}
}
}
this.graphics.endFill();
},
"public static function GetColours",function(col){
var rgb=col;
var red=(rgb&16711680)>>16;
var green=(rgb&65280)>>8;
var blue=rgb&255;
var shift=2;
var basecolor=col;
var highlight=col;
var bgred=(rgb&16711680)>>16;
var bggreen=(rgb&65280)>>8;
var bgblue=rgb&255;
var hired=(rgb&16711680)>>16;
var higreen=(rgb&65280)>>8;
var hiblue=rgb&255;
if(red+red/shift>255||green+green/shift>255||blue+blue/shift>255)
{
bgred=red-red/shift;
bggreen=green-green/shift;
bgblue=blue-blue/shift;
}
hired=bgred+red/shift;
hiblue=bgblue+blue/shift;
higreen=bggreen+green/shift;
basecolor=bgred<<16|bggreen<<8|bgblue;
highlight=hired<<16|higreen<<8|hiblue;
return[highlight,basecolor];
},
"public static function magicTrigFunctionX",function(pointRatio){
return Math.cos(pointRatio*2*Math.PI);
},
"public static function magicTrigFunctionY",function(pointRatio){
return Math.sin(pointRatio*2*Math.PI);
},
"public function Ellipse",function(centerX,centerY,xRadius,yRadius,sides){
this.graphics.moveTo(centerX+xRadius,centerY);
for(var i=0;i<=sides;i++){
var pointRatio=i/sides;
var xSteps=charts.series.bars.Round3D.magicTrigFunctionX(pointRatio);
var ySteps=charts.series.bars.Round3D.magicTrigFunctionY(pointRatio);
var pointX=centerX+xSteps*xRadius;
var pointY=centerY+ySteps*yRadius;
this.graphics.lineTo(pointX,pointY);
}
return 1;
},
"public function halfEllipse",function(centerX,centerY,xRadius,yRadius,sides,top){
var loopStart;
var loopEnd;
if(top==true)
{
loopStart=sides/2;
loopEnd=sides;
}
else
{
loopStart=0;
loopEnd=sides/2;
}
this.graphics.moveTo(centerX+xRadius,centerY);
for(var i=loopStart;i<=loopEnd;i++){
var pointRatio=i/sides;
var xSteps=charts.series.bars.Round3D.magicTrigFunctionX(pointRatio);
var ySteps=charts.series.bars.Round3D.magicTrigFunctionY(pointRatio);
var pointX=centerX+xSteps*xRadius;
var pointY=centerY+ySteps*yRadius;
this.graphics.lineTo(pointX,pointY);
}
return 1;
},
];},["GetColours","magicTrigFunctionX","magicTrigFunctionY"],["charts.series.bars.Base","flash.filters.DropShadowFilter","ScreenCoords","flash.geom.Matrix","Math"], "0.8.0", "0.8.4"
);
// class charts.series.bars.RoundGlass
joo.classLoader.prepare("package charts.series.bars",
"public class RoundGlass extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Math);},

"public function RoundGlass",function(index,props,group){
this.super$8(index,props,group);
var dropShadow=new flash.filters.DropShadowFilter();
dropShadow.blurX=5;
dropShadow.blurY=5;
dropShadow.distance=3;
dropShadow.angle=45;
dropShadow.quality=2;
dropShadow.alpha=0.4;
this.filters=[dropShadow];
},
"public override function resize",function(sc){
this.graphics.clear();
var h=this.resize_helper(as(sc,ScreenCoords));
this.bg$8(h.width,h.height,h.upside_down);
this.glass$8(h.width,h.height,h.upside_down);
},
"private function bg",function(w,h,upside_down){
var rad=w/3;
if(rad>(w/2))
rad=w/2;
this.graphics.lineStyle(0,0,0);
var bgcolors=charts.series.bars.RoundGlass.GetColours(this.colour);
var bgalphas=[1,1];
var bgratios=[0,255];
var bgmatrix=new flash.geom.Matrix();
var xRadius;
var yRadius;
var x;
var y;
bgmatrix.createGradientBox(w,h,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
if(!upside_down&&h>0)
{
if(h>=w/2)
{
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.moveTo(0,w/2);
this.graphics.lineTo(0,h);
this.graphics.lineTo(w,h);
this.graphics.lineTo(w,w/2);
x=w/2;
y=w/2;
xRadius=w/2;
yRadius=xRadius;
this.halfEllipse(x,y,xRadius,yRadius,100,true);
}
else
{
x=w/2;
y=h;
xRadius=w/2;
yRadius=h;
this.halfEllipse(x,y,xRadius,yRadius,100,true);
}
}
else
{
if(h>=w/2)
{
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.moveTo(0,0);
this.graphics.lineTo(0,h-w/2);
this.graphics.lineTo(w,h-w/2);
this.graphics.lineTo(w,0);
x=w/2;
y=h-w/2;
xRadius=w/2;
yRadius=xRadius;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
}
else
{
if(h>0)
{
x=w/2;
y=0;
xRadius=w/2;
yRadius=h;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
}
else
{
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
this.graphics.moveTo(0,-0.05*w);
this.graphics.lineTo(0,h+0.05*w);
this.graphics.lineTo(w,h+0.05*w);
this.graphics.lineTo(w,-0.05*w);
}
}
}
this.graphics.endFill();
},
"private function glass",function(w,h,upside_down){
this.graphics.lineStyle(0,0,0);
var bgcolors=charts.series.bars.RoundGlass.GetColours(this.colour);
var bgalphas=[1,1];
var bgratios=[0,255];
var bgmatrix=new flash.geom.Matrix();
bgmatrix.createGradientBox(w,h,(180/180)*Math.PI);
var colors=[0xFFFFFF,0xFFFFFF];
var alphas=[0,0.75];
var ratios=[100,255];
var xRadius;
var yRadius;
var x;
var y;
var matrix=new flash.geom.Matrix();
matrix.createGradientBox(this.width,this.height,(180/180)*Math.PI);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
var rad=w/3;
if(!upside_down&&h>0)
{
if(h>=w/2)
{
this.graphics.moveTo(0+(0.05*w),w/2);
this.graphics.lineTo(0+(0.05*w),h-(0.05*w));
this.graphics.lineTo(w-(0.05*w),h-(0.05*w));
this.graphics.lineTo(w-(0.05*w),w/2);
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
x=w/2;
y=w/2;
xRadius=w/2;
yRadius=xRadius;
this.halfEllipse(x,y,xRadius,yRadius,100,true);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
x=w/3;
y=w/2;
xRadius=w/3-(0.05*w);
yRadius=xRadius+(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,true);
}
else
{
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
x=w/3;
y=h-(0.05*w);
xRadius=w/3-(0.05*w);
yRadius=h-2.5*(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,true);
}
}
else
{
if(h>=w/2)
{
this.graphics.moveTo(0+(0.05*w),0+(0.05*w));
this.graphics.lineTo(0+(0.05*w),h-w/2);
this.graphics.lineTo(w-(0.05*w),h-w/2);
this.graphics.lineTo(w-(0.05*w),0+(0.05*w));
this.graphics.beginGradientFill('linear',bgcolors,bgalphas,bgratios,bgmatrix,'pad');
x=w/2;
y=h-w/2;
xRadius=w/2;
yRadius=xRadius;
this.halfEllipse(x,y,xRadius,yRadius,100,false);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
x=w/3;
y=h-w/2;
xRadius=w/3-(0.05*w);
yRadius=xRadius+(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,false);
}
else
{
if(h>0)
{
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
x=w/3;
y=0+(0.05*w);
xRadius=w/3-(0.05*w);
yRadius=h-2.5*(0.05*w);
this.halfEllipse(x,y,xRadius,yRadius,100,false);
}
else
{
this.graphics.moveTo(0+0.025*w,-0.025*w);
this.graphics.lineTo(0+0.025*w,h+0.025*w);
this.graphics.lineTo(w,h+0.025*w);
this.graphics.lineTo(w,-0.025*w);
}
}
}
this.graphics.endFill();
},
"public static function GetColours",function(col){
var rgb=col;
var red=(rgb&16711680)>>16;
var green=(rgb&65280)>>8;
var blue=rgb&255;
var shift=2;
var basecolor=col;
var highlight=col;
var bgred=(rgb&16711680)>>16;
var bggreen=(rgb&65280)>>8;
var bgblue=rgb&255;
var hired=(rgb&16711680)>>16;
var higreen=(rgb&65280)>>8;
var hiblue=rgb&255;
if(red+red/shift>255||green+green/shift>255||blue+blue/shift>255)
{
bgred=red-red/shift;
bggreen=green-green/shift;
bgblue=blue-blue/shift;
}
hired=bgred+red/shift;
hiblue=bgblue+blue/shift;
higreen=bggreen+green/shift;
basecolor=bgred<<16|bggreen<<8|bgblue;
highlight=hired<<16|higreen<<8|hiblue;
return[highlight,basecolor];
},
"public static function magicTrigFunctionX",function(pointRatio){
return Math.cos(pointRatio*2*Math.PI);
},
"public static function magicTrigFunctionY",function(pointRatio){
return Math.sin(pointRatio*2*Math.PI);
},
"public function Ellipse",function(centerX,centerY,xRadius,yRadius,sides){
this.graphics.moveTo(centerX+xRadius,centerY);
for(var i=0;i<=sides;i++){
var pointRatio=i/sides;
var xSteps=charts.series.bars.RoundGlass.magicTrigFunctionX(pointRatio);
var ySteps=charts.series.bars.RoundGlass.magicTrigFunctionY(pointRatio);
var pointX=centerX+xSteps*xRadius;
var pointY=centerY+ySteps*yRadius;
this.graphics.lineTo(pointX,pointY);
}
return 1;
},
"public function halfEllipse",function(centerX,centerY,xRadius,yRadius,sides,top){
var loopStart;
var loopEnd;
if(top==true)
{
loopStart=sides/2;
loopEnd=sides;
}
else
{
loopStart=0;
loopEnd=sides/2;
}
this.graphics.moveTo(centerX+xRadius,centerY);
for(var i=loopStart;i<=loopEnd;i++){
var pointRatio=i/sides;
var xSteps=charts.series.bars.RoundGlass.magicTrigFunctionX(pointRatio);
var ySteps=charts.series.bars.RoundGlass.magicTrigFunctionY(pointRatio);
var pointX=centerX+xSteps*xRadius;
var pointY=centerY+ySteps*yRadius;
this.graphics.lineTo(pointX,pointY);
}
return 1;
},
];},["GetColours","magicTrigFunctionX","magicTrigFunctionY"],["charts.series.bars.Base","flash.filters.DropShadowFilter","ScreenCoords","flash.geom.Matrix","Math"], "0.8.0", "0.8.4"
);
// class charts.series.bars.Sketch
joo.classLoader.prepare("package charts.series.bars",
"public class Sketch extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[
"private var",{outline:NaN},
"private var",{offset:NaN},
"public function Sketch",function(index,props,group){
this.super$8(index,props,group);
this.outline$8=props.get_colour('outline-colour');
this.offset$8=props.get('offset');
},
"public override function resize",function(sc){
var h=this.resize_helper(as(sc,ScreenCoords));
var offset=this.offset$8;
var o2=offset/2;
var strokes=6;
var l_width=h.width/strokes;
this.graphics.clear();
this.graphics.lineStyle(l_width+1,this.colour,0.85,true,"none","round","miter",0.8);
for(var i=0;i<strokes;i++)
{
this.graphics.moveTo(((l_width*i)+(l_width/2))+(Math.random()*offset-o2),2+(Math.random()*offset-o2));
this.graphics.lineTo(((l_width*i)+(l_width/2))+(Math.random()*offset-o2),h.height-2+(Math.random()*offset-o2));
}
this.graphics.lineStyle(2,this.outline$8,1);
this.graphics.moveTo(Math.random()*offset-o2,Math.random()*offset-o2);
this.graphics.lineTo(Math.random()*offset-o2,h.height+Math.random()*offset-o2);
this.graphics.moveTo(Math.random()*offset-o2,Math.random()*offset-o2);
this.graphics.lineTo(h.width+(Math.random()*offset-o2),Math.random()*offset-o2);
this.graphics.moveTo(h.width+(Math.random()*offset-o2),Math.random()*offset-o2);
this.graphics.lineTo(h.width+(Math.random()*offset-o2),h.height+(Math.random()*offset-o2));
this.graphics.moveTo(Math.random()*offset-o2,h.height+(Math.random()*offset-o2));
this.graphics.lineTo(h.width+(Math.random()*offset-o2),h.height+(Math.random()*offset-o2));
},
];},[],["charts.series.bars.Base","ScreenCoords","Math"], "0.8.0", "0.8.4"
);
// class charts.series.bars.Stack
joo.classLoader.prepare("package charts.series.bars",
"public class Stack extends charts.series.bars.Base",8,function($$private){var as=joo.as;return[
"private var",{total:NaN},
"public function Stack",function(index,props,group){
this.total$8=props.get('total');
this.super$8(index,props,group);
},
"protected override function replace_magic_values",function(t){
t=this.replace_magic_values$8(t);
t=t.replace('#total#',NumberUtils.formatNumber(this.total$8));
return t;
},
"public function replace_x_axis_label",function(t){
this.tooltip=this.tooltip.replace('#x_label#',t);
},
"public override function resize",function(sc){
var h=this.resize_helper(as(sc,ScreenCoords));
this.graphics.clear();
this.graphics.beginFill(this.colour,1.0);
this.graphics.moveTo(0,0);
this.graphics.lineTo(h.width,0);
this.graphics.lineTo(h.width,h.height);
this.graphics.lineTo(0,h.height);
this.graphics.lineTo(0,0);
this.graphics.endFill();
},
];},[],["charts.series.bars.Base","NumberUtils","ScreenCoords"], "0.8.0", "0.8.4"
);
// class charts.series.bars.StackCollection
joo.classLoader.prepare("package charts.series.bars",
"public class StackCollection extends charts.series.Element",7,function($$private){var as=joo.as,is=joo.is;return[
"protected var",{tip_pos:null},
"private var",{vals:null},
"public var",{colours:null},
"protected var",{group:NaN},
"private var",{total:NaN},
"public function StackCollection",function(index,props,group){this.super$7();
this.tooltip=props.get('tip');
this.index=index;
var item;
this.vals$7=as(props.get('values'),Array);
this.total$7=0;
for(var $1 in this.vals$7){item=this.vals$7[$1];
if(item!=null){
if(is(item,Number))
this.total$7+=item;
else
this.total$7+=item.val;
}
}
this.colours=new Array();
for(var $2 in props.get('colours')){var colour=props.get('colours')[$2];
this.colours.push(string.Utils.get_colour(colour));}
this.group=group;
this.visible=true;
var prop;
var n;
var bottom=0;
var top=0;
var colr;
var count=0;
for(var $3 in this.vals$7)
{item=this.vals$7[$3];
if(item!=null)
{
colr=this.colours[(count%this.colours.length)];
var defaul_stack_props=new Properties({
bottom:bottom,
colour:colr,
total:this.total$7
},props);
if(is(item,Number)){
item={val:item};
}
if(item==null){
item={val:null};
}
top+=item.val;
item.top=top;
var stack_props=new Properties(item,defaul_stack_props);
var p=new charts.series.bars.Stack(index,stack_props,group);
this.addChild(p);
bottom=top;
count++;
}
}
},
"public override function resize",function(sc){
for(var i=0;i<this.numChildren;i++)
{
var e=as(this.getChildAt(i),charts.series.Element);
e.resize(sc);
}
},
"public override function get_mid_point",function(){
var e=as(this.getChildAt(0),charts.series.Element);
return e.get_mid_point();
},
"public function get_children",function(){
var tmp=[];
for(var i=0;i<this.numChildren;i++){
tmp.push(this.getChildAt(i));
}
return tmp;
},
"public override function get_tip_pos",function(){
var e=as(this.getChildAt(this.numChildren-1),charts.series.Element);
return e.get_tip_pos();
},
"public override function get_tooltip",function(){
for(var i=0;i<this.numChildren;i++)
{
var e=as(this.getChildAt(i),charts.series.Element);
if(e.is_tip)
{
return e.get_tooltip();
}
}
return this.tooltip;
},
"public override function tooltip_replace_labels",function(labels){
for(var i=0;i<this.numChildren;i++){
var e=as(this.getChildAt(i),charts.series.bars.Stack);
e.replace_x_axis_label(labels.get(this.index));
}
},
];},[],["charts.series.Element","Array","Number","string.Utils","Properties","charts.series.bars.Stack"], "0.8.0", "0.8.4"
);
// class charts.series.dots.anchor
joo.classLoader.prepare("package charts.series.dots",
"public class anchor extends charts.series.dots.PointDotBase",8,function($$private){;return[function(){joo.classLoader.init(flash.display.BlendMode);},
"public function anchor",function(index,value){
var colour=string.Utils.get_colour(value.get('colour'));
this.super$8(index,value);
this.tooltip=this.replace_magic_values(value.get('tip'));
this.attach_events();
if(value.get('hollow'))
{
if(value.has('background-colour'))
{
var bgColor=string.Utils.get_colour(value.get('background-colour'));
}
else
{
bgColor=colour;
}
this.graphics.beginFill(bgColor,value.get('background-alpha'));
}
else
{
this.graphics.beginFill(colour,value.get('alpha'));
}
this.graphics.lineStyle(value.get('width'),colour,value.get('alpha'));
this.drawAnchor$8(this.graphics,this.radius,value.get('sides'),this.rotation);
if(value.get('halo-size')>0)
{
var size=value.get('halo-size')+this.radius;
var s=new flash.display.Sprite();
s.graphics.lineStyle(0,0,0);
s.graphics.beginFill(0,1);
this.drawAnchor$8(s.graphics,size,value.get('sides'),this.rotation);
s.blendMode=flash.display.BlendMode.ERASE;
s.graphics.endFill();
this.line_mask=s;
}
},
"public override function set_tip",function(b){
if(b)
{
if(!this.is_tip)
{
caurina.transitions.Tweener.addTween(this,{scaleX:1.3,time:0.4,transition:"easeoutbounce"});
caurina.transitions.Tweener.addTween(this,{scaleY:1.3,time:0.4,transition:"easeoutbounce"});
if(this.line_mask!=null)
{
caurina.transitions.Tweener.addTween(this.line_mask,{scaleX:1.3,time:0.4,transition:"easeoutbounce"});
caurina.transitions.Tweener.addTween(this.line_mask,{scaleY:1.3,time:0.4,transition:"easeoutbounce"});
}
}
this.is_tip=true;
}
else
{
caurina.transitions.Tweener.removeTweens(this);
caurina.transitions.Tweener.removeTweens(this.line_mask);
this.scaleX=1;
this.scaleY=1;
if(this.line_mask!=null)
{
this.line_mask.scaleX=1;
this.line_mask.scaleY=1;
}
this.is_tip=false;
}
},
"private function drawAnchor",function(aGraphics,aRadius,
aSides,aRotation)
{
if(aSides<3)aSides=3;
if(aSides>360)aSides=360;
var angle=360/aSides;
for(var ix=0;ix<=aSides;ix++)
{
var degrees=-90+aRotation+(ix%aSides)*angle;
var xVal=this.calcXOnCircle(aRadius,degrees);
var yVal=this.calcYOnCircle(aRadius,degrees);
if(ix==0)
{
aGraphics.moveTo(xVal,yVal);
}
else
{
aGraphics.lineTo(xVal,yVal);
}
}
},
];},[],["charts.series.dots.PointDotBase","string.Utils","flash.display.Sprite","flash.display.BlendMode","caurina.transitions.Tweener"], "0.8.0", "0.8.4"
);
// class charts.series.dots.bow
joo.classLoader.prepare("package charts.series.dots",
"public class bow extends charts.series.dots.PointDotBase",8,function($$private){;return[function(){joo.classLoader.init(flash.display.BlendMode);},
"public function bow",function(index,value){
var colour=string.Utils.get_colour(value.get('colour'));
this.super$8(index,value);
this.tooltip=this.replace_magic_values(value.get('tip'));
this.attach_events();
if(value.get('hollow'))
{
if(value.get('background-colour')!=null)
{
var bgColor=string.Utils.get_colour(value.get('background-colour'));
}
else
{
bgColor=colour;
}
this.graphics.beginFill(bgColor,value.get('background-alpha'));
}
else
{
this.graphics.beginFill(colour,value.get('alpha'));
}
this.graphics.lineStyle(value.get('width'),colour,value.get('alpha'));
this.draw$8(this.graphics,this.radius,value.get('rotation'));
if(value.get('halo-size')>0)
{
var s=new flash.display.Sprite();
s.graphics.lineStyle(0,0,0);
s.graphics.beginFill(0,1);
this.draw$8(s.graphics,value.get('halo-size')+this.radius,value.get('rotation'));
s.blendMode=flash.display.BlendMode.ERASE;
s.graphics.endFill();
this.line_mask=s;
}
},
"public override function set_tip",function(b){
if(b)
{
if(!this.is_tip)
{
caurina.transitions.Tweener.addTween(this,{scaleX:1.3,time:0.4,transition:"easeoutbounce"});
caurina.transitions.Tweener.addTween(this,{scaleY:1.3,time:0.4,transition:"easeoutbounce"});
if(this.line_mask!=null)
{
caurina.transitions.Tweener.addTween(this.line_mask,{scaleX:1.3,time:0.4,transition:"easeoutbounce"});
caurina.transitions.Tweener.addTween(this.line_mask,{scaleY:1.3,time:0.4,transition:"easeoutbounce"});
}
}
this.is_tip=true;
}
else
{
caurina.transitions.Tweener.removeTweens(this);
caurina.transitions.Tweener.removeTweens(this.line_mask);
this.scaleX=1;
this.scaleY=1;
if(this.line_mask!=null)
{
this.line_mask.scaleX=1;
this.line_mask.scaleY=1;
}
this.is_tip=false;
}
},
"private function draw",function(aGraphics,aRadius,aRotation)
{
var angle=60;
aGraphics.moveTo(0,0);
var degrees=-90+aRotation+angle;
var xVal=this.calcXOnCircle(aRadius,degrees);
var yVal=this.calcYOnCircle(aRadius,degrees);
aGraphics.lineTo(xVal,yVal);
degrees+=angle;
xVal=this.calcXOnCircle(aRadius,degrees);
yVal=this.calcYOnCircle(aRadius,degrees);
aGraphics.lineTo(xVal,yVal);
aGraphics.lineTo(xVal,yVal);
degrees=-90+aRotation-angle;
xVal=this.calcXOnCircle(aRadius,degrees);
yVal=this.calcYOnCircle(aRadius,degrees);
aGraphics.lineTo(xVal,yVal);
degrees-=angle;
xVal=this.calcXOnCircle(aRadius,degrees);
yVal=this.calcYOnCircle(aRadius,degrees);
aGraphics.lineTo(xVal,yVal);
aGraphics.lineTo(xVal,yVal);
},
];},[],["charts.series.dots.PointDotBase","string.Utils","flash.display.Sprite","flash.display.BlendMode","caurina.transitions.Tweener"], "0.8.0", "0.8.4"
);
// class charts.series.dots.DefaultDotProperties
joo.classLoader.prepare("package charts.series.dots",
"public class DefaultDotProperties extends Properties",2,function($$private){;return[

"public function DefaultDotProperties",function(json,colour,axis){
var parent=new Properties({
axis:axis,
'type':'dot',
'dot-size':5,
'halo-size':2,
'colour':colour,
'tip':'#val#',
alpha:1,
rotation:0,
sides:3,
width:1
});
this.super$2(json,parent);
tr.aces('4',this.get('axis'));
},
];},[],["Properties","tr"], "0.8.0", "0.8.4"
);
// class charts.series.dots.dot_factory
joo.classLoader.prepare("package charts.series.dots",
"public class dot_factory",1,function($$private){;return[
"public static function make",function(index,style){
switch(style.get('type'))
{
case'star':
return new charts.series.dots.star(index,style);
break;
case'bow':
return new charts.series.dots.bow(index,style);
break;
case'anchor':
return new charts.series.dots.anchor(index,style);
break;
case'dot':
return new charts.series.dots.Point(index,style);
break;
case'solid-dot':
return new charts.series.dots.PointDot(index,style);
break;
case'hollow-dot':
return new charts.series.dots.Hollow(index,style);
break;
default:
return new charts.series.dots.Point(index,style);
break;
}
},
];},["make"],["charts.series.dots.star","charts.series.dots.bow","charts.series.dots.anchor","charts.series.dots.Point","charts.series.dots.PointDot","charts.series.dots.Hollow"], "0.8.0", "0.8.4"
);
// class charts.series.dots.Hollow
joo.classLoader.prepare("package charts.series.dots",
"public class Hollow extends charts.series.dots.PointDotBase",8,function($$private){;return[function(){joo.classLoader.init(flash.display.BlendMode);},
"public function Hollow",function(index,style){
this.super$8(index,style);
var colour=string.Utils.get_colour(style.get('colour'));
this.graphics.clear();
this.graphics.lineStyle(0,0,0);
this.graphics.beginFill(colour,1);
this.graphics.drawCircle(0,0,style.get('dot-size'));
this.graphics.drawCircle(0,0,style.get('dot-size')-style.get('width'));
this.graphics.endFill();
this.graphics.lineStyle(0,0,0);
this.graphics.beginFill(0,0);
this.graphics.drawCircle(0,0,style.get('dot-size'));
this.graphics.endFill();
var s=new flash.display.Sprite();
s.graphics.lineStyle(0,0,0);
s.graphics.beginFill(0,1);
s.graphics.drawCircle(0,0,style.get('dot-size')+style.get('halo-size'));
s.blendMode=flash.display.BlendMode.ERASE;
this.line_mask=s;
this.attach_events();
},
];},[],["charts.series.dots.PointDotBase","string.Utils","flash.display.Sprite","flash.display.BlendMode"], "0.8.0", "0.8.4"
);
// class charts.series.dots.Point
joo.classLoader.prepare("package charts.series.dots",
"public class Point extends charts.series.dots.PointDotBase",8,function($$private){;return[function(){joo.classLoader.init(flash.display.BlendMode);},
"public function Point",function(index,style)
{
this.super$8(index,style);
var colour=string.Utils.get_colour(style.get('colour'));
this.graphics.lineStyle(0,0,0);
this.graphics.beginFill(colour,1);
this.graphics.drawCircle(0,0,style.get('dot-size'));
this.visible=false;
this.attach_events();
var s=new flash.display.Sprite();
s.graphics.lineStyle(0,0,0);
s.graphics.beginFill(0,1);
s.graphics.drawCircle(0,0,style.get('dot-size')+style.get('halo-size'));
s.blendMode=flash.display.BlendMode.ERASE;
s.visible=false;
this.line_mask=s;
},
"public override function set_tip",function(b){
this.visible=b;
this.line_mask.visible=b;
},
];},[],["charts.series.dots.PointDotBase","string.Utils","flash.display.Sprite","flash.display.BlendMode"], "0.8.0", "0.8.4"
);
// class charts.series.dots.PointDot
joo.classLoader.prepare("package charts.series.dots",
"public class PointDot extends charts.series.dots.PointDotBase",8,function($$private){;return[
"public function PointDot",function(index,style){
this.super$8(index,style);
var colour=string.Utils.get_colour(style.get('colour'));
this.graphics.lineStyle(0,0,0);
this.graphics.beginFill(colour,1);
this.graphics.drawCircle(0,0,style.get('dot-size'));
this.graphics.endFill();
var s=new flash.display.Sprite();
s.graphics.lineStyle(0,0,0);
s.graphics.beginFill(0xFFFFFF,1);
s.graphics.drawCircle(0,0,style.get('dot-size')+style.get('halo-size'));
this.line_mask=s;
this.attach_events();
},
];},[],["charts.series.dots.PointDotBase","string.Utils","flash.display.Sprite"], "0.8.0", "0.8.4"
);
// class charts.series.dots.PointDotBase
joo.classLoader.prepare("package charts.series.dots",
"public class PointDotBase extends charts.series.Element",7,function($$private){;return[function(){joo.classLoader.init(caurina.transitions.Equations,Number,Math);},
"protected var",{radius:NaN},
"protected var",{colour:NaN},
"private var",{on_show_animate:false},
"protected var",{on_show:null},
"public function PointDotBase",function(index,props){
this.super$7();
this.is_tip=false;
this.visible=true;
this.on_show_animate$7=true;
this.on_show=props.get('on-show');
if(!props.has('y'))
props.set('y',props.get('value'));
this._y=props.get('y');
if(!props.has('x'))
{
this.index=this._x=index;
}
else
{
this._x=props.get('x');
this.index=Number.MIN_VALUE;
}
this.radius=props.get('dot-size');
this.tooltip=this.replace_magic_values(props.get('tip'));
if(props.has('on-click'))
this.set_on_click(props.get('on-click'));
if(props.has('axis'))
if(props.get('axis')=='right')
this.right_axis=true;
},
"public override function resize",function(sc){
var x;
var y;
if(this.index!=Number.MIN_VALUE){
var p=sc.get_get_x_from_pos_and_y_from_val(this.index,this._y,this.right_axis);
x=p.x;
y=p.y;
}
else
{
x=sc.get_x_from_val(this._x);
y=sc.get_y_from_val(this._y,this.right_axis);
}
if(this.line_mask!=null)
{
this.line_mask.x=x;
this.line_mask.y=y;
}
if(this.on_show_animate$7)
this.first_show(x,y,sc);
else{
this.y=y;
this.x=x;
}
},
"public function is_tweening",function(){
return caurina.transitions.Tweener.isTweening(this);
},
"protected function first_show",function(x,y,sc){
this.on_show_animate$7=false;
caurina.transitions.Tweener.removeTweens(this);
var d=x/this.stage.stageWidth;
d*=this.on_show.get('cascade');
d+=this.on_show.get('delay');
switch(this.on_show.get('type')){
case'pop-up':
this.x=x;
this.y=sc.get_y_bottom(this.right_axis);
caurina.transitions.Tweener.addTween(this,{y:y,time:1.4,delay:d,transition:caurina.transitions.Equations.easeOutQuad});
if(this.line_mask!=null)
{
this.line_mask.x=x;
this.line_mask.y=sc.get_y_bottom(this.right_axis);
caurina.transitions.Tweener.addTween(this.line_mask,{y:y,time:1.4,delay:d,transition:caurina.transitions.Equations.easeOutQuad});
}
break;
case'explode':
this.x=this.stage.stageWidth/2;
this.y=this.stage.stageHeight/2;
caurina.transitions.Tweener.addTween(this,{y:y,x:x,time:1.4,delay:d,transition:caurina.transitions.Equations.easeOutQuad});
if(this.line_mask!=null)
{
this.line_mask.x=this.stage.stageWidth/2;
this.line_mask.y=this.stage.stageHeight/2;
caurina.transitions.Tweener.addTween(this.line_mask,{y:y,x:x,time:1.4,delay:d,transition:caurina.transitions.Equations.easeOutQuad});
}
break;
case'mid-slide':
this.x=x;
this.y=this.stage.stageHeight/2;
this.alpha=0.2;
caurina.transitions.Tweener.addTween(this,{alpha:1,y:y,time:1.4,delay:d,transition:caurina.transitions.Equations.easeOutQuad});
if(this.line_mask!=null)
{
this.line_mask.x=x;
this.line_mask.y=this.stage.stageHeight/2;
caurina.transitions.Tweener.addTween(this.line_mask,{y:y,time:1.4,delay:d,transition:caurina.transitions.Equations.easeOutQuad});
}
break;
case'drop':
this.x=x;
this.y=-this.height-10;
caurina.transitions.Tweener.addTween(this,{y:y,time:1.4,delay:d,transition:caurina.transitions.Equations.easeOutBounce});
if(this.line_mask!=null)
{
this.line_mask.x=x;
this.line_mask.y=-this.height-10;
caurina.transitions.Tweener.addTween(this.line_mask,{y:y,time:1.4,delay:d,transition:caurina.transitions.Equations.easeOutQuad});
}
break;
case'fade-in':
this.x=x;
this.y=y;
this.alpha=0;
caurina.transitions.Tweener.addTween(this,{alpha:1,time:1.2,delay:d,transition:caurina.transitions.Equations.easeOutQuad});
break;
case'shrink-in':
this.x=x;
this.y=y;
this.scaleX=this.scaleY=5;
this.alpha=0;
caurina.transitions.Tweener.addTween(
this,
{
scaleX:1,scaleY:1,alpha:1,time:1.2,
delay:d,transition:caurina.transitions.Equations.easeOutQuad,
onComplete:function(){tr.ace('Fin');}
});
break;
default:
this.y=y;
this.x=x;
}
},
"public override function set_tip",function(b){
if(b){
this.scaleY=this.scaleX=1.3;
this.line_mask.scaleY=this.line_mask.scaleX=1.3;
}
else{
this.scaleY=this.scaleX=1;
this.line_mask.scaleY=this.line_mask.scaleX=1;
}
},
"protected function replace_magic_values",function(t){
t=t.replace('#val#',NumberUtils.formatNumber(this._y));
t=t.replace('#x#',NumberUtils.formatNumber(this._x));
t=t.replace('#y#',NumberUtils.formatNumber(this._y));
t=t.replace('#size#',NumberUtils.formatNumber(this.radius));
t=string.DateUtils.replace_magic_values(t,this._x);
return t;
},
"protected function calcXOnCircle",function(aRadius,aDegrees)
{
return aRadius*Math.cos(aDegrees/180*Math.PI);
},
"protected function calcYOnCircle",function(aRadius,aDegrees)
{
return aRadius*Math.sin(aDegrees/180*Math.PI);
},
];},[],["charts.series.Element","Number","caurina.transitions.Tweener","caurina.transitions.Equations","tr","NumberUtils","string.DateUtils","Math"], "0.8.0", "0.8.4"
);
// class charts.series.dots.scat
joo.classLoader.prepare("package charts.series.dots",
"public class scat extends charts.series.dots.PointDotBase",8,function($$private){;return[function(){joo.classLoader.init(flash.display.BlendMode);},
"public function scat",function(style){
style.value=style.y;
this.super$8(-99,new Properties({}));
this._x=style.x;
this._y=style.y;
this.visible=true;
if(style.alpha==null)
style.alpha=1;
this.tooltip=this.replace_magic_values(style.tip);
this.attach_events();
if(style.x==null)
{
this.visible=false;
}
else
{
var haloSize=isNaN(style['halo-size'])?0:style['halo-size'];
var isHollow=style['hollow'];
if(isHollow)
{
if(style['background-colour']!=null)
{
var bgColor=string.Utils.get_colour(style['background-colour']);
}
else
{
bgColor=style.colour;
}
var bgAlpha=isNaN(style['background-alpha'])?0:style['background-alpha'];
this.graphics.beginFill(bgColor,bgAlpha);
}
else
{
this.graphics.beginFill(style.colour,style.alpha);
}
switch(style['type'])
{
case'dot':
this.graphics.lineStyle(0,0,0);
this.graphics.beginFill(style.colour,style.alpha);
this.graphics.drawCircle(0,0,style['dot-size']);
this.graphics.endFill();
var s=new flash.display.Sprite();
s.graphics.lineStyle(0,0,0);
s.graphics.beginFill(0,1);
s.graphics.drawCircle(0,0,style['dot-size']+haloSize);
s.blendMode=flash.display.BlendMode.ERASE;
this.line_mask=s;
break;
case'anchor':
this.graphics.lineStyle(style.width,style.colour,style.alpha);
var rotation=isNaN(style['rotation'])?0:style['rotation'];
var sides=Math.max(3,isNaN(style['sides'])?3:style['sides']);
this.drawAnchor$8(this.graphics,this.radius,sides,rotation);
if(haloSize>0)
{
haloSize+=this.radius;
s=new flash.display.Sprite();
s.graphics.lineStyle(0,0,0);
s.graphics.beginFill(0,1);
this.drawAnchor$8(s.graphics,haloSize,sides,rotation);
s.blendMode=flash.display.BlendMode.ERASE;
s.graphics.endFill();
this.line_mask=s;
}
break;
case'bow':
this.graphics.lineStyle(style.width,style.colour,style.alpha);
rotation=isNaN(style['rotation'])?0:style['rotation'];
this.drawBow$8(this.graphics,this.radius,rotation);
if(haloSize>0)
{
haloSize+=this.radius;
s=new flash.display.Sprite();
s.graphics.lineStyle(0,0,0);
s.graphics.beginFill(0,1);
this.drawBow$8(s.graphics,haloSize,rotation);
s.blendMode=flash.display.BlendMode.ERASE;
s.graphics.endFill();
this.line_mask=s;
}
break;
case'star':
this.graphics.lineStyle(style.width,style.colour,style.alpha);
rotation=isNaN(style['rotation'])?0:style['rotation'];
this.drawStar_2$8(this.graphics,this.radius,rotation);
if(haloSize>0)
{
haloSize+=this.radius;
s=new flash.display.Sprite();
s.graphics.lineStyle(0,0,0);
s.graphics.beginFill(0,1);
this.drawStar_2$8(s.graphics,haloSize,rotation);
s.blendMode=flash.display.BlendMode.ERASE;
s.graphics.endFill();
this.line_mask=s;
}
break;
default:
this.graphics.drawCircle(0,0,this.radius);
this.graphics.drawCircle(0,0,this.radius-1);
this.graphics.endFill();
}
}
},
"public override function set_tip",function(b){
if(b)
{
if(!this.is_tip)
{
caurina.transitions.Tweener.addTween(this,{scaleX:1.3,time:0.4,transition:"easeoutbounce"});
caurina.transitions.Tweener.addTween(this,{scaleY:1.3,time:0.4,transition:"easeoutbounce"});
if(this.line_mask!=null)
{
caurina.transitions.Tweener.addTween(this.line_mask,{scaleX:1.3,time:0.4,transition:"easeoutbounce"});
caurina.transitions.Tweener.addTween(this.line_mask,{scaleY:1.3,time:0.4,transition:"easeoutbounce"});
}
}
this.is_tip=true;
}
else
{
caurina.transitions.Tweener.removeTweens(this);
caurina.transitions.Tweener.removeTweens(this.line_mask);
this.scaleX=1;
this.scaleY=1;
if(this.line_mask!=null)
{
this.line_mask.scaleX=1;
this.line_mask.scaleY=1;
}
this.is_tip=false;
}
},
"public override function resize",function(sc){
this.x=sc.get_x_from_val(this._x);
this.y=sc.get_y_from_val(this._y,this.right_axis);
if(this.line_mask!=null)
{
this.line_mask.x=this.x;
this.line_mask.y=this.y;
}
},
"private function drawAnchor",function(aGraphics,aRadius,
aSides,aRotation)
{
if(aSides<3)aSides=3;
if(aSides>360)aSides=360;
var angle=360/aSides;
for(var ix=0;ix<=aSides;ix++)
{
var degrees=-90+aRotation+(ix%aSides)*angle;
var xVal=this.calcXOnCircle(aRadius,degrees);
var yVal=this.calcYOnCircle(aRadius,degrees);
if(ix==0)
{
aGraphics.moveTo(xVal,yVal);
}
else
{
aGraphics.lineTo(xVal,yVal);
}
}
},
"private function drawBow",function(aGraphics,aRadius,
aRotation)
{
var angle=60;
aGraphics.moveTo(0,0);
var degrees=-90+aRotation+angle;
var xVal=this.calcXOnCircle(aRadius,degrees);
var yVal=this.calcYOnCircle(aRadius,degrees);
aGraphics.lineTo(xVal,yVal);
degrees+=angle;
xVal=this.calcXOnCircle(aRadius,degrees);
yVal=this.calcYOnCircle(aRadius,degrees);
aGraphics.lineTo(xVal,yVal);
aGraphics.lineTo(xVal,yVal);
degrees=-90+aRotation-angle;
xVal=this.calcXOnCircle(aRadius,degrees);
yVal=this.calcYOnCircle(aRadius,degrees);
aGraphics.lineTo(xVal,yVal);
degrees-=angle;
xVal=this.calcXOnCircle(aRadius,degrees);
yVal=this.calcYOnCircle(aRadius,degrees);
aGraphics.lineTo(xVal,yVal);
aGraphics.lineTo(xVal,yVal);
},
"private function drawStar_2",function(aGraphics,aRadius,
aRotation)
{
var angle=360/10;
var degrees=-90+aRotation;
for(var ix=0;ix<11;ix++)
{
var rad;
rad=(ix%2==0)?aRadius:aRadius/2;
var xVal=this.calcXOnCircle(rad,degrees);
var yVal=this.calcYOnCircle(rad,degrees);
if(ix==0)
{
aGraphics.moveTo(xVal,yVal);
}
else
{
aGraphics.lineTo(xVal,yVal);
}
degrees+=angle;
}
},
"private function drawStar",function(aGraphics,aRadius,
aRotation)
{
var angle=360/5;
var degrees=-90+aRotation;
for(var ix=0;ix<=5;ix++)
{
var xVal=this.calcXOnCircle(aRadius,degrees);
var yVal=this.calcYOnCircle(aRadius,degrees);
if(ix==0)
{
aGraphics.moveTo(xVal,yVal);
}
else
{
aGraphics.lineTo(xVal,yVal);
}
degrees+=(2*angle);
}
},
];},[],["charts.series.dots.PointDotBase","Properties","string.Utils","flash.display.Sprite","flash.display.BlendMode","Math","caurina.transitions.Tweener"], "0.8.0", "0.8.4"
);
// class charts.series.dots.star
joo.classLoader.prepare("package charts.series.dots",
"public class star extends charts.series.dots.PointDotBase",8,function($$private){;return[function(){joo.classLoader.init(flash.display.BlendMode);},
"public function star",function(index,value){
var colour=string.Utils.get_colour(value.get('colour'));
this.super$8(index,value);
this.tooltip=this.replace_magic_values(value.get('tip'));
this.attach_events();
if(value.get('hollow'))
{
if(value.get('background-colour')!=null)
{
var bgColor=string.Utils.get_colour(value.get('background-colour'));
}
else
{
bgColor=colour;
}
this.graphics.beginFill(bgColor,value.get('background-alpha'));
}
else
{
this.graphics.beginFill(colour,value.get('alpha'));
}
this.graphics.lineStyle(value.get('width'),colour,value.get('alpha'));
this.drawStar_2$8(this.graphics,this.radius,value.get('rotation'));
this.graphics.endFill();
if(value.get('halo-size')>0)
{
var s=new flash.display.Sprite();
s.graphics.lineStyle(0,0,0);
s.graphics.beginFill(0,1);
this.drawStar_2$8(s.graphics,value.get('halo-size')+this.radius,value.get('rotation'));
s.blendMode=flash.display.BlendMode.ERASE;
s.graphics.endFill();
this.line_mask=s;
}
},
"public override function set_tip",function(b){
if(b)
{
if(!this.is_tip)
{
caurina.transitions.Tweener.addTween(this,{scaleX:1.3,time:0.4,transition:"easeoutbounce"});
caurina.transitions.Tweener.addTween(this,{scaleY:1.3,time:0.4,transition:"easeoutbounce"});
if(this.line_mask!=null)
{
caurina.transitions.Tweener.addTween(this.line_mask,{scaleX:1.3,time:0.4,transition:"easeoutbounce"});
caurina.transitions.Tweener.addTween(this.line_mask,{scaleY:1.3,time:0.4,transition:"easeoutbounce"});
}
}
this.is_tip=true;
}
else
{
caurina.transitions.Tweener.removeTweens(this);
caurina.transitions.Tweener.removeTweens(this.line_mask);
this.scaleX=1;
this.scaleY=1;
if(this.line_mask!=null)
{
this.line_mask.scaleX=1;
this.line_mask.scaleY=1;
}
this.is_tip=false;
}
},
"private function drawStar_2",function(aGraphics,aRadius,
aRotation)
{
var angle=360/10;
var degrees=-90+aRotation;
for(var ix=0;ix<11;ix++)
{
var rad;
rad=(ix%2==0)?aRadius:aRadius/2;
var xVal=this.calcXOnCircle(rad,degrees);
var yVal=this.calcYOnCircle(rad,degrees);
if(ix==0)
{
aGraphics.moveTo(xVal,yVal);
}
else
{
aGraphics.lineTo(xVal,yVal);
}
degrees+=angle;
}
},
];},[],["charts.series.dots.PointDotBase","string.Utils","flash.display.Sprite","flash.display.BlendMode","caurina.transitions.Tweener"], "0.8.0", "0.8.4"
);
// class charts.series.Element
joo.classLoader.prepare("package charts.series",
"public class Element extends flash.display.Sprite implements charts.series.has_tooltip",6,function($$private){var is=joo.is,$$bound=joo.boundMethod,trace=joo.trace;return[function(){joo.classLoader.init(caurina.transitions.Equations,flash.events.MouseEvent);},
"public var",{_x:NaN},
"public var",{_y:NaN},
"public var",{index:NaN},
"protected var",{tooltip:null},
"private var",{link:null},
"public var",{is_tip:false},
"public var",{line_mask:null},
"protected var",{right_axis:false},
"public function Element",function()
{this.super$6();
this.cacheAsBitmap=true;
this.right_axis=false;
},
"public function resize",function(sc){
var p=sc.get_get_x_from_pos_and_y_from_val(this._x,this._y,this.right_axis);
this.x=p.x;
this.y=p.y;
},
"public function get_mid_point",function(){
return new flash.geom.Point(this.x,this.y);
},
"public function get_x",function(){
return this._x;
},
"public function set_tip",function(b){},
"protected function attach_events",function(){
this.addEventListener(flash.events.MouseEvent.MOUSE_OVER,$$bound(this,"mouseOver"),false,0,true);
this.addEventListener(flash.events.MouseEvent.MOUSE_OUT,$$bound(this,"mouseOut"),false,0,true);
},
"public function mouseOver",function(event){
this.pulse();
},
"public function pulse",function(){
caurina.transitions.Tweener.addTween(this,{alpha:.5,time:0.4,transition:"linear"});
caurina.transitions.Tweener.addTween(this,{alpha:1,time:0.4,delay:0.4,onComplete:$$bound(this,"pulse"),transition:"linear"});
},
"public function mouseOut",function(event){
caurina.transitions.Tweener.removeTweens(this);
caurina.transitions.Tweener.addTween(this,{alpha:1,time:0.4,transition:caurina.transitions.Equations.easeOutElastic});
},
"public function set_on_click",function(s){
this.link$6=s;
this.buttonMode=true;
this.useHandCursor=true;
this.addEventListener(flash.events.MouseEvent.MOUSE_UP,$$bound(this,"mouseUp$6"),false,0,true);
},
"private function mouseUp",function(event){
if(this.link$6.substring(0,6)=='trace:'){
tr.ace(this.link$6);
}
else if(this.link$6.substring(0,5)=='http:')
this.browse_url$6(this.link$6);
else if(this.link$6.substring(0,6)=='https:')
this.browse_url$6(this.link$6);
else{
flash.external.ExternalInterface.call(this.link$6,this.index);
}
},
"private function browse_url",function(url){
var req=new flash.net.URLRequest(this.link$6);
try
{
flash.net.navigateToURL(req);
}
catch(e){if(is(e,Error))
{
trace("Error opening link: "+this.link$6);
}else throw e;}
},
"public function get_tip_pos",function(){
return{x:this.x,y:this.y};
},
"public function get_tooltip",function(){
return this.tooltip;
},
"public function tooltip_replace_labels",function(labels){
tr.aces('x label',this._x,labels.get(this._x));
this.tooltip=this.tooltip.replace('#x_label#',labels.get(this._x));
},
"public function die",function(){
if(this.line_mask!=null){
this.line_mask.graphics.clear();
this.line_mask=null;
}
},
];},[],["flash.display.Sprite","charts.series.has_tooltip","flash.geom.Point","flash.events.MouseEvent","caurina.transitions.Tweener","caurina.transitions.Equations","tr","flash.external.ExternalInterface","flash.net.URLRequest","Error"], "0.8.0", "0.8.4"
);
// class charts.series.has_tooltip
joo.classLoader.prepare("package charts.series",
"public interface has_tooltip",1,function($$private){;return[,,,
];},[],[], "0.8.0", "0.8.4"
);
// class charts.series.pies.DefaultPieProperties
joo.classLoader.prepare("package charts.series.pies",
"public class DefaultPieProperties extends Properties",2,function($$private){;return[

"public function DefaultPieProperties",function(json){
var parent=new Properties({
alpha:0.5,
'start-angle':90,
'label-colour':null,
'font-size':10,
'gradient-fill':false,
stroke:1,
colours:["#900000","#009000"],
animate:[{"type":"fade-in"}],
tip:'#val# of #total#',
'no-labels':false,
'on-click':null
});
this.super$2(json,parent);
tr.aces('4',this.get('start-angle'));
},
];},[],["Properties","tr"], "0.8.0", "0.8.4"
);
// class charts.series.pies.PieLabel
joo.classLoader.prepare("package charts.series.pies",
"public class PieLabel extends flash.text.TextField implements charts.series.has_tooltip",5,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Math);},
"public var",{is_over:false},
"private static var",{TO_RADIANS:function(){return(Math.PI/180);}},
"public function PieLabel",function(style)
{this.super$5();
this.text=style.label;
var fmt=new flash.text.TextFormat();
fmt.color=string.Utils.get_colour(style.colour);
fmt.font="Verdana";
fmt.size=style['font-size'];
fmt.align="center";
this.setTextFormat(fmt);
this.autoSize="left";
this.mouseEnabled=false;
},
"public function move_label",function(rad,x,y,ang){
var legend_x=x+rad*Math.cos((ang)*$$private.TO_RADIANS);
var legend_y=y+rad*Math.sin((ang)*$$private.TO_RADIANS);
if(legend_x<x)
legend_x-=this.width;
if(legend_y<y)
legend_y-=this.height;
this.x=legend_x;
this.y=legend_y;
if((this.x>0)&&
(this.y>0)&&
(this.y+this.height<this.stage.stageHeight)&&
(this.x+this.width<this.stage.stageWidth))
return true;
else
return false;
},
"public function get_tooltip",function(){
tr.ace((as(this.parent,charts.series.has_tooltip)).get_tooltip());
return(as(this.parent,charts.series.has_tooltip)).get_tooltip();
},
"public function get_tip_pos",function(){
return(as(this.parent,charts.series.has_tooltip)).get_tip_pos();
},
"public function set_tip",function(b){
return(as(this.parent,charts.series.has_tooltip)).set_tip(b);
},
"public function resize",function(sc){
},
];},[],["flash.text.TextField","charts.series.has_tooltip","Math","flash.text.TextFormat","string.Utils","tr"], "0.8.0", "0.8.4"
);
// class charts.series.pies.PieSlice
joo.classLoader.prepare("package charts.series.pies",
"public class PieSlice extends charts.series.Element",7,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(caurina.transitions.Equations,Math,flash.display.GradientType);},
"private var",{TO_RADIANS:function(){return(Math.PI/180);}},
"private var",{colour:NaN},
"public var",{slice_angle:NaN},
"private var",{border_width:NaN},
"public var",{angle:NaN},
"public var",{is_over:false},
"public var",{nolabels:false},
"private var",{animate:false},
"private var",{finished_animating:false},
"public var",{value:NaN},
"private var",{gradientFill:false},
"private var",{label:null},
"private var",{pieRadius:NaN},
"private var",{rawToolTip:null},
"public var",{position_original:null},
"public var",{position_animate_to:null},
"public function PieSlice",function(index,value){this.super$7();this.TO_RADIANS$7=this.TO_RADIANS$7();
this.colour$7=value.get_colour('colour');
this.slice_angle=value.get('angle');
this.border_width$7=1;
this.angle=value.get('start');
this.animate$7=value.get('animate');
this.nolabels=value.get('no-labels');
this.value=value.get('value');
this.gradientFill$7=value.get('gradient-fill');
this.index=index;
this.rawToolTip$7=value.get('tip');
this.label$7=this.replace_magic_values$7(value.get('label'));
this.tooltip=this.replace_magic_values$7(value.get('tip'));
if(value.has('on-click'))
this.set_on_click(value.get('on-click'));
this.finished_animating$7=false;
},
"public override function set_tip",function(b){},
"public override function get_tip_pos",function(){
var p=this.localToGlobal(new flash.geom.Point(this.mouseX,this.mouseY));
return{x:p.x,y:p.y};
},
"private function replace_magic_values",function(t){
t=t.replace('#label#',this.label$7);
t=t.replace('#val#',NumberUtils.formatNumber(this.value));
t=t.replace('#radius#',NumberUtils.formatNumber(this.pieRadius$7));
return t;
},
"public override function get_tooltip",function(){
this.tooltip=this.replace_magic_values$7(this.rawToolTip$7);
return this.tooltip;
},
"public override function resize",function(sc){},
"public function pie_resize",function(sc,radius){
this.pieRadius$7=radius;
this.x=sc.get_center_x();
this.y=sc.get_center_y();
var label_line_length=10;
this.graphics.clear();
this.graphics.lineStyle(this.border_width$7,this.colour$7,1);
if(this.gradientFill$7)
{
var colors=[this.colour$7,this.colour$7];
var alphas=[1,0.5];
var ratios=[100,255];
var matrix=new flash.geom.Matrix();
matrix.createGradientBox(radius*2,radius*2,0,-radius,-radius);
this.graphics.beginGradientFill(flash.display.GradientType.RADIAL,colors,alphas,ratios,matrix);
}
else
this.graphics.beginFill(this.colour$7,1);
this.graphics.moveTo(0,0);
this.graphics.lineTo(radius,0);
var angle=4;
var a=Math.tan((angle/2)*this.TO_RADIANS$7);
var i=0;
var endx;
var endy;
var ax;
var ay;
for(i=0;i+angle<this.slice_angle;i+=angle){
endx=radius*Math.cos((i+angle)*this.TO_RADIANS$7);
endy=radius*Math.sin((i+angle)*this.TO_RADIANS$7);
ax=endx+radius*a*Math.cos(((i+angle)-90)*this.TO_RADIANS$7);
ay=endy+radius*a*Math.sin(((i+angle)-90)*this.TO_RADIANS$7);
this.graphics.curveTo(ax,ay,endx,endy);
}
angle=0.08;
a=Math.tan((angle/2)*this.TO_RADIANS$7);
for(;i+angle<this.slice_angle;i+=angle){
endx=radius*Math.cos((i+angle)*this.TO_RADIANS$7);
endy=radius*Math.sin((i+angle)*this.TO_RADIANS$7);
ax=endx+radius*a*Math.cos(((i+angle)-90)*this.TO_RADIANS$7);
ay=endy+radius*a*Math.sin(((i+angle)-90)*this.TO_RADIANS$7);
this.graphics.curveTo(ax,ay,endx,endy);
}
this.graphics.endFill();
this.graphics.lineTo(0,0);
if(!this.nolabels)this.draw_label_line$7(radius,label_line_length,this.slice_angle);
if(this.animate$7)
{
if(!this.finished_animating$7){
this.finished_animating$7=true;
caurina.transitions.Tweener.addTween(this,{rotation:this.angle,time:1.4,transition:caurina.transitions.Equations.easeOutCirc,onComplete:$$bound(this,"done_animating$7")});
}
}
else
{
this.done_animating$7();
}
},
"private function done_animating",function(){
this.rotation=this.angle;
this.finished_animating$7=true;
},
"private function draw_label_line",function(rad,tick_size,slice_angle){
},
"public override function toString",function(){
return"PieSlice: "+this.get_tooltip();
},
"public function getTicAngle",function(){
return this.angle+(this.slice_angle/2);
},
"public function isRightSide",function()
{
return(this.getTicAngle()>=270)||(this.getTicAngle()<=90);
},
"public function get_colour",function(){
return this.colour$7;
},
];},[],["charts.series.Element","Math","flash.geom.Point","NumberUtils","flash.geom.Matrix","flash.display.GradientType","caurina.transitions.Tweener","caurina.transitions.Equations"], "0.8.0", "0.8.4"
);
// class charts.series.pies.PieSliceContainer
joo.classLoader.prepare("package charts.series.pies",
"public class PieSliceContainer extends charts.series.Element",7,function($$private){var is=joo.is,as=joo.as,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(caurina.transitions.Equations,Math,flash.events.MouseEvent);},
"private var",{TO_RADIANS:function(){return(Math.PI/180);}},
"private var",{animating:false},
"private var",{pieSlice:null},
"private var",{pieLabel:null},
"private var",{pieRadius:NaN},
"private var",{tick_size:10},
"private var",{tick_extension_size:4},
"private var",{label_margin:2},
"private var",{animationOffset:30},
"private var",{saveX:NaN},
"private var",{saveY:NaN},
"private var",{moveToX:NaN},
"private var",{moveToY:NaN},
"private var",{original_alpha:NaN},
"public function PieSliceContainer",function(index,value)
{this.super$7();this.TO_RADIANS$7=this.TO_RADIANS$7();
tr.aces('pie',value.get('animate'));
this.pieSlice$7=new charts.series.pies.PieSlice(index,value);
this.addChild(this.pieSlice$7);
var textlabel=value.get('label');
this.alpha=this.original_alpha$7=value.get('alpha');
if(!value.has('label-colour'))
value.set('label-colour',value.get('colour'));
var l=value.get('no-labels')?'':value.get('label');
this.pieLabel$7=new charts.series.pies.PieLabel(
{
label:l,
colour:value.get('label-colour'),
'font-size':value.get('font-size'),
'on-click':value.get('on-click')});
this.addChild(this.pieLabel$7);
this.attach_events__(value);
this.animating$7=false;
},
"public function is_over",function(){
return this.pieSlice$7.is_over;
},
"public function get_slice",function(){
return this.pieSlice$7;
},
"public function get_label",function(){
return this.pieLabel$7;
},
"public override function resize",function(sc){},
"public function is_label_on_screen",function(sc,slice_radius){
return this.pieLabel$7.move_label(
slice_radius+10,
sc.get_center_x(),
sc.get_center_y(),
this.pieSlice$7.angle+(this.pieSlice$7.slice_angle/2));
},
"public function pie_resize",function(sc,slice_radius){
this.pieRadius$7=slice_radius;
this.pieSlice$7.pie_resize(sc,slice_radius);
var ticAngle=this.getTicAngle();
this.saveX$7=this.x;
this.saveY$7=this.y;
this.moveToX$7=this.x+(this.animationOffset$7*Math.cos(ticAngle*this.TO_RADIANS$7));
this.moveToY$7=this.y+(this.animationOffset$7*Math.sin(ticAngle*this.TO_RADIANS$7));
if(this.pieLabel$7.visible)
{
var lblRadius=slice_radius+this.tick_size$7;
var lblAngle=ticAngle*this.TO_RADIANS$7;
this.pieLabel$7.x=this.pieSlice$7.x+lblRadius*Math.cos(lblAngle);
this.pieLabel$7.y=this.pieSlice$7.y+lblRadius*Math.sin(lblAngle);
if(this.isRightSide())
{
this.pieLabel$7.x+=this.tick_extension_size$7+this.label_margin$7;
}
else
{
this.pieLabel$7.x=
this.pieLabel$7.x-
this.pieLabel$7.width-
this.tick_extension_size$7-
this.label_margin$7-
4;
}
this.pieLabel$7.y-=this.pieLabel$7.height/2;
this.drawTicLines();
}
},
"public override function get_tooltip",function(){
return this.pieSlice$7.get_tooltip();
},
"public override function get_tip_pos",function(){
var p=this.localToGlobal(new flash.geom.Point(this.mouseX,this.mouseY));
return{x:p.x,y:p.y};
},
"protected function attach_events__",function(value){
var animate=value.get('animate');
if(!(is(animate,Array))){
if((animate==null)||(animate)){
animate=[{"type":"bounce","distance":5}];
}
else{
animate=new Array();
}
}
var anims=as(animate,Array);
this.addEventListener(flash.events.MouseEvent.MOUSE_OVER,$$bound(this,"mouseOver_first"),false,0,true);
this.addEventListener(flash.events.MouseEvent.MOUSE_OUT,$$bound(this,"mouseOut_first"),false,0,true);
for(var $1 in anims){var a=anims[$1];
switch(a.type){
case"bounce":
this.addEventListener(flash.events.MouseEvent.MOUSE_OVER,$$bound(this,"mouseOver_bounce_out"),false,0,true);
this.addEventListener(flash.events.MouseEvent.MOUSE_OUT,$$bound(this,"mouseOut_bounce_out"),false,0,true);
this.animationOffset$7=a.distance;
break;
default:
this.addEventListener(flash.events.MouseEvent.MOUSE_OVER,$$bound(this,"mouseOver_alpha"),false,0,true);
this.addEventListener(flash.events.MouseEvent.MOUSE_OUT,$$bound(this,"mouseOut_alpha"),false,0,true);
break;
}
}
},
"public function mouseOver_first",function(event){
if(this.animating$7)return;
this.animating$7=true;
caurina.transitions.Tweener.removeTweens(this);
},
"public function mouseOut_first",function(event){
caurina.transitions.Tweener.removeTweens(this);
this.animating$7=false;
},
"public function mouseOver_bounce_out",function(event){
caurina.transitions.Tweener.addTween(this,{x:this.moveToX$7,y:this.moveToY$7,time:0.4,transition:"easeOutBounce"});
},
"public function mouseOut_bounce_out",function(event){
caurina.transitions.Tweener.addTween(this,{x:this.saveX$7,y:this.saveY$7,time:0.4,transition:"easeOutBounce"});
},
"public function mouseOver_alpha",function(event){
caurina.transitions.Tweener.addTween(this,{alpha:1,time:0.6,transition:caurina.transitions.Equations.easeOutCirc});
},
"public function mouseOut_alpha",function(event){
caurina.transitions.Tweener.addTween(this,{alpha:this.original_alpha$7,time:0.8,transition:caurina.transitions.Equations.easeOutElastic});
},
"public function getLabelTopY",function()
{
return this.pieLabel$7.y;
},
"public function getLabelBottomY",function()
{
return this.pieLabel$7.y+this.pieLabel$7.height;
},
"public function moveLabelDown",function(sc,minY)
{
if(this.pieLabel$7.visible)
{
var bAdjustToBottom=false;
var lblTop=this.getLabelTopY();
if(lblTop<minY)
{
var adjust=minY-lblTop;
if((this.pieLabel$7.height+minY)>(sc.bottom-1))
{
adjust=sc.bottom-this.pieLabel$7.height-lblTop;
bAdjustToBottom=true;
}
this.pieLabel$7.y+=adjust;
if(!bAdjustToBottom)
{
var lblRadius=this.pieRadius$7+this.tick_size$7;
var calcSin=((this.pieLabel$7.y+this.pieLabel$7.height/2)-this.pieSlice$7.y)/lblRadius;
calcSin=Math.max(-1,Math.min(1,calcSin));
var newAngle=Math.asin(calcSin)/this.TO_RADIANS$7;
if((this.getTicAngle()>90)&&(this.getTicAngle()<270))
{
newAngle=180-newAngle;
}
else if(this.getTicAngle()>=270)
{
newAngle=360+newAngle;
}
var newX=this.pieSlice$7.x+lblRadius*Math.cos(newAngle*this.TO_RADIANS$7);
if(this.isRightSide())
{
this.pieLabel$7.x=newX+this.tick_extension_size$7+this.label_margin$7;
}
else
{
this.pieLabel$7.x=newX-this.pieLabel$7.width-
this.tick_extension_size$7-this.label_margin$7-4;
}
}
}
this.drawTicLines();
return this.pieLabel$7.y+this.pieLabel$7.height;
}
else
{
return minY;
}
},
"public function moveLabelUp",function(sc,maxY)
{
if(this.pieLabel$7.visible)
{
var sign=1;
var bAdjustToTop=false;
var lblBottom=this.getLabelBottomY();
if(lblBottom>maxY)
{
var adjust=maxY-lblBottom;
if((maxY-this.pieLabel$7.height)<(sc.top+1))
{
adjust=sc.top-this.getLabelTopY();
bAdjustToTop=true;
}
this.pieLabel$7.y+=adjust;
if(!bAdjustToTop)
{
var lblRadius=this.pieRadius$7+this.tick_size$7;
var calcSin=((this.pieLabel$7.y+this.pieLabel$7.height/2)-this.pieSlice$7.y)/lblRadius;
calcSin=Math.max(-1,Math.min(1,calcSin));
var newAngle=Math.asin(calcSin)/this.TO_RADIANS$7;
if((this.getTicAngle()>90)&&(this.getTicAngle()<270))
{
newAngle=180-newAngle;
sign=-1;
}
else if(this.getTicAngle()>=270)
{
newAngle=360+newAngle;
}
var newX=this.pieSlice$7.x+lblRadius*Math.cos(newAngle*this.TO_RADIANS$7);
if(this.isRightSide())
{
this.pieLabel$7.x=newX+this.tick_extension_size$7+this.label_margin$7;
}
else
{
this.pieLabel$7.x=newX-this.pieLabel$7.width-
this.tick_extension_size$7-this.label_margin$7-4;
}
}
}
this.drawTicLines();
return this.pieLabel$7.y;
}
else
{
return maxY;
}
},
"public function get_radius_offsets",function(){
var offset={top:this.animationOffset$7,right:this.animationOffset$7,
bottom:this.animationOffset$7,left:this.animationOffset$7};
if(this.pieLabel$7.visible)
{
var ticAngle=this.getTicAngle();
var offset_threshold=20;
var ticLength=this.tick_size$7;
if((ticAngle>=0)&&(ticAngle<=90))
{
offset.bottom=(ticAngle/90)*ticLength+this.pieLabel$7.height/2+1;
offset.right=((90-ticAngle)/90)*ticLength+this.tick_extension_size$7+this.label_margin$7+this.pieLabel$7.width;
}
else if((ticAngle>90)&&(ticAngle<=180))
{
offset.bottom=((180-ticAngle)/90)*ticLength+this.pieLabel$7.height/2+1;
offset.left=((ticAngle-90)/90)*ticLength+this.tick_extension_size$7+this.label_margin$7+this.pieLabel$7.width+4;
}
else if((ticAngle>180)&&(ticAngle<270))
{
offset.top=((ticAngle-180)/90)*ticLength+this.pieLabel$7.height/2+1;
offset.left=((270-ticAngle)/90)*ticLength+this.tick_extension_size$7+this.label_margin$7+this.pieLabel$7.width+4;
}
else
{
offset.top=((360-ticAngle)/90)*ticLength+this.pieLabel$7.height/2+1;
offset.right=((ticAngle-270)/90)*ticLength+this.tick_extension_size$7+this.label_margin$7+this.pieLabel$7.width;
}
}
return offset;
},
"protected function drawTicLines",function()
{
if((this.pieLabel$7.text!='')&&(this.pieLabel$7.visible))
{
var ticAngle=this.getTicAngle();
var lblRadius=this.pieRadius$7+this.tick_size$7;
var lblAngle=ticAngle*this.TO_RADIANS$7;
var ticLblX;
var ticLblY;
if(this.pieSlice$7.isRightSide())
{
ticLblX=this.pieLabel$7.x-this.label_margin$7;
}
else
{
ticLblX=this.pieLabel$7.x+this.pieLabel$7.width+this.label_margin$7+4;
}
ticLblY=this.pieLabel$7.y+this.pieLabel$7.height/2;
var ticArcX=this.pieSlice$7.x+this.pieRadius$7*Math.cos(lblAngle);
var ticArcY=this.pieSlice$7.y+this.pieRadius$7*Math.sin(lblAngle);
this.graphics.clear();
this.graphics.lineStyle(1,this.pieSlice$7.get_colour(),1);
this.graphics.moveTo(ticLblX,ticLblY);
if(this.pieSlice$7.isRightSide())
{
this.graphics.lineTo(ticLblX-this.tick_extension_size$7,ticLblY);
}
else
{
this.graphics.lineTo(ticLblX+this.tick_extension_size$7,ticLblY);
}
this.graphics.lineTo(ticArcX,ticArcY);
}
},
"public function getTicAngle",function()
{
return this.pieSlice$7.getTicAngle();
},
"public function isRightSide",function()
{
return this.pieSlice$7.isRightSide();
},
];},[],["charts.series.Element","Math","tr","charts.series.pies.PieSlice","charts.series.pies.PieLabel","flash.geom.Point","Array","flash.events.MouseEvent","caurina.transitions.Tweener","caurina.transitions.Equations"], "0.8.0", "0.8.4"
);
// class charts.series.tags.Tag
joo.classLoader.prepare("package charts.series.tags",
"public class Tag extends flash.text.TextField",5,function($$private){var is=joo.is,trace=joo.trace,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(Math,flash.events.MouseEvent);},
"public var",{_x:NaN},
"public var",{_y:NaN},
"public var",{xAdj:0},
"public var",{yAdj:0},
"private var",{link:null},
"private var",{index:NaN},
"protected var",{right_axis:false},
"public function Tag",function(style){this.super$5();
this._x=style.x;
this._y=style.y;
this.right_axis=(style.axis=='right');
if(style['on-click'])
this.set_on_click(style['on-click']);
this.htmlText=this.replace_magic_values$5(style.text);
this.autoSize="left";
this.alpha=style.alpha;
this.border=style.border;
if(style.background!=null){
this.background=true;
this.backgroundColor=string.Utils.get_colour(style.background);
}
var fmt=new flash.text.TextFormat();
if(style.rotate!=0){
fmt.font="spArial";
this.embedFonts=true;
}
else{
fmt.font=style.font;
}
fmt.color=style.colour;
fmt.size=style['font-size'];
fmt.bold=style.bold;
fmt.underline=style.underline;
fmt.align="center";
this.setTextFormat(fmt);
this.selectable=false;
this.rotate_and_align(style.rotate,style['align-x'],style['align-y'],style['pad-x'],style['pad-y']);
},
"public function rotate_and_align",function(rotation,xAlign,yAlign,
xPad,yPad)
{
rotation=rotation%360;
if(rotation<0)rotation+=360;
this.rotation=rotation;
var myright=this.width*Math.cos(rotation*Math.PI/180);
var myleft=this.height*Math.cos((90-rotation)*Math.PI/180);
var mytop=this.height*Math.sin((90-rotation)*Math.PI/180);
var mybottom=this.width*Math.sin(rotation*Math.PI/180);
trace("rotation=",rotation,"width=",this.width,"left=",myleft,"right=",myright);
trace("rotation=",rotation,"height=",this.height,"top=",mytop,"bottom=",mybottom);
if(xAlign=="right")
{
switch(rotation)
{
case 0:this.xAdj=0;
break;
case 90:this.xAdj=this.width;
break;
case 180:this.xAdj=this.width;
break;
case 270:this.xAdj=0;
break;
}
this.xAdj=this.xAdj+xPad;
}
else if(xAlign=="left")
{
switch(rotation)
{
case 0:this.xAdj=-this.width;
break;
case 90:this.xAdj=0;
break;
case 180:this.xAdj=0;
break;
case 270:this.xAdj=-this.width;
break;
}
this.xAdj=this.xAdj-xPad;
}
else
{
switch(rotation)
{
case 0:this.xAdj=-this.width/2;
break;
case 90:this.xAdj=this.width/2;
break;
case 180:this.xAdj=this.width/2;
break;
case 270:this.xAdj=-this.width/2;
break;
}
}
if(yAlign=="center")
{
switch(rotation)
{
case 0:this.yAdj=-this.height/2;
break;
case 90:this.yAdj=-this.height/2;
break;
case 180:this.yAdj=this.height/2;
break;
case 270:this.yAdj=this.height/2;
break;
}
}
else if(yAlign=="below")
{
switch(rotation)
{
case 0:this.yAdj=0;
break;
case 90:this.yAdj=0;
break;
case 180:this.yAdj=this.height;
break;
case 270:this.yAdj=this.height;
break;
}
this.yAdj=this.yAdj+yPad;
}
else
{
switch(rotation)
{
case 0:this.yAdj=-this.height;
break;
case 90:this.yAdj=-this.height;
break;
case 180:this.yAdj=0;
break;
case 270:this.yAdj=0;
break;
}
this.yAdj=this.yAdj-yPad;
}
},
"private function replace_magic_values",function(t){
var regex=/#x#/g;
t=t.replace(regex,NumberUtils.formatNumber(this._x));
regex=/#y#/g;
t=t.replace('#y#',NumberUtils.formatNumber(this._y));
t=string.DateUtils.replace_magic_values(t,this._x);
regex=/#ygmdate/g;
t=t.replace(regex,'#gmdate');
regex=/#ydate/g;
t=t.replace('#ydate','#date');
t=string.DateUtils.replace_magic_values(t,this._y);
return t;
},
"public function set_on_click",function(s){
this.link$5=s;
this.addEventListener(flash.events.MouseEvent.MOUSE_UP,$$bound(this,"mouseUp$5"),false,0,true);
},
"private function mouseUp",function(event){
if(this.link$5.substring(0,6)=='trace:'){
tr.ace(this.link$5);
}
else if(this.link$5.substring(0,5)=='http:')
this.browse_url$5(this.link$5);
else
flash.external.ExternalInterface.call(this.link$5,this._x);
},
"private function browse_url",function(url){
var req=new flash.net.URLRequest(this.link$5);
try
{
flash.net.navigateToURL(req);
}
catch(e){if(is(e,Error))
{
trace("Error opening link: "+this.link$5);
}else throw e;}
},
"public function resize",function(sc){
this.x=sc.get_x_from_val(this._x)+this.xAdj;
this.y=sc.get_y_from_val(this._y,this.right_axis)+this.yAdj;
},
];},[],["flash.text.TextField","string.Utils","flash.text.TextFormat","Math","NumberUtils","string.DateUtils","flash.events.MouseEvent","tr","flash.external.ExternalInterface","flash.net.URLRequest","Error"], "0.8.0", "0.8.4"
);
// class charts.Shape
joo.classLoader.prepare("package charts",
"public class Shape extends charts.Base",7,function($$private){;return[
"private var",{style:null},
"public function Shape",function(json)
{this.super$7();
this.style$7={
points:[],
colour:'#808080',
alpha:0.5
};
object_helper.merge_2(json,this.style$7);
this.style$7.colour=string.Utils.get_colour(this.style$7.colour);
for(var $1 in json.values){var val=json.values[$1];
this.style$7.points.push(new flash.geom.Point(val.x,val.y));}
},
"public override function resize",function(sc){
this.graphics.clear();
this.graphics.lineStyle(0,0,0);
this.graphics.beginFill(this.style$7.colour,this.style$7.alpha);
var moved=false;
for(var $1 in this.style$7.points){var p=this.style$7.points[$1];
if(!moved)
this.graphics.moveTo(sc.get_x_from_val(p.x),sc.get_y_from_val(p.y));
else
this.graphics.lineTo(sc.get_x_from_val(p.x),sc.get_y_from_val(p.y));
moved=true;
}
this.graphics.endFill();
},
];},[],["charts.Base","object_helper","string.Utils","flash.geom.Point"], "0.8.0", "0.8.4"
);
// class charts.Tags
joo.classLoader.prepare("package charts",
"public class Tags extends charts.Base",7,function($$private){var as=joo.as;return[
"private var",{style:null},
"public function Tags",function(json)
{this.super$7();
this.style$7={
values:[],
colour:'#000000',
text:'[#x#, #y#]',
'align-x':'center',
'align-y':'above',
'pad-x':4,
'pad-y':4,
font:'Verdana',
bold:false,
'on-click':null,
rotate:0,
'font-size':12,
border:false,
underline:false,
alpha:1
};
object_helper.merge_2(json,this.style$7);
for(var $1 in this.style$7.values)
{var v=this.style$7.values[$1];
var tmp=this.make_tag$7(v);
this.addChild(tmp);
}
},
"private function make_tag",function(json)
{
var tagStyle={};
object_helper.merge_2(this.style$7,tagStyle);
object_helper.merge_2(json,tagStyle);
tagStyle.colour=string.Utils.get_colour(tagStyle.colour);
return new charts.series.tags.Tag(tagStyle);
},
"public override function resize",function(sc){
for(var i=0;i<this.numChildren;i++){
var tag=as(this.getChildAt(i),charts.series.tags.Tag);
tag.resize(sc);
}
},
];},[],["charts.Base","object_helper","string.Utils","charts.series.tags.Tag"], "0.8.0", "0.8.4"
);
// class com.adobe.crypto.MD5
joo.classLoader.prepare(
"package com.adobe.crypto",
"public class MD5",1,function($$private){;return[
"public static function hash",function(s){
var a=1732584193;
var b=-271733879;
var c=-1732584194;
var d=271733878;
var aa;
var bb;
var cc;
var dd;
var x=$$private.createBlocks(s);
var len=x.length;
for(var i=0;i<len;i+=16){
aa=a;
bb=b;
cc=c;
dd=d;
a=$$private.ff(a,b,c,d,x[i+0],7,-680876936);
d=$$private.ff(d,a,b,c,x[i+1],12,-389564586);
c=$$private.ff(c,d,a,b,x[i+2],17,606105819);
b=$$private.ff(b,c,d,a,x[i+3],22,-1044525330);
a=$$private.ff(a,b,c,d,x[i+4],7,-176418897);
d=$$private.ff(d,a,b,c,x[i+5],12,1200080426);
c=$$private.ff(c,d,a,b,x[i+6],17,-1473231341);
b=$$private.ff(b,c,d,a,x[i+7],22,-45705983);
a=$$private.ff(a,b,c,d,x[i+8],7,1770035416);
d=$$private.ff(d,a,b,c,x[i+9],12,-1958414417);
c=$$private.ff(c,d,a,b,x[i+10],17,-42063);
b=$$private.ff(b,c,d,a,x[i+11],22,-1990404162);
a=$$private.ff(a,b,c,d,x[i+12],7,1804603682);
d=$$private.ff(d,a,b,c,x[i+13],12,-40341101);
c=$$private.ff(c,d,a,b,x[i+14],17,-1502002290);
b=$$private.ff(b,c,d,a,x[i+15],22,1236535329);
a=$$private.gg(a,b,c,d,x[i+1],5,-165796510);
d=$$private.gg(d,a,b,c,x[i+6],9,-1069501632);
c=$$private.gg(c,d,a,b,x[i+11],14,643717713);
b=$$private.gg(b,c,d,a,x[i+0],20,-373897302);
a=$$private.gg(a,b,c,d,x[i+5],5,-701558691);
d=$$private.gg(d,a,b,c,x[i+10],9,38016083);
c=$$private.gg(c,d,a,b,x[i+15],14,-660478335);
b=$$private.gg(b,c,d,a,x[i+4],20,-405537848);
a=$$private.gg(a,b,c,d,x[i+9],5,568446438);
d=$$private.gg(d,a,b,c,x[i+14],9,-1019803690);
c=$$private.gg(c,d,a,b,x[i+3],14,-187363961);
b=$$private.gg(b,c,d,a,x[i+8],20,1163531501);
a=$$private.gg(a,b,c,d,x[i+13],5,-1444681467);
d=$$private.gg(d,a,b,c,x[i+2],9,-51403784);
c=$$private.gg(c,d,a,b,x[i+7],14,1735328473);
b=$$private.gg(b,c,d,a,x[i+12],20,-1926607734);
a=$$private.hh(a,b,c,d,x[i+5],4,-378558);
d=$$private.hh(d,a,b,c,x[i+8],11,-2022574463);
c=$$private.hh(c,d,a,b,x[i+11],16,1839030562);
b=$$private.hh(b,c,d,a,x[i+14],23,-35309556);
a=$$private.hh(a,b,c,d,x[i+1],4,-1530992060);
d=$$private.hh(d,a,b,c,x[i+4],11,1272893353);
c=$$private.hh(c,d,a,b,x[i+7],16,-155497632);
b=$$private.hh(b,c,d,a,x[i+10],23,-1094730640);
a=$$private.hh(a,b,c,d,x[i+13],4,681279174);
d=$$private.hh(d,a,b,c,x[i+0],11,-358537222);
c=$$private.hh(c,d,a,b,x[i+3],16,-722521979);
b=$$private.hh(b,c,d,a,x[i+6],23,76029189);
a=$$private.hh(a,b,c,d,x[i+9],4,-640364487);
d=$$private.hh(d,a,b,c,x[i+12],11,-421815835);
c=$$private.hh(c,d,a,b,x[i+15],16,530742520);
b=$$private.hh(b,c,d,a,x[i+2],23,-995338651);
a=$$private.ii(a,b,c,d,x[i+0],6,-198630844);
d=$$private.ii(d,a,b,c,x[i+7],10,1126891415);
c=$$private.ii(c,d,a,b,x[i+14],15,-1416354905);
b=$$private.ii(b,c,d,a,x[i+5],21,-57434055);
a=$$private.ii(a,b,c,d,x[i+12],6,1700485571);
d=$$private.ii(d,a,b,c,x[i+3],10,-1894986606);
c=$$private.ii(c,d,a,b,x[i+10],15,-1051523);
b=$$private.ii(b,c,d,a,x[i+1],21,-2054922799);
a=$$private.ii(a,b,c,d,x[i+8],6,1873313359);
d=$$private.ii(d,a,b,c,x[i+15],10,-30611744);
c=$$private.ii(c,d,a,b,x[i+6],15,-1560198380);
b=$$private.ii(b,c,d,a,x[i+13],21,1309151649);
a=$$private.ii(a,b,c,d,x[i+4],6,-145523070);
d=$$private.ii(d,a,b,c,x[i+11],10,-1120210379);
c=$$private.ii(c,d,a,b,x[i+2],15,718787259);
b=$$private.ii(b,c,d,a,x[i+9],21,-343485551);
a+=aa;
b+=bb;
c+=cc;
d+=dd;
}
return com.adobe.utils.IntUtil.toHex(a)+com.adobe.utils.IntUtil.toHex(b)+com.adobe.utils.IntUtil.toHex(c)+com.adobe.utils.IntUtil.toHex(d);
},
"private static function f",function(x,y,z){
return(x&y)|((~x)&z);
},
"private static function g",function(x,y,z){
return(x&z)|(y&(~z));
},
"private static function h",function(x,y,z){
return x^y^z;
},
"private static function i",function(x,y,z){
return y^(x|(~z));
},
"private static function transform",function(func,a,b,c,d,x,s,t){
var tmp=a+$$int(func(b,c,d))+x+t;
return com.adobe.utils.IntUtil.rol(tmp,s)+b;
},
"private static function ff",function(a,b,c,d,x,s,t){
return $$private.transform($$private.f,a,b,c,d,x,s,t);
},
"private static function gg",function(a,b,c,d,x,s,t){
return $$private.transform($$private.g,a,b,c,d,x,s,t);
},
"private static function hh",function(a,b,c,d,x,s,t){
return $$private.transform($$private.h,a,b,c,d,x,s,t);
},
"private static function ii",function(a,b,c,d,x,s,t){
return $$private.transform($$private.i,a,b,c,d,x,s,t);
},
"private static function createBlocks",function(s){
var blocks=new Array();
var len=s.length*8;
var mask=0xFF;
for(var i=0;i<len;i+=8){
blocks[i>>5]|=(s.charCodeAt(i/8)&mask)<<(i%32);
}
blocks[len>>5]|=0x80<<(len%32);
blocks[(((len+64)>>>9)<<4)+14]=len;
return blocks;
},
];},["hash"],["com.adobe.utils.IntUtil","int","Array"], "0.8.0", "0.8.4"
);
// class com.adobe.crypto.SHA1
joo.classLoader.prepare(
"package com.adobe.crypto",
"public class SHA1",1,function($$private){;return[

"public static function hash",function(s)
{
var blocks=$$private.createBlocksFromString(s);
var byteArray=$$private.hashBlocks(blocks);
return com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true);
},
"public static function hashBytes",function(data)
{
var blocks=$$private.createBlocksFromByteArray(data);
var byteArray=$$private.hashBlocks(blocks);
return com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true);
},
"public static function hashToBase64",function(s)
{
var blocks=$$private.createBlocksFromString(s);
var byteArray=$$private.hashBlocks(blocks);
var charsInByteArray="";
byteArray.position=0;
for(var j=0;j<byteArray.length;j++)
{
var byte=byteArray.readUnsignedByte();
charsInByteArray+=String.fromCharCode(byte);
}
var encoder=new mx.utils.Base64Encoder();
encoder.encode(charsInByteArray);
return encoder.flush();
},
"private static function hashBlocks",function(blocks)
{
var h0=0x67452301;
var h1=0xefcdab89;
var h2=0x98badcfe;
var h3=0x10325476;
var h4=0xc3d2e1f0;
var len=blocks.length;
var w=new Array(80);
for(var i=0;i<len;i+=16){
var a=h0;
var b=h1;
var c=h2;
var d=h3;
var e=h4;
for(var t=0;t<80;t++){
if(t<16){
w[t]=blocks[i+t];
}else{
w[t]=com.adobe.utils.IntUtil.rol(w[t-3]^w[t-8]^w[t-14]^w[t-16],1);
}
var temp=com.adobe.utils.IntUtil.rol(a,5)+$$private.f(t,b,c,d)+e+$$int(w[t])+$$private.k(t);
e=d;
d=c;
c=com.adobe.utils.IntUtil.rol(b,30);
b=a;
a=temp;
}
h0+=a;
h1+=b;
h2+=c;
h3+=d;
h4+=e;
}
var byteArray=new flash.utils.ByteArray();
byteArray.writeInt(h0);
byteArray.writeInt(h1);
byteArray.writeInt(h2);
byteArray.writeInt(h3);
byteArray.writeInt(h4);
byteArray.position=0;
return byteArray;
},
"private static function f",function(t,b,c,d){
if(t<20){
return(b&c)|(~b&d);
}else if(t<40){
return b^c^d;
}else if(t<60){
return(b&c)|(b&d)|(c&d);
}
return b^c^d;
},
"private static function k",function(t){
if(t<20){
return 0x5a827999;
}else if(t<40){
return 0x6ed9eba1;
}else if(t<60){
return 0x8f1bbcdc;
}
return 0xca62c1d6;
},
"private static function createBlocksFromByteArray",function(data)
{
var oldPosition=data.position;
data.position=0;
var blocks=new Array();
var len=data.length*8;
var mask=0xFF;
for(var i=0;i<len;i+=8)
{
blocks[i>>5]|=(data.readByte()&mask)<<(24-i%32);
}
blocks[len>>5]|=0x80<<(24-len%32);
blocks[(((len+64)>>9)<<4)+15]=len;
data.position=oldPosition;
return blocks;
},
"private static function createBlocksFromString",function(s)
{
var blocks=new Array();
var len=s.length*8;
var mask=0xFF;
for(var i=0;i<len;i+=8){
blocks[i>>5]|=(s.charCodeAt(i/8)&mask)<<(24-i%32);
}
blocks[len>>5]|=0x80<<(24-len%32);
blocks[(((len+64)>>9)<<4)+15]=len;
return blocks;
},
];},["hash","hashBytes","hashToBase64"],["com.adobe.utils.IntUtil","String","mx.utils.Base64Encoder","Array","int","flash.utils.ByteArray"], "0.8.0", "0.8.4"
);
// class com.adobe.crypto.SHA224
joo.classLoader.prepare(
"package com.adobe.crypto",
"public class SHA224",1,function($$private){;return[

"public static function hash",function(s){
var blocks=$$private.createBlocksFromString(s);
var byteArray=$$private.hashBlocks(blocks);
return com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true);
},
"public static function hashBytes",function(data)
{
var blocks=$$private.createBlocksFromByteArray(data);
var byteArray=$$private.hashBlocks(blocks);
return com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true);
},
"public static function hashToBase64",function(s)
{
var blocks=$$private.createBlocksFromString(s);
var byteArray=$$private.hashBlocks(blocks);
var charsInByteArray="";
byteArray.position=0;
for(var j=0;j<byteArray.length;j++)
{
var byte=byteArray.readUnsignedByte();
charsInByteArray+=String.fromCharCode(byte);
}
var encoder=new mx.utils.Base64Encoder();
encoder.encode(charsInByteArray);
return encoder.flush();
},
"private static function hashBlocks",function(blocks){
var h0=0xc1059ed8;
var h1=0x367cd507;
var h2=0x3070dd17;
var h3=0xf70e5939;
var h4=0xffc00b31;
var h5=0x68581511;
var h6=0x64f98fa7;
var h7=0xbefa4fa4;
var k=new Array(0x428a2f98,0x71374491,0xb5c0fbcf,0xe9b5dba5,0x3956c25b,0x59f111f1,0x923f82a4,0xab1c5ed5,0xd807aa98,0x12835b01,0x243185be,0x550c7dc3,0x72be5d74,0x80deb1fe,0x9bdc06a7,0xc19bf174,0xe49b69c1,0xefbe4786,0x0fc19dc6,0x240ca1cc,0x2de92c6f,0x4a7484aa,0x5cb0a9dc,0x76f988da,0x983e5152,0xa831c66d,0xb00327c8,0xbf597fc7,0xc6e00bf3,0xd5a79147,0x06ca6351,0x14292967,0x27b70a85,0x2e1b2138,0x4d2c6dfc,0x53380d13,0x650a7354,0x766a0abb,0x81c2c92e,0x92722c85,0xa2bfe8a1,0xa81a664b,0xc24b8b70,0xc76c51a3,0xd192e819,0xd6990624,0xf40e3585,0x106aa070,0x19a4c116,0x1e376c08,0x2748774c,0x34b0bcb5,0x391c0cb3,0x4ed8aa4a,0x5b9cca4f,0x682e6ff3,0x748f82ee,0x78a5636f,0x84c87814,0x8cc70208,0x90befffa,0xa4506ceb,0xbef9a3f7,0xc67178f2);
var len=blocks.length;
var w=new Array();
for(var i=0;i<len;i+=16){
var a=h0;
var b=h1;
var c=h2;
var d=h3;
var e=h4;
var f=h5;
var g=h6;
var h=h7;
for(var t=0;t<64;t++){
if(t<16){
w[t]=blocks[i+t];
if(isNaN(w[t])){w[t]=0;}
}else{
var ws0=com.adobe.utils.IntUtil.ror(w[t-15],7)^com.adobe.utils.IntUtil.ror(w[t-15],18)^(w[t-15]>>>3);
var ws1=com.adobe.utils.IntUtil.ror(w[t-2],17)^com.adobe.utils.IntUtil.ror(w[t-2],19)^(w[t-2]>>>10);
w[t]=w[t-16]+ws0+w[t-7]+ws1;
}
var s0=com.adobe.utils.IntUtil.ror(a,2)^com.adobe.utils.IntUtil.ror(a,13)^com.adobe.utils.IntUtil.ror(a,22);
var maj=(a&b)^(a&c)^(b&c);
var t2=s0+maj;
var s1=com.adobe.utils.IntUtil.ror(e,6)^com.adobe.utils.IntUtil.ror(e,11)^com.adobe.utils.IntUtil.ror(e,25);
var ch=(e&f)^((~e)&g);
var t1=h+s1+ch+k[t]+w[t];
h=g;
g=f;
f=e;
e=d+t1;
d=c;
c=b;
b=a;
a=t1+t2;
}
h0+=a;
h1+=b;
h2+=c;
h3+=d;
h4+=e;
h5+=f;
h6+=g;
h7+=h;
}
var byteArray=new flash.utils.ByteArray();
byteArray.writeInt(h0);
byteArray.writeInt(h1);
byteArray.writeInt(h2);
byteArray.writeInt(h3);
byteArray.writeInt(h4);
byteArray.writeInt(h5);
byteArray.writeInt(h6);
byteArray.position=0;
return byteArray;
},
"private static function createBlocksFromByteArray",function(data)
{
var oldPosition=data.position;
data.position=0;
var blocks=new Array();
var len=data.length*8;
var mask=0xFF;
for(var i=0;i<len;i+=8)
{
blocks[i>>5]|=(data.readByte()&mask)<<(24-i%32);
}
blocks[len>>5]|=0x80<<(24-len%32);
blocks[(((len+64)>>9)<<4)+15]=len;
data.position=oldPosition;
return blocks;
},
"private static function createBlocksFromString",function(s)
{
var blocks=new Array();
var len=s.length*8;
var mask=0xFF;
for(var i=0;i<len;i+=8){
blocks[i>>5]|=(s.charCodeAt(i/8)&mask)<<(24-i%32);
}
blocks[len>>5]|=0x80<<(24-len%32);
blocks[(((len+64)>>9)<<4)+15]=len;
return blocks;
},
];},["hash","hashBytes","hashToBase64"],["com.adobe.utils.IntUtil","String","mx.utils.Base64Encoder","Array","flash.utils.ByteArray"], "0.8.0", "0.8.4"
);
// class com.adobe.crypto.SHA256
joo.classLoader.prepare(
"package com.adobe.crypto",
"public class SHA256",1,function($$private){;return[

"public static function hash",function(s){
var blocks=$$private.createBlocksFromString(s);
var byteArray=$$private.hashBlocks(blocks);
return com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true);
},
"public static function hashBytes",function(data)
{
var blocks=$$private.createBlocksFromByteArray(data);
var byteArray=$$private.hashBlocks(blocks);
return com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true)
+com.adobe.utils.IntUtil.toHex(byteArray.readInt(),true);
},
"public static function hashToBase64",function(s)
{
var blocks=$$private.createBlocksFromString(s);
var byteArray=$$private.hashBlocks(blocks);
var charsInByteArray="";
byteArray.position=0;
for(var j=0;j<byteArray.length;j++)
{
var byte=byteArray.readUnsignedByte();
charsInByteArray+=String.fromCharCode(byte);
}
var encoder=new mx.utils.Base64Encoder();
encoder.encode(charsInByteArray);
return encoder.flush();
},
"private static function hashBlocks",function(blocks){
var h0=0x6a09e667;
var h1=0xbb67ae85;
var h2=0x3c6ef372;
var h3=0xa54ff53a;
var h4=0x510e527f;
var h5=0x9b05688c;
var h6=0x1f83d9ab;
var h7=0x5be0cd19;
var k=new Array(0x428a2f98,0x71374491,0xb5c0fbcf,0xe9b5dba5,0x3956c25b,0x59f111f1,0x923f82a4,0xab1c5ed5,0xd807aa98,0x12835b01,0x243185be,0x550c7dc3,0x72be5d74,0x80deb1fe,0x9bdc06a7,0xc19bf174,0xe49b69c1,0xefbe4786,0x0fc19dc6,0x240ca1cc,0x2de92c6f,0x4a7484aa,0x5cb0a9dc,0x76f988da,0x983e5152,0xa831c66d,0xb00327c8,0xbf597fc7,0xc6e00bf3,0xd5a79147,0x06ca6351,0x14292967,0x27b70a85,0x2e1b2138,0x4d2c6dfc,0x53380d13,0x650a7354,0x766a0abb,0x81c2c92e,0x92722c85,0xa2bfe8a1,0xa81a664b,0xc24b8b70,0xc76c51a3,0xd192e819,0xd6990624,0xf40e3585,0x106aa070,0x19a4c116,0x1e376c08,0x2748774c,0x34b0bcb5,0x391c0cb3,0x4ed8aa4a,0x5b9cca4f,0x682e6ff3,0x748f82ee,0x78a5636f,0x84c87814,0x8cc70208,0x90befffa,0xa4506ceb,0xbef9a3f7,0xc67178f2);
var len=blocks.length;
var w=new Array(64);
for(var i=0;i<len;i+=16){
var a=h0;
var b=h1;
var c=h2;
var d=h3;
var e=h4;
var f=h5;
var g=h6;
var h=h7;
for(var t=0;t<64;t++){
if(t<16){
w[t]=blocks[i+t];
if(isNaN(w[t])){w[t]=0;}
}else{
var ws0=com.adobe.utils.IntUtil.ror(w[t-15],7)^com.adobe.utils.IntUtil.ror(w[t-15],18)^(w[t-15]>>>3);
var ws1=com.adobe.utils.IntUtil.ror(w[t-2],17)^com.adobe.utils.IntUtil.ror(w[t-2],19)^(w[t-2]>>>10);
w[t]=w[t-16]+ws0+w[t-7]+ws1;
}
var s0=com.adobe.utils.IntUtil.ror(a,2)^com.adobe.utils.IntUtil.ror(a,13)^com.adobe.utils.IntUtil.ror(a,22);
var maj=(a&b)^(a&c)^(b&c);
var t2=s0+maj;
var s1=com.adobe.utils.IntUtil.ror(e,6)^com.adobe.utils.IntUtil.ror(e,11)^com.adobe.utils.IntUtil.ror(e,25);
var ch=(e&f)^((~e)&g);
var t1=h+s1+ch+k[t]+w[t];
h=g;
g=f;
f=e;
e=d+t1;
d=c;
c=b;
b=a;
a=t1+t2;
}
h0+=a;
h1+=b;
h2+=c;
h3+=d;
h4+=e;
h5+=f;
h6+=g;
h7+=h;
}
var byteArray=new flash.utils.ByteArray();
byteArray.writeInt(h0);
byteArray.writeInt(h1);
byteArray.writeInt(h2);
byteArray.writeInt(h3);
byteArray.writeInt(h4);
byteArray.writeInt(h5);
byteArray.writeInt(h6);
byteArray.writeInt(h7);
byteArray.position=0;
return byteArray;
},
"private static function createBlocksFromByteArray",function(data)
{
var oldPosition=data.position;
data.position=0;
var blocks=new Array();
var len=data.length*8;
var mask=0xFF;
for(var i=0;i<len;i+=8)
{
blocks[i>>5]|=(data.readByte()&mask)<<(24-i%32);
}
blocks[len>>5]|=0x80<<(24-len%32);
blocks[(((len+64)>>9)<<4)+15]=len;
data.position=oldPosition;
return blocks;
},
"private static function createBlocksFromString",function(s)
{
var blocks=new Array();
var len=s.length*8;
var mask=0xFF;
for(var i=0;i<len;i+=8){
blocks[i>>5]|=(s.charCodeAt(i/8)&mask)<<(24-i%32);
}
blocks[len>>5]|=0x80<<(24-len%32);
blocks[(((len+64)>>9)<<4)+15]=len;
return blocks;
},
];},["hash","hashBytes","hashToBase64"],["com.adobe.utils.IntUtil","String","mx.utils.Base64Encoder","Array","flash.utils.ByteArray"], "0.8.0", "0.8.4"
);
// class com.adobe.crypto.WSSEUsernameToken
joo.classLoader.prepare(
"package com.adobe.crypto",
"public class WSSEUsernameToken",1,function($$private){;return[

"public static function getUsernameToken",function(username,password,nonce,timestamp)
{if(arguments.length<4){if(arguments.length<3){nonce=null;}timestamp=null;}
if(nonce==null)
{
nonce=$$private.generateNonce();
}
nonce=com.adobe.crypto.WSSEUsernameToken.base64Encode(nonce);
var created=com.adobe.crypto.WSSEUsernameToken.generateTimestamp(timestamp);
var password64=com.adobe.crypto.WSSEUsernameToken.getBase64Digest(nonce,
created,
password);
var token=new String("UsernameToken Username=\"");
token+=username+"\", "+
"PasswordDigest=\""+password64+"\", "+
"Nonce=\""+nonce+"\", "+
"Created=\""+created+"\"";
return token;
},
"private static function generateNonce",function()
{
var s=Math.random().toString();
return s.replace(".","");
},
"internal static function base64Encode",function(s)
{
var encoder=new mx.utils.Base64Encoder();
encoder.encode(s);
return encoder.flush();
},
"internal static function generateTimestamp",function(timestamp)
{
if(timestamp==null)
{
timestamp=new Date();
}
var dateFormatter=new mx.formatters.DateFormatter();
dateFormatter.formatString="YYYY-MM-DDTJJ:NN:SS";
return dateFormatter.format(timestamp)+"Z";
},
"internal static function getBase64Digest",function(nonce,created,password)
{
return com.adobe.crypto.SHA1.hashToBase64(nonce+created+password);
},
];},["getUsernameToken","base64Encode","generateTimestamp","getBase64Digest"],["String","Math","mx.utils.Base64Encoder","Date","mx.formatters.DateFormatter","com.adobe.crypto.SHA1"], "0.8.0", "0.8.4"
);
// class com.adobe.errors.IllegalStateError
joo.classLoader.prepare(
"package com.adobe.errors",
"public class IllegalStateError extends Error",2,function($$private){;return[

"public function IllegalStateError",function(message)
{
this.super$2(message);
},
];},[],["Error"], "0.8.0", "0.8.4"
);
// class com.adobe.images.BitString
joo.classLoader.prepare(
"package com.adobe.images",
"public class BitString",1,function($$private){;return[

"public var",{len:0},
"public var",{val:0},
];},[],[], "0.8.0", "0.8.4"
);
// class com.adobe.images.JPGEncoder
joo.classLoader.prepare(
"package com.adobe.images",
"public class JPGEncoder",1,function($$private){;return[

"private var",{ZigZag:function(){return([
0,1,5,6,14,15,27,28,
2,4,7,13,16,26,29,42,
3,8,12,17,25,30,41,43,
9,11,18,24,31,40,44,53,
10,19,23,32,39,45,52,54,
20,22,33,38,46,51,55,60,
21,34,37,47,50,56,59,61,
35,36,48,49,57,58,62,63
]);}},
"private var",{YTable:function(){return(new Array(64));}},
"private var",{UVTable:function(){return(new Array(64));}},
"private var",{fdtbl_Y:function(){return(new Array(64));}},
"private var",{fdtbl_UV:function(){return(new Array(64));}},
"private function initQuantTables",function(sf)
{
var i;
var t;
var YQT=[
16,11,10,16,24,40,51,61,
12,12,14,19,26,58,60,55,
14,13,16,24,40,57,69,56,
14,17,22,29,51,87,80,62,
18,22,37,56,68,109,103,77,
24,35,55,64,81,104,113,92,
49,64,78,87,103,121,120,101,
72,92,95,98,112,100,103,99
];
for(i=0;i<64;i++){
t=Math.floor((YQT[i]*sf+50)/100);
if(t<1){
t=1;
}else if(t>255){
t=255;
}
this.YTable$1[this.ZigZag$1[i]]=t;
}
var UVQT=[
17,18,24,47,99,99,99,99,
18,21,26,66,99,99,99,99,
24,26,56,99,99,99,99,99,
47,66,99,99,99,99,99,99,
99,99,99,99,99,99,99,99,
99,99,99,99,99,99,99,99,
99,99,99,99,99,99,99,99,
99,99,99,99,99,99,99,99
];
for(i=0;i<64;i++){
t=Math.floor((UVQT[i]*sf+50)/100);
if(t<1){
t=1;
}else if(t>255){
t=255;
}
this.UVTable$1[this.ZigZag$1[i]]=t;
}
var aasf=[
1.0,1.387039845,1.306562965,1.175875602,
1.0,0.785694958,0.541196100,0.275899379
];
i=0;
for(var row=0;row<8;row++)
{
for(var col=0;col<8;col++)
{
this.fdtbl_Y$1[i]=(1.0/(this.YTable$1[this.ZigZag$1[i]]*aasf[row]*aasf[col]*8.0));
this.fdtbl_UV$1[i]=(1.0/(this.UVTable$1[this.ZigZag$1[i]]*aasf[row]*aasf[col]*8.0));
i++;
}
}
},
"private var",{YDC_HT:null},
"private var",{UVDC_HT:null},
"private var",{YAC_HT:null},
"private var",{UVAC_HT:null},
"private function computeHuffmanTbl",function(nrcodes,std_table)
{
var codevalue=0;
var pos_in_table=0;
var HT=new Array();
for(var k=1;k<=16;k++){
for(var j=1;j<=nrcodes[k];j++){
HT[std_table[pos_in_table]]=new com.adobe.images.BitString();
HT[std_table[pos_in_table]].val=codevalue;
HT[std_table[pos_in_table]].len=k;
pos_in_table++;
codevalue++;
}
codevalue*=2;
}
return HT;
},
"private var",{std_dc_luminance_nrcodes:function(){return([0,0,1,5,1,1,1,1,1,1,0,0,0,0,0,0,0]);}},
"private var",{std_dc_luminance_values:function(){return([0,1,2,3,4,5,6,7,8,9,10,11]);}},
"private var",{std_ac_luminance_nrcodes:function(){return([0,0,2,1,3,3,2,4,3,5,5,4,4,0,0,1,0x7d]);}},
"private var",{std_ac_luminance_values:function(){return([
0x01,0x02,0x03,0x00,0x04,0x11,0x05,0x12,
0x21,0x31,0x41,0x06,0x13,0x51,0x61,0x07,
0x22,0x71,0x14,0x32,0x81,0x91,0xa1,0x08,
0x23,0x42,0xb1,0xc1,0x15,0x52,0xd1,0xf0,
0x24,0x33,0x62,0x72,0x82,0x09,0x0a,0x16,
0x17,0x18,0x19,0x1a,0x25,0x26,0x27,0x28,
0x29,0x2a,0x34,0x35,0x36,0x37,0x38,0x39,
0x3a,0x43,0x44,0x45,0x46,0x47,0x48,0x49,
0x4a,0x53,0x54,0x55,0x56,0x57,0x58,0x59,
0x5a,0x63,0x64,0x65,0x66,0x67,0x68,0x69,
0x6a,0x73,0x74,0x75,0x76,0x77,0x78,0x79,
0x7a,0x83,0x84,0x85,0x86,0x87,0x88,0x89,
0x8a,0x92,0x93,0x94,0x95,0x96,0x97,0x98,
0x99,0x9a,0xa2,0xa3,0xa4,0xa5,0xa6,0xa7,
0xa8,0xa9,0xaa,0xb2,0xb3,0xb4,0xb5,0xb6,
0xb7,0xb8,0xb9,0xba,0xc2,0xc3,0xc4,0xc5,
0xc6,0xc7,0xc8,0xc9,0xca,0xd2,0xd3,0xd4,
0xd5,0xd6,0xd7,0xd8,0xd9,0xda,0xe1,0xe2,
0xe3,0xe4,0xe5,0xe6,0xe7,0xe8,0xe9,0xea,
0xf1,0xf2,0xf3,0xf4,0xf5,0xf6,0xf7,0xf8,
0xf9,0xfa
]);}},
"private var",{std_dc_chrominance_nrcodes:function(){return([0,0,3,1,1,1,1,1,1,1,1,1,0,0,0,0,0]);}},
"private var",{std_dc_chrominance_values:function(){return([0,1,2,3,4,5,6,7,8,9,10,11]);}},
"private var",{std_ac_chrominance_nrcodes:function(){return([0,0,2,1,2,4,4,3,4,7,5,4,4,0,1,2,0x77]);}},
"private var",{std_ac_chrominance_values:function(){return([
0x00,0x01,0x02,0x03,0x11,0x04,0x05,0x21,
0x31,0x06,0x12,0x41,0x51,0x07,0x61,0x71,
0x13,0x22,0x32,0x81,0x08,0x14,0x42,0x91,
0xa1,0xb1,0xc1,0x09,0x23,0x33,0x52,0xf0,
0x15,0x62,0x72,0xd1,0x0a,0x16,0x24,0x34,
0xe1,0x25,0xf1,0x17,0x18,0x19,0x1a,0x26,
0x27,0x28,0x29,0x2a,0x35,0x36,0x37,0x38,
0x39,0x3a,0x43,0x44,0x45,0x46,0x47,0x48,
0x49,0x4a,0x53,0x54,0x55,0x56,0x57,0x58,
0x59,0x5a,0x63,0x64,0x65,0x66,0x67,0x68,
0x69,0x6a,0x73,0x74,0x75,0x76,0x77,0x78,
0x79,0x7a,0x82,0x83,0x84,0x85,0x86,0x87,
0x88,0x89,0x8a,0x92,0x93,0x94,0x95,0x96,
0x97,0x98,0x99,0x9a,0xa2,0xa3,0xa4,0xa5,
0xa6,0xa7,0xa8,0xa9,0xaa,0xb2,0xb3,0xb4,
0xb5,0xb6,0xb7,0xb8,0xb9,0xba,0xc2,0xc3,
0xc4,0xc5,0xc6,0xc7,0xc8,0xc9,0xca,0xd2,
0xd3,0xd4,0xd5,0xd6,0xd7,0xd8,0xd9,0xda,
0xe2,0xe3,0xe4,0xe5,0xe6,0xe7,0xe8,0xe9,
0xea,0xf2,0xf3,0xf4,0xf5,0xf6,0xf7,0xf8,
0xf9,0xfa
]);}},
"private function initHuffmanTbl",function()
{
this.YDC_HT$1=this.computeHuffmanTbl$1(this.std_dc_luminance_nrcodes$1,this.std_dc_luminance_values$1);
this.UVDC_HT$1=this.computeHuffmanTbl$1(this.std_dc_chrominance_nrcodes$1,this.std_dc_chrominance_values$1);
this.YAC_HT$1=this.computeHuffmanTbl$1(this.std_ac_luminance_nrcodes$1,this.std_ac_luminance_values$1);
this.UVAC_HT$1=this.computeHuffmanTbl$1(this.std_ac_chrominance_nrcodes$1,this.std_ac_chrominance_values$1);
},
"private var",{bitcode:function(){return(new Array(65535));}},
"private var",{category:function(){return(new Array(65535));}},
"private function initCategoryNumber",function()
{
var nrlower=1;
var nrupper=2;
var nr;
for(var cat=1;cat<=15;cat++){
for(nr=nrlower;nr<nrupper;nr++){
this.category$1[32767+nr]=cat;
this.bitcode$1[32767+nr]=new com.adobe.images.BitString();
this.bitcode$1[32767+nr].len=cat;
this.bitcode$1[32767+nr].val=nr;
}
for(nr=-(nrupper-1);nr<=-nrlower;nr++){
this.category$1[32767+nr]=cat;
this.bitcode$1[32767+nr]=new com.adobe.images.BitString();
this.bitcode$1[32767+nr].len=cat;
this.bitcode$1[32767+nr].val=nrupper-1+nr;
}
nrlower<<=1;
nrupper<<=1;
}
},
"private var",{byteout:null},
"private var",{bytenew:0},
"private var",{bytepos:7},
"private function writeBits",function(bs)
{
var value=bs.val;
var posval=bs.len-1;
while(posval>=0){
if(value&$$uint(1<<posval)){
this.bytenew$1|=$$uint(1<<this.bytepos$1);
}
posval--;
this.bytepos$1--;
if(this.bytepos$1<0){
if(this.bytenew$1==0xFF){
this.writeByte$1(0xFF);
this.writeByte$1(0);
}
else{
this.writeByte$1(this.bytenew$1);
}
this.bytepos$1=7;
this.bytenew$1=0;
}
}
},
"private function writeByte",function(value)
{
this.byteout$1.writeByte(value);
},
"private function writeWord",function(value)
{
this.writeByte$1((value>>8)&0xFF);
this.writeByte$1((value)&0xFF);
},
"private function fDCTQuant",function(data,fdtbl)
{
var tmp0,tmp1,tmp2,tmp3,tmp4,tmp5,tmp6,tmp7;
var tmp10,tmp11,tmp12,tmp13;
var z1,z2,z3,z4,z5,z11,z13;
var i;
var dataOff=0;
for(i=0;i<8;i++){
tmp0=data[dataOff+0]+data[dataOff+7];
tmp7=data[dataOff+0]-data[dataOff+7];
tmp1=data[dataOff+1]+data[dataOff+6];
tmp6=data[dataOff+1]-data[dataOff+6];
tmp2=data[dataOff+2]+data[dataOff+5];
tmp5=data[dataOff+2]-data[dataOff+5];
tmp3=data[dataOff+3]+data[dataOff+4];
tmp4=data[dataOff+3]-data[dataOff+4];
tmp10=tmp0+tmp3;
tmp13=tmp0-tmp3;
tmp11=tmp1+tmp2;
tmp12=tmp1-tmp2;
data[dataOff+0]=tmp10+tmp11;
data[dataOff+4]=tmp10-tmp11;
z1=(tmp12+tmp13)*0.707106781;
data[dataOff+2]=tmp13+z1;
data[dataOff+6]=tmp13-z1;
tmp10=tmp4+tmp5;
tmp11=tmp5+tmp6;
tmp12=tmp6+tmp7;
z5=(tmp10-tmp12)*0.382683433;
z2=0.541196100*tmp10+z5;
z4=1.306562965*tmp12+z5;
z3=tmp11*0.707106781;
z11=tmp7+z3;
z13=tmp7-z3;
data[dataOff+5]=z13+z2;
data[dataOff+3]=z13-z2;
data[dataOff+1]=z11+z4;
data[dataOff+7]=z11-z4;
dataOff+=8;
}
dataOff=0;
for(i=0;i<8;i++){
tmp0=data[dataOff+0]+data[dataOff+56];
tmp7=data[dataOff+0]-data[dataOff+56];
tmp1=data[dataOff+8]+data[dataOff+48];
tmp6=data[dataOff+8]-data[dataOff+48];
tmp2=data[dataOff+16]+data[dataOff+40];
tmp5=data[dataOff+16]-data[dataOff+40];
tmp3=data[dataOff+24]+data[dataOff+32];
tmp4=data[dataOff+24]-data[dataOff+32];
tmp10=tmp0+tmp3;
tmp13=tmp0-tmp3;
tmp11=tmp1+tmp2;
tmp12=tmp1-tmp2;
data[dataOff+0]=tmp10+tmp11;
data[dataOff+32]=tmp10-tmp11;
z1=(tmp12+tmp13)*0.707106781;
data[dataOff+16]=tmp13+z1;
data[dataOff+48]=tmp13-z1;
tmp10=tmp4+tmp5;
tmp11=tmp5+tmp6;
tmp12=tmp6+tmp7;
z5=(tmp10-tmp12)*0.382683433;
z2=0.541196100*tmp10+z5;
z4=1.306562965*tmp12+z5;
z3=tmp11*0.707106781;
z11=tmp7+z3;
z13=tmp7-z3;
data[dataOff+40]=z13+z2;
data[dataOff+24]=z13-z2;
data[dataOff+8]=z11+z4;
data[dataOff+56]=z11-z4;
dataOff++;
}
for(i=0;i<64;i++){
data[i]=Math.round((data[i]*fdtbl[i]));
}
return data;
},
"private function writeAPP0",function()
{
this.writeWord$1(0xFFE0);
this.writeWord$1(16);
this.writeByte$1(0x4A);
this.writeByte$1(0x46);
this.writeByte$1(0x49);
this.writeByte$1(0x46);
this.writeByte$1(0);
this.writeByte$1(1);
this.writeByte$1(1);
this.writeByte$1(0);
this.writeWord$1(1);
this.writeWord$1(1);
this.writeByte$1(0);
this.writeByte$1(0);
},
"private function writeSOF0",function(width,height)
{
this.writeWord$1(0xFFC0);
this.writeWord$1(17);
this.writeByte$1(8);
this.writeWord$1(height);
this.writeWord$1(width);
this.writeByte$1(3);
this.writeByte$1(1);
this.writeByte$1(0x11);
this.writeByte$1(0);
this.writeByte$1(2);
this.writeByte$1(0x11);
this.writeByte$1(1);
this.writeByte$1(3);
this.writeByte$1(0x11);
this.writeByte$1(1);
},
"private function writeDQT",function()
{
this.writeWord$1(0xFFDB);
this.writeWord$1(132);
this.writeByte$1(0);
var i;
for(i=0;i<64;i++){
this.writeByte$1(this.YTable$1[i]);
}
this.writeByte$1(1);
for(i=0;i<64;i++){
this.writeByte$1(this.UVTable$1[i]);
}
},
"private function writeDHT",function()
{
this.writeWord$1(0xFFC4);
this.writeWord$1(0x01A2);
var i;
this.writeByte$1(0);
for(i=0;i<16;i++){
this.writeByte$1(this.std_dc_luminance_nrcodes$1[i+1]);
}
for(i=0;i<=11;i++){
this.writeByte$1(this.std_dc_luminance_values$1[i]);
}
this.writeByte$1(0x10);
for(i=0;i<16;i++){
this.writeByte$1(this.std_ac_luminance_nrcodes$1[i+1]);
}
for(i=0;i<=161;i++){
this.writeByte$1(this.std_ac_luminance_values$1[i]);
}
this.writeByte$1(1);
for(i=0;i<16;i++){
this.writeByte$1(this.std_dc_chrominance_nrcodes$1[i+1]);
}
for(i=0;i<=11;i++){
this.writeByte$1(this.std_dc_chrominance_values$1[i]);
}
this.writeByte$1(0x11);
for(i=0;i<16;i++){
this.writeByte$1(this.std_ac_chrominance_nrcodes$1[i+1]);
}
for(i=0;i<=161;i++){
this.writeByte$1(this.std_ac_chrominance_values$1[i]);
}
},
"private function writeSOS",function()
{
this.writeWord$1(0xFFDA);
this.writeWord$1(12);
this.writeByte$1(3);
this.writeByte$1(1);
this.writeByte$1(0);
this.writeByte$1(2);
this.writeByte$1(0x11);
this.writeByte$1(3);
this.writeByte$1(0x11);
this.writeByte$1(0);
this.writeByte$1(0x3f);
this.writeByte$1(0);
},
"private var",{DU:function(){return(new Array(64));}},
"private function processDU",function(CDU,fdtbl,DC,HTDC,HTAC)
{
var EOB=HTAC[0x00];
var M16zeroes=HTAC[0xF0];
var i;
var DU_DCT=this.fDCTQuant$1(CDU,fdtbl);
for(i=0;i<64;i++){
this.DU$1[this.ZigZag$1[i]]=DU_DCT[i];
}
var Diff=this.DU$1[0]-DC;DC=this.DU$1[0];
if(Diff==0){
this.writeBits$1(HTDC[0]);
}else{
this.writeBits$1(HTDC[this.category$1[32767+Diff]]);
this.writeBits$1(this.bitcode$1[32767+Diff]);
}
var end0pos=63;
for(;(end0pos>0)&&(this.DU$1[end0pos]==0);end0pos--){
};
if(end0pos==0){
this.writeBits$1(EOB);
return DC;
}
i=1;
while(i<=end0pos){
var startpos=i;
for(;(this.DU$1[i]==0)&&(i<=end0pos);i++){
}
var nrzeroes=i-startpos;
if(nrzeroes>=16){
for(var nrmarker=1;nrmarker<=nrzeroes/16;nrmarker++){
this.writeBits$1(M16zeroes);
}
nrzeroes=$$int(nrzeroes&0xF);
}
this.writeBits$1(HTAC[nrzeroes*16+this.category$1[32767+this.DU$1[i]]]);
this.writeBits$1(this.bitcode$1[32767+this.DU$1[i]]);
i++;
}
if(end0pos!=63){
this.writeBits$1(EOB);
}
return DC;
},
"private var",{YDU:function(){return(new Array(64));}},
"private var",{UDU:function(){return(new Array(64));}},
"private var",{VDU:function(){return(new Array(64));}},
"private function RGB2YUV",function(img,xpos,ypos)
{
var pos=0;
for(var y=0;y<8;y++){
for(var x=0;x<8;x++){
var P=img.getPixel32(xpos+x,ypos+y);
var R=Number((P>>16)&0xFF);
var G=Number((P>>8)&0xFF);
var B=Number((P)&0xFF);
this.YDU$1[pos]=(((0.29900)*R+(0.58700)*G+(0.11400)*B))-128;
this.UDU$1[pos]=(((-0.16874)*R+(-0.33126)*G+(0.50000)*B));
this.VDU$1[pos]=(((0.50000)*R+(-0.41869)*G+(-0.08131)*B));
pos++;
}
}
},
"public function JPGEncoder",function(quality)
{if(arguments.length<1){quality=50;}this.ZigZag$1=this.ZigZag$1();this.YTable$1=this.YTable$1();this.UVTable$1=this.UVTable$1();this.fdtbl_Y$1=this.fdtbl_Y$1();this.fdtbl_UV$1=this.fdtbl_UV$1();this.std_dc_luminance_nrcodes$1=this.std_dc_luminance_nrcodes$1();this.std_dc_luminance_values$1=this.std_dc_luminance_values$1();this.std_ac_luminance_nrcodes$1=this.std_ac_luminance_nrcodes$1();this.std_ac_luminance_values$1=this.std_ac_luminance_values$1();this.std_dc_chrominance_nrcodes$1=this.std_dc_chrominance_nrcodes$1();this.std_dc_chrominance_values$1=this.std_dc_chrominance_values$1();this.std_ac_chrominance_nrcodes$1=this.std_ac_chrominance_nrcodes$1();this.std_ac_chrominance_values$1=this.std_ac_chrominance_values$1();this.bitcode$1=this.bitcode$1();this.category$1=this.category$1();this.DU$1=this.DU$1();this.YDU$1=this.YDU$1();this.UDU$1=this.UDU$1();this.VDU$1=this.VDU$1();
if(quality<=0){
quality=1;
}
if(quality>100){
quality=100;
}
var sf=0;
if(quality<50){
sf=$$int(5000/quality);
}else{
sf=$$int(200-quality*2);
}
this.initHuffmanTbl$1();
this.initCategoryNumber$1();
this.initQuantTables$1(sf);
},
"public function encode",function(image)
{
this.byteout$1=new flash.utils.ByteArray();
this.bytenew$1=0;
this.bytepos$1=7;
this.writeWord$1(0xFFD8);
this.writeAPP0$1();
this.writeDQT$1();
this.writeSOF0$1(image.width,image.height);
this.writeDHT$1();
this.writeSOS$1();
var DCY=0;
var DCU=0;
var DCV=0;
this.bytenew$1=0;
this.bytepos$1=7;
for(var ypos=0;ypos<image.height;ypos+=8){
for(var xpos=0;xpos<image.width;xpos+=8){
this.RGB2YUV$1(image,xpos,ypos);
DCY=this.processDU$1(this.YDU$1,this.fdtbl_Y$1,DCY,this.YDC_HT$1,this.YAC_HT$1);
DCU=this.processDU$1(this.UDU$1,this.fdtbl_UV$1,DCU,this.UVDC_HT$1,this.UVAC_HT$1);
DCV=this.processDU$1(this.VDU$1,this.fdtbl_UV$1,DCV,this.UVDC_HT$1,this.UVAC_HT$1);
}
}
if(this.bytepos$1>=0){
var fillbits=new com.adobe.images.BitString();
fillbits.len=this.bytepos$1+1;
fillbits.val=(1<<(this.bytepos$1+1))-1;
this.writeBits$1(fillbits);
}
this.writeWord$1(0xFFD9);
return this.byteout$1;
},
];},[],["Array","Math","com.adobe.images.BitString","uint","int","Number","flash.utils.ByteArray"], "0.8.0", "0.8.4"
);
// class com.adobe.images.PNGEncoder
joo.classLoader.prepare(
"package com.adobe.images",
"public class PNGEncoder",1,function($$private){;return[

"public static function encode",function(img){
var png=new flash.utils.ByteArray();
png.writeUnsignedInt(0x89504e47);
png.writeUnsignedInt(0x0D0A1A0A);
var IHDR=new flash.utils.ByteArray();
IHDR.writeInt(img.width);
IHDR.writeInt(img.height);
IHDR.writeUnsignedInt(0x08060000);
IHDR.writeByte(0);
$$private.writeChunk(png,0x49484452,IHDR);
var IDAT=new flash.utils.ByteArray();
for(var i=0;i<img.height;i++){
IDAT.writeByte(0);
var p;
var j;
if(!img.transparent){
for(j=0;j<img.width;j++){
p=img.getPixel(j,i);
IDAT.writeUnsignedInt(
$$uint(((p&0xFFFFFF)<<8)|0xFF));
}
}else{
for(j=0;j<img.width;j++){
p=img.getPixel32(j,i);
IDAT.writeUnsignedInt(
$$uint(((p&0xFFFFFF)<<8)|
(p>>>24)));
}
}
}
IDAT.compress();
$$private.writeChunk(png,0x49444154,IDAT);
$$private.writeChunk(png,0x49454E44,null);
return png;
},
"private static var",{crcTable:null},
"private static var",{crcTableComputed:false},
"private static function writeChunk",function(png,
type,data){
if(!$$private.crcTableComputed){
$$private.crcTableComputed=true;
$$private.crcTable=[];
var c;
for(var n=0;n<256;n++){
c=n;
for(var k=0;k<8;k++){
if(c&1){
c=$$uint($$uint(0xedb88320)^
$$uint(c>>>1));
}else{
c=$$uint(c>>>1);
}
}
$$private.crcTable[n]=c;
}
}
var len=0;
if(data!=null){
len=data.length;
}
png.writeUnsignedInt(len);
var p=png.position;
png.writeUnsignedInt(type);
if(data!=null){
png.writeBytes(data);
}
var e=png.position;
png.position=p;
c=0xffffffff;
for(var i=0;i<(e-p);i++){
c=$$uint($$private.crcTable[
(c^png.readUnsignedByte())&
$$uint(0xff)]^$$uint(c>>>8));
}
c=$$uint(c^$$uint(0xffffffff));
png.position=e;
png.writeUnsignedInt(c);
},
];},["encode"],["flash.utils.ByteArray","uint"], "0.8.0", "0.8.4"
);
// class com.adobe.net.DynamicURLLoader
joo.classLoader.prepare(
"package com.adobe.net",
"public dynamic class DynamicURLLoader extends flash.net.URLLoader",3,function($$private){;return[

"public function DynamicURLLoader",function()
{
this.super$3();
},
];},[],["flash.net.URLLoader"], "0.8.0", "0.8.4"
);
// class com.adobe.net.IURIResolver
joo.classLoader.prepare(
"package com.adobe.net",
"public interface IURIResolver",1,function($$private){;return[
,
];},[],[], "0.8.0", "0.8.4"
);
// class com.adobe.net.proxies.RFC2817Socket
joo.classLoader.prepare(
"package com.adobe.net.proxies",
"public class RFC2817Socket\n"+
"extends flash.net.Socket",3,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.events.ProgressEvent,flash.events.IOErrorEvent,flash.events.Event);},

"private var",{proxyHost:null},
"private var",{host:null},
"private var",{proxyPort:0},
"private var",{port:0},
"private var",{deferredEventHandlers:function(){return(new Object());}},
"private var",{buffer:function(){return(new String());}},
"public function RFC2817Socket",function(host,port)
{if(arguments.length<2){if(arguments.length<1){host=null;}port=0;}
this.super$3(host,port);this.deferredEventHandlers$3=this.deferredEventHandlers$3();this.buffer$3=this.buffer$3();
},
"public function setProxyInfo",function(host,port)
{
this.proxyHost$3=host;
this.proxyPort$3=port;
var deferredSocketDataHandler=this.deferredEventHandlers$3[flash.events.ProgressEvent.SOCKET_DATA];
var deferredConnectHandler=this.deferredEventHandlers$3[flash.events.Event.CONNECT];
if(deferredSocketDataHandler!=null)
{
this.removeEventListener(flash.events.ProgressEvent.SOCKET_DATA,deferredSocketDataHandler.listener,deferredSocketDataHandler.useCapture);
}
if(deferredConnectHandler!=null)
{
this.removeEventListener(flash.events.Event.CONNECT,deferredConnectHandler.listener,deferredConnectHandler.useCapture);
}
},
"public override function connect",function(host,port)
{
if(this.proxyHost$3==null)
{
this.redirectConnectEvent$3();
this.redirectSocketDataEvent$3();
this.connect$3(host,port);
}
else
{
this.host$3=host;
this.port$3=port;
this.addEventListener$3(flash.events.Event.CONNECT,$$bound(this,"onConnect$3"));
this.addEventListener$3(flash.events.ProgressEvent.SOCKET_DATA,$$bound(this,"onSocketData$3"));
this.connect$3(this.proxyHost$3,this.proxyPort$3);
}
},
"private function onConnect",function(event)
{
this.writeUTFBytes("CONNECT "+this.host$3+":"+this.port$3+" HTTP/1.1\n\n");
this.flush();
this.redirectConnectEvent$3();
},
"private function onSocketData",function(event)
{
while(this.bytesAvailable!=0)
{
this.buffer$3+=this.readUTFBytes(1);
if(this.buffer$3.search(/\r?\n\r?\n$/)!=-1)
{
this.checkResponse$3(event);
break;
}
}
},
"private function checkResponse",function(event)
{
var responseCode=this.buffer$3.substr(this.buffer$3.indexOf(" ")+1,3);
if(responseCode.search(/^2/)==-1)
{
var ioError=new flash.events.IOErrorEvent(flash.events.IOErrorEvent.IO_ERROR);
ioError.text="Error connecting to the proxy ["+this.proxyHost$3+"] on port ["+this.proxyPort$3+"]: "+this.buffer$3;
this.dispatchEvent(ioError);
}
else
{
this.redirectSocketDataEvent$3();
this.dispatchEvent(new flash.events.Event(flash.events.Event.CONNECT));
if(this.bytesAvailable>0)
{
this.dispatchEvent(event);
}
}
this.buffer$3=null;
},
"private function redirectConnectEvent",function()
{
this.removeEventListener(flash.events.Event.CONNECT,$$bound(this,"onConnect$3"));
var deferredEventHandler=this.deferredEventHandlers$3[flash.events.Event.CONNECT];
if(deferredEventHandler!=null)
{
this.addEventListener$3(flash.events.Event.CONNECT,deferredEventHandler.listener,deferredEventHandler.useCapture,deferredEventHandler.priority,deferredEventHandler.useWeakReference);
}
},
"private function redirectSocketDataEvent",function()
{
this.removeEventListener(flash.events.ProgressEvent.SOCKET_DATA,$$bound(this,"onSocketData$3"));
var deferredEventHandler=this.deferredEventHandlers$3[flash.events.ProgressEvent.SOCKET_DATA];
if(deferredEventHandler!=null)
{
this.addEventListener$3(flash.events.ProgressEvent.SOCKET_DATA,deferredEventHandler.listener,deferredEventHandler.useCapture,deferredEventHandler.priority,deferredEventHandler.useWeakReference);
}
},
"public override function addEventListener",function(type,listener,useCapture,priority,useWeakReference)
{if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){useCapture=false;}priority=0.0;}useWeakReference=false;}
if(type==flash.events.Event.CONNECT||type==flash.events.ProgressEvent.SOCKET_DATA)
{
this.deferredEventHandlers$3[type]={listener:listener,useCapture:useCapture,priority:priority,useWeakReference:useWeakReference};
}
else
{
this.addEventListener$3(type,listener,useCapture,priority,useWeakReference);
}
},
];},[],["flash.net.Socket","Object","String","flash.events.ProgressEvent","flash.events.Event","flash.events.IOErrorEvent"], "0.8.0", "0.8.4"
);
// class com.adobe.net.URI
joo.classLoader.prepare(
"package com.adobe.net",
"public class URI",1,function($$private){;return[

"public static const",{URImustEscape:" %"},
"public static const",{URIbaselineEscape:function(){return(com.adobe.net.URI.URImustEscape+":?#/@");}},
"public static const",{URIpathEscape:function(){return(com.adobe.net.URI.URImustEscape+"?#");}},
"public static const",{URIqueryEscape:function(){return(com.adobe.net.URI.URImustEscape+"#");}},
"public static const",{URIqueryPartEscape:function(){return(com.adobe.net.URI.URImustEscape+"#&=");}},
"public static const",{URInonHierEscape:function(){return(com.adobe.net.URI.URImustEscape+"?#/");}},
"public static const",{UNKNOWN_SCHEME:"unknown"},
"protected static const",{URIbaselineExcludedBitmap:function(){return(
new com.adobe.net.URIEncodingBitmap(com.adobe.net.URI.URIbaselineEscape));}},
"protected static const",{URIschemeExcludedBitmap:function(){return(
com.adobe.net.URI.URIbaselineExcludedBitmap);}},
"protected static const",{URIuserpassExcludedBitmap:function(){return(
com.adobe.net.URI.URIbaselineExcludedBitmap);}},
"protected static const",{URIauthorityExcludedBitmap:function(){return(
com.adobe.net.URI.URIbaselineExcludedBitmap);}},
"protected static const",{URIportExludedBitmap:function(){return(
com.adobe.net.URI.URIbaselineExcludedBitmap);}},
"protected static const",{URIpathExcludedBitmap:function(){return(
new com.adobe.net.URIEncodingBitmap(com.adobe.net.URI.URIpathEscape));}},
"protected static const",{URIqueryExcludedBitmap:function(){return(
new com.adobe.net.URIEncodingBitmap(com.adobe.net.URI.URIqueryEscape));}},
"protected static const",{URIqueryPartExcludedBitmap:function(){return(
new com.adobe.net.URIEncodingBitmap(com.adobe.net.URI.URIqueryPartEscape));}},
"protected static const",{URIfragmentExcludedBitmap:function(){return(
com.adobe.net.URI.URIqueryExcludedBitmap);}},
"protected static const",{URInonHierexcludedBitmap:function(){return(
new com.adobe.net.URIEncodingBitmap(com.adobe.net.URI.URInonHierEscape));}},
"public static const",{NOT_RELATED:0},
"public static const",{CHILD:1},
"public static const",{EQUAL:2},
"public static const",{PARENT:3},
"protected var",{_valid:false},
"protected var",{_relative:false},
"protected var",{_scheme:""},
"protected var",{_authority:""},
"protected var",{_username:""},
"protected var",{_password:""},
"protected var",{_port:""},
"protected var",{_path:""},
"protected var",{_query:""},
"protected var",{_fragment:""},
"protected var",{_nonHierarchical:""},
"protected static var",{_resolver:null},
"public function URI",function(uri)
{if(arguments.length<1){uri=null;}
if(uri==null)
this.initialize();
else
this.constructURI(uri);
},
"protected function constructURI",function(uri)
{
if(!this.parseURI(uri))
this._valid=false;
return this.isValid();
},
"protected function initialize",function()
{
this._valid=false;
this._relative=false;
this._scheme=com.adobe.net.URI.UNKNOWN_SCHEME;
this._authority="";
this._username="";
this._password="";
this._port="";
this._path="";
this._query="";
this._fragment="";
this._nonHierarchical="";
},
"protected function set hierState",function(state)
{
if(state)
{
this._nonHierarchical="";
if(this._scheme==""||this._scheme==com.adobe.net.URI.UNKNOWN_SCHEME)
this._relative=true;
else
this._relative=false;
if(this._authority.length==0&&this._path.length==0)
this._valid=false;
else
this._valid=true;
}
else
{
this._authority="";
this._username="";
this._password="";
this._port="";
this._path="";
this._relative=false;
if(this._scheme==""||this._scheme==com.adobe.net.URI.UNKNOWN_SCHEME)
this._valid=false;
else
this._valid=true;
}
},
"protected function get hierState",function()
{
return(this._nonHierarchical.length==0);
},
"protected function validateURI",function()
{
if(this.isAbsolute())
{
if(this._scheme.length<=1||this._scheme==com.adobe.net.URI.UNKNOWN_SCHEME)
{
return false;
}
else if(this.verifyAlpha(this._scheme)==false)
return false;
}
if(this.hierState)
{
if(this._path.search('\\')!=-1)
return false;
else if(this.isRelative()==false&&this._scheme==com.adobe.net.URI.UNKNOWN_SCHEME)
return false;
}
else
{
if(this._nonHierarchical.search('\\')!=-1)
return false;
}
return true;
},
"protected function parseURI",function(uri)
{
var baseURI=uri;
var index,index2;
this.initialize();
index=baseURI.indexOf("#");
if(index!=-1)
{
if(baseURI.length>(index+1))
this._fragment=baseURI.substr(index+1,baseURI.length-(index+1));
baseURI=baseURI.substr(0,index);
}
index=baseURI.indexOf("?");
if(index!=-1)
{
if(baseURI.length>(index+1))
this._query=baseURI.substr(index+1,baseURI.length-(index+1));
baseURI=baseURI.substr(0,index);
}
index=baseURI.search(':');
index2=baseURI.search('/');
var containsColon=(index!=-1);
var containsSlash=(index2!=-1);
var colonBeforeSlash=(!containsSlash||index<index2);
if(containsColon&&colonBeforeSlash)
{
this._scheme=baseURI.substr(0,index);
this._scheme=this._scheme.toLowerCase();
baseURI=baseURI.substr(index+1);
if(baseURI.substr(0,2)=="//")
{
this._nonHierarchical="";
baseURI=baseURI.substr(2,baseURI.length-2);
}
else
{
this._nonHierarchical=baseURI;
if((this._valid=this.validateURI())==false)
this.initialize();
return this.isValid();
}
}
else
{
this._scheme="";
this._relative=true;
this._nonHierarchical="";
}
if(this.isRelative())
{
this._authority="";
this._port="";
this._path=baseURI;
}
else
{
if(baseURI.substr(0,2)=="//")
{
while(baseURI.charAt(0)=="/")
baseURI=baseURI.substr(1,baseURI.length-1);
}
index=baseURI.search('/');
if(index==-1)
{
this._authority=baseURI;
this._path="";
}
else
{
this._authority=baseURI.substr(0,index);
this._path=baseURI.substr(index,baseURI.length-index);
}
index=this._authority.search('@');
if(index!=-1)
{
this._username=this._authority.substr(0,index);
this._authority=this._authority.substr(index+1);
index=this._username.search(':');
if(index!=-1)
{
this._password=this._username.substring(index+1,this._username.length);
this._username=this._username.substr(0,index);
}
else
this._password="";
}
else
{
this._username="";
this._password="";
}
index=this._authority.search(':');
if(index!=-1)
{
this._port=this._authority.substring(index+1,this._authority.length);
this._authority=this._authority.substr(0,index);
}
else
{
this._port="";
}
this._authority=this._authority.toLowerCase();
}
if((this._valid=this.validateURI())==false)
this.initialize();
return this.isValid();
},
"public function copyURI",function(uri)
{
this._scheme=uri._scheme;
this._authority=uri._authority;
this._username=uri._username;
this._password=uri._password;
this._port=uri._port;
this._path=uri._path;
this._query=uri._query;
this._fragment=uri._fragment;
this._nonHierarchical=uri._nonHierarchical;
this._valid=uri._valid;
this._relative=uri._relative;
},
"protected function verifyAlpha",function(str)
{
var pattern=/[^a-z]/;
var index;
str=str.toLowerCase();
index=str.search(pattern);
if(index==-1)
return true;
else
return false;
},
"public function isValid",function()
{
return this._valid;
},
"public function isAbsolute",function()
{
return!this._relative;
},
"public function isRelative",function()
{
return this._relative;
},
"public function isDirectory",function()
{
if(this._path.length==0)
return false;
return(this._path.charAt(this.path.length-1)=='/');
},
"public function isHierarchical",function()
{
return this.hierState;
},
"public function get scheme",function()
{
return com.adobe.net.URI.unescapeChars(this._scheme);
},
"public function set scheme",function(schemeStr)
{
var normalized=schemeStr.toLowerCase();
this._scheme=com.adobe.net.URI.fastEscapeChars(normalized,com.adobe.net.URI.URIschemeExcludedBitmap);
},
"public function get authority",function()
{
return com.adobe.net.URI.unescapeChars(this._authority);
},
"public function set authority",function(authorityStr)
{
authorityStr=authorityStr.toLowerCase();
this._authority=com.adobe.net.URI.fastEscapeChars(authorityStr,
com.adobe.net.URI.URIauthorityExcludedBitmap);
this.hierState=true;
},
"public function get username",function()
{
return com.adobe.net.URI.unescapeChars(this._username);
},
"public function set username",function(usernameStr)
{
this._username=com.adobe.net.URI.fastEscapeChars(usernameStr,com.adobe.net.URI.URIuserpassExcludedBitmap);
this.hierState=true;
},
"public function get password",function()
{
return com.adobe.net.URI.unescapeChars(this._password);
},
"public function set password",function(passwordStr)
{
this._password=com.adobe.net.URI.fastEscapeChars(passwordStr,
com.adobe.net.URI.URIuserpassExcludedBitmap);
this.hierState=true;
},
"public function get port",function()
{
return com.adobe.net.URI.unescapeChars(this._port);
},
"public function set port",function(portStr)
{
this._port=com.adobe.net.URI.escapeChars(portStr);
this.hierState=true;
},
"public function get path",function()
{
return com.adobe.net.URI.unescapeChars(this._path);
},
"public function set path",function(pathStr)
{
this._path=com.adobe.net.URI.fastEscapeChars(pathStr,com.adobe.net.URI.URIpathExcludedBitmap);
if(this._scheme==com.adobe.net.URI.UNKNOWN_SCHEME)
{
this._scheme="";
}
this.hierState=true;
},
"public function get query",function()
{
return com.adobe.net.URI.unescapeChars(this._query);
},
"public function set query",function(queryStr)
{
this._query=com.adobe.net.URI.fastEscapeChars(queryStr,com.adobe.net.URI.URIqueryExcludedBitmap);
},
"public function get queryRaw",function()
{
return this._query;
},
"public function set queryRaw",function(queryStr)
{
this._query=queryStr;
},
"public function get fragment",function()
{
return com.adobe.net.URI.unescapeChars(this._fragment);
},
"public function set fragment",function(fragmentStr)
{
this._fragment=com.adobe.net.URI.fastEscapeChars(fragmentStr,com.adobe.net.URI.URIfragmentExcludedBitmap);
},
"public function get nonHierarchical",function()
{
return com.adobe.net.URI.unescapeChars(this._nonHierarchical);
},
"public function set nonHierarchical",function(nonHier)
{
this._nonHierarchical=com.adobe.net.URI.fastEscapeChars(nonHier,com.adobe.net.URI.URInonHierexcludedBitmap);
this.hierState=false;
},
"public function setParts",function(schemeStr,authorityStr,
portStr,pathStr,queryStr,
fragmentStr)
{
this.scheme=schemeStr;
this.authority=authorityStr;
this.port=portStr;
this.path=pathStr;
this.query=queryStr;
this.fragment=fragmentStr;
this.hierState=true;
},
"static public function escapeChars",function(unescaped)
{
return com.adobe.net.URI.fastEscapeChars(unescaped,com.adobe.net.URI.URIbaselineExcludedBitmap);
},
"static public function unescapeChars",function(escaped)
{
var unescaped;
unescaped=decodeURIComponent(escaped);
return unescaped;
},
"static public function fastEscapeChars",function(unescaped,bitmap)
{
var escaped="";
var c;
var x,i;
for(i=0;i<unescaped.length;i++)
{
c=unescaped.charAt(i);
x=bitmap.ShouldEscape(c);
if(x)
{
c=x.toString(16);
if(c.length==1)
c="0"+c;
c="%"+c;
c=c.toUpperCase();
}
escaped+=c;
}
return escaped;
},
"public function isOfType",function(scheme)
{
scheme=scheme.toLowerCase();
return(this._scheme==scheme);
},
"public function getQueryValue",function(name)
{
var map;
var item;
var value;
map=this.getQueryByMap();
for(item in map)
{
if(item==name)
{
value=map[item];
return value;
}
}
return new String("");
},
"public function setQueryValue",function(name,value)
{
var map;
map=this.getQueryByMap();
map[name]=value;
this.setQueryByMap(map);
},
"public function getQueryByMap",function()
{
var queryStr;
var pair;
var pairs;
var item;
var name,value;
var index;
var map=new Object();
queryStr=this._query;
pairs=queryStr.split('&');
for(var $1 in pairs)
{pair=pairs[$1];
if(pair.length==0)
continue;
item=pair.split('=');
if(item.length>0)
name=item[0];
else
continue;
if(item.length>1)
value=item[1];
else
value="";
name=com.adobe.net.URI.queryPartUnescape(name);
value=com.adobe.net.URI.queryPartUnescape(value);
map[name]=value;
}
return map;
},
"public function setQueryByMap",function(map)
{
var item;
var name,value;
var queryStr="";
var tmpPair;
var foo;
for(item in map)
{
name=item;
value=map[item];
if(value==null)
value="";
name=com.adobe.net.URI.queryPartEscape(name);
value=com.adobe.net.URI.queryPartEscape(value);
tmpPair=name;
if(value.length>0)
{
tmpPair+="=";
tmpPair+=value;
}
if(queryStr.length!=0)
queryStr+='&';
queryStr+=tmpPair;
}
this._query=queryStr;
},
"static public function queryPartEscape",function(unescaped)
{
var escaped=unescaped;
escaped=com.adobe.net.URI.fastEscapeChars(unescaped,com.adobe.net.URI.URIqueryPartExcludedBitmap);
return escaped;
},
"static public function queryPartUnescape",function(escaped)
{
var unescaped=escaped;
unescaped=com.adobe.net.URI.unescapeChars(unescaped);
return unescaped;
},
"public function toString",function()
{
if(this==null)
return"";
else
return this.toStringInternal(false);
},
"public function toDisplayString",function()
{
return this.toStringInternal(true);
},
"protected function toStringInternal",function(forDisplay)
{
var uri="";
var part="";
if(this.isHierarchical()==false)
{
uri+=(forDisplay?this.scheme:this._scheme);
uri+=":";
uri+=(forDisplay?this.nonHierarchical:this._nonHierarchical);
}
else
{
if(this.isRelative()==false)
{
if(this._scheme.length!=0)
{
part=(forDisplay?this.scheme:this._scheme);
uri+=part+":";
}
if(this._authority.length!=0||this.isOfType("file"))
{
uri+="//";
if(this._username.length!=0)
{
part=(forDisplay?this.username:this._username);
uri+=part;
if(this._password.length!=0)
{
part=(forDisplay?this.password:this._password);
uri+=":"+part;
}
uri+="@";
}
part=(forDisplay?this.authority:this._authority);
uri+=part;
if(this.port.length!=0)
uri+=":"+this.port;
}
}
part=(forDisplay?this.path:this._path);
uri+=part;
}
if(this._query.length!=0)
{
part=(forDisplay?this.query:this._query);
uri+="?"+part;
}
if(this.fragment.length!=0)
{
part=(forDisplay?this.fragment:this._fragment);
uri+="#"+part;
}
return uri;
},
"public function forceEscape",function()
{
this.scheme=this.scheme;
this.setQueryByMap(this.getQueryByMap());
this.fragment=this.fragment;
if(this.isHierarchical())
{
this.authority=this.authority;
this.path=this.path;
this.port=this.port;
this.username=this.username;
this.password=this.password;
}
else
{
this.nonHierarchical=this.nonHierarchical;
}
},
"public function isOfFileType",function(extension)
{
var thisExtension;
var index;
index=extension.lastIndexOf(".");
if(index!=-1)
{
extension=extension.substr(index+1);
}
else
{
}
thisExtension=this.getExtension(true);
if(thisExtension=="")
return false;
if(com.adobe.net.URI.compareStr(thisExtension,extension,false)==0)
return true;
else
return false;
},
"public function getExtension",function(minusDot)
{if(arguments.length<1){minusDot=false;}
var filename=this.getFilename();
var extension;
var index;
if(filename=="")
return String("");
index=filename.lastIndexOf(".");
if(index==-1||index==0)
return String("");
extension=filename.substr(index);
if(minusDot&&extension.charAt(0)==".")
extension=extension.substr(1);
return extension;
},
"public function getFilename",function(minusExtension)
{if(arguments.length<1){minusExtension=false;}
if(this.isDirectory())
return String("");
var pathStr=this.path;
var filename;
var index;
index=pathStr.lastIndexOf("/");
if(index!=-1)
filename=pathStr.substr(index+1);
else
filename=pathStr;
if(minusExtension)
{
index=filename.lastIndexOf(".");
if(index!=-1)
filename=filename.substr(0,index);
}
return filename;
},
"static protected function compareStr",function(str1,str2,
sensitive)
{if(arguments.length<3){sensitive=true;}
if(sensitive==false)
{
str1=str1.toLowerCase();
str2=str2.toLowerCase();
}
return(str1==str2);
},
"public function getDefaultPort",function()
{
if(this._scheme=="http")
return String("80");
else if(this._scheme=="ftp")
return String("21");
else if(this._scheme=="file")
return String("");
else if(this._scheme=="sftp")
return String("22");
else
{
return String("");
}
},
"static protected function resolve",function(uri)
{
var copy=new com.adobe.net.URI();
copy.copyURI(uri);
if(com.adobe.net.URI._resolver!=null)
{
return com.adobe.net.URI._resolver.resolve(copy);
}
else
{
return copy;
}
},
"static public function set resolver",function(resolver)
{
com.adobe.net.URI._resolver=resolver;
},
"static public function get resolver",function()
{
return com.adobe.net.URI._resolver;
},
"public function getRelation",function(uri,caseSensitive)
{if(arguments.length<2){caseSensitive=true;}
var thisURI=com.adobe.net.URI.resolve(this);
var thatURI=com.adobe.net.URI.resolve(uri);
if(thisURI.isRelative()||thatURI.isRelative())
{
return com.adobe.net.URI.NOT_RELATED;
}
else if(thisURI.isHierarchical()==false||thatURI.isHierarchical()==false)
{
if(((thisURI.isHierarchical()==false)&&(thatURI.isHierarchical()==true))||
((thisURI.isHierarchical()==true)&&(thatURI.isHierarchical()==false)))
{
return com.adobe.net.URI.NOT_RELATED;
}
else
{
if(thisURI.scheme!=thatURI.scheme)
return com.adobe.net.URI.NOT_RELATED;
if(thisURI.nonHierarchical!=thatURI.nonHierarchical)
return com.adobe.net.URI.NOT_RELATED;
return com.adobe.net.URI.EQUAL;
}
}
if(thisURI.scheme!=thatURI.scheme)
return com.adobe.net.URI.NOT_RELATED;
if(thisURI.authority!=thatURI.authority)
return com.adobe.net.URI.NOT_RELATED;
var thisPort=thisURI.port;
var thatPort=thatURI.port;
if(thisPort=="")
thisPort=thisURI.getDefaultPort();
if(thatPort=="")
thatPort=thatURI.getDefaultPort();
if(thisPort!=thatPort)
return com.adobe.net.URI.NOT_RELATED;
if(com.adobe.net.URI.compareStr(thisURI.path,thatURI.path,caseSensitive))
return com.adobe.net.URI.EQUAL;
var thisPath=thisURI.path;
var thatPath=thatURI.path;
if((thisPath=="/"||thatPath=="/")&&
(thisPath==""||thatPath==""))
{
return com.adobe.net.URI.EQUAL;
}
var thisParts,thatParts;
var thisPart,thatPart;
var i;
thisParts=thisPath.split("/");
thatParts=thatPath.split("/");
if(thisParts.length>thatParts.length)
{
thatPart=thatParts[thatParts.length-1];
if(thatPart.length>0)
{
return com.adobe.net.URI.NOT_RELATED;
}
else
{
thatParts.pop();
}
for(i=0;i<thatParts.length;i++)
{
thisPart=thisParts[i];
thatPart=thatParts[i];
if(com.adobe.net.URI.compareStr(thisPart,thatPart,caseSensitive)==false)
return com.adobe.net.URI.NOT_RELATED;
}
return com.adobe.net.URI.CHILD;
}
else if(thisParts.length<thatParts.length)
{
thisPart=thisParts[thisParts.length-1];
if(thisPart.length>0)
{
return com.adobe.net.URI.NOT_RELATED;
}
else
{
thisParts.pop();
}
for(i=0;i<thisParts.length;i++)
{
thisPart=thisParts[i];
thatPart=thatParts[i];
if(com.adobe.net.URI.compareStr(thisPart,thatPart,caseSensitive)==false)
return com.adobe.net.URI.NOT_RELATED;
}
return com.adobe.net.URI.PARENT;
}
else
{
return com.adobe.net.URI.NOT_RELATED;
}
return com.adobe.net.URI.NOT_RELATED;
},
"public function getCommonParent",function(uri,caseSensitive)
{if(arguments.length<2){caseSensitive=true;}
var thisURI=com.adobe.net.URI.resolve(this);
var thatURI=com.adobe.net.URI.resolve(uri);
if(!thisURI.isAbsolute()||!thatURI.isAbsolute()||
thisURI.isHierarchical()==false||
thatURI.isHierarchical()==false)
{
return null;
}
var relation=thisURI.getRelation(thatURI);
if(relation==com.adobe.net.URI.NOT_RELATED)
{
return null;
}
thisURI.chdir(".");
thatURI.chdir(".");
var strBefore,strAfter;
do
{
relation=thisURI.getRelation(thatURI,caseSensitive);
if(relation==com.adobe.net.URI.EQUAL||relation==com.adobe.net.URI.PARENT)
break;
strBefore=thisURI.toString();
thisURI.chdir("..");
strAfter=thisURI.toString();
}
while(strBefore!=strAfter);
return thisURI;
},
"public function chdir",function(reference,escape)
{if(arguments.length<2){escape=false;}
var uriReference;
var ref=reference;
if(escape)
ref=com.adobe.net.URI.escapeChars(reference);
if(ref=="")
{
return true;
}
else if(ref.substr(0,2)=="//")
{
var final=this.scheme+":"+ref;
return this.constructURI(final);
}
else if(ref.charAt(0)=="?")
{
ref="./"+ref;
}
uriReference=new com.adobe.net.URI(ref);
if(uriReference.isAbsolute()||
uriReference.isHierarchical()==false)
{
this.copyURI(uriReference);
return true;
}
var thisPath,thatPath;
var thisParts,thatParts;
var thisIsDir=false,thatIsDir=false;
var thisIsAbs=false,thatIsAbs=false;
var lastIsDotOperation=false;
var curDir;
var i;
thisPath=this.path;
thatPath=uriReference.path;
if(thisPath.length>0)
thisParts=thisPath.split("/");
else
thisParts=new Array();
if(thatPath.length>0)
thatParts=thatPath.split("/");
else
thatParts=new Array();
if(thisParts.length>0&&thisParts[0]=="")
{
thisIsAbs=true;
thisParts.shift();
}
if(thisParts.length>0&&thisParts[thisParts.length-1]=="")
{
thisIsDir=true;
thisParts.pop();
}
if(thatParts.length>0&&thatParts[0]=="")
{
thatIsAbs=true;
thatParts.shift();
}
if(thatParts.length>0&&thatParts[thatParts.length-1]=="")
{
thatIsDir=true;
thatParts.pop();
}
if(thatIsAbs)
{
this.path=uriReference.path;
this.queryRaw=uriReference.queryRaw;
this.fragment=uriReference.fragment;
return true;
}
else if(thatParts.length==0&&uriReference.query=="")
{
this.fragment=uriReference.fragment;
return true;
}
else if(thisIsDir==false&&thisParts.length>0)
{
thisParts.pop();
}
this.queryRaw=uriReference.queryRaw;
this.fragment=uriReference.fragment;
thisParts=thisParts.concat(thatParts);
for(i=0;i<thisParts.length;i++)
{
curDir=thisParts[i];
lastIsDotOperation=false;
if(curDir==".")
{
thisParts.splice(i,1);
i=i-1;
lastIsDotOperation=true;
}
else if(curDir=="..")
{
if(i>=1)
{
if(thisParts[i-1]=="..")
{
}
else
{
thisParts.splice(i-1,2);
i=i-2;
}
}
else
{
if(this.isRelative())
{
}
else
{
thisParts.splice(i,1);
i=i-1;
}
}
lastIsDotOperation=true;
}
}
var finalPath="";
thatIsDir=thatIsDir||lastIsDotOperation;
finalPath=this.joinPath(thisParts,thisIsAbs,thatIsDir);
this.path=finalPath;
return true;
},
"protected function joinPath",function(parts,isAbs,isDir)
{
var pathStr="";
var i;
for(i=0;i<parts.length;i++)
{
if(pathStr.length>0)
pathStr+="/";
pathStr+=parts[i];
}
if(isDir&&pathStr.length>0)
pathStr+="/";
if(isAbs)
pathStr="/"+pathStr;
return pathStr;
},
"public function makeAbsoluteURI",function(base_uri)
{
if(this.isAbsolute()||base_uri.isRelative())
{
return false;
}
var base=new com.adobe.net.URI();
base.copyURI(base_uri);
if(base.chdir(this.toString())==false)
return false;
this.copyURI(base);
return true;
},
"public function makeRelativeURI",function(base_uri,caseSensitive)
{if(arguments.length<2){caseSensitive=true;}
var base=new com.adobe.net.URI();
base.copyURI(base_uri);
var thisParts,thatParts;
var finalParts=new Array();
var thisPart,thatPart,finalPath;
var pathStr=this.path;
var queryStr=this.queryRaw;
var fragmentStr=this.fragment;
var i;
var diff=false;
var isDir=false;
if(this.isRelative())
{
return true;
}
if(base.isRelative())
{
return false;
}
if((this.isOfType(base_uri.scheme)==false)||
(this.authority!=base_uri.authority))
{
return false;
}
isDir=this.isDirectory();
base.chdir(".");
thisParts=pathStr.split("/");
thatParts=base.path.split("/");
if(thisParts.length>0&&thisParts[0]=="")
thisParts.shift();
if(thisParts.length>0&&thisParts[thisParts.length-1]=="")
{
isDir=true;
thisParts.pop();
}
if(thatParts.length>0&&thatParts[0]=="")
thatParts.shift();
if(thatParts.length>0&&thatParts[thatParts.length-1]=="")
thatParts.pop();
while(thatParts.length>0)
{
if(thisParts.length==0)
{
break;
}
thisPart=thisParts[0];
thatPart=thatParts[0];
if(com.adobe.net.URI.compareStr(thisPart,thatPart,caseSensitive))
{
thisParts.shift();
thatParts.shift();
}
else
break;
}
var dotdot="..";
for(i=0;i<thatParts.length;i++)
{
finalParts.push(dotdot);
}
finalParts=finalParts.concat(thisParts);
finalPath=this.joinPath(finalParts,false,isDir);
if(finalPath.length==0)
{
finalPath="./";
}
this.setParts("","","",finalPath,queryStr,fragmentStr);
return true;
},
"public function unknownToURI",function(unknown,defaultScheme)
{if(arguments.length<2){defaultScheme="http";}
var temp;
if(unknown.length==0)
{
this.initialize();
return false;
}
unknown=unknown.replace(/\\/g,"/");
if(unknown.length>=2)
{
temp=unknown.substr(0,2);
if(temp=="//")
unknown=defaultScheme+":"+unknown;
}
if(unknown.length>=3)
{
temp=unknown.substr(0,3);
if(temp=="://")
unknown=defaultScheme+unknown;
}
var uri=new com.adobe.net.URI(unknown);
if(uri.isHierarchical()==false)
{
if(uri.scheme==com.adobe.net.URI.UNKNOWN_SCHEME)
{
this.initialize();
return false;
}
this.copyURI(uri);
this.forceEscape();
return true;
}
else if((uri.scheme!=com.adobe.net.URI.UNKNOWN_SCHEME)&&
(uri.scheme.length>0))
{
if((uri.authority.length>0)||
(uri.scheme=="file"))
{
this.copyURI(uri);
this.forceEscape();
return true;
}
else if(uri.authority.length==0&&uri.path.length==0)
{
this.setParts(uri.scheme,"","","","","");
return false;
}
}
else
{
var path=uri.path;
if(path==".."||path=="."||
(path.length>=3&&path.substr(0,3)=="../")||
(path.length>=2&&path.substr(0,2)=="./"))
{
this.copyURI(uri);
this.forceEscape();
return true;
}
}
uri=new com.adobe.net.URI(defaultScheme+"://"+unknown);
if(uri.scheme.length>0&&uri.authority.length>0)
{
this.copyURI(uri);
this.forceEscape();
return true;
}
this.initialize();
return false;
},
];},["escapeChars","unescapeChars","fastEscapeChars","queryPartEscape","queryPartUnescape","resolver"],["com.adobe.net.URIEncodingBitmap","String","Object","Array"], "0.8.0", "0.8.4"
);
// class com.adobe.net.URIEncodingBitmap
joo.classLoader.prepare(
"package com.adobe.net",
"public class URIEncodingBitmap extends flash.utils.ByteArray",2,function($$private){;return[

"public function URIEncodingBitmap",function(charsToEscape)
{this.super$2();
var i;
var data=new flash.utils.ByteArray();
for(i=0;i<16;i++)
this.writeByte(0);
data.writeUTFBytes(charsToEscape);
data.position=0;
while(data.bytesAvailable)
{
var c=data.readByte();
if(c>0x7f)
continue;
var enc;
this.position=(c>>3);
enc=this.readByte();
enc|=1<<(c&0x7);
this.position=(c>>3);
this.writeByte(enc);
}
},
"public function ShouldEscape",function(char)
{
var data=new flash.utils.ByteArray();
var c,mask;
data.writeUTFBytes(char);
data.position=0;
c=data.readByte();
if(c&0x80)
{
return 0;
}
else if((c<0x1f)||(c==0x7f))
{
return c;
}
this.position=(c>>3);
mask=this.readByte();
if(mask&(1<<(c&0x7)))
{
return c;
}
else
{
return 0;
}
},
];},[],["flash.utils.ByteArray"], "0.8.0", "0.8.4"
);
// class com.adobe.serialization.json.JSON
joo.classLoader.prepare(
"package com.adobe.serialization.json",
"public class JSON",1,function($$private){var is=joo.is;return[

"static public function deserialize",function(source){
source=new String(source);
var at=0;
var ch=' ';
var _isDigit;
var _isHexDigit;
var _white;
var _string;
var _next;
var _array;
var _object;
var _number;
var _word;
var _value;
var _error;
_isDigit=function(c){
return(("0"<=c)&&(c<="9"));
};
_isHexDigit=function(c){
return(_isDigit(c)||(("A"<=c)&&(c<="F"))||(("a"<=c)&&(c<="f")));
};
_error=function(m){
throw new Error(m,at-1);
};
_next=function(){
ch=source.charAt(at);
at+=1;
return ch;
};
_white=function(){
while(ch){
if(ch<=' '){
_next();
}else if(ch=='/'){
switch(_next()){
case'/':
while(_next()&&ch!='\n'&&ch!='\r'){}
break;
case'*':
_next();
for(;;){
if(ch){
if(ch=='*'){
if(_next()=='/'){
_next();
break;
}
}else{
_next();
}
}else{
_error("Unterminated Comment");
}
}
break;
default:
_error("Syntax Error");
}
}else{
break;
}
}
};
_string=function(){
var i='';
var s='';
var t;
var u;
var outer=false;
if(ch=='"'){
while(_next()){
if(ch=='"')
{
_next();
return s;
}
else if(ch=='\\')
{
switch(_next()){
case'b':
s+='\b';
break;
case'f':
s+='\f';
break;
case'n':
s+='\n';
break;
case'r':
s+='\r';
break;
case't':
s+='\t';
break;
case'u':
u=0;
for(i=0;i<4;i+=1){
t=parseInt(_next(),16);
if(!isFinite(t)){
outer=true;
break;
}
u=u*16+t;
}
if(outer){
outer=false;
break;
}
s+=String.fromCharCode(u);
break;
default:
s+=ch;
}
}else{
s+=ch;
}
}
}
_error("Bad String");
return null;
};
_array=function(){
var a=[];
if(ch=='['){
_next();
_white();
if(ch==']'){
_next();
return a;
}
while(ch){
a.push(_value());
_white();
if(ch==']'){
_next();
return a;
}else if(ch!=','){
break;
}
_next();
_white();
if(ch==']'){
_next();
return a;
}
}
}
_error("Bad Array");
return null;
};
_object=function(){
var k={};
var o={};
if(ch=='{'){
_next();
_white();
if(ch=='}')
{
_next();
return o;
}
while(ch)
{
k=_string();
_white();
if(ch!=':')
{
break;
}
_next();
o[k]=_value();
_white();
if(ch=='}'){
_next();
return o;
}else if(ch!=','){
break;
}
_next();
_white();
if(ch=='}'){
_next();
return o;
}
}
}
_error("Bad Object");
};
_number=function(){
var n='';
var e='';
var v;
var exp;
var hex='';
var sign='';
if(ch=='-'){
n='-';
sign=n;
_next();
}
if(ch=="0"){
_next();
if((ch=="x")||(ch=="X")){
_next();
while(_isHexDigit(ch)){
hex+=ch;
_next();
}
if(hex==""){
_error("mal formed Hexadecimal");
}else{
return Number(sign+"0x"+hex);
}
}else{
n+="0";
}
}
while(_isDigit(ch)){
n+=ch;
_next();
}
if(ch=='.'){
n+='.';
while(_next()&&ch>='0'&&ch<='9'){
n+=ch;
}
}
v=1*n;
if(!isFinite(v)){
_error("Bad Number");
}else{
if((ch=='e')||(ch=='E'))
{
_next();
var expSign=(ch=='-')?-1:1;
if((ch=='+')||(ch=='-'))
{
_next();
}
if(_isDigit(ch))
{
e+=ch;
}
else
{
_error("Bad Exponent");
}
while(_next()&&ch>='0'&&ch<='9')
{
e+=ch;
}
exp=expSign*e;
if(!isFinite(v))
{
_error("Bad Exponent");
}
else
{
v=v*Math.pow(10,exp);
}
}
return v;
}
return NaN;
};
_word=function(){
switch(ch){
case't':
if(_next()=='r'&&_next()=='u'&&_next()=='e'){
_next();
return true;
}
break;
case'f':
if(_next()=='a'&&_next()=='l'&&_next()=='s'&&_next()=='e'){
_next();
return false;
}
break;
case'n':
if(_next()=='u'&&_next()=='l'&&_next()=='l'){
_next();
return null;
}
break;
}
_error("Syntax Error");
return null;
};
_value=function(){
_white();
switch(ch){
case'{':
return _object();
case'[':
return _array();
case'"':
return _string();
case'-':
return _number();
default:
return ch>='0'&&ch<='9'?_number():_word();
}
};
return _value();
},
"static public function serialize",function(o){
var c;
var i;
var l;
var s='';
var v;
switch(typeof o)
{
case'object':
if(o)
{
if(is(o,Array))
{
l=o.length;
for(i=0;i<l;++i)
{
v=com.adobe.serialization.json.JSON.serialize(o[i]);
if(s)s+=',';
s+=v;
}
return'['+s+']';
}
else if(typeof(o.toString)!='undefined')
{
for(var prop in o)
{
v=o[prop];
if((typeof(v)!='undefined')&&(typeof(v)!='function'))
{
v=com.adobe.serialization.json.JSON.serialize(v);
if(s)s+=',';
s+=com.adobe.serialization.json.JSON.serialize(prop)+':'+v;
}
}
return"{"+s+"}";
}
}
return'null';
case'number':
return isFinite(o)?String(o):'null';
case'string':
l=o.length;
s='"';
for(i=0;i<l;i+=1){
c=o.charAt(i);
if(c>=' '){
if(c=='\\'||c=='"')
{
s+='\\';
}
s+=c;
}
else
{
switch(c)
{
case'\b':
s+='\\b';
break;
case'\f':
s+='\\f';
break;
case'\n':
s+='\\n';
break;
case'\r':
s+='\\r';
break;
case'\t':
s+='\\t';
break;
default:
var code=c.charCodeAt();
s+='\\u00'+(Math.floor(code/16).toString(16))+((code%16).toString(16));
}
}
}
return s+'"';
case'boolean':
return String(o);
default:
return'null';
}
},
];},["deserialize","serialize"],["String","Error","Number","Math","Array"], "0.8.0", "0.8.4"
);
// class com.adobe.serialization.json.JSONDecoder
joo.classLoader.prepare(
"package com.adobe.serialization.json",
"public class JSONDecoder",1,function($$private){;return[function(){joo.classLoader.init(com.adobe.serialization.json.JSONTokenType);},
"private var",{value:undefined},
"private var",{tokenizer:null},
"private var",{token:null},
"public function JSONDecoder",function(s){
this.tokenizer$1=new com.adobe.serialization.json.JSONTokenizer(s);
this.nextToken$1();
this.value$1=this.parseValue$1();
},
"public function getValue",function(){
return this.value$1;
},
"private function nextToken",function(){
return this.token$1=this.tokenizer$1.getNextToken();
},
"private function parseArray",function(){
var a=new Array();
this.nextToken$1();
if(this.token$1.type==com.adobe.serialization.json.JSONTokenType.RIGHT_BRACKET){
return a;
}
while(true){
a.push(this.parseValue$1());
this.nextToken$1();
if(this.token$1.type==com.adobe.serialization.json.JSONTokenType.RIGHT_BRACKET){
return a;
}else if(this.token$1.type==com.adobe.serialization.json.JSONTokenType.COMMA){
this.nextToken$1();
}else{
this.tokenizer$1.parseError("Expecting ] or , but found "+this.token$1.value);
}
}
return null;
},
"private function parseObject",function(){
var o=new Object();
var key;
this.nextToken$1();
if(this.token$1.type==com.adobe.serialization.json.JSONTokenType.RIGHT_BRACE){
return o;
}
while(true){
if(this.token$1.type==com.adobe.serialization.json.JSONTokenType.STRING){
key=String(this.token$1.value);
this.nextToken$1();
if(this.token$1.type==com.adobe.serialization.json.JSONTokenType.COLON){
this.nextToken$1();
o[key]=this.parseValue$1();
this.nextToken$1();
if(this.token$1.type==com.adobe.serialization.json.JSONTokenType.RIGHT_BRACE){
return o;
}else if(this.token$1.type==com.adobe.serialization.json.JSONTokenType.COMMA){
this.nextToken$1();
}else{
this.tokenizer$1.parseError("Expecting } or , but found "+this.token$1.value);
}
}else{
this.tokenizer$1.parseError("Expecting : but found "+this.token$1.value);
}
}else{
this.tokenizer$1.parseError("Expecting string but found "+this.token$1.value);
}
}
return null;
},
"private function parseValue",function(){
switch(this.token$1.type){
case com.adobe.serialization.json.JSONTokenType.LEFT_BRACE:
return this.parseObject$1();
case com.adobe.serialization.json.JSONTokenType.LEFT_BRACKET:
return this.parseArray$1();
case com.adobe.serialization.json.JSONTokenType.STRING:
case com.adobe.serialization.json.JSONTokenType.NUMBER:
case com.adobe.serialization.json.JSONTokenType.TRUE:
case com.adobe.serialization.json.JSONTokenType.FALSE:
case com.adobe.serialization.json.JSONTokenType.NULL:
return this.token$1.value;
default:
this.tokenizer$1.parseError("Unexpected "+this.token$1.value);
}
return null;
},
];},[],["com.adobe.serialization.json.JSONTokenizer","Array","com.adobe.serialization.json.JSONTokenType","Object","String"], "0.8.0", "0.8.4"
);
// class com.adobe.serialization.json.JSONEncoder
joo.classLoader.prepare(
"package com.adobe.serialization.json",
"public class JSONEncoder",1,function($$private){var is=joo.is,as=joo.as;return[
"private var",{jsonString:null},
"public function JSONEncoder",function(value){
this.jsonString$1=this.convertToString$1(value);
},
"public function getString",function(){
return this.jsonString$1;
},
"private function convertToString",function(value){
if(is(value,String)){
return this.escapeString$1(as(value,String));
}else if(is(value,Number)){
return isFinite(as(value,Number))?value.toString():"null";
}else if(is(value,Boolean)){
return value?"true":"false";
}else if(is(value,Array)){
return this.arrayToString$1(as(value,Array));
}else if(is(value,Object)&&value!=null){
return this.objectToString$1(value);
}
return"null";
},
"private function escapeString",function(str){
var s="";
var ch;
var len=str.length;
for(var i=0;i<len;i++){
ch=str.charAt(i);
switch(ch){
case'"':
s+="\\\"";
break;
case'\\':
s+="\\\\";
break;
case'\b':
s+="\\b";
break;
case'\f':
s+="\\f";
break;
case'\n':
s+="\\n";
break;
case'\r':
s+="\\r";
break;
case'\t':
s+="\\t";
break;
default:
if(ch<' '){
var hexCode=ch.charCodeAt(0).toString(16);
var zeroPad=hexCode.length==2?"00":"000";
s+="\\u"+zeroPad+hexCode;
}else{
s+=ch;
}
}
}
return"\""+s+"\"";
},
"private function arrayToString",function(a){
var s="";
for(var i=0;i<a.length;i++){
if(s.length>0){
s+=",";
}
s+=this.convertToString$1(a[i]);
}
return"["+s+"]";
},
"private function objectToString",function(o)
{
var s="";
var classInfo=flash.utils.describeType(o);
if(classInfo['@name'].toString()=="Object")
{
var value;
for(var key in o)
{
value=o[key];
if(is(value,Function))
{
continue;
}
if(s.length>0){
s+=",";
}
s+=this.escapeString$1(key)+":"+this.convertToString$1(value);
}
}
else
{
}
return"{"+s+"}";
},
];},[],["String","Number","Boolean","Array","Object","Function"], "0.8.0", "0.8.4"
);
// class com.adobe.serialization.json.JSONParseError
joo.classLoader.prepare(
"package com.adobe.serialization.json",
"public class JSONParseError extends Error",2,function($$private){;return[
"private var",{_location:0},
"private var",{_text:null},
"public function JSONParseError",function(message,location,text){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){message="";}location=0;}text="";}
this.super$2(message);
this._location$2=location;
this._text$2=text;
},
"public function get location",function(){
return this._location$2;
},
"public function get text",function(){
return this._text$2;
},
];},[],["Error"], "0.8.0", "0.8.4"
);
// class com.adobe.serialization.json.JSONToken
joo.classLoader.prepare(
"package com.adobe.serialization.json",
"public class JSONToken",1,function($$private){;return[
"private var",{_type:0},
"private var",{_value:null},
"public function JSONToken",function(type,value){if(arguments.length<2){if(arguments.length<1){type=-1;}value=null;}
this._type$1=type;
this._value$1=value;
},
"public function get type",function(){
return this._type$1;
},
"public function set type",function(value){
this._type$1=value;
},
"public function get value",function(){
return this._value$1;
},
"public function set value",function(v){
this._value$1=v;
},
];},[],[], "0.8.0", "0.8.4"
);
// class com.adobe.serialization.json.JSONTokenizer
joo.classLoader.prepare(
"package com.adobe.serialization.json",
"public class JSONTokenizer",1,function($$private){;return[function(){joo.classLoader.init(com.adobe.serialization.json.JSONTokenType);},
"private var",{obj:null},
"private var",{jsonString:null},
"private var",{loc:0},
"private var",{ch:null},
"public function JSONTokenizer",function(s){
this.jsonString$1=s;
this.loc$1=0;
this.nextChar$1();
},
"public function getNextToken",function(){
var token=new com.adobe.serialization.json.JSONToken();
this.skipIgnored$1();
switch(this.ch$1){
case'{':
token.type=com.adobe.serialization.json.JSONTokenType.LEFT_BRACE;
token.value='{';
this.nextChar$1();
break;
case'}':
token.type=com.adobe.serialization.json.JSONTokenType.RIGHT_BRACE;
token.value='}';
this.nextChar$1();
break;
case'[':
token.type=com.adobe.serialization.json.JSONTokenType.LEFT_BRACKET;
token.value='[';
this.nextChar$1();
break;
case']':
token.type=com.adobe.serialization.json.JSONTokenType.RIGHT_BRACKET;
token.value=']';
this.nextChar$1();
break;
case',':
token.type=com.adobe.serialization.json.JSONTokenType.COMMA;
token.value=',';
this.nextChar$1();
break;
case':':
token.type=com.adobe.serialization.json.JSONTokenType.COLON;
token.value=':';
this.nextChar$1();
break;
case't':
var possibleTrue="t"+this.nextChar$1()+this.nextChar$1()+this.nextChar$1();
if(possibleTrue=="true"){
token.type=com.adobe.serialization.json.JSONTokenType.TRUE;
token.value=true;
this.nextChar$1();
}else{
this.parseError("Expecting 'true' but found "+possibleTrue);
}
break;
case'f':
var possibleFalse="f"+this.nextChar$1()+this.nextChar$1()+this.nextChar$1()+this.nextChar$1();
if(possibleFalse=="false"){
token.type=com.adobe.serialization.json.JSONTokenType.FALSE;
token.value=false;
this.nextChar$1();
}else{
this.parseError("Expecting 'false' but found "+possibleFalse);
}
break;
case'n':
var possibleNull="n"+this.nextChar$1()+this.nextChar$1()+this.nextChar$1();
if(possibleNull=="null"){
token.type=com.adobe.serialization.json.JSONTokenType.NULL;
token.value=null;
this.nextChar$1();
}else{
this.parseError("Expecting 'null' but found "+possibleNull);
}
break;
case'"':
token=this.readString$1();
break;
default:
if(this.isDigit$1(this.ch$1)||this.ch$1=='-'){
token=this.readNumber$1();
}else if(this.ch$1==''){
return null;
}else{
this.parseError("Unexpected "+this.ch$1+" encountered");
}
}
return token;
},
"private function readString",function(){
var token=new com.adobe.serialization.json.JSONToken();
token.type=com.adobe.serialization.json.JSONTokenType.STRING;
var string="";
this.nextChar$1();
while(this.ch$1!='"'&&this.ch$1!=''){
if(this.ch$1=='\\'){
this.nextChar$1();
switch(this.ch$1){
case'"':
string+='"';
break;
case'/':
string+="/";
break;
case'\\':
string+='\\';
break;
case'b':
string+='\b';
break;
case'f':
string+='\f';
break;
case'n':
string+='\n';
break;
case'r':
string+='\r';
break;
case't':
string+='\t';
break;
case'u':
var hexValue="";
for(var i=0;i<4;i++){
if(!this.isHexDigit$1(this.nextChar$1())){
this.parseError(" Excepted a hex digit, but found: "+this.ch$1);
}
hexValue+=this.ch$1;
}
string+=String.fromCharCode(parseInt(hexValue,16));
break;
default:
string+='\\'+this.ch$1;
}
}else{
string+=this.ch$1;
}
this.nextChar$1();
}
if(this.ch$1==''){
this.parseError("Unterminated string literal");
}
this.nextChar$1();
token.value=string;
return token;
},
"private function readNumber",function(){
var token=new com.adobe.serialization.json.JSONToken();
token.type=com.adobe.serialization.json.JSONTokenType.NUMBER;
var input="";
if(this.ch$1=='-'){
input+='-';
this.nextChar$1();
}
if(!this.isDigit$1(this.ch$1))
{
this.parseError("Expecting a digit");
}
if(this.ch$1=='0')
{
input+=this.ch$1;
this.nextChar$1();
if(this.isDigit$1(this.ch$1))
{
this.parseError("A digit cannot immediately follow 0");
}
}
else
{
while(this.isDigit$1(this.ch$1)){
input+=this.ch$1;
this.nextChar$1();
}
}
if(this.ch$1=='.'){
input+='.';
this.nextChar$1();
if(!this.isDigit$1(this.ch$1))
{
this.parseError("Expecting a digit");
}
while(this.isDigit$1(this.ch$1)){
input+=this.ch$1;
this.nextChar$1();
}
}
if(this.ch$1=='e'||this.ch$1=='E')
{
input+="e";
this.nextChar$1();
if(this.ch$1=='+'||this.ch$1=='-')
{
input+=this.ch$1;
this.nextChar$1();
}
if(!this.isDigit$1(this.ch$1))
{
this.parseError("Scientific notation number needs exponent value");
}
while(this.isDigit$1(this.ch$1))
{
input+=this.ch$1;
this.nextChar$1();
}
}
var num=Number(input);
if(isFinite(num)&&!isNaN(num)){
token.value=num;
return token;
}else{
this.parseError("Number "+num+" is not valid!");
}
return null;
},
"private function nextChar",function(){
return this.ch$1=this.jsonString$1.charAt(this.loc$1++);
},
"private function skipIgnored",function(){
this.skipWhite$1();
this.skipComments$1();
this.skipWhite$1();
},
"private function skipComments",function(){
if(this.ch$1=='/'){
this.nextChar$1();
switch(this.ch$1){
case'/':
do{
this.nextChar$1();
}while(this.ch$1!='\n'&&this.ch$1!='');
this.nextChar$1();
break;
case'*':
this.nextChar$1();
while(true){
if(this.ch$1=='*'){
this.nextChar$1();
if(this.ch$1=='/'){
this.nextChar$1();
break;
}
}else{
this.nextChar$1();
}
if(this.ch$1==''){
this.parseError("Multi-line comment not closed");
}
}
break;
default:
this.parseError("Unexpected "+this.ch$1+" encountered (expecting '/' or '*' )");
}
}
},
"private function skipWhite",function(){
while(this.isWhiteSpace$1(this.ch$1)){
this.nextChar$1();
}
},
"private function isWhiteSpace",function(ch){
return(ch==' '||ch=='\t'||ch=='\n');
},
"private function isDigit",function(ch){
return(ch>='0'&&ch<='9');
},
"private function isHexDigit",function(ch){
var uc=ch.toUpperCase();
return(this.isDigit$1(ch)||(uc>='A'&&uc<='F'));
},
"public function parseError",function(message){
throw new com.adobe.serialization.json.JSONParseError(message,this.loc$1,this.jsonString$1);
},
];},[],["com.adobe.serialization.json.JSONToken","com.adobe.serialization.json.JSONTokenType","String","Number","com.adobe.serialization.json.JSONParseError"], "0.8.0", "0.8.4"
);
// class com.adobe.serialization.json.JSONTokenType
joo.classLoader.prepare(
"package com.adobe.serialization.json",
"public class JSONTokenType",1,function($$private){;return[
"public static const",{UNKNOWN:-1},
"public static const",{COMMA:0},
"public static const",{LEFT_BRACE:1},
"public static const",{RIGHT_BRACE:2},
"public static const",{LEFT_BRACKET:3},
"public static const",{RIGHT_BRACKET:4},
"public static const",{COLON:6},
"public static const",{TRUE:7},
"public static const",{FALSE:8},
"public static const",{NULL:9},
"public static const",{STRING:10},
"public static const",{NUMBER:11},
];},[],[], "0.8.0", "0.8.4"
);
// class com.adobe.utils.ArrayUtil
joo.classLoader.prepare(
"package com.adobe.utils",
"public class ArrayUtil",1,function($$private){;return[

"public static function arrayContainsValue",function(arr,value)
{
return(arr.indexOf(value)!=-1);
},
"public static function removeValueFromArray",function(arr,value)
{
var len=arr.length;
for(var i=len;i>-1;i--)
{
if(arr[i]===value)
{
arr.splice(i,1);
}
}
},
"public static function createUniqueCopy",function(a)
{
var newArray=new Array();
var len=a.length;
var item;
for(var i=0;i<len;++i)
{
item=a[i];
if(com.adobe.utils.ArrayUtil.arrayContainsValue(newArray,item))
{
continue;
}
newArray.push(item);
}
return newArray;
},
"public static function copyArray",function(arr)
{
return arr.slice();
},
"public static function arraysAreEqual",function(arr1,arr2)
{
if(arr1.length!=arr2.length)
{
return false;
}
var len=arr1.length;
for(var i=0;i<len;i++)
{
if(arr1[i]!==arr2[i])
{
return false;
}
}
return true;
},
];},["arrayContainsValue","removeValueFromArray","createUniqueCopy","copyArray","arraysAreEqual"],["Array"], "0.8.0", "0.8.4"
);
// class com.adobe.utils.DateUtil
joo.classLoader.prepare(
"package com.adobe.utils",
"public class DateUtil",1,function($$private){var is=joo.is;return[function(){joo.classLoader.init(mx.formatters.DateBase);},

"public static function getShortMonthName",function(d)
{
return mx.formatters.DateBase.monthNamesShort[d.getMonth()];
},
"public static function getShortMonthIndex",function(m)
{
return mx.formatters.DateBase.monthNamesShort.indexOf(m);
},
"public static function getFullMonthName",function(d)
{
return mx.formatters.DateBase.monthNamesLong[d.getMonth()];
},
"public static function getFullMonthIndex",function(m)
{
return mx.formatters.DateBase.monthNamesLong.indexOf(m);
},
"public static function getShortDayName",function(d)
{
return mx.formatters.DateBase.dayNamesShort[d.getDay()];
},
"public static function getShortDayIndex",function(d)
{
return mx.formatters.DateBase.dayNamesShort.indexOf(d);
},
"public static function getFullDayName",function(d)
{
return mx.formatters.DateBase.dayNamesLong[d.getDay()];
},
"public static function getFullDayIndex",function(d)
{
return mx.formatters.DateBase.dayNamesLong.indexOf(d);
},
"public static function getShortYear",function(d)
{
var dStr=String(d.getFullYear());
if(dStr.length<3)
{
return dStr;
}
return(dStr.substr(dStr.length-2));
},
"public static function compareDates",function(d1,d2)
{
var d1ms=d1.getTime();
var d2ms=d2.getTime();
if(d1ms>d2ms)
{
return-1;
}
else if(d1ms<d2ms)
{
return 1;
}
else
{
return 0;
}
},
"public static function getShortHour",function(d)
{
var h=d.hours;
if(h==0||h==12)
{
return 12;
}
else if(h>12)
{
return h-12;
}
else
{
return h;
}
},
"public static function getAMPM",function(d)
{
return(d.hours>11)?"PM":"AM";
},
"public static function parseRFC822",function(str)
{
var finalDate;
try
{
var dateParts=str.split(" ");
var day=null;
if(dateParts[0].search(/\d/)==-1)
{
day=dateParts.shift().replace(/\W/,"");
}
var date=Number(dateParts.shift());
var month=Number(com.adobe.utils.DateUtil.getShortMonthIndex(dateParts.shift()));
var year=Number(dateParts.shift());
var timeParts=dateParts.shift().split(":");
var hour=$$int(timeParts.shift());
var minute=$$int(timeParts.shift());
var second=(timeParts.length>0)?$$int(timeParts.shift()):0;
var milliseconds=Date.UTC(year,month,date,hour,minute,second,0);
var timezone=dateParts.shift();
var offset=0;
if(timezone.search(/\d/)==-1)
{
switch(timezone)
{
case"UT":
offset=0;
break;
case"UTC":
offset=0;
break;
case"GMT":
offset=0;
break;
case"EST":
offset=(-5*3600000);
break;
case"EDT":
offset=(-4*3600000);
break;
case"CST":
offset=(-6*3600000);
break;
case"CDT":
offset=(-5*3600000);
break;
case"MST":
offset=(-7*3600000);
break;
case"MDT":
offset=(-6*3600000);
break;
case"PST":
offset=(-8*3600000);
break;
case"PDT":
offset=(-7*3600000);
break;
case"Z":
offset=0;
break;
case"A":
offset=(-1*3600000);
break;
case"M":
offset=(-12*3600000);
break;
case"N":
offset=(1*3600000);
break;
case"Y":
offset=(12*3600000);
break;
default:
offset=0;
}
}
else
{
var multiplier=1;
var oHours=0;
var oMinutes=0;
if(timezone.length!=4)
{
if(timezone.charAt(0)=="-")
{
multiplier=-1;
}
timezone=timezone.substr(1,4);
}
oHours=Number(timezone.substr(0,2));
oMinutes=Number(timezone.substr(2,2));
offset=(((oHours*3600000)+(oMinutes*60000))*multiplier);
}
finalDate=new Date(milliseconds-offset);
if(finalDate.toString()=="Invalid Date")
{
throw new Error("This date does not conform to RFC822.");
}
}
catch(e){if(is(e,Error))
{
var eStr="Unable to parse the string ["+str+"] into a date. ";
eStr+="The internal error was: "+e.toString();
throw new Error(eStr);
}else throw e;}
return finalDate;
},
"public static function toRFC822",function(d)
{
var date=d.getUTCDate();
var hours=d.getUTCHours();
var minutes=d.getUTCMinutes();
var seconds=d.getUTCSeconds();
var sb=new String();
sb+=mx.formatters.DateBase.dayNamesShort[d.getUTCDay()];
sb+=", ";
if(date<10)
{
sb+="0";
}
sb+=date;
sb+=" ";
sb+=mx.formatters.DateBase.monthNamesShort[d.getUTCMonth()];
sb+=" ";
sb+=d.getUTCFullYear();
sb+=" ";
if(hours<10)
{
sb+="0";
}
sb+=hours;
sb+=":";
if(minutes<10)
{
sb+="0";
}
sb+=minutes;
sb+=":";
if(seconds<10)
{
sb+="0";
}
sb+=seconds;
sb+=" GMT";
return sb;
},
"public static function parseW3CDTF",function(str)
{
var finalDate;
try
{
var dateStr=str.substring(0,str.indexOf("T"));
var timeStr=str.substring(str.indexOf("T")+1,str.length);
var dateArr=dateStr.split("-");
var year=Number(dateArr.shift());
var month=Number(dateArr.shift());
var date=Number(dateArr.shift());
var multiplier;
var offsetHours;
var offsetMinutes;
var offsetStr;
if(timeStr.indexOf("Z")!=-1)
{
multiplier=1;
offsetHours=0;
offsetMinutes=0;
timeStr=timeStr.replace("Z","");
}
else if(timeStr.indexOf("+")!=-1)
{
multiplier=1;
offsetStr=timeStr.substring(timeStr.indexOf("+")+1,timeStr.length);
offsetHours=Number(offsetStr.substring(0,offsetStr.indexOf(":")));
offsetMinutes=Number(offsetStr.substring(offsetStr.indexOf(":")+1,offsetStr.length));
timeStr=timeStr.substring(0,timeStr.indexOf("+"));
}
else
{
multiplier=-1;
offsetStr=timeStr.substring(timeStr.indexOf("-")+1,timeStr.length);
offsetHours=Number(offsetStr.substring(0,offsetStr.indexOf(":")));
offsetMinutes=Number(offsetStr.substring(offsetStr.indexOf(":")+1,offsetStr.length));
timeStr=timeStr.substring(0,timeStr.indexOf("-"));
}
var timeArr=timeStr.split(":");
var hour=Number(timeArr.shift());
var minutes=Number(timeArr.shift());
var secondsArr=(timeArr.length>0)?String(timeArr.shift()).split("."):null;
var seconds=(secondsArr!=null&&secondsArr.length>0)?Number(secondsArr.shift()):0;
var milliseconds=(secondsArr!=null&&secondsArr.length>0)?Number(secondsArr.shift()):0;
var utc=Date.UTC(year,month-1,date,hour,minutes,seconds,milliseconds);
var offset=(((offsetHours*3600000)+(offsetMinutes*60000))*multiplier);
finalDate=new Date(utc-offset);
if(finalDate.toString()=="Invalid Date")
{
throw new Error("This date does not conform to W3CDTF.");
}
}
catch(e){if(is(e,Error))
{
var eStr="Unable to parse the string ["+str+"] into a date. ";
eStr+="The internal error was: "+e.toString();
throw new Error(eStr);
}else throw e;}
return finalDate;
},
"public static function toW3CDTF",function(d,includeMilliseconds)
{if(arguments.length<2){includeMilliseconds=false;}
var date=d.getUTCDate();
var month=d.getUTCMonth();
var hours=d.getUTCHours();
var minutes=d.getUTCMinutes();
var seconds=d.getUTCSeconds();
var milliseconds=d.getUTCMilliseconds();
var sb=new String();
sb+=d.getUTCFullYear();
sb+="-";
if(month+1<10)
{
sb+="0";
}
sb+=month+1;
sb+="-";
if(date<10)
{
sb+="0";
}
sb+=date;
sb+="T";
if(hours<10)
{
sb+="0";
}
sb+=hours;
sb+=":";
if(minutes<10)
{
sb+="0";
}
sb+=minutes;
sb+=":";
if(seconds<10)
{
sb+="0";
}
sb+=seconds;
if(includeMilliseconds&&milliseconds>0)
{
sb+=".";
sb+=milliseconds;
}
sb+="-00:00";
return sb;
},
];},["getShortMonthName","getShortMonthIndex","getFullMonthName","getFullMonthIndex","getShortDayName","getShortDayIndex","getFullDayName","getFullDayIndex","getShortYear","compareDates","getShortHour","getAMPM","parseRFC822","toRFC822","parseW3CDTF","toW3CDTF"],["mx.formatters.DateBase","String","Number","int","Date","Error"], "0.8.0", "0.8.4"
);
// class com.adobe.utils.DictionaryUtil
joo.classLoader.prepare(
"package com.adobe.utils",
"public class DictionaryUtil",1,function($$private){;return[

"public static function getKeys",function(d)
{
var a=new Array();
for(var key in d)
{
a.push(key);
}
return a;
},
"public static function getValues",function(d)
{
var a=new Array();
for(var $1 in d)
{var value=d[$1];
a.push(value);
}
return a;
},
];},["getKeys","getValues"],["Array"], "0.8.0", "0.8.4"
);
// class com.adobe.utils.IntUtil
joo.classLoader.prepare(
"package com.adobe.utils",
"public class IntUtil",1,function($$private){;return[
"public static function rol",function(x,n){
return(x<<n)|(x>>>(32-n));
},
"public static function ror",function(x,n){
var nn=32-n;
return(x<<nn)|(x>>>(32-nn));
},
"private static var",{hexChars:"0123456789abcdef"},
"public static function toHex",function(n,bigEndian){if(arguments.length<2){bigEndian=false;}
var s="";
if(bigEndian){
for(var i=0;i<4;i++){
s+=$$private.hexChars.charAt((n>>((3-i)*8+4))&0xF)
+$$private.hexChars.charAt((n>>((3-i)*8))&0xF);
}
}else{
for(var x=0;x<4;x++){
s+=$$private.hexChars.charAt((n>>(x*8+4))&0xF)
+$$private.hexChars.charAt((n>>(x*8))&0xF);
}
}
return s;
},
];},["rol","ror","toHex"],[], "0.8.0", "0.8.4"
);
// class com.adobe.utils.NumberFormatter
joo.classLoader.prepare(
"package com.adobe.utils",
"public class NumberFormatter",1,function($$private){;return[

"public static function addLeadingZero",function(n)
{
var out=String(n);
if(n<10&&n>-1)
{
out="0"+out;
}
return out;
},
];},["addLeadingZero"],["String"], "0.8.0", "0.8.4"
);
// class com.adobe.utils.StringUtil
joo.classLoader.prepare(
"package com.adobe.utils",
"public class StringUtil",1,function($$private){;return[

"public static function stringsAreEqual",function(s1,s2,
caseSensitive)
{
if(caseSensitive)
{
return(s1==s2);
}
else
{
return(s1.toUpperCase()==s2.toUpperCase());
}
},
"public static function trim",function(input)
{
return com.adobe.utils.StringUtil.ltrim(com.adobe.utils.StringUtil.rtrim(input));
},
"public static function ltrim",function(input)
{
var size=input.length;
for(var i=0;i<size;i++)
{
if(input.charCodeAt(i)>32)
{
return input.substring(i);
}
}
return"";
},
"public static function rtrim",function(input)
{
var size=input.length;
for(var i=size;i>0;i--)
{
if(input.charCodeAt(i-1)>32)
{
return input.substring(0,i);
}
}
return"";
},
"public static function beginsWith",function(input,prefix)
{
return(prefix==input.substring(0,prefix.length));
},
"public static function endsWith",function(input,suffix)
{
return(suffix==input.substring(input.length-suffix.length));
},
"public static function remove",function(input,remove)
{
return com.adobe.utils.StringUtil.replace(input,remove,"");
},
"public static function replace",function(input,replace,replaceWith)
{
var sb=new String();
var found=false;
var sLen=input.length;
var rLen=replace.length;
for(var i=0;i<sLen;i++)
{
if(input.charAt(i)==replace.charAt(0))
{
found=true;
for(var j=0;j<rLen;j++)
{
if(!(input.charAt(i+j)==replace.charAt(j)))
{
found=false;
break;
}
}
if(found)
{
sb+=replaceWith;
i=i+(rLen-1);
continue;
}
}
sb+=input.charAt(i);
}
return sb;
},
];},["stringsAreEqual","trim","ltrim","rtrim","beginsWith","endsWith","remove","replace"],["String"], "0.8.0", "0.8.4"
);
// class com.adobe.utils.XMLUtil
joo.classLoader.prepare(
"package com.adobe.utils",
"public class XMLUtil",1,function($$private){var is=joo.is;return[

"public static const",{TEXT:"text"},
"public static const",{COMMENT:"comment"},
"public static const",{PROCESSING_INSTRUCTION:"processing-instruction"},
"public static const",{ATTRIBUTE:"attribute"},
"public static const",{ELEMENT:"element"},
"public static function isValidXML",function(data)
{
var xml;
try
{
xml=new XML(data);
}
catch(e){if(is(e,Error))
{
return false;
}else throw e;}
if(xml.nodeKind()!=com.adobe.utils.XMLUtil.ELEMENT)
{
return false;
}
return true;
},
"public static function getNextSibling",function(x)
{
return com.adobe.utils.XMLUtil.getSiblingByIndex(x,1);
},
"public static function getPreviousSibling",function(x)
{
return com.adobe.utils.XMLUtil.getSiblingByIndex(x,-1);
},
"protected static function getSiblingByIndex",function(x,count)
{
var out;
try
{
out=x.parent().children()[x.childIndex()+count];
}
catch(e){if(is(e,Error))
{
return null;
}else throw e;}
return out;
},
];},["isValidXML","getNextSibling","getPreviousSibling"],["XML","Error"], "0.8.0", "0.8.4"
);
// class com.adobe.webapis.events.ServiceEvent
joo.classLoader.prepare(
"package com.adobe.webapis.events",
"public class ServiceEvent extends flash.events.Event",2,function($$private){;return[

"private var",{_data:function(){return(new Object());}},
"public function ServiceEvent",function(type,bubbles,
cancelable)
{if(arguments.length<3){if(arguments.length<2){bubbles=false;}cancelable=false;}
this.super$2(type,bubbles,cancelable);this._data$2=this._data$2();
},
"public function get data",function()
{
return this._data$2;
},
"public function set data",function(d)
{
this._data$2=d;
},
];},[],["flash.events.Event","Object"], "0.8.0", "0.8.4"
);
// class com.adobe.webapis.ServiceBase
joo.classLoader.prepare(
"package com.adobe.webapis",
"public class ServiceBase extends flash.events.EventDispatcher",2,function($$private){;return[

"public function ServiceBase",function()
{this.super$2();
},
];},[],["flash.events.EventDispatcher"], "0.8.0", "0.8.4"
);
// class com.adobe.webapis.URLLoaderBase
joo.classLoader.prepare(
"package com.adobe.webapis",
{Event:{name:"progress",type:"flash.events.ProgressEvent"}},
{Event:{name:"ioError",type:"flash.events.IOErrorEvent"}},
{Event:{name:"securityError",type:"flash.events.SecurityErrorEvent"}},
"public class URLLoaderBase extends com.adobe.webapis.ServiceBase",3,function($$private){var $$bound=joo.boundMethod;return[

"protected function getURLLoader",function()
{
var loader=new com.adobe.net.DynamicURLLoader();
loader.addEventListener("progress",$$bound(this,"onProgress$3"));
loader.addEventListener("ioError",$$bound(this,"onIOError$3"));
loader.addEventListener("securityError",$$bound(this,"onSecurityError$3"));
return loader;
},
"private function onIOError",function(event)
{
this.dispatchEvent(event);
},
"private function onSecurityError",function(event)
{
this.dispatchEvent(event);
},
"private function onProgress",function(event)
{
this.dispatchEvent(event);
},
];},[],["com.adobe.webapis.ServiceBase","com.adobe.net.DynamicURLLoader"], "0.8.0", "0.8.4"
);
// class com.serialization.json.JSON
joo.classLoader.prepare(
"package com.serialization.json",
"public class JSON",1,function($$private){var is=joo.is;return[

"static public function deserialize",function(source){
source=new String(source);
var at=0;
var ch=' ';
var _isDigit;
var _isHexDigit;
var _white;
var _string;
var _next;
var _array;
var _object;
var _number;
var _word;
var _value;
var _error;
_isDigit=function(c){
return(("0"<=c)&&(c<="9"));
};
_isHexDigit=function(c){
return(_isDigit(c)||(("A"<=c)&&(c<="F"))||(("a"<=c)&&(c<="f")));
};
_error=function(m){
throw new Error(m,at-1);
};
_next=function(){
ch=source.charAt(at);
at+=1;
return ch;
};
_white=function(){
while(ch){
if(ch<=' '){
_next();
}else if(ch=='/'){
switch(_next()){
case'/':
while(_next()&&ch!='\n'&&ch!='\r'){}
break;
case'*':
_next();
for(;;){
if(ch){
if(ch=='*'){
if(_next()=='/'){
_next();
break;
}
}else{
_next();
}
}else{
_error("Unterminated Comment");
}
}
break;
default:
_error("Syntax Error");
}
}else{
break;
}
}
};
_string=function(){
var i='';
var s='';
var t;
var u;
var outer=false;
if(ch=='"'){
while(_next()){
if(ch=='"')
{
_next();
return s;
}
else if(ch=='\\')
{
switch(_next()){
case'b':
s+='\b';
break;
case'f':
s+='\f';
break;
case'n':
s+='\n';
break;
case'r':
s+='\r';
break;
case't':
s+='\t';
break;
case'u':
u=0;
for(i=0;i<4;i+=1){
t=parseInt(_next(),16);
if(!isFinite(t)){
outer=true;
break;
}
u=u*16+t;
}
if(outer){
outer=false;
break;
}
s+=String.fromCharCode(u);
break;
default:
s+=ch;
}
}else{
s+=ch;
}
}
}
_error("Bad String");
return null;
};
_array=function(){
var a=[];
if(ch=='['){
_next();
_white();
if(ch==']'){
_next();
return a;
}
while(ch){
a.push(_value());
_white();
if(ch==']'){
_next();
return a;
}else if(ch!=','){
break;
}
_next();
_white();
}
}
_error("Bad Array");
return null;
};
_object=function(){
var k={};
var o={};
if(ch=='{'){
_next();
_white();
if(ch=='}')
{
_next();
return o;
}
while(ch)
{
k=_string();
_white();
if(ch!=':')
{
break;
}
_next();
o[k]=_value();
_white();
if(ch=='}'){
_next();
return o;
}else if(ch!=','){
break;
}
_next();
_white();
}
}
_error("Bad Object");
};
_number=function(){
var n='';
var v;
var hex='';
var sign='';
if(ch=='-'){
n='-';
sign=n;
_next();
}
if(ch=="0"){
_next();
if((ch=="x")||(ch=="X")){
_next();
while(_isHexDigit(ch)){
hex+=ch;
_next();
}
if(hex==""){
_error("mal formed Hexadecimal");
}else{
return Number(sign+"0x"+hex);
}
}else{
n+="0";
}
}
while(_isDigit(ch)){
n+=ch;
_next();
}
if(ch=='.'){
n+='.';
while(_next()&&ch>='0'&&ch<='9'){
n+=ch;
}
}
v=1*n;
if(!isFinite(v)){
_error("Bad Number");
}else{
return v;
}
return NaN;
};
_word=function(){
switch(ch){
case't':
if(_next()=='r'&&_next()=='u'&&_next()=='e'){
_next();
return true;
}
break;
case'f':
if(_next()=='a'&&_next()=='l'&&_next()=='s'&&_next()=='e'){
_next();
return false;
}
break;
case'n':
if(_next()=='u'&&_next()=='l'&&_next()=='l'){
_next();
return null;
}
break;
}
_error("Syntax Error");
return null;
};
_value=function(){
_white();
switch(ch){
case'{':
return _object();
case'[':
return _array();
case'"':
return _string();
case'-':
return _number();
default:
return ch>='0'&&ch<='9'?_number():_word();
}
};
return _value();
},
"static public function serialize",function(o){
var c;
var i;
var l;
var s='';
var v;
switch(typeof o)
{
case'object':
if(o)
{
if(is(o,Array))
{
l=o.length;
for(i=0;i<l;++i)
{
v=com.serialization.json.JSON.serialize(o[i]);
if(s)s+=',';
s+=v;
}
return'['+s+']';
}
else if(typeof(o.toString)!='undefined')
{
for(var prop in o)
{
v=o[prop];
if((typeof(v)!='undefined')&&(typeof(v)!='function'))
{
v=com.serialization.json.JSON.serialize(v);
if(s)s+=',';
s+=com.serialization.json.JSON.serialize(prop)+':'+v;
}
}
return"{"+s+"}";
}
}
return'null';
case'number':
return isFinite(o)?String(o):'null';
case'string':
l=o.length;
s='"';
for(i=0;i<l;i+=1){
c=o.charAt(i);
if(c>=' '){
if(c=='\\'||c=='"')
{
s+='\\';
}
s+=c;
}
else
{
switch(c)
{
case'\b':
s+='\\b';
break;
case'\f':
s+='\\f';
break;
case'\n':
s+='\\n';
break;
case'\r':
s+='\\r';
break;
case'\t':
s+='\\t';
break;
default:
var code=c.charCodeAt();
s+='\\u00'+(Math.floor(code/16).toString(16))+((code%16).toString(16));
}
}
}
return s+'"';
case'boolean':
return String(o);
default:
return'null';
}
},
];},["deserialize","serialize"],["String","Error","Number","Array","Math"], "0.8.0", "0.8.4"
);
// class elements.axis.AxisLabel
joo.classLoader.prepare(
"package elements.axis",
"public class AxisLabel extends flash.text.TextField",5,function($$private){;return[function(){joo.classLoader.init(Math);},
"public var",{xAdj:0},
"public var",{yAdj:0},
"public var",{leftOverhang:0},
"public var",{rightOverhang:0},
"public var",{xVal:function(){return(NaN);}},
"public var",{yVal:function(){return(NaN);}},
"public function AxisLabel",function(){this.super$5();this.xVal=this.xVal();this.yVal=this.yVal();},
"public function rotate_and_align",function(rotation,align,parent)
{
rotation=rotation%360;
if(rotation<0)rotation+=360;
var myright=this.width*Math.cos(rotation*Math.PI/180);
var myleft=this.height*Math.cos((90-rotation)*Math.PI/180);
var mytop=this.height*Math.sin((90-rotation)*Math.PI/180);
var mybottom=this.width*Math.sin(rotation*Math.PI/180);
if(((rotation%90)==0)||(align=="center"))
{
this.xAdj=(myleft-myright)/2;
}
else
{
this.xAdj=(rotation<180)?myleft/2:-myright+(myleft/2);
}
if(rotation>90){
this.yAdj=-mytop;
}
if(rotation>180){
this.yAdj=-mytop-mybottom;
}
if(rotation>270){
this.yAdj=-mybottom;
}
this.rotation=rotation;
var titleRect=this.getBounds(parent);
this.leftOverhang=Math.abs(titleRect.x+this.xAdj);
this.rightOverhang=Math.abs(titleRect.x+titleRect.width+this.xAdj);
},
];},[],["flash.text.TextField","Math"], "0.8.0", "0.8.4"
);
// class elements.axis.RadarAxis
joo.classLoader.prepare("package elements.axis",
"public class RadarAxis extends flash.display.Sprite",6,function($$private){;return[function(){joo.classLoader.init(Math);},
"private var",{style:null},
"private var",{TO_RADIANS:function(){return(Math.PI/180);}},
"private var",{colour:NaN},
"private var",{grid_colour:NaN},
"private var",{labels:null},
"private var",{spoke_labels:null},
"function RadarAxis",function(json)
{this.super$6();this.TO_RADIANS$6=this.TO_RADIANS$6();
this.style$6={
stroke:2,
colour:'#784016',
'grid-colour':'#F5E1AA',
min:0,
max:null,
steps:1
};
if(json!=null)
object_helper.merge_2(json,this.style$6);
this.colour$6=string.Utils.get_colour(this.style$6.colour);
this.grid_colour$6=string.Utils.get_colour(this.style$6['grid-colour']);
this.labels$6=new elements.axis.RadarAxisLabels(json.labels);
this.addChild(this.labels$6);
this.spoke_labels$6=new elements.axis.RadarSpokeLabels(json['spoke-labels']);
this.addChild(this.spoke_labels$6);
},
"public function get_range",function(){
return new util.Range(this.style$6.min,this.style$6.max,this.style$6.steps,false);
},
"public function resize",function(sc)
{
this.x=0;
this.y=0;
this.graphics.clear();
this.spoke_labels$6.resize(sc);
var count=sc.get_angles();
this.draw_grid$6(sc,count);
this.draw_axis$6(sc,count);
this.labels$6.resize(sc);
},
"private function draw_axis",function(sc,count){
this.graphics.lineStyle(this.style$6.stroke,this.colour$6,1,true);
for(var i=0;i<count;i++){
var p=sc.get_get_x_from_pos_and_y_from_val(i,0);
this.graphics.moveTo(p.x,p.y);
var q=sc.get_get_x_from_pos_and_y_from_val(i,sc.get_max());
this.graphics.lineTo(q.x,q.y);
}
},
"private function draw_grid",function(sc,count){
this.graphics.lineStyle(1,this.grid_colour$6,1,true);
var max=sc.get_max()+0.00001;
var r_step=this.style$6.steps;
var p;
for(var r_pos=r_step;r_pos<=max;r_pos+=r_step){
p=sc.get_get_x_from_pos_and_y_from_val(0,r_pos);
this.graphics.moveTo(p.x,p.y);
for(var i=1;i<(count+1);i++){
p=sc.get_get_x_from_pos_and_y_from_val(i,r_pos);
this.graphics.lineTo(p.x,p.y);
}
}
},
"public function die",function(){
this.style$6=null;
this.labels$6.die();
this.spoke_labels$6.die();
this.graphics.clear();
while(this.numChildren>0)
this.removeChildAt(0);
},
];},[],["flash.display.Sprite","Math","object_helper","string.Utils","elements.axis.RadarAxisLabels","elements.axis.RadarSpokeLabels","util.Range"], "0.8.0", "0.8.4"
);
// class elements.axis.RadarAxisLabels
joo.classLoader.prepare("package elements.axis",
"public class RadarAxisLabels extends flash.display.Sprite",6,function($$private){var is=joo.is,as=joo.as;return[
"private var",{style:null},
"public var",{labels:null},
"public function RadarAxisLabels",function(json){this.super$6();
this.style$6={
colour:'#784016',
steps:1
};
if(json!=null)
object_helper.merge_2(json,this.style$6);
this.style$6.colour=string.Utils.get_colour(this.style$6.colour);
this.labels=new Array();
var values;
var ok=false;
if((is(this.style$6.labels,Array))&&(this.style$6.labels.length>0))
{
for(var $1 in this.style$6.labels){var s=this.style$6.labels[$1];
this.add(s,this.style$6);}
}
},
"public function add",function(label,style)
{
var label_style={
colour:style.colour,
text:'',
size:style.size,
visible:true
};
if(is(label,String))
label_style.text=as(label,String);
else{
object_helper.merge_2(label,label_style);
}
if(is(label_style.colour,String))
label_style.colour=string.Utils.get_colour(label_style.colour);
this.labels.push(label_style.text);
if(label_style.visible==null)
{
if(((this.labels.length-1)%style.steps)==0)
label_style.visible=true;
else
label_style.visible=false;
}
var l=this.make_label(label_style);
this.addChild(l);
},
"public function make_label",function(label_style){
var tf=new flash.text.TextField();
tf.x=0;
tf.y=0;
tf.text=label_style.text;
var fmt=new flash.text.TextFormat();
fmt.color=label_style.colour;
fmt.font="Verdana";
fmt.size=label_style.size;
fmt.align="right";
tf.setTextFormat(fmt);
tf.autoSize="left";
tf.visible=label_style.visible;
return tf;
},
"public function resize",function(sc){
var i;
var tf;
var center=sc.get_center_x();
for(i=0;i<this.numChildren;i++){
tf=as(this.getChildAt(i),flash.text.TextField);
tf.x=center-tf.width;
}
for(i=0;i<this.numChildren;i++){
tf=as(this.getChildAt(i),flash.text.TextField);
tf.y=(sc.get_y_from_val(i,false)-(tf.height/2));
}
},
"public function die",function(){
this.style$6=null;
this.labels=null;
this.graphics.clear();
while(this.numChildren>0)
this.removeChildAt(0);
},
];},[],["flash.display.Sprite","object_helper","string.Utils","Array","String","flash.text.TextField","flash.text.TextFormat"], "0.8.0", "0.8.4"
);
// class elements.axis.RadarSpokeLabels
joo.classLoader.prepare("package elements.axis",
"public class RadarSpokeLabels extends flash.display.Sprite",6,function($$private){var is=joo.is,as=joo.as;return[
"private var",{style:null},
"public var",{labels:null},
"public function RadarSpokeLabels",function(json){this.super$6();
this.style$6={
colour:'#784016'
};
if(json!=null)
object_helper.merge_2(json,this.style$6);
this.style$6.colour=string.Utils.get_colour(this.style$6.colour);
this.labels=new Array();
var values;
var ok=false;
if((is(this.style$6.labels,Array))&&(this.style$6.labels.length>0))
{
for(var $1 in this.style$6.labels){var s=this.style$6.labels[$1];
this.add(s,this.style$6);}
}
},
"public function add",function(label,style)
{
var label_style={
colour:style.colour,
text:'',
size:11
};
if(is(label,String))
label_style.text=as(label,String);
else{
object_helper.merge_2(label,label_style);
}
if(is(label_style.colour,String))
label_style.colour=string.Utils.get_colour(label_style.colour);
this.labels.push(label_style.text);
var l=this.make_label(label_style);
this.addChild(l);
},
"public function make_label",function(label_style){
var tf=new flash.text.TextField();
tf.x=0;
tf.y=0;
var tmp=label_style.text.split('<br>');
var text=tmp.join('\n');
tf.text=text;
var fmt=new flash.text.TextFormat();
fmt.color=label_style.colour;
fmt.font="Verdana";
fmt.size=label_style.size;
fmt.align="right";
tf.setTextFormat(fmt);
tf.autoSize="left";
tf.visible=true;
return tf;
},
"public function resize",function(sc){
var tf;
var i=0;
var outside;
do
{
outside=false;
this.resize_2$6(sc);
for(i=0;i<this.numChildren;i++)
{
tf=as(this.getChildAt(i),flash.text.TextField);
if((tf.x<sc.left)||
(tf.y<sc.top)||
(tf.y+tf.height>sc.bottom)||
(tf.x+tf.width>sc.right)
)
outside=true;
}
sc.reduce_radius();
}
while(outside&&sc.get_radius()>10);
},
"private function resize_2",function(sc){
var i;
var tf;
var mid_x=sc.get_center_x();
for(i=0;i<this.numChildren;i++){
tf=as(this.getChildAt(i),flash.text.TextField);
var p=sc.get_get_x_from_pos_and_y_from_val(i,sc.get_max());
if(p.x>mid_x)
tf.x=p.x;
else
tf.x=p.x-tf.width;
if(i==0){
tf.y=p.y-tf.height;
tf.x=p.x;
}
else
tf.y=p.y;
}
},
"public function die",function(){
this.style$6=null;
this.labels=null;
this.graphics.clear();
while(this.numChildren>0)
this.removeChildAt(0);
},
];},[],["flash.display.Sprite","object_helper","string.Utils","Array","String","flash.text.TextField","flash.text.TextFormat"], "0.8.0", "0.8.4"
);
// class elements.axis.XAxis
joo.classLoader.prepare("package elements.axis",
"public class XAxis extends flash.display.Sprite",6,function($$private){var is=joo.is;return[function(){joo.classLoader.init(Math);},
"private var",{steps:NaN},
"private var",{alt_axis_colour:NaN},
"private var",{alt_axis_step:NaN},
"private var",{three_d:false},
"private var",{three_d_height:NaN},
"private var",{stroke:NaN},
"private var",{tick_height:NaN},
"private var",{colour:NaN},
"public var",{offset:false},
"private var",{grid_colour:NaN},
"private var",{grid_visible:false},
"private var",{user_ticks:false},
"private var",{user_labels:null},
"public var",{labels:null},
"private var",{style:null},
"function XAxis",function(json,min,max)
{this.super$6();
this.style$6={
stroke:2,
'tick-height':3,
colour:'#784016',
offset:true,
'grid-colour':'#F5E1AA',
'grid-visible':true,
'3d':0,
steps:1,
min:0,
max:null
};
if(json!=null)
object_helper.merge_2(json.x_axis,this.style$6);
this.calcSteps$6();
this.stroke$6=this.style$6.stroke;
this.tick_height$6=this.style$6['tick-height'];
this.colour$6=this.style$6.colour;
this.offset=this.style$6.offset;
this.grid_visible$6=this.style$6['grid-visible'];
this.colour$6=string.Utils.get_colour(this.style$6.colour);
this.grid_colour$6=string.Utils.get_colour(this.style$6['grid-colour']);
if(this.style$6['3d']>0)
{
this.three_d$6=true;
this.three_d_height$6=$$int(this.style$6['3d']);
}
else
this.three_d$6=false;
if(json)
{
if(json.x_label_style!=undefined){
if(json.x_label_style.alt_axis_step!=undefined)
this.alt_axis_step$6=json.x_label_style.alt_axis_step;
if(json.x_label_style.alt_axis_colour!=undefined)
this.alt_axis_colour$6=string.Utils.get_colour(json.x_label_style.alt_axis_colour);
}
}
this.labels=new elements.axis.XAxisLabels(json);
this.addChild(this.labels);
if(!this.range_set())
{
if(this.labels.need_labels){
this.set_range(min,max);
}
else
{
if(this.labels.count()>max){
this.set_range(0,this.labels.count());
}else{
this.set_range(min,max);
}
}
}
else
{
this.labels.auto_label(this.get_range(),this.get_steps());
}
this.make_user_ticks$6();
},
"private function make_user_ticks",function(){
if((this.style$6.labels!=null)&&
(this.style$6.labels.labels!=null)&&
(is(this.style$6.labels.labels,Array))&&
(this.style$6.labels.labels.length>0))
{
this.user_labels$6=new Array();
for(var $1 in this.style$6.labels.labels)
{var lbl=this.style$6.labels.labels[$1];
if(!(is(lbl,String))){
if(lbl.x!=null)
{
var tmpObj={x:lbl.x};
if(lbl["grid-colour"])
{
tmpObj["grid-colour"]=string.Utils.get_colour(lbl["grid-colour"]);
}
else
{
tmpObj["grid-colour"]=this.grid_colour$6;
}
this.user_ticks$6=true;
this.user_labels$6.push(tmpObj);
}
}
}
}
},
"private function calcSteps",function(){
if(this.style$6.max==null){
this.steps$6=1;
}
else{
var xRange=this.style$6.max-this.style$6.min;
var rev=(this.style$6.min>=this.style$6.max);
this.steps$6=((this.style$6.steps!=null)&&
(this.style$6.steps!=0))?this.style$6.steps:1;
if((Math.abs(xRange)/this.steps$6)>250)this.steps$6=xRange/250;
this.steps$6=rev?-Math.abs(this.steps$6):Math.abs(this.steps$6);
}
},
"public function range_set",function(){
return this.style$6.max!=null;
},
"public function set_range",function(min,max)
{
this.style$6.min=min;
this.style$6.max=max;
this.calcSteps$6();
this.labels.auto_label(this.get_range(),this.get_steps());
},
"public function get_range",function(){
return new util.Range(this.style$6.min,this.style$6.max,this.steps$6,this.offset);
},
"public function get_steps",function(){
return this.steps$6;
},
"public function resize",function(sc,yPos)
{
this.graphics.clear();
if(this.user_ticks$6)
{
for(var $1 in this.user_labels$6)
{var lbl=this.user_labels$6[$1];
this.graphics.beginFill(lbl["grid-colour"],1);
var xVal=sc.get_x_from_val(lbl.x);
this.graphics.drawRect(xVal,sc.top,1,sc.height);
this.graphics.endFill();
}
}
else if(this.grid_visible$6)
{
var rev=(this.style$6.min>=this.style$6.max);
var tickMax=this.style$6.max;
for(var i=this.style$6.min;rev?i>=tickMax:i<=tickMax;i+=this.steps$6)
{
if((this.alt_axis_step$6>1)&&(i%this.alt_axis_step$6==0))
this.graphics.beginFill(this.alt_axis_colour$6,1);
else
this.graphics.beginFill(this.grid_colour$6,1);
xVal=sc.get_x_from_val(i);
this.graphics.drawRect(xVal,sc.top,1,sc.height);
this.graphics.endFill();
}
}
if(this.three_d$6)
this.three_d_axis(sc);
else
this.two_d_axis(sc);
this.labels.resize(sc,yPos);
},
"public function three_d_axis",function(sc)
{
var h=this.three_d_height$6;
var offset=12;
var x_axis_height=h+offset;
var item_width=sc.width/this.style$6.max;
this.graphics.lineStyle(0,0,0);
var w=1;
if(this.user_ticks$6)
{
for(var $1 in this.user_labels$6)
{var lbl=this.user_labels$6[$1];
var xVal=sc.get_x_from_val(lbl.x);
this.graphics.beginFill(this.colour$6,1);
this.graphics.drawRect(xVal,sc.bottom+x_axis_height,w,this.tick_height$6);
this.graphics.endFill();
}
}
else
{
for(var i=0;i<this.style$6.max;i+=this.steps$6)
{
var pos=sc.get_x_tick_pos(i);
this.graphics.beginFill(this.colour$6,1);
this.graphics.drawRect(pos,sc.bottom+x_axis_height,w,this.tick_height$6);
this.graphics.endFill();
}
}
var lighter=charts.series.bars.Bar3D.Lighten(this.colour$6);
var colors=[this.colour$6,lighter];
var alphas=[100,100];
var ratios=[0,255];
var matrix=new flash.geom.Matrix();
matrix.createGradientBox(sc.width_(),offset,(270/180)*Math.PI,sc.left-offset,sc.bottom);
this.graphics.beginGradientFill('linear',colors,alphas,ratios,matrix,'pad');
this.graphics.moveTo(sc.left,sc.bottom);
this.graphics.lineTo(sc.right,sc.bottom);
this.graphics.lineTo(sc.right-offset,sc.bottom+offset);
this.graphics.lineTo(sc.left-offset,sc.bottom+offset);
this.graphics.endFill();
colors=[this.colour$6,lighter];
alphas=[100,100];
ratios=[0,255];
matrix.createGradientBox(sc.width_(),h,(270/180)*Math.PI,sc.left-offset,sc.bottom+offset);
this.graphics.beginGradientFill("linear",colors,alphas,ratios,matrix);
this.graphics.moveTo(sc.left-offset,sc.bottom+offset);
this.graphics.lineTo(sc.right-offset,sc.bottom+offset);
this.graphics.lineTo(sc.right-offset,sc.bottom+offset+h);
this.graphics.lineTo(sc.left-offset,sc.bottom+offset+h);
this.graphics.endFill();
colors=[this.colour$6,lighter];
alphas=[100,100];
ratios=[0,255];
matrix.createGradientBox(sc.width_(),h,(225/180)*Math.PI,sc.left-offset,sc.bottom+offset);
this.graphics.beginGradientFill("linear",colors,alphas,ratios,matrix);
this.graphics.moveTo(sc.right,sc.bottom);
this.graphics.lineTo(sc.right,sc.bottom+h);
this.graphics.lineTo(sc.right-offset,sc.bottom+offset+h);
this.graphics.lineTo(sc.right-offset,sc.bottom+offset);
this.graphics.endFill();
},
"public function two_d_axis",function(sc)
{
var item_width=sc.width/this.style$6.max;
var left=sc.left+(item_width/2);
this.graphics.lineStyle(0,0,0);
this.graphics.beginFill(this.colour$6);
this.graphics.drawRect(sc.left,sc.bottom,sc.width,this.stroke$6);
this.graphics.endFill();
if(this.user_ticks$6)
{
for(var $1 in this.user_labels$6)
{var lbl=this.user_labels$6[$1];
var xVal=sc.get_x_from_val(lbl.x);
this.graphics.beginFill(this.colour$6,1);
this.graphics.drawRect(xVal-(this.stroke$6/2),sc.bottom+this.stroke$6,this.stroke$6,this.tick_height$6);
this.graphics.endFill();
}
}
else
{
for(var i=this.style$6.min;i<=this.style$6.max;i+=this.steps$6)
{
xVal=sc.get_x_from_val(i);
this.graphics.beginFill(this.colour$6,1);
this.graphics.drawRect(xVal-(this.stroke$6/2),sc.bottom+this.stroke$6,this.stroke$6,this.tick_height$6);
this.graphics.endFill();
}
}
},
"public function get_height",function(){
if(this.three_d$6)
{
return this.three_d_height$6+12+this.tick_height$6+this.labels.get_height();
}
else
return this.stroke$6+this.tick_height$6+this.labels.get_height();
},
"public function first_label_width",function()
{
return this.labels.first_label_width();
},
"public function last_label_width",function()
{
return this.labels.last_label_width();
},
"public function die",function(){
this.style$6=null;
this.graphics.clear();
while(this.numChildren>0)
this.removeChildAt(0);
if(this.labels!=null)
this.labels.die();
this.labels=null;
},
];},[],["flash.display.Sprite","object_helper","string.Utils","int","elements.axis.XAxisLabels","Array","String","Math","util.Range","charts.series.bars.Bar3D","flash.geom.Matrix"], "0.8.0", "0.8.4"
);
// class elements.axis.XAxisLabels
joo.classLoader.prepare("package elements.axis",
"public class XAxisLabels extends flash.display.Sprite",6,function($$private){var is=joo.is,as=joo.as;return[
"public var",{need_labels:false},
"public var",{axis_labels:null},
"private var",{style:null},
"private var",{userSpecifiedVisible:null},
{Embed:{systemFont:'Arial',fontName:'spArial',mimeType:'application/x-font'}},
"public static var",{ArialFont__:null},
"function XAxisLabels",function(json){this.super$6();
this.need_labels=true;
this.style$6={
rotate:0,
visible:null,
labels:null,
text:'#val#',
steps:null,
size:10,
align:'auto',
colour:'#000000',
"visible-steps":null
};
this.axis_labels=new Array();
if((json.x_axis!=null)&&(json.x_axis.labels!=null))
object_helper.merge_2(json.x_axis.labels,this.style$6);
this.userSpecifiedVisible$6=this.style$6.visible;
if(this.style$6.visible==null)this.style$6.visible=true;
if(is(this.style$6.rotate,String))
{
if(this.style$6.rotate=="vertical")
{
this.style$6.rotate=270;
}
else if(this.style$6.rotate=="diagonal")
{
this.style$6.rotate=-45;
}
}
this.style$6.colour=string.Utils.get_colour(this.style$6.colour);
if((is(this.style$6.labels,Array))&&(this.style$6.labels.length>0))
{
this.need_labels=false;
if(this.style$6.steps==null)
this.style$6.steps=1;
var x=0;
var lblCount=0;
var visibleSteps=(this.style$6["visible-steps"]==null)?this.style$6.steps:this.style$6["visible-steps"];
for(var $1 in this.style$6.labels)
{var s=this.style$6.labels[$1];
var tmpStyle={};
object_helper.merge_2(this.style$6,tmpStyle);
tmpStyle.visible=((lblCount%visibleSteps)==0);
tmpStyle.x=x;
this.add(s,tmpStyle);
x++;
lblCount++;
}
}
},
"public function auto_label",function(range,steps){
if(this.need_labels){
var rev=(range.min>=range.max);
var lblSteps=1;
if(this.style$6.steps!=null)lblSteps=this.style$6.steps;
if(Math.abs(range.count()/lblSteps)>250)lblSteps=range.count()/250;
lblSteps=rev?-Math.abs(lblSteps):Math.abs(lblSteps);
var visibleSteps=(this.style$6["visible-steps"]==null)?steps:this.style$6["visible-steps"];
var tempStyle={};
object_helper.merge_2(this.style$6,tempStyle);
var lblCount=0;
for(var i=range.min;rev?i>=range.max:i<=range.max;i+=lblSteps){
tempStyle.x=i;
if(this.userSpecifiedVisible$6==null)
{
tempStyle.visible=((lblCount%visibleSteps)==0);
lblCount++;
}
else
{
tempStyle.visible=this.userSpecifiedVisible$6;
}
this.add(null,tempStyle);
}
}
},
"public function add",function(label,style)
{
var label_style={
colour:style.colour,
text:style.text,
rotate:style.rotate,
size:style.size,
align:style.align,
visible:style.visible,
x:style.x
};
if(is(label,String))
label_style.text=as(label,String);
else
object_helper.merge_2(label,label_style);
if(label_style.x!=null){
label_style.text=this.replace_magic_values$6(label_style.text,label_style.x);
}
var lines=label_style.text.split('<br>');
label_style.text=lines.join('\n');
this.axis_labels[label_style.x]=label_style.text;
if(label_style.visible){
if(is(label_style.colour,String))
label_style.colour=string.Utils.get_colour(label_style.colour);
var l=this.make_label(label_style);
this.addChild(l);
}
},
"public function get",function(i)
{
if(i<this.axis_labels.length)
return this.axis_labels[i];
else
return'';
},
"public function make_label",function(label_style){
var title=new elements.axis.AxisLabel();
title.x=0;
title.y=0;
title.text=label_style.text;
var fmt=new flash.text.TextFormat();
fmt.color=label_style.colour;
if(label_style.rotate!=0)
{
fmt.font="spArial";
title.embedFonts=true;
}
else
{
fmt.font="Verdana";
}
fmt.size=label_style.size;
fmt.align="left";
title.setTextFormat(fmt);
title.autoSize="left";
title.rotate_and_align(label_style.rotate,label_style.align,this);
title.visible=label_style.visible;
if(label_style.x!=null)
{
title.xVal=label_style.x;
}
return title;
},
"public function count",function()
{
return this.axis_labels.length-1;
},
"public function get_height",function()
{
var height=0;
for(var pos=0;pos<this.numChildren;pos++)
{
var child=this.getChildAt(pos);
height=Math.max(height,child.height);
}
return height;
},
"public function resize",function(sc,yPos)
{
this.graphics.clear();
var i=0;
for(var pos=0;pos<this.numChildren;pos++)
{
var child=as(this.getChildAt(pos),elements.axis.AxisLabel);
if(isNaN(child.xVal))
{
child.x=sc.get_x_tick_pos(pos)+child.xAdj;
}
else
{
child.x=sc.get_x_from_val(child.xVal)+child.xAdj;
}
child.y=yPos+child.yAdj;
}
},
"public function last_label_width",function()
{
if(this.numChildren>0)
return(this.getChildAt(this.numChildren-1)).rightOverhang*2;
else
return 0;
},
"public function first_label_width",function()
{
if(this.numChildren>0)
return(this.getChildAt(0)).leftOverhang*2;
else
return 0;
},
"public function die",function(){
this.axis_labels=null;
this.style$6=null;
this.graphics.clear();
while(this.numChildren>0)
this.removeChildAt(0);
},
"private function replace_magic_values",function(labelText,xVal){
labelText=labelText.replace('#val#',NumberUtils.formatNumber(xVal));
labelText=string.DateUtils.replace_magic_values(labelText,xVal);
return labelText;
},
];},[],["flash.display.Sprite","Array","object_helper","String","string.Utils","Math","elements.axis.AxisLabel","flash.text.TextFormat","NumberUtils","string.DateUtils"], "0.8.0", "0.8.4"
);
// class elements.axis.XLabelStyle
joo.classLoader.prepare("package elements.axis",
"public class XLabelStyle",1,function($$private){;return[

"public var",{size:10},
"public var",{colour:0x000000},
"public var",{vertical:false},
"public var",{diag:false},
"public var",{step:1},
"public var",{show_labels:false},
"public function XLabelStyle",function(json)
{
if(!json)
return;
if(json.x_label_style==undefined)
return;
if(json.x_label_style.visible==undefined||json.x_label_style.visible)
{
this.show_labels=true;
if(json.x_label_style.size!=undefined)
this.size=json.x_label_style.size;
if(json.x_label_style.colour!=undefined)
this.colour=string.Utils.get_colour(json.x_label_style.colour);
if(json.x_label_style.rotation!=undefined)
{
this.vertical=(json.x_label_style.rotation==1);
this.diag=(json.x_label_style.rotation==2);
}
if(json.x_label_style.step!=undefined)
this.step=json.x_label_style.step;
}
else
this.show_labels=true;
},
];},[],["string.Utils"], "0.8.0", "0.8.4"
);
// class elements.axis.YAxisBase
joo.classLoader.prepare("package elements.axis",
"public class YAxisBase extends flash.display.Sprite",6,function($$private){var is=joo.is;return[
"protected var",{stroke:NaN},
"protected var",{tick_length:NaN},
"protected var",{colour:NaN},
"protected var",{grid_colour:NaN},
"public var",{style:null},
"protected var",{labels:null},
"private var",{user_labels:null},
"private var",{user_ticks:false},
"function YAxisBase",function(){this.super$6();},
"public function init",function(json){},
"protected function _init",function(json,name,style){
this.style=style;
if(json[name])
object_helper.merge_2(json[name],this.style);
this.colour=string.Utils.get_colour(style.colour);
this.grid_colour=string.Utils.get_colour(style['grid-colour']);
this.stroke=style.stroke;
this.tick_length=style['tick-length'];
tr.aces('YAxisBase auto',this.auto_range(50001));
tr.aces('YAxisBase min, max',this.style.min,this.style.max);
if(this.style.max==null){
this.style.max=this.labels.y_max;
}
var min=Math.min(this.style.min,this.style.max);
var max=Math.max(this.style.min,this.style.max);
this.style.steps=this.get_steps$6(min,max,this.stage.stageHeight);
if(this.labels.i_need_labels)
this.labels.make_labels(min,max,this.style.steps);
if((this.style.labels!=null)&&
(this.style.labels.labels!=null)&&
(is(this.style.labels.labels,Array))&&
(this.style.labels.labels.length>0))
{
this.user_labels$6=new Array();
for(var $1 in this.style.labels.labels)
{var lbl=this.style.labels.labels[$1];
if(!(is(lbl,String))){
if(lbl.y!=null)
{
var tmpObj={y:lbl.y};
if(lbl["grid-colour"])
{
tmpObj["grid-colour"]=string.Utils.get_colour(lbl["grid-colour"]);
}
else
{
tmpObj["grid-colour"]=this.grid_colour;
}
this.user_ticks$6=true;
this.user_labels$6.push(tmpObj);
}
}
}
}
},
"public function auto_range",function(max){
var maxValue=Math.max(max)*1.07;
var l=Math.round(Math.log(maxValue)/Math.log(10));
var p=Math.pow(10,l)/2;
maxValue=Math.round((maxValue*1.1)/p)*p;
return maxValue;
},
"public function get_style",function(){return null;},
"public function set_y_max",function(m){
this.style.max=m;
},
"public function get_range",function(){
return new util.Range(this.style.min,this.style.max,this.style.steps,this.style.offset);
},
"public function get_width",function(){
return this.stroke+this.tick_length+this.labels.width;
},
"public function die",function(){
this.style=null;
if(this.labels!=null)this.labels.die();
this.labels=null;
this.graphics.clear();
while(this.numChildren>0)
this.removeChildAt(0);
},
"private function get_steps",function(min,max,height){
if(this.style.steps==0)
this.style.steps=1;
if(this.style.steps<0)
this.style.steps*=-1;
var s=(max-min)/this.style.steps;
if(s>(height/2)){
return(max-min)/5;
}
return this.style.steps;
},
"public function resize",function(label_pos,sc){},
"protected function resize_helper",function(label_pos,sc,right){
var i2=0;
var i;
var y;
var lbl;
var min=Math.min(this.style.min,this.style.max);
var max=Math.max(this.style.min,this.style.max);
if(!right)
this.labels.resize(label_pos,sc);
else
this.labels.resize(sc.right+this.stroke+this.tick_length,sc);
if(!this.style.visible)
return;
this.graphics.clear();
this.graphics.lineStyle(0,0,0);
if(this.style['grid-visible'])
this.draw_grid_lines$6(this.style.steps,min,max,right,sc);
var pos;
if(!right)
pos=sc.left-this.stroke;
else
pos=sc.right;
this.graphics.beginFill(this.colour,1);
this.graphics.drawRect(
$$int(pos),
sc.top,
this.stroke,
sc.height);
this.graphics.endFill();
var width;
if(this.user_ticks$6)
{
for(var $1 in this.user_labels$6)
{lbl=this.user_labels$6[$1];
y=sc.get_y_from_val(lbl.y,right);
if(!right)
tick_pos=sc.left-this.stroke-this.tick_length;
else
tick_pos=sc.right+this.stroke;
this.graphics.beginFill(this.colour,1);
this.graphics.drawRect(tick_pos,y-(this.stroke/2),this.tick_length,this.stroke);
this.graphics.endFill();
}
}
else
{
for(i=min;i<=max;i+=this.style.steps){
y=sc.get_y_from_val(i,right);
var tick_pos;
if(!right)
tick_pos=sc.left-this.stroke-this.tick_length;
else
tick_pos=sc.right+this.stroke;
this.graphics.beginFill(this.colour,1);
this.graphics.drawRect(tick_pos,y-(this.stroke/2),this.tick_length,this.stroke);
this.graphics.endFill();
}
}
},
"private function draw_grid_lines",function(steps,min,max,right,sc){
var y;
var lbl;
if(this.user_ticks$6)
{
for(var $1 in this.user_labels$6)
{lbl=this.user_labels$6[$1];
y=sc.get_y_from_val(lbl.y,right);
this.graphics.beginFill(lbl["grid-colour"],1);
this.graphics.drawRect(sc.left,y,sc.width,1);
this.graphics.endFill();
}
}
else
{
max+=0.000004;
for(var i=min;i<=max;i+=steps){
y=sc.get_y_from_val(i,right);
this.graphics.beginFill(this.grid_colour,1);
this.graphics.drawRect(
$$int(sc.left),
$$int(y),
sc.width,
1);
this.graphics.endFill();
}
}
},
];},[],["flash.display.Sprite","object_helper","string.Utils","tr","Math","Array","String","util.Range","int"], "0.8.0", "0.8.4"
);
// class elements.axis.YAxisLabelsBase
joo.classLoader.prepare("package elements.axis",
"public class YAxisLabelsBase extends flash.display.Sprite",6,function($$private){var is=joo.is,as=joo.as;return[function(){joo.classLoader.init(flash.text.AntiAliasType);},
"private var",{steps:NaN},
"private var",{right:false},
"protected var",{style:null},
"public var",{i_need_labels:false},
"protected var",{lblText:null},
"public var",{y_max:NaN},
"public function YAxisLabelsBase",function(json,axis_name){this.super$6();
var i;
var s;
var values;
var steps;
this.y_max=10;
if(json[axis_name])
{
if(is(json[axis_name].labels,Array))
{
values=[];
i=(json[axis_name]&&json[axis_name].min)?json[axis_name].min:0;
for(var $1 in json[axis_name].labels)
{s=json[axis_name].labels[$1];
values.push({val:s,pos:i});
i++;
}
this.y_max=(json[axis_name]&&json[axis_name].max)?json[axis_name].max:values.length-1;
this.i_need_labels=false;
}
}
if(json[axis_name])
{
if(is(json[axis_name].labels,Object))
{
if(is(json[axis_name].labels.text,String))
this.lblText=json[axis_name].labels.text;
var visibleSteps=1;
if(is(json[axis_name].steps,Number))
visibleSteps=json[axis_name].steps;
if(is(json[axis_name].labels.steps,Number))
visibleSteps=json[axis_name].labels.steps;
if(is(json[axis_name].labels.labels,Array))
{
values=[];
var label_pos=(json[axis_name]&&json[axis_name].min)?json[axis_name].min:0;
for(var $2 in json[axis_name].labels.labels)
{var obj=json[axis_name].labels.labels[$2];
if(is(obj,Number))
{
values.push({val:this.lblText,pos:obj});
}
else if(is(obj,String))
{
values.push({
val:obj,
pos:label_pos,
visible:((label_pos%visibleSteps)==0)
});
}
else if(is(obj.y,Number))
{
s=(is(obj.text,String))?obj.text:this.lblText;
var style={val:s,pos:obj.y};
if(obj.colour!=null)
style.colour=obj.colour;
if(obj.size!=null)
style.size=obj.size;
if(obj.rotate!=null)
style.rotate=obj.rotate;
values.push(style);
}
label_pos++;
}
this.i_need_labels=false;
}
}
}
this.steps$6=steps;
var lblStyle=new elements.axis.YLabelStyle(json,this.name);
this.style=lblStyle.style;
if(!json[axis_name]&&axis_name!='y_axis')
this.style.show_labels=false;
if(json[axis_name]&&json[axis_name].rotate){
this.style.rotate=json[axis_name].rotate;
}
if((json[axis_name]!=null)&&
(json[axis_name].labels!=null)){
object_helper.merge_2(json[axis_name].labels,this.style);
}
this.add_labels$6(values);
},
"private function add_labels",function(values){
if(!this.style.show_labels)
return;
var pos=0;
for(var $1 in values)
{var v=values[$1];
var lblStyle={};
object_helper.merge_2(this.style,lblStyle);
object_helper.merge_2(v,lblStyle);
if(lblStyle.visible)
{
var tmp=this.make_label$6(lblStyle);
tmp.y_val=v.pos;
this.addChild(tmp);
pos++;
}
}
},
"public function make_labels",function(min,max,steps){
tr.aces('make_labels',this.i_need_labels,min,max,false,steps,this.lblText);
tr.aces(this.style.show_labels);
if(!this.i_need_labels)
return;
this.i_need_labels=false;
this.make_labels_(min,max,false,steps,this.lblText);
},
"protected function make_labels_",function(min,max,right,steps,lblText){
var values=[];
var min_=Math.min(min,max);
var max_=Math.max(min,max);
max_+=0.000004;
var eek=0;
for(var i=min_;i<=max_;i+=steps){
values.push({val:lblText,pos:i});
if(eek++>250)break;
}
this.add_labels$6(values);
},
"private function make_label",function(lblStyle)
{
lblStyle.colour=string.Utils.get_colour(lblStyle.colour);
var tf=new elements.axis.YTextField();
tf.text=this.replace_magic_values$6(lblStyle.val,lblStyle.pos);
var fmt=new flash.text.TextFormat();
fmt.color=lblStyle.colour;
fmt.font=lblStyle.rotate=="vertical"?"spArial":"Verdana";
fmt.size=lblStyle.size;
fmt.align="right";
tf.setTextFormat(fmt);
tf.autoSize="right";
if(lblStyle.rotate=="vertical")
{
tf.rotation=270;
tf.embedFonts=true;
tf.antiAliasType=flash.text.AntiAliasType.ADVANCED;
}
return tf;
},
"public function resize",function(left,sc)
{
},
"public function get_width",function(){
var max=0;
for(var i=0;i<this.numChildren;i++)
{
var tf=as(this.getChildAt(i),elements.axis.YTextField);
max=Math.max(max,tf.width);
}
return max;
},
"public function die",function(){
while(this.numChildren>0)
this.removeChildAt(0);
},
"private function replace_magic_values",function(labelText,yVal){
labelText=labelText.replace('#val#',NumberUtils.formatNumber(yVal));
return labelText;
},
];},[],["flash.display.Sprite","Array","Object","String","Number","elements.axis.YLabelStyle","object_helper","tr","Math","string.Utils","elements.axis.YTextField","flash.text.TextFormat","flash.text.AntiAliasType","NumberUtils"], "0.8.0", "0.8.4"
);
// class elements.axis.YAxisLabelsLeft
joo.classLoader.prepare("package elements.axis",
"public class YAxisLabelsLeft extends elements.axis.YAxisLabelsBase",7,function($$private){var as=joo.as;return[
"public function YAxisLabelsLeft",function(json){
this.lblText="#val#";
this.i_need_labels=true;
this.super$7(json,'y_axis');
},
"public override function resize",function(left,sc){
var maxWidth=this.get_width();
var i;
var tf;
for(i=0;i<this.numChildren;i++){
tf=as(this.getChildAt(i),elements.axis.YTextField);
tf.x=left-tf.width+maxWidth;
}
for(i=0;i<this.numChildren;i++){
tf=as(this.getChildAt(i),elements.axis.YTextField);
if(tf.rotation!=0){
tf.y=sc.get_y_from_val(tf.y_val,false)+(tf.height/2);
}
else{
tf.y=sc.get_y_from_val(tf.y_val,false)-(tf.height/2);
}
if(tf.y<0&&sc.top==0)
tf.y=(tf.rotation!=0)?tf.height:tf.textHeight-tf.height;
}
},
];},[],["elements.axis.YAxisLabelsBase","elements.axis.YTextField"], "0.8.0", "0.8.4"
);
// class elements.axis.YAxisLabelsRight
joo.classLoader.prepare("package elements.axis",
"public class YAxisLabelsRight extends elements.axis.YAxisLabelsBase",7,function($$private){var as=joo.as;return[
"public function YAxisLabelsRight",function(json){
this.lblText="#val#";
this.i_need_labels=true;
this.super$7(json,'y_axis_right');
},
"public override function resize",function(left,box){
var maxWidth=this.get_width();
var i;
var tf;
for(i=0;i<this.numChildren;i++){
tf=as(this.getChildAt(i),elements.axis.YTextField);
tf.x=left;
}
for(i=0;i<this.numChildren;i++){
tf=as(this.getChildAt(i),elements.axis.YTextField);
if(tf.rotation!=0){
tf.y=box.get_y_from_val(tf.y_val,true)+(tf.height/2);
}
else{
tf.y=box.get_y_from_val(tf.y_val,true)-(tf.height/2);
}
if(tf.y<0&&box.top==0)
tf.y=(tf.rotation!=0)?tf.height:tf.textHeight-tf.height;
}
},
];},[],["elements.axis.YAxisLabelsBase","elements.axis.YTextField"], "0.8.0", "0.8.4"
);
// class elements.axis.YAxisLeft
joo.classLoader.prepare("package elements.axis",
"public class YAxisLeft extends elements.axis.YAxisBase",7,function($$private){;return[
"function YAxisLeft",function(){this.super$7();},
"public override function init",function(json){
this.labels=new elements.axis.YAxisLabelsLeft(json);
this.addChild(this.labels);
var style={
stroke:2,
'tick-length':3,
colour:'#784016',
offset:false,
'grid-colour':'#F5E1AA',
'grid-visible':true,
'3d':0,
steps:1,
visible:true,
min:0,
max:null
};
this._init(json,'y_axis',style);
},
"public override function resize",function(label_pos,sc){
this.resize_helper(label_pos,sc,false);
},
];},[],["elements.axis.YAxisBase","elements.axis.YAxisLabelsLeft"], "0.8.0", "0.8.4"
);
// class elements.axis.YAxisRight
joo.classLoader.prepare("package elements.axis",
"public class YAxisRight extends elements.axis.YAxisBase",7,function($$private){;return[
"function YAxisRight",function(){this.super$7();},
"public override function init",function(json){
this.labels=new elements.axis.YAxisLabelsRight(json);
this.addChild(this.labels);
var style={
stroke:2,
'tick-length':3,
colour:'#784016',
offset:false,
'grid-colour':'#F5E1AA',
'grid-visible':false,
'3d':0,
steps:1,
visible:false,
min:0,
max:10
};
if(json.y_axis_right)
style.visible=true;
this._init(json,'y_axis_right',style);
},
"public override function resize",function(label_pos,sc){
this.resize_helper(label_pos,sc,true);
},
];},[],["elements.axis.YAxisBase","elements.axis.YAxisLabelsRight"], "0.8.0", "0.8.4"
);
// class elements.axis.YLabelStyle
joo.classLoader.prepare("package elements.axis",
"public class YLabelStyle",1,function($$private){;return[

"public var",{style:null},
"public function YLabelStyle",function(json,name)
{
this.style={size:10,
colour:0x000000,
show_labels:true,
visible:true
};
var comma;
var none;
var tmp;
if(json[name+'_label_style']==undefined)
return;
comma=json[name+'_label_style'].lastIndexOf(',');
if(comma<0)
{
none=json[name+'_label_style'].lastIndexOf('none',0);
if(none>-1)
{
this.style.show_labels=false;
}
}
else
{
tmp=json[name+'_label_style'].split(',');
if(tmp.length>0)
this.style.size=tmp[0];
if(tmp.length>1)
this.style.colour=string.Utils.get_colour(tmp[1]);
}
},
];},[],["string.Utils"], "0.8.0", "0.8.4"
);
// class elements.axis.YTextField
joo.classLoader.prepare("package elements.axis",
"public class YTextField extends flash.text.TextField",5,function($$private){;return[
"public var",{y_val:NaN},
"public function YTextField",function(){
this.super$5();
this.y_val=0;
},
];},[],["flash.text.TextField"], "0.8.0", "0.8.4"
);
// class elements.Background
joo.classLoader.prepare("package elements",
"public class Background extends flash.display.Sprite",6,function($$private){;return[
"private var",{colour:NaN},
"private var",{img_x:NaN},
"private var",{img_y:NaN},
"public function Background",function(json)
{this.super$6();
if(json.bg_colour!=undefined)
this.colour$6=string.Utils.get_colour(json.bg_colour);
else
this.colour$6=0xf8f8d8;
if(json.bg_image!=undefined)
this.load_img$6(json.bg_image);
},
"private function load_img",function(json){
if(json.bg_image_x!=undefined)
this.img_x$6=json.bg_image_x;
if(json.bg_image_y!=undefined)
this.img_y$6=json.bg_image_y;
},
"public function resize",function(){
this.graphics.beginFill(this.colour$6);
this.graphics.drawRect(0,0,this.stage.stageWidth,this.stage.stageHeight);
},
"public function die",function(){
this.graphics.clear();
},
];},[],["flash.display.Sprite","string.Utils"], "0.8.0", "0.8.4"
);
// class elements.labels.BaseLabel
joo.classLoader.prepare(
"package elements.labels",
"public class BaseLabel extends flash.display.Sprite",6,function($$private){;return[
"public var",{text:null},
"protected var",{css:null},
"public var",{style:null},
"protected var",{_height:NaN},
"public function BaseLabel",function(){this.super$6();},
"protected function build",function(text){
var title=new flash.text.TextField();
title.x=0;
title.y=0;
this.text=text;
title.htmlText=this.text;
var fmt=new flash.text.TextFormat();
fmt.color=this.css.color;
fmt.font=this.css.font_family?this.css.font_family:'Verdana';
fmt.bold=this.css.font_weight=='bold'?true:false;
fmt.size=this.css.font_size;
fmt.align="center";
title.setTextFormat(fmt);
title.autoSize="left";
title.y=this.css.padding_top+this.css.margin_top;
title.x=this.css.padding_left+this.css.margin_left;
if(this.css.background_colour_set)
{
this.graphics.beginFill(this.css.background_colour,1);
this.graphics.drawRect(0,0,this.css.padding_left+title.width+this.css.padding_right,this.css.padding_top+title.height+this.css.padding_bottom);
this.graphics.endFill();
}
this.addChild(title);
},
"public function get_width",function(){
return this.getChildAt(0).width;
},
"public function die",function(){
this.graphics.clear();
while(this.numChildren>0)
this.removeChildAt(0);
},
];},[],["flash.display.Sprite","flash.text.TextField","flash.text.TextFormat"], "0.8.0", "0.8.4"
);
// class elements.labels.Keys
joo.classLoader.prepare("package elements.labels",
"public class Keys extends flash.display.Sprite",6,function($$private){;return[
"private var",{_height:0},
"private var",{count:0},
"public var",{colours:null},
"public function Keys",function(stuff)
{this.super$6();
this.colours=new Array();
var key=0;
for(var $2 in stuff.sets)
{var b=stuff.sets[$2];
for(var $1 in b.get_keys()){var o=b.get_keys()[$1];
this.make_key$6(o.text,o['font-size'],o.colour);
this.colours.push(o.colour);
key++;
}
}
this.count$6=key;
},
"private function make_key",function(text,font_size,colour)
{
var tf=new flash.text.TextField();
tf.text=text;
var fmt=new flash.text.TextFormat();
fmt.color=colour;
fmt.font="Verdana";
fmt.size=font_size;
fmt.align="left";
tf.setTextFormat(fmt);
tf.autoSize="left";
this.addChild(tf);
},
"private function draw_line",function(x,y,height,colour){
y+=(height/2);
this.graphics.beginFill(colour,100);
this.graphics.drawRect(x,y-1,10,2);
this.graphics.endFill();
return x+12;
},
"public function resize",function(x,y){
if(this.count$6==0)
return;
this.x=x;
this.y=y;
var height=0;
var x=0;
var y=0;
this.graphics.clear();
for(var i=0;i<this.numChildren;i++)
{
var width=this.getChildAt(i).width;
if((this.x+x+width+12)>this.stage.stageWidth)
{
x=0;
y+=this.getChildAt(i).height;
height+=this.getChildAt(i).height;
}
this.draw_line$6(x,y,this.getChildAt(i).height,this.colours[i]);
x+=12;
this.getChildAt(i).x=x;
this.getChildAt(i).y=y;
x+=width+10;
}
height+=this.getChildAt(0).height;
this._height$6=height;
},
"public function get_height",function(){
return this._height$6;
},
"public function die",function(){
this.colours=null;
this.graphics.clear();
while(this.numChildren>0)
this.removeChildAt(0);
},
];},[],["flash.display.Sprite","Array","flash.text.TextField","flash.text.TextFormat"], "0.8.0", "0.8.4"
);
// class elements.labels.Title
joo.classLoader.prepare(
"package elements.labels",
"public class Title extends elements.labels.BaseLabel",7,function($$private){;return[
"public var",{colour:NaN},
"public var",{size:NaN},
"private var",{top_padding:0},
"public function Title",function(json)
{
this.super$7();
if(!json)
return;
this.style="font-size: 12px";
object_helper.merge_2(json,this);
this.css=new string.Css(this.style);
this.build(this.text);
},
"public function resize",function(){
if(this.text==null)
return;
this.getChildAt(0).width=this.stage.stageWidth;
var tmp=this.css.text_align;
switch(tmp)
{
case'left':
this.x=this.css.margin_left;
break;
case'right':
this.x=this.stage.stageWidth-(this.get_width()+this.css.margin_right);
break;
case'center':
default:
this.x=(this.stage.stageWidth/2)-(this.get_width()/2);
break;
}
this.y=this.css.margin_top;
},
"public function get_height",function(){
if(this.text==null)
return 0;
else
return this.css.padding_top+
this.css.margin_top+
this.getChildAt(0).height+
this.css.padding_bottom+
this.css.margin_bottom;
},
];},[],["elements.labels.BaseLabel","object_helper","string.Css"], "0.8.0", "0.8.4"
);
// class elements.labels.XLegend
joo.classLoader.prepare("package elements.labels",
"public class XLegend extends elements.labels.BaseLabel",7,function($$private){;return[
"public function XLegend",function(json)
{
this.super$7();
if(!json)
return;
object_helper.merge_2(json,this);
this.css=new string.Css(this.style);
this.build(this.text);
},
"public function resize",function(sc){
if(this.text==null)
return;
this.x=sc.left+((sc.width/2)-(this.get_width()/2));
this.getChildAt(0).y=this.stage.stageHeight-this.getChildAt(0).height;
},
"public function get_height",function(){
return this.height;
},
];},[],["elements.labels.BaseLabel","object_helper","string.Css"], "0.8.0", "0.8.4"
);
// class elements.labels.YLegendBase
joo.classLoader.prepare("package elements.labels",
"public class YLegendBase extends flash.display.Sprite",6,function($$private){;return[function(){joo.classLoader.init(flash.text.TextFieldAutoSize,flash.text.AntiAliasType);},
"public var",{tf:null},
"public var",{text:null},
"public var",{style:null},
"private var",{css:null},
{Embed:{systemFont:'Arial',fontName:'spArial',mimeType:'application/x-font'}},
"public static var",{ArialFont:null},
"public function YLegendBase",function(json,name)
{this.super$6();
if(json[name+'_legend']==undefined)
return;
if(json[name+'_legend'])
{
object_helper.merge_2(json[name+'_legend'],this);
}
this.css$6=new string.Css(this.style);
this.build$6(this.text);
},
"private function build",function(text){
var title=new flash.text.TextField();
title.x=0;
title.y=0;
var fmt=new flash.text.TextFormat();
fmt.color=this.css$6.color;
fmt.font="spArial";
fmt.size=this.css$6.font_size;
fmt.align="center";
title.htmlText=text;
title.setTextFormat(fmt);
title.autoSize="left";
title.embedFonts=true;
title.rotation=270;
title.height=title.textHeight;
title.antiAliasType=flash.text.AntiAliasType.ADVANCED;
title.autoSize=flash.text.TextFieldAutoSize.LEFT;
this.addChild(title);
},
"public function resize",function(){
if(this.text==null)
return;
},
"public function get_width",function(){
if(this.numChildren==0)
return 0;
else
return this.getChildAt(0).width;
},
"public function die",function(){
while(this.numChildren>0)
this.removeChildAt(0);
},
];},[],["flash.display.Sprite","object_helper","string.Css","flash.text.TextField","flash.text.TextFormat","flash.text.AntiAliasType","flash.text.TextFieldAutoSize"], "0.8.0", "0.8.4"
);
// class elements.labels.YLegendLeft
joo.classLoader.prepare("package elements.labels",
"public class YLegendLeft extends elements.labels.YLegendBase",7,function($$private){;return[
"public function YLegendLeft",function(json){
this.super$7(json,'y');
},
"public override function resize",function(){
if(this.numChildren==0)
return;
this.y=(this.stage.stageHeight/2)+(this.getChildAt(0).height/2);
this.x=0;
},
];},[],["elements.labels.YLegendBase"], "0.8.0", "0.8.4"
);
// class elements.labels.YLegendRight
joo.classLoader.prepare("package elements.labels",
"public class YLegendRight extends elements.labels.YLegendBase",7,function($$private){;return[
"public function YLegendRight",function(json){
this.super$7(json,'y2');
},
"public override function resize",function(){
if(this.numChildren==0)
return;
this.y=(this.stage.stageHeight/2)+(this.getChildAt(0).height/2);
this.x=this.stage.stageWidth-this.getChildAt(0).width;
},
];},[],["elements.labels.YLegendBase"], "0.8.0", "0.8.4"
);
// class elements.menu.CameraIcon
joo.classLoader.prepare("package elements.menu",
"public class CameraIcon extends elements.menu.menuItem",7,function($$private){;return[
"public function CameraIcon",function(chartId,props){
this.super$7(chartId,props);
},
"protected override function add_elements",function(){
this.draw_camera$7();
var width=this.add_text(this.props.get('text'),35);
return width+30;
},
"private function draw_camera",function(){
var s=new flash.display.Sprite();
s.graphics.beginFill(0x505050);
s.graphics.drawRoundRect(2,4,26,14,2,2);
s.graphics.drawRect(20,1,5,3);
s.graphics.endFill();
s.graphics.beginFill(0x202020);
s.graphics.drawCircle(9,11,4.5);
s.graphics.endFill();
this.addChild(s);
},
];},[],["elements.menu.menuItem","flash.display.Sprite"], "0.8.0", "0.8.4"
);
// class elements.menu.DefaultCameraIconProperties
joo.classLoader.prepare("package elements.menu",
"public class DefaultCameraIconProperties extends Properties",2,function($$private){;return[

"public function DefaultCameraIconProperties",function(json){
var parent=new Properties({
'colour':'#0000E0',
'text':"Save chart",
'javascript-function':"save_image",
'background-colour':"#ffffff",
'glow-colour':"#148DCF",
'text-colour':"#0000ff"
});
this.super$2(json,parent);
},
];},[],["Properties"], "0.8.0", "0.8.4"
);
// class elements.menu.DefaultMenuProperties
joo.classLoader.prepare("package elements.menu",
"public class DefaultMenuProperties extends Properties",2,function($$private){;return[

"public function DefaultMenuProperties",function(json){
var parent=new Properties({
'colour':'#E0E0E0',
"outline-colour":"#707070",
'camera-text':"Save chart"
});
this.super$2(json,parent);
},
];},[],["Properties"], "0.8.0", "0.8.4"
);
// class elements.menu.Menu
joo.classLoader.prepare("package elements.menu",
"public class Menu extends flash.display.Sprite",6,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(caurina.transitions.Equations,flash.events.MouseEvent);},
"private var",{original_alpha:NaN},
"private var",{props:null},
"private var",{first_showing:false},
"private var",{hidden_pos:NaN},
"public function Menu",function(chartID,json){this.super$6();
this.props$6=new elements.menu.DefaultMenuProperties(json);
this.original_alpha$6=0.4;
this.alpha=1;
var pos=5;
var height=0;
this.first_showing$6=true;
for(var $1 in json.values)
{var val=json.values[$1];
var tmp=new elements.menu.DefaultCameraIconProperties(val);
var menu_item=elements.menu.menu_item_factory.make(chartID,tmp);
menu_item.x=5;
menu_item.y=pos;
this.addChild(menu_item);
height=menu_item.y+menu_item.height+5;
pos+=menu_item.height+5;
}
var width=0;
for(var i=0;i<this.numChildren;i++)
width=Math.max(width,this.getChildAt(i).width);
this.draw$6(width+10,height);
this.hidden_pos$6=height;
this.addEventListener(flash.events.MouseEvent.MOUSE_OVER,$$bound(this,"mouseOverHandler"));
this.addEventListener(flash.events.MouseEvent.MOUSE_OUT,$$bound(this,"mouseOutHandler"));
},
"private function draw",function(width,height){
this.graphics.clear();
var colour=string.Utils.get_colour(this.props$6.get('colour'));
var o_colour=string.Utils.get_colour(this.props$6.get('outline-colour'));
this.graphics.lineStyle(1,o_colour);
this.graphics.beginFill(colour,1);
this.graphics.moveTo(0,-2);
this.graphics.lineTo(0,height);
this.graphics.lineTo(width-25,height);
this.graphics.lineTo(width-20,height+10);
this.graphics.lineTo(width,height+10);
this.graphics.lineTo(width,-2);
this.graphics.endFill();
this.graphics.lineStyle(1,o_colour);
this.graphics.moveTo(width-15,height+3);
this.graphics.lineTo(width-10,height+8);
this.graphics.lineTo(width-5,height+3);
this.graphics.moveTo(width-15,height);
this.graphics.lineTo(width-10,height+5);
this.graphics.lineTo(width-5,height);
},
"public function mouseOverHandler",function(event){
caurina.transitions.Tweener.removeTweens(this);
caurina.transitions.Tweener.addTween(this,{y:0,time:0.4,transition:caurina.transitions.Equations.easeOutBounce});
caurina.transitions.Tweener.addTween(this,{alpha:1,time:0.4,transition:caurina.transitions.Equations.easeOutBounce});
},
"public function mouseOutHandler",function(event){
this.hide_menu$6();
},
"private function hide_menu",function()
{
caurina.transitions.Tweener.removeTweens(this);
caurina.transitions.Tweener.addTween(this,{y:-this.hidden_pos$6,time:0.4,transition:caurina.transitions.Equations.easeOutBounce});
caurina.transitions.Tweener.addTween(this,{alpha:this.original_alpha$6,time:0.4,transition:caurina.transitions.Equations.easeOutBounce});
},
"public function resize",function(){
if(this.first_showing$6){
this.y=0;
this.first_showing$6=false;
caurina.transitions.Tweener.removeTweens(this);
caurina.transitions.Tweener.addTween(this,{time:3,onComplete:$$bound(this,"hide_menu$6")});
}
else{
this.y=-(this.height)+10;
}
this.x=this.stage.stageWidth-this.width-5;
},
];},[],["flash.display.Sprite","elements.menu.DefaultMenuProperties","elements.menu.DefaultCameraIconProperties","elements.menu.menu_item_factory","Math","flash.events.MouseEvent","string.Utils","caurina.transitions.Tweener","caurina.transitions.Equations"], "0.8.0", "0.8.4"
);
// class elements.menu.menuItem
joo.classLoader.prepare("package elements.menu",
"public class menuItem extends flash.display.Sprite",6,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.events.MouseEvent);},
"protected var",{chartId:null},
"protected var",{props:null},
"public function menuItem",function(chartId,props){this.super$6();
this.props=props;
this.buttonMode=true;
this.useHandCursor=true;
this.chartId=chartId;
this.alpha=0.5;
var width=this.add_elements();
this.draw_bg$6(
width+
10
);
this.addEventListener(flash.events.MouseEvent.CLICK,$$bound(this,"mouseClickHandler"));
this.addEventListener(flash.events.MouseEvent.MOUSE_DOWN,$$bound(this,"mouseDownHandler"));
this.addEventListener(flash.events.MouseEvent.MOUSE_OVER,$$bound(this,"mouseOverHandler"));
this.addEventListener(flash.events.MouseEvent.MOUSE_OUT,$$bound(this,"mouseOutHandler"));
},
"protected function add_elements",function(){
var width=this.add_text(this.props.get('text'),5);
return width;
},
"private function draw_bg",function(width){
this.graphics.beginFill(string.Utils.get_colour(this.props.get('background-colour')));
this.graphics.drawRoundRect(0,0,width,20,5,5);
this.graphics.endFill();
},
"protected function add_text",function(text,left){
var title=new flash.text.TextField();
title.x=left;
title.y=0;
title.htmlText=text;
var fmt=new flash.text.TextFormat();
fmt.color=string.Utils.get_colour(this.props.get('text-colour'));
fmt.font='Verdana';
fmt.size=10;
fmt.underline=true;
title.setTextFormat(fmt);
title.autoSize="left";
title.mouseEnabled=false;
this.addChild(title);
return title.width;
},
"public function mouseClickHandler",function(event){
this.alpha=0.0;
tr.aces('Menu item clicked:',this.props.get('javascript-function')+'('+this.chartId+')');
flash.external.ExternalInterface.call(this.props.get('javascript-function'),this.chartId);
this.alpha=1.0;
},
"public function mouseOverHandler",function(event){
this.alpha=1;
var glow=new flash.filters.GlowFilter();
glow.color=string.Utils.get_colour(this.props.get('glow-colour'));
glow.alpha=0.8;
glow.blurX=4;
glow.blurY=4;
glow.inner=false;
this.filters=new Array(glow);
},
"public function mouseDownHandler",function(event){
this.alpha=1.0;
},
"public function mouseOutHandler",function(event){
this.alpha=0.5;
this.filters=new Array();
},
];},[],["flash.display.Sprite","flash.events.MouseEvent","string.Utils","flash.text.TextField","flash.text.TextFormat","tr","flash.external.ExternalInterface","flash.filters.GlowFilter","Array"], "0.8.0", "0.8.4"
);
// class elements.menu.menu_item_factory
joo.classLoader.prepare("package elements.menu",
"public class menu_item_factory",1,function($$private){;return[
"public static function make",function(chartID,style){
switch(style.get('type'))
{
case'camera-icon':
return new elements.menu.CameraIcon(chartID,style);
break;
default:
return new elements.menu.menuItem(chartID,style);
break;
}
},
];},["make"],["elements.menu.CameraIcon","elements.menu.menuItem"], "0.8.0", "0.8.4"
);
// class ErrorMsg
joo.classLoader.prepare(
"package",
"public class ErrorMsg extends flash.display.Sprite",6,function($$private){var as=joo.as;return[
"public function ErrorMsg",function(msg){this.super$6();
var title=new flash.text.TextField();
title.text=msg;
var fmt=new flash.text.TextFormat();
fmt.color=0x000000;
fmt.font="Courier";
fmt.size=10;
fmt.align="left";
title.setTextFormat(fmt);
title.autoSize="left";
title.border=true;
title.x=5;
title.y=5;
this.addChild(title);
},
"public function add_html",function(html){
var txt=new flash.text.TextField();
var style=new flash.text.StyleSheet();
var hover=new Object();
hover.fontWeight="bold";
hover.color="#0000FF";
var link=new Object();
link.fontWeight="bold";
link.textDecoration="underline";
link.color="#0000A0";
var active=new Object();
active.fontWeight="bold";
active.color="#0000A0";
var visited=new Object();
visited.fontWeight="bold";
visited.color="#CC0099";
visited.textDecoration="underline";
style.setStyle("a:link",link);
style.setStyle("a:hover",hover);
style.setStyle("a:active",active);
style.setStyle(".visited",visited);
txt.styleSheet=style;
txt.htmlText=html;
txt.autoSize="left";
txt.border=true;
var t=as(this.getChildAt(0),flash.text.TextField);
txt.y=t.y+t.height+10;
txt.x=5;
this.addChild(txt);
},
];},[],["flash.display.Sprite","flash.text.TextField","flash.text.TextFormat","flash.text.StyleSheet","Object"], "0.8.0", "0.8.4"
);
// class ExternalInterfaceManager
joo.classLoader.prepare("package",
"public class ExternalInterfaceManager",1,function($$private){;return[function(){joo.classLoader.init(flash.external.ExternalInterface);},

"public var",{has_id:false},
"public var",{chart_id:null},
"private static var",{_instance:null},
"public static function getInstance",function(){
if($$private._instance==null){
$$private._instance=new ExternalInterfaceManager();
}
return $$private._instance;
},
"public function setUp",function(chart_id){
this.has_id=true;
this.chart_id=chart_id;
tr.aces('this.chart_id',this.chart_id);
},
"public function callJavascript",function(functionName){var optionalArgs=Array.prototype.slice.call(arguments,1);
if(flash.external.ExternalInterface.available){
if(this.has_id){
tr.aces(functionName,optionalArgs);
optionalArgs.unshift(this.chart_id);
tr.aces(functionName,optionalArgs);
}
return flash.external.ExternalInterface.call(functionName,optionalArgs);
}
},
];},["getInstance"],["tr","flash.external.ExternalInterface"], "0.8.0", "0.8.4"
);
// class global.Global
joo.classLoader.prepare("package global",
"public class Global",1,function($$private){;return[
"private static var",{instance:null},
"private static var",{allowInstantiation:false},
"public var",{x_labels:null},
"public var",{x_legend:null},
"private var",{tooltip:null},
"public function Global",function(){
},
"public static function getInstance",function(){
if($$private.instance==null){$$private.allowInstantiation=true;$$private.instance=new global.Global();$$private.allowInstantiation=false;
}
return $$private.instance;
},
"public function get_x_label",function(pos){
tr.ace('xxx');
tr.ace(this.x_labels==null);
tr.ace(pos);
if(this.x_labels==null)
return null;
else
return this.x_labels.get(pos);
},
"public function get_x_legend",function(){
if(this.x_legend==null)
return null;
else
return this.x_legend.text;
},
"public function set_tooltip_string",function(s){
tr.ace('@@@@@@@');
tr.ace(s);
this.tooltip$1=s;
},
"public function get_tooltip_string",function(){
return this.tooltip$1;
},
];},["getInstance"],["tr"], "0.8.0", "0.8.4"
);
// class JsonErrorMsg
joo.classLoader.prepare("package",
"public class JsonErrorMsg extends ErrorMsg",7,function($$private){;return[
"public function JsonErrorMsg",function(json,e){
var tmp="Open Flash Chart\n\n";
tmp+="JSON Parse Error ["+e.message+"]\n";
var pos=json.indexOf("\n",e.errorID);
var s=json.substr(0,pos);
var lines=s.split("\n");
tmp+="Error at character "+e.errorID+", line "+lines.length+":\n\n";
for(var i=3;i>0;i--){
if(lines.length-i>-1)
tmp+=(lines.length-i).toString()+": "+lines[lines.length-i];
}
this.super$7(tmp);
},
];},[],["ErrorMsg"], "0.8.0", "0.8.4"
);
// class JsonInspector
joo.classLoader.prepare("package",
"public class JsonInspector",1,function($$private){var as=joo.as;return[

"public static function has_pie_chart",function(json)
{
var elements=as(json['elements'],Array);
for(var i=0;i<elements.length;i++)
{
if(elements[i]['type']=='pie')
return true;
}
return false;
},
"public static function is_radar",function(json)
{
if(json['radar_axis']!=null)
return true;
return false;
},
];},["has_pie_chart","is_radar"],["Array"], "0.8.0", "0.8.4"
);
// class Loading
joo.classLoader.prepare(
"package",
"public class Loading extends flash.display.Sprite",6,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(Math,flash.events.Event);},
"private var",{tf:null},
"public function Loading",function(text){this.super$6();
this.tf$6=new flash.text.TextField();
this.tf$6.text=text;
var fmt=new flash.text.TextFormat();
fmt.color=0x000000;
fmt.font="Verdana";
fmt.size=12;
fmt.align="center";
this.tf$6.setTextFormat(fmt);
this.tf$6.autoSize="left";
this.tf$6.x=5;
this.tf$6.y=5;
this.addEventListener(flash.events.Event.ENTER_FRAME,$$bound(this,"onEnter$6"));
this.addChild(this.tf$6);
this.graphics.lineStyle(2,0x808080,1);
this.graphics.beginFill(0xf0f0f0);
this.graphics.drawRoundRect(0,0,this.tf$6.width+10,this.tf$6.height+10,5,5);
var spin=new flash.display.Sprite();
spin.x=this.tf$6.width+40;
spin.y=(this.tf$6.height+10)/2;
var radius=15;
var dots=6;
var colours=[0xF0F0F0,0xD0D0D0,0xB0B0B0,0x909090,0x707070,0x505050,0x303030];
for(var i=0;i<dots;i++)
{
var deg=(360/dots)*i;
var radians=deg*(Math.PI/180);
var x=radius*Math.cos(radians);
var y=radius*Math.sin(radians);
spin.graphics.lineStyle(0,0,0);
spin.graphics.beginFill(colours[i],1);
spin.graphics.drawCircle(x,y,4);
}
this.addChild(spin);
var dropShadow=new flash.filters.DropShadowFilter();
dropShadow.blurX=4;
dropShadow.blurY=4;
dropShadow.distance=4;
dropShadow.angle=45;
dropShadow.quality=2;
dropShadow.alpha=0.5;
this.filters=[dropShadow];
},
"private function onEnter",function(event){
if(this.stage){
this.x=(this.stage.stageWidth/2)-((this.tf$6.width+10)/2);
this.y=(this.stage.stageHeight/2)-((this.tf$6.height+10)/2);
this.removeEventListener(flash.events.Event.ENTER_FRAME,$$bound(this,"onEnter$6"));
}
this.getChildAt(1).rotation+=5;
},
];},[],["flash.display.Sprite","flash.text.TextField","flash.text.TextFormat","flash.events.Event","Math","flash.filters.DropShadowFilter"], "0.8.0", "0.8.4"
);
// class main
joo.classLoader.prepare(
"package",
{SWF:{width:"450",height:"300"}},
"public class main extends flash.display.Sprite",6,function($$private){var is=joo.is,as=joo.as,$$bound=joo.boundMethod,trace=joo.trace;return[function(){joo.classLoader.init(Tooltip,flash.display.StageScaleMode,flash.net.URLRequestMethod,flash.events.ContextMenuEvent,flash.events.ProgressEvent,flash.events.IOErrorEvent,flash.events.MouseEvent,flash.display.StageAlign,flash.events.Event,flash.external.ExternalInterface,flash.net.URLLoaderDataFormat);},
"public var",{VERSION:"2 Lug Wyrm Charmer"},
"private var",{title:null},
"private var",{x_axis:null},
"private var",{radar_axis:null},
"private var",{x_legend:null},
"private var",{y_axis:null},
"private var",{y_axis_right:null},
"private var",{y_legend:null},
"private var",{y_legend_2:null},
"private var",{keys:null},
"private var",{obs:null},
"public var",{tool_tip_wrapper:null},
"private var",{sc:null},
"private var",{tooltip:null},
"private var",{background:null},
"private var",{menu:null},
"private var",{ok:false},
"private var",{URL:null},
"private var",{id:null},
"private var",{chart_parameters:null},
"private var",{json:null},
"public function main",function(){this.super$6();
this.chart_parameters$6=(this.loaderInfo).parameters;
if(this.chart_parameters$6['loading']==null)
this.chart_parameters$6['loading']='Loading data...';
var l=new Loading(this.chart_parameters$6['loading']);
this.addChild(l);
this.build_right_click_menu$6();
this.ok$6=false;
if(!this.find_data())
{
try{
var file="../data-files/bar-2.txt";
this.load_external_file$6(file);
}
catch(e){if(is(e,Error)){
this.show_error$6('Loading test data\n'+file+'\n'+e.message);
}else throw e;}
}
this.addCallback$6("reload",$$bound(this,"reload"));
this.addCallback$6("load",$$bound(this,"load"));
this.addCallback$6("post_image",$$bound(this,"post_image"));
this.addCallback$6("get_img_binary",$$bound(this,"getImgBinary"));
this.addCallback$6("get_version",$$bound(this,"getVersion"));
if(this.chart_parameters$6['id'])
{
var ex=ExternalInterfaceManager.getInstance();
ex.setUp(this.chart_parameters$6['id']);
}
if(this.chart_parameters$6['id'])
this.callExternalCallback$6("ofc_ready",this.chart_parameters$6['id']);
else
this.callExternalCallback$6("ofc_ready");
this.set_the_stage$6();
},
"private function addCallback",function(functionName,closure){
if(flash.external.ExternalInterface.available)
flash.external.ExternalInterface.addCallback(functionName,closure);
},
"private function callExternalCallback",function(functionName){var optionalArgs=Array.prototype.slice.call(arguments,1);
if(flash.external.ExternalInterface.available)
return flash.external.ExternalInterface.call(functionName,optionalArgs);
},
"public function getVersion",function(){return this.VERSION;},
"public function getImgBinary",function(){
tr.ace('Saving image :: image_binary()');
var bmp=new flash.display.BitmapData(this.stage.stageWidth,this.stage.stageHeight);
bmp.draw(this);
var b64=new mx.utils.Base64Encoder();
var b=com.adobe.images.PNGEncoder.encode(bmp);
b64.encodeBytes(b);
return b64.toString();
},
"public function saveImage",function(e){
this.callExternalCallback$6("save_image",this.chart_parameters$6['id']);
},
"private function image_binary",function(){
tr.ace('Saving image :: image_binary()');
var pngSource=new flash.display.BitmapData(this.width,this.height);
pngSource.draw(this);
return com.adobe.images.PNGEncoder.encode(pngSource);
},
"public function post_image",function(url,callback,debug){var this$=this;
var header=new flash.net.URLRequestHeader("Content-type","application/octet-stream");
var request=new flash.net.URLRequest(url);
request.requestHeaders.push(header);
request.method=flash.net.URLRequestMethod.POST;
request.data=this.image_binary$6();
var loader=new flash.net.URLLoader();
loader.dataFormat=flash.net.URLLoaderDataFormat.VARIABLES;
var id='';
if(this.chart_parameters$6['id'])
id=this.chart_parameters$6['id'];
if(debug)
{
flash.net.navigateToURL(request,"_blank");
}
else
{
loader.addEventListener(flash.events.ProgressEvent.PROGRESS,function(e){
tr.ace("progress:"+e.bytesLoaded+", total: "+e.bytesTotal);
if((e.bytesLoaded==e.bytesTotal)&&(callback!=null)){
tr.aces('Calling: ',callback+'('+id+')');
this$.call(callback,id);
}
});
try{
loader.load(request);
}catch(error){if(is(error,Error)){
tr.ace("unable to load:"+error);
}else throw error;}
}
},
"private function onContextMenuHandler",function(event)
{
},
"public function find_data",function(){
var vars=this.callExternalCallback$6("window.location.search.substring",1);
if(vars!=null)
{
var p=vars.split('&');
for(var $1 in p)
{var v=p[$1];
if(v.indexOf('ofc=')>-1)
{
var tmp=v.split('=');
tr.ace('Found external file:'+tmp[1]);
this.load_external_file$6(tmp[1]);
return true;
}
}
}
if(this.chart_parameters$6['data-file'])
{
this.load_external_file$6(this.chart_parameters$6['data-file']);
return true;
}
var get_data='open_flash_chart_data';
if(this.chart_parameters$6['get-data'])
get_data=this.chart_parameters$6['get-data'];
var json_string;
if(this.chart_parameters$6['id'])
json_string=this.callExternalCallback$6(get_data,this.chart_parameters$6['id']);
else
json_string=this.callExternalCallback$6(get_data);
if(json_string!=null)
{
if(is(json_string,String))
{
this.parse_json$6(json_string);
this.ok$6=true;
return true;
}
}
return false;
},
"public function reload",function(url){
var l=new Loading(this.chart_parameters$6['loading']);
this.addChild(l);
this.load_external_file$6(url);
},
"private function load_external_file",function(file){
this.URL$6=file;
var loader=new flash.net.URLLoader();
loader.addEventListener(flash.events.IOErrorEvent.IO_ERROR,$$bound(this,"ioError$6"));
loader.addEventListener(flash.events.Event.COMPLETE,$$bound(this,"xmlLoaded$6"));
var request=new flash.net.URLRequest(file);
loader.load(request);
},
"private function ioError",function(e){
this.removeChildAt(0);
var msg=new ErrorMsg('Open Flash Chart\nIO ERROR\nLoading test data\n'+e.text);
msg.add_html('This is the URL that I tried to open:<br><a href="'+this.URL$6+'">'+this.URL$6+'</a>');
this.addChild(msg);
},
"private function show_error",function(msg){
this.removeChildAt(0);
var m=new ErrorMsg(msg);
this.addChild(m);
},
"public function get_x_legend",function(){
return this.x_legend$6;
},
"private function set_the_stage",function(){
this.stage.align=flash.display.StageAlign.TOP_LEFT;
this.stage.scaleMode=flash.display.StageScaleMode.NO_SCALE;
this.stage.addEventListener(flash.events.Event.ACTIVATE,$$bound(this,"activateHandler$6"));
this.stage.addEventListener(flash.events.Event.RESIZE,$$bound(this,"resizeHandler$6"));
this.stage.addEventListener(flash.events.Event.MOUSE_LEAVE,$$bound(this,"mouseOut$6"));
this.addEventListener(flash.events.MouseEvent.MOUSE_OVER,$$bound(this,"mouseMove$6"));
},
"private function mouseMove",function(event){
if(!this.tooltip$6)
return;
switch(this.tooltip$6.get_tip_style()){
case Tooltip.CLOSEST:
this.mouse_move_closest$6(event);
break;
case Tooltip.PROXIMITY:
this.mouse_move_proximity$6(as(event,flash.events.MouseEvent));
break;
case Tooltip.NORMAL:
this.mouse_move_follow$6(as(event,flash.events.MouseEvent));
break;
}
},
"private function mouse_move_follow",function(event){
if(is(event.target,charts.series.has_tooltip))
this.tooltip$6.draw(as(event.target,charts.series.has_tooltip));
else
this.tooltip$6.hide();
},
"private function mouse_move_proximity",function(event){
var elements=this.obs$6.mouse_move_proximity(this.mouseX,this.mouseY);
this.tooltip$6.closest(elements);
},
"private function mouse_move_closest",function(event){
var elements=this.obs$6.closest_2(this.mouseX,this.mouseY);
this.tooltip$6.closest(elements);
},
"private function activateHandler",function(event){
tr.aces("activateHandler:",event);
tr.aces("stage",this.stage);
},
"private function resizeHandler",function(event){
this.resize$6();
},
"private function resize_pie",function(){
this.addEventListener(flash.events.MouseEvent.MOUSE_MOVE,$$bound(this,"mouseMove$6"));
this.background$6.resize();
this.title$6.resize();
this.sc$6=new ScreenCoords(
this.title$6.get_height(),0,this.stage.stageWidth,this.stage.stageHeight,
null,null,null,0,0,false);
this.obs$6.resize(this.sc$6);
return this.sc$6;
},
"private function resize_radar",function(){
this.addEventListener(flash.events.MouseEvent.MOUSE_MOVE,$$bound(this,"mouseMove$6"));
this.background$6.resize();
this.title$6.resize();
this.keys$6.resize(0,this.title$6.get_height());
var top=this.title$6.get_height()+this.keys$6.get_height();
var sc=new ScreenCoordsRadar(top,0,this.stage.stageWidth,this.stage.stageHeight);
sc.set_range(this.radar_axis$6.get_range());
sc.set_angles(this.obs$6.get_max_x()-this.obs$6.get_min_x()+1);
this.radar_axis$6.resize(sc);
this.obs$6.resize(sc);
return sc;
},
"private function resize",function(){
if(!this.ok$6)
return;
var sc;
if(this.radar_axis$6!=null)
sc=this.resize_radar$6();
else if(this.obs$6.has_pie())
sc=this.resize_pie$6();
else
sc=this.resize_chart$6();
if(this.menu$6)
this.menu$6.resize();
if(this.chart_parameters$6['id'])
this.callExternalCallback$6("ofc_resize",sc.left,sc.width,sc.top,sc.height,this.chart_parameters$6['id']);
else
this.callExternalCallback$6("ofc_resize",sc.left,sc.width,sc.top,sc.height);
sc=null;
},
"private function resize_chart",function(){
this.addEventListener(flash.events.MouseEvent.MOUSE_MOVE,$$bound(this,"mouseMove$6"));
this.background$6.resize();
this.title$6.resize();
var left=this.y_legend$6.get_width()+this.y_axis$6.get_width();
this.keys$6.resize(left,this.title$6.get_height());
var top=this.title$6.get_height()+this.keys$6.get_height();
var bottom=this.stage.stageHeight;
bottom-=(this.x_legend$6.get_height()+this.x_axis$6.get_height());
var right=this.stage.stageWidth;
right-=this.y_legend_2$6.get_width();
right-=this.y_axis_right$6.get_width();
this.sc$6=new ScreenCoords(
top,left,right,bottom,
this.y_axis$6.get_range(),
this.y_axis_right$6.get_range(),
this.x_axis$6.get_range(),
this.x_axis$6.first_label_width(),
this.x_axis$6.last_label_width(),
false);
trace("sc.left",this.sc$6.left);
this.sc$6.set_bar_groups(this.obs$6.groups);
this.x_axis$6.resize(this.sc$6,
this.stage.stageHeight-(this.x_legend$6.get_height()+this.x_axis$6.labels.get_height())
);
this.y_axis$6.resize(this.y_legend$6.get_width(),this.sc$6);
this.y_axis_right$6.resize(0,this.sc$6);
this.x_legend$6.resize(this.sc$6);
this.y_legend$6.resize();
this.y_legend_2$6.resize();
this.obs$6.resize(this.sc$6);
this.dispatchEvent(new flash.events.Event("on-show"));
return this.sc$6;
},
"private function mouseOut",function(event){
if(this.tooltip$6!=null)
this.tooltip$6.hide();
if(this.obs$6!=null)
this.obs$6.mouse_out();
},
"public function load",function(s){
this.parse_json$6(s);
},
"private function xmlLoaded",function(event){
var loader=(event.target);
this.parse_json$6(loader.data);
},
"private function parse_json",function(json_string){
var ok=false;
try{
var json=com.serialization.json.JSON.deserialize(json_string);
ok=true;
}
catch(e){if(is(e,Error)){
this.removeChildAt(0);
this.addChild(new JsonErrorMsg(as(json_string,String),e));
}else throw e;}
if(ok)
{
this.removeChildAt(0);
this.build_chart$6(json);
json=null;
}
json_string='';
},
"private function build_chart",function(json){
tr.ace('----');
tr.ace(com.serialization.json.JSON.serialize(json));
tr.ace('----');
if(this.obs$6!=null)
this.die$6();
NumberFormat.getInstance(json);
NumberFormat.getInstanceY2(json);
this.tooltip$6=new Tooltip(json.tooltip);
var g=global.Global.getInstance();
g.set_tooltip_string(this.tooltip$6.tip_text);
this.background$6=new elements.Background(json);
this.title$6=new elements.labels.Title(json.title);
this.addChild(this.background$6);
if(JsonInspector.is_radar(json)){
this.obs$6=charts.Factory.MakeChart(json);
this.radar_axis$6=new elements.axis.RadarAxis(json.radar_axis);
this.keys$6=new elements.labels.Keys(this.obs$6);
this.addChild(this.radar_axis$6);
this.addChild(this.keys$6);
}
else if(!JsonInspector.has_pie_chart(json))
{
this.build_chart_background$6(json);
}
else
{
this.obs$6=charts.Factory.MakeChart(json);
this.tooltip$6.set_tip_style(Tooltip.NORMAL);
}
this.addChild(this.title$6);
for(var $1 in this.obs$6.sets){var set=this.obs$6.sets[$1];
this.addChild(set);}
this.addChild(this.tooltip$6);
if(json['menu']!=null){
this.menu$6=new elements.menu.Menu('99',json['menu']);
this.addChild(this.menu$6);
}
this.ok$6=true;
this.resize$6();
},
"private function build_chart_background",function(json){
this.obs$6=charts.Factory.MakeChart(json);
this.x_legend$6=new elements.labels.XLegend(json.x_legend);
this.y_legend$6=new elements.labels.YLegendLeft(json);
this.y_legend_2$6=new elements.labels.YLegendRight(json);
this.x_axis$6=new elements.axis.XAxis(json,this.obs$6.get_min_x(),this.obs$6.get_max_x());
this.y_axis$6=new elements.axis.YAxisLeft();
this.y_axis_right$6=new elements.axis.YAxisRight();
var g=global.Global.getInstance();
g.x_labels=this.x_axis$6.labels;
g.x_legend=this.x_legend$6;
this.obs$6.tooltip_replace_labels(this.x_axis$6.labels);
this.keys$6=new elements.labels.Keys(this.obs$6);
this.addChild(this.x_legend$6);
this.addChild(this.y_legend$6);
this.addChild(this.y_legend_2$6);
this.addChild(this.y_axis$6);
this.addChild(this.y_axis_right$6);
this.addChild(this.x_axis$6);
this.addChild(this.keys$6);
this.y_axis$6.init(json);
this.y_axis_right$6.init(json);
},
"private function die",function(){
this.obs$6.die();
this.obs$6=null;
if(this.tooltip$6!=null)this.tooltip$6.die();
if(this.x_legend$6!=null)this.x_legend$6.die();
if(this.y_legend$6!=null)this.y_legend$6.die();
if(this.y_legend_2$6!=null)this.y_legend_2$6.die();
if(this.y_axis$6!=null)this.y_axis$6.die();
if(this.y_axis_right$6!=null)this.y_axis_right$6.die();
if(this.x_axis$6!=null)this.x_axis$6.die();
if(this.keys$6!=null)this.keys$6.die();
if(this.title$6!=null)this.title$6.die();
if(this.radar_axis$6!=null)this.radar_axis$6.die();
if(this.background$6!=null)this.background$6.die();
this.tooltip$6=null;
this.x_legend$6=null;
this.y_legend$6=null;
this.y_legend_2$6=null;
this.y_axis$6=null;
this.y_axis_right$6=null;
this.x_axis$6=null;
this.keys$6=null;
this.title$6=null;
this.radar_axis$6=null;
this.background$6=null;
while(this.numChildren>0)
this.removeChildAt(0);
if(this.hasEventListener(flash.events.MouseEvent.MOUSE_MOVE))
this.removeEventListener(flash.events.MouseEvent.MOUSE_MOVE,$$bound(this,"mouseMove$6"));
},
"private function build_right_click_menu",function(){
var cm=new flash.ui.ContextMenu();
cm.addEventListener(flash.events.ContextMenuEvent.MENU_SELECT,$$bound(this,"onContextMenuHandler$6"));
cm.hideBuiltInItems();
var fs=new flash.ui.ContextMenuItem("Charts by Open Flash Chart [Version "+this.VERSION+"]");
fs.addEventListener(
flash.events.ContextMenuEvent.MENU_ITEM_SELECT,
function doSomething(e){
var url="http://teethgrinder.co.uk/open-flash-chart-2/";
var request=new flash.net.URLRequest(url);
flash.net.navigateToURL(request,'_blank');
});
cm.customItems.push(fs);
var save_image_message=(this.chart_parameters$6['save_image_message'])?this.chart_parameters$6['save_image_message']:'Save Image Locally';
var dl=new flash.ui.ContextMenuItem(save_image_message);
dl.addEventListener(flash.events.ContextMenuEvent.MENU_ITEM_SELECT,$$bound(this,"saveImage"));
cm.customItems.push(dl);
this.contextMenu=cm;
},
"public function format_y_axis_label",function(val){
return NumberUtils.format(val,2,true,true,false);
},
];},[],["flash.display.Sprite","flash.display.LoaderInfo","Loading","Error","ExternalInterfaceManager","flash.external.ExternalInterface","tr","flash.display.BitmapData","mx.utils.Base64Encoder","com.adobe.images.PNGEncoder","flash.net.URLRequestHeader","flash.net.URLRequest","flash.net.URLRequestMethod","flash.net.URLLoader","flash.net.URLLoaderDataFormat","flash.events.ProgressEvent","String","flash.events.IOErrorEvent","flash.events.Event","ErrorMsg","flash.display.StageAlign","flash.display.StageScaleMode","flash.events.MouseEvent","Tooltip","charts.series.has_tooltip","ScreenCoords","ScreenCoordsRadar","com.serialization.json.JSON","JsonErrorMsg","NumberFormat","global.Global","elements.Background","elements.labels.Title","JsonInspector","charts.Factory","elements.axis.RadarAxis","elements.labels.Keys","elements.menu.Menu","elements.labels.XLegend","elements.labels.YLegendLeft","elements.labels.YLegendRight","elements.axis.XAxis","elements.axis.YAxisLeft","elements.axis.YAxisRight","flash.ui.ContextMenu","flash.events.ContextMenuEvent","flash.ui.ContextMenuItem","NumberUtils"], "0.8.0", "0.8.4"
);
// class mx.core.Singleton
joo.classLoader.prepare(
"package mx.core",
{ExcludeClass:{}},
"public class Singleton",1,function($$private){;return[

"mx_internal static const",{VERSION:"4.1.0.16076"},

"private static var",{classMap:function(){return({});}},
"public static function registerClass",function(interfaceName,
clazz)
{
var c=$$private.classMap[interfaceName];
if(!c)
$$private.classMap[interfaceName]=clazz;
},
"public static function getClass",function(interfaceName)
{
return $$private.classMap[interfaceName];
},
"public static function getInstance",function(interfaceName)
{
var c=$$private.classMap[interfaceName];
if(!c)
{
throw new Error("No class registered for interface '"+
interfaceName+"'.");
}
return c["getInstance"]();
},
];},["registerClass","getClass","getInstance"],["Error"], "0.8.0", "0.8.4"
);
// class mx.formatters.DateBase
joo.classLoader.prepare(
"package mx.formatters",
{ResourceBundle:{$value:"formatters"}},
{ResourceBundle:{$value:"SharedResources"}},
"public class DateBase",1,function($$private){;return[function(){joo.classLoader.init(flash.events.Event);},

"mx_internal static const",{VERSION:"4.1.0.16076"},

"private static var",{initialized:false},
"private static var",{_resourceManager:null},
"private static function get resourceManager",function()
{
if(!$$private._resourceManager)
$$private._resourceManager=mx.resources.ResourceManager.getInstance();
return $$private._resourceManager;
},
"private static var",{_dayNamesLong:null},
"private static var",{dayNamesLongOverride:null},
"public static function get dayNamesLong",function()
{
$$private.initialize();
return $$private._dayNamesLong;
},
"public static function set dayNamesLong",function(value)
{
$$private.dayNamesLongOverride=value;
$$private._dayNamesLong=value!=null?
value:
$$private.resourceManager.getStringArray(
"SharedResources","dayNames");
},
"private static var",{_dayNamesShort:null},
"private static var",{dayNamesShortOverride:null},
"public static function get dayNamesShort",function()
{
$$private.initialize();
return $$private._dayNamesShort;
},
"public static function set dayNamesShort",function(value)
{
$$private.dayNamesShortOverride=value;
$$private._dayNamesShort=value!=null?
value:
$$private.resourceManager.getStringArray(
"formatters","dayNamesShort");
},
"mx_internal static function get defaultStringKey",function()
{
$$private.initialize();
return mx.formatters.DateBase.monthNamesLong.concat(mx.formatters.DateBase.timeOfDay);
},
"private static var",{_monthNamesLong:null},
"private static var",{monthNamesLongOverride:null},
"public static function get monthNamesLong",function()
{
$$private.initialize();
return $$private._monthNamesLong;
},
"public static function set monthNamesLong",function(value)
{
$$private.monthNamesLongOverride=value;
$$private._monthNamesLong=value!=null?
value:
$$private.resourceManager.getStringArray(
"SharedResources","monthNames");
if(value==null)
{
var monthSymbol=$$private.resourceManager.getString(
"SharedResources","monthSymbol");
if(monthSymbol!=" ")
{
var n=$$private._monthNamesLong?$$private._monthNamesLong.length:0;
for(var i=0;i<n;i++)
{
$$private._monthNamesLong[i]+=monthSymbol;
}
}
}
},
"private static var",{_monthNamesShort:null},
"private static var",{monthNamesShortOverride:null},
"public static function get monthNamesShort",function()
{
$$private.initialize();
return $$private._monthNamesShort;
},
"public static function set monthNamesShort",function(value)
{
$$private.monthNamesShortOverride=value;
$$private._monthNamesShort=value!=null?
value:
$$private.resourceManager.getStringArray(
"formatters","monthNamesShort");
if(value==null)
{
var monthSymbol=$$private.resourceManager.getString(
"SharedResources","monthSymbol");
if(monthSymbol!=" ")
{
var n=$$private._monthNamesShort?$$private._monthNamesShort.length:0;
for(var i=0;i<n;i++)
{
$$private._monthNamesShort[i]+=monthSymbol;
}
}
}
},
"private static var",{_timeOfDay:null},
"private static var",{timeOfDayOverride:null},
"public static function get timeOfDay",function()
{
$$private.initialize();
return $$private._timeOfDay;
},
"public static function set timeOfDay",function(value)
{
$$private.timeOfDayOverride=value;
var am=$$private.resourceManager.getString("formatters","am");
var pm=$$private.resourceManager.getString("formatters","pm");
$$private._timeOfDay=value!=null?value:[am,pm];
},
"private static function initialize",function()
{
if(!$$private.initialized)
{
$$private.resourceManager.addEventListener(
flash.events.Event.CHANGE,$$private.static_resourceManager_changeHandler,
false,0,true);
$$private.static_resourcesChanged();
$$private.initialized=true;
}
},
"private static function static_resourcesChanged",function()
{
mx.formatters.DateBase.dayNamesLong=$$private.dayNamesLongOverride;
mx.formatters.DateBase.dayNamesShort=$$private.dayNamesShortOverride;
mx.formatters.DateBase.monthNamesLong=$$private.monthNamesLongOverride;
mx.formatters.DateBase.monthNamesShort=$$private.monthNamesShortOverride;
mx.formatters.DateBase.timeOfDay=$$private.timeOfDayOverride;
},
"mx_internal static function extractTokenDate",function(date,
tokenInfo)
{
$$private.initialize();
var result="";
var key=$$int(tokenInfo.end)-$$int(tokenInfo.begin);
var day;
var hours;
switch(tokenInfo.token)
{
case"Y":
{
var year=date.getFullYear().toString();
if(key<3)
return year.substr(2);
else if(key>4)
return $$private.setValue(Number(year),key);
else
return year;
}
case"M":
{
var month=$$int(date.getMonth());
if(key<3)
{
month++;
result+=$$private.setValue(month,key);
return result;
}
else if(key==3)
{
return mx.formatters.DateBase.monthNamesShort[month];
}
else
{
return mx.formatters.DateBase.monthNamesLong[month];
}
}
case"D":
{
day=$$int(date.getDate());
result+=$$private.setValue(day,key);
return result;
}
case"E":
{
day=$$int(date.getDay());
if(key<3)
{
result+=$$private.setValue(day,key);
return result;
}
else if(key==3)
{
return mx.formatters.DateBase.dayNamesShort[day];
}
else
{
return mx.formatters.DateBase.dayNamesLong[day];
}
}
case"A":
{
hours=$$int(date.getHours());
if(hours<12)
return mx.formatters.DateBase.timeOfDay[0];
else
return mx.formatters.DateBase.timeOfDay[1];
}
case"H":
{
hours=$$int(date.getHours());
if(hours==0)
hours=24;
result+=$$private.setValue(hours,key);
return result;
}
case"J":
{
hours=$$int(date.getHours());
result+=$$private.setValue(hours,key);
return result;
}
case"K":
{
hours=$$int(date.getHours());
if(hours>=12)
hours=hours-12;
result+=$$private.setValue(hours,key);
return result;
}
case"L":
{
hours=$$int(date.getHours());
if(hours==0)
hours=12;
else if(hours>12)
hours=hours-12;
result+=$$private.setValue(hours,key);
return result;
}
case"N":
{
var mins=$$int(date.getMinutes());
result+=$$private.setValue(mins,key);
return result;
}
case"S":
{
var sec=$$int(date.getSeconds());
result+=$$private.setValue(sec,key);
return result;
}
case"Q":
{
var ms=$$int(date.getMilliseconds());
result+=$$private.setValue(ms,key);
return result;
}
}
return result;
},
"private static function setValue",function(value,key)
{
var result="";
var vLen=value.toString().length;
if(vLen<key)
{
var n=key-vLen;
for(var i=0;i<n;i++)
{
result+="0";
}
}
result+=value.toString();
return result;
},
"private static function static_resourceManager_changeHandler",function(event)
{
$$private.static_resourcesChanged();
},
];},["dayNamesLong","dayNamesShort","defaultStringKey","monthNamesLong","monthNamesShort","timeOfDay","extractTokenDate"],["mx.resources.ResourceManager","flash.events.Event","int","Number"], "0.8.0", "0.8.4"
);
// class mx.formatters.DateFormatter
joo.classLoader.prepare(
"package mx.formatters",
{ResourceBundle:{$value:"SharedResources"}},
"public class DateFormatter extends mx.formatters.Formatter",2,function($$private){var is=joo.is;return[function(){joo.classLoader.init(mx.formatters.DateBase);},

"mx_internal static const",{VERSION:"4.1.0.16076"},

"private static const",{VALID_PATTERN_CHARS:"Y,M,D,A,E,H,J,K,L,N,S,Q"},
"public static function parseDateString",function(str)
{
if(!str||str=="")
return null;
var year=-1;
var mon=-1;
var day=-1;
var hour=-1;
var min=-1;
var sec=-1;
var letter="";
var marker=0;
var count=0;
var len=str.length;
var timezoneRegEx=/(GMT|UTC)((\+|-)\d\d\d\d )?/ig;
str=str.replace(timezoneRegEx,"");
while(count<len)
{
letter=str.charAt(count);
count++;
if(letter<=" "||letter==",")
continue;
if(letter=="/"||letter==":"||
letter=="+"||letter=="-")
{
marker=letter;
continue;
}
if("a"<=letter&&letter<="z"||
"A"<=letter&&letter<="Z")
{
var word=letter;
while(count<len)
{
letter=str.charAt(count);
if(!("a"<=letter&&letter<="z"||
"A"<=letter&&letter<="Z"))
{
break;
}
word+=letter;
count++;
}
var n=mx.formatters.DateBase.defaultStringKey.length;
for(var i=0;i<n;i++)
{
var s=String(mx.formatters.DateBase.defaultStringKey[i]);
if(s.toLowerCase()==word.toLowerCase()||
s.toLowerCase().substr(0,3)==word.toLowerCase())
{
if(i==13)
{
if(hour>12||hour<1)
break;
else if(hour<12)
hour+=12;
}
else if(i==12)
{
if(hour>12||hour<1)
break;
else if(hour==12)
hour=0;
}
else if(i<12)
{
if(mon<0)
mon=i;
else
break;
}
break;
}
}
marker=0;
}
else if("0"<=letter&&letter<="9")
{
var numbers=letter;
while("0"<=(letter=str.charAt(count))&&
letter<="9"&&
count<len)
{
numbers+=letter;
count++;
}
var num=$$int(numbers);
if(num>=70)
{
if(year!=-1)
{
break;
}
else if(letter<=" "||letter==","||letter=="."||
letter=="/"||letter=="-"||count>=len)
{
year=num;
}
else
{
break;
}
}
else if(letter=="/"||letter=="-"||letter==".")
{
if(mon<0)
mon=(num-1);
else if(day<0)
day=num;
else
break;
}
else if(letter==":")
{
if(hour<0)
hour=num;
else if(min<0)
min=num;
else
break;
}
else if(hour>=0&&min<0)
{
min=num;
}
else if(min>=0&&sec<0)
{
sec=num;
}
else if(day<0)
{
day=num;
}
else if(year<0&&mon>=0&&day>=0)
{
year=2000+num;
}
else
{
break;
}
marker=0;
}
}
if(year<0||mon<0||mon>11||day<1||day>31)
return null;
if(sec<0)
sec=0;
if(min<0)
min=0;
if(hour<0)
hour=0;
var newDate=new Date(year,mon,day,hour,min,sec);
if(day!=newDate.getDate()||mon!=newDate.getMonth())
return null;
return newDate;
},
"public function DateFormatter",function()
{
this.super$2();
},
"private var",{_formatString:null},
"private var",{formatStringOverride:null},
{Inspectable:{category:"General",defaultValue:"null"}},
"public function get formatString",function()
{
return this._formatString$2;
},
"public function set formatString",function(value)
{
this.formatStringOverride$2=value;
this._formatString$2=value!=null?
value:
this.resourceManager.getString(
"SharedResources","dateFormat");
},
"override protected function resourcesChanged",function()
{
this.resourcesChanged$2();
this.formatString=this.formatStringOverride$2;
},
"override public function format",function(value)
{
if(this.error)
this.error=null;
if(!value||(is(value,String)&&value==""))
{
this.error=mx.formatters.Formatter.defaultInvalidValueError;
return"";
}
if(is(value,String))
{
value=mx.formatters.DateFormatter.parseDateString(String(value));
if(!value)
{
this.error=mx.formatters.Formatter.defaultInvalidValueError;
return"";
}
}
else if(!(is(value,Date)))
{
this.error=mx.formatters.Formatter.defaultInvalidValueError;
return"";
}
var letter;
var nTokens=0;
var tokens="";
var n=this.formatString.length;
for(var i=0;i<n;i++)
{
letter=this.formatString.charAt(i);
if($$private.VALID_PATTERN_CHARS.indexOf(letter)!=-1&&letter!=",")
{
nTokens++;
if(tokens.indexOf(letter)==-1)
{
tokens+=letter;
}
else
{
if(letter!=this.formatString.charAt(Math.max(i-1,0)))
{
this.error=mx.formatters.Formatter.defaultInvalidFormatError;
return"";
}
}
}
}
if(nTokens<1)
{
this.error=mx.formatters.Formatter.defaultInvalidFormatError;
return"";
}
var dataFormatter=new mx.formatters.StringFormatter(
this.formatString,$$private.VALID_PATTERN_CHARS,
mx.formatters.DateBase.extractTokenDate);
return dataFormatter.formatValue(value);
},
];},["parseDateString"],["mx.formatters.Formatter","mx.formatters.DateBase","String","int","Date","Math","mx.formatters.StringFormatter"], "0.8.0", "0.8.4"
);
// class mx.formatters.Formatter
joo.classLoader.prepare(
"package mx.formatters",
{ResourceBundle:{$value:"formatters"}},
"public class Formatter",1,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.events.Event);},

"mx_internal static const",{VERSION:"4.1.0.16076"},

"private static var",{initialized:false},
"private static var",{_static_resourceManager:null},
"private static function get static_resourceManager",function()
{
if(!$$private._static_resourceManager)
$$private._static_resourceManager=mx.resources.ResourceManager.getInstance();
return $$private._static_resourceManager;
},
"private static var",{_defaultInvalidFormatError:null},
"private static var",{defaultInvalidFormatErrorOverride:null},
"public static function get defaultInvalidFormatError",function()
{
$$private.initialize();
return $$private._defaultInvalidFormatError;
},
"public static function set defaultInvalidFormatError",function(value)
{
$$private.defaultInvalidFormatErrorOverride=value;
$$private._defaultInvalidFormatError=
value!=null?
value:
$$private.static_resourceManager.getString(
"formatters","defaultInvalidFormatError");
},
"private static var",{_defaultInvalidValueError:null},
"private static var",{defaultInvalidValueErrorOverride:null},
"public static function get defaultInvalidValueError",function()
{
$$private.initialize();
return $$private._defaultInvalidValueError;
},
"public static function set defaultInvalidValueError",function(value)
{
$$private.defaultInvalidValueErrorOverride=value;
$$private._defaultInvalidValueError=
value!=null?
value:
$$private.static_resourceManager.getString(
"formatters","defaultInvalidValueError");
},
"private static function initialize",function()
{
if(!$$private.initialized)
{
$$private.static_resourceManager.addEventListener(
flash.events.Event.CHANGE,$$private.static_resourceManager_changeHandler,
false,0,true);
$$private.static_resourcesChanged();
$$private.initialized=true;
}
},
"private static function static_resourcesChanged",function()
{
mx.formatters.Formatter.defaultInvalidFormatError=$$private.defaultInvalidFormatErrorOverride;
mx.formatters.Formatter.defaultInvalidValueError=$$private.defaultInvalidValueErrorOverride;
},
"private static function static_resourceManager_changeHandler",function(
event)
{
$$private.static_resourcesChanged();
},
"public function Formatter",function()
{this._resourceManager$1=this._resourceManager$1();
this.resourceManager.addEventListener(
flash.events.Event.CHANGE,$$bound(this,"resourceManager_changeHandler$1"),false,0,true);
this.resourcesChanged();
},
{Inspectable:{category:"General",defaultValue:"null"}},
"public var",{error:null},
"private var",{_resourceManager:function(){return(
mx.resources.ResourceManager.getInstance());}},
{Bindable:{$value:"unused"}},
"protected function get resourceManager",function()
{
return this._resourceManager$1;
},
"protected function resourcesChanged",function()
{
},
"public function format",function(value)
{
this.error="This format function is abstract. "+
"Subclasses must override it.";
return"";
},
"private function resourceManager_changeHandler",function(event)
{
this.resourcesChanged();
},
];},["defaultInvalidFormatError","defaultInvalidValueError"],["mx.resources.ResourceManager","flash.events.Event"], "0.8.0", "0.8.4"
);
// class mx.formatters.StringFormatter
joo.classLoader.prepare(
"package mx.formatters",
{ExcludeClass:{}},
"public class StringFormatter",1,function($$private){var $$bound=joo.boundMethod;return[

"mx_internal static const",{VERSION:"4.1.0.16076"},

"public function StringFormatter",function(format,tokens,
extractTokenFunc)
{;
this.formatPattern$1(format,tokens);
this.extractToken$1=extractTokenFunc;
},
"private var",{extractToken:null},
"private var",{reqFormat:null},
"private var",{patternInfo:null},
"public function formatValue",function(value)
{
var curTokenInfo=this.patternInfo$1[0];
var result=this.reqFormat$1.substring(0,curTokenInfo.begin)+
this.extractToken$1(value,curTokenInfo);
var lastTokenInfo=curTokenInfo;
var n=this.patternInfo$1.length;
for(var i=1;i<n;i++)
{
curTokenInfo=this.patternInfo$1[i];
result+=this.reqFormat$1.substring(lastTokenInfo.end,
curTokenInfo.begin)+
this.extractToken$1(value,curTokenInfo);
lastTokenInfo=curTokenInfo;
}
if(lastTokenInfo.end>0&&lastTokenInfo.end!=this.reqFormat$1.length)
result+=this.reqFormat$1.substring(lastTokenInfo.end);
return result;
},
"private function formatPattern",function(format,tokens)
{
var start=0;
var finish=0;
var index=0;
var tokenArray=tokens.split(",");
this.reqFormat$1=format;
this.patternInfo$1=[];
var n=tokenArray.length;
for(var i=0;i<n;i++)
{
start=this.reqFormat$1.indexOf(tokenArray[i]);
if(start>=0&&start<this.reqFormat$1.length)
{
finish=this.reqFormat$1.lastIndexOf(tokenArray[i]);
finish=finish>=0?finish+1:start+1;
this.patternInfo$1.splice(index,0,
{token:tokenArray[i],begin:start,end:finish});
index++;
}
}
this.patternInfo$1.sort($$bound(this,"compareValues$1"));
},
"private function compareValues",function(a,b)
{
if(a.begin>b.begin)
return 1;
else if(a.begin<b.begin)
return-1;
else
return 0;
},
];},[],[], "0.8.0", "0.8.4"
);
// class mx.resources.IResourceManager
joo.classLoader.prepare(
"package mx.resources",
"public interface IResourceManager extends flash.events.IEventDispatcher",1,function($$private){;return[
,,,,,,,,,,,,,
{Bindable:{$value:"change"}},,
{Bindable:{$value:"change"}},,
{Bindable:{$value:"change"}},,
{Bindable:{$value:"change"}},,
{Bindable:{$value:"change"}},,
{Bindable:{$value:"change"}},,
{Bindable:{$value:"change"}},,
{Bindable:{$value:"change"}},,,,
];},[],["flash.events.IEventDispatcher"], "0.8.0", "0.8.4"
);
// class mx.resources.ResourceManager
joo.classLoader.prepare(
"package mx.resources",
"public class ResourceManager",1,function($$private){var is=joo.is;return[

"mx_internal static const",{VERSION:"4.1.0.16076"},

"private static var",{implClassDependency:null},
"private static var",{instance:null},
"public static function getInstance",function()
{
if(!$$private.instance)
{
if(!mx.core.Singleton.getClass("mx.resources::IResourceManager"))
mx.core.Singleton.registerClass("mx.resources::IResourceManager",(flash.utils.getDefinitionByName("mx.resources::ResourceManagerImpl")));
try
{
$$private.instance=(
mx.core.Singleton.getInstance("mx.resources::IResourceManager"));
}
catch(e){if(is(e,Error))
{
$$private.instance=null;
}else throw e;}
}
return $$private.instance;
},
"public function ResourceManager",function()
{;
},
];},["getInstance"],["mx.core.Singleton","Class","mx.resources.IResourceManager","Error"], "0.8.0", "0.8.4"
);
// class mx.utils.Base64Decoder
joo.classLoader.prepare(
"package mx.utils",
{ResourceBundle:{$value:"utils"}},
"public class Base64Decoder",1,function($$private){;return[

"public function Base64Decoder",function()
{this.work$1=this.work$1();this.resourceManager$1=this.resourceManager$1();
this.data$1=new flash.utils.ByteArray();
},
"public function decode",function(encoded)
{
for(var i=0;i<encoded.length;++i)
{
var c=encoded.charCodeAt(i);
if(c==$$private.ESCAPE_CHAR_CODE)
this.work$1[this.count$1++]=-1;
else if($$private.inverse[c]!=64)
this.work$1[this.count$1++]=$$private.inverse[c];
else
continue;
if(this.count$1==4)
{
this.count$1=0;
this.data$1.writeByte((this.work$1[0]<<2)|((this.work$1[1]&0xFF)>>4));
this.filled$1++;
if(this.work$1[2]==-1)
break;
this.data$1.writeByte((this.work$1[1]<<4)|((this.work$1[2]&0xFF)>>2));
this.filled$1++;
if(this.work$1[3]==-1)
break;
this.data$1.writeByte((this.work$1[2]<<6)|this.work$1[3]);
this.filled$1++;
}
}
},
"public function drain",function()
{
var result=new flash.utils.ByteArray();
$$private.copyByteArray(this.data$1,result,this.filled$1);
this.filled$1=0;
return result;
},
"public function flush",function()
{
if(this.count$1>0)
{
var message=this.resourceManager$1.getString("utils","partialBlockDropped",[this.count$1]);
throw new Error(message);
}
return this.drain();
},
"public function reset",function()
{
this.data$1=new flash.utils.ByteArray();
this.count$1=0;
this.filled$1=0;
},
"public function toByteArray",function()
{
var result=this.flush();
this.reset();
return result;
},
"private static function copyByteArray",function(source,destination,length)
{if(arguments.length<3){length=0;}
var oldPosition=source.position;
source.position=0;
destination.position=0;
var i=0;
while(source.bytesAvailable>0&&i<length)
{
destination.writeByte(source.readByte());
i++;
}
source.position=oldPosition;
destination.position=0;
},
"private var",{count:0},
"private var",{data:null},
"private var",{filled:0},
"private var",{work:function(){return([0,0,0,0]);}},
"private var",{resourceManager:function(){return(
mx.resources.ResourceManager.getInstance());}},
"private static const",{ESCAPE_CHAR_CODE:61},
"private static const",{inverse:function(){return(
[
64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,
64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,
64,64,64,64,64,64,64,64,64,64,64,62,64,64,64,63,
52,53,54,55,56,57,58,59,60,61,64,64,64,64,64,64,
64,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,
15,16,17,18,19,20,21,22,23,24,25,64,64,64,64,64,
64,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,
41,42,43,44,45,46,47,48,49,50,51,64,64,64,64,64,
64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,
64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,
64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,
64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,
64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,
64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,
64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,
64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64
]);}},
];},[],["flash.utils.ByteArray","Error","mx.resources.ResourceManager"], "0.8.0", "0.8.4"
);
// class mx.utils.Base64Encoder
joo.classLoader.prepare(
"package mx.utils",
"public class Base64Encoder",1,function($$private){var as=joo.as;return[

"public static const",{CHARSET_UTF_8:"UTF-8"},
"public static var",{newLine:10},
"public function Base64Encoder",function()
{this._work$1=this._work$1();
this.reset();
},
"public var",{insertNewLines:true},
"public function drain",function()
{
var result="";
for(var i=0;i<this._buffers$1.length;i++)
{
var buffer=as(this._buffers$1[i],Array);
result+=String.fromCharCode.apply(null,buffer);
}
this._buffers$1=[];
this._buffers$1.push([]);
return result;
},
"public function encode",function(data,offset,length)
{if(arguments.length<3){if(arguments.length<2){offset=0;}length=0;}
if(length==0)
length=data.length;
var currentIndex=offset;
var endIndex=offset+length;
if(endIndex>data.length)
endIndex=data.length;
while(currentIndex<endIndex)
{
this._work$1[this._count$1]=data.charCodeAt(currentIndex);
this._count$1++;
if(this._count$1==this._work$1.length||endIndex-currentIndex==1)
{
this.encodeBlock$1();
this._count$1=0;
this._work$1[0]=0;
this._work$1[1]=0;
this._work$1[2]=0;
}
currentIndex++;
}
},
"public function encodeUTFBytes",function(data)
{
var bytes=new flash.utils.ByteArray();
bytes.writeUTFBytes(data);
bytes.position=0;
this.encodeBytes(bytes);
},
"public function encodeBytes",function(data,offset,length)
{if(arguments.length<3){if(arguments.length<2){offset=0;}length=0;}
if(length==0)
length=data.length;
var oldPosition=data.position;
data.position=offset;
var currentIndex=offset;
var endIndex=offset+length;
if(endIndex>data.length)
endIndex=data.length;
while(currentIndex<endIndex)
{
this._work$1[this._count$1]=data[currentIndex];
this._count$1++;
if(this._count$1==this._work$1.length||endIndex-currentIndex==1)
{
this.encodeBlock$1();
this._count$1=0;
this._work$1[0]=0;
this._work$1[1]=0;
this._work$1[2]=0;
}
currentIndex++;
}
data.position=oldPosition;
},
"public function flush",function()
{
if(this._count$1>0)
this.encodeBlock$1();
var result=this.drain();
this.reset();
return result;
},
"public function reset",function()
{
this._buffers$1=[];
this._buffers$1.push([]);
this._count$1=0;
this._line$1=0;
this._work$1[0]=0;
this._work$1[1]=0;
this._work$1[2]=0;
},
"public function toString",function()
{
return this.flush();
},
"private function encodeBlock",function()
{
var currentBuffer=as(this._buffers$1[this._buffers$1.length-1],Array);
if(currentBuffer.length>=mx.utils.Base64Encoder.MAX_BUFFER_SIZE)
{
currentBuffer=[];
this._buffers$1.push(currentBuffer);
}
currentBuffer.push($$private.ALPHABET_CHAR_CODES[(this._work$1[0]&0xFF)>>2]);
currentBuffer.push($$private.ALPHABET_CHAR_CODES[((this._work$1[0]&0x03)<<4)|((this._work$1[1]&0xF0)>>4)]);
if(this._count$1>1)
currentBuffer.push($$private.ALPHABET_CHAR_CODES[((this._work$1[1]&0x0F)<<2)|((this._work$1[2]&0xC0)>>6)]);
else
currentBuffer.push($$private.ESCAPE_CHAR_CODE);
if(this._count$1>2)
currentBuffer.push($$private.ALPHABET_CHAR_CODES[this._work$1[2]&0x3F]);
else
currentBuffer.push($$private.ESCAPE_CHAR_CODE);
if(this.insertNewLines)
{
if((this._line$1+=4)==76)
{
currentBuffer.push(mx.utils.Base64Encoder.newLine);
this._line$1=0;
}
}
},
"private var",{_buffers:null},
"private var",{_count:0},
"private var",{_line:0},
"private var",{_work:function(){return([0,0,0]);}},
"public static const",{MAX_BUFFER_SIZE:32767},
"private static const",{ESCAPE_CHAR_CODE:61},
"private static const",{ALPHABET_CHAR_CODES:function(){return(
[
65,66,67,68,69,70,71,72,
73,74,75,76,77,78,79,80,
81,82,83,84,85,86,87,88,
89,90,97,98,99,100,101,102,
103,104,105,106,107,108,109,110,
111,112,113,114,115,116,117,118,
119,120,121,122,48,49,50,51,
52,53,54,55,56,57,43,47
]);}},
];},[],["Array","String","flash.utils.ByteArray"], "0.8.0", "0.8.4"
);
// class NumberFormat
joo.classLoader.prepare("package",
"public class NumberFormat",1,function($$private){;return[

"public static var",{DEFAULT_NUM_DECIMALS:2},
"public var",{numDecimals:function(){return(NumberFormat.DEFAULT_NUM_DECIMALS);}},
"public var",{isFixedNumDecimalsForced:false},
"public var",{isDecimalSeparatorComma:false},
"public var",{isThousandSeparatorDisabled:false},
"public function NumberFormat",function(numDecimals,isFixedNumDecimalsForced,isDecimalSeparatorComma,isThousandSeparatorDisabled)
{this.numDecimals=this.numDecimals();
this.numDecimals=util.Parser.getNumberValue(numDecimals,NumberFormat.DEFAULT_NUM_DECIMALS,true,false);
this.isFixedNumDecimalsForced=util.Parser.getBooleanValue(isFixedNumDecimalsForced,false);
this.isDecimalSeparatorComma=util.Parser.getBooleanValue(isDecimalSeparatorComma,false);
this.isThousandSeparatorDisabled=util.Parser.getBooleanValue(isThousandSeparatorDisabled,false);
},
"private static var",{_instance:null},
"public static function getInstance",function(json){
if($$private._instance==null){
var o={
num_decimals:2,
is_fixed_num_decimals_forced:0,
is_decimal_separator_comma:0,
is_thousand_separator_disabled:0
};
object_helper.merge_2(json,o);
$$private._instance=new NumberFormat(
o.num_decimals,
o.is_fixed_num_decimals_forced==1,
o.is_decimal_separator_comma==1,
o.is_thousand_separator_disabled==1
);
}else{
}
return $$private._instance;
},
"private static var",{_instanceY2:null},
"public static function getInstanceY2",function(json){
if($$private._instanceY2==null){
var o={
num_decimals:2,
is_fixed_num_decimals_forced:0,
is_decimal_separator_comma:0,
is_thousand_separator_disabled:0
};
object_helper.merge_2(json,o);
$$private._instanceY2=new NumberFormat(
o.num_decimals,
o.is_fixed_num_decimals_forced==1,
o.is_decimal_separator_comma==1,
o.is_thousand_separator_disabled==1
);
}else{
}
return $$private._instanceY2;
},
];},["getInstance","getInstanceY2"],["util.Parser","object_helper"], "0.8.0", "0.8.4"
);
// class NumberUtils
joo.classLoader.prepare("package",
"public class NumberUtils",1,function($$private){;return[
"public static function formatNumber",function(i){
var format=NumberFormat.getInstance(null);
return NumberUtils.format(i,
format.numDecimals,
format.isFixedNumDecimalsForced,
format.isDecimalSeparatorComma,
format.isThousandSeparatorDisabled
);
},
"public static function formatNumberY2",function(i){
var format=NumberFormat.getInstanceY2(null);
return NumberUtils.format(i,
format.numDecimals,
format.isFixedNumDecimalsForced,
format.isDecimalSeparatorComma,
format.isThousandSeparatorDisabled
);
},
"public static function format",function(
i,
numDecimals,
isFixedNumDecimalsForced,
isDecimalSeparatorComma,
isThousandSeparatorDisabled
){
if(isNaN(numDecimals)){
numDecimals=4;
}
i=Math.round(i*Math.pow(10,numDecimals))/Math.pow(10,numDecimals);
var s='';
var num;
if(i<0)
num=String(-i).split('.');
else
num=String(i).split('.');
var x=num[0];
var pos=0;
var c=0;
for(c=x.length-1;c>-1;c--)
{
if(pos%3==0&&s.length>0)
{
s=','+s;
pos=0;
}
pos++;
s=x.substr(c,1)+s;
}
if(num[1]!=undefined){
if(isFixedNumDecimalsForced){
num[1]+="0000000000000000";
}
s+='.'+num[1].substr(0,numDecimals);
}else{
if(isFixedNumDecimalsForced&&numDecimals>0){
num[1]="0000000000000000";
s+='.'+num[1].substr(0,numDecimals);
}
}
if(i<0)
s='-'+s;
if(isThousandSeparatorDisabled){
s=s.replace(",","");
}
if(isDecimalSeparatorComma){
s=NumberUtils.toDecimalSeperatorComma(s);
}
return s;
},
"public static function toDecimalSeperatorComma",function(value){
return value
.replace(".","|")
.replace(",",".")
.replace("|",",");
},
];},["formatNumber","formatNumberY2","format","toDecimalSeperatorComma"],["NumberFormat","Math","String"], "0.8.0", "0.8.4"
);
// class object_helper
joo.classLoader.prepare("package",
"public class object_helper",1,function($$private){;return[
"public static function merge",function(o,defaults){
for(var prop in defaults){
if(o[prop]==undefined)
o[prop]=defaults[prop];
}
return o;
},
"public static function merge_2",function(json,defaults){
for(var prop in json){
defaults[prop]=json[prop];
}
},
];},["merge","merge_2"],[], "0.8.0", "0.8.4"
);
// class PointHLC
joo.classLoader.prepare("package",
"public class PointHLC",1,function($$private){;return[

"public var",{width:NaN},
"public var",{bar_bottom:NaN},
"public var",{high:NaN},
"public var",{close:NaN},
"public var",{low:NaN},
"public function PointHLC",function(x,high,close,low,tooltip,width){
this.width=width;
this.high=high;
this.close=close;
this.low=low;
},
"public function make_tooltip",function(
tip,key,val,x_legend,
x_axis_label,tip_set){
this.make_tooltip(tip,key,val,x_legend,x_axis_label,tip_set);
},
"public function get_tip_pos",function(){
return null;
},
];},[],[], "0.8.0", "0.8.4"
);
// class Properties
joo.classLoader.prepare("package",
"public class Properties extends Object",1,function($$private){;return[function(){joo.classLoader.init(Number);},

"private var",{_props:null},
"private var",{_parent:null},
"public function Properties",function(json,parent){if(arguments.length<2){parent=null;}
this._props$1=new flash.utils.Dictionary();
this._parent$1=parent;
for(var prop in json){
this._props$1[prop]=json[prop];
}
},
"public function get",function(name){
if(name in this._props$1)
return this._props$1[name];
if(this._parent$1!=null)
return this._parent$1.get(name);
var e=new Error();
var str=e.stack;
tr.aces('ERROR: property not found',name,str);
return Number.NEGATIVE_INFINITY;
},
"public function get_colour",function(name){
return string.Utils.get_colour(this.get(name));
},
"public function set",function(name,value){
this._props$1[name]=value;
},
"public function has",function(name){
if(this._props$1[name]==null){
if(this._parent$1!=null)
return this._parent$1.has(name);
else
return false;
}
else
return true;
},
"public function set_parent",function(p){
if(this._parent$1!=null)
p.set_parent(this._parent$1);
this._parent$1=p;
},
"public function die",function(){
if(this._parent$1)
this._parent$1.die();
for(var key in this._props$1){
this._props$1[key]=null;
}
this._parent$1=null;
},
];},[],["Object","flash.utils.Dictionary","Error","tr","Number","string.Utils"], "0.8.0", "0.8.4"
);
// class ScreenCoords
joo.classLoader.prepare("package",
"public class ScreenCoords extends ScreenCoordsBase",2,function($$private){;return[

"private var",{x_range:null},
"private var",{y_range:null},
"private var",{y_right_range:null},
"public var",{tick_offset:NaN},
"private var",{x_offset:false},
"private var",{y_offset:false},
"private var",{bar_groups:NaN},
"public function ScreenCoords",function(top,left,right,bottom,
y_axis_range,
y_axis_right_range,
x_axis_range,
x_left_label_width,x_right_label_width,
three_d)
{
this.super$2(top,left,right,bottom);
var tmp_left=left;
this.x_range$2=x_axis_range;
this.y_range$2=y_axis_range;
this.y_right_range$2=y_axis_right_range;
if(this.x_range$2){
right=this.jiggle(left,right,x_right_label_width,x_axis_range.count());
tmp_left=this.shrink_left(left,right,x_left_label_width,x_axis_range.count());
}
this.top=top;
this.left=Math.max(left,tmp_left);
this.right=Math.floor(right);
this.bottom=bottom;
this.width=this.right-this.left;
this.height=bottom-top;
if(three_d)
{
this.tick_offset=12;
}
else
this.tick_offset=0;
if(x_axis_range){
this.x_offset$2=x_axis_range.offset;
}
if(y_axis_range){
this.y_offset$2=y_axis_range.offset;
}
this.bar_groups$2=1;
},
"public function jiggle_original",function(left,right,x_label_width,count){
var r=0;
if(x_label_width!=0)
{
var item_width=(right-left)/count;
r=right-(item_width/2);
var new_right=right;
while(r+(x_label_width/2)>right)
{
new_right-=1;
item_width=(new_right-left)/count;
r=new_right-(item_width/2);
}
right=new_right;
}
return right;
},
"public function jiggle",function(left,right,x_label_width,count){
return right-(x_label_width/2);
},
"public function shrink_left",function(left,right,x_label_width,count){
var pos=0;
if(x_label_width!=0)
{
var item_width=(right-left)/count;
pos=left+(item_width/2);
var new_left=left;
while(pos-(x_label_width/2)<0)
{
new_left+=1;
item_width=(right-new_left)/count;
pos=new_left+(item_width/2);
}
left=new_left;
}
return left;
},
"public override function get_y_bottom",function(right_axis)
{if(arguments.length<1){right_axis=false;}
var r=right_axis?this.y_right_range$2:this.y_range$2;
var min=r.min;
var max=r.max;
min=Math.min(min,max);
return this.get_y_from_val(Math.max(0,min),right_axis);
},
"public function getY_old",function(i,right_axis)
{
var r=right_axis?this.y_right_range$2:this.y_range$2;
var steps=this.height/(r.count());
var y=this.bottom-(steps*(r.min*-1));
y-=i*steps;
return y;
},
"public override function get_y_from_val",function(i,right_axis){if(arguments.length<2){right_axis=false;}
var r=right_axis?this.y_right_range$2:this.y_range$2;
var steps=this.height/r.count();
var tmp=0;
if(this.y_offset$2)
tmp=(steps/2);
return this.bottom-tmp-(r.min-i)*steps*-1;
},
"public override function get_get_x_from_pos_and_y_from_val",function(index,y,right_axis){if(arguments.length<3){right_axis=false;}
return new flash.geom.Point(
this.get_x_from_pos(index),
this.get_y_from_val(y,right_axis));
},
"public function width_",function()
{
return this.right-this.left_$2();
},
"private function left_",function()
{
var padding_left=this.tick_offset;
return this.left+padding_left;
},
"public override function get_x_from_val",function(i){
var rev=this.x_range$2.min>this.x_range$2.max;
var count=this.x_range$2.count();
count+=(rev&&this.x_range$2.offset)?-2:0;
var item_width=this.width_()/count;
var pos=i-this.x_range$2.min;
var tmp=0;
if(this.x_offset$2)
tmp=Math.abs(item_width/2);
return this.left_$2()+tmp+(pos*item_width);
},
"public override function get_x_from_pos",function(i){
var rev=this.x_range$2.min>this.x_range$2.max;
var count=this.x_range$2.count();
count+=(rev&&this.x_range$2.offset)?-2:0;
var item_width=Math.abs(this.width_()/count);
var tmp=0;
if(this.x_offset$2)
tmp=(item_width/2);
return this.left_$2()+tmp+(i*item_width);
},
"public function get_x_tick_pos",function(i)
{
return this.get_x_from_pos(i)-this.tick_offset;
},
"public function set_bar_groups",function(n){
this.bar_groups$2=n;
},
"public function get_bar_coords",function(index,group){
var item_width=this.width_()/this.x_range$2.count();
var bar_set_width=item_width*0.8;
var tmp=0;
if(this.x_offset$2)
tmp=item_width;
var bar_width=bar_set_width/this.bar_groups$2;
var bar_left=this.left_$2()+((tmp-bar_set_width)/2);
var left=bar_left+(index*item_width);
left+=bar_width*group;
return{x:left,width:bar_width};
},
"public function get_horiz_bar_coords",function(index,group){
var bar_width=this.height/this.y_range$2.count();
var bar_set_width=bar_width*0.8;
var group_width=bar_set_width/this.bar_groups$2;
var bar_top=this.top+((bar_width-bar_set_width)/2);
var top=bar_top+(index*bar_width);
top+=group_width*group;
return{y:top,width:group_width};
},
"public function makePointHLC",function(x,high,close,low,right_axis,group,group_count){
var item_width=this.width_()/this.x_range$2.count();
var bar_set_width=item_width*1;
var bar_left=this.left_$2()+((item_width-bar_set_width)/2);
var bar_width=bar_set_width/group_count;
var left=bar_left+(x*item_width);
left+=bar_width*group;
return new PointHLC(
left,
this.get_y_from_val(high,right_axis),
this.get_y_from_val(close,right_axis),
this.get_y_from_val(low,right_axis),
high,
bar_width
);
},
];},[],["ScreenCoordsBase","Math","flash.geom.Point","PointHLC"], "0.8.0", "0.8.4"
);
// class ScreenCoordsBase
joo.classLoader.prepare("package",
"public class ScreenCoordsBase",1,function($$private){;return[

"public var",{top:NaN},
"public var",{left:NaN},
"public var",{right:NaN},
"public var",{bottom:NaN},
"public var",{width:NaN},
"public var",{height:NaN},
"public function ScreenCoordsBase",function(top,left,right,bottom){
this.top=top;
this.left=left;
this.right=right;
this.bottom=bottom;
this.width=this.right-this.left;
this.height=bottom-top;
},
"public function get_center_x",function(){
return(this.width/2)+this.left;
},
"public function get_center_y",function(){
return(this.height/2)+this.top;
},
"public function get_y_from_val",function(i,right_axis){if(arguments.length<2){right_axis=false;}return-1;},
"public function get_x_from_val",function(i){return-1;},
"public function get_get_x_from_pos_and_y_from_val",function(index,y,right_axis){if(arguments.length<3){right_axis=false;}
return null;
},
"public function get_y_bottom",function(right_axis){if(arguments.length<1){right_axis=false;}
return-1;
},
"public function get_x_from_pos",function(i){return-1;},
];},[],[], "0.8.0", "0.8.4"
);
// class ScreenCoordsRadar
joo.classLoader.prepare("package",
"public class ScreenCoordsRadar extends ScreenCoordsBase",2,function($$private){;return[function(){joo.classLoader.init(Math);},

"private var",{TO_RADIANS:function(){return(Math.PI/180);}},
"private var",{range:null},
"private var",{angles:NaN},
"private var",{angle:NaN},
"private var",{radius:NaN},
"public function ScreenCoordsRadar",function(top,left,right,bottom){
this.super$2(top,left,right,bottom);this.TO_RADIANS$2=this.TO_RADIANS$2();
this.radius$2=(Math.min(this.width,this.height)/2.0);
},
"public function set_range",function(r){
this.range$2=r;
},
"public function get_max",function(){
return this.range$2.max;
},
"public function set_angles",function(a){
this.angles$2=a;
this.angle$2=360/a;
},
"public function get_angles",function(){
return this.angles$2;
},
"public function get_radius",function(){
return this.radius$2;
},
"public function reduce_radius",function(){
this.radius$2--;
},
"public function get_pos",function(angle,radius){
var a=(angle-90)*this.TO_RADIANS$2;
var r=this.get_radius()*radius;
var p=new flash.geom.Point(
r*Math.cos(a),
r*Math.sin(a));
return p;
},
"public override function get_get_x_from_pos_and_y_from_val",function(index,y,right_axis){if(arguments.length<3){right_axis=false;}
var p=this.get_pos(this.angle$2*index,y/this.range$2.count());
p.x+=this.get_center_x();
p.y+=this.get_center_y();
return p;
},
"public override function get_y_from_val",function(y,right_axis){if(arguments.length<2){right_axis=false;}
var p=this.get_pos(0,y/this.range$2.count());
p.y+=this.get_center_y();
return p.y;
},
];},[],["ScreenCoordsBase","Math","flash.geom.Point"], "0.8.0", "0.8.4"
);
// class ShowTipEvent
joo.classLoader.prepare("package",
"public class ShowTipEvent extends flash.events.Event",2,function($$private){;return[
"public static const",{SHOW_TIP_TYPE:"ShowTipEvent"},
"public var",{pos:NaN},
"public function ShowTipEvent",function(pos){
this.super$2(ShowTipEvent.SHOW_TIP_TYPE);
this.pos=pos;
},
];},[],["flash.events.Event"], "0.8.0", "0.8.4"
);
// class string.Css
joo.classLoader.prepare("package string",
"public class Css",1,function($$private){;return[
"public var",{text_align:null},
"public var",{font_size:NaN},
"private var",{text_decoration:null},
"private var",{margin:null},
"public var",{margin_top:NaN},
"public var",{margin_bottom:NaN},
"public var",{margin_left:NaN},
"public var",{margin_right:NaN},
"private var",{padding:null},
"public var",{padding_top:0},
"public var",{padding_bottom:0},
"public var",{padding_left:0},
"public var",{padding_right:0},
"public var",{font_weight:null},
"public var",{font_style:null},
"public var",{font_family:null},
"public var",{color:NaN},
"private var",{stop_process:NaN},
"public var",{background_colour:NaN},
"public var",{background_colour_set:false},
"private var",{display:null},
"public function Css",function(txt){
txt.toLowerCase();
txt=txt.replace('{','');
txt=txt.replace('}','');
this.margin_top=0;
this.margin_bottom=0;
this.margin_left=0;
this.margin_right=0;
this.padding_top=0;
this.padding_bottom=0;
this.padding_left=0;
this.padding_right=0;
this.color=0;
this.background_colour_set=false;
this.font_size=9;
var arr=txt.split(";");
for(var i=0;i<arr.length;i++)
{
this.getAttribute$1(arr[i]);
}
},
"private function trim",function(txt){
var l=0;
var r=txt.length-1;
while(txt.charAt(l)==' '||txt.charAt(l)=="\t")l++;
while(txt.charAt(r)==' '||txt.charAt(r)=="\t")r--;
return txt.substring(l,r+1);
},
"private function removeDoubleSpaces",function(txt){
var aux;
var auxPrev;
aux=txt;
do{
auxPrev=aux;
aux.replace('  ',' ');
}while(auxPrev.length!=aux.length);
return aux;
},
"private function ToNumber",function(cad){
cad=cad.replace('px','');
if(isNaN(Number(cad))){
return 0;
}else{
return Number(cad);
}
},
"private function getAttribute",function(txt){
var arr=txt.split(":");
if(arr.length==2)
{
this.stop_process$1=1;
this.set(arr[0],this.trim$1(arr[1]));
}
},
"public function set",function(cad,val){
cad=this.trim$1(cad);
switch(cad)
{
case"text-align":this.text_align=val;break;
case"font-size":this.set_font_size(val);break;
case"text-decoration":this.text_decoration$1=val;break;
case"margin":this.setMargin$1(val);break;
case"margin-top":this.margin_top=this.ToNumber$1(val);break;
case"margin-bottom":this.margin_bottom=this.ToNumber$1(val);break;
case"margin-left":this.margin_left=this.ToNumber$1(val);break;
case"margin-right":this.margin_right=this.ToNumber$1(val);break;
case'padding':this.setPadding$1(val);break;
case"padding-top":this.padding_top=this.ToNumber$1(val);break;
case"padding-bottom":this.padding_bottom=this.ToNumber$1(val);break;
case"padding-left":this.padding_left=this.ToNumber$1(val);break;
case"padding-right":this.padding_right=this.ToNumber$1(val);break;
case"font-weight":this.font_weight=val;break;
case"font-style":this.font_style=val;break;
case"font-family":this.font_family=val;break;
case"color":this.set_color(val);break;
case"background-color":
this.background_colour=string.Utils.get_colour(val);
this.background_colour_set=true;
break;
case"display":this.display$1=val;break;
}
},
"public function set_color",function(val){
this.color=string.Utils.get_colour(val);
},
"public function set_font_size",function(val){
this.font_size=this.ToNumber$1(val);
},
"private function setPadding",function(val){
val=this.trim$1(val);
var arr=val.split(' ');
switch(arr.length)
{
case 1:
this.padding_top=this.ToNumber$1(arr[0]);
this.padding_right=this.ToNumber$1(arr[0]);
this.padding_bottom=this.ToNumber$1(arr[0]);
this.padding_left=this.ToNumber$1(arr[0]);
break;
case 2:
this.padding_top=this.ToNumber$1(arr[0]);
this.padding_right=this.ToNumber$1(arr[1]);
this.padding_bottom=this.ToNumber$1(arr[0]);
this.padding_left=this.ToNumber$1(arr[1]);
break;
case 3:
this.padding_top=this.ToNumber$1(arr[0]);
this.padding_right=this.ToNumber$1(arr[1]);
this.padding_bottom=this.ToNumber$1(arr[2]);
this.padding_left=this.ToNumber$1(arr[1]);
break;
default:
this.padding_top=this.ToNumber$1(arr[0]);
this.padding_right=this.ToNumber$1(arr[1]);
this.padding_bottom=this.ToNumber$1(arr[2]);
this.padding_left=this.ToNumber$1(arr[3]);
}
},
"private function setMargin",function(val){
val=this.trim$1(val);
var arr=val.split(' ');
switch(arr.length)
{
case 1:
this.margin_top=this.ToNumber$1(arr[0]);
this.margin_right=this.ToNumber$1(arr[0]);
this.margin_bottom=this.ToNumber$1(arr[0]);
this.margin_left=this.ToNumber$1(arr[0]);
break;
case 2:
this.margin_top=this.ToNumber$1(arr[0]);
this.margin_right=this.ToNumber$1(arr[1]);
this.margin_bottom=this.ToNumber$1(arr[0]);
this.margin_left=this.ToNumber$1(arr[1]);
break;
case 3:
this.margin_top=this.ToNumber$1(arr[0]);
this.margin_right=this.ToNumber$1(arr[1]);
this.margin_bottom=this.ToNumber$1(arr[2]);
this.margin_left=this.ToNumber$1(arr[1]);
break;
default:
this.margin_top=this.ToNumber$1(arr[0]);
this.margin_right=this.ToNumber$1(arr[1]);
this.margin_bottom=this.ToNumber$1(arr[2]);
this.margin_left=this.ToNumber$1(arr[3]);
}
},
"public function clear",function(){
this.text_align=undefined;
this.font_size=undefined;
this.text_decoration$1=undefined;
this.margin_top=undefined;
this.margin_bottom=undefined;
this.margin_left=undefined;
this.margin_right=undefined;
this.font_weight=undefined;
this.font_style=undefined;
this.font_family=undefined;
this.color=undefined;
this.display$1=undefined;
},
];},[],["Number","string.Utils"], "0.8.0", "0.8.4"
);
// class string.DateUtils
joo.classLoader.prepare("package string",
"public class DateUtils",1,function($$private){;return[

"protected static var",{dateConsts:function(){return({
shortMonths:['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'],
longMonths:['January','February','March','April','May','June','July','August','September','October','November','December'],
shortDays:['Sun','Mon','Tue','Wed','Thu','Fri','Sat'],
longDays:['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday']
});}},
"public static function replace_magic_values",function(tip,xVal){
var as3Date=new Date(xVal*1000);
tip=tip.replace('#date#',string.DateUtils.formatDate(as3Date,"Y-m-d"));
var begPtr=tip.indexOf("#date:");
while(begPtr>=0)
{
var endPtr=tip.indexOf("#",begPtr+1)+1;
var replaceStr=tip.substr(begPtr,endPtr-begPtr);
var timeFmt=replaceStr.substr(6,replaceStr.length-7);
var dateStr=string.DateUtils.formatDate(as3Date,timeFmt);
tip=tip.replace(replaceStr,dateStr);
begPtr=tip.indexOf("#date:");
}
begPtr=tip.indexOf("#gmdate:");
while(begPtr>=0)
{
endPtr=tip.indexOf("#",begPtr+1)+1;
replaceStr=tip.substr(begPtr,endPtr-begPtr);
timeFmt=replaceStr.substr(8,replaceStr.length-9);
dateStr=string.DateUtils.formatUTCDate(as3Date,timeFmt);
tip=tip.replace(replaceStr,dateStr);
begPtr=tip.indexOf("#gmdate:");
}
return tip;
},
"public static function formatDate",function(aDate,fmt)
{
var returnStr='';
for(var i=0;i<fmt.length;i++){
var curChar=fmt.charAt(i);
switch(curChar)
{
case'd':
returnStr+=(aDate.getDate()<10?'0':'')+aDate.getDate();
break;
case'D':
returnStr+=string.DateUtils.dateConsts.shortDays[aDate.getDate()];
break;
case'j':
returnStr+=aDate.getDate();
break;
case'l':
returnStr+=string.DateUtils.dateConsts.longDays[aDate.getDay()];
break;
case'N':
returnStr+=aDate.getDay()+1;
break;
case'S':
returnStr+=(aDate.getDate()%10==1&&aDate.getDate()!=11?'st':(aDate.getDate()%10==2&&aDate.getDate()!=12?'nd':(aDate.getDate()%10==3&&aDate.getDate()!=13?'rd':'th')));
break;
case'w':
returnStr+=aDate.getDay();
break;
case'F':
returnStr+=string.DateUtils.dateConsts.longMonths[aDate.getMonth()];
break;
case'm':
returnStr+=(aDate.getMonth()<9?'0':'')+(aDate.getMonth()+1);
break;
case'M':
returnStr+=string.DateUtils.dateConsts.shortMonths[aDate.getMonth()];
break;
case'n':
returnStr+=aDate.getMonth()+1;
break;
case'Y':
returnStr+=aDate.getFullYear();
break;
case'y':
returnStr+=(''+aDate.getFullYear()).substr(2);
break;
case'a':
returnStr+=aDate.getHours()<12?'am':'pm';
break;
case'A':
returnStr+=aDate.getHours()<12?'AM':'PM';
break;
case'g':
returnStr+=aDate.getHours()==0?12:(aDate.getHours()>12?aDate.getHours()-12:aDate.getHours());
break;
case'G':
returnStr+=aDate.getHours();
break;
case'h':
returnStr+=(aDate.getHours()<10||(12<aDate.getHours()<22)?'0':'')+(aDate.getHours()<10?aDate.getHours()+1:aDate.getHours()-12);
break;
case'H':
returnStr+=(aDate.getHours()<10?'0':'')+aDate.getHours();
break;
case'i':
returnStr+=(aDate.getMinutes()<10?'0':'')+aDate.getMinutes();
break;
case's':
returnStr+=(aDate.getSeconds()<10?'0':'')+aDate.getSeconds();
break;
case'O':
returnStr+=(aDate.getTimezoneOffset()<0?'-':'+')+(aDate.getTimezoneOffset()/60<10?'0':'')+(aDate.getTimezoneOffset()/60)+'00';
break;
case'Z':
returnStr+=aDate.getTimezoneOffset()*60;
break;
case'r':
returnStr+=aDate.toString();
break;
case'U':
returnStr+=aDate.getTime()/1000;
break;
default:
returnStr+=curChar;
}
}
return returnStr;
},
"public static function formatUTCDate",function(aDate,fmt)
{
var returnStr='';
for(var i=0;i<fmt.length;i++){
var curChar=fmt.charAt(i);
switch(curChar)
{
case'd':
returnStr+=(aDate.getUTCDate()<10?'0':'')+aDate.getUTCDate();
break;
case'D':
returnStr+=string.DateUtils.dateConsts.shortDays[aDate.getUTCDate()];
break;
case'j':
returnStr+=aDate.getUTCDate();
break;
case'l':
returnStr+=string.DateUtils.dateConsts.longDays[aDate.getUTCDay()];
break;
case'N':
returnStr+=aDate.getUTCDay()+1;
break;
case'S':
returnStr+=(aDate.getUTCDate()%10==1&&aDate.getUTCDate()!=11?'st':(aDate.getUTCDate()%10==2&&aDate.getUTCDate()!=12?'nd':(aDate.getUTCDate()%10==3&&aDate.getUTCDate()!=13?'rd':'th')));
break;
case'w':
returnStr+=aDate.getUTCDay();
break;
case'F':
returnStr+=string.DateUtils.dateConsts.longMonths[aDate.getUTCMonth()];
break;
case'm':
returnStr+=(aDate.getUTCMonth()<9?'0':'')+(aDate.getUTCMonth()+1);
break;
case'M':
returnStr+=string.DateUtils.dateConsts.shortMonths[aDate.getUTCMonth()];
break;
case'n':
returnStr+=aDate.getUTCMonth()+1;
break;
case'Y':
returnStr+=aDate.getUTCFullYear();
break;
case'y':
returnStr+=(''+aDate.getUTCFullYear()).substr(2);
break;
case'a':
returnStr+=aDate.getUTCHours()<12?'am':'pm';
break;
case'A':
returnStr+=aDate.getUTCHours()<12?'AM':'PM';
break;
case'g':
returnStr+=aDate.getUTCHours()==0?12:(aDate.getUTCHours()>12?aDate.getUTCHours()-12:aDate.getHours());
break;
case'G':
returnStr+=aDate.getUTCHours();
break;
case'h':
returnStr+=(aDate.getUTCHours()<10||(12<aDate.getUTCHours()<22)?'0':'')+(aDate.getUTCHours()<10?aDate.getUTCHours()+1:aDate.getUTCHours()-12);
break;
case'H':
returnStr+=(aDate.getUTCHours()<10?'0':'')+aDate.getUTCHours();
break;
case'i':
returnStr+=(aDate.getUTCMinutes()<10?'0':'')+aDate.getUTCMinutes();
break;
case's':
returnStr+=(aDate.getUTCSeconds()<10?'0':'')+aDate.getUTCSeconds();
break;
case'O':
returnStr+='+0000';
break;
case'Z':
returnStr+=0;
break;
case'r':
returnStr+=aDate.toUTCString();
break;
case'U':
returnStr+=aDate.getTime()/1000;
break;
default:
returnStr+=curChar;
}
}
return returnStr;
},
];},["replace_magic_values","formatDate","formatUTCDate"],["Date"], "0.8.0", "0.8.4"
);
// class string.Utils
joo.classLoader.prepare(
"package string",
"public class Utils",1,function($$private){;return[
"public function Utils",function(){
},
"static public function get_colour",function(col)
{col=String(col);
if(col.substr(0,2)=='0x')
return Number(col);
if(col.substr(0,1)=='#')
return Number('0x'+col.substr(1,col.length));
if(col.length==6)
return Number('0x'+col);
return Number(col);
},
];},["get_colour"],["String","Number"], "0.8.0", "0.8.4"
);
// class Tooltip
joo.classLoader.prepare("package",
"public class Tooltip extends flash.display.Sprite",6,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(caurina.transitions.Equations);},
"private var",{style:null},
"private var",{tip_style:NaN},
"private var",{cached_elements:null},
"private var",{tip_showing:false},
"public var",{tip_text:null},
"public static const",{CLOSEST:0},
"public static const",{PROXIMITY:1},
"public static const",{NORMAL:2},
"public function Tooltip",function(json)
{this.super$6();
this.mouseEnabled=false;
this.tip_showing$6=false;
this.style$6={
shadow:true,
rounded:6,
stroke:2,
colour:'#808080',
background:'#f0f0f0',
title:"color: #0000F0; font-weight: bold; font-size: 12;",
body:"color: #000000; font-weight: normal; font-size: 12;",
mouse:Tooltip.CLOSEST,
text:"_default"
};
if(json)
{
this.style$6=object_helper.merge(json,this.style$6);
}
this.style$6.colour=string.Utils.get_colour(this.style$6.colour);
this.style$6.background=string.Utils.get_colour(this.style$6.background);
this.style$6.title=new string.Css(this.style$6.title);
this.style$6.body=new string.Css(this.style$6.body);
this.tip_style$6=this.style$6.mouse;
this.tip_text=this.style$6.text;
this.cached_elements$6=[];
if(this.style$6.shadow==1)
{
var dropShadow=new flash.filters.DropShadowFilter();
dropShadow.blurX=4;
dropShadow.blurY=4;
dropShadow.distance=4;
dropShadow.angle=45;
dropShadow.quality=2;
dropShadow.alpha=0.5;
this.filters=[dropShadow];
}
},
"public function make_tip",function(elements){
this.graphics.clear();
while(this.numChildren>0)
this.removeChildAt(0);
var height=0;
var x=5;
for(var $1 in elements){var e=elements[$1];
var o=this.make_one_tip$6(e,x);
height=Math.max(height,o.height);
x+=o.width+2;
}
this.graphics.lineStyle(this.style$6.stroke,this.style$6.colour,1);
this.graphics.beginFill(this.style$6.background,1);
this.graphics.drawRoundRect(
0,0,
this.width+10,height+5,
this.style$6.rounded,this.style$6.rounded);
},
"private function make_one_tip",function(e,x){
var tt=e.get_tooltip();
var lines=tt.split('<br>');
var top=5;
var width=0;
if(lines.length>1){
var title=this.make_title$6(lines.shift());
title.mouseEnabled=false;
title.x=x;
title.y=top;
top+=title.height;
width=title.width;
this.addChild(title);
}
var text=this.make_body$6(lines.join('\n'));
text.mouseEnabled=false;
text.x=x;
text.y=top;
width=Math.max(width,text.width);
this.addChild(text);
top+=text.height;
return{width:width,height:top};
},
"private function make_title",function(text){
var title=new flash.text.TextField();
title.mouseEnabled=false;
title.htmlText=text;
var fmt=new flash.text.TextFormat();
fmt.color=this.style$6.title.color;
fmt.font="Verdana";
fmt.bold=(this.style$6.title.font_weight=="bold");
fmt.size=this.style$6.title.font_size;
fmt.align="right";
title.setTextFormat(fmt);
title.autoSize="left";
return title;
},
"private function make_body",function(body){
var text=new flash.text.TextField();
text.mouseEnabled=false;
text.htmlText=body;
var fmt2=new flash.text.TextFormat();
fmt2.color=this.style$6.body.color;
fmt2.font="Verdana";
fmt2.bold=(this.style$6.body.font_weight=="bold");
fmt2.size=this.style$6.body.font_size;
fmt2.align="left";
text.setTextFormat(fmt2);
text.autoSize="left";
return text;
},
"private function get_pos",function(e){
var pos=e.get_tip_pos();
var x=(pos.x+this.width+16)>this.stage.stageWidth?(this.stage.stageWidth-this.width-16):pos.x;
var y=pos.y;
y-=4;
y-=(this.height+10);
if(y<0)
{
y=0;
}
return new flash.geom.Point(x,y);
},
"private function show_tip",function(e){
caurina.transitions.Tweener.removeTweens(this);
var p=this.get_pos$6(e);
if(this.style$6.mouse==Tooltip.CLOSEST)
{
this.visible=true;
this.alpha=1;
this.x=p.x;
this.y=p.y;
}
else
{
this.tip_showing$6=true;
tr.ace('show');
this.alpha=0;
this.visible=true;
this.x=p.x;
this.y=p.y;
caurina.transitions.Tweener.addTween(
this,
{
alpha:1,
time:0.4,
transition:caurina.transitions.Equations.easeOutExpo
});
}
},
"public function draw",function(e){
if(this.cached_elements$6[0]==e)
{
if(!this.tip_showing$6)
this.show_tip$6(e);
}
else
{
this.untip$6();
this.cached_elements$6=[e];
this.make_tip([e]);
this.show_tip$6(e);
}
},
"public function closest",function(elements){
if(elements.length==0)
return;
if(this.is_cached$6(elements))
return;
this.untip$6();
this.cached_elements$6=elements;
this.tip$6();
this.make_tip(elements);
var p=this.get_pos$6(elements[0]);
this.visible=true;
caurina.transitions.Tweener.addTween(this,{x:p.x,time:0.3,transition:caurina.transitions.Equations.easeOutExpo});
caurina.transitions.Tweener.addTween(this,{y:p.y,time:0.3,transition:caurina.transitions.Equations.easeOutExpo});
},
"private function is_cached",function(elements){
if(this.cached_elements$6.length==0)
return false;
for(var $1 in elements){var e=elements[$1];
if(this.cached_elements$6.indexOf(e)==-1)
return false;}
return true;
},
"private function untip",function(){
for(var $1 in this.cached_elements$6){var e=this.cached_elements$6[$1];
e.set_tip(false);}
},
"private function tip",function(){
for(var $1 in this.cached_elements$6){var e=this.cached_elements$6[$1];
e.set_tip(true);}
},
"private function hideAway",function(){
this.visible=false;
this.untip$6();
this.cached_elements$6=new Array();
this.alpha=1;
},
"public function hide",function(){
this.tip_showing$6=false;
tr.ace('hide tooltip');
caurina.transitions.Tweener.addTween(this,{alpha:0,time:0.6,transition:caurina.transitions.Equations.easeOutExpo,onComplete:$$bound(this,"hideAway$6")});
},
"public function get_tip_style",function(){
return this.tip_style$6;
},
"public function set_tip_style",function(i){
this.tip_style$6=i;
},
"public function die",function(){
this.filters=[];
this.graphics.clear();
while(this.numChildren>0)
this.removeChildAt(0);
this.style$6=null;
this.cached_elements$6=null;
},
];},[],["flash.display.Sprite","object_helper","string.Utils","string.Css","flash.filters.DropShadowFilter","Math","flash.text.TextField","flash.text.TextFormat","flash.geom.Point","caurina.transitions.Tweener","tr","caurina.transitions.Equations","Array"], "0.8.0", "0.8.4"
);
// class tr
joo.classLoader.prepare("package",
"public class tr",1,function($$private){var trace=joo.trace;return[
"public static function ace",function(o){
if(o==null)
trace('null');
else
trace(o.toString());
if(false)
tr.ace_full();
},
"public static function aces",function(){var optionalArgs=Array.prototype.slice.call(arguments);
var tmp=[];
for(var $1 in optionalArgs)
{var o=optionalArgs[$1];
if(o==null)
tmp.push('null');
else
tmp.push(o.toString());
}
trace(tmp.join(', '));
},
"static public function ace_full",function(snum)
{if(arguments.length<1){snum=3;}
var e=new Error();
var str=e.stack;
if(str==null)
{
trace("(!debug) ");
}
else
{
var stacks=str.split("\n");
var caller=$$private.gimme_caller(stacks[snum]);
trace(caller);
}
},
"static private function gimme_caller",function(line)
{
var dom_pos=line.indexOf("::");
var caller;
if(dom_pos==-1)
{
caller=line.substr(4);
}
else
{
caller=line.substr(dom_pos+2);
}
var lb_pos=caller.indexOf("[");
if(lb_pos==-1)
{
return"["+caller+"]";
}
else
{
var line_num=caller.substr(caller.lastIndexOf(":"));
caller=caller.substr(0,lb_pos);
return"["+caller+line_num;
}
},
"public static function ace_json",function(json){
tr.ace(com.serialization.json.JSON.serialize(json));
},
];},["ace","aces","ace_full","ace_json"],["Error","com.serialization.json.JSON"], "0.8.0", "0.8.4"
);
// class util.Parser
joo.classLoader.prepare("package util",
"public class Parser",1,function($$private){var trace=joo.trace;return[
"public static function isEmptyValue",function(value){
return false;
},
"public static function getStringValue",function(
value,
defaultValue,
isEmptyStringValid){
if(util.Parser.isEmptyValue(defaultValue)){
defaultValue="";
}
if(util.Parser.isEmptyValue(value)){
return defaultValue;
}
if(!isEmptyStringValid&&value.length==0){
return defaultValue;
}
return String(value);
},
"public static function getNumberValue",function(
value,
defaultValue,
isZeroValueValid,
isNegativeValueValid
){
if(util.Parser.isEmptyValue(defaultValue)
||isNaN(defaultValue)
){
defaultValue=0;
}
if(util.Parser.isEmptyValue(value)){
return defaultValue;
}
var numValue=Number(value);
if(isNaN(numValue)){
return defaultValue;
}
if(!isZeroValueValid&&numValue==0){
return defaultValue;
}
if(!isNegativeValueValid&&numValue<0){
return defaultValue;
}
return numValue;
},
"public static function getBooleanValue",function(
value,
defaultValue
){
if(util.Parser.isEmptyValue(value)){
return defaultValue;
}
var numValue=Number(value);
if(!isNaN(numValue)){
if(numValue==0){
return false;
}else{
return true;
}
}
var strValue=util.Parser.getStringValue(value,"false",false);
strValue=strValue.toLowerCase();
if(strValue.indexOf('true')!=-1){
return true;
}else{
return false;
}
},
"public static function runTests",function(){
var notDefinedNum;
trace("testing Parser.getStringValue...");
trace("1) stringOK  '"+util.Parser.getStringValue("stringOK","myDefault",true)+"'");
trace("2) ''        '"+util.Parser.getStringValue("","myDefault",true)+"'");
trace("3) myDefault '"+util.Parser.getStringValue("","myDefault",false)+"'");
trace("testing Parser.getNumberValue...");
trace("01) 999       '"+util.Parser.getNumberValue(999,22222222,true,true)+"'");
trace("02) 999       '"+util.Parser.getNumberValue("999",22222222,true,true)+"'");
trace("06) -1        '"+util.Parser.getNumberValue("abc",-1,false,false)+"'");
trace("07) -1        '"+util.Parser.getNumberValue(null,-1,false,false)+"'");
trace("11) 22222222  '"+util.Parser.getNumberValue(0,22222222,false,false)+"'");
trace("12) 22222222  '"+util.Parser.getNumberValue(-0.1,22222222,false,false)+"'");
trace("13) -0.1      '"+util.Parser.getNumberValue(-0.1,22222222,false,true)+"'");
trace("13) 22222222  '"+util.Parser.getNumberValue("-0.1x",22222222,false,true)+"'");
trace("testing Parser.getBooleanValue...");
trace("true       '"+util.Parser.getBooleanValue("1",false)+"'");
trace("true       '"+util.Parser.getBooleanValue("-1",false)+"'");
trace("false      '"+util.Parser.getBooleanValue("0.000",false)+"'");
trace("false      '"+util.Parser.getBooleanValue("",false)+"'");
trace("true       '"+util.Parser.getBooleanValue("",true)+"'");
trace("false      '"+util.Parser.getBooleanValue("false",false)+"'");
trace("false      '"+util.Parser.getBooleanValue("xxx",false)+"'");
trace("true      '"+util.Parser.getBooleanValue("true",true)+"'");
trace("true      '"+util.Parser.getBooleanValue("TRUE",true)+"'");
trace("true      '"+util.Parser.getBooleanValue(" TRUE ",true)+"'");
},
];},["isEmptyValue","getStringValue","getNumberValue","getBooleanValue","runTests"],["String","Number"], "0.8.0", "0.8.4"
);
// class util.Range
joo.classLoader.prepare("package util",
"public class Range",1,function($$private){;return[

"public var",{min:NaN},
"public var",{max:NaN},
"public var",{step:NaN},
"public var",{offset:false},
"public function Range",function(min,max,step,offset)
{
this.min=min;
this.max=max;
this.step=step;
this.offset=offset;
},
"public function count",function(){
if(this.offset)
return(this.max-this.min)+1;
else
return this.max-this.min;
},
"public function toString",function(){
return'util.Range : '+this.min+', '+this.max;
},
];},[],[], "0.8.0", "0.8.4"
);
