package net.jangaroo.jooc;

import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertTrue;

public class JoocMxmlTest extends AbstractJoocTest {

  @Test
  public void testInterfaceImplementingMxml() throws Exception {
    File sourceFile = getFile("/package1/mxml/DoesNotImplementMethodFromInterface.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    String expected = "Does not implement [doIt]";
    assertTrue("Expected error (does not implement function) did not occur",
            testLog.hasError(expected));
    assertErrorAt(expected, 4, 15);
  }

  @Test
  public void testUsingNonIdentifierCharacterAfterMxmlNode() throws Exception {
    File sourceFile = getFile("/package1/mxml/UsingNonIdentifierCharacterAfterMxmlNode.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    String expected = "unexpected tokens";
    assertTrue("Expected error (unexpected tokens) did not occur",
            testLog.hasError(expected));
    assertErrorAt(expected, 7, 1);
  }

  @Test
  public void testInterfaceImplementingMxmlClass() throws Exception {
    assertCompilationResult("package1/mxml/InterfaceImplementingMxmlClass", ".mxml");
  }

  @Test
  public void testSimpleMxml() throws Exception {
    assertCompilationResult("package1/mxml/SimpleMxmlClass", ".mxml");
  }

  @Test
  public void testTestComponent() throws Exception {
    assertCompilationResult("package1/mxml/pkg/TestComponentBase", ".as");
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
  public void testOldPropertyAccessSyntax() throws Exception {
    assertCompilationResult("package1/mxml/TestOldPropertyAccessSyntax", ".mxml");
  }

  @Test
  public void testCyclicUsages() throws Exception {
    assertCompilationResult("package1/mxml/AInstantiatesB", ".mxml");
  }

  @Test
  public void testMxmlCannotResolveClass() throws Exception {
    File sourceFile = getFile("/package1/mxml/CannotResolveClass.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    String expected = "Could not resolve class from MXML node <foo:UnknownClass/>";
    assertTrue("Expected error (cannot resolve class) did not occur",
            testLog.hasError(expected));
    assertErrorAt(expected, 14, 7);
  }

  @Test
  public void testMxmlSelfSuperClass() throws Exception {
    File sourceFile = getFile("/package1/mxml/SelfSuperclass.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    String expected = "Cyclic inheritance error: Super class and this component are the same.";
    assertTrue("Expected error (cyclic inheritance) did not occur",
            testLog.hasError(expected));
    assertErrorAt(expected, 2, 1);
  }

  @Test
  public void testMxmlUndefinedType() throws Exception {
    File sourceFile = getFile("/package1/mxml/UndefinedType.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    String expected = "Undefined type: ext.config.UnknownClass";
    assertTrue("Expected error (undefined type) did not occur",
            testLog.hasError(expected));
    assertErrorAt(expected, 12, 7);
  }

  @Test
  public void testMxmlUndefinedTypeInBinding() throws Exception {
    File sourceFile = getFile("/package1/mxml/UndefinedTypeInBinding.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    String expected = "Unable to import package1.mxml.UndefinedType: error while parsing its source (see error above).";
    assertTrue("Expected error (unable to import) did not occur",
            testLog.hasError(expected));
    assertErrorAt(expected, 4, 86);

    String expected2 = "Undefined type: ext.config.UnknownClass";
    assertTrue("Expected error (undefined type) did not occur",
            testLog.hasError(expected2));
    assertErrorAt(expected2, 12, 7);
  }

  @Test
  public void testMxmlPropertiesAccess() throws Exception {
    assertCompilationResult("package1/mxml/pkg/PropertiesAccessBase", ".as");
    assertCompilationResult("package1/mxml/pkg/PropertiesAccess", ".mxml");
  }

  @Test
  public void testMxmlSyntaxErrorInBinding() throws Exception {
    File sourceFile = getFile("/package1/mxml/SyntaxErrorInBinding.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    assertTrue("Expected error (syntax error) did not occur",
            testLog.hasError("Syntax error: 'default'"));
    assertErrorAt("Syntax error: 'default'", 4, 21);
  }

  @Test
  public void testMxmlSyntaxErrorInBinding1() throws Exception {
    File sourceFile = getFile("/package1/mxml/SyntaxErrorInBinding1.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    assertTrue("Expected error (syntax error) did not occur",
            testLog.hasError("Syntax error: '|'"));
    assertErrorAt("Syntax error: '|'", 7, 9);
  }

  @Test
  public void testInvalidXmlHeader() throws Exception {
    File sourceFile = getFile("/package1/mxml/InvalidXmlHeader.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    assertTrue("Expected error (unsupported XML header attribute) did not occur",
            testLog.hasError("Caused by: unsupported XML header attribute"));
    assertErrorAt("Caused by: unsupported XML header attribute", 1, 21);
  }

  @Test
  public void testInvalidIdentifier() throws Exception {
    File sourceFile = getFile("/package1/mxml/InvalidIdentifier.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    assertTrue("Expected error (invalid action script identifier) did not occur",
            testLog.hasError("invalid action script identifier"));
    assertErrorAt("invalid action script identifier", 6, 37);
  }

  @Test
  public void testUnexpectedTextContent() throws Exception {
    File sourceFile = getFile("/package1/mxml/UnexpectedTextContent.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    assertTrue("Expected error (Unexpected text inside MXML element: 'Blablabla'.) did not occur",
            testLog.hasError("Unexpected text inside MXML element: 'Blablabla'."));

    assertErrorAt("Unexpected text inside MXML element: 'Blablabla'.", 8, 30);
  }

  @Test
  public void testInvalidInitializeReturnType() throws Exception {
    File sourceFile = getFile("/package1/mxml/InvalidInitializeReturnType.mxml");
    config.addSourceFile(sourceFile);
    jooc.run();
    assertTrue("Expected error (Invalid MXML __initialize__ method return type, ...) did not occur",
            testLog.hasError("Invalid MXML __initialize__ method return type, must be void or 'package1.mxml.InvalidInitializeReturnType'."));

    assertErrorAt("Invalid MXML __initialize__ method return type, must be void or 'package1.mxml.InvalidInitializeReturnType'.", 6, 73);
  }

  @Test
  public void testScriptCdataMxml() throws Exception {
    assertCompilationResult("package1/mxml/ScriptCdataMxmlClass", ".mxml");
  }

  @Test
  public void testStringToArrayCoercion() throws Exception {
    assertCompilationResult("package1/mxml/StringToArrayCoercion", ".mxml");
  }

  @Test
  public void testStringToEmptyArrayCoercion() throws Exception {
    assertCompilationResult("package1/mxml/StringToEmptyArrayCoercion", ".mxml");
  }

  @Test
  public void testWhitespaceAroundBindingExpression() throws Exception {
    assertCompilationResult("package1/mxml/WhitespaceAroundBindingExpression", ".mxml");
  }

  @Test
  public void testAllElements() throws Exception {
    assertCompilationResult("package1/AllElements", ".mxml");
  }

  @Test
  public void testSimpleMxmlClassNameClass() throws Exception {
    assertCompilationResult("package1/mxml/pkg/SimpleMxmlClass", ".mxml");
  }

}
