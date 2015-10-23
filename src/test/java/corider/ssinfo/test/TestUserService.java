package corider.ssinfo.test;

import javax.ws.rs.core.MediaType;

import com.ssinfo.corider.ws.model.AllRoutes;
import com.ssinfo.corider.ws.model.DeviceInfo;
import com.ssinfo.corider.ws.model.Distance;
import com.ssinfo.corider.ws.model.Duration;
import com.ssinfo.corider.ws.model.Legs;
import com.ssinfo.corider.ws.model.Location;
import com.ssinfo.corider.ws.model.MessageDTO;
import com.ssinfo.corider.ws.model.Routes;
import com.ssinfo.corider.ws.model.SearchParams;
import com.ssinfo.corider.ws.model.Steps;
import com.ssinfo.corider.ws.model.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class TestUserService {
	private static final String BASE_URL = "http://192.168.137.58:8089/corider/user";
	private static ClientResponse response = null;

	public static void main(String args[]) {
		
		
		/*MailSender mailSender = new MailSender();
		mailSender.setSendTo("vinitforyou@gmail.com");
		mailSender.setFrom("vinitforyou@gmail.com");
		String message = ServiceHelper.getMessage("Vinit", "47", ServiceHelper.generateVerificationCode());
		System.out.println(message);
		mailSender.sendMessage(message);*/
		
		TestUserService testService = new TestUserService();
		response = testService.refresh();

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.toString());

		}

		String output = response.getEntity(String.class);

		System.out.println("Output from Server .... \n");
		System.out.println(output);

	}
	
	
	
	public ClientResponse sendMessage(){
		String url = "/sendMessage";
		User from = new User();
		from.setUserId("47");
		MessageDTO messageDto = new MessageDTO();
		messageDto.setFromUser(from);
		messageDto.setTouser(from);
		messageDto.setMsg("I want to share a ride with you");
		 return executeRequest(url, messageDto);
 	}
	
	public  ClientResponse refresh(){
		String url="/refresh/61";
		return executeRequest(url,null);
	}

	public ClientResponse saveSearchInfo() {
		String URL = "/availableCoriders";
		SearchParams sParams = new SearchParams();
		Location currentLocation = new Location();
		Location destination = new Location();
		currentLocation.setLatitude(28.62396567);
		currentLocation.setLongitude(77.35618);
		currentLocation.setDescription("currentLocation,noida,india");
		sParams.setCurrentLocation(currentLocation);

		destination.setLatitude(28.57466);
		destination.setLongitude(77.35608);
		destination.setDescription("destinationAddress,noida,india");
		sParams.setDestination(destination);

		return executeRequest(URL, sParams);

	}
	
	

	public ClientResponse executeRequest(String url, Object params) {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		Client client = Client.create(clientConfig);
		// Client client = Client.create();
		WebResource webResource = client.resource(BASE_URL + url);
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(ClientResponse.class);
		
	/*	ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, params);*/
		return response;

	}

	public ClientResponse registerUser() {
		String url = "/register";
		User userInfo = new User();
		userInfo.setUserName("Vinit1140");
		userInfo.setEmailId("test@gmail.com");
		DeviceInfo device = new DeviceInfo();
		device.setOsType("AndroidService");
		device.setOsVersion("4.4.2");
		device.setScreenSize("4.5");
		device.setAppVersion("1.0");
		device.setModelNo("iphon6");
		device.setScreenResolution("1920x1280");

		userInfo.setDevice(device);

		return executeRequest(url, userInfo);
	}
	
	public ClientResponse saveAllRoutesInfo(){
		String url = "/availableCoriders";
		SearchParams params = new SearchParams();
		Location currentLocation = new Location();
		currentLocation.setCity("Patna");
		currentLocation.setCountry("India");
		currentLocation.setLatitude(28.7649474);
		currentLocation.setLongitude(76.93848575);
		params.setCurrentLocation(currentLocation);
		Location destination = new Location();
		destination.setCity("Daniwaya");
		destination.setCountry("India");
		destination.setLatitude(28.76492020);
		destination.setLongitude(75.93848575);
		params.setDestination(destination);
		User user = new User();
		user.setUserId("37");
		params.setUser(user);
		//set all routes.
		params.setAllRoutes(getAllRoutesData());
		return executeRequest(url, params);
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
