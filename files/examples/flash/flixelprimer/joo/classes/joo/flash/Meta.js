joo.classLoader.prepare("package joo.flash",/* {
import flash.display.Bitmap
import flash.display.BitmapData

import flash.media.Sound

import joo.DynamicClassLoader
import joo.MemberDeclaration
import joo.JooClassDeclaration
import joo.getOrCreatePackage

import js.HTMLAudioElement
import js.HTMLImageElement*/

"public class Meta",1,function($$private){var is=joo.is,trace=joo.trace;return[function(){joo.classLoader.init(flash.media.Sound,flash.display.Bitmap);}, function()

  {
    joo.getOrCreatePackage("joo.meta").Embed = joo.flash.Meta.embed;
  },

  //noinspection JSUnusedLocalSymbols
  "public static function embed",function embed(classDeclaration/*:JooClassDeclaration*/, memberDeclaration/*:MemberDeclaration*/, parameters/*:Object*/)/*:void*/ {
    var relativeUrl/*:String*/ = parameters['source'];
    var resource/*:Object*/ = joo.DynamicClassLoader.INSTANCE.getResource(relativeUrl);
    if (is(resource,  String)) {
      memberDeclaration.value = function joo$flash$Meta$26_33()/*:String*/ {
        // in order to replace the object created by "new", we have to return a String object, not a String literal:
        return new String(resource);
      };
      return;
    }
    var EmbedClass/*:Function*/;
    var superClassDeclaration/*:JooClassDeclaration*/;
    if (resource) {
      if (is(resource,  js.HTMLImageElement)) {
        superClassDeclaration = flash.display.Bitmap['$class'];
        EmbedClass = function joo$flash$Meta$37_22()/*:void*/ {
          var bitmapData/*:BitmapData*/ = flash.display.BitmapData.fromImg(/*js.HTMLImageElement*/(resource));
          superClassDeclaration.constructor_.call(this, bitmapData);
        };
      } else if (is(resource,  js.HTMLAudioElement)) {
        superClassDeclaration = flash.media.Sound['$class'];
        EmbedClass = function joo$flash$Meta$43_22()/*:void*/ {
          this['audio'] =/* js.HTMLAudioElement*/(resource);
          superClassDeclaration.constructor_.call(this);
        };
      }
    }
    if (EmbedClass) {
      EmbedClass.prototype = new superClassDeclaration.Public();
      EmbedClass.toString = function joo$flash$Meta$51_29()/*:String*/ {
        return relativeUrl;
      };
      memberDeclaration.value = EmbedClass;
    } else {
      // TODO: map other extensions to Sound etc.
      trace("[WARN]", "Ignoring unsupported media type of file " + relativeUrl);
    }
  },
];},["embed"],["joo.DynamicClassLoader","String","js.HTMLImageElement","flash.display.Bitmap","flash.display.BitmapData","js.HTMLAudioElement","flash.media.Sound"], "0.8.0", "0.8.3"

);