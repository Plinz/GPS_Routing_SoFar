package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import gps_log_processor.WayPoint;

public class WayPointsQuery {
	
	/**Connection to the DataBase**/
	private DBConnection base;

	
	public WayPointsQuery(){
		this.base = new DBConnection("antoine", "sofar", "localhost", 3306, "waypoints");
	}
	
	/**
	 * Get all points where bus pass
	 * 
	 * @param 
	 *            id of the bus and limit (-1 of no limit)
	 * @return
	 */
	@SuppressWarnings("finally")
	public ArrayList<WayPoint> getWayPointsBybusid(String busid, int limit) {

		ArrayList<WayPoint> wayPoints = new ArrayList<WayPoint>();
		try {
			PreparedStatement prep;
			if (limit != -1){
				prep = base.getCon().prepareStatement("Select * from buspl_waypoints where busid=? limit ?;");
				prep.setString(1, busid);
				prep.setInt(2, limit);
			}
			else{
				prep = base.getCon().prepareStatement("Select * from buspl_waypoints where busid=?;");
				prep.setString(1, busid);
			}
				
	
			ResultSet rs = prep.executeQuery();;
			while (rs.next()) {
				wayPoints.add(new WayPoint(rs.getInt(1), rs.getFloat(2), rs.getFloat(3), rs.getString(4), rs.getInt(5), rs.getFloat(6), rs.getFloat(7), rs.getString(8), rs.getString(9), rs.getFloat(10), rs.getFloat(11)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			return wayPoints;
		}
	}
	
	/**
	 * Get all points where bus pass on this date
	 * 
	 * @param 
	 *            id of the bus, date search and limit (-1 of no limit)
	 * @return
	 */
	@SuppressWarnings("finally")
	public ArrayList<WayPoint> getWayPointsByDate(String busid, LocalDate date, int limit) {
		long start = date.atStartOfDay().toEpochSecond(ZoneOffset.ofHours(2));
		long end = date.plusDays(1).atStartOfDay().toEpochSecond(ZoneOffset.ofHours(2));
		
		ArrayList<WayPoint> wayPoints = new ArrayList<WayPoint>();
		try {
			PreparedStatement prep;
			if (limit != -1){
				prep = base.getCon().prepareStatement("Select * from buspl_waypoints where busid=? and time<? and time>? limit ?;");
				prep.setString(1, busid);
				prep.setLong(2, end);
				prep.setLong(3, start);
				prep.setInt(4, limit);
			}
			else{
				prep = base.getCon().prepareStatement("Select * from buspl_waypoints where busid=? and time<? and time>?;");
				prep.setString(1, busid);
				prep.setLong(2, end);
				prep.setLong(3, start);
			}
	
			ResultSet rs = prep.executeQuery();;
			while (rs.next()) {
				wayPoints.add(new WayPoint(rs.getInt(1), rs.getFloat(2), rs.getFloat(3), rs.getString(4), rs.getInt(5), rs.getFloat(6), rs.getFloat(7), rs.getString(8), rs.getString(9), rs.getFloat(10), rs.getFloat(11)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			return wayPoints;
		}
	}
	
	@SuppressWarnings("finally")
	public ArrayList<WayPoint> getWayPointsByDate(LocalDateTime from, LocalDateTime to, int limit) {
		long start = from.toEpochSecond(ZoneOffset.ofHours(2));
		long end = to.toEpochSecond(ZoneOffset.ofHours(2));
		
		ArrayList<WayPoint> wayPoints = new ArrayList<WayPoint>();
		try {
			PreparedStatement prep;
			if (limit != -1){
				prep = base.getCon().prepareStatement("Select * from buspl_waypoints where time<? and time>? limit ?;");
				prep.setLong(1, end);
				prep.setLong(2, start);
				prep.setInt(3, limit);
			}
			else{
				prep = base.getCon().prepareStatement("Select * from buspl_waypoints where time<? and time>?;");
				prep.setLong(1, end);
				prep.setLong(2, start);
			}
			ResultSet rs = prep.executeQuery();;
			while (rs.next()) {
				wayPoints.add(new WayPoint(rs.getInt(1), rs.getFloat(2), rs.getFloat(3), rs.getString(4), rs.getInt(5), rs.getFloat(6), rs.getFloat(7), rs.getString(8), rs.getString(9), rs.getFloat(10), rs.getFloat(11)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			return wayPoints;
		}
	}

	@SuppressWarnings("finally")
	public List<String> getBusIdByDate(LocalDateTime from, LocalDateTime to) {
		long start = from.toEpochSecond(ZoneOffset.ofHours(2));
		long end = to.toEpochSecond(ZoneOffset.ofHours(2));
		
		ArrayList<String> busId = new ArrayList<String>();
		try {
			PreparedStatement prep;
			
			prep = base.getCon().prepareStatement("Select distinct busid from buspl_waypoints where time<? and time>?;");
			prep.setLong(1, end);
			prep.setLong(2, start);
			ResultSet rs = prep.executeQuery();;
			while (rs.next()) {
				busId.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			return busId;
		}	}

	@SuppressWarnings("finally")
	public List<WayPoint> getWayPointsByDate(String busid, LocalDateTime from, LocalDateTime to, int limit) {
		long start = from.toEpochSecond(ZoneOffset.ofHours(2));
		long end = to.toEpochSecond(ZoneOffset.ofHours(2));
		
		ArrayList<WayPoint> wayPoints = new ArrayList<WayPoint>();
		try {
			PreparedStatement prep;
			if (limit != -1){
				prep = base.getCon().prepareStatement("Select * from buspl_waypoints where busid=? and time<? and time>? limit ?;");
				prep.setString(1, busid);
				prep.setLong(2, end);
				prep.setLong(3, start);
				prep.setInt(4, limit);
			}
			else{
				prep = base.getCon().prepareStatement("Select * from buspl_waypoints where busid=? and time<? and time>?;");
				prep.setString(1, busid);
				prep.setLong(2, end);
				prep.setLong(3, start);
			}
				
			ResultSet rs = prep.executeQuery();;
			while (rs.next()) {
				wayPoints.add(new WayPoint(rs.getInt(1), rs.getFloat(2), rs.getFloat(3), rs.getString(4), rs.getInt(5), rs.getFloat(6), rs.getFloat(7), rs.getString(8), rs.getString(9), rs.getFloat(10), rs.getFloat(11)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			return wayPoints;
		}
	}

	public void close(){
		this.base.closeConnection();
	}
	
}
