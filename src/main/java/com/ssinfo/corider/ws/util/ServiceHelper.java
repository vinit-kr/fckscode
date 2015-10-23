package com.ssinfo.corider.ws.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import org.apache.log4j.Logger;

import com.ssinfo.corider.ws.model.MatchedCorider;
import com.ssinfo.corider.ws.model.MatchedRoute;
import com.ssinfo.ws.rider.constants.Constants;
import com.ssinfo.ws.rider.mapping.LegsTbl;
import com.ssinfo.ws.rider.mapping.LocationInfoTbl;
import com.ssinfo.ws.rider.mapping.RoutesTbl;
import com.ssinfo.ws.rider.mapping.StepsTbl;
import com.ssinfo.ws.rider.mapping.UserTbl;

/**
 * Class for calculating the matched coriders.
 * @author VINIT
 *
 */
public class ServiceHelper {
	
	private Logger logger = Logger.getLogger(ServiceHelper.class);
	private static SecureRandom random = new SecureRandom();
	
	public ServiceHelper() {
		// TODO Auto-generated constructor stub
	}
/**
 * Method for comparing two steps.
 * @param availableUserStep
 * @param currentUserStep
 * @return
 */
	//TODO Need to remove this function. Unused function.
	public boolean isStepEqual(StepsTbl availableUserStep,
			StepsTbl currentUserStep) {
        //The available user's step start and end location.
		LocationInfoTbl availStartLocation = availableUserStep
				.getLocationInfoTblByStartLocationId();
		LocationInfoTbl availEndLocation = availableUserStep
				.getLocationInfoTblByEndLocationId();
         //The current user start and end location.  
		LocationInfoTbl currentUsrStartLocation = currentUserStep
				.getLocationInfoTblByStartLocationId();
		LocationInfoTbl currentUsrEndtLocation = currentUserStep
				.getLocationInfoTblByEndLocationId();

		if (isLocationEqual(availStartLocation, currentUsrStartLocation)
				&& isLocationEqual(availEndLocation, currentUsrEndtLocation)) {
			return true;
		} else {
			return false;
		}

	}

	public int getStepIndex(StepsTbl currentUserStep, StepsTbl availableUserStep) {
		
		//current user start and end location.
		LocationInfoTbl currentUsrStartLocation = currentUserStep.getLocationInfoTblByStartLocationId();
		LocationInfoTbl currentUsrEndtLocation = currentUserStep.getLocationInfoTblByEndLocationId();
        //available user start and end location.
		LocationInfoTbl availStartLocation = availableUserStep.getLocationInfoTblByStartLocationId();
		LocationInfoTbl availEndLocation = availableUserStep.getLocationInfoTblByEndLocationId();
		
		 //if current user start location matched with available user start location and current user end location matched with avail user end location.(Exact match step)
        if(isLocationEqual(currentUsrStartLocation, availStartLocation) && isLocationEqual(currentUsrEndtLocation, availEndLocation)){
        	return 0;
        }else if (isLocationEqual(currentUsrStartLocation, availStartLocation)) {
			return -1;
		} else if (isLocationEqual(currentUsrStartLocation, availEndLocation)) {
			return -1;
		} else if (isLocationEqual(currentUsrEndtLocation, availEndLocation)|| isLocationEqual(currentUsrEndtLocation, availStartLocation)) {
			return 0;
		}
		return -2;

	}

	public boolean isLegsEqual(LegsTbl availUserLeg, LegsTbl currentUserLeg) {
		LocationInfoTbl availStartLocation = availUserLeg
				.getLocationInfoTblByStartLocationId();
		LocationInfoTbl availEndLocation = availUserLeg
				.getLocationInfoTblByEndLocationId();

		LocationInfoTbl currentUsrStartLocation = currentUserLeg
				.getLocationInfoTblByStartLocationId();
		LocationInfoTbl currentUsrEndtLocation = currentUserLeg
				.getLocationInfoTblByEndLocationId();

		if (isLocationEqual(availStartLocation, currentUsrStartLocation)
				&& isLocationEqual(availEndLocation, currentUsrEndtLocation)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isLocationEqual(LocationInfoTbl location1,
			LocationInfoTbl location2) {
		if ((location1.getLatitude() == location2.getLatitude())
				&& (location1.getLongitude() == location2.getLongitude())) {
			return true;
		} else {
			return false;
		}
	}
/**
 * Function for getting the matchedIndex of StepArr
 * @param currentUserSteps
 * @param coriderStepsArr
 * @param totalDistance
 * @return
 */
	public int getMatchedIndex(StepsTbl[] currentUserStepsArr,
			StepsTbl[] coriderStepsArr, long totalDistance) {
		int returnIndex = -1;
		boolean isStepMatched = false;
		boolean isThreshHoldReached = false;
		float totalThresHold = getThresholdDistance(totalDistance);
		logger.info(totalDistance+" total distance : thrashhold distance limit is "+totalThresHold);
		long totalUnMatchedDistance = 0;
		for (int userStepIndex = currentUserStepsArr.length - 1; userStepIndex >= 0; userStepIndex--) {
			logger.info("total Unmatched distance "+totalUnMatchedDistance);
            if(isThreshHoldReached || isStepMatched){
            	break;
            }
            StepsTbl userStep = currentUserStepsArr[userStepIndex];
            
			for (int coriderStepIndex = coriderStepsArr.length - 1; coriderStepIndex >= 0; coriderStepIndex--) {
				 StepsTbl coriderStep = coriderStepsArr[coriderStepIndex];
				int index = getStepIndex(userStep,coriderStep);
				if (index == 0) {
					logger.info("matched index "+userStepIndex);
					logger.info("Steps Arr length is "+currentUserStepsArr.length);
					returnIndex = userStepIndex;
					
					isStepMatched = true;
					break;
				} else if (index == -1) {
					isStepMatched = true;
					if (userStepIndex > 0) {
						returnIndex = userStepIndex - 1;
					} else {
						returnIndex = userStepIndex;
					}
			      break;		
             
				}
			}
			if (!isStepMatched) {
                  
				totalUnMatchedDistance += userStep.getDistance();
				if (totalUnMatchedDistance >= totalThresHold) {
					isThreshHoldReached = true;
					returnIndex = -2; //threshHold reached no need for next leg comparison if any.
				}
			}

		}
		return returnIndex;

	}

	public void addMatchedCoriderToList(List<MatchedCorider> coriderList,UserTbl user, int legIndex, int stepsIndex, LegsTbl[] legs,StepsTbl[] stepArr,String routeSummary) {
		long totalDistance = 0;
		int totalDuration = 0;
		MatchedCorider corider = new MatchedCorider();
		corider.setUserId(user.getUserId());
		corider.setEmailId(user.getEmailId());
		corider.setUserName(user.getUserName());
		corider.setGcmRegistrationId(user.getGcmId());
		if(routeSummary !=null){
			corider.setMatchedRoute(routeSummary);	
		}else {
			corider.setMatchedRoute(legs[legIndex].getStartAddress());
		}
				
		// get the total matched distance.
		logger.info("leg index "+legIndex);
		if (legIndex > 0) {

			for (int legsLength = legIndex-1; legsLength >= 0; legsLength--) {
				LegsTbl leg = legs[legsLength];
				totalDistance += leg.getDistance();
				totalDuration += leg.getDuration();
			}

		}

		for (int lastIndex = stepsIndex; lastIndex >= 0; lastIndex--) {
			StepsTbl step = stepArr[lastIndex];
			totalDistance += step.getDistance();
			totalDuration += step.getDuration();
		}

		corider.setMatchedDistance(totalDistance);
		logger.info("total matched distance "+totalDistance);
		logger.info("total duration "+totalDuration);
		corider.setMinutes(totalDuration);

		coriderList.add(corider);

	}

	public static long getTotalDistance(LegsTbl[] legsArr) {
		long totalDistance = 0;
		for (LegsTbl legs : legsArr) {
			totalDistance += legs.getDistance();
		}
		return totalDistance;
	}
	
	public static long getTotalDuration(LegsTbl[] legsArr){
		long totalDurationInMinute = 0;
		for (LegsTbl legs : legsArr) {
			totalDurationInMinute += legs.getDuration();
		}
		return totalDurationInMinute;
	}

	public List<MatchedRoute> getMatchedRoute(RoutesTbl userRoute,
			RoutesTbl coriderRoute) {

		return null;
	}

	public float getThresholdDistance(long totalDistance) {
		return (totalDistance * Constants.THRESHHOLD_PERCENTAGE);
	}
	
	public static synchronized String generateVerificationCode(){
		return new BigInteger(130, random).toString(32);
		
	}
	
	/**
	 * Function for creating a template that server sends as acknowledgment for registered email confirmation.
	 * @param userName
	 * @param userId
	 * @param verificationCode
	 * @return
	 */
	public static String getMessage(String userName,String userId,String verificationCode){
		//TODO Need to put this hard coded value in config file.
		//String url = "http://192.168.1.51:8089/corider/user/verification?";
		String url = "http://54.169.71.224:8080/corider/user/verification?";
		
		url +="userId="+userId+"&"+"verificationCode="+verificationCode;
		//TODO email id also need to put in config file.
		String supportUrl="mailto:"+Constants.FEEDBACK_EMAIL_ID+"?Subject=Please%20help%20me";
		String supportEmail = "vinit.javaengineer@gmail.com"; //TODO this value should be configurable.
		
		/*<a href="mailto:someone@example.com?Subject=Hello%20again" target="_top">*/
		
		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append("<html><head>");
		messageBuilder.append("<style>");
		messageBuilder.append("nav {line-height:30px; background-color:#01DF01;height:35px;width:200px;text-align:center;margin-bottom: 10px;margin-right: auto;margin-left: auto;padding:5px;}");
		messageBuilder.append("header { background-color:#01DF01; color:white; height: 55px; font-size: large; margin-bottom: 30px;	line-height: 55px; padding:5px;}");
		messageBuilder.append("</style>");
		messageBuilder.append("</head>");
		messageBuilder.append("<body>");
		messageBuilder.append("<header");
		messageBuilder.append(" style=\"background-color: #01DF01;color: white;height: 55px;font-size: large;margin-bottom: 30px;line-height: 55px;padding: 5px;\">");
		messageBuilder.append("Please confirm your email address");
		messageBuilder.append("</header>");
		messageBuilder.append("Hello ");
		messageBuilder.append(userName);
		messageBuilder.append("<p>");
		messageBuilder.append("Thanks for joining Corider. We are happy to see you in our community. Could you please click on the following link to confirm your account.\n");
		messageBuilder.append("<nav");
		messageBuilder.append(" style=\"line-height: 30px;background-color: #01DF01;height: 35px;width: 200px;text-align: center;margin-bottom: 10px;margin-right: auto;margin-left: auto;padding: 5px;\">");
		messageBuilder.append("<a href=\"");
		messageBuilder.append(url);
		messageBuilder.append("\">");
		messageBuilder.append("Activate Account");
		messageBuilder.append("</a>");
		messageBuilder.append("</nav>");
		
		messageBuilder.append("Of course, you can always reach us at ");
		messageBuilder.append("<a href=\"");
		messageBuilder.append(supportUrl);
		messageBuilder.append("\">");
		messageBuilder.append(supportEmail);
		messageBuilder.append("</a>");
		messageBuilder.append("<P>");
		messageBuilder.append("Enjoy sharing");
		messageBuilder.append("<P>");
		messageBuilder.append("Your corider team");
		messageBuilder.append("</body>");
		messageBuilder.append("</html>");
		return messageBuilder.toString();
		
	}

}
