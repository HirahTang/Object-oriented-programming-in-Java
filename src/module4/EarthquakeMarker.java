package module4;

import de.fhpotsdam.unfolding.data.PointFeature;

import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/** Implements a visual marker for earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public abstract class EarthquakeMarker extends SimplePointMarker
{
	
	// Did the earthquake occur on land?  This will be set by the subclasses.
	protected boolean isOnLand;

	// SimplePointMarker has a field "radius" which is inherited
	// by Earthquake marker:
	// protected float radius;
	//
	// You will want to set this in the constructor, either
	// using the thresholds below, or a continuous function
	// based on magnitude. 
  
	
	
	/** Greater than or equal to this threshold is a moderate earthquake */
	public static final float THRESHOLD_MODERATE = 5;
	/** Greater than or equal to this threshold is a light earthquake */
	public static final float THRESHOLD_LIGHT = 4;

	/** Greater than or equal to this threshold is an intermediate depth */
	public static final float THRESHOLD_INTERMEDIATE = 70;
	/** Greater than or equal to this threshold is a deep depth */
	public static final float THRESHOLD_DEEP = 300;
    
	public static final String AGE = (String)"Past Hour";
	// ADD constants for colors

	
	// abstract method implemented in derived classes
	public abstract void drawEarthquake(PGraphics pg, float x, float y);
		
	
	// constructor
	public EarthquakeMarker (PointFeature feature) 
	{
		super(feature.getLocation());
		// Add a radius property and then set the properties
		java.util.HashMap<String, Object> properties = feature.getProperties();
		float magnitude = Float.parseFloat(properties.get("magnitude").toString());
		properties.put("radius", 2*magnitude );
		setProperties(properties);
		this.radius = 1.75f*getMagnitude();
		if (magnitude > THRESHOLD_MODERATE) {
			this.radius = 15;
		}
		else if (magnitude > THRESHOLD_LIGHT) {
			this.radius = 10;
		}
		else {
			this.radius = 5;
		}
		//System.out.println(properties.get("age").toString());
		
	}
	

	// calls abstract method drawEarthquake and then checks age and draws X if needed
	public void draw(PGraphics pg, float x, float y) {
		// save previous styling
		pg.pushStyle();
			
		// determine color of marker from depth
		colorDetermine(pg);
		
		// call abstract method implemented in child class to draw marker shape
		drawEarthquake(pg, x, y);
		
		// OPTIONAL TODO: draw X over marker if within past day		
		
		drawpasday(pg, x, y);
		
		// reset to previous styling
		pg.popStyle();
		
	}
	
	// determine color of marker from depth, and set pg's fill color 
	// using the pg.fill method.
	// We suggest: Deep = red, intermediate = blue, shallow = yellow
	// But this is up to you, of course.
	// You might find the getters below helpful.
	private void colorDetermine(PGraphics pg) {
		//TODO: Implement this method
		float depth = getDepth();
		if (depth > THRESHOLD_DEEP) {
			pg.fill(255, 0, 0);
		}
		else if (depth > THRESHOLD_INTERMEDIATE) {
			pg.fill(0, 0, 255);
		}
		else {
			pg.fill(255, 255, 0);
		}
	}
	
	private void drawpasday(PGraphics pg, float x, float y) {
		String age = getAge();
		if (age.equals(AGE)) {
			//System.out.println(age+AGE);
			pg.line(x-this.radius/2, y-this.radius/2, x+this.radius/2, y+this.radius/2);
			pg.line(x+this.radius/2, y-this.radius/2, x-this.radius/2, y+this.radius/2);
		}
	}
	
	/*
	 * getters for earthquake properties
	 */
	
	public float getMagnitude() {
		return Float.parseFloat(getProperty("magnitude").toString());
	}
	
	public String getAge() {
		return (String) getProperty("age");	
	}
	
	public float getDepth() {
		return Float.parseFloat(getProperty("depth").toString());	
	}
	
	public String getTitle() {
		return (String) getProperty("title");	
		
	}
	
	public float getRadius() {
		return Float.parseFloat(getProperty("radius").toString());
	}
	
	public boolean isOnLand()
	{
		return isOnLand;
	}
	
	
}
