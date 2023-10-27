package main.java.controllers;

import java.util.ArrayList;

import classes.AnimatedBFS;
import classes.AnimatedCaveGeneration;
import classes.AnimatedCreep;
import classes.AnimatedDFS;
import classes.CanvasCoordinate;
import classes.Coordinate;
import classes.GridElement;
import classes.NodeType;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AnimationTestController 
{
	@FXML 
	private Canvas canvas;
	@FXML
	private Button showDfsButton;
	@FXML
	private Button showBfsButton;
	@FXML
	private Button antButton;
	
	@FXML
	private CheckBox wallBreaker;
	
	@FXML
	private Rectangle obstacleNode, redNode;
	
	private ArrayList<Node> disabledNodeList;
	
	private final int MAX_THREADS = 50;	
	private ArrayList<Thread> activeThreads;
	private NodeType selectedNode;
	private NodeType obstacleNodeInfo = new NodeType("obstacle", Color.BLACK);
	private NodeType redNodeInfo = new NodeType("red", Color.RED);
	
	public GridController gridController;
	
	
	@FXML
	private Spinner<Integer> simStepsSpinner, deathMinSpinner, birthMinSpinner;

	
	@FXML
	public void initialize()
	{
		selectedNode = obstacleNodeInfo;
		disabledNodeList = new ArrayList<Node>();
		disabledNodeList.add(showDfsButton);
		disabledNodeList.add(showBfsButton);
		disabledNodeList.add(antButton);
		disabledNodeList.add(wallBreaker);
		
		simStepsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 4));
		simStepsSpinner.increment();
		
		deathMinSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
		deathMinSpinner.increment();
		
		birthMinSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 3));
		birthMinSpinner.increment();
		
		this.gridController = GridController.getInstance();
		
		gridController.setCanvasWidth(canvas.getWidth());
		gridController.setCanvasHeight(canvas.getHeight());



        GridElement[][] grid = new GridElement[GridController.GRID_SIZE_X][GridController.GRID_SIZE_Y];
		for(int y = 0; y < GridController.GRID_SIZE_Y; y++)
		{
			for(int x = 0; x < GridController.GRID_SIZE_X; x++)
			{
				CanvasCoordinate canvasCoor = new CanvasCoordinate( (double)x * gridController.getRectWidth(),
				(double)y * gridController.getRectHeight());
				grid[x][y] = new GridElement(x,y,Color.WHITE, true, canvasCoor);
			}
		}
		
		gridController.setGrid(grid);
		
		activeThreads = new ArrayList<Thread>();

		gridController.setGraphicsContext(canvas.getGraphicsContext2D());
        
		AnimationTimer timer = new AnimationTimer() {
	        @Override
	        public void handle(long now) 
	        {
	            
	        	gridController.refreshGrid();
	        	
	        }
	    };
	    timer.start();
    
	}
	
	@FXML
	private void handleSpinnerUpdate(Event e)
	{
		AnimatedCaveGeneration.simulationSteps = simStepsSpinner.getValue();
		AnimatedCaveGeneration.birthMinimum = birthMinSpinner.getValue();
		AnimatedCaveGeneration.deathMinimum = deathMinSpinner.getValue();
	}
	
	@FXML
	private void generateObstacles(Event e) throws InterruptedException
	{
		interruptAllThreads();
		AnimatedCaveGeneration animatedFill = new AnimatedCaveGeneration("Thread 1");
		animatedFill.start();	
		activeThreads.add(animatedFill);

		disabledNodeList.forEach( (disabledNode) -> { if(disabledNode.isDisable()) {disabledNode.setDisable(false); } });
	}
	
	@FXML
	private void handleCanvasClick(Event e)
	{
		MouseEvent event = (MouseEvent)e;

		double x = event.getX();
		double y = event.getY();
	
		if(wallBreaker.isSelected())
		{
			
			Coordinate grid = gridController.convertCanvasPosToGrid(x, y);
			if( grid != null)
			{
				gridController.wallBreaker(grid.x, grid.y);
			}
		}
		
	}
	
	@FXML
	private void handleCanvasDrag(Event e)
	{
		MouseEvent event = (MouseEvent)e;

		double x = event.getX();
		double y = event.getY();


		Coordinate grid = gridController.convertCanvasPosToGrid(x, y);
		if( grid != null)
		{
			gridController.painter(grid.x, grid.y, selectedNode.nodeColor);	
		}

	}
	

	@FXML
	private void handleNodeSelection(Event e)
	{
		MouseEvent event = (MouseEvent)e;
		Rectangle source = (Rectangle)event.getSource();
		
		switch(source.getId())
		{
			case "obstacleNode":
				this.selectedNode = obstacleNodeInfo;
	
				this.redNode.setStrokeWidth(0);
	
				this.obstacleNode.setStrokeWidth(2);
				break;
			case "redNode":
				this.selectedNode = redNodeInfo;
				this.obstacleNode.setStrokeWidth(0);
				this.redNode.setStrokeWidth(2);
				break;
		}
		System.out.println(source.getId());
	}
	
	@FXML
	private void showDFS(Event e)
	{
		interruptAllThreads();
		gridController.getStartLocation().print();
		gridController.getStartLocation().print();
		AnimatedDFS animatedDFS = new AnimatedDFS("DFS Thread");
		animatedDFS.start();
		activeThreads.add(animatedDFS);
	}
	
	@FXML
	private void showBFS(Event e)
	{
		interruptAllThreads();
		AnimatedBFS animatedBFS = new AnimatedBFS("BFS Thread");
		animatedBFS.start();
		activeThreads.add(animatedBFS);
	}
	
	@FXML
	private void ant(Event e)
	{
		if(activeThreads.size() <= MAX_THREADS)
		{
			AnimatedCreep animatedCreep = new AnimatedCreep("Ant");
			animatedCreep.start();
			activeThreads.add(animatedCreep);
		}
		else
		{
			antButton.setDisable(true);
		}
	}
	
	public void interruptAllThreads()
	{
		System.out.println("Interrupting all threads");
		activeThreads.forEach(thread -> { thread.interrupt(); });
	}
	
	@FXML
	public void closeRequested(Event e)
	{
		interruptAllThreads();
	}
	
	
	
	
	
	
	
}
