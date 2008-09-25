package joox.unit.textui {

import joox.unit.util.JooError;

public class Usage extends JooError {

  public function Usage(msg : String) {
    super("Usage", msg);
  }
}
}