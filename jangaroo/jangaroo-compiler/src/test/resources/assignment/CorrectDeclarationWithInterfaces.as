package assignment{
public class CorrectDeclarationWithInterfaces {

  public function CorrectDeclarationWithInterfaces() {
  }
  public static function make():void {

    var s1:ImplementMe = new SuperBeanImplementer();

    var i:ImplementMe = new Implementer();

    var b:ImplementMe = new BeanImplementer();

    var s:ImplementMe = new SuperImplementer();


  }
}
}