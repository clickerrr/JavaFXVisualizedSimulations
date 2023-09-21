package main.java.controllers;

import java.util.ArrayList;
import java.util.Random;

import classes.AnimatedBFS;
import classes.AnimatedCaveGeneration;
import classes.AnimatedCreep;
import classes.AnimatedDFS;
import classes.Coordinate;
import classes.GridElement;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;

public class AnimationTestController 
{
	@FXML 
	private Canvas canvas;
	
	private double W;
	private double H;
	
	private GraphicsContext gc;

	private double rectWidth;
	private double rectHeight;
	
	private int gridSizeX;
	private int gridSizeY;

	private GridElement[][] grid;
	private Coordinate startLocation;
	private Coordinate endLocation;
	
	private ArrayList<Thread> activeThreads;
	
	private Random random;
	
	@FXML
	private Spinner<Integer> simStepsSpinner, deathMinSpinner, birthMinSpinner;

	
	@FXML
	public void initialize()
	{
		
		simStepsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 4));
		simStepsSpinner.increment();
		
		deathMinSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
		deathMinSpinner.increment();
		
		birthMinSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 3));
		birthMinSpinner.increment();
		
		this.random = new Random();
		this.W = canvas.getWidth();
        this.H = canvas.getHeight();
        
		gridSizeX = 50;
		gridSizeY = 50;

		rectWidth = W / gridSizeX;
		rectHeight = H / gridSizeY;
		
		grid = new GridElement[gridSizeX][gridSizeY];
		for(int y = 0; y < gridSizeY; y++)
		{
			for(int x = 0; x < gridSizeX; x++)
			{
				grid[x][y] = new GridElement(x,y,Color.WHITE, true);
			}
		}
		
		activeThreads = new ArrayList<Thread>();
		
		this.startLocation = new Coordinate();
		this.endLocation = new Coordinate();
		
		this.gc = canvas.getGraphicsContext2D();
        
		AnimationTimer timer = new AnimationTimer() {
	        @Override
	        public void handle(long now) {
	            
	        	refreshGrid();
	        	
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
		AnimatedCaveGeneration animatedFill = new AnimatedCaveGeneration("Thread 1", grid, gridSizeX, gridSizeY, startLocation, endLocation);
		animatedFill.start();	
		activeThreads.add(animatedFill);
	}
	
	@FXML
	private void showDFS(Event e)
	{
		interruptAllThreads();
		startLocation.print();
		endLocation.print();
		AnimatedDFS animatedDFS = new AnimatedDFS("DFS Thread", grid, gridSizeX, gridSizeY, startLocation, endLocation);
		animatedDFS.start();
		activeThreads.add(animatedDFS);
	}
	
	@FXML
	private void showBFS(Event e)
	{
		interruptAllThreads();
		AnimatedBFS animatedBFS = new AnimatedBFS("DFS Thread", grid, gridSizeX, gridSizeY, startLocation, endLocation);
		animatedBFS.start();
		activeThreads.add(animatedBFS);
	}
	
	@FXML
	private void ant(Event e)
	{
		AnimatedCreep animatedCreep = new AnimatedCreep("Ant", grid, gridSizeX, gridSizeY, startLocation, endLocation);
		animatedCreep.start();
		activeThreads.add(animatedCreep);
	}
	
	public void refreshGrid()
	{
		gc.clearRect(0,0,W,H);
		
		for(int y = 0; y < gridSizeY; y++)
		{
			for(int x = 0; x < gridSizeX; x++)
			{
				gc.setFill(grid[x][y].color);

				if(startLocation.x != -1 && startLocation.y != -1)
				{
					grid[startLocation.x][startLocation.y].color = Color.BLUE;	
				}
				
				gc.fillRect(grid[x][y].x * rectWidth, grid[x][y].y * rectHeight, rectWidth, rectHeight);
			}
		}
	}
	
	
	
	
	public void printGrid()
	{
		for(int y= 0; y < gridSizeY; y++)
		{
			System.out.print("[ ");
			for(int x = 0; x < gridSizeX; x++)
			{
				if(!grid[x][y].alive)
					System.out.print("1 ");
				else
					System.out.print("0 ");
			}

			System.out.println("]");
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
