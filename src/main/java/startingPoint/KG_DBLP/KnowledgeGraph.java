package startingPoint.KG_DBLP;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import Controller.UserController;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import neo4j.domain.*;
import neo4j.repositories.*;
import neo4j.services.DatasetService;
import neo4j.services.PaperService;

@Configuration
@Import(App.class)
@RestController("/")
public class KnowledgeGraph extends WebMvcConfigurerAdapter {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(KnowledgeGraph.class, args);
    }
    
    @Autowired
    PaperService paperService;
    @Autowired
    DatasetService datasetService;

    @Autowired PaperRepository paperRepository;
    @Autowired DatasetRepository datasetRepository;

    @RequestMapping("/graph")
    public Map<String, Object> graph(@RequestParam(value = "limit",required = false) Integer limit) {
    	return paperService.graph(limit == null ? 100 : limit);
    }
    
    @RequestMapping("/graphTest")
    public String graphTest(@RequestParam(value = "limit",required = false) Integer limit) {
    	Map<String, Object> map = paperService.graphAlc(limit == null ? 100 : limit);
    	String json = "";
    	ObjectMapper mapper = new ObjectMapper();
    	try {
    		//convert map to JSON string
    		json = mapper.writeValueAsString(map);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return json;
    }
    
    @RequestMapping("/graphUserDataset")
    public String graphUserDataset(@RequestParam(value = "limit",required = false) Integer limit) {
    	Map<String, Object> map = datasetService.graphAlc(limit == null ? 100 : limit);
    	String json = "";
    	ObjectMapper mapper = new ObjectMapper();
    	try {
    		//convert map to JSON string
    		json = mapper.writeValueAsString(map);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return json;
    }
    
    @RequestMapping("/getPaperByAuthor")
    public String query1(@RequestParam(value = "name",required = false) String name) {
    	System.out.println("getPaperByAuthor");
    	name = name.replace('+', ' ');
    	System.out.println("Name: " + name);
    	Map<String, Object> map = paperService.getPaperByAuthor(name == null ? "" : name);
    	String json = "";
    	ObjectMapper mapper = new ObjectMapper();
    	try {
    		//convert map to JSON string
    		json = mapper.writeValueAsString(map);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return json;
    }
    
    @RequestMapping("/getCoAuthor")
    public String query2(@RequestParam(value = "name",required = false) String name) {
    	System.out.println("getCoAuthor");
    	name = name.replace('+', ' ');
    	System.out.println("Name: " + name);
    	Map<String, Object> map = paperService.getCoAuthor(name == null ? "" : name);
    	String json = "";
    	ObjectMapper mapper = new ObjectMapper();
    	try {
    		//convert map to JSON string
    		json = mapper.writeValueAsString(map);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return json;
    }
    
    @RequestMapping("/getMultiDepthCoAuthor")
    public String getMultiDepthCoAuthor(@RequestParam(value = "name",required = false) String name) {
    	System.out.println("getMultiDepthCoAuthor");
    	name = name.replace('+', ' ');
    	System.out.println("Name: " + name);
    	Map<String, Object> map = paperService.getMultiDepthCoAuthor(name == null ? "" : name);
    	String json = "";
    	ObjectMapper mapper = new ObjectMapper();
    	try {
    		//convert map to JSON string
    		json = mapper.writeValueAsString(map);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return json;
    }
    
    @RequestMapping("/getPaperByAuthorAndTimeline")
    public String getPaperByAuthorAndTimeline(@RequestParam(value = "name",required = false) String name,
    		@RequestParam(value = "begYear",required = false) Integer begYear
    		,@RequestParam(value = "endYear",required = false) Integer endYear) {
    	System.out.println("getMultiDepthCoAuthor");
    	name = name.replace('+', ' ');
    	System.out.println("Name: " + name);
    	System.out.println("begYear: " + begYear);
    	System.out.println("endYear: " + endYear);
    	Map<String, Object> map = paperService.getPaperByAuthorAndTimeline(name, begYear, endYear);
    	String json = "";
    	ObjectMapper mapper = new ObjectMapper();
    	try {
    		//convert map to JSON string
    		json = mapper.writeValueAsString(map);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return json;
    }
    
    @RequestMapping("/getPaperByTimeline")
    public String getPaperByTimeline(
    		@RequestParam(value = "begYear",required = false) Integer begYear
    		,@RequestParam(value = "endYear",required = false) Integer endYear) {
    	System.out.println("getPaperByTimeline");
    	System.out.println("begYear: " + begYear);
    	System.out.println("endYear: " + endYear);
    	Map<String, Object> map = paperService.getPaperByTimeline(begYear, endYear);
    	String json = "";
    	ObjectMapper mapper = new ObjectMapper();
    	try {
    		//convert map to JSON string
    		json = mapper.writeValueAsString(map);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return json;
    }
    
    @RequestMapping("/findByTitleContaining")
    public String findByTitleContaining(@RequestParam(value = "title",required = false) String title) {
    	System.out.println("findByTitleContaining");
    	title = title.replace('+', ' ');
    	System.out.println("Title:     " + title);
    	Map<String, Object> map = paperService.findByTitleContaining(title == null ? "" : title);
    	String json = "";
    	ObjectMapper mapper = new ObjectMapper();
    	try {
    		//convert map to JSON string
    		json = mapper.writeValueAsString(map);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return json;
    }
    
    @RequestMapping("/signUp")
    public String signUp(@RequestParam(value = "userName",required = false) String userName
    					,@RequestParam(value = "password",required = false) String password) {
    	System.out.println("signUp");
    	UserController userController = new UserController();
    	User user = new User(userName, password);
    	String json = "";
    	if(userController.isUserValid(user)){
    		userController.signUp(userName, password);
    		json = "1";
    	}else{
    		json = "0";
    	}
    	
    	return json;
    }
    @RequestMapping("/logIn")
    public String logIn(@RequestParam(value = "userName",required = false) String userName
    					,@RequestParam(value = "password",required = false) String password) {
    	System.out.println("logIn");
    	UserController userController = new UserController();
    	boolean res = userController.logIn(userName, password);
    	String json = res ? "1" : "0";
    	
    	return json;
    }
    

}