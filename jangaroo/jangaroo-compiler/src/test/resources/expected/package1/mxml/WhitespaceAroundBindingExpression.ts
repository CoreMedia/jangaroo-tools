import { cast } from "@jangaroo/joo/AS3";
import Exml from "../../net/jangaroo/ext/Exml";
import Panel from "../../ext/Panel";
import ContainerLayout from "../../ext/layout/ContainerLayout";
interface WhitespaceAroundBindingExpression_ {
}


class WhitespaceAroundBindingExpression extends Panel{constructor(readonly config:WhitespaceAroundBindingExpression._){
    super( Exml.apply(new WhitespaceAroundBindingExpression._({
  layout: new ContainerLayout()
}),config));
}}
declare namespace WhitespaceAroundBindingExpression {
  export type _ = WhitespaceAroundBindingExpression_;
  export const _: { new(config?: _): _; };
}


export default WhitespaceAroundBindingExpression;
