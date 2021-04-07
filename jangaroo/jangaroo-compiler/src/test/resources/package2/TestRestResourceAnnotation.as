package package2 {

/**
 * This is some class documentation.
 */
[RestResource(uriTemplate="test/{id:[0-9]+}")]
public class TestRestResourceAnnotation {
  /**
   * This is some member documentation.
   */
  private static const ANOTHER_CONST: Number = 42;
}
}