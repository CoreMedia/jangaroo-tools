joo.classLoader.prepare("package",/*
{
	import org.flixel.* //Allows you to refer to flixel objects in your code
	import com.chipacabra.Jumper.**/
	{SWF:{width : "640", height : "480", backgroundColor : "#000000"}}, //Set the size and color of the Flash file
	{Frame:{factoryClass:"Preloader"}}, //Activate the preloader
  
	"public class Jumper extends org.flixel.FlxGame",7,function($$private){;return[function(){joo.classLoader.init(com.chipacabra.Jumper.MenuState,org.flixel.FlxState);},
	
		"public function Jumper",function Jumper$()/*:void*/
		{
			this.super$7(640, 480, com.chipacabra.Jumper.MenuState, 1); //Create a new FlxGame object at 640x480 with 1x pixels, then load MenuState
			org.flixel.FlxState.bgColor = 0xFF101414;
		},
	];},[],["org.flixel.FlxGame","com.chipacabra.Jumper.MenuState","org.flixel.FlxState"], "0.8.0", "0.8.2-SNAPSHOT"
);