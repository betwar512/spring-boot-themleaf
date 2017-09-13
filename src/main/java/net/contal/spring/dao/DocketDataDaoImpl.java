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
	public List<DocketDataDao> getAllItems(BankType bankType) {
		
	return this.sessionFactory
			.getCurrentSession()
			.createCriteria(DocketLog.class)
			.add(Restrictions.eq("bankType", bankType))
			.list();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<SettlementLog> getAllSettlments(BankType entity) {
	return this.getSession().createCriteria(SettlementLog.class).list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DocketLog> getByDate(Date dateFrom, Date dateTo, BankType bankType) {
		List<DocketLog> list = this.getSession()
									.createCriteria(DocketLog.class)
									.add(Restrictions.between("dateTime", dateFrom, dateTo))
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
				.createCriteria(BankType.class).add(Restrictions.eq("name", name)).list();

		return entities != null && !entities.isEmpty() ? entities.get(0) : null;
	}


	@Override
	public Date findLastDocketDate(BankType entity) {
		@SuppressWarnings("unchecked")
		List<DocketLog> docketsOrderedByDate = this.getSession()
											            .createCriteria(DocketLog.class)
														.addOrder(Order.desc("dateTime"))
														.setMaxResults(1)
														.list();
	return docketsOrderedByDate != null && !docketsOrderedByDate.isEmpty() ?
			docketsOrderedByDate.get(0).getDateTime() :
				null;
	}


	@Override
	public int docketCount(BankType entity) {
	return this.getAllItems(entity).size();
	
	}


	@Override
	public int settlementsCount(BankType entity) {
		return this.getAllSettlments(entity).size();
	}
	
	
	
	
	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}


	@Override
	public Date findLastSettlementDate(BankType entity) {
		  @SuppressWarnings("unchecked")
		List<SettlementLog> list =    this.getSession()
				  .createCriteria(SettlementLog.class).
				  addOrder(Order.desc("date"))
				  .setMaxResults(1)
				  .list();
		return list!= null && !list.isEmpty() ? list.get(0).getDate() : null;
	}


	@Override
	public void saveOrUpdate(Object object) {
		this.getSession().saveOrUpdate(object);
		
	}
	
	
	
	
	

}
