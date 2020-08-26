package net.jangaroo.ext {

/**
 * integration test stub
 */
public class Exml {
  public static native function append(arr:Array):Array;

  public static native function prepend(arr:Array):Array;

  public static native function apply(config:Object, overrideConfig:Object):*;

  public static native function asString(value:*):String;

  public static native function eventHandler(flexEventName:String, flexEventClass:Class, flexEventHandler:Function):Function;

}
}
