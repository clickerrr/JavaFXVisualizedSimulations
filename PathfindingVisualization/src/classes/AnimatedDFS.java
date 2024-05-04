package classes;

import java.util.Random;

import javafx.scene.paint.Color;

public class AnimatedDFS extends Thread
{
	private String threadName;
	
	private GridElement[][] grid;
	private int gridSizeX;
	private int gridSizeY;
	
	public Coordinate startLocation;
	public Coordinate endLocation;
	
	private Random random;
	
	public AnimatedDFS(String threadName, GridElement[][] grid, int gridSizeX, int gridSizeY, Coordinate startLocation, Coordinate endLocation)
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
			DFS();
		}
		catch (InterruptedException e) 
		{
			System.out.println(String.format("[%s]: %s", threadName, e.getMessage()));
		}
	}
	
	public void DFS() throws InterruptedException 
	{
        //created visited array
        boolean [][] visited = new boolean[gridSizeX][gridSizeY];
        System.out.println(String.format("[%s] Running Depth-First Search", threadName));
        DFSUtil(startLocation.x, startLocation.y, visited);
    }

    public void DFSUtil(int x, int y, boolean[][] visited) throws InterruptedException
    {

    	int W = gridSizeX;
        int H = gridSizeY;

        if (x < 0 || y < 0 || x >= W || y >= H || visited[x][y] || !grid[x][y].alive || new Coordinate(x,y).equals(endLocation))
            return;

        //mark the cell visited
        visited[x][y] = true;

//		grid[x][y].color = Color.rgb(255, 0, 102);
		grid[x][y].color = Color.RED;
        Thread.sleep((long)1);
//        System.out.print(grid[x][y] + " ");
        DFSUtil(x+ 1, y,visited); // go right
        DFSUtil(x- 1, y,visited); //go left
        DFSUtil(x, y + 1,visited); //go down
        DFSUtil(x, y - 1,visited); // go up
    }
	
	
}
