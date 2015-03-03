define(["as3/joo/getOrCreatePackage"], function(getOrCreatePackage) {
  'use strict';
  var localization = getOrCreatePackage("joo.localization");
  var localeCookieName = localization.localeCookieName || "joo.locale";
  var preferredLocales = localization.preferredLocales || [];
  var supportedLocales = localization.supportedLocales || ["en"];
  var defaultLocale = supportedLocales[0];

  function readLocaleFromCookie()/*:String*/ {
    return findSupportedLocale(getCookie(localeCookieName));
  }

  function getCookie(name/*:String*/)/*:String*/ {
    var cookieKey/*:String*/ = name.replace(/([.*+?^${}()|[\]\/\\])/g, "\\$1");
    var match/*:Array*/ = document.cookie.match("(?:^|;)\\s*" + cookieKey + "=([^;]*)");
    return match ? decodeURIComponent(match[1]) : null;
  }

  function getLocaleFromPreferredLocales()/*:String*/ {
    for (var i/*:int*/ = 0; i < preferredLocales.length; i++) {
      var preferredLocale/*:String*/ = findSupportedLocale(preferredLocales[i]);
      if (preferredLocale) {
        return preferredLocale;
      }
    }
    return null;
  }

  function readLocaleFromNavigator()/*:String*/ {
    if (window.navigator) {
      var locale/*:String*/ = navigator['language'] || navigator['browserLanguage']
              || navigator['systemLanguage'] || navigator['userLanguage'];
      if (locale) {
        return findSupportedLocale(locale.replace(/-/g, "_"));
      }
    }
    return null;
  }

  function findSupportedLocale(locale/*:String*/)/*:String*/ {
    if (!locale) {
      return null;
    }
    // find longest match of locale in supported locales
    var longestMatch/*:String*/ = "";
    for (var i/*:int*/ = 0; i < supportedLocales.length; i++) {
      if (locale.indexOf(supportedLocales[i]) === 0
        && supportedLocales[i].length > longestMatch.length) {
        longestMatch = supportedLocales[i];
      }
    }
    return longestMatch ? longestMatch : null;
  }

  var locale = readLocaleFromCookie() || getLocaleFromPreferredLocales() || readLocaleFromNavigator() || defaultLocale;
  var suffix = locale == defaultLocale ? "" : "_" + locale;

  return {
    load: function (name, req, load, config) {
      'use strict';
      req([name + suffix], function() {
        load(req(name));
      });
    }
  };

});