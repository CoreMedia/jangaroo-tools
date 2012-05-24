package net.jangaroo.jooc.model;

/**
 * Created with IntelliJ IDEA. User: fwienber Date: 15.05.12 Time: 00:22 To change this template use File | Settings |
 * File Templates.
 */
public interface ValuedModel extends ActionScriptModel {
  String getValue();

  void setValue(String value);
}
