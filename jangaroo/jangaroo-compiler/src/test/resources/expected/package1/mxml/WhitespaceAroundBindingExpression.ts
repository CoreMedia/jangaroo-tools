import Config from "@jangaroo/runtime/AS3/Config";
import { cast } from "@jangaroo/runtime/AS3";
import Panel from "../../ext/Panel";
import ContainerLayout from "../../ext/layout/ContainerLayout";
import Exml from "../../net/jangaroo/ext/Exml";
interface WhitespaceAroundBindingExpressionConfig extends Config<Panel> {
}


class WhitespaceAroundBindingExpression extends Panel{
  declare Config: WhitespaceAroundBindingExpressionConfig;constructor(config:Config<WhitespaceAroundBindingExpression> =null){
    super( Exml.apply(Config(WhitespaceAroundBindingExpression, {
  layout: new ContainerLayout()
}),config));
}}
export default WhitespaceAroundBindingExpression;
