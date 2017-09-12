package net.contal.spring.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.google.gson.JsonObject;


/**
 * 
 * @author A.H.Safaie
 *
 */
public class CustomItem implements Comparator<CustomItem>{

	private String merchantId;
	private String terminalId;
	private Date dateTime;
	private String cardNumber;
	private String status;
	private String purchaseAmount;
	private Float totalAmount;
	private String cardType;
	private String date;
	
	
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	//sort by date 
	
	@Override
    public int compare(CustomItem o1, CustomItem o2) {
        // write comparison logic here like below , it's just a sample
        return o1.getDateTime().compareTo(o2.getDateTime());
    }
	
	
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPurchaseAmount() {
		return purchaseAmount;
	}
	public void setPurchaseAmount(String purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}
	public Float getTotalAmount() {
		return totalAmount;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}

	//check for null 
	public boolean isValid() {
	    return  dateTime != null && status != null && totalAmount!=null;
	  }
	
	
	public void setDateFromString(String date){
		
	if(date==null || date.length()<0)	{
				date=this.date;	
	}
	if(date!=null && date.length()>0){
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		 try { 
		Date dateDate = df.parse(date);
		 this.dateTime= dateDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		
	}
	
	
	/*-----------------------------------------------------------------------------------------------------------*/
	/*												HashCode & equals										     */
	/*-----------------------------------------------------------------------------------------------------------*/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cardNumber == null) ? 0 : cardNumber.hashCode());
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((terminalId == null) ? 0 : terminalId.hashCode());
		result = prime * result + ((totalAmount == null) ? 0 : totalAmount.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomItem other = (CustomItem) obj;
		if (cardNumber == null) {
			if (other.cardNumber != null)
				return false;
		} else if (!cardNumber.equals(other.cardNumber))
			return false;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (terminalId == null) {
			if (other.terminalId != null)
				return false;
		} else if (!terminalId.equals(other.terminalId))
			return false;
		if (totalAmount == null) {
			if (other.totalAmount != null)
				return false;
		} else if (!totalAmount.equals(other.totalAmount))
			return false;
		return true;
	}
	
	
	
	
	/*-----------------------------------------------------------------------------------------------------------*/
	/*												SQLLite Queries 											 */
	/*-----------------------------------------------------------------------------------------------------------*/

	@Override
	public String toString() {
		return "CustomItem [merchantId=" + merchantId + ", terminalId=" + terminalId + ", dateTime=" + dateTime
				+ ", status=" + status + ", date=" + date + "]";
	}

	public String createStamenet(){
		
		if(this.purchaseAmount != null &&this.purchaseAmount.startsWith("$")) {
			this.purchaseAmount =purchaseAmount.substring(1);
		}
		
		
		
		
     return "INSERT INTO CUSTOMITEM VALUES (null,'"+this.terminalId+"','"+this.cardNumber+"','"+this.merchantId+"','"+this.status+"',"
     		+this.purchaseAmount+","+this.totalAmount.toString()+",'"+this.cardType+"',"+this.dateTime.getTime()+");";
		
	
		
	}
	
	
	public static String getCount(){
		return "SELECT COUNT(*) as total FROM CUSTOMITEM ";
	}
	
	/**
	 * Get Last entry 
	 * @return
	 */
	public static String gettheLastOne(){		
	return "SELECT * FROM CUSTOMITEM ORDER BY dateTime DESC LIMIT 1";
	}
	

	/**
	 * @note get all the between in order to calculated satelments 
	 * @param start
	 * @param end
	 * @return
	 */
	public static String getBetweenOrderByDate(Date start , Date end){	
		return "SELECT * FROM CUSTOMITEM it WHERE it.dateTime > "+ start.getTime() + " AND it.dateTime < "+ end.getTime()
		+"ORDER BY it.dateTime DESC";

	}
	
	
	public static String getBetweenOrderByDate(long start , long end){	
		return "SELECT * FROM CUSTOMITEM it WHERE it.dateTime > "+ start + " AND it.dateTime < "+ end
		+" ORDER BY it.dateTime DESC";

	}
	
	private static final String GET_ALL = "SELECT * FROM CUSTOMITEM ORDER BY terminalId";
	/**
	 * @note Select All 
	 * @return
	 */
	public static String selectAll(){	
		return GET_ALL;
	}

	
	/**
	 * @note : group by terminal 
	 * @param start
	 * @param end
	 * @return
	 */
public static String groupTotalByTerminal(Date start,Date end){
	return "select sum(it.totalAMount) as total , terminalId from CUSTOMITEM it where it.dateTime > "+ start.getTime() +
				 " AND it.dateTime < " + end.getTime()+
				 " AND it.status LIKE '%APPROVED%' "+
				 " GROUP BY terminalId";
	}
	
/**
 * 
 * @param start
 * @param end
 * @return
 */
public static String groupTotalByCardType(Date start,Date end){
	
	return  "select sum(it.totalAMount) as total , cardType from CUSTOMITEM it where it.dateTime > "+ start.getTime() +
				 " AND it.dateTime < " + end.getTime()+
				 " AND it.status LIKE '%APPROVED%' "+
				 " GROUP BY cardType";
	}


	/**
	 * 
	 * @param set
	 * @return
	 * @throws SQLException
	 */
	public static CustomItem serializeItem(ResultSet set) throws SQLException{
		CustomItem item = new CustomItem();
		item.merchantId =set.getString("merchantId");
		item.terminalId=set.getString("terminalId");
		item.cardNumber= set.getString("cardNumber");
		item.status=set.getString("status");
		item.purchaseAmount=set.getString("totalAmount");
		item.totalAmount= set.getFloat("totalAmount");
		item.cardType=set.getString("cardType");
		item.dateTime=new Date(set.getLong("dateTime"));	
		return item;
	}
	
	
	

	

	/**
	 * 
	 * @param Date start
	 * @param Date end
	 * @return String sql 
	 */
	public static String selectBetweenDates(Date start , Date end){		
		return "SELECT * FROM CUSTOMITEM it WHERE it.dateTime > "+start.getTime() + " AND it.dateTime < "+ end.getTime() +
				" ORDER BY dateTime DESC" ;

	}
	
	
	
	
	
	public JsonObject toJson(){
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    JsonObject object = new JsonObject();
	    object.addProperty("merchantId", this.merchantId);
	    object.addProperty("terminalId", this.terminalId);
        object.addProperty("date",this.dateTime!=null? df.format(this.dateTime):"");
	    object.addProperty("cardNumber", this.cardNumber);
	    object.addProperty("status", this.status);
	    object.addProperty("purchaseAmount", this.purchaseAmount);
	    object.addProperty("totalAmount", this.totalAmount);
	    object.addProperty("cardType", this.cardType);    
	return object;
}
	
	
	
	
}
