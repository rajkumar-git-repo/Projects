package com.enterprise.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * {@link} PersistenceFactory is a factory class which return singleton object 
 * @author rajkumar
 *
 */
public class PersistenceFactory {

	private final static String PERSISTENCE_UNIT = "ENTERPRISE";
	private static volatile EntityManagerFactory entityManagerFactory = null;
	private static EntityManager entityManager = null;
	
	/**
	 * @return {@link EntityManagerFactory} singleton object
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		if(entityManagerFactory == null) {
			synchronized (PersistenceFactory.class) {
				if(entityManagerFactory == null) {
					entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
				}
			}
		}
		return entityManagerFactory;
	}
	
    /**
     * 
     * @return {@link} EntityManager singleton object
     */
	public static EntityManager getEntityManager() {
		if(entityManager == null) {
			entityManager = getEntityManagerFactory().createEntityManager();
		}
		return entityManager;
	}
	
	/**
	 * 
	 * @return {@link} EntityTransaction object
	 */
	public static EntityTransaction getTransaction() {
		return getEntityManager().getTransaction();
	}
	
	/**
	 * Method is used to begin transaction
	 */
	public static void startTransaction() {
		getTransaction().begin();
	}
	
	/**
	 * Method is used to commit transaction
	 */
	public static void endTransaction() {
		getTransaction().commit();
	}
	
}
