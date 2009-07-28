package net.jangaroo.example {
import flexunit.framework.Assert;
import flexunit.framework.TestCase;
import net.jangaroo.example.HelloWorld;

public class HelloWorldTest extends TestCase{
  public function HelloWorldTest() {
    super();
  }

  public function testGreet():void {
    var greeter:HelloWorld = new HelloWorld();
    Assert.assertEquals("Hello, World!", greeter.greet("World"));
  }

  public function testGreetHtml():void {
    var greeter:HelloWorld = new HelloWorld();
    Assert.assertEquals("Hello, Wo&amp;rld!", greeter.greetHtml("Wo&rld"));
  }
}
}