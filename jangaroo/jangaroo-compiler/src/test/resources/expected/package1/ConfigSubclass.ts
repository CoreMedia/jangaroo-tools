import { asConfig } from "@jangaroo/runtime";
import ConfigClass from "./ConfigClass";


class ConfigSubclass extends ConfigClass {

  override get defaultType():string {
    return super.defaultType + "!";
  }
  override set defaultType(value:string) {
    super.defaultType = value;
  }


  override set title(value:string) {
    super.title = value + "!";
  }
  override get title():string {
    return super.title;
  }

}
export default ConfigSubclass;
