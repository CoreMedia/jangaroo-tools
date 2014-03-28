joo.classLoader.prepare("package js",/* {*/

"public class Navigator",1,function($$private){;return[ 

  /**
   * String passed by browser as user-agent header. (ie: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1))
   */
  "public native function get userAgent"/*():String;*/,

  /**
   * The name of the browser (ie: Microsoft Internet Explorer).
   */
  "public native function get appName"/*():String;*/,

  /**
   * The code name of the browser.
   */
  "public native function get appCodeName"/*():String;*/,

  /**
   * Version information for the browser (ie: 4.75 [en] (Win98; U)).
   */
  "public native function get appVersion"/*():String;*/,

  /**
   * Boolean that indicates whether the browser has cookies enabled.
   */
  "public native function get cookieEnabled"/*():Boolean;*/,

  /**
   * The platform of the client's computer (ie: Win32).
   */
  "public native function get platform"/*():String;*/,

  /**
   * Returns the default language of the browser version (ie: en-US). NS and Firefox only.
   */
  "public native function get language"/*():String;*/,

  /**
   * Returns the default language of the operating system (ie: en-us). IE only.
   */
  "public native function get systemLanguage"/*():String;*/,

  /**
   * An array of all MIME types supported by the client. NS and Firefox only.
   */
  "public native function get mimeTypes"/*():Array;*/,

  /**
   * An array of all plug-ins currently installed on the client. NS and Firefox only.
   */
  "public native function get plugins"/*():Array;*/,

  /**
   * Returns the preferred language setting of the user (ie: en-ca). IE only.
   */
  "public native function get userLanguage"/*():Boolean;*/,

];},[],[], "0.8.0", "0.8.3"

);