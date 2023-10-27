package classes;

import javafx.scene.paint.Color;

public class StartingGridElement extends GridElement
{
	public StartingGridElement(int x, int y, CanvasCoordinate canvasCoor) 
	{
		super(x, y, Color.BLUE, true, canvasCoor);
	}	
}
