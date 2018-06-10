package com.xinyue.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

	public static <T> T jsonToObj(String value, Class<T> t) {
		return JSON.parseObject(value, t);
	}

	public static String objToJson(Object obj) {
		return JSON.toJSONString(obj);
	}

	public static <T> T jsonToObj(JSONObject jsonObject, Class<T> t) {

		return JSON.toJavaObject(jsonObject, t);
	}

}
