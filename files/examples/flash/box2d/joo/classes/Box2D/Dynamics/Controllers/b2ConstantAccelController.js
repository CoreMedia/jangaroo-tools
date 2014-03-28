joo.classLoader.prepare(/*
* Copyright (c) 2006-2007 Adam Newgas
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

"package Box2D.Dynamics.Controllers",/*{

import Box2D.Common.Math.*
import Box2D.Common.*
import Box2D.Collision.Shapes.*
import Box2D.Dynamics.**/


/**
 * Applies an acceleration every frame, like gravity
 */
"public class b2ConstantAccelController extends Box2D.Dynamics.Controllers.b2Controller",2,function($$private){;return[
	
	/**
	 * The acceleration to apply
	 */
	"public var",{ A/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2(0,0));}},
	
	"public override function Step",function Step(step/*:b2TimeStep*/)/*:void*/{
		var smallA/*:b2Vec2*/ = new Box2D.Common.Math.b2Vec2(this.A.x*step.dt,this.A.y*step.dt);
		for (this.m_iterator$2 = this.m_bodyIterable.ResetIterator(this.m_iterator$2); this.m_iterator$2.HasNext(); )
		{
			var body/*:b2Body*/ = this.m_iterator$2.Next();
			if(!body.IsAwake())
				continue;
			//Am being lazy here
			body.SetLinearVelocity(new Box2D.Common.Math.b2Vec2(
				body.GetLinearVelocity().x +smallA.x,
				body.GetLinearVelocity().y +smallA.y
				));
		}
	},
	
	"public override function SetBodyIterable",function SetBodyIterable(iterable/*:IBodyIterable*/)/*:void*/ 
	{
		this.SetBodyIterable$2(iterable);
		this.m_iterator$2 = this.m_bodyIterable.GetIterator();
	},
	
	"private var",{ m_iterator/*:IBodyIterator*/:null},
"public function b2ConstantAccelController",function b2ConstantAccelController$(){this.super$2();this.A=this.A();}];},[],["Box2D.Dynamics.Controllers.b2Controller","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"

);