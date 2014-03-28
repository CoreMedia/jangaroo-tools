joo.classLoader.prepare(/*
 * Copyright 2009 CoreMedia AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */

// JangarooScript runtime support. Author: Frank Wienberg

"package joo",/* {*/
"public class DynamicClassLoader extends joo.StandardClassLoader",3,function($$private){var trace=joo.trace;return[ 

  "private static function isEmpty",function isEmpty(object/* : Object*/)/* : Boolean*/ {
    //noinspection LoopStatementThatDoesntLoopJS
    for (var m/*:String*/ in object) {
      return false;
    }
    return true;
  },

  "public static var",{ INSTANCE/*:DynamicClassLoader*/:null},

  "private var",{ resourceByPath/* : Object*/ :function(){return( {});}},
  "private var",{ onCompleteCallbacks/* : Array*//*<Function>*/ :function(){return( []);}},

  "public function DynamicClassLoader",function DynamicClassLoader$() {this.super$3();this.resourceByPath$3=this.resourceByPath$3();this.onCompleteCallbacks$3=this.onCompleteCallbacks$3();this.pendingDependencies$3=this.pendingDependencies$3();this.pendingClassState$3=this.pendingClassState$3();
    joo.classLoader = joo.DynamicClassLoader.INSTANCE = this;
  },

  /**
   * Keep record of all classes whose dependencies still have to be loaded.
   */
  "private var",{ pendingDependencies/* : Array*//*<JooClassDeclaration>*/ :function(){return( []);}},
  /**
   * false => pending
   * true => loading
   */
  "private var",{ pendingClassState/* : Object*//*<String,Boolean>*/ :function(){return( {});}},

  "override public function prepare",function prepare(/*...params*/)/*:JooClassDeclaration*/ {var params=arguments;
    var cd/*:JooClassDeclaration*/ =/* joo.JooClassDeclaration*/(this.prepare$3.apply(this, params));
    this.pendingDependencies$3.push(cd);
    this.fireDependency(cd.fullClassName);
    return cd;
  },

  "public function addDependency",function addDependency(dependency/*:String*/)/*:void*/ {
    this.pendingClassState$3[dependency] = true;
  },

  "public function fireDependency",function fireDependency(dependency/*:String*/)/*:void*/ {
    if (delete this.pendingClassState$3[dependency]) {
//      if (this.debug) {
//        trace("prepared class " + dependency + ", removed from pending classes.");
//      }
      if (this.onCompleteCallbacks$3.length) {
        this.loadPendingDependencies$3();
        if ($$private.isEmpty(this.pendingClassState$3)) {
          this.doCompleteCallbacks(this.onCompleteCallbacks$3);
        }
      }
    }
  },

  "override protected function doCompleteCallbacks",function doCompleteCallbacks(onCompleteCallbacks/* : Array*//*Function*/)/*:void*/ {var this$=this;
    this.onCompleteCallbacks$3 = [];
    // "invoke later":
    joo.getQualifiedObject("setTimeout")(function joo$DynamicClassLoader$76_38()/* : void*/ {
      this$.completeAll();
      this$.internalDoCompleteCallbacks$3(onCompleteCallbacks);
    }, 0);
  },

  "private function internalDoCompleteCallbacks",function internalDoCompleteCallbacks(onCompleteCallbacks/* : Array*//*Function*/)/*:void*/ {
    this.doCompleteCallbacks$3(onCompleteCallbacks);
  },

  // separate factory function to move the anonymous function out of the caller's scope:
  "private function createClassLoadErrorHandler",function createClassLoadErrorHandler(fullClassName/*:String*/, url/*:String*/)/*:Function*/ {var this$=this;
    return function joo$DynamicClassLoader$88_12()/*:void*/ {
      this$.classLoadErrorHandler(fullClassName, url);
    };
  },

  "public function classLoadErrorHandler",function classLoadErrorHandler(fullClassName/*:String*/, url/*:String*/)/*:void*/ {
    trace("[ERROR] Jangaroo Runtime: Class "+fullClassName+" not found at URL ["+url+"].");
  },

  /**
   * Import the class given by its fully qualified class name (package plus name).
   * All imports are collected in a hash and can be used in the #complete() callback function.
   * Additionally, the DynamicClassLoader tries to load the class from a URL if it is not present on #complete().
   * @param fullClassName : String the fully qualified class name (package plus name) of the class to load and import.
   */
  "public override function import_",function import_(fullClassName/* : String*/)/* : void*/ {
    this.import_$3(fullClassName);
    this.load$3(fullClassName);
  },

  "override public function run",function run(mainClassName/* : String, ...args*/)/*:void*/ {var args=Array.prototype.slice.call(arguments,1);
    this.load$3(mainClassName);
    args.splice(0,0,mainClassName);
    this.run$3.apply(this,args);
  },

  "private function load",function load(fullClassName/* : String*/)/* : void*/ {
    var resourcePathMatch/*:Array*/ = fullClassName.match(/^resource:(.*)$/);
    if (resourcePathMatch) {
      this.loadResource$3(resourcePathMatch[1]);
      return;
    }
    if (!this.getClassDeclaration(fullClassName)) {
      if (this.onCompleteCallbacks$3.length==0) {
        if (this.pendingClassState$3[fullClassName]===undefined) {
          // we are not yet in completion phase: just add to pending classes:
          this.pendingClassState$3[fullClassName] = false;
//          if (this.debug) {
//            trace("added to pending classes: "+fullClassName+".");
//          }
        }
      } else {
        if (this.pendingClassState$3[fullClassName]!==true) {
          // trigger loading:
          this.pendingClassState$3[fullClassName] = true;
          var url/*:String*/ = joo.getRelativeClassUrl(fullClassName);
//          if (this.debug) {
//            trace("triggering to load class " + fullClassName + " from URL " + url + ".");
//          }
          var script/*:Object*/ = joo.loadScriptAsync(url);
          // script.onerror does not work in IE, but since this feature is for debugging only, we don't mind:
          script.onerror = this.createClassLoadErrorHandler$3(fullClassName, script['src']);
        }
      }
    }
  },

  "private static const",{ RESOURCE_TYPE_IMAGE/*:String*/ : "Image"},
  "private static const",{ RESOURCE_TYPE_AUDIO/*:String*/ : "Audio"},
  "private static const",{ RESOURCE_TYPE_BY_EXTENSION/*:Object*/ :function(){return( {
    "png": $$private.RESOURCE_TYPE_IMAGE,
    "gif": $$private.RESOURCE_TYPE_IMAGE,
    "jpg": $$private.RESOURCE_TYPE_IMAGE,
    "jpeg": $$private.RESOURCE_TYPE_IMAGE,
    "mp3": $$private.RESOURCE_TYPE_AUDIO,
    "ogg": $$private.RESOURCE_TYPE_AUDIO,
    "wav": $$private.RESOURCE_TYPE_AUDIO
  });}},
  // TODO: map more extensions, also for video etc.
  // TODO: improvement: instead of extensions, we could do a HEAD request to the path and map the Content-Type to media/resource type.

  "private function loadResource",function loadResource(path/*:String*/)/*:void*/ {var this$=this;var this$=this;
    var resource/*:Object*/ = this.resourceByPath$3[path];
    if (!resource) {
      var dotPos/*:int*/ = path.lastIndexOf('.');
      var extension/*:String*/ = path.substring(dotPos + 1);
      var resourceType/*:String*/ = $$private.RESOURCE_TYPE_BY_EXTENSION[extension];
      if (resourceType) {
        var resourceTypeClass/*:Class*/ = joo.getQualifiedObject(resourceType);
        if (resourceTypeClass) {
          this.resourceByPath$3[path] = resource = new (resourceTypeClass)();
          if (resourceType === $$private.RESOURCE_TYPE_IMAGE) {
            this.addDependency("resource:" + path);
            resource.onload = function joo$DynamicClassLoader$171_31()/*:void*/ {
              this$.fireDependency("resource:" + path);
            };
            resource.onerror = function joo$DynamicClassLoader$174_32(m/*:**/)/*:void*/ {
              trace("[WARN]", "Error while loading resource " + path + ": " + m);
              // however, we do not want dynamic loading to fail completely:
              this$.fireDependency("resource:" + path);
            };
          } else if (resourceType === $$private.RESOURCE_TYPE_AUDIO) {
            if (!resource['canPlayType']("audio/" + extension)) {
              // try another MIME type / extension:
              var fallbackExtension/*:String*/ = $$private.findFallback(resource);
              if (!fallbackExtension) {
                return;
              }
              path = path.substring(0, dotPos) + "." + fallbackExtension;
            }
            resource.preload = "auto"; // Embed -> load early, but don't wait for load like with images.
          }
          resource.src = joo.baseUrl + "joo/classes/" + path;
        } else {
          trace("[WARN]", "Resource type " + resourceType + " not supported by client, ignoring resource " + path);
        }
      } else {
        trace("[WARN]", "Ignoring unsupported media type of file " + path);
      }
    }
  },

  "private static const",{ AUDIO_FALLBACK_ORDER/*:Array*/ :function(){return( ["mp3", "ogg", "wav"]);}},
  "private static var",{ AUDIO_FALLBACK_EXTENSION/*:String*/ : null},
  "private static function findFallback",function findFallback(audio/*:Object*/)/*:String*/ {
    if ($$private.AUDIO_FALLBACK_EXTENSION === null) {
      for (var i/*:int*/ = 0; i < $$private.AUDIO_FALLBACK_ORDER.length; i++) {
        var fallback/*:String*/ = $$private.AUDIO_FALLBACK_ORDER[i];
        if (audio['canPlayType']("audio/" + fallback)) {
          return $$private.AUDIO_FALLBACK_EXTENSION = fallback;
        }
      }
      trace("[WARN]", "Could not find any audio extension that this client can play (" + $$private.AUDIO_FALLBACK_ORDER.join(",") +
        "), no sound available.");
      $$private.AUDIO_FALLBACK_EXTENSION = "";
    }
    return $$private.AUDIO_FALLBACK_EXTENSION;
  },

  "public function getResource",function getResource(path/*:String*/)/*:Object*/ {
    return this.resourceByPath$3[path];
  },

  /**
   * Tell Jangaroo to load and initialize all required classes, then call the given function.
   * The function receives an import hash, which can be used in pure JavaScript in a 'with' statement
   * (Jangaroo does not support 'with', there, you would use import declarations!) like this:
   * <pre>
   * joo.classLoader.import_("com.custom.Foo");
   * joo.classLoader.complete(function(imports){with(imports){
   *   Foo.doSomething("bar");
   * }});
   * </pre>
   * @param onCompleteCallback : Function
   * @return void
   */
  "public override function complete",function complete(onCompleteCallback/* : Function = undefined*/)/* : void*/ {
    if (onCompleteCallback || this.onCompleteCallbacks$3.length==0) {
      this.onCompleteCallbacks$3.push(onCompleteCallback || $$private.defaultOnCompleteCallback);
    }
    this.loadPendingDependencies$3();
    if ($$private.isEmpty(this.pendingClassState$3)) {
      // no deferred classes, thus no dependency will trigger execution, so do it explicitly:
      this.doCompleteCallbacks(this.onCompleteCallbacks$3);
    } else {
      for (var c/*:String*/ in this.pendingClassState$3) {
        this.load$3(c);
      }
    }
  },

  "private static function defaultOnCompleteCallback",function defaultOnCompleteCallback()/* : void*/ {
    trace("[INFO] Jangaroo Runtime: All classes loaded!");
  },

  "private function loadPendingDependencies",function loadPendingDependencies()/*:void*/ {
    for (var j/*:int*/ =0; j<this.pendingDependencies$3.length; ++j) {
      var dependencies/* : Array*/ =/* joo.JooClassDeclaration*/(this.pendingDependencies$3[j]).getDependencies();
      for (var i/*:int*/ =0; i<dependencies.length; ++i) {
        this.load$3(dependencies[i]);
      }
    }
    this.pendingDependencies$3 = [];
  },
];},[],["joo.StandardClassLoader","joo.JooClassDeclaration"], "0.8.0", "0.8.1"
);