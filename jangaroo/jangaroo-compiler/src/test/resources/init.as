package {
import package1.someOtherPackage.SomeEvent;
import package1.someOtherPackage.SomeOtherClass;

public function init():void {
   new SomeOtherClass().addEventListener(SomeEvent.CLICK_CLACK, function (event: SomeEvent):void {
     const title: String = event.source.title;
   });
}
}
