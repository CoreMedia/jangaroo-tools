package ext {
import ext.layout.ContainerLayout;

[Native("Ext.Container", require)]
public class Container extends Component {

  public function Container(config:Object = null) {
  }

  /**
   * defaults for children
   */
  public native function get defaults():Component;
  /**
   * @private
   */
  public native function set defaults(value:Component):void;

  /**
   * this container's children
   */
  [DefaultProperty]
  public native function get items():Array;
  /**
   * @private
   */
  [DefaultProperty]
  public native function set items(value:Array):void;

  public native function get layout():ContainerLayout;

  public native function set layout(value:ContainerLayout):void;
  
  public native function get layoutConfig():Object;

  public native function set layoutConfig(value:Object):void;
}
}
