package net.contal.spring.dao;

import java.util.Date;
import java.util.List;

import net.contal.spring.model.BankType;
import net.contal.spring.model.DocketLog;
import net.contal.spring.model.SettlementLog;

public interface DocketDataDao {

	/**
	 * <p>
	 * Return all the items in docket table
	 * </p>
	 * 
	 * @param entity
	 * @return
	 */
	public List<DocketDataDao> getAllItems(BankType entity);

	/**
	 * <p>
	 * Return all the items in Settlement table
	 * </p>
	 * 
	 * @param entity
	 * @return
	 */
	public List<SettlementLog> getAllSettlments(BankType entity);

	/**
	 * 
	 * @param dateFrom
	 * @param dateTo
	 * @param entity
	 * @return
	 */
	public List<DocketLog> getByDate(Date dateFrom, Date dateTo, BankType entity);

	/**
	 * <p>
	 * Find entity by name
	 * </p>
	 * 
	 * @param name
	 * @return LegalEntity
	 */
	public BankType loadBank(String name);

	/**
	 * <p>
	 * Find last Row date for Dockets
	 * </p>
	 * 
	 * @param entity
	 * @return
	 */
	public Date findLastDocketDate(BankType bankType);

	/**
	 * <p>
	 * Find last Row date for Dockets
	 * </p>
	 * 
	 * @param entity
	 * @return
	 */
	public Date findLastSettlementDate(BankType bankType);

	/**
	 * <p>
	 * Table size
	 * </p>
	 * 
	 * @param entity
	 * @return
	 */
	public int docketCount(BankType bankType);

	/**
	 * <p>
	 * Table size
	 * </p>
	 * 
	 * @param entity
	 * @return
	 */
	public int settlementsCount(BankType bankType);

	/**
	 * <p>
	 * Save to DB
	 * </p>
	 * 
	 * @param object
	 */
	public void saveOrUpdate(Object object);

}
