package com.xtn.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrintJson {
	public static String json = "{}";
	public static ObjectMapper om = new ObjectMapper();

	//将boolean值解析为json串
	public static String printJsonFlag(boolean flag){
		
		Map<String,Boolean> map = new HashMap<String,Boolean>();
		map.put("success",flag);
		try {
			//{"success":true}
			json = om.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return json;
		
	}

	//将对象解析为json串
	public static String printJsonObj(Object obj){

		/*
		 *
		 * Person p
		 * 	id name age
		 * {"id":"?","name":"?","age":?}
		 *
		 * List<Person> pList
		 * [{"id":"?","name":"?","age":?},{"id":"?","name":"?","age":?},{"id":"?","name":"?","age":?}...]
		 *
		 * Map
		 * 	key value
		 * {key:value}
		 *
		 *
		 */
		try {
			json = om.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;

	}

}























