package module6;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	
	public void setup() {
		// setting up PAppler
		size(800,600, OPENGL);
		
		// setting up map and default events
		map = new UnfoldingMap(this, 50, 50, 750, 550);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		//System.out.println(features);
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		
		// create markers from features
		for(PointFeature feature : features) {
			
			AirportMarker m = new AirportMarker(feature);
			m.setId(feature.getId());
			m.setRadius(5);
			airportList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
		
		}
	//	int n = 0;
	//	for(Marker mker: airportList) {
	//		System.out.println(mker.getId());
	//		n++;
	//		if (n > 10) {
	//			break;
	//		}
	//	}
		//System.out.println(airports);
		//System.out.println(airportList);
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
		
			//System.out.println(sl.getProperties());
			
			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
			routeList.add(sl);
		}
		
		
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		map.addMarkers(routeList);
		
		map.addMarkers(airportList);
		for (Marker mker:routeList) 
		{
			mker.setHidden(true);
		}
		
	}
	
	public void draw() {
		background(0);
		map.draw();
		
	}
	
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(airportList);
		//selectMarkerIfHover(cityMarkers);
		//loop();
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) 
		{
			return;
		}
			
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) 
			{
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}
	
	@Override
	public void mouseClicked()
	{
		if (lastClicked != null) {
			unhideMarkers();
			lastClicked = null;
		}
		else if (lastClicked == null) 
		{
		
			checkCitiesForClick();
			
		}
	}
	
	private void unhideMarkers() {
		for(Marker marker : airportList) {
			marker.setHidden(false);
		}
		
		for(Marker marker: routeList) {
			marker.setHidden(true);
		}
	}
	
	private void checkCitiesForClick()
	{
		if (lastClicked != null) return;
		// Loop over the earthquake markers to see if one of them is selected
		Set<Integer> remainList = new HashSet<Integer>();
		for (Marker m : airportList) {
			AirportMarker marker = (AirportMarker)m;
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				lastClicked = marker;
				// Hide all the other earthquakes and hide
				
				for (Marker route : routeList) 
				{
					route.setHidden(true);
					int source = Integer.parseInt((String)route.getProperty("source"));
					int dest = Integer.parseInt((String)route.getProperty("destination"));
					if (Integer.parseInt(lastClicked.getId()) == source || Integer.parseInt(lastClicked.getId()) == dest)
					{
						route.setHidden(false);
						remainList.add(source);
						remainList.add(dest);
						
						
					}
					
				}
				
				for (Marker mhide : airportList) {
					if (mhide != lastClicked) {
						mhide.setHidden(true);
						if (remainList.contains(Integer.parseInt(mhide.getId()))) 
						{
							mhide.setHidden(false);
						}
						
					}
					
				}
				
				return;
			}
		}
	}

}
