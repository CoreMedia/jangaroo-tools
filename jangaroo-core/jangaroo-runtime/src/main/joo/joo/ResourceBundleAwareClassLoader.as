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

  private static const RESOURCE_BUNDLE_PATTERN:RegExp = /_properties$/;

  public var supportedLocales:Array = ["en"];
  public var localeCookieName:String = "locale";

  private static function isBundleName(fullClassName:String):Boolean {

    return fullClassName.match(RESOURCE_BUNDLE_PATTERN);

  }

  private function loadCookie():String {
    var cookieKey : String = localeCookieName.replace(/([.*+?^${}()|[\]\/\\])/g, "\\$1");
    var match : Array = window.document.cookie.match("(?:^|;)\\s*" + cookieKey + "=([^;]*)");
    return match ? decodeURIComponent(match[1]) : null;
  }


  private function getCurrentLocale():String {

    var userLocale:String = loadCookie();
    var result:String;

    if (!userLocale && navigator) {
      userLocale = navigator.language || navigator.browserLanguage || navigator.systemLanguage || navigator.userLanguage;
      if (userLocale) {
        userLocale = userLocale.replace(/-/g, "_");
      }
    }

    if (!userLocale) {
      userLocale = "en";
    }

    //find longest match
    var longestMatch:String;
    for (var i:int = 0; i < supportedLocales.length; i++) {
      if (userLocale.indexOf(supportedLocales[i]) === 0) {
        if (!longestMatch || longestMatch.length > supportedLocales[i]) {
          longestMatch = supportedLocales[i];
        }
      }
    }
    if (longestMatch) {
      result = longestMatch;
    }

    //The default language "en" has no ending.
    if (!result || result === "en") {
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