package com.coremedia.ui.components {
import com.coremedia.ui.util.ObjectUtils;

import ext.BoxComponent;
import ext.ComponentMgr;
import ext.DomHelper;
import ext.Element;
import ext.Ext;

public class ImageComponent extends BoxComponent {

  public static const xtype:String = "Image";
  {
    ComponentMgr.registerType(xtype, ImageComponent);
  }
  /**
   * @cfg {Function} handler image click handler
   * @cfg {String} src the image src
   *@param config
   */
  public function ImageComponent(config:* = undefined) {
    super(Ext.apply(config, {}));
  }

}
}
