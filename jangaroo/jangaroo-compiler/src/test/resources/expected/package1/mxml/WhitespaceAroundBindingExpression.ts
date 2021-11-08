import Config from "@jangaroo/runtime/Config";
import Panel from "../../Ext/Panel";
import ContainerLayout from "../../Ext/layout/ContainerLayout";
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
