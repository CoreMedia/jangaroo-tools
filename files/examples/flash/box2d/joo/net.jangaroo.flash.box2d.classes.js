// class Box2D.Collision.b2AABB
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2AABB",1,function($$private){;return[function(){joo.classLoader.init(Number);},

"public function Copy",function()
{
var aabb=new Box2D.Collision.b2AABB();
aabb.Set(this);
return aabb;
},
"public function Set",function(other)
{
this.lowerBound.SetV(other.lowerBound);
this.upperBound.SetV(other.upperBound);
},
"public function IsValid",function(){
var dX=this.upperBound.x-this.lowerBound.x;
var dY=this.upperBound.y-this.lowerBound.y;
var valid=dX>=0.0&&dY>=0.0;
valid=valid&&this.lowerBound.IsValid()&&this.upperBound.IsValid();
return valid;
},
"public function GetCenter",function()
{
return new Box2D.Common.Math.b2Vec2((this.lowerBound.x+this.upperBound.x)/2,
(this.lowerBound.y+this.upperBound.y)/2);
},
"public function GetExtents",function()
{
return new Box2D.Common.Math.b2Vec2((this.upperBound.x-this.lowerBound.x)/2,
(this.upperBound.y-this.lowerBound.y)/2);
},
"public function Contains",function(aabb)
{
var result=true;
result=
result&&(this.lowerBound.x<=aabb.lowerBound.x);
result=
result&&(this.lowerBound.y<=aabb.lowerBound.y);
result=
result&&(aabb.upperBound.x<=this.upperBound.x);
result=
result&&(aabb.upperBound.y<=this.upperBound.y);
return result;
},
"public function RayCast",function(output,input)
{
var tmin=-Number.MAX_VALUE;
var tmax=Number.MAX_VALUE;
var pX=input.p1.x;
var pY=input.p1.y;
var dX=input.p2.x-input.p1.x;
var dY=input.p2.y-input.p1.y;
var absDX=Math.abs(dX);
var absDY=Math.abs(dY);
var normal=output.normal;
var inv_d;
var t1;
var t2;
var t3;
var s;
{
if(absDX<Number.MIN_VALUE)
{
if(pX<this.lowerBound.x||this.upperBound.x<pX)
return false;
}
else
{
inv_d=1.0/dX;
t1=(this.lowerBound.x-pX)*inv_d;
t2=(this.upperBound.x-pX)*inv_d;
s=-1.0;
if(t1>t2)
{
t3=t1;
t1=t2;
t2=t3;
s=1.0;
}
if(t1>tmin)
{
normal.x=s;
normal.y=0;
tmin=t1;
}
tmax=Math.min(tmax,t2);
if(tmin>tmax)
return false;
}
}
{
if(absDY<Number.MIN_VALUE)
{
if(pY<this.lowerBound.y||this.upperBound.y<pY)
return false;
}
else
{
inv_d=1.0/dY;
t1=(this.lowerBound.y-pY)*inv_d;
t2=(this.upperBound.y-pY)*inv_d;
s=-1.0;
if(t1>t2)
{
t3=t1;
t1=t2;
t2=t3;
s=1.0;
}
if(t1>tmin)
{
normal.y=s;
normal.x=0;
tmin=t1;
}
tmax=Math.min(tmax,t2);
if(tmin>tmax)
return false;
}
}
output.fraction=tmin;
return true;
},
"public function TestOverlap",function(other)
{
var d1X=other.lowerBound.x-this.upperBound.x;
var d1Y=other.lowerBound.y-this.upperBound.y;
var d2X=this.lowerBound.x-other.upperBound.x;
var d2Y=this.lowerBound.y-other.upperBound.y;
if(d1X>0.0||d1Y>0.0)
return false;
if(d2X>0.0||d2Y>0.0)
return false;
return true;
},
"public static function Combine",function(aabb1,aabb2)
{
var aabb=new Box2D.Collision.b2AABB();
aabb.Combine(aabb1,aabb2);
return aabb;
},
"public function Combine",function(aabb1,aabb2)
{
this.lowerBound.x=Math.min(aabb1.lowerBound.x,aabb2.lowerBound.x);
this.lowerBound.y=Math.min(aabb1.lowerBound.y,aabb2.lowerBound.y);
this.upperBound.x=Math.max(aabb1.upperBound.x,aabb2.upperBound.x);
this.upperBound.y=Math.max(aabb1.upperBound.y,aabb2.upperBound.y);
},
"public var",{lowerBound:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{upperBound:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public function b2AABB",function b2AABB$(){this.lowerBound=this.lowerBound();this.upperBound=this.upperBound();}];},["Combine"],["Box2D.Common.Math.b2Vec2","Number","Math"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2Collision
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2Collision",1,function($$private){;return[function(){joo.classLoader.init(Box2D.Collision.b2Manifold,Number,Box2D.Common.b2Settings);},
"static public const",{b2_nullFeature:0x000000ff},
"static public function ClipSegmentToLine",function(vOut,vIn,normal,offset)
{
var cv;
var numOut=0;
cv=vIn[0];
var vIn0=cv.v;
cv=vIn[1];
var vIn1=cv.v;
var distance0=normal.x*vIn0.x+normal.y*vIn0.y-offset;
var distance1=normal.x*vIn1.x+normal.y*vIn1.y-offset;
if(distance0<=0.0)vOut[numOut++].Set(vIn[0]);
if(distance1<=0.0)vOut[numOut++].Set(vIn[1]);
if(distance0*distance1<0.0)
{
var interp=distance0/(distance0-distance1);
cv=vOut[numOut];
var tVec=cv.v;
tVec.x=vIn0.x+interp*(vIn1.x-vIn0.x);
tVec.y=vIn0.y+interp*(vIn1.y-vIn0.y);
cv=vOut[numOut];
var cv2;
if(distance0>0.0)
{
cv2=vIn[0];
cv.id=cv2.id;
}
else
{
cv2=vIn[1];
cv.id=cv2.id;
}
++numOut;
}
return numOut;
},
"static public function EdgeSeparation",function(poly1,xf1,edge1,
poly2,xf2)
{
var count1=poly1.m_vertexCount;
var vertices1=poly1.m_vertices;
var normals1=poly1.m_normals;
var count2=poly2.m_vertexCount;
var vertices2=poly2.m_vertices;
var tMat;
var tVec;
tMat=xf1.R;
tVec=normals1[edge1];
var normal1WorldX=(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
var normal1WorldY=(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
tMat=xf2.R;
var normal1X=(tMat.col1.x*normal1WorldX+tMat.col1.y*normal1WorldY);
var normal1Y=(tMat.col2.x*normal1WorldX+tMat.col2.y*normal1WorldY);
var index=0;
var minDot=Number.MAX_VALUE;
for(var i=0;i<count2;++i)
{
tVec=vertices2[i];
var dot=tVec.x*normal1X+tVec.y*normal1Y;
if(dot<minDot)
{
minDot=dot;
index=i;
}
}
tVec=vertices1[edge1];
tMat=xf1.R;
var v1X=xf1.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
var v1Y=xf1.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
tVec=vertices2[index];
tMat=xf2.R;
var v2X=xf2.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
var v2Y=xf2.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
v2X-=v1X;
v2Y-=v1Y;
var separation=v2X*normal1WorldX+v2Y*normal1WorldY;
return separation;
},
"static public function FindMaxSeparation",function(edgeIndex,
poly1,xf1,
poly2,xf2)
{
var count1=poly1.m_vertexCount;
var normals1=poly1.m_normals;
var tVec;
var tMat;
tMat=xf2.R;
tVec=poly2.m_centroid;
var dX=xf2.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
var dY=xf2.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
tMat=xf1.R;
tVec=poly1.m_centroid;
dX-=xf1.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
dY-=xf1.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
var dLocal1X=(dX*xf1.R.col1.x+dY*xf1.R.col1.y);
var dLocal1Y=(dX*xf1.R.col2.x+dY*xf1.R.col2.y);
var edge=0;
var maxDot=-Number.MAX_VALUE;
for(var i=0;i<count1;++i)
{
tVec=normals1[i];
var dot=(tVec.x*dLocal1X+tVec.y*dLocal1Y);
if(dot>maxDot)
{
maxDot=dot;
edge=i;
}
}
var s=Box2D.Collision.b2Collision.EdgeSeparation(poly1,xf1,edge,poly2,xf2);
var prevEdge=edge-1>=0?edge-1:count1-1;
var sPrev=Box2D.Collision.b2Collision.EdgeSeparation(poly1,xf1,prevEdge,poly2,xf2);
var nextEdge=edge+1<count1?edge+1:0;
var sNext=Box2D.Collision.b2Collision.EdgeSeparation(poly1,xf1,nextEdge,poly2,xf2);
var bestEdge;
var bestSeparation;
var increment;
if(sPrev>s&&sPrev>sNext)
{
increment=-1;
bestEdge=prevEdge;
bestSeparation=sPrev;
}
else if(sNext>s)
{
increment=1;
bestEdge=nextEdge;
bestSeparation=sNext;
}
else
{
edgeIndex[0]=edge;
return s;
}
while(true)
{
if(increment==-1)
edge=bestEdge-1>=0?bestEdge-1:count1-1;
else
edge=bestEdge+1<count1?bestEdge+1:0;
s=Box2D.Collision.b2Collision.EdgeSeparation(poly1,xf1,edge,poly2,xf2);
if(s>bestSeparation)
{
bestEdge=edge;
bestSeparation=s;
}
else
{
break;
}
}
edgeIndex[0]=bestEdge;
return bestSeparation;
},
"static public function FindIncidentEdge",function(c,
poly1,xf1,edge1,
poly2,xf2)
{
var count1=poly1.m_vertexCount;
var normals1=poly1.m_normals;
var count2=poly2.m_vertexCount;
var vertices2=poly2.m_vertices;
var normals2=poly2.m_normals;
var tMat;
var tVec;
tMat=xf1.R;
tVec=normals1[edge1];
var normal1X=(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
var normal1Y=(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
tMat=xf2.R;
var tX=(tMat.col1.x*normal1X+tMat.col1.y*normal1Y);
normal1Y=(tMat.col2.x*normal1X+tMat.col2.y*normal1Y);
normal1X=tX;
var index=0;
var minDot=Number.MAX_VALUE;
for(var i=0;i<count2;++i)
{
tVec=normals2[i];
var dot=(normal1X*tVec.x+normal1Y*tVec.y);
if(dot<minDot)
{
minDot=dot;
index=i;
}
}
var tClip;
var i1=index;
var i2=i1+1<count2?i1+1:0;
tClip=c[0];
tVec=vertices2[i1];
tMat=xf2.R;
tClip.v.x=xf2.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
tClip.v.y=xf2.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
tClip.id.features.referenceEdge=edge1;
tClip.id.features.incidentEdge=i1;
tClip.id.features.incidentVertex=0;
tClip=c[1];
tVec=vertices2[i2];
tMat=xf2.R;
tClip.v.x=xf2.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
tClip.v.y=xf2.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
tClip.id.features.referenceEdge=edge1;
tClip.id.features.incidentEdge=i2;
tClip.id.features.incidentVertex=1;
},
"private static function MakeClipPointVector",function()
{
var r=new Array(2);
r[0]=new Box2D.Collision.ClipVertex();
r[1]=new Box2D.Collision.ClipVertex();
return r;
},
"private static var",{s_incidentEdge:function(){return($$private.MakeClipPointVector());}},
"private static var",{s_clipPoints1:function(){return($$private.MakeClipPointVector());}},
"private static var",{s_clipPoints2:function(){return($$private.MakeClipPointVector());}},
"private static var",{s_edgeAO:function(){return(new Array(1));}},
"private static var",{s_edgeBO:function(){return(new Array(1));}},
"private static var",{s_localTangent:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private static var",{s_localNormal:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private static var",{s_planePoint:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private static var",{s_normal:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private static var",{s_tangent:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private static var",{s_tangent2:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private static var",{s_v11:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private static var",{s_v12:function(){return(new Box2D.Common.Math.b2Vec2());}},
"static private var",{b2CollidePolyTempVec:function(){return(new Box2D.Common.Math.b2Vec2());}},
"static public function CollidePolygons",function(manifold,
polyA,xfA,
polyB,xfB)
{
var cv;
manifold.m_pointCount=0;
var totalRadius=polyA.m_radius+polyB.m_radius;
var edgeA=0;
$$private.s_edgeAO[0]=edgeA;
var separationA=Box2D.Collision.b2Collision.FindMaxSeparation($$private.s_edgeAO,polyA,xfA,polyB,xfB);
edgeA=$$private.s_edgeAO[0];
if(separationA>totalRadius)
return;
var edgeB=0;
$$private.s_edgeBO[0]=edgeB;
var separationB=Box2D.Collision.b2Collision.FindMaxSeparation($$private.s_edgeBO,polyB,xfB,polyA,xfA);
edgeB=$$private.s_edgeBO[0];
if(separationB>totalRadius)
return;
var poly1;
var poly2;
var xf1;
var xf2;
var edge1;
var flip;var k_relativeTol=0.98;var k_absoluteTol=0.001;
var tMat;
if(separationB>k_relativeTol*separationA+k_absoluteTol)
{
poly1=polyB;
poly2=polyA;
xf1=xfB;
xf2=xfA;
edge1=edgeB;
manifold.m_type=Box2D.Collision.b2Manifold.e_faceB;
flip=1;
}
else
{
poly1=polyA;
poly2=polyB;
xf1=xfA;
xf2=xfB;
edge1=edgeA;
manifold.m_type=Box2D.Collision.b2Manifold.e_faceA;
flip=0;
}
var incidentEdge=$$private.s_incidentEdge;
Box2D.Collision.b2Collision.FindIncidentEdge(incidentEdge,poly1,xf1,edge1,poly2,xf2);
var count1=poly1.m_vertexCount;
var vertices1=poly1.m_vertices;
var local_v11=vertices1[edge1];
var local_v12;
if(edge1+1<count1){
local_v12=vertices1[$$int(edge1+1)];
}else{
local_v12=vertices1[0];
}
var localTangent=$$private.s_localTangent;
localTangent.Set(local_v12.x-local_v11.x,local_v12.y-local_v11.y);
localTangent.Normalize();
var localNormal=$$private.s_localNormal;
localNormal.x=localTangent.y;
localNormal.y=-localTangent.x;
var planePoint=$$private.s_planePoint;
planePoint.Set(0.5*(local_v11.x+local_v12.x),0.5*(local_v11.y+local_v12.y));
var tangent=$$private.s_tangent;
tMat=xf1.R;
tangent.x=(tMat.col1.x*localTangent.x+tMat.col2.x*localTangent.y);
tangent.y=(tMat.col1.y*localTangent.x+tMat.col2.y*localTangent.y);
var tangent2=$$private.s_tangent2;
tangent2.x=-tangent.x;
tangent2.y=-tangent.y;
var normal=$$private.s_normal;
normal.x=tangent.y;
normal.y=-tangent.x;
var v11=$$private.s_v11;
var v12=$$private.s_v12;
v11.x=xf1.position.x+(tMat.col1.x*local_v11.x+tMat.col2.x*local_v11.y);
v11.y=xf1.position.y+(tMat.col1.y*local_v11.x+tMat.col2.y*local_v11.y);
v12.x=xf1.position.x+(tMat.col1.x*local_v12.x+tMat.col2.x*local_v12.y);
v12.y=xf1.position.y+(tMat.col1.y*local_v12.x+tMat.col2.y*local_v12.y);
var frontOffset=normal.x*v11.x+normal.y*v11.y;
var sideOffset1=-tangent.x*v11.x-tangent.y*v11.y+totalRadius;
var sideOffset2=tangent.x*v12.x+tangent.y*v12.y+totalRadius;
var clipPoints1=$$private.s_clipPoints1;
var clipPoints2=$$private.s_clipPoints2;
var np;
np=Box2D.Collision.b2Collision.ClipSegmentToLine(clipPoints1,incidentEdge,tangent2,sideOffset1);
if(np<2)
return;
np=Box2D.Collision.b2Collision.ClipSegmentToLine(clipPoints2,clipPoints1,tangent,sideOffset2);
if(np<2)
return;
manifold.m_localPlaneNormal.SetV(localNormal);
manifold.m_localPoint.SetV(planePoint);
var pointCount=0;
for(var i=0;i<Box2D.Common.b2Settings.b2_maxManifoldPoints;++i)
{
cv=clipPoints2[i];
var separation=normal.x*cv.v.x+normal.y*cv.v.y-frontOffset;
if(separation<=totalRadius)
{
var cp=manifold.m_points[pointCount];
tMat=xf2.R;
var tX=cv.v.x-xf2.position.x;
var tY=cv.v.y-xf2.position.y;
cp.m_localPoint.x=(tX*tMat.col1.x+tY*tMat.col1.y);
cp.m_localPoint.y=(tX*tMat.col2.x+tY*tMat.col2.y);
cp.m_id.Set(cv.id);
cp.m_id.features.flip=flip;
++pointCount;
}
}
manifold.m_pointCount=pointCount;
},
"static public function CollideCircles",function(
manifold,
circle1,xf1,
circle2,xf2)
{
manifold.m_pointCount=0;
var tMat;
var tVec;
tMat=xf1.R;tVec=circle1.m_p;
var p1X=xf1.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
var p1Y=xf1.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
tMat=xf2.R;tVec=circle2.m_p;
var p2X=xf2.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
var p2Y=xf2.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
var dX=p2X-p1X;
var dY=p2Y-p1Y;
var distSqr=dX*dX+dY*dY;
var radius=circle1.m_radius+circle2.m_radius;
if(distSqr>radius*radius)
{
return;
}
manifold.m_type=Box2D.Collision.b2Manifold.e_circles;
manifold.m_localPoint.SetV(circle1.m_p);
manifold.m_localPlaneNormal.SetZero();
manifold.m_pointCount=1;
manifold.m_points[0].m_localPoint.SetV(circle2.m_p);
manifold.m_points[0].m_id.key=0;
},
"static public function CollidePolygonAndCircle",function(
manifold,
polygon,xf1,
circle,xf2)
{
manifold.m_pointCount=0;
var tPoint;
var dX;
var dY;
var positionX;
var positionY;
var tVec;
var tMat;
tMat=xf2.R;
tVec=circle.m_p;
var cX=xf2.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
var cY=xf2.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
dX=cX-xf1.position.x;
dY=cY-xf1.position.y;
tMat=xf1.R;
var cLocalX=(dX*tMat.col1.x+dY*tMat.col1.y);
var cLocalY=(dX*tMat.col2.x+dY*tMat.col2.y);
var dist;
var normalIndex=0;
var separation=-Number.MAX_VALUE;
var radius=polygon.m_radius+circle.m_radius;
var vertexCount=polygon.m_vertexCount;
var vertices=polygon.m_vertices;
var normals=polygon.m_normals;
for(var i=0;i<vertexCount;++i)
{
tVec=vertices[i];
dX=cLocalX-tVec.x;
dY=cLocalY-tVec.y;
tVec=normals[i];
var s=tVec.x*dX+tVec.y*dY;
if(s>radius)
{
return;
}
if(s>separation)
{
separation=s;
normalIndex=i;
}
}
var vertIndex1=normalIndex;
var vertIndex2=vertIndex1+1<vertexCount?vertIndex1+1:0;
var v1=vertices[vertIndex1];
var v2=vertices[vertIndex2];
if(separation<Number.MIN_VALUE)
{
manifold.m_pointCount=1;
manifold.m_type=Box2D.Collision.b2Manifold.e_faceA;
manifold.m_localPlaneNormal.SetV(normals[normalIndex]);
manifold.m_localPoint.x=0.5*(v1.x+v2.x);
manifold.m_localPoint.y=0.5*(v1.y+v2.y);
manifold.m_points[0].m_localPoint.SetV(circle.m_p);
manifold.m_points[0].m_id.key=0;
return;
}
var u1=(cLocalX-v1.x)*(v2.x-v1.x)+(cLocalY-v1.y)*(v2.y-v1.y);
var u2=(cLocalX-v2.x)*(v1.x-v2.x)+(cLocalY-v2.y)*(v1.y-v2.y);
if(u1<=0.0)
{
if((cLocalX-v1.x)*(cLocalX-v1.x)+(cLocalY-v1.y)*(cLocalY-v1.y)>radius*radius)
return;
manifold.m_pointCount=1;
manifold.m_type=Box2D.Collision.b2Manifold.e_faceA;
manifold.m_localPlaneNormal.x=cLocalX-v1.x;
manifold.m_localPlaneNormal.y=cLocalY-v1.y;
manifold.m_localPlaneNormal.Normalize();
manifold.m_localPoint.SetV(v1);
manifold.m_points[0].m_localPoint.SetV(circle.m_p);
manifold.m_points[0].m_id.key=0;
}
else if(u2<=0)
{
if((cLocalX-v2.x)*(cLocalX-v2.x)+(cLocalY-v2.y)*(cLocalY-v2.y)>radius*radius)
return;
manifold.m_pointCount=1;
manifold.m_type=Box2D.Collision.b2Manifold.e_faceA;
manifold.m_localPlaneNormal.x=cLocalX-v2.x;
manifold.m_localPlaneNormal.y=cLocalY-v2.y;
manifold.m_localPlaneNormal.Normalize();
manifold.m_localPoint.SetV(v2);
manifold.m_points[0].m_localPoint.SetV(circle.m_p);
manifold.m_points[0].m_id.key=0;
}
else
{
var faceCenterX=0.5*(v1.x+v2.x);
var faceCenterY=0.5*(v1.y+v2.y);
separation=(cLocalX-faceCenterX)*normals[vertIndex1].x+(cLocalY-faceCenterY)*normals[vertIndex1].y;
if(separation>radius)
return;
manifold.m_pointCount=1;
manifold.m_type=Box2D.Collision.b2Manifold.e_faceA;
manifold.m_localPlaneNormal.x=normals[vertIndex1].x;
manifold.m_localPlaneNormal.y=normals[vertIndex1].y;
manifold.m_localPlaneNormal.Normalize();
manifold.m_localPoint.Set(faceCenterX,faceCenterY);
manifold.m_points[0].m_localPoint.SetV(circle.m_p);
manifold.m_points[0].m_id.key=0;
}
},
"static public function TestOverlap",function(a,b)
{
var t1=b.lowerBound;
var t2=a.upperBound;
var d1X=t1.x-t2.x;
var d1Y=t1.y-t2.y;
t1=a.lowerBound;
t2=b.upperBound;
var d2X=t1.x-t2.x;
var d2Y=t1.y-t2.y;
if(d1X>0.0||d1Y>0.0)
return false;
if(d2X>0.0||d2Y>0.0)
return false;
return true;
},
];},["ClipSegmentToLine","EdgeSeparation","FindMaxSeparation","FindIncidentEdge","CollidePolygons","CollideCircles","CollidePolygonAndCircle","TestOverlap"],["Number","Array","Box2D.Collision.ClipVertex","Box2D.Common.Math.b2Vec2","Box2D.Collision.b2Manifold","int","Box2D.Common.b2Settings"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2ContactID
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2ContactID",1,function($$private){;return[

"public function b2ContactID",function(){this.features=this.features();
this.features._m_id=this;
},
"public function Set",function(id){
this.key=id._key;
},
"public function Copy",function(){
var id=new Box2D.Collision.b2ContactID();
id.key=this.key;
return id;
},
"public function get key",function(){
return this._key;
},
"public function set key",function(value){
this._key=value;
this.features._referenceEdge=this._key&0x000000ff;
this.features._incidentEdge=((this._key&0x0000ff00)>>8)&0x000000ff;
this.features._incidentVertex=((this._key&0x00ff0000)>>16)&0x000000ff;
this.features._flip=((this._key&0xff000000)>>24)&0x000000ff;
},
"public var",{features:function(){return(new Box2D.Collision.Features());}},
"b2internal var",{_key:0},
];},[],["Box2D.Collision.Features"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2ContactPoint
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2ContactPoint",1,function($$private){;return[

"public var",{shape1:null},
"public var",{shape2:null},
"public var",{position:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{velocity:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{normal:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{separation:NaN},
"public var",{friction:NaN},
"public var",{restitution:NaN},
"public var",{id:function(){return(new Box2D.Collision.b2ContactID());}},
"public function b2ContactPoint",function b2ContactPoint$(){this.position=this.position();this.velocity=this.velocity();this.normal=this.normal();this.id=this.id();}];},[],["Box2D.Common.Math.b2Vec2","Box2D.Collision.b2ContactID"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2Distance
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2Distance",1,function($$private){var assert=joo.assert;return[function(){joo.classLoader.init(Number);},

"private static var",{b2_gjkCalls:0},
"private static var",{b2_gjkIters:0},
"private static var",{b2_gjkMaxIters:0},
"private static var",{s_simplex:function(){return(new Box2D.Collision.b2Simplex());}},
"private static var",{s_saveA:function(){return(new Array(3));}},
"private static var",{s_saveB:function(){return(new Array(3));}},
"public static function Distance",function(output,cache,input)
{
++$$private.b2_gjkCalls;
var proxyA=input.proxyA;
var proxyB=input.proxyB;
var transformA=input.transformA;
var transformB=input.transformB;
var simplex=$$private.s_simplex;
simplex.ReadCache(cache,proxyA,transformA,proxyB,transformB);
var vertices=simplex.m_vertices;var k_maxIters=20;
var saveA=$$private.s_saveA;
var saveB=$$private.s_saveB;
var saveCount=0;
var closestPoint=simplex.GetClosestPoint();
var distanceSqr1=closestPoint.LengthSquared();
var distanceSqr2=distanceSqr1;
var i;
var p;
var iter=0;
while(iter<k_maxIters)
{
saveCount=simplex.m_count;
for(i=0;i<saveCount;i++)
{
saveA[i]=vertices[i].indexA;
saveB[i]=vertices[i].indexB;
}
switch(simplex.m_count)
{
case 1:
break;
case 2:
simplex.Solve2();
break;
case 3:
simplex.Solve3();
break;
default:
}
if(simplex.m_count==3)
{
break;
}
p=simplex.GetClosestPoint();
distanceSqr2=p.LengthSquared();
if(distanceSqr2>distanceSqr1)
{
}
distanceSqr1=distanceSqr2;
var d=simplex.GetSearchDirection();
if(d.LengthSquared()<Number.MIN_VALUE*Number.MIN_VALUE)
{
break;
}
var vertex=vertices[simplex.m_count];
vertex.indexA=proxyA.GetSupport(Box2D.Common.Math.b2Math.MulTMV(transformA.R,d.GetNegative()));
vertex.wA=Box2D.Common.Math.b2Math.MulX(transformA,proxyA.GetVertex(vertex.indexA));
vertex.indexB=proxyB.GetSupport(Box2D.Common.Math.b2Math.MulTMV(transformB.R,d));
vertex.wB=Box2D.Common.Math.b2Math.MulX(transformB,proxyB.GetVertex(vertex.indexB));
vertex.w=Box2D.Common.Math.b2Math.SubtractVV(vertex.wB,vertex.wA);
++iter;
++$$private.b2_gjkIters;
var duplicate=false;
for(i=0;i<saveCount;i++)
{
if(vertex.indexA==saveA[i]&&vertex.indexB==saveB[i])
{
duplicate=true;
break;
}
}
if(duplicate)
{
break;
}
++simplex.m_count;
}
$$private.b2_gjkMaxIters=Box2D.Common.Math.b2Math.Max($$private.b2_gjkMaxIters,iter);
simplex.GetWitnessPoints(output.pointA,output.pointB);
output.distance=Box2D.Common.Math.b2Math.SubtractVV(output.pointA,output.pointB).Length();
output.iterations=iter;
simplex.WriteCache(cache);
if(input.useRadii)
{
var rA=proxyA.m_radius;
var rB=proxyB.m_radius;
if(output.distance>rA+rB&&output.distance>Number.MIN_VALUE)
{
output.distance-=rA+rB;
var normal=Box2D.Common.Math.b2Math.SubtractVV(output.pointB,output.pointA);
normal.Normalize();
output.pointA.x+=rA*normal.x;
output.pointA.y+=rA*normal.y;
output.pointB.x-=rB*normal.x;
output.pointB.y-=rB*normal.y;
}
else
{
p=new Box2D.Common.Math.b2Vec2();
p.x=.5*(output.pointA.x+output.pointB.x);
p.y=.5*(output.pointA.y+output.pointB.y);
output.pointA.x=output.pointB.x=p.x;
output.pointA.y=output.pointB.y=p.y;
output.distance=0.0;
}
}
},
];},["Distance"],["Box2D.Collision.b2Simplex","Array","Number","Box2D.Common.Math.b2Math","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2DistanceInput
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2DistanceInput",1,function($$private){;return[

"public var",{proxyA:null},
"public var",{proxyB:null},
"public var",{transformA:null},
"public var",{transformB:null},
"public var",{useRadii:false},
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2DistanceOutput
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2DistanceOutput",1,function($$private){;return[

"public var",{pointA:function(){return(new Box2D.Common.Math.b2Vec2());}},"public var",{pointB:function(){return(new Box2D.Common.Math.b2Vec2());}},"public var",{distance:NaN},
"public var",{iterations:0},"public function b2DistanceOutput",function b2DistanceOutput$(){this.pointA=this.pointA();this.pointB=this.pointB();}];},[],["Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2DistanceProxy
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2DistanceProxy",1,function($$private){var as=joo.as,assert=joo.assert;return[function(){joo.classLoader.init(Box2D.Collision.Shapes.b2Shape);},

"public function Set",function(shape)
{
switch(shape.GetType())
{
case Box2D.Collision.Shapes.b2Shape.e_circleShape:
{
var circle=as(shape,Box2D.Collision.Shapes.b2CircleShape);
this.m_vertices=new Array(1,true);
this.m_vertices[0]=circle.m_p;
this.m_count=1;
this.m_radius=circle.m_radius;
}
break;
case Box2D.Collision.Shapes.b2Shape.e_polygonShape:
{
var polygon=as(shape,Box2D.Collision.Shapes.b2PolygonShape);
this.m_vertices=polygon.m_vertices;
this.m_count=polygon.m_vertexCount;
this.m_radius=polygon.m_radius;
}
break;
default:
}
},
"public function GetSupport",function(d)
{
var bestIndex=0;
var bestValue=this.m_vertices[0].x*d.x+this.m_vertices[0].y*d.y;
for(var i=1;i<this.m_count;++i)
{
var value=this.m_vertices[i].x*d.x+this.m_vertices[i].y*d.y;
if(value>bestValue)
{
bestIndex=i;
bestValue=value;
}
}
return bestIndex;
},
"public function GetSupportVertex",function(d)
{
var bestIndex=0;
var bestValue=this.m_vertices[0].x*d.x+this.m_vertices[0].y*d.y;
for(var i=1;i<this.m_count;++i)
{
var value=this.m_vertices[i].x*d.x+this.m_vertices[i].y*d.y;
if(value>bestValue)
{
bestIndex=i;
bestValue=value;
}
}
return this.m_vertices[bestIndex];
},
"public function GetVertexCount",function()
{
return this.m_count;
},
"public function GetVertex",function(index)
{
return this.m_vertices[index];
},
"public var",{m_vertices:null},
"public var",{m_count:0},
"public var",{m_radius:NaN},
];},[],["Box2D.Collision.Shapes.b2Shape","Box2D.Collision.Shapes.b2CircleShape","Array","Box2D.Collision.Shapes.b2PolygonShape"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2DynamicTree
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2DynamicTree",1,function($$private){var assert=joo.assert;return[function(){joo.classLoader.init(Box2D.Common.b2Settings);},

"public function b2DynamicTree",function()
{
this.m_root$1=null;
this.m_freeList$1=null;
this.m_path$1=0;
this.m_insertionCount$1=0;
},
"public function CreateProxy",function(aabb,userData)
{
var node=this.AllocateNode$1();
var extendX=Box2D.Common.b2Settings.b2_aabbExtension;
var extendY=Box2D.Common.b2Settings.b2_aabbExtension;
node.aabb.lowerBound.x=aabb.lowerBound.x-extendX;
node.aabb.lowerBound.y=aabb.lowerBound.y-extendY;
node.aabb.upperBound.x=aabb.upperBound.x+extendX;
node.aabb.upperBound.y=aabb.upperBound.y+extendY;
node.userData=userData;
this.InsertLeaf$1(node);
var iterationCount=this.m_nodeCount$1>>4;
var tryCount=0;
var height=this.ComputeFullHeight$1();
while(height>64&&tryCount<10)
{
this.Rebalance(iterationCount);
height=this.ComputeFullHeight$1();
++tryCount;
}
return node;
},
"public function DestroyProxy",function(proxy)
{
this.RemoveLeaf$1(proxy);
this.FreeNode$1(proxy);
},
"public function MoveProxy",function(proxy,aabb,displacement)
{
if(proxy.aabb.Contains(aabb))
{
return false;
}
this.RemoveLeaf$1(proxy);
var extendX=Box2D.Common.b2Settings.b2_aabbExtension+Box2D.Common.b2Settings.b2_aabbMultiplier*(displacement.x>0?displacement.x:-displacement.x);
var extendY=Box2D.Common.b2Settings.b2_aabbExtension+Box2D.Common.b2Settings.b2_aabbMultiplier*(displacement.y>0?displacement.y:-displacement.y);
proxy.aabb.lowerBound.x=aabb.lowerBound.x-extendX;
proxy.aabb.lowerBound.y=aabb.lowerBound.y-extendY;
proxy.aabb.upperBound.x=aabb.upperBound.x+extendX;
proxy.aabb.upperBound.y=aabb.upperBound.y+extendY;
this.InsertLeaf$1(proxy);
return true;
},
"public function Rebalance",function(iterations)
{
if(this.m_root$1==null)
return;
for(var i=0;i<iterations;i++)
{
var node=this.m_root$1;
var bit=0;
while(node.IsLeaf()==false)
{
node=(this.m_path$1>>bit)&1?node.child2:node.child1;
bit=(bit+1)&31;
}
++this.m_path$1;
this.RemoveLeaf$1(node);
this.InsertLeaf$1(node);
}
},
"public function GetFatAABB",function(proxy)
{
return proxy.aabb;
},
"public function GetUserData",function(proxy)
{
return proxy.userData;
},
"public function Query",function(callback,aabb)
{
this.QueryStack$1(callback,aabb,new Array());
},
"private static var",{s_stack:function(){return(new Array());}},
"public function QueryNonRecursive",function(callback,aabb)
{
this.QueryStack$1(callback,aabb,$$private.s_stack);
},
"private function QueryStack",function(callback,aabb,stack)
{
if(this.m_root$1==null)
return;
var count=0;
stack[count++]=this.m_root$1;
while(count>0)
{
var node=stack[--count];
if(node.aabb.TestOverlap(aabb))
{
if(node.IsLeaf())
{
var proceed=callback(node);
if(!proceed)
return;
}
else
{
stack[count++]=node.child1;
stack[count++]=node.child2;
}
}
}
},
"public function RayCast",function(callback,input)
{
if(this.m_root$1==null)
return;
var p1=input.p1;
var p2=input.p2;
var r=Box2D.Common.Math.b2Math.SubtractVV(p1,p2);
r.Normalize();
var v=Box2D.Common.Math.b2Math.CrossFV(1.0,r);
var abs_v=Box2D.Common.Math.b2Math.AbsV(v);
var maxFraction=input.maxFraction;
var segmentAABB=new Box2D.Collision.b2AABB();
var tX;
var tY;
{
tX=p1.x+maxFraction*(p2.x-p1.x);
tY=p1.y+maxFraction*(p2.y-p1.y);
segmentAABB.lowerBound.x=Math.min(p1.x,tX);
segmentAABB.lowerBound.y=Math.min(p1.y,tY);
segmentAABB.upperBound.x=Math.max(p1.x,tX);
segmentAABB.upperBound.y=Math.max(p1.y,tY);
}
var stack=new Array();
var count=0;
stack[count++]=this.m_root$1;
while(count>0)
{
var node=stack[--count];
if(node.aabb.TestOverlap(segmentAABB)==false)
{
continue;
}
var c=node.aabb.GetCenter();
var h=node.aabb.GetExtents();
var separation=Math.abs(v.x*(p1.x-c.x)+v.y*(p1.y-c.y))
-abs_v.x*h.x-abs_v.y*h.y;
if(separation>0.0)
continue;
if(node.IsLeaf())
{
var subInput=new Box2D.Collision.b2RayCastInput();
subInput.p1=input.p1;
subInput.p2=input.p2;
subInput.maxFraction=input.maxFraction;
var value=callback(subInput,node);
if(value==0.0)
{
return;
}
if(value>0.0)
{
maxFraction=value;
{
tX=p1.x+maxFraction*(p2.x-p1.x);
tY=p1.y+maxFraction*(p2.y-p1.y);
segmentAABB.lowerBound.x=Math.min(p1.x,tX);
segmentAABB.lowerBound.y=Math.min(p1.y,tY);
segmentAABB.upperBound.x=Math.max(p1.x,tX);
segmentAABB.upperBound.y=Math.max(p1.y,tY);
}
}
}
else
{
stack[count++]=node.child1;
stack[count++]=node.child2;
}
}
},
"private function AllocateNode",function()
{
this.m_nodeCount$1++;
if(this.m_freeList$1)
{
var node=this.m_freeList$1;
this.m_freeList$1=node.parent;
node.parent=null;
node.child1=null;
node.child2=null;
return node;
}
return new Box2D.Collision.b2DynamicTreeNode();
},
"private function FreeNode",function(node)
{
this.m_nodeCount$1--;
node.parent=this.m_freeList$1;
this.m_freeList$1=node;
},
"private function InsertLeaf",function(leaf)
{
++this.m_insertionCount$1;
if(this.m_root$1==null)
{
this.m_root$1=leaf;
this.m_root$1.parent=null;
return;
}
var center=leaf.aabb.GetCenter();
var sibling=this.m_root$1;
if(sibling.IsLeaf()==false)
{
do
{
var child1=sibling.child1;
var child2=sibling.child2;
var norm1=Math.abs((child1.aabb.lowerBound.x+child1.aabb.upperBound.x)/2-center.x)
+Math.abs((child1.aabb.lowerBound.y+child1.aabb.upperBound.y)/2-center.y);
var norm2=Math.abs((child2.aabb.lowerBound.x+child2.aabb.upperBound.x)/2-center.x)
+Math.abs((child2.aabb.lowerBound.y+child2.aabb.upperBound.y)/2-center.y);
if(norm1<norm2)
{
sibling=child1;
}else{
sibling=child2;
}
}
while(sibling.IsLeaf()==false);
}
var node1=sibling.parent;
var node2=this.AllocateNode$1();
node2.parent=node1;
node2.userData=null;
node2.aabb.Combine(leaf.aabb,sibling.aabb);
if(node1)
{
if(sibling.parent.child1==sibling)
{
node1.child1=node2;
}
else
{
node1.child2=node2;
}
node2.child1=sibling;
node2.child2=leaf;
sibling.parent=node2;
leaf.parent=node2;
do
{
if(node1.aabb.Contains(node2.aabb))
break;
node1.aabb.Combine(node1.child1.aabb,node1.child2.aabb);
node2=node1;
node1=node1.parent;
}
while(node1);
}
else
{
node2.child1=sibling;
node2.child2=leaf;
sibling.parent=node2;
leaf.parent=node2;
this.m_root$1=node2;
}
},
"private function RemoveLeaf",function(leaf)
{
if(leaf==this.m_root$1)
{
this.m_root$1=null;
return;
}
var node2=leaf.parent;
var node1=node2.parent;
var sibling;
if(node2.child1==leaf)
{
sibling=node2.child2;
}
else
{
sibling=node2.child1;
}
if(node1)
{
if(node1.child1==node2)
{
node1.child1=sibling;
}
else
{
node1.child2=sibling;
}
sibling.parent=node1;
this.FreeNode$1(node2);
while(node1)
{
var oldAABB=node1.aabb;
node1.aabb=Box2D.Collision.b2AABB.Combine(node1.child1.aabb,node1.child2.aabb);
if(oldAABB.Contains(node1.aabb))
break;
node1=node1.parent;
}
}
else
{
this.m_root$1=sibling;
sibling.parent=null;
this.FreeNode$1(node2);
}
},
"private function ComputeHeight",function(node)
{
if(node==null)
return 0;
return 1+Box2D.Common.Math.b2Math.Max(
this.ComputeHeight$1(node.child1),
this.ComputeHeight$1(node.child2));
},
"private function ComputeFullHeight",function()
{
return this.ComputeHeight$1(this.m_root$1);
},
"private var",{m_nodeCount:0},
"private var",{m_root:null},
"private var",{m_freeList:null},
"private var",{m_path:0},
"private var",{m_insertionCount:0},
];},[],["Box2D.Common.b2Settings","Array","Box2D.Common.Math.b2Math","Box2D.Collision.b2AABB","Math","Box2D.Collision.b2RayCastInput","Box2D.Collision.b2DynamicTreeNode"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2DynamicTreeBroadPhase
joo.classLoader.prepare("package Box2D.Collision",
"public class b2DynamicTreeBroadPhase implements Box2D.Collision.IBroadPhase",1,function($$private){;return[

"public function CreateProxy",function(aabb,userData)
{
var proxy=this.m_tree$1.CreateProxy(aabb,userData);
++this.m_proxyCount$1;
this.BufferMove$1(proxy);
return proxy;
},
"public function DestroyProxy",function(proxy)
{
this.UnBufferMove$1(proxy);
--this.m_proxyCount$1;
this.m_tree$1.DestroyProxy(proxy);
},
"public function MoveProxy",function(proxy,aabb,displacement)
{
var buffer=this.m_tree$1.MoveProxy(proxy,aabb,displacement);
if(buffer)
{
this.BufferMove$1(proxy);
}
},
"public function TestOverlap",function(proxyA,proxyB)
{
var aabbA=this.m_tree$1.GetFatAABB(proxyA);
var aabbB=this.m_tree$1.GetFatAABB(proxyB);
return aabbA.TestOverlap(aabbB);
},
"public function GetUserData",function(proxy)
{
return this.m_tree$1.GetUserData(proxy);
},
"public function GetFatAABB",function(proxy)
{
return this.m_tree$1.GetFatAABB(proxy);
},
"public function GetProxyCount",function()
{
return this.m_proxyCount$1;
},
"public function UpdatePairs",function(callback)
{var this$=this;
this.m_pairCount$1=0;
for(var $1 in this.m_moveBuffer$1)
{var queryProxy=this.m_moveBuffer$1[$1];
function QueryCallback(proxy)
{
if(proxy==queryProxy)
return true;
if(this$.m_pairCount$1==this$.m_pairBuffer$1.length)
{
this$.m_pairBuffer$1[this$.m_pairCount$1]=new Box2D.Collision.b2DynamicTreePair();
}
var pair=this$.m_pairBuffer$1[this$.m_pairCount$1];
pair.proxyA=proxy<queryProxy?proxy:queryProxy;
pair.proxyB=proxy>=queryProxy?proxy:queryProxy;
++this$.m_pairCount$1;
return true;
}
var fatAABB=this.m_tree$1.GetFatAABB(queryProxy);
this.m_tree$1.QueryNonRecursive(QueryCallback,fatAABB);
}
this.m_moveBuffer$1.length=0;
for(var i=0;i<this.m_pairCount$1;)
{
var primaryPair=this.m_pairBuffer$1[i];
var userDataA=this.m_tree$1.GetUserData(primaryPair.proxyA);
var userDataB=this.m_tree$1.GetUserData(primaryPair.proxyB);
callback(userDataA,userDataB);
++i;
while(i<this.m_pairCount$1)
{
var pair=this.m_pairBuffer$1[i];
if(pair.proxyA!=primaryPair.proxyA||pair.proxyB!=primaryPair.proxyB)
{
break;
}
++i;
}
}
this.m_tree$1.Rebalance(4);
},
"public function Query",function(callback,aabb)
{
this.m_tree$1.Query(callback,aabb);
},
"public function RayCast",function(callback,input)
{
this.m_tree$1.RayCast(callback,input);
},
"public function Validate",function()
{
},
"public function Rebalance",function(iterations)
{
this.m_tree$1.Rebalance(iterations);
},
"private function BufferMove",function(proxy)
{
this.m_moveBuffer$1[this.m_moveBuffer$1.length]=proxy;
},
"private function UnBufferMove",function(proxy)
{
var i=this.m_moveBuffer$1.indexOf(proxy);
this.m_moveBuffer$1.splice(i,1);
},
"private function ComparePairs",function(pair1,pair2)
{
return 0;
},
"private var",{m_tree:function(){return(new Box2D.Collision.b2DynamicTree());}},
"private var",{m_proxyCount:0},
"private var",{m_moveBuffer:function(){return(new Array());}},
"private var",{m_pairBuffer:function(){return(new Array());}},
"private var",{m_pairCount:0},
"public function b2DynamicTreeBroadPhase",function b2DynamicTreeBroadPhase$(){this.m_tree$1=this.m_tree$1();this.m_moveBuffer$1=this.m_moveBuffer$1();this.m_pairBuffer$1=this.m_pairBuffer$1();}];},[],["Box2D.Collision.IBroadPhase","Box2D.Collision.b2DynamicTreePair","Box2D.Collision.b2DynamicTree","Array"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2DynamicTreeNode
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2DynamicTreeNode",1,function($$private){;return[

"public function IsLeaf",function()
{
return this.child1==null;
},
"public var",{userData:undefined},
"public var",{aabb:function(){return(new Box2D.Collision.b2AABB());}},
"public var",{parent:null},
"public var",{child1:null},
"public var",{child2:null},
"public function b2DynamicTreeNode",function b2DynamicTreeNode$(){this.aabb=this.aabb();}];},[],["Box2D.Collision.b2AABB"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2DynamicTreePair
joo.classLoader.prepare("package Box2D.Collision",
"public class b2DynamicTreePair",1,function($$private){;return[

"public var",{proxyA:null},
"public var",{proxyB:null},
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2Manifold
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2Manifold",1,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Box2D.Common.b2Settings);},

"public function b2Manifold",function(){
this.m_points=new Array(Box2D.Common.b2Settings.b2_maxManifoldPoints);
for(var i=0;i<Box2D.Common.b2Settings.b2_maxManifoldPoints;i++){
this.m_points[i]=new Box2D.Collision.b2ManifoldPoint();
}
this.m_localPlaneNormal=new Box2D.Common.Math.b2Vec2();
this.m_localPoint=new Box2D.Common.Math.b2Vec2();
},
"public function Reset",function(){
for(var i=0;i<Box2D.Common.b2Settings.b2_maxManifoldPoints;i++){
(as(this.m_points[i],Box2D.Collision.b2ManifoldPoint)).Reset();
}
this.m_localPlaneNormal.SetZero();
this.m_localPoint.SetZero();
this.m_type=0;
this.m_pointCount=0;
},
"public function Set",function(m){
this.m_pointCount=m.m_pointCount;
for(var i=0;i<Box2D.Common.b2Settings.b2_maxManifoldPoints;i++){
(as(this.m_points[i],Box2D.Collision.b2ManifoldPoint)).Set(m.m_points[i]);
}
this.m_localPlaneNormal.SetV(m.m_localPlaneNormal);
this.m_localPoint.SetV(m.m_localPoint);
this.m_type=m.m_type;
},
"public function Copy",function()
{
var copy=new Box2D.Collision.b2Manifold();
copy.Set(this);
return copy;
},
"public var",{m_points:null},
"public var",{m_localPlaneNormal:null},
"public var",{m_localPoint:null},
"public var",{m_type:0},
"public var",{m_pointCount:0},
"public static const",{e_circles:0x0001},
"public static const",{e_faceA:0x0002},
"public static const",{e_faceB:0x0004},
];},[],["Array","Box2D.Common.b2Settings","Box2D.Collision.b2ManifoldPoint","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2ManifoldPoint
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2ManifoldPoint",1,function($$private){;return[

"public function b2ManifoldPoint",function()
{this.m_localPoint=this.m_localPoint();this.m_id=this.m_id();
this.Reset();
},
"public function Reset",function(){
this.m_localPoint.SetZero();
this.m_normalImpulse=0.0;
this.m_tangentImpulse=0.0;
this.m_id.key=0;
},
"public function Set",function(m){
this.m_localPoint.SetV(m.m_localPoint);
this.m_normalImpulse=m.m_normalImpulse;
this.m_tangentImpulse=m.m_tangentImpulse;
this.m_id.Set(m.m_id);
},
"public var",{m_localPoint:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{m_normalImpulse:NaN},
"public var",{m_tangentImpulse:NaN},
"public var",{m_id:function(){return(new Box2D.Collision.b2ContactID());}},
];},[],["Box2D.Common.Math.b2Vec2","Box2D.Collision.b2ContactID"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2OBB
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2OBB",1,function($$private){;return[

"public var",{R:function(){return(new Box2D.Common.Math.b2Mat22());}},
"public var",{center:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{extents:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public function b2OBB",function b2OBB$(){this.R=this.R();this.center=this.center();this.extents=this.extents();}];},[],["Box2D.Common.Math.b2Mat22","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2Point
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2Point",1,function($$private){;return[

"public function Support",function(xf,vX,vY)
{
return this.p;
},
"public function GetFirstVertex",function(xf)
{
return this.p;
},
"public var",{p:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public function b2Point",function b2Point$(){this.p=this.p();}];},[],["Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2RayCastInput
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2RayCastInput",1,function($$private){;return[

"function b2RayCastInput",function(p1,p2,maxFraction)
{if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){p1=null;}p2=null;}maxFraction=1;}this.p1=this.p1();this.p2=this.p2();
if(p1)
this.p1.SetV(p1);
if(p2)
this.p2.SetV(p2);
this.maxFraction=maxFraction;
},
"public var",{p1:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{p2:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{maxFraction:NaN},
];},[],["Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2RayCastOutput
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2RayCastOutput",1,function($$private){;return[

"public function Set",function(other)
{
this.normal.SetV(other.normal);
this.fraction=other.fraction;
},
"public var",{normal:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{fraction:NaN},
"public function b2RayCastOutput",function b2RayCastOutput$(){this.normal=this.normal();}];},[],["Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2SeparationFunction
joo.classLoader.prepare(
"package Box2D.Collision",
"internal class b2SeparationFunction",1,function($$private){var assert=joo.assert;return[

"public static const",{e_points:0x01},
"public static const",{e_faceA:0x02},
"public static const",{e_faceB:0x04},
"public function Initialize",function(cache,
proxyA,transformA,
proxyB,transformB)
{
this.m_proxyA=proxyA;
this.m_proxyB=proxyB;
var count=cache.count;
var localPointA;
var localPointA1;
var localPointA2;
var localPointB;
var localPointB1;
var localPointB2;
var pointAX;
var pointAY;
var pointBX;
var pointBY;
var normalX;
var normalY;
var tMat;
var tVec;
var s;
var sgn;
if(count==1)
{
this.m_type=Box2D.Collision.b2SeparationFunction.e_points;
localPointA=this.m_proxyA.GetVertex(cache.indexA[0]);
localPointB=this.m_proxyB.GetVertex(cache.indexB[0]);
tVec=localPointA;
tMat=transformA.R;
pointAX=transformA.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
pointAY=transformA.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
tVec=localPointB;
tMat=transformB.R;
pointBX=transformB.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
pointBY=transformB.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
this.m_axis.x=pointBX-pointAX;
this.m_axis.y=pointBY-pointAY;
this.m_axis.Normalize();
}
else if(cache.indexB[0]==cache.indexB[1])
{
this.m_type=Box2D.Collision.b2SeparationFunction.e_faceA;
localPointA1=this.m_proxyA.GetVertex(cache.indexA[0]);
localPointA2=this.m_proxyA.GetVertex(cache.indexA[1]);
localPointB=this.m_proxyB.GetVertex(cache.indexB[0]);
this.m_localPoint.x=0.5*(localPointA1.x+localPointA2.x);
this.m_localPoint.y=0.5*(localPointA1.y+localPointA2.y);
this.m_axis=Box2D.Common.Math.b2Math.CrossVF(Box2D.Common.Math.b2Math.SubtractVV(localPointA2,localPointA1),1.0);
this.m_axis.Normalize();
tVec=this.m_axis;
tMat=transformA.R;
normalX=tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
normalY=tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
tVec=this.m_localPoint;
tMat=transformA.R;
pointAX=transformA.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
pointAY=transformA.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
tVec=localPointB;
tMat=transformB.R;
pointBX=transformB.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
pointBY=transformB.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
s=(pointBX-pointAX)*normalX+(pointBY-pointAY)*normalY;
if(s<0.0)
{
this.m_axis.NegativeSelf();
}
}
else if(cache.indexA[0]==cache.indexA[1])
{
this.m_type=Box2D.Collision.b2SeparationFunction.e_faceB;
localPointB1=this.m_proxyB.GetVertex(cache.indexB[0]);
localPointB2=this.m_proxyB.GetVertex(cache.indexB[1]);
localPointA=this.m_proxyA.GetVertex(cache.indexA[0]);
this.m_localPoint.x=0.5*(localPointB1.x+localPointB2.x);
this.m_localPoint.y=0.5*(localPointB1.y+localPointB2.y);
this.m_axis=Box2D.Common.Math.b2Math.CrossVF(Box2D.Common.Math.b2Math.SubtractVV(localPointB2,localPointB1),1.0);
this.m_axis.Normalize();
tVec=this.m_axis;
tMat=transformB.R;
normalX=tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
normalY=tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
tVec=this.m_localPoint;
tMat=transformB.R;
pointBX=transformB.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
pointBY=transformB.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
tVec=localPointA;
tMat=transformA.R;
pointAX=transformA.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
pointAY=transformA.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
s=(pointAX-pointBX)*normalX+(pointAY-pointBY)*normalY;
if(s<0.0)
{
this.m_axis.NegativeSelf();
}
}
else
{
localPointA1=this.m_proxyA.GetVertex(cache.indexA[0]);
localPointA2=this.m_proxyA.GetVertex(cache.indexA[1]);
localPointB1=this.m_proxyB.GetVertex(cache.indexB[0]);
localPointB2=this.m_proxyB.GetVertex(cache.indexB[1]);
var pA=Box2D.Common.Math.b2Math.MulX(transformA,localPointA1);
var dA=Box2D.Common.Math.b2Math.MulMV(transformA.R,Box2D.Common.Math.b2Math.SubtractVV(localPointA2,localPointA1));
var pB=Box2D.Common.Math.b2Math.MulX(transformB,localPointB1);
var dB=Box2D.Common.Math.b2Math.MulMV(transformB.R,Box2D.Common.Math.b2Math.SubtractVV(localPointB2,localPointB1));
var a=dA.x*dA.x+dA.y*dA.y;
var e=dB.x*dB.x+dB.y*dB.y;
var r=Box2D.Common.Math.b2Math.SubtractVV(pA,pB);
var c=dA.x*r.x+dA.y*r.y;
var f=dB.x*r.x+dB.y*r.y;
var b=dA.x*dB.x+dA.y*dB.y;
var denom=a*e-b*b;
s=0.0;
if(denom!=0.0)
{
s=Box2D.Common.Math.b2Math.Clamp((b*f-c*e)/denom,0.0,1.0);
}
var t=(b*s+f)/e;
if(t<0.0)
{
t=0.0;
s=Box2D.Common.Math.b2Math.Clamp((b-c)/a,0.0,1.0);
}
localPointA=new Box2D.Common.Math.b2Vec2();
localPointA.x=localPointA1.x+s*(localPointA2.x-localPointA1.x);
localPointA.y=localPointA1.y+s*(localPointA2.y-localPointA1.y);
localPointB=new Box2D.Common.Math.b2Vec2();
localPointB.x=localPointB1.x+s*(localPointB2.x-localPointB1.x);
localPointB.y=localPointB1.y+s*(localPointB2.y-localPointB1.y);
if(s==0.0||s==1.0)
{
this.m_type=Box2D.Collision.b2SeparationFunction.e_faceB;
this.m_axis=Box2D.Common.Math.b2Math.CrossVF(Box2D.Common.Math.b2Math.SubtractVV(localPointB2,localPointB1),1.0);
this.m_axis.Normalize();
this.m_localPoint=localPointB;
tVec=this.m_axis;
tMat=transformB.R;
normalX=tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
normalY=tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
tVec=this.m_localPoint;
tMat=transformB.R;
pointBX=transformB.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
pointBY=transformB.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
tVec=localPointA;
tMat=transformA.R;
pointAX=transformA.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
pointAY=transformA.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
sgn=(pointAX-pointBX)*normalX+(pointAY-pointBY)*normalY;
if(s<0.0)
{
this.m_axis.NegativeSelf();
}
}
else
{
this.m_type=Box2D.Collision.b2SeparationFunction.e_faceA;
this.m_axis=Box2D.Common.Math.b2Math.CrossVF(Box2D.Common.Math.b2Math.SubtractVV(localPointA2,localPointA1),1.0);
this.m_axis.Normalize();
this.m_localPoint=localPointA;
tVec=this.m_axis;
tMat=transformA.R;
normalX=tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
normalY=tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
tVec=this.m_localPoint;
tMat=transformA.R;
pointAX=transformA.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
pointAY=transformA.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
tVec=localPointB;
tMat=transformB.R;
pointBX=transformB.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
pointBY=transformB.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
sgn=(pointBX-pointAX)*normalX+(pointBY-pointAY)*normalY;
if(s<0.0)
{
this.m_axis.NegativeSelf();
}
}
}
},
"public function Evaluate",function(transformA,transformB)
{
var axisA;
var axisB;
var localPointA;
var localPointB;
var pointA;
var pointB;
var seperation;
var normal;
switch(this.m_type)
{
case Box2D.Collision.b2SeparationFunction.e_points:
{
axisA=Box2D.Common.Math.b2Math.MulTMV(transformA.R,this.m_axis);
axisB=Box2D.Common.Math.b2Math.MulTMV(transformB.R,this.m_axis.GetNegative());
localPointA=this.m_proxyA.GetSupportVertex(axisA);
localPointB=this.m_proxyB.GetSupportVertex(axisB);
pointA=Box2D.Common.Math.b2Math.MulX(transformA,localPointA);
pointB=Box2D.Common.Math.b2Math.MulX(transformB,localPointB);
seperation=(pointB.x-pointA.x)*this.m_axis.x+(pointB.y-pointA.y)*this.m_axis.y;
return seperation;
}
case Box2D.Collision.b2SeparationFunction.e_faceA:
{
normal=Box2D.Common.Math.b2Math.MulMV(transformA.R,this.m_axis);
pointA=Box2D.Common.Math.b2Math.MulX(transformA,this.m_localPoint);
axisB=Box2D.Common.Math.b2Math.MulTMV(transformB.R,normal.GetNegative());
localPointB=this.m_proxyB.GetSupportVertex(axisB);
pointB=Box2D.Common.Math.b2Math.MulX(transformB,localPointB);
seperation=(pointB.x-pointA.x)*normal.x+(pointB.y-pointA.y)*normal.y;
return seperation;
}
case Box2D.Collision.b2SeparationFunction.e_faceB:
{
normal=Box2D.Common.Math.b2Math.MulMV(transformB.R,this.m_axis);
pointB=Box2D.Common.Math.b2Math.MulX(transformB,this.m_localPoint);
axisA=Box2D.Common.Math.b2Math.MulTMV(transformA.R,normal.GetNegative());
localPointA=this.m_proxyA.GetSupportVertex(axisA);
pointA=Box2D.Common.Math.b2Math.MulX(transformA,localPointA);
seperation=(pointA.x-pointB.x)*normal.x+(pointA.y-pointB.y)*normal.y;
return seperation;
}
default:
return 0.0;
}
},
"public var",{m_proxyA:null},
"public var",{m_proxyB:null},
"public var",{m_type:0},
"public var",{m_localPoint:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{m_axis:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public function b2SeparationFunction",function b2SeparationFunction$(){this.m_localPoint=this.m_localPoint();this.m_axis=this.m_axis();}];},[],["Box2D.Common.Math.b2Math","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2Simplex
joo.classLoader.prepare(
"package Box2D.Collision",
"internal class b2Simplex",1,function($$private){var assert=joo.assert;return[function(){joo.classLoader.init(Number);},

"public function b2Simplex",function()
{this.m_v1=this.m_v1();this.m_v2=this.m_v2();this.m_v3=this.m_v3();this.m_vertices=this.m_vertices();
this.m_vertices[0]=this.m_v1;
this.m_vertices[1]=this.m_v2;
this.m_vertices[2]=this.m_v3;
},
"public function ReadCache",function(cache,
proxyA,transformA,
proxyB,transformB)
{
var wALocal;
var wBLocal;
this.m_count=cache.count;
var vertices=this.m_vertices;
for(var i=0;i<this.m_count;i++)
{
var v=vertices[i];
v.indexA=cache.indexA[i];
v.indexB=cache.indexB[i];
wALocal=proxyA.GetVertex(v.indexA);
wBLocal=proxyB.GetVertex(v.indexB);
v.wA=Box2D.Common.Math.b2Math.MulX(transformA,wALocal);
v.wB=Box2D.Common.Math.b2Math.MulX(transformB,wBLocal);
v.w=Box2D.Common.Math.b2Math.SubtractVV(v.wB,v.wA);
v.a=0;
}
if(this.m_count>1)
{
var metric1=cache.metric;
var metric2=this.GetMetric();
if(metric2<.5*metric1||2.0*metric1<metric2||metric2<Number.MIN_VALUE)
{
this.m_count=0;
}
}
if(this.m_count==0)
{
v=vertices[0];
v.indexA=0;
v.indexB=0;
wALocal=proxyA.GetVertex(0);
wBLocal=proxyB.GetVertex(0);
v.wA=Box2D.Common.Math.b2Math.MulX(transformA,wALocal);
v.wB=Box2D.Common.Math.b2Math.MulX(transformB,wBLocal);
v.w=Box2D.Common.Math.b2Math.SubtractVV(v.wB,v.wA);
this.m_count=1;
}
},
"public function WriteCache",function(cache)
{
cache.metric=this.GetMetric();
cache.count=$$uint(this.m_count);
var vertices=this.m_vertices;
for(var i=0;i<this.m_count;i++)
{
cache.indexA[i]=$$uint(vertices[i].indexA);
cache.indexB[i]=$$uint(vertices[i].indexB);
}
},
"public function GetSearchDirection",function()
{
switch(this.m_count)
{
case 1:
return this.m_v1.w.GetNegative();
case 2:
{
var e12=Box2D.Common.Math.b2Math.SubtractVV(this.m_v2.w,this.m_v1.w);
var sgn=Box2D.Common.Math.b2Math.CrossVV(e12,this.m_v1.w.GetNegative());
if(sgn>0.0)
{
return Box2D.Common.Math.b2Math.CrossFV(1.0,e12);
}else{
return Box2D.Common.Math.b2Math.CrossVF(e12,1.0);
}
}
default:
return new Box2D.Common.Math.b2Vec2();
}
},
"public function GetClosestPoint",function()
{
switch(this.m_count)
{
case 0:
return new Box2D.Common.Math.b2Vec2();
case 1:
return this.m_v1.w;
case 2:
return new Box2D.Common.Math.b2Vec2(
this.m_v1.a*this.m_v1.w.x+this.m_v2.a*this.m_v2.w.x,
this.m_v1.a*this.m_v1.w.y+this.m_v2.a*this.m_v2.w.y);
case 3:
return new Box2D.Common.Math.b2Vec2();
default:
return new Box2D.Common.Math.b2Vec2();
}
},
"public function GetWitnessPoints",function(pA,pB)
{
switch(this.m_count)
{
case 0:
break;
case 1:
pA.SetV(this.m_v1.wA);
pB.SetV(this.m_v1.wB);
break;
case 2:
pA.x=this.m_v1.a*this.m_v1.wA.x+this.m_v2.a*this.m_v2.wA.x;
pA.y=this.m_v1.a*this.m_v1.wA.y+this.m_v2.a*this.m_v2.wA.y;
pB.x=this.m_v1.a*this.m_v1.wB.x+this.m_v2.a*this.m_v2.wB.x;
pB.y=this.m_v1.a*this.m_v1.wB.y+this.m_v2.a*this.m_v2.wB.y;
break;
case 3:
pB.x=pA.x=this.m_v1.a*this.m_v1.wA.x+this.m_v2.a*this.m_v2.wA.x+this.m_v3.a*this.m_v3.wA.x;
pB.y=pA.y=this.m_v1.a*this.m_v1.wA.y+this.m_v2.a*this.m_v2.wA.y+this.m_v3.a*this.m_v3.wA.y;
break;
default:
break;
}
},
"public function GetMetric",function()
{
switch(this.m_count)
{
case 0:
return 0.0;
case 1:
return 0.0;
case 2:
return Box2D.Common.Math.b2Math.SubtractVV(this.m_v1.w,this.m_v2.w).Length();
case 3:
return Box2D.Common.Math.b2Math.CrossVV(Box2D.Common.Math.b2Math.SubtractVV(this.m_v2.w,this.m_v1.w),Box2D.Common.Math.b2Math.SubtractVV(this.m_v3.w,this.m_v1.w));
default:
return 0.0;
}
},
"public function Solve2",function()
{
var w1=this.m_v1.w;
var w2=this.m_v2.w;
var e12=Box2D.Common.Math.b2Math.SubtractVV(w2,w1);
var d12_2=-(w1.x*e12.x+w1.y*e12.y);
if(d12_2<=0.0)
{
this.m_v1.a=1.0;
this.m_count=1;
return;
}
var d12_1=(w2.x*e12.x+w2.y*e12.y);
if(d12_1<=0.0)
{
this.m_v2.a=1.0;
this.m_count=1;
this.m_v1.Set(this.m_v2);
return;
}
var inv_d12=1.0/(d12_1+d12_2);
this.m_v1.a=d12_1*inv_d12;
this.m_v2.a=d12_2*inv_d12;
this.m_count=2;
},
"public function Solve3",function()
{
var w1=this.m_v1.w;
var w2=this.m_v2.w;
var w3=this.m_v3.w;
var e12=Box2D.Common.Math.b2Math.SubtractVV(w2,w1);
var w1e12=Box2D.Common.Math.b2Math.Dot(w1,e12);
var w2e12=Box2D.Common.Math.b2Math.Dot(w2,e12);
var d12_1=w2e12;
var d12_2=-w1e12;
var e13=Box2D.Common.Math.b2Math.SubtractVV(w3,w1);
var w1e13=Box2D.Common.Math.b2Math.Dot(w1,e13);
var w3e13=Box2D.Common.Math.b2Math.Dot(w3,e13);
var d13_1=w3e13;
var d13_2=-w1e13;
var e23=Box2D.Common.Math.b2Math.SubtractVV(w3,w2);
var w2e23=Box2D.Common.Math.b2Math.Dot(w2,e23);
var w3e23=Box2D.Common.Math.b2Math.Dot(w3,e23);
var d23_1=w3e23;
var d23_2=-w2e23;
var n123=Box2D.Common.Math.b2Math.CrossVV(e12,e13);
var d123_1=n123*Box2D.Common.Math.b2Math.CrossVV(w2,w3);
var d123_2=n123*Box2D.Common.Math.b2Math.CrossVV(w3,w1);
var d123_3=n123*Box2D.Common.Math.b2Math.CrossVV(w1,w2);
if(d12_2<=0.0&&d13_2<=0.0)
{
this.m_v1.a=1.0;
this.m_count=1;
return;
}
if(d12_1>0.0&&d12_2>0.0&&d123_3<=0.0)
{
var inv_d12=1.0/(d12_1+d12_2);
this.m_v1.a=d12_1*inv_d12;
this.m_v2.a=d12_2*inv_d12;
this.m_count=2;
return;
}
if(d13_1>0.0&&d13_2>0.0&&d123_2<=0.0)
{
var inv_d13=1.0/(d13_1+d13_2);
this.m_v1.a=d13_1*inv_d13;
this.m_v3.a=d13_2*inv_d13;
this.m_count=2;
this.m_v2.Set(this.m_v3);
return;
}
if(d12_1<=0.0&&d23_2<=0.0)
{
this.m_v2.a=1.0;
this.m_count=1;
this.m_v1.Set(this.m_v2);
return;
}
if(d13_1<=0.0&&d23_1<=0.0)
{
this.m_v3.a=1.0;
this.m_count=1;
this.m_v1.Set(this.m_v3);
return;
}
if(d23_1>0.0&&d23_2>0.0&&d123_1<=0.0)
{
var inv_d23=1.0/(d23_1+d23_2);
this.m_v2.a=d23_1*inv_d23;
this.m_v3.a=d23_2*inv_d23;
this.m_count=2;
this.m_v1.Set(this.m_v3);
return;
}
var inv_d123=1.0/(d123_1+d123_2+d123_3);
this.m_v1.a=d123_1*inv_d123;
this.m_v2.a=d123_2*inv_d123;
this.m_v3.a=d123_3*inv_d123;
this.m_count=3;
},
"public var",{m_v1:function(){return(new Box2D.Collision.b2SimplexVertex());}},
"public var",{m_v2:function(){return(new Box2D.Collision.b2SimplexVertex());}},
"public var",{m_v3:function(){return(new Box2D.Collision.b2SimplexVertex());}},
"public var",{m_vertices:function(){return(new Array(3));}},
"public var",{m_count:0},
];},[],["Box2D.Common.Math.b2Math","Number","uint","Box2D.Common.Math.b2Vec2","Box2D.Collision.b2SimplexVertex","Array"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2SimplexCache
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2SimplexCache",1,function($$private){;return[

"public var",{metric:NaN},
"public var",{count:0},
"public var",{indexA:function(){return(new Array(3));}},
"public var",{indexB:function(){return(new Array(3));}},
"public function b2SimplexCache",function b2SimplexCache$(){this.indexA=this.indexA();this.indexB=this.indexB();}];},[],["Array"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2SimplexVertex
joo.classLoader.prepare(
"package Box2D.Collision",
"internal class b2SimplexVertex",1,function($$private){;return[

"public function Set",function(other)
{
this.wA.SetV(other.wA);
this.wB.SetV(other.wB);
this.w.SetV(other.w);
this.a=other.a;
this.indexA=other.indexA;
this.indexB=other.indexB;
},
"public var",{wA:null},
"public var",{wB:null},
"public var",{w:null},
"public var",{a:NaN},
"public var",{indexA:0},
"public var",{indexB:0},
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2TimeOfImpact
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2TimeOfImpact",1,function($$private){var assert=joo.assert;return[function(){joo.classLoader.init(Number);},

"private static var",{b2_toiCalls:0},
"private static var",{b2_toiIters:0},
"private static var",{b2_toiMaxIters:0},
"private static var",{b2_toiRootIters:0},
"private static var",{b2_toiMaxRootIters:0},
"private static var",{s_cache:function(){return(new Box2D.Collision.b2SimplexCache());}},
"private static var",{s_distanceInput:function(){return(new Box2D.Collision.b2DistanceInput());}},
"private static var",{s_xfA:function(){return(new Box2D.Common.Math.b2Transform());}},
"private static var",{s_xfB:function(){return(new Box2D.Common.Math.b2Transform());}},
"private static var",{s_fcn:function(){return(new Box2D.Collision.b2SeparationFunction());}},
"private static var",{s_distanceOutput:function(){return(new Box2D.Collision.b2DistanceOutput());}},
"public static function TimeOfImpact",function(input)
{
++$$private.b2_toiCalls;
var proxyA=input.proxyA;
var proxyB=input.proxyB;
var sweepA=input.sweepA;
var sweepB=input.sweepB;
var radius=proxyA.m_radius+proxyB.m_radius;
var tolerance=input.tolerance;
var alpha=0.0;var k_maxIterations=1000;
var iter=0;
var target=0.0;
var cache=$$private.s_cache;
cache.count=0;
var distanceInput=$$private.s_distanceInput;
distanceInput.useRadii=false;
var xfA=$$private.s_xfA;
var xfB=$$private.s_xfB;
for(;;)
{
sweepA.GetTransform(xfA,alpha);
sweepB.GetTransform(xfB,alpha);
distanceInput.proxyA=proxyA;
distanceInput.proxyB=proxyB;
distanceInput.transformA=xfA;
distanceInput.transformB=xfB;
var distanceOutput=$$private.s_distanceOutput;
Box2D.Collision.b2Distance.Distance(distanceOutput,cache,distanceInput);
if(distanceOutput.distance<=0.0)
{
alpha=1.0;
break;
}
var fcn=$$private.s_fcn;
fcn.Initialize(cache,proxyA,xfA,proxyB,xfB);
var separation=fcn.Evaluate(xfA,xfB);
if(separation<=0.0)
{
alpha=1.0;
break;
}
if(iter==0)
{
if(separation>radius)
{
target=Box2D.Common.Math.b2Math.Max(radius-tolerance,0.75*radius);
}
else
{
target=Box2D.Common.Math.b2Math.Max(separation-tolerance,0.02*radius);
}
}
if(separation-target<0.5*tolerance)
{
if(iter==0)
{
alpha=1.0;
break;
}
break;
}
var newAlpha=alpha;
{
var x1=alpha;
var x2=1.0;
var f1=separation;
sweepA.GetTransform(xfA,x2);
sweepB.GetTransform(xfB,x2);
var f2=fcn.Evaluate(xfA,xfB);
if(f2>=target)
{
alpha=1.0;
break;
}
var rootIterCount=0;
for(;;)
{
var x;
if(rootIterCount&1)
{
x=x1+(target-f1)*(x2-x1)/(f2-f1);
}
else
{
x=0.5*(x1+x2);
}
sweepA.GetTransform(xfA,x);
sweepB.GetTransform(xfB,x);
var f=$$private.s_fcn.Evaluate($$private.s_xfA,$$private.s_xfB);
if(Box2D.Common.Math.b2Math.Abs(f-target)<0.025*tolerance)
{
newAlpha=x;
break;
}
if(f>target)
{
x1=x;
f1=f;
}
else
{
x2=x;
f2=f;
}
++rootIterCount;
++$$private.b2_toiRootIters;
if(rootIterCount==50)
{
break;
}
}
$$private.b2_toiMaxRootIters=Box2D.Common.Math.b2Math.Max($$private.b2_toiMaxRootIters,rootIterCount);
}
if(newAlpha<(1.0+100.0*Number.MIN_VALUE)*alpha)
{
break;
}
alpha=newAlpha;
iter++;
++$$private.b2_toiIters;
if(iter==k_maxIterations)
{
break;
}
}
$$private.b2_toiMaxIters=Box2D.Common.Math.b2Math.Max($$private.b2_toiMaxIters,iter);
return alpha;
},
];},["TimeOfImpact"],["Box2D.Collision.b2SimplexCache","Box2D.Collision.b2DistanceInput","Box2D.Common.Math.b2Transform","Box2D.Collision.b2SeparationFunction","Box2D.Collision.b2DistanceOutput","Number","Box2D.Collision.b2Distance","Box2D.Common.Math.b2Math"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2TOIInput
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2TOIInput",1,function($$private){;return[

"public var",{proxyA:function(){return(new Box2D.Collision.b2DistanceProxy());}},
"public var",{proxyB:function(){return(new Box2D.Collision.b2DistanceProxy());}},
"public var",{sweepA:function(){return(new Box2D.Common.Math.b2Sweep());}},
"public var",{sweepB:function(){return(new Box2D.Common.Math.b2Sweep());}},
"public var",{tolerance:NaN},
"public function b2TOIInput",function b2TOIInput$(){this.proxyA=this.proxyA();this.proxyB=this.proxyB();this.sweepA=this.sweepA();this.sweepB=this.sweepB();}];},[],["Box2D.Collision.b2DistanceProxy","Box2D.Common.Math.b2Sweep"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.b2WorldManifold
joo.classLoader.prepare(
"package Box2D.Collision",
"public class b2WorldManifold",1,function($$private){;return[function(){joo.classLoader.init(Box2D.Collision.b2Manifold,Number,Box2D.Common.b2Settings);},

"public function b2WorldManifold",function()
{this.m_normal=this.m_normal();
this.m_points=new Array(Box2D.Common.b2Settings.b2_maxManifoldPoints);
for(var i=0;i<Box2D.Common.b2Settings.b2_maxManifoldPoints;i++)
{
this.m_points[i]=new Box2D.Common.Math.b2Vec2();
}
},
"public function Initialize",function(manifold,
xfA,radiusA,
xfB,radiusB)
{
if(manifold.m_pointCount==0)
{
return;
}
var i;
var tVec;
var tMat;
var normalX;
var normalY;
var planePointX;
var planePointY;
var clipPointX;
var clipPointY;
switch(manifold.m_type)
{
case Box2D.Collision.b2Manifold.e_circles:
{
tMat=xfA.R;
tVec=manifold.m_localPoint;
var pointAX=xfA.position.x+tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
var pointAY=xfA.position.y+tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
tMat=xfB.R;
tVec=manifold.m_points[0].m_localPoint;
var pointBX=xfB.position.x+tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
var pointBY=xfB.position.y+tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
var dX=pointBX-pointAX;
var dY=pointBY-pointAY;
var d2=dX*dX+dY*dY;
if(d2>Number.MIN_VALUE*Number.MIN_VALUE)
{
var d=Math.sqrt(d2);
this.m_normal.x=dX/d;
this.m_normal.y=dY/d;
}else{
this.m_normal.x=1;
this.m_normal.y=0;
}
var cAX=pointAX+radiusA*this.m_normal.x;
var cAY=pointAY+radiusA*this.m_normal.y;
var cBX=pointBX-radiusB*this.m_normal.x;
var cBY=pointBY-radiusB*this.m_normal.y;
this.m_points[0].x=0.5*(cAX+cBX);
this.m_points[0].y=0.5*(cAY+cBY);
}
break;
case Box2D.Collision.b2Manifold.e_faceA:
{
tMat=xfA.R;
tVec=manifold.m_localPlaneNormal;
normalX=tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
normalY=tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
tMat=xfA.R;
tVec=manifold.m_localPoint;
planePointX=xfA.position.x+tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
planePointY=xfA.position.y+tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
this.m_normal.x=normalX;
this.m_normal.y=normalY;
for(i=0;i<manifold.m_pointCount;i++)
{
tMat=xfB.R;
tVec=manifold.m_points[i].m_localPoint;
clipPointX=xfB.position.x+tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
clipPointY=xfB.position.y+tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
this.m_points[i].x=clipPointX+0.5*(radiusA-(clipPointX-planePointX)*normalX-(clipPointY-planePointY)*normalY-radiusB)*normalX;
this.m_points[i].y=clipPointY+0.5*(radiusA-(clipPointX-planePointX)*normalX-(clipPointY-planePointY)*normalY-radiusB)*normalY;
}
}
break;
case Box2D.Collision.b2Manifold.e_faceB:
{
tMat=xfB.R;
tVec=manifold.m_localPlaneNormal;
normalX=tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
normalY=tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
tMat=xfB.R;
tVec=manifold.m_localPoint;
planePointX=xfB.position.x+tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
planePointY=xfB.position.y+tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
this.m_normal.x=-normalX;
this.m_normal.y=-normalY;
for(i=0;i<manifold.m_pointCount;i++)
{
tMat=xfA.R;
tVec=manifold.m_points[i].m_localPoint;
clipPointX=xfA.position.x+tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
clipPointY=xfA.position.y+tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
this.m_points[i].x=clipPointX+0.5*(radiusB-(clipPointX-planePointX)*normalX-(clipPointY-planePointY)*normalY-radiusA)*normalX;
this.m_points[i].y=clipPointY+0.5*(radiusB-(clipPointX-planePointX)*normalX-(clipPointY-planePointY)*normalY-radiusA)*normalY;
}
}
break;
}
},
"public var",{m_normal:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{m_points:null},
];},[],["Array","Box2D.Common.b2Settings","Box2D.Common.Math.b2Vec2","Box2D.Collision.b2Manifold","Number","Math"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.ClipVertex
joo.classLoader.prepare(
"package Box2D.Collision",
"public class ClipVertex",1,function($$private){;return[

"public function Set",function(other)
{
this.v.SetV(other.v);
this.id.Set(other.id);
},
"public var",{v:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{id:function(){return(new Box2D.Collision.b2ContactID());}},
"public function ClipVertex",function ClipVertex$(){this.v=this.v();this.id=this.id();}];},[],["Box2D.Common.Math.b2Vec2","Box2D.Collision.b2ContactID"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.Features
joo.classLoader.prepare(
"package Box2D.Collision",
"public class Features",1,function($$private){;return[

"public function get referenceEdge",function(){
return this._referenceEdge;
},
"public function set referenceEdge",function(value){
this._referenceEdge=value;
this._m_id._key=(this._m_id._key&0xffffff00)|(this._referenceEdge&0x000000ff);
},
"b2internal var",{_referenceEdge:0},
"public function get incidentEdge",function(){
return this._incidentEdge;
},
"public function set incidentEdge",function(value){
this._incidentEdge=value;
this._m_id._key=(this._m_id._key&0xffff00ff)|((this._incidentEdge<<8)&0x0000ff00);
},
"b2internal var",{_incidentEdge:0},
"public function get incidentVertex",function(){
return this._incidentVertex;
},
"public function set incidentVertex",function(value){
this._incidentVertex=value;
this._m_id._key=(this._m_id._key&0xff00ffff)|((this._incidentVertex<<16)&0x00ff0000);
},
"b2internal var",{_incidentVertex:0},
"public function get flip",function(){
return this._flip;
},
"public function set flip",function(value){
this._flip=value;
this._m_id._key=(this._m_id._key&0x00ffffff)|((this._flip<<24)&0xff000000);
},
"b2internal var",{_flip:0},
"b2internal var",{_m_id:null},
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Collision.IBroadPhase
joo.classLoader.prepare("package Box2D.Collision",
"public interface IBroadPhase",1,function($$private){;return[
,,,,,,,,,,,,
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Collision.Shapes.b2CircleShape
joo.classLoader.prepare(
"package Box2D.Collision.Shapes",
"public class b2CircleShape extends Box2D.Collision.Shapes.b2Shape",2,function($$private){var is=joo.is,as=joo.as,assert=joo.assert;return[function(){joo.classLoader.init(Number,Math,Box2D.Common.b2Settings);},

"override public function Copy",function()
{
var s=new Box2D.Collision.Shapes.b2CircleShape();
s.Set(this);
return s;
},
"override public function Set",function(other)
{
this.Set$2(other);
if(is(other,Box2D.Collision.Shapes.b2CircleShape))
{
var other2=as(other,Box2D.Collision.Shapes.b2CircleShape);
this.m_p.SetV(other2.m_p);
}
},
"override public function MulBy",function(xf)
{
this.m_p.SetV(Box2D.Common.Math.b2Math.MulX(xf,this.m_p));
},
"override public function ScaleBy",function(scale)
{
this.m_p.Multiply(scale);
this.m_radius*=scale;
},
"override public function ReflectX",function()
{
this.m_p.x=-this.m_p.x;
},
"public override function TestPoint",function(transform,p){
var tMat=transform.R;
var dX=transform.position.x+(tMat.col1.x*this.m_p.x+tMat.col2.x*this.m_p.y);
var dY=transform.position.y+(tMat.col1.y*this.m_p.x+tMat.col2.y*this.m_p.y);
dX=p.x-dX;
dY=p.y-dY;
return(dX*dX+dY*dY)<=this.m_radius*this.m_radius;
},
"public override function RayCast",function(output,input,transform)
{
var tMat=transform.R;
var positionX=transform.position.x+(tMat.col1.x*this.m_p.x+tMat.col2.x*this.m_p.y);
var positionY=transform.position.y+(tMat.col1.y*this.m_p.x+tMat.col2.y*this.m_p.y);
var sX=input.p1.x-positionX;
var sY=input.p1.y-positionY;
var b=(sX*sX+sY*sY)-this.m_radius*this.m_radius;
var rX=input.p2.x-input.p1.x;
var rY=input.p2.y-input.p1.y;
var c=(sX*rX+sY*rY);
var rr=(rX*rX+rY*rY);
var sigma=c*c-rr*b;
if(sigma<0.0||rr<Number.MIN_VALUE)
{
return false;
}
var a=-(c+Math.sqrt(sigma));
if(0.0<=a&&a<=input.maxFraction*rr)
{
a/=rr;
output.fraction=a;
output.normal.x=sX+a*rX;
output.normal.y=sY+a*rY;
output.normal.Normalize();
return true;
}
return false;
},
"public override function ComputeAABB",function(aabb,transform){
var tMat=transform.R;
var pX=transform.position.x+(tMat.col1.x*this.m_p.x+tMat.col2.x*this.m_p.y);
var pY=transform.position.y+(tMat.col1.y*this.m_p.x+tMat.col2.y*this.m_p.y);
aabb.lowerBound.Set(pX-this.m_radius,pY-this.m_radius);
aabb.upperBound.Set(pX+this.m_radius,pY+this.m_radius);
},
"public override function ComputeMass",function(massData,density){
massData.mass=density*Box2D.Common.b2Settings.b2_pi*this.m_radius*this.m_radius;
massData.center.SetV(this.m_p);
massData.I=massData.mass*(0.5*this.m_radius*this.m_radius+(this.m_p.x*this.m_p.x+this.m_p.y*this.m_p.y));
},
"public override function ComputeSubmergedArea",function(
normal,
offset,
xf,
c)
{
var p=Box2D.Common.Math.b2Math.MulX(xf,this.m_p);
var l=-(Box2D.Common.Math.b2Math.Dot(normal,p)-offset);
if(l<-this.m_radius+Number.MIN_VALUE)
{
return 0;
}
if(l>this.m_radius)
{
c.SetV(p);
return Math.PI*this.m_radius*this.m_radius;
}
var r2=this.m_radius*this.m_radius;
var l2=l*l;
var area=r2*(Math.asin(l/this.m_radius)+Math.PI/2)+l*Math.sqrt(r2-l2);
var com=-2/3*Math.pow(r2-l2,1.5)/area;
c.x=p.x+normal.x*com;
c.y=p.y+normal.y*com;
return area;
},
"public function GetLocalPosition",function(){
return this.m_p;
},
"public function SetLocalPosition",function(position){
this.m_p.SetV(position);
},
"public function GetRadius",function()
{
return this.m_radius;
},
"public function SetRadius",function(radius)
{
this.m_radius=radius;
},
"public function b2CircleShape",function(radius){if(arguments.length<1){radius=0;}
this.super$2();this.m_p=this.m_p();
this.m_type=Box2D.Collision.Shapes.b2Shape.e_circleShape;
this.m_radius=radius;
},
"b2internal var",{m_p:function(){return(new Box2D.Common.Math.b2Vec2());}},
];},[],["Box2D.Collision.Shapes.b2Shape","Box2D.Common.Math.b2Math","Number","Math","Box2D.Common.b2Settings","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.Shapes.b2MassData
joo.classLoader.prepare(
"package Box2D.Collision.Shapes",
"public class b2MassData",1,function($$private){;return[

"public var",{mass:0.0},
"public var",{center:function(){return(new Box2D.Common.Math.b2Vec2(0,0));}},
"public var",{I:0.0},
"public function b2MassData",function b2MassData$(){this.center=this.center();}];},[],["Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.Shapes.b2PolygonShape
joo.classLoader.prepare(
"package Box2D.Collision.Shapes",
"public class b2PolygonShape extends Box2D.Collision.Shapes.b2Shape",2,function($$private){var is=joo.is,as=joo.as,assert=joo.assert;return[function(){joo.classLoader.init(Number,Box2D.Common.b2Settings);},

"public override function Copy",function()
{
var s=new Box2D.Collision.Shapes.b2PolygonShape();
s.Set(this);
return s;
},
"public override function Set",function(other)
{
this.Set$2(other);
if(is(other,Box2D.Collision.Shapes.b2PolygonShape))
{
var other2=as(other,Box2D.Collision.Shapes.b2PolygonShape);
this.m_centroid.SetV(other2.m_centroid);
this.m_vertexCount=other2.m_vertexCount;
this.Reserve$2(this.m_vertexCount);
for(var i=0;i<this.m_vertexCount;i++)
{
this.m_vertices[i].SetV(other2.m_vertices[i]);
this.m_normals[i].SetV(other2.m_normals[i]);
}
}
},
"override public function MulBy",function(xf)
{
for(var i=0;i<this.m_vertexCount;++i)
{
this.m_vertices[i].SetV(Box2D.Common.Math.b2Math.MulX(xf,this.m_vertices[i]));
this.m_normals[i].SetV(Box2D.Common.Math.b2Math.MulMV(xf.R,this.m_normals[i]));
}
},
"override public function ScaleBy",function(scale)
{
for(var i=0;i<this.m_vertexCount;++i)
{
this.m_vertices[i].x*=scale;
this.m_vertices[i].y*=scale;
}
},
"override public function ReflectX",function()
{
for(var i=0;i<this.m_vertexCount;++i)
{
this.m_vertices[i].x*=-1;
}
var t;
var i1=0;
var i2=this.m_vertexCount-1;
for(;i1<i2;++i1,--i2)
{
t=this.m_vertices[i1];
this.m_vertices[i1]=this.m_vertices[i2];
this.m_vertices[i2]=t;
}
this.SetAsVector(this.m_vertices);
},
"public function SetAsArray",function(vertices,vertexCount)
{if(arguments.length<2){vertexCount=0;}
var v=new Array();
for(var $1 in vertices)
{var tVec=vertices[$1];
v.push(tVec);
}
this.SetAsVector(v,vertexCount);
},
"public static function AsArray",function(vertices,vertexCount)
{
var polygonShape=new Box2D.Collision.Shapes.b2PolygonShape();
polygonShape.SetAsArray(vertices,vertexCount);
return polygonShape;
},
"public function SetAsVector",function(vertices,vertexCount)
{if(arguments.length<2){vertexCount=0;}
if(vertexCount==0)
vertexCount=vertices.length;
this.m_vertexCount=vertexCount;
this.Reserve$2(vertexCount);
var i;
for(i=0;i<this.m_vertexCount;i++)
{
this.m_vertices[i].SetV(vertices[i]);
}
for(i=0;i<this.m_vertexCount;++i)
{
var i1=i;
var i2=i+1<this.m_vertexCount?i+1:0;
var edge=Box2D.Common.Math.b2Math.SubtractVV(this.m_vertices[i2],this.m_vertices[i1]);
this.m_normals[i].SetV(Box2D.Common.Math.b2Math.CrossVF(edge,1.0));
this.m_normals[i].Normalize();
}
this.m_centroid=Box2D.Collision.Shapes.b2PolygonShape.ComputeCentroid(this.m_vertices,this.m_vertexCount);
},
"public static function AsVector",function(vertices,vertexCount)
{if(arguments.length<2){vertexCount=0;}
var polygonShape=new Box2D.Collision.Shapes.b2PolygonShape();
polygonShape.SetAsVector(vertices,vertexCount);
return polygonShape;
},
"public function SetAsBox",function(hx,hy)
{
this.m_vertexCount=4;
this.Reserve$2(4);
this.m_vertices[0].Set(-hx,-hy);
this.m_vertices[1].Set(hx,-hy);
this.m_vertices[2].Set(hx,hy);
this.m_vertices[3].Set(-hx,hy);
this.m_normals[0].Set(0.0,-1.0);
this.m_normals[1].Set(1.0,0.0);
this.m_normals[2].Set(0.0,1.0);
this.m_normals[3].Set(-1.0,0.0);
this.m_centroid.SetZero();
},
"public static function AsBox",function(hx,hy)
{
var polygonShape=new Box2D.Collision.Shapes.b2PolygonShape();
polygonShape.SetAsBox(hx,hy);
return polygonShape;
},
"static private var",{s_mat:function(){return(new Box2D.Common.Math.b2Mat22());}},
"public function SetAsOrientedBox",function(hx,hy,center,angle)
{if(arguments.length<4){if(arguments.length<3){center=null;}angle=0.0;}
this.m_vertexCount=4;
this.Reserve$2(4);
this.m_vertices[0].Set(-hx,-hy);
this.m_vertices[1].Set(hx,-hy);
this.m_vertices[2].Set(hx,hy);
this.m_vertices[3].Set(-hx,hy);
this.m_normals[0].Set(0.0,-1.0);
this.m_normals[1].Set(1.0,0.0);
this.m_normals[2].Set(0.0,1.0);
this.m_normals[3].Set(-1.0,0.0);
this.m_centroid=center;
var xf=new Box2D.Common.Math.b2Transform();
xf.position=center;
xf.R.Set(angle);
for(var i=0;i<this.m_vertexCount;++i)
{
this.m_vertices[i]=Box2D.Common.Math.b2Math.MulX(xf,this.m_vertices[i]);
this.m_normals[i]=Box2D.Common.Math.b2Math.MulMV(xf.R,this.m_normals[i]);
}
},
"public static function AsOrientedBox",function(hx,hy,center,angle)
{if(arguments.length<4){if(arguments.length<3){center=null;}angle=0.0;}
var polygonShape=new Box2D.Collision.Shapes.b2PolygonShape();
polygonShape.SetAsOrientedBox(hx,hy,center,angle);
return polygonShape;
},
"public function SetAsEdge",function(v1,v2)
{
this.m_vertexCount=2;
this.Reserve$2(2);
this.m_vertices[0].SetV(v1);
this.m_vertices[1].SetV(v2);
this.m_centroid.x=0.5*(v1.x+v2.x);
this.m_centroid.y=0.5*(v1.y+v2.y);
this.m_normals[0]=Box2D.Common.Math.b2Math.CrossVF(Box2D.Common.Math.b2Math.SubtractVV(v2,v1),1.0);
this.m_normals[0].Normalize();
this.m_normals[1].x=-this.m_normals[0].x;
this.m_normals[1].y=-this.m_normals[0].y;
},
"static public function AsEdge",function(v1,v2)
{
var polygonShape=new Box2D.Collision.Shapes.b2PolygonShape();
polygonShape.SetAsEdge(v1,v2);
return polygonShape;
},
"public override function TestPoint",function(xf,p){
var tVec;
var tMat=xf.R;
var tX=p.x-xf.position.x;
var tY=p.y-xf.position.y;
var pLocalX=(tX*tMat.col1.x+tY*tMat.col1.y);
var pLocalY=(tX*tMat.col2.x+tY*tMat.col2.y);
for(var i=0;i<this.m_vertexCount;++i)
{
tVec=this.m_vertices[i];
tX=pLocalX-tVec.x;
tY=pLocalY-tVec.y;
tVec=this.m_normals[i];
var dot=(tVec.x*tX+tVec.y*tY);
if(dot>0.0)
{
return false;
}
}
return true;
},
"public override function RayCast",function(output,input,transform)
{
tX=input.p1.x-transform.position.x;
tY=input.p1.y-transform.position.y;
tMat=transform.R;
var p1X=(tX*tMat.col1.x+tY*tMat.col1.y);
var p1Y=(tX*tMat.col2.x+tY*tMat.col2.y);
tX=input.p2.x-transform.position.x;
tY=input.p2.y-transform.position.y;
tMat=transform.R;
var p2X=(tX*tMat.col1.x+tY*tMat.col1.y);
var p2Y=(tX*tMat.col2.x+tY*tMat.col2.y);
var dX=p2X-p1X;
var dY=p2Y-p1Y;
var numerator;
var denominator;
if(this.m_vertexCount==2)
{
var v1=this.m_vertices[0];
var v2=this.m_vertices[1];
var normal=this.m_normals[0];
numerator=normal.x*(v1.x-p1X)+normal.y*(v1.y-p1Y);
denominator=normal.x*dX+normal.y*dY;
if(denominator==0.0)
return false;
var t=numerator/denominator;
if(t<0.0||1.0<t)
return false;
var qX=p1X+t*dX;
var qY=p1Y+t*dY;
var rX=v2.x-v1.x;
var rY=v2.y-v1.y;
var rr=rX*rX+rY*rY;
if(rr==0.0)
return false;
var s=((qX-v1.x)*rX+(qY-v1.y)*rY)/rr;
if(s<0.0||1.0<s)
return false;
output.fraction=t;
if(numerator>0.0)
{
output.normal.x=-normal.x;
output.normal.y=-normal.y;
}else{
output.normal.x=normal.x;
output.normal.y=normal.y;
}
return true;
}else{
var lower=0.0;
var upper=input.maxFraction;
var tX;
var tY;
var tMat;
var tVec;
var index=-1;
for(var i=0;i<this.m_vertexCount;++i)
{
tVec=this.m_vertices[i];
tX=tVec.x-p1X;
tY=tVec.y-p1Y;
tVec=this.m_normals[i];
numerator=(tVec.x*tX+tVec.y*tY);
denominator=(tVec.x*dX+tVec.y*dY);
if(denominator==0.0)
{
if(numerator<0.0)
{
return false;
}
}
else
{
if(denominator<0.0&&numerator<lower*denominator)
{
lower=numerator/denominator;
index=i;
}
else if(denominator>0.0&&numerator<upper*denominator)
{
upper=numerator/denominator;
}
}
if(upper<lower)
{
return false;
}
}
if(index>=0)
{
output.fraction=lower;
tMat=transform.R;
tVec=this.m_normals[index];
output.normal.x=(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
output.normal.y=(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
return true;
}
return false;
}
},
"public override function ComputeAABB",function(aabb,xf)
{
var tMat=xf.R;
var tVec=this.m_vertices[0];
var lowerX=xf.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
var lowerY=xf.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
var upperX=lowerX;
var upperY=lowerY;
for(var i=1;i<this.m_vertexCount;++i)
{
tVec=this.m_vertices[i];
var vX=xf.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
var vY=xf.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
lowerX=lowerX<vX?lowerX:vX;
lowerY=lowerY<vY?lowerY:vY;
upperX=upperX>vX?upperX:vX;
upperY=upperY>vY?upperY:vY;
}
aabb.lowerBound.x=lowerX-this.m_radius;
aabb.lowerBound.y=lowerY-this.m_radius;
aabb.upperBound.x=upperX+this.m_radius;
aabb.upperBound.y=upperY+this.m_radius;
},
"public override function ComputeMass",function(massData,density){
if(this.m_vertexCount==2)
{
massData.center.x=0.5*(this.m_vertices[0].x+this.m_vertices[1].x);
massData.center.y=0.5*(this.m_vertices[0].y+this.m_vertices[1].y);
massData.mass=0.0;
massData.I=0.0;
return;
}
var centerX=0.0;
var centerY=0.0;
var area=0.0;
var I=0.0;
var p1X=0.0;
var p1Y=0.0;
var k_inv3=1.0/3.0;
for(var i=0;i<this.m_vertexCount;++i)
{
var p2=this.m_vertices[i];
var p3=i+1<this.m_vertexCount?this.m_vertices[$$int(i+1)]:this.m_vertices[0];
var e1X=p2.x-p1X;
var e1Y=p2.y-p1Y;
var e2X=p3.x-p1X;
var e2Y=p3.y-p1Y;
var D=e1X*e2Y-e1Y*e2X;
var triangleArea=0.5*D;
area+=triangleArea;
centerX+=triangleArea*k_inv3*(p1X+p2.x+p3.x);
centerY+=triangleArea*k_inv3*(p1Y+p2.y+p3.y);
var px=p1X;
var py=p1Y;
var ex1=e1X;
var ey1=e1Y;
var ex2=e2X;
var ey2=e2Y;
var intx2=k_inv3*(0.25*(ex1*ex1+ex2*ex1+ex2*ex2)+(px*ex1+px*ex2))+0.5*px*px;
var inty2=k_inv3*(0.25*(ey1*ey1+ey2*ey1+ey2*ey2)+(py*ey1+py*ey2))+0.5*py*py;
I+=D*(intx2+inty2);
}
massData.mass=density*area;
centerX*=1.0/area;
centerY*=1.0/area;
massData.center.Set(centerX,centerY);
massData.I=density*I;
},
"public override function ComputeSubmergedArea",function(
normal,
offset,
xf,
c)
{
var normalL=Box2D.Common.Math.b2Math.MulTMV(xf.R,normal);
var offsetL=offset-Box2D.Common.Math.b2Math.Dot(normal,xf.position);
var depths=new Array();
var diveCount=0;
var intoIndex=-1;
var outoIndex=-1;
var lastSubmerged=false;
var i;
for(i=0;i<this.m_vertexCount;++i)
{
depths[i]=Box2D.Common.Math.b2Math.Dot(normalL,this.m_vertices[i])-offsetL;
var isSubmerged=depths[i]<-Number.MIN_VALUE;
if(i>0)
{
if(isSubmerged)
{
if(!lastSubmerged)
{
intoIndex=i-1;
diveCount++;
}
}
else
{
if(lastSubmerged)
{
outoIndex=i-1;
diveCount++;
}
}
}
lastSubmerged=isSubmerged;
}
switch(diveCount)
{
case 0:
if(lastSubmerged)
{
var md=new Box2D.Collision.Shapes.b2MassData();
this.ComputeMass(md,1);
c.SetV(Box2D.Common.Math.b2Math.MulX(xf,md.center));
return md.mass;
}
else
{
return 0;
}
break;
case 1:
if(intoIndex==-1)
{
intoIndex=this.m_vertexCount-1;
}
else
{
outoIndex=this.m_vertexCount-1;
}
break;
}
var intoIndex2=(intoIndex+1)%this.m_vertexCount;
var outoIndex2=(outoIndex+1)%this.m_vertexCount;
var intoLamdda=(0-depths[intoIndex])/(depths[intoIndex2]-depths[intoIndex]);
var outoLamdda=(0-depths[outoIndex])/(depths[outoIndex2]-depths[outoIndex]);
var intoVec=new Box2D.Common.Math.b2Vec2(this.m_vertices[intoIndex].x*(1-intoLamdda)+this.m_vertices[intoIndex2].x*intoLamdda,
this.m_vertices[intoIndex].y*(1-intoLamdda)+this.m_vertices[intoIndex2].y*intoLamdda);
var outoVec=new Box2D.Common.Math.b2Vec2(this.m_vertices[outoIndex].x*(1-outoLamdda)+this.m_vertices[outoIndex2].x*outoLamdda,
this.m_vertices[outoIndex].y*(1-outoLamdda)+this.m_vertices[outoIndex2].y*outoLamdda);
var area=0;
var center=new Box2D.Common.Math.b2Vec2();
var p2=this.m_vertices[intoIndex2];
var p3;
i=intoIndex2;
while(i!=outoIndex2)
{
i=(i+1)%this.m_vertexCount;
if(i==outoIndex2)
p3=outoVec;
else
p3=this.m_vertices[i];
var triangleArea=0.5*((p2.x-intoVec.x)*(p3.y-intoVec.y)-(p2.y-intoVec.y)*(p3.x-intoVec.x));
area+=triangleArea;
center.x+=triangleArea*(intoVec.x+p2.x+p3.x)/3;
center.y+=triangleArea*(intoVec.y+p2.y+p3.y)/3;
p2=p3;
}
center.Multiply(1/area);
c.SetV(Box2D.Common.Math.b2Math.MulX(xf,center));
return area;
},
"public function GetVertexCount",function(){
return this.m_vertexCount;
},
"public function GetVertices",function(){
return this.m_vertices;
},
"public function GetNormals",function()
{
return this.m_normals;
},
"public function GetSupport",function(d)
{
var bestIndex=0;
var bestValue=this.m_vertices[0].x*d.x+this.m_vertices[0].y*d.y;
for(var i=1;i<this.m_vertexCount;++i)
{
var value=this.m_vertices[i].x*d.x+this.m_vertices[i].y*d.y;
if(value>bestValue)
{
bestIndex=i;
bestValue=value;
}
}
return bestIndex;
},
"public function GetSupportVertex",function(d)
{
var bestIndex=0;
var bestValue=this.m_vertices[0].x*d.x+this.m_vertices[0].y*d.y;
for(var i=1;i<this.m_vertexCount;++i)
{
var value=this.m_vertices[i].x*d.x+this.m_vertices[i].y*d.y;
if(value>bestValue)
{
bestIndex=i;
bestValue=value;
}
}
return this.m_vertices[bestIndex];
},
"private function Validate",function()
{
return false;
},
"public function b2PolygonShape",function(){this.super$2();
this.m_type=Box2D.Collision.Shapes.b2Shape.e_polygonShape;
this.m_radius=Box2D.Common.b2Settings.b2_polygonRadius;
this.m_centroid=new Box2D.Common.Math.b2Vec2();
this.m_vertices=new Array();
this.m_normals=new Array();
},
"private function Reserve",function(count)
{
for(var i=this.m_vertices.length;i<count;i++)
{
this.m_vertices[i]=new Box2D.Common.Math.b2Vec2();
this.m_normals[i]=new Box2D.Common.Math.b2Vec2();
}
},
"b2internal var",{m_centroid:null},
"b2internal var",{m_vertices:null},
"b2internal var",{m_normals:null},
"b2internal var",{m_vertexCount:0},
"static public function ComputeCentroid",function(vs,count)
{
var c=new Box2D.Common.Math.b2Vec2();
var area=0.0;
var p1X=0.0;
var p1Y=0.0;
var inv3=1.0/3.0;
for(var i=0;i<count;++i)
{
var p2=vs[i];
var p3=i+1<count?vs[$$int(i+1)]:vs[0];
var e1X=p2.x-p1X;
var e1Y=p2.y-p1Y;
var e2X=p3.x-p1X;
var e2Y=p3.y-p1Y;
var D=(e1X*e2Y-e1Y*e2X);
var triangleArea=0.5*D;
area+=triangleArea;
c.x+=triangleArea*inv3*(p1X+p2.x+p3.x);
c.y+=triangleArea*inv3*(p1Y+p2.y+p3.y);
}
c.x*=1.0/area;
c.y*=1.0/area;
return c;
},
"static b2internal function ComputeOBB",function(obb,vs,count)
{
var i;
var p=new Array(count+1);
for(i=0;i<count;++i)
{
p[i]=vs[i];
}
p[count]=p[0];
var minArea=Number.MAX_VALUE;
for(i=1;i<=count;++i)
{
var root=p[$$int(i-1)];
var uxX=p[i].x-root.x;
var uxY=p[i].y-root.y;
var length=Math.sqrt(uxX*uxX+uxY*uxY);
uxX/=length;
uxY/=length;
var uyX=-uxY;
var uyY=uxX;
var lowerX=Number.MAX_VALUE;
var lowerY=Number.MAX_VALUE;
var upperX=-Number.MAX_VALUE;
var upperY=-Number.MAX_VALUE;
for(var j=0;j<count;++j)
{
var dX=p[j].x-root.x;
var dY=p[j].y-root.y;
var rX=(uxX*dX+uxY*dY);
var rY=(uyX*dX+uyY*dY);
if(rX<lowerX)lowerX=rX;
if(rY<lowerY)lowerY=rY;
if(rX>upperX)upperX=rX;
if(rY>upperY)upperY=rY;
}
var area=(upperX-lowerX)*(upperY-lowerY);
if(area<0.95*minArea)
{
minArea=area;
obb.R.col1.x=uxX;
obb.R.col1.y=uxY;
obb.R.col2.x=uyX;
obb.R.col2.y=uyY;
var centerX=0.5*(lowerX+upperX);
var centerY=0.5*(lowerY+upperY);
var tMat=obb.R;
obb.center.x=root.x+(tMat.col1.x*centerX+tMat.col2.x*centerY);
obb.center.y=root.y+(tMat.col1.y*centerX+tMat.col2.y*centerY);
obb.extents.x=0.5*(upperX-lowerX);
obb.extents.y=0.5*(upperY-lowerY);
}
}
},
];},["AsArray","AsVector","AsBox","AsOrientedBox","AsEdge","ComputeCentroid","ComputeOBB"],["Box2D.Collision.Shapes.b2Shape","Box2D.Common.Math.b2Math","Array","Number","Box2D.Common.Math.b2Mat22","Box2D.Common.Math.b2Transform","int","Box2D.Collision.Shapes.b2MassData","Box2D.Common.Math.b2Vec2","Box2D.Common.b2Settings","Math"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.Shapes.b2PolyLine
joo.classLoader.prepare(
"package Box2D.Collision.Shapes",
"public class b2PolyLine extends Box2D.Collision.Shapes.b2Shape",2,function($$private){;return[

"public var",{vertices:function(){return(new Array());}},
"public var",{isALoop:false},
"public function b2PolyLine",function b2PolyLine$(){this.super$2();this.vertices=this.vertices();}];},[],["Box2D.Collision.Shapes.b2Shape","Array"], "0.8.0", "0.8.1"
);
// class Box2D.Collision.Shapes.b2Shape
joo.classLoader.prepare(
"package Box2D.Collision.Shapes",
"public class b2Shape",1,function($$private){;return[function(){joo.classLoader.init(Number,Box2D.Common.b2Settings);},

"virtual public function Copy",function()
{
return null;
},
"virtual public function Set",function(other)
{
this.m_radius=other.m_radius;
},
"virtual public function MulBy",function(xf){},
"virtual public function ScaleBy",function(scale){},
"virtual public function ReflectX",function(){},
"public function GetType",function()
{
return this.m_type;
},
"public virtual function TestPoint",function(xf,p){return false;},
"public virtual function RayCast",function(output,input,transform)
{
return false;
},
"public virtual function ComputeAABB",function(aabb,xf){},
"public virtual function ComputeMass",function(massData,density){},
"public virtual function ComputeSubmergedArea",function(
normal,
offset,
xf,
c){return 0;},
"public static function TestOverlap",function(shape1,transform1,shape2,transform2)
{
var input=new Box2D.Collision.b2DistanceInput();
input.proxyA=new Box2D.Collision.b2DistanceProxy();
input.proxyA.Set(shape1);
input.proxyB=new Box2D.Collision.b2DistanceProxy();
input.proxyB.Set(shape2);
input.transformA=transform1;
input.transformB=transform2;
input.useRadii=true;
var simplexCache=new Box2D.Collision.b2SimplexCache();
simplexCache.count=0;
var output=new Box2D.Collision.b2DistanceOutput();
Box2D.Collision.b2Distance.Distance(output,simplexCache,input);
return output.distance<10.0*Number.MIN_VALUE;
},
"public function b2Shape",function()
{
this.m_type=Box2D.Collision.Shapes.b2Shape.e_unknownShape;
this.m_radius=Box2D.Common.b2Settings.b2_linearSlop;
},
"b2internal var",{m_type:0},
"b2internal var",{m_radius:NaN},
"static public const",{e_unknownShape:-1},
"static public const",{e_circleShape:0},
"static public const",{e_polygonShape:1},
"static public const",{e_edgeShape:2},
"static public const",{e_shapeTypeCount:3},
"static public const",{e_hitCollide:1},
"static public const",{e_missCollide:0},
"static public const",{e_startsInsideCollide:-1},
];},["TestOverlap"],["Box2D.Collision.b2DistanceInput","Box2D.Collision.b2DistanceProxy","Box2D.Collision.b2SimplexCache","Box2D.Collision.b2DistanceOutput","Box2D.Collision.b2Distance","Number","Box2D.Common.b2Settings"], "0.8.0", "0.8.1"
);
// class Box2D.Common.b2Color
joo.classLoader.prepare(
"package Box2D.Common",
"public class b2Color",1,function($$private){;return[

"public function b2Color",function(rr,gg,bb){
this._r$1=$$uint(255*Box2D.Common.Math.b2Math.Clamp(rr,0.0,1.0));
this._g$1=$$uint(255*Box2D.Common.Math.b2Math.Clamp(gg,0.0,1.0));
this._b$1=$$uint(255*Box2D.Common.Math.b2Math.Clamp(bb,0.0,1.0));
},
"public function Set",function(rr,gg,bb){
this._r$1=$$uint(255*Box2D.Common.Math.b2Math.Clamp(rr,0.0,1.0));
this._g$1=$$uint(255*Box2D.Common.Math.b2Math.Clamp(gg,0.0,1.0));
this._b$1=$$uint(255*Box2D.Common.Math.b2Math.Clamp(bb,0.0,1.0));
},
"public function set r",function(rr){
this._r$1=$$uint(255*Box2D.Common.Math.b2Math.Clamp(rr,0.0,1.0));
},
"public function set g",function(gg){
this._g$1=$$uint(255*Box2D.Common.Math.b2Math.Clamp(gg,0.0,1.0));
},
"public function set b",function(bb){
this._b$1=$$uint(255*Box2D.Common.Math.b2Math.Clamp(bb,0.0,1.0));
},
"public function get color",function(){
return(this._r$1<<16)|(this._g$1<<8)|(this._b$1);
},
"private var",{_r:0},
"private var",{_g:0},
"private var",{_b:0},
];},[],["uint","Box2D.Common.Math.b2Math"], "0.8.0", "0.8.1"
);
// class Box2D.Common.b2internal
joo.classLoader.prepare(
"package Box2D.Common",
"public namespace b2internal","http://www.box2d.org/ns/b2internal",[],[], "0.8.0", "0.8.1"
);
// class Box2D.Common.b2Settings
joo.classLoader.prepare(
"package Box2D.Common",
"public class b2Settings",1,function($$private){;return[function(){joo.classLoader.init(Math);},
"static public const",{VERSION:"2.1beta"},
"static public const",{USHRT_MAX:0x0000ffff},
"static public const",{b2_pi:function(){return(Math.PI);}},
"static public const",{b2_maxManifoldPoints:2},
"static public const",{b2_aabbExtension:0.1},
"static public const",{b2_aabbMultiplier:2.0},
"static public const",{b2_polygonRadius:function(){return(2.0*Box2D.Common.b2Settings.b2_linearSlop);}},
"static public const",{b2_linearSlop:0.005},
"static public const",{b2_angularSlop:function(){return(2.0/180.0*Box2D.Common.b2Settings.b2_pi);}},
"static public const",{b2_toiSlop:function(){return(8.0*Box2D.Common.b2Settings.b2_linearSlop);}},
"static public const",{b2_maxTOIContacts:32},
"static public const",{b2_maxTOIJoints:32},
"static public const",{b2_velocityThreshold:1.0},
"static public const",{b2_maxLinearCorrection:0.2},
"static public const",{b2_maxAngularCorrection:function(){return(8.0/180.0*Box2D.Common.b2Settings.b2_pi);}},
"static public const",{b2_maxTranslation:2.0},
"static public const",{b2_maxTranslationSquared:function(){return(Box2D.Common.b2Settings.b2_maxTranslation*Box2D.Common.b2Settings.b2_maxTranslation);}},
"static public const",{b2_maxRotation:function(){return(0.5*Box2D.Common.b2Settings.b2_pi);}},
"static public const",{b2_maxRotationSquared:function(){return(Box2D.Common.b2Settings.b2_maxRotation*Box2D.Common.b2Settings.b2_maxRotation);}},
"static public const",{b2_contactBaumgarte:0.2},
"public static function b2MixFriction",function(friction1,friction2)
{
return Math.sqrt(friction1*friction2);
},
"public static function b2MixRestitution",function(restitution1,restitution2)
{
return restitution1>restitution2?restitution1:restitution2;
},
"static public const",{b2_timeToSleep:0.5},
"static public const",{b2_linearSleepTolerance:0.01},
"static public const",{b2_angularSleepTolerance:function(){return(2.0/180.0*Box2D.Common.b2Settings.b2_pi);}},
"static public function b2Assert",function(a)
{
if(!a){
throw"Assertion Failed";
}
},
];},["b2MixFriction","b2MixRestitution","b2Assert"],["Math"], "0.8.0", "0.8.1"
);
// class Box2D.Common.Math.b2Mat22
joo.classLoader.prepare(
"package Box2D.Common.Math",
"public class b2Mat22",1,function($$private){;return[

"public function b2Mat22",function()
{this.col1=this.col1();this.col2=this.col2();
this.col1.x=this.col2.y=1.0;
},
"public static function FromAngle",function(angle)
{
var mat=new Box2D.Common.Math.b2Mat22();
mat.Set(angle);
return mat;
},
"public static function FromVV",function(c1,c2)
{
var mat=new Box2D.Common.Math.b2Mat22();
mat.SetVV(c1,c2);
return mat;
},
"public function Set",function(angle)
{
var c=Math.cos(angle);
var s=Math.sin(angle);
this.col1.x=c;this.col2.x=-s;
this.col1.y=s;this.col2.y=c;
},
"public function SetVV",function(c1,c2)
{
this.col1.SetV(c1);
this.col2.SetV(c2);
},
"public function Copy",function(){
var mat=new Box2D.Common.Math.b2Mat22();
mat.SetM(this);
return mat;
},
"public function SetM",function(m)
{
this.col1.SetV(m.col1);
this.col2.SetV(m.col2);
},
"public function AddM",function(m)
{
this.col1.x+=m.col1.x;
this.col1.y+=m.col1.y;
this.col2.x+=m.col2.x;
this.col2.y+=m.col2.y;
},
"public function SetIdentity",function()
{
this.col1.x=1.0;this.col2.x=0.0;
this.col1.y=0.0;this.col2.y=1.0;
},
"public function SetZero",function()
{
this.col1.x=0.0;this.col2.x=0.0;
this.col1.y=0.0;this.col2.y=0.0;
},
"public function GetAngle",function()
{
return Math.atan2(this.col1.y,this.col1.x);
},
"public function GetInverse",function(out)
{
var a=this.col1.x;
var b=this.col2.x;
var c=this.col1.y;
var d=this.col2.y;
var det=a*d-b*c;
if(det!=0.0)
{
det=1.0/det;
}
out.col1.x=det*d;out.col2.x=-det*b;
out.col1.y=-det*c;out.col2.y=det*a;
return out;
},
"public function Solve",function(out,bX,bY)
{
var a11=this.col1.x;
var a12=this.col2.x;
var a21=this.col1.y;
var a22=this.col2.y;
var det=a11*a22-a12*a21;
if(det!=0.0)
{
det=1.0/det;
}
out.x=det*(a22*bX-a12*bY);
out.y=det*(a11*bY-a21*bX);
return out;
},
"public function Abs",function()
{
this.col1.Abs();
this.col2.Abs();
},
"public var",{col1:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{col2:function(){return(new Box2D.Common.Math.b2Vec2());}},
];},["FromAngle","FromVV"],["Math","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Common.Math.b2Mat33
joo.classLoader.prepare(
"package Box2D.Common.Math",
"public class b2Mat33",1,function($$private){;return[

"public function b2Mat33",function(c1,c2,c3)
{if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){c1=null;}c2=null;}c3=null;}this.col1=this.col1();this.col2=this.col2();this.col3=this.col3();
if(!c1&&!c2&&!c3)
{
this.col1.SetZero();
this.col2.SetZero();
this.col3.SetZero();
}
else
{
this.col1.SetV(c1);
this.col2.SetV(c2);
this.col3.SetV(c3);
}
},
"public function SetVVV",function(c1,c2,c3)
{
this.col1.SetV(c1);
this.col2.SetV(c2);
this.col3.SetV(c3);
},
"public function Copy",function(){
return new Box2D.Common.Math.b2Mat33(this.col1,this.col2,this.col3);
},
"public function SetM",function(m)
{
this.col1.SetV(m.col1);
this.col2.SetV(m.col2);
this.col3.SetV(m.col3);
},
"public function AddM",function(m)
{
this.col1.x+=m.col1.x;
this.col1.y+=m.col1.y;
this.col1.z+=m.col1.z;
this.col2.x+=m.col2.x;
this.col2.y+=m.col2.y;
this.col2.z+=m.col2.z;
this.col3.x+=m.col3.x;
this.col3.y+=m.col3.y;
this.col3.z+=m.col3.z;
},
"public function SetIdentity",function()
{
this.col1.x=1.0;this.col2.x=0.0;this.col3.x=0.0;
this.col1.y=0.0;this.col2.y=1.0;this.col3.y=0.0;
this.col1.z=0.0;this.col2.z=0.0;this.col3.z=1.0;
},
"public function SetZero",function()
{
this.col1.x=0.0;this.col2.x=0.0;this.col3.x=0.0;
this.col1.y=0.0;this.col2.y=0.0;this.col3.y=0.0;
this.col1.z=0.0;this.col2.z=0.0;this.col3.z=0.0;
},
"public function Solve22",function(out,bX,bY)
{
var a11=this.col1.x;
var a12=this.col2.x;
var a21=this.col1.y;
var a22=this.col2.y;
var det=a11*a22-a12*a21;
if(det!=0.0)
{
det=1.0/det;
}
out.x=det*(a22*bX-a12*bY);
out.y=det*(a11*bY-a21*bX);
return out;
},
"public function Solve33",function(out,bX,bY,bZ)
{
var a11=this.col1.x;
var a21=this.col1.y;
var a31=this.col1.z;
var a12=this.col2.x;
var a22=this.col2.y;
var a32=this.col2.z;
var a13=this.col3.x;
var a23=this.col3.y;
var a33=this.col3.z;
var det=a11*(a22*a33-a32*a23)+
a21*(a32*a13-a12*a33)+
a31*(a12*a23-a22*a13);
if(det!=0.0)
{
det=1.0/det;
}
out.x=det*(bX*(a22*a33-a32*a23)+
bY*(a32*a13-a12*a33)+
bZ*(a12*a23-a22*a13));
out.y=det*(a11*(bY*a33-bZ*a23)+
a21*(bZ*a13-bX*a33)+
a31*(bX*a23-bY*a13));
out.z=det*(a11*(a22*bZ-a32*bY)+
a21*(a32*bX-a12*bZ)+
a31*(a12*bY-a22*bX));
return out;
},
"public var",{col1:function(){return(new Box2D.Common.Math.b2Vec3());}},
"public var",{col2:function(){return(new Box2D.Common.Math.b2Vec3());}},
"public var",{col3:function(){return(new Box2D.Common.Math.b2Vec3());}},
];},[],["Box2D.Common.Math.b2Vec3"], "0.8.0", "0.8.1"
);
// class Box2D.Common.Math.b2Math
joo.classLoader.prepare(
"package Box2D.Common.Math",
"public class b2Math",1,function($$private){;return[
"static public function IsValid",function(x)
{
return isFinite(x);
},
"static public function Dot",function(a,b)
{
return a.x*b.x+a.y*b.y;
},
"static public function CrossVV",function(a,b)
{
return a.x*b.y-a.y*b.x;
},
"static public function CrossVF",function(a,s)
{
var v=new Box2D.Common.Math.b2Vec2(s*a.y,-s*a.x);
return v;
},
"static public function CrossFV",function(s,a)
{
var v=new Box2D.Common.Math.b2Vec2(-s*a.y,s*a.x);
return v;
},
"static public function MulMV",function(A,v)
{
var u=new Box2D.Common.Math.b2Vec2(A.col1.x*v.x+A.col2.x*v.y,A.col1.y*v.x+A.col2.y*v.y);
return u;
},
"static public function MulTMV",function(A,v)
{
var u=new Box2D.Common.Math.b2Vec2(Box2D.Common.Math.b2Math.Dot(v,A.col1),Box2D.Common.Math.b2Math.Dot(v,A.col2));
return u;
},
"static public function MulX",function(T,v)
{
var a=Box2D.Common.Math.b2Math.MulMV(T.R,v);
a.x+=T.position.x;
a.y+=T.position.y;
return a;
},
"static public function MulXT",function(T,v)
{
var a=Box2D.Common.Math.b2Math.SubtractVV(v,T.position);
var tX=(a.x*T.R.col1.x+a.y*T.R.col1.y);
a.y=(a.x*T.R.col2.x+a.y*T.R.col2.y);
a.x=tX;
return a;
},
"static public function MulXX",function(T1,T2)
{
var out=new Box2D.Common.Math.b2Transform();
out.R.SetM(Box2D.Common.Math.b2Math.MulMM(T1.R,T2.R));
out.position.SetV(Box2D.Common.Math.b2Math.MulX(T1,T2.position));
return out;
},
"static public function AddVV",function(a,b)
{
var v=new Box2D.Common.Math.b2Vec2(a.x+b.x,a.y+b.y);
return v;
},
"static public function SubtractVV",function(a,b)
{
var v=new Box2D.Common.Math.b2Vec2(a.x-b.x,a.y-b.y);
return v;
},
"static public function Distance",function(a,b){
var cX=a.x-b.x;
var cY=a.y-b.y;
return Math.sqrt(cX*cX+cY*cY);
},
"static public function DistanceSquared",function(a,b){
var cX=a.x-b.x;
var cY=a.y-b.y;
return(cX*cX+cY*cY);
},
"static public function MulFV",function(s,a)
{
var v=new Box2D.Common.Math.b2Vec2(s*a.x,s*a.y);
return v;
},
"static public function AddMM",function(A,B)
{
var C=Box2D.Common.Math.b2Mat22.FromVV(Box2D.Common.Math.b2Math.AddVV(A.col1,B.col1),Box2D.Common.Math.b2Math.AddVV(A.col2,B.col2));
return C;
},
"static public function MulMM",function(A,B)
{
var C=Box2D.Common.Math.b2Mat22.FromVV(Box2D.Common.Math.b2Math.MulMV(A,B.col1),Box2D.Common.Math.b2Math.MulMV(A,B.col2));
return C;
},
"static public function MulTMM",function(A,B)
{
var c1=new Box2D.Common.Math.b2Vec2(Box2D.Common.Math.b2Math.Dot(A.col1,B.col1),Box2D.Common.Math.b2Math.Dot(A.col2,B.col1));
var c2=new Box2D.Common.Math.b2Vec2(Box2D.Common.Math.b2Math.Dot(A.col1,B.col2),Box2D.Common.Math.b2Math.Dot(A.col2,B.col2));
var C=Box2D.Common.Math.b2Mat22.FromVV(c1,c2);
return C;
},
"static public function Abs",function(a)
{
return a>0.0?a:-a;
},
"static public function AbsV",function(a)
{
var b=new Box2D.Common.Math.b2Vec2(Box2D.Common.Math.b2Math.Abs(a.x),Box2D.Common.Math.b2Math.Abs(a.y));
return b;
},
"static public function AbsM",function(A)
{
var B=Box2D.Common.Math.b2Mat22.FromVV(Box2D.Common.Math.b2Math.AbsV(A.col1),Box2D.Common.Math.b2Math.AbsV(A.col2));
return B;
},
"static public function Min",function(a,b)
{
return a<b?a:b;
},
"static public function MinV",function(a,b)
{
var c=new Box2D.Common.Math.b2Vec2(Box2D.Common.Math.b2Math.Min(a.x,b.x),Box2D.Common.Math.b2Math.Min(a.y,b.y));
return c;
},
"static public function Max",function(a,b)
{
return a>b?a:b;
},
"static public function MaxV",function(a,b)
{
var c=new Box2D.Common.Math.b2Vec2(Box2D.Common.Math.b2Math.Max(a.x,b.x),Box2D.Common.Math.b2Math.Max(a.y,b.y));
return c;
},
"static public function Clamp",function(a,low,high)
{
return a<low?low:a>high?high:a;
},
"static public function ClampV",function(a,low,high)
{
return Box2D.Common.Math.b2Math.MaxV(low,Box2D.Common.Math.b2Math.MinV(a,high));
},
"static public function Swap",function(a,b)
{
var tmp=a[0];
a[0]=b[0];
b[0]=tmp;
},
"static public function Random",function()
{
return Math.random()*2-1;
},
"static public function RandomRange",function(lo,hi)
{
var r=Math.random();
r=(hi-lo)*r+lo;
return r;
},
"static public function NextPowerOfTwo",function(x)
{
x|=(x>>1)&0x7FFFFFFF;
x|=(x>>2)&0x3FFFFFFF;
x|=(x>>4)&0x0FFFFFFF;
x|=(x>>8)&0x00FFFFFF;
x|=(x>>16)&0x0000FFFF;
return x+1;
},
"static public function IsPowerOfTwo",function(x)
{
var result=x>0&&(x&(x-1))==0;
return result;
},
"static public const",{b2Vec2_zero:function(){return(new Box2D.Common.Math.b2Vec2(0.0,0.0));}},
"static public const",{b2Mat22_identity:function(){return(Box2D.Common.Math.b2Mat22.FromVV(new Box2D.Common.Math.b2Vec2(1.0,0.0),new Box2D.Common.Math.b2Vec2(0.0,1.0)));}},
"static public const",{b2Transform_identity:function(){return(new Box2D.Common.Math.b2Transform(Box2D.Common.Math.b2Math.b2Vec2_zero,Box2D.Common.Math.b2Math.b2Mat22_identity));}},
];},["IsValid","Dot","CrossVV","CrossVF","CrossFV","MulMV","MulTMV","MulX","MulXT","MulXX","AddVV","SubtractVV","Distance","DistanceSquared","MulFV","AddMM","MulMM","MulTMM","Abs","AbsV","AbsM","Min","MinV","Max","MaxV","Clamp","ClampV","Swap","Random","RandomRange","NextPowerOfTwo","IsPowerOfTwo"],["Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Transform","Math","Box2D.Common.Math.b2Mat22"], "0.8.0", "0.8.1"
);
// class Box2D.Common.Math.b2Sweep
joo.classLoader.prepare(
"package Box2D.Common.Math",
"public class b2Sweep",1,function($$private){;return[function(){joo.classLoader.init(Number);},

"public function Set",function(other)
{
this.localCenter.SetV(other.localCenter);
this.c0.SetV(other.c0);
this.c.SetV(other.c);
this.a0=other.a0;
this.a=other.a;
this.t0=other.t0;
},
"public function Copy",function()
{
var copy=new Box2D.Common.Math.b2Sweep();
copy.localCenter.SetV(this.localCenter);
copy.c0.SetV(this.c0);
copy.c.SetV(this.c);
copy.a0=this.a0;
copy.a=this.a;
copy.t0=this.t0;
return copy;
},
"public function GetTransform",function(xf,alpha)
{
xf.position.x=(1.0-alpha)*this.c0.x+alpha*this.c.x;
xf.position.y=(1.0-alpha)*this.c0.y+alpha*this.c.y;
var angle=(1.0-alpha)*this.a0+alpha*this.a;
xf.R.Set(angle);
var tMat=xf.R;
xf.position.x-=(tMat.col1.x*this.localCenter.x+tMat.col2.x*this.localCenter.y);
xf.position.y-=(tMat.col1.y*this.localCenter.x+tMat.col2.y*this.localCenter.y);
},
"public function Advance",function(t){
if(this.t0<t&&1.0-this.t0>Number.MIN_VALUE)
{
var alpha=(t-this.t0)/(1.0-this.t0);
this.c0.x=(1.0-alpha)*this.c0.x+alpha*this.c.x;
this.c0.y=(1.0-alpha)*this.c0.y+alpha*this.c.y;
this.a0=(1.0-alpha)*this.a0+alpha*this.a;
this.t0=t;
}
},
"public var",{localCenter:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{c0:function(){return(new Box2D.Common.Math.b2Vec2);}},
"public var",{c:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{a0:NaN},
"public var",{a:NaN},
"public var",{t0:NaN},
"public function b2Sweep",function b2Sweep$(){this.localCenter=this.localCenter();this.c0=this.c0();this.c=this.c();}];},[],["Number","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Common.Math.b2Transform
joo.classLoader.prepare(
"package Box2D.Common.Math",
"public class b2Transform",1,function($$private){;return[

"public function b2Transform",function(pos,r)
{if(arguments.length<2){if(arguments.length<1){pos=null;}r=null;}this.position=this.position();this.R=this.R();
if(pos){
this.position.SetV(pos);
this.R.SetM(r);
}
},
"public function Initialize",function(pos,r)
{
this.position.SetV(pos);
this.R.SetM(r);
},
"public function SetIdentity",function()
{
this.position.SetZero();
this.R.SetIdentity();
},
"public function Set",function(x)
{
this.position.SetV(x.position);
this.R.SetM(x.R);
},
"public function GetInverse",function(out)
{if(arguments.length<1){out=null;}
if(!out)
out=new Box2D.Common.Math.b2Transform();
this.R.GetInverse(out.R);
out.position.SetV(Box2D.Common.Math.b2Math.MulMV(out.R,this.position));
out.position.NegativeSelf();
return out;
},
"public function GetAngle",function()
{
return Math.atan2(this.R.col1.y,this.R.col1.x);
},
"public var",{position:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{R:function(){return(new Box2D.Common.Math.b2Mat22());}},
];},[],["Box2D.Common.Math.b2Math","Math","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Mat22"], "0.8.0", "0.8.1"
);
// class Box2D.Common.Math.b2Vec2
joo.classLoader.prepare(
"package Box2D.Common.Math",
"public class b2Vec2",1,function($$private){;return[function(){joo.classLoader.init(Number);},

"public function b2Vec2",function(x_,y_){if(arguments.length<2){if(arguments.length<1){x_=0;}y_=0;}this.x=x_;this.y=y_;},
"public function SetZero",function(){this.x=0.0;this.y=0.0;},
"public function Set",function(x_,y_){if(arguments.length<2){if(arguments.length<1){x_=0;}y_=0;}this.x=x_;this.y=y_;},
"public function SetV",function(v){this.x=v.x;this.y=v.y;},
"public function GetNegative",function(){return new Box2D.Common.Math.b2Vec2(-this.x,-this.y);},
"public function NegativeSelf",function(){this.x=-this.x;this.y=-this.y;},
"static public function Make",function(x_,y_)
{
return new Box2D.Common.Math.b2Vec2(x_,y_);
},
"public function Copy",function(){
return new Box2D.Common.Math.b2Vec2(this.x,this.y);
},
"public function Add",function(v)
{
this.x+=v.x;this.y+=v.y;
},
"public function Subtract",function(v)
{
this.x-=v.x;this.y-=v.y;
},
"public function Multiply",function(a)
{
this.x*=a;this.y*=a;
},
"public function MulM",function(A)
{
var tX=this.x;
this.x=A.col1.x*tX+A.col2.x*this.y;
this.y=A.col1.y*tX+A.col2.y*this.y;
},
"public function MulTM",function(A)
{
var tX=Box2D.Common.Math.b2Math.Dot(this,A.col1);
this.y=Box2D.Common.Math.b2Math.Dot(this,A.col2);
this.x=tX;
},
"public function CrossVF",function(s)
{
var tX=this.x;
this.x=s*this.y;
this.y=-s*tX;
},
"public function CrossFV",function(s)
{
var tX=this.x;
this.x=-s*this.y;
this.y=s*tX;
},
"public function MinV",function(b)
{
this.x=this.x<b.x?this.x:b.x;
this.y=this.y<b.y?this.y:b.y;
},
"public function MaxV",function(b)
{
this.x=this.x>b.x?this.x:b.x;
this.y=this.y>b.y?this.y:b.y;
},
"public function Abs",function()
{
if(this.x<0)this.x=-this.x;
if(this.y<0)this.y=-this.y;
},
"public function Length",function()
{
return Math.sqrt(this.x*this.x+this.y*this.y);
},
"public function LengthSquared",function()
{
return(this.x*this.x+this.y*this.y);
},
"public function Normalize",function()
{
var length=Math.sqrt(this.x*this.x+this.y*this.y);
if(length<Number.MIN_VALUE)
{
return 0.0;
}
var invLength=1.0/length;
this.x*=invLength;
this.y*=invLength;
return length;
},
"public function IsValid",function()
{
return Box2D.Common.Math.b2Math.IsValid(this.x)&&Box2D.Common.Math.b2Math.IsValid(this.y);
},
"public var",{x:NaN},
"public var",{y:NaN},
];},["Make"],["Box2D.Common.Math.b2Math","Math","Number"], "0.8.0", "0.8.1"
);
// class Box2D.Common.Math.b2Vec3
joo.classLoader.prepare(
"package Box2D.Common.Math",
"public class b2Vec3",1,function($$private){;return[

"public function b2Vec3",function(x,y,z)
{if(arguments.length<3){if(arguments.length<2){if(arguments.length<1){x=0;}y=0;}z=0;}
this.x=x;
this.y=y;
this.z=z;
},
"public function SetZero",function()
{
this.x=this.y=this.z=0.0;
},
"public function Set",function(x,y,z)
{
this.x=x;
this.y=y;
this.z=z;
},
"public function SetV",function(v)
{
this.x=v.x;
this.y=v.y;
this.z=v.z;
},
"public function GetNegative",function(){return new Box2D.Common.Math.b2Vec3(-this.x,-this.y,-this.z);},
"public function NegativeSelf",function(){this.x=-this.x;this.y=-this.y;this.z=-this.z;},
"public function Copy",function(){
return new Box2D.Common.Math.b2Vec3(this.x,this.y,this.z);
},
"public function Add",function(v)
{
this.x+=v.x;this.y+=v.y;this.z+=v.z;
},
"public function Subtract",function(v)
{
this.x-=v.x;this.y-=v.y;this.z-=v.z;
},
"public function Multiply",function(a)
{
this.x*=a;this.y*=a;this.z*=a;
},
"public var",{x:NaN},
"public var",{y:NaN},
"public var",{z:NaN},
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2Body
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2Body",1,function($$private){var is=joo.is,as=joo.as,assert=joo.assert;return[function(){joo.classLoader.init(Box2D.Dynamics.b2World);},

"public function CreateFixture",function(def)
{
this.m_world.CheckUnlocked();
if(is(def.shape,Box2D.Collision.Shapes.b2PolyLine))
{
var pl=(as(def.shape,Box2D.Collision.Shapes.b2PolyLine));
var p=new Box2D.Collision.Shapes.b2PolygonShape();
def.shape=p;
var vertices=pl.vertices;
for(var i=1;i<vertices.length;i++)
{
p.SetAsEdge(vertices[i-1],vertices[i]);
this.CreateFixture(def);
}
if(pl.isALoop)
{
p.SetAsEdge(vertices[vertices.length-1],vertices[0]);
this.CreateFixture(def);
}
def.shape=pl;
return null;
}
var fixture=new Box2D.Dynamics.b2Fixture();
fixture.Create(this,this.m_xf,def);
if(this.m_flags&Box2D.Dynamics.b2Body.e_activeFlag)
{
var broadPhase=this.m_world.m_contactManager.m_broadPhase;
fixture.CreateProxy(broadPhase,this.m_xf);
}
fixture.m_next=this.m_fixtureList;
this.m_fixtureList=fixture;
++this.m_fixtureCount;
fixture.m_body=this;
if(fixture.m_density>0.0)
{
this.ResetMassData();
}
this.m_world.m_flags|=Box2D.Dynamics.b2World.e_newFixture;
this.m_world.m_addFixtureEvent.fixture=fixture;
this.m_world.dispatchEvent(this.m_world.m_addFixtureEvent);
if(this.m_eventDispatcher)this.m_eventDispatcher.dispatchEvent(this.m_world.m_addFixtureEvent);
return fixture;
},
"public function CreateFixture2",function(shape,density)
{
var def=new Box2D.Dynamics.b2FixtureDef();
def.shape=shape;
def.density=density;
return this.CreateFixture(def);
},
"public function DestroyFixture",function(fixture)
{
this.m_world.CheckUnlocked();
if(!fixture.m_body)
throw new Error("You cannot delete a fixture twice.");
this.m_world.m_removeFixtureEvent.fixture=fixture;
if(this.m_eventDispatcher)this.m_eventDispatcher.dispatchEvent(this.m_world.m_removeFixtureEvent);
this.m_world.dispatchEvent(this.m_world.m_removeFixtureEvent);
var node=this.m_fixtureList;
var ppF=null;
var found=false;
while(node!=null)
{
if(node==fixture)
{
if(ppF)
ppF.m_next=fixture.m_next;
else
this.m_fixtureList=fixture.m_next;
found=true;
break;
}
ppF=node;
node=node.m_next;
}
var edge=this.m_contactList;
while(edge)
{
var c=edge.contact;
edge=edge.next;
var fixtureA=c.GetFixtureA();
var fixtureB=c.GetFixtureB();
if(fixture==fixtureA||fixture==fixtureB)
{
this.m_world.m_contactManager.Destroy(c);
}
}
if(this.m_flags&Box2D.Dynamics.b2Body.e_activeFlag)
{
var broadPhase=this.m_world.m_contactManager.m_broadPhase;
fixture.DestroyProxy(broadPhase);
}
else
{
}
fixture.Destroy();
fixture.m_body=null;
fixture.m_next=null;
--this.m_fixtureCount;
this.ResetMassData();
fixture.m_body=null;
},
"public function SetPositionAndAngle",function(position,angle){
var f;
this.m_world.CheckUnlocked();
this.m_xf.R.Set(angle);
this.m_xf.position.SetV(position);
var tMat=this.m_xf.R;
var tVec=this.m_sweep.localCenter;
this.m_sweep.c.x=(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
this.m_sweep.c.y=(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
this.m_sweep.c.x+=this.m_xf.position.x;
this.m_sweep.c.y+=this.m_xf.position.y;
this.m_sweep.c0.SetV(this.m_sweep.c);
this.m_sweep.a0=this.m_sweep.a=angle;
var broadPhase=this.m_world.m_contactManager.m_broadPhase;
for(f=this.m_fixtureList;f;f=f.m_next)
{
f.Synchronize(broadPhase,this.m_xf,this.m_xf);
}
this.m_world.m_contactManager.FindNewContacts();
},
"public function SetTransform",function(xf)
{
this.SetPositionAndAngle(xf.position,xf.GetAngle());
},
"public function GetTransform",function(){
return this.m_xf;
},
"public function GetPosition",function(){
return this.m_xf.position;
},
"public function SetPosition",function(position)
{
this.SetPositionAndAngle(position,this.GetAngle());
},
"public function GetAngle",function(){
return this.m_sweep.a;
},
"public function SetAngle",function(angle)
{
this.SetPositionAndAngle(this.GetPosition(),angle);
},
"public function GetWorldCenter",function(){
return this.m_sweep.c;
},
"public function GetLocalCenter",function(){
return this.m_sweep.localCenter;
},
"public function SetLinearVelocity",function(v){
if(this.m_type==Box2D.Dynamics.b2Body.b2_staticBody)
{
return;
}
this.m_linearVelocity.SetV(v);
},
"public function GetLinearVelocity",function(){
return this.m_linearVelocity;
},
"public function SetAngularVelocity",function(omega){
if(this.m_type==Box2D.Dynamics.b2Body.b2_staticBody)
{
return;
}
this.m_angularVelocity=omega;
},
"public function GetAngularVelocity",function(){
return this.m_angularVelocity;
},
"public function GetDefinition",function()
{
var bd=new Box2D.Dynamics.b2BodyDef();
bd.type=this.GetType();
bd.allowSleep=(this.m_flags&Box2D.Dynamics.b2Body.e_allowSleepFlag)==Box2D.Dynamics.b2Body.e_allowSleepFlag;
bd.angle=this.GetAngle();
bd.angularDamping=this.m_angularDamping;
bd.angularVelocity=this.m_angularVelocity;
bd.fixedRotation=(this.m_flags&Box2D.Dynamics.b2Body.e_fixedRotationFlag)==Box2D.Dynamics.b2Body.e_fixedRotationFlag;
bd.bullet=(this.m_flags&Box2D.Dynamics.b2Body.e_bulletFlag)==Box2D.Dynamics.b2Body.e_bulletFlag;
bd.awake=(this.m_flags&Box2D.Dynamics.b2Body.e_awakeFlag)==Box2D.Dynamics.b2Body.e_awakeFlag;
bd.linearDamping=this.m_linearDamping;
bd.linearVelocity.SetV(this.GetLinearVelocity());
bd.position=this.GetPosition();
bd.userData=this.GetUserData();
return bd;
},
"public function SetDefinition",function(bd)
{
this.SetType(bd.type);
this.SetSleepingAllowed(bd.allowSleep);
this.SetPositionAndAngle(bd.position,bd.angle);
this.m_angularDamping=bd.angularDamping;
this.m_angularVelocity=bd.angularVelocity;
this.SetFixedRotation(bd.fixedRotation);
this.SetBullet(bd.bullet);
this.SetAwake(bd.awake);
this.m_linearDamping=bd.linearDamping;
this.m_linearVelocity.SetV(bd.linearVelocity);
this.m_userData$1=bd.userData;
},
"public function ApplyForce",function(force,point){
if(this.m_type!=Box2D.Dynamics.b2Body.b2_dynamicBody)
{
return;
}
if(this.IsAwake()==false)
{
this.SetAwake(true);
}
this.m_force.x+=force.x;
this.m_force.y+=force.y;
this.m_torque+=((point.x-this.m_sweep.c.x)*force.y-(point.y-this.m_sweep.c.y)*force.x);
},
"public function ApplyTorque",function(torque){
if(this.m_type!=Box2D.Dynamics.b2Body.b2_dynamicBody)
{
return;
}
if(this.IsAwake()==false)
{
this.SetAwake(true);
}
this.m_torque+=torque;
},
"public function ApplyLinearImpulse",function(impulse,point){
if(this.m_type!=Box2D.Dynamics.b2Body.b2_dynamicBody)
{
return;
}
if(this.IsAwake()==false)
{
this.SetAwake(true);
}
this.m_linearVelocity.x+=this.m_invMass*impulse.x;
this.m_linearVelocity.y+=this.m_invMass*impulse.y;
this.m_angularVelocity+=this.m_invI*((point.x-this.m_sweep.c.x)*impulse.y-(point.y-this.m_sweep.c.y)*impulse.x);
},
"public function ApplyAngularImpulse",function(impulse)
{
if(this.m_type!=Box2D.Dynamics.b2Body.b2_dynamicBody)
return;
if(this.IsAwake()==false)
this.SetAwake(true);
this.m_angularVelocity+=this.m_invI*impulse;
},
"public function Split",function(callback)
{
var linearVelocity=this.GetLinearVelocity().Copy();
var angularVelocity=this.GetAngularVelocity();
var center=this.GetWorldCenter();
var body1=this;
var body2=this.m_world.CreateBody(this.GetDefinition());
var prev;
for(var f=body1.m_fixtureList;f;)
{
if(callback(f))
{
var next=f.m_next;
if(prev)
{
prev.m_next=next;
}else{
body1.m_fixtureList=next;
}
body1.m_fixtureCount--;
f.m_next=body2.m_fixtureList;
body2.m_fixtureList=f;
body2.m_fixtureCount++;
f.m_body=body2;
f=next;
}else{
prev=f;
f=f.m_next;
}
}
body1.ResetMassData();
body2.ResetMassData();
var center1=body1.GetWorldCenter();
var center2=body2.GetWorldCenter();
var velocity1=Box2D.Common.Math.b2Math.AddVV(linearVelocity,
Box2D.Common.Math.b2Math.CrossFV(angularVelocity,
Box2D.Common.Math.b2Math.SubtractVV(center1,center)));
var velocity2=Box2D.Common.Math.b2Math.AddVV(linearVelocity,
Box2D.Common.Math.b2Math.CrossFV(angularVelocity,
Box2D.Common.Math.b2Math.SubtractVV(center2,center)));
body1.SetLinearVelocity(velocity1);
body2.SetLinearVelocity(velocity2);
body1.SetAngularVelocity(angularVelocity);
body2.SetAngularVelocity(angularVelocity);
body1.SynchronizeFixtures();
body2.SynchronizeFixtures();
return body2;
},
"public function Merge",function(other)
{
this.m_world.CheckUnlocked();
var body1=this;
var body2=other;
var center1=body1.GetWorldCenter();
var center2=body2.GetWorldCenter();
var v1=body1.GetLinearVelocity();
var v2=body2.GetLinearVelocity();
var m1=body1.GetMass();
var m2=body2.GetMass();
var c=Box2D.Common.Math.b2Math.AddVV(
Box2D.Common.Math.b2Math.MulFV(m1,center1),
Box2D.Common.Math.b2Math.MulFV(m2,center2));
var mass=m1+m2;
c.x/=mass;
c.y/=mass;
var v=Box2D.Common.Math.b2Math.AddVV(
Box2D.Common.Math.b2Math.MulFV(m1,v1),
Box2D.Common.Math.b2Math.MulFV(m2,v2));
var r1=Box2D.Common.Math.b2Math.SubtractVV(center1,c);
var r2=Box2D.Common.Math.b2Math.SubtractVV(center2,c);
var angularMomentum=
body1.GetAngularVelocity()*body1.GetInertia()+m1*Box2D.Common.Math.b2Math.CrossVV(r1,v1);
body2.GetAngularVelocity()*body2.GetInertia()+m2*Box2D.Common.Math.b2Math.CrossVV(r2,v2);
var I=
body1.GetInertia()+m1*r1.LengthSquared()+
body2.GetInertia()+m2*r2.LengthSquared();
var xf=Box2D.Common.Math.b2Math.MulXX(
body1.GetTransform().GetInverse(),
body2.GetTransform());
var f;
for(f=other.m_fixtureList;f;f=f.m_next)
{
var fd=f.GetDefinition();
fd.shape.MulBy(xf);
body1.CreateFixture(fd);
}
var md=new Box2D.Collision.Shapes.b2MassData();
md.center=Box2D.Common.Math.b2Math.MulXT(body1.GetTransform(),c);
md.I=I+mass*md.center.LengthSquared();
md.mass=mass;
this.SetMassData(md);
this.m_linearVelocity.x=(v1.x*m1+v2.x*m2)/mass;
this.m_linearVelocity.y=(v1.y*m1+v2.y*m2)/mass;
this.m_angularVelocity=angularMomentum/I;
this.m_world.DestroyBody(body2);
this.SynchronizeFixtures();
},
"public function GetMass",function(){
return this.m_mass;
},
"public function GetInertia",function(){
return this.m_I;
},
"public function GetMassData",function(data)
{
data.mass=this.m_mass;
data.I=this.m_I;
data.center.SetV(this.m_sweep.localCenter);
},
"public function SetMassData",function(massData)
{
this.m_world.CheckUnlocked();
if(this.m_type!=Box2D.Dynamics.b2Body.b2_dynamicBody)
{
return;
}
this.m_invMass=0.0;
this.m_I=0.0;
this.m_invI=0.0;
this.m_mass=massData.mass;
if(this.m_mass<=0.0)
{
this.m_mass=1.0;
}
this.m_invMass=1.0/this.m_mass;
if(massData.I>0.0&&(this.m_flags&Box2D.Dynamics.b2Body.e_fixedRotationFlag)==0)
{
this.m_I=massData.I-this.m_mass*(massData.center.x*massData.center.x+massData.center.y*massData.center.y);
this.m_invI=1.0/this.m_I;
}
var oldCenter=this.m_sweep.c.Copy();
this.m_sweep.localCenter.SetV(massData.center);
this.m_sweep.c0.SetV(Box2D.Common.Math.b2Math.MulX(this.m_xf,this.m_sweep.localCenter));
this.m_sweep.c.SetV(this.m_sweep.c0);
this.m_linearVelocity.x+=this.m_angularVelocity*-(this.m_sweep.c.y-oldCenter.y);
this.m_linearVelocity.y+=this.m_angularVelocity*+(this.m_sweep.c.x-oldCenter.x);
},
"public function ResetMassData",function()
{
this.m_mass=0.0;
this.m_invMass=0.0;
this.m_I=0.0;
this.m_invI=0.0;
this.m_sweep.localCenter.SetZero();
var center=Box2D.Common.Math.b2Vec2.Make(0,0);
if(this.m_type==Box2D.Dynamics.b2Body.b2_staticBody||this.m_type==Box2D.Dynamics.b2Body.b2_kinematicBody)
{
this.m_sweep.c0.x=this.m_sweep.c.x=this.m_xf.position.x;
this.m_sweep.c0.y=this.m_sweep.c.y=this.m_xf.position.y;
return;
}
{
for(var f=this.m_fixtureList;f;f=f.m_next)
{
if(f.m_density==0.0)
{
continue;
}
var massData=f.GetMassData();
this.m_mass+=massData.mass;
center.x+=massData.center.x*massData.mass;
center.y+=massData.center.y*massData.mass;
this.m_I+=massData.I;
}
if(this.m_mass>0.0)
{
this.m_invMass=1.0/this.m_mass;
center.x*=this.m_invMass;
center.y*=this.m_invMass;
}
else
{
this.m_mass=1.0;
this.m_invMass=1.0;
}
if(this.m_I>0.0&&(this.m_flags&Box2D.Dynamics.b2Body.e_fixedRotationFlag)==0)
{
this.m_I-=this.m_mass*(center.x*center.x+center.y*center.y);
this.m_I*=this.m_inertiaScale;
this.m_invI=1.0/this.m_I;
}else{
this.m_I=0.0;
this.m_invI=0.0;
}
}
var oldCenter=this.m_sweep.c.Copy();
this.m_sweep.localCenter.SetV(center);
this.m_sweep.c0.SetV(Box2D.Common.Math.b2Math.MulX(this.m_xf,this.m_sweep.localCenter));
this.m_sweep.c.SetV(this.m_sweep.c0);
this.m_linearVelocity.x+=this.m_angularVelocity*-(this.m_sweep.c.y-oldCenter.y);
this.m_linearVelocity.y+=this.m_angularVelocity*+(this.m_sweep.c.x-oldCenter.x);
},
"public function GetWorldPoint",function(localPoint){
var A=this.m_xf.R;
var u=new Box2D.Common.Math.b2Vec2(A.col1.x*localPoint.x+A.col2.x*localPoint.y,
A.col1.y*localPoint.x+A.col2.y*localPoint.y);
u.x+=this.m_xf.position.x;
u.y+=this.m_xf.position.y;
return u;
},
"public function GetWorldVector",function(localVector){
return Box2D.Common.Math.b2Math.MulMV(this.m_xf.R,localVector);
},
"public function GetLocalPoint",function(worldPoint){
return Box2D.Common.Math.b2Math.MulXT(this.m_xf,worldPoint);
},
"public function GetLocalVector",function(worldVector){
return Box2D.Common.Math.b2Math.MulTMV(this.m_xf.R,worldVector);
},
"public function GetLinearVelocityFromWorldPoint",function(worldPoint)
{
return new Box2D.Common.Math.b2Vec2(this.m_linearVelocity.x-this.m_angularVelocity*(worldPoint.y-this.m_sweep.c.y),
this.m_linearVelocity.y+this.m_angularVelocity*(worldPoint.x-this.m_sweep.c.x));
},
"public function GetLinearVelocityFromLocalPoint",function(localPoint)
{
var A=this.m_xf.R;
var worldPoint=new Box2D.Common.Math.b2Vec2(A.col1.x*localPoint.x+A.col2.x*localPoint.y,
A.col1.y*localPoint.x+A.col2.y*localPoint.y);
worldPoint.x+=this.m_xf.position.x;
worldPoint.y+=this.m_xf.position.y;
return new Box2D.Common.Math.b2Vec2(this.m_linearVelocity.x-this.m_angularVelocity*(worldPoint.y-this.m_sweep.c.y),
this.m_linearVelocity.y+this.m_angularVelocity*(worldPoint.x-this.m_sweep.c.x));
},
"public function GetLinearDamping",function()
{
return this.m_linearDamping;
},
"public function SetLinearDamping",function(linearDamping)
{
this.m_linearDamping=linearDamping;
},
"public function GetAngularDamping",function()
{
return this.m_angularDamping;
},
"public function SetAngularDamping",function(angularDamping)
{
this.m_angularDamping=angularDamping;
},
"public function SetType",function(type)
{
if(this.m_type==type)
{
return;
}
this.m_type=type;
this.ResetMassData();
if(this.m_type==Box2D.Dynamics.b2Body.b2_staticBody)
{
this.m_linearVelocity.SetZero();
this.m_angularVelocity=0.0;
}
this.SetAwake(true);
this.m_force.SetZero();
this.m_torque=0.0;
for(var ce=this.m_contactList;ce;ce=ce.next)
{
ce.contact.FlagForFiltering();
}
this.SynchronizeTransform();
},
"public function GetType",function()
{
return this.m_type;
},
"public function SetBullet",function(flag){
if(flag)
{
this.m_flags|=Box2D.Dynamics.b2Body.e_bulletFlag;
}
else
{
this.m_flags&=~Box2D.Dynamics.b2Body.e_bulletFlag;
}
},
"public function IsBullet",function(){
return(this.m_flags&Box2D.Dynamics.b2Body.e_bulletFlag)==Box2D.Dynamics.b2Body.e_bulletFlag;
},
"public function SetSleepingAllowed",function(flag){
if(flag)
{
this.m_flags|=Box2D.Dynamics.b2Body.e_allowSleepFlag;
}
else
{
this.m_flags&=~Box2D.Dynamics.b2Body.e_allowSleepFlag;
this.SetAwake(true);
}
},
"public function SetAwake",function(flag){
if(flag)
{
this.m_flags|=Box2D.Dynamics.b2Body.e_awakeFlag;
this.m_sleepTime=0.0;
}
else
{
this.m_flags&=~Box2D.Dynamics.b2Body.e_awakeFlag;
this.m_sleepTime=0.0;
this.m_linearVelocity.SetZero();
this.m_angularVelocity=0.0;
this.m_force.SetZero();
this.m_torque=0.0;
}
},
"public function IsAwake",function(){
return(this.m_flags&Box2D.Dynamics.b2Body.e_awakeFlag)==Box2D.Dynamics.b2Body.e_awakeFlag;
},
"public function SetFixedRotation",function(fixed)
{
if(fixed)
{
this.m_flags|=Box2D.Dynamics.b2Body.e_fixedRotationFlag;
}
else
{
this.m_flags&=~Box2D.Dynamics.b2Body.e_fixedRotationFlag;
}
this.ResetMassData();
},
"public function GetFixedRotation",function()
{
return(this.m_flags&Box2D.Dynamics.b2Body.e_fixedRotationFlag)>0;
},
"public function IsFixedRotation",function()
{
return(this.m_flags&Box2D.Dynamics.b2Body.e_fixedRotationFlag)==Box2D.Dynamics.b2Body.e_fixedRotationFlag;
},
"public function SetActive",function(flag){
if(flag==this.IsActive())
{
return;
}
var broadPhase;
var f;
if(flag)
{
this.m_flags|=Box2D.Dynamics.b2Body.e_activeFlag;
broadPhase=this.m_world.m_contactManager.m_broadPhase;
for(f=this.m_fixtureList;f;f=f.m_next)
{
f.CreateProxy(broadPhase,this.m_xf);
}
}
else
{
this.m_flags&=~Box2D.Dynamics.b2Body.e_activeFlag;
broadPhase=this.m_world.m_contactManager.m_broadPhase;
for(f=this.m_fixtureList;f;f=f.m_next)
{
f.DestroyProxy(broadPhase);
}
var ce=this.m_contactList;
while(ce)
{
var ce0=ce;
ce=ce.next;
this.m_world.m_contactManager.Destroy(ce0.contact);
}
this.m_contactList=null;
}
},
"public function IsActive",function(){
return(this.m_flags&Box2D.Dynamics.b2Body.e_activeFlag)==Box2D.Dynamics.b2Body.e_activeFlag;
},
"public function IsSleepingAllowed",function()
{
return(this.m_flags&Box2D.Dynamics.b2Body.e_allowSleepFlag)==Box2D.Dynamics.b2Body.e_allowSleepFlag;
},
"public function GetFixtureList",function(){
return this.m_fixtureList;
},
"public function GetJointList",function(){
return this.m_jointList;
},
"public function GetContactList",function(){
return this.m_contactList;
},
"public function GetNext",function(){
return this.m_next;
},
"public function GetUserData",function(){
return this.m_userData$1;
},
"public function SetUserData",function(data)
{
this.m_userData$1=data;
},
"public function GetEventDispatcher",function(){
return this.m_eventDispatcher;
},
"public function SetEventDispatcher",function(dispatcher)
{
this.m_eventDispatcher=dispatcher;
},
"public function GetWorld",function()
{
return this.m_world;
},
"public function b2Body",function(bd,world){this.m_xf=this.m_xf();this.m_sweep=this.m_sweep();this.m_linearVelocity=this.m_linearVelocity();this.m_force=this.m_force();
this.m_flags=0;
if(bd.bullet)
{
this.m_flags|=Box2D.Dynamics.b2Body.e_bulletFlag;
}
if(bd.fixedRotation)
{
this.m_flags|=Box2D.Dynamics.b2Body.e_fixedRotationFlag;
}
if(bd.allowSleep)
{
this.m_flags|=Box2D.Dynamics.b2Body.e_allowSleepFlag;
}
if(bd.awake)
{
this.m_flags|=Box2D.Dynamics.b2Body.e_awakeFlag;
}
if(bd.active)
{
this.m_flags|=Box2D.Dynamics.b2Body.e_activeFlag;
}
this.m_world=world;
this.m_xf.position.SetV(bd.position);
this.m_xf.R.Set(bd.angle);
this.m_sweep.localCenter.SetZero();
this.m_sweep.t0=1.0;
this.m_sweep.a0=this.m_sweep.a=bd.angle;
var tMat=this.m_xf.R;
var tVec=this.m_sweep.localCenter;
this.m_sweep.c.x=(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
this.m_sweep.c.y=(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
this.m_sweep.c.x+=this.m_xf.position.x;
this.m_sweep.c.y+=this.m_xf.position.y;
this.m_sweep.c0.SetV(this.m_sweep.c);
this.m_jointList=null;
this.m_contactList=null;
this.m_prev=null;
this.m_next=null;
this.m_linearVelocity.SetV(bd.linearVelocity);
this.m_angularVelocity=bd.angularVelocity;
this.m_linearDamping=bd.linearDamping;
this.m_angularDamping=bd.angularDamping;
this.m_force.Set(0.0,0.0);
this.m_torque=0.0;
this.m_sleepTime=0.0;
this.m_type=bd.type;
if(this.m_type==Box2D.Dynamics.b2Body.b2_dynamicBody)
{
this.m_mass=1.0;
this.m_invMass=1.0;
}
else
{
this.m_mass=0.0;
this.m_invMass=0.0;
}
this.m_I=0.0;
this.m_invI=0.0;
this.m_inertiaScale=bd.inertiaScale;
this.m_userData$1=bd.userData;
this.m_fixtureList=null;
this.m_fixtureCount=0;
},
"static private var",{s_xf1:function(){return(new Box2D.Common.Math.b2Transform());}},
"b2internal function SynchronizeFixtures",function(){
var xf1=$$private.s_xf1;
xf1.R.Set(this.m_sweep.a0);
var tMat=xf1.R;
var tVec=this.m_sweep.localCenter;
xf1.position.x=this.m_sweep.c0.x-(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
xf1.position.y=this.m_sweep.c0.y-(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
var f;
var broadPhase=this.m_world.m_contactManager.m_broadPhase;
for(f=this.m_fixtureList;f;f=f.m_next)
{
f.Synchronize(broadPhase,xf1,this.m_xf);
}
},
"b2internal function SynchronizeTransform",function(){
this.m_xf.R.Set(this.m_sweep.a);
var tMat=this.m_xf.R;
var tVec=this.m_sweep.localCenter;
this.m_xf.position.x=this.m_sweep.c.x-(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
this.m_xf.position.y=this.m_sweep.c.y-(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
},
"b2internal function ShouldCollide",function(other){
if(this.m_type!=Box2D.Dynamics.b2Body.b2_dynamicBody&&other.m_type!=Box2D.Dynamics.b2Body.b2_dynamicBody)
{
return false;
}
for(var jn=this.m_jointList;jn;jn=jn.next)
{
if(jn.other==other)
if(jn.joint.m_collideConnected==false)
{
return false;
}
}
return true;
},
"b2internal function Advance",function(t){
this.m_sweep.Advance(t);
this.m_sweep.c.SetV(this.m_sweep.c0);
this.m_sweep.a=this.m_sweep.a0;
this.SynchronizeTransform();
},
"b2internal var",{m_flags:0},
"b2internal var",{m_type:0},
"b2internal var",{m_islandIndex:0},
"b2internal var",{m_xf:function(){return(new Box2D.Common.Math.b2Transform());}},
"b2internal var",{m_sweep:function(){return(new Box2D.Common.Math.b2Sweep());}},
"b2internal var",{m_linearVelocity:function(){return(new Box2D.Common.Math.b2Vec2());}},
"b2internal var",{m_angularVelocity:NaN},
"b2internal var",{m_force:function(){return(new Box2D.Common.Math.b2Vec2());}},
"b2internal var",{m_torque:NaN},
"b2internal var",{m_world:null},
"b2internal var",{m_prev:null},
"b2internal var",{m_next:null},
"b2internal var",{m_fixtureList:null},
"b2internal var",{m_fixtureCount:0},
"b2internal var",{m_jointList:null},
"b2internal var",{m_contactList:null},
"b2internal var",{m_mass:NaN,m_invMass:NaN},
"b2internal var",{m_I:NaN,m_invI:NaN},
"b2internal var",{m_inertiaScale:NaN},
"b2internal var",{m_linearDamping:NaN},
"b2internal var",{m_angularDamping:NaN},
"b2internal var",{m_sleepTime:NaN},
"private var",{m_userData:undefined},
"b2internal var",{m_eventDispatcher:null},
"static b2internal var",{e_islandFlag:0x0001},
"static b2internal var",{e_awakeFlag:0x0002},
"static b2internal var",{e_allowSleepFlag:0x0004},
"static b2internal var",{e_bulletFlag:0x0008},
"static b2internal var",{e_fixedRotationFlag:0x0010},
"static b2internal var",{e_activeFlag:0x0020},
"static public var",{b2_staticBody:0},
"static public var",{b2_kinematicBody:1},
"static public var",{b2_dynamicBody:2},
];},[],["Box2D.Collision.Shapes.b2PolyLine","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.b2Fixture","Box2D.Dynamics.b2World","Box2D.Dynamics.b2FixtureDef","Error","Box2D.Dynamics.b2BodyDef","Box2D.Common.Math.b2Math","Box2D.Collision.Shapes.b2MassData","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Transform","Box2D.Common.Math.b2Sweep"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2BodyDef
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2BodyDef",1,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body);},

"public function b2BodyDef",function()
{this.position=this.position();this.linearVelocity=this.linearVelocity();
this.userData=null;
this.position.Set(0.0,0.0);
this.angle=0.0;
this.linearVelocity.Set(0,0);
this.angularVelocity=0.0;
this.linearDamping=0.0;
this.angularDamping=0.0;
this.allowSleep=true;
this.awake=true;
this.fixedRotation=false;
this.bullet=false;
this.type=Box2D.Dynamics.b2Body.b2_staticBody;
this.active=true;
this.inertiaScale=1.0;
},
"public var",{type:0},
"public var",{position:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{angle:NaN},
"public var",{linearVelocity:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{angularVelocity:NaN},
"public var",{linearDamping:NaN},
"public var",{angularDamping:NaN},
"public var",{allowSleep:false},
"public var",{awake:false},
"public var",{fixedRotation:false},
"public var",{bullet:false},
"public var",{active:false},
"public var",{userData:undefined},
"public var",{inertiaScale:NaN},
];},[],["Box2D.Dynamics.b2Body","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2BodyEvent
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2BodyEvent extends flash.events.Event",2,function($$private){;return[

"public var",{body:null},
"function b2BodyEvent",function(type)
{
this.super$2(type);
},
"override public function clone",function()
{
var event=new Box2D.Dynamics.b2BodyEvent(this.type);
event.body=this.body;
return event;
},
];},[],["flash.events.Event"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2ContactEvent
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2ContactEvent extends flash.events.Event",2,function($$private){;return[

"public var",{contact:null},
"function b2ContactEvent",function(type)
{
this.super$2(type);
},
"override public function clone",function()
{
var event=new Box2D.Dynamics.b2ContactEvent(this.type);
event.contact=this.contact;
return event;
},
];},[],["flash.events.Event"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2ContactFilter
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2ContactFilter",1,function($$private){;return[

"public virtual function ShouldCollide",function(fixtureA,fixtureB){
var filter1=fixtureA.GetFilterData();
var filter2=fixtureB.GetFilterData();
if(filter1.groupIndex==filter2.groupIndex&&filter1.groupIndex!=0)
{
return filter1.groupIndex>0;
}
var collide=(filter1.maskBits&filter2.categoryBits)!=0&&(filter1.categoryBits&filter2.maskBits)!=0;
return collide;
},
"static b2internal var",{b2_defaultFilter:function(){return(new Box2D.Dynamics.b2ContactFilter());}},
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2ContactImpulse
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2ContactImpulse",1,function($$private){;return[function(){joo.classLoader.init(Box2D.Common.b2Settings);},

"public var",{normalImpulses:function(){return(new Array(Box2D.Common.b2Settings.b2_maxManifoldPoints));}},
"public var",{tangentImpulses:function(){return(new Array(Box2D.Common.b2Settings.b2_maxManifoldPoints));}},
"public function b2ContactImpulse",function b2ContactImpulse$(){this.normalImpulses=this.normalImpulses();this.tangentImpulses=this.tangentImpulses();}];},[],["Array","Box2D.Common.b2Settings"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2ContactManager
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2ContactManager",1,function($$private){var as=joo.as,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(Box2D.Dynamics.b2ContactFilter,Box2D.Dynamics.Contacts.b2Contact);},

"public function b2ContactManager",function(world){
this.m_world=world;
this.m_contactCount=0;
this.m_contactFilter=Box2D.Dynamics.b2ContactFilter.b2_defaultFilter;
this.m_contactListener=new Box2D.Dynamics.b2DefaultContactListener(world);
this.m_contactFactory=new Box2D.Dynamics.Contacts.b2ContactFactory(this.m_allocator);
this.m_broadPhase=new Box2D.Collision.b2DynamicTreeBroadPhase();
},
"public function AddPair",function(proxyUserDataA,proxyUserDataB){
var fixtureA=as(proxyUserDataA,Box2D.Dynamics.b2Fixture);
var fixtureB=as(proxyUserDataB,Box2D.Dynamics.b2Fixture);
var bodyA=fixtureA.GetBody();
var bodyB=fixtureB.GetBody();
if(bodyA==bodyB)
return;
var edge=bodyB.GetContactList();
while(edge)
{
if(edge.other==bodyA)
{
var fA=edge.contact.GetFixtureA();
var fB=edge.contact.GetFixtureB();
if(fA==fixtureA&&fB==fixtureB)
return;
if(fA==fixtureB&&fB==fixtureA)
return;
}
edge=edge.next;
}
if(bodyB.ShouldCollide(bodyA)==false)
{
return;
}
if(this.m_contactFilter&&this.m_contactFilter.ShouldCollide(fixtureA,fixtureB)==false)
{
return;
}
var c=this.m_contactFactory.Create(fixtureA,fixtureB);
fixtureA=c.GetFixtureA();
fixtureB=c.GetFixtureB();
bodyA=fixtureA.m_body;
bodyB=fixtureB.m_body;
c.m_prev=null;
c.m_next=this.m_world.m_contactList;
if(this.m_world.m_contactList!=null)
{
this.m_world.m_contactList.m_prev=c;
}
this.m_world.m_contactList=c;
c.m_nodeA.contact=c;
c.m_nodeA.other=bodyB;
c.m_nodeA.prev=null;
c.m_nodeA.next=bodyA.m_contactList;
if(bodyA.m_contactList!=null)
{
bodyA.m_contactList.prev=c.m_nodeA;
}
bodyA.m_contactList=c.m_nodeA;
c.m_nodeB.contact=c;
c.m_nodeB.other=bodyA;
c.m_nodeB.prev=null;
c.m_nodeB.next=bodyB.m_contactList;
if(bodyB.m_contactList!=null)
{
bodyB.m_contactList.prev=c.m_nodeB;
}
bodyB.m_contactList=c.m_nodeB;
++this.m_contactCount;
return;
},
"public function FindNewContacts",function()
{
this.m_broadPhase.UpdatePairs($$bound(this,"AddPair"));
},
"static private const",{s_evalCP:function(){return(new Box2D.Collision.b2ContactPoint());}},
"public function Destroy",function(c)
{
var fixtureA=c.GetFixtureA();
var fixtureB=c.GetFixtureB();
var bodyA=fixtureA.GetBody();
var bodyB=fixtureB.GetBody();
if(this.m_contactListener&&c.IsTouching())
{
this.m_contactListener.EndContact(c);
}
if(c.m_prev)
{
c.m_prev.m_next=c.m_next;
}
if(c.m_next)
{
c.m_next.m_prev=c.m_prev;
}
if(c==this.m_world.m_contactList)
{
this.m_world.m_contactList=c.m_next;
}
if(c.m_nodeA.prev)
{
c.m_nodeA.prev.next=c.m_nodeA.next;
}
if(c.m_nodeA.next)
{
c.m_nodeA.next.prev=c.m_nodeA.prev;
}
if(c.m_nodeA==bodyA.m_contactList)
{
bodyA.m_contactList=c.m_nodeA.next;
}
if(c.m_nodeB.prev)
{
c.m_nodeB.prev.next=c.m_nodeB.next;
}
if(c.m_nodeB.next)
{
c.m_nodeB.next.prev=c.m_nodeB.prev;
}
if(c.m_nodeB==bodyB.m_contactList)
{
bodyB.m_contactList=c.m_nodeB.next;
}
this.m_contactFactory.Destroy(c);
--this.m_contactCount;
},
"public function Collide",function()
{
var c=this.m_world.m_contactList;
while(c)
{
var fixtureA=c.GetFixtureA();
var fixtureB=c.GetFixtureB();
var bodyA=fixtureA.GetBody();
var bodyB=fixtureB.GetBody();
if(bodyA.IsAwake()==false&&bodyB.IsAwake()==false)
{
c=c.GetNext();
continue;
}
if(c.m_flags&Box2D.Dynamics.Contacts.b2Contact.e_filterFlag)
{
if(bodyB.ShouldCollide(bodyA)==false)
{
var cNuke=c;
c=cNuke.GetNext();
this.Destroy(cNuke);
continue;
}
if(this.m_contactFilter&&this.m_contactFilter.ShouldCollide(fixtureA,fixtureB)==false)
{
cNuke=c;
c=cNuke.GetNext();
this.Destroy(cNuke);
continue;
}
c.m_flags&=~Box2D.Dynamics.Contacts.b2Contact.e_filterFlag;
}
var proxyA=fixtureA.m_proxy;
var proxyB=fixtureB.m_proxy;
var overlap=this.m_broadPhase.TestOverlap(proxyA,proxyB);
if(overlap==false)
{
cNuke=c;
c=cNuke.GetNext();
this.Destroy(cNuke);
continue;
}
c.Update(this.m_contactListener);
c=c.GetNext();
}
},
"b2internal var",{m_world:null},
"b2internal var",{m_broadPhase:null},
"b2internal var",{m_contactList:null},
"b2internal var",{m_contactCount:0},
"b2internal var",{m_contactFilter:null},
"b2internal var",{m_contactListener:null},
"b2internal var",{m_contactFactory:null},
"b2internal var",{m_allocator:undefined},
];},[],["Box2D.Dynamics.b2ContactFilter","Box2D.Dynamics.b2DefaultContactListener","Box2D.Dynamics.Contacts.b2ContactFactory","Box2D.Collision.b2DynamicTreeBroadPhase","Box2D.Dynamics.b2Fixture","Box2D.Collision.b2ContactPoint","Box2D.Dynamics.Contacts.b2Contact"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2DebugDraw
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2DebugDraw",1,function($$private){;return[

"public function b2DebugDraw",function(){
this.m_drawFlags$1=0;
},
"static public var",{e_shapeBit:0x0001},
"static public var",{e_jointBit:0x0002},
"static public var",{e_aabbBit:0x0004},
"static public var",{e_pairBit:0x0008},
"static public var",{e_centerOfMassBit:0x0010},
"static public var",{e_controllerBit:0x0020},
"public function SetFlags",function(flags){
this.m_drawFlags$1=flags;
},
"public function GetFlags",function(){
return this.m_drawFlags$1;
},
"public function AppendFlags",function(flags){
this.m_drawFlags$1|=flags;
},
"public function ClearFlags",function(flags){
this.m_drawFlags$1&=~flags;
},
"public function SetSprite",function(sprite){
this.m_sprite=sprite;
},
"public function GetSprite",function(){
return this.m_sprite;
},
"public function SetDrawScale",function(drawScale){
this.m_drawScale$1=drawScale;
},
"public function GetDrawScale",function(){
return this.m_drawScale$1;
},
"public function SetLineThickness",function(lineThickness){
this.m_lineThickness$1=lineThickness;
},
"public function GetLineThickness",function(){
return this.m_lineThickness$1;
},
"public function SetAlpha",function(alpha){
this.m_alpha$1=alpha;
},
"public function GetAlpha",function(){
return this.m_alpha$1;
},
"public function SetFillAlpha",function(alpha){
this.m_fillAlpha$1=alpha;
},
"public function GetFillAlpha",function(){
return this.m_fillAlpha$1;
},
"public function SetXFormScale",function(xformScale){
this.m_xformScale$1=xformScale;
},
"public function GetXFormScale",function(){
return this.m_xformScale$1;
},
"public virtual function DrawPolygon",function(vertices,vertexCount,color){
this.m_sprite.graphics.lineStyle(this.m_lineThickness$1,color.color,this.m_alpha$1);
this.m_sprite.graphics.moveTo(vertices[0].x*this.m_drawScale$1,vertices[0].y*this.m_drawScale$1);
for(var i=1;i<vertexCount;i++){
this.m_sprite.graphics.lineTo(vertices[i].x*this.m_drawScale$1,vertices[i].y*this.m_drawScale$1);
}
this.m_sprite.graphics.lineTo(vertices[0].x*this.m_drawScale$1,vertices[0].y*this.m_drawScale$1);
},
"public virtual function DrawSolidPolygon",function(vertices,vertexCount,color){
this.m_sprite.graphics.lineStyle(this.m_lineThickness$1,color.color,this.m_alpha$1);
this.m_sprite.graphics.moveTo(vertices[0].x*this.m_drawScale$1,vertices[0].y*this.m_drawScale$1);
this.m_sprite.graphics.beginFill(color.color,this.m_fillAlpha$1);
for(var i=1;i<vertexCount;i++){
this.m_sprite.graphics.lineTo(vertices[i].x*this.m_drawScale$1,vertices[i].y*this.m_drawScale$1);
}
this.m_sprite.graphics.lineTo(vertices[0].x*this.m_drawScale$1,vertices[0].y*this.m_drawScale$1);
this.m_sprite.graphics.endFill();
},
"public virtual function DrawCircle",function(center,radius,color){
this.m_sprite.graphics.lineStyle(this.m_lineThickness$1,color.color,this.m_alpha$1);
this.m_sprite.graphics.drawCircle(center.x*this.m_drawScale$1,center.y*this.m_drawScale$1,radius*this.m_drawScale$1);
},
"public virtual function DrawSolidCircle",function(center,radius,axis,color){
this.m_sprite.graphics.lineStyle(this.m_lineThickness$1,color.color,this.m_alpha$1);
this.m_sprite.graphics.moveTo(0,0);
this.m_sprite.graphics.beginFill(color.color,this.m_fillAlpha$1);
this.m_sprite.graphics.drawCircle(center.x*this.m_drawScale$1,center.y*this.m_drawScale$1,radius*this.m_drawScale$1);
this.m_sprite.graphics.endFill();
this.m_sprite.graphics.moveTo(center.x*this.m_drawScale$1,center.y*this.m_drawScale$1);
this.m_sprite.graphics.lineTo((center.x+axis.x*radius)*this.m_drawScale$1,(center.y+axis.y*radius)*this.m_drawScale$1);
},
"public virtual function DrawSegment",function(p1,p2,color){
this.m_sprite.graphics.lineStyle(this.m_lineThickness$1,color.color,this.m_alpha$1);
this.m_sprite.graphics.moveTo(p1.x*this.m_drawScale$1,p1.y*this.m_drawScale$1);
this.m_sprite.graphics.lineTo(p2.x*this.m_drawScale$1,p2.y*this.m_drawScale$1);
},
"public virtual function DrawTransform",function(xf){
this.m_sprite.graphics.lineStyle(this.m_lineThickness$1,0xff0000,this.m_alpha$1);
this.m_sprite.graphics.moveTo(xf.position.x*this.m_drawScale$1,xf.position.y*this.m_drawScale$1);
this.m_sprite.graphics.lineTo((xf.position.x+this.m_xformScale$1*xf.R.col1.x)*this.m_drawScale$1,(xf.position.y+this.m_xformScale$1*xf.R.col1.y)*this.m_drawScale$1);
this.m_sprite.graphics.lineStyle(this.m_lineThickness$1,0x00ff00,this.m_alpha$1);
this.m_sprite.graphics.moveTo(xf.position.x*this.m_drawScale$1,xf.position.y*this.m_drawScale$1);
this.m_sprite.graphics.lineTo((xf.position.x+this.m_xformScale$1*xf.R.col2.x)*this.m_drawScale$1,(xf.position.y+this.m_xformScale$1*xf.R.col2.y)*this.m_drawScale$1);
},
"private var",{m_drawFlags:0},
"b2internal var",{m_sprite:null},
"private var",{m_drawScale:1.0},
"private var",{m_lineThickness:1.0},
"private var",{m_alpha:1.0},
"private var",{m_fillAlpha:1.0},
"private var",{m_xformScale:1.0},
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2DefaultContactListener
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2DefaultContactListener implements Box2D.Dynamics.IContactListener",1,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2World);},

"public function b2DefaultContactListener",function(world)
{this.m_beginContact$1=this.m_beginContact$1();this.m_endContact$1=this.m_endContact$1();this.m_preSolveContact$1=this.m_preSolveContact$1();this.m_postSolveContact$1=this.m_postSolveContact$1();
this.m_world$1=world;
},
"public function BeginContact",function(contact)
{
this.m_beginContact$1.contact=contact;
this.m_world$1.dispatchEvent(this.m_beginContact$1);
var dispatcher;
dispatcher=contact.m_fixtureA.m_body.m_eventDispatcher;
if(dispatcher)dispatcher.dispatchEvent(this.m_beginContact$1);
dispatcher=contact.m_fixtureB.m_body.m_eventDispatcher;
if(dispatcher)dispatcher.dispatchEvent(this.m_beginContact$1);
},
"public function EndContact",function(contact)
{
this.m_endContact$1.contact=contact;
var dispatcher;
dispatcher=contact.m_fixtureA.m_body.m_eventDispatcher;
if(dispatcher)dispatcher.dispatchEvent(this.m_endContact$1);
dispatcher=contact.m_fixtureB.m_body.m_eventDispatcher;
if(dispatcher)dispatcher.dispatchEvent(this.m_endContact$1);
this.m_world$1.dispatchEvent(this.m_endContact$1);
},
"public function PreSolve",function(contact,oldManifold)
{
this.m_preSolveContact$1.contact=contact;
this.m_preSolveContact$1.oldManifold=oldManifold;
this.m_world$1.dispatchEvent(this.m_beginContact$1);
var dispatcher;
dispatcher=contact.m_fixtureA.m_body.m_eventDispatcher;
if(dispatcher)dispatcher.dispatchEvent(this.m_preSolveContact$1);
dispatcher=contact.m_fixtureB.m_body.m_eventDispatcher;
if(dispatcher)dispatcher.dispatchEvent(this.m_preSolveContact$1);
},
"public function PostSolve",function(contact,impulse)
{
this.m_postSolveContact$1.contact=contact;
this.m_postSolveContact$1.impulse=impulse;
var dispatcher;
dispatcher=contact.m_fixtureA.m_body.m_eventDispatcher;
if(dispatcher)dispatcher.dispatchEvent(this.m_postSolveContact$1);
dispatcher=contact.m_fixtureB.m_body.m_eventDispatcher;
if(dispatcher)dispatcher.dispatchEvent(this.m_postSolveContact$1);
this.m_world$1.dispatchEvent(this.m_beginContact$1);
},
"private var",{m_beginContact:function(){return(new Box2D.Dynamics.b2ContactEvent(Box2D.Dynamics.b2World.BEGINCONTACT));}},
"private var",{m_endContact:function(){return(new Box2D.Dynamics.b2ContactEvent(Box2D.Dynamics.b2World.ENDCONTACT));}},
"private var",{m_preSolveContact:function(){return(new Box2D.Dynamics.b2PreSolveEvent(Box2D.Dynamics.b2World.PRESOLVE));}},
"private var",{m_postSolveContact:function(){return(new Box2D.Dynamics.b2PostSolveEvent(Box2D.Dynamics.b2World.POSTSOLVE));}},
"private var",{m_world:null},
];},[],["Box2D.Dynamics.IContactListener","Box2D.Dynamics.b2ContactEvent","Box2D.Dynamics.b2World","Box2D.Dynamics.b2PreSolveEvent","Box2D.Dynamics.b2PostSolveEvent"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2EmptyContactListener
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2EmptyContactListener implements Box2D.Dynamics.IContactListener",1,function($$private){;return[

"public function BeginContact",function(contact){},
"public function EndContact",function(contact){},
"public function PreSolve",function(contact,oldManifold){},
"public function PostSolve",function(contact,impulse){},
];},[],["Box2D.Dynamics.IContactListener"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2FilterData
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2FilterData",1,function($$private){;return[

"public function Copy",function(){
var copy=new Box2D.Dynamics.b2FilterData();
copy.Set(this);
return copy;
},
"public function Set",function(other)
{
this.categoryBits=other.categoryBits;
this.maskBits=other.maskBits;
this.groupIndex=other.groupIndex;
},
"public var",{categoryBits:0x0001},
"public var",{maskBits:0xFFFF},
"public var",{groupIndex:0},
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2Fixture
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2Fixture",1,function($$private){;return[

"public function GetType",function()
{
return this.m_shape.GetType();
},
"public function GetShape",function()
{
return this.m_shape;
},
"public function SetSensor",function(sensor)
{
if(this.m_isSensor==sensor)
return;
this.m_isSensor=sensor;
if(this.m_body==null)
return;
var edge=this.m_body.GetContactList();
while(edge)
{
var contact=edge.contact;
var fixtureA=contact.GetFixtureA();
var fixtureB=contact.GetFixtureB();
if(fixtureA==this||fixtureB==this)
contact.SetSensor(fixtureA.IsSensor()||fixtureB.IsSensor());
edge=edge.next;
}
},
"public function IsSensor",function()
{
return this.m_isSensor;
},
"public function SetFilterData",function(filter)
{
this.m_filter=filter.Copy();
if(!this.m_body)
return;
var edge=this.m_body.GetContactList();
while(edge)
{
var contact=edge.contact;
var fixtureA=contact.GetFixtureA();
var fixtureB=contact.GetFixtureB();
if(fixtureA==this||fixtureB==this)
contact.FlagForFiltering();
edge=edge.next;
}
},
"public function GetFilterData",function()
{
return this.m_filter.Copy();
},
"public function GetBody",function()
{
return this.m_body;
},
"public function GetNext",function()
{
return this.m_next;
},
"public function GetUserData",function()
{
return this.m_userData;
},
"public function SetUserData",function(data)
{
this.m_userData=data;
},
"public function TestPoint",function(p)
{
return this.m_shape.TestPoint(this.m_body.GetTransform(),p);
},
"public function RayCast",function(output,input)
{
return this.m_shape.RayCast(output,input,this.m_body.GetTransform());
},
"public function GetMassData",function(massData)
{if(arguments.length<1){massData=null;}
if(massData==null)
{
massData=new Box2D.Collision.Shapes.b2MassData();
}
this.m_shape.ComputeMass(massData,this.m_density);
return massData;
},
"public function SetDensity",function(density){
this.m_density=density;
},
"public function GetDensity",function(){
return this.m_density;
},
"public function GetFriction",function()
{
return this.m_friction;
},
"public function SetFriction",function(friction)
{
this.m_friction=friction;
},
"public function GetRestitution",function()
{
return this.m_restitution;
},
"public function SetRestitution",function(restitution)
{
this.m_restitution=restitution;
},
"public function GetAABB",function(){
return this.m_aabb;
},
"public function b2Fixture",function()
{this.m_filter=this.m_filter();
this.m_aabb=new Box2D.Collision.b2AABB();
this.m_userData=null;
this.m_body=null;
this.m_next=null;
this.m_shape=null;
this.m_density=0.0;
this.m_friction=0.0;
this.m_restitution=0.0;
},
"b2internal function Create",function(body,xf,def)
{
this.m_userData=def.userData;
this.m_friction=def.friction;
this.m_restitution=def.restitution;
this.m_body=body;
this.m_next=null;
this.m_filter=def.filter.Copy();
this.m_isSensor=def.isSensor;
this.m_shape=def.shape.Copy();
this.m_density=def.density;
},
"b2internal function Destroy",function()
{
this.m_shape=null;
},
"b2internal function CreateProxy",function(broadPhase,xf){
this.m_shape.ComputeAABB(this.m_aabb,xf);
this.m_proxy=broadPhase.CreateProxy(this.m_aabb,this);
},
"b2internal function DestroyProxy",function(broadPhase){
if(this.m_proxy==null)
{
return;
}
broadPhase.DestroyProxy(this.m_proxy);
this.m_proxy=null;
},
"private static var",{s_aabb1:function(){return(new Box2D.Collision.b2AABB());}},
"private static var",{s_aabb2:function(){return(new Box2D.Collision.b2AABB());}},
"private static var",{s_displacement:function(){return(new Box2D.Common.Math.b2Vec2());}},
"b2internal function Synchronize",function(broadPhase,transform1,transform2)
{
if(!this.m_proxy)
return;
var aabb1=$$private.s_aabb1;
var aabb2=$$private.s_aabb2;
this.m_shape.ComputeAABB(aabb1,transform1);
this.m_shape.ComputeAABB(aabb2,transform2);
this.m_aabb.Combine(aabb1,aabb2);
var displacement=$$private.s_displacement;
displacement.x=transform2.position.x-transform1.position.x;
displacement.y=transform2.position.y-transform1.position.y;
broadPhase.MoveProxy(this.m_proxy,this.m_aabb,displacement);
},
"public function GetDefinition",function()
{
var fd=new Box2D.Dynamics.b2FixtureDef();
fd.density=this.m_density;
fd.filter=this.m_filter.Copy();
fd.friction=this.m_friction;
fd.isSensor=this.m_isSensor;
fd.restitution=this.m_restitution;
fd.shape=this.m_shape;
fd.userData=this.m_userData;
return fd;
},
"private var",{m_massData:null},
"b2internal var",{m_aabb:null},
"b2internal var",{m_density:NaN},
"b2internal var",{m_next:null},
"b2internal var",{m_body:null},
"b2internal var",{m_shape:null},
"b2internal var",{m_friction:NaN},
"b2internal var",{m_restitution:NaN},
"b2internal var",{m_proxy:undefined},
"b2internal var",{m_filter:function(){return(new Box2D.Dynamics.b2FilterData());}},
"b2internal var",{m_isSensor:false},
"b2internal var",{m_userData:undefined},
];},[],["Box2D.Collision.Shapes.b2MassData","Box2D.Collision.b2AABB","Box2D.Common.Math.b2Vec2","Box2D.Dynamics.b2FixtureDef","Box2D.Dynamics.b2FilterData"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2FixtureDef
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2FixtureDef",1,function($$private){;return[

"public function b2FixtureDef",function()
{this.filter=this.filter();
this.shape=null;
this.userData=null;
this.friction=0.2;
this.restitution=0.0;
this.density=0.0;
this.filter.categoryBits=0x0001;
this.filter.maskBits=0xFFFF;
this.filter.groupIndex=0;
this.isSensor=false;
},
"public function Copy",function()
{
var fd=new Box2D.Dynamics.b2FixtureDef();
fd.Set(this);
return fd;
},
"public function Set",function(other)
{
this.shape=other.shape;
this.userData=other.userData;
this.friction=other.friction;
this.restitution=other.restitution;
this.density=other.density;
this.isSensor=other.isSensor;
this.filter.Set(other.filter);
},
"public var",{shape:null},
"public var",{userData:undefined},
"public var",{friction:NaN},
"public var",{restitution:NaN},
"public var",{density:NaN},
"public var",{isSensor:false},
"public var",{filter:function(){return(new Box2D.Dynamics.b2FilterData());}},
];},[],["Box2D.Dynamics.b2FilterData"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2FixtureEvent
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2FixtureEvent extends flash.events.Event",2,function($$private){;return[

"public var",{fixture:null},
"function b2FixtureEvent",function(type)
{
this.super$2(type);
},
"override public function clone",function()
{
var event=new Box2D.Dynamics.b2FixtureEvent(this.type);
event.fixture=this.fixture;
return event;
},
];},[],["flash.events.Event"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2Island
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2Island",1,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Number,Box2D.Common.b2Settings);},

"public function b2Island",function()
{
this.m_bodies=new Array();
this.m_contacts=new Array();
this.m_joints=new Array();
},
"public function Initialize",function(
bodyCapacity,
contactCapacity,
jointCapacity,
allocator,
listener,
contactSolver)
{
var i;
this.m_bodyCapacity$1=bodyCapacity;
this.m_contactCapacity=contactCapacity;
this.m_jointCapacity=jointCapacity;
this.m_bodyCount=0;
this.m_contactCount=0;
this.m_jointCount=0;
this.m_allocator$1=allocator;
this.m_listener$1=listener;
this.m_contactSolver$1=contactSolver;
for(i=this.m_bodies.length;i<bodyCapacity;i++)
this.m_bodies[i]=null;
for(i=this.m_contacts.length;i<contactCapacity;i++)
this.m_contacts[i]=null;
for(i=this.m_joints.length;i<jointCapacity;i++)
this.m_joints[i]=null;
},
"public function Clear",function()
{
this.m_bodyCount=0;
this.m_contactCount=0;
this.m_jointCount=0;
},
"public function Solve",function(step,gravity,allowSleep)
{
var i;
var j;
var b;
var joint;
for(i=0;i<this.m_bodyCount;++i)
{
b=this.m_bodies[i];
if(b.GetType()!=Box2D.Dynamics.b2Body.b2_dynamicBody)
continue;
b.m_linearVelocity.x+=step.dt*(gravity.x+b.m_invMass*b.m_force.x);
b.m_linearVelocity.y+=step.dt*(gravity.y+b.m_invMass*b.m_force.y);
b.m_angularVelocity+=step.dt*b.m_invI*b.m_torque;
b.m_linearVelocity.Multiply(Box2D.Common.Math.b2Math.Clamp(1.0-step.dt*b.m_linearDamping,0.0,1.0));
b.m_angularVelocity*=Box2D.Common.Math.b2Math.Clamp(1.0-step.dt*b.m_angularDamping,0.0,1.0);
}
this.m_contactSolver$1.Initialize(step,this.m_contacts,this.m_contactCount,this.m_allocator$1);
var contactSolver=this.m_contactSolver$1;
contactSolver.InitVelocityConstraints(step);
for(i=0;i<this.m_jointCount;++i)
{
joint=this.m_joints[i];
joint.InitVelocityConstraints(step);
}
for(i=0;i<step.velocityIterations;++i)
{
for(j=0;j<this.m_jointCount;++j)
{
joint=this.m_joints[j];
joint.SolveVelocityConstraints(step);
}
contactSolver.SolveVelocityConstraints();
}
for(i=0;i<this.m_jointCount;++i)
{
joint=this.m_joints[i];
joint.FinalizeVelocityConstraints();
}
contactSolver.FinalizeVelocityConstraints();
for(i=0;i<this.m_bodyCount;++i)
{
b=this.m_bodies[i];
if(b.GetType()==Box2D.Dynamics.b2Body.b2_staticBody)
continue;
var translationX=step.dt*b.m_linearVelocity.x;
var translationY=step.dt*b.m_linearVelocity.y;
if((translationX*translationX+translationY*translationY)>Box2D.Common.b2Settings.b2_maxTranslationSquared)
{
b.m_linearVelocity.Normalize();
b.m_linearVelocity.x*=Box2D.Common.b2Settings.b2_maxTranslation*step.inv_dt;
b.m_linearVelocity.y*=Box2D.Common.b2Settings.b2_maxTranslation*step.inv_dt;
}
var rotation=step.dt*b.m_angularVelocity;
if(rotation*rotation>Box2D.Common.b2Settings.b2_maxRotationSquared)
{
if(b.m_angularVelocity<0.0)
{
b.m_angularVelocity=-Box2D.Common.b2Settings.b2_maxRotation*step.inv_dt;
}
else
{
b.m_angularVelocity=Box2D.Common.b2Settings.b2_maxRotation*step.inv_dt;
}
}
b.m_sweep.c0.SetV(b.m_sweep.c);
b.m_sweep.a0=b.m_sweep.a;
b.m_sweep.c.x+=step.dt*b.m_linearVelocity.x;
b.m_sweep.c.y+=step.dt*b.m_linearVelocity.y;
b.m_sweep.a+=step.dt*b.m_angularVelocity;
b.SynchronizeTransform();
}
for(i=0;i<step.positionIterations;++i)
{
var contactsOkay=contactSolver.SolvePositionConstraints(Box2D.Common.b2Settings.b2_contactBaumgarte);
var jointsOkay=true;
for(j=0;j<this.m_jointCount;++j)
{
joint=this.m_joints[j];
var jointOkay=joint.SolvePositionConstraints(Box2D.Common.b2Settings.b2_contactBaumgarte);
jointsOkay=jointsOkay&&jointOkay;
}
if(contactsOkay&&jointsOkay)
{
break;
}
}
this.Report(contactSolver.m_constraints);
if(allowSleep){
var minSleepTime=Number.MAX_VALUE;
var linTolSqr=Box2D.Common.b2Settings.b2_linearSleepTolerance*Box2D.Common.b2Settings.b2_linearSleepTolerance;
var angTolSqr=Box2D.Common.b2Settings.b2_angularSleepTolerance*Box2D.Common.b2Settings.b2_angularSleepTolerance;
for(i=0;i<this.m_bodyCount;++i)
{
b=this.m_bodies[i];
if(b.GetType()==Box2D.Dynamics.b2Body.b2_staticBody)
{
continue;
}
if((b.m_flags&Box2D.Dynamics.b2Body.e_allowSleepFlag)==0)
{
b.m_sleepTime=0.0;
minSleepTime=0.0;
}
if((b.m_flags&Box2D.Dynamics.b2Body.e_allowSleepFlag)==0||
b.m_angularVelocity*b.m_angularVelocity>angTolSqr||
Box2D.Common.Math.b2Math.Dot(b.m_linearVelocity,b.m_linearVelocity)>linTolSqr)
{
b.m_sleepTime=0.0;
minSleepTime=0.0;
}
else
{
b.m_sleepTime+=step.dt;
minSleepTime=Box2D.Common.Math.b2Math.Min(minSleepTime,b.m_sleepTime);
}
}
if(minSleepTime>=Box2D.Common.b2Settings.b2_timeToSleep)
{
for(i=0;i<this.m_bodyCount;++i)
{
b=this.m_bodies[i];
b.SetAwake(false);
}
}
}
},
"public function SolveTOI",function(subStep)
{
var i;
var j;
this.m_contactSolver$1.Initialize(subStep,this.m_contacts,this.m_contactCount,this.m_allocator$1);
var contactSolver=this.m_contactSolver$1;
for(i=0;i<this.m_jointCount;++i)
{
this.m_joints[i].InitVelocityConstraints(subStep);
}
for(i=0;i<subStep.velocityIterations;++i)
{
contactSolver.SolveVelocityConstraints();
for(j=0;j<this.m_jointCount;++j)
{
this.m_joints[j].SolveVelocityConstraints(subStep);
}
}
for(i=0;i<this.m_bodyCount;++i)
{
var b=this.m_bodies[i];
if(b.GetType()==Box2D.Dynamics.b2Body.b2_staticBody)
continue;
var translationX=subStep.dt*b.m_linearVelocity.x;
var translationY=subStep.dt*b.m_linearVelocity.y;
if((translationX*translationX+translationY*translationY)>Box2D.Common.b2Settings.b2_maxTranslationSquared)
{
b.m_linearVelocity.Normalize();
b.m_linearVelocity.x*=Box2D.Common.b2Settings.b2_maxTranslation*subStep.inv_dt;
b.m_linearVelocity.y*=Box2D.Common.b2Settings.b2_maxTranslation*subStep.inv_dt;
}
var rotation=subStep.dt*b.m_angularVelocity;
if(rotation*rotation>Box2D.Common.b2Settings.b2_maxRotationSquared)
{
if(b.m_angularVelocity<0.0)
{
b.m_angularVelocity=-Box2D.Common.b2Settings.b2_maxRotation*subStep.inv_dt;
}
else
{
b.m_angularVelocity=Box2D.Common.b2Settings.b2_maxRotation*subStep.inv_dt;
}
}
b.m_sweep.c0.SetV(b.m_sweep.c);
b.m_sweep.a0=b.m_sweep.a;
b.m_sweep.c.x+=subStep.dt*b.m_linearVelocity.x;
b.m_sweep.c.y+=subStep.dt*b.m_linearVelocity.y;
b.m_sweep.a+=subStep.dt*b.m_angularVelocity;
b.SynchronizeTransform();
}
var k_toiBaumgarte=0.75;
for(i=0;i<subStep.positionIterations;++i)
{
var contactsOkay=contactSolver.SolvePositionConstraints(k_toiBaumgarte);
var jointsOkay=true;
for(j=0;j<this.m_jointCount;++j)
{
var jointOkay=this.m_joints[j].SolvePositionConstraints(Box2D.Common.b2Settings.b2_contactBaumgarte);
jointsOkay=jointsOkay&&jointOkay;
}
if(contactsOkay&&jointsOkay)
{
break;
}
}
this.Report(contactSolver.m_constraints);
},
"private static var",{s_impulse:function(){return(new Box2D.Dynamics.b2ContactImpulse());}},
"public function Report",function(constraints)
{
if(this.m_listener$1==null)
{
return;
}
for(var i=0;i<this.m_contactCount;++i)
{
var c=this.m_contacts[i];
var cc=constraints[i];
for(var j=0;j<cc.pointCount;++j)
{
$$private.s_impulse.normalImpulses[j]=cc.points[j].normalImpulse;
$$private.s_impulse.tangentImpulses[j]=cc.points[j].tangentImpulse;
}
this.m_listener$1.PostSolve(c,$$private.s_impulse);
}
},
"public function AddBody",function(body)
{
body.m_islandIndex=this.m_bodyCount;
this.m_bodies[this.m_bodyCount++]=body;
},
"public function AddContact",function(contact)
{
this.m_contacts[this.m_contactCount++]=contact;
},
"public function AddJoint",function(joint)
{
this.m_joints[this.m_jointCount++]=joint;
},
"private var",{m_allocator:undefined},
"private var",{m_listener:null},
"private var",{m_contactSolver:null},
"b2internal var",{m_bodies:null},
"b2internal var",{m_contacts:null},
"b2internal var",{m_joints:null},
"b2internal var",{m_bodyCount:0},
"b2internal var",{m_jointCount:0},
"b2internal var",{m_contactCount:0},
"private var",{m_bodyCapacity:0},
"b2internal var",{m_contactCapacity:0},
"b2internal var",{m_jointCapacity:0},
];},[],["Array","Box2D.Dynamics.b2Body","Box2D.Common.Math.b2Math","Box2D.Common.b2Settings","Number","Box2D.Dynamics.b2ContactImpulse"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2JointEvent
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2JointEvent extends flash.events.Event",2,function($$private){;return[

"public var",{joint:null},
"function b2JointEvent",function(type)
{
this.super$2(type);
},
"override public function clone",function()
{
var event=new Box2D.Dynamics.b2JointEvent(this.type);
event.joint=this.joint;
return event;
},
];},[],["flash.events.Event"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2PostSolveEvent
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2PostSolveEvent extends Box2D.Dynamics.b2ContactEvent",3,function($$private){;return[

"public var",{impulse:null},
"function b2PostSolveEvent",function(type)
{
this.super$3(type);
},
"override public function clone",function()
{
var event=new Box2D.Dynamics.b2PostSolveEvent(this.type);
event.contact=this.contact;
event.impulse=this.impulse;
return event;
},
];},[],["Box2D.Dynamics.b2ContactEvent"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2PreSolveEvent
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2PreSolveEvent extends Box2D.Dynamics.b2ContactEvent",3,function($$private){;return[

"public var",{oldManifold:null},
"function b2PreSolveEvent",function(type)
{
this.super$3(type);
},
"override public function clone",function()
{
var event=new Box2D.Dynamics.b2PreSolveEvent(this.type);
event.contact=this.contact;
event.oldManifold=this.oldManifold;
return event;
},
];},[],["Box2D.Dynamics.b2ContactEvent"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2TimeStep
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2TimeStep",1,function($$private){;return[

"public function Set",function(step)
{
this.dt=step.dt;
this.inv_dt=step.inv_dt;
this.positionIterations=step.positionIterations;
this.velocityIterations=step.velocityIterations;
this.warmStarting=step.warmStarting;
},
"public var",{dt:NaN},
"public var",{inv_dt:NaN},
"public var",{dtRatio:NaN},
"public var",{velocityIterations:0},
"public var",{positionIterations:0},
"public var",{warmStarting:false},
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.b2World
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public class b2World extends flash.events.EventDispatcher",2,function($$private){var as=joo.as,assert=joo.assert;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint,Box2D.Dynamics.b2Body,Box2D.Collision.Shapes.b2Shape,Number,Box2D.Dynamics.Contacts.b2Contact,Box2D.Common.b2Settings,Box2D.Dynamics.b2DebugDraw);},

{Event:{name:"PreStep",type:"flash.events.Event"}},
"public static var",{PRESTEP:"PreStep"},
{Event:{name:"PostStep",type:"flash.events.Event"}},
"public static var",{POSTSTEP:"PostStep"},
{Event:{name:"BeginContact",type:"Box2D.Dynamics.b2ContactEvent"}},
"public static var",{BEGINCONTACT:"BeginContact"},
{Event:{name:"EndContact",type:"Box2D.Dynamics.b2ContactEvent"}},
"public static var",{ENDCONTACT:"EndContact"},
{Event:{name:"PreSolve",type:"Box2D.Dynamics.b2PreSolveEvent"}},
"public static var",{PRESOLVE:"PreSolve"},
{Event:{name:"PostSolve",type:"Box2D.Dynamics.b2PostSolveEvent"}},
"public static var",{POSTSOLVE:"PostSolve"},
{Event:{name:"AddBody",type:"Box2D.Dynamics.b2BodyEvent"}},
"public static var",{ADDBODY:"AddBody"},
{Event:{name:"RemoveBody",type:"Box2D.Dynamics.b2BodyEvent"}},
"public static var",{REMOVEBODY:"RemoveBody"},
{Event:{name:"AddFixture",type:"Box2D.Dynamics.b2FixtureEvent"}},
"public static var",{ADDFIXTURE:"AddFixture"},
{Event:{name:"RemoveFixture",type:"Box2D.Dynamics.b2FixtureEvent"}},
"public static var",{REMOVEFIXTURE:"RemoveJoint"},
{Event:{name:"AddJoint",type:"Box2D.Dynamics.b2JointEvent"}},
"public static var",{ADDJOINT:"AddJoint"},
{Event:{name:"RemoveJoint",type:"Box2D.Dynamics.b2JointEvent"}},
"public static var",{REMOVEJOINT:"RemoveJoint"},
"public function b2World",function(gravity,doSleep){this.super$2();this.s_stack$2=this.s_stack$2();this.m_contactSolver$2=this.m_contactSolver$2();this.m_island$2=this.m_island$2();this.m_preStepEvent$2=this.m_preStepEvent$2();this.m_postStepEvent$2=this.m_postStepEvent$2();this.m_addBodyEvent$2=this.m_addBodyEvent$2();this.m_removeBodyEvent$2=this.m_removeBodyEvent$2();this.m_addFixtureEvent=this.m_addFixtureEvent();this.m_removeFixtureEvent=this.m_removeFixtureEvent();this.m_addJointEvent$2=this.m_addJointEvent$2();this.m_removeJointEvent$2=this.m_removeJointEvent$2();
this.m_destructionListener$2=null;
this.m_debugDraw$2=null;
this.m_bodyList=null;
this.m_contactList=null;
this.m_jointList$2=null;
this.m_controllerList$2=null;
this.m_bodyCount$2=0;
this.m_jointCount$2=0;
this.m_controllerCount$2=0;
$$private.m_warmStarting=true;
$$private.m_continuousPhysics=true;
this.m_allowSleep$2=doSleep;
this.m_gravity$2=gravity.Copy();
this.m_flags=Box2D.Dynamics.b2World.e_clearForces;
this.m_inv_dt0$2=0.0;
this.m_contactManager=new Box2D.Dynamics.b2ContactManager(this);
var bd=new Box2D.Dynamics.b2BodyDef();
this.m_groundBody=this.CreateBody(bd);
},
"public function SetDestructionListener",function(listener){
this.m_destructionListener$2=listener;
},
"public function SetContactFilter",function(filter){
this.m_contactManager.m_contactFilter=filter;
},
"public function SetContactListener",function(listener){
this.m_contactManager.m_contactListener=listener;
},
"public function SetDebugDraw",function(debugDraw){
this.m_debugDraw$2=debugDraw;
},
"public function SetBroadPhase",function(broadPhase){
var oldBroadPhase=this.m_contactManager.m_broadPhase;
this.m_contactManager.m_broadPhase=broadPhase;
for(var b=this.m_bodyList;b;b=b.m_next)
{
for(var f=b.m_fixtureList;f;f=f.m_next)
{
f.m_proxy=broadPhase.CreateProxy(oldBroadPhase.GetFatAABB(f.m_proxy),f);
}
}
},
"public function Validate",function()
{
this.m_contactManager.m_broadPhase.Validate();
},
"public function GetProxyCount",function()
{
return this.m_contactManager.m_broadPhase.GetProxyCount();
},
"public function CreateBody",function(def){
this.CheckUnlocked();
var b=new Box2D.Dynamics.b2Body(def,this);
b.m_prev=null;
b.m_next=this.m_bodyList;
if(this.m_bodyList)
{
this.m_bodyList.m_prev=b;
}
this.m_bodyList=b;
++this.m_bodyCount$2;
this.m_addBodyEvent$2.body=b;
this.dispatchEvent(this.m_addBodyEvent$2);
if(b.m_eventDispatcher)b.m_eventDispatcher.dispatchEvent(this.m_addBodyEvent$2);
return b;
},
"public function DestroyBody",function(b){
this.CheckUnlocked();
if(!b.m_world)
throw new Error("You cannot delete a body twice");
b.m_world=null;
this.m_removeBodyEvent$2.body=b;
if(b.m_eventDispatcher)b.m_eventDispatcher.dispatchEvent(this.m_removeBodyEvent$2);
this.dispatchEvent(this.m_removeBodyEvent$2);
var jn=b.m_jointList;
while(jn)
{
var jn0=jn;
jn=jn.next;
if(this.m_destructionListener$2)
{
this.m_destructionListener$2.SayGoodbyeJoint(jn0.joint);
}
this.DestroyJoint(jn0.joint);
}
var ce=b.m_contactList;
while(ce)
{
var ce0=ce;
ce=ce.next;
this.m_contactManager.Destroy(ce0.contact);
}
b.m_contactList=null;
var f=b.m_fixtureList;
while(f)
{
var f0=f;
f=f.m_next;
if(this.m_destructionListener$2)
{
this.m_destructionListener$2.SayGoodbyeFixture(f0);
}
this.m_removeFixtureEvent.fixture=f;
if(b.m_eventDispatcher)b.m_eventDispatcher.dispatchEvent(this.m_removeFixtureEvent);
this.dispatchEvent(this.m_removeFixtureEvent);
f0.DestroyProxy(this.m_contactManager.m_broadPhase);
f0.Destroy();
}
b.m_fixtureList=null;
b.m_fixtureCount=0;
if(b.m_prev)
{
b.m_prev.m_next=b.m_next;
}
if(b.m_next)
{
b.m_next.m_prev=b.m_prev;
}
if(b==this.m_bodyList)
{
this.m_bodyList=b.m_next;
}
--this.m_bodyCount$2;
},
"public function CreateJoint",function(def){
var j=Box2D.Dynamics.Joints.b2Joint.Create(def,null);
j.m_prev=null;
j.m_next=this.m_jointList$2;
if(this.m_jointList$2)
{
this.m_jointList$2.m_prev=j;
}
this.m_jointList$2=j;
++this.m_jointCount$2;
j.m_edgeA.joint=j;
j.m_edgeA.other=j.m_bodyB;
j.m_edgeA.prev=null;
j.m_edgeA.next=j.m_bodyA.m_jointList;
if(j.m_bodyA.m_jointList)j.m_bodyA.m_jointList.prev=j.m_edgeA;
j.m_bodyA.m_jointList=j.m_edgeA;
j.m_edgeB.joint=j;
j.m_edgeB.other=j.m_bodyA;
j.m_edgeB.prev=null;
j.m_edgeB.next=j.m_bodyB.m_jointList;
if(j.m_bodyB.m_jointList)j.m_bodyB.m_jointList.prev=j.m_edgeB;
j.m_bodyB.m_jointList=j.m_edgeB;
var bodyA=def.bodyA;
var bodyB=def.bodyB;
if(def.collideConnected==false)
{
var edge=bodyB.GetContactList();
while(edge)
{
if(edge.other==bodyA)
{
edge.contact.FlagForFiltering();
}
edge=edge.next;
}
}
this.m_addJointEvent$2.joint=j;
this.dispatchEvent(this.m_addJointEvent$2);
if(bodyA.m_eventDispatcher)bodyA.m_eventDispatcher.dispatchEvent(this.m_addJointEvent$2);
if(bodyB.m_eventDispatcher)bodyB.m_eventDispatcher.dispatchEvent(this.m_addJointEvent$2);
return j;
},
"public function DestroyJoint",function(j){
this.CheckUnlocked();
var bodyA=j.m_bodyA;
var bodyB=j.m_bodyB;
if(!j.m_bodyA)
throw new Error("You cannot delete a joint twice.");
j.m_bodyA=null;
j.m_bodyB=null;
this.m_removeJointEvent$2.joint=j;
if(bodyB.m_eventDispatcher)bodyB.m_eventDispatcher.dispatchEvent(this.m_removeJointEvent$2);
if(bodyA.m_eventDispatcher)bodyA.m_eventDispatcher.dispatchEvent(this.m_removeJointEvent$2);
this.dispatchEvent(this.m_removeJointEvent$2);
var collideConnected=j.m_collideConnected;
if(j.m_prev)
{
j.m_prev.m_next=j.m_next;
}
if(j.m_next)
{
j.m_next.m_prev=j.m_prev;
}
if(j==this.m_jointList$2)
{
this.m_jointList$2=j.m_next;
}
bodyA.SetAwake(true);
bodyB.SetAwake(true);
if(j.m_edgeA.prev)
{
j.m_edgeA.prev.next=j.m_edgeA.next;
}
if(j.m_edgeA.next)
{
j.m_edgeA.next.prev=j.m_edgeA.prev;
}
if(j.m_edgeA==bodyA.m_jointList)
{
bodyA.m_jointList=j.m_edgeA.next;
}
j.m_edgeA.prev=null;
j.m_edgeA.next=null;
if(j.m_edgeB.prev)
{
j.m_edgeB.prev.next=j.m_edgeB.next;
}
if(j.m_edgeB.next)
{
j.m_edgeB.next.prev=j.m_edgeB.prev;
}
if(j.m_edgeB==bodyB.m_jointList)
{
bodyB.m_jointList=j.m_edgeB.next;
}
j.m_edgeB.prev=null;
j.m_edgeB.next=null;
Box2D.Dynamics.Joints.b2Joint.Destroy(j,null);
--this.m_jointCount$2;
if(collideConnected==false)
{
var edge=bodyB.GetContactList();
while(edge)
{
if(edge.other==bodyA)
{
edge.contact.FlagForFiltering();
}
edge=edge.next;
}
}
},
"public function AddController",function(c)
{
c.m_next=this.m_controllerList$2;
c.m_prev=null;
this.m_controllerList$2=c;
c.m_world=this;
this.m_controllerCount$2++;
return c;
},
"public function RemoveController",function(c)
{
if(c.m_prev)
c.m_prev.m_next=c.m_next;
if(c.m_next)
c.m_next.m_prev=c.m_prev;
if(this.m_controllerList$2==c)
this.m_controllerList$2=c.m_next;
this.m_controllerCount$2--;
},
"public function SetWarmStarting",function(flag){$$private.m_warmStarting=flag;},
"public function SetContinuousPhysics",function(flag){$$private.m_continuousPhysics=flag;},
"public function GetBodyCount",function()
{
return this.m_bodyCount$2;
},
"public function GetJointCount",function()
{
return this.m_jointCount$2;
},
"public function GetContactCount",function()
{
return this.m_contactManager.m_contactCount;
},
"public function SetGravity",function(gravity)
{
this.m_gravity$2=gravity;
},
"public function GetGravity",function(){
return this.m_gravity$2;
},
"public function GetGroundBody",function(){
return this.m_groundBody;
},
"private static var",{s_timestep2:function(){return(new Box2D.Dynamics.b2TimeStep());}},
"public function Step",function(dt,velocityIterations,positionIterations){
this.dispatchEvent(this.m_preStepEvent$2);
if(this.m_flags&Box2D.Dynamics.b2World.e_newFixture)
{
this.m_contactManager.FindNewContacts();
this.m_flags&=~Box2D.Dynamics.b2World.e_newFixture;
}
this.m_flags|=Box2D.Dynamics.b2World.e_locked;
var step=$$private.s_timestep2;
step.dt=dt;
step.velocityIterations=velocityIterations;
step.positionIterations=positionIterations;
if(dt>0.0)
{
step.inv_dt=1.0/dt;
}
else
{
step.inv_dt=0.0;
}
step.dtRatio=this.m_inv_dt0$2*dt;
step.warmStarting=$$private.m_warmStarting;
this.m_contactManager.Collide();
if(step.dt>0.0)
{
this.Solve(step);
}
if($$private.m_continuousPhysics&&step.dt>0.0)
{
this.SolveTOI(step);
}
if(step.dt>0.0)
{
this.m_inv_dt0$2=step.inv_dt;
}
if(this.m_flags&Box2D.Dynamics.b2World.e_clearForces)
{
this.ClearForces();
}
this.m_flags&=~Box2D.Dynamics.b2World.e_locked;
this.dispatchEvent(this.m_postStepEvent$2);
},
"public function ClearForces",function()
{
for(var body=this.m_bodyList;body;body=body.m_next)
{
body.m_force.SetZero();
body.m_torque=0.0;
}
},
"static private var",{s_xf:function(){return(new Box2D.Common.Math.b2Transform());}},
"public function DrawDebugData",function(){
if(this.m_debugDraw$2==null)
{
return;
}
this.m_debugDraw$2.m_sprite.graphics.clear();
var flags=this.m_debugDraw$2.GetFlags();
var i;
var b;
var f;
var s;
var j;
var bp;
var invQ=new Box2D.Common.Math.b2Vec2;
var x1=new Box2D.Common.Math.b2Vec2;
var x2=new Box2D.Common.Math.b2Vec2;
var xf;
var b1=new Box2D.Collision.b2AABB();
var b2=new Box2D.Collision.b2AABB();
var vs=[new Box2D.Common.Math.b2Vec2(),new Box2D.Common.Math.b2Vec2(),new Box2D.Common.Math.b2Vec2(),new Box2D.Common.Math.b2Vec2()];
var color=new Box2D.Common.b2Color(0,0,0);
if(flags&Box2D.Dynamics.b2DebugDraw.e_shapeBit)
{
for(b=this.m_bodyList;b;b=b.m_next)
{
xf=b.m_xf;
for(f=b.GetFixtureList();f;f=f.m_next)
{
s=f.GetShape();
if(b.IsActive()==false)
{
color.Set(0.5,0.5,0.3);
this.DrawShape(s,xf,color);
}
else if(b.GetType()==Box2D.Dynamics.b2Body.b2_staticBody)
{
color.Set(0.5,0.9,0.5);
this.DrawShape(s,xf,color);
}
else if(b.GetType()==Box2D.Dynamics.b2Body.b2_kinematicBody)
{
color.Set(0.5,0.5,0.9);
this.DrawShape(s,xf,color);
}
else if(b.IsAwake()==false)
{
color.Set(0.6,0.6,0.6);
this.DrawShape(s,xf,color);
}
else
{
color.Set(0.9,0.7,0.7);
this.DrawShape(s,xf,color);
}
}
}
}
if(flags&Box2D.Dynamics.b2DebugDraw.e_jointBit)
{
for(j=this.m_jointList$2;j;j=j.m_next)
{
this.DrawJoint(j);
}
}
if(flags&Box2D.Dynamics.b2DebugDraw.e_controllerBit)
{
for(var c=this.m_controllerList$2;c;c=c.m_next)
{
c.Draw(this.m_debugDraw$2);
}
}
if(flags&Box2D.Dynamics.b2DebugDraw.e_pairBit)
{
color.Set(0.3,0.9,0.9);
for(var contact=this.m_contactManager.m_contactList;contact;contact=contact.GetNext())
{
var fixtureA=contact.GetFixtureA();
var fixtureB=contact.GetFixtureB();
var cA=fixtureA.GetAABB().GetCenter();
var cB=fixtureB.GetAABB().GetCenter();
this.m_debugDraw$2.DrawSegment(cA,cB,color);
}
}
if(flags&Box2D.Dynamics.b2DebugDraw.e_aabbBit)
{
bp=this.m_contactManager.m_broadPhase;
vs=[new Box2D.Common.Math.b2Vec2(),new Box2D.Common.Math.b2Vec2(),new Box2D.Common.Math.b2Vec2(),new Box2D.Common.Math.b2Vec2()];
for(b=this.m_bodyList;b;b=b.GetNext())
{
if(b.IsActive()==false)
{
continue;
}
for(f=b.GetFixtureList();f;f=f.GetNext())
{
var aabb=bp.GetFatAABB(f.m_proxy);
vs[0].Set(aabb.lowerBound.x,aabb.lowerBound.y);
vs[1].Set(aabb.upperBound.x,aabb.lowerBound.y);
vs[2].Set(aabb.upperBound.x,aabb.upperBound.y);
vs[3].Set(aabb.lowerBound.x,aabb.upperBound.y);
this.m_debugDraw$2.DrawPolygon(vs,4,color);
}
}
}
if(flags&Box2D.Dynamics.b2DebugDraw.e_centerOfMassBit)
{
for(b=this.m_bodyList;b;b=b.m_next)
{
xf=$$private.s_xf;
xf.R=b.m_xf.R;
xf.position=b.GetWorldCenter();
this.m_debugDraw$2.DrawTransform(xf);
}
}
},
"public function QueryAABB",function(callback,aabb)
{
var broadPhase=this.m_contactManager.m_broadPhase;
function WorldQueryWrapper(proxy)
{
return callback(broadPhase.GetUserData(proxy));
}
broadPhase.Query(WorldQueryWrapper,aabb);
},
"public function QueryShape",function(callback,shape,transform)
{if(arguments.length<3){transform=null;}
if(transform==null)
{
transform=new Box2D.Common.Math.b2Transform();
transform.SetIdentity();
}
var broadPhase=this.m_contactManager.m_broadPhase;
function WorldQueryWrapper(proxy)
{
var fixture=as(broadPhase.GetUserData(proxy),Box2D.Dynamics.b2Fixture);
if(Box2D.Collision.Shapes.b2Shape.TestOverlap(shape,transform,fixture.GetShape(),fixture.GetBody().GetTransform()))
return callback(fixture);
return true;
}
var aabb=new Box2D.Collision.b2AABB();
shape.ComputeAABB(aabb,transform);
broadPhase.Query(WorldQueryWrapper,aabb);
},
"public function QueryPoint",function(callback,p)
{
var broadPhase=this.m_contactManager.m_broadPhase;
function WorldQueryWrapper(proxy)
{
var fixture=as(broadPhase.GetUserData(proxy),Box2D.Dynamics.b2Fixture);
if(fixture.TestPoint(p))
return callback(fixture);
return true;
}
var aabb=new Box2D.Collision.b2AABB();
aabb.lowerBound.Set(p.x-Box2D.Common.b2Settings.b2_linearSlop,p.y-Box2D.Common.b2Settings.b2_linearSlop);
aabb.upperBound.Set(p.x+Box2D.Common.b2Settings.b2_linearSlop,p.y+Box2D.Common.b2Settings.b2_linearSlop);
broadPhase.Query(WorldQueryWrapper,aabb);
},
"public function RayCast",function(callback,point1,point2)
{
var broadPhase=this.m_contactManager.m_broadPhase;
var output=new Box2D.Collision.b2RayCastOutput;
function RayCastWrapper(input,proxy)
{
var userData=broadPhase.GetUserData(proxy);
var fixture=as(userData,Box2D.Dynamics.b2Fixture);
var hit=fixture.RayCast(output,input);
if(hit)
{
var fraction=output.fraction;
var point=new Box2D.Common.Math.b2Vec2(
(1.0-fraction)*point1.x+fraction*point2.x,
(1.0-fraction)*point1.y+fraction*point2.y);
return callback(fixture,point,output.normal,fraction);
}
return input.maxFraction;
}
var input=new Box2D.Collision.b2RayCastInput(point1,point2);
broadPhase.RayCast(RayCastWrapper,input);
},
"public function RayCastOne",function(point1,point2)
{
var result;
function RayCastOneWrapper(fixture,point,normal,fraction)
{
result=fixture;
return fraction;
}
this.RayCast(RayCastOneWrapper,point1,point2);
return result;
},
"public function RayCastAll",function(point1,point2)
{
var result=new Array();
function RayCastAllWrapper(fixture,point,normal,fraction)
{
result[result.length]=fixture;
return 1;
}
this.RayCast(RayCastAllWrapper,point1,point2);
return result;
},
"public function GetBodyList",function(){
return this.m_bodyList;
},
"public function GetJointList",function(){
return this.m_jointList$2;
},
"public function GetContactList",function()
{
return this.m_contactList;
},
"public function IsLocked",function()
{
return(this.m_flags&Box2D.Dynamics.b2World.e_locked)>0;
},
"b2internal function CheckUnlocked",function()
{
if(this.IsLocked())
throw new Error("You cannot call this method while the world is locked");
},
"public function SetAutoClearForces",function(flag)
{
if(flag)
{
this.m_flags|=Box2D.Dynamics.b2World.e_clearForces;
}else{
this.m_flags&=~Box2D.Dynamics.b2World.e_clearForces;
}
},
"public function GetAutoClearForces",function()
{
return(this.m_flags&Box2D.Dynamics.b2World.e_clearForces)==Box2D.Dynamics.b2World.e_clearForces;
},
"private var",{s_stack:function(){return(new Array());}},
"b2internal function Solve",function(step){
var b;
for(var controller=this.m_controllerList$2;controller;controller=controller.m_next)
{
controller.Step(step);
}
var island=this.m_island$2;
island.Initialize(this.m_bodyCount$2,this.m_contactManager.m_contactCount,this.m_jointCount$2,null,this.m_contactManager.m_contactListener,this.m_contactSolver$2);
for(b=this.m_bodyList;b;b=b.m_next)
{
b.m_flags&=~Box2D.Dynamics.b2Body.e_islandFlag;
}
for(var c=this.m_contactList;c;c=c.m_next)
{
c.m_flags&=~Box2D.Dynamics.Contacts.b2Contact.e_islandFlag;
}
for(var j=this.m_jointList$2;j;j=j.m_next)
{
j.m_islandFlag=false;
}
var stackSize=this.m_bodyCount$2;
var stack=this.s_stack$2;
for(var seed=this.m_bodyList;seed;seed=seed.m_next)
{
if(seed.m_flags&Box2D.Dynamics.b2Body.e_islandFlag)
{
continue;
}
if(seed.IsAwake()==false||seed.IsActive()==false)
{
continue;
}
if(seed.GetType()==Box2D.Dynamics.b2Body.b2_staticBody)
{
continue;
}
island.Clear();
var stackCount=0;
stack[stackCount++]=seed;
seed.m_flags|=Box2D.Dynamics.b2Body.e_islandFlag;
while(stackCount>0)
{
b=stack[--stackCount];
island.AddBody(b);
b.SetAwake(true);
if(b.GetType()==Box2D.Dynamics.b2Body.b2_staticBody)
{
continue;
}
var other;
for(var ce=b.m_contactList;ce;ce=ce.next)
{
var contact=ce.contact;
if(contact.m_flags&Box2D.Dynamics.Contacts.b2Contact.e_islandFlag)
{
continue;
}
if(contact.IsSensor()==true||
contact.IsEnabled()==false||
contact.IsTouching()==false)
{
continue;
}
island.AddContact(contact);
contact.m_flags|=Box2D.Dynamics.Contacts.b2Contact.e_islandFlag;
other=ce.other;
if(other.m_flags&Box2D.Dynamics.b2Body.e_islandFlag)
{
continue;
}
stack[stackCount++]=other;
other.m_flags|=Box2D.Dynamics.b2Body.e_islandFlag;
}
for(var jn=b.m_jointList;jn;jn=jn.next)
{
var joint=jn.joint;
if(joint.m_islandFlag==true)
{
continue;
}
other=jn.other;
if(other.IsActive()==false)
{
continue;
}
island.AddJoint(joint);
joint.m_islandFlag=true;
if(other.m_flags&Box2D.Dynamics.b2Body.e_islandFlag)
{
continue;
}
stack[stackCount++]=other;
other.m_flags|=Box2D.Dynamics.b2Body.e_islandFlag;
}
}
island.Solve(step,this.m_gravity$2,this.m_allowSleep$2);
for(var i=0;i<island.m_bodyCount;++i)
{
b=island.m_bodies[i];
if(b.GetType()==Box2D.Dynamics.b2Body.b2_staticBody)
{
b.m_flags&=~Box2D.Dynamics.b2Body.e_islandFlag;
}
}
}
for(i=0;i<stack.length;++i)
{
if(!stack[i])break;
stack[i]=null;
}
for(b=this.m_bodyList;b;b=b.m_next)
{
if((b.m_flags&Box2D.Dynamics.b2Body.e_islandFlag)==0)
{
continue;
}
if(b.GetType()==Box2D.Dynamics.b2Body.b2_staticBody)
{
continue;
}
b.SynchronizeFixtures();
}
this.m_contactManager.FindNewContacts();
},
"private static var",{s_backupA:function(){return(new Box2D.Common.Math.b2Sweep());}},
"private static var",{s_backupB:function(){return(new Box2D.Common.Math.b2Sweep());}},
"private static var",{s_timestep:function(){return(new Box2D.Dynamics.b2TimeStep());}},
"private static var",{s_queue:function(){return(new Array());}},
"b2internal function SolveTOI",function(step){
var b;
var fA;
var fB;
var bA;
var bB;
var cEdge;
var j;
var island=this.m_island$2;
island.Initialize(this.m_bodyCount$2,Box2D.Common.b2Settings.b2_maxTOIContacts,Box2D.Common.b2Settings.b2_maxTOIJoints,null,this.m_contactManager.m_contactListener,this.m_contactSolver$2);
var queue=$$private.s_queue;
for(b=this.m_bodyList;b;b=b.m_next)
{
b.m_flags&=~Box2D.Dynamics.b2Body.e_islandFlag;
b.m_sweep.t0=0.0;
}
var c;
for(c=this.m_contactList;c;c=c.m_next)
{
c.m_flags&=~(Box2D.Dynamics.Contacts.b2Contact.e_toiFlag|Box2D.Dynamics.Contacts.b2Contact.e_islandFlag);
}
for(j=this.m_jointList$2;j;j=j.m_next)
{
j.m_islandFlag=false;
}
for(;;)
{
var minContact=null;
var minTOI=1.0;
for(c=this.m_contactList;c;c=c.m_next)
{
if(c.IsSensor()==true||
c.IsEnabled()==false||
c.IsContinuous()==false)
{
continue;
}
var toi=1.0;
if(c.m_flags&Box2D.Dynamics.Contacts.b2Contact.e_toiFlag)
{
toi=c.m_toi;
}
else
{
fA=c.m_fixtureA;
fB=c.m_fixtureB;
bA=fA.m_body;
bB=fB.m_body;
if((bA.GetType()!=Box2D.Dynamics.b2Body.b2_dynamicBody||bA.IsAwake()==false)&&
(bB.GetType()!=Box2D.Dynamics.b2Body.b2_dynamicBody||bB.IsAwake()==false))
{
continue;
}
var t0=bA.m_sweep.t0;
if(bA.m_sweep.t0<bB.m_sweep.t0)
{
t0=bB.m_sweep.t0;
bA.m_sweep.Advance(t0);
}
else if(bB.m_sweep.t0<bA.m_sweep.t0)
{
t0=bA.m_sweep.t0;
bB.m_sweep.Advance(t0);
}
toi=c.ComputeTOI(bA.m_sweep,bB.m_sweep);
if(toi>0.0&&toi<1.0)
{
toi=(1.0-toi)*t0+toi;
if(toi>1)toi=1;
}
c.m_toi=toi;
c.m_flags|=Box2D.Dynamics.Contacts.b2Contact.e_toiFlag;
}
if(Number.MIN_VALUE<toi&&toi<minTOI)
{
minContact=c;
minTOI=toi;
}
}
if(minContact==null||1.0-100.0*Number.MIN_VALUE<minTOI)
{
break;
}
fA=minContact.m_fixtureA;
fB=minContact.m_fixtureB;
bA=fA.m_body;
bB=fB.m_body;
$$private.s_backupA.Set(bA.m_sweep);
$$private.s_backupB.Set(bB.m_sweep);
bA.Advance(minTOI);
bB.Advance(minTOI);
minContact.Update(this.m_contactManager.m_contactListener);
minContact.m_flags&=~Box2D.Dynamics.Contacts.b2Contact.e_toiFlag;
if(minContact.IsSensor()==true||
minContact.IsEnabled()==false)
{
bA.m_sweep.Set($$private.s_backupA);
bB.m_sweep.Set($$private.s_backupB);
bA.SynchronizeTransform();
bB.SynchronizeTransform();
continue;
}
if(minContact.IsTouching()==false)
{
continue;
}
var seed=bA;
if(seed.GetType()!=Box2D.Dynamics.b2Body.b2_dynamicBody)
{
seed=bB;
}
island.Clear();
var queueStart=0;
var queueSize=0;
queue[queueStart+queueSize++]=seed;
seed.m_flags|=Box2D.Dynamics.b2Body.e_islandFlag;
while(queueSize>0)
{
b=queue[queueStart++];
--queueSize;
island.AddBody(b);
if(b.IsAwake()==false)
{
b.SetAwake(true);
}
if(b.GetType()!=Box2D.Dynamics.b2Body.b2_dynamicBody)
{
continue;
}
for(cEdge=b.m_contactList;cEdge;cEdge=cEdge.next)
{
if(island.m_contactCount==island.m_contactCapacity)
{
break;
}
if(cEdge.contact.m_flags&Box2D.Dynamics.Contacts.b2Contact.e_islandFlag)
{
continue;
}
if(cEdge.contact.IsSensor()==true||
cEdge.contact.IsEnabled()==false||
cEdge.contact.IsTouching()==false)
{
continue;
}
island.AddContact(cEdge.contact);
cEdge.contact.m_flags|=Box2D.Dynamics.Contacts.b2Contact.e_islandFlag;
var other=cEdge.other;
if(other.m_flags&Box2D.Dynamics.b2Body.e_islandFlag)
{
continue;
}
if(other.GetType()!=Box2D.Dynamics.b2Body.b2_staticBody)
{
other.Advance(minTOI);
other.SetAwake(true);
}
queue[queueStart+queueSize]=other;
++queueSize;
other.m_flags|=Box2D.Dynamics.b2Body.e_islandFlag;
}
for(var jEdge=b.m_jointList;jEdge;jEdge=jEdge.next)
{
if(island.m_jointCount==island.m_jointCapacity)
continue;
if(jEdge.joint.m_islandFlag==true)
continue;
other=jEdge.other;
if(other.IsActive()==false)
{
continue;
}
island.AddJoint(jEdge.joint);
jEdge.joint.m_islandFlag=true;
if(other.m_flags&Box2D.Dynamics.b2Body.e_islandFlag)
continue;
if(other.GetType()!=Box2D.Dynamics.b2Body.b2_staticBody)
{
other.Advance(minTOI);
other.SetAwake(true);
}
queue[queueStart+queueSize]=other;
++queueSize;
other.m_flags|=Box2D.Dynamics.b2Body.e_islandFlag;
}
}
var subStep=$$private.s_timestep;
subStep.warmStarting=false;
subStep.dt=(1.0-minTOI)*step.dt;
subStep.inv_dt=1.0/subStep.dt;
subStep.dtRatio=0.0;
subStep.velocityIterations=step.velocityIterations;
subStep.positionIterations=step.positionIterations;
island.SolveTOI(subStep);
var i;
for(i=0;i<island.m_bodyCount;++i)
{
b=island.m_bodies[i];
b.m_flags&=~Box2D.Dynamics.b2Body.e_islandFlag;
if(b.IsAwake()==false)
{
continue;
}
if(b.GetType()!=Box2D.Dynamics.b2Body.b2_dynamicBody)
{
continue;
}
b.SynchronizeFixtures();
for(cEdge=b.m_contactList;cEdge;cEdge=cEdge.next)
{
cEdge.contact.m_flags&=~Box2D.Dynamics.Contacts.b2Contact.e_toiFlag;
}
}
for(i=0;i<island.m_contactCount;++i)
{
c=island.m_contacts[i];
c.m_flags&=~(Box2D.Dynamics.Contacts.b2Contact.e_toiFlag|Box2D.Dynamics.Contacts.b2Contact.e_islandFlag);
}
for(i=0;i<island.m_jointCount;++i)
{
j=island.m_joints[i];
j.m_islandFlag=false;
}
this.m_contactManager.FindNewContacts();
}
},
"static private var",{s_jointColor:function(){return(new Box2D.Common.b2Color(0.5,0.8,0.8));}},
"b2internal function DrawJoint",function(joint){
var b1=joint.GetBodyA();
var b2=joint.GetBodyB();
var xf1=b1.m_xf;
var xf2=b2.m_xf;
var x1=xf1.position;
var x2=xf2.position;
var p1=joint.GetAnchorA();
var p2=joint.GetAnchorB();
var color=$$private.s_jointColor;
switch(joint.m_type)
{
case Box2D.Dynamics.Joints.b2Joint.e_distanceJoint:
this.m_debugDraw$2.DrawSegment(p1,p2,color);
break;
case Box2D.Dynamics.Joints.b2Joint.e_pulleyJoint:
{
var pulley=(as(joint,Box2D.Dynamics.Joints.b2PulleyJoint));
var s1=pulley.GetGroundAnchorA();
var s2=pulley.GetGroundAnchorB();
this.m_debugDraw$2.DrawSegment(s1,p1,color);
this.m_debugDraw$2.DrawSegment(s2,p2,color);
this.m_debugDraw$2.DrawSegment(s1,s2,color);
}
break;
case Box2D.Dynamics.Joints.b2Joint.e_mouseJoint:
this.m_debugDraw$2.DrawSegment(p1,p2,color);
break;
default:
if(b1!=this.m_groundBody)
this.m_debugDraw$2.DrawSegment(x1,p1,color);
this.m_debugDraw$2.DrawSegment(p1,p2,color);
if(b2!=this.m_groundBody)
this.m_debugDraw$2.DrawSegment(x2,p2,color);
}
},
"b2internal function DrawShape",function(shape,xf,color){
switch(shape.m_type)
{
case Box2D.Collision.Shapes.b2Shape.e_circleShape:
{
var circle=(as(shape,Box2D.Collision.Shapes.b2CircleShape));
var center=Box2D.Common.Math.b2Math.MulX(xf,circle.m_p);
var radius=circle.m_radius;
var axis=xf.R.col1;
this.m_debugDraw$2.DrawSolidCircle(center,radius,axis,color);
}
break;
case Box2D.Collision.Shapes.b2Shape.e_polygonShape:
{
var i;
var poly=(as(shape,Box2D.Collision.Shapes.b2PolygonShape));
var vertexCount=poly.GetVertexCount();
var localVertices=poly.GetVertices();
var vertices=new Array(vertexCount);
for(i=0;i<vertexCount;++i)
{
vertices[i]=Box2D.Common.Math.b2Math.MulX(xf,localVertices[i]);
}
this.m_debugDraw$2.DrawSolidPolygon(vertices,vertexCount,color);
}
break;
}
},
"b2internal var",{m_flags:0},
"b2internal var",{m_contactManager:null},
"private var",{m_contactSolver:function(){return(new Box2D.Dynamics.Contacts.b2ContactSolver());}},
"private var",{m_island:function(){return(new Box2D.Dynamics.b2Island());}},
"b2internal var",{m_bodyList:null},
"private var",{m_jointList:null},
"b2internal var",{m_contactList:null},
"private var",{m_bodyCount:0},
"private var",{m_jointCount:0},
"private var",{m_controllerList:null},
"private var",{m_controllerCount:0},
"private var",{m_gravity:null},
"private var",{m_allowSleep:false},
"b2internal var",{m_groundBody:null},
"private var",{m_destructionListener:null},
"private var",{m_debugDraw:null},
"private var",{m_inv_dt0:NaN},
"static private var",{m_warmStarting:false},
"static private var",{m_continuousPhysics:false},
"public static const",{e_newFixture:0x0001},
"public static const",{e_locked:0x0002},
"public static const",{e_clearForces:0x0004},
"private var",{m_preStepEvent:function(){return(new flash.events.Event(Box2D.Dynamics.b2World.PRESTEP));}},
"private var",{m_postStepEvent:function(){return(new flash.events.Event(Box2D.Dynamics.b2World.POSTSTEP));}},
"private var",{m_addBodyEvent:function(){return(new Box2D.Dynamics.b2BodyEvent(Box2D.Dynamics.b2World.ADDBODY));}},
"private var",{m_removeBodyEvent:function(){return(new Box2D.Dynamics.b2BodyEvent(Box2D.Dynamics.b2World.REMOVEBODY));}},
"b2internal var",{m_addFixtureEvent:function(){return(new Box2D.Dynamics.b2FixtureEvent(Box2D.Dynamics.b2World.ADDFIXTURE));}},
"b2internal var",{m_removeFixtureEvent:function(){return(new Box2D.Dynamics.b2FixtureEvent(Box2D.Dynamics.b2World.REMOVEFIXTURE));}},
"private var",{m_addJointEvent:function(){return(new Box2D.Dynamics.b2JointEvent(Box2D.Dynamics.b2World.ADDJOINT));}},
"private var",{m_removeJointEvent:function(){return(new Box2D.Dynamics.b2JointEvent(Box2D.Dynamics.b2World.REMOVEJOINT));}},
];},[],["flash.events.EventDispatcher","Box2D.Dynamics.b2ContactManager","Box2D.Dynamics.b2BodyDef","Box2D.Dynamics.b2Body","Error","Box2D.Dynamics.Joints.b2Joint","Box2D.Dynamics.b2TimeStep","Box2D.Common.Math.b2Transform","Box2D.Common.Math.b2Vec2","Box2D.Collision.b2AABB","Box2D.Common.b2Color","Box2D.Dynamics.b2DebugDraw","Box2D.Dynamics.b2Fixture","Box2D.Collision.Shapes.b2Shape","Box2D.Common.b2Settings","Box2D.Collision.b2RayCastOutput","Box2D.Collision.b2RayCastInput","Array","Box2D.Dynamics.Contacts.b2Contact","Box2D.Common.Math.b2Sweep","Number","Box2D.Dynamics.Joints.b2PulleyJoint","Box2D.Collision.Shapes.b2CircleShape","Box2D.Common.Math.b2Math","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Dynamics.Contacts.b2ContactSolver","Box2D.Dynamics.b2Island","flash.events.Event","Box2D.Dynamics.b2BodyEvent","Box2D.Dynamics.b2FixtureEvent","Box2D.Dynamics.b2JointEvent"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Contacts.b2CircleContact
joo.classLoader.prepare(
"package Box2D.Dynamics.Contacts",
"public class b2CircleContact extends Box2D.Dynamics.Contacts.b2Contact",2,function($$private){var as=joo.as;return[

"static public function Create",function(allocator){
return new Box2D.Dynamics.Contacts.b2CircleContact();
},
"static public function Destroy",function(contact,allocator){
},
"public function Reset",function(fixtureA,fixtureB){
this.b2internal_Reset$2(fixtureA,fixtureB);
},
"b2internal override function Evaluate",function(){
var bA=this.m_fixtureA.GetBody();
var bB=this.m_fixtureB.GetBody();
Box2D.Collision.b2Collision.CollideCircles(this.m_manifold,as(
this.m_fixtureA.GetShape(),Box2D.Collision.Shapes.b2CircleShape),bA.m_xf,as(
this.m_fixtureB.GetShape(),Box2D.Collision.Shapes.b2CircleShape),bB.m_xf);
},
];},["Create","Destroy"],["Box2D.Dynamics.Contacts.b2Contact","Box2D.Collision.b2Collision","Box2D.Collision.Shapes.b2CircleShape"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Contacts.b2Contact
joo.classLoader.prepare(
"package Box2D.Dynamics.Contacts",
"public class b2Contact",1,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.b2Body,Box2D.Common.b2Settings);},

"public function GetManifold",function()
{
return this.m_manifold;
},
"public function GetWorldManifold",function(worldManifold)
{
var bodyA=this.m_fixtureA.GetBody();
var bodyB=this.m_fixtureB.GetBody();
var shapeA=this.m_fixtureA.GetShape();
var shapeB=this.m_fixtureB.GetShape();
worldManifold.Initialize(this.m_manifold,bodyA.GetTransform(),shapeA.m_radius,bodyB.GetTransform(),shapeB.m_radius);
},
"public function IsTouching",function()
{
return(this.m_flags&Box2D.Dynamics.Contacts.b2Contact.e_touchingFlag)==Box2D.Dynamics.Contacts.b2Contact.e_touchingFlag;
},
"public function IsContinuous",function()
{
return(this.m_flags&Box2D.Dynamics.Contacts.b2Contact.e_continuousFlag)==Box2D.Dynamics.Contacts.b2Contact.e_continuousFlag;
},
"public function SetSensor",function(sensor){
if(sensor)
{
this.m_flags|=Box2D.Dynamics.Contacts.b2Contact.e_sensorFlag;
}
else
{
this.m_flags&=~Box2D.Dynamics.Contacts.b2Contact.e_sensorFlag;
}
},
"public function IsSensor",function(){
return(this.m_flags&Box2D.Dynamics.Contacts.b2Contact.e_sensorFlag)==Box2D.Dynamics.Contacts.b2Contact.e_sensorFlag;
},
"public function SetEnabled",function(flag){
if(flag)
{
this.m_flags|=Box2D.Dynamics.Contacts.b2Contact.e_enabledFlag;
}
else
{
this.m_flags&=~Box2D.Dynamics.Contacts.b2Contact.e_enabledFlag;
}
},
"public function IsEnabled",function(){
return(this.m_flags&Box2D.Dynamics.Contacts.b2Contact.e_enabledFlag)==Box2D.Dynamics.Contacts.b2Contact.e_enabledFlag;
},
"public function GetNext",function(){
return this.m_next;
},
"public function GetFixtureA",function()
{
return this.m_fixtureA;
},
"public function GetFixtureB",function()
{
return this.m_fixtureB;
},
"public function FlagForFiltering",function()
{
this.m_flags|=Box2D.Dynamics.Contacts.b2Contact.e_filterFlag;
},
"static b2internal var",{e_sensorFlag:0x0001},
"static b2internal var",{e_continuousFlag:0x0002},
"static b2internal var",{e_islandFlag:0x0004},
"static b2internal var",{e_toiFlag:0x0008},
"static b2internal var",{e_touchingFlag:0x0010},
"static b2internal var",{e_enabledFlag:0x0020},
"static b2internal var",{e_filterFlag:0x0040},
"public function b2Contact",function()
{this.m_nodeA=this.m_nodeA();this.m_nodeB=this.m_nodeB();this.m_manifold=this.m_manifold();this.m_oldManifold=this.m_oldManifold();
},
"b2internal function b2internal_Reset",function(fixtureA,fixtureB)
{if(arguments.length<2){if(arguments.length<1){fixtureA=null;}fixtureB=null;}
this.m_flags=Box2D.Dynamics.Contacts.b2Contact.e_enabledFlag;
if(!fixtureA||!fixtureB){
this.m_fixtureA=null;
this.m_fixtureB=null;
return;
}
if(fixtureA.IsSensor()||fixtureB.IsSensor())
{
this.m_flags|=Box2D.Dynamics.Contacts.b2Contact.e_sensorFlag;
}
var bodyA=fixtureA.GetBody();
var bodyB=fixtureB.GetBody();
if(bodyA.GetType()!=Box2D.Dynamics.b2Body.b2_dynamicBody||bodyA.IsBullet()||bodyB.GetType()!=Box2D.Dynamics.b2Body.b2_dynamicBody||bodyB.IsBullet())
{
this.m_flags|=Box2D.Dynamics.Contacts.b2Contact.e_continuousFlag;
}
this.m_fixtureA=fixtureA;
this.m_fixtureB=fixtureB;
this.m_manifold.m_pointCount=0;
this.m_prev=null;
this.m_next=null;
this.m_nodeA.contact=null;
this.m_nodeA.prev=null;
this.m_nodeA.next=null;
this.m_nodeA.other=null;
this.m_nodeB.contact=null;
this.m_nodeB.prev=null;
this.m_nodeB.next=null;
this.m_nodeB.other=null;
},
"b2internal function Update",function(listener)
{
var tManifold=this.m_oldManifold;
this.m_oldManifold=this.m_manifold;
this.m_manifold=tManifold;
this.m_flags|=Box2D.Dynamics.Contacts.b2Contact.e_enabledFlag;
var touching=false;
var wasTouching=(this.m_flags&Box2D.Dynamics.Contacts.b2Contact.e_touchingFlag)==Box2D.Dynamics.Contacts.b2Contact.e_touchingFlag;
var bodyA=this.m_fixtureA.m_body;
var bodyB=this.m_fixtureB.m_body;
var aabbOverlap=this.m_fixtureA.m_aabb.TestOverlap(this.m_fixtureB.m_aabb);
if(this.m_flags&Box2D.Dynamics.Contacts.b2Contact.e_sensorFlag)
{
if(aabbOverlap)
{
var shapeA=this.m_fixtureA.GetShape();
var shapeB=this.m_fixtureB.GetShape();
var xfA=bodyA.GetTransform();
var xfB=bodyB.GetTransform();
touching=Box2D.Collision.Shapes.b2Shape.TestOverlap(shapeA,xfA,shapeB,xfB);
}
this.m_manifold.m_pointCount=0;
}
else
{
if(bodyA.GetType()!=Box2D.Dynamics.b2Body.b2_dynamicBody||bodyA.IsBullet()||bodyB.GetType()!=Box2D.Dynamics.b2Body.b2_dynamicBody||bodyB.IsBullet())
{
this.m_flags|=Box2D.Dynamics.Contacts.b2Contact.e_continuousFlag;
}
else
{
this.m_flags&=~Box2D.Dynamics.Contacts.b2Contact.e_continuousFlag;
}
if(aabbOverlap)
{
this.Evaluate();
touching=this.m_manifold.m_pointCount>0;
for(var i=0;i<this.m_manifold.m_pointCount;++i)
{
var mp2=this.m_manifold.m_points[i];
mp2.m_normalImpulse=0.0;
mp2.m_tangentImpulse=0.0;
var id2=mp2.m_id;
for(var j=0;j<this.m_oldManifold.m_pointCount;++j)
{
var mp1=this.m_oldManifold.m_points[j];
if(mp1.m_id.key==id2.key)
{
mp2.m_normalImpulse=mp1.m_normalImpulse;
mp2.m_tangentImpulse=mp1.m_tangentImpulse;
break;
}
}
}
}
else
{
this.m_manifold.m_pointCount=0;
}
if(touching!=wasTouching)
{
bodyA.SetAwake(true);
bodyB.SetAwake(true);
}
}
if(touching)
{
this.m_flags|=Box2D.Dynamics.Contacts.b2Contact.e_touchingFlag;
}
else
{
this.m_flags&=~Box2D.Dynamics.Contacts.b2Contact.e_touchingFlag;
}
if(!wasTouching&&touching&&listener)
{
listener.BeginContact(this);
}
if(wasTouching&&!touching&&listener)
{
listener.EndContact(this);
}
if((this.m_flags&Box2D.Dynamics.Contacts.b2Contact.e_sensorFlag)==0&&touching&&listener)
{
listener.PreSolve(this,this.m_oldManifold);
}
},
"b2internal virtual function Evaluate",function(){},
"private static var",{s_input:function(){return(new Box2D.Collision.b2TOIInput());}},
"b2internal function ComputeTOI",function(sweepA,sweepB)
{
$$private.s_input.proxyA.Set(this.m_fixtureA.GetShape());
$$private.s_input.proxyB.Set(this.m_fixtureB.GetShape());
$$private.s_input.sweepA=sweepA;
$$private.s_input.sweepB=sweepB;
$$private.s_input.tolerance=Box2D.Common.b2Settings.b2_linearSlop;
return Box2D.Collision.b2TimeOfImpact.TimeOfImpact($$private.s_input);
},
"b2internal var",{m_flags:0},
"b2internal var",{m_prev:null},
"b2internal var",{m_next:null},
"b2internal var",{m_nodeA:function(){return(new Box2D.Dynamics.Contacts.b2ContactEdge());}},
"b2internal var",{m_nodeB:function(){return(new Box2D.Dynamics.Contacts.b2ContactEdge());}},
"b2internal var",{m_fixtureA:null},
"b2internal var",{m_fixtureB:null},
"b2internal var",{m_manifold:function(){return(new Box2D.Collision.b2Manifold());}},
"b2internal var",{m_oldManifold:function(){return(new Box2D.Collision.b2Manifold());}},
"b2internal var",{m_toi:NaN},
];},[],["Box2D.Dynamics.b2Body","Box2D.Collision.Shapes.b2Shape","Box2D.Collision.b2TOIInput","Box2D.Common.b2Settings","Box2D.Collision.b2TimeOfImpact","Box2D.Dynamics.Contacts.b2ContactEdge","Box2D.Collision.b2Manifold"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Contacts.b2ContactConstraint
joo.classLoader.prepare(
"package Box2D.Dynamics.Contacts",
"public class b2ContactConstraint",1,function($$private){;return[function(){joo.classLoader.init(Box2D.Common.b2Settings);},

"public function b2ContactConstraint",function(){this.localPlaneNormal=this.localPlaneNormal();this.localPoint=this.localPoint();this.normal=this.normal();this.normalMass=this.normalMass();this.K=this.K();
this.points=new Array(Box2D.Common.b2Settings.b2_maxManifoldPoints);
for(var i=0;i<Box2D.Common.b2Settings.b2_maxManifoldPoints;i++){
this.points[i]=new Box2D.Dynamics.Contacts.b2ContactConstraintPoint();
}
},
"public var",{points:null},
"public var",{localPlaneNormal:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{localPoint:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{normal:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{normalMass:function(){return(new Box2D.Common.Math.b2Mat22());}},
"public var",{K:function(){return(new Box2D.Common.Math.b2Mat22());}},
"public var",{bodyA:null},
"public var",{bodyB:null},
"public var",{type:0},
"public var",{radius:NaN},
"public var",{friction:NaN},
"public var",{restitution:NaN},
"public var",{pointCount:0},
"public var",{manifold:null},
];},[],["Array","Box2D.Common.b2Settings","Box2D.Dynamics.Contacts.b2ContactConstraintPoint","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Mat22"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Contacts.b2ContactConstraintPoint
joo.classLoader.prepare(
"package Box2D.Dynamics.Contacts",
"public class b2ContactConstraintPoint",1,function($$private){;return[

"public var",{localPoint:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{rA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{rB:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{normalImpulse:NaN},
"public var",{tangentImpulse:NaN},
"public var",{normalMass:NaN},
"public var",{tangentMass:NaN},
"public var",{equalizedMass:NaN},
"public var",{velocityBias:NaN},
"public function b2ContactConstraintPoint",function b2ContactConstraintPoint$(){this.localPoint=this.localPoint();this.rA=this.rA();this.rB=this.rB();}];},[],["Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Contacts.b2ContactEdge
joo.classLoader.prepare(
"package Box2D.Dynamics.Contacts",
"public class b2ContactEdge",1,function($$private){;return[

"public var",{other:null},
"public var",{contact:null},
"public var",{prev:null},
"public var",{next:null},
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Contacts.b2ContactFactory
joo.classLoader.prepare(
"package Box2D.Dynamics.Contacts",
"public class b2ContactFactory",1,function($$private){var assert=joo.assert;return[function(){joo.classLoader.init(Box2D.Collision.Shapes.b2Shape,Box2D.Dynamics.Contacts.b2PolygonContact,Box2D.Dynamics.Contacts.b2CircleContact,Box2D.Dynamics.Contacts.b2PolyAndCircleContact);},

"function b2ContactFactory",function(allocator)
{
this.m_allocator$1=allocator;
this.InitializeRegisters();
},
"b2internal function AddType",function(createFcn,destroyFcn,type1,type2)
{
this.m_registers$1[type1][type2].createFcn=createFcn;
this.m_registers$1[type1][type2].destroyFcn=destroyFcn;
this.m_registers$1[type1][type2].primary=true;
if(type1!=type2)
{
this.m_registers$1[type2][type1].createFcn=createFcn;
this.m_registers$1[type2][type1].destroyFcn=destroyFcn;
this.m_registers$1[type2][type1].primary=false;
}
},
"b2internal function InitializeRegisters",function(){
this.m_registers$1=new Array(Box2D.Collision.Shapes.b2Shape.e_shapeTypeCount);
for(var i=0;i<Box2D.Collision.Shapes.b2Shape.e_shapeTypeCount;i++){
this.m_registers$1[i]=new Array(Box2D.Collision.Shapes.b2Shape.e_shapeTypeCount);
for(var j=0;j<Box2D.Collision.Shapes.b2Shape.e_shapeTypeCount;j++){
this.m_registers$1[i][j]=new Box2D.Dynamics.Contacts.b2ContactRegister();
}
}
this.AddType(Box2D.Dynamics.Contacts.b2CircleContact.Create,Box2D.Dynamics.Contacts.b2CircleContact.Destroy,Box2D.Collision.Shapes.b2Shape.e_circleShape,Box2D.Collision.Shapes.b2Shape.e_circleShape);
this.AddType(Box2D.Dynamics.Contacts.b2PolyAndCircleContact.Create,Box2D.Dynamics.Contacts.b2PolyAndCircleContact.Destroy,Box2D.Collision.Shapes.b2Shape.e_polygonShape,Box2D.Collision.Shapes.b2Shape.e_circleShape);
this.AddType(Box2D.Dynamics.Contacts.b2PolygonContact.Create,Box2D.Dynamics.Contacts.b2PolygonContact.Destroy,Box2D.Collision.Shapes.b2Shape.e_polygonShape,Box2D.Collision.Shapes.b2Shape.e_polygonShape);
},
"public function Create",function(fixtureA,fixtureB){
var type1=fixtureA.GetType();
var type2=fixtureB.GetType();
var reg=this.m_registers$1[type1][type2];
var poolReg=reg;
if(!reg.primary)poolReg=this.m_registers$1[type2][type1];
var c;
if(poolReg.pool)
{
c=poolReg.pool;
poolReg.pool=c.m_next;
poolReg.poolCount--;
}else{
var createFcn=reg.createFcn;
if(createFcn==null)return null;
c=createFcn(this.m_allocator$1);
}
if(reg.primary)
{
c.b2internal_Reset(fixtureA,fixtureB);
}
else
{
c.b2internal_Reset(fixtureB,fixtureA);
}
return c;
},
"public function Destroy",function(contact){
if(contact.m_manifold.m_pointCount>0)
{
contact.m_fixtureA.m_body.SetAwake(true);
contact.m_fixtureB.m_body.SetAwake(true);
}
var type1=contact.m_fixtureA.GetType();
var type2=contact.m_fixtureB.GetType();
var reg=this.m_registers$1[type1][type2];
if(true)
{
reg.poolCount++;
contact.m_next=reg.pool;
reg.pool=contact;
}
var destroyFcn=reg.destroyFcn;
destroyFcn(contact,this.m_allocator$1);
},
"private var",{m_registers:null},
"private var",{m_allocator:undefined},
];},[],["Array","Box2D.Collision.Shapes.b2Shape","Box2D.Dynamics.Contacts.b2ContactRegister","Box2D.Dynamics.Contacts.b2CircleContact","Box2D.Dynamics.Contacts.b2PolyAndCircleContact","Box2D.Dynamics.Contacts.b2PolygonContact"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Contacts.b2ContactRegister
joo.classLoader.prepare(
"package Box2D.Dynamics.Contacts",
"public class b2ContactRegister",1,function($$private){;return[

"public var",{createFcn:null},
"public var",{destroyFcn:null},
"public var",{primary:false},
"public var",{pool:null},
"public var",{poolCount:0},
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Contacts.b2ContactResult
joo.classLoader.prepare(
"package Box2D.Dynamics.Contacts",
"public class b2ContactResult",1,function($$private){;return[

"public var",{shape1:null},
"public var",{shape2:null},
"public var",{position:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{normal:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{normalImpulse:NaN},
"public var",{tangentImpulse:NaN},
"public var",{id:function(){return(new Box2D.Collision.b2ContactID());}},
"public function b2ContactResult",function b2ContactResult$(){this.position=this.position();this.normal=this.normal();this.id=this.id();}];},[],["Box2D.Common.Math.b2Vec2","Box2D.Collision.b2ContactID"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Contacts.b2ContactSolver
joo.classLoader.prepare(
"package Box2D.Dynamics.Contacts",
"public class b2ContactSolver",1,function($$private){var assert=joo.assert;return[function(){joo.classLoader.init(Box2D.Common.b2Settings);},

"public function b2ContactSolver",function()
{this.m_step$1=this.m_step$1();this.m_constraints=this.m_constraints();
},
"private static var",{s_worldManifold:function(){return(new Box2D.Collision.b2WorldManifold());}},
"public function Initialize",function(step,contacts,contactCount,allocator)
{
var contact;
this.m_step$1.Set(step);
this.m_allocator$1=allocator;
var i;
var tVec;
var tMat;
this.m_constraintCount$1=contactCount;
while(this.m_constraints.length<this.m_constraintCount$1)
{
this.m_constraints[this.m_constraints.length]=new Box2D.Dynamics.Contacts.b2ContactConstraint();
}
for(i=0;i<contactCount;++i)
{
contact=contacts[i];
var fixtureA=contact.m_fixtureA;
var fixtureB=contact.m_fixtureB;
var shapeA=fixtureA.m_shape;
var shapeB=fixtureB.m_shape;
var radiusA=shapeA.m_radius;
var radiusB=shapeB.m_radius;
var bodyA=fixtureA.m_body;
var bodyB=fixtureB.m_body;
var manifold=contact.GetManifold();
var friction=Box2D.Common.b2Settings.b2MixFriction(fixtureA.GetFriction(),fixtureB.GetFriction());
var restitution=Box2D.Common.b2Settings.b2MixRestitution(fixtureA.GetRestitution(),fixtureB.GetRestitution());
var vAX=bodyA.m_linearVelocity.x;
var vAY=bodyA.m_linearVelocity.y;
var vBX=bodyB.m_linearVelocity.x;
var vBY=bodyB.m_linearVelocity.y;
var wA=bodyA.m_angularVelocity;
var wB=bodyB.m_angularVelocity;
$$private.s_worldManifold.Initialize(manifold,bodyA.m_xf,radiusA,bodyB.m_xf,radiusB);
var normalX=$$private.s_worldManifold.m_normal.x;
var normalY=$$private.s_worldManifold.m_normal.y;
var cc=this.m_constraints[i];
cc.bodyA=bodyA;
cc.bodyB=bodyB;
cc.manifold=manifold;
cc.normal.x=normalX;
cc.normal.y=normalY;
cc.pointCount=manifold.m_pointCount;
cc.friction=friction;
cc.restitution=restitution;
cc.localPlaneNormal.x=manifold.m_localPlaneNormal.x;
cc.localPlaneNormal.y=manifold.m_localPlaneNormal.y;
cc.localPoint.x=manifold.m_localPoint.x;
cc.localPoint.y=manifold.m_localPoint.y;
cc.radius=radiusA+radiusB;
cc.type=manifold.m_type;
for(var k=0;k<cc.pointCount;++k)
{
var cp=manifold.m_points[k];
var ccp=cc.points[k];
ccp.normalImpulse=cp.m_normalImpulse;
ccp.tangentImpulse=cp.m_tangentImpulse;
ccp.localPoint.SetV(cp.m_localPoint);
var rAX=ccp.rA.x=$$private.s_worldManifold.m_points[k].x-bodyA.m_sweep.c.x;
var rAY=ccp.rA.y=$$private.s_worldManifold.m_points[k].y-bodyA.m_sweep.c.y;
var rBX=ccp.rB.x=$$private.s_worldManifold.m_points[k].x-bodyB.m_sweep.c.x;
var rBY=ccp.rB.y=$$private.s_worldManifold.m_points[k].y-bodyB.m_sweep.c.y;
var rnA=rAX*normalY-rAY*normalX;
var rnB=rBX*normalY-rBY*normalX;
rnA*=rnA;
rnB*=rnB;
var kNormal=bodyA.m_invMass+bodyB.m_invMass+bodyA.m_invI*rnA+bodyB.m_invI*rnB;
ccp.normalMass=1.0/kNormal;
var kEqualized=bodyA.m_mass*bodyA.m_invMass+bodyB.m_mass*bodyB.m_invMass;
kEqualized+=bodyA.m_mass*bodyA.m_invI*rnA+bodyB.m_mass*bodyB.m_invI*rnB;
ccp.equalizedMass=1.0/kEqualized;
var tangentX=normalY;
var tangentY=-normalX;
var rtA=rAX*tangentY-rAY*tangentX;
var rtB=rBX*tangentY-rBY*tangentX;
rtA*=rtA;
rtB*=rtB;
var kTangent=bodyA.m_invMass+bodyB.m_invMass+bodyA.m_invI*rtA+bodyB.m_invI*rtB;
ccp.tangentMass=1.0/kTangent;
ccp.velocityBias=0.0;
var tX=vBX+(-wB*rBY)-vAX-(-wA*rAY);
var tY=vBY+(wB*rBX)-vAY-(wA*rAX);
var vRel=cc.normal.x*tX+cc.normal.y*tY;
if(vRel<-Box2D.Common.b2Settings.b2_velocityThreshold)
{
ccp.velocityBias+=-cc.restitution*vRel;
}
}
if(cc.pointCount==2)
{
var ccp1=cc.points[0];
var ccp2=cc.points[1];
var invMassA=bodyA.m_invMass;
var invIA=bodyA.m_invI;
var invMassB=bodyB.m_invMass;
var invIB=bodyB.m_invI;
var rn1A=ccp1.rA.x*normalY-ccp1.rA.y*normalX;
var rn1B=ccp1.rB.x*normalY-ccp1.rB.y*normalX;
var rn2A=ccp2.rA.x*normalY-ccp2.rA.y*normalX;
var rn2B=ccp2.rB.x*normalY-ccp2.rB.y*normalX;
var k11=invMassA+invMassB+invIA*rn1A*rn1A+invIB*rn1B*rn1B;
var k22=invMassA+invMassB+invIA*rn2A*rn2A+invIB*rn2B*rn2B;
var k12=invMassA+invMassB+invIA*rn1A*rn2A+invIB*rn1B*rn2B;
var k_maxConditionNumber=100.0;
if(k11*k11<k_maxConditionNumber*(k11*k22-k12*k12))
{
cc.K.col1.Set(k11,k12);
cc.K.col2.Set(k12,k22);
cc.K.GetInverse(cc.normalMass);
}
else
{
cc.pointCount=1;
}
}
}
},
"public function InitVelocityConstraints",function(step){
var tVec;
var tVec2;
var tMat;
for(var i=0;i<this.m_constraintCount$1;++i)
{
var c=this.m_constraints[i];
var bodyA=c.bodyA;
var bodyB=c.bodyB;
var invMassA=bodyA.m_invMass;
var invIA=bodyA.m_invI;
var invMassB=bodyB.m_invMass;
var invIB=bodyB.m_invI;
var normalX=c.normal.x;
var normalY=c.normal.y;
var tangentX=normalY;
var tangentY=-normalX;
var tX;
var j;
var tCount;
if(step.warmStarting)
{
tCount=c.pointCount;
for(j=0;j<tCount;++j)
{
var ccp=c.points[j];
ccp.normalImpulse*=step.dtRatio;
ccp.tangentImpulse*=step.dtRatio;
var PX=ccp.normalImpulse*normalX+ccp.tangentImpulse*tangentX;
var PY=ccp.normalImpulse*normalY+ccp.tangentImpulse*tangentY;
bodyA.m_angularVelocity-=invIA*(ccp.rA.x*PY-ccp.rA.y*PX);
bodyA.m_linearVelocity.x-=invMassA*PX;
bodyA.m_linearVelocity.y-=invMassA*PY;
bodyB.m_angularVelocity+=invIB*(ccp.rB.x*PY-ccp.rB.y*PX);
bodyB.m_linearVelocity.x+=invMassB*PX;
bodyB.m_linearVelocity.y+=invMassB*PY;
}
}
else
{
tCount=c.pointCount;
for(j=0;j<tCount;++j)
{
var ccp2=c.points[j];
ccp2.normalImpulse=0.0;
ccp2.tangentImpulse=0.0;
}
}
}
},
"public function SolveVelocityConstraints",function(){
var j;
var ccp;
var rAX;
var rAY;
var rBX;
var rBY;
var dvX;
var dvY;
var vn;
var vt;
var lambda;
var maxFriction;
var newImpulse;
var PX;
var PY;
var dX;
var dY;
var P1X;
var P1Y;
var P2X;
var P2Y;
var tMat;
var tVec;
for(var i=0;i<this.m_constraintCount$1;++i)
{
var c=this.m_constraints[i];
var bodyA=c.bodyA;
var bodyB=c.bodyB;
var wA=bodyA.m_angularVelocity;
var wB=bodyB.m_angularVelocity;
var vA=bodyA.m_linearVelocity;
var vB=bodyB.m_linearVelocity;
var invMassA=bodyA.m_invMass;
var invIA=bodyA.m_invI;
var invMassB=bodyB.m_invMass;
var invIB=bodyB.m_invI;
var normalX=c.normal.x;
var normalY=c.normal.y;
var tangentX=normalY;
var tangentY=-normalX;
var friction=c.friction;
var tX;
for(j=0;j<c.pointCount;j++)
{
ccp=c.points[j];
dvX=vB.x-wB*ccp.rB.y-vA.x+wA*ccp.rA.y;
dvY=vB.y+wB*ccp.rB.x-vA.y-wA*ccp.rA.x;
vt=dvX*tangentX+dvY*tangentY;
lambda=ccp.tangentMass*-vt;
maxFriction=friction*ccp.normalImpulse;
newImpulse=Box2D.Common.Math.b2Math.Clamp(ccp.tangentImpulse+lambda,-maxFriction,maxFriction);
lambda=newImpulse-ccp.tangentImpulse;
PX=lambda*tangentX;
PY=lambda*tangentY;
vA.x-=invMassA*PX;
vA.y-=invMassA*PY;
wA-=invIA*(ccp.rA.x*PY-ccp.rA.y*PX);
vB.x+=invMassB*PX;
vB.y+=invMassB*PY;
wB+=invIB*(ccp.rB.x*PY-ccp.rB.y*PX);
ccp.tangentImpulse=newImpulse;
}
var tCount=c.pointCount;
if(c.pointCount==1)
{
ccp=c.points[0];
dvX=vB.x+(-wB*ccp.rB.y)-vA.x-(-wA*ccp.rA.y);
dvY=vB.y+(wB*ccp.rB.x)-vA.y-(wA*ccp.rA.x);
vn=dvX*normalX+dvY*normalY;
lambda=-ccp.normalMass*(vn-ccp.velocityBias);
newImpulse=ccp.normalImpulse+lambda;
newImpulse=newImpulse>0?newImpulse:0.0;
lambda=newImpulse-ccp.normalImpulse;
PX=lambda*normalX;
PY=lambda*normalY;
vA.x-=invMassA*PX;
vA.y-=invMassA*PY;
wA-=invIA*(ccp.rA.x*PY-ccp.rA.y*PX);
vB.x+=invMassB*PX;
vB.y+=invMassB*PY;
wB+=invIB*(ccp.rB.x*PY-ccp.rB.y*PX);
ccp.normalImpulse=newImpulse;
}
else
{
var cp1=c.points[0];
var cp2=c.points[1];
var aX=cp1.normalImpulse;
var aY=cp2.normalImpulse;
var dv1X=vB.x-wB*cp1.rB.y-vA.x+wA*cp1.rA.y;
var dv1Y=vB.y+wB*cp1.rB.x-vA.y-wA*cp1.rA.x;
var dv2X=vB.x-wB*cp2.rB.y-vA.x+wA*cp2.rA.y;
var dv2Y=vB.y+wB*cp2.rB.x-vA.y-wA*cp2.rA.x;
var vn1=dv1X*normalX+dv1Y*normalY;
var vn2=dv2X*normalX+dv2Y*normalY;
var bX=vn1-cp1.velocityBias;
var bY=vn2-cp2.velocityBias;
tMat=c.K;
bX-=tMat.col1.x*aX+tMat.col2.x*aY;
bY-=tMat.col1.y*aX+tMat.col2.y*aY;
var k_errorTol=0.001;
for(;;)
{
tMat=c.normalMass;
var xX=-(tMat.col1.x*bX+tMat.col2.x*bY);
var xY=-(tMat.col1.y*bX+tMat.col2.y*bY);
if(xX>=0.0&&xY>=0.0){
dX=xX-aX;
dY=xY-aY;
P1X=dX*normalX;
P1Y=dX*normalY;
P2X=dY*normalX;
P2Y=dY*normalY;
vA.x-=invMassA*(P1X+P2X);
vA.y-=invMassA*(P1Y+P2Y);
wA-=invIA*(cp1.rA.x*P1Y-cp1.rA.y*P1X+cp2.rA.x*P2Y-cp2.rA.y*P2X);
vB.x+=invMassB*(P1X+P2X);
vB.y+=invMassB*(P1Y+P2Y);
wB+=invIB*(cp1.rB.x*P1Y-cp1.rB.y*P1X+cp2.rB.x*P2Y-cp2.rB.y*P2X);
cp1.normalImpulse=xX;
cp2.normalImpulse=xY;
break;
}
xX=-cp1.normalMass*bX;
xY=0.0;
vn1=0.0;
vn2=c.K.col1.y*xX+bY;
if(xX>=0.0&&vn2>=0.0)
{
dX=xX-aX;
dY=xY-aY;
P1X=dX*normalX;
P1Y=dX*normalY;
P2X=dY*normalX;
P2Y=dY*normalY;
vA.x-=invMassA*(P1X+P2X);
vA.y-=invMassA*(P1Y+P2Y);
wA-=invIA*(cp1.rA.x*P1Y-cp1.rA.y*P1X+cp2.rA.x*P2Y-cp2.rA.y*P2X);
vB.x+=invMassB*(P1X+P2X);
vB.y+=invMassB*(P1Y+P2Y);
wB+=invIB*(cp1.rB.x*P1Y-cp1.rB.y*P1X+cp2.rB.x*P2Y-cp2.rB.y*P2X);
cp1.normalImpulse=xX;
cp2.normalImpulse=xY;
break;
}
xX=0.0;
xY=-cp2.normalMass*bY;
vn1=c.K.col2.x*xY+bX;
vn2=0.0;
if(xY>=0.0&&vn1>=0.0)
{
dX=xX-aX;
dY=xY-aY;
P1X=dX*normalX;
P1Y=dX*normalY;
P2X=dY*normalX;
P2Y=dY*normalY;
vA.x-=invMassA*(P1X+P2X);
vA.y-=invMassA*(P1Y+P2Y);
wA-=invIA*(cp1.rA.x*P1Y-cp1.rA.y*P1X+cp2.rA.x*P2Y-cp2.rA.y*P2X);
vB.x+=invMassB*(P1X+P2X);
vB.y+=invMassB*(P1Y+P2Y);
wB+=invIB*(cp1.rB.x*P1Y-cp1.rB.y*P1X+cp2.rB.x*P2Y-cp2.rB.y*P2X);
cp1.normalImpulse=xX;
cp2.normalImpulse=xY;
break;
}
xX=0.0;
xY=0.0;
vn1=bX;
vn2=bY;
if(vn1>=0.0&&vn2>=0.0){
dX=xX-aX;
dY=xY-aY;
P1X=dX*normalX;
P1Y=dX*normalY;
P2X=dY*normalX;
P2Y=dY*normalY;
vA.x-=invMassA*(P1X+P2X);
vA.y-=invMassA*(P1Y+P2Y);
wA-=invIA*(cp1.rA.x*P1Y-cp1.rA.y*P1X+cp2.rA.x*P2Y-cp2.rA.y*P2X);
vB.x+=invMassB*(P1X+P2X);
vB.y+=invMassB*(P1Y+P2Y);
wB+=invIB*(cp1.rB.x*P1Y-cp1.rB.y*P1X+cp2.rB.x*P2Y-cp2.rB.y*P2X);
cp1.normalImpulse=xX;
cp2.normalImpulse=xY;
break;
}
break;
}
}
bodyA.m_angularVelocity=wA;
bodyB.m_angularVelocity=wB;
}
},
"public function FinalizeVelocityConstraints",function()
{
for(var i=0;i<this.m_constraintCount$1;++i)
{
var c=this.m_constraints[i];
var m=c.manifold;
for(var j=0;j<c.pointCount;++j)
{
var point1=m.m_points[j];
var point2=c.points[j];
point1.m_normalImpulse=point2.normalImpulse;
point1.m_tangentImpulse=point2.tangentImpulse;
}
}
},
"private static var",{s_psm:function(){return(new Box2D.Dynamics.Contacts.b2PositionSolverManifold());}},
"public function SolvePositionConstraints",function(baumgarte)
{
var minSeparation=0.0;
for(var i=0;i<this.m_constraintCount$1;i++)
{
var c=this.m_constraints[i];
var bodyA=c.bodyA;
var bodyB=c.bodyB;
var invMassA=bodyA.m_mass*bodyA.m_invMass;
var invIA=bodyA.m_mass*bodyA.m_invI;
var invMassB=bodyB.m_mass*bodyB.m_invMass;
var invIB=bodyB.m_mass*bodyB.m_invI;
$$private.s_psm.Initialize(c);
var normal=$$private.s_psm.m_normal;
for(var j=0;j<c.pointCount;j++)
{
var ccp=c.points[j];
var point=$$private.s_psm.m_points[j];
var separation=$$private.s_psm.m_separations[j];
var rAX=point.x-bodyA.m_sweep.c.x;
var rAY=point.y-bodyA.m_sweep.c.y;
var rBX=point.x-bodyB.m_sweep.c.x;
var rBY=point.y-bodyB.m_sweep.c.y;
minSeparation=minSeparation<separation?minSeparation:separation;
var C=Box2D.Common.Math.b2Math.Clamp(baumgarte*(separation+Box2D.Common.b2Settings.b2_linearSlop),-Box2D.Common.b2Settings.b2_maxLinearCorrection,0.0);
var impulse=-ccp.equalizedMass*C;
var PX=impulse*normal.x;
var PY=impulse*normal.y;
bodyA.m_sweep.c.x-=invMassA*PX;
bodyA.m_sweep.c.y-=invMassA*PY;
bodyA.m_sweep.a-=invIA*(rAX*PY-rAY*PX);
bodyA.SynchronizeTransform();
bodyB.m_sweep.c.x+=invMassB*PX;
bodyB.m_sweep.c.y+=invMassB*PY;
bodyB.m_sweep.a+=invIB*(rBX*PY-rBY*PX);
bodyB.SynchronizeTransform();
}
}
return minSeparation>-1.5*Box2D.Common.b2Settings.b2_linearSlop;
},
"private var",{m_step:function(){return(new Box2D.Dynamics.b2TimeStep());}},
"private var",{m_allocator:undefined},
"b2internal var",{m_constraints:function(){return(new Array());}},
"private var",{m_constraintCount:0},
];},[],["Box2D.Collision.b2WorldManifold","Box2D.Dynamics.Contacts.b2ContactConstraint","Box2D.Common.b2Settings","Box2D.Common.Math.b2Math","Box2D.Dynamics.Contacts.b2PositionSolverManifold","Box2D.Dynamics.b2TimeStep","Array"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Contacts.b2PolyAndCircleContact
joo.classLoader.prepare(
"package Box2D.Dynamics.Contacts",
"public class b2PolyAndCircleContact extends Box2D.Dynamics.Contacts.b2Contact",2,function($$private){var as=joo.as,assert=joo.assert;return[function(){joo.classLoader.init(Box2D.Collision.Shapes.b2Shape);},
"static public function Create",function(allocator){
return new Box2D.Dynamics.Contacts.b2PolyAndCircleContact();
},
"static public function Destroy",function(contact,allocator){
},
"public function Reset",function(fixtureA,fixtureB){
this.b2internal_Reset$2(fixtureA,fixtureB);
},
"b2internal override function Evaluate",function(){
var bA=this.m_fixtureA.m_body;
var bB=this.m_fixtureB.m_body;
Box2D.Collision.b2Collision.CollidePolygonAndCircle(this.m_manifold,as(
this.m_fixtureA.GetShape(),Box2D.Collision.Shapes.b2PolygonShape),bA.m_xf,as(
this.m_fixtureB.GetShape(),Box2D.Collision.Shapes.b2CircleShape),bB.m_xf);
},
];},["Create","Destroy"],["Box2D.Dynamics.Contacts.b2Contact","Box2D.Collision.Shapes.b2Shape","Box2D.Collision.b2Collision","Box2D.Collision.Shapes.b2PolygonShape","Box2D.Collision.Shapes.b2CircleShape"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Contacts.b2PolygonContact
joo.classLoader.prepare(
"package Box2D.Dynamics.Contacts",
"public class b2PolygonContact extends Box2D.Dynamics.Contacts.b2Contact",2,function($$private){var as=joo.as;return[

"static public function Create",function(allocator){
return new Box2D.Dynamics.Contacts.b2PolygonContact();
},
"static public function Destroy",function(contact,allocator){
},
"public function Reset",function(fixtureA,fixtureB){
this.b2internal_Reset$2(fixtureA,fixtureB);
},
"b2internal override function Evaluate",function(){
var bA=this.m_fixtureA.GetBody();
var bB=this.m_fixtureB.GetBody();
Box2D.Collision.b2Collision.CollidePolygons(this.m_manifold,as(
this.m_fixtureA.GetShape(),Box2D.Collision.Shapes.b2PolygonShape),bA.m_xf,as(
this.m_fixtureB.GetShape(),Box2D.Collision.Shapes.b2PolygonShape),bB.m_xf);
},
];},["Create","Destroy"],["Box2D.Dynamics.Contacts.b2Contact","Box2D.Collision.b2Collision","Box2D.Collision.Shapes.b2PolygonShape"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Contacts.b2PositionSolverManifold
joo.classLoader.prepare(
"package Box2D.Dynamics.Contacts",
"internal class b2PositionSolverManifold",1,function($$private){var assert=joo.assert;return[function(){joo.classLoader.init(Box2D.Collision.b2Manifold,Number,Box2D.Common.b2Settings);},

"public function b2PositionSolverManifold",function()
{
this.m_normal=new Box2D.Common.Math.b2Vec2();
this.m_separations=new Array(Box2D.Common.b2Settings.b2_maxManifoldPoints);
this.m_points=new Array(Box2D.Common.b2Settings.b2_maxManifoldPoints);
for(var i=0;i<Box2D.Common.b2Settings.b2_maxManifoldPoints;i++)
{
this.m_points[i]=new Box2D.Common.Math.b2Vec2();
}
},
"private static var",{circlePointA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private static var",{circlePointB:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public function Initialize",function(cc)
{
var i;
var clipPointX;
var clipPointY;
var tMat;
var tVec;
var planePointX;
var planePointY;
switch(cc.type)
{
case Box2D.Collision.b2Manifold.e_circles:
{
tMat=cc.bodyA.m_xf.R;
tVec=cc.localPoint;
var pointAX=cc.bodyA.m_xf.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
var pointAY=cc.bodyA.m_xf.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
tMat=cc.bodyB.m_xf.R;
tVec=cc.points[0].localPoint;
var pointBX=cc.bodyB.m_xf.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
var pointBY=cc.bodyB.m_xf.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
var dX=pointBX-pointAX;
var dY=pointBY-pointAY;
var d2=dX*dX+dY*dY;
if(d2>Number.MIN_VALUE*Number.MIN_VALUE)
{
var d=Math.sqrt(d2);
this.m_normal.x=dX/d;
this.m_normal.y=dY/d;
}
else
{
this.m_normal.x=1.0;
this.m_normal.y=0.0;
}
this.m_points[0].x=0.5*(pointAX+pointBX);
this.m_points[0].y=0.5*(pointAY+pointBY);
this.m_separations[0]=dX*this.m_normal.x+dY*this.m_normal.y-cc.radius;
}
break;
case Box2D.Collision.b2Manifold.e_faceA:
{
tMat=cc.bodyA.m_xf.R;
tVec=cc.localPlaneNormal;
this.m_normal.x=tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
this.m_normal.y=tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
tMat=cc.bodyA.m_xf.R;
tVec=cc.localPoint;
planePointX=cc.bodyA.m_xf.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
planePointY=cc.bodyA.m_xf.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
tMat=cc.bodyB.m_xf.R;
for(i=0;i<cc.pointCount;++i)
{
tVec=cc.points[i].localPoint;
clipPointX=cc.bodyB.m_xf.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
clipPointY=cc.bodyB.m_xf.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
this.m_separations[i]=(clipPointX-planePointX)*this.m_normal.x+(clipPointY-planePointY)*this.m_normal.y-cc.radius;
this.m_points[i].x=clipPointX;
this.m_points[i].y=clipPointY;
}
}
break;
case Box2D.Collision.b2Manifold.e_faceB:
{
tMat=cc.bodyB.m_xf.R;
tVec=cc.localPlaneNormal;
this.m_normal.x=tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
this.m_normal.y=tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
tMat=cc.bodyB.m_xf.R;
tVec=cc.localPoint;
planePointX=cc.bodyB.m_xf.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
planePointY=cc.bodyB.m_xf.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
tMat=cc.bodyA.m_xf.R;
for(i=0;i<cc.pointCount;++i)
{
tVec=cc.points[i].localPoint;
clipPointX=cc.bodyA.m_xf.position.x+(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
clipPointY=cc.bodyA.m_xf.position.y+(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
this.m_separations[i]=(clipPointX-planePointX)*this.m_normal.x+(clipPointY-planePointY)*this.m_normal.y-cc.radius;
this.m_points[i].Set(clipPointX,clipPointY);
}
this.m_normal.x*=-1;
this.m_normal.y*=-1;
}
break;
}
},
"public var",{m_normal:null},
"public var",{m_points:null},
"public var",{m_separations:null},
];},[],["Box2D.Common.Math.b2Vec2","Array","Box2D.Common.b2Settings","Box2D.Collision.b2Manifold","Number","Math"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Controllers.b2AABBBodyIterable
joo.classLoader.prepare(
"package Box2D.Dynamics.Controllers",
"public class b2AABBBodyIterable implements Box2D.Dynamics.Controllers.IBodyIterable",1,function($$private){var as=joo.as;return[

"public function b2AABBBodyIterable",function(world,aabb)
{
this.m_world$1=world;
this.m_aabb$1=aabb.Copy();
},
"public function GetIterator",function()
{
var iterator=new Box2D.Dynamics.Controllers.b2ManualBodyIterator();
iterator.bodyList=new Array();
return this.ResetIterator(iterator);
},
"public function ResetIterator",function(iterator)
{
var iterator2=as(iterator,Box2D.Dynamics.Controllers.b2ManualBodyIterator);
var bodyList=iterator2.bodyList;
bodyList.length=0;
function callback(fixture)
{
var body=fixture.GetBody();
if(bodyList.indexOf(body)===-1)
bodyList[bodyList.length]=body;
return true;
}
this.m_world$1.QueryAABB(callback,this.m_aabb$1);
return iterator2;
},
"private var",{m_world:null},
"private var",{m_aabb:null},
];},[],["Box2D.Dynamics.Controllers.IBodyIterable","Box2D.Dynamics.Controllers.b2ManualBodyIterator","Array"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Controllers.b2BuoyancyController
joo.classLoader.prepare(
"package Box2D.Dynamics.Controllers",
"public class b2BuoyancyController extends Box2D.Dynamics.Controllers.b2Controller",2,function($$private){;return[function(){joo.classLoader.init(Number);},

"public var",{normal:function(){return(new Box2D.Common.Math.b2Vec2(0,-1));}},
"public var",{offset:0},
"public var",{density:0},
"public var",{velocity:function(){return(new Box2D.Common.Math.b2Vec2(0,0));}},
"public var",{linearDrag:2},
"public var",{angularDrag:1},
"public var",{useDensity:false},
"public var",{useWorldGravity:true},
"public var",{gravity:null},
"public override function Step",function(step){
if(this.useWorldGravity){
this.gravity=this.GetWorld().GetGravity().Copy();
}
for(this.m_iterator$2=this.m_bodyIterable.ResetIterator(this.m_iterator$2);this.m_iterator$2.HasNext();)
{
var body=this.m_iterator$2.Next();
if(body.IsAwake()==false){
continue;
}
var areac=new Box2D.Common.Math.b2Vec2();
var massc=new Box2D.Common.Math.b2Vec2();
var area=0.0;
var mass=0.0;
for(var fixture=body.GetFixtureList();fixture;fixture=fixture.GetNext()){
var sc=new Box2D.Common.Math.b2Vec2();
var sarea=fixture.GetShape().ComputeSubmergedArea(this.normal,this.offset,body.GetTransform(),sc);
area+=sarea;
areac.x+=sarea*sc.x;
areac.y+=sarea*sc.y;
var shapeDensity;
if(this.useDensity){
shapeDensity=1;
}else{
shapeDensity=1;
}
mass+=sarea*shapeDensity;
massc.x+=sarea*sc.x*shapeDensity;
massc.y+=sarea*sc.y*shapeDensity;
}
areac.x/=area;
areac.y/=area;
massc.x/=mass;
massc.y/=mass;
if(area<Number.MIN_VALUE)
continue;
var buoyancyForce=this.gravity.GetNegative();
buoyancyForce.Multiply(this.density*area);
body.ApplyForce(buoyancyForce,massc);
var dragForce=body.GetLinearVelocityFromWorldPoint(areac);
dragForce.Subtract(this.velocity);
dragForce.Multiply(-this.linearDrag*area);
body.ApplyForce(dragForce,areac);
body.ApplyTorque(-body.GetInertia()/body.GetMass()*area*body.GetAngularVelocity()*this.angularDrag);
}
},
"public override function Draw",function(debugDraw)
{
var r=1000;
var p1=new Box2D.Common.Math.b2Vec2();
var p2=new Box2D.Common.Math.b2Vec2();
p1.x=this.normal.x*this.offset+this.normal.y*r;
p1.y=this.normal.y*this.offset-this.normal.x*r;
p2.x=this.normal.x*this.offset-this.normal.y*r;
p2.y=this.normal.y*this.offset+this.normal.x*r;
var color=new Box2D.Common.b2Color(0,0,1);
debugDraw.DrawSegment(p1,p2,color);
},
"public override function SetBodyIterable",function(iterable)
{
this.SetBodyIterable$2(iterable);
this.m_iterator$2=this.m_bodyIterable.GetIterator();
},
"private var",{m_iterator:null},
"public function b2BuoyancyController",function b2BuoyancyController$(){this.super$2();this.normal=this.normal();this.velocity=this.velocity();}];},[],["Box2D.Dynamics.Controllers.b2Controller","Box2D.Common.Math.b2Vec2","Number","Box2D.Common.b2Color"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Controllers.b2ConstantAccelController
joo.classLoader.prepare(
"package Box2D.Dynamics.Controllers",
"public class b2ConstantAccelController extends Box2D.Dynamics.Controllers.b2Controller",2,function($$private){;return[

"public var",{A:function(){return(new Box2D.Common.Math.b2Vec2(0,0));}},
"public override function Step",function(step){
var smallA=new Box2D.Common.Math.b2Vec2(this.A.x*step.dt,this.A.y*step.dt);
for(this.m_iterator$2=this.m_bodyIterable.ResetIterator(this.m_iterator$2);this.m_iterator$2.HasNext();)
{
var body=this.m_iterator$2.Next();
if(!body.IsAwake())
continue;
body.SetLinearVelocity(new Box2D.Common.Math.b2Vec2(
body.GetLinearVelocity().x+smallA.x,
body.GetLinearVelocity().y+smallA.y
));
}
},
"public override function SetBodyIterable",function(iterable)
{
this.SetBodyIterable$2(iterable);
this.m_iterator$2=this.m_bodyIterable.GetIterator();
},
"private var",{m_iterator:null},
"public function b2ConstantAccelController",function b2ConstantAccelController$(){this.super$2();this.A=this.A();}];},[],["Box2D.Dynamics.Controllers.b2Controller","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Controllers.b2ConstantForceController
joo.classLoader.prepare(
"package Box2D.Dynamics.Controllers",
"public class b2ConstantForceController extends Box2D.Dynamics.Controllers.b2Controller",2,function($$private){;return[

"public var",{F:function(){return(new Box2D.Common.Math.b2Vec2(0,0));}},
"public override function Step",function(step){
for(this.m_iterator$2=this.m_bodyIterable.ResetIterator(this.m_iterator$2);this.m_iterator$2.HasNext();)
{
var body=this.m_iterator$2.Next();
if(!body.IsAwake())
continue;
body.ApplyForce(this.F,body.GetWorldCenter());
}
},
"public override function SetBodyIterable",function(iterable)
{
this.SetBodyIterable$2(iterable);
this.m_iterator$2=this.m_bodyIterable.GetIterator();
},
"private var",{m_iterator:null},
"public function b2ConstantForceController",function b2ConstantForceController$(){this.super$2();this.F=this.F();}];},[],["Box2D.Dynamics.Controllers.b2Controller","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Controllers.b2Controller
joo.classLoader.prepare(
"package Box2D.Dynamics.Controllers",
"public class b2Controller",1,function($$private){var is=joo.is,as=joo.as;return[

"public function b2Controller",function()
{
this.SetBodyIterable(new Box2D.Dynamics.Controllers.b2ManualBodyIterable());
},
"public virtual function Step",function(step){},
"public virtual function Draw",function(debugDraw){},
"public function AddBody",function(body)
{
this.GetManualBodyIterable$1().AddBody(body);
},
"public function RemoveBody",function(body)
{
this.GetManualBodyIterable$1().RemoveBody(body);
},
"public function Clear",function()
{
this.GetManualBodyIterable$1().Clear();
},
"public function GetNext",function(){return this.m_next;},
"public function GetWorld",function(){return this.m_world;},
"public function SetBodyIterable",function(iterable)
{
this.m_bodyIterable=iterable;
},
"public function GetBodyIterable",function()
{
return this.m_bodyIterable;
},
"private function GetManualBodyIterable",function()
{
if(is(this.m_bodyIterable,Box2D.Dynamics.Controllers.b2ManualBodyIterable))
{
return as(this.m_bodyIterable,Box2D.Dynamics.Controllers.b2ManualBodyIterable);
}else{
throw new TypeError("The current iterable doesn't support this operation.");
}
},
"b2internal var",{m_next:null},
"b2internal var",{m_prev:null},
"protected var",{m_bodyIterable:null},
"b2internal var",{m_world:null},
];},[],["Box2D.Dynamics.Controllers.b2ManualBodyIterable","TypeError"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Controllers.b2GravityController
joo.classLoader.prepare(
"package Box2D.Dynamics.Controllers",
"public class b2GravityController extends Box2D.Dynamics.Controllers.b2Controller",2,function($$private){;return[function(){joo.classLoader.init(Number);},

"public var",{G:1},
"public var",{invSqr:true},
"public override function Step",function(step){
var body1=null;
var p1=null;
var mass1=0;
var body2=null;
var p2=null;
var dx=0;
var dy=0;
var r2=0;
var f=null;
if(this.invSqr){
for(this.m_iterator1$2=this.m_bodyIterable.ResetIterator(this.m_iterator1$2);this.m_iterator1$2.HasNext();){
body1=this.m_iterator1$2.Next();
p1=body1.GetWorldCenter();
mass1=body1.GetMass();
for(this.m_iterator2$2=this.m_bodyIterable.ResetIterator(this.m_iterator2$2);this.m_iterator2$2.HasNext();){
body2=this.m_iterator2$2.Next();
p2=body2.GetWorldCenter();
dx=p2.x-p1.x;
dy=p2.y-p1.y;
r2=dx*dx+dy*dy;
if(r2<Number.MIN_VALUE)
continue;
f=new Box2D.Common.Math.b2Vec2(dx,dy);
f.Multiply(this.G/r2/Math.sqrt(r2)*mass1*body2.GetMass());
if(body1.IsAwake())
body1.ApplyForce(f,p1);
f.Multiply(-1);
if(body2.IsAwake())
body2.ApplyForce(f,p2);
}
}
}else{
for(this.m_iterator1$2=this.m_bodyIterable.ResetIterator(this.m_iterator1$2);this.m_iterator1$2.HasNext();){
body1=this.m_iterator1$2.Next();
p1=body1.GetWorldCenter();
mass1=body1.GetMass();
for(this.m_iterator2$2=this.m_bodyIterable.ResetIterator(this.m_iterator2$2);this.m_iterator2$2.HasNext();){
body2=this.m_iterator2$2.Next();
p2=body2.GetWorldCenter();
dx=p2.x-p1.x;
dy=p2.y-p1.y;
r2=dx*dx+dy*dy;
if(r2<Number.MIN_VALUE)
continue;
f=new Box2D.Common.Math.b2Vec2(dx,dy);
f.Multiply(this.G/r2*mass1*body2.GetMass());
if(body1.IsAwake())
body1.ApplyForce(f,p1);
f.Multiply(-1);
if(body2.IsAwake())
body2.ApplyForce(f,p2);
}
}
}
},
"public override function SetBodyIterable",function(iterable)
{
this.SetBodyIterable$2(iterable);
this.m_iterator1$2=this.m_bodyIterable.GetIterator();
this.m_iterator2$2=this.m_bodyIterable.GetIterator();
},
"private var",{m_iterator1:null},
"private var",{m_iterator2:null},
];},[],["Box2D.Dynamics.Controllers.b2Controller","Number","Box2D.Common.Math.b2Vec2","Math"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Controllers.b2ManualBodyIterable
joo.classLoader.prepare(
"package Box2D.Dynamics.Controllers",
"public class b2ManualBodyIterable implements Box2D.Dynamics.Controllers.IBodyIterable",1,function($$private){var as=joo.as,$$bound=joo.boundMethod;return[function(){joo.classLoader.init(Box2D.Dynamics.b2World);},

"public static function FromBodies",function()
{var bodies=arguments;
return Box2D.Dynamics.Controllers.b2ManualBodyIterable.FromArray(bodies);
},
"public static function FromArray",function(bodyList)
{
var bodyList2=new Array();
for(var $1 in bodyList)
{var body=bodyList[$1];
bodyList2[bodyList2.length]=body;
}
return new Box2D.Dynamics.Controllers.b2ManualBodyIterable(bodyList2);
},
"public function b2ManualBodyIterable",function(bodyList)
{if(arguments.length<1){bodyList=null;}
var bodyList2=new Array();
if(bodyList)
{
for(var $1 in bodyList)
{var body=bodyList[$1];
bodyList2[bodyList2.length]=body;
}
}
this.m_bodyList$1=bodyList2;
},
"public function AddBody",function(body)
{
var p=this.m_bodyList$1.indexOf(body);
if(p!=-1)
return;
this.m_bodyList$1[this.m_bodyList$1.length]=body;
this.GetEventDispatcher$1(body).addEventListener(Box2D.Dynamics.b2World.REMOVEBODY,$$bound(this,"OnBodyRemoved$1"),false,0,true);
},
"public function RemoveBody",function(body)
{
var p=this.m_bodyList$1.indexOf(body);
if(p==-1)
return;
this.m_bodyList$1.splice(p,1);
this.GetEventDispatcher$1(body).removeEventListener(Box2D.Dynamics.b2World.REMOVEBODY,$$bound(this,"OnBodyRemoved$1"));
},
"public function Clear",function()
{
while(this.m_bodyList$1.length>0)
this.RemoveBody(this.m_bodyList$1[0]);
},
"private function OnBodyRemoved",function(e)
{
this.RemoveBody(e.body);
},
"private function GetEventDispatcher",function(body)
{
var dispatcher=body.GetEventDispatcher();
if(!dispatcher)
{
dispatcher=new flash.events.EventDispatcher();
body.SetEventDispatcher(dispatcher);
}
return dispatcher;
},
"public function GetIterator",function()
{
var iter=new Box2D.Dynamics.Controllers.b2ManualBodyIterator();
iter.bodyList=this.m_bodyList$1;
return iter;
},
"public function ResetIterator",function(iterator)
{
var iter=as(iterator,Box2D.Dynamics.Controllers.b2ManualBodyIterator);
iter.bodyList=this.m_bodyList$1;
iter.position=0;
return iter;
},
"private var",{m_bodyList:null},
];},["FromBodies","FromArray"],["Box2D.Dynamics.Controllers.IBodyIterable","Array","Box2D.Dynamics.b2World","flash.events.EventDispatcher","Box2D.Dynamics.Controllers.b2ManualBodyIterator"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Controllers.b2ManualBodyIterator
joo.classLoader.prepare("package Box2D.Dynamics.Controllers",
"internal class b2ManualBodyIterator implements Box2D.Dynamics.Controllers.IBodyIterator",1,function($$private){;return[

"public var",{position:0},
"public var",{bodyList:null},
"public function HasNext",function()
{
return this.position<this.bodyList.length;
},
"public function Next",function()
{
return this.bodyList[this.position++];
},
];},[],["Box2D.Dynamics.Controllers.IBodyIterator"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Controllers.b2TensorDampingController
joo.classLoader.prepare(
"package Box2D.Dynamics.Controllers",
"public class b2TensorDampingController extends Box2D.Dynamics.Controllers.b2Controller",2,function($$private){;return[function(){joo.classLoader.init(Number);},

"public var",{T:function(){return(new Box2D.Common.Math.b2Mat22());}},
"public var",{maxTimestep:0},
"public function SetAxisAligned",function(xDamping,yDamping){
this.T.col1.x=-xDamping;
this.T.col1.y=0;
this.T.col2.x=0;
this.T.col2.y=-yDamping;
if(xDamping>0||yDamping>0){
this.maxTimestep=1/Math.max(xDamping,yDamping);
}else{
this.maxTimestep=0;
}
},
"public override function Step",function(step){
var timestep=step.dt;
if(timestep<=Number.MIN_VALUE)
return;
if(timestep>this.maxTimestep&&this.maxTimestep>0)
timestep=this.maxTimestep;
for(this.m_iterator$2=this.m_bodyIterable.ResetIterator(this.m_iterator$2);this.m_iterator$2.HasNext();)
{
var body=this.m_iterator$2.Next();
if(!body.IsAwake()){
continue;
}
var damping=
body.GetWorldVector(
Box2D.Common.Math.b2Math.MulMV(this.T,
body.GetLocalVector(
body.GetLinearVelocity()
)
)
);
body.SetLinearVelocity(new Box2D.Common.Math.b2Vec2(
body.GetLinearVelocity().x+damping.x*timestep,
body.GetLinearVelocity().y+damping.y*timestep
));
}
},
"public override function SetBodyIterable",function(iterable)
{
this.SetBodyIterable$2(iterable);
this.m_iterator$2=this.m_bodyIterable.GetIterator();
},
"private var",{m_iterator:null},
"public function b2TensorDampingController",function b2TensorDampingController$(){this.super$2();this.T=this.T();}];},[],["Box2D.Dynamics.Controllers.b2Controller","Box2D.Common.Math.b2Mat22","Math","Number","Box2D.Common.Math.b2Math","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Controllers.b2TouchingBodyIterable
joo.classLoader.prepare(
"package Box2D.Dynamics.Controllers",
"public class b2TouchingBodyIterable implements Box2D.Dynamics.Controllers.IBodyIterable",1,function($$private){var as=joo.as;return[

"public function b2TouchingBodyIterable",function(body)
{
this.m_body$1=body;
},
"public function GetIterator",function()
{
var iterator=new Box2D.Dynamics.Controllers.b2TouchingBodyIterable.b2TouchingBodyIterator();
return this.ResetIterator(iterator);
},
"public function ResetIterator",function(iterator)
{
var iterator2=as(iterator,Box2D.Dynamics.Controllers.b2TouchingBodyIterable.b2TouchingBodyIterator);
iterator2.contactEdge=this.m_body$1.GetContactList();
iterator2.seen.length=0;
return iterator2;
},
"private var",{m_body:null},

"internal class b2TouchingBodyIterator implements Box2D.Dynamics.Controllers.IBodyIterator",1,function($$private){;return[

"public var",{contactEdge:null},
"public var",{seen:function(){return(new Array());}},
"public function HasNext",function()
{
return this.contactEdge!=null;
},
"public function Next",function()
{
var localBody=this.contactEdge.other;
this.seen[this.seen.length]=localBody;
this.Seek();
return localBody;
},
"public function Seek",function()
{
while(this.contactEdge&&this.seen.indexOf(this.contactEdge.other)!==-1)
{
this.contactEdge=this.contactEdge.next;
}
},
"public function b2TouchingBodyIterator",function b2TouchingBodyIterator$(){this.seen=this.seen();}];},[],];},[],["Box2D.Dynamics.Controllers.IBodyIterable","Box2D.Dynamics.Controllers.IBodyIterator","Array"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Controllers.b2WorldBodyIterable
joo.classLoader.prepare(
"package Box2D.Dynamics.Controllers",
"public class b2WorldBodyIterable implements Box2D.Dynamics.Controllers.IBodyIterable",1,function($$private){var is=joo.is,as=joo.as;return[

"public function b2WorldBodyIterable",function(world)
{
this.m_world$1=world;
},
"public function GetFilter",function()
{
return this.m_filter$1;
},
"public function SetFilter",function(filter)
{
this.m_filter$1=filter;
},
"public function GetIterator",function()
{
if(this.m_filter$1!=null)
return new Box2D.Dynamics.Controllers.b2WorldBodyIterable.b2FilteringWorldBodyIterator(this.m_world$1,this.m_filter$1);
else
return new Box2D.Dynamics.Controllers.b2WorldBodyIterable.b2WorldBodyIterator(this.m_world$1);
},
"public function ResetIterator",function(iterator)
{
if(this.m_filter$1!=null)
{
if(is(iterator,Box2D.Dynamics.Controllers.b2WorldBodyIterable.b2FilteringWorldBodyIterator))
{
var iter1=as(iterator,Box2D.Dynamics.Controllers.b2WorldBodyIterable.b2FilteringWorldBodyIterator);
iter1.Reset(this.m_world$1,this.m_filter$1);
return iter1;
}else{
return this.GetIterator();
}
}else{
if(is(iterator,Box2D.Dynamics.Controllers.b2WorldBodyIterable.b2WorldBodyIterator))
{
var iter2=as(iterator,Box2D.Dynamics.Controllers.b2WorldBodyIterable.b2WorldBodyIterator);
iter2.Reset(this.m_world$1);
return iter2;
}else{
return this.GetIterator();
}
}
},
"private var",{m_filter:null},
"private var",{m_world:null},

"internal class b2WorldBodyIterator implements Box2D.Dynamics.Controllers.IBodyIterator",1,function($$private){;return[

"public var",{body:null},
"public function b2WorldBodyIterator",function(world)
{
this.Reset(world);
},
"public function Reset",function(world)
{
this.body=world.GetBodyList();
},
"public function HasNext",function()
{
return this.body!=null;
},
"public function Next",function()
{
var localBody=this.body;
this.body=this.body.GetNext();
return localBody;
},
];},[],
"internal class b2FilteringWorldBodyIterator implements Box2D.Dynamics.Controllers.IBodyIterator",1,function($$private){;return[

"public var",{body:null},
"public var",{filter:null},
"public function b2FilteringWorldBodyIterator",function(world,filter)
{
this.Reset(world,filter);
},
"public function Reset",function(world,filter)
{
this.body=world.GetBodyList();
this.filter=filter;
this.Seek$1();
},
"public function HasNext",function()
{
return this.body!=null;
},
"public function Next",function()
{
var localBody=this.body;
this.body=this.body.GetNext();
this.Seek$1();
return localBody;
},
"private function Seek",function()
{
while(this.body&&!this.filter(this.body))
{
this.body=this.body.GetNext();
}
},
];},[],];},[],["Box2D.Dynamics.Controllers.IBodyIterable","Box2D.Dynamics.Controllers.IBodyIterator"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Controllers.IBodyIterable
joo.classLoader.prepare(
"package Box2D.Dynamics.Controllers",
"public interface IBodyIterable",1,function($$private){;return[
,,
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Controllers.IBodyIterator
joo.classLoader.prepare(
"package Box2D.Dynamics.Controllers",
"public interface IBodyIterator",1,function($$private){;return[
,,
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.IContactListener
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public interface IContactListener",1,function($$private){;return[
,,,,
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.IDestructionListener
joo.classLoader.prepare(
"package Box2D.Dynamics",
"public interface IDestructionListener",1,function($$private){;return[
,,
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2DistanceJoint
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2DistanceJoint extends Box2D.Dynamics.Joints.b2Joint",2,function($$private){;return[function(){joo.classLoader.init(Math,Box2D.Common.b2Settings);},

"public override function GetAnchorA",function(){
return this.m_bodyA.GetWorldPoint(this.m_localAnchor1$2);
},
"public override function GetAnchorB",function(){
return this.m_bodyB.GetWorldPoint(this.m_localAnchor2$2);
},
"public override function GetReactionForce",function(inv_dt)
{
return new Box2D.Common.Math.b2Vec2(inv_dt*this.m_impulse$2*this.m_u$2.x,inv_dt*this.m_impulse$2*this.m_u$2.y);
},
"public override function GetReactionTorque",function(inv_dt)
{
return 0.0;
},
"public function GetLength",function()
{
return this.m_length$2;
},
"public function SetLength",function(length)
{
this.m_length$2=length;
},
"public function GetFrequency",function()
{
return this.m_frequencyHz$2;
},
"public function SetFrequency",function(hz)
{
this.m_frequencyHz$2=hz;
},
"public function GetDampingRatio",function()
{
return this.m_dampingRatio$2;
},
"public function SetDampingRatio",function(ratio)
{
this.m_dampingRatio$2=ratio;
},
"public function b2DistanceJoint",function(def){
this.super$2(def);this.m_localAnchor1$2=this.m_localAnchor1$2();this.m_localAnchor2$2=this.m_localAnchor2$2();this.m_u$2=this.m_u$2();
var tMat;
var tX;
var tY;
this.m_localAnchor1$2.SetV(def.localAnchorA);
this.m_localAnchor2$2.SetV(def.localAnchorB);
this.m_length$2=def.length;
this.m_frequencyHz$2=def.frequencyHz;
this.m_dampingRatio$2=def.dampingRatio;
this.m_impulse$2=0.0;
this.m_gamma$2=0.0;
this.m_bias$2=0.0;
},
"b2internal override function InitVelocityConstraints",function(step){
var tMat;
var tX;
var bA=this.m_bodyA;
var bB=this.m_bodyB;
tMat=bA.m_xf.R;
var r1X=this.m_localAnchor1$2.x-bA.m_sweep.localCenter.x;
var r1Y=this.m_localAnchor1$2.y-bA.m_sweep.localCenter.y;
tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=bB.m_xf.R;
var r2X=this.m_localAnchor2$2.x-bB.m_sweep.localCenter.x;
var r2Y=this.m_localAnchor2$2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
this.m_u$2.x=bB.m_sweep.c.x+r2X-bA.m_sweep.c.x-r1X;
this.m_u$2.y=bB.m_sweep.c.y+r2Y-bA.m_sweep.c.y-r1Y;
var length=Math.sqrt(this.m_u$2.x*this.m_u$2.x+this.m_u$2.y*this.m_u$2.y);
if(length>Box2D.Common.b2Settings.b2_linearSlop)
{
this.m_u$2.Multiply(1.0/length);
}
else
{
this.m_u$2.SetZero();
}
var cr1u=(r1X*this.m_u$2.y-r1Y*this.m_u$2.x);
var cr2u=(r2X*this.m_u$2.y-r2Y*this.m_u$2.x);
var invMass=bA.m_invMass+bA.m_invI*cr1u*cr1u+bB.m_invMass+bB.m_invI*cr2u*cr2u;
this.m_mass$2=invMass!=0.0?1.0/invMass:0.0;
if(this.m_frequencyHz$2>0.0)
{
var C=length-this.m_length$2;
var omega=2.0*Math.PI*this.m_frequencyHz$2;
var d=2.0*this.m_mass$2*this.m_dampingRatio$2*omega;
var k=this.m_mass$2*omega*omega;
this.m_gamma$2=step.dt*(d+step.dt*k);
this.m_gamma$2=this.m_gamma$2!=0.0?1/this.m_gamma$2:0.0;
this.m_bias$2=C*step.dt*k*this.m_gamma$2;
this.m_mass$2=invMass+this.m_gamma$2;
this.m_mass$2=this.m_mass$2!=0.0?1.0/this.m_mass$2:0.0;
}
if(step.warmStarting)
{
this.m_impulse$2*=step.dtRatio;
var PX=this.m_impulse$2*this.m_u$2.x;
var PY=this.m_impulse$2*this.m_u$2.y;
bA.m_linearVelocity.x-=bA.m_invMass*PX;
bA.m_linearVelocity.y-=bA.m_invMass*PY;
bA.m_angularVelocity-=bA.m_invI*(r1X*PY-r1Y*PX);
bB.m_linearVelocity.x+=bB.m_invMass*PX;
bB.m_linearVelocity.y+=bB.m_invMass*PY;
bB.m_angularVelocity+=bB.m_invI*(r2X*PY-r2Y*PX);
}
else
{
this.m_impulse$2=0.0;
}
},
"b2internal override function SolveVelocityConstraints",function(step){
var tMat;
var bA=this.m_bodyA;
var bB=this.m_bodyB;
tMat=bA.m_xf.R;
var r1X=this.m_localAnchor1$2.x-bA.m_sweep.localCenter.x;
var r1Y=this.m_localAnchor1$2.y-bA.m_sweep.localCenter.y;
var tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=bB.m_xf.R;
var r2X=this.m_localAnchor2$2.x-bB.m_sweep.localCenter.x;
var r2Y=this.m_localAnchor2$2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
var v1X=bA.m_linearVelocity.x+(-bA.m_angularVelocity*r1Y);
var v1Y=bA.m_linearVelocity.y+(bA.m_angularVelocity*r1X);
var v2X=bB.m_linearVelocity.x+(-bB.m_angularVelocity*r2Y);
var v2Y=bB.m_linearVelocity.y+(bB.m_angularVelocity*r2X);
var Cdot=(this.m_u$2.x*(v2X-v1X)+this.m_u$2.y*(v2Y-v1Y));
var impulse=-this.m_mass$2*(Cdot+this.m_bias$2+this.m_gamma$2*this.m_impulse$2);
this.m_impulse$2+=impulse;
var PX=impulse*this.m_u$2.x;
var PY=impulse*this.m_u$2.y;
bA.m_linearVelocity.x-=bA.m_invMass*PX;
bA.m_linearVelocity.y-=bA.m_invMass*PY;
bA.m_angularVelocity-=bA.m_invI*(r1X*PY-r1Y*PX);
bB.m_linearVelocity.x+=bB.m_invMass*PX;
bB.m_linearVelocity.y+=bB.m_invMass*PY;
bB.m_angularVelocity+=bB.m_invI*(r2X*PY-r2Y*PX);
},
"b2internal override function SolvePositionConstraints",function(baumgarte)
{
var tMat;
if(this.m_frequencyHz$2>0.0)
{
return true;
}
var bA=this.m_bodyA;
var bB=this.m_bodyB;
tMat=bA.m_xf.R;
var r1X=this.m_localAnchor1$2.x-bA.m_sweep.localCenter.x;
var r1Y=this.m_localAnchor1$2.y-bA.m_sweep.localCenter.y;
var tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=bB.m_xf.R;
var r2X=this.m_localAnchor2$2.x-bB.m_sweep.localCenter.x;
var r2Y=this.m_localAnchor2$2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
var dX=bB.m_sweep.c.x+r2X-bA.m_sweep.c.x-r1X;
var dY=bB.m_sweep.c.y+r2Y-bA.m_sweep.c.y-r1Y;
var length=Math.sqrt(dX*dX+dY*dY);
dX/=length;
dY/=length;
var C=length-this.m_length$2;
C=Box2D.Common.Math.b2Math.Clamp(C,-Box2D.Common.b2Settings.b2_maxLinearCorrection,Box2D.Common.b2Settings.b2_maxLinearCorrection);
var impulse=-this.m_mass$2*C;
this.m_u$2.Set(dX,dY);
var PX=impulse*this.m_u$2.x;
var PY=impulse*this.m_u$2.y;
bA.m_sweep.c.x-=bA.m_invMass*PX;
bA.m_sweep.c.y-=bA.m_invMass*PY;
bA.m_sweep.a-=bA.m_invI*(r1X*PY-r1Y*PX);
bB.m_sweep.c.x+=bB.m_invMass*PX;
bB.m_sweep.c.y+=bB.m_invMass*PY;
bB.m_sweep.a+=bB.m_invI*(r2X*PY-r2Y*PX);
bA.SynchronizeTransform();
bB.SynchronizeTransform();
return Box2D.Common.Math.b2Math.Abs(C)<Box2D.Common.b2Settings.b2_linearSlop;
},
"private var",{m_localAnchor1:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_localAnchor2:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_u:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_frequencyHz:NaN},
"private var",{m_dampingRatio:NaN},
"private var",{m_gamma:NaN},
"private var",{m_bias:NaN},
"private var",{m_impulse:NaN},
"private var",{m_mass:NaN},
"private var",{m_length:NaN},
];},[],["Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2","Math","Box2D.Common.b2Settings","Box2D.Common.Math.b2Math"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2DistanceJointDef
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2DistanceJointDef extends Box2D.Dynamics.Joints.b2JointDef",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint);},

"public function b2DistanceJointDef",function()
{this.super$2();this.localAnchorA=this.localAnchorA();this.localAnchorB=this.localAnchorB();
this.type=Box2D.Dynamics.Joints.b2Joint.e_distanceJoint;
this.length=1.0;
this.frequencyHz=0.0;
this.dampingRatio=0.0;
},
"public function Initialize",function(bA,bB,
anchorA,anchorB)
{
this.bodyA=bA;
this.bodyB=bB;
this.localAnchorA.SetV(this.bodyA.GetLocalPoint(anchorA));
this.localAnchorB.SetV(this.bodyB.GetLocalPoint(anchorB));
var dX=anchorB.x-anchorA.x;
var dY=anchorB.y-anchorA.y;
this.length=Math.sqrt(dX*dX+dY*dY);
this.frequencyHz=0.0;
this.dampingRatio=0.0;
},
"public var",{localAnchorA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{localAnchorB:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{length:NaN},
"public var",{frequencyHz:NaN},
"public var",{dampingRatio:NaN},
];},[],["Box2D.Dynamics.Joints.b2JointDef","Box2D.Dynamics.Joints.b2Joint","Math","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2FrictionJoint
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2FrictionJoint extends Box2D.Dynamics.Joints.b2Joint",2,function($$private){;return[

"public override function GetAnchorA",function(){
return this.m_bodyA.GetWorldPoint(this.m_localAnchorA$2);
},
"public override function GetAnchorB",function(){
return this.m_bodyB.GetWorldPoint(this.m_localAnchorB$2);
},
"public override function GetReactionForce",function(inv_dt)
{
return new Box2D.Common.Math.b2Vec2(inv_dt*this.m_linearImpulse$2.x,inv_dt*this.m_linearImpulse$2.y);
},
"public override function GetReactionTorque",function(inv_dt)
{
return inv_dt*this.m_angularImpulse$2;
},
"public function SetMaxForce",function(force)
{
this.m_maxForce$2=force;
},
"public function GetMaxForce",function()
{
return this.m_maxForce$2;
},
"public function SetMaxTorque",function(torque)
{
this.m_maxTorque$2=torque;
},
"public function GetMaxTorque",function()
{
return this.m_maxTorque$2;
},
"public function b2FrictionJoint",function(def){
this.super$2(def);this.m_localAnchorA$2=this.m_localAnchorA$2();this.m_localAnchorB$2=this.m_localAnchorB$2();this.m_linearMass=this.m_linearMass();this.m_linearImpulse$2=this.m_linearImpulse$2();
this.m_localAnchorA$2.SetV(def.localAnchorA);
this.m_localAnchorB$2.SetV(def.localAnchorB);
this.m_linearMass.SetZero();
this.m_angularMass=0.0;
this.m_linearImpulse$2.SetZero();
this.m_angularImpulse$2=0.0;
this.m_maxForce$2=def.maxForce;
this.m_maxTorque$2=def.maxTorque;
},
"b2internal override function InitVelocityConstraints",function(step){
var tMat;
var tX;
var bA=this.m_bodyA;
var bB=this.m_bodyB;
tMat=bA.m_xf.R;
var rAX=this.m_localAnchorA$2.x-bA.m_sweep.localCenter.x;
var rAY=this.m_localAnchorA$2.y-bA.m_sweep.localCenter.y;
tX=(tMat.col1.x*rAX+tMat.col2.x*rAY);
rAY=(tMat.col1.y*rAX+tMat.col2.y*rAY);
rAX=tX;
tMat=bB.m_xf.R;
var rBX=this.m_localAnchorB$2.x-bB.m_sweep.localCenter.x;
var rBY=this.m_localAnchorB$2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*rBX+tMat.col2.x*rBY);
rBY=(tMat.col1.y*rBX+tMat.col2.y*rBY);
rBX=tX;
var mA=bA.m_invMass;
var mB=bB.m_invMass;
var iA=bA.m_invI;
var iB=bB.m_invI;
var K=new Box2D.Common.Math.b2Mat22();
K.col1.x=mA+mB;K.col2.x=0.0;
K.col1.y=0.0;K.col2.y=mA+mB;
K.col1.x+=iA*rAY*rAY;K.col2.x+=-iA*rAX*rAY;
K.col1.y+=-iA*rAX*rAY;K.col2.y+=iA*rAX*rAX;
K.col1.x+=iB*rBY*rBY;K.col2.x+=-iB*rBX*rBY;
K.col1.y+=-iB*rBX*rBY;K.col2.y+=iB*rBX*rBX;
K.GetInverse(this.m_linearMass);
this.m_angularMass=iA+iB;
if(this.m_angularMass>0.0)
{
this.m_angularMass=1.0/this.m_angularMass;
}
if(step.warmStarting)
{
this.m_linearImpulse$2.x*=step.dtRatio;
this.m_linearImpulse$2.y*=step.dtRatio;
this.m_angularImpulse$2*=step.dtRatio;
var P=this.m_linearImpulse$2;
bA.m_linearVelocity.x-=mA*P.x;
bA.m_linearVelocity.y-=mA*P.y;
bA.m_angularVelocity-=iA*(rAX*P.y-rAY*P.x+this.m_angularImpulse$2);
bB.m_linearVelocity.x+=mB*P.x;
bB.m_linearVelocity.y+=mB*P.y;
bB.m_angularVelocity+=iB*(rBX*P.y-rBY*P.x+this.m_angularImpulse$2);
}
else
{
this.m_linearImpulse$2.SetZero();
this.m_angularImpulse$2=0.0;
}
},
"b2internal override function SolveVelocityConstraints",function(step){
var tMat;
var tX;
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var vA=bA.m_linearVelocity;
var wA=bA.m_angularVelocity;
var vB=bB.m_linearVelocity;
var wB=bB.m_angularVelocity;
var mA=bA.m_invMass;
var mB=bB.m_invMass;
var iA=bA.m_invI;
var iB=bB.m_invI;
tMat=bA.m_xf.R;
var rAX=this.m_localAnchorA$2.x-bA.m_sweep.localCenter.x;
var rAY=this.m_localAnchorA$2.y-bA.m_sweep.localCenter.y;
tX=(tMat.col1.x*rAX+tMat.col2.x*rAY);
rAY=(tMat.col1.y*rAX+tMat.col2.y*rAY);
rAX=tX;
tMat=bB.m_xf.R;
var rBX=this.m_localAnchorB$2.x-bB.m_sweep.localCenter.x;
var rBY=this.m_localAnchorB$2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*rBX+tMat.col2.x*rBY);
rBY=(tMat.col1.y*rBX+tMat.col2.y*rBY);
rBX=tX;
var maxImpulse;
{
var Cdot=wB-wA;
var impulse=-this.m_angularMass*Cdot;
var oldImpulse=this.m_angularImpulse$2;
maxImpulse=step.dt*this.m_maxTorque$2;
this.m_angularImpulse$2=Box2D.Common.Math.b2Math.Clamp(this.m_angularImpulse$2+impulse,-maxImpulse,maxImpulse);
impulse=this.m_angularImpulse$2-oldImpulse;
wA-=iA*impulse;
wB+=iB*impulse;
}
{
var CdotX=vB.x-wB*rBY-vA.x+wA*rAY;
var CdotY=vB.y+wB*rBX-vA.y-wA*rAX;
var impulseV=Box2D.Common.Math.b2Math.MulMV(this.m_linearMass,new Box2D.Common.Math.b2Vec2(-CdotX,-CdotY));
var oldImpulseV=this.m_linearImpulse$2.Copy();
this.m_linearImpulse$2.Add(impulseV);
maxImpulse=step.dt*this.m_maxForce$2;
if(this.m_linearImpulse$2.LengthSquared()>maxImpulse*maxImpulse)
{
this.m_linearImpulse$2.Normalize();
this.m_linearImpulse$2.Multiply(maxImpulse);
}
impulseV=Box2D.Common.Math.b2Math.SubtractVV(this.m_linearImpulse$2,oldImpulseV);
vA.x-=mA*impulseV.x;
vA.y-=mA*impulseV.y;
wA-=iA*(rAX*impulseV.y-rAY*impulseV.x);
vB.x+=mB*impulseV.x;
vB.y+=mB*impulseV.y;
wB+=iB*(rBX*impulseV.y-rBY*impulseV.x);
}
bA.m_angularVelocity=wA;
bB.m_angularVelocity=wB;
},
"b2internal override function SolvePositionConstraints",function(baumgarte)
{
return true;
},
"private var",{m_localAnchorA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_localAnchorB:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{m_linearMass:function(){return(new Box2D.Common.Math.b2Mat22());}},
"public var",{m_angularMass:NaN},
"private var",{m_linearImpulse:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_angularImpulse:NaN},
"private var",{m_maxForce:NaN},
"private var",{m_maxTorque:NaN},
];},[],["Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Mat22","Box2D.Common.Math.b2Math"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2FrictionJointDef
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2FrictionJointDef extends Box2D.Dynamics.Joints.b2JointDef",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint);},

"public function b2FrictionJointDef",function()
{this.super$2();this.localAnchorA=this.localAnchorA();this.localAnchorB=this.localAnchorB();
this.type=Box2D.Dynamics.Joints.b2Joint.e_frictionJoint;
this.maxForce=0.0;
this.maxTorque=0.0;
},
"public function Initialize",function(bA,bB,
anchor)
{
this.bodyA=bA;
this.bodyB=bB;
this.localAnchorA.SetV(this.bodyA.GetLocalPoint(anchor));
this.localAnchorB.SetV(this.bodyB.GetLocalPoint(anchor));
},
"public var",{localAnchorA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{localAnchorB:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{maxForce:NaN},
"public var",{maxTorque:NaN},
];},[],["Box2D.Dynamics.Joints.b2JointDef","Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2GearJoint
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2GearJoint extends Box2D.Dynamics.Joints.b2Joint",2,function($$private){var as=joo.as;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint,Box2D.Common.b2Settings);},

"public override function GetAnchorA",function(){
return this.m_bodyA.GetWorldPoint(this.m_localAnchor1$2);
},
"public override function GetAnchorB",function(){
return this.m_bodyB.GetWorldPoint(this.m_localAnchor2$2);
},
"public override function GetReactionForce",function(inv_dt){
return new Box2D.Common.Math.b2Vec2(inv_dt*this.m_impulse$2*this.m_J$2.linearB.x,inv_dt*this.m_impulse$2*this.m_J$2.linearB.y);
},
"public override function GetReactionTorque",function(inv_dt){
var tMat=this.m_bodyB.m_xf.R;
var rX=this.m_localAnchor1$2.x-this.m_bodyB.m_sweep.localCenter.x;
var rY=this.m_localAnchor1$2.y-this.m_bodyB.m_sweep.localCenter.y;
var tX=tMat.col1.x*rX+tMat.col2.x*rY;
rY=tMat.col1.y*rX+tMat.col2.y*rY;
rX=tX;
var PX=this.m_impulse$2*this.m_J$2.linearB.x;
var PY=this.m_impulse$2*this.m_J$2.linearB.y;
return inv_dt*(this.m_impulse$2*this.m_J$2.angularB-rX*PY+rY*PX);
},
"public function GetRatio",function(){
return this.m_ratio$2;
},
"public function SetRatio",function(ratio){
this.m_ratio$2=ratio;
},
"public function b2GearJoint",function(def){
this.super$2(def);this.m_groundAnchor1$2=this.m_groundAnchor1$2();this.m_groundAnchor2$2=this.m_groundAnchor2$2();this.m_localAnchor1$2=this.m_localAnchor1$2();this.m_localAnchor2$2=this.m_localAnchor2$2();this.m_J$2=this.m_J$2();
var type1=def.joint1.m_type;
var type2=def.joint2.m_type;
this.m_revolute1$2=null;
this.m_prismatic1$2=null;
this.m_revolute2$2=null;
this.m_prismatic2$2=null;
var coordinate1;
var coordinate2;
this.m_ground1$2=def.joint1.GetBodyA();
this.m_bodyA=def.joint1.GetBodyB();
if(type1==Box2D.Dynamics.Joints.b2Joint.e_revoluteJoint)
{
this.m_revolute1$2=as(def.joint1,Box2D.Dynamics.Joints.b2RevoluteJoint);
this.m_groundAnchor1$2.SetV(this.m_revolute1$2.m_localAnchor1);
this.m_localAnchor1$2.SetV(this.m_revolute1$2.m_localAnchor2);
coordinate1=this.m_revolute1$2.GetJointAngle();
}
else
{
this.m_prismatic1$2=as(def.joint1,Box2D.Dynamics.Joints.b2PrismaticJoint);
this.m_groundAnchor1$2.SetV(this.m_prismatic1$2.m_localAnchor1);
this.m_localAnchor1$2.SetV(this.m_prismatic1$2.m_localAnchor2);
coordinate1=this.m_prismatic1$2.GetJointTranslation();
}
this.m_ground2$2=def.joint2.GetBodyA();
this.m_bodyB=def.joint2.GetBodyB();
if(type2==Box2D.Dynamics.Joints.b2Joint.e_revoluteJoint)
{
this.m_revolute2$2=as(def.joint2,Box2D.Dynamics.Joints.b2RevoluteJoint);
this.m_groundAnchor2$2.SetV(this.m_revolute2$2.m_localAnchor1);
this.m_localAnchor2$2.SetV(this.m_revolute2$2.m_localAnchor2);
coordinate2=this.m_revolute2$2.GetJointAngle();
}
else
{
this.m_prismatic2$2=as(def.joint2,Box2D.Dynamics.Joints.b2PrismaticJoint);
this.m_groundAnchor2$2.SetV(this.m_prismatic2$2.m_localAnchor1);
this.m_localAnchor2$2.SetV(this.m_prismatic2$2.m_localAnchor2);
coordinate2=this.m_prismatic2$2.GetJointTranslation();
}
this.m_ratio$2=def.ratio;
this.m_constant$2=coordinate1+this.m_ratio$2*coordinate2;
this.m_impulse$2=0.0;
},
"b2internal override function InitVelocityConstraints",function(step){
var g1=this.m_ground1$2;
var g2=this.m_ground2$2;
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var ugX;
var ugY;
var rX;
var rY;
var tMat;
var tVec;
var crug;
var tX;
var K=0.0;
this.m_J$2.SetZero();
if(this.m_revolute1$2)
{
this.m_J$2.angularA=-1.0;
K+=bA.m_invI;
}
else
{
tMat=g1.m_xf.R;
tVec=this.m_prismatic1$2.m_localXAxis1;
ugX=tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
ugY=tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
tMat=bA.m_xf.R;
rX=this.m_localAnchor1$2.x-bA.m_sweep.localCenter.x;
rY=this.m_localAnchor1$2.y-bA.m_sweep.localCenter.y;
tX=tMat.col1.x*rX+tMat.col2.x*rY;
rY=tMat.col1.y*rX+tMat.col2.y*rY;
rX=tX;
crug=rX*ugY-rY*ugX;
this.m_J$2.linearA.Set(-ugX,-ugY);
this.m_J$2.angularA=-crug;
K+=bA.m_invMass+bA.m_invI*crug*crug;
}
if(this.m_revolute2$2)
{
this.m_J$2.angularB=-this.m_ratio$2;
K+=this.m_ratio$2*this.m_ratio$2*bB.m_invI;
}
else
{
tMat=g2.m_xf.R;
tVec=this.m_prismatic2$2.m_localXAxis1;
ugX=tMat.col1.x*tVec.x+tMat.col2.x*tVec.y;
ugY=tMat.col1.y*tVec.x+tMat.col2.y*tVec.y;
tMat=bB.m_xf.R;
rX=this.m_localAnchor2$2.x-bB.m_sweep.localCenter.x;
rY=this.m_localAnchor2$2.y-bB.m_sweep.localCenter.y;
tX=tMat.col1.x*rX+tMat.col2.x*rY;
rY=tMat.col1.y*rX+tMat.col2.y*rY;
rX=tX;
crug=rX*ugY-rY*ugX;
this.m_J$2.linearB.Set(-this.m_ratio$2*ugX,-this.m_ratio$2*ugY);
this.m_J$2.angularB=-this.m_ratio$2*crug;
K+=this.m_ratio$2*this.m_ratio$2*(bB.m_invMass+bB.m_invI*crug*crug);
}
this.m_mass$2=K>0.0?1.0/K:0.0;
if(step.warmStarting)
{
bA.m_linearVelocity.x+=bA.m_invMass*this.m_impulse$2*this.m_J$2.linearA.x;
bA.m_linearVelocity.y+=bA.m_invMass*this.m_impulse$2*this.m_J$2.linearA.y;
bA.m_angularVelocity+=bA.m_invI*this.m_impulse$2*this.m_J$2.angularA;
bB.m_linearVelocity.x+=bB.m_invMass*this.m_impulse$2*this.m_J$2.linearB.x;
bB.m_linearVelocity.y+=bB.m_invMass*this.m_impulse$2*this.m_J$2.linearB.y;
bB.m_angularVelocity+=bB.m_invI*this.m_impulse$2*this.m_J$2.angularB;
}
else
{
this.m_impulse$2=0.0;
}
},
"b2internal override function SolveVelocityConstraints",function(step)
{
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var Cdot=this.m_J$2.Compute(bA.m_linearVelocity,bA.m_angularVelocity,
bB.m_linearVelocity,bB.m_angularVelocity);
var impulse=-this.m_mass$2*Cdot;
this.m_impulse$2+=impulse;
bA.m_linearVelocity.x+=bA.m_invMass*impulse*this.m_J$2.linearA.x;
bA.m_linearVelocity.y+=bA.m_invMass*impulse*this.m_J$2.linearA.y;
bA.m_angularVelocity+=bA.m_invI*impulse*this.m_J$2.angularA;
bB.m_linearVelocity.x+=bB.m_invMass*impulse*this.m_J$2.linearB.x;
bB.m_linearVelocity.y+=bB.m_invMass*impulse*this.m_J$2.linearB.y;
bB.m_angularVelocity+=bB.m_invI*impulse*this.m_J$2.angularB;
},
"b2internal override function SolvePositionConstraints",function(baumgarte)
{
var linearError=0.0;
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var coordinate1;
var coordinate2;
if(this.m_revolute1$2)
{
coordinate1=this.m_revolute1$2.GetJointAngle();
}
else
{
coordinate1=this.m_prismatic1$2.GetJointTranslation();
}
if(this.m_revolute2$2)
{
coordinate2=this.m_revolute2$2.GetJointAngle();
}
else
{
coordinate2=this.m_prismatic2$2.GetJointTranslation();
}
var C=this.m_constant$2-(coordinate1+this.m_ratio$2*coordinate2);
var impulse=-this.m_mass$2*C;
bA.m_sweep.c.x+=bA.m_invMass*impulse*this.m_J$2.linearA.x;
bA.m_sweep.c.y+=bA.m_invMass*impulse*this.m_J$2.linearA.y;
bA.m_sweep.a+=bA.m_invI*impulse*this.m_J$2.angularA;
bB.m_sweep.c.x+=bB.m_invMass*impulse*this.m_J$2.linearB.x;
bB.m_sweep.c.y+=bB.m_invMass*impulse*this.m_J$2.linearB.y;
bB.m_sweep.a+=bB.m_invI*impulse*this.m_J$2.angularB;
bA.SynchronizeTransform();
bB.SynchronizeTransform();
return linearError<Box2D.Common.b2Settings.b2_linearSlop;
},
"private var",{m_ground1:null},
"private var",{m_ground2:null},
"private var",{m_revolute1:null},
"private var",{m_prismatic1:null},
"private var",{m_revolute2:null},
"private var",{m_prismatic2:null},
"private var",{m_groundAnchor1:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_groundAnchor2:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_localAnchor1:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_localAnchor2:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_J:function(){return(new Box2D.Dynamics.Joints.b2Jacobian());}},
"private var",{m_constant:NaN},
"private var",{m_ratio:NaN},
"private var",{m_mass:NaN},
"private var",{m_impulse:NaN},
];},[],["Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2","Box2D.Dynamics.Joints.b2RevoluteJoint","Box2D.Dynamics.Joints.b2PrismaticJoint","Box2D.Common.b2Settings","Box2D.Dynamics.Joints.b2Jacobian"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2GearJointDef
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2GearJointDef extends Box2D.Dynamics.Joints.b2JointDef",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint);},

"public function b2GearJointDef",function()
{this.super$2();
this.type=Box2D.Dynamics.Joints.b2Joint.e_gearJoint;
this.joint1=null;
this.joint2=null;
this.ratio=1.0;
},
"public var",{joint1:null},
"public var",{joint2:null},
"public var",{ratio:NaN},
];},[],["Box2D.Dynamics.Joints.b2JointDef","Box2D.Dynamics.Joints.b2Joint"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2Jacobian
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2Jacobian",1,function($$private){;return[

"public var",{linearA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{angularA:NaN},
"public var",{linearB:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{angularB:NaN},
"public function SetZero",function(){
this.linearA.SetZero();this.angularA=0.0;
this.linearB.SetZero();this.angularB=0.0;
},
"public function Set",function(x1,a1,x2,a2){
this.linearA.SetV(x1);this.angularA=a1;
this.linearB.SetV(x2);this.angularB=a2;
},
"public function Compute",function(x1,a1,x2,a2){
return(this.linearA.x*x1.x+this.linearA.y*x1.y)+this.angularA*a1+(this.linearB.x*x2.x+this.linearB.y*x2.y)+this.angularB*a2;
},
"public function b2Jacobian",function b2Jacobian$(){this.linearA=this.linearA();this.linearB=this.linearB();}];},[],["Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2Joint
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2Joint",1,function($$private){var as=joo.as,assert=joo.assert;return[

"public function GetType",function(){
return this.m_type;
},
"public virtual function GetAnchorA",function(){return null;},
"public virtual function GetAnchorB",function(){return null;},
"public virtual function GetReactionForce",function(inv_dt){return null;},
"public virtual function GetReactionTorque",function(inv_dt){return 0.0;},
"public function GetBodyA",function()
{
return this.m_bodyA;
},
"public function GetBodyB",function()
{
return this.m_bodyB;
},
"public function GetNext",function(){
return this.m_next;
},
"public function GetUserData",function(){
return this.m_userData$1;
},
"public function SetUserData",function(data){
this.m_userData$1=data;
},
"public function IsActive",function(){
return this.m_bodyA.IsActive()&&this.m_bodyB.IsActive();
},
"static b2internal function Create",function(def,allocator){
var joint=null;
switch(def.type)
{
case Box2D.Dynamics.Joints.b2Joint.e_distanceJoint:
{
joint=new Box2D.Dynamics.Joints.b2DistanceJoint(as(def,Box2D.Dynamics.Joints.b2DistanceJointDef));
}
break;
case Box2D.Dynamics.Joints.b2Joint.e_mouseJoint:
{
joint=new Box2D.Dynamics.Joints.b2MouseJoint(as(def,Box2D.Dynamics.Joints.b2MouseJointDef));
}
break;
case Box2D.Dynamics.Joints.b2Joint.e_prismaticJoint:
{
joint=new Box2D.Dynamics.Joints.b2PrismaticJoint(as(def,Box2D.Dynamics.Joints.b2PrismaticJointDef));
}
break;
case Box2D.Dynamics.Joints.b2Joint.e_revoluteJoint:
{
joint=new Box2D.Dynamics.Joints.b2RevoluteJoint(as(def,Box2D.Dynamics.Joints.b2RevoluteJointDef));
}
break;
case Box2D.Dynamics.Joints.b2Joint.e_pulleyJoint:
{
joint=new Box2D.Dynamics.Joints.b2PulleyJoint(as(def,Box2D.Dynamics.Joints.b2PulleyJointDef));
}
break;
case Box2D.Dynamics.Joints.b2Joint.e_gearJoint:
{
joint=new Box2D.Dynamics.Joints.b2GearJoint(as(def,Box2D.Dynamics.Joints.b2GearJointDef));
}
break;
case Box2D.Dynamics.Joints.b2Joint.e_lineJoint:
{
joint=new Box2D.Dynamics.Joints.b2LineJoint(as(def,Box2D.Dynamics.Joints.b2LineJointDef));
}
break;
case Box2D.Dynamics.Joints.b2Joint.e_weldJoint:
{
joint=new Box2D.Dynamics.Joints.b2WeldJoint(as(def,Box2D.Dynamics.Joints.b2WeldJointDef));
}
break;
case Box2D.Dynamics.Joints.b2Joint.e_frictionJoint:
{
joint=new Box2D.Dynamics.Joints.b2FrictionJoint(as(def,Box2D.Dynamics.Joints.b2FrictionJointDef));
}
break;
default:
break;
}
return joint;
},
"static b2internal function Destroy",function(joint,allocator){
},
"public function b2Joint",function(def){this.m_edgeA=this.m_edgeA();this.m_edgeB=this.m_edgeB();this.m_localCenterA=this.m_localCenterA();this.m_localCenterB=this.m_localCenterB();
this.m_type=def.type;
this.m_prev=null;
this.m_next=null;
this.m_bodyA=def.bodyA;
this.m_bodyB=def.bodyB;
this.m_collideConnected=def.collideConnected;
this.m_islandFlag=false;
this.m_userData$1=def.userData;
},
"b2internal virtual function InitVelocityConstraints",function(step){},
"b2internal virtual function SolveVelocityConstraints",function(step){},
"b2internal virtual function FinalizeVelocityConstraints",function(){},
"b2internal virtual function SolvePositionConstraints",function(baumgarte){return false;},
"b2internal var",{m_type:0},
"b2internal var",{m_prev:null},
"b2internal var",{m_next:null},
"b2internal var",{m_edgeA:function(){return(new Box2D.Dynamics.Joints.b2JointEdge());}},
"b2internal var",{m_edgeB:function(){return(new Box2D.Dynamics.Joints.b2JointEdge());}},
"b2internal var",{m_bodyA:null},
"b2internal var",{m_bodyB:null},
"b2internal var",{m_islandFlag:false},
"b2internal var",{m_collideConnected:false},
"private var",{m_userData:undefined},
"b2internal var",{m_localCenterA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"b2internal var",{m_localCenterB:function(){return(new Box2D.Common.Math.b2Vec2());}},
"b2internal var",{m_invMassA:NaN},
"b2internal var",{m_invMassB:NaN},
"b2internal var",{m_invIA:NaN},
"b2internal var",{m_invIB:NaN},
"static b2internal const",{e_unknownJoint:0},
"static b2internal const",{e_revoluteJoint:1},
"static b2internal const",{e_prismaticJoint:2},
"static b2internal const",{e_distanceJoint:3},
"static b2internal const",{e_pulleyJoint:4},
"static b2internal const",{e_mouseJoint:5},
"static b2internal const",{e_gearJoint:6},
"static b2internal const",{e_lineJoint:7},
"static b2internal const",{e_weldJoint:8},
"static b2internal const",{e_frictionJoint:9},
"static b2internal const",{e_inactiveLimit:0},
"static b2internal const",{e_atLowerLimit:1},
"static b2internal const",{e_atUpperLimit:2},
"static b2internal const",{e_equalLimits:3},
];},["Create","Destroy"],["Box2D.Dynamics.Joints.b2DistanceJoint","Box2D.Dynamics.Joints.b2DistanceJointDef","Box2D.Dynamics.Joints.b2MouseJoint","Box2D.Dynamics.Joints.b2MouseJointDef","Box2D.Dynamics.Joints.b2PrismaticJoint","Box2D.Dynamics.Joints.b2PrismaticJointDef","Box2D.Dynamics.Joints.b2RevoluteJoint","Box2D.Dynamics.Joints.b2RevoluteJointDef","Box2D.Dynamics.Joints.b2PulleyJoint","Box2D.Dynamics.Joints.b2PulleyJointDef","Box2D.Dynamics.Joints.b2GearJoint","Box2D.Dynamics.Joints.b2GearJointDef","Box2D.Dynamics.Joints.b2LineJoint","Box2D.Dynamics.Joints.b2LineJointDef","Box2D.Dynamics.Joints.b2WeldJoint","Box2D.Dynamics.Joints.b2WeldJointDef","Box2D.Dynamics.Joints.b2FrictionJoint","Box2D.Dynamics.Joints.b2FrictionJointDef","Box2D.Dynamics.Joints.b2JointEdge","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2JointDef
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2JointDef",1,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint);},

"public function b2JointDef",function()
{
this.type=Box2D.Dynamics.Joints.b2Joint.e_unknownJoint;
this.userData=null;
this.bodyA=null;
this.bodyB=null;
this.collideConnected=false;
},
"public var",{type:0},
"public var",{userData:undefined},
"public var",{bodyA:null},
"public var",{bodyB:null},
"public var",{collideConnected:false},
];},[],["Box2D.Dynamics.Joints.b2Joint"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2JointEdge
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2JointEdge",1,function($$private){;return[

"public var",{other:null},
"public var",{joint:null},
"public var",{prev:null},
"public var",{next:null},
];},[],[], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2LineJoint
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2LineJoint extends Box2D.Dynamics.Joints.b2Joint",2,function($$private){;return[function(){joo.classLoader.init(Number,Box2D.Common.b2Settings);},

"public override function GetAnchorA",function(){
return this.m_bodyA.GetWorldPoint(this.m_localAnchor1);
},
"public override function GetAnchorB",function(){
return this.m_bodyB.GetWorldPoint(this.m_localAnchor2);
},
"public override function GetReactionForce",function(inv_dt)
{
return new Box2D.Common.Math.b2Vec2(inv_dt*(this.m_impulse$2.x*this.m_perp$2.x+(this.m_motorImpulse$2+this.m_impulse$2.y)*this.m_axis$2.x),
inv_dt*(this.m_impulse$2.x*this.m_perp$2.y+(this.m_motorImpulse$2+this.m_impulse$2.y)*this.m_axis$2.y));
},
"public override function GetReactionTorque",function(inv_dt)
{
return inv_dt*this.m_impulse$2.y;
},
"public function GetJointTranslation",function(){
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var tMat;
var p1=bA.GetWorldPoint(this.m_localAnchor1);
var p2=bB.GetWorldPoint(this.m_localAnchor2);
var dX=p2.x-p1.x;
var dY=p2.y-p1.y;
var axis=bA.GetWorldVector(this.m_localXAxis1);
var translation=axis.x*dX+axis.y*dY;
return translation;
},
"public function GetJointSpeed",function(){
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var tMat;
tMat=bA.m_xf.R;
var r1X=this.m_localAnchor1.x-bA.m_sweep.localCenter.x;
var r1Y=this.m_localAnchor1.y-bA.m_sweep.localCenter.y;
var tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=bB.m_xf.R;
var r2X=this.m_localAnchor2.x-bB.m_sweep.localCenter.x;
var r2Y=this.m_localAnchor2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
var p1X=bA.m_sweep.c.x+r1X;
var p1Y=bA.m_sweep.c.y+r1Y;
var p2X=bB.m_sweep.c.x+r2X;
var p2Y=bB.m_sweep.c.y+r2Y;
var dX=p2X-p1X;
var dY=p2Y-p1Y;
var axis=bA.GetWorldVector(this.m_localXAxis1);
var v1=bA.m_linearVelocity;
var v2=bB.m_linearVelocity;
var w1=bA.m_angularVelocity;
var w2=bB.m_angularVelocity;
var speed=(dX*(-w1*axis.y)+dY*(w1*axis.x))+(axis.x*(((v2.x+(-w2*r2Y))-v1.x)-(-w1*r1Y))+axis.y*(((v2.y+(w2*r2X))-v1.y)-(w1*r1X)));
return speed;
},
"public function IsLimitEnabled",function()
{
return this.m_enableLimit$2;
},
"public function EnableLimit",function(flag)
{
this.m_bodyA.SetAwake(true);
this.m_bodyB.SetAwake(true);
this.m_enableLimit$2=flag;
},
"public function GetLowerLimit",function()
{
return this.m_lowerTranslation$2;
},
"public function GetUpperLimit",function()
{
return this.m_upperTranslation$2;
},
"public function SetLimits",function(lower,upper)
{
this.m_bodyA.SetAwake(true);
this.m_bodyB.SetAwake(true);
this.m_lowerTranslation$2=lower;
this.m_upperTranslation$2=upper;
},
"public function IsMotorEnabled",function()
{
return this.m_enableMotor$2;
},
"public function EnableMotor",function(flag)
{
this.m_bodyA.SetAwake(true);
this.m_bodyB.SetAwake(true);
this.m_enableMotor$2=flag;
},
"public function SetMotorSpeed",function(speed)
{
this.m_bodyA.SetAwake(true);
this.m_bodyB.SetAwake(true);
this.m_motorSpeed$2=speed;
},
"public function GetMotorSpeed",function()
{
return this.m_motorSpeed$2;
},
"public function SetMaxMotorForce",function(force)
{
this.m_bodyA.SetAwake(true);
this.m_bodyB.SetAwake(true);
this.m_maxMotorForce$2=force;
},
"public function GetMaxMotorForce",function()
{
return this.m_maxMotorForce$2;
},
"public function GetMotorForce",function()
{
return this.m_motorImpulse$2;
},
"public function b2LineJoint",function(def){
this.super$2(def);this.m_localAnchor1=this.m_localAnchor1();this.m_localAnchor2=this.m_localAnchor2();this.m_localXAxis1=this.m_localXAxis1();this.m_localYAxis1$2=this.m_localYAxis1$2();this.m_axis$2=this.m_axis$2();this.m_perp$2=this.m_perp$2();this.m_K$2=this.m_K$2();this.m_impulse$2=this.m_impulse$2();
var tMat;
var tX;
var tY;
this.m_localAnchor1.SetV(def.localAnchorA);
this.m_localAnchor2.SetV(def.localAnchorB);
this.m_localXAxis1.SetV(def.localAxisA);
this.m_localYAxis1$2.x=-this.m_localXAxis1.y;
this.m_localYAxis1$2.y=this.m_localXAxis1.x;
this.m_impulse$2.SetZero();
this.m_motorMass$2=0.0;
this.m_motorImpulse$2=0.0;
this.m_lowerTranslation$2=def.lowerTranslation;
this.m_upperTranslation$2=def.upperTranslation;
this.m_maxMotorForce$2=def.maxMotorForce;
this.m_motorSpeed$2=def.motorSpeed;
this.m_enableLimit$2=def.enableLimit;
this.m_enableMotor$2=def.enableMotor;
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
this.m_axis$2.SetZero();
this.m_perp$2.SetZero();
},
"b2internal override function InitVelocityConstraints",function(step){
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var tMat;
var tX;
this.m_localCenterA.SetV(bA.GetLocalCenter());
this.m_localCenterB.SetV(bB.GetLocalCenter());
var xf1=bA.GetTransform();
var xf2=bB.GetTransform();
tMat=bA.m_xf.R;
var r1X=this.m_localAnchor1.x-this.m_localCenterA.x;
var r1Y=this.m_localAnchor1.y-this.m_localCenterA.y;
tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=bB.m_xf.R;
var r2X=this.m_localAnchor2.x-this.m_localCenterB.x;
var r2Y=this.m_localAnchor2.y-this.m_localCenterB.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
var dX=bB.m_sweep.c.x+r2X-bA.m_sweep.c.x-r1X;
var dY=bB.m_sweep.c.y+r2Y-bA.m_sweep.c.y-r1Y;
this.m_invMassA=bA.m_invMass;
this.m_invMassB=bB.m_invMass;
this.m_invIA=bA.m_invI;
this.m_invIB=bB.m_invI;
{
this.m_axis$2.SetV(Box2D.Common.Math.b2Math.MulMV(xf1.R,this.m_localXAxis1));
this.m_a1$2=(dX+r1X)*this.m_axis$2.y-(dY+r1Y)*this.m_axis$2.x;
this.m_a2$2=r2X*this.m_axis$2.y-r2Y*this.m_axis$2.x;
this.m_motorMass$2=this.m_invMassA+this.m_invMassB+this.m_invIA*this.m_a1$2*this.m_a1$2+this.m_invIB*this.m_a2$2*this.m_a2$2;
this.m_motorMass$2=this.m_motorMass$2>Number.MIN_VALUE?1.0/this.m_motorMass$2:0.0;
}
{
this.m_perp$2.SetV(Box2D.Common.Math.b2Math.MulMV(xf1.R,this.m_localYAxis1$2));
this.m_s1$2=(dX+r1X)*this.m_perp$2.y-(dY+r1Y)*this.m_perp$2.x;
this.m_s2$2=r2X*this.m_perp$2.y-r2Y*this.m_perp$2.x;
var m1=this.m_invMassA;
var m2=this.m_invMassB;
var i1=this.m_invIA;
var i2=this.m_invIB;
this.m_K$2.col1.x=m1+m2+i1*this.m_s1$2*this.m_s1$2+i2*this.m_s2$2*this.m_s2$2;
this.m_K$2.col1.y=i1*this.m_s1$2*this.m_a1$2+i2*this.m_s2$2*this.m_a2$2;
this.m_K$2.col2.x=this.m_K$2.col1.y;
this.m_K$2.col2.y=m1+m2+i1*this.m_a1$2*this.m_a1$2+i2*this.m_a2$2*this.m_a2$2;
}
if(this.m_enableLimit$2)
{
var jointTransition=this.m_axis$2.x*dX+this.m_axis$2.y*dY;
if(Box2D.Common.Math.b2Math.Abs(this.m_upperTranslation$2-this.m_lowerTranslation$2)<2.0*Box2D.Common.b2Settings.b2_linearSlop)
{
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_equalLimits;
}
else if(jointTransition<=this.m_lowerTranslation$2)
{
if(this.m_limitState$2!=Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit)
{
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit;
this.m_impulse$2.y=0.0;
}
}
else if(jointTransition>=this.m_upperTranslation$2)
{
if(this.m_limitState$2!=Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
{
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit;
this.m_impulse$2.y=0.0;
}
}
else
{
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
this.m_impulse$2.y=0.0;
}
}
else
{
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
}
if(this.m_enableMotor$2==false)
{
this.m_motorImpulse$2=0.0;
}
if(step.warmStarting)
{
this.m_impulse$2.x*=step.dtRatio;
this.m_impulse$2.y*=step.dtRatio;
this.m_motorImpulse$2*=step.dtRatio;
var PX=this.m_impulse$2.x*this.m_perp$2.x+(this.m_motorImpulse$2+this.m_impulse$2.y)*this.m_axis$2.x;
var PY=this.m_impulse$2.x*this.m_perp$2.y+(this.m_motorImpulse$2+this.m_impulse$2.y)*this.m_axis$2.y;
var L1=this.m_impulse$2.x*this.m_s1$2+(this.m_motorImpulse$2+this.m_impulse$2.y)*this.m_a1$2;
var L2=this.m_impulse$2.x*this.m_s2$2+(this.m_motorImpulse$2+this.m_impulse$2.y)*this.m_a2$2;
bA.m_linearVelocity.x-=this.m_invMassA*PX;
bA.m_linearVelocity.y-=this.m_invMassA*PY;
bA.m_angularVelocity-=this.m_invIA*L1;
bB.m_linearVelocity.x+=this.m_invMassB*PX;
bB.m_linearVelocity.y+=this.m_invMassB*PY;
bB.m_angularVelocity+=this.m_invIB*L2;
}
else
{
this.m_impulse$2.SetZero();
this.m_motorImpulse$2=0.0;
}
},
"b2internal override function SolveVelocityConstraints",function(step){
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var v1=bA.m_linearVelocity;
var w1=bA.m_angularVelocity;
var v2=bB.m_linearVelocity;
var w2=bB.m_angularVelocity;
var PX;
var PY;
var L1;
var L2;
if(this.m_enableMotor$2&&this.m_limitState$2!=Box2D.Dynamics.Joints.b2Joint.e_equalLimits)
{
var Cdot=this.m_axis$2.x*(v2.x-v1.x)+this.m_axis$2.y*(v2.y-v1.y)+this.m_a2$2*w2-this.m_a1$2*w1;
var impulse=this.m_motorMass$2*(this.m_motorSpeed$2-Cdot);
var oldImpulse=this.m_motorImpulse$2;
var maxImpulse=step.dt*this.m_maxMotorForce$2;
this.m_motorImpulse$2=Box2D.Common.Math.b2Math.Clamp(this.m_motorImpulse$2+impulse,-maxImpulse,maxImpulse);
impulse=this.m_motorImpulse$2-oldImpulse;
PX=impulse*this.m_axis$2.x;
PY=impulse*this.m_axis$2.y;
L1=impulse*this.m_a1$2;
L2=impulse*this.m_a2$2;
v1.x-=this.m_invMassA*PX;
v1.y-=this.m_invMassA*PY;
w1-=this.m_invIA*L1;
v2.x+=this.m_invMassB*PX;
v2.y+=this.m_invMassB*PY;
w2+=this.m_invIB*L2;
}
var Cdot1=this.m_perp$2.x*(v2.x-v1.x)+this.m_perp$2.y*(v2.y-v1.y)+this.m_s2$2*w2-this.m_s1$2*w1;
if(this.m_enableLimit$2&&this.m_limitState$2!=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit)
{
var Cdot2=this.m_axis$2.x*(v2.x-v1.x)+this.m_axis$2.y*(v2.y-v1.y)+this.m_a2$2*w2-this.m_a1$2*w1;
var f1=this.m_impulse$2.Copy();
var df=this.m_K$2.Solve(new Box2D.Common.Math.b2Vec2(),-Cdot1,-Cdot2);
this.m_impulse$2.Add(df);
if(this.m_limitState$2==Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit)
{
this.m_impulse$2.y=Box2D.Common.Math.b2Math.Max(this.m_impulse$2.y,0.0);
}
else if(this.m_limitState$2==Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
{
this.m_impulse$2.y=Box2D.Common.Math.b2Math.Min(this.m_impulse$2.y,0.0);
}
var b=-Cdot1-(this.m_impulse$2.y-f1.y)*this.m_K$2.col2.x;
var f2r;
if(this.m_K$2.col1.x!=0.0)
{
f2r=b/this.m_K$2.col1.x+f1.x;
}else{
f2r=f1.x;
}
this.m_impulse$2.x=f2r;
df.x=this.m_impulse$2.x-f1.x;
df.y=this.m_impulse$2.y-f1.y;
PX=df.x*this.m_perp$2.x+df.y*this.m_axis$2.x;
PY=df.x*this.m_perp$2.y+df.y*this.m_axis$2.y;
L1=df.x*this.m_s1$2+df.y*this.m_a1$2;
L2=df.x*this.m_s2$2+df.y*this.m_a2$2;
v1.x-=this.m_invMassA*PX;
v1.y-=this.m_invMassA*PY;
w1-=this.m_invIA*L1;
v2.x+=this.m_invMassB*PX;
v2.y+=this.m_invMassB*PY;
w2+=this.m_invIB*L2;
}
else
{
var df2;
if(this.m_K$2.col1.x!=0.0)
{
df2=(-Cdot1)/this.m_K$2.col1.x;
}else{
df2=0.0;
}
this.m_impulse$2.x+=df2;
PX=df2*this.m_perp$2.x;
PY=df2*this.m_perp$2.y;
L1=df2*this.m_s1$2;
L2=df2*this.m_s2$2;
v1.x-=this.m_invMassA*PX;
v1.y-=this.m_invMassA*PY;
w1-=this.m_invIA*L1;
v2.x+=this.m_invMassB*PX;
v2.y+=this.m_invMassB*PY;
w2+=this.m_invIB*L2;
}
bA.m_linearVelocity.SetV(v1);
bA.m_angularVelocity=w1;
bB.m_linearVelocity.SetV(v2);
bB.m_angularVelocity=w2;
},
"b2internal override function SolvePositionConstraints",function(baumgarte)
{
var limitC;
var oldLimitImpulse;
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var c1=bA.m_sweep.c;
var a1=bA.m_sweep.a;
var c2=bB.m_sweep.c;
var a2=bB.m_sweep.a;
var tMat;
var tX;
var m1;
var m2;
var i1;
var i2;
var linearError=0.0;
var angularError=0.0;
var active=false;
var C2=0.0;
var R1=Box2D.Common.Math.b2Mat22.FromAngle(a1);
var R2=Box2D.Common.Math.b2Mat22.FromAngle(a2);
tMat=R1;
var r1X=this.m_localAnchor1.x-this.m_localCenterA.x;
var r1Y=this.m_localAnchor1.y-this.m_localCenterA.y;
tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=R2;
var r2X=this.m_localAnchor2.x-this.m_localCenterB.x;
var r2Y=this.m_localAnchor2.y-this.m_localCenterB.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
var dX=c2.x+r2X-c1.x-r1X;
var dY=c2.y+r2Y-c1.y-r1Y;
if(this.m_enableLimit$2)
{
this.m_axis$2=Box2D.Common.Math.b2Math.MulMV(R1,this.m_localXAxis1);
this.m_a1$2=(dX+r1X)*this.m_axis$2.y-(dY+r1Y)*this.m_axis$2.x;
this.m_a2$2=r2X*this.m_axis$2.y-r2Y*this.m_axis$2.x;
var translation=this.m_axis$2.x*dX+this.m_axis$2.y*dY;
if(Box2D.Common.Math.b2Math.Abs(this.m_upperTranslation$2-this.m_lowerTranslation$2)<2.0*Box2D.Common.b2Settings.b2_linearSlop)
{
C2=Box2D.Common.Math.b2Math.Clamp(translation,-Box2D.Common.b2Settings.b2_maxLinearCorrection,Box2D.Common.b2Settings.b2_maxLinearCorrection);
linearError=Box2D.Common.Math.b2Math.Abs(translation);
active=true;
}
else if(translation<=this.m_lowerTranslation$2)
{
C2=Box2D.Common.Math.b2Math.Clamp(translation-this.m_lowerTranslation$2+Box2D.Common.b2Settings.b2_linearSlop,-Box2D.Common.b2Settings.b2_maxLinearCorrection,0.0);
linearError=this.m_lowerTranslation$2-translation;
active=true;
}
else if(translation>=this.m_upperTranslation$2)
{
C2=Box2D.Common.Math.b2Math.Clamp(translation-this.m_upperTranslation$2+Box2D.Common.b2Settings.b2_linearSlop,0.0,Box2D.Common.b2Settings.b2_maxLinearCorrection);
linearError=translation-this.m_upperTranslation$2;
active=true;
}
}
this.m_perp$2=Box2D.Common.Math.b2Math.MulMV(R1,this.m_localYAxis1$2);
this.m_s1$2=(dX+r1X)*this.m_perp$2.y-(dY+r1Y)*this.m_perp$2.x;
this.m_s2$2=r2X*this.m_perp$2.y-r2Y*this.m_perp$2.x;
var impulse=new Box2D.Common.Math.b2Vec2();
var C1=this.m_perp$2.x*dX+this.m_perp$2.y*dY;
linearError=Box2D.Common.Math.b2Math.Max(linearError,Box2D.Common.Math.b2Math.Abs(C1));
angularError=0.0;
if(active)
{
m1=this.m_invMassA;
m2=this.m_invMassB;
i1=this.m_invIA;
i2=this.m_invIB;
this.m_K$2.col1.x=m1+m2+i1*this.m_s1$2*this.m_s1$2+i2*this.m_s2$2*this.m_s2$2;
this.m_K$2.col1.y=i1*this.m_s1$2*this.m_a1$2+i2*this.m_s2$2*this.m_a2$2;
this.m_K$2.col2.x=this.m_K$2.col1.y;
this.m_K$2.col2.y=m1+m2+i1*this.m_a1$2*this.m_a1$2+i2*this.m_a2$2*this.m_a2$2;
this.m_K$2.Solve(impulse,-C1,-C2);
}
else
{
m1=this.m_invMassA;
m2=this.m_invMassB;
i1=this.m_invIA;
i2=this.m_invIB;
var k11=m1+m2+i1*this.m_s1$2*this.m_s1$2+i2*this.m_s2$2*this.m_s2$2;
var impulse1;
if(k11!=0.0)
{
impulse1=(-C1)/k11;
}else{
impulse1=0.0;
}
impulse.x=impulse1;
impulse.y=0.0;
}
var PX=impulse.x*this.m_perp$2.x+impulse.y*this.m_axis$2.x;
var PY=impulse.x*this.m_perp$2.y+impulse.y*this.m_axis$2.y;
var L1=impulse.x*this.m_s1$2+impulse.y*this.m_a1$2;
var L2=impulse.x*this.m_s2$2+impulse.y*this.m_a2$2;
c1.x-=this.m_invMassA*PX;
c1.y-=this.m_invMassA*PY;
a1-=this.m_invIA*L1;
c2.x+=this.m_invMassB*PX;
c2.y+=this.m_invMassB*PY;
a2+=this.m_invIB*L2;
bA.m_sweep.a=a1;
bB.m_sweep.a=a2;
bA.SynchronizeTransform();
bB.SynchronizeTransform();
return linearError<=Box2D.Common.b2Settings.b2_linearSlop&&angularError<=Box2D.Common.b2Settings.b2_angularSlop;
},
"b2internal var",{m_localAnchor1:function(){return(new Box2D.Common.Math.b2Vec2());}},
"b2internal var",{m_localAnchor2:function(){return(new Box2D.Common.Math.b2Vec2());}},
"b2internal var",{m_localXAxis1:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_localYAxis1:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_axis:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_perp:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_s1:NaN},
"private var",{m_s2:NaN},
"private var",{m_a1:NaN},
"private var",{m_a2:NaN},
"private var",{m_K:function(){return(new Box2D.Common.Math.b2Mat22());}},
"private var",{m_impulse:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_motorMass:NaN},
"private var",{m_motorImpulse:NaN},
"private var",{m_lowerTranslation:NaN},
"private var",{m_upperTranslation:NaN},
"private var",{m_maxMotorForce:NaN},
"private var",{m_motorSpeed:NaN},
"private var",{m_enableLimit:false},
"private var",{m_enableMotor:false},
"private var",{m_limitState:0},
];},[],["Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Math","Number","Box2D.Common.b2Settings","Box2D.Common.Math.b2Mat22"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2LineJointDef
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2LineJointDef extends Box2D.Dynamics.Joints.b2JointDef",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint);},

"public function b2LineJointDef",function()
{this.super$2();this.localAnchorA=this.localAnchorA();this.localAnchorB=this.localAnchorB();this.localAxisA=this.localAxisA();
this.type=Box2D.Dynamics.Joints.b2Joint.e_lineJoint;
this.localAxisA.Set(1.0,0.0);
this.enableLimit=false;
this.lowerTranslation=0.0;
this.upperTranslation=0.0;
this.enableMotor=false;
this.maxMotorForce=0.0;
this.motorSpeed=0.0;
},
"public function Initialize",function(bA,bB,anchor,axis)
{
this.bodyA=bA;
this.bodyB=bB;
this.localAnchorA=this.bodyA.GetLocalPoint(anchor);
this.localAnchorB=this.bodyB.GetLocalPoint(anchor);
this.localAxisA=this.bodyA.GetLocalVector(axis);
},
"public var",{localAnchorA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{localAnchorB:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{localAxisA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{enableLimit:false},
"public var",{lowerTranslation:NaN},
"public var",{upperTranslation:NaN},
"public var",{enableMotor:false},
"public var",{maxMotorForce:NaN},
"public var",{motorSpeed:NaN},
];},[],["Box2D.Dynamics.Joints.b2JointDef","Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2MouseJoint
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2MouseJoint extends Box2D.Dynamics.Joints.b2Joint",2,function($$private){;return[function(){joo.classLoader.init(Math);},

"public override function GetAnchorA",function(){
return this.m_target$2;
},
"public override function GetAnchorB",function(){
return this.m_bodyB.GetWorldPoint(this.m_localAnchor$2);
},
"public override function GetReactionForce",function(inv_dt)
{
return new Box2D.Common.Math.b2Vec2(inv_dt*this.m_impulse$2.x,inv_dt*this.m_impulse$2.y);
},
"public override function GetReactionTorque",function(inv_dt)
{
return 0.0;
},
"public function GetTarget",function()
{
return this.m_target$2;
},
"public function SetTarget",function(target){
if(this.m_bodyB.IsAwake()==false){
this.m_bodyB.SetAwake(true);
}
this.m_target$2=target;
},
"public function GetMaxForce",function()
{
return this.m_maxForce$2;
},
"public function SetMaxForce",function(maxForce)
{
this.m_maxForce$2=maxForce;
},
"public function GetFrequency",function()
{
return this.m_frequencyHz$2;
},
"public function SetFrequency",function(hz)
{
this.m_frequencyHz$2=hz;
},
"public function GetDampingRatio",function()
{
return this.m_dampingRatio$2;
},
"public function SetDampingRatio",function(ratio)
{
this.m_dampingRatio$2=ratio;
},
"public function b2MouseJoint",function(def){
this.super$2(def);this.K$2=this.K$2();this.K1$2=this.K1$2();this.K2$2=this.K2$2();this.m_localAnchor$2=this.m_localAnchor$2();this.m_target$2=this.m_target$2();this.m_impulse$2=this.m_impulse$2();this.m_mass$2=this.m_mass$2();this.m_C$2=this.m_C$2();
this.m_target$2.SetV(def.target);
var tX=this.m_target$2.x-this.m_bodyB.m_xf.position.x;
var tY=this.m_target$2.y-this.m_bodyB.m_xf.position.y;
var tMat=this.m_bodyB.m_xf.R;
this.m_localAnchor$2.x=(tX*tMat.col1.x+tY*tMat.col1.y);
this.m_localAnchor$2.y=(tX*tMat.col2.x+tY*tMat.col2.y);
this.m_maxForce$2=def.maxForce;
this.m_impulse$2.SetZero();
this.m_frequencyHz$2=def.frequencyHz;
this.m_dampingRatio$2=def.dampingRatio;
this.m_beta$2=0.0;
this.m_gamma$2=0.0;
},
"private var",{K:function(){return(new Box2D.Common.Math.b2Mat22());}},
"private var",{K1:function(){return(new Box2D.Common.Math.b2Mat22());}},
"private var",{K2:function(){return(new Box2D.Common.Math.b2Mat22());}},
"b2internal override function InitVelocityConstraints",function(step){
var b=this.m_bodyB;
var mass=b.GetMass();
var omega=2.0*Math.PI*this.m_frequencyHz$2;
var d=2.0*mass*this.m_dampingRatio$2*omega;
var k=mass*omega*omega;
this.m_gamma$2=step.dt*(d+step.dt*k);
this.m_gamma$2=this.m_gamma$2!=0?1/this.m_gamma$2:0.0;
this.m_beta$2=step.dt*k*this.m_gamma$2;
var tMat;
tMat=b.m_xf.R;
var rX=this.m_localAnchor$2.x-b.m_sweep.localCenter.x;
var rY=this.m_localAnchor$2.y-b.m_sweep.localCenter.y;
var tX=(tMat.col1.x*rX+tMat.col2.x*rY);
rY=(tMat.col1.y*rX+tMat.col2.y*rY);
rX=tX;
var invMass=b.m_invMass;
var invI=b.m_invI;
this.K1$2.col1.x=invMass;this.K1$2.col2.x=0.0;
this.K1$2.col1.y=0.0;this.K1$2.col2.y=invMass;
this.K2$2.col1.x=invI*rY*rY;this.K2$2.col2.x=-invI*rX*rY;
this.K2$2.col1.y=-invI*rX*rY;this.K2$2.col2.y=invI*rX*rX;
this.K$2.SetM(this.K1$2);
this.K$2.AddM(this.K2$2);
this.K$2.col1.x+=this.m_gamma$2;
this.K$2.col2.y+=this.m_gamma$2;
this.K$2.GetInverse(this.m_mass$2);
this.m_C$2.x=b.m_sweep.c.x+rX-this.m_target$2.x;
this.m_C$2.y=b.m_sweep.c.y+rY-this.m_target$2.y;
b.m_angularVelocity*=0.98;
this.m_impulse$2.x*=step.dtRatio;
this.m_impulse$2.y*=step.dtRatio;
b.m_linearVelocity.x+=invMass*this.m_impulse$2.x;
b.m_linearVelocity.y+=invMass*this.m_impulse$2.y;
b.m_angularVelocity+=invI*(rX*this.m_impulse$2.y-rY*this.m_impulse$2.x);
},
"b2internal override function SolveVelocityConstraints",function(step){
var b=this.m_bodyB;
var tMat;
var tX;
var tY;
tMat=b.m_xf.R;
var rX=this.m_localAnchor$2.x-b.m_sweep.localCenter.x;
var rY=this.m_localAnchor$2.y-b.m_sweep.localCenter.y;
tX=(tMat.col1.x*rX+tMat.col2.x*rY);
rY=(tMat.col1.y*rX+tMat.col2.y*rY);
rX=tX;
var CdotX=b.m_linearVelocity.x+(-b.m_angularVelocity*rY);
var CdotY=b.m_linearVelocity.y+(b.m_angularVelocity*rX);
tMat=this.m_mass$2;
tX=CdotX+this.m_beta$2*this.m_C$2.x+this.m_gamma$2*this.m_impulse$2.x;
tY=CdotY+this.m_beta$2*this.m_C$2.y+this.m_gamma$2*this.m_impulse$2.y;
var impulseX=-(tMat.col1.x*tX+tMat.col2.x*tY);
var impulseY=-(tMat.col1.y*tX+tMat.col2.y*tY);
var oldImpulseX=this.m_impulse$2.x;
var oldImpulseY=this.m_impulse$2.y;
this.m_impulse$2.x+=impulseX;
this.m_impulse$2.y+=impulseY;
var maxImpulse=step.dt*this.m_maxForce$2;
if(this.m_impulse$2.LengthSquared()>maxImpulse*maxImpulse)
{
this.m_impulse$2.Multiply(maxImpulse/this.m_impulse$2.Length());
}
impulseX=this.m_impulse$2.x-oldImpulseX;
impulseY=this.m_impulse$2.y-oldImpulseY;
b.m_linearVelocity.x+=b.m_invMass*impulseX;
b.m_linearVelocity.y+=b.m_invMass*impulseY;
b.m_angularVelocity+=b.m_invI*(rX*impulseY-rY*impulseX);
},
"b2internal override function SolvePositionConstraints",function(baumgarte){
return true;
},
"private var",{m_localAnchor:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_target:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_impulse:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_mass:function(){return(new Box2D.Common.Math.b2Mat22());}},
"private var",{m_C:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_maxForce:NaN},
"private var",{m_frequencyHz:NaN},
"private var",{m_dampingRatio:NaN},
"private var",{m_beta:NaN},
"private var",{m_gamma:NaN},
];},[],["Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Mat22","Math"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2MouseJointDef
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2MouseJointDef extends Box2D.Dynamics.Joints.b2JointDef",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint);},

"public function b2MouseJointDef",function()
{this.super$2();this.target=this.target();
this.type=Box2D.Dynamics.Joints.b2Joint.e_mouseJoint;
this.maxForce=0.0;
this.frequencyHz=5.0;
this.dampingRatio=0.7;
},
"public var",{target:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{maxForce:NaN},
"public var",{frequencyHz:NaN},
"public var",{dampingRatio:NaN},
];},[],["Box2D.Dynamics.Joints.b2JointDef","Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2PrismaticJoint
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2PrismaticJoint extends Box2D.Dynamics.Joints.b2Joint",2,function($$private){;return[function(){joo.classLoader.init(Number,Box2D.Common.b2Settings);},

"public override function GetAnchorA",function(){
return this.m_bodyA.GetWorldPoint(this.m_localAnchor1);
},
"public override function GetAnchorB",function(){
return this.m_bodyB.GetWorldPoint(this.m_localAnchor2);
},
"public override function GetReactionForce",function(inv_dt)
{
return new Box2D.Common.Math.b2Vec2(inv_dt*(this.m_impulse$2.x*this.m_perp$2.x+(this.m_motorImpulse$2+this.m_impulse$2.z)*this.m_axis$2.x),
inv_dt*(this.m_impulse$2.x*this.m_perp$2.y+(this.m_motorImpulse$2+this.m_impulse$2.z)*this.m_axis$2.y));
},
"public override function GetReactionTorque",function(inv_dt)
{
return inv_dt*this.m_impulse$2.y;
},
"public function GetJointTranslation",function(){
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var tMat;
var p1=bA.GetWorldPoint(this.m_localAnchor1);
var p2=bB.GetWorldPoint(this.m_localAnchor2);
var dX=p2.x-p1.x;
var dY=p2.y-p1.y;
var axis=bA.GetWorldVector(this.m_localXAxis1);
var translation=axis.x*dX+axis.y*dY;
return translation;
},
"public function GetJointSpeed",function(){
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var tMat;
tMat=bA.m_xf.R;
var r1X=this.m_localAnchor1.x-bA.m_sweep.localCenter.x;
var r1Y=this.m_localAnchor1.y-bA.m_sweep.localCenter.y;
var tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=bB.m_xf.R;
var r2X=this.m_localAnchor2.x-bB.m_sweep.localCenter.x;
var r2Y=this.m_localAnchor2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
var p1X=bA.m_sweep.c.x+r1X;
var p1Y=bA.m_sweep.c.y+r1Y;
var p2X=bB.m_sweep.c.x+r2X;
var p2Y=bB.m_sweep.c.y+r2Y;
var dX=p2X-p1X;
var dY=p2Y-p1Y;
var axis=bA.GetWorldVector(this.m_localXAxis1);
var v1=bA.m_linearVelocity;
var v2=bB.m_linearVelocity;
var w1=bA.m_angularVelocity;
var w2=bB.m_angularVelocity;
var speed=(dX*(-w1*axis.y)+dY*(w1*axis.x))+(axis.x*(((v2.x+(-w2*r2Y))-v1.x)-(-w1*r1Y))+axis.y*(((v2.y+(w2*r2X))-v1.y)-(w1*r1X)));
return speed;
},
"public function IsLimitEnabled",function()
{
return this.m_enableLimit$2;
},
"public function EnableLimit",function(flag)
{
this.m_bodyA.SetAwake(true);
this.m_bodyB.SetAwake(true);
this.m_enableLimit$2=flag;
},
"public function GetLowerLimit",function()
{
return this.m_lowerTranslation$2;
},
"public function GetUpperLimit",function()
{
return this.m_upperTranslation$2;
},
"public function SetLimits",function(lower,upper)
{
this.m_bodyA.SetAwake(true);
this.m_bodyB.SetAwake(true);
this.m_lowerTranslation$2=lower;
this.m_upperTranslation$2=upper;
},
"public function IsMotorEnabled",function()
{
return this.m_enableMotor$2;
},
"public function EnableMotor",function(flag)
{
this.m_bodyA.SetAwake(true);
this.m_bodyB.SetAwake(true);
this.m_enableMotor$2=flag;
},
"public function SetMotorSpeed",function(speed)
{
this.m_bodyA.SetAwake(true);
this.m_bodyB.SetAwake(true);
this.m_motorSpeed$2=speed;
},
"public function GetMotorSpeed",function()
{
return this.m_motorSpeed$2;
},
"public function SetMaxMotorForce",function(force)
{
this.m_bodyA.SetAwake(true);
this.m_bodyB.SetAwake(true);
this.m_maxMotorForce$2=force;
},
"public function GetMotorForce",function()
{
return this.m_motorImpulse$2;
},
"public function b2PrismaticJoint",function(def){
this.super$2(def);this.m_localAnchor1=this.m_localAnchor1();this.m_localAnchor2=this.m_localAnchor2();this.m_localXAxis1=this.m_localXAxis1();this.m_localYAxis1$2=this.m_localYAxis1$2();this.m_axis$2=this.m_axis$2();this.m_perp$2=this.m_perp$2();this.m_K$2=this.m_K$2();this.m_impulse$2=this.m_impulse$2();
var tMat;
var tX;
var tY;
this.m_localAnchor1.SetV(def.localAnchorA);
this.m_localAnchor2.SetV(def.localAnchorB);
this.m_localXAxis1.SetV(def.localAxisA);
this.m_localYAxis1$2.x=-this.m_localXAxis1.y;
this.m_localYAxis1$2.y=this.m_localXAxis1.x;
this.m_refAngle$2=def.referenceAngle;
this.m_impulse$2.SetZero();
this.m_motorMass$2=0.0;
this.m_motorImpulse$2=0.0;
this.m_lowerTranslation$2=def.lowerTranslation;
this.m_upperTranslation$2=def.upperTranslation;
this.m_maxMotorForce$2=def.maxMotorForce;
this.m_motorSpeed$2=def.motorSpeed;
this.m_enableLimit$2=def.enableLimit;
this.m_enableMotor$2=def.enableMotor;
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
this.m_axis$2.SetZero();
this.m_perp$2.SetZero();
},
"b2internal override function InitVelocityConstraints",function(step){
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var tMat;
var tVec;
var tX;
this.m_localCenterA.SetV(bA.m_sweep.localCenter);
this.m_localCenterB.SetV(bB.m_sweep.localCenter);
var xf1=bA.m_xf;
var xf2=bB.m_xf;
tMat=bA.m_xf.R;
var r1X=this.m_localAnchor1.x-this.m_localCenterA.x;
var r1Y=this.m_localAnchor1.y-this.m_localCenterA.y;
tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=bB.m_xf.R;
var r2X=this.m_localAnchor2.x-this.m_localCenterB.x;
var r2Y=this.m_localAnchor2.y-this.m_localCenterB.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
var dX=bB.m_sweep.c.x+r2X-bA.m_sweep.c.x-r1X;
var dY=bB.m_sweep.c.y+r2Y-bA.m_sweep.c.y-r1Y;
this.m_invMassA=bA.m_invMass;
this.m_invMassB=bB.m_invMass;
this.m_invIA=bA.m_invI;
this.m_invIB=bB.m_invI;
{
tVec=this.m_localXAxis1;
tMat=xf1.R;
this.m_axis$2.x=(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
this.m_axis$2.y=(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
this.m_a1$2=(dX+r1X)*this.m_axis$2.y-(dY+r1Y)*this.m_axis$2.x;
this.m_a2$2=r2X*this.m_axis$2.y-r2Y*this.m_axis$2.x;
this.m_motorMass$2=this.m_invMassA+this.m_invMassB+this.m_invIA*this.m_a1$2*this.m_a1$2+this.m_invIB*this.m_a2$2*this.m_a2$2;
if(this.m_motorMass$2>Number.MIN_VALUE)
this.m_motorMass$2=1.0/this.m_motorMass$2;
}
{
tVec=this.m_localYAxis1$2;
tMat=xf1.R;
this.m_perp$2.x=(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
this.m_perp$2.y=(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
this.m_s1$2=(dX+r1X)*this.m_perp$2.y-(dY+r1Y)*this.m_perp$2.x;
this.m_s2$2=r2X*this.m_perp$2.y-r2Y*this.m_perp$2.x;
var m1=this.m_invMassA;
var m2=this.m_invMassB;
var i1=this.m_invIA;
var i2=this.m_invIB;
this.m_K$2.col1.x=m1+m2+i1*this.m_s1$2*this.m_s1$2+i2*this.m_s2$2*this.m_s2$2;
this.m_K$2.col1.y=i1*this.m_s1$2+i2*this.m_s2$2;
this.m_K$2.col1.z=i1*this.m_s1$2*this.m_a1$2+i2*this.m_s2$2*this.m_a2$2;
this.m_K$2.col2.x=this.m_K$2.col1.y;
this.m_K$2.col2.y=i1+i2;
this.m_K$2.col2.z=i1*this.m_a1$2+i2*this.m_a2$2;
this.m_K$2.col3.x=this.m_K$2.col1.z;
this.m_K$2.col3.y=this.m_K$2.col2.z;
this.m_K$2.col3.z=m1+m2+i1*this.m_a1$2*this.m_a1$2+i2*this.m_a2$2*this.m_a2$2;
}
if(this.m_enableLimit$2)
{
var jointTransition=this.m_axis$2.x*dX+this.m_axis$2.y*dY;
if(Box2D.Common.Math.b2Math.Abs(this.m_upperTranslation$2-this.m_lowerTranslation$2)<2.0*Box2D.Common.b2Settings.b2_linearSlop)
{
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_equalLimits;
}
else if(jointTransition<=this.m_lowerTranslation$2)
{
if(this.m_limitState$2!=Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit)
{
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit;
this.m_impulse$2.z=0.0;
}
}
else if(jointTransition>=this.m_upperTranslation$2)
{
if(this.m_limitState$2!=Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
{
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit;
this.m_impulse$2.z=0.0;
}
}
else
{
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
this.m_impulse$2.z=0.0;
}
}
else
{
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
}
if(this.m_enableMotor$2==false)
{
this.m_motorImpulse$2=0.0;
}
if(step.warmStarting)
{
this.m_impulse$2.x*=step.dtRatio;
this.m_impulse$2.y*=step.dtRatio;
this.m_motorImpulse$2*=step.dtRatio;
var PX=this.m_impulse$2.x*this.m_perp$2.x+(this.m_motorImpulse$2+this.m_impulse$2.z)*this.m_axis$2.x;
var PY=this.m_impulse$2.x*this.m_perp$2.y+(this.m_motorImpulse$2+this.m_impulse$2.z)*this.m_axis$2.y;
var L1=this.m_impulse$2.x*this.m_s1$2+this.m_impulse$2.y+(this.m_motorImpulse$2+this.m_impulse$2.z)*this.m_a1$2;
var L2=this.m_impulse$2.x*this.m_s2$2+this.m_impulse$2.y+(this.m_motorImpulse$2+this.m_impulse$2.z)*this.m_a2$2;
bA.m_linearVelocity.x-=this.m_invMassA*PX;
bA.m_linearVelocity.y-=this.m_invMassA*PY;
bA.m_angularVelocity-=this.m_invIA*L1;
bB.m_linearVelocity.x+=this.m_invMassB*PX;
bB.m_linearVelocity.y+=this.m_invMassB*PY;
bB.m_angularVelocity+=this.m_invIB*L2;
}
else
{
this.m_impulse$2.SetZero();
this.m_motorImpulse$2=0.0;
}
},
"private static var",{s_f2r:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private static var",{s_f1:function(){return(new Box2D.Common.Math.b2Vec3());}},
"b2internal override function SolveVelocityConstraints",function(step){
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var v1=bA.m_linearVelocity;
var w1=bA.m_angularVelocity;
var v2=bB.m_linearVelocity;
var w2=bB.m_angularVelocity;
var PX;
var PY;
var L1;
var L2;
if(this.m_enableMotor$2&&this.m_limitState$2!=Box2D.Dynamics.Joints.b2Joint.e_equalLimits)
{
var Cdot=this.m_axis$2.x*(v2.x-v1.x)+this.m_axis$2.y*(v2.y-v1.y)+this.m_a2$2*w2-this.m_a1$2*w1;
var impulse=this.m_motorMass$2*(this.m_motorSpeed$2-Cdot);
var oldImpulse=this.m_motorImpulse$2;
var maxImpulse=step.dt*this.m_maxMotorForce$2;
this.m_motorImpulse$2=Box2D.Common.Math.b2Math.Clamp(this.m_motorImpulse$2+impulse,-maxImpulse,maxImpulse);
impulse=this.m_motorImpulse$2-oldImpulse;
PX=impulse*this.m_axis$2.x;
PY=impulse*this.m_axis$2.y;
L1=impulse*this.m_a1$2;
L2=impulse*this.m_a2$2;
v1.x-=this.m_invMassA*PX;
v1.y-=this.m_invMassA*PY;
w1-=this.m_invIA*L1;
v2.x+=this.m_invMassB*PX;
v2.y+=this.m_invMassB*PY;
w2+=this.m_invIB*L2;
}
var Cdot1X=this.m_perp$2.x*(v2.x-v1.x)+this.m_perp$2.y*(v2.y-v1.y)+this.m_s2$2*w2-this.m_s1$2*w1;
var Cdot1Y=w2-w1;
if(this.m_enableLimit$2&&this.m_limitState$2!=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit)
{
var Cdot2=this.m_axis$2.x*(v2.x-v1.x)+this.m_axis$2.y*(v2.y-v1.y)+this.m_a2$2*w2-this.m_a1$2*w1;
var f1=$$private.s_f1;
f1.SetV(this.m_impulse$2);
var df=this.m_K$2.Solve33($$private.s_vec3,-Cdot1X,-Cdot1Y,-Cdot2);
this.m_impulse$2.Add(df);
if(this.m_limitState$2==Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit)
{
this.m_impulse$2.z=Box2D.Common.Math.b2Math.Max(this.m_impulse$2.z,0.0);
}
else if(this.m_limitState$2==Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
{
this.m_impulse$2.z=Box2D.Common.Math.b2Math.Min(this.m_impulse$2.z,0.0);
}
var bX=-Cdot1X-(this.m_impulse$2.z-f1.z)*this.m_K$2.col3.x;
var bY=-Cdot1Y-(this.m_impulse$2.z-f1.z)*this.m_K$2.col3.y;
var f2r=this.m_K$2.Solve22($$private.s_f2r,bX,bY);
f2r.x+=f1.x;
f2r.y+=f1.y;
this.m_impulse$2.x=f2r.x;
this.m_impulse$2.y=f2r.y;
df.x=this.m_impulse$2.x-f1.x;
df.y=this.m_impulse$2.y-f1.y;
df.z=this.m_impulse$2.z-f1.z;
PX=df.x*this.m_perp$2.x+df.z*this.m_axis$2.x;
PY=df.x*this.m_perp$2.y+df.z*this.m_axis$2.y;
L1=df.x*this.m_s1$2+df.y+df.z*this.m_a1$2;
L2=df.x*this.m_s2$2+df.y+df.z*this.m_a2$2;
v1.x-=this.m_invMassA*PX;
v1.y-=this.m_invMassA*PY;
w1-=this.m_invIA*L1;
v2.x+=this.m_invMassB*PX;
v2.y+=this.m_invMassB*PY;
w2+=this.m_invIB*L2;
}
else
{
var df2=this.m_K$2.Solve22($$private.s_f2r,-Cdot1X,-Cdot1Y);
this.m_impulse$2.x+=df2.x;
this.m_impulse$2.y+=df2.y;
PX=df2.x*this.m_perp$2.x;
PY=df2.x*this.m_perp$2.y;
L1=df2.x*this.m_s1$2+df2.y;
L2=df2.x*this.m_s2$2+df2.y;
v1.x-=this.m_invMassA*PX;
v1.y-=this.m_invMassA*PY;
w1-=this.m_invIA*L1;
v2.x+=this.m_invMassB*PX;
v2.y+=this.m_invMassB*PY;
w2+=this.m_invIB*L2;
}
bA.m_linearVelocity.SetV(v1);
bA.m_angularVelocity=w1;
bB.m_linearVelocity.SetV(v2);
bB.m_angularVelocity=w2;
},
"private static var",{s_R1:function(){return(new Box2D.Common.Math.b2Mat22());}},
"private static var",{s_R2:function(){return(new Box2D.Common.Math.b2Mat22());}},
"private static var",{s_vec3:function(){return(new Box2D.Common.Math.b2Vec3());}},
"b2internal override function SolvePositionConstraints",function(baumgarte)
{
var limitC;
var oldLimitImpulse;
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var c1=bA.m_sweep.c;
var a1=bA.m_sweep.a;
var c2=bB.m_sweep.c;
var a2=bB.m_sweep.a;
var tMat;
var tVec;
var tX;
var m1;
var m2;
var i1;
var i2;
var linearError=0.0;
var angularError=0.0;
var active=false;
var C2=0.0;
var R1=$$private.s_R1;
var R2=$$private.s_R2;
R1.Set(a1);
R2.Set(a2);
tMat=R1;
var r1X=this.m_localAnchor1.x-this.m_localCenterA.x;
var r1Y=this.m_localAnchor1.y-this.m_localCenterA.y;
tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=R2;
var r2X=this.m_localAnchor2.x-this.m_localCenterB.x;
var r2Y=this.m_localAnchor2.y-this.m_localCenterB.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
var dX=c2.x+r2X-c1.x-r1X;
var dY=c2.y+r2Y-c1.y-r1Y;
if(this.m_enableLimit$2)
{
tVec=this.m_localXAxis1;
tMat=R1;
this.m_axis$2.x=(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
this.m_axis$2.y=(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
this.m_a1$2=(dX+r1X)*this.m_axis$2.y-(dY+r1Y)*this.m_axis$2.x;
this.m_a2$2=r2X*this.m_axis$2.y-r2Y*this.m_axis$2.x;
var translation=this.m_axis$2.x*dX+this.m_axis$2.y*dY;
if(Box2D.Common.Math.b2Math.Abs(this.m_upperTranslation$2-this.m_lowerTranslation$2)<2.0*Box2D.Common.b2Settings.b2_linearSlop)
{
C2=Box2D.Common.Math.b2Math.Clamp(translation,-Box2D.Common.b2Settings.b2_maxLinearCorrection,Box2D.Common.b2Settings.b2_maxLinearCorrection);
linearError=Box2D.Common.Math.b2Math.Abs(translation);
active=true;
}
else if(translation<=this.m_lowerTranslation$2)
{
C2=Box2D.Common.Math.b2Math.Clamp(translation-this.m_lowerTranslation$2+Box2D.Common.b2Settings.b2_linearSlop,-Box2D.Common.b2Settings.b2_maxLinearCorrection,0.0);
linearError=this.m_lowerTranslation$2-translation;
active=true;
}
else if(translation>=this.m_upperTranslation$2)
{
C2=Box2D.Common.Math.b2Math.Clamp(translation-this.m_upperTranslation$2+Box2D.Common.b2Settings.b2_linearSlop,0.0,Box2D.Common.b2Settings.b2_maxLinearCorrection);
linearError=translation-this.m_upperTranslation$2;
active=true;
}
}
tVec=this.m_localYAxis1$2;
tMat=R1;
this.m_perp$2.x=(tMat.col1.x*tVec.x+tMat.col2.x*tVec.y);
this.m_perp$2.y=(tMat.col1.y*tVec.x+tMat.col2.y*tVec.y);
this.m_s1$2=(dX+r1X)*this.m_perp$2.y-(dY+r1Y)*this.m_perp$2.x;
this.m_s2$2=r2X*this.m_perp$2.y-r2Y*this.m_perp$2.x;
var impulse=$$private.s_vec3;
var C1X=this.m_perp$2.x*dX+this.m_perp$2.y*dY;
var C1Y=a2-a1-this.m_refAngle$2;
linearError=Box2D.Common.Math.b2Math.Max(linearError,Box2D.Common.Math.b2Math.Abs(C1X));
angularError=Box2D.Common.Math.b2Math.Abs(C1Y);
if(active)
{
m1=this.m_invMassA;
m2=this.m_invMassB;
i1=this.m_invIA;
i2=this.m_invIB;
this.m_K$2.col1.x=m1+m2+i1*this.m_s1$2*this.m_s1$2+i2*this.m_s2$2*this.m_s2$2;
this.m_K$2.col1.y=i1*this.m_s1$2+i2*this.m_s2$2;
this.m_K$2.col1.z=i1*this.m_s1$2*this.m_a1$2+i2*this.m_s2$2*this.m_a2$2;
this.m_K$2.col2.x=this.m_K$2.col1.y;
this.m_K$2.col2.y=i1+i2;
this.m_K$2.col2.z=i1*this.m_a1$2+i2*this.m_a2$2;
this.m_K$2.col3.x=this.m_K$2.col1.z;
this.m_K$2.col3.y=this.m_K$2.col2.z;
this.m_K$2.col3.z=m1+m2+i1*this.m_a1$2*this.m_a1$2+i2*this.m_a2$2*this.m_a2$2;
this.m_K$2.Solve33(impulse,-C1X,-C1Y,-C2);
}
else
{
m1=this.m_invMassA;
m2=this.m_invMassB;
i1=this.m_invIA;
i2=this.m_invIB;
var k11=m1+m2+i1*this.m_s1$2*this.m_s1$2+i2*this.m_s2$2*this.m_s2$2;
var k12=i1*this.m_s1$2+i2*this.m_s2$2;
var k22=i1+i2;
this.m_K$2.col1.Set(k11,k12,0.0);
this.m_K$2.col2.Set(k12,k22,0.0);
var impulse1=this.m_K$2.Solve22(new Box2D.Common.Math.b2Vec2(),-C1X,-C1Y);
impulse.x=impulse1.x;
impulse.y=impulse1.y;
impulse.z=0.0;
}
var PX=impulse.x*this.m_perp$2.x+impulse.z*this.m_axis$2.x;
var PY=impulse.x*this.m_perp$2.y+impulse.z*this.m_axis$2.y;
var L1=impulse.x*this.m_s1$2+impulse.y+impulse.z*this.m_a1$2;
var L2=impulse.x*this.m_s2$2+impulse.y+impulse.z*this.m_a2$2;
c1.x-=this.m_invMassA*PX;
c1.y-=this.m_invMassA*PY;
a1-=this.m_invIA*L1;
c2.x+=this.m_invMassB*PX;
c2.y+=this.m_invMassB*PY;
a2+=this.m_invIB*L2;
bA.m_sweep.a=a1;
bB.m_sweep.a=a2;
bA.SynchronizeTransform();
bB.SynchronizeTransform();
return linearError<=Box2D.Common.b2Settings.b2_linearSlop&&angularError<=Box2D.Common.b2Settings.b2_angularSlop;
},
"b2internal var",{m_localAnchor1:function(){return(new Box2D.Common.Math.b2Vec2());}},
"b2internal var",{m_localAnchor2:function(){return(new Box2D.Common.Math.b2Vec2());}},
"b2internal var",{m_localXAxis1:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_localYAxis1:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_refAngle:NaN},
"private var",{m_axis:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_perp:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_s1:NaN},
"private var",{m_s2:NaN},
"private var",{m_a1:NaN},
"private var",{m_a2:NaN},
"private var",{m_K:function(){return(new Box2D.Common.Math.b2Mat33());}},
"private var",{m_impulse:function(){return(new Box2D.Common.Math.b2Vec3());}},
"private var",{m_motorMass:NaN},
"private var",{m_motorImpulse:NaN},
"private var",{m_lowerTranslation:NaN},
"private var",{m_upperTranslation:NaN},
"private var",{m_maxMotorForce:NaN},
"private var",{m_motorSpeed:NaN},
"private var",{m_enableLimit:false},
"private var",{m_enableMotor:false},
"private var",{m_limitState:0},
];},[],["Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2","Number","Box2D.Common.Math.b2Math","Box2D.Common.b2Settings","Box2D.Common.Math.b2Vec3","Box2D.Common.Math.b2Mat22","Box2D.Common.Math.b2Mat33"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2PrismaticJointDef
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2PrismaticJointDef extends Box2D.Dynamics.Joints.b2JointDef",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint);},

"public function b2PrismaticJointDef",function()
{this.super$2();this.localAnchorA=this.localAnchorA();this.localAnchorB=this.localAnchorB();this.localAxisA=this.localAxisA();
this.type=Box2D.Dynamics.Joints.b2Joint.e_prismaticJoint;
this.localAxisA.Set(1.0,0.0);
this.referenceAngle=0.0;
this.enableLimit=false;
this.lowerTranslation=0.0;
this.upperTranslation=0.0;
this.enableMotor=false;
this.maxMotorForce=0.0;
this.motorSpeed=0.0;
},
"public function Initialize",function(bA,bB,anchor,axis)
{
this.bodyA=bA;
this.bodyB=bB;
this.localAnchorA=this.bodyA.GetLocalPoint(anchor);
this.localAnchorB=this.bodyB.GetLocalPoint(anchor);
this.localAxisA=this.bodyA.GetLocalVector(axis);
this.referenceAngle=this.bodyB.GetAngle()-this.bodyA.GetAngle();
},
"public var",{localAnchorA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{localAnchorB:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{localAxisA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{referenceAngle:NaN},
"public var",{enableLimit:false},
"public var",{lowerTranslation:NaN},
"public var",{upperTranslation:NaN},
"public var",{enableMotor:false},
"public var",{maxMotorForce:NaN},
"public var",{motorSpeed:NaN},
];},[],["Box2D.Dynamics.Joints.b2JointDef","Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2PulleyJoint
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2PulleyJoint extends Box2D.Dynamics.Joints.b2Joint",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Common.b2Settings);},

"public override function GetAnchorA",function(){
return this.m_bodyA.GetWorldPoint(this.m_localAnchor1$2);
},
"public override function GetAnchorB",function(){
return this.m_bodyB.GetWorldPoint(this.m_localAnchor2$2);
},
"public override function GetReactionForce",function(inv_dt)
{
return new Box2D.Common.Math.b2Vec2(inv_dt*this.m_impulse$2*this.m_u2$2.x,inv_dt*this.m_impulse$2*this.m_u2$2.y);
},
"public override function GetReactionTorque",function(inv_dt)
{
return 0.0;
},
"public function GetGroundAnchorA",function()
{
var a=this.m_ground$2.m_xf.position.Copy();
a.Add(this.m_groundAnchor1$2);
return a;
},
"public function GetGroundAnchorB",function()
{
var a=this.m_ground$2.m_xf.position.Copy();
a.Add(this.m_groundAnchor2$2);
return a;
},
"public function GetLength1",function()
{
var p=this.m_bodyA.GetWorldPoint(this.m_localAnchor1$2);
var sX=this.m_ground$2.m_xf.position.x+this.m_groundAnchor1$2.x;
var sY=this.m_ground$2.m_xf.position.y+this.m_groundAnchor1$2.y;
var dX=p.x-sX;
var dY=p.y-sY;
return Math.sqrt(dX*dX+dY*dY);
},
"public function GetLength2",function()
{
var p=this.m_bodyB.GetWorldPoint(this.m_localAnchor2$2);
var sX=this.m_ground$2.m_xf.position.x+this.m_groundAnchor2$2.x;
var sY=this.m_ground$2.m_xf.position.y+this.m_groundAnchor2$2.y;
var dX=p.x-sX;
var dY=p.y-sY;
return Math.sqrt(dX*dX+dY*dY);
},
"public function GetRatio",function(){
return this.m_ratio$2;
},
"public function b2PulleyJoint",function(def){
this.super$2(def);this.m_groundAnchor1$2=this.m_groundAnchor1$2();this.m_groundAnchor2$2=this.m_groundAnchor2$2();this.m_localAnchor1$2=this.m_localAnchor1$2();this.m_localAnchor2$2=this.m_localAnchor2$2();this.m_u1$2=this.m_u1$2();this.m_u2$2=this.m_u2$2();
var tMat;
var tX;
var tY;
this.m_ground$2=this.m_bodyA.m_world.m_groundBody;
this.m_groundAnchor1$2.x=def.groundAnchorA.x-this.m_ground$2.m_xf.position.x;
this.m_groundAnchor1$2.y=def.groundAnchorA.y-this.m_ground$2.m_xf.position.y;
this.m_groundAnchor2$2.x=def.groundAnchorB.x-this.m_ground$2.m_xf.position.x;
this.m_groundAnchor2$2.y=def.groundAnchorB.y-this.m_ground$2.m_xf.position.y;
this.m_localAnchor1$2.SetV(def.localAnchorA);
this.m_localAnchor2$2.SetV(def.localAnchorB);
this.m_ratio$2=def.ratio;
this.m_constant$2=def.lengthA+this.m_ratio$2*def.lengthB;
this.m_maxLength1$2=Box2D.Common.Math.b2Math.Min(def.maxLengthA,this.m_constant$2-this.m_ratio$2*Box2D.Dynamics.Joints.b2PulleyJoint.b2_minPulleyLength);
this.m_maxLength2$2=Box2D.Common.Math.b2Math.Min(def.maxLengthB,(this.m_constant$2-Box2D.Dynamics.Joints.b2PulleyJoint.b2_minPulleyLength)/this.m_ratio$2);
this.m_impulse$2=0.0;
this.m_limitImpulse1$2=0.0;
this.m_limitImpulse2$2=0.0;
},
"b2internal override function InitVelocityConstraints",function(step){
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var tMat;
tMat=bA.m_xf.R;
var r1X=this.m_localAnchor1$2.x-bA.m_sweep.localCenter.x;
var r1Y=this.m_localAnchor1$2.y-bA.m_sweep.localCenter.y;
var tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=bB.m_xf.R;
var r2X=this.m_localAnchor2$2.x-bB.m_sweep.localCenter.x;
var r2Y=this.m_localAnchor2$2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
var p1X=bA.m_sweep.c.x+r1X;
var p1Y=bA.m_sweep.c.y+r1Y;
var p2X=bB.m_sweep.c.x+r2X;
var p2Y=bB.m_sweep.c.y+r2Y;
var s1X=this.m_ground$2.m_xf.position.x+this.m_groundAnchor1$2.x;
var s1Y=this.m_ground$2.m_xf.position.y+this.m_groundAnchor1$2.y;
var s2X=this.m_ground$2.m_xf.position.x+this.m_groundAnchor2$2.x;
var s2Y=this.m_ground$2.m_xf.position.y+this.m_groundAnchor2$2.y;
this.m_u1$2.Set(p1X-s1X,p1Y-s1Y);
this.m_u2$2.Set(p2X-s2X,p2Y-s2Y);
var length1=this.m_u1$2.Length();
var length2=this.m_u2$2.Length();
if(length1>Box2D.Common.b2Settings.b2_linearSlop)
{
this.m_u1$2.Multiply(1.0/length1);
}
else
{
this.m_u1$2.SetZero();
}
if(length2>Box2D.Common.b2Settings.b2_linearSlop)
{
this.m_u2$2.Multiply(1.0/length2);
}
else
{
this.m_u2$2.SetZero();
}
var C=this.m_constant$2-length1-this.m_ratio$2*length2;
if(C>0.0)
{
this.m_state$2=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
this.m_impulse$2=0.0;
}
else
{
this.m_state$2=Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit;
}
if(length1<this.m_maxLength1$2)
{
this.m_limitState1$2=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
this.m_limitImpulse1$2=0.0;
}
else
{
this.m_limitState1$2=Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit;
}
if(length2<this.m_maxLength2$2)
{
this.m_limitState2$2=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
this.m_limitImpulse2$2=0.0;
}
else
{
this.m_limitState2$2=Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit;
}
var cr1u1=r1X*this.m_u1$2.y-r1Y*this.m_u1$2.x;
var cr2u2=r2X*this.m_u2$2.y-r2Y*this.m_u2$2.x;
this.m_limitMass1$2=bA.m_invMass+bA.m_invI*cr1u1*cr1u1;
this.m_limitMass2$2=bB.m_invMass+bB.m_invI*cr2u2*cr2u2;
this.m_pulleyMass$2=this.m_limitMass1$2+this.m_ratio$2*this.m_ratio$2*this.m_limitMass2$2;
this.m_limitMass1$2=1.0/this.m_limitMass1$2;
this.m_limitMass2$2=1.0/this.m_limitMass2$2;
this.m_pulleyMass$2=1.0/this.m_pulleyMass$2;
if(step.warmStarting)
{
this.m_impulse$2*=step.dtRatio;
this.m_limitImpulse1$2*=step.dtRatio;
this.m_limitImpulse2$2*=step.dtRatio;
var P1X=(-this.m_impulse$2-this.m_limitImpulse1$2)*this.m_u1$2.x;
var P1Y=(-this.m_impulse$2-this.m_limitImpulse1$2)*this.m_u1$2.y;
var P2X=(-this.m_ratio$2*this.m_impulse$2-this.m_limitImpulse2$2)*this.m_u2$2.x;
var P2Y=(-this.m_ratio$2*this.m_impulse$2-this.m_limitImpulse2$2)*this.m_u2$2.y;
bA.m_linearVelocity.x+=bA.m_invMass*P1X;
bA.m_linearVelocity.y+=bA.m_invMass*P1Y;
bA.m_angularVelocity+=bA.m_invI*(r1X*P1Y-r1Y*P1X);
bB.m_linearVelocity.x+=bB.m_invMass*P2X;
bB.m_linearVelocity.y+=bB.m_invMass*P2Y;
bB.m_angularVelocity+=bB.m_invI*(r2X*P2Y-r2Y*P2X);
}
else
{
this.m_impulse$2=0.0;
this.m_limitImpulse1$2=0.0;
this.m_limitImpulse2$2=0.0;
}
},
"b2internal override function SolveVelocityConstraints",function(step)
{
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var tMat;
tMat=bA.m_xf.R;
var r1X=this.m_localAnchor1$2.x-bA.m_sweep.localCenter.x;
var r1Y=this.m_localAnchor1$2.y-bA.m_sweep.localCenter.y;
var tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=bB.m_xf.R;
var r2X=this.m_localAnchor2$2.x-bB.m_sweep.localCenter.x;
var r2Y=this.m_localAnchor2$2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
var v1X;
var v1Y;
var v2X;
var v2Y;
var P1X;
var P1Y;
var P2X;
var P2Y;
var Cdot;
var impulse;
var oldImpulse;
if(this.m_state$2==Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
{
v1X=bA.m_linearVelocity.x+(-bA.m_angularVelocity*r1Y);
v1Y=bA.m_linearVelocity.y+(bA.m_angularVelocity*r1X);
v2X=bB.m_linearVelocity.x+(-bB.m_angularVelocity*r2Y);
v2Y=bB.m_linearVelocity.y+(bB.m_angularVelocity*r2X);
Cdot=-(this.m_u1$2.x*v1X+this.m_u1$2.y*v1Y)-this.m_ratio$2*(this.m_u2$2.x*v2X+this.m_u2$2.y*v2Y);
impulse=this.m_pulleyMass$2*(-Cdot);
oldImpulse=this.m_impulse$2;
this.m_impulse$2=Box2D.Common.Math.b2Math.Max(0.0,this.m_impulse$2+impulse);
impulse=this.m_impulse$2-oldImpulse;
P1X=-impulse*this.m_u1$2.x;
P1Y=-impulse*this.m_u1$2.y;
P2X=-this.m_ratio$2*impulse*this.m_u2$2.x;
P2Y=-this.m_ratio$2*impulse*this.m_u2$2.y;
bA.m_linearVelocity.x+=bA.m_invMass*P1X;
bA.m_linearVelocity.y+=bA.m_invMass*P1Y;
bA.m_angularVelocity+=bA.m_invI*(r1X*P1Y-r1Y*P1X);
bB.m_linearVelocity.x+=bB.m_invMass*P2X;
bB.m_linearVelocity.y+=bB.m_invMass*P2Y;
bB.m_angularVelocity+=bB.m_invI*(r2X*P2Y-r2Y*P2X);
}
if(this.m_limitState1$2==Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
{
v1X=bA.m_linearVelocity.x+(-bA.m_angularVelocity*r1Y);
v1Y=bA.m_linearVelocity.y+(bA.m_angularVelocity*r1X);
Cdot=-(this.m_u1$2.x*v1X+this.m_u1$2.y*v1Y);
impulse=-this.m_limitMass1$2*Cdot;
oldImpulse=this.m_limitImpulse1$2;
this.m_limitImpulse1$2=Box2D.Common.Math.b2Math.Max(0.0,this.m_limitImpulse1$2+impulse);
impulse=this.m_limitImpulse1$2-oldImpulse;
P1X=-impulse*this.m_u1$2.x;
P1Y=-impulse*this.m_u1$2.y;
bA.m_linearVelocity.x+=bA.m_invMass*P1X;
bA.m_linearVelocity.y+=bA.m_invMass*P1Y;
bA.m_angularVelocity+=bA.m_invI*(r1X*P1Y-r1Y*P1X);
}
if(this.m_limitState2$2==Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
{
v2X=bB.m_linearVelocity.x+(-bB.m_angularVelocity*r2Y);
v2Y=bB.m_linearVelocity.y+(bB.m_angularVelocity*r2X);
Cdot=-(this.m_u2$2.x*v2X+this.m_u2$2.y*v2Y);
impulse=-this.m_limitMass2$2*Cdot;
oldImpulse=this.m_limitImpulse2$2;
this.m_limitImpulse2$2=Box2D.Common.Math.b2Math.Max(0.0,this.m_limitImpulse2$2+impulse);
impulse=this.m_limitImpulse2$2-oldImpulse;
P2X=-impulse*this.m_u2$2.x;
P2Y=-impulse*this.m_u2$2.y;
bB.m_linearVelocity.x+=bB.m_invMass*P2X;
bB.m_linearVelocity.y+=bB.m_invMass*P2Y;
bB.m_angularVelocity+=bB.m_invI*(r2X*P2Y-r2Y*P2X);
}
},
"b2internal override function SolvePositionConstraints",function(baumgarte)
{
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var tMat;
var s1X=this.m_ground$2.m_xf.position.x+this.m_groundAnchor1$2.x;
var s1Y=this.m_ground$2.m_xf.position.y+this.m_groundAnchor1$2.y;
var s2X=this.m_ground$2.m_xf.position.x+this.m_groundAnchor2$2.x;
var s2Y=this.m_ground$2.m_xf.position.y+this.m_groundAnchor2$2.y;
var r1X;
var r1Y;
var r2X;
var r2Y;
var p1X;
var p1Y;
var p2X;
var p2Y;
var length1;
var length2;
var C;
var impulse;
var oldImpulse;
var oldLimitPositionImpulse;
var tX;
var linearError=0.0;
if(this.m_state$2==Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
{
tMat=bA.m_xf.R;
r1X=this.m_localAnchor1$2.x-bA.m_sweep.localCenter.x;
r1Y=this.m_localAnchor1$2.y-bA.m_sweep.localCenter.y;
tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=bB.m_xf.R;
r2X=this.m_localAnchor2$2.x-bB.m_sweep.localCenter.x;
r2Y=this.m_localAnchor2$2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
p1X=bA.m_sweep.c.x+r1X;
p1Y=bA.m_sweep.c.y+r1Y;
p2X=bB.m_sweep.c.x+r2X;
p2Y=bB.m_sweep.c.y+r2Y;
this.m_u1$2.Set(p1X-s1X,p1Y-s1Y);
this.m_u2$2.Set(p2X-s2X,p2Y-s2Y);
length1=this.m_u1$2.Length();
length2=this.m_u2$2.Length();
if(length1>Box2D.Common.b2Settings.b2_linearSlop)
{
this.m_u1$2.Multiply(1.0/length1);
}
else
{
this.m_u1$2.SetZero();
}
if(length2>Box2D.Common.b2Settings.b2_linearSlop)
{
this.m_u2$2.Multiply(1.0/length2);
}
else
{
this.m_u2$2.SetZero();
}
C=this.m_constant$2-length1-this.m_ratio$2*length2;
linearError=Box2D.Common.Math.b2Math.Max(linearError,-C);
C=Box2D.Common.Math.b2Math.Clamp(C+Box2D.Common.b2Settings.b2_linearSlop,-Box2D.Common.b2Settings.b2_maxLinearCorrection,0.0);
impulse=-this.m_pulleyMass$2*C;
p1X=-impulse*this.m_u1$2.x;
p1Y=-impulse*this.m_u1$2.y;
p2X=-this.m_ratio$2*impulse*this.m_u2$2.x;
p2Y=-this.m_ratio$2*impulse*this.m_u2$2.y;
bA.m_sweep.c.x+=bA.m_invMass*p1X;
bA.m_sweep.c.y+=bA.m_invMass*p1Y;
bA.m_sweep.a+=bA.m_invI*(r1X*p1Y-r1Y*p1X);
bB.m_sweep.c.x+=bB.m_invMass*p2X;
bB.m_sweep.c.y+=bB.m_invMass*p2Y;
bB.m_sweep.a+=bB.m_invI*(r2X*p2Y-r2Y*p2X);
bA.SynchronizeTransform();
bB.SynchronizeTransform();
}
if(this.m_limitState1$2==Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
{
tMat=bA.m_xf.R;
r1X=this.m_localAnchor1$2.x-bA.m_sweep.localCenter.x;
r1Y=this.m_localAnchor1$2.y-bA.m_sweep.localCenter.y;
tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
p1X=bA.m_sweep.c.x+r1X;
p1Y=bA.m_sweep.c.y+r1Y;
this.m_u1$2.Set(p1X-s1X,p1Y-s1Y);
length1=this.m_u1$2.Length();
if(length1>Box2D.Common.b2Settings.b2_linearSlop)
{
this.m_u1$2.x*=1.0/length1;
this.m_u1$2.y*=1.0/length1;
}
else
{
this.m_u1$2.SetZero();
}
C=this.m_maxLength1$2-length1;
linearError=Box2D.Common.Math.b2Math.Max(linearError,-C);
C=Box2D.Common.Math.b2Math.Clamp(C+Box2D.Common.b2Settings.b2_linearSlop,-Box2D.Common.b2Settings.b2_maxLinearCorrection,0.0);
impulse=-this.m_limitMass1$2*C;
p1X=-impulse*this.m_u1$2.x;
p1Y=-impulse*this.m_u1$2.y;
bA.m_sweep.c.x+=bA.m_invMass*p1X;
bA.m_sweep.c.y+=bA.m_invMass*p1Y;
bA.m_sweep.a+=bA.m_invI*(r1X*p1Y-r1Y*p1X);
bA.SynchronizeTransform();
}
if(this.m_limitState2$2==Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
{
tMat=bB.m_xf.R;
r2X=this.m_localAnchor2$2.x-bB.m_sweep.localCenter.x;
r2Y=this.m_localAnchor2$2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
p2X=bB.m_sweep.c.x+r2X;
p2Y=bB.m_sweep.c.y+r2Y;
this.m_u2$2.Set(p2X-s2X,p2Y-s2Y);
length2=this.m_u2$2.Length();
if(length2>Box2D.Common.b2Settings.b2_linearSlop)
{
this.m_u2$2.x*=1.0/length2;
this.m_u2$2.y*=1.0/length2;
}
else
{
this.m_u2$2.SetZero();
}
C=this.m_maxLength2$2-length2;
linearError=Box2D.Common.Math.b2Math.Max(linearError,-C);
C=Box2D.Common.Math.b2Math.Clamp(C+Box2D.Common.b2Settings.b2_linearSlop,-Box2D.Common.b2Settings.b2_maxLinearCorrection,0.0);
impulse=-this.m_limitMass2$2*C;
p2X=-impulse*this.m_u2$2.x;
p2Y=-impulse*this.m_u2$2.y;
bB.m_sweep.c.x+=bB.m_invMass*p2X;
bB.m_sweep.c.y+=bB.m_invMass*p2Y;
bB.m_sweep.a+=bB.m_invI*(r2X*p2Y-r2Y*p2X);
bB.SynchronizeTransform();
}
return linearError<Box2D.Common.b2Settings.b2_linearSlop;
},
"private var",{m_ground:null},
"private var",{m_groundAnchor1:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_groundAnchor2:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_localAnchor1:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_localAnchor2:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_u1:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_u2:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_constant:NaN},
"private var",{m_ratio:NaN},
"private var",{m_maxLength1:NaN},
"private var",{m_maxLength2:NaN},
"private var",{m_pulleyMass:NaN},
"private var",{m_limitMass1:NaN},
"private var",{m_limitMass2:NaN},
"private var",{m_impulse:NaN},
"private var",{m_limitImpulse1:NaN},
"private var",{m_limitImpulse2:NaN},
"private var",{m_state:0},
"private var",{m_limitState1:0},
"private var",{m_limitState2:0},
"static b2internal const",{b2_minPulleyLength:2.0},
];},[],["Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2","Math","Box2D.Common.Math.b2Math","Box2D.Common.b2Settings"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2PulleyJointDef
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2PulleyJointDef extends Box2D.Dynamics.Joints.b2JointDef",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint,Box2D.Dynamics.Joints.b2PulleyJoint);},

"public function b2PulleyJointDef",function()
{this.super$2();this.groundAnchorA=this.groundAnchorA();this.groundAnchorB=this.groundAnchorB();this.localAnchorA=this.localAnchorA();this.localAnchorB=this.localAnchorB();
this.type=Box2D.Dynamics.Joints.b2Joint.e_pulleyJoint;
this.groundAnchorA.Set(-1.0,1.0);
this.groundAnchorB.Set(1.0,1.0);
this.localAnchorA.Set(-1.0,0.0);
this.localAnchorB.Set(1.0,0.0);
this.lengthA=0.0;
this.maxLengthA=0.0;
this.lengthB=0.0;
this.maxLengthB=0.0;
this.ratio=1.0;
this.collideConnected=true;
},
"public function Initialize",function(bA,bB,
gaA,gaB,
anchorA,anchorB,
r)
{
this.bodyA=bA;
this.bodyB=bB;
this.groundAnchorA.SetV(gaA);
this.groundAnchorB.SetV(gaB);
this.localAnchorA=this.bodyA.GetLocalPoint(anchorA);
this.localAnchorB=this.bodyB.GetLocalPoint(anchorB);
var d1X=anchorA.x-gaA.x;
var d1Y=anchorA.y-gaA.y;
this.lengthA=Math.sqrt(d1X*d1X+d1Y*d1Y);
var d2X=anchorB.x-gaB.x;
var d2Y=anchorB.y-gaB.y;
this.lengthB=Math.sqrt(d2X*d2X+d2Y*d2Y);
this.ratio=r;
var C=this.lengthA+this.ratio*this.lengthB;
this.maxLengthA=C-this.ratio*Box2D.Dynamics.Joints.b2PulleyJoint.b2_minPulleyLength;
this.maxLengthB=(C-Box2D.Dynamics.Joints.b2PulleyJoint.b2_minPulleyLength)/this.ratio;
},
"public var",{groundAnchorA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{groundAnchorB:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{localAnchorA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{localAnchorB:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{lengthA:NaN},
"public var",{maxLengthA:NaN},
"public var",{lengthB:NaN},
"public var",{maxLengthB:NaN},
"public var",{ratio:NaN},
];},[],["Box2D.Dynamics.Joints.b2JointDef","Box2D.Dynamics.Joints.b2Joint","Math","Box2D.Dynamics.Joints.b2PulleyJoint","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2RevoluteJoint
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2RevoluteJoint extends Box2D.Dynamics.Joints.b2Joint",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Common.b2Settings);},

"public override function GetAnchorA",function(){
return this.m_bodyA.GetWorldPoint(this.m_localAnchor1);
},
"public override function GetAnchorB",function(){
return this.m_bodyB.GetWorldPoint(this.m_localAnchor2);
},
"public override function GetReactionForce",function(inv_dt){
return new Box2D.Common.Math.b2Vec2(inv_dt*this.m_impulse$2.x,inv_dt*this.m_impulse$2.y);
},
"public override function GetReactionTorque",function(inv_dt){
return inv_dt*this.m_impulse$2.z;
},
"public function GetJointAngle",function(){
return this.m_bodyB.m_sweep.a-this.m_bodyA.m_sweep.a-this.m_referenceAngle$2;
},
"public function GetJointSpeed",function(){
return this.m_bodyB.m_angularVelocity-this.m_bodyA.m_angularVelocity;
},
"public function IsLimitEnabled",function(){
return this.m_enableLimit$2;
},
"public function EnableLimit",function(flag){
this.m_enableLimit$2=flag;
},
"public function GetLowerLimit",function(){
return this.m_lowerAngle$2;
},
"public function GetUpperLimit",function(){
return this.m_upperAngle$2;
},
"public function SetLimits",function(lower,upper){
this.m_lowerAngle$2=lower;
this.m_upperAngle$2=upper;
},
"public function IsMotorEnabled",function(){
this.m_bodyA.SetAwake(true);
this.m_bodyB.SetAwake(true);
return this.m_enableMotor$2;
},
"public function EnableMotor",function(flag){
this.m_enableMotor$2=flag;
},
"public function SetMotorSpeed",function(speed){
this.m_bodyA.SetAwake(true);
this.m_bodyB.SetAwake(true);
this.m_motorSpeed$2=speed;
},
"public function GetMotorSpeed",function(){
return this.m_motorSpeed$2;
},
"public function SetMaxMotorTorque",function(torque){
this.m_maxMotorTorque$2=torque;
},
"public function GetMotorTorque",function(){
return this.m_maxMotorTorque$2;
},
"public function b2RevoluteJoint",function(def){
this.super$2(def);this.K$2=this.K$2();this.K1$2=this.K1$2();this.K2$2=this.K2$2();this.K3$2=this.K3$2();this.impulse3$2=this.impulse3$2();this.impulse2$2=this.impulse2$2();this.reduced$2=this.reduced$2();this.m_localAnchor1=this.m_localAnchor1();this.m_localAnchor2=this.m_localAnchor2();this.m_impulse$2=this.m_impulse$2();this.m_mass$2=this.m_mass$2();
this.m_localAnchor1.SetV(def.localAnchorA);
this.m_localAnchor2.SetV(def.localAnchorB);
this.m_referenceAngle$2=def.referenceAngle;
this.m_impulse$2.SetZero();
this.m_motorImpulse$2=0.0;
this.m_lowerAngle$2=def.lowerAngle;
this.m_upperAngle$2=def.upperAngle;
this.m_maxMotorTorque$2=def.maxMotorTorque;
this.m_motorSpeed$2=def.motorSpeed;
this.m_enableLimit$2=def.enableLimit;
this.m_enableMotor$2=def.enableMotor;
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
},
"private var",{K:function(){return(new Box2D.Common.Math.b2Mat22());}},
"private var",{K1:function(){return(new Box2D.Common.Math.b2Mat22());}},
"private var",{K2:function(){return(new Box2D.Common.Math.b2Mat22());}},
"private var",{K3:function(){return(new Box2D.Common.Math.b2Mat22());}},
"b2internal override function InitVelocityConstraints",function(step){
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var tMat;
var tX;
if(this.m_enableMotor$2||this.m_enableLimit$2)
{
}
tMat=bA.m_xf.R;
var r1X=this.m_localAnchor1.x-bA.m_sweep.localCenter.x;
var r1Y=this.m_localAnchor1.y-bA.m_sweep.localCenter.y;
tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=bB.m_xf.R;
var r2X=this.m_localAnchor2.x-bB.m_sweep.localCenter.x;
var r2Y=this.m_localAnchor2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
var m1=bA.m_invMass;
var m2=bB.m_invMass;
var i1=bA.m_invI;
var i2=bB.m_invI;
this.m_mass$2.col1.x=m1+m2+r1Y*r1Y*i1+r2Y*r2Y*i2;
this.m_mass$2.col2.x=-r1Y*r1X*i1-r2Y*r2X*i2;
this.m_mass$2.col3.x=-r1Y*i1-r2Y*i2;
this.m_mass$2.col1.y=this.m_mass$2.col2.x;
this.m_mass$2.col2.y=m1+m2+r1X*r1X*i1+r2X*r2X*i2;
this.m_mass$2.col3.y=r1X*i1+r2X*i2;
this.m_mass$2.col1.z=this.m_mass$2.col3.x;
this.m_mass$2.col2.z=this.m_mass$2.col3.y;
this.m_mass$2.col3.z=i1+i2;
this.m_motorMass$2=i1+i2;
if(this.m_motorMass$2>0.0)
{
this.m_motorMass$2=1.0/this.m_motorMass$2;
}
if(this.m_enableMotor$2==false)
{
this.m_motorImpulse$2=0.0;
}
if(this.m_enableLimit$2)
{
var jointAngle=bB.m_sweep.a-bA.m_sweep.a-this.m_referenceAngle$2;
if(Box2D.Common.Math.b2Math.Abs(this.m_upperAngle$2-this.m_lowerAngle$2)<2.0*Box2D.Common.b2Settings.b2_angularSlop)
{
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_equalLimits;
}
else if(jointAngle<=this.m_lowerAngle$2)
{
if(this.m_limitState$2!=Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit)
{
this.m_impulse$2.z=0.0;
}
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit;
}
else if(jointAngle>=this.m_upperAngle$2)
{
if(this.m_limitState$2!=Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
{
this.m_impulse$2.z=0.0;
}
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit;
}
else
{
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
this.m_impulse$2.z=0.0;
}
}
else
{
this.m_limitState$2=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit;
}
if(step.warmStarting)
{
this.m_impulse$2.x*=step.dtRatio;
this.m_impulse$2.y*=step.dtRatio;
this.m_motorImpulse$2*=step.dtRatio;
var PX=this.m_impulse$2.x;
var PY=this.m_impulse$2.y;
bA.m_linearVelocity.x-=m1*PX;
bA.m_linearVelocity.y-=m1*PY;
bA.m_angularVelocity-=i1*((r1X*PY-r1Y*PX)+this.m_motorImpulse$2+this.m_impulse$2.z);
bB.m_linearVelocity.x+=m2*PX;
bB.m_linearVelocity.y+=m2*PY;
bB.m_angularVelocity+=i2*((r2X*PY-r2Y*PX)+this.m_motorImpulse$2+this.m_impulse$2.z);
}
else
{
this.m_impulse$2.SetZero();
this.m_motorImpulse$2=0.0;
}
},
"private var",{impulse3:function(){return(new Box2D.Common.Math.b2Vec3());}},
"private var",{impulse2:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{reduced:function(){return(new Box2D.Common.Math.b2Vec2());}},
"b2internal override function SolveVelocityConstraints",function(step){
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var tMat;
var tX;
var newImpulse;
var r1X;
var r1Y;
var r2X;
var r2Y;
var v1=bA.m_linearVelocity;
var w1=bA.m_angularVelocity;
var v2=bB.m_linearVelocity;
var w2=bB.m_angularVelocity;
var m1=bA.m_invMass;
var m2=bB.m_invMass;
var i1=bA.m_invI;
var i2=bB.m_invI;
if(this.m_enableMotor$2&&this.m_limitState$2!=Box2D.Dynamics.Joints.b2Joint.e_equalLimits)
{
var Cdot=w2-w1-this.m_motorSpeed$2;
var impulse=this.m_motorMass$2*(-Cdot);
var oldImpulse=this.m_motorImpulse$2;
var maxImpulse=step.dt*this.m_maxMotorTorque$2;
this.m_motorImpulse$2=Box2D.Common.Math.b2Math.Clamp(this.m_motorImpulse$2+impulse,-maxImpulse,maxImpulse);
impulse=this.m_motorImpulse$2-oldImpulse;
w1-=i1*impulse;
w2+=i2*impulse;
}
if(this.m_enableLimit$2&&this.m_limitState$2!=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit)
{
tMat=bA.m_xf.R;
r1X=this.m_localAnchor1.x-bA.m_sweep.localCenter.x;
r1Y=this.m_localAnchor1.y-bA.m_sweep.localCenter.y;
tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=bB.m_xf.R;
r2X=this.m_localAnchor2.x-bB.m_sweep.localCenter.x;
r2Y=this.m_localAnchor2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
var Cdot1X=v2.x+(-w2*r2Y)-v1.x-(-w1*r1Y);
var Cdot1Y=v2.y+(w2*r2X)-v1.y-(w1*r1X);
var Cdot2=w2-w1;
this.m_mass$2.Solve33(this.impulse3$2,-Cdot1X,-Cdot1Y,-Cdot2);
if(this.m_limitState$2==Box2D.Dynamics.Joints.b2Joint.e_equalLimits)
{
this.m_impulse$2.Add(this.impulse3$2);
}
else if(this.m_limitState$2==Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit)
{
newImpulse=this.m_impulse$2.z+this.impulse3$2.z;
if(newImpulse<0.0)
{
this.m_mass$2.Solve22(this.reduced$2,-Cdot1X,-Cdot1Y);
this.impulse3$2.x=this.reduced$2.x;
this.impulse3$2.y=this.reduced$2.y;
this.impulse3$2.z=-this.m_impulse$2.z;
this.m_impulse$2.x+=this.reduced$2.x;
this.m_impulse$2.y+=this.reduced$2.y;
this.m_impulse$2.z=0.0;
}
}
else if(this.m_limitState$2==Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
{
newImpulse=this.m_impulse$2.z+this.impulse3$2.z;
if(newImpulse>0.0)
{
this.m_mass$2.Solve22(this.reduced$2,-Cdot1X,-Cdot1Y);
this.impulse3$2.x=this.reduced$2.x;
this.impulse3$2.y=this.reduced$2.y;
this.impulse3$2.z=-this.m_impulse$2.z;
this.m_impulse$2.x+=this.reduced$2.x;
this.m_impulse$2.y+=this.reduced$2.y;
this.m_impulse$2.z=0.0;
}
}
v1.x-=m1*this.impulse3$2.x;
v1.y-=m1*this.impulse3$2.y;
w1-=i1*(r1X*this.impulse3$2.y-r1Y*this.impulse3$2.x+this.impulse3$2.z);
v2.x+=m2*this.impulse3$2.x;
v2.y+=m2*this.impulse3$2.y;
w2+=i2*(r2X*this.impulse3$2.y-r2Y*this.impulse3$2.x+this.impulse3$2.z);
}
else
{
tMat=bA.m_xf.R;
r1X=this.m_localAnchor1.x-bA.m_sweep.localCenter.x;
r1Y=this.m_localAnchor1.y-bA.m_sweep.localCenter.y;
tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=bB.m_xf.R;
r2X=this.m_localAnchor2.x-bB.m_sweep.localCenter.x;
r2Y=this.m_localAnchor2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
var CdotX=v2.x+(-w2*r2Y)-v1.x-(-w1*r1Y);
var CdotY=v2.y+(w2*r2X)-v1.y-(w1*r1X);
this.m_mass$2.Solve22(this.impulse2$2,-CdotX,-CdotY);
this.m_impulse$2.x+=this.impulse2$2.x;
this.m_impulse$2.y+=this.impulse2$2.y;
v1.x-=m1*this.impulse2$2.x;
v1.y-=m1*this.impulse2$2.y;
w1-=i1*(r1X*this.impulse2$2.y-r1Y*this.impulse2$2.x);
v2.x+=m2*this.impulse2$2.x;
v2.y+=m2*this.impulse2$2.y;
w2+=i2*(r2X*this.impulse2$2.y-r2Y*this.impulse2$2.x);
}
bA.m_linearVelocity.SetV(v1);
bA.m_angularVelocity=w1;
bB.m_linearVelocity.SetV(v2);
bB.m_angularVelocity=w2;
},
"private static var",{tImpulse:function(){return(new Box2D.Common.Math.b2Vec2());}},
"b2internal override function SolvePositionConstraints",function(baumgarte){
var oldLimitImpulse;
var C;
var tMat;
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var angularError=0.0;
var positionError=0.0;
var tX;
var impulseX;
var impulseY;
if(this.m_enableLimit$2&&this.m_limitState$2!=Box2D.Dynamics.Joints.b2Joint.e_inactiveLimit)
{
var angle=bB.m_sweep.a-bA.m_sweep.a-this.m_referenceAngle$2;
var limitImpulse=0.0;
if(this.m_limitState$2==Box2D.Dynamics.Joints.b2Joint.e_equalLimits)
{
C=Box2D.Common.Math.b2Math.Clamp(angle-this.m_lowerAngle$2,-Box2D.Common.b2Settings.b2_maxAngularCorrection,Box2D.Common.b2Settings.b2_maxAngularCorrection);
limitImpulse=-this.m_motorMass$2*C;
angularError=Box2D.Common.Math.b2Math.Abs(C);
}
else if(this.m_limitState$2==Box2D.Dynamics.Joints.b2Joint.e_atLowerLimit)
{
C=angle-this.m_lowerAngle$2;
angularError=-C;
C=Box2D.Common.Math.b2Math.Clamp(C+Box2D.Common.b2Settings.b2_angularSlop,-Box2D.Common.b2Settings.b2_maxAngularCorrection,0.0);
limitImpulse=-this.m_motorMass$2*C;
}
else if(this.m_limitState$2==Box2D.Dynamics.Joints.b2Joint.e_atUpperLimit)
{
C=angle-this.m_upperAngle$2;
angularError=C;
C=Box2D.Common.Math.b2Math.Clamp(C-Box2D.Common.b2Settings.b2_angularSlop,0.0,Box2D.Common.b2Settings.b2_maxAngularCorrection);
limitImpulse=-this.m_motorMass$2*C;
}
bA.m_sweep.a-=bA.m_invI*limitImpulse;
bB.m_sweep.a+=bB.m_invI*limitImpulse;
bA.SynchronizeTransform();
bB.SynchronizeTransform();
}
{
tMat=bA.m_xf.R;
var r1X=this.m_localAnchor1.x-bA.m_sweep.localCenter.x;
var r1Y=this.m_localAnchor1.y-bA.m_sweep.localCenter.y;
tX=(tMat.col1.x*r1X+tMat.col2.x*r1Y);
r1Y=(tMat.col1.y*r1X+tMat.col2.y*r1Y);
r1X=tX;
tMat=bB.m_xf.R;
var r2X=this.m_localAnchor2.x-bB.m_sweep.localCenter.x;
var r2Y=this.m_localAnchor2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*r2X+tMat.col2.x*r2Y);
r2Y=(tMat.col1.y*r2X+tMat.col2.y*r2Y);
r2X=tX;
var CX=bB.m_sweep.c.x+r2X-bA.m_sweep.c.x-r1X;
var CY=bB.m_sweep.c.y+r2Y-bA.m_sweep.c.y-r1Y;
var CLengthSquared=CX*CX+CY*CY;
var CLength=Math.sqrt(CLengthSquared);
positionError=CLength;
var invMass1=bA.m_invMass;
var invMass2=bB.m_invMass;
var invI1=bA.m_invI;
var invI2=bB.m_invI;var k_allowedStretch=10.0*Box2D.Common.b2Settings.b2_linearSlop;
if(CLengthSquared>k_allowedStretch*k_allowedStretch)
{
var uX=CX/CLength;
var uY=CY/CLength;
var k=invMass1+invMass2;
var m=1.0/k;
impulseX=m*(-CX);
impulseY=m*(-CY);var k_beta=0.5;
bA.m_sweep.c.x-=k_beta*invMass1*impulseX;
bA.m_sweep.c.y-=k_beta*invMass1*impulseY;
bB.m_sweep.c.x+=k_beta*invMass2*impulseX;
bB.m_sweep.c.y+=k_beta*invMass2*impulseY;
CX=bB.m_sweep.c.x+r2X-bA.m_sweep.c.x-r1X;
CY=bB.m_sweep.c.y+r2Y-bA.m_sweep.c.y-r1Y;
}
this.K1$2.col1.x=invMass1+invMass2;this.K1$2.col2.x=0.0;
this.K1$2.col1.y=0.0;this.K1$2.col2.y=invMass1+invMass2;
this.K2$2.col1.x=invI1*r1Y*r1Y;this.K2$2.col2.x=-invI1*r1X*r1Y;
this.K2$2.col1.y=-invI1*r1X*r1Y;this.K2$2.col2.y=invI1*r1X*r1X;
this.K3$2.col1.x=invI2*r2Y*r2Y;this.K3$2.col2.x=-invI2*r2X*r2Y;
this.K3$2.col1.y=-invI2*r2X*r2Y;this.K3$2.col2.y=invI2*r2X*r2X;
this.K$2.SetM(this.K1$2);
this.K$2.AddM(this.K2$2);
this.K$2.AddM(this.K3$2);
this.K$2.Solve($$private.tImpulse,-CX,-CY);
impulseX=$$private.tImpulse.x;
impulseY=$$private.tImpulse.y;
bA.m_sweep.c.x-=bA.m_invMass*impulseX;
bA.m_sweep.c.y-=bA.m_invMass*impulseY;
bA.m_sweep.a-=bA.m_invI*(r1X*impulseY-r1Y*impulseX);
bB.m_sweep.c.x+=bB.m_invMass*impulseX;
bB.m_sweep.c.y+=bB.m_invMass*impulseY;
bB.m_sweep.a+=bB.m_invI*(r2X*impulseY-r2Y*impulseX);
bA.SynchronizeTransform();
bB.SynchronizeTransform();
}
return positionError<=Box2D.Common.b2Settings.b2_linearSlop&&angularError<=Box2D.Common.b2Settings.b2_angularSlop;
},
"b2internal var",{m_localAnchor1:function(){return(new Box2D.Common.Math.b2Vec2());}},
"b2internal var",{m_localAnchor2:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_impulse:function(){return(new Box2D.Common.Math.b2Vec3());}},
"private var",{m_motorImpulse:NaN},
"private var",{m_mass:function(){return(new Box2D.Common.Math.b2Mat33());}},
"private var",{m_motorMass:NaN},
"private var",{m_enableMotor:false},
"private var",{m_maxMotorTorque:NaN},
"private var",{m_motorSpeed:NaN},
"private var",{m_enableLimit:false},
"private var",{m_referenceAngle:NaN},
"private var",{m_lowerAngle:NaN},
"private var",{m_upperAngle:NaN},
"private var",{m_limitState:0},
];},[],["Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Mat22","Box2D.Common.Math.b2Math","Box2D.Common.b2Settings","Box2D.Common.Math.b2Vec3","Math","Box2D.Common.Math.b2Mat33"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2RevoluteJointDef
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2RevoluteJointDef extends Box2D.Dynamics.Joints.b2JointDef",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint);},

"public function b2RevoluteJointDef",function()
{this.super$2();this.localAnchorA=this.localAnchorA();this.localAnchorB=this.localAnchorB();
this.type=Box2D.Dynamics.Joints.b2Joint.e_revoluteJoint;
this.localAnchorA.Set(0.0,0.0);
this.localAnchorB.Set(0.0,0.0);
this.referenceAngle=0.0;
this.lowerAngle=0.0;
this.upperAngle=0.0;
this.maxMotorTorque=0.0;
this.motorSpeed=0.0;
this.enableLimit=false;
this.enableMotor=false;
},
"public function Initialize",function(bA,bB,anchor){
this.bodyA=bA;
this.bodyB=bB;
this.localAnchorA=this.bodyA.GetLocalPoint(anchor);
this.localAnchorB=this.bodyB.GetLocalPoint(anchor);
this.referenceAngle=this.bodyB.GetAngle()-this.bodyA.GetAngle();
},
"public var",{localAnchorA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{localAnchorB:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{referenceAngle:NaN},
"public var",{enableLimit:false},
"public var",{lowerAngle:NaN},
"public var",{upperAngle:NaN},
"public var",{enableMotor:false},
"public var",{motorSpeed:NaN},
"public var",{maxMotorTorque:NaN},
];},[],["Box2D.Dynamics.Joints.b2JointDef","Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2WeldJoint
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2WeldJoint extends Box2D.Dynamics.Joints.b2Joint",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Common.b2Settings);},

"public override function GetAnchorA",function(){
return this.m_bodyA.GetWorldPoint(this.m_localAnchorA$2);
},
"public override function GetAnchorB",function(){
return this.m_bodyB.GetWorldPoint(this.m_localAnchorB$2);
},
"public override function GetReactionForce",function(inv_dt)
{
return new Box2D.Common.Math.b2Vec2(inv_dt*this.m_impulse$2.x,inv_dt*this.m_impulse$2.y);
},
"public override function GetReactionTorque",function(inv_dt)
{
return inv_dt*this.m_impulse$2.z;
},
"public function b2WeldJoint",function(def){
this.super$2(def);this.m_localAnchorA$2=this.m_localAnchorA$2();this.m_localAnchorB$2=this.m_localAnchorB$2();this.m_impulse$2=this.m_impulse$2();this.m_mass$2=this.m_mass$2();
this.m_localAnchorA$2.SetV(def.localAnchorA);
this.m_localAnchorB$2.SetV(def.localAnchorB);
this.m_referenceAngle$2=def.referenceAngle;
this.m_impulse$2.SetZero();
this.m_mass$2=new Box2D.Common.Math.b2Mat33();
},
"b2internal override function InitVelocityConstraints",function(step){
var tMat;
var tX;
var bA=this.m_bodyA;
var bB=this.m_bodyB;
tMat=bA.m_xf.R;
var rAX=this.m_localAnchorA$2.x-bA.m_sweep.localCenter.x;
var rAY=this.m_localAnchorA$2.y-bA.m_sweep.localCenter.y;
tX=(tMat.col1.x*rAX+tMat.col2.x*rAY);
rAY=(tMat.col1.y*rAX+tMat.col2.y*rAY);
rAX=tX;
tMat=bB.m_xf.R;
var rBX=this.m_localAnchorB$2.x-bB.m_sweep.localCenter.x;
var rBY=this.m_localAnchorB$2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*rBX+tMat.col2.x*rBY);
rBY=(tMat.col1.y*rBX+tMat.col2.y*rBY);
rBX=tX;
var mA=bA.m_invMass;
var mB=bB.m_invMass;
var iA=bA.m_invI;
var iB=bB.m_invI;
this.m_mass$2.col1.x=mA+mB+rAY*rAY*iA+rBY*rBY*iB;
this.m_mass$2.col2.x=-rAY*rAX*iA-rBY*rBX*iB;
this.m_mass$2.col3.x=-rAY*iA-rBY*iB;
this.m_mass$2.col1.y=this.m_mass$2.col2.x;
this.m_mass$2.col2.y=mA+mB+rAX*rAX*iA+rBX*rBX*iB;
this.m_mass$2.col3.y=rAX*iA+rBX*iB;
this.m_mass$2.col1.z=this.m_mass$2.col3.x;
this.m_mass$2.col2.z=this.m_mass$2.col3.y;
this.m_mass$2.col3.z=iA+iB;
if(step.warmStarting)
{
this.m_impulse$2.x*=step.dtRatio;
this.m_impulse$2.y*=step.dtRatio;
this.m_impulse$2.z*=step.dtRatio;
bA.m_linearVelocity.x-=mA*this.m_impulse$2.x;
bA.m_linearVelocity.y-=mA*this.m_impulse$2.y;
bA.m_angularVelocity-=iA*(rAX*this.m_impulse$2.y-rAY*this.m_impulse$2.x+this.m_impulse$2.z);
bB.m_linearVelocity.x+=mB*this.m_impulse$2.x;
bB.m_linearVelocity.y+=mB*this.m_impulse$2.y;
bB.m_angularVelocity+=iB*(rBX*this.m_impulse$2.y-rBY*this.m_impulse$2.x+this.m_impulse$2.z);
}
else
{
this.m_impulse$2.SetZero();
}
},
"b2internal override function SolveVelocityConstraints",function(step){
var tMat;
var tX;
var bA=this.m_bodyA;
var bB=this.m_bodyB;
var vA=bA.m_linearVelocity;
var wA=bA.m_angularVelocity;
var vB=bB.m_linearVelocity;
var wB=bB.m_angularVelocity;
var mA=bA.m_invMass;
var mB=bB.m_invMass;
var iA=bA.m_invI;
var iB=bB.m_invI;
tMat=bA.m_xf.R;
var rAX=this.m_localAnchorA$2.x-bA.m_sweep.localCenter.x;
var rAY=this.m_localAnchorA$2.y-bA.m_sweep.localCenter.y;
tX=(tMat.col1.x*rAX+tMat.col2.x*rAY);
rAY=(tMat.col1.y*rAX+tMat.col2.y*rAY);
rAX=tX;
tMat=bB.m_xf.R;
var rBX=this.m_localAnchorB$2.x-bB.m_sweep.localCenter.x;
var rBY=this.m_localAnchorB$2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*rBX+tMat.col2.x*rBY);
rBY=(tMat.col1.y*rBX+tMat.col2.y*rBY);
rBX=tX;
var Cdot1X=vB.x-wB*rBY-vA.x+wA*rAY;
var Cdot1Y=vB.y+wB*rBX-vA.y-wA*rAX;
var Cdot2=wB-wA;
var impulse=new Box2D.Common.Math.b2Vec3();
this.m_mass$2.Solve33(impulse,-Cdot1X,-Cdot1Y,-Cdot2);
this.m_impulse$2.Add(impulse);
vA.x-=mA*impulse.x;
vA.y-=mA*impulse.y;
wA-=iA*(rAX*impulse.y-rAY*impulse.x+impulse.z);
vB.x+=mB*impulse.x;
vB.y+=mB*impulse.y;
wB+=iB*(rBX*impulse.y-rBY*impulse.x+impulse.z);
bA.m_angularVelocity=wA;
bB.m_angularVelocity=wB;
},
"b2internal override function SolvePositionConstraints",function(baumgarte)
{
var tMat;
var tX;
var bA=this.m_bodyA;
var bB=this.m_bodyB;
tMat=bA.m_xf.R;
var rAX=this.m_localAnchorA$2.x-bA.m_sweep.localCenter.x;
var rAY=this.m_localAnchorA$2.y-bA.m_sweep.localCenter.y;
tX=(tMat.col1.x*rAX+tMat.col2.x*rAY);
rAY=(tMat.col1.y*rAX+tMat.col2.y*rAY);
rAX=tX;
tMat=bB.m_xf.R;
var rBX=this.m_localAnchorB$2.x-bB.m_sweep.localCenter.x;
var rBY=this.m_localAnchorB$2.y-bB.m_sweep.localCenter.y;
tX=(tMat.col1.x*rBX+tMat.col2.x*rBY);
rBY=(tMat.col1.y*rBX+tMat.col2.y*rBY);
rBX=tX;
var mA=bA.m_invMass;
var mB=bB.m_invMass;
var iA=bA.m_invI;
var iB=bB.m_invI;
var C1X=bB.m_sweep.c.x+rBX-bA.m_sweep.c.x-rAX;
var C1Y=bB.m_sweep.c.y+rBY-bA.m_sweep.c.y-rAY;
var C2=bB.m_sweep.a-bA.m_sweep.a-this.m_referenceAngle$2;
var k_allowedStretch=10.0*Box2D.Common.b2Settings.b2_linearSlop;
var positionError=Math.sqrt(C1X*C1X+C1Y*C1Y);
var angularError=Box2D.Common.Math.b2Math.Abs(C2);
if(positionError>k_allowedStretch)
{
iA*=1.0;
iB*=1.0;
}
this.m_mass$2.col1.x=mA+mB+rAY*rAY*iA+rBY*rBY*iB;
this.m_mass$2.col2.x=-rAY*rAX*iA-rBY*rBX*iB;
this.m_mass$2.col3.x=-rAY*iA-rBY*iB;
this.m_mass$2.col1.y=this.m_mass$2.col2.x;
this.m_mass$2.col2.y=mA+mB+rAX*rAX*iA+rBX*rBX*iB;
this.m_mass$2.col3.y=rAX*iA+rBX*iB;
this.m_mass$2.col1.z=this.m_mass$2.col3.x;
this.m_mass$2.col2.z=this.m_mass$2.col3.y;
this.m_mass$2.col3.z=iA+iB;
var impulse=new Box2D.Common.Math.b2Vec3();
this.m_mass$2.Solve33(impulse,-C1X,-C1Y,-C2);
bA.m_sweep.c.x-=mA*impulse.x;
bA.m_sweep.c.y-=mA*impulse.y;
bA.m_sweep.a-=iA*(rAX*impulse.y-rAY*impulse.x+impulse.z);
bB.m_sweep.c.x+=mB*impulse.x;
bB.m_sweep.c.y+=mB*impulse.y;
bB.m_sweep.a+=iB*(rBX*impulse.y-rBY*impulse.x+impulse.z);
bA.SynchronizeTransform();
bB.SynchronizeTransform();
return positionError<=Box2D.Common.b2Settings.b2_linearSlop&&angularError<=Box2D.Common.b2Settings.b2_angularSlop;
},
"private var",{m_localAnchorA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_localAnchorB:function(){return(new Box2D.Common.Math.b2Vec2());}},
"private var",{m_referenceAngle:NaN},
"private var",{m_impulse:function(){return(new Box2D.Common.Math.b2Vec3());}},
"private var",{m_mass:function(){return(new Box2D.Common.Math.b2Mat33());}},
];},[],["Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2","Box2D.Common.Math.b2Mat33","Box2D.Common.Math.b2Vec3","Box2D.Common.b2Settings","Math","Box2D.Common.Math.b2Math"], "0.8.0", "0.8.1"
);
// class Box2D.Dynamics.Joints.b2WeldJointDef
joo.classLoader.prepare(
"package Box2D.Dynamics.Joints",
"public class b2WeldJointDef extends Box2D.Dynamics.Joints.b2JointDef",2,function($$private){;return[function(){joo.classLoader.init(Box2D.Dynamics.Joints.b2Joint);},

"public function b2WeldJointDef",function()
{this.super$2();this.localAnchorA=this.localAnchorA();this.localAnchorB=this.localAnchorB();
this.type=Box2D.Dynamics.Joints.b2Joint.e_weldJoint;
this.referenceAngle=0.0;
},
"public function Initialize",function(bA,bB,
anchor)
{
this.bodyA=bA;
this.bodyB=bB;
this.localAnchorA.SetV(this.bodyA.GetLocalPoint(anchor));
this.localAnchorB.SetV(this.bodyB.GetLocalPoint(anchor));
this.referenceAngle=this.bodyB.GetAngle()-this.bodyA.GetAngle();
},
"public var",{localAnchorA:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{localAnchorB:function(){return(new Box2D.Common.Math.b2Vec2());}},
"public var",{referenceAngle:NaN},
];},[],["Box2D.Dynamics.Joints.b2JointDef","Box2D.Dynamics.Joints.b2Joint","Box2D.Common.Math.b2Vec2"], "0.8.0", "0.8.1"
);
