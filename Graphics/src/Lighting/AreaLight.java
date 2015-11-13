package Lighting;

import java.awt.Color;
import java.util.ArrayList;

import math.Point;
import math.Ray;
import math.Transformation;
import camera.PerspectiveCamera;
import sampling.Sample;
import shape.EmissiveRectangle;

public class AreaLight extends Light{

	public EmissiveRectangle rectangle;
	
	
	public AreaLight(double intensity, Color colorTint, EmissiveRectangle rec, int shadowRays) {
		super(intensity, colorTint,shadowRays);
		this.rectangle=rec;
		this.rectangle.setLight(this);
		
	}
	
	public int getShadowRays(){
		return this.shadowRays;
	}
	
	public ArrayList<Point> generateRays(int nbOfRays){
		double eps = 0.000001;
		Transformation trans = rectangle.transformation;
	
		double sqrt = Math.sqrt(nbOfRays);
		
		ArrayList<Point> points = new ArrayList<Point>();
		if(nbOfRays<=1){
			points.add(trans.transform(new Point(0.5,0.5,-eps)));
		}else{
			if(sqrt%1==0){
				
				double part = 1/(sqrt);
				double party1 = 0;
				double party2 = 1/(sqrt);
				int yPart = (int) sqrt;
				while(yPart>0){
					int xPart = (int) sqrt;
					double partx1 = 0;
					double partx2 = 1/(sqrt);
					while( xPart>0){
						double random = Math.random()*(partx2-partx1)+partx1;
						double random2 = Math.random()*(party2-party1)+party1;
						points.add(trans.transform(new Point(random,random2,-eps)));
						xPart--;
						partx1= partx1+ part;
						partx2= partx2+ part;
					}
					yPart--;
					party1= party1+ part;
					party2= party2+ part;
				}
			}else{
				
				while( nbOfRays>0){
					double random = Math.random();
					double random2 = Math.random();
					points.add(trans.transform(new Point(random,random2,-eps)));
					nbOfRays--;
				}
			}
		}
		return points;
	}
}
