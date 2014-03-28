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
import Box2D.Dynamics.*
import flash.events.**/
	
	/**
	 * Adapts a regular vector to a IBodyIterable.
	 */
	"public class b2ManualBodyIterable implements Box2D.Dynamics.Controllers.IBodyIterable",1,function($$private){var as=joo.as,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(Box2D.Dynamics.b2World);},
	
		/**
		 * Constructs a b2ManualBodyIterable from passed in bodies.
		 */
		"public static function FromBodies",function FromBodies(/*...bodies*/)/*:b2ManualBodyIterable*/
		{var bodies=arguments;
			return Box2D.Dynamics.Controllers.b2ManualBodyIterable.FromArray(bodies);
		},
		
		/**
		 * Constructs a b2ManualBodyIterable from an Array of bodies.
		 */
		"public static function FromArray",function FromArray(bodyList/*:Array*/)/*:b2ManualBodyIterable*/
		{
			var bodyList2/*:Array*//*b2Body*/ = new Array/*b2Body*/();
			for/* each*/(var $1 in bodyList)
			{var body/*:b2Body*/ = bodyList[$1];
				bodyList2[bodyList2.length] = body;
			}
			return new Box2D.Dynamics.Controllers.b2ManualBodyIterable(bodyList2);
		},
		
		/**
		 * Constructs a b2ManualBodyIterable from a Vector of bodies.
		 */
		"public function b2ManualBodyIterable",function b2ManualBodyIterable$(bodyList/*:Array/*b2Body* / = null*/)
		{if(arguments.length<1){bodyList/*b2Body*/ = null;}
			var bodyList2/*:Array*//*b2Body*/ = new Array/*b2Body*/();
			if (bodyList)
			{
				for/* each*/(var $1 in bodyList)
				{var body/*:b2Body*/ = bodyList[$1];
					bodyList2[bodyList2.length] = body;
				}
			}
			this.m_bodyList$1 = bodyList2;
		},
		
		/** Adds a body to the container */
		"public function AddBody",function AddBody(body/*:b2Body*/)/*:void*/
		{
			var p/*:int*/ = this.m_bodyList$1.indexOf(body);
			if (p != -1)
				return;
			this.m_bodyList$1[this.m_bodyList$1.length] = body;
			this.GetEventDispatcher$1(body).addEventListener(Box2D.Dynamics.b2World.REMOVEBODY, $$bound(this,"OnBodyRemoved$1"), false, 0, true);
		},
		
		/** Removes a body to a controller */
		"public function RemoveBody",function RemoveBody(body/*:b2Body*/)/*:void*/
		{
			var p/*:int*/ = this.m_bodyList$1.indexOf(body);
			if (p == -1)
				return;
			this.m_bodyList$1.splice(p, 1);
			this.GetEventDispatcher$1(body).removeEventListener(Box2D.Dynamics.b2World.REMOVEBODY, $$bound(this,"OnBodyRemoved$1"));
		},
		
		/** Removes all bodies */
		"public function Clear",function Clear()/*:void*/
		{
			while (this.m_bodyList$1.length > 0)
				this.RemoveBody(this.m_bodyList$1[0]);
		},
		
		"private function OnBodyRemoved",function OnBodyRemoved(e/*:b2BodyEvent*/)/*:void*/
		{
			this.RemoveBody(e.body);
		},
		
		"private function GetEventDispatcher",function GetEventDispatcher(body/*:b2Body*/)/*:IEventDispatcher*/
		{
			var dispatcher/*:IEventDispatcher*/ = body.GetEventDispatcher();
			if (!dispatcher)
			{
				dispatcher = new flash.events.EventDispatcher();
				body.SetEventDispatcher(dispatcher);
			}
			return dispatcher;
		},
		
		/** @inheritDoc */
		"public function GetIterator",function GetIterator()/*:IBodyIterator*/
		{
			var iter/*:b2ManualBodyIterator*/ = new Box2D.Dynamics.Controllers.b2ManualBodyIterator();
			iter.bodyList = this.m_bodyList$1;
			return iter;
		},
		
		/** @inheritDoc */
		"public function ResetIterator",function ResetIterator(iterator/*:IBodyIterator*/)/*:IBodyIterator*/
		{
			var iter/*:b2ManualBodyIterator*/ =as( iterator,  Box2D.Dynamics.Controllers.b2ManualBodyIterator);
			iter.bodyList = this.m_bodyList$1;
			iter.position = 0;
			return iter;
		},
		
		"private var",{ m_bodyList/*:Array*/:null}/*b2Body*/,
	];},["FromBodies","FromArray"],["Box2D.Dynamics.Controllers.IBodyIterable","Array","Box2D.Dynamics.b2World","flash.events.EventDispatcher","Box2D.Dynamics.Controllers.b2ManualBodyIterator"], "0.8.0", "0.8.1"
	
);