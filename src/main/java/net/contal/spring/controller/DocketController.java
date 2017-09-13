package net.contal.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.contal.spring.model.DocketLog;
import net.contal.spring.service.HibernateDataServiceImpl;
import net.contal.spring.utils.ConfigUtils;

@RestController
public class DocketController extends MasterController {
	@Autowired
	public HibernateDataServiceImpl hibernateService;
	
	
	@RequestMapping(value="/json/dockets")
	public List<DocketLog> returnThisBudy(){	
		String entityName =  ConfigUtils.ENTITIES.ELIXIR.getEntityName();
		hibernateService.checkDatabase(entityName);
		return null;
	}
	
	@RequestMapping(value="/json/dockets/count")
	public int getCount() {
		return this.hibernateService.getCountForDockets();
	}
	
	
	
	
}
