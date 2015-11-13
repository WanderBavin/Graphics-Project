package shape;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;
import shading.Material;
import Lighting.Light;
import acceleration.World;


public class Cylinder implements Shape {
	public Transformation transformation;
	public final double radius;
	public Material mat;
	public World world;

	public Cylinder(Transformation transformation, double radius,Material mat,World world) {

		this.transformation = transformation;
		this.radius = radius;
		this.mat=mat;
		this.world = world;
		world.add(this);
	}

	@Override
	public double intersectionT(Ray ray) {
		double eps = 0.000001;
		Ray transformed = transformation.transformInverse(ray);



		double a = Math.pow(transformed.direction.x,2)+Math.pow(transformed.direction.z,2);
		double b = 2.0 * (transformed.origin.x*transformed.direction.x + transformed.origin.z*transformed.direction.z);
		double c = transformed.origin.x*transformed.origin.x + transformed.origin.z*transformed.origin.z - radius * radius;

		double d = b * b - 4.0 * a * c;

		if (d < 0)
			return -1;
		double dr = Math.sqrt(d);
		double q = b < 0 ? -0.5 * (b - dr) : -0.5 * (b + dr);

		double t0 = q / a;
		double t1 = c / q;

		double p1 = transformed.origin.add(transformed.direction.scale(t1)).y;
		double p0 = transformed.origin.add(transformed.direction.scale(t0)).y;

		if(t0 >= eps || t1 >= eps){
			if(t0<0){
				if(p1< -1 || p1>1)
					return -1;
				ray.checkT(t1, this);
				return t1;
			}if(t1<0){
				if(p0<-1 || p0>1)
					return -1;
				ray.checkT(t0, this);
				return t0;
			}
			if((p0<-1 || p0>1) && (p1<-1 || p1>1))
				return -1;
			if(transformed.origin.add(transformed.direction.scale(Math.min(t0, t1))).y <-1 || transformed.origin.add(transformed.direction.scale(Math.min(t0, t1))).y >1){
				ray.checkT(Math.max(t0, t1), this);
				return Math.max(t0, t1);
			}
			ray.checkT(Math.min(t0, t1), this);
			return Math.min(t0, t1);
		}
		return -1;
	}

	@Override
	public Vector getNormalAt(Point point1,Ray ray){
		Point point = transformation.getInverseTransformationMatrix().transform(point1);
		Vector normal0 = new Vector(point.x,0,point.z);
		Vector nor = transformation.getInverseTransformationMatrix().transpose().transform(normal0);
		if(ray.direction.scale(-1).dot(nor)<=0){
			return nor.scale(-1);
		}
		return nor;
	}

	@Override
	public Color shade(Point point,List<Light> lights,Ray ray) {

		return mat.shade(this, point, lights,ray);
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
	public World getWorld() {
		return this.world;
	}

	@Override
	public BoundingBox getBoundingBox(){
		Point min = transformation.transform(new Point(-radius,-1,-radius));
		Point max = transformation.transform(new Point(radius,1,radius));
		ArrayList<Point> points = new ArrayList<Point>();
		points.add( transformation.transform(new Point(-radius,-1,-radius)));
		points.add( transformation.transform(new Point(radius,1,radius)));
		points.add(  transformation.transform(new Point(-radius,-1,radius)));
		points.add(  transformation.transform(new Point(-radius,1,-radius)));
		points.add(  transformation.transform(new Point(radius,-1,-radius)));
		points.add(  transformation.transform(new Point(radius,1,-radius)));
		points.add(  transformation.transform(new Point(-radius,1,radius)));
		points.add(  transformation.transform(new Point(radius,-1,radius)));

		double minX = min.x;
		double minY = min.y;
		double minZ = min.z;
		double maxX = max.x;
		double maxY = max.y;
		double maxZ = max.z;

		for(Point p:points){
			if (p.x < minX ) minX = p.x;
			if ( p.y < minY ) minY =p.y;
			if ( p.z < minZ ) minZ = p.z;
			if (p.x > maxX ) maxX = p.x;
			if (p.y > maxY ) maxY = p.y;
			if (p.z > maxZ ) maxZ = p.z;
		}

		return new BoundingBox(new Point(minX,minY,minZ),new Point(maxX,maxY,maxZ),this);
	}

	@Override
	public double getCost() {
		return 1;
	}

}
