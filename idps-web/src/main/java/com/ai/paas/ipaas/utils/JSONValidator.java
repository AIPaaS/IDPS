package com.ai.paas.ipaas.utils;

import java.util.Map;


public class JSONValidator {

	private JSONValidator() {

	}

	public static boolean isChanged(Map jsonObj, String key, String value) {
		if (jsonObj.containsKey(key) && jsonObj.get(key) != null
				&& !jsonObj.get(key).equals(value)) {
			return true;
		}
		return false;
	}

	public static boolean isChanged(Map jsonObj, String key, int value) {
		if (jsonObj.containsKey(key) && jsonObj.get(key) != null
				&& !jsonObj.get(key).equals(value+"")) {
			return true;
		}
		return false;
	}	
	public static void main(String[] args) {

	}

}
