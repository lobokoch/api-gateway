package br.com.kerubin.api.apigateway.config;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;


public class Utils {
	
	public static String messageFormat(String pattern, Object ... arguments) {
		return MessageFormat.format(pattern, arguments);
	}
	
	public static String format(String pattern, Object ... arguments) {
		return MessageFormat.format(pattern, arguments);
	}
	
	public static boolean allCharsIsEquals(String value) {
		if(isEmpty(value)) {
			return true;
		}
		
		int index = 0;
		char actual = value.charAt(index++);
		char lastChar = actual; 
		while (lastChar == actual && index < value.length()) {
			lastChar = actual;
			actual = value.charAt(index++);			
		}
		
		return index == value.length();
	}
	
	public static boolean isNotEmpty(Object value) {
		return !isEmpty(value);
	}
	
	public static boolean isEmpty(Object value) {
		if (value == null) {
			return true;
		}
		
		if (value instanceof String) {
			return ((String) value).trim().isEmpty();
		}
		
		if (value instanceof Collection) {
			return ((Collection<?>) value).isEmpty();
		}
		
		if (value.getClass().isArray()) {
			return Arrays.asList(value).isEmpty();
		}
		
		return false;
	}
	
	public static String firstUpper(String str) {
		if (isEmpty(str)) {
			return str;
		}
		
		if (Character.isUpperCase(str.charAt(0))) {
			return str;
		}
		
		if (str.length() == 1) {
			return str.toUpperCase();
		}
		
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	public static String lowerWithFirstUpper(String str) {
		if (isEmpty(str)) {
			return str;
		}
		
		String result = firstUpper(str.toLowerCase());
		return result;
	}
	
	public static String firstLower(String str) {
		if (isEmpty(str)) {
			return str;
		}
		
		if (Character.isLowerCase(str.charAt(0))) {
			return str;
		}
		
		if (str.length() == 1) {
			return str.toLowerCase();
		}
		
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	
}
