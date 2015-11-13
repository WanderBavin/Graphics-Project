package acceleration;

import java.util.ArrayList;
import java.util.Collections;

import math.Point;
import shape.BoundingBox;

public class SurfaceAreaHeuristic extends SplitHeuristic{

	int splitPlanes;

	public SurfaceAreaHeuristic(World world, boolean fixed, int splitPlanes) {
		super(world, fixed);
		this.splitPlanes = splitPlanes;

	}


	@Override
	public void makeRecursiveBoxes(BoundingBox box, int axisToSplit) {
		double KT = 1.0;
		double KI =1.5;
		double Cns = KI*box.boxes.size();

		box.partitionPoint=0;
		ArrayList<BoundingBox> boxListXMin = new ArrayList<BoundingBox>(box.boxes);
		ArrayList<BoundingBox> boxListYMin = new ArrayList<BoundingBox>(box.boxes);
		ArrayList<BoundingBox> boxListZMin = new ArrayList<BoundingBox>(box.boxes);
		Collections.sort(boxListXMin, box.getComparator(0));
		Collections.sort(boxListYMin, box.getComparator(1));
		Collections.sort(boxListZMin, box.getComparator(2));

		box.partitionPoint=1;
		ArrayList<BoundingBox> boxListXMax = new ArrayList<BoundingBox>(box.boxes);
		ArrayList<BoundingBox> boxListYMax = new ArrayList<BoundingBox>(box.boxes);
		ArrayList<BoundingBox> boxListZMax = new ArrayList<BoundingBox>(box.boxes);
		Collections.sort(boxListXMax, box.getComparator(0));
		Collections.sort(boxListYMax, box.getComparator(1));
		Collections.sort(boxListZMax, box.getComparator(2));

		int size = box.boxes.size();

		Point min = box.min;
		Point max = box.max;
		double temp = Double.MAX_VALUE;
		int splitPlane = 100;
		int splitCount = 0;
		int index = 0;
		double SAL;
		double SAR;
		double SAT;
		double total = Double.MAX_VALUE;

		double previousMax = boxListXMin.get(0).min.x;

		for(BoundingBox boxke:boxListXMin){

			SAL= previousMax - min.x;
			SAR= max.x - boxke.min.x;
			SAT= max.x - min.x;

			total = KT+KI*(SAL/SAT*index + SAR/SAT*(size-index));

			if(total<=temp && total<Cns){
				splitPlane=0;
				temp=total;
				splitCount=index;
			}

			previousMax = Math.max(boxke.max.x, previousMax);
			index++;

		}

		previousMax = boxListXMax.get(size-1).max.x;
		BoundingBox boxkeMax;
		for(int i= size-1;i>=0;i--){
			boxkeMax = boxListXMax.get(i);

			SAL= boxkeMax.max.x - min.x;
			SAR= max.x - previousMax;
			SAT= max.x - min.x;

			total = KT+KI*(SAL/SAT*(i+1) + SAR/SAT*(size-i-1));

			if(total<=temp && total<Cns){
				splitPlane=1;
				temp=total;
				splitCount=i;
			}

			previousMax = Math.min(boxkeMax.min.x, previousMax);

		}

		previousMax = boxListYMin.get(0).min.y;
		index=0;
		for(BoundingBox boxke:boxListYMin){

			SAL= previousMax - min.y;
			SAR= max.y - boxke.min.y;
			SAT= max.y - min.y;

			total = KT+KI*(SAL/SAT*index + SAR/SAT*(size-index));

			if(total<=temp && total<Cns){
				splitPlane=2;
				temp=total;
				splitCount=index;
			}
			previousMax = Math.max(boxke.max.y, previousMax);
			index++;

		}

		previousMax = boxListYMax.get(size-1).max.y;
		for(int i= size-1;i>=0;i--){
			boxkeMax = boxListYMax.get(i);

			SAL= boxkeMax.max.y - min.y;
			SAR= max.y - previousMax;
			SAT= max.y - min.y;

			total = KT+KI*(SAL/SAT*(i+1) + SAR/SAT*(size-i-1));

			if(total<=temp && total<Cns){
				splitPlane=3;
				temp=total;
				splitCount=i;
			}
			previousMax = Math.min(boxkeMax.min.y, previousMax);

		}

		previousMax = boxListZMin.get(0).min.z;
		index=0;
		for(BoundingBox boxke:boxListZMin){

			SAL= previousMax - min.z;
			SAR= max.z - boxke.min.z;
			SAT= max.z - min.z;

			total = KT+KI*(SAL/SAT*index + SAR/SAT*(size-index));

			if(total<=temp && total<Cns){
				splitPlane=4;
				temp=total;
				splitCount=index;
			}
			previousMax = Math.max(boxke.max.z, previousMax);
			index++;

		}

		previousMax = boxListZMax.get(size-1).max.z;
		for(int i= size-1;i>=0;i--){
			boxkeMax = boxListZMax.get(i);

			SAL= boxkeMax.max.z - min.z;
			SAR= max.z - previousMax;
			SAT= max.z - min.z;

			total = KT+KI*(SAL/SAT*(i+1) + SAR/SAT*(size-i-1));

			if(total<=temp && total<Cns){
				splitPlane=5;
				temp=total;
				splitCount=i;
			}
			previousMax = Math.min(boxkeMax.min.z, previousMax);

		}

		if(total > Cns || splitCount == 0 || splitCount == size-1)
			return;

		ArrayList<BoundingBox> left = new ArrayList<BoundingBox>();
		ArrayList<BoundingBox> right = new ArrayList<BoundingBox>();

		if(splitPlane==0){

			left.addAll(boxListXMin.subList(0, splitCount+1));
			right.addAll(boxListXMin.subList(splitCount+1, size));
		}else if(splitPlane==1){

			left.addAll(boxListXMax.subList(0, splitCount+1));
			right.addAll( boxListXMax.subList(splitCount+1, size));
		}else if(splitPlane==2){

			left.addAll(boxListYMin.subList(0, splitCount+1));
			right.addAll( boxListYMin.subList(splitCount+1, size));
		}else if(splitPlane==3){

			left.addAll(boxListYMax.subList(0, splitCount+1));
			right.addAll( boxListYMax.subList(splitCount+1, size));
		}else if(splitPlane==4){

			left.addAll(boxListZMin.subList(0, splitCount+1));
			right.addAll( boxListZMin.subList(splitCount+1, size));
		}else if(splitPlane==5){

			left.addAll(boxListZMax.subList(0, splitCount+1));
			right.addAll( boxListZMax.subList(splitCount+1, size));
		}else
			return;

		box.boxes.clear();

		if(left.size()>0){
			BoundingBox subBox1 = makeBoundingBox(left);
			box.boxes.add(subBox1);
			if(subBox1.boxes.size()>2){
				this.makeRecursiveBoxes(subBox1, axisToSplit);
			}
		}
		if(right.size()>0){
			BoundingBox subBox2=makeBoundingBox(right);
			box.boxes.add(subBox2);
			if(subBox2.boxes.size()>2){
				this.makeRecursiveBoxes(subBox2, axisToSplit);
			}
		}

	}

	/* TESTCODE Fast-binned SAH

		public double getSurface(Point min,Point max){
			double x =max.x-min.x;
			double y = max.y-min.y;
			double z = max.z-min.z;
			return 2*x*y + 2*x*z + 2*y*z;
		}

	@Override
	public void makeRecursiveBoxes(BoundingBox box, int axisToSplit) {
		double KT = 1.0;
		double KI =1.5;
		double Cns = KI*box.boxes.size();

		int axis;
		if(fixed)
			axis = axisToSplit%3;
		else
			axis = box.getLongestAxis();

		ArrayList<BoundingBox> boxList = new ArrayList<BoundingBox>(box.boxes);
		ArrayList<Bin> bins = new ArrayList<Bin>();

		if(axis == 0){
			double lengthX =(box.max.x-box.min.x)/splitPlanes;
			for(int i=1;i<=splitPlanes;i++){

				bins.add(new Bin(box.min.x,box.min.x+i*lengthX,Type.XBin));

			}
		}else if(axis==1){
			double lengthY = (box.max.y-box.min.y)/splitPlanes;
			for(int i=1;i<=splitPlanes;i++){
				bins.add(new Bin(box.min.y,box.min.y+i*lengthY,Type.YBin));
			}
		}else{
			double lengthZ = (box.max.z-box.min.z)/splitPlanes;
			for(int i=1;i<=splitPlanes;i++){
				bins.add(new Bin(box.min.z,box.min.z+i*lengthZ,Type.ZBin));
			}
		}

		//TODO increase speed
		for(BoundingBox smallBox:boxList){
			for(Bin bin:bins){

				if(smallBox.getPartitionPoint(axis)<=bin.max)
					bin.add(smallBox);
				else
					bin.addRest(smallBox);


			}
		}

		double temp=Double.MAX_VALUE;
		Bin best=bins.get(0);
		for(Bin bin:bins){
			if(bin.boxes.size()==0)
				break;
			if(bin.restBoxes.size()==0)
				break;

			BoundingBox box1  = this.makeBoundingBox(bin.boxes);
			BoundingBox box2 = this.makeBoundingBox(bin.restBoxes);
			bin.left=box1;
			bin.right=box2;

			double SAL=box1.getSurface();
			double SAR=box2.getSurface();
			double SAT=box.getSurface();
			if(SAT>1)
				System.out.println(SAL +": "+ SAR + ": total is: " + SAT);

			double total = KT+KI*(SAL/SAT*box1.boxes.size() + SAR/SAT*box2.boxes.size());


			if(total<=temp){
				bin.cost=total;
				best=bin;
				temp=total;
			}
		}

		if(best.cost>Cns || temp==Double.MAX_VALUE || best.cost==0)
			return;

		box.boxes.clear();

		box.boxes.add(best.left);
		if(best.left.boxes.size()>2){
			this.makeRecursiveBoxes(best.left,axis+1);
		}
		box.boxes.add(best.right);
		if(best.right.boxes.size()>2){
			this.makeRecursiveBoxes(best.right,axis+1);
		}

	}
	 */
	/*
	@Override
	public void makeRecursiveBoxes(BoundingBox box, int axisToSplit) {
		double KT = 1.0;
		double KI =1.5;
		double Cns = KI*box.boxes.size();

		ArrayList<BoundingBox> boxList = new ArrayList<BoundingBox>(box.boxes);
		ArrayList<Bin> bins = new ArrayList<Bin>();

		double lengthX =(box.max.x-box.min.x)/splitPlanes;
		double lengthY = (box.max.y-box.min.y)/splitPlanes;
		double lengthZ = (box.max.z-box.min.z)/splitPlanes;

		for(int i=1;i<=splitPlanes;i++){
			bins.add(new Bin(box.min.x,box.min.x+i*lengthX,Type.XBin));
			bins.add(new Bin(box.min.y,box.min.y+i*lengthY,Type.YBin));
			bins.add(new Bin(box.min.z,box.min.z+i*lengthZ,Type.ZBin));
		}
		//TODO increase speed
		for(BoundingBox smallBox:boxList){
			for(Bin bin:bins){
				switch(bin.binType){
				case XBin:
					if(smallBox.getPartitionPoint(0)<=bin.max)
						bin.add(smallBox);
					else
						bin.addRest(smallBox);
					break;
				case YBin:
					if(smallBox.getPartitionPoint(1)<=bin.max)
						bin.add(smallBox);
					else
						bin.addRest(smallBox);
					break;
				case ZBin:
					if(smallBox.getPartitionPoint(2)<=bin.max)
						bin.add(smallBox);
					else
						bin.addRest(smallBox);
					break;

				}

			}
		}

		double temp=Double.MAX_VALUE;
		Bin best=bins.get(0);
		for(Bin bin:bins){
			if(bin.boxes.size()==0)
				break;
			if(bin.restBoxes.size()==0)
				break;

			BoundingBox box1  = this.makeBoundingBox(bin.boxes);
			BoundingBox box2 = this.makeBoundingBox(bin.restBoxes);
			bin.left=box1;
			bin.right=box2;

			double SAL=box1.getSurface();
			double SAR=box2.getSurface();
			double SAT=box.getSurface();


			double total = KT+KI*(SAL/SAT*box1.boxes.size() + SAR/SAT*box2.boxes.size());

			bin.cost=total;
			if(total<=temp){
				best=bin;
				temp=total;
			}
		}

		if(best.cost>Cns || temp==Double.MAX_VALUE || best.cost==0)
			return;

		box.boxes.clear();

		box.boxes.add(best.left);
		if(best.left.boxes.size()>2){
			this.makeRecursiveBoxes(best.left,axisToSplit);
		}
		box.boxes.add(best.right);
		if(best.right.boxes.size()>2){
			this.makeRecursiveBoxes(best.right,axisToSplit);
		}

	}
//*/
}
