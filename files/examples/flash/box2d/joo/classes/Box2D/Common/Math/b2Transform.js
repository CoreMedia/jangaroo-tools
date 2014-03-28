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
* A transform contains translation and rotation. It is used to represent
* the position and orientation of rigid frames.
*/
"public class b2Transform",1,function($$private){;return[

	/**
	* The default constructor does nothing (for performance).
	*/
	"public function b2Transform",function b2Transform$(pos/*:b2Vec2=null*/, r/*:b2Mat22=null*/)/* : void*/ 
	{if(arguments.length<2){if(arguments.length<1){pos=null;}r=null;}this.position=this.position();this.R=this.R();
		if (pos){
			this.position.SetV(pos);
			this.R.SetM(r);

		}
	},

	/**
	* Initialize using a position vector and a rotation matrix.
	*/
	"public function Initialize",function Initialize(pos/*:b2Vec2*/, r/*:b2Mat22*/)/* : void*/ 
	{
		this.position.SetV(pos);
		this.R.SetM(r);
	},

	/**
	* Set this to the identity transform.
	*/
	"public function SetIdentity",function SetIdentity()/* : void*/
	{
		this.position.SetZero();
		this.R.SetIdentity();
	},

	"public function Set",function Set(x/*:b2Transform*/)/* : void*/
	{
		this.position.SetV(x.position);
		this.R.SetM(x.R);
	},
	
	"public function GetInverse",function GetInverse(out/*:b2Transform = null*/)/*:b2Transform*/
	{if(arguments.length<1){out = null;}
		if (!out)
			out = new Box2D.Common.Math.b2Transform();
		this.R.GetInverse(out.R);
		out.position.SetV(Box2D.Common.Math.b2Math.MulMV(out.R, this.position));
		out.position.NegativeSelf();
		return out;
	},
	
	/** 
	 * Calculate the angle that the rotation matrix represents.
	 */
	"public function GetAngle",function GetAngle()/*:Number*/
	{
		return Math.atan2(this.R.col1.y, this.R.col1.x);
	},
	 

	"public var",{ position/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"public var",{ R/*:b2Mat22*/ :function(){return( new Box2D.Common.Math.b2Mat22());}},
];},[],["Box2D.Common.Math.b2Math","Math","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Mat22"], "0.8.0", "0.8.1"

);