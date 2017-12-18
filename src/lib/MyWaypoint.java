
package lib;

import java.awt.Color;

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

	/**
	 * @param label the text
	 * @param color the color
	 * @param coord the coordinate
	 */
	public MyWaypoint(String label, Color color, GeoPosition coord)
	{
		super(coord);
		this.label = label;
		this.color = color;
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
}
