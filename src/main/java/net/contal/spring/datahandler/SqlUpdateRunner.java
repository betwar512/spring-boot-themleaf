package net.contal.spring.datahandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import net.contal.spring.model.CustomItem;



/**
 * 
 * @author A.H.Safaie 
 *
 */
public class SqlUpdateRunner  implements Runnable {

	SqliteDbHandler db;

	
	public SqlUpdateRunner(){	
		 ResultSet        set = null;
		 Statement  statement = null;
		 Connection        cn = null;
		try {
			this.db = new SqliteDbHandler();
			  cn = this.db.conn;
			String     sql =  CustomItem.getCount();
		         statement = cn.createStatement();
		               set = statement.executeQuery(sql); 
		    set.next();
			int  total = 0;
			
			if(!set.isClosed()) {
				total=	set.getInt("total");
			   }
			
			if(total == 0){
				   update();
				//   updated=true;
				   }
		      } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(set != null) {
				     statement.close();
				           set.close();	
				           cn.close();
				 }
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
		String	serverType = "NAB";
			try {
				db.updatedb(serverType);
			} catch (SQLException e) {
	
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
