package classes;

import java.util.Random;

public class AnimatedCreep extends Thread 
{
	private String threadName;
	
	private GridElement[][] grid;
	private int gridSizeX;
	private int gridSizeY;
	
	public Coordinate startLocation;
	public Coordinate endLocation;
	
	private Random random;
	
	public AnimatedCreep(String threadName, GridElement[][] grid, int gridSizeX, int gridSizeY, Coordinate startLocation, Coordinate endLocation)
	{
		this.threadName = threadName;
		this.grid = grid;
		this.gridSizeX = gridSizeX;
		this.gridSizeY = gridSizeY;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		this.random = new Random();
	}

	@Override
	public void run()
	{
		
	}
	
	
}
