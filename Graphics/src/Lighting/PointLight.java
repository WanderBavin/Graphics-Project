package Lighting;

import java.awt.Color;

import math.Point;



public class PointLight extends Light{

	public Point location;
	
	public PointLight(double intensity, Color colorTint,Point point,int shadowRays) {
		super(intensity, colorTint,shadowRays);
		location = point;
	}

}
