package gps_log_processor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class GestionTimePeriod {

	private LocalDateTime from;
	private LocalDateTime to;

	public GestionTimePeriod() {
		this.from = LocalDateTime.now().minusDays(1);
		this.to = LocalDateTime.now();
	}

	public GestionTimePeriod(LocalDateTime from, LocalDateTime to) {
		this.from = from;
		this.to = to;
	}

	public GestionTimePeriod(String from, String to) {
		this.from = LocalDateTime.parse(from);
		this.to = LocalDateTime.parse(to);
	}

	public GestionTimePeriod(int yFrom, int moFrom, int dFrom, int hFrom, int yTo, int moTo, int dTo, int hTo) {
		this.from = LocalDateTime.of(yFrom, moFrom, dFrom, hFrom, 0);
		this.to = LocalDateTime.of(yTo, moTo, dTo, hTo, 0);
	}
	
	public List<LegCollection> NormalStats(){
		TimePeriod tp = new TimePeriod(this.from,this.to);
		tp.process();
		for(LegCollection lg: tp.getTravelStats()){
			lg.setMonth("NONE");
			lg.setYear(-1);
		}
		return tp.getTravelStats();
	}
	
	public List<LegCollection> statsCutByMonth(){
		int yearFrom = this.from.getYear();
		int yearTo = this.to.getYear();
		int monthFrom = this.from.getMonthValue();
		int monthTo = this.to.getMonthValue();
		List<LegCollection> list = new ArrayList<LegCollection>();
		
		if(yearFrom==yearTo){
			for (int i=monthFrom; i<monthTo+1; i++){
				TimePeriod tp = new TimePeriod(LocalDate.of(yearFrom, i, 1).atStartOfDay(), LocalDate.of(yearFrom, i, 1).plusMonths(1).atStartOfDay());
				tp.process();
				for (LegCollection lg: tp.getTravelStats()){
					lg.setMonth(Month.of(i).toString());
					lg.setYear(yearFrom);
				}
				list.addAll(tp.getTravelStats());
			}
		}
		else{
			int monthTmp = monthFrom;
			for (int i=yearFrom; i<yearTo; i++){
				while(monthTmp<13){
					TimePeriod tp = new TimePeriod(LocalDate.of(i, monthTmp, 1).atStartOfDay(), LocalDate.of(i, monthTmp, 1).plusMonths(1).atStartOfDay());
					tp.process();
					for(LegCollection lg: tp.getTravelStats()){
						lg.setMonth(Month.of(monthTmp).toString());
						lg.setYear(i);
					}
					list.addAll(tp.getTravelStats());
					monthTmp++;
				}
				monthTmp=1;
			}
			while(monthTmp<monthTo+1){
				TimePeriod tp = new TimePeriod(LocalDate.of(yearTo, monthTmp, 1).atStartOfDay(), LocalDate.of(yearTo, monthTmp, 1).plusMonths(1).atStartOfDay());
				tp.process();
				for(LegCollection lg: tp.getTravelStats()){
					lg.setMonth(Month.of(monthTmp).toString());
					lg.setYear(yearTo);
				}
				list.addAll(tp.getTravelStats());
				monthTmp++;
			}
		}		
		return list;
	}
}
