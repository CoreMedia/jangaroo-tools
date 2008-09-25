package joox.unit.framework.test {

import joox.unit.framework.TestCase;

public class MyTestCase extends TestCase {

  public var mSetUp : Boolean;
  public var mTearDown : Boolean;

  public function MyTestCase() {
    super("testMe");
    this.mSetUp = false;
    this.mTearDown = false;
  }

  override public function setUp() : void {
    this.mSetUp = true;
  }

  public function testMe() : void {
  }

  override public function tearDown() : void {
    this.mTearDown = true;
  }

}
}
