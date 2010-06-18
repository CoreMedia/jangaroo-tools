package some.pakage {

import example.*;

import net.jangaroo.test.JavaPackageTest;

import package1.*;
import package1.package11.*;

import package2.*;

public class TestImportFromClassPath {
  public function TestImportFromClassPath() {
    // use as many test classes as possible to make jooc parse them - just to verify the syntax of the generated API code
    new ToplevelClassToImport();
    new ExtendedPerson("abc", null);
    new Person(null);
    new JavaPackageTest();
    new ClassToImport();
    new TestAssert();
    new TestBind(null);
    new TestExplicitSuper();
    new TestImplements();
    new TestInheritanceSubClass(null, null);
    new TestInheritanceSubSubClass(null, null, null);
    new TestInheritanceSuperClass(null);
    new TestInitializers();
    new TestInitializersWithDefaultConstructor();
    var t:TestInterface;
    var t2:TestInterface2;
    new TestIs();
    new TestLocalVariables();
    new TestMemberNames();
    new TestMethodCall();
    new TestMultiDeclarations();
    new TestPackageHidesVar();
    new TestParamInitializers();
    new TestRestParams();
    new TestSelfAwareness();
    new TestSetterOverride();
    new TestTypeCast();
    new TestUnqualifiedAccess("dd");
    new TestSubPackage();
    new TestExpressions();
    new TestImport();
    new TestInclude();
    new TestNew();
    new TestStatements();
    new TestStaticAccess();
    new ClassToImport();
    new ClassToImport2();
    new TestGetterSetter();
    new TestImplicitSuper();
    new TestInheritImplements();
    new TestStaticInitializer();
  }
}
}