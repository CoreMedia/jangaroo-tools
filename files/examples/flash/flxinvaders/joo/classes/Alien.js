joo.classLoader.prepare("package",/*
{
	import org.flixel.**/

	"public class Alien extends org.flixel.FlxSprite",5,function($$private){;return[function(){joo.classLoader.init(org.flixel.FlxG);},		//Class declaration for the squid monster class
	
		"private var",{ bullets/*:Array*/:null},			//Reference to the bullets the enemies shoot at you
		"static private var",{ bulletIndex/*:uint*/:0},	//Tracker or marker for the bullet list
		"private var",{ shotClock/*:Number*/:NaN},			//A simple timer for deciding when to shoot
		"private var",{ originalX/*:int*/:0},				//Saves the starting horizontal position (for movement logic)
		
		{Embed:{source:"alien.png"}}, "private var",{ ImgAlien/*:Class*/:null},	//The graphic of the squid monster
		
		//This is the constructor for the squid monster.
		//We are going to set up the basic values and then create a simple animation.
		"public function Alien",function Alien$(X/*:int*/,Y/*:int*/,Color/*:uint*/,Bullets/*:Array*/)
		{
			this.super$5(X,Y);					//Initialize sprite object
			this.loadGraphic(this.ImgAlien$5,true);	//Load this animated graphic file
			
			//Saving off some of the values we passed in
			this.originalX$5 = X;
			this.color = Color;		//setting the color tints the plain white alien graphic
			this.bullets$5 = Bullets;
			$$private.bulletIndex = 0;
			this.restart$5();			//Resets the timer for the bullet shooting logic
			
			//Time to create a simple animation!  alien.png has 3 frames of animation in it.
			//We want to play them in the order 1, 2, 3, 1 (but of course this stuff is 0-index).
			//To avoid a weird, annoying appearance the framerate is randomized a little bit
			// to a value between 6 and 10 (6+4) frames per second.
			this.addAnimation("Default",[0,1,0,2],6+org.flixel.FlxU.random()*4);
			
			//Now that the animation is set up, it's very easy to play it back!
			this.play("Default");
			
			//Everybody move to the right!
			this.velocity.x = 10;
		},
		
		//Basic game loop is BACK y'all
		"override public function update",function update()/*:void*/
		{
			//If alien has moved too far to the left, reverse direction and increase speed!
			if(this.x < this.originalX$5 - 8)
			{
				this.x = this.originalX$5 - 8;
				this.velocity.x = 10;
				this.velocity.y++;
			}
			if(this.x > this.originalX$5 + 8) //If alien has moved too far to the right, reverse direction
			{
				this.x = this.originalX$5 + 8;
				this.velocity.x = -10;
			}
			
			//Then do some bullet shooting logic
			if(this.y > org.flixel.FlxG.height * 0.35)
				this.shotClock$5 -= org.flixel.FlxG.elapsed; //Only count down if on the bottom half of the screen
			if(this.shotClock$5 <= 0)
			{
				//We counted down to zero, so it's time to shoot a bullet!
				this.restart$5();									//First, reset the shot clock
				var b/*:FlxSprite*/ = this.bullets$5[$$private.bulletIndex];		//Then look up the bullet
				b.reset(this.x + this.width / 2 - b.width, this.y);
				b.velocity.y = 65;
				$$private.bulletIndex++;
				if($$private.bulletIndex >= this.bullets$5.length)
					$$private.bulletIndex = 0;
			}
			
			//Finally, the all important basic game loop update
			this.update$5();
		},
		
		//This function just resets our bullet logic timer to a random value between 1 and 11
		"private function restart",function restart()/*:void*/
		{
			this.shotClock$5 = 1+org.flixel.FlxU.random()*10;
		},
	];},[],["org.flixel.FlxSprite","resource:alien.png","org.flixel.FlxU","org.flixel.FlxG"], "0.8.0", "0.8.1"
);