package com.ssinfo.corider.ws.model;

public class MatchedRoute {

	public MatchedRoute() {
		// TODO Auto-generated constructor stub
	}

	private long distance;
	private int hour;
	private int minute;

	private int stepIndex;
	private Steps[] stepArr;

	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getStepIndex() {
		return stepIndex;
	}

	public void setStepIndex(int stepIndex) {
		this.stepIndex = stepIndex;
	}

	public Steps[] getStepArr() {
		return stepArr;
	}

	public void setStepArr(Steps[] stepArr) {
		this.stepArr = stepArr;
	}

}
