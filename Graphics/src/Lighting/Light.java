package Lighting;

import java.awt.Color;

public class Light {
	public double intensity;
	public Color colorTint;
	int shadowRays;
	public Light(double intensity,Color colorTint, int shadowRays){
		this.intensity=intensity;
		this.colorTint = colorTint;
		this.shadowRays=shadowRays;
	}
}
