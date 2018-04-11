package lib;

import java.util.HashMap;
import java.util.Map;


public class Vertex implements Comparable<Vertex>{
	public final String name;
	public int dist = Integer.MAX_VALUE; // MAX_VALUE assumed to be infinity
	public Vertex previous = null;
	public final Map<Vertex, Integer> neighbours = new HashMap<>();
	   public static int i = 0;
	   public static int j = 1;
	   
	public Vertex(String name)
	{
		this.name = name;
	}
 
	public String printPath()
	{	
		String r = new String();
		if (this == this.previous)
		{
//			System.out.printf("%s", this.name);
			r+=this.name;
		}
		else if (this.previous == null)
		{
			System.out.printf("%s(unreached)", this.name);
		}
		else
		{
			r = this.previous.printPath();
//			System.out.printf(" -> %s(%d)", this.name, this.dist);
			r = r + " " + this.name;
//			System.out.println("r: " + r + " name: " + this.name + " dist: " + this.dist);
		}
		return r;
	}
 
	public int compareTo(Vertex other)
	{
		if (dist == other.dist)
			return name.compareTo(other.name);
 
		return Integer.compare(dist, other.dist);
	}
 
	@Override public String toString()
	{
		return "(" + name + ", " + dist + ")";
	}
}