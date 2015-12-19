package neo4j.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import neo4j.repositories.PaperRepository;

public class FormatConverter {
	public static Map<String, Object> toD3Format(Iterator<Map<String, Object>> result) {
        List<Map<String,Object>> nodes = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> rels = new ArrayList<Map<String,Object>>();
        int i = 0;
        int target = 0;
        while (result.hasNext()) {
            Map<String, Object> row = result.next();
            nodes.add(MapMethod.map2("name",row.get("paper"),"label","paper"));
            i++;
            target = i;
            for (Object name : (Collection) row.get("cast")) {
                Map<String, Object> author = MapMethod.map2("name", name,"label","author");
                int source = nodes.indexOf(author);
                if (source == -1) {
                    nodes.add(author);
                    source = i++;
                }
                rels.add(MapMethod.map2("source",source,"target",target));
            }
        }
        return MapMethod.map2("nodes", nodes, "links", rels);
    }
    
	public static Map<String, Object> toAlcFormat(Iterator<Map<String, Object>> result) {
        List<Map<String,Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> rels = new ArrayList<Map<String, Object>>();
        int i = 1;
        int target = 0;
        while (result.hasNext()) {
            Map<String, Object> row = result.next();
            nodes.add(MapMethod.map6("id", i, "title",row.get("paper"), "label", "paper", "cluster", "1", "value", 2, "group", "paper"));
            System.out.println("------------------------");
            System.out.println(row);
            System.out.println("------------------------");
            target = i++;
            for (Object name : (Collection) row.get("cast")) {
                Map<String, Object> author = MapMethod.map5("title", 
                		name,"label", "author", "cluster", "2", "value", 1, "group", "author");
                int source = 0;
                for (int j = 0; j < nodes.size(); j++) {
                	if (nodes.get(j).get("title").equals(name)) {
                		source = (int) nodes.get(j).get("id");
                		break;
                	} 
                }
                if (source == 0) {
                	author.put("id", i);
                    source = i;
                    i++;
                    nodes.add(author);
                }

                rels.add(MapMethod.map3("from", source, "to", target, "title", "PUBLISH"));
            }
        }
        return MapMethod.map2("nodes", nodes, "edges", rels);
    }
	public static Map<String, Object> getCitationByPaper(Iterator<Map<String, Object>> result) {
        List<Map<String,Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> rels = new ArrayList<Map<String, Object>>();
        int i = 1;
        int target = 0;
        while (result.hasNext()) {
            Map<String, Object> row = result.next();
            nodes.add(MapMethod.map6("id", i, "title",row.get("paper"), "label", "paper", "cluster", "1", "value", 2, "group", "paper"));
            System.out.println("------------------------");
            System.out.println(row);
            System.out.println("------------------------");
            target = i++;
            for (Object name : (Collection) row.get("cast")) {
                Map<String, Object> author = MapMethod.map5("title", 
                		name,"label", "paper", "cluster", "2", "value", 1, "group", "paper");
                int source = 0;
                for (int j = 0; j < nodes.size(); j++) {
                	if (nodes.get(j).get("title").equals(name)) {
                		source = (int) nodes.get(j).get("id");
                		break;
                	} 
                }
                if (source == 0) {
                	author.put("id", i);
                    source = i;
                    i++;
                    nodes.add(author);
                }

                rels.add(MapMethod.map3("from", source, "to", target, "title", "CITE"));
            }
        }
        return MapMethod.map2("nodes", nodes, "edges", rels);
    }
    
	public static Map<String, Object> toAlcFormat1(Iterator<Map<String, Object>> result) {
        List<Map<String,Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> rels = new ArrayList<Map<String, Object>>();
        int i = 1;
        int target = 0;
//        遍历当前条目
        while (result.hasNext()) {
            Map<String, Object> row = result.next();
            System.out.println("------------------------");
            System.out.println(row);
            System.out.println("------------------------");
            nodes.add(MapMethod.map6("id", i, "title",row.get("author"),"label", "author", "cluster", "1", "value", 2, "group", "author"));
            target = i++;
//        遍历当前条目 下cast list 里面的
            for (Object name : (Collection) row.get("cast")) {
                Map<String, Object> author = MapMethod.map5("title", 
                		name,"label", "author", "cluster", "2", "value", 1, "group", "coauthor");
                
                author.put("id", i);
                int source = i;
                i++;
                nodes.add(author);
                

                rels.add(MapMethod.map3("from", source, "to", target, "name", "CO"));
            }
        }
        return MapMethod.map2("nodes", nodes, "edges", rels);
    }
    
	
	public static Map<String, Object> toCollectionFormat(Iterator<Map<String, Object>> result) {
        List<Map<String,Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> rels = new ArrayList<Map<String, Object>>();
        int i = 1;
        while (result.hasNext()) {
        	Map<String, Object> row = result.next();
            nodes.add(MapMethod.map6("id", i, "title", row.get("paper"), "label", "paper", "cluster", "1", "value", 2, "group", "paper"));
            System.out.println("------------------------");
            System.out.println(row);
            System.out.println("------------------------");
            i++;
            
        }
        System.out.println("************");
        System.out.println(nodes);
        return MapMethod.map2("nodes", nodes, "edges", rels);
    }
	
	
	public static Map<String, Object> toAlcFormatTop5(Iterator<Map<String, Object>> result) {
        List<Map<String,Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> rels = new ArrayList<Map<String, Object>>();
        int i = 1;
        int target = 0;
        while (result.hasNext()) {
        	if(target > 5){
        		break;
        	}
            Map<String, Object> row = result.next();
            nodes.add(MapMethod.map6("id", i, "title",row.get("paper"), "label", "paper", "cluster", "1", "value", 2, "group", "paper" ));//+ target));
            System.out.println("------------------------");
            System.out.println(row);
            System.out.println("------------------------");
            target = i++;
            for (Object name : (Collection) row.get("cast")) {
                Map<String, Object> author = MapMethod.map5("title", 
                		name,"label", "author", "cluster", "2", "value", 1, "group", "author");
                int source = 0;
                for (int j = 0; j < nodes.size(); j++) {
                	if (nodes.get(j).get("title").equals(name)) {
                		source = (int) nodes.get(j).get("id");
                		break;
                	} 
                }
                if (source == 0) {
                	author.put("id", i);
                    source = i;
                    i++;
                    nodes.add(author);
                }

                rels.add(MapMethod.map3("from", source, "to", target, "title", "PUBLISH"));
            }
        }
        return MapMethod.map2("nodes", nodes, "edges", rels);
    }
	
	public static Map<String, Object> toCategorizedFormat(Iterator<Map<String, Object>> result) {
        List<Map<String,Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> rels = new ArrayList<Map<String, Object>>();
        int i = 1;
        int target = 0;
        while (result.hasNext()) {
        	if(target > 10){
        		break;
        	}
            Map<String, Object> row = result.next();
            nodes.add(MapMethod.map6("id", i, "title",row.get("name"), "label", "author", "cluster", "1", "value", 2, "group", "paper"));
            System.out.println("------------------------");
            System.out.println(row);
            System.out.println("------------------------");
            target = i++;
            for (Object name : (Collection) row.get("cast")) {
                Map<String, Object> author = MapMethod.map5("title", 
                		name,"label", "volume", "cluster", "2", "value", 1, "group", "author");
                int source = 0;
                for (int j = 0; j < nodes.size(); j++) {
                	if (nodes.get(j).get("title").equals(name)) {
                		source = (int) nodes.get(j).get("id");
                		break;
                	} 
                }
                if (source == 0) {
                	author.put("id", i);
                    source = i;
                    i++;
                    nodes.add(author);
                }

                rels.add(MapMethod.map3("from", source, "to", target, "title", ""));
            }
        }
        return MapMethod.map2("nodes", nodes, "edges", rels);
    }
	
	public static Map<String, Object> findPaperYouMayBeInterestedIn(Iterator<Map<String, Object>> result) {
		List<Map<String,Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> rels = new ArrayList<Map<String, Object>>();
        int i = 1;
        while (result.hasNext()) {
        	if(i > 6){
        		break;
        	}
        	Map<String, Object> row = result.next();
            nodes.add(MapMethod.map6("id", i, "title", row.get("paper"), "label", "paper", "cluster", "1", "value", 2, 
            		"group", "paper" + i));//+ ((Collection)row.get("cast")).size()));
            System.out.println("------------------------");
            System.out.println(row);
            System.out.println("------------------------");
            i++;
            
        }
        
        return MapMethod.map2("nodes", nodes, "edges", rels);

    }
	
	public static Map<String, Object> findCollaborators(Iterator<Map<String, Object>> result) {
        List<Map<String,Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> rels = new ArrayList<Map<String, Object>>();
        int i = 1;
        while (result.hasNext()) {
        	Map<String, Object> row = result.next();
            nodes.add(MapMethod.map6("id", i, "title", row.get("author"), "label", "author", "cluster", "1", "value", 2, "group", "author"));
            System.out.println("------------------------");
            System.out.println(row);
            System.out.println("------------------------");
            i++;
            
        }
        System.out.println(nodes);
        return MapMethod.map2("nodes", nodes, "edges", rels);
    }
	
	public static Map<String, Object> getPaperByTimeline(Iterator<Map<String, Object>> result) {
        List<Map<String,Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> rels = new ArrayList<Map<String, Object>>();
        int i = 1;
        int target = 0;
        int temp = 0;
        while (result.hasNext()) {
            Map<String, Object> row = result.next();
            nodes.add(MapMethod.map6("id", i, "title",row.get("paper"), "label", "paper", "cluster", "1", "value", 2, "group", "paper" + temp/10));
            System.out.println("------------------------");
            System.out.println(row);
            System.out.println("------------------------");
            target = i++;
            temp++;
            for (Object name : (Collection) row.get("cast")) {
                Map<String, Object> author = MapMethod.map5("title", 
                		name,"label", "author", "cluster", "2", "value", 1, "group", "author");
                int source = 0;
                for (int j = 0; j < nodes.size(); j++) {
                	if (nodes.get(j).get("title").equals(name)) {
                		source = (int) nodes.get(j).get("id");
                		break;
                	} 
                }
                if (source == 0) {
                	author.put("id", i);
                    source = i;
                    i++;
                    nodes.add(author);
                }

                rels.add(MapMethod.map3("from", source, "to", target, "title", "PUBLISH"));
            }
        }
        return MapMethod.map2("nodes", nodes, "edges", rels);
    }
	
	
	public static Map<String, Object> findTop5CitedPaper(Iterator<Map<String, Object>> result) {
        List<Map<String,Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> rels = new ArrayList<Map<String, Object>>();
        int i = 1;
        int target = 0;
        while (result.hasNext()) {
        	if(target > 7){
        		break;
        	}
            Map<String, Object> row = result.next();
            nodes.add(MapMethod.map6("id", i, "title",row.get("paper"), "label", "paper", "cluster", "1", "value", 2, "group", "paper" ));//+ ((Collection)row.get("cast")).size()    ));
            System.out.println("------------------------");
            System.out.println(row);
            System.out.println("------------------------");
            target = i++;
            
        }
        return MapMethod.map2("nodes", nodes, "edges", rels);
    }
	
	
	public static Map<String, Object> formTeamByKeyWord(Iterator<Map<String, Object>> result) {
		List<Map<String,Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> rels = new ArrayList<Map<String, Object>>();
        int i = 1;
        int target = 0;
        while (result.hasNext()) {
        	if(target > 7){
        		break;
        	}
            Map<String, Object> row = result.next();
            nodes.add(MapMethod.map6("id", i, "title",row.get("paper"), "label", "author", "cluster", "1", "value", 2, "group", "paper"));
            System.out.println("------------------------");
            System.out.println(row);
            System.out.println("------------------------");
            target = i++;
            
        }
        return MapMethod.map2("nodes", nodes, "edges", rels);
    }
	
	
	public static Map<String, Object> toD3FormatSmallWorld(Iterator<Map<String, String>> result) {
        List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> rels = new ArrayList<Map<String,Object>>();
        int i = 0;
        while (result.hasNext()) {
        	Map<String, String> row = result.next();
        	nodes.add(MapMethod.map6("id", i, "title",row.get("cast"),"label", "author", "cluster", "1", "value", 2, "group", "author"));
        	if (i != 0) {
        		rels.add(MapMethod.map2("source", i - 1,"target", i));
        	}
        	i++;
        }

        return MapMethod.map2("nodes", nodes, "links", rels);
    }
	
	public static Map<String, Object> getAPath(Iterator<Map<String, Object>> result) {
        List<Map<String,Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> rels = new ArrayList<Map<String, Object>>();
        int i = 1;
        int target = 1;
        Map<String, Object> temp = result.next();
        System.out.println(temp);
        List<String> path =(List<String>)(temp.get("paper"));
        System.out.println(path);
        List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
        

        for(int j = 0; j < path.size(); j++){
        	map.add(new HashMap<String, Object>());
        	
        	if(path.get(j).length() > 20){
        		map.get(j).put("paper", path.get(j));
        	}else{
        		map.get(j).put("author", path.get(j));
        	}
        }
        System.out.println(map);
        result = map.iterator(); 
        while (result.hasNext()) {
            Map<String, Object> row = result.next();
            String title = new String();
            String group = new String();
            if(row.containsKey("paper")){
            	title = (String) row.get("paper");
            	group = "paper";
            }else{
            	title = (String) row.get("author");
            	group = "author";
            }
            
            nodes.add(MapMethod.map6("id", i, "title",title, "label", group, "cluster", "1", "value", 2, "group", group));
            System.out.println("------------------------");
            System.out.println(row);
            System.out.println("------------------------");
            i++;
        }
        while(target < i){
        	rels.add(MapMethod.map3("from", target, "to", ++target, "title", ""));
        }
        
        return MapMethod.map2("nodes", nodes, "edges", rels);
    }
	
	public static Map<String, Object> toAlcFormat2(Iterator<Map<String, Object>> result) {
        List<Map<String,Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> rels = new ArrayList<Map<String, Object>>();
        
        //Record id of every node
        Map<String, Integer> idMap = new HashMap<String, Integer>();
        int autoId = 1;
        
//        遍历当前条目
        while (result.hasNext()) {
            Map<String, Object> row = result.next();
            System.out.println("------------------------");
            System.out.println(row);
            System.out.println("------------------------");
            Integer sourceId = idMap.get((String) row.get("author"));
            if(sourceId == null) {
            	idMap.put((String) row.get("author"), autoId++);
            	sourceId = idMap.get((String) row.get("author"));
            	nodes.add(MapMethod.map6("id", sourceId, "title",row.get("author"),"label", "author", "cluster", "1", "value", 2, "group", "author"));
            }
//        遍历当前条目 下cast list 里面的
            for (Object name : (Collection) row.get("cast")) {
                
                Integer targetId = idMap.get((String) name);
                if(targetId == null) {
                	idMap.put((String) name, autoId++);
                	targetId = idMap.get((String) name);
                	
                	Map<String, Object> coAuthor = MapMethod.map5("title", 
                    		name,"label", "coauthor", "cluster", "2", "value", 1, "group", "coauthor");
                	
                	coAuthor.put("id", targetId);
                    
                    nodes.add(coAuthor);
                }
                

                rels.add(MapMethod.map3("from", sourceId, "to", targetId, "name", "CO"));
            }
        }
        return MapMethod.map2("nodes", nodes, "edges", rels);
    }
	
}
