package js{
public class CSSPrimitiveValue {

  public static const CSS_UNKNOWN : Number = 0;
  public static const CSS_NUMBER : Number = 1;
  /**
   * Many CSS properties take percentage values, such as width , margin-top , and font-size . The CSS
   * syntax for percentage is a number followed immediately by a % (percentage sign).
   */
  public static const CSS_PERCENTAGE : Number = 2;
  /**
   * The font-size of the element.
   */
  public static const CSS_EMS : Number = 3;
  /**
   * The x-height of the element's font . This is generally the height of lowercase letters in the font.
   */
  public static const CSS_EXS : Number = 4;
  /**
   * For screen display, one pixel (dot) of the display. For very high resolution screens and for printers,
   * multiple pixels, so that the number of px per inch stays around 96.
   */
  public static const CSS_PX : Number = 5;
  /**
   * One centimeter (which is 10 millimeters). For screen display, the number of pixels in an centimeter is
   * determined by the system's estimate (often incorrect) of the resolution of its display.
   */
  public static const CSS_CM : Number = 6;
  /**
   * One millimeter. For screen display, the number of pixels in an millimeter is determined by the system's
   * estimate (often incorrect) of the resolution of its display.
   */
  public static const CSS_MM : Number = 7;
  /**
   * One inch (which is 2.54 centimeters). For screen display, the number of pixels in an inch is determined
   * by the system's estimate (often incorrect) of the resolution of its display.
   */
  public static const CSS_IN : Number = 8;
  /**
   * One point (which is 1/72 of an inch). For screen display, the number of pixels in an point is determined
   * by the system's estimate (often incorrect) of the resolution of its display.
   */
  public static const CSS_PT : Number = 9;
  /**
   * One pica (which is 12 points). For screen display, the number of pixels in an pica is determined by the
   * system's estimate (often incorrect) of the resolution of its display.
   */
  public static const CSS_PC : Number = 10;
  public static const CSS_DEG : Number = 11;
  public static const CSS_RAD : Number = 12;
  public static const CSS_GRAD : Number = 13;
  public static const CSS_MS : Number = 14;
  public static const CSS_S : Number = 15;
  public static const CSS_HZ : Number = 16;
  public static const CSS_KHZ : Number = 17;
  public static const CSS_DIMENSION : Number = 18;
  public static const CSS_STRING : Number = 19;
  public static const CSS_URI : Number = 20;
  public static const CSS_IDENT : Number = 21;
  public static const CSS_ATTR : Number = 22;
  public static const CSS_COUNTER : Number = 23;
  public static const CSS_RECT : Number = 24;
  public static const CSS_RGBCOLOR : Number = 25;

  public function CSSPrimitiveValue() {
    super();
  }
}
}
