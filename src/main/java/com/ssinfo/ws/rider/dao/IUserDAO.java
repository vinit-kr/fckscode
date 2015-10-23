package com.ssinfo.ws.rider.dao;

import com.ssinfo.corider.ws.model.User;
import com.ssinfo.ws.rider.mapping.UserTbl;

public interface IUserDAO {
	
	public UserTbl saveUserInfo(User userInfo);

	public void remove(User user);
		
	public void updateUserInfo(User userInfo);

		
	
	
	

}
