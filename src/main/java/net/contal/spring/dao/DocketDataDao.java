package net.contal.spring.dao;

import java.util.Date;

public interface DocketDataDao {

	
	public void getAllItems();
	public void getAllSettlments();
	public void getByDate(Date dateFrom , Date dateTo );
	
	
	
	
}
