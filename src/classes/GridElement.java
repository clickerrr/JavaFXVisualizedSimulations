package classes;

import javafx.scene.paint.Color;

/**
 * <h1>GridElement</h1>
 * <p>Representation of a grid element in an X, Y grid, which has an associated canvas representation. It has a color, and a status (alive or dead).</p>
 */
public class GridElement extends Coordinate
{
	
	public Color color;
	public boolean alive;
	
	// THIS ELEMENT COULD POTENTIALLY BE UNNECESSAYR
	private CanvasCoordinate canvasCoordinate;
	
	public GridElement(int x, int y, Color color, boolean alive, CanvasCoordinate canvasCoordinate)
	{
		super(x,y);
		this.color = color;
		this.alive = alive;
		this.canvasCoordinate = canvasCoordinate;
	}
	
	public GridElement(GridElement copy)
	{
		this.x = copy.x;
		this.y = copy.y;
		this.color = copy.color;
		this.alive = copy.alive;
		this.canvasCoordinate = copy.canvasCoordinate;
	}
	
	public CanvasCoordinate getCanvasCoordinate()
	{
		return this.canvasCoordinate;
	}
	
	public String toString()
	{
		return String.format("[GridElement] [x: %f, y: %f] [Color: %s] [Alive: %s]", x, y, color, alive);
		
	}
}
