package Controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import neo4j.domain.History;
import neo4j.domain.User;
import userdb.DBConnection;
import userdb.EditUser;

public class UserController {
	private EditUser ed;
	private static Deque<History> cache = new LinkedList<History>();
	public UserController(){
		this.ed = new EditUser();
	}

	public boolean signUp(String firstName, String lastName, String password, String email){
		User user = new User(firstName, lastName, password, email);
		if(!isUserValid(user)) {
			System.out.println("User exists!");
			return false;
		}
		ed.insertUser(user);
		System.out.println("New User Inserted: " + user.getFirstName() + " " + user.getLastName()
				   + " " + user.getPassword() + " " + user.getEmail());
		return true;
	}
	public int logIn(String email, String password){
		User user = new User(null, null, password, email);
		int res = ed.checkPassword(user);
		System.out.println("Check User: email --> " + user.getEmail()
						+ " result --> " + res);
		return res;
	}
	
	private boolean isUserValid(User user){
		return ed.checkValid(user);
	}
	
	public void appendHistory(String email, String history) {
		User user = new User(null, null, null, email);
		ed.appendHistory(user, history);
		System.out.println("New history inserted: " + history + " on User: " + email);
		if (cache.size() > 5) {
			cache.pollFirst();
		}
		cache.offerLast(new History(cache.size(), history));
	}
	
	public void addFollow(String beFollowedEmail, String followerEmail) {
		User beFollowed = new User(null, null, null, beFollowedEmail);
		User follower = new User(null, null, null, followerEmail);
		if (ed.follow(follower, beFollowed))
			System.out.println("Add a follow: " + follower + " --> " + beFollowed);
		else
			System.out.println("Add fail!");
	}
	
	public List<History> getHistory(String email) {
		User user = new User(null, null, null, email);
		List<History> list = ed.getHistory(user);
		System.out.println("User's history got: " + list.toString() + " on User: " + email);
		return list;
	}
	
	public List<User> getFollowers(String email) {
		User user = new User(null, null, null, email);
		List<User> list = ed.getFollowers(user);
		System.out.println("User's Followers got: " + list.toString() + " on User: " + email);
		return list;
	}
	
	public List<User> getFollows(String email) {
		User user = new User(null, null, null, email);
		List<User> list = ed.getFollows(user);
		System.out.println("User's Follows got: " + list.toString() + " on User: " + email);
		return list;
	}

	public User getAllRelation(String email) {
		User user = new User(null, null, null, email);
		user = ed.getAllRelation(user);
		System.out.println("User's AllRelationship got: " + user.getRelation().toString() + " on User: " + email);
		return user;
	}
	
	public List<History> getcacheHistory() {
		List<History> list = new ArrayList<History>();
		Deque<History> tmp = new LinkedList<History>();
		while (!cache.isEmpty()) {
			History cur = cache.pollFirst();
			list.add(cur);
			tmp.offerLast(cur);
		}
		while (!tmp.isEmpty()) {
			cache.offerLast(tmp.pollFirst());
		}
		System.out.println(list.size());
		return list;
	}

}
