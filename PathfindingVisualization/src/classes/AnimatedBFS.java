package classes;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javafx.scene.control.Cell;
import javafx.scene.paint.Color;

public class AnimatedBFS extends Thread
{
	private String threadName;
	
	private GridElement[][] grid;
	private int gridSizeX;
	private int gridSizeY;
	
	public Coordinate startLocation;
	public Coordinate endLocation;
	
	private Random random;
	
	public AnimatedBFS(String threadName, GridElement[][] grid, int gridSizeX, int gridSizeY, Coordinate startLocation, Coordinate endLocation)
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
			BFS(startLocation.x, startLocation.y);
		} 
		catch (InterruptedException e) 
		{
			System.out.println(String.format("[%s]: %s", threadName, e.getMessage()));
		}
	}
	
	public void BFS(int startX, int startY) throws InterruptedException 
	{
		boolean[][] visited = new boolean[gridSizeX][gridSizeY];
	      
	   // Define the directions for exploring neighbors (up, down, left, right)
		int[] dr = {0, 0, 1, -1};
		int[] dc = {1, -1, 0, 0};
	   
	   Queue<Coordinate> queue = new LinkedList<>();
	   
	   queue.add(new Coordinate(startX, startY));
	   visited[startX][startY] = true;
	   
	   while (!queue.isEmpty()) 
	   {
			Coordinate currentCell = queue.poll();
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
					queue.add(new Coordinate(ni, nj));
//					grid[ni][nj].color = Color.rgb(255, 0, 102);
					grid[ni][nj].color = Color.RED;
					visited[ni][nj] = true;
				}
			}
			Thread.sleep((long)1.0);
		}

	}
	
	
}
