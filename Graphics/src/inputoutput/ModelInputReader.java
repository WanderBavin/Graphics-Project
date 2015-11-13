package inputoutput;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import acceleration.World;
import shading.Material;
import shape.TriangleMesh;
import math.Transformation;

public class ModelInputReader {

	public static void readObject(World world,String path,Transformation trans,Material mat,String texPath){
		
		
		try {
			BufferedImage image = ImageIO.read(new File(texPath));
			TriangleMesh objMesh = new TriangleMesh(world,trans,mat,image);
		
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while ((line = br.readLine()) != null) {
				 String[] splitLine = line.split(" ");
				   switch(splitLine[0]){
				   case("v"):objMesh.processVertex(splitLine[1],splitLine[2],splitLine[3]);break;
				   case("vt"):objMesh.processTextureCoord(splitLine[1],splitLine[2]);break;
				   case("vn"):objMesh.processNormal(splitLine[1],splitLine[2],splitLine[3]);break;
				   case("f"):objMesh.processTriangle(splitLine[1],splitLine[2],splitLine[3]);break;
				   default:System.err.println("Invalid Line!");
				   }
			}
			br.close();
			
		} catch (IOException e) {
			System.err.println("The path does not exist!");
		}
	}
	
public static void readObject(World world,String path,Transformation trans,Material mat){
		
		
		try {
			TriangleMesh objMesh = new TriangleMesh(world,trans,mat);		
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while ((line = br.readLine()) != null) {
				 String[] splitLine = line.split(" ");
				   switch(splitLine[0]){
				   case("v"):objMesh.processVertex(splitLine[1],splitLine[2],splitLine[3]);break;
				   case("vt"):objMesh.processTextureCoord(splitLine[1],splitLine[2]);break;
				   case("vn"):objMesh.processNormal(splitLine[1],splitLine[2],splitLine[3]);break;
				   case("f"):objMesh.processTriangle(splitLine[1],splitLine[2],splitLine[3]);break;
				   default:System.err.println("Invalid Line!");
				   }
			}
			br.close();
		} catch (IOException e) {
			System.err.println("The path does not exist!");
		}
	}
	
	
}
