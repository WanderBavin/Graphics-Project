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

public class Disk implements Shape{

	public final double radius;
	public Transformation transformation;
	public Material mat;
	public World world;

	public Disk(Transformation transformation, double radius,Material mat,World world) {

		this.transformation = transformation;
		this.radius = radius;
		this.mat=mat;
		this.world = world;
		world.add(this);
	}

	@Override
	public Vector getNormalAt(Point point1, Ray ray) {

		Vector normal0 = new Vector(0,radius,0);
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
		Vector normal = new Vector(0,1,0);
		if(normal.dot(transformed.direction.scale(-1))<=0)
			normal = normal.scale(-1);

		float t = (float) ((center.subtract(transformed.origin)).dot(normal)/(transformed.direction.dot(normal)));
		if(t<=eps)
			return -1;

		Point hitPoint = transformed.origin.add(transformed.direction.scale(t));

		if(hitPoint.subtract(center).lengthSquared()<Math.pow(radius,2)){
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

	@Override
	public BoundingBox getBoundingBox(){
		Point min = transformation.transform(new Point(-radius,0,-radius));
		Point max = transformation.transform(new Point(radius,0,radius));
		ArrayList<Point> points = new ArrayList<Point>();
		points.add( transformation.transform(new Point(-radius,0,-radius)));
		points.add( transformation.transform(new Point(-radius,0,radius)));
		points.add( transformation.transform(new Point(radius,0,-radius)));
		points.add( transformation.transform(new Point(radius,0,radius)));


		double minX = min.x;
		double minY = min.y;
		double minZ = min.z;
		double maxX = max.x;
		double maxY = max.y;
		double maxZ = max.z;

		for(Point p:points){
			if (p.x < minX ) minX = p.x;
			if (p.y < minY ) minY = p.y;
			if (p.z < minZ ) minZ = p.z;
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
