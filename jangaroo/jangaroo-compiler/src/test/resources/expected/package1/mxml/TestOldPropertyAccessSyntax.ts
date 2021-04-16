import { asConfig } from "@jangaroo/runtime/AS3";
import Exml from "../../net/jangaroo/ext/Exml";
import PropertiesTest_properties from "../../testPackage/PropertiesTest_properties";
interface TestOldPropertyAccessSyntax_ extends Partial<Pick<TestOldPropertyAccessSyntax,
  "foo"
>> {
}


class TestOldPropertyAccessSyntax extends Object{
  declare readonly initialConfig: TestOldPropertyAccessSyntax._;

  static readonly BUNDLE:PropertiesTest_properties = PropertiesTest_properties;constructor(config:TestOldPropertyAccessSyntax._=null){
    super();
    this.foo = Exml.asString( TestOldPropertyAccessSyntax.BUNDLE.key + "\""); Exml.apply(this,config);
}

  #foo:string = null;

  get foo():string { return this.#foo; }
  set foo(value:string) { this.#foo = value; }}
declare namespace TestOldPropertyAccessSyntax {
  export type _ = TestOldPropertyAccessSyntax_;
  export const _: { new(config?: _): _; };
}


export default TestOldPropertyAccessSyntax;
