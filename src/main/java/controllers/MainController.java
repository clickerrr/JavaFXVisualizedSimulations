//package main.java.controllers;
//
//import java.util.Arrays;
//
//import classes.Coordinate;
//import classes.NodeType;
//import javafx.event.Event;
//import javafx.fxml.FXML;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.Spinner;
//import javafx.scene.control.SpinnerValueFactory;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//
//public class MainController 
//{
//	@FXML
//	private Canvas canvas;
//	@FXML
//	private Rectangle obstacleNode;
//	@FXML
//	private Rectangle startNode;
//	@FXML
//	private Rectangle endNode;
//	@FXML
//	private Label errorMessage;
//	@FXML
//	private Button calculatePath;
//	
//	@FXML
//	private Spinner<Integer> simStepsSpinner, deathMinSpinner, birthMinSpinner;
//	
//	private static int simulationSteps = 5;
//	private static int birthMinimum = 4;
//	private static int deathMinimum = 2;
//	
//	
//	private NodeType selectedNode;
//	private Coordinate startLocation;
//	private Coordinate endLocation;
//	
//	private boolean startPlaced;
//	private boolean endPlaced;
//	
//	private GraphicsContext gc;
//	private double width;
//	private double height;
//	private int gridSizeX;
//	private int gridSizeY;
//	private double rectWidth;
//	private double rectHeight;
//	
//	private int[][] grid;
//	
//	@FXML
//	public void initialize()
//	{
//		simStepsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 4));
//		simStepsSpinner.increment();
//		
//		deathMinSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
//		deathMinSpinner.increment();
//		
//		birthMinSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 3));
//		birthMinSpinner.increment();
//		
//		
//		this.gc = canvas.getGraphicsContext2D();
//		this.width = canvas.getWidth();
//		this.height = canvas.getHeight();
//		
//		this.gridSizeX = 200;
//		this.gridSizeY = 200;
//
//		grid = new int[gridSizeX][gridSizeY];
//		
//		this.rectWidth = width / gridSizeX;
//		this.rectHeight = height / gridSizeY;
//		
//
//		this.errorMessage.setVisible(false);
//		renderGrid();
//		this.startPlaced = false;
//		this.endPlaced = false;
//		calculatePath.setDisable(true);
//
//		this.grid = new int[gridSizeX][gridSizeY];
//		for(int i = 0; i < gridSizeX; i++)
//		{
//
//			for(int j = 0; j < gridSizeY; j++)
//			{
//				grid[i][j] = 0;
//			}
//		}
//		//printGridArray();
//		
//		
//		System.out.println("Successfully initialized");
//	}
//	
//	@FXML
//	private void handleSpinnerUpdate(Event e)
//	{
//		simulationSteps = simStepsSpinner.getValue();
//		birthMinimum = birthMinSpinner.getValue();
//		deathMinimum = deathMinSpinner.getValue();
//		System.out.println(simulationSteps);
//		System.out.println(birthMinimum);
//		System.out.println(deathMinimum);
//	}
//	
//
//	@FXML
//	private void handleCanvasClick(Event e)
//	{
//		MouseEvent event = (MouseEvent)e;
//
//		double x = event.getX();
//		double y = event.getY();
//	
//		
//		System.out.println(String.format("[%f, %f]", x, y));
//
//		// format x, y
//		Coordinate grid = convertCanvasPosToGrid(x, y);
//
//		System.out.println(String.format("[%d, %d]", grid.getX(), grid.getY()));
//
//		updatePaintAtGridPos(grid.getX(), grid.getY());
//		if (startLocation != null)
//		{
//			startLocation.print();
//		}
//		if (endLocation != null)
//		{
//			endLocation.print();
//		}
//		//printGridArray();
//		
//		if(startPlaced && endPlaced)
//		{
//			calculatePath.setDisable(false);
//		}
//		else
//		{
//			calculatePath.setDisable(true);
//		}
//
//	}
//	
//	@FXML
//	private void handleKeyPress(Event e)
//	{
//		KeyEvent event = (KeyEvent)e;
//		switch(event.getCharacter())
//		{
//			case "1":
//				System.out.println("Hi");
//				break;
//			case "2":
//				System.out.println("Hi 2");
//				break;
//		}
//	}
//	
//	@FXML
//	private void handleNodeSelection(Event e)
//	{
//		MouseEvent event = (MouseEvent)e;
//		Rectangle source = (Rectangle)event.getSource();
//		
//		switch(source.getId())
//		{
//		case "obstacleNode":
//			this.selectedNode = NodeType.OBSTACLE;
//			this.startNode.setStrokeWidth(0);
//			this.endNode.setStrokeWidth(0);
//
//			this.obstacleNode.setStrokeWidth(2);
//			break;
//		case "startNode":
//			this.selectedNode = NodeType.START;
//			this.obstacleNode.setStrokeWidth(0);
//			this.endNode.setStrokeWidth(0);
//			
//			this.startNode.setStrokeWidth(2);
//			break;
//		case "endNode":
//			this.selectedNode = NodeType.END;
//			this.obstacleNode.setStrokeWidth(0);
//			this.startNode.setStrokeWidth(0);
//			
//			this.endNode.setStrokeWidth(2);
//			break;
//		}
//		System.out.println(source.getId());
//	}
//	
//	@FXML
//	private void handleCalculatePath()
//	{
//		
//	}
//	
//	@FXML
//	private void handleGenerateRandomMaze() throws InterruptedException
//	{
//		System.out.println("Generating new grid");
//		renderGrid();
//		this.grid = new int[gridSizeX][gridSizeY];
////		for(int i = 0 ; i < this.grid.length; i++)
////		{
////			for(int j = 0; j < this.grid[i].length; j++)
////			{
////				this.grid[i][j] = 1;
////				gc.setFill(Color.BLACK);
////				gc.fillRect(i * rectWidth, j * rectHeight, rectWidth, rectHeight);
////			}
////		}
//		if(startLocation != null)
//		{
//			gc.setFill(Color.rgb(30, 144, 255));
//			gc.fillRect(startLocation.getX() * rectWidth, startLocation.getY() * rectHeight, rectWidth, rectHeight);
//			grid[startLocation.getX()][startLocation.getY()] = 2;
//		}
//		else
//		{
//			int randomX = (int)Math.floor(Math.random() * (gridSizeX - 1 - 0 + 1) + 0);
//			int randomY = (int)Math.floor(Math.random() * (gridSizeY - 1 - 0 + 1) + 0);
//			if (grid[randomX][randomY] == 0)
//			{
//				grid[randomX][randomY] = 2;
//				startLocation = new Coordinate(randomX, randomY);
//				startPlaced = true;
//				gc.setStroke(Color.BLACK);
//				gc.setFill(Color.rgb(30, 144, 255));
//				gc.fillRect(randomX * rectWidth, randomY * rectHeight, rectWidth, rectHeight);
//			}
//			
//		}
//		if (endLocation != null)
//		{
//			gc.setFill(Color.RED);
//			gc.fillRect(endLocation.getX() * rectWidth, endLocation.getY() * rectHeight, rectWidth, rectHeight);
//			grid[endLocation.getX()][endLocation.getY()] = 3;
//		}
//		else
//		{
//			int randomX = (int)Math.floor(Math.random() * (gridSizeX - 1 - 0 + 1) + 0);
//			int randomY = (int)Math.floor(Math.random() * (gridSizeY - 1 - 0 + 1) + 0);
//			if (grid[randomX][randomY] == 0)
//			{
//				grid[randomX][randomY] = 3;
//				endLocation = new Coordinate(randomX, randomY);
//				endPlaced = true;
//				gc.setStroke(Color.BLACK);
//				gc.setFill(Color.RED);
//				gc.fillRect(randomX * rectWidth, randomY * rectHeight, rectWidth, rectHeight);
//			}
//			
//		}
//		calculatePath.setDisable(false);
//
////		generateMaze(startLocation.getX(), endLocation.getY());
//
//		int startX = startLocation.getX();
//		int startY = startLocation.getY();
//		
//		generateMaze(startX, startY);
//		
//		
//		System.out.println("New grid:");
//		//printGridArray();
//		
//		
//		
//		
//		
//	}
//	
//	// random choice version
////	private void generateMaze(int x, int y)
////	{
////		int fillAmount = (int)(.5 * gridSizeX * gridSizeY);
////		int fillCount = 0;
////		while(fillCount < fillAmount)
////		{
////			int randomX = (int)Math.floor(Math.random() * (gridSizeX - 1 - 0 + 1) + 0);
////			int randomY = (int)Math.floor(Math.random() * (gridSizeY - 1 - 0 + 1) + 0);
////			if (grid[randomX][randomY] == 1)
////				continue;
////			
////			if (grid[randomX][randomY] == 0)
////			{
////				gc.setStroke(Color.BLACK);
////				gc.setFill(Color.BLACK);
////				gc.fillRect(randomX * rectWidth, randomY * rectHeight, rectWidth, rectHeight);
////			}
////			grid[randomX][randomY] = 1;
////			fillCount++;
////			
////		}
////		
////	}
//	
//	
//	
//	// Cellular Automata
//	private void generateMaze(int x, int y) throws InterruptedException
//	{
//		double chanceToStartAlive = .45;
//		
//		for(int i = 0; i < this.grid.length; i++)
//		{
//			for(int j = 0 ; j < this.grid[i].length; j++)
//			{
//				if( grid[i][j] != 2 && grid[i][j] != 3 )
//				{
//					double randomNumber = Math.random();
//					//System.out.println(randomNumber);
//					if(randomNumber <= chanceToStartAlive)
//					{
//						grid[i][j] = 0;
//					}
//					else
//					{
//						grid[i][j] = 1;
//					}
//					
//					if(grid[i][j] == 1)
//					{
//						gc.setFill(Color.BLACK);
//						gc.fillRect(i * rectWidth, j * rectHeight, rectWidth, rectHeight);
//					}
//					
//				}
//			}
//		}
//		for(int i = 0; i < simulationSteps; i++)
//		{
//			System.out.println("Running simulation step");
//			doSimulationStep();
//		}
//
//		
//	}
//	
//	
//	@FXML
//	private void doSimulationStep()
//	{	
//		int[][] newMap = grid;
//		
//		for(int x=0; x<grid.length; x++){
//			for(int y=0; y<grid[0].length; y++){
//				
//				int aliveNeighbors = countAliveNeighbours(newMap, x, y);
//				//The new value is based on our simulation rules 
//				//First, if a cell is alive but has too few neighbours, kill it. 
//				if(grid[x][y] == 0){
//					if(aliveNeighbors <= deathMinimum)
//					{
//						gc.setFill(Color.BLACK);
//						gc.fillRect(x * rectWidth, y * rectHeight, rectWidth, rectHeight);
//						newMap[x][y] = 1;
//	  				}
//	  			} //Otherwise, if the cell is dead now, check if it has the right number of neighbours to be 'born' 
//	  			else if (grid[x][y] == 1){
//	  				if(aliveNeighbors > birthMinimum)
//	  				{
//	  					gc.setFill(Color.WHITE);
//						gc.fillRect(x * rectWidth, y * rectHeight, rectWidth, rectHeight);
////						gc.setStroke(Color.BLACK);
////						gc.strokeRect(x * rectWidth, y * rectHeight, rectWidth, rectHeight);
//	  					newMap[x][y] = 0;
//					}
//				}
//			}
//		}
////		System.out.println("---------------SIMULATION STEP COMPARISON");
////		for(int i = 0; i < this.grid.length; i++)
////		{
////			System.out.println(Arrays.toString(newMap[i]));
////		}
////		System.out.println();
////		printGridArray();
//		grid = newMap;
//		
//	}
//	
//	private int countAliveNeighbours(int[][] map, int x, int y){
//		int count = 0;
//		for(int i=-1; i<2; i++){
//			for(int j=-1; j<2; j++){
//				int neighbour_x = x+i;
//				int neighbour_y = y+j;
//				//If we're looking at the middle point 
//				if(i == 0 && j == 0){
//					//Do nothing, we don't want to add ourselves in! 
//				}
//				//In case the index we're looking at it off the edge of the map 
//				else if(neighbour_x < 0 || neighbour_y < 0 || neighbour_x >= map.length || neighbour_y >= map[0].length){
////					count = count + 1;
//				}
//				//Otherwise, a normal check of the neighbour 
//				else if(map[neighbour_x][neighbour_y] == 0){
//					count = count + 1;
//				}
//			}
//		}
//		return count;
//	}
//	
//	private void renderGrid()
//	{
//		if (this.gc == null)
//			return;
//		
//		double xpos = 0;
//		double ypos = 0;
//		
//		for(int i = 1; i <= gridSizeX * gridSizeY; i++)
//		{
//			Rectangle r = new Rectangle();
////			r.setX(xpos);
////			r.setY(ypos);
////			r.setWidth(rectWidth);
////			r.setHeight(rectHeight);
////			r.setFill(Color.WHITE);
////			r.setStroke(Color.BLACK);
//			
//			gc.setFill(Color.WHITE);
//			gc.fillRect(xpos, ypos, rectWidth, rectHeight);
////			gc.setStroke(Color.BLACK);
////			gc.strokeRect(xpos, ypos, rectWidth, rectHeight);
//			xpos += rectWidth;
//			if (i % gridSizeX == 0)
//			{
//				xpos = 0;
//				ypos += rectHeight;
//			}
//		}
//	
//	}
//	
//	private Coordinate convertCanvasPosToGrid(double x, double y)
//	{
//
//		Coordinate gridPos = new Coordinate((int)(x / rectWidth), (int)(y / rectHeight));
//		return gridPos;
//	}
//	
//	private void updatePaintAtGridPos(int x, int y)
//	{
//		
//		double drawX = x * rectWidth;
//		double drawY = y * rectHeight;
//		if (this.grid[x][y] == 1)
//		{
//			errorMessage.setVisible(false);
//			this.grid[x][y] = 0;
//			gc.setFill(Color.WHITE);
//			gc.fillRect(drawX, drawY, rectWidth, rectHeight);
////			gc.setStroke(Color.BLACK);
////			gc.strokeRect(drawX, drawY, rectWidth, rectHeight);
//		}
//		else if(this.grid[x][y] == 2)
//		{
//			errorMessage.setVisible(false);
//			this.startPlaced = false;
//			this.grid[x][y] = 0;
//			this.startLocation = null;
//			
//			gc.setFill(Color.WHITE);
//			gc.fillRect(drawX, drawY, rectWidth, rectHeight);
////			gc.setStroke(Color.BLACK);
////			gc.strokeRect(drawX, drawY, rectWidth, rectHeight);
//		}
//		else if(this.grid[x][y] == 3)
//		{
//			errorMessage.setVisible(false);
//			this.endPlaced = false;
//			this.endLocation = null;
//			this.grid[x][y] = 0;
//			gc.setFill(Color.WHITE);
//			gc.fillRect(drawX, drawY, rectWidth, rectHeight);
////			gc.setStroke(Color.BLACK);
////			gc.strokeRect(drawX, drawY, rectWidth, rectHeight);
//		}
//		else
//		{
//			switch(selectedNode)
//			{
//				case START:
//					
//					if (this.startPlaced)
//					{
//						errorMessage.setText("A start node has already been placed");
//						errorMessage.setVisible(true);
//						return;
//					}
//					errorMessage.setVisible(false);
//					gc.setFill(Color.rgb(30, 144, 255));
//					this.startLocation = new Coordinate(x, y);
//					this.grid[x][y] = 2;
//					this.startPlaced = true;
//					
//					break;
//				case OBSTACLE:
//					errorMessage.setVisible(false);
//					gc.setFill(Color.BLACK);
//					this.grid[x][y] = 1;
//					break;
//				case END:
//					if (this.endPlaced)
//					{
//						errorMessage.setText("An end node has already been placed");
//						errorMessage.setVisible(true);
//						return;
//					}
//					errorMessage.setVisible(false);
//					this.endLocation = new Coordinate(x,y);
//					gc.setFill(Color.RED);
//					this.endPlaced = true;
//					this.grid[x][y] = 3;
//					break;
//				default:
//					gc.setFill(Color.BLACK);
//					break;
//				
//			}
//
//			gc.fillRect(drawX, drawY, rectWidth, rectHeight);
//		}
//				
//				
//		
//	}
//	
//	private void printGridArray()
//	{
//		for(int i = 0; i < this.grid.length; i++)
//		{
//			System.out.println(Arrays.toString(this.grid[i]));
//		}
//	}
//	
//	
//
//
//}
//	