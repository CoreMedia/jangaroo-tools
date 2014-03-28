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
{*/
	
	/**
	 * A container of bodies that can be looped over many times.
	 */
	"public interface IBodyIterable",1,function($$private){;return[ 
	/*	
		/**
		 * Construct a new iterator for looping over the contained bodies once.
		 * /
		function GetIterator():IBodyIterator;*/,/*
		
		/**
		 * Reset a prexisting iterator created by this iterable.
		 * This is a performance optimization over just creating a new one with
		 * #GetIterator.
		 * /
		function ResetIterator(iterator:IBodyIterator):IBodyIterator;*/,
	];},[],[], "0.8.0", "0.8.1"
	
);