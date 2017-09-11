package net.contal.spring.datahandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import net.contal.spring.model.CustomItem;


public class JsonHandler {

	private static final String folderwestpac="/Users/betwar/Desktop/EFPOS";
	private static final String path="/Users/betwar/Desktop/outfolder/";//"C:\\log-out\\";
	
	
	/**
	 * 
	 * @return List CustomItem
	 * @throws FileNotFoundException
	 */
	public static ArrayList<CustomItem> readJson() throws FileNotFoundException{
		ArrayList<CustomItem> items=new ArrayList<CustomItem>();
		  Gson gson = new Gson();
		  List<File> files = new ArrayList<File>();
		  final File folder = new File(path+"json/");
		    File[] folderFiles= folder.listFiles();
		  files.addAll(Arrays.asList(folderFiles));
		  
		  for(File file:files){
			  if(file.getName().endsWith("json")){
	        Reader reader = new FileReader(file);
				// Convert JSON to Java Object
	        	CustomItem[] obj = gson.fromJson(reader, CustomItem[].class);
		
	        	List<CustomItem> objects =new ArrayList<CustomItem>();
	        	objects = Arrays.asList(obj);
	        	
	        	   for(CustomItem o:objects) o.setDateFromString(o.getDate());
	        
	        	   items.addAll(objects);
		        }
			  }
		  return items;
	}
	

	/**
	 * @note Write data in json files 
	 */
	public void writeJson(String folderPath){	
		 new WestpacLogHelper();
	}
	
	
}
