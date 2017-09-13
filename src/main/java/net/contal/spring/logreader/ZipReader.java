package net.contal.spring.logreader;

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
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import com.google.gson.JsonArray;

import net.contal.spring.datahandler.SettlementCustomHandler;
import net.contal.spring.dto.CustomItemDto;
import net.contal.spring.dto.SettlementDto;
import net.contal.spring.utils.ConfigUtils;
import net.contal.spring.utils.TypeConvertor;


/**
 * @ westPac 
 * @author betwar
 *
 */
public class ZipReader {


	public List<CustomItemDto> getListItems() {
		return listItems;
	}

	public void setListItems(List<CustomItemDto> listItems) {
		this.listItems = listItems;
	}
	private static final String   outPutFolder= "outfolder/";
	//"C:\\log-out\\"; "/Users/betwar/Desktop/outfolder/";
	private static final String jsonPath = outPutFolder+"json/";//json\\";
	private static final String logPath = outPutFolder+"log/";
	private Map<String,List<CustomItemDto>> objectArray;  //CustomItemList 
	private List<List<String>>  stringMap; //Map Strings
	private List<List<String>>  settlement; //Settlements for westPack 
	private List<CustomItemDto> listItems = new ArrayList<>();
	List<SettlementDto> list ;
	List<File> files; //All the log unzip files 
	
	public ZipReader(){
		this.listItems = new ArrayList<>();
		checkFolders();
		    setFiles();
		  stringMap = createMap();
		objectArray = createCustomItems();
		List<List<String>> settlements= getSettlement();
		 this.list = SettlementCustomHandler.getWestpacSettlements(settlements);
		 System.out.println(list.size());	
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
		      final File folder = new File("/Users/Betwar/Desktop/Elixer");
	 	   List<File>  zipFiles = listFilesForFolderZiped(folder);
                     this.files = new ArrayList<>();

	 for(File file:zipFiles){
		 List<File> filesUnzip;
		 try {
			filesUnzip = unzip(file);
			this.files.addAll(filesUnzip);
		} catch (IOException e) {
			e.printStackTrace();
		    }
		
		 }
	 
	this.files.addAll(listFilesForFolder(folder));
	 

	}
	
	/*
	 * arg Folder Path as a file type 
	 * Capture all the files with sufix : .LOG 
	 */
private  List<File> listFilesForFolder(final File folder) {
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
	 * @note write item list to Json 
	 * @throws IOException
	 */
	public void writeToJson() throws IOException{
		int i = 0;
		JsonArray array = new JsonArray();
		final File folder = new File(outPutFolder+"json\\");
		if(folder.listFiles().length>0)  //If files exist delete first 
			for(File f : folder.listFiles()) f.delete();
		
		Collections.sort(this.listItems, new CustomItemDto());//Sort it first 
		StringBuffer stBuffer= new StringBuffer();
		stBuffer.append("f-");
		for(CustomItemDto obj: this.listItems){
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
	private List<List<String>> getSettlement(){
	
		List<List<String>> arrayList=new ArrayList<>();//add all to map 
	
		for(File file:this.files){
					
			try( FileInputStream fstream = new FileInputStream(file)){
					   BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
					   String strLine;
					   /* read the log File line by line */
					   while ((strLine = br.readLine()) != null)   {
					  //doing the tag catching here 
			   
						boolean bool=false; //checker for B 
				   
						   //check for a tag 
						   if(strLine.contains("B     PRE-SETTLEMENT")) {
						    bool=true;	 
						    }
						   
						   //end of the data 
					   if(strLine.contains("<?xml version")){
					    	 bool=false;     
					    }		   
						 if(bool){	   
							  String[] strArray=strLine.split("B");//split by B	  
							  List<String> strList=new ArrayList<>();
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
private  List<List<String>> createMap(){

	List<List<String>> map=new ArrayList<>();//add all to map 

		for(File file:this.files){

		   try( FileInputStream   fstream = new FileInputStream(file)){
			  
			   BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			   String strLine;
			   /* read the log File line by line */
			   while ((strLine = br.readLine()) != null)   {
			  //doing the tag catching here 
	   
				//   boolean bool = false; //checker for B 
				   //check for a tag 
				   boolean    bool = strLine.contains("B     CUSTOMER COPY      B") ?  true : false;
			      //end of the data 
				 if(strLine.contains("<?xml version")) {
					   bool = false;
				  } 

				   if(bool){	   
					  String[] strArray=strLine.split("B");//split by B	  
					  List<String> strList=new ArrayList<>();
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
			  fstream.close();
			} catch (Exception e) {
			     System.err.println("Error: " + e.getMessage());
		} 
	}//top forEach 

		return map;
		
	}
	


	
/*
* Test level 3
* Version 2.0
* Map Array String to CustomItems with cardType as key 
*/
private  Map<String,List<CustomItemDto>> createCustomItems(){
		

		//get Map 
	List<List<String>> retMap = this.stringMap;	
	         Map<String,List<CustomItemDto>> map=new HashMap<>();
	         List<CustomItemDto> clisItems= new ArrayList<>(); //use this to write Json +
		for (List<String> t : retMap) {

			//Entry<Integer, ArrayList<String>> t = entries.next();	
			CustomItemDto item=new CustomItemDto(); //Pass an Item 
			
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
				List<String> cleanSplit=new ArrayList<>(Arrays.asList(stSplit));
			 cleanSplit.removeAll(Arrays.asList(""," ",null));
			 String stS=cleanSplit.get(0);
			 
			
			 //Card Type 
			 if(bool&& cardCounter>=1 && r.length()>3){ 
				 	item.setCardType(r.toLowerCase());
				 	bool=false;
				 	}
			 if(bool) {
				 cardCounter++;
			 }
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
				ArrayList<CustomItemDto> c=new ArrayList<>();
				c.add(item);
			map.put(item.getCardType(),c);
			}else{
				
			List<CustomItemDto> c=map.get(item.getCardType());
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
		  List<File> files=new ArrayList<>();
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
		List<File> files = new ArrayList<>();
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