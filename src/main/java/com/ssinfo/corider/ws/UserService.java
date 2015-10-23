package com.ssinfo.corider.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.ssinfo.corider.google.gcm.Message;
import com.ssinfo.corider.google.gcm.Result;
import com.ssinfo.corider.google.gcm.Sender;
import com.ssinfo.corider.mail.MailSender;
import com.ssinfo.corider.ws.model.AllRoutes;
import com.ssinfo.corider.ws.model.Legs;
import com.ssinfo.corider.ws.model.Location;
import com.ssinfo.corider.ws.model.MatchedCorider;
import com.ssinfo.corider.ws.model.MatchedCoriders;
import com.ssinfo.corider.ws.model.MessageDTO;
import com.ssinfo.corider.ws.model.RouteCoriders;
import com.ssinfo.corider.ws.model.Routes;
import com.ssinfo.corider.ws.model.SearchParams;
import com.ssinfo.corider.ws.model.Steps;
import com.ssinfo.corider.ws.model.User;
import com.ssinfo.corider.ws.model.UserInfo;
import com.ssinfo.corider.ws.util.ServiceHelper;
import com.ssinfo.ws.rider.constants.Constants;
import com.ssinfo.ws.rider.dao.LocationDao;
import com.ssinfo.ws.rider.dao.RouteDao;
import com.ssinfo.ws.rider.dao.SearchDao;
import com.ssinfo.ws.rider.dao.UserDAOImpl;
import com.ssinfo.ws.rider.mapping.CoriderInfoTbl;
import com.ssinfo.ws.rider.mapping.LegsTbl;
import com.ssinfo.ws.rider.mapping.LocationInfoTbl;
import com.ssinfo.ws.rider.mapping.RoutesTbl;
import com.ssinfo.ws.rider.mapping.SearchInfoTbl;
import com.ssinfo.ws.rider.mapping.StepsTbl;
import com.ssinfo.ws.rider.mapping.UserTbl;

@Path("/user")
public class UserService {
	private Logger logger = Logger.getLogger(UserService.class);

	@Context
	UriInfo uriInfo;

	@POST
	@Path("/sendMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendMessageToCorider(MessageDTO messageDto) {
		logger.info("sendMessage started..");
		Result result = null;
		try {
			User from = messageDto.getFromUser();
			User toUser = messageDto.getTouser();
			String regId = toUser.getGcmRegistrationId();
			Sender sender = new Sender(com.ssinfo.corider.google.gcm.Constants.GCM_ANDROID_SERVER_KEY);
			// send a single message using plain post
			Message.Builder messageBuilder = new Message.Builder();
			messageBuilder.addData(com.ssinfo.ws.rider.constants.Constants.FROM_USER_ID, String.valueOf(from.getUserId()));
			messageBuilder.addData(Constants.FROM_USER_NAME, from.getUserName());
			messageBuilder.addData(Constants.TO_USER_ID, toUser.getUserId());
		    messageBuilder.addData(Constants.TO_USER_NAME, toUser.getUserName());
		    messageBuilder.addData(Constants.MESSAGE, messageDto.getMsg());
		    messageBuilder.addData(Constants.MATCHED_DISTANCE, messageDto.getMatchedDistance());
		    messageBuilder.addData(Constants.DEST_ADDRESS, messageDto.getDestination());
		    messageBuilder.addData(Constants.USER_NAME, from.getUserName());
		    messageBuilder.addData(Constants.DEST_ADDRESS, messageDto.getDestination());
		    
         result = sender.send(messageBuilder.build(), regId, 5);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		String status = "Sent message to one device: " + result;
		if(result.getMessageId() !=null){
			status = "Message sent successfully";
		}else {
			status = "Message sent failed.";
		}
		return Response.status(200).entity(status).build();
	}
	
	
	
	@GET
	@Path("/sayHello/{param}")
	public Response getMsg(@PathParam("param") String msg) {

		String output = "Corider says : hello " + msg;

		return Response.status(200).entity(output).build();
	}

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User registerUser(User userInfo) {
		UserDAOImpl mUserDao = new UserDAOImpl();
		boolean isVerficationRequired = true;
		UserTbl userTbl = null;
		try {
			if(userInfo.getUserId() ==null){
				 userTbl = mUserDao.saveUserInfo(userInfo);
				userInfo.setUserId(String.valueOf(userTbl.getUserId()));
				
			}else {
				userTbl = mUserDao.findById(Long.parseLong(userInfo.getUserId()));
				userTbl.setUserName(userInfo.getUserName());
				if(!userTbl.getEmailId().equals(userInfo.getEmailId())){
					userTbl.setEmailId(userInfo.getEmailId());
				}else {
					isVerficationRequired = false;
				}
				mUserDao.updateUser(userTbl);
			}
			
		} catch (Exception ex) {
			logger.info(ex.getMessage());
			
			userInfo.setStatus(ex.getMessage());
		}
		
		if(isVerficationRequired){
			sendMessage(userTbl);
			userInfo.setStatus(Constants.VERIFICATION_PENDING);
		}
		return userInfo;

	}
	
	public void sendMessage(UserTbl user){
		MailSender mailSender = new MailSender();
		mailSender.setSendTo(user.getEmailId());
		mailSender.setFrom("coridr@netxylem.in");
		String message = ServiceHelper.getMessage(user.getUserName(), String.valueOf(user.getUserId()), user.getStatus());
		mailSender.sendMessage(message);
	}
	@GET
	@Path("/isActive")
	@Produces(MediaType.APPLICATION_JSON)
	public User isUserActive(@QueryParam("userId") String userId){
		UserDAOImpl userDao = new UserDAOImpl();
		UserTbl user = userDao.findById(Long.valueOf(userId));
		if(user !=null){
			return populateUser(user);
		}
		return new User();
	}
	
	public User populateUser(UserTbl userTbl){
		User user = new User();
		user.setUserId(String.valueOf(userTbl.getUserId()));
		user.setEmailId(userTbl.getEmailId());
		user.setUserName(userTbl.getUserName());
		user.setGcmRegistrationId(userTbl.getGcmId());
		user.setStatus(userTbl.getStatus());
		return user;
	}
	/*@GET
	@Path("/testing")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response test(@QueryParam("userId") String userId,@QueryParam("verificationCode") String verificationCode) {
		String msg = userId+verificationCode;
		return Response.status(200).entity(msg).build();
	}*/
	
	
	@GET
	@Path("/verification")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String verifyUserEmail(@QueryParam("userId") String userId,@QueryParam("verificationCode") String verificationCode) {
		String message = "Invalid verification code";
		UserDAOImpl mUserDao = new UserDAOImpl();
		try {
			UserTbl user = mUserDao.findById(Long.valueOf(userId)); 
			if(user !=null){
				if(user.getStatus().equals(Constants.ACTIVE_USER)){
					return "Already active user.";
				}
				if(verificationCode.equals(user.getStatus())){
				  	user.setStatus(Constants.ACTIVE_USER);
				  	mUserDao.updateUser(user);
				  	message = "User is active now";
				}else {
					message = "Invalid verification code.";
				}
				 
			}else {
				message = "User doesn't exist";
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			message = "Server internal issue";
		}
		return message;

	}

	public LocationInfoTbl populateLocationTbl(Location location) {
       logger.info("Inside the populateLocationTbl");
		LocationInfoTbl locationTbl = new LocationInfoTbl();
		if (location.getLng() != null && location.getLat() != null) {
			locationTbl.setLongitude(Float.valueOf(location.getLng()));
			locationTbl.setLatitude(Float.valueOf(location.getLat()));
		} else {
			locationTbl.setLatitude((float) location.getLatitude());
			locationTbl.setLongitude((float) location.getLongitude());
		}
		String desc = null;
		if (location.getLocationAddress() != null && location.getLocationAddress().length()>0) {
			locationTbl.setLocationAddress(location.getLocationAddress());
			locationTbl.setCity(location.getCity());
			locationTbl.setCountry(location.getCountry());
			return locationTbl;
		} else if(location.getDescription() !=null){
			desc = location.getDescription();
		}else {
			return locationTbl;
		}

		logger.info("location desc in populateLocationInof" + desc);
		if (desc != null && desc.length() > 0) {
			List<String> address = Arrays.asList(desc.split(","));
			if (address.size() >= 3) {
				locationTbl.setLocationAddress(address.get(0)+address.get(1));
				locationTbl.setCity(address.get(2));
				locationTbl.setCountry(address.get(address.size() - 1));
			}

		}
		return locationTbl;
	}

	public LegsTbl populateLesgTbl(Legs leg) {
		LegsTbl legsTbl = new LegsTbl();
		legsTbl.setStartAddress(leg.getStart_address());
		legsTbl.setEndAddress(leg.getEnd_address());
		
		// start location of leg
		Location startLoc = leg.getStart_location();
		logger.info("legs address are "+leg.getStart_address());
		if (leg.getStart_address() != null ) {
			startLoc.setDescription(leg.getStart_address());
		}
		LocationDao mLocationDao = new LocationDao();
		//start location
		LocationInfoTbl startLocationTbl = populateLocationTbl(startLoc);
		startLocationTbl.setLocationType("LEGS_START_LOCATION");
		mLocationDao.persist(startLocationTbl);
		legsTbl.setLocationInfoTblByStartLocationId(startLocationTbl);
		
		Location endLoc = leg.getEnd_location();
		if (leg.getEnd_address() != null) {
			endLoc.setDescription(leg.getEnd_address());
		}

		LocationInfoTbl endLocationTbl = populateLocationTbl(endLoc);
		endLocationTbl.setLocationType("LEGS_END_LOCATION");
		mLocationDao.persist(endLocationTbl);
		legsTbl.setLocationInfoTblByEndLocationId(endLocationTbl);
		legsTbl.setDistance(Long.valueOf(leg.getDistance().getValue()));
		legsTbl.setDuration(Long.valueOf(leg.getDuration().getValue()));
		return legsTbl;
	}

	public StepsTbl populateStepsTbl(Steps steps) {
		Location startLoc = steps.getStart_location();
		LocationInfoTbl startLocationTbl = populateLocationTbl(startLoc);
		LocationDao mLocationDao = new LocationDao();
		mLocationDao.persist(startLocationTbl);
		LocationInfoTbl endLocation = populateLocationTbl(steps
				.getEnd_location());
		mLocationDao.persist(endLocation);
		StepsTbl stepTbl = new StepsTbl();
		stepTbl.setLocationInfoTblByStartLocationId(startLocationTbl);
		stepTbl.setLocationInfoTblByEndLocationId(endLocation);
		stepTbl.setDistance(Long.valueOf(steps.getDistance().getValue()));
		stepTbl.setDuration(Long.valueOf(steps.getDuration().getValue()));
		return stepTbl;

	}
	
	public List<CoriderInfoTbl> populateCoriderInfoTbl(List<UserTbl> availableCoriderList,SearchInfoTbl searchInfo){
		List<CoriderInfoTbl> coriderList = new ArrayList<CoriderInfoTbl>();
		for(UserTbl availUser:availableCoriderList){
			CoriderInfoTbl corider = new CoriderInfoTbl();
			corider.setSearchInfoTbl(searchInfo);
			corider.setUserTbl(availUser);
			corider.setStatus(Constants.AVAILABLE);
			coriderList.add(corider);
		}
		
		return coriderList;
	}

	@POST
	@Path("/matchedCoriders")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public MatchedCoriders findMatchedCoriders(SearchParams searchParams) {
		logger.info("Inside the findMatchedCoriders..."+Calendar.getInstance().getTime());
		UserDAOImpl userDao = new UserDAOImpl();
	
		User user = searchParams.getUser();
		//load the user information from database.
		UserTbl userTbl = userDao.findById(Long.valueOf(user.getUserId()));
		// delete previous search routes of user.
		userDao.deleteUserPreviousSearchRoutes(userTbl);
        //save current location and destination.
		LocationInfoTbl currentLocationTbl = populateLocationTbl(searchParams.getCurrentLocation());
		LocationInfoTbl destLocation = populateLocationTbl(searchParams.getDestination());
		LocationDao mLocationDao = new LocationDao();
		mLocationDao.persist(currentLocationTbl);
		mLocationDao.persist(destLocation);

		// save all the details of user into database.
		SearchInfoTbl userSearchInfo = saveUserInfo(searchParams, currentLocationTbl, destLocation, userTbl);

		SearchDao searchDao = new SearchDao();
		
		// get all the available coriders.
		List<UserTbl> availableCoridersList = searchDao
				.findAllAvailableCoriders(currentLocationTbl,userTbl.getUserId());
		//set the total available coriders for this search
		userSearchInfo.setAvailableCoriderCount(availableCoridersList.size());
       //send totalAvailable coriders to client i.e device.
		sentAvailabeCoriders(availableCoridersList.size(),userTbl);
		//save all available coriders for this search.
		List<CoriderInfoTbl> availCoriderList = searchDao.saveAllAvailableCoriders(populateCoriderInfoTbl(availableCoridersList, userSearchInfo));
		
		
		Set<RoutesTbl> currentUserRoutes = userDao.findUserRoutes(userTbl.getUserId());
		Iterator<RoutesTbl> currentUsrItr = currentUserRoutes.iterator();
		RoutesTbl currentRoute = null;
		ServiceHelper serviceUtil = new ServiceHelper();
        int counter = 0;
        Set<Long> matchedUser = new HashSet<Long>();
        RouteCoriders[] routeCoridersArr = new RouteCoriders[currentUserRoutes.size()];
		while (currentUsrItr.hasNext()) {
			
			//get the current user route for compare
			currentRoute = currentUsrItr.next();
			RouteCoriders routeCoriders = new RouteCoriders();
			routeCoriders.setRouteName(currentRoute.getSummary());
			Set<LegsTbl> userLegsSet = currentRoute.getLegsTbls();
			LegsTbl[] userLegs = userLegsSet.toArray(new LegsTbl[userLegsSet.size()]);
            long legsTotalDistance = ServiceHelper.getTotalDistance(userLegs);
            long legsTotalDuration = ServiceHelper.getTotalDuration(userLegs);
            routeCoriders.setTotalDistanceOfRoute(String.valueOf(legsTotalDistance));
            routeCoriders.setTotalDuration(String.valueOf(legsTotalDuration));
            
            List<MatchedCorider> matchCoriderList = new ArrayList<MatchedCorider>();
			// start matching with all available user, start with last legs.
			for (int userLegsIndex = userLegs.length - 1; userLegsIndex >= 0; userLegsIndex--) {
				Set<StepsTbl> userStepsSet = userLegs[userLegsIndex].getStepsTbls();
				StepsTbl[] userStepsArr = userStepsSet.toArray(new StepsTbl[userStepsSet.size()]);
                 int coriderIndex = 0;
				// start comparing with all available coriders.
                 for (UserTbl corider : availableCoridersList) {
					List<MatchedCorider> tempMatchedRoutesList = new ArrayList<MatchedCorider>();
                    
					Set<RoutesTbl> coriderRouteSet = corider.getRoutesTbls();
					Iterator<RoutesTbl> coriderItr = coriderRouteSet.iterator();
					while (coriderItr.hasNext()) {
						RoutesTbl coriderBestRoute = coriderItr.next();
						if (coriderBestRoute != null
								&& coriderBestRoute.getLegsTbls().size() > 0) {
							Set<LegsTbl> coriderLegsSet = coriderBestRoute.getLegsTbls();
							//convert into array for comparing from last index.
							LegsTbl[] coriderLegsArr = coriderLegsSet.toArray(new LegsTbl[coriderLegsSet.size()]);
                             boolean isStepMatched = false;
							for (int legIndex = coriderLegsArr.length - 1; legIndex >= 0; legIndex--) {
								 if(isStepMatched){
									 break;
								 }
								Set<StepsTbl> coriderStepSet = coriderLegsArr[legIndex].getStepsTbls();
								StepsTbl[] coriderStepArr = coriderStepSet.toArray(new StepsTbl[coriderStepSet.size()]);
								if (coriderStepArr.length > 0) {
									int index = serviceUtil.getMatchedIndex(userStepsArr,coriderStepArr,legsTotalDistance);

									if (index > 0) {
										serviceUtil.addMatchedCoriderToList(tempMatchedRoutesList, corider,	userLegsIndex, index, userLegs,	userStepsArr,coriderBestRoute.getSummary());
										matchedUser.add(corider.getUserId());
										isStepMatched = true;
										//update the status of corider in AvailableCoriderList
										CoriderInfoTbl availCorider = availCoriderList.get(coriderIndex);
										availCorider.setStatus(Constants.MATCHED);
										//save the matched corider.
										searchDao.updateCoriderStatus(availCorider);
									} else if (index == -2) {
										// threshold reached.
										break;
									} // continue for second legs comparison.

								} else {
									System.out.println("coriderStepsarr are zero");
								}
							}

						}
						
					}
					MatchedCorider bestRouteMatched = getBestMatchedRoute(tempMatchedRoutesList);
					tempMatchedRoutesList.clear();
					if(bestRouteMatched !=null){
						logger.info("total distance "+bestRouteMatched.getMatchedDistance());
						logger.info("minutes "+bestRouteMatched.getMinutes());
						//get the current location and destination of matched corider
						SearchInfoTbl searchInfo = searchDao.getLatestSearchInfo(bestRouteMatched.getUserId());
						LocationInfoTbl currentLocation = searchInfo.getLocationInfoTblByUserCurrentLocationInfoId();
						Location currentLoc = populateLocationInfo(currentLocation);
						bestRouteMatched.setCurrentLocation(currentLoc);
						//Destination 
						LocationInfoTbl destination = searchInfo.getLocationInfoTblByUserDestinationLocationId();
						Location dest = populateLocationInfo(destination);
						bestRouteMatched.setDestination(dest);
					matchCoriderList.add(bestRouteMatched);
					}
					coriderIndex++;
				}
			}
			
			//add all the list of matched coriders 
			if(matchCoriderList.size() >0){
				routeCoriders.setMatchedCoriderList(matchCoriderList);
				routeCoridersArr[counter] = routeCoriders;
 				counter++;	
				matchCoriderList = null;
			}
			
		}
		MatchedCoriders coriders = new MatchedCoriders();
		logger.info("total matched users are "+matchedUser.size());
		coriders.setTotalMatchedUser(matchedUser.size());
		coriders.setStatus(200);
		coriders.setRouteCoriders(routeCoridersArr);
		//Save the total matched coriders.
		userSearchInfo.setMatchedCoriderCount(matchedUser.size());
        searchDao.updateSearchInfo(userSearchInfo); 		
		
		return coriders;
		
	}
	
	public Location populateLocationInfo(LocationInfoTbl locationInfoTbl){
		Location location = new Location();
		location.setLatitude(locationInfoTbl.getLatitude());
		location.setLongitude(locationInfoTbl.getLongitude());
		location.setLocationAddress(locationInfoTbl.getLocationAddress());
		location.setCity(locationInfoTbl.getCity());
		
		return location;
	}
	
	/**
	 * Function for getting the best matched route among list of matched routes.
	 * @param coriderList
	 * @return
	 */
	public MatchedCorider getBestMatchedRoute(List<MatchedCorider> coriderList){
		if(coriderList.size() <= 0){
			return null;
		}else if(coriderList.size() == 1){
			return coriderList.get(0);
		}
		MatchedCorider corider = coriderList.get(0);
		for(int index = 1;index<coriderList.size();index ++){
			MatchedCorider cRider = coriderList.get(index);
			if(cRider.getMatchedDistance() > corider.getMatchedDistance()){
				corider = cRider;
				logger.info("matched distance "+corider.getMatchedDistance());
			}
		}
		return corider;
		
	}
	
	public void sentAvailabeCoriders(int totalCount,UserTbl currentUser){
		try{
		Sender sender = new Sender(com.ssinfo.corider.google.gcm.Constants.GCM_ANDROID_SERVER_KEY);
		// send a single message using plain post
		Message.Builder messageBuilder = new Message.Builder();
		messageBuilder.addData(Constants.TO_USER_ID, String.valueOf(currentUser.getUserId()));
	    messageBuilder.addData(Constants.TO_USER_NAME, currentUser.getUserName());
	    messageBuilder.addData(Constants.MESSAGE, ""+totalCount);
	    messageBuilder.addData(Constants.MESSAGE_TYPE, Constants.AVAILABLE_CORIDERS_INFO);
        sender.send(messageBuilder.build(), currentUser.getGcmId(), 5);
	} catch (IOException e) {
		logger.info(e.getMessage());
	}
	}

	@GET
	@Path("/refresh")
	//@Path("/refresh/{param}")
	//public Response refreshRoutes(@PathParam("param") String userId) {
	public Response refreshRoutes(@QueryParam("userId") String userId) {
		logger.info("Inside the refresh route "+userId);
		UserDAOImpl userDao = new UserDAOImpl();
		UserTbl userTbl = userDao.findById(Long.valueOf(userId));
		int totalCount = userDao.deleteUserPreviousSearchRoutes(userTbl);
		return Response.status(200).entity("totalCount is "+totalCount).build();
	}

	public SearchInfoTbl saveUserInfo(SearchParams params,
			LocationInfoTbl currentLocation,
			LocationInfoTbl destinationLocation, UserTbl currentUser) {
		logger.info("inside saveUserInfo");

		SearchDao mSearchDao = new SearchDao();
		SearchInfoTbl searchInfoTbl = new SearchInfoTbl();

		// Persist Search data
		searchInfoTbl
				.setLocationInfoTblByUserCurrentLocationInfoId(currentLocation);
		searchInfoTbl
				.setLocationInfoTblByUserDestinationLocationId(destinationLocation);
		searchInfoTbl.setSearchDate(new Date());
		searchInfoTbl.setUserTbl(currentUser);
		mSearchDao.persist(searchInfoTbl);
		UserInfo userInfo = new UserInfo();
		userInfo.setSearchId(searchInfoTbl.getSearchId());
		// save userRoutes
		saveUserRoutesInfo(params.getAllRoutes(), searchInfoTbl, currentUser);

		return searchInfoTbl;
	}

	public void saveUserRoutesInfo(AllRoutes userRoutes,
			SearchInfoTbl searchInfo, UserTbl user) {

		// persist all the route details.
		Routes[] routeArr = userRoutes.getRoutes();
		logger.info("routes length are " + routeArr.length);
		for (int routeIndex = 0; routeIndex < routeArr.length; routeIndex++) {
			logger.info("routes saving..");
			Routes route = routeArr[routeIndex];
			Legs[] legsArr = route.getLegs();
			// save route info into routes_tbl
			RoutesTbl routesTbl = new RoutesTbl();
			RouteDao routeDao = new RouteDao();
			routesTbl.setSearchInfoTbl(searchInfo);
			routesTbl.setUserTbl(user);
			logger.info("route summary is"+route.getSummary());
			routesTbl.setSummary(route.getSummary());
			routeDao.saveRoutesInfo(routesTbl);
			// save legs info.
			for (int legsIndex = 0; legsIndex < legsArr.length; legsIndex++) {
				Legs leg = legsArr[legsIndex];
				LegsTbl legsTbl = populateLesgTbl(leg);
				legsTbl.setRoutesTbl(routesTbl);
				routeDao.saveLegsInfo(legsTbl);
				Steps[] stepsArr = leg.getSteps();
				// save steps.
				for (int stepsIndex = 0; stepsIndex < stepsArr.length; stepsIndex++) {
					Steps step = stepsArr[stepsIndex];
					StepsTbl stepsTbl = populateStepsTbl(step);
					// Add legs id.
					stepsTbl.setLegsTbl(legsTbl);

					// save steps info.
					routeDao.saveSteps(stepsTbl);
				}
			}
		}
	}

}
