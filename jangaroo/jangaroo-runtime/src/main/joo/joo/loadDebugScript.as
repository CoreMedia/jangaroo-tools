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
 * @deprecated
 * If in debug mode, add the given URL to the list of scripts to load when the application starts.
 * <p>This method allows a convenient and more readable notation for <code>loadScript(null, debugUrl)</code>.</p>
 * <p>Deprecated like <code>loadScript()</code>, for rationale, see there.</p>
 * <p>
 *
 * @param debugUrl the script source URL, relative to <code>baseUrl</code>, to use in debug mode
 *
 * @see joo.loadScript
 * @see joo.baseUrl
 * @see joo.debug
 */
[Native]
public native function loadDebugScript(debugUrl:String):void;

}
