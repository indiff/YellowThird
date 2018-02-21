package com.pear.common.utils.strings;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.pear.yellowthird.vo.databases.UserVo;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class JsonUtil {

	final static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
	}

	public JsonUtil() {
		// TODO Auto-generated constructor stub
	}

	public static ObjectMapper getCommonObjectMapper() {
		return mapper;
	}
	
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {   
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);   
	}   
	
	public static <T> T write2Class(String json,Class<T> valueType)
	{
		try
		{
			ObjectMapper objectMapper = new ObjectMapper();   
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); 
			T	t = objectMapper.readValue( json.toString(), valueType);
			return t;
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T write2Class(String json,JavaType valueType)
	{
		try
		{
			ObjectMapper objectMapper = new ObjectMapper();   
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); 
			T t = objectMapper.readValue( json.toString(), valueType);
			return t;
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static <T> Observable<T> write2ClassAsync(final String json,final Class<T> valueType)
	{
		return Observable.create(new Observable.OnSubscribe<T>() {
			@Override
			public void call(Subscriber<? super T> subscriber) {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				try {
					T t = objectMapper.readValue( json.toString(), valueType);
					subscriber.onNext(t);
				} catch (Exception e) {
					e.printStackTrace();
				}
				subscriber.onCompleted();
			}
		}).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	public static boolean isJson(String text) {
		try {
			new ObjectMapper().readValue(text, JsonNode.class);
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}


	/**
	 * java对象转换为json字符串
	 * 
	 * @param object
	 *            Java对象
	 * @return 返回字符串
	 */
	public static String fromObjectToJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String args[]) {
		
		Map<Object,Object> jsonMap=new HashMap<Object,Object>();
		jsonMap.put("key1", "key1");
		jsonMap.put("key2", 2);
		System.out.println(fromObjectToJson(jsonMap));

	}

}
