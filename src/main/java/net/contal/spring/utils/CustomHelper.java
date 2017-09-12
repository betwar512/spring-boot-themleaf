package net.contal.spring.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.assertj.core.util.Strings;

public class CustomHelper {
	public static final String DATE_TIME_GENERAL = "DD/MM/YYYY HH:mm";
	
	public static Date stringToDate(String str) throws ParseException {
		if(!Strings.isNullOrEmpty(str)) {	
		   SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_GENERAL);
		   return sdf.parse(str);
		}
		return null;
	}
	
}
