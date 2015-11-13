package acceleration;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import math.Point;
import math.Ray;
import math.Vector;
import shape.BoundingBox;
import shape.Shape;
import Lighting.Light;

public class Compound implements Shape{

	ArrayList<Shape> shapeList = new ArrayList<Shape>();

	public Compound(){

	}

	public void addObject(Shape shape){
		shapeList.add(shape);
	}

	@Override
	public double intersectionT(Ray ray) {
		double t=Double.MAX_VALUE;
		for(Shape shape:shapeList){
			double temp = shape.intersectionT(ray);
			if(temp<t && temp!=-1)
				t=temp;
		}
		return t;
	}

	@Override
	public Color shade(Point point, List<Light> lights, Ray ray) {
		return null;
	}

	@Override
	public World getWorld() {
		return null;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return null;
	}

	@Override
	public double getCost() {
		return 0;
	}

	@Override
	public Vector getNormalAt(Point point, Ray ray) {
		return null;
	}

}
