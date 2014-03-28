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

"package Box2D.Common",/*{

	
import Box2D.Common.*
import Box2D.Common.Math.**/


/**
* Color for debug drawing. Each value has the range [0,1].
*/

"public class b2Color",1,function($$private){;return[


	"public function b2Color",function b2Color$(rr/*:Number*/, gg/*:Number*/, bb/*:Number*/){
		this._r$1 = $$uint(255 * Box2D.Common.Math.b2Math.Clamp(rr, 0.0, 1.0));
		this._g$1 = $$uint(255 * Box2D.Common.Math.b2Math.Clamp(gg, 0.0, 1.0));
		this._b$1 = $$uint(255 * Box2D.Common.Math.b2Math.Clamp(bb, 0.0, 1.0));
	},
	
	"public function Set",function Set(rr/*:Number*/, gg/*:Number*/, bb/*:Number*/)/*:void*/{
		this._r$1 = $$uint(255 * Box2D.Common.Math.b2Math.Clamp(rr, 0.0, 1.0));
		this._g$1 = $$uint(255 * Box2D.Common.Math.b2Math.Clamp(gg, 0.0, 1.0));
		this._b$1 = $$uint(255 * Box2D.Common.Math.b2Math.Clamp(bb, 0.0, 1.0));
	},
	
	// R
	"public function set r",function r$set(rr/*:Number*/)/* : void*/{
		this._r$1 = $$uint(255 * Box2D.Common.Math.b2Math.Clamp(rr, 0.0, 1.0));
	},
	// G
	"public function set g",function g$set(gg/*:Number*/)/* : void*/{
		this._g$1 = $$uint(255 * Box2D.Common.Math.b2Math.Clamp(gg, 0.0, 1.0));
	},
	// B
	"public function set b",function b$set(bb/*:Number*/)/* : void*/{
		this._b$1 = $$uint(255 * Box2D.Common.Math.b2Math.Clamp(bb, 0.0, 1.0));
	},
	
	// Color
	"public function get color",function color$get()/* : uint*/{
		return (this._r$1 << 16) | (this._g$1 << 8) | (this._b$1);
	},
	
	"private var",{ _r/*:uint*/ : 0},
	"private var",{ _g/*:uint*/ : 0},
	"private var",{ _b/*:uint*/ : 0},

];},[],["uint","Box2D.Common.Math.b2Math"], "0.8.0", "0.8.1"

);