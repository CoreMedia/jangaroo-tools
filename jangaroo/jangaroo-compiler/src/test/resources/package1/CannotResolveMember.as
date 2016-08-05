package package1 {
public class CannotResolveMember {
  [ArrayElementType("package1.ConfigClass")] private var ccList:Array = [];

  public function CannotResolveMember() {
    var configClass:ConfigClass = new ConfigClass();
    configClass.wrong7;
    configClass.wrong8();
    configClass.title.wrong9;

    new ConfigClass().wrong11;
    getConfigClass().wrong12;
    getConfigClass().title.wrong13;
    getConfigClass().toString().wrong14;

    ccList[0].wrong16;
    ccList[0].title.wrong17;
    getCCList()[0].title.wrong18;
    getCCList()[0].title.wrong19;

    var vector:Vector.<Vector.<String>> = new <Vector.<String>>[new <String>["a", "b"]];
    vector[0][0].wrong22;
  }

  [ArrayElementType("package1.ConfigClass")]
  private function getCCList():Array {
    return ccList;
  }

  private function getConfigClass():ConfigClass {
    return new ConfigClass();
  }

}
}