package google;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import database.BusLocationsQuery;
import database.GraphHopperQuery;
import gps_log_processor.BusLocation;

public class GraphHopper {

	/*
	* Geocode request URL. Here see we are passing "json" it means we will get
	* the output in JSON format. You can also pass "xml" instead of "json" for
	* XML output. For XML output URL will be
	* "http://maps.googleapis.com/maps/api/geocode/xml";
	*/

	private static final String URLdistance = "http://192.168.1.200:8989/route";

	
	private static String getJSONGraphHopper(double latX, double lonX, double latY, double lonY) throws UnsupportedEncodingException {

		/*
		* Create an java.net.URL object by passing the request URL in
		* constructor. Here you can see I am converting the fullAddress String
		* in UTF-8 format. You will get Exception if you don't convert your
		* address in UTF-8 format. Perhaps google loves UTF-8 format. :) In
		* parameter we also need to pass "sensor" parameter. sensor (required
		* parameter) — Indicates whether or not the geocoding request comes
		* from a device with a location sensor. This value must be either true
		* or false.
		*/
		URL url = null;
		try {
			url = new URL(URLdistance + "?point="+latX+"%2C"+lonX+"&point="+latY+"%2C"+lonY);
			System.out.println(url.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// Open the Connection
		URLConnection conn = null;
		try {
			conn = url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// This is Simple a byte array output stream that we will use to keep
		// the output data from google.
		ByteArrayOutputStream output = new ByteArrayOutputStream(1024);

		// copying the output data from Google which will be either in JSON or
		// XML depending on your request URL that in which format you have
		// requested.
		try {
			IOUtils.copy(conn.getInputStream(), output);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// close the byte array output stream now.
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return output.toString(); // This returned String is JSON string from
									// which you can retrieve all key value pair
									// and can save it in POJO.
	}

	public static int[] getTimeAndDistanceByCoord(double latX, double lonX, double latY, double lonY) {
		String distanceFeed = null;
		try {
			distanceFeed = GraphHopper.getJSONGraphHopper(latX,lonX,latY,lonY);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		JSONObject ja = new JSONObject(distanceFeed);
		JSONArray row = (JSONArray) ja.get("paths");
		JSONObject ob = (JSONObject) row.get(0);
		int dist = ob.getInt("distance");
		int time = ob.getInt("time");
		return new int[]{dist, time};
	}
	
	//Fill the Graph Hopper Table in the database in function of the location in the table buspl_location
	public static void main(String[] args) {
		BusLocationsQuery bus = new BusLocationsQuery();
		ArrayList<BusLocation> tabBus = bus.getAllBusLocation();
		GraphHopperQuery query = new GraphHopperQuery();
		for (int i=0; i<tabBus.size(); i++){
			BusLocation a = tabBus.get(i);
			for (int j=i+1; j<tabBus.size(); j++){
				BusLocation z = tabBus.get(j);
				int[] result = GraphHopper.getTimeAndDistanceByCoord(a.getLat(), a.getLng(), z.getLat(), z.getLng());
				query.insert(a, z, result[0], result[1]);
				System.out.println("ajout "+((i*tabBus.size())+j));
			}
		}
		System.out.println("fin");
	}
}