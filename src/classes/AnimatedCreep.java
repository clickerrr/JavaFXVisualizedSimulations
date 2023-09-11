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
		this.currentLocation = new Coordinate(startLocation.x, startLocation.y);
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

	public int[][] BFS(int startX, int startY) throws InterruptedException 
	{
		int[][] weight = new int[gridSizeX][gridSizeY];
		for(int i = 0; i < gridSizeY; i++)
		{
			for(int j = 0; j < gridSizeX; j++)
			{
				weight[i][j] = -1;
			}
		}
		
		// have a weight map setup from the origin position
		// weight at starting is 0
		// weight in the surrounding is incremented by one, if it does not already have a weight
		// keep going with the bfs and return the weight map
		
		
	   // Define the directions for exploring neighbors (up, down, left, right)
		int[] dr = {0, 0, 1, -1};
		int[] dc = {1, -1, 0, 0};
	   
	   Queue<Coordinate> queue = new LinkedList<>();
	   
	   queue.add(new Coordinate(startX, startY));
	   weight[startX][startY] = 0;
	   int currentWeight;
	   
	   while (!queue.isEmpty()) 
	   {
			Coordinate currentCell = queue.poll();
			
			int x =(int)currentCell.x;
			int y = (int)currentCell.y;
			  
			currentWeight = weight[x][y];
			//	      System.out.println("Visiting cell at (" + i + ", " + j + ")"); // Print the visited cell
			  
			for (int k = 0; k < 4; k++) 
			{ // Four directions: up, down, left, right
				int ni = x + dr[k];
				int nj = y + dc[k];
				
				  // Check if the new cell (ni, nj) is within the grid boundaries
				if (ni >= 0 && ni < gridSizeX && nj >= 0 && nj < gridSizeY && weight[ni][nj] == -1 && grid[ni][nj].alive) 
				{ 
					
					weight[ni][nj] = currentWeight + 1;
					// found a red

					if(grid[ni][nj].color == Color.RED)
					{
						return weight;
					}
					// return closest red coordinate
					queue.add(new Coordinate(ni, nj));
				}
			}

		}
	   
	   return null;

	}
	
	public void updateLocation() throws InterruptedException
	{


		while(true)
		{
			int[][] weightMap = BFS(currentLocation.x, currentLocation.y);
			if(weightMap != null)
			{
				System.out.println("Weighted");
				for(int y = 0; y < weightMap.length; y++)
				{
					System.out.print("[ ");
					for(int x = 0; x < weightMap[y].length; x++)
					{
						if(x == currentLocation.x && y == currentLocation.y)
						{
							System.out.print("X ");
						}
						else
						{
							System.out.print(weightMap[x][y] + " ");
						}
					}
					System.out.println("]");
				}
				traverseWeight(weightMap);
			}
			else
			{
				System.out.println("Random");
				move();
			}
			Thread.sleep((long)(100));
		}
		
		
	}
	public void traverseWeight(int[][] weightMap)
	{
		int x = currentLocation.x;
		int y = currentLocation.y;
		
		int[] dr = {0, 0, 1, -1};
		int[] dc = {1, -1, 0, 0};
		
		ArrayList<Coordinate> adjacentList = new ArrayList<Coordinate>();
		
		for (int k = 0; k < 4; k++) 
		{
			int ni = x + dr[k];
			int nj = y + dc[k];
			if (ni >= 0 && ni < gridSizeX && nj >= 0 && nj < gridSizeY && weightMap[ni][nj] != -1 && grid[ni][nj].alive) 
			{
				adjacentList.add(new Coordinate(ni, nj));
			}
		}
		
		Coordinate lowestWeight = adjacentList.get(0);
		ArrayList<Coordinate> lowestWeightList = new ArrayList<Coordinate>();
		lowestWeightList.add(lowestWeight);
		for(int i = 1; i < adjacentList.size(); i++) 
		{
			if(weightMap[lowestWeight.x][lowestWeight.y] > weightMap[adjacentList.get(i).x][adjacentList.get(i).y])
			{
				lowestWeightList.clear();
				lowestWeight = adjacentList.get(i);
				lowestWeightList.add(lowestWeight);
			}
			else
			{
				lowestWeightList.add(adjacentList.get(i));
			}
		}
//		for (int k = 0; k < 4; k++) 
//		{ // Four directions: up, down, left, right
//			int ni = x + dr[k];
//			int nj = y + dc[k];
//			
//			  // Check if the new cell (ni, nj) is within the grid boundaries
//			if (ni >= 0 && ni < gridSizeX && nj >= 0 && nj < gridSizeY && weightMap[ni][nj] != -1 && grid[ni][nj].alive) 
//			{ 
//				if(weightMap[ni][nj] < weightMap[lowestWeight.x][lowestWeight.y])
//				{
//					lowestWeight.x = ni;
//					lowestWeight.y = nj;
//				}
//				// found a red
//				// return closest red coordinate
//			}
//		}
		int randomIndex = random.nextInt(lowestWeightList.size() - 0) + 0;
		Coordinate randomCoordinate = lowestWeightList.get(randomIndex);
		
		grid[x][y].color = Color.WHITE;
		x = randomCoordinate.x;
		y = randomCoordinate.y;
		
		grid[x][y].color = Color.GREEN;
		
	
		currentLocation.x = x;
		currentLocation.y = y;
		
		// timer loop
//		int xDir = 0;
//		int yDir = 0;
//		if(currentLocation.x < x)
//			xDir = 1;
//		else if(currentLocation.x > x)
//			xDir = -1;
//		if(currentLocation.y < y)
//			yDir = 1;
//		else if(currentLocation.y > y)
//			yDir = -1;
//
//		grid[currentLocation.x][currentLocation.y].color = Color.WHITE;
//		currentLocation.x = x + xDir;
//		currentLocation.y = y + yDir;
//		grid[x][y].color = Color.GREEN;		
		
	}
	
	public void move() throws InterruptedException
	{
		int x = currentLocation.x;
		int y = currentLocation.y;
		
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
	
}
