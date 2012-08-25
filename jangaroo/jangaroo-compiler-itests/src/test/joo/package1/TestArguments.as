/*
 * Copyright 2012 CoreMedia AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */

package package1 {

public class TestArguments {

  public function TestArguments() {
  }

  public function joinArguments() : String {
    return arguments.join(",");
  }

  public function joinArgumentsClassic() : String {
    var joined:String = "";
    for (var i:int = 0; i < arguments.length; i++) {
      if (i > 0) {
        joined += ",";
      }
      joined += arguments[i];
    }
    return joined;
  }

  public function joinArgumentsAuxVar() : String {
    var rest:Array = arguments;
    return rest.join(",");
  }

  public function joinRest( ... rest) : String {
    return rest.join(",");
  }

  public function joinExplicitArguments( ... arguments) : String {
    return arguments.join(",");
  }

  public function joinRestWithSeparator(sep : String, ... rest) : String {
    return rest.join(sep);
  }

  public function testArguments() : Array {
    return arguments;
  }

  public function testParameterShadesArguments(arguments : *) : * {
    return arguments;
  }

}
}