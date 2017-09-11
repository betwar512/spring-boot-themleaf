package net.contal.spring.datahandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import net.contal.spring.model.CustomItem;

import java.util.*;




public class SettlementWriter {

	
	
	
	
	public static void writeJson(HashMap<String, ArrayList<CustomItem>> objectArray)  {
		
		
		
	
//		FileWriter fw = null;
//		try {
//			fw = new FileWriter("/Users/betwar/Desktop/JsonObjects/item.json");
//			
//			
//			
//		    
//			for(Entry<String, ArrayList<CustomItem>> items: objectArray.entrySet()){
//				
//				String key = items.getKey();
//				
//				List<CustomItem>    list = items.getValue();
//				Iterator<CustomItem> it = list.iterator();
//				
//				while(it.hasNext()){	
//					CustomItem item = it.next();
//				   	Gson gson =new GsonBuilder().setPrettyPrinting().create();
//				//	gson.toJson(item,fw);
//					gson.toJson(item, fw);
//				}
//			}
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
	}
	
	
	
	
	
	public static void writeToFile(ArrayList<ArrayList<String>> list){
		
		
		for(ArrayList<String> stringList:list){
			
			for(String str:stringList){
				
				File file =new File("/Users/betwar/Desktop/javaio-appendfile.txt");
				
				if(!file.exists()){
	    			try {
						file.createNewFile();
	
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
				
				
				FileWriter fileWritter;
				try {
					   fileWritter = new FileWriter(file.getName(),true);
					   BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		    	        bufferWritter.write(str);
		    	        bufferWritter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
  
				
			}
			
			
			
		}
		
		
		
	}
	
	
	
}
