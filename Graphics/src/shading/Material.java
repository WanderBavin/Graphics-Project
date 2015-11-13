package shading;

import java.awt.Color;
import java.util.List;

import math.Point;
import math.Ray;
import math.Vector;
import shape.BoundingBox;
import shape.EmissiveRectangle;
import shape.Shape;
import Lighting.Light;

public abstract class Material {

	protected double ka;

	protected Color ca;

	public Material(double ka,Color ca){
		this.ka = ka;
		this.ca=ca;
	}

	public abstract Color shade(Shape shape,Point point,List<Light> lights,Ray ray);
	public abstract Color shade(Shape shape,Point point,List<Light> lights,Ray ray,Color col);

	protected Color clampColors(float r, float g, float b){
		r=Math.abs(r);
		g=Math.abs(g);
		b=Math.abs(b);
		float max = Math.max(Math.max(r,g), b);
		if(max>1){
			return new Color(r/max,g/max,b/max);
		}
		return new Color(r,g,b);
	}

	protected boolean inShadow(Point p1,Point p2,Ray shadowRay,Shape obj, double t){
		Point hitPoint = shadowRay.origin.add(shadowRay.direction.scale(t));

		double xComponent = Math.pow(p2.x-p1.x, 2);
		double yComponent = Math.pow(p2.y-p1.y, 2);
		double zComponent = Math.pow(p2.z-p1.z, 2);
		double distance1 = Math.sqrt(xComponent+yComponent+zComponent);
		double xComponent2 = Math.pow(hitPoint.x-p1.x, 2);
		double yComponent2 = Math.pow(hitPoint.y-p1.y, 2);
		double zComponent2 = Math.pow(hitPoint.z-p1.z, 2);
		double distance2 = Math.sqrt(xComponent2+yComponent2+zComponent2);
		double eps = 0.00000;
		if(obj instanceof EmissiveRectangle || shadowRay.hitShape instanceof EmissiveRectangle){
			if(obj instanceof BoundingBox)
				obj = shadowRay.hitShape;
			Vector nor = ((EmissiveRectangle) obj).getHardcodedNormal();
			if(nor.dot(shadowRay.direction.scale(-1)) >0 && distance1-distance2==0)
				return false;
		}
		return distance1+eps-distance2>=0;
	}
}
