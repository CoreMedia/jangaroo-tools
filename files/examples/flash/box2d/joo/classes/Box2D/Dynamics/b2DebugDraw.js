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

"package Box2D.Dynamics",/*{


import Box2D.Collision.*
import Box2D.Collision.Shapes.*
import Box2D.Dynamics.Contacts.*
import Box2D.Dynamics.*
import Box2D.Common.Math.*
import Box2D.Common.*

import Box2D.Common.b2internal
use namespace b2internal

import flash.display.Sprite*/


/**
* Implement and register this class with a b2World to provide debug drawing of physics
* entities in your game.
*/
"public class b2DebugDraw",1,function($$private){;return[


	"public function b2DebugDraw",function b2DebugDraw$(){
		this.m_drawFlags$1 = 0;
	},

	//virtual ~b2DebugDraw() {}

	//enum
	//{
	/** Draw shapes */
	"static public var",{ e_shapeBit/*:uint*/ 			: 0x0001},
	/** Draw joint connections */
	"static public var",{ e_jointBit/*:uint*/			: 0x0002},
	/** Draw axis aligned bounding boxes */
	"static public var",{ e_aabbBit/*:uint*/			: 0x0004},
	/** Draw broad-phase pairs */
	"static public var",{ e_pairBit/*:uint*/			: 0x0008},
	/** Draw center of mass frame */
	"static public var",{ e_centerOfMassBit/*:uint*/	: 0x0010},
	/** Draw controllers */
	"static public var",{ e_controllerBit/*:uint*/		: 0x0020},
	//};

	/**
	* Set the drawing flags.
	*/
	"public function SetFlags",function SetFlags(flags/*:uint*/)/* : void*/{
		this.m_drawFlags$1 = flags;
	},

	/**
	* Get the drawing flags.
	*/
	"public function GetFlags",function GetFlags()/* : uint*/{
		return this.m_drawFlags$1;
	},
	
	/**
	* Append flags to the current flags.
	*/
	"public function AppendFlags",function AppendFlags(flags/*:uint*/)/* : void*/{
		this.m_drawFlags$1 |= flags;
	},

	/**
	* Clear flags from the current flags.
	*/
	"public function ClearFlags",function ClearFlags(flags/*:uint*/)/* : void*/ {
		this.m_drawFlags$1 &= ~flags;
	},

	/**
	* Set the sprite
	*/
	"public function SetSprite",function SetSprite(sprite/*:Sprite*/)/* : void*/ {
		this.m_sprite = sprite; 
	},
	
	/**
	* Get the sprite
	*/
	"public function GetSprite",function GetSprite()/* : Sprite*/ {
		return this.m_sprite;
	},
	
	/**
	* Set the draw scale
	*/
	"public function SetDrawScale",function SetDrawScale(drawScale/*:Number*/)/* : void*/ {
		this.m_drawScale$1 = drawScale; 
	},
	
	/**
	* Get the draw
	*/
	"public function GetDrawScale",function GetDrawScale()/* : Number*/ {
		return this.m_drawScale$1;
	},
	
	/**
	* Set the line thickness
	*/
	"public function SetLineThickness",function SetLineThickness(lineThickness/*:Number*/)/* : void*/ {
		this.m_lineThickness$1 = lineThickness; 
	},
	
	/**
	* Get the line thickness
	*/
	"public function GetLineThickness",function GetLineThickness()/* : Number*/ {
		return this.m_lineThickness$1;
	},
	
	/**
	* Set the alpha value used for lines
	*/
	"public function SetAlpha",function SetAlpha(alpha/*:Number*/)/* : void*/ {
		this.m_alpha$1 = alpha; 
	},
	
	/**
	* Get the alpha value used for lines
	*/
	"public function GetAlpha",function GetAlpha()/* : Number*/ {
		return this.m_alpha$1;
	},
	
	/**
	* Set the alpha value used for fills
	*/
	"public function SetFillAlpha",function SetFillAlpha(alpha/*:Number*/)/* : void*/ {
		this.m_fillAlpha$1 = alpha; 
	},
	
	/**
	* Get the alpha value used for fills
	*/
	"public function GetFillAlpha",function GetFillAlpha()/* : Number*/ {
		return this.m_fillAlpha$1;
	},
	
	/**
	* Set the scale used for drawing XForms
	*/
	"public function SetXFormScale",function SetXFormScale(xformScale/*:Number*/)/* : void*/ {
		this.m_xformScale$1 = xformScale; 
	},
	
	/**
	* Get the scale used for drawing XForms
	*/
	"public function GetXFormScale",function GetXFormScale()/* : Number*/ {
		return this.m_xformScale$1;
	},
	
	/**
	* Draw a closed polygon provided in CCW order.
	*/
	"public virtual function DrawPolygon",function DrawPolygon(vertices/*:Array*/, vertexCount/*:int*/, color/*:b2Color*/)/* : void*/{
		
		this.m_sprite.graphics.lineStyle(this.m_lineThickness$1, color.color, this.m_alpha$1);
		this.m_sprite.graphics.moveTo(vertices[0].x * this.m_drawScale$1, vertices[0].y * this.m_drawScale$1);
		for (var i/*:int*/ = 1; i < vertexCount; i++){
				this.m_sprite.graphics.lineTo(vertices[i].x * this.m_drawScale$1, vertices[i].y * this.m_drawScale$1);
		}
		this.m_sprite.graphics.lineTo(vertices[0].x * this.m_drawScale$1, vertices[0].y * this.m_drawScale$1);
		
	},

	/**
	* Draw a solid closed polygon provided in CCW order.
	*/
	"public virtual function DrawSolidPolygon",function DrawSolidPolygon(vertices/*:Array*//*b2Vec2*/, vertexCount/*:int*/, color/*:b2Color*/)/* : void*/{
		
		this.m_sprite.graphics.lineStyle(this.m_lineThickness$1, color.color, this.m_alpha$1);
		this.m_sprite.graphics.moveTo(vertices[0].x * this.m_drawScale$1, vertices[0].y * this.m_drawScale$1);
		this.m_sprite.graphics.beginFill(color.color, this.m_fillAlpha$1);
		for (var i/*:int*/ = 1; i < vertexCount; i++){
				this.m_sprite.graphics.lineTo(vertices[i].x * this.m_drawScale$1, vertices[i].y * this.m_drawScale$1);
		}
		this.m_sprite.graphics.lineTo(vertices[0].x * this.m_drawScale$1, vertices[0].y * this.m_drawScale$1);
		this.m_sprite.graphics.endFill();
		
	},

	/**
	* Draw a circle.
	*/
	"public virtual function DrawCircle",function DrawCircle(center/*:b2Vec2*/, radius/*:Number*/, color/*:b2Color*/)/* : void*/{
		
		this.m_sprite.graphics.lineStyle(this.m_lineThickness$1, color.color, this.m_alpha$1);
		this.m_sprite.graphics.drawCircle(center.x * this.m_drawScale$1, center.y * this.m_drawScale$1, radius * this.m_drawScale$1);
		
	},
	
	/**
	* Draw a solid circle.
	*/
	"public virtual function DrawSolidCircle",function DrawSolidCircle(center/*:b2Vec2*/, radius/*:Number*/, axis/*:b2Vec2*/, color/*:b2Color*/)/* : void*/{
		
		this.m_sprite.graphics.lineStyle(this.m_lineThickness$1, color.color, this.m_alpha$1);
		this.m_sprite.graphics.moveTo(0,0);
		this.m_sprite.graphics.beginFill(color.color, this.m_fillAlpha$1);
		this.m_sprite.graphics.drawCircle(center.x * this.m_drawScale$1, center.y * this.m_drawScale$1, radius * this.m_drawScale$1);
		this.m_sprite.graphics.endFill();
		this.m_sprite.graphics.moveTo(center.x * this.m_drawScale$1, center.y * this.m_drawScale$1);
		this.m_sprite.graphics.lineTo((center.x + axis.x*radius) * this.m_drawScale$1, (center.y + axis.y*radius) * this.m_drawScale$1);
		
	},

	
	/**
	* Draw a line segment.
	*/
	"public virtual function DrawSegment",function DrawSegment(p1/*:b2Vec2*/, p2/*:b2Vec2*/, color/*:b2Color*/)/* : void*/{
		
		this.m_sprite.graphics.lineStyle(this.m_lineThickness$1, color.color, this.m_alpha$1);
		this.m_sprite.graphics.moveTo(p1.x * this.m_drawScale$1, p1.y * this.m_drawScale$1);
		this.m_sprite.graphics.lineTo(p2.x * this.m_drawScale$1, p2.y * this.m_drawScale$1);
		
	},

	/**
	* Draw a transform. Choose your own length scale.
	* @param xf a transform.
	*/
	"public virtual function DrawTransform",function DrawTransform(xf/*:b2Transform*/)/* : void*/{
		
		this.m_sprite.graphics.lineStyle(this.m_lineThickness$1, 0xff0000, this.m_alpha$1);
		this.m_sprite.graphics.moveTo(xf.position.x * this.m_drawScale$1, xf.position.y * this.m_drawScale$1);
		this.m_sprite.graphics.lineTo((xf.position.x + this.m_xformScale$1*xf.R.col1.x) * this.m_drawScale$1, (xf.position.y + this.m_xformScale$1*xf.R.col1.y) * this.m_drawScale$1);

		this.m_sprite.graphics.lineStyle(this.m_lineThickness$1, 0x00ff00, this.m_alpha$1);
		this.m_sprite.graphics.moveTo(xf.position.x * this.m_drawScale$1, xf.position.y * this.m_drawScale$1);
		this.m_sprite.graphics.lineTo((xf.position.x + this.m_xformScale$1*xf.R.col2.x) * this.m_drawScale$1, (xf.position.y + this.m_xformScale$1*xf.R.col2.y) * this.m_drawScale$1);
		
	},
	
	
	
	"private var",{ m_drawFlags/*:uint*/:0},
	"b2internal var",{ m_sprite/*:Sprite*/:null},
	"private var",{ m_drawScale/*:Number*/ : 1.0},
	
	"private var",{ m_lineThickness/*:Number*/ : 1.0},
	"private var",{ m_alpha/*:Number*/ : 1.0},
	"private var",{ m_fillAlpha/*:Number*/ : 1.0},
	"private var",{ m_xformScale/*:Number*/ : 1.0},
	
];},[],[], "0.8.0", "0.8.1"

);