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

public class Plane implements Shape{

	public Transformation transformation;

	public Material mat;
	public World world;


	public Plane(Transformation t,Material mat,World world){
		this.transformation=t;
		this.mat=mat;
		this.world=world;
		world.add(this);


	}

	@Override
	public Vector getNormalAt(Point point,Ray ray) {

		Vector normal0 = new Vector(0,0,-1);
		Vector returnVec = transformation.getInverseTransformationMatrix().transpose().transform(normal0);
		if(returnVec.dot(ray.direction.scale(-1))<=0)
			return returnVec.scale(-1);
		return returnVec;
	}


	@Override
	public double intersectionT(Ray ray) {
		double eps = 0.000001;
		Ray transformed = transformation.transformInverse(ray);
		Point center = new Point(0,0,0);
		//Vector normal = getNormalAt(center,transformed);

		Vector normal = new Vector(0,0,-1);
		if(normal.dot(transformed.direction.scale(-1))<=0)
			normal = normal.scale(-1);

		float t = (float) ((center.subtract(transformed.origin)).dot(normal)/(transformed.direction.dot(normal)));
		if(t>eps){
			ray.checkT(t, this);
			return t;
		}
		return -1;
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
	public BoundingBox getBoundingBox() {
		return null;
	}


	@Override
	public double getCost() {
		return 1;
	}
}
