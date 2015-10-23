package com.ssinfo.ws.rider.login;

import com.ssinfo.corider.ws.model.User;

public interface ILogin {
	
	public User login(User userInfo);
	public User register(User userInfo);

}
