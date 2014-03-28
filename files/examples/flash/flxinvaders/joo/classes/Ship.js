joo.classLoader.prepare("package",/*
{
	import org.flixel.**/

	"public class Ship extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},		//Class declaration for the player's little ship
	
		"private var",{ bullets/*:Array*/:null},		//Refers to the bullets you can shoot at enemies
		"private var",{ bulletIndex/*:int*/:0},		//Keeps track of where in the list of bullets we are
		
		{Embed:{source:"ship.png"}}, "private var",{ ImgShip/*:Class*/:null},	//Graphic of the player's ship
		
		//Constructor for the player - just initializing a simple sprite using a graphic.
		"public function Ship",function Ship$(Bullets/*:Array*/)
		{
			//This initializes this sprite object with the graphic of the ship and
			// positions it in the middle of the screen.
			this.super$5(org.flixel.FlxG.width/2-6, org.flixel.FlxG.height-12, this.ImgShip$5);
			this.bullets$5 = Bullets;	//Save a reference to the bullets array
			this.bulletIndex$5 = 0;	//Initialize our list marker to the first entry
		},
		
		//Basic game loop function again!
		"override public function update",function update()/*:void*/
		{
			//Controls!
			this.velocity.x = 0;				//Default velocity to zero
			if(org.flixel.FlxG.keys.LEFT)
				this.velocity.x -= 150;		//If the player is pressing left, set velocity to left 150
			if(org.flixel.FlxG.keys.RIGHT)	
				this.velocity.x += 150;		//If the player is pressing right, then right 150
			
			//Just like in PlayState, this is easy to forget but very important!
			//Call this to automatically evaluate your velocity and position and stuff.
			this.update$5();
			
			//Here we are stopping the player from moving off the screen,
			// with a little border or margin of 4 pixels.
			if(this.x > org.flixel.FlxG.width-this.width-4)
				this.x = org.flixel.FlxG.width-this.width-4; //Checking and setting the right side boundary
			if(this.x < 4)
				this.x = 4;					//Checking and setting the left side boundary
			
			//Finally, we gotta shoot some bullets amirite?  First we check to see if the
			// space bar was just pressed (no autofire in space invaders you guys)
			if(org.flixel.FlxG.keys.justPressed("SPACE"))
			{
				//Space bar was pressed!  FIRE A BULLET
				var b/*:FlxSprite*/ = this.bullets$5[this.bulletIndex$5];	//Figure out which bullet to fire
				b.reset(this.x + this.width / 2 - b.width, this.y);
				b.velocity.y = -240;					//Set the vertical speed to shoot up fast
				this.bulletIndex$5++;							//Increment our bullet list tracker
				if(this.bulletIndex$5 >= this.bullets$5.length)		//And check to see if we went over
					this.bulletIndex$5 = 0;					//If we did just reset.
			}
		},
	];},[],["org.flixel.FlxSprite","resource:ship.png","org.flixel.FlxG"], "0.8.0", "0.8.1"
);