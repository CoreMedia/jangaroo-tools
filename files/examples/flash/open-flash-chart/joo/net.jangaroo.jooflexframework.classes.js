// class mx.collections.ArrayCollection
joo.classLoader.prepare(
"package mx.collections",
"public class ArrayCollection extends mx.collections.ListCollectionView",3,function($$private){;return[

"mx_internal static const",{VERSION:"3.3.0.4852"},

"public function ArrayCollection",function(source)
{
this.super$3();
if(source){
for(var i=0;i<source.length;++i){
this[i]=source[i];
}
this.length=source.length;
}
},
"override public function getItemAt",function(index,prefetch){if(arguments.length<2){prefetch=0;}
if(index<0||index>=this.length)
{
throw new Error("[collections] outOfBounds: "+index);
}
return this[index];
},
"override public function addItem",function(item){
this[this.length++]=item;
},
"override public function toArray",function(){
var result=[];
for(var i=0;i<this.length;++i){
result[i]=this[i];
}
return result;
},
"override public function getItemIndex",function(item){
return this.indexOf(item);
},
"override public function removeAll",function(){
this.length=0;
},
"override public function setItemAt",function(item,index){
var oldItem=this.getItemAt(index);
this[index]=item;
return oldItem;
},
];},[],["mx.collections.ListCollectionView","Error"], "0.8.0", "0.8.4"
);
// class mx.collections.IList
joo.classLoader.prepare(
"package mx.collections",
{Event:{name:"collectionChange",type:"mx.events.CollectionEvent"}},
"public interface IList extends flash.events.IEventDispatcher",1,function($$private){;return[
,,,,,,,,,,
];},[],["flash.events.IEventDispatcher"], "0.8.0", "0.8.4"
);
// class mx.collections.ListCollectionView
joo.classLoader.prepare(
"package mx.collections",
{Event:{name:"collectionChange",type:"mx.events.CollectionEvent"}},
"public class ListCollectionView extends Array\n"+
"implements mx.collections.IList",2,function($$private){;return[

"mx_internal static const",{VERSION:"3.3.0.4852"},

"public function addItem",function(item){
throw new Error("not implemented!");
},
"public function addItemAt",function(item,index){
throw new Error("not implemented!");
},
"public function getItemAt",function(index,prefetch){if(arguments.length<2){prefetch=0;}
throw new Error("not implemented!");
},
"public function getItemIndex",function(item){
throw new Error("not implemented!");
},
"public function itemUpdated",function(item,property,oldValue,newValue){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){property=null;}oldValue=null;}newValue=null;}
throw new Error("not implemented!");
},
"public function removeAll",function(){
throw new Error("not implemented!");
},
"public function removeItemAt",function(index){
throw new Error("not implemented!");
},
"public function setItemAt",function(item,index){
throw new Error("not implemented!");
},
"public function toArray",function(){
throw new Error("not implemented!");
},
"public function addEventListener",function(type,listener,useCapture,priority,useWeakReference){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){useCapture=false;}priority=0;}useWeakReference=false;}
throw new Error("not implemented!");
},
"public function dispatchEvent",function(event){
throw new Error("not implemented!");
},
"public function hasEventListener",function(type){
throw new Error("not implemented!");
},
"public function removeEventListener",function(type,listener,useCapture){if(arguments.length<3){useCapture=false;}
throw new Error("not implemented!");
},
"public function willTrigger",function(type){
throw new Error("not implemented!");
},
];},[],["Array","mx.collections.IList","Error"], "0.8.0", "0.8.4"
);
// class mx.events.CollectionEvent
joo.classLoader.prepare(
"package mx.events",
"public class CollectionEvent extends flash.events.Event",2,function($$private){;return[

"mx_internal static const",{VERSION:"3.3.0.4852"},

"public static const",{COLLECTION_CHANGE:"collectionChange"},
"public function CollectionEvent",function(type,bubbles,
cancelable,
kind,location,
oldLocation,items)
{if(arguments.length<7){if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){bubbles=false;}cancelable=false;}kind=null;}location=-1;}oldLocation=-1;}items=null;}
this.super$2(type,bubbles,cancelable);
this.kind=kind;
this.location=location;
this.oldLocation=oldLocation;
this.items=items?items:[];
},
"public var",{kind:null},
"public var",{items:null},
"public var",{location:0},
"public var",{oldLocation:0},
"override public function toString",function()
{
return this.formatToString("CollectionEvent","kind","location",
"oldLocation","type","bubbles",
"cancelable","eventPhase");
},
"override public function clone",function()
{
return new mx.events.CollectionEvent(this.type,this.bubbles,this.cancelable,this.kind,this.location,this.oldLocation,this.items);
},
];},[],["flash.events.Event"], "0.8.0", "0.8.4"
);
// class mx.events.CollectionEventKind
joo.classLoader.prepare(
"package mx.events",
"public final class CollectionEventKind",1,function($$private){;return[

"mx_internal static const",{VERSION:"3.3.0.4852"},

"public static const",{ADD:"add"},
"public static const",{MOVE:"move"},
"public static const",{REFRESH:"refresh"},
"public static const",{REMOVE:"remove"},
"public static const",{REPLACE:"replace"},
"mx_internal static const",{EXPAND:"expand"},
"public static const",{RESET:"reset"},
"public static const",{UPDATE:"update"},
];},[],[], "0.8.0", "0.8.4"
);
// class mx.utils.ArrayUtil
joo.classLoader.prepare(
"package mx.utils",
"public class ArrayUtil",1,function($$private){var is=joo.is,as=joo.as;return[

"mx_internal static const",{VERSION:"3.3.0.4852"},

"public static function toArray",function(obj)
{
if(!obj)
return[];
else if(is(obj,Array))
return as(obj,Array);
else
return[obj];
},
"public static function getItemIndex",function(item,source)
{
var n=source.length;
for(var i=0;i<n;i++)
{
if(source[i]===item)
return i;
}
return-1;
},
];},["toArray","getItemIndex"],["Array"], "0.8.0", "0.8.4"
);
// class mx.utils.DescribeTypeCache
joo.classLoader.prepare("package mx.utils",
"public class DescribeTypeCache",1,function($$private){;return[
"public function DescribeTypeCache",function(){
},
"static function describeType",function(obj){
return undefined;
},
];},["describeType"],[], "0.8.0", "0.8.4"
);
// class mx.utils.ObjectUtil
joo.classLoader.prepare(
"package mx.utils",
"public class ObjectUtil",1,function($$private){var is=joo.is,as=joo.as;return[

"mx_internal static const",{VERSION:"3.3.0.4852"},

"private static var",{defaultToStringExcludes:function(){return(["password","credentials"]);}},
"public static function compare",function(a,b,depth)
{if(arguments.length<3){depth=-1;}
return $$private.internalCompare(a,b,0,depth,new flash.utils.Dictionary(true));
},
"public static function copy",function(value)
{
return null;
},
"public static function isSimple",function(value)
{
var type=typeof(value);
switch(type)
{
case"number":
case"string":
case"boolean":
{
return true;
}
case"object":
{
return(is(value,Date))||(is(value,Array));
}
}
return false;
},
"public static function numericCompare",function(a,b)
{
if(isNaN(a)&&isNaN(b))
return 0;
if(isNaN(a))
return 1;
if(isNaN(b))
return-1;
if(a<b)
return-1;
if(a>b)
return 1;
return 0;
},
"public static function stringCompare",function(a,b,
caseInsensitive)
{if(arguments.length<3){caseInsensitive=false;}
if(a==null&&b==null)
return 0;
if(a==null)
return 1;
if(b==null)
return-1;
if(caseInsensitive)
{
a=a.toLocaleLowerCase();
b=b.toLocaleLowerCase();
}
var result=a.localeCompare(b);
if(result<-1)
result=-1;
else if(result>1)
result=1;
return result;
},
"public static function dateCompare",function(a,b)
{
if(a==null&&b==null)
return 0;
if(a==null)
return 1;
if(b==null)
return-1;
var na=a.getTime();
var nb=b.getTime();
if(na<nb)
return-1;
if(na>nb)
return 1;
return 0;
},
"public static function toString",function(value,
namespaceURIs,
exclude)
{if(arguments.length<3){if(arguments.length<2){namespaceURIs=null;}exclude=null;}
if(exclude==null)
{
exclude=$$private.defaultToStringExcludes;
}
$$private.refCount=0;
return $$private.internalToString(value,0,null,namespaceURIs,exclude);
},
"private static function internalToString",function(value,
indent,
refs,
namespaceURIs,
exclude)
{if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){indent=0;}refs=null;}namespaceURIs=null;}exclude=null;}
var str;
var type=value==null?"null":typeof(value);
switch(type)
{
case"boolean":
case"number":
{
return value.toString();
}
case"string":
{
return"\""+value.toString()+"\"";
}
case"object":
{
if(is(value,Date))
{
return value.toString();
}
else if(is(value,Class))
{
return"("+flash.utils.getQualifiedClassName(value)+")";
}
else
{
var classInfo=mx.utils.ObjectUtil.getClassInfo(value,exclude,
{includeReadOnly:true,uris:namespaceURIs});
var properties=classInfo.properties;
str="("+classInfo.name+")";
if(refs==null)
refs=new flash.utils.Dictionary(true);
var id=refs[value];
if(id!=null)
{
str+="#"+id;
return str;
}
if(value!=null)
{
str+="#"+$$private.refCount;
refs[value]=$$private.refCount;
$$private.refCount++;
}
var isArray=is(value,Array);
var isDict=is(value,flash.utils.Dictionary);
var prop;
indent+=2;
for(var j=0;j<properties.length;j++)
{
str=$$private.newline(str,indent);
prop=properties[j];
if(isArray)
str+="[";
else if(isDict)
str+="{";
if(isDict)
{
str+=$$private.internalToString(prop,indent,refs,
namespaceURIs,exclude);
}
else
{
str+=prop.toString();
}
if(isArray)
str+="] ";
else if(isDict)
str+="} = ";
else
str+=" = ";
try
{
str+=$$private.internalToString(value[prop],indent,refs,
namespaceURIs,exclude);
}
catch(e){if(is(e,Error))
{
str+="?";
}else throw e;}
}
indent-=2;
return str;
}
break;
}
case"xml":
{
return value.toString();
}
default:
{
return"("+type+")";
}
}
return"(unknown)";
},
"private static function newline",function(str,n)
{if(arguments.length<2){n=0;}
var result=str;
result+="\n";
for(var i=0;i<n;i++)
{
result+=" ";
}
return result;
},
"private static function internalCompare",function(a,b,
currentDepth,desiredDepth,
refs)
{
if(a==null&&b==null)
return 0;
if(a==null)
return 1;
if(b==null)
return-1;
var typeOfA=typeof(a);
var typeOfB=typeof(b);
var result=0;
if(typeOfA==typeOfB)
{
switch(typeOfA)
{
case"boolean":
{
result=mx.utils.ObjectUtil.numericCompare(Number(a),Number(b));
break;
}
case"number":
{
result=mx.utils.ObjectUtil.numericCompare(as(a,Number),as(b,Number));
break;
}
case"string":
{
result=mx.utils.ObjectUtil.stringCompare(as(a,String),as(b,String));
break;
}
case"object":
{
var newDepth=desiredDepth>0?desiredDepth-1:desiredDepth;
var aRef=refs[a];
var bRef=refs[b];
if(aRef&&!bRef)
return 1;
else if(bRef&&!aRef)
return-1;
else if(bRef&&aRef)
return 0;
refs[a]=true;
refs[b]=true;
if(desiredDepth!=-1&&(currentDepth>desiredDepth))
{
result=mx.utils.ObjectUtil.stringCompare(a.toString(),b.toString());
}
else if((is(a,Array))&&(is(b,Array)))
{
result=$$private.arrayCompare(as(a,Array),as(b,Array),currentDepth,desiredDepth,refs);
}
else if((is(a,Date))&&(is(b,Date)))
{
result=mx.utils.ObjectUtil.dateCompare(as(a,Date),as(b,Date));
}
else if((is(a,mx.collections.IList))&&(is(b,mx.collections.IList)))
{
result=$$private.listCompare(as(a,mx.collections.IList),as(b,mx.collections.IList),currentDepth,desiredDepth,refs);
}
else if(flash.utils.getQualifiedClassName(a)==flash.utils.getQualifiedClassName(b))
{
var aProps=mx.utils.ObjectUtil.getClassInfo(a).properties;
var bProps;
if(flash.utils.getQualifiedClassName(a)=="Object")
{
bProps=mx.utils.ObjectUtil.getClassInfo(b).properties;
result=$$private.arrayCompare(aProps,bProps,currentDepth,newDepth,refs);
}
if(result!=0)
{
return result;
}
var propName;
var aProp;
var bProp;
for(var i=0;i<aProps.length;i++)
{
propName=aProps[i];
aProp=a[propName];
bProp=b[propName];
result=$$private.internalCompare(aProp,bProp,currentDepth+1,newDepth,refs);
if(result!=0)
{
i=aProps.length;
}
}
}
else
{
return 1;
}
break;
}
}
}
else
{
return mx.utils.ObjectUtil.stringCompare(typeOfA,typeOfB);
}
return result;
},
"public static function getClassInfo",function(obj,
excludes,
options)
{if(arguments.length<3){if(arguments.length<2){excludes=null;}options=null;}
return null;
},
"public static function hasMetadata",function(obj,
propName,
metadataName,
excludes,
options)
{if(arguments.length<5){if(arguments.length<4){excludes=null;}options=null;}
var classInfo=mx.utils.ObjectUtil.getClassInfo(obj,excludes,options);
var metadataInfo=classInfo["metadata"];
return $$private.internalHasMetadata(metadataInfo,propName,metadataName);
},
"private static function internalHasMetadata",function(metadataInfo,propName,metadataName)
{
if(metadataInfo!=null)
{
var metadata=metadataInfo[propName];
if(metadata!=null)
{
if(metadata[metadataName]!=null)
return true;
}
}
return false;
},
"private static function getCacheKey",function(o,excludes,options)
{if(arguments.length<3){if(arguments.length<2){excludes=null;}options=null;}
var key=flash.utils.getQualifiedClassName(o);
if(excludes!=null)
{
for(var i=0;i<excludes.length;i++)
{
var excl=as(excludes[i],String);
if(excl!=null)
key+=excl;
}
}
if(options!=null)
{
for(var flag in options)
{
key+=flag;
var value=as(options[flag],String);
if(value!=null)
key+=value;
}
}
return key;
},
"private static function arrayCompare",function(a,b,
currentDepth,desiredDepth,
refs)
{
var result=0;
if(a.length!=b.length)
{
if(a.length<b.length)
result=-1;
else
result=1;
}
else
{
var key;
for(key in a)
{
if(b.hasOwnProperty(key))
{
result=$$private.internalCompare(a[key],b[key],currentDepth,
desiredDepth,refs);
if(result!=0)
return result;
}
else
{
return-1;
}
}
for(key in b)
{
if(!a.hasOwnProperty(key))
{
return 1;
}
}
}
return result;
},
"private static function listCompare",function(a,b,currentDepth,
desiredDepth,refs)
{
var result=0;
if(a.length!=b.length)
{
if(a.length<b.length)
result=-1;
else
result=1;
}
else
{
for(var i=0;i<a.length;i++)
{
result=$$private.internalCompare(a.getItemAt(i),b.getItemAt(i),
currentDepth+1,desiredDepth,refs);
if(result!=0)
{
i=a.length;
}
}
}
return result;
},
"private static var",{refCount:0},
"private static var",{CLASS_INFO_CACHE:function(){return({});}},
];},["compare","copy","isSimple","numericCompare","stringCompare","dateCompare","toString","getClassInfo","hasMetadata"],["flash.utils.Dictionary","Date","Array","Class","Error","Number","String","mx.collections.IList"], "0.8.0", "0.8.4"
);
// class mx.utils.StringUtil
joo.classLoader.prepare(
"package mx.utils",
"public class StringUtil",1,function($$private){var is=joo.is,as=joo.as;return[

"mx_internal static const",{VERSION:"3.3.0.4852"},

"public static function trim",function(str)
{
if(str==null)return'';
var startIndex=0;
while(mx.utils.StringUtil.isWhitespace(str.charAt(startIndex)))
++startIndex;
var endIndex=str.length-1;
while(mx.utils.StringUtil.isWhitespace(str.charAt(endIndex)))
--endIndex;
if(endIndex>=startIndex)
return str.slice(startIndex,endIndex+1);
else
return"";
},
"public static function trimArrayElements",function(value,delimiter)
{
if(value!=""&&value!=null)
{
var items=value.split(delimiter);
var len=items.length;
for(var i=0;i<len;i++)
{
items[i]=mx.utils.StringUtil.trim(items[i]);
}
if(len>0)
{
value=items.join(delimiter);
}
}
return value;
},
"public static function isWhitespace",function(character)
{
switch(character)
{
case" ":
case"\t":
case"\r":
case"\n":
case"\f":
return true;
default:
return false;
}
},
"public static function substitute",function(str)
{var rest=Array.prototype.slice.call(arguments,1);
if(str==null)return'';
var len=rest.length;
var args;
if(len==1&&is(rest[0],Array))
{
args=as(rest[0],Array);
len=args.length;
}
else
{
args=rest;
}
for(var i=0;i<len;i++)
{
str=str.replace(new RegExp("\\{"+i+"\\}","g"),String(args[i]));
}
return str;
},
];},["trim","trimArrayElements","isWhitespace","substitute"],["Array","RegExp","String"], "0.8.0", "0.8.4"
);
