package com.ssinfo.corider.ws.model;

import java.io.Serializable;

public class SearchParams implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4308538222359944111L;
	private User user;
	private Location currentLocation;
	private Location destination;
	private AllRoutes allRoutes;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public AllRoutes getAllRoutes() {
		return allRoutes;
	}

	public void setAllRoutes(AllRoutes allRoutes) {
		this.allRoutes = allRoutes;
	}

}
