
//
Box2DFlashAS3
Version 2.0.1
//

//
Homepage:
http://box2dflash.sourceforge.net
//

//
Credits:
Box2D - Erin Catto ( http://gphysics.com/ )
Box2DFlashAS3 - Matthew Bush (Flash ActionScript 3.0 port of Erin Catto's work)
Box2DFlashAS3 2.0.1 - John "shaktool" Nesky ( http://johnnesky.com )
//

//
To compile the demo:
//
Flash: extract Box2DFlashAS3 and open PhysTest.fla in Flash CS3 (9) and hit Ctrl+Enter.
Flex: Create a new ActionScript project and add all the files from the archive to the project. Set Main.as to the default application and hit Ctrl+F11.
MXMLC: From the "Examples" directory, run:
mxmlc -source-path=../Source/ Main.as;

//
Controls:
//
-Left and right arrows to change to previous/next example
-Left mouse to drag objects
-R to reset

//

Changes
//
List of general API changes from 1.4.3 can be found here
http://box2d.org/forum/viewtopic.php?f=2&t=509
Notes:
-Past users note that b2BoxDef has been replaced by SetAsBox() function in b2PolygonDef
(Refer to any of the PhysTest examples or the manual for more info and usage examples)
-Debug rendering class included, see the constructor in Test.as in the PhysTest project for example usage
-One of the major features of 2.0.0 is the inclusion of CCD, see here for more information:
http://www.box2d.org/manual.html#bullets
//


//
Getting started:
//
Please refer to the source code from the examples provided to get an idea of how to use Box2DFlash in your projects.
For more detailed information about the Box2D, please refer to the online manual at http://www.box2d.org/manual.html
Or the wiki at http://www.box2d.org/wiki/
If you have any questions about Box2D, or have any problems, please visit the Box2D forums at http://www.box2d.org/forum/ and make a note that you are using this Flash port.

Note: The 'General' folder contains some classes used in the test bed, such as for the frame rate counter and input, you don't have to include this folder to use Box2DFlashAS3 but you're welcome to use any of the code.

If you make a commercial application using Box2DFlashAS3 and would like to pass on some of your earnings, I would like to request that you donate to Erin Catto's project here: http://sourceforge.net/donate/index.php?group_id=205387 since creating a physics engine is a much larger task than simply porting it to Flash.
But if you really, really want to send me a donation, you can PayPal me at skatehead [at] gmail.com and I will be very grateful.

Please report any bugs on the forums mentioned above, or if you like, you can contact me directly at the email address mentioned above.

Thanks,
If you do use Box2DFlashAS3 in any projects, I'd love to see them, so drop me an email or make a post on the Box2D forums in the 'Games and Applications' sections.
All the best,
-Matt
