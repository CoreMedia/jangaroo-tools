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

[Native]
public class localization {

  /**
   * The supported locales of this Jangaroo application, represented as an Array of String locales.
   * The first element of this list is used as the default locale to use when all other attempts to determine
   * a locale fail.
   * Must be set before loading jangaroo-application.js.
   */
  public static var supportedLocales:Array;

  /**
   * The locales preferred by the current user, in order of preference, which will be used to determine the
   * best supported locale if no locale Cookie is set.
   * <p>
   * Although every browser allows to set the preferred languages and sends them with every request (header
   * <code>Accept-Language</code>), these are not directly available from JavaScript.
   * Instead, you need a JSONP Web service that returns code that sets this Jangaroo configuration.
   * For example, when the <code>Accept-Language</code> header contains <code>en,de_DE;q=0.7</code>, it
   * should return
   * </p>
   * <pre>
   * joo = { localization: { preferredLocales: ["en", "de_DE"] } };
   * </pre>
   * Assuming this service is available under the URL <code>service-path/jangaroo-localization.js</code>,
   * you load its response using a script element:
   * <pre>
   * &lt;script type="text/javascript" src="service-path/jangaroo-localization.js">&lt;/script>
   * </pre>
   * Place this script element before <code>&lt;script src="joo/jangaroo-application.js"></code>, and your
   * Jangaroo application should always use the best fit between the preferred locales the user has configured
   * in her browser preferences and the locales supported by the Jangaroo application.
   *
   * @see #supportedLocales
   */
   public static var preferredLocales:Array;

  /**
   * The name of the Cookie to load and store locale information on the client (default: <code>"joo.locale"</code>).
   */
  public static var localeCookieName:String;

  /**
   * The path of the Cookie to load and store locale information on the client (default: <code>window.location.pathname</code>).
   */
  public static var localeCookiePath:String;

  /**
   * The domain of the Cookie to load and store locale information on the client (default: <code>null</code>).
   */
  public static var localeCookieDomain:String;
}

}
