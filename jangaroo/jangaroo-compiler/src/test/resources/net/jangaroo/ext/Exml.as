package net.jangaroo.ext {

/**
 * integration test stub
 */
public class Exml {
  public static const PREPEND:int = -1;

  public static const APPEND:int = 0;

  public static native function apply(config:Object, overrideConfig:Object):*;

  public static native function asString(value:*):String;

  public static native function eventHandler(flexEventName:String, flexEventClass:Class, flexEventHandler:Function):Function;

}
}
