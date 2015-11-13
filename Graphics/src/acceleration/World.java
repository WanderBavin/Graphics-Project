package acceleration;

import java.util.ArrayList;

import shape.BoundingBox;
import shape.FlatTriangle;
import shape.Plane;
import shape.Shape;
import shape.SmoothTriangle;
import shape.TriangleMesh;

public class World {

	public ArrayList<Shape> worldObjects;
	ArrayList<SmoothTriangle> triangles;
	public BoundingBox motherOfAllBoxes;
	ArrayList<TriangleMesh> meshList;
	SplitHeuristic splitAlgorithm;

	public World(){
		worldObjects = new ArrayList<Shape>();
		triangles = new ArrayList<SmoothTriangle>();
		meshList = new ArrayList<TriangleMesh>();
	}

	public void setSplitAlgorithm(SplitHeuristic splitAlgorithm){
		this.splitAlgorithm = splitAlgorithm;
	}

	public ArrayList<Shape> getWorldObjects() {
		return worldObjects;
	}

	public void add(Shape shape){
		worldObjects.add(shape);
	}

	public void addMesh(TriangleMesh mesh){
		meshList.add(mesh);
	}

	public void createTriangleList(){

		for(TriangleMesh mesh:meshList){
			triangles.addAll(mesh.allTriangles);
		}

	}

	public void makeFullList(){
		this.createTriangleList();
		worldObjects.addAll(triangles);
	}

	public void createAABBSmartRecursive(){
		//this.createTriangleList();
		ArrayList<BoundingBox> meshBoxes = new ArrayList<BoundingBox>();
		if(meshList.size()!=0){

			ArrayList<BoundingBox> triangleBoxes = new ArrayList<BoundingBox>();

			for(TriangleMesh mesh:meshList){

				for(SmoothTriangle tri : mesh.allTriangles){
					triangleBoxes.add(tri.getBoundingBox());
				}
				BoundingBox meshMother = splitAlgorithm.makeBoundingBox(triangleBoxes);
				meshBoxes.add(meshMother);
			}
			for(BoundingBox box:meshBoxes){
				splitAlgorithm.makeRecursiveBoxes(box, 0);
			}
		}

		ArrayList<Shape> temp = new ArrayList<Shape>(worldObjects);
		for(Shape shape: temp){
			if(!( shape instanceof Plane || shape instanceof FlatTriangle)){
				meshBoxes.add(shape.getBoundingBox());
				worldObjects.remove(shape);
			}
		}

		if(meshBoxes.size()!=0){
			BoundingBox motherOfAllBoxes = splitAlgorithm.makeBoundingBox(meshBoxes);
			splitAlgorithm.makeRecursiveBoxes(motherOfAllBoxes, 0);

			this.motherOfAllBoxes = motherOfAllBoxes;

			worldObjects.add(motherOfAllBoxes);
		}
	}

	public void createAABBSmart(){
		//this.createTriangleList();
		ArrayList<BoundingBox> triangleBoxes = new ArrayList<BoundingBox>();
		if(meshList.size()!=0){
			for(TriangleMesh mesh:meshList){

				for(SmoothTriangle tri : mesh.allTriangles){
					triangleBoxes.add(tri.getBoundingBox());
				}
			}
		}

		ArrayList<Shape> temp = new ArrayList<Shape>(worldObjects);
		for(Shape shape: temp){
			if(!( shape instanceof Plane || shape instanceof FlatTriangle)){
				triangleBoxes.add(shape.getBoundingBox());
				worldObjects.remove(shape);
			}
		}

		if(triangleBoxes.size()!=0){
			BoundingBox motherOfAllBoxes = splitAlgorithm.makeBoundingBox(triangleBoxes);
			splitAlgorithm.makeRecursiveBoxes(motherOfAllBoxes, 0);

			this.motherOfAllBoxes = motherOfAllBoxes;

			worldObjects.add(motherOfAllBoxes);
		}
	}

	public void createGrid(){
		ArrayList<BoundingBox> triangleBoxes = new ArrayList<BoundingBox>();
		if(meshList.size()!=0){
			for(TriangleMesh mesh:meshList){

				for(SmoothTriangle tri : mesh.allTriangles){
					triangleBoxes.add(tri.getBoundingBox());
				}
			}
		}

		ArrayList<Shape> temp = new ArrayList<Shape>(worldObjects);
		for(Shape shape: temp){
			if(!( shape instanceof Plane || shape instanceof FlatTriangle)){
				triangleBoxes.add(shape.getBoundingBox());
				worldObjects.remove(shape);
			}
		}
		System.out.println(triangleBoxes.size());

		if(triangleBoxes.size()!=0){
			BoundingBox motherOfAllBoxes = splitAlgorithm.makeBoundingBox(triangleBoxes);

			System.out.println(triangleBoxes.size());

			Grid grid = new Grid(motherOfAllBoxes,triangleBoxes);
			grid.setupCells();
			motherOfAllBoxes.setAsGrid(grid);
			this.motherOfAllBoxes = motherOfAllBoxes;

			worldObjects.add(motherOfAllBoxes);
		}
	}

	//	public void clear(){
	//		worldObjects = new ArrayList<Shape>();
	//		triangles = new ArrayList<SmoothTriangle>();
	//		meshList = new ArrayList<TriangleMesh>();
	//		motherOfAllBoxes=null;
	//	}

	////////////////////////TESTCODE/////////////////

	//	public void makeRecursiveBoxes(BoundingBox box){
	//		Point min1;
	//		Point max1;
	//		Point min2;
	//		Point max2;
	//		int axis = box.getLongestAxis();
	//		double middle= box.getMiddleOfAxis(axis);
	//		if(axis==0){
	//			min1 = new Point(box.min.x,box.min.y,box.min.z);
	//			max1 = new Point(middle,box.max.y,box.max.z);
	//			min2 = new Point(middle,box.min.y,box.min.z);
	//			max2 = new Point(box.max.x,box.max.y,box.max.z);
	//
	//			ArrayList<BoundingBox> left = new ArrayList<BoundingBox>();
	//			ArrayList<BoundingBox> right = new ArrayList<BoundingBox>();
	//			//BoundingBox subBox1 = new BoundingBox(min1, max1, this);
	//			//BoundingBox subBox2 = new BoundingBox(min2, max2, this);
	//			for(BoundingBox littleBox:box.boxes){
	////				if(littleBox.min.get(axis)<=middle){
	////					subBox1.boxes.add(littleBox);
	////				}
	////				if(littleBox.max.get(axis)>=middle){
	////					subBox2.boxes.add(littleBox);
	////				}
	//				if(littleBox.getMiddleOfAxis(axis) <=middle)
	//					left.add(littleBox);
	//				else
	//					right.add(littleBox);
	//			}
	//			ArrayList<BoundingBox> temp = new ArrayList<BoundingBox>();
	//
	//			if(left.size()>2 && right.size()==0){
	//				right.addAll(left.subList(left.size()/2, left.size()));
	//				temp.addAll(left.subList(0, left.size()/2 ));
	//				left.clear();
	//				left = temp;
	//			}
	//			if(right.size()>2 && left.size()==0){
	//				left.addAll(right.subList(right.size()/2, right.size()));
	//				temp.addAll(right.subList(0, right.size()/2 ));
	//				right.clear();
	//				right = temp;
	//			}
	//			box.boxes.clear();
	//			if(left.size()>0){
	//				double tem=middle;
	//				for(BoundingBox boxke:left){
	//					if(boxke.max.x>=tem)
	//						max1.x=boxke.max.x;
	//				}
	//				BoundingBox subBox1 =new BoundingBox(min1, max1, this,left);
	//				box.boxes.add(subBox1);
	//				if(subBox1.boxes.size()>2){
	//					this.makeRecursiveBoxes(subBox1);
	//				}
	//			}
	//			if(right.size()>0){
	//				double tem=middle;
	//				for(BoundingBox boxke:right){
	//					if(boxke.min.x<=tem)
	//						min2.x=boxke.min.x;
	//				}
	//				BoundingBox subBox2=new BoundingBox(min2, max2, this,right);
	//				box.boxes.add(subBox2);
	//				if(subBox2.boxes.size()>2){
	//					this.makeRecursiveBoxes(subBox2);
	//				}
	//			}
	//
	//		}else if(axis==1){
	//			min1 = new Point(box.min.x,box.min.y,box.min.z);
	//			max1 = new Point(box.max.x,middle,box.max.z);
	//			min2 = new Point(box.min.x,middle,box.min.z);
	//			max2 = new Point(box.max.x,box.max.y,box.max.z);
	//
	//			ArrayList<BoundingBox> left = new ArrayList<BoundingBox>();
	//			ArrayList<BoundingBox> right = new ArrayList<BoundingBox>();
	//			//BoundingBox subBox1 = new BoundingBox(min1, max1, this);
	//			//BoundingBox subBox2 = new BoundingBox(min2, max2, this);
	//			for(BoundingBox littleBox:box.boxes){
	////				if(littleBox.min.get(axis)<=middle){
	////					subBox1.boxes.add(littleBox);
	////				}
	////				if(littleBox.max.get(axis)>=middle){
	////					subBox2.boxes.add(littleBox);
	////				}
	//				if(littleBox.getMiddleOfAxis(axis) <=middle)
	//					left.add(littleBox);
	//				else
	//					right.add(littleBox);
	//			}
	//			ArrayList<BoundingBox> temp = new ArrayList<BoundingBox>();
	//
	//			if(left.size()>2 && right.size()==0){
	//				right.addAll(left.subList(left.size()/2, left.size()));
	//				temp.addAll(left.subList(0, left.size()/2 ));
	//				left.clear();
	//				left = temp;
	//			}
	//			if(right.size()>2 && left.size()==0){
	//				left.addAll(right.subList(right.size()/2, right.size()));
	//				temp.addAll(right.subList(0, right.size()/2 ));
	//				right.clear();
	//				right = temp;
	//			}
	//			box.boxes.clear();
	//			if(left.size()>0){
	//				double tem=middle;
	//				for(BoundingBox boxke:left){
	//					if(boxke.max.y>=tem)
	//						max1.y=boxke.max.y;
	//				}
	//				BoundingBox subBox1 =new BoundingBox(min1, max1, this,left);
	//				box.boxes.add(subBox1);
	//				if(subBox1.boxes.size()>2){
	//					this.makeRecursiveBoxes(subBox1);
	//				}
	//			}
	//			if(right.size()>0){
	//				double tem=middle;
	//				for(BoundingBox boxke:right){
	//					if(boxke.min.y<=tem)
	//						min2.y=boxke.min.y;
	//				}
	//				BoundingBox subBox2=new BoundingBox(min2, max2, this,right);
	//				box.boxes.add(subBox2);
	//				if(subBox2.boxes.size()>2){
	//					this.makeRecursiveBoxes(subBox2);
	//				}
	//			}
	//
	//		}else{
	//			min1 = new Point(box.min.x,box.min.y,box.min.z);
	//			max1 = new Point(box.max.x,box.max.y,middle);
	//			min2 = new Point(box.min.x,box.min.y,middle);
	//			max2 = new Point(box.max.x,box.max.y,box.max.z);
	//
	//			ArrayList<BoundingBox> left = new ArrayList<BoundingBox>();
	//			ArrayList<BoundingBox> right = new ArrayList<BoundingBox>();
	//			//BoundingBox subBox1 = new BoundingBox(min1, max1, this);
	//			//BoundingBox subBox2 = new BoundingBox(min2, max2, this);
	//			for(BoundingBox littleBox:box.boxes){
	////				if(littleBox.min.get(axis)<=middle){
	////					subBox1.boxes.add(littleBox);
	////				}
	////				if(littleBox.max.get(axis)>=middle){
	////					subBox2.boxes.add(littleBox);
	////				}
	//				if(littleBox.getMiddleOfAxis(axis) <=middle)
	//					left.add(littleBox);
	//				else
	//					right.add(littleBox);
	//			}
	//			ArrayList<BoundingBox> temp = new ArrayList<BoundingBox>();
	//
	//			if(left.size()>2 && right.size()==0){
	//				right.addAll(left.subList(left.size()/2, left.size()));
	//				temp.addAll(left.subList(0, left.size()/2 ));
	//				left.clear();
	//				left = temp;
	//			}
	//			if(right.size()>2 && left.size()==0){
	//				left.addAll(right.subList(right.size()/2, right.size()));
	//				temp.addAll(right.subList(0, right.size()/2 ));
	//				right.clear();
	//				right = temp;
	//			}
	//			box.boxes.clear();
	//			if(left.size()>0){
	//				double tem=middle;
	//				for(BoundingBox boxke:left){
	//					if(boxke.max.z>=tem)
	//						max1.z=boxke.max.z;
	//				}
	//				BoundingBox subBox1 =new BoundingBox(min1, max1, this,left);
	//				box.boxes.add(subBox1);
	//				if(subBox1.boxes.size()>2){
	//					this.makeRecursiveBoxes(subBox1);
	//				}
	//			}
	//			if(right.size()>0){
	//				double tem=middle;
	//				for(BoundingBox boxke:right){
	//					if(boxke.min.z<=tem)
	//						min2.z=boxke.min.z;
	//				}
	//				BoundingBox subBox2=new BoundingBox(min2, max2, this,right);
	//				box.boxes.add(subBox2);
	//				if(subBox2.boxes.size()>2){
	//					this.makeRecursiveBoxes(subBox2);
	//				}
	//			}
	//		}
	//	public void makeRecursiveBoxes(BoundingBox box, int axisToSplit) {
	//
	//		//			Point min1;
	//		//			Point max1;
	//		//			Point min2;
	//		//			Point max2;
	//		int axis;
	//		if(fixed)
	//
	//
	//			axis = box.getLongestAxis();
	//		double middle= box.getMiddleOfAxis(axis);
	//		//			if(axis==0){
	//		//				min1 = new Point(box.min.x,box.min.y,box.min.z);
	//		//				max1 = new Point(middle,box.max.y,box.max.z);
	//		//				min2 = new Point(middle,box.min.y,box.min.z);
	//		//				max2 = new Point(box.max.x,box.max.y,box.max.z);
	//		//
	//		//			}else if(axis==1){
	//		//				min1 = new Point(box.min.x,box.min.y,box.min.z);
	//		//				max1 = new Point(box.max.x,middle,box.max.z);
	//		//				min2 = new Point(box.min.x,middle,box.min.z);
	//		//				max2 = new Point(box.max.x,box.max.y,box.max.z);
	//		//
	//		//			}else{
	//		//				min1 = new Point(box.min.x,box.min.y,box.min.z);
	//		//				max1 = new Point(box.max.x,box.max.y,middle);
	//		//				min2 = new Point(box.min.x,box.min.y,middle);
	//		//				max2 = new Point(box.max.x,box.max.y,box.max.z);
	//		//			}
	//		ArrayList<BoundingBox> left = new ArrayList<BoundingBox>();
	//		ArrayList<BoundingBox> right = new ArrayList<BoundingBox>();
	//		//BoundingBox subBox1 = new BoundingBox(min1, max1, this);
	//		//BoundingBox subBox2 = new BoundingBox(min2, max2, this);
	//		for(BoundingBox littleBox:box.boxes){
	//			//				if(littleBox.min.get(axis)<=middle){
	//			//					subBox1.boxes.add(littleBox);
	//			//				}
	//			//				if(littleBox.max.get(axis)>=middle){
	//			//					subBox2.boxes.add(littleBox);
	//			//				}
	//			if(littleBox.getMiddleOfAxis(axis) <=middle)
	//				left.add(littleBox);
	//			else
	//				right.add(littleBox);
	//		}
	//		ArrayList<BoundingBox> temp = new ArrayList<BoundingBox>();
	//
	//		if(left.size()>2 && right.size()==0){
	//			right.addAll(left.subList(left.size()/2, left.size()));
	//			temp.addAll(left.subList(0, left.size()/2 ));
	//			left.clear();
	//			left = temp;
	//		}
	//		if(right.size()>2 && left.size()==0){
	//			left.addAll(right.subList(right.size()/2, right.size()));
	//			temp.addAll(right.subList(0, right.size()/2 ));
	//			right.clear();
	//			right = temp;
	//		}
	//		box.boxes.clear();
	//		if(left.size()>0){
	//			BoundingBox subBox1 = makeBoundingBox(left);
	//			box.boxes.add(subBox1);
	//			if(subBox1.boxes.size()>2){
	//				this.makeRecursiveBoxes(subBox1);
	//			}
	//		}
	//		if(right.size()>0){
	//			BoundingBox subBox2=makeBoundingBox(right);
	//			box.boxes.add(subBox2);
	//			if(subBox2.boxes.size()>2){
	//				this.makeRecursiveBoxes(subBox2);
	//			}
	//		}
	//
	//
	//		//
	//		//			ArrayList<BoundingBox> left = new ArrayList<BoundingBox>();
	//		//			ArrayList<BoundingBox> right = new ArrayList<BoundingBox>();
	//		//			this.splitInTwo(left, right, box.boxes);
	//		//			box.boxes.clear();
	//		//			if(left.size()>0){
	//		//				BoundingBox leftBox = makeBoundingBox(left);
	//		//				box.boxes.add(leftBox);
	//		//				if(leftBox.boxes.size()>2)
	//		//					this.makeRecursiveBoxes(leftBox);
	//		//			}
	//		//			if(right.size()>0){
	//		//				BoundingBox rightBox = makeBoundingBox(right);
	//		//				box.boxes.add(rightBox);
	//		//				if(rightBox.boxes.size()>2)
	//		//					this.makeRecursiveBoxes(rightBox);
	//		//			}
	//
	//
	//
	//	}
	//
	//
	//
	//	}
	//
	//	public void makeAABB(){
	//		System.out.println("Started");
	//		long curr = System.currentTimeMillis();
	//
	//		this.createTriangleList();
	//		if(triangles.size()==0)
	//			return;
	//		ArrayList<BoundingBox> triangleBoxes = new ArrayList<BoundingBox>();
	//		for(SmoothTriangle tri : triangles){
	//			triangleBoxes.add(tri.getBoundingBox());
	//		}
	//		if(triangleBoxes.size()!=0)
	//		Collections.sort(triangleBoxes, triangleBoxes.get(0).getComparator());
	//
	//
	//
	//		BoundingBox motherOfAllBoxes = makeBoundingBox(triangleBoxes);
	//
	//		this.makeRecursiveBoxes(motherOfAllBoxes);
	//		this.motherOfAllBoxes = motherOfAllBoxes;
	//
	//		worldObjects.add(motherOfAllBoxes);
	//
	//		long curr2 =  System.currentTimeMillis();
	//		System.out.println("createtriangle list takes:"+ (curr2 - curr)/1000);
	//	}
	//


	//	public BoundingBox makeBoundingBox(ArrayList<BoundingBox> boxes){
	//
	//		ArrayList<Point> vertices = new ArrayList<Point>();
	//		for(BoundingBox box:boxes){
	//			vertices.add(box.min);
	//			vertices.add(box.max);
	//		}
	//		HashSet<Point> hs = new HashSet<Point>();
	//		hs.addAll(vertices);
	//		vertices.clear();
	//		vertices.addAll(hs);
	//		double minX = vertices.get(0).x;
	//		double minY = vertices.get(0).y;
	//		double minZ = vertices.get(0).z;
	//		double maxX =vertices.get(0).x;
	//		double maxY = vertices.get(0).y;
	//		double maxZ =vertices.get(0).z;
	//		int n = vertices.size();
	//		for (int i = 1; i < n; ++i)
	//		{
	//			if (vertices.get(i).x < minX ) minX = vertices.get(i).x;
	//			if ( vertices.get(i).y < minY ) minY =vertices.get(i).y;
	//			if ( vertices.get(i).z < minZ ) minZ = vertices.get(i).z;
	//			if ( vertices.get(i).x > maxX ) maxX = vertices.get(i).x;
	//			if (vertices.get(i).y > maxY ) maxY = vertices.get(i).y;
	//			if (vertices.get(i).z > maxZ ) maxZ = vertices.get(i).z;
	//		}
	//		return new BoundingBox(new Point(minX,minY,minZ),new Point(maxX,maxY,maxZ),this,boxes);
	//	}

	//	public BoundingBox makeBoundingBox(ArrayList<BoundingBox> boxes){
	//		double minX = boxes.get(0).min.x;
	//		double minY =  boxes.get(0).min.y;
	//		double minZ =  boxes.get(0).min.z;
	//		double maxX = boxes.get(0).min.x;
	//		double maxY =  boxes.get(0).min.y;
	//		double maxZ = boxes.get(0).min.z;
	//
	//		for(BoundingBox box:boxes){
	//			if (box.min.x < minX ) minX = box.min.x;
	//			if ( box.min.y < minY ) minY =box.min.y;
	//			if ( box.min.z < minZ ) minZ = box.min.z;
	//			if (box.min.x > maxX ) maxX = box.min.x;
	//			if (box.min.y > maxY ) maxY = box.min.y;
	//			if (box.min.z > maxZ ) maxZ = box.min.z;
	//
	//			if (box.max.x < minX ) minX = box.max.x;
	//			if ( box.max.y < minY ) minY =box.max.y;
	//			if ( box.max.z < minZ ) minZ = box.max.z;
	//			if (box.max.x > maxX ) maxX = box.max.x;
	//			if (box.max.y > maxY ) maxY = box.max.y;
	//			if (box.max.z > maxZ ) maxZ = box.max.z;
	//		}
	//		return new BoundingBox(new Point(minX,minY,minZ),new Point(maxX,maxY,maxZ),this,boxes);
	//	}


	//	public void splitInTwo(ArrayList<BoundingBox> left,ArrayList<BoundingBox> right, ArrayList<BoundingBox> boxes){
	//		//Collections.sort(boxes, boxes.get(0).getComparator());
	//		left.addAll(boxes.subList(0, boxes.size()/2 ));
	//		right.addAll( boxes.subList(boxes.size()/2, boxes.size() ));
	//	}

	//	public void makeRecursiveBoxes(BoundingBox box){
	//		ArrayList<BoundingBox> left = new ArrayList<BoundingBox>();
	//		ArrayList<BoundingBox> right = new ArrayList<BoundingBox>();
	//		this.splitInTwo(left, right, box.boxes);
	//		box.boxes.clear();
	//		if(left.size()>0){
	//			BoundingBox leftBox = makeBoundingBox(left);
	//			box.boxes.add(leftBox);
	//			if(leftBox.boxes.size()>2)
	//				this.makeRecursiveBoxes(leftBox);
	//		}
	//		if(right.size()>0){
	//			BoundingBox rightBox = makeBoundingBox(right);
	//			box.boxes.add(rightBox);
	//			if(rightBox.boxes.size()>2)
	//				this.makeRecursiveBoxes(rightBox);
	//		}
	//
	//
	//	}

}
