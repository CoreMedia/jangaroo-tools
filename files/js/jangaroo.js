// Copyright 2008 CoreMedia AG
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an "AS
// IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
// express or implied. See the License for the specific language
// governing permissions and limitations under the License.

// JangarooScript runtime support. Author: Frank Wienberg

Function.prototype.getName = typeof Function.prototype.name=="string"
? (function getName() { return this.name; })
: (function() {
  var nameRE = /function +([a-zA-Z\$_][a-zA-Z\$_0-9]*) *\(/;
  return (function getName() {
    if (!("name" in this)) {
      var matches = nameRE.exec(this.toString());
      var name = matches ? matches[1] : "";
      if (name=="anonymous")
        name = "";
      this.name = name;
    }
    return this.name;
  });
}());Function.prototype.bind = function(object) {
  var fn = this;
  return function() {
    return fn.apply(object,arguments);
  };
};(function(theGlobalObject) {
  function initFields(privateBean, publicBean, fieldNamesAndInitializers) {
    for (var i=0; i<fieldNamesAndInitializers.length; ++i) {
      var fieldNameOrInitializer = fieldNamesAndInitializers[i];
      if (typeof fieldNameOrInitializer == "function") {
        fieldNameOrInitializer();
      } else {
        //alert("init field: "+fieldNameOrInitializer);
        var bean = publicBean[fieldNameOrInitializer] ? publicBean : privateBean;
        bean[fieldNameOrInitializer] = bean[fieldNameOrInitializer]();
      }
    }
  }
  function createPackage(packageName) {
    var $package = theGlobalObject;
    if (packageName) {
      var packageParts = packageName.split(".");
      for (var i=0; i<packageParts.length; ++i) {
        var subpackage = $package[packageParts[i]];
        if (!subpackage) {
          subpackage = new Package();
          $package[packageParts[i]] = subpackage;
        }
        $package = subpackage;
      }
    }
    return $package;
  }
  function isFunction(object) {
    return typeof object=="function" && object.constructor!==RegExp;
  }
  function isEmpty(object) {
    for (var m in object) {
      alert("found "+m);
      return false;
    }
    return true;
  }
  function createEmptyConstructor($prototype) {
    var emptyConstructor = function(){};
    emptyConstructor.prototype =  $prototype;
    return emptyConstructor;
  };
  function createDefaultConstructor(superName) {
    return (function $DefaultConstructor() {
      this[superName].apply(this,arguments);
    });
  }
  function setFunctionName(theFunction, name) {
    theFunction.getName = function() { return name; };
    return theFunction;
  }
  function registerPrivateMember(privateStatic, classPrefix, memberName) {
    var privateMemberName = classPrefix+memberName;
    privateStatic["_"+memberName] = privateMemberName;
    return privateMemberName;
  }
  function createGetClass($constructor) {
    return (function Object$getClass() { return $constructor; });
  }
  function emptySuper() {}
  var ClassDescription = (function() {
    var ClassDescription$static = {
      // static members:
      PENDING: 0,
      PREPARING: 1,
      PREPARED: 2,
      INITIALIZING: 3,
      INITIALIZED: 4,
      classDescriptions: {},
      missingClassDescriptions: {},
      pendingClassDescriptions: {},
      getClassDescription: function(fullClassName) {
        var cd = this.classDescriptions[fullClassName];
        if (!cd) {
          this.missingClassDescriptions[fullClassName] = true;
        }
        return cd;
      },
      waitForSuper: function(classDef) {
        var pendingCDs = this.pendingClassDescriptions[classDef.$extends];
        if (!pendingCDs) {
          pendingCDs = this.pendingClassDescriptions[classDef.$extends] = [];
        }
        pendingCDs.push(classDef);
      },
      prepareSubclasses: function(classDef) {
        var pendingCDS = this.pendingClassDescriptions[classDef.fullClassName];
        if (pendingCDS) {
          delete this.pendingClassDescriptions[classDef.fullClassName];
          for (var c=0; c<pendingCDS.length; ++c) {
            pendingCDS[c].prepare();
          }
        }
      }
    };
    // constructor:
    function ClassDescription(classDef) {
      for (var m in classDef) {
        this[m] = classDef[m];
      }
      this.fullClassName = this.$package ? (this.$package + "." + this.$class) : this.$class;
      ClassDescription$static.classDescriptions[this.fullClassName] = this;
      for (var im in this.$imports) {
        var importDecl = this.$imports[im];
        ClassDescription$static.getClassDescription(importDecl);
      }
      delete ClassDescription$static.missingClassDescriptions[this.fullClassName];
      this.prepare();
    }
    with(ClassDescription$static) {
      // instance members:
      ClassDescription.prototype = {
        fullClassName: undefined,
        $extends: "Object",
        level: undefined,
        state: PENDING,
        superClassDescription: undefined,
        $constructor: undefined,
        Public: undefined,
        publicConstructor: undefined,
        createInitializingPublicStaticMethod: function(methodName) {
          var classDescription = this;
          this.publicConstructor[methodName] = function() {
            classDescription.initialize();
            return classDescription.publicConstructor[methodName].apply(null, arguments);
          };
        },
        /**
         * Prepares this class to be used by constructor, by accessing a static member, or as a super class.
         * The actual class loading is done when any of this three methods is called.
         */
        prepare: function() {
          if (this.state===PREPARING)
            throw new Error("cyclic usages between classes "+this.fullClassName+" and "+this.superClassDescription.fullClassName+".");
          if (this.state!==PENDING)
            return;
          if (this.$extends=="Object") {
            this.superClassDescription = null;
          } else {
            this.superClassDescription = getClassDescription(this.$extends);
            if (!this.superClassDescription || this.superClassDescription.state==PENDING) {
              // super class not yet loaded, stay pending and wait for super class:
              waitForSuper(this);
              return;
            }
          }
          this.state = PREPARING;
          // Only do the minimal setup to allow a preliminary, initializing public constructor and static getter,
          // and to allow subclasses to plug their constructor into this class.
          // create preliminary constructor and static getter that initialize before delegating to the real ones:
          this.level = this.superClassDescription ? this.superClassDescription.level + 1 : 0;
          var classDescription = this;
          this.$constructor = function() {
            classDescription.initialize();
            classDescription.$constructor.apply(this,arguments);
          };
          // TODO: only if public:
          this.$package = createPackage(this.$package);
          var className = this.$class;
          this.$package[className] = this.publicConstructor = setFunctionName(function() {
            classDescription.$constructor.apply(this,arguments);
          }, this.fullClassName);
          // to initialize when calling the first public static method, wrap those methods:
          for (var i=0; i<this.$publicStaticMethods.length; ++i) {
            this.createInitializingPublicStaticMethod(this.$publicStaticMethods[i]);
          }
          if (this.superClassDescription) {
            this.publicConstructor.prototype = new (this.superClassDescription.Public)();
          }
          // TODO: only if not final:
          this.Public = createEmptyConstructor(this.publicConstructor.prototype);
          this.state = PREPARED;
          prepareSubclasses(this);
        },
        /**
         * Initializes this class by finishing the class setup and then invoking all static initializers.
         */
        initialize: function() {
          if (this.state!==PREPARED)
            return this.publicConstructor;
          this.state = INITIALIZING;
          // finish object structure setup of this class:
          // public part: avoid recursion!
          this.$constructor = undefined;          // private part of the object structure:
          if (false && this.$extends!="Object" && this.superClassDescription == null) {
            throw new Error("Super class "+this.$extends+" of class "+this.fullClassName+" is not prepared.");
          }
          var classPrefix = this.level; // + "$";
          var fieldsWithInitializer = [];
          var classDescription = this;
          var superName = classPrefix+"super";
          // static part:
          var publicConstructor = this.publicConstructor;
          var privateStatic = {_super: superName};          if (this.superClassDescription) {
            // init super class:
            this.superClassDescription.initialize();
          }          // evaluate $members, transfer members into the prepared objects:          // Define a mapping to efficiently find the right prototype object to store a member,
          // depending on its modifiers.
          // Note: As long as "protected" is not implemented, treat it like "public".
          var targetMap = {
            $this: {
              fieldsWithInitializer: fieldsWithInitializer,
              $public: this.Public.prototype,
              $protected: this.Public.prototype,
              $private: this.Public.prototype
            },
            $static: {
              fieldsWithInitializer: [],
              $public: publicConstructor,
              $protected: publicConstructor,
              $private: privateStatic
            }
          };
          if (isFunction(this.$members)) {
            var memberDeclarations = this.$members(publicConstructor, privateStatic);
            var i=0;
            while (i<memberDeclarations.length) {
              var memberKey = "$this"; // default: not static
              var visibility = "$public"; // default: public visibility
              var members = memberDeclarations[i++];
              if (members===undefined) {
                continue;
              }
              var memberType = "function";
              var memberName = undefined;
              var modifiers;
              if (typeof members=="string") {
                modifiers = members.split(" ");
                for (var j=0; j<modifiers.length; ++j) {
                  var modifier = modifiers[j];
                  if (modifier=="static") {
                    memberKey = "$static";
                  } else if (modifier=="private" || modifier=="public" || modifier=="protected") {
                    visibility = "$"+modifier;
                  } else if (modifier=="var" || modifier=="const") {
                    memberType = modifier;
                  } else if (modifier=="override") {
                    // so far: ignore. TODO: enable super-call!
                  } else if (j==modifiers.length-1) {
                    // last "modifier" may be the member name:
                    memberName = modifier;
                  } else {
                    throw new Error("Unknown modifier '"+modifier+"'.");
                  }
                }
                if (i>=memberDeclarations.length) {
                  throw new Error("Member expected after modifiers "+modifiers.join(" "));
                }
                members = memberDeclarations[i++];
              } else {
                modifiers = [];
              }
              var target = targetMap[memberKey][visibility];
              //document.writeln("defining "+modifiers.join(" ")+" member(s):");
       if (memberType=="function") {
                if (!memberName) {
                  // found static code block; execute on initialization
                  targetMap.$static.fieldsWithInitializer.push(members);
                } else {
                  if (memberName=="_"+this.$class) {
                    this.$constructor = members;
                    memberName = this.$class;
                  } else if (memberKey=="$this") {
                    if (visibility=="$private") {
                      memberName = registerPrivateMember(privateStatic, classPrefix, memberName);
                      setFunctionName(members, memberName);
                    } else if (isFunction(target[memberName])) {
                      // Found overriding! Store super method as private method delegate for super access:
                      this.Public.prototype[registerPrivateMember(privateStatic, classPrefix, memberName)] = target[memberName];
                    }
                  }
                  setFunctionName(members, memberName);
                  target[memberName] = members;
                }
              } else {
                var targetFieldsWithInitializer = targetMap[memberKey].fieldsWithInitializer;
                for (memberName in members) {
                  var member = members[memberName];
                  if (memberKey=="$this" && visibility=="$private") {
                    memberName = registerPrivateMember(privateStatic, classPrefix, memberName);
                  }
                  target[memberName] = member;
                  if (isFunction(member)) {
                    targetFieldsWithInitializer.push(memberName);
                  }
                }
              }
            }
          }
          this.Public.prototype[superName] =
            classDescription.superClassDescription
            ? fieldsWithInitializer.length > 0
              ? (function $super() {
                   classDescription.superClassDescription.$constructor.apply(this,arguments);
                   initFields(null, this, fieldsWithInitializer);
                 })
              : classDescription.superClassDescription.$constructor
            : fieldsWithInitializer.length > 0
              ? (function $super() {
                   initFields(null, this, fieldsWithInitializer);
                 })
              : emptySuper;
          if (!this.$constructor) {
            this.$constructor = createDefaultConstructor(superName);
          }
          this.Public.prototype.getClass = createGetClass(publicConstructor);
          // TODO: constructor visibility!
          privateStatic[this.$class] = publicConstructor;
          // init static fields with initializer:
          for (var im in this.$imports) {
            var importDecl = this.$imports[im];
            var importedClassDesc = getClassDescription(importDecl);
            if (importedClassDesc) { // ignore unknown imports!
              privateStatic[im] = importedClassDesc.initialize();
            }
          }
          initFields(privateStatic, publicConstructor, targetMap.$static.fieldsWithInitializer);
          return publicConstructor;
        }
      };
    }
    ClassDescription.$static = ClassDescription$static;
    return ClassDescription;
  })();
  function Package() { }
  theGlobalObject.joo = new Package();
  var loadingClasses = {};
  function createScript(code) {
    //alert("creating script "+code);
    var script = document.createElement("script");
    if (code) {
      script.appendChild(document.createTextNode(code));
    }
    document.body.appendChild(script);
    return script;
  }
  theGlobalObject.joo.Class = {
    load: function(fullClassName) {
      if (!ClassDescription.$static.getClassDescription(fullClassName)) {
        if (loadingClasses[fullClassName]) {
          // class description is not there, but we already queued to load it:
          return false;
        }
        loadingClasses[fullClassName] = true;
        var uri = document.location.href;
        uri = uri.substring(0, uri.lastIndexOf("/")+1);
        uri += fullClassName.replace(/\./g,"/")+".js";
        createScript().src = uri;
      }
      // class description is or will be loaded:
      return true;
    },
    run: function(mainClass) {
      if (typeof mainClass=="string") {
        this.load(mainClass);
      }
      var args = [];
      for (var i=1; i<arguments.length; ++i) {
        args.push(arguments[i]);
      }
      this.complete(function() {
        if (typeof mainClass=="string") {
          mainClass = ClassDescription.$static.getClassDescription(mainClass).publicConstructor;
        }
        mainClass.main.apply(null,args);
      });
    },
    prepare: function(packageDef /* import*, classDef, publicStaticMethods, members */) {
      var classDef = arguments[arguments.length-3];
      var publicStaticMethods = arguments[arguments.length-2];
      var members = arguments[arguments.length-1];
      var imports = {};
      for (var im=1; im<arguments.length-3; ++im) {
        var importDecl = arguments[im].split(" ")[1];
        var lastDotPos = importDecl.lastIndexOf(".");
        var abbr = importDecl.substring(lastDotPos+1);
        imports[abbr] = importDecl;
      }
      var classDesc = { $imports: imports, $publicStaticMethods: publicStaticMethods, $members: members};
      if (typeof packageDef=="undefined") {
        classDesc.$package = "";
      } else {
        if (typeof packageDef!="string")
          throw new Error("package declaration must be a string.");
        var packageParts = packageDef.split(/\s+/);
        if (packageParts[0]!="package")
          throw new Error("package declaration must start with 'package'.");
        if (packageParts.length!=2) {
          throw new Error("package declaration must be followed by a package name.");
        }
        classDesc.$package = packageParts[1];
      }      if (typeof classDef!="string")
        throw new Error("class declaration must be a string.");
      var classParts = classDef.split(/\s+/);
      var i=0;
      if (classParts[i]=="public") {
        classDesc.visibility = classParts[i++];
      }
      if (classParts[i]=="abstract") {
        classDesc.$abstract = true;
        ++i;
      }
      if (classParts[i++]!="class")
        throw new Error("expected 'class' after class modifiers.");
      if (i==classParts.length) {
        throw new Error("expected class name after keyword 'class'.");
      }
      classDesc.$class = classParts[i++];
      if (i<classParts.length) {
        if (classParts[i++]!="extends")
          throw new Error("expected EOL or 'extends' after class name.");
        if (i==classParts.length)
          throw new Error("expected class name after 'extends'.");
        classDesc.$extends = classParts[i++];
        if (imports[classDesc.$extends]) {
          classDesc.$extends = imports[classDesc.$extends];
        }
      }
      if (i<classParts.length)
        throw new Error("unexpected token '"+classParts[i]+" after class declaration.");
      new ClassDescription(classDesc);
    },
    init: function(clazz) {
      return ClassDescription.$static.getClassDescription(clazz.getName()).initialize();
    },
    complete: function(oncomplete) {
      if (!isEmpty(loadingClasses) || !isEmpty(ClassDescription.$static.missingClassDescriptions)) {
        // delay doComplete on after scripts are loaded:
        if (oncomplete)
          joo.Class.oncomplete = oncomplete;
        createScript("joo.Class.doComplete();");
        return;
      } else if (oncomplete) {
        oncomplete();
      }
    },
    doComplete: function() {
      var missingCDsMap = ClassDescription.$static.missingClassDescriptions;
      var allLoaded = true;
      for (var missingClassName in missingCDsMap) {
        if (!this.load(missingClassName)) {
          // if a missing class is loading, we tried before but did not succeed:
          throw new Error("ClassNotFoundError: "+missingClassName);
        }
        allLoaded = false;
      }
      if (allLoaded) {
        createScript("joo.Class.oncomplete();");
      } else {
        this.complete();
      }
    }
  };
})(this);
//  alert("runtime loaded!");
joo.typeOf = function typeOf(obj){
  if (obj==undefined) return false;
  var type = typeof obj;
  if (type == 'object' || type == 'function'){
    switch(obj.constructor){
      case Array: return 'array';
      case RegExp: return 'regexp';
    }
    if (typeof obj.length == 'number' && obj.callee) {
      return 'arguments';
    }
  }
  return type;
};
joo.Class.prepare("package joo.css","import joo.lang.JOObject",
"import joo.lang.JsonBuilder","public class Color extends JOObject",[],function($jooPublic,$jooPrivate){with($jooPublic)with($jooPrivate)return["public static const",{TRANSPARENT:function(){return new Color(-1,-1,-1,0);}},
"public static const",{WHITE:function(){return new Color(255,255,255);}},
"public static const",{BLACK:function(){return new Color(0,0,0);}},
"public static const",{GRAY:function(){return new Color(128,128,128);}},
"public static const",{RED:function(){return new Color(255,0,0);}},
"public static const",{GREEN:function(){return new Color(0,255,0);}},
"public static const",{BLUE:function(){return new Color(0,0,255);}},
"public static const",{DARK_BLUE:function(){return new Color(0,0,128);}},"private static const",{FACTOR:0.7},"public _Color",function(red,green,blue){
this[_super]();
this[_red]=red;
this[_green]=green;
this[_blue]=blue;
this[_updateAsString]();
},"private updateAsString",function(){
this[_asString]=this[_red]<0
?"transparent"
:["rgb(",Math.round(this[_red]),",",Math.round(this[_green]),",",Math.round(this[_blue]),")"].join("");
},"public clone",function(){
return new Color(this[_red],this[_green],this[_blue]);
},"public getDarker",function(){
return this.darker();
},"public darker",function(factor){
if(!factor)
factor=FACTOR;
if(factor==FACTOR&&this[_darkerColor]){
return this[_darkerColor];
}
var dc=new Color(
this[_red]*factor,
this[_green]*factor,
this[_blue]*factor
);
if(factor==FACTOR)
this[_darkerColor]=dc;
return dc;
},"public getBrighter",function(){
return this.brighter();
},"public brighter",function(factor){
if(!factor)
factor=FACTOR;
if(factor==FACTOR&&this[_brighterColor]){
return this[_brighterColor];
}
var bc;
var r=this[_red];
var g=this[_green];
var b=this[_blue];var i=1/(1-factor);
if(r==0&&g==0&&b==0){
bc=new Color(i,i,i);
}else{
if(r>0&&r<i)r=i;
if(g>0&&g<i)g=i;
if(b>0&&b<i)b=i;bc=new Color(Math.min(r/factor,255),
Math.min(g/factor,255),
Math.min(b/factor,255));
}
if(factor==FACTOR)
this[_brighterColor]=bc;
return bc;
},"override public hashCode",function(){
return this[_asString];
},"override public equals",function(color){
return color&&this[_asString]==color.toString();
},"private static const",{JSON_PROPERTIES:function(){return ["1red","1green","1blue"];}},"override protected isJsonProperty",function(property){
return JSON_PROPERTIES.contains(property);
},"override protected addPropertiesFromJson",function(json,jsonBuilder){
this[_addPropertiesFromJson](json,jsonBuilder);
this[_updateAsString]();
},"override public toString",function(){
return this[_asString];
},"private var",{red: undefined},
"private var",{green: undefined},
"private var",{blue: undefined},
"private var",{asString: undefined},
"private var",{brighterColor: undefined},
"private var",{darkerColor: undefined},
]}
);joo.Class.prepare("package joo.css","public class URL",[],function($jooPublic,$jooPrivate){with($jooPublic)with($jooPrivate)return["private static const",{TO_STRING:function(){return ["url(",")"];}},"public _URL",function(url){this[_super]();
this.url=url;
},"override public toString",function(){
return TO_STRING.join(this.url);
},"public var",{url: undefined},
]}
);joo.Class.prepare("package joo.html",
"public class Dimensions",[],function($jooPublic,$jooPrivate){with($jooPublic)with($jooPrivate)return["public _Dimensions",function(width,height){this[_super]();
this.width=width;
this.height=height;
},"public var",{width: undefined},
"public var",{height: undefined},
]}
);joo.Class.prepare("package joo.html","import joo.html.Element","public class Document",[],function($jooPublic,$jooPrivate){with($jooPublic)with($jooPrivate)return["public static const",{INSTANCE:function(){return new Document();}},"public _Document",function(ownerWindow){
this[_super]();
this.ownerWindow=ownerWindow?ownerWindow:window;
this[_peer]=this.ownerWindow.document;
},"public getPeer",function(){
return this[_peer];
},"public getWindow",function(){
return this.ownerWindow;
},"public createElement",function(elementName){
return new Element(this,this[_peer].createElement(elementName));
},"public createTextNode",function(text){return this[_peer].createTextNode(text);
},"public getElement",function(peerOrId){
var thePeer=typeof peerOrId=="string"?this[_peer].getElementById(peerOrId):peerOrId;
if(!thePeer){
throw"Undefined Element "+peerOrId;
}
return new Element(this,thePeer);
},"public getDocumentElement",function(){
return this.getElement(this[_peer].documentElement);
},"public getBody",function(){
if(!this[_body]){
this[_body]=this.getElement(this[_peer].getElementsByTagName("body")[0]);
}
return this[_body];
},"public write",function(text){
this[_peer].write(text);
},"public close",function(){
this[_peer].close();
},"override public toString",function(){
return"Document "+this[_peer];
},"public var",{ownerWindow: undefined},
"private var",{peer: undefined},
"private var",{body: undefined},
]}
);joo.Class.prepare("package joo.html","import joo.lang.JOObject",
"import joo.html.Dimensions",
"import joo.html.Offset","public class Element extends JOObject",["setCssText"],function($jooPublic,$jooPrivate){with($jooPublic)with($jooPrivate)return["public _Element",function(ownerDocument,peer){
this[_super]();
this.ownerDocument=ownerDocument;
this[_peer]=peer;
},"public getPeer",function(){
return this[_peer];
},"private static const",{PREVENT_DEFAULT:function(){return (function preventDefault(){
this["returnValue"]=false;
});}},"private static const",{STOP_PROPAGATION:function(){return (function stopPropagation(){
this["cancelBubble"]=true;
});}},"private createIEEventHandler",function(listener){
return(function(){
var event=window.event;
event.preventDefault=PREVENT_DEFAULT;
event.stopPropagation=STOP_PROPAGATION;var scrollContainer=window.document["compatMode"]=="CSS1Compat"?window.document.documentElement:window.document["body"];event.pageX=event.clientX+scrollContainer.scrollLeft;
event.pageY=event.clientY+scrollContainer.scrollTop;
return listener(event);
});
},"public addEventListener",function(eventType,listener,capture){
var thisPeer=this[_peer];
if(typeof thisPeer.addEventListener=="function"){
thisPeer.addEventListener(eventType,listener,capture);
}else{thisPeer.attachEvent("on"+eventType,this[_createIEEventHandler](listener));
}
},"public setId",function(id){
this[_peer].id=id;
return this;
},"public setClassName",function(className){
this[_peer].className=className;
return this;
},"public setAttributes",function(attributes){
var className=attributes["className"];
if(className){
this.setClassName(className);
delete attributes["className"];
}
var style=attributes["style"];
if(style){
this.setStyle(style);
delete attributes["style"];
}
for(var a in attributes){
var value=attributes[a];
if(typeof value=="number"){
value=String(Math.round(value));
}
this[_peer].setAttribute(a,value);
}
return this;
},"public getAttribute",function(name){
return this[_peer].getAttribute(name);
},"private static convertToString",function(value){
switch(typeof value){
case"string":
return value;
case"number":
return Math.round(value)+"px";
case"object":
if(value!=null&&value.constructor==Array){
var result=[];
var i;
for(i=0;i<value.length;++i){
result.push(convertToString(value[i]));
}
return result.join(" ");
}
}
return String(value);
},"public static setCssText",function(element,cssText){
if(typeof element["getPeer"]=="function"){
element=element["getPeer"]();
}
if(Browser.SUPPORTS_CSS_TEXT){
element.style.cssText=cssText;
}else{
element.setAttribute("style",cssText);
}
},"public setStyle",function(styleAttributes){
if(typeof styleAttributes=="string"){
setCssText(this[_peer],styleAttributes);
return this;
}
var style=this[_peer].style;
if(styleAttributes["opacity"]!==undefined&&typeof style.filter=="string"){
var opacity=Math.round(styleAttributes["opacity"]*100);try{this[_peer].filters.item("DXImageTransform.Microsoft.Alpha")["Opacity"]=opacity;
}catch(ex){
style.filter="progid:DXImageTransform.Microsoft.Alpha(opacity="+opacity+")";
}
delete styleAttributes["opacity"];
}
for(var s in styleAttributes){
style[s]=convertToString(styleAttributes[s]);
}
return this;
},"public remove",function(){
this[_peer].parentNode.removeChild(this[_peer]);
return this;
},"private static isJooElement",function(element){
return typeof element.getPeer=="function";
},"private static unwrap",function(element){
return isJooElement(element)?element.getPeer():element;
},"public appendChild",function(element){
var elementPeer=unwrap(element);
this[_peer].appendChild(elementPeer);
return isJooElement(element)?element:this.ownerDocument.getElement(elementPeer);
},"insertBefore",function(element,referenceElement){
var elementPeer=unwrap(element);
this[_peer].insertBefore(elementPeer,unwrap(referenceElement));
return isJooElement(element)?element:this.ownerDocument.getElement(elementPeer);
},"public getParentElement",function(){
return this.ownerDocument.getElement(this[_peer].parentNode);
},"public getOffset",function(){
return new Offset(this[_peer].offsetLeft,this[_peer].offsetTop);
},"public setOffset",function(offsetOrLeft,top){
var left;
if(top===undefined){
left=offsetOrLeft.left;top=offsetOrLeft.top;
}else{
left=offsetOrLeft;
}
return this.setStyle({left:left,top:top});
},"public getAbsoluteOffset",function(){
var thisPeer=this[_peer];
var offset=new Offset(0,0);
do{
offset.left+=thisPeer.offsetLeft;
offset.top+=thisPeer.offsetTop;
thisPeer=thisPeer.offsetParent;
}while(thisPeer);
return offset;
},
"public getDimensions",function(){
var thisPeer=this[_peer];
return new Dimensions(thisPeer.offsetWidth,thisPeer.offsetHeight);
},"override public toString",function(){
return this[_peer].id||this[_peer].nodeName;
},"public var",{ownerDocument: undefined},
"private var",{peer: undefined},
]}
);joo.Class.prepare("package joo.html","public class Offset",["getOffset"],function($jooPublic,$jooPrivate){with($jooPublic)with($jooPrivate)return["public static var",{HOME:function(){return new Offset(0,0);}},"public _Offset",function(left,top){this[_super]();
this.left=left;
this.top=top;
},"public static getOffset",function(offsetOrLeft,top){
if(top!==undefined){
return new Offset(offsetOrLeft,top);
}
return offsetOrLeft;
},"public clone",function(){
return new Offset(this.left,this.top);
},"public plus",function(offsetOrLeft,top){
var offset=getOffset(offsetOrLeft,top);
return new Offset(this.left+offset.left,this.top+offset.top);
},"public minus",function(offsetOrLeft,top){
var offset=getOffset(offsetOrLeft,top);
return new Offset(this.left-offset.left,this.top-offset.top);
},"public getDistance",function(){
return Math.sqrt(this.left*this.left+this.top*this.top);
},"public scale",function(factor){
return new Offset(this.left*factor,this.top*factor);
},"public isHome",function(){
return this.left==0&&this.top==0;
},"override public toString",function(){
return[this.left,"px ",this.top,"px"].join("");
},"public var",{left: undefined},
"public var",{top: undefined},
]}
);joo.Class.prepare("package joo.lang","import joo.lang.JsonBuilder","public class JOObject",["getHashCode","equal","toJsonString","fromJsonString","toJsonObject","fromJsonObject"],function($jooPublic,$jooPrivate){with($jooPublic)with($jooPrivate)return["private static var",{idCnt:0},function(){Array.prototype["hashCode"]=JOObject.prototype["hashCode"];
Array.prototype["equals"]=JOObject.prototype["0arrayEquals"];
},"public static getHashCode",function(obj){
return obj&&typeof obj.hashCode=="function"?obj.hashCode():"_"+obj;
},"public static equal",function(o1,o2){
return o1===o2||o1&&typeof o1.equals=="function"&&o1.equals(o2);
},"private arrayEquals",function(a){
if(this.length==a.length){
for(var i=0;i<this.length;++i){
if(!equal(this[i],a[i])){
return false;
}
}
return true;
}
return false;
},"public static toJsonString",function(json){
switch(typeof json){
case"string":
return'"'+json.replace(/(["\\])/g,"\\$1")+'"';
case"object":
if(json===null){
return"null";
}
if(json.constructor==Array){
return'['+json.map(toJsonString).join(',')+']';
}
var string=[];
for(var property in json){
string.push(toJsonString(property)+':'+toJsonString(json[property]));
}
return'{'+string.join(',')+'}';
case"undefined":
return"undefined";
case"number":
if(!isFinite(json))
return"null";
}
return String(json);
},"private static const",{JSON_REG_EXP:/^("(\\.|[^"\\\n\r])*?"|[,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t])+?$/},"public static fromJsonString",function(str,secure){
return((typeof str!="string")||(secure&&!JSON_REG_EXP.test(str)))?null:eval('('+str+')');
},"public static toJsonObject",function(obj){
return new JsonBuilder().toJsonObject(obj);
},"public static fromJsonObject",function(json){
return new JsonBuilder().fromJsonObject(json);
},"public getClass",function(){
return this["constructor"];
},"public hashCode",function(){
return this["0id"]||(this["0id"]="_"+(idCnt++));
},"public equals",function(joobject){
return this===joobject;
},"public toJson",function(){
return new JsonBuilder().toJsonObject(this);
},"protected addPropertiesToJson",function(result,jsonBuilder){
var noPredicate=typeof this.isJsonProperty!="function";
for(var property in this){
if(property!="_pcl"&&(noPredicate||this.isJsonProperty(property))){
result[property]=jsonBuilder.toJsonObject(this[property]);
}
}
},"protected isJsonProperty",function(property){
var value=this[property];
return typeof value!="function"&&value!==this.getClass().prototype[property];
},"public fromJson",function(json){
this.addPropertiesFromJson(json,new JsonBuilder());
},"protected addPropertiesFromJson",function(json,jsonBuilder){for(var property in json){
if(property.charAt(0)!='$'){
this[property]=jsonBuilder.fromJsonObject(json[property]);
}
}
},"override public toString",function(){
return this.getClass()["getName"]()+"@"+this.hashCode();
},]}
);joo.Class.prepare("package joo.lang",
"class JsonBuilder",[],function($jooPublic,$jooPrivate){with($jooPublic)with($jooPrivate)return["toJsonObject",function(obj){
if(typeof obj!="object"||obj==null)
return obj;
if(obj.constructor==Array){var jsonArray=[];
for(var i=0;i<obj.length;++i){
jsonArray[i]=this.toJsonObject(obj[i]);
}
return jsonArray;
}
var json;
if(typeof obj.addPropertiesToJson=="function"){
var hashCode=obj.hashCode();
var ref=this[_visited][hashCode];
if(ref){
var idref=ref["$id"];
if(!idref){
idref=ref["$id"]=String(this[_idCnt]++);
}
return{"$idref":idref};
}
json=this[_visited][hashCode]={"$class":obj.getClass()["getName"]()};
obj.addPropertiesToJson(json,this);
return json;
}json={};
JOObject.prototype.addPropertiesToJson.call(obj,json,this);
return json;
},"fromJsonObject",function(json){
if(typeof json!="object"||json==null)
return json;
if(json.constructor==Array){var array=[];
for(var i=0;i<json.length;++i){
array[i]=this.fromJsonObject(json[i]);
}
return array;
}
var refId=json["$idref"];
if(refId){
return this[_visited][refId];
}
var fullClassName=json["$class"];
if(!fullClassName){
var obj={};
JOObject.prototype.addPropertiesFromJson.call(obj,json,this);
return obj;
}
var clazz=eval(fullClassName);
var joobject=new clazz();
var id=json["$id"];
if(id){
this[_visited][id]=joobject;
}
joobject.addPropertiesFromJson(json,this);
return joobject;
},"private const",{visited:function(){return {};}},
"private var",{idCnt:1},
]}
);joo.Class.prepare("package joo.util","public class TimedExecutor",[],function($jooPublic,$jooPrivate){with($jooPublic)with($jooPrivate)return["public _TimedExecutor",function(handler,timeout){this[_super]();
this[_handler]=handler;
this[_timeout]=timeout;
this[_trigger]=this[_trigger].bind(this);
},"private setTimeout",function(delay){
this[_timer]=window.setTimeout(this[_trigger],Math.max(this[_timeout]-delay,0));
},"private trigger",function(){
this[_triggerStartTime]=new Date().getTime();
var realTimeout=this[_triggerStartTime]-this[_lastTriggerStartTime];var result=this[_handler](realTimeout,this);
this[_lastTriggerStartTime]=this[_triggerStartTime];
if(typeof result=="number"){
this[_timeout]=result;
}
if(result===false||result<0){
this[_timer]=undefined;
}else{
var duration=new Date().getTime()-this[_triggerStartTime];this[_setTimeout](duration);
}
},"public getTriggerStartTime",function(){
return this[_triggerStartTime];
},"public getLastTriggerStartTime",function(){
return this[_lastTriggerStartTime];
},"public isRunning",function(){
return! !this[_timer];
},"public start",function(){
if(!this[_timer]){
this[_lastTriggerStartTime]=new Date().getTime();
this[_setTimeout](0);
return true;
}
return false;
},"public stop",function(){
if(this[_timer]){
window.clearTimeout(this[_timer]);
this[_timer]=undefined;
return true;
}
return false;
},"public restart",function(){
var result=this.stop();
this.start();
return result;
},"private var",{handler: undefined},
"private var",{timeout:0},
"private var",{timer: undefined},
"private var",{triggerStartTime: undefined},
"private var",{lastTriggerStartTime: undefined},
]}
);joo.Class.prepare("package net.jangaroo.logo","import joo.html.Document",
"import joo.html.Element",
"import joo.html.Offset",
"import joo.css.Color",
"import joo.css.URL",
"import joo.util.TimedExecutor",
"import net.jangaroo.logo.Pupil","public class Eyes",["main"],function($jooPublic,$jooPrivate){with($jooPublic)with($jooPrivate)return["private var",{pupilUrl:function(){return new URL("pupil.png");}},
"private var",{blinkStep:2},
"private var",{blinkIndex: undefined},
"private var",{blinkDir: undefined},
"private var",{blinkMode: undefined},
"private var",{blinkTimer: undefined},
"private var",{leftPupil: undefined},
"private var",{rightPupil: undefined},
"private var",{returnTimer: undefined},
"private var",{logo: undefined},"public static main",function(logoId,customBlinkStep,customPupilUrl,
pupilCenterTop,pupilDiameter,leftPupilOffset,rightPupilOffset,
eyeDiameter){
new Eyes(logoId,customBlinkStep,customPupilUrl,
pupilCenterTop,pupilDiameter,leftPupilOffset,rightPupilOffset,
eyeDiameter);
},"public _Eyes",function(logoId,customBlinkStep,customPupilUrl,
pupilCenterTop,pupilDiameter,leftPupilOffset,rightPupilOffset,
eyeDiameter){this[_super]();
if(customBlinkStep){
this[_blinkStep]=customBlinkStep;
}
if(customPupilUrl){
this[_pupilUrl]=new URL(customPupilUrl);
}
var document=Document.INSTANCE;
this[_logo]=document.getElement(logoId);
this[_logo].setStyle({position:"relative"});
this[_leftPupil]=new Pupil(this[_logo],this[_pupilUrl],new Offset(leftPupilOffset,pupilCenterTop),pupilDiameter,eyeDiameter);
this[_rightPupil]=new Pupil(this[_logo],this[_pupilUrl],new Offset(rightPupilOffset,pupilCenterTop),pupilDiameter,eyeDiameter);
this[_blinkTimer]=new TimedExecutor(this[_doBlink].bind(this),40);
this[_waitAndBlink](5000,10000);
this[_returnTimer]=new TimedExecutor(this[_returnToCenter].bind(this),2000);
document.getBody().addEventListener("click",this[_comeAlive].bind(this));
},"public getLogo",function(){
return this[_logo];
},"public blink",function(){
this[_blinkIndex]=0;
this[_blinkDir]=this[_blinkStep];
this[_blinkMode]=Math.random();
this[_blinkTimer].restart();
},"private doBlink",function(){
this[_blinkIndex]+=this[_blinkDir];
if(this[_blinkMode]<0.9)
this[_leftPupil].updateBlink(this[_blinkIndex]);
if(this[_blinkMode]>0.1)
this[_rightPupil].updateBlink(this[_blinkIndex]);
if(this[_blinkDir]>0&&this[_blinkIndex]+this[_blinkDir]>=this[_rightPupil].pupilDiameter/2)
this[_blinkDir]=-this[_blinkDir];
return this[_blinkIndex]>0;
},"private waitAndBlink",function(min,max){
function randomTimeout(){
return min+(max-min)*Math.random();
};
new TimedExecutor(function(){
this.blink();
return randomTimeout();
}.bind(this),randomTimeout()).start();
},"private processMouseMove",function(event){
var absOffset=new Offset(event.pageX,event.pageY);
this[_leftPupil].setTarget(absOffset);
this[_rightPupil].setTarget(absOffset);
this[_returnTimer].restart();
},"private returnToCenter",function(){
this[_leftPupil].setRelativeTarget(Offset.HOME);
this[_rightPupil].setRelativeTarget(Offset.HOME);
return false;
},"private comeAlive",function(){
Document.INSTANCE.getBody().addEventListener('mousemove',this[_processMouseMove].bind(this));
},]}
);joo.Class.prepare("package net.jangaroo.logo","import joo.html.Document",
"import joo.html.Element",
"import joo.html.Offset",
"import joo.css.Color",
"import joo.css.URL",
"import joo.util.TimedExecutor","public class Pupil",[],function($jooPublic,$jooPrivate){with($jooPublic)with($jooPrivate)return["private static const",{EYE_COLOR:function(){return new Color(192,0,0);}},
"private static const",{PUPIL_DIAMETER:11},
"private static const",{PUPIL_RADIUS:function(){return PUPIL_DIAMETER/2;}},"private static var",{pupilUrl: undefined},"public _Pupil",function(logo,
pupilUrl,
pupilCenter,pupilDiameter,
eyeDiameter){
this[_super]();
this.pupilUrl=pupilUrl;
this[_pupilCenter]=pupilCenter;
this.pupilDiameter=pupilDiameter;
this[_eyeRadius]=eyeDiameter/2;
var background=this[_createElement](logo);
background.setStyle({backgroundColor:EYE_COLOR});
this[_pupil]=this[_createElement](logo);
this[_pupil].setStyle({backgroundImage:pupilUrl,
backgroundRepeat:"no-repeat"});
this[_targetOffsetDelta]=logo.getAbsoluteOffset()
.plus(this[_pupilCenter])
.plus(this.pupilDiameter/2,this.pupilDiameter/2);
this[_moveTimer]=new TimedExecutor(this[_move].bind(this),10);
},"private createElement",function(logo){
var elem=Document.INSTANCE.createElement("div");
elem.setStyle({width:this.pupilDiameter,height:this.pupilDiameter,
lineHeight:0,fontSize:0,
position:"absolute"});
elem.setOffset(this[_pupilCenter]);
logo.appendChild(elem);
return elem;
},"public updateBlink",function(blinkIndex){
this[_pupil].setStyle({marginTop:blinkIndex,
backgroundPosition:["left ",-blinkIndex],
height:this.pupilDiameter-blinkIndex*2});
},"private move",function(){
if(this[_newTargetOffset]){
var newDistance=this[_newTargetOffset].getDistance();
this[_targetOffset]=newDistance>this[_eyeRadius]
?this[_newTargetOffset].scale(this[_eyeRadius]/newDistance)
:this[_newTargetOffset];
this[_newTargetOffset]=null;
}var delta=this[_targetOffset].minus(this[_offset]);
if(!delta.isHome()){
var distance=delta.getDistance();
if(distance>.5){
delta=delta.scale(1/distance);
this[_offset]=this[_offset].plus(delta);
this[_pupil].setOffset(this[_pupilCenter].plus(this[_offset]));
return true;
}else{
this[_offset]=this[_targetOffset];
}
}
return false;
},"public setTarget",function(absOffset){
var relativeOffset=absOffset.minus(this[_targetOffsetDelta]);
this.setRelativeTarget(relativeOffset);
},"public setRelativeTarget",function(newTargetOffset){
this[_newTargetOffset]=newTargetOffset;
this[_moveTimer].start();
},"private var",{pupilCenter: undefined},
"public var",{pupilDiameter: undefined},
"private var",{eyeRadius: undefined},
"private var",{pupil: undefined},
"private var",{offset:function(){return Offset.HOME;}},
"private var",{targetOffset:function(){return Offset.HOME;}},
"private var",{newTargetOffset: undefined},
"private var",{targetOffsetDelta: undefined},
"private var",{moveTimer: undefined},]}
);
