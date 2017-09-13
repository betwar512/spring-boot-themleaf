package net.contal.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.contal.spring.model.BankType;
import net.contal.spring.service.GlobalService;


@Component
public class MasterController {

		@Autowired
		GlobalService gService;
	

	/**
	 * <p>Load bank for this project </p>
	 * @return
	 */
	protected BankType loadBank() {
		return  gService.loadBankType();
	}
	

	
}
