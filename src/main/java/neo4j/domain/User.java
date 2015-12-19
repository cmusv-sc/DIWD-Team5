package neo4j.domain;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

@JsonIdentityInfo(generator=JSOGGenerator.class)
@NodeEntity
public class User {
    @GraphId 
    Long id;

    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private List<History> history;
    private Map<User, Boolean> relation;

    @Relationship(type = "USE")
    List<Dataset> datasets;

    public User(String firstName, String lastName, String password, String email){
    	this.setFirstName(firstName);
    	this.setLastName(lastName);
    	this.password = password;
    	this.setEmail(email);
    	this.setHistory(new ArrayList<History>());
    	this.relation = new HashMap<User, Boolean>();
    }
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setDatasets(List<Dataset> datasets) {
		this.datasets = datasets;
	}

    public List<Dataset> getDatasets() {
        return datasets;
    }
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<History> getHistory() {
		return history;
	}
	public void setHistory(List<History> history) {
		this.history = history;
	}
	@Override
	public String toString() {
		JSONObject obj = new JSONObject();
	  	 try {
	  		obj.put("firstName", this.getFirstName());
	  		obj.put("lastName", this.getLastName());
	  		obj.put("email", this.getEmail());
	  		 System.out.println(obj.toString());
	  	 } catch (JSONException e) {
	  		 // TODO Auto-generated catch block
	  		 e.printStackTrace();
	  	 }
	  	 return obj.toString();
	}
	public Map<User, Boolean> getRelation() {
		return relation;
	}
	public void addRelation(User usr, boolean relation) {
		this.relation.put(usr, relation);
	}
	public String relationsToString() {
		String s = "[{\"email\":\"464632571@qq.com\",\"f\":\"follow\"},{\"email\":\"bailiangg@andrew.cmu.edu\",\"f\":\"unfollow\"}, {\"email\":\"abhasing@yahoo.com\",\"f\":\"unfollow\"}]";
		StringBuilder sb = new StringBuilder();
		List<String> list = new ArrayList<String>();
		for (Map.Entry<User, Boolean> entry : relation.entrySet()) {
			sb.append("{");
			sb.append("\"email\":");
			sb.append("\"" + entry.getKey().getEmail() + "\"");
			sb.append(",\"f\":");
			if (entry.getValue()) {
				sb.append("\"follow\"");
			} else {
				sb.append("\"unfollow\"");
			}
			sb.append("}");
			list.add(sb.toString());
			sb.setLength(0);
		}
		/*JSONArray arr = new JSONArray();
		System.out.println("list == " + list.toString());
	  	arr.put(list);
		System.out.println(arr.toString());
	  	return arr.toString();*/
		return list.toString();
	}
}

