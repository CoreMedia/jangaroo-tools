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

  private static const RESOURCE_BUNDLE_PATTERN = /.+_properties/;

  private static function isBundleName(fullClassName:String):Boolean {

    return fullClassName.match(RESOURCE_BUNDLE_PATTERN);

  }


  private static function getCurrentLocale():String {

    if (navigator) {
      if (navigator.language) {
        return navigator.language;
      }
      else if (navigator.browserLanguage) {
        return navigator.browserLanguage;
      }
      else if (navigator.systemLanguage) {
        return navigator.systemLanguage;
      }
      else if (navigator.userLanguage) {
        return navigator.userLanguage;
      }
    }

    return "en";

  }


  override protected function getUri(fullClassName : String):String {

    if (isBundleName(fullClassName)) {

      fullClassName += "_" + getCurrentLocale();

    }

    return super.getUri(fullClassName);

  }

}
}