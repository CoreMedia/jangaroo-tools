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

[Native(amd)]
/**
 * Return a qualified object, given by its dot-separated qualified name.
 * Usually used to retrieve a package, class, interface, or package-scoped member by its fully qualified name.
 * @param qualifiedName the qualified name for which to retrieve the qualified object
 * @return Any object found under the qualified name or undefined.
 * @see joo.getOrCreatePackage
 */
public native function getQualifiedObject(qualifiedName : String) : *;


}
