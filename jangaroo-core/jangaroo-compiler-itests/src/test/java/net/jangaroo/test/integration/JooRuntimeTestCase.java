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

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;

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

  private static final String CLASS_JS_FILE_PATH = "jangaroo-runtime-debug.js";
//    Jooc. + /* "-debug" + */Jooc.OUTPUT_FILE_SUFFIX;

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

    public String getClassName() {
      return "global";
    }

    private String jsDir;

    public String getJsDir() {
      return jsDir;
    }

    public void setJsDir(final String jsDir) {
      this.jsDir = jsDir;
    }

    static public void trace(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
      for (int i = 0; i < args.length; i++) {
        if (i > 0)
          System.out.print(" ");
        // Convert the arbitrary JavaScript value into a string form.
        String s = Context.toString(args[i]);
        System.out.print(s);
      }
      System.out.println();
    }

    public static void joo__loadScript(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws Exception {
      Global global = (Global) thisObj;
      for (Object arg : args) {
        // Convert the arbitrary JavaScript value into a string form.
        String s = Context.toString(arg);
        global.load(cx, s);
      }
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

    public Object load(Context cx, File jsFile) throws Exception {
      String jsFileName = jsFile.getAbsolutePath();
      System.out.println("loading script " + jsFileName);
      FileReader reader = new FileReader(jsFile);
      return cx.evaluateReader(this, reader, jsFileName, 1, null);
    }

    public void load(Context cx, String jsFileName) throws Exception {
      load(cx, new File(getJsDir(), jsFileName));
    }


    public void import_(Context cx, String qualifiedJooClassName) throws Exception {
      eval(cx, "joo.classLoader.import_('" + qualifiedJooClassName + "')");
    }

    public Object eval(Context cx, String script) throws Exception {
      //System.out.print("evaluating script '" + script + "': ");
      Reader reader = new StringReader(script);
      Object result = cx.evaluateReader(this, reader, "", 1, null);
      return result;
    }
  }

  protected void setUp() throws Exception {
    super.setUp();
    global = new Global();
    cx = ContextFactory.getGlobal().enterContext();
    cx.setLanguageVersion(Context.VERSION_1_5);
    cx.initStandardObjects(global);
    global.defineFunctionProperties(new String[]{"trace"},  Global.class, ScriptableObject.DONTENUM);
    global.defineFunctionProperties(new String[]{"joo__loadScript"},  Global.class, ScriptableObject.EMPTY);
    global.defineFunctionProperties(new String[]{"setTimeout"},  Global.class, ScriptableObject.EMPTY);
    global.defineProperty("window", global, ScriptableObject.EMPTY);
    global.setJsDir(destinationDir);
    load(CLASS_JS_FILE_PATH);
    eval("joo.classLoader.urlPrefix = '/'");
  }

  protected void tearDown() throws Exception {
    super.tearDown();
    Context.exit();
  }

  protected void load(String jsFileName) throws Exception {
    global.load(cx, jsFileName);
  }

  protected void printJsResult(Object result) throws Exception {
    global.printJsResult(result);
  }

  protected Object load(File jsFile) throws Exception {
    return global.load(cx, jsFile);
  }

  protected Object loadAll() throws Exception {
    return loadAll(new File(global.getJsDir()));
  }

  protected Object loadAll(final File dir) throws Exception {
    File[] files = dir.listFiles();
    Object lastResult = null;
    for (File file :files) {
      if (file.isDirectory()) {
        lastResult = loadAll(file);
      } else if (file.getName().endsWith(".js")) {
        lastResult = load(file);
      }
    }
    return lastResult;
  }

  protected void import_(String qualifiedJooClassName) throws Exception {
    global.import_(cx, qualifiedJooClassName);
  }

  protected void initClass(String qualifiedJooClassName) throws Exception {
    eval(Jooc.CLASS_LOADER_FULLY_QUALIFIED_NAME + ".init(" + qualifiedJooClassName + ")");
  }

  protected void complete() throws Exception {
    eval(Jooc.CLASS_LOADER_FULLY_QUALIFIED_NAME + ".complete();");
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
    else fail("expected string result, found: " + result.getClass().getName());
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
