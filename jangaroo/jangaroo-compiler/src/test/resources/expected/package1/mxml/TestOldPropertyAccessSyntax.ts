import Config from "@jangaroo/runtime/Config";
import { asConfig } from "@jangaroo/runtime";
import Exml from "../../net/jangaroo/ext/Exml";
import PropertiesTest_properties from "../../test/package2/PropertiesTest_properties";
interface TestOldPropertyAccessSyntaxConfig extends Partial<Pick<TestOldPropertyAccessSyntax,
  "foo"
>> {
}


class TestOldPropertyAccessSyntax extends Object{
  declare Config: TestOldPropertyAccessSyntaxConfig;

  static readonly BUNDLE:PropertiesTest_properties = PropertiesTest_properties;constructor(config:Config<TestOldPropertyAccessSyntax> =null){
    super();
    this.foo = TestOldPropertyAccessSyntax.BUNDLE.key + "\""; Exml.apply(this,config);
}

  #foo:string = null;

  get foo():string { return this.#foo; }
  set foo(value:string) { this.#foo = value; }}
export default TestOldPropertyAccessSyntax;
