package net.contal.spring.logreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import net.contal.spring.datahandler.SettlementCustomHandler;
import net.contal.spring.dto.CustomItemDto;
import net.contal.spring.dto.SettlementDto;
import net.contal.spring.utils.ConfigUtils;



/**
 * 
 *@author A.H.Safaie 
 *@note Handling serialization for WFPOS Elixer roofTop backlog 
 *
 * */
public class WestpacLogHelper {

		public static final Logger logger = Logger.getLogger(WestpacLogHelper.class);
					
			//public Map<String,List<CustomItemDto>> objectArray;  //CustomItemList 
		//	public List<List<String>>  stringMap; //Map Strings
			private List<List<String>>  settlementsString; //Settlements for westPack 
			private List<CustomItemDto> items;
			private List<SettlementDto> settlements;
			public WestpacLogHelper(){	
				 this.items = new ArrayList<>();
				 ZipReader zip = new ZipReader();
				 List<CustomItemDto> listzip = zip.getListItems();    
			       //Settlements in logs 
			  this.settlementsString = getSettlement();
			  this.settlements = SettlementCustomHandler.getWestpacSettlements(this.settlementsString);
			/*-----------------------------------------------------------------*/	
			logger.debug("Size before" + zip.getListItems().size());
			  this.settlements.removeAll(zip.list);
			  this.settlements.addAll(zip.list);
			  this.items.removeAll(listzip);
			  this.items.addAll(listzip);
			}
	
			
			
			
			/*
			 * Get all the settlements in log files 
			 * */
	private List<List<String>> getSettlement(){
		List<List<String>> arrayList=new ArrayList<>();//add all to map 
		final File folder = new File(ConfigUtils.getLogPath());
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
									  if(!s.contains("\\0d\\0a") && s.trim().length()>0){ // if not contains \0d\0a
									if( !s.trim().isEmpty()){
									strList.add(s.trim());
										logger.debug(s.trim());
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
					
						logger.debug("Settlements for westpac: Done");
					
						return arrayList;
			
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
