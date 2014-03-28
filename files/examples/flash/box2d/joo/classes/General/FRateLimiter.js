joo.classLoader.prepare(//===========================================================
//=========================================================//
//						-=ANTHEM=-
//	file: frameLimiter.as
//
//	copyright: Matthew Bush 2007
//
//	notes: limits framerate
//
//=========================================================//
//===========================================================



//===========================================================
// frame limiter
//===========================================================

"package General",/*{
	
	
	import flash.utils.getTimer*/
	
	
	"public class FRateLimiter",1,function($$private){;return[
		
		
		//======================
		// limit frame function
		//======================
		"static public function limitFrame",function limitFrame(maxFPS/*:uint*/)/*:void*/{
			
			var fTime/*:uint*/ = 1000 / maxFPS;
			
			while(Math.abs($$private.newT - $$private.oldT) < fTime){
				$$private.newT = flash.utils.getTimer();
			}
			$$private.oldT = flash.utils.getTimer();
			
		},
		
		//======================
		// member vars
		//======================
		"private static var",{ oldT/*:uint*/ :function(){return( flash.utils.getTimer());}},
		"private static var",{ newT/*:uint*/ :function(){return( $$private.oldT);}},
	];},["limitFrame"],["Math"], "0.8.0", "0.8.1"

);