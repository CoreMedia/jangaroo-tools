joo.classLoader.prepare("package js",/*{*/
"public interface CanvasRenderingContext2D",1,function($$private){;return[ /*

  // back-reference to the canvas
  function canvas() : HTMLCanvasElement;*/,/*

  // state
  function save() : void;*/,/* // push state on state stack
  function restore() : void;*/,/* // pop state stack and restore state

  // transformations (default transform is the identity matrix)
  function scale(x : Number, y : Number) : void;*/,/*

  function rotate(angle : Number) : void;*/,/*

  function translate(x : Number, y : Number) : void;*/,/*

  function transform(m11 : Number, m12 : Number, m21 : Number, m22 : Number, dx : Number, dy : Number) : void;*/,/*

  function setTransform(m11 : Number, m12 : Number, m21 : Number, m22 : Number, dx : Number, dy : Number) : void;*/,/*

  // compositing
  function globalAlpha() : Number;*/,/* // (default 1.0)
  function globalAlpha(alpha : Number) : void;*/,/*

  function globalCompositeOperation() : String;*/,/* // (default source-over)
  function globalCompositeOperation(op : String) : void;*/,/*

  // colors and styles
  function strokeStyle() : *;*/,/* // (default black)
  function strokeStyle(style : *) : void;*/,/*

  function fillStyle() : *;*/,/* // (default black)
  function fillStyle(style : *) : void;*/,/*

  function createLinearGradient(x0 : Number, y0 : Number, x1 : Number, y1 : Number) : CanvasGradient;*/,/*

  function createRadialGradient(x0 : Number, y0 : Number, r0 : Number, x1 : Number, y1 : Number, r1 : Number) : CanvasGradient;*/,/*

  /**
   * @param image HTMLImageElement, HTMLCanvasElement, or HTMLVideoElement.
   * @param repetition
   * @return
   * /
  function createPattern(image : HTMLElement, repetition : String) : CanvasPattern;*/,/*

  // line caps/joins
  function lineWidth() : Number;*/,/* // (default 1)
  function lineWidth(width : Number) : void;*/,/*

  function lineCap() : String;*/,/* // "butt", "round", "square" (default "butt")
  function lineCap(cap : String) : void;*/,/* // "butt", "round", "square" (default "butt")
  function lineJoin() : String;*/,/* // "round", "bevel", "miter" (default "miter")
  function lineJoin(join : String) : void;*/,/* // "round", "bevel", "miter" (default "miter")
  function miterLimit() : Number;*/,/* // (default 10)
  function miterLimit(limit : Number) : void;*/,/* // (default 10)

  // shadows
  function shadowOffsetX() : Number;*/,/* // (default 0)
  function shadowOffsetX(x : Number) : void;*/,/*

  function shadowOffsetY() : Number;*/,/* // (default 0)
  function shadowOffsetY(y : Number) : void;*/,/*

  function shadowBlur() : Number;*/,/* // (default 0)
  function shadowBlur(blur : Number) : void;*/,/*

  function shadowColor() : String;*/,/* // (default transparent black)
  function shadowColor(color : String) : void;*/,/*

  // rects
  function clearRect(x : Number, y : Number, w : Number, h : Number) : void;*/,/*

  function fillRect(x : Number, y : Number, w : Number, h : Number) : void;*/,/*

  function strokeRect(x : Number, y : Number, w : Number, h : Number) : void;*/,/*

  // path API
  function beginPath() : void;*/,/*

  function closePath() : void;*/,/*

  function moveTo(x : Number, y : Number) : void;*/,/*

  function lineTo(x : Number, y : Number) : void;*/,/*

  function quadraticCurveTo(cpx : Number, cpy : Number, x : Number, y : Number) : void;*/,/*

  function bezierCurveTo(cp1x : Number, cp1y : Number, cp2x : Number, cp2y : Number, x : Number, y : Number) : void;*/,/*

  function arcTo(x1 : Number, y1 : Number, x2 : Number, y2 : Number, radius : Number) : void;*/,/*

  function rect(x : Number, y : Number, w : Number, h : Number) : void;*/,/*

  function arc(x : Number, y : Number, radius : Number, startAngle : Number, endAngle : Number, anticlockwise : Boolean) : void;*/,/*

  function fill() : void;*/,/*

  function stroke() : void;*/,/*

  function clip() : void;*/,/*

  function isPointInPath(x : Number, y : Number) : Boolean;*/,/*

  // text
  function font() : String;*/,/* // (default 10px sans-serif)
  function font(font : String) : void;*/,/*

  function textAlign() : String;*/,/* // "start", "end", "left", "right", "center" (default: "start")
  function textAlign(align : String) : void;*/,/*

  function textBaseline() : String;*/,/* // "top", "hanging", "middle", "alphabetic", "ideographic", "bottom" (default: "alphabetic")
  function textBaseline(baseline : String) : void;*/,/*

  function fillText(text : String, x : Number, y : Number, maxWidth : Number = undefined) : void;*/,/*

  function strokeText(text : String, x : Number, y : Number, maxWidth : Number = undefined) : void;*/,/*

  function measureText(text : String) : TextMetrics;*/,/*

  /**
   * To draw images onto the canvas, the drawImage method can be used.
   * This method can be invoked with three different sets of arguments:
   * drawImage(image, dx, dy)
   * drawImage(image, dx, dy, dw, dh)
   * drawImage(image, sx, sy, sw, sh, dx, dy, dw, dh)
   * @param image HTMLImageElement, HTMLCanvasElement, or HTMLVideoElement.
   * /
  function drawImage(image : HTMLElement, dxOrSx : Number, dyOrSy : Number,
                     dwOrSw : Number = undefined, dhOrSh : Number = undefined,
                     dx : Number = undefined, dy : Number = undefined,
                     dw : Number = undefined, dh : Number = undefined) : void;*/,/*

  /**
   *   pixel manipulation
   * @param imagedataOrSw - ImageData or Number
   * @param sh
   * @return
   * /
  function createImageData(imagedataOrSw : *, sh : Number) : ImageData;*/,/*

  function getImageData(sx : Number, sy : Number, sw : Number, sh : Number) : ImageData;*/,/*

  function putImageData(imagedata : ImageData, dx : Number, dy : Number, dirtyX : Number = undefined, dirtyY : Number = undefined, dirtyWidth : Number = undefined, dirtyHeight : Number = undefined) : void;*/,

];},[],[], "0.8.0", "0.8.2-SNAPSHOT"
);