package com.ssinfo.corider.ws.model;

import java.io.Serializable;

public class MessageDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private User fromUser;
	private String matchedDistance;
	private String destination;
	private User touser;
	private String msg;
	
	public MessageDTO(){
		
	}
	
	
	public String getMatchedDistance() {
		return matchedDistance;
	}

	public void setMatchedDistance(String matchedDistance) {
		this.matchedDistance = matchedDistance;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	public User getTouser() {
		return touser;
	}

	public void setTouser(User touser) {
		this.touser = touser;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}

	
}
