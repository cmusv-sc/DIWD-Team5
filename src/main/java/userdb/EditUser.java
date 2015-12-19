package userdb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import neo4j.domain.History;
import neo4j.domain.User;

public class EditUser extends DBConnection {
	
	public static final int PASS = 1;
	public static final int FAIL = 0;
	public static final int NOTFOUND = -1;
	
	private Connection conn = null;
	private Statement st = null;
	
	public void insertUser(User user){
		OpenDB();
		String firstname = user.getFirstName();
		String lastname = user.getLastName();
		String password = user.getPassword();
		String email = user.getEmail();
		try{
			st.execute("INSERT INTO USER (FirstName, LastName, Password, Email)"
					+ "VALUES('"+firstname+"', '"+lastname+"', '"+password+"', '"+email+"');");
		}catch(Exception e){
			System.out.println(e);
		}	
		closeDB();
	}
	
	public void appendHistory(User user, String history) {
		OpenDB();
		String email = user.getEmail();
		try{
			ResultSet res = st.executeQuery("SELECT * FROM USER WHERE Email = '"+email+"';");
			int usrId = -1;
			while(res.next()){
				usrId = res.getInt("id");
			}
			System.out.println("history::::!!!!!" + history + "!!!!!");
			st.executeUpdate("INSERT INTO HISTORY (item, usrid) " + "VALUES('"+history+"', '"+usrId+"');");
			res.close();
		}catch(Exception e){
			System.out.println(e);
		}	
		
		closeDB();
	}
	
	public List<History> getHistory(User user) {
		OpenDB();
		String email = user.getEmail();
		List<History> list = new ArrayList<History>();
		try{
			ResultSet res = st.executeQuery("SELECT * FROM USER WHERE Email = '"+email+"';");
			int usrId = -1;
			while(res.next()){
				usrId = res.getInt("id");
			}
			res = st.executeQuery("SELECT * FROM HISTORY WHERE usrid = '"+usrId +"';");
			while(res.next()){
				History his = new History(res.getInt("id"), res.getString("item"));
				list.add(his);
			}
			res.close();
		}catch(Exception e){
			System.out.println(e);
		}	
		
		closeDB();
		return list;
	}
	
	public boolean follow(User follower, User befollowed) {
		OpenDB();
		String followerEmail = follower.getEmail();
		String befollowedEmail = befollowed.getEmail();
		int followerId = -1;
		int beFollowedId = -1;
		try{
			ResultSet res = st.executeQuery("SELECT * FROM USER WHERE Email = '"+followerEmail + "';");
			while(res.next()){
				followerId = res.getInt("id");
			}
			
			res = st.executeQuery("SELECT * FROM USER WHERE Email = '"+befollowedEmail + "';");
			while(res.next()){
				beFollowedId = res.getInt("id");
			}
			res = st.executeQuery("SELECT * FROM FOLLOW WHERE befollowedid = '"+beFollowedId + "' AND followerid = '" + followerId + "';");
			if(res.next())
				return false;
			st.executeUpdate("INSERT INTO FOLLOW (befollowedid, followerid) " + "VALUES('"+beFollowedId+"', '"+followerId+"');");
			res.close();
		}catch(Exception e){
			System.out.println(e);
		}
		closeDB();
		return true;
	}
	
	public List<User> getFollows(User follower) {
		OpenDB();
		String email = follower.getEmail();
		List<User> list = new ArrayList<User>();
		List<Integer> followId = new ArrayList<Integer>();
		try{
			ResultSet res = st.executeQuery("SELECT * FROM USER WHERE Email = '"+email+"';");
			int usrId = -1;
			while(res.next()){
				usrId = res.getInt("id");
			}
			//Get all followers
			res = st.executeQuery("SELECT * FROM FOLLOW WHERE followerid = '"+usrId +"';");
			while(res.next()){
				followId.add(res.getInt("befollowedid"));
			}
			
			for (int id : followId) {
				res = st.executeQuery("SELECT * FROM USER WHERE id = '"+id+"';");
				while(res.next()){
					User user = new User(res.getString("FirstName"), res.getString("LastName")
						, null, res.getString("Email"));
					list.add(user);
				}
			}
			
			res.close();
		}catch(Exception e){
			System.out.println(e);
		}	
		
		closeDB();
		return list;
	}
	
	public List<User> getFollowers(User befollowed) {
		OpenDB();
		String email = befollowed.getEmail();
		List<User> list = new ArrayList<User>();
		List<Integer> followerId = new ArrayList<Integer>();
		try{
			ResultSet res = st.executeQuery("SELECT * FROM USER WHERE Email = '"+email+"';");
			int usrId = -1;
			while(res.next()){
				usrId = res.getInt("id");
			}
			//Get all followers
			res = st.executeQuery("SELECT * FROM FOLLOW WHERE befollowedid = '"+usrId +"';");
			while(res.next()){
				followerId.add(res.getInt("followerid"));
			}
			
			for (int id : followerId) {
				res = st.executeQuery("SELECT * FROM USER WHERE id = '"+id+"';");
				while(res.next()){
					User user = new User(res.getString("FirstName"), res.getString("LastName")
							, null, res.getString("Email"));
					list.add(user);
				}
			}
			
			res.close();
		}catch(Exception e){
			System.out.println(e);
		}	
		
		closeDB();
		return list;
	}
	
	public User getAllRelation(User self) {
		
		OpenDB();
		String email = self.getEmail();
		Set<Integer> follow = new HashSet<Integer>();
		try{
			ResultSet res = st.executeQuery("SELECT * FROM USER WHERE Email = '"+email+"';");
			int selfId = -1;
			while(res.next()){
				selfId = res.getInt("id");
			}
			//Get all followed users
			res = st.executeQuery("SELECT * FROM FOLLOW WHERE followerid = '"+selfId +"';");
			while(res.next()){
				follow.add(res.getInt("befollowedid"));
			}
			//Get all users not in followed list
			res = st.executeQuery("SELECT * FROM USER;");
			while(res.next()) {
				int usrId = res.getInt("id");
				if (usrId == selfId)
					continue;
				User user = new User(res.getString("FirstName"), res.getString("LastName")
						, null, res.getString("Email"));
				if (!follow.contains(usrId)) {
					self.addRelation(user, false);
				} else {
					self.addRelation(user, true);
				}
			}
			res.close();
		}catch(Exception e){
			System.out.println(e);
		}	
		
		closeDB();
		return self;
		
	}
	
	public boolean checkValid(User user) {
		OpenDB();
		String email = user.getEmail();
		int usrId = NOTFOUND;
		try{
			ResultSet res = st.executeQuery("SELECT * FROM USER WHERE Email = '"+email+"';");
			while(res.next()){
				usrId = res.getInt("id");
			}
			res.close();
		}catch(Exception e){
			System.out.println(e);
		}	
		closeDB();
		return usrId == NOTFOUND;
	}
	
	public int checkPassword(User user){
		
		OpenDB();
		String email = user.getEmail();
		String password = user.getPassword();
		int result = -99;
		try{
			// Get the CategoryID
			ResultSet res = st.executeQuery("SELECT * FROM USER WHERE Email = '"+email + "';");
			int usrID = NOTFOUND;
			String pw = null;
			while(res.next()){
				usrID = res.getInt("id");
				pw = res.getString("Password");
			}
			System.out.println("usrID= " + usrID);		
			if(usrID == NOTFOUND){
				result = NOTFOUND;
			}
			System.out.println("pw= " + pw);
			if(pw == null)
				return result;
			if(pw.equals(password)){
				result = PASS;
			}else{
				result = FAIL;
			}
			res.close();
		}catch(Exception e){
			System.out.println(e);
		}
		closeDB();
		return result;
	}
	
	public void OpenDB(){
		try {
			conn = getConnection(URL);
			st = conn.createStatement();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void closeDB(){
		try {
			st.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
