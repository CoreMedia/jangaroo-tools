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
 * Load a script from the given URL by writing a script element into the document. This downloads and
 * executes the script immediately after the current script block terminates.
 * @param url the script source URL, relative to <code>joo.baseUrl</code>, to use in production (= non-debug) mode
 * @param debugUrl the script source URL, relative to <code>joo.baseUrl</code>, to use in debug mode
 *
 * @see joo.baseUrl
 * @see joo.debug
 */
public native function loadScript(url:String, debugUrl:String):void;

}
