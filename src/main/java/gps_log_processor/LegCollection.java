package gps_log_processor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LegCollection {
	private String tag;
	private String month;
	private int year;
	private BusLocation pointA;
	private BusLocation pointB;
	private Duration avgTravelTime;
	private Duration sqrtAvgTravelTime;
	private HashMap<Duration, Integer> avgStopsByTime;
	private int avgDistance;
	private Duration minTravelTime;
	private Duration maxTravelTime;
	List<TravelLeg> logedLegs;
	
	public LegCollection(){}
	
	public LegCollection(TravelLeg tl) {
		this.pointA = tl.getPointA();
		this.pointB = tl.getPointB();
		this.avgTravelTime = Duration.ofSeconds(0);
		this.avgStopsByTime = new HashMap<Duration, Integer>();
		this.avgStopsByTime.put(Duration.ofMinutes(1), 0);
		this.avgStopsByTime.put(Duration.ofMinutes(5), 0);
		this.avgStopsByTime.put(Duration.ofMinutes(10), 0);
		this.avgDistance = 0;
		this.minTravelTime = Duration.ofDays(99);
		this.maxTravelTime = Duration.ofSeconds(0);
		this.sqrtAvgTravelTime = Duration.ofSeconds(0);
		this.logedLegs = new ArrayList<TravelLeg>();
		this.logedLegs.add(tl);
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
	public Duration getAvgTravelTime() {
		return avgTravelTime;
	}
	public void setAvgTravelTime(Duration avgTravelTime) {
		this.avgTravelTime = avgTravelTime;
	}
	public HashMap<Duration, Integer> getAvgStopsByTime() {
		return avgStopsByTime;
	}
	public void setAvgStopsByTime(HashMap<Duration, Integer> avgStopsByTime) {
		this.avgStopsByTime = avgStopsByTime;
	}
	public int getAvgDistance() {
		return avgDistance;
	}
	public void setAvgDistance(int avgDistance) {
		this.avgDistance = avgDistance;
	}
	public List<TravelLeg> getLogedLegs() {
		return logedLegs;
	}
	public void setLogedLegs(List<TravelLeg> logedLegs) {
		this.logedLegs = logedLegs;
	}
	public Duration getMinTravelTime() {
		return minTravelTime;
	}
	public void setMinTravelTime(Duration minTravelTime) {
		this.minTravelTime = minTravelTime;
	}
	public Duration getMaxTravelTime() {
		return maxTravelTime;
	}
	public void setMaxTravelTime(Duration maxTravelTime) {
		this.maxTravelTime = maxTravelTime;
	}
	public Duration getSqrtAvgTravelTime() {
		return sqrtAvgTravelTime;
	}
	public void setSqrtAvgTravelTime(Duration sqrtAvgTravelTime) {
		this.sqrtAvgTravelTime = sqrtAvgTravelTime;
	}
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public void statProcess(){
		for (TravelLeg tmpTl : this.logedLegs){
			if(tmpTl.getTravelTime().compareTo(this.minTravelTime)<0)
				this.minTravelTime = tmpTl.getTravelTime();
			if(tmpTl.getTravelTime().compareTo(this.maxTravelTime)>0)
				this.maxTravelTime = tmpTl.getTravelTime();
			
			this.avgDistance += tmpTl.getDistance();
			this.avgStopsByTime.put(Duration.ofMinutes(1), this.avgStopsByTime.get(Duration.ofMinutes(1))+tmpTl.getStopsByTime().get(Duration.ofMinutes(1)));
			this.avgStopsByTime.put(Duration.ofMinutes(5), this.avgStopsByTime.get(Duration.ofMinutes(5))+tmpTl.getStopsByTime().get(Duration.ofMinutes(5)));
			this.avgStopsByTime.put(Duration.ofMinutes(10), this.avgStopsByTime.get(Duration.ofMinutes(10))+tmpTl.getStopsByTime().get(Duration.ofMinutes(10)));
			this.avgTravelTime = this.avgTravelTime.plus(tmpTl.getTravelTime());
			this.sqrtAvgTravelTime = this.sqrtAvgTravelTime.plus(Duration.ofSeconds((long)Math.pow(((double)tmpTl.getTravelTime().getSeconds()),2)));
		}
		this.avgDistance /= this.logedLegs.size();
		this.avgTravelTime = this.avgTravelTime.dividedBy(this.logedLegs.size());
		this.sqrtAvgTravelTime = this.sqrtAvgTravelTime.dividedBy((long)Math.pow(this.logedLegs.size(), 2));
		this.sqrtAvgTravelTime = Duration.ofSeconds((long)Math.sqrt((double)this.sqrtAvgTravelTime.getSeconds()));
	}
	public void add(TravelLeg tl) {
		this.logedLegs.add(tl);
	}
}
