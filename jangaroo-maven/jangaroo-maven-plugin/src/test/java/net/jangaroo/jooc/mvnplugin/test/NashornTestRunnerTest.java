package net.jangaroo.jooc.mvnplugin.test;


import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Test;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class NashornTestRunnerTest {

  @Test
  public void doit() throws ScriptException {
    String testUrl = "about:blank";
    NashornTestRunner nashornTestRunner = new NashornTestRunner(new SystemStreamLog(), testUrl, 1000);
    ScriptEngine scriptEngine = nashornTestRunner.loadScripts();
    assertNotNull(scriptEngine);

    ScriptObjectMirror ext = (ScriptObjectMirror) scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE).get("Ext");
    assertNull(ext);

    scriptEngine.eval("load('src/test/resources/bootstrap.js')");

    ext = (ScriptObjectMirror) scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE).get("Ext");
    assertNotNull(ext);

    assertNotNull("Boot", ext.get("Boot"));

    assertNotNull("Test", scriptEngine.eval("aTest"));
  }

  @Test
  public void loadJooUnitTests() throws ScriptException {
    String testUrl = "http://localhost:10100/joounit/target/test-classes/?cache";
    NashornTestRunner nashornTestRunner = new NashornTestRunner(new SystemStreamLog(), testUrl, 1000);
    ScriptEngine scriptEngine = nashornTestRunner.loadScripts();
    assertNotNull(scriptEngine);

    ScriptObjectMirror ext = (ScriptObjectMirror) scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE).get("Ext");
    assertNull(ext);

    scriptEngine.eval("window.location.href='" + testUrl + "'");

    ext = (ScriptObjectMirror) scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE).get("Ext");
    assertNotNull(ext);

    assertNotNull("Boot", ext.get("Boot"));

    assertNotNull("define", ext.get("define"));
  }

}