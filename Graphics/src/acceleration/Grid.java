package acceleration;

import java.util.ArrayList;

import math.Point;
import math.Ray;
import shape.BoundingBox;
import shape.Shape;

public class Grid {

	public BoundingBox box;
	public ArrayList<BoundingBox> shapeList;
	int nx;
	int ny;
	int nz;
	Shape[] cellArray;

	public Grid(BoundingBox box,ArrayList<BoundingBox> shapeList){
		this.box=box;
		this.shapeList=shapeList;
	}

	public void setupCells(){

		Point min = box.min;
		Point max = box.max;

		int NbOfObjects = shapeList.size();

		double lengthX = max.x - min.x;
		double lengthY = max.y - min.y;
		double lengthZ = max.z - min.z;

		double multiplier = 1;

		double s = Math.pow(lengthX*lengthY*lengthZ/NbOfObjects,0.3333333);
		nx = (int) (multiplier*lengthX/s +1);
		ny = (int) (multiplier*lengthY/s +1);
		nz = (int) (multiplier*lengthZ/s +1);

		int nbOfCells = nx*ny*nz;

		cellArray = new Shape[nbOfCells];
		int[] counts = new int[nbOfCells];
		int index;
		BoundingBox objBox;
		for(int j=0;j<NbOfObjects;j++){

			objBox = shapeList.get(j);
			int ixmin = (int) clamp((objBox.min.x - min.x) * nx / (max.x - min.x), 0, nx - 1);
			int iymin = (int) clamp((objBox.min.y - min.y) * ny / (max.y - min.y), 0, ny - 1);
			int izmin = (int) clamp((objBox.min.z - min.z) * nz / (max.z - min.z), 0, nz - 1);
			int ixmax = (int) clamp((objBox.max.x - min.x) * nx / (max.x - min.x), 0, nx - 1);
			int iymax = (int) clamp((objBox.max.y - min.y) * ny / (max.y - min.y), 0, ny - 1);
			int izmax = (int) clamp((objBox.max.z - min.z) * nz / (max.z - min.z), 0, nz - 1);

			for(int iz=izmin;iz<=izmax;iz++)
				for(int iy=iymin;iy<=iymax;iy++)
					for(int ix = ixmin; ix <= ixmax; ix++){
						index = ix + nx * iy + nx * ny * iz;

						if (counts[index] == 0) {
							cellArray[index] = shapeList.get(j);
							counts[index]++;
						}
						else {
							if (counts[index] == 1) {
								Compound comp = new Compound();
								comp.addObject(cellArray[index]);
								comp.addObject(shapeList.get(j));
								cellArray[index] = comp;
								counts[index] += 1;
							}
							else {
								((Compound) cellArray[index]).addObject(shapeList.get(j));
								counts[index] += 1;
							}
						}
					}
		}
	}

	private double clamp(double x,double min,double max){
		return (x<min ? min : (x>max ? max: x));
	}

	public double intersectionT(Ray ray){
		double ox = ray.origin.x;
		double oy = ray.origin.y;
		double oz = ray.origin.z;
		double dx = ray.direction.x;
		double dy = ray.direction.y;
		double dz = ray.direction.z;

		double x0 = box.min.x;
		double y0 = box.min.y;
		double z0 = box.min.z;
		double x1 = box.max.x;
		double y1 = box.max.y;
		double z1 = box.max.z;

		double dtx, dty, dtz;
		int initialX, initialY, initialZ;
		double txmin, tymin, tzmin, txmax, tymax, tzmax, t0, t1;
		double txnext, tynext, tznext;
		int ixstep, iystep, izstep, ixstop, iystop, izstop;

		if (dx >= 0) {
			txmin = (x0 - ox) /dx;
			txmax = (x1 - ox) /dx;
		}else{
			txmin = (x1 - ox) /dx;
			txmax = (x0 - ox) /dx;
		}
		if (dy >= 0) {
			tymin = (y0 - oy)/dy;
			tymax = (y1 - oy)/dy;
		}else{
			tymin = (y1 - oy)/dy;
			tymax = (y0 - oy)/dy;
		}
		if(dz >= 0){
			tzmin = (z0 - oz)/dz;
			tzmax = (z1 - oz)/dz;
		}else{
			tzmin = (z1 - oz)/dz;
			tzmax = (z0 - oz)/dz;
		}
		if (txmin > tymin)
			t0 = txmin;
		else
			t0 = tymin;

		if (tzmin > t0)
			t0 = tzmin;

		if (txmax < tymax)
			t1 = txmax;
		else
			t1 = tymax;

		if (tzmax < t1)
			t1 = tzmax;

		if (t0 > t1)
			return -1;


		if(box.insideBox(ray.origin)) {
			initialX = (int) clamp((ox - x0) * nx / (x1 - x0), 0, nx - 1);
			initialY = (int) clamp((oy - y0) * ny / (y1 - y0), 0, ny - 1);
			initialZ = (int) clamp((oz - z0) * nz / (z1 - z0), 0, nz - 1);
		}else{
			Point p = ray.origin.add(ray.direction.scale(t0));
			initialX = (int) clamp((p.x - x0) * nx / (x1 - x0), 0, nx - 1);
			initialY = (int) clamp((p.y - y0) * ny / (y1 - y0), 0, ny - 1);
			initialZ = (int) clamp((p.z - z0) * nz / (z1 - z0), 0, nz - 1);
		}

		dtx = (txmax - txmin) / nx;
		dty = (tymax - tymin) / ny;
		dtz = (tzmax - tzmin) / nz;

		if(dx > 0){
			txnext = txmin + (initialX + 1) * dtx;
			ixstep = +1;
			ixstop = nx;
		}else{
			txnext = txmin + (nx - initialX) * dtx;
			ixstep = -1;
			ixstop = -1;
		}
		if(dx == 0.0){
			txnext = Double.MAX_VALUE;
			ixstep = -1;
			ixstop = -1;
		}
		if (dy > 0){
			tynext = tymin + (initialY + 1) * dty;
			iystep = +1;
			iystop = ny;
		}else{
			tynext = tymin + (ny - initialY) * dty;
			iystep = -1;
			iystop = -1;
		}
		if(dy == 0.0){
			tynext = Double.MAX_VALUE;
			iystep = -1;
			iystop = -1;
		}
		if(dz > 0){
			tznext = tzmin + (initialZ + 1) * dtz;
			izstep = +1;
			izstop = nz;
		}else{
			tznext = tzmin + (nz - initialZ) * dtz;
			izstep = -1;
			izstop = -1;
		}
		if (dz == 0.0) {
			tznext = Double.MAX_VALUE;
			izstep = -1;
			izstop = -1;
		}

		double t = Double.MAX_VALUE;
		while (true) {

			Shape obj = cellArray[initialX + nx * initialY + nx * ny * initialZ];

			if(txnext < tynext && txnext < tznext) {
				if(obj != null){
					t = obj.intersectionT(ray);

					if( t != -1 && t < txnext) {

						return t;
					}
				}

				txnext += dtx;
				initialX += ixstep;

				if (initialX == ixstop)
					return -1;
			}else{
				if(tynext < tznext) {
					if(obj != null){
						t = obj.intersectionT(ray);

						if( t!= -1 && t < tynext) {

							return t;
						}
					}

					tynext += dty;
					initialY += iystep;

					if(initialY == iystop)
						return -1;
				}else{
					if(obj != null){
						t = obj.intersectionT(ray);

						if( t!= -1 && t < tznext){

							return t;
						}
					}

					tznext += dtz;
					initialZ += izstep;

					if(initialZ == izstop)
						return -1;
				}
			}
		}
	}

}
