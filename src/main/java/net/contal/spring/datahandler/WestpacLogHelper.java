package net.contal.spring.datahandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.contal.spring.model.CustomItem;
import net.contal.spring.model.Settlement;
import net.contal.spring.util.ConfigUtils;



/**
 * 
 *@author A.H.Safaie 
 *@note Handling serialization for WFPOS Elixer roofTop backlog 
 *
 * */
public class WestpacLogHelper {

					
			public HashMap<String,ArrayList<CustomItem>> objectArray;  //CustomItemList 
			public ArrayList<ArrayList<String>>  stringMap; //Map Strings
			public ArrayList<ArrayList<String>>  settlement; //Settlements for westPack 
			public List<CustomItem> items = new ArrayList<CustomItem>();
			public List<Settlement> settlements;
			public WestpacLogHelper(){	
				String str=ConfigUtils.getLogPath();
				ZipReader zip = new ZipReader();
				 List<CustomItem> listzip = zip.listItems;
			       stringMap=createMap(str);
			       objectArray=createCustomItems();
			    
			       //Settlements in logs 
			  this.settlement=getSettlement();
			  this.settlements=SettlementCustomHandler.getWestpacSettlements(this.settlement);
			/*-----------------------------------------------------------------*/	
			//System.out.println("Size before" + zip.listItems.size());
			  this.settlements.removeAll(zip.list);
			  this.settlements.addAll(zip.list);
			  this.items.removeAll(listzip);
			  this.items.addAll(listzip);
			//  listzip.addAll(this.items);	  	
					//System.out.println("Size After" + zip.listItems.size());
			/*	  try {
					zip.writeToJson();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
	/*-----------------------------------------------------------------*/	
			//	settlement=getSettlement(str);
			  //    SettlementWriter.writeJson(objectArray);

			}
			

		
			/*
			 * arg Folder Path as a file type 
			 * Capture all the files with sufix : .LOG 
			 */
			private static List<File> listFilesForFolder(final File folder) {
				  List<File> files=new ArrayList<>();
				  
					for (File fileEntry : folder.listFiles())  {//fpos Root folders 	
						//fileEntry.add(fileEntry);   
						
						if(fileEntry.isDirectory()){
						for(File secFolder:fileEntry.listFiles()){
						if (secFolder.getName().endsWith("LOG") ||secFolder.getName().endsWith("log") ){					
						    	 files.add(secFolder);

							}
						  }
						}
					}  
				    return files;
				}

	/**
	 * Generate ArrayList out of LOG file 
	 * Map file into Array 
	 * @deprecated
	 */
	private  ArrayList<ArrayList<String>> createMap(String folderPath)
			{

				ArrayList<ArrayList<String>> map=new ArrayList<ArrayList<String>>();//add all to map 
				//int mapIndex=0;
				
			//	final String folderPath = "C:\\PC_EFT";
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
							//	System.out.println(s.trim());
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

			//	System.out.println("Wespac Logs : Done");

				return map;
				
			}
			

	
			
	/**
	 * Test level 3
	 * Version 2.0
	 * Map Array String to CustomItems with cardType as key 
	 * @deprecated
	 */
	private  HashMap<String,ArrayList<CustomItem>> createCustomItems(){
				
	
				//get Map 
			ArrayList<ArrayList<String>> retMap=this.stringMap;	
			HashMap<String,ArrayList<CustomItem>> map=new HashMap<>();

				for (ArrayList<String> t : retMap) {
		
					//Entry<Integer, ArrayList<String>> t = entries.next();	
					CustomItem item=new CustomItem(); //Pass an Item 
					
					/*bool flags to validate the pattern 
					 * NOT 100% but works in most of a time  
					 *
					 */
					boolean bool=false;
					boolean statBool=false;
					boolean auth=false;
				    int cardCounter=0; //2 line after date/time
				  //  int statusCounter=0;
	                    int index =0;
					for (String r : t) {//String inside Array String 		
						//Try Items
						String[] stSplit=r.split(" "); //split String by space to detriment what's in our String  
				
						
						//cleanUp array 
						List<String> cleanSplit=new ArrayList<String>(Arrays.asList(stSplit));
					 cleanSplit.removeAll(Arrays.asList(""," ",null));
					 String stS=cleanSplit.get(0);
					 
					
					 //Card Type 
					 if(bool&& cardCounter>=1 && r.length()>3){ 
						 	item.setCardType(r.toLowerCase());
						 	bool=false;	}
					 if(bool)
						 cardCounter++;
					 
					 //Status 
					 	if(statBool &&  auth){
					 		item.setStatus(r);
					 		auth=false;
					 		statBool=false;
					 	}	 
					 if(statBool&& !auth)
					 {
						 if(r.contains("AUTH"))
							 auth=true;
						 else{
							item.setStatus(r);
							statBool=false;	
						 }
					 }
					 
			
					 if(r.startsWith("......")) {//check if it's card number 
							item.setCardNumber(cleanSplit.get(0).substring(6));
							
							}else
									if(stS.contains("TERMINAL")) 
										item.setTerminalId(cleanSplit.get(2));	
									else
										if(stS.contains("DATE/TIME")){	
											String month="";
											String year="";
											String date="";
											String day=cleanSplit.get(1).substring(0, 2);
											if(cleanSplit.get(1).length()<5){ //FEB issue 
										//	System.out.println("Less Time date");
											month=cleanSplit.get(1).substring(2, 4);
											
											
											String yearAfter = t.get(index+1);
											
											String[] spList = yearAfter.split(" ");
											 year =" 20"+ spList[0]; 
											month+="B";
											
											date	= month +" "+day+" "+year +" " +spList[1];
											
											}else{ 
											//TODO problem here need to be changed 
											 month=cleanSplit.get(1).substring(2,5);
											 year =" 20"+ cleanSplit.get(1).substring(5, 7);
											date	= month +" "+day+" "+year +" " +cleanSplit.get(2);
											 }							 
										

										
											
										 Date d = TypeConvertor.convertStringToDateElixer(date);
										 item.setDateTime(d);
										 bool=true;
												}
												else
													if(stS.contains("AMOUNT")) 
													item.setPurchaseAmount(cleanSplit.get(1));
												else 
													if(stS.contains("TOTAL")){
														item.setTotalAmount(TypeConvertor.stringTofloat(cleanSplit.get(2).substring(0)));
														statBool=true;
													}else
													   if(stS.contains("INV/ROC"))
														  item.setMerchantId(cleanSplit.get(2));
											index++;	}	
					
					if(item.isValid()){//check if item is null or not 
							this.items.add(item);
		
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

		//	SettlementWriter.writeJson(map);

				return map;
				
			}
			
	/**
	 * @note create map out of list group by card type 
	 * @return Map<String,ArrayList<CustomItem>>
	 */
	public static HashMap<String,ArrayList<CustomItem>> createMapFromItems(List<CustomItem> items){
		 HashMap<String,ArrayList<CustomItem>> map= new HashMap<String,ArrayList<CustomItem>>(); 
		
		 Iterator<CustomItem> it = items.iterator();
		 
		 while(it.hasNext()){
			 CustomItem item = it.next();
		if(!map.containsKey(item.getCardType())){	
			ArrayList<CustomItem> c=new ArrayList<>();
			c.add(item);
		map.put(item.getCardType(),c);
		}else{
			
		ArrayList<CustomItem> c=map.get(item.getCardType());
		c.add(item);
			
		}//else
		
	}
		 return map;
	}
		
	/*
	 * Get all the settlements in log files 
	 * */
	private ArrayList<ArrayList<String>> getSettlement(){
	
				ArrayList<ArrayList<String>> arrayList=new ArrayList<ArrayList<String>>();//add all to map 
				//int mapIndex=0;
				
				//final String folderPath = "C:\\PC_EFT";
				final File folder = new File(ConfigUtils.getLogPath());
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
						   if(strLine.contains("B     PRE-SETTLEMENT"))
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
							//	System.out.println(s.trim());
							}			 
						}	  
					}
						  arrayList.add(strList);
				}
			}
					  br.close();
					} catch (Exception e) {
					     System.err.println("Error: " + e.getMessage());
				}
			}//top forEach 
			
				System.out.println("Settlements for westpac: Done");
			
				return arrayList;
	
}


	
			

	

}
