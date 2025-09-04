package classes;

/**
 * <h1>CanvasCoordinate</h1>
 * <p>This is the class the represents the coordinate in the canvas drawing space</p>
 * <p>It will always be a double because the canvas operates in doubles of exact coordinates</p>
 */
public class CanvasCoordinate 
{
	public double x;
	public double y;
	public CanvasCoordinate(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
}
