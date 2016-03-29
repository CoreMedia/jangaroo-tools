package net.jangaroo.jooc;

import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertTrue;

public class JoocMxmlTest extends AbstractJoocTest {

  @org.junit.Ignore
  @Test
  public void testInterfaceImplementingMxml() throws Exception {
    File sourceFile = getFile("/package1/mxml/InterfaceImplementingMxmlClass.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    assertTrue("Expected error (does not implement function) did not occur",
            testLog.hasError("Does not implement function: doIt"));
  }

  @Test
  public void testSimpleMxml() throws Exception {
    assertCompilationResult("package1/mxml/SimpleMxmlClass", ".mxml");
  }

  @Test
  public void testTestComponent() throws Exception {
    assertCompilationResult("package1/mxml/pkg/TestComponent", ".mxml");
  }

  @Test
  public void testCyclicDependencies() throws Exception {
    assertCompilationResult("package1/mxml/pkg/CyclicDependencies", ".mxml");
  }

  @Test
  public void testMetadataCdataMxml() throws Exception {
    assertCompilationResult("package1/mxml/MetadataCdataMxmlClass", ".mxml");
  }

  @Test
  public void testSimpleMetadataMxml() throws Exception {
    assertCompilationResult("package1/mxml/SimpleMetadataMxmlClass", ".mxml");
  }

  @Test
  public void testMetadataMxml() throws Exception {
    assertCompilationResult("package1/mxml/MetadataMxmlClass", ".mxml");
  }

  @Test
  public void testDeclarationsMxml() throws Exception {
    assertCompilationResult("package1/mxml/DeclarationsMxmlClass", ".mxml");
  }

  @Test
  public void testMxmlCannotResolveClass() throws Exception {
    File sourceFile = getFile("/package1/mxml/ErrorCannotResolveClass.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    assertTrue("Expected error (cannot resolve class) did not occur",
            testLog.hasError("Could not resolve class from MXML node <foo:UnknownClass/>"));
  }

  @Test
  public void testMxmlSelfSuperClass() throws Exception {
    File sourceFile = getFile("/package1/mxml/ErrorSelfSuperclass.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    assertTrue("Expected error (cyclic inheritance) did not occur",
            testLog.hasError("Cyclic inheritance error: Super class and this component are the same."));
  }

  @Test
  public void testMxmlUndefinedType() throws Exception {
    File sourceFile = getFile("/package1/mxml/ErrorUndefinedType.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    assertTrue("Expected error (undefined type) did not occur",
            testLog.hasError("Undefined type: ext.config.UnknownClass"));
  }

  @Test
  public void testInvalidXmlHeader() throws Exception {
    File sourceFile = getFile("/package1/mxml/InvalidXmlHeader.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    assertTrue("Expected error (unsupported XML header attribute) did not occur",
            testLog.hasError("Caused by: unsupported XML header attribute"));
  }

  @Test
  public void testInvalidIdentifier() throws Exception {
    File sourceFile = getFile("/package1/mxml/InvalidIdentifier.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    assertTrue("Expected error (invalid action script identifier) did not occur",
            testLog.hasError("invalid action script identifier"));
  }

  @Test
  public void testUnexpectedTextContent() throws Exception {
    File sourceFile = getFile("/package1/mxml/ErrorUnexpectedTextContent.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    assertTrue("Expected error (Unexpected text inside MXML element: 'Blablabla'.) did not occur",
            testLog.hasError("Unexpected text inside MXML element: 'Blablabla'."));
  }

  @Test
  public void testScriptCdataMxml() throws Exception {
    assertCompilationResult("package1/mxml/ScriptCdataMxmlClass", ".mxml");
  }

  @Test
  public void testAllElements() throws Exception {
    assertCompilationResult("package1/AllElements", ".mxml");
  }

}
