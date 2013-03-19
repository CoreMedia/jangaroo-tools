package package1 {

/**
 * This is an example of a class using an [Embed] annotation.
 */
public class UsingEmbed {

  [Embed(source="UsingEmbed.as")]
  public var someText:Class;

  [Embed(source="Interface.as")]
  private static var anotherText:Class;

  [Embed(source="jooley.png")]
  private static var jooley:Class;

}
}