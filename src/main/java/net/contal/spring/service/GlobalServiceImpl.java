package net.contal.spring.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import net.contal.spring.dao.BankTypeDao;
import net.contal.spring.model.BankType;

@Repository
@Transactional
public class GlobalServiceImpl implements GlobalService {

	@Autowired
	BankTypeDao bankDao;
	
	@Override
	public BankType loadBankType() {
		return bankDao.loadProjectBank();
	}

}
