
package lib;

import java.awt.Color;
import java.util.Hashtable;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * A waypoint that also has a color and a label
 * @author Martin Steiger
 */
public class MyWaypoint extends DefaultWaypoint
{
	private String label;
	private Color color;
	private String name;
	private Hashtable<String, Double> visited = new Hashtable<String, Double>(); 
	/**
	 * @param label the text
	 * @param color the color
	 * @param coord the coordinate
	 */
	public MyWaypoint(String label, Color color, String name, GeoPosition coord)
	{
		super(coord);
		this.label = label;
		this.color = color;
		this.name = name;
	}

	/**
	 * @return the label text
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @return the color
	 */
	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public Hashtable<String, Double> getVisited() {
		return visited;
	}

	public void setVisited(Hashtable<String, Double> visited) {
		this.visited = visited;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
