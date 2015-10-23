package com.ssinfo.corider.ws.model;

public class AllRoutes {
	
	public AllRoutes(){
		
	}
	private String status;

	private Routes[] routes;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Routes[] getRoutes() {
		return routes;
	}

	public void setRoutes(Routes[] routes) {
		this.routes = routes;
	}

}
