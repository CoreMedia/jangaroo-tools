package joox.unit.textui.test {

import joox.unit.textui.TestRunner;
import joox.unit.framework.TestResult;

public class DummyRunner extends TestRunner {

  public function DummyRunner() {
    super();
  }

  public override function start(args : Object) : TestResult {
    throw args.length;
  }

  public static var msg : String;

  public override function runFailed(newMsg : String) : void {
    msg = newMsg;
  }

}
}
