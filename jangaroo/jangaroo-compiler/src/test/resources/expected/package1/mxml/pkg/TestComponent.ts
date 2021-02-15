import {cast} from '@jangaroo/joo/AS3';
import Exml from '../../../net/jangaroo/ext/Exml';
import TestComponentBase from './TestComponentBase';
import int from '../../../AS3/int_';

class TestComponentConfigs {

    
     property_1:string = null;


    
     property_2:int = 0;}
interface TestComponent_ extends TestComponentBase._, Partial<TestComponentConfigs> {
}


class TestComponent<Cfg extends TestComponent._ = TestComponent._> extends TestComponentBase<Cfg>{

     constructor(config:TestComponent._ = null){
    config = Exml.apply({
    property_1: "withDefault"
    },config);
    super( Exml.apply(new TestComponent._({
        emptyText: Exml.asString( '<div class=\'widget-content-list-empty\'>' + TestComponentBase.DEFAULT + '</div>'),
        letters: [
              'a',
              'b',
              'c'
             ]

}),config));
  }}
declare namespace TestComponent {
  export type _ = TestComponent_;
  export const _: { new(config?: _): _; };
}


export default TestComponent;
