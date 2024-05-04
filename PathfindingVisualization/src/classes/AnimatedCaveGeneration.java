package classes;

import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.paint.Color;
import main.java.controllers.GridController;

public class AnimatedCaveGeneration extends Thread
{
	private String threadName;
	private Random random;

	public static int simulationSteps = 5;
	public static int birthMinimum = 4;
	public static int deathMinimum = 2;
	
	private GridController gridController;
	
	
	public AnimatedCaveGeneration(String threadName)
	{
		this.threadName = threadName;
		this.random = new Random();
		this.gridController = GridController.getInstance(); 
		
	}
	@Override
	public void run()
	{
		try
		{
			generateMaze();
			generateStartAndEndingLocation();
		}
		catch(InterruptedException e)
		{
			System.out.println(String.format("[%s]: %s", threadName, e.getMessage()));
		}
		
	}
	
	public void generateMaze() throws InterruptedException
	{	
		int gridSizeX = GridController.GRID_SIZE_X;
		int gridSizeY = GridController.GRID_SIZE_Y;
		
		double chanceToStartAlive = .45;

		for(int y = 0; y < gridSizeY; y++)
		{
			for(int x = 0; x < gridSizeX; x++)
			{
				
				double randomNumber = Math.random();
				//System.out.println(randomNumber);
				if(randomNumber <= chanceToStartAlive)
				{
					gridController.updateCell(x, y, Color.WHITE, true);
				}
				else
				{
					gridController.updateCell(x, y, Color.BLACK, false);
				}
			}
		
		}
		
		for(int i = 0; i < simulationSteps; i++)
		{
			Thread.sleep((long)100);
			doObstacleStep();
		}
		System.out.println("Finished obstacle generation");
	}
	
	public void doObstacleStep()
	{	
		GridElement[][] copyGrid = gridController.getCopyOfGrid();
		int gridSizeX = GridController.GRID_SIZE_X;
		int gridSizeY = GridController.GRID_SIZE_Y;

		for(int y = 0; y < gridSizeY; y++)
		{
			for(int x = 0; x < gridSizeX; x++)
			{

				GridElement element = gridController.getCell(x, y);
				int aliveNeighbors = countAliveNeighbours(element);
				//The new value is based on our simulation rules 
				//First, if a cell is alive but has too few neighbours, kill it. 
				// filled means dead
				if(element.alive)
				{
					if(aliveNeighbors <= deathMinimum)
					{
						copyGrid[x][y].color = Color.BLACK;
						copyGrid[x][y].alive = false;
	  				}
	  			} //Otherwisez, if the cell is dead now, check if it has the right number of neighbours to be 'born' 
	  			else if (!element.alive)
	  			{
	  				if(aliveNeighbors > birthMinimum)
	  				{
	  					copyGrid[x][y].color = Color.WHITE;
	  					copyGrid[x][y].alive = true;
					}
				}
				
			}
		}
		
		gridController.setGrid(copyGrid);
		
	}
	
	public int countAliveNeighbours(GridElement e)
	{	
		int gridSizeX = GridController.GRID_SIZE_X;
		int gridSizeY = GridController.GRID_SIZE_Y;
		int count = 0;
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
				int neighbor_x = (int)e.x+i;
				int neighbor_y = (int)e.y+j;
				//If we're looking at the middle point 
				if(i == 0 && j == 0){
					//Do nothing, we don't want to add ourselves in! 
				}
				//In case the index we're looking at it off the edge of the map 
				else if(neighbor_x < 0 || neighbor_y < 0 || neighbor_x >= gridSizeX || neighbor_y >= gridSizeY){
					continue;
				}
				//Otherwise, a normal check of the neighbour 
				if(gridController.getCell(neighbor_x, neighbor_y).alive )
				{
					count = count + 1;
				}
			}
		}
		return count;
	}
	
	public void generateStartAndEndingLocation()
	{
		System.out.println("Running generation of start and ending locations");
		int gridSizeX = GridController.GRID_SIZE_X;
		int gridSizeY = GridController.GRID_SIZE_Y;
		while(true)
		{
			int randomX = random.nextInt(gridSizeX - 1 - 0) + 0;
		    int randomY = random.nextInt(gridSizeY - 1 - 0) + 0;
		    if(gridController.getCell(randomX, randomY).alive)
		    {
		    	gridController.setStartLocation(gridController.getCell(randomX, randomY));
		    	break;
		    }
		}
		while(true)
		{
			int randomX = random.nextInt(gridSizeX - 1 - 0) + 0;
		    int randomY = random.nextInt(gridSizeY - 1 - 0) + 0;
		    if(gridController.getCell(randomX, randomY).alive && !(gridController.getCell(randomX, randomY).equals(gridController.getStartLocation())	))
		    {
		    	gridController.setEndLocation(gridController.getCell(randomX, randomY));
		    	break;
		    }
		}
		
		gridController.getStartLocation().print();
		gridController.getEndLocation().print();
	    
	}
	
}
