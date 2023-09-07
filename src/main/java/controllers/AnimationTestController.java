package main.java.controllers;

import java.util.Random;

import classes.AnimatedBFS;
import classes.AnimatedCaveGeneration;
import classes.AnimatedDFS;
import classes.Coordinate;
import classes.GridElement;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
	
	private AnimatedCaveGeneration animatedFill;
	private AnimatedDFS animatedDFS;
	private AnimatedDFS animatedBFS;
	
	private Random random;
	
	@FXML
	public void initialize()
	{
		this.random = new Random();
		this.W = canvas.getWidth();
        this.H = canvas.getHeight();
        
		gridSizeX = 150;
		gridSizeY = 150;

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
    
//        printGrid();
	}
	
	@FXML
	private void generateNewCircleSubmit(Event e) throws InterruptedException
	{
		interruptAllThreads();
		animatedFill = new AnimatedCaveGeneration("Thread 1", grid, gridSizeX, gridSizeY, startLocation, endLocation);
		animatedFill.start();	
	}
	
	@FXML
	private void showDFS(Event e)
	{
		interruptAllThreads();
		startLocation.print();
		endLocation.print();
		AnimatedDFS animatedDFS = new AnimatedDFS("DFS Thread", grid, gridSizeX, gridSizeY, startLocation, endLocation);
		animatedDFS.start();
	}
	
	@FXML
	private void showBFS(Event e)
	{
		interruptAllThreads();
		AnimatedBFS animatedBFS = new AnimatedBFS("DFS Thread", grid, gridSizeX, gridSizeY, startLocation, endLocation);
		animatedBFS.start();
	}
	
	
	
	public void refreshGrid()
	{
		gc.clearRect(0,0,W,H);
		
		for(int y = 0; y < gridSizeY; y++)
		{
			for(int x = 0; x < gridSizeX; x++)
			{
				gc.setFill(grid[x][y].color);
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
		if(animatedFill != null)
		{
			animatedFill.interrupt();
		}
		if(animatedDFS != null)
		{
			animatedDFS.interrupt();
		}
		if(animatedBFS != null)
		{
			animatedBFS.interrupt();
		}
	}
	@FXML
	public void closeRequested(Event e)
	{
		interruptAllThreads();
	}
	
	
	
	
	
	
	
}
