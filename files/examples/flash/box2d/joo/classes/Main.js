joo.classLoader.prepare(/*
* Copyright (c) 2006-2007 Erin Catto http://www.gphysics.com
*
* This software is provided 'as-is', without any express or implied
* warranty.  In no event will the authors be held liable for any damages
* arising from the use of this software.
* Permission is granted to anyone to use this software for any purpose,
* including commercial applications, and to alter it and redistribute it
* freely, subject to the following restrictions:
* 1. The origin of this software must not be misrepresented; you must not
* claim that you wrote the original software. If you use this software
* in a product, an acknowledgment in the product documentation would be
* appreciated but is not required.
* 2. Altered source versions must be plainly marked as such, and must not be
* misrepresented as being the original software.
* 3. This notice may not be removed or altered from any source distribution.
*/

"package",/*{

import Box2D.Dynamics.*
import Box2D.Collision.*
import Box2D.Collision.Shapes.*
import Box2D.Dynamics.Joints.*
import Box2D.Dynamics.Contacts.*
import Box2D.Common.Math.*
import flash.events.Event
import flash.display.*
import flash.text.*
import General.*
import TestBed.*

import flash.display.MovieClip*/
	{SWF:{width:'640', height:'360', backgroundColor:'#292C2C', frameRate:'30'}},
	"public class Main extends flash.display.MovieClip",7,function($$private){var $$bound=joo.boundMethod;return[function(){joo.classLoader.init(flash.text.TextFormatAlign,TestBed.TestBuoyancy,TestBed.TestRagdoll,TestBed.TestCompound,TestBed.TestStack,TestBed.TestBridge,TestBed.TestCCD,TestBed.TestTheoJansen,TestBed.TestBreakable,flash.events.Event,TestBed.TestSensor,TestBed.TestOneSidedPlatform,TestBed.TestRaycast,TestBed.TestCrankGearsPulley);},
		"public function Main",function Main$() {this.super$7();
			this.addEventListener(flash.events.Event.ENTER_FRAME, $$bound(this,"update"), false, 0, true);
			
			Main.m_fpsCounter.x = 7;
			Main.m_fpsCounter.y = 5;
			this.addChildAt(Main.m_fpsCounter, 0);
			
			Main.m_sprite = new flash.display.Sprite();
			this.addChild(Main.m_sprite);
			// input
			this.m_input = new General.Input(Main.m_sprite);
			
			
			//Instructions Text
			var instructions_text/*:TextField*/ = new flash.text.TextField();
			
			var instructions_text_format/*:TextFormat*/ = new flash.text.TextFormat("Arial", 16, 0xffffff, false, false, false);
			instructions_text_format.align = flash.text.TextFormatAlign.RIGHT;
			
			instructions_text.defaultTextFormat = instructions_text_format;
			instructions_text.x = 140;
			instructions_text.y = 4.5;
			instructions_text.width = 495;
			instructions_text.height = 61;
			instructions_text.text = "Box2DFlashAS3 2.1a\n'Left'/'Right' arrows to go to previous/next example. \n'R' to reset.";
			this.addChild(instructions_text);
			
			// textfield pointer
			Main.m_aboutText = new flash.text.TextField();
			var m_aboutTextFormat/*:TextFormat*/ = new flash.text.TextFormat("Arial", 16, 0x00CCFF, true, false, false);
			m_aboutTextFormat.align = flash.text.TextFormatAlign.RIGHT;
			Main.m_aboutText.defaultTextFormat = m_aboutTextFormat;
			Main.m_aboutText.x = 334;
			Main.m_aboutText.y = 71;
			Main.m_aboutText.width = 300;
			Main.m_aboutText.height = 30;
			this.addChild(Main.m_aboutText);
			
			// Thanks to everyone who contacted me about this fix
			instructions_text.mouseEnabled = false;
			Main.m_aboutText.mouseEnabled = false;
		},
		
		"public function update",function update(e/*:Event*/)/*:void*/{
			// clear for rendering
			Main.m_sprite.graphics.clear();
			
			// toggle between tests
			if (General.Input.isKeyPressed(39)){ // Right Arrow
				this.m_currId++;
				Main.m_currTest = null;
			}
			else if (General.Input.isKeyPressed(37)){ // Left Arrow
				this.m_currId--;
				Main.m_currTest = null;
			}
			// Reset
			else if (General.Input.isKeyPressed(82)){ // R
				Main.m_currTest = null;
			}
			
			var tests/*:Array*/ = [
				TestBed.TestRagdoll,			// Ragdoll
				TestBed.TestCompound,			// Compound Shapes
				TestBed.TestCrankGearsPulley,	// Crank/Gears/Pulley
				TestBed.TestBridge,				// Bridge
				TestBed.TestStack,				// Stack
				TestBed.TestCCD,				// CCD
				TestBed.TestTheoJansen,			// Theo Jansen
			//	TestEdges,				// Edges & Raycast
				TestBed.TestBuoyancy,			// Buoyancy
				TestBed.TestOneSidedPlatform,	// One Sided Platform
				TestBed.TestBreakable,			// Breakable
				TestBed.TestRaycast,			// Raycast
				TestBed.TestSensor,				// Sensor & Customized Controller
			//	TestSVG,				// Edge + SVG + Convex decomposition
			null
			];
			tests.length -= 1;
			
            var testCount/*:int*/ = tests.length;
			this.m_currId = (this.m_currId + testCount) % testCount;
			
			
			
			// if null, set new test
			if (!Main.m_currTest){
				switch(this.m_currId) {
					default:
						Main.m_currTest = new  tests[this.m_currId]();
				}
			}
			
			// update current test
			Main.m_currTest.Update();
			
			// Update input (last)
			General.Input.update();
			
			// update counter and limit framerate
			Main.m_fpsCounter.update();
			General.FRateLimiter.limitFrame(30);
			
		},
		
		
		//======================
		// Member data
		//======================
		"static public var",{ m_fpsCounter/*:FpsCounter*/ :function(){return( new General.FpsCounter());}},
		"public var",{ m_currId/*:int*/ : 0},
		"static public var",{ m_currTest/*:Test*/:null},
		"static public var",{ m_sprite/*:Sprite*/:null},
		"static public var",{ m_aboutText/*:TextField*/:null},
		// input
		"public var",{ m_input/*:Input*/:null},
	];},[],["flash.display.MovieClip","flash.events.Event","flash.display.Sprite","General.Input","flash.text.TextField","flash.text.TextFormat","flash.text.TextFormatAlign","TestBed.TestRagdoll","TestBed.TestCompound","TestBed.TestCrankGearsPulley","TestBed.TestBridge","TestBed.TestStack","TestBed.TestCCD","TestBed.TestTheoJansen","TestBed.TestBuoyancy","TestBed.TestOneSidedPlatform","TestBed.TestBreakable","TestBed.TestRaycast","TestBed.TestSensor","General.FRateLimiter","General.FpsCounter"], "0.8.0", "0.8.1"
);