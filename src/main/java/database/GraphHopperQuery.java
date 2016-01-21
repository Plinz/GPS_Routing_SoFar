package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import gps_log_processor.BusLocation;

public class GraphHopperQuery {
	
	/**Connection to the DataBase**/
	private DBConnection base;

	
	public GraphHopperQuery(){
		this.base = new DBConnection("antoine", "sofar", "localhost", 3306, "waypoints");
	}
	
	
	
	public void insert (BusLocation pointA, BusLocation pointB, int distance, int time){
	    try {
	    	int result = this.selectDistance(pointA, pointB);
	    	if(result == -1){
				PreparedStatement prep = this.base.getCon().prepareStatement("INSERT into buspl_graphhopper values (?, ?, ?, ?);");
				prep.setString(1, pointA.getName());
				prep.setString(2, pointB.getName());
				prep.setInt(3, distance);
				prep.setInt(4, time);
				prep.executeUpdate();
				prep.close();
	    	}
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int selectDistance (BusLocation pointA, BusLocation pointB){
		try{
			PreparedStatement prep;
			prep = base.getCon().prepareStatement("SELECT DISTANCE FROM buspl_graphhopper where (POINT_A=? AND POINT_B=?) OR (POINT_B=? AND POINT_A=?);");
			prep.setString(1, pointA.getName());
			prep.setString(2, pointB.getName());
			prep.setString(3, pointA.getName());
			prep.setString(4, pointB.getName());
			ResultSet result = prep.executeQuery();
			result.next();
			int dist = result.getInt(1);
			prep.close();
			return dist;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return -1;
	}

	public int[] selectDistanceTime(BusLocation pointA, BusLocation pointB) {
		try{
			PreparedStatement prep;
			prep = base.getCon().prepareStatement("SELECT DISTANCE, TIME FROM buspl_graphhopper where (POINT_A=? AND POINT_B=?) OR (POINT_B=? AND POINT_A=?);");
			prep.setString(1, pointA.getName());
			prep.setString(2, pointB.getName());
			prep.setString(3, pointA.getName());
			prep.setString(4, pointB.getName());
			ResultSet result = prep.executeQuery();
			int [] ret = new int [2];
			while(result.next()){
				ret[0] = result.getInt(1);
				ret[1] = result.getInt(2);
			}
			prep.close();
			return ret;
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
