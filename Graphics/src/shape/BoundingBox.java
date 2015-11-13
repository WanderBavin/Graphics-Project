package shape;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import math.Point;
import math.Ray;
import math.Vector;
import Lighting.Light;
import acceleration.Grid;
import acceleration.World;

public class BoundingBox implements Comparable<BoundingBox>,Shape{

	public int partitionPoint=2;
	public Point min;
	public Point max;
	public World world;
	public Shape shape;
	public ArrayList<BoundingBox> boxes;
	public boolean gridBox;
	public Grid grid;

	public BoundingBox(Point min,Point max,Shape shape){
		this.min = min;
		this.max = max;
		this.world=shape.getWorld();
		this.shape = shape;
	}

	public BoundingBox(Point min,Point max,World world,ArrayList<BoundingBox> boxes){
		this.min = min;
		this.max = max;
		this.world=world;
		this.boxes = boxes;
	}

	public BoundingBox(Point min,Point max,World world){
		this.min = min;
		this.max = max;
		this.world=world;
		this.boxes = new ArrayList<BoundingBox>();
	}

	public void setAsGrid(Grid grid){
		this.grid=grid;
		this.gridBox=true;
	}

	@Override
	public double intersectionT(Ray ray) {
		ray.intersectCount++;

		if(gridBox)
			return grid.intersectionT(ray);

		double txmin, txmax, tymin, tymax, tzmin, tzmax;
		if(tMin==Double.MAX_VALUE && tMax ==Double.MAX_VALUE){
			if(ray.direction.x >= 0){
				txmin = (min.x - ray.origin.x) / ray.direction.x;
				txmax = (max.x - ray.origin.x) / ray.direction.x;
			}else{
				txmin = (max.x - ray.origin.x) / ray.direction.x;
				txmax = (min.x - ray.origin.x) / ray.direction.x;
			}
			if(ray.direction.y >= 0){
				tymin = (min.y - ray.origin.y) / ray.direction.y;
				tymax = (max.y - ray.origin.y) / ray.direction.y;
			}else{
				tymin = (max.y - ray.origin.y) / ray.direction.y;
				tymax = (min.y - ray.origin.y) / ray.direction.y;
			}
			if(ray.direction.z >= 0){
				tzmin = (min.z - ray.origin.z) / ray.direction.z;
				tzmax = (max.z - ray.origin.z) / ray.direction.z;
			}else{
				tzmin = (max.z - ray.origin.z) / ray.direction.z;
				tzmax = (min.z - ray.origin.z) / ray.direction.z;
			}
			if((txmin > txmax) || (tymin > tymax)){
				return -1;
			}
			if (tymin > txmin)
				txmin = tymin;
			if (tymax < txmax)
				txmax = tymax;

			if((txmin > tzmax) || (tzmin > txmax)){
				return -1;
			}
		}
		if(shape!=null){
			return shape.intersectionT(ray);
		}else{
			if(boxes.size()==1)
				return boxes.get(0).intersectionT(ray);
			else{
				double t= Double.MAX_VALUE;
				for(BoundingBox box: boxes){
					double temp = box.getEntranceT(ray);
					if(temp<=t && temp!=-1){
						double t0 = box.intersectionT(ray);
						t =  Math.min(t0,t) != -1 ?  Math.min(t0,t): Math.max(t0,t);
					}
				}
				return t;

			}
		}
	}


	@Override
	public World getWorld() {
		return this.world;
	}


	@Override
	public int compareTo(BoundingBox box1) {
		ArrayList<Point> points = new ArrayList<Point>();
		points.add(min);
		points.add(max);
		points.add(box1.min);
		points.add(box1.max);
		points.sort(min.getComparator(0));
		if(points.get(0).equals(min) || points.get(0).equals(max)){
			return 1;
		}else
			return -1;
	}

	public Comparator<BoundingBox> getComparator(final int axis){
		return new Comparator<BoundingBox>() {
			@Override
			public int compare(BoundingBox b1, BoundingBox b2) {
				//				ArrayList<Point> points = new ArrayList<Point>();
				//				points.add(b1.min);
				//				points.add(b1.max);
				//				points.add(b2.min);
				//				points.add(b2.max);
				//			points.sort(min.getComparator(axis));

				//if(points.get(0).equals(min) || points.get(0).equals(max)){

				if(partitionPoint==0){
					if(b1.min.get(axis)<b2.min.get(axis)){
						return -1;
					}else if(b1.min.get(axis)>b2.min.get(axis))
						return 1;
					else
						return 0;
				}else if(partitionPoint==1){
					if(b1.max.get(axis)<b2.max.get(axis)){
						return -1;
					}else if(b1.max.get(axis)>b2.max.get(axis))
						return 1;
					else
						return 0;
				}else{
					if(b1.getMiddleOfAxis(axis)<b2.getMiddleOfAxis(axis)){
						return -1;
					}else if(b1.getMiddleOfAxis(axis)>b2.getMiddleOfAxis(axis))
						return 1;
					else
						return 0;
				}
			}
		};
	}


	public double getMiddleOfAxis(int axis){
		if(axis == 0)
			return min.x+ (max.x-min.x)/2;
		else if(axis==1)
			return min.y+ (max.y-min.y)/2;
		else
			return min.z+ (max.z-min.z)/2;
	}

	public double getPartitionPoint(int axis){
		if(partitionPoint==0){
			if(axis == 0)
				return min.x;
			else if(axis==1)
				return min.y;
			else
				return min.z;
		}else if(partitionPoint==1){
			if(axis == 0)
				return max.x;
			else if(axis==1)
				return max.y;
			else
				return max.z;
		}else{
			if(axis == 0)
				return min.x+ (max.x-min.x)/2;
			else if(axis==1)
				return min.y+ (max.y-min.y)/2;
			else
				return min.z+ (max.z-min.z)/2;
		}
	}

	public int getLongestAxis(){

		double x =max.x-min.x;
		double y = max.y-min.y;
		double z = max.z-min.z;
		if(y<=x && z<=x)
			return 0;
		else if(x<=y && z<=y)
			return 1;
		else
			return 2;

	}

	private double tMin = Double.MAX_VALUE;
	private double tMax = Double.MAX_VALUE;

	public double getEntranceT(Ray ray) {
		//ray.intersectCount++;
		double txmin, txmax, tymin, tymax, tzmin, tzmax;
		if(ray.direction.x >= 0){
			txmin = (min.x - ray.origin.x) / ray.direction.x;
			txmax = (max.x - ray.origin.x) / ray.direction.x;
		}else{
			txmin = (max.x - ray.origin.x) / ray.direction.x;
			txmax = (min.x - ray.origin.x) / ray.direction.x;
		}
		if(ray.direction.y >= 0){
			tymin = (min.y - ray.origin.y) / ray.direction.y;
			tymax = (max.y - ray.origin.y) / ray.direction.y;
		}else{
			tymin = (max.y - ray.origin.y) / ray.direction.y;
			tymax = (min.y - ray.origin.y) / ray.direction.y;
		}
		if(ray.direction.z >= 0){
			tzmin = (min.z - ray.origin.z) / ray.direction.z;
			tzmax = (max.z - ray.origin.z) / ray.direction.z;
		}else{
			tzmin = (max.z - ray.origin.z) / ray.direction.z;
			tzmax = (min.z - ray.origin.z) / ray.direction.z;
		}
		if((txmin > tymax) || (tymin > txmax)){
			return -1;
		}
		if (tymin > txmin)
			txmin = tymin;
		if (tymax < txmax)
			txmax = tymax;
		if((txmin > tzmax) || (tzmin > txmax)){
			return -1;
		}
		if(tzmin > txmin){
			txmin = tzmin;
		}
		if(tzmax < txmax){
			txmax = tzmax;
		}
		double eps = 0.000001;
		if(txmin>eps){
			tMin = txmin;
			tMax = txmax;
			return txmin;
		}
		else if(txmax>eps){
			tMin = txmin;
			tMax = txmax;
			return txmin;
		}
		return -1;

	}

	@Override
	public double getCost(){
		if(shape!=null){
			return shape.getCost();
		}else return 0;
	}


	public double getSurface(){
		double x =max.x-min.x;
		double y = max.y-min.y;
		double z = max.z-min.z;
		return 2*x*y + 2*x*z + 2*y*z;
	}

	@Override
	public Vector getNormalAt(Point point, Ray ray) {
		return null;
	}

	@Override
	public Color shade(Point point, List<Light> lights, Ray ray) {
		return null;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return null;
	}

	public boolean insideBox(Point p) {
		return ((p.x > min.x && p.x < max.x) && (p.y > min.y && p.y < max.y) && (p.z > min.z && p.z < max.z));
	}
}
