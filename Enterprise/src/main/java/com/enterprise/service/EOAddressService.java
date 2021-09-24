package com.enterprise.service;

import com.enterprise.model.EOAddress;
import com.enterprise.object.EOObject;

public class EOAddressService extends EOObject{
	
	/**
	 * @param json
	 */
	public void saveAddress(String json) {
		EOAddress eoAddress = (EOAddress) this.mapToObject(new EOAddress(), this.jsonToMap(json));
		this.saveObject(eoAddress);
	}

	/**
	 * @param json
	 */
	public EOAddress updateAddress(String json) {
		EOAddress eoAddress = (EOAddress) this.mapToObject(new EOAddress(), this.jsonToMap(json));
		return (EOAddress)this.updateObject(eoAddress);
	}

	/**
	 * @param json
	 * @return
	 */
	public EOAddress deleteAddress(String json) {
		EOAddress eoAddress = (EOAddress) this.mapToObject(new EOAddress(), this.jsonToMap(json));
		return (EOAddress)this.deleteObject(eoAddress);
	}

}
