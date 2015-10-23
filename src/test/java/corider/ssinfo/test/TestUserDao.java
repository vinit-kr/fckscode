package corider.ssinfo.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.ssinfo.corider.ws.hibernate.HibernateUtil;
import com.ssinfo.corider.ws.model.AllRoutes;
import com.ssinfo.corider.ws.model.Distance;
import com.ssinfo.corider.ws.model.Duration;
import com.ssinfo.corider.ws.model.Legs;
import com.ssinfo.corider.ws.model.Location;
import com.ssinfo.corider.ws.model.MatchedCorider;
import com.ssinfo.corider.ws.model.Routes;
import com.ssinfo.corider.ws.model.SearchParams;
import com.ssinfo.corider.ws.model.Steps;
import com.ssinfo.corider.ws.util.ServiceHelper;
import com.ssinfo.ws.rider.dao.LocationDao;
import com.ssinfo.ws.rider.dao.SearchDao;
import com.ssinfo.ws.rider.dao.UserDAOImpl;
import com.ssinfo.ws.rider.mapping.DeviceInfoTbl;
import com.ssinfo.ws.rider.mapping.LegsTbl;
import com.ssinfo.ws.rider.mapping.LocationInfoTbl;
import com.ssinfo.ws.rider.mapping.RoutesTbl;
import com.ssinfo.ws.rider.mapping.SearchInfoTbl;
import com.ssinfo.ws.rider.mapping.StepsTbl;
import com.ssinfo.ws.rider.mapping.UserTbl;

public class TestUserDao extends TestCase {
	 SessionFactory sessionFactory;
	
	@Test
	public void testSaveLocation(){
		System.out.println("Inside the testSaveUserInfo");
		getGcmRegId();
		//getAllAvailableCoriders();
		//getMatchedCoriders();
		//deleteRoutes();
		//saveSearchInfo();
	}

	public void getGcmRegId(){
	 new UserDAOImpl().findById(61L);
	}
	
	public void saveSearchInfo(){
		SearchInfoTbl search = new SearchInfoTbl();
		search.setSearchDate(new Date());
		new SearchDao().persist(search);
	}
	
	@Override
	protected void setUp() throws Exception {
		try {
			 Configuration configuration = new Configuration().configure();
			 StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
			 applySettings(configuration.getProperties());
			 sessionFactory = configuration.buildSessionFactory(builder.build());
             if(sessionFactory == null){
            	 throw new Exception("Error occurred in sessionFactory creation");
             }
		} catch (Exception e) {
			
		}
		super.setUp();
	}
	 public Session getSession()
	    {
	        Session session;

	        try {
	            session = sessionFactory.getCurrentSession();
	        } catch (HibernateException se) {
	            session = sessionFactory.openSession();
	        }

	        return session;
	    }

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
	
	public void registerUser(){
		Session session = HibernateUtil.getSession();
	    Transaction transaction= session.beginTransaction();
		UserTbl user = new UserTbl();
		user.setUserName("amit1");
				
		DeviceInfoTbl device = new DeviceInfoTbl();
		device.setDeviceOsType("android");
		device.setDeviceOsVersion("4.4");
	    Set<DeviceInfoTbl> devices = new HashSet<DeviceInfoTbl>();
	    devices.add(device);
	    device.setUserTbl(user);
	    user.setDeviceInfoTbls(devices);
	    session.persist(user);
	    transaction.commit();
		
		session.close();
	}
	
	public void saveLocation(){
		Session session = HibernateUtil.getSession();
	    Transaction transaction= session.beginTransaction();
	    LocationInfoTbl locationTbl = new LocationInfoTbl();
	    locationTbl.setLatitude(28.9876555f);
	    locationTbl.setLongitude(77.8733933f);
	    session.persist(locationTbl);
	    SearchInfoTbl stbl = new SearchInfoTbl();
	    stbl.setLocationInfoTblByUserCurrentLocationInfoId(locationTbl);
	    session.persist(stbl);
	    transaction.commit();
	    session.close();
	}
	
	public void getAllAvailableCoriders(){
	//UserDAOImpl userDao = new UserDAOImpl();
	LocationInfoTbl currentLocation = new LocationInfoTbl();
	currentLocation.setLatitude(28.976321);
	currentLocation.setLongitude(77.8756431);
	currentLocation.setCity("NOIDA");
	currentLocation.setCountry("India");
	new SearchDao().findAllAvailableCoriders(currentLocation,61);
	  	
	}
	
	public LocationInfoTbl populateLocationTbl(Location location){
		
		   LocationInfoTbl locationTbl = new LocationInfoTbl();
	        locationTbl.setLongitude(location.getLongitude());
	        locationTbl.setLatitude(location.getLatitude());
	        String desc = location.getDescription();
	        System.out.println("location desc in populateLocationInof"+desc);
	        if(desc !=null && desc.length() > 0){
	        	List<String> address = Arrays.asList(desc.split(","));
	        	if(address.size() >= 3){
	        		locationTbl.setLocationAddress(address.get(0));
		        	locationTbl.setCity(address.get(1));
		        	locationTbl.setCountry(address.get(address.size()-1));	
	        	}
	        	
	        }
	        return locationTbl;
	}
	
	public void deleteRoutes(){
		UserDAOImpl userDao = new UserDAOImpl();
		UserTbl user = userDao.findById(42L);
		if(user !=null){
			userDao.deleteUserPreviousSearchRoutes(user);	
		}else {
			System.out.println("user is null");
		}
		
	}
	public void getMatchedCoriders(){
		//Map routeMatchedCoriderMap = new HashMap<String, MatchedCorider>();
        SearchDao searchDao = new SearchDao();
        SearchParams searchParams = new SearchParams();
		Location currentLocation = new Location();
		currentLocation.setLatitude( 28.6239461);
		currentLocation.setLongitude(77.2921093);
		currentLocation.setDescription("Mandawali,delhi,110092,India");
		searchParams.setCurrentLocation(currentLocation);
		Location destination = new Location();
			
		destination.setLatitude(28.5874806);
		destination.setLongitude(77.3683319);
		destination.setDescription("Sector 52, Noida, Uttar Pradesh, India");
		searchParams.setDestination(destination);
		/*User user = new User();
		user.setUserId("37");*/
		//searchParams.setUser(user);
		
        LocationInfoTbl currentLocationTbl = populateLocationTbl(searchParams.getCurrentLocation());
        LocationDao locDao = new LocationDao();
        locDao.persist(currentLocationTbl);
        LocationInfoTbl destLocation = populateLocationTbl(searchParams.getDestination());
        locDao.persist(destLocation);
        UserTbl currentUser = new UserDAOImpl().findById(42L);
        SearchInfoTbl searchInfo = new SearchInfoTbl();
        searchInfo.setLocationInfoTblByUserCurrentLocationInfoId(currentLocationTbl);
        searchInfo.setLocationInfoTblByUserDestinationLocationId(destLocation);
        searchInfo.setUserTbl(currentUser);
        
        new SearchDao().persist(searchInfo);
        searchInfo.setSearchDate(new Date());
		// get all the available coriders.
		List<UserTbl> availableCoridersList = searchDao.findAllAvailableCoriders(currentLocationTbl,61);

		Map<String, List<MatchedCorider>> routeMatchedCoriderMap = new HashMap<String, List<MatchedCorider>>();
		UserDAOImpl userDao = new UserDAOImpl();
        UserTbl userTbl = userDao.findById(37L);
		Set<RoutesTbl> currentUserRoutes = userTbl.getRoutesTbls();
		Iterator<RoutesTbl> currentUsrItr = currentUserRoutes.iterator();
		RoutesTbl currentRoute = null;
		ServiceHelper serviceUtil = new ServiceHelper();
		int routeCount = 1;
		while (currentUsrItr.hasNext()) {
			routeCount++;
			currentRoute = currentUsrItr.next();
			String routeAddress = null;
			//boolean routeMatched = false;
			List<MatchedCorider> matchCoriderList = new ArrayList<MatchedCorider>();
			Set<LegsTbl> userLegsSet = currentRoute.getLegsTbls();
			LegsTbl[] userLegs = userLegsSet.toArray(new LegsTbl[userLegsSet.size()]);
					

			// start matching with all available user, start with last legs.
			for (int userLegsIndex = userLegs.length-1; userLegsIndex >= 0; userLegsIndex--) {
				routeAddress = userLegs[userLegsIndex].getStartAddress();  
				Set<StepsTbl> userStepsSet = userLegs[userLegsIndex].getStepsTbls();
				StepsTbl[] userStepsArr = userStepsSet.toArray(new StepsTbl[userStepsSet.size()]);

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
							LegsTbl[] coriderLegsArr = coriderLegsSet.toArray(new LegsTbl[coriderLegsSet.size()]);

							for (int legIndex = coriderLegsArr.length-1; legIndex >= 0; legIndex--) {
								Set<StepsTbl> coriderStepSet = coriderLegsArr[legIndex].getStepsTbls();
								StepsTbl[] coriderStepArr = coriderStepSet.toArray(new StepsTbl[coriderStepSet.size()]);
                                if(coriderStepArr.length > 0 ){
                                	int index = serviceUtil.getMatchedIndex(
    										userStepsArr, coriderStepArr,
    										serviceUtil.getTotalDistance(userLegs));
                                     
    								if (index > 0) {
    									serviceUtil.addMatchedCoriderToList(tempMatchedRoutesList, corider,userLegsIndex, index, userLegs, userStepsArr,coriderBestRoute.getSummary());

    								} else if (index == -2) {
    									// threshold reached.
    									break;
    								} // continue for second legs comparison.
	
                                }else {
                                 System.out.println("coriderStepsarr are zero");
                                }
								
							}

						}
						//get the best route from list of matched routes of corider.
						if(tempMatchedRoutesList.size() >1){
							//sort the route on the basis of distance.
							Collections.sort(tempMatchedRoutesList);
							matchCoriderList.add(tempMatchedRoutesList.get(0)); // get the best route from list of matched routes.
						}else if(tempMatchedRoutesList.size()> 0 && tempMatchedRoutesList.size() <2) {
							matchCoriderList.add(tempMatchedRoutesList.get(0)); //get the best route.
						}
					}

				}

			}
		
			routeMatchedCoriderMap.put(routeAddress+routeCount, matchCoriderList);
			
		}
		
		System.out.println("size of matched coriders are "+routeMatchedCoriderMap.size());
		for(Map.Entry<String, List<MatchedCorider>> entry:routeMatchedCoriderMap.entrySet()){
			System.out.println("routeAddress "+entry.getKey());
			 for(MatchedCorider corider:entry.getValue()){
			
			System.out.println("matched distance "+corider.getMatchedDistance());
			System.out.println("hour "+corider.getHour());
			System.out.println("userId"+corider.getUserName());
			
			}
		}	
	
	}
	public AllRoutes getAllRoutesData(){
		AllRoutes allRoutes = new AllRoutes();
         allRoutes.setRoutes(getRoutesList());		
		return allRoutes;
	}
	
	public Routes[] getRoutesList(){
		Routes[] routesArr= new Routes[5];
		for(int i =0; i< 4; i++){
			Routes routes = new Routes();
			routes.setLegs(getLegsArr());
			routesArr[i] = routes;
		}
		return routesArr;
	}
	
	public Legs[] getLegsArr(){
		 Legs[] legsArr = new Legs[2];
		  for(int j = 0;j<2;j++){
			  Legs legs = new Legs();
			  legs.setDistance(getDistance());
			  legs.setDuration(getDuration());
			  legs.setStart_location(getLocation("legs"));
			  legs.setEnd_location(getLocation("legs"));
			  legs.setEnd_address("legsEndAddress");
			  legs.setStart_address("legsStartAddress");
			  legs.setSteps(getStepsArr());
			  legsArr[j] = legs;
		  }
		  return legsArr;
	}
	
	public Steps[] getStepsArr(){
		Steps[] stepsArr = new Steps[10];
		for(int i = 0;i<6;i++){
			Steps step = new Steps();
			step.setDistance(getDistance());
			step.setDuration(getDuration());
			step.setStart_location(getLocation("stepsStart"));
			step.setEnd_location(getLocation("stepEnds"));
			stepsArr[i] = step;
		}
		return stepsArr;
	}
	
	public Location getLocation(String locfor){
		Location location = new Location();
		location.setLatitude(28.2223);
		location.setLongitude(76.38448939);
		location.setCity(locfor+"City");
		location.setCountry("Country");
		location.setLocationAddress(locfor+"locationAddress");
		return location;
	}
	public Distance getDistance(){
		Distance distance = new Distance();
		distance.setText("duration");
		distance.setValue("20");
		return distance;
	}
	
	public Duration getDuration(){
		Duration duration = new Duration();
		duration.setText("duration");
		duration.setValue("189");
		return duration;
	}
}
