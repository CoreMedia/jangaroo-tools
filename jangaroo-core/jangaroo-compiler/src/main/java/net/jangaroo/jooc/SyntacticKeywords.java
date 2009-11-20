package net.jangaroo.jooc;

/**
 * Identifiers with special meaning become keywords in certain syntactic contexts:

    * In a for-each-in statement between the 'for' token and the '(' token:
      each
    * In a function definition between the 'function' token and an identifier token:
      get set
    * As the first word of a directive:
      namespace include
    * In an attribute list or wherever an attribute list can be used:
      dynamic final native override static

It is a syntax error to use a syntactic keyword in a context where it is treated as a keyword:

namespace = "hello"
namespace()

In these cases, the grammar requires an identifier after the namespace keyword.

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

}
