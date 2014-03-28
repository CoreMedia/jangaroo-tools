joo.classLoader.prepare(/**
 * Flixel Primer
 * A tutorial project for Flixel 2
 * Written for CreativeApplications.Net
 * Copyright (c) 2010 Andreas Zecher, http://www.pixelate.de
 *
 * This project was compiled against Flixel v2.32, Flex SDK 4.0 and
 * Flash Player 10.
 *
 * Includes Flixel source files by Adam Saltsman. Sounds made with Cfxr
 * (http://thirdcog.eu/apps/cfxr) by DrPetter and Third Cog Software. Game
 * sprites inspired by Defender from Williams.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

"package",/* {	
	import org.flixel.*
	import de.pixelate.flixelprimer.**/
	
	{SWF:{width:"640", height:"480", backgroundColor:"#ABCC7D"}},
	{Frame:{factoryClass:"Preloader"}},

	"public class Main extends org.flixel.FlxGame",7,function($$private){;return[function(){joo.classLoader.init(de.pixelate.flixelprimer.PlayState);},
	
		"public function Main",function Main$()/*:void*/
		{
			this.super$7(640, 480, de.pixelate.flixelprimer.PlayState, 1);
		},
	];},[],["org.flixel.FlxGame","de.pixelate.flixelprimer.PlayState"], "0.8.0", "0.8.3"
);