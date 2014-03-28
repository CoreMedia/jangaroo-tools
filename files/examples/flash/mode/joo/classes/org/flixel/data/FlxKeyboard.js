joo.classLoader.prepare("package org.flixel.data",/*
{*/
	"public class FlxKeyboard extends org.flixel.data.FlxInput",2,function($$private){;return[
	
		"public var",{ ESCAPE/*:Boolean*/:false},
		"public var",{ F1/*:Boolean*/:false},
		"public var",{ F2/*:Boolean*/:false},
		"public var",{ F3/*:Boolean*/:false},
		"public var",{ F4/*:Boolean*/:false},
		"public var",{ F5/*:Boolean*/:false},
		"public var",{ F6/*:Boolean*/:false},
		"public var",{ F7/*:Boolean*/:false},
		"public var",{ F8/*:Boolean*/:false},
		"public var",{ F9/*:Boolean*/:false},
		"public var",{ F10/*:Boolean*/:false},
		"public var",{ F11/*:Boolean*/:false},
		"public var",{ F12/*:Boolean*/:false},
		"public var",{ ONE/*:Boolean*/:false},
		"public var",{ TWO/*:Boolean*/:false},
		"public var",{ THREE/*:Boolean*/:false},
		"public var",{ FOUR/*:Boolean*/:false},
		"public var",{ FIVE/*:Boolean*/:false},
		"public var",{ SIX/*:Boolean*/:false},
		"public var",{ SEVEN/*:Boolean*/:false},
		"public var",{ EIGHT/*:Boolean*/:false},
		"public var",{ NINE/*:Boolean*/:false},
		"public var",{ ZERO/*:Boolean*/:false},
		"public var",{ NUMPADONE/*:Boolean*/:false},
		"public var",{ NUMPADTWO/*:Boolean*/:false},
		"public var",{ NUMPADTHREE/*:Boolean*/:false},
		"public var",{ NUMPADFOUR/*:Boolean*/:false},
		"public var",{ NUMPADFIVE/*:Boolean*/:false},
		"public var",{ NUMPADSIX/*:Boolean*/:false},
		"public var",{ NUMPADSEVEN/*:Boolean*/:false},
		"public var",{ NUMPADEIGHT/*:Boolean*/:false},
		"public var",{ NUMPADNINE/*:Boolean*/:false},
		"public var",{ NUMPADZERO/*:Boolean*/:false},
		"public var",{ MINUS/*:Boolean*/:false},
		"public var",{ NUMPADMINUS/*:Boolean*/:false},
		"public var",{ PLUS/*:Boolean*/:false},
		"public var",{ NUMPADPLUS/*:Boolean*/:false},
		"public var",{ DELETE/*:Boolean*/:false},
		"public var",{ BACKSPACE/*:Boolean*/:false},
		"public var",{ Q/*:Boolean*/:false},
		"public var",{ W/*:Boolean*/:false},
		"public var",{ E/*:Boolean*/:false},
		"public var",{ R/*:Boolean*/:false},
		"public var",{ T/*:Boolean*/:false},
		"public var",{ Y/*:Boolean*/:false},
		"public var",{ U/*:Boolean*/:false},
		"public var",{ I/*:Boolean*/:false},
		"public var",{ O/*:Boolean*/:false},
		"public var",{ P/*:Boolean*/:false},
		"public var",{ LBRACKET/*:Boolean*/:false},
		"public var",{ RBRACKET/*:Boolean*/:false},
		"public var",{ BACKSLASH/*:Boolean*/:false},
		"public var",{ CAPSLOCK/*:Boolean*/:false},
		"public var",{ A/*:Boolean*/:false},
		"public var",{ S/*:Boolean*/:false},
		"public var",{ D/*:Boolean*/:false},
		"public var",{ F/*:Boolean*/:false},
		"public var",{ G/*:Boolean*/:false},
		"public var",{ H/*:Boolean*/:false},
		"public var",{ J/*:Boolean*/:false},
		"public var",{ K/*:Boolean*/:false},
		"public var",{ L/*:Boolean*/:false},
		"public var",{ SEMICOLON/*:Boolean*/:false},
		"public var",{ QUOTE/*:Boolean*/:false},
		"public var",{ ENTER/*:Boolean*/:false},
		"public var",{ SHIFT/*:Boolean*/:false},
		"public var",{ Z/*:Boolean*/:false},
		"public var",{ X/*:Boolean*/:false},
		"public var",{ C/*:Boolean*/:false},
		"public var",{ V/*:Boolean*/:false},
		"public var",{ B/*:Boolean*/:false},
		"public var",{ N/*:Boolean*/:false},
		"public var",{ M/*:Boolean*/:false},
		"public var",{ COMMA/*:Boolean*/:false},
		"public var",{ PERIOD/*:Boolean*/:false},
		"public var",{ NUMPADPERIOD/*:Boolean*/:false},
		"public var",{ SLASH/*:Boolean*/:false},
		"public var",{ NUMPADSLASH/*:Boolean*/:false},
		"public var",{ CONTROL/*:Boolean*/:false},
		"public var",{ ALT/*:Boolean*/:false},
		"public var",{ SPACE/*:Boolean*/:false},
		"public var",{ UP/*:Boolean*/:false},
		"public var",{ DOWN/*:Boolean*/:false},
		"public var",{ LEFT/*:Boolean*/:false},
		"public var",{ RIGHT/*:Boolean*/:false},


		"public function FlxKeyboard",function FlxKeyboard$()
		{this.super$2();
			var i/*:uint*/;
			
			//LETTERS
			i = 65;
			while(i <= 90)
				this.addKey(String.fromCharCode(i),i++);
			
			//NUMBERS
			i = 48;
			this.addKey("ZERO",i++);
			this.addKey("ONE",i++);
			this.addKey("TWO",i++);
			this.addKey("THREE",i++);
			this.addKey("FOUR",i++);
			this.addKey("FIVE",i++);
			this.addKey("SIX",i++);
			this.addKey("SEVEN",i++);
			this.addKey("EIGHT",i++);
			this.addKey("NINE",i++);
			i = 96;
			this.addKey("NUMPADZERO",i++);
			this.addKey("NUMPADONE",i++);
			this.addKey("NUMPADTWO",i++);
			this.addKey("NUMPADTHREE",i++);
			this.addKey("NUMPADFOUR",i++);
			this.addKey("NUMPADFIVE",i++);
			this.addKey("NUMPADSIX",i++);
			this.addKey("NUMPADSEVEN",i++);
			this.addKey("NUMPADEIGHT",i++);
			this.addKey("NUMPADNINE",i++);
			
			//FUNCTION KEYS
			i = 1;
			while(i <= 12)
				this.addKey("F"+i,111+(i++));
			
			//SPECIAL KEYS + PUNCTUATION
			this.addKey("ESCAPE",27);
			this.addKey("MINUS",189);
			this.addKey("NUMPADMINUS",109);
			this.addKey("PLUS",187);
			this.addKey("NUMPADPLUS",107);
			this.addKey("DELETE",46);
			this.addKey("BACKSPACE",8);
			this.addKey("LBRACKET",219);
			this.addKey("RBRACKET",221);
			this.addKey("BACKSLASH",220);
			this.addKey("CAPSLOCK",20);
			this.addKey("SEMICOLON",186);
			this.addKey("QUOTE",222);
			this.addKey("ENTER",13);
			this.addKey("SHIFT",16);
			this.addKey("COMMA",188);
			this.addKey("PERIOD",190);
			this.addKey("NUMPADPERIOD",110);
			this.addKey("SLASH",191);
			this.addKey("NUMPADSLASH",191);
			this.addKey("CONTROL",17);
			this.addKey("ALT",18);
			this.addKey("SPACE",32);
			this.addKey("UP",38);
			this.addKey("DOWN",40);
			this.addKey("LEFT",37);
			this.addKey("RIGHT",39);
		},
	];},[],["org.flixel.data.FlxInput","String"], "0.8.0", "0.8.2-SNAPSHOT"
);