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

"package Box2D.Common.Math",/*{

	
import Box2D.Common.**/
	
	
/**
* A 3-by-3 matrix. Stored in column-major order.
*/
"public class b2Mat33",1,function($$private){;return[

	"public function b2Mat33",function b2Mat33$(c1/*:b2Vec3=null*/, c2/*:b2Vec3=null*/, c3/*:b2Vec3=null*/)
	{if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){c1=null;}c2=null;}c3=null;}this.col1=this.col1();this.col2=this.col2();this.col3=this.col3();
		if (!c1 && !c2 && !c3)
		{
			this.col1.SetZero();
			this.col2.SetZero();
			this.col3.SetZero();
		}
		else
		{
			this.col1.SetV(c1);
			this.col2.SetV(c2);
			this.col3.SetV(c3);
		}
	},
	
	"public function SetVVV",function SetVVV(c1/*:b2Vec3*/, c2/*:b2Vec3*/, c3/*:b2Vec3*/)/* : void*/
	{
		this.col1.SetV(c1);
		this.col2.SetV(c2);
		this.col3.SetV(c3);
	},
	
	"public function Copy",function Copy()/*:b2Mat33*/{
		return new Box2D.Common.Math.b2Mat33(this.col1, this.col2, this.col3);
	},
	
	"public function SetM",function SetM(m/*:b2Mat33*/)/* : void*/
	{
		this.col1.SetV(m.col1);
		this.col2.SetV(m.col2);
		this.col3.SetV(m.col3);
	},
	
	"public function AddM",function AddM(m/*:b2Mat33*/)/* : void*/
	{
		this.col1.x += m.col1.x;
		this.col1.y += m.col1.y;
		this.col1.z += m.col1.z;
		this.col2.x += m.col2.x;
		this.col2.y += m.col2.y;
		this.col2.z += m.col2.z;
		this.col3.x += m.col3.x;
		this.col3.y += m.col3.y;
		this.col3.z += m.col3.z;
	},
	
	"public function SetIdentity",function SetIdentity()/* : void*/
	{
		this.col1.x = 1.0; this.col2.x = 0.0; this.col3.x = 0.0;
		this.col1.y = 0.0; this.col2.y = 1.0; this.col3.y = 0.0;
		this.col1.z = 0.0; this.col2.z = 0.0; this.col3.z = 1.0;
	},

	"public function SetZero",function SetZero()/* : void*/
	{
		this.col1.x = 0.0; this.col2.x = 0.0; this.col3.x = 0.0;
		this.col1.y = 0.0; this.col2.y = 0.0; this.col3.y = 0.0;
		this.col1.z = 0.0; this.col2.z = 0.0; this.col3.z = 0.0;
	},
	
	// Solve A * x = b
	"public function Solve22",function Solve22(out/*:b2Vec2*/, bX/*:Number*/, bY/*:Number*/)/*:b2Vec2*/
	{
		//float32 a11 = col1.x, a12 = col2.x, a21 = col1.y, a22 = col2.y;
		var a11/*:Number*/ = this.col1.x;
		var a12/*:Number*/ = this.col2.x;
		var a21/*:Number*/ = this.col1.y;
		var a22/*:Number*/ = this.col2.y;
		//float32 det = a11 * a22 - a12 * a21;
		var det/*:Number*/ = a11 * a22 - a12 * a21;
		if (det != 0.0)
		{
			det = 1.0 / det;
		}
		out.x = det * (a22 * bX - a12 * bY);
		out.y = det * (a11 * bY - a21 * bX);
		
		return out;
	},
	
	// Solve A * x = b
	"public function Solve33",function Solve33(out/*:b2Vec3*/, bX/*:Number*/, bY/*:Number*/, bZ/*:Number*/)/*:b2Vec3*/
	{
		var a11/*:Number*/ = this.col1.x;
		var a21/*:Number*/ = this.col1.y;
		var a31/*:Number*/ = this.col1.z;
		var a12/*:Number*/ = this.col2.x;
		var a22/*:Number*/ = this.col2.y;
		var a32/*:Number*/ = this.col2.z;
		var a13/*:Number*/ = this.col3.x;
		var a23/*:Number*/ = this.col3.y;
		var a33/*:Number*/ = this.col3.z;
		//float32 det = b2Dot(col1, b2Cross(col2, col3));
		var det/*:Number*/ = 	a11 * (a22 * a33 - a32 * a23) +
							a21 * (a32 * a13 - a12 * a33) +
							a31 * (a12 * a23 - a22 * a13);
		if (det != 0.0)
		{
			det = 1.0 / det;
		}
		//out.x = det * b2Dot(b, b2Cross(col2, col3));
		out.x = det * (	bX * (a22 * a33 - a32 * a23) +
						bY * (a32 * a13 - a12 * a33) +
						bZ * (a12 * a23 - a22 * a13) );
		//out.y = det * b2Dot(col1, b2Cross(b, col3));
		out.y = det * (	a11 * (bY * a33 - bZ * a23) +
						a21 * (bZ * a13 - bX * a33) +
						a31 * (bX * a23 - bY * a13));
		//out.z = det * b2Dot(col1, b2Cross(col2, b));
		out.z = det * (	a11 * (a22 * bZ - a32 * bY) +
						a21 * (a32 * bX - a12 * bZ) +
						a31 * (a12 * bY - a22 * bX));
		return out;
	},

	"public var",{ col1/*:b2Vec3*/ :function(){return( new Box2D.Common.Math.b2Vec3());}},
	"public var",{ col2/*:b2Vec3*/ :function(){return( new Box2D.Common.Math.b2Vec3());}},
	"public var",{ col3/*:b2Vec3*/ :function(){return( new Box2D.Common.Math.b2Vec3());}},
];},[],["Box2D.Common.Math.b2Vec3"], "0.8.0", "0.8.1"

);