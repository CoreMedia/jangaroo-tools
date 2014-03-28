joo.classLoader.prepare("package",/*
{
	import flash.display.Sprite*/
	
	{SWF:{ backgroundColor:'0x212121', frameRate:'30', width:'384', height:'384'}},

	"public class Main extends flash.display.Sprite",6,function($$private){;return[
	
		"private var",{ app/*: ParticleApplication*/:null},
		
		"public function Main",function Main$()
		{this.super$6();
			this.stage.frameRate = 44.444;
			
			this.app$6 = new ParticleApplication();
			this.addChild( this.app$6 );
		},
	];},[],["flash.display.Sprite","ParticleApplication"], "0.8.0", "0.8.1"
);