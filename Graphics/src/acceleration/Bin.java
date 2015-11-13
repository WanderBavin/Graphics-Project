package acceleration;

import java.util.ArrayList;

import shape.BoundingBox;

/**
 * This class is unused and was introduced as an attempt to implement fast-binned SAH.
 * @author Wander
 *
 */
public class Bin {

	BoundingBox left;
	BoundingBox right;
	ArrayList<BoundingBox> boxes = new ArrayList<BoundingBox>();
	ArrayList<BoundingBox> restBoxes = new ArrayList<BoundingBox>();
	double min;
	double max;
	double cost;

	Type binType;

	public Bin(double min,double max,Type type){
		this.min=min;
		this.max=max;
		binType=type;
	}

	public void add(BoundingBox box){
		boxes.add(box);
	}
	public void addRest(BoundingBox box){
		restBoxes.add(box);
	}
}
