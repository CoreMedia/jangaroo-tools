import ConfigClass from './ConfigClass';

 class CannotResolveMember {
    #ccList:Array<ConfigClass> = [];

   constructor() {
    var configClass = new ConfigClass();
    configClass['wrong7'];
    configClass['wrong8']();
    configClass.getConfig("title")['wrong9'];

    new ConfigClass()['wrong11'];
    this.#getConfigClass()['wrong12'];
    this.#getConfigClass().getConfig("title")['wrong13'];
    this.#getConfigClass().toString()['wrong14'];

    this.#ccList[0]['wrong16'];
    this.#ccList[0].getConfig("title")['wrong17'];
    this.#getCCList()[0].getConfig("title")['wrong18'];
    this.#getCCList()[0].getConfig("title")['wrong19'];

    var vector:Array<Array<string>> =[["a", "b"]];
    vector[0][0]['wrong22'];
  }

  
  //@ts-expect-error 18022
   #getCCList():Array<ConfigClass> {
    return this.#ccList;
  }

  //@ts-expect-error 18022
   #getConfigClass():ConfigClass {
    return new ConfigClass();
  }

}
export default CannotResolveMember;
