package net.contal.spring.service;

import java.util.Date;
import java.util.List;

import net.contal.spring.model.DocketLog;
import net.contal.spring.model.SettlementLog;

public interface HibernateDataService {
	
	/**
	 * 
	 * @param entityName
	 */
	public void checkDatabase(String entityName);
	
	public List<DocketLog> findByDate(Date dateFrom,Date dateTo);
	public List<SettlementLog> findSettleByDate(Date dateFrom,Date dateTo);
	/**
	 * 
	 * @return LastDate
	 */
	public Date getLastDocketDate();
	/**
	 * 
	 * @return lastDate
	 */
	public Date getLastSettlementDate();
	/**
	 * 
	 * @param entityName
	 */
	public void updateDatabase();
/**
 * Get count for Items 
 * @return int
 */
	public int getCountForDockets();
	
	/**
	 * Get count for settlements 
	 * @return int
	 */
	public int getCountForSettlements();
	/**
	 * <p>Return all the items belong to this bank 
	 * @return
	 */
	public List<DocketLog> getDockets();
	
	public List<SettlementLog> getSettlements();
	
}
