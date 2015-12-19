package neo4j.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class History {

	private int id;
	private String content;
	
	public History(int id, String content) {
		this.id = id;
		this.content = content;
	}
	
	@Override
	public String toString() {
		JSONObject obj = new JSONObject();
	  	 try {
	  		obj.put("id", String.valueOf(this.id));
	  		obj.put("query", content);
	  		System.out.println(obj.toString());
	  	 } catch (JSONException e) {
	  		 // TODO Auto-generated catch block
	  		 e.printStackTrace();
	  	 }
	  	 return obj.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
