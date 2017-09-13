package net.contal.spring.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.contal.spring.model.SettlementLog;

public class SettlementDto  implements Comparator<SettlementDto>{

	
	public String merchantId;
	//TODO change type to Date 
	public Date date;
	public String purchCount;
	public Float purchAmount;
	//TODO get list of FPOS Id 
	public String terminalId;
	public Double total;

	
	
	public SettlementLog parsToModel() {
		SettlementLog sl = new SettlementLog();
		sl.setDate(this.date);
		sl.setMerchantId(this.merchantId);
		sl.setPurchAmount(this.purchAmount);
		sl.setPurchCount(this.purchCount);
		sl.setTerminalId(this.terminalId);
		sl.setTotal(this.total);
		return sl;
	}
	
	
	/**
	 * 
	 * @param set
	 * @return
	 * @throws SQLException
	 */
	public static SettlementDto serializeItem(ResultSet set) throws SQLException{
		SettlementDto item = new SettlementDto();
		item.merchantId =set.getString("merchantId");
		item.terminalId=set.getString("terminalId");
		item.purchCount=set.getString("purchaseCount");
		item.total=set.getDouble("total");
	    item.date=set.getLong("dateTime") !=0 ? new Date(set.getInt("dateTime")) : null;
		item.setPurchAmount(set.getFloat("purchaseAmount"));		

		return item;
	}
	
	
	
	
	
	
public static String createTable(){
		
		
		return "CREATE TABLE IF NOT EXISTS SETTLEMENT("+
				   "ID               INTEGER PRIMARY  KEY AUTOINCREMENT,"+
				   " terminalId      TEXT     NULL,"+
				   " merchantId      TEXT     NULL,"+
				    "purchaseAmount  REAL     NULL,"+
				    "total           REAL     NULL,"+
				    "purchaseCount  CHAR(16) NULL,"+
				    "dateTime        INTEGER      NULL)";
		
	}


/**
 * 
 * @param Date start
 * @param Date end
 * @return String sql 
 */
public static String selectBetweenDates(Date start , Date end){
	
	String sql = "SELECT * FROM SETTLEMENT it WHERE it.dateTime > "+start.getTime() + " AND it.dateTime < "+ end.getTime() +
			" ORDER BY dateTime DESC" ;
	
	return sql;
	
}


/**
 * Get Last entry 
 * @return
 */
public static String gettheLastOne(){
	
	String sql = "SELECT * FROM SETTLEMENT ORDER BY dateTime DESC LIMIT 1";
	return sql;
}

	
	public static String terminalSettlement(Date start,Date end){
		
		String sql = "select sum(it.totalAMount) as total , terminalId from CUSTOMITEM it where it.dateTime > "+ start.getTime() +
					 " AND it.dateTime < " + end.getTime()+
					 " AND it.status LIKE '%APPROVED%' "+
					 " GROUP BY terminalId";
			return sql;
		}

	/**
	 * @query insert into settlement 
	 * @return
	 */
	public  String insert(){
		long time =0;
		if(this.date!=null)
			time=date.getTime();
		
	String sql ="INSERT INTO SETTLEMENT VALUES (null,'"+this.terminalId+"','"+this.merchantId+"','"+this.purchAmount+"','"+this.total+"','"+this.purchCount+"','"+time+"');";
		
		
		return sql;
		
	}
	
	/**
	 * 
	 * @param <E>
	 * @param Date start
	 * @param Date end
	 * @param items
	 */
	/*private static Map<Date,List<CustomItem>>  getListForDate( List<CustomItem> items){
				
		
		   Map<Date,List<CustomItem>> map = new HashMap<Date,List<CustomItem>>();
		   
		   Date nextDay = new Date();
	
		   Calendar calendar=Calendar.getInstance();
		      
		     Date  firstDate=new Date();
		   	if(items.size()>0)
	             firstDate = items.get(0).getDateTime();
		   	calendar.setTime(firstDate);
		    calendar.set(Calendar.HOUR_OF_DAY,7);
		    calendar.set(Calendar.MINUTE,0);
		    firstDate=calendar.getTime();
		    
		   	Calendar calendar2=Calendar.getInstance();
				     calendar2.setTime(firstDate);
				     calendar2.set(Calendar.HOUR_OF_DAY,7);
					 calendar2.add(calendar.DATE,1);
				
				nextDay=calendar2.getTime();
 
		 for(CustomItem item : items){

			 
			 //Get data for first Date 
			   Date date = item.getDateTime();
			   if(date.before(nextDay)&& date.after(firstDate))
			   {
			   if(map.containsKey(firstDate)){
				    map.get(firstDate).add(item);
			     }else
				   map.put(firstDate, new ArrayList<CustomItem>());
		        }//If in between 
			   else if(date.after(nextDay)){
				               firstDate = date; //Set first to nextDate 
					Calendar newCalendar = Calendar.getInstance();
				             newCalendar.setTime(firstDate);
					         newCalendar.set(Calendar.HOUR_OF_DAY,7);
					         newCalendar.set(Calendar.MINUTE,0);
					           firstDate = newCalendar.getTime(); //Set new firstDate 
							
							 newCalendar.add(newCalendar.DATE,1);   //Set new last Date 
						         nextDay = newCalendar.getTime();
						         
			   if(date.before(nextDay)&& date.after(firstDate))
			 {
					if(map.containsKey(firstDate)){
							   map.get(firstDate).add(item);
						 }else
							  map.put(firstDate, new ArrayList<CustomItem>());
							        }//If in between 
						         
			     }
		      }
				
		 return map;
		}
	*/
	
	
	
	public static Double calculateTotals(List<CustomItemDto> items){
		
		Double total = new Double(0);
		for(CustomItemDto item : items ){
			String stat = item.getStatus();
			if(stat.contains("APPROVED")){
			Double amount = item.getTotalAmount().doubleValue();	
			total = total + amount;
			}
		}
		
		
		return total;
		
	} 
	
	
	
	
	
	
	
	public String getMerchantId() {
		return merchantId;
	}
	
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getPurchCount() {
		return purchCount;
	}
	public void setPurchCount(String purchCount) {
		this.purchCount = purchCount;
	}
	public Float getPurchAmount() {
		return purchAmount;
	}
	public void setPurchAmount(Float purchAmount) {
		this.purchAmount = purchAmount;
	}
	
	
	
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
		//check for null 
		public boolean isValid() {
		    return merchantId != null || purchCount!=null;
		  }

		
		
		

		@Override
		public int compare(SettlementDto o1, SettlementDto o2) {
			// TODO Auto-generated method stub
			  return o1.getDate().compareTo(o2.getDate());
		}
}
