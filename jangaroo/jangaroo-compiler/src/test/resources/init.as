package {
import package1.someOtherPackage.ConfigClass_stranger_antonEvent;
import package1.someOtherPackage.SomeOtherClass;

public function init():void {
   new SomeOtherClass().addEventListener(ConfigClass_stranger_antonEvent.CLICK_CLACK, function (event: ConfigClass_stranger_antonEvent):void {
     const title: String = event.source.title;
   });
}
}
