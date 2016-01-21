package delay;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ScheduleServerConnection {
	private static final String URLdistance = "http://192.168.1.201:8080/route/JsonExporter";

	private static String getJSONService(LocalDate date) throws UnsupportedEncodingException {

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
			url = new URL(URLdistance + "?date="+date.getDayOfMonth()+"/"+date.getMonthValue()+"/"+date.getYear());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Open the Connection
		URLConnection conn = null;
		try {
			conn = url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// close the byte array output stream now.
		try {
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return output.toString(); // This returned String is JSON string from
									// which you can retrieve all key value pair
									// and can save it in POJO.
	}

	public static List<Travel> getFormatedData(LocalDate date) {
		String distanceFeed = null;
		try {
			distanceFeed = ScheduleServerConnection.getJSONService(date);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject ja = new JSONObject(distanceFeed);
		JSONArray array = ja.getJSONArray("routes");
		List<Travel> list = new ArrayList<Travel>();
		for (int i=0; i<array.length(); i++){
			JSONObject ob = (JSONObject) array.get(i);
			String [] stops = ob.getString("stops").split("-");
			int startHour = Integer.parseInt(ob.getString("stT").split(":")[0]);
			int startMinute = Integer.parseInt(ob.getString("stT").split(":")[1]);
			int endHour = Integer.parseInt(ob.getString("edT").split(":")[0]);
			int endMinute = Integer.parseInt(ob.getString("edT").split(":")[1]);

			list.add(new Travel(stops , date.atTime(LocalTime.of(startHour, startMinute)), date.atTime(LocalTime.of(endHour, endMinute)), ob.getString("bus").replace(" ", "")));

		}
		return list;
	}
}
