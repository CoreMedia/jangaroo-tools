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
 * If not in debug mode, load the script containing all code of the module given by <code>groupId</code>
 * and <code>artifactId</code>, using <code>loadScript()</code>.
 * <p>If in debug mode, no all classes are automatically loaded by <code>DynamicClassLoader</code>.</p>
 * @param groupId the Maven <code>groupId</code> of the module to load
 * @param artifactId the Maven <code>artifactId</code> of the module to load
 *
 * @see joo.debug
 * @see joo.loadScript
 * @see joo.DynamicClassLoader
 */
public native function loadModule(groupId:String, artifactId:String):void;

}
