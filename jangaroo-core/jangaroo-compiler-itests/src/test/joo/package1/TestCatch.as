/*
 * Copyright 2008 CoreMedia AG
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

import package3.ClassToImport;

public class TestCatch {

  public static var finallyExecuted :Boolean;

  public static function testCatch(thrower :Function) : String {
    finallyExecuted = false;
    try {
      thrower();
      return "nothing thrown";
    } catch (e2 : package3.ClassToImport) {
      return "ClassToImport";
    } catch (e : TestInterface) {
      return "TestInterface";
    /*todo implement finally
    } finally {
      finallyExecuted = false;*/
    }
    return "nothing catched";
  }

  public static function testFallThrough() : void {
    try {
      throw new Error("general error");
    } catch (e : TestInterface) {
      // Error should not be caught!
    }
  }
}
}
