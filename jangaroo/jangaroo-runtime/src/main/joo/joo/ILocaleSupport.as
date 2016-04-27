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
 * This interface provides the foundation of localization support with ExtJS.
 * Its singleton instance <code>joo.localeSupport</code> maintain a current locale and loads a locale-specific subclass of any
 * class ending with <code>_properties</code>.
 * <p>The locale is saved in and retrieved from a Cookie whose name, path, and domain
 * can be configured.</p>
 * <p>In your main HTML file, load <code>joo/before-ext-load.js</code>, before loading Ext and the Jangaroo Runtime.
 * Activate locale-specific resource bundle overrides loading as follows:</p>
 * <pre>
 * new joo.ILocaleSupport(["en", "de"]);
 * </pre>
 * <p>You can create resource bundle classes by creating <code>*_<i>locale</i>.properties</code>
 * files below the <code>src/main/joo</code> directory. These files are translated to JavaScript
 * code by the Jangaroo properties compiler (Maven goal: <code>properties</code>).
 * Each resource bundle must consist of properties files for each supported locale, where
 * the default locale suffix is suppressed.
 * For example for supported locales <code>["en", "de"]</code> (which makes <code>"en"</code>
 * the default locale), you need properties files <code>My.properties</code> and <code>My_de.properties</code>.</p>
 * <p>To change the locale, your application code must call</p>
 * <pre>
 * joo.localeSupport.setLocale(newLocale);
 * </pre>
 *
 * @see #ILocaleSupport
 * @see #getLocale
 * @see #setLocale
 *
 * @author Manuel Ohlendorf
 * @author Frank Wienberg
 */
public interface ILocaleSupport {

  /**
   * Set before any resource bundle is loaded. Default is <code>"joo.locale"</code>
   * @see #getLocale
   */
  function get localeCookieDomain():String;

  //noinspection JSUnusedGlobalSymbols
  function getSupportedLocales():Array;

  function getDefaultLocale():String;

  function readLocaleFromCookie():String;

  /**
   * Sets the current locale to the given locale or, if the given locale is not supported,
   * to the longest match in the supported locales, or the default locale if there is no match.
   * @param newLocale the locale to use for resource bundle class loading
   * @return String the supported locale that has actually been set
   * @see #ILocaleSupport
   */
  function setLocale(newLocale:String):String;

  /**
   * Find the supported locale with the longest match against the given locale
   */
  function findSupportedLocale(locale:String):String;

  /**
   * Return the locale currently used for resource bundle loading.
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
   * @see #ILocaleSupport
   * @see #setLocale
   */
  function getLocale():String;

}
}