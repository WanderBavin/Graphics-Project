package shape;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import math.Point;
import math.Ray;
import math.Transformation;
import math.Vector;
import shading.Material;
import Lighting.Light;
import acceleration.World;

public class SmoothTriangle implements Shape{

	public Transformation transformation;

	public Material mat;
	public World world;

	public ArrayList<Integer> vertices;
	public ArrayList<Integer> textures;
	public ArrayList<Integer> normals;

	public TriangleMesh mesh;

	public SmoothTriangle(ArrayList<Integer> vertices,ArrayList<Integer> textures,ArrayList<Integer> normals,TriangleMesh mesh,Material mat,World world){
		this.transformation=mesh.transformation;
		this.mat=mat;
		this.world=world;
		this.vertices = vertices;
		this.textures = textures;
		this.normals = normals;
		this.mesh=mesh;
		//world.add(this);

	}

	@Override
	public Vector getNormalAt(Point point,Ray ray2) {

		//Ray ray = transformation.transformInverse(ray2);
		Ray ray = ray2;

		Point v0 = mesh.vertices.get(vertices.get(0));
		Point v1 = mesh.vertices.get(vertices.get(1));
		Point v2 = mesh.vertices.get(vertices.get(2));

		double a = v0.x-v1.x, b = v0.x-v2.x, c = ray.direction.x, d = v0.x - ray.origin.x;
		double e = v0.y - v1.y, f = v0.y-v2.y, g = ray.direction.y, h = v0.y - ray.origin.y;
		double i = v0.z - v1.z, j = v0.z-v2.z, k = ray.direction.z, l = v0.z - ray.origin.z;

		double m = f*k - g*j, n = h*k - g*l, p = f*l - h*j;
		double q = g*i - e*k, s =e*j - f*i;

		double invDenom = 1/(a*m+b*q+c*s);
		double e1 = d*m -b*n -c*p;
		double beta = e1*invDenom;

		double r = e*l - h*i;
		double e2 = a*n + d*q +c*r;
		double gamma = e2*invDenom;

		Vector nor0 = mesh.normals.get(normals.get(0));
		Vector nor1 = mesh.normals.get(normals.get(1));
		Vector nor2 = mesh.normals.get(normals.get(2));



		Vector nor = nor0.scale(1-beta-gamma).add(nor1.scale(beta).add(nor2.scale(gamma)));

		if(ray.direction.scale(-1).dot(nor)<0){
			return  nor.scale(-1);
			//return  transformation.getInverseTransformationMatrix().transpose().transform(nor.scale(-1));

		}
		return  nor;
		//return  transformation.getInverseTransformationMatrix().transpose().transform(nor);


	}


	@Override
	public double intersectionT(Ray ray2) {
		//ray2.intersectCount++;
		//Ray ray = transformation.transformInverse(ray2);
		Ray ray = ray2;
		double eps = 0.000001;

		Point v0 = mesh.vertices.get(vertices.get(0));
		Point v1 = mesh.vertices.get(vertices.get(1));
		Point v2 = mesh.vertices.get(vertices.get(2));

		double a = v0.x-v1.x, b = v0.x-v2.x, c = ray.direction.x, d = v0.x - ray.origin.x;
		double e = v0.y - v1.y, f = v0.y-v2.y, g = ray.direction.y, h = v0.y - ray.origin.y;
		double i = v0.z - v1.z, j = v0.z-v2.z, k = ray.direction.z, l = v0.z - ray.origin.z;

		double m = f*k - g*j, n = h*k - g*l, p = f*l - h*j;
		double q = g*i - e*k, s =e*j - f*i;

		double invDenom = 1/(a*m+b*q+c*s);

		double e1 = d*m -b*n -c*p;

		double beta = e1*invDenom;

		if(beta<0)
			return -1;

		double r = e*l - h*i;
		double e2 = a*n + d*q +c*r;
		double gamma = e2*invDenom;

		if(gamma<0)
			return -1;

		if(beta+gamma >1)
			return -1;

		double e3 = a*p - b*r +d*s;
		double t = e3*invDenom;

		if(t<eps)
			return -1;

		ray2.checkT(t, this);

		return t;
	}

	@Override
	public Color shade(Point point, List<Light> lights, Ray ray) {
		if(mesh.image==null){
			return mat.shade(this, point, lights, ray);
		}
		return mat.shade(this, point, lights, ray, this.getColor(point,ray));
	}

	public Color getColor(Point point,Ray ray2) {
		//Ray ray =transformation.transformInverse(ray2);
		Ray ray = ray2;

		Point v0 = mesh.vertices.get(vertices.get(0));
		Point v1 = mesh.vertices.get(vertices.get(1));
		Point v2 = mesh.vertices.get(vertices.get(2));

		double a = v0.x-v1.x, b = v0.x-v2.x, c = ray.direction.x, d = v0.x - ray.origin.x;
		double e = v0.y - v1.y, f = v0.y-v2.y, g = ray.direction.y, h = v0.y - ray.origin.y;
		double i = v0.z - v1.z, j = v0.z-v2.z, k = ray.direction.z, l = v0.z - ray.origin.z;

		double m = f*k - g*j, n = h*k - g*l, p = f*l - h*j;
		double q = g*i - e*k, s =e*j - f*i;

		double invDenom = 1/(a*m+b*q+c*s);
		double e1 = d*m -b*n -c*p;
		double beta = e1*invDenom;

		double r = e*l - h*i;
		double e2 = a*n + d*q +c*r;
		double gamma = e2*invDenom;

		double interpolU = (1-beta-gamma)*mesh.u.get(textures.get(0)) + beta*mesh.u.get(textures.get(1)) + gamma*mesh.u.get(textures.get(2));

		double interpolV = (1-beta-gamma)*mesh.v.get(textures.get(0)) + beta*mesh.v.get(textures.get(1)) + gamma*mesh.v.get(textures.get(2));

		int col = mesh.image.getRGB((int)(Math.round(interpolU*(mesh.image.getWidth()-1))),(int)(Math.round( (1-interpolV)*(mesh.image.getHeight()-1))));
		Color col1 = new Color(col);
		return col1;
	}


	@Override
	public World getWorld() {
		return this.world;
	}


	public Color falseColorImage(Point point,Ray ray){
		Vector nor = getNormalAt(point,ray);
		float r = Math.abs((float) nor.x);
		float g = Math.abs((float) nor.y);
		float b = Math.abs((float) nor.z);

		return clampColors(r,g,b);
	}

	protected Color clampColors(float r, float g, float b){
		r=Math.abs(r);
		g=Math.abs(g);
		b=Math.abs(b);
		float max = Math.max(Math.max(r,g), b);
		if(max>1){
			return new Color(r/max,g/max,b/max);
		}
		return new Color(r,g,b);
	}

	@Override
	public BoundingBox getBoundingBox(){
		int n = 3;
		//		Point v0 = transformation.transform(mesh.vertices.get(vertices.get(0)));
		//		Point v1 = transformation.transform(mesh.vertices.get(vertices.get(1)));
		//		Point v2 = transformation.transform(mesh.vertices.get(vertices.get(2)));
		Point v0 = mesh.vertices.get(vertices.get(0));
		Point v1 =mesh.vertices.get(vertices.get(1));
		Point v2 = mesh.vertices.get(vertices.get(2));

		double minX = v0.x;
		double minY = v0.y;
		double minZ = v0.z;
		double maxX = v0.x;
		double maxY = v0.y;
		double maxZ = v0.z;
		Point[] V = {v0,v1,v2};
		for (int i = 1; i < n; ++i)
		{
			if ( V[i].x < minX ) minX = V[i].x;
			if ( V[i].y < minY ) minY = V[i].y;
			if ( V[i].z < minZ ) minZ = V[i].z;
			if ( V[i].x > maxX ) maxX = V[i].x;
			if ( V[i].y > maxY ) maxY = V[i].y;
			if ( V[i].z > maxZ ) maxZ = V[i].z;
		}

		return new BoundingBox(new Point(minX,minY,minZ),new Point(maxX,maxY,maxZ),this);
	}

	@Override
	public double getCost() {
		return 1;
	}

}
