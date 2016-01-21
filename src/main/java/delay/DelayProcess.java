package delay;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import database.StatisticsQuery;

public class DelayProcess {

	String currentTime;
	String currentDate;
	String busId;
	String currentLocation;
	Duration delay;
	LocalDateTime currentDateTime;
	HashMap<String, String> greeklish;
	
	public DelayProcess(){
		
	}
	
	public DelayProcess(String currentDate,  String busId, String currentLocation, String currentTime) {
		greeklish = new HashMap<String, String>();
		greeklish.put("ά" , "a"); greeklish.put( "α" , "a"); greeklish.put( "Α" , "A"); greeklish.put( "Ά" , "A"); greeklish.put(
		        "β" , "v"); greeklish.put( "Β" , "V"); greeklish.put(
		        "γ" , "g"); greeklish.put( "Γ" , "G"); greeklish.put(
		        "δ" , "d"); greeklish.put( "Δ" , "D"); greeklish.put(
		        "έ" , "e"); greeklish.put( "ε" , "e"); greeklish.put( "Ε" , "E"); greeklish.put( "Έ" , "E"); greeklish.put(
		        "ζ" , "z"); greeklish.put( "Ζ" , "Z"); greeklish.put(
		        "ή" , "h"); greeklish.put( "η" , "h"); greeklish.put( "Η" , "H"); greeklish.put( "Ή" , "H"); greeklish.put(
		        "θ" , "th"); greeklish.put( "Θ" , "TH"); greeklish.put(
		        "ί" , "i"); greeklish.put( "ϊ" , "i"); greeklish.put( "ΐ" , "i"); greeklish.put( "ι" , "i"); greeklish.put( "Ι" , "I"); greeklish.put( "Ί" , "I"); greeklish.put(
		        "κ" , "k"); greeklish.put( "Κ" , "K"); greeklish.put(
		        "λ" , "l"); greeklish.put( "Λ" , "L"); greeklish.put(
		        "μ" , "m"); greeklish.put( "Μ" , "M"); greeklish.put(
		        "ν" , "n"); greeklish.put( "Ν" , "N"); greeklish.put(
		        "ξ" , "ks"); greeklish.put( "Ξ" , "KS"); greeklish.put(
		        "ό" , "o"); greeklish.put( "ο" , "o"); greeklish.put( "Ό" , "O"); greeklish.put("Ο" , "O"); greeklish.put(
		        "π" , "p"); greeklish.put( "Π" , "P"); greeklish.put(
		        "ρ" , "r"); greeklish.put( "Ρ" , "R"); greeklish.put(
		        "σ" , "s"); greeklish.put( "Σ" , "S"); greeklish.put( "ς" , "s"); greeklish.put(
		        "τ" , "t"); greeklish.put( "Τ" , "T"); greeklish.put(
		        "ύ" , "y"); greeklish.put( "υ" , "y"); greeklish.put( "Ύ" , "Y"); greeklish.put("Υ" , "Y"); greeklish.put(
		        "φ" , "f"); greeklish.put( "Φ" , "F"); greeklish.put(
		        "χ" , "x"); greeklish.put( "Χ" , "X"); greeklish.put(
		        "ψ" , "ps"); greeklish.put( "Ψ" , "PS"); greeklish.put(
		        "ώ" , "w"); greeklish.put( "ω" , "w"); greeklish.put( "Ώ" , "W"); greeklish.put("Ω" , "W");
		if (currentTime != null)
			this.currentTime = currentTime;
		if (currentDate != null)
			this.currentDate = currentDate;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		this.currentDateTime = LocalDateTime.of(LocalDate.parse(this.currentDate, dtf), LocalTime.parse(this.currentTime));
		this.busId = busId;
		this.currentLocation = currentLocation;
		this.delay = null;
	}
	
	public void process(){
		List<Travel> list = ScheduleServerConnection.getFormatedData(currentDateTime.toLocalDate());
		
		for(Travel t : list){
			
			if (t.busId.replaceAll("\\s+","").equals(this.busId.replaceAll("\\s+",""))){
				System.out.println("-------------------------BUS ID OK");
				String log ="";
				for(String s : t.stops)
					log+=" "+s;
				System.out.println(t.busId+" "+log);
				String[] stops = t.getStops();
				if (stops[0].equals(currentLocation)){
					System.out.println("----------GOOD LOCATION");
					if (this.delay == null || Duration.between(t.start, this.currentDateTime).abs().compareTo(this.delay.abs())<0){
						this.delay = Duration.between(t.start, this.currentDateTime);
						System.out.println("delay :"+this.delay);
					}
				}
				else{
					StatisticsQuery query = new StatisticsQuery();
					LocalTime minTimeOnStop = t.start.toLocalTime();
					for(int i=1; i<stops.length; i++){
						String currentStop = stops[i];
						minTimeOnStop.plus(Duration.ofSeconds(query.selectMinTime(stops[i-1], currentStop)));
						if(currentStop.equals(currentLocation)){
							System.out.println("----------GOOD LOCATION");
							if(this.delay == null || (Duration.between(minTimeOnStop, this.currentDateTime).abs().compareTo(this.delay.abs()))<0){
								this.delay = Duration.between(minTimeOnStop, this.currentDateTime);
								System.out.println("delay :"+this.delay+" compare"+Duration.between(minTimeOnStop, this.currentDateTime).abs().compareTo(this.delay.abs()));	
							}
						}
					}						
				}
			}
		}
	}

	public String getBusId() {
		return busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public Duration getDelay() {
		return delay;
	}

	public void setDelay(Duration delay) {
		this.delay = delay;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public LocalDateTime getCurrentDateTime() {
		return currentDateTime;
	}

	public void setCurrentDateTime(LocalDateTime currentDateTime) {
		this.currentDateTime = currentDateTime;
	}

	public HashMap<String, String> getGreeklish() {
		return greeklish;
	}

	public void setGreeklish(HashMap<String, String> greeklish) {
		this.greeklish = greeklish;
	}
}
