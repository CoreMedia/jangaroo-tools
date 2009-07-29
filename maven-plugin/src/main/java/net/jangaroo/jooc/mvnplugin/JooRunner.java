/*
 * Copyright (c) 2009, CoreMedia AG, Hamburg. All rights reserved.
 */
package net.jangaroo.jooc.mvnplugin;


import org.mozilla.javascript.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 *
 */
public class JooRunner {
  private Context context;
  private final Scriptable scope;
  private final TraceOutputHandler outputHandler;

  static public class Global extends ScriptableObject {

    public TraceOutputHandler outputHandler;

    public Global(final TraceOutputHandler outputHandler) {
      this.outputHandler = outputHandler;
    }

    public String getClassName() {
      return "Global";
    }


    public static void print(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
      trace(cx, thisObj, args, funObj);
    }

    public static void trace(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
      for (int i = 0; i < args.length; i++) {
        if (i > 0)
          ((Global)thisObj).outputHandler.print(" ");
        // Convert the arbitrary JavaScript value into a string form.
        String s = Context.toString(args[i]);
        ((Global)thisObj).outputHandler.print(s);
      }
    }    
  }

  /**
   * Creates a new JooRunner, every output is written to system.out
   */
  public JooRunner() {
    this(new TraceOutputHandler() {
      public void print(String input) {
        System.out.print(input);
      }

      public void println(String input) {
        System.out.println(input);
      }
    });
  }

  /**
   * creates a new Joo runner with the given implementation of TraceOutput
   *
   * @param outputHandler The Output handler
   */
  public JooRunner(final TraceOutputHandler outputHandler) {
    this.outputHandler = outputHandler;
    Global global = new Global(this.outputHandler);
    global.defineFunctionProperties(new String[]{"trace", "print"},
        Global.class, ScriptableObject.DONTENUM);
    context = ContextFactory.getGlobal().enterContext();

    context.setLanguageVersion(Context.VERSION_1_7);
    scope = context.initStandardObjects(global, false);
  }

  /**
   * Adds the object with the variable name to ghe global scope of the current runner
   * @param value the object
   * @param variableName the JavaScript variable name of this object
   */
  public void addInstanceToScope(Object value, String variableName) {
    Context ctx = ContextFactory.getGlobal().enterContext(context);
    try {
      Scriptable instance = Context.toObject(value, scope);
      scope.put(variableName, scope, instance);
    } finally {
      Context.exit();
    }

  }


  /**
   * Load additional code into the JavaScript context. The provided reader is read until
   * execution and closed afterwards.
   *
   * @param reader the reader providing the code
   * @param name   an identifying name of the code (normally the file name)
   * @throws IOException
   * @since upcoming
   */
  public void load(final Reader reader, final String name) throws IOException {
    if (reader == null) {
      throw new IllegalArgumentException("The reader is null");
    }

    ContextFactory.getGlobal().call(new ContextAction() {
      public Object run(Context cx) {
        try {
          cx.evaluateReader(scope, reader, name, 1, null);
        } catch (IOException e) {
          throw new RuntimeException("Cannot evaluate JavaScript code of " + name, e);
        }finally {
          try {
            reader.close();
          } catch (IOException e) {
            throw new RuntimeException("Cannot close reader while parsing: " + name, e);
          }
        }
        return null;
      }
    });

  }

  /**
   * Run the given script.
   * @param script The script
   */
  public void run(final String script) {
    ContextFactory.getGlobal().call(new ContextAction() {
      public Object run(Context cx) {
        cx.evaluateString(scope, script, "main", 1, null);
        return null;
      }
    });
  }

  /**
   * run the given script and write the output to the given writer. The writer will be closed.
   * @param writer  the writer
   * @param script  the script
   * @throws IOException
   */
  public void run(final Writer writer, final String script) throws IOException {
    Context ctx = ContextFactory.getGlobal().enterContext(context);
    try {
      String result = (String) ctx.evaluateString(scope, script, "main", 1, null);
      writer.write(result);

    } finally {
      writer.close();
      Context.exit();
    }

  }


  public static void main(String[] args) throws Exception {
    JooRunner run = new JooRunner();
    run.run(" trace('test');\n");

  }

  /**
   * Interface for handling the trace and print calls
   */
  public interface TraceOutputHandler {
    /**
     * Print the input
     * @param input the input
     */
    public void print(String input);

    /**
     * Print a line with the input
     * @param input
     */
    public void println(String input);
  }    
}
