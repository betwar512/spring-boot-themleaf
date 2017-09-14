package net.contal.spring.logreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import net.contal.spring.dto.CustomItemDto;
import net.contal.spring.dto.SettlementDto;
import net.contal.spring.utils.CustomHelper;
import net.contal.spring.utils.TypeConvertor;



/**
 * 
 * @author A.H.SAfaie 
 *
 */
public class NABLogHelper {

	public static final Logger logger = Logger.getLogger(NABLogHelper.class);
	//PRE SETTLEMENT  
	
	private  String logUrl ;
	private List<List<String>>  stringMap;
	private List<CustomItemDto> items = new ArrayList<>();
	private List<SettlementDto> listSettlements;
	
	/**
	 * @note : reads all the Log files and generates custome List by thtat Data 
	 * @param str
	 */
	public NABLogHelper(String logUrl){
		this.logUrl = logUrl;
		if(this.logUrl == null) {
			throw new NullPointerException("Url cant be null ");
		}
		  createReceiptMap();
		  createListItems();
	      Collections.sort(this.items, new CustomItemDto());
	      
		this.listSettlements =     getNabSettlements(getSettlementForNab());
		listSettlements.forEach(t->logger.debug(t.toString()));

	}
	


	/*
	 * Read all the files 
	 * You need to separate from Log files and Reciept files 
	 * */
	private void createReceiptMap(){
		
		this.stringMap = new ArrayList<>();//add all to map 
		final File folder = new File(logUrl);
		List<File> files= CustomHelper.listFilesForFolder(folder);

		for(File file:files){
	      try( FileInputStream fstream = new FileInputStream(file)){
			   BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			   String strLine;
			   boolean bool=false;
			   int lineCounter=0; //if is line counter 2 that make ziro and add to array 
			   /* read the log File line by line */
			   ArrayList<String> array=new ArrayList<>();
			   while ((strLine = br.readLine()) != null)   {		  
				  //  CUSTOMER COPY
				  if(strLine.trim().contains("CUSTOMER COPY")) {
				      	bool = true;
				     }else				   
				         if(strLine.equals("------------------------")){	    	
				          	bool=false;
				         	lineCounter++;
				    	} 
				    
				    	if(!bool && lineCounter==2){
				    		@SuppressWarnings("unchecked")
						List<String> cloneArray= (List<String>) array.clone();
				    		if(cloneArray.size()>5) {
				    			this.stringMap.add(cloneArray);
				    		     }
					    	array.clear();
					    	lineCounter=0;
				    	}

				  if(bool && strLine.trim().length()>1){				    	
				      	array.add(strLine.trim());	   	
				      }
			      }
		 
			 br.close(); 
			 fstream.close();
	     	 } catch (Exception e) {
				   System.err.println("Error: " + e.getMessage());
			 }
		  }
		
		
	
		}
	
	/**
	 * 
	 * @return List<CustomItem>
	 */
  private void createListItems(){	
	   List<List<String>>       array = stringMap;	
		this.items = new ArrayList<>();
		
		//Create Items out of String array 
		for(List<String> list:array){
			CustomItemDto item = new CustomItemDto();
			item.setMerchantId(list.get(2).split(" ")[6]);
			item.setCardType(list.get(7));	
			item.setTerminalId(list.get(3).split(" ")[8]);
			
			
			
		boolean statusBool=false;
		int statusCounter=0;
			for(String r : list){
				  if(statusBool) {
					if(statusCounter < 1) {
						statusCounter++;
					   }else   
						   if(statusCounter == 1){	
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
			 this.items.add(item);
		}//foreach
	}
	

  
  /**
	 * @param folderPath
	 * @return
	 */
	private  List<List<String>> getSettlementForNab(){
		
		List<List<String>> map = new ArrayList<>();//add all to map 
		final File folder = new File(logUrl);
		List<File> files= CustomHelper.listFilesForFolder(folder);

		for(File file:files){
		 try(FileInputStream fstream = new FileInputStream(file)){
			boolean bool = false;
			int lineCounter=0;
			
			   BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			   String strLine;
			   /* read the log File line by line */
			   	ArrayList<String> array=new ArrayList<>();
			   while ((strLine = br.readLine()) != null)   {
			  //doing the tag catching here 
				   if(strLine.trim().equals("PRE SETTLEMENT")){
				      	bool = true;
				        }else if(bool && strLine.equals("------------------------")){	    
				      	lineCounter++;
				    	   }     
				    	if(bool && lineCounter==3){
				    		@SuppressWarnings("unchecked")
							List<String> cloneArray=(List<String>) array.clone();
				    		if(cloneArray.size()>5) {
				    		         map.add(cloneArray);
				    	          	}
					    	array.clear();
					    	lineCounter=0;
					    	bool=false;
				    	}

				    
				    if(bool && strLine.trim().length()>1){				    	
				    	array.add(strLine.trim());	
				    	logger.debug(strLine.trim());
				    }
			      }
			br.close(); 
	     	} catch (Exception e) {
				     System.err.println("Error: " + e.getMessage());
			}
		  }

		return map;
	}//Method  
  
  
  
	/**
	 * 
	 * @param retMap
	 * @return
	 */
  public static List<SettlementDto>  getNabSettlements(List<List<String>> retMap){
		
		ArrayList<SettlementDto> list=new ArrayList<>();
		for (List<String> t : retMap){

	   	boolean settlement  = false;
		SettlementDto settl =  null;
		for (String r : t) {//String inside Array String x
			
			String[] stSplit=r.split(" "); //split String by space to detriment what's in our String  
			//cleanUp array 
			List<String> cleanSplit=new ArrayList<>(Arrays.asList(stSplit));
		 cleanSplit.removeAll(Arrays.asList(""," ",null));
	
		 if(!settlement){
			 logger.debug(r);
		    if(r.contains("PRE SETTLEMENT")){
		    	 settl = new SettlementDto();
		    	 settl.purchAmount=0f;
				settlement=true;
                  }      
		 }else{
			 logger.debug(r);
			 if(r.contains("Merchant ID"))
					settl.merchantId=cleanSplit.get(2);	     
				else
					if(r.contains("Terminal ID ")) 
						settl.terminalId=cleanSplit.get(2);
					else 
						if(r.contains("SETTLEMENT") && !r.contains("PRE")){ //Date 
							String time = cleanSplit.get(1);
							 Date d=  CustomHelper.formatToDate(time);
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
								}else 
									if(r.contains("------------------------")) {
									   settlement=false;
									   list.add(settl);
									   }
			 
			 
	             } 
			} //Second for 
		
		}//Firdt Foor
	
	return list;
	}
	

	

	/*--------------------------------------------------------------------------*/
	/* 						Getters								*/
	/*----------------------------------------------------------------------------*/


	public List<CustomItemDto> getItems() {
		return items;
	}


	public List<SettlementDto> getListSettlements() {
		return listSettlements;
	}


	public void setItems(List<CustomItemDto> items) {
		this.items = items;
	}


	public void setListSettlements(List<SettlementDto> listSettlements) {
		this.listSettlements = listSettlements;
	}
	

}
