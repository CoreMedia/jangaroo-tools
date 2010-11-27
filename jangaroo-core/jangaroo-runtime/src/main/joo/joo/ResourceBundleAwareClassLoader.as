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

package joo {

/**
 * A class loader that provides special treatment of resource bundle classes.
 * It maintains a current locale and loads a locale-specific subclass of any
 * class ending with <code>_properties</code>.
 * <p>The locale is saved in and retrieved from a Cookie whose name, path, and domain
 * can be configured.</p>
 * <p>In your JavaScript code, after including the Jangaroo Runtime, activate
 * locale-specific resource bundle class loading as follows:</p>
 * <pre>
 * new joo.ResourceBundleAwareClassLoader(["en", "de"]);
 * </pre>
 * <p>You can create resource bundle classes by creating <code>*_<locale>.properties</code>
 * files below the <code>src/main/joo</code> directory. These files are translated to AS3
 * code by the Jangaroo properties compiler (Maven goal: <code>properties</code>).
 * Each resource bundle must consist of properties files for each supported locale, where
 * the default locale is suppressed.
 * For example for supported locales <code>["en", "de"]</code> (which makes <code>"en"</code>
 * the default locale), you need properties files <code>My.properties</code> and <code>My_de.properties</code>.</p>
 * <p>To change the locale, your application code must call</p>
 * <pre>
 * joo.ResourceBundleAwareClassLoader.INSTANCE.setLocale(newLocale);
 * </pre>
 *
 * @see #ResourceBundleAwareClassLoader
 * @see #getLocale
 * @see #setLocale
 *
 * @author Manuel Ohlendorf
 * @author Frank Wienberg
 */
public class ResourceBundleAwareClassLoader extends DynamicClassLoader {

  private static const DAYS_TILL_LOCALE_COOKIE_EXPIRY:int = 10*356;

  private static const RESOURCE_BUNDLE_PATTERN:RegExp = /_properties$/;

  /**
   * The ResourceBundleAwareClassLoader singleton, which is created or overwritten
   * by invoking the constructor.
   * @see #ResourceBundleAwareClassLoader
   */
  public static var INSTANCE:ResourceBundleAwareClassLoader;

  private var supportedLocales:Array;

  private var localeCookieName:String;

  private var localeCookiePath:String;

  /**
   *
   * Set before any resource bundle is loaded.
   * @see #getLocale
   */
  public var localeCookieDomain:String = null;

  private var locale:String;

  /**
   * Create a new ResourceBundleAwareClassLoader with the given supported locales, default locale,
   * and locale Cookie name, and set it as the default Jangaroo class loader (<code>joo.classLoader</code>)
   * and as the singleton ResourceBundleAwareClassLoader (<code>INSTANCE</code>).
   * @param supportedLocales (default: <code>["en"]</code>) An Array of supported locales (String).
   *   The current locale is guaranteed to be an item of this list.
   *   The first element of this list is used as the default locale to use when all other attempts to determine
   *   a locale fail.
   * @param localeCookieName (default: <code>"joo.locale"</code>) The name of the Cookie to load and store locale
   *   information on the client.
   * @param localeCookiePath (default: <code>window.location.pathname</code>) The path of the Cookie to load and
   *   store locale information on the client.
   * @param localeCookieDomain (default: <code>null</code>) The domain of the Cookie to load and store locale
   *   information on the client.
   * @see joo.classLoader
   * @see #INSTANCE
   * @see #getLocale
   */
  public function ResourceBundleAwareClassLoader(supportedLocales:Array = ["en"],
                                                 localeCookieName:String = "joo.locale",
                                                 localeCookiePath:String = null,
                                                 localeCookieDomain:String = null) {
    INSTANCE = this;
    super();
    this.supportedLocales = supportedLocales;
    this.localeCookieName = localeCookieName;
    this.localeCookiePath = localeCookiePath || getQualifiedObject("location.pathname");
    this.localeCookieDomain = localeCookieDomain;
  }

  public function getSupportedLocales():Array {
    return supportedLocales.concat();
  }

  public function getDefaultLocale():String {
    return supportedLocales[0];
  }

  override protected function createClassDeclaration(packageDef : String, classDef : String, memberFactory : Function,
                                                     publicStaticMethodNames : Array, dependencies : Array):SystemClassDeclaration {
    var cd : ClassDeclaration = ClassDeclaration(super.createClassDeclaration(packageDef, classDef, memberFactory, publicStaticMethodNames, dependencies));
    if (cd.fullClassName.match(RESOURCE_BUNDLE_PATTERN)) {
      cd.getDependencies().push(getLocalizedResourceClassName(cd));
    }
    return cd;
  }

  /**
   * Used internally by code generated by the properties compiler.
   * In case your want to implement a custom resource bundle, use the following code to
   * generate a locale-specific instance of your bundle class:
   * <pre>
   * public static const INSTANCE:My_properties = My_properties(ResourceBundleAwareClassLoader.INSTANCE.createSingleton(My_properties));
   * </pre>
   * @param resourceBundle the resource bundle class for which the subclass corresponding to the current locale
   *   is to be instantiated.
   * @return Object an instance of the resource bundle class or the subclass corresponding to the current locale
   * @see #getLocale
   */
  public function createSingleton(resourceBundle:Class):Object {
    var cd:NativeClassDeclaration = NativeClassDeclaration(resourceBundle['$class']);
    var fullLocalizedClassName:String = getLocalizedResourceClassName(cd);
    var LocalizedResourceBundle:Class = getQualifiedObject(fullLocalizedClassName);
    return new LocalizedResourceBundle();
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

  private function readLocaleFromNavigator(locale:String):String {
    var navigator:* = getQualifiedObject("navigator");
    if (navigator) {
      locale = navigator['language'] || navigator['browserLanguage']
        || navigator['systemLanguage'] || navigator['userLanguage'];
      if (locale) {
        locale = locale.replace(/-/g, "_");
      }
    }
    return locale;
  }

  /**
   * Sets the current locale to the given locale or, if the given locale is not supported,
   * to the longest match in the supported locales, or the default locale if there is not match.
   * @param locale the locale to use for resource bundle class loading
   * @return String the supported locale that has actually been set
   * @see #ResourceBundleAwareClassLoader
   */
  public function setLocale(locale :String):String {
    // find longest match of locale in supported locales
    var longestMatch:String = "";
    for (var i:int = 0; i < supportedLocales.length; i++) {
      if (locale.indexOf(supportedLocales[i]) === 0
        && supportedLocales[i].length > longestMatch.length) {
        longestMatch = supportedLocales[i];
      }
    }
    this.locale = longestMatch ? longestMatch : getDefaultLocale();
    setCookie(localeCookieName, this.locale, localeCookiePath, getLocaleCookieExpiry(), localeCookieDomain);
    return this.locale;
  }

  /**
   * Return the locale currently used for resource bundle class loading.
   * On the first call of this method, the locale is read from the Cookie given by
   * <code>localeCookieName</code> (default "joo.locale"), <code>localeCookiePath</code>
   * (<code>window.location.pathname</code>) and <code>localeCookieDomain</code>
   * (<code>null</code>).
   * If there is no such Cookie, the browser's <code>navigator</code> object is asked for its
   * language. If the locale still is not determined, the <code>defaultLocale</code> is used.
   * This value if stored using <code>setLocale()</code>.
   *
   * @return the locale currently used for resource bundle class loading.
   *
   * @see #ResourceBundleAwareClassLoader
   * @see #setLocale
   */
  public function getLocale():String {
    if (!this.locale) {
      setLocale(readLocaleFromCookie() || readLocaleFromNavigator(locale));
    }
    return this.locale;
  }

  private function getLocalizedResourceClassName(cd:NativeClassDeclaration):String {
    var localizedResourceClassName:String = cd.fullClassName;
    var locale:String = getLocale();
    if (locale !== getDefaultLocale()) {
      localizedResourceClassName += "_" + locale;
    }
    return localizedResourceClassName;
  }

}
}