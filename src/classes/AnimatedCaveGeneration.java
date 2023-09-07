package classes;

import java.util.Random;

import javafx.scene.paint.Color;

public class AnimatedCaveGeneration extends Thread
{
	private String threadName;
	private GridElement[][] grid;
	private int gridSizeX;
	private int gridSizeY;
	
	public Coordinate startLocation;
	public Coordinate endLocation;
	
	private Random random;
	
	public AnimatedCaveGeneration(String threadName, GridElement[][] grid, int gridSizeX, int gridSizeY, Coordinate startLocation, Coordinate endLocation)
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
			generateMaze();
			generateStartAndEndingLocation();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void generateMaze() throws InterruptedException
	{
		double chanceToStartAlive = .45;

		for(int y = 0; y < gridSizeY; y++)
		{
			for(int x = 0; x < gridSizeX; x++)
			{
				
				double randomNumber = Math.random();
				//System.out.println(randomNumber);
				if(randomNumber <= chanceToStartAlive)
				{
					grid[x][y].alive = true;
					grid[x][y].color = Color.WHITE;
				}
				else
				{
					grid[x][y].alive = false;
					grid[x][y].color = Color.BLACK;
				}
					
				

			}
		
		}
		
		for(int i = 0; i < 10; i++)
		{
			Thread.sleep((long)100);
			doObstacleStep();
		}
		System.out.println("Finished obstacle generation");
	}
	
	public void doObstacleStep()
	{	
		GridElement[][] copyGrid = grid.clone();
		
		int deathMinimum = 3;
		int birthMinimum = 4;
		for(int y = 0; y < gridSizeY; y++)
		{
			for(int x = 0; x < gridSizeX; x++)
			{
				int aliveNeighbors = countAliveNeighbours(grid[x][y]);
				//The new value is based on our simulation rules 
				//First, if a cell is alive but has too few neighbours, kill it. 
				// filled means dead
				if(grid[x][y].alive)
				{
					if(aliveNeighbors <= deathMinimum)
					{
						copyGrid[x][y].color = Color.BLACK;
						copyGrid[x][y].alive = false;
	  				}
	  			} //Otherwisez, if the cell is dead now, check if it has the right number of neighbours to be 'born' 
	  			else if (!grid[x][y].alive)
	  			{
	  				if(aliveNeighbors > birthMinimum)
	  				{
	  					copyGrid[x][y].color = Color.WHITE;
	  					copyGrid[x][y].alive = true;
					}
				}
				
			}
		}
		
		grid = copyGrid.clone();
		
	}
	
	public int countAliveNeighbours(GridElement e){
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
				if(grid[neighbor_x][neighbor_y].alive )
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
		while(true)
		{
			int randomX = random.nextInt(gridSizeX - 1 - 0) + 0;
		    int randomY = random.nextInt(gridSizeY - 1 - 0) + 0;
		    if(grid[randomX][randomY].alive)
		    {
		    	grid[randomX][randomY] = new StartingGridElement(randomX, randomY);
			    startLocation.x = randomX;
			    startLocation.y = randomY;
		    	break;
		    }
		}
		while(true)
		{
			int randomX = random.nextInt(gridSizeX - 1 - 0) + 0;
		    int randomY = random.nextInt(gridSizeY - 1 - 0) + 0;
		    if(grid[randomX][randomY].alive && !(new Coordinate(randomX, randomY).equals(startLocation)	))
		    {
		    	grid[randomX][randomY] = new EndingGridElement(randomX, randomY);
			    endLocation.x = randomX;
			    endLocation.y = randomY;
		    	break;
		    }
		}
		startLocation.print();
		endLocation.print();
	    
	}
	
}
