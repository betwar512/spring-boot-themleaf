package net.contal.spring.logreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import net.contal.spring.dto.CustomItemDto;
import net.contal.spring.dto.SettlementDto;
import net.contal.spring.utils.TypeConvertor;

/**
 * 
 *@author A.H.Safaie 
 *@note Handling serialization for WFPOS Elixer roofTop backlog 
 *
 * */
public class WestpacLogHelper {

		public static final Logger logger = Logger.getLogger(WestpacLogHelper.class);

	//		private List<List<String>>  settlementsString; //Settlements for westPack 
			private List<CustomItemDto> items;
			private List<SettlementDto> settlements;
			public WestpacLogHelper(String logsUrl){	
				 this.items = new ArrayList<>();
				 ZipReader zip = new ZipReader(logsUrl);
				 List<CustomItemDto> listzip = zip.getListItems();    
			       //Settlements in logs 
			   List<List<String>> settlementsString = getSettlement(logsUrl);
			   getWestpacSettlements(settlementsString);
			/*-----------------------------------------------------------------*/	
			logger.debug("Size before" + zip.getListItems().size());
			  this.settlements.removeAll(zip.list);
			  this.settlements.addAll(zip.list);
			  this.items.removeAll(listzip);
			  this.items.addAll(listzip);
			}
	
			
			
			
	 /*
	  * Get all the settlements in log files 
	  * 
	* */
	private List<List<String>> getSettlement(String logsUrl){
		List<List<String>> arrayList=new ArrayList<>();//add all to map 
		final File folder = new File(logsUrl);
	    List<File> files= listFilesForFolder(folder);
						
			for(File file:files){
				try(FileInputStream fstream = new FileInputStream(file)){				   
							   BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
							   String strLine;
							   /* read the log File line by line */
							   while ((strLine = br.readLine()) != null)   {
							  //doing the tag catching here 
					   
								boolean bool=false; //checker for B 
						   
								   //check for a tag 
								   if(strLine.contains("B     PRE-SETTLEMENT")) {
								    bool=true;	 }
								   
								   //end of the data 
							     if(strLine.contains("<?xml version")){
							    	 bool=false;     
							     }
								   
								   if(bool){	   
									  String[] strArray=strLine.split("B");//split by B	  
								      ArrayList<String> strList=new ArrayList<>();
								  for (String s : strArray) {
									//  strList=new ArrayList<String>();//List for Strings	  
									  if(!s.contains("\\0d\\0a")
											  && s.trim().length()>0
											  && !s.trim().isEmpty()){ // if not contains \0d\0a		
									       strList.add(s.trim());
										 logger.debug(s.trim());
											 
								}	  
							}
								  arrayList.add(strList);
						}
					}
							  br.close();
							} catch (Exception e) {
							     logger.error( e);
						}
					}//top forEach 
				logger.debug("Settlements for westpac: Done");			
			return arrayList;	
		}
	
	/**
	 * 
	 * @param retMap
	 * @return
	 */
	 private  void getWestpacSettlements(List<List<String>> retMap){
			this.settlements = new ArrayList<>();
			for (List<String> t : retMap){

			   	boolean settlement  =false;
				boolean terminalBool=false;

				 SettlementDto settl=new SettlementDto();
				 settl.purchAmount=0f;
				for (String r : t) {//String inside Array String x
					String[] stSplit=r.split(" "); //split String by space to detriment what's in our String  
					//cleanUp array 
					List<String> cleanSplit=new ArrayList<>(Arrays.asList(stSplit));
				 cleanSplit.removeAll(Arrays.asList(""," ",null));
					String stS=cleanSplit.get(0);
				    if(r.contains("PRE-SETTLEMENT")){ settlement = true; }    
				    
					if(terminalBool){ //terminal code comes 1 iteration after Terminal keyWord 	
						settl.terminalId=stSplit[0];
						terminalBool=false;
					        }

					
						if(settlement){
							logger.info(r);
								if(r.contains("MERCHANT ID"))
									settl.merchantId=cleanSplit.get(2);	     
								else
									if(r.contains("TERMINAL ID")) 
										settl.terminalId=cleanSplit.get(2);
									else
										if(stS.contains("DATE/TIME")){
												String month="";
												String date="";
												String day=cleanSplit.get(1).substring(0, 2);
												StringBuilder stBuilder = new StringBuilder();
											if(cleanSplit.get(1).length()<5){ //FEB issue 				 
												  month = cleanSplit.get(1).substring(2, 4);
												stBuilder.append(month+"B"+ " " + day + " " + "2016" + " 00:00");
												}else{ 	
													stBuilder.append(cleanSplit.get(1).substring(2,5) + " " + 
															day + " " + " 20"+ cleanSplit.get(1).substring(5, 7) + 
														" " + cleanSplit.get(2))	;
											     }							 
		                               logger.info(stBuilder.toString());
		                               if(date== "") {
		                            	   logger.info("Empty date ");
		                            	   }
										Date d = TypeConvertor.convertStringToDateElixer(stBuilder.toString());
										settl.date=d;
											}else
												if(r.contains("TOTAL AUD")) {
													 String strTemp = cleanSplit.get(cleanSplit.size()-1).replace("$"," ").trim();			
												  settl.total = Double.valueOf(strTemp);			
												}  else
							 							if(r.contains("TOTAL PUR CNT")) {
							 								settl.purchCount=cleanSplit.get(3);
							 							}
										            }
		                                        }

						  if(settl.isValid()) {
							// settl.logString = t.toString();
							   settlements.add(settl);		
								}
					
			        }
		   }	

				
			/*
			 * arg Folder Path as a file type 
			 * Capture all the files with sufix : .LOG 
			 */
	private static List<File> listFilesForFolder(final File folder) {
		 List<File> files=new ArrayList<>();
				  
		for (File fileEntry : folder.listFiles())  {//fpos Root folders 	
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
	 * @note create map out of list group by card type 
	 * @return Map<String,ArrayList<CustomItem>>
	 */
	public static Map<String,List<CustomItemDto>> createMapFromItems(List<CustomItemDto> items){
		 Map<String,List<CustomItemDto>> map= new HashMap<>(); 		
		 Iterator<CustomItemDto> it = items.iterator();	 
		 while(it.hasNext()){
			 CustomItemDto item = it.next();
	   if(!map.containsKey(item.getCardType())){	
			 List<CustomItemDto> c=new ArrayList<>();
			 c.add(item);
		     map.put(item.getCardType(),c);
		   }else{			
			List<CustomItemDto> c = map.get(item.getCardType());
		     c.add(item);	
		   }//else
		
	    }
		 return map;
	}
	
	
	public List<SettlementDto> getSettlements() {
		return settlements;
	}


	public List<CustomItemDto> getItems() {
		return items;
	}


		




}
