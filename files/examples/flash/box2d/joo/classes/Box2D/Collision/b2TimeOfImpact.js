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

"package Box2D.Collision",/*{
	
import Box2D.Common.Math.*
import Box2D.Common.*
import Box2D.Collision.Shapes.*
import Box2D.Collision.*

import Box2D.Common.b2internal
use namespace b2internal*/


/**
* @private
*/
"public class b2TimeOfImpact",1,function($$private){var assert=joo.assert;return[function(){joo.classLoader.init(Number);},

	
	"private static var",{ b2_toiCalls/*:int*/ : 0},
	"private static var",{ b2_toiIters/*:int*/ : 0},
	"private static var",{ b2_toiMaxIters/*:int*/ : 0},
	"private static var",{ b2_toiRootIters/*:int*/ : 0},
	"private static var",{ b2_toiMaxRootIters/*:int*/ : 0},

	"private static var",{ s_cache/*:b2SimplexCache*/ :function(){return( new Box2D.Collision.b2SimplexCache());}},
	"private static var",{ s_distanceInput/*:b2DistanceInput*/ :function(){return( new Box2D.Collision.b2DistanceInput());}},
	"private static var",{ s_xfA/*:b2Transform*/ :function(){return( new Box2D.Common.Math.b2Transform());}},
	"private static var",{ s_xfB/*:b2Transform*/ :function(){return( new Box2D.Common.Math.b2Transform());}},
	"private static var",{ s_fcn/*:b2SeparationFunction*/ :function(){return( new Box2D.Collision.b2SeparationFunction());}},
	"private static var",{ s_distanceOutput/*:b2DistanceOutput*/ :function(){return( new Box2D.Collision.b2DistanceOutput());}},
	"public static function TimeOfImpact",function TimeOfImpact(input/*:b2TOIInput*/)/*:Number*/
	{
		++$$private.b2_toiCalls;
		
		var proxyA/*:b2DistanceProxy*/ = input.proxyA;
		var proxyB/*:b2DistanceProxy*/ = input.proxyB;
		
		var sweepA/*:b2Sweep*/ = input.sweepA;
		var sweepB/*:b2Sweep*/ = input.sweepB;/*
		
		assert sweepA.t0 == sweepB.t0;
		assert 1.0 - sweepA.t0 > Number.MIN_VALUE;*/
		
		var radius/*:Number*/ = proxyA.m_radius + proxyB.m_radius;
		var tolerance/*:Number*/ = input.tolerance;
		
		var alpha/*:Number*/ = 0.0;/*
		
		const*/var k_maxIterations/*:int*/ = 1000; //TODO_ERIN b2Settings
		var iter/*:int*/ = 0;
		var target/*:Number*/ = 0.0;
		
		// Prepare input for distance query.
		var cache/*:b2SimplexCache*/ = $$private.s_cache;
		cache.count = 0;
		var distanceInput/*:b2DistanceInput*/ = $$private.s_distanceInput;
		distanceInput.useRadii = false;
		
		var xfA/*:b2Transform*/ = $$private.s_xfA;
		var xfB/*:b2Transform*/ = $$private.s_xfB;
		
		for (;; )
		{
			sweepA.GetTransform(xfA, alpha);
			sweepB.GetTransform(xfB, alpha);
			
			// Get the distance between shapes
			distanceInput.proxyA = proxyA;
			distanceInput.proxyB = proxyB;
			distanceInput.transformA = xfA;
			distanceInput.transformB = xfB;
			var distanceOutput/*:b2DistanceOutput*/ = $$private.s_distanceOutput;
			Box2D.Collision.b2Distance.Distance(distanceOutput, cache, distanceInput);
			
			if (distanceOutput.distance <= 0.0)
			{
				alpha = 1.0;
				break;
			}
			
			var fcn/*:b2SeparationFunction*/ = $$private.s_fcn;
			fcn.Initialize(cache, proxyA, xfA, proxyB, xfB);
			
			var separation/*:Number*/ = fcn.Evaluate(xfA, xfB);
			if (separation <= 0.0)
			{
				alpha = 1.0;
				break;
			}
			
			if (iter == 0)
			{
				// Compute a reasonable target distance to give some breathing room
				// for conservative advancement. We take advantage of the shape radii
				// to create additional clearance
				if (separation > radius)
				{
					target = Box2D.Common.Math.b2Math.Max(radius - tolerance, 0.75 * radius);
				}
				else
				{
					target = Box2D.Common.Math.b2Math.Max(separation - tolerance, 0.02 * radius);
				}
			}
			
			if (separation - target < 0.5 * tolerance)
			{
				if (iter == 0)
				{
					alpha = 1.0;
					break;
				}
				break;
			}
			
//#if 0
			// Dump the curve seen by the root finder
			//{
				//const N:int = 100;
				//var dx:Number = 1.0 / N;
				//var xs:Array/*Number*/ = new Array(N + 1);
				//var fs:Array/*Number*/ = new Array(N + 1);
				//
				//var x:Number = 0.0;
				//for (var i:int = 0; i <= N; i++)
				//{
					//sweepA.GetTransform(xfA, x);
					//sweepB.GetTransform(xfB, x);
					//var f:Number = fcn.Evaluate(xfA, xfB) - target;
					//
					//trace(x, f);
					//xs[i] = x;
					//fx[i] = f'
					//
					//x += dx;
				//}
			//}
//#endif
			// Compute 1D root of f(x) - target = 0
			var newAlpha/*:Number*/ = alpha;
			{
				var x1/*:Number*/ = alpha;
				var x2/*:Number*/ = 1.0;
				
				var f1/*:Number*/ = separation;
				
				sweepA.GetTransform(xfA, x2);
				sweepB.GetTransform(xfB, x2);
				
				var f2/*:Number*/ = fcn.Evaluate(xfA, xfB);
				
				// If intervals don't overlap at t2, then we are done
				if (f2 >= target)
				{
					alpha = 1.0;
					break;
				}
				
				// Determine when intervals intersect
				var rootIterCount/*:int*/ = 0;
				for (;; )
				{
					// Use a mis of the secand rule and bisection
					var x/*:Number*/;
					if (rootIterCount & 1)
					{
						// Secant rule to improve convergence
						x = x1 + (target - f1) * (x2 - x1) / (f2 - f1);
					}
					else
					{
						// Bisection to guarantee progress
						x = 0.5 * (x1 + x2);
					}
					
					sweepA.GetTransform(xfA, x);
					sweepB.GetTransform(xfB, x);
					
					var f/*:Number*/ = $$private.s_fcn.Evaluate($$private.s_xfA, $$private.s_xfB);
					
					if (Box2D.Common.Math.b2Math.Abs(f - target) < 0.025 * tolerance)
					{
						newAlpha = x;
						break;
					}
					
					// Ensure we continue to bracket the root
					if (f > target)
					{
						x1 = x;
						f1 = f;
					}
					else
					{
						x2 = x;
						f2 = f;
					}
					
					++rootIterCount;
					++$$private.b2_toiRootIters;
					if (rootIterCount == 50)
					{
						break;
					}
				}
				
				$$private.b2_toiMaxRootIters = Box2D.Common.Math.b2Math.Max($$private.b2_toiMaxRootIters, rootIterCount);
			}
			
			// Ensure significant advancement
			if (newAlpha < (1.0 + 100.0 * Number.MIN_VALUE) * alpha)
			{
				break;
			}
			
			alpha = newAlpha;
			
			iter++;
			++$$private.b2_toiIters;
			
			if (iter == k_maxIterations)
			{
				break;
			}
		}
		
		$$private.b2_toiMaxIters = Box2D.Common.Math.b2Math.Max($$private.b2_toiMaxIters, iter);

		return alpha;
	},

];},["TimeOfImpact"],["Box2D.Collision.b2SimplexCache","Box2D.Collision.b2DistanceInput","Box2D.Common.Math.b2Transform","Box2D.Collision.b2SeparationFunction","Box2D.Collision.b2DistanceOutput","Number","Box2D.Collision.b2Distance","Box2D.Common.Math.b2Math"], "0.8.0", "0.8.1"

);