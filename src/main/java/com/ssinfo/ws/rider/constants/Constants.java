package com.ssinfo.ws.rider.constants;

public interface Constants {
	
	public int NO_ANY_STEPS_MATCHED = -1;
	public float THRESHHOLD_PERCENTAGE = .20f;
	
	  public static final String FROM_USER_ID = "fromUserId";
	  public static final String FROM_USER_NAME = "fromUserName";
	  public  static final String TO_USER_ID = "toUserId";
	  public static final String TO_USER_NAME = "toUserName";
	  public  static final String MESSAGE = "message";
	  
	  public static final String HEADER= "headerTitle";
	  public static final String USER_NAME = "userName";
	  public static final String MATCHED_DISTANCE = "matchedDistance";
	  public static final String DEST_ADDRESS = "destination";
	  
	  //user status
	  public static final String ACTIVE_USER = "1";
	  public static final String VERIFICATION_PENDING = "2";
	  public static final String INACTIVE_USER = "3";
	  public static final String INVALID_VERIFICATION_CODE = "Invalid Verification Code";
	  
	  //Messages
	  public static final String MESSAGE_HEADER = "Please confirm your e-mail address";
	  public static final String MESSAGE_TYPE = "messageType";
	  public static final String AVAILABLE_CORIDERS_INFO = "availableCoriders";
	  public static final String MATCHED_CORIDERS = "matchedCoriders";
	  
	  public static final String AVAILABLE = "available";
	  public static final String MATCHED = "matched";
	  
	  //email id
	  public static final String FEEDBACK_EMAIL_ID = "coridr@netxylem.in";
	  public static final String EMAIL_HOST = "mail.netxylem.in";
}
