joo.classLoader.prepare("package",/*
{
	import org.flixel.**/

	//This is our main game state, where we initialize all our game objects and do
	// things like check for game over, perform collisions, etc.
	"public class PlayState extends org.flixel.FlxState",7,function($$private){;return[function(){joo.classLoader.init(Asteroid,org.flixel.FlxG);},
	
		"protected var",{ _ship/*:Ship*/:null},			//Refers to the little player ship
		"protected var",{ _bullets/*:FlxGroup*/:null},	//A list of the bullets you shoot at the asteroids
		"protected var",{ _asteroids/*:FlxGroup*/:null},	//A list of all the asteroids 
		"protected var",{ _timer/*:Number*/:NaN},		//A timer to decide when to make a new asteroid
		
		"override public function create",function create()/*:void*/
		{
			org.flixel.FlxG.mouse.hide();
			var i/*:int*/;
			
			//Initialize the list of asteroids
			this._asteroids = new org.flixel.FlxGroup();
			this.add(this._asteroids);
			Asteroid.group = this._asteroids;
			this.addAsteroid$7();
			
			//Then instantiate the bullets you fire at your enemies.
			var s/*:FlxSprite*/;
			this._bullets = new org.flixel.FlxGroup();	//Initializing the array is very important and easy to forget!
			for(i = 0; i < 32; i++)		//Create 32 bullets for the player to recycle
			{
				//Instantiate a new 2x8 generic sprite offscreen
				s = new WrapSprite(-100, -100);
				s.createGraphic(8, 2);
				s.width = 10;		//We're going to exaggerate the bullet's bounding box a little
				s.height = 10;
				s.offset.x = -1;
				s.offset.y = -4;
				s.exists = false;
				this._bullets.add(s);	//Add it to the array of player bullets
			}
			this.add(this._bullets);
			
			//Initialize the ship and add it to the layer
			this._ship = new Ship(this._bullets.members);
			this.add(this._ship);
		},
		
		//The main game loop function
		"override public function update",function update()/*:void*/
		{
			//Count down the new asteroid timer
			this._timer -= org.flixel.FlxG.elapsed;
			if(this._timer <= 0)
				this.addAsteroid$7();
			
			//Perform collisions between the different objects.  Overlap will
			// check to see if any of those sprites touch, and if they do, it will
			// call kill() on both of them, which sets 'exists' to false, and 'dead' to true.
			//You can optionally pass it a callback function if you want more detailed reactions.
			org.flixel.FlxU.overlap(this._bullets, this._asteroids);		//Check to see if any bullets overlap any asteroids
			org.flixel.FlxU.overlap(this._asteroids, this._ship);			//Check to see if any asteroids overlap the ship
			org.flixel.FlxU.collide(this._asteroids, this._asteroids);	//Check for asteroid collisions
				
			//Pretty much always call the main game loop's parent update.
			//This goes and calls update on all the objects you added to this state
			this.update$7();
			
			//If the player died, reset the game
			if(!this._ship.exists)
				org.flixel.FlxG.state = new PlayState();
		},
		
		//This function resets the timer and adds a new asteroid to the game
		"private function addAsteroid",function addAsteroid()/*:void*/
		{
			//Create a new asteroid and add it to the group
			this._asteroids.add(new Asteroid().create());
			this._timer = org.flixel.FlxU.random()*4;	//Reset the timer
		},
	];},[],["org.flixel.FlxState","org.flixel.FlxG","org.flixel.FlxGroup","Asteroid","WrapSprite","Ship","org.flixel.FlxU"], "0.8.0", "0.8.1"
);