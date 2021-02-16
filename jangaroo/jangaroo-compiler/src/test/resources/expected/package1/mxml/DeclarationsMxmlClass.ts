import {_} from '@jangaroo/joo/AS3';
import SomeNativeClass from '../someOtherPackage/SomeNativeClass';
import Exml from '../../net/jangaroo/ext/Exml';
import ConfigClass from '../ConfigClass';
import SomeOtherClass from '../someOtherPackage/SomeOtherClass';
import int from '../../AS3/int_';

class DeclarationsMxmlClassConfigs {

    
     bar:string = null;

    /**
     Some number.
     */
    
     num:int = 0;

    /**
     Empty declaration.
     */
    
     empty:int = 0;

    
     blub:any = null;

    
     list:Array<any> = null;

    
     other:SomeOtherClass = null;}
interface DeclarationsMxmlClass_ extends Partial<DeclarationsMxmlClassConfigs> {
}


class DeclarationsMxmlClass<Cfg extends DeclarationsMxmlClass._ = DeclarationsMxmlClass._> extends SomeNativeClass<Cfg>{constructor(config:DeclarationsMxmlClass._=null){
    super();
    this.setConfig("bar" , "BAR!");
    /**
     Some number.
     */
    this.setConfig("num" , 123);
    this.setConfig("blub" ,{ name: "Kuno"});
    this.setConfig("list" ,[
      { name: "Joe"},
      new ConfigClass(_<ConfigClass._>({
        items:[
          new SomeOtherClass(_<SomeOtherClass._>({ bla: 123}))
        ]
      }))
    ]);
    this.setConfig("other" , new SomeOtherClass(_<SomeOtherClass._>({ bla: 3,
                        blubb_config: 'blub config expression',
                        blubb_accessor: 'blub accessor expression'}))); Exml.apply(this,config);
}}
declare namespace DeclarationsMxmlClass {
  export type _ = DeclarationsMxmlClass_;
  export const _: { new(config?: _): _; };
}


export default DeclarationsMxmlClass;
