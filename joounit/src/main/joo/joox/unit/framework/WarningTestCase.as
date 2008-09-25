package joox.unit.framework {

import joox.unit.framework.TestCase;
import joox.unit.framework.Assert;

public class WarningTestCase extends TestCase {

  public var mMessage : String;

  public function WarningTestCase(message : String) {
    super("warning");
    this.mMessage = message;
  }

  override public function runTest() : void {
    Assert.fail(this.mMessage);
  }

}
}
