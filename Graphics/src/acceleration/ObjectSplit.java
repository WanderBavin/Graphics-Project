package acceleration;

import java.util.ArrayList;
import java.util.Collections;

import shape.BoundingBox;

public class ObjectSplit extends SplitHeuristic{

	public ObjectSplit(World world, boolean fixed) {
		super(world, fixed);

	}

	@Override
	public void makeRecursiveBoxes(BoundingBox box, int axisToSplit) {
		ArrayList<BoundingBox> boxList = new ArrayList<BoundingBox>(box.boxes);

		int axis;
		if(fixed)
			axis = axisToSplit%3;
		else
			axis = box.getLongestAxis();

		ArrayList<BoundingBox> left = new ArrayList<BoundingBox>();
		ArrayList<BoundingBox> right = new ArrayList<BoundingBox>();

		if(axis == 0){
			Collections.sort(boxList, box.getComparator(0));
		}else if(axis==1){
			Collections.sort(boxList, box.getComparator(1));
		}else{
			Collections.sort(boxList, box.getComparator(2));
		}

		left.addAll(boxList.subList(0, boxList.size()/2 ));
		right.addAll( boxList.subList(boxList.size()/2, boxList.size() ));

		box.boxes.clear();

		if(left.size()>0){
			BoundingBox subBox1 = makeBoundingBox(left);
			box.boxes.add(subBox1);
			if(subBox1.boxes.size()>2){
				this.makeRecursiveBoxes(subBox1, axis+1);
			}
		}
		if(right.size()>0){
			BoundingBox subBox2=makeBoundingBox(right);
			box.boxes.add(subBox2);
			if(subBox2.boxes.size()>2){
				this.makeRecursiveBoxes(subBox2, axis+1);
			}
		}

	}

}
