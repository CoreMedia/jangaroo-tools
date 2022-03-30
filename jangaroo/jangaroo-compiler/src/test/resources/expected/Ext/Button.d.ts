import Config from "@jangaroo/runtime/Config";
import { AnyFunction } from "@jangaroo/runtime/types";
import Component from "./Component";
interface ButtonConfig extends Config<Component>, Partial<Pick<Button,
  "toolTip" |
  "text" |
  "handler"
>> {
}



declare class Button extends Component {
  declare Config: ButtonConfig;

  constructor(config?:Config<Button>)

  /**
   * The tool tip of the button
   */
  get toolTip():string;
  set toolTip(value:string);

  /**
   * The text of the button
   */
   get text():string;
  /**
   * @private
   */
   set text(value:string);

  handler:AnyFunction;
}
export default Button;
