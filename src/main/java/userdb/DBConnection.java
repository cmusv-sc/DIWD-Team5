package userdb;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	static final String JDBC = "com.mysql.jdbc.Driver";
	static final String LHURL = "jdbc:mysql://localhost:3306/";
	public static final String URL = LHURL + "DATA";
	static final String USER = "root";
	static final String PASSWORD = "root";
	static final String dropDB = "DROP DATABASE DATA";
	static final String createDB = "CREATE DATABASE IF NOT EXISTS DATA";
	static final String useDB = "USE DATA";
	static final String createUserTB = "CREATE TABLE IF NOT EXISTS DATA.USER"
			+ "(id INTEGER not NULL AUTO_INCREMENT, FirstName TEXT, LastName TEXT, "
			+ "Password TEXT, Email TEXT, PRIMARY KEY (id));";
	static final String createHisTB = "CREATE TABLE IF NOT EXISTS DATA.HISTORY"
			+ "(id INTEGER not NULL AUTO_INCREMENT, item TEXT, usrid INTEGER, "
			+ "PRIMARY KEY (id), FOREIGN KEY(usrid) REFERENCES USER(id) ON DELETE CASCADE);";
	static final String createFollowTB = "CREATE TABLE IF NOT EXISTS DATA.FOLLOW"
			+ "(id INTEGER not NULL AUTO_INCREMENT, befollowedid INTEGER, followerid INTEGER, "
			+ "PRIMARY KEY (id), FOREIGN KEY(befollowedid) REFERENCES USER(id) ON DELETE CASCADE, "
			+ "FOREIGN KEY(followerid) REFERENCES USER(id) ON DELETE CASCADE)";
	
	//Open a Connection from local host
	public Connection getLHConnection(String host) throws SQLException{
		Connection conn = DriverManager.getConnection(host, USER, PASSWORD);
		return conn!=null ? conn : null;
	}
	//Get a Connection of the LostandFoundDB database
	public Connection getConnection(String db) throws SQLException{
		return getLHConnection(db);
	}
	//Close Connection
	public void closeConnection(Connection conn) throws SQLException{
		if(conn!=null){
			conn.close();
		}
		else
			return;
	}
	//Close Statement
	public void closeStat(Statement st) throws SQLException{
		st.close();
	}
	
	public void createAutoTable() throws SQLException{
		Connection conn = null;
		Statement st = null;
		
		//Register a JDBC Driver
		try {
			Class.forName(JDBC);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Get a connection
		conn = getLHConnection(LHURL);
		st = conn.createStatement();
		st.executeUpdate(dropDB);
		st.executeUpdate(createDB);
		st.executeUpdate(useDB);
		st.executeUpdate(createUserTB);
		st.executeUpdate(createHisTB);
		st.executeUpdate(createFollowTB);
		//Close Connection
		closeStat(st);
		closeConnection(conn);
		System.out.println("Table created sucessfully!");
	}
}