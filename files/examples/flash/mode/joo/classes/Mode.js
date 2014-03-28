joo.classLoader.prepare("package",/* {
	import org.flixel.*
	import com.adamatomic.Mode.MenuState*/
	
	{SWF:{width:"640", height:"480", backgroundColor:"#000000"}},
	{Frame:{factoryClass:"Preloader"}},

	"public class Mode extends org.flixel.FlxGame",7,function($$private){;return[function(){joo.classLoader.init(com.adamatomic.Mode.MenuState,org.flixel.FlxState);},
	
		"public function Mode",function Mode$()/*:void*/
		{
			this.super$7(320,240,com.adamatomic.Mode.MenuState);
			org.flixel.FlxState.bgColor = 0xff131c1b;
			this.useDefaultHotKeys = true;
		},
	];},[],["org.flixel.FlxGame","com.adamatomic.Mode.MenuState","org.flixel.FlxState"], "0.8.0", "0.8.2-SNAPSHOT"
);