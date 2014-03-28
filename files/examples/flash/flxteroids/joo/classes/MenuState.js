joo.classLoader.prepare("package",/*
{
	import org.flixel.**/

	//This is a simple, auto-generated menu (See the flx.py Tutorial on the wiki for more info)
	"public class MenuState extends org.flixel.FlxState",7,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		"override public function create",function create()/*:void*/
		{
			//A couple of simple text fields
			var t/*:FlxText*/;
			t = new org.flixel.FlxText(0,org.flixel.FlxG.height/2-10,org.flixel.FlxG.width,"FlxTeroids");
			t.size = 16;
			t.alignment = "center";
			this.add(t);
			t = new org.flixel.FlxText(0,org.flixel.FlxG.height-20,org.flixel.FlxG.width,"click to play");
			t.alignment = "center";
			this.add(t);
			
			org.flixel.FlxG.mouse.show();
		},

		"override public function update",function update()/*:void*/
		{
			//Switch to play state if the mouse is pressed
			if(org.flixel.FlxG.mouse.justPressed())
				org.flixel.FlxG.state = new PlayState();
		},
	];},[],["org.flixel.FlxState","org.flixel.FlxText","org.flixel.FlxG","PlayState"], "0.8.0", "0.8.1"
);