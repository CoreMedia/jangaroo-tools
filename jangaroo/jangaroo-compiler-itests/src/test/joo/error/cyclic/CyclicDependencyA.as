
package error.cyclic {

public class CyclicDependencyA {
  public static const b:CyclicDependencyB = new CyclicDependencyB();
}
}