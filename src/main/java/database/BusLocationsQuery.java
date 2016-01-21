package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import gps_log_processor.BusLocation;

public class BusLocationsQuery {
	
	/**Connection to the DataBase**/
	private DBConnection base;

	
	public BusLocationsQuery(){
		this.base = new DBConnection("antoine", "sofar", "localhost", 3306, "waypoints");
	}
	
	
	
	/**
	 * Get all the Bus Location
           
	 * @return	List of bus locations
	 */
	@SuppressWarnings("finally")
	public ArrayList<BusLocation> getAllBusLocation() {
		ArrayList<BusLocation> busLocations = new ArrayList<BusLocation>();
		try {
			PreparedStatement prep = this.base.getCon().prepareStatement("Select * from buspl_locations");
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				busLocations.add(new BusLocation(rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5), rs.getInt(6)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			return busLocations;
		}
	}
	
	/**
	 * Get a Bus Location by Name
	 * 
	 * @param 
	 *            Name of the Location
	 * @return	 Bus Location
	 */
	public BusLocation getBusLocationByName(String name) {
		try {
			PreparedStatement prep = this.base.getCon().prepareStatement("Select * from buspl_locations where name=?;");
			prep.setString(1, name);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				return (new BusLocation(rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5), rs.getInt(6)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Get a Bus Location by Code
	 * 
	 * @param 
	 *            Code of the Location
	 * @return	 Bus Location
	 */
	public BusLocation getBusLocationByCode(String code) {
		try {
			PreparedStatement prep = this.base.getCon().prepareStatement("Select * from buspl_locations where code=?;");
			prep.setString(1, code);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				return (new BusLocation(rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5), rs.getInt(6)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void close(){
		this.base.closeConnection();
	}
}
