/*
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

// JangarooScript runtime support. Author: Manuel Ohlendorf

package joo {

import joo.*;

public class ResourceBundleAwareClassLoader extends joo.DynamicClassLoader {

  private static const RESOURCE_BUNDLE_PATTERN:RegExp = /.+_properties/;

  public var supportedLocales:Array = ["en"];

  private static function isBundleName(fullClassName:String):Boolean {

    return fullClassName.match(RESOURCE_BUNDLE_PATTERN);

  }


  private function getCurrentLocale():String {
    var userLocale:String = "en";
    var result:String = "en";

    if (navigator) {
      if (navigator.language) {
        userLocale = navigator.language;
      }
      else if (navigator.browserLanguage) {
        userLocale = navigator.browserLanguage;
      }
      else if (navigator.systemLanguage) {
        return navigator.systemLanguage;
      }
      else if (navigator.userLanguage) {
        userLocale = navigator.userLanguage;
      }
    }
    userLocale = userLocale.replace(/-/g,"_");

    var exactMatchFilter:Function = function(item:String, index:int, array:Array):Boolean {
      return item === userLocale;
    };

    var almostMatchFilter:Function = function(item:String, index:int, array:Array):Boolean {
        return item.indexOf(userLocale) != -1;
    };

    var exactMatch:Array = supportedLocales.filter(exactMatchFilter);

    if(exactMatch.length > 0){
      result = exactMatch.pop() as String;
    } else {

      var possibleMatch:Array = supportedLocales.filter(almostMatchFilter);
      if(possibleMatch.length > 0){
        result = possibleMatch.pop() as String;
      }
    }

    //The default language "en" has no ending.
    if(result === "en") {
       result = "";
    } else {
      result = "_" + result;
    }

    return result;
  }


  override protected function getUri(fullClassName : String):String {

    if (isBundleName(fullClassName)) {

      fullClassName += getCurrentLocale();

    }

    return super.getUri(fullClassName);

  }

}
}