package neo4j.domain;


import org.neo4j.ogm.annotation.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

@JsonIdentityInfo(generator=JSOGGenerator.class)
@NodeEntity
public class User {
    @GraphId 
    Long id;

    private String name;
    private String password;

    @Relationship(type = "USE")
    List<Dataset> datasets;

    public User() { }
    public User(String name, String password){
    	this.name = name;
    	this.password = password;
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
	public void setName(String name) {
		this.name = name;
	}
	public void setDatasets(List<Dataset> datasets) {
		this.datasets = datasets;
	}
	public String getName() {
        return name;
    }

    public List<Dataset> getDatasets() {
        return datasets;
    }
}

