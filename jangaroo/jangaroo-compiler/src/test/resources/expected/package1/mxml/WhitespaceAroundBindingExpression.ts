import { cast } from "@jangaroo/runtime/AS3";
import Panel from "../../ext/Panel";
import ContainerLayout from "../../ext/layout/ContainerLayout";
import Exml from "../../net/jangaroo/ext/Exml";
interface WhitespaceAroundBindingExpression_ {
}


class WhitespaceAroundBindingExpression extends Panel{
  declare readonly initialConfig: WhitespaceAroundBindingExpression._;constructor(config:WhitespaceAroundBindingExpression._=null){
    super( Exml.apply(new WhitespaceAroundBindingExpression._({
  layout: new ContainerLayout()
}),config));
}}
declare namespace WhitespaceAroundBindingExpression {
  export type _ = WhitespaceAroundBindingExpression_;
  export const _: { new(config?: _): _; };
}


export default WhitespaceAroundBindingExpression;
