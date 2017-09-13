package net.contal.spring.logreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import com.google.gson.JsonArray;
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




	//PRE SETTLEMENT  
	private static final String LOG_URL = "/Users/betwar/Desktop/PC_EFT_XY";
	private List<List<String>>  stringMap;
	private List<CustomItemDto> items = new ArrayList<>();
	private List<SettlementDto> listSettlements;
	
	/**
	 * @note : reads all the Log files and generates custome List by thtat Data 
	 * @param str
	 */
	public NABLogHelper(){
		  stringMap = createReceiptMap();
		 this.items = createListItems();
	      Collections.sort(this.items, new CustomItemDto());

	}
	


	/*
	 * Read all the files 
	 * You need to separate from Log files and Reciept files 
	 * */
	private List<List<String>> createReceiptMap(){
		
		List<List<String>> map = new ArrayList<>();//add all to map 
		final File folder = new File(LOG_URL);
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
				    		     map.add(cloneArray);
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
		
		
		return map;
		}
	
	/**
	 * 
	 * @return List<CustomItem>
	 */
  private List<CustomItemDto> createListItems(){	
	   List<List<String>>       array = stringMap;	
		List<CustomItemDto> listItems = new ArrayList<>();
		
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
			 listItems.add(item);
		}//foreach
		return items;
	}
	

	

	/*------------------------------------------------------------------------*/
	
	/*------------------------------------------------------------------*/

	/**
	 * @note write item list to Json 
	 * @throws IOException
	 */
	public static void writeToJson(List<CustomItemDto> listItems) throws IOException{
		int i = 0;
		JsonArray array = new JsonArray();
		final String   outPutFolder="/Users/betwar/Desktop/outfolder/";
	
		final File folder = new File(outPutFolder+"json/");
		if(folder.listFiles().length>0) { //If files exist delete first 
		for(File f : folder.listFiles()) {
			f.delete();
		   }
		}
		
		StringBuilder stBuffer= new StringBuilder();
		stBuffer.append("f-");
		for(CustomItemDto obj: listItems){
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
