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

// JangarooScript runtime support. Author: Frank Wienberg

package joo {

[Native(amd)]
public class MemberDeclaration {

  public static const
          METHOD_TYPE_GET : String = "get",
          METHOD_TYPE_SET : String = "set",
          MEMBER_TYPE_VAR : String = "var",
          MEMBER_TYPE_CONST : String = "const",
          MEMBER_TYPE_FUNCTION : String = "function",
          MEMBER_TYPE_CLASS : String = "class",
          MEMBER_TYPE_INTERFACE : String = "interface",
          MEMBER_TYPE_NAMESPACE : String = "namespace",
          NAMESPACE_PRIVATE : String = "private",
          NAMESPACE_INTERNAL : String = "internal",
          NAMESPACE_PROTECTED : String = "protected",
          NAMESPACE_PUBLIC : String = "public",
          STATIC : String = "static",
          FINAL : String = "final",
          NATIVE : String = "native",
          OVERRIDE : String = "override",
          VIRTUAL : String = "virtual";

  public function MemberDeclaration(config:Object) {
  }

  public native function get memberType():String;

  public native function get getterOrSetter():String;

  public native function get memberName():String;

  /**
   * The metadata (annotations) associated with this member.
   */
  public native function get metadata():Object;

  public native function getQualifiedName():String;

  public native function isPrivate():Boolean;

  public native function isStatic():Boolean;

  public native function isFinal():Boolean;

  public native function isNative():Boolean;

  public native function isOverride():Boolean;

  public native function isMethod():Boolean;

}
}
