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
 * Retrieve or create the package with the given fully qualified name.
 * Although in AS3, packages do not have a runtime representation, in Jangaroo, they have.
 * @param packageName the fully qualified name of the package to retrieve or create
 * @return * the package object retrieved or created.
 * @see joo.getQualifiedObject()
 */
[Native(amd)]
public native function getOrCreatePackage(packageName : String) : *;


}
