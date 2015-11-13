package shape;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import acceleration.World;
import shading.DiffuseShading;
import shading.Material;
import math.Point;
import math.Transformation;
import math.Vector;

public class TriangleMesh {
	public Transformation transformation;
	public World world;
	public ArrayList<Point> vertices;
	public ArrayList<Vector> normals;
	public ArrayList<Float> u;
	public ArrayList<Float> v;
	public BufferedImage image;
	public Material mat;
	public ArrayList<SmoothTriangle> allTriangles;
	
	public TriangleMesh(World world,Transformation trans,Material mat){
		this.transformation=trans;
		vertices = new ArrayList<Point>();
		allTriangles = new ArrayList<SmoothTriangle>();
		normals = new ArrayList<Vector>();
		u = new ArrayList<Float>();
		v = new ArrayList<Float>();
		this.mat=mat;
		this.world = world;
		world.addMesh(this);;
	}
	
	public TriangleMesh(World world,Transformation trans,Material mat,BufferedImage image){
		this.transformation=trans;
		vertices = new ArrayList<Point>();
		allTriangles = new ArrayList<SmoothTriangle>();
		normals = new ArrayList<Vector>();
		u = new ArrayList<Float>();
		v = new ArrayList<Float>();
		this.mat=mat;
		this.image = image;
		this.world = world;
		world.addMesh(this);;
	}

	public void processVertex(String x, String y, String z) {
		vertices.add(transformation.transform(new Point(Float.parseFloat(x),Float.parseFloat(y),Float.parseFloat(z))));
		//vertices.add(new Point(Float.parseFloat(x),Float.parseFloat(y),Float.parseFloat(z)));

	}

	public void processTextureCoord(String u, String v) {
		this.u.add(Float.parseFloat(u));
		this.v.add(Float.parseFloat(v));
		
	}

	public void processNormal(String x, String y, String z) {
		normals.add(transformation.getInverseTransformationMatrix().transpose().transform(new Vector(Float.parseFloat(x),Float.parseFloat(y),Float.parseFloat(z))));
		//normals.add(new Vector(Float.parseFloat(x),Float.parseFloat(y),Float.parseFloat(z)));

	}

	public void processTriangle(String v1, String v2, String v3) {
		ArrayList<Integer> ver = new ArrayList<Integer>();
		ArrayList<Integer> tex = new ArrayList<Integer>();
		ArrayList<Integer> nor = new ArrayList<Integer>();
		String[] v1Split = v1.split("/");
		String[] v2Split = v2.split("/");
		String[] v3Split = v3.split("/");
		ver.add(Integer.parseInt(v1Split[0])-1);
		ver.add(Integer.parseInt(v2Split[0])-1);
		ver.add(Integer.parseInt(v3Split[0])-1);
		
		tex.add(Integer.parseInt(v1Split[1])-1);
		tex.add(Integer.parseInt(v2Split[1])-1);
		tex.add(Integer.parseInt(v3Split[1])-1);
		
		nor.add(Integer.parseInt(v1Split[2])-1);
		nor.add(Integer.parseInt(v2Split[2])-1);
		nor.add(Integer.parseInt(v3Split[2])-1);
		
		
		allTriangles.add(new SmoothTriangle(ver, tex, nor, this, mat, world));
		
		
	}
	
	
}
