package mil.ln.ncos.cmmn.util;


import java.util.Map;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil{
	
	
	public static String toGsonStr(Object obj) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.toJson(obj);
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String,Object> fromJsonStrToMap(String jsonStr) {
		Gson gson = new Gson();
		Map map = gson.fromJson(jsonStr, Map.class);
		return map;
		
	}

}