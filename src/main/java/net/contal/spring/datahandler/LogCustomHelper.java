package net.contal.spring.datahandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.contal.spring.model.CustomItem;



	/**
	 * @author Betwar-mac
	 *
	 */
	public class LogCustomHelper {
		
		
		public HashMap<String,ArrayList<CustomItem>> objectArray;  //CustomItemList 
		public ArrayList<ArrayList<String>>  stringMap; //Map Strings
		
		public LogCustomHelper(){
			
			objectArray=createCustomItems();
			stringMap=createMap();
			
		}
		
		
		/*
		 * 
		 * 	final File folder = new File("/home/you/Desktop");
	                	listFilesForFolder(folder);
		
		  */
		private static List<File> listFilesForFolder(final File folder) {
		  List<File> files=new ArrayList<>();
		  
			for (final File fileEntry : folder.listFiles())   		
				files.add(fileEntry);      
		    return files;
		}

		
		
		/*
		 * Test level 1
		 * Version 1.0
		 * Map Array String to CustomItems with cardType as key 
		 * 
		 * */
		public  HashMap<String,ArrayList<CustomItem>> createCustomItems(){
			
			   //counters
			int cardTypeCounter=0;
			int statusCounter=0;
			
	
			
			ArrayList<CustomItem> ci=new ArrayList<>();
			//get Map 
		ArrayList<ArrayList<String>> retMap=createMap();		
		HashMap<String,ArrayList<CustomItem>> map=new HashMap<>();

			for (ArrayList<String> t : retMap) {
	
				//Entry<Integer, ArrayList<String>> t = entries.next();	
				CustomItem item=new CustomItem(); //Pass an Item 
				
				/*bool flags to validate the pattern 
				 * NOT 100% but works in most of a time  
				 *
				 */
				boolean bool=false;
				boolean terminalBool=false;
				boolean statusBool=false; //validation 2 iteration after card number 
				
				if(t.size()>18)item.setStatus(t.get(19));
				
				
				for (String r : t) {//String inside Array String 		
					//Try Items
					String[] stSplit=r.split(" "); //split String by space to detriment what's in our String  
					String stS=stSplit[0];
					
							//card type 
							if(bool) {
								if(cardTypeCounter<1)
									cardTypeCounter++;
								else 
									if(cardTypeCounter==1)
								{	
									cardTypeCounter=0;
									if(r.contains("DE"))
									item.setCardType("DEBIT");
									else
										item.setCardType(r);
									bool=false;	
								}
							}
							
							
							//get Status 2 line down card number  
							if(statusBool) {
								if(statusCounter<1)
									statusCounter++;
								else 
									if(statusCounter==1)
								{	
									statusCounter=0;
									item.setStatus(r);
									statusBool=false;	
								}
							}
							
							
						if(terminalBool){ //terminal code comes 1 iteration after Terminal keyWord 	
							item.setTerminalId(stSplit[0]);
							terminalBool=false;
						}
						else if(r.startsWith("#")) {//check if it's card number 
						item.setCardNumber(stSplit[0].substring(stSplit[0].length()-4,stSplit[0].length())); //last 4 digit 
						statusBool=true;
						
						}else
							if(stS.contains("Merchant"))item.setMerchantId(stSplit[6]);		     
							else
								if(stS.contains("Terminal")) terminalBool=true;    
								else
									if(stS.contains("Date/Time")){
						
						 Date d = TypeConvertor.stringToDate(stSplit[1]+" "+stSplit[2]);
						item.setDateTime(d);
						bool=true;
						cardTypeCounter=0; //2 line after dateTime 
					}
					else if(stS.contains("PURCHASE"))item.setPurchaseAmount(stSplit[stSplit.length-1]);
					else if(stS.contains("TOTAL"))item.setTotalAmount(TypeConvertor.stringTofloat(stSplit[stSplit.length-1]));	
				}	
				
				if(item.isValid()){//check if item is null or not 
				ci.add(item);
				
				if(!map.containsKey(item.getCardType())){	
					ArrayList<CustomItem> c=new ArrayList<>();
					c.add(item);
				map.put(item.getCardType(),c);
				}else{
					
				ArrayList<CustomItem> c=map.get(item.getCardType());
				c.add(item);
					
				}//else
			}//if valid 
		
		}
			
 
			return map;
			
		}
		
	
		
		/*
		 * Generate ArrayList out of LOG file 
		 * Map file into Array 
		 */
		public  ArrayList<ArrayList<String>> createMap()
		{

			ArrayList<ArrayList<String>> map=new ArrayList<ArrayList<String>>();//add all to map 
			//int mapIndex=0;
			
			final String folderPath = "/Users/betwar/Desktop/Elixer";
			final File folder = new File(folderPath);
			List<File> files= listFilesForFolder(folder);
			
			for(File file:files){
			try{
		
				   FileInputStream fstream = new FileInputStream(file);
				   BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
				   String strLine;
				   /* read the log File line by line */
				   while ((strLine = br.readLine()) != null)   {
				  //doing the tag catching here 
		   
					boolean bool=false; //checker for B 
			   
					   //check for a tag 
					   if(strLine.contains("B     CUSTOMER COPY      B"))
					    bool=true;	 
					   
					   //end of the data 
				     if(strLine.contains("<?xml version")){
				    	 bool=false;     
				     }
					   
					   if(bool){	   
						  String[] strArray=strLine.split("B");//split by B	  
					      ArrayList<String> strList=new ArrayList<String>();
					  for (String s : strArray) {
						//  strList=new ArrayList<String>();//List for Strings	  
						  if(!s.contains("\\0d\\0a") && s.trim().length()>0){ // if not contains \0d\0a
						if( !s.trim().isEmpty()){
						strList.add(s.trim());
							//System.out.println(s.trim());
						}			 
					}	  
				}
					map.add(strList);
			}
		}
				  br.close();
				} catch (Exception e) {
				     System.err.println("Error: " + e.getMessage());
			}
		}//top forEach 

			System.out.println("Done");

			return map;
			
		}
		
		
		
//		/*
//		 * Read all the files 
//		 * */
//		public ArrayList<ArrayList<String>> createReceiptMap()
//		{
//			
//			ArrayList<ArrayList<String>> map=new ArrayList<ArrayList<String>>();//add all to map 
//			//int mapIndex=0;
//			
//			final String folderPath = "/Users/betwar/Desktop/workSpace/boostrap-webapp/src/main/java/receipt";
//			final File folder = new File(folderPath);
//			List<File> files= listFilesForFolder(folder);
//			
//			
//			for(File file:files){
//				
//		     	try{
//		
//				   FileInputStream fstream = new FileInputStream(file);
//				   BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
//				   String strLine;
//				   boolean bool=false;
//				   int lineCounter=0; //if is line counter 2 that make ziro and add to array 
//				   /* read the log File line by line */
//				   ArrayList<String> array=new ArrayList<String>();
//				   while ((strLine = br.readLine()) != null)   {
//					  
//			
//					  //  CUSTOMER COPY
//					    
//					    
//					    if(strLine.trim().equals("CUSTOMER COPY"))
//					    {
//					    	bool=true;
//					    }else
//					    	if(strLine.equals("------------------------")){	    	
//					    	bool=false;
//					    	lineCounter++;
//					    	} 
//					    
//					    
//					    	if(!bool && lineCounter==2){
//					    		
//					    		
//					    		@SuppressWarnings("unchecked")
//								ArrayList<String> cloneArray=(ArrayList<String>) array.clone();
//					    		if(cloneArray.size()>5)
//					    		map.add(cloneArray);
//						    	array.clear();
//						    	lineCounter=0;
//					    	}
//					    
//					    	
//					    	
//					    
//					    if(bool && strLine.trim().length()>1){				    	
//					    	array.add(strLine.trim());	
//					    }
//
//					    	
//
//					   System.out.println(strLine);
//				      }
//				br.close(); 
//		     	} catch (Exception e) {
//					     System.err.println("Error: " + e.getMessage());
//				}
//			  }
//			
//			createCustomMap(map);
//			return map;
//			}
//		
//
//		
//		private HashMap<String,ArrayList<CustomItem>> createCustomMap(ArrayList<ArrayList<String>> array){
//			
//			
//			
//			HashMap<String,ArrayList<CustomItem>> map=new HashMap<String,ArrayList<CustomItem>>();		
//				
//			for(ArrayList<String> list:array){
//				
//				
//				CustomItem item=new CustomItem();
//				
//				item.merchantId=list.get(2).split(" ")[6];//
//				item.cardType=list.get(7);	//
//				item.status=list.get(12);//
//				item.terminalId=list.get(3).split(" ")[8];
//				
//			
//				
//				
//				for(String r : list){
//					
//					
//					if(r.startsWith("#")) {//check if it's card number 
//						item.cardNumber=r.substring(r.length()-4,r.length()); //last 4 digit 
//						
//						}	        
//								else
//									if(r.contains("Date/Time")){
//						
//						 Date d = TypeConvertor.stringToDate(r.substring(9).trim());
//						item.dateTime=d;
//					
//					}
//					else 
//						if(r.contains("PURCHASE")){
//							
//							String[] s=r.split(" ");
//							item.purchaseAmount=r.split(" ")[11].substring(1);	}
//					else 
//						if(r.contains("TOTAL AUD"))
//							item.totalAmount=Float.valueOf(r.split(" ")[10].substring(1));	
//				}	
//					
//
////				
////				item.cardNumber=list.get(10).substring(12);//
////		     	item.purchaseAmount=list.get(15).split(" ")[10].substring(1);	
////				item.totalAmount=Float.valueOf(list.get(16).split(" ")[10].substring(1));
////				item.dateTime;
//
//				
//				if(!map.containsKey(item.cardType)){	
//					ArrayList<CustomItem> c=new ArrayList<>();
//					c.add(item);
//				map.put(item.cardType,c);
//				}else{
//					
//				ArrayList<CustomItem> c=map.get(item.cardType);
//				c.add(item);
//					
//				}//else
//				
//			}//foreach
//
//			return map;
//
//			
//		}
				

}
