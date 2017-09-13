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

	private void setBankTypeForClass() {
			if(this.bankType == null) {
				String bankName =  ConfigUtils.BANKS.get(bankKey).getBankName();
				this.bankType =  this.docketDao.loadBank(bankName);
			}
			
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
		setBankTypeForClass();
		//check count if first time than update all 
		int count = docketDao.docketCount(this.bankType);
		if(count == 0) {
			updatedb();
		}
	}
	
	
	@Override
	public Date getLastDocketDate() {	
		setBankTypeForClass();
		return this.docketDao.findLastDocketDate(this.bankType);
	}

	@Override
	public Date getLastSettlementDate() {
	  setBankTypeForClass();
	  return this.docketDao.findLastSettlementDate(this.bankType);
	}

	@Override
	public void updateDatabase(String bankType) {
		setBankTypeForClass();
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
			Date settlementDate = this.docketDao.findLastSettlementDate(this.bankType);
			Date    compareDate = this.docketDao.findLastDocketDate(this.bankType);
			
		    //Set Items here by configuration file 
		if(ConfigUtils.ENTITIES.XY.getEntityName().equals(this.bankType.getTradingName())){
		    	NABLogHelper nh= new NABLogHelper();
		      items = nh.getItems();
		    	stlLsit = nh.getListSettlements();
		    }else
		    	   if(ConfigUtils.ENTITIES.ELIXIR.getEntityName().equals(this.bankType.getTradingName())){
		      	  WestpacLogHelper wh = new WestpacLogHelper();
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
			 dl.setBankType(this.bankType);
			this.docketDao.saveOrUpdate(dl);
		}
	}
	
	
	private void saveSettlementToDb(List<SettlementDto> stlLsit) {
		for(SettlementDto s: stlLsit){
		    SettlementLog sl = s.parsToModel();
		    sl.setBankType(this.bankType);
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
		this.setBankTypeForClass();
		return this.docketDao.docketCount(this.bankType);
	}

	@Override
	public int getCountForSettlements() {
		this.setBankTypeForClass();
		return  this.docketDao.settlementsCount(this.bankType);
	}


}
