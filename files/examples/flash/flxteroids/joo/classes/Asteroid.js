joo.classLoader.prepare("package",/*
{
	import org.flixel.**/

	//This is the class used for all the different asteroid sizes
	"public class Asteroid extends WrapSprite",6,function($$private){var as=joo.as;return[function(){joo.classLoader.init(org.flixel.FlxG);},
	
		"static public var",{ group/*:FlxGroup*/:null},
		
		{Embed:{source:"small.png"}}, "private var",{ ImgSmall/*:Class*/:null},
		{Embed:{source:"medium.png"}}, "private var",{ ImgMedium/*:Class*/:null},
		{Embed:{source:"large.png"}}, "private var",{ ImgLarge/*:Class*/:null},
		
		//Asteroid constructor - doesn't do much since
		// we want to be able to easily recycle asteroids later...
		"public function Asteroid",function Asteroid$()
		{
			this.super$6();
			this.antialiasing = true; //Smooth rotations
		},
		
		//This function actually creates the asteroid
		"public function create",function create(X/*:int=0*/,Y/*:int=0*/,VelocityX/*:Number=0*/,VelocityY/*:Number=0*/,Size/*:Class=null*/)/*:Asteroid*/
		{if(arguments.length<5){if(arguments.length<4){if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){X=0;}Y=0;}VelocityX=0;}VelocityY=0;}Size=null;}
			//This function can be used to reset or revive an asteroid too, so flip all these flags back on
			this.exists = true;
			this.visible = true;
			this.active = true;
			this.solid = true;
			this.loadRotatedGraphic((Size == null) ? this.ImgLarge$6 : Size,100,-1,true,true);
			this.alterBoundingBox();
			
			//Set the asteroids a-rotatin' at a random speed (looks neat)
			//angularVelocity = FlxU.random()*120 - 60;
			this.angle = org.flixel.FlxU.random()*360;
			
			//Initialize a splinter of asteroid if necessary
			if((X != 0) || (Y != 0))
			{
				this.x = X;
				this.y = Y;
				this.velocity.x = VelocityX;
				this.velocity.y = VelocityY;
				return this;	//Just return, the rest of the code here is for spawning a new large asteroid
			}
			
			//Let's spawn a giant asteroid!
			var initial_velocity/*:int*/ = 20;
			//The basic idea here is we first try and figure out what side the asteroid
			// should come from, and then from there figure out how fast it should go,
			// and in what direction.  It looks kinda crazy but it's basically the same
			// block of code repeated twice, once for 'vertical' and once for 'horizontal'
			if(org.flixel.FlxU.random() < 0.5) 	//Appearing on the sides
			{
				if(org.flixel.FlxU.random() < 0.5)	//Appears on the left
				{
					this.x = -64 + this.offset.x;
					this.velocity.x = initial_velocity / 2 + org.flixel.FlxU.random() * initial_velocity;
				}
				else					//Appears on the right
				{
					this.x = org.flixel.FlxG.width + this.offset.x;
					this.velocity.x = -initial_velocity / 2 - org.flixel.FlxU.random() * initial_velocity;
				}
				this.y = org.flixel.FlxU.random()*(org.flixel.FlxG.height-this.height);
				this.velocity.y = org.flixel.FlxU.random() * initial_velocity * 2 - initial_velocity;
			}
			else						//Appearing on top or bottom
			{
				this.x = org.flixel.FlxU.random()*(org.flixel.FlxG.width-this.width);
				this.velocity.x = org.flixel.FlxU.random() * initial_velocity * 2 - initial_velocity;
				if(org.flixel.FlxU.random() < 0.5)	//Appears above
				{
					this.y = -64 + this.offset.y;
					this.velocity.y = initial_velocity / 2 + org.flixel.FlxU.random() * initial_velocity;
				}
				else					//Appears below
				{
					this.y = org.flixel.FlxG.height + this.offset.y;
					this.velocity.y = initial_velocity / 2 + org.flixel.FlxU.random() * initial_velocity;
				}
			}
			
			return this;
		},
		
		//Asteroids are so simple that we don't even have to override their game loop.
		//BUT we do want to override their "kill" function.  FlxG.overlapArrays() will call
		// this whenever a bullet overlaps an asteroid.  We want to make sure it makes babies!
		"override public function kill",function kill()/*:void*/
		{
			//Default kill behavior - sets exists to false, and dead to true (useful for complex animations)
			this.kill$6();
			
			//Don't spawn chunks if this was the smallest asteroid bit
			if(this.frameWidth <= 32)
				return;
			
			//Spawn new asteroid chunks
			var initial_velocity/*:int*/ = 20;
			var slot/*:uint*/;
			var size/*:Class*/;
			//Need to figure out what size of chunk to show
			if(this.frameWidth >= 64)
			{
				size = this.ImgMedium$6;
				initial_velocity *= 2;
			}
			else
			{
				size = this.ImgSmall$6;
				initial_velocity *= 3;
			}
			//Figure out how many chunks to generate
			var numChunks/*:int*/ = 2 + org.flixel.FlxU.random()*3;
			//For each chunk generate a new asteroid, filling in old slots in the list whenever possible.
			for(var i/*:uint*/ = 0; i < numChunks; i++)
			{
				//Figure out the speed and position of the new asteroid chunk
				var ax/*:Number*/ = this.x + this.width / 2;
				var ay/*:Number*/ = this.y + this.height / 2;
				var avx/*:Number*/ = org.flixel.FlxU.random() * initial_velocity * 2 - initial_velocity;
				var avy/*:Number*/ = org.flixel.FlxU.random() * initial_velocity * 2 - initial_velocity;
				
				//Figure out if we need to add a new asteroid to the group, or edit an old dead one
				var a/*:Asteroid*/ =as( Asteroid.group.getFirstAvail(),  Asteroid);
				if(a == null)
					a =as( Asteroid.group.add(new Asteroid()),  Asteroid);
				//Generate the actual asteroid
				a.create(ax,ay,avx,avy,size);
			}								
		},
		
		//It looks cool if the asteroids change rotation speed when they bump into stuff
		"override public function hitBottom",function hitBottom(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/
		{
			this.velocity.y = -this.velocity.y;
			//angularVelocity = FlxU.random()*120 - 60;
		},
		
		"override public function hitTop",function hitTop(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/
		{
			this.velocity.y = -this.velocity.y;
			//angularVelocity = FlxU.random()*120 - 60;
		},
		
		"override public function hitLeft",function hitLeft(Contact/*:FlxObject*/,Velocity/*:Number*/)/*:void*/
		{
			this.velocity.x = -this.velocity.x;
			//angularVelocity = FlxU.random()*120 - 60;
		},
	];},[],["WrapSprite","resource:small.png","resource:medium.png","resource:large.png","org.flixel.FlxU","org.flixel.FlxG"], "0.8.0", "0.8.1"
);