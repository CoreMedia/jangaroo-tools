package net.jangaroo.extxml;

import junit.framework.TestCase;
import net.sf.saxon.s9api.SaxonApiException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.Collections;
import java.net.URL;

import utils.TestUtils;


public class JooClassGeneratorTest extends TestCase {

  private JooClassGenerator jooClassGenerator = null;

  @Override
  protected void setUp() throws Exception {
    jooClassGenerator = new JooClassGenerator(null);
  }

  private String toJSON(String fileName) throws ScriptException, SaxonApiException {
    InputStream inputStream = getClass().getResourceAsStream(fileName);
    return reformatJSON(jooClassGenerator.transformToJSON(inputStream));
  }

  private String reformatJSON(String json) throws ScriptException {
    System.out.println(json);
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
    String s = jsEngine.eval("(function() {return (" + json + ")}).toString();").toString();

    /*

   remove the wrapping:
       function () {
           return ({xtype:"panel", id:"foo", title:"bar"});
       }

    result:
       {xtype:"panel", id:"foo", title:"bar"}

    */

    return s.substring(27, s.length() - 5);
  }


  // ******************
  public void testAll() throws Exception {
    assertEquals("{xtype:\"panel\", layout:\"border\", defaults:{collapsible:true, split:true, animCollapse:false}, items:[{region:\"north\", xtype:\"panel\"}, {region:\"north\", xtype:\"header\", cls:\"cm-header\", border:false, logoUrl:\"x.jpg\", title:\"New Editor\"}, {region:\"west\", xtype:\"panel\", id:\"treeTabsPanel\", title:\"Navigation\", width:400, layout:\"fit\", split:true, items:[{xtype:\"tabpanel\", id:\"treeTabs\", activeTab:0, plain:true, defaults:{autoScroll:true}, items:[{xtype:\"editortreepanel\", id:\"repository-tree-panel\", title:\"Repository\", rootVisible:true, url:\"tree.jsp\", selectionChangeListener:currentContentController}, {xtype:\"editortreepanel\", id:\"navigation-tree-panel\", title:\"Navigation\", rootVisible:false, url:\"navtree.jsp\", selectionChangeListener:currentContentController}]}]}, {region:\"center\", xtype:\"panel\", id:\"contentPanel\", title:\"Main Content\", width:400, layout:\"fit\", split:true, collapsible:true, items:[{xtype:\"tabpanel\", activeTab:0, plain:true, items:[{region:\"center\", xtype:\"panel\", id:\"contenttestpanel\", title:\"contentest\", closable:true, layout:\"border\", items:[{region:\"north\", xtype:\"toolbar\", items:[{xtype:\"reloadbutton\", id:\"reloadbutton\", text:\"reload\", controller:resourceController}]}, {region:\"center\", xtype:\"panel\", layout:\"fit\", items:[{xtype:\"contenttable\", title:\"Content Table\", collapsible:false, width:\"auto\"}]}]}, {xtype:\"grid\", id:\"mygrid\", title:\"MVC Testy\", columns:[{header:\"Id\", sortable:false}, {header:\"Headline\", sortable:false}, {header:\"State\", sortable:false}], store:contentStore}, {region:\"border\", xtype:\"panel\", id:\"richtexttest\", title:\"TinyMCE\", closable:true, layout:\"border\", items:[{region:\"north\", xtype:\"panel\", id:\"toolbar\", title:\"toolbar\", closable:true, items:[{xtype:\"toolbar\", items:[{xtype:\"editorbutton\", text:\"Test Button\", refid:\"richText\", enableToggle:true}]}]}, {region:\"center\", xtype:\"xtinymceeditor\", fieldLabel:\"Rich text\", id:\"richText\", name:\"richText\", height:400, width:\"auto\", value:\"Hallo\", layout:\"fit\", tinymceSettings:{theme:\"advanced\", plugins:\"safari,pagebreak,style,layer,table,advhr,advimage,advlink,emotions,iespell,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,noneditable,visualchars,nonbreaking,xhtmlxtras,template,-example\", theme_advanced_buttons1:\"mymenubutton,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect,fontselect,fontsizeselect\", theme_advanced_toolbar_location:\"top\", theme_advanced_toolbar_align:\"left\", theme_advanced_statusbar_location:\"bottom\", theme_advanced_resizing:false}}]}]}]}, {region:\"east\", xtype:\"previewpanel\", id:\"previewpanel\", url:\"../segment/english_tutorial/\", beanModel:currentContentModel, closable:true, width:\"600px\", layout:\"fit\"}]}",
        toJSON("/testAll.xml"));
  }


  public void testBorderLayout() throws Exception {
    assertEquals("{xtype:\"panel\", layout:\"border\", items:[{region:\"north\", xtype:\"panel\"}, {region:\"south\", xtype:\"panel\"}, {region:\"west\", xtype:\"panel\"}]}",
        toJSON("/testBorderLayout.xml"));
  }


  public void testEL() throws Exception {
    assertEquals("{xtype:\"panel\", items:[{xtype:\"panel\", id:\"foo\", controller:some.javascript()}]}",
        toJSON("/testEL.xml"));
  }


  public void testJsonInclude() throws Exception {
    assertEquals("{xtype:\"panel\", id:\"foo\", title:\"bar\"}",
        toJSON("/testJsonInclude.xml"));
  }


  public void testTrueFalse() throws Exception {
    assertEquals("{xtype:\"panel\", items:[{xtype:\"panel\", id:\"foo\", x:true}, {xtype:\"panel\", id:\"bar\", x:false}, {xtype:\"panel\", id:\"foo2\", x:\"True\"}, {xtype:\"panel\", id:\"bar2\", x:\"FALSE\"}]}",
        toJSON("/testTrueFalse.xml"));
  }


  public void testNumber() throws Exception {
    assertEquals("{xtype:\"panel\", items:[{xtype:\"panel\", id:\"foo\", x:100}, {xtype:\"panel\", id:\"foo\", x:\"200b\"}, {xtype:\"panel\", id:\"foo\", x:1.5}]}",
        toJSON("/testNumber.xml"));
  }

  public void testPackage() throws Exception {
    assertEquals("{xtype:\"panel\", title:\"I am inside a package!\", items:[{xtype:\"testAll\"}]}", toJSON("/testpackage/testPackage.xml"));
  }


  public void testCollectXTypes() throws Exception {
    Set<String> set = jooClassGenerator.collectXTypesFromJSON(toJSON("/testAll.xml"));

    String expectedXTypes[] = {
        "contenttable",
        "editorbutton",
        "editortreepanel",
        "grid",
        "header",
        "panel",
        "previewpanel",
        "reloadbutton",
        "tabpanel",
        "toolbar",
        "xtinymceeditor"};
    Set expectedSet = new TreeSet<String>(Arrays.asList(expectedXTypes));

    assertEquals(expectedSet, set);
  }


  public void testGenerateJangarooClass() throws Exception {
    ComponentClass jooClazz = new ComponentClass(Collections.<String>emptyList(), "com.coremedia.test.TestClass","SuperClass","json");
    StringWriter writer = new StringWriter();

    jooClassGenerator.generateJangarooClass(jooClazz, writer);
    writer.flush();
    System.out.println(writer.toString());
    assertEquals("Class not equals","package com.coremedia.test {import ext.ComponentMgr;/** * @xtype com.coremedia.test.TestClass */public class TestClass extends SuperClass {  public const xtype:String = \"com.coremedia.test.TestClass\";{  ext.ComponentMgr.registerType(xtype, TestClass);}  public function TestClass(config:* = undefined) {    super(Ext.apply(config, json));  }}}",writer.toString().replaceAll("\r\n",""));
  }

  public void testGenerateClasses() throws Exception {
    File rootDir = TestUtils.getRootDir(getClass());
    File outDir = TestUtils.computeTestDataRoot(getClass());
    
    ComponentSuite suite = new ComponentSuite("local",File.createTempFile("testXsd","xsd"), rootDir, Collections.<File>emptyList(), outDir);
    ComponentClass cc = new ComponentClass(TestUtils.getFile("/testpackage/testPackage.xml", getClass()));
    ComponentClass panel = new ComponentClass("panel", "ext.Panel");
    suite.addComponentClass(panel);
    cc.setType(ComponentType.XML);
    cc.setFullClassName("testpackage.testPackage");
    cc.setXtype("testpackage.testPackage");
    suite.addComponentClass(cc);

    JooClassGenerator generator = new JooClassGenerator(suite);
    generator.generateClasses();

    assertEquals("{xtype: \"panel\", title: \"I am inside a package!\",items: [{xtype: \"testAll\"}]}",cc.getJson());
    assertEquals("ext.Panel",cc.getSuperClassName());

    File result = new File(outDir, "testpackage/testPackage.as");
    assertTrue(result.exists());

  }

}
