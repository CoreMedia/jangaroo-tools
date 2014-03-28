joo.classLoader.prepare("package",/*
{
	import org.flixel.**/ //Get access to all the wonders flixel has to offer

	"public class PlayState extends org.flixel.FlxState",7,function($$private){var is=joo.is,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(org.flixel.FlxG);},		//The class declaration for the main game state
	
		"protected var",{ _player/*:Ship*/:null},				//refers to the little player ship at the bottom
		"protected var",{ _playerBullets/*:FlxGroup*/:null},	//refers to the bullets you shoot
		"protected var",{ _aliens/*:FlxGroup*/:null},			//refers to all the squid monsters
		"protected var",{ _alienBullets/*:FlxGroup*/:null},	//refers to all the bullets the enemies shoot at you
		"protected var",{ _shields/*:FlxGroup*/:null},		//refers to the box shields along the bottom of the game
		
		//Some meta-groups for speeding up overlap checks later
		"protected var",{ _vsShields/*:FlxGroup*/:null},		//Meta-group to speed up the shield collisions later
		
		//This is where we create the main game state!
		//Inside this function we will create and orient all the important game objects.
		"override public function create",function create()/*:void*/
		{
			var i/*:int*/;
			
			//We're using the global scores array to store a basic, state-independent status string.
			//If there is no status string (the scores array is empty) then make a new welcome message.
			if(org.flixel.FlxG.scores.length <= 0)
				org.flixel.FlxG.scores[0] = "WELCOME TO FLX INVADERS";
			
			//First we will instantiate the bullets you fire at your enemies.
			var s/*:FlxSprite*/;
			this._playerBullets = new org.flixel.FlxGroup();//Initializing the array is very important and easy to forget!
			for(i = 0; i < 8; i++)			//Create 8 bullets for the player to recycle
			{
				s = new org.flixel.FlxSprite(-100,-100);	//Instantiate a new sprite offscreen
				s.createGraphic(2,8);			//Create a 2x8 white box
				s.exists = false;
				this._playerBullets.add(s);			//Add it to the group of player bullets
			}
			this.add(this._playerBullets);
			//NOTE: what we're doing here with bullets might seem kind of complicated but
			// it is a good thing to get into the practice of doing.  What we are doing
			// is creating a big pile of bullets that we can recycle, because there are only
			// ever like 10 bullets or something on screen at once anyways.
			
			//Now that we have a list of bullets, we can initialize the player (and give them the bullets)
			this._player = new Ship(this._playerBullets.members);
			this.add(this._player);	//Adds the player to the state
			
			//Then we kind of do the same thing for the enemy invaders; first we make their bullets...
			this._alienBullets = new org.flixel.FlxGroup();
			for(i = 0; i < 64; i++)
			{
				s = new org.flixel.FlxSprite(-100,-100);
				s.createGraphic(2,8);
				s.exists = false;
				this._alienBullets.add(s);
			}
			this.add(this._alienBullets);
			
			//...then we go through and make the invaders.  This looks all mathy but it's not that bad!
			//We're basically making 5 rows of 10 invaders, and each row is a different color.
			var a/*:Alien*/;
			this._aliens = new org.flixel.FlxGroup();
			var colors/*:Array*/ = new Array(0xff0000ff, 0xff00ffff, 0xff00ff00, 0xffffff00, 0xffff0000);
			for(i = 0; i < 50; i++)
			{
				a = new Alien(	8 + (i % 10) * 32,		//The X position of the alien
								24 + $$int(i / 10) * 32,	//The Y position of the alien
								colors[$$int(i / 10)], this._alienBullets.members);
				this._aliens.add(a);
			}
			this.add(this._aliens);

			//Finally, we're going to make the little box shields at the bottom of the screen.
			//Each shield is made up of a bunch of little white 2x2 pixel blocks.
			//That way they look like they're getting chipped apart as they get shot.
			//This also looks kind of crazy and mathy (it sort of is), but we're just
			// telling the game where to put all the individual bits that make up each box.
			this._shields = new org.flixel.FlxGroup();
			for(i = 0; i < 256; i++)
			{
				s = new org.flixel.FlxSprite(	32 + 80 * $$int(i / 64) + (i % 8) * 2,		//The X position of this bit
									org.flixel.FlxG.height - 32 + ($$int((i % 64) / 8) * 2));//The Y position of this bit
				s.moves = false;
				s.createGraphic(2,2);
				this._shields.add(s);
			}
			this.add(this._shields);
			
			//Store these things in meta-groups so they're easier to compare/overlap later
			this._vsShields = new org.flixel.FlxGroup();
			this._vsShields.add(this._alienBullets);
			this._vsShields.add(this._playerBullets);
			
			//Then we're going to add a text field to display the label we're storing in the scores array.
			var t/*:FlxText*/ = new org.flixel.FlxText(4,4,org.flixel.FlxG.width-8,org.flixel.FlxG.scores[0]);
			t.alignment = "center";
			this.add(t);
			
			//Finally we display the cursor to encourage people to click the game,
			// which will give Flash the browser focus and let the keyboard work.
			org.flixel.FlxG.mouse.show();
		},
		
		//This is the main game loop function, where all the logic is done.
		"override public function update",function update()/*:void*/
		{
			//This just says if the user clicked on the game to hide the cursor
			if(org.flixel.FlxG.mouse.justPressed())
				org.flixel.FlxG.mouse.hide();
			
			//This is how we do basic sprite collisions in flixel!
			//We compare one array of objects against another, and then if any of them overlap
			// flixel calls their 'kill' method, which by default sets the object to not exist (!exists)
			org.flixel.FlxU.overlap(this._shields,this._vsShields,$$bound(this,"overlapped"));
			org.flixel.FlxU.overlap(this._playerBullets,this._aliens);
			org.flixel.FlxU.overlap(this._alienBullets,this._player);
			
			//THIS IS SUPER IMPORTANT and also easy to forget.  But all those objects that we added
			// to the state earlier (i.e. all of everything) will not get automatically updated
			// if you forget to call this function.  This is basically saying "state, call update
			// right now on all of the objects that were added."
			this.update$7();
			
			//Now that everything has been updated, we are going to check and see if there
			// is a game over yet.  There are two ways to get a game over - player dies,
			// OR player kills all aliens.  First we check to see if the player is dead:
			if(!this._player.exists)
			{
				org.flixel.FlxG.scores[0] = "YOU LOST";	//Player died, so set our label to YOU LOST
				org.flixel.FlxG.state = new PlayState();	//Then reload the playstate
				return;							//Anytime you call switchstate it is good to just return
			}
			else if(this._aliens.getFirstExtant() == null)
			{
				org.flixel.FlxG.scores[0] = "YOU WON";		//No aliens left; you win!
				org.flixel.FlxG.state = new PlayState();	//Same dealy as above
				return;
			}
		},
		
		//We want aliens to mow down shields when they touch them, not die
		"protected function overlapped",function overlapped(Object1/*:FlxObject*/,Object2/*:FlxObject*/)/*:void*/
		{
			Object1.kill();
			if(!(is(Object2,  Alien)))
				Object2.kill();
		},
	];},[],["org.flixel.FlxState","org.flixel.FlxG","org.flixel.FlxGroup","org.flixel.FlxSprite","Ship","Array","Alien","int","org.flixel.FlxText","org.flixel.FlxU"], "0.8.0", "0.8.1"
);