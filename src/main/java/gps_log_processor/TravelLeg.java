package gps_log_processor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class TravelLeg {
	private BusLocation pointA;
	private BusLocation pointB;
	private LocalDateTime start;
	private LocalDateTime end;
	private Duration travelTime;
	private HashMap<Duration, Integer> stopsByTime;
	private int distance;
	
	public TravelLeg (BusLocation pointA, BusLocation pointB, LocalDateTime start, LocalDateTime end, HashMap<Duration, Integer> stopsByTime, int distance){
		this.pointA = pointA;
		this.pointB = pointB;
		this.start = start;
		this.end = end;
		this.travelTime = Duration.between(start, end);
		this.distance = distance;
		this.stopsByTime = stopsByTime;
	}

	public BusLocation getPointA() {
		return pointA;
	}

	public void setPointA(BusLocation pointA) {
		this.pointA = pointA;
	}

	public BusLocation getPointB() {
		return pointB;
	}

	public void setPointB(BusLocation pointB) {
		this.pointB = pointB;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public Duration getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(Duration travelTime) {
		this.travelTime = travelTime;
	}

	public HashMap<Duration, Integer> getStopsByTime() {
		return stopsByTime;
	}

	public void setStopsByTime(HashMap<Duration, Integer> stopsByTime) {
		this.stopsByTime = stopsByTime;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

}
