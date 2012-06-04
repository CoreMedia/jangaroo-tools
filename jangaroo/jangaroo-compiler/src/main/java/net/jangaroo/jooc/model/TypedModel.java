package net.jangaroo.jooc.model;

/**
 * Created with IntelliJ IDEA. User: fwienber Date: 21.05.12 Time: 15:55 To change this template use File | Settings |
 * File Templates.
 */
public interface TypedModel extends ActionScriptModel {
  String getType();

  void setType(String type);
}
