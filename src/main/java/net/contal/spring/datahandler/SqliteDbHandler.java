package net.contal.spring.datahandler;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import net.contal.spring.model.*;
import net.contal.spring.utils.ConfigUtils;



/**
 * 
 * @author A.H.Safaie 
 * @note : opening closing and updating db takes place here 
 */
public class SqliteDbHandler{
	 @Autowired
	 Environment env;
  public	     Connection conn = null;
  public 	      Statement stmt = null;
 // final String     nab = EftposType.nab;
  //final String westpac = EftposType.westpac;
   //protected static final String folderPath ="/Users/Betwar/Desktop/Elixir"; //"C://PC_EFT";
   protected final String serv = "NAB";//ServletSelector.getFromEnvironment("conf\\serv-conf.txt");
   public static final  String dbURL = "jdbc:sqlite:/Users/Betwar/Desktop/temp/xydb.db";
   public static final String jdbClassName = "org.sqlite.JDBC";
   public SqliteDbHandler() throws SQLException{ 
	  if(conn==null) {
	  conn = openConnection(); 
	  }
   }

   
   
	/**
	 * Check if tables exist if not create it 
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static void checkDb(){
		  Connection cn  = null;
	          try {
				Class.forName(ConfigUtils.getJdbClassName()).newInstance();
		           cn =  DriverManager.getConnection(dbURL);
		            Statement st = cn.createStatement();
		            		st.executeUpdate(createTable()); //Table items
		            		st.close();
		            		  Statement st1 = cn.createStatement();
		            		  st1.executeUpdate(Settlement.createTable()); //Table settlements      
		            		  st1.close();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
	       
	}
	/**
	 * 
	 * @return String Sql create table 
	 */
public static String createTable(){
		
		
		return "CREATE TABLE IF NOT EXISTS CUSTOMITEM("+
				   "ID               INTEGER PRIMARY  KEY AUTOINCREMENT,"+
				   " terminalId      TEXT     NULL,"+
				    "cardNumber      TEXT     NULL,"+
				    "merchantId      TEXT     NULL,"+
				   " status          TEXT     NULL,"+
				    "purchaseAmount  REAL     NULL,"+
				    "totalAmount     REAL     NULL,"+
				    "cardType        CHAR(16) NULL,"+
				    "dateTime        INTEGER      NULL)";
		
	}
	
   

  
	/**
	 * @note : Open Sqlite connection to fpos.db 
	 */
	public static Connection openConnection(){
		
	  try {
          Class.forName(ConfigUtils.getJdbClassName());
          
         Connection conn = DriverManager.getConnection(dbURL/*ConfigUtils.getDbUrl()*/);
          if (conn != null) {
              System.out.println("Connected to the database");
              DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
              System.out.println("Driver name: " + dm.getDriverName());
              System.out.println("Driver version: " + dm.getDriverVersion());
              System.out.println("Product name: " + dm.getDatabaseProductName());
              System.out.println("Product version: " + dm.getDatabaseProductVersion());

              return conn;
          }
      } catch (ClassNotFoundException ex) {
          ex.printStackTrace();
      } catch (SQLException ex) {
          ex.printStackTrace();
      }
	  return null;
  }
	
	
	/**
	 * 
	 * @throws SQLException
	 */
	protected void closeConnection() throws SQLException{

	//	if(!conn.isClosed())
		//	conn.close();
		
	}
	
	/**
	 * @note : this methods updates db by checking last row date with data in out Receipt 
	 * @throws SQLException
	 */
	public void updatedb(String className) throws SQLException{
	//	className = "NAB";
		List<CustomItem> items= new ArrayList<>();
		List<CustomItem> newItems = null;
		List<Settlement> stlLsit= new ArrayList<>();
		List<Settlement> newSettel= null;
	    Connection conn = openConnection();
		String sql = CustomItem.gettheLastOne();
		String lastSettlement= Settlement.gettheLastOne();
		ResultSet  set = conn.createStatement().executeQuery(sql);
	
		Long  time=null;
		Long timeSettlements= null;
		
	if(set.next()) {
		  time = set.getLong("dateTime");
	     }
	ResultSet  setSettlement = conn.createStatement().executeQuery(lastSettlement);
	if(setSettlement.next()) {
		 timeSettlements= setSettlement.getLong("dateTime");
	         }
		    //Set Items here by configuration file 
		if(className.equals(ConfigUtils.SERVERS.NAB.toString())){
		    	NABLogHelper nh= new NABLogHelper();
		    	items=nh.items;
		    	stlLsit= nh.listSettlements;
		    }else if(className.equals(ConfigUtils.SERVERS.WESTPAC.toString())){
		    	WestpacLogHelper wh= new WestpacLogHelper();
		    	items=wh.items;	
		    	stlLsit = wh.settlements;
		    	
		    	}
		 
		    
		    
		    	//Check if database not empty 
		    if(time!=null){
			    Date  date = new Date(time); //Last entry dateTime 
			    newItems = getItemAfter(items,date);
		       }
		    
	if(timeSettlements!=null){	
		     Date d = new Date(timeSettlements);
		  newSettel = getSettelmentAfter(stlLsit,d);
	}
		  
			//If anything exist update the db 
		if(newItems!=null){
			
			for(CustomItem item: newItems){
				String insertSql = item.createStamenet();
				System.out.println(insertSql);
				conn.createStatement().executeUpdate(insertSql);
			}
			
		}else 
			if(time==null){  //if ResultSet returns empty it means first time so create db for system
		    	for(CustomItem item: items){
				String insertSql = item.createStamenet();
				System.out.println(insertSql);
				conn.createStatement().executeUpdate(insertSql);
			}
		}else
			System.out.println("nothing to update Items ");

		//Settlement
		if(newSettel!=null){
			for(Settlement s: newSettel){
				   String insert = s.insert();
				   conn.createStatement().executeUpdate(insert);	
			}
			
		}else
			if(timeSettlements==null && stlLsit!=null){
				for(Settlement s: stlLsit){
					   String insert = s.insert();
					   conn.createStatement().executeUpdate(insert);	
				}
			
			}else {
				System.out.println("nothing to update Settlment ");
			}
		System.out.println("END ");
		conn.close();
		//Close db connection and clean up 
	//	clodeConnection();
			
		
		
		
		
		
	}
	
	
	/**
	 * @note : get all the items after the last entry into db 
	 * @param items
	 * @param date
	 * @return List CustomItem 
	 */
	public List<CustomItem> getItemAfter(List<CustomItem> items,Date date){
		List<CustomItem> after= new ArrayList<>();
		for(CustomItem item : items){
			if(item.getDateTime().after(date))
				after.add(item);

		}
		return after;
	}


	

	/**
	 * @note : get all the items after the last entry into db 
	 * @param items
	 * @param date
	 * @return List CustomItem 
	 */
	public List<Settlement> getSettelmentAfter(List<Settlement> items,Date date){
		List<Settlement> after= new ArrayList<>();
		for(Settlement item : items){	
			if(item.getDate()!=null && item.getDate().after(date)) {
				after.add(item);
			}
		}
		return after;
	}

	
	
	
	
}




