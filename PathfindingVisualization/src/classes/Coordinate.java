package classes;

/**
 * <h1>Coordinate</h1>
 * <p>Representation of a Coordinate in an X, Y grid</p>
 */
public class Coordinate 
{
	public int x;
	public int y;
	
	public Coordinate()
	{
		this.x = -1;
		this.y = -1;
	}
	public Coordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	
	
	public void print()
	{
		System.out.println(String.format("Coordinate Location: [%d, %d]", this.x, this.y));
	}
	
	public boolean equals(Coordinate compare)
	{
		if(compare == null)
			return false;
		return compare.x == x && compare.y == y;
	}
	
}
