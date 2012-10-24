package net.jangaroo.jooc.mvnplugin.test;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Executes unit tests using Java's {@link javax.script.ScriptEngine inbuilt javascript} engine (Rhino). Requires Java 6.
 * @requiresDependencyResolution test
 * @goal run-test-rhino
 * @phase test
 */
public class RhinoJsTestRunner extends TestMojoBase {

  /**
   * Time in milliseconds to wait for the test to finish. Default is 30000ms.
   *
   * @parameter
   */
  private int timeout = 30000;

  /**
   * Additional javascript to be invoked prior to invoking the tests. Can be used for setting up additional
   * infrastructure. Example
   * <code>
   * console.log('Setting up tests');
   * </code>
   *
   * @parameter default-value=""
   */
  private String setUpScript;

  /**
   * If set to true, then additional messages will be logged. For debugging purposes.
   * @parameter default-value="false"
   */
  private boolean verbose;


  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    if( isSkipTests() ) {
      getLog().info("Skipping tests");
      return;
    }

    try {

      unpackResources();
      runTests();
    }
    catch (MojoFailureException e) {
      throw e;
    }
    catch (MojoExecutionException e) {
      throw e;
    }
    catch (Exception e) {
      throw new MojoExecutionException(e.toString(), e);
    }
  }


  // ===========

  protected void runTests() throws Exception {

    ScriptEngineManager factory = new ScriptEngineManager();
    ScriptEngine engine = factory.getEngineByName("JavaScript");
    ScriptContext context = engine.getContext();

    JavascriptConsole console = new JavascriptConsole(getLog(), verbose);
    JavascriptImporter importer = new JavascriptImporter(engine, getTestOutputDirectory(), console);
    JavascriptTimer timer = new JavascriptTimer(getLog());
    JavascriptTestListener listener = new JavascriptTestListener(getLog());

    context.setAttribute("mojoScriptImporter", importer, ScriptContext.ENGINE_SCOPE);
    context.setAttribute("mojoTestListener", listener, ScriptContext.ENGINE_SCOPE);
    context.setAttribute("mojoConsole", console, ScriptContext.ENGINE_SCOPE);
    context.setAttribute("mojoTimer", timer, ScriptContext.ENGINE_SCOPE);

    invokeScript(engine, importer);
    waitForResult(listener);
  }


  // ==============

  /**
   * Invokes bootstrap javascript
   */
  private void invokeScript(ScriptEngine engine, JavascriptImporter importer) throws Exception {


    // -----  defining patches for jangaroo-runtime
    importer.importScript("classpath:/net/jangaroo/jooc/mvnplugin/test/rhino-jangaroo-runtime.js", false);

    // -----  minimal window emulation
    // TODO: Make this configurable/exchangeable?
    importer.importScript("classpath:/net/jangaroo/jooc/mvnplugin/test/rhino-window-minimal.js", false);

    // -----  importing jangaroo
    importer.importScript("joo/jangaroo-application.js", false);

    // ----- invoke custom setup script
    if( setUpScript != null ) {
      engine.eval(setUpScript);
    }

    // -----  defining invoker (other scripts will be imported dynamically)
    String testClassesJson =  getTestClassNamesJSON();
    String invokerScript = "var consumeTestResult = function(testSuite, testResult, testResultXml) {mojoTestListener.finished(testResultXml);};";
    invokerScript += "joo.classLoader.run(" +
            "\"net.jangaroo.joounit.runner.DefaultJooTestRunner\", " +
            "{'tests': " +testClassesJson+ ", 'onComplete': consumeTestResult});";
    engine.eval(invokerScript);
  }

  /**
   * Waits for the test result to be available
   */
  private void waitForResult(JavascriptTestListener listener ) throws Exception {

    getLog().info("Started tests and waiting for the result ...");
    // TODO implement with java synchronisation rather than polling
    long begin = System.currentTimeMillis();
    long end = begin+timeout;
    do {
      if( listener.getTestResultXml() != null ) {
        // received test result
        break;
      }
      Thread.sleep(1000);
    }
    while( System.currentTimeMillis() < end );

    if( listener.getTestResultXml() == null ) {
      throw new MojoFailureException("No test result received within timeout of "+timeout+" ms");
    }
    else {

      String testXml = listener.getTestResultXml();
      handleResult(testXml);
    }
  }



  // =====================================


  /**
   * A javascript/java bridge for scheduling and cancelling tasks
   */
  private static class JavascriptTimer {

    private volatile int count = 0;
    private final Log log;
    private final Timer timer = new Timer();
    private final Map<String, TimerTask> scheduled = new HashMap<String, TimerTask>();

    public JavascriptTimer(Log log) {
      this.log = log;
    }

    public void cancel(String id) {

      log.debug("javascript timer> Cancelling execution of " + id);
      TimerTask task = scheduled.get(id);
      if( task != null ) {
        task.cancel();
        scheduled.remove(id);
      }
    }

    public String schedule(final Runnable runnable, long time, final boolean repeat) {

      final String id = "_timer"+(count++);
      TimerTask task = new TimerTask() {
        @Override
        public void run() {

          //log.info("timer> Executed "+id);
          runnable.run();
          if( !repeat ) {
            // remove itself from map to avoid memory problems
            scheduled.remove(id);
          }
        }
      };
      scheduled.put(id, task);

      if( repeat ) {
        // repeated execution
        timer.schedule(task, time, time);
      }
      else {
        // single execution
        timer.schedule(task, time);
      }

      log.debug("javascript timer> Scheduled" + (repeat ? " repeated " : " ") + "execution of " + id + " in " + time + " ms");

      return id;
    }
  }

  // ======================================

  /**
   * A javascript/java bridge for logging purposes
   */
  private static class JavascriptConsole {

    private final Log log;
    private final boolean verbose;

    private JavascriptConsole(Log log, boolean verbose) {
      this.log = log;
      this.verbose = verbose;
    }

    public void log(String m1) {
      if( verbose ) {
        log.info("javascript> "+m1);
      }
    }

    public void log(String m1, String m2) {
      if( verbose ) {
        log.info("javascript> "+m1+" "+m2);
      }
    }

    public void log(String m1, String m2, String m3) {
      if( verbose ) {
        log.info("javascript> "+m1+" "+m2+" "+m3);
      }
    }

    public void log(String m1, String m2, String m3, String m4) {
      if( verbose ) {
        log.info("javascript> "+m1+" "+m2+" "+m3+" "+m4);
      }
    }

  }

  // ======================================

  /**
   * A javascript/java bridge for listening for a test result
   */
  private static class JavascriptTestListener {

    private String testResultXml;
    private Log log;

    public JavascriptTestListener(Log log) {
      this.log = log;
    }

    /**
     * Test execution has finished
     * @param testResultXml The test result as XML
     */
    public void finished(String testResultXml) {
      this.testResultXml = testResultXml;
      log.debug("Test execution has been finished");
    }

    public String getTestResultXml() {
      return testResultXml;
    }
  }

  // ======================================

  /**
   * A javascript/java bridge for importing script files
   */
  private static class JavascriptImporter {

    private final ScriptEngine engine;
    private final File baseDir;
    private final JavascriptConsole console;


    public JavascriptImporter(ScriptEngine engine, File baseDir, JavascriptConsole console) {
      this.engine = engine;
      this.baseDir = baseDir;
      this.console = console;
    }

    public void importScript(String src, boolean async) throws ScriptException {

      Reader script;
      String scriptName;
      if( src.startsWith("classpath:") ) {
        scriptName = src;
        script = new InputStreamReader(getClass().getResourceAsStream(src.substring("classpath:".length())));
      }
      else {
        File scriptFile = new File(baseDir, src);
        scriptName = scriptFile.getPath();
        try {
          script = new FileReader(scriptFile);
        }
        catch (FileNotFoundException e) {
          throw new ScriptException("Error reading script "+scriptFile+": file not found");
        }
      }


      // --- importing script file
      String importComment = "// -------- import: "+scriptName+"\n";
      console.log("Importing script " + scriptName + " " + (async ? "asynchronously " : "") + "...");
      // update filename for better ability to debug
      engine.getContext().setAttribute(ScriptEngine.FILENAME, scriptName, ScriptContext.ENGINE_SCOPE);
      try {

        try {
          engine.eval(importComment);
          engine.eval(script);
        }
        finally {
          script.close();
        }
      }
      catch(IOException e) {
        throw new ScriptException("Error reading script "+scriptName+": "+e.getMessage());
      }
      finally {
        engine.getContext().removeAttribute(ScriptEngine.FILENAME, ScriptContext.ENGINE_SCOPE);
      }
    }

  }




}
