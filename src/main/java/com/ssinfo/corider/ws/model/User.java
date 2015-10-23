package com.ssinfo.corider.ws.model;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1514415004861662387L;

	private String userId;
	private String userName;
	private String emailId;
	private String gcmRegistrationId;
	private String status;
	private DeviceInfo device;
	private AllRoutes allRoutesObj;
	
	public User(){
		
	}
   public User(String userId){
	   this.userId = userId;
   }
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AllRoutes getAllRoutesObj() {
		return allRoutesObj;
	}

	public void setAllRoutesObj(AllRoutes allRoutesObj) {
		this.allRoutesObj = allRoutesObj;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public DeviceInfo getDevice() {
		return device;
	}

	public void setDevice(DeviceInfo device) {
		this.device = device;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGcmRegistrationId() {
		return gcmRegistrationId;
	}

	public void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}
}
