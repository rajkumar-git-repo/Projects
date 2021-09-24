/**
 * 
 */
package com.enterprise.object;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 * @author rajkumar
 *
 */
public interface PersistenseInterface {

	public  EntityManagerFactory reqEMFactory();
	public  EntityManager reqEManager();
	public  EntityTransaction getTransaction();
	public  void startTransaction();
	public  void endTransaction();
}

