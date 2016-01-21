package gps_log_processor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class WayPoint {
	private int id;
	private float xlon;
	private float ylat;
	private String busid;
	private int time;
	private float xlon0;
	private float xlat0;
	private String engineStatus;
	private String generalStatus;
	private float distance;
	private float speed;
	
	public WayPoint(int id, float xlon, float ylat, String busid, int time, float xlon0, float xlat0,
			String engineStatus, String generalStatus, float distance, float speed) {
		super();
		this.id = id;
		this.xlon = xlon;
		this.ylat = ylat;
		this.busid = busid;
		this.time = time;
		this.xlon0 = xlon0;
		this.xlat0 = xlat0;
		this.engineStatus = engineStatus;
		this.generalStatus = generalStatus;
		this.distance = distance;
		this.speed = speed;
	}
	
	public WayPoint(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getXlon() {
		return xlon;
	}

	public void setXlon(float xlon) {
		this.xlon = xlon;
	}

	public float getYlat() {
		return ylat;
	}

	public void setYlat(float ylat) {
		this.ylat = ylat;
	}

	public String getBusid() {
		return busid;
	}

	public void setBusid(String busid) {
		this.busid = busid;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public float getXlon0() {
		return xlon0;
	}

	public void setXlon0(float xlon0) {
		this.xlon0 = xlon0;
	}

	public float getXlat0() {
		return xlat0;
	}

	public void setXlat0(float xlat0) {
		this.xlat0 = xlat0;
	}

	public String getEngineStatus() {
		return engineStatus;
	}

	public void setEngineStatus(String engineStatus) {
		this.engineStatus = engineStatus;
	}

	public String getGeneralStatus() {
		return generalStatus;
	}

	public void setGeneralStatus(String generalStatus) {
		this.generalStatus = generalStatus;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	@Override
	public String toString() {
		return "WayPoint [id=" + id + ", xlon=" + xlon + ", ylat=" + ylat + ", busid=" + busid + ", time=" + time
				+ ", xlon0=" + xlon0 + ", xlat0=" + xlat0 + ", engineStatus=" + engineStatus + ", generalStatus="
				+ generalStatus + ", distance=" + distance + ", speed=" + speed + "]";
	}

	public double[] getCoord() {
		return new double[]{this.ylat, this.xlon};
	};
	
	public LocalDateTime getLocalDateTime(){
		return LocalDateTime.ofEpochSecond(this.time, 0, ZoneOffset.ofHours(2));
	}
	
	public double calculateDistance(WayPoint point){
		double[] coordDepart = this.getCoord();
		double[] coordArrival = point.getCoord();
		double rlat1 = Math.PI * coordDepart[0]/180;
	    double rlat2 = Math.PI * coordArrival[0]/180;
	 
	    double theta = coordDepart[1]-coordArrival[1];
	    double rtheta = Math.PI * theta/180;
	 
	    double dist = Math.sin(rlat1) * Math.sin(rlat2) + Math.cos(rlat1) * Math.cos(rlat2) * Math.cos(rtheta);
	    double ret = Math.acos(dist) * 180/Math.PI * 60 * 1.1515 * 1.609344 * 1000;
	    return ret;
	}
}
