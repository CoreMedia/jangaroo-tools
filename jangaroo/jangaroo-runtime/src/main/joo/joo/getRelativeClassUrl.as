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
 * Return a URL relative to <code>joo.baseUrl</code> to load the class given by its fully qualified name.
 * You can use this URL with <code>joo.loadScript</code> or <code>joo.loadScriptAsync</code>.
 * This function is used internally and usually not needed by application code.
 * @param qualifiedName the qualified name of a class, interface, or function for which to compute the relative URL
 * @return  the <code>baseUrl</code>-relative URL of the given qualified class, interface, or function
 * @see joo.baseUrl
 */
[Native]
public native function getRelativeClassUrl(qualifiedName:String):String;


}
