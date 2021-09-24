/**
 * 
 */
package com.enterprise.object;

import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import com.enterprise.exception.EnterpriseException;
import com.enterprise.jpa.PersistenceFactory;
import com.enterprise.util.EnterpriseUtil;

/**
 * {@link EOObject} is root object class for all service class
 * 
 * @author rajkumar
 *
 */
public class EOObject implements PersistenseInterface {
	
	/**
	 * convert map into object
	 * @param object
	 * @param map
	 * @return
	 */
    public Object mapToObject(Object object,HashMap<String, Object> map) {
    	return EnterpriseUtil.getObjectFromMap(object, map);
    }
    
    /**
     * convert map into rel object
     * @param object
     * @param map
     * @return
     */
    public Object getRelObjectFromMap(Object object,HashMap<String, Object> map) {
    	return EnterpriseUtil.getRelObjectFromMap(object, map);
    }
    
    /**
     * convert json to map
     * @param json
     * @return
     */
    public HashMap<String, Object> jsonToMap(String json){
    	return EnterpriseUtil.jsonStringToMap(json);
    }
    
    /**
     * convert map to json
     * @param map
     * @return
     */
    public String mapToJson(HashMap<String, Object> map){
    	return EnterpriseUtil.mapToJsonString(map);
    }
    
	/**
	 * return {@link EntityManagerFactory} object
	 */
	@Override
	public EntityManagerFactory reqEMFactory() {
		return PersistenceFactory.getEntityManagerFactory();
	}

	/**
	 * return {@link EntityManager} object
	 */
	@Override
	public EntityManager reqEManager() {
		return PersistenceFactory.getEntityManager();
	}

	/**
	 * return {@link EntityTransaction} object
	 */
	@Override
	public EntityTransaction getTransaction() {
		return PersistenceFactory.getTransaction();
	}

	/**
	 * start transaction
	 */
	@Override
	public void startTransaction() {
		PersistenceFactory.startTransaction();
	}

	/**
	 * end transaction
	 */
	@Override
	public void endTransaction() {
		PersistenceFactory.endTransaction();
	}
	
	/**
	 * this method id used to save object into database;
	 * 
	 * @param object
	 */
	public void saveObject(Object object) {
		startTransaction();
		reqEManager().persist(object);
		endTransaction();
	}

	/**
	 * Generic method used to delete any entity object from database
	 * 
	 * @param object
	 * @return
	 */
	public Object deleteObject(Object object) {
		Object deleteObject = reqEManager().find(object.getClass(), EnterpriseUtil.getPrimaryKey(object));
		if (deleteObject != null) {
			startTransaction();
			reqEManager().remove(deleteObject);
			endTransaction();
			return deleteObject;
		} else {
			throw new EnterpriseException("Object Not Found");
		}
	}

	/**
	 * Generic method used to update persistence entity
	 * 
	 * @param sourceObject
	 * @return
	 */
	public Object updateObject(Object sourceObject) {
		try {
			Object targetObject = reqEManager().find(sourceObject.getClass(),
					EnterpriseUtil.getPrimaryKey(sourceObject));
			startTransaction();
			Object target = EnterpriseUtil.copyObject(sourceObject, targetObject);
			endTransaction();
			return target;
		} catch (Exception e) {
			throw new EnterpriseException("Exception while updating...");
		}
	}


}
