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
 * Applies top down linear damping to the controlled bodies
 * The damping is calculated by multiplying velocity by a matrix in local co-ordinates.
 */
"public class b2TensorDampingController extends Box2D.Dynamics.Controllers.b2Controller",2,function($$private){;return[function(){joo.classLoader.init(Number);},
	
	/**
	 * Tensor to use in damping model
	 */
	"public var",{ T/*:b2Mat22*/ :function(){return( new Box2D.Common.Math.b2Mat22());}},
	/*Some examples (matrixes in format (row1; row2) )
	(-a 0;0 -a)		Standard isotropic damping with strength a
	(0 a;-a 0)		Electron in fixed field - a force at right angles to velocity with proportional magnitude
	(-a 0;0 -b)		Differing x and y damping. Useful e.g. for top-down wheels.
	*/
	//By the way, tensor in this case just means matrix, don't let the terminology get you down.
	
	/**
	 * Set this to a positive number to clamp the maximum amount of damping done.
	 */
	"public var",{ maxTimestep/*:Number*/ : 0},
	// Typically one wants maxTimestep to be 1/(max eigenvalue of T), so that damping will never cause something to reverse direction
	
	/**
	 * Helper function to set T in a common case
	 */
	"public function SetAxisAligned",function SetAxisAligned(xDamping/*:Number*/, yDamping/*:Number*/)/*:void*/{
		this.T.col1.x = -xDamping;
		this.T.col1.y = 0;
		this.T.col2.x = 0;
		this.T.col2.y = -yDamping;
		if(xDamping>0 || yDamping>0){
			this.maxTimestep = 1/Math.max(xDamping,yDamping);
		}else{
			this.maxTimestep = 0;
		}
	},
	
	"public override function Step",function Step(step/*:b2TimeStep*/)/*:void*/{
		var timestep/*:Number*/ = step.dt;
		if(timestep<=Number.MIN_VALUE)
			return;
		if(timestep>this.maxTimestep && this.maxTimestep>0)
			timestep = this.maxTimestep;
		for (this.m_iterator$2 = this.m_bodyIterable.ResetIterator(this.m_iterator$2); this.m_iterator$2.HasNext(); )
		{
			var body/*:b2Body*/ = this.m_iterator$2.Next();
			if(!body.IsAwake()){
				//Sleeping bodies are still - so have no damping
				continue;
			}
			var damping/*:b2Vec2*/ =
				body.GetWorldVector(
					Box2D.Common.Math.b2Math.MulMV(this.T,
						body.GetLocalVector(
							body.GetLinearVelocity()
						)
					)
				);
			body.SetLinearVelocity(new Box2D.Common.Math.b2Vec2(
				body.GetLinearVelocity().x + damping.x * timestep,
				body.GetLinearVelocity().y + damping.y * timestep
				));
		}
	},
	
	"public override function SetBodyIterable",function SetBodyIterable(iterable/*:IBodyIterable*/)/*:void*/ 
	{
		this.SetBodyIterable$2(iterable);
		this.m_iterator$2 = this.m_bodyIterable.GetIterator();
	},
	
	"private var",{ m_iterator/*:IBodyIterator*/:null},
"public function b2TensorDampingController",function b2TensorDampingController$(){this.super$2();this.T=this.T();}];},[],["Box2D.Dynamics.Controllers.b2Controller","Box2D.Common.Math.b2Mat22","Math","Number","Box2D.Common.Math.b2Math","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"

);