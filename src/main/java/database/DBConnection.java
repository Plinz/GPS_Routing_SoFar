package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * You will get the following exception if the credentials are wrong:
 * 
 * java.sql.SQLException: Access denied for user 'userName'@'localhost' (using password: YES)
 * 
 * You will instead get the following exception if MySQL isn't installed, isn't
 * running, or if your serverName or portNumber are wrong:
 * 
 * java.net.ConnectException: Connection refused
 */

public class DBConnection {

	/** The name of the MySQL account to use (or empty for anonymous) */
	private String userName = "antoine";

	/** The password for the MySQL account (or empty for anonymous) */
	private String password = "sofar";

	/** The name of the computer running MySQL */
	private String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private int portNumber = 3306;

	/** The name of the database*/
	private String dbName = "waypoints";
		
	/** The Connection to the DataBase*/
	private Connection con;
	
	public DBConnection(String user, String pwd, String server, int port, String dbName){
		this.userName = user;
		this.password = pwd;
		this.serverName = server;
		this.portNumber = port;
		//this.dbName = dbName;
		
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);

		try {
			this.con = DriverManager.getConnection("jdbc:mysql://"
					+ this.serverName + ":" + this.portNumber + "/" + this.dbName,
					connectionProps);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.con = null;
			e.printStackTrace();
		}
	}

	public void closeConnection(){
		if (this.con!=null){
			try {
				this.con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Run a SQL command which does not return a recordset:
	 * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
	 * 
	 * @throws SQLException If something goes wrong
	 */
	public boolean executeUpdate(String command) throws SQLException {
	    Statement stmt = null;
	    try {
	        stmt = this.con.createStatement();
	        stmt.executeUpdate(command); // This will throw a SQLException if it fails
	        return true;
	    } finally {

	    	// This will run whether we throw an exception or not
	        if (stmt != null) { stmt.close(); }
	    }
	}

	public Connection getCon() {
		return con;
	}	
}