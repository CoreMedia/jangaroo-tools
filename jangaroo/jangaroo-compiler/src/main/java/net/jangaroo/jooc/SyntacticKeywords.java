/*
 * Copyright 2008 CoreMedia AG
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

package net.jangaroo.jooc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Identifiers with special meaning become keywords in certain syntactic contexts:
 * <p/>
 * In a for-each-in statement between the 'for' token and the '(' token:
 * each
 * In a function definition between the 'function' token and an identifier token:
 * get set
 * As the first word of a directive:
 * namespace include
 * In an attribute list or wherever an attribute list can be used:
 * dynamic final native override static
 * <p/>
 * It is a syntax error to use a syntactic keyword in a context where it is treated as a keyword:
 * <p/>
 * namespace = "hello"
 * namespace()
 * <p/>
 * In these cases, the grammar requires an identifier after the namespace keyword.
 */
public interface SyntacticKeywords {

  static final String ASSERT = "assert";
  static final String DYNAMIC = "dynamic";
  static final String EACH = "each";
  static final String FINAL = "final";
  static final String GET = "get";
  static final String INCLUDE = "include";
  static final String NAMESPACE = "namespace";
  static final String NATIVE = "native";
  static final String OVERRIDE = "override";
  static final String SET = "set";
  static final String STATIC = "static";
  static final String VIRTUAL = "virtual";

  static final Set<String> RESERVED_WORDS = new HashSet<String>(Arrays.asList("int", "uint"));

}
