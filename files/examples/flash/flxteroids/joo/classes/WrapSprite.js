joo.classLoader.prepare("package",/*
{
	import org.flixel.**/

	//In asteroids, everything wraps around from one side of the screen to the other.
	//To simulate this effect, I build a simple wrapper for FlxSprite that handles that behavior.
	"public class WrapSprite extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		//This is the constructor - it is just like the FlxSprite constructor,
		// except we shrink the sprite's bounding box a little bit for more forgiving play.
		"public function WrapSprite",function WrapSprite$(X/*:int=0*/, Y/*:int=0*/, Graphic/*:Class=null*/)
		{if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){X=0;}Y=0;}Graphic=null;}
			this.super$5(X, Y, Graphic);
			this.alterBoundingBox();
		},
		
		//This function reduces the size of the bounding box
		"public function alterBoundingBox",function alterBoundingBox()/*:void*/
		{
			this.width = this.width*0.55;
			this.height = this.height*0.55;
			this.offset.x = (this.frameWidth - this.width) / 2;
			this.offset.y = (this.frameHeight - this.height) / 2;
		},
		
		//The game loop function
		"override public function update",function update()/*:void*/
		{
			//Always call super.update() unless you really know what you're doing!!
			this.update$5();
			
			//This is the actual wrap effect code.  All this is doing is checking to see
			// if the object went off the screen.  If it did, it moves it to the other side.
			//It looks kind of crazy but that's because we done shrank up the bounding boxes,
			// which requires a little extra math to make sure the graphics don't disappear
			// or re-appear too early.
			if(this.x < -this.frameWidth + this.offset.x)
				this.x = org.flixel.FlxG.width + this.offset.x;
			else if(this.x > org.flixel.FlxG.width + this.offset.x)
				this.x = -this.frameWidth + this.offset.x;
			if(this.y < -this.frameHeight + this.offset.y)
				this.y = org.flixel.FlxG.height + this.offset.y;
			else if(this.y > org.flixel.FlxG.height + this.offset.y)
				this.y = -this.frameHeight + this.offset.y;
		},
	];},[],["org.flixel.FlxSprite","org.flixel.FlxG"], "0.8.0", "0.8.1"
);