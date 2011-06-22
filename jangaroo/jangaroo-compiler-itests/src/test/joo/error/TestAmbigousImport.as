
package error {

import package1.*;
import package3.ClassToImport;

public class TestAmbigousImport {
  public function TestAmbigousImport() {
  }


  public function TestImport() {
    var ti :TestImplements = new TestImplements();
    /*package3.*/ ClassToImport.m_package3(); //should be reported as unknown/ambigous
  }

}
}