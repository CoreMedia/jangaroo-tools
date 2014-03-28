joo.classLoader.prepare("package flash.display",/* {*/


/**
 * The StageQuality class provides values for the <code>Stage.quality</code> property.
 * @see Stage#quality
 *
 */
"public final class StageQuality",1,function($$private){;return[ 
  /**
   * Specifies very high rendering quality: graphics are anti-aliased using a 4 x 4 pixel grid and bitmaps are always smoothed.
   */
  "public static const",{ BEST/*:String*/ : "best"},
  /**
   * Specifies high rendering quality: graphics are anti-aliased using a 4 x 4 pixel grid, and bitmaps are smoothed if the movie is static.
   */
  "public static const",{ HIGH/*:String*/ : "high"},
  /**
   * Specifies low rendering quality: graphics are not anti-aliased, and bitmaps are not smoothed. This setting is not supported in Adobe AIR.
   */
  "public static const",{ LOW/*:String*/ : "low"},
  /**
   * Specifies medium rendering quality: graphics are anti-aliased using a 2 x 2 pixel grid, but bitmaps are not smoothed. This setting is suitable for movies that do not contain text. This setting is not supported in Adobe AIR.
   */
  "public static const",{ MEDIUM/*:String*/ : "medium"},
];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);