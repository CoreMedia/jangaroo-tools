Ext.define("package2.TestStaticAccess", function(TestStaticAccess) {/* /*
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
 * /

package package2 {

public class TestStaticAccess extends StaticAccessSuperClass {

  static public var s1:String = "s1";
  static private*/ var s2$static/*:String*/ = "s2";/*

  public*/ function TestStaticAccess$() {this.callParent([]);
    package1.StaticAccessSuperSuperClass.f0();
  }/*

  static public*/ function get_s0$static()/*:String*/ {
    return package2.StaticAccessSuperClass.s0;
  }/*
 
  static public*/ function set_s0$static(_s0/*:String*/)/*:void*/ {
    package2.StaticAccessSuperClass.s0 = _s0;
  }/*

  static public*/ function get_s0_qualified$static()/*:String*/ {
    var s0/*:String*/ = "qualified error";
    return package2.StaticAccessSuperClass.s0;
  }/*

  static public*/ function set_s0_qualified$static(s0/*:String*/)/*:void*/ {
    package2.StaticAccessSuperClass.s0 = s0;
  }/*

  static public*/ function get_s0_fully_qualified$static()/*:String*/ {
    var s0/*:String*/ = "fully qualified error";
    return package2.StaticAccessSuperClass.s0;
  }/*

  static public*/ function set_s0_fully_qualified$static(s0/*:String*/)/*:void*/ {
    package2.StaticAccessSuperClass.s0 = s0;
  }/*

  static public*/ function get_s1$static()/*:String*/ {
      return TestStaticAccess.s1;
    }/*
  
  static public*/ function set_s1$static(_s1/*:String*/)/*:void*/ {
    TestStaticAccess.s1 = _s1;
  }/*

  static public*/ function get_s1_qualified$static()/*:String*/ {
    var s1/*:String*/ = "qualified error";
    return TestStaticAccess.s1;
  }/*

  static public*/ function set_s1_qualified$static(s1/*:String*/)/*:void*/ {
    TestStaticAccess.s1 = s1;
  }/*

  static public*/ function get_s1_fully_qualified$static()/*:String*/ {
    var s1/*:String*/ = "fully qualified error";
    return TestStaticAccess.s1;
  }/*

  static public*/ function set_s1_fully_qualified$static(s1/*:String*/)/*:void*/ {
    TestStaticAccess.s1 = s1;
  }/*

  static public*/ function get_s2$static()/*:String*/ {
    return s2$static;
  }/*

  static public*/ function set_s2$static(_s2/*:String*/)/*:void*/ {
    s2$static = _s2;
  }/*

  static public*/ function get_s2_qualified$static()/*:String*/ {
    var s2/*:String*/ = "qualified error";
    return/* TestStaticAccess.*/s2$static;
  }/*

  static public*/ function set_s2_qualified$static(s2/*:String*/)/*:void*/ {/*
    TestStaticAccess.*/s2$static = s2;
  }/*

  static public*/ function get_s2_fully_qualified$static()/*:String*/ {
    var s2/*:String*/ = "fully qualified error";
    return/* package2.TestStaticAccess.*/s2$static;
  }/*

  static public*/ function set_s2_fully_qualified$static(s2/*:String*/)/*:void*/ {/*
    package2.TestStaticAccess.*/s2$static = s2;
  }/*

  static private*/ function get_s2_private$static()/*:String*/ {
    return s2$static;
  }/*

  static public*/ function get_s2_via_private_static_method$static()/*:String*/ {
    return get_s2_private$static();
  }/*

  static public*/ function get_s2_via_private_static_method_qualified$static()/*:String*/ {
    return/* TestStaticAccess.*/get_s2_private$static();
  }/*

  static public*/ function get_s2_via_private_static_method_full_qualified$static()/*:String*/ {
    return/* package2.TestStaticAccess.*/get_s2_private$static();
  }/*

}
}

============================================== Jangaroo part ==============================================*/
    return {
      extend: "package2.StaticAccessSuperClass",
      constructor: TestStaticAccess$,
      statics: {
        s1: {
          value: "s1",
          writable: true
        },
        get_s0: get_s0$static,
        set_s0: set_s0$static,
        get_s0_qualified: get_s0_qualified$static,
        set_s0_qualified: set_s0_qualified$static,
        get_s0_fully_qualified: get_s0_fully_qualified$static,
        set_s0_fully_qualified: set_s0_fully_qualified$static,
        get_s1: get_s1$static,
        set_s1: set_s1$static,
        get_s1_qualified: get_s1_qualified$static,
        set_s1_qualified: set_s1_qualified$static,
        get_s1_fully_qualified: get_s1_fully_qualified$static,
        set_s1_fully_qualified: set_s1_fully_qualified$static,
        get_s2: get_s2$static,
        set_s2: set_s2$static,
        get_s2_qualified: get_s2_qualified$static,
        set_s2_qualified: set_s2_qualified$static,
        get_s2_fully_qualified: get_s2_fully_qualified$static,
        set_s2_fully_qualified: set_s2_fully_qualified$static,
        get_s2_via_private_static_method: get_s2_via_private_static_method$static,
        get_s2_via_private_static_method_qualified: get_s2_via_private_static_method_qualified$static,
        get_s2_via_private_static_method_full_qualified: get_s2_via_private_static_method_full_qualified$static
      }
    };
});
