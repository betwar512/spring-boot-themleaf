package net.contal.spring.dao;

import java.util.Date;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class DocketDataDaoImpl implements DocketDataDao {
	
	@Autowired
     SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}

	
	@Override
	public void getAllItems() {
		System.out.println(this.sessionFactory.getCurrentSession().getStatistics().toString());


	}

	@Override
	public void getAllSettlments() {
	}

	@Override
	public void getByDate(Date dateFrom, Date dateTo) {


	}

}
