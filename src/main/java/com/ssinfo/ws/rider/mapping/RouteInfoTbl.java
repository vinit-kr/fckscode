package com.ssinfo.ws.rider.mapping;

// Generated 4 Oct, 2014 9:40:17 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RouteInfoTbl generated by hbm2java
 */
@Entity
@Table(name = "route_info_tbl", schema = "public")
public class RouteInfoTbl implements java.io.Serializable {

	private long routeId;

	public RouteInfoTbl() {
	}

	public RouteInfoTbl(long routeId) {
		this.routeId = routeId;
	}

	@Id
	@Column(name = "route_id", unique = true, nullable = false)
	public long getRouteId() {
		return this.routeId;
	}

	public void setRouteId(long routeId) {
		this.routeId = routeId;
	}

}
