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
  public void testOverrides() throws Exception {
    assertOverridesCompilationResult("package1/Override");
  }

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
  public void testStaticAndNonStatic() throws Exception {
    assertCompilationResult("package1/StaticAndNonStatic");
  }

  @Test
  public void testMixin() throws Exception {
    File compileResult = compile("package2/ITestMixin");
    assertFalse("[Mixin] interfaces must not have compile output.", compileResult.exists());
    assertCompilationResult("package2/TestMixinClient");
    assertCompilationResult("package2/TestMixin");
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
    assertApiCompilationResult("package1/someOtherPackage/ImportReduction", "/withExclude");
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
    File compileResult = compile("package1/SomeNativeClass");
    assertFalse("[Native] classes must not have compile output.", compileResult.exists());
    assertApiCompilationResult("package1/SomeNativeClass");
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

  private void assertApiCompilationResult(String path) throws URISyntaxException, IOException {
    assertApiCompilationResult(path, "");
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
  public void testPrivateMemberAccess() throws Exception {
    assertCompilationResult("package1/PrivateMemberAccess");
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
