package net.contal.spring.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import net.contal.spring.model.DocketLog;
import net.contal.spring.model.BankType;
import net.contal.spring.model.SettlementLog;

@Repository
@Transactional
public class DocketDataDaoImpl implements DocketDataDao {
	
	@Autowired
     SessionFactory sessionFactory;

	
	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<DocketLog> getAllItems(BankType bankType) {
		
	return this.sessionFactory
			.getCurrentSession()
			.createCriteria(DocketLog.class)
			.add(Restrictions.eq("bankType", bankType))
			.addOrder(Order.desc("dateTime"))
			.list();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<SettlementLog> getAllSettlments(BankType bankType) {
	return this.getSession()
			.createCriteria(SettlementLog.class)
			.add(Restrictions.eq("bankType", bankType))
			.addOrder(Order.desc("date"))
			.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DocketLog> getDocketsByDate(Date dateFrom, Date dateTo, BankType bankType) {
		List<DocketLog> list = this.getSession()
									.createCriteria(DocketLog.class)
									.add(Restrictions.between("dateTime", dateFrom, dateTo))
									.add(Restrictions.eq("bankType", bankType))
									.list();
		 return list != null ? list : new ArrayList<>();
	}

	@Override
	public List<SettlementLog> getSettleByDate(Date dateFrom, Date dateTo, BankType bankType) {
		@SuppressWarnings("unchecked")
		List<SettlementLog> list = this.getSession()
				.createCriteria(SettlementLog.class)
				.add(Restrictions.between("date", dateFrom, dateTo))
				.add(Restrictions.eq("bankType", bankType))
				.list();
        return list != null ? list : new ArrayList<>();
	}
	
	

	@Override
	@SuppressWarnings("unchecked")
	public BankType loadBank(String name) {
		if(this.getSession() == null) {
			System.out.println("Session is null");
		}
		
		List<BankType> entities = this.getSession()
				.createCriteria(BankType.class)
				.add(Restrictions.eq("name", name)).list();

		return entities != null && !entities.isEmpty() ? entities.get(0) : null;
	}


	@Override
	public Date findLastDocketDate(BankType bankType) {
		@SuppressWarnings("unchecked")
		List<DocketLog> docketsOrderedByDate = this.getSession()
											            .createCriteria(DocketLog.class)
											            .add(Restrictions.eq("bankType", bankType))
														.addOrder(Order.desc("dateTime"))
														.setMaxResults(1)
														.list();
	return docketsOrderedByDate != null && !docketsOrderedByDate.isEmpty() ?
			docketsOrderedByDate.get(0).getDateTime() :
				null;
	}


	@Override
	public int docketCount(BankType bankType) {
	return this.getAllItems(bankType).size();
	
	}


	@Override
	public int settlementsCount(BankType bankType) {
		return this.getAllSettlments(bankType).size();
	}
	
	
	
	
	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}


	@Override
	public Date findLastSettlementDate(BankType bankType) {
		  @SuppressWarnings("unchecked")
		List<SettlementLog> list =    this.getSession()
				  .createCriteria(SettlementLog.class)
				  .add(Restrictions.eq("bankType", bankType))
				  .addOrder(Order.desc("date"))
				  .setMaxResults(1)
				  .list();
		return list!= null && !list.isEmpty() ? list.get(0).getDate() : null;
	}


	@Override
	public void saveOrUpdate(Object object) {
		this.getSession().saveOrUpdate(object);
		
	}



}
