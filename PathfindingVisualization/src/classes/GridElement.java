package classes;

import javafx.scene.paint.Color;

public class GridElement
{
	public double x;
	public double y;
	public Color color;
	public boolean alive;
	
	public GridElement(double x, double y, Color color, boolean alive)
	{
		this.x = x;
		this.y = y;
		this.color = color;
		this.alive = alive;
	}
	public String toString()
	{
		return String.format("[GridElement] [x: %f, y: %f] [Color: %s] [Alive: %s]", x, y, color, alive);
		
	}
}
