/*
 * Copyright 2013 CoreMedia AG
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

[Native(amd)]
/**
 * Load the ActionScript class corresponding to the given qualified name
 * and hand the loaded class to the given callback function.
 * Computes the AMD module name from the qualified name, calls <code>require()</code>,
 * and retrieves the class from the resulting compilation unit.
 * If no callback function is given, tries to call synchronous <code>require()</code>,
 * which fails if the class has not been loaded in advance.
 *
 * @param qualifiedName the qualified name for which to load the corresponding class
 * @param callback the function to call with the corresponding class when it is loaded
 * @return null if a callback function is given, or the retrieved class if no callback function is given
 */
public native function loadClass(qualifiedName: String, callback: Function = null):Class;


}
