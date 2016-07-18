package package1.someOtherPackage {
import package1.testNamespace;

[Uses("Object")]
public class NamespacedMembers {
  testNamespace native function getFoo():String;
}
}