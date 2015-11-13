package main;

import gui.ImagePanel;
import gui.ProgressReporter;
import gui.RenderFrame;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import math.Point;
import math.Ray;
import math.Vector;
import sampling.Sample;
import shape.Shape;
import Lighting.AmbientLight;
import Lighting.Light;
import Lighting.PointLight;
import acceleration.BoxSplit;
import acceleration.ObjectSplit;
import acceleration.RandomGenerator;
import acceleration.SurfaceAreaHeuristic;
import acceleration.World;
import camera.PerspectiveCamera;

/**
 * Entry point of your renderer.
 *
 * @author Niels Billen
 * @version 1.0
 */
public class Renderer {
	/**
	 * Entry point of your renderer.
	 *
	 * @param arguments
	 *            command line arguments.
	 */
	public static void main(String[] arguments) {
		int width = 640;
		int height = 640;

		World world = new World();
		BoxSplit heuristic = new BoxSplit(world,true);
		ObjectSplit heuristic2 = new ObjectSplit(world,true);
		SurfaceAreaHeuristic heuristic3 = new SurfaceAreaHeuristic(world, true,3);

		world.setSplitAlgorithm(heuristic);


		// parse the command line arguments
		for (int i = 0; i < arguments.length; ++i) {
			if (arguments[i].startsWith("-")) {
				try {
					if (arguments[i].equals("-width"))
						width = Integer.parseInt(arguments[++i]);
					else if (arguments[i].equals("-height"))
						height = Integer.parseInt(arguments[++i]);
					else if (arguments[i].equals("-help")) {
						System.out.println("usage: "
								+ "[-width  width of the image] "
								+ "[-height  height of the image]");
						return;
					} else {
						System.err.format("unknown flag \"%s\" encountered!\n",
								arguments[i]);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.err.format("could not find a value for "
							+ "flag \"%s\"\n!", arguments[i]);
				}
			} else
				System.err.format("unknown value \"%s\" encountered! "
						+ "This will be skipped!\n", arguments[i]);
		}

		// validate the input
		if (width <= 0)
			throw new IllegalArgumentException("the given width cannot be "
					+ "smaller than or equal to zero!");
		if (height <= 0)
			throw new IllegalArgumentException("the given height cannot be "
					+ "smaller than or equal to zero!");

		// initialize the camera
		PerspectiveCamera camera = new PerspectiveCamera(width, height,
				new Point(0.5,0.5,-1), new Vector(0, 0, 1), new Vector(0, 1, 0), 60);

		// initialize the graphical user interface
		ImagePanel panel = new ImagePanel(width, height);
		RenderFrame frame = new RenderFrame("A", panel);

		// initialize the progress reporter
		ProgressReporter reporter = new ProgressReporter("Rendering", 40, width
				* height, false);
		reporter.addProgressListener(frame);



		List<Shape> shapes = new ArrayList<Shape>();


		//		ArrayList<Long> curr1s = new ArrayList<Long>();
		//		ArrayList<Long> curr2s = new ArrayList<Long>();
		//		ArrayList<Long> curr3s = new ArrayList<Long>();
		//		ArrayList<Long> curr4s = new ArrayList<Long>();
		//		ArrayList<Long> curr5s = new ArrayList<Long>();
		//		ArrayList<Long> curr6s = new ArrayList<Long>();
		//		ArrayList<Integer> intersectCountList = new ArrayList<Integer>();
		//		for(int i=0;i<10;i++){

		long curr1 = System.currentTimeMillis();
		//RandomGenerator.testWorld7(world);
		//RandomGenerator.testWorld8(world);
		//RandomGenerator.UniformTeaPot(world, 100);
		//RandomGenerator.randGenUniform(world, 500000);
		//RandomGenerator.randGenUniformTeaPot(world, 500);
		//RandomGenerator.randGenNormal(world, 5000);
		//RandomGenerator.randGenExponential(world, 5000);
		RandomGenerator.UniformSphere(world, 500000);
		long curr2 = System.currentTimeMillis();


		//EmissiveRectangle rec = new EmissiveRectangle(Transformation.createTranslation(0, 0, 5).append(Transformation.createRotationX(120)), world);


		long curr3 = System.currentTimeMillis();
		//world.createAABBSmartRecursive();
		//world.createAABBSmart();
		world.createGrid();
		long curr4 = System.currentTimeMillis();

		shapes.addAll(world.worldObjects);

		List<Light> lights = new ArrayList<Light>();
		lights.add(new AmbientLight(1,Color.white,2));
		//lights.add(new PointLight(5,Color.white,new Point(0,5,0),2));
		lights.add(new PointLight(2,Color.white,new Point(0,10,-3),2));




		//lights.add(new AreaLight(5,Color.white,rec,1));
		//lights.add(new AreaLight(5,Color.white,rec2,6));
		//lights.add(new PointLight(5,Color.white,new Point(-5,100,950)));
		//lights.add(new PointLight(5,Color.red,new Point(-10,0,5)));

		// render the scene
		long curr5 = System.currentTimeMillis();
		double[][] intersectionList = new double[width][height];
		double max=1;
		int intersectCount=0;
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {

				ArrayList<Ray> rays= generateRays(1,x,y,camera);
				ArrayList<Color> colors = new ArrayList<Color>();

				for(Ray ray:rays){

					Shape nearest = null;
					double hitPoint = Double.MAX_VALUE;

					for (Shape shape : shapes){

						double temp = shape.intersectionT(ray);
						if( temp<hitPoint && temp!=-1){
							hitPoint = temp;
							nearest = ray.hitShape;
						}
					}

					if(nearest !=null){
						colors.add(nearest.shade(ray.origin.add(ray.direction.scale(hitPoint)),lights,ray));

					}else{
						colors.add(Color.black);

					}
					intersectCount+=ray.intersectCount;
				}
				panel.set(x, y, getColor(colors));


				double temp=Renderer.getAverageIntersects(rays);
				if(max<=temp)
					max=temp;
				intersectionList[x][y]=temp;

			}
			reporter.update(height);
		}
		long curr6 = System.currentTimeMillis();
		reporter.done();

		// save the output
		try {
			ImageIO.write(panel.getImage(), "png", new File("output.png"));
		} catch (IOException e) {
		}
		Renderer.falseColorImage(intersectionList,max,width,height,panel);
		// save the output
		try {
			ImageIO.write(panel.getImage(), "png", new File("falseColor.png"));
		} catch (IOException e) {
		}

		System.out.println(world.worldObjects.size());

		System.out.println("inlezen duurt " + (curr2-curr1));
		System.out.println("AABB maken duurt " + (curr4-curr3));
		System.out.println("renderen duurt " + (curr6-curr5));
		System.out.println("total duurt " + (curr6-curr1));
		System.out.println("Aantal intersecties: "+ intersectCount);

		for(Shape shape: world.worldObjects){
			System.out.println(shape.toString());
		}

	}

	private static long getAverage(ArrayList<Long> list){
		long sum=0;
		for (Long time : list) {
			sum += time;
		}
		return sum / list.size();
	}

	private static int getAverageInt(ArrayList<Integer> list){
		long sum=0;
		for (Integer time : list) {
			sum += time;
		}
		return (int) (sum / list.size());
	}


	private static double getAverageIntersects(ArrayList<Ray> rays) {

		double count=0;
		for(Ray ray:rays){
			count+=ray.intersectCount;
		}
		return count/rays.size();
	}

	private static Color getColor(ArrayList<Color> colors){
		int r=0;
		int g=0;
		int b=0;
		for(Color col : colors){
			r+=col.getRed();
			b+=col.getBlue();
			g+=col.getGreen();
		}
		r= r/colors.size();
		g=g/colors.size();
		b=b/colors.size();

		return new Color(r,g,b);

	}

	private static ArrayList<Ray> generateRays(int nbOfRays,int x, int y,PerspectiveCamera camera){
		double sqrt = Math.sqrt(nbOfRays);

		ArrayList<Ray> rays = new ArrayList<Ray>();
		if(nbOfRays<=1){
			rays.add(camera.generateRay(new Sample(x + 0.5, y + 0.5)));
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
						rays.add(camera.generateRay(new Sample(x + random, y + random2)));
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
					rays.add(camera.generateRay(new Sample(x + random, y + random2)));
					nbOfRays--;
				}
			}
		}

		return rays;
	}

	private static void falseColorImage(double[][] intersectionList, double max,int width,int height,ImagePanel panel){
		Color firstComponent;
		Color secondComponent;

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				double intersectPart = intersectionList[x][y]/max;

				if(intersectPart < 0.25){
					firstComponent = Color.blue;
					secondComponent = Color.green;
					intersectPart *= 4;
				}
				else if(intersectPart < 0.5){
					firstComponent = Color.green;
					secondComponent = Color.yellow;
					intersectPart *= 2;
				}
				else if(intersectPart < 0.75){
					firstComponent = Color.yellow;
					secondComponent = Color.orange;
					intersectPart *= 4/3;
				}
				else {
					firstComponent = Color.orange;
					secondComponent = Color.red;
				}

				int r = (int) ((1-intersectPart)*firstComponent.getRed() + intersectPart*secondComponent.getRed());
				int g = (int) ((1-intersectPart)*firstComponent.getGreen() + intersectPart*secondComponent.getGreen());
				int b = (int) ((1-intersectPart)*firstComponent.getBlue() + intersectPart*secondComponent.getBlue());
				panel.set(x, y, new Color(r,g,b));
			}
		}
	}

	private static void colorImage(World world, Ray ray, ImagePanel panel, int x, int y){
		Color col = clampColors(0,ray.intersectCount,255);
		panel.set(x, y, col);


	}

	private static Color clampColors(float r, float g, float b){
		r=Math.abs(r);
		g=Math.abs(g);
		b=Math.abs(b);
		float max = Math.max(Math.max(r,g), b);
		if(max>1){
			return new Color(r/max,g/max,b/max);
		}
		return new Color(r,g,b);
	}

}
