joo.classLoader.prepare("package com.chipacabra.Jumper",/* 
{
	import org.flixel.FlxG
	import org.flixel.FlxSprite
	import org.flixel.FlxState
	import org.flixel.FlxText
	import org.flixel.FlxU*/
	
	/**
	 * ...
	 * @author David Bell
	 */
	"public class MenuState extends org.flixel.FlxState",7,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);}, 
	
		{Embed:{source : 'com/chipacabra/Jumper/../../../../art/pointer.png'}},"public var",{ imgPoint/*:Class*/:null},
		
		{Embed:{source : 'com/chipacabra/Jumper/../../../../sounds/coin.mp3'}},"public var",{ sndClick/*:Class*/:null},
		{Embed:{source : 'com/chipacabra/Jumper/../../../../sounds/menu.mp3'}},"public var",{ sndPoint/*:Class*/:null},

		"static public const",{ OPTIONS/*:int*/:3}, //How many menu options there are.
		"static public const",{ TEXT_SPEED/*:Number*/:200},
		
		"private var",{ _text1/*:FlxText*/:null},
		"private var",{ _text2/*:FlxText*/:null},
		"private var",{ _text3/*:FlxText*/:null},
		"private var",{ _text4/*:FlxText*/:null},
		"private var",{ _text5/*:FlxText*/:null},  // augh so many text objects. I should make arrays.
		"private var",{ _pointer/*:FlxSprite*/:null},
		"private var",{ _option/*:int*/:0},     // This will indicate what the pointer is pointing at
		
		"override public function create",function create()/*:void*/ 
		{
			//Each word is its own object so we can position them independantly
			//_text1 = new FlxText(FlxG.width/5, FlxG.height / 4, 320, "Project");
			this._text1$7 = new org.flixel.FlxText(-220, org.flixel.FlxG.height / 4, 320, "Project");
			this._text1$7.size = 40;
			this._text1$7.color = 0xFFFF00;
			this._text1$7.antialiasing = true;
			this.add(this._text1$7);
			
			//Base everything off of text1, so if we change color or size, only have to change one
			//_text2 = new FlxText(FlxG.width / 2.5, FlxG.height / 2.5, 320, "Jumper");
			this._text2$7 = new org.flixel.FlxText(org.flixel.FlxG.width-50, org.flixel.FlxG.height / 2.5, 320, "Jumper");
			this._text2$7.size = this._text1$7.size;
			this._text2$7.color = this._text1$7.color;
			this._text2$7.antialiasing = this._text1$7.antialiasing;
			this.add(this._text2$7);
			
			//Set up the menu options
			this._text3$7 = new org.flixel.FlxText(org.flixel.FlxG.width * 2 / 3, org.flixel.FlxG.height * 2 / 3, 150, "Play");
			this._text4$7 = new org.flixel.FlxText(org.flixel.FlxG.width * 2 / 3, org.flixel.FlxG.height * 2 / 3+30, 150, "Visit NIWID");
			this._text5$7 = new org.flixel.FlxText(org.flixel.FlxG.width * 2 / 3, org.flixel.FlxG.height * 2 / 3 + 60, 150, "Visit flixel.org");
			this._text3$7.color = this._text4$7.color = this._text5$7.color =0xAAFFFF00; 
			this._text3$7.size = this._text4$7.size = this._text5$7.size =16;
			this._text3$7.antialiasing = this._text4$7.antialiasing=this._text5$7.antialiasing=true;
			this.add(this._text3$7);
			this.add(this._text4$7);
			this.add(this._text5$7);
			
			this._pointer$7 = new org.flixel.FlxSprite();
			this._pointer$7.loadGraphic(this.imgPoint);
			this._pointer$7.x = this._text3$7.x - this._pointer$7.width - 10;
			this.add(this._pointer$7);
			this._option$7 = 0;
			this.create$7();
		},
		
		"override public function update",function update()/*:void*/ 
		{
			if (this._text1$7.x < org.flixel.FlxG.width / 5)	this._text1$7.velocity.x = com.chipacabra.Jumper.MenuState.TEXT_SPEED;
			else this._text1$7.velocity.x = 0;
			if (this._text2$7.x > org.flixel.FlxG.width / 2.5) this._text2$7.velocity.x = -com.chipacabra.Jumper.MenuState.TEXT_SPEED;
			else this._text2$7.velocity.x = 0;
			
			switch(this._option$7)    // this is the goofus way to do it. An array would be way better
			{
				case 0:
				this._pointer$7.y = this._text3$7.y;
				break;
				case 1:
				this._pointer$7.y = this._text4$7.y;
				break;
				case 2:
				this._pointer$7.y = this._text5$7.y;
				break;
			}
			if (org.flixel.FlxG.keys.justPressed("UP"))
				{
					this._option$7 = (this._option$7 +com.chipacabra.Jumper.MenuState.OPTIONS- 1) % com.chipacabra.Jumper.MenuState.OPTIONS;  // A goofy format, because % doesn't work on negative numbers
					org.flixel.FlxG.play(this.sndPoint, 1, false, 50);
				}
			if (org.flixel.FlxG.keys.justPressed("DOWN"))
				{
					this._option$7 = (this._option$7 +com.chipacabra.Jumper.MenuState.OPTIONS + 1) % com.chipacabra.Jumper.MenuState.OPTIONS;
					org.flixel.FlxG.play(this.sndPoint, 1, false, 50);
				}
			if (org.flixel.FlxG.keys.justPressed("SPACE") || org.flixel.FlxG.keys.justPressed("ENTER"))
				switch (this._option$7) 
				{
					case 0:
					org.flixel.FlxG.fade.start(0xFF969867, 1, $$bound(this,"startGame$7"));
					org.flixel.FlxG.play(this.sndClick, 1, false, 50);
					break;
					case 1:
					this.onURL$7();
					break;
					case 2:
					this.onFlixel$7();
					break;
				}
			
			this.update$7();
		},
		
		"private function onFlixel",function onFlixel()/*:void*/ 
		{
			org.flixel.FlxU.openURL("http://flixel.org");
		},
		
		
		"private function onURL",function onURL()/*:void*/ 
		{
			org.flixel.FlxU.openURL("http://chipacabra.blogspot.com");
		},
		
		"private function startGame",function startGame()/*:void*/
		{
			org.flixel.FlxG.state = new com.chipacabra.Jumper.PlayState;
		},
	];},[],["org.flixel.FlxState","resource:com/chipacabra/Jumper/../../../../art/pointer.png","resource:com/chipacabra/Jumper/../../../../sounds/coin.mp3","resource:com/chipacabra/Jumper/../../../../sounds/menu.mp3","org.flixel.FlxText","org.flixel.FlxG","org.flixel.FlxSprite","org.flixel.FlxU","com.chipacabra.Jumper.PlayState"], "0.8.0", "0.8.2-SNAPSHOT"
);