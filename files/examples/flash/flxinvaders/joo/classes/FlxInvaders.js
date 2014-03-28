joo.classLoader.prepare("package",/* {
	
	import org.flixel.**/ //Allows you to refer to flixel objects in your code
	{SWF:{width:"640", height:"480", backgroundColor:"#000000"}}, //Set the size and color of the Flash file
	{Frame:{factoryClass:"Preloader"}},  //Tells flixel to use the default preloader

	"public class FlxInvaders extends org.flixel.FlxGame",7,function($$private){;return[function(){joo.classLoader.init(PlayState);},
	
		"public function FlxInvaders",function FlxInvaders$()/*:void*/
		{
			this.super$7(320,240,PlayState); //Create a new FlxGame object at 320x240 with 2x pixels, then load PlayState
			//showLogo = false;
		},
	];},[],["org.flixel.FlxGame","PlayState"], "0.8.0", "0.8.1"
);