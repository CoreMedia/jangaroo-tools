joo.classLoader.prepare("package flash.geom",/* {*/

/**
 * The ColorTransform class lets you adjust the color values in a display object. The color adjustment or <i>color transformation</i> can be applied to all four channels: red, green, blue, and alpha transparency.
 * <p>When a ColorTransform object is applied to a display object, a new value for each color channel is calculated like this:</p>
 * <ul>
 * <li>New red value = (old red value * <code>redMultiplier</code>) + <code>redOffset</code></li>
 * <li>New green value = (old green value * <code>greenMultiplier</code>) + <code>greenOffset</code></li>
 * <li>New blue value = (old blue value * <code>blueMultiplier</code>) + <code>blueOffset</code></li>
 * <li>New alpha value = (old alpha value * <code>alphaMultiplier</code>) + <code>alphaOffset</code></li></ul>
 * <p>If any of the color channel values is greater than 255 after the calculation, it is set to 255. If it is less than 0, it is set to 0.</p>
 * <p>You can use ColorTransform objects in the following ways:</p>
 * <ul>
 * <li>In the <code>colorTransform</code> parameter of the <code>colorTransform()</code> method of the BitmapData class</li>
 * <li>As the <code>colorTransform</code> property of a Transform object (which can be used as the <code>transform</code> property of a display object)</li></ul>
 * <p>You must use the <code>new ColorTransform()</code> constructor to create a ColorTransform object before you can call the methods of the ColorTransform object.</p>
 * <p>Color transformations do not apply to the background color of a movie clip (such as a loaded SWF object). They apply only to graphics and symbols that are attached to the movie clip.</p>
 * <p><a href="http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/geom/ColorTransform.html#includeExamplesSummary">View the examples</a></p>
 * @see Transform
 * @see flash.display.DisplayObject#transform
 * @see flash.display.BitmapData#colorTransform()
 * @see http://help.adobe.com/en_US/Flex/4.0/UsingSDK/WSda78ed3a750d6b8fee1b36612357de97a3-8000.html Using MXML graphics
 *
 */
"public class ColorTransform",1,function($$private){;return[ 
  /**
   * A decimal value that is multiplied with the alpha transparency channel value.
   * <p>If you set the alpha transparency value of a display object directly by using the <code>alpha</code> property of the DisplayObject instance, it affects the value of the <code>alphaMultiplier</code> property of that display object's <code>transform.colorTransform</code> property.</p>
   * @see flash.display.DisplayObject#alpha
   *
   */
  "public var",{ alphaMultiplier/*:Number*/:NaN},
  /**
   * A number from -255 to 255 that is added to the alpha transparency channel value after it has been multiplied by the <code>alphaMultiplier</code> value.
   */
  "public var",{ alphaOffset/*:Number*/:NaN},
  /**
   * A decimal value that is multiplied with the blue channel value.
   */
  "public var",{ blueMultiplier/*:Number*/:NaN},
  /**
   * A number from -255 to 255 that is added to the blue channel value after it has been multiplied by the <code>blueMultiplier</code> value.
   */
  "public var",{ blueOffset/*:Number*/:NaN},

  /**
   * The RGB color value for a ColorTransform object.
   * <p>When you set this property, it changes the three color offset values (<code>redOffset</code>, <code>greenOffset</code>, and <code>blueOffset</code>) accordingly, and it sets the three color multiplier values (<code>redMultiplier</code>, <code>greenMultiplier</code>, and <code>blueMultiplier</code>) to 0. The alpha transparency multiplier and offset values do not change.</p>
   * <p>When you pass a value for this property, use the format 0x<i>RRGGBB</i>. <i>RR</i>, <i>GG</i>, and <i>BB</i> each consist of two hexadecimal digits that specify the offset of each color component. The 0x tells the ActionScript compiler that the number is a hexadecimal value.</p>
   */
  "public function get color",function color$get()/*:uint*/ {
    return this.redOffset << 16 | this.greenOffset << 8 || this.blueOffset;
  },

  /**
   * @private
   */
  "public function set color",function color$set(value/*:uint*/)/*:void*/ {
    this.redOffset = value >> 16 & 0xF;
    this.greenOffset = value >> 8 & 0xF;
    this.blueOffset = value & 0xF;
    this.redMultiplier = this.greenMultiplier = this.blueMultiplier = 1;
  },

  /**
   * A decimal value that is multiplied with the green channel value.
   */
  "public var",{ greenMultiplier/*:Number*/:NaN},
  /**
   * A number from -255 to 255 that is added to the green channel value after it has been multiplied by the <code>greenMultiplier</code> value.
   */
  "public var",{ greenOffset/*:Number*/:NaN},
  /**
   * A decimal value that is multiplied with the red channel value.
   */
  "public var",{ redMultiplier/*:Number*/:NaN},
  /**
   * A number from -255 to 255 that is added to the red channel value after it has been multiplied by the <code>redMultiplier</code> value.
   */
  "public var",{ redOffset/*:Number*/:NaN},

  /**
   * Creates a ColorTransform object for a display object with the specified color channel values and alpha values.
   * @param redMultiplier The value for the red multiplier, in the range from 0 to 1.
   * @param greenMultiplier The value for the green multiplier, in the range from 0 to 1.
   * @param blueMultiplier The value for the blue multiplier, in the range from 0 to 1.
   * @param alphaMultiplier The value for the alpha transparency multiplier, in the range from 0 to 1.
   * @param redOffset The offset value for the red color channel, in the range from -255 to 255.
   * @param greenOffset The offset value for the green color channel, in the range from -255 to 255.
   * @param blueOffset The offset for the blue color channel value, in the range from -255 to 255.
   * @param alphaOffset The offset for alpha transparency channel value, in the range from -255 to 255.
   *
   */
  "public function ColorTransform",function ColorTransform$(redMultiplier/*:Number = 1.0*/, greenMultiplier/*:Number = 1.0*/, blueMultiplier/*:Number = 1.0*/, alphaMultiplier/*:Number = 1.0*/, redOffset/*:Number = 0*/, greenOffset/*:Number = 0*/, blueOffset/*:Number = 0*/, alphaOffset/*:Number = 0*/) {if(arguments.length<8){if(arguments.length<7){if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){redMultiplier = 1.0;}greenMultiplier = 1.0;}blueMultiplier = 1.0;}alphaMultiplier = 1.0;}redOffset = 0;}greenOffset = 0;}blueOffset = 0;}alphaOffset = 0;}
    this.redMultiplier = redMultiplier;
    this.greenMultiplier = greenMultiplier;
    this.blueMultiplier = blueMultiplier;
    this.alphaMultiplier = alphaMultiplier;
    this.redOffset = redOffset;
    this.greenOffset = greenOffset;
    this.blueOffset = blueOffset;
    this.alphaOffset = alphaOffset;
  },

  /**
   * Concatenates the ColorTransform object specified by the <code>second</code> parameter with the current ColorTransform object and sets the current object as the result, which is an additive combination of the two color transformations. When you apply the concatenated ColorTransform object, the effect is the same as applying the <code>second</code> color transformation after the <i>original</i> color transformation.
   * @param second The ColorTransform object to be combined with the current ColorTransform object.
   *
   */
  "public function concat",function concat(second/*:ColorTransform*/)/*:void*/ {
    this.redMultiplier *= second.redMultiplier;
    this.greenMultiplier *= second.greenMultiplier;
    this.blueMultiplier *= second.blueMultiplier;
    this.alphaMultiplier *= second.alphaMultiplier;
    this.redOffset += second.redOffset;
    this.greenOffset += second.greenOffset;
    this.blueOffset += second.blueOffset;
    this.alphaOffset += second.alphaOffset;
  },

  /**
   * Formats and returns a string that describes all of the properties of the ColorTransform object.
   * @return A string that lists all of the properties of the ColorTransform object.
   *
   */
  "public function toString",function toString()/*:String*/ {
    return "[ColorTransform(" + [this.redMultiplier, this.greenMultiplier, this.blueMultiplier, this.alphaMultiplier,
      this.redOffset, this.greenOffset, this.blueOffset, this.alphaOffset].join(", ") + ")]";
  },

  "private var",{ maps/*:Array*/:null},

  "public function getComponentMaps",function getComponentMaps()/*:Array*/ {
    if (!this.maps$1) {
      var offsets/*:Array*/ = [this.redOffset, this.greenOffset, this.blueOffset, this.alphaOffset];
      var multipliers/*:Array*/ = [this.redMultiplier, this.greenMultiplier, this.blueMultiplier, this.alphaMultiplier];
      this.maps$1 = new Array(4);
      for (var c/*:uint*/ = 0; c < 4; ++c) {
        var offset/*:Number*/ = offsets[c];
        var multiplier/*:Number*/ = multipliers[c];
        var map/*:Array*/;
        if (offset == 0 && multiplier == 1) {
          map = null;
        } else {
          map = new Array(256);
          for (var b/*:uint*/ = 0; b < 256; ++b) {
            var val/*:Number*/ = offset + multiplier * b;
            map[b] = val <= 0 ? 0 : val <= 255 ? val : 255;
          }
        }
        this.maps$1[c] = map;
      }
    }
    return this.maps$1;
  },

];},[],["Array"], "0.8.0", "0.8.1"
);