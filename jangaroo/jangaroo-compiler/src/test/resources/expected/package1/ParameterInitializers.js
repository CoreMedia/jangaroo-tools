/*package package1 {*/

Ext.define("package1.ParameterInitializers", function(ParameterInitializers) {/*public class ParameterInitializers {
  public*/ function ParameterInitializers$(str/* = "foo"*/, integer/* = 1*/, num2/* = NaN*/,
                                        bool/* = false*/, obj/* = null*/, undef/* = undefined*/) {switch(arguments.length){case 0:str="foo";case 1:integer=1;case 2:num2=NaN;case 3:bool=false;case 4:obj=null;}
  }/*
}
}

============================================== Jangaroo part ==============================================*/
    return {
      constructor: ParameterInitializers$,
      uses: [
        "NaN",
        "undefined"
      ]
    };
});
