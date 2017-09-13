package net.contal.spring.service;

import java.util.Date;

public interface HibernateDataService {
	
	/**
	 * 
	 * @param entityName
	 */
	public void checkDatabase(String entityName);
	/**
	 * 
	 * @return
	 */
	public Date getLastDocketDate();
	/**
	 * 
	 * @return
	 */
	public Date getLastSettlementDate();
	/**
	 * 
	 * @param entityName
	 */
	public void updateDatabase(String entityName);

	public int getCountForDockets();
	
	public int getCountForSettlements();
}
