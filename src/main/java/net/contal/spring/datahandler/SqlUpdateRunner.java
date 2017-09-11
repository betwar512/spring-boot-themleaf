package net.contal.spring.datahandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import net.contal.spring.model.CustomItem;
import net.contal.spring.util.ConfigUtils;


/**
 * 
 * @author A.H.Safaie 
 *
 */
public class SqlUpdateRunner  implements Runnable {
	
	SqliteDbHandler db;
	public boolean updated=false;
	public SqlUpdateRunner(){
		try {
			this.db= new SqliteDbHandler();
			//String checker = createTable();
			//this.db.stmt.executeQuery(checker);
			Connection cn =SqliteDbHandler.openConnection();
			String     sql =  CustomItem.getCount();
		    ResultSet  set =  cn.createStatement().executeQuery(sql);   
			int  total = 0;
			if(!set.isClosed())
				total=	set.getInt("total");
			
			cn.close();		
			if(total == 0){
				   update();
				   updated=true;}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public void run(){	
		System.out.println("Start again : " + new Date().toString());
		update();
	}

	/**
	 * @note : update db 
	 */
		public  void update(){
		String	serverType = ConfigUtils.getServer();
			try {
				db.updatedb(serverType);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		
 /**
 * @note Time 
 */
   public static long millisToNextHour(Calendar calendar) {
		    int minutes = calendar.get(Calendar.MINUTE);
		    int seconds = calendar.get(Calendar.SECOND);
		    int millis = calendar.get(Calendar.MILLISECOND);
		    int minutesToNextHour = 60 - minutes;
		    int secondsToNextHour = 60 - seconds;
		    int millisToNextHour = 1000 - millis;
		    return minutesToNextHour*60*1000 + secondsToNextHour*1000 + millisToNextHour;
		}
}
