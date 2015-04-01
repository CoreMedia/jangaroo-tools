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
 * Add the given URL to the list of scripts to load when the application starts.
 * If in debug mode, use the optionally given debug URL instead.
 * Like in Jangaroo 2, the scripts are loaded sequentially.
 * This function is deprecated, because the preferred way to load "native" JavaScript in
 * Jangaroo 2 is to create ActionScript API annotated with <code>[Native(amd)]</code>.
 * <p>
 * This tells Jangaroo to use RequireJS to load an AMD module with a name derived from
 * the fully-qualified ActionScript name. For example, using the annotation on a class
 * called <code>com.acme.MyClass</code> requires the AMD module named
 * <code>as3/com/acme/MyClass</code>, which would, in a Jangaroo Maven module, be located
 * in <code>src/main/resources/META-INF/resources/amd/as3/com/acme/MyClass.js</code>.
 * </p>
 * <p>
 * If you want another AMD name, e.g. because you integrate a 3rd-party library with
 * AMD names that do not fit ActionScripts package and class naming patterns, you can
 * assign the desired name to the annotaion parameter:
 * <code>[Native(amd="Acme/myClass")]</code>
 * </p>
 * <p>
 * If one AMD if represented as multiple ActionScript compilation units, you can tell
 * the Jangaroo compiler the property path relative to the AMD module result object by
 * adding a second unnamed parameter:
 * <code>[Native(amd="Acme", "myClass")]</code> would load the AMD <code>Acme</code>
 * (<code>src/main/resources/META-INF/resources/amd/Acme.js</code>) and, of the resulting
 * object, use the property <code>myClass</code> to reference the annotated class.
 * Annotating <code>MyOtherClass</code> with
 * <code>[Native(amd="Acme", "myOtherClass")]</code>
 * would load the same AMD module, but use the property <code>myOtherClass</code> to
 * reference <code>MyOtherClass</code>. Like any AMD, the module is loaded exactly once
 * and returns (or fills) one object, which should in this example expose both properties
 * <code>myClass</code> and <code>myOtherClass</code>.
 * </p>
 * <p>
 * If you need to integrate JavaScript code that is <em>not</em> an AMD (unfortunately,
 * there is still a lot code like that around), please refer to the excellent documentation
 * of RequireJS, especially "shim" configuration.
 * </p>
 * 
 * @param url the script source URL, relative to <code>joo.baseUrl</code>, to use in production (= non-debug) mode
 * @param debugUrl the script source URL, relative to <code>joo.baseUrl</code>, to use in debug mode
 *
 * @see joo.baseUrl
 * @see joo.debug
 * @see http://requirejs.org/docs/api.html#config-shim
 */
[Native]
public native function loadScript(url:String, debugUrl:String = null):void;

}
