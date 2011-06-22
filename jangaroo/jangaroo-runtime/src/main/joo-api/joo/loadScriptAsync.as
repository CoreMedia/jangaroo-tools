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
 * Load a script from the given URL by creating a DOM script element. In most browsers,
 * this allows download of scripts in parallel and leads to execution using a first-come-first-serve
 * strategy.
 * @param url the script source URL, relative to <code>joo.baseUrl</code>, to use in production (= non-debug) mode
 * @return the generated script element
 *
 * @see joo.baseUrl
 */
public native function loadScriptAsync(url : String) : Object;

}
