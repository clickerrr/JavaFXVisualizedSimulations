package classes;

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
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void print()
	{
		System.out.println(String.format("Coordinate Location: [%d, %d]", this.x, this.y));
	}
	public boolean equals(Coordinate compare)
	{
		return compare.x == x && compare.y == y;
		
	}
	
}
