package net.contal.spring.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import net.contal.spring.model.BankType;

public class BankTypeDaoImpl {
	@Autowired
    SessionFactory sessionFactory;
	@Value("${fpos.bank.name}")
	public String bankName;
	
	@SuppressWarnings("unchecked")
	public BankType loadProjectBank() {
	List<BankType> list =   this.sessionFactory
								.getCurrentSession()
								.createCriteria(BankType.class)
		                         .add(Restrictions.eq("name",bankName))
		                         .list();
		return list!= null && !list.isEmpty() ? list.get(0) : null;
	}
	

}
