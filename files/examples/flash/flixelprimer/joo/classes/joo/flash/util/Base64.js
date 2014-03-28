joo.classLoader.prepare(/**
*
*  Base64 encode / decode
*  http://www.webtoolkit.info/
*
**/

"package joo.flash.util",/* {
import flash.utils.ByteArray*/

"public class Base64",1,function($$private){;return[ 

  "private static const",{ keyStr/* : String*/ : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="},

  "public static function encodeBytes",function encodeBytes(input/* : ByteArray*/)/* : String*/ {
    var output/* : Array*/ = [];
    for (var i/* : Number*/ = 0; i < input.length; ) {
      var chr1/* : Number*/ = input[i++];
      var chr2/* : Number*/ = input[i++];
      var chr3/* : Number*/ = input[i++];

      var enc1/* : Number*/ = chr1 >> 2;
      var enc2/* : Number*/ = ((chr1 & 3) << 4) | (chr2 >> 4);
      var enc3/* : Number*/ = ((chr2 & 15) << 2) | (chr3 >> 6);
      var enc4/* : Number*/ = chr3 & 63;

      if (isNaN(chr2)) {
        enc3 = enc4 = 64;
      } else if (isNaN(chr3)) {
        enc4 = 64;
      }

      output.push($$private.keyStr.charAt(enc1),
                  $$private.keyStr.charAt(enc2),
                  $$private.keyStr.charAt(enc3),
                  $$private.keyStr.charAt(enc4));
    }
    return output.join("");
  },

  "public static function encode",function encode(input/* : String*/)/* : String*/ {
    var output/* : Array*/ = [];
    input = $$private.utf8Encode(input);

    for (var i/* : Number*/ = 0; i < input.length; ) {
      var chr1/* : Number*/ = input.charCodeAt(i++);
      var chr2/* : Number*/ = input.charCodeAt(i++);
      var chr3/* : Number*/ = input.charCodeAt(i++);

      var enc1/* : Number*/ = chr1 >> 2;
      var enc2/* : Number*/ = ((chr1 & 3) << 4) | (chr2 >> 4);
      var enc3/* : Number*/ = ((chr2 & 15) << 2) | (chr3 >> 6);
      var enc4/* : Number*/ = chr3 & 63;

      if (isNaN(chr2)) {
        enc3 = enc4 = 64;
      } else if (isNaN(chr3)) {
        enc4 = 64;
      }

      output.push($$private.keyStr.charAt(enc1),
                  $$private.keyStr.charAt(enc2),
                  $$private.keyStr.charAt(enc3),
                  $$private.keyStr.charAt(enc4));
    }

    return output.join("");
  },

  "public static function decode",function decode(input/* : String*/)/* : String*/ {
    var output/* : Array*/ = [];
    input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

    for (var i/* : Number*/ = 0; i < input.length; ) {
      var enc1/* : Number*/ = $$private.keyStr.indexOf(input.charAt(i++));
      var enc2/* : Number*/ = $$private.keyStr.indexOf(input.charAt(i++));
      var enc3/* : Number*/ = $$private.keyStr.indexOf(input.charAt(i++));
      var enc4/* : Number*/ = $$private.keyStr.indexOf(input.charAt(i++));

      var chr1/* : Number*/ = (enc1 << 2) | (enc2 >> 4);
      var chr2/* : Number*/ = ((enc2 & 15) << 4) | (enc3 >> 2);
      var chr3/* : Number*/ = ((enc3 & 3) << 6) | enc4;

      output.push(String.fromCharCode(chr1));

      if (enc3 != 64) {
        output.push(String.fromCharCode(chr2));
      }
      if (enc4 != 64) {
        output.push(String.fromCharCode(chr3));
      }
    }
    return $$private.utf8Decode(output.join(""));
  },

  // private method for UTF-8 encoding
  "private static function utf8Encode",function utf8Encode(string/* : String*/)/* : String*/ {
    string = string.replace(/\r\n/g,"\n");
    var utftext/* : Array*/ = [];
    for (var n/* : Number*/ = 0; n < string.length; n++) {
      var c/* : Number*/ = string.charCodeAt(n);
      if (c < 128) {
        utftext.push(String.fromCharCode(c));
      } else if (c < 2048) {
        utftext.push(String.fromCharCode((c >> 6) | 192),
                     String.fromCharCode((c & 63) | 128));
      } else {
        utftext.push(String.fromCharCode((c >> 12) | 224),
                     String.fromCharCode(((c >> 6) & 63) | 128),
                     String.fromCharCode((c & 63) | 128));
      }
    }
    return utftext.join("");
  },

  "private static function utf8Decode",function utf8Decode(utftext/* : String*/)/* : String*/ {
    var string/* : Array*/ = [];
    for (var i/* : Number*/ = 0; i < utftext.length; ) {
      var c/* : Number*/ = utftext.charCodeAt(i++);
      if (c >= 128) {
        var c2/* : Number*/ = utftext.charCodeAt(i++);
        if (c > 191 && c < 224) {
          c = ((c & 31) << 6) | (c2 & 63);
        } else {
          var c3/* : Number*/ = utftext.charCodeAt(i++);
          c = ((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63);
        }
      }
      string.push(String.fromCharCode(c));
    }
    return string.join("");
  },

];},["encodeBytes","encode","decode"],["String"], "0.8.0", "0.8.3"
);