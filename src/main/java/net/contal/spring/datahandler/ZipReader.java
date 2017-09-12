package net.contal.spring.datahandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.gson.JsonArray;

import net.contal.spring.model.CustomItem;
import net.contal.spring.model.Settlement;
import net.contal.spring.utils.ConfigUtils;


/**
 * @ westPac 
 * @author betwar
 *
 */
public class ZipReader {


	private static final String   outPutFolder= "outfolder/";//"C:\\log-out\\"; "/Users/betwar/Desktop/outfolder/";
	private static final String jsonPath = outPutFolder+"json/";//json\\";
	private static final String logPath = outPutFolder+"log/";
	public HashMap<String,ArrayList<CustomItem>> objectArray;  //CustomItemList 
	public ArrayList<ArrayList<String>>  stringMap; //Map Strings
	public ArrayList<ArrayList<String>>  settlement; //Settlements for westPack 
	public List<CustomItem> listItems = new ArrayList<>();
	ArrayList<Settlement> list ;
	List<File> files; //All the log unzip files 
	
	public ZipReader(){
		checkFolders();
		    setFiles();
		stringMap=createMap();
		objectArray=createCustomItems();
		 ArrayList<ArrayList<String>> settlements= getSettlement();
		 this.list = SettlementCustomHandler.getWestpacSettlements(settlements);
		 System.out.println(list.size());	
//		 	for(Settlement it: list)
//		 		System.out.println(it.insert());
	  //SettlementWriter.writeJson(objectArray);
	}
	
	private static void checkFolders(){
		String[] paths =new String[3];
		paths[0]= "outfolder";
		paths[1]=outPutFolder+"json";
		paths[2]=outPutFolder+"log";
	   for(String st:paths){
		  File file = new File(st);
		      if(!file.isDirectory())
			       	      file.mkdir();
	          }
	}
	
	
	
	/**
	 * @note : set all the logFile for zipReader object 
	 */
	public void setFiles(){
		      final File folder = new File(ConfigUtils.getLogPath());
	 	   List<File>  zipFiles = listFilesForFolderZiped(folder);
                     this.files = new ArrayList<File>();

	 for(File file:zipFiles){
		 List<File> filesUnzip;
		 try {
			filesUnzip = unzip(file);
			this.files.addAll(filesUnzip);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		
		 }

	}
	
	
	
	
	
	/**
	 * @note write item list to Json 
	 * @throws IOException
	 */
	public void writeToJson() throws IOException{
		int i = 0;
		JsonArray array = new JsonArray();
		final File folder = new File(outPutFolder+"json\\");
		if(folder.listFiles().length>0)  //If files exist delete first 
			for(File f : folder.listFiles()) f.delete();
		
		Collections.sort(this.listItems, new CustomItem());//Sort it first 
		StringBuffer stBuffer= new StringBuffer();
		stBuffer.append("f-");
		for(CustomItem obj: this.listItems){
			stBuffer.append(obj.getDate());
			
			  
			array.add(obj.toJson());
			i++;

			if (i%1000==0){
				stBuffer.append("-e-");
				stBuffer.append(obj.getDate());
				System.out.println("count for array "+ i );
				 FileWriter fileWriter = new FileWriter(jsonPath+stBuffer+".json");
				 stBuffer=new StringBuffer();
				 stBuffer.append("f-");
				 fileWriter.write(array.toString());
				 fileWriter.close();
				 array = new JsonArray();
				
			}
			
		}
		
		if (i%1000!=0){ //Last array need to be write in here 
			
			System.out.println("count for array "+ i );
			 FileWriter fileWriter = new FileWriter(jsonPath+"jsonArray-"+i+".json");
			 fileWriter.write(array.toString());
			 fileWriter.close();
		}
	}
	


	/*
	 * Get all the settlements in log files 
	 * */
	private ArrayList<ArrayList<String>> getSettlement(){
	
				ArrayList<ArrayList<String>> arrayList=new ArrayList<ArrayList<String>>();//add all to map 
		
				
				for(File file:this.files){
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
	


/*
* Generate ArrayList out of LOG file 
* Map file into Array 
*/
private  ArrayList<ArrayList<String>> createMap(){

		ArrayList<ArrayList<String>> map=new ArrayList<ArrayList<String>>();//add all to map 
		//int mapIndex=0;
		
	//	final File folder = new File(SqliteDbHandler.folderPath);
	//	List<File> zipFiles= listFilesForFolderZiped(folder);
	//	List<File> files= new ArrayList<File>();
		
//		try {
//			
//			for(File file:files){
//		List<File> filesUnzip=unzip(file);
//		files.addAll(filesUnzip);
//		}
//
//			
			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
		for(File file:this.files){
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

	//	System.out.println("Wespac Logs : Done");

		return map;
		
	}
	


	
/*
* Test level 3
* Version 2.0
* Map Array String to CustomItems with cardType as key 
*/
private  HashMap<String,ArrayList<CustomItem>> createCustomItems(){
		

		//get Map 
	        ArrayList<ArrayList<String>> retMap=this.stringMap;	
	         HashMap<String,ArrayList<CustomItem>> map=new HashMap<>();
		  	ArrayList<CustomItem> clisItems= new ArrayList<>(); //use this to write Json +
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
		//	ci.add(item);
				clisItems.add(item);
			if(!map.containsKey(item.getCardType())){	
				ArrayList<CustomItem> c=new ArrayList<>();
				c.add(item);
			map.put(item.getCardType(),c);
			}else{
				
			ArrayList<CustomItem> c=map.get(item.getCardType());
			c.add(item);
				
			}//else
		}//if valid 
			
			
	     this.listItems= clisItems;
			
	
	}

		
		
		

//	SettlementWriter.writeJson(map);


		return map;
		
	}
	
	
	
	
	
	
	
	/*
	 * arg Folder Path as a file type 
	 * Capture all the files with sufix : .LOG 
	 */
	public static List<File> listFilesForFolderZiped(final File folder) {
		  List<File> files=new ArrayList<File>();
		  	File[] tempFile = folder.listFiles();
		  	
		for (File fileEntry : tempFile) {//fpos Root folders 	
				//fileEntry.add(fileEntry);   	
				if(fileEntry.isDirectory()){
				for(File secFolder:fileEntry.listFiles()){
					if(secFolder.isDirectory()){
					for(File nextFile: secFolder.listFiles()){
					if (nextFile.getName().endsWith("ZIP") ||nextFile.getName().endsWith("zip") ){							
				    	 files.add(nextFile);

						 } 	 
					   }
					}
				  }
				}
			}  
		    return files;
		}
	
	public static List<File> unzip(File zipFile) throws IOException{
		checkFolders();
	   byte[] buffer = new byte[1024]; 
		List<File> files = new ArrayList<File>();
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
	    	//get the zipped file list entry
	    	ZipEntry ze = zis.getNextEntry();
	    	
	    	while(ze!=null){		
	    	   String fileName = ze.getName();
	           File newFile = new File(logPath+fileName);
	            if(!newFile.exists())  {  
	           System.out.println("file unzip : "+ newFile.getAbsoluteFile());
	            //create all non exists folders
	           //else you will hit FileNotFoundException for compressed folder
	          // new File(newFile.getParent()).mkdirs();     
	            FileOutputStream fos = new FileOutputStream(newFile);             
	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	       		fos.write(buffer, 0, len);
	               }	
	            fos.close();   
	            System.out.println("NewFile : " + fileName);
	            } //If not exist 
	            ze = zis.getNextEntry(); 
	            files.add(newFile);
	    	}
	    	 zis.closeEntry();
	     	zis.close();
	     	
	
	     	
	     	return files;
	}
	
	
	
	
}
