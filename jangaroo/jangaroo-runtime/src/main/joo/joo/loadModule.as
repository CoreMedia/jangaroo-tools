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
 * In Jangaroo 3, calling joo.loadModule() in your &#42;.module.js is no longer necessary.
 * This function has only been kept to keep script still using it from failing, and does nothing.
 * If not in debug mode, all scripts containing the aggregated code of a module are
 * <code>require</code>d automatically.
 */
[Native]
public native function loadModule(groupId:String, artifactId:String):void;

}
