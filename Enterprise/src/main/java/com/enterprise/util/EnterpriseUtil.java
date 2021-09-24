package com.enterprise.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import com.enterprise.exception.EnterpriseException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * {@link EnterpriseUtil} is util class.Describe many util method
 * @author rajkumar
 *
 */
public class EnterpriseUtil {

	/**
	 * return primary Key of any entity object
	 * @param object
	 * @return
	 */
	public static Integer getPrimaryKey(Object object) {
		try {
			return (Integer) object.getClass().getMethod("getPrimarykey", null).invoke(object, null);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * method used to copy source object into target object of same type
	 * @param source
	 * @param target
	 * @return
	 */
	public static Object copyObject(Object source , Object target) {
		Field[] fields1 = source.getClass().getDeclaredFields();
		Field[] fields2 = target.getClass().getDeclaredFields();
		for(int i=0; i<fields1.length; i++){
			try {
				fields2[i].setAccessible(true);
				fields1[i].setAccessible(true);
				fields2[i].set(target,fields1[i].get(source));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return target;
	}

	/**
	 * 
	 * @param method
	 * @return
	 */
	@SuppressWarnings("unused")
	private static boolean isGetter(Method method){
		if((method.getName().startsWith("get") || method.getName().startsWith("is")) 
				&& method.getParameterCount() == 0 && !method.getReturnType().equals(void.class)){
			return true;
		}
		return false;    
	}

	/**
	 * 
	 * @param method
	 * @return
	 */
	@SuppressWarnings("unused")
	private static boolean isSetter(Method method){
		if(method.getName().startsWith("set") && method.getParameterCount() == 1 
				&& method.getReturnType().equals(void.class)){
			return true;
		}
		return false;    
	}

	/**
	 * Method is used to convert json string into map object
	 * @param json
	 * @return map object
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> jsonStringToMap(String json) {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> map = null;
		try {
			if(json != null) {
				map = mapper.readValue(json, HashMap.class);
			}else {
				map = new HashMap<String, Object>();
			}
			return map;
		} catch (Exception exception) {
			throw new EnterpriseException("Exception occur while converting json string into map");
		}
	}

	/**
	 *  Method is used to convert map into json string
	 * @param map
	 * @return json string
	 */
	public static String mapToJsonString(HashMap<String, Object> map) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			if(map != null) {
				json = mapper.writeValueAsString(map);
			}else {
				json = "";
			}
			return json;
		} catch (Exception e) {
			throw new EnterpriseException("Exception occur while converting map into json string");
		}
	}

	/**
	 * Method is used to convert map into Object
	 * @param target
	 * @param map
	 * @return
	 */
	public static Object getObjectFromMap(Object target,HashMap<String, Object> map) {
		ObjectMapper mapper = new ObjectMapper();
		Object source = null;
		try {
			if(map != null) {
				source = mapper.convertValue(map, target.getClass());
			}else {
				source = target;
			}
			return source;
		}catch (Exception e) {
			throw new EnterpriseException("Exception occur while converting map into object");
		}
	}
	
	/**
	 *  Method is used to convert map into Rel Object
	 * @param target
	 * @param map
	 * @return
	 */
	public static Object getRelObjectFromMap(Object target,HashMap<String, Object> map) {
		try {
			Field[] fields = target.getClass().getDeclaredFields();
			for(Field field : fields) {
				field.setAccessible(true);
				field.set(target, map.get(field.getName()));
			}
			return target;
		}catch (Exception e) {
			throw new EnterpriseException("Exception occur while converting map into object");
		}
		
	}
}
