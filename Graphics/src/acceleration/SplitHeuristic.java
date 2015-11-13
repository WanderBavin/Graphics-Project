package acceleration;

import java.util.ArrayList;

import math.Point;
import shape.BoundingBox;

public abstract class SplitHeuristic {

	World world;
	boolean fixed;

	public SplitHeuristic(World world,boolean fixed){
		this.world =world;
		this.fixed = fixed;
	}

	public abstract void makeRecursiveBoxes(BoundingBox box, int axisToSplit);

	public BoundingBox makeBoundingBox(ArrayList<BoundingBox> boxes){
		double minX = boxes.get(0).min.x;
		double minY = boxes.get(0).min.y;
		double minZ = boxes.get(0).min.z;
		double maxX = boxes.get(0).min.x;
		double maxY = boxes.get(0).min.y;
		double maxZ = boxes.get(0).min.z;

		for(BoundingBox box:boxes){
			if ( box.min.x < minX ) minX = box.min.x;
			if ( box.min.y < minY ) minY = box.min.y;
			if ( box.min.z < minZ ) minZ = box.min.z;
			if ( box.min.x > maxX ) maxX = box.min.x;
			if ( box.min.y > maxY ) maxY = box.min.y;
			if ( box.min.z > maxZ ) maxZ = box.min.z;

			if ( box.max.x < minX ) minX = box.max.x;
			if ( box.max.y < minY ) minY = box.max.y;
			if ( box.max.z < minZ ) minZ = box.max.z;
			if ( box.max.x > maxX ) maxX = box.max.x;
			if ( box.max.y > maxY ) maxY = box.max.y;
			if ( box.max.z > maxZ ) maxZ = box.max.z;
		}
		return new BoundingBox(new Point(minX,minY,minZ),new Point(maxX,maxY,maxZ),world,boxes);
	}
}
