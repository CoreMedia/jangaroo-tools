import Exml from '../../net/jangaroo/ext/Exml';
import PropertiesTest_properties from '../../testPackage/PropertiesTest_properties';

class TestOldPropertyAccessSyntaxConfigs {

    
     foo:string = null;}
interface TestOldPropertyAccessSyntax_ extends Partial<TestOldPropertyAccessSyntaxConfigs> {
}


class TestOldPropertyAccessSyntax<Cfg extends TestOldPropertyAccessSyntax._ = TestOldPropertyAccessSyntax._> extends Object<Cfg>{

     static readonly BUNDLE:PropertiesTest_properties = PropertiesTest_properties;constructor(config:TestOldPropertyAccessSyntax._=null){
    super();
    this.setConfig("foo" , Exml.asString( TestOldPropertyAccessSyntax.BUNDLE.key + "\"")); Exml.apply(this,config);
}}
declare namespace TestOldPropertyAccessSyntax {
  export type _ = TestOldPropertyAccessSyntax_;
  export const _: { new(config?: _): _; };
}


export default TestOldPropertyAccessSyntax;
