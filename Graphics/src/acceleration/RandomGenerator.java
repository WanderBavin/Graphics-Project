package acceleration;

import inputoutput.ModelInputReader;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import math.Transformation;
import shading.DiffuseShading;
import shading.Material;
import shading.PhongShading;
import shape.Cylinder;
import shape.Disk;
import shape.Plane;
import shape.Rectangle;
import shape.Sphere;

public class RandomGenerator {

	public static void randGenUniform(World world, int amount){
		for(int i =0;i<amount;i++){

			double xRand = Math.random();
			double yRand = Math.random();
			double zRand = Math.random();

			Transformation trans = Transformation.createTranslation(xRand, yRand, zRand);//.append(Transformation.createScale(0.01, 0.01, 0.01));
			Material mat = new DiffuseShading(0.2, 0.6, Color.red, Color.red);
			new Sphere(trans, 0.01, mat, world);
		}
	}

	public static void UniformSphere(World world, double amount){
		double amo = 1/amount;

		double s = Math.pow(amo,0.3333333);
		double nx =  (1/s +1);
		System.out.println(nx);
		ArrayList<Transformation> transList = new ArrayList<Transformation>();
		double step=1/nx;
		for(int x =1;x<=nx;x++){
			double xStep = x*step;
			for(int y = 1;y<=nx;y++){
				double yStep=y*step;
				for(int z = 1;z<=nx;z++){
					double zStep=z*step;
					Transformation trans = Transformation.createTranslation(xStep, yStep, zStep);
					transList.add(trans);
				}
			}
		}
		Material mat = new DiffuseShading(0.2, 0.6, Color.red, Color.red);
		int nx3 = (int) (nx)* (int)(nx)*(int)(nx);
		System.out.println(nx3);
		for(int i=0;i<nx3;i++){
			new Sphere(transList.get(i), 0.01, mat, world);
		}
	}

	public static void randGenUniformTeaPot(World world, int amount){
		for(int i =0;i<amount;i++){

			double xRand = Math.random();
			double yRand = Math.random();
			double zRand = Math.random();

			Transformation trans = Transformation.createTranslation(xRand, yRand, zRand).append(Transformation.createScale(0.01, 0.01, 0.01));
			Material mat = new DiffuseShading(0.2, 0.6, Color.red, Color.red);
			ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\teapot.obj",trans,mat);
		}
	}

	public static void randGenNormal(World world, int amount){
		Random rand = new Random();
		double std = 0.5/3;
		double mean = 0.5;
		for(int i =0;i<amount;i++){

			double xRand = rand.nextGaussian()*(std) + mean;
			double yRand = rand.nextGaussian()*(std) + mean;
			double zRand = rand.nextGaussian()*(std) + mean;

			Transformation trans = Transformation.createTranslation(xRand, yRand, zRand);//.append(Transformation.createScale(0.01, 0.01, 0.01));
			Material mat = new DiffuseShading(0.2, 0.6, Color.red, Color.red);
			new Sphere(trans, 0.01, mat, world);
		}
	}

	public static void UniformTeaPot(World world, double amount){
		ArrayList<Transformation> transList = new ArrayList<Transformation>((int) (2*amount));
		double amo = 1/amount;

		double s = Math.pow(amo,0.3333333);
		double nx =  (1/s +1);

		double step=1/nx;
		for(int x =1;x<=nx;x++){
			double xStep = x*step;
			for(int y = 1;y<=nx;y++){
				double yStep=y*step;
				for(int z = 1;z<=nx;z++){
					double zStep=z*step;
					Transformation trans = Transformation.createTranslation(xStep, yStep, zStep).append(Transformation.createScale(0.01, 0.01, 0.01));
					transList.add(trans);
				}
			}
		}
		Material mat = new DiffuseShading(0.2, 0.6, Color.red, Color.red);

		for(int i=0;i<amount;i++){
			ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\teapot.obj",transList.get(i),mat);
		}
	}


	public static void randGenExponential(World world, int amount){
		double lambda= 5;
		for(int i =0;i<amount;i++){

			double xRand = -Math.log((1 - (1 - Math.exp(-lambda)) * Math.random())) / lambda;
			double yRand = -Math.log((1 - (1 - Math.exp(-lambda)) * Math.random())) / lambda;
			double zRand = -Math.log((1 - (1 - Math.exp(-lambda)) * Math.random())) / lambda;

			Transformation trans = Transformation.createTranslation(xRand, yRand, zRand);
			Material mat = new DiffuseShading(0.2, 0.6, Color.red, Color.red);
			new Sphere(trans, 0.01, mat, world);
		}
	}

	public static void testWorld1(World world){

		//Transformation t4 = Transformation.createRotationY(90).append(Transformation.createTranslation(0,0, -1));

		Transformation t1 = Transformation.createTranslation(0, 0, 20);
		t1 = t1.append(Transformation.createRotationX(0));
		Transformation t2 =  Transformation.createRotationX(90);
		new Plane(t1,new DiffuseShading(0.2, 0.6, Color.red, Color.red),world);
		new Plane(t2,new DiffuseShading(0.2, 0.6, Color.blue, Color.blue),world);
		Transformation t3 = Transformation.createTranslation(3, 4, 15);
		Transformation t4 = Transformation.createTranslation(0, 1, 0).append(Transformation.createRotationY(180).append(Transformation.createScale(0.3, 0.3, 0.3)));
		Transformation t5 = Transformation.createTranslation(-3, 1, 0);

		Color col = new Color(255,236,139);
		Material mat=new DiffuseShading(0.2, 0.6, col,col);
		ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\venus.obj",t4,mat);
		mat = new DiffuseShading(0.2, 0.6, Color.yellow,Color.yellow);
		//ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\sphere.obj",t3,mat);

		//ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\torus.obj",t5,mat);

	}

	public static void testWorld2(World world){

		//Bunny in the fields.
		Transformation t15 = Transformation.createTranslation(0, 0,1000);
		t15 = t15.append(Transformation.createRotationX(0));
		Transformation t16 =  Transformation.createTranslation(0, -6, 0).append(Transformation.createRotationX(90));
		new Plane(t15,new DiffuseShading(0.2, 0.6, new Color(164,	211	,238), new Color(164,211,238)),world);
		new Plane(t16,new DiffuseShading(0.2, 0.6, new Color(124	,252	,0), new Color(124,	252	,0)),world);

		Transformation t1 = Transformation.createRotationY(180);
		Transformation t2 = Transformation.createTranslation(0, -6, 20).append(t1);
		Material mat = new DiffuseShading(0.2,0.5,new Color(200,130,20),new Color(200,130,20));

		ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\bunny.obj",t2,mat);


		Transformation trans = Transformation.createRotationX(90);
		Transformation l2 = Transformation.createTranslation(-5, 10, 999).append(trans);
		new Disk(l2,300,new PhongShading(0.2, 0.4, 0.5, Color.YELLOW, Color.YELLOW, Color.white, 50),world);
	}

	public static void testWorld3(World world){

		//diglett
		Transformation l1 = Transformation.createTranslation(-4, -4, 10);
		Transformation t1 = Transformation.createTranslation(0, -2, 8).append(l1).append(Transformation.createScale(1, 4, 1));
		Transformation t2 = Transformation.createTranslation(0, 2, 8).append(l1);
		Transformation t3 = Transformation.createTranslation(0, 1.2, 6).append(l1).append(Transformation.createScale(0.6, 0.4, 0.3));
		Transformation t4 = Transformation.createTranslation(-0.5, 2, 6.1).append(l1).append(Transformation.createScale(0.1, 0.35, 0.1));
		Transformation t5 = Transformation.createTranslation(0.5, 2, 6.1).append(l1).append(Transformation.createScale(0.1, 0.35, 0.1));
		Transformation t6 = Transformation.createTranslation(-0.7, -2, 6.3).append(l1);
		Transformation t7 = Transformation.createTranslation(0.7, -2, 6.3).append(l1);
		Transformation t8 = Transformation.createTranslation(-1.4, -2, 6.6).append(l1);
		Transformation t9 = Transformation.createTranslation(1.4, -2, 6.6).append(l1);
		Transformation t10 = Transformation.createTranslation(-2, -2, 7).append(l1);
		Transformation t11 = Transformation.createTranslation(2, -2, 7).append(l1);
		Transformation t12 = Transformation.createTranslation(0, -2, 6).append(l1);
		Transformation t13 = Transformation.createTranslation(-1.5, -2, 7.5).append(l1);
		Transformation t14 = Transformation.createTranslation(1.5, -2, 7.5).append(l1);

		Material mat = new DiffuseShading(0.2,0.5,new Color(200,130,20),new Color(200,130,20));
		Material phongMat = new PhongShading(0.2, 0.5, 0.8,new Color(214,90,90),new Color(214,90,90), Color.white, 50);
		Material phongMat2 = new PhongShading(0.2, 0.5, 0.4,new Color(20,20,20),new Color(20,20,20), Color.white, 50);
		Material mat2 = new DiffuseShading(0.2,0.5,new Color(105,62,40),new Color(105,62,40));

		new Cylinder(t1, 2,mat , world);
		new Sphere(t2, 2, mat, world);
		new Sphere(t3, 1, phongMat, world);
		new Sphere(t4, 1, phongMat2, world);
		new Sphere(t5, 1,phongMat2, world);
		new Sphere(t6, 0.6, mat2, world);
		new Sphere(t7, 0.6, mat2, world);
		new Sphere(t8, 0.6,mat2, world);
		new Sphere(t9, 0.6, mat2, world);
		new Sphere(t10, 0.6, mat2, world);
		new Sphere(t11, 0.6, mat2, world);
		new Sphere(t12, 0.6, mat2, world);
		new Sphere(t13, 0.6, mat2, world);
		new Sphere(t14, 0.6, mat2, world);
		Transformation t15 = Transformation.createTranslation(0, 0,1000);
		t15 = t15.append(Transformation.createRotationX(0));

		Transformation t16 =  Transformation.createTranslation(0, -6, 0).append(Transformation.createRotationX(90));
		new Plane(t15,new DiffuseShading(0.2, 0.6, new Color(164,	211	,238), new Color(164,211,238)),world);
		new Plane(t16,new DiffuseShading(0.2, 0.6, new Color(124	,252	,0), new Color(124,	252	,0)),world);
		//diglett2
		l1 = Transformation.createTranslation(0, -4, 8);
		t1 = Transformation.createTranslation(0, -2, 8).append(l1).append(Transformation.createScale(1, 4, 1));
		t2 = Transformation.createTranslation(0, 2, 8).append(l1);
		t3 = Transformation.createTranslation(0, 1.2, 6).append(l1).append(Transformation.createScale(0.6, 0.4, 0.3));
		t4 = Transformation.createTranslation(-0.5, 2, 6.1).append(l1).append(Transformation.createScale(0.1, 0.35, 0.1));
		t5 = Transformation.createTranslation(0.5, 2, 6.1).append(l1).append(Transformation.createScale(0.1, 0.35, 0.1));
		t6 = Transformation.createTranslation(-0.7, -2, 6.3).append(l1);
		t7 = Transformation.createTranslation(0.7, -2, 6.3).append(l1);
		t8 = Transformation.createTranslation(-1.4, -2, 6.6).append(l1);
		t9 = Transformation.createTranslation(1.4, -2, 6.6).append(l1);
		t10 = Transformation.createTranslation(-2, -2, 7).append(l1);
		t11 = Transformation.createTranslation(2, -2, 7).append(l1);
		t12 = Transformation.createTranslation(0, -2, 6).append(l1);
		t13 = Transformation.createTranslation(-1.5, -2, 7.5).append(l1);
		t14 = Transformation.createTranslation(1.5, -2, 7.5).append(l1);

		mat = new DiffuseShading(0.2,0.5,new Color(200,130,20),new Color(200,130,20));
		phongMat = new PhongShading(0.2, 0.5, 0.8,new Color(214,90,90),new Color(214,90,90), Color.white, 50);
		phongMat2 = new PhongShading(0.2, 0.5, 0.4,new Color(20,20,20),new Color(20,20,20), Color.white, 50);
		mat2 = new DiffuseShading(0.2,0.5,new Color(105,62,40),new Color(105,62,40));

		new Cylinder(t1, 2,mat , world);
		new Sphere(t2, 2, mat, world);
		new Sphere(t3, 1, phongMat, world);
		new Sphere(t4, 1, phongMat2, world);
		new Sphere(t5, 1,phongMat2, world);
		new Sphere(t6, 0.6, mat2, world);
		new Sphere(t7, 0.6, mat2, world);
		new Sphere(t8, 0.6,mat2, world);
		new Sphere(t9, 0.6, mat2, world);
		new Sphere(t10, 0.6, mat2, world);
		new Sphere(t11, 0.6, mat2, world);
		new Sphere(t12, 0.6, mat2, world);
		new Sphere(t13, 0.6, mat2, world);
		new Sphere(t14, 0.6, mat2, world);

		//diglett3
		l1 = Transformation.createTranslation(4, -4, 10);
		t1 = Transformation.createTranslation(0, -2, 8).append(l1).append(Transformation.createScale(1, 4, 1));
		t2 = Transformation.createTranslation(0, 2, 8).append(l1);
		t3 = Transformation.createTranslation(0, 1.2, 6).append(l1).append(Transformation.createScale(0.6, 0.4, 0.3));
		t4 = Transformation.createTranslation(-0.5, 2, 6.1).append(l1).append(Transformation.createScale(0.1, 0.35, 0.1));
		t5 = Transformation.createTranslation(0.5, 2, 6.1).append(l1).append(Transformation.createScale(0.1, 0.35, 0.1));
		t6 = Transformation.createTranslation(-0.7, -2, 6.3).append(l1);
		t7 = Transformation.createTranslation(0.7, -2, 6.3).append(l1);
		t8 = Transformation.createTranslation(-1.4, -2, 6.6).append(l1);
		t9 = Transformation.createTranslation(1.4, -2, 6.6).append(l1);
		t10 = Transformation.createTranslation(-2, -2, 7).append(l1);
		t11 = Transformation.createTranslation(2, -2, 7).append(l1);
		t12 = Transformation.createTranslation(0, -2, 6).append(l1);
		t13 = Transformation.createTranslation(-1.5, -2, 7.5).append(l1);
		t14 = Transformation.createTranslation(1.5, -2, 7.5).append(l1);

		mat = new DiffuseShading(0.2,0.5,new Color(200,130,20),new Color(200,130,20));
		phongMat = new PhongShading(0.2, 0.5, 0.8,new Color(214,90,90),new Color(214,90,90), Color.white, 50);
		phongMat2 = new PhongShading(0.2, 0.5, 0.4,new Color(20,20,20),new Color(20,20,20), Color.white, 50);
		mat2 = new DiffuseShading(0.2,0.5,new Color(105,62,40),new Color(105,62,40));

		new Cylinder(t1, 2,mat , world);
		new Sphere(t2, 2, mat, world);
		new Sphere(t3, 1, phongMat, world);
		new Sphere(t4, 1, phongMat2, world);
		new Sphere(t5, 1,phongMat2, world);
		new Sphere(t6, 0.6, mat2, world);
		new Sphere(t7, 0.6, mat2, world);
		new Sphere(t8, 0.6,mat2, world);
		new Sphere(t9, 0.6, mat2, world);
		new Sphere(t10, 0.6, mat2, world);
		new Sphere(t11, 0.6, mat2, world);
		new Sphere(t12, 0.6, mat2, world);
		new Sphere(t13, 0.6, mat2, world);
		new Sphere(t14, 0.6, mat2, world);

		//diglett4
		l1 = Transformation.createTranslation(-8, -4, 12);
		t1 = Transformation.createTranslation(0, -2, 8).append(l1).append(Transformation.createScale(1, 4, 1));
		t2 = Transformation.createTranslation(0, 2, 8).append(l1);
		t3 = Transformation.createTranslation(0, 1.2, 6).append(l1).append(Transformation.createScale(0.6, 0.4, 0.3));
		t4 = Transformation.createTranslation(-0.5, 2, 6.1).append(l1).append(Transformation.createScale(0.1, 0.35, 0.1));
		t5 = Transformation.createTranslation(0.5, 2, 6.1).append(l1).append(Transformation.createScale(0.1, 0.35, 0.1));
		t6 = Transformation.createTranslation(-0.7, -2, 6.3).append(l1);
		t7 = Transformation.createTranslation(0.7, -2, 6.3).append(l1);
		t8 = Transformation.createTranslation(-1.4, -2, 6.6).append(l1);
		t9 = Transformation.createTranslation(1.4, -2, 6.6).append(l1);
		t10 = Transformation.createTranslation(-2, -2, 7).append(l1);
		t11 = Transformation.createTranslation(2, -2, 7).append(l1);
		t12 = Transformation.createTranslation(0, -2, 6).append(l1);
		t13 = Transformation.createTranslation(-1.5, -2, 7.5).append(l1);
		t14 = Transformation.createTranslation(1.5, -2, 7.5).append(l1);

		mat = new DiffuseShading(0.2,0.5,new Color(200,130,20),new Color(200,130,20));
		phongMat = new PhongShading(0.2, 0.5, 0.8,new Color(214,90,90),new Color(214,90,90), Color.white, 50);
		phongMat2 = new PhongShading(0.2, 0.5, 0.4,new Color(20,20,20),new Color(20,20,20), Color.white, 50);
		mat2 = new DiffuseShading(0.2,0.5,new Color(105,62,40),new Color(105,62,40));

		new Cylinder(t1, 2,mat , world);
		new Sphere(t2, 2, mat, world);
		new Sphere(t3, 1, phongMat, world);
		new Sphere(t4, 1, phongMat2, world);
		new Sphere(t5, 1,phongMat2, world);
		new Sphere(t6, 0.6, mat2, world);
		new Sphere(t7, 0.6, mat2, world);
		new Sphere(t8, 0.6,mat2, world);
		new Sphere(t9, 0.6, mat2, world);
		new Sphere(t10, 0.6, mat2, world);
		new Sphere(t11, 0.6, mat2, world);
		new Sphere(t12, 0.6, mat2, world);
		new Sphere(t13, 0.6, mat2, world);
		new Sphere(t14, 0.6, mat2, world);

		//diglett5
		l1 = Transformation.createTranslation(8, -4, 12);
		t1 = Transformation.createTranslation(0, -2, 8).append(l1).append(Transformation.createScale(1, 4, 1));
		t2 = Transformation.createTranslation(0, 2, 8).append(l1);
		t3 = Transformation.createTranslation(0, 1.2, 6).append(l1).append(Transformation.createScale(0.6, 0.4, 0.3));
		t4 = Transformation.createTranslation(-0.5, 2, 6.1).append(l1).append(Transformation.createScale(0.1, 0.35, 0.1));
		t5 = Transformation.createTranslation(0.5, 2, 6.1).append(l1).append(Transformation.createScale(0.1, 0.35, 0.1));
		t6 = Transformation.createTranslation(-0.7, -2, 6.3).append(l1);
		t7 = Transformation.createTranslation(0.7, -2, 6.3).append(l1);
		t8 = Transformation.createTranslation(-1.4, -2, 6.6).append(l1);
		t9 = Transformation.createTranslation(1.4, -2, 6.6).append(l1);
		t10 = Transformation.createTranslation(-2, -2, 7).append(l1);
		t11 = Transformation.createTranslation(2, -2, 7).append(l1);
		t12 = Transformation.createTranslation(0, -2, 6).append(l1);
		t13 = Transformation.createTranslation(-1.5, -2, 7.5).append(l1);
		t14 = Transformation.createTranslation(1.5, -2, 7.5).append(l1);

		mat = new DiffuseShading(0.2,0.5,new Color(200,130,20),new Color(200,130,20));
		phongMat = new PhongShading(0.2, 0.5, 0.8,new Color(214,90,90),new Color(214,90,90), Color.white, 50);
		phongMat2 = new PhongShading(0.2, 0.5, 0.4,new Color(20,20,20),new Color(20,20,20), Color.white, 50);
		mat2 = new DiffuseShading(0.2,0.5,new Color(105,62,40),new Color(105,62,40));

		new Cylinder(t1, 2,mat , world);
		new Sphere(t2, 2, mat, world);
		new Sphere(t3, 1, phongMat, world);
		new Sphere(t4, 1, phongMat2, world);
		new Sphere(t5, 1,phongMat2, world);
		new Sphere(t6, 0.6, mat2, world);
		new Sphere(t7, 0.6, mat2, world);
		new Sphere(t8, 0.6,mat2, world);
		new Sphere(t9, 0.6, mat2, world);
		new Sphere(t10, 0.6, mat2, world);
		new Sphere(t11, 0.6, mat2, world);
		new Sphere(t12, 0.6, mat2, world);
		new Sphere(t13, 0.6, mat2, world);
		new Sphere(t14, 0.6, mat2, world);


		//diglett6
		l1 = Transformation.createTranslation(-12, -4, 14);
		t1 = Transformation.createTranslation(0, -2, 8).append(l1).append(Transformation.createScale(1, 4, 1));
		t2 = Transformation.createTranslation(0, 2, 8).append(l1);
		t3 = Transformation.createTranslation(0, 1.2, 6).append(l1).append(Transformation.createScale(0.6, 0.4, 0.3));
		t4 = Transformation.createTranslation(-0.5, 2, 6.1).append(l1).append(Transformation.createScale(0.1, 0.35, 0.1));
		t5 = Transformation.createTranslation(0.5, 2, 6.1).append(l1).append(Transformation.createScale(0.1, 0.35, 0.1));
		t6 = Transformation.createTranslation(-0.7, -2, 6.3).append(l1);
		t7 = Transformation.createTranslation(0.7, -2, 6.3).append(l1);
		t8 = Transformation.createTranslation(-1.4, -2, 6.6).append(l1);
		t9 = Transformation.createTranslation(1.4, -2, 6.6).append(l1);
		t10 = Transformation.createTranslation(-2, -2, 7).append(l1);
		t11 = Transformation.createTranslation(2, -2, 7).append(l1);
		t12 = Transformation.createTranslation(0, -2, 6).append(l1);
		t13 = Transformation.createTranslation(-1.5, -2, 7.5).append(l1);
		t14 = Transformation.createTranslation(1.5, -2, 7.5).append(l1);

		mat = new DiffuseShading(0.2,0.5,new Color(200,130,20),new Color(200,130,20));
		phongMat = new PhongShading(0.2, 0.5, 0.8,new Color(214,90,90),new Color(214,90,90), Color.white, 50);
		phongMat2 = new PhongShading(0.2, 0.5, 0.4,new Color(20,20,20),new Color(20,20,20), Color.white, 50);
		mat2 = new DiffuseShading(0.2,0.5,new Color(105,62,40),new Color(105,62,40));

		new Cylinder(t1, 2,mat , world);
		new Sphere(t2, 2, mat, world);
		new Sphere(t3, 1, phongMat, world);
		new Sphere(t4, 1, phongMat2, world);
		new Sphere(t5, 1,phongMat2, world);
		new Sphere(t6, 0.6, mat2, world);
		new Sphere(t7, 0.6, mat2, world);
		new Sphere(t8, 0.6,mat2, world);
		new Sphere(t9, 0.6, mat2, world);
		new Sphere(t10, 0.6, mat2, world);
		new Sphere(t11, 0.6, mat2, world);
		new Sphere(t12, 0.6, mat2, world);
		new Sphere(t13, 0.6, mat2, world);
		new Sphere(t14, 0.6, mat2, world);


		//diglett7
		l1 = Transformation.createTranslation(12, -4, 14);
		t1 = Transformation.createTranslation(0, -2, 8).append(l1).append(Transformation.createScale(1, 4, 1));
		t2 = Transformation.createTranslation(0, 2, 8).append(l1);
		t3 = Transformation.createTranslation(0, 1.2, 6).append(l1).append(Transformation.createScale(0.6, 0.4, 0.3));
		t4 = Transformation.createTranslation(-0.5, 2, 6.1).append(l1).append(Transformation.createScale(0.1, 0.35, 0.1));
		t5 = Transformation.createTranslation(0.5, 2, 6.1).append(l1).append(Transformation.createScale(0.1, 0.35, 0.1));
		t6 = Transformation.createTranslation(-0.7, -2, 6.3).append(l1);
		t7 = Transformation.createTranslation(0.7, -2, 6.3).append(l1);
		t8 = Transformation.createTranslation(-1.4, -2, 6.6).append(l1);
		t9 = Transformation.createTranslation(1.4, -2, 6.6).append(l1);
		t10 = Transformation.createTranslation(-2, -2, 7).append(l1);
		t11 = Transformation.createTranslation(2, -2, 7).append(l1);
		t12 = Transformation.createTranslation(0, -2, 6).append(l1);
		t13 = Transformation.createTranslation(-1.5, -2, 7.5).append(l1);
		t14 = Transformation.createTranslation(1.5, -2, 7.5).append(l1);

		mat = new DiffuseShading(0.2,0.5,new Color(200,130,20),new Color(200,130,20));
		phongMat = new PhongShading(0.2, 0.5, 0.8,new Color(214,90,90),new Color(214,90,90), Color.white, 50);
		phongMat2 = new PhongShading(0.2, 0.5, 0.4,new Color(20,20,20),new Color(20,20,20), Color.white, 50);
		mat2 = new DiffuseShading(0.2,0.5,new Color(105,62,40),new Color(105,62,40));

		new Cylinder(t1, 2,mat , world);
		new Sphere(t2, 2, mat, world);
		new Sphere(t3, 1, phongMat, world);
		new Sphere(t4, 1, phongMat2, world);
		new Sphere(t5, 1,phongMat2, world);
		new Sphere(t6, 0.6, mat2, world);
		new Sphere(t7, 0.6, mat2, world);
		new Sphere(t8, 0.6,mat2, world);
		new Sphere(t9, 0.6, mat2, world);
		new Sphere(t10, 0.6, mat2, world);
		new Sphere(t11, 0.6, mat2, world);
		new Sphere(t12, 0.6, mat2, world);
		new Sphere(t13, 0.6, mat2, world);
		new Sphere(t14, 0.6, mat2, world);

		Transformation trans = Transformation.createRotationX(90);
		Transformation l2 = Transformation.createTranslation(-5, 10, 999).append(trans);
		new Disk(l2,300,new PhongShading(0.2, 0.4, 0.5, Color.YELLOW, Color.YELLOW, Color.white, 50),world);

		Transformation t20 = Transformation.createRotationY(-90);
		Transformation t19 = Transformation.createScale(20, 20, 20);
		Transformation t21 = Transformation.createTranslation(-10, 3, 30).append(t19).append(t20);
		Material mattte = new PhongShading(0.2,1,0.2,new Color(200,130,20),new Color(200,130,20),Color.white,75);
		mattte = new PhongShading(0.2,0.5,0.2,Color.yellow,Color.yellow,Color.white,75);
		ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\house\\house.obj",t21,mattte,"C:\\Users\\Wander\\Desktop\\graphics\\house\\house_texture.jpg");

	}

	public static void testWorld4(World world){
		Transformation t1 = Transformation.createRotationY(180);
		Transformation t2 = Transformation.createTranslation(0, -6, 20).append(t1);
		Material mat = new DiffuseShading(0.2,0.5,new Color(200,130,20),new Color(200,130,20));

		ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\cone.obj",t2,mat);
	}

	public static void testWorld5(World world){
		//Bunny in the fields.
		Transformation t15 = Transformation.createTranslation(0, 0,100000000);
		t15 = t15.append(Transformation.createRotationX(0));
		Transformation t16 =  Transformation.createTranslation(0, -6, 0).append(Transformation.createRotationX(90));
		new Plane(t15,new DiffuseShading(0.2, 0.6, new Color(164,	211	,238), new Color(164,211,238)),world);
		new Plane(t16,new DiffuseShading(0.2, 0.6, new Color(124	,252	,0), new Color(124,	252	,0)),world);

		Transformation t1 = Transformation.createRotationY(180);
		Transformation t2 = Transformation.createTranslation(10, -2, 30).append(t1);
		Material mat = new PhongShading(0.2,0.5,0.2,new Color(200,130,20),new Color(200,130,20),Color.white,75);

		ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\triceratops.obj",t2,mat);

		t1 = Transformation.createRotationY(0);
		t2 = Transformation.createTranslation(-10, -2, 30).append(t1);
		mat = new PhongShading(0.2,0.5,0.2,new Color(205,	201	,201),new Color(205	,201	,201),Color.white,75);
		ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\triceratops.obj",t2,mat);


		Transformation trans = Transformation.createRotationX(90);
		Transformation l2 = Transformation.createTranslation(-5, 10, 999).append(trans);
		new Disk(l2,300,new PhongShading(0.2, 0.4, 0.5, Color.YELLOW, Color.YELLOW, Color.white, 50),world);

	}

	public static void testWorld6(World world){
		//textures on house.
		Transformation t15 = Transformation.createTranslation(0, 0,1000000);
		t15 = t15.append(Transformation.createRotationX(0));
		Transformation t16 =  Transformation.createTranslation(0, -6, 0).append(Transformation.createRotationX(90));
		new Plane(t15,new DiffuseShading(0.2, 0.6, new Color(164,	211	,238), new Color(164,211,238)),world);
		new Plane(t16,new DiffuseShading(0.2, 0.6, new Color(124	,252	,0), new Color(124,	252	,0)),world);

		Transformation t1 = Transformation.createRotationY(45);
		Transformation t2 = Transformation.createTranslation(0, 2, -4).append(t1);
		Material mat = new PhongShading(0.2,1,0.2,new Color(200,130,20),new Color(200,130,20),Color.white,75);
		mat = new PhongShading(0.2,0.5,0.2,Color.yellow,Color.yellow,Color.white,75);
		ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\house\\house.obj",t2,mat,"C:\\Users\\Wander\\Desktop\\graphics\\house\\house_texture.jpg");

		t1 = Transformation.createRotationY(0);
		t2 = Transformation.createTranslation(-10, -2, 30).append(t1);
		mat = new PhongShading(0.2,0.5,0.2,new Color(205,	201	,201),new Color(205	,201	,201),Color.white,75);
		//ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\triceratops.obj",t2,mat);


		Transformation trans = Transformation.createRotationX(90);
		Transformation l2 = Transformation.createTranslation(-5, 10, 999).append(trans);
		new Disk(l2,300,new PhongShading(0.2, 0.4, 0.5, Color.YELLOW, Color.YELLOW, Color.white, 50),world);

	}

	public static void testWorld7(World world){
		//dragon.
		Transformation t15 = Transformation.createTranslation(0, 0,1000);
		t15 = t15.append(Transformation.createRotationX(0));
		Transformation t16 =  Transformation.createTranslation(0, -6, 0).append(Transformation.createRotationX(90));
		new Plane(t15,new DiffuseShading(0.2, 0.6, new Color(164,	211	,238), new Color(164,211,238)),world);
		new Plane(t16,new DiffuseShading(0.2, 0.6, new Color(124	,252	,0), new Color(124,	252	,0)),world);

		Transformation t1 = Transformation.createRotationY(0);
		Transformation t3 = Transformation.createScale(10, 10, 10);
		Transformation t2 = Transformation.createTranslation(2, 0, 15).append(t1).append(t3);
		Material mat = new PhongShading(0.2,0.5,0.2,new Color(200,130,20),new Color(200,130,20),Color.white,75);
		//Material mat = new DiffuseShading(0.2,0.5,new Color(200,130,20),new Color(200,130,20));

		//ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\buddha.obj",t2,mat);
		ModelInputReader.readObject(world, "graphics/buddha.obj",t2,mat);

		t1 = Transformation.createRotationY(0);
		t2 = Transformation.createTranslation(-10, -2, 30).append(t1);
		mat = new PhongShading(0.2,0.5,0.2,new Color(205,	201	,201),new Color(205	,201	,201),Color.white,75);
		//ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\triceratops.obj",t2,mat);


		Transformation trans = Transformation.createRotationX(90);
		Transformation l2 = Transformation.createTranslation(-5, 10, 999).append(trans);
		new Disk(l2,300,new PhongShading(0.2, 0.4, 0.5, Color.YELLOW, Color.YELLOW, Color.white, 50),world);

	}

	public static void testWorld8(World world){
		Transformation t1 = Transformation.createRotationY(30);
		Transformation t3 = Transformation.createScale(2, 2, 2);
		Transformation t2 = Transformation.createTranslation(2, 0, 10).append(t1).append(t3);
		Material mat = new PhongShading(0.2,0.5,0.2,new Color(200,130,20),new Color(200,130,20),Color.white,75);

		ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\teapot.obj",t2,mat);
	}

	public static void testWorld9(World world){
		Transformation t1 = Transformation.createRotationY(180);
		Transformation t4 = Transformation.createRotationX(90);
		Transformation t3 = Transformation.createScale(1, 1, 1);
		Transformation t2 = Transformation.createTranslation(0, -4, 20).append(t1).append(t3).append(t4);
		Material mat = new PhongShading(0.2,0.5,0.2,new Color(200,130,20),new Color(200,130,20),Color.white,75);

		ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\muwtoo\\mewtwo.obj",t2,mat,"C:\\Users\\Wander\\Desktop\\graphics\\muwtoo\\mewtwo_0_0.jpg");


		Transformation t5 = Transformation.createScale(200, 200, 200);

		Transformation t6 = Transformation.createRotationX(90);
		Transformation t7 = Transformation.createTranslation(0, 0, 300).append(t6).append(t5);
		ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\plane.obj",t7,mat,"C:\\Users\\Wander\\Desktop\\graphics\\muwtoo\\space.jpg");

		Transformation t8 = Transformation.createScale(2, 2, 2);

		Transformation t9 = Transformation.createRotationX(0);
		Transformation t10 = Transformation.createTranslation(-9, 5, 20).append(t9).append(t8);

		ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\sphere.obj",t10,mat,"C:\\Users\\Wander\\Desktop\\graphics\\muwtoo\\pur.jpg");


		Transformation t11 = Transformation.createScale(2, 2, 2);

		Transformation t12 = Transformation.createRotationX(0);
		Transformation t13 = Transformation.createTranslation(9, 5, 20).append(t12).append(t11);
		ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\sphere.obj",t13,mat,"C:\\Users\\Wander\\Desktop\\graphics\\muwtoo\\pur.jpg");


	}
	public static void testWorld10(World world){
		Transformation t1 = Transformation.createRotationY(0);
		Transformation t3 = Transformation.createScale(2, 2, 2);
		Transformation t2 = Transformation.createTranslation(2, 0, 3).append(t1).append(t3);
		Transformation t4 = Transformation.createRotationX(0);
		Transformation t6 = Transformation.createTranslation(0, 0, 20);
		Material mat = new DiffuseShading(0.2,0.5,new Color(200,130,20),new Color(200,130,20));
		new Rectangle(t2,mat,world);
		mat = new DiffuseShading(0.2,0.5,Color.RED,Color.RED);
		t4=t4.append(t6);
		new Plane(t4,mat,world);
		//ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\teapot.obj",t2,mat);
	}

	public static void testWorld11(World world){
		//dragon.

		Transformation t1 = Transformation.createRotationY(0);
		Transformation t3 = Transformation.createScale(10, 10, 10);
		Transformation t2 = Transformation.createTranslation(2, 0, 15).append(t1).append(t3);
		Material mat = new PhongShading(0.2,0.5,0.2,new Color(200,130,20),new Color(200,130,20),Color.white,75);
		//Material mat = new DiffuseShading(0.2,0.5,new Color(200,130,20),new Color(200,130,20));

		//ModelInputReader.readObject(world, "C:\\Users\\Wander\\Desktop\\graphics\\buddha.obj",t2,mat);
		ModelInputReader.readObject(world, "graphics/buddha.obj",t2,mat);
	}

}
