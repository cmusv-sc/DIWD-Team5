package neo4j.services;

import java.util.ArrayList;
import java.util.Collection;
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
//               id ----> i
//               title -----> row.get("paper")
//               label ---> "paper"
//               cluster ----> 1
//               value ------> 2
//               group -----> paper
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
    
	public static Map<String, Object> toAlcFormat1(Iterator<Map<String, Object>> result) {
        List<Map<String,Object>> nodes = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> rels = new ArrayList<Map<String, Object>>();
        int i = 1;
        int target = 0;
        while (result.hasNext()) {
            Map<String, Object> row = result.next();
            nodes.add(MapMethod.map6("id", i, "name",row.get("coauthor"),"label", "coauthor", "cluster", "1", "value", 2, "group", "coauthor"));
            target = i++;
            for (Object name : (Collection) row.get("cast")) {
                Map<String, Object> author = MapMethod.map5("name", 
                		name,"label", "author", "cluster", "2", "value", 1, "group", "author");
                int source = 0;
                for (int j = 0; j < nodes.size(); j++) {
                	if (nodes.get(j).get("name").equals(name)) {
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

                rels.add(MapMethod.map3("from", source, "to", target, "name", "CO"));
            }
        }
        return MapMethod.map2("nodes", nodes, "edges", rels);
    }
    
	
}
