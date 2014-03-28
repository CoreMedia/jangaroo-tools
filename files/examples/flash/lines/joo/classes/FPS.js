joo.classLoader.prepare("package",/*
{
	import flash.events.Event
	import flash.text.TextField
	import flash.text.TextFormat
	import flash.utils.getTimer*/
	
	"public class FPS extends flash.text.TextField",5,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.events.Event);},
	
		"private var",{ fs/*: int*/:0},
		"private var",{ ms/*: int*/:0},
		
		"public function FPS",function FPS$()
		{this.super$5();
			var format/*: TextFormat*/ = new flash.text.TextFormat();
			
			format.color = 0x666666;
			format.size = 10;
			format.bold = true;
			format.font = 'Verdana';
			
			this.textColor = 0xcecece;
			this.autoSize = "left";
			this.defaultTextFormat = format;
			
			this.ms$5 = flash.utils.getTimer();
			this.fs$5 = 0;
			
			this.addEventListener( flash.events.Event.ENTER_FRAME, $$bound(this,"onEnterFrame$5") );
		},
		
		"private function onEnterFrame",function onEnterFrame( event/*: Event*/ )/*: void*/
		{
			if( flash.utils.getTimer() - 1000 > this.ms$5 )
			{
				this.ms$5 = flash.utils.getTimer();
				this.text = this.fs$5.toString();
				this.fs$5 = 0;
			}
			else
			{
				++this.fs$5;
			}
		},
	];},[],["flash.text.TextField","flash.text.TextFormat","flash.events.Event"], "0.8.0", "0.8.1"
);