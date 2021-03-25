
class NoMultipleThisAliases {
  constructor() {const this$=this;
    function foo1():void {
      this$.#method();
    }
    function foo2():void {
      this$.#method();
    }
  }

  //@ts-expect-error 18022
  #method():void {}
}
export default NoMultipleThisAliases;
