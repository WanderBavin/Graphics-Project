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

public class DiffuseShading extends Material{



	private double kd;

	private Color cd;

	public DiffuseShading(double ka,double kd,Color ca,Color cd) {
		super(ka, ca);
		this.kd = kd;
		this.cd=cd;
	}

	@Override
	public Color shade(Shape shape,Point point,List<Light>lights,Ray ray) {
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
	public Color shade(Shape shape,Point point,List<Light>lights,Ray ray,Color col) {
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
