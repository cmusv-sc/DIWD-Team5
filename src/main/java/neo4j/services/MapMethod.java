package neo4j.services;

import java.util.HashMap;
import java.util.Map;

public class MapMethod {
	public static Map<String, Object> map1(String key1, Object value1){
		Map<String, Object> result = new HashMap<String,Object>(1);
        result.put(key1,value1);
        return result;
	}
	public static Map<String, Object> map2(String key1, Object value1, String key2, Object value2) {
        Map<String, Object> result = new HashMap<String,Object>(2);
        result.put(key1,value1);
        result.put(key2,value2);
        return result;
    }
    
	public static Map<String, Object> map3(String key1, Object value1, String key2, Object value2, 
    		String key3, Object value3) {
        Map<String, Object> result = new HashMap<String,Object>(3);
        result.put(key1,value1);
        result.put(key2,value2);
        result.put(key3, value3);
        return result;
    }
	
	public static Map<String, Object> map4(String key1, Object value1, String key2, Object value2, 
    		String key3, Object value3, String key4, Object value4) {
        Map<String, Object> result = new HashMap<String,Object>(4);
        result.put(key1,value1);
        result.put(key2,value2);
        result.put(key3, value3);
        result.put(key4, value4);
        return result;
    }
    
	public static Map<String, Object> map5(String key1, Object value1, String key2, Object value2, 
    		String key3, Object value3, String key4, Object value4, String key5, Object value5) {
        Map<String, Object> result = new HashMap<String,Object>(5);
        result.put(key1,value1);
        result.put(key2,value2);
        result.put(key3, value3);
        result.put(key4, value4);
        result.put(key5, value5);
        return result;
    }
    
	public static Map<String, Object> map6(String key1, Object value1, String key2, Object value2, 
    		String key3, Object value3, String key4, Object value4, String key5, Object value5,
    		String key6, Object value6) {
        Map<String, Object> result = new HashMap<String,Object>(6);
        result.put(key1,value1);
        result.put(key2,value2);
        result.put(key3, value3);
        result.put(key4, value4);
        result.put(key5, value5);
        result.put(key6, value6);
        return result;
    }

}
