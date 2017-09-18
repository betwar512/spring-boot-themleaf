package net.contal.spring.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.contal.spring.model.DocketLog;
import net.contal.spring.model.SettlementLog;
import net.contal.spring.service.HibernateDataService;


@RestController
public class DocketController {
	
	private static final Logger logger = Logger.getLogger(DocketController.class); 
	
	@Autowired
	public HibernateDataService hibernateService;
	
	
	@RequestMapping(value="/json/dockets")
	public List<DocketLog> getAllDockets(){	
		return this.hibernateService.getDockets();
	}
	
	@RequestMapping(value="/json/sett")
	public List<SettlementLog> getAllSettlements(){	
		return this.hibernateService.getSettlements();
	}
	
	
	@RequestMapping(value="/json/dockebydate")
	public List<DocketLog> loadByDate(Date dateFrom,Date dateTo) {
		logger.info(dateFrom + "   " + dateTo);
		   return  this.hibernateService.findByDate(dateFrom, dateTo);
	}
	
	
	@RequestMapping(value="/json/settlebydate")
	public List<SettlementLog> loadSettleByDate(Date dateFrom,Date dateTo) {
		logger.info(dateFrom + "   " + dateTo);
		   return  this.hibernateService.findSettleByDate(dateFrom, dateTo);
	}
	
	
	@RequestMapping(value="/json/dockets/count")
	public int getCountItems() {
		return this.hibernateService.getCountForDockets();
	}
	
	
	
	
	@RequestMapping(value="/json/update")
	public void updateDatabase() {
			this.hibernateService.updateDatabase();
	}
	
	
	@RequestMapping(value="/json/sett/count")
	public int getCountSettlements() {
		return this.hibernateService.getCountForSettlements();
	}
	
	
	
	
	
}
