package shading;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import math.Point;
import math.Ray;
import math.Vector;
import shape.Shape;
import Lighting.AmbientLight;
import Lighting.AreaLight;
import Lighting.Light;
import Lighting.PointLight;

public class PhongShading extends Material{


	private double kd;
	private double ks;

	private Color cd;
	private Color cs;

	private double phongExp;

	public PhongShading(double ka,double kd,double ks,Color ca,Color cd, Color cs,int phongExp) {
		super(ka,ca);
		this.kd=kd;
		this.ks=ks;
		this.cd=cd;
		this.cs=cs;
		this.phongExp=phongExp;

	}

	@Override
	public Color shade(Shape shape, Point point, List<Light> lights,Ray ray) {
		float r=0;
		float g=0;
		float b=0;
		for(Light light: lights){
			if(light instanceof AmbientLight){
				r+=ka*ca.getRGBColorComponents(null)[0]*light.intensity*light.colorTint.getRGBColorComponents(null)[0];
				g+=ka*ca.getRGBColorComponents(null)[1]*light.intensity*light.colorTint.getRGBColorComponents(null)[1];
				b+=ka*ca.getRGBColorComponents(null)[2]*light.intensity*light.colorTint.getRGBColorComponents(null)[2];
			}else if(light instanceof PointLight){
				Vector normal = shape.getNormalAt(point,ray);
				Vector lightvec = ((PointLight) light).location.subtract(point);
				Ray shadowRay = new Ray(point,lightvec);

				boolean hit = false;
				for(Shape obj:shape.getWorld().getWorldObjects()){
					double t = obj.intersectionT(shadowRay);
					if(t!=-1 && t<Double.MAX_VALUE){
						hit=inShadow(point, ((PointLight) light).location, shadowRay,obj,t);
						if(hit)
							break;
					}
				}
				if(!hit){
					double dotProd = normal.dot(lightvec);
					if(dotProd<0)
						dotProd=0;
					r+=kd/Math.PI*cd.getRGBColorComponents(null)[0]*light.intensity*light.colorTint.getRGBColorComponents(null)[0]*dotProd/(normal.length()*lightvec.length());
					g+=kd/Math.PI*cd.getRGBColorComponents(null)[1]*light.intensity*light.colorTint.getRGBColorComponents(null)[1]*dotProd/(normal.length()*lightvec.length());
					b+=kd/Math.PI*cd.getRGBColorComponents(null)[2]*light.intensity*light.colorTint.getRGBColorComponents(null)[2]*dotProd/(normal.length()*lightvec.length());
					r = clampColors(r, g, b).getRGBColorComponents(null)[0];
					g = clampColors(r, g, b).getRGBColorComponents(null)[1];
					b = clampColors(r, g, b).getRGBColorComponents(null)[2];

					Vector reflect = lightvec.scale(-1).add(normal.scale(2*(normal.dot(lightvec))));
					double refDot = reflect.dot(ray.direction.scale(-1))/(reflect.length()*ray.direction.scale(-1).length());
					if(refDot<0)
						refDot = 0;

					r+=ks*cs.getRGBColorComponents(null)[0]*Math.pow(refDot,phongExp) * light.intensity*light.colorTint.getRGBColorComponents(null)[0]*dotProd/(normal.length()*lightvec.length());
					g+=ks*cs.getRGBColorComponents(null)[1]*Math.pow(refDot,phongExp) * light.intensity*light.colorTint.getRGBColorComponents(null)[1]*dotProd/(normal.length()*lightvec.length());
					b+=ks*cs.getRGBColorComponents(null)[2]*Math.pow(refDot,phongExp) * light.intensity*light.colorTint.getRGBColorComponents(null)[2]*dotProd/(normal.length()*lightvec.length());

				}
			}else if(light instanceof AreaLight){
				int nbOfShadowRays=((AreaLight) light).getShadowRays();
				ArrayList<Point> points = ((AreaLight) light).generateRays(nbOfShadowRays);
				for(Point areaPoint:points){
					Vector normal = shape.getNormalAt(point,ray);
					Vector lightvec = areaPoint.subtract(point);
					Ray shadowRay = new Ray(point,lightvec);

					boolean hit = false;
					for(Shape obj:shape.getWorld().getWorldObjects()){
						double t = obj.intersectionT(shadowRay);
						if(t!=-1 && t<Double.MAX_VALUE){
							hit=inShadow(point, areaPoint, shadowRay,obj,t);
							//if(obj.equals(((AreaLight) light).rectangle))
							//hit=false;
							if(hit)
								break;
						}
					}
					if(!hit){

						double dotProd = normal.dot(lightvec);
						if(dotProd<0)
							dotProd=0;
						r+=(kd/Math.PI*cd.getRGBColorComponents(null)[0]*light.intensity*light.colorTint.getRGBColorComponents(null)[0]*dotProd/(normal.length()*lightvec.length()))/nbOfShadowRays;
						g+=(kd/Math.PI*cd.getRGBColorComponents(null)[1]*light.intensity*light.colorTint.getRGBColorComponents(null)[1]*dotProd/(normal.length()*lightvec.length()))/nbOfShadowRays;
						b+=(kd/Math.PI*cd.getRGBColorComponents(null)[2]*light.intensity*light.colorTint.getRGBColorComponents(null)[2]*dotProd/(normal.length()*lightvec.length()))/nbOfShadowRays;
						r = clampColors(r, g, b).getRGBColorComponents(null)[0];
						g = clampColors(r, g, b).getRGBColorComponents(null)[1];
						b = clampColors(r, g, b).getRGBColorComponents(null)[2];

						Vector reflect = lightvec.scale(-1).add(normal.scale(2*(normal.dot(lightvec))));
						double refDot = reflect.dot(ray.direction.scale(-1))/(reflect.length()*ray.direction.scale(-1).length());
						if(refDot<0)
							refDot = 0;

						r+=(ks*cs.getRGBColorComponents(null)[0]*Math.pow(refDot,phongExp) * light.intensity*light.colorTint.getRGBColorComponents(null)[0]*dotProd/(normal.length()*lightvec.length()))/nbOfShadowRays;
						g+=(ks*cs.getRGBColorComponents(null)[1]*Math.pow(refDot,phongExp) * light.intensity*light.colorTint.getRGBColorComponents(null)[1]*dotProd/(normal.length()*lightvec.length()))/nbOfShadowRays;
						b+=(ks*cs.getRGBColorComponents(null)[2]*Math.pow(refDot,phongExp) * light.intensity*light.colorTint.getRGBColorComponents(null)[2]*dotProd/(normal.length()*lightvec.length()))/nbOfShadowRays;
						r = clampColors(r, g, b).getRGBColorComponents(null)[0];
						g = clampColors(r, g, b).getRGBColorComponents(null)[1];
						b = clampColors(r, g, b).getRGBColorComponents(null)[2];
					}
				}

			}

			r = clampColors(r, g, b).getRGBColorComponents(null)[0];
			g = clampColors(r, g, b).getRGBColorComponents(null)[1];
			b = clampColors(r, g, b).getRGBColorComponents(null)[2];
		}
		return new Color(r,g,b);
	}

	@Override
	public Color shade(Shape shape, Point point, List<Light> lights,Ray ray,Color col) {
		float r=0;
		float g=0;
		float b=0;
		for(Light light: lights){
			if(light instanceof AmbientLight){
				r+=ka*col.getRGBColorComponents(null)[0]*light.intensity*light.colorTint.getRGBColorComponents(null)[0];
				g+=ka*col.getRGBColorComponents(null)[1]*light.intensity*light.colorTint.getRGBColorComponents(null)[1];
				b+=ka*col.getRGBColorComponents(null)[2]*light.intensity*light.colorTint.getRGBColorComponents(null)[2];
			}else if(light instanceof PointLight){
				Vector normal = shape.getNormalAt(point,ray);
				Vector lightvec = ((PointLight) light).location.subtract(point);
				Ray shadowRay = new Ray(point,lightvec);

				boolean hit = false;
				for(Shape obj:shape.getWorld().getWorldObjects()){
					double t = obj.intersectionT(shadowRay);
					if(t!=-1 && t<Double.MAX_VALUE){
						hit=inShadow(point, ((PointLight) light).location, shadowRay,obj,t);
						if(hit)
							break;
					}
				}
				if(!hit){
					double dotProd = normal.dot(lightvec);
					if(dotProd<0)
						dotProd=0;
					r+=kd/Math.PI*col.getRGBColorComponents(null)[0]*light.intensity*light.colorTint.getRGBColorComponents(null)[0]*dotProd/(normal.length()*lightvec.length());
					g+=kd/Math.PI*col.getRGBColorComponents(null)[1]*light.intensity*light.colorTint.getRGBColorComponents(null)[1]*dotProd/(normal.length()*lightvec.length());
					b+=kd/Math.PI*col.getRGBColorComponents(null)[2]*light.intensity*light.colorTint.getRGBColorComponents(null)[2]*dotProd/(normal.length()*lightvec.length());
					r = clampColors(r, g, b).getRGBColorComponents(null)[0];
					g = clampColors(r, g, b).getRGBColorComponents(null)[1];
					b = clampColors(r, g, b).getRGBColorComponents(null)[2];

					Vector reflect = lightvec.scale(-1).add(normal.scale(2*(normal.dot(lightvec))));
					double refDot = reflect.dot(ray.direction.scale(-1))/(reflect.length()*ray.direction.scale(-1).length());
					if(refDot<0)
						refDot = 0;

					r+=ks*cs.getRGBColorComponents(null)[0]*Math.pow(refDot,phongExp) * light.intensity*light.colorTint.getRGBColorComponents(null)[0]*dotProd/(normal.length()*lightvec.length());
					g+=ks*cs.getRGBColorComponents(null)[1]*Math.pow(refDot,phongExp) * light.intensity*light.colorTint.getRGBColorComponents(null)[1]*dotProd/(normal.length()*lightvec.length());
					b+=ks*cs.getRGBColorComponents(null)[2]*Math.pow(refDot,phongExp) * light.intensity*light.colorTint.getRGBColorComponents(null)[2]*dotProd/(normal.length()*lightvec.length());

				}
			}else if(light instanceof AreaLight){
				int nbOfShadowRays=((AreaLight) light).getShadowRays();
				ArrayList<Point> points = ((AreaLight) light).generateRays(nbOfShadowRays);
				for(Point areaPoint:points){
					Vector normal = shape.getNormalAt(point,ray);
					Vector lightvec = areaPoint.subtract(point);
					Ray shadowRay = new Ray(point,lightvec);

					boolean hit = false;
					for(Shape obj:shape.getWorld().getWorldObjects()){
						double t = obj.intersectionT(shadowRay);
						if(t!=-1 && t<Double.MAX_VALUE){
							hit=inShadow(point, areaPoint, shadowRay,obj,t);
							//if(obj.equals(((AreaLight) light).rectangle))
							//hit=false;
							if(hit)
								break;
						}
					}
					if(!hit){

						double dotProd = normal.dot(lightvec);
						if(dotProd<0)
							dotProd=0;
						r+=(kd/Math.PI*col.getRGBColorComponents(null)[0]*light.intensity*light.colorTint.getRGBColorComponents(null)[0]*dotProd/(normal.length()*lightvec.length()))/nbOfShadowRays;
						g+=(kd/Math.PI*col.getRGBColorComponents(null)[1]*light.intensity*light.colorTint.getRGBColorComponents(null)[1]*dotProd/(normal.length()*lightvec.length()))/nbOfShadowRays;
						b+=(kd/Math.PI*col.getRGBColorComponents(null)[2]*light.intensity*light.colorTint.getRGBColorComponents(null)[2]*dotProd/(normal.length()*lightvec.length()))/nbOfShadowRays;
						r = clampColors(r, g, b).getRGBColorComponents(null)[0];
						g = clampColors(r, g, b).getRGBColorComponents(null)[1];
						b = clampColors(r, g, b).getRGBColorComponents(null)[2];

						Vector reflect = lightvec.scale(-1).add(normal.scale(2*(normal.dot(lightvec))));
						double refDot = reflect.dot(ray.direction.scale(-1))/(reflect.length()*ray.direction.scale(-1).length());
						if(refDot<0)
							refDot = 0;

						r+=(ks*cs.getRGBColorComponents(null)[0]*Math.pow(refDot,phongExp) * light.intensity*light.colorTint.getRGBColorComponents(null)[0]*dotProd/(normal.length()*lightvec.length()))/nbOfShadowRays;
						g+=(ks*cs.getRGBColorComponents(null)[1]*Math.pow(refDot,phongExp) * light.intensity*light.colorTint.getRGBColorComponents(null)[1]*dotProd/(normal.length()*lightvec.length()))/nbOfShadowRays;
						b+=(ks*cs.getRGBColorComponents(null)[2]*Math.pow(refDot,phongExp) * light.intensity*light.colorTint.getRGBColorComponents(null)[2]*dotProd/(normal.length()*lightvec.length()))/nbOfShadowRays;
						r = clampColors(r, g, b).getRGBColorComponents(null)[0];
						g = clampColors(r, g, b).getRGBColorComponents(null)[1];
						b = clampColors(r, g, b).getRGBColorComponents(null)[2];
					}
				}

			}

			r = clampColors(r, g, b).getRGBColorComponents(null)[0];
			g = clampColors(r, g, b).getRGBColorComponents(null)[1];
			b = clampColors(r, g, b).getRGBColorComponents(null)[2];
		}
		return new Color(r,g,b);
	}



}
