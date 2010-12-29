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

public class localization {

  /**
   * The supported locales of this Jangaroo application, represented as an Array of String locales.
   * The first element of this list is used as the default locale to use when all other attempts to determine
   * a locale fail.
   * Must be set before loading jangaroo-modules.js.
   */
  public static var supportedLocales:Array;

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
