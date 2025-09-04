package classes;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javafx.scene.paint.Color;
import main.java.controllers.GridController;

public class AnimatedBFS extends Thread
{
	private String threadName;
	private GridController gridController;
	private Random random;
	
	public AnimatedBFS(String threadName)
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
			BFS(gridController.getStartLocation().x, gridController.getStartLocation().y);
		} 
		catch (InterruptedException e) 
		{
			System.out.println(String.format("[%s]: %s", threadName, e.getMessage()));
		}
	}
	
	public void BFS(int startX, int startY) throws InterruptedException 
	{
		int gridSizeX = GridController.GRID_SIZE_X;
		int gridSizeY = GridController.GRID_SIZE_Y;
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
				if (ni >= 0 && ni < gridSizeX && nj >= 0 && nj < gridSizeY && !visited[ni][nj] && gridController.getCellStatus(ni, nj)) 
				{ 
					queue.add(new Coordinate(ni, nj));
//					grid[ni][nj].color = Color.rgb(255, 0, 102);
					
					gridController.updateCell(ni, nj, Color.RED);
					visited[ni][nj] = true;
				}
			}
			Thread.sleep((long)1.0);
		}

	}
	
	
}
