package neo4j.services;

import neo4j.repositories.PaperRepository;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PaperService {
	HashMap<String, ArrayList<Float>> map = new HashMap<String, ArrayList<Float>>();
	
    @Autowired PaperRepository paperRepository;
    public void initialMap(){
    	ArrayList<Float> temp = new ArrayList<Float>();
    	temp.add((float) 40.712784);
    	temp.add((float) -74.005941);
    	map.put("New York City", new ArrayList<Float>(temp));
    	temp.set(0, (float) 34.052234);
    	temp.set(1, (float) -118.243685);
    	map.put("Los Angeles", new ArrayList<Float>(temp));
    	temp.set(0, (float) 41.878114);
    	temp.set(1, (float) -87.629798);
    	map.put("Chicago", new ArrayList<Float>(temp));
    	temp.set(0, (float) 29.760427);
    	temp.set(1, (float) -95.369803);
    	map.put("Houston", new ArrayList<Float>(temp));
    	temp.set(0, (float) 39.952584);
    	temp.set(1, (float) -75.165222);
    	map.put("Philadelphia", new ArrayList<Float>(temp));
    	temp.set(0, (float) 33.448377);
    	temp.set(1, (float) -112.074037);
    	map.put("Phoenix", new ArrayList<Float>(temp));
    	temp.set(0, (float) 37.774929);
    	temp.set(1, (float) -122.419416);
    	map.put("San Francisco", new ArrayList<Float>(temp));
    	temp.set(0, (float) 39.961176);
    	temp.set(1, (float) -82.998794);
    	map.put("Columbus", new ArrayList<Float>(temp));
    	
    }
    
    public  Map<String, Object> graph(int limit) {
        Iterator<Map<String, Object>> result = paperRepository.graph(limit).iterator();
        return FormatConverter.toD3Format(result);
    }
    
	public  Map<String, Object> graphAlc(int limit) {
        Iterator<Map<String, Object>> result = paperRepository.graph(limit).iterator();
        return FormatConverter.toAlcFormat(result);
    }
    
    public Map<String, Object> getPaperByAuthor(String s) {
    	System.out.println("getPaperByAuthor: " + s);
        Iterator<Map<String, Object>> result = paperRepository.getPaperByAuthor(s).iterator();
        return FormatConverter.toAlcFormat(result);
    }
    public Map<String, Object> getCoAuthor(String s) {
    	System.out.println("getCoAuthor:     " + s);
        Iterator<Map<String, Object>> result = paperRepository.getCoAuthor(s).iterator();
        return FormatConverter.toAlcFormat1(result);
    }
    public Map<String, Object> getMultiDepthCoAuthor(String s) {
    	System.out.println("getMultiDepthCoAuthor:     " + s);
        List<Map<String, Object>> result = paperRepository.getCoAuthor(s);
        Map<String, Object> row = result.get(0);
        for(Object name : (Collection)row.get("cast")){
        	System.out.println("Current author :      " + name);
        	result.addAll(paperRepository.getCoAuthor((String)name));
        }
        
        return FormatConverter.toAlcFormat2(result.iterator());
    }
    
    public Map<String, Object> getPaperByAuthorAndTimeline(String s, Integer begYear, Integer endYear) {
    	System.out.println("getPaperByAuthorAndTimeline:     " + s);
        Iterator<Map<String, Object>> result = paperRepository.getPaperByAuthorAndTimeline(s, begYear, endYear).iterator();
        return FormatConverter.toAlcFormat(result);
    }
    
    public Map<String, Object> getPaperByTimeline(Integer begYear, Integer endYear) {
    	System.out.println("getPaperByTimeline:     " + begYear + " to " + endYear);
        Iterator<Map<String, Object>> result = paperRepository.getPaperByTimeline(begYear, endYear).iterator();
        return FormatConverter.getPaperByTimeline(result);
    }
    
    public Map<String, Object> findByTitleContaining(String s) {
    	System.out.println("findByTitleContaining:    " + s);
        Iterator<Map<String, Object>> result = paperRepository.findByTitleContaining(s).iterator();
        return FormatConverter.toAlcFormat(result);
    }
    
    public Map<String, Object> findPaperByTitleContaining(String s) {
    	System.out.println("findPaperByTitleContaining:    " + s);
    	Iterator<Map<String, Object>> result = paperRepository.findPaperByTitleContaining(s).iterator();
        return FormatConverter.toCollectionFormat(result);
    }
    
    ////// new query
    
    public Map<String, Object> findTop5RelatedPaper(String s) {
    	System.out.println("findByTitleContaining:    " + s);
        Iterator<Map<String, Object>> result = paperRepository.findByTitleContaining(s).iterator();
        return FormatConverter.toAlcFormatTop5(result);
    }
    
    public Map<String, Object> getPaperByAuthorSetAndTimeline(String s1, String s2, String s3, Integer begYear, Integer endYear) {
    	System.out.println("getPaperByAuthorAndTimeline:     " + s1 + "  " + s2 + "  " + s3);
        List<Map<String, Object>> result1 = paperRepository.getPaperByAuthorAndTimeline(s1, begYear, endYear);
        List<Map<String, Object>> result2 = paperRepository.getPaperByAuthorAndTimeline(s2, begYear, endYear);
        List<Map<String, Object>> result3 = paperRepository.getPaperByAuthorAndTimeline(s3, begYear, endYear);
        
        result1.addAll(result2);
        result1.addAll(result3);
        return FormatConverter.toAlcFormat(result1.iterator());
    }
    
    public Map<String, Object> getAuthorVolumeByJournal(String s) {
    	System.out.println("getAuthorVolumeByJournal:    " + s);
        List<Map<String, Object>> result = paperRepository.getPaperByJournal(s);
        System.out.println(result);
        
        List<String> titleList = new ArrayList<String>();
        for(Map<String, Object> temp : result){
        	titleList.add((String) temp.get("paper"));
        }
        System.out.println("TitleList:    ");
        System.out.println(titleList);
        
        result = new ArrayList<Map<String, Object>>();
        for(String title : titleList){
        	result.addAll(paperRepository.getVolumeByTitle(title));
        }
        
        
        return FormatConverter.toCategorizedFormat(result.iterator());
    }
    
    public Map<String, Object> findPaperYouMayBeInterestedIn(String s1, String s2) {
    	System.out.println("findPaperYouMayBeInterestedIn:    " + s1);
        List<Map<String, Object>> result = paperRepository.findByTitleContaining(s1);
        List<Map<String, Object>> tempre =   paperRepository.findByTitleContaining(s2);
        result.addAll(tempre);
        System.out.println(result);
        
        List<String> titleList = new ArrayList<String>();
        for(Map<String, Object> temp : result){
        	titleList.add((String) temp.get("paper"));
        }
        System.out.println("TitleList:    ");
        System.out.println(titleList);
        
        result = new ArrayList<Map<String, Object>>();
        for(String title : titleList){
        	result.addAll(paperRepository.getCitationByPaper(title));
        }
        
        
        return FormatConverter.findPaperYouMayBeInterestedIn(result.iterator());
    }
    
    public Map<String, Object> findCollaborators(String s1, String s2) {
    	System.out.println("findCollaborators:    " + s1);
        List<Map<String, Object>> result = paperRepository.findByTitleContaining(s1);
        List<Map<String, Object>> tempre =   paperRepository.findByTitleContaining(s2);
        result.addAll(tempre);
        System.out.println(result);
        
        List<String> titleList = new ArrayList<String>();
        for(Map<String, Object> temp : result){
        	titleList.add((String) temp.get("paper"));
        }
        System.out.println("TitleList:    ");
        System.out.println(titleList);
        
        result = new ArrayList<Map<String, Object>>();
        for(String title : titleList){
        	result.addAll(paperRepository.getAuthorByTitle(title));
        }
        
        
        return FormatConverter.findCollaborators(result.iterator());
    }
    
    public Map<String, Object> findExperts(String s1, String s2) {
    	System.out.println("findExperts:    " + s1);
        List<Map<String, Object>> result = paperRepository.findByTitleContaining(s1);
        List<Map<String, Object>> tempre =   paperRepository.findByTitleContaining(s2);
        
        result.addAll(tempre);
        System.out.println(result);
        
        List<String> titleList = new ArrayList<String>();
        for(Map<String, Object> temp : result){
        	titleList.add((String) temp.get("paper"));
        }
        System.out.println("TitleList:    ");
        System.out.println(titleList);
        
        result = new ArrayList<Map<String, Object>>();
        for(String title : titleList){
        	result.addAll(paperRepository.getCitationByPaper(title));
        }
        
        System.out.println("Result:      " + result);
        
        tempre = new ArrayList<Map<String, Object>>();
        
        for(Map<String, Object> temp : result){
        	tempre.addAll(paperRepository.getAuthorByTitle((String)temp.get("paper")));
        }
        System.out.println("tempre:      " );
        System.out.println(tempre);
        
        
        return FormatConverter.findCollaborators(tempre.iterator());
    }
    public Map<String, Object> getCitationByPaper(String s){
    	s = s.replace('+', ' ');
    	System.out.println("getCitationByPaper:    " + s);
        Iterator<Map<String, Object>> result = paperRepository.getCitationByPaper(s).iterator();
        return FormatConverter.getCitationByPaper(result);
    }
    public Map<String, Object> findTop5CitedPaper(String s, Integer year) {
    	System.out.println("findTop5CitedPaper:    " + s);
        Iterator<Map<String, Object>> result = paperRepository.findTopCitedPaper(s, year).iterator();
        return FormatConverter.findTop5CitedPaper(result);
    }
    
    public Map<String, Object> formTeamByKeyWord(String s) {
    	System.out.println("formTeamByKeyWord:    " + s);
        Iterator<Map<String, Object>> result = paperRepository.findByTitleContaining1(s).iterator();
        return FormatConverter.formTeamByKeyWord(result);
    }
    
    public Map<String, Object> proveSmallWorld(String author1, String author2) {
    	Iterator<Map<String, String>> result = paperRepository.smallWorldTheory(author1, author2).iterator();

    	return FormatConverter.toD3FormatSmallWorld(result);
    }
    
    public Map<String, Object> getAPath(String author1, String author2) {
    	System.out.println("Get A Path : " + author1 + "  " + author2);
    	Iterator<Map<String, Object>> result = paperRepository.getAPath(author1, author2).iterator();

    	return FormatConverter.getAPath(result);
    }
    
    public List<String> map_journal_yearRange(String journal, Integer startYear, Integer endYear) {
    	journal = journal.replace('+', ' ');
    	System.out.println(journal); 
    	System.out.println(startYear);
    	System.out.println(endYear);
    	System.out.println(paperRepository.map_journal_yearRange(journal, startYear, endYear));
    	Iterator<Map<String, String>> result = paperRepository.map_journal_yearRange(journal, startYear, endYear).iterator();
    	initialMap();
    	List<String> res = new ArrayList<String>();
    	System.out.println("PaperService : map_journal_yearRange");
    	while (result.hasNext()) {
            Map<String, String> row = result.next();
            System.out.println(row);
            
            Map<String, Object> curMap = new HashMap<String, Object>();
            
            String paper = (String)row.get("paper");
            String address = (String)row.get("cast");
            if(address.length() < 2){
            	continue;
            }
            curMap.put("name", paper);
            Random rand = new Random();
            curMap.put("lat",((Float)(map.get(address).get(0) + rand.nextFloat())).toString());
            curMap.put("lng",((Float)(map.get(address).get(1) + rand.nextFloat())).toString());
            
            String json = "";
        	ObjectMapper mapper = new ObjectMapper();
        	try {
        		//convert map to JSON string
        		json = mapper.writeValueAsString(curMap);
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
            res.add(json);
    	}
    	return res;
    }
    public List<String> map_location_keyWords(String location, String keyWord1, String keyWord2) {
    	location = location.replace('+', ' ');
    	keyWord1 = keyWord1.replace('+', ' ');
    	keyWord2 = keyWord2.replace('+', ' ');
    	System.out.println(location); 
    	System.out.println(keyWord1);
    	System.out.println(keyWord2);
    	System.out.println(paperRepository.map_location_keyWords(location, keyWord1, keyWord2));
    	Iterator<Map<String, String>> result = paperRepository.map_location_keyWords(location, keyWord1, keyWord2).iterator();
    	initialMap();
    	List<String> res = new ArrayList<String>();
    	System.out.println("PaperService : map_location_keyWords");
    	while (result.hasNext()) {
            Map<String, String> row = result.next();
            System.out.println(row);
            
            Map<String, Object> curMap = new HashMap<String, Object>();
            
            String paper = (String)row.get("author");
            String address = (String)row.get("cast");
            if(address.length() < 2){
            	continue;
            }
            curMap.put("name", paper);
            Random rand = new Random();
            curMap.put("lat",((Float)(map.get(address).get(0) + rand.nextFloat())).toString());
            curMap.put("lng",((Float)(map.get(address).get(1) + rand.nextFloat())).toString());
            
            String json = "";
        	ObjectMapper mapper = new ObjectMapper();
        	try {
        		//convert map to JSON string
        		json = mapper.writeValueAsString(curMap);
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
            res.add(json);
    	}
    	return res;
    }
    
    public String getPaperDetails(String title) {
    	title = title.replace('+', ' ');
    	System.out.println(title); 
    	
    	System.out.println(paperRepository.getPaperDetails(title));
    	Iterator<Map<String, String>> result = paperRepository.getPaperDetails(title).iterator();
 
    	System.out.println("PaperService : getPaperDetails");
    	Map<String, String> curMap = new HashMap<String, String>();
    	while (result.hasNext()) {
            Map<String, String> row = result.next();
            
            curMap.putAll(row);
    	}
    	String json = "";
    	ObjectMapper mapper = new ObjectMapper();
    	try {
    		//convert map to JSON string
    		json = mapper.writeValueAsString(curMap);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        return json;
    }
}

