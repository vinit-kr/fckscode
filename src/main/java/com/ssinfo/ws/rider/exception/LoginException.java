package com.ssinfo.ws.rider.exception;

public class LoginException extends Exception {
	String mMessage;

	public LoginException(String message) {
		super(message);
		this.mMessage = message;
	}

	public String getMessage() {
		return mMessage;
	}

}
