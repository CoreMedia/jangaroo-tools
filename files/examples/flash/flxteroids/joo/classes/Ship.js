joo.classLoader.prepare("package",/*
{
	import org.flixel.**/

	//This is the class declaration for the little player ship that you fly around in
	"public class Ship extends WrapSprite",6,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		"private var",{ bullets/*:Array*/:null},		//Refers to the bullets you can shoot at enemies
		"private var",{ bulletIndex/*:int*/:0},		//Keeps track of where in the list of bullets we are
		
		{Embed:{source:"ship.png"}}, "private var",{ ImgShip/*:Class*/:null},	//Graphic of the player's ship
		
		//This function creates the ship, taking the list of bullets as a parameter
		"public function Ship",function Ship$(Bullets/*:Array*/)
		{
			this.super$6(org.flixel.FlxG.width/2-8, org.flixel.FlxG.height/2-8, this.ImgShip$6);
			this.bullets$6 = Bullets;	//Save a reference to the bullets array
			this.bulletIndex$6 = 0;	//Initialize our list marker to the first entry
			
			this.angle = -90;		//Start the ship pointed upward
			this.maxThrust = 90;		//Cap the thrust at 90p/s2
			this.antialiasing = true;//Smoother rotations
		},
		
		//The main game loop function
		"override public function update",function update()/*:void*/
		{
			//This is where we handle turning the ship left and right
			this.angularVelocity = 0;
			if(org.flixel.FlxG.keys.LEFT)
				this.angularVelocity -= 240;
			if(org.flixel.FlxG.keys.RIGHT)
				this.angularVelocity += 240;
			
			//This is where thrust is handled
			this.thrust = 0;
			if(org.flixel.FlxG.keys.UP)
				this.thrust -= this.maxThrust*3;
			
			//The all important parent game loop update - this calls WrapSprite.update()
			this.update$6();
			
			if(org.flixel.FlxG.keys.justPressed("SPACE"))
			{
				//Space bar was pressed!  FIRE A BULLET
				var b/*:FlxSprite*/ = this.bullets$6[this.bulletIndex$6];	//Figure out which bullet to fire
				b.reset(this.x + (this.width - b.width) / 2, this.y + (this.height - b.height) / 2);
				b.angle = this.angle;
				b.velocity = org.flixel.FlxU.rotatePoint(150,0,0,0,b.angle);
				b.velocity.x += this.velocity.x;
				b.velocity.y += this.velocity.y;
				this.bulletIndex$6++;							//Increment our bullet list tracker
				if(this.bulletIndex$6 >= this.bullets$6.length)		//And check to see if we went over
					this.bulletIndex$6 = 0;					//If we did just reset.
			}
		},
	];},[],["WrapSprite","resource:ship.png","org.flixel.FlxG","org.flixel.FlxU"], "0.8.0", "0.8.1"
);