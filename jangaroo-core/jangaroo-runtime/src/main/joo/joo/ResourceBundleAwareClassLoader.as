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

public class ResourceBundleAwareClassLoader extends DynamicClassLoader {

  private static const DAYS_TILL_LOCALE_COOKIE_EXPIRY:int = 10*356;

  private static const RESOURCE_BUNDLE_PATTERN:RegExp = /_properties$/;

  public var supportedLocales:Array;
  public var localeCookieName:String;
  public var localeCookiePath:String = getQualifiedObject("location.pathname");
  public var localeCookieDomain:String = null;

  public function ResourceBundleAwareClassLoader(supportedLocales:Array = ["en"], localeCookieName:String = "joo.locale") {
    var debug:Boolean = classLoader.debug;
    var urlPrefix:String = (classLoader as DynamicClassLoader).urlPrefix;
    super();
    this.debug = debug;
    this.urlPrefix = urlPrefix;
    this.supportedLocales = supportedLocales;
    this.localeCookieName = localeCookieName;
  }

  private static function isBundleName(fullClassName:String):Boolean {

    return !!fullClassName.match(RESOURCE_BUNDLE_PATTERN);

  }

  private function escape(s:String):String {
    return s.replace(/([.*+?^${}()|[\]\/\\])/g, "\\$1");
  }

  private function readLocaleFromCookie():String {
    var cookieKey : String = escape(localeCookieName);
    var document:* = getQualifiedObject("document");
    var match : Array = document.cookie.match("(?:^|;)\\s*" + cookieKey + "=([^;]*)");
    return match ? decodeURIComponent(match[1]) : null;
  }

  private function setCookie(name:String, value:String,
                             path:String = null,
                             expires:Date = null,
                             domain:String = null,
                             secure:Boolean = false):void {
    var document:* = getQualifiedObject("document");
    document.cookie =
            name + "=" + encodeURIComponent(value) +
                    ((expires === null) ? "" : ("; expires=" + expires.toGMTString())) +
                    ((path === null) ? "" : ("; path=" + path)) +
                    ((domain === null) ? "" : ("; domain=" + domain)) +
                    (secure ? "; secure" : "");
  }

  private function getLocaleCookieExpiry():Date {
    var date:Date = new Date();
    date.setTime(date.getTime() + (DAYS_TILL_LOCALE_COOKIE_EXPIRY * 24 * 60 * 60 * 1000));
    return date;
  }

  public function setLocale(locale :String ):void {
    setCookie(localeCookieName, locale, localeCookiePath, getLocaleCookieExpiry(), localeCookieDomain);
  }

  public function getLocale():String {
    var userLocale:String = readLocaleFromCookie();

    if (!userLocale) {
      var navigator:* = getQualifiedObject("navigator");
      if (navigator) {
        userLocale = navigator.language || navigator.browserLanguage || navigator.systemLanguage || navigator.userLanguage;
        if (userLocale) {
          userLocale = userLocale.replace(/-/g, "_");
        }
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
    return longestMatch;
  }

  private function getCurrentLocaleSuffix():String {
    var locale:String = getLocale();
    //The default language "en" has no ending.
    return !locale || locale === "en"
      ? ""
      : "_" + locale;
  }

  override protected function getUri(fullClassName : String):String {

    if (isBundleName(fullClassName)) {

      fullClassName += getCurrentLocaleSuffix();

    }

    return super.getUri(fullClassName);

  }

}
}