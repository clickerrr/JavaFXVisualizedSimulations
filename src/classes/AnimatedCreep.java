package classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
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
	
	private Coordinate currentLocation;
	
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

	public Queue<Coordinate> BFS(int startX, int startY) throws InterruptedException 
	{
		boolean[][] visited = new boolean[gridSizeX][gridSizeY];
	      
	   // Define the directions for exploring neighbors (up, down, left, right)
		int[] dr = {0, 0, 1, -1};
		int[] dc = {1, -1, 0, 0};
	   
	   Queue<Coordinate> queue = new LinkedList<>();
	   Queue<Coordinate> returnQueue = new LinkedList<>();
	   queue.add(new Coordinate(startX, startY));
	   visited[startX][startY] = true;
	   
	   while (!queue.isEmpty()) 
	   {
			Coordinate currentCell = queue.poll();
			returnQueue.add(currentCell);
			int x =(int)currentCell.x;
			int y = (int)currentCell.y;
			  
			//	      System.out.println("Visiting cell at (" + i + ", " + j + ")"); // Print the visited cell
			  
			for (int k = 0; k < 4; k++) 
			{ // Four directions: up, down, left, right
				int ni = x + dr[k];
				int nj = y + dc[k];
				
				  // Check if the new cell (ni, nj) is within the grid boundaries
				if (ni >= 0 && ni < gridSizeX && nj >= 0 && nj < gridSizeY && !visited[ni][nj] && grid[ni][nj].alive) 
				{ 
					// found a red
					// return closest red coordinate
					if(grid[ni][nj].color == Color.RED)
					{
						return returnQueue;
					}
					queue.add(new Coordinate(ni, nj));
					visited[ni][nj] = true;
				}
			}

		}
	   return null;

	}
	
	public void updateLocation() throws InterruptedException
	{

		this.currentLocation = startLocation;
		while(true)
		{
			Queue<Coordinate> moveTo = BFS(currentLocation.x, currentLocation.y);
			if(moveTo != null)
			{
				while(!moveTo.isEmpty())
				{
					System.out.println("Current Location");
					currentLocation.print();
					Coordinate nextCoordinate = moveTo.poll();

					System.out.println("Next Location");
					nextCoordinate.print();
					move(nextCoordinate);	
					Thread.sleep((long)(100));
				}
			}
			else
			{
				move();
			}
			Thread.sleep((long)(500));
		}
		
		
	}
	public void move(Coordinate moveToCoordinate)
	{
		int x = moveToCoordinate.x;
		int y = moveToCoordinate.y;
		
		// timer loop
		int xDir = 0;
		int yDir = 0;
		if(currentLocation.x < x)
			xDir = 1;
		else if(currentLocation.x > x)
			xDir = -1;
		if(currentLocation.y < y)
			yDir = 1;
		else if(currentLocation.y > y)
			yDir = -1;

		grid[currentLocation.x][currentLocation.y].color = Color.WHITE;
		currentLocation.x = x + xDir;
		currentLocation.y = y + yDir;
		grid[x][y].color = Color.GREEN;


		
		
	}
	
	public void move() throws InterruptedException
	{
		int x = startLocation.x;
		int y = startLocation.y;
		this.currentLocation = new Coordinate(x, y);
		
		// timer loop
		
		Coordinate[] directions = {new Coordinate(x - 1, y), new Coordinate(x + 1, y), new Coordinate(x, y + 1), new Coordinate(x, y - 1)};
		ArrayList<Coordinate> validNext = new ArrayList<Coordinate>();
		// coordinate arraylist that will add all the valid coordinates that the next step can go to 
		for(int i =0 ; i < directions.length; i++)
		{
			int neighbor_x = directions[i].x;
			int neighbor_y = directions[i].y;
			if(neighbor_x < 0 || neighbor_y < 0 || neighbor_x >= gridSizeX || neighbor_y >= gridSizeY || !grid[neighbor_x][neighbor_y].alive){
				continue;
			}
			validNext.add(directions[i]);
		}
		
		int randomIndex = random.nextInt(validNext.size() - 0) + 0;
		Coordinate randomCoordinate = validNext.get(randomIndex);
		grid[x][y].color = Color.WHITE;
		x = randomCoordinate.x;
		y = randomCoordinate.y;
		
		grid[x][y].color = Color.GREEN;
		
	
		currentLocation.x = x;
		currentLocation.y = y;
			
	}
	
	public void chooseDirection()
	{

	}
	
	
}
