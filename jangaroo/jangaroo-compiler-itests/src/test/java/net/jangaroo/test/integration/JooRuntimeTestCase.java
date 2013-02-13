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

package net.jangaroo.test.integration;

import net.jangaroo.jooc.Jooc;
import net.jangaroo.test.JooTestCase;
import org.mozilla.javascript.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.MessageFormat;

/**
 * A JooTestCase to be executed at runtime
 * <p/>
 * This class adds a global evaluation context and methods to load and execute script code.
 *
 * @author Andreas Gawecki
 */
public abstract class JooRuntimeTestCase extends JooTestCase {

  protected Global global;
  private Context cx;

  private static final String REQUIRE_JS_FILE_PATH = "/requirejs/r.js";

  public static String jsFileName(final String qualifiedJooClassName) {
    return qualifiedJooClassName.replace('.', File.separatorChar) + Jooc.OUTPUT_FILE_SUFFIX;
  }

  public static String asFileName(final String qualifiedJooClassName) {
    return qualifiedJooClassName.replace('.', File.separatorChar) + Jooc.AS_SUFFIX;
  }

  public JooRuntimeTestCase(String name) {
    super(name);
  }

  static public class Global extends ScriptableObject {
    private String jsDir;

    public String getClassName() {
      return "global";
    }

    public Object eval(Context cx, String script) throws Exception {
      //System.out.print("evaluating script '" + script + "': ");
      return cx.evaluateString(this, script, "", 1, null);
    }

    public static void setTimeout(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws Exception {
      Function fn = (Function) args[0];
      Object n = args[1];
      // no invoke later yet...
      fn.call(cx, thisObj, fn, new Object[]{});
    }

    public void printJsResult(Object result) throws Exception {
      System.out.println(Context.toString(result) + " (" + result.getClass().getName() + ")");
    }

    public String getJsDir() {
      return jsDir;
    }

    public void setJsDir(final String jsDir) {
      this.jsDir = jsDir;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static void print(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
      for (int i = 0; i < args.length; i++) {
        if (i > 0)
          System.out.print(" ");
        // Convert the arbitrary JavaScript value into a string form.
        String s = Context.toString(args[i]);
        System.out.print(s);
      }
      System.out.println();
    }

    @SuppressWarnings("UnusedDeclaration")
    public static void load(Context cx, Scriptable thisObj, Object[] args,
            Function funObj) throws IOException {
      Global global = (Global) getTopLevelScope(thisObj);
      for (Object arg : args) {
        String jsFileName = Context.toString(arg);
        global.doLoad(cx, jsFileName);
      }
    }

    public void doLoad(Context cx, String jsFileName) throws IOException {
      System.out.println("loading script " + jsFileName);
      Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(jsFileName), "UTF-8"));
      cx.evaluateReader(this, reader, jsFileName, 1, null);
    }

    public void require_(Context cx, String moduleQName, String varName) throws Exception {
      eval(cx, MessageFormat.format("require([\"{0}\"]); export(\"{1}\", require(\"{0}\"));", moduleQName, varName));
    }

    public void import_(Context cx, String qualifiedJooClassName) throws Exception {
      System.out.println("importing class " + qualifiedJooClassName);
      String moduleQName = "as3/" + qualifiedJooClassName.replace(".", "/");
      require_(cx, moduleQName, qualifiedJooClassName);
    }

  }

  static {
    ContextFactory.initGlobal(new ContextFactory() {
      @Override
      protected boolean hasFeature(Context cx, int featureIndex) {
        switch (featureIndex) {
          case Context.FEATURE_RESERVED_KEYWORD_AS_IDENTIFIER:
            return true;
          case Context.FEATURE_STRICT_MODE:
            return false;
        }
        return super.hasFeature(cx, featureIndex);
      }
    });
  }

  protected void setUp() throws Exception {
    super.setUp();
    global = new Global();
    cx = ContextFactory.getGlobal().enterContext();
    cx.setOptimizationLevel(-1); // disable compilation to prevent "out of PermGen space", not really slower, anyway
    cx.setLanguageVersion(Context.VERSION_1_8);
    cx.initStandardObjects(global);
    global.defineFunctionProperties(new String[]{"setTimeout", "load", "print"}, Global.class, ScriptableObject.EMPTY);
    global.defineProperty("window", global, ScriptableObject.EMPTY);
    global.eval(cx, "navigator = undefined;");
    Scriptable argsObj = cx.newArray(global, new Object[] {});
    global.defineProperty("arguments", argsObj, ScriptableObject.DONTENUM);
    global.eval(cx, "function export(qName, value) { var parts=qName.split('.');for (var current=this,i=0;i<parts.length-1;++i)current=(current[parts[i]]=current[parts[i]]||{});if (value._) { value = value._; } else { print('exported interface ' + qName + ' / ' + JSON.stringify(value.interfaces)); } current[parts[parts.length-1]]=value;}");
    load(destinationDir + REQUIRE_JS_FILE_PATH);
    global.eval(cx, "require.config({ baseUrl: \"" + destinationDir.replace(File.separatorChar, '/') + "/amd\"});");
  }

  protected void tearDown() throws Exception {
    super.tearDown();
    Context.exit();
  }

  protected void load(String jsFileName) throws Exception {
    global.doLoad(cx, jsFileName);
  }

  protected void printJsResult(Object result) throws Exception {
    global.printJsResult(result);
  }

  protected void import_(String qualifiedJooClassName) throws Exception {
    global.import_(cx, qualifiedJooClassName);
  }

  protected void require_(String moduleQName, String varName) throws Exception {
    global.require_(cx, moduleQName, varName);
  }

  protected void initClass(String qualifiedJooClassName) throws Exception {
    //eval(Jooc.CLASS_LOADER_FULLY_QUALIFIED_NAME + ".init(" + qualifiedJooClassName + ")");
  }

  protected void complete() throws Exception {
    //eval(Jooc.CLASS_LOADER_FULLY_QUALIFIED_NAME + ".complete();");
  }

  protected void runClass(String qualifiedJooClassName) throws Exception {
    import_(qualifiedJooClassName);
    complete();
    //initClass(qualifiedJooClassName);
    eval(qualifiedJooClassName + ".main()");
  }

  protected Object eval(String script) throws Exception {
    return global.eval(cx, script);
  }

  protected void expectString(String expected, String script) throws Exception {
    Object result = eval(script);
    String actual = null;
    if (result instanceof String)
      actual = (String) result;
    else {
      if (result == null) {
        fail("expected string result, found null");
      } else {
        fail("expected string result, found: " + result.getClass().getName());
      }
    }
    if (!expected.equals(actual)) {
      if (actual.length() == expected.length()) {
        int i = 0;
        while (actual.charAt(i) == expected.charAt(i))
          i++;
        fail("expected: \"" + expected + "\", found: \"" + actual + "\"\nstrings differ at index " + i + "");
      } else
        fail("expected length " + expected.length() + ": \"" + expected +
                "\", found length " + actual.length() + ": \"" + actual + "\"");
    }
  }

  protected void expectSubstring(String expected, String script) throws Exception {
    Object result = eval(script);
    String actual = null;
    if (result instanceof String)
      actual = (String) result;
    else fail("expected string result, found: " + result.getClass().getName());
    if (!actual.contains(expected)) {
      fail("expected substring '" + expected + "' not found within: " + unparse(result));
    }
  }

  protected void expectNumber(double expected, String script) throws Exception {
    Object result = eval(script);
    double actual = 0;
    if (result instanceof Number)
      actual = ((Number) result).doubleValue();
    else fail("expected numeric result, found: " + result.getClass().getName() + ": " + unparse(result));
    assertEquals(expected, actual, 0.00000000001);
  }

  protected void expectBoolean(boolean expected, String script) throws Exception {
    Object result = eval(script);
    boolean actual = false;
    if (result instanceof Boolean)
      actual = (Boolean) result;
    else fail("expected boolean result, found: " + result.getClass().getName());
    assertEquals(expected, actual);
  }

  protected void expectException(String script) throws Exception {
    try {
      Object result = eval(script);
      fail("expected exception, but got regular result: " + unparse(result));
    } catch (Exception e) {
      // ok
    }
  }

  private String unparse(final Object result) {
    if (result instanceof String) {
      return "'" + result + "'";
    }
    return result.toString();
  }

  public void testRhino() throws Exception {
    expectNumber(23, "23");
    expectNumber(23.1, "22.1+1");
  }

}
