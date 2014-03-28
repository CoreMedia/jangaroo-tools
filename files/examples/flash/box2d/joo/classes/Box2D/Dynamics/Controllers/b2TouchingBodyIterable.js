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
	import Box2D.Dynamics.b2Body*/
	
	/**
	 * Lists all bodies touching the given body. You'd usually want to 
	 * pass in a sensor.
	 */
	"public class b2TouchingBodyIterable implements Box2D.Dynamics.Controllers.IBodyIterable",1,function($$private){var as=joo.as;return[
	
		
		"public function b2TouchingBodyIterable",function b2TouchingBodyIterable$(body/*:b2Body*/)
		{
			this.m_body$1 = body;
		},
		
		/** @inheritDoc */
		"public function GetIterator",function GetIterator()/*:IBodyIterator*/
		{
			var iterator/*:b2TouchingBodyIterator*/ = new Box2D.Dynamics.Controllers.b2TouchingBodyIterable.b2TouchingBodyIterator();
			return this.ResetIterator(iterator);
		},
		
		/** @inheritDoc */
		"public function ResetIterator",function ResetIterator(iterator/*:IBodyIterator*/)/*:IBodyIterator*/
		{
			var iterator2/*:b2TouchingBodyIterator*/ =as( iterator,  Box2D.Dynamics.Controllers.b2TouchingBodyIterable.b2TouchingBodyIterator);
			iterator2.contactEdge = this.m_body$1.GetContactList();
			iterator2.seen.length = 0;
			return iterator2;
		},
		
		"private var",{ m_body/*:b2Body*/:null},
	/*
import Box2D.Dynamics.b2Body
import Box2D.Dynamics.Contacts.b2ContactEdge
import Box2D.Dynamics.Controllers.IBodyIterator*/

"internal class b2TouchingBodyIterator implements Box2D.Dynamics.Controllers.IBodyIterator",1,function($$private){;return[

	"public var",{ contactEdge/*:b2ContactEdge*/:null},
	"public var",{ seen/*:Array*//*b2Body*/ :function(){return( new Array/*b2Body*/());}},
	
	"public function HasNext",function HasNext()/*:Boolean*/
	{
		return this.contactEdge != null;
	},
		
	"public function Next",function Next()/*:b2Body*/
	{
		var localBody/*:b2Body*/ = this.contactEdge.other;
		this.seen[this.seen.length] = localBody;
		this.Seek();
		return localBody;
	},
	
	"public function Seek",function Seek()/*:void*/
	{
		while (this.contactEdge && this.seen.indexOf(this.contactEdge.other)!==-1 )
		{
			this.contactEdge = this.contactEdge.next;
		}
	},
"public function b2TouchingBodyIterator",function b2TouchingBodyIterator$(){this.seen=this.seen();}];},[],];},[],["Box2D.Dynamics.Controllers.IBodyIterable","Box2D.Dynamics.Controllers.IBodyIterator","Array"], "0.8.0", "0.8.1"
	
);