package main.java.controllers;

import classes.Coordinate;
import classes.EndingGridElement;
import classes.GridElement;
import classes.StartingGridElement;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 * <h1>GridController</h1>
 * {@link classes.Coordinate}
 *  
 */
public class GridController 
{

	private static GridController INSTANCE;
	
	private double canvasWidth = 0;
	private double canvasHeight = 0;

	private GraphicsContext graphicsContext;


	public static final int GRID_SIZE_X = 250;
	public static final int GRID_SIZE_Y = 250;
	
	private double RECT_WIDTH = canvasWidth / GRID_SIZE_X;
	private double RECT_HEIGHT = canvasHeight / GRID_SIZE_Y;

	private GridElement[][] grid;
	
	private StartingGridElement startLocation;
	private EndingGridElement endLocation;
	
	// i want the grid and the starting locations to be updated and stuff like that
	// have everyone reference this class and then update stuff through its methods
	private GridController() {}
	
	public static GridController getInstance()
	{
		if(INSTANCE == null)
		{
			INSTANCE =  new GridController();
		}
		return INSTANCE;
	}
	public double getCanvasWidth()
	{
		return this.canvasWidth;
	}
	public double getCanvasHeight()
	{
		return this.canvasHeight;
	}
	public GraphicsContext getGraphicsContext()
	{
		return this.graphicsContext;
	}
	public StartingGridElement getStartLocation()
	{
		return this.startLocation;
	}
	public EndingGridElement getEndLocation()
	{
		return this.endLocation;
	}
	
	public boolean setGrid(GridElement[][] grid)
	{
		if(grid == null)
			return false;
		this.grid = grid;
		return true;
	}
	
	public void resetGrid()
	{
		this.grid = null;
	}
	
	public boolean setStartLocation(StartingGridElement start)
	{
		if(start == null)
			return false;
		this.startLocation = start;
		updateCell( start.x, start.y, start.color, start.alive);
		return true;
	}
	
	public boolean setEndLocation(EndingGridElement end)
	{
		if(end == null)
			return false;
		this.endLocation = end;
		updateCell( end.x, end.y, end.color, end.alive );
		return true;
	}
	public boolean resetStartLocation()
	{
		this.startLocation.x = -1;
		this.startLocation.y = -1;
		return true;
	}
	public boolean resetEndLocation()
	{
		this.endLocation.x = -1;
		this.endLocation.y = -1;
		return true;
	}
	public double getRectWidth() 
	{
		return RECT_WIDTH;
	}

	public double getRectHeight() 
	{
		return RECT_HEIGHT;
	}

	public void setCanvasWidth(double canvasWidth) 
	{		
		if(canvasWidth > 0)
		{
			this.canvasWidth = canvasWidth;
			this.RECT_WIDTH = canvasWidth / GRID_SIZE_X;
		}
	}

	public void setCanvasHeight(double canvasHeight) 
	{
		if(canvasHeight > 0)
		{
			this.canvasHeight = canvasHeight;
			RECT_HEIGHT = canvasHeight / GRID_SIZE_Y;
		}
	}

	public void setGraphicsContext(GraphicsContext graphicsContext) 
	{
		if(graphicsContext != null)
			this.graphicsContext = graphicsContext;
	}
	
	/*
	 * 
	 * Functionality Methods
	 * 
	 */
	
	public boolean confirmValidCoordinate(int x, int y)
	{
		if( x < 0  || x >= GRID_SIZE_X)
			return false;
		if( y < 0  || y >= GRID_SIZE_Y)
			return false;
		return true;
	}
	
	public boolean updateCell(int x, int y, Color color, boolean alive)
	{
		if(!confirmValidCoordinate(x, y))
			return false;
		if( color == null )
			return false;
		
		grid[x][y].color = color;
		grid[x][y].alive = alive;
		
		return true;
	}
	
	public boolean updateCell(int x, int y, Color color)
	{
		if(!confirmValidCoordinate(x, y))
			return false;
		if( color == null )
			return false;
		
		grid[x][y].color = color;
		
		
		return true;
	}
	public boolean updateCell(int x, int y, boolean alive)
	{
		if(!confirmValidCoordinate(x, y))
			return false;
		
		grid[x][y].alive = alive;
		
		return true;
	}
	
	public GridElement getCell(int x, int y)
	{
		if(!confirmValidCoordinate(x,y))
			throw new IndexOutOfBoundsException("Ensure the x and y coordinates are within the grid's bounds.");
		return new GridElement(grid[x][y]);
	}
	
	public boolean getCellStatus(int x, int y)
	{
		if(!confirmValidCoordinate(x,y))
			throw new IndexOutOfBoundsException("Ensure the x and y coordinates are within the grid's bounds.");
		return grid[x][y].alive;
	}
	
	public Color getCellColor(int x, int y)
	{
		if(!confirmValidCoordinate(x,y))
			throw new IndexOutOfBoundsException("Ensure the x and y coordinates are within the grid's bounds.");
		return grid[x][y].color;
	}
	
	public GridElement[][] getCopyOfGrid()
	{
		return grid.clone();
	}
	
	public void refreshGrid()
	{
		graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
		if(startLocation != null)
			updateCell( startLocation.x, startLocation.y, startLocation.color, startLocation.alive);
		
		for(int y = 0; y < GRID_SIZE_Y; y++)
		{
			for(int x = 0; x < GRID_SIZE_X; x++)
			{
				
				graphicsContext.setFill(grid[x][y].color);

				graphicsContext.fillRect(grid[x][y].x * RECT_WIDTH, grid[x][y].y * RECT_HEIGHT, RECT_WIDTH, RECT_HEIGHT);
			}
		}
	}

	public void wallBreaker(int x, int y)
	{
		if(!confirmValidCoordinate(x,y))
			return;
		if(!grid[x][y].alive)
		{
			grid[x][y].alive = true;
			grid[x][y].color = Color.WHITE;
		}
	}
	
	public void painter(int x, int y, Color nodeColor)
	{
		if(!confirmValidCoordinate(x,y))
			return;
		if(!grid[x][y].color.equals(nodeColor) && grid[x][y].alive)
		{
			grid[x][y].color = nodeColor;
			if(grid[x][y].color.equals(Color.BLACK))
			{
				grid[x][y].alive = false;
			}
		}

	}
	
	public Coordinate convertCanvasPosToGrid(double x, double y)
	{
		Coordinate gridPos = new Coordinate((int)(x / RECT_WIDTH), (int)(y / RECT_HEIGHT));
		if( !confirmValidCoordinate(gridPos.x, gridPos.y) )
			return null;
		return gridPos;
	}

	public void printGrid()
	{
		for(int y= 0; y < GRID_SIZE_Y; y++)
		{
			System.out.print("[ ");
			for(int x = 0; x < GRID_SIZE_X; x++)
			{
				if(!grid[x][y].alive)
					System.out.print("1 ");
				else
					System.out.print("0 ");
			}

			System.out.println("]");
		}
		
	}
	
}
