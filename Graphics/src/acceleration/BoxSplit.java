package acceleration;

import java.util.ArrayList;

import shape.BoundingBox;

public class BoxSplit extends SplitHeuristic{

	public BoxSplit(World world, boolean fixed) {
		super(world, fixed);

	}

	@Override
	public void makeRecursiveBoxes(BoundingBox box, int axisToSplit) {

		int axis;
		if(fixed)
			axis = axisToSplit%3;
		else
			axis = box.getLongestAxis();
		double middle= box.getMiddleOfAxis(axis);

		ArrayList<BoundingBox> left = new ArrayList<BoundingBox>();
		ArrayList<BoundingBox> right = new ArrayList<BoundingBox>();

		for(BoundingBox littleBox:box.boxes){

			if(littleBox.getPartitionPoint(axis) <=middle)
				left.add(littleBox);
			else
				right.add(littleBox);
		}

		ArrayList<BoundingBox> temp = new ArrayList<BoundingBox>();

		if(left.size()>2 && right.size()==0){
			right.addAll(left.subList(left.size()/2, left.size()));
			temp.addAll(left.subList(0, left.size()/2 ));
			left.clear();
			left = temp;
		}
		if(right.size()>2 && left.size()==0){
			left.addAll(right.subList(right.size()/2, right.size()));
			temp.addAll(right.subList(0, right.size()/2 ));
			right.clear();
			right = temp;
		}

		box.boxes.clear();

		if(left.size()>0){
			BoundingBox subBox1 = makeBoundingBox(left);
			box.boxes.add(subBox1);
			if(subBox1.boxes.size()>2){
				this.makeRecursiveBoxes(subBox1,axis+1);
			}
		}
		if(right.size()>0){
			BoundingBox subBox2=makeBoundingBox(right);
			box.boxes.add(subBox2);
			if(subBox2.boxes.size()>2){
				this.makeRecursiveBoxes(subBox2,axis+1);
			}
		}
	}

}
