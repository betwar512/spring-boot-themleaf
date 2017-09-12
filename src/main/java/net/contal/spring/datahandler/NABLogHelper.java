package net.contal.spring.datahandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;

import net.contal.spring.model.*;
import net.contal.spring.utils.ConfigUtils;


public class NABLogHelper {

	//PRE SETTLEMENT  

	//public HashMap<String,ArrayList<CustomItem>> map;
	public List<List<String>>  stringMap;
	
	public List<CustomItem> items = new ArrayList<>();
	public List<Settlement> listSettlements;
	/**
	 * @note : reads all the Log files and generates custome List by thtat Data 
	 * @param str
	 */
	public NABLogHelper(){
		  stringMap = createReceiptMap();
		 this.items = createListItems();
	      Collections.sort(this.items, new CustomItem());

	}
	
	
	
	
	
	public static Date writeDate(String strInput){
		SimpleDateFormat sdfmt1 = new SimpleDateFormat("dd/MM/yy");
		Date dDate;
		try {
			dDate = sdfmt1.parse( strInput );			
		return dDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	/**
	 * 
	 * @see	final File folder = new File("/home/you/Desktop");  listFilesForFolder(folder);
	  */
	private static List<File> listFilesForFolder(final File folder) {
	  List<File> files=new ArrayList<>();
	  
		for (File fileEntry : folder.listFiles())  { 	//fpos Root folders 		
			if(fileEntry.isDirectory()){
			for(File secFolder:fileEntry.listFiles()){
			if (secFolder.isDirectory()){
				if(secFolder.getName().equals("Receipt")) {
			     for(final File file: secFolder.listFiles()){
			    	 files.add(file);
			        }
			      }
			 	} else{ 			  
					 if(secFolder.getName().endsWith("LOG")) {
						files.add(secFolder);    
						}
		        }
			  }
			}
				
		}  
	    return files;
	}

	
	/**
	 * @param folderPath
	 * @return
	 */
	public static List<List<String>> getSettlementForNab(){
		//add all to map 
		List<List<String>> map=new ArrayList<>();
		//final String folderPath = "C:\\PC_EFT";
		final File folder = new File(ConfigUtils.getLogPath());
		List<File> files= listFilesForFolder(folder);

		for(File file:files){
		try{
			boolean bool = false;
			int lineCounter=0;
			   FileInputStream fstream = new FileInputStream(file);
			   BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			   String strLine;
			   /* read the log File line by line */
			  ArrayList<String> array=new ArrayList<>();
			   while ((strLine = br.readLine()) != null)   {
			  //doing the tag catching here 
				   if(strLine.trim().equals("PRE SETTLEMENT"))
				    {
				    	bool=true;
				    }else				   
				    	if(bool && strLine.equals("------------------------")){	    	
				    	//bool=false;
				    	lineCounter++;
				    	}     
				    	if(bool && lineCounter==3){
				    		@SuppressWarnings("unchecked")
							ArrayList<String> cloneArray=(ArrayList<String>) array.clone();
				    		if(cloneArray.size()>5)
				    		map.add(cloneArray);
					    	array.clear();
					    	lineCounter=0;
					    	bool=false;
				    	}

				    
				    if(bool && strLine.trim().length()>1){				    	
				    	array.add(strLine.trim());	
				    	System.out.println(strLine.trim());
				    }
			      }
			br.close(); 
	     	} catch (Exception e) {
				     System.err.println("Error: " + e.getMessage());
			}
		  }

		return map;
	}//Method  
	
		//System.out.println("Settlements for westpac: Done");
	
/**
 * 
 * @param dateInString
 * @return
 */
private static Date formatStrongToDate(String dateInString){
	
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy hh:mm");
	Date date=null;
	try {

		 date = formatter.parse(dateInString);
		System.out.println(date);
		System.out.println(formatter.format(date));

	} catch (ParseException e) {
		e.printStackTrace();
	}
	return date;
}
	


	/**
	 * @deprecated
	 * @param map
	 * @return
	 */
	public static Map<Date,List<String>>  settlmentMapByDate(){
		
		List<List<String>> map = getSettlementForNab();
		
		
		Map<Date,List<String>> dateMap = new HashMap<>();

		for(List<String> item:map){
			Date date = new Date();
			if(item.size()>37){
			String dateString = item.get(38);
			
			if(dateString.length()>0) {
			    date = formatStrongToDate(dateString) ;
			   }
			
			if(dateMap.containsKey(date)){
			    dateMap.put(date, item);
			  }else{
				dateMap.put(date,item);
			}
		   }else {
			   System.out.println("36 > " + item.size());
			   }
		}
		return dateMap;
	}
	
	
	public static List<Settlement>  getNabSettlements(List<List<String>> retMap){
	
		ArrayList<Settlement> list=new ArrayList<>();
	 for (List<String> t : retMap){

	    	boolean settlement  =false;
	//	boolean terminalBool=false;
		 Settlement settl=new Settlement();		
		 settl.purchAmount=0f;
	//	 int index=0; 
		for (String r : t) {//String inside Array String x
			
			String[] stSplit=r.split(" "); //split String by space to detriment what's in our String  
			//cleanUp array 
			List<String> cleanSplit=new ArrayList<>(Arrays.asList(stSplit));
		 cleanSplit.removeAll(Arrays.asList(""," ",null));
		// String stS=cleanSplit.get(0);
		 
		 
		 if(!settlement){
		    if(r.contains("PRE SETTLEMENT")){
				settlement=true;
                    }      
		 }else{
			 
			 if(r.contains("Merchant ID"))
					settl.merchantId=cleanSplit.get(2);	     
				else
					if(r.contains("Terminal ID ")) 
						settl.terminalId=cleanSplit.get(2);
					else 
						if(r.contains("SETTLEMENT") && !r.contains("PRE")){ //Date 
							String time = cleanSplit.get(1);
							 Date d=  writeDate(time);
							 settl.setDate(d);
						}else
							if(r.contains("Purch Amount")){
								String purchaseAmount = cleanSplit.get(2);
								purchaseAmount=  purchaseAmount.replace("$","");
								settl.purchAmount=Float.parseFloat(purchaseAmount);
								
							}else
								if(r.contains("Purch Count")){
								String count=	cleanSplit.get(2);
								settl.setPurchCount(count);
								}else if(r.contains("------------------------"))
									   settlement=false;
			 
			 
		 }
		 
		 
			
			} //Second for 
		list.add(settl);
		}//Firdt Foor
	
	return list;
	}
	
	
	
	
	
	
	
	
	/*
	 * Read all the files 
	 * You need to sepeerate from Log files and Reciept files 
	 * */
	private List<List<String>> createReceiptMap(){
		
		List<List<String>> map = new ArrayList<>();//add all to map 
		
		//int mapIndex=0;
		
		//final String folderPath = "C:\\PC_EFT";
		final File folder = new File("/Users/betwar/Desktop/PC_EFT_XY");
		List<File> files= listFilesForFolder(folder);
		
		
	
		for(File file:files){
			  FileInputStream fstream  = null;
	     	try{
	
			    fstream = new FileInputStream(file);
			   BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			   String strLine;
			   boolean bool=false;
			   int lineCounter=0; //if is line counter 2 that make ziro and add to array 
			   /* read the log File line by line */
			   ArrayList<String> array=new ArrayList<>();
			   while ((strLine = br.readLine()) != null)   {
				  
				  //  CUSTOMER COPY
				   
		 
				    if(strLine.trim().contains("CUSTOMER COPY"))
				    {
				    	bool=true;
				    }else				   
				    	if(strLine.equals("------------------------")){	    	
				    	bool=false;
				    	lineCounter++;
				    	} 
				    
				    	if(!bool && lineCounter==2){
				    		@SuppressWarnings("unchecked")
							ArrayList<String> cloneArray=(ArrayList<String>) array.clone();
				    		if(cloneArray.size()>5) {
				    		     map.add(cloneArray);
				    		     }
					    	array.clear();
					    	lineCounter=0;
				    	}

				   if(bool && strLine.trim().length()>1){				    	
				    	array.add(strLine.trim());	
				    	
				    }

				    	

			
			      }
		  fstream.close();
			br.close(); 
	     	} catch (Exception e) {
				     System.err.println("Error: " + e.getMessage());
			}finally {
				
			}
		  }
		
		
		return map;
		}
	

	/*-----------------------------------------------------------------------------------------------------------*/
	
	/*-----------------------------------------------------------------------------------------------------------*/
	/**
	 * CreaTE CUSTOMITEM Map with key CardTYPE
	 * @deprecated
	 * 
	 * */
	
	private Map<String,List<CustomItem>> createCustomMap(){	
		List<List<String>> array=stringMap;	
		Map<String,List<CustomItem>> map=new HashMap<>();				
		for(List<String> list:array){
			CustomItem item=new CustomItem();	
			item.setMerchantId(list.get(2).split(" ")[6]);
			item.setCardType(list.get(7));	
			item.setTerminalId(list.get(3).split(" ")[8]);
		boolean  statusBool = false;
		int   statusCounter =     0;

			for(String r : list){
				if(statusBool) {
					if(statusCounter<1) {
						statusCounter++;
				   	 }else 
						if(statusCounter==1) {	
								statusCounter=0;
								item.setStatus(r);
								statusBool=false;	
							}
						}
				
				
				if(r.startsWith("#")) {//check if it's card number 
					item.setCardNumber(r.split(" ")[0].substring(12)); //last 4 digit 
					statusBool=true;		
					}
				    else if(r.contains("Date/Time")){
					 Date d = TypeConvertor.stringToDate(r.substring(9).trim());
					item.setDateTime(d);
				}
				else 
					if(r.startsWith("PURCHASE")){
						
						String[] s=r.split(" ");
						String value = s[s.length-1].replace("$", "");
						item.setPurchaseAmount(value);
						}
				else 
					if(r.contains("TOTAL AUD")){
						String[] t=r.split(" ");
						
						String value = t[t.length-1].replace("$", "");
						item.setTotalAmount(Float.valueOf(value));
						}
			      }	
				


			
			if(!map.containsKey(item.getCardType())){	
				ArrayList<CustomItem> c=new ArrayList<>();
				c.add(item);
			map.put(item.getCardType(),c);
			}else{
				
				List<CustomItem> c=map.get(item.getCardType());
			c.add(item);
				
			}//else
			
		}//foreach

		return map;	
	}
	/**
	 * 
	 * @return List<CustomItem>
	 */
  private List<CustomItem> createListItems(){
		
	   List<List<String>> array=stringMap;	
		List<CustomItem> items = new ArrayList<>();
		
		//Create Items out of String array 
		for(List<String> list:array){
			CustomItem item=new CustomItem();
			item.setMerchantId(list.get(2).split(" ")[6]);
			item.setCardType(list.get(7));	
			item.setTerminalId(list.get(3).split(" ")[8]);
			
			
			
		boolean statusBool=false;
		int statusCounter=0;
			for(String r : list){
				  if(statusBool) {
					if(statusCounter<1) {
						statusCounter++;
					}else if(statusCounter==1){	
						statusCounter=0;
						item.setStatus(r);
						statusBool=false;	
					}
				}
				
				
		   	 if(r.startsWith("#")) {//check if it's card number 
					item.setCardNumber(r.split(" ")[0].substring(12)); //last 4 digit 
					statusBool=true;		
					}else 
				    	    if(r.contains("Date/Time")){
					    Date d = TypeConvertor.stringToDate(r.substring(9).trim());
					        item.setDateTime(d);
							}
							else 
								if(r.startsWith("PURCHASE")){
									String[] s=r.split(" ");
									String strFloat = s[s.length-1];
									strFloat = 	strFloat.replace("$", "");
									item.setPurchaseAmount(strFloat);
									}else 
								        if(r.contains("TOTAL AUD")){
											String[] t=r.split(" ");
											
											String strFloat = t[t.length-1];
											strFloat = 	strFloat.replace("$", "");
											item.setTotalAmount(Float.valueOf(strFloat));
									}
						      }	
				
				items.add(item);

		}//foreach

		return items;
	}
/*-----------------------------------------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------------------------------------*/
	
	/**
	 * @note write item list to Json 
	 * @throws IOException
	 */
	public static void writeToJson(List<CustomItem> listItems) throws IOException{
		int i = 0;
		JsonArray array = new JsonArray();
		final String   outPutFolder="/Users/betwar/Desktop/outfolder/";
	
		final File folder = new File(outPutFolder+"json/");
		if(folder.listFiles().length>0)  //If files exist delete first 
		for(File f : folder.listFiles()) {
			f.delete();
		}
		
		StringBuilder stBuffer= new StringBuilder();
		stBuffer.append("f-");
		for(CustomItem obj: listItems){
			if(stBuffer.length()==2) {
			  stBuffer.append(obj.getDate());
			     }
			      
			
			array.add(obj.toJson());
			i++;

			if (i%1000==0){
				stBuffer.append("-e-");
				stBuffer.append(obj.getDate());
				System.out.println("count for array "+ i );
				 FileWriter fileWriter = new FileWriter(outPutFolder+"json/"+stBuffer+".json");
			    stBuffer.delete(0, stBuffer.length());
				 stBuffer.append("f-");
				 fileWriter.write(array.toString());
				 fileWriter.close();
				 array = new JsonArray();
				
			}
			
		}
		
		if (i%1000!=0){ //Last array need to be write in here 
			stBuffer.append("-e-");
			stBuffer.append(listItems.get(listItems.size()-1).getDate());
			System.out.println("count for array "+ i );
			 FileWriter fileWriter = new FileWriter(outPutFolder+"json\\"+stBuffer+".json");
			 fileWriter.write(array.toString());
			 fileWriter.close();
		}
	}
	
	
	
	
	public static  File writetoFile(Map<Date,ArrayList<String>> bla,Date startD,Date endD) throws IOException{

           Iterator<Entry<Date, ArrayList<String>>> it =bla.entrySet().iterator();
      	   FileWriter file=null;
	       File rFile = new File("settlement");

		if(rFile.createNewFile()){
	       file = new FileWriter(rFile);
		}
			 
		 BufferedWriter bw = new BufferedWriter(file);
		    
   while(it.hasNext()){
	Entry<Date, ArrayList<String>> ite = it.next();
	 Date key =ite.getKey();
	 if(key.after(startD)&&key.before(endD)){
	 List<String>li= ite.getValue();
	 System.out.println("---------Top-------------");

	 for(String t: li){
		 bw.write(t+'\n');
	 System.out.println(t);}
	 System.out.println("---------###-------------");

	 			}
			}
			bw.close();
			return rFile;
		}
	

}
