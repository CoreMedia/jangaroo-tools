package js{
public interface ImageData {

  function get width() : uint;

  function get height() : uint;

  function get data() : Array; // CanvasPixelArray

}
}