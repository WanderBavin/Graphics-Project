package shape;

import java.awt.Color;
import java.util.List;

import math.Point;
import math.Ray;
import math.Vector;
import Lighting.Light;
import acceleration.World;

/**
 * Interface which should be implemented by all {@link Shape}s.
 *
 * @author Niels Billen
 * @version 1.0
 */
public interface Shape {

	public Vector getNormalAt(Point point,Ray ray);

	public double intersectionT(Ray ray);

	public Color shade(Point point,List<Light> lights,Ray ray);

	public World getWorld();

	public BoundingBox getBoundingBox();

	public double getCost();
}
