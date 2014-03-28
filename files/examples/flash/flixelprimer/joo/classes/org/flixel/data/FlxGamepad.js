joo.classLoader.prepare("package org.flixel.data",/*
{
	import org.flixel.FlxG*/
	
	"public class FlxGamepad extends org.flixel.data.FlxInput",2,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		"public var",{ UP/*:Boolean*/:false},
		"public var",{ DOWN/*:Boolean*/:false},
		"public var",{ LEFT/*:Boolean*/:false},
		"public var",{ RIGHT/*:Boolean*/:false},
		"public var",{ A/*:Boolean*/:false},
		"public var",{ B/*:Boolean*/:false},
		"public var",{ X/*:Boolean*/:false},
		"public var",{ Y/*:Boolean*/:false},
		"public var",{ START/*:Boolean*/:false},
		"public var",{ SELECT/*:Boolean*/:false},
		"public var",{ L1/*:Boolean*/:false},
		"public var",{ L2/*:Boolean*/:false},
		"public var",{ R1/*:Boolean*/:false},
		"public var",{ R2/*:Boolean*/:false},
		
		"public function FlxGamepad",function FlxGamepad$()
		{
			this.super$2();
		},
		
		/**
		 * Assign a keyboard key to a gamepad button.  For example, if you pass "X" as the <code>AButton</code>
		 * parameter, this gamepad's member variable <code>A</code> will be set to true whenever the 'x' key
		 * on the keyboard is pressed.  Pretty simple!  Nice for multiplayer games and utilities that
		 * can convert gamepad pressed to keyboard keys at the operating system level.
		 */
		"public function bind",function bind(Up/*:String=null*/, Down/*:String=null*/, Left/*:String=null*/, Right/*:String=null*/,
							 AButton/*:String=null*/, BButton/*:String=null*/, XButton/*:String=null*/, YButton/*:String=null*/,
							 StartButton/*:String=null*/, SelectButton/*:String=null*/,
							 L1Button/*:String=null*/, L2Button/*:String=null*/, R1Button/*:String=null*/, R2Button/*:String=null*/)/*:void*/
		{if(arguments.length<14){if(arguments.length<13){if(arguments.length<12){if(arguments.length<11){if(arguments.length<10){if(arguments.length<9){if(arguments.length<8){if(arguments.length<7){if(arguments.length<6){if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){Up=null;}Down=null;}Left=null;}Right=null;}AButton=null;}BButton=null;}XButton=null;}YButton=null;}StartButton=null;}SelectButton=null;}L1Button=null;}L2Button=null;}R1Button=null;}R2Button=null;}
			if(Up != null)			this.addKey("UP",org.flixel.FlxG.keys._lookup[Up]);
			if(Down != null)		this.addKey("DOWN",org.flixel.FlxG.keys._lookup[Down]);
			if(Left != null)		this.addKey("LEFT",org.flixel.FlxG.keys._lookup[Left]);
			if(Right != null)		this.addKey("RIGHT",org.flixel.FlxG.keys._lookup[Right]);
			if(AButton != null)		this.addKey("A",org.flixel.FlxG.keys._lookup[AButton]);
			if(BButton != null)		this.addKey("B",org.flixel.FlxG.keys._lookup[BButton]);
			if(XButton != null)		this.addKey("X",org.flixel.FlxG.keys._lookup[XButton]);
			if(YButton != null)		this.addKey("Y",org.flixel.FlxG.keys._lookup[YButton]);
			if(StartButton != null)	this.addKey("START",org.flixel.FlxG.keys._lookup[StartButton]);
			if(SelectButton != null)this.addKey("SELECT",org.flixel.FlxG.keys._lookup[SelectButton]);
			if(L1Button != null)	this.addKey("L1",org.flixel.FlxG.keys._lookup[L1Button]);
			if(L2Button != null)	this.addKey("L2",org.flixel.FlxG.keys._lookup[L2Button]);
			if(R1Button != null)	this.addKey("R1",org.flixel.FlxG.keys._lookup[R1Button]);
			if(R2Button != null)	this.addKey("R2",org.flixel.FlxG.keys._lookup[R2Button]);
		},
	];},[],["org.flixel.data.FlxInput","org.flixel.FlxG"], "0.8.0", "0.8.3"
);