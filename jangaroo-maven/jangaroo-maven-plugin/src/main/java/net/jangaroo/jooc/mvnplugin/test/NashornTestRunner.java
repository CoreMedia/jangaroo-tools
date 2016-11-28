package net.jangaroo.jooc.mvnplugin.test;

import org.apache.maven.plugin.logging.Log;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.InputStreamReader;

public class NashornTestRunner {

  final Log log;
  final String testUrl;
  final int timeout;

  public NashornTestRunner(Log log, String testUrl, int timeout) {
    this.log = log;
    this.testUrl = testUrl;
    this.timeout = timeout;
  }

  void doit() throws ScriptException {
    ScriptEngine nashorn = loadScripts();

    nashorn.eval("window.location.href = '" + testUrl + "'");

    long start = System.currentTimeMillis();
    int millisRemaining;
    do {
      millisRemaining = millisElapsed(start);
      Object result = nashorn.eval("(function() {return window[\"result\"];})()");
      log.info("current result: " + result);
      log.info("seconds remaining: " + millisRemaining/1000);
      log.info("current state of jangaroo: " + nashorn.eval("(function() {return typeof(joo);})()"));
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        log.debug("thread interrupted", e);
      }
    } while (timeout > millisRemaining);
  }

  ScriptEngine loadScripts() throws ScriptException {
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine nashorn = mgr.getEngineByName("nashorn");

    nashorn.getBindings(ScriptContext.ENGINE_SCOPE).put("console", new JSConsole(log));

    if (new File("src/main/js/net/jangaroo/jooc/mvnplugin/test/env.nashorn.1.2.js").exists()) {
      // enable IDEA JS debugging
      // see https://blog.jetbrains.com/idea/2014/03/debugger-for-jdk8s-nashorn-javascript-in-intellij-idea-13-1/
      nashorn.eval("load('src/main/js/net/jangaroo/jooc/mvnplugin/test/env.nashorn.1.2.js')");
      nashorn.eval("load('src/main/js/net/jangaroo/jooc/mvnplugin/test/setup.js')");
    } else {
      nashorn.eval(new InputStreamReader(getClass().getResourceAsStream("env.nashorn.1.2.js")));
      nashorn.eval(new InputStreamReader(getClass().getResourceAsStream("setup.js")));
    }
    return nashorn;
  }

  private static int millisElapsed(long start) {
    return (int)(System.currentTimeMillis() - start);
  }

}
