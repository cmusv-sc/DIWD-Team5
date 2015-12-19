package startingPoint.KG_DBLP;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
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
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import neo4j.domain.*;
import neo4j.repositories.*;
import neo4j.services.DatasetService;
import neo4j.services.PaperService;
import userdb.DBConnection;
import userdb.EditUser;

import org.neo4j.shell.util.json.JSONException;
import org.neo4j.shell.util.json.JSONObject;

@Configuration
@Import(App.class)
@RestController("/")
public class KnowledgeGraph extends WebMvcConfigurerAdapter {

	public static void main(String[] args) throws IOException {
		DBConnection db = new DBConnection();
		try {
			db.createAutoTable();
			/*
			 * UserController controller = new UserController();
			 * controller.signUp("Keen", "Wang", "aaaa",
			 * "keen.wang@sv.cmu.edu"); controller.signUp("Baiyang", "Wang",
			 * "aaaa", "baiyangw@andrew.cmu.edu"); controller.signUp("Keen",
			 * "Wang", "bbbb", "keen.wang@sv.cmu.edu");
			 * controller.signUp("Baiyang2", "Wang", "bbbb",
			 * "baiyangw2@andrew.cmu.edu");
			 * controller.logIn("keen.wang@sv.cmu.edu", "aaaa");
			 * controller.logIn("keen.wang@sv.cmu.edu", "bbbb");
			 * controller.logIn("keen.wang@sv2.cmu.edu", "bbbb");
			 * /*controller.appendHistory("keen.wang@sv.cmu.edu", "111");
			 * controller.appendHistory("keen.wang@sv.cmu.edu", "222");
			 * controller.appendHistory("keen.wang@sv.cmu.edu", "333");
			 * controller.signUp("Keen2", "Wang", "bbbbbb",
			 * "keen2.wang@sv.cmu.edu"); List<User> unfollows =
			 * controller.getAllUnFollowed("keen.wang@sv.cmu.edu");
			 * System.out.println("---------------------------");
			 * controller.addFollow("baiyangw@andrew.cmu.edu",
			 * "keen.wang@sv.cmu.edu");
			 * controller.addFollow("baiyangw@andrew.cmu.edu",
			 * "keen2.wang@sv.cmu.edu"); unfollows =
			 * controller.getAllUnFollowed("keen.wang@sv.cmu.edu"); List<User>
			 * followers = controller.getFollowers("baiyangw@andrew.cmu.edu");
			 * JSONArray usrArr = new JSONArray();
			 * usrArr.put(unfollows.toString());
			 * System.out.println(usrArr.toString().replace("\\", ""));
			 * /*usrArr.put(followers.toString());
			 * System.out.println(usrArr.toString().replace("\\", ""));
			 */

			/*
			 * JSONArray hisArr = new JSONArray(); List<History> history =
			 * controller.getHistory("keen.wang@sv.cmu.edu");
			 * hisArr.put(history.toString());
			 * System.out.println(hisArr.toString().replace("\\", ""));
			 */

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SpringApplication.run(KnowledgeGraph.class, args);
	}

	@Autowired
	PaperService paperService;
	@Autowired
	DatasetService datasetService;

	@Autowired
	PaperRepository paperRepository;
	@Autowired
	DatasetRepository datasetRepository;

	@RequestMapping("/graph")
	public Map<String, Object> graph(
			@RequestParam(value = "limit", required = false) Integer limit) {
		return paperService.graph(limit == null ? 100 : limit);
	}

	@RequestMapping("/graphTest")
	public String graphTest(
			@RequestParam(value = "limit", required = false) Integer limit) {
		Map<String, Object> map = paperService.graphAlc(limit == null ? 100
				: limit);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/graphUserDataset")
	public String graphUserDataset(
			@RequestParam(value = "limit", required = false) Integer limit) {
		Map<String, Object> map = datasetService.graphAlc(limit == null ? 100
				: limit);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/getPaperByAuthor")
	public String query1(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "email", required = false) String email) {
		System.out.println("getPaperByAuthor");
		name = name.replace('+', ' ');
		System.out.println("Name: " + name + ", email: " + email);
		UserController controller = new UserController();
		String history = "Get Paper By Author --> Author Name: " + name;
		controller.appendHistory(email, history);
		Map<String, Object> map = paperService
				.getPaperByAuthor(name == null ? "" : name);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/getCoAuthor")
	public String query2(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "email", required = false) String email) {
		System.out.println("getCoAuthor");
		name = name.replace('+', ' ');
		System.out.println("Name: " + name + ", email: " + email);
		UserController controller = new UserController();
		String history = "Get CoAuthor By author --> Author Name: " + name;
		controller.appendHistory(email, history);
		System.out.println("Name: " + name);
		Map<String, Object> map = paperService.getCoAuthor(name == null ? ""
				: name);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/getMultiDepthCoAuthor")
	public String getMultiDepthCoAuthor(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "email", required = false) String email) {
		System.out.println("getMultiDepthCoAuthor");
		name = name.replace('+', ' ');
		System.out.println("Name: " + name);
		UserController controller = new UserController();
		String history = "Get MuiltiDepthCoAuthor By author --> Author Name: "
				+ name;
		controller.appendHistory(email, history);
		System.out.println("Name: " + name);
		Map<String, Object> map = paperService
				.getMultiDepthCoAuthor(name == null ? "" : name);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
			System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/getPaperByAuthorAndTimeline")
	public String getPaperByAuthorAndTimeline(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "begYear", required = false) Integer begYear,
			@RequestParam(value = "endYear", required = false) Integer endYear,
			@RequestParam(value = "email", required = false) String email) {
		System.out.println("getMultiDepthCoAuthor");
		name = name.replace('+', ' ');
		System.out.println("Name: " + name);
		System.out.println("begYear: " + begYear);
		System.out.println("endYear: " + endYear);
		UserController controller = new UserController();
		String history = "Get Paper By author and timeline --> Author Name: "
				+ name + " Begin Year: " + begYear + " End Year: " + endYear;
		controller.appendHistory(email, history);
		System.out.println("Name: " + name);
		Map<String, Object> map = paperService.getPaperByAuthorAndTimeline(
				name, begYear, endYear);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/getPaperByTimeline")
	public String getPaperByTimeline(
			@RequestParam(value = "begYear", required = false) Integer begYear,
			@RequestParam(value = "endYear", required = false) Integer endYear,
			@RequestParam(value = "email", required = false) String email) {
		System.out.println("getPaperByTimeline");
		System.out.println("begYear: " + begYear);
		System.out.println("endYear: " + endYear);
		UserController controller = new UserController();
		String history = "Get Paper By timeline --> Author Name: "
				+ " Begin Year: " + begYear + " End Year: " + endYear;
		controller.appendHistory(email, history);
		Map<String, Object> map = paperService.getPaperByTimeline(begYear,
				endYear);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/findByTitleContaining")
	public String findByTitleContaining(
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "email", required = false) String email) {
		System.out.println("findByTitleContaining");
		title = title.replace('+', ' ');
		System.out.println("Title:     " + title);
		UserController controller = new UserController();
		String history = "Find By Title Containing --> Title: " + title;
		controller.appendHistory(email, history);
		Map<String, Object> map = paperService
				.findByTitleContaining(title == null ? "" : title);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/findPaperByTitleContaining")
	public String findPaperByTitleContaining(
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "email", required = false) String email) {
		System.out.println("findByTitleContaining");
		title = title.replace('+', ' ');
		System.out.println("Title:     " + title);
		UserController controller = new UserController();
		String history = "Find Paper By Title Containing --> Title: " + title;
		controller.appendHistory(email, history);
		Map<String, Object> map = paperService
				.findPaperByTitleContaining(title == null ? "" : title);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/signup")
	public String signUp(
			@RequestParam(value = "FirstName", required = false) String firstName,
			@RequestParam(value = "LastName", required = false) String lastName,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "password", required = false) String password)
			throws JSONException {
		UserController controller = new UserController();
		JSONObject js = new JSONObject();
		email = email.replace("%40", "@");
		if (controller.signUp(firstName, lastName, password, email)) {
			js.put("OK", "1");
		} else {
			js.put("OK", "0");
		}
		return js.toString();
	}

	@RequestMapping("/login")
	public String logIn(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "password", required = false) String password)
			throws JSONException {
		UserController controller = new UserController();
		JSONObject js = new JSONObject();
		email = email.replace("%40", "@");
		int res = controller.logIn(email, password);
		System.out.println("res = " + res);
		if (res == EditUser.PASS) {
			js.put("OK", 1);
		} else if (res == EditUser.FAIL || res == EditUser.NOTFOUND) {
			js.put("OK", 0);
		}
		System.out.println("JSPN " + js.toString());
		return js.toString();

	}

	@RequestMapping("/getFollower")
	public String getFollowers(
			@RequestParam(value = "email", required = false) String email)
			throws JSONException {
		UserController controller = new UserController();
		email = email.replace("%40", "@");
		List<User> followers = controller.getFollowers(email);
		/* Trans to JSON here */
		// String s =
		// "[{\"email\":\"464632571@qq.com\",\"firstName\":\"Peng\", \"lastName\":\"Tong\"},{\"email\":\"bailiangg@andrew.cmu.edu\",\"firstName\":\"bailiang\", \"lastName\":\"Gong\"}, {\"email\":\"abhasing@yahoo.com\",\"firstName\":\"vibha\", \"lastName\":\"Singha\"}]";
		String res = followers.toString();
		System.out.println(res);
		return res;

	}

	@RequestMapping("/getFollowing")
	public String getFollow(
			@RequestParam(value = "email", required = false) String email)
			throws JSONException {
		UserController controller = new UserController();
		List<User> follows = controller.getFollows(email);
		email = email.replace("%40", "@");
		/* Trans to JSON here */
		// String s =
		// "[{\"email\":\"464632571@qq.com\",\"firstName\":\"Peng\", \"lastName\":\"Tong\"},{\"email\":\"bailiangg@andrew.cmu.edu\",\"firstName\":\"bailiang\", \"lastName\":\"Gong\"}, {\"email\":\"abhasing@yahoo.com\",\"firstName\":\"vibha\", \"lastName\":\"Singha\"}]";

		String res = follows.toString();
		System.out.println(res);
		return res;

	}

	@RequestMapping("/getAllUsers")
	public String getAllRelation(
			@RequestParam(value = "email", required = false) String email)
			throws JSONException {
		UserController controller = new UserController();
		User user = controller.getAllRelation(email);
		email = email.replace("%40", "@");
		System.out.println("email == " + email);
		/* Trans to JSON here */
		String res = user.relationsToString();
		/*
		 * JSONArray arr = new JSONArray(); arr.put(follows.toString()); String
		 * res = arr.toString().replace("\\", "");
		 */
		System.out.println("res == " + res);
		return res;

	}

	@RequestMapping("/getHistory")
	public String getQueryHistory(
			@RequestParam(value = "email", required = false) String email)
			throws JSONException {
		// String s =
		// "[{\"id\":\"1\",\"query\":\"get all publications\"},{\"id\":\"2\",\"query\":\"get all co-author\"}, {\"id\":\"3\",\"query\":\"get multi-depth co-author\"}]";
		email = email.replace("%40", "@");
		UserController controller = new UserController();
		List<History> history = controller.getHistory(email);
		System.out.println(history.toString());
		return history.toString();
	}

	@RequestMapping("/follow")
	public void followUser(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "vehicle", required = false) String toFollowEmail)
			throws JSONException {
		email = email.replace("%40", "@");
		UserController controller = new UserController();
		controller.addFollow(toFollowEmail, email);
		System.out.println(email + " follows " + toFollowEmail + " now!");
	}

	// // new query

	@RequestMapping("/findTop5RelatedPaper")
	public String findTop5RelatedPaper(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "title", required = false) String title) {
		System.out.println("findTop5RelatedPaper");
		title = title.replace('+', ' ');
		System.out.println("KeyWord:     " + title);
		UserController controller = new UserController();
		String history = "Find Top 5 Related Paper --> title: " + title;
		controller.appendHistory(email, history);
		Map<String, Object> map = paperService
				.findTop5RelatedPaper(title == null ? "" : title);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/getPaperByAuthorSetAndTimeline")
	public String getPaperByAuthorSetAndTimeline(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "name1", required = false) String name1,
			@RequestParam(value = "name2", required = false) String name2,
			@RequestParam(value = "name3", required = false) String name3,
			@RequestParam(value = "begYear", required = false) Integer begYear,
			@RequestParam(value = "endYear", required = false) Integer endYear) {
		System.out.println("getPaperByAuthorSetAndTimeline");
		name1 = name1.replace('+', ' ');
		name2 = name2.replace('+', ' ');
		name3 = name3.replace('+', ' ');
		System.out.println("Name: " + name1 + "  " + name2 + "   " + name3);
		System.out.println("begYear: " + begYear);
		System.out.println("endYear: " + endYear);
		UserController controller = new UserController();
		String history = "Get Paper By Author Set And Timeline --> Names: "
				+ name1 + " " + name2 + " " + name3 + " " + "Begin Year: "
				+ begYear + " End Year: " + endYear;
		controller.appendHistory(email, history);
		Map<String, Object> map = paperService.getPaperByAuthorSetAndTimeline(
				name1, name2, name3, begYear, endYear);

		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/getAuthorVolumeByJournal")
	public String getAuthorVolumeByJournal(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "name", required = false) String name) {
		System.out.println("getAuthorVolumeByJournal");
		name = name.replace('+', ' ');
		System.out.println("Name: " + name);
		UserController controller = new UserController();
		String history = "Get Author Volume By Journal --> name: " + name;
		controller.appendHistory(email, history);
		Map<String, Object> map = paperService.getAuthorVolumeByJournal(name);

		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/findPaperYouMayBeInterestedIn")
	public String findPaperYouMayBeInterestedIn(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "name1", required = false) String name1,
			@RequestParam(value = "name2", required = false) String name2) {
		System.out.println("findPaperYouMayBeInterestedIn");
		name1 = name1.replace('+', ' ');
		System.out.println("Name: " + name1 + "   " + name2);
		UserController controller = new UserController();
		String history = "Find Paper You May Be Interested In --> name1: "
				+ name1;
		controller.appendHistory(email, history);
		Map<String, Object> map = paperService.findPaperYouMayBeInterestedIn(
				name1, name2);

		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/findCollaborators")
	public String findCollaborators(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "name1", required = false) String name1,
			@RequestParam(value = "name2", required = false) String name2) {
		System.out.println("findCollaborators");
		name1 = name1.replace('+', ' ');
		System.out.println("Name: " + name1 + "   " + name2);
		UserController controller = new UserController();
		String history = "Find Collaborators --> name1: " + name1;
		controller.appendHistory(email, history);
		Map<String, Object> map = paperService.findCollaborators(name1, name2);

		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/findExperts")
	public String findExperts(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "name1", required = false) String name1,
			@RequestParam(value = "name2", required = false) String name2) {
		System.out.println("findExperts");
		name1 = name1.replace('+', ' ');
		System.out.println("Name: " + name1 + "   " + name2);
		UserController controller = new UserController();
		String history = "Find Experts --> name1: " + name1;
		controller.appendHistory(email, history);
		Map<String, Object> map = paperService.findExperts(name1, name2);

		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/findTop5CitedPaper")
	public String findTop5CitedPaper(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "year", required = false) Integer year) {
		System.out.println("findTop5CitedPaper");
		title = title.replace('+', ' ');
		System.out.println("KeyWord:     " + title);
		UserController controller = new UserController();
		String history = "Find Top5 Cited Paper --> title: " + title + "year: "
				+ year;
		controller.appendHistory(email, history);
		Map<String, Object> map = paperService.findTop5CitedPaper(
				title == null ? "" : title, year);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/formTeamByKeyWord")
	public String formTeamByKeyWord(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "title", required = false) String title) {
		System.out.println("formTeamByKeyWord");
		title = title.replace('+', ' ');
		System.out.println("KeyWord:     " + title);
		UserController controller = new UserController();
		String history = "Form Team By Key Word --> title: " + title;
		controller.appendHistory(email, history);
		Map<String, Object> map = paperService
				.formTeamByKeyWord(title == null ? "" : title);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/getsmallWorldTheory")
	public String proveSmallWorld(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "author1", required = false) String author1,
			@RequestParam(value = "author2", required = false) String author2) {
		author1 = author1.replace('+', ' ');
		author2 = author2.replace('+', ' ');
		System.out.println(author1 + " " + author2);
		UserController controller = new UserController();
		String history = "Get Small World Theory --> author1: " + author1;
		controller.appendHistory(email, history);
		Map<String, Object> map = paperService.getAPath(author1, author2);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(json.toString());
		return json;
	}

	@RequestMapping("/getRecommendations")
	public String getRecomm() {
		UserController controller = new UserController();
		List<History> recHistory = controller.getcacheHistory();
		System.out.println(recHistory.toString());
		JSONObject obj = new JSONObject();
		int i = 0;
		for (History his : recHistory) {
			try {
				obj.put(++i + "", his.getContent());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return obj.toString();
	}

	// Version 2.0
	@RequestMapping("/getAPath")
	public String getAPath(
			@RequestParam(value = "author1", required = false) String author1,
			@RequestParam(value = "author2", required = false) String author2,
			@RequestParam(value = "email", required = false) String email) {
		author1 = author1.replace('+', ' ');
		author2 = author2.replace('+', ' ');
		Map<String, Object> map = paperService.getAPath(author1, author2);
		UserController controller = new UserController();
		String history = "getAPath: " + "author1: " + author1 + ", " + "author2: " + author2;
		controller.appendHistory(email, history);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping("/map_location_keyWords")
	public String showMapByLocAndKey(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "location", required = false) String location,
			@RequestParam(value = "keyWord1", required = false) String keyWord1,
			@RequestParam(value = "keyWord2", required = false) String keyWord2) {
		System.out.println("location: " + location + ";" + "keyword1: "
				+ keyWord1 + ";" + "keyword2; " + keyWord2);
		List<String> res = paperService.map_location_keyWords(location,
				keyWord1, keyWord2);
		UserController controller = new UserController();
		String history = "Map_location_keyWords " + location;
		controller.appendHistory(email, history);
		String s = "[{\"name\":\"paper1\",\"lat\":\"-33.890542\", \"lng\":\"151.274856\"}, {\"name\":\"paper2\",\"lat\":\"-33.923036\", \"lng\":\"151.259052\"}, {\"name\":\"paper3\",\"lat\":\"-34.028249\", \"lng\":\"151.157507\"}]";
		s = res.toString();
		System.out.println(s);
		return s;
	}

	@RequestMapping("/map_journal_yearRange")
	public String showMapByJournalAndYearRange(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "journal", required = false) String journal,
			@RequestParam(value = "startYear", required = false) Integer startYear,
			@RequestParam(value = "endYear", required = false) Integer endYear) {
		UserController controller = new UserController();
		String history = "Map_journal_yearRange: " + journal;
		controller.appendHistory(email, history);
		System.out.println("journal: " + journal + ";" + "startYear: "
				+ startYear + ";" + "endYear; " + endYear);
		List<String> res = paperService.map_journal_yearRange(journal,
				startYear, endYear);
		String s = "[{\"name\":\"paper1\",\"lat\":\"-33.890542\", \"lng\":\"151.274856\"}, {\"name\":\"paper2\",\"lat\":\"-33.923036\", \"lng\":\"151.259052\"}, {\"name\":\"paper3\",\"lat\":\"-34.028249\", \"lng\":\"151.157507\"}]";
		s = res.toString();
		System.out.println(s);
		return s;
	}

	@RequestMapping("/getPaperDetails")
	public String getPaperDetails(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "title", required = false) String title) {
		System.out.println("title: " + title);
		String res = paperService.getPaperDetails(title);

		System.out.println(res);
		return res;
	}
	
	@RequestMapping("/getCitationByPaper")
	public String getPaperByTimeline(
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "name", required = false) String name) {
		System.out.println("getCitationByPaper");
		UserController controller = new UserController();
		String history = "Get Citation By Paper --> Paper";
		controller.appendHistory(email, history);
		name = name.replace('+', ' ');
		Map<String, Object> map = paperService.getCitationByPaper(name);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert map to JSON string
			json = mapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

}