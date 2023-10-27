package classes;

import java.util.Random;

import javafx.scene.paint.Color;
import main.java.controllers.GridController;

public class AnimatedDFS extends Thread
{
	private String threadName;
	private GridController gridController;
	private Random random;
	
	public AnimatedDFS(String threadName)
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
        boolean [][] visited = new boolean[gridController.GRID_SIZE_X][gridController.GRID_SIZE_Y];
        System.out.println(String.format("[%s] Running Depth-First Search", threadName));
        DFSUtil(gridController.getStartLocation().x, gridController.getStartLocation().y, visited);
    }

    public void DFSUtil(int x, int y, boolean[][] visited) throws InterruptedException
    {

    	

        if (!gridController.confirmValidCoordinate(x, y) || visited[x][y] || !gridController.getCellStatus(x, y) || new Coordinate(x,y).equals(gridController.getEndLocation()))
            return;

        //mark the cell visited
        visited[x][y] = true;

//		grid[x][y].color = Color.rgb(255, 0, 102);
		gridController.updateCell(x, y, Color.RED);
        Thread.sleep((long)1);
//        System.out.print(grid[x][y] + " ");
        DFSUtil(x+ 1, y,visited); // go right
        DFSUtil(x- 1, y,visited); //go left
        DFSUtil(x, y + 1,visited); //go down
        DFSUtil(x, y - 1,visited); // go up
    }
	
	
}
