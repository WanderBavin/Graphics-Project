package shape;

import java.awt.Color;
import java.util.List;

import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;
import shading.Material;
import Lighting.Light;
import acceleration.World;

public class Triangle implements Shape{

	public Transformation transformation;

	public Material mat;
	public World world;

	Point v0;
	Point v1;
	Point v2;

	public Triangle(Point a,Point b,Point c,Material mat,World world){
		//this.transformation=t;
		this.mat=mat;
		this.world=world;
		world.add(this);
		v0=a;
		v1=b;
		v2=c;

	}
	public Triangle(Transformation trans,Material mat,World world){
		//this.transformation=t;
		this.mat=mat;
		this.world=world;
		world.add(this);
		v0=trans.transform(new Point(0,0,0));
		v1=trans.transform(new Point(1,0,0));
		v2=trans.transform(new Point(0,1,0));

	}

	@Override
	public Vector getNormalAt(Point point,Ray ray) {
		Vector vec1 = v0.subtract(point);
		Vector vec2 = v1.subtract(point);
		Vector nor = vec1.cross(vec2);
		if(ray.direction.scale(-1).dot(nor)<=0){
			return nor.scale(-1);
		}
		return nor;
	}

	@Override
	public double intersectionT(Ray ray) {
		double a = v0.x-v1.x, b = v0.x-v2.x, c = ray.direction.x, d = v0.x - ray.origin.x;
		double e = v0.y - v1.y, f = v0.y-v2.y, g = ray.direction.y, h = v0.y - ray.origin.y;
		double i = v0.z - v1.z, j = v0.z-v2.z, k = ray.direction.z, l = v0.z - ray.origin.z;

		double m = f*k - g*j, n = h*k - g*l, p = f*l - h*j;
		double q = g*i - e*k, s =e*j - f*i;

		double invDenom = 1/(a*m+b*q+c*s);

		double e1 = d*m -b*n -c*p;

		double beta = e1*invDenom;

		if(beta<0)
			return -1;

		double r = e*l - h*i;
		double e2 = a*n + d*q +c*r;
		double gamma = e2*invDenom;

		if(gamma<0)
			return -1;

		if(beta+gamma >1)
			return -1;

		double e3 = a*p - b*r +d*s;
		double t = e3*invDenom;

		if(t<0.000001)
			return -1;
		ray.checkT(t, this);
		return t;
	}

	@Override
	public Color shade(Point point, List<Light> lights, Ray ray) {

		return mat.shade(this, point, lights, ray);
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	public Color falseColorImage(Point point,Ray ray){
		Vector nor = getNormalAt(point,ray);
		float r = Math.abs((float) nor.x);
		float g = Math.abs((float) nor.y);
		float b = Math.abs((float) nor.z);

		return clampColors(r,g,b);
	}

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

	@Override
	public BoundingBox getBoundingBox(){
		int n = 3;

		double minX = v0.x;
		double minY = v0.y;
		double minZ = v0.z;
		double maxX = v0.x;
		double maxY = v0.y;
		double maxZ = v0.z;
		Point[] V = {v0,v1,v2};
		for (int i = 1; i < n; ++i)
		{
			if ( V[i].x < minX ) minX = V[i].x;
			if ( V[i].y < minY ) minY = V[i].y;
			if ( V[i].z < minZ ) minZ = V[i].z;
			if ( V[i].x > maxX ) maxX = V[i].x;
			if ( V[i].y > maxY ) maxY = V[i].y;
			if ( V[i].z > maxZ ) maxZ = V[i].z;
		}

		return new BoundingBox(new Point(minX,minY,minZ),new Point(maxX,maxY,maxZ),this);
	}

	@Override
	public double getCost() {
		return 1;
	}


}
