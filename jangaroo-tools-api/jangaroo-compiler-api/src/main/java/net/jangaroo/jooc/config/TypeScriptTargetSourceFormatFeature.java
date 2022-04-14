package net.jangaroo.jooc.config;

public interface TypeScriptTargetSourceFormatFeature {
  /**
   * Simplified syntax for Ext classes using <code>this</code> before <code>super()</code>
   * constructor call. Instead of wrapping all code that does so inside an
   * immediately-evaluating arrow function expression which nested inside the <code>super</code>
   * call, an alias for <code>this</code> is defined (named <code>this$</code>), and the
   * alias assignment is forced to be accepted by the TypeScript compiler using
   * <code>// @ts-expect error</code>.
   */
  long SIMPLIFIED_THIS_USAGE_BEFORE_SUPER_CONSTRUCTOR_CALL = 1L;

  /**
   * Simplify <code>(as(foo, Foo)).bar</code> to <code>(foo as Foo).bar</code>,
   * i.e. leave out runtime checks that, when failing, would have caused an NPE anyway.
   * The ActionScript <code>as</code> operator returns <code>null</code> when the
   * given expression is not of the given type. So when (immediately) de-referencing
   * the result, the JavaScript equivalent of a NullPointerException would be raised.
   * If the developer really wanted to raise an error when the type is not matched,
   * they should have used a type cast instead, which would then throw a
   * <code>TypeError</code>.
   * This leads to the assumption that in this case, <code>as</code> was meant as a
   * compile-time type assertion only, which does not exist in ActionScript.
   * However, TypeScript <em>only</em> supports compile-time type assertions, so
   * to emulate ActionScript semantics, Jangaroo introduced a utility function
   * <code>as(expr, type)</code>.
   * The "simplified as-expressions" flag tells the Jangaroo compiler to convert
   * immediately de-referenced <code>as</code>-expression to type assertions instead
   * of calls to the Jangaroo utility function <code>as()</code>. This saves
   * runtime overhead and a value-dependency on the target type, which can help to
   * resolve cyclic compilation unit dependencies (which are not allowed in ECMAScript).
   * The only semantic difference is that when the expression is <em>not</em> of
   * the given type, instead of an NPE, either the error is raised that the property
   * that is then de-referenced does not exist, or, by coincidence, a property of
   * the given name exists nevertheless, which may lead to consequential errors.
   * If you want to switch on the flag in general, but prevent the conversion for
   * certain code, simply extract the as-expression into a local variable or even
   * better use a type cast instead.
   */
  long SIMPLIFIED_AS_EXPRESSIONS = 2L;

  /**
   * Generate static blocks. This ECMAScript feature is supported since TypeScript 4.4, but has
   * a serious bug, namely that forward-references to static methods lead to a type error.
   * This bug is fixed in TypeScript 4.7, so if your static code block ActionScript code
   * contains forward references, you should only switch it on when targeting TypeScript 4.7+.
   */
  long STATIC_BLOCKS = 4L;
}
