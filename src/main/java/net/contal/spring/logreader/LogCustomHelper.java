package net.contal.spring.logreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.contal.spring.dto.CustomItemDto;
import net.contal.spring.utils.TypeConvertor;



 /**
  * @author A.Safaie 
  *
  */
@Deprecated
 public class LogCustomHelper {
		
		
	private Map<String,List<CustomItemDto>> objectArray;  //CustomItemList 
	private List<List<String>>             stringMap; //Map Strings
		
		public LogCustomHelper(){
			objectArray = createCustomItems();
			  stringMap = createMap();
		  }
	
		public Map<String, List<CustomItemDto>> getObjectArray() {
			return objectArray;
		}

		public List<List<String>> getStringMap() {
			return stringMap;
		}
	
		
		
		
		/*
		 * 
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
		public  Map<String,List<CustomItemDto>> createCustomItems(){
			
			   //counters
			int cardTypeCounter =  0;
			int statusCounter   =  0;

			List<CustomItemDto> ci=new ArrayList<>();
			//get Map 
			List<List<String>> retMap=createMap();		
		    Map<String,List<CustomItemDto>> map=new HashMap<>();

			for (List<String> t : retMap) {
				CustomItemDto item = new CustomItemDto(); //Pass an Item 
				
				/*bool flags to validate the pattern 
				 * NOT 100% but works in most of a time  
				 *
				 */
				boolean bool=false;
				boolean terminalBool=false;
				boolean statusBool=false; //validation 2 iteration after card number 
				
			if(t.size()>18) {
				item.setStatus(t.get(19));
			     }
	
			for (String r : t) {//String inside Array String 		
					//Try Items
					String[] stSplit=r.split(" "); //split String by space to detriment what's in our String  
					String stS=stSplit[0];
							//card type 
							if(bool) {
								if(cardTypeCounter<1) {
									cardTypeCounter++;
								}else if(cardTypeCounter==1){	
									cardTypeCounter=0;
										if(r.contains("DE")) {
											item.setCardType("DEBIT");
											} else {
									     	item.setCardType(r);
										}
									bool = false;	
								}
							}
							
							
							//get Status 2 line down card number  
							if(statusBool) {
								if(statusCounter<1) {
									statusCounter++;
								  }else 
								 	 if(statusCounter==1){	
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
							if(stS.contains("Merchant")) {
								item.setMerchantId(stSplit[6]);		     
							} else
								 if(stS.contains("Terminal")) {
									terminalBool=true;    
								} else
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
					ArrayList<CustomItemDto> c=new ArrayList<>();
					c.add(item);
				map.put(item.getCardType(),c);
				}else{
				 List<CustomItemDto> c = map.get(item.getCardType());
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
		public  List<List<String>> createMap(){
			List<List<String>> map=new ArrayList<>();//add all to map 
			final String folderPath = "/Users/betwar/Desktop/Elixer";
			final File folder = new File(folderPath);
			List<File> files= listFilesForFolder(folder);
			
			for(File file:files){
			try( FileInputStream fstream =new FileInputStream(file)){
				   BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
				   String strLine;
				   /* read the log File line by line */
				   while ((strLine = br.readLine()) != null)   {
				  //doing the tag catching here 		   
					boolean  bool = strLine.contains("B     CUSTOMER COPY      B") ; //checker for B 	   
	
					   //end of the data 
				     if(strLine.contains("<?xml version")){
				            	 bool = false;     
				        }
					   
					  if(bool){	   
						  String[] strArray=strLine.split("B");//split by B	  
					      ArrayList<String> strList=new ArrayList<>();
					  for (String s : strArray) {
						//  strList=new ArrayList<String>();//List for Strings	  
						  if(!s.contains("\\0d\\0a") && s.trim().length()>0 && !s.trim().isEmpty()){ // if not contains \0d\0a	
						     strList.add(s.trim());		 
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

}
