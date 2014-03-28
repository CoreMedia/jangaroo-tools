joo.classLoader.prepare("package",/* {
import js.CanvasRenderingContext2D
import js.HTMLCanvasElement
import js.HTMLElement
import js.ImageData*/

"public class CanvasTest",1,function($$private){var as=joo.as;return[ 

  "public static function main",function main(id/*:String*/)/*:void*/ {
    var canvasElement/*:HTMLCanvasElement*/ =as( window.document.getElementById(id),  js.HTMLCanvasElement);
    canvasElement.width = 100;
    canvasElement.height = 100;
    var context/*:CanvasRenderingContext2D*/ =as( canvasElement.getContext("2d"),  js.CanvasRenderingContext2D);
    context.fillRect(10,10,90,90);
    var imageData/*:ImageData*/ = context.getImageData(0,0, 100, 100);
    canvasElement.width = 200;
    canvasElement.height = 200;
    context.putImageData(imageData, 0, 0);
    /*
    var dataURL:String = canvasElement.toDataURL();
    canvasElement.width = 200;
    canvasElement.height = 200;
    var img:HTMLElement = window.document.createElement("IMG") as HTMLElement;
    img['onload'] = function():void {
      context.drawImage(img, 0, 0);
    };
    img.src = dataURL;
    */
  },
];},["main"],["js.HTMLCanvasElement","js.CanvasRenderingContext2D"], "0.8.0", "0.8.1"
);