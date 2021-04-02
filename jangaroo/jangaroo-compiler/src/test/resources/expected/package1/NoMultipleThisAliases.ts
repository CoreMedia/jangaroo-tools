
class NoMultipleThisAliases {
  constructor() {const this$=this;
    function foo1():void {
      this$.#method();
    }
    function foo2():void {
      this$.#method();
    }
  }

  #method():void {}
}
export default NoMultipleThisAliases;
