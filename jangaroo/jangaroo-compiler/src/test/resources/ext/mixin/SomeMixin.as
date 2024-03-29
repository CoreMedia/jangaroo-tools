package ext.mixin {

import ext.Base;

[Native("Ext.mixin.SomeMixin", require)]
[ExtConfig]
public class SomeMixin extends Base {

  public function SomeMixin(config:Object = null) {
    super(config);
  }


  public native function get someList():Array;

  /**
   * @private
   */
  [ExtConfig(mode="append")]
  public native function set someList(value:Array):void;

}
}
