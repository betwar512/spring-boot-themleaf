package net.contal.spring.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

@Configuration
@EntityScan({"net.contal.spring.model" })
public class DatabaseConfig {

	  @Bean
	    public HibernateJpaSessionFactoryBean sessionFactory(EntityManagerFactory emf) {
	        HibernateJpaSessionFactoryBean fact = new HibernateJpaSessionFactoryBean();
	    
	        fact.setEntityManagerFactory(emf);
	        return fact;
	    }
 }
