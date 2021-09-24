package com.enterprise.service;

import com.enterprise.model.EOState;
import com.enterprise.object.EOObject;

/**
 * {@link} EOStateService used to perform oparation on {@link EOState} Object
 * @author rajkumar
 *
 */
public class EOStateService extends EOObject{
	
	/**
	 * 
	 * @param json
	 */
	public void saveState(String json) {
		EOState eoState = (EOState) this.mapToObject(new EOState(), this.jsonToMap(json));
		this.saveObject(eoState);
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	public EOState updateState(String json) {
		EOState eoState = (EOState) this.mapToObject(new EOState(), this.jsonToMap(json));
		return (EOState)this.updateObject(eoState);
	}
	
	/**
	 * 
	 * @param json
	 * @return
	 */
	public EOState deleteState(String json) {
		EOState eoState = (EOState) this.mapToObject(new EOState(), this.jsonToMap(json));
		return (EOState)this.deleteObject(eoState);
	}
}
