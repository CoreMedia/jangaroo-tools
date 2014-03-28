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
		{switch(arguments.length){case 0:Up=null;case 1:Down=null;case 2:Left=null;case 3:Right=null;case 4:AButton=null;case 5:BButton=null;case 6:XButton=null;case 7:YButton=null;case 8:StartButton=null;case 9:SelectButton=null;case 10:L1Button=null;case 11:L2Button=null;case 12:R1Button=null;case 13:R2Button=null;}
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
	];},[],["org.flixel.data.FlxInput","org.flixel.FlxG"], "0.8.0", "0.8.2-SNAPSHOT"
);