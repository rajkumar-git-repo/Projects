package com.enterprise.service;

import java.util.HashMap;

import com.enterprise.model.EOAddress;
import com.enterprise.model.EOPerson;
import com.enterprise.object.EOObject;

public class EOPersonService extends EOObject {
	/**
	 * @param json
	 */
	public void savePerson(String json) {
		EOPerson eoPerson = (EOPerson) this.mapToObject(new EOPerson(), this.jsonToMap(json));
		this.saveObject(eoPerson);
	}

	/**
	 * @param json
	 */
	public EOPerson updatePerson(String json) {
		EOPerson eoPerson = (EOPerson) this.mapToObject(new EOPerson(), this.jsonToMap(json));
		return (EOPerson)this.updateObject(eoPerson);
		
	}

	/**
	 * @param json
	 * @return
	 */
	public EOPerson deletePerson(String json) {
		EOPerson eoPerson = (EOPerson) this.mapToObject(new EOPerson(), this.jsonToMap(json));
		return (EOPerson)this.deleteObject(eoPerson);
	}

	/**
	 * @param json
	 */
	public EOAddress saveAddress(String json) {
		HashMap<String, Object> map = this.jsonToMap(json);
		EOPerson eoPerson = this.reqEManager().find(EOPerson.class, map.get("eoPerson"));
		EOAddress eoAddress = this.reqEManager().find(EOAddress.class, map.get("eoAddress"));
		eoAddress.setEoPerson(eoPerson);
		this.updateObject(eoAddress);
		return eoAddress;
	}
}
