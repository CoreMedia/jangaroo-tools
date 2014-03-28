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

"package Box2D.Dynamics",/*{*/
	
	
/**
* @private
*/
"public class b2TimeStep",1,function($$private){;return[

	"public function Set",function Set(step/*:b2TimeStep*/)/*:void*/
	{
		this.dt = step.dt;
		this.inv_dt = step.inv_dt;
		this.positionIterations = step.positionIterations;
		this.velocityIterations = step.velocityIterations;
		this.warmStarting = step.warmStarting;
	},
	"public var",{ dt/*:Number*/:NaN},			// time step
	"public var",{ inv_dt/*:Number*/:NaN},		// inverse time step (0 if dt == 0).
	"public var",{ dtRatio/*:Number*/:NaN},		// dt * inv_dt0
	"public var",{ velocityIterations/*:int*/:0},
	"public var",{ positionIterations/*:int*/:0},
	"public var",{ warmStarting/*:Boolean*/:false},
];},[],[], "0.8.0", "0.8.1"


);