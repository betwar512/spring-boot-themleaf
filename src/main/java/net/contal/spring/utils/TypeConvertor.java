package net.contal.spring.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class TypeConvertor {

	
	
	
	public static final Date settelmentDate(String str){

		Date result=null;
		int count=str.length();
		if(count==12){
			String month=str.substring(2, 5);
			String day=str.substring(0, 2);
			String year =str.substring(5, 7);
			String time=str.substring(7);

			Date date = null;
			try {
				date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(date);
		    int monthInt = cal.get(Calendar.MONTH);
		    System.out.println(monthInt);

		    String dateString=day+"/"+monthInt+"/"+year+" "+time;
		 result=stringToDate(dateString);    
		}
		
		return result;
		
	}
	
	
	/**
	 * @note : create list of days between 
	 * @param start
	 * @param end
	 * @return List Dates 
	 */
	public static List<Date> listDaysForSettlement(Date start,Date end){
		
		List<Date> dates = new ArrayList<>();
		
		  Calendar cal = Calendar.getInstance();
		           cal.setTime(start);
		           cal.set(Calendar.HOUR_OF_DAY,7);
		           start = cal.getTime();
		           
		           dates.add(start);
		           
		           Calendar cal2 = Calendar.getInstance();
		           cal2.setTime(end);
		           cal2.set(Calendar.HOUR_OF_DAY,7);
		           end = cal2.getTime();    
		           
		           
		           
		           while(start.before(end)){
		    
		           Calendar calNext= Calendar.getInstance();  
		                    calNext.setTime(start);
		                    calNext.add(Calendar.DATE,1);   //Set new last Date 
		                    start = calNext.getTime();
		                    dates.add(start);
		                    
		                    //Check if last one add 1 to end and finilize the loop 
		                    if(start.equals(end))
		                    {
		                    	 Calendar calEnd= Calendar.getInstance();  
		                    	 calEnd.setTime(end);
		                    	 calEnd.add(Calendar.DATE,1);   //Set new last Date 
				                 Date   nextAfterEnd = calNext.getTime();
				                    dates.add(nextAfterEnd);
		                    }
		                    
		           }
		    
		return dates;
		
	}
	
	
	
	/*
	 * Convert String DateTime to java Type DateTime
	 * dateFormat="dd/MM/yy HH:mm"
	 */
	public static final Date stringToDate(String str){
	
	 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
	 
	 Date date=null;
     try
     {
          date = simpleDateFormat.parse(str);
       //  System.out.println("date : "+simpleDateFormat.format(date));
     }
     catch (ParseException ex)
     {
         System.out.println("Exception "+ex);
     }
     
     return date;
	}

	
	//Convert date for Jquery input 
	public static final Date jqueryStringToDate(String str){
		
		 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		 
		 Date date=null;
	     try
	     {
	          date = simpleDateFormat.parse(str);
	       //  System.out.println("date : "+simpleDateFormat.format(date));
	     }
	     catch (ParseException ex)
	     {
	         System.out.println("Exception "+ex);
	     }
	     
	     return date;
		}
	
	
	
	public static final Date convertStringToDateElixer(String str){
		
 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm");
		 
		 Date date=null;
	     try
	     {
	          date = simpleDateFormat.parse(str);
	      //  System.out.println("date : "+simpleDateFormat.format(date));
	     }
	     catch (ParseException ex)
	     {
	         System.out.println("Exception "+ex);
	     }
	     
	     return date;
	}
	
	/*
	 * Convert string to Float  
	 */
  public static final Float stringTofloat(String str){
	
	String number=str.substring(1);
	Float f=Float.parseFloat(number);
	
	return f;
    }
  
  public static void cardGroups(){
	    
  }
  
  
  /*
   * Time formatter to 24h from 12h
   * */
  public static String convertTime(String str){
		  SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
		  SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mma");
		  String time = "";
		  Date date=new Date();
		try {
			date = parseFormat.parse(str);
			time = displayFormat.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		  return time;
     }
  
  
}
