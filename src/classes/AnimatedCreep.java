package classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import javafx.scene.paint.Color;
import main.java.controllers.GridController;

public class AnimatedCreep extends Thread 
{
	private String threadName;
	
	private GridController gridController;
	
	private Coordinate currentLocation;
	
	private Random random;
	
	public AnimatedCreep(String threadName)
	{
		this.threadName = threadName;
		this.gridController = GridController.getInstance();
		this.random = new Random();
		this.currentLocation = new Coordinate(gridController.getStartLocation().x, gridController.getStartLocation().y);
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

	public Stack<Coordinate> BFS(int startX, int startY) throws InterruptedException 
	{
		int gridSizeX = GridController.GRID_SIZE_X;
		int gridSizeY = GridController.GRID_SIZE_Y;
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
	   
	   Stack<Coordinate> traverseStack = new Stack<Coordinate>();
	   weight[startX][startY] = 0;
	   int currentWeight;
	   
	   while (!queue.isEmpty()) 
	   {
			Coordinate currentCell = queue.poll();
			traverseStack.push(currentCell);
			
			int x =(int)currentCell.x;
			int y = (int)currentCell.y;
			  
			currentWeight = weight[x][y];
			//	      System.out.println("Visiting cell at (" + i + ", " + j + ")"); // Print the visited cell
			  
			for (int k = 0; k < 4; k++) 
			{ // Four directions: up, down, left, right
				int ni = x + dr[k];
				int nj = y + dc[k];
				
				  // Check if the new cell (ni, nj) is within the grid boundaries
				if (gridController.confirmValidCoordinate(ni, nj) && weight[ni][nj] == -1 && gridController.getCellStatus(ni, nj)) 
				{ 
					
					weight[ni][nj] = currentWeight + 1;
					// found a red
					if(gridController.getCellColor(ni, nj) == Color.RED)
					{
						return backTrackPath(new Coordinate(ni, nj), new Coordinate(startX, startY), weight);
					}
					// return closest red coordinate
					queue.add(new Coordinate(ni, nj));
				}
			}

		}
	   
	   return null;

	}
	
	public Stack<Coordinate> backTrackPath(Coordinate source, Coordinate target, int[][] weightMap)
	{
		Coordinate currentCoordinate = source;
		Stack<Coordinate> returnStack = new Stack<Coordinate>();

		while(!currentCoordinate.equals(target))
		{
			int x = currentCoordinate.x;
			int y = currentCoordinate.y;
			int[] dr = {0, 0, 1, -1};
			int[] dc = {1, -1, 0, 0};

			returnStack.push(currentCoordinate);

			Coordinate newCoordinate = currentCoordinate;
			
			for (int k = 0; k < 4; k++) 
			{ // Four directions: up, down, left, right
				int nx = x + dr[k];
				int ny = y + dc[k];

				if(gridController.confirmValidCoordinate(nx, ny) && weightMap[x][ny] != -1 && weightMap[nx][ny] != -1 && weightMap[newCoordinate.x][newCoordinate.y] > weightMap[nx][ny])
				{
					newCoordinate = new Coordinate(nx,ny);
				}
				
			}
			currentCoordinate = newCoordinate;
			
		}
		return returnStack;
	}
	
	public void updateLocation() throws InterruptedException
	{

		while(true)
		{
			Stack<Coordinate> weightMap = BFS(currentLocation.x, currentLocation.y);
			if(weightMap != null)
			{
				traverseWeight(weightMap);
			}
			else
			{
				move();
			}
			Thread.sleep((long)(50));
		}
		
		
	}
	
	public void traverseWeight(Stack<Coordinate> weightMap)
	{
		
		Coordinate moveCoordinate = weightMap.pop();
		
		int x = moveCoordinate.x;
		int y = moveCoordinate.y;
		
		gridController.updateCell(currentLocation.x, currentLocation.y, Color.WHITE);
		gridController.updateCell(x, y, Color.GREEN);	
	
		currentLocation.x = x;
		currentLocation.y = y;
		
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
			if(!gridController.confirmValidCoordinate(neighbor_x, neighbor_y) || !gridController.getCellStatus(neighbor_x, neighbor_y)){
				continue;
			}
			validNext.add(directions[i]);
		}
		
		int randomIndex = random.nextInt(validNext.size() - 0) + 0;
		Coordinate randomCoordinate = validNext.get(randomIndex);
		gridController.updateCell(x, y, Color.WHITE);
		x = randomCoordinate.x;
		y = randomCoordinate.y;
		
		gridController.updateCell(x, y, Color.GREEN);
	
		currentLocation.x = x;
		currentLocation.y = y;
			
	}
	
}
