package Controller;

import java.util.List;

import neo4j.domain.User;

public class UserController {
	private List<User> userList;
	public UserController(){
		
	}
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	public void signUp(String userName, String password){
		User user = new User(userName, password);
		userList.add(user);
		return;
	}
	public boolean logIn(String userName, String password){
		boolean res = false;
		for(User temp : userList){
			if(temp.getName().equals(userName) && temp.getPassword().equals(password)){
				res = true;
				break;
			}
		}
		return res;
	}
	public boolean isUserValid(User user){
		boolean res = true;
		for(User cur : userList){
			if(cur.getName().equals(user.getName())){
				res = false;
				break;
			}
		}
		return res;
	}
}
