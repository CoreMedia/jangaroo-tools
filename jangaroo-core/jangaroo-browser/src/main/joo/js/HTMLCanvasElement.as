package js{
public class HTMLCanvasElement extends HTMLElement {

  public native function get width() : uint;

  public native function set width(width : uint) : void;

  public native function get height() : uint;

  public native function set height(height : uint) : void;

  /**
   * Returns a data: URL for the image in the canvas.
   * @param type if provided, controls the type of the image to be returned (e.g. PNG or JPEG).
   *   The default is image/png; that type is also used if the given type isn't supported.
   * @param args arguments specific to the type. They control the way that the image is generated.
   *   For image/jpeg, the second argument, if it is a number between 0.0 and 1.0, is treated as the desired
   *   quality level. If it is not a number or is outside that range, the default value is used,
   *   as if the argument had been omitted.
   * @return a data: URL for the image in the canvas.
   */
  public native function toDataURL(type : String = "image/png", ...args) : String;

  /**
   * Returns an object that exposes an API for drawing on the canvas.
   * Returns null if the given context ID is not supported.
   * The specification only defines one context, with the name "2d". If getContext() is called with that
   * exact string for its contextId argument, then the result is a reference to an object implementing
   * CanvasRenderingContext2D. Other extensions may define their own contexts, which would return
   * different objects.
   * @param type the type of context to return, where only "2d" has to be supported.
   * @return an object that exposes an API for drawing on the canvas, for "2d", a CanvasRenderingContext2D.
   */
  public native function getContext(type : String) : Object;
}
}