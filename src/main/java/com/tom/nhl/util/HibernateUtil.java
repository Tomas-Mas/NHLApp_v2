package com.tom.nhl.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	public static EntityManagerFactory emf;
	
	public static EntityManagerFactory getFactory() {
		if(emf == null) {
			Configuration config = new Configuration();
			config.configure();
			emf = config.buildSessionFactory();
		}
		return emf;
	}
	
	public static EntityManager createEntityManager() {
		EntityManagerFactory emf = getFactory();
		return emf.createEntityManager();
	}
}