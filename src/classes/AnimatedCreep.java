package classes;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;

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
		try 
		{
			updateLocation();
		} 
		catch (InterruptedException e) 
		{
			System.out.println(String.format("[%s]: %s", threadName, e.getMessage()));
		}
	}
	
	public void updateLocation() throws InterruptedException
	{
		int x = startLocation.x;
		int y = startLocation.y;
		
		// timer loop
		while(true)
		{
			
			ArrayList<Coordinate> validNext = new ArrayList<Coordinate>();
			// coordinate arraylist that will add all the valid coordinates that the next step can go to 
			for(int i=-1; i<2; i++){
				for(int j=-1; j<2; j++){
					int neighbor_x = x+i;
					int neighbor_y = y+j;
					//If we're looking at the middle point 
					if(i == 0 && j == 0){
						//Do nothing, we don't want to add ourselves in! 
					}
					//In case the index we're looking at it off the edge of the map 
					else if(neighbor_x < 0 || neighbor_y < 0 || neighbor_x >= gridSizeX || neighbor_y >= gridSizeY || !grid[neighbor_x][neighbor_y].alive){
						continue;
					}
					// add to valid coordinates
					validNext.add(new Coordinate(neighbor_x, neighbor_y));
				}
			}
			
			int randomIndex = random.nextInt(validNext.size() - 1 - 0) + 0;
			Coordinate randomCoordinate = validNext.get(randomIndex);
			grid[x][y].color = Color.WHITE;
			x = randomCoordinate.x;
			y = randomCoordinate.y;
			grid[x][y].color = Color.GREEN;
			
			Thread.sleep((long)200);
		}
		
	}
	
	public void chooseDirection()
	{

	}
	
	
}
