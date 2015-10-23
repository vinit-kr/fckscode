package com.ssinfo.corider.ws.model;

import java.io.Serializable;

public class MatchedCorider implements Serializable,Comparable<MatchedCorider> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MatchedCorider() {
	
	}
    private String matchedRoute;
	private long userId;
	private String userName;
	private String emailId;
	private long matchedDistance;
	private int hour;
	private int minutes;
	private Location currentLocation;
	private Location destination;
	private String gcmRegistrationId;

	public String getGcmRegistrationId() {
		return gcmRegistrationId;
	}

	public void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
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

	public long getMatchedDistance() {
		return matchedDistance;
	}

	public void setMatchedDistance(long matchedDistance) {
		this.matchedDistance = matchedDistance;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MatchedCorider){
			if(this.userId == ((MatchedCorider) obj).getUserId()){
				return true;
			}else {
				return false;
			}
		}
	 return false;
	}

	@Override
	public int compareTo(MatchedCorider corider) {
		if(this.matchedDistance -corider.getMatchedDistance() > 0){
			return 1;
		}else {
			return -1;
		}
	}
	public String getMatchedRoute() {
		return matchedRoute;
	}

	public void setMatchedRoute(String matchedRoute) {
		this.matchedRoute = matchedRoute;
	}
	

}
