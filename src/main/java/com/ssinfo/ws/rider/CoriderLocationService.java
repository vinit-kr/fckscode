package com.ssinfo.ws.rider;

import com.ssinfo.corider.ws.model.Location;

public interface CoriderLocationService {
	
	public boolean saveCurrentLocation(Location currentLocation);
	public boolean saveDestination(Location destinationLocation);
	
	
    
}
