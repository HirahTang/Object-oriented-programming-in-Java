package module6;

import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMarker extends CommonMarker {
	public static List<SimpleLinesMarker> routes;
	
	public static int TRI_SIZE = 5;  // The size of the triangle marker
	
	public AirportMarker(Feature city) {
		
		super(((PointFeature)city).getLocation(), city.getProperties());
		
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		pg.fill(11);
		pg.ellipse(x, y, 5, 5);
		
		
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		pg.pushStyle();
		// show rectangle with title
		
		String name = getName() + "," + getId();
		String loc = getCity() + ", " + getCountry();
		
		pg.fill(255, 255, 255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x, y-TRI_SIZE-39, Math.max(pg.textWidth(name), pg.textWidth(loc)) + 6, 39);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(name, x+3, y-TRI_SIZE-33);
		pg.text(loc, x+3, y - TRI_SIZE -18);
		
		
		// show routes
		
		pg.popStyle();
	}
	
	private String getName()
	{
		String name = getStringProperty("name");
		
		return name.substring(1, name.length()-1);
	}
	
	private String getCity()
	{
		String city = getStringProperty("city");
		return city.substring(1, city.length()-1);
	}
	
	private String getCountry()
	{
		String country = getStringProperty("country");
		return country.substring(1, country.length()-1);
	}
	
	
}
