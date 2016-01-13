
package error.cyclic {

public class CyclicDependencyB {
  public static const a:CyclicDependencyA = new CyclicDependencyA();
}
}