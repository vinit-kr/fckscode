package com.ssinfo.corider.ws.model;

public class WayPoint {
	private String step_interpolation;

	private String step_index;

	private Location location;

	public String getStep_interpolation() {
		return step_interpolation;
	}

	public void setStep_interpolation(String step_interpolation) {
		this.step_interpolation = step_interpolation;
	}

	public String getStep_index() {
		return step_index;
	}

	public void setStep_index(String step_index) {
		this.step_index = step_index;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
