joo.classLoader.prepare("package joo.flash",/* {
import flash.display.DisplayObject
import flash.display.Stage

import flash.events.Event
import flash.utils.getDefinitionByName

import joo.DynamicClassLoader
import joo.JooClassDeclaration*/

"public class Run",1,function($$private){var trace=joo.trace;return[function(){joo.classLoader.init(joo.DynamicClassLoader,flash.events.Event);}, 

  "public static function main",function main(id/* : String*/, primaryDisplayObjectClassName/* : String*/)/* : void*/ {
    var classLoader/*:DynamicClassLoader*/ = joo.DynamicClassLoader.INSTANCE;
    classLoader.import_(primaryDisplayObjectClassName);
    classLoader.complete(function joo$flash$Run$16_26()/* : void*/ {
      if (classLoader.debug) {
        trace("[INFO] Loaded Flash main class " + primaryDisplayObjectClassName + ".");
      }
      var primaryDisplayObjectClass/* : Object*/ = flash.utils.getDefinitionByName(primaryDisplayObjectClassName);
      var cd/*:JooClassDeclaration*/ = primaryDisplayObjectClass['$class'];
      var metadata/*:Object*/ = cd.metadata;
      var swf/*:Object*/ = metadata['SWF'];
      var stage/* : Stage*/ = new flash.display.Stage(id, swf);
      // use Jangaroo tricks to add the DisplayObject to the Stage before its constructor is called:
      var displayObject/*:DisplayObject*/ =/* flash.display.DisplayObject*/(new cd.Public());
      stage.internalAddChildAt(displayObject, 0);
      cd.constructor_.call(displayObject);
      displayObject.broadcastEvent(new flash.events.Event(flash.events.Event.ADDED_TO_STAGE, false, false));
    });
  },

];},["main"],["joo.DynamicClassLoader","flash.display.Stage","flash.display.DisplayObject","flash.events.Event"], "0.8.0", "0.8.1"
);