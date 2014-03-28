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

"package Box2D.Dynamics",/*{

import Box2D.Common.Math.*
import Box2D.Common.*
import Box2D.Collision.*
import Box2D.Collision.Shapes.*
import Box2D.Dynamics.*
import Box2D.Dynamics.Contacts.*
import Box2D.Dynamics.Joints.*

import Box2D.Common.b2internal
use namespace b2internal*/


/**
 * A fixture is used to attach a shape to a body for collision detection. A fixture
 * inherits its transform from its parent. Fixtures hold additional non-geometric data
 * such as friction, collision filters, etc.
 * Fixtures are created via b2Body::CreateFixture.
 * @warning you cannot reuse fixtures.
 */
"public class b2Fixture",1,function($$private){;return[

	/**
	 * Get the type of the child shape. You can use this to down cast to the concrete shape.
	 * @return the shape type.
	 */
	"public function GetType",function GetType()/*:int*/
	{
		return this.m_shape.GetType();
	},
	
	/**
	 * Get the child shape. You can modify the child shape, however you should not change the
	 * number of vertices because this will crash some collision caching mechanisms.
	 * Manipulating the shape may lead to non-physical behavior.
	 */
	"public function GetShape",function GetShape()/*:b2Shape*/
	{
		return this.m_shape;
	},
	
	/**
	 * Set if this fixture is a sensor.
	 */
	"public function SetSensor",function SetSensor(sensor/*:Boolean*/)/*:void*/
	{
		if ( this.m_isSensor == sensor)
			return;
			
		this.m_isSensor = sensor;
		
		if (this.m_body == null)
			return;
			
		var edge/*:b2ContactEdge*/ = this.m_body.GetContactList();
		while (edge)
		{
			var contact/*:b2Contact*/ = edge.contact;
			var fixtureA/*:b2Fixture*/ = contact.GetFixtureA();
			var fixtureB/*:b2Fixture*/ = contact.GetFixtureB();
			if (fixtureA == this || fixtureB == this)
				contact.SetSensor(fixtureA.IsSensor() || fixtureB.IsSensor());
			edge = edge.next;
		}
		
	},
	
	/**
	 * Is this fixture a sensor (non-solid)?
	 * @return the true if the shape is a sensor.
	 */
	"public function IsSensor",function IsSensor()/*:Boolean*/
	{
		return this.m_isSensor;
	},
	
	/**
	 * Set the contact filtering data. This will not update contacts until the next time
	 * step when either parent body is active and awake.
	 */
	"public function SetFilterData",function SetFilterData(filter/*:b2FilterData*/)/*:void*/
	{
		this.m_filter = filter.Copy();
		
		if (!this.m_body)
			return;
			
		var edge/*:b2ContactEdge*/ = this.m_body.GetContactList();
		while (edge)
		{
			var contact/*:b2Contact*/ = edge.contact;
			var fixtureA/*:b2Fixture*/ = contact.GetFixtureA();
			var fixtureB/*:b2Fixture*/ = contact.GetFixtureB();
			if (fixtureA == this || fixtureB == this)
				contact.FlagForFiltering();
			edge = edge.next;
		}
	},
	
	/**
	 * Get the contact filtering data.
	 */
	"public function GetFilterData",function GetFilterData()/*: b2FilterData*/
	{
		return this.m_filter.Copy();
	},
	
	/**
	 * Get the parent body of this fixture. This is NULL if the fixture is not attached.
	 * @return the parent body.
	 */
	"public function GetBody",function GetBody()/*:b2Body*/
	{
		return this.m_body;
	},
	
	/**
	 * Get the next fixture in the parent body's fixture list.
	 * @return the next shape.
	 */
	"public function GetNext",function GetNext()/*:b2Fixture*/
	{
		return this.m_next;
	},
	
	/**
	 * Get the user data that was assigned in the fixture definition. Use this to
	 * store your application specific data.
	 */
	"public function GetUserData",function GetUserData()/*:**/
	{
		return this.m_userData;
	},
	
	/**
	 * Set the user data. Use this to store your application specific data.
	 */
	"public function SetUserData",function SetUserData(data/*:**/)/*:void*/
	{
		this.m_userData = data;
	},
	
	/**
	 * Test a point for containment in this fixture.
	 * @param xf the shape world transform.
	 * @param p a point in world coordinates.
	 */
	"public function TestPoint",function TestPoint(p/*:b2Vec2*/)/*:Boolean*/
	{
		return this.m_shape.TestPoint(this.m_body.GetTransform(), p);
	},
	
	/**
	 * Perform a ray cast against this shape.
	 * @param output the ray-cast results.
	 * @param input the ray-cast input parameters.
	 */
	"public function RayCast",function RayCast(output/*:b2RayCastOutput*/, input/*:b2RayCastInput*/)/*:Boolean*/
	{
		return this.m_shape.RayCast(output, input, this.m_body.GetTransform());
	},
	
	/**
	 * Get the mass data for this fixture. The mass data is based on the density and
	 * the shape. The rotational inertia is about the shape's origin. This operation may be expensive
	 * @param massData - this is a reference to a valid massData, if it is null a new b2MassData is allocated and then returned
	 * @note if the input is null then you must get the return value.
	 */
	"public function GetMassData",function GetMassData(massData/*:b2MassData = null*/)/*:b2MassData*/
	{if(arguments.length<1){massData = null;}
		if ( massData == null )
		{
			massData = new Box2D.Collision.Shapes.b2MassData();
		}
		this.m_shape.ComputeMass(massData, this.m_density);
		return massData;
	},
	
	/**
	 * Set the density of this fixture. This will _not_ automatically adjust the mass
	 * of the body. You must call b2Body::ResetMassData to update the body's mass.
	 * @param	density
	 */
	"public function SetDensity",function SetDensity(density/*:Number*/)/*:void*/ {
		//b2Settings.b2Assert(b2Math.b2IsValid(density) && density >= 0.0);
		this.m_density = density;
	},
	
	/**
	 * Get the density of this fixture.
	 * @return density
	 */
	"public function GetDensity",function GetDensity()/*:Number*/ {
		return this.m_density;
	},
	
	/**
	 * Get the coefficient of friction.
	 */
	"public function GetFriction",function GetFriction()/*:Number*/
	{
		return this.m_friction;
	},
	
	/**
	 * Set the coefficient of friction.
	 */
	"public function SetFriction",function SetFriction(friction/*:Number*/)/*:void*/
	{
		this.m_friction = friction;
	},
	
	/**
	 * Get the coefficient of restitution.
	 */
	"public function GetRestitution",function GetRestitution()/*:Number*/
	{
		return this.m_restitution;
	},
	
	/**
	 * Get the coefficient of restitution.
	 */
	"public function SetRestitution",function SetRestitution(restitution/*:Number*/)/*:void*/
	{
		this.m_restitution = restitution;
	},
	
	/**
	 * Get the fixture's AABB. This AABB may be enlarge and/or stale.
	 * If you need a more accurate AABB, compute it using the shape and
	 * the body transform.
	 * @return
	 */
	"public function GetAABB",function GetAABB()/*:b2AABB*/ {
		return this.m_aabb;
	},
	
	/**
	 * @private
	 */
	"public function b2Fixture",function b2Fixture$()
	{this.m_filter=this.m_filter();
		this.m_aabb = new Box2D.Collision.b2AABB();
		this.m_userData = null;
		this.m_body = null;
		this.m_next = null;
		//m_proxyId = b2BroadPhase.e_nullProxy;
		this.m_shape = null;
		this.m_density = 0.0;
		
		this.m_friction = 0.0;
		this.m_restitution = 0.0;
	},
	
	/**
	 * the destructor cannot access the allocator (no destructor arguments allowed by C++).
	 *  We need separation create/destroy functions from the constructor/destructor because
	 */
	"b2internal function Create",function Create( body/*:b2Body*/, xf/*:b2Transform*/, def/*:b2FixtureDef*/)/*:void*/
	{
		this.m_userData = def.userData;
		this.m_friction = def.friction;
		this.m_restitution = def.restitution;
		
		this.m_body = body;
		this.m_next = null;
		
		this.m_filter = def.filter.Copy();
		
		this.m_isSensor = def.isSensor;
		
		this.m_shape = def.shape.Copy();
		
		this.m_density = def.density;
	},
	
	/**
	 * the destructor cannot access the allocator (no destructor arguments allowed by C++).
	 *  We need separation create/destroy functions from the constructor/destructor because
	 */
	"b2internal function Destroy",function Destroy()/*:void*/
	{
		// The proxy must be destroyed before calling this.
		//b2Assert(m_proxyId == b2BroadPhase::e_nullProxy);
		
		// Free the child shape
		this.m_shape = null;
	},
	
	/**
	 * This supports body activation/deactivation.
	 */ 
	"b2internal function CreateProxy",function CreateProxy(broadPhase/*:IBroadPhase*/, xf/*:b2Transform*/)/*:void*/ {
		//b2Assert(m_proxyId == b2BroadPhase::e_nullProxy);
		
		// Create proxy in the broad-phase.
		this.m_shape.ComputeAABB(this.m_aabb, xf);
		this.m_proxy = broadPhase.CreateProxy(this.m_aabb, this);
	},
	
	/**
	 * This supports body activation/deactivation.
	 */
	"b2internal function DestroyProxy",function DestroyProxy(broadPhase/*:IBroadPhase*/)/*:void*/ {
		if (this.m_proxy == null)
		{
			return;
		}
		
		// Destroy proxy in the broad-phase.
		broadPhase.DestroyProxy(this.m_proxy);
		this.m_proxy = null;
	},
	
	"private static var",{ s_aabb1/*:b2AABB*/ :function(){return( new Box2D.Collision.b2AABB());}},
	"private static var",{ s_aabb2/*:b2AABB*/ :function(){return( new Box2D.Collision.b2AABB());}},
	"private static var",{ s_displacement/*:b2Vec2*/ :function(){return( new Box2D.Common.Math.b2Vec2());}},
	"b2internal function Synchronize",function Synchronize(broadPhase/*:IBroadPhase*/, transform1/*:b2Transform*/, transform2/*:b2Transform*/)/*:void*/
	{
		if (!this.m_proxy)
			return;
			
		// Compute an AABB that ocvers the swept shape (may miss some rotation effect)
		var aabb1/*:b2AABB*/ = $$private.s_aabb1;
		var aabb2/*:b2AABB*/ = $$private.s_aabb2;
		this.m_shape.ComputeAABB(aabb1, transform1);
		this.m_shape.ComputeAABB(aabb2, transform2);
		
		this.m_aabb.Combine(aabb1, aabb2);
		var displacement/*:b2Vec2*/ = $$private.s_displacement;
		displacement.x = transform2.position.x - transform1.position.x;
		displacement.y = transform2.position.y - transform1.position.y;
		broadPhase.MoveProxy(this.m_proxy, this.m_aabb, displacement);
	},

	/**
	 * Get the definition containing the fixture properties.
	 * @asonly
	 */
	"public function GetDefinition",function GetDefinition()/*:b2FixtureDef*/
	{
		var fd/*:b2FixtureDef*/ = new Box2D.Dynamics.b2FixtureDef();
		fd.density = this.m_density;
		fd.filter = this.m_filter.Copy();
		fd.friction = this.m_friction;
		fd.isSensor = this.m_isSensor;
		fd.restitution = this.m_restitution;
		fd.shape = this.m_shape;
		fd.userData = this.m_userData;
		return fd;
	},
	
	"private var",{ m_massData/*:b2MassData*/:null},
	
	"b2internal var",{ m_aabb/*:b2AABB*/:null},
	"b2internal var",{ m_density/*:Number*/:NaN},
	"b2internal var",{ m_next/*:b2Fixture*/:null},
	"b2internal var",{ m_body/*:b2Body*/:null},
	"b2internal var",{ m_shape/*:b2Shape*/:null},
	
	"b2internal var",{ m_friction/*:Number*/:NaN},
	"b2internal var",{ m_restitution/*:Number*/:NaN},
	
	"b2internal var",{ m_proxy/*:**/:undefined},
	"b2internal var",{ m_filter/*:b2FilterData*/ :function(){return( new Box2D.Dynamics.b2FilterData());}},
	
	"b2internal var",{ m_isSensor/*:Boolean*/:false},
	
	"b2internal var",{ m_userData/*:**/:undefined},
];},[],["Box2D.Collision.Shapes.b2MassData","Box2D.Collision.b2AABB","Box2D.Common.Math.b2Vec2","Box2D.Dynamics.b2FixtureDef","Box2D.Dynamics.b2FilterData"], "0.8.0", "0.8.1"



);