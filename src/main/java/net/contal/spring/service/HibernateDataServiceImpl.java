package net.contal.spring.service;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import net.contal.spring.dao.DocketDataDao;
import net.contal.spring.dto.CustomItemDto;
import net.contal.spring.dto.SettlementDto;
import net.contal.spring.logreader.NABLogHelper;
import net.contal.spring.logreader.WestpacLogHelper;
import net.contal.spring.model.DocketLog;
import net.contal.spring.model.BankType;
import net.contal.spring.model.SettlementLog;
import net.contal.spring.utils.ConfigUtils;

/**
 * <p>Class responsible for updating database from logs 
 * @author A.H.Safaie
 *
 */
@Service
 public class HibernateDataServiceImpl implements HibernateDataService ,  Runnable {
	
	
	@Value("${fpos.bank.name}")
	private String bankKey ;
	
		BankType bankType;
		@Autowired
		DocketDataDao docketDao;
		@Value("${fpos.logs.url.nab}")
		private  String nabLogsUrl ;
		@Value("${fpos.logs.url.westpac}")
		private  String westpacLogsUrl ;

	private void setBankTypeForClass() {
			if(this.bankType == null) {
				String bankName =  ConfigUtils.BANKS.get(bankKey).getBankName();
				this.bankType =  this.docketDao.loadBank(bankName);
			}
			
		}
		
	
	private BankType getBankType() {
		if(this.bankType == null) {
			this.setBankTypeForClass();
		}
		return this.bankType;
	}
		/**
		 * Only run if You want to update database 
		 */
	@Override
	public void run() {
		updatedb();
	}
	
	@Override
	public void checkDatabase(String bankName) {
		//check count if first time than update all 
		int count = docketDao.docketCount(this.getBankType());
		if(count == 0) {
			updatedb();
		}
	}
	
	
	@Override
	public Date getLastDocketDate() {	
		return this.docketDao.findLastDocketDate(this.getBankType());
	}

	@Override
	public Date getLastSettlementDate() {
	  return this.docketDao.findLastSettlementDate(this.getBankType());
	}

	@Override
	public void updateDatabase() {
		updatedb();
	}
	
	
	
	/**
	 * @note : this methods updates db by checking last row date with data in out Receipt 
	 * @throws SQLException
	 */
	private void updatedb() {
		List<CustomItemDto>    items = new ArrayList<>();
		List<CustomItemDto> newItems = null;
		List<SettlementDto>  stlLsit = new ArrayList<>();
		List<SettlementDto> newSettel= null;
		//Find last settlement 
			Date settlementDate = this.docketDao.findLastSettlementDate(this.getBankType());
			Date    compareDate = this.docketDao.findLastDocketDate(this.getBankType());
			
		    //Set Items here by configuration file 
		if(ConfigUtils.BANKS.NAB.getBankName().equals(this.getBankType().getName())){
		    	NABLogHelper nh= new NABLogHelper(this.nabLogsUrl);
		      items = nh.getItems();
		    	stlLsit = nh.getListSettlements();
		    }else
		    	   if(ConfigUtils.BANKS.WESTPAC.getBankName().equals(this.getBankType().getName())){
		      	  WestpacLogHelper wh = new WestpacLogHelper(this.westpacLogsUrl);
		            items = wh.getItems();	
		          stlLsit = wh.getSettlements();
		    	}

		    	//Check if database not empty 
		    if(compareDate!=null){
			    newItems = getItemAfter(items,compareDate);
		       }
		    
	if(settlementDate!=null){	
		  newSettel = getSettelmentAfter(stlLsit,settlementDate);
	}
		  
			//If anything exist update the db 
	if(newItems!=null){		
		saveDocketToDb(newItems);
		}else 
			if(compareDate == null){  //if ResultSet returns empty it means first time so create db for system
				saveDocketToDb(items);
		}else {
			System.out.println("nothing to update Items ");
		 }
		//Settlement
		if(newSettel!=null){
			saveSettlementToDb(newSettel);
		}else
			if(settlementDate == null && stlLsit!=null){		
					saveSettlementToDb(stlLsit);
			}else {
				System.out.println("nothing to update Settlment ");
			}
		System.out.println("END ");
	}	

	private void saveDocketToDb(List<CustomItemDto> items) {
		
	  	for(CustomItemDto item: items){
	   		 DocketLog dl = item.parsToModel();
			 dl.setBankType(this.getBankType());
			this.docketDao.saveOrUpdate(dl);
		}
	}
	
	
	private void saveSettlementToDb(List<SettlementDto> stlLsit) {
		for(SettlementDto s: stlLsit){
		    SettlementLog sl = s.parsToModel();
		    sl.setBankType(this.getBankType());
		   this.docketDao.saveOrUpdate(sl);
	     }
	}
	
	
	
	/**
	 * @note : get all the items after the last entry into db 
	 * @param items
	 * @param date
	 * @return List CustomItem 
	 */
	private List<SettlementDto> getSettelmentAfter(List<SettlementDto> items,Date date){
		List<SettlementDto> after= new ArrayList<>();
		for(SettlementDto item : items){	
			if(item.getDate()!=null && item.getDate().after(date)) {
				after.add(item);
			}
		}
		return after;
	}

	
	
	/**
	 * @note : get all the items after the last entry into db 
	 * @param items
	 * @param date
	 * @return List CustomItem 
	 */
	private List<CustomItemDto> getItemAfter(List<CustomItemDto> items,Date date){
		List<CustomItemDto> after= new ArrayList<>();
		for(CustomItemDto item : items){
			if(item.getDateTime().after(date))
				after.add(item);

		}
		return after;
	}

	@Override
	public int getCountForDockets() {
		return this.docketDao.docketCount(this.getBankType());
	}

	@Override
	public int getCountForSettlements() {
		return  this.docketDao.settlementsCount(this.getBankType());
	}

	@Override
	public List<DocketLog> getDockets() {
		return this.docketDao.getAllItems(getBankType());
	}


	@Override
	public List<SettlementLog> getSettlements() {
	return	this.docketDao.getAllSettlments(this.getBankType());
	}


	@Override
	public List<DocketLog> findByDate(Date dateFrom, Date dateTo) {
			return	this.docketDao.getDocketsByDate(dateFrom, dateTo, getBankType());
	}


	@Override
	public List<SettlementLog> findSettleByDate(Date dateFrom, Date dateTo) {
		return	this.docketDao.getSettleByDate(dateFrom, dateTo, getBankType());
	}


}
