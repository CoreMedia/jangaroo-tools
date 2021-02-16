

 class AuxVarConfusion {

   doSomething():void {
    for (var i of Object.values({foo:true} || {})) {
      new AuxVarConfusion();
    }
  }

}
export default AuxVarConfusion;
