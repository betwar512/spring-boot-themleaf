package net.contal.spring.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class CustomHelper {
	public static final String DATE_TIME_GENERAL = "DD/MM/YYYY HH:mm";
	public static final String DATE_TIME = "dd/MM/yy hh:mm";
	public static final String DATE = "dd/MM/yy";
	public static Date stringToDateTime(String str) throws ParseException {
		if(str != null && !str.isEmpty()) {	
		   SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_GENERAL);
		   return sdf.parse(str);
		}
		return null;
	}
	
	 /**
	 * @note Time 
	 */
	   public static long millisToNextHour(Calendar calendar) {
			    int minutes = calendar.get(Calendar.MINUTE);
			    int seconds = calendar.get(Calendar.SECOND);
			    int millis = calendar.get(Calendar.MILLISECOND);
			    int minutesToNextHour = 60 - minutes;
			    int secondsToNextHour = 60 - seconds;
			    int millisToNextHour = 1000 - millis;
			    return minutesToNextHour*60*1000 + secondsToNextHour*1000 + millisToNextHour;
			}
	   
	
	
	/**
	 * 
	 * @param dateInString
	 * @return
	 */
	
   public static Date formatStringToDate(String dateInString){	
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME);
		Date date=null;
		try {
			date = formatter.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 return date;
	}
	

 public static Date formatToDate(String strInput){
	SimpleDateFormat sdfmt1 = new SimpleDateFormat(DATE);
	Date dDate = null;
	try {
		dDate = sdfmt1.parse( strInput );			
	} catch (ParseException e) {
		e.printStackTrace();
	  }
	return dDate;
	
}


	
	/**
	 * <p>Get List of Files exist in Log folder for NAB </p>
	 * @see	final File folder = new File("/home/you/Desktop");  listFilesForFolder(folder);
	  */
	public static List<File> listFilesForFolder(final File folder) {
	  List<File> files = new ArrayList<>();
		for (File fileEntry : folder.listFiles())  { 	//fpos Root folders 		
			if(fileEntry.isDirectory()){
			for(File secFolder:fileEntry.listFiles()){
				if( secFolder.isDirectory() && secFolder.getName().equals("Receipt")) {
			        for(final File file: secFolder.listFiles()){
			       	 	files.add(file);
			        }
			      
			 	} else 		  
					 if(secFolder.getName().endsWith("LOG")) {
						   files.add(secFolder);    
						}
		        
			       }
			  }		
		 }  
	    return files;
	}
	
	
/**
 * Write List<List<String>> into file 
 * @param bla
 * @param startD
 * @param endD
 * @return
 */
 public static  File writetoFile(Map<Date,List<String>> bla,Date startD,Date endD){
         Iterator<Entry<Date, List<String>>> it =bla.entrySet().iterator();
      	  FileWriter file=null;
	    File rFile = new File("settlement");
		try {
			if(rFile.createNewFile()){
			   file = new FileWriter(rFile);
			   }
		   } catch (IOException e) {
			e.printStackTrace();
		}
			 
   try(BufferedWriter bw = new BufferedWriter(file)){		    
		   while(it.hasNext()){
			Entry<Date, List<String>> ite = it.next();
			 Date key =ite.getKey();
			 if(key.after(startD)&&key.before(endD)){
				 List<String>li= ite.getValue();
				 System.out.println("---------Top-------------");
		
			 for(String t: li){
				     bw.write(t+'\n');
					 System.out.println(t);
					 }
					 System.out.println("---------###-------------");
			 			   }
					   }
					bw.close();		
		            }catch(IOException e1) {
			   
		         }
			return rFile;
		}
	
}
