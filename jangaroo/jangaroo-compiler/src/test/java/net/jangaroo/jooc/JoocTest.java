package net.jangaroo.jooc;

import net.jangaroo.jooc.input.FileInputSource;
import net.jangaroo.jooc.input.InputSource;
import net.jangaroo.jooc.mxml.CatalogComponentsParser;
import net.jangaroo.jooc.mxml.MxmlComponentRegistry;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class JoocTest extends AbstractJoocTest {

  @Test
  public void testEqualDeclarationVariableType() throws Exception {
    File sourcefile = getFile("/package1/SomeClass.as");
    config.addSourceFile(sourcefile);
    jooc.run();
    assertTrue("Expected error (type not a compile-time constant) did not occur",
            testLog.hasError("Type was not found or was not a compile-time constant: SomeClass"));
  }

  @Test
  public void testNonConstantInitializer() throws Exception {
    File sourcefile = getFile("/package1/NonConstantInitializer.as");
    config.addSourceFile(sourcefile);
    jooc.run();
    assertTrue("Expected error (initializer not a compile-time constant) did not occur",
            testLog.hasError("Parameter initializer must be compile-time constant."));
  }

  @Test
  public void testStaticReference() throws Exception {
    assertCompilationResult("package1/WithStaticReference");
  }

  @Test
  public void testResolveMembers() throws Exception {
    assertCompilationResult("package1/TestResolveMembers");
  }

  @Test
  public void testNoMultipleThisAliases() throws Exception {
    assertCompilationResult("package1/NoMultipleThisAliases");
  }

  @Test
  public void testNoPrimitiveInit() throws Exception {
    assertCompilationResult("package1/NoPrimitiveInit");
  }

  @Test
  public void testChainedConstants() throws Exception {
    assertCompilationResult("package1/ChainedConstants");
  }

  @Test
  public void testChainedConstantsApi() throws Exception {
    assertApiCompilationResult("package1/ChainedConstants");
  }

  @Test
  public void testInterface() throws Exception {
    assertCompilationResult("package1/Interface");
  }

  @Test
  public void testInterfaceStaticMethod() throws Exception {
    File sourcefile = getFile("/package1/InterfaceStaticMethod.as");
    config.addSourceFile(sourcefile);
    jooc.run();
    String expected = "illegal modifier: static";
    assertTrue("Expected error (illegal modifier) did not occur",
            testLog.hasError(expected));
    assertErrorAt(expected, 4, 3);
  }

  @Test
  public void testInterfaceInvalidModifier1() throws Exception {
    File sourcefile = getFile("/package1/InterfaceInvalidModifier1.as");
    config.addSourceFile(sourcefile);
    jooc.run();
    String expected = "illegal modifier: private";
    assertTrue("Expected error (illegal modifier) did not occur",
            testLog.hasError(expected));
    assertErrorAt(expected, 3, 1);
  }

  @Test
  public void testInterfaceInvalidModifier2() throws Exception {
    File sourcefile = getFile("/package1/InterfaceInvalidModifier2.as");
    config.addSourceFile(sourcefile);
    jooc.run();
    String expected = "illegal modifier: internal";
    assertTrue("Expected error (illegal modifier) did not occur",
            testLog.hasError(expected));
    assertErrorAt(expected, 4, 3);
  }

  @Test
  public void testCannotResolveMember() throws Exception {
    File sourcefile = getFile("/package1/CannotResolveMember.as");
    config.addSourceFile(sourcefile);
    jooc.run();
    assertCannotResolveWrongMember(7, 17);
    assertCannotResolveWrongMember(8, 17);
    assertCannotResolveWrongMember(9, 23);

    assertCannotResolveWrongMember(11, 23);
    assertCannotResolveWrongMember(12, 22);
    assertCannotResolveWrongMember(13, 28);
    assertCannotResolveWrongMember(14, 33);

    assertCannotResolveWrongMember(16, 15);
    assertCannotResolveWrongMember(17, 21);
    assertCannotResolveWrongMember(18, 26);
    assertCannotResolveWrongMember(19, 26);

    assertCannotResolveWrongMember(22, 18);
  }

  private void assertCannotResolveWrongMember(int line, int column) {
    String expected = String.format("cannot resolve member 'wrong%d'.", line);
    assertTrue("Expected error (" + expected + ") did not occur",
            testLog.hasError(expected));
    assertErrorAt(expected, line, column);
  }

  @Test
  public void testStaticAndNonStatic() throws Exception {
    assertCompilationResult("package1/StaticAndNonStatic");
  }

  @Test
  public void testTestTypeCast() throws Exception {
    assertCompilationResult("package1/TestTypeCast");
  }

  @Test
  public void testMixin() throws Exception {
    final String relativeClassFileName = "package2/ITestMixin";
    compile(relativeClassFileName);
    File compileResult = outputFile(outputFolder, relativeClassFileName, ".js");

    assertFalse("[Mixin] interfaces must not have compile output.", compileResult.exists());
    assertCompilationResult("package2/TestMixinClient");
    assertCompilationResult("package2/TestMixin");
  }

  @Test
  public void testCustomConfig() throws IOException, URISyntaxException {
    assertCompilationResult("package1/ConfigClass");
  }

  @Test
  public void testPublicApiApi() throws Exception {
    config.setExcludeClassByDefault(true);
    assertApiCompilationResult("package1/IncludedClass");
  }

  @Test
  public void testInterfaceApi() throws Exception {
    assertApiCompilationResult("package1/Interface");
  }

  @Test
  public void testImplementInterface() throws Exception {
    assertCompilationResult("package1/ImplementsInterface");
    assertApiCompilationResult("package1/ImplementsInterface");
  }

  @Test
  public void testParameterInitializers() throws Exception {
    assertApiCompilationResult("package1/ParameterInitializers");
  }

  @Test
  public void testSuperCallParameters() throws Exception {
    assertCompilationResult("package1/SuperCallParameters");
    assertApiCompilationResult("package1/SuperCallParameters");
  }

  @Test
  public void testImportReduction() throws Exception {
    assertApiCompilationResult("package1/someOtherPackage/ImportReduction");
  }

  @Test
  public void testImportReductionExcludeClass() throws Exception {
    config.setExcludeClassByDefault(true);
    assertApiCompilationResult("package1/someOtherPackage/ImportReduction", "/expectedApi/withExclude");
  }

  @Test
  public void testAuxVarConfusion() throws Exception {
    assertCompilationResult("package1/AuxVarConfusion");
  }

  @Test
  public void testNamespaceDeclarationApi() throws Exception {
    assertApiCompilationResult("package1/testNamespace");
  }

  @Test
  public void testPackageGlobalVar() throws Exception {
    assertCompilationResult("package1/somePackageGlobal");
  }
  
  @Test
  public void testInitPackageGlobalVar() throws Exception {
    assertCompilationResult("package1/UsingSomePackageGlobal");
  }
  
  @Test
  public void testUninitializedPackageGlobalVar() throws Exception {
    assertCompilationResult("package1/uninitializedPackageGlobal");
  }
  
  @Test
  public void testPackageGlobalFun() throws Exception {
    assertCompilationResult("package1/somePackageGlobalFun");
  }
  
  @Test
  public void testArrayForIn() throws Exception {
    assertCompilationResult("package1/TestArrayForIn");
  }
  
  @Test
  public void testPackageGlobalVarApi() throws Exception {
    config.setExcludeClassByDefault(true);
    assertApiCompilationResult("package1/somePackageGlobal");
  }
  
  @Test
  public void testPackageGlobalFunApi() throws Exception {
    assertApiCompilationResult("package1/somePackageGlobalFun");
  }
  
  @Test
  public void testNativeApi() throws Exception {
    String relativeClassFileName = "package1/SomeNativeClass";
    compile(relativeClassFileName);
    File compileResult = outputFile(outputFolder, relativeClassFileName, ".js");
    assertFalse("[Native] classes must not have compile output.", compileResult.exists());
    assertApiCompilationResult(relativeClassFileName);
  }

  @Test
  public void testUsingNativeClass() throws Exception {
    assertCompilationResult("package1/UsingSomeNativeClass");
  }

  @Test
  public void testUsingPackageGlobal() throws Exception {
    assertCompilationResult("package1/UsingSomePackageGlobal");
  }

  @Test
  public void testUsingEmbed() throws Exception {
    assertCompilationResult("package1/UsingEmbed");
  }

  @Test
  public void testClassWithNamespacedMembers() throws Exception {
    assertApiCompilationResult("package1/someOtherPackage/NamespacedMembers");
  }

  private void assertApiCompilationResult(String relativeClassFileName) throws URISyntaxException, IOException {
    assertApiCompilationResult(relativeClassFileName, "/expectedApi");
  }

  @Test
  public void testStaticAccess() throws Exception {
    assertCompilationResult("package2/TestStaticAccess");
  }

  @Test
  public void testMethodCall() throws Exception {
    assertCompilationResult("package1/TestMethodCall");
  }

  @Test
  public void testStaticNonStaticConfusion() throws Exception {
    assertCompilationResult("package1/TestStaticNonStaticConfusion");
  }

  // TODO: KNOWN BUG!
  public void testLocalVariableDoesNotShadeClass() throws Exception {
    assertCompilationResult("package1/TestLocalVariableDoesNotShadeClass");
  }

  @Test
  public void testIdeWithReservedName() throws Exception {
    assertCompilationResult("package1/TestIdeWithReservedName");
  }

  @Test
  public void testMethodBinding() throws Exception {
    assertCompilationResult("package1/TestBind");
  }

  @Test
  public void testHelperClasses() throws Exception {
    assertCompilationResult("package1/TestHelperClasses");
  }

  @Test
  public void testNoCodeExpression() throws Exception {
    assertCompilationResult("package1/NoCodeExpression");
  }

  @Test
  public void testFieldInitializer() throws Exception {
    assertCompilationResult("package1/FieldInitializer");
  }

  @Test
  public void testPrototypeConstants() throws Exception {
    assertCompilationResult("package2/TestPrototypeConstants");
  }

  @Test
  public void testPrivateMemberAccess() throws Exception {
    assertCompilationResult("package1/PrivateMemberAccess");
  }

  @Test
  public void testEventListener() throws Exception {
    assertCompilationResult("package2/TestEventListener");
  }

  @Test
  public void testRequireResourceBundle() throws Exception {
    assertCompilationResult("package2/TestRequireResourceBundle");
  }

  @Test
  public void testManifestToCatalogConversion() throws Exception {
    File manifestFile = getFile("/customNamespace/manifest.xml");
    File catalogFile = new File(outputFolder, "/catalog.xml");
    generateCatalogFromManifest(manifestFile, catalogFile);
    assertTrue("the output file " + catalogFile + " should exist, but doesn't", catalogFile.exists());
    String result = readFileToString(catalogFile);
    String expected = readFileToString(getFile("/expected/customNamespace/catalog.xml"));
    assertEquals("Result file not equal", expected, result);
  }

  @Test
  public void testCatalogueComponentModelParsing() throws Exception {
    File manifestFile = getFile("/customNamespace/manifest.xml");
    File catalogFile = new File(outputFolder, "/catalog.xml");
    generateCatalogFromManifest(manifestFile, catalogFile);

    InputSource inputSource = new FileInputSource(catalogFile, true);
    MxmlComponentRegistry mxmlComponentRegistry = new MxmlComponentRegistry();
    new CatalogComponentsParser(mxmlComponentRegistry).parse(inputSource.getInputStream());
    assertEquals("ext.Panel",
            mxmlComponentRegistry.getClassName("library://test.namespace", "panel"));
    assertEquals("com.coremedia.ui.sdk.desktop.FavoritesToolbar",
            mxmlComponentRegistry.getClassName("library://test.namespace", "favoritesToolbar"));
  }

}
