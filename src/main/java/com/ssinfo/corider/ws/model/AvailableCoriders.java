package com.ssinfo.corider.ws.model;

public class AvailableCoriders {
	private User[] availableUsers;
	private long currentLocationId;
	private long destinationLocationId;
	private long currentUserId;
	private long searchId;

	public long getCurrentLocationId() {
		return currentLocationId;
	}

	public void setCurrentLocationId(long currentLocationId) {
		this.currentLocationId = currentLocationId;
	}

	public long getDestinationLocationId() {
		return destinationLocationId;
	}

	public void setDestinationLocationId(long destinationLocationId) {
		this.destinationLocationId = destinationLocationId;
	}

	public long getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(long currentUserId) {
		this.currentUserId = currentUserId;
	}

	public long getSearchId() {
		return searchId;
	}

	public void setSearchId(long searchId) {
		this.searchId = searchId;
	}

	public AvailableCoriders() {

	}

	public User[] getAvailableUsers() {
		return availableUsers;
	}

	public void setAvailableUsers(User[] availableUsers) {
		this.availableUsers = availableUsers;
	}

}
