package com.ssinfo.corider.ws.model;

import com.ssinfo.ws.rider.mapping.UserTbl;

public class UserInfo {

	public UserInfo() {
		// TODO Auto-generated constructor stub
	}

	private UserTbl user;
	private long searchId;

	public UserTbl getUser() {
		return user;
	}

	public void setUser(UserTbl user) {
		this.user = user;
	}

	public long getSearchId() {
		return searchId;
	}

	public void setSearchId(long searchId) {
		this.searchId = searchId;
	}

}
