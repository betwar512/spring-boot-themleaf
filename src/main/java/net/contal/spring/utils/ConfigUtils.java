package net.contal.spring.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/**
 * <p> Config util class </p>
 * @author A.H.Safaie
 * @version 0.0.5
 *
 */

 public class ConfigUtils {
	 @Autowired
	 Environment env;
	 
  private static SERVERS SERVER_NAME;
	 
  public enum ENTITIES{
		 XY("XY2 Bar"),
		 ELIXIR("Elixir Rooftop");
		 private final String entityName;
		 private static final Map<String, ENTITIES> lookup = new HashMap<>(); 
		 static{
			 for(ENTITIES s:ENTITIES.values()){
				 lookup.put(s.toString(), s);
			 }
		 }
		private ENTITIES(String entityName){
			this.entityName=entityName;
		       }
	     public String getEntityName(){
			return this.entityName;
		     }	
	     public static ENTITIES get(String entityName){
	    	return lookup.get(entityName);
	     }
	  
  }
  
  
  public enum BANKS {
		 NAB("NAB"),
		 WESTPAC("WESTPAC");
		 private final String bankName;
		 private static final Map<String, BANKS> lookup = new HashMap<>(); 
		 static{
			 for(BANKS s:BANKS.values()){
				 lookup.put(s.toString(), s);
			 }
		 }
		private BANKS(String bankName){
			this.bankName=bankName;
		       }
	     public String getBankName(){
			return this.bankName;
		     }	
	     public static BANKS get(String bankName){
	    	return lookup.get(bankName);
	     }
	  
}
  
  
  
  
	public enum SERVERS{
	 NAB("db.db"),
	 WESTPAC("fpos.db");
	 private final String dbName;
	 private static final Map<String, SERVERS> lookup = new HashMap<>(); 
	 static{
		 for(SERVERS s:SERVERS.values()){
			 lookup.put(s.toString(), s);
		 }
	 }
	private SERVERS(String dbName){
		this.dbName=dbName;
	       }
     public String getDbName(){
		return this.dbName;
	     }	
     public static SERVERS get(String name){
    	return lookup.get(name);
     }
	}
	
	 /**
	  * <p> Set server Name </p>
	  * @param serverName
	  */
	 private ConfigUtils(String serverName){
		 SERVER_NAME = SERVERS.get(serverName);
	 }
	 
	 
	 /**
	  * <p> Static init </p>
	  * @return
	  */
    public static ConfigUtils newIstance(){
		String serverName = getServer();
		return new ConfigUtils(serverName);
		
	 }
	 
    /**
	  * <p> Static init </p>
	  * @return
	  */
   public static ConfigUtils newIstance(String name){
		ConfigUtils   cu  = new ConfigUtils(name);
		return cu;
	 }
	 
	 private static final String      DB_DRIVER =  "jdbc:sqlite:db";
	 private static final String JDB_CLASS_NAME = "org.sqlite.JDBC";
	// private static final String      FILE_NAME =  "serv-conf.json";
	 private static final String    CONFIG_PATH = "resources"+getDivider();
	
		
	 public  File getFile() throws IOException{
	 File file = new File(getClass().getClassLoader().getResource("resources").getFile()+getDivider() + 
			 SERVER_NAME.toString().toLowerCase()+"_config.json");
		 if (file.createNewFile()) {
		     System.out.println("File is created!");
		 } else {
		     System.out.println("File already exists.");
		 }
		 return file;
	 }
	 
	private static final String getUrl(){
		return DB_DRIVER + getDivider() + SERVER_NAME.getDbName();
		}
		
	/**
	 * <p>returns divider for windows or linux base systems </p>
	 * @return
	 */
	private static final String getDivider(){
		return SystemUtils.IS_OS_WINDOWS ?"\\":"/";
		}

	/**
	 * <p> Generate Config file for project </p>
	 * @throws IOException
	 */
	public void generateConfig() throws IOException{
			JsonObject root = new JsonObject();
			JsonObject db = new JsonObject();
			root.addProperty("version", "");
			root.addProperty("timestamp",new Date().toString());
			root.addProperty("server-name",SERVER_NAME.toString());
			root.add("database", db); 
			db.addProperty("name",SERVER_NAME.getDbName());
			db.addProperty("url",getUrl());
			db.addProperty("driver", JDB_CLASS_NAME);
			writeToFile(root);
	}
	
	private static String getDbName(){
		String st="";
		JsonObject config;
		try {
			config = readConfig();	
			if(config!=null){
				JsonObject db=  config.getAsJsonObject("database");
				 st= db.get("url").getAsString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return st;
	}
	

	/**
	 * <p> Read config file </p>
	 * @throws IOException
	 * @return JsonConfig Object 
	 */
	private static JsonObject readConfig() throws IOException{
		JsonObject result= null;
		 File file = new File("/Users/betwar/Desktop/nab_config.json");
			if(file.exists()){
			JsonParser parser= new JsonParser();
			JsonElement obg = parser.parse(new FileReader(file));
			     result= obg.getAsJsonObject();
		  }
			return result;
	}
	
	/**
	 *<p> Return server Name from config file </p>
	 * @return
	 * @throws IOException
	 */
	public static String getServer(){
		String st="";
		JsonObject config;
		try {
			config = readConfig();	
			if(config!=null) {
					st=config.get("server-name").getAsString();
					}
		   } catch (IOException e) {
			e.printStackTrace();
		 }
		return st;
		
	}
	
	/**
	* @param JsonObject
	* @return fileName 
	* @throws IOException
	*/
	private String writeToFile(JsonObject str) throws IOException{
	File file = getFile();
	Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
	String formaterJson = gsonBuilder.toJson(str);
	 FileWriter writer = new FileWriter(file);
	writer.write(formaterJson);
	writer.close();
	return file.getName();
	}

	@Deprecated
	public static String getLogPath(){
	 return  SystemUtils.IS_OS_WINDOWS ? "C://PC_EFT":"/Users/Betwar/Desktop/PC_EFT_XY";
	}
	
	
	
	public static String getDbDriver() {
		return DB_DRIVER;
	}

	public static String getDbUrl() {
		 return getDbName();
	}

	public static String getJdbClassName() {
		return JDB_CLASS_NAME;
	}

//	public static String getFileName() {
//		return FILE_NAME;
//	}

	public static String getConfigPath() {
		return CONFIG_PATH;
	}

	public static SERVERS getServerName() {
		return SERVER_NAME;
	}

	
 }
	

