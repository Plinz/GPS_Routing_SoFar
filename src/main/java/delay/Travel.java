package delay;

import java.time.LocalDateTime;

public class Travel {
	String[] stops;
	LocalDateTime start;
	LocalDateTime end;
	String busId;
	
	
	public Travel(String[] stops, LocalDateTime start, LocalDateTime end, String busId) {
		this.stops = stops;
		this.start = start;
		this.end = end;
		this.busId = busId;
	}
	
	public String[]getStops() {
		return stops;
	}
	public void setStop(String[] stops) {
		this.stops = stops;
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
	public String getBusId() {
		return busId;
	}
	public void setBusId(String busId) {
		this.busId = busId;
	}

}
