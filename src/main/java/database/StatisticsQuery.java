package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gps_log_processor.BusLocation;
import gps_log_processor.LegCollection;
import gps_log_processor.TravelLeg;

public class StatisticsQuery {
	
	/**Connection to the DataBase**/
	private DBConnection base;

	
	public StatisticsQuery(){
		this.base = new DBConnection("antoine", "sofar", "localhost", 3306, "waypoints");
	}
	
	
	
	public void insertAll (List<LegCollection> list){
	    try {
	    	for (int i=0; i<list.size(); i++){
	    		LegCollection l = list.get(i);
	    		ResultSet result = this.select(l.getTag(), l.getMonth(), l.getYear(), l.getPointA(), l.getPointB());
	    		if (!result.last()){
	    			PreparedStatement prep = this.base.getCon().prepareStatement("DELETE FROM buspl_statistics WHERE TAG=? AND MONTH=? AND YEAR=? AND (POINT_A=? AND POINT_B=?) OR (POINT_B=? AND POINT_A=?)");
	    			prep.setString(1, l.getTag());
	    			prep.setString(2, l.getMonth());
	    			prep.setInt(3, l.getYear());
	    			prep.setString(4, l.getPointA().getName());
	    			prep.setString(5, l.getPointB().getName());
	    			prep.setString(6, l.getPointB().getName());
	    			prep.setString(7, l.getPointA().getName());
	    			prep.executeUpdate();
	    		}
				PreparedStatement prep = this.base.getCon().prepareStatement("insert into buspl_statistics values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				prep.setString(1, l.getTag());
				prep.setString(2, l.getMonth());
				prep.setInt(3, l.getYear());
				prep.setString(4, l.getPointA().getName());
				prep.setString(5, l.getPointB().getName());
				prep.setInt(6, l.getAvgStopsByTime().get(Duration.ofMinutes(1)));
				prep.setInt(7, l.getAvgStopsByTime().get(Duration.ofMinutes(5)));
				prep.setInt(8, l.getAvgStopsByTime().get(Duration.ofMinutes(10)));
				prep.setInt(9, (int)l.getAvgTravelTime().getSeconds());
				prep.setInt(10, (int)l.getSqrtAvgTravelTime().getSeconds());
				prep.setInt(11, l.getAvgDistance());
				prep.setInt(12, l.getLogedLegs().size());
				prep.setInt(13, (int)l.getMinTravelTime().getSeconds());
				prep.setInt(14, (int)l.getMaxTravelTime().getSeconds());

				prep.executeUpdate();
				prep.close();
	    	}
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getAvgTravelTime (String from, String to){
		try {
			PreparedStatement prep;
			prep = this.base.getCon().prepareStatement("SELECT AVG(MINTIME) FROM buspl_statistics WHERE (POINT_A=? and POINT_B=?) or (POINT_A=? and POINT_B=?);");
			prep.setString(1, from);
			prep.setString(2, to);
			prep.setString(3, to);
			prep.setString(4, from);
			ResultSet rs = prep.executeQuery();
			while(rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public LegCollection selectMin (BusLocation PointA, BusLocation PointB, int month, int year){
		try {
			PreparedStatement prep;
			if (month > 0 && month < 13 && year > 0){
				prep = this.base.getCon().prepareStatement("Select STOP_1_5, STOP_5_10, STOP_10, AVGTIME, SQRTTIME, AVGDISTANCE, OCCUR, MINTIME, MIN(MINTIME), MAXTIME from buspl_statistics where (POINT_A=? and POINT_B=?) or (POINT_A=? and POINT_B=?) and MONTH=? and YEAR=?");
				prep.setString(1, PointA.getName());
				prep.setString(2, PointB.getName());
				prep.setString(3, PointB.getName());
				prep.setString(4, PointA.getName());
				prep.setString(5, Month.of(month).toString());
				prep.setInt(6, year);
			}else if(year > 0){
				prep = this.base.getCon().prepareStatement("Select STOP_1_5, STOP_5_10, STOP_10, AVGTIME, SQRTTIME, AVGDISTANCE, OCCUR, MINTIME, MIN(MINTIME), MAXTIME from buspl_statistics where (POINT_A=? and POINT_B=?) or (POINT_A=? and POINT_B=?) and YEAR=?");
				prep.setString(1, PointA.getName());
				prep.setString(2, PointB.getName());
				prep.setString(3, PointB.getName());
				prep.setString(4, PointA.getName());
				prep.setInt(5, year);				
			}else if(month > 0 && month < 13){
				prep = this.base.getCon().prepareStatement("Select STOP_1_5, STOP_5_10, STOP_10, AVGTIME, SQRTTIME, AVGDISTANCE, OCCUR, MINTIME, MIN(MINTIME), MAXTIME from buspl_statistics where (POINT_A=? and POINT_B=?) or (POINT_A=? and POINT_B=?) and MONTH=?");
				prep.setString(1, PointA.getName());
				prep.setString(2, PointB.getName());
				prep.setString(3, PointB.getName());
				prep.setString(4, PointA.getName());
				prep.setString(5, Month.of(month).toString());					
			}else{
				prep = this.base.getCon().prepareStatement("Select STOP_1_5, STOP_5_10, STOP_10, AVGTIME, SQRTTIME, AVGDISTANCE, OCCUR, MINTIME, MIN(MINTIME), MAXTIME from buspl_statistics where (POINT_A=? and POINT_B=?) or (POINT_A=? and POINT_B=?)");
				prep.setString(1, PointA.getName());
				prep.setString(2, PointB.getName());
				prep.setString(3, PointB.getName());
				prep.setString(4, PointA.getName());
			}
			ResultSet rs = prep.executeQuery();
			LegCollection lg = new LegCollection();
			lg.setPointA(PointA);
			lg.setPointB(PointB);
			while (rs.next()) {
				System.out.println("next");
				HashMap<Duration, Integer> map = new HashMap<Duration,Integer>();
				map.put(Duration.ofMinutes(1), rs.getInt(1));
				map.put(Duration.ofMinutes(5), rs.getInt(2));
				map.put(Duration.ofMinutes(10), rs.getInt(3));
				lg.setAvgStopsByTime(map);
				lg.setAvgTravelTime(Duration.ofSeconds(rs.getLong(4)));
				System.out.println(lg.getAvgTravelTime().getSeconds());
				lg.setSqrtAvgTravelTime(Duration.ofSeconds(rs.getLong(5)));
				lg.setAvgDistance(rs.getInt(6));
				lg.setLogedLegs(new ArrayList<TravelLeg>(rs.getInt(7)));
				lg.setMinTravelTime(Duration.ofSeconds(rs.getLong(8)));
				lg.setMaxTravelTime(Duration.ofSeconds(9));
			}
			return lg;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int selectMinTime (String PointA, String PointB){
		try {
			PreparedStatement prep;
			prep = this.base.getCon().prepareStatement("Select MIN(MINTIME) from buspl_statistics where (POINT_A=? and POINT_B=?) or (POINT_A=? and POINT_B=?)");
			prep.setString(1, PointA);
			prep.setString(2, PointB);
			prep.setString(3, PointB);
			prep.setString(4, PointA);
			
			ResultSet rs = prep.executeQuery();
			int min = -1;
			while (rs.next()) {
				min = rs.getInt(1);
			}
			return min;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public ResultSet select (String tag, String month, int year, BusLocation pointA, BusLocation pointB){
		try{
			PreparedStatement prep;
			prep = base.getCon().prepareStatement("SELECT * FROM buspl_statistics where TAG=? AND MONTH=? AND YEAR=? AND (POINT_A=? AND POINT_B=?) OR (POINT_B=? AND POINT_A=?);");
			prep.setString(1, tag);
			prep.setString(2, month);
			prep.setInt(3, year);
			prep.setString(4, pointA.getName());
			prep.setString(5, pointB.getName());
			prep.setString(6, pointB.getName());
			prep.setString(7, pointA.getName());
			return prep.executeQuery();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void close(){
		this.base.closeConnection();
	}
}
