package neo4j.services;

import neo4j.repositories.PaperRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PaperService {

    @Autowired PaperRepository paperRepository;
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
        
        return FormatConverter.toAlcFormat1(result.iterator());
    }
    
    public Map<String, Object> getPaperByAuthorAndTimeline(String s, Integer begYear, Integer endYear) {
    	System.out.println("getPaperByAuthorAndTimeline:     " + s);
        Iterator<Map<String, Object>> result = paperRepository.getPaperByAuthorAndTimeline(s, begYear, endYear).iterator();
        return FormatConverter.toAlcFormat(result);
    }
    
    public Map<String, Object> getPaperByTimeline(Integer begYear, Integer endYear) {
    	System.out.println("getPaperByTimeline:     " + begYear + " to " + endYear);
        Iterator<Map<String, Object>> result = paperRepository.getPaperByTimeline(begYear, endYear).iterator();
        return FormatConverter.toAlcFormat(result);
    }
    
    public Map<String, Object> findByTitleContaining(String s) {
    	System.out.println("findByTitleContaining:    " + s);
        Iterator<Map<String, Object>> result = paperRepository.findByTitleContaining(s).iterator();
        return FormatConverter.toAlcFormat(result);
    }
}

