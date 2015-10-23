package com.ssinfo.ws.rider.mapping;

// Generated 4 Oct, 2014 9:40:17 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * LocationInfoTbl generated by hbm2java
 */
@Entity
@Table(name = "location_info_tbl", schema = "public")
public class LocationInfoTbl implements java.io.Serializable {

	private long locationId;
	//private SearchInfoTbl searchInfoTbl;
	private double latitude;
	private double longitude;
	private String locationName;
	private String locationAddress;
	private String city;
	private String country;
	private String locationType;
	private Set<StepsTbl> stepsTblsForStartLocationId = new HashSet<StepsTbl>(0);
	private Set<LegsTbl> legsTblsForStartLocationId = new HashSet<LegsTbl>(0);
	private Set<StepsTbl> stepsTblsForEndLocationId = new HashSet<StepsTbl>(0);
	private Set<LegsTbl> legsTblsForEndLocationId = new HashSet<LegsTbl>(0);

	public LocationInfoTbl() {
	}

	public LocationInfoTbl(long locationIdl) {
		this.locationId = locationId;
		
	}

	public LocationInfoTbl(long locationId,
			double latitude, double longitude, String locationName,
			String locationAddress, String city, String country,
			String locationType, Set<StepsTbl> stepsTblsForStartLocationId,
			Set<LegsTbl> legsTblsForStartLocationId,
			Set<StepsTbl> stepsTblsForEndLocationId,
			Set<LegsTbl> legsTblsForEndLocationId) {
		this.locationId = locationId;
		//this.searchInfoTbl = searchInfoTbl;
		this.latitude = latitude;
		this.longitude = longitude;
		this.locationName = locationName;
		this.locationAddress = locationAddress;
		this.city = city;
		this.country = country;
		this.locationType = locationType;
		this.stepsTblsForStartLocationId = stepsTblsForStartLocationId;
		this.legsTblsForStartLocationId = legsTblsForStartLocationId;
		this.stepsTblsForEndLocationId = stepsTblsForEndLocationId;
		this.legsTblsForEndLocationId = legsTblsForEndLocationId;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="search_sequence")
	@SequenceGenerator(name="search_sequence",sequenceName="search_sequence",	allocationSize=1)
	@Column(name = "location_id", unique = true, nullable = false)
	public long getLocationId() {
		return this.locationId;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	
	@Column(name = "latitude")
	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Column(name = "longitude")
	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Column(name = "location_name", length = 100)
	public String getLocationName() {
		return this.locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	@Column(name = "location_address", length = 200)
	public String getLocationAddress() {
		return this.locationAddress;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}

	@Column(name = "city", length = 50)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "country", length = 50)
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "location_type", length = 50)
	public String getLocationType() {
		return this.locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "locationInfoTblByStartLocationId")
	public Set<StepsTbl> getStepsTblsForStartLocationId() {
		return this.stepsTblsForStartLocationId;
	}

	public void setStepsTblsForStartLocationId(
			Set<StepsTbl> stepsTblsForStartLocationId) {
		this.stepsTblsForStartLocationId = stepsTblsForStartLocationId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "locationInfoTblByStartLocationId")
	public Set<LegsTbl> getLegsTblsForStartLocationId() {
		return this.legsTblsForStartLocationId;
	}

	public void setLegsTblsForStartLocationId(
			Set<LegsTbl> legsTblsForStartLocationId) {
		this.legsTblsForStartLocationId = legsTblsForStartLocationId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "locationInfoTblByEndLocationId")
	public Set<StepsTbl> getStepsTblsForEndLocationId() {
		return this.stepsTblsForEndLocationId;
	}

	public void setStepsTblsForEndLocationId(
			Set<StepsTbl> stepsTblsForEndLocationId) {
		this.stepsTblsForEndLocationId = stepsTblsForEndLocationId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "locationInfoTblByEndLocationId")
	public Set<LegsTbl> getLegsTblsForEndLocationId() {
		return this.legsTblsForEndLocationId;
	}

	public void setLegsTblsForEndLocationId(
			Set<LegsTbl> legsTblsForEndLocationId) {
		this.legsTblsForEndLocationId = legsTblsForEndLocationId;
	}

}
