joo.classLoader.prepare(/*
* Copyright (c) 2010 Adam Newgas http://www.boristhebrave.com
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

"package Box2D.Dynamics.Controllers",/* 
{
	import Box2D.Dynamics.b2Body
	import Box2D.Dynamics.b2World*/
	
	/**
	 * Provides iterators from every body in the world, optionally passing some filter.
	 */
	"public class b2WorldBodyIterable implements Box2D.Dynamics.Controllers.IBodyIterable",1,function($$private){var is=joo.is,as=joo.as;return[
	
		"public function b2WorldBodyIterable",function b2WorldBodyIterable$(world/*:b2World*/)
		{
			this.m_world$1 = world;
		},
		
		/**
		 * Gets the filter used to restrict bodies returned.
		 */
		"public function GetFilter",function GetFilter()/*:Function*/
		{
			return this.m_filter$1;
		},
		
		/**
		 * Sets the filter used to restrict bodies returned.
		 * Filter should be a Function with signature
		 * 
		 * <code>
		 * function filter(body:b2Body):Boolean
		 * </code>
		 * 
		 * where return true means the body should be in the iterator.
		 */
		"public function SetFilter",function SetFilter(filter/*:Function*/)/*:void*/
		{
			this.m_filter$1 = filter;
		},
		
		/** @inheritDoc */
		"public function GetIterator",function GetIterator()/*:IBodyIterator*/
		{
			if (this.m_filter$1 != null)
				return new Box2D.Dynamics.Controllers.b2WorldBodyIterable.b2FilteringWorldBodyIterator(this.m_world$1,this.m_filter$1);
			else
				return new Box2D.Dynamics.Controllers.b2WorldBodyIterable.b2WorldBodyIterator(this.m_world$1);
		},
		
		/** @inheritDoc */
		"public function ResetIterator",function ResetIterator(iterator/*:IBodyIterator*/)/*:IBodyIterator*/
		{
			if (this.m_filter$1 != null)
			{
				if (is(iterator,  Box2D.Dynamics.Controllers.b2WorldBodyIterable.b2FilteringWorldBodyIterator))
				{
					var iter1/*:b2FilteringWorldBodyIterator*/ =as( iterator,  Box2D.Dynamics.Controllers.b2WorldBodyIterable.b2FilteringWorldBodyIterator);
					iter1.Reset(this.m_world$1, this.m_filter$1);
					return iter1;
				}else{
					return this.GetIterator();
				}
			}else {
				if (is(iterator,  Box2D.Dynamics.Controllers.b2WorldBodyIterable.b2WorldBodyIterator))
				{
					var iter2/*:b2WorldBodyIterator*/ =as( iterator,  Box2D.Dynamics.Controllers.b2WorldBodyIterable.b2WorldBodyIterator);
					iter2.Reset(this.m_world$1);
					return iter2;
				}else{
					return this.GetIterator();
				}
			}
		},
		
		"private var",{ m_filter/*:Function*/:null},
		"private var",{ m_world/*:b2World*/:null},
	/*
import Box2D.Dynamics.b2Body
import Box2D.Dynamics.b2World
import Box2D.Dynamics.Controllers.IBodyIterator*/

"internal class b2WorldBodyIterator implements Box2D.Dynamics.Controllers.IBodyIterator",1,function($$private){;return[

	"public var",{ body/*:b2Body*/:null},
	
	"public function b2WorldBodyIterator",function b2WorldBodyIterator$(world/*:b2World*/)
	{
		this.Reset(world);
	},
	
	"public function Reset",function Reset(world/*:b2World*/)/*:void*/
	{
		this.body = world.GetBodyList();
	},
	
	"public function HasNext",function HasNext()/*:Boolean*/
	{
		return this.body != null;
	},
		
	"public function Next",function Next()/*:b2Body*/
	{
		var localBody/*:b2Body*/ = this.body;
		this.body = this.body.GetNext();
		return localBody;
	},
];},[],

"internal class b2FilteringWorldBodyIterator implements Box2D.Dynamics.Controllers.IBodyIterator",1,function($$private){;return[

	"public var",{ body/*:b2Body*/:null},
	"public var",{ filter/*:Function*/:null},
	
	"public function b2FilteringWorldBodyIterator",function b2FilteringWorldBodyIterator$(world/*:b2World*/, filter/*:Function*/)
	{
		this.Reset(world, filter);
	},
	
	"public function Reset",function Reset(world/*:b2World*/, filter/*:Function*/)/*:void*/
	{
		this.body = world.GetBodyList();
		this.filter = filter;
		this.Seek$1();
	},
	
	"public function HasNext",function HasNext()/*:Boolean*/
	{
		return this.body != null;
	},
		
	"public function Next",function Next()/*:b2Body*/
	{
		var localBody/*:b2Body*/ = this.body;
		this.body = this.body.GetNext();
		this.Seek$1();
		return localBody;
	},
	
	"private function Seek",function Seek()/*:void*/
	{
		while (this.body && !this.filter(this.body))
		{
			this.body = this.body.GetNext();
		}
	},
];},[],];},[],["Box2D.Dynamics.Controllers.IBodyIterable","Box2D.Dynamics.Controllers.IBodyIterator"], "0.8.0", "0.8.1"
	
);