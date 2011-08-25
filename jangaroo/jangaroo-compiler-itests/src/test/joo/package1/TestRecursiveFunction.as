package package1 {

public class TestRecursiveFunction {

  public function testFib(m:int):int {
    function fib(n:int):int {
      return n < 2 ? n : fib(n-2) + fib(n-1);
    }
    return fib(m);
  }

}
}