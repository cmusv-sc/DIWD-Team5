package userdb;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Controller.UserController;
import neo4j.domain.History;
import neo4j.domain.User;

public class Test {

	public static void main(String[] args) throws JSONException {
		DBConnection db = new DBConnection();
		try {
			db.createAutoTable();
			UserController controller = new UserController();
			controller.signUp("Keen", "Wang", "aaaa", "keen.wang@sv.cmu.edu");
			controller.appendHistory("keen.wang@sv.cmu.edu", "111");
			controller.appendHistory("keen.wang@sv.cmu.edu", "222");
			controller.appendHistory("keen.wang@sv.cmu.edu", "333");
			controller.signUp("Keen2", "Wang", "bbbbbb", "keen2.wang@sv.cmu.edu");
			//User usr = controller.getAllRelation("keen.wang@sv.cmu.edu");
			System.out.println("---------------------------");
			System.out.println(controller.getcacheHistory());
			/*controller.addFollow("baiyangw@andrew.cmu.edu", "keen.wang@sv.cmu.edu");
			controller.addFollow("baiyangw@andrew.cmu.edu", "keen.wang@sv.cmu.edu");
			controller.addFollow("baiyangw@andrew.cmu.edu", "keen.wang@sv.cmu.edu");
			controller.addFollow("baiyangw@andrew.cmu.edu", "keen2.wang@sv.cmu.edu");*/
			/*User usr = controller.getAllRelation("keen.wang@sv.cmu.edu");
			/System.out.println(usr.relationsToString().replace("\\", ""));
			List<User> followers = controller.getFollowers("baiyangw@andrew.cmu.edu");
			System.out.println(followers.toString());
			JSONArray usrArr = new JSONArray();	
			usrArr.put(unfollows.toString());
			System.out.println(usrArr.toString().replace("\\", ""));
			usrArr.put(followers.toString());
			System.out.println(usrArr.toString().replace("\\", ""));
			
			
			List<History> history = controller.getHistory("keen.wang@sv.cmu.edu");
			System.out.println(history.toString());
			*/
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
